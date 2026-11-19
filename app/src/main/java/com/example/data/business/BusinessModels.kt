package com.example.data.business

import com.example.data.model.AppLanguage

/**
 * Mandatory evidence category taxonomy (PART 1.D).
 * Never conflates revealed scripture, classical jurisprudence, institutional standards,
 * state regulations, and practical educational commentary.
 */
enum class EvidenceCategory(val id: String) {
    DIRECT_QURAN("direct_quran"),
    AUTHENTIC_HADITH("authentic_hadith"),
    CLASSICAL_FIQH("classical_fiqh"),
    CONTEMPORARY_RESOLUTION("contemporary_resolution"),
    REGULATORY_LEGAL("regulatory_legal"),
    EDUCATIONAL_BUSINESS("educational_business"),
    SCHOLARLY_REVIEW_REQUIRED("scholarly_review_required");

    fun label(language: AppLanguage): String = when (this) {
        DIRECT_QURAN -> when (language) {
            AppLanguage.URDU -> "1. صریح قرآنی نص"
            AppLanguage.ARABIC -> "١. نص قرآني صريح"
            AppLanguage.HINDI -> "1. प्रत्यक्ष क़ुरआनी आयत"
            AppLanguage.ENGLISH -> "1. Direct Quranic Text"
        }
        AUTHENTIC_HADITH -> when (language) {
            AppLanguage.URDU -> "2. مستند حدیثِ نبوی ﷺ"
            AppLanguage.ARABIC -> "٢. حديث نبوي صحيح"
            AppLanguage.HINDI -> "2. प्रामाणिक हदीस"
            AppLanguage.ENGLISH -> "2. Authentic Hadith"
        }
        CLASSICAL_FIQH -> when (language) {
            AppLanguage.URDU -> "3. ثابت شدہ کلاسیکی فقہی حکم"
            AppLanguage.ARABIC -> "٣. حكم فقهي معتمد"
            AppLanguage.HINDI -> "3. स्थापित शास्त्रीय फ़िक़्ह नियम"
            AppLanguage.ENGLISH -> "3. Established Classical Fiqh Ruling"
        }
        CONTEMPORARY_RESOLUTION -> when (language) {
            AppLanguage.URDU -> "4. معاصر شرعی قرارداد (AAOIFI / مجمع الفقہ)"
            AppLanguage.ARABIC -> "٤. قرار فقهي معاصر (أيوفي / المجمع)"
            AppLanguage.HINDI -> "4. समकालीन शरीअत प्रस्ताव (AAOIFI / IIFA)"
            AppLanguage.ENGLISH -> "4. Contemporary Scholarly Resolution"
        }
        REGULATORY_LEGAL -> when (language) {
            AppLanguage.URDU -> "5. قانونی و ضابطہ جاتی تقاضا (SBP / SECP)"
            AppLanguage.ARABIC -> "٥. متطلب تنظيمي وقانوني"
            AppLanguage.HINDI -> "5. नियामक एवं कानूनी आवश्यकता"
            AppLanguage.ENGLISH -> "5. Regulatory or Legal Requirement"
        }
        EDUCATIONAL_BUSINESS -> when (language) {
            AppLanguage.URDU -> "6. کاروباری و تعلیمی وضاحت"
            AppLanguage.ARABIC -> "٦. شرح إداري وتجاري تعليمي"
            AppLanguage.HINDI -> "6. व्यावसायिक शैक्षिक व्याख्या"
            AppLanguage.ENGLISH -> "6. Educational Business Explanation"
        }
        SCHOLARLY_REVIEW_REQUIRED -> when (language) {
            AppLanguage.URDU -> "7. مستند مفتی سے رجوع طلب معاملہ"
            AppLanguage.ARABIC -> "٧. مسألة تتطلب مراجعة عالم أو مفتٍ"
            AppLanguage.HINDI -> "7. प्रमाणित मुफ़्ती से परामर्श आवश्यक"
            AppLanguage.ENGLISH -> "7. Issue Requiring Further Scholarly Review"
        }
    }

    companion object {
        fun fromId(id: String?): EvidenceCategory =
            entries.firstOrNull { it.id == id } ?: EDUCATIONAL_BUSINESS
    }
}

