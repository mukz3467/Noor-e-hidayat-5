package com.example.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.data.model.AppLanguage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

enum class LectureCategory(
    val id: String,
    val labelKey: String,
    val matchKeywords: List<String>
) {
    POPULAR_SCHOLARS(
        id = "popular_scholars",
        labelKey = "lecture_cat_popular_scholars",
        matchKeywords = emptyList()
    ),
    LATEST_LECTURES(
        id = "latest_lectures",
        labelKey = "lecture_cat_latest",
        matchKeywords = emptyList()
    ),
    QURAN_TAFSEER(
        id = "quran_tafseer",
        labelKey = "lecture_cat_tafseer",
        matchKeywords = listOf(
            "tafseer", "tafsir", "surah", "sura", "ayat", "ayah", "deeper look",
            "تفسير", "تفسیر", "سورة", "قرآن", "القرآن", "quran", "qur'an"
        )
    ),
    QURAN_EXPLANATION(
        id = "quran_explanation",
        labelKey = "lecture_cat_quran_explanation",
        matchKeywords = listOf(
            "tadabbur", "reflection", "reflections", "story", "stories", "meaning",
            "message", "quran", "qur'an", "surah", "sura", "فہم", "تدبر", "قرآن", "تلاوة"
        )
    ),
    HADITH(
        id = "hadith",
        labelKey = "lecture_cat_hadith",
        matchKeywords = listOf(
            "hadith", "hadeeth", "bukhari", "muslim", "sunnah", "narrator", "narrators",
            "صحيح", "حديث", "حدیث", "سنت", "السنة"
        )
    ),
    SEERAH(
        id = "seerah",
        labelKey = "lecture_cat_seerah",
        matchKeywords = listOf(
            "seerah", "sirah", "prophet", "muhammad", "makkah", "madinah", "medina",
            "hijrah", "firsts", "companion", "companions", "سيرة", "سیرت", "نبی", "الرسول"
        )
    ),
    ISLAMIC_HISTORY(
        id = "islamic_history",
        labelKey = "lecture_cat_history",
        matchKeywords = listOf(
            "history", "companions", "sahaba", "ummah", "khilafah", "jerusalem",
            "aqsa", "firsts", "medina", "uzbekistan", "تاريخ", "تاریخ", "صحابہ"
        )
    ),
    SALAH_WORSHIP(
        id = "salah_worship",
        labelKey = "lecture_cat_salah",
        matchKeywords = listOf(
            "salah", "salat", "prayer", "pray", "namaz", "wudu", "fasting", "ramadan",
            "zakat", "hajj", "dua", "dhikr", "worship", "humility", "صلاة", "نماز",
            "عبادت", "دعاء", "وضوء", "العشاء", "المغرب", "الفجر"
        )
    ),
    ISLAMIC_MANNERS(
        id = "islamic_manners",
        labelKey = "lecture_cat_manners",
        matchKeywords = listOf(
            "akhlaq", "adab", "manners", "character", "patience", "sabr", "mercy",
            "forgiveness", "heart", "soul", "sinning", "humility", "mind", "charity",
            "sadness", "اخلاق", "أخلاق", "صبر", "توبہ", "قلب", "تواضع"
        )
    ),
    FAMILY_SOCIETY(
        id = "family_society",
        labelKey = "lecture_cat_family",
        matchKeywords = listOf(
            "family", "marriage", "parents", "mother", "father", "children", "child",
            "youth", "community", "wife", "husband", "nikah", "talaq", "نکاح", "طلاق",
            "والدین", "خاندان", "الأسرة", "الشباب"
        )
    ),
    NEW_MUSLIM_EDUCATION(
        id = "new_muslim_education",
        labelKey = "lecture_cat_new_muslim",
        matchKeywords = listOf(
            "revert", "convert", "new muslim", "basics", "pillar", "pillars",
            "islam", "faith", "aqeedah", "shahada", "muslim", "allah", "belief",
            "عقيدة", "ایمان", "اسلام", "الإيمان"
        )
    ),
    ISLAMIC_QA(
        id = "islamic_qa",
        labelKey = "lecture_cat_qa",
        matchKeywords = listOf(
            "q&a", "question", "questions", "ask", "fatwa", "ruling", "halal",
            "haram", "permissible", "masail", "must i", "is it", "is there",
            "هل ", "مسئلہ", "مسائل", "سوال", "جواب", "فتوى", "askzad"
        )
    )
}

enum class PodcastLanguageSection(val id: String, val labelKey: String) {
    URDU("ur", "pod_lang_urdu"),
    ARABIC("ar", "pod_lang_arabic"),
    ENGLISH("en", "pod_lang_english"),
    LANGUAGE_NOT_VERIFIED("unverified", "pod_lang_not_verified")
}

enum class PodcastTopicFilter(
    val id: String,
    val labelKey: String,
    val matchKeywords: List<String>
) {
    ALL_TOPICS("all", "pod_topic_all", emptyList()),
    QURAN_TAFSEER(
        "quran",
        "pod_topic_quran",
        listOf("quran", "qur'an", "tafsir", "tafseer", "surah", "ayah", "tadabbur", "قرآن", "تفسير", "تفسیر", "سورة")
    ),
    SEERAH_HISTORY(
        "seerah",
        "pod_topic_seerah",
        listOf("seerah", "sirah", "prophet", "muhammad", "companion", "sahaba", "history", "firsts", "سیرت", "سيرة", "تاريخ")
    ),
    SPIRITUAL_REMINDERS(
        "spiritual",
        "pod_topic_spiritual",
        listOf("heart", "soul", "faith", "dua", "patience", "sabr", "mercy", "sin", "sinning", "love", "allah", "قلب", "الله", "ربك", "تزکیہ", "محبت", "دعاء")
    ),
    FAMILY_YOUTH(
        "family",
        "pod_topic_family",
        listOf("family", "youth", "parents", "mother", "marriage", "nikah", "talaq", "children", "نکاح", "طلاق", "والدین", "خاندان", "الأسرة", "نوجوان")
    ),
    ISLAMIC_QA(
        "qa",
        "pod_topic_qa",
        listOf("q&a", "masail", "fatwa", "question", "ruling", "halal", "haram", "hadith", "sunnah", "هل ", "مسائل", "حديث", "صحيح مسلم", "فتوى")
    )
}

enum class HarameynLocation {
    MAKKAH,
    MADINAH
}

enum class LiveBroadcastState {
    LOADING,
    LIVE_CONFIRMED,
    LIVE_STATUS_UNCONFIRMED,
    NO_ACTIVE_BROADCAST,
    CURRENTLY_UNAVAILABLE,
    OFFLINE
}

enum class FeedLoadStatus {
    IDLE,
    ONLINE_FRESH,
    CACHED_FRESH,
    OFFLINE_CACHE,
    EXPIRED_CACHE_FALLBACK,
    INVALID_MAPPING,
    ERROR_NO_CACHE
}

data class VerifiedIslamicChannel(
    val key: String,
    val channelId: String,
    val handle: String,
    val nameEn: String,
    val nameUr: String,
    val nameAr: String,
    val nameHi: String,
    val specialtyEn: String,
    val specialtyUr: String,
    val specialtyAr: String,
    val specialtyHi: String,
    val languageCode: String,
    val primaryCategories: List<LectureCategory>,
    val podcastLanguage: PodcastLanguageSection? = null,
    val podcastTopics: List<PodcastTopicFilter> = emptyList()
) {
    val feedUrl: String
        get() = "https://www.youtube.com/feeds/videos.xml?channel_id=$channelId"

    val officialChannelUrl: String
        get() = "https://www.youtube.com/channel/$channelId"

    fun localizedName(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> nameEn
        AppLanguage.URDU -> nameUr
        AppLanguage.ARABIC -> nameAr
        AppLanguage.HINDI -> nameHi
    }

    fun localizedSpecialty(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> specialtyEn
        AppLanguage.URDU -> specialtyUr
        AppLanguage.ARABIC -> specialtyAr
        AppLanguage.HINDI -> specialtyHi
    }
}

data class HarameynStreamChannel(
    val location: HarameynLocation,
    val channelId: String,
    val handle: String,
    val nameEn: String,
    val nameUr: String,
    val nameAr: String,
    val nameHi: String
) {
    val officialChannelUrl: String
        get() = "https://www.youtube.com/channel/$channelId"

    val feedUrl: String
        get() = "https://www.youtube.com/feeds/videos.xml?channel_id=$channelId"

    fun localizedName(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> nameEn
        AppLanguage.URDU -> nameUr
        AppLanguage.ARABIC -> nameAr
        AppLanguage.HINDI -> nameHi
    }
}

