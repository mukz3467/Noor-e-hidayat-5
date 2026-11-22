package com.example.data.lectures

import com.example.data.repository.LectureCategory
import com.example.data.repository.VerifiedIslamicChannel

/**
 * Additional live-verified English Islamic scholar, institute, and history channels
 * (74 channels). Every UC Channel ID was resolved from YouTube and live-tested
 * against official YouTube Atom RSS feeds returning HTTP 200 XML.
 */
internal object EnglishScholarChannelsExtra {

    val CHANNELS: List<VerifiedIslamicChannel> = listOf(
        verifiedScholarChannel(
            key = "omar_of_the_orient", channelId = "UCEgF5vyAFtjPc6FC_a3gygQ", handle = "@OmaroftheOrient",
            nameEn = "Omar of the Orient", nameUr = "عمر آف دی اورینٹ", nameAr = "عمر المشرق للتاريخ الإسلامي", nameHi = "उमर ऑफ द ओरिएंट",
            specialtyEn = "Islamic History, Empires, Caliphates & Battles", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_BATTLES),
            channelType = ChannelType.EDUCATIONAL, priority = 98
        ),
        verifiedScholarChannel(
            key = "the_seerah_timeline", channelId = "UC_TIuMejD0YyMxDwu5MgkQg", handle = "@SeerahWithAmmarKhanYasir",
            nameEn = "The Seerah Timeline", nameUr = "سیرت ٹائم لائن", nameAr = "الخط الزمني للسيرة النبوية", nameHi = "द सीरत टाइमलाइन",
            specialtyEn = "Seerah, Islamic Battles & Early History", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.EDUCATIONAL, priority = 94
        ),
        verifiedScholarChannel(
            key = "islamic_history_podcast", channelId = "UCBSJLQy9GDSfkJYl62JJtrA", handle = "@islamichistorypodcast",
            nameEn = "Islamic History Podcast", nameUr = "اسلامک ہسٹری پوڈکاسٹ", nameAr = "بودكاست التاريخ الإسلامي", nameHi = "इस्लामिक हिस्ट्री पॉडकास्ट",
            specialtyEn = "Long-Form Islamic History, Caliphates & Battles", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.EDUCATIONAL, priority = 99
        ),
        verifiedScholarChannel(
            key = "al_muqaddimah", channelId = "UCf0O2efB4K66UUaT7QJPVNA", handle = "@AlMuqaddimahYT",
            nameEn = "Al Muqaddimah", nameUr = "المقدمہ (اسلامی تاریخ)", nameAr = "المقدمة للتاريخ الإسلامي", nameHi = "अल मुक़द्दिमाह",
            specialtyEn = "Islamic History, Civilization & Dynasties", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_BATTLES),
            channelType = ChannelType.EDUCATIONAL, priority = 98
        ),
        verifiedScholarChannel(
            key = "history_of_islam_nm", channelId = "UCvvBNZEMpHEEt7n4jMqhv2g", handle = "@HistoryOfIslam-NM",
            nameEn = "History of Islam", nameUr = "ہسٹری آف اسلام", nameAr = "تاريخ الإسلام", nameHi = "हिस्ट्री ऑफ़ इस्लाम",
            specialtyEn = "Islamic History, Seerah & Companions", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_CALIPHATES),
            channelType = ChannelType.EDUCATIONAL, priority = 97
        ),
        verifiedScholarChannel(
            key = "muslim_heritage", channelId = "UCJYbntihKTlAdYC4HURXNvg", handle = "@savemuslimheritage",
            nameEn = "Muslim Heritage", nameUr = "مسلم ہیریٹیج", nameAr = "التراث الإسلامي", nameHi = "मुस्लिम हेरिटेज",
            specialtyEn = "Islamic Civilization, Golden Age & History", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_CALIPHATES),
            channelType = ChannelType.EDUCATIONAL, priority = 93
        ),
        verifiedScholarChannel(
            key = "quranweekly", channelId = "UCV7aA2UxRf3SHyxRoeYb9TQ", handle = "@QuranWeekly",
            nameEn = "Quran Weekly",
            specialtyEn = "Quran Tafseer, Quran Reflections, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "dreyadqunaibiglobalchannel", channelId = "UCf_Zd_7oIJKHDzdu2HsXbEA", handle = "@DrEyadQunaibiGlobalChannel",
            nameEn = "Dr. Eyad Qunaibi - Global Channel",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.BROADCASTER, priority = 92
        ),
        verifiedScholarChannel(
            key = "islamic_guidance", channelId = "UCNDpwS2nEiGiJAW3Oe-3zBg", handle = "@Islamic_Guidance",
            nameEn = "Islamic Guidance",
            specialtyEn = "Seerah, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "islamicguidancechannelhd", channelId = "UCuey_1zHTv-wp5exLipVeaA", handle = "@IslamicGuidanceChannelHD",
            nameEn = "Islamic Guidance HD",
            specialtyEn = "Islamic Battles, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "justalayman", channelId = "UCbwFrxI0u_5vi9wwD_Ni97g", handle = "@Justalayman",
            nameEn = "Just a layman *Sh. Assim Al Hakeem*",
            specialtyEn = "Sahaba Biographies, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "uthmanibnfarooqofficial", channelId = "UCc17C9v22IjmvgsgxzuNUvw", handle = "@UthmanIbnFarooqOfficial",
            nameEn = "Uthman Ibn Farooq Official",
            specialtyEn = "Hadith & Sunnah, Islamic History, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.HADITH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.SAHABA_STORIES, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "dr_tareqalsuwaidan_en", channelId = "UCvKOo3hQZcRgN3ERr-eQf0A", handle = "@Dr.TareqAlSuwaidan_EN",
            nameEn = "Dr. Tareq Al-Suwaidan",
            specialtyEn = "Islamic History", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_HISTORY),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "scdawah", channelId = "UCnZPtuya_yUUqGZGHk1nEOA", handle = "@scdawah",
            nameEn = "SCDawah Channel",
            specialtyEn = "Hadith & Sunnah, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.HADITH, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.BROADCASTER, priority = 85
        ),
        verifiedScholarChannel(
            key = "miftaahinstitute", channelId = "UCYr_FKFkc_pJq_sMKuaktnw", handle = "@MiftaahInstitute",
            nameEn = "Miftaah Institute",
            specialtyEn = "Stories of Prophets, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.INSTITUTE, priority = 85
        ),
        verifiedScholarChannel(
            key = "themuslimlantern", channelId = "UCeCAQhKbU2ETNWxWxB94HgA", handle = "@TheMuslimLantern",
            nameEn = "The Muslim Lantern",
            specialtyEn = "Hadith & Sunnah, Sahaba Biographies, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.HADITH, LectureCategory.SAHABA_STORIES, LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "omarsuleimanpersonal", channelId = "UCtm8rtofLSnaIBi3noB0INg", handle = "@OmarSuleimanPersonal",
            nameEn = "Omar Suleiman Personal",
            specialtyEn = "Quran Tafseer, Quran Reflections, Stories of Prophets", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "sandalamediacenter", channelId = "UC361kz8bZYcYuF7k5j2kXdw", handle = "@SandalaMediaCenter",
            nameEn = "Hamza Yusuf",
            specialtyEn = "Stories of Prophets, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "renovatiojournal", channelId = "UChf1mV5uFwXj9h8aSaJRtyg", handle = "@RenovatioJournal",
            nameEn = "Renovatio: The Journal of Zaytuna College",
            specialtyEn = "Hadith & Sunnah, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 81
        ),
        verifiedScholarChannel(
            key = "efdawah", channelId = "UCx420FfPBbEboBvnYjIemzw", handle = "@EFDawah",
            nameEn = "EFDawah",
            specialtyEn = "Aqeedah & Dawah, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "samdawahchannel", channelId = "UC9zz50H6HKkWs7veJP6vTDQ", handle = "@SamDawahChannel",
            nameEn = "Sam Dawah",
            specialtyEn = "Hadith & Sunnah, Sahaba Biographies, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.SAHABA_STORIES, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "islamickidsvideos", channelId = "UCvWyP-vFhlS1-XENpD6w68A", handle = "@IslamicKidsVideos",
            nameEn = "Stories of the Prophets - Quran Stories",
            specialtyEn = "Quran Tafseer, Quran Reflections, Seerah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SEERAH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.EDUCATIONAL, priority = 77
        ),
        verifiedScholarChannel(
            key = "alidawah", channelId = "UCK9GD2WRxJxbDz9croru9UQ", handle = "@AliDawah",
            nameEn = "Ali Dawah",
            specialtyEn = "Sahaba Biographies, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "ustadhwahajtarin", channelId = "UCPOhf2e8duW6czbZVW43LVQ", handle = "@ustadhwahajtarin",
            nameEn = "Ustadh Wahaj Tarin",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "international_open_university", channelId = "UCKTdGEZ6Kivs-ZUk5_kv_tA", handle = "@international_open_university",
            nameEn = "International Open University",
            specialtyEn = "Hadith & Sunnah, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 77
        ),
        verifiedScholarChannel(
            key = "drbilalphilipsclips", channelId = "UCW-bdrrXxoCRMeOaApSRgag", handle = "@drbilalphilipsclips",
            nameEn = "Dr. Bilal Philips Clips",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "imanforce25", channelId = "UCF56DHgih-4bnSNIr2_RnGQ", handle = "@ImanForce25",
            nameEn = "Iman Force",
            specialtyEn = "Akhlaq & Tazkiyah, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "wajihuddinofficial", channelId = "UC1yVqUgCc6NRMt-zSmA0AXg", handle = "@WajihUddinOfficial",
            nameEn = "Wajih Uddin",
            specialtyEn = "Quran Reflections, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "theeastlondonmosque", channelId = "UCvLvAfXrrWkWITIAIfrwOtQ", handle = "@theeastlondonmosque",
            nameEn = "East London Mosque & London Muslim Centre",
            specialtyEn = "Hadith & Sunnah, Stories of Prophets, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.INSTITUTE, priority = 77
        ),
        verifiedScholarChannel(
            key = "islamicevent", channelId = "UCEVp9oosQo9HPVf7IdKmD4w", handle = "@islamicevent",
            nameEn = "islamicevent",
            specialtyEn = "Stories of Prophets, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "sunnahdiscourse", channelId = "UCESTJpz08lBBYmYiiA6b-Cg", handle = "@SunnahDiscourse",
            nameEn = "Sunnah Discourse",
            specialtyEn = "Hadith & Sunnah, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "epicislamiclectures", channelId = "UCNeqbUiXUleUN3Ah2wKKVFg", handle = "@EpicIslamicLectures",
            nameEn = "Epic Islamic Lectures",
            specialtyEn = "Salah & Worship, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "alqalaminstitute", channelId = "UC4lkSSykAm8iervJg43qozg", handle = "@AlQalamInstitute",
            nameEn = "Al-Qalam Institute",
            specialtyEn = "Quran Tafseer, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 73
        ),
        verifiedScholarChannel(
            key = "assuffainstitute", channelId = "UC29vgDQfQnvIlJWmfdyHr7A", handle = "@AsSuffaInstitute",
            nameEn = "As-Suffa",
            specialtyEn = "Hadith & Sunnah, Sahaba Biographies, Family & Youth", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.SAHABA_STORIES, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "rootscommunity", channelId = "UCWv1QqcaD2yWVWj0468eAnw", handle = "@rootscommunity",
            nameEn = "Roots Community",
            specialtyEn = "Salah & Worship, Akhlaq & Tazkiyah, Family & Youth", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "isna_canada", channelId = "UCX41jcJx1eznp2mQVUTRJGA", handle = "@isna_canada",
            nameEn = "ISNA Canada",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "shaykhsaqibiqbal", channelId = "UCtC5KDdWwK6W3OwhLNY8xzA", handle = "@ShaykhSaqibIqbal",
            nameEn = "Shaykh Saqib Iqbal",
            specialtyEn = "Sahaba Biographies, Family & Youth, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.FAMILY_SOCIETY, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "almaqasid", channelId = "UCP5hDyVvdKl1yX3FpfKs55g", handle = "@AlMaqasid",
            nameEn = "Al-Maqasid",
            specialtyEn = "Sahaba Biographies, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "abuibrahim_mm", channelId = "UCmBeDLS33OmwgiuhWPtwlyQ", handle = "@AbuIbrahim-MM",
            nameEn = "Abu Ibrahim",
            specialtyEn = "Stories of Prophets, Sahaba Biographies, Islamic Caliphates", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_CALIPHATES),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "ibnawesome", channelId = "UCvrQlTnLEvAg9YA7wR1LZ7g", handle = "@ibnawesome",
            nameEn = "Muhammad Umar Mustafa",
            specialtyEn = "Sahaba Biographies, Fiqh & Islamic Q&A", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "qariziyaadpatel", channelId = "UCHJcTYLVPlwOZe4bgTp0NCA", handle = "@QariZiyaadPatel",
            nameEn = "Qari Ziyaad Patel",
            specialtyEn = "Quran Tafseer, Quran Reflections, Stories of Prophets", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "alhudaonline", channelId = "UCzwxFFZX_b4pRi1LFSwxEtQ", handle = "@alhudaonline",
            nameEn = "AlHuda Online Courses Official",
            specialtyEn = "Quran Tafseer, Quran Reflections, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "ynw_eng", channelId = "UCCiELM4Cyba90O2jyWiylxQ", handle = "@YNW-Eng",
            nameEn = "Yasir Nadeem al Wajidi - English",
            specialtyEn = "Sahaba Biographies, Salah & Worship, Fiqh & Islamic Q&A", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "alkawtharacademy", channelId = "UCmNkiglqMnz9cYuCQ47PHSw", handle = "@AlKawtharAcademy",
            nameEn = "Al Kawthar Academy",
            specialtyEn = "Hadith & Sunnah, Stories of Prophets, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 73
        ),
        verifiedScholarChannel(
            key = "considerislamlivekennybomer", channelId = "UCi4Qjbn4cbYbE_30ygoRpug", handle = "@ConsiderIslamLIVEKennyBomer",
            nameEn = "Consider Islam: LIVE with Kenny Bomer",
            specialtyEn = "Hadith & Sunnah, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "mohamadbaajourlectures", channelId = "UC7SOnJkOuPqP59vLJTPrLEg", handle = "@MohamadBaajourLectures",
            nameEn = "Mohamad Baajour Lectures",
            specialtyEn = "Quran Tafseer, Quran Reflections, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "tzubair", channelId = "UCJ3mOfgzY76_2g-UeXj2XfQ", handle = "@tzubair",
            nameEn = "Taimiyyah Zubair",
            specialtyEn = "Quran Tafseer, Quran Reflections, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SAHABA_STORIES, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "yasminmogahedofficial", channelId = "UCsDTwDhGICLG1U06ZtVwRXQ", handle = "@yasminmogahedofficial",
            nameEn = "Yasmin Mogahed Dawah",
            specialtyEn = "Aqeedah & Dawah, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "officialadamscenter", channelId = "UCLIr9L8Ko0RYTlv6RxS49Ug", handle = "@OfficialADAMSCenter",
            nameEn = "ADAMS Center - Official YouTube Channel",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.BROADCASTER, priority = 73
        ),
        verifiedScholarChannel(
            key = "peacetvenglish214", channelId = "UCTzmbtcSKJu6h57ylVAp_ZA", handle = "@peacetvenglish214",
            nameEn = "Peace TV English",
            specialtyEn = "Aqeedah & Dawah, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.BROADCASTER, priority = 68
        ),
        verifiedScholarChannel(
            key = "fariqnaik", channelId = "UC0ieOTVCnHG_XkS8vzNFGxQ", handle = "@fariqnaik",
            nameEn = "Shaikh Fariq Naik",
            specialtyEn = "Hadith & Sunnah, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "spiritualliife", channelId = "UCqhXEsxfewDDVOjfdYatJSA", handle = "@spiritualliife",
            nameEn = "Spiritual Life",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "isnavideos", channelId = "UCUMhfPLyJX2rm-GX5lJ8T3w", handle = "@isnavideos",
            nameEn = "ISNA (Islamic Society of North America) Official Channel",
            specialtyEn = "Akhlaq & Tazkiyah, Family & Youth", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.BROADCASTER, priority = 68
        ),
        verifiedScholarChannel(
            key = "epicmasjidclips", channelId = "UCpPDkiIaxYvztFRgsnlrKBw", handle = "@epicmasjidclips",
            nameEn = "EPIC Masjid Clips",
            specialtyEn = "Salah & Worship, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 68
        ),
        verifiedScholarChannel(
            key = "therahmahfoundationwomen", channelId = "UC6uXiqV0trOU20AMrM_5cng", handle = "@TheRahmahFoundationWomen",
            nameEn = "The Rahmah Foundation",
            specialtyEn = "Quran Tafseer, Hadith & Sunnah, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.HADITH, LectureCategory.SAHABA_STORIES, LectureCategory.FAMILY_SOCIETY, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.INSTITUTE, priority = 68
        ),
        verifiedScholarChannel(
            key = "cambridgeislamiccollege", channelId = "UCHUhtE6AMSG4lhijvXr39yg", handle = "@CambridgeIslamicCollege",
            nameEn = "Cambridge Islamic College",
            specialtyEn = "Quran Tafseer, Quran Reflections, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 68
        ),
        verifiedScholarChannel(
            key = "bloggingtheology", channelId = "UC_SLXSHcCwK2RSZTXVL26SA", handle = "@BloggingTheology",
            nameEn = "Blogging Theology",
            specialtyEn = "Quran Tafseer, Islamic History, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.ISLAMIC_HISTORY, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "haithamalhaddad_official", channelId = "UC8jzHJx36TnYt0caAqnfp4g", handle = "@haithamalhaddad.official",
            nameEn = "Sheikh Haitham Al Haddad",
            specialtyEn = "Islamic Battles, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_BATTLES, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "lovee_islam", channelId = "UCTyOEy5LjVeTIt7iYtbgtEA", handle = "@lovee_islam",
            nameEn = "Love Islam",
            specialtyEn = "Stories of Prophets, Salah & Worship, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "rihlatulilm", channelId = "UCfCrOHHYAw8jz96w7NlzRqw", handle = "@RihlatulIlm",
            nameEn = "Rihla",
            specialtyEn = "Stories of Prophets, Sahaba Biographies, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "shaykhhamzacom", channelId = "UCbVHGXEnlE7ARCkNXlJN_Pg", handle = "@ShaykhHamzacom",
            nameEn = "ShaykhHamza.com",
            specialtyEn = "Stories of Prophets, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "ustadhbaajourspeaks", channelId = "UCmcSDZMt9MMRvxcfQkNI9-A", handle = "@Ustadhbaajourspeaks",
            nameEn = "Ustadh Baajour Speaks",
            specialtyEn = "Akhlaq & Tazkiyah, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "propheticguidance", channelId = "UCz8rqHTuG1ghQ4apApYsc_g", handle = "@PropheticGuidance",
            nameEn = "AlHadyAlNabawy",
            specialtyEn = "Hadith & Sunnah, Stories of Prophets, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.FAMILY_SOCIETY, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "routetojannahinstitute1312", channelId = "UCLZfNkqDSOvowIEglwncdJA", handle = "@routetojannahinstitute1312",
            nameEn = "Route to Jannah Institute",
            specialtyEn = "Quran Tafseer, Seerah, Sahaba Biographies", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.INSTITUTE, priority = 68
        ),
        verifiedScholarChannel(
            key = "islamreligionguide", channelId = "UClDHnqFr8gOkmTnHZEe-koA", handle = "@IslamReligionGuide",
            nameEn = "Islam Religion",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "mishkahuniversity", channelId = "UCKTLDLHoZGw1VAM_CnZFrUg", handle = "@MishkahUniversity",
            nameEn = "Mishkah University | جامعة مشكاة",
            specialtyEn = "Salah & Worship, Akhlaq & Tazkiyah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 68
        ),
        verifiedScholarChannel(
            key = "zadacademybaramulla", channelId = "UCl7JyGs9CAKlyz7UKLqxk5w", handle = "@zadacademybaramulla",
            nameEn = "ZAD Academy Baramulla",
            specialtyEn = "Hadith & Sunnah, Akhlaq & Tazkiyah, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_MANNERS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.INSTITUTE, priority = 68
        ),
        verifiedScholarChannel(
            key = "muslim4life76", channelId = "UC8FrE5UdIZHQnKRg0Snqtww", handle = "@Muslim4life76",
            nameEn = "Muslim4life76",
            specialtyEn = "Hadith & Sunnah, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "quranacademy945", channelId = "UC3t4hEZ6RlRYhAOuKUmasow", handle = "@quranacademy945",
            nameEn = "Quran Academy",
            specialtyEn = "Quran Tafseer, Quran Reflections, Family & Youth", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.INSTITUTE, priority = 68
        ),
        verifiedScholarChannel(
            key = "icnatv", channelId = "UC56VroaBR4VkhaTJoqbQjWA", handle = "@ICNATV",
            nameEn = "ICNA",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "seerahofprophetmuhammad8385", channelId = "UCZUoULlDKGKdJ3US9xSMdTg", handle = "@seerahofprophetmuhammad8385",
            nameEn = "Seerah Of Prophet Muhammad",
            specialtyEn = "Seerah, Stories of Prophets", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.STORIES_OF_PROPHETS),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "theusuliinstitute", channelId = "UCacZ3k2AOpd5-3tdCxcDmgQ", handle = "@TheUsuliInstitute",
            nameEn = "The Usuli Institute",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.INSTITUTE, priority = 68
        ),
        verifiedScholarChannel(
            key = "sabeelun_najaah", channelId = "UCottLwPVIAyDfoXZ2jZyudg", handle = "@sabeelun_najaah",
            nameEn = "Sabeelun Najaah",
            specialtyEn = "Sahaba Biographies, Aqeedah & Dawah", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "remindersfromshadyalsuleiman", channelId = "UCzyHJJAChFMhp_5wLfDd4Ng", handle = "@RemindersFromShadyAlSuleiman",
            nameEn = "Reminders From Shady Al Suleiman",
            specialtyEn = "Akhlaq & Tazkiyah, Family & Youth", languageCode = "en", primaryLanguageGroup = LectureLanguageGroup.ENGLISH,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 68
        )
    )
}
