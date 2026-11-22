package com.example.data.lectures

import android.content.Context
import android.util.Log
import com.example.data.model.AppLanguage
import com.example.data.repository.LectureCategory
import com.example.data.repository.PodcastLanguageSection
import com.example.data.repository.PodcastTopicFilter
import com.example.data.repository.VerifiedIslamicChannel
import org.json.JSONObject

enum class ChannelVerificationStatus(val wireValue: String) {
    VERIFIED("VERIFIED"),
    CANDIDATE("CANDIDATE"),
    REJECTED("REJECTED"),
    TEMPORARILY_UNAVAILABLE("TEMPORARILY_UNAVAILABLE"),
    INVALID_RSS("INVALID_RSS");

    companion object {
        fun fromWire(value: String?): ChannelVerificationStatus =
            entries.firstOrNull { it.wireValue.equals(value?.trim(), ignoreCase = true) } ?: CANDIDATE
    }
}

enum class ChannelFeedStatus(val wireValue: String) {
    HEALTHY("HEALTHY"),
    UNKNOWN("UNKNOWN"),
    TEMPORARILY_UNAVAILABLE("TEMPORARILY_UNAVAILABLE"),
    INVALID_RSS("INVALID_RSS"),
    RATE_LIMITED("RATE_LIMITED");

    companion object {
        fun fromWire(value: String?): ChannelFeedStatus =
            entries.firstOrNull { it.wireValue.equals(value?.trim(), ignoreCase = true) } ?: UNKNOWN
    }
}

enum class ChannelType(val wireValue: String) {
    SCHOLAR("SCHOLAR"),
    INSTITUTE("INSTITUTE"),
    ACADEMY("ACADEMY"),
    BROADCASTER("BROADCASTER"),
    EDUCATIONAL("EDUCATIONAL"),
    COMMUNITY_EDUCATION("COMMUNITY_EDUCATION");

    companion object {
        fun fromWire(value: String?): ChannelType =
            entries.firstOrNull { it.wireValue.equals(value?.trim(), ignoreCase = true) } ?: SCHOLAR
    }
}

/**
 * Combined language groups for Islamic Lectures filtering:
 * - ALL: All verified languages
 * - URDU_HINDI: Combined Urdu & Hindi target group (100+ target)
 * - ENGLISH: English group (100+ target)
 * - ARABIC: Arabic group (100+ target)
 * - UNKNOWN: Content whose spoken language could not be reliably confirmed
 */
enum class LectureLanguageGroup(
    val id: String,
    val labelKey: String
) {
    ALL("all", "lectures_all_languages"),
    URDU_HINDI("ur_hi", "lectures_lang_ur_hi"),
    ENGLISH("en", "lectures_lang_en"),
    ARABIC("ar", "lectures_lang_ar"),
    UNKNOWN("und", "lectures_lang_unknown");

    companion object {
        val USER_FILTER_OPTIONS: List<LectureLanguageGroup> = listOf(ALL, URDU_HINDI, ENGLISH, ARABIC)

        fun fromCode(code: String?): LectureLanguageGroup {
            return when (code?.trim()?.lowercase()) {
                "all", "", null -> ALL
                "ur_hi", "ur", "hi", "urdu", "hindi" -> URDU_HINDI
                "en", "eng", "english" -> ENGLISH
                "ar", "ara", "arabic" -> ARABIC
                else -> UNKNOWN
            }
        }
    }
}

enum class DurationVerificationStatus(val wireValue: String) {
    /** Confirmed >= configured minimum lecture duration from real XML/player metadata. */
    CONFIRMED_LONG_FORM("CONFIRMED_LONG_FORM"),

    /** Confirmed short clip / YouTube Short (< min lecture duration or /shorts/ URL) — rejected from lectures. */
    CONFIRMED_SHORT_REJECTED("CONFIRMED_SHORT_REJECTED"),

