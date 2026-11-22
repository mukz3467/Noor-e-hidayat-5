package com.example.data.lectures

import com.example.data.repository.LectureCategory
import com.example.data.repository.VerifiedIslamicChannel

/**
 * Additional live-verified Arabic Islamic scholar, institute, and history channels
 * (74 channels). Every UC Channel ID was resolved from YouTube and live-tested
 * against official YouTube Atom RSS feeds returning HTTP 200 XML.
 */
internal object ArabicScholarChannelsExtra {

    val CHANNELS: List<VerifiedIslamicChannel> = listOf(
        verifiedScholarChannel(
            key = "jihad_alturbani_100_great", channelId = "UChdeAO0naFewi8leXaCquOQ", handle = "@100GREAT",
            nameEn = "100 Great (Jihad Al-Turbani)", nameUr = "عظماء مائة - جہاد التربانی", nameAr = "العظماء المائة - جهاد الترباني", nameHi = "100 ग्रेट (जिहाद अल-तुरबानी)",
            specialtyEn = "Islamic History, Heroes, Battles & Caliphates", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_BATTLES, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.SAHABA_STORIES, LectureCategory.POPULAR_SCHOLARS),
            channelType = ChannelType.EDUCATIONAL, priority = 99
        ),
        verifiedScholarChannel(
            key = "early_history_of_islam_ar", channelId = "UCdH7FMyR5SJbp-jqOzdsppQ", handle = "@Earlyhistoryofisalm",
            nameEn = "Early History of Islam", nameUr = "ابتدائی اسلامی تاریخ", nameAr = "التاريخ المبكر للإسلام", nameHi = "प्रारंभिक इस्लामी इतिहास",
            specialtyEn = "Early Islamic History, Caliphates & Battles", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.EDUCATIONAL, priority = 98
        ),
        verifiedScholarChannel(
            key = "altareekh_alislami_ar", channelId = "UCd-fNq_mn4-TUG1nhdbhHfA", handle = "@islamic.history1",
            nameEn = "Islamic History (التاريخ الاسلامي)", nameUr = "التاریخ الاسلامی", nameAr = "التاريخ الإسلامي", nameHi = "इस्लामिक हिस्ट्री (अरबी)",
            specialtyEn = "Islamic History, Civilization & Historical Biographies", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.EDUCATIONAL, priority = 97
        ),
        verifiedScholarChannel(
            key = "mahmoudalhasanat", channelId = "UCTsAN8CeCY5zJ_SA8kekTXg", handle = "@mahmoudalhasanat",
            nameEn = "الداعية محمود الحسنات القناة الرسمية",
            specialtyEn = "Family & Youth", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.BROADCASTER, priority = 92
        ),
        verifiedScholarChannel(
            key = "i7sannet", channelId = "UCC8Nx8I7Uh-1sWKLg7XikkQ", handle = "@i7sanNet",
            nameEn = "Dejavu & احمد الشقيري",
            specialtyEn = "Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "quranradio_ksa", channelId = "UCrZNAOv_KBLfb-3TFgxIZqg", handle = "@QuranRadio_ksa",
            nameEn = "إذاعة القرآن الكريم",
            specialtyEn = "Quran Tafseer, Quran Reflections, Aqeedah & Dawah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.BROADCASTER, priority = 92
        ),
        verifiedScholarChannel(
            key = "cheikhimamomarabdelkafy6541", channelId = "UCEeFI-v9NH7ZBl0eZs2slPA", handle = "@cheikhimamomarabdelkafy6541",
            nameEn = "cheikh Imam Omar Abd el kafy",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "yasseraldosry", channelId = "UCf5vl3vN4zU2NSdcL-3Z5Nw", handle = "@Yasseraldosry",
            nameEn = "أ.د ياسر بن راشد الدوسري",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "abdullahrushdy", channelId = "UCb5kp2EMETR2RCR1kT0c26g", handle = "@abdullahrushdy",
            nameEn = "abdullah rushdy-عبدالله رشدي",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "alsharawiofficial", channelId = "UCijSEIUA72pyzSNOf_lx5ng", handle = "@alsharawiofficial",
            nameEn = "القناة الرسمية لفضيلة الشيخ محمد متولي الشعراوي",
            specialtyEn = "Stories of Prophets, Family & Youth", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.BROADCASTER, priority = 92
        ),
        verifiedScholarChannel(
            key = "nabulsi1", channelId = "UCvwDvEh9i7Ge8bs_tw6nrrA", handle = "@Nabulsi1",
            nameEn = "قناة نفحات النابلسيtv",
            specialtyEn = "Quran Tafseer", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER),
            channelType = ChannelType.BROADCASTER, priority = 92
        ),
        verifiedScholarChannel(
            key = "sheikhsaadalghamdi", channelId = "UCL8fnPgqDPPBbUphwdv99og", handle = "@SheikhSaadAlGhamdi",
            nameEn = "Sheikh Saad Al Ghamdi | الشيخ سعد الغامدي",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "mahmoud_masry123", channelId = "UCQLs3IFVvJ1eFBpA2DDzv-A", handle = "@Mahmoud.Masry123",
            nameEn = "القناة الرسمية للشيخ محمود المصرى",
            specialtyEn = "Seerah, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SEERAH, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.BROADCASTER, priority = 89
        ),
        verifiedScholarChannel(
            key = "elnastv", channelId = "UC_7rmGviqlPKHV0hdGOpiOQ", handle = "@ElnasTv",
            nameEn = "قناة الناس",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.BROADCASTER, priority = 89
        ),
        verifiedScholarChannel(
            key = "hazzaalblushi_v9h", channelId = "UCX8v_ZCcrynYG2QxUbaMSZA", handle = "@HazzaAlblushi-v9h",
            nameEn = "قناة هزاع البلوشي مباشر",
            specialtyEn = "Quran Tafseer, Quran Reflections, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.BROADCASTER, priority = 89
        ),
        verifiedScholarChannel(
            key = "hazzaalblushi", channelId = "UCEiEQLs1HFaS4LXmRPdRYgA", handle = "@HazzaAlblushi",
            nameEn = "Hazza Alblushi l هزاع البلوشي",
            specialtyEn = "Quran Tafseer, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "dorrosnabilel_awadi", channelId = "UC1NuE6xeMyyTCHq0sKjJ5IQ", handle = "@DorrosNabilEl-Awadi",
            nameEn = "دروس الشيخ نبيل العوضي",
            specialtyEn = "Seerah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SEERAH),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "alqtami", channelId = "UC-AuvzIPDgBmvoDMlLhBuFQ", handle = "@Alqtami",
            nameEn = "القناة الرسمية للشيخ ناصر القطامي",
            specialtyEn = "Quran Tafseer, Quran Reflections, Stories of Prophets", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.BROADCASTER, priority = 89
        ),
        verifiedScholarChannel(
            key = "aluthaymeen", channelId = "UCFjzJYgxHjk44AFoEwwgPjg", handle = "@ALUthaymeen",
            nameEn = "فضيلة الشيخ محمد بن صالح العثيمين",
            specialtyEn = "Aqeedah & Dawah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "ch_ar_jxqweq", channelId = "UCwLgnvgp32d8bHtQljXqWeQ", handle = "@3laaHamed",
            nameEn = "Alaa Hamed - علاء حامد",
            specialtyEn = "Stories of Prophets, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "ch_ar_wicepw", channelId = "UCr4Kz8-cozLWzGYa1WICePw", handle = "@القناةالرسميةللشيخالدكتورمحمدح",
            nameEn = "القناة الرسمية للشيخ الدكتور محمد حسان",
            specialtyEn = "Quran Tafseer, Seerah, Fiqh & Islamic Q&A", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.SEERAH, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.BROADCASTER, priority = 89
        ),
        verifiedScholarChannel(
            key = "waie", channelId = "UC2qcjzOEX3lxE73cyroWdiw", handle = "@Waie",
            nameEn = "وعي",
            specialtyEn = "Sahaba Biographies", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "alrahmatvofficial", channelId = "UCY9j0n2-prtlMHUnnLA9dTw", handle = "@AlrahmaTVOfficial",
            nameEn = "قناة الرحمة - Al Rahma TV",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.BROADCASTER, priority = 89
        ),
        verifiedScholarChannel(
            key = "alhassanat", channelId = "UCZSoID7NSlf-8PO3T5LHMWA", handle = "@ALHASSANAT",
            nameEn = "الحسنات -ALHASANAT",
            specialtyEn = "Salah & Worship, Family & Youth", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SALAH_WORSHIP, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 89
        ),
        verifiedScholarChannel(
            key = "mostafaaladwy", channelId = "UCkL2vNPCvXU1niLe7KhKFXg", handle = "@mostafaaladwy",
            nameEn = "الشيخ مصطفى العدوي",
            specialtyEn = "Quran Tafseer, Hadith & Sunnah, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.HADITH, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "shuraym1414", channelId = "UCGUK0a-a20ef34PI4X60SRQ", handle = "@shuraym1414",
            nameEn = "موقع الشيخ أ.د. سعود الشريم",
            specialtyEn = "Quran Tafseer, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "alresalahtoday", channelId = "UCtFKCoLm69Wj0RJdYHpsvyA", handle = "@AlresalahToday",
            nameEn = "Alresalah",
            specialtyEn = "Salah & Worship, Family & Youth, Fiqh & Islamic Q&A", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SALAH_WORSHIP, LectureCategory.FAMILY_SOCIETY, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "sherifali", channelId = "UC562PEzdpMULIOtinS79mzw", handle = "@sherifali",
            nameEn = "شريف علي",
            specialtyEn = "Quran Tafseer, Quran Reflections, Sahaba Biographies", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SAHABA_STORIES, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "makanyworld", channelId = "UC2hgUdruvEzm4-sxTWrP2zg", handle = "@MakanyWorld",
            nameEn = "فقه النفس/ مكاني Makany",
            specialtyEn = "Family & Youth", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "saad_alghamidi_live", channelId = "UCPOIoIgnoOVoSCH9IwDHhsw", handle = "@Saad_Alghamidi_live",
            nameEn = "القناة الرسمية للشيخ سعدالغامدي بث مباشر",
            specialtyEn = "Quran Tafseer, Quran Reflections, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.BROADCASTER, priority = 85
        ),
        verifiedScholarChannel(
            key = "drrshaal", channelId = "UCKRBJMyGkVHMfP4IxfF11EQ", handle = "@drrshaal",
            nameEn = "درر الدكتور محمد خير الشعال",
            specialtyEn = "Family & Youth", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "drshaaltube", channelId = "UC-O2jn0wYbHNO_81yKX1lgQ", handle = "@DrShaalTube",
            nameEn = "د.محمد خير الشعال",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "almunajjid", channelId = "UCEtzpou7x4iq1r8PxSAcZxQ", handle = "@almunajjid",
            nameEn = "محمد صالح المنجد",
            specialtyEn = "Aqeedah & Dawah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "drmahmoudelmasry", channelId = "UCoxabeyx-KBuLth-cFgvwNw", handle = "@DrMahmoudElmasry",
            nameEn = "الشيخ الدكتور محمود المصرى - Dr.Mahmoud Elmasry",
            specialtyEn = "Aqeedah & Dawah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "alsaeedantv", channelId = "UCx0auuCjs_7oBkcUDqERlNA", handle = "@AlsaeedanTV",
            nameEn = "محبي الشيخ وليد السعيدان",
            specialtyEn = "Aqeedah & Dawah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "albayanalqurany", channelId = "UC5aPqshbvN4_NQvtuM0xkMw", handle = "@albayanalqurany",
            nameEn = "روائع البيان القرآني",
            specialtyEn = "Quran Tafseer, Quran Reflections, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "ch_ar_lt6cca", channelId = "UCUNFQ0Z8HAyEThg27lt6ccA", handle = "@قناةالشيخمحمدبنعليالشنقيطي",
            nameEn = "قناة الشيخ محمد بن علي الشنقيطي",
            specialtyEn = "Quran Tafseer", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER),
            channelType = ChannelType.BROADCASTER, priority = 85
        ),
        verifiedScholarChannel(
            key = "zadgroup", channelId = "UCw0OFJrMMH6N5aTyeOTTWZQ", handle = "@ZADGROUP",
            nameEn = "مجموعة زاد",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "almuaiqly10", channelId = "UCIiZpxs209kndl6lr34vBPw", handle = "@almuaiqly10",
            nameEn = "تلاوات د. ماهر المعيقلي - almuaiqly10",
            specialtyEn = "Quran Tafseer, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "emam_moathen", channelId = "UC-G8RL7iYJt5L_-7F5bRKLA", handle = "@Emam_moathen",
            nameEn = "شؤون الأئمة والمؤذنين",
            specialtyEn = "Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "ch_ar_pknyda", channelId = "UC33zSaBeWvxCQttkkPknydA", handle = "@دروسمعاليالشيخصالحبنفوزانالفوز",
            nameEn = "دروس معالي الشيخ صالح بن فوزان الفوزان",
            specialtyEn = "Fiqh & Islamic Q&A", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "efdawaharabic", channelId = "UCWDRaXP17opD9cAXPNDzuyg", handle = "@efdawaharabic",
            nameEn = "EFDawah بالعربية",
            specialtyEn = "Quran Tafseer, Quran Reflections, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_MANNERS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "alslafi", channelId = "UCKhFY4_MLxLJruPy91LRKfQ", handle = "@ALSLAFI",
            nameEn = "قناة فتاوى لشيخنا الدكتور عثمان الخميس",
            specialtyEn = "Seerah, Salah & Worship, Fiqh & Islamic Q&A", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SEERAH, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.BROADCASTER, priority = 81
        ),
        verifiedScholarChannel(
            key = "rabaniatube", channelId = "UCdwmI3CAhNtmtr2Q8n44tlA", handle = "@RabaniaTube",
            nameEn = "محمد حسين يعقوب",
            specialtyEn = "Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "almajdquranchannelofficial", channelId = "UCKg2dp4g9Y0VOmciYlwsR7g", handle = "@AlmajdQuranChannelofficial",
            nameEn = "قناة المجد للقرآن الكريم - Almajd Quran Channel HD",
            specialtyEn = "Quran Tafseer, Quran Reflections, Islamic History", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_BATTLES),
            channelType = ChannelType.BROADCASTER, priority = 81
        ),
        verifiedScholarChannel(
            key = "mshmsdin", channelId = "UCNQR1yu8BLrSihTabql3rVQ", handle = "@MShmsDin",
            nameEn = "محمد بن شمس الدين",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.STORIES_OF_PROPHETS),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "lamasatbayania", channelId = "UCET_yD8OFwj8m4DptN1bMsQ", handle = "@LamasatBayania",
            nameEn = "لمسات بيانية - أ.د. فاضل السامرائي",
            specialtyEn = "Quran Tafseer, Quran Reflections, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "alkharaztv", channelId = "UC_EcxjaRwHg-pQ083e_FB8g", handle = "@AlkharazTv",
            nameEn = "AlkharazTv",
            specialtyEn = "Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.BROADCASTER, priority = 81
        ),
        verifiedScholarChannel(
            key = "telawatyasseer1", channelId = "UClncV9OLPto_MinRzGfjr2g", handle = "@telawatyasseer1",
            nameEn = "تلاوات ياسر الدوسري إمام الحرم المكي",
            specialtyEn = "Quran Tafseer, Quran Reflections, Islamic History", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "ch_ar_kftnsa", channelId = "UCRiazAHCckrEV2RMIKFTNsA", handle = "@مقاطعالشيخالألباني",
            nameEn = "مقاطع الشيخ الألباني",
            specialtyEn = "Hadith & Sunnah, Seerah, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.HADITH, LectureCategory.SEERAH, LectureCategory.SALAH_WORSHIP, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "fatawabaly", channelId = "UC6xycbU-9FOPr1MD-u0-ORQ", handle = "@fatawabaly",
            nameEn = "فتاوى الشيخ وحيد بالي",
            specialtyEn = "Family & Youth, Fiqh & Islamic Q&A", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.FAMILY_SOCIETY, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "al_maqrami", channelId = "UC_0LNCisiIGMBqX13SGTIdw", handle = "@al_Maqrami",
            nameEn = "الشيخ(المهندس) | محمد المقرمي",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "islam_sobhi", channelId = "UCzU3jMTL1mFb3Ql9g-pRn9A", handle = "@islam_sobhi",
            nameEn = "القارىء إسلام صبحي Islam Sobhi",
            specialtyEn = "Quran Tafseer, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "mohammedhijabarabic", channelId = "UC9gF2VxscVlIWpyqOOOEAMg", handle = "@MohammedHijabarabic",
            nameEn = "محمد حجاب",
            specialtyEn = "Salah & Worship, Akhlaq & Tazkiyah, Fiqh & Islamic Q&A", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "ftawamostafaaladwy", channelId = "UCYW44APHfIo0GyAO9iosHjQ", handle = "@ftawamostafaaladwy",
            nameEn = "الشيخ مصطفى العدوي",
            specialtyEn = "Seerah, Sahaba Biographies, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "alresalahnet", channelId = "UCBVUg4nQTryjzB2mQRNTJIw", handle = "@alresalahnet",
            nameEn = "alresalahnet",
            specialtyEn = "Islamic History, Islamic Battles, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "albadr09", channelId = "UCWSfNmixfPlKg9OCoqghwwg", handle = "@albadr09",
            nameEn = "الشيخ عبد الرزاق البدر",
            specialtyEn = "Islamic Battles, Aqeedah & Dawah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_BATTLES, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "alsheikhbinbaz", channelId = "UCXI4M81wRAVYlFPw7V1l3Mw", handle = "@Alsheikhbinbaz",
            nameEn = "فضيلة الشيخ ابن باز Alsheikh Binbaz",
            specialtyEn = "Seerah, Salah & Worship, Aqeedah & Dawah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.SALAH_WORSHIP, LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "ch_ar_brk8pg", channelId = "UCLrj2x8j1akKb6HdCbrk8pg", handle = "@مؤسسة-وعي",
            nameEn = "مؤسسة وعي",
            specialtyEn = "Quran Tafseer, Quran Reflections, Family & Youth", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.INSTITUTE, priority = 77
        ),
        verifiedScholarChannel(
            key = "ch_ar_wuhe_q", channelId = "UC5IcXAcnHUQka2d48WuHE_Q", handle = "@إخلا-ص6ح",
            nameEn = "إخلاص",
            specialtyEn = "Quran Tafseer, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "zadilearning", channelId = "UCxjAohc3xfh1WbWF7kA0tRw", handle = "@zadilearning",
            nameEn = "منصة زادي للتعليم الشرعي المفتوح",
            specialtyEn = "Family & Youth, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.FAMILY_SOCIETY, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 77
        ),
        verifiedScholarChannel(
            key = "nourhanelsheikh_official", channelId = "UCVGs1aOJLsSEQzr6IM3Notg", handle = "@nourhanelsheikh-official",
            nameEn = "Nourhan Elsheikh - نورهان الشيخ",
            specialtyEn = "Seerah, Islamic History, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "drsamiameri", channelId = "UCm85scTKjex-ah3psqbykvw", handle = "@DrSamiAmeri",
            nameEn = "د. سامي عامري Dr. Sami Ameri",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "masbob2", channelId = "UCtXGrPGVWFTsfn61MBx4t1g", handle = "@masbob2",
            nameEn = "عـبـد الـغـفـور",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "dr_alnaqeb", channelId = "UCRt6WsfUsid7kuDSEMKkCQA", handle = "@dr_alnaqeb",
            nameEn = "الشيخ الدكتور أحمد النقيب - البصيرة نت",
            specialtyEn = "Quran Tafseer, Seerah, Islamic History", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.SEERAH, LectureCategory.ISLAMIC_HISTORY),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "chankititv", channelId = "UCc8dJo2VCAGsacAJpYetXNg", handle = "@chankititv",
            nameEn = "قناة الشيخ محمد محمود ولد أحمد الشيخ الشنقيطي",
            specialtyEn = "Islamic Caliphates, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.BROADCASTER, priority = 77
        ),
        verifiedScholarChannel(
            key = "abosaleh95_archive", channelId = "UCX7Byh4t365tM6j9W0drOkQ", handle = "@abosaleh95-archive",
            nameEn = "قناة عبدالله العجيري - الأرشيف",
            specialtyEn = "Seerah, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.BROADCASTER, priority = 77
        ),
        verifiedScholarChannel(
            key = "ch_ar_kfh4jg", channelId = "UCUCy4g7bcIlMi264ekFH4jg", handle = "@تسجيليمنقناةالمجدللقرانالكريم",
            nameEn = "تسجيلي من قناة المجد للقران الكريم",
            specialtyEn = "Quran Tafseer, Quran Reflections, Seerah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SEERAH),
            channelType = ChannelType.BROADCASTER, priority = 77
        ),
        verifiedScholarChannel(
            key = "ch_ar_93xagw", channelId = "UCUoYtTky7mURqrkp593xAgw", handle = "@الجامع_الأزهر",
            nameEn = "الجامع الأزهر",
            specialtyEn = "Hadith & Sunnah, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 77
        ),
        verifiedScholarChannel(
            key = "ch_ar_nlml2q", channelId = "UCc3aJf5GtcEO5AKY0NlmL2Q", handle = "@قناة-الشيخ-عبدالله-السلمي",
            nameEn = "قناة الشيخ عبدالله بن ناصر السلمي",
            specialtyEn = "Salah & Worship, Fiqh & Islamic Q&A", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.BROADCASTER, priority = 77
        ),
        verifiedScholarChannel(
            key = "samirmoustafa", channelId = "UCch6Y4YgssEzMa4Q5zw4xjw", handle = "@samirmoustafa",
            nameEn = "قناة الشيخ سمير مصطفى الرسمية",
            specialtyEn = "Akhlaq & Tazkiyah, Salah & Worship", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.BROADCASTER, priority = 77
        ),
        verifiedScholarChannel(
            key = "user_fatwa_alnabulsi", channelId = "UCl8zJ92geL5-pADM4SlHASA", handle = "@user-Fatwa_Alnabulsi",
            nameEn = "فتاوى النابلسي",
            specialtyEn = "Fiqh & Islamic Q&A, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.ISLAMIC_QA, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "ch_ar_30gg9g", channelId = "UCXuQlnqATqwbZekYT30GG9g", handle = "@3bdelmohsen",
            nameEn = "عبدالمحسن الأحمد",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "ch_ar_ny-uow", channelId = "UCBlB2owSEk4PqrkTpNY-uow", handle = "@فصيحالحجازالشيخعليالقرني",
            nameEn = "فصيح الحجاز الشيخ علي القرني",
            specialtyEn = "Salah & Worship, Akhlaq & Tazkiyah", languageCode = "ar", primaryLanguageGroup = LectureLanguageGroup.ARABIC,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        )
    )
}