data class YouTubeVideoItem(
    val id: String,
    val title: String,
    val channel: String,
    val channelId: String = "",
    val channelKey: String = "",
    val thumbnail: String,
    val publishedIso: String = "",
    val publishedDisplay: String = "",
    val publishedEpochMs: Long = 0L,
    val videoUrl: String = "https://www.youtube.com/watch?v=$id",
    val languageCode: String = "en",
    val verifiedPodcastLanguage: PodcastLanguageSection = PodcastLanguageSection.LANGUAGE_NOT_VERIFIED,
    val categories: List<LectureCategory> = listOf(LectureCategory.LATEST_LECTURES),
    val isFromCache: Boolean = false,
    val isCacheExpired: Boolean = false
)

data class LectureFeedResult(
    val videos: List<YouTubeVideoItem>,
    val status: FeedLoadStatus,
    val unavailableChannels: List<String> = emptyList(),
    val lastUpdatedMs: Long = 0L,
    val isCacheExpired: Boolean = false,
    val errorMessage: String? = null
)

data class LiveStreamStatusResult(
    val location: HarameynLocation,
    val channel: HarameynStreamChannel,
    val state: LiveBroadcastState,
    val verifiedVideoId: String? = null,
    val streamTitle: String = "",
    val publishedDisplay: String = "",
    val watchOnYouTubeUrl: String = channel.officialChannelUrl,
    val isEmbeddableConfirmed: Boolean = false,
    val recentRecordedUploads: List<YouTubeVideoItem> = emptyList(),
    val statusDetail: String = "",
    val checkedAtMs: Long = System.currentTimeMillis()
)

/**
 * Zero-API-key YouTube repository backed strictly by official public YouTube Atom RSS feeds:
 * https://www.youtube.com/feeds/videos.xml?channel_id=CHANNEL_ID
 *
 * - Zero API keys, zero OAuth, zero HTML scraping, zero unofficial endpoints.
 * - Topic-based fetching for Lectures (fetches ONLY mapped channels for selected topic).
 * - Separated Urdu / Arabic / English YouTube RSS podcasts with content-based language
 *   verification and explicit "Language Not Verified" categorization.
 * - Honest live broadcast handling for Makkah & Madinah (never fakes LIVE status or
 *   substitutes recorded uploads as active live broadcasts).
 */
object YouTubeRepository {

    private const val TAG = "YouTubeRepository"
    private const val PREFS_NAME = "noor_youtube_feeds_v2"
    private const val KEY_CHANNEL_CACHE_PREFIX = "rss_cache_"
    private const val KEY_CHANNEL_TS_PREFIX = "rss_ts_"
    private const val KEY_FAVORITES_JSON = "lecture_favorites_v2"
    private const val KEY_RECENT_WATCHED_JSON = "lecture_recent_v2"
    private const val KEY_PODCAST_FAVORITES_JSON = "yt_podcast_favorites_v1"
    private const val KEY_PODCAST_RECENT_JSON = "yt_podcast_recent_v1"
    internal const val CACHE_TTL_MS = 30 * 60 * 1000L
    private const val MAX_RECENT_WATCHED = 25

    private val CHANNEL_ID_REGEX = Regex("^UC[A-Za-z0-9_-]{22}$")
    private val VIDEO_ID_REGEX = Regex("^[A-Za-z0-9_-]{11}$")

    internal var httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    internal var networkChecker: ((Context) -> Boolean)? = null
    internal var currentTimeProvider: (() -> Long)? = null

    private fun nowMs(): Long = currentTimeProvider?.invoke() ?: System.currentTimeMillis()

    private val memoryCache = ConcurrentHashMap<String, Pair<Long, List<YouTubeVideoItem>>>()
    private val inFlightRequests = ConcurrentHashMap<String, Deferred<ChannelFetchOutcome>>()

