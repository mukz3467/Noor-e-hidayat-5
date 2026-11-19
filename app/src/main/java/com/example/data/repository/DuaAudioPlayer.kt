package com.example.data.repository

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.data.model.AppLanguage
import com.example.data.model.Dua
import com.example.localization.AppLocalization
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * Genuine Qari / reciter playback manager for Quranic & Prophetic Duas.
 *
 * Strictly forbids synthesized beeps (ToneGenerator) or robotic device TTS when
 * genuine Qari audio is missing (`hasGenuineAudio == false`). When a dua has no
 * genuine Qari recording, an explicit unavailable message is surfaced and playback
 * never starts.
 */
object DuaAudioPlayer {

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    private val mainHandler = Handler(Looper.getMainLooper())
    private val playbackSessionId = AtomicLong(0L)
    private var mediaPlayer: MediaPlayer? = null

    private val _currentDuaId = MutableStateFlow<String?>(null)
    val currentDuaId: StateFlow<String?> = _currentDuaId.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _repeatCount = MutableStateFlow(1)
    val repeatCount: StateFlow<Int> = _repeatCount.asStateFlow()

    private val _currentLoop = MutableStateFlow(1)
    val currentLoop: StateFlow<Int> = _currentLoop.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isDownloadingPack = MutableStateFlow(false)
    val isDownloadingPack: StateFlow<Boolean> = _isDownloadingPack.asStateFlow()

    private val _offlinePackProgress = MutableStateFlow(0f)
    val offlinePackProgress: StateFlow<Float> = _offlinePackProgress.asStateFlow()

    private val _cachedTrackCount = MutableStateFlow(0)
    val cachedTrackCount: StateFlow<Int> = _cachedTrackCount.asStateFlow()

    private var activeContext: Context? = null
    private var activeSources: List<String> = emptyList()
    private var activeSourceIndex: Int = 0
    private var isPaused: Boolean = false

