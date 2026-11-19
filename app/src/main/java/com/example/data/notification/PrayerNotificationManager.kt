package com.example.data.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.QuranAudioManager
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

data class PrayerNotificationSettings(
    val fajrEnabled: Boolean = true,
    val dhuhrEnabled: Boolean = true,
    val asrEnabled: Boolean = true,
    val maghribEnabled: Boolean = true,
    val ishaEnabled: Boolean = true,
    val adhanSound: String = "makkah",
    val vibrationEnabled: Boolean = true,
    val leadTimeMinutes: Int = 0
)

data class PrayerAlarmEntry(
    val id: String,
    val prayerName: String,
    val prayerTimeMillis: Long
)

data class PrayerScheduleStatus(
    val scheduledCount: Int = 0,
    val canScheduleExactAlarms: Boolean = true,
    val usedExactAlarms: Boolean = true,
    val lastRescheduleReason: String = ""
)

object PrayerNotificationManager {
    const val CHANNEL_ID = "noor_prayer_silent_vib_v3"
    const val CHANNEL_ID_NOVIB = "noor_prayer_silent_novib_v3"
    const val LEGACY_CHANNEL_ID = "noor_prayer_channel"
    private val DEPRECATED_CHANNEL_IDS = listOf(
        LEGACY_CHANNEL_ID,
        "noor_prayer_v2_vib",
        "noor_prayer_v2_novib"
    )
    const val NOTIF_ID_SERVICE = 202
    private const val REPLENISH_REQUEST_CODE = 770999
    private const val ROLLING_DAYS = 14

    private const val PREFS_NAME = "prayer_notif_prefs"
    private const val KEY_SCHEDULED_CODES = "scheduled_request_codes_csv"

    private val PRAYER_KEYS = listOf("fajr", "dhuhr", "asr", "maghrib", "isha")
    private val eventDedupLock = Any()
    private val recentTriggeredEvents = HashMap<String, Long>()

    private val _settings = MutableStateFlow(PrayerNotificationSettings())
    val settings: StateFlow<PrayerNotificationSettings> = _settings.asStateFlow()

    private val _scheduleStatus = MutableStateFlow(PrayerScheduleStatus())
    val scheduleStatus: StateFlow<PrayerScheduleStatus> = _scheduleStatus.asStateFlow()

    @Volatile
    private var isInitialized = false

    fun init(context: Context) {
        val appCtx = context.applicationContext
        if (!isInitialized) {
            isInitialized = true
            loadSettings(appCtx)
        }
        createNotificationChannels(appCtx)
    }

    fun resetForTest() {
        isInitialized = false
        synchronized(eventDedupLock) {
            recentTriggeredEvents.clear()
        }
        AzanPlaybackService.resetActiveEventForTest()
        _settings.value = PrayerNotificationSettings()
        _scheduleStatus.value = PrayerScheduleStatus()
    }

    /**
     * Prevents duplicate triggers (same scheduled prayer alarm delivered twice or rapid double taps)
     * without blocking subsequent prayers or later user actions.
     */
    internal fun tryAcquirePrayerEventTrigger(
        eventId: String,
        cooldownMs: Long,
        nowMillis: Long = System.currentTimeMillis()
    ): Boolean {
        synchronized(eventDedupLock) {
            val prev = recentTriggeredEvents[eventId]
            if (prev != null && (nowMillis - prev) < cooldownMs) {
                return false
            }
            recentTriggeredEvents[eventId] = nowMillis
            // Bound map size
            if (recentTriggeredEvents.size > 64) {
                val cutoff = nowMillis - 600_000L
                val it = recentTriggeredEvents.entries.iterator()
                while (it.hasNext()) {
                    if (it.next().value < cutoff) it.remove()
                }
            }
            return true
        }
    }

    private fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
            DEPRECATED_CHANNEL_IDS.forEach { oldId ->
                runCatching { nm.deleteNotificationChannel(oldId) }
            }

