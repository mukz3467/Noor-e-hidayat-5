package com.example.data.lectures

import com.example.data.repository.LectureCategory
import com.example.data.repository.VerifiedIslamicChannel

/**
 * Additional live-verified Urdu / Hindi Islamic scholar, institute, and history channels
 * (82 channels). Every UC Channel ID was resolved from YouTube and live-tested
 * against official YouTube Atom RSS feeds returning HTTP 200 XML.
 */
internal object UrduHindiScholarChannelsExtra {

    val CHANNELS: List<VerifiedIslamicChannel> = listOf(
        verifiedScholarChannel(
            key = "tareekh_e_islam_ur", channelId = "UCPVTMNnJLpapkfc3hh5KONQ", handle = "@tareekh-e-islam6221",
            nameEn = "Tareekh-e-Islam Urdu", nameUr = "تاریخِ اسلام اردو", nameAr = "تاريخ الإسلام بالأردية", nameHi = "तारीख़-ए-इस्लाम",
            specialtyEn = "Islamic History, Battles & Stories of Prophets", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_BATTLES, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.EDUCATIONAL, priority = 95
        ),
        verifiedScholarChannel(
            key = "waqiyat_e_islam_hi", channelId = "UCemmlDNwVpj6JLqiRosInPA", handle = "@waqiyateislamhindi",
            nameEn = "Waqiyat-e-Islam Urdu/Hindi", nameUr = "واقعاتِ اسلام", nameAr = "وقائع التاريخ الإسلامي", nameHi = "वाक़िआत-ए-इस्लाम",
            specialtyEn = "Islamic History, Battles, Sahaba & Stories of Prophets", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SAHABA_STORIES, LectureCategory.STORIES_OF_PROPHETS),
            channelType = ChannelType.EDUCATIONAL, priority = 95
        ),
        verifiedScholarChannel(
            key = "the_islamic_history", channelId = "UCx8DxncMGxZ_4AQRit0P6bw", handle = "@TheIslamicHistoryofficial",
            nameEn = "The Islamic History", nameUr = "دی اسلامک ہسٹری", nameAr = "التاريخ الإسلامي", nameHi = "द इस्लामिक हिस्ट्री",
            specialtyEn = "Islamic History, Seerah & Companions", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_BATTLES, LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.EDUCATIONAL, priority = 99
        ),
        verifiedScholarChannel(
            key = "islamic_history_urdu", channelId = "UC-H027-gGt41rgPqNICb2nA", handle = "@IslamicHistoryUrduOfficial",
            nameEn = "Islamic History Urdu", nameUr = "اسلامی تاریخ اردو", nameAr = "التاريخ الإسلامي بالأردية", nameHi = "इस्लामिक हिस्ट्री उर्दू",
            specialtyEn = "Islamic History, Caliphates & Historical Events in Urdu", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_BATTLES, LectureCategory.STORIES_OF_PROPHETS),
            channelType = ChannelType.EDUCATIONAL, priority = 97
        ),
        verifiedScholarChannel(
            key = "askmuftitariqmasood", channelId = "UCnAsQQqfuBi1NJ8r4wcpr3A", handle = "@AskMuftiTariqMasood",
            nameEn = "Ask Mufti Tariq Masood",
            specialtyEn = "Sahaba Biographies, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "tariqmasoodofficial", channelId = "UChE-B08gBYDdokkpDS8ib-g", handle = "@TariqMasoodOfficial",
            nameEn = "Tariq Masood Exclusive",
            specialtyEn = "Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "muftitariqmasood", channelId = "UCC3pnIpWMVail5J8mvYEiZA", handle = "@muftitariqmasood",
            nameEn = "Mufti Tariq Masood",
            specialtyEn = "Sahaba Biographies, Salah & Worship, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "drzakirnaik1", channelId = "UCczZnI6hkhkdvEPwUrQVMcQ", handle = "@DrZakirNaik1",
            nameEn = "Dr Zakir Naik",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "aryqtvofficial", channelId = "UCE2oY4S4wOu6WofU2YbfWsw", handle = "@ARYQtvofficial",
            nameEn = "ARY Qtv",
            specialtyEn = "Quran Tafseer, Quran Reflections, Sahaba Biographies", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_MANNERS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.BROADCASTER, priority = 92
        ),
        verifiedScholarChannel(
            key = "raah_e_hidayat_emam", channelId = "UC5VYosjbcRb3yKfvl-YuJFw", handle = "@RAAH-e-HIDAYAT-EMAM",
            nameEn = "RAAH-e-HIDAYAT [Engineer Muhammad Ali Mirza]",
            specialtyEn = "Quran Tafseer, Sahaba Biographies, Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 92
        ),
        verifiedScholarChannel(
            key = "kidsmadanichannel", channelId = "UCsNSt0fxMniSH9XHaNneZfw", handle = "@KidsMadaniChannel",
            nameEn = "Kids Madani Channel",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.BROADCASTER, priority = 89
        ),
        verifiedScholarChannel(
            key = "takrirvideochannel", channelId = "UCwD0nXfFEwYBw_hRf-lraeQ", handle = "@TakrirVideoChannel",
            nameEn = "Takrir Video Channel",
            specialtyEn = "Hadith & Sunnah, Sahaba Biographies, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.HADITH, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.BROADCASTER, priority = 85
        ),
        verifiedScholarChannel(
            key = "pirsaqibshaami9176", channelId = "UCTXRpUjesDqSt0EFloLUW1w", handle = "@pirsaqibshaami9176",
            nameEn = "PIR SAQIB SHAAMI",
            specialtyEn = "Family & Youth", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "muftisalmanazhariofficial", channelId = "UCAVoPfGIRVAsMaRrc7bx3RQ", handle = "@MuftiSalmanAzhariOfficial",
            nameEn = "Mufti Salman Azhari",
            specialtyEn = "Stories of Prophets, Salah & Worship, Aqeedah & Dawah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "mufti_saeed_arshad_al_hussaini", channelId = "UCmhYY2ffOTgeXU87-lEha1w", handle = "@mufti_saeed_arshad_al_hussaini",
            nameEn = "Mufti Saeed Arshad Al Hussaini {Official}",
            specialtyEn = "Stories of Prophets, Sahaba Biographies, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "iplustvdeeniqa", channelId = "UCoX6tV8HvXUmeIR_9SHscVA", handle = "@iPlusTVDeeniQA",
            nameEn = "iPlus TV Deeni Q&A",
            specialtyEn = "Family & Youth, Aqeedah & Dawah, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.FAMILY_SOCIETY, LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.BROADCASTER, priority = 85
        ),
        verifiedScholarChannel(
            key = "iplustvikhtilafkahal", channelId = "UC1QVPLdd972vdOlLz__kycQ", handle = "@iPlusTVikhtilafKaHal",
            nameEn = "iPlus TV ikhtilaf Ka Hal",
            specialtyEn = "Seerah, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SEERAH, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.BROADCASTER, priority = 85
        ),
        verifiedScholarChannel(
            key = "tafheemulquranfull", channelId = "UC-zAw960DhOlFccDGwPUA5w", handle = "@TafheemUlQuranFull",
            nameEn = "Tafheem Ul Quran Full",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "alkauthar_institute", channelId = "UCXBL9YagfmdHqToWmLFlKGg", handle = "@alkauthar.institute",
            nameEn = "Al Kauthar Institute",
            specialtyEn = "Stories of Prophets, Sahaba Biographies", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.INSTITUTE, priority = 85
        ),
        verifiedScholarChannel(
            key = "jarjeesansariofficial", channelId = "UCKPgqGsRuH4uqYE42138ADw", handle = "@jarjeesansariofficial",
            nameEn = "Jarjees Ansari Official",
            specialtyEn = "Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "ahnafmediaservices", channelId = "UCZPrjAhKsnwrG_taNqapNQw", handle = "@AhnafMediaServices",
            nameEn = "Ahnaf Media Services",
            specialtyEn = "Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.BROADCASTER, priority = 85
        ),
        verifiedScholarChannel(
            key = "islamknowledgeonly", channelId = "UCqjTcfF_oH3h4hytq7TREMQ", handle = "@IslamKnowledgeonly",
            nameEn = "Farhat Hashmi - Quran For EveryOne",
            specialtyEn = "Quran Tafseer, Quran Reflections, Sahaba Biographies", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.SCHOLAR, priority = 85
        ),
        verifiedScholarChannel(
            key = "meezan786", channelId = "UCUae_8oNBVsTCQoExPPN_PA", handle = "@meezan786",
            nameEn = "Meezan",
            specialtyEn = "Quran Tafseer, Quran Reflections, Islamic History", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.ISLAMIC_HISTORY, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "muftihassanattari", channelId = "UCtuyo2F_LRHNKAxZo5zrS8w", handle = "@MuftiHassanAttari",
            nameEn = "Mufti Muhammad Hassan Attari",
            specialtyEn = "Seerah, Sahaba Biographies, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "daruliftaahlesunnat", channelId = "UC5iUfTm7RzAFQ8OTBXOfybg", handle = "@DaruliftaAhleSunnat",
            nameEn = "Darulifta AhleSunnat",
            specialtyEn = "Stories of Prophets, Salah & Worship, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.INSTITUTE, priority = 81
        ),
        verifiedScholarChannel(
            key = "lifewithhamza3633", channelId = "UCsGDQ85yca34zqv3oWt543Q", handle = "@Lifewithhamza3633",
            nameEn = "Muhammad Ali Speaks",
            specialtyEn = "Sahaba Biographies, Salah & Worship, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "muftifazalhamdard", channelId = "UCkMqLzG3P9xX-UdQlp2-FlQ", handle = "@MuftiFazalHamdard",
            nameEn = "Mufti Fazal Hamdard",
            specialtyEn = "Sahaba Biographies, Salah & Worship, Aqeedah & Dawah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "noortvjeddah", channelId = "UC3zVJnKnGf4JTs3u4BAT5NQ", handle = "@NoorTVJeddah",
            nameEn = "Noor TV Jeddah",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.BROADCASTER, priority = 81
        ),
        verifiedScholarChannel(
            key = "yasiraljabrimadni", channelId = "UCCDVOMB1vr4ouvSmD8uZLZw", handle = "@Yasiraljabrimadni",
            nameEn = "Shaikh Yasir AlJabri Madani",
            specialtyEn = "Quran Reflections, Seerah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SEERAH),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "muftiqasimattari", channelId = "UCsR1A6EpKoTZvDZvf_iD0bg", handle = "@MuftiQasimAttari",
            nameEn = "Mufti Qasim Attari",
            specialtyEn = "Stories of Prophets, Salah & Worship, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.POPULAR_SCHOLARS, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "peerajmalrazaqadri889", channelId = "UCv9F8HSgt1ETYNXkxdcO5-g", handle = "@peerajmalrazaqadri889",
            nameEn = "PEER AJMAL RAZA QADRI",
            specialtyEn = "Islamic History, Sahaba Biographies, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_HISTORY, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "muftiabdulraheem", channelId = "UCaIDDjlLDZSINECCOsGjqPg", handle = "@muftiabdulraheem",
            nameEn = "Mufti Abdul Raheem",
            specialtyEn = "Seerah, Stories of Prophets, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 81
        ),
        verifiedScholarChannel(
            key = "islamicinformationcentremumbai", channelId = "UC-FKgD7JCJ6BoLZTdPtHeTw", handle = "@islamicinformationcentremumbai",
            nameEn = "Islamic Information Centre Mumbai",
            specialtyEn = "Quran Tafseer, Quran Reflections, Sahaba Biographies", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 81
        ),
        verifiedScholarChannel(
            key = "delhimarkazvoice", channelId = "UCROjMQ-J8PDT2UEN09zWiXw", handle = "@delhimarkazvoice",
            nameEn = "Delhi Markaz Voice",
            specialtyEn = "Salah & Worship, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 77
        ),
        verifiedScholarChannel(
            key = "rahamtv", channelId = "UCoE5iXkOiM0-08a0Cdg0ACg", handle = "@RahamTV",
            nameEn = "Raham TV",
            specialtyEn = "Hadith & Sunnah, Seerah, Sahaba Biographies", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.BROADCASTER, priority = 77
        ),
        verifiedScholarChannel(
            key = "muftirasheedofficial", channelId = "UCxHRfFUsaLm5yxAkETB1rOg", handle = "@MuftiRasheedofficial",
            nameEn = "Mufti Rasheed Official",
            specialtyEn = "Sahaba Biographies, Salah & Worship, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "muftiadnankakakhail", channelId = "UCRv0foldueVzZl9kyb1AuNQ", handle = "@MuftiAdnanKakakhail",
            nameEn = "Mufti Syed Adnan Kakakhail",
            specialtyEn = "Stories of Prophets, Salah & Worship, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "allamanasirabbasofficial", channelId = "UCgjRvd3nIe2jfz-Og97jV6A", handle = "@allamanasirabbasofficial",
            nameEn = "Allama Nasir Abbas official",
            specialtyEn = "Sahaba Biographies, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "molanakaleemsiddiquiphulat", channelId = "UCn6NxfINoi_Cp-_JMhbiyeA", handle = "@MolanaKaleemSiddiquiPhulat",
            nameEn = "Maulana Kaleem Siddiqui",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "darsequrandaily", channelId = "UCc-vmCTXq8zrI-53f4pFBJw", handle = "@DarsequranDaily",
            nameEn = "Darsequran Daily",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "maulviwithanattitude", channelId = "UCFbPWf0TC9uby01Uq0GSuvw", handle = "@MaulviWithAnAttitude",
            nameEn = "Maulvi With An Attitude",
            specialtyEn = "Stories of Prophets, Family & Youth", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "muftispeeches1", channelId = "UCvtL6z_7amT3Z199UQGW6JQ", handle = "@muftispeeches1",
            nameEn = "Mufti Abdul Wahid Qureshi Speeches",
            specialtyEn = "Quran Tafseer, Akhlaq & Tazkiyah, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.ISLAMIC_MANNERS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "muftiabdulwahidqureshi", channelId = "UCWdAUT4V6BmW5vxavBVMvQw", handle = "@MuftiAbdulWahidQureshi",
            nameEn = "Mufti Abdul Wahid Qureshi",
            specialtyEn = "Stories of Prophets, Salah & Worship, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "abdulghaffarofficial", channelId = "UCjY1gR7nkUneHmhkbaokiUQ", handle = "@AbdulGhaffarOfficial",
            nameEn = "Abdul Ghaffar Official",
            specialtyEn = "Sahaba Biographies, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "aj_official", channelId = "UCEiYBcJPr34kHs2ol_qHCYA", handle = "@AJ.Official",
            nameEn = "AJ Official",
            specialtyEn = "Stories of Prophets, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "jamaateislamihind", channelId = "UCZvCS6XjKfR3UeHBgN7Kk0g", handle = "@JamaateIslamiHind",
            nameEn = "Jamaat-e-Islami Hind",
            specialtyEn = "Hadith & Sunnah, Islamic History, Sahaba Biographies", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "almawridofficial", channelId = "UCHDtHmCv7QIcc7Ye1jhKC_A", handle = "@AlMawridOfficial",
            nameEn = "Al-Mawrid Official",
            specialtyEn = "Hadith & Sunnah, Islamic History, Sahaba Biographies", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_CALIPHATES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "quranacademyfsd", channelId = "UCaSVUs8W76G7vwV88MwjYlA", handle = "@quranacademyfsd",
            nameEn = "Quran Academy Fsd",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.INSTITUTE, priority = 77
        ),
        verifiedScholarChannel(
            key = "arshadmadani007", channelId = "UCSQSaetiyKbwobPkPft1Rgw", handle = "@ArshadMadani007",
            nameEn = "Arshad Madani",
            specialtyEn = "Hadith & Sunnah, Islamic History, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "drqadri", channelId = "UC3TenRnIEWa-P-JliJv3yTQ", handle = "@drqadri",
            nameEn = "Shaykh-ul-Islam Dr Muhammad Tahir-ul-Qadri",
            specialtyEn = "Quran Tafseer, Quran Reflections, Seerah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.SEERAH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "deeniyatofficial404", channelId = "UCeuL0y3_UXzGEITlbmOStwQ", handle = "@deeniyatofficial404",
            nameEn = "Deeniyat Official",
            specialtyEn = "Family & Youth, Aqeedah & Dawah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.FAMILY_SOCIETY, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "thedrjalali", channelId = "UCiQoMsK_mCObTvPjuKBcPzg", handle = "@TheDrJalali",
            nameEn = "Dr Muhammad Ashraf Asif Jalali",
            specialtyEn = "Seerah, Sahaba Biographies", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.SCHOLAR, priority = 77
        ),
        verifiedScholarChannel(
            key = "okarvispeeches", channelId = "UCJKsVTsyUl8-OlILEkenKSg", handle = "@okarvispeeches",
            nameEn = "Okarvi Speeches Network Official",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "mtmspeeche", channelId = "UCggXnpOoeQbVu0lz-hms63A", handle = "@MTMSpeeche",
            nameEn = "MTM Speeches",
            specialtyEn = "Sahaba Biographies, Family & Youth, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.FAMILY_SOCIETY, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "kaukabnooraniokarvichannel", channelId = "UCj0ZUxcm2EL0aTVrgHHo-Xw", handle = "@KaukabNooraniOkarviChannel",
            nameEn = "Kaukab Noorani Okarvi Channel",
            specialtyEn = "Islamic Battles, Akhlaq & Tazkiyah, Aqeedah & Dawah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_BATTLES, LectureCategory.ISLAMIC_MANNERS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.BROADCASTER, priority = 73
        ),
        verifiedScholarChannel(
            key = "abdulgaffarsalafiofficial", channelId = "UCBhc77pS0OB9wiaLLZYLOog", handle = "@AbdulGaffarSalafiOfficial",
            nameEn = "Abdul Gaffar Salafi Official",
            specialtyEn = "Family & Youth, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.FAMILY_SOCIETY, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "yasirnadeemalwajidi", channelId = "UCeFxNORbefqSBEIL0AXNIXQ", handle = "@YasirNadeemalWajidi",
            nameEn = "Yasir Nadeem al Wajidi",
            specialtyEn = "Hadith & Sunnah, Stories of Prophets, Family & Youth", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.FAMILY_SOCIETY, LectureCategory.NEW_MUSLIM_EDUCATION, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "qarikhalil", channelId = "UCmLKM_ZALDrIoMJvbUTvM-Q", handle = "@qarikhalil",
            nameEn = "Abu-Insha Dr. Khalil ur Rehman Jawaid",
            specialtyEn = "Sahaba Biographies, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "silsilainaqshbandia", channelId = "UCrylAfAk0O39Y1i5brsIUaA", handle = "@SilsilaiNaqshbandia",
            nameEn = "Silsila Naqshbandia",
            specialtyEn = "Akhlaq & Tazkiyah, Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "fanmuftiakmal", channelId = "UCZsJeUMbzZa0MmNp3bNmwFQ", handle = "@FanMuftiAkmal",
            nameEn = "Fan Mufti Akmal",
            specialtyEn = "Akhlaq & Tazkiyah, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "ghamidi_discourse", channelId = "UCoZ7RCLNu8anVWrHxTlilyA", handle = "@ghamidi_discourse",
            nameEn = "Ghamidi discourse",
            specialtyEn = "Quran Tafseer, Quran Reflections, Stories of Prophets", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES, LectureCategory.FAMILY_SOCIETY, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "shaykhdrtahirulqadribayan", channelId = "UCFJmBHxBxruCjXMJZayksbQ", handle = "@ShaykhDrTahirUlQadriBayan",
            nameEn = "Shaykh Dr Tahir Ul Qadri Bayan",
            specialtyEn = "Seerah, Sahaba Biographies, Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.SAHABA_STORIES, LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "adv_faizsyedlecture", channelId = "UCIwvwrGOWFVBPY9qeh3COzg", handle = "@Adv.FaizSyedLecture",
            nameEn = "ADV. Faiz Syed Lecture",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "askmuftihanifqureshi", channelId = "UC4n6-BKCWScQbsbXdF0lUlA", handle = "@AskMuftiHanifQureshi",
            nameEn = "Ask Mufti Hanif Qureshi",
            specialtyEn = "Seerah, Stories of Prophets, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "maulanamanzooryousufofficial", channelId = "UCRKYFFOPqp2ijauI0qFyW9g", handle = "@MaulanaManzoorYousufOfficial",
            nameEn = "Maulana Manzoor Yousuf Official",
            specialtyEn = "Hadith & Sunnah, Seerah, Stories of Prophets", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.SEERAH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "oldbayanat774", channelId = "UCwGyPrKrqBuwKErCeNRefAw", handle = "@oldbayanat774",
            nameEn = "Old Bayanat NO ADS Tariq Jameel",
            specialtyEn = "Seerah, Islamic Battles, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.ISLAMIC_BATTLES, LectureCategory.ISLAMIC_MANNERS, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "ayattoayatqurantafseer", channelId = "UC1niFsIQ4H3tFHUCO1j-R9g", handle = "@ayattoayatqurantafseer",
            nameEn = "Ayat to Ayat Quran Tafseer",
            specialtyEn = "Quran Tafseer, Quran Reflections", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "baitussalam", channelId = "UC_1nJC5P33mKsTXJjXeH9vA", handle = "@Baitussalam",
            nameEn = "Baitussalam",
            specialtyEn = "Family & Youth, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.FAMILY_SOCIETY, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "the_faizofficial", channelId = "UCZBDI5smNGxO0DyfHMGMs_A", handle = "@the.faizofficial",
            nameEn = "Faiz Official",
            specialtyEn = "Akhlaq & Tazkiyah, Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "paigham_tv_ur_hi", channelId = "UCr-aPoKaFR0qxuKGmLTYy9g", handle = "@Paigham-TV",
            nameEn = "Paigham TV Official",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.BROADCASTER, priority = 73
        ),
        verifiedScholarChannel(
            key = "ulemaeludhiana", channelId = "UC8ABZTh9HWuQhsjzbJZLF0A", handle = "@UlemaeLudhiana",
            nameEn = "Ulema e Ludhiana",
            specialtyEn = "Sahaba Biographies, Fiqh & Islamic Q&A", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "maulanailyasghummanhd_3", channelId = "UCCtbtgCaxdNVXyvIq0T0N6w", handle = "@MaulanailyasghummanHD.3",
            nameEn = "Maulana ilyas ghuman HD.3",
            specialtyEn = "Seerah, Islamic History", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.ISLAMIC_HISTORY),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "aalamimazhabislam", channelId = "UCVkc_zPEP9q3z7xCpYA1k4A", handle = "@AalamiMazhabIslam",
            nameEn = "AALAMI ISLAM",
            specialtyEn = "Hadith & Sunnah, Family & Youth", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.FAMILY_SOCIETY),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "muftifazalahmadchishtiofficial", channelId = "UCLpregBsxLzsQTig6-olfXw", handle = "@MuftiFazalAhmadChishtiOfficial",
            nameEn = "Mufti Fazal Ahmad Chishti Official",
            specialtyEn = "Hadith & Sunnah, Sahaba Biographies, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_MANNERS, LectureCategory.ISLAMIC_QA),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "dr_sulemanmisbahi_786", channelId = "UCEKncj0dTFDi_DSkwnb306A", handle = "@Dr.SulemanMisbahi_786",
            nameEn = "Dr. Muhammad Suleman Misbahi",
            specialtyEn = "Akhlaq & Tazkiyah, Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "syedjawadnaqvi_official", channelId = "UC5q41mpGBlCIaXc43iG4qwQ", handle = "@SyedJawadNaqvi-Official",
            nameEn = "Syed Jawad Naqvi",
            specialtyEn = "Akhlaq & Tazkiyah, Quran Reflections, Salah & Worship", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.ISLAMIC_MANNERS, LectureCategory.QURAN_EXPLANATION, LectureCategory.SALAH_WORSHIP),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "raheetedal", channelId = "UCb6fu7LE2YxC6sXznYWtZ0A", handle = "@raheetedal",
            nameEn = "Rahe Etedal (Jalaluddin Qasmi)",
            specialtyEn = "Salah & Worship, Akhlaq & Tazkiyah, Aqeedah & Dawah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SALAH_WORSHIP, LectureCategory.ISLAMIC_MANNERS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "ghamidi_amic", channelId = "UCUONrNyB2vdCxH8lZy-Iarg", handle = "@Ghamidi-AMIC",
            nameEn = "Al-Mawrid Institute Canada",
            specialtyEn = "Quran Tafseer, Quran Reflections, Hadith & Sunnah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.QURAN_TAFSEER, LectureCategory.QURAN_EXPLANATION, LectureCategory.HADITH, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES),
            channelType = ChannelType.INSTITUTE, priority = 73
        ),
        verifiedScholarChannel(
            key = "sahiladeem", channelId = "UCA8ItldZ8-9j8nQ4S8M8k0w", handle = "@SahilAdeem",
            nameEn = "Sahil Adeem",
            specialtyEn = "Hadith & Sunnah, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 73
        ),
        verifiedScholarChannel(
            key = "syedmuzaffarshahqadriofficial", channelId = "UCtOwtUP6MeSwbCzeDoW1CTQ", handle = "@Syedmuzaffarshahqadriofficial",
            nameEn = "Allama Syed Muzaffar Shah Qadri",
            specialtyEn = "Seerah, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.SEERAH, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "allamaibtisamelahizaheer", channelId = "UC9ebJgp_g8fHrUWQldm7Ktg", handle = "@AllamaIbtisamElahiZaheer",
            nameEn = "Allama Ibtisam Elahi Zaheer",
            specialtyEn = "Stories of Prophets, Akhlaq & Tazkiyah", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.STORIES_OF_PROPHETS, LectureCategory.ISLAMIC_MANNERS),
            channelType = ChannelType.SCHOLAR, priority = 68
        ),
        verifiedScholarChannel(
            key = "taimoorahmedtalks", channelId = "UC9jx2fj_dcLa_ewkoSEq7SA", handle = "@TaimoorAhmedTalks",
            nameEn = "Taimoor Ahmed",
            specialtyEn = "Hadith & Sunnah, Islamic History, Stories of Prophets", languageCode = "ur", primaryLanguageGroup = LectureLanguageGroup.URDU_HINDI,
            primaryCategories = listOf(LectureCategory.HADITH, LectureCategory.ISLAMIC_HISTORY, LectureCategory.STORIES_OF_PROPHETS, LectureCategory.SAHABA_STORIES, LectureCategory.ISLAMIC_MANNERS, LectureCategory.NEW_MUSLIM_EDUCATION),
            channelType = ChannelType.SCHOLAR, priority = 68
        )
    )
}
