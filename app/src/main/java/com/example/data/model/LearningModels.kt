package com.example.data.model

enum class LearningModuleType(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val iconName: String
) {
    NOORANI_QAIDA("qaida", "Noorani Qaida", "نورانی قاعدہ", "القاعدة النورانية", "नूरानी क़ाइदा", "MenuBook"),
    TAJWEED("tajweed", "Tajweed Rules", "قواعد التجوید", "أحكام التجويد", "तजवीद के नियम", "AutoAwesome"),
    QURAN_READING("reading", "Quran Reading", "مشقِ تلاوت", "تلاوة القرآن", "क़ुरआन पठन", "RecordVoiceOver"),
    QURANIC_ARABIC("vocabulary", "Quranic Words", "مفردات القرآن", "مفردات القرآن", "क़ुरआनी शब्दावली", "Translate"),
    TRANSLATION("translation", "Word-by-Word", "لفظی ترجمہ و فہم", "ترجمة القرآن", "शब्द-दर-शब्द अनुवाद", "LibraryBooks"),
    HIFZ("hifz", "Hifz & Memorization", "حفظ القرآن و تکرار", "حفظ القرآن", "हिफ़्ज़ और याद करना", "BookmarkAdded"),
    ARABIC_WRITING("writing", "Arabic Writing", "مشقِ کتابت", "تعليم الكتابة", "अरबी लेखन", "Draw"),
    PRONUNCIATION("pronunciation", "Audio & Makharij", "صوتیات و مخارج", "الصوتيات والمخارج", "उच्चारण और मख़ारिज", "VolumeUp"),
    UNDERSTANDING("understanding", "Tafseer & Asbab", "تدبر و اسبابِ نزول", "التدبر وأسباب النزول", "तफ़सीर और समझ", "Psychology"),
    ASSESSMENTS("assessments", "Tests & Quizzes", "امتحانات و ٹیسٹ", "الاختبارات والتقييم", "टेस्ट और क्विज़", "Quiz")
}

data class QaidaLesson(
    val id: String,
    val lessonNumber: Int,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionUr: String,
    val descriptionAr: String,
    val descriptionHi: String,
    val items: List<QaidaItem>
)

data class QaidaItem(
    val arabic: String,
    val transliteration: String,
    val noteEn: String = "",
    val noteUr: String = "",
    val noteAr: String = "",
    val noteHi: String = "",
    val audioExample: String = ""
)

data class QuranicWord(
    val id: String,
    val arabic: String,
    val transliteration: String,
    val meaningEn: String,
    val meaningUr: String,
    val meaningAr: String,
    val meaningHi: String,
    val frequencyInQuran: Int,
    val category: String, // Pronoun, Preposition, Noun, Verb
    val exampleAyah: String,
    val exampleSurahRef: String
)

data class WordPart(
    val arabic: String,
    val translit: String,
    val meaningEn: String,
    val meaningUr: String,
    val meaningAr: String,
    val meaningHi: String
)

data class WordByWordAyah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val fullArabic: String,
    val translationEn: String,
    val translationUr: String,
    val translationHi: String,
    val words: List<WordPart>
)

data class HifzTarget(
    val surahNumber: Int,
    val surahNameArabic: String,
    val surahNameEnglish: String,
    val totalAyahs: Int,
    val startAyah: Int = 1,
    val endAyah: Int = totalAyahs,
    val isMemorized: Boolean = false,
    val lastRevisedDate: String = "",
    val repetitionCount: Int = 0
)

data class SurahExegesis(
    val surahNumber: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val revelationPlace: String,
    val asbabNuzoolEn: String,
    val asbabNuzoolUr: String,
    val asbabNuzoolAr: String,
    val asbabNuzoolHi: String,
    val mainThemesEn: List<String>,
    val mainThemesUr: List<String>,
    val mainThemesAr: List<String>,
    val mainThemesHi: List<String>,
    val practicalLessonsEn: List<String>,
    val practicalLessonsUr: List<String>,
    val practicalLessonsAr: List<String>,
    val practicalLessonsHi: List<String>,
    val sourceAttribution: String
)

enum class StageToolType {
    NONE,
    QAIDA_TAJWEED_WRITING,
    QURAN_UNDERSTANDING,
    HIFZ_TRACKER,
    ASSESSMENTS_HUB
}

data class IslamicReference(
    val sourceName: String,
    val citation: String,
    val gradeOrNoteEn: String = "",
    val gradeOrNoteUr: String = "",
    val gradeOrNoteAr: String = "",
    val gradeOrNoteHi: String = ""
) {
    fun noteFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> gradeOrNoteUr
        AppLanguage.ARABIC -> gradeOrNoteAr
        AppLanguage.HINDI -> gradeOrNoteHi
        AppLanguage.ENGLISH -> gradeOrNoteEn
    }
}

