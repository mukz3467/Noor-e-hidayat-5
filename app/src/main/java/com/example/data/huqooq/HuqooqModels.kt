package com.example.data.huqooq

import com.example.data.model.AppLanguage

enum class HuqooqDepartment(val id: String) {
    RIGHTS_RESPONSIBILITIES("dept_rights"),
    ILM_UL_FARAID("dept_faraid"),
    ISLAH_MUASHRA("dept_islah"),
    AKHLAQ_ADAAB("dept_akhlaq");

    fun title(language: AppLanguage): String = when (this) {
        RIGHTS_RESPONSIBILITIES -> when (language) {
            AppLanguage.URDU -> "1. حقوق اور ذمہ داریاں"
            AppLanguage.ARABIC -> "١. الحقوق والمسؤوليات"
            AppLanguage.HINDI -> "1. हुक़ूक़ और ज़िम्मेदारियाँ"
            AppLanguage.ENGLISH -> "1. Rights & Responsibilities"
        }
        ILM_UL_FARAID -> when (language) {
            AppLanguage.URDU -> "2. علم الفرائض (وراثت)"
            AppLanguage.ARABIC -> "٢. علم الفرائض والمواريث"
            AppLanguage.HINDI -> "2. इल्म-उल-फ़राइज़ (विरासत)"
            AppLanguage.ENGLISH -> "2. Ilm-ul-Faraid (Inheritance)"
        }
        ISLAH_MUASHRA -> when (language) {
            AppLanguage.URDU -> "3. اسلامی اصلاحِ معاشرہ"
            AppLanguage.ARABIC -> "٣. الإصلاح المجتمعي الإسلامي"
            AppLanguage.HINDI -> "3. इस्लामी समाज सुधार"
            AppLanguage.ENGLISH -> "3. Social Reform (Islah-e-Muashra)"
        }
        AKHLAQ_ADAAB -> when (language) {
            AppLanguage.URDU -> "4. اخلاق اور اسلامی آداب"
            AppLanguage.ARABIC -> "٤. الأخلاق والآداب الإسلامية"
            AppLanguage.HINDI -> "4. अख़लाक़ और इस्लामी आदाब"
            AppLanguage.ENGLISH -> "4. Akhlaq & Islamic Manners"
        }
    }

    fun subtitle(language: AppLanguage): String = when (this) {
        RIGHTS_RESPONSIBILITIES -> when (language) {
            AppLanguage.URDU -> "خاندان، کمزور طبقات، معیشت اور انسانی وقار کے 30 ابواب"
            AppLanguage.ARABIC -> "٣٠ باباً في حقوق الأسرة والمجتمع والفئات الضعيفة والبيئة"
            AppLanguage.HINDI -> "परिवार, कमज़ोर वर्ग, समाज और मानव गरिमा के 30 अध्याय"
            AppLanguage.ENGLISH -> "30 Categories • Family, Vulnerable Groups, Society & Environment"
        }
        ILM_UL_FARAID -> when (language) {
            AppLanguage.URDU -> "26 مکمل اسباق + آف لائن اسلامی وراثت کیلکولیٹر"
            AppLanguage.ARABIC -> "٢٦ درساً منهجياً + حاسبة المواريث الشرعية"
            AppLanguage.HINDI -> "26 विस्तृत पाठ + ऑफ़लाइन इस्लामी विरासत कैलकुलेटर"
            AppLanguage.ENGLISH -> "26 Structured Lessons + Offline Inheritance Calculator"
        }
        ISLAH_MUASHRA -> when (language) {
            AppLanguage.URDU -> "معاشرتی برائیاں، حل اور تحفظ (7 گروپس، 65 موضوعات)"
            AppLanguage.ARABIC -> "٧ محاور و٦٥ موضوعاً في معالجة المشكلات الأسرية والمجتمعية"
            AppLanguage.HINDI -> "7 समूह, 65 विषय • सामाजिक बुराइयों का इस्लामी समाधान"
            AppLanguage.ENGLISH -> "7 Topic Groups, 65 Lessons • Roots, Solutions & Victim-Safe Help"
        }
        AKHLAQ_ADAAB -> when (language) {
            AppLanguage.URDU -> "سچائی، حیا، صبر، عدل اور روزمرہ آداب کے 25 اسباق"
            AppLanguage.ARABIC -> "٢٥ درساً في تزكية الأخلاق والآداب اليومية والرقمية"
            AppLanguage.HINDI -> "ईमानदारी, शालीनता, धैर्य और दैनिक आदाब के 25 पाठ"
            AppLanguage.ENGLISH -> "25 Character-Building & Daily Manners Lessons"
        }
    }

