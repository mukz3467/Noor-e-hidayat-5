package com.example.data.ramadan

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.data.location.AppLocationManager
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import java.util.concurrent.TimeUnit

class RamadanDailyRefreshWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            AppLocationManager.init(applicationContext)
            PrayerCalculator.init(applicationContext)
            IslamicCalendarRepository.init(applicationContext)
            RamadanRepository.init(applicationContext)
            RamadanRepository.refreshTodayTimingsAndReschedule(
                context = applicationContext,
                preferNetwork = true,
                reason = "workmanager_daily_refresh"
            )
            Result.success()
        } catch (_: Exception) {
            // Fall back cleanly with offline calculation so alarms remain intact
            runCatching {
                RamadanRepository.rescheduleAlarmsOfflineFallback(applicationContext, reason = "workmanager_retry_fallback")
            }
            Result.retry()
        }
    }

    companion object {
        const val PERIODIC_WORK_NAME = "ramadan_daily_timing_refresh"
        const val ONE_TIME_WORK_NAME = "ramadan_immediate_timing_refresh"

        fun schedulePeriodicRefresh(context: Context) {
            runCatching {
                val request = PeriodicWorkRequestBuilder<RamadanDailyRefreshWorker>(
                    12,
                    TimeUnit.HOURS
                ).build()
                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    PERIODIC_WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
            }
        }

        fun enqueueImmediateRefresh(context: Context) {
            runCatching {
                val request = OneTimeWorkRequestBuilder<RamadanDailyRefreshWorker>().build()
                WorkManager.getInstance(context).enqueue(request)
            }
        }
    }
}
