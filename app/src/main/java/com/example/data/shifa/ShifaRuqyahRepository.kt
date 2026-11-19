package com.example.data.shifa

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppLanguage
import com.example.data.model.Ayah
import com.example.data.repository.QuranAudioManager
import com.example.data.repository.QuranData
import com.example.data.repository.QuranTarjumaAssetsLoader
import com.example.data.repository.QuranTranslationTafseerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

enum class ShifaSavedFilter {
    ALL,
    FAVORITES,
    BOOKMARKS
}

data class OnlineShifaTafseerCacheEntry(
    val text: String,
    val attribution: String
)

object ShifaRuqyahRepository {

    private const val PREFS_NAME = "shifa_ruqyah_prefs"
    private const val KEY_FAVORITES = "shifa_favorite_ids"
    private const val KEY_BOOKMARKS = "shifa_bookmark_ids"

    val allItems: List<ShifaRuqyahItem> =
        ShifaQuranAndRuqyahData.items + ShifaTibHadithData.items

    val conditionTopics: List<TreatmentConditionTopic> =
        ShifaTibHadithData.treatmentConditionTopics

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _bookmarkedIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedIds: StateFlow<Set<String>> = _bookmarkedIds.asStateFlow()

    private val onlineTafseerCache = ConcurrentHashMap<String, OnlineShifaTafseerCacheEntry>()
    private val resolvedCache = ConcurrentHashMap<String, ResolvedShifaEntry>()

    @Volatile
    private var prefsInitialized = false

    fun init(context: Context) {
        if (!prefsInitialized) {
            synchronized(this) {
                if (!prefsInitialized) {
                    val prefs = prefs(context)
                    _favoriteIds.value = prefs.getStringSet(KEY_FAVORITES, emptySet())?.toSet() ?: emptySet()
                    _bookmarkedIds.value = prefs.getStringSet(KEY_BOOKMARKS, emptySet())?.toSet() ?: emptySet()
                    prefsInitialized = true
                }
            }
        }
    }

    internal fun resetForTest(context: Context) {
        prefs(context).edit().clear().apply()
        _favoriteIds.value = emptySet()
        _bookmarkedIds.value = emptySet()
        onlineTafseerCache.clear()
        resolvedCache.clear()
        prefsInitialized = false
        init(context)
    }

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun toggleFavorite(context: Context, itemId: String) {
        init(context)
        val updated = _favoriteIds.value.toMutableSet()
        if (!updated.add(itemId)) updated.remove(itemId)
        _favoriteIds.value = updated
        prefs(context).edit().putStringSet(KEY_FAVORITES, updated).apply()
    }

    fun toggleBookmark(context: Context, itemId: String) {
        init(context)
        val updated = _bookmarkedIds.value.toMutableSet()
        if (!updated.add(itemId)) updated.remove(itemId)
        _bookmarkedIds.value = updated
        prefs(context).edit().putStringSet(KEY_BOOKMARKS, updated).apply()
    }

    fun allReferenceUrls(): Set<String> {
        val urls = linkedSetOf<String>()
        allItems.forEach { item ->
            urls.add(item.primaryReference.url)
            item.additionalReferences.forEach { urls.add(it.url) }
        }
        return urls
    }

    fun getItemsForCategory(category: ShifaCategory): List<ShifaRuqyahItem> {
        return when (category) {
            ShifaCategory.NAZAR_HASAD -> allItems.filter {
                it.category == ShifaCategory.NAZAR_HASAD ||
                    it.id in setOf("ruqyah_q_falaq", "ruqyah_q_naas", "ruqyah_dua_hasan_husayn", "ruqyah_dua_jibril")
            }
            ShifaCategory.WASWASE -> allItems.filter {
                it.category == ShifaCategory.WASWASE ||
                    it.id == "ruqyah_q_naas"
            }
            ShifaCategory.SIHR_PROTECTION -> allItems.filter {
                it.category == ShifaCategory.SIHR_PROTECTION ||
                    it.id in setOf("ruqyah_q_falaq", "ruqyah_q_naas", "ruqyah_q_kursi", "tib_ajwah_dates")
            }
            ShifaCategory.SHIFA_E_QALB -> allItems.filter {
                it.category == ShifaCategory.SHIFA_E_QALB ||
                    it.id in setOf("shifa_q_13_28", "shifa_q_2_153", "shifa_q_39_53", "shifa_q_3_173", "tib_talbina")
            }
            ShifaCategory.TREATMENT_INDEX -> emptyList()
            else -> allItems.filter { it.category == category }
        }
    }

