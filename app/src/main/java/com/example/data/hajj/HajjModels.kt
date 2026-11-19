package com.example.data.hajj

import com.example.data.model.AppLanguage

enum class PilgrimageMode {
    UMRAH,
    HAJJ
}

enum class HajjSubType {
    TAMATTU,
    QIRAN,
    IFRAD
}

enum class FiqhSchool {
    COMPARATIVE,
    HANAFI,
    SHAFII,
    MALIKI,
    HANBALI
}

enum class RulingLevel {
    RUKN_PILLAR,
    WAJIB_NECESSARY,
    SUNNAH_RECOMMENDED,
    CONDITION_SHART,
    MUBAH_OPTIONAL
}

enum class GuideSectionTab {
    INTRO,
    IHRAM,
    RITUALS,
    DUAS,
    PRACTICAL
}

enum class ChecklistCategory {
    PACKING,
    TRAVEL_PREP,
    IHRAM_PREP
}

data class LocalizedText(
    val en: String,
    val ur: String,
    val ar: String,
    val hi: String
) {
    fun get(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> en
        AppLanguage.URDU -> ur
        AppLanguage.ARABIC -> ar
        AppLanguage.HINDI -> hi
    }
}

data class MadhhabDifferenceNote(
    val topic: LocalizedText,
    val hanafi: LocalizedText,
    val shafii: LocalizedText,
    val maliki: LocalizedText,
    val hanbali: LocalizedText,
    val reference: String
) {
    fun viewForSchool(school: FiqhSchool, language: AppLanguage): String = when (school) {
        FiqhSchool.HANAFI -> "Hanafi: ${hanafi.get(language)}"
        FiqhSchool.SHAFII -> "Shafi'i: ${shafii.get(language)}"
        FiqhSchool.MALIKI -> "Maliki: ${maliki.get(language)}"
        FiqhSchool.HANBALI -> "Hanbali: ${hanbali.get(language)}"
        FiqhSchool.COMPARATIVE -> buildString {
            appendLine("• Hanafi: ${hanafi.get(language)}")
            appendLine("• Shafi'i: ${shafii.get(language)}")
            appendLine("• Maliki: ${maliki.get(language)}")
            append("• Hanbali: ${hanbali.get(language)}")
        }
    }
}

data class GuideStep(
    val id: String,
    val mode: PilgrimageMode,
    val orderIndex: Int,
    val stageBadge: LocalizedText,
    val title: LocalizedText,
    val rulingLevel: RulingLevel,
    val summary: LocalizedText,
    val instructions: LocalizedText,
    val menVsWomenNote: LocalizedText? = null,
    val hajjFormNote: LocalizedText? = null,
    val references: String,
    val madhhabNotes: List<MadhhabDifferenceNote> = emptyList()
)

data class GuideTerm(
    val id: String,
    val term: String,
    val arabic: String,
    val definition: LocalizedText
)

data class MeeqatPoint(
    val id: String,
    val name: LocalizedText,
    val arabic: String,
    val servesRegions: LocalizedText,
    val reference: String
)

data class IhramProhibition(
    val id: String,
    val title: LocalizedText,
    val scopeNote: LocalizedText,
    val reference: String
)

data class GuideChecklistItem(
    val id: String,
    val category: ChecklistCategory,
    val title: LocalizedText,
    val isCustom: Boolean = false
)

data class HajjDuaEntry(
    val id: String,
    val linkedDuaDataId: String? = null,
    val occasion: LocalizedText,
    val arabic: String,
    val transliteration: String,
    val translation: LocalizedText,
    val reference: String,
    val sourceUrl: String
)

data class HajjPersonalNote(
    val id: String,
    val title: String,
    val content: String,
    val stepId: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)

data class HajjGuideState(
    val selectedMode: PilgrimageMode = PilgrimageMode.UMRAH,
    val selectedSubType: HajjSubType = HajjSubType.TAMATTU,
    val selectedSchool: FiqhSchool = FiqhSchool.COMPARATIVE,
    val completedStepIds: Set<String> = emptySet(),
    val checkedChecklistIds: Set<String> = emptySet(),
    val customChecklistItems: List<GuideChecklistItem> = emptyList(),
    val notes: List<HajjPersonalNote> = emptyList()
)
