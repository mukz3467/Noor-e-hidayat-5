package com.example.data.asma

import com.example.data.model.AppLanguage

enum class NameEvidenceCategory {
    QURAN_EXPLICIT,
    AUTHENTIC_HADITH,
    SCHOLARLY_COMPILATION
}

data class AsmaName(
    val number: Int,
    val arabic: String,
    val transliteration: String,
    val meaningEn: String,
    val meaningUr: String,
    val meaningHi: String,
    val explanationAr: String,
    val explanationEn: String,
    val explanationUr: String,
    val explanationHi: String,
    val evidenceCategory: NameEvidenceCategory,
    val quranReference: String?,
    val hadithReference: String?,
    val sourceUrl: String
) {
    fun localizedMeaning(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> meaningEn
        AppLanguage.URDU -> meaningUr
        AppLanguage.ARABIC -> explanationAr
        AppLanguage.HINDI -> meaningHi
    }

    fun localizedExplanation(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> explanationEn
        AppLanguage.URDU -> explanationUr
        AppLanguage.ARABIC -> explanationAr
        AppLanguage.HINDI -> explanationHi
    }
}

enum class AsmaFilterOption {
    ALL,
    QURANIC,
    HADITH,
    COMPILATION,
    FAVORITES
}

data class AsmaState(
    val favoriteNumbers: Set<Int> = emptySet(),
    val exploredNumbers: Set<Int> = emptySet()
)
