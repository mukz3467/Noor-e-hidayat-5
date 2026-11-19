package com.example.data.shifa

import com.example.data.model.AppLanguage

enum class ShifaCategory(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val subtitleEn: String,
    val subtitleUr: String,
    val subtitleAr: String,
    val subtitleHi: String,
    val testTag: String
) {
    QURANIC_SHIFA(
        id = "quranic_shifa",
        titleEn = "Quranic Shifa",
        titleUr = "شفائے قرآن",
        titleAr = "الشفاء في القرآن",
        titleHi = "क़ुरआनी शिफ़ा",
        subtitleEn = "Verses of healing, mercy, patience, tranquility & Allah's protection",
        subtitleUr = "شفا، رحمت، صبر، توکل، اطمینانِ قلب اور حفاظت کی قرآنی آیات",
        subtitleAr = "آيات الشفاء والرحمة والصبر والسكينة والحفظ",
        subtitleHi = "शिफ़ा, रहमत, सब्र, दिल के सुकून और हिफ़ाज़त की आयतें",
        testTag = "shifa_tab_quranic"
    ),
    RUQYAH_SHARIYYAH(
        id = "ruqyah_shariyyah",
        titleEn = "Ruqyah Shar'iyyah",
        titleUr = "رقیہ شرعیہ",
        titleAr = "الرقية الشرعية",
        titleHi = "रुक़या शरइय्या",
        subtitleEn = "Authentic Quranic Surahs, Masnoon Duas & Prophetic Ruqyah etiquette",
        subtitleUr = "مسنون قرآنی و نبوی دم، خود پر اور دوسرے پر دم کا مستند طریقہ",
        subtitleAr = "آيات الرقية والأدعية النبوية الصحيحة وآدابها",
        subtitleHi = "सहीह क़ुरआनी व मसनून दुआएं तथा रुक़या का प्रामाणिक तरीक़ा",
        testTag = "shifa_tab_ruqyah"
    ),
    TIB_E_NABAWI(
        id = "tib_e_nabawi",
        titleEn = "Tib-e-Nabawi",
        titleUr = "طبِ نبوی",
        titleAr = "الطب النبوي",
        titleHi = "तिब्ब-ए-नबवी",
        subtitleEn = "Prophetic remedies strictly verified in Sahih Bukhari & Muslim",
        subtitleUr = "صحیح بخاری و مسلم سے ثابت نبوی علاج مع مستند سیاق و سباق",
        subtitleAr = "الأدوية والهدي النبوي الثابت في الصحيحين",
        subtitleHi = "सहीह बुख़ारी व मुस्लिम से प्रमाणित नबवी उपचार",
        testTag = "shifa_tab_tib"
    ),
    SIHR_PROTECTION(
        id = "sihr_protection",
        titleEn = "Sihr & Guidance",
        titleUr = "سحر / کالا علم",
        titleAr = "السحر والوقاية منه",
        titleHi = "सिह्र और सुरक्षा",
        subtitleEn = "Quranic truth about Sihr, seeking refuge in Allah & Sunnah Ruqyah",
        subtitleUr = "سحر کے بارے میں قرآنی حقیقت، اللہ کی پناہ اور مسنون رقیہ",
        subtitleAr = "حقيقة السحر في القرآن والاستعاذة بالله والرقية المشروعة",
        subtitleHi = "सिह्र (जादू) और सुरक्षा — अल्लाह की पनाह व मसनून रुक़या",
        testTag = "shifa_tab_sihr"
    ),
    NAZAR_HASAD(
        id = "nazar_hasad",
        titleEn = "Nazar & Hasad",
        titleUr = "نظر اور حسد",
        titleAr = "العين والحسد",
        titleHi = "नज़र और हसद",
        subtitleEn = "Quranic guidance, Sahih Ahadith & Sunnah protection from evil eye",
        subtitleUr = "نظرِ بد اور حسد سے بچاؤ کی قرآنی آیات اور صحیح احادیث",
        subtitleAr = "الوقاية من العين والحسد بالقرآن والسنة الصحيحة",
        subtitleHi = "नज़र-ए-बद और ईर्ष्या से बचाव के क़ुरआनी व हदीस प्रमाण",
        testTag = "shifa_tab_nazar"
    ),
    WASWASE(
        id = "waswase",
        titleEn = "Waswase",
        titleUr = "وسوسے",
        titleAr = "الوسوسة وعلاجها",
        titleHi = "वसवसे",
        subtitleEn = "Dealing with Shaytani whispers & worship doubts with Prophetic compassion",
        subtitleUr = "شیطانی وسوسوں اور عبادت کے شک سے نجات کی مسنون رہنمائی",
        subtitleAr = "دفع الوساوس في الإيمان والعبادة بالهدي النبوي",
        subtitleHi = "शैतानी वसवसों और इबादत के संदेह से बचाव का मसनून मार्ग",
        testTag = "shifa_tab_waswase"
    ),
    SHIFA_E_QALB(
        id = "shifa_e_qalb",
        titleEn = "Heart & Sukoon",
        titleUr = "شفائے قلب و سکون",
        titleAr = "شفاء القلب والسكينة",
        titleHi = "दिल की शिफ़ा व सुकून",
        subtitleEn = "Comfort in grief, anxiety & fear through Sabr, Tawakkul & Zikr",
        subtitleUr = "غم، خوف، پریشانی میں صبر، توکل، امیدِ رحمت اور اطمینانِ قلب",
        subtitleAr = "السكينة في الحزن والخوف بالصبر والتوكل والذكر",
        subtitleHi = "ग़म, डर व चिंता में सब्र, तवक्कुल और दिल का सुकून",
        testTag = "shifa_tab_qalb"
    ),
    TREATMENT_INDEX(
        id = "treatment_index",
        titleEn = "Treatment Index",
        titleUr = "سرچ انڈیکس",
        titleAr = "فهرس الاستشفاء",
        titleHi = "खोज सूचकांक",
        subtitleEn = "Search conditions strictly against verified Quran & Sahih Hadith sources",
        subtitleUr = "مختلف کیفیات کے لیے مستند قرآنی و حدیثی رہنمائی کی تلاش",
        subtitleAr = "البحث عن الحالات في المصادر القرآنية والنبوية الموثقة فقط",
        subtitleHi = "प्रमाणित क़ुरआनी व सहीह हदीस स्रोतों में विषय खोजें",
        testTag = "shifa_tab_index"
    );

    fun title(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> titleUr
        AppLanguage.ARABIC -> titleAr
        AppLanguage.HINDI -> titleHi
        AppLanguage.ENGLISH -> titleEn
    }

    fun subtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> subtitleUr
        AppLanguage.ARABIC -> subtitleAr
        AppLanguage.HINDI -> subtitleHi
        AppLanguage.ENGLISH -> subtitleEn
    }
}

