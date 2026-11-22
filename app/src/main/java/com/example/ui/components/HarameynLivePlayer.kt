package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.data.live.LivePlayerTransport
import com.example.data.live.ResolvedLiveStreamSource
import com.example.data.model.AppLanguage
import com.example.data.repository.AudioFocusCoordinator
import com.example.localization.AppLocalization
import com.example.ui.components.FullscreenVideoController.Companion.findActivity
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean

object HarameynLivePlayerConfig {
    const val LIVE_JS_BRIDGE_NAME = "NoorLiveBridge"
    const val YOUTUBE_LIVE_START_TIMEOUT_MS = 10_000L
    const val HLS_LIVE_START_TIMEOUT_MS = 12_000L
    const val MAX_HLS_AUTO_RETRIES = 1
    const val HLS_RETRY_DELAY_MS = 1_500L

    /**
     * Injected into YouTube live embeds to detect:
     * 1. Real playback errors / unavailable live streams / sign-in walls -> immediate automatic fallback.
     * 2. Active live video playback (`!paused && readyState >= 2 && currentTime > 0` with live indicator)
     *    -> confirms LIVE only when playback actually starts.
     * 3. Unexpected playback stops / stalls -> signals reconnecting or failure.
     */
    internal const val LIVE_PLAYBACK_MONITOR_JS = """
        (function() {
          if (window.__noorLiveMonitorAttached) return;
          window.__noorLiveMonitorAttached = true;
          var confirmedLive = false;
          var failed = false;
          var lastCurrentTime = -1;
          var stallTicks = 0;

          function reportError(code, reason) {
            if (failed) return;
            failed = true;
            if (window.NoorLiveBridge && window.NoorLiveBridge.onLiveError) {
              window.NoorLiveBridge.onLiveError(code, reason || 'Playback error');
            }
          }

          function reportLive() {
            if (failed || confirmedLive) return;
            confirmedLive = true;
            if (window.NoorLiveBridge && window.NoorLiveBridge.onLiveConfirmed) {
              window.NoorLiveBridge.onLiveConfirmed();
            }
          }

          function reportBuffering() {
            if (failed || !confirmedLive) return;
            if (window.NoorLiveBridge && window.NoorLiveBridge.onLiveBuffering) {
              window.NoorLiveBridge.onLiveBuffering();
            }
          }

          function inspectPlayer() {
            if (failed) return true;
            try {
              var bodyText = (document.body && document.body.innerText) ? document.body.innerText : '';
              var errEl = document.querySelector('.ytp-error, .ytp-error-content-wrap-reason, .ytp-offline-slate');
              if ((errEl && errEl.offsetParent !== null) ||
                  /Video unavailable|This live stream recording is not available|This live stream is unavailable|This live stream has ended|Live stream offline|Sign in to confirm|Playback on other websites has been disabled|Private video|This video is unavailable/i.test(bodyText)) {
                var m = bodyText.match(/\b(100|101|150|152|153)\b/);
                var code = m ? parseInt(m[1], 10) : 150;
                reportError(code, 'YouTube live stream unavailable or restricted');
                return true;
              }

              var v = document.querySelector('video');
              if (v) {
                if (v.error) {
                  reportError(v.error.code || 500, 'Video media error');
                  return true;
                }
                var playerEl = document.querySelector('.html5-video-player');
                var playerHasLiveClass = !!(playerEl && (playerEl.classList.contains('ytp-live') || playerEl.classList.contains('ytp-live-dvr')));
                var liveBadge = document.querySelector('.ytp-live-badge');
                var hasVisibleLiveBadge = !!(liveBadge && liveBadge.offsetParent !== null && getComputedStyle(liveBadge).display !== 'none');
                var isLiveWindow = (!isFinite(v.duration) || v.duration === 0 || playerHasLiveClass || hasVisibleLiveBadge);

                if (!v.paused && v.readyState >= 2 && v.currentTime > 0 && isLiveWindow) {
                  stallTicks = 0;
                  reportLive();
                  lastCurrentTime = v.currentTime;
                } else if (confirmedLive) {
                  if (v.ended) {
                    reportError(410, 'Live broadcast ended');
                    return true;
                  }
                  if (!v.paused && v.currentTime === lastCurrentTime) {
                    stallTicks++;
                    if (stallTicks === 3) {
                      reportBuffering();
                    } else if (stallTicks >= 8) {
                      reportError(408, 'Live stream stalled');
                      return true;
                    }
                  } else {
                    stallTicks = 0;
                    lastCurrentTime = v.currentTime;
                  }
                }
              }
            } catch (e) {}
            return false;
          }

          setTimeout(inspectPlayer, 800);
          var timer = setInterval(function() {
            if (inspectPlayer()) {
              clearInterval(timer);
            }
          }, 1200);
        })();
    """
}