    /**
     * Verified public YouTube channels whose UC channel IDs and official Atom RSS feeds
     * (https://www.youtube.com/feeds/videos.xml?channel_id=...) have been confirmed live.
     */
    val VERIFIED_CHANNELS: List<VerifiedIslamicChannel> = listOf(
        VerifiedIslamicChannel(
            key = "bayyinah",
            channelId = "UCRtiU-lpcBSi-ipFKyfIkug",
            handle = "@bayyinah",
            nameEn = "Nouman Ali Khan (Bayyinah)",
            nameUr = "نعمان علی خان (بینہ انسٹیٹیوٹ)",
            nameAr = "نعمان علي خان (مؤسسة بينة)",
            nameHi = "नोमान अली ख़ान (बय्यिनाह)",
            specialtyEn = "Quran Tafseer & Deep Linguistic Explanation",
            specialtyUr = "تفسیر اور فہمِ قرآن",
            specialtyAr = "التفسير وتدبر القرآن",
            specialtyHi = "क़ुरआन तफ़सीर और गहन व्याख्या",
            languageCode = "en",
            primaryCategories = listOf(
                LectureCategory.POPULAR_SCHOLARS,
                LectureCategory.QURAN_TAFSEER,
                LectureCategory.QURAN_EXPLANATION
            ),
            podcastLanguage = PodcastLanguageSection.ENGLISH,
            podcastTopics = listOf(PodcastTopicFilter.QURAN_TAFSEER, PodcastTopicFilter.SPIRITUAL_REMINDERS)
        ),
        VerifiedIslamicChannel(
            key = "mufti_menk",
            channelId = "UCNB_OaI4524fASt8h0IL8dw",
            handle = "@muftimenkofficial",
            nameEn = "Mufti Menk Official",
            nameUr = "مفتی مینک آفیشل",
            nameAr = "المفتي منك الرسمي",
            nameHi = "मुफ़्ती मेनक आधिकारिक",
            specialtyEn = "Manners, Family Guidance & Daily Reminders",
            specialtyUr = "اخلاق، خاندانی تربیت اور ایمانی تذکیر",
            specialtyAr = "الأخلاق والأسرة والتذكير الإيماني",
            specialtyHi = "इस्लामी शिष्टाचार, परिवार और मार्गदर्शन",
            languageCode = "en",
            primaryCategories = listOf(
                LectureCategory.POPULAR_SCHOLARS,
                LectureCategory.ISLAMIC_MANNERS,
                LectureCategory.FAMILY_SOCIETY,
                LectureCategory.NEW_MUSLIM_EDUCATION
            ),
            podcastLanguage = PodcastLanguageSection.ENGLISH,
            podcastTopics = listOf(PodcastTopicFilter.SPIRITUAL_REMINDERS, PodcastTopicFilter.FAMILY_YOUTH)
        ),
        VerifiedIslamicChannel(
            key = "yaqeen",
            channelId = "UC3vHW2h22WE-pNi5WJtRIjg",
            handle = "@yaqeeninstituteofficial",
            nameEn = "Yaqeen Institute (Dr. Omar Suleiman)",
            nameUr = "یقین انسٹیٹیوٹ (ڈاکٹر عمر سلیمان)",
            nameAr = "معهد يقين (د. عمر سليمان)",
            nameHi = "यक़ीन इंस्टीट्यूट (डॉ. उमर सुलेमान)",
            specialtyEn = "Seerah, Islamic History & Contemporary Faith",
            specialtyUr = "سیرت النبی ﷺ، اسلامی تاریخ اور جدید مسائل",
            specialtyAr = "السيرة والتاريخ الإسلامي والفكر",
            specialtyHi = "सीरत, इस्लामी इतिहास और ईमान",
            languageCode = "en",
            primaryCategories = listOf(
                LectureCategory.POPULAR_SCHOLARS,
                LectureCategory.SEERAH,
                LectureCategory.ISLAMIC_HISTORY,
                LectureCategory.ISLAMIC_MANNERS
            ),
            podcastLanguage = PodcastLanguageSection.ENGLISH,
            podcastTopics = listOf(PodcastTopicFilter.SEERAH_HISTORY, PodcastTopicFilter.SPIRITUAL_REMINDERS)
        ),
        VerifiedIslamicChannel(
            key = "seekersguidance",
            channelId = "UCbPVEsJhn5My75IfMWXv1_Q",
            handle = "@seekersguidanceorg",
            nameEn = "SeekersGuidance",
            nameUr = "سیکرز گائیڈنس",
            nameAr = "سيكرز جايدنس للعلوم الشرعية",
            nameHi = "सीकर्सगाइडेंस",
            specialtyEn = "Quran Tafsir, Hadith & Classical Knowledge",
            specialtyUr = "تفسیرِ قرآن، حدیث اور دینی علوم",
            specialtyAr = "تفسير القرآن والحديث والعلوم الشرعية",
            specialtyHi = "क़ुरआन तफ़सीर, हदीस और इस्लामी शिक्षा",
            languageCode = "en",
            primaryCategories = listOf(
                LectureCategory.QURAN_TAFSEER,
                LectureCategory.HADITH,
                LectureCategory.SALAH_WORSHIP,
                LectureCategory.NEW_MUSLIM_EDUCATION
            ),
            podcastLanguage = PodcastLanguageSection.ENGLISH,
            podcastTopics = listOf(PodcastTopicFilter.QURAN_TAFSEER, PodcastTopicFilter.ISLAMIC_QA)
        ),
        VerifiedIslamicChannel(
            key = "assim",
            channelId = "UCWsdcrre0WbCWML_PnuzoAg",
            handle = "@assimalhakeem",
            nameEn = "Sheikh Assim Al-Hakeem",
            nameUr = "شیخ عاصم الحکیم",
            nameAr = "الشيخ عاصم الحكيم",
            nameHi = "शेख़ आसिम अल-हकीम",
            specialtyEn = "Islamic Q&A, Salah & Practical Jurisprudence",
            specialtyUr = "سوال و جواب، نماز اور عملی مسائل",
            specialtyAr = "الفتاوى والأسئلة وفقه الصلاة",
            specialtyHi = "इस्लामी प्रश्नोत्तर, नमाज़ और फ़िक़्ह",
            languageCode = "en",
            primaryCategories = listOf(
                LectureCategory.POPULAR_SCHOLARS,
                LectureCategory.ISLAMIC_QA,
                LectureCategory.SALAH_WORSHIP,
                LectureCategory.NEW_MUSLIM_EDUCATION
            ),
            podcastLanguage = PodcastLanguageSection.ENGLISH,
            podcastTopics = listOf(PodcastTopicFilter.ISLAMIC_QA)
        ),
        VerifiedIslamicChannel(
            key = "qalam",
            channelId = "UCVp5Ze24V2iFiUY1y0WtRaA",
            handle = "@QalamInstitute",
            nameEn = "Qalam Institute",
            nameUr = "قلم انسٹیٹیوٹ",
            nameAr = "معهد قلم",
            nameHi = "क़लम इंस्टीट्यूट",
            specialtyEn = "Seerah, Khutbahs & Youth Guidance",
            specialtyUr = "سیرت، خطبات اور نوجوانوں کی رہنمائی",
            specialtyAr = "السيرة والخطب وتوجيه الشباب",
            specialtyHi = "सीरत, ख़ुत्बा और युवा मार्गदर्शन",
            languageCode = "en",
            primaryCategories = listOf(
                LectureCategory.SEERAH,
                LectureCategory.FAMILY_SOCIETY,
                LectureCategory.ISLAMIC_MANNERS
            ),
            podcastLanguage = PodcastLanguageSection.ENGLISH,
            podcastTopics = listOf(PodcastTopicFilter.SEERAH_HISTORY, PodcastTopicFilter.FAMILY_YOUTH)
        ),
        VerifiedIslamicChannel(
            key = "almadina",
            channelId = "UCys4f7i9vgHiJlk7zLpvk2A",
            handle = "@almadinainstitute",
            nameEn = "Al-Madina Institute",
            nameUr = "المدینہ انسٹیٹیوٹ",
            nameAr = "معهد المدينة",
            nameHi = "अल-मदीना इंस्टीट्यूट",
            specialtyEn = "Podcast Discussions, Faith & Society",
            specialtyUr = "پوڈکاسٹ مکالمے، ایمان اور معاشرہ",
            specialtyAr = "بودكاست وحوارات الإيمان والمجتمع",
            specialtyHi = "पॉडकास्ट चर्चा, ईमान और समाज",
            languageCode = "en",
            primaryCategories = listOf(
                LectureCategory.ISLAMIC_MANNERS,
                LectureCategory.FAMILY_SOCIETY
            ),
            podcastLanguage = PodcastLanguageSection.ENGLISH,
            podcastTopics = listOf(PodcastTopicFilter.SPIRITUAL_REMINDERS, PodcastTopicFilter.FAMILY_YOUTH)
        ),
        VerifiedIslamicChannel(
            key = "tariq_masood",
            channelId = "UCumT1HMTXegYzESyPKH3pVg",
            handle = "@MuftiTariqMasoodSpeeches",
            nameEn = "Mufti Tariq Masood Speeches",
            nameUr = "مفتی طارق مسعود اسپیچز",
            nameAr = "خطب المفتي طارق مسعود",
            nameHi = "मुफ़्ती तारिक़ मसूद स्पीचेज़",
            specialtyEn = "Urdu Masail, Family Life & Q&A",
            specialtyUr = "اردو مسائل، خاندانی زندگی اور سوال و جواب",
            specialtyAr = "المسائل الأسرية والأسئلة بالأردية",
            specialtyHi = "उर्दू मसाइल, पारिवारिक जीवन और प्रश्नोत्तर",
            languageCode = "ur",
            primaryCategories = listOf(
                LectureCategory.POPULAR_SCHOLARS,
                LectureCategory.SALAH_WORSHIP,
                LectureCategory.FAMILY_SOCIETY,
                LectureCategory.ISLAMIC_QA
            ),
            podcastLanguage = PodcastLanguageSection.URDU,
            podcastTopics = listOf(PodcastTopicFilter.ISLAMIC_QA, PodcastTopicFilter.FAMILY_YOUTH)
        ),
        VerifiedIslamicChannel(
            key = "tariq_jamil",
            channelId = "UCct5L-DaVYK8UtmqGU8gJtw",
            handle = "@tariqjamilofficial",
            nameEn = "Maulana Tariq Jamil Official",
            nameUr = "مولانا طارق جمیل آفیشل",
            nameAr = "مولانا طارق جميل الرسمي",
            nameHi = "मौलाना तारिक़ जमील आधिकारिक",
            specialtyEn = "Urdu Spiritual Talks, History & Repentance",
            specialtyUr = "ایمانی بیانات، اسلامی تاریخ اور اصلاحِ قلب",
            specialtyAr = "الدروس الإيمانية والتاريخ الإسلامي بالأردية",
            specialtyHi = "उर्दू रूहानी बयान, इतिहास और तौबा",
            languageCode = "ur",
            primaryCategories = listOf(
                LectureCategory.POPULAR_SCHOLARS,
                LectureCategory.ISLAMIC_MANNERS,
                LectureCategory.ISLAMIC_HISTORY
            ),
            podcastLanguage = PodcastLanguageSection.URDU,
            podcastTopics = listOf(PodcastTopicFilter.SPIRITUAL_REMINDERS, PodcastTopicFilter.SEERAH_HISTORY)
        ),
        VerifiedIslamicChannel(
            key = "youth_club",
            channelId = "UCTedHfyVtctMgTWX2uZkEDQ",
            handle = "@YouthClubPK",
            nameEn = "Youth Club Pakistan",
            nameUr = "یوتھ کلب پاکستان",
            nameAr = "نادي الشباب باكستان",
            nameHi = "यूथ क्लब पाकिस्तान",
            specialtyEn = "Youth Podcasts, Quran & Hadith Discussions",
            specialtyUr = "نوجوانوں کے پوڈکاسٹس، قرآن و حدیث مکالمے",
            specialtyAr = "بودكاست الشباب وحلقات القرآن والحديث",
            specialtyHi = "युवा पॉडकास्ट, क़ुरआन व हदीस चर्चा",
            languageCode = "ur",
            primaryCategories = listOf(
                LectureCategory.FAMILY_SOCIETY,
                LectureCategory.QURAN_EXPLANATION,
                LectureCategory.HADITH
            ),
            podcastLanguage = PodcastLanguageSection.URDU,
            podcastTopics = listOf(PodcastTopicFilter.FAMILY_YOUTH, PodcastTopicFilter.QURAN_TAFSEER, PodcastTopicFilter.ISLAMIC_QA)
        ),
        VerifiedIslamicChannel(
            key = "ajmal_qadri",
            channelId = "UC2SqSPyDNijIPVQuo14lQEQ",
            handle = "@PeerAjmalRazaQadri",
            nameEn = "Peer Ajmal Raza Qadri",
            nameUr = "پیر اجمل رضا قادری",
            nameAr = "بير أجمل رضا قادري",
            nameHi = "पीर अजमल रज़ा क़ादरी",
            specialtyEn = "Urdu Seerah & Emotional Islamic Talks",
            specialtyUr = "سیرت النبی ﷺ اور اصلاحی بیانات",
            specialtyAr = "السيرة النبوية والمواعظ بالأردية",
            specialtyHi = "उर्दू सीरत व इस्लामी बयान",
            languageCode = "ur",
            primaryCategories = listOf(
                LectureCategory.SEERAH,
                LectureCategory.ISLAMIC_MANNERS
            ),
            podcastLanguage = PodcastLanguageSection.URDU,
            podcastTopics = listOf(PodcastTopicFilter.SEERAH_HISTORY, PodcastTopicFilter.SPIRITUAL_REMINDERS)
        ),
        VerifiedIslamicChannel(
            key = "othman_alkamees",
            channelId = "UCWjCSGhmSGu0VLf2mPFS0Kg",
            handle = "@othmanalkamees",
            nameEn = "Dr. Othman Al-Kamees (Arabic)",
            nameUr = "ڈاکٹر عثمان الخمیس (عربی)",
            nameAr = "الشيخ د. عثمان الخميس",
            nameHi = "डॉ. उस्मान अल-ख़मीस (अरबी)",
            specialtyEn = "Arabic Hadith (Sahih Muslim), Fiqh & Fatwa Q&A",
            specialtyUr = "صحیح مسلم، فقہ اور عربی فتاویٰ",
            specialtyAr = "شرح صحيح مسلم والفقه والفتاوى",
            specialtyHi = "अरबी हदीस (सहीह मुस्लिम), फ़िक़्ह व फ़तवा",
            languageCode = "ar",
            primaryCategories = listOf(
                LectureCategory.POPULAR_SCHOLARS,
                LectureCategory.HADITH,
                LectureCategory.ISLAMIC_QA,
                LectureCategory.SALAH_WORSHIP
            ),
            podcastLanguage = PodcastLanguageSection.ARABIC,
            podcastTopics = listOf(PodcastTopicFilter.ISLAMIC_QA, PodcastTopicFilter.SEERAH_HISTORY)
        ),
        VerifiedIslamicChannel(
            key = "mustafa_hosny",
            channelId = "UCwttCEi9Fq1DgTd5Anil6rQ",
            handle = "@MustafaHosny",
            nameEn = "Mustafa Hosny (Arabic)",
            nameUr = "مصطفیٰ حسنی (عربی)",
            nameAr = "مصطفى حسني",
            nameHi = "मुस्तफ़ा होस्नी (अरबी)",
            specialtyEn = "Arabic Spiritual Reminders & Names of Allah",
            specialtyUr = "اسماء الحسنیٰ اور ایمانی تربیت (عربی)",
            specialtyAr = "الرقائق وشرح أسماء الله الحسنى",
            specialtyHi = "अरबी रूहानी तज़कीर व अस्माउल हुस्ना",
            languageCode = "ar",
            primaryCategories = listOf(
                LectureCategory.ISLAMIC_MANNERS,
                LectureCategory.FAMILY_SOCIETY
            ),
            podcastLanguage = PodcastLanguageSection.ARABIC,
            podcastTopics = listOf(PodcastTopicFilter.SPIRITUAL_REMINDERS, PodcastTopicFilter.FAMILY_YOUTH)
        ),
        VerifiedIslamicChannel(
            key = "islamweb",
            channelId = "UCeH0M5ZjsNE1BDBs-AeH4sA",
            handle = "@islamweb",
            nameEn = "IslamWeb Official (Arabic)",
            nameUr = "اسلام ویب آفیشل (عربی)",
            nameAr = "إسلام ويب الرسمي",
            nameHi = "इस्लामवेब आधिकारिक (अरबी)",
            specialtyEn = "Arabic Quran Competitions, Fatwa & Knowledge",
            specialtyUr = "قرآن، فقہ اور شرعی علم (عربی)",
            specialtyAr = "القرآن الكريم والعلوم الشرعية",
            specialtyHi = "अरबी क़ुरआन, फ़िक़्ह व इस्लामी ज्ञान",
            languageCode = "ar",
            primaryCategories = listOf(
                LectureCategory.QURAN_EXPLANATION,
                LectureCategory.NEW_MUSLIM_EDUCATION
            ),
            podcastLanguage = PodcastLanguageSection.ARABIC,
            podcastTopics = listOf(PodcastTopicFilter.QURAN_TAFSEER, PodcastTopicFilter.ISLAMIC_QA)
        ),
        VerifiedIslamicChannel(
            key = "saudi_quran",
            channelId = "UCos52azQNBgW63_9uDJoPDA",
            handle = "@SaudiQuranTv",
            nameEn = "Saudi Quran TV (Makkah)",
            nameUr = "سعودی قرآن ٹی وی (مکہ مکرمہ)",
            nameAr = "قناة القرآن الكريم السعودية",
            nameHi = "सऊदी क़ुरआन टीवी (मक्का)",
            specialtyEn = "Official Masjid al-Haram Recitations & Prayers",
            specialtyUr = "مسجد الحرام کی تلاوت و نمازیں",
            specialtyAr = "تلاوات وصلوات المسجد الحرام",
            specialtyHi = "मस्जिद अल-हरम से तिलावत व नमाज़",
            languageCode = "ar",
            primaryCategories = listOf(
                LectureCategory.QURAN_EXPLANATION,
                LectureCategory.SALAH_WORSHIP
            ),
            podcastLanguage = PodcastLanguageSection.ARABIC,
            podcastTopics = listOf(PodcastTopicFilter.QURAN_TAFSEER)
        ),
        VerifiedIslamicChannel(
            key = "saudi_sunnah",
            channelId = "UCROKYPep-UuODNwyipe6JMw",
            handle = "@SaudiSunnahTv",
            nameEn = "Saudi Sunnah TV (Madinah)",
            nameUr = "سعودی سنت ٹی وی (مدینہ منورہ)",
            nameAr = "قناة السنة النبوية السعودية",
            nameHi = "सऊदी सुन्नत टीवी (मदीना)",
            specialtyEn = "Official Masjid an-Nabawi Prayers & Recitations",
            specialtyUr = "مسجد نبوی ﷺ سے تلاوت و نمازیں",
            specialtyAr = "تلاوات وصلوات المسجد النبوي",
            specialtyHi = "मस्जिद अन-नबवी से तिलावत व नमाज़",
            languageCode = "ar",
            primaryCategories = listOf(
                LectureCategory.HADITH,
                LectureCategory.SEERAH
            ),
            podcastLanguage = PodcastLanguageSection.ARABIC,
            podcastTopics = listOf(PodcastTopicFilter.SEERAH_HISTORY)
        )
    )

