package com.example.data.podcast

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.data.repository.YouTubeRepository
import java.util.concurrent.TimeUnit

/**
 * Unified ~30-minute background refresh worker for BOTH:
 * 1. **Islamic Podcasts** (349 verified channels across Urdu, English, Arabic — cumulative archive up to 4,500 long-form videos, zero Shorts)
 * 2. **Islamic Lectures** (325 verified channels across 16 categories — cumulative archive up to 3,000+ / 5,000+ long-form lectures, zero Shorts)
 *
 * Scheduled via Android WorkManager at an approximate 30-minute interval with [NetworkType.CONNECTED].
 * Note on Android OS scheduling: WorkManager periodic tasks are subject to Android Doze mode,
 * App Standby buckets, and device battery optimization, so exact 30-minute wall-clock execution
 * cannot be guaranteed when the device is sleeping. Foreground 30-minute tickers inside both
 * PodcastsScreen and LecturesScreen complement this worker whenever the app is open.
 */
class PodcastRefreshWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            if (!PodcastLibraryRepository.isOnline(applicationContext)) {
                return Result.success()
            }
            // 1. Cumulatively refresh Urdu, English, and Arabic Podcast archives (349 verified channels, zero Shorts)
            val podcastTotal = PodcastLibraryRepository.refreshAllLanguagesCumulative(
                context = applicationContext,
                sweepAllBatches = false
            )
            // 2. Refresh stale individual scholar profiles
            val scholarRefreshed = PodcastLibraryRepository.refreshStaleLanguageBatch(
                context = applicationContext,
                maxScholars = MAX_SCHOLARS_PER_RUN
            )
            // 3. Cumulatively refresh Islamic Lectures catalog (325 verified channels, zero Shorts)
            val lectureSummary = YouTubeRepository.refreshCumulativeLectureCatalog(
                context = applicationContext,
                batchSize = YouTubeRepository.DEFAULT_BG_SWEEP_BATCH_SIZE
            )
            Log.d(
                TAG,
                "30-min refresh complete: podcasts=$podcastTotal videos, scholars=$scholarRefreshed, " +
                    "lectures=${lectureSummary.totalAccumulatedLectures} (+${lectureSummary.newVideosAdded} new)"
            )
            Result.success()
        } catch (e: Exception) {
            Log.w(TAG, "Background podcast/lecture refresh failed: ${e::class.java.simpleName}")
            if (runAttemptCount < 2) Result.retry() else Result.success()
        }
    }

    companion object {
        private const val TAG = "PodcastRefreshWorker"
        const val PERIODIC_WORK_NAME = "podcast_library_periodic_refresh"
        const val REFRESH_INTERVAL_MINUTES = 30L
        const val MAX_SCHOLARS_PER_RUN = 4

        fun buildPeriodicRequest() = PeriodicWorkRequestBuilder<PodcastRefreshWorker>(
            REFRESH_INTERVAL_MINUTES,
            TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        fun schedulePeriodicRefresh(context: Context) {
            runCatching {
                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    PERIODIC_WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    buildPeriodicRequest()
                )
            }
        }

        fun enqueueImmediateRefresh(context: Context) {
            runCatching {
                val request = OneTimeWorkRequestBuilder<PodcastRefreshWorker>()
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .build()
                WorkManager.getInstance(context).enqueue(request)
            }
        }
    }
}
