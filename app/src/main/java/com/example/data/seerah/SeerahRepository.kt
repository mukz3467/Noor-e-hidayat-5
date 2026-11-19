package com.example.data.seerah

import android.content.Context
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

object SeerahRepository {

    private const val PREFS_NAME = "seerah_progress_prefs"
    private const val KEY_READ_IDS = "seerah_read_ids"
    private const val KEY_BOOKMARK_IDS = "seerah_bookmark_ids"
    private const val KEY_LAST_READ_ID = "seerah_last_read_id"

    val allArticles: List<SeerahArticle> by lazy {
        (SeerahCatalogPart1.articles +
            SeerahCatalogPart2.articles +
            SeerahCatalogPart3a.articles +
            SeerahCatalogPart3b.articles +
            SeerahCatalogPart3c.articles +
            SeerahCatalogPart4a.articles +
            SeerahCatalogPart4b.articles).sortedBy { it.orderIndex }
    }

    private val _progress = MutableStateFlow(SeerahProgressState())
    val progress: StateFlow<SeerahProgressState> = _progress.asStateFlow()

    @Volatile
    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        isInitialized = true
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val reads = prefs.getStringSet(KEY_READ_IDS, emptySet())?.toSet() ?: emptySet()
        val bookmarks = prefs.getStringSet(KEY_BOOKMARK_IDS, emptySet())?.toSet() ?: emptySet()
        val lastId = prefs.getString(KEY_LAST_READ_ID, null)?.takeIf { id ->
            allArticles.any { it.id == id }
        }
        _progress.value = SeerahProgressState(
            readArticleIds = reads,
            bookmarkedArticleIds = bookmarks,
            lastReadArticleId = lastId
        )
    }

    internal fun resetForTest(context: Context? = null) {
        isInitialized = false
        _progress.value = SeerahProgressState()
        context?.applicationContext
            ?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            ?.edit()
            ?.clear()
            ?.apply()
    }

    fun getArticleById(id: String?): SeerahArticle? =
        if (id == null) null else allArticles.firstOrNull { it.id == id }

    fun getArticlesForEra(era: SeerahEra): List<SeerahArticle> =
        allArticles.filter { it.era == era }

    fun searchArticles(
        query: String,
        eraFilter: SeerahEra? = null,
        bookmarkedOnly: Boolean = false,
        language: AppLanguage = AppLanguage.ENGLISH
    ): List<SeerahArticle> {
        val q = query.trim().lowercase(Locale.ROOT)
        val bookmarks = _progress.value.bookmarkedArticleIds
        return allArticles.filter { article ->
            val eraOk = eraFilter == null || article.era == eraFilter
            val bmOk = !bookmarkedOnly || bookmarks.contains(article.id)
            if (!eraOk || !bmOk) return@filter false
            if (q.isBlank()) return@filter true

            val titleMatch = article.title.values.any { it.lowercase(Locale.ROOT).contains(q) }
            val overviewMatch = article.overview.values.any { it.lowercase(Locale.ROOT).contains(q) }
            val keywordMatch = article.keywords.any { it.lowercase(Locale.ROOT).contains(q) }
            val citationMatch = article.citations.any { c ->
                c.sourceName.lowercase(Locale.ROOT).contains(q) ||
                    c.referenceCode.lowercase(Locale.ROOT).contains(q) ||
                    c.quoteTranslation.values.any { it.lowercase(Locale.ROOT).contains(q) }
            }
            val eraTitleMatch = article.era.title(language).lowercase(Locale.ROOT).contains(q)
            titleMatch || overviewMatch || keywordMatch || citationMatch || eraTitleMatch
        }
    }

    fun markArticleOpened(context: Context, articleId: String) {
        init(context)
        val cur = _progress.value
        val updatedReads = cur.readArticleIds + articleId
        val nextState = cur.copy(
            readArticleIds = updatedReads,
            lastReadArticleId = articleId
        )
        _progress.value = nextState
        persist(context, nextState)
    }

    fun toggleReadStatus(context: Context, articleId: String) {
        init(context)
        val cur = _progress.value
        val updatedReads = if (cur.readArticleIds.contains(articleId)) {
            cur.readArticleIds - articleId
        } else {
            cur.readArticleIds + articleId
        }
        val nextState = cur.copy(readArticleIds = updatedReads)
        _progress.value = nextState
        persist(context, nextState)
    }

    fun toggleBookmark(context: Context, articleId: String) {
        init(context)
        val cur = _progress.value
        val updated = if (cur.bookmarkedArticleIds.contains(articleId)) {
            cur.bookmarkedArticleIds - articleId
        } else {
            cur.bookmarkedArticleIds + articleId
        }
        val nextState = cur.copy(bookmarkedArticleIds = updated)
        _progress.value = nextState
        persist(context, nextState)
    }

    fun previousArticle(articleId: String): SeerahArticle? {
        val idx = allArticles.indexOfFirst { it.id == articleId }
        return if (idx > 0) allArticles[idx - 1] else null
    }

    fun nextArticle(articleId: String): SeerahArticle? {
        val idx = allArticles.indexOfFirst { it.id == articleId }
        return if (idx in 0 until allArticles.lastIndex) allArticles[idx + 1] else null
    }

    fun buildShareText(article: SeerahArticle, language: AppLanguage): String {
        val title = article.textFor(article.title, language)
        val era = article.era.title(language)
        val chrono = article.textFor(article.approxChronology, language)
        val overview = article.textFor(article.overview, language)
        val lessons = article.textFor(article.lessonsToday, language)
        val refs = article.citations.joinToString("\n") {
            "• [${it.tier.shortBadge(language)}] ${it.sourceName} (${it.referenceCode})"
        }
        return "$title\n$era — $chrono\n\n$overview\n\n$lessons\n\n$refs\n\n— Noor-e-Hidayat"
    }

    private fun persist(context: Context, state: SeerahProgressState) {
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_READ_IDS, state.readArticleIds)
            .putStringSet(KEY_BOOKMARK_IDS, state.bookmarkedArticleIds)
            .putString(KEY_LAST_READ_ID, state.lastReadArticleId)
            .apply()
    }
}