private class LiveYouTubeJsBridge(
    private val onConfirmed: () -> Unit,
    private val onBuffering: () -> Unit,
    private val onError: (Int, String) -> Unit
) {
    @JavascriptInterface
    fun onLiveConfirmed() {
        onConfirmed()
    }

    @JavascriptInterface
    fun onLiveBuffering() {
        onBuffering()
    }

    @JavascriptInterface
    fun onLiveError(code: Int, reason: String?) {
        onError(code, reason ?: "Playback error ($code)")
    }
}

/**
 * Dedicated YouTube Live Stream player for Makkah & Madinah (Source 1 Official YouTube &
 * Source 3 Haramain Voice verified embed).
 *
 * Never reports LIVE on mere page load; confirms LIVE only when real active live playback is
 * detected, and immediately notifies [onPlaybackFailed] on errors, embed restrictions, renderer
 * crashes, or startup timeout so the ViewModel can seamlessly fall back to the next source.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun InAppYouTubeLivePlayer(
    resolvedSource: ResolvedLiveStreamSource,
    language: AppLanguage,
    onLivePlaybackConfirmed: () -> Unit,
    onReconnecting: () -> Unit,
    onPlaybackFailed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val fullscreenController = LocalFullscreenVideoController.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainHandler = remember { Handler(Looper.getMainLooper()) }

    val latestOnConfirmed by rememberUpdatedState(onLivePlaybackConfirmed)
    val latestOnReconnecting by rememberUpdatedState(onReconnecting)
    val latestOnFailed by rememberUpdatedState(onPlaybackFailed)

    var confirmedLive by remember(resolvedSource) { mutableStateOf(false) }
    var failed by remember(resolvedSource) { mutableStateOf(false) }
    var activeWebView by remember(resolvedSource) { mutableStateOf<WebView?>(null) }
    var activeDisposedFlag by remember(resolvedSource) { mutableStateOf<AtomicBoolean?>(null) }

    val softwareMode = remember {
        YouTubeIFramePlayerConfig.shouldUseSoftwareLayer(localFallback = false)
    }

    val embedUrl = remember(resolvedSource, softwareMode) {
        val autoplay = YouTubeIFramePlayerConfig.effectiveAutoplay(
            requestedAutoplay = true,
            softwareLayerActive = softwareMode
        )
        when (resolvedSource.transport) {
            LivePlayerTransport.YOUTUBE_VIDEO_EMBED -> {
                val vid = resolvedSource.youtubeVideoId.orEmpty()
                YouTubeIFramePlayerConfig.buildDirectEmbedUrl(
                    videoId = vid,
                    autoplay = autoplay,
                    useNoCookie = false
                )
            }
            LivePlayerTransport.YOUTUBE_CHANNEL_LIVE_EMBED -> {
                val cid = resolvedSource.youtubeChannelId ?: resolvedSource.config.officialChannelId
                YouTubeIFramePlayerConfig.buildChannelLiveEmbedUrl(
                    channelId = cid,
                    autoplay = autoplay,
                    useNoCookie = false
                )
            }
            LivePlayerTransport.HLS_NATIVE -> ""
        }
    }

    // Strict connection timeout -> triggers automatic fallback if live playback never starts
    LaunchedEffect(resolvedSource, confirmedLive, failed) {
        if (!confirmedLive && !failed) {
            delay(HarameynLivePlayerConfig.YOUTUBE_LIVE_START_TIMEOUT_MS)
            if (!confirmedLive && !failed) {
                failed = true
                AudioFocusCoordinator.onPlaybackStopped("live_video")
                latestOnFailed("YouTube live broadcast timed out before playback started.")
            }
        }
    }

    val latestWebView by rememberUpdatedState(activeWebView)
    val latestDisposedFlag by rememberUpdatedState(activeDisposedFlag)

    DisposableEffect(lifecycleOwner, resolvedSource) {
        val pauseJs =
            "(function(){try{document.querySelectorAll('video,audio').forEach(function(m){try{m.pause();}catch(e){}});}catch(e){}})();"
        val observer = LifecycleEventObserver { _, event ->
            val wv = latestWebView
            val disposed = latestDisposedFlag?.get() == true
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    AudioFocusCoordinator.onPlaybackStopped("live_video")
                    if (wv != null && !disposed) {
                        runCatching {
                            if (wv.isAttachedToWindow) wv.evaluateJavascript(pauseJs, null)
                            wv.onPause()
                        }
                    }
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (wv != null && !disposed) {
                        runCatching { wv.onResume() }
                    }
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mainHandler.removeCallbacksAndMessages(null)
            AudioFocusCoordinator.onPlaybackStopped("live_video")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (!failed && embedUrl.isNotBlank()) {
            val isDisposed = remember(resolvedSource) { AtomicBoolean(false) }

            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(android.graphics.Color.BLACK)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, false)
                        }

                        val webViewInstance = this
                        val effectiveAutoplay = YouTubeIFramePlayerConfig.effectiveAutoplay(
                            requestedAutoplay = true,
                            softwareLayerActive = softwareMode
                        )
                        setLayerType(
                            if (softwareMode) View.LAYER_TYPE_SOFTWARE else View.LAYER_TYPE_NONE,
                            null
                        )

                        runCatching {
                            CookieManager.getInstance().apply {
                                setAcceptCookie(true)
                                setAcceptThirdPartyCookies(webViewInstance, true)
                            }
                        }

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            loadsImagesAutomatically = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            mediaPlaybackRequiresUserGesture = !effectiveAutoplay
                            cacheMode = WebSettings.LOAD_DEFAULT
                            allowContentAccess = true
                            allowFileAccess = false
                            setSupportMultipleWindows(false)
                            javaScriptCanOpenWindowsAutomatically = false
                            setSupportZoom(false)
                            builtInZoomControls = false
                            displayZoomControls = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                offscreenPreRaster = false
                            }
                        }

                        addJavascriptInterface(
                            LiveYouTubeJsBridge(
                                onConfirmed = {
                                    mainHandler.post {
                                        if (!isDisposed.get() && !failed) {
                                            confirmedLive = true
                                            AudioFocusCoordinator.onPlaybackStarting(
                                                context = context,
                                                ownerKey = "live_video"
                                            ) {
                                                mainHandler.post {
                                                    if (!isDisposed.get()) {
                                                        runCatching {
                                                            webViewInstance.evaluateJavascript(
                                                                "(function(){try{document.querySelectorAll('video,audio').forEach(function(m){try{m.pause();}catch(e){}});}catch(e){}})();",
                                                                null
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            latestOnConfirmed()
                                        }
                                    }
                                },
                                onBuffering = {
                                    mainHandler.post {
                                        if (!isDisposed.get() && !failed && confirmedLive) {
                                            latestOnReconnecting()
                                        }
                                    }
                                },
                                onError = { code, reason ->
                                    mainHandler.post {
                                        if (!isDisposed.get() && !failed) {
                                            failed = true
                                            AudioFocusCoordinator.onPlaybackStopped("live_video")
                                            latestOnFailed("$reason (code $code)")
                                        }
                                    }
                                }
                            ),
                            HarameynLivePlayerConfig.LIVE_JS_BRIDGE_NAME
                        )

                        webChromeClient = object : WebChromeClient() {
                            override fun getDefaultVideoPoster(): Bitmap =
                                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

                            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                                if (!isDisposed.get() && view != null && fullscreenController != null) {
                                    fullscreenController.enterFullscreen(view, callback, activity)
                                } else {
                                    super.onShowCustomView(view, callback)
                                }
                            }

                            override fun onHideCustomView() {
                                if (fullscreenController != null) {
                                    fullscreenController.onWebChromeHideCustomView()
                                } else {
                                    super.onHideCustomView()
                                }
                            }
                        }

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val targetUri = request?.url ?: return false
                                val urlStr = targetUri.toString()
                                if (urlStr.contains("accounts.google.com") || urlStr.contains("ServiceLogin")) {
                                    mainHandler.post {
                                        if (!isDisposed.get() && !failed) {
                                            failed = true
                                            AudioFocusCoordinator.onPlaybackStopped("live_video")
                                            latestOnFailed("YouTube sign-in wall blocked embedded live stream.")
                                        }
                                    }
                                    return true
                                }
                                if (urlStr.contains("/watch") || urlStr.contains("youtu.be/")) {
                                    runCatching { ctx.startActivity(Intent(Intent.ACTION_VIEW, targetUri)) }
                                    return true
                                }
                                return false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (!isDisposed.get() && request?.isForMainFrame == true) {
                                    val code = error?.errorCode ?: -1
                                    mainHandler.post {
                                        if (!isDisposed.get() && !failed) {
                                            failed = true
                                            AudioFocusCoordinator.onPlaybackStopped("live_video")
                                            latestOnFailed("Network error loading YouTube live player ($code)")
                                        }
                                    }
                                }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                if (!isDisposed.get()) {
                                    mainHandler.post {
                                        if (!isDisposed.get() && !failed) {
                                            runCatching {
                                                view?.evaluateJavascript(
                                                    HarameynLivePlayerConfig.LIVE_PLAYBACK_MONITOR_JS,
                                                    null
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            override fun onRenderProcessGone(
                                view: WebView?,
                                detail: RenderProcessGoneDetail?
                            ): Boolean {
                                AudioFocusCoordinator.onPlaybackStopped("live_video")
                                if (activeWebView === view) {
                                    activeWebView = null
                                    activeDisposedFlag = null
                                }
                                runCatching { view?.removeJavascriptInterface(HarameynLivePlayerConfig.LIVE_JS_BRIDGE_NAME) }
                                YouTubeIFramePlayerConfig.disposeWebViewSafely(
                                    webView = view,
                                    disposedFlag = isDisposed,
                                    rendererAlreadyGone = true
                                )
                                val didCrash = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    detail?.didCrash() == true
                                } else {
                                    true
                                }
                                mainHandler.post {
                                    if (didCrash) {
                                        YouTubeIFramePlayerConfig.markSessionSoftwareFallback()
                                    }
                                    if (fullscreenController?.isFullscreen == true) {
                                        fullscreenController.exitFullscreen()
                                    }
                                    if (!failed) {
                                        failed = true
                                        latestOnFailed("WebView renderer terminated — switching to native HLS fallback.")
                                    }
                                }
                                return true
                            }
                        }

                        activeWebView = this
                        activeDisposedFlag = isDisposed
                        val refererHeaders = YouTubeIFramePlayerConfig.buildRefererHeaders(ctx.packageName)
                        val loadInitiated = AtomicBoolean(false)
                        val startNav = {
                            if (!isDisposed.get() && loadInitiated.compareAndSet(false, true)) {
                                runCatching { loadUrl(embedUrl, refererHeaders) }
                            }
                        }
                        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                            override fun onViewAttachedToWindow(v: View) {
                                v.removeOnAttachStateChangeListener(this)
                                v.post { startNav() }
                            }
                            override fun onViewDetachedFromWindow(v: View) = Unit
                        })
                        post {
                            if (isAttachedToWindow) startNav()
                        }
                    }
                },
                onRelease = { wv ->
                    if (activeWebView === wv) {
                        activeWebView = null
                        activeDisposedFlag = null
                    }
                    if (fullscreenController?.isFullscreen == true) {
                        fullscreenController.exitFullscreen()
                    }
                    runCatching { wv.removeJavascriptInterface(HarameynLivePlayerConfig.LIVE_JS_BRIDGE_NAME) }
                    YouTubeIFramePlayerConfig.disposeWebViewSafely(
                        webView = wv,
                        disposedFlag = isDisposed,
                        rendererAlreadyGone = false
                    )
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (!confirmedLive && !failed) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .testTag("live_player_connecting_overlay")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = AppLocalization.get("live_phase_connecting", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.92f)
                    )
                }
            }
        }
    }
}

/**
 * Native AndroidX Media3 ExoPlayer HLS Live Player (used for Quran Hayat's official Saudi Quran TV
 * and Saudi Sunnah TV Globecast/Akamai HLS broadcasts).
 *
 * - Zero WebView overhead and immune to Chromium renderer crashes.
 * - Confirms LIVE only when `playbackState == Player.STATE_READY && isPlaying && isCurrentMediaItemLive`.
 * - Bounded auto-retry (max 1) on transient network blips before automatically falling back to Source 3.
 * - Releases ExoPlayer cleanly on lifecycle dispose or source change.
 */
