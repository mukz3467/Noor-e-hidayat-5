package com.example.data.ramadan

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.location.UserLocation
import com.example.data.model.AppLanguage
import com.example.data.notification.AzanPlaybackService
import com.example.data.notification.DailyReminderScheduler
import com.example.data.notification.PrayerAlarmReceiver
import com.example.data.notification.PrayerNotificationManager
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object RamadanAlarmScheduler {

    const val ACTION_RAMADAN_ALARM = "com.example.ACTION_RAMADAN_ALARM"
    const val ACTION_RAMADAN_SNOOZE = "com.example.ACTION_RAMADAN_SNOOZE"
    const val ACTION_RAMADAN_DISMISS = "com.example.ACTION_RAMADAN_DISMISS"
    const val ACTION_RAMADAN_ROLLOVER = "com.example.ACTION_RAMADAN_ROLLOVER"

    const val EXTRA_ALARM_KEY = "extra_ramadan_alarm_key"
    const val EXTRA_ALARM_TITLE = "extra_ramadan_alarm_title"
    const val EXTRA_ALARM_BODY = "extra_ramadan_alarm_body"
    const val EXTRA_ALARM_TIME = "extra_ramadan_alarm_time"
    const val EXTRA_SOUND_TYPE = "extra_ramadan_sound_type"
    const val EXTRA_SNOOZE_MINUTES = "extra_ramadan_snooze_minutes"
    const val EXTRA_NOTIF_ID = "extra_ramadan_notif_id"
    const val EXTRA_IS_SNOOZED = "extra_ramadan_is_snoozed"

    const val CHANNEL_ID_VIB = "noor_ramadan_silent_v2_vib"
    const val CHANNEL_ID_NOVIB = "noor_ramadan_silent_v2_novib"
    const val CHANNEL_ID_CHIME = "noor_ramadan_v1_chime"

    private const val PREFS_NAME = "ramadan_alarm_scheduler_prefs"
    private const val KEY_SCHEDULED_CODES = "ramadan_scheduled_codes"

    private const val REQUEST_CODE_BASE = 880000
    private const val REQUEST_CODE_ROLLOVER = 880900
    private const val REQUEST_CODE_SNOOZE = 880950
    private const val NOTIF_ID_RAMADAN_BASE = 3100

    fun localizedTitleForKey(alarmKey: String, lang: AppLanguage): String? = when (alarmKey) {
        "sehri_pre" -> AppLocalization.get("ram_alarm_sehri_pre_title", lang)
        "sehri_fajr" -> AppLocalization.get("ram_alarm_sehri_end_title", lang)
        "iftar_pre" -> AppLocalization.get("ram_alarm_iftar_pre_title", lang)
        "iftar_main" -> AppLocalization.get("ram_alarm_iftar_main_title", lang)
        "night_taraweeh" -> AppLocalization.get("ram_alarm_taraweeh_title", lang)
        "night_tahajjud" -> AppLocalization.get("ram_alarm_tahajjud_title", lang)
        "night_last10" -> AppLocalization.get("ram_alarm_last10_title", lang)
        else -> null
    }

    private fun snoozeSuffix(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> "(اسنوز)"
        AppLanguage.ARABIC -> "(غفوة)"
        AppLanguage.HINDI -> "(स्नूज़)"
        AppLanguage.ENGLISH -> "(Snoozed)"
    }

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
        runCatching { nm.deleteNotificationChannel("noor_ramadan_v1_vib") }
        runCatching { nm.deleteNotificationChannel("noor_ramadan_v1_novib") }

        val vibChannel = NotificationChannel(
            CHANNEL_ID_VIB,
            "Ramadan Sehri & Iftar Alarms",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Sehri, Iftar and Ramadan worship reminders (sound managed by Adhan player)"
            setSound(null, null)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 500, 250, 500)
        }

        val noVibChannel = NotificationChannel(
            CHANNEL_ID_NOVIB,
            "Ramadan Silent Visual Alarms",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Silent visual Ramadan Sehri & Iftar alerts"
            setSound(null, null)
            enableVibration(false)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val chimeChannel = NotificationChannel(
            CHANNEL_ID_CHIME,
            "Ramadan Tone Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Ramadan alerts with system notification sound"
            if (defaultSoundUri != null) {
                setSound(
                    defaultSoundUri,
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
            }
            enableVibration(true)
        }

        nm.createNotificationChannel(vibChannel)
        nm.createNotificationChannel(noVibChannel)
        nm.createNotificationChannel(chimeChannel)
    }

    fun cancelAllAlarms(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val existingCodes = prefs.getStringSet(KEY_SCHEDULED_CODES, emptySet()).orEmpty()
        for (codeStr in existingCodes) {
            val code = codeStr.toIntOrNull() ?: continue
            val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                action = if (code == REQUEST_CODE_ROLLOVER) ACTION_RAMADAN_ROLLOVER else ACTION_RAMADAN_ALARM
            }
            val pi = PendingIntent.getBroadcast(
                context,
                code,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pi != null) {
                am?.cancel(pi)
                runCatching { pi.cancel() }
            }
        }
        prefs.edit().putStringSet(KEY_SCHEDULED_CODES, emptySet()).apply()
    }

    fun rescheduleAlarms(
        context: Context,
        alarmConfig: RamadanAlarmConfig,
        nightWorship: NightWorshipConfig,
        location: UserLocation,
        calcConfig: PrayerCalculator.PrayerCalculationConfig,
        confirmedStartDateKey: String?,
        confirmedTotalDays: Int,
        todayTimingOverride: DailyRamadanTimingRecord? = null,
        nowMillis: Long = System.currentTimeMillis()
    ): List<UpcomingRamadanAlarmInfo> {
        ensureChannels(context)
        cancelAllAlarms(context)

        if (!alarmConfig.alarmsMasterEnabled) {
            return emptyList()
        }

        val am = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val canExact = PrayerNotificationManager.canScheduleExactAlarms(context)
        val tz = TimeZone.getTimeZone(location.timeZoneId)
        val lang = runCatching { UserPreferencesRepository(context).language.value }.getOrDefault(AppLanguage.ENGLISH)
        val upcoming = mutableListOf<UpcomingRamadanAlarmInfo>()
        val scheduledCodes = mutableSetOf<String>()
        val timeFmt = SimpleDateFormat("hh:mm a", Locale.US).apply { timeZone = tz }

        // Schedule across today + next 2 days (rolling window replenished daily)
        for (dayOffset in 0..2) {
            val dayCal = Calendar.getInstance(tz).apply {
                timeInMillis = nowMillis
                add(Calendar.DAY_OF_MONTH, dayOffset)
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val periodForDay = RamadanRepository.evaluatePeriodForCalendar(
                cal = dayCal,
                confirmedStartDateKey = confirmedStartDateKey,
                confirmedTotalDays = confirmedTotalDays
            )

            // If user opted to run Ramadan alarms only during Ramadan, auto-skip & stop outside Ramadan
            val allowSehriIftarDay = !alarmConfig.activeOnlyDuringRamadan || periodForDay.isRamadanActive
            if (!allowSehriIftarDay) continue

            val dayDateKey = formatDateKey(dayCal)
            val timing = if (dayOffset == 0 && todayTimingOverride != null && todayTimingOverride.isForDateKey(dayDateKey)) {
                todayTimingOverride
            } else {
                RamadanPrayerTimingSync.computeOfflineTiming(
                    location = location,
                    config = calcConfig,
                    targetCal = dayCal,
                    nowMillis = nowMillis
                )
            }

            val candidates = mutableListOf<Triple<String, String, Long>>()

            if (alarmConfig.sehriPreAlarmEnabled) {
                val trigger = timing.fajrTriggerMillis - (alarmConfig.sehriOffsetMinutes.coerceIn(1, 180) * 60_000L)
                candidates.add(
                    Triple(
                        "sehri_pre",
                        AppLocalization.get("ram_alarm_sehri_pre_title", lang),
                        trigger
                    )
                )
            }
            if (alarmConfig.sehriFajrEndAlarmEnabled) {
                candidates.add(
                    Triple(
                        "sehri_fajr",
                        AppLocalization.get("ram_alarm_sehri_end_title", lang),
                        timing.fajrTriggerMillis
                    )
                )
            }
            if (alarmConfig.iftarPreReminderEnabled) {
                val trigger = timing.maghribTriggerMillis - (alarmConfig.iftarOffsetMinutes.coerceIn(1, 120) * 60_000L)
                candidates.add(
                    Triple(
                        "iftar_pre",
                        AppLocalization.get("ram_alarm_iftar_pre_title", lang),
                        trigger
                    )
                )
            }
            if (alarmConfig.iftarMainAlarmEnabled) {
                candidates.add(
                    Triple(
                        "iftar_main",
                        AppLocalization.get("ram_alarm_iftar_main_title", lang),
                        timing.maghribTriggerMillis
                    )
                )
            }

            if (nightWorship.taraweehReminderEnabled && periodForDay.isRamadanActive) {
                val tCal = (dayCal.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, nightWorship.taraweehHour.coerceIn(0, 23))
                    set(Calendar.MINUTE, nightWorship.taraweehMinute.coerceIn(0, 59))
                }
                candidates.add(
                    Triple(
                        "night_taraweeh",
                        AppLocalization.get("ram_alarm_taraweeh_title", lang),
                        tCal.timeInMillis
                    )
                )
            }

            if (nightWorship.tahajjudReminderEnabled) {
                val tahajjudTrigger = if (nightWorship.tahajjudUseCustomFixedTime) {
                    (dayCal.clone() as Calendar).apply {
                        set(Calendar.HOUR_OF_DAY, nightWorship.tahajjudFixedHour.coerceIn(0, 23))
                        set(Calendar.MINUTE, nightWorship.tahajjudFixedMinute.coerceIn(0, 59))
                    }.timeInMillis
                } else {
                    timing.fajrTriggerMillis - (nightWorship.tahajjudOffsetBeforeFajrMinutes.coerceIn(15, 180) * 60_000L)
                }
                candidates.add(
                    Triple(
                        "night_tahajjud",
                        AppLocalization.get("ram_alarm_tahajjud_title", lang),
                        tahajjudTrigger
                    )
                )
            }

            val rDay = periodForDay.currentRamadanDay ?: 0
            if (nightWorship.lastTenNightsReminderEnabled && periodForDay.isRamadanActive && rDay >= 20) {
                val qCal = (dayCal.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, nightWorship.lastTenNightsHour.coerceIn(0, 23))
                    set(Calendar.MINUTE, nightWorship.lastTenNightsMinute.coerceIn(0, 59))
                }
                candidates.add(
                    Triple(
                        "night_last10",
                        AppLocalization.get("ram_alarm_last10_title", lang),
                        qCal.timeInMillis
                    )
                )
            }

            candidates.forEachIndexed { idx, (key, title, triggerMillis) ->
                if (triggerMillis > nowMillis) {
                    val requestCode = REQUEST_CODE_BASE + dayOffset * 20 + idx
                    val formatted = timeFmt.format(Date(triggerMillis))
                    val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                        action = ACTION_RAMADAN_ALARM
                        putExtra(EXTRA_ALARM_KEY, key)
                        putExtra(EXTRA_ALARM_TITLE, title)
                        putExtra(EXTRA_ALARM_TIME, formatted)
                        putExtra(EXTRA_SOUND_TYPE, alarmConfig.alarmSoundType.id)
                        putExtra(EXTRA_SNOOZE_MINUTES, alarmConfig.snoozeMinutes)
                    }
                    val pi = PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    val didExact = if (am != null) scheduleSingleAlarm(am, triggerMillis, pi, canExact) else false
                    scheduledCodes.add(requestCode.toString())
                    upcoming.add(
                        UpcomingRamadanAlarmInfo(
                            alarmKey = key,
                            title = title,
                            triggerAtMillis = triggerMillis,
                            formattedTime = formatted,
                            isExact = didExact,
                            requestCode = requestCode
                        )
                    )
                }
            }
        }

        // Schedule daily midnight rollover alarm so next day's Sehri & Iftar update automatically
        if (am != null) {
            val rolloverCal = Calendar.getInstance(tz).apply {
                timeInMillis = nowMillis
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 5)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val rolloverIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                action = ACTION_RAMADAN_ROLLOVER
            }
            val rolloverPi = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_ROLLOVER,
                rolloverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, rolloverCal.timeInMillis, rolloverPi)
            scheduledCodes.add(REQUEST_CODE_ROLLOVER.toString())
        }

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_SCHEDULED_CODES, scheduledCodes)
            .apply()

        return upcoming.sortedBy { it.triggerAtMillis }
    }

    fun scheduleSnoozeAlarm(
        context: Context,
        alarmKey: String,
        title: String,
        snoozeMinutes: Int,
        soundTypeId: String,
        nowMillis: Long = System.currentTimeMillis()
    ) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val canExact = PrayerNotificationManager.canScheduleExactAlarms(context)
        val lang = runCatching { UserPreferencesRepository(context).language.value }.getOrDefault(AppLanguage.ENGLISH)
        val resolvedBase = localizedTitleForKey(alarmKey, lang)
            ?: title.ifBlank { AppLocalization.get("ramadan_companion", lang) }
        val triggerAt = nowMillis + snoozeMinutes.coerceIn(1, 30) * 60_000L
        val timeFmt = SimpleDateFormat("hh:mm a", Locale.US)
        val formatted = timeFmt.format(Date(triggerAt))
        val requestCode = REQUEST_CODE_SNOOZE + (alarmKey.hashCode() and 0x1F)
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = ACTION_RAMADAN_ALARM
            putExtra(EXTRA_ALARM_KEY, alarmKey)
            putExtra(EXTRA_ALARM_TITLE, "$resolvedBase ${snoozeSuffix(lang)}")
            putExtra(EXTRA_ALARM_TIME, formatted)
            putExtra(EXTRA_SOUND_TYPE, soundTypeId)
            putExtra(EXTRA_SNOOZE_MINUTES, snoozeMinutes)
            putExtra(EXTRA_IS_SNOOZED, true)
        }
        val pi = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        scheduleSingleAlarm(am, triggerAt, pi, canExact)
    }

    fun handleAlarmFired(
        context: Context,
        alarmKey: String,
        title: String,
        timeFormatted: String,
        soundTypeId: String,
        snoozeMinutes: Int,
        isTest: Boolean = false,
        isSnoozed: Boolean = false
    ) {
        ensureChannels(context)
        val prefsRepo = UserPreferencesRepository(context)
        val appSettings = prefsRepo.appSettings.value
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (!isTest && DailyReminderScheduler.isWithinDndWindow(currentHour, appSettings)) {
            return
        }

        if (!PrayerNotificationManager.hasNotificationPermission(context)) {
            return
        }

        val soundType = AlarmSoundType.fromId(soundTypeId)
        val config = RamadanRepository.state.value.alarmConfig
        val channelId = when {
            soundType == AlarmSoundType.NOTIFICATION_TONE -> CHANNEL_ID_CHIME
            config.vibrationEnabled -> CHANNEL_ID_VIB
            else -> CHANNEL_ID_NOVIB
        }

        val notifId = if (isTest) NOTIF_ID_RAMADAN_BASE + 99 else NOTIF_ID_RAMADAN_BASE + (alarmKey.hashCode() and 0x1F)
        val lang = appSettings.language
        val resolvedBase = localizedTitleForKey(alarmKey, lang)
            ?: title.ifBlank { AppLocalization.get("ramadan_companion", lang) }
        val resolvedTitle = if (isSnoozed) "$resolvedBase ${snoozeSuffix(lang)}" else resolvedBase

        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openPi = PendingIntent.getActivity(
            context,
            notifId,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = ACTION_RAMADAN_SNOOZE
            putExtra(EXTRA_ALARM_KEY, alarmKey)
            putExtra(EXTRA_ALARM_TITLE, resolvedBase)
            putExtra(EXTRA_SOUND_TYPE, soundTypeId)
            putExtra(EXTRA_SNOOZE_MINUTES, snoozeMinutes)
            putExtra(EXTRA_NOTIF_ID, notifId)
        }
        val snoozePi = PendingIntent.getBroadcast(
            context,
            notifId + 200,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = ACTION_RAMADAN_DISMISS
            putExtra(EXTRA_NOTIF_ID, notifId)
        }
        val dismissPi = PendingIntent.getBroadcast(
            context,
            notifId + 400,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val bodyText = if (timeFormatted.isNotBlank()) {
            "${AppLocalization.get("ram_scheduled_for", lang)} $timeFormatted"
        } else {
            AppLocalization.get("ramadan_companion", lang)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(resolvedTitle)
            .setContentText(bodyText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bodyText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(openPi)
            .addAction(
                android.R.drawable.ic_popup_reminder,
                "${AppLocalization.get("ram_snooze", lang)} (${snoozeMinutes}m)",
                snoozePi
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                AppLocalization.get("ram_dismiss", lang),
                dismissPi
            )

        if (soundType != AlarmSoundType.NOTIFICATION_TONE) {
            builder.setSilent(true)
            builder.setSound(null)
            builder.setDefaults(0)
            builder.setOnlyAlertOnce(true)
            if (!config.vibrationEnabled) {
                builder.setVibrate(longArrayOf(0L))
            }
        }

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        nm?.notify(notifId, builder.build())

        val adhanAudio = soundType.adhanAudioKey
        if (!adhanAudio.isNullOrBlank() && soundType != AlarmSoundType.SILENT) {
            val ramadanEventId = "ramadan:$alarmKey:$timeFormatted"
            if (PrayerNotificationManager.tryAcquirePrayerEventTrigger(ramadanEventId, cooldownMs = 60_000L)) {
                val serviceIntent = Intent(context, AzanPlaybackService::class.java).apply {
                    this.action = AzanPlaybackService.ACTION_START_AZAN
                    putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_KEY, alarmKey)
                    putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TITLE, resolvedBase)
                    putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TIME, timeFormatted)
                    putExtra(PrayerAlarmReceiver.EXTRA_ADHAN_SOUND, adhanAudio)
                    putExtra(PrayerAlarmReceiver.EXTRA_EVENT_ID, ramadanEventId)
                }
                try {
                    ContextCompat.startForegroundService(context, serviceIntent)
                } catch (_: Exception) {
                    // Notification is already posted safely even if FGS is restricted in background
                }
            }
        }
    }

    fun dismissAlarmNotification(context: Context, notifId: Int) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        if (notifId > 0) {
            nm?.cancel(notifId)
        }
        PrayerNotificationManager.stopAzanAudio(context)
    }

    private fun scheduleSingleAlarm(
        am: AlarmManager,
        triggerAtMillis: Long,
        pi: PendingIntent,
        allowExact: Boolean
    ): Boolean {
        if (allowExact) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
                } else {
                    am.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
                }
                return true
            } catch (_: SecurityException) {
                // Fall back gracefully
            }
        }
        AlarmManagerCompat.setAndAllowWhileIdle(am, AlarmManager.RTC_WAKEUP, triggerAtMillis, pi)
        return false
    }
}