    companion object {
        fun fromId(id: String?): HuqooqDepartment =
            entries.firstOrNull { it.id == id } ?: RIGHTS_RESPONSIBILITIES
    }
}

enum class HuqooqEvidenceType(val id: String) {
    QURAN("quran"),
    AUTHENTIC_HADITH("authentic_hadith"),
    SCHOLARLY_CONSENSUS("scholarly_consensus"),
    MADHHAB_DIFFERENCE("madhhab_difference"),
    NATIONAL_LAW_DISTINCTION("national_law"),
    SAFETY_PROFESSIONAL_HELP("safety_help"),
    MUFTI_REVIEW_REQUIRED("mufti_review");

    fun label(language: AppLanguage): String = when (this) {
        QURAN -> when (language) {
            AppLanguage.URDU -> "قرآنی نص"
            AppLanguage.ARABIC -> "نص قرآني صريح"
            AppLanguage.HINDI -> "क़ुरआनी आयत"
            AppLanguage.ENGLISH -> "Quranic Text"
        }
        AUTHENTIC_HADITH -> when (language) {
            AppLanguage.URDU -> "صحیح / مستند حدیث"
            AppLanguage.ARABIC -> "حديث نبوي ثابت"
            AppLanguage.HINDI -> "प्रामाणिक हदीस"
            AppLanguage.ENGLISH -> "Authentic Hadith"
        }
        SCHOLARLY_CONSENSUS -> when (language) {
            AppLanguage.URDU -> "متفقہ فقہی رہنمائی"
            AppLanguage.ARABIC -> "توجيه فقهي معتمد"
            AppLanguage.HINDI -> "मान्य फ़िक़्ह मार्गदर्शन"
            AppLanguage.ENGLISH -> "Established Fiqh Guidance"
        }
        MADHHAB_DIFFERENCE -> when (language) {
            AppLanguage.URDU -> "فقہی اختلافِ رائے"
            AppLanguage.ARABIC -> "خلاف فقهي معتبر"
            AppLanguage.HINDI -> "फ़िक़्ह मतभेद नोट"
            AppLanguage.ENGLISH -> "Scholarly Difference Noted"
        }
        NATIONAL_LAW_DISTINCTION -> when (language) {
            AppLanguage.URDU -> "شرعی حکم بمقابلہ ملکی قانون"
            AppLanguage.ARABIC -> "التمييز بين الشرع والقانون الوطني"
            AppLanguage.HINDI -> "शरीअत बनाम स्थानीय कानून"
            AppLanguage.ENGLISH -> "Islamic Law vs National Law Note"
        }
        SAFETY_PROFESSIONAL_HELP -> when (language) {
            AppLanguage.URDU -> "تحفظ، قانونی و ہنگامی امداد"
            AppLanguage.ARABIC -> "الأمان والدعم القانوني والطبي"
            AppLanguage.HINDI -> "सुरक्षा, कानूनी व आपात सहायता"
            AppLanguage.ENGLISH -> "Safety, Legal & Professional Support"
        }
        MUFTI_REVIEW_REQUIRED -> when (language) {
            AppLanguage.URDU -> "مستند مفتی سے رجوع لازم"
            AppLanguage.ARABIC -> "يتطلب استفتاء عالم مختص"
            AppLanguage.HINDI -> "योग्य मुफ़्ती समीक्षा आवश्यक"
            AppLanguage.ENGLISH -> "Qualified Scholar Review Required"
        }
    }
}

data class HuqooqLocalized4(
    val en: String,
    val ur: String,
    val ar: String = en,
    val hi: String = en
) {
    fun get(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> en
        AppLanguage.URDU -> ur.ifBlank { en }
        AppLanguage.ARABIC -> ar.ifBlank { en }
        AppLanguage.HINDI -> hi.ifBlank { en }
    }
}