enum class ShifaItemType {
    QURAN_AYAH,
    HADITH_NARRATION,
    SUNNAH_DUA,
    PROPHETIC_REMEDY,
    GUIDANCE_NOTE
}

data class ShifaReferenceSource(
    val label: String,
    val bookAndNumber: String,
    val authenticityGrade: String,
    val url: String
)

data class LocalizedText4(
    val en: String,
    val ur: String,
    val ar: String = "",
    val hi: String
) {
    fun get(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> en
        AppLanguage.URDU -> ur
        AppLanguage.ARABIC -> ar.ifBlank { en }
        AppLanguage.HINDI -> hi
    }
}

data class ShifaRuqyahItem(
    val id: String,
    val category: ShifaCategory,
    val type: ShifaItemType,
    val topicTag: LocalizedText4,
    val title: LocalizedText4,
    // For Quran passages resolved dynamically from Tanzil/QuranData assets (never fabricated)
    val surahNumber: Int? = null,
    val ayahStart: Int? = null,
    val ayahEnd: Int? = null,
    // For Hadith / Masnoon Dua items (verified verbatim texts)
    val arabicText: String = "",
    val translation: LocalizedText4? = null,
    val narrator: String = "",
    // Islamic context & scholarly separation (distinct from original Quran/Hadith text)
    val islamicContext: LocalizedText4,
    // Tib-e-Nabawi separation: Prophet's ﷺ explicit established practice vs Later Scholars' explanation
    val propheticAction: LocalizedText4? = null,
    val scholarlyExplanation: LocalizedText4? = null,
    // Ruqyah recitation method & count (countShown ONLY when explicitly verified in Hadith text)
    val recitationMethod: LocalizedText4? = null,
    val establishedCountNote: LocalizedText4? = null,
    // Source verification metadata
    val primaryReference: ShifaReferenceSource,
    val additionalReferences: List<ShifaReferenceSource> = emptyList(),
    // Index / search keywords (English, Roman Urdu, Urdu, Hindi, Arabic)
    val searchKeywords: List<String> = emptyList(),
    val supportsQuranAudio: Boolean = false
)

data class TreatmentConditionTopic(
    val id: String,
    val title: LocalizedText4,
    val matchedItemIds: List<String>,
    val scopeNote: LocalizedText4,
    val searchAliases: List<String>
)

data class ResolvedShifaEntry(
    val item: ShifaRuqyahItem,
    val arabicText: String,
    val surahLabel: String?,
    val translationText: String,
    val translationAttribution: String,
    val offlineTafseerText: String?,
    val offlineTafseerAttribution: String?,
    val onlineTafseerText: String? = null,
    val onlineTafseerAttribution: String? = null,
    val isLoadingOnlineTafseer: Boolean = false,
    val onlineTafseerError: String? = null
)
