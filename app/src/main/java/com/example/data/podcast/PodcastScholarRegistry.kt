package com.example.data.podcast

/**
 * Verified Multilingual Podcast Scholar & Channel Registry for Noor-e-Hidayat 5.
 *
 * Every single UC Channel ID listed below has been live-verified against YouTube HTTPS + Atom RSS
 * (`https://www.youtube.com/feeds/videos.xml?channel_id=UC...`).
 *
 * Policy:
 * - Zero invented or guessed channel IDs.
 * - Official personal/institutional channels are strictly distinguished from Hosted Interview sources.
 * - Candidates without a verifiable official or reliable hosted channel are kept honestly marked
 *   as UNAVAILABLE (`sources = emptyList()`) instead of fabricating content or using unrelated channels.
 * - Mufti Muhammad Taqi Usmani is explicitly excluded from the Urdu candidate list.
 * - Great History of Islam (`UCRNMo453JQk8dpYsAGrIwFQ`) is listed as a separate CHANNEL, not a scholar.
 */
object PodcastScholarRegistry {

    val URDU_SCHOLARS: List<PodcastScholarProfile> = listOf(
        PodcastScholarProfile(
            id = "ur_dr_israr_ahmed",
            language = PodcastLanguage.URDU,
            displayNameNative = "ڈاکٹر اسرار احمد",
            displayNameEn = "Dr. Israr Ahmed",
            attributionAliases = listOf("israr", "ڈاکٹر اسرار", "اسرار احمد", "tanzeem", "bayan ul quran"),
            sources = listOf(
                PodcastChannelSource("UCwGzYwcG_N8rvrdS2EBQA-g", "@struglefordeen", "Tanzeem-e-Islami Official", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UCBj6SU_o5wZQJbZJZSkVzOw", "@Official-drisrarahmed", "Dr. Israr Ahmed Official Archive", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_maulana_tariq_jamil",
            language = PodcastLanguage.URDU,
            displayNameNative = "مولانا طارق جمیل",
            displayNameEn = "Maulana Tariq Jamil",
            attributionAliases = listOf("tariq jamil", "tariq jameel", "طارق جمیل", "مولانا طارق"),
            sources = listOf(
                PodcastChannelSource("UCct5L-DaVYK8UtmqGU8gJtw", "@tariqjamilofficial", "Tariq Jamil Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_mufti_tariq_masood",
            language = PodcastLanguage.URDU,
            displayNameNative = "مفتی طارق مسعود",
            displayNameEn = "Mufti Tariq Masood",
            attributionAliases = listOf("tariq masood", "طارق مسعود", "مفتی طارق"),
            sources = listOf(
                PodcastChannelSource("UCumT1HMTXegYzESyPKH3pVg", "@MuftiTariqMasoodSpeeches", "Mufti Tariq Masood Speeches", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UCnAsQQqfuBi1NJ8r4wcpr3A", "@AskMuftiTariqMasood", "Ask Mufti Tariq Masood", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_dr_farhat_hashmi",
            language = PodcastLanguage.URDU,
            displayNameNative = "ڈاکٹر فرحت ہاشمی",
            displayNameEn = "Dr. Farhat Hashmi",
            attributionAliases = listOf("farhat hashmi", "فرحت ہاشمی", "al-huda", "alhuda"),
            sources = listOf(
                PodcastChannelSource("UCFnsCaT_2VKBg_SoGjhO1nA", "@DrFarhatHashmiOfficial", "Dr. Farhat Hashmi Official", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UCNp50YO1J0oOEWbb9-0-aDA", "@alhudaofficials", "Al-Huda International", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_mufti_muhammad_akmal",
            language = PodcastLanguage.URDU,
            displayNameNative = "مفتی محمد اکمل",
            displayNameEn = "Mufti Muhammad Akmal",
            attributionAliases = listOf("mufti akmal", "mufti muhammad akmal", "مفتی اکمل", "مفتی محمد اکمل"),
            sources = listOf(
                PodcastChannelSource("UCZCcw3xHJAW4BtHopjY9aHw", "@AlFurqanNetworkofMuftiAkmal", "AlFurqan Network of Mufti Akmal", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_dr_zakir_naik",
            language = PodcastLanguage.URDU,
            displayNameNative = "ڈاکٹر ذاکر نائیک",
            displayNameEn = "Dr. Zakir Naik (Urdu)",
            attributionAliases = listOf("zakir naik", "ذاکر نائیک", "ڈاکٹر ذاکر"),
            sources = listOf(
                PodcastChannelSource("UCczZnI6hkhkdvEPwUrQVMcQ", "@DrZakirNaik1", "Dr Zakir Naik Urdu", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_peer_ajmal_raza_qadri",
            language = PodcastLanguage.URDU,
            displayNameNative = "پیر اجمل رضا قادری",
            displayNameEn = "Peer Ajmal Raza Qadri",
            attributionAliases = listOf("ajmal raza", "ajmal qadri", "peer ajmal", "اجمل رضا", "اجمل قادری"),
            sources = listOf(
                PodcastChannelSource("UC2SqSPyDNijIPVQuo14lQEQ", "@PeerAjmalRazaQadri", "Peer Ajmal Raza Qadri Official", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UCv9F8HSgt1ETYNXkxdcO5-g", "@peerajmalrazaqadri889", "Peer Ajmal Raza Qadri Speeches", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_engineer_ali_mirza",
            language = PodcastLanguage.URDU,
            displayNameNative = "انجینئر محمد علی مرزا",
            displayNameEn = "Engineer Muhammad Ali Mirza",
            attributionAliases = listOf("ali mirza", "engineer muhammad ali", "محمد علی مرزا", "انجینئر"),
            sources = listOf(
                PodcastChannelSource("UC5VYosjbcRb3yKfvl-YuJFw", "@RAAH-e-HIDAYAT-EMAM", "RAAH-e-HIDAYAT (Engineer Muhammad Ali Mirza)", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_maulana_makki_al_hijazi",
            language = PodcastLanguage.URDU,
            displayNameNative = "مولانا محمد مکی الحجازی",
            displayNameEn = "Maulana Muhammad Makki Al-Hijazi",
            attributionAliases = listOf("makki", "hijazi", "مکی الحجازی", "مولانا مکی"),
            sources = listOf(
                PodcastChannelSource("UCur0Z_lKSZbevv7nWby0NHw", "@MakkiHijazi", "Maulana Makki Al-Hijazi Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_pir_zulfiqar_naqshbandi",
            language = PodcastLanguage.URDU,
            displayNameNative = "پیر ذوالفقار احمد نقشبندی",
            displayNameEn = "Pir Zulfiqar Ahmad Naqshbandi",
            attributionAliases = listOf("zulfiqar", "naqshbandi", "ذوالفقار احمد", "نقشبندی"),
            sources = listOf(
                PodcastChannelSource("UCw5mzR5S77Lw3fGGxU1V7KQ", "@ShaykhZulfiqarAhmadOfficial", "Shaykh Zulfiqar Ahmad Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_maulana_aslam_sheikhupuri",
            language = PodcastLanguage.URDU,
            displayNameNative = "مولانا اسلم شیخوپوری",
            displayNameEn = "Maulana Aslam Sheikhupuri",
            attributionAliases = listOf("aslam sheikhupuri", "اسلم شیخوپوری"),
            sources = emptyList(),
            verificationNote = "No verified official YouTube channel with active RSS feed could be confirmed."
        ),
        PodcastScholarProfile(
            id = "ur_qari_hanif_dar",
            language = PodcastLanguage.URDU,
            displayNameNative = "قاری حنیف ڈار",
            displayNameEn = "Qari Hanif Dar",
            attributionAliases = listOf("hanif dar", "qari hanif", "حنیف ڈار", "قاری حنیف"),
            sources = listOf(
                PodcastChannelSource("UCurO5Y3V4GFZawhajwUL4Fg", "@MuhammadHanif", "Qari Hanif Dar", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_maulana_abdul_sattar",
            language = PodcastLanguage.URDU,
            displayNameNative = "مولانا عبد الستار",
            displayNameEn = "Maulana Abdul Sattar",
            attributionAliases = listOf("abdul sattar", "عبد الستار", "مولانا عبد الستار"),
            sources = emptyList(),
            verificationNote = "No verified official YouTube channel could be confirmed."
        ),
        PodcastScholarProfile(
            id = "ur_javed_ahmad_ghamidi",
            language = PodcastLanguage.URDU,
            displayNameNative = "جاوید احمد غامدی",
            displayNameEn = "Javed Ahmad Ghamidi",
            attributionAliases = listOf("ghamidi", "javed ahmad", "جاوید احمد غامدی", "غامدی"),
            sources = listOf(
                PodcastChannelSource("UCd0F5R_zECxivBm7bQoWLVQ", "@JavedGhamidiOfficial", "Javed Ahmad Ghamidi Official", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UCoZ7RCLNu8anVWrHxTlilyA", "@ghamidi_discourse", "Ghamidi Discourse", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_dr_khalid_zaheer",
            language = PodcastLanguage.URDU,
            displayNameNative = "ڈاکٹر خالد ظہیر",
            displayNameEn = "Dr. Khalid Zaheer",
            attributionAliases = listOf("khalid zaheer", "خالد ظہیر", "ڈاکٹر خالد"),
            sources = listOf(
                PodcastChannelSource("UCBqEJFY5tswFYXTuUYVNGrw", "@DrKhalidZaheer", "Dr Khalid Zaheer", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_mufti_qasim_attari",
            language = PodcastLanguage.URDU,
            displayNameNative = "مفتی محمد قاسم عطاری",
            displayNameEn = "Mufti Muhammad Qasim Attari",
            attributionAliases = listOf("qasim attari", "قاسم عطاری", "مفتی قاسم"),
            sources = listOf(
                PodcastChannelSource("UCsR1A6EpKoTZvDZvf_iD0bg", "@MuftiQasimAttari", "Mufti Qasim Attari Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_maulana_ilyas_qadri",
            language = PodcastLanguage.URDU,
            displayNameNative = "مولانا الیاس قادری",
            displayNameEn = "Maulana Ilyas Qadri",
            attributionAliases = listOf("ilyas qadri", "الیاس قادری", "امیر اہلسنت", "bapa"),
            sources = listOf(
                PodcastChannelSource("UCIQQovwY3Gws0ZFc1jzINqQ", "@MaulanaIlyasQadri", "Maulana Ilyas Qadri Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_allama_kaukab_noorani",
            language = PodcastLanguage.URDU,
            displayNameNative = "علامہ کوکب نورانی اوکاڑوی",
            displayNameEn = "Allama Kaukab Noorani Okarvi",
            attributionAliases = listOf("kaukab", "noorani", "okarvi", "کوکب نورانی", "اوکاڑوی"),
            sources = listOf(
                PodcastChannelSource("UCj0ZUxcm2EL0aTVrgHHo-Xw", "@KaukabNooraniOkarviChannel", "Kaukab Noorani Okarvi Channel", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UCJKsVTsyUl8-OlILEkenKSg", "@okarvispeeches", "Okarvi Speeches Network Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_shujauddin_sheikh",
            language = PodcastLanguage.URDU,
            displayNameNative = "شجاع الدین شیخ",
            displayNameEn = "Shujauddin Sheikh",
            attributionAliases = listOf("shujauddin", "shuja uddin", "شجاع الدین"),
            sources = listOf(
                PodcastChannelSource("UC1tjM94eePLTV0TW_158cdg", "@ShujaUddinSheikh", "Shuja Uddin Sheikh", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_qais_ahmad_raja",
            language = PodcastLanguage.URDU,
            displayNameNative = "قیس احمد راجہ",
            displayNameEn = "Qais Ahmad Raja (Qaiser Ahmed Raja)",
            attributionAliases = listOf("qaiser", "qais ahmad", "qaiser ahmed raja", "قیس احمد", "قیصر احمد راجہ"),
            sources = listOf(
                PodcastChannelSource("UCv79kSIg3FzzfWsL7n5MPIg", "@QaiserAhmedRaja", "Qaiser Ahmed Raja", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_mufti_abdul_wahid_qureshi",
            language = PodcastLanguage.URDU,
            displayNameNative = "مفتی عبد الواحد قریشی",
            displayNameEn = "Mufti Abdul Wahid Qureshi",
            attributionAliases = listOf("abdul wahid qureshi", "wahid qureshi", "عبد الواحد قریشی"),
            sources = listOf(
                PodcastChannelSource("UCWdAUT4V6BmW5vxavBVMvQw", "@MuftiAbdulWahidQureshi", "Mufti Abdul Wahid Qureshi", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_mufti_zar_wali_khan",
            language = PodcastLanguage.URDU,
            displayNameNative = "مفتی زر ولی خان",
            displayNameEn = "Mufti Zar Wali Khan",
            attributionAliases = listOf("zar wali", "زر ولی خان", "مفتی زر ولی"),
            sources = listOf(
                PodcastChannelSource("UCw_WxAtAKSvTCYA8NWepavg", "@muftizarwaliofficial", "Mufti Zar Wali Khan Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_maulana_tahir_ul_qadri",
            language = PodcastLanguage.URDU,
            displayNameNative = "مولانا طاہر القادری",
            displayNameEn = "Maulana Tahir-ul-Qadri",
            attributionAliases = listOf("tahir-ul-qadri", "tahir ul qadri", "dr qadri", "طاہر القادری"),
            sources = listOf(
                PodcastChannelSource("UC3TenRnIEWa-P-JliJv3yTQ", "@drqadri", "Shaykh-ul-Islam Dr. Tahir-ul-Qadri", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UCFJmBHxBxruCjXMJZayksbQ", "@ShaykhDrTahirUlQadriBayan", "Dr. Tahir-ul-Qadri Bayan", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_mufti_muhammad_zubair",
            language = PodcastLanguage.URDU,
            displayNameNative = "مفتی محمد زبیر",
            displayNameEn = "Mufti Muhammad Zubair",
            attributionAliases = listOf("mufti muhammad zubair", "مفتی محمد زبیر"),
            sources = emptyList(),
            verificationNote = "No verified official YouTube channel could be confirmed (unrelated lookalike channel rejected)."
        ),
        PodcastScholarProfile(
            id = "ur_mufti_salman_azhari",
            language = PodcastLanguage.URDU,
            displayNameNative = "مفتی سلمان ازہری",
            displayNameEn = "Mufti Salman Azhari",
            attributionAliases = listOf("salman azhari", "سلمان ازہری", "مفتی سلمان"),
            sources = listOf(
                PodcastChannelSource("UCAVoPfGIRVAsMaRrc7bx3RQ", "@MuftiSalmanAzhariOfficial", "Mufti Salman Azhari Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ur_maulana_muhammad_amjad",
            language = PodcastLanguage.URDU,
            displayNameNative = "مولانا محمد امجد",
            displayNameEn = "Maulana Muhammad Amjad",
            attributionAliases = listOf("maulana muhammad amjad", "مولانا محمد امجد"),
            sources = emptyList(),
            verificationNote = "No verified official YouTube channel could be confirmed."
        )
    )

    /**
     * Separate Islamic History channel inside Urdu Podcasts — explicitly modeled as a CHANNEL, NOT a scholar.
     */
    val GREAT_HISTORY_OF_ISLAM_CHANNEL: PodcastScholarProfile = PodcastScholarProfile(
        id = "ch_great_history_of_islam",
        language = PodcastLanguage.URDU,
        profileType = PodcastProfileType.CHANNEL,
        displayNameNative = "Great History of Islam",
        displayNameEn = "Great History of Islam",
        attributionAliases = listOf("great history of islam", "history", "تاریخ"),
        sources = listOf(
            PodcastChannelSource(
                channelId = "UCRNMo453JQk8dpYsAGrIwFQ",
                handle = "@greathistoryofislam",
                channelName = "Great History of Islam",
                sourceKind = PodcastSourceKind.OFFICIAL_CHANNEL,
                requiresScholarAttribution = false
            )
        ),
        verificationNote = "Verified Islamic history channel (HTTP 200 RSS)."
    )

    val ENGLISH_SCHOLARS: List<PodcastScholarProfile> = listOf(
        PodcastScholarProfile(
            id = "en_mufti_menk",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Mufti Menk",
            displayNameEn = "Mufti Menk",
            attributionAliases = listOf("mufti menk", "ismail menk", "menk"),
            sources = listOf(
                PodcastChannelSource("UCNB_OaI4524fASt8h0IL8dw", "@muftimenkofficial", "Mufti Menk Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_omar_suleiman",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Omar Suleiman",
            displayNameEn = "Dr. Omar Suleiman",
            attributionAliases = listOf("omar suleiman", "suleiman"),
            sources = listOf(
                PodcastChannelSource("UCtm8rtofLSnaIBi3noB0INg", "@OmarSuleimanPersonal", "Omar Suleiman Personal", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UC3vHW2h22WE-pNi5WJtRIjg", "@yaqeeninstituteofficial", "Yaqeen Institute", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_yasir_qadhi",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Yasir Qadhi",
            displayNameEn = "Dr. Yasir Qadhi",
            attributionAliases = listOf("yasir qadhi", "qadhi"),
            sources = listOf(
                PodcastChannelSource("UClUa7-iHJNKEM2e_zWYSPQg", "@YasirQadhi", "Dr. Yasir Qadhi", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_nouman_ali_khan",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Nouman Ali Khan",
            displayNameEn = "Nouman Ali Khan",
            attributionAliases = listOf("nouman ali khan", "nouman", "bayyinah"),
            sources = listOf(
                PodcastChannelSource("UCRtiU-lpcBSi-ipFKyfIkug", "@bayyinah", "Bayyinah Institute (Nouman Ali Khan)", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_sheikh_assim_al_hakeem",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Sheikh Assim Al-Hakeem",
            displayNameEn = "Sheikh Assim Al-Hakeem",
            attributionAliases = listOf("assim al hakeem", "assim al-hakeem", "assimalhakeem", "assim"),
            sources = listOf(
                PodcastChannelSource("UCWsdcrre0WbCWML_PnuzoAg", "@assimalhakeem", "Sheikh Assim Al-Hakeem", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_sheikh_belal_assaad",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Sheikh Belal Assaad",
            displayNameEn = "Sheikh Belal Assaad",
            attributionAliases = listOf("belal assaad", "belal"),
            sources = listOf(
                PodcastChannelSource("UC34dxKHa9n9ggxz4m7bztfA", "@belal.assaad", "Shaykh Belal Assaad", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_sheikh_hamza_yusuf",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Sheikh Hamza Yusuf",
            displayNameEn = "Sheikh Hamza Yusuf",
            attributionAliases = listOf("hamza yusuf", "sandala", "zaytuna"),
            sources = listOf(
                PodcastChannelSource("UC361kz8bZYcYuF7k5j2kXdw", "@SandalaMediaCenter", "Hamza Yusuf (Sandala)", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UC88lF82aS_VW3oJe7GV5zsA", "@Zaytuna", "Zaytuna College", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_bilal_philips",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Bilal Philips",
            displayNameEn = "Dr. Bilal Philips",
            attributionAliases = listOf("bilal philips", "abu ameenah"),
            sources = listOf(
                PodcastChannelSource("UCk1-R7Mmzd7iikof378dcuA", "@aabphilips", "Dr. Bilal Philips Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_yasmin_mogahed",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Yasmin Mogahed",
            displayNameEn = "Yasmin Mogahed",
            attributionAliases = listOf("yasmin mogahed", "mogahed"),
            sources = listOf(
                PodcastChannelSource("UCsDTwDhGICLG1U06ZtVwRXQ", "@yasminmogahedofficial", "Yasmin Mogahed Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_mohamed_hoblos",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Mohamed Hoblos",
            displayNameEn = "Mohamed Hoblos",
            attributionAliases = listOf("mohamed hoblos", "mohammed hoblos", "hoblos"),
            sources = listOf(
                PodcastChannelSource("UCQHRLH8RQIrdGWMhf5heWiA", "@OnePathNetwork", "OnePath Network", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true),
                PodcastChannelSource("UCz4AXmFeSbi-0vPXI102Q5w", "@MuslimCentral", "Muslim Central", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            ),
            verificationNote = "Hosted interviews and discussions from verified platforms (lookalike @MohamedHoblos channel rejected)."
        ),
        PodcastScholarProfile(
            id = "en_sheikh_abu_bakr_zoud",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Sheikh Abu Bakr Zoud",
            displayNameEn = "Sheikh Abu Bakr Zoud",
            attributionAliases = listOf("abu bakr zoud", "abubakr zoud", "zoud"),
            sources = listOf(
                PodcastChannelSource("UCF5EboPxUSzbWt4OqMr3JYQ", "@abubakrzoud", "Abu Bakr Zoud Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_ustadh_ali_hammuda",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Ustadh Ali Hammuda",
            displayNameEn = "Ustadh Ali Hammuda",
            attributionAliases = listOf("ali hammuda", "hammuda"),
            sources = listOf(
                PodcastChannelSource("UC5_hrgirl1FHITUPOYrALVw", "@Ali.Hammuda", "Ali Hammuda Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_shabir_ally",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Shabir Ally",
            displayNameEn = "Dr. Shabir Ally",
            attributionAliases = listOf("shabir ally", "shabir", "let the quran speak"),
            sources = listOf(
                PodcastChannelSource("UCaADzQEp9eYu9PTmJ28_-HQ", "@QuranSpeaks", "Let the Quran Speak (Dr. Shabir Ally)", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_uthman_ibn_farooq",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Uthman Ibn Farooq",
            displayNameEn = "Uthman Ibn Farooq",
            attributionAliases = listOf("uthman ibn farooq", "ibn farooq"),
            sources = listOf(
                PodcastChannelSource("UCc17C9v22IjmvgsgxzuNUvw", "@UthmanIbnFarooqOfficial", "Uthman Ibn Farooq Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_shaykh_abdul_nasir_jangda",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Shaykh Abdul Nasir Jangda",
            displayNameEn = "Shaykh Abdul Nasir Jangda",
            attributionAliases = listOf("abdul nasir jangda", "abdulnasir jangda", "jangda"),
            sources = listOf(
                PodcastChannelSource("UCVp5Ze24V2iFiUY1y0WtRaA", "@QalamInstitute", "Qalam Institute", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "en_mikaeel_smith",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Mikaeel Smith",
            displayNameEn = "Mikaeel Smith",
            attributionAliases = listOf("mikaeel smith", "mikaeel ahmed smith", "mikaeel"),
            sources = listOf(
                PodcastChannelSource("UC4S5N_fMAgW6W2B9KWmH2EA", "@mikaeelahmedsmith", "Mikaeel Ahmed Smith Official", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UCVp5Ze24V2iFiUY1y0WtRaA", "@QalamInstitute", "Qalam Institute", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_haifaa_younis",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Haifaa Younis",
            displayNameEn = "Dr. Haifaa Younis",
            attributionAliases = listOf("haifaa younis", "haifaa", "jannah institute"),
            sources = listOf(
                PodcastChannelSource("UCx1Zr8zUKUHUtHEVq09b5vg", "@jannahinstitute", "Jannah Institute (Dr. Haifaa Younis)", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_abdelrahman_murphy",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "AbdelRahman Murphy",
            displayNameEn = "AbdelRahman Murphy",
            attributionAliases = listOf("abdelrahman murphy", "abdel rahman murphy", "murphy"),
            sources = listOf(
                PodcastChannelSource("UCWv1QqcaD2yWVWj0468eAnw", "@RootsCommunity", "Roots Community", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true),
                PodcastChannelSource("UCVp5Ze24V2iFiUY1y0WtRaA", "@QalamInstitute", "Qalam Institute", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_shadee_elmasry",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Shadee Elmasry",
            displayNameEn = "Dr. Shadee Elmasry",
            attributionAliases = listOf("shadee elmasry", "shadee", "safina society"),
            sources = listOf(
                PodcastChannelSource("UC2cX3SmsdWsrRS8t_5zvzEw", "@SafinaSociety", "Safina Society (Dr. Shadee Elmasry)", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_suhaib_webb",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Suhaib Webb",
            displayNameEn = "Suhaib Webb",
            attributionAliases = listOf("suhaib webb", "webb"),
            sources = listOf(
                PodcastChannelSource("UC4KC9OS3dZZcoNhY__KFSog", "@ImamSuhaibWebb", "Imam Suhaib Webb Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_jonathan_brown",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Jonathan Brown",
            displayNameEn = "Dr. Jonathan Brown",
            attributionAliases = listOf("jonathan brown", "jonathan ac brown", "dr. brown"),
            sources = listOf(
                PodcastChannelSource("UC3vHW2h22WE-pNi5WJtRIjg", "@yaqeeninstituteofficial", "Yaqeen Institute", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true),
                PodcastChannelSource("UC4L23Z_agh1qeV_odQfV6Vg", "@DrJonathanBrownUnofficial", "Dr. Jonathan Brown Seminar Archive", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "en_tim_humble",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Tim Humble",
            displayNameEn = "Tim Humble",
            attributionAliases = listOf("tim humble", "muhammad tim humble", "ustadh muhammad tim"),
            sources = listOf(
                PodcastChannelSource("UCTsey7p1BAigPbHMof1-f-g", "@muhammadtim", "Muhammad Tim Humble Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_shaykh_hasan_ali",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Shaykh Hasan Ali",
            displayNameEn = "Shaykh Hasan Ali",
            attributionAliases = listOf("hasan ali", "shaykh hasan ali"),
            sources = listOf(
                PodcastChannelSource("UCJX37FkXKvFYFGihom_9NYg", "@ShaykhHasanAli", "Shaykh Hasan Ali Official", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_mohammed_faqih",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Mohammed Faqih",
            displayNameEn = "Dr. Mohammed Faqih",
            attributionAliases = listOf("mohammed faqih", "muhammad faqih", "faqih"),
            sources = emptyList(),
            verificationNote = "No verified official YouTube channel could be confirmed."
        ),
        PodcastScholarProfile(
            id = "en_shaykh_yusuf_estes",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Shaykh Yusuf Estes",
            displayNameEn = "Shaykh Yusuf Estes",
            attributionAliases = listOf("yusuf estes", "estes"),
            sources = listOf(
                PodcastChannelSource("UCNqPFtRFrXRmxNTGATbBjgA", "@GuideUSTV", "GuideUS TV (Yusuf Estes)", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "en_dr_ingrid_mattson",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Dr. Ingrid Mattson",
            displayNameEn = "Dr. Ingrid Mattson",
            attributionAliases = listOf("ingrid mattson", "mattson"),
            sources = listOf(
                PodcastChannelSource("UC24pRTbkopITR452GXMyHJg", "@CelebrateMercy", "CelebrateMercy", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true),
                PodcastChannelSource("UCz4AXmFeSbi-0vPXI102Q5w", "@MuslimCentral", "Muslim Central", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "en_imam_zaid_shakir",
            language = PodcastLanguage.ENGLISH,
            displayNameNative = "Imam Zaid Shakir",
            displayNameEn = "Imam Zaid Shakir",
            attributionAliases = listOf("zaid shakir", "imam zaid"),
            sources = listOf(
                PodcastChannelSource("UC3IOKCBxB_fZy5HPkbb9DnA", "@ZaidShakir", "Imam Zaid Shakir Official", PodcastSourceKind.OFFICIAL_CHANNEL),
                PodcastChannelSource("UC88lF82aS_VW3oJe7GV5zsA", "@Zaytuna", "Zaytuna College", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        )
    )

    val ARABIC_SCHOLARS: List<PodcastScholarProfile> = listOf(
        PodcastScholarProfile(
            id = "ar_othman_al_khamis",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عثمان الخميس",
            displayNameEn = "Sheikh Othman Al-Khamis",
            attributionAliases = listOf("عثمان الخميس", "الخميس", "othman"),
            sources = listOf(
                PodcastChannelSource("UCWjCSGhmSGu0VLf2mPFS0Kg", "@othmanalkamees", "الشيخ د. عثمان الخميس", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_saleh_al_fawzan",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ صالح الفوزان",
            displayNameEn = "Sheikh Saleh Al-Fawzan",
            attributionAliases = listOf("صالح الفوزان", "الفوزان"),
            sources = listOf(
                PodcastChannelSource("UCi7vzSJrU3beV_6Sdgpowng", "@dralfawzann", "العلامة د. صالح الفوزان", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_abdul_razzaq_al_badr",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عبد الرزاق البدر",
            displayNameEn = "Sheikh Abdul Razzaq Al-Badr",
            attributionAliases = listOf("عبد الرزاق البدر", "عبدالرزاق البدر", "البدر"),
            sources = listOf(
                PodcastChannelSource("UCB6m3UVQVt2oh86rmmu87sA", "@sheikhalbadr", "الشيخ عبد الرزاق البدر", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_ibn_uthaymeen",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ محمد بن صالح العثيمين",
            displayNameEn = "Sheikh Muhammad ibn Salih al-Uthaymeen",
            attributionAliases = listOf("العثيمين", "ابن عثيمين"),
            sources = listOf(
                PodcastChannelSource("UCtF3YygTiodnYSw8vD3UJtQ", "@ibnothaimeentv", "قناة الشيخ ابن عثيمين الرسمية", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_ibn_baz",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عبد العزيز بن باز",
            displayNameEn = "Sheikh Abdul Aziz ibn Baz",
            attributionAliases = listOf("بن باز", "ابن باز"),
            sources = listOf(
                PodcastChannelSource("UCXI4M81wRAVYlFPw7V1l3Mw", "@Alsheikhbinbaz", "فضيلة الشيخ ابن باز", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_abdul_muhsin_al_abbad",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عبد المحسن العباد",
            displayNameEn = "Sheikh Abdul Muhsin Al-Abbad",
            attributionAliases = listOf("عبد المحسن العباد", "عبدالمحسن العباد", "العباد"),
            sources = emptyList(),
            verificationNote = "No verified official YouTube channel could be confirmed (unrelated lookalike handle rejected)."
        ),
        PodcastScholarProfile(
            id = "ar_saleh_al_maghamsi",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ صالح المغامسي",
            displayNameEn = "Sheikh Saleh Al-Maghamsi",
            attributionAliases = listOf("صالح المغامسي", "المغامسي"),
            sources = listOf(
                PodcastChannelSource("UCfpli4VHoS12syPkPxHl7XA", "@Alrasekhoon", "الشيخ صالح المغامسي", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_nabil_al_awadi",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ نبيل العوضي",
            displayNameEn = "Sheikh Nabil Al-Awadi",
            attributionAliases = listOf("نبيل العوضي", "العوضي"),
            sources = listOf(
                PodcastChannelSource("UCpPZKwayH_pWPF20Ls0hgVw", "@نبيل-العوضي", "الشيخ نبيل العوضي", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_omar_abdelkafy",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عمر عبد الكافي",
            displayNameEn = "Sheikh Omar Abdelkafy",
            attributionAliases = listOf("عمر عبد الكافي", "عبد الكافي"),
            sources = listOf(
                PodcastChannelSource("UCKUOmGXE9Ytlc2EzpGqimtw", "@abdelkafytube", "الدكتور عمر عبد الكافي", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_ratib_al_nabulsi",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الدكتور محمد راتب النابلسي",
            displayNameEn = "Dr. Muhammad Ratib Al-Nabulsi",
            attributionAliases = listOf("راتب النابلسي", "النابلسي"),
            sources = listOf(
                PodcastChannelSource("UC7naRnmAOTwDPu738W2SljQ", "@nabulsiencyclopedia", "موسوعة النابلسي للعلوم الإسلامية", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_badr_al_mishari",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ بدر المشاري",
            displayNameEn = "Sheikh Badr Al-Mishari",
            attributionAliases = listOf("بدر المشاري", "المشاري"),
            sources = listOf(
                PodcastChannelSource("UCxf8crdLQZdblya8ak-LWPQ", "@Sheikh-Badr-Al-Mashari", "الشيخ بدر المشاري", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_mishary_al_kharraz",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ مشاري الخراز",
            displayNameEn = "Sheikh Mishary Al-Kharraz",
            attributionAliases = listOf("مشاري الخراز", "الخراز"),
            sources = listOf(
                PodcastChannelSource("UC_EcxjaRwHg-pQ083e_FB8g", "@AlkharazTv", "قناة الشيخ مشاري الخراز الرسمية", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_mustafa_hosny",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الدكتور مصطفى حسني",
            displayNameEn = "Dr. Mustafa Hosny",
            attributionAliases = listOf("مصطفى حسني", "حسني"),
            sources = listOf(
                PodcastChannelSource("UCwttCEi9Fq1DgTd5Anil6rQ", "@MustafaHosny", "مصطفى حسني - Mustafa Hosny", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_amr_khaled",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الدكتور عمرو خالد",
            displayNameEn = "Dr. Amr Khaled",
            attributionAliases = listOf("عمرو خالد"),
            sources = listOf(
                PodcastChannelSource("UCjxcF4A7pCyWsuJcZOymF_g", "@AmrKhaled", "الدكتور عمرو خالد", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_saeed_al_kamali",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ سعيد الكملي",
            displayNameEn = "Sheikh Saeed Al-Kamali",
            attributionAliases = listOf("سعيد الكملي", "الكملي"),
            sources = listOf(
                PodcastChannelSource("UCASAOTD6fMPEg8jmO4nPblQ", "@elkamali", "قناة الشيخ سعيد الكملي", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_muhammad_al_dedew",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ محمد الحسن الددو",
            displayNameEn = "Sheikh Muhammad Al-Hassan Al-Dedew",
            attributionAliases = listOf("الددو", "محمد الحسن الددو"),
            sources = listOf(
                PodcastChannelSource("UCifZUETjBgcsFaS-ewfb3YQ", "@dedewiat", "القناة الرسمية للشيخ محمد الحسن الددو", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_abdulkarim_bakkar",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عبد الكريم بكار",
            displayNameEn = "Dr. Abdulkarim Bakkar",
            attributionAliases = listOf("عبد الكريم بكار", "عبدالكريم بكار", "بكار"),
            sources = listOf(
                PodcastChannelSource("UCTa3en_LdRAExVevt-Lu--w", "@dr.bakkar", "د. عبد الكريم بكار", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_ahmad_al_sayyid",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ أحمد السيد",
            displayNameEn = "Sheikh Ahmad Al-Sayyid",
            attributionAliases = listOf("أحمد السيد", "احمد السيد"),
            sources = listOf(
                PodcastChannelSource("UC7AbKZa30XPMInZIsViiBOQ", "@alsayyed_ah", "الشيخ أحمد السيد", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_abdulaziz_al_tarifi",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عبد العزيز الطريفي",
            displayNameEn = "Sheikh Abdulaziz Al-Tarifi",
            attributionAliases = listOf("عبد العزيز الطريفي", "الطريفي"),
            sources = emptyList(),
            verificationNote = "No verified active official YouTube channel could be confirmed."
        ),
        PodcastScholarProfile(
            id = "ar_khalid_al_rashed",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ خالد الراشد",
            displayNameEn = "Sheikh Khalid Al-Rashed",
            attributionAliases = listOf("خالد الراشد", "الراشد"),
            sources = emptyList(),
            verificationNote = "No verified official YouTube channel with active long-form feed could be confirmed."
        ),
        PodcastScholarProfile(
            id = "ar_ali_al_qarni",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ علي القرني",
            displayNameEn = "Sheikh Ali Al-Qarni",
            attributionAliases = listOf("علي القرني", "الشيخ علي القرني"),
            sources = listOf(
                PodcastChannelSource("UCBlB2owSEk4PqrkTpNY-uow", "@فصيحالحجازالشيخعليالقرني", "فصيح الحجاز الشيخ علي القرني", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = false)
            )
        ),
        PodcastScholarProfile(
            id = "ar_saleh_al_sheikh",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ صالح آل الشيخ",
            displayNameEn = "Sheikh Saleh Aal Al-Sheikh",
            attributionAliases = listOf("صالح آل الشيخ", "آل الشيخ"),
            sources = listOf(
                PodcastChannelSource("UCLHZET13eDxW-z1tSKTAdVg", "@Salihalalsheikh", "فضيلة الشيخ صالح آل الشيخ", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_abdullah_al_muslih",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عبد الله المصلح",
            displayNameEn = "Sheikh Abdullah Al-Muslih",
            attributionAliases = listOf("عبد الله المصلح", "المصلح"),
            sources = emptyList(),
            verificationNote = "No verified active official YouTube channel could be confirmed."
        ),
        PodcastScholarProfile(
            id = "ar_muhammad_al_arefe",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ محمد العريفي",
            displayNameEn = "Sheikh Muhammad Al-Arefe",
            attributionAliases = listOf("العريفي", "محمد العريفي", "alarefe"),
            sources = listOf(
                PodcastChannelSource("UCdC_KOXojzyy8eN36X1Wiow", "@hamode656", "مقاطع الشيخ محمد العريفي (Archive)", PodcastSourceKind.HOSTED_INTERVIEW, requiresScholarAttribution = true)
            )
        ),
        PodcastScholarProfile(
            id = "ar_aidh_al_qarni",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عائض القرني",
            displayNameEn = "Sheikh Aidh Al-Qarni",
            attributionAliases = listOf("عائض القرني", "د. عائض القرني"),
            sources = listOf(
                PodcastChannelSource("UCeFb4KW80fOV2ltjU31uznA", "@drqarnee", "القناة الرسمية للشيخ د. عائض القرني", PodcastSourceKind.OFFICIAL_CHANNEL)
            )
        ),
        PodcastScholarProfile(
            id = "ar_abdullah_ibn_jibreen",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عبد الله بن جبرين",
            displayNameEn = "Sheikh Abdullah Ibn Jibreen",
            attributionAliases = listOf("ابن جبرين", "بن جبرين"),
            sources = listOf(
                PodcastChannelSource("UC3gOv15bWP78IqsaT3h2Maw", "@ibnjebreen", "جوال ابن جبرين الرسمي", PodcastSourceKind.OFFICIAL_CHANNEL)
            ),
            verificationNote = "Official channel verified (HTTP 200); displays actual eligible uploads when published."
        ),
        PodcastScholarProfile(
            id = "ar_abdullah_al_mutlaq",
            language = PodcastLanguage.ARABIC,
            displayNameNative = "الشيخ عبد الله المطلق",
            displayNameEn = "Sheikh Abdullah Al-Mutlaq",
            attributionAliases = listOf("عبد الله المطلق", "المطلق"),
            sources = emptyList(),
            verificationNote = "No verified active official YouTube channel could be confirmed."
        )
    )

    fun profilesForLanguage(language: PodcastLanguage): List<PodcastScholarProfile> = when (language) {
        PodcastLanguage.URDU -> URDU_SCHOLARS + GREAT_HISTORY_OF_ISLAM_CHANNEL
        PodcastLanguage.ENGLISH -> ENGLISH_SCHOLARS
        PodcastLanguage.ARABIC -> ARABIC_SCHOLARS
    }

    fun scholarsOnlyForLanguage(language: PodcastLanguage): List<PodcastScholarProfile> = when (language) {
        PodcastLanguage.URDU -> URDU_SCHOLARS
        PodcastLanguage.ENGLISH -> ENGLISH_SCHOLARS
        PodcastLanguage.ARABIC -> ARABIC_SCHOLARS
    }

    fun channelsOnlyForLanguage(language: PodcastLanguage): List<PodcastScholarProfile> = when (language) {
        PodcastLanguage.URDU -> listOf(GREAT_HISTORY_OF_ISLAM_CHANNEL)
        PodcastLanguage.ENGLISH, PodcastLanguage.ARABIC -> emptyList()
    }

    fun allProfiles(): List<PodcastScholarProfile> =
        URDU_SCHOLARS + GREAT_HISTORY_OF_ISLAM_CHANNEL + ENGLISH_SCHOLARS + ARABIC_SCHOLARS

    fun findProfileById(id: String): PodcastScholarProfile? =
        allProfiles().firstOrNull { it.id == id }

    fun findById(id: String): PodcastScholarProfile? = findProfileById(id)

    fun scholarsForLanguage(language: PodcastLanguage): List<PodcastScholarProfile> =
        scholarsOnlyForLanguage(language)

    fun channelsForLanguage(language: PodcastLanguage): List<PodcastScholarProfile> =
        channelsOnlyForLanguage(language)
}