    /**
     * YouTube's public Atom RSS feed does not supply video duration.
     * Duration remains unverified until supplied by an official metadata/player source.
     * Must NEVER be fabricated or labelled as confirmed long-form.
     */
    UNVERIFIED_FROM_RSS("UNVERIFIED_FROM_RSS");

    companion object {
        fun fromWire(value: String?): DurationVerificationStatus =
            entries.firstOrNull { it.wireValue.equals(value?.trim(), ignoreCase = true) }
                ?: UNVERIFIED_FROM_RSS
    }
}

enum class DurationFilterPolicy {
    /** Show ONLY videos whose real duration has been confirmed >= minDurationSeconds. */
    CONFIRMED_LONG_FORM_ONLY,

    /**
     * Exclude all confirmed Shorts/clips (< minDurationSeconds or /shorts/ URL), and display
     * RSS items whose duration is not provided by YouTube RSS with an explicit "Duration unverified" label.
     */
    EXCLUDE_CONFIRMED_SHORTS_SHOW_UNVERIFIED_WITH_BADGE
}

/**
 * Single centralized duration policy for long-form Islamic lectures.
 * Never invent, guess, or infer durations from titles, descriptions, or thumbnails.
 */
object LectureDurationConfig {
    const val MIN_LECTURE_DURATION_SECONDS = 480 // 8 minutes default
    const val SHORT_MAX_SECONDS = 60
    const val CLIP_MAX_SECONDS = 180
    val ALLOWED_MIN_DURATION_SECONDS_OPTIONS: List<Int> = listOf(480, 600, 900, 1200) // 8, 10, 15, 20 min

    fun evaluateDuration(
        durationSeconds: Int?,
        isShortsUrl: Boolean = false,
        minDurationSeconds: Int = MIN_LECTURE_DURATION_SECONDS
    ): DurationVerificationStatus {
        val threshold = minDurationSeconds.coerceAtLeast(MIN_LECTURE_DURATION_SECONDS)
        if (isShortsUrl) return DurationVerificationStatus.CONFIRMED_SHORT_REJECTED
        if (durationSeconds == null || durationSeconds <= 0) {
            return DurationVerificationStatus.UNVERIFIED_FROM_RSS
        }
        return if (durationSeconds >= threshold) {
            DurationVerificationStatus.CONFIRMED_LONG_FORM
        } else {
            DurationVerificationStatus.CONFIRMED_SHORT_REJECTED
        }
    }

    fun isEligibleForDisplay(
        status: DurationVerificationStatus,
        policy: DurationFilterPolicy
    ): Boolean = when (status) {
        DurationVerificationStatus.CONFIRMED_SHORT_REJECTED -> false
        DurationVerificationStatus.CONFIRMED_LONG_FORM -> true
        DurationVerificationStatus.UNVERIFIED_FROM_RSS ->
            policy == DurationFilterPolicy.EXCLUDE_CONFIRMED_SHORTS_SHOW_UNVERIFIED_WITH_BADGE
    }

    fun formatConfirmedDuration(durationSeconds: Int?): String {
        if (durationSeconds == null || durationSeconds <= 0) return ""
        val hours = durationSeconds / 3600
        val minutes = (durationSeconds % 3600) / 60
        val seconds = durationSeconds % 60
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%d:%02d", minutes, seconds)
        }
    }
}

data class ChannelFeedHealth(
    val channelId: String,
    val lastFeedSuccessAtMs: Long = 0L,
    val lastErrorAtMs: Long = 0L,
    val consecutiveFailures: Int = 0,
    val backoffUntilMs: Long = 0L,
    val lastErrorMessage: String? = null,
    val feedStatus: ChannelFeedStatus = ChannelFeedStatus.UNKNOWN
) {
    fun isInBackoff(nowMs: Long): Boolean = backoffUntilMs > nowMs
}

