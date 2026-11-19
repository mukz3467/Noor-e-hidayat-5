package com.example.data.prophets

import android.content.Context
import com.example.data.model.AppLanguage
import com.example.data.model.Ayah
import com.example.data.repository.QuranData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ResolvedQuranAyahDisplay(
    val surah: Int,
    val ayah: Int,
    val arabicText: String,
    val translationText: String,
    val translatorName: String
)

data class ProphetProgressState(
    val readIds: Set<String> = emptySet(),
    val bookmarkedIds: Set<String> = emptySet(),
    val quizCompletedIds: Set<String> = emptySet(),
    val lastReadId: String? = null,
    val summaryModeEnabled: Boolean = false
)

object ProphetRepository {

    private const val PREFS_NAME = "stories_prophets_prefs"
    private const val KEY_READ_IDS = "read_prophet_ids"
    private const val KEY_BOOKMARK_IDS = "bookmark_prophet_ids"
    private const val KEY_QUIZ_IDS = "quiz_completed_ids"
    private const val KEY_LAST_READ_ID = "last_read_prophet_id"
    private const val KEY_SUMMARY_MODE = "summary_mode_enabled"

    val allStories: List<ProphetStory> by lazy {
        (PROPHET_CATALOG_PART1 +
            PROPHET_CATALOG_PART2 +
            PROPHET_CATALOG_PART3 +
            PROPHET_CATALOG_PART4)
            .sortedBy { it.orderNumber }
    }

    private val _progress = MutableStateFlow(ProphetProgressState())
    val progress: StateFlow<ProphetProgressState> = _progress.asStateFlow()

