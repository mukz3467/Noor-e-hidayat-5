package com.example.data.podcast

import com.example.data.lectures.LectureLanguageGroup
import com.example.data.lectures.YouTubeRssParser
import java.util.Locale

/**
 * Strict, non-bypassable filtering engine for the Noor-e-Hidayat Multilingual Podcast Library.
 *
 * Enforces official YouTube Shorts rules + conservative 4-minute minimum duration policy:
 * - Rejects explicit `/shorts/` URLs and `#shorts`/`#reels` tags.
 * - Rejects vertical or square videos (`height >= width`) of <= 3 minutes (180s) or unknown duration.
 * - Rejects videos of <= 3 minutes (180s) whenever orientation or Shorts status cannot be verified.
 * - Enforces conservative 4-minute (240s) minimum duration for Podcast content.
 * - Rejects short clips, trailers, promos, teasers, highlights, WhatsApp status clips, and snippets.
 * - Verifies scholar attribution (rejects wrong-scholar videos on hosted/shared channels).
 * - Verifies language accuracy (never mixes Urdu, English, and Arabic).
 * - Deduplicates strictly by canonical 11-character YouTube video ID.
 */
object PodcastLongFormFilter {

    const val YOUTUBE_SHORTS_MAX_SECONDS = 180
    const val DEFAULT_MIN_DURATION_SECONDS = 240

    private val DELETED_TITLES = setOf(
        "deleted video",
        "[deleted video]",
        "private video",
        "[private video]",
        "unavailable video"
    )

    private val SHORTS_HASHTAG_PATTERNS = listOf(
        "#shorts",
        "#short ",
        "#ytshorts",
        "#youtubeshorts",
        "#reels",
        "#reel ",
        "#tiktok",
        "youtube shorts"
    )

    private val CLIP_PROMO_TRAILER_PATTERNS = listOf(
        "short clip",
        "lecture clip",
        "podcast clip",
        "clips |",
        "| clip",
        "(clip)",
        "[clip]",
        " clip ",
        "trailer",
        "official trailer",
        "teaser",
        "promo",
        "promotional",
        "highlight",
        "highlights",
        "snippet",
        "intro snippet",
        "whatsapp status",
        "islamic status",
        "status video",
        "30 sec",
        "30 second",
        "60 sec",
        "60 second",
        "1 minute reminder",
        "1 min reminder",
        "2 min reminder",
        "short reminder",
        "مقطع قصير",
        "مقاطع قصيرة",
        "برومو",
        "تريلر",
        "تشويقي",
        "حالة واتس",
        "مختصر کلپ",
        "شارٹ کلپ",
        "کلپ ",
        "واٹس ایپ سٹیٹس"
    )

    private val LONG_FORM_PODCAST_OR_FULL_SESSION_MARKERS = listOf(
        "podcast", "episode", "ep.", "ep ", "interview", "discussion", "q&a", "qa ", "q & a",
        "full lecture", "complete", "full session", "full video", "full bayan", "full speech",
        "part ", "series", "session", "seminar", "dialogue", "conversation", "khutbah", "khutba",
        "jummah", "jumuah", "friday", "tafseer", "tafsir", "surah", "dars", "lecture", "class ",
        "lesson ", "majlis", "panel", "roundtable", "sit down", "live q&a", "ask ", "documentary",
        "history of", "seerah", "sirah", "seerat", "hadith", "hadeeth", "sunnah", "aqeedah",
        "fiqh", "sahih", "bukhari", "muslim", "ramadan", "islam", "allah", "prophet", "muhammad",
        "quran", "qur'an", "story of", "caliphate", "companions", "sahaba", "remembrance", "bayyinah",
        "بودكاست", "مقابلة", "لقاء", "حوار", "الحلقة", "حلقة", "المجلس", "مجلس", "خطبة", "دورة",
        "محاضرة", "درس ", "دروس", "شرح ", "تفسير", "سلسلة", "كاملة", "كامل", "فتاوى", "سؤال وجواب",
        "سورة", "حديث", "السيرة", "التاريخ", "القرآن", "رمضان", "العقيدة", "الفقه", "صحيح",
        "پوڈکاسٹ", "انٹرویو", "مکمل", "بیان", "تفسیر", "درس", "نشست", "مکالمہ", "سوال و جواب",
        "سوالات", "مسائل", "قسط", "حصہ", "تاریخ", "واقعہ", "خطاب", "سیرت", "قرآن", "حدیث",
        "صحابہ", "نبی", "نماز", "رمضان", "قیامت", "جنت", "توبہ", "اصلاح", "bayan", "waqia", "masail"
    )