data class ChannelRegistryEntry(
    val channelId: String,
    val channelName: String,
    val scholarOrOrgName: String,
    val youtubeChannelId: String,
    val handle: String,
    val channelUrl: String,
    val rssUrl: String,
    val nameEn: String,
    val nameUr: String,
    val nameAr: String,
    val nameHi: String,
    val specialtyEn: String,
    val specialtyUr: String,
    val specialtyAr: String,
    val specialtyHi: String,
    val primaryLanguage: LectureLanguageGroup,
    val secondaryLanguages: List<LectureLanguageGroup> = emptyList(),
    val country: String = "",
    val topics: List<String> = emptyList(),
    val channelType: ChannelType = ChannelType.SCHOLAR,
    val verificationStatus: ChannelVerificationStatus = ChannelVerificationStatus.CANDIDATE,
    val verificationSource: String = "",
    val officialWebsite: String = "",
    val lastVerifiedAt: String = "",
    val lastFeedSuccessAt: Long = 0L,
    val feedStatus: ChannelFeedStatus = ChannelFeedStatus.UNKNOWN,
    val duplicateDetectionKey: String = "",
    val notes: String = "",
    val enabled: Boolean = false,
    val priority: Int = 50,
    val createdAt: String = "2026-11-19",
    val updatedAt: String = "2026-11-19",
    val podcastLanguage: PodcastLanguageSection? = null,
    val podcastTopics: List<PodcastTopicFilter> = emptyList()
) {
    fun supportsLanguage(group: LectureLanguageGroup): Boolean {
        if (group == LectureLanguageGroup.ALL) return true
        return primaryLanguage == group || secondaryLanguages.contains(group)
    }

    fun localizedName(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> nameEn.ifBlank { channelName }
        AppLanguage.URDU -> nameUr.ifBlank { nameEn.ifBlank { channelName } }
        AppLanguage.ARABIC -> nameAr.ifBlank { nameEn.ifBlank { channelName } }
        AppLanguage.HINDI -> nameHi.ifBlank { nameEn.ifBlank { channelName } }
    }

    fun localizedSpecialty(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> specialtyEn
        AppLanguage.URDU -> specialtyUr.ifBlank { specialtyEn }
        AppLanguage.ARABIC -> specialtyAr.ifBlank { specialtyEn }
        AppLanguage.HINDI -> specialtyHi.ifBlank { specialtyEn }
    }

    fun toVerifiedIslamicChannel(): VerifiedIslamicChannel {
        val resolvedCategories = topics.mapNotNull { topicId ->
            LectureCategory.entries.firstOrNull { it.id == topicId }
        }
        val legacyLangCode = when (primaryLanguage) {
            LectureLanguageGroup.URDU_HINDI -> "ur"
            LectureLanguageGroup.ENGLISH -> "en"
            LectureLanguageGroup.ARABIC -> "ar"
            else -> "und"
        }
        return VerifiedIslamicChannel(
            key = channelId,
            channelId = youtubeChannelId,
            handle = handle,
            nameEn = nameEn.ifBlank { channelName },
            nameUr = nameUr.ifBlank { nameEn },
            nameAr = nameAr.ifBlank { nameEn },
            nameHi = nameHi.ifBlank { nameEn },
            specialtyEn = specialtyEn,
            specialtyUr = specialtyUr.ifBlank { specialtyEn },
            specialtyAr = specialtyAr.ifBlank { specialtyEn },
            specialtyHi = specialtyHi.ifBlank { specialtyEn },
            languageCode = legacyLangCode,
            primaryLanguageGroup = primaryLanguage,
            secondaryLanguageGroups = secondaryLanguages,
            primaryCategories = resolvedCategories,
            podcastLanguage = podcastLanguage,
            podcastTopics = podcastTopics,
            verificationStatus = verificationStatus,
            officialWebsite = officialWebsite,
            verificationSource = verificationSource,
            channelType = channelType,
            priority = priority,
            notes = notes
        )
    }
}

