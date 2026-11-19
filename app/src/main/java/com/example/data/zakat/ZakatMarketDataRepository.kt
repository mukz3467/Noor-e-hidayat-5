package com.example.data.zakat

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

enum class ExchangeRateStatus {
    SAME_CURRENCY,
    AUTOMATIC_LIVE,
    CACHED_VALID,
    CACHED_OUTDATED,
    UNSUPPORTED_PAIR,
    UNAVAILABLE,
    MANUAL_USER_PROVIDED
}

enum class MetalPriceStatus {
    AUTOMATIC_LIVE_USD,
    AUTOMATIC_CONVERTED,
    CACHED_VALID,
    CACHED_OUTDATED,
    UNAVAILABLE,
    MANUAL_USER_PROVIDED
}

data class ExchangeRateQuote(
    val baseCurrency: String,
    val targetCurrency: String,
    val rate: BigDecimal?,
    val rateDate: String,
    val fetchedAtEpochMs: Long,
    val sourceName: String,
    val status: ExchangeRateStatus,
    val isSupportedByProvider: Boolean,
    val isStale: Boolean,
    val message: String? = null
)

data class MetalsSpotQuote(
    val currencyCode: String,
    val gold24kPerGramUsd: BigDecimal?,
    val silverPurePerGramUsd: BigDecimal?,
    val gold24kPerGramLocal: BigDecimal?,
    val silverPurePerGramLocal: BigDecimal?,
    val exchangeRateUsed: ExchangeRateQuote?,
    val sourceName: String,
    val updatedAtText: String,
    val fetchedAtEpochMs: Long,
    val status: MetalPriceStatus,
    val isStale: Boolean,
    val errorMessage: String? = null
)

object ZakatMarketDataRepository {

    private const val PREFS_NAME = "noor_zakat_market_cache_v1"
    private const val KEY_FRANKFURTER_CURRENCIES = "ff_supported_currencies_json"
    private const val KEY_FRANKFURTER_CURRENCIES_TS = "ff_supported_currencies_ts"
    private const val KEY_FX_PREFIX = "fx_quote_"
    private const val KEY_METALS_USD = "metals_spot_usd_json"

    private const val STALE_THRESHOLD_MS = 24L * 60L * 60L * 1000L // 24 hours
    val TROY_OUNCE_IN_GRAMS: BigDecimal = BigDecimal("31.1034768")

    @Volatile
    var httpClientOverrideForTest: OkHttpClient? = null

    @Volatile
    var clockMillisOverrideForTest: (() -> Long)? = null