    private val PODCAST_EPISODE_KEYWORDS = listOf(
        "podcast", "episode", "ep.", "ep ", "season ", "بودكاست", "الحلقة", "حلقة", "پوڈکاسٹ", "قسط"
    )

    private val INTERVIEW_KEYWORDS = listOf(
        "interview", "in conversation", "sit down", "hosted by", "guest", "exclusive interview",
        "مقابلة", "لقاء", "حوار مع", "انٹرویو", "خصوصی نشست", "گفتگو"
    )

    private val DISCUSSION_KEYWORDS = listOf(
        "discussion", "dialogue", "debate", "panel", "roundtable", "talk", "conversation",
        "مناقشة", "حوار", "ندوة", "مکالمہ", "مباحثہ", "نشست"
    )

    private val QA_KEYWORDS = listOf(
        "q&a", "q & a", "qa ", "questions", "question", "ask ", "fatwa", "fatawa", "masail",
        "ruling", "halal", "haram", "فتوى", "فتاوى", "سؤال", "جواب", "هل ", "ما حكم", "مسائل", "مسئلہ", "سوالات"
    )

    private val QURAN_TAFSEER_KEYWORDS = listOf(
        "quran", "qur'an", "tafseer", "tafsir", "surah", "ayah", "tadabbur", "bayan ul quran",
        "قرآن", "القرآن", "تفسير", "تفسیر", "سورة", "آية", "تدبر"
    )

    private val HADITH_SUNNAH_KEYWORDS = listOf(
        "hadith", "hadeeth", "sunnah", "bukhari", "muslim", "tirmidhi", "nawawi", "riyad",
        "حديث", "حدیث", "السنة", "سنت", "صحيح", "صحیح", "بخاري", "بخاری"
    )

    private val HISTORY_SEERAH_KEYWORDS = listOf(
        "seerah", "sirah", "seerat", "history", "caliphate", "khilafat", "ottoman", "andalus",
        "sahaba", "companions", "prophet muhammad", "battle", "ghazwa", "tareekh", "tarikh",
        "سيرة", "السيرة", "سیرت", "تاريخ", "تاریخ", "صحابة", "صحابہ", "خلافة", "خلافت", "غزوة", "غزوہ"
    )

    private val LIFESTYLE_KEYWORDS = listOf(
        "lifestyle", "daily life", "habit", "habits", "modern", "work", "wealth", "finance",
        "business", "career", "productivity", "time management", "mental health", "stress",
        "حياة المسلم", "الحياة", "أسلوب حياة", "معاملات", "زندگی", "معاشرت", "روزمرہ", "کامیابی"
    )

    private val YOUTH_FAMILY_KEYWORDS = listOf(
        "youth", "family", "marriage", "parents", "mother", "father", "children", "parenting",
        "husband", "wife", "nikah", "divorce", "talaq", "generation", "teen", "students",
        "الشباب", "الأسرة", "الزواج", "الوالدين", "التربية", "نوجوان", "خاندان", "والدین", "شادی", "نکاح", "تربیت"
    )

    private val REPENTANCE_SELF_KEYWORDS = listOf(
        "repentance", "tawbah", "taubah", "forgiveness", "sin", "sins", "heart", "soul",
        "tazkiyah", "purification", "self-improvement", "patience", "sabr", "ikhlas", "sincerity",
        "anxiety", "depression", "healing", "return to allah", "dua", "istighfar",
        "توبة", "التوبة", "استغفار", "تزكية", "إصلاح النفس", "القلب", "القلوب", "صبر",
        "توبہ", "استغفار", "اصلاح", "تزکیہ", "نفس", "دل", "صبر", "گناہ", "معافی"
    )

