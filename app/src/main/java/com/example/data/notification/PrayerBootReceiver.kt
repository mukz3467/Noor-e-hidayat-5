package com.example.data.notification

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.location.AppLocationManager
import com.example.data.podcast.PodcastRefreshWorker
import com.example.data.ramadan.RamadanDailyRefreshWorker
import com.example.data.ramadan.RamadanRepository
import com.example.data.repository.PrayerCalculator

class PrayerBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        val supportedActions = setOf(
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
        )
        if (action !in supportedActions) return

        val appCtx = context.applicationContext ?: context
        AppLocationManager.init(appCtx)
        PrayerCalculator.init(appCtx)
        PrayerNotificationManager.init(appCtx)
        PrayerNotificationManager.rescheduleAllAlarms(
            context = appCtx,
            reason = "system_event:$action"
        )
        DailyReminderScheduler.rescheduleAll(appCtx)
        RamadanRepository.init(appCtx)
        RamadanRepository.rescheduleAlarmsOfflineFallback(
            context = appCtx,
            reason = "system_event:$action"
        )
        RamadanDailyRefreshWorker.schedulePeriodicRefresh(appCtx)
        PodcastRefreshWorker.schedulePeriodicRefresh(appCtx)
    }
}
