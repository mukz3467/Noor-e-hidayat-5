package com.example.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.data.lectures.ChannelCatalogLoader
import com.example.data.lectures.ChannelDirectoryCoverageReport
import com.example.data.lectures.ChannelFeedHealth
import com.example.data.lectures.ChannelFeedStatus
import com.example.data.lectures.ChannelRegistryEntry
import com.example.data.lectures.ChannelType
import com.example.data.lectures.ChannelVerificationStatus
import com.example.data.lectures.DurationFilterPolicy
import com.example.data.lectures.DurationVerificationStatus
import com.example.data.lectures.LectureDurationConfig
import com.example.data.lectures.LectureLanguageGroup
import com.example.data.lectures.LoadedChannelCatalog
import com.example.data.lectures.VerifiedScholarRegistry
import com.example.data.lectures.YouTubeRssParser
import com.example.data.live.HarameynLiveSourceResolver
import com.example.data.live.HarameynLiveSources
import com.example.data.live.LiveSourceAttempt
import com.example.data.live.LiveSourceAttemptStatus
import com.example.data.live.ResolvedLiveStreamSource
import com.example.data.model.AppLanguage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

enum class LectureCategory(
    val id: String,
    val labelKey: String,
    val matchKeywords: List<String>,
    val exclusionKeywords: List<String> = emptyList()
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
            "tafseer", "tafsir", "surah", "sura", "surat", "ayat", "ayah", "ayahs", "deeper look",
            "bayan ul quran", "bayan-ul-quran", "tafheem", "parah", "para", "juz", "juzz", "quran",
            "qur'an", "exegesis", "commentary", "dars e quran", "dars-e-quran", "baqarah", "imran",
            "nisa", "maidah", "anam", "araf", "anfal", "tawbah", "yunus", "yusuf", "kahf", "maryam",
            "taha", "yaseen", "yasin", "rahman", "waqiah", "mulk", "fatiha", "fatihah", "ikhlas",
            "تفسير", "تفسیر", "سورة", "سورت", "قرآن", "القرآن", "بیان القرآن", "پارہ", "آیت",
            "آية", "آيات", "تدبر", "البيان القرآني", "لمسات بيانية", "درس قرآن", "تفهيم القرآن",
            "तफ़सीर", "क़ुरआन", "सूरह", "आयत"
        ),
        exclusionKeywords = listOf("#shorts", "trailer", "promo", "teaser")
    ),
    QURAN_EXPLANATION(
        id = "quran_explanation",
        labelKey = "lecture_cat_quran_explanation",
        matchKeywords = listOf(
            "tadabbur", "reflection", "reflections", "meaning", "message", "quran", "qur'an",
            "surah", "sura", "surat", "arabic", "vocabulary", "themes", "recitation", "tilawat",
            "tilawah", "qirat", "khatm", "khatam", "ayah", "ayat", "tajweed", "memorize", "hifz",
            "فہم", "فهم", "تدبر", "قرآن", "القرآن", "تلاوة", "تلاوت", "ترجمہ", "معانی", "هدايات",
            "ختمة", "قراءة", "سورة", "آيات", "وقفات", "تأملات",
            "क़ुरआन", "तदब्बुर", "अनुवाद", "तिलावत"
        ),
        exclusionKeywords = listOf("#shorts", "promo", "teaser")
    ),
    HADITH(
        id = "hadith",
        labelKey = "lecture_cat_hadith",
        matchKeywords = listOf(
            "hadith", "hadeeth", "ahadith", "bukhari", "muslim", "tirmidhi", "abu dawud", "nasai",
            "ibn majah", "muwatta", "musnad", "sunnah", "sunnat", "dars-e-hadith", "dars e hadith",
            "narrator", "narrators", "riyad", "riyadh", "nawawi", "forty hadith", "40 hadith",
            "shama'il", "shamail", "sahih", "isnad", "muhaddis", "mishkat", "bulugh",
            "صحيح", "صحیح", "حديث", "حدیث", "احادیث", "أحاديث", "سنت", "السنة", "بخاری", "بخاري",
            "مسلم", "الترمذي", "مسند", "رياض الصالحين", "الأربعين النووية", "شمائل", "درر", "الهدي النبوي",
            "हदीस", "बुख़ारी", "सुन्नत"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    SEERAH(
        id = "seerah",
        labelKey = "lecture_cat_seerah",
        matchKeywords = listOf(
            "seerah", "sirah", "seerat", "prophet muhammad", "messenger of allah", "rasool", "rasul",
            "nabi", "mustafa", "makkah", "mecca", "madinah", "medina", "hijrah", "hijrat", "isra",
            "miraj", "firsts", "farewell sermon", "prophetic", "huzoor", "khatm e nabuwwat", "nabuwat",
            "banu", "quraysh", "quraish", "hudaybiyyah", "khutbah", "milad", "mawlid",
            "سيرة", "السيرة", "سیرت", "النبي", "نبی", "الرسول", "رسول الله", "رسول اللہ", "ہجرت",
            "الهجرة", "معراج", "الإسراء", "مکہ", "مكة", "مدینہ", "المدينة", "المصطفى", "حضور",
            "सीरत", "पैग़ंबर मुहम्मद", "हिजरत", "रसूल"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    ISLAMIC_HISTORY(
        id = "islamic_history",
        labelKey = "lecture_cat_history",
        matchKeywords = listOf(
            "history", "historical", "tareekh", "tarikh", "ummah", "civilization", "jerusalem",
            "aqsa", "quds", "andalus", "cordoba", "granada", "baghdad", "damascus", "cairo",
            "golden age", "ottoman", "abbasid", "umayyad", "mamluk", "ayyubid", "seljuk", "mughal",
            "delhi sultanate", "sultanate", "empire", "dynasty", "salahuddin", "saladin", "fatimid",
            "caliph", "caliphate", "khilafat", "khilafah", "waqia", "waqiat", "karbala", "crusade",
            "mongol", "heritage", "muslim world", "conquest", "fath", "century", "ancient",
            "تاريخ", "التاريخ", "تاریخ", "امت", "الأمة", "الحضارة", "القدس", "الأندلس", "اسلامی تاریخ",
            "العثمانية", "الأموية", "العباسية", "صلاح الدين", "المسلمين", "العظماء", "السلاطين", "واقعہ",
            "واقعات", "تہذیب", "خلافت", "سلطنت", "فتوحات",
            "इस्लामी इतिहास", "इतिहास", "सभ्यता", "वाक़िआ"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    STORIES_OF_PROPHETS(
        id = "stories_of_prophets",
        labelKey = "lecture_cat_prophets",
        matchKeywords = listOf(
            "stories of prophets", "stories of the prophets", "prophets in islam", "prophetic stories",
            "stories of the messengers", "qasas", "story of", "prophet", "anbiya", "ambiya",
            "ibrahim", "abraham", "musa", "moses", "pharaoh", "firaun", "yusuf", "joseph",
            "nuh", "noah", "isa", "jesus", "maryam", "adam", "dawud", "david", "sulayman", "solomon",
            "yunus", "jonah", "ayyub", "job", "zakariya", "yahya", "ismail", "ishmael", "ishaq",
            "yaqub", "jacob", "hud", "salih", "lut", "shuayb", "idris", "khidr", "dhul qarnayn",
            "hazrat adam", "hazrat nuh", "hazrat ibrahim", "hazrat musa", "hazrat yusuf",
            "hazrat isa", "hazrat dawud", "hazrat sulayman", "hazrat yunus", "hazrat ayyub",
            "انبیاء", "قصص الانبیاء", "حضرت آدم", "حضرت نوح", "حضرت ابراہیم", "حضرت موسی",
            "حضرت یوسف", "حضرت عیسی", "حضرت داؤد", "حضرت سلیمان", "حضرت یونس", "حضرت ایوب",
            "حضرت اسماعیل", "حضرت یعقوب", "نبی", "فرعون",
            "قصص الأنبياء", "قصص الرسل", "الأنبياء", "قصة", "آدم", "نوح", "إبراهيم", "موسى",
            "يوسف", "عيسى", "مريم", "داود", "سليمان", "يونس", "أيوب", "إدريس", "سيدنا",
            "पैग़ंबर", "क़िस्से", "हज़रत आदम", "हज़रत इब्राहीम", "हज़रत मूसा", "हज़रत यूसुफ़"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    ISLAMIC_BATTLES(
        id = "islamic_battles",
        labelKey = "lecture_cat_battles",
        matchKeywords = listOf(
            "battles in islamic history", "islamic battles", "battle", "battles", "ghazawat",
            "ghazwah", "ghazwa", "badr", "uhud", "trench", "khandaq", "ahzab", "khaybar", "khaibar",
            "hudaybiyyah", "hudaybiyah", "conquest of makkah", "fath makkah", "tabuk", "mu'tah",
            "mutah", "hunayn", "hunain", "yarmouk", "yarmuk", "qadisiyyah", "qadisiyah", "hattin",
            "ain jalut", "nahawand", "karbala", "conquest", "crusade", "military", "war", "jihad",
            "martyr", "shahid", "shaheed", "victory", "fath", "expedition", "army", "khalid",
            "غزوات", "غزوہ", "جنگ", "جنگیں", "بدر", "احد", "خندق", "خیبر", "حدیبیہ", "فتح مکہ",
            "تبوک", "موتہ", "کربلا", "فتوحات", "شہادت", "جہاد", "حنین", "یرموک", "قادسیہ",
            "الغزوات", "غزوة", "المعارك", "معركة", "المعركة", "بدر", "أحد", "الخندق", "خيبر",
            "الحديبية", "فتح مكة", "تبوك", "مؤتة", "الفتوحات", "اليرموك", "القادسية", "حطين",
            "جهاد", "قتال", "القتال", "الجيش", "انتصار", "النصر", "الشهداء", "مقاتل",
            "ग़ज़वा", "बद्र", "उहुद", "फ़तह मक्का", "कर्बला", "इस्लामी युद्ध"
        ),
        exclusionKeywords = listOf("#shorts", "gaming", "pubg", "video game", "meme")
    ),
    SAHABA_STORIES(
        id = "sahaba_stories",
        labelKey = "lecture_cat_sahaba",
        matchKeywords = listOf(
            "stories of sahaba", "sahaba", "sahabah", "sahabi", "companions", "companion",
            "abu bakr", "siddiq", "umar", "farooq", "uthman", "usman", "ali ibn", "maula ali",
            "hazrat ali", "imam hussain", "hussain", "hasan", "bilal", "khalid ibn", "khalid bin",
            "abu ubaidah", "musab", "aisha", "khadijah", "khadija", "fatimah", "fatima",
            "abu hurairah", "salman al-farsi", "hamza", "zubair", "talhah", "saad ibn", "muadh",
            "amr ibn", "jabir", "ibn abbas", "ibn masud", "ahle bait", "ahlul bayt", "muawiya",
            "صحابہ", "صحابی", "حضرت ابو بکر", "صدیق اکبر", "حضرت عمر", "عمر فاروق", "حضرت عثمان",
            "حضرت علی", "مولا علی", "امام حسین", "حضرت بلال", "خالد بن ولید", "حضرت عائشہ",
            "حضرت خدیجہ", "حضرت فاطمہ", "اہل بیت", "عظمت صحابہ",
            "قصص الصحابة", "الصحابة", "صحابي", "أبو بكر", "الصديق", "عمر بن الخطاب", "الفاروق",
            "عثمان بن عفان", "علي بن أبي طالب", "بلال بن رباح", "خالد بن الوليد", "عائشة",
            "خديجة", "فاطمة", "الحسين", "الزبير", "معاذ بن جبل", "عمرو بن", "العشرة المبشرون",
            "सहाबा", "हज़रत अबू बक्र", "हज़रत उमर", "हज़रत उस्मान", "हज़रत अली", "इमाम हुसैन"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    ISLAMIC_CALIPHATES(
        id = "islamic_caliphates",
        labelKey = "lecture_cat_caliphates",
        matchKeywords = listOf(
            "caliph", "caliphate", "caliphates", "khilafat", "khilafah", "khulafa", "rashidun",
            "rightly guided", "umayyad", "abbasid", "ottoman", "andalus", "fatimid", "mamluk",
            "ayyubid", "sultanate", "sultan", "dynasty", "empire", "islamic civilization",
            "golden age", "governance", "shariah", "sharia", "nizam", "hukumat", "daur-e-hukumat",
            "revival", "ummah", "state", "imamate", "muawiya", "harun", "mutasim", "abdul hamid",
            "خلافت", "خلیفہ", "خلفاء", "راشدہ", "راشدین", "بنو امیہ", "بنو عباس", "عثمانیہ",
            "سلطنت", "حکومت", "نظام", "تہذیب", "اندلس", "امت", "اسلامی ریاست",
            "الخلافة", "خليفة", "الخلفاء", "الراشدة", "الراشدون", "الأموية", "الأمويون", "العباسية",
            "العثمانية", "الحضارة", "الأندلس", "الدولة", "الدول", "ولاية", "السلطان", "المعتصم",
            "ख़िलाफ़त", "ख़लीफ़ा", "ख़ुलाफ़ा-ए-राशिदीन", "इस्लामी सभ्यता", "सल्तनत"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    SALAH_WORSHIP(
        id = "salah_worship",
        labelKey = "lecture_cat_salah",
        matchKeywords = listOf(
            "salah", "salat", "prayer", "pray", "praying", "namaz", "wudu", "wudhu", "ghusl",
            "fasting", "ramadan", "ramazan", "roza", "zakat", "sadaqah", "charity", "hajj", "umrah",
            "dua", "dhikr", "zikr", "tasbeeh", "tasbih", "istighfar", "tahajjud", "fajr", "jummah",
            "jumuah", "jumma", "khutbah", "khutba", "taraweeh", "eid", "qurbani", "worship",
            "ibadah", "khushu", "humility", "azan", "azaan", "adhan", "masjid", "mosque",
            "صلاة", "الصلاة", "نماز", "عبادت", "العبادة", "دعاء", "دعا", "وضوء", "وضو", "رمضان",
            "روزہ", "صيام", "زکوٰۃ", "زكاة", "حج", "عمرة", "عمرہ", "العشاء", "المغرب", "الفجر",
            "الجمعة", "جمعه", "جمعہ", "تہجد", "تراویح", "ذکر", "ذكر", "استغفار", "تسبیح", "قربانی",
            "नमाज़", "रोज़ा", "ज़कात", "हज", "उमराह", "इबादत", "दुआ", "रमज़ान"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    ISLAMIC_MANNERS(
        id = "islamic_manners",
        labelKey = "lecture_cat_manners",
        matchKeywords = listOf(
            "akhlaq", "adab", "manners", "character", "patience", "sabr", "mercy", "tawbah",
            "taubah", "repentance", "forgiveness", "heart", "hearts", "soul", "sin", "sins",
            "sinning", "gunah", "humility", "mind", "charity", "sadness", "anxiety", "peace",
            "sukoon", "tazkiyah", "purification", "gratitude", "shukr", "sincerity", "ikhlas",
            "tawakkul", "trust allah", "love", "muhabbat", "mohabbat", "haya", "modesty", "honesty",
            "arrogance", "ego", "nafs", "nafsiyat", "akhirah", "akhirat", "jannah", "paradise",
            "death", "maut", "qabar", "islahi", "reminder", "bayan", "spiritual", "barakah",
            "اخلاق", "أخلاق", "صبر", "توبہ", "توبة", "قلب", "القلوب", "تواضع", "تزکیہ", "تزكية",
            "اصلاح", "شکر", "اخلاص", "محبت", "حیا", "گناہ", "آخرت", "جنت", "موت", "سکون", "تقوى",
            "الخشوع", "الإيمان", "موعظة", "النفس", "القيم", "غض البصر", "التفاؤل",
            "अख़लाक़", "सब्र", "तौबा", "चरित्र", "हया", "सुकून", "ईमान"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    FAMILY_SOCIETY(
        id = "family_society",
        labelKey = "lecture_cat_family",
        matchKeywords = listOf(
            "family", "marriage", "marry", "parents", "mother", "father", "children", "child",
            "kids", "parenting", "youth", "community", "wife", "husband", "nikah", "talaq",
            "divorce", "society", "home", "ghar", "women", "woman", "sister", "brother", "aurat",
            "biwi", "shohar", "miya", "shadi", "shaadi", "rishta", "waldain", "maa", "baap",
            "aulaad", "social media", "muashra", "huqooq", "rights", "relationship", "love",
            "نکاح", "طلاق", "والدین", "خاندان", "معاشرہ", "نوجوان", "گھر", "عورت", "بیوی", "شوہر",
            "شادی", "اولاد", "حقوق", "الأسرة", "الزواج", "الزوجة", "الطلاق", "الأولاد", "الشباب",
            "المجتمع", "الوالدين", "الأم", "التربية", "المرأة", "الخطبة",
            "परिवार", "निकाह", "माता-पिता", "समाज", "शादी", "औरत", "बच्चे"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    NEW_MUSLIM_EDUCATION(
        id = "new_muslim_education",
        labelKey = "lecture_cat_new_muslim",
        matchKeywords = listOf(
            "revert", "convert", "new muslim", "basics", "pillar", "pillars", "five pillars",
            "islam", "faith", "aqeedah", "aqidah", "aqeeda", "tawheed", "tawhid", "shahada",
            "shahadah", "belief", "certainty", "yaqeen", "introduction to islam", "dawah", "da'wah",
            "christian", "bible", "atheist", "atheism", "doubt", "doubts", "shirk", "kufr",
            "truth", "haqq", "purpose of life", "who is allah", "god", "deen", "iman", "imaan",
            "qiyamah", "qiyamat", "day of judgment", "hereafter", "angels", "taqdeer", "qadr",
            "عقيدة", "العقيدة", "عقیدہ", "توحید", "التوحيد", "ایمان", "اسلام", "الإسلام", "الإيمان",
            "أركان الإسلام", "دعوة", "شبهات", "ملحد", "الإلحاد", "شرك", "شرک", "دین", "تقدیر",
            "قیامت", "اللہ", "الله",
            "ईमान", "तौहीद", "इस्लाम", "अक़ीदा", "दीन"
        ),
        exclusionKeywords = listOf("#shorts")
    ),
    ISLAMIC_QA(
        id = "islamic_qa",
        labelKey = "lecture_cat_qa",
        matchKeywords = listOf(
            "q&a", "qa", "question", "questions", "ask", "fatwa", "fatawa", "ruling", "rulings",
            "halal", "haram", "haraam", "permissible", "allowed", "jaiz", "najaiz", "masail",
            "masla", "masala", "must i", "is it", "is there", "can a", "can muslims", "how does",
            "why", "kya", "kia", "kaisa", "kaisi", "sahi hai", "darul ifta", "darulifta",
            "askzad", "fiqh", "shariah", "bidah", "bid'ah", "sunni", "hanafi", "shafii", "maliki",
            "hanbali", "mufti", "jawab", "sawal", "crypto", "interest", "riba", "zakat",
            "هل ", "ما حكم", "حكم", "مسئلہ", "مسائل", "سوال", "جواب", "فتوى", "فتاوى", "فتوی",
            "يستفتونك", "حلال", "حرام", "فقہ", "الفقه", "فقه", "جائز", "ناجائز", "دار الافتاء",
            "مفتی", "مفتي", "أحكام",
            "सवाल", "जवाब", "मसाइल", "फ़तवा", "हलाल", "हराम", "जायज़"
        ),
        exclusionKeywords = listOf("#shorts")
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
    CONNECTING,
    RECONNECTING,
    LIVE_CONFIRMED,
    LIVE_STATUS_UNCONFIRMED,
    NO_ACTIVE_BROADCAST,
    SOURCE_UNAVAILABLE,
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
    val primaryLanguageGroup: LectureLanguageGroup = LectureLanguageGroup.fromCode(languageCode),
    val secondaryLanguageGroups: List<LectureLanguageGroup> = emptyList(),
    val primaryCategories: List<LectureCategory>,
    val podcastLanguage: PodcastLanguageSection? = null,
    val podcastTopics: List<PodcastTopicFilter> = emptyList(),
    val verificationStatus: ChannelVerificationStatus = ChannelVerificationStatus.VERIFIED,
    val officialWebsite: String = "",
    val verificationSource: String = "",
    val channelType: ChannelType = ChannelType.SCHOLAR,
    val priority: Int = 50,
    val notes: String = ""
) {
    val feedUrl: String
        get() = "https://www.youtube.com/feeds/videos.xml?channel_id=$channelId"

    val officialChannelUrl: String
        get() = "https://www.youtube.com/channel/$channelId"

    fun supportsLanguageGroup(group: LectureLanguageGroup): Boolean {
        if (group == LectureLanguageGroup.ALL) return true
        return primaryLanguageGroup == group || secondaryLanguageGroups.contains(group)
    }

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
    val updatedIso: String = "",
    val publishedDisplay: String = "",
    val publishedEpochMs: Long = 0L,
    val videoUrl: String = "https://www.youtube.com/watch?v=$id",
    val description: String = "",
    val languageCode: String = "und",
    val languageGroup: LectureLanguageGroup = LectureLanguageGroup.fromCode(languageCode),
    val verifiedPodcastLanguage: PodcastLanguageSection = PodcastLanguageSection.LANGUAGE_NOT_VERIFIED,
    val categories: List<LectureCategory> = listOf(LectureCategory.LATEST_LECTURES),
    val durationSeconds: Int? = null,
    val durationStatus: DurationVerificationStatus = DurationVerificationStatus.UNVERIFIED_FROM_RSS,
    val viewCount: Long? = null,
    val isFromCache: Boolean = false,
    val isCacheExpired: Boolean = false
)

data class LectureFeedResult(
    val videos: List<YouTubeVideoItem>,
    val status: FeedLoadStatus,
    val unavailableChannels: List<String> = emptyList(),
    val lastUpdatedMs: Long = 0L,
    val isCacheExpired: Boolean = false,
    val errorMessage: String? = null,
    val loadedChannelCount: Int = 0,
    val totalMatchingChannels: Int = 0,
    val hasMoreChannelBatches: Boolean = false,
    val filteredShortsCount: Int = 0,
    val unverifiedDurationCount: Int = 0,
    val confirmedLongFormCount: Int = 0
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
    val checkedAtMs: Long = System.currentTimeMillis(),
    val resolvedSource: ResolvedLiveStreamSource? = null,
    val sourceAttempts: List<LiveSourceAttempt> = emptyList()
)

/**
 * Zero-API-key YouTube repository backed strictly by official public YouTube Atom RSS feeds:
 * https://www.youtube.com/feeds/videos.xml?channel_id=CHANNEL_ID
 *
 * Architecture highlights:
 * - Expandable offline JSON channel catalog (`assets/lectures/channel_catalog_v1.json`) supporting 300+ channels
 *   with strict separation between VERIFIED, CANDIDATE, REJECTED, TEMPORARILY_UNAVAILABLE, and INVALID_RSS.
 * - Controlled batching (batch size 10) + bounded concurrency (max 5 simultaneous RSS requests) +
 *   per-channel feed health tracking and capped exponential backoff.
 * - Combined Urdu/Hindi, English, and Arabic language filtering wired directly into channel selection & item matching.
 * - Honest long-form duration policy (default 480s / 8 min): rejects confirmed Shorts (< 480s or /shorts/ URL),
 *   NEVER invents duration when omitted by YouTube RSS, and caches real player-verified durations once played.
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
    private const val KEY_VERIFIED_DURATIONS_JSON = "verified_video_durations_v1"
    internal const val CACHE_TTL_MS = 30 * 60 * 1000L
    private const val MAX_RECENT_WATCHED = 25
    private const val KEY_LECTURE_BG_SWEEP_OFFSET = "lecture_bg_sweep_offset_v1"
    const val MAX_CUMULATIVE_VIDEOS_PER_CHANNEL = 120
    const val DEFAULT_BG_SWEEP_BATCH_SIZE = 55

    const val DEFAULT_CHANNEL_BATCH_SIZE = 10
    const val MAX_CONCURRENT_RSS_REQUESTS = 8
    private const val MAX_BACKOFF_FAILURES = 4
    private const val BASE_BACKOFF_MS = 30_000L
    private const val MAX_BACKOFF_MS = 5 * 60 * 1000L

    internal var httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    internal var networkChecker: ((Context) -> Boolean)? = null
    internal var currentTimeProvider: (() -> Long)? = null

    private fun nowMs(): Long = currentTimeProvider?.invoke() ?: System.currentTimeMillis()

    private val memoryCache = ConcurrentHashMap<String, Pair<Long, List<YouTubeVideoItem>>>()
    private val inFlightRequests = ConcurrentHashMap<String, Deferred<ChannelFetchOutcome>>()
    private val channelHealthMap = ConcurrentHashMap<String, ChannelFeedHealth>()
    private val verifiedDurationCache = ConcurrentHashMap<String, Int>()
    private val cacheGeneration = AtomicLong(0L)
    private val rssConcurrencyLimiter = Semaphore(MAX_CONCURRENT_RSS_REQUESTS)

    /**
     * Live-verified Islamic scholar & educational publisher channels (325 total: 108 Urdu/Hindi,
     * 109 English, 108 Arabic — every UC Channel ID verified via YouTube & live HTTP 200 Atom RSS).
     */
    val VERIFIED_CHANNELS: List<VerifiedIslamicChannel> = VerifiedScholarRegistry.VERIFIED_CHANNELS

    internal val SEED_VERIFIED_ENTRIES: List<ChannelRegistryEntry> by lazy {
        VERIFIED_CHANNELS.map { ch ->
            ChannelRegistryEntry(
                channelId = ch.key,
                channelName = ch.nameEn,
                scholarOrOrgName = ch.nameEn,
                youtubeChannelId = ch.channelId,
                handle = ch.handle,
                channelUrl = ch.officialChannelUrl,
                rssUrl = ch.feedUrl,
                nameEn = ch.nameEn,
                nameUr = ch.nameUr,
                nameAr = ch.nameAr,
                nameHi = ch.nameHi,
                specialtyEn = ch.specialtyEn,
                specialtyUr = ch.specialtyUr,
                specialtyAr = ch.specialtyAr,
                specialtyHi = ch.specialtyHi,
                primaryLanguage = ch.primaryLanguageGroup,
                secondaryLanguages = ch.secondaryLanguageGroups,
                topics = ch.primaryCategories.map { it.id },
                channelType = ch.channelType,
                verificationStatus = ChannelVerificationStatus.VERIFIED,
                verificationSource = ch.verificationSource,
                officialWebsite = ch.officialWebsite,
                lastVerifiedAt = "2026-11-19",
                feedStatus = ChannelFeedStatus.HEALTHY,
                duplicateDetectionKey = "uc:${ch.channelId.lowercase(Locale.ROOT)}",
                notes = ch.notes,
                enabled = true,
                priority = ch.priority,
                podcastLanguage = ch.podcastLanguage,
                podcastTopics = ch.podcastTopics
            )
        }
    }

    /**
     * Explicit topic-to-channel mapping covering verified Urdu/Hindi, English, and Arabic scholars
     * across all 16 Islamic lecture topics.
     */
    val TOPIC_CHANNEL_MAPPING: Map<LectureCategory, List<String>> =
        VerifiedScholarRegistry.TOPIC_CHANNEL_MAPPING

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

    fun isValidChannelId(channelId: String): Boolean = YouTubeRssParser.isValidChannelId(channelId)

    fun isValidVideoId(videoId: String): Boolean = YouTubeRssParser.isValidVideoId(videoId)

    fun getChannelCatalog(context: Context? = null, forceReload: Boolean = false): LoadedChannelCatalog =
        ChannelCatalogLoader.load(context, forceReload)

    fun getDirectoryCoverageReport(context: Context? = null): ChannelDirectoryCoverageReport =
        getChannelCatalog(context).coverageReport

    fun getVerifiedChannels(context: Context? = null): List<VerifiedIslamicChannel> {
        val loaded = getChannelCatalog(context).verifiedEntries.map { it.toVerifiedIslamicChannel() }
        return if (loaded.isNotEmpty()) loaded else VERIFIED_CHANNELS
    }

    fun verifiedChannelsForLanguage(
        languageGroup: LectureLanguageGroup,
        context: Context? = null
    ): List<VerifiedIslamicChannel> {
        val all = getVerifiedChannels(context)
        if (languageGroup == LectureLanguageGroup.ALL) return all
        return all.filter { it.supportsLanguageGroup(languageGroup) }
    }

    fun getChannelHealth(channelId: String): ChannelFeedHealth =
        channelHealthMap[channelId] ?: ChannelFeedHealth(channelId = channelId)

    /**
     * Resolves verified channels for [topic], optionally scoped to [languageGroup].
     * Combines explicit [channelMapping] with any dynamically added verified channels in the
     * expandable catalog whose `topics` list includes [topic].
     */
    fun channelsForTopic(
        topic: LectureCategory?,
        channelMapping: Map<LectureCategory, List<String>> = TOPIC_CHANNEL_MAPPING,
        languageGroup: LectureLanguageGroup = LectureLanguageGroup.ALL,
        context: Context? = null
    ): List<VerifiedIslamicChannel>? {
        if (topic == null) return null
        val verifiedPool = getVerifiedChannels(context)
        val mappedKeys = channelMapping[topic] ?: return null
        if (mappedKeys.isEmpty()) return null

        val explicitResolved = mappedKeys.mapNotNull { key ->
            verifiedPool.firstOrNull { it.key == key && isValidChannelId(it.channelId) }
        }
        if (explicitResolved.size != mappedKeys.size || explicitResolved.isEmpty()) {
            return null
        }

        // Also include any additional verified channels from the expandable JSON catalog tagged with this topic
        val catalogExtras = if (topic == LectureCategory.LATEST_LECTURES) {
            emptyList()
        } else {
            verifiedPool.filter { ch ->
                ch.primaryCategories.contains(topic) && explicitResolved.none { it.key == ch.key }
            }
        }

        // Preserve curated topic-first order for explicit mappings (so dedicated channels like
        // The Islamic History appear first in Islamic History and Bayyinah/Dr. Israr appear first in Tafseer),
        // then append additional catalog channels tagged with this topic sorted by priority.
        val combined = (explicitResolved + catalogExtras.sortedByDescending { it.priority })
            .distinctBy { it.channelId }

        return if (languageGroup == LectureLanguageGroup.ALL) {
            combined
        } else {
            combined.filter { it.supportsLanguageGroup(languageGroup) }
        }
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
     * Topic-based lecture fetch with:
     * - Language-aware verified channel selection (never fetches unrelated channels or unverified candidates).
     * - Controlled channel batching (default 10 channels per batch) and bounded concurrency (max 5 simultaneous).
     * - Single-pass accurate topic filtering (no destructive double-filtering).
     * - Centralized long-form duration policy (default 480s / 8 min; excludes confirmed Shorts; honest about unverified RSS duration).
     */
    suspend fun fetchLecturesForTopic(
        context: Context,
        topic: LectureCategory?,
        channelKey: String? = null,
        forceRefresh: Boolean = false,
        channelMapping: Map<LectureCategory, List<String>> = TOPIC_CHANNEL_MAPPING,
        languageGroup: LectureLanguageGroup = LectureLanguageGroup.ALL,
        minDurationSeconds: Int = LectureDurationConfig.MIN_LECTURE_DURATION_SECONDS,
        durationPolicy: DurationFilterPolicy = DurationFilterPolicy.EXCLUDE_CONFIRMED_SHORTS_SHOW_UNVERIFIED_WITH_BADGE,
        batchIndex: Int = 0,
        batchSize: Int = DEFAULT_CHANNEL_BATCH_SIZE
    ): LectureFeedResult = withContext(Dispatchers.IO) {
        ensureVerifiedDurationsLoaded(context)

        val allMatchingTargets = if (!channelKey.isNullOrBlank()) {
            val single = getVerifiedChannels(context).firstOrNull {
                it.key == channelKey && isValidChannelId(it.channelId)
            }
            if (single == null) {
                return@withContext LectureFeedResult(
                    videos = emptyList(),
                    status = FeedLoadStatus.INVALID_MAPPING,
                    errorMessage = "Invalid or unknown scholar channel mapping: $channelKey"
                )
            }
            listOf(single)
        } else {
            val resolved = channelsForTopic(
                topic = topic,
                channelMapping = channelMapping,
                languageGroup = languageGroup,
                context = context
            ) ?: return@withContext LectureFeedResult(
                videos = emptyList(),
                status = FeedLoadStatus.INVALID_MAPPING,
                errorMessage = "Missing or invalid topic channel mapping."
            )
            if (resolved.isEmpty()) {
                return@withContext LectureFeedResult(
                    videos = emptyList(),
                    status = FeedLoadStatus.ONLINE_FRESH,
                    loadedChannelCount = 0,
                    totalMatchingChannels = 0,
                    hasMoreChannelBatches = false
                )
            }
            resolved
        }

        val safeBatchSize = batchSize.coerceAtLeast(1)
        val endExclusive = ((batchIndex.coerceAtLeast(0) + 1) * safeBatchSize).coerceAtMost(allMatchingTargets.size)
        val batchTargets = allMatchingTargets.take(endExclusive)
        val hasMoreBatches = endExclusive < allMatchingTargets.size

        val online = isNetworkAvailable(context)
        val outcomes = coroutineScope {
            batchTargets.map { channel ->
                async {
                    rssConcurrencyLimiter.withPermit {
                        fetchSingleChannelFeed(
                            context = context,
                            channel = channel,
                            forceRefresh = forceRefresh,
                            isOnline = online,
                            minDurationSeconds = minDurationSeconds
                        )
                    }
                }
            }.awaitAll()
        }

        // Re-evaluate duration status with current threshold & player-verified cache
        val rawCombined = outcomes
            .flatMap { it.items }
            .distinctBy { it.id }
            .map { item -> applyDurationPolicyMetadata(item, minDurationSeconds) }
            .sortedByDescending { it.publishedEpochMs }

        // Single-pass topic filter
        val topicFiltered = if (!channelKey.isNullOrBlank() || topic == null ||
            topic == LectureCategory.LATEST_LECTURES || topic == LectureCategory.POPULAR_SCHOLARS
        ) {
            rawCombined
        } else {
            rawCombined.filter { it.categories.contains(topic) }
        }

        // Language group filter (never mixes English into Urdu/Hindi or Arabic unless ALL is selected)
        val languageFiltered = if (languageGroup == LectureLanguageGroup.ALL) {
            topicFiltered
        } else {
            topicFiltered.filter { it.languageGroup == languageGroup }
        }

        // Long-form duration policy filter (rejects all confirmed Shorts < minDurationSeconds or /shorts/ links)
        val filteredShortsCount = languageFiltered.count {
            it.durationStatus == DurationVerificationStatus.CONFIRMED_SHORT_REJECTED
        }
        val durationEligible = languageFiltered.filter { item ->
            LectureDurationConfig.isEligibleForDisplay(item.durationStatus, durationPolicy)
        }
        val confirmedLongCount = durationEligible.count {
            it.durationStatus == DurationVerificationStatus.CONFIRMED_LONG_FORM
        }
        val unverifiedDurationCount = durationEligible.count {
            it.durationStatus == DurationVerificationStatus.UNVERIFIED_FROM_RSS
        }

        buildFeedResult(
            videos = durationEligible,
            outcomes = outcomes,
            online = online,
            hadAnyRawItems = rawCombined.isNotEmpty(),
            loadedChannelCount = batchTargets.size,
            totalMatchingChannels = allMatchingTargets.size,
            hasMoreChannelBatches = hasMoreBatches,
            filteredShortsCount = filteredShortsCount,
            unverifiedDurationCount = unverifiedDurationCount,
            confirmedLongFormCount = confirmedLongCount
        )
    }

    /**
     * Fetches official Atom XML feeds for a specific verified channel key or all verified channels
     * using bounded concurrency.
     */
    suspend fun fetchVerifiedChannelFeeds(
        context: Context,
        channelKey: String? = null,
        forceRefresh: Boolean = false
    ): LectureFeedResult = withContext(Dispatchers.IO) {
        val verified = getVerifiedChannels(context)
        val targets = if (channelKey.isNullOrBlank()) {
            verified
        } else {
            val matched = verified.filter { it.key == channelKey }
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
                async {
                    rssConcurrencyLimiter.withPermit {
                        fetchSingleChannelFeed(context, channel, forceRefresh, online)
                    }
                }
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
            hadAnyRawItems = combined.isNotEmpty(),
            loadedChannelCount = targets.size,
            totalMatchingChannels = targets.size,
            hasMoreChannelBatches = false
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
                async {
                    rssConcurrencyLimiter.withPermit {
                        fetchSingleChannelFeed(context, channel, forceRefresh, online)
                    }
                }
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
            languageFiltered.filter { item ->
                val lower = item.title.lowercase(Locale.ROOT)
                topicFilter.matchKeywords.any { kw -> lower.contains(kw) }
            }
        }

        buildFeedResult(
            videos = topicFiltered,
            outcomes = outcomes,
            online = online,
            hadAnyRawItems = allItems.isNotEmpty(),
            loadedChannelCount = targets.size,
            totalMatchingChannels = targets.size,
            hasMoreChannelBatches = false
        )
    }

    private fun applyDurationPolicyMetadata(
        item: YouTubeVideoItem,
        minDurationSeconds: Int
    ): YouTubeVideoItem {
        val knownDuration = item.durationSeconds ?: verifiedDurationCache[item.id]
        val isShortsUrl = item.videoUrl.contains("/shorts/", ignoreCase = true) ||
            item.durationStatus == DurationVerificationStatus.CONFIRMED_SHORT_REJECTED && knownDuration == null
        val evaluated = LectureDurationConfig.evaluateDuration(
            durationSeconds = knownDuration,
            isShortsUrl = isShortsUrl,
            minDurationSeconds = minDurationSeconds
        )
        return item.copy(
            durationSeconds = knownDuration,
            durationStatus = evaluated
        )
    }

    private fun buildFeedResult(
        videos: List<YouTubeVideoItem>,
        outcomes: List<ChannelFetchOutcome>,
        online: Boolean,
        hadAnyRawItems: Boolean,
        loadedChannelCount: Int = outcomes.size,
        totalMatchingChannels: Int = outcomes.size,
        hasMoreChannelBatches: Boolean = false,
        filteredShortsCount: Int = 0,
        unverifiedDurationCount: Int = 0,
        confirmedLongFormCount: Int = 0
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
            errorMessage = errorDetail,
            loadedChannelCount = loadedChannelCount,
            totalMatchingChannels = totalMatchingChannels,
            hasMoreChannelBatches = hasMoreChannelBatches,
            filteredShortsCount = filteredShortsCount,
            unverifiedDurationCount = unverifiedDurationCount,
            confirmedLongFormCount = confirmedLongFormCount
        )
    }

    private suspend fun fetchSingleChannelFeed(
        context: Context,
        channel: VerifiedIslamicChannel,
        forceRefresh: Boolean,
        isOnline: Boolean,
        minDurationSeconds: Int = LectureDurationConfig.MIN_LECTURE_DURATION_SECONDS
    ): ChannelFetchOutcome {
        if (!isValidChannelId(channel.channelId)) {
            recordChannelFailure(channel.channelId, "Invalid channel ID format", ChannelFeedStatus.INVALID_RSS)
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

        val startGen = cacheGeneration.get()
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
                if (cacheGeneration.get() == startGen) {
                    memoryCache[channel.channelId] = diskCached.first to cachedList
                }
                return ChannelFetchOutcome(
                    channel = channel,
                    items = cachedList,
                    fetchedOnline = false,
                    cacheTimestampMs = diskCached.first,
                    isExpiredCache = false,
                    unavailable = false
                )
            }

            // Respect exponential backoff cooldown for repeatedly failing channels (unless forceRefresh)
            val health = channelHealthMap[channel.channelId]
            if (health != null && health.isInBackoff(now)) {
                val fallbackCache = loadChannelCache(context, channel.channelId)
                val expired = fallbackCache.first > 0L && (now - fallbackCache.first) >= CACHE_TTL_MS
                return ChannelFetchOutcome(
                    channel = channel,
                    items = fallbackCache.second.map { it.copy(isFromCache = true, isCacheExpired = expired) },
                    fetchedOnline = false,
                    cacheTimestampMs = fallbackCache.first,
                    isExpiredCache = expired && fallbackCache.second.isNotEmpty(),
                    unavailable = true,
                    errorDetail = health.lastErrorMessage ?: "Channel temporarily in backoff cooldown."
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
                        val parsed = parseAtomFeedXml(
                            xml = fetchResult.body,
                            channel = channel,
                            minDurationSeconds = minDurationSeconds
                        )
                        if (parsed.isNotEmpty()) {
                            val timestamp = nowMs()
                            // Cumulatively merge newly discovered long-form lectures with previously cached
                            // verified uploads for this channel (newest first, deduplicated by videoId,
                            // zero confirmed Shorts) so every 30-minute refresh grows the real library.
                            val priorCached = loadChannelCache(context, channel.channelId).second
                                .map { applyDurationPolicyMetadata(it, minDurationSeconds) }
                                .filter { it.durationStatus != DurationVerificationStatus.CONFIRMED_SHORT_REJECTED }
                            val mergedForCache = (parsed + priorCached)
                                .distinctBy { it.id }
                                .filter { it.durationStatus != DurationVerificationStatus.CONFIRMED_SHORT_REJECTED }
                                .sortedByDescending { it.publishedEpochMs }
                                .take(MAX_CUMULATIVE_VIDEOS_PER_CHANNEL)
                            val outcomeItems = (parsed + priorCached)
                                .distinctBy { it.id }
                                .sortedByDescending { it.publishedEpochMs }
                                .take(MAX_CUMULATIVE_VIDEOS_PER_CHANNEL)
                            if (cacheGeneration.get() == startGen) {
                                memoryCache[channel.channelId] = timestamp to mergedForCache
                                saveChannelCache(context, channel.channelId, timestamp, mergedForCache)
                                recordChannelSuccess(channel.channelId, timestamp)
                            }
                            ChannelFetchOutcome(
                                channel = channel,
                                items = outcomeItems,
                                fetchedOnline = true,
                                cacheTimestampMs = timestamp,
                                isExpiredCache = false,
                                unavailable = false
                            )
                        } else {
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
                        val feedStatus = when {
                            fetchResult.httpCode == 429 -> ChannelFeedStatus.RATE_LIMITED
                            fetchResult.httpCode == 404 || fetchResult.httpCode == 410 -> ChannelFeedStatus.INVALID_RSS
                            else -> ChannelFeedStatus.TEMPORARILY_UNAVAILABLE
                        }
                        recordChannelFailure(channel.channelId, fetchResult.errorMessage, feedStatus)
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
                } catch (ce: CancellationException) {
                    throw ce
                } finally {
                    inFlightRequests.remove(channel.channelId)
                }
            }
            inFlightRequests[channel.channelId] = deferred
            deferred.await()
        }
    }

    data class LectureCatalogRefreshSummary(
        val channelsSwept: Int,
        val totalCatalogChannels: Int,
        val totalAccumulatedLectures: Int,
        val newVideosAdded: Int
    )

    /**
     * Sweeps a rotating batch of stale verified channels across the full 325+ Islamic Lectures
     * catalog every ~30 minutes, cumulatively merging newly published real long-form lectures
     * (never Shorts, never fake videos) into per-channel caches.
     */
    suspend fun refreshCumulativeLectureCatalog(
        context: Context,
        batchSize: Int = DEFAULT_BG_SWEEP_BATCH_SIZE,
        forceAllChannels: Boolean = false
    ): LectureCatalogRefreshSummary = withContext(Dispatchers.IO) {
        ensureVerifiedDurationsLoaded(context)
        val allChannels = getVerifiedChannels(context).filter { isValidChannelId(it.channelId) }
        if (allChannels.isEmpty() || !isNetworkAvailable(context)) {
            return@withContext LectureCatalogRefreshSummary(
                channelsSwept = 0,
                totalCatalogChannels = allChannels.size,
                totalAccumulatedLectures = getAccumulatedLectureCount(context),
                newVideosAdded = 0
            )
        }

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val priorTotal = getAccumulatedLectureCount(context)
        val targets = if (forceAllChannels) {
            allChannels
        } else {
            val offset = prefs.getInt(KEY_LECTURE_BG_SWEEP_OFFSET, 0) % allChannels.size
            val slice = (0 until minOf(batchSize.coerceAtLeast(1), allChannels.size)).map { idx ->
                allChannels[(offset + idx) % allChannels.size]
            }
            val nextOffset = (offset + slice.size) % allChannels.size
            prefs.edit().putInt(KEY_LECTURE_BG_SWEEP_OFFSET, nextOffset).apply()
            slice
        }

        val outcomes = coroutineScope {
            targets.map { channel ->
                async {
                    rssConcurrencyLimiter.withPermit {
                        fetchSingleChannelFeed(
                            context = context,
                            channel = channel,
                            forceRefresh = false,
                            isOnline = true,
                            minDurationSeconds = LectureDurationConfig.MIN_LECTURE_DURATION_SECONDS
                        )
                    }
                }
            }.awaitAll()
        }

        val sweptOnline = outcomes.count { it.fetchedOnline }
        val newTotal = getAccumulatedLectureCount(context)
        LectureCatalogRefreshSummary(
            channelsSwept = sweptOnline,
            totalCatalogChannels = allChannels.size,
            totalAccumulatedLectures = newTotal,
            newVideosAdded = (newTotal - priorTotal).coerceAtLeast(0)
        )
    }

    /**
     * Counts all unique verified non-short lectures currently accumulated across all verified channels.
     */
    fun getAccumulatedLectureCount(context: Context): Int {
        val seenIds = HashSet<String>()
        val channels = getVerifiedChannels(context)
        for (ch in channels) {
            val items = memoryCache[ch.channelId]?.second ?: loadChannelCache(context, ch.channelId).second
            for (item in items) {
                if (item.durationStatus != DurationVerificationStatus.CONFIRMED_SHORT_REJECTED) {
                    seenIds.add(item.id)
                }
            }
        }
        return seenIds.size
    }

    private fun recordChannelSuccess(channelId: String, timestampMs: Long) {
        channelHealthMap[channelId] = ChannelFeedHealth(
            channelId = channelId,
            lastFeedSuccessAtMs = timestampMs,
            lastErrorAtMs = 0L,
            consecutiveFailures = 0,
            backoffUntilMs = 0L,
            lastErrorMessage = null,
            feedStatus = ChannelFeedStatus.HEALTHY
        )
    }

    private fun recordChannelFailure(channelId: String, error: String?, status: ChannelFeedStatus) {
        val now = nowMs()
        val prev = channelHealthMap[channelId]
        val failures = ((prev?.consecutiveFailures ?: 0) + 1).coerceAtMost(MAX_BACKOFF_FAILURES)
        val exponent = (failures - 1).coerceAtLeast(0)
        val backoffMs = (BASE_BACKOFF_MS shl exponent).coerceAtMost(MAX_BACKOFF_MS)
        channelHealthMap[channelId] = ChannelFeedHealth(
            channelId = channelId,
            lastFeedSuccessAtMs = prev?.lastFeedSuccessAtMs ?: 0L,
            lastErrorAtMs = now,
            consecutiveFailures = failures,
            backoffUntilMs = now + backoffMs,
            lastErrorMessage = error,
            feedStatus = status
        )
    }

    private data class HttpFetchResult(
        val body: String? = null,
        val errorMessage: String? = null,
        val httpCode: Int? = null
    )

    private fun executeGetWithRetry(url: String, maxAttempts: Int = 2): HttpFetchResult {
        var lastErr = "Request failed"
        var lastCode: Int? = null
        repeat(maxAttempts) { attempt ->
            try {
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
                httpClient.newCall(request).execute().use { response ->
                    lastCode = response.code
                    if (response.isSuccessful) {
                        return HttpFetchResult(body = response.body?.string(), httpCode = response.code)
                    }
                    lastErr = "HTTP ${response.code}"
                    Log.w(TAG, "RSS request returned HTTP ${response.code}")
                    if (response.code == 404 || response.code == 410 || response.code == 429 || response.code in 400..499) {
                        return HttpFetchResult(body = null, errorMessage = lastErr, httpCode = response.code)
                    }
                }
            } catch (e: Exception) {
                lastErr = e::class.java.simpleName
                Log.w(TAG, "RSS request attempt ${attempt + 1} failed: ${e::class.java.simpleName}")
                if (attempt == maxAttempts - 1) {
                    return HttpFetchResult(body = null, errorMessage = lastErr, httpCode = lastCode)
                }
            }
        }
        return HttpFetchResult(body = null, errorMessage = lastErr, httpCode = lastCode)
    }

    /**
     * Parses an official YouTube Atom XML feed (`https://www.youtube.com/feeds/videos.xml?channel_id=...`).
     */
    fun parseAtomFeedXml(
        xml: String,
        channel: VerifiedIslamicChannel? = null,
        minDurationSeconds: Int = LectureDurationConfig.MIN_LECTURE_DURATION_SECONDS
    ): List<YouTubeVideoItem> = YouTubeRssParser.parseAtomFeedXml(
        xml = xml,
        channel = channel,
        minDurationSeconds = minDurationSeconds,
        verifiedDurationLookup = { vid -> verifiedDurationCache[vid] }
    )

    fun verifyPodcastLanguageFromContent(
        title: String,
        channelName: String = ""
    ): PodcastLanguageSection = YouTubeRssParser.verifyPodcastLanguageFromContent(title, channelName)

    fun detectVideoLanguageGroup(
        title: String,
        description: String = "",
        channel: VerifiedIslamicChannel? = null
    ): LectureLanguageGroup = YouTubeRssParser.detectVideoLanguageGroup(title, description, channel)

    internal fun classifyLectureCategories(
        title: String,
        channel: VerifiedIslamicChannel?,
        description: String = ""
    ): List<LectureCategory> = YouTubeRssParser.classifyLectureCategories(title, description, channel)

    /**
     * Searches ONLY across real verified feed items (scholar, title, description, channel, category keywords, language).
     * Never returns fake unrelated fallback videos when nothing matches.
     */
    fun searchLocalVerifiedLectures(
        items: List<YouTubeVideoItem>,
        query: String = "",
        category: LectureCategory? = null,
        channelKey: String? = null,
        languageFilter: String? = null,
        languageGroupFilter: LectureLanguageGroup? = null
    ): List<YouTubeVideoItem> {
        val trimmed = query.trim().lowercase(Locale.ROOT)
        val effectiveGroup = languageGroupFilter ?: when {
            languageFilter.isNullOrBlank() -> null
            else -> LectureLanguageGroup.fromCode(languageFilter)
        }
        return items.filter { item ->
            val matchesChannel = channelKey.isNullOrBlank() || item.channelKey == channelKey
            val matchesLang = when (effectiveGroup) {
                null, LectureLanguageGroup.ALL -> true
                else -> item.languageGroup == effectiveGroup
            }
            val matchesCat = when (category) {
                null, LectureCategory.LATEST_LECTURES, LectureCategory.POPULAR_SCHOLARS -> true
                else -> item.categories.contains(category)
            }
            if (!matchesChannel || !matchesLang || !matchesCat) return@filter false

            if (trimmed.isEmpty()) return@filter true

            val channelMeta = VERIFIED_CHANNELS.firstOrNull { it.key == item.channelKey }
            val searchableFields = listOfNotNull(
                item.title,
                item.description,
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
     * Checks live broadcast availability for Makkah (Masjid al-Haram) or Madinah (Masjid an-Nabawi)
     * across the ordered 3-source chain (Official Saudi YouTube -> Quran Hayat HLS -> Haramain Voice),
     * plus recent recorded official RSS uploads kept strictly separate from live status.
     *
     * Honesty rules:
     * - NEVER treats a recent recorded RSS upload as a live broadcast.
     * - NEVER returns [LiveBroadcastState.LIVE_CONFIRMED] merely from HTTP 200; returns
     *   [LiveBroadcastState.CONNECTING] with the resolved candidate source until the actual
     *   in-app player confirms active live playback.
     * - Returns [LiveBroadcastState.OFFLINE] when disconnected and [LiveBroadcastState.SOURCE_UNAVAILABLE]
     *   when all supported live sources fail.
     */
    suspend fun checkHarameynLiveStatus(
        context: Context,
        location: HarameynLocation,
        startFromSourceIndex: Int = 0
    ): LiveStreamStatusResult = withContext(Dispatchers.IO) {
        val channel = HARAMEYN_CHANNELS.getValue(location)
        val orderedSources = HarameynLiveSources.sourcesFor(location)

        if (!isNetworkAvailable(context)) {
            return@withContext LiveStreamStatusResult(
                location = location,
                channel = channel,
                state = LiveBroadcastState.OFFLINE,
                verifiedVideoId = null,
                streamTitle = channel.nameEn,
                watchOnYouTubeUrl = channel.officialChannelUrl,
                isEmbeddableConfirmed = false,
                statusDetail = "Offline — connect to internet to watch the live broadcast.",
                resolvedSource = null,
                sourceAttempts = orderedSources.map {
                    LiveSourceAttempt(it, LiveSourceAttemptStatus.PENDING)
                }
            )
        }

        val attempts = orderedSources.mapIndexed { idx, src ->
            if (idx < startFromSourceIndex) {
                LiveSourceAttempt(src, LiveSourceAttemptStatus.FAILED, "Skipped after prior failure")
            } else {
                LiveSourceAttempt(src, LiveSourceAttemptStatus.PENDING)
            }
        }.toMutableList()

        var resolved: ResolvedLiveStreamSource? = null
        var lastError: String? = null

        for (idx in startFromSourceIndex.coerceAtLeast(0) until orderedSources.size) {
            val candidate = orderedSources[idx]
            attempts[idx] = LiveSourceAttempt(candidate, LiveSourceAttemptStatus.TESTING, "Probing source")
            val outcome = HarameynLiveSourceResolver.probeSource(candidate, httpClient)
            if (outcome.resolved != null) {
                resolved = outcome.resolved
                attempts[idx] = LiveSourceAttempt(
                    candidate,
                    LiveSourceAttemptStatus.TESTING,
                    "Awaiting player playback confirmation"
                )
                break
            } else {
                lastError = outcome.failureReason ?: "Source unavailable"
                attempts[idx] = LiveSourceAttempt(candidate, LiveSourceAttemptStatus.FAILED, lastError)
            }
        }

        val recordedUploads = fetchHarameynRecordedUploadsInternal(channel)

        if (resolved != null) {
            return@withContext LiveStreamStatusResult(
                location = location,
                channel = channel,
                state = LiveBroadcastState.CONNECTING,
                verifiedVideoId = resolved.youtubeVideoId,
                streamTitle = resolved.resolvedTitle.ifBlank { channel.nameEn },
                publishedDisplay = recordedUploads.firstOrNull()?.publishedDisplay.orEmpty(),
                watchOnYouTubeUrl = resolved.externalOpenUrl,
                isEmbeddableConfirmed = false,
                recentRecordedUploads = recordedUploads,
                statusDetail = "Connecting to ${resolved.config.labelEn} — waiting for active playback confirmation.",
                resolvedSource = resolved,
                sourceAttempts = attempts
            )
        }

        val finalState = if (recordedUploads.isNotEmpty()) {
            LiveBroadcastState.LIVE_STATUS_UNCONFIRMED
        } else {
            LiveBroadcastState.SOURCE_UNAVAILABLE
        }

        LiveStreamStatusResult(
            location = location,
            channel = channel,
            state = finalState,
            verifiedVideoId = null,
            streamTitle = channel.nameEn,
            publishedDisplay = recordedUploads.firstOrNull()?.publishedDisplay.orEmpty(),
            watchOnYouTubeUrl = channel.officialChannelUrl,
            isEmbeddableConfirmed = false,
            recentRecordedUploads = recordedUploads,
            statusDetail = lastError ?: "All supported live sources are currently unavailable.",
            resolvedSource = null,
            sourceAttempts = attempts
        )
    }

    suspend fun fetchHarameynRecordedUploads(
        context: Context,
        location: HarameynLocation
    ): List<YouTubeVideoItem> = withContext(Dispatchers.IO) {
        if (!isNetworkAvailable(context)) return@withContext emptyList()
        fetchHarameynRecordedUploadsInternal(HARAMEYN_CHANNELS.getValue(location))
    }

    private fun fetchHarameynRecordedUploadsInternal(channel: HarameynStreamChannel): List<YouTubeVideoItem> {
        val rssResult = executeGetWithRetry(channel.feedUrl, maxAttempts = 1)
        val body = rssResult.body ?: return emptyList()
        val channelMeta = VERIFIED_CHANNELS.firstOrNull { it.channelId == channel.channelId }
        return parseAtomFeedXml(body, channelMeta).take(6)
    }

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
    // REAL PLAYER-VERIFIED DURATION CACHE (NEVER FABRICATED)
    // =========================================================

    fun recordPlayerVerifiedDuration(context: Context, videoId: String, durationSeconds: Int) {
        if (!isValidVideoId(videoId) || durationSeconds <= 0) return
        verifiedDurationCache[videoId] = durationSeconds
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val raw = prefs.getString(KEY_VERIFIED_DURATIONS_JSON, "{}") ?: "{}"
            val json = runCatching { JSONObject(raw) }.getOrDefault(JSONObject())
            json.put(videoId, durationSeconds)
            prefs.edit().putString(KEY_VERIFIED_DURATIONS_JSON, json.toString()).apply()
        } catch (e: Exception) {
            Log.w(TAG, "Failed saving verified duration: ${e::class.java.simpleName}")
        }
    }

    fun getPlayerVerifiedDuration(context: Context, videoId: String): Int? {
        ensureVerifiedDurationsLoaded(context)
        return verifiedDurationCache[videoId]
    }

    private fun ensureVerifiedDurationsLoaded(context: Context) {
        if (verifiedDurationCache.isNotEmpty()) return
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val raw = prefs.getString(KEY_VERIFIED_DURATIONS_JSON, null) ?: return
            val json = JSONObject(raw)
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val sec = json.optInt(key, -1)
                if (isValidVideoId(key) && sec > 0) {
                    verifiedDurationCache[key] = sec
                }
            }
        } catch (_: Exception) {
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
        cacheGeneration.incrementAndGet()
        inFlightRequests.values.forEach { it.cancel() }
        memoryCache.clear()
        inFlightRequests.clear()
        channelHealthMap.clear()
        verifiedDurationCache.clear()
        ChannelCatalogLoader.clearCacheForTest()
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
                put("updatedIso", item.updatedIso)
                put("publishedDisplay", item.publishedDisplay)
                put("publishedEpochMs", item.publishedEpochMs)
                put("videoUrl", item.videoUrl)
                put("description", item.description)
                put("languageCode", item.languageCode)
                put("languageGroup", item.languageGroup.id)
                put("verifiedPodcastLanguage", item.verifiedPodcastLanguage.id)
                put("categories", JSONArray(item.categories.map { it.id }))
                if (item.durationSeconds != null) put("durationSeconds", item.durationSeconds)
                put("durationStatus", item.durationStatus.wireValue)
                if (item.viewCount != null) put("viewCount", item.viewCount)
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
            val description = obj.optString("description", "")
            val storedPodcastLang = obj.optString("verifiedPodcastLanguage", "")
            val verifiedLang = PodcastLanguageSection.entries.firstOrNull { it.id == storedPodcastLang }
                ?: verifyPodcastLanguageFromContent(title, channel)
            val storedLangGroup = obj.optString("languageGroup", "")
            val resolvedGroup = if (storedLangGroup.isNotBlank()) {
                LectureLanguageGroup.fromCode(storedLangGroup)
            } else {
                val chMeta = VERIFIED_CHANNELS.firstOrNull { it.key == obj.optString("channelKey") }
                detectVideoLanguageGroup(title, description, chMeta)
            }
            val durSec = if (obj.has("durationSeconds") && !obj.isNull("durationSeconds")) {
                obj.optInt("durationSeconds", -1).takeIf { it > 0 }
            } else {
                null
            }
            val durStatus = if (obj.has("durationStatus")) {
                DurationVerificationStatus.fromWire(obj.optString("durationStatus"))
            } else {
                LectureDurationConfig.evaluateDuration(durSec, obj.optString("videoUrl").contains("/shorts/"))
            }
            val views = if (obj.has("viewCount") && !obj.isNull("viewCount")) {
                obj.optLong("viewCount", -1L).takeIf { it >= 0L }
            } else {
                null
            }

            result.add(
                YouTubeVideoItem(
                    id = id,
                    title = title,
                    channel = channel,
                    channelId = obj.optString("channelId"),
                    channelKey = obj.optString("channelKey"),
                    thumbnail = obj.optString("thumbnail"),
                    publishedIso = obj.optString("publishedIso"),
                    updatedIso = obj.optString("updatedIso", ""),
                    publishedDisplay = obj.optString("publishedDisplay"),
                    publishedEpochMs = obj.optLong("publishedEpochMs", 0L),
                    videoUrl = obj.optString("videoUrl", "https://www.youtube.com/watch?v=$id"),
                    description = description,
                    languageCode = obj.optString("languageCode", "und"),
                    languageGroup = resolvedGroup,
                    verifiedPodcastLanguage = verifiedLang,
                    categories = cats.ifEmpty { listOf(LectureCategory.LATEST_LECTURES) },
                    durationSeconds = durSec,
                    durationStatus = durStatus,
                    viewCount = views,
                    isFromCache = true,
                    isCacheExpired = false
                )
            )
        }
        return result
    }

    internal fun parseIsoDateEpoch(iso: String): Long = YouTubeRssParser.parseIsoDateEpoch(iso)

    internal fun formatIsoDateShort(iso: String): String = YouTubeRssParser.formatIsoDateShort(iso)
}