data class PrayerStepRecitation(
    val labelEn: String,
    val labelUr: String,
    val labelAr: String,
    val labelHi: String,
    val arabic: String,
    val transliteration: String,
    val translationEn: String,
    val translationUr: String,
    val translationAr: String,
    val translationHi: String,
    val explanationEn: String,
    val explanationUr: String,
    val explanationAr: String,
    val explanationHi: String
) {
    fun labelFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> labelUr
        AppLanguage.ARABIC -> labelAr
        AppLanguage.HINDI -> labelHi
        AppLanguage.ENGLISH -> labelEn
    }

    fun translationFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> translationUr
        AppLanguage.ARABIC -> translationAr
        AppLanguage.HINDI -> translationHi
        AppLanguage.ENGLISH -> translationEn
    }

    fun explanationFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> explanationUr
        AppLanguage.ARABIC -> explanationAr
        AppLanguage.HINDI -> explanationHi
        AppLanguage.ENGLISH -> explanationEn
    }
}

data class AcademyLesson(
    val id: String,
    val stageNumber: Int,
    val lessonNumber: Int,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val summaryEn: String,
    val summaryUr: String,
    val summaryAr: String,
    val summaryHi: String,
    val bodyEn: String,
    val bodyUr: String,
    val bodyAr: String,
    val bodyHi: String,
    val examplesEn: List<String> = emptyList(),
    val examplesUr: List<String> = emptyList(),
    val examplesAr: List<String> = emptyList(),
    val examplesHi: List<String> = emptyList(),
    val practicalTaskEn: String = "",
    val practicalTaskUr: String = "",
    val practicalTaskAr: String = "",
    val practicalTaskHi: String = "",
    val fiqhDifferencesEn: String = "",
    val fiqhDifferencesUr: String = "",
    val fiqhDifferencesAr: String = "",
    val fiqhDifferencesHi: String = "",
    val recitations: List<PrayerStepRecitation> = emptyList(),
    val references: List<IslamicReference> = emptyList(),
    val quizId: String? = null
) {
    fun titleFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> titleUr
        AppLanguage.ARABIC -> titleAr
        AppLanguage.HINDI -> titleHi
        AppLanguage.ENGLISH -> titleEn
    }

    fun summaryFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> summaryUr
        AppLanguage.ARABIC -> summaryAr
        AppLanguage.HINDI -> summaryHi
        AppLanguage.ENGLISH -> summaryEn
    }

    fun bodyFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> bodyUr
        AppLanguage.ARABIC -> bodyAr
        AppLanguage.HINDI -> bodyHi
        AppLanguage.ENGLISH -> bodyEn
    }

    fun examplesFor(language: AppLanguage): List<String> = when (language) {
        AppLanguage.URDU -> examplesUr
        AppLanguage.ARABIC -> examplesAr
        AppLanguage.HINDI -> examplesHi
        AppLanguage.ENGLISH -> examplesEn
    }

    fun practicalTaskFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> practicalTaskUr
        AppLanguage.ARABIC -> practicalTaskAr
        AppLanguage.HINDI -> practicalTaskHi
        AppLanguage.ENGLISH -> practicalTaskEn
    }

    fun fiqhDifferencesFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> fiqhDifferencesUr
        AppLanguage.ARABIC -> fiqhDifferencesAr
        AppLanguage.HINDI -> fiqhDifferencesHi
        AppLanguage.ENGLISH -> fiqhDifferencesEn
    }
}

data class AcademyStage(
    val stageNumber: Int,
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val subtitleEn: String,
    val subtitleUr: String,
    val subtitleAr: String,
    val subtitleHi: String,
    val overviewEn: String,
    val overviewUr: String,
    val overviewAr: String,
    val overviewHi: String,
    val interactiveToolType: StageToolType = StageToolType.NONE,
    val lessons: List<AcademyLesson>
) {
    fun titleFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> titleUr
        AppLanguage.ARABIC -> titleAr
        AppLanguage.HINDI -> titleHi
        AppLanguage.ENGLISH -> titleEn
    }

    fun subtitleFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> subtitleUr
        AppLanguage.ARABIC -> subtitleAr
        AppLanguage.HINDI -> subtitleHi
        AppLanguage.ENGLISH -> subtitleEn
    }

    fun overviewFor(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> overviewUr
        AppLanguage.ARABIC -> overviewAr
        AppLanguage.HINDI -> overviewHi
        AppLanguage.ENGLISH -> overviewEn
    }
}

data class AcademyQuizQuestion(
    val id: String,
    val category: String,
    val questionEn: String,
    val questionUr: String,
    val questionAr: String,
    val questionHi: String,
    val arabicPrompt: String? = null,
    val optionsEn: List<String>,
    val optionsUr: List<String>,
    val optionsAr: List<String>,
    val optionsHi: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationUr: String,
    val explanationAr: String,
    val explanationHi: String,
    val stageNumber: Int = 7
)

data class StudentAcademyProgress(
    val completedLessonIds: Set<String> = emptySet(),
    val memorizedSurahNumbers: Set<Int> = setOf(1, 112, 113, 114),
    val learnedWordIds: Set<String> = emptySet(),
    val completedQuizIds: Set<String> = emptySet(),
    val hifzRevisionCounts: Map<Int, Int> = emptyMap(),
    val totalQuizzesPassed: Int = 0,
    val dailyTargetAyahs: Int = 3,
    val todayCompletedAyahs: Int = 0,
    val currentStreakDays: Int = 1,
    val lastActiveModuleId: String = "stage_1",
    val lastActiveLessonTitle: String = "Stage 1 · What Is Islam?",
    val lastActiveStageNumber: Int = 1,
    val lastActiveLessonId: String = "s1_l1",
    val hifzRevisionReminderEnabled: Boolean = true
)