    fun evaluate(
        candidate: PodcastCandidateVideo,
        profile: PodcastScholarProfile
    ): PodcastFilterDecision {
        // 1. Validate video ID and YouTube URL
        if (!YouTubeRssParser.isValidVideoId(candidate.videoId) || !isValidYouTubeWatchUrl(candidate.videoUrl)) {
            return PodcastFilterDecision(false, PodcastRejectionReason.INVALID_VIDEO_ID_OR_URL)
        }

        // 2. Reject explicit /shorts/ URL immediately
        if (candidate.videoUrl.contains("/shorts/", ignoreCase = true)) {
            return PodcastFilterDecision(false, PodcastRejectionReason.EXPLICIT_SHORTS_URL)
        }

        // 3. Validate non-blank, non-deleted title
        val title = candidate.title.trim()
        val lowerTitle = title.lowercase(Locale.ROOT)
        if (title.isBlank() || lowerTitle in DELETED_TITLES) {
            return PodcastFilterDecision(false, PodcastRejectionReason.MISSING_TITLE_OR_DELETED)
        }

        val combinedLower = "$lowerTitle ${candidate.description.lowercase(Locale.ROOT)}"

        // 4. Reject explicit #shorts / #reels hashtags
        if (SHORTS_HASHTAG_PATTERNS.any { combinedLower.contains(it) } ||
            lowerTitle.endsWith("#short") ||
            lowerTitle.endsWith("#reel")
        ) {
            return PodcastFilterDecision(false, PodcastRejectionReason.SHORTS_HASHTAG_OR_REEL)
        }

        // 5. Reject vertical or square Shorts (official YouTube Shorts rule: vertical/square up to 3 min)
        val isVerticalOrSquare = candidate.videoWidth != null &&
            candidate.videoHeight != null &&
            candidate.videoWidth > 0 &&
            candidate.videoHeight > 0 &&
            candidate.videoHeight >= candidate.videoWidth

        if (isVerticalOrSquare) {
            if (candidate.durationSeconds == null || candidate.durationSeconds <= YOUTUBE_SHORTS_MAX_SECONDS) {
                return PodcastFilterDecision(false, PodcastRejectionReason.VERTICAL_OR_SQUARE_SHORT)
            }
        }

        // 6. Duration rules:
        //    - Any video <= 3 minutes (180s) whose orientation/Shorts status cannot be verified -> REJECT
        //    - Conservative Podcast minimum = 4 minutes (240s) -> REJECT anything < 240s
        val dur = candidate.durationSeconds
        if (dur != null) {
            if (dur <= YOUTUBE_SHORTS_MAX_SECONDS) {
                return PodcastFilterDecision(
                    false,
                    if (candidate.videoWidth == null || candidate.videoHeight == null) {
                        PodcastRejectionReason.UNVERIFIED_SHORT_DURATION
                    } else {
                        PodcastRejectionReason.UNDER_MIN_DURATION
                    }
                )
            }
            if (dur < DEFAULT_MIN_DURATION_SECONDS) {
                return PodcastFilterDecision(false, PodcastRejectionReason.UNDER_MIN_DURATION)
            }
        }

        // 7. Reject clips, trailers, promos, teasers, highlights, snippets
        if (isClipTrailerOrPromo(lowerTitle)) {
            return PodcastFilterDecision(false, PodcastRejectionReason.CLIP_TRAILER_OR_PROMO)
        }

        // 8. If duration is unavailable (plain Atom RSS without duration metadata), require clear
        //    long-form session/podcast/interview/discussion/full-lecture evidence — never blindly accept
        //    short or ambiguous unlabeled uploads!
        if (dur == null) {
            val hasLongFormEvidence = LONG_FORM_PODCAST_OR_FULL_SESSION_MARKERS.any { combinedLower.contains(it) }
            if (!hasLongFormEvidence) {
                return PodcastFilterDecision(false, PodcastRejectionReason.UNVERIFIED_LONG_FORM_METADATA)
            }
        }

        // 9. Verify scholar attribution (mandatory on hosted/shared channels AND catches mismatched scholars)
        if (!matchesScholarAttribution(candidate, profile)) {
            return PodcastFilterDecision(false, PodcastRejectionReason.WRONG_SCHOLAR)
        }

        // 10. Strict language isolation (Urdu vs English vs Arabic — never cross-mix!)
        if (!matchesExpectedLanguage(candidate, profile.language)) {
            return PodcastFilterDecision(false, PodcastRejectionReason.WRONG_LANGUAGE)
        }

        val contentType = classifyContentType(lowerTitle, combinedLower, candidate.sourceKind)
        val categories = classifyCategories(lowerTitle, combinedLower, contentType, profile)

        return PodcastFilterDecision(
            accepted = true,
            rejectionReason = null,
            contentType = contentType,
            categories = categories
        )
    }