@OptIn(UnstableApi::class)
@Composable
fun InAppHlsLivePlayer(
    hlsUrl: String,
    language: AppLanguage,
    onLivePlaybackConfirmed: () -> Unit,
    onReconnecting: () -> Unit,
    onPlaybackFailed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val fullscreenController = LocalFullscreenVideoController.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val latestOnConfirmed by rememberUpdatedState(onLivePlaybackConfirmed)
    val latestOnReconnecting by rememberUpdatedState(onReconnecting)
    val latestOnFailed by rememberUpdatedState(onPlaybackFailed)

    var confirmedLive by remember(hlsUrl) { mutableStateOf(false) }
    var failed by remember(hlsUrl) { mutableStateOf(false) }
    var retryCount by remember(hlsUrl) { mutableIntStateOf(0) }
    var pendingRetryTrigger by remember(hlsUrl) { mutableIntStateOf(0) }

    val exoPlayer = remember(hlsUrl) {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setConnectTimeoutMs(10_000)
            .setReadTimeoutMs(10_000)
            .setAllowCrossProtocolRedirects(true)
            .setUserAgent("NoorEHidayatAndroid/1.0 (ExoPlayer)")

        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(hlsUrl))
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        val hlsSource = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .createMediaSource(mediaItem)

        ExoPlayer.Builder(context)
            .build()
            .apply {
                setMediaSource(hlsSource)
                playWhenReady = true
                prepare()
            }
    }

    // Bounded single reconnect delay when a transient HLS error occurs
    LaunchedEffect(hlsUrl, pendingRetryTrigger) {
        if (pendingRetryTrigger > 0 && !failed) {
            delay(HarameynLivePlayerConfig.HLS_RETRY_DELAY_MS)
            if (!failed) {
                runCatching {
                    exoPlayer.seekToDefaultPosition()
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                }
            }
        }
    }

    // Strict startup timeout so a hung HLS connection fails over cleanly
    LaunchedEffect(hlsUrl, confirmedLive, failed) {
        if (!confirmedLive && !failed) {
            delay(HarameynLivePlayerConfig.HLS_LIVE_START_TIMEOUT_MS)
            if (!confirmedLive && !failed) {
                failed = true
                AudioFocusCoordinator.onPlaybackStopped("live_video")
                runCatching { exoPlayer.stop() }
                latestOnFailed("HLS live broadcast timed out before playback started.")
            }
        }
    }

    DisposableEffect(exoPlayer, lifecycleOwner) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (failed) return
                when (playbackState) {
                    Player.STATE_READY -> {
                        if (exoPlayer.isPlaying) {
                            if (exoPlayer.isCurrentMediaItemLive) {
                                confirmedLive = true
                                retryCount = 0
                                AudioFocusCoordinator.onPlaybackStarting(context, "live_video") {
                                    runCatching { exoPlayer.pause() }
                                }
                                latestOnConfirmed()
                            } else {
                                failed = true
                                AudioFocusCoordinator.onPlaybackStopped("live_video")
                                latestOnFailed("Stream is not an active live broadcast.")
                            }
                        }
                    }
                    Player.STATE_BUFFERING -> {
                        if (confirmedLive) {
                            latestOnReconnecting()
                        }
                    }
                    Player.STATE_ENDED -> {
                        if (retryCount < HarameynLivePlayerConfig.MAX_HLS_AUTO_RETRIES) {
                            retryCount++
                            latestOnReconnecting()
                            pendingRetryTrigger++
                        } else {
                            failed = true
                            AudioFocusCoordinator.onPlaybackStopped("live_video")
                            latestOnFailed("HLS live stream ended unexpectedly.")
                        }
                    }
                    else -> Unit
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (failed) return
                if (isPlaying && exoPlayer.playbackState == Player.STATE_READY) {
                    if (exoPlayer.isCurrentMediaItemLive) {
                        confirmedLive = true
                        retryCount = 0
                        AudioFocusCoordinator.onPlaybackStarting(context, "live_video") {
                            runCatching { exoPlayer.pause() }
                        }
                        latestOnConfirmed()
                    } else {
                        failed = true
                        AudioFocusCoordinator.onPlaybackStopped("live_video")
                        latestOnFailed("HLS stream is a recorded file, not a live broadcast.")
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                if (failed) return
                if (retryCount < HarameynLivePlayerConfig.MAX_HLS_AUTO_RETRIES) {
                    retryCount++
                    latestOnReconnecting()
                    pendingRetryTrigger++
                } else {
                    failed = true
                    AudioFocusCoordinator.onPlaybackStopped("live_video")
                    latestOnFailed("HLS playback error: ${error.errorCodeName}")
                }
            }
        }

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP -> {
                    AudioFocusCoordinator.onPlaybackStopped("live_video")
                    runCatching { exoPlayer.pause() }
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (!failed) {
                        runCatching {
                            if (exoPlayer.playbackState == Player.STATE_IDLE) {
                                exoPlayer.prepare()
                            }
                            exoPlayer.seekToDefaultPosition()
                            exoPlayer.playWhenReady = true
                        }
                    }
                }
                else -> Unit
            }
        }

        exoPlayer.addListener(listener)
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            exoPlayer.removeListener(listener)
            AudioFocusCoordinator.onPlaybackStopped("live_video")
            if (fullscreenController?.isFullscreen == true) {
                fullscreenController.exitFullscreen()
            }
            runCatching { exoPlayer.release() }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    player = exoPlayer
                    useController = true
                    keepScreenOn = true
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                    setFullscreenButtonClickListener { enterFs ->
                        val fs = fullscreenController ?: return@setFullscreenButtonClickListener
                        if (enterFs && !fs.isFullscreen) {
                            val fsPlayerView = PlayerView(ctx).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                player = exoPlayer
                                useController = true
                                keepScreenOn = true
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                setFullscreenButtonClickListener {
                                    fs.exitFullscreen()
                                }
                            }
                            player = null
                            fs.enterFullscreen(
                                fsPlayerView,
                                WebChromeClient.CustomViewCallback {
                                    fsPlayerView.player = null
                                    player = exoPlayer
                                },
                                activity
                            )
                        } else if (!enterFs && fs.isFullscreen) {
                            fs.exitFullscreen()
                        }
                    }
                }
            },
            update = { view ->
                if (fullscreenController?.isFullscreen != true && view.player !== exoPlayer) {
                    view.player = exoPlayer
                }
            },
            onRelease = { view ->
                view.player = null
            },
            modifier = Modifier
                .fillMaxSize()
                .testTag("hls_live_player_view")
        )

        if (!confirmedLive && !failed) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .testTag("live_player_connecting_overlay")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = AppLocalization.get("live_phase_connecting", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.92f)
                    )
                }
            }
        }
    }
}
