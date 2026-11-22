package com.example.data.podcast

import android.content.Context
import android.util.Log
import com.example.data.lectures.YouTubeRssParser
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

data class PodcastCatalogChannelEntry(
    val channelId: String,
    val handle: String,
    val nameNative: String,
    val nameEn: String,
    val language: PodcastLanguage,
    val priority: Int,
    val sourceKind: PodcastSourceKind,
    val scholarId: String,
    val scholarName: String,
    val requiresScholarAttribution: Boolean = false
) {
    fun toChannelSource(): PodcastChannelSource = PodcastChannelSource(
        channelId = channelId,
        handle = handle,
        channelName = nameEn.ifBlank { nameNative },
        sourceKind = sourceKind,
        requiresScholarAttribution = requiresScholarAttribution
    )

    fun toProfile(): PodcastScholarProfile = PodcastScholarProfile(
        id = scholarId,
        language = language,
        profileType = PodcastProfileType.CHANNEL,
        displayNameNative = nameNative.ifBlank { nameEn },
        displayNameEn = nameEn.ifBlank { nameNative },
        attributionAliases = listOf(nameEn, nameNative).filter { it.isNotBlank() }.distinct(),
        sources = listOf(toChannelSource())
    )
}

/**
 * Unifies the 325+ verified YouTube channels from `assets/lectures/channel_catalog_v1.json`
 * (read-only) with the curated multilingual scholar registry in [PodcastScholarRegistry].
 *
 * Result: 350+ strictly verified, real YouTube channels across Urdu, English, and Arabic
 * (110+ per language), strictly language-isolated, with zero invented channel IDs.
 *
 * Respects all explicit exclusions (e.g., Mufti Muhammad Taqi Usmani is excluded from Podcasts).
 */
object PodcastCatalogBridge {

    private const val TAG = "PodcastCatalogBridge"
    private const val ASSET_CATALOG_PATH = "lectures/channel_catalog_v1.json"
    private val EXCLUDED_CHANNEL_IDS = setOf("UCLfMxJctTnF5BidnWSHKsNQ")

    @Volatile
    private var cachedEntriesByLanguage: Map<PodcastLanguage, List<PodcastCatalogChannelEntry>>? = null

    fun ensureLoaded(context: Context): Map<PodcastLanguage, List<PodcastCatalogChannelEntry>> {
        cachedEntriesByLanguage?.let { return it }
        synchronized(this) {
            cachedEntriesByLanguage?.let { return it }
            val loaded = buildMergedCatalog(context)
            cachedEntriesByLanguage = loaded
            return loaded
        }
    }

    fun channelsForLanguage(context: Context, language: PodcastLanguage): List<PodcastCatalogChannelEntry> {
        return ensureLoaded(context)[language].orEmpty()
    }

    fun totalVerifiedChannelCount(context: Context): Int {
        return ensureLoaded(context).values.sumOf { it.size }
    }

    fun verifiedChannelCountForLanguage(context: Context, language: PodcastLanguage): Int {
        return channelsForLanguage(context, language).size
    }

    /**
     * Returns additional verified channels from the 350+ catalog that are NOT already represented
     * by a curated scholar profile in [PodcastScholarRegistry], so users can open any channel
     * individually in addition to the curated major scholars.
     */
    fun extraCatalogChannelsForLanguage(context: Context, language: PodcastLanguage): List<PodcastScholarProfile> {
        val curatedChannelIds = PodcastScholarRegistry.profilesForLanguage(language)
            .flatMap { it.sources }
            .map { it.channelId.lowercase(Locale.ROOT) }
            .toSet()

        return channelsForLanguage(context, language)
            .filter { it.channelId.lowercase(Locale.ROOT) !in curatedChannelIds }
            .map { it.toProfile() }
    }

