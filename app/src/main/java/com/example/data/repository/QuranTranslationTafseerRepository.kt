package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.db.AsbabAlNuzulEntity
import com.example.data.db.NoorDatabase
import com.example.data.db.NoorDatabaseSeeder
import com.example.data.db.TafseerBenefitEntity
import com.example.data.db.TafseerDao
import com.example.data.db.TafseerPassageEntity
import com.example.data.db.TafseerSourceEntity
import com.example.data.db.TranslationCatalogEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Ayah
import com.example.data.model.AyahRevelationContext
import com.example.data.model.Hadith
import com.example.data.model.RevelationType
import com.example.data.network.QuranCatalogApiService
import com.example.data.network.QuranFoundationChapterInfo
import com.example.data.network.QuranFoundationTafsirResource
import com.example.data.network.QuranFoundationTafsirResult
import com.example.localization.LocalizedContentResolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

data class TranslationDownloadState(
    val translationId: String,
    val isDownloading: Boolean = false,
    val currentSurah: Int = 0,
    val totalSurahs: Int = 114,
    val downloadedBytes: Long = 0L,
    val errorMessage: String? = null
)

data class ResolvedAyahTranslation(
    val translationId: String,
    val translatorName: String,
    val publisher: String,
    val editionVersion: String,
    val licenseName: String,
    val sourceUrl: String,
    val translatedText: String,
    val footnotes: String,
    val isOfflineAvailable: Boolean,
    val isOnlineOnly: Boolean,
    val isUnavailableNotice: String? = null
)

private data class OnlineCachedTafseerPassage(
    val text: String,
    val startAyah: Int,
    val endAyah: Int,
    val resolvedEndpointLabel: String
)

data class ResolvedTafseerDetail(
    val source: TafseerSourceEntity,
    val surahNumber: Int,
    val ayahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val passageText: String?,
    val passageLanguageNotice: String?,
    val languageAvailabilityNote: String? = null,
    val editionAttribution: String,
    val sourceUrl: String = "",
    val volumeAndPage: String?,
    val historicalBackground: String,
    val asbabAlNuzulList: List<AsbabAlNuzulEntity>,
    val asbabDisplayText: String,
    val unavailableAsbabMessage: String,
    val benefitsList: List<TafseerBenefitEntity>,
    val relatedHadiths: List<Hadith>,
    val isLoadingOnline: Boolean = false,
    val onlineError: String? = null
)