            val vibChannel = NotificationChannel(
                CHANNEL_ID,
                "Prayer Alerts (Silent Audio / Vibration)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Silent prayer notifications with vibration; Azan audio is played exclusively by AzanPlaybackService"
                setSound(null, null)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
            }

            val noVibChannel = NotificationChannel(
                CHANNEL_ID_NOVIB,
                "Prayer Alerts (Visual / Silent)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Silent prayer notifications without ringtone or vibration; Azan audio is played exclusively by AzanPlaybackService"
                setSound(null, null)
                enableVibration(false)
            }

            nm.createNotificationChannel(vibChannel)
            nm.createNotificationChannel(noVibChannel)
        }
    }

    fun hasNotificationPermission(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

    fun canScheduleExactAlarms(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            (context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.canScheduleExactAlarms() == true

    fun canUseExactAlarms(context: Context): Boolean = canScheduleExactAlarms(context)

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return true
        return pm.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun openExactAlarmSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                context.startActivity(
                    Intent(
                        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                        Uri.parse("package:" + context.packageName)
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                return
            } catch (_: Exception) { }
        }
        try {
            context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + context.packageName)
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (_: Exception) { }
    }

    fun requestExactAlarmPermission(context: Context) = openExactAlarmSettings(context)

    fun openBatteryOptimizationSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                context.startActivity(
                    Intent(
                        Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                return
            } catch (_: Exception) { }
        }
        openExactAlarmSettings(context)
    }

    private fun loadSettings(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _settings.value = PrayerNotificationSettings(
            fajrEnabled = prefs.getBoolean("fajr_enabled", true),
            dhuhrEnabled = prefs.getBoolean("dhuhr_enabled", true),
            asrEnabled = prefs.getBoolean("asr_enabled", true),
            maghribEnabled = prefs.getBoolean("maghrib_enabled", true),
            ishaEnabled = prefs.getBoolean("isha_enabled", true),
            adhanSound = prefs.getString("adhan_sound", "makkah") ?: "makkah",
            vibrationEnabled = prefs.getBoolean("vibration_enabled", true),
            leadTimeMinutes = prefs.getInt("lead_time_minutes", 0).coerceIn(0, 60)
        )
    }

    fun updateSettings(context: Context, newSettings: PrayerNotificationSettings) {
        val appCtx = context.applicationContext
        init(appCtx)
        val sanitized = newSettings.copy(leadTimeMinutes = newSettings.leadTimeMinutes.coerceIn(0, 60))
        _settings.value = sanitized
        val prefs = appCtx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean("fajr_enabled", sanitized.fajrEnabled)
            putBoolean("dhuhr_enabled", sanitized.dhuhrEnabled)
            putBoolean("asr_enabled", sanitized.asrEnabled)
            putBoolean("maghrib_enabled", sanitized.maghribEnabled)
            putBoolean("isha_enabled", sanitized.ishaEnabled)
            putString("adhan_sound", sanitized.adhanSound)
            putBoolean("vibration_enabled", sanitized.vibrationEnabled)
            putInt("lead_time_minutes", sanitized.leadTimeMinutes)
            apply()
        }
        scheduleNextRollingWindow(appCtx, reason = "settings_updated")
    }

    fun scheduleNextRollingWindow(
        context: Context,
        reason: String = "rolling_window"
    ): PrayerScheduleStatus = rescheduleAllAlarms(context, reason)

    fun rescheduleAllAlarms(
        context: Context,
        reason: String = "manual_reschedule"
    ): PrayerScheduleStatus {
        val appCtx = context.applicationContext
        init(appCtx)
        val masterEnabled = runCatching {
            UserPreferencesRepository(appCtx).appSettings.value.notificationsEnabled
        }.getOrDefault(true)

        cancelAllAlarms(appCtx)

        val canExact = canScheduleExactAlarms(appCtx)
        val curSettings = _settings.value
        val anyPrayerEnabled = masterEnabled && (
            curSettings.fajrEnabled || curSettings.dhuhrEnabled ||
                curSettings.asrEnabled || curSettings.maghribEnabled || curSettings.ishaEnabled
            )

        if (!anyPrayerEnabled) {
            val emptyStatus = PrayerScheduleStatus(
                scheduledCount = 0,
                canScheduleExactAlarms = canExact,
                usedExactAlarms = canExact,
                lastRescheduleReason = reason
            )
            _scheduleStatus.value = emptyStatus
            return emptyStatus
        }

        AppLocationManager.init(appCtx)
        PrayerCalculator.init(appCtx)

        val location = AppLocationManager.currentLocation.value
        val calcConfig = PrayerCalculator.calcConfig.value
        val zone = TimeZone.getTimeZone(location.timeZoneId)
        val parser = SimpleDateFormat("hh:mm a", Locale.US).apply { timeZone = zone }
        val am = appCtx.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val now = System.currentTimeMillis()
        val scheduledCodes = mutableListOf<Int>()
        var usedExactAny = canExact

        val baseCal = Calendar.getInstance(zone)
        for (dayOffset in 0..ROLLING_DAYS) {
            val day = (baseCal.clone() as Calendar).apply {
                timeInMillis = now
                add(Calendar.DAY_OF_YEAR, dayOffset)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val daily = PrayerCalculator.calculatePrayerTimes(
                latitude = location.latitude,
                longitude = location.longitude,
                calendar = day,
                method = calcConfig.method,
                asrJuristic = calcConfig.asrJuristic,
                adjustments = calcConfig.adjustments,
                cityName = "${location.cityName}, ${location.countryName}",
                timeZoneId = location.timeZoneId,
                highLatitudeRule = calcConfig.highLatitudeRule,
                customParams = calcConfig.customParams
            )

            val times = listOf(
                "fajr" to daily.fajr,
                "dhuhr" to daily.dhuhr,
                "asr" to daily.asr,
                "maghrib" to daily.maghrib,
                "isha" to daily.isha
            )
            times.forEachIndexed { prayerIndex, (prayerKey, formattedTime) ->
                if (!isPrayerEnabled(prayerKey, curSettings)) return@forEachIndexed
                val triggerAt = parseAlarmMillis(day, zone, parser, formattedTime, curSettings.leadTimeMinutes)
                    ?: return@forEachIndexed
                // Skip triggers in the past or within a 15-second clock-drift buffer so an alarm that
                // just fired slightly early cannot be re-scheduled and fired twice.
                if (triggerAt <= now + 15_000L) return@forEachIndexed

                val requestCode = (dayOffset + 1) * 10 + prayerIndex
                val title = prayerKey.replaceFirstChar { it.uppercaseChar() }
                val dateKey = String.format(
                    Locale.US,
                    "%04d-%02d-%02d",
                    day.get(Calendar.YEAR),
                    day.get(Calendar.MONTH) + 1,
                    day.get(Calendar.DAY_OF_MONTH)
                )
                val intent = Intent(appCtx, PrayerAlarmReceiver::class.java).apply {
                    action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM
                    putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_KEY, prayerKey)
                    putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TITLE, title)
                    putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TIME, formattedTime)
                    putExtra(PrayerAlarmReceiver.EXTRA_ADHAN_SOUND, curSettings.adhanSound)
                    putExtra(PrayerAlarmReceiver.EXTRA_EVENT_ID, "alarm:${prayerKey.lowercase()}:$dateKey:$formattedTime")
                }
                val pi = PendingIntent.getBroadcast(
                    appCtx,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                if (am != null) {
                    val exactUsed = scheduleTrigger(am, triggerAt, pi, canExact)
                    if (!exactUsed) usedExactAny = false
                }
                scheduledCodes.add(requestCode)
            }
        }

        // Schedule daily replenish alarm 24h from now
        if (am != null && scheduledCodes.isNotEmpty()) {
            val replenishAt = now + 24L * 3600L * 1000L
            val replenishIntent = Intent(appCtx, PrayerAlarmReceiver::class.java).apply {
                action = PrayerAlarmReceiver.ACTION_REPLENISH_ALARMS
            }
            val replenishPi = PendingIntent.getBroadcast(
                appCtx,
                REPLENISH_REQUEST_CODE,
                replenishIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, replenishAt, replenishPi)
        }

        saveScheduledCodes(appCtx, scheduledCodes)
        val status = PrayerScheduleStatus(
            scheduledCount = scheduledCodes.size,
            canScheduleExactAlarms = canExact,
            usedExactAlarms = canExact && usedExactAny,
            lastRescheduleReason = reason
        )
        _scheduleStatus.value = status
        return status
    }

    private fun parseAlarmMillis(
        day: Calendar,
        zone: TimeZone,
        parser: SimpleDateFormat,
        formattedTime: String,
        leadMinutes: Int
    ): Long? {
        return try {
            val parsed = parser.parse(formattedTime) ?: return null
            val timeOnly = Calendar.getInstance(zone).apply { time = parsed }
            val alarmCal = (day.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, timeOnly.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, timeOnly.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.MINUTE, -leadMinutes)
            }
            alarmCal.timeInMillis
        } catch (_: Exception) {
            null
        }
    }

    private fun scheduleTrigger(
        am: AlarmManager,
        triggerAt: Long,
        pi: PendingIntent,
        allowExact: Boolean
    ): Boolean {
        if (allowExact) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
                } else {
                    @Suppress("DEPRECATION")
                    am.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pi)
                }
                return true
            } catch (_: SecurityException) {
                // Fall back to inexact alarm cleanly
            }
        }
        AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, triggerAt, pi)
        return false
    }

    fun scheduleAlarms(context: Context, @Suppress("UNUSED_PARAMETER") entries: List<PrayerAlarmEntry>) {
        scheduleNextRollingWindow(context, reason = "screen_sync")
    }

    fun cancelAllAlarms(context: Context) {
        val appCtx = context.applicationContext
        val am = appCtx.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val existing = getScheduledRequestCodes(appCtx)
        for (code in existing) {
            val intent = Intent(appCtx, PrayerAlarmReceiver::class.java).apply {
                action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM
            }
            val pi = PendingIntent.getBroadcast(
                appCtx,
                code,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pi != null) {
                am?.cancel(pi)
                runCatching { pi.cancel() }
            }
        }
        val replenishIntent = Intent(appCtx, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_REPLENISH_ALARMS
        }
        val replenishPi = PendingIntent.getBroadcast(
            appCtx,
            REPLENISH_REQUEST_CODE,
            replenishIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (replenishPi != null) {
            am?.cancel(replenishPi)
            runCatching { replenishPi.cancel() }
        }
        saveScheduledCodes(appCtx, emptyList())
    }

    fun getScheduledRequestCodes(context: Context): List<Int> {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_SCHEDULED_CODES, "") ?: ""
        if (raw.isBlank()) return emptyList()
        return raw.split(",").mapNotNull { it.trim().toIntOrNull() }
    }

    private fun saveScheduledCodes(context: Context, codes: List<Int>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_SCHEDULED_CODES, codes.joinToString(",")).commit()
    }

    private fun isPrayerEnabled(key: String, s: PrayerNotificationSettings): Boolean = when (key.lowercase()) {
        "fajr" -> s.fajrEnabled
        "dhuhr" -> s.dhuhrEnabled
        "asr" -> s.asrEnabled
        "maghrib" -> s.maghribEnabled
        "isha" -> s.ishaEnabled
        else -> false
    }

    fun resolveLocalizedPrayerName(prayerKeyOrName: String, language: AppLanguage): String {
        val key = prayerKeyOrName.trim().lowercase()
        return if (key in PRAYER_KEYS) {
            AppLocalization.get(key, language)
        } else {
            prayerKeyOrName
        }
    }

    fun postPrayerNotification(
        context: Context,
        prayerName: String,
        prayerTime: String = "",
        soundType: String = _settings.value.adhanSound,
        vibration: Boolean = _settings.value.vibrationEnabled,
        notifId: Int = 100 + (PRAYER_KEYS.indexOf(prayerName.lowercase()).coerceAtLeast(0))
    ) {
        val appCtx = context.applicationContext
        init(appCtx)
        if (!hasNotificationPermission(appCtx)) return

        val lang = runCatching {
            UserPreferencesRepository(appCtx).language.value
        }.getOrDefault(AppLanguage.ENGLISH)
        val localizedPrayer = resolveLocalizedPrayerName(prayerName, lang)
        val titleSuffix = AppLocalization.get("notif_azan_title_suffix", lang)
        val stopLabel = AppLocalization.get("notif_azan_stop", lang)
        val bodyText = when (lang) {
            AppLanguage.URDU -> "$localizedPrayer کی نماز کا وقت ہو گیا ہے۔"
            AppLanguage.ARABIC -> "حان الآن وقت صلاة $localizedPrayer."
            AppLanguage.HINDI -> "$localizedPrayer की नमाज़ का समय हो गया है।"
            AppLanguage.ENGLISH -> "It is time for $localizedPrayer prayer."
        }

        val openApp = PendingIntent.getActivity(
            appCtx,
            notifId,
            Intent(appCtx, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(appCtx, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_STOP_AZAN
        }
        val stopPi = PendingIntent.getBroadcast(
            appCtx,
            990004,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = if (vibration) CHANNEL_ID else CHANNEL_ID_NOVIB
        val timeSuffix = if (prayerTime.isNotBlank()) " ($prayerTime)" else ""
        val builder = NotificationCompat.Builder(appCtx, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🕌 $localizedPrayer — $titleSuffix$timeSuffix")
            .setContentText(bodyText)
            .setContentIntent(openApp)
            .setAutoCancel(true)
            .setSilent(true)
            .setSound(null)
            .setDefaults(0)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)

        if (!vibration) {
            builder.setVibrate(longArrayOf(0L))
        }

        if (soundType != "silent" && soundType != "system") {
            builder.addAction(android.R.drawable.ic_media_pause, stopLabel, stopPi)
        }

        val nm = appCtx.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
        nm.notify(notifId, builder.build())
    }

    fun sendTestNotification(
        context: Context,
        prayerName: String = "Asr",
        prayerTime: String = "04:15 PM"
    ) {
        val appCtx = context.applicationContext
        init(appCtx)
        if (!hasNotificationPermission(appCtx)) return
        val cur = _settings.value
        val eventId = "test:${prayerName.lowercase()}:$prayerTime"
        if (!tryAcquirePrayerEventTrigger(eventId, cooldownMs = 1_500L)) {
            return
        }
        postPrayerNotification(appCtx, prayerName, prayerTime, cur.adhanSound, cur.vibrationEnabled, 101)
        if (cur.adhanSound != "silent" && cur.adhanSound != "system") {
            val svcIntent = Intent(appCtx, AzanPlaybackService::class.java).apply {
                action = AzanPlaybackService.ACTION_START_AZAN
                putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_KEY, prayerName.lowercase())
                putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TITLE, prayerName)
                putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TIME, prayerTime)
                putExtra(PrayerAlarmReceiver.EXTRA_ADHAN_SOUND, cur.adhanSound)
                putExtra(PrayerAlarmReceiver.EXTRA_EVENT_ID, "$eventId:${System.currentTimeMillis() / 1_500L}")
            }
            try {
                ContextCompat.startForegroundService(appCtx, svcIntent)
            } catch (_: Exception) { }
        }
    }

    fun stopAzanAudio(context: Context) {
        QuranAudioManager.stopMedia()
        AzanPlaybackService.resetActiveEventForTest()
        val appCtx = context.applicationContext
        runCatching {
            val stopIntent = Intent(appCtx, AzanPlaybackService::class.java).apply {
                action = AzanPlaybackService.ACTION_STOP_SERVICE
            }
            appCtx.stopService(stopIntent)
        }
    }
}