data class ChannelDirectoryCoverageReport(
    val catalogVersion: Int,
    val urduHindiVerified: Int,
    val urduHindiTarget: Int = 100,
    val englishVerified: Int,
    val englishTarget: Int = 100,
    val arabicVerified: Int,
    val arabicTarget: Int = 100,
    val totalVerified: Int,
    val totalTarget: Int = 300,
    val candidateCount: Int,
    val rejectedCount: Int,
    val unavailableCount: Int,
    val invalidRssCount: Int
) {
    val isTargetMet: Boolean
        get() = urduHindiVerified >= urduHindiTarget &&
            englishVerified >= englishTarget &&
            arabicVerified >= arabicTarget &&
            totalVerified >= totalTarget
}

data class LoadedChannelCatalog(
    val catalogVersion: Int,
    val updatedAt: String,
    val allEntries: List<ChannelRegistryEntry>,
    val verifiedEntries: List<ChannelRegistryEntry>,
    val coverageReport: ChannelDirectoryCoverageReport
)

/**
 * Loads and validates the versioned offline channel catalog from assets (`lectures/channel_catalog_v1.json`).
 *
 * Strict integrity guarantees:
 * - Only channels with status == VERIFIED, enabled == true, canonical UC[22] channel ID,
 *   and matching official RSS URL (`https://www.youtube.com/feeds/videos.xml?channel_id=UC...`)
 *   are promoted into `verifiedEntries`.
 * - Candidate, rejected, temporarily unavailable, or invalid RSS records are retained in `allEntries`
 *   for transparent directory auditing and NEVER leak into the normal user-facing Lectures feed.
 * - Duplicate `youtubeChannelId` or `duplicateDetectionKey` entries are automatically rejected.
 */
object ChannelCatalogLoader {
    private const val TAG = "ChannelCatalogLoader"
    const val CATALOG_ASSET_PATH = "lectures/channel_catalog_v1.json"
    private val VALID_UC_ID_REGEX = Regex("^UC[A-Za-z0-9_-]{22}$")
    private val EXPECTED_RSS_PREFIX = "https://www.youtube.com/feeds/videos.xml?channel_id="

    @Volatile
    private var cachedCatalog: LoadedChannelCatalog? = null

    @Volatile
    private var cachedFromAsset: Boolean = false

    fun load(context: Context? = null, forceReload: Boolean = false): LoadedChannelCatalog {
        if (!forceReload) {
            val existing = cachedCatalog
            if (existing != null && (cachedFromAsset || context == null)) {
                return existing
            }
        }
        val rawJson = readAssetJson(context)
        val parsed = if (rawJson != null) {
            cachedFromAsset = true
            parseCatalogJson(rawJson)
        } else {
            cachedFromAsset = false
            buildBuiltInFallbackCatalog()
        }
        cachedCatalog = parsed
        return parsed
    }

    fun clearCacheForTest() {
        cachedCatalog = null
        cachedFromAsset = false
    }

    private fun readAssetJson(context: Context?): String? {
        if (context == null) return null
        return try {
            context.assets.open(CATALOG_ASSET_PATH).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.w(TAG, "Unable to read $CATALOG_ASSET_PATH from assets: ${e::class.java.simpleName}")
            null
        }
    }