    fun init(context: Context) {
        activeContext = context.applicationContext
        refreshCachedTrackCount(context.applicationContext)
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun setRepeatCount(count: Int) {
        if (count in 1..100) {
            _repeatCount.value = count
        }
    }

    private fun cacheDir(context: Context): File {
        val dir = File(context.filesDir, "dua_qari_audio")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun cachedFileForUrl(context: Context, url: String): File {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(url.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
            .take(24)
        return File(cacheDir(context), "$digest.mp3")
    }

    private fun refreshCachedTrackCount(context: Context) {
        val count = cacheDir(context).listFiles()?.count { it.isFile && it.length() > 1024L } ?: 0
        _cachedTrackCount.value = count
    }

    private fun resolvedSources(dua: Dua): List<String> {
        if (dua.audioSources.isNotEmpty()) return dua.audioSources
        val asset = dua.audioAssetPath?.trim().orEmpty()
        return if (asset.isNotEmpty()) listOf("asset:$asset") else emptyList()
    }

    fun isDuaOfflineReady(context: Context, dua: Dua): Boolean {
        if (!dua.hasGenuineAudio) return false
        val sources = resolvedSources(dua)
        if (sources.isEmpty()) return false
        return sources.all { src ->
            if (src.startsWith("asset:")) {
                val relPath = src.removePrefix("asset:")
                try {
                    context.assets.openFd(relPath).use { it.length > 0 }
                } catch (_: Exception) {
                    try {
                        context.assets.open(relPath).use { it.available() > 0 }
                    } catch (_: Exception) {
                        false
                    }
                }
            } else {
                val f = cachedFileForUrl(context, src)
                f.exists() && f.length() > 1024L
            }
        }
    }

    suspend fun downloadAllGenuineDuaAudios(
        context: Context,
        duas: List<Dua>
    ): Result<Int> = withContext(Dispatchers.IO) {
        val appCtx = context.applicationContext
        val remoteUrls = duas
            .filter { it.hasGenuineAudio }
            .flatMap { resolvedSources(it) }
            .filter { !it.startsWith("asset:") && it.startsWith("http") }
            .distinct()

        if (remoteUrls.isEmpty()) {
            refreshCachedTrackCount(appCtx)
            _offlinePackProgress.value = 1f
            return@withContext Result.success(0)
        }

        _isDownloadingPack.value = true
        _offlinePackProgress.value = 0f
        var downloaded = 0
        try {
            remoteUrls.forEachIndexed { idx, url ->
                val target = cachedFileForUrl(appCtx, url)
                if (!target.exists() || target.length() <= 1024L) {
                    val req = Request.Builder()
                        .url(url)
                        .header("User-Agent", "Noor-e-Hidayat/1.0")
                        .get()
                        .build()
                    client.newCall(req).execute().use { resp ->
                        if (resp.isSuccessful) {
                            val bytes = resp.body?.bytes()
                            if (bytes != null && bytes.size > 1024) {
                                val tmp = File(target.parentFile, "${target.name}.tmp")
                                tmp.writeBytes(bytes)
                                if (target.exists()) target.delete()
                                if (tmp.renameTo(target)) {
                                    downloaded++
                                }
                            }
                        }
                    }
                } else {
                    downloaded++
                }
                _offlinePackProgress.value = (idx + 1).toFloat() / remoteUrls.size.toFloat()
            }
            refreshCachedTrackCount(appCtx)
            Result.success(downloaded)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            _isDownloadingPack.value = false
        }
    }

    fun playDua(context: Context, dua: Dua, repeatTarget: Int = 1) {
        val appCtx = context.applicationContext
        activeContext = appCtx
        clearError()

        if (!dua.hasGenuineAudio) {
            stop()
            val lang = UserPreferencesRepository(appCtx).language.value
            _errorMessage.value = AppLocalization.get("dua_audio_not_available", lang)
            return
        }

        val sources = resolvedSources(dua)
        if (sources.isEmpty()) {
            stop()
            val lang = UserPreferencesRepository(appCtx).language.value
            _errorMessage.value = AppLocalization.get("dua_audio_not_available", lang)
            return
        }

        if (_currentDuaId.value == dua.id && _isPlaying.value) {
            pause()
            return
        }
        if (_currentDuaId.value == dua.id && isPaused && mediaPlayer != null) {
            resume()
            return
        }

        stop()
        _currentDuaId.value = dua.id
        _repeatCount.value = repeatTarget.coerceAtLeast(1)
        _currentLoop.value = 1
        activeSources = sources
        activeSourceIndex = 0
        isPaused = false
        startCurrentSource()
    }

    private fun startCurrentSource() {
        val ctx = activeContext ?: return
        val source = activeSources.getOrNull(activeSourceIndex) ?: run {
            stop()
            return
        }

        val sessionId = playbackSessionId.incrementAndGet()
        AudioFocusCoordinator.onPlaybackStarting(ctx, "dua") { pause() }
        releasePlayer()
        _isBuffering.value = true
        _isPlaying.value = false

        val player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnPreparedListener { mp ->
                mainHandler.post {
                    if (playbackSessionId.get() != sessionId || mediaPlayer !== mp) return@post
                    _isBuffering.value = false
                    _isPlaying.value = true
                    isPaused = false
                    try {
                        mp.start()
                    } catch (e: Exception) {
                        _errorMessage.value = e.message ?: "Unable to start playback."
                        stop()
                    }
                }
            }
            setOnCompletionListener { mp ->
                mainHandler.post {
                    if (playbackSessionId.get() != sessionId || mediaPlayer !== mp) return@post
                    handleSegmentCompleted()
                }
            }
            setOnErrorListener { mp, what, extra ->
                mainHandler.post {
                    if (playbackSessionId.get() != sessionId || mediaPlayer !== mp) return@post
                    AudioFocusCoordinator.onPlaybackStopped("dua")
                    _isBuffering.value = false
                    _isPlaying.value = false
                    _errorMessage.value = "Audio stream unavailable (code $what/$extra)."
                    _currentDuaId.value = null
                }
                true
            }
        }
        mediaPlayer = player

        try {
            if (source.startsWith("asset:")) {
                val relPath = source.removePrefix("asset:")
                val afd = ctx.assets.openFd(relPath)
                try {
                    player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                } finally {
                    runCatching { afd.close() }
                }
                player.prepareAsync()
            } else {
                val localCache = cachedFileForUrl(ctx, source)
                if (localCache.exists() && localCache.length() > 1024L) {
                    player.setDataSource(localCache.absolutePath)
                } else {
                    player.setDataSource(source)
                }
                player.prepareAsync()
            }
        } catch (e: Exception) {
            AudioFocusCoordinator.onPlaybackStopped("dua")
            releasePlayer()
            _isBuffering.value = false
            _isPlaying.value = false
            _errorMessage.value = e.message ?: "Unable to open audio recording."
            _currentDuaId.value = null
        }
    }

    private fun handleSegmentCompleted() {
        if (activeSourceIndex + 1 < activeSources.size) {
            activeSourceIndex++
            startCurrentSource()
        } else if (_currentLoop.value < _repeatCount.value) {
            _currentLoop.value = _currentLoop.value + 1
            activeSourceIndex = 0
            startCurrentSource()
        } else {
            stop()
        }
    }

    fun pause() {
        AudioFocusCoordinator.onPlaybackStopped("dua")
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    isPaused = true
                }
            }
        } catch (_: Exception) {}
        _isPlaying.value = false
        _isBuffering.value = false
    }

    fun resume() {
        activeContext?.let { ctx ->
            AudioFocusCoordinator.onPlaybackStarting(ctx, "dua") { pause() }
        }
        try {
            mediaPlayer?.let {
                it.start()
                isPaused = false
                _isPlaying.value = true
                return
            }
        } catch (_: Exception) {}
        if (_currentDuaId.value != null && activeSources.isNotEmpty()) {
            startCurrentSource()
        }
    }

    fun stop() {
        playbackSessionId.incrementAndGet()
        mainHandler.removeCallbacksAndMessages(null)
        AudioFocusCoordinator.onPlaybackStopped("dua")
        releasePlayer()
        _isPlaying.value = false
        _isBuffering.value = false
        _currentDuaId.value = null
        _currentLoop.value = 1
        activeSources = emptyList()
        activeSourceIndex = 0
        isPaused = false
    }

    private fun releasePlayer() {
        try {
            mediaPlayer?.reset()
        } catch (_: Exception) {}
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
    }
}
