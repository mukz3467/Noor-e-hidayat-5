package com.example.data.podcast

import com.example.data.model.AppLanguage
import com.example.data.repository.PodcastLanguageSection
import com.example.data.repository.YouTubeVideoItem

enum class PodcastLanguage(
    val id: String,
    val labelKey: String,
    val badgeEn: String,
    val badgeUr: String,
    val badgeAr: String,
    val badgeHi: String,
    val legacySection: PodcastLanguageSection
) {
    URDU(
        id = "ur",
        labelKey = "pod_lang_urdu",
        badgeEn = "Urdu",
        badgeUr = "اردو",
        badgeAr = "أردو",
        badgeHi = "उर्दू",
        legacySection = PodcastLanguageSection.URDU
    ),
    ENGLISH(
        id = "en",
        labelKey = "pod_lang_english",
        badgeEn = "English",
        badgeUr = "انگریزی",
        badgeAr = "إنجليزي",
        badgeHi = "अंग्रेज़ी",
        legacySection = PodcastLanguageSection.ENGLISH
    ),
    ARABIC(
        id = "ar",
        labelKey = "pod_lang_arabic",
        badgeEn = "Arabic",
        badgeUr = "عربی",
        badgeAr = "عربي",
        badgeHi = "अरबी",
        legacySection = PodcastLanguageSection.ARABIC
    );

    fun localizedBadge(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> badgeEn
        AppLanguage.URDU -> badgeUr
        AppLanguage.ARABIC -> badgeAr
        AppLanguage.HINDI -> badgeHi
    }

    companion object {
        fun fromId(id: String?): PodcastLanguage? = entries.firstOrNull { it.id.equals(id, ignoreCase = true) }
    }
}

enum class PodcastProfileType {
    SCHOLAR,
    CHANNEL
}

enum class PodcastSourceKind(val wireValue: String, val labelKey: String) {
    OFFICIAL_CHANNEL("official_channel", "pod_source_official"),
    HOSTED_INTERVIEW("hosted_interview", "pod_source_hosted"),
    UNAVAILABLE("unavailable", "pod_source_unavailable");

    companion object {
        fun fromWire(value: String?): PodcastSourceKind =
            entries.firstOrNull { it.wireValue.equals(value, ignoreCase = true) } ?: OFFICIAL_CHANNEL
    }
}

enum class PodcastContentType(val wireValue: String, val labelKey: String) {
    COMPLETE_PODCAST_EPISODE("podcast_episode", "pod_type_episode"),
    FULL_INTERVIEW("full_interview", "pod_type_interview"),
    LONG_FORM_DISCUSSION("long_discussion", "pod_type_discussion"),
    ISLAMIC_QA("islamic_qa", "pod_type_qa"),
    EDUCATIONAL_DISCUSSION("educational_discussion", "pod_type_educational"),
    FULL_LECTURE("full_lecture", "pod_type_lecture");

    companion object {
        fun fromWire(value: String?): PodcastContentType =
            entries.firstOrNull { it.wireValue.equals(value, ignoreCase = true) } ?: LONG_FORM_DISCUSSION
    }
}

/**
 * All 13 distinct categories inside each Scholar / Channel profile.
 * Only categories that actually contain verified eligible videos are displayed.
 */
enum class PodcastProfileCategory(val id: String, val labelKey: String) {
    ALL_ELIGIBLE("all_eligible", "pod_cat_all_eligible"),
    COMPLETE_PODCAST_EPISODES("complete_episodes", "pod_cat_complete_episodes"),
    FULL_INTERVIEWS("full_interviews", "pod_cat_full_interviews"),
    LONG_FORM_DISCUSSIONS("long_discussions", "pod_cat_long_discussions"),
    ISLAMIC_QA("islamic_qa", "pod_cat_islamic_qa"),
    QURAN_TAFSEER("quran_tafseer", "pod_cat_quran_tafseer"),
    HADITH_SUNNAH("hadith_sunnah", "pod_cat_hadith_sunnah"),
    ISLAMIC_HISTORY_SEERAH("history_seerah", "pod_cat_history_seerah"),
    ISLAMIC_LIFESTYLE("islamic_lifestyle", "pod_cat_islamic_lifestyle"),
    YOUTH_FAMILY("youth_family", "pod_cat_youth_family"),
    REPENTANCE_SELF_IMPROVEMENT("repentance_self", "pod_cat_repentance_self"),
    LATEST_LONG_FORM("latest_long_form", "pod_cat_latest_long_form"),
    MOST_VIEWED_LONG_FORM("most_viewed_long_form", "pod_cat_most_viewed");

    companion object {
        fun fromId(id: String?): PodcastProfileCategory? =
            entries.firstOrNull { it.id.equals(id, ignoreCase = true) }
    }
}

