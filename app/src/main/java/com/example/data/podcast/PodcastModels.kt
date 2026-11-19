package com.example.data.podcast

import com.example.data.model.AppLanguage

enum class PodcastCategory(
    val id: String,
    val labelKey: String,
    val keywords: List<String>
) {
    QURAN_REFLECTIONS(
        id = "quran_reflections",
        labelKey = "pod_cat_quran_reflections",
        keywords = listOf("quran", "qur'an", "reflection", "tadabbur", "surah", "ayah", "juz", "30 for 30", "قرآن", "تدبر")
    ),
    QURAN_TAFSEER(
        id = "quran_tafseer",
        labelKey = "pod_cat_quran_tafseer",
        keywords = listOf("tafsir", "tafseer", "bayan", "exegesis", "تفسير", "تفسیر", "قرآن")
    ),
    HADITH_SUNNAH(
        id = "hadith_sunnah",
        labelKey = "pod_cat_hadith_sunnah",
        keywords = listOf("hadith", "hadeeth", "sunnah", "bukhari", "muslim", "nawawi", "حديث", "حدیث", "سنت")
    ),
    SEERAH(
        id = "seerah",
        labelKey = "pod_cat_seerah",
        keywords = listOf("seerah", "sirah", "prophet", "muhammad", "makkah", "madinah", "firsts", "companions", "سيرة", "سیرت")
    ),
    ISLAMIC_HISTORY(
        id = "islamic_history",
        labelKey = "pod_cat_islamic_history",
        keywords = listOf("history", "sahaba", "companions", "andalus", "caliphate", "ummah", "تاريخ", "تاریخ")
    ),
    ISLAMIC_KNOWLEDGE(
        id = "islamic_knowledge",
        labelKey = "pod_cat_islamic_knowledge",
        keywords = listOf("knowledge", "fiqh", "aqeedah", "creed", "seekers", "halal", "scholars", "علم", "فقہ", "مسائل")
    ),
    SPIRITUAL_DEVELOPMENT(
        id = "spiritual_development",
        labelKey = "pod_cat_spiritual_development",
        keywords = listOf("heart", "soul", "spirit", "spiritual", "tazkiyah", "ihsan", "dua", "dhikr", "ramadan", "patience", "قلب", "تزکیہ", "روح")
    ),
    MUSLIM_FAMILY(
        id = "muslim_family",
        labelKey = "pod_cat_muslim_family",
        keywords = listOf("family", "marriage", "parents", "parenting", "children", "youth", "home", "خاندان", "والدین", "أسرة")
    ),
    ISLAMIC_MANNERS(
        id = "islamic_manners",
        labelKey = "pod_cat_islamic_manners",
        keywords = listOf("manners", "akhlaq", "adab", "character", "mercy", "compassion", "kindness", "اخلاق", "أخلاق", "آداب")
    ),
    ISLAMIC_EDUCATION(
        id = "islamic_education",
        labelKey = "pod_cat_islamic_education",
        keywords = listOf("education", "curriculum", "learn", "academy", "study", "seekersguidance", "qalam", "تعليم", "تعلیم")
    ),
    NEW_MUSLIM_GUIDANCE(
        id = "new_muslim_guidance",
        labelKey = "pod_cat_new_muslim_guidance",
        keywords = listOf("new muslim", "revert", "convert", "basics", "pillar", "prayer", "islam 101", "foundation", "نو مسلم", "مسلم جديد")
    ),
    URDU_PODCASTS(
        id = "urdu_podcasts",
        labelKey = "pod_cat_urdu_podcasts",
        keywords = emptyList()
    ),
    ENGLISH_PODCASTS(
        id = "english_podcasts",
        labelKey = "pod_cat_english_podcasts",
        keywords = emptyList()
    ),
    ARABIC_PODCASTS(
        id = "arabic_podcasts",
        labelKey = "pod_cat_arabic_podcasts",
        keywords = emptyList()
    )
}

enum class PodcastVerificationStatus {
    VERIFIED_RSS_FEED,
    DIRECTORY_LOOKUP_VERIFIED,
    FEED_UNAVAILABLE
}

data class PodcastShow(
    val id: String,
    val showName: String,
    val publisher: String,
    val host: String,
    val officialRssFeedUrl: String,
    val itunesLookupTerm: String,
    val expectedKeywords: List<String>,
    val artworkUrl: String,
    val languageCode: String,
    val primaryCategories: List<PodcastCategory>,
    val sourceAttribution: String,
    val verificationStatus: PodcastVerificationStatus,
    val downloadPermittedByPublisher: Boolean,
    val publisherLicenseNote: String
)

data class PodcastEpisode(
    val guid: String,
    val showId: String,
    val showName: String,
    val publisher: String,
    val host: String,
    val title: String,
    val description: String,
    val pubDateRaw: String,
    val pubDateDisplay: String,
    val publishedEpochMs: Long,
    val audioEnclosureUrl: String,
    val mimeType: String,
    val fileLengthBytes: Long,
    val durationSeconds: Long,
    val durationDisplay: String,
    val artworkUrl: String,
    val languageCode: String,
    val categories: List<PodcastCategory>,
    val sourceAttribution: String,
    val downloadPermittedByPublisher: Boolean,
    val isFromCache: Boolean = false
)

enum class PodcastDownloadStatus {
    NOT_DOWNLOADED,
    DOWNLOADING,
    PAUSED,
    DOWNLOADED,
    FAILED
}

data class PodcastDownloadProgress(
    val episodeGuid: String,
    val status: PodcastDownloadStatus,
    val bytesDownloaded: Long = 0L,
    val totalBytes: Long = 0L,
    val localFilePath: String? = null,
    val errorMessage: String? = null
) {
    val progressFraction: Float
        get() = if (totalBytes > 0L) (bytesDownloaded.toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f) else 0f
}

data class PodcastFeedLoadResult(
    val shows: List<PodcastShow>,
    val episodes: List<PodcastEpisode>,
    val isOffline: Boolean,
    val isFromCache: Boolean,
    val unavailableShows: List<String> = emptyList(),
    val lastUpdatedMs: Long = 0L,
    val errorMessage: String? = null
)

data class PodcastPlaybackUiState(
    val currentEpisode: PodcastEpisode? = null,
    val queue: List<PodcastEpisode> = emptyList(),
    val currentIndex: Int = -1,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isConnecting: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val playingOfflineFile: Boolean = false,
    val errorMessage: String? = null
)

fun podcastLanguageDisplayName(code: String, uiLanguage: AppLanguage): String = when (code.lowercase()) {
    "ur" -> when (uiLanguage) {
        AppLanguage.URDU -> "اردو"
        AppLanguage.ARABIC -> "الأردية"
        AppLanguage.HINDI -> "उर्दू"
        AppLanguage.ENGLISH -> "Urdu"
    }
    "ar" -> when (uiLanguage) {
        AppLanguage.URDU -> "عربی"
        AppLanguage.ARABIC -> "العربية"
        AppLanguage.HINDI -> "अरबी"
        AppLanguage.ENGLISH -> "Arabic"
    }
    else -> when (uiLanguage) {
        AppLanguage.URDU -> "انگریزی"
        AppLanguage.ARABIC -> "الإنجليزية"
        AppLanguage.HINDI -> "अंग्रेज़ी"
        AppLanguage.ENGLISH -> "English"
    }
}