    /**
     * Filters and deduplicates a batch of candidate videos, returning both verified items and
     * exact rejection diagnostics.
     */
    fun filterAndDeduplicate(
        candidates: List<PodcastCandidateVideo>,
        profile: PodcastScholarProfile,
        existingIds: Set<String> = emptySet()
    ): Pair<List<VerifiedPodcastVideo>, Map<PodcastRejectionReason, Int>> {
        val seenIds = HashSet<String>(existingIds)
        val accepted = mutableListOf<VerifiedPodcastVideo>()
        val rejections = LinkedHashMap<PodcastRejectionReason, Int>()

        for (candidate in candidates) {
            if (!seenIds.add(candidate.videoId)) {
                rejections[PodcastRejectionReason.DUPLICATE_VIDEO] =
                    (rejections[PodcastRejectionReason.DUPLICATE_VIDEO] ?: 0) + 1
                continue
            }
            val decision = evaluate(candidate, profile)
            if (!decision.accepted) {
                val reason = decision.rejectionReason ?: PodcastRejectionReason.UNVERIFIED_LONG_FORM_METADATA
                rejections[reason] = (rejections[reason] ?: 0) + 1
                continue
            }
            val thumb = candidate.thumbnailUrl.ifBlank {
                "https://i.ytimg.com/vi/${candidate.videoId}/hqdefault.jpg"
            }
            accepted.add(
                VerifiedPodcastVideo(
                    videoId = candidate.videoId,
                    title = candidate.title.trim(),
                    description = candidate.description.trim(),
                    scholarId = profile.id,
                    scholarName = profile.displayNameNative,
                    channelId = candidate.channelId,
                    channelName = candidate.channelName,
                    thumbnailUrl = thumb,
                    videoUrl = candidate.videoUrl.ifBlank { "https://www.youtube.com/watch?v=${candidate.videoId}" },
                    publishedIso = candidate.publishedIso,
                    publishedDisplay = candidate.publishedDisplay,
                    publishedEpochMs = candidate.publishedEpochMs,
                    durationSeconds = candidate.durationSeconds,
                    viewCount = candidate.viewCount,
                    language = profile.language,
                    contentType = decision.contentType ?: PodcastContentType.LONG_FORM_DISCUSSION,
                    sourceKind = candidate.sourceKind,
                    categories = decision.categories
                )
            )
        }
        return accepted.sortedByDescending { it.publishedEpochMs } to rejections
    }

    /**
     * Filters and deduplicates a batch of candidates collected across multiple verified channels
     * belonging to a single [PodcastLanguage] (for the 100+ long-form language suggestions feed).
     * Enforces the exact same non-bypassable Shorts, duration, clip, and strict language checks.
     */
    fun filterCatalogBatch(
        candidatesWithChannel: List<Pair<PodcastCandidateVideo, PodcastCatalogChannelEntry>>,
        language: PodcastLanguage,
        existingIds: Set<String> = emptySet()
    ): Pair<List<VerifiedPodcastVideo>, Map<PodcastRejectionReason, Int>> {
        val seenIds = HashSet<String>(existingIds)
        val accepted = mutableListOf<VerifiedPodcastVideo>()
        val rejections = LinkedHashMap<PodcastRejectionReason, Int>()

        for ((candidate, entry) in candidatesWithChannel) {
            if (!seenIds.add(candidate.videoId)) {
                rejections[PodcastRejectionReason.DUPLICATE_VIDEO] =
                    (rejections[PodcastRejectionReason.DUPLICATE_VIDEO] ?: 0) + 1
                continue
            }
            val syntheticProfile = PodcastScholarRegistry.findById(entry.scholarId) ?: entry.toProfile()
            val decision = evaluate(candidate, syntheticProfile)
            if (!decision.accepted) {
                val reason = decision.rejectionReason ?: PodcastRejectionReason.UNVERIFIED_LONG_FORM_METADATA
                rejections[reason] = (rejections[reason] ?: 0) + 1
                continue
            }
            val thumb = candidate.thumbnailUrl.ifBlank {
                "https://i.ytimg.com/vi/${candidate.videoId}/hqdefault.jpg"
            }
            accepted.add(
                VerifiedPodcastVideo(
                    videoId = candidate.videoId,
                    title = candidate.title.trim(),
                    description = candidate.description.trim(),
                    scholarId = entry.scholarId,
                    scholarName = entry.scholarName,
                    channelId = candidate.channelId,
                    channelName = candidate.channelName.ifBlank { entry.nameEn },
                    thumbnailUrl = thumb,
                    videoUrl = candidate.videoUrl.ifBlank { "https://www.youtube.com/watch?v=${candidate.videoId}" },
                    publishedIso = candidate.publishedIso,
                    publishedDisplay = candidate.publishedDisplay,
                    publishedEpochMs = candidate.publishedEpochMs,
                    durationSeconds = candidate.durationSeconds,
                    viewCount = candidate.viewCount,
                    language = language,
                    contentType = decision.contentType ?: PodcastContentType.LONG_FORM_DISCUSSION,
                    sourceKind = candidate.sourceKind,
                    categories = decision.categories
                )
            )
        }
        return accepted.sortedByDescending { it.publishedEpochMs } to rejections
    }

