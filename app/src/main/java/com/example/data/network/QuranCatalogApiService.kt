package com.example.data.network

import androidx.annotation.VisibleForTesting
import androidx.core.text.HtmlCompat
import com.example.data.db.TranslationCatalogEntity
import com.example.data.db.TranslationVerseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

data class QuranEncCatalogItem(
    val key: String,
    val title: String,
    val description: String,
    val languageIsoCode: String,
    val version: String,
    val lastUpdate: String,
    val direction: String
)

data class QuranFoundationTafsirResource(
    val id: Int,
    val name: String,
    val authorName: String,
    val slug: String,
    val languageName: String
)

data class QuranFoundationTafsirResult(
    val resourceId: Int,
    val resourceName: String,
    val languageName: String,
    val text: String,
    val startAyah: Int,
    val endAyah: Int,
    val versesMapText: String
)

data class QuranFoundationChapterInfo(
    val chapterId: Int,
    val languageName: String,
    val shortText: String,
    val source: String,
    val fullText: String
)

data class Spa5kEditionMeta(
    val slug: String,
    val name: String,
    val authorName: String,
    val languageName: String,
    val sourceUrl: String
)

data class Spa5kTafsirResult(
    val slug: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val text: String,
    val endpointUsed: String
)

object QuranCatalogApiService {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    /** In-memory only session cache for spa5k Surah fallback JSONs (prevents redundant Surah fetches during ayah navigation). */
    private val spa5kSurahSessionCache = ConcurrentHashMap<String, Map<Int, String>>()

    @VisibleForTesting
    @Volatile
    internal var httpGetOverride: ((String) -> String?)? = null

    fun clearSessionCaches() {
        spa5kSurahSessionCache.clear()
    }

    /**
     * Retrieves official available translations list from QuranEnc API v1
     * for language code ("ur", "en", "hi", "ar").
     */
    suspend fun fetchQuranEncTranslationsList(languageCode: String): Result<List<TranslationCatalogEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val url = "https://quranenc.com/api/v1/translations/list/$languageCode?localization=en"
                val jsonText = executeGet(url) ?: return@withContext Result.failure(Exception("Unable to reach QuranEnc catalog"))
                val root = JSONObject(jsonText)
                val arr = root.optJSONArray("translations") ?: return@withContext Result.success(emptyList())
                val list = mutableListOf<TranslationCatalogEntity>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val key = obj.optString("key").trim()
                    if (key.isEmpty()) continue
                    val title = obj.optString("title").ifBlank { key }
                    val desc = obj.optString("description")
                    val lang = obj.optString("language_iso_code", languageCode).lowercase()
                    val version = obj.optString("version", "1.0")
                    val lastUpdate = obj.optString("last_update", "")

