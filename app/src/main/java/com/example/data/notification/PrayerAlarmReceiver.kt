package com.example.data.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.data.ramadan.RamadanAlarmScheduler
import com.example.data.ramadan.RamadanRepository

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val appCtx = context.applicationContext
        when (intent.action) {
            ACTION_STOP_AZAN -> {
                PrayerNotificationManager.stopAzanAudio(appCtx)
            }
            ACTION_REPLENISH_ALARMS -> {
                PrayerNotificationManager.init(appCtx)
                PrayerNotificationManager.scheduleNextRollingWindow(appCtx, reason = "daily_replenish")
            }
            DailyReminderScheduler.ACTION_DAILY_REMINDER -> {
                val reminderType = intent.getStringExtra(DailyReminderScheduler.EXTRA_REMINDER_TYPE)
                DailyReminderScheduler.handleReminderAlarm(appCtx, reminderType)
            }
            RamadanAlarmScheduler.ACTION_RAMADAN_ALARM -> {
                val alarmKey = intent.getStringExtra(RamadanAlarmScheduler.EXTRA_ALARM_KEY) ?: return
                val title = intent.getStringExtra(RamadanAlarmScheduler.EXTRA_ALARM_TITLE) ?: ""
                val timeFormatted = intent.getStringExtra(RamadanAlarmScheduler.EXTRA_ALARM_TIME) ?: ""
                val soundTypeId = intent.getStringExtra(RamadanAlarmScheduler.EXTRA_SOUND_TYPE) ?: "silent"
                val snoozeMinutes = intent.getIntExtra(RamadanAlarmScheduler.EXTRA_SNOOZE_MINUTES, 10)
                val isSnoozed = intent.getBooleanExtra(RamadanAlarmScheduler.EXTRA_IS_SNOOZED, false)
                RamadanAlarmScheduler.handleAlarmFired(
                    context = appCtx,
                    alarmKey = alarmKey,
                    title = title,
                    timeFormatted = timeFormatted,
                    soundTypeId = soundTypeId,
                    snoozeMinutes = snoozeMinutes,
                    isSnoozed = isSnoozed
                )
            }
            RamadanAlarmScheduler.ACTION_RAMADAN_SNOOZE -> {
                val alarmKey = intent.getStringExtra(RamadanAlarmScheduler.EXTRA_ALARM_KEY) ?: return
                val title = intent.getStringExtra(RamadanAlarmScheduler.EXTRA_ALARM_TITLE) ?: ""
                val soundTypeId = intent.getStringExtra(RamadanAlarmScheduler.EXTRA_SOUND_TYPE) ?: "silent"
                val snoozeMinutes = intent.getIntExtra(RamadanAlarmScheduler.EXTRA_SNOOZE_MINUTES, 10)
                val notifId = intent.getIntExtra(RamadanAlarmScheduler.EXTRA_NOTIF_ID, -1)
                if (notifId > 0) {
                    (appCtx.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancel(notifId)
                }
                PrayerNotificationManager.stopAzanAudio(appCtx)
                RamadanAlarmScheduler.scheduleSnoozeAlarm(
                    context = appCtx,
                    alarmKey = alarmKey,
                    title = title,
                    snoozeMinutes = snoozeMinutes,
                    soundTypeId = soundTypeId
                )
            }
            RamadanAlarmScheduler.ACTION_RAMADAN_DISMISS -> {
                val notifId = intent.getIntExtra(RamadanAlarmScheduler.EXTRA_NOTIF_ID, -1)
                RamadanAlarmScheduler.dismissAlarmNotification(appCtx, notifId)
            }
            RamadanAlarmScheduler.ACTION_RAMADAN_ROLLOVER -> {
                RamadanRepository.rescheduleAlarmsOfflineFallback(appCtx, "midnight_rollover")
            }
            else -> {
                PrayerNotificationManager.init(appCtx)
                val prayerKey = intent.getStringExtra(EXTRA_PRAYER_KEY)
                    ?: intent.getStringExtra(EXTRA_ID)
                    ?: return
                val prayerTitle = intent.getStringExtra(EXTRA_PRAYER_TITLE)
                    ?: intent.getStringExtra(EXTRA_NAME)
                    ?: prayerKey.replaceFirstChar { it.uppercaseChar() }
                val prayerTime = intent.getStringExtra(EXTRA_PRAYER_TIME) ?: ""
                val adhanSound = intent.getStringExtra(EXTRA_ADHAN_SOUND)
                    ?: intent.getStringExtra(EXTRA_SOUND)
                    ?: PrayerNotificationManager.settings.value.adhanSound
                val eventId = intent.getStringExtra(EXTRA_EVENT_ID)
                    ?: "alarm:${prayerKey.lowercase()}:$prayerTime"

                if (!PrayerNotificationManager.tryAcquirePrayerEventTrigger(eventId, cooldownMs = 120_000L)) {
                    return
                }

                PrayerNotificationManager.postPrayerNotification(
                    context = appCtx,
                    prayerName = prayerKey,
                    prayerTime = prayerTime,
                    soundType = adhanSound
                )

                if (adhanSound != "silent" && adhanSound != "system") {
                    val serviceIntent = Intent(appCtx, AzanPlaybackService::class.java).apply {
                        action = AzanPlaybackService.ACTION_START_AZAN
                        putExtra(EXTRA_PRAYER_KEY, prayerKey)
                        putExtra(EXTRA_PRAYER_TITLE, prayerTitle)
                        putExtra(EXTRA_PRAYER_TIME, prayerTime)
                        putExtra(EXTRA_ADHAN_SOUND, adhanSound)
                        putExtra(EXTRA_EVENT_ID, eventId)
                    }
                    try {
                        ContextCompat.startForegroundService(appCtx, serviceIntent)
                    } catch (_: Exception) {
                        // Notification remains visible safely if FGS start is blocked in background
                    }
                }

                PrayerNotificationManager.scheduleNextRollingWindow(appCtx, reason = "alarm_fired:$prayerKey")
            }
        }
    }

    companion object {
        const val ACTION_PRAYER_ALARM = "com.example.ACTION_PRAYER_ALARM"
        const val ACTION_REPLENISH_ALARMS = "com.example.ACTION_REPLENISH_ALARMS"
        const val ACTION_STOP_AZAN = "com.example.ACTION_STOP_AZAN"

        const val EXTRA_PRAYER_KEY = "extra_prayer_key"
        const val EXTRA_PRAYER_TITLE = "extra_prayer_title"
        const val EXTRA_PRAYER_TIME = "extra_prayer_time"
        const val EXTRA_ADHAN_SOUND = "extra_adhan_sound"
        const val EXTRA_EVENT_ID = "extra_prayer_event_id"

        const val EXTRA_ID = "prayer_alarm_id"
        const val EXTRA_NAME = "prayer_alarm_name"
        const val EXTRA_SOUND = "prayer_alarm_sound"
        const val EXTRA_VIBRATE = "prayer_alarm_vibrate"
    }
}
