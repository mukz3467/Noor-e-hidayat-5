package com.example.data.podcast

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.data.repository.AudioFocusCoordinator
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object PodcastPlayerManager {

    val SUPPORTED_SPEEDS = listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _playbackState = MutableStateFlow(PodcastPlaybackUiState())
    val playbackState: StateFlow<PodcastPlaybackUiState> = _playbackState.asStateFlow()

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null
    private var isConnectingController = false
    private val pendingControllerCallbacks = mutableListOf<(MediaController) -> Unit>()
    private var positionTickerJob: Job? = null
    private var appContextRef: Context? = null

    private fun pauseFromFocusLoss() {
        val controller = mediaController ?: return
        if (controller.isPlaying) {
            controller.pause()
            persistCurrentPosition()
            syncStateFromController()
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            syncStateFromController()
            if (isPlaying) {
                startPositionTicker()
            } else {
                persistCurrentPosition()
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            syncStateFromController()
            if (playbackState == Player.STATE_ENDED) {
                AudioFocusCoordinator.onPlaybackStopped("podcast")
                appContextRef?.let { ctx ->
                    _playbackState.value.currentEpisode?.let { ep ->
                        PodcastRepository.savePlaybackPositionMs(ctx, ep.guid, 0L)
                    }
                }
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            syncStateFromController()
        }

        override fun onPlayerError(error: PlaybackException) {
            AudioFocusCoordinator.onPlaybackStopped("podcast")
            val detail = error.localizedMessage ?: "Audio stream failed (${error.errorCodeName})"
            _playbackState.value = _playbackState.value.copy(
                isPlaying = false,
                isBuffering = false,
                isConnecting = false,
                errorMessage = detail
            )
            persistCurrentPosition()
        }
    }

    fun playEpisodeWithQueue(
        context: Context,
        queue: List<PodcastEpisode>,
        startIndex: Int
    ) {
        if (queue.isEmpty() || startIndex !in queue.indices) return
        val appContext = context.applicationContext
        appContextRef = appContext
        val selected = queue[startIndex]

        if (!PodcastRepository.isValidAudioEnclosureUrl(selected.audioEnclosureUrl, selected.mimeType) &&
            PodcastRepository.getLocalAudioFile(appContext, selected) == null
        ) {
            _playbackState.value = _playbackState.value.copy(
                errorMessage = "Episode does not have a valid verified audio stream."
            )
            return
        }

        val localSelectedFile = PodcastRepository.getLocalAudioFile(appContext, selected)
        val resumePosMs = PodcastRepository.getSavedPlaybackPositionMs(appContext, selected.guid)

        val savedSpeed = com.example.data.repository.UserPreferencesRepository.getSavedAudioSpeed(appContext)
        val initialSpeed = if (_playbackState.value.currentEpisode == null) savedSpeed else _playbackState.value.playbackSpeed

        _playbackState.value = _playbackState.value.copy(
            currentEpisode = selected,
            queue = queue,
            currentIndex = startIndex,
            isConnecting = true,
            isBuffering = true,
            playingOfflineFile = localSelectedFile != null,
            playbackSpeed = initialSpeed,
            errorMessage = null
        )

        AudioFocusCoordinator.onPlaybackStarting(appContext, "podcast") { pauseFromFocusLoss() }
        ensureControllerConnected(appContext) { controller ->
            val mediaItems = queue.map { ep ->
                val localFile = PodcastRepository.getLocalAudioFile(appContext, ep)
                val uri = if (localFile != null) Uri.fromFile(localFile) else Uri.parse(ep.audioEnclosureUrl)
                val meta = MediaMetadata.Builder()
                    .setTitle(ep.title)
                    .setArtist(ep.host.ifBlank { ep.publisher })
                    .setAlbumTitle(ep.showName)
                    .apply {
                        if (ep.artworkUrl.startsWith("http")) {
                            setArtworkUri(Uri.parse(ep.artworkUrl))
                        }
                    }
                    .build()
                MediaItem.Builder()
                    .setMediaId(ep.guid)
                    .setUri(uri)
                    .setMediaMetadata(meta)
                    .build()
            }

            controller.setMediaItems(mediaItems, startIndex, resumePosMs)
            controller.setPlaybackSpeed(_playbackState.value.playbackSpeed)
            controller.prepare()
            controller.play()
            syncStateFromController()
            startPositionTicker()
        }
    }

    fun togglePlayPause(context: Context) {
        val controller = mediaController
        if (controller == null) {
            val state = _playbackState.value
            if (state.currentEpisode != null && state.queue.isNotEmpty() && state.currentIndex in state.queue.indices) {
                playEpisodeWithQueue(context, state.queue, state.currentIndex)
            }
            return
        }
        if (controller.isPlaying) {
            controller.pause()
            persistCurrentPosition()
            AudioFocusCoordinator.onPlaybackStopped("podcast")
        } else {
            AudioFocusCoordinator.onPlaybackStarting(context.applicationContext, "podcast") { pauseFromFocusLoss() }
            if (controller.playbackState == Player.STATE_ENDED) {
                controller.seekTo(0L)
            }
            controller.play()
            startPositionTicker()
        }
        syncStateFromController()
    }

    fun seekTo(positionMs: Long) {
        val controller = mediaController ?: return
        val clamped = positionMs.coerceAtLeast(0L)
        controller.seekTo(clamped)
        _playbackState.value = _playbackState.value.copy(positionMs = clamped)
        persistCurrentPosition()
    }

    fun skipForward(deltaMs: Long = 15_000L) {
        val controller = mediaController ?: return
        val duration = controller.duration.coerceAtLeast(0L)
        val target = (controller.currentPosition + deltaMs).let { pos ->
            if (duration > 0L) pos.coerceAtMost(duration) else pos
        }
        seekTo(target)
    }

    fun skipBackward(deltaMs: Long = 15_000L) {
        val controller = mediaController ?: return
        seekTo((controller.currentPosition - deltaMs).coerceAtLeast(0L))
    }

    fun setPlaybackSpeed(speed: Float) {
        val validSpeed = if (speed in SUPPORTED_SPEEDS) speed else 1.0f
        mediaController?.setPlaybackSpeed(validSpeed)
        _playbackState.value = _playbackState.value.copy(playbackSpeed = validSpeed)
    }

    fun cyclePlaybackSpeed(): Float {
        val current = _playbackState.value.playbackSpeed
        val idx = SUPPORTED_SPEEDS.indexOfFirst { kotlin.math.abs(it - current) < 0.05f }
        val next = SUPPORTED_SPEEDS[(idx + 1) % SUPPORTED_SPEEDS.size]
        setPlaybackSpeed(next)
        return next
    }

    fun skipToNext() {
        val controller = mediaController ?: return
        if (controller.hasNextMediaItem()) {
            appContextRef?.let { ctx ->
                AudioFocusCoordinator.onPlaybackStarting(ctx, "podcast") { pauseFromFocusLoss() }
            }
            persistCurrentPosition()
            controller.seekToNextMediaItem()
            controller.play()
            syncStateFromController()
        }
    }

    fun skipToPrevious() {
        val controller = mediaController ?: return
        if (controller.currentPosition > 4_000L || !controller.hasPreviousMediaItem()) {
            controller.seekTo(0L)
        } else {
            persistCurrentPosition()
            controller.seekToPreviousMediaItem()
        }
        syncStateFromController()
    }

    fun addToQueue(context: Context, episode: PodcastEpisode) {
        val currentQueue = _playbackState.value.queue
        if (currentQueue.isEmpty() || _playbackState.value.currentEpisode == null) {
            playEpisodeWithQueue(context, listOf(episode), 0)
            return
        }
        if (currentQueue.any { it.guid == episode.guid }) return
        val appContext = context.applicationContext
        val localFile = PodcastRepository.getLocalAudioFile(appContext, episode)
        val uri = if (localFile != null) Uri.fromFile(localFile) else Uri.parse(episode.audioEnclosureUrl)
        val item = MediaItem.Builder()
            .setMediaId(episode.guid)
            .setUri(uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(episode.title)
                    .setArtist(episode.host.ifBlank { episode.publisher })
                    .setAlbumTitle(episode.showName)
                    .build()
            )
            .build()
        mediaController?.addMediaItem(item)
        _playbackState.value = _playbackState.value.copy(
            queue = currentQueue + episode
        )
    }

    /**
     * Stops playback completely, clears queue & notification, and shuts down the foreground service
     * so audio never continues in the background after an explicit Stop action.
     */
    fun stopAndClearSession(context: Context) {
        AudioFocusCoordinator.onPlaybackStopped("podcast")
        persistCurrentPosition()
        positionTickerJob?.cancel()
        positionTickerJob = null
        pendingControllerCallbacks.clear()
        isConnectingController = false
        val appContext = context.applicationContext

        mediaController?.let { controller ->
            try {
                controller.stop()
                controller.clearMediaItems()
                controller.removeListener(playerListener)
            } catch (_: Exception) {
            }
        }
        mediaController = null
        controllerFuture?.let { future ->
            try {
                MediaController.releaseFuture(future)
            } catch (_: Exception) {
            }
        }
        controllerFuture = null

        try {
            appContext.stopService(Intent(appContext, PodcastPlaybackService::class.java))
        } catch (_: Exception) {
        }

        _playbackState.value = PodcastPlaybackUiState(playbackSpeed = _playbackState.value.playbackSpeed)
    }

    fun detachUiOnly() {
        // Keeps background MediaSessionService running if playing, but pauses rapid UI polling when UI closes if idle
        val controller = mediaController
        if (controller == null || !controller.isPlaying) {
            positionTickerJob?.cancel()
            positionTickerJob = null
        }
    }

    private fun ensureControllerConnected(
        appContext: Context,
        onReady: (MediaController) -> Unit
    ) {
        val existing = mediaController
        if (existing != null) {
            onReady(existing)
            return
        }

        pendingControllerCallbacks.add(onReady)
        if (isConnectingController) {
            return
        }
        isConnectingController = true

        val sessionToken = SessionToken(
            appContext,
            ComponentName(appContext, PodcastPlaybackService::class.java)
        )
        val future = MediaController.Builder(appContext, sessionToken).buildAsync()
        controllerFuture = future
        future.addListener(
            {
                isConnectingController = false
                val callbacks = pendingControllerCallbacks.toList()
                pendingControllerCallbacks.clear()
                try {
                    val controller = future.get()
                    mediaController = controller
                    controller.removeListener(playerListener)
                    controller.addListener(playerListener)
                    callbacks.forEach { cb -> cb(controller) }
                } catch (e: Exception) {
                    AudioFocusCoordinator.onPlaybackStopped("podcast")
                    _playbackState.value = _playbackState.value.copy(
                        isConnecting = false,
                        isBuffering = false,
                        errorMessage = e.localizedMessage ?: "Unable to connect audio session"
                    )
                }
            },
            ContextCompat.getMainExecutor(appContext)
        )
    }

    private fun syncStateFromController() {
        val controller = mediaController ?: return
        val state = _playbackState.value
        val queue = state.queue
        val idx = controller.currentMediaItemIndex.takeIf { it in queue.indices } ?: state.currentIndex
        val ep = queue.getOrNull(idx) ?: state.currentEpisode
        val controllerDur = controller.duration.takeIf { it > 0L } ?: ((ep?.durationSeconds ?: 0L) * 1000L)
        val offline = ep != null && appContextRef != null && PodcastRepository.isEpisodeDownloadedOffline(appContextRef!!, ep)

        _playbackState.value = state.copy(
            currentEpisode = ep,
            currentIndex = idx,
            isPlaying = controller.isPlaying,
            isBuffering = controller.playbackState == Player.STATE_BUFFERING,
            isConnecting = false,
            positionMs = controller.currentPosition.coerceAtLeast(0L),
            durationMs = controllerDur.coerceAtLeast(0L),
            playingOfflineFile = offline
        )
    }

    private fun startPositionTicker() {
        if (positionTickerJob?.isActive == true) return
        positionTickerJob = scope.launch {
            var tickCount = 0
            while (isActive) {
                val controller = mediaController ?: break
                syncStateFromController()
                tickCount++
                if (tickCount % 6 == 0) {
                    persistCurrentPosition()
                }
                if (!controller.isPlaying && controller.playbackState != Player.STATE_BUFFERING) {
                    break
                }
                delay(500L)
            }
        }
    }

    private fun persistCurrentPosition() {
        val ctx = appContextRef ?: return
        val ep = _playbackState.value.currentEpisode ?: return
        val pos = mediaController?.currentPosition ?: _playbackState.value.positionMs
        if (pos > 0L) {
            PodcastRepository.savePlaybackPositionMs(ctx, ep.guid, pos)
        }
    }
}
