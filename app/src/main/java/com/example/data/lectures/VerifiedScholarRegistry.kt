package com.example.data.lectures

import com.example.data.repository.LectureCategory
import com.example.data.repository.VerifiedIslamicChannel

/**
 * Central registry combining all 325 live-verified Urdu/Hindi (108), English (109), and
 * Arabic (108) Islamic scholar, institute, and Islamic history YouTube channels
 * (0 fabricated IDs — every UC Channel ID verified via YouTube & live HTTP 200 Atom RSS),
 * together with curated topic-to-channel mappings across all 16 Islamic lecture categories.
 */
internal object VerifiedScholarRegistry {

    val VERIFIED_CHANNELS: List<VerifiedIslamicChannel> =
        UrduHindiScholarChannels.CHANNELS +
            UrduHindiScholarChannelsExtra.CHANNELS +
            EnglishScholarChannels.CHANNELS +
            EnglishScholarChannelsExtra.CHANNELS +
            ArabicScholarChannels.CHANNELS +
            ArabicScholarChannelsExtra.CHANNELS

    val TOPIC_CHANNEL_MAPPING: Map<LectureCategory, List<String>> = mapOf(
        LectureCategory.POPULAR_SCHOLARS to listOf(
            "dr_israr_ahmed", "tanzeem_islami", "dr_zakir_naik", "yaqeen", "bayyinah",
            "mufti_menk", "yasir_qadhi", "mufti_taqi_usmani", "farhat_hashmi", "tariq_masood",
            "tariq_jamil", "engineer_ali_mirza", "assim", "othman_alkamees", "mustafa_hosny",
            "eyad_qunaibi", "tareekh_e_islam_ur", "omar_of_the_orient", "jihad_alturbani_100_great", "waqiyat_e_islam_hi",
            "the_seerah_timeline", "early_history_of_islam_ar", "the_islamic_history", "islamic_history_podcast", "altareekh_alislami_ar",
            "islamic_history_urdu", "al_muqaddimah", "mahmoudalhasanat", "askmuftitariqmasood", "history_of_islam_nm",
            "i7sannet", "tariqmasoodofficial", "muslim_heritage", "quranradio_ksa", "muftitariqmasood",
            "quranweekly", "cheikhimamomarabdelkafy6541", "drzakirnaik1", "dreyadqunaibiglobalchannel", "yasseraldosry",
            "aryqtvofficial", "islamic_guidance", "abdullahrushdy", "raah_e_hidayat_emam", "islamicguidancechannelhd",
            "alsharawiofficial", "kidsmadanichannel", "justalayman"
        ),
        LectureCategory.LATEST_LECTURES to listOf(
            "dr_israr_ahmed", "tanzeem_islami", "dr_zakir_naik", "yaqeen", "bayyinah",
            "mufti_menk", "yasir_qadhi", "mufti_taqi_usmani", "farhat_hashmi", "tariq_masood",
            "tariq_jamil", "othman_alkamees", "zad_tv", "ragheb_elsergany", "tareekh_e_islam_ur",
            "omar_of_the_orient", "jihad_alturbani_100_great", "waqiyat_e_islam_hi", "the_seerah_timeline", "early_history_of_islam_ar",
            "the_islamic_history", "islamic_history_podcast", "altareekh_alislami_ar", "islamic_history_urdu", "al_muqaddimah",
            "mahmoudalhasanat", "askmuftitariqmasood", "history_of_islam_nm", "i7sannet", "tariqmasoodofficial",
            "muslim_heritage", "quranradio_ksa", "muftitariqmasood", "quranweekly", "cheikhimamomarabdelkafy6541",
            "drzakirnaik1", "dreyadqunaibiglobalchannel", "yasseraldosry", "aryqtvofficial", "islamic_guidance",
            "abdullahrushdy", "raah_e_hidayat_emam", "islamicguidancechannelhd", "alsharawiofficial", "kidsmadanichannel",
            "justalayman", "nabulsi1", "takrirvideochannel"
        ),
        LectureCategory.QURAN_TAFSEER to listOf(
            "bayyinah", "seekersguidance", "dr_israr_ahmed", "tanzeem_islami", "mufti_taqi_usmani",
            "farhat_hashmi", "al_huda_international", "javed_ghamidi", "yasir_qadhi", "epic_masjid",
            "amau_official", "muslim_central", "tafsir_center", "ibn_uthaymeen", "ratib_alnabulsi",
            "zad_academy", "islamweb", "othman_alkamees", "drzakirnaik1", "quranweekly",
            "quranradio_ksa", "aryqtvofficial", "dreyadqunaibiglobalchannel", "yasseraldosry", "raah_e_hidayat_emam",
            "omarsuleimanpersonal", "nabulsi1", "kidsmadanichannel", "islamickidsvideos", "sheikhsaadalghamdi",
            "tafheemulquranfull", "alqalaminstitute", "hazzaalblushi_v9h", "islamknowledgeonly", "isna_canada",
            "hazzaalblushi", "meezan786", "qariziyaadpatel", "alqtami", "noortvjeddah",
            "alhudaonline", "ch_ar_wicepw", "islamicinformationcentremumbai", "mohamadbaajourlectures", "alrahmatvofficial",
            "molanakaleemsiddiquiphulat", "tzubair", "mostafaaladwy"
        ),
        LectureCategory.QURAN_EXPLANATION to listOf(
            "dr_israr_ahmed", "tanzeem_islami", "bayyinah", "dr_zakir_naik", "yaqeen",
            "farhat_hashmi", "youth_club", "sajjad_nomani", "javed_ghamidi", "cambridge_muslim_college",
            "jannah_institute", "suleiman_hani", "tafsir_center", "saleh_almaghamsi", "eyad_qunaibi",
            "islamweb", "saudi_quran", "drzakirnaik1", "quranweekly", "quranradio_ksa",
            "aryqtvofficial", "dreyadqunaibiglobalchannel", "cheikhimamomarabdelkafy6541", "kidsmadanichannel", "omarsuleimanpersonal",
            "yasseraldosry", "tafheemulquranfull", "islamickidsvideos", "abdullahrushdy", "islamknowledgeonly",
            "ustadhwahajtarin", "sheikhsaadalghamdi", "meezan786", "drbilalphilipsclips", "elnastv",
            "noortvjeddah", "wajihuddinofficial", "hazzaalblushi_v9h", "yasiraljabrimadni", "isna_canada",
            "hazzaalblushi", "islamicinformationcentremumbai", "qariziyaadpatel", "alqtami", "molanakaleemsiddiquiphulat",
            "alhudaonline", "alrahmatvofficial", "darsequrandaily"
        ),
        LectureCategory.HADITH to listOf(
            "tanzeem_islami", "mufti_taqi_usmani", "farhat_hashmi", "engineer_ali_mirza", "seekersguidance",
            "safina_society", "amau_official", "qalam", "vric", "othman_alkamees",
            "saleh_alfawzan", "abdulrazzaq_albadr", "sulaiman_alruhayli", "abdulkarim_alkhudair", "ibn_uthaymeen",
            "saeed_alkamali", "zad_academy", "saudi_sunnah", "takrirvideochannel", "uthmanibnfarooqofficial",
            "alrahmatvofficial", "noortvjeddah", "scdawah", "mostafaaladwy", "rahamtv",
            "themuslimlantern", "mshmsdin", "darsequrandaily", "renovatiojournal", "ch_ar_kftnsa",
            "jamaateislamihind", "samdawahchannel", "ch_ar_93xagw", "almawridofficial", "international_open_university",
            "quranacademyfsd", "theeastlondonmosque", "arshadmadani007", "sunnahdiscourse", "yasirnadeemalwajidi",
            "assuffainstitute", "adv_faizsyedlecture", "isna_canada", "maulanamanzooryousufofficial", "alkawtharacademy",
            "aalamimazhabislam", "considerislamlivekennybomer", "muftifazalahmadchishtiofficial"
        ),
        LectureCategory.SEERAH to listOf(
            "dr_israr_ahmed", "tanzeem_islami", "yasir_qadhi", "yaqeen", "mufti_menk",
            "the_seerah_timeline", "qalam", "epic_masjid", "tariq_jamil", "ajmal_qadri",
            "raza_saqib_mustafai", "salman_nadwi", "othman_alkamees", "saeed_alkamali", "nabil_alawadi",
            "badr_almishari", "ali_alsallabi", "ragheb_elsergany", "the_islamic_history", "mahmoud_masry123",
            "iplustvikhtilafkahal", "history_of_islam_nm", "dorrosnabilel_awadi", "muftihassanattari", "islamic_guidance",
            "ch_ar_wicepw", "yasiraljabrimadni", "islamickidsvideos", "alslafi", "muftiabdulraheem",
            "routetojannahinstitute1312", "ch_ar_kftnsa", "rahamtv", "seerahofprophetmuhammad8385", "ftawamostafaaladwy",
            "drqadri", "alsheikhbinbaz", "thedrjalali", "nourhanelsheikh_official", "shaykhdrtahirulqadribayan",
            "dr_alnaqeb", "askmuftihanifqureshi", "abosaleh95_archive", "maulanamanzooryousufofficial", "ch_ar_kfh4jg",
            "oldbayanat774", "maulanailyasghummanhd_3", "syedmuzaffarshahqadriofficial"
        ),
        LectureCategory.ISLAMIC_HISTORY to listOf(
            "the_islamic_history", "islamic_history_podcast", "omar_of_the_orient", "al_muqaddimah", "history_of_islam_nm",
            "islamic_history_urdu", "jihad_alturbani_100_great", "early_history_of_islam_ar", "altareekh_alislami_ar", "tareekh_e_islam_ur",
            "waqiyat_e_islam_hi", "ragheb_elsergany", "ali_alsallabi", "tareq_alsuwaidan", "dr_israr_ahmed",
            "tanzeem_islami", "zamana_gawah_hai", "yasir_qadhi", "yaqeen", "muslim_heritage",
            "the_seerah_timeline", "cambridge_muslim_college", "zaytuna_college", "almadina", "safina_society",
            "onepath_network", "tariq_jamil", "salman_nadwi", "othman_alkamees", "islamweb",
            "almajdquranchannelofficial", "meezan786", "telawatyasseer1", "noortvjeddah", "alresalahnet",
            "peerajmalrazaqadri889", "uthmanibnfarooqofficial", "nourhanelsheikh_official", "jamaateislamihind", "dr_tareqalsuwaidan_en",
            "dr_alnaqeb", "almawridofficial", "isna_canada", "arshadmadani007", "bloggingtheology",
            "maulanailyasghummanhd_3", "taimoorahmedtalks"
        ),
        LectureCategory.STORIES_OF_PROPHETS to listOf(
            "mufti_menk", "dr_israr_ahmed", "bayyinah", "yasir_qadhi", "yaqeen",
            "abu_taymiyyah", "belal_assaad", "tariq_jamil", "ajmal_qadri", "irc_tv",
            "othman_alkamees", "nabil_alawadi", "omar_abdelkafy", "saleh_almaghamsi", "ali_alsallabi",
            "mustafa_hosny", "tareekh_e_islam_ur", "waqiyat_e_islam_hi", "miftaahinstitute", "alsharawiofficial",
            "omarsuleimanpersonal", "alqtami", "islamic_history_urdu", "sandalamediacenter", "ch_ar_jxqweq",
            "muftisalmanazhariofficial", "islamickidsvideos", "mshmsdin", "mufti_saeed_arshad_al_hussaini", "theeastlondonmosque",
            "alkauthar_institute", "islamicevent", "meezan786", "isna_canada", "daruliftaahlesunnat",
            "abuibrahim_mm", "muftiqasimattari", "qariziyaadpatel", "muftiabdulraheem", "alkawtharacademy",
            "muftiadnankakakhail", "officialadamscenter", "maulviwithanattitude", "lovee_islam", "muftiabdulwahidqureshi",
            "rihlatulilm", "aj_official", "shaykhhamzacom"
        ),
        LectureCategory.ISLAMIC_BATTLES to listOf(
            "the_islamic_history", "islamic_history_podcast", "omar_of_the_orient", "jihad_alturbani_100_great", "early_history_of_islam_ar",
            "altareekh_alislami_ar", "tareekh_e_islam_ur", "waqiyat_e_islam_hi", "ragheb_elsergany", "ali_alsallabi",
            "badr_almishari", "nabil_alawadi", "dr_israr_ahmed", "yasir_qadhi", "yaqeen",
            "qalam", "the_seerah_timeline", "onepath_network", "tariq_jamil", "ajmal_qadri",
            "salman_nadwi", "othman_alkamees", "islamic_history_urdu", "al_muqaddimah", "almajdquranchannelofficial",
            "kaukabnooraniokarvichannel", "islamicguidancechannelhd", "telawatyasseer1", "oldbayanat774", "haithamalhaddad_official",
            "alresalahnet", "albadr09"
        ),
        LectureCategory.SAHABA_STORIES to listOf(
            "yaqeen", "yasir_qadhi", "dr_israr_ahmed", "qalam", "abu_taymiyyah",
            "the_islamic_history", "islamic_history_podcast", "history_of_islam_nm", "jihad_alturbani_100_great", "tariq_jamil",
            "ajmal_qadri", "raza_saqib_mustafai", "salman_nadwi", "othman_alkamees", "ali_alsallabi",
            "badr_almishari", "nabil_alawadi", "saeed_alkamali", "tareekh_e_islam_ur", "the_seerah_timeline",
            "waqiyat_e_islam_hi", "early_history_of_islam_ar", "altareekh_alislami_ar", "askmuftitariqmasood", "islamic_guidance",
            "waie", "muftitariqmasood", "islamicguidancechannelhd", "sherifali", "aryqtvofficial",
            "justalayman", "ftawamostafaaladwy", "raah_e_hidayat_emam", "uthmanibnfarooqofficial", "takrirvideochannel",
            "miftaahinstitute", "mufti_saeed_arshad_al_hussaini", "themuslimlantern", "alkauthar_institute", "samdawahchannel",
            "islamknowledgeonly", "islamickidsvideos", "muftihassanattari", "alidawah", "lifewithhamza3633",
            "assuffainstitute", "muftifazalhamdard", "shaykhsaqibiqbal"
        ),
        LectureCategory.ISLAMIC_CALIPHATES to listOf(
            "islamic_history_podcast", "omar_of_the_orient", "al_muqaddimah", "the_islamic_history", "islamic_history_urdu",
            "jihad_alturbani_100_great", "early_history_of_islam_ar", "altareekh_alislami_ar", "ragheb_elsergany", "ali_alsallabi",
            "tareq_alsuwaidan", "muslim_heritage", "dr_israr_ahmed", "tanzeem_islami", "zamana_gawah_hai",
            "yasir_qadhi", "yaqeen", "cambridge_muslim_college", "zaytuna_college", "almadina",
            "safina_society", "salman_nadwi", "ajmal_qadri", "badr_almishari", "othman_alkamees",
            "almawridofficial", "history_of_islam_nm", "chankititv", "abuibrahim_mm"
        ),
        LectureCategory.SALAH_WORSHIP to listOf(
            "assim", "dr_zakir_naik", "mufti_menk", "seekersguidance", "huda_tv",
            "mufti_taqi_usmani", "farhat_hashmi", "tariq_masood", "irc_tv", "makki_al_hijazi",
            "othman_alkamees", "saleh_alfawzan", "saad_alkhathlan", "sulaiman_alruhayli", "dar_al_ifta",
            "saudi_quran", "saudi_sunnah", "muftitariqmasood", "justalayman", "i7sannet",
            "raah_e_hidayat_emam", "ustadhwahajtarin", "cheikhimamomarabdelkafy6541", "muftisalmanazhariofficial", "drbilalphilipsclips",
            "abdullahrushdy", "jarjeesansariofficial", "imanforce25", "mahmoud_masry123", "meezan786",
            "theeastlondonmosque", "elnastv", "daruliftaahlesunnat", "islamicevent", "hazzaalblushi",
            "lifewithhamza3633", "epicislamiclectures", "alqtami", "muftifazalhamdard", "rootscommunity",
            "alhassanat", "muftiqasimattari", "qariziyaadpatel", "mostafaaladwy", "delhimarkazvoice",
            "alhudaonline", "shuraym1414", "muftirasheedofficial"
        ),
        LectureCategory.ISLAMIC_MANNERS to listOf(
            "mufti_menk", "yaqeen", "qalam", "almadina", "jannah_institute",
            "belal_assaad", "mufti_taqi_usmani", "tariq_jamil", "zulfiqar_naqshbandi", "ajmal_qadri",
            "raza_saqib_mustafai", "mustafa_hosny", "abdulrazzaq_albadr", "omar_abdelkafy", "hazem_shouman",
            "muftitariqmasood", "quranweekly", "cheikhimamomarabdelkafy6541", "aryqtvofficial", "omarsuleimanpersonal",
            "abdullahrushdy", "lifewithhamza3633", "sandalamediacenter", "elnastv", "peerajmalrazaqadri889",
            "renovatiojournal", "hazzaalblushi_v9h", "islamicinformationcentremumbai", "efdawah", "alqtami",
            "delhimarkazvoice", "ustadhwahajtarin", "ch_ar_jxqweq", "muftirasheedofficial", "international_open_university",
            "mostafaaladwy", "muftiadnankakakhail", "drbilalphilipsclips", "saad_alghamidi_live", "muftispeeches1",
            "imanforce25", "drshaaltube", "abdulghaffarofficial", "wajihuddinofficial", "albayanalqurany",
            "aj_official", "epicislamiclectures", "zadgroup"
        ),
        LectureCategory.FAMILY_SOCIETY to listOf(
            "mufti_menk", "qalam", "almadina", "jannah_institute", "belal_assaad",
            "mcc_east_bay", "tariq_masood", "farhat_hashmi", "youth_club", "irc_tv",
            "sajjad_nomani", "tuaha_ibn_jalil", "mustafa_hosny", "omar_abdelkafy", "ratib_alnabulsi",
            "aziz_farhan", "almajd_channels", "pirsaqibshaami9176", "assuffainstitute", "mahmoudalhasanat",
            "iplustvdeeniqa", "rootscommunity", "alsharawiofficial", "lifewithhamza3633", "isna_canada",
            "alhassanat", "maulviwithanattitude", "shaykhsaqibiqbal", "alresalahtoday", "deeniyatofficial404",
            "mohamadbaajourlectures", "sherifali", "mtmspeeche", "officialadamscenter", "makanyworld",
            "abdulgaffarsalafiofficial", "isnavideos", "drrshaal", "yasirnadeemalwajidi", "therahmahfoundationwomen",
            "ch_ar_kftnsa", "ghamidi_discourse", "propheticguidance", "fatawabaly", "oldbayanat774",
            "quranacademy945", "ch_ar_brk8pg", "baitussalam"
        ),
        LectureCategory.NEW_MUSLIM_EDUCATION to listOf(
            "dr_zakir_naik", "peace_tv_official", "assim", "mufti_menk", "seekersguidance",
            "sapience_institute", "iera_org", "why_islam", "huda_tv", "tariq_masood",
            "youth_club", "irc_tv", "meraj_rabbani", "eyad_qunaibi", "islamweb",
            "zad_academy", "saleh_alfawzan", "aryqtvofficial", "uthmanibnfarooqofficial", "quranradio_ksa",
            "muftisalmanazhariofficial", "scdawah", "aluthaymeen", "iplustvdeeniqa", "themuslimlantern",
            "alrahmatvofficial", "muftifazalhamdard", "efdawah", "almunajjid", "jamaateislamihind",
            "samdawahchannel", "drmahmoudelmasry", "deeniyatofficial404", "alidawah", "alsaeedantv",
            "kaukabnooraniokarvichannel", "sunnahdiscourse", "efdawaharabic", "yasirnadeemalwajidi", "shaykhsaqibiqbal",
            "albadr09", "shaykhdrtahirulqadribayan", "considerislamlivekennybomer", "alsheikhbinbaz", "raheetedal",
            "tzubair", "taimoorahmedtalks", "yasminmogahedofficial"
        ),
        LectureCategory.ISLAMIC_QA to listOf(
            "dr_zakir_naik", "assim", "seekersguidance", "yasir_qadhi", "huda_tv",
            "safina_society", "mufti_taqi_usmani", "tariq_masood", "engineer_ali_mirza", "irc_tv",
            "makki_al_hijazi", "othman_alkamees", "saleh_alfawzan", "saad_alkhathlan", "aziz_farhan",
            "dar_al_ifta", "islamweb", "askmuftitariqmasood", "themuslimlantern", "ch_ar_wicepw",
            "tariqmasoodofficial", "ibnawesome", "alresalahtoday", "muftitariqmasood", "ynw_eng",
            "ch_ar_pknyda", "takrirvideochannel", "therahmahfoundationwomen", "alslafi", "muftisalmanazhariofficial",
            "fatawabaly", "mufti_saeed_arshad_al_hussaini", "mohammedhijabarabic", "iplustvdeeniqa", "ftawamostafaaladwy",
            "iplustvikhtilafkahal", "alsheikhbinbaz", "ahnafmediaservices", "ch_ar_wuhe_q", "muftihassanattari",
            "ch_ar_nlml2q", "daruliftaahlesunnat", "user_fatwa_alnabulsi", "muftifazalhamdard", "muftiqasimattari",
            "muftiabdulraheem", "rahamtv", "muftirasheedofficial"
        )
    )
}
