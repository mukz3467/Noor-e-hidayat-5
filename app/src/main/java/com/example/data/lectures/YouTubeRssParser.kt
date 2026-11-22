package com.example.data.lectures

import android.util.Log
import com.example.data.repository.LectureCategory
import com.example.data.repository.PodcastLanguageSection
import com.example.data.repository.VerifiedIslamicChannel
import com.example.data.repository.YouTubeVideoItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Namespace-aware official YouTube Atom RSS XML parser (`https://www.youtube.com/feeds/videos.xml?channel_id=...`).
 *
 * Extracts:
 * - yt:videoId
 * - yt:channelId
 * - title
 * - published & updated timestamps
 * - author name & uri
 * - link (alternate href, detecting /shorts/ URLs)
 * - media:thumbnail, media:description, media:statistics views
 * - duration ONLY when actually supplied in XML (media:content duration / yt:duration seconds).
 *   NEVER invents or guesses video duration when omitted by YouTube RSS.
 */
object YouTubeRssParser {

    private const val TAG = "YouTubeRssParser"
    private const val NS_YT = "http://www.youtube.com/xml/schemas/2015"
    private const val NS_MEDIA = "http://search.yahoo.com/mrss/"
    private const val MAX_DESCRIPTION_LENGTH = 600

    private val VIDEO_ID_REGEX = Regex("^[A-Za-z0-9_-]{11}$")
    private val CHANNEL_ID_REGEX = Regex("^UC[A-Za-z0-9_-]{22}$")
    private val DELETED_OR_PRIVATE_TITLES = setOf(
        "deleted video",
        "[deleted video]",
        "private video",
        "[private video]",
        "unavailable video"
    )

    fun isValidVideoId(videoId: String): Boolean = VIDEO_ID_REGEX.matches(videoId.trim())

    fun isValidChannelId(channelId: String): Boolean = CHANNEL_ID_REGEX.matches(channelId.trim())