                    list.add(
                        TranslationCatalogEntity(
                            translationId = "quranenc_$key",
                            languageCode = lang,
                            translator = title,
                            publisher = if (desc.isNotBlank()) desc.take(120) else "QuranEnc / Rowwad",
                            editionVersion = "$key (v$version)",
                            sourceUrl = "https://quranenc.com/en/browse/$key",
                            licenseName = "QuranEnc Open Download & Attribution Terms",
                            licenseUrl = "https://quranenc.com/en/home/about/terms",
                            lastUpdatedDate = lastUpdate.ifBlank { "Official Catalog" },
                            sourceProvider = "QURANENC",
                            remoteKey = key,
                            isBundledOffline = false,
                            offlineAllowed = true,
                            onlineAllowed = true,
                            isDownloadedOffline = false,
                            downloadedAyahCount = 0,
                            sizeBytes = 0L,
                            isMachineTranslated = false
                        )
                    )
                }
                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Fetches a full Surah translation from QuranEnc API v1.
     */
    suspend fun fetchQuranEncSurah(
        translationId: String,
        remoteKey: String,
        surahNumber: Int
    ): Result<Pair<List<TranslationVerseEntity>, Long>> = withContext(Dispatchers.IO) {
        try {
            val url = "https://quranenc.com/api/v1/translation/sura/$remoteKey/$surahNumber"
            val jsonText = executeGet(url) ?: return@withContext Result.failure(Exception("Network error downloading Surah $surahNumber"))
            val root = JSONObject(jsonText)
            val resultArr = root.optJSONArray("result")
                ?: return@withContext Result.failure(Exception("Invalid QuranEnc surah payload"))

            val verses = mutableListOf<TranslationVerseEntity>()
            for (i in 0 until resultArr.length()) {
                val item = resultArr.getJSONObject(i)
                val sura = item.optString("sura").toIntOrNull() ?: surahNumber
                val aya = item.optString("aya").toIntOrNull() ?: (i + 1)
                val trans = cleanHtmlPreservingParagraphs(item.optString("translation"))
                val footnotes = cleanHtmlPreservingParagraphs(item.optString("footnotes"))
                if (trans.isNotBlank()) {
                    verses.add(
                        TranslationVerseEntity(
                            id = "${translationId}_${sura}_$aya",
                            translationId = translationId,
                            surahNumber = sura,
                            ayahNumber = aya,
                            translatedText = trans,
                            footnotes = footnotes
                        )
                    )
                }
            }
            Result.success(verses to jsonText.toByteArray(Charsets.UTF_8).size.toLong())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches official translations catalog from Quran Foundation / Quran.com API v4.
     * Note: Per Quran Foundation Terms, QF translations are marked online-only (offlineAllowed = false).
     */
    suspend fun fetchQuranFoundationTranslationsList(): Result<List<TranslationCatalogEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val url = "https://api.quran.com/api/v4/resources/translations"
                val jsonText = executeGet(url) ?: return@withContext Result.failure(Exception("Unable to reach Quran Foundation API"))
                val root = JSONObject(jsonText)
                val arr = root.optJSONArray("translations") ?: return@withContext Result.success(emptyList())
                val list = mutableListOf<TranslationCatalogEntity>()
                val targetLanguages = mapOf(
                    "urdu" to "ur",
                    "english" to "en",
                    "hindi" to "hi",
                    "arabic" to "ar"
                )
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val langName = obj.optString("language_name").lowercase()
                    val langCode = targetLanguages[langName] ?: continue
                    val id = obj.optInt("id", -1)
                    if (id <= 0) continue
                    val name = obj.optString("name")
                    val author = obj.optString("author_name").ifBlank { name }
                    val slug = obj.optString("slug").ifBlank { "qf_$id" }

                    list.add(
                        TranslationCatalogEntity(
                            translationId = "qf_${langCode}_$id",
                            languageCode = langCode,
                            translator = author,
                            publisher = "Quran Foundation API v4 (Online-Only)",
                            editionVersion = "$name ($slug)",
                            sourceUrl = "https://api-docs.quran.com/docs/content_apis_versioned/4.0.0/translations/",
                            licenseName = "Quran Foundation Online Display Only (No Offline Redistribution)",
                            licenseUrl = "https://api-docs.quran.com/docs/content_apis_versioned/4.0.0/translations/",
                            lastUpdatedDate = "Live API",
                            sourceProvider = "QURAN_FOUNDATION",
                            remoteKey = id.toString(),
                            isBundledOffline = false,
                            offlineAllowed = false,
                            onlineAllowed = true,
                            isDownloadedOffline = false,
                            downloadedAyahCount = 0,
                            sizeBytes = 0L,
                            isMachineTranslated = false
                        )
                    )
                }
                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Fetches single ayah translation online-only from Quran Foundation API v4 (not cached to disk).
     */
    suspend fun fetchQuranFoundationVerseTranslation(
        translationNumericId: String,
        surahNumber: Int,
        ayahNumber: Int
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.quran.com/api/v4/verses/by_key/$surahNumber:$ayahNumber?translations=$translationNumericId"
            val jsonText = executeGet(url) ?: return@withContext Result.failure(Exception("Unable to fetch live translation"))
            val root = JSONObject(jsonText)
            val verseObj = root.optJSONObject("verse")
            val transArr = verseObj?.optJSONArray("translations")
            if (transArr != null && transArr.length() > 0) {
                val raw = transArr.getJSONObject(0).optString("text")
                Result.success(cleanHtmlPreservingParagraphs(raw))
            } else {
                Result.failure(Exception("Translation passage not returned by API"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Dynamically retrieves verified Tafsir resources catalog from Quran Foundation API v4.
     */
    suspend fun fetchQuranFoundationTafsirResources(): Result<List<QuranFoundationTafsirResource>> =
        withContext(Dispatchers.IO) {
            try {
                val url = "https://api.quran.com/api/v4/resources/tafsirs"
                val jsonText = executeGet(url) ?: return@withContext Result.failure(Exception("Unable to reach Quran Foundation tafsirs API"))
                val root = JSONObject(jsonText)
                val arr = root.optJSONArray("tafsirs") ?: return@withContext Result.success(emptyList())
                val list = mutableListOf<QuranFoundationTafsirResource>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val id = obj.optInt("id", -1)
                    if (id <= 0) continue
                    list.add(
                        QuranFoundationTafsirResource(
                            id = id,
                            name = obj.optString("name"),
                            authorName = obj.optString("author_name"),
                            slug = obj.optString("slug"),
                            languageName = obj.optString("language_name").lowercase()
                        )
                    )
                }
                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Fetches live online-only Tafsir for a specific ayah from Quran Foundation API v4.
     * Supports multi-ayah verse ranges returned in the `verses` object (never misattributing multi-ayah passages).
     * Never writes QF content to offline database storage.
     */
    suspend fun fetchQuranFoundationTafsirByAyah(
        slugOrId: String,
        surahNumber: Int,
        ayahNumber: Int
    ): Result<QuranFoundationTafsirResult> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.quran.com/api/v4/tafsirs/$slugOrId/by_ayah/$surahNumber:$ayahNumber"
            val jsonText = executeGet(url) ?: return@withContext Result.failure(Exception("Network unavailable for live Tafseer"))
            val root = JSONObject(jsonText)
            val tafsirObj = root.optJSONObject("tafsir")
                ?: return@withContext Result.failure(Exception("No Tafseer entry found for $surahNumber:$ayahNumber"))

            val rawHtml = tafsirObj.optString("text")
            val cleanedText = cleanHtmlPreservingParagraphs(rawHtml)
            if (cleanedText.isBlank()) {
                return@withContext Result.failure(Exception("Tafseer passage is empty for $surahNumber:$ayahNumber"))
            }

            val resourceId = tafsirObj.optInt("resource_id", 0)
            val resourceName = tafsirObj.optString("resource_name")
            val langName = tafsirObj.optString("language_name").ifBlank {
                tafsirObj.optJSONObject("translated_name")?.optString("language_name").orEmpty()
            }

            // Parse actual ayah span covered by this commentary passage
            var startAyah = ayahNumber
            var endAyah = ayahNumber
            val versesObj = tafsirObj.optJSONObject("verses")
            if (versesObj != null && versesObj.length() > 0) {
                val ayahNumbers = mutableListOf<Int>()
                val keys = versesObj.keys()
                while (keys.hasNext()) {
                    val key = keys.next() // format "surah:ayah"
                    val parts = key.split(":")
                    if (parts.size == 2 && parts[0].toIntOrNull() == surahNumber) {
                        parts[1].toIntOrNull()?.let { ayahNumbers.add(it) }
                    }
                }
                if (ayahNumbers.isNotEmpty()) {
                    startAyah = ayahNumbers.minOrNull() ?: ayahNumber
                    endAyah = ayahNumbers.maxOrNull() ?: ayahNumber
                }
            }

            Result.success(
                QuranFoundationTafsirResult(
                    resourceId = resourceId,
                    resourceName = resourceName,
                    languageName = langName,
                    text = cleanedText,
                    startAyah = startAyah,
                    endAyah = endAyah,
                    versesMapText = if (startAyah == endAyah) "$surahNumber:$startAyah" else "$surahNumber:$startAyah-$endAyah"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches verified Surah historical background & theme online-only from Quran Foundation API v4.
     * Cached only in session memory; never written to offline disk.
     */
    suspend fun fetchQuranFoundationChapterInfo(
        surahNumber: Int,
        languageCode: String = "en"
    ): Result<QuranFoundationChapterInfo> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.quran.com/api/v4/chapters/$surahNumber/info?language=$languageCode"
            val jsonText = executeGet(url) ?: return@withContext Result.failure(Exception("Chapter info unavailable"))
            val root = JSONObject(jsonText)
            val info = root.optJSONObject("chapter_info")
                ?: return@withContext Result.failure(Exception("No chapter_info for Surah $surahNumber ($languageCode)"))
            val shortText = cleanHtmlPreservingParagraphs(info.optString("short_text"))
            val fullText = cleanHtmlPreservingParagraphs(info.optString("text"))
            if (shortText.isBlank() && fullText.isBlank()) {
                return@withContext Result.failure(Exception("Empty chapter info"))
            }
            Result.success(
                QuranFoundationChapterInfo(
                    chapterId = surahNumber,
                    languageName = info.optString("language_name", languageCode),
                    shortText = shortText,
                    source = info.optString("source", "Quran Foundation API v4"),
                    fullText = fullText
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches available editions catalog from spa5k/tafsir_api (`editions.json`).
     */
    suspend fun fetchSpa5kEditionsMetadata(): Result<List<Spa5kEditionMeta>> = withContext(Dispatchers.IO) {
        try {
            val url = "https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@v1.2.3/tafsir/editions.json"
            val jsonText = executeGet(url) ?: return@withContext Result.failure(Exception("Unable to reach spa5k tafsir_api editions"))
            val arr = JSONArray(jsonText)
            val list = mutableListOf<Spa5kEditionMeta>()
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val slug = obj.optString("slug").trim()
                if (slug.isEmpty()) continue
                list.add(
                    Spa5kEditionMeta(
                        slug = slug,
                        name = obj.optString("name", slug),
                        authorName = obj.optString("author_name", ""),
                        languageName = obj.optString("language_name").ifBlank { obj.optString("language") }.lowercase(),
                        sourceUrl = obj.optString("source", "")
                    )
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches live online Tafseer for a single Ayah from spa5k/tafsir_api CDN:
     * 1) Attempts `/tafsir/{slug}/{surah}/{ayah}.json` on `@main`
     * 2) Falls back to `/tafsir/{slug}/{surah}.json` (handles ayahs missing standalone leaf JSONs such as `hindi-mokhtasar/36/83`)
     * 3) Optionally tries [fallbackSlug] on `@v1.2.3` if different.
     */
    suspend fun fetchSpa5kTafsirByAyah(
        slug: String,
        surahNumber: Int,
        ayahNumber: Int,
        fallbackSlug: String? = null
    ): Result<Spa5kTafsirResult> = withContext(Dispatchers.IO) {
        try {
            val trimmedSlug = slug.trim()
            if (trimmedSlug.isEmpty()) {
                return@withContext Result.failure(Exception("Missing spa5k edition slug"))
            }

            // Fast in-memory Surah cache hit if Surah JSON was already fetched
            spa5kSurahSessionCache["$trimmedSlug:$surahNumber"]?.get(ayahNumber)?.let { cached ->
                return@withContext Result.success(
                    Spa5kTafsirResult(
                        slug = trimmedSlug,
                        surahNumber = surahNumber,
                        ayahNumber = ayahNumber,
                        text = cached,
                        endpointUsed = "https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@main/tafsir/$trimmedSlug/$surahNumber.json"
                    )
                )
            }

            // 1. Primary leaf Ayah endpoint (@main)
            val primaryUrl = "https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@main/tafsir/$trimmedSlug/$surahNumber/$ayahNumber.json"
            val leafText = executeGet(primaryUrl)?.let { parseSpa5kSingleAyahJson(it) }
            if (!leafText.isNullOrBlank()) {
                return@withContext Result.success(
                    Spa5kTafsirResult(
                        slug = trimmedSlug,
                        surahNumber = surahNumber,
                        ayahNumber = ayahNumber,
                        text = leafText,
                        endpointUsed = primaryUrl
                    )
                )
            }

            // 2. Surah JSON fallback (@main) — covers CDN 403/404 leaf omissions cleanly in memory
            val surahUrl = "https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@main/tafsir/$trimmedSlug/$surahNumber.json"
            val surahRaw = executeGet(surahUrl)
            if (!surahRaw.isNullOrBlank()) {
                val surahMap = parseSpa5kSurahJson(surahRaw)
                if (surahMap.isNotEmpty()) {
                    spa5kSurahSessionCache["$trimmedSlug:$surahNumber"] = surahMap
                    surahMap[ayahNumber]?.takeIf { it.isNotBlank() }?.let { matched ->
                        return@withContext Result.success(
                            Spa5kTafsirResult(
                                slug = trimmedSlug,
                                surahNumber = surahNumber,
                                ayahNumber = ayahNumber,
                                text = matched,
                                endpointUsed = surahUrl
                            )
                        )
                    }
                }
            }

            // 3. Tagged v1.2.3 CDN fallback (covers paths where @main returns jsDelivr 403, e.g. hindi-mokhtasar/112/1.json)
            val altSlug = fallbackSlug?.trim()?.takeIf { it.isNotEmpty() } ?: trimmedSlug
            val v123LeafUrl = "https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@v1.2.3/tafsir/$altSlug/$surahNumber/$ayahNumber.json"
            val v123LeafText = executeGet(v123LeafUrl)?.let { parseSpa5kSingleAyahJson(it) }
            if (!v123LeafText.isNullOrBlank()) {
                return@withContext Result.success(
                    Spa5kTafsirResult(
                        slug = altSlug,
                        surahNumber = surahNumber,
                        ayahNumber = ayahNumber,
                        text = v123LeafText,
                        endpointUsed = v123LeafUrl
                    )
                )
            }

            // 4. Direct GitHub raw fallback (immune to jsDelivr edge 403s)
            val rawLeafUrl = "https://raw.githubusercontent.com/spa5k/tafsir_api/main/tafsir/$trimmedSlug/$surahNumber/$ayahNumber.json"
            val rawLeafText = executeGet(rawLeafUrl)?.let { parseSpa5kSingleAyahJson(it) }
            if (!rawLeafText.isNullOrBlank()) {
                return@withContext Result.success(
                    Spa5kTafsirResult(
                        slug = trimmedSlug,
                        surahNumber = surahNumber,
                        ayahNumber = ayahNumber,
                        text = rawLeafText,
                        endpointUsed = rawLeafUrl
                    )
                )
            }

            val rawSurahUrl = "https://raw.githubusercontent.com/spa5k/tafsir_api/main/tafsir/$trimmedSlug/$surahNumber.json"
            val rawSurahBody = executeGet(rawSurahUrl)
            if (!rawSurahBody.isNullOrBlank()) {
                val rawSurahMap = parseSpa5kSurahJson(rawSurahBody)
                if (rawSurahMap.isNotEmpty()) {
                    spa5kSurahSessionCache["$trimmedSlug:$surahNumber"] = rawSurahMap
                    rawSurahMap[ayahNumber]?.takeIf { it.isNotBlank() }?.let { matched ->
                        return@withContext Result.success(
                            Spa5kTafsirResult(
                                slug = trimmedSlug,
                                surahNumber = surahNumber,
                                ayahNumber = ayahNumber,
                                text = matched,
                                endpointUsed = rawSurahUrl
                            )
                        )
                    }
                }
            }

            Result.failure(Exception("No online Tafseer entry found at spa5k for $trimmedSlug ($surahNumber:$ayahNumber)"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseSpa5kSingleAyahJson(rawJson: String): String? {
        return try {
            val obj = JSONObject(rawJson)
            cleanHtmlPreservingParagraphs(obj.optString("text")).takeIf { it.isNotBlank() }
        } catch (_: Throwable) {
            null
        }
    }

    private fun parseSpa5kSurahJson(rawJson: String): Map<Int, String> {
        val result = HashMap<Int, String>()
        val trimmed = rawJson.trim()
        try {
            val arr: JSONArray? = when {
                trimmed.startsWith("[") -> JSONArray(trimmed)
                trimmed.startsWith("{") -> JSONObject(trimmed).optJSONArray("ayahs")
                else -> null
            }
            if (arr != null) {
                for (i in 0 until arr.length()) {
                    val item = arr.optJSONObject(i) ?: continue
                    val ayaNum = item.optInt("ayah", 0)
                    val txt = cleanHtmlPreservingParagraphs(item.optString("text"))
                    if (ayaNum > 0 && txt.isNotBlank()) {
                        result[ayaNum] = txt
                    }
                }
            }
        } catch (_: Throwable) {
            // ignore parse error
        }
        return result
    }

    private fun executeGet(url: String): String? {
        httpGetOverride?.let { override ->
            return override(url)
        }
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Noor-e-Hidayat/4.0 (Android; AuthenticQuranClient)")
            .header("Accept", "application/json")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            return response.body?.string()
        }
    }

    fun cleanHtmlPreservingParagraphs(raw: String): String {
        if (raw.isBlank()) return ""
        val withParagraphMarkers = raw
            .replace(Regex("(?i)<br\\s*/?>"), "\n")
            .replace(Regex("(?i)</p>"), "\n\n")
            .replace(Regex("(?i)<h[1-6][^>]*>"), "\n\n")
            .replace(Regex("(?i)</h[1-6]>"), "\n\n")
            .replace(Regex("(?i)<li[^>]*>"), "\n• ")
            .replace(Regex("(?i)</li>"), "")
            .replace(Regex("(?i)</div>"), "\n")
            .replace(Regex("(?i)<sup[^>]*>.*?</sup>"), "")
        val decoded = try {
            HtmlCompat.fromHtml(withParagraphMarkers, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } catch (_: Throwable) {
            withParagraphMarkers
                .replace(Regex("<[^>]+>"), "")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&nbsp;", " ")
        }
        return decoded
            .lines()
            .map { it.trimEnd() }
            .joinToString("\n")
            .replace(Regex("\n{3,}"), "\n\n")
            .trim()
    }
}