data class PodcastChannelSource(
    val channelId: String,
    val handle: String,
    val channelName: String,
    val sourceKind: PodcastSourceKind,
    val requiresScholarAttribution: Boolean = false
) {
    val rssUrl: String
        get() = "https://www.youtube.com/feeds/videos.xml?channel_id=$channelId"

    val channelUrl: String
        get() = "https://www.youtube.com/$handle"
}

data class PodcastScholarProfile(
    val id: String,
    val language: PodcastLanguage,
    val profileType: PodcastProfileType = PodcastProfileType.SCHOLAR,
    val displayNameNative: String,
    val displayNameEn: String,
    val attributionAliases: List<String>,
    val sources: List<PodcastChannelSource>,
    val verificationNote: String = ""
) {
    val primarySourceKind: PodcastSourceKind
        get() = when {
            sources.any { it.sourceKind == PodcastSourceKind.OFFICIAL_CHANNEL } ->
                PodcastSourceKind.OFFICIAL_CHANNEL
            sources.any { it.sourceKind == PodcastSourceKind.HOSTED_INTERVIEW } ->
                PodcastSourceKind.HOSTED_INTERVIEW
            else -> PodcastSourceKind.UNAVAILABLE
        }

    val isAvailable: Boolean
        get() = sources.isNotEmpty()

    fun displayNameFor(appLanguage: AppLanguage): String = when (appLanguage) {
        AppLanguage.ENGLISH -> if (language == PodcastLanguage.ENGLISH || profileType == PodcastProfileType.CHANNEL) {
            displayNameNative
        } else {
            "$displayNameEn ($displayNameNative)"
        }
        AppLanguage.URDU -> if (language == PodcastLanguage.URDU) displayNameNative else displayNameEn
        AppLanguage.ARABIC -> if (language == PodcastLanguage.ARABIC) displayNameNative else displayNameEn
        AppLanguage.HINDI -> displayNameEn
    }
}

enum class PodcastRejectionReason {
    INVALID_VIDEO_ID_OR_URL,
    MISSING_TITLE_OR_DELETED,
    EXPLICIT_SHORTS_URL,
    SHORTS_HASHTAG_OR_REEL,
    VERTICAL_OR_SQUARE_SHORT,
    UNDER_MIN_DURATION,
    UNVERIFIED_SHORT_DURATION,
    CLIP_TRAILER_OR_PROMO,
    UNVERIFIED_LONG_FORM_METADATA,
    WRONG_SCHOLAR,
    WRONG_LANGUAGE,
    DUPLICATE_VIDEO
}

data class PodcastCandidateVideo(
    val videoId: String,
    val title: String,
    val description: String = "",
    val channelId: String,
    val channelName: String,
    val videoUrl: String,
    val thumbnailUrl: String = "",
    val publishedIso: String = "",
    val publishedDisplay: String = "",
    val publishedEpochMs: Long = 0L,
    val durationSeconds: Int? = null,
    val viewCount: Long? = null,
    val videoWidth: Int? = null,
    val videoHeight: Int? = null,
    val sourceKind: PodcastSourceKind = PodcastSourceKind.OFFICIAL_CHANNEL,
    val requiresScholarAttribution: Boolean = false
)

data class PodcastFilterDecision(
    val accepted: Boolean,
    val rejectionReason: PodcastRejectionReason? = null,
    val contentType: PodcastContentType? = null,
    val categories: List<PodcastProfileCategory> = emptyList()
)

data class VerifiedPodcastVideo(
    val videoId: String,
    val title: String,
    val description: String,
    val scholarId: String,
    val scholarName: String,
    val channelId: String,
    val channelName: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val publishedIso: String,
    val publishedDisplay: String,
    val publishedEpochMs: Long,
    val durationSeconds: Int?,
    val viewCount: Long?,
    val language: PodcastLanguage,
    val contentType: PodcastContentType,
    val sourceKind: PodcastSourceKind,
    val categories: List<PodcastProfileCategory>,
    val isFromCache: Boolean = false
) {
    val isDurationVerified: Boolean
        get() = durationSeconds != null && durationSeconds >= PodcastLongFormFilter.DEFAULT_MIN_DURATION_SECONDS

    val durationDisplay: String
        get() = PodcastLongFormFilter.formatDuration(durationSeconds)

    fun toYouTubeVideoItem(): YouTubeVideoItem = YouTubeVideoItem(
        id = videoId,
        title = title,
        channel = channelName,
        channelId = channelId,
        channelKey = scholarId,
        thumbnail = thumbnailUrl,
        publishedIso = publishedIso,
        updatedIso = publishedIso,
        publishedDisplay = publishedDisplay,
        publishedEpochMs = publishedEpochMs,
        videoUrl = videoUrl,
        description = description,
        languageCode = language.id,
        verifiedPodcastLanguage = language.legacySection,
        durationSeconds = durationSeconds,
        viewCount = viewCount,
        isFromCache = isFromCache
    )
}

