package com.example.data.notification

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.ramadan.RamadanAlarmScheduler
import com.example.data.repository.QuranAudioManager
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization

class AzanPlaybackService : Service() {

    companion object {
        const val ACTION_START_AZAN = "com.example.ACTION_START_AZAN"
        const val ACTION_STOP_SERVICE = "com.example.ACTION_STOP_SERVICE"

        private val serviceLock = Any()
        @Volatile
        private var activeEventId: String? = null
        @Volatile
        private var activeStartedAtMs: Long = 0L

        internal fun resetActiveEventForTest() {
            synchronized(serviceLock) {
                activeEventId = null
                activeStartedAtMs = 0L
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_SERVICE || intent?.action == PrayerAlarmReceiver.ACTION_STOP_AZAN) {
            stopAzanAndService()
            return START_NOT_STICKY
        }

        PrayerNotificationManager.init(applicationContext)
        val prayerKey = intent?.getStringExtra(PrayerAlarmReceiver.EXTRA_PRAYER_KEY) ?: ""
        val rawTitle = intent?.getStringExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TITLE) ?: ""
        val prayerTime = intent?.getStringExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TIME) ?: ""
        val adhanSound = intent?.getStringExtra(PrayerAlarmReceiver.EXTRA_ADHAN_SOUND) ?: "makkah"
        val eventId = intent?.getStringExtra(PrayerAlarmReceiver.EXTRA_EVENT_ID)
            ?: "${prayerKey.lowercase()}:$prayerTime:${adhanSound.lowercase()}"

        val lang = UserPreferencesRepository(applicationContext).language.value
        val stopLabel = AppLocalization.get("azan_stop_notif_action", lang)
        val localizedSubject = when {
            prayerKey.lowercase() in listOf("fajr", "dhuhr", "asr", "maghrib", "isha") ->
                AppLocalization.get(prayerKey.lowercase(), lang)
            prayerKey.isNotBlank() ->
                RamadanAlarmScheduler.localizedTitleForKey(prayerKey, lang)
                    ?: rawTitle.ifBlank { AppLocalization.get("prayer_times", lang) }
            else -> rawTitle.ifBlank { AppLocalization.get("prayer_times", lang) }
        }
        val soundLabel = when (adhanSound.lowercase()) {
            "makkah" -> AppLocalization.get("azan_makkah", lang)
            "madina", "madinah" -> AppLocalization.get("azan_madinah", lang)
            "fajr" -> AppLocalization.get("azan_fajr", lang)
            else -> adhanSound
        }
        val titlePrefix = AppLocalization.get("notif_azan_title_suffix", lang)
        val timeSuffix = if (prayerTime.isNotBlank()) " ($prayerTime)" else ""
        val notifTitle = "🕌 $localizedSubject — $titlePrefix$timeSuffix"
        val notifBody = when (lang) {
            AppLanguage.URDU -> "$soundLabel کی اذان جاری ہے"
            AppLanguage.ARABIC -> "جاري تشغيل أذان $soundLabel"
            AppLanguage.HINDI -> "$soundLabel की अज़ान चल रही है"
            AppLanguage.ENGLISH -> "Playing $soundLabel"
        }

        val openIntent = Intent(applicationContext, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openPi = PendingIntent.getActivity(
            applicationContext,
            10,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(applicationContext, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_STOP_AZAN
        }
        val stopPi = PendingIntent.getBroadcast(
            applicationContext,
            990003,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = NotificationCompat.Builder(applicationContext, PrayerNotificationManager.CHANNEL_ID_NOVIB)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notifTitle)
            .setContentText(notifBody)
            .setOngoing(true)
            .setSilent(true)
            .setSound(null)
            .setDefaults(0)
            .setVibrate(longArrayOf(0L))
            .setOnlyAlertOnce(true)
            .setContentIntent(openPi)
            .addAction(android.R.drawable.ic_media_pause, stopLabel, stopPi)
            .build()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    PrayerNotificationManager.NOTIF_ID_SERVICE,
                    notif,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(PrayerNotificationManager.NOTIF_ID_SERVICE, notif)
            }
        } catch (_: Exception) {
            stopSelf()
            return START_NOT_STICKY
        }

        if (adhanSound.equals("silent", ignoreCase = true) || adhanSound.equals("system", ignoreCase = true)) {
            stopAzanAndService()
            return START_NOT_STICKY
        }

        // Prevent duplicate simultaneous Azan playback for the same prayer event
        val shouldStartPlayback = synchronized(serviceLock) {
            val nowMs = System.currentTimeMillis()
            val isDuplicate = activeEventId == eventId && (nowMs - activeStartedAtMs) < 60_000L
            if (!isDuplicate) {
                activeEventId = eventId
                activeStartedAtMs = nowMs
                true
            } else {
                false
            }
        }

        if (shouldStartPlayback) {
            QuranAudioManager.playAzan(
                context = applicationContext,
                azanType = adhanSound,
                useAlarmUsage = true,
                onCompletion = {
                    stopAzanAndService(resetActiveEvent = false)
                }
            )
        }

        return START_NOT_STICKY
    }

    private fun stopAzanAndService(resetActiveEvent: Boolean = true) {
        if (resetActiveEvent) {
            resetActiveEventForTest()
        }
        QuranAudioManager.stopMedia()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            runCatching { stopForeground(STOP_FOREGROUND_REMOVE) }
        } else {
            @Suppress("DEPRECATION")
            runCatching { stopForeground(true) }
        }
        stopSelf()
    }

    override fun onDestroy() {
        resetActiveEventForTest()
        QuranAudioManager.stopMedia()
        super.onDestroy()
    }
}