    private fun buildMergedCatalog(context: Context): Map<PodcastLanguage, List<PodcastCatalogChannelEntry>> {
        val buckets = mapOf(
            PodcastLanguage.URDU to LinkedHashMap<String, PodcastCatalogChannelEntry>(),
            PodcastLanguage.ENGLISH to LinkedHashMap<String, PodcastCatalogChannelEntry>(),
            PodcastLanguage.ARABIC to LinkedHashMap<String, PodcastCatalogChannelEntry>()
        )

        // 1. Seed with curated featured major scholars first (highest priority: Dr. Israr Ahmed,
        //    Dr. Zakir Naik, Peer Ajmal Raza Qadri, Maulana Tariq Jamil, Mufti Tariq Masood,
        //    Mufti Menk, Nouman Ali Khan, Dr. Omar Suleiman, Yasir Qadhi, Othman Al-Khamees,
        //    Saleh Al-Fawzan, Mustafa Hosny, Great History of Islam, etc.)
        for (lang in PodcastLanguage.entries) {
            val bucket = buckets.getValue(lang)
            val profiles = PodcastScholarRegistry.profilesForLanguage(lang)
            profiles.forEachIndexed { idx, profile ->
                val basePriority = 200 - idx
                profile.sources.forEach { src ->
                    if (!YouTubeRssParser.isValidChannelId(src.channelId)) return@forEach
                    if (src.channelId in EXCLUDED_CHANNEL_IDS) return@forEach
                    val dedupKey = src.channelId.lowercase(Locale.ROOT)
                    if (!bucket.containsKey(dedupKey)) {
                        bucket[dedupKey] = PodcastCatalogChannelEntry(
                            channelId = src.channelId,
                            handle = src.handle,
                            nameNative = profile.displayNameNative,
                            nameEn = src.channelName.ifBlank { profile.displayNameEn },
                            language = lang,
                            priority = basePriority,
                            sourceKind = src.sourceKind,
                            scholarId = profile.id,
                            scholarName = profile.displayNameNative,
                            requiresScholarAttribution = src.requiresScholarAttribution
                        )
                    }
                }
            }
        }

        // 2. Merge all 325+ verified, enabled channels from channel_catalog_v1.json (read-only)
        val jsonText = readCatalogAsset(context)
        if (!jsonText.isNullOrBlank()) {
            parseCatalogJsonIntoBuckets(jsonText, buckets)
        }

        return buckets.mapValues { (_, map) ->
            map.values.sortedByDescending { it.priority }
        }
    }

    private fun readCatalogAsset(context: Context): String? {
        return try {
            context.assets.open(ASSET_CATALOG_PATH).bufferedReader(Charsets.UTF_8).use { it.readText() }
        } catch (e: Exception) {
            Log.w(TAG, "Unable to read $ASSET_CATALOG_PATH: ${e.message}")
            null
        }
    }

    internal fun parseCatalogJsonIntoBuckets(
        jsonText: String,
        buckets: Map<PodcastLanguage, LinkedHashMap<String, PodcastCatalogChannelEntry>>
    ) {
        try {
            val root = JSONObject(jsonText)
            val arr = root.optJSONArray("channels") ?: JSONArray()
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val enabled = obj.optBoolean("enabled", false)
                val status = obj.optString("verificationStatus", "")
                if (!enabled || !status.equals("VERIFIED", ignoreCase = true)) continue

                val ytChannelId = obj.optString("youtubeChannelId", "").trim()
                if (!YouTubeRssParser.isValidChannelId(ytChannelId)) continue
                if (ytChannelId in EXCLUDED_CHANNEL_IDS) continue

                val key = obj.optString("channelId", "").trim()
                val nameEn = obj.optString("nameEn", obj.optString("channelName", "")).trim()
                if (isExcludedScholar(key, nameEn)) continue

                val primaryLangRaw = obj.optString("primaryLanguage", "").lowercase(Locale.ROOT)
                val podcastLang = when (primaryLangRaw) {
                    "ur_hi", "ur", "urdu" -> PodcastLanguage.URDU
                    "en", "english" -> PodcastLanguage.ENGLISH
                    "ar", "arabic" -> PodcastLanguage.ARABIC
                    else -> null
                } ?: continue

                val nameNative = when (podcastLang) {
                    PodcastLanguage.URDU -> obj.optString("nameUr", nameEn).ifBlank { nameEn }
                    PodcastLanguage.ARABIC -> obj.optString("nameAr", nameEn).ifBlank { nameEn }
                    PodcastLanguage.ENGLISH -> nameEn
                }
                val handle = obj.optString("handle", "").trim()
                val priority = obj.optInt("priority", 50).coerceIn(1, 150)

                val bucket = buckets.getValue(podcastLang)
                val dedupKey = ytChannelId.lowercase(Locale.ROOT)
                if (!bucket.containsKey(dedupKey)) {
                    bucket[dedupKey] = PodcastCatalogChannelEntry(
                        channelId = ytChannelId,
                        handle = handle,
                        nameNative = nameNative,
                        nameEn = nameEn,
                        language = podcastLang,
                        priority = priority,
                        sourceKind = PodcastSourceKind.OFFICIAL_CHANNEL,
                        scholarId = "cat_${key.ifBlank { ytChannelId }}",
                        scholarName = nameNative,
                        requiresScholarAttribution = false
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error parsing channel_catalog_v1.json: ${e.message}")
        }
    }

    private fun isExcludedScholar(key: String, nameEn: String): Boolean {
        val blob = "$key $nameEn".lowercase(Locale.ROOT)
        return blob.contains("taqi usmani") || blob.contains("taqi_usmani") || blob.contains("taqiusmani")
    }

    internal fun clearCacheForTest() {
        cachedEntriesByLanguage = null
    }
}