    @Volatile
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        initialized = true
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _progress.value = ProphetProgressState(
            readIds = prefs.getStringSet(KEY_READ_IDS, emptySet())?.toSet() ?: emptySet(),
            bookmarkedIds = prefs.getStringSet(KEY_BOOKMARK_IDS, emptySet())?.toSet() ?: emptySet(),
            quizCompletedIds = prefs.getStringSet(KEY_QUIZ_IDS, emptySet())?.toSet() ?: emptySet(),
            lastReadId = prefs.getString(KEY_LAST_READ_ID, null),
            summaryModeEnabled = prefs.getBoolean(KEY_SUMMARY_MODE, false)
        )
    }

    internal fun resetForTest(context: Context) {
        initialized = false
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        _progress.value = ProphetProgressState()
    }

    fun getStoryById(id: String?): ProphetStory? =
        if (id.isNullOrBlank()) null else allStories.firstOrNull { it.id == id }

    fun getAdjacentIds(currentId: String): Pair<String?, String?> {
        val idx = allStories.indexOfFirst { it.id == currentId }
        if (idx < 0) return null to null
        val prev = allStories.getOrNull(idx - 1)?.id
        val next = allStories.getOrNull(idx + 1)?.id
        return prev to next
    }

    fun searchStories(
        query: String,
        categoryFilter: ProphetCategory?,
        bookmarkedOnly: Boolean,
        language: AppLanguage
    ): List<ProphetStory> {
        val q = query.trim().lowercase()
        val bookmarks = _progress.value.bookmarkedIds
        return allStories.filter { story ->
            if (categoryFilter != null && story.category != categoryFilter) return@filter false
            if (bookmarkedOnly && !bookmarks.contains(story.id)) return@filter false
            if (q.isEmpty()) return@filter true

            val nameMatch = story.names.values.any { it.lowercase().contains(q) } ||
                story.arabicName.lowercase().contains(q) ||
                story.id.lowercase().contains(q)
            val titleMatch = story.titles.values.any { it.lowercase().contains(q) }
            val introMatch = (story.summaryIntro[language] ?: "").lowercase().contains(q)
            val refMatch = story.verseRefs.any { ref ->
                ref.referenceLabel.lowercase().contains(q) ||
                    "${ref.surah}:${ref.startAyah}".contains(q)
            }
            nameMatch || titleMatch || introMatch || refMatch
        }
    }

    fun markOpened(context: Context, prophetId: String) {
        init(context)
        val cur = _progress.value
        val updatedRead = cur.readIds + prophetId
        val next = cur.copy(readIds = updatedRead, lastReadId = prophetId)
        _progress.value = next
        saveProgress(context, next)
    }

    fun toggleReadStatus(context: Context, prophetId: String) {
        init(context)
        val cur = _progress.value
        val updatedRead = if (cur.readIds.contains(prophetId)) cur.readIds - prophetId else cur.readIds + prophetId
        val next = cur.copy(readIds = updatedRead)
        _progress.value = next
        saveProgress(context, next)
    }

    fun toggleBookmark(context: Context, prophetId: String) {
        init(context)
        val cur = _progress.value
        val updatedBookmarks = if (cur.bookmarkedIds.contains(prophetId)) {
            cur.bookmarkedIds - prophetId
        } else {
            cur.bookmarkedIds + prophetId
        }
        val next = cur.copy(bookmarkedIds = updatedBookmarks)
        _progress.value = next
        saveProgress(context, next)
    }

    fun markQuizCompleted(context: Context, prophetId: String) {
        init(context)
        val cur = _progress.value
        val next = cur.copy(quizCompletedIds = cur.quizCompletedIds + prophetId)
        _progress.value = next
        saveProgress(context, next)
    }

    fun setSummaryMode(context: Context, summaryOnly: Boolean) {
        init(context)
        val next = _progress.value.copy(summaryModeEnabled = summaryOnly)
        _progress.value = next
        saveProgress(context, next)
    }

    /**
     * Resolves verbatim Tanzil Uthmani Arabic and bundled verified human translations
     * (`en.sahih`, `ur.jalandhry`, `hi.farooq`) directly from `QuranData` offline assets.
     */
    fun resolveQuranVerses(
        context: Context,
        surah: Int,
        startAyah: Int,
        endAyah: Int,
        language: AppLanguage
    ): List<ResolvedQuranAyahDisplay> {
        val ayahs: List<Ayah> = QuranData.getAyahsForSurah(context, surah)
        if (ayahs.isEmpty()) return emptyList()

        return ayahs.filter { it.ayahNumber in startAyah..endAyah }.map { ayah ->
            val targetTranslation = when (language) {
                AppLanguage.ARABIC -> null
                AppLanguage.ENGLISH, AppLanguage.URDU, AppLanguage.HINDI ->
                    ayah.translations.firstOrNull { it.language == language }
                        ?: ayah.translations.firstOrNull { it.language == AppLanguage.ENGLISH }
            }
            ResolvedQuranAyahDisplay(
                surah = ayah.surahNumber,
                ayah = ayah.ayahNumber,
                arabicText = ayah.textArabic,
                translationText = targetTranslation?.translationText.orEmpty(),
                translatorName = targetTranslation?.translatorName.orEmpty()
            )
        }
    }

    fun buildShareText(story: ProphetStory, language: AppLanguage): String {
        val refs = story.verseRefs.joinToString(", ") { "Quran ${it.referenceLabel}" }
        val hadiths = if (story.hadithRefs.isNotEmpty()) {
            "\nHadith: ${story.hadithRefs.joinToString(", ") { it.citation }}"
        } else {
            ""
        }
        return buildString {
            append("${story.orderNumber}. ${story.localizedName(language)} — ${story.localizedTitle(language)}\n\n")
            append(story.summaryIntro[language] ?: story.summaryIntro[AppLanguage.ENGLISH].orEmpty())
            append("\n\nReferences: $refs$hadiths\n(Noor-e-Hidayat — Offline Stories of the Prophets)")
        }
    }

    private fun saveProgress(context: Context, state: ProphetProgressState) {
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_READ_IDS, state.readIds)
            .putStringSet(KEY_BOOKMARK_IDS, state.bookmarkedIds)
            .putStringSet(KEY_QUIZ_IDS, state.quizCompletedIds)
            .putString(KEY_LAST_READ_ID, state.lastReadId)
            .putBoolean(KEY_SUMMARY_MODE, state.summaryModeEnabled)
            .apply()
    }
}
