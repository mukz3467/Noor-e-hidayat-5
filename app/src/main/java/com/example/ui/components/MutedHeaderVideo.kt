package com.example.ui.components

import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.net.Uri
import android.os.Build
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer

object MutedHeaderVideoConfig {
    const val HOME_HEADER_VIDEO_ASSET_PATH = "video/home_header_video.mp4"

    fun buildAssetUri(assetPath: String = HOME_HEADER_VIDEO_ASSET_PATH): Uri {
        val normalized = assetPath.trimStart('/')
        return Uri.parse("asset:///$normalized")
    }

    /**
     * Computes TextureView (scaleX, scaleY) multipliers around the view center
     * to achieve aspect-preserving center-crop (ContentScale.Crop equivalent).
     */
    fun computeCenterCropScales(
        viewWidth: Int,
        viewHeight: Int,
        videoWidth: Int,
        videoHeight: Int
    ): Pair<Float, Float> {
        if (viewWidth <= 0 || viewHeight <= 0 || videoWidth <= 0 || videoHeight <= 0) {
            return 1f to 1f
        }
        val viewRatio = viewWidth.toFloat() / viewHeight.toFloat()
        val videoRatio = videoWidth.toFloat() / videoHeight.toFloat()
        return if (videoRatio > viewRatio) {
            (videoRatio / viewRatio) to 1f
        } else {
            1f to (viewRatio / videoRatio)
        }
    }

    fun applyCenterCropTransform(
        textureView: TextureView,
        videoWidth: Int,
        videoHeight: Int
    ) {
        val viewW = textureView.width
        val viewH = textureView.height
        val (scaleX, scaleY) = computeCenterCropScales(viewW, viewH, videoWidth, videoHeight)
        val matrix = Matrix().apply {
            setScale(scaleX, scaleY, viewW / 2f, viewH / 2f)
        }
        textureView.setTransform(matrix)
    }

    fun isRobolectricEnvironment(): Boolean {
        return Build.FINGERPRINT.equals("robolectric", ignoreCase = true)
    }
}

@OptIn(UnstableApi::class)
@Composable
fun MutedLoopingHeaderVideo(
    contentDescriptionText: String,
    modifier: Modifier = Modifier,
    assetPath: String = MutedHeaderVideoConfig.HOME_HEADER_VIDEO_ASSET_PATH
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val isInspection = LocalInspectionMode.current
    val skipNativePlayer = isInspection || MutedHeaderVideoConfig.isRobolectricEnvironment()

    var videoWidth by remember(assetPath) { mutableIntStateOf(0) }
    var videoHeight by remember(assetPath) { mutableIntStateOf(0) }
    var hasRenderedFirstFrame by remember(assetPath) { mutableStateOf(false) }
    var hasError by remember(assetPath) { mutableStateOf(false) }

    val textureViewRef = remember(assetPath) { mutableStateOf<TextureView?>(null) }

    val exoPlayer = remember(context, assetPath, skipNativePlayer) {
        if (skipNativePlayer) {
            null
        } else {
            try {
                ExoPlayer.Builder(context)
                    .setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus = */ false)
                    .build()
                    .apply {
                        // Permanently mute audio and disable audio track decoding so it never plays sound
                        // or interrupts Quran / Azan / Podcast audio.
                        volume = 0f
                        trackSelectionParameters = trackSelectionParameters
                            .buildUpon()
                            .setTrackTypeDisabled(C.TRACK_TYPE_AUDIO, true)
                            .build()
                        repeatMode = Player.REPEAT_MODE_ALL
                        playWhenReady = true
                        setMediaItem(MediaItem.fromUri(MutedHeaderVideoConfig.buildAssetUri(assetPath)))
                        prepare()
                    }
            } catch (_: Throwable) {
                hasError = true
                null
            }
        }
    }

    DisposableEffect(exoPlayer, lifecycleOwner) {
        val player = exoPlayer ?: return@DisposableEffect onDispose {}

        val listener = object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                if (videoSize.width > 0 && videoSize.height > 0) {
                    videoWidth = videoSize.width
                    videoHeight = videoSize.height
                    textureViewRef.value?.let { tv ->
                        MutedHeaderVideoConfig.applyCenterCropTransform(
                            textureView = tv,
                            videoWidth = videoSize.width,
                            videoHeight = videoSize.height
                        )
                    }
                }
            }

            override fun onRenderedFirstFrame() {
                hasRenderedFirstFrame = true
            }

            override fun onPlayerError(error: PlaybackException) {
                hasError = true
            }
        }
        player.addListener(listener)

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_RESUME -> {
                    player.volume = 0f
                    player.play()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.removeListener(listener)
            textureViewRef.value?.let { tv ->
                try {
                    player.clearVideoTextureView(tv)
                } catch (_: Exception) {
                }
            }
            try {
                player.release()
            } catch (_: Exception) {
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1320))
            .semantics { contentDescription = contentDescriptionText }
            .testTag("home_header_video")
    ) {
        if (exoPlayer != null && !hasError) {
            AndroidView(
                factory = { ctx ->
                    TextureView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        isOpaque = true
                        textureViewRef.value = this
                        exoPlayer.setVideoTextureView(this)

                        addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
                            val tv = v as? TextureView ?: return@addOnLayoutChangeListener
                            if (videoWidth > 0 && videoHeight > 0) {
                                MutedHeaderVideoConfig.applyCenterCropTransform(tv, videoWidth, videoHeight)
                            }
                        }

                        surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                            override fun onSurfaceTextureAvailable(
                                surface: SurfaceTexture,
                                width: Int,
                                height: Int
                            ) {
                                if (videoWidth > 0 && videoHeight > 0) {
                                    MutedHeaderVideoConfig.applyCenterCropTransform(
                                        this@apply,
                                        videoWidth,
                                        videoHeight
                                    )
                                }
                            }

                            override fun onSurfaceTextureSizeChanged(
                                surface: SurfaceTexture,
                                width: Int,
                                height: Int
                            ) {
                                if (videoWidth > 0 && videoHeight > 0) {
                                    MutedHeaderVideoConfig.applyCenterCropTransform(
                                        this@apply,
                                        videoWidth,
                                        videoHeight
                                    )
                                }
                            }

                            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                                return true
                            }

                            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit
                        }
                    }
                },
                onRelease = { tv ->
                    if (textureViewRef.value === tv) {
                        textureViewRef.value = null
                    }
                    try {
                        exoPlayer.clearVideoTextureView(tv)
                    } catch (_: Exception) {
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
