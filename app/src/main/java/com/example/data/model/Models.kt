package com.example.data.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val isRtl: Boolean
) {
    ENGLISH("en", "English", "English", false),
    URDU("ur", "Urdu", "اردو", true),
    ARABIC("ar", "Arabic", "العربية", true),
    HINDI("hi", "Hindi", "हिन्दी", false)
}

enum class AppTheme(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val isDarkDefault: Boolean,
    val swatchPrimaryHex: Long,
    val swatchSecondaryHex: Long,
    val swatchAccentHex: Long,
    val swatchBackgroundHex: Long
) {
    EMERALD_GOLD(
        id = "emerald_gold",
        titleEn = "Emerald & Gold",
        titleUr = "زمردی و سنہری",
        titleAr = "الزمرد والذهب",
        titleHi = "पन्ना और स्वर्ण",
        isDarkDefault = false,
        swatchPrimaryHex = 0xFF064E3B,
        swatchSecondaryHex = 0xFF0F766E,
        swatchAccentHex = 0xFFD4AF37,
        swatchBackgroundHex = 0xFFF8F5EA
    ),
    WARM_CREAM(
        id = "warm_cream",
        titleEn = "Warm Cream",
        titleUr = "کریم پرسکون",
        titleAr = "البيج الدافئ",
        titleHi = "हल्का क्रीम",
        isDarkDefault = false,
        swatchPrimaryHex = 0xFF6E5034,
        swatchSecondaryHex = 0xFF9B7952,
        swatchAccentHex = 0xFF9B7952,
        swatchBackgroundHex = 0xFFF7F1E5
    ),
    MIDNIGHT_BLUE(
        id = "midnight_blue",
        titleEn = "Midnight Blue",
        titleUr = "شبِ نیلگوں",
        titleAr = "الأزرق الليلي",
        titleHi = "मिडनाइट ब्लू",
        isDarkDefault = true,
        swatchPrimaryHex = 0xFF1E3A5F,
        swatchSecondaryHex = 0xFF2C5282,
        swatchAccentHex = 0xFFC6A45C,
        swatchBackgroundHex = 0xFF101C35
    ),
    SAGE_GREEN(
        id = "sage_green",
        titleEn = "Sage Green",
        titleUr = "سیج سبز",
        titleAr = "الأخضر المريمي",
        titleHi = "सेज हरा",
        isDarkDefault = false,
        swatchPrimaryHex = 0xFF344E41,
        swatchSecondaryHex = 0xFF6B8F71,
        swatchAccentHex = 0xFFC9D8C5,
        swatchBackgroundHex = 0xFFF3F5EE
    ),
    OBSIDIAN_DARK(
        id = "obsidian_dark",
        titleEn = "Obsidian Dark",
        titleUr = "آبسیڈین ڈارک (رات)",
        titleAr = "الداكن الفاخر",
        titleHi = "ऑब्सिडियन डार्क",
        isDarkDefault = true,
        swatchPrimaryHex = 0xFFB99A55,
        swatchSecondaryHex = 0xFF7D6538,
        swatchAccentHex = 0xFFB99A55,
        swatchBackgroundHex = 0xFF121212
    ),
    ROSE_SAND(
        id = "rose_sand",
        titleEn = "Rose Sand",
        titleUr = "گلابی ریت",
        titleAr = "الرملي الوردي",
        titleHi = "रोज़ सैंड",
        isDarkDefault = false,
        swatchPrimaryHex = 0xFF8B6570,
        swatchSecondaryHex = 0xFFC9A6A5,
        swatchAccentHex = 0xFF8B6570,
        swatchBackgroundHex = 0xFFF1E5E1
    ),
    ROYAL_TEAL(
        id = "royal_teal",
        titleEn = "Royal Teal",
        titleUr = "شاہی فیروزی",
        titleAr = "الفيروزي الملكي",
        titleHi = "रॉयल टील",
        isDarkDefault = false,
        swatchPrimaryHex = 0xFF064B50,
        swatchSecondaryHex = 0xFF087E83,
        swatchAccentHex = 0xFFD6B66B,
        swatchBackgroundHex = 0xFFF1F6F3
    );

    fun localizedTitle(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> titleUr
        AppLanguage.ARABIC -> titleAr
        AppLanguage.HINDI -> titleHi
        AppLanguage.ENGLISH -> titleEn
    }

    companion object {
        fun fromId(rawId: String?): AppTheme {
            return when (rawId?.lowercase()) {
                "warm_cream" -> WARM_CREAM
                "midnight_blue" -> MIDNIGHT_BLUE
                "sage_green" -> SAGE_GREEN
                "obsidian_dark", "dark_reading" -> OBSIDIAN_DARK
                "rose_sand" -> ROSE_SAND
                "royal_teal", "soft_light" -> ROYAL_TEAL
                else -> EMERALD_GOLD
            }
        }
    }
}

