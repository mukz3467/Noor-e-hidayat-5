package com.example.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Coordinates playback mutual exclusion, Android AudioManager focus, and
 * ACTION_AUDIO_BECOMING_NOISY across Quran recitation, Dua Qari audio,
 * Podcasts, In-App Video, and Azan playback.
 */
object AudioFocusCoordinator {

    private val lock = Any()
    private val activeOwners = LinkedHashMap<String, () -> Unit>()

    private var appContextRef: Context? = null
    private var audioManager: AudioManager? = null
    private var focusRequest: AudioFocusRequest? = null
    private var noisyReceiver: BroadcastReceiver? = null
    private var hasSystemAudioFocus = false
    private var currentIsAlarmUsage = false

    private val focusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                pauseAll(excludeOwner = null)
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    abandonSystemFocusLockedSafe()
                }
            }
        }
    }

    fun onPlaybackStarting(
        context: Context,
        ownerKey: String,
        isAlarmUsage: Boolean = false,
        pauseSelf: () -> Unit
    ) {
        val appCtx = context.applicationContext ?: context
        val otherCallbacks: List<() -> Unit>
        synchronized(lock) {
            appContextRef = appCtx
            currentIsAlarmUsage = isAlarmUsage
            otherCallbacks = activeOwners
                .filterKeys { it != ownerKey }
                .values
                .toList()
            otherCallbacks.forEach { _ -> }
            activeOwners.keys.filter { it != ownerKey }.toList().forEach { activeOwners.remove(it) }
            activeOwners[ownerKey] = pauseSelf
            requestSystemFocusLocked(appCtx, isAlarmUsage)
            registerNoisyReceiverLocked(appCtx)
        }
        otherCallbacks.forEach { cb ->
            runCatching { cb() }
        }
    }

    fun onPlaybackStopped(ownerKey: String) {
        val remainingEmpty: Boolean
        synchronized(lock) {
            activeOwners.remove(ownerKey)
            remainingEmpty = activeOwners.isEmpty()
            if (remainingEmpty) {
                unregisterNoisyReceiverLocked()
                abandonSystemFocusLocked()
                currentIsAlarmUsage = false
            }
        }
    }

    fun activeOwnerKeysForTest(): Set<String> = synchronized(lock) {
        activeOwners.keys.toSet()
    }

    fun resetForTest() {
        synchronized(lock) {
            activeOwners.clear()
            unregisterNoisyReceiverLocked()
            abandonSystemFocusLocked()
            currentIsAlarmUsage = false
        }
    }

    private fun pauseAll(excludeOwner: String?) {
        val callbacks: List<() -> Unit>
        synchronized(lock) {
            val targets = if (excludeOwner == null) {
                activeOwners.entries.filter { !(currentIsAlarmUsage && it.key == "azan") }
            } else {
                activeOwners.entries.filter { it.key != excludeOwner }
            }
            callbacks = targets.map { it.value }
            targets.map { it.key }.forEach { activeOwners.remove(it) }
            if (activeOwners.isEmpty()) {
                unregisterNoisyReceiverLocked()
            }
        }
        callbacks.forEach { cb -> runCatching { cb() } }
    }

    private fun requestSystemFocusLocked(appCtx: Context, isAlarmUsage: Boolean) {
        runCatching {
            val mgr = audioManager ?: (appCtx.getSystemService(Context.AUDIO_SERVICE) as? AudioManager).also {
                audioManager = it
            } ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                focusRequest?.let { prev -> runCatching { mgr.abandonAudioFocusRequest(prev) } }
                val attrs = AudioAttributes.Builder()
                    .setContentType(
                        if (isAlarmUsage) AudioAttributes.CONTENT_TYPE_SONIFICATION
                        else AudioAttributes.CONTENT_TYPE_MUSIC
                    )
                    .setUsage(
                        if (isAlarmUsage) AudioAttributes.USAGE_ALARM
                        else AudioAttributes.USAGE_MEDIA
                    )
                    .build()
                val req = AudioFocusRequest.Builder(
                    if (isAlarmUsage) AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                    else AudioManager.AUDIOFOCUS_GAIN
                )
                    .setAudioAttributes(attrs)
                    .setAcceptsDelayedFocusGain(false)
                    .setOnAudioFocusChangeListener(focusChangeListener)
                    .build()
                focusRequest = req
                val res = mgr.requestAudioFocus(req)
                hasSystemAudioFocus = (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            } else {
                @Suppress("DEPRECATION")
                val res = mgr.requestAudioFocus(
                    focusChangeListener,
                    if (isAlarmUsage) AudioManager.STREAM_ALARM else AudioManager.STREAM_MUSIC,
                    if (isAlarmUsage) AudioManager.AUDIOFOCUS_GAIN_TRANSIENT else AudioManager.AUDIOFOCUS_GAIN
                )
                hasSystemAudioFocus = (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            }
        }
    }

    private fun abandonSystemFocusLockedSafe() {
        synchronized(lock) {
            abandonSystemFocusLocked()
        }
    }

    private fun abandonSystemFocusLocked() {
        if (!hasSystemAudioFocus && focusRequest == null) return
        runCatching {
            val mgr = audioManager ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                focusRequest?.let { req -> mgr.abandonAudioFocusRequest(req) }
                focusRequest = null
            } else {
                @Suppress("DEPRECATION")
                mgr.abandonAudioFocus(focusChangeListener)
            }
        }
        hasSystemAudioFocus = false
    }

    private fun registerNoisyReceiverLocked(appCtx: Context) {
        if (noisyReceiver != null) return
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                    val callbacksToPause: List<() -> Unit>
                    synchronized(lock) {
                        val targets = activeOwners.entries.filter { it.key != "azan" }
                        callbacksToPause = targets.map { it.value }
                        targets.map { it.key }.forEach { activeOwners.remove(it) }
                        if (activeOwners.isEmpty()) {
                            unregisterNoisyReceiverLocked()
                            abandonSystemFocusLocked()
                        }
                    }
                    callbacksToPause.forEach { cb -> runCatching { cb() } }
                }
            }
        }
        runCatching {
            ContextCompat.registerReceiver(
                appCtx,
                receiver,
                IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            noisyReceiver = receiver
        }
    }

    private fun unregisterNoisyReceiverLocked() {
        val receiver = noisyReceiver ?: return
        val ctx = appContextRef ?: return
        runCatching { ctx.unregisterReceiver(receiver) }
        noisyReceiver = null
    }
}