    fun parseAtomFeedXml(
        xml: String,
        channel: VerifiedIslamicChannel? = null,
        minDurationSeconds: Int = LectureDurationConfig.MIN_LECTURE_DURATION_SECONDS,
        verifiedDurationLookup: ((String) -> Int?)? = null
    ): List<YouTubeVideoItem> {
        val cleanXml = xml.trimStart('\uFEFF', ' ', '\t', '\r', '\n').trim()
        if (cleanXml.isBlank()) return emptyList()
        val items = mutableListOf<YouTubeVideoItem>()

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(StringReader(cleanXml))

            var eventType = parser.eventType
            var insideEntry = false
            var insideAuthor = false
            var feedTitle = ""
            var feedChannelId = ""

            var entryVideoId = ""
            var entryChannelId = ""
            var entryTitle = ""
            var entryAuthor = ""
            var entryPublished = ""
            var entryUpdated = ""
            var entryThumbnail = ""
            var entryLink = ""
            var entryDescription = ""
            var entryDurationSeconds: Int? = null
            var entryViews: Long? = null
            var isShortsLink = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name ?: ""
                        val ns = parser.namespace ?: ""
                        when {
                            !insideEntry && name == "title" -> {
                                feedTitle = safeNextText(parser)
                            }
                            !insideEntry && (name == "channelId" || (ns == NS_YT && name == "channelId")) -> {
                                feedChannelId = safeNextText(parser)
                            }
                            name == "entry" -> {
                                insideEntry = true
                                insideAuthor = false
                                entryVideoId = ""
                                entryChannelId = ""
                                entryTitle = ""
                                entryAuthor = ""
                                entryPublished = ""
                                entryUpdated = ""
                                entryThumbnail = ""
                                entryLink = ""
                                entryDescription = ""
                                entryDurationSeconds = null
                                entryViews = null
                                isShortsLink = false
                            }
                            insideEntry -> {
                                when {
                                    name == "videoId" && (ns.isEmpty() || ns == NS_YT) -> {
                                        entryVideoId = safeNextText(parser)
                                    }
                                    name == "channelId" && (ns.isEmpty() || ns == NS_YT) -> {
                                        entryChannelId = safeNextText(parser)
                                    }
                                    name == "title" && ns != NS_MEDIA -> {
                                        val txt = safeNextText(parser)
                                        if (txt.isNotBlank()) entryTitle = txt
                                    }
                                    name == "title" && ns == NS_MEDIA -> {
                                        val txt = safeNextText(parser)
                                        if (entryTitle.isBlank() && txt.isNotBlank()) entryTitle = txt
                                    }
                                    name == "description" && (ns == NS_MEDIA || ns.isEmpty()) -> {
                                        val desc = safeNextText(parser)
                                        if (desc.isNotBlank() && entryDescription.isBlank()) {
                                            entryDescription = desc.take(MAX_DESCRIPTION_LENGTH)
                                        }
                                    }
                                    name == "author" -> {
                                        insideAuthor = true
                                    }
                                    name == "name" && insideAuthor -> {
                                        entryAuthor = safeNextText(parser)
                                    }
                                    name == "published" -> {
                                        entryPublished = safeNextText(parser)
                                    }
                                    name == "updated" -> {
                                        entryUpdated = safeNextText(parser)
                                    }
                                    name == "thumbnail" -> {
                                        val urlAttr = parser.getAttributeValue(null, "url")?.trim().orEmpty()
                                        if (urlAttr.startsWith("https://") && entryThumbnail.isBlank()) {
                                            entryThumbnail = urlAttr
                                        }
                                    }
                                    name == "content" && ns == NS_MEDIA -> {
                                        val durAttr = parser.getAttributeValue(null, "duration")?.trim()
                                        val parsedDur = durAttr?.toIntOrNull()
                                        if (parsedDur != null && parsedDur > 0 && entryDurationSeconds == null) {
                                            entryDurationSeconds = parsedDur
                                        }
                                    }
                                    name == "duration" && (ns == NS_YT || ns == NS_MEDIA || ns.isEmpty()) -> {
                                        val secAttr = parser.getAttributeValue(null, "seconds")?.trim()
                                            ?: parser.getAttributeValue(null, "duration")?.trim()
                                        val parsedDur = secAttr?.toIntOrNull()
                                        if (parsedDur != null && parsedDur > 0 && entryDurationSeconds == null) {
                                            entryDurationSeconds = parsedDur
                                        }
                                    }
                                    name == "statistics" && (ns == NS_MEDIA || ns.isEmpty()) -> {
                                        val viewsAttr = parser.getAttributeValue(null, "views")?.trim()
                                        entryViews = viewsAttr?.toLongOrNull()
                                    }
                                    name == "link" -> {
                                        val rel = parser.getAttributeValue(null, "rel") ?: "alternate"
                                        val href = parser.getAttributeValue(null, "href")?.trim().orEmpty()
                                        if (rel == "alternate" && href.isNotBlank()) {
                                            entryLink = href
                                            if (href.contains("/shorts/", ignoreCase = true)) {
                                                isShortsLink = true
                                                if (entryVideoId.isBlank()) {
                                                    entryVideoId = href.substringAfter("/shorts/").substringBefore("?").substringBefore("/").trim()
                                                }
                                            } else if (entryVideoId.isBlank() && href.contains("v=")) {
                                                entryVideoId = href.substringAfter("v=").substringBefore("&").trim()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        val name = parser.name ?: ""
                        if (name == "author" && insideAuthor) {
                            insideAuthor = false
                        } else if (name == "entry" && insideEntry) {
                            insideEntry = false
                            val normalizedTitle = entryTitle.trim()
                            val isDeletedOrPrivate = normalizedTitle.lowercase(Locale.ROOT) in DELETED_OR_PRIVATE_TITLES

                            if (isValidVideoId(entryVideoId) && normalizedTitle.isNotBlank() && !isDeletedOrPrivate) {
                                val resolvedChannelId = when {
                                    isValidChannelId(entryChannelId) -> entryChannelId
                                    channel != null && isValidChannelId(channel.channelId) -> channel.channelId
                                    isValidChannelId(feedChannelId) -> feedChannelId
                                    else -> ""
                                }
                                val resolvedChannelName = entryAuthor.ifBlank {
                                    channel?.nameEn ?: feedTitle.ifBlank { "Verified Channel" }
                                }
                                val thumb = entryThumbnail.ifBlank {
                                    "https://i.ytimg.com/vi/$entryVideoId/hqdefault.jpg"
                                }
                                val videoLink = entryLink.ifBlank {
                                    "https://www.youtube.com/watch?v=$entryVideoId"
                                }
                                val effectiveDateIso = entryPublished.ifBlank { entryUpdated }
                                val epochMs = parseIsoDateEpoch(effectiveDateIso)
                                val displayDate = formatIsoDateShort(effectiveDateIso)

                                // Real duration ONLY from XML or previously player-verified cache (never invented!)
                                val playerCachedDuration = verifiedDurationLookup?.invoke(entryVideoId)
                                val resolvedDurationSec = entryDurationSeconds ?: playerCachedDuration
                                val isExplicitShortTag = isShortsLink || hasExplicitShortsHashtag(normalizedTitle, entryDescription)
                                val durationStatus = LectureDurationConfig.evaluateDuration(
                                    durationSeconds = resolvedDurationSec,
                                    isShortsUrl = isExplicitShortTag,
                                    minDurationSeconds = minDurationSeconds
                                )

                                val categories = classifyLectureCategories(
                                    title = normalizedTitle,
                                    description = entryDescription,
                                    channel = channel
                                )
                                val verifiedPodcastLang = verifyPodcastLanguageFromContent(
                                    title = normalizedTitle,
                                    channelName = resolvedChannelName
                                )
                                val detectedLangGroup = detectVideoLanguageGroup(
                                    title = normalizedTitle,
                                    description = entryDescription,
                                    channel = channel
                                )
                                val resolvedLangCode = when (detectedLangGroup) {
                                    LectureLanguageGroup.URDU_HINDI -> "ur"
                                    LectureLanguageGroup.ARABIC -> "ar"
                                    LectureLanguageGroup.ENGLISH -> "en"
                                    else -> when (verifiedPodcastLang) {
                                        PodcastLanguageSection.URDU -> "ur"
                                        PodcastLanguageSection.ARABIC -> "ar"
                                        PodcastLanguageSection.ENGLISH -> "en"
                                        PodcastLanguageSection.LANGUAGE_NOT_VERIFIED -> "und"
                                    }
                                }

                                items.add(
                                    YouTubeVideoItem(
                                        id = entryVideoId,
                                        title = normalizedTitle,
                                        channel = resolvedChannelName,
                                        channelId = resolvedChannelId,
                                        channelKey = channel?.key ?: "",
                                        thumbnail = thumb,
                                        publishedIso = entryPublished,
                                        updatedIso = entryUpdated,
                                        publishedDisplay = displayDate,
                                        publishedEpochMs = epochMs,
                                        videoUrl = videoLink,
                                        description = entryDescription,
                                        languageCode = resolvedLangCode,
                                        languageGroup = detectedLangGroup,
                                        verifiedPodcastLanguage = verifiedPodcastLang,
                                        categories = categories,
                                        durationSeconds = resolvedDurationSec,
                                        durationStatus = durationStatus,
                                        viewCount = entryViews,
                                        isFromCache = false,
                                        isCacheExpired = false
                                    )
                                )
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Atom feed XML parse error: ${e::class.java.simpleName}")
            return items.distinctBy { it.id }.sortedByDescending { it.publishedEpochMs }
        }

        return items.distinctBy { it.id }.sortedByDescending { it.publishedEpochMs }
    }

    private fun safeNextText(parser: XmlPullParser): String {
        return try {
            parser.nextText()?.trim().orEmpty()
        } catch (_: Exception) {
            ""
        }
    }

    private fun hasExplicitShortsHashtag(title: String, description: String): Boolean {
        val combined = "$title $description".lowercase(Locale.ROOT)
        return combined.contains("#shorts") ||
            combined.contains("#short ") ||
            combined.endsWith("#short") ||
            combined.contains("#reels") ||
            combined.contains("#youtubeshorts")
    }

    /**
     * Classifies actual spoken language of a video into [LectureLanguageGroup] using real
     * script and vocabulary signals from title/description (Devanagari Hindi, Urdu Nastaliq,
     * Roman Urdu/Hinglish, Arabic, English).
     *
     * Never misclassifies English videos as Urdu/Hindi or Arabic merely because of scholar nationality.
     * Falls back to channel default language ONLY when text is script-neutral (e.g. numerals/names)
     * and the channel has a verified primary language. Otherwise returns [LectureLanguageGroup.UNKNOWN].
     */
    fun detectVideoLanguageGroup(
        title: String,
        description: String = "",
        channel: VerifiedIslamicChannel? = null
    ): LectureLanguageGroup {
        val cleanTitle = title.trim()
        if (cleanTitle.isBlank()) return LectureLanguageGroup.UNKNOWN

        val devanagariCount = cleanTitle.count { it in '\u0900'..'\u097F' }
        if (devanagariCount >= 3) {
            return LectureLanguageGroup.URDU_HINDI
        }

        val urduOnlyChars = setOf('ٹ', 'ڈ', 'ڑ', 'ں', 'ے', 'ھ', 'پ', 'چ', 'ژ', 'گ', 'ک', 'ی', 'ہ')
        val strongUrduChars = setOf('ٹ', 'ڈ', 'ڑ', 'ں', 'ے', 'ھ', 'پ', 'چ', 'گ')
        val arabicDistinctChars = setOf('ة', 'ي', 'ك', 'أ', 'إ', 'ؤ', 'ئ', 'ى')

        val arabicScriptCount = cleanTitle.count { it in '\u0600'..'\u06FF' }
        val latinCount = cleanTitle.count { it in 'a'..'z' || it in 'A'..'Z' }

        if (arabicScriptCount >= 4) {
            val hasStrongUrdu = cleanTitle.any { it in strongUrduChars }
            val hasUrduChar = cleanTitle.any { it in urduOnlyChars }
            val hasArabicChar = cleanTitle.any { it in arabicDistinctChars }
            return when {
                hasStrongUrdu -> LectureLanguageGroup.URDU_HINDI
                hasUrduChar && !hasArabicChar -> LectureLanguageGroup.URDU_HINDI
                hasArabicChar -> LectureLanguageGroup.ARABIC
                channel?.primaryLanguageGroup == LectureLanguageGroup.URDU_HINDI -> LectureLanguageGroup.URDU_HINDI
                channel?.primaryLanguageGroup == LectureLanguageGroup.ARABIC -> LectureLanguageGroup.ARABIC
                arabicScriptCount >= 6 -> LectureLanguageGroup.ARABIC
                else -> LectureLanguageGroup.UNKNOWN
            }
        }

        if (latinCount >= 4) {
            val lower = cleanTitle.lowercase(Locale.ROOT)
            val tokens = lower.split(Regex("[^a-z0-9']+")).filter { it.isNotBlank() }.toSet()

            val romanUrduHindiMarkers = setOf(
                "ki", "ka", "ke", "ko", "se", "par", "pe", "mein", "main", "hai", "hain", "tha", "thi",
                "nahi", "nahin", "kya", "kyu", "kiu", "kyun", "kaise", "kaisa", "kro", "karo", "karen",
                "apni", "apna", "apne", "aap", "hum", "tum", "woh", "ye", "yeh", "bayan", "masail",
                "talaq", "hifazat", "sehat", "madad", "muhabbat", "mohabbat", "dosti", "zindagi",
                "waqia", "waqiat", "rula", "dene", "wala", "paak", "sabr", "namaz", "duniya", "akhirat",
                "qabar", "daawa", "ahl", "teen", "khatam", "aaj", "kal", "raaz", "dil", "sabak",
                "tarbiyat", "urdu", "hindi", "dars", "khatm", "aur", "liye", "baat", "khabar", "waqt",
                "khilafat", "ghazwa", "hazrat", "nabi", "sahaba", "qissa", "qasas", "parah", "para"
            )
            val englishMarkers = setOf(
                "the", "and", "for", "with", "from", "that", "this", "what", "why", "how", "when",
                "who", "where", "which", "are", "was", "were", "have", "has", "not", "you", "your",
                "about", "never", "ever", "there", "here", "must", "should", "would", "could",
                "into", "over", "under", "role", "miracle", "ep", "episode", "podcast", "muslim",
                "muslims", "islam", "islamic", "prophet", "prophets", "prayer", "sinning", "mother",
                "patience", "faith", "belief", "perfection", "mental", "depression", "revival",
                "people", "life", "best", "mind", "honor", "loss", "train", "good", "being", "real",
                "hardest", "days", "story", "stories", "battle", "companions", "caliphate", "history",
                "lecture", "full", "series", "part", "lesson", "understanding", "explained", "english"
            )

            // Explicit language tag inside title overrides ambiguity
            if (lower.contains("(urdu)") || lower.contains("[urdu]") || lower.contains("in urdu") ||
                lower.contains("(hindi)") || lower.contains("[hindi]") || lower.contains("in hindi")
            ) {
                return LectureLanguageGroup.URDU_HINDI
            }
            if (lower.contains("(english)") || lower.contains("[english]") || lower.contains("in english")) {
                return LectureLanguageGroup.ENGLISH
            }
            if (lower.contains("(arabic)") || lower.contains("[arabic]") || lower.contains("in arabic")) {
                return LectureLanguageGroup.ARABIC
            }

            val urHits = tokens.count { it in romanUrduHindiMarkers }
            val enHits = tokens.count { it in englishMarkers }

            return when {
                urHits >= 2 && urHits >= enHits -> LectureLanguageGroup.URDU_HINDI
                enHits >= 2 && urHits == 0 -> LectureLanguageGroup.ENGLISH
                urHits >= 1 && channel?.primaryLanguageGroup == LectureLanguageGroup.URDU_HINDI && enHits <= 1 ->
                    LectureLanguageGroup.URDU_HINDI
                enHits >= 1 && urHits == 0 && (channel?.primaryLanguageGroup == LectureLanguageGroup.ENGLISH || tokens.size >= 3) ->
                    LectureLanguageGroup.ENGLISH
                // If channel is single-language verified Urdu/Hindi and title uses neutral Arabic/Islamic terms
                // like "Surah Al-Baqarah | Dr Israr Ahmed" (no English grammar words), allow verified fallback
                urHits == 0 && enHits == 0 && channel != null && channel.secondaryLanguageGroups.isEmpty() ->
                    channel.primaryLanguageGroup
                else -> {
                    // Check description if available as secondary signal
                    if (description.isNotBlank()) {
                        val descDev = description.count { it in '\u0900'..'\u097F' }
                        val descUr = description.count { it in strongUrduChars }
                        val descAr = description.count { it in arabicDistinctChars }
                        when {
                            descDev >= 6 || descUr >= 4 -> LectureLanguageGroup.URDU_HINDI
                            descAr >= 6 && descUr == 0 -> LectureLanguageGroup.ARABIC
                            else -> LectureLanguageGroup.UNKNOWN
                        }
                    } else {
                        LectureLanguageGroup.UNKNOWN
                    }
                }
            }
        }

        return LectureLanguageGroup.UNKNOWN
    }

    /**
     * Legacy podcast language verification preserved for PodcastsScreen compatibility.
     */
    fun verifyPodcastLanguageFromContent(
        title: String,
        channelName: String = ""
    ): PodcastLanguageSection {
        val clean = title.trim()
        if (clean.isBlank()) return PodcastLanguageSection.LANGUAGE_NOT_VERIFIED

        val urduOnlyChars = setOf('ٹ', 'ڈ', 'ڑ', 'ں', 'ے', 'ھ', 'پ', 'چ', 'ژ', 'گ', 'ک', 'ی', 'ہ')
        val arabicDistinctChars = setOf('ة', 'ي', 'ك', 'أ', 'إ', 'ؤ', 'ئ', 'ى')

        val arabicScriptCount = clean.count { it in '\u0600'..'\u06FF' }
        val latinLetterCount = clean.count { it in 'a'..'z' || it in 'A'..'Z' }

        if (arabicScriptCount >= 4) {
            val hasUrduSpecific = clean.any { it in urduOnlyChars }
            val hasArabicDistinct = clean.any { it in arabicDistinctChars }
            return when {
                hasUrduSpecific && !hasArabicDistinct -> PodcastLanguageSection.URDU
                hasUrduSpecific && clean.any { it in setOf('ٹ', 'ڈ', 'ڑ', 'ں', 'ے', 'ھ', 'پ', 'چ', 'گ') } -> PodcastLanguageSection.URDU
                hasArabicDistinct || arabicScriptCount >= 6 -> PodcastLanguageSection.ARABIC
                else -> PodcastLanguageSection.LANGUAGE_NOT_VERIFIED
            }
        }

        if (latinLetterCount < 4) {
            return PodcastLanguageSection.LANGUAGE_NOT_VERIFIED
        }

        val tokens = clean.lowercase(Locale.ROOT)
            .split(Regex("[^a-z0-9']+"))
            .filter { it.isNotBlank() }
            .toSet()

        val romanUrduMarkers = setOf(
            "ki", "ka", "ke", "ko", "se", "par", "pe", "mein", "main", "hai", "hain", "tha", "thi",
            "nahi", "kya", "kyu", "kiu", "kyun", "kaise", "kaisa", "kro", "karo", "karen", "apni",
            "apna", "apne", "aap", "hum", "tum", "woh", "ye", "yeh", "bayan", "masail", "talaq",
            "hifazat", "sehat", "madad", "muhabbat", "mohabbat", "dosti", "zindagi", "waqia",
            "rula", "dene", "wala", "paak", "sabr", "namaz", "duniya", "akhirat", "qabar",
            "daawa", "ahl", "teen", "khatam", "aaj", "kal", "raaz", "dil", "sabak", "tarbiyat"
        )
        val urduHits = tokens.count { it in romanUrduMarkers }
        if (urduHits >= 2) {
            return PodcastLanguageSection.URDU
        }
        val lowerFull = "$clean $channelName".lowercase(Locale.ROOT)
        if (urduHits == 1 && (lowerFull.contains("bayan") || lowerFull.contains("masood") ||
                lowerFull.contains("jamil") || lowerFull.contains("qadri") || lowerFull.contains("israr") ||
                lowerFull.contains("urdu"))
        ) {
            return PodcastLanguageSection.URDU
        }

        val englishMarkers = setOf(
            "the", "and", "for", "with", "from", "that", "this", "what", "why", "how", "when",
            "who", "where", "which", "are", "was", "were", "have", "has", "not", "you", "your",
            "about", "never", "ever", "there", "here", "must", "should", "would", "could",
            "into", "over", "under", "role", "miracle", "ep", "episode", "podcast", "muslim",
            "muslims", "islam", "islamic", "prophet", "prayer", "sinning", "mother", "patience",
            "faith", "belief", "perfection", "mental", "depression", "revival", "people", "life",
            "best", "mind", "honor", "loss", "train", "good", "being", "real", "hardest", "days"
        )
        val englishHits = tokens.count { it in englishMarkers }
        if (englishHits >= 2 || (englishHits >= 1 && tokens.size >= 4 && urduHits == 0)) {
            return PodcastLanguageSection.ENGLISH
        }

        return PodcastLanguageSection.LANGUAGE_NOT_VERIFIED
    }

    /**
     * Classifies a video across all [LectureCategory] topics using BOTH title and RSS description,
     * applying each category's positive keywords AND exclusion rules.
     */
    fun classifyLectureCategories(
        title: String,
        description: String = "",
        channel: VerifiedIslamicChannel? = null
    ): List<LectureCategory> {
        val lowerTitle = title.lowercase(Locale.ROOT)
        val lowerDesc = description.lowercase(Locale.ROOT)
        val combined = if (lowerDesc.isBlank()) lowerTitle else "$lowerTitle\n$lowerDesc"

        val matched = LinkedHashSet<LectureCategory>()
        matched.add(LectureCategory.LATEST_LECTURES)

        if (channel != null && LectureCategory.POPULAR_SCHOLARS in channel.primaryCategories) {
            matched.add(LectureCategory.POPULAR_SCHOLARS)
        }

        // Dedicated single-domain educational channels (e.g. The Islamic History, Islamic History Podcast,
        // Omar of the Orient, Al Muqaddimah, The Seerah Timeline) always carry their primary topic
        // unless the entry triggers an exclusion keyword (e.g. #shorts or gaming).
        if (channel != null && channel.channelType == ChannelType.EDUCATIONAL) {
            val primaryTopic = channel.primaryCategories.firstOrNull {
                it != LectureCategory.LATEST_LECTURES && it != LectureCategory.POPULAR_SCHOLARS
            }
            if (primaryTopic != null && primaryTopic.exclusionKeywords.none { ex -> lowerTitle.contains(ex) }) {
                matched.add(primaryTopic)
            }
        }

        LectureCategory.entries.forEach { cat ->
            if (cat.matchKeywords.isNotEmpty()) {
                val excluded = cat.exclusionKeywords.any { ex -> lowerTitle.contains(ex) }
                if (!excluded && cat.matchKeywords.any { kw -> combined.contains(kw) }) {
                    matched.add(cat)
                }
            }
        }

        return matched.toList()
    }

    fun parseIsoDateEpoch(iso: String): Long {
        if (iso.isBlank()) return 0L
        val normalized = iso.replace("Z", "+0000")
            .replace(Regex("([+-]\\d\\d):(\\d\\d)$"), "$1$2")
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd"
        )
        for (pattern in patterns) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val parsed = sdf.parse(normalized)
                if (parsed != null) return parsed.time
            } catch (_: Exception) {
            }
        }
        return 0L
    }

    fun formatIsoDateShort(iso: String): String {
        if (iso.isBlank()) return ""
        return if (iso.length >= 10 && iso[4] == '-' && iso[7] == '-') {
            iso.substring(0, 10)
        } else {
            iso.take(16)
        }
    }
}