enum class AppearanceMode(val id: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromId(id: String?): AppearanceMode =
            entries.firstOrNull { it.id == id } ?: SYSTEM
    }
}

enum class ArabicFontOption(val id: String) {
    AMIRI("amiri"),
    NOTO_NASKH("noto_naskh"),
    SYSTEM("system");

    companion object {
        fun fromId(id: String?): ArabicFontOption =
            entries.firstOrNull { it.id == id } ?: AMIRI
    }
}

enum class AyahLayoutMode(val id: String) {
    COMPACT("compact"),
    COMFORTABLE("comfortable"),
    SPACIOUS("spacious");

    companion object {
        fun fromId(id: String?): AyahLayoutMode =
            entries.firstOrNull { it.id == id } ?: COMFORTABLE
    }
}

enum class DateFormatOption(val id: String) {
    DMY("dmy"),
    MDY("mdy"),
    YMD("ymd"),
    HIJRI_FIRST("hijri_first");

    companion object {
        fun fromId(id: String?): DateFormatOption =
            entries.firstOrNull { it.id == id } ?: DMY
    }
}

enum class NumberFormatOption(val id: String) {
    WESTERN("western"),
    LOCALE_INDIC("locale_indic");

    companion object {
        fun fromId(id: String?): NumberFormatOption =
            entries.firstOrNull { it.id == id } ?: WESTERN
    }
}

enum class CardAppearanceStyle(val id: String) {
    ELEVATED("elevated"),
    OUTLINED("outlined"),
    FLAT("flat");

    companion object {
        fun fromId(id: String?): CardAppearanceStyle =
            entries.firstOrNull { it.id == id } ?: ELEVATED
    }
}

enum class UiFontStyleOption(val id: String) {
    DEFAULT("default"),
    SERIF("serif"),
    SANS_CLEAN("sans_clean");

    companion object {
        fun fromId(id: String?): UiFontStyleOption =
            entries.firstOrNull { it.id == id } ?: DEFAULT
    }
}

enum class DailyGoalLevel(val id: String, val minutesPerDay: Int, val targetLessonsPerDay: Int) {
    CASUAL_5M("casual_5m", 5, 1),
    STEADY_15M("steady_15m", 15, 2),
    DEDICATED_30M("dedicated_30m", 30, 3);

    companion object {
        fun fromId(id: String?): DailyGoalLevel =
            entries.firstOrNull { it.id == id } ?: STEADY_15M
    }
}

data class AppAccentPreset(
    val id: String,
    val colorHex: Long,
    val labelEn: String,
    val labelUr: String,
    val labelAr: String,
    val labelHi: String
) {
    fun localizedLabel(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> labelUr
        AppLanguage.ARABIC -> labelAr
        AppLanguage.HINDI -> labelHi
        AppLanguage.ENGLISH -> labelEn
    }
}