    fun resolveEntry(
        context: Context,
        item: ShifaRuqyahItem,
        language: AppLanguage
    ): ResolvedShifaEntry {
        val cacheKey = "${item.id}_${language.code}"
        val onlineEntry = onlineTafseerCache[cacheKey]
        resolvedCache[cacheKey]?.let { cached ->
            return if (onlineEntry != null) {
                cached.copy(
                    onlineTafseerText = onlineEntry.text,
                    onlineTafseerAttribution = onlineEntry.attribution
                )
            } else cached
        }

        val resolved = if (item.surahNumber != null && item.ayahStart != null) {
            resolveQuranItem(context, item, language)
        } else {
            resolveHadithOrGuideItem(item, language)
        }
        resolvedCache[cacheKey] = resolved
        return if (onlineEntry != null) {
            resolved.copy(
                onlineTafseerText = onlineEntry.text,
                onlineTafseerAttribution = onlineEntry.attribution
            )
        } else resolved
    }

    private fun resolveQuranItem(
        context: Context,
        item: ShifaRuqyahItem,
        language: AppLanguage
    ): ResolvedShifaEntry {
        val surahNum = item.surahNumber!!
        val startAyah = item.ayahStart!!
        val endAyah = item.ayahEnd ?: startAyah

        val allSurahAyahs = QuranData.getAyahsForSurah(context, surahNum)
        val ayahs = allSurahAyahs.filter { it.ayahNumber in startAyah..endAyah }
        val surahMeta = QuranData.surahs.firstOrNull { it.number == surahNum }

        val surahDisplayName = when (language) {
            AppLanguage.ARABIC -> surahMeta?.nameArabic ?: "سورة $surahNum"
            AppLanguage.URDU -> surahMeta?.nameUrdu ?: "سورۃ $surahNum"
            AppLanguage.HINDI -> surahMeta?.nameHindi ?: "सूरह $surahNum"
            AppLanguage.ENGLISH -> surahMeta?.nameEnglish ?: "Surah $surahNum"
        }
        val ayahRangeStr = if (startAyah == endAyah) "$surahNum:$startAyah" else "$surahNum:$startAyah–$endAyah"
        val surahLabel = "$surahDisplayName ($ayahRangeStr)"

        val arabicText = if (ayahs.isNotEmpty()) {
            ayahs.joinToString(" ") { ayah ->
                "${ayah.textArabic.trim()} ﴿${toArabicIndicDigits(ayah.ayahNumber)}﴾"
            }
        } else {
            item.arabicText
        }

        val (translationText, translationAttribution) = buildQuranTranslation(
            context = context,
            surahNum = surahNum,
            ayahs = ayahs,
            language = language
        )

        val (offlineTafseer, offlineTafseerAttr) = buildOfflineTafseer(
            context = context,
            surahNum = surahNum,
            startAyah = startAyah,
            endAyah = endAyah,
            language = language
        )

        return ResolvedShifaEntry(
            item = item,
            arabicText = arabicText,
            surahLabel = surahLabel,
            translationText = translationText,
            translationAttribution = translationAttribution,
            offlineTafseerText = offlineTafseer,
            offlineTafseerAttribution = offlineTafseerAttr
        )
    }

