package com.example

import android.app.Application
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.model.NisabType
import com.example.data.zakat.DebtDeductionMethodology
import com.example.data.zakat.ExchangeRateStatus
import com.example.data.zakat.GlobalZakatInput
import com.example.data.zakat.GoldPurity
import com.example.data.zakat.HawlStatus
import com.example.data.zakat.InvestmentMethodology
import com.example.data.zakat.JewelleryMethodology
import com.example.data.zakat.MetalPriceStatus
import com.example.data.zakat.ReceivableMethodology
import com.example.data.zakat.SilverPurity
import com.example.data.zakat.ZakatCalculationEngine
import com.example.data.zakat.ZakatCurrencyCatalog
import com.example.data.zakat.ZakatCurrencyFormatter
import com.example.data.zakat.ZakatEligibilityReason
import com.example.data.zakat.ZakatMarketDataRepository
import com.example.ui.screens.ZakatScreen
import com.example.ui.theme.NooreHidayatTheme
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GlobalZakatCalculatorTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences("noor_global_zakat_prefs_v1", Context.MODE_PRIVATE)
            .edit().clear().commit()
        appContext.getSharedPreferences("noor_zakat_market_cache_v1", Context.MODE_PRIVATE)
            .edit().clear().commit()
        ZakatMarketDataRepository.httpClientOverrideForTest = null
        ZakatMarketDataRepository.clockMillisOverrideForTest = null
    }

    @After
    fun tearDown() {
        ZakatMarketDataRepository.httpClientOverrideForTest = null
        ZakatMarketDataRepository.clockMillisOverrideForTest = null
    }

    @Test
    fun currencyCatalog_coversGlobalRegionsAndExcludesObsoleteAndSpecialCodes() {
        val codes = ZakatCurrencyCatalog.ACTIVE_CURRENCIES.map { it.code }.toSet()
        listOf(
            "PKR", "SAR", "AED", "USD", "GBP", "EUR", "JPY", "KWD", "QAR", "BHD",
            "OMR", "JOD", "EGP", "INR", "BDT", "MYR", "IDR", "TRY", "CAD", "AUD",
            "NGN", "KES", "ZAR", "BRL", "CHF", "SEK", "PLN", "NZD", "FJD", "ZWG"
        ).forEach { expected ->
            assertTrue("Expected active currency $expected in catalog", codes.contains(expected))
        }

        // Verify obsolete / supranational / metal codes are absent
        listOf("XAU", "XAG", "XDR", "XTS", "XXX", "DEM", "FRF", "ITL", "HRK", "VEF", "ZWD", "TRL").forEach { excluded ->
            assertFalse("Excluded non-national/obsolete code $excluded must not appear", codes.contains(excluded))
        }

        // Verify independent Country & Currency persistence
        ZakatCurrencyCatalog.saveSelectedCountry(appContext, "PK")
        ZakatCurrencyCatalog.saveSelectedCurrency(appContext, "USD")
        val (country, currency) = ZakatCurrencyCatalog.loadPersistedSelection(appContext)
        assertEquals("PK", country.isoCode)
        assertEquals("USD", currency.code)
    }

    @Test
    fun currencyFormatter_respectsIsoMinorUnitsAndUnicodeNumerals() {
        assertEquals(0, ZakatCurrencyCatalog.getCurrencyOrDefault("JPY").decimalDigits)
        assertEquals(2, ZakatCurrencyCatalog.getCurrencyOrDefault("PKR").decimalDigits)
        assertEquals(3, ZakatCurrencyCatalog.getCurrencyOrDefault("KWD").decimalDigits)

        val kwdFormatted = ZakatCurrencyFormatter.formatAmount(BigDecimal("125.456"), "KWD", Locale.US)
        assertTrue(kwdFormatted.contains("125.456"))
        val jpyFormatted = ZakatCurrencyFormatter.formatAmount(BigDecimal("10000"), "JPY", Locale.US)
        assertFalse(jpyFormatted.contains(".00"))

        assertEquals(BigDecimal("12500"), ZakatCurrencyFormatter.parseDecimalOrNull("١٢٥٠٠"))
        assertEquals(BigDecimal("25000.50"), ZakatCurrencyFormatter.parseDecimalOrNull("۲۵۰۰۰.۵۰"))
    }

    @Test
    fun marketDataRepository_parsesFrankfurterV2AndMetalsSpotWithStaleAndUnsupportedHandling() = runTest {
        var nowMs = 1_750_000_000_000L
        var failNetwork = false
        ZakatMarketDataRepository.clockMillisOverrideForTest = { nowMs }

        ZakatMarketDataRepository.httpClientOverrideForTest = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                if (failNetwork) {
                    return@Interceptor Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(503)
                        .message("Service Unavailable")
                        .body("".toResponseBody("application/json".toMediaType()))
                        .build()
                }
                val url = chain.request().url.toString()
                val json = when {
                    url.contains("api.frankfurter.dev/v2/currencies") ->
                        """[{"iso_code":"USD","name":"US Dollar"},{"iso_code":"EUR","name":"Euro"},{"iso_code":"GBP","name":"British Pound"}]"""
                    url.contains("api.frankfurter.dev/v2/rate/usd/eur") ->
                        """{"date":"2026-06-15","base":"USD","quote":"EUR","rate":0.9200}"""
                    url.contains("api.gold-api.com/price/XAU") ->
                        """{"currency":"USD","price":3110.34768,"symbol":"XAU","updatedAt":"2026-06-15T10:00:00Z"}"""
                    url.contains("api.gold-api.com/price/XAG") ->
                        """{"currency":"USD","price":31.1034768,"symbol":"XAG","updatedAt":"2026-06-15T10:00:00Z"}"""
                    else -> "{}"
                }
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body(json.toResponseBody("application/json".toMediaType()))
                    .build()
            })
            .build()

        // 1 troy oz XAU = $3110.34768 -> exactly $100.000000 / gram USD -> €92.00 / gram EUR
        val eurSpot = ZakatMarketDataRepository.fetchGoldAndSilverPrices(appContext, "EUR", forceRefresh = true)
        assertEquals(MetalPriceStatus.AUTOMATIC_CONVERTED, eurSpot.status)
        assertNotNull(eurSpot.gold24kPerGramUsd)
        assertEquals(0, eurSpot.gold24kPerGramUsd!!.compareTo(BigDecimal("100")))
        assertEquals(0, eurSpot.silverPurePerGramUsd!!.compareTo(BigDecimal("1")))
        assertEquals(BigDecimal("92.00"), eurSpot.gold24kPerGramLocal)
        assertEquals(BigDecimal("0.92"), eurSpot.silverPurePerGramLocal)
        assertFalse(eurSpot.isStale)

        // Unlisted currency (e.g. PKR) -> UNSUPPORTED_PAIR, never fabricated
        val unsupported = ZakatMarketDataRepository.fetchExchangeRate(appContext, "USD", "PKR", forceRefresh = false)
        assertEquals(ExchangeRateStatus.UNSUPPORTED_PAIR, unsupported.status)
        assertFalse(unsupported.isSupportedByProvider)
        assertNull(unsupported.rate)

        // Advance time 25 hours and fail network -> CACHED_OUTDATED + isStale = true
        nowMs += 25L * 60L * 60L * 1000L
        failNetwork = true
        val staleSpot = ZakatMarketDataRepository.fetchGoldAndSilverPrices(appContext, "EUR", forceRefresh = true)
        assertEquals(MetalPriceStatus.CACHED_OUTDATED, staleSpot.status)
        assertTrue(staleSpot.isStale)
    }

    @Test
    fun calculationEngine_enforcesHawlPuritiesJewelleryReceivablesDebtsAndBigDecimal() {
        // 1. Missing Hawl must NEVER say Zakat Due even when above Nisab
        val missingHawl = GlobalZakatInput(
            currencyCode = "USD",
            cashAtHome = BigDecimal("10000.00"),
            gold24kPricePerGram = BigDecimal("100.00"),
            silverPurePricePerGram = BigDecimal("1.00"),
            nisabType = NisabType.SILVER,
            hawlConfirmedByUser = null
        )
        val resUnverified = ZakatCalculationEngine.calculate(missingHawl)
        assertFalse(resUnverified.isEligible)
        assertEquals(ZakatEligibilityReason.HAWL_UNVERIFIED, resUnverified.eligibilityReason)
        assertEquals(BigDecimal("0.00"), resUnverified.zakatPayable)

        // 2. Incomplete Hawl (date 100 days ago)
        val incomplete = missingHawl.copy(
            hawlConfirmedByUser = false,
            hawlStartGregorianIso = "2026-01-01"
        )
        val resIncomplete = ZakatCalculationEngine.calculate(incomplete)
        assertFalse(resIncomplete.isEligible)
        assertEquals(ZakatEligibilityReason.HAWL_NOT_COMPLETE, resIncomplete.eligibilityReason)
        assertEquals(HawlStatus.NOT_YET_COMPLETE, resIncomplete.hawlEvaluation.status)
        assertTrue(resIncomplete.hawlEvaluation.anniversaryGregorianIso.isNotBlank())

        // 3. 22K Gold + Sterling Silver + Personal Jewellery Exempt (Majority) vs Included (Hanafi)
        val jewelleryInput = GlobalZakatInput(
            currencyCode = "USD",
            cashAtHome = BigDecimal("5000.00"),
            goldGrams = BigDecimal("100.0"),
            goldPurity = GoldPurity.K22, // 0.9167 -> 91.6700g pure -> $9167.00 at $100/g
            gold24kPricePerGram = BigDecimal("100.00"),
            silverPurePricePerGram = BigDecimal("1.00"),
            goldJewelleryIsPersonalWearOnly = true,
            jewelleryMethodology = JewelleryMethodology.ZAKATABLE_ALL_JEWELLERY,
            silverGrams = BigDecimal("200.0"),
            silverPurity = SilverPurity.STERLING_925, // 185.0000g pure -> $185.00
            nisabType = NisabType.SILVER,
            hawlConfirmedByUser = true
        )
        val hanafiRes = ZakatCalculationEngine.calculate(jewelleryInput)
        assertTrue(hanafiRes.isEligible)
        assertEquals(BigDecimal("9167.00"), hanafiRes.goldValuation)
        assertEquals(BigDecimal("185.00"), hanafiRes.silverValuation)
        assertEquals(BigDecimal("14352.00"), hanafiRes.netZakatableWealth)
        assertEquals(BigDecimal("358.80"), hanafiRes.zakatPayable)

        val majorityRes = ZakatCalculationEngine.calculate(
            jewelleryInput.copy(jewelleryMethodology = JewelleryMethodology.EXEMPT_PERSONAL_USE)
        )
        assertEquals(BigDecimal("0.00"), majorityRes.goldValuation)
        assertEquals(BigDecimal("5185.00"), majorityRes.netZakatableWealth)

        // 4. Receivables & Investment portion & Debts exceeding wealth floor
        val complexInput = GlobalZakatInput(
            currencyCode = "PKR",
            sharesMarketValue = BigDecimal("100000"),
            investmentMethodology = InvestmentMethodology.USER_ZAKATABLE_PORTION,
            userSpecifiedInvestmentZakatablePercent = BigDecimal("35"), // 35000
            strongReceivables = BigDecimal("20000"),
            doubtfulReceivables = BigDecimal("15000"),
            unrecoverableReceivables = BigDecimal("50000"),
            receivableMethodology = ReceivableMethodology.STRONG_ONLY_EXCLUDE_DOUBTFUL,
            overdueDebts = BigDecimal("80000"),
            debtMethodology = DebtDeductionMethodology.CURRENT_DUE_ONLY,
            silverPurePricePerGram = BigDecimal("300"),
            nisabType = NisabType.SILVER,
            hawlConfirmedByUser = true
        )
        val complexRes = ZakatCalculationEngine.calculate(complexInput)
        assertEquals(BigDecimal("35000.00"), complexRes.qualifyingInvestmentsTotal)
        assertEquals(BigDecimal("20000.00"), complexRes.eligibleReceivablesTotal)
        assertEquals(BigDecimal("65000.00"), complexRes.excludedReceivablesTotal)
        assertEquals(BigDecimal("0.00"), complexRes.netZakatableWealth)
        assertEquals(ZakatEligibilityReason.ZERO_OR_NEGATIVE_WEALTH, complexRes.eligibilityReason)
        assertFalse(complexRes.isEligible)
    }

    @Test
    fun zakatScreen_calculatesEndToEndWithManualPriceAndConfirmedHawl() {
        ZakatMarketDataRepository.httpClientOverrideForTest = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(503)
                    .message("Offline")
                    .body("".toResponseBody("application/json".toMediaType()))
                    .build()
            })
            .build()

        ZakatCurrencyCatalog.saveSelectedCountry(appContext, "US")
        ZakatCurrencyCatalog.saveSelectedCurrency(appContext, "USD")

        composeRule.setContent {
            NooreHidayatTheme {
                ZakatScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // Enable manual metal prices & enter prices
        composeRule.onNodeWithTag("manual_metal_prices_switch").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("manual_gold_price_input").performScrollTo().performTextInput("100")
        composeRule.onNodeWithTag("manual_silver_price_input").performScrollTo().performTextInput("1")

        // Confirm Hawl = Yes
        composeRule.onNodeWithTag("hawl_yes_chip").performScrollTo().performClick()
        composeRule.waitForIdle()

        // Enter Cash = 10000 USD
        composeRule.onNodeWithTag("cash_input").performScrollTo().performTextInput("10000")
        composeRule.waitForIdle()

        // Calculate
        composeRule.onNodeWithTag("calculate_zakat_button").performScrollTo().performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("zakat_result_section").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("zakat_payable_amount_text").performScrollTo().assertIsDisplayed()
    }
}
