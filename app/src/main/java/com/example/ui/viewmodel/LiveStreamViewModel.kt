package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.live.HarameynLiveSourceResolver
import com.example.data.live.HarameynLiveSources
import com.example.data.live.HarameynLiveStreamPrefs
import com.example.data.live.LiveConnectionPhase
import com.example.data.live.LiveSourceAttempt
import com.example.data.live.LiveSourceAttemptStatus
import com.example.data.live.ResolvedLiveStreamSource
import com.example.data.repository.HarameynLocation
import com.example.data.repository.HarameynStreamChannel
import com.example.data.repository.YouTubeRepository
import com.example.data.repository.YouTubeVideoItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicLong

data class LiveStreamUiState(
    val selectedLocation: HarameynLocation = HarameynLocation.MAKKAH,
    val channel: HarameynStreamChannel = YouTubeRepository.HARAMEYN_CHANNELS.getValue(HarameynLocation.MAKKAH),
    val phase: LiveConnectionPhase = LiveConnectionPhase.CONNECTING,
    val activeSourceIndex: Int = 0,
    val activeSource: ResolvedLiveStreamSource? = null,
    val attemptToken: Long = 0L,
    val sessionNonce: Int = 0,
    val sourceAttempts: List<LiveSourceAttempt> = HarameynLiveSources.sourcesFor(HarameynLocation.MAKKAH)
        .map { LiveSourceAttempt(it, LiveSourceAttemptStatus.PENDING) },
    val statusDetail: String = "",
    val isFallbackActive: Boolean = false,
    val recentRecordedUploads: List<YouTubeVideoItem> = emptyList(),
    val selectedRecordedVideo: YouTubeVideoItem? = null
)

class LiveStreamViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext
    private val tokenGenerator = AtomicLong(0L)
    private var activeProbeJob: Job? = null
    private var recordedUploadsJob: Job? = null
    private var connectivityManager: ConnectivityManager? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var lastLoadedRecordedLocation: HarameynLocation? = null
    private var needsRestartOnReentry: Boolean = false

    private val initialLocation = HarameynLiveStreamPrefs.loadSelectedLocation(appContext)

    private val _uiState = MutableStateFlow(
        LiveStreamUiState(
            selectedLocation = initialLocation,
            channel = YouTubeRepository.HARAMEYN_CHANNELS.getValue(initialLocation),
            phase = LiveConnectionPhase.CONNECTING,
            sourceAttempts = HarameynLiveSources.sourcesFor(initialLocation)
                .map { LiveSourceAttempt(it, LiveSourceAttemptStatus.PENDING) }
        )
    )
    val uiState: StateFlow<LiveStreamUiState> = _uiState.asStateFlow()

    init {
        registerNetworkCallback()
        startLocationFlow(initialLocation, isReconnect = false)
    }

    fun selectLocation(location: HarameynLocation) {
        if (_uiState.value.selectedLocation == location &&
            _uiState.value.phase != LiveConnectionPhase.SOURCE_UNAVAILABLE &&
            _uiState.value.phase != LiveConnectionPhase.OFFLINE
        ) {
            return
        }
        HarameynLiveStreamPrefs.saveSelectedLocation(appContext, location)
        lastLoadedRecordedLocation = null
        startLocationFlow(location, isReconnect = false)
    }

    fun retryAllSources() {
        val location = _uiState.value.selectedLocation
        lastLoadedRecordedLocation = null
        startLocationFlow(location, isReconnect = false)
    }

    fun selectRecordedVideo(video: YouTubeVideoItem) {
        activeProbeJob?.cancel()
        // Invalidate live player token & release live source so only ONE player is ever active.
        val nextToken = tokenGenerator.incrementAndGet()
        _uiState.update { state ->
            state.copy(
                attemptToken = nextToken,
                activeSource = null,
                selectedRecordedVideo = video
            )
        }
    }

    fun returnToLiveBroadcast() {
        val location = _uiState.value.selectedLocation
        startLocationFlow(location, isReconnect = false)
    }

    /**
     * Called ONLY by the active in-app player once actual live video playback is genuinely confirmed.
     */
    fun onPlayerConfirmedLive(attemptToken: Long) {
        _uiState.update { state ->
            if (state.attemptToken != attemptToken || state.activeSource == null) return@update state
            val updatedAttempts = state.sourceAttempts.mapIndexed { idx, attempt ->
                if (idx == state.activeSourceIndex) {
                    attempt.copy(status = LiveSourceAttemptStatus.ACTIVE_LIVE, detail = "Live playback active")
                } else {
                    attempt
                }
            }
            state.copy(
                phase = LiveConnectionPhase.LIVE,
                isFallbackActive = state.activeSourceIndex > 0,
                sourceAttempts = updatedAttempts,
                statusDetail = state.activeSource.resolvedTitle.ifBlank { state.activeSource.config.labelEn }
            )
        }
    }

    /**
     * Called when an already-live player is buffering or performing its bounded reconnect attempt.
     */
    fun onPlayerReconnecting(attemptToken: Long) {
        _uiState.update { state ->
            if (state.attemptToken != attemptToken || state.activeSource == null) return@update state
            state.copy(phase = LiveConnectionPhase.RECONNECTING)
        }
    }

    /**
     * Called when the active player experiences a genuine playback failure, error, restriction,
     * renderer crash, or startup timeout. Automatically advances to the next configured source
     * without requiring user intervention.
     */
    fun onPlayerFailed(attemptToken: Long, reason: String) {
        val snapshot = _uiState.value
        if (snapshot.attemptToken != attemptToken) return

        val failedIndex = snapshot.activeSourceIndex
        val updatedAttempts = snapshot.sourceAttempts.mapIndexed { idx, attempt ->
            if (idx == failedIndex) {
                attempt.copy(status = LiveSourceAttemptStatus.FAILED, detail = reason)
            } else {
                attempt
            }
        }

        if (!YouTubeRepository.isNetworkAvailable(appContext)) {
            markOffline(snapshot.selectedLocation, updatedAttempts)
            return
        }

        val nextIndex = failedIndex + 1
        val sources = HarameynLiveSources.sourcesFor(snapshot.selectedLocation)
        if (nextIndex >= sources.size) {
            val terminalToken = tokenGenerator.incrementAndGet()
            _uiState.update { state ->
                state.copy(
                    attemptToken = terminalToken,
                    activeSource = null,
                    activeSourceIndex = nextIndex,
                    phase = LiveConnectionPhase.SOURCE_UNAVAILABLE,
                    sourceAttempts = updatedAttempts,
                    statusDetail = reason
                )
            }
        } else {
            probeFromSourceIndex(
                location = snapshot.selectedLocation,
                startIndex = nextIndex,
                priorAttempts = updatedAttempts,
                isAutomaticFallback = true
            )
        }
    }

    /**
     * Handles real-time network disconnects and reconnects cleanly without infinite retry loops.
     */
    fun onNetworkConnectivityChanged(isOnline: Boolean) {
        val current = _uiState.value
        if (!isOnline) {
            activeProbeJob?.cancel()
            val nextToken = tokenGenerator.incrementAndGet()
            _uiState.update { state ->
                state.copy(
                    attemptToken = nextToken,
                    activeSource = null,
                    phase = LiveConnectionPhase.OFFLINE,
                    statusDetail = "Offline — connect to internet to watch the live broadcast."
                )
            }
        } else if (current.phase == LiveConnectionPhase.OFFLINE) {
            startLocationFlow(current.selectedLocation, isReconnect = true)
        }
    }

    fun onScreenAttached() {
        if (needsRestartOnReentry) {
            needsRestartOnReentry = false
            startLocationFlow(_uiState.value.selectedLocation, isReconnect = false)
        }
    }

    fun onScreenDisposed() {
        cancelActiveOperations()
        needsRestartOnReentry = true
        val nextToken = tokenGenerator.incrementAndGet()
        _uiState.update { state ->
            state.copy(
                attemptToken = nextToken,
                activeSource = null,
                phase = if (state.phase == LiveConnectionPhase.OFFLINE) {
                    LiveConnectionPhase.OFFLINE
                } else {
                    LiveConnectionPhase.CONNECTING
                }
            )
        }
    }

    fun cancelActiveOperations() {
        activeProbeJob?.cancel()
        recordedUploadsJob?.cancel()
        activeProbeJob = null
        recordedUploadsJob = null
    }

    private fun startLocationFlow(location: HarameynLocation, isReconnect: Boolean) {
        activeProbeJob?.cancel()
        val channel = YouTubeRepository.HARAMEYN_CHANNELS.getValue(location)
        val freshAttempts = HarameynLiveSources.sourcesFor(location)
            .map { LiveSourceAttempt(it, LiveSourceAttemptStatus.PENDING) }

        if (!YouTubeRepository.isNetworkAvailable(appContext)) {
            markOffline(location, freshAttempts)
            return
        }

        _uiState.update { state ->
            state.copy(
                selectedLocation = location,
                channel = channel,
                phase = if (isReconnect) LiveConnectionPhase.RECONNECTING else LiveConnectionPhase.CONNECTING,
                activeSourceIndex = 0,
                activeSource = null,
                sessionNonce = state.sessionNonce + 1,
                sourceAttempts = freshAttempts,
                statusDetail = "",
                isFallbackActive = false,
                selectedRecordedVideo = null,
                recentRecordedUploads = if (state.selectedLocation == location) state.recentRecordedUploads else emptyList()
            )
        }

        // Load official channel recent recorded uploads once per location (strictly separate from live status)
        if (lastLoadedRecordedLocation != location) {
            lastLoadedRecordedLocation = location
            recordedUploadsJob?.cancel()
            recordedUploadsJob = viewModelScope.launch {
                val uploads = runCatching {
                    YouTubeRepository.fetchHarameynRecordedUploads(appContext, location)
                }.getOrDefault(emptyList())
                _uiState.update { state ->
                    if (state.selectedLocation == location) {
                        state.copy(recentRecordedUploads = uploads)
                    } else {
                        state
                    }
                }
            }
        }

        probeFromSourceIndex(
            location = location,
            startIndex = 0,
            priorAttempts = freshAttempts,
            isAutomaticFallback = isReconnect
        )
    }

    private fun probeFromSourceIndex(
        location: HarameynLocation,
        startIndex: Int,
        priorAttempts: List<LiveSourceAttempt>,
        isAutomaticFallback: Boolean
    ) {
        activeProbeJob?.cancel()
        val runToken = tokenGenerator.incrementAndGet()
        val sources = HarameynLiveSources.sourcesFor(location)

        _uiState.update { state ->
            state.copy(
                attemptToken = runToken,
                activeSource = null,
                activeSourceIndex = startIndex,
                phase = if (isAutomaticFallback) LiveConnectionPhase.RECONNECTING else LiveConnectionPhase.CONNECTING,
                sourceAttempts = priorAttempts,
                selectedRecordedVideo = null
            )
        }

        activeProbeJob = viewModelScope.launch {
            try {
                val attempts = priorAttempts.toMutableList()
                var resolved: ResolvedLiveStreamSource? = null
                var resolvedIdx = startIndex
                var lastReason = "All supported live sources are currently unavailable."

                for (idx in startIndex until sources.size) {
                    if (!YouTubeRepository.isNetworkAvailable(appContext)) {
                        markOffline(location, attempts)
                        return@launch
                    }

                    val candidate = sources[idx]
                    attempts[idx] = LiveSourceAttempt(
                        source = candidate,
                        status = LiveSourceAttemptStatus.TESTING,
                        detail = "Connecting..."
                    )
                    _uiState.update { state ->
                        if (state.attemptToken != runToken) return@update state
                        state.copy(
                            activeSourceIndex = idx,
                            phase = if (idx > 0 || isAutomaticFallback) LiveConnectionPhase.RECONNECTING else LiveConnectionPhase.CONNECTING,
                            sourceAttempts = attempts.toList(),
                            statusDetail = candidate.labelEn
                        )
                    }

                    val outcome = withContext(Dispatchers.IO) {
                        HarameynLiveSourceResolver.probeSource(candidate, YouTubeRepository.httpClient)
                    }

                    if (outcome.resolved != null) {
                        resolved = outcome.resolved
                        resolvedIdx = idx
                        attempts[idx] = LiveSourceAttempt(
                            source = candidate,
                            status = LiveSourceAttemptStatus.TESTING,
                            detail = "Starting player..."
                        )
                        break
                    } else {
                        lastReason = outcome.failureReason ?: "Source unavailable"
                        attempts[idx] = LiveSourceAttempt(
                            source = candidate,
                            status = LiveSourceAttemptStatus.FAILED,
                            detail = lastReason
                        )
                    }
                }

                _uiState.update { state ->
                    if (state.attemptToken != runToken) return@update state
                    if (resolved != null) {
                        state.copy(
                            activeSourceIndex = resolvedIdx,
                            activeSource = resolved,
                            phase = if (resolvedIdx > 0 || isAutomaticFallback) LiveConnectionPhase.RECONNECTING else LiveConnectionPhase.CONNECTING,
                            isFallbackActive = resolvedIdx > 0,
                            sourceAttempts = attempts.toList(),
                            statusDetail = resolved.config.labelEn
                        )
                    } else {
                        state.copy(
                            activeSourceIndex = sources.size,
                            activeSource = null,
                            phase = LiveConnectionPhase.SOURCE_UNAVAILABLE,
                            sourceAttempts = attempts.toList(),
                            statusDetail = lastReason
                        )
                    }
                }
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                _uiState.update { state ->
                    if (state.attemptToken != runToken) return@update state
                    state.copy(
                        activeSource = null,
                        phase = LiveConnectionPhase.SOURCE_UNAVAILABLE,
                        statusDetail = e.message ?: "Unable to connect to live broadcast."
                    )
                }
            }
        }
    }

    private fun markOffline(location: HarameynLocation, attempts: List<LiveSourceAttempt>) {
        val nextToken = tokenGenerator.incrementAndGet()
        _uiState.update { state ->
            state.copy(
                selectedLocation = location,
                channel = YouTubeRepository.HARAMEYN_CHANNELS.getValue(location),
                attemptToken = nextToken,
                activeSource = null,
                phase = LiveConnectionPhase.OFFLINE,
                sourceAttempts = attempts,
                statusDetail = "Offline — connect to internet to watch the live broadcast."
            )
        }
    }

    private fun registerNetworkCallback() {
        runCatching {
            val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    viewModelScope.launch {
                        onNetworkConnectivityChanged(true)
                    }
                }

                override fun onLost(network: Network) {
                    viewModelScope.launch {
                        val stillOnline = YouTubeRepository.isNetworkAvailable(appContext)
                        if (!stillOnline) {
                            onNetworkConnectivityChanged(false)
                        }
                    }
                }
            }
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            cm.registerNetworkCallback(request, callback)
            connectivityManager = cm
            networkCallback = callback
        }
    }

    override fun onCleared() {
        cancelActiveOperations()
        runCatching {
            networkCallback?.let { cb -> connectivityManager?.unregisterNetworkCallback(cb) }
        }
        networkCallback = null
        connectivityManager = null
        super.onCleared()
    }
}