    /**
     * Explicit topic-to-channel mapping so selecting a lecture topic fetches ONLY the
     * relevant channel RSS feeds (never all configured channels unnecessarily).
     */
    val TOPIC_CHANNEL_MAPPING: Map<LectureCategory, List<String>> = mapOf(
        LectureCategory.POPULAR_SCHOLARS to listOf("bayyinah", "yaqeen", "tariq_masood", "othman_alkamees"),
        LectureCategory.LATEST_LECTURES to listOf("yaqeen", "bayyinah", "tariq_masood", "othman_alkamees"),
        LectureCategory.QURAN_TAFSEER to listOf("bayyinah", "seekersguidance"),
        LectureCategory.QURAN_EXPLANATION to listOf("bayyinah", "saudi_quran", "youth_club"),
        LectureCategory.HADITH to listOf("seekersguidance", "othman_alkamees", "youth_club"),
        LectureCategory.SEERAH to listOf("yaqeen", "qalam", "ajmal_qadri"),
        LectureCategory.ISLAMIC_HISTORY to listOf("yaqeen", "tariq_jamil"),
        LectureCategory.SALAH_WORSHIP to listOf("assim", "tariq_masood", "saudi_quran", "saudi_sunnah"),
        LectureCategory.ISLAMIC_MANNERS to listOf("mufti_menk", "mustafa_hosny", "tariq_jamil"),
        LectureCategory.FAMILY_SOCIETY to listOf("mufti_menk", "tariq_masood", "qalam", "youth_club"),
        LectureCategory.NEW_MUSLIM_EDUCATION to listOf("assim", "mufti_menk", "seekersguidance"),
        LectureCategory.ISLAMIC_QA to listOf("assim", "tariq_masood", "othman_alkamees")
    )