    fun isValidYouTubeWatchUrl(url: String): Boolean {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return false
        if (!trimmed.startsWith("https://", ignoreCase = true) && !trimmed.startsWith("http://", ignoreCase = true)) {
            return false
        }
        val lower = trimmed.lowercase(Locale.ROOT)
        return lower.contains("youtube.com/") || lower.contains("youtu.be/")
    }

    private fun isClipTrailerOrPromo(lowerTitle: String): Boolean {
        if (CLIP_PROMO_TRAILER_PATTERNS.any { lowerTitle.contains(it) }) return true
        // Explicit "clip" standalone word (e.g. "Amazing Clip - ...")
        if (Regex("""\bclips?\b""").containsMatchIn(lowerTitle)) return true
        return false
    }

    internal fun matchesScholarAttribution(
        candidate: PodcastCandidateVideo,
        profile: PodcastScholarProfile
    ): Boolean {
        if (profile.profileType == PodcastProfileType.CHANNEL) {
            return profile.sources.any { it.channelId.equals(candidate.channelId, ignoreCase = true) }
        }
        val isVerifiedSourceForProfile = profile.sources.any {
            it.channelId.equals(candidate.channelId, ignoreCase = true)
        }
        val textBlob = "${candidate.title} ${candidate.description} ${candidate.channelName}".lowercase(Locale.ROOT)
        val hasAliasMatch = profile.attributionAliases.any { alias ->
            alias.isNotBlank() && textBlob.contains(alias.lowercase(Locale.ROOT))
        }

        // Hosted / shared institutional channels MUST explicitly name the scholar in title/description
        if (candidate.requiresScholarAttribution || candidate.sourceKind == PodcastSourceKind.HOSTED_INTERVIEW) {
            val titleDescBlob = "${candidate.title} ${candidate.description}".lowercase(Locale.ROOT)
            val channelMatchesAlias = profile.attributionAliases.any {
                it.isNotBlank() && candidate.channelName.lowercase(Locale.ROOT).contains(it.lowercase(Locale.ROOT))
            }
            val titleDescMatchesAlias = profile.attributionAliases.any {
                it.isNotBlank() && titleDescBlob.contains(it.lowercase(Locale.ROOT))
            }
            return titleDescMatchesAlias || (isVerifiedSourceForProfile && channelMatchesAlias)
        }

        // Official personal channel uploads belong to the scholar if the channelId matches one of their
        // verified official channels, OR if the scholar alias appears explicitly.
        return isVerifiedSourceForProfile || hasAliasMatch
    }

    internal fun matchesExpectedLanguage(
        candidate: PodcastCandidateVideo,
        expected: PodcastLanguage
    ): Boolean {
        val detected = YouTubeRssParser.detectVideoLanguageGroup(
            title = candidate.title,
            description = candidate.description,
            channel = null
        )
        return when (expected) {
            PodcastLanguage.URDU -> detected == LectureLanguageGroup.URDU_HINDI
            PodcastLanguage.ENGLISH -> detected == LectureLanguageGroup.ENGLISH
            PodcastLanguage.ARABIC -> detected == LectureLanguageGroup.ARABIC
        }
    }

