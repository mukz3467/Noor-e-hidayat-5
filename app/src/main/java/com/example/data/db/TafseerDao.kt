package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TafseerDao {

    // 1. TafseerSources
    @Query("SELECT * FROM TafseerSources ORDER BY displayOrder ASC")
    fun observeTafseerSources(): Flow<List<TafseerSourceEntity>>

    @Query("SELECT * FROM TafseerSources ORDER BY displayOrder ASC")
    suspend fun getTafseerSources(): List<TafseerSourceEntity>

    @Query("SELECT * FROM TafseerSources WHERE id = :id LIMIT 1")
    suspend fun getTafseerSourceById(id: String): TafseerSourceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTafseerSources(sources: List<TafseerSourceEntity>)

    @Query("DELETE FROM TafseerSources WHERE offlineAllowed = 0")
    suspend fun deleteNonOfflineTafseerSources()

    @Query("DELETE FROM TafseerSources WHERE id NOT IN (:activeIds)")
    suspend fun retainOnlyTafseerSources(activeIds: List<String>)

    @Query(
        "UPDATE TafseerSources SET isDownloaded = :isDownloaded, downloadedBytes = :bytes, totalPassagesCached = :count, lastSyncedTimestamp = :timestamp WHERE id = :sourceId"
    )
    suspend fun updateSourceDownloadState(
        sourceId: String,
        isDownloaded: Boolean,
        bytes: Long,
        count: Int,
        timestamp: Long
    )

    // 2. TafseerPassages (supports multi-ayah ranges via startAyah <= ayah <= endAyah)
    @Query(
        "SELECT * FROM TafseerPassages WHERE sourceId = :sourceId AND surahNumber = :surahNumber AND startAyah <= :ayahNumber AND endAyah >= :ayahNumber LIMIT 1"
    )
    suspend fun getPassageForAyah(
        sourceId: String,
        surahNumber: Int,
        ayahNumber: Int
    ): TafseerPassageEntity?

    @Query("SELECT COUNT(*) FROM TafseerPassages WHERE sourceId = :sourceId")
    suspend fun getPassageCountForSource(sourceId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPassages(passages: List<TafseerPassageEntity>)

    @Query("DELETE FROM TafseerPassages WHERE sourceId = :sourceId")
    suspend fun deletePassagesForSource(sourceId: String)

    // 3. TafseerTranslations
    @Query("SELECT * FROM TafseerTranslations WHERE passageId = :passageId")
    suspend fun getTranslationsForPassage(passageId: String): List<TafseerTranslationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTafseerTranslations(translations: List<TafseerTranslationEntity>)

    // 4. AsbabAlNuzul
    @Query(
        "SELECT * FROM AsbabAlNuzul WHERE surahNumber = :surahNumber AND startAyah <= :ayahNumber AND endAyah >= :ayahNumber"
    )
    suspend fun getAsbabAlNuzulForAyah(
        surahNumber: Int,
        ayahNumber: Int
    ): List<AsbabAlNuzulEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAsbabAlNuzul(items: List<AsbabAlNuzulEntity>)

    // 5. TafseerBenefits
    @Query("SELECT * FROM TafseerBenefits WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    suspend fun getBenefitsForAyah(
        surahNumber: Int,
        ayahNumber: Int
    ): List<TafseerBenefitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTafseerBenefits(items: List<TafseerBenefitEntity>)

    // 6. TranslationCatalog
    @Query("SELECT * FROM TranslationCatalog ORDER BY isBundledOffline DESC, isDownloadedOffline DESC, offlineAllowed DESC, translator ASC")
    fun observeTranslationCatalog(): Flow<List<TranslationCatalogEntity>>

    @Query("SELECT * FROM TranslationCatalog WHERE languageCode = :langCode ORDER BY isBundledOffline DESC, isDownloadedOffline DESC, offlineAllowed DESC, translator ASC")
    fun observeTranslationsByLanguage(langCode: String): Flow<List<TranslationCatalogEntity>>

    @Query("SELECT * FROM TranslationCatalog ORDER BY isBundledOffline DESC, translator ASC")
    suspend fun getTranslationCatalog(): List<TranslationCatalogEntity>

    @Query("SELECT * FROM TranslationCatalog WHERE languageCode = :langCode ORDER BY isBundledOffline DESC, isDownloadedOffline DESC, offlineAllowed DESC, translator ASC")
    suspend fun getTranslationsByLanguage(langCode: String): List<TranslationCatalogEntity>

    @Query("SELECT * FROM TranslationCatalog WHERE translationId = :translationId LIMIT 1")
    suspend fun getTranslationCatalogById(translationId: String): TranslationCatalogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTranslationCatalog(items: List<TranslationCatalogEntity>)

    @Query(
        "UPDATE TranslationCatalog SET isDownloadedOffline = :isDownloaded, downloadedAyahCount = :ayahCount, sizeBytes = :sizeBytes WHERE translationId = :translationId"
    )
    suspend fun updateTranslationDownloadState(
        translationId: String,
        isDownloaded: Boolean,
        ayahCount: Int,
        sizeBytes: Long
    )

    // 7. TranslationVerses
    @Query(
        "SELECT * FROM TranslationVerses WHERE translationId = :translationId AND surahNumber = :surahNumber AND ayahNumber = :ayahNumber LIMIT 1"
    )
    suspend fun getDownloadedVerseTranslation(
        translationId: String,
        surahNumber: Int,
        ayahNumber: Int
    ): TranslationVerseEntity?

    @Query(
        "SELECT * FROM TranslationVerses WHERE translationId = :translationId AND surahNumber = :surahNumber ORDER BY ayahNumber ASC"
    )
    suspend fun getDownloadedSurahTranslations(
        translationId: String,
        surahNumber: Int
    ): List<TranslationVerseEntity>

    @Query("SELECT COUNT(*) FROM TranslationVerses WHERE translationId = :translationId")
    suspend fun countDownloadedVerses(translationId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTranslationVerses(verses: List<TranslationVerseEntity>)

    @Query("DELETE FROM TranslationVerses WHERE translationId = :translationId")
    suspend fun deleteTranslationVerses(translationId: String)
}