    private fun buildQuranTranslation(
        context: Context,
        surahNum: Int,
        ayahs: List<Ayah>,
        language: AppLanguage
    ): Pair<String, String> {
        if (ayahs.isEmpty()) {
            return "" to ""
        }
        return when (language) {
            AppLanguage.ENGLISH -> {
                val lines = ayahs.mapNotNull { ayah ->
                    val text = ayah.translations.firstOrNull { it.language == AppLanguage.ENGLISH }?.translationText?.trim()
                        ?: QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, "english_rwwad", surahNum, ayah.ayahNumber)?.translation
                    text?.let { if (ayahs.size > 1) "(${ayah.ayahNumber}) $it" else it }
                }
                lines.joinToString("\n") to "Saheeh International (Offline Verified Quran Corpus)"
            }
            AppLanguage.URDU -> {
                val lines = ayahs.mapNotNull { ayah ->
                    val text = ayah.translations.firstOrNull { it.language == AppLanguage.URDU }?.translationText?.trim()
                    text?.let { if (ayahs.size > 1) "(${ayah.ayahNumber}) $it" else it }
                }
                lines.joinToString("\n") to "مولانا فتح محمد جالندھریؒ (مستند آف لائن ترجمہ)"
            }
            AppLanguage.HINDI -> {
                val lines = ayahs.mapNotNull { ayah ->
                    val text = ayah.translations.firstOrNull { it.language == AppLanguage.HINDI }?.translationText?.trim()
                        ?: QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, "hindi_omari", surahNum, ayah.ayahNumber)?.translation
                    text?.let { if (ayahs.size > 1) "(${ayah.ayahNumber}) $it" else it }
                }
                lines.joinToString("\n") to "मुहम्मद फ़ारूक़ ख़ान (प्रामाणिक ऑफ़लाइन हिन्दी अनुवाद)"
            }
            AppLanguage.ARABIC -> {
                val lines = ayahs.mapNotNull { ayah ->
                    val muyassar = QuranTarjumaAssetsLoader.getBundledQuranEncVerse(
                        context = context,
                        editionKey = "arabic_mokhtasar",
                        surahNumber = surahNum,
                        ayahNumber = ayah.ayahNumber
                    )?.translation?.trim()
                    val t = muyassar ?: ayah.translations.firstOrNull { it.language == AppLanguage.ARABIC }?.translationText?.trim()
                    t?.let { if (ayahs.size > 1) "﴿${toArabicIndicDigits(ayah.ayahNumber)}﴾ $it" else it }
                }
                lines.joinToString("\n") to "المختصر في تفسير القرآن الكريم (نص عربي ميسّر)"
            }
        }
    }

    private fun buildOfflineTafseer(
        context: Context,
        surahNum: Int,
        startAyah: Int,
        endAyah: Int,
        language: AppLanguage
    ): Pair<String?, String?> {
        // Strictly load ONLY verified Tafseer editions (NEVER substitute Quran translations!)
        val editionKey = when (language) {
            AppLanguage.ARABIC -> "arabic_mokhtasar"
            AppLanguage.ENGLISH -> "english_mokhtasar"
            AppLanguage.HINDI -> "hindi_mokhtasar"
            AppLanguage.URDU -> return null to null // Full Urdu Tafseer loaded on-demand via Ibn Kathir online
        }
        val chunks = (startAyah..endAyah).mapNotNull { ayahNum ->
            val verse = QuranTarjumaAssetsLoader.getBundledQuranEncVerse(
                context = context,
                editionKey = editionKey,
                surahNumber = surahNum,
                ayahNumber = ayahNum
            )
            verse?.translation?.takeIf { it.isNotBlank() }?.let { text ->
                if (startAyah != endAyah) "[$surahNum:$ayahNum] $text" else text
            }
        }
        if (chunks.isEmpty()) return null to null
        val attribution = when (language) {
            AppLanguage.ARABIC -> "المختصر في تفسير القرآن الكريم (تفسير موثق دون إنترنت)"
            AppLanguage.ENGLISH -> "Al-Mukhtasar fi Tafsir al-Quran (Offline Verified Commentary)"
            AppLanguage.HINDI -> "अल-मुख़्तसर फ़ी तफ़सीर अल-क़ुरआन (प्रमाणित ऑफ़लाइन तफ़सीर)"
            AppLanguage.URDU -> ""
        }
        return chunks.joinToString("\n\n") to attribution
    }

    private fun resolveHadithOrGuideItem(
        item: ShifaRuqyahItem,
        language: AppLanguage
    ): ResolvedShifaEntry {
        val translation = item.translation?.get(language).orEmpty()
        val attribution = buildString {
            if (item.narrator.isNotBlank()) {
                append(item.narrator)
                append(" • ")
            }
            append(item.primaryReference.bookAndNumber)
            append(" (")
            append(item.primaryReference.authenticityGrade)
            append(")")
        }
        return ResolvedShifaEntry(
            item = item,
            arabicText = item.arabicText,
            surahLabel = null,
            translationText = translation,
            translationAttribution = attribution,
            offlineTafseerText = null,
            offlineTafseerAttribution = null
        )
    }

    suspend fun loadOnlineScholarTafseer(
        context: Context,
        item: ShifaRuqyahItem,
        language: AppLanguage
    ): Result<OnlineShifaTafseerCacheEntry> = withContext(Dispatchers.IO) {
        val surahNum = item.surahNumber
            ?: return@withContext Result.failure(IllegalArgumentException("Online Tafseer applies to Quranic passages only."))
        val startAyah = item.ayahStart
            ?: return@withContext Result.failure(IllegalArgumentException("Missing ayah number."))
        val endAyah = item.ayahEnd ?: startAyah

        val cacheKey = "${item.id}_${language.code}"
        onlineTafseerCache[cacheKey]?.let { return@withContext Result.success(it) }

        val editionId = when (language) {
            AppLanguage.URDU -> "ur_ibn_kathir"
            AppLanguage.ENGLISH -> "en_ibn_kathir"
            AppLanguage.HINDI -> "hi_al_mukhtasar"
            AppLanguage.ARABIC -> "ar_ibn_kathir"
        }

        val repo = QuranTranslationTafseerRepository.getInstance(context)
        val combinedParagraphs = mutableListOf<String>()
        var attribution = ""
        var lastError: String? = null

        for (ayahNum in startAyah..endAyah) {
            val detail = repo.loadTafseerDetail(
                surahNumber = surahNum,
                ayahNumber = ayahNum,
                sourceId = editionId,
                language = language
            )
            val txt = detail.passageText?.trim()
            if (!txt.isNullOrBlank()) {
                val prefix = if (startAyah != endAyah) "($surahNum:$ayahNum) " else ""
                combinedParagraphs.add("$prefix$txt")
                attribution = detail.editionAttribution
            } else if (!detail.onlineError.isNullOrBlank()) {
                lastError = detail.onlineError
            }
        }

        if (combinedParagraphs.isEmpty()) {
            Result.failure(Exception(lastError ?: "Unable to load online scholarly Tafseer right now."))
        } else {
            val entry = OnlineShifaTafseerCacheEntry(
                text = combinedParagraphs.joinToString("\n\n"),
                attribution = attribution
            )
            onlineTafseerCache[cacheKey] = entry
            Result.success(entry)
        }
    }

    fun matchConditionTopics(query: String): List<TreatmentConditionTopic> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isBlank()) return conditionTopics
        return conditionTopics.filter { topic ->
            topic.title.en.lowercase().contains(trimmed) ||
                topic.title.ur.lowercase().contains(trimmed) ||
                topic.title.ar.lowercase().contains(trimmed) ||
                topic.title.hi.lowercase().contains(trimmed) ||
                topic.searchAliases.any { alias ->
                    val a = alias.lowercase()
                    a.contains(trimmed) || trimmed.contains(a)
                }
        }
    }

    fun searchEntries(
        context: Context,
        language: AppLanguage,
        query: String,
        category: ShifaCategory,
        savedFilter: ShifaSavedFilter
    ): List<ResolvedShifaEntry> {
        init(context)
        val favSet = _favoriteIds.value
        val bmSet = _bookmarkedIds.value
        val q = query.trim().lowercase()

        val baseItems: List<ShifaRuqyahItem> = when {
            category == ShifaCategory.TREATMENT_INDEX && q.isNotBlank() -> {
                val matchedTopics = matchConditionTopics(q)
                val topicIds = matchedTopics.flatMap { it.matchedItemIds }.toSet()
                allItems.filter { item ->
                    item.id in topicIds || matchesItemText(item, q)
                }
            }
            category == ShifaCategory.TREATMENT_INDEX -> emptyList()
            else -> getItemsForCategory(category)
        }

        val savedFiltered = when (savedFilter) {
            ShifaSavedFilter.ALL -> baseItems
            ShifaSavedFilter.FAVORITES -> baseItems.filter { it.id in favSet }
            ShifaSavedFilter.BOOKMARKS -> baseItems.filter { it.id in bmSet }
        }

        val queryFiltered = if (q.isBlank() || category == ShifaCategory.TREATMENT_INDEX) {
            savedFiltered
        } else {
            val topicMatchedIds = matchConditionTopics(q).flatMap { it.matchedItemIds }.toSet()
            savedFiltered.filter { item ->
                item.id in topicMatchedIds || matchesItemText(item, q)
            }
        }

        return queryFiltered.map { resolveEntry(context, it, language) }
    }

    fun isUnmappedConditionQuery(
        context: Context,
        language: AppLanguage,
        query: String
    ): Boolean {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return false
        val topics = matchConditionTopics(trimmed)
        if (topics.isNotEmpty()) return false
        val anyItemMatch = allItems.any { matchesItemText(it, trimmed.lowercase()) }
        return !anyItemMatch
    }

    private fun matchesItemText(item: ShifaRuqyahItem, qLower: String): Boolean {
        return item.title.en.lowercase().contains(qLower) ||
            item.title.ur.lowercase().contains(qLower) ||
            item.title.ar.lowercase().contains(qLower) ||
            item.title.hi.lowercase().contains(qLower) ||
            item.topicTag.en.lowercase().contains(qLower) ||
            item.topicTag.ur.lowercase().contains(qLower) ||
            item.topicTag.hi.lowercase().contains(qLower) ||
            item.islamicContext.en.lowercase().contains(qLower) ||
            item.islamicContext.ur.lowercase().contains(qLower) ||
            item.islamicContext.hi.lowercase().contains(qLower) ||
            item.primaryReference.bookAndNumber.lowercase().contains(qLower) ||
            item.searchKeywords.any { it.lowercase().contains(qLower) } ||
            (item.translation?.en?.lowercase()?.contains(qLower) == true) ||
            (item.translation?.ur?.lowercase()?.contains(qLower) == true) ||
            (item.translation?.hi?.lowercase()?.contains(qLower) == true)
    }

    fun toggleAudioPlayback(context: Context, item: ShifaRuqyahItem): String? {
        if (!item.supportsQuranAudio || item.surahNumber == null || item.ayahStart == null) {
            return "Quran recitation audio applies to Quranic passages; Hadith narrations are text-verified."
        }
        val activeSurah = QuranAudioManager.currentPlayingSurahNumber.value
        val activeAyah = QuranAudioManager.currentPlayingAyahNumber.value
        val isActive = (QuranAudioManager.isAudioPlaying.value || QuranAudioManager.isBuffering.value) &&
            activeSurah == item.surahNumber && activeAyah == item.ayahStart
        if (isActive) {
            QuranAudioManager.stopMedia()
        } else {
            QuranAudioManager.playAyah(
                context = context,
                surahNumber = item.surahNumber,
                ayahNumber = item.ayahStart,
                repeatTarget = 1
            )
        }
        return null
    }

    private fun toArabicIndicDigits(num: Int): String {
        val digits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return num.toString().map { ch ->
            if (ch in '0'..'9') digits[ch - '0'] else ch
        }.joinToString("")
    }
}
