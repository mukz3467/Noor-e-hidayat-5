package com.example.ui.viewmodel

import android.content.Context
import com.example.data.podcast.LanguageSuggestionsLoadResult
import com.example.data.podcast.PodcastCatalogBridge
import com.example.data.podcast.PodcastLanguage
import com.example.data.podcast.PodcastLibraryRepository
import com.example.data.podcast.PodcastProfileCategory
import com.example.data.podcast.PodcastScholarProfile
import com.example.data.podcast.PodcastScholarRegistry
import com.example.data.podcast.ScholarPodcastLoadResult
import com.example.data.podcast.VerifiedPodcastVideo
import com.example.data.repository.YouTubeRepository
import com.example.data.repository.YouTubeVideoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class PodcastLibraryTab {
    LANGUAGE_LIBRARY,
    FAVORITES,
    RECENTLY_WATCHED
}

enum class PodcastLanguageSubView {
    SUGGESTED_VIDEOS,
    SCHOLARS_DIRECTORY
}

data class PodcastsUiState(
    val libraryTab: PodcastLibraryTab = PodcastLibraryTab.LANGUAGE_LIBRARY,
    val selectedLanguage: PodcastLanguage? = null,
    val languageSubView: PodcastLanguageSubView = PodcastLanguageSubView.SUGGESTED_VIDEOS,
    val selectedScholar: PodcastScholarProfile? = null,
    val selectedCategory: PodcastProfileCategory = PodcastProfileCategory.ALL_ELIGIBLE,
    val searchQuery: String = "",
    val visiblePageSize: Int = PodcastsViewModel.PAGE_STEP,
    val totalCatalogChannels: Int = 0,
    val languageChannelCount: Int = 0,
    val languageSuggestionsResult: LanguageSuggestionsLoadResult? = null,
    val scholarResult: ScholarPodcastLoadResult? = null,
    val cachedCountsByScholarId: Map<String, Int> = emptyMap(),
    val activeVideo: VerifiedPodcastVideo? = null,
    val activeLegacyVideo: YouTubeVideoItem? = null,
    val favoritePodcasts: List<YouTubeVideoItem> = emptyList(),
    val recentPodcasts: List<YouTubeVideoItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false
) {
    val activePlayableVideoId: String?
        get() = activeVideo?.videoId ?: activeLegacyVideo?.id

    val activePlayableTitle: String?
        get() = activeVideo?.title ?: activeLegacyVideo?.title

    val activePlayableChannel: String?
        get() = activeVideo?.channelName ?: activeLegacyVideo?.channel

    val activePlayableUrl: String?
        get() = activeVideo?.videoUrl ?: activeLegacyVideo?.videoUrl

    val nonEmptyScholarCategories: List<Pair<PodcastProfileCategory, Int>>
        get() = scholarResult?.nonEmptyCategories().orEmpty()

    val nonEmptyLanguageCategories: List<Pair<PodcastProfileCategory, Int>>
        get() = languageSuggestionsResult?.nonEmptyCategories().orEmpty()

    val filteredScholarVideos: List<VerifiedPodcastVideo>
        get() {
            val res = scholarResult ?: return emptyList()
            val byCategory = res.videosForCategory(selectedCategory)
            return PodcastLibraryRepository.searchVerifiedVideos(byCategory, searchQuery, res.scholar)
        }

    val paginatedScholarVideos: List<VerifiedPodcastVideo>
        get() = filteredScholarVideos.take(visiblePageSize)

    val canLoadMoreScholarVideos: Boolean
        get() = filteredScholarVideos.size > visiblePageSize ||
            (scholarResult?.hasMoreRemotePages == true && searchQuery.isBlank())

    val filteredLanguageVideos: List<VerifiedPodcastVideo>
        get() {
            val res = languageSuggestionsResult ?: return emptyList()
            val byCategory = res.videosForCategory(selectedCategory)
            return PodcastLibraryRepository.searchVerifiedVideos(byCategory, searchQuery, profile = null)
                .filter { it.language == res.language }
        }

    val paginatedLanguageVideos: List<VerifiedPodcastVideo>
        get() = filteredLanguageVideos.take(visiblePageSize)

    val canLoadMoreLanguageVideos: Boolean
        get() = filteredLanguageVideos.size > visiblePageSize ||
            (languageSuggestionsResult?.hasMoreChannelBatches == true && searchQuery.isBlank())
}