data class LocalizedText4(
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

data class LocalizedList4(
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

data class BusinessQuranCitation(
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val arabicText: String,
    val urduTranslation: String,
    val englishTranslation: String,
    val hindiTranslation: String,
    val contextNote: LocalizedText4,
    val businessRelevance: LocalizedText4,
    val sourceUrl: String = "https://quran.com/$surahNumber/$startAyah"
)

data class BusinessHadithCitation(
    val id: String,
    val collection: String,
    val hadithNumber: String,
    val chapter: String,
    val arabicText: String,
    val urduTranslation: String,
    val englishTranslation: String,
    val hindiTranslation: String,
    val narrator: String,
    val grading: String,
    val gradingSourceAndNotes: LocalizedText4,
    val sourceUrl: String
)

data class BusinessSourceRef(
    val title: String,
    val category: EvidenceCategory,
    val attribution: String,
    val url: String
)

data class BusinessMcq(
    val id: String,
    val question: LocalizedText4,
    val options: LocalizedList4,
    val correctIndex: Int,
    val explanation: LocalizedText4
)

data class HalalVsProhibitedRow(
    val halalPractice: LocalizedText4,
    val prohibitedPractice: LocalizedText4
)

/**
 * 26-part structured Islamic Business lesson (PART 4).
 */
data class BusinessLesson(
    val id: String,
    val level: Int, // 1, 2, or 4 (Level 3 is Sectors)
    val moduleNumber: Int,
    val moduleTitle: LocalizedText4,
    val title: LocalizedText4,
    val primaryEvidenceCategory: EvidenceCategory,
    val learningObjectives: LocalizedList4,
    val introduction: LocalizedText4,
    val explanationUr: String,
    val explanationEn: String,
    val explanationAr: String,
    val explanationHi: String,
    val quranRefSpecs: List<BusinessQuranRefSpec>,
    val hadithIds: List<String>,
    val classicalFiqh: LocalizedText4,
    val contemporaryGuidance: LocalizedText4,
    val madhhabDifferences: LocalizedText4,
    val modernExample: LocalizedText4,
    val halalVsProhibited: List<HalalVsProhibitedRow>,
    val commonMistakes: LocalizedList4,
    val complianceChecklist: LocalizedList4,
    val summary: LocalizedText4,
    val mcqs: List<BusinessMcq>,
    val practicalAssignment: LocalizedText4,
    val sources: List<BusinessSourceRef>
) {
    fun explanationFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> explanationUr.ifBlank { explanationEn }
        AppLanguage.ENGLISH -> explanationEn
        AppLanguage.ARABIC -> explanationAr.ifBlank { explanationEn }
        AppLanguage.HINDI -> explanationHi.ifBlank { explanationEn }
    }
}

data class BusinessQuranRefSpec(
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val contextNote: LocalizedText4,
    val businessRelevance: LocalizedText4
)

/**
 * Module 10 — Individual deep breakdown for each of the 14 Islamic Financial Contracts.
 */
data class BusinessContractGuide(
    val id: String,
    val order: Int,
    val arabicTerm: String,
    val name: LocalizedText4,
    val definition: LocalizedText4,
    val purpose: LocalizedText4,
    val shariahPrinciples: LocalizedText4,
    val conditions: LocalizedList4,
    val partyRights: LocalizedText4,
    val ownershipAndPossession: LocalizedText4,
    val profitAndLossRules: LocalizedText4,
    val commonInvalidStructures: LocalizedList4,
    val modernExamples: LocalizedText4,
    val aaoifiStandard: String,
    val sbpGuidance: LocalizedText4,
    val madhhabDifferences: LocalizedText4,
    val muftiConsultationQuestions: LocalizedList4,
    val quranRefSpecs: List<BusinessQuranRefSpec>,
    val hadithIds: List<String>,
    val mcqs: List<BusinessMcq>
)

/**
 * Level 3 — All 30 Practical Halal Business Sectors with sections A through R.
 */
data class BusinessSectorGuide(
    val id: String,
    val sectorNumber: Int, // 1..30
    val name: LocalizedText4,
    val nonBlanketAssessmentNote: LocalizedText4,
    val introductionA: LocalizedText4,
    val howItOperatesB: LocalizedText4,
    val startupRequirementsC: LocalizedText4,
    val capitalAndInvestmentD: LocalizedText4,
    val supplierSelectionE: LocalizedText4,
    val purchasingProcurementF: LocalizedText4,
    val ownershipPossessionG: LocalizedText4,
    val productsServicesH: LocalizedText4,
    val pricingProfitI: LocalizedText4,
    val customerRightsJ: LocalizedText4,
    val employeeRightsK: LocalizedText4,
    val relevantContractsL: LocalizedList4,
    val potentialProhibitedM: LocalizedList4,
    val halalChecklistN: LocalizedList4,
    val realisticCaseStudyO: LocalizedText4,
    val commonMistakesP: LocalizedList4,
    val practicalAssignmentQ: LocalizedText4,
    val referencesR: List<BusinessSourceRef>,
    val quranRefSpecs: List<BusinessQuranRefSpec>,
    val hadithIds: List<String>,
    val mcqs: List<BusinessMcq>
)

/**
 * Part 3 — 23 Additional Specialized Commercial & Contemporary Fiqh Topics.
 */
data class SpecializedTopicGuide(
    val id: String,
    val order: Int,
    val title: LocalizedText4,
    val evidenceCategory: EvidenceCategory,
    val isDisputedContemporaryIssue: Boolean,
    val overview: LocalizedText4,
    val quranAndHadithBasis: LocalizedText4,
    val classicalAndContemporaryPositions: LocalizedText4,
    val practicalGuidance: LocalizedText4,
    val scholarlyReviewTriggers: LocalizedText4,
    val quranRefSpecs: List<BusinessQuranRefSpec>,
    val hadithIds: List<String>,
    val sources: List<BusinessSourceRef>,
    val mcqs: List<BusinessMcq>
)

sealed interface BusinessAcademyUiState {
    object Loading : BusinessAcademyUiState
    data class Ready(
        val lessons: List<BusinessLesson>,
        val contracts: List<BusinessContractGuide>,
        val sectors: List<BusinessSectorGuide>,
        val specializedTopics: List<SpecializedTopicGuide>,
        val hadithCatalog: Map<String, BusinessHadithCitation>,
        val officialSources: List<BusinessSourceRef>
    ) : BusinessAcademyUiState
    data class Error(val message: String) : BusinessAcademyUiState
}
