package com.example.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "TafseerSources")
data class TafseerSourceEntity(
    @PrimaryKey val id: String,
    val displayOrder: Int,
    val scholarName: String,
    val translatorName: String,
    val reviewers: String,
    val bookTitle: String,
    val originalLanguage: String,
    val publisher: String,
    val edition: String,
    val sourceUrl: String,
    val licenseEvidenceUrl: String,
    val permissionStatus: String, // AUTHORIZED_OFFLINE, ONLINE_ONLY, REFERENCE_ONLY_PENDING_PERMISSION
    val verificationStatus: String,
    val offlineAllowed: Boolean,
    val onlineAllowed: Boolean,
    val isDownloaded: Boolean = false,
    val downloadedBytes: Long = 0L,
    val totalPassagesCached: Int = 0,
    val lastSyncedTimestamp: Long = 0L
)

@Entity(
    tableName = "TafseerPassages",
    foreignKeys = [
        ForeignKey(
            entity = TafseerSourceEntity::class,
            parentColumns = ["id"],
            childColumns = ["sourceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["sourceId", "surahNumber", "startAyah", "endAyah"])
    ]
)
data class TafseerPassageEntity(
    @PrimaryKey val id: String,
    val sourceId: String,
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val originalText: String,
    val volume: String,
    val page: String,
    val sourceReference: String,
    val verified: Boolean
)

@Entity(
    tableName = "TafseerTranslations",
    foreignKeys = [
        ForeignKey(
            entity = TafseerPassageEntity::class,
            parentColumns = ["id"],
            childColumns = ["passageId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["passageId", "language"])
    ]
)
data class TafseerTranslationEntity(
    @PrimaryKey val id: String,
    val passageId: String,
    val language: String,
    val translatedText: String,
    val translatorName: String,
    val translationType: String, // ORIGINAL_PUBLISHED, PUBLISHED_EDITION, MACHINE_TRANSLATION
    val isMachineTranslated: Boolean,
    val verificationStatus: String
)

@Entity(
    tableName = "AsbabAlNuzul",
    indices = [
        Index(value = ["surahNumber", "startAyah", "endAyah"])
    ]
)
data class AsbabAlNuzulEntity(
    @PrimaryKey val id: String,
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val narration: String,
    val sourceBook: String,
    val hadithReference: String,
    val authenticityGrade: String,
    val verificationStatus: String
)

@Entity(
    tableName = "TafseerBenefits",
    indices = [
        Index(value = ["surahNumber", "ayahNumber"])
    ]
)
data class TafseerBenefitEntity(
    @PrimaryKey val id: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val benefitText: String,
    val sourceReference: String,
    val contentType: String, // CITED_HADITH, CITED_TAFSEER, SUMMARY_LABELLED
    val verificationStatus: String
)

@Entity(tableName = "TranslationCatalog")
data class TranslationCatalogEntity(
    @PrimaryKey val translationId: String,
    val languageCode: String, // ur, en, hi, ar
    val translator: String,
    val publisher: String,
    val editionVersion: String,
    val sourceUrl: String,
    val licenseName: String,
    val licenseUrl: String,
    val lastUpdatedDate: String,
    val sourceProvider: String, // TANZIL_BUNDLED, QURANENC, QURAN_FOUNDATION
    val remoteKey: String,
    val isBundledOffline: Boolean,
    val offlineAllowed: Boolean,
    val onlineAllowed: Boolean,
    val isDownloadedOffline: Boolean = false,
    val downloadedAyahCount: Int = 0,
    val sizeBytes: Long = 0L,
    val isMachineTranslated: Boolean = false
)

@Entity(
    tableName = "TranslationVerses",
    foreignKeys = [
        ForeignKey(
            entity = TranslationCatalogEntity::class,
            parentColumns = ["translationId"],
            childColumns = ["translationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["translationId", "surahNumber", "ayahNumber"], unique = true)
    ]
)
data class TranslationVerseEntity(
    @PrimaryKey val id: String,
    val translationId: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val translatedText: String,
    val footnotes: String = ""
)