    private val defaultClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .followRedirects(true)
            .build()
    }

    private fun client(): OkHttpClient = httpClientOverrideForTest ?: defaultClient
    private fun nowMs(): Long = clockMillisOverrideForTest?.invoke() ?: System.currentTimeMillis()

    /**
     * Dynamically fetches supported currencies from Frankfurter v2 (`GET /v2/currencies`).
     */
    suspend fun fetchSupportedFrankfurterCurrencies(
        context: Context,
        forceRefresh: Boolean = false
    ): Set<String> = withContext(Dispatchers.IO) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val cachedJson = prefs.getString(KEY_FRANKFURTER_CURRENCIES, null)
        val cachedTs = prefs.getLong(KEY_FRANKFURTER_CURRENCIES_TS, 0L)
        if (!forceRefresh && !cachedJson.isNullOrBlank() && (nowMs() - cachedTs) < STALE_THRESHOLD_MS) {
            parseFrankfurterCurrenciesJson(cachedJson).takeIf { it.isNotEmpty() }?.let { return@withContext it }
        }

        val liveBody = fetchGet("https://api.frankfurter.dev/v2/currencies")
        if (!liveBody.isNullOrBlank()) {
            val parsed = parseFrankfurterCurrenciesJson(liveBody)
            if (parsed.isNotEmpty()) {
                prefs.edit()
                    .putString(KEY_FRANKFURTER_CURRENCIES, liveBody)
                    .putLong(KEY_FRANKFURTER_CURRENCIES_TS, nowMs())
                    .apply()
                return@withContext parsed
            }
        }

        if (!cachedJson.isNullOrBlank()) {
            val cachedSet = parseFrankfurterCurrenciesJson(cachedJson)
            if (cachedSet.isNotEmpty()) return@withContext cachedSet
        }
        emptySet()
    }

    fun parseFrankfurterCurrenciesJson(json: String): Set<String> {
        val trimmed = json.trim()
        if (trimmed.isEmpty()) return emptySet()
        val result = linkedSetOf<String>()
        return try {
            if (trimmed.startsWith("[")) {
                val arr = JSONArray(trimmed)
                for (i in 0 until arr.length()) {
                    val item = arr.opt(i)
                    when (item) {
                        is JSONObject -> {
                            val code = item.optString("iso_code")
                                .ifBlank { item.optString("code") }
                                .ifBlank { item.optString("currency") }
                                .trim()
                                .uppercase(Locale.US)
                            if (code.length == 3) result.add(code)
                        }
                        is String -> if (item.trim().length == 3) result.add(item.trim().uppercase(Locale.US))
                    }
                }
            } else if (trimmed.startsWith("{")) {
                val obj = JSONObject(trimmed)
                val keys = obj.keys()
                while (keys.hasNext()) {
                    val k = keys.next().trim().uppercase(Locale.US)
                    if (k.length == 3) result.add(k)
                }
            }
            result
        } catch (_: Exception) {
            emptySet()
        }
    }

    /**
     * Fetches exchange rate from `base` -> `target` using Frankfurter v2 API (`https://api.frankfurter.dev/v2`).
     * Never fabricates missing rates; explicitly reports unsupported pairs, stale caches, or network errors.
     */
    suspend fun fetchExchangeRate(
        context: Context,
        baseCurrency: String,
        targetCurrency: String,
        forceRefresh: Boolean = false
    ): ExchangeRateQuote = withContext(Dispatchers.IO) {
        val base = baseCurrency.trim().uppercase(Locale.US)
        val target = targetCurrency.trim().uppercase(Locale.US)
        if (base == target) {
            return@withContext ExchangeRateQuote(
                baseCurrency = base,
                targetCurrency = target,
                rate = BigDecimal.ONE,
                rateDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(nowMs())),
                fetchedAtEpochMs = nowMs(),
                sourceName = "Identity (1:1)",
                status = ExchangeRateStatus.SAME_CURRENCY,
                isSupportedByProvider = true,
                isStale = false
            )
        }

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val cacheKey = "${KEY_FX_PREFIX}${base}_$target"
        val cachedQuote = readCachedFxQuote(prefs.getString(cacheKey, null), base, target)

        // Return fresh cache when not forcing refresh
        if (!forceRefresh && cachedQuote != null && !cachedQuote.isStale) {
            return@withContext cachedQuote.copy(status = ExchangeRateStatus.CACHED_VALID)
        }

        // Check currency coverage before requesting pair rate
        val supported = fetchSupportedFrankfurterCurrencies(context, forceRefresh)
        if (supported.isNotEmpty() && (!supported.contains(base) || !supported.contains(target))) {
            return@withContext ExchangeRateQuote(
                baseCurrency = base,
                targetCurrency = target,
                rate = null,
                rateDate = "",
                fetchedAtEpochMs = 0L,
                sourceName = "Frankfurter API v2 (api.frankfurter.dev)",
                status = ExchangeRateStatus.UNSUPPORTED_PAIR,
                isSupportedByProvider = false,
                isStale = false,
                message = "Frankfurter / ECB feed does not cover $base/$target. Enter exchange rate manually."
            )
        }

        val pairUrl = "https://api.frankfurter.dev/v2/rate/${base.lowercase(Locale.US)}/${target.lowercase(Locale.US)}"
        val rawPair = fetchGet(pairUrl)
        if (!rawPair.isNullOrBlank()) {
            val parsed = parseFrankfurterSingleRateJson(rawPair)
            if (parsed != null && parsed.first > BigDecimal.ZERO) {
                val ts = nowMs()
                val quote = ExchangeRateQuote(
                    baseCurrency = base,
                    targetCurrency = target,
                    rate = parsed.first,
                    rateDate = parsed.second,
                    fetchedAtEpochMs = ts,
                    sourceName = "Frankfurter API v2 (ECB Reference Rates)",
                    status = ExchangeRateStatus.AUTOMATIC_LIVE,
                    isSupportedByProvider = true,
                    isStale = false
                )
                saveCachedFxQuote(prefs, cacheKey, quote)
                return@withContext quote
            }
        }

        // Fallback to multi-rate endpoint in case single-rate endpoint is throttled or cached differently
        val multiUrl = "https://api.frankfurter.dev/v2/rates?base=$base&quotes=$target"
        val rawMulti = fetchGet(multiUrl)
        if (!rawMulti.isNullOrBlank()) {
            val parsed = parseFrankfurterMultiRateJson(rawMulti, target)
            if (parsed != null && parsed.first > BigDecimal.ZERO) {
                val ts = nowMs()
                val quote = ExchangeRateQuote(
                    baseCurrency = base,
                    targetCurrency = target,
                    rate = parsed.first,
                    rateDate = parsed.second,
                    fetchedAtEpochMs = ts,
                    sourceName = "Frankfurter API v2 (ECB Reference Rates)",
                    status = ExchangeRateStatus.AUTOMATIC_LIVE,
                    isSupportedByProvider = true,
                    isStale = false
                )
                saveCachedFxQuote(prefs, cacheKey, quote)
                return@withContext quote
            }
        }

        // If live fetch failed, check if we have a cached quote and mark stale warning honestly
        if (cachedQuote != null && cachedQuote.rate != null) {
            return@withContext cachedQuote.copy(
                status = ExchangeRateStatus.CACHED_OUTDATED,
                isStale = true,
                message = "Live rate fetch unavailable; showing cached rate from ${cachedQuote.rateDate}."
            )
        }

        ExchangeRateQuote(
            baseCurrency = base,
            targetCurrency = target,
            rate = null,
            rateDate = "",
            fetchedAtEpochMs = 0L,
            sourceName = "Frankfurter API v2 (api.frankfurter.dev)",
            status = ExchangeRateStatus.UNAVAILABLE,
            isSupportedByProvider = supported.isEmpty() || (supported.contains(base) && supported.contains(target)),
            isStale = false,
            message = "Unable to retrieve $base/$target exchange rate. Please enter rate manually."
        )
    }

    fun parseFrankfurterSingleRateJson(json: String): Pair<BigDecimal, String>? {
        return try {
            val obj = JSONObject(json.trim())
            if (!obj.has("rate")) return null
            val rateStr = obj.optString("rate", "")
            val rate = rateStr.toBigDecimalOrNull() ?: return null
            val date = obj.optString("date", "").trim()
            if (rate > BigDecimal.ZERO) rate to date else null
        } catch (_: Exception) {
            null
        }
    }

    fun parseFrankfurterMultiRateJson(json: String, targetCode: String): Pair<BigDecimal, String>? {
        return try {
            val arr = JSONArray(json.trim())
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val q = obj.optString("quote", "").trim().uppercase(Locale.US)
                if (q == targetCode.uppercase(Locale.US) && obj.has("rate")) {
                    val rate = obj.optString("rate", "").toBigDecimalOrNull() ?: continue
                    val date = obj.optString("date", "").trim()
                    if (rate > BigDecimal.ZERO) return rate to date
                }
            }
            null
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Fetches Gold (24K fine) and Silver (pure 999) spot market prices from free, no-key endpoints
     * (`api.gold-api.com/price/XAU` & `XAG`, with `data-asg.goldprice.org/dbXRates/USD` fallback),
     * converts troy ounce to per-gram using BigDecimal, and converts USD/g to [targetCurrencyCode]
     * ONLY when a valid automatic or manual exchange rate exists.
     */
    suspend fun fetchGoldAndSilverPrices(
        context: Context,
        targetCurrencyCode: String,
        manualExchangeRateOverride: BigDecimal? = null,
        forceRefresh: Boolean = false
    ): MetalsSpotQuote = withContext(Dispatchers.IO) {
        val target = targetCurrencyCode.trim().uppercase(Locale.US)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Acquire USD per-gram spot price (cache or live)
        val cachedSpot = readCachedMetalsUsd(prefs.getString(KEY_METALS_USD, null))
        var goldUsdPerGram: BigDecimal? = null
        var silverUsdPerGram: BigDecimal? = null
        var sourceName = ""
        var updatedAt = ""
        var fetchedAt = 0L
        var isFromCache = false
        var isStale = false

        if (!forceRefresh && cachedSpot != null && (nowMs() - cachedSpot.fetchedAtMs) < STALE_THRESHOLD_MS) {
            goldUsdPerGram = cachedSpot.goldUsdPerGram
            silverUsdPerGram = cachedSpot.silverUsdPerGram
            sourceName = cachedSpot.sourceName
            updatedAt = cachedSpot.updatedAt
            fetchedAt = cachedSpot.fetchedAtMs
            isFromCache = true
        } else {
            val liveGoldApi = fetchFromGoldApi()
            val resolvedLive = liveGoldApi ?: fetchFromGoldPriceOrg()
            if (resolvedLive != null) {
                goldUsdPerGram = resolvedLive.goldUsdPerGram
                silverUsdPerGram = resolvedLive.silverUsdPerGram
                sourceName = resolvedLive.sourceName
                updatedAt = resolvedLive.updatedAt
                fetchedAt = nowMs()
                saveCachedMetalsUsd(prefs, resolvedLive.copy(fetchedAtMs = fetchedAt))
            } else if (cachedSpot != null) {
                goldUsdPerGram = cachedSpot.goldUsdPerGram
                silverUsdPerGram = cachedSpot.silverUsdPerGram
                sourceName = cachedSpot.sourceName
                updatedAt = cachedSpot.updatedAt
                fetchedAt = cachedSpot.fetchedAtMs
                isFromCache = true
                isStale = (nowMs() - cachedSpot.fetchedAtMs) >= STALE_THRESHOLD_MS
            }
        }

        if (goldUsdPerGram == null || silverUsdPerGram == null) {
            return@withContext MetalsSpotQuote(
                currencyCode = target,
                gold24kPerGramUsd = null,
                silverPurePerGramUsd = null,
                gold24kPerGramLocal = null,
                silverPurePerGramLocal = null,
                exchangeRateUsed = null,
                sourceName = "Market Spot Feed Unavailable",
                updatedAtText = "",
                fetchedAtEpochMs = 0L,
                status = MetalPriceStatus.UNAVAILABLE,
                isStale = false,
                errorMessage = "Could not fetch live gold/silver spot prices. Enter local retail prices per gram manually."
            )
        }

        // 2. Convert USD/g -> target currency if needed
        if (target == "USD") {
            val status = when {
                isStale -> MetalPriceStatus.CACHED_OUTDATED
                isFromCache -> MetalPriceStatus.CACHED_VALID
                else -> MetalPriceStatus.AUTOMATIC_LIVE_USD
            }
            return@withContext MetalsSpotQuote(
                currencyCode = "USD",
                gold24kPerGramUsd = goldUsdPerGram,
                silverPurePerGramUsd = silverUsdPerGram,
                gold24kPerGramLocal = goldUsdPerGram.setScale(2, RoundingMode.HALF_UP),
                silverPurePerGramLocal = silverUsdPerGram.setScale(2, RoundingMode.HALF_UP),
                exchangeRateUsed = ExchangeRateQuote(
                    baseCurrency = "USD",
                    targetCurrency = "USD",
                    rate = BigDecimal.ONE,
                    rateDate = updatedAt.take(10),
                    fetchedAtEpochMs = fetchedAt,
                    sourceName = "Identity (1:1)",
                    status = ExchangeRateStatus.SAME_CURRENCY,
                    isSupportedByProvider = true,
                    isStale = false
                ),
                sourceName = sourceName,
                updatedAtText = updatedAt,
                fetchedAtEpochMs = fetchedAt,
                status = status,
                isStale = isStale
            )
        }

        val fxQuote = if (manualExchangeRateOverride != null && manualExchangeRateOverride > BigDecimal.ZERO) {
            ExchangeRateQuote(
                baseCurrency = "USD",
                targetCurrency = target,
                rate = manualExchangeRateOverride,
                rateDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(nowMs())),
                fetchedAtEpochMs = nowMs(),
                sourceName = "User-Provided Manual Exchange Rate",
                status = ExchangeRateStatus.MANUAL_USER_PROVIDED,
                isSupportedByProvider = true,
                isStale = false
            )
        } else {
            fetchExchangeRate(context, "USD", target, forceRefresh)
        }

        val rate = fxQuote.rate
        val currencyDecimals = ZakatCurrencyCatalog.getCurrencyOrDefault(target).decimalDigits
        val convertedGold = rate?.let {
            goldUsdPerGram.multiply(it).setScale(currencyDecimals, RoundingMode.HALF_UP)
        }
        val convertedSilver = rate?.let {
            silverUsdPerGram.multiply(it).setScale(currencyDecimals, RoundingMode.HALF_UP)
        }

        val combinedStale = isStale || fxQuote.isStale
        val status = when {
            rate == null -> MetalPriceStatus.UNAVAILABLE
            combinedStale -> MetalPriceStatus.CACHED_OUTDATED
            isFromCache -> MetalPriceStatus.CACHED_VALID
            else -> MetalPriceStatus.AUTOMATIC_CONVERTED
        }

        MetalsSpotQuote(
            currencyCode = target,
            gold24kPerGramUsd = goldUsdPerGram,
            silverPurePerGramUsd = silverUsdPerGram,
            gold24kPerGramLocal = convertedGold,
            silverPurePerGramLocal = convertedSilver,
            exchangeRateUsed = fxQuote,
            sourceName = sourceName,
            updatedAtText = updatedAt,
            fetchedAtEpochMs = fetchedAt,
            status = status,
            isStale = combinedStale,
            errorMessage = if (rate == null) fxQuote.message else null
        )
    }

    private data class CachedMetalsUsd(
        val goldUsdPerGram: BigDecimal,
        val silverUsdPerGram: BigDecimal,
        val sourceName: String,
        val updatedAt: String,
        val fetchedAtMs: Long
    )

    private fun fetchFromGoldApi(): CachedMetalsUsd? {
        val xauBody = fetchGet("https://api.gold-api.com/price/XAU") ?: return null
        val xagBody = fetchGet("https://api.gold-api.com/price/XAG") ?: return null
        val xau = parseGoldApiSingleJson(xauBody) ?: return null
        val xag = parseGoldApiSingleJson(xagBody) ?: return null
        val goldPerG = xau.first.divide(TROY_OUNCE_IN_GRAMS, 6, RoundingMode.HALF_UP)
        val silverPerG = xag.first.divide(TROY_OUNCE_IN_GRAMS, 6, RoundingMode.HALF_UP)
        return CachedMetalsUsd(
            goldUsdPerGram = goldPerG,
            silverUsdPerGram = silverPerG,
            sourceName = "Gold-API Spot Feed (api.gold-api.com)",
            updatedAt = xau.second.ifBlank { xag.second },
            fetchedAtMs = nowMs()
        )
    }

    fun parseGoldApiSingleJson(json: String): Pair<BigDecimal, String>? {
        return try {
            val obj = JSONObject(json.trim())
            if (!obj.has("price")) return null
            val ozPrice = obj.optString("price", "").toBigDecimalOrNull() ?: return null
            val updatedAt = obj.optString("updatedAt", "").trim()
            if (ozPrice > BigDecimal.ZERO) ozPrice to updatedAt else null
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchFromGoldPriceOrg(): CachedMetalsUsd? {
        val body = fetchGet("https://data-asg.goldprice.org/dbXRates/USD") ?: return null
        return parseGoldPriceOrgJson(body)?.let {
            CachedMetalsUsd(
                goldUsdPerGram = it.first,
                silverUsdPerGram = it.second,
                sourceName = "GoldPrice Public Feed (goldprice.org)",
                updatedAt = it.third,
                fetchedAtMs = nowMs()
            )
        }
    }

    fun parseGoldPriceOrgJson(json: String): Triple<BigDecimal, BigDecimal, String>? {
        return try {
            val obj = JSONObject(json.trim())
            val dateText = obj.optString("date", "").trim()
            val items = obj.optJSONArray("items") ?: return null
            val first = items.optJSONObject(0) ?: return null
            val xauOz = first.optString("xauPrice", "").toBigDecimalOrNull() ?: return null
            val xagOz = first.optString("xagPrice", "").toBigDecimalOrNull() ?: return null
            if (xauOz <= BigDecimal.ZERO || xagOz <= BigDecimal.ZERO) return null
            val goldG = xauOz.divide(TROY_OUNCE_IN_GRAMS, 6, RoundingMode.HALF_UP)
            val silverG = xagOz.divide(TROY_OUNCE_IN_GRAMS, 6, RoundingMode.HALF_UP)
            Triple(goldG, silverG, dateText)
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchGet(url: String): String? {
        return try {
            val req = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("User-Agent", "Noor-e-Hidayat/1.0")
                .build()
            client().newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                resp.body?.string()
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun saveCachedFxQuote(
        prefs: android.content.SharedPreferences,
        key: String,
        quote: ExchangeRateQuote
    ) {
        val json = JSONObject().apply {
            put("base", quote.baseCurrency)
            put("target", quote.targetCurrency)
            put("rate", quote.rate?.toPlainString() ?: "")
            put("rateDate", quote.rateDate)
            put("fetchedAt", quote.fetchedAtEpochMs)
            put("sourceName", quote.sourceName)
        }.toString()
        prefs.edit().putString(key, json).apply()
    }

    private fun readCachedFxQuote(raw: String?, base: String, target: String): ExchangeRateQuote? {
        if (raw.isNullOrBlank()) return null
        return try {
            val obj = JSONObject(raw)
            val rate = obj.optString("rate", "").toBigDecimalOrNull() ?: return null
            val fetchedAt = obj.optLong("fetchedAt", 0L)
            val stale = (nowMs() - fetchedAt) >= STALE_THRESHOLD_MS
            ExchangeRateQuote(
                baseCurrency = base,
                targetCurrency = target,
                rate = rate,
                rateDate = obj.optString("rateDate", ""),
                fetchedAtEpochMs = fetchedAt,
                sourceName = obj.optString("sourceName", "Frankfurter API v2"),
                status = if (stale) ExchangeRateStatus.CACHED_OUTDATED else ExchangeRateStatus.CACHED_VALID,
                isSupportedByProvider = true,
                isStale = stale
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun saveCachedMetalsUsd(prefs: android.content.SharedPreferences, cached: CachedMetalsUsd) {
        val json = JSONObject().apply {
            put("goldUsdG", cached.goldUsdPerGram.toPlainString())
            put("silverUsdG", cached.silverUsdPerGram.toPlainString())
            put("sourceName", cached.sourceName)
            put("updatedAt", cached.updatedAt)
            put("fetchedAt", cached.fetchedAtMs)
        }.toString()
        prefs.edit().putString(KEY_METALS_USD, json).apply()
    }

    private fun readCachedMetalsUsd(raw: String?): CachedMetalsUsd? {
        if (raw.isNullOrBlank()) return null
        return try {
            val obj = JSONObject(raw)
            val gold = obj.optString("goldUsdG", "").toBigDecimalOrNull() ?: return null
            val silver = obj.optString("silverUsdG", "").toBigDecimalOrNull() ?: return null
            CachedMetalsUsd(
                goldUsdPerGram = gold,
                silverUsdPerGram = silver,
                sourceName = obj.optString("sourceName", "Spot Feed"),
                updatedAt = obj.optString("updatedAt", ""),
                fetchedAtMs = obj.optLong("fetchedAt", 0L)
            )
        } catch (_: Exception) {
            null
        }
    }
}
