package com.example.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Manages genuine Android WebChromeClient custom view fullscreen transitions,
 * orientation changes, WindowInsetsControllerCompat system-bar hiding, and Back restoration.
 */
@Stable
class FullscreenVideoController {
    var customView: View? by mutableStateOf(null)
        private set

    var isFullscreen: Boolean by mutableStateOf(false)
        private set

    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    private var previousOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    private var hostContainer: FrameLayout? = null
    private var boundActivity: Activity? = null
    private var isHiding: Boolean = false

    fun attachHost(activity: Activity, container: FrameLayout) {
        boundActivity = activity
        hostContainer = container
        syncCustomViewToHost()
    }

    fun detachHost() {
        exitFullscreen()
        hostContainer = null
        boundActivity = null
    }

    fun enterFullscreen(
        view: View,
        callback: WebChromeClient.CustomViewCallback?,
        activity: Activity? = boundActivity
    ) {
        val targetActivity = activity ?: boundActivity

        // Prevent duplicate custom views if WebView triggers onShowCustomView while already active
        if (customView != null && customView !== view) {
            releaseCurrentViewOnly(notifyCallback = true)
        }

        if (targetActivity != null) {
            if (boundActivity == null) {
                boundActivity = targetActivity
            }
            if (!isFullscreen) {
                previousOrientation = targetActivity.requestedOrientation
                targetActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                hideSystemBars(targetActivity)
            }
        }

        customView = view
        customViewCallback = callback
        isFullscreen = true
        syncCustomViewToHost()
    }

    fun exitFullscreen(): Boolean {
        if (!isFullscreen && customView == null) return false
        if (isHiding) return true
        isHiding = true
        try {
            releaseCurrentViewOnly(notifyCallback = true)
            boundActivity?.let { activity ->
                activity.requestedOrientation = previousOrientation
                showSystemBars(activity)
            }
            isFullscreen = false
        } finally {
            isHiding = false
        }
        return true
    }

    fun onWebChromeHideCustomView() {
        if (isHiding) return
        isHiding = true
        try {
            releaseCurrentViewOnly(notifyCallback = false)
            boundActivity?.let { activity ->
                activity.requestedOrientation = previousOrientation
                showSystemBars(activity)
            }
            isFullscreen = false
        } finally {
            isHiding = false
        }
    }

    private fun releaseCurrentViewOnly(notifyCallback: Boolean) {
        val current = customView
        customView = null
        val cb = customViewCallback
        customViewCallback = null

        try {
            (current?.parent as? ViewGroup)?.removeView(current)
            hostContainer?.removeAllViews()
            hostContainer?.isClickable = false
            hostContainer?.isFocusable = false
            hostContainer?.visibility = View.GONE
        } catch (_: Exception) {
        }

        if (notifyCallback) {
            try {
                cb?.onCustomViewHidden()
            } catch (_: Exception) {
            }
        }
    }

    private fun syncCustomViewToHost() {
        val container = hostContainer ?: return
        val view = customView
        container.removeAllViews()
        if (view != null) {
            (view.parent as? ViewGroup)?.removeView(view)
            container.addView(
                view,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            container.isClickable = true
            container.isFocusable = true
            container.visibility = View.VISIBLE
            container.bringToFront()
        } else {
            container.isClickable = false
            container.isFocusable = false
            container.visibility = View.GONE
        }
    }

    companion object {
        fun hideSystemBars(activity: Activity) {
            try {
                val window = activity.window ?: return
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                controller.hide(WindowInsetsCompat.Type.systemBars())
            } catch (_: Exception) {
            }
        }

        fun showSystemBars(activity: Activity) {
            try {
                val window = activity.window ?: return
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.show(WindowInsetsCompat.Type.systemBars())
            } catch (_: Exception) {
            }
        }

        tailrec fun Context.findActivity(): Activity? = when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.findActivity()
            else -> null
        }
    }
}

val LocalFullscreenVideoController = staticCompositionLocalOf<FullscreenVideoController?> { null }