    fun parseCatalogJson(rawJson: String): LoadedChannelCatalog {
        val root = JSONObject(rawJson)
        val version = root.optInt("catalogVersion", 1)
        val updatedAt = root.optString("updatedAt", "2026-11-19")
        val targets = root.optJSONObject("targets")
        val targetUrHi = targets?.optInt("urduHindi", 100) ?: 100
        val targetEn = targets?.optInt("english", 100) ?: 100
        val targetAr = targets?.optInt("arabic", 100) ?: 100
        val targetTotal = targets?.optInt("total", 300) ?: 300

        val arr = root.optJSONArray("channels")
        val allEntries = mutableListOf<ChannelRegistryEntry>()
        val seenKeys = HashSet<String>()
        val seenYoutubeIds = HashSet<String>()

        if (arr != null) {
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val id = obj.optString("channelId").trim()
                if (id.isBlank()) continue

                val ytId = obj.optString("youtubeChannelId").trim()
                val rawStatus = ChannelVerificationStatus.fromWire(obj.optString("verificationStatus"))
                val dupKey = obj.optString("duplicateDetectionKey").trim().lowercase()
                    .ifBlank { if (ytId.isNotBlank()) "uc:${ytId.lowercase()}" else "id:${id.lowercase()}" }

                // Duplicate guard
                if (!seenKeys.add(dupKey)) continue
                if (ytId.isNotBlank() && !seenYoutubeIds.add(ytId.lowercase())) continue

                val hasValidUc = VALID_UC_ID_REGEX.matches(ytId)
                val canonicalRss = if (hasValidUc) "$EXPECTED_RSS_PREFIX$ytId" else ""
                val declaredRss = obj.optString("rssUrl").trim()
                val rssValid = hasValidUc && (declaredRss.isBlank() || declaredRss == canonicalRss)

                // Never allow VERIFIED without canonical UC ID and official RSS URL
                val effectiveStatus = when {
                    rawStatus == ChannelVerificationStatus.VERIFIED && !hasValidUc ->
                        ChannelVerificationStatus.INVALID_RSS
                    rawStatus == ChannelVerificationStatus.VERIFIED && !rssValid ->
                        ChannelVerificationStatus.INVALID_RSS
                    else -> rawStatus
                }

                val secondaryLangs = mutableListOf<LectureLanguageGroup>()
                val secArr = obj.optJSONArray("secondaryLanguages")
                if (secArr != null) {
                    for (j in 0 until secArr.length()) {
                        val grp = LectureLanguageGroup.fromCode(secArr.optString(j))
                        if (grp != LectureLanguageGroup.ALL && grp != LectureLanguageGroup.UNKNOWN) {
                            secondaryLangs.add(grp)
                        }
                    }
                }

                val topics = mutableListOf<String>()
                val topicsArr = obj.optJSONArray("topics")
                if (topicsArr != null) {
                    for (j in 0 until topicsArr.length()) {
                        val t = topicsArr.optString(j).trim()
                        if (t.isNotBlank()) topics.add(t)
                    }
                }

                val podLangStr = obj.optString("podcastLanguage", "")
                val podLang = PodcastLanguageSection.entries.firstOrNull {
                    it.id.equals(podLangStr, ignoreCase = true)
                }

                val podTopics = mutableListOf<PodcastTopicFilter>()
                val podTopicsArr = obj.optJSONArray("podcastTopics")
                if (podTopicsArr != null) {
                    for (j in 0 until podTopicsArr.length()) {
                        val ptId = podTopicsArr.optString(j).trim()
                        PodcastTopicFilter.entries.firstOrNull { it.id == ptId }?.let { podTopics.add(it) }
                    }
                }

                val entry = ChannelRegistryEntry(
                    channelId = id,
                    channelName = obj.optString("channelName", id),
                    scholarOrOrgName = obj.optString("scholarOrOrgName", obj.optString("channelName", id)),
                    youtubeChannelId = if (hasValidUc) ytId else "",
                    handle = obj.optString("handle", ""),
                    channelUrl = if (hasValidUc) "https://www.youtube.com/channel/$ytId" else obj.optString("channelUrl", ""),
                    rssUrl = canonicalRss,
                    nameEn = obj.optString("nameEn", obj.optString("channelName", id)),
                    nameUr = obj.optString("nameUr", ""),
                    nameAr = obj.optString("nameAr", ""),
                    nameHi = obj.optString("nameHi", ""),
                    specialtyEn = obj.optString("specialtyEn", ""),
                    specialtyUr = obj.optString("specialtyUr", ""),
                    specialtyAr = obj.optString("specialtyAr", ""),
                    specialtyHi = obj.optString("specialtyHi", ""),
                    primaryLanguage = LectureLanguageGroup.fromCode(obj.optString("primaryLanguage")),
                    secondaryLanguages = secondaryLangs,
                    country = obj.optString("country", ""),
                    topics = topics,
                    channelType = ChannelType.fromWire(obj.optString("channelType")),
                    verificationStatus = effectiveStatus,
                    verificationSource = obj.optString("verificationSource", ""),
                    officialWebsite = obj.optString("officialWebsite", ""),
                    lastVerifiedAt = obj.optString("lastVerifiedAt", ""),
                    lastFeedSuccessAt = obj.optLong("lastFeedSuccessAt", 0L),
                    feedStatus = ChannelFeedStatus.fromWire(obj.optString("feedStatus")),
                    duplicateDetectionKey = dupKey,
                    notes = obj.optString("notes", ""),
                    enabled = obj.optBoolean("enabled", false) && effectiveStatus == ChannelVerificationStatus.VERIFIED,
                    priority = obj.optInt("priority", 50),
                    createdAt = obj.optString("createdAt", updatedAt),
                    updatedAt = obj.optString("updatedAt", updatedAt),
                    podcastLanguage = podLang,
                    podcastTopics = podTopics
                )
                allEntries.add(entry)
            }
        }