    internal fun classifyContentType(
        lowerTitle: String,
        combinedLower: String,
        sourceKind: PodcastSourceKind
    ): PodcastContentType {
        return when {
            PODCAST_EPISODE_KEYWORDS.any { lowerTitle.contains(it) } ->
                PodcastContentType.COMPLETE_PODCAST_EPISODE
            INTERVIEW_KEYWORDS.any { lowerTitle.contains(it) } ||
                (sourceKind == PodcastSourceKind.HOSTED_INTERVIEW && DISCUSSION_KEYWORDS.any { combinedLower.contains(it) }) ->
                PodcastContentType.FULL_INTERVIEW
            QA_KEYWORDS.any { lowerTitle.contains(it) } ->
                PodcastContentType.ISLAMIC_QA
            DISCUSSION_KEYWORDS.any { lowerTitle.contains(it) } ->
                PodcastContentType.LONG_FORM_DISCUSSION
            QURAN_TAFSEER_KEYWORDS.any { lowerTitle.contains(it) } ||
                HADITH_SUNNAH_KEYWORDS.any { lowerTitle.contains(it) } ||
                HISTORY_SEERAH_KEYWORDS.any { lowerTitle.contains(it) } ->
                PodcastContentType.EDUCATIONAL_DISCUSSION
            else -> PodcastContentType.FULL_LECTURE
        }
    }

    internal fun classifyCategories(
        lowerTitle: String,
        combinedLower: String,
        contentType: PodcastContentType,
        profile: PodcastScholarProfile
    ): List<PodcastProfileCategory> {
        val cats = LinkedHashSet<PodcastProfileCategory>()
        cats.add(PodcastProfileCategory.ALL_ELIGIBLE)
        cats.add(PodcastProfileCategory.LATEST_LONG_FORM)

        when (contentType) {
            PodcastContentType.COMPLETE_PODCAST_EPISODE -> cats.add(PodcastProfileCategory.COMPLETE_PODCAST_EPISODES)
            PodcastContentType.FULL_INTERVIEW -> cats.add(PodcastProfileCategory.FULL_INTERVIEWS)
            PodcastContentType.LONG_FORM_DISCUSSION -> cats.add(PodcastProfileCategory.LONG_FORM_DISCUSSIONS)
            PodcastContentType.ISLAMIC_QA -> cats.add(PodcastProfileCategory.ISLAMIC_QA)
            else -> Unit
        }

        if (PODCAST_EPISODE_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.COMPLETE_PODCAST_EPISODES)
        }
        if (INTERVIEW_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.FULL_INTERVIEWS)
        }
        if (DISCUSSION_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.LONG_FORM_DISCUSSIONS)
        }
        if (QA_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.ISLAMIC_QA)
        }
        if (QURAN_TAFSEER_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.QURAN_TAFSEER)
        }
        if (HADITH_SUNNAH_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.HADITH_SUNNAH)
        }
        if (profile.profileType == PodcastProfileType.CHANNEL ||
            HISTORY_SEERAH_KEYWORDS.any { combinedLower.contains(it) }
        ) {
            cats.add(PodcastProfileCategory.ISLAMIC_HISTORY_SEERAH)
        }
        if (LIFESTYLE_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.ISLAMIC_LIFESTYLE)
        }
        if (YOUTH_FAMILY_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.YOUTH_FAMILY)
        }
        if (REPENTANCE_SELF_KEYWORDS.any { combinedLower.contains(it) }) {
            cats.add(PodcastProfileCategory.REPENTANCE_SELF_IMPROVEMENT)
        }

        return cats.toList()
    }

    /**
     * Parses YouTube Data API v3 ISO-8601 duration strings such as `PT1H23M45S`, `PT45M`, `PT3M12S`, `PT58S`.
     */
    fun parseIso8601DurationSeconds(iso: String?): Int? {
        if (iso.isNullOrBlank()) return null
        val match = Regex("""^PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?$""").matchEntire(iso.trim().uppercase(Locale.US))
            ?: return null
        val hours = match.groupValues[1].toIntOrNull() ?: 0
        val minutes = match.groupValues[2].toIntOrNull() ?: 0
        val seconds = match.groupValues[3].toIntOrNull() ?: 0
        val total = hours * 3600 + minutes * 60 + seconds
        return if (total > 0) total else null
    }

    fun formatDuration(durationSeconds: Int?): String {
        if (durationSeconds == null || durationSeconds <= 0) return ""
        val h = durationSeconds / 3600
        val m = (durationSeconds % 3600) / 60
        val s = durationSeconds % 60
        return if (h > 0) {
            String.format(Locale.US, "%d:%02d:%02d", h, m, s)
        } else {
            String.format(Locale.US, "%d:%02d", m, s)
        }
    }
}