/**
 * State controller for the Noor-e-Hidayat Multilingual Podcast Library (350+ verified channels).
 *
 * - Keeps Urdu, English, and Arabic strictly separated (zero mixed feeds on open).
 * - Delivers 100+ real verified long-form video suggestions per language from 110+ channels each.
 * - Provides dedicated Scholar & Channel profiles with 13 dynamic non-empty categories.
 * - Handles foreground ~30-minute staleness refresh while keeping existing verified videos visible.
 */
class PodcastsViewModel(
    private val appContext: Context,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) {
    private val _uiState = MutableStateFlow(
        PodcastsUiState(
            totalCatalogChannels = PodcastCatalogBridge.totalVerifiedChannelCount(appContext),
            favoritePodcasts = YouTubeRepository.getFavoritePodcasts(appContext),
            recentPodcasts = YouTubeRepository.getRecentlyWatchedPodcasts(appContext)
        )
    )
    val uiState: StateFlow<PodcastsUiState> = _uiState.asStateFlow()

    private var activeLoadJob: Job? = null
    private var foregroundTickerJob: Job? = null

    fun startForegroundRefreshTicker() {
        if (foregroundTickerJob?.isActive == true) return
        foregroundTickerJob = scope.launch {
            while (isActive) {
                delay(PodcastLibraryRepository.STALE_CACHE_MS)
                refreshIfStale()
            }
        }
    }

    fun stopForegroundRefreshTicker() {
        foregroundTickerJob?.cancel()
        foregroundTickerJob = null
    }

    fun selectLibraryTab(tab: PodcastLibraryTab) {
        _uiState.update {
            it.copy(
                libraryTab = tab,
                favoritePodcasts = YouTubeRepository.getFavoritePodcasts(appContext),
                recentPodcasts = YouTubeRepository.getRecentlyWatchedPodcasts(appContext)
            )
        }
    }

    fun selectLanguageSubView(subView: PodcastLanguageSubView) {
        val state = _uiState.value
        _uiState.update {
            it.copy(
                languageSubView = subView,
                selectedScholar = null,
                scholarResult = null,
                selectedCategory = PodcastProfileCategory.ALL_ELIGIBLE,
                searchQuery = "",
                visiblePageSize = PAGE_STEP
            )
        }
        val lang = state.selectedLanguage ?: return
        if (subView == PodcastLanguageSubView.SUGGESTED_VIDEOS &&
            (state.languageSuggestionsResult == null || state.languageSuggestionsResult.language != lang)
        ) {
            loadSuggestionsForLanguage(lang, forceRefresh = false)
        }
    }

    fun selectLanguage(language: PodcastLanguage) {
        val current = _uiState.value.selectedLanguage
        if (current == language && _uiState.value.selectedScholar == null) {
            activeLoadJob?.cancel()
            PodcastLibraryRepository.saveLastSelectedLanguage(appContext, null)
            _uiState.update {
                it.copy(
                    selectedLanguage = null,
                    selectedScholar = null,
                    scholarResult = null,
                    languageSuggestionsResult = null,
                    languageChannelCount = 0,
                    selectedCategory = PodcastProfileCategory.ALL_ELIGIBLE,
                    searchQuery = "",
                    isLoading = false,
                    isRefreshing = false
                )
            }
            return
        }
        activeLoadJob?.cancel()
        PodcastLibraryRepository.saveLastSelectedLanguage(appContext, language)
        val counts = PodcastLibraryRepository.getCachedCountsForLanguage(appContext, language)
        val langChannelCount = PodcastCatalogBridge.verifiedChannelCountForLanguage(appContext, language)
        _uiState.update {
            it.copy(
                libraryTab = PodcastLibraryTab.LANGUAGE_LIBRARY,
                selectedLanguage = language,
                languageSubView = PodcastLanguageSubView.SUGGESTED_VIDEOS,
                selectedScholar = null,
                scholarResult = null,
                languageSuggestionsResult = null,
                languageChannelCount = langChannelCount,
                selectedCategory = PodcastProfileCategory.ALL_ELIGIBLE,
                searchQuery = "",
                visiblePageSize = PAGE_STEP,
                cachedCountsByScholarId = counts,
                isLoading = true,
                isRefreshing = false,
                isLoadingMore = false
            )
        }
        loadSuggestionsForLanguage(language, forceRefresh = false)
    }

    private fun loadSuggestionsForLanguage(
        language: PodcastLanguage,
        forceRefresh: Boolean,
        loadNextBatch: Boolean = false
    ) {
        activeLoadJob?.cancel()
        activeLoadJob = scope.launch {
            var result = PodcastLibraryRepository.loadLanguageSuggestions(
                context = appContext,
                language = language,
                forceRefresh = forceRefresh,
                loadNextBatch = loadNextBatch
            )
            applyLanguageSuggestionsResult(result, expandPage = loadNextBatch)

            // Progressively expand across remaining verified channels in this language (114-120 channels)
            // so users receive 400+ real long-form videos per category / 1,000+ per language automatically
            // without blocking initial display.
            if (!loadNextBatch &&
                result.videos.isNotEmpty() &&
                result.verifiedVideoCount < TARGET_AUTO_EXPAND_VIDEOS_PER_LANG &&
                result.hasMoreChannelBatches &&
                PodcastLibraryRepository.isOnline(appContext)
            ) {
                var extraBatches = 0
                while (
                    extraBatches < MAX_AUTO_EXPAND_BATCHES &&
                    result.verifiedVideoCount < TARGET_AUTO_EXPAND_VIDEOS_PER_LANG &&
                    result.hasMoreChannelBatches
                ) {
                    val prevCount = result.verifiedVideoCount
                    result = PodcastLibraryRepository.loadLanguageSuggestions(
                        context = appContext,
                        language = language,
                        forceRefresh = false,
                        loadNextBatch = true
                    )
                    applyLanguageSuggestionsResult(result, expandPage = false)
                    extraBatches++
                    if (result.verifiedVideoCount <= prevCount) break
                }
            }
        }
    }

    fun openScholarProfile(scholar: PodcastScholarProfile, forceRefresh: Boolean = false) {
        activeLoadJob?.cancel()
        _uiState.update {
            it.copy(
                libraryTab = PodcastLibraryTab.LANGUAGE_LIBRARY,
                selectedLanguage = scholar.language,
                selectedScholar = scholar,
                selectedCategory = PodcastProfileCategory.ALL_ELIGIBLE,
                searchQuery = "",
                visiblePageSize = PAGE_STEP,
                scholarResult = null,
                isLoading = true,
                isRefreshing = false,
                isLoadingMore = false
            )
        }
        activeLoadJob = scope.launch {
            val result = PodcastLibraryRepository.loadScholarProfile(
                context = appContext,
                scholar = scholar,
                forceRefresh = forceRefresh,
                loadNextPage = false
            )
            applyScholarResult(result)
        }
    }

    fun closeScholarProfile() {
        activeLoadJob?.cancel()
        val lang = _uiState.value.selectedLanguage
        val counts = if (lang != null) {
            PodcastLibraryRepository.getCachedCountsForLanguage(appContext, lang)
        } else {
            emptyMap()
        }
        _uiState.update {
            it.copy(
                selectedScholar = null,
                scholarResult = null,
                selectedCategory = PodcastProfileCategory.ALL_ELIGIBLE,
                searchQuery = "",
                visiblePageSize = PAGE_STEP,
                cachedCountsByScholarId = counts,
                isLoading = false,
                isRefreshing = false,
                isLoadingMore = false
            )
        }
    }

    fun selectCategory(category: PodcastProfileCategory) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                visiblePageSize = PAGE_STEP
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                visiblePageSize = PAGE_STEP
            )
        }
    }

    fun refreshActiveView() {
        val state = _uiState.value
        val scholar = state.selectedScholar
        if (scholar != null) {
            activeLoadJob?.cancel()
            _uiState.update { it.copy(isRefreshing = true) }
            activeLoadJob = scope.launch {
                val result = PodcastLibraryRepository.loadScholarProfile(
                    context = appContext,
                    scholar = scholar,
                    forceRefresh = true,
                    loadNextPage = false
                )
                applyScholarResult(result)
            }
            return
        }
        val lang = state.selectedLanguage ?: return
        _uiState.update { it.copy(isRefreshing = true) }
        loadSuggestionsForLanguage(lang, forceRefresh = true, loadNextBatch = false)
    }

    fun refreshIfStale() {
        if (!PodcastLibraryRepository.isOnline(appContext)) return
        val state = _uiState.value
        val scholar = state.selectedScholar
        if (scholar != null) {
            if (PodcastLibraryRepository.isCacheStale(appContext, scholar.id)) {
                refreshActiveView()
            }
            return
        }
        val lang = state.selectedLanguage ?: return
        if (state.languageSubView == PodcastLanguageSubView.SUGGESTED_VIDEOS &&
            PodcastLibraryRepository.isLanguageSuggestionsStale(appContext, lang)
        ) {
            refreshActiveView()
        }
    }

    fun loadMoreVideos() {
        val state = _uiState.value
        val scholar = state.selectedScholar
        if (scholar != null) {
            if (state.filteredScholarVideos.size > state.visiblePageSize) {
                _uiState.update { it.copy(visiblePageSize = it.visiblePageSize + PAGE_STEP) }
                return
            }
            if (state.scholarResult?.hasMoreRemotePages == true && !state.isLoadingMore) {
                _uiState.update { it.copy(isLoadingMore = true) }
                activeLoadJob = scope.launch {
                    val result = PodcastLibraryRepository.loadScholarProfile(
                        context = appContext,
                        scholar = scholar,
                        forceRefresh = false,
                        loadNextPage = true
                    )
                    applyScholarResult(result, expandPage = true)
                }
            }
            return
        }

        val lang = state.selectedLanguage ?: return
        if (state.filteredLanguageVideos.size > state.visiblePageSize) {
            _uiState.update { it.copy(visiblePageSize = it.visiblePageSize + PAGE_STEP) }
            return
        }
        if (state.languageSuggestionsResult?.hasMoreChannelBatches == true && !state.isLoadingMore) {
            _uiState.update { it.copy(isLoadingMore = true) }
            loadSuggestionsForLanguage(lang, forceRefresh = false, loadNextBatch = true)
        }
    }

    fun playVideo(video: VerifiedPodcastVideo) {
        val legacyItem = video.toYouTubeVideoItem()
        val updatedRecent = YouTubeRepository.recordWatchedPodcast(appContext, legacyItem)
        _uiState.update {
            it.copy(
                activeVideo = video,
                activeLegacyVideo = null,
                recentPodcasts = updatedRecent
            )
        }
    }

    fun playLegacyItem(item: YouTubeVideoItem) {
        val updatedRecent = YouTubeRepository.recordWatchedPodcast(appContext, item)
        _uiState.update {
            it.copy(
                activeVideo = null,
                activeLegacyVideo = item,
                recentPodcasts = updatedRecent
            )
        }
    }

    fun toggleFavorite(video: VerifiedPodcastVideo) {
        YouTubeRepository.toggleFavoritePodcast(appContext, video.toYouTubeVideoItem())
        _uiState.update {
            it.copy(favoritePodcasts = YouTubeRepository.getFavoritePodcasts(appContext))
        }
    }

    fun toggleFavoriteLegacy(item: YouTubeVideoItem) {
        YouTubeRepository.toggleFavoritePodcast(appContext, item)
        _uiState.update {
            it.copy(favoritePodcasts = YouTubeRepository.getFavoritePodcasts(appContext))
        }
    }

    fun onPlayerVerifiedDuration(videoId: String, durationSeconds: Int) {
        val state = _uiState.value
        val scholar = state.selectedScholar
        if (scholar != null) {
            val updated = PodcastLibraryRepository.reapplyPlayerDuration(appContext, scholar, videoId, durationSeconds)
            if (updated != null) applyScholarResult(updated)
        }
        val lang = state.selectedLanguage
        if (lang != null) {
            val updatedLang = PodcastLibraryRepository.reapplyPlayerDurationToLanguage(appContext, lang, videoId, durationSeconds)
            if (updatedLang != null) applyLanguageSuggestionsResult(updatedLang)
        }
    }

    fun scholarsForSelectedLanguage(): List<PodcastScholarProfile> {
        val lang = _uiState.value.selectedLanguage ?: return emptyList()
        val query = _uiState.value.searchQuery.trim().lowercase()
        val list = PodcastScholarRegistry.scholarsForLanguage(lang)
        if (query.isBlank()) return list
        return list.filter { s ->
            s.displayNameNative.lowercase().contains(query) ||
                s.displayNameEn.lowercase().contains(query) ||
                s.attributionAliases.any { it.lowercase().contains(query) }
        }
    }

    fun channelsForSelectedLanguage(): List<PodcastScholarProfile> {
        val lang = _uiState.value.selectedLanguage ?: return emptyList()
        val query = _uiState.value.searchQuery.trim().lowercase()
        val list = PodcastScholarRegistry.channelsForLanguage(lang)
        if (query.isBlank()) return list
        return list.filter { c ->
            c.displayNameNative.lowercase().contains(query) ||
                c.displayNameEn.lowercase().contains(query)
        }
    }

    fun extraCatalogChannelsForSelectedLanguage(): List<PodcastScholarProfile> {
        val lang = _uiState.value.selectedLanguage ?: return emptyList()
        val query = _uiState.value.searchQuery.trim().lowercase()
        val list = PodcastCatalogBridge.extraCatalogChannelsForLanguage(appContext, lang)
        if (query.isBlank()) return list
        return list.filter { c ->
            c.displayNameNative.lowercase().contains(query) ||
                c.displayNameEn.lowercase().contains(query)
        }
    }

    private fun applyScholarResult(result: ScholarPodcastLoadResult, expandPage: Boolean = false) {
        _uiState.update { state ->
            val nonEmpty = result.nonEmptyCategories().map { it.first }.toSet()
            val safeCategory = if (state.selectedCategory in nonEmpty) {
                state.selectedCategory
            } else {
                PodcastProfileCategory.ALL_ELIGIBLE
            }
            val updatedCounts = state.cachedCountsByScholarId.toMutableMap().apply {
                if (result.verifiedVideoCount > 0) {
                    put(result.scholar.id, result.verifiedVideoCount)
                }
            }
            state.copy(
                scholarResult = result,
                selectedCategory = safeCategory,
                visiblePageSize = if (expandPage) state.visiblePageSize + PAGE_STEP else state.visiblePageSize,
                cachedCountsByScholarId = updatedCounts,
                isLoading = false,
                isRefreshing = false,
                isLoadingMore = false
            )
        }
    }

    private fun applyLanguageSuggestionsResult(
        result: LanguageSuggestionsLoadResult,
        expandPage: Boolean = false
    ) {
        _uiState.update { state ->
            val nonEmpty = result.nonEmptyCategories().map { it.first }.toSet()
            val safeCategory = if (state.selectedCategory in nonEmpty) {
                state.selectedCategory
            } else {
                PodcastProfileCategory.ALL_ELIGIBLE
            }
            state.copy(
                languageSuggestionsResult = result,
                languageChannelCount = result.totalVerifiedChannelsInLanguage,
                selectedCategory = safeCategory,
                visiblePageSize = if (expandPage) state.visiblePageSize + PAGE_STEP else state.visiblePageSize,
                isLoading = false,
                isRefreshing = false,
                isLoadingMore = false
            )
        }
    }

    fun dispose() {
        stopForegroundRefreshTicker()
        activeLoadJob?.cancel()
        scope.cancel()
    }

    companion object {
        const val PAGE_STEP = 25
        const val TARGET_AUTO_EXPAND_VIDEOS_PER_LANG = 1200
        private const val MAX_AUTO_EXPAND_BATCHES = 3
    }
}
