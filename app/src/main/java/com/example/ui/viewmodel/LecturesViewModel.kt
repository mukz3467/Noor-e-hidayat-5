package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.lectures.ChannelDirectoryCoverageReport
import com.example.data.lectures.DurationFilterPolicy
import com.example.data.lectures.LectureDurationConfig
import com.example.data.lectures.LectureLanguageGroup
import com.example.data.repository.FeedLoadStatus
import com.example.data.repository.LectureCategory
import com.example.data.repository.LectureFeedResult
import com.example.data.repository.VerifiedIslamicChannel
import com.example.data.repository.YouTubeRepository
import com.example.data.repository.YouTubeVideoItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class LectureLibraryTab {
    ALL_FEEDS,
    FAVORITES,
    RECENTLY_WATCHED
}

data class LecturesUiState(
    val selectedTab: LectureLibraryTab = LectureLibraryTab.ALL_FEEDS,
    val selectedCategory: LectureCategory? = null,
    val selectedChannelKey: String? = null,
    val selectedLanguageGroup: LectureLanguageGroup = LectureLanguageGroup.ALL,
    val searchQuery: String = "",
    val minDurationSeconds: Int = LectureDurationConfig.MIN_LECTURE_DURATION_SECONDS,
    val durationPolicy: DurationFilterPolicy = DurationFilterPolicy.EXCLUDE_CONFIRMED_SHORTS_SHOW_UNVERIFIED_WITH_BADGE,
    val batchIndex: Int = 0,
    val isLoading: Boolean = false,
    val isLoadingMoreBatches: Boolean = false,
    val feedResult: LectureFeedResult = LectureFeedResult(
        videos = emptyList(),
        status = FeedLoadStatus.IDLE
    ),
    val verifiedChannels: List<VerifiedIslamicChannel> = emptyList(),
    val coverageReport: ChannelDirectoryCoverageReport = YouTubeRepository.getDirectoryCoverageReport(),
    val favoriteLectures: List<YouTubeVideoItem> = emptyList(),
    val recentLectures: List<YouTubeVideoItem> = emptyList(),
    val activeVideo: YouTubeVideoItem? = null
)

class LecturesViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext
    private var activeFetchJob: Job? = null
    private var foregroundTickerJob: Job? = null
    private var initializedQuery: String? = null

    private val _uiState = MutableStateFlow(
        LecturesUiState(
            verifiedChannels = YouTubeRepository.getVerifiedChannels(appContext),
            coverageReport = YouTubeRepository.getDirectoryCoverageReport(appContext),
            favoriteLectures = YouTubeRepository.getFavoriteLectures(appContext),
            recentLectures = YouTubeRepository.getRecentlyWatchedLectures(appContext)
        )
    )
    val uiState: StateFlow<LecturesUiState> = _uiState.asStateFlow()

    fun applyInitialQueryIfNeeded(initialQuery: String?) {
        if (initialQuery.isNullOrBlank() || initialQuery == initializedQuery) return
        initializedQuery = initialQuery
        _uiState.update {
            it.copy(
                searchQuery = initialQuery,
                selectedCategory = it.selectedCategory ?: LectureCategory.LATEST_LECTURES
            )
        }
        triggerFetch(forceRefresh = false, resetBatch = true)
    }

    fun selectTab(tab: LectureLibraryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectCategory(category: LectureCategory) {
        val current = _uiState.value
        val nextCategory = if (current.selectedCategory == category && current.selectedChannelKey == null) {
            null
        } else {
            category
        }
        _uiState.update {
            it.copy(
                selectedChannelKey = null,
                selectedCategory = nextCategory,
                batchIndex = 0
            )
        }
        if (nextCategory == null) {
            cancelActiveFetchAndResetIdle()
        } else {
            triggerFetch(forceRefresh = false, resetBatch = true)
        }
    }

    fun selectChannel(channelKey: String) {
        val current = _uiState.value
        val nextChannel = if (current.selectedChannelKey == channelKey) null else channelKey
        _uiState.update {
            it.copy(
                selectedChannelKey = nextChannel,
                batchIndex = 0
            )
        }
        if (nextChannel == null && current.selectedCategory == null) {
            cancelActiveFetchAndResetIdle()
        } else {
            triggerFetch(forceRefresh = false, resetBatch = true)
        }
    }

    /**
     * Keeps selected language active across category changes; refreshes current topic/channel
     * smoothly without resetting the screen or clearing active video playback.
     */
    fun selectLanguageGroup(group: LectureLanguageGroup) {
        val current = _uiState.value
        if (current.selectedLanguageGroup == group) return
        val filteredChannels = YouTubeRepository.verifiedChannelsForLanguage(group, appContext)
        // If a specific channel was selected and does not match the newly chosen language, clear channel selection
        val keepChannel = current.selectedChannelKey?.takeIf { key ->
            group == LectureLanguageGroup.ALL || filteredChannels.any { it.key == key }
        }
        _uiState.update {
            it.copy(
                selectedLanguageGroup = group,
                selectedChannelKey = keepChannel,
                batchIndex = 0
            )
        }
        if (current.selectedCategory != null || keepChannel != null) {
            triggerFetch(forceRefresh = false, resetBatch = true)
        }
    }

    fun selectMinDurationSeconds(seconds: Int) {
        val validated = seconds.coerceAtLeast(LectureDurationConfig.MIN_LECTURE_DURATION_SECONDS)
        if (_uiState.value.minDurationSeconds == validated) return
        _uiState.update { it.copy(minDurationSeconds = validated) }
        if (_uiState.value.selectedCategory != null || _uiState.value.selectedChannelKey != null) {
            triggerFetch(forceRefresh = false, resetBatch = false)
        }
    }

    fun selectDurationPolicy(policy: DurationFilterPolicy) {
        if (_uiState.value.durationPolicy == policy) return
        _uiState.update { it.copy(durationPolicy = policy) }
        if (_uiState.value.selectedCategory != null || _uiState.value.selectedChannelKey != null) {
            triggerFetch(forceRefresh = false, resetBatch = false)
        }
    }

    fun refresh() {
        if (_uiState.value.selectedCategory != null || _uiState.value.selectedChannelKey != null) {
            triggerFetch(forceRefresh = true, resetBatch = false)
        }
    }

    fun startForegroundAutoRefreshTicker() {
        if (foregroundTickerJob?.isActive == true) return
        foregroundTickerJob = viewModelScope.launch {
            while (isActive) {
                delay(YouTubeRepository.CACHE_TTL_MS)
                refreshIfStale()
            }
        }
    }

    fun stopForegroundAutoRefreshTicker() {
        foregroundTickerJob?.cancel()
        foregroundTickerJob = null
    }

    fun refreshIfStale() {
        val state = _uiState.value
        if (state.selectedCategory == null && state.selectedChannelKey == null) return
        if (!YouTubeRepository.isNetworkAvailable(appContext)) return
        val lastUpdated = state.feedResult.lastUpdatedMs
        if (lastUpdated > 0L && (System.currentTimeMillis() - lastUpdated) >= YouTubeRepository.CACHE_TTL_MS) {
            triggerFetch(forceRefresh = true, resetBatch = false, isBatchAppend = true)
        }
    }

    fun loadMoreChannelBatches() {
        val current = _uiState.value
        if (current.isLoading || current.isLoadingMoreBatches || !current.feedResult.hasMoreChannelBatches) return
        _uiState.update { it.copy(batchIndex = it.batchIndex + 1, isLoadingMoreBatches = true) }
        triggerFetch(forceRefresh = false, resetBatch = false, isBatchAppend = true)
    }

    fun clearFilters() {
        val hadLang = _uiState.value.selectedLanguageGroup != LectureLanguageGroup.ALL
        _uiState.update {
            it.copy(
                searchQuery = "",
                selectedLanguageGroup = LectureLanguageGroup.ALL
            )
        }
        if (hadLang && (_uiState.value.selectedCategory != null || _uiState.value.selectedChannelKey != null)) {
            triggerFetch(forceRefresh = false, resetBatch = true)
        }
    }

    fun selectAndPlay(video: YouTubeVideoItem) {
        val updatedRecent = YouTubeRepository.recordWatchedLecture(appContext, video)
        _uiState.update {
            it.copy(
                activeVideo = video,
                recentLectures = updatedRecent
            )
        }
    }

    fun toggleFavorite(video: YouTubeVideoItem) {
        YouTubeRepository.toggleFavoriteLecture(appContext, video)
        _uiState.update {
            it.copy(favoriteLectures = YouTubeRepository.getFavoriteLectures(appContext))
        }
    }

    /**
     * Records real video duration obtained directly from the official YouTube embedded player.
     * Updates active video and feed list immediately so verified long-form status or short-clip
     * warning reflects real metadata (never guessed or invented).
     */
    fun onVideoDurationDetected(videoId: String, durationSeconds: Int) {
        if (durationSeconds <= 0) return
        YouTubeRepository.recordPlayerVerifiedDuration(appContext, videoId, durationSeconds)
        _uiState.update { state ->
            val newStatus = LectureDurationConfig.evaluateDuration(
                durationSeconds = durationSeconds,
                isShortsUrl = false,
                minDurationSeconds = state.minDurationSeconds
            )
            val updatedActive = state.activeVideo?.let { active ->
                if (active.id == videoId) {
                    active.copy(durationSeconds = durationSeconds, durationStatus = newStatus)
                } else active
            }
            val updatedVideos = state.feedResult.videos.map { item ->
                if (item.id == videoId) {
                    item.copy(durationSeconds = durationSeconds, durationStatus = newStatus)
                } else item
            }
            state.copy(
                activeVideo = updatedActive,
                feedResult = state.feedResult.copy(videos = updatedVideos)
            )
        }
    }

    fun cancelActiveOperations() {
        stopForegroundAutoRefreshTicker()
        activeFetchJob?.cancel()
        activeFetchJob = null
        _uiState.update {
            it.copy(isLoading = false, isLoadingMoreBatches = false)
        }
    }

    private fun cancelActiveFetchAndResetIdle() {
        activeFetchJob?.cancel()
        activeFetchJob = null
        _uiState.update {
            it.copy(
                isLoading = false,
                isLoadingMoreBatches = false,
                feedResult = LectureFeedResult(videos = emptyList(), status = FeedLoadStatus.IDLE)
            )
        }
    }

    private fun triggerFetch(
        forceRefresh: Boolean,
        resetBatch: Boolean,
        isBatchAppend: Boolean = false
    ) {
        activeFetchJob?.cancel()
        val snapshot = _uiState.value
        val topic = snapshot.selectedCategory
        val channelKey = snapshot.selectedChannelKey
        if (topic == null && channelKey == null) {
            cancelActiveFetchAndResetIdle()
            return
        }

        val targetBatchIndex = if (resetBatch) 0 else snapshot.batchIndex
        _uiState.update {
            it.copy(
                isLoading = !isBatchAppend,
                isLoadingMoreBatches = isBatchAppend,
                batchIndex = targetBatchIndex
            )
        }

        activeFetchJob = viewModelScope.launch {
            try {
                var currentBatch = targetBatchIndex
                var result = YouTubeRepository.fetchLecturesForTopic(
                    context = appContext,
                    topic = topic,
                    channelKey = channelKey,
                    forceRefresh = forceRefresh,
                    languageGroup = snapshot.selectedLanguageGroup,
                    minDurationSeconds = snapshot.minDurationSeconds,
                    durationPolicy = snapshot.durationPolicy,
                    batchIndex = currentBatch,
                    batchSize = YouTubeRepository.DEFAULT_CHANNEL_BATCH_SIZE
                )

                val shouldAutoExpand = channelKey == null &&
                    topic != null &&
                    !isBatchAppend &&
                    result.videos.isNotEmpty() &&
                    result.videos.size < TARGET_MIN_VIDEOS_PER_CATEGORY &&
                    result.hasMoreChannelBatches

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMoreBatches = shouldAutoExpand,
                        batchIndex = currentBatch,
                        feedResult = result
                    )
                }

                // Progressively expand across additional verified channel batches until 50+ real
                // non-short videos are available for the selected category (or all mapped channels
                // are exhausted). Earlier batches are already cached in memory so each step only
                // fetches the next batch of 10 channels.
                if (shouldAutoExpand) {
                    val maxAutoBatch = targetBatchIndex + MAX_AUTO_EXPAND_EXTRA_BATCHES
                    var consecutiveZeroGain = 0
                    while (
                        currentBatch < maxAutoBatch &&
                        result.videos.size < TARGET_MIN_VIDEOS_PER_CATEGORY &&
                        result.hasMoreChannelBatches &&
                        consecutiveZeroGain < 2
                    ) {
                        val previousCount = result.videos.size
                        currentBatch += 1
                        result = YouTubeRepository.fetchLecturesForTopic(
                            context = appContext,
                            topic = topic,
                            channelKey = null,
                            forceRefresh = false,
                            languageGroup = snapshot.selectedLanguageGroup,
                            minDurationSeconds = snapshot.minDurationSeconds,
                            durationPolicy = snapshot.durationPolicy,
                            batchIndex = currentBatch,
                            batchSize = YouTubeRepository.DEFAULT_CHANNEL_BATCH_SIZE
                        )
                        if (result.videos.size <= previousCount) {
                            consecutiveZeroGain += 1
                        } else {
                            consecutiveZeroGain = 0
                        }
                        val stillNeedMore = currentBatch < maxAutoBatch &&
                            result.videos.size < TARGET_MIN_VIDEOS_PER_CATEGORY &&
                            result.hasMoreChannelBatches &&
                            consecutiveZeroGain < 2
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMoreBatches = stillNeedMore,
                                batchIndex = currentBatch,
                                feedResult = result
                            )
                        }
                    }
                    _uiState.update { it.copy(isLoadingMoreBatches = false) }
                }
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                // Guarantee loading state is NEVER stuck after an unexpected failure
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMoreBatches = false,
                        feedResult = LectureFeedResult(
                            videos = emptyList(),
                            status = FeedLoadStatus.ERROR_NO_CACHE,
                            errorMessage = e.message ?: "Unable to load verified feeds."
                        )
                    )
                }
            }
        }
    }

    companion object {
        const val TARGET_MIN_VIDEOS_PER_CATEGORY = 400
        private const val MAX_AUTO_EXPAND_EXTRA_BATCHES = 35
    }

    override fun onCleared() {
        stopForegroundAutoRefreshTicker()
        activeFetchJob?.cancel()
        super.onCleared()
    }
}