data class AppSettingsState(
    // A. Language & Region
    val language: AppLanguage = AppLanguage.ENGLISH,
    val dateFormat: DateFormatOption = DateFormatOption.DMY,
    val numberFormat: NumberFormatOption = NumberFormatOption.WESTERN,
    val showTimezoneLabel: Boolean = true,

    // B. Appearance & Personalization
    val theme: AppTheme = AppTheme.EMERALD_GOLD,
    val appearanceMode: AppearanceMode = AppearanceMode.SYSTEM,
    val customAccentHex: Long? = null,
    val uiTextScale: Float = 1.0f,
    val uiFontStyle: UiFontStyleOption = UiFontStyleOption.DEFAULT,
    val cardStyle: CardAppearanceStyle = CardAppearanceStyle.ELEVATED,
    val showHomePrayerCard: Boolean = true,

    // C. Quran & Reading Preferences
    val arabicFontSize: Float = 24f,
    val arabicFontOption: ArabicFontOption = ArabicFontOption.AMIRI,
    val translationFontSize: Float = 15f,
    val ayahLayoutMode: AyahLayoutMode = AyahLayoutMode.COMFORTABLE,
    val showInlineTranslation: Boolean = true,

    // D. Tafseer & Translation Preferences
    val defaultTafseerScholarId: String = "",

    // E. Audio & Playback
    val selectedReciterId: String = "mishary",
    val audioPlaybackSpeed: Float = 1.0f,
    val ayahRepeatCount: Int = 1,

    // F. Prayer & Qibla Preferences
    val qiblaUseTrueNorth: Boolean = true,

    // G. Notifications, Daily Reminders & Quiet Hours
    val notificationsEnabled: Boolean = true,
    val dailyQuranReminderEnabled: Boolean = false,
    val dailyQuranReminderHour: Int = 7,
    val dailyQuranReminderMinute: Int = 0,
    val dailyHadithReminderEnabled: Boolean = false,
    val dailyDuaReminderEnabled: Boolean = false,
    val morningAdhkarReminderEnabled: Boolean = false,
    val eveningAdhkarReminderEnabled: Boolean = false,
    val dndEnabled: Boolean = false,
    val dndStartHour: Int = 22,
    val dndEndHour: Int = 5,

    // H. Learning & Daily Routine
    val dailyGoalLevel: DailyGoalLevel = DailyGoalLevel.STEADY_15M,

    // I. Internet & Storage
    val wifiOnlyDownloads: Boolean = true,
    val allowMobileDataStreaming: Boolean = true,

    // J. Privacy & Security
    val isPinLockEnabled: Boolean = false,

    // K. Accessibility
    val highContrastEnabled: Boolean = false,
    val reducedMotionEnabled: Boolean = false
)


data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameUrdu: String,
    val nameHindi: String,
    val englishMeaning: String,
    val ayahCount: Int,
    val revelationType: RevelationType,
    val startingPara: Int
)

enum class RevelationType {
    MAKKI, MADANI
}

data class Para(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameUrdu: String,
    val nameHindi: String,
    val startSurahNumber: Int,
    val startAyahNumber: Int
)

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val paraNumber: Int,
    val translations: List<AyahTranslation>,
    val tafseers: List<AyahTafseer>
)

data class AyahTranslation(
    val id: String,
    val language: AppLanguage,
    val translatorName: String,
    val editionName: String,
    val translationText: String
)

data class AyahTafseer(
    val id: String,
    val language: AppLanguage,
    val authorName: String,
    val bookTitle: String,
    val tafseerText: String,
    val sourceAttribution: String
)

data class ScholarTafseer(
    val scholarKey: String, // "ibn_kathir", "tabari", "qurtubi", "saadi", "maariful_quran"
    val authorName: String,
    val bookTitle: String,
    val eraAndCredentials: String,
    val tafseerText: String,
    val keyPoints: List<String>,
    val sourceAttribution: String = "",
    val isMachineTranslated: Boolean = false,
    val translationNotice: String = ""
)

data class AyahRevelationContext(
    val sababNuzool: String,       // 3. WHY WAS THIS AYAH REVEALED? (Asbab al-Nuzul & historical background)
    val zamanaNuzool: String,      // 4. WHEN WAS THIS AYAH REVEALED? (Makki/Madani, verified period & event)
    val maqsadHikmat: String,      // 5. HIDDEN WISDOM AND DEEP MEANING (Deeper message, wisdom, surrounding ayahs, cited reference)
    val fazaailBenefits: String,   // 6. BENEFITS AND PRACTICAL LESSONS (Spiritual, moral & everyday guidance)
    val authenticReferences: String = "", // 7. AUTHENTIC REFERENCES (Tafseer book/scholar, volume/page, Asbab & Hadith refs)
    val referenceLinks: List<Pair<String, String>> = emptyList() // Label to official source URL
)

data class Reciter(
    val id: String,
    val nameArabic: String,
    val nameEnglish: String,
    val nameUrdu: String,
    val style: String,
    val audioBaseUrl: String
)

data class Hadith(
    val id: String,
    val collection: String,
    val bookNumber: Int,
    val bookName: String,
    val hadithNumber: Int,
    val chapterArabic: String,
    val chapterEnglish: String,
    val chapterUrdu: String,
    val chapterHindi: String,
    val textArabic: String,
    val textEnglish: String,
    val textUrdu: String,
    val textHindi: String,
    val narrator: String,
    val grade: String,
    val sourceAttribution: String,
    val volumeNumber: Int = 1,
    val chapterNumber: Int = 1,
    val sourceUrl: String = "",
    val editionName: String = "",
    val translatorName: String = "",
    val licenseName: String = "",
    val isHindiAvailable: Boolean = true,
    val isBundled: Boolean = true
)

