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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.model.AppLanguage
import com.example.data.repository.AudioFocusCoordinator
import com.example.localization.AppLocalization
import com.example.ui.components.FullscreenVideoController.Companion.findActivity
import kotlinx.coroutines.delay
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

object YouTubeIFramePlayerConfig {
    const val APP_EMBED_ORIGIN = "https://noor-e-hidayat.app"
    const val JS_BRIDGE_NAME = "NoorBridge"
    const val MAX_AUTO_RECOVERY_ATTEMPTS = 1
    const val LOAD_TIMEOUT_MS = 12_000L

    fun sanitizeVideoId(raw: String): String {
        val candidate = when {
            raw.contains("watch?v=") -> raw.substringAfter("watch?v=").substringBefore("&")
            raw.contains("embed/") -> raw.substringAfter("embed/").substringBefore("?")
            raw.contains("youtu.be/") -> raw.substringAfter("youtu.be/").substringBefore("?")
            else -> raw.trim()
        }
        return candidate.filter { it.isLetterOrDigit() || it == '_' || it == '-' }.take(16)
    }

    fun buildOfficialEmbedUrl(videoId: String, autoplay: Boolean = true): String {
        val cleanId = sanitizeVideoId(videoId)
        val autoplayInt = if (autoplay) 1 else 0
        return "https://www.youtube.com/embed/$cleanId?enablejsapi=1&autoplay=$autoplayInt&playsinline=1&rel=0&fs=1&origin=$APP_EMBED_ORIGIN"
    }

    fun buildDirectEmbedUrl(
        videoId: String,
        autoplay: Boolean = true,
        useNoCookie: Boolean = false
    ): String {
        val cleanId = sanitizeVideoId(videoId)
        val autoplayInt = if (autoplay) 1 else 0
        val host = if (useNoCookie) "https://www.youtube-nocookie.com" else "https://www.youtube.com"
        return "$host/embed/$cleanId?autoplay=$autoplayInt&playsinline=1&rel=0&fs=1&modestbranding=1"
    }

    fun buildRefererHeaders(packageName: String): Map<String, String> {
        val safePkg = packageName.trim().ifBlank { "com.aistudio.noorehidayat" }
        return mapOf("Referer" to "https://$safePkg/")
    }

    internal const val ERROR_MONITOR_JS = """
        (function() {
          if (window.__noorMonitorAttached) return;
          window.__noorMonitorAttached = true;
          function reportErr(code) {
            if (window.NoorBridge && window.NoorBridge.onPlayerError) {
              window.NoorBridge.onPlayerError(code);
            }
          }
          function checkPlayerError() {
            var errEl = document.querySelector('.ytp-error, .ytp-error-content-wrap-reason');
            if (errEl && errEl.offsetParent !== null) {
              var txt = (document.body && document.body.innerText) ? document.body.innerText : '';
              var m = txt.match(/\b(100|101|150|152|153)\b/);
              var code = m ? parseInt(m[1], 10) : 150;
              reportErr(code);
              return true;
            }
            return false;
          }
          setTimeout(checkPlayerError, 1000);
          var errTimer = setInterval(function() {
            if (checkPlayerError()) {
              clearInterval(errTimer);
            }
          }, 2000);
        })();
    """