        val verifiedEntries = allEntries
            .filter {
                it.verificationStatus == ChannelVerificationStatus.VERIFIED &&
                    it.enabled &&
                    VALID_UC_ID_REGEX.matches(it.youtubeChannelId) &&
                    it.rssUrl == "$EXPECTED_RSS_PREFIX${it.youtubeChannelId}"
            }
            .sortedByDescending { it.priority }

        val report = ChannelDirectoryCoverageReport(
            catalogVersion = version,
            urduHindiVerified = verifiedEntries.count { it.primaryLanguage == LectureLanguageGroup.URDU_HINDI },
            urduHindiTarget = targetUrHi,
            englishVerified = verifiedEntries.count { it.primaryLanguage == LectureLanguageGroup.ENGLISH },
            englishTarget = targetEn,
            arabicVerified = verifiedEntries.count { it.primaryLanguage == LectureLanguageGroup.ARABIC },
            arabicTarget = targetAr,
            totalVerified = verifiedEntries.size,
            totalTarget = targetTotal,
            candidateCount = allEntries.count { it.verificationStatus == ChannelVerificationStatus.CANDIDATE },
            rejectedCount = allEntries.count { it.verificationStatus == ChannelVerificationStatus.REJECTED },
            unavailableCount = allEntries.count { it.verificationStatus == ChannelVerificationStatus.TEMPORARILY_UNAVAILABLE },
            invalidRssCount = allEntries.count { it.verificationStatus == ChannelVerificationStatus.INVALID_RSS }
        )

        return LoadedChannelCatalog(
            catalogVersion = version,
            updatedAt = updatedAt,
            allEntries = allEntries,
            verifiedEntries = verifiedEntries,
            coverageReport = report
        )
    }

    private fun buildBuiltInFallbackCatalog(): LoadedChannelCatalog {
        val entries = com.example.data.repository.YouTubeRepository.SEED_VERIFIED_ENTRIES
        val verified = entries.filter {
            it.verificationStatus == ChannelVerificationStatus.VERIFIED &&
                it.enabled &&
                VALID_UC_ID_REGEX.matches(it.youtubeChannelId)
        }
        return LoadedChannelCatalog(
            catalogVersion = 1,
            updatedAt = "2026-11-19",
            allEntries = entries,
            verifiedEntries = verified,
            coverageReport = ChannelDirectoryCoverageReport(
                catalogVersion = 1,
                urduHindiVerified = verified.count { it.primaryLanguage == LectureLanguageGroup.URDU_HINDI },
                englishVerified = verified.count { it.primaryLanguage == LectureLanguageGroup.ENGLISH },
                arabicVerified = verified.count { it.primaryLanguage == LectureLanguageGroup.ARABIC },
                totalVerified = verified.size,
                candidateCount = 0,
                rejectedCount = 0,
                unavailableCount = 0,
                invalidRssCount = 0
            )
        )
    }
}