data class Dua(
    val id: String,
    val categoryId: String,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val arabic: String,
    val transliteration: String,
    val translationEn: String,
    val translationUr: String,
    val translationHi: String,
    val reference: String,
    val occasionEn: String,
    val occasionUr: String = "",
    val occasionAr: String = "",
    val occasionHi: String = "",
    val bookName: String = "",
    val hadithOrAyahNumber: String = "",
    val grade: String = "Sahih",
    val recommendedTime: String = "",
    val virtuesBenefitsEn: String = "",
    val virtuesBenefitsUr: String = "",
    val virtuesBenefitsAr: String = "",
    val virtuesBenefitsHi: String = "",
    val repeatCount: Int = 1,
    val audioAssetPath: String? = null,
    val narrator: String = "",
    val sourceUrl: String = "",
    val methodEn: String = "",
    val methodUr: String = "",
    val methodAr: String = "",
    val methodHi: String = "",
    val audioSources: List<String> = emptyList(),
    val audioReciter: String = "",
    val audioSourceName: String = "",
    val audioLicense: String = ""
) {
    val hasGenuineAudio: Boolean
        get() = audioSources.isNotEmpty() || !audioAssetPath.isNullOrBlank()
}

data class Lecture(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val titleHi: String,
    val scholar: String,
    val categoryEn: String,
    val categoryUr: String,
    val duration: String,
    val videoId: String,
    val isTrending: Boolean,
    val datePublished: String
)

data class Podcast(
    val id: String,
    val title: String,
    val speaker: String,
    val episodeTitle: String,
    val description: String,
    val duration: String,
    val language: String,
    val publisher: String,
    val publishDate: String,
    val audioUrl: String
)

data class PrayerSchedule(
    val dateStr: String,
    val hijriDate: String,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val calculationMethod: String,
    val cityName: String
)

data class BookmarkItem(
    val id: String,
    val type: BookmarkType,
    val title: String,
    val subtitle: String,
    val surahNumber: Int? = null,
    val ayahNumber: Int? = null,
    val hadithId: String? = null,
    val duaId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class BookmarkType {
    QURAN, HADITH, DUA
}

data class DownloadedSurahAudio(
    val surahNumber: Int,
    val reciterId: String,
    val sizeBytes: Long,
    val isCompleted: Boolean,
    val progressPercent: Int = 100,
    val localFilePath: String
)

data class ZakatCalculationInput(
    val cashOnHand: Double = 0.0,
    val bankBalance: Double = 0.0,
    val goldGrams: Double = 0.0,
    val goldGramPrice: Double = 0.0,
    val silverGrams: Double = 0.0,
    val silverGramPrice: Double = 0.0,
    val businessInventory: Double = 0.0,
    val receivables: Double = 0.0,
    val otherInvestments: Double = 0.0,
    val debtsAndLiabilities: Double = 0.0,
    val selectedNisabType: NisabType = NisabType.SILVER,
    val currency: String = "USD"
)

enum class NisabType {
    SILVER, GOLD
}

data class ZakatResult(
    val totalAssets: Double,
    val deductibleLiabilities: Double,
    val netZakatableWealth: Double,
    val nisabThreshold: Double,
    val isEligible: Boolean,
    val zakatPayable: Double,
    val currency: String
)

data class ArabicLetter(
    val letter: String,
    val nameArabic: String,
    val nameTranslit: String,
    val makhrajEn: String,
    val makhrajUr: String,
    val makhrajHi: String,
    val isolated: String,
    val initial: String,
    val medial: String,
    val finalPos: String,
    val exampleArabic: String,
    val exampleMeaning: String,
    val makhrajAr: String = ""
)

data class TajweedRule(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val category: String,
    val explanationEn: String,
    val explanationUr: String,
    val explanationHi: String,
    val examples: List<TajweedExample>,
    val explanationAr: String = ""
)

data class TajweedExample(
    val arabicText: String,
    val surahReference: String,
    val ruleApplicationNote: String,
    val ruleApplicationNoteUr: String = "",
    val ruleApplicationNoteAr: String = "",
    val ruleApplicationNoteHi: String = ""
) {
    fun noteFor(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> ruleApplicationNote
        AppLanguage.URDU -> ruleApplicationNoteUr
        AppLanguage.ARABIC -> ruleApplicationNoteAr
        AppLanguage.HINDI -> ruleApplicationNoteHi
    }
}