    val HARAMEYN_CHANNELS: Map<HarameynLocation, HarameynStreamChannel> = mapOf(
        HarameynLocation.MAKKAH to HarameynStreamChannel(
            location = HarameynLocation.MAKKAH,
            channelId = "UCos52azQNBgW63_9uDJoPDA",
            handle = "@SaudiQuranTv",
            nameEn = "Al-Quran Al-Kareem TV (Saudi Broadcasting Authority)",
            nameUr = "سعودی چینل القرآن الکریم (مسجد الحرام)",
            nameAr = "قناة القرآن الكريم الرسمية (المسجد الحرام)",
            nameHi = "सऊदी अल-क़ुरआन अल-करीम टीवी (मस्जिद अल-हरम)"
        ),
        HarameynLocation.MADINAH to HarameynStreamChannel(
            location = HarameynLocation.MADINAH,
            channelId = "UCROKYPep-UuODNwyipe6JMw",
            handle = "@SaudiSunnahTv",
            nameEn = "Al-Sunnah Al-Nabawiyah TV (Saudi Broadcasting Authority)",
            nameUr = "سعودی چینل السنۃ النبویہ (مسجد نبوی ﷺ)",
            nameAr = "قناة السنة النبوية الرسمية (المسجد النبوي)",
            nameHi = "सऊदी अल-सुन्नत अन-नबविया टीवी (मस्जिद अन-नबवी)"
        )
    )

    fun isValidChannelId(channelId: String): Boolean = CHANNEL_ID_REGEX.matches(channelId.trim())

    fun isValidVideoId(videoId: String): Boolean = VIDEO_ID_REGEX.matches(videoId.trim())

    fun channelsForTopic(
        topic: LectureCategory?,
        channelMapping: Map<LectureCategory, List<String>> = TOPIC_CHANNEL_MAPPING
    ): List<VerifiedIslamicChannel>? {
        if (topic == null) return null
        val mappedKeys = channelMapping[topic] ?: return null
        if (mappedKeys.isEmpty()) return null
        val resolved = mappedKeys.mapNotNull { key ->
            VERIFIED_CHANNELS.firstOrNull { it.key == key && isValidChannelId(it.channelId) }
        }
        return if (resolved.size == mappedKeys.size && resolved.isNotEmpty()) resolved else null
    }