data class HuqooqLocalizedList4(
    val en: List<String>,
    val ur: List<String>,
    val ar: List<String> = en,
    val hi: List<String> = en
) {
    fun get(language: AppLanguage): List<String> = when (language) {
        AppLanguage.ENGLISH -> en
        AppLanguage.URDU -> ur.ifEmpty { en }
        AppLanguage.ARABIC -> ar.ifEmpty { en }
        AppLanguage.HINDI -> hi.ifEmpty { en }
    }
}

data class HuqooqQuranRefSpec(
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val contextExplanation: HuqooqLocalized4
)

data class HuqooqQuranCitation(
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val arabicText: String,
    val urduTranslation: String,
    val englishTranslation: String,
    val hindiTranslation: String,
    val contextExplanation: HuqooqLocalized4,
    val sourceUrl: String
)

data class HuqooqHadithCitation(
    val id: String,
    val collection: String,
    val hadithNumber: String,
    val bookChapter: String,
    val arabicText: String,
    val englishTranslation: String,
    val urduTranslation: String,
    val hindiTranslation: String,
    val narrator: String,
    val grading: String,
    val sourceNote: HuqooqLocalized4,
    val sourceUrl: String
)

data class HuqooqSourceLink(
    val title: String,
    val attribution: String,
    val url: String
)

data class HuqooqMcq(
    val id: String,
    val question: HuqooqLocalized4,
    val options: HuqooqLocalizedList4,
    val correctIndex: Int,
    val explanation: HuqooqLocalized4
)

/**
 * Unified rich lesson model covering all 4 departments:
 * - Dept 1 (Rights 1..30): includes dutyBearer, mutualBoundaries, rightsViolatedAction
 * - Dept 2 (Faraid 1..26): includes scholarlyNote, practicalExamples
 * - Dept 3 (Islah Groups A..G, 1..65): includes rootCauses, individualRole, familySocietyRole, safetyAndSupportNote
 * - Dept 4 (Akhlaq & Adaab 1..25): includes dailyExercises, practicalExamples
 */
data class HuqooqLesson(
    val id: String,
    val department: HuqooqDepartment,
    val order: Int,
    val groupCode: String, // e.g. "FAMILY", "VULNERABLE", "GROUP_A".."GROUP_G", "FARAID", "AKHLAQ"
    val groupLabel: HuqooqLocalized4,
    val title: HuqooqLocalized4,
    val introduction: HuqooqLocalized4,
    val explanation: HuqooqLocalized4,
    // Dept 1 specific fields (optional / empty for others)
    val dutyBearer: HuqooqLocalized4? = null,
    val mutualBoundaries: HuqooqLocalized4? = null,
    val rightsViolatedAction: HuqooqLocalized4? = null,
    // Dept 3 specific fields
    val rootCauses: HuqooqLocalizedList4? = null,
    val individualSolution: HuqooqLocalizedList4? = null,
    val familySocietySolution: HuqooqLocalizedList4? = null,
    val safetyAndLegalSupport: HuqooqLocalized4? = null,
    // Dept 4 specific
    val dailyExercise: HuqooqLocalized4? = null,
    // Shared rich pedagogical blocks
    val practicalWays: HuqooqLocalizedList4,
    val realLifeExamples: HuqooqLocalizedList4,
    val commonMistakes: HuqooqLocalizedList4,
    val actionSteps: HuqooqLocalizedList4,
    val scholarlyNuance: HuqooqLocalized4? = null,
    val isDisputedOrComplex: Boolean = false,
    val evidenceBadge: HuqooqEvidenceType = HuqooqEvidenceType.QURAN,
    val quranRefSpecs: List<HuqooqQuranRefSpec>,
    val hadithIds: List<String>,
    val sourceLinks: List<HuqooqSourceLink>,
    val mcqs: List<HuqooqMcq>
)

data class HuqooqSearchHit(
    val id: String,
    val department: HuqooqDepartment,
    val title: HuqooqLocalized4,
    val subtitle: HuqooqLocalized4
)

sealed interface HuqooqAcademyUiState {
    object Loading : HuqooqAcademyUiState
    data class Ready(
        val lessons: List<HuqooqLesson>,
        val hadithCatalog: Map<String, HuqooqHadithCitation>,
        val officialSources: List<HuqooqSourceLink>
    ) : HuqooqAcademyUiState
    data class Error(val message: String) : HuqooqAcademyUiState
}