enum class PodcastFeedStatus {
    IDLE,
    ONLINE_FRESH,
    CACHED_FRESH,
    OFFLINE_CACHE,
    QUOTA_EXCEEDED_CACHE,
    UNAVAILABLE_NO_SOURCES,
    ERROR_NO_CACHE
}

data class ScholarPodcastLoadResult(
    val scholar: PodcastScholarProfile,
    val videos: List<VerifiedPodcastVideo>,
    val status: PodcastFeedStatus,
    val rejectedCount: Int = 0,
    val rejectionBreakdown: Map<PodcastRejectionReason, Int> = emptyMap(),
    val lastUpdatedMs: Long = 0L,
    val nextPageToken: String? = null,
    val hasMoreRemotePages: Boolean = false,
    val errorMessage: String? = null
) {
    val verifiedVideoCount: Int get() = videos.size

    /**
     * Returns ONLY categories that actually contain at least one verified eligible video.
     * Never exposes empty or fabricated categories.
     */
    fun nonEmptyCategories(): List<Pair<PodcastProfileCategory, Int>> {
        if (videos.isEmpty()) return emptyList()
        val result = mutableListOf<Pair<PodcastProfileCategory, Int>>()
        result.add(PodcastProfileCategory.ALL_ELIGIBLE to videos.size)
        for (cat in PodcastProfileCategory.entries) {
            if (cat == PodcastProfileCategory.ALL_ELIGIBLE) continue
            val count = when (cat) {
                PodcastProfileCategory.LATEST_LONG_FORM -> videos.size
                PodcastProfileCategory.MOST_VIEWED_LONG_FORM -> videos.count { (it.viewCount ?: 0L) > 0L }
                else -> videos.count { cat in it.categories }
            }
            if (count > 0) {
                result.add(cat to count)
            }
        }
        return result
    }

    fun videosForCategory(category: PodcastProfileCategory): List<VerifiedPodcastVideo> {
        return when (category) {
            PodcastProfileCategory.ALL_ELIGIBLE -> videos
            PodcastProfileCategory.LATEST_LONG_FORM -> videos.sortedByDescending { it.publishedEpochMs }
            PodcastProfileCategory.MOST_VIEWED_LONG_FORM ->
                videos.filter { (it.viewCount ?: 0L) > 0L }.sortedByDescending { it.viewCount ?: 0L }
            else -> videos.filter { category in it.categories }
        }
    }
}

/**
 * Aggregated long-form video suggestions across all verified channels in a single language
 * (Urdu, English, or Arabic — strictly isolated, never cross-mixed, zero Shorts).
 */
data class LanguageSuggestionsLoadResult(
    val language: PodcastLanguage,
    val videos: List<VerifiedPodcastVideo>,
    val status: PodcastFeedStatus,
    val totalVerifiedChannelsInLanguage: Int = 0,
    val channelsFetchedCount: Int = 0,
    val nextBatchOffset: Int = 0,
    val hasMoreChannelBatches: Boolean = false,
    val rejectedCount: Int = 0,
    val lastUpdatedMs: Long = 0L,
    val errorMessage: String? = null
) {
    val verifiedVideoCount: Int get() = videos.size

    fun nonEmptyCategories(): List<Pair<PodcastProfileCategory, Int>> {
        if (videos.isEmpty()) return emptyList()
        val result = mutableListOf<Pair<PodcastProfileCategory, Int>>()
        result.add(PodcastProfileCategory.ALL_ELIGIBLE to videos.size)
        for (cat in PodcastProfileCategory.entries) {
            if (cat == PodcastProfileCategory.ALL_ELIGIBLE) continue
            val count = when (cat) {
                PodcastProfileCategory.LATEST_LONG_FORM -> videos.size
                PodcastProfileCategory.MOST_VIEWED_LONG_FORM -> videos.count { (it.viewCount ?: 0L) > 0L }
                else -> videos.count { cat in it.categories }
            }
            if (count > 0) {
                result.add(cat to count)
            }
        }
        return result
    }

    fun videosForCategory(category: PodcastProfileCategory): List<VerifiedPodcastVideo> {
        return when (category) {
            PodcastProfileCategory.ALL_ELIGIBLE -> videos
            PodcastProfileCategory.LATEST_LONG_FORM -> videos.sortedByDescending { it.publishedEpochMs }
            PodcastProfileCategory.MOST_VIEWED_LONG_FORM ->
                videos.filter { (it.viewCount ?: 0L) > 0L }.sortedByDescending { it.viewCount ?: 0L }
            else -> videos.filter { category in it.categories }
        }
    }
}