class QuranTranslationTafseerRepository private constructor(
    private val context: Context,
    private val dao: TafseerDao,
    private val prefs: SharedPreferences
) {
    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val downloadJobs = ConcurrentHashMap<String, Job>()

    // Volatile in-memory session caches ONLY (never written to offline disk)
    private val onlineTafsirSessionCache = ConcurrentHashMap<String, QuranFoundationTafsirResult>()
    private val onlineScholarTafseerCache = ConcurrentHashMap<String, OnlineCachedTafseerPassage>()
    private val onlineWahidiAsbabCache = ConcurrentHashMap<String, String>()
    private val onlineChapterInfoCache = ConcurrentHashMap<String, QuranFoundationChapterInfo>()
    private val onlineQfVerseCache = ConcurrentHashMap<String, String>()
    private val qfTafsirResourcesCache = MutableStateFlow<List<QuranFoundationTafsirResource>>(emptyList())

    fun clearOnlineTafseerSessionCache() {
        onlineScholarTafseerCache.clear()
        onlineWahidiAsbabCache.clear()
        onlineChapterInfoCache.clear()
        QuranCatalogApiService.clearSessionCaches()
    }

    private val _downloadStates = MutableStateFlow<Map<String, TranslationDownloadState>>(emptyMap())
    val downloadStates: StateFlow<Map<String, TranslationDownloadState>> = _downloadStates.asStateFlow()

    private val _selectedTranslatorIds = MutableStateFlow<Map<String, String>>(loadSavedTranslators())
    val selectedTranslatorIds: StateFlow<Map<String, String>> = _selectedTranslatorIds.asStateFlow()

    init {
        repoScope.launch {
            NoorDatabaseSeeder.seedIfEmpty(dao)
        }
    }

    fun observeTafseerSources(): Flow<List<TafseerSourceEntity>> = dao.observeTafseerSources()

    fun observeTranslationCatalog(): Flow<List<TranslationCatalogEntity>> = dao.observeTranslationCatalog()

    fun observeTranslationsByLanguage(language: AppLanguage): Flow<List<TranslationCatalogEntity>> =
        dao.observeTranslationsByLanguage(language.code)

    suspend fun ensureSeeded() {
        NoorDatabaseSeeder.seedIfEmpty(dao)
    }

    fun getSelectedTranslatorId(language: AppLanguage): String {
        return _selectedTranslatorIds.value[language.code] ?: defaultTranslatorIdFor(language)
    }

    fun selectTranslator(language: AppLanguage, translationId: String) {
        prefs.edit().putString("active_trans_${language.code}", translationId).apply()
        _selectedTranslatorIds.update { current ->
            current + (language.code to translationId)
        }
    }

    private fun loadSavedTranslators(): Map<String, String> {
        return AppLanguage.entries.associate { lang ->
            lang.code to (prefs.getString("active_trans_${lang.code}", null) ?: defaultTranslatorIdFor(lang))
        }
    }

    private fun defaultTranslatorIdFor(language: AppLanguage): String {
        return when (language) {
            AppLanguage.URDU -> "ur_jalandhry_bundled"
            AppLanguage.ENGLISH -> "en_sahih_bundled"
            AppLanguage.HINDI -> "hi_farooq_bundled"
            AppLanguage.ARABIC -> "tanzil_ar_uthmani"
        }
    }

    /**
     * Dynamically refreshes official QuranEnc and Quran Foundation translation catalogs
     * without overwriting user's local download states.
     */
    suspend fun refreshRemoteTranslationCatalog(language: AppLanguage): Result<Int> = withContext(Dispatchers.IO) {
        ensureSeeded()
        var totalAddedOrUpdated = 0

        val quranEncResult = QuranCatalogApiService.fetchQuranEncTranslationsList(language.code)
        val qfResult = QuranCatalogApiService.fetchQuranFoundationTranslationsList()

        val existingMap = dao.getTranslationCatalog().associateBy { it.translationId }
        val toUpsert = mutableListOf<TranslationCatalogEntity>()

        quranEncResult.getOrNull()?.forEach { remoteItem ->
            val existing = existingMap[remoteItem.translationId]
            toUpsert.add(
                if (existing != null) {
                    remoteItem.copy(
                        isDownloadedOffline = existing.isDownloadedOffline,
                        downloadedAyahCount = existing.downloadedAyahCount,
                        sizeBytes = existing.sizeBytes
                    )
                } else remoteItem
            )
        }

        qfResult.getOrNull()?.forEach { qfItem ->
            // Strictly keep Quran Foundation translations online-only (offlineAllowed = false)
            toUpsert.add(qfItem.copy(offlineAllowed = false, isDownloadedOffline = false))
        }

        if (toUpsert.isNotEmpty()) {
            dao.upsertTranslationCatalog(toUpsert)
            totalAddedOrUpdated = toUpsert.size
            Result.success(totalAddedOrUpdated)
        } else {
            val err = quranEncResult.exceptionOrNull() ?: qfResult.exceptionOrNull()
            if (err != null) Result.failure(err) else Result.success(0)
        }
    }

    /**
     * Resolves the complete Tarjuma for an Ayah using the selected verified translator.
     * Uses local Room DB if downloaded/bundled, or live authorized API if online-only.
     */
    suspend fun resolveAyahTranslation(
        ayah: Ayah,
        language: AppLanguage,
        requestedTranslationId: String? = null
    ): ResolvedAyahTranslation = withContext(Dispatchers.IO) {
        ensureSeeded()
        val transId = requestedTranslationId ?: getSelectedTranslatorId(language)
        val meta = dao.getTranslationCatalogById(transId)
            ?: dao.getTranslationCatalogById(defaultTranslatorIdFor(language))!!

        // 1. Check pre-bundled verified QuranEnc editions (urdu_junagarhi, english_rwwad, hindi_omari, arabic_mokhtasar, english_mokhtasar, hindi_mokhtasar)
        if (meta.sourceProvider == "QURANENC" && QuranTarjumaAssetsLoader.hasBundledQuranEncEdition(meta.remoteKey)) {
            val bundledEnc = QuranTarjumaAssetsLoader.getBundledQuranEncVerse(
                context = context,
                editionKey = meta.remoteKey,
                surahNumber = ayah.surahNumber,
                ayahNumber = ayah.ayahNumber
            )
            if (bundledEnc != null) {
                return@withContext ResolvedAyahTranslation(
                    translationId = meta.translationId,
                    translatorName = meta.translator,
                    publisher = meta.publisher,
                    editionVersion = meta.editionVersion,
                    licenseName = meta.licenseName,
                    sourceUrl = meta.sourceUrl,
                    translatedText = bundledEnc.translation,
                    footnotes = bundledEnc.footnotes,
                    isOfflineAvailable = true,
                    isOnlineOnly = false
                )
            }
        }

        // 2. Check bundled Tanzil-sourced assets (6,236 ayahs offline) and enrich footnotes from verified QuranEnc scholarly notes
        if (meta.isBundledOffline) {
            val bundledTrans = ayah.translations.firstOrNull { it.language == language }
            val text = when {
                language == AppLanguage.ARABIC -> ayah.textArabic
                bundledTrans != null && bundledTrans.translationText.isNotBlank() -> bundledTrans.translationText
                else -> ""
            }
            val unavailableNotice = if (language != AppLanguage.ARABIC && text.isBlank()) {
                LocalizedContentResolver.unavailableTranslationNotice(language)
            } else null
            val attributedFootnote = when (language) {
                AppLanguage.URDU -> QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, "urdu_junagarhi", ayah.surahNumber, ayah.ayahNumber)
                    ?.footnotes?.takeIf { it.isNotBlank() }
                    ?.let { "حاشیہ (مولانا محمد جوناگڑھی / QuranEnc):\n$it" } ?: ""
                AppLanguage.ENGLISH -> QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, "english_rwwad", ayah.surahNumber, ayah.ayahNumber)
                    ?.footnotes?.takeIf { it.isNotBlank() }
                    ?.let { "Explanatory Notes (Rowwad Translation Center / QuranEnc):\n$it" } ?: ""
                AppLanguage.HINDI -> QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, "hindi_omari", ayah.surahNumber, ayah.ayahNumber)
                    ?.footnotes?.takeIf { it.isNotBlank() }
                    ?.let { "टिप्पणी (मौलाना अज़ीज़ुल हक़ अल-उमरी / QuranEnc):\n$it" } ?: ""
                AppLanguage.ARABIC -> QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, "arabic_mokhtasar", ayah.surahNumber, ayah.ayahNumber)
                    ?.translation?.takeIf { it.isNotBlank() }
                    ?.let { "المختصر في تفسير القرآن الكريم (مركز تفسير):\n$it" } ?: ""
            }
            return@withContext ResolvedAyahTranslation(
                translationId = meta.translationId,
                translatorName = meta.translator,
                publisher = meta.publisher,
                editionVersion = meta.editionVersion,
                licenseName = meta.licenseName,
                sourceUrl = meta.sourceUrl,
                translatedText = text,
                footnotes = attributedFootnote,
                isOfflineAvailable = text.isNotBlank(),
                isOnlineOnly = false,
                isUnavailableNotice = unavailableNotice
            )
        }

        // 2. Check downloaded Room cache (authorized QuranEnc editions only)
        if (meta.offlineAllowed && meta.isDownloadedOffline) {
            val cached = dao.getDownloadedVerseTranslation(meta.translationId, ayah.surahNumber, ayah.ayahNumber)
            if (cached != null && cached.translatedText.isNotBlank()) {
                return@withContext ResolvedAyahTranslation(
                    translationId = meta.translationId,
                    translatorName = meta.translator,
                    publisher = meta.publisher,
                    editionVersion = meta.editionVersion,
                    licenseName = meta.licenseName,
                    sourceUrl = meta.sourceUrl,
                    translatedText = cached.translatedText,
                    footnotes = cached.footnotes,
                    isOfflineAvailable = true,
                    isOnlineOnly = false
                )
            }
        }

        // 3. Online fetch (QuranEnc live surah OR Quran Foundation online-only API)
        if (meta.sourceProvider == "QURANENC") {
            val surahRes = QuranCatalogApiService.fetchQuranEncSurah(
                translationId = meta.translationId,
                remoteKey = meta.remoteKey,
                surahNumber = ayah.surahNumber
            )
            val matched = surahRes.getOrNull()?.first?.firstOrNull { it.ayahNumber == ayah.ayahNumber }
            if (matched != null) {
                return@withContext ResolvedAyahTranslation(
                    translationId = meta.translationId,
                    translatorName = meta.translator,
                    publisher = meta.publisher,
                    editionVersion = meta.editionVersion,
                    licenseName = meta.licenseName,
                    sourceUrl = meta.sourceUrl,
                    translatedText = matched.translatedText,
                    footnotes = matched.footnotes,
                    isOfflineAvailable = false,
                    isOnlineOnly = false
                )
            }
        } else if (meta.sourceProvider == "QURAN_FOUNDATION") {
            val cacheKey = "${meta.remoteKey}:${ayah.surahNumber}:${ayah.ayahNumber}"
            val liveText = onlineQfVerseCache[cacheKey] ?: run {
                val res = QuranCatalogApiService.fetchQuranFoundationVerseTranslation(
                    translationNumericId = meta.remoteKey,
                    surahNumber = ayah.surahNumber,
                    ayahNumber = ayah.ayahNumber
                ).getOrNull()
                if (!res.isNullOrBlank()) {
                    onlineQfVerseCache[cacheKey] = res
                }
                res
            }
            if (!liveText.isNullOrBlank()) {
                return@withContext ResolvedAyahTranslation(
                    translationId = meta.translationId,
                    translatorName = meta.translator,
                    publisher = meta.publisher,
                    editionVersion = meta.editionVersion,
                    licenseName = meta.licenseName,
                    sourceUrl = meta.sourceUrl,
                    translatedText = liveText,
                    footnotes = "",
                    isOfflineAvailable = false,
                    isOnlineOnly = true
                )
            }
        }

        // 4. Honest fallback notice (no silent fabrication!)
        val unavailableMessage = when (language) {
            AppLanguage.URDU -> "${meta.translator} (${meta.editionVersion}) اس وقت حاصل نہیں ہو سکا۔ انٹرنیٹ سے منسلک ہوں یا آف لائن ڈاؤن لوڈ شدہ ترجمہ منتخب کریں۔"
            AppLanguage.ARABIC -> "تعذر تحميل (${meta.translator}) حالياً. يرجى التحقق من الاتصال بالإنترنت أو اختيار نسخة متاحة دون اتصال."
            AppLanguage.HINDI -> "${meta.translator} (${meta.editionVersion}) अभी लोड नहीं हो सका। इंटरनेट से जुड़ें या ऑफ़लाइन अनुवाद चुनें।"
            AppLanguage.ENGLISH -> "Unable to fetch ${meta.translator} (${meta.editionVersion}) right now. Connect to the internet or select an offline downloaded translation."
        }
        ResolvedAyahTranslation(
            translationId = meta.translationId,
            translatorName = meta.translator,
            publisher = meta.publisher,
            editionVersion = meta.editionVersion,
            licenseName = meta.licenseName,
            sourceUrl = meta.sourceUrl,
            translatedText = "",
            footnotes = "",
            isOfflineAvailable = false,
            isOnlineOnly = !meta.offlineAllowed,
            isUnavailableNotice = unavailableMessage
        )
    }

    /**
     * Downloads all 114 Surahs of an authorized QuranEnc translation into Room.
     * - Strictly refuses non-offlineAllowed sources (e.g. Quran Foundation).
     * - Never marks incomplete downloads as completed.
     */
    fun startTranslationDownload(translationId: String) {
        if (downloadJobs[translationId]?.isActive == true) return

        val job = repoScope.launch {
            val meta = dao.getTranslationCatalogById(translationId)
            if (meta == null || !meta.offlineAllowed || meta.sourceProvider != "QURANENC") {
                _downloadStates.update {
                    it + (translationId to TranslationDownloadState(
                        translationId = translationId,
                        isDownloading = false,
                        errorMessage = "Offline downloading is not permitted for this source."
                    ))
                }
                return@launch
            }

            _downloadStates.update {
                it + (translationId to TranslationDownloadState(
                    translationId = translationId,
                    isDownloading = true,
                    currentSurah = 0,
                    totalSurahs = 114
                ))
            }

            // Ensure incomplete download is never marked completed
            dao.updateTranslationDownloadState(translationId, isDownloaded = false, ayahCount = 0, sizeBytes = 0L)

            var totalBytes = 0L
            var totalVerses = 0
            var failed = false
            var failMessage: String? = null

            for (surah in 1..114) {
                val res = QuranCatalogApiService.fetchQuranEncSurah(
                    translationId = translationId,
                    remoteKey = meta.remoteKey,
                    surahNumber = surah
                )
                val pair = res.getOrNull()
                if (pair == null || pair.first.isEmpty()) {
                    failed = true
                    failMessage = res.exceptionOrNull()?.message ?: "Download interrupted at Surah $surah"
                    break
                }
                dao.upsertTranslationVerses(pair.first)
                totalVerses += pair.first.size
                totalBytes += pair.second

                _downloadStates.update {
                    it + (translationId to TranslationDownloadState(
                        translationId = translationId,
                        isDownloading = true,
                        currentSurah = surah,
                        totalSurahs = 114,
                        downloadedBytes = totalBytes
                    ))
                }
            }

            if (!failed && totalVerses >= 6200) {
                dao.updateTranslationDownloadState(
                    translationId = translationId,
                    isDownloaded = true,
                    ayahCount = totalVerses,
                    sizeBytes = totalBytes
                )
                _downloadStates.update {
                    it + (translationId to TranslationDownloadState(
                        translationId = translationId,
                        isDownloading = false,
                        currentSurah = 114,
                        totalSurahs = 114,
                        downloadedBytes = totalBytes
                    ))
                }
            } else {
                // Clean up incomplete data or preserve false completion status
                dao.updateTranslationDownloadState(
                    translationId = translationId,
                    isDownloaded = false,
                    ayahCount = 0,
                    sizeBytes = 0L
                )
                dao.deleteTranslationVerses(translationId)
                _downloadStates.update {
                    it + (translationId to TranslationDownloadState(
                        translationId = translationId,
                        isDownloading = false,
                        currentSurah = 0,
                        totalSurahs = 114,
                        downloadedBytes = 0L,
                        errorMessage = failMessage ?: "Download incomplete — not marked complete."
                    ))
                }
            }
        }
        downloadJobs[translationId] = job
    }

    fun deleteDownloadedTranslation(translationId: String) {
        downloadJobs[translationId]?.cancel()
        repoScope.launch {
            val meta = dao.getTranslationCatalogById(translationId) ?: return@launch
            if (meta.isBundledOffline) return@launch // Do not delete bundled Tanzil core
            dao.deleteTranslationVerses(translationId)
            dao.updateTranslationDownloadState(translationId, isDownloaded = false, ayahCount = 0, sizeBytes = 0L)
            _downloadStates.update { it - translationId }

            // Revert active translator to bundled default if deleted one was selected
            AppLanguage.entries.forEach { lang ->
                if (getSelectedTranslatorId(lang) == translationId) {
                    selectTranslator(lang, defaultTranslatorIdFor(lang))
                }
            }
        }
    }

    /**
     * Builds the complete 7-section Tarjuma study context ([AyahRevelationContext]) using
     * verified Room Asbab al-Nuzul, Hadith-cited benefits, authentic Surah revelation periods,
     * and optional online-only Quran Foundation scholarly commentary in session memory.
     */
    suspend fun loadTarjumaStudyContext(
        ayah: Ayah,
        language: AppLanguage,
        activeTranslation: ResolvedAyahTranslation?,
        fetchOnlineWisdom: Boolean = false
    ): AyahRevelationContext = withContext(Dispatchers.IO) {
        ensureSeeded()
        val asbabList = dao.getAsbabAlNuzulForAyah(ayah.surahNumber, ayah.ayahNumber)
        val benefitsList = dao.getBenefitsForAyah(ayah.surahNumber, ayah.ayahNumber)
        val relatedHadiths = QuranTafseerRepository.findVerifiedHadithsForAyah(ayah.surahNumber, ayah.ayahNumber)

        val preferredSlug: String? = when (language) {
            AppLanguage.URDU -> qfTafsirResourcesCache.value.firstOrNull {
                it.languageName == "urdu" && (it.slug.contains("kathir", true) || it.name.contains("Kathir", true))
            }?.id?.toString()
            AppLanguage.ARABIC -> "14"
            AppLanguage.ENGLISH -> "en-tafisr-ibn-kathir"
            AppLanguage.HINDI -> null
        }
        val editionLabel = when (language) {
            AppLanguage.URDU -> "تفسیر ابنِ کثیر (اردو) — Quran Foundation API"
            AppLanguage.ARABIC -> "تفسير القرآن العظيم للحافظ ابن كثير — Quran Foundation API v4"
            AppLanguage.ENGLISH -> "Tafsir Ibn Kathir (Abridged English, Safiur-Rahman al-Mubarakpuri / Darussalam) — Quran Foundation API v4"
            AppLanguage.HINDI -> "तफ़सीर इब्न कसीर"
        }
        val cacheKey = preferredSlug?.let { "${it}_${ayah.surahNumber}_${ayah.ayahNumber}" }
        val chapterLangCode = when (language) {
            AppLanguage.ARABIC -> "ar"
            AppLanguage.URDU -> "ur"
            AppLanguage.ENGLISH -> "en"
            AppLanguage.HINDI -> null
        }
        val chapterCacheKey = chapterLangCode?.let { "${ayah.surahNumber}_$it" }

        var qfCommentary = cacheKey?.let { onlineTafsirSessionCache[it] }
        var chapterInfo = chapterCacheKey?.let { onlineChapterInfoCache[it] }

        if (fetchOnlineWisdom && ((preferredSlug != null && qfCommentary == null) || (chapterLangCode != null && chapterInfo == null))) {
            val tafsirDeferred = if (preferredSlug != null && qfCommentary == null && cacheKey != null) {
                async {
                    QuranCatalogApiService.fetchQuranFoundationTafsirByAyah(
                        slugOrId = preferredSlug,
                        surahNumber = ayah.surahNumber,
                        ayahNumber = ayah.ayahNumber
                    ).getOrNull()?.also { fetched ->
                        onlineTafsirSessionCache[cacheKey] = fetched
                    }
                }
            } else null

            val chapterDeferred = if (chapterLangCode != null && chapterInfo == null && chapterCacheKey != null) {
                async {
                    QuranCatalogApiService.fetchQuranFoundationChapterInfo(
                        surahNumber = ayah.surahNumber,
                        languageCode = chapterLangCode
                    ).getOrNull()?.also { fetched ->
                        onlineChapterInfoCache[chapterCacheKey] = fetched
                    }
                }
            } else null

            if (tafsirDeferred != null) qfCommentary = tafsirDeferred.await()
            if (chapterDeferred != null) chapterInfo = chapterDeferred.await()
        }

        QuranTafseerRepository.buildVerifiedAyahRevelationContext(
            surahNumber = ayah.surahNumber,
            ayahNumber = ayah.ayahNumber,
            language = language,
            asbabList = asbabList,
            benefitsList = benefitsList,
            relatedHadiths = relatedHadiths,
            onlineQfCommentary = qfCommentary,
            onlineQfEditionLabel = if (qfCommentary != null) editionLabel else null,
            activeTranslation = activeTranslation,
            context = context,
            onlineChapterInfo = chapterInfo
        )
    }

    /**
     * Returns verified online scholarly Tafseer sources strictly matching [language].
     */
    suspend fun getAvailableOnlineTafseerSources(language: AppLanguage): List<TafseerSourceEntity> = withContext(Dispatchers.IO) {
        ensureSeeded()
        val orderedIds = VerifiedTafseerCatalog.onlineEditionsFor(language).map { it.id }
        val dbMap = dao.getTafseerSources().associateBy { it.id }
        val fallbackMap = VerifiedTafseerCatalog.toTafseerSourceEntities().associateBy { it.id }
        orderedIds.mapNotNull { id -> dbMap[id] ?: fallbackMap[id] }
    }

    /**
     * Fetches real scholarly Tafseer ONLINE for [surahNumber]:[ayahNumber] and [sourceId] in [language].
     * - Strictly auto-synced to global Settings [language] (no cross-language fallback).
     * - Fetches live from Quran.com API v4 (`api.quran.com/api/v4`) and `spa5k/tafsir_api` CDN (`qul.tarteel.ai` / `altafsir.com`).
     * - Holds fetched commentary in volatile session memory only (never bundles full Tafseer offline).
     * - Never displays Quran translations or fabricated summaries under a Tafseer heading.
     */
    suspend fun loadTafseerDetail(
        surahNumber: Int,
        ayahNumber: Int,
        sourceId: String,
        language: AppLanguage,
        forceRefresh: Boolean = false
    ): ResolvedTafseerDetail = withContext(Dispatchers.IO) {
        ensureSeeded()
        val edition = VerifiedTafseerCatalog.resolveOnlineEdition(sourceId, language)
        val sourceEntity = dao.getTafseerSourceById(edition.id)
            ?: VerifiedTafseerCatalog.toTafseerSourceEntities().first { it.id == edition.id }

        val asbabList = dao.getAsbabAlNuzulForAyah(surahNumber, ayahNumber)
        val benefitsList = dao.getBenefitsForAyah(surahNumber, ayahNumber)
        val relatedHadiths = QuranTafseerRepository.findVerifiedHadithsForAyah(surahNumber, ayahNumber)
        val unavailableAsbabMsg = QuranTafseerRepository.getUnavailableAsbabMessage(language)
        val bundledAsbab = QuranTarjumaAssetsLoader.getAsbabAlNuzul(context, surahNumber, ayahNumber)

        val chapterLangCode = when (language) {
            AppLanguage.ARABIC -> "ar"
            AppLanguage.URDU -> "ur"
            AppLanguage.ENGLISH -> "en"
            AppLanguage.HINDI -> null
        }
        val chapterCacheKey = chapterLangCode?.let { "${surahNumber}_$it" }
        val wahidiKey = "${surahNumber}_$ayahNumber"
        val tafseerCacheKey = "${edition.id}:$surahNumber:$ayahNumber"

        val chapterDeferred = if (chapterLangCode != null && chapterCacheKey != null && (forceRefresh || !onlineChapterInfoCache.containsKey(chapterCacheKey))) {
            async {
                QuranCatalogApiService.fetchQuranFoundationChapterInfo(
                    surahNumber = surahNumber,
                    languageCode = chapterLangCode
                ).getOrNull()?.also { info ->
                    onlineChapterInfoCache[chapterCacheKey] = info
                }
            }
        } else null

        val wahidiDeferred = if (language == AppLanguage.ENGLISH && (forceRefresh || !onlineWahidiAsbabCache.containsKey(wahidiKey))) {
            async {
                val res = QuranCatalogApiService.fetchSpa5kTafsirByAyah(
                    slug = "en-asbab-al-nuzul-by-al-wahidi",
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber
                ).getOrNull()?.text.orEmpty()
                onlineWahidiAsbabCache[wahidiKey] = res
                res
            }
        } else null

        val cachedPassage = if (!forceRefresh) onlineScholarTafseerCache[tafseerCacheKey] else null
        var fetchedPassage: OnlineCachedTafseerPassage? = cachedPassage
        var fetchError: String? = null

        if (fetchedPassage == null) {
            // 1. Primary Quran.com / Quran Foundation API v4 (returns full multi-page Tafseer + exact verse range)
            if (edition.quranFoundationId != null) {
                val qfRes = QuranCatalogApiService.fetchQuranFoundationTafsirByAyah(
                    slugOrId = edition.quranFoundationId,
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber
                )
                val qfData = qfRes.getOrNull()
                if (qfData != null && qfData.text.isNotBlank()) {
                    fetchedPassage = OnlineCachedTafseerPassage(
                        text = formatReadableTafseerParagraphs(qfData.text),
                        startAyah = qfData.startAyah,
                        endAyah = qfData.endAyah,
                        resolvedEndpointLabel = "Quran.com API v4"
                    )
                }
            }

            // 2. Fallback / Direct CDN via spa5k/tafsir_api (QUL & Al-Tafsir.com)
            if (fetchedPassage == null && edition.spa5kSlug != null) {
                val spaRes = QuranCatalogApiService.fetchSpa5kTafsirByAyah(
                    slug = edition.spa5kSlug,
                    surahNumber = surahNumber,
                    ayahNumber = ayahNumber,
                    fallbackSlug = edition.spa5kFallbackSlug
                )
                val spaData = spaRes.getOrNull()
                if (spaData != null && spaData.text.isNotBlank()) {
                    fetchedPassage = OnlineCachedTafseerPassage(
                        text = formatReadableTafseerParagraphs(spaData.text),
                        startAyah = ayahNumber,
                        endAyah = ayahNumber,
                        resolvedEndpointLabel = "spa5k tafsir_api CDN"
                    )
                } else {
                    fetchError = VerifiedTafseerCatalog.onlineFetchErrorMessage(language, edition.bookTitle)
                }
            } else if (fetchedPassage == null) {
                fetchError = VerifiedTafseerCatalog.onlineFetchErrorMessage(language, edition.bookTitle)
            }

            if (fetchedPassage != null) {
                onlineScholarTafseerCache[tafseerCacheKey] = fetchedPassage
            }
        }

        val chapterInfo = chapterDeferred?.await() ?: chapterCacheKey?.let { onlineChapterInfoCache[it] }
        val liveWahidiText = (wahidiDeferred?.await() ?: onlineWahidiAsbabCache[wahidiKey])?.takeIf { it.isNotBlank() }

        val historical = QuranTafseerRepository.buildVerifiedHistoricalPhase(
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            language = language,
            context = context,
            onlineChapterShortText = chapterInfo?.shortText
        )

        val asbabDisplayText = buildAsbabSectionText(
            language = language,
            asbabList = asbabList,
            bundledAsbab = bundledAsbab,
            liveWahidiEnglish = liveWahidiText,
            unavailableAsbabMsg = unavailableAsbabMsg
        )

        val rangeLabel = if (fetchedPassage != null && fetchedPassage.startAyah != fetchedPassage.endAyah) {
            when (language) {
                AppLanguage.URDU -> "مشترکہ تفسیر: آیات $surahNumber:${fetchedPassage.startAyah}–${fetchedPassage.endAyah}"
                AppLanguage.ARABIC -> "نطاق التفسير: الآيات $surahNumber:${fetchedPassage.startAyah}–${fetchedPassage.endAyah}"
                AppLanguage.HINDI -> "संयुक्त व्याख्या: आयत $surahNumber:${fetchedPassage.startAyah}–${fetchedPassage.endAyah}"
                AppLanguage.ENGLISH -> "Covers Ayahs $surahNumber:${fetchedPassage.startAyah}–${fetchedPassage.endAyah}"
            }
        } else {
            "$surahNumber:$ayahNumber"
        }

        ResolvedTafseerDetail(
            source = sourceEntity,
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            startAyah = fetchedPassage?.startAyah ?: ayahNumber,
            endAyah = fetchedPassage?.endAyah ?: ayahNumber,
            passageText = fetchedPassage?.text,
            passageLanguageNotice = if (fetchedPassage == null) fetchError else null,
            languageAvailabilityNote = VerifiedTafseerCatalog.languageDepthTransparencyNote(language),
            editionAttribution = edition.attribution,
            sourceUrl = edition.sourceUrl,
            volumeAndPage = "${edition.methodologyBadge} • $rangeLabel",
            historicalBackground = historical,
            asbabAlNuzulList = asbabList,
            asbabDisplayText = asbabDisplayText,
            unavailableAsbabMessage = unavailableAsbabMsg,
            benefitsList = benefitsList,
            relatedHadiths = relatedHadiths,
            isLoadingOnline = false,
            onlineError = if (fetchedPassage == null) fetchError else null
        )
    }

    private fun buildAsbabSectionText(
        language: AppLanguage,
        asbabList: List<AsbabAlNuzulEntity>,
        bundledAsbab: BundledAsbabEntry?,
        liveWahidiEnglish: String?,
        unavailableAsbabMsg: String
    ): String {
        return buildString {
            when (language) {
                AppLanguage.ENGLISH -> {
                    if (asbabList.isNotEmpty()) {
                        asbabList.forEachIndexed { idx, entry ->
                            if (idx > 0) append("\n\n")
                            append(entry.narration)
                            append("\n[Source: ${entry.sourceBook} • ${entry.hadithReference} • ${entry.authenticityGrade}]")
                        }
                    }
                    val wahidi = liveWahidiEnglish ?: bundledAsbab?.wahidiEnglish?.takeIf { it.isNotBlank() }
                    if (wahidi != null) {
                        if (isNotEmpty()) append("\n\n")
                        append(wahidi)
                        append("\n[Source: Asbab al-Nuzul — Imam al-Wahidi (d. 468 AH)]")
                    }
                }
                AppLanguage.ARABIC -> {
                    val sahihAr = bundledAsbab?.sahihArabic?.takeIf { it.isNotBlank() }
                    if (sahihAr != null) {
                        append(sahihAr)
                        append("\n[المصدر: الصحيح المسند من أسباب النزول]")
                    } else if (asbabList.isNotEmpty()) {
                        val refs = asbabList.joinToString(" • ") { "${it.sourceBook} (${it.hadithReference})" }
                        append("توجد رواية مسندة لسبب النزول موثقة في المراجع التالية:\n$refs")
                    }
                }
                AppLanguage.URDU -> {
                    if (asbabList.isNotEmpty()) {
                        val refs = asbabList.joinToString(" • ") { "${it.sourceBook} (${it.hadithReference})" }
                        append("اس آیت کے شانِ نزول سے متعلق صحیح احادیث کے تصدیق شدہ حوالہ جات:\n$refs")
                    }
                }
                AppLanguage.HINDI -> {
                    if (asbabList.isNotEmpty()) {
                        val refs = asbabList.joinToString(" • ") { "${it.sourceBook} (${it.hadithReference})" }
                        append("इस आयत के अवतरण से संबंधित प्रामाणिक हदीस संदर्भ:\n$refs")
                    }
                }
            }
            if (isBlank()) {
                append(unavailableAsbabMsg)
            }
        }
    }

    /**
     * Preserves all scholarly Tafseer wording cleanly across multiple paragraphs without trimming,
     * summarizing, or artificially padding any content.
     */
    private fun formatReadableTafseerParagraphs(raw: String): String {
        val normalized = raw.replace("\r\n", "\n").trim()
        if (normalized.contains("\n\n") || normalized.length < 480) return normalized
        val sb = StringBuilder(normalized.length + 32)
        var charsSinceBreak = 0
        var i = 0
        while (i < normalized.length) {
            val ch = normalized[i]
            sb.append(ch)
            charsSinceBreak++
            val isSentenceEnd = (ch == '.' || ch == '۔' || ch == '؟' || ch == '!') &&
                (i + 1 < normalized.length && normalized[i + 1] == ' ')
            if (isSentenceEnd && charsSinceBreak >= 380) {
                sb.append("\n\n")
                i++
                charsSinceBreak = 0
            }
            i++
        }
        return sb.toString().trim()
    }

    // Historical & Hadith verification helpers live in QuranTafseerRepository for single-source consistency.

    companion object {
        @Volatile
        private var INSTANCE: QuranTranslationTafseerRepository? = null

        fun getInstance(context: Context): QuranTranslationTafseerRepository {
            return INSTANCE ?: synchronized(this) {
                val appCtx = context.applicationContext
                val db = NoorDatabase.getInstance(appCtx)
                val prefs = appCtx.getSharedPreferences("noor_quran_catalog_prefs", Context.MODE_PRIVATE)
                val repo = QuranTranslationTafseerRepository(appCtx, db.tafseerDao(), prefs)
                INSTANCE = repo
                repo
            }
        }
    }
}