    fun buildIFrameApiHtml(videoId: String, autoplay: Boolean): String {
        val cleanId = sanitizeVideoId(videoId)
        val autoplayInt = if (autoplay) 1 else 0
        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
              <meta name="referrer" content="strict-origin-when-cross-origin" />
              <style>
                html, body, #player {
                  margin: 0;
                  padding: 0;
                  width: 100%;
                  height: 100%;
                  background-color: #000000;
                  overflow: hidden;
                }
              </style>
            </head>
            <body>
              <div id="player"></div>
              <script>
                var playerReadyFired = false;
                function reportError(code) {
                  if (window.NoorBridge && window.NoorBridge.onPlayerError) {
                    window.NoorBridge.onPlayerError(code);
                  }
                }
                setTimeout(function() {
                  if (!playerReadyFired && (typeof YT === 'undefined' || !YT.Player)) {
                    reportError(-2);
                  }
                }, 9000);
                var player;
                function onYouTubeIframeAPIReady() {
                  try {
                    player = new YT.Player('player', {
                      width: '100%',
                      height: '100%',
                      host: 'https://www.youtube.com',
                      videoId: '$cleanId',
                      playerVars: {
                        autoplay: $autoplayInt,
                        controls: 1,
                        playsinline: 1,
                        enablejsapi: 1,
                        fs: 1,
                        rel: 0,
                        modestbranding: 1,
                        origin: '${APP_EMBED_ORIGIN}'
                      },
                      events: {
                        'onReady': function() {
                          playerReadyFired = true;
                          if (window.NoorBridge && window.NoorBridge.onPlayerReady) {
                            window.NoorBridge.onPlayerReady();
                          }
                        },
                        'onError': function(e) {
                          playerReadyFired = true;
                          reportError(e && e.data ? e.data : 150);
                        }
                      }
                    });
                  } catch (err) {
                    reportError(150);
                  }
                }
              </script>
              <script src="https://www.youtube.com/iframe_api" onerror="reportError(-2)"></script>
            </body>
            </html>
        """.trimIndent()
    }

    @Volatile
    var sessionSoftwareFallbackRequired: Boolean = false
        private set

    fun markSessionSoftwareFallback() {
        sessionSoftwareFallbackRequired = true
    }

    internal fun resetSessionFallbackForTest() {
        sessionSoftwareFallbackRequired = false
    }

    fun hasDrmRenderNode(): Boolean {
        return runCatching {
            val driDir = File("/dev/dri")
            driDir.exists() && driDir.listFiles()?.any { it.name.startsWith("renderD") } == true
        }.getOrDefault(false)
    }

    fun isSoftwareGpuEnvironment(
        hardware: String = Build.HARDWARE.orEmpty(),
        fingerprint: String = Build.FINGERPRINT.orEmpty(),
        product: String = Build.PRODUCT.orEmpty(),
        model: String = Build.MODEL.orEmpty(),
        hasRenderNode: Boolean = hasDrmRenderNode()
    ): Boolean {
        val hw = hardware.lowercase()
        val fp = fingerprint.lowercase()
        val prod = product.lowercase()
        val mdl = model.lowercase()
        if (fp == "robolectric") return false
        val isVirtualizedEmulator = hw.contains("ranchu") ||
            hw.contains("goldfish") ||
            hw.contains("cutf") ||
            hw.contains("vsoc") ||
            fp.startsWith("generic") ||
            fp.startsWith("unknown") ||
            fp.contains("emulator") ||
            fp.contains("sdk_gphone") ||
            fp.contains("cuttlefish") ||
            prod.contains("sdk_gphone") ||
            prod.contains("emulator") ||
            prod.contains("cuttlefish") ||
            prod.startsWith("cf_") ||
            mdl.contains("emulator") ||
            mdl.contains("android sdk") ||
            mdl.contains("cuttlefish")
        return isVirtualizedEmulator && !hasRenderNode
    }

    fun shouldUseSoftwareLayer(localFallback: Boolean): Boolean {
        return localFallback || sessionSoftwareFallbackRequired || isSoftwareGpuEnvironment()
    }

    fun effectiveAutoplay(requestedAutoplay: Boolean, softwareLayerActive: Boolean): Boolean {
        return requestedAutoplay && !softwareLayerActive
    }

    fun shouldAutoRecoverRenderer(didCrash: Boolean, priorAttempts: Int): Boolean {
        return !didCrash && priorAttempts < MAX_AUTO_RECOVERY_ATTEMPTS
    }

    private val SAFE_TEARDOWN_WEBVIEW_CLIENT = object : WebViewClient() {
        override fun onRenderProcessGone(
            view: WebView?,
            detail: RenderProcessGoneDetail?
        ): Boolean = true
    }

    /**
     * Safely tears down a WebView instance exactly once.
     * Avoids calling loadUrl("about:blank") right before destroy(), which triggers mid-navigation
     * renderer IPC kills, keeps an onRenderProcessGone-safe WebViewClient during destruction,
     * and prevents double-destroy after onRenderProcessGone.
     */
    fun disposeWebViewSafely(
        webView: WebView?,
        disposedFlag: AtomicBoolean,
        rendererAlreadyGone: Boolean
    ): Boolean {
        if (webView == null || !disposedFlag.compareAndSet(false, true)) {
            return false
        }
        try {
            if (!rendererAlreadyGone) {
                webView.onPause()
                webView.stopLoading()
            }
        } catch (_: Exception) {
        }
        try {
            webView.removeJavascriptInterface(JS_BRIDGE_NAME)
        } catch (_: Exception) {
        }
        try {
            webView.webChromeClient = null
            webView.webViewClient = SAFE_TEARDOWN_WEBVIEW_CLIENT
        } catch (_: Exception) {
        }
        try {
            webView.setLayerType(View.LAYER_TYPE_NONE, null)
        } catch (_: Exception) {
        }
        try {
            (webView.parent as? ViewGroup)?.removeView(webView)
            if (!rendererAlreadyGone) {
                webView.clearHistory()
            }
            webView.removeAllViews()
            webView.destroy()
        } catch (_: Exception) {
        }
        return true
    }

    fun formatErrorMessage(
        errorCode: Int,
        isNetworkError: Boolean,
        isRendererCrash: Boolean,
        language: AppLanguage
    ): String {
        return when {
            isRendererCrash -> AppLocalization.get("player_error_renderer", language)
            isNetworkError || errorCode == -2 || errorCode == 408 ->
                AppLocalization.get("player_error_network", language)
            errorCode == 100 -> AppLocalization.get("player_error_not_found", language)
            errorCode == 101 || errorCode == 150 ->
                String.format(AppLocalization.get("player_error_embed_disabled", language), errorCode)
            errorCode == 153 -> AppLocalization.get("player_error_153", language)
            errorCode == 2 || errorCode == 5 ->
                String.format(AppLocalization.get("player_error_generic", language), errorCode)
            else -> String.format(AppLocalization.get("player_error_generic", language), errorCode)
        }
    }
}

private class YouTubeJsBridge(
    private val onReady: () -> Unit,
    private val onError: (Int) -> Unit
) {
    @JavascriptInterface
    fun onPlayerReady() {
        onReady()
    }

    @JavascriptInterface
    fun onPlayerError(code: Int) {
        onError(code)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun InAppYouTubePlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    autoplay: Boolean = true,
    language: AppLanguage = AppLanguage.ENGLISH,
    onFallbackRequested: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val fullscreenController = LocalFullscreenVideoController.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainHandler = remember { Handler(Looper.getMainLooper()) }

    var hasError by remember(videoId) { mutableStateOf(false) }
    var errorCode by remember(videoId) { mutableIntStateOf(0) }
    var isNetworkErr by remember(videoId) { mutableStateOf(false) }
    var isLoading by remember(videoId) { mutableStateOf(true) }
    var isRendererCrashed by remember(videoId) { mutableStateOf(false) }
    var reloadCount by remember(videoId) { mutableIntStateOf(0) }
    var autoRecoveryAttempts by remember(videoId) { mutableIntStateOf(0) }
    var useNoCookieDomain by remember(videoId) { mutableStateOf(false) }
    var triedNoCookieFallback by remember(videoId) { mutableStateOf(false) }
    var pendingReloadOnResume by remember(videoId) { mutableStateOf(false) }
    var useSoftwareLayerFallback by remember(videoId) {
        mutableStateOf(YouTubeIFramePlayerConfig.shouldUseSoftwareLayer(localFallback = false))
    }
    var activeWebView by remember(videoId) { mutableStateOf<WebView?>(null) }
    var activeDisposedFlag by remember(videoId) { mutableStateOf<AtomicBoolean?>(null) }

    val cleanVideoId = remember(videoId) { YouTubeIFramePlayerConfig.sanitizeVideoId(videoId) }

    // Timeout safeguard so loading indicator never spins indefinitely on stalled connections
    LaunchedEffect(cleanVideoId, reloadCount, isLoading, hasError, isRendererCrashed) {
        if (isLoading && !hasError && !isRendererCrashed && cleanVideoId.isNotBlank()) {
            delay(YouTubeIFramePlayerConfig.LOAD_TIMEOUT_MS)
            if (isLoading && !hasError && !isRendererCrashed) {
                isLoading = false
                isNetworkErr = true
                errorCode = 408
                hasError = true
            }
        }
    }

    val latestWebView by rememberUpdatedState(activeWebView)
    val latestDisposedFlag by rememberUpdatedState(activeDisposedFlag)

    DisposableEffect(lifecycleOwner) {
        val pauseWebViewMediaJs =
            "(function(){try{document.querySelectorAll('video,audio').forEach(function(m){try{m.pause();}catch(e){}});}catch(e){}})();"
        val observer = LifecycleEventObserver { _, event ->
            val wv = latestWebView
            val disposed = latestDisposedFlag?.get() == true
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    AudioFocusCoordinator.onPlaybackStopped("video")
                    if (wv != null && !disposed) {
                        try {
                            if (wv.isAttachedToWindow) {
                                wv.evaluateJavascript(pauseWebViewMediaJs, null)
                            }
                            wv.onPause()
                        } catch (_: Exception) {
                        }
                    }
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (wv != null && !disposed) {
                        try {
                            wv.onResume()
                        } catch (_: Exception) {
                        }
                    }
                    if (pendingReloadOnResume) {
                        pendingReloadOnResume = false
                        hasError = false
                        isRendererCrashed = false
                        isLoading = true
                        reloadCount++
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mainHandler.removeCallbacksAndMessages(null)
            AudioFocusCoordinator.onPlaybackStopped("video")
        }
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Black)
        ) {
            if (!isRendererCrashed && !hasError && cleanVideoId.isNotBlank()) {
                key(cleanVideoId, reloadCount) {
                    val isDisposed = remember(cleanVideoId, reloadCount) { AtomicBoolean(false) }

                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                setBackgroundColor(android.graphics.Color.BLACK)

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    setRendererPriorityPolicy(
                                        WebView.RENDERER_PRIORITY_IMPORTANT,
                                        false
                                    )
                                }

                                val webViewInstance = this
                                val softwareMode = YouTubeIFramePlayerConfig.shouldUseSoftwareLayer(useSoftwareLayerFallback)
                                val effectiveAutoplay = YouTubeIFramePlayerConfig.effectiveAutoplay(
                                    requestedAutoplay = autoplay,
                                    softwareLayerActive = softwareMode
                                )
                                // Avoid forcing LAYER_TYPE_HARDWARE on WebView and proactively use
                                // LAYER_TYPE_SOFTWARE on virtualized environments lacking DRM rendernodes
                                // or after a renderer crash in this session.
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
                                    YouTubeJsBridge(
                                        onReady = {
                                            mainHandler.post {
                                                if (!isDisposed.get()) {
                                                    isLoading = false
                                                    AudioFocusCoordinator.onPlaybackStarting(
                                                        context = context,
                                                        ownerKey = "video"
                                                    ) {
                                                        mainHandler.post {
                                                            if (!isDisposed.get()) {
                                                                try {
                                                                    webViewInstance.evaluateJavascript(
                                                                        "(function(){try{document.querySelectorAll('video,audio').forEach(function(m){try{m.pause();}catch(e){}});}catch(e){}})();",
                                                                        null
                                                                    )
                                                                } catch (_: Exception) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        onError = { code ->
                                            mainHandler.post {
                                                if (!isDisposed.get()) {
                                                    AudioFocusCoordinator.onPlaybackStopped("video")
                                                    val isEmbedHandshakeError =
                                                        code == 150 || code == 152 || code == 153 || code == 101
                                                    if (isEmbedHandshakeError && !triedNoCookieFallback) {
                                                        triedNoCookieFallback = true
                                                        useNoCookieDomain = true
                                                        hasError = false
                                                        isNetworkErr = false
                                                        errorCode = 0
                                                        isLoading = true
                                                        reloadCount++
                                                    } else {
                                                        errorCode = code
                                                        isNetworkErr = code == -2
                                                        hasError = true
                                                        isLoading = false
                                                    }
                                                }
                                            }
                                        }
                                    ),
                                    YouTubeIFramePlayerConfig.JS_BRIDGE_NAME
                                )

                                webChromeClient = object : WebChromeClient() {
                                    override fun getDefaultVideoPoster(): Bitmap {
                                        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
                                    }

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
                                            try {
                                                ctx.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse("https://www.youtube.com/watch?v=$cleanVideoId")
                                                    )
                                                )
                                            } catch (_: Exception) {
                                            }
                                            return true
                                        }
                                        if (urlStr.contains("/watch") ||
                                            urlStr.contains("youtu.be/") ||
                                            urlStr.contains("support.google.com")
                                        ) {
                                            try {
                                                ctx.startActivity(Intent(Intent.ACTION_VIEW, targetUri))
                                            } catch (_: Exception) {
                                            }
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
                                                if (!isDisposed.get()) {
                                                    AudioFocusCoordinator.onPlaybackStopped("video")
                                                    errorCode = code
                                                    isNetworkErr = true
                                                    hasError = true
                                                    isLoading = false
                                                }
                                            }
                                        }
                                    }

                                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                        super.onPageStarted(view, url, favicon)
                                        if (!isDisposed.get()) {
                                            mainHandler.post {
                                                if (!isDisposed.get()) {
                                                    hasError = false
                                                    isLoading = true
                                                }
                                            }
                                        }
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        if (!isDisposed.get()) {
                                            mainHandler.post {
                                                if (!isDisposed.get()) {
                                                    isLoading = false
                                                    view?.evaluateJavascript(
                                                        YouTubeIFramePlayerConfig.ERROR_MONITOR_JS,
                                                        null
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    override fun onRenderProcessGone(
                                        view: WebView?,
                                        detail: RenderProcessGoneDetail?
                                    ): Boolean {
                                        AudioFocusCoordinator.onPlaybackStopped("video")
                                        if (activeWebView === view) {
                                            activeWebView = null
                                            activeDisposedFlag = null
                                        }
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
                                                useSoftwareLayerFallback = true
                                            }
                                            if (fullscreenController?.isFullscreen == true) {
                                                fullscreenController.exitFullscreen()
                                            }
                                            if (YouTubeIFramePlayerConfig.shouldAutoRecoverRenderer(
                                                    didCrash = didCrash,
                                                    priorAttempts = autoRecoveryAttempts
                                                )
                                            ) {
                                                autoRecoveryAttempts++
                                                val isResumed = lifecycleOwner.lifecycle.currentState
                                                    .isAtLeast(Lifecycle.State.RESUMED)
                                                if (isResumed) {
                                                    hasError = false
                                                    isRendererCrashed = false
                                                    isLoading = true
                                                    reloadCount++
                                                } else {
                                                    pendingReloadOnResume = true
                                                }
                                            } else {
                                                errorCode = 500
                                                isRendererCrashed = true
                                                hasError = true
                                                isLoading = false
                                            }
                                        }
                                        return true
                                    }
                                }

                                activeWebView = this
                                activeDisposedFlag = isDisposed
                                val directUrl = YouTubeIFramePlayerConfig.buildDirectEmbedUrl(
                                    videoId = cleanVideoId,
                                    autoplay = effectiveAutoplay,
                                    useNoCookie = useNoCookieDomain
                                )
                                val refererHeaders = YouTubeIFramePlayerConfig.buildRefererHeaders(ctx.packageName)
                                val loadInitiated = AtomicBoolean(false)
                                val startNavigation = {
                                    if (!isDisposed.get() && loadInitiated.compareAndSet(false, true)) {
                                        runCatching { loadUrl(directUrl, refererHeaders) }
                                    }
                                }
                                addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                                    override fun onViewAttachedToWindow(v: View) {
                                        v.removeOnAttachStateChangeListener(this)
                                        v.post { startNavigation() }
                                    }
                                    override fun onViewDetachedFromWindow(v: View) = Unit
                                })
                                post {
                                    if (isAttachedToWindow) {
                                        startNavigation()
                                    }
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
                            YouTubeIFramePlayerConfig.disposeWebViewSafely(
                                webView = wv,
                                disposedFlag = isDisposed,
                                rendererAlreadyGone = false
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (isLoading && !hasError && !isRendererCrashed && cleanVideoId.isNotBlank()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .testTag("youtube_player_loading")
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = AppLocalization.get("player_connecting", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            if (hasError || isRendererCrashed || cleanVideoId.isBlank()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF0F172A))
                        .padding(16.dp)
                        .testTag("youtube_player_error")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircleFilled,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = YouTubeIFramePlayerConfig.formatErrorMessage(
                                errorCode = errorCode,
                                isNetworkError = isNetworkErr,
                                isRendererCrash = isRendererCrashed,
                                language = language
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = {
                                    hasError = false
                                    isRendererCrashed = false
                                    isNetworkErr = false
                                    errorCode = 0
                                    autoRecoveryAttempts = 0
                                    triedNoCookieFallback = false
                                    useNoCookieDomain = false
                                    isLoading = true
                                    reloadCount++
                                    onFallbackRequested?.invoke()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.sizeIn(minHeight = 48.dp)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = AppLocalization.get("retry", language),
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(AppLocalization.get("retry", language), color = Color.White)
                            }

                            if (cleanVideoId.isNotBlank()) {
                                OutlinedButton(
                                    onClick = {
                                        try {
                                            context.startActivity(
                                                Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://www.youtube.com/watch?v=$cleanVideoId")
                                                )
                                            )
                                        } catch (_: Exception) {
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.sizeIn(minHeight = 48.dp)
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.OpenInNew,
                                        contentDescription = AppLocalization.get("lectures_watch_on_youtube", language),
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = AppLocalization.get("lectures_watch_on_youtube", language),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