    fun podcastChannelsForSelection(
        languageSection: PodcastLanguageSection,
        topicFilter: PodcastTopicFilter = PodcastTopicFilter.ALL_TOPICS
    ): List<VerifiedIslamicChannel> {
        val base = when (languageSection) {
            PodcastLanguageSection.LANGUAGE_NOT_VERIFIED -> {
                VERIFIED_CHANNELS.filter { it.podcastLanguage != null }
            }
            else -> {
                VERIFIED_CHANNELS.filter { it.podcastLanguage == languageSection }
            }
        }
        if (topicFilter == PodcastTopicFilter.ALL_TOPICS || languageSection == PodcastLanguageSection.LANGUAGE_NOT_VERIFIED) {
            return base
        }
        return base.filter { it.podcastTopics.contains(topicFilter) }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        networkChecker?.let { return it(context) }
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return true
            val network = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            true
        }
    }

    private data class ChannelFetchOutcome(
        val channel: VerifiedIslamicChannel,
        val items: List<YouTubeVideoItem>,
        val fetchedOnline: Boolean,
        val cacheTimestampMs: Long,
        val isExpiredCache: Boolean,
        val unavailable: Boolean,
        val errorDetail: String? = null
    )

    /**
     * Topic-based lecture fetch:
     * - Never fetches all configured channels.
     * - Resolves ONLY channels mapped to [topic].
     * - If mapping is missing/invalid, returns [FeedLoadStatus.INVALID_MAPPING] without network calls.
     * - Filters genuine fetched items to the selected topic locally.
     */
    suspend fun fetchLecturesForTopic(
        context: Context,
        topic: LectureCategory?,
        channelKey: String? = null,
        forceRefresh: Boolean = false,
        channelMapping: Map<LectureCategory, List<String>> = TOPIC_CHANNEL_MAPPING
    ): LectureFeedResult = withContext(Dispatchers.IO) {
        val targets = if (!channelKey.isNullOrBlank()) {
            val single = VERIFIED_CHANNELS.firstOrNull { it.key == channelKey && isValidChannelId(it.channelId) }
            if (single == null) {
                return@withContext LectureFeedResult(
                    videos = emptyList(),
                    status = FeedLoadStatus.INVALID_MAPPING,
                    errorMessage = "Invalid or unknown scholar channel mapping: $channelKey"
                )
            }
            listOf(single)
        } else {
            channelsForTopic(topic, channelMapping)
                ?: return@withContext LectureFeedResult(
                    videos = emptyList(),
                    status = FeedLoadStatus.INVALID_MAPPING,
                    errorMessage = "Missing or invalid topic channel mapping."
                )
        }

        val online = isNetworkAvailable(context)
        val outcomes = coroutineScope {
            targets.map { channel ->
                async { fetchSingleChannelFeed(context, channel, forceRefresh, online) }
            }.awaitAll()
        }

        val rawCombined = outcomes
            .flatMap { it.items }
            .distinctBy { it.id }
            .sortedByDescending { it.publishedEpochMs }

        val filteredByTopic = if (!channelKey.isNullOrBlank() || topic == null ||
            topic == LectureCategory.LATEST_LECTURES || topic == LectureCategory.POPULAR_SCHOLARS
        ) {
            rawCombined
        } else {
            rawCombined.filter { it.categories.contains(topic) }
        }

        buildFeedResult(
            videos = filteredByTopic,
            outcomes = outcomes,
            online = online,
            hadAnyRawItems = rawCombined.isNotEmpty()
        )
    }

    /**
     * Fetches official Atom XML feeds for a specific verified channel key or all verified channels.
     * If a non-blank [channelKey] does not match any verified channel, returns [FeedLoadStatus.INVALID_MAPPING]
     * instead of silently fetching all channels.
     */
    suspend fun fetchVerifiedChannelFeeds(
        context: Context,
        channelKey: String? = null,
        forceRefresh: Boolean = false
    ): LectureFeedResult = withContext(Dispatchers.IO) {
        val targets = if (channelKey.isNullOrBlank()) {
            VERIFIED_CHANNELS
        } else {
            val matched = VERIFIED_CHANNELS.filter { it.key == channelKey }
            if (matched.isEmpty()) {
                return@withContext LectureFeedResult(
                    videos = emptyList(),
                    status = FeedLoadStatus.INVALID_MAPPING,
                    errorMessage = "Invalid channel mapping: $channelKey"
                )
            }
            matched
        }

        val online = isNetworkAvailable(context)
        val outcomes = coroutineScope {
            targets.map { channel ->
                async { fetchSingleChannelFeed(context, channel, forceRefresh, online) }
            }.awaitAll()
        }

        val combined = outcomes
            .flatMap { it.items }
            .distinctBy { it.id }
            .sortedByDescending { it.publishedEpochMs }

        buildFeedResult(
            videos = combined,
            outcomes = outcomes,
            online = online,
            hadAnyRawItems = combined.isNotEmpty()
        )
    }

    /**
     * Fetches official YouTube RSS podcast episodes strictly for the chosen [languageSection]
     * and [topicFilter], verifying each entry's actual language from RSS metadata.
     */
    suspend fun fetchPodcastsForSelection(
        context: Context,
        languageSection: PodcastLanguageSection,
        topicFilter: PodcastTopicFilter = PodcastTopicFilter.ALL_TOPICS,
        forceRefresh: Boolean = false
    ): LectureFeedResult = withContext(Dispatchers.IO) {
        val targets = podcastChannelsForSelection(languageSection, topicFilter)
        if (targets.isEmpty()) {
            return@withContext LectureFeedResult(
                videos = emptyList(),
                status = FeedLoadStatus.ONLINE_FRESH
            )
        }

        val online = isNetworkAvailable(context)
        val outcomes = coroutineScope {
            targets.map { channel ->
                async { fetchSingleChannelFeed(context, channel, forceRefresh, online) }
            }.awaitAll()
        }

        val allItems = outcomes
            .flatMap { it.items }
            .distinctBy { it.id }
            .sortedByDescending { it.publishedEpochMs }

        val languageFiltered = allItems.filter { item ->
            item.verifiedPodcastLanguage == languageSection
        }

        val topicFiltered = if (topicFilter == PodcastTopicFilter.ALL_TOPICS ||
            languageSection == PodcastLanguageSection.LANGUAGE_NOT_VERIFIED
        ) {
            languageFiltered
        } else {
            val byKeyword = languageFiltered.filter { item ->
                val lower = item.title.lowercase(Locale.ROOT)
                topicFilter.matchKeywords.any { kw -> lower.contains(kw) }
            }
            byKeyword
        }

        buildFeedResult(
            videos = topicFiltered,
            outcomes = outcomes,
            online = online,
            hadAnyRawItems = allItems.isNotEmpty()
        )
    }

    private fun buildFeedResult(
        videos: List<YouTubeVideoItem>,
        outcomes: List<ChannelFetchOutcome>,
        online: Boolean,
        hadAnyRawItems: Boolean
    ): LectureFeedResult {
        val unavailable = outcomes.filter { it.unavailable }.map { it.channel.handle }
        val latestTimestamp = outcomes.maxOfOrNull { it.cacheTimestampMs } ?: 0L
        val anyFreshOnline = outcomes.any { it.fetchedOnline }
        val anyExpiredCache = outcomes.any { it.isExpiredCache && it.items.isNotEmpty() }
        val allUnavailableAndEmpty = !hadAnyRawItems && outcomes.all { it.unavailable || it.items.isEmpty() }

        val status = when {
            anyFreshOnline -> FeedLoadStatus.ONLINE_FRESH
            !online && hadAnyRawItems -> FeedLoadStatus.OFFLINE_CACHE
            anyExpiredCache && hadAnyRawItems -> FeedLoadStatus.EXPIRED_CACHE_FALLBACK
            hadAnyRawItems -> if (online) FeedLoadStatus.CACHED_FRESH else FeedLoadStatus.OFFLINE_CACHE
            allUnavailableAndEmpty && (!online || unavailable.isNotEmpty()) -> FeedLoadStatus.ERROR_NO_CACHE
            else -> if (online) FeedLoadStatus.ONLINE_FRESH else FeedLoadStatus.ERROR_NO_CACHE
        }

        val errorDetail = if (status == FeedLoadStatus.ERROR_NO_CACHE) {
            outcomes.firstNotNullOfOrNull { it.errorDetail }
                ?: if (!online) "No internet connection and no cached feed available." else "Feed temporarily unavailable."
        } else {
            null
        }

        return LectureFeedResult(
            videos = videos,
            status = status,
            unavailableChannels = unavailable,
            lastUpdatedMs = latestTimestamp,
            isCacheExpired = anyExpiredCache && !anyFreshOnline,
            errorMessage = errorDetail
        )
    }

    private suspend fun fetchSingleChannelFeed(
        context: Context,
        channel: VerifiedIslamicChannel,
        forceRefresh: Boolean,
        isOnline: Boolean
    ): ChannelFetchOutcome {
        if (!isValidChannelId(channel.channelId)) {
            return ChannelFetchOutcome(
                channel = channel,
                items = emptyList(),
                fetchedOnline = false,
                cacheTimestampMs = 0L,
                isExpiredCache = false,
                unavailable = true,
                errorDetail = "Invalid channel ID format for ${channel.handle}"
            )
        }

        val now = nowMs()
        if (!forceRefresh) {
            val mem = memoryCache[channel.channelId]
            if (mem != null && (now - mem.first) < CACHE_TTL_MS && mem.second.isNotEmpty()) {
                return ChannelFetchOutcome(
                    channel = channel,
                    items = mem.second,
                    fetchedOnline = false,
                    cacheTimestampMs = mem.first,
                    isExpiredCache = false,
                    unavailable = false
                )
            }
            val diskCached = loadChannelCache(context, channel.channelId)
            if (diskCached.second.isNotEmpty() && (now - diskCached.first) < CACHE_TTL_MS) {
                val cachedList = diskCached.second.map { it.copy(isFromCache = true, isCacheExpired = false) }
                memoryCache[channel.channelId] = diskCached.first to cachedList
                return ChannelFetchOutcome(
                    channel = channel,
                    items = cachedList,
                    fetchedOnline = false,
                    cacheTimestampMs = diskCached.first,
                    isExpiredCache = false,
                    unavailable = false
                )
            }
        }

        if (!isOnline) {
            val diskCached = loadChannelCache(context, channel.channelId)
            val expired = diskCached.first > 0L && (now - diskCached.first) >= CACHE_TTL_MS
            return ChannelFetchOutcome(
                channel = channel,
                items = diskCached.second.map { it.copy(isFromCache = true, isCacheExpired = expired) },
                fetchedOnline = false,
                cacheTimestampMs = diskCached.first,
                isExpiredCache = expired,
                unavailable = diskCached.second.isEmpty(),
                errorDetail = if (diskCached.second.isEmpty()) "Offline and no local cache available." else null
            )
        }

        val existingJob = inFlightRequests[channel.channelId]
        if (existingJob != null && existingJob.isActive) {
            return existingJob.await()
        }

        return coroutineScope {
            val deferred = async {
                try {
                    val fetchResult = executeGetWithRetry(channel.feedUrl, maxAttempts = 2)
                    if (fetchResult.body != null) {
                        val parsed = parseAtomFeedXml(fetchResult.body, channel)
                        if (parsed.isNotEmpty()) {
                            val timestamp = nowMs()
                            memoryCache[channel.channelId] = timestamp to parsed
                            saveChannelCache(context, channel.channelId, timestamp, parsed)
                            ChannelFetchOutcome(
                                channel = channel,
                                items = parsed,
                                fetchedOnline = true,
                                cacheTimestampMs = timestamp,
                                isExpiredCache = false,
                                unavailable = false
                            )
                        } else {
                            // Empty or malformed feed XML — do not treat expired cache as fresh
                            val fallbackCache = loadChannelCache(context, channel.channelId)
                            val expired = fallbackCache.first > 0L && (nowMs() - fallbackCache.first) >= CACHE_TTL_MS
                            ChannelFetchOutcome(
                                channel = channel,
                                items = fallbackCache.second.map { it.copy(isFromCache = true, isCacheExpired = expired) },
                                fetchedOnline = fallbackCache.second.isEmpty(),
                                cacheTimestampMs = fallbackCache.first,
                                isExpiredCache = expired && fallbackCache.second.isNotEmpty(),
                                unavailable = false
                            )
                        }
                    } else {
                        val fallbackCache = loadChannelCache(context, channel.channelId)
                        val expired = fallbackCache.first > 0L && (nowMs() - fallbackCache.first) >= CACHE_TTL_MS
                        ChannelFetchOutcome(
                            channel = channel,
                            items = fallbackCache.second.map { it.copy(isFromCache = true, isCacheExpired = expired) },
                            fetchedOnline = false,
                            cacheTimestampMs = fallbackCache.first,
                            isExpiredCache = expired && fallbackCache.second.isNotEmpty(),
                            unavailable = true,
                            errorDetail = fetchResult.errorMessage
                        )
                    }
                } finally {
                    inFlightRequests.remove(channel.channelId)
                }
            }
            inFlightRequests[channel.channelId] = deferred
            deferred.await()
        }
    }

    private data class HttpFetchResult(val body: String? = null, val errorMessage: String? = null)

    private fun executeGetWithRetry(url: String, maxAttempts: Int = 2): HttpFetchResult {
        var lastErr = "Request failed"
        repeat(maxAttempts) { attempt ->
            try {
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        return HttpFetchResult(body = response.body?.string())
                    }
                    lastErr = "HTTP ${response.code}"
                    Log.w(TAG, "RSS request returned HTTP ${response.code}")
                    if (response.code == 404 || response.code == 410 || response.code in 400..499) {
                        return HttpFetchResult(body = null, errorMessage = lastErr)
                    }
                }
            } catch (e: Exception) {
                lastErr = e::class.java.simpleName
                Log.w(TAG, "RSS request attempt ${attempt + 1} failed: ${e::class.java.simpleName}")
                if (attempt == maxAttempts - 1) {
                    return HttpFetchResult(body = null, errorMessage = lastErr)
                }
            }
        }
        return HttpFetchResult(body = null, errorMessage = lastErr)
    }

    /**
     * Parses an official YouTube Atom XML feed (`https://www.youtube.com/feeds/videos.xml?channel_id=...`).
     * Validates video IDs (`^[A-Za-z0-9_-]{11}$`), skips malformed entries, deduplicates IDs, and sorts newest first.
     */
    fun parseAtomFeedXml(
        xml: String,
        channel: VerifiedIslamicChannel? = null
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

            var entryVideoId = ""
            var entryTitle = ""
            var entryAuthor = ""
            var entryPublished = ""
            var entryThumbnail = ""
            var entryLink = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name ?: ""
                        when {
                            name == "title" && !insideEntry -> {
                                feedTitle = parser.nextText().trim()
                            }
                            name == "entry" -> {
                                insideEntry = true
                                entryVideoId = ""
                                entryTitle = ""
                                entryAuthor = ""
                                entryPublished = ""
                                entryThumbnail = ""
                                entryLink = ""
                            }
                            insideEntry -> {
                                when {
                                    name == "videoId" -> {
                                        entryVideoId = parser.nextText().trim()
                                    }
                                    name == "title" -> {
                                        entryTitle = parser.nextText().trim()
                                    }
                                    name == "author" -> {
                                        insideAuthor = true
                                    }
                                    name == "name" && insideAuthor -> {
                                        entryAuthor = parser.nextText().trim()
                                    }
                                    name == "published" -> {
                                        entryPublished = parser.nextText().trim()
                                    }
                                    name == "thumbnail" -> {
                                        val urlAttr = parser.getAttributeValue(null, "url")?.trim().orEmpty()
                                        if (urlAttr.startsWith("https://") && entryThumbnail.isBlank()) {
                                            entryThumbnail = urlAttr
                                        }
                                    }
                                    name == "link" -> {
                                        val rel = parser.getAttributeValue(null, "rel") ?: "alternate"
                                        val href = parser.getAttributeValue(null, "href")?.trim().orEmpty()
                                        if (rel == "alternate" && href.isNotBlank()) {
                                            entryLink = href
                                            if (entryVideoId.isBlank() && href.contains("v=")) {
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
                            if (isValidVideoId(entryVideoId) && entryTitle.isNotBlank()) {
                                val resolvedChannelName = entryAuthor.ifBlank {
                                    channel?.nameEn ?: feedTitle.ifBlank { "Verified Channel" }
                                }
                                val thumb = entryThumbnail.ifBlank {
                                    "https://i.ytimg.com/vi/$entryVideoId/hqdefault.jpg"
                                }
                                val videoLink = entryLink.ifBlank {
                                    "https://www.youtube.com/watch?v=$entryVideoId"
                                }
                                val epochMs = parseIsoDateEpoch(entryPublished)
                                val displayDate = formatIsoDateShort(entryPublished)
                                val categories = classifyLectureCategories(entryTitle, channel)
                                val verifiedPodcastLang = verifyPodcastLanguageFromContent(
                                    title = entryTitle,
                                    channelName = resolvedChannelName
                                )
                                val resolvedLangCode = when (verifiedPodcastLang) {
                                    PodcastLanguageSection.URDU -> "ur"
                                    PodcastLanguageSection.ARABIC -> "ar"
                                    PodcastLanguageSection.ENGLISH -> "en"
                                    PodcastLanguageSection.LANGUAGE_NOT_VERIFIED -> channel?.languageCode ?: "und"
                                }

                                items.add(
                                    YouTubeVideoItem(
                                        id = entryVideoId,
                                        title = entryTitle,
                                        channel = resolvedChannelName,
                                        channelId = channel?.channelId ?: "",
                                        channelKey = channel?.key ?: "",
                                        thumbnail = thumb,
                                        publishedIso = entryPublished,
                                        publishedDisplay = displayDate,
                                        publishedEpochMs = epochMs,
                                        videoUrl = videoLink,
                                        languageCode = resolvedLangCode,
                                        verifiedPodcastLanguage = verifiedPodcastLang,
                                        categories = categories,
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
            return items
        }

        return items.distinctBy { it.id }.sortedByDescending { it.publishedEpochMs }
    }

    /**
     * Inspects actual RSS entry title & metadata to verify podcast language reliably.
     * Never guesses from channel name alone and never mislabels English titles as Urdu or Arabic.
     * Ambiguous/unverifiable titles are placed into [PodcastLanguageSection.LANGUAGE_NOT_VERIFIED].
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

        // Tokenize Latin words for deterministic Roman Urdu vs English verification
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
        // Single strong Roman Urdu word combined with known Urdu speaker keywords
        val lowerFull = "$clean $channelName".lowercase(Locale.ROOT)
        if (urduHits == 1 && (lowerFull.contains("bayan") || lowerFull.contains("masood") ||
                lowerFull.contains("jamil") || lowerFull.contains("qadri") || lowerFull.contains("urdu"))
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

    internal fun classifyLectureCategories(
        title: String,
        channel: VerifiedIslamicChannel?
    ): List<LectureCategory> {
        val lower = title.lowercase(Locale.ROOT)
        val matched = LinkedHashSet<LectureCategory>()
        matched.add(LectureCategory.LATEST_LECTURES)

        if (channel != null && LectureCategory.POPULAR_SCHOLARS in channel.primaryCategories) {
            matched.add(LectureCategory.POPULAR_SCHOLARS)
        }

        LectureCategory.entries.forEach { cat ->
            if (cat.matchKeywords.isNotEmpty() && cat.matchKeywords.any { kw -> lower.contains(kw) }) {
                matched.add(cat)
            }
        }

        return matched.toList()
    }

    /**
     * Searches ONLY across real verified feed items (scholar, title, channel, category keywords, language).
     * Never returns fake unrelated fallback videos when nothing matches.
     */
    fun searchLocalVerifiedLectures(
        items: List<YouTubeVideoItem>,
        query: String = "",
        category: LectureCategory? = null,
        channelKey: String? = null,
        languageFilter: String? = null
    ): List<YouTubeVideoItem> {
        val trimmed = query.trim().lowercase(Locale.ROOT)
        return items.filter { item ->
            val matchesChannel = channelKey.isNullOrBlank() || item.channelKey == channelKey
            val matchesLang = languageFilter.isNullOrBlank() || item.languageCode.equals(languageFilter, ignoreCase = true)
            val matchesCat = when (category) {
                null, LectureCategory.LATEST_LECTURES, LectureCategory.POPULAR_SCHOLARS -> true
                else -> item.categories.contains(category)
            }
            if (!matchesChannel || !matchesLang || !matchesCat) return@filter false

            if (trimmed.isEmpty()) return@filter true

            val channelMeta = VERIFIED_CHANNELS.firstOrNull { it.key == item.channelKey }
            val searchableFields = listOfNotNull(
                item.title,
                item.channel,
                item.publishedDisplay,
                item.languageCode,
                channelMeta?.nameEn,
                channelMeta?.nameUr,
                channelMeta?.nameAr,
                channelMeta?.nameHi,
                channelMeta?.specialtyEn,
                channelMeta?.specialtyUr
            )
            searchableFields.any { it.lowercase(Locale.ROOT).contains(trimmed) } ||
                item.categories.any { cat -> cat.matchKeywords.any { kw -> kw.contains(trimmed) || trimmed.contains(kw) } }
        }
    }

    /**
     * Checks official public YouTube RSS feeds for the Saudi Makkah / Madinah broadcaster channels.
     *
     * STRICT ZERO-API-KEY POLICY:
     * - Zero YouTube HTML scraping (`channel/<id>/live` HTML is never scraped).
     * - Zero fabricated LIVE badges or hardcoded stream video IDs.
     * - Official channel RSS (`feeds/videos.xml`) provides recent uploaded/recorded broadcasts ONLY;
     *   active live stream status cannot be confirmed without YouTube Data API v3 credentials.
     * - Recorded RSS uploads are clearly returned as [recentRecordedUploads] and never presented as LIVE.
     */
    suspend fun checkHarameynLiveStatus(
        context: Context,
        location: HarameynLocation
    ): LiveStreamStatusResult = withContext(Dispatchers.IO) {
        val channel = HARAMEYN_CHANNELS.getValue(location)

        if (!isNetworkAvailable(context)) {
            return@withContext LiveStreamStatusResult(
                location = location,
                channel = channel,
                state = LiveBroadcastState.OFFLINE,
                verifiedVideoId = null,
                streamTitle = channel.nameEn,
                watchOnYouTubeUrl = channel.officialChannelUrl,
                isEmbeddableConfirmed = false,
                statusDetail = "Offline — connect to internet to check official channel feed."
            )
        }

        val rssResult = executeGetWithRetry(channel.feedUrl, maxAttempts = 2)
        if (rssResult.body == null) {
            return@withContext LiveStreamStatusResult(
                location = location,
                channel = channel,
                state = LiveBroadcastState.CURRENTLY_UNAVAILABLE,
                verifiedVideoId = null,
                streamTitle = channel.nameEn,
                watchOnYouTubeUrl = channel.officialChannelUrl,
                isEmbeddableConfirmed = false,
                statusDetail = rssResult.errorMessage ?: "Official channel feed currently unavailable."
            )
        }

        val channelMeta = VERIFIED_CHANNELS.firstOrNull { it.channelId == channel.channelId }
        val recordedUploads = parseAtomFeedXml(rssResult.body, channelMeta).take(6)

        LiveStreamStatusResult(
            location = location,
            channel = channel,
            state = LiveBroadcastState.LIVE_STATUS_UNCONFIRMED,
            verifiedVideoId = null,
            streamTitle = channel.nameEn,
            publishedDisplay = recordedUploads.firstOrNull()?.publishedDisplay.orEmpty(),
            watchOnYouTubeUrl = channel.officialChannelUrl,
            isEmbeddableConfirmed = false,
            recentRecordedUploads = recordedUploads,
            statusDetail = "Official RSS reachable; live stream status cannot be confirmed without YouTube Data API credentials."
        )
    }

    /**
     * Checks official YouTube oEmbed endpoint to verify a video exists and returns embeddable metadata.
     */
    fun fetchOEmbedJson(videoId: String): JSONObject? {
        if (!isValidVideoId(videoId)) return null
        val url = "https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=$videoId&format=json"
        val res = executeGetWithRetry(url, maxAttempts = 1)
        val body = res.body ?: return null
        return try {
            JSONObject(body)
        } catch (_: Exception) {
            null
        }
    }

    fun formatLastUpdatedDisplay(timestampMs: Long): String {
        if (timestampMs <= 0L) return ""
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            sdf.format(Date(timestampMs))
        } catch (_: Exception) {
            ""
        }
    }

    // =========================================================
    // LOCAL METADATA CACHE, BOOKMARKS & RECENTLY WATCHED HISTORY
    // =========================================================

    private fun saveChannelCache(context: Context, channelId: String, timestamp: Long, items: List<YouTubeVideoItem>) {
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit()
                .putString(KEY_CHANNEL_CACHE_PREFIX + channelId, serializeVideosJson(items))
                .putLong(KEY_CHANNEL_TS_PREFIX + channelId, timestamp)
                .apply()
        } catch (e: Exception) {
            Log.w(TAG, "Failed saving channel cache: ${e::class.java.simpleName}")
        }
    }

    private fun loadChannelCache(context: Context, channelId: String): Pair<Long, List<YouTubeVideoItem>> {
        return try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val ts = prefs.getLong(KEY_CHANNEL_TS_PREFIX + channelId, 0L)
            val raw = prefs.getString(KEY_CHANNEL_CACHE_PREFIX + channelId, null) ?: return 0L to emptyList()
            ts to deserializeVideosJson(raw)
        } catch (e: Exception) {
            Log.w(TAG, "Failed loading channel cache: ${e::class.java.simpleName}")
            0L to emptyList()
        }
    }

    fun getFavoriteLectures(context: Context): List<YouTubeVideoItem> =
        readVideoListPref(context, KEY_FAVORITES_JSON)

    fun toggleFavoriteLecture(context: Context, item: YouTubeVideoItem): Boolean =
        toggleVideoInPrefList(context, KEY_FAVORITES_JSON, item)

    fun getRecentlyWatchedLectures(context: Context): List<YouTubeVideoItem> =
        readVideoListPref(context, KEY_RECENT_WATCHED_JSON)

    fun recordWatchedLecture(context: Context, item: YouTubeVideoItem): List<YouTubeVideoItem> =
        recordVideoInPrefList(context, KEY_RECENT_WATCHED_JSON, item)

    fun getFavoritePodcasts(context: Context): List<YouTubeVideoItem> =
        readVideoListPref(context, KEY_PODCAST_FAVORITES_JSON)

    fun toggleFavoritePodcast(context: Context, item: YouTubeVideoItem): Boolean =
        toggleVideoInPrefList(context, KEY_PODCAST_FAVORITES_JSON, item)

    fun getRecentlyWatchedPodcasts(context: Context): List<YouTubeVideoItem> =
        readVideoListPref(context, KEY_PODCAST_RECENT_JSON)

    fun recordWatchedPodcast(context: Context, item: YouTubeVideoItem): List<YouTubeVideoItem> =
        recordVideoInPrefList(context, KEY_PODCAST_RECENT_JSON, item)

    private fun readVideoListPref(context: Context, key: String): List<YouTubeVideoItem> {
        return try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val raw = prefs.getString(key, null) ?: return emptyList()
            deserializeVideosJson(raw)
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun toggleVideoInPrefList(context: Context, key: String, item: YouTubeVideoItem): Boolean {
        val current = readVideoListPref(context, key).toMutableList()
        val exists = current.any { it.id == item.id }
        val isNowFav = if (exists) {
            current.removeAll { it.id == item.id }
            false
        } else {
            current.add(0, item)
            true
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(key, serializeVideosJson(current))
            .apply()
        return isNowFav
    }

    private fun recordVideoInPrefList(context: Context, key: String, item: YouTubeVideoItem): List<YouTubeVideoItem> {
        val current = readVideoListPref(context, key).toMutableList()
        current.removeAll { it.id == item.id }
        current.add(0, item)
        val trimmed = current.take(MAX_RECENT_WATCHED)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(key, serializeVideosJson(trimmed))
            .apply()
        return trimmed
    }

    fun clearTestCaches() {
        memoryCache.clear()
        inFlightRequests.clear()
    }

    private fun serializeVideosJson(items: List<YouTubeVideoItem>): String {
        val arr = JSONArray()
        items.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("title", item.title)
                put("channel", item.channel)
                put("channelId", item.channelId)
                put("channelKey", item.channelKey)
                put("thumbnail", item.thumbnail)
                put("publishedIso", item.publishedIso)
                put("publishedDisplay", item.publishedDisplay)
                put("publishedEpochMs", item.publishedEpochMs)
                put("videoUrl", item.videoUrl)
                put("languageCode", item.languageCode)
                put("verifiedPodcastLanguage", item.verifiedPodcastLanguage.id)
                put("categories", JSONArray(item.categories.map { it.id }))
            }
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun deserializeVideosJson(raw: String): List<YouTubeVideoItem> {
        val arr = JSONArray(raw)
        val result = ArrayList<YouTubeVideoItem>(arr.length())
        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            val id = obj.optString("id")
            if (!isValidVideoId(id)) continue
            val catArr = obj.optJSONArray("categories")
            val cats = mutableListOf<LectureCategory>()
            if (catArr != null) {
                for (c in 0 until catArr.length()) {
                    val catId = catArr.optString(c)
                    LectureCategory.entries.firstOrNull { it.id == catId }?.let { cats.add(it) }
                }
            }
            val title = obj.optString("title")
            val channel = obj.optString("channel")
            val storedPodcastLang = obj.optString("verifiedPodcastLanguage", "")
            val verifiedLang = PodcastLanguageSection.entries.firstOrNull { it.id == storedPodcastLang }
                ?: verifyPodcastLanguageFromContent(title, channel)

            result.add(
                YouTubeVideoItem(
                    id = id,
                    title = title,
                    channel = channel,
                    channelId = obj.optString("channelId"),
                    channelKey = obj.optString("channelKey"),
                    thumbnail = obj.optString("thumbnail"),
                    publishedIso = obj.optString("publishedIso"),
                    publishedDisplay = obj.optString("publishedDisplay"),
                    publishedEpochMs = obj.optLong("publishedEpochMs", 0L),
                    videoUrl = obj.optString("videoUrl", "https://www.youtube.com/watch?v=$id"),
                    languageCode = obj.optString("languageCode", "en"),
                    verifiedPodcastLanguage = verifiedLang,
                    categories = cats.ifEmpty { listOf(LectureCategory.LATEST_LECTURES) },
                    isFromCache = true,
                    isCacheExpired = false
                )
            )
        }
        return result
    }

    internal fun parseIsoDateEpoch(iso: String): Long {
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

    internal fun formatIsoDateShort(iso: String): String {
        if (iso.isBlank()) return ""
        return if (iso.length >= 10 && iso[4] == '-' && iso[7] == '-') {
            iso.substring(0, 10)
        } else {
            iso.take(16)
        }
    }
}
