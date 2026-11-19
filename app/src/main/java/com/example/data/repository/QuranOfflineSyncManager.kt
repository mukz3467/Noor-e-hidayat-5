package com.example.data.repository

import android.content.Context
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.net.Inet4Address
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

enum class SurahDownloadStatus {
    NOT_DOWNLOADED,
    BUNDLED_OFFLINE,
    DOWNLOADING,
    COMPLETED,
    FAILED
}

data class SurahDownloadProgress(
    val surahNumber: Int,
    val reciterId: String,
    val status: SurahDownloadStatus,
    val bytesDownloaded: Long = 0L,
    val totalBytes: Long = 0L,
    val progress: Float = 0f,
    val errorMessage: String? = null
)

data class DownloadedSurahAudioRecord(
    val key: String,
    val surahNumber: Int,
    val surahNameEnglish: String,
    val surahNameArabic: String,
    val reciterId: String,
    val reciterNameEnglish: String,
    val fileSizeBytes: Long,
    val lastModifiedMillis: Long,
    val filePath: String
)

enum class SyncStatus {
    IDLE,
    SYNCING,
    ALL_DOWNLOADED,
    SOME_FAILED,
    NO_NETWORK
}

data class SyncStatusSummary(
    val status: SyncStatus = SyncStatus.IDLE,
    val succeededCount: Int = 0,
    val failedCount: Int = 0,
    val totalCount: Int = 0,
    val message: String = ""
)

data class BulkEstimateResult(
    val reciterId: String,
    val reciterName: String,
    val totalSurahs: Int = 114,
    val alreadyOfflineCount: Int,
    val remainingToDownloadCount: Int,
    val estimatedRemainingBytes: Long,
    val existingBytes: Long,
    val availableDeviceBytes: Long,
    val verifiedByServerCount: Int,
    val estimatedByHeuristicCount: Int,
    val hasEnoughStorage: Boolean,
    val isNetworkAvailable: Boolean
)

enum class BulkPhase {
    IDLE,
    ESTIMATING,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    COMPLETED_WITH_ERRORS,
    CANCELLED
}

data class BulkQuranDownloadState(
    val reciterId: String = "mishary",
    val phase: BulkPhase = BulkPhase.IDLE,
    val currentSurahNumber: Int? = null,
    val currentSurahName: String = "",
    val completedSurahsCount: Int = 0,
    val skippedSurahsCount: Int = 0,
    val failedSurahs: List<Int> = emptyList(),
    val totalSurahs: Int = 114,
    val bytesDownloadedInRun: Long = 0L,
    val errorMessage: String? = null
) {
    val overallProgress: Float
        get() = ((completedSurahsCount + skippedSurahsCount).coerceIn(0, totalSurahs)).toFloat() / totalSurahs.toFloat()
}

object QuranOfflineSyncManager {

    // Backward-compatible Compose observable states
    val isSyncing = mutableStateOf(false)
    val syncProgress = mutableFloatStateOf(0f)
    val offlineAudioCount = mutableIntStateOf(0)
    val isAutoSyncComplete = mutableStateOf(false)

    private val _syncSummary = MutableStateFlow(SyncStatusSummary())
    val syncSummary: StateFlow<SyncStatusSummary> = _syncSummary.asStateFlow()

    private val _downloadedSurahRecords = MutableStateFlow<List<DownloadedSurahAudioRecord>>(emptyList())
    val downloadedSurahRecords: StateFlow<List<DownloadedSurahAudioRecord>> = _downloadedSurahRecords.asStateFlow()

    private val _totalOfflineAudioBytes = MutableStateFlow(0L)
    val totalOfflineAudioBytes: StateFlow<Long> = _totalOfflineAudioBytes.asStateFlow()

    private val _surahDownloadStates = MutableStateFlow<Map<String, SurahDownloadProgress>>(emptyMap())
    val surahDownloadStates: StateFlow<Map<String, SurahDownloadProgress>> = _surahDownloadStates.asStateFlow()

    private val _bulkDownloadState = MutableStateFlow(BulkQuranDownloadState())
    val bulkDownloadState: StateFlow<BulkQuranDownloadState> = _bulkDownloadState.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var syncJob: Job? = null
    private var bulkJob: Job? = null

    private val activeSurahJobs = ConcurrentHashMap<String, Job>()
    private val activeCalls = ConcurrentHashMap<String, Call>()
    private val failedSyncItems = mutableListOf<SyncItem>()
    @Volatile
    private var lastBulkPauseRequested = false

    /**
     * Test hooks for deterministic Robolectric + MockWebServer tests.
     */
    @Volatile
    var httpClientOverrideForTest: OkHttpClient? = null

    @Volatile
    var surahUrlResolverOverrideForTest: ((surahNumber: Int, reciterId: String) -> List<String>)? = null

    @Volatile
    var minSurahBytesOverrideForTest: ((surahNumber: Int) -> Long)? = null

    private val defaultHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .followRedirects(true)
            .dns(object : Dns {
                override fun lookup(hostname: String): List<java.net.InetAddress> {
                    return try {
                        val addresses = Dns.SYSTEM.lookup(hostname)
                        addresses.sortedBy { if (it is Inet4Address) 0 else 1 }
                    } catch (_: Exception) {
                        Dns.SYSTEM.lookup(hostname)
                    }
                }
            })
            .build()
    }

    internal fun activeHttpClient(): OkHttpClient = httpClientOverrideForTest ?: defaultHttpClient

    internal fun candidateUrlsForSurah(surahNumber: Int, reciterId: String): List<String> {
        return surahUrlResolverOverrideForTest?.invoke(surahNumber, reciterId)
            ?: QuranAudioSourceCatalog.getFullSurahCandidateUrls(surahNumber, reciterId)
    }

    internal fun requiredMinBytesForSurah(surahNumber: Int): Long {
        return minSurahBytesOverrideForTest?.invoke(surahNumber)
            ?: QuranAudioSourceCatalog.minExpectedSurahBytes(surahNumber)
    }

    fun surahKey(surahNumber: Int, reciterId: String): String {
        val norm = QuranAudioSourceCatalog.normalizeReciterId(reciterId)
        return "surah_${surahNumber}_$norm"
    }

    fun getOfflineStorageDir(context: Context): File {
        return File(context.filesDir, "quran_offline_audio").apply {
            if (!exists()) mkdirs()
        }
    }

    fun getOfflineSurahFile(context: Context, surahNumber: Int, reciterId: String): File? {
        val dir = getOfflineStorageDir(context)
        val file = File(dir, "${surahKey(surahNumber, reciterId)}.mp3")
        val minBytes = requiredMinBytesForSurah(surahNumber)
        return if (QuranAudioSourceCatalog.isValidMp3File(file, minBytes)) file else null
    }

    fun isSurahBundled(context: Context, surahNumber: Int, reciterId: String): Boolean {
        val key = surahKey(surahNumber, reciterId)
        return QuranAudioSourceCatalog.isBundledAssetPresent(context, "$key.mp3")
    }

    fun isSurahAvailableOffline(context: Context, surahNumber: Int, reciterId: String): Boolean {
        return isSurahBundled(context, surahNumber, reciterId) ||
            getOfflineSurahFile(context, surahNumber, reciterId) != null
    }

    fun getSurahOfflineStatus(context: Context, surahNumber: Int, reciterId: String): SurahDownloadProgress {
        val norm = QuranAudioSourceCatalog.normalizeReciterId(reciterId)
        val key = surahKey(surahNumber, norm)
        val existingState = _surahDownloadStates.value[key]
        if (existingState != null &&
            (existingState.status == SurahDownloadStatus.DOWNLOADING || existingState.status == SurahDownloadStatus.FAILED)
        ) {
            return existingState
        }

        val diskFile = getOfflineSurahFile(context, surahNumber, norm)
        if (diskFile != null) {
            return SurahDownloadProgress(
                surahNumber = surahNumber,
                reciterId = norm,
                status = SurahDownloadStatus.COMPLETED,
                bytesDownloaded = diskFile.length(),
                totalBytes = diskFile.length(),
                progress = 1f
            )
        }

        if (isSurahBundled(context, surahNumber, norm)) {
            return SurahDownloadProgress(
                surahNumber = surahNumber,
                reciterId = norm,
                status = SurahDownloadStatus.BUNDLED_OFFLINE,
                progress = 1f
            )
        }

        return SurahDownloadProgress(
            surahNumber = surahNumber,
            reciterId = norm,
            status = SurahDownloadStatus.NOT_DOWNLOADED
        )
    }

    /**
     * Scans persistent internal storage and updates exact downloaded Surah MP3 list, total bytes, and counts.
     * Cleans orphaned .tmp files when no download is actively running.
     */
    fun refreshOfflineIndex(context: Context) {
        val dir = getOfflineStorageDir(context)
        val regex = Regex("""^surah_(\d+)_([a-z0-9_]+)\.mp3$""")
        val records = mutableListOf<DownloadedSurahAudioRecord>()
        val stateUpdates = _surahDownloadStates.value.toMutableMap()
        var validTotalBytes = 0L
        var validFileCount = 0

        try {
            val files = dir.listFiles() ?: emptyArray()
            for (file in files) {
                if (!file.isFile) continue
                if (file.name.endsWith(".tmp", ignoreCase = true)) {
                    val baseKey = file.name.removeSuffix(".mp3.tmp").removeSuffix(".tmp")
                    if (!activeSurahJobs.containsKey(baseKey) && bulkJob?.isActive != true) {
                        runCatching { file.delete() }
                    }
                    continue
                }

                val match = regex.matchEntire(file.name)
                if (match != null) {
                    val sNum = match.groupValues[1].toIntOrNull() ?: continue
                    val recId = QuranAudioSourceCatalog.normalizeReciterId(match.groupValues[2])
                    val minBytes = requiredMinBytesForSurah(sNum)
                    if (QuranAudioSourceCatalog.isValidMp3File(file, minBytes)) {
                        val surah = QuranData.surahs.firstOrNull { it.number == sNum }
                        val reciter = QuranData.reciters.firstOrNull { it.id == recId }
                        val key = surahKey(sNum, recId)
                        val size = file.length()
                        validTotalBytes += size
                        validFileCount++
                        records.add(
                            DownloadedSurahAudioRecord(
                                key = key,
                                surahNumber = sNum,
                                surahNameEnglish = surah?.nameEnglish ?: "Surah $sNum",
                                surahNameArabic = surah?.nameArabic ?: "",
                                reciterId = recId,
                                reciterNameEnglish = reciter?.nameEnglish ?: recId,
                                fileSizeBytes = size,
                                lastModifiedMillis = file.lastModified(),
                                filePath = file.absolutePath
                            )
                        )
                        if (stateUpdates[key]?.status != SurahDownloadStatus.DOWNLOADING) {
                            stateUpdates[key] = SurahDownloadProgress(
                                surahNumber = sNum,
                                reciterId = recId,
                                status = SurahDownloadStatus.COMPLETED,
                                bytesDownloaded = size,
                                totalBytes = size,
                                progress = 1f
                            )
                        }
                    } else {
                        // Purge corrupt or truncated file so it never falsely appears available offline
                        runCatching { file.delete() }
                    }
                } else if (QuranAudioSourceCatalog.isValidMp3File(file, QuranAudioSourceCatalog.MIN_AYAH_BYTES)) {
                    validTotalBytes += file.length()
                    validFileCount++
                }
            }
        } catch (_: Exception) {}

        records.sortWith(compareBy<DownloadedSurahAudioRecord> { it.surahNumber }.thenBy { it.reciterId })
        _downloadedSurahRecords.value = records
        _totalOfflineAudioBytes.value = validTotalBytes
        _surahDownloadStates.value = stateUpdates
        offlineAudioCount.intValue = validFileCount
    }

    fun updateCachedCount(dir: File) {
        try {
            val validFiles = dir.listFiles { file ->
                file.isFile &&
                    !file.name.endsWith(".tmp", ignoreCase = true) &&
                    file.name.endsWith(".mp3", ignoreCase = true) &&
                    file.length() >= QuranAudioSourceCatalog.MIN_AYAH_BYTES
            } ?: emptyArray()
            offlineAudioCount.intValue = validFiles.size
        } catch (_: Exception) {}
    }

    /**
     * Background auto-sync of core offline audio.
     * - Never overwrites valid downloaded/bundled assets.
     * - Tracks succeeded vs failed items honestly.
     * - Never reports 100% / isAutoSyncComplete = true when failures occur or when offline.
     */
    fun startAutoSync(context: Context, retryOnlyFailed: Boolean = false) {
        val appContext = context.applicationContext
        if (syncJob?.isActive == true) return

        refreshOfflineIndex(appContext)

        if (!QuranAudioSourceCatalog.isNetworkAvailable(appContext)) {
            isSyncing.value = false
            isAutoSyncComplete.value = false
            _syncSummary.value = SyncStatusSummary(
                status = SyncStatus.NO_NETWORK,
                message = "No network connection. Bundled and previously downloaded Surahs remain available offline."
            )
            return
        }

        isSyncing.value = true
        isAutoSyncComplete.value = false
        _syncSummary.value = SyncStatusSummary(status = SyncStatus.SYNCING, message = "Syncing core offline audio...")

        val job = scope.launch {
            val offlineDir = getOfflineStorageDir(appContext)
            val targets = if (retryOnlyFailed && failedSyncItems.isNotEmpty()) {
                failedSyncItems.toList()
            } else {
                getAutoSyncItems(appContext)
            }

            val newFailures = mutableListOf<SyncItem>()
            var succeeded = 0
            val total = targets.size

            if (total == 0) {
                syncProgress.floatValue = 1f
                isAutoSyncComplete.value = true
                _syncSummary.value = SyncStatusSummary(
                    status = SyncStatus.ALL_DOWNLOADED,
                    succeededCount = 0,
                    failedCount = 0,
                    totalCount = 0,
                    message = "All core offline audio files are already verified on device."
                )
                return@launch
            }

            for ((index, item) in targets.withIndex()) {
                if (!isActive) break
                val targetFile = File(offlineDir, item.filename)

                val alreadyBundled = QuranAudioSourceCatalog.isBundledAssetPresent(appContext, item.filename)
                val alreadyOnDisk = QuranAudioSourceCatalog.isValidMp3File(targetFile, item.minBytes)

                val ok = if (alreadyBundled || alreadyOnDisk) {
                    true
                } else if (!QuranAudioSourceCatalog.isNetworkAvailable(appContext)) {
                    false
                } else {
                    downloadFromCandidatesSync(
                        callKey = "autosync_${item.filename}",
                        candidates = item.urls,
                        targetFile = targetFile,
                        minBytes = item.minBytes,
                        resumeExistingTmp = false,
                        deleteTmpOnFail = true
                    ).isSuccess
                }

                if (ok) {
                    succeeded++
                } else {
                    newFailures.add(item)
                }

                val progressNow = (index + 1).toFloat() / total.toFloat()
                syncProgress.floatValue = if (newFailures.isEmpty()) progressNow else progressNow.coerceAtMost(0.99f)
            }

            failedSyncItems.clear()
            failedSyncItems.addAll(newFailures)
            refreshOfflineIndex(appContext)

            val allOk = newFailures.isEmpty()
            val networkLost = !QuranAudioSourceCatalog.isNetworkAvailable(appContext) && newFailures.isNotEmpty()
            isAutoSyncComplete.value = allOk
            syncProgress.floatValue = if (total == 0) 1f else (succeeded.toFloat() / total.toFloat())
            _syncSummary.value = when {
                allOk -> SyncStatusSummary(
                    status = SyncStatus.ALL_DOWNLOADED,
                    succeededCount = succeeded,
                    failedCount = 0,
                    totalCount = total,
                    message = "All $total core audio files synced and verified offline."
                )
                networkLost -> SyncStatusSummary(
                    status = SyncStatus.NO_NETWORK,
                    succeededCount = succeeded,
                    failedCount = newFailures.size,
                    totalCount = total,
                    message = "Sync interrupted: no network connection ($succeeded/$total complete, ${newFailures.size} failed)."
                )
                else -> SyncStatusSummary(
                    status = SyncStatus.SOME_FAILED,
                    succeededCount = succeeded,
                    failedCount = newFailures.size,
                    totalCount = total,
                    message = "$succeeded of $total synced; ${newFailures.size} downloads failed. Tap Retry."
                )
            }
        }
        job.invokeOnCompletion {
            isSyncing.value = false
        }
        syncJob = job
    }

    fun retryFailedSyncItems(context: Context) {
        startAutoSync(context, retryOnlyFailed = true)
    }

    /**
     * Download a single Surah MP3 cleanly for genuine offline playback.
     */
    fun downloadSurah(
        context: Context,
        surahNumber: Int,
        reciterId: String,
        onComplete: (Result<File>) -> Unit = {}
    ) {
        val appContext = context.applicationContext
        val normReciter = QuranAudioSourceCatalog.normalizeReciterId(reciterId)
        val key = surahKey(surahNumber, normReciter)

        if (activeSurahJobs[key]?.isActive == true) return

        val existingDisk = getOfflineSurahFile(appContext, surahNumber, normReciter)
        if (existingDisk != null) {
            updateSurahState(
                key,
                SurahDownloadProgress(
                    surahNumber = surahNumber,
                    reciterId = normReciter,
                    status = SurahDownloadStatus.COMPLETED,
                    bytesDownloaded = existingDisk.length(),
                    totalBytes = existingDisk.length(),
                    progress = 1f
                )
            )
            onComplete(Result.success(existingDisk))
            return
        }

        if (UserPreferencesRepository.isDownloadBlockedByWifiOnly(appContext)) {
            val msg = "Wi-Fi Only Downloads is enabled in Settings. Connect to Wi-Fi or turn it off."
            updateSurahState(
                key,
                SurahDownloadProgress(
                    surahNumber = surahNumber,
                    reciterId = normReciter,
                    status = SurahDownloadStatus.FAILED,
                    errorMessage = msg
                )
            )
            onComplete(Result.failure(IllegalStateException(msg)))
            return
        }

        if (!QuranAudioSourceCatalog.isNetworkAvailable(appContext)) {
            val msg = "No internet connection. Connect to the internet to download this Surah."
            updateSurahState(
                key,
                SurahDownloadProgress(
                    surahNumber = surahNumber,
                    reciterId = normReciter,
                    status = SurahDownloadStatus.FAILED,
                    errorMessage = msg
                )
            )
            onComplete(Result.failure(IllegalStateException(msg)))
            return
        }

        val job = scope.launch {
            val result = downloadSurahSuspend(appContext, surahNumber, normReciter, resumeExistingTmp = false)
            refreshOfflineIndex(appContext)
            withContext(Dispatchers.Main) {
                onComplete(result)
            }
            activeSurahJobs.remove(key)
        }
        activeSurahJobs[key] = job
    }

    suspend fun downloadSurahSuspend(
        context: Context,
        surahNumber: Int,
        reciterId: String,
        resumeExistingTmp: Boolean = false
    ): Result<File> = withContext(Dispatchers.IO) {
        val normReciter = QuranAudioSourceCatalog.normalizeReciterId(reciterId)
        val key = surahKey(surahNumber, normReciter)
        val targetFile = File(getOfflineStorageDir(context), "$key.mp3")
        val minBytes = requiredMinBytesForSurah(surahNumber)

        if (QuranAudioSourceCatalog.isValidMp3File(targetFile, minBytes)) {
            updateSurahState(
                key,
                SurahDownloadProgress(
                    surahNumber = surahNumber,
                    reciterId = normReciter,
                    status = SurahDownloadStatus.COMPLETED,
                    bytesDownloaded = targetFile.length(),
                    totalBytes = targetFile.length(),
                    progress = 1f
                )
            )
            return@withContext Result.success(targetFile)
        }

        if (UserPreferencesRepository.isDownloadBlockedByWifiOnly(context)) {
            val err = "Wi-Fi Only Downloads is enabled in Settings. Connect to Wi-Fi or turn it off."
            updateSurahState(
                key,
                SurahDownloadProgress(
                    surahNumber = surahNumber,
                    reciterId = normReciter,
                    status = SurahDownloadStatus.FAILED,
                    errorMessage = err
                )
            )
            return@withContext Result.failure(IllegalStateException(err))
        }

        if (!QuranAudioSourceCatalog.isNetworkAvailable(context)) {
            val err = "No internet connection."
            updateSurahState(
                key,
                SurahDownloadProgress(
                    surahNumber = surahNumber,
                    reciterId = normReciter,
                    status = SurahDownloadStatus.FAILED,
                    errorMessage = err
                )
            )
            return@withContext Result.failure(IllegalStateException(err))
        }

        updateSurahState(
            key,
            SurahDownloadProgress(
                surahNumber = surahNumber,
                reciterId = normReciter,
                status = SurahDownloadStatus.DOWNLOADING,
                bytesDownloaded = 0L,
                totalBytes = 0L,
                progress = 0f
            )
        )

        val candidates = candidateUrlsForSurah(surahNumber, normReciter)
        val result = downloadFromCandidatesSync(
            callKey = key,
            candidates = candidates,
            targetFile = targetFile,
            minBytes = minBytes,
            resumeExistingTmp = resumeExistingTmp,
            deleteTmpOnFail = !resumeExistingTmp,
            onProgress = { downloaded, total ->
                val pct = if (total > 0L) (downloaded.toFloat() / total.toFloat()).coerceIn(0f, 0.99f) else 0f
                updateSurahState(
                    key,
                    SurahDownloadProgress(
                        surahNumber = surahNumber,
                        reciterId = normReciter,
                        status = SurahDownloadStatus.DOWNLOADING,
                        bytesDownloaded = downloaded,
                        totalBytes = total,
                        progress = pct
                    )
                )
            }
        )

        result.fold(
            onSuccess = { file ->
                updateSurahState(
                    key,
                    SurahDownloadProgress(
                        surahNumber = surahNumber,
                        reciterId = normReciter,
                        status = SurahDownloadStatus.COMPLETED,
                        bytesDownloaded = file.length(),
                        totalBytes = file.length(),
                        progress = 1f
                    )
                )
                Result.success(file)
            },
            onFailure = { ex ->
                if (ex is CancellationException && lastBulkPauseRequested) {
                    updateSurahState(
                        key,
                        SurahDownloadProgress(
                            surahNumber = surahNumber,
                            reciterId = normReciter,
                            status = SurahDownloadStatus.NOT_DOWNLOADED
                        )
                    )
                } else if (ex is CancellationException) {
                    updateSurahState(
                        key,
                        SurahDownloadProgress(
                            surahNumber = surahNumber,
                            reciterId = normReciter,
                            status = SurahDownloadStatus.NOT_DOWNLOADED
                        )
                    )
                } else {
                    updateSurahState(
                        key,
                        SurahDownloadProgress(
                            surahNumber = surahNumber,
                            reciterId = normReciter,
                            status = SurahDownloadStatus.FAILED,
                            errorMessage = ex.message ?: "Download failed"
                        )
                    )
                }
                Result.failure(ex)
            }
        )
    }

    fun cancelSurahDownload(context: Context, surahNumber: Int, reciterId: String) {
        val normReciter = QuranAudioSourceCatalog.normalizeReciterId(reciterId)
        val key = surahKey(surahNumber, normReciter)
        activeCalls.remove(key)?.cancel()
        activeSurahJobs.remove(key)?.cancel()
        val tmpFile = File(getOfflineStorageDir(context), "$key.mp3.tmp")
        runCatching { if (tmpFile.exists()) tmpFile.delete() }
        updateSurahState(key, getSurahOfflineStatus(context, surahNumber, normReciter).let {
            if (it.status == SurahDownloadStatus.DOWNLOADING) {
                SurahDownloadProgress(surahNumber, normReciter, SurahDownloadStatus.NOT_DOWNLOADED)
            } else it
        })
    }

    fun deleteSurahAudio(context: Context, surahNumber: Int, reciterId: String): Boolean {
        val normReciter = QuranAudioSourceCatalog.normalizeReciterId(reciterId)
        val key = surahKey(surahNumber, normReciter)
        activeCalls.remove(key)?.cancel()
        activeSurahJobs.remove(key)?.cancel()

        QuranAudioManager.stopIfPlayingKey(key)

        val dir = getOfflineStorageDir(context)
        val mp3File = File(dir, "$key.mp3")
        val tmpFile = File(dir, "$key.mp3.tmp")
        val cacheFile = File(File(context.cacheDir, "quran_audio"), "$key.mp3")

        val deleted = (if (mp3File.exists()) mp3File.delete() else false)
        runCatching { if (tmpFile.exists()) tmpFile.delete() }
        runCatching { if (cacheFile.exists()) cacheFile.delete() }

        val updatedStatus = if (isSurahBundled(context, surahNumber, normReciter)) {
            SurahDownloadStatus.BUNDLED_OFFLINE
        } else {
            SurahDownloadStatus.NOT_DOWNLOADED
        }
        updateSurahState(key, SurahDownloadProgress(surahNumber, normReciter, updatedStatus))
        refreshOfflineIndex(context)
        return deleted
    }

    /**
     * Measures real total download size across all remaining Surahs (1..114) for a single reciter using HTTP HEAD / Range checks.
     */
    suspend fun estimateReciterFullQuranSize(
        context: Context,
        reciterId: String
    ): BulkEstimateResult = withContext(Dispatchers.IO) {
        val normReciter = QuranAudioSourceCatalog.normalizeReciterId(reciterId)
        val reciterName = QuranData.reciters.firstOrNull { it.id == normReciter }?.nameEnglish ?: normReciter
        val dir = getOfflineStorageDir(context)
        val availableBytes = dir.usableSpace
        val online = QuranAudioSourceCatalog.isNetworkAvailable(context)

        var alreadyOfflineCount = 0
        var existingBytes = 0L
        val missingSurahs = mutableListOf<Int>()

        for (s in 1..114) {
            val disk = getOfflineSurahFile(context, s, normReciter)
            if (disk != null) {
                alreadyOfflineCount++
                existingBytes += disk.length()
            } else if (isSurahBundled(context, s, normReciter)) {
                alreadyOfflineCount++
            } else {
                missingSurahs.add(s)
            }
        }

        var remainingEstimatedBytes = 0L
        var verifiedByServer = 0
        var heuristicCount = 0

        if (online && missingSurahs.isNotEmpty()) {
            val semaphore = Semaphore(8)
            val results = coroutineScope {
                missingSurahs.map { surahNum ->
                    async {
                        semaphore.withPermit {
                            val minBytes = requiredMinBytesForSurah(surahNum)
                            val urls = candidateUrlsForSurah(surahNum, normReciter)
                            var measuredSize: Long? = null
                            for (url in urls) {
                                val meta = QuranAudioSourceCatalog.verifyUrlOnline(activeHttpClient(), url, minBytes)
                                if (meta != null && meta.contentLengthBytes >= minBytes) {
                                    measuredSize = meta.contentLengthBytes
                                    break
                                }
                            }
                            measuredSize
                        }
                    }
                }.awaitAll()
            }

            for ((index, measured) in results.withIndex()) {
                val surahNum = missingSurahs[index]
                if (measured != null) {
                    remainingEstimatedBytes += measured
                    verifiedByServer++
                } else {
                    // Honest heuristic proportional to Surah ayah count if server omits Content-Length
                    val ayahs = QuranData.surahs.firstOrNull { it.number == surahNum }?.ayahCount ?: 10
                    remainingEstimatedBytes += (ayahs * 85_000L).coerceAtLeast(150_000L)
                    heuristicCount++
                }
            }
        } else {
            for (surahNum in missingSurahs) {
                val ayahs = QuranData.surahs.firstOrNull { it.number == surahNum }?.ayahCount ?: 10
                remainingEstimatedBytes += (ayahs * 85_000L).coerceAtLeast(150_000L)
                heuristicCount++
            }
        }

        BulkEstimateResult(
            reciterId = normReciter,
            reciterName = reciterName,
            totalSurahs = 114,
            alreadyOfflineCount = alreadyOfflineCount,
            remainingToDownloadCount = missingSurahs.size,
            estimatedRemainingBytes = remainingEstimatedBytes,
            existingBytes = existingBytes,
            availableDeviceBytes = availableBytes,
            verifiedByServerCount = verifiedByServer,
            estimatedByHeuristicCount = heuristicCount,
            hasEnoughStorage = availableBytes >= remainingEstimatedBytes + 10_485_760L || missingSurahs.isEmpty(),
            isNetworkAvailable = online
        )
    }

    /**
     * Downloads all 114 Surahs for a single reciter, skipping already downloaded/bundled Surahs,
     * with pause, resume, cancel, and retry-failed controls.
     */
    fun startFullQuranDownload(
        context: Context,
        reciterId: String,
        targetSurahList: List<Int> = (1..114).toList()
    ) {
        val appContext = context.applicationContext
        val normReciter = QuranAudioSourceCatalog.normalizeReciterId(reciterId)
        if (bulkJob?.isActive == true) return

        lastBulkPauseRequested = false

        if (UserPreferencesRepository.isDownloadBlockedByWifiOnly(appContext)) {
            _bulkDownloadState.value = BulkQuranDownloadState(
                reciterId = normReciter,
                phase = BulkPhase.COMPLETED_WITH_ERRORS,
                failedSurahs = targetSurahList.filter { !isSurahAvailableOffline(appContext, it, normReciter) },
                totalSurahs = 114,
                errorMessage = "Wi-Fi Only Downloads is enabled in Settings. Connect to Wi-Fi or disable the restriction."
            )
            return
        }

        if (!QuranAudioSourceCatalog.isNetworkAvailable(appContext)) {
            _bulkDownloadState.value = BulkQuranDownloadState(
                reciterId = normReciter,
                phase = BulkPhase.COMPLETED_WITH_ERRORS,
                failedSurahs = targetSurahList.filter { !isSurahAvailableOffline(appContext, it, normReciter) },
                totalSurahs = 114,
                errorMessage = "No internet connection. Cannot start full Quran download."
            )
            return
        }

        _bulkDownloadState.value = BulkQuranDownloadState(
            reciterId = normReciter,
            phase = BulkPhase.DOWNLOADING,
            completedSurahsCount = 0,
            skippedSurahsCount = 0,
            totalSurahs = 114
        )

        bulkJob = scope.launch {
            var completedCount = 0
            var skippedCount = 0
            val failedList = mutableListOf<Int>()
            var totalBytesInRun = 0L

            // Count already offline Surahs across all 114 first
            for (s in 1..114) {
                if (!targetSurahList.contains(s) && isSurahAvailableOffline(appContext, s, normReciter)) {
                    skippedCount++
                }
            }

            _bulkDownloadState.value = _bulkDownloadState.value.copy(
                skippedSurahsCount = skippedCount
            )

            for (s in targetSurahList) {
                if (!isActive || lastBulkPauseRequested) break

                if (isSurahAvailableOffline(appContext, s, normReciter)) {
                    skippedCount++
                    _bulkDownloadState.value = _bulkDownloadState.value.copy(
                        skippedSurahsCount = skippedCount,
                        currentSurahNumber = s,
                        currentSurahName = QuranData.surahs.firstOrNull { it.number == s }?.nameEnglish ?: "Surah $s"
                    )
                    continue
                }

                val surahName = QuranData.surahs.firstOrNull { it.number == s }?.nameEnglish ?: "Surah $s"
                _bulkDownloadState.value = _bulkDownloadState.value.copy(
                    currentSurahNumber = s,
                    currentSurahName = surahName
                )

                val res = downloadSurahSuspend(appContext, s, normReciter, resumeExistingTmp = true)
                if (lastBulkPauseRequested) break

                res.fold(
                    onSuccess = { downloadedFile ->
                        completedCount++
                        totalBytesInRun += downloadedFile.length()
                    },
                    onFailure = { ex ->
                        if (ex !is CancellationException) {
                            failedList.add(s)
                        }
                    }
                )

                refreshOfflineIndex(appContext)
                _bulkDownloadState.value = _bulkDownloadState.value.copy(
                    completedSurahsCount = completedCount,
                    skippedSurahsCount = skippedCount,
                    failedSurahs = failedList.toList(),
                    bytesDownloadedInRun = totalBytesInRun
                )
            }

            refreshOfflineIndex(appContext)

            val finalPhase = when {
                lastBulkPauseRequested -> BulkPhase.PAUSED
                !isActive -> BulkPhase.CANCELLED
                failedList.isNotEmpty() -> BulkPhase.COMPLETED_WITH_ERRORS
                else -> BulkPhase.COMPLETED
            }

            _bulkDownloadState.value = _bulkDownloadState.value.copy(
                phase = finalPhase,
                currentSurahNumber = null,
                currentSurahName = "",
                failedSurahs = failedList.toList(),
                errorMessage = if (failedList.isNotEmpty()) {
                    "${failedList.size} Surah downloads failed. Tap Retry Failed to resume."
                } else null
            )
        }
    }

    fun pauseFullQuranDownload() {
        lastBulkPauseRequested = true
        val state = _bulkDownloadState.value
        val currentKey = state.currentSurahNumber?.let { surahKey(it, state.reciterId) }
        if (currentKey != null) {
            activeCalls.remove(currentKey)?.cancel()
        }
        bulkJob?.cancel()
        bulkJob = null
        _bulkDownloadState.value = state.copy(phase = BulkPhase.PAUSED)
    }

    fun resumeFullQuranDownload(context: Context) {
        val state = _bulkDownloadState.value
        val remaining = (1..114).filter { !isSurahAvailableOffline(context, it, state.reciterId) }
        startFullQuranDownload(context, state.reciterId, remaining)
    }

    fun retryFailedBulkSurahs(context: Context) {
        val state = _bulkDownloadState.value
        val failed = state.failedSurahs.ifEmpty {
            (1..114).filter { !isSurahAvailableOffline(context, it, state.reciterId) }
        }
        startFullQuranDownload(context, state.reciterId, failed)
    }

    fun cancelFullQuranDownload(context: Context) {
        lastBulkPauseRequested = false
        val state = _bulkDownloadState.value
        val currentKey = state.currentSurahNumber?.let { surahKey(it, state.reciterId) }
        if (currentKey != null) {
            activeCalls.remove(currentKey)?.cancel()
        }
        bulkJob?.cancel()
        bulkJob = null
        // Clean incomplete .tmp files cleanly on cancellation
        try {
            getOfflineStorageDir(context).listFiles { f -> f.name.endsWith(".tmp") }?.forEach {
                runCatching { it.delete() }
            }
        } catch (_: Exception) {}
        _bulkDownloadState.value = BulkQuranDownloadState(reciterId = state.reciterId, phase = BulkPhase.CANCELLED)
    }

    /**
     * Verified atomic download helper used by both QuranOfflineSyncManager and QuranAudioManager.
     * - Streams into .tmp
     * - Enforces HTTP 2xx, audio Content-Type, Content-Length match, MP3 header, and proportional minBytes
     * - Supports safe Range resume when resumeExistingTmp == true and server responds with 206 Partial Content
     */
    internal fun downloadFromCandidatesSync(
        callKey: String,
        candidates: List<String>,
        targetFile: File,
        minBytes: Long,
        resumeExistingTmp: Boolean,
        deleteTmpOnFail: Boolean,
        onProgress: (bytesDownloaded: Long, totalBytes: Long) -> Unit = { _, _ -> }
    ): Result<File> {
        val parent = targetFile.parentFile ?: return Result.failure(IllegalStateException("Invalid storage directory"))
        if (!parent.exists()) parent.mkdirs()

        if (parent.usableSpace < minBytes + 512_000L) {
            return Result.failure(IllegalStateException("Insufficient device storage space."))
        }

        val tempFile = File(parent, "${targetFile.name}.tmp")
        var lastError: Throwable? = null

        for (url in candidates) {
            val existingTmpBytes = if (resumeExistingTmp && tempFile.exists()) tempFile.length() else 0L
            val attempt = tryDownloadSingleUrl(
                callKey = callKey,
                url = url,
                tempFile = tempFile,
                targetFile = targetFile,
                minBytes = minBytes,
                resumeFromBytes = existingTmpBytes,
                deleteTmpOnFail = deleteTmpOnFail,
                onProgress = onProgress
            )
            if (attempt.isSuccess) {
                return attempt
            }
            val err = attempt.exceptionOrNull()
            if (err is CancellationException) {
                return attempt
            }
            lastError = err
        }

        if (deleteTmpOnFail) {
            runCatching { if (tempFile.exists()) tempFile.delete() }
        }
        return Result.failure(lastError ?: IllegalStateException("Audio download failed from all verified mirrors."))
    }

    private fun tryDownloadSingleUrl(
        callKey: String,
        url: String,
        tempFile: File,
        targetFile: File,
        minBytes: Long,
        resumeFromBytes: Long,
        deleteTmpOnFail: Boolean,
        onProgress: (bytesDownloaded: Long, totalBytes: Long) -> Unit
    ): Result<File> {
        return try {
            val reqBuilder = Request.Builder()
                .url(url)
                .header("User-Agent", "Noor-e-Hidayat/3.0 (Android; OfflineAudio)")
            if (resumeFromBytes > 0L) {
                reqBuilder.header("Range", "bytes=$resumeFromBytes-")
            }

            val call = activeHttpClient().newCall(reqBuilder.build())
            activeCalls[callKey] = call

            val result = call.execute().use { response ->
                if (response.code == 416 && tempFile.exists() && QuranAudioSourceCatalog.isValidMp3File(tempFile, minBytes)) {
                    // Range already satisfied completely
                    val moved = moveTempToTarget(tempFile, targetFile, minBytes)
                    return@use if (moved != null) Result.success(moved) else Result.failure(IllegalStateException("Invalid audio file"))
                }

                if (!response.isSuccessful) {
                    return@use Result.failure(IllegalStateException("HTTP ${response.code} from audio server"))
                }

                val contentType = response.header("Content-Type")
                if (!QuranAudioSourceCatalog.isValidAudioContentType(contentType)) {
                    return@use Result.failure(IllegalStateException("Invalid audio Content-Type: $contentType"))
                }

                val isResuming = response.code == 206 && resumeFromBytes > 0L
                val body = response.body ?: return@use Result.failure(IllegalStateException("Empty HTTP response body"))
                val responseLength = body.contentLength()
                val expectedTotalBytes = when {
                    isResuming && responseLength > 0L -> resumeFromBytes + responseLength
                    !isResuming && responseLength > 0L -> responseLength
                    else -> -1L
                }

                if (expectedTotalBytes in 0 until minBytes) {
                    return@use Result.failure(
                        IllegalStateException("Audio file too small ($expectedTotalBytes bytes; minimum $minBytes bytes required).")
                    )
                }

                val freeSpace = targetFile.parentFile?.usableSpace ?: 0L
                val neededAdditional = if (expectedTotalBytes > 0L) (expectedTotalBytes - if (isResuming) resumeFromBytes else 0L) else minBytes
                if (freeSpace < neededAdditional + 524_288L) {
                    return@use Result.failure(IllegalStateException("Insufficient storage space on device."))
                }

                var writtenTotal = if (isResuming) resumeFromBytes else 0L
                FileOutputStream(tempFile, isResuming).use { output ->
                    body.byteStream().use { input ->
                        val buffer = ByteArray(16 * 1024)
                        var lastProgressEmit = 0L
                        while (true) {
                            if (call.isCanceled()) {
                                throw CancellationException("Download cancelled")
                            }
                            val read = input.read(buffer)
                            if (read <= 0) break
                            output.write(buffer, 0, read)
                            writtenTotal += read
                            if (writtenTotal - lastProgressEmit >= 32 * 1024L || (expectedTotalBytes in 1..writtenTotal)) {
                                lastProgressEmit = writtenTotal
                                onProgress(writtenTotal, expectedTotalBytes)
                            }
                        }
                        output.flush()
                    }
                }
                onProgress(writtenTotal, expectedTotalBytes)

                // Verify exact Content-Length completion when server provided it
                if (expectedTotalBytes > 0L && tempFile.length() != expectedTotalBytes) {
                    return@use Result.failure(
                        IllegalStateException("Incomplete download (${tempFile.length()} of $expectedTotalBytes bytes).")
                    )
                }

                // Verify proportional minimum byte threshold and valid MP3 frame / ID3 header
                if (!validateTempMp3(tempFile, minBytes)) {
                    return@use Result.failure(IllegalStateException("Downloaded file failed MP3 integrity validation."))
                }

                val finalFile = moveTempToTarget(tempFile, targetFile, minBytes)
                    ?: return@use Result.failure(IllegalStateException("Failed to finalize downloaded MP3 file."))

                Result.success(finalFile)
            }

            result
        } catch (ce: CancellationException) {
            if (!lastBulkPauseRequested && deleteTmpOnFail) {
                runCatching { if (tempFile.exists()) tempFile.delete() }
            }
            Result.failure(ce)
        } catch (ex: Exception) {
            if (deleteTmpOnFail) {
                runCatching { if (tempFile.exists()) tempFile.delete() }
            }
            Result.failure(ex)
        } finally {
            activeCalls.remove(callKey)
        }
    }

    private fun validateTempMp3(tempFile: File, minBytes: Long): Boolean {
        if (!tempFile.exists() || tempFile.length() < minBytes) return false
        return try {
            val header = ByteArray(4096)
            val read = tempFile.inputStream().use { it.read(header) }
            QuranAudioSourceCatalog.hasValidMp3Header(header, read)
        } catch (_: Exception) {
            false
        }
    }

    private fun moveTempToTarget(tempFile: File, targetFile: File, minBytes: Long): File? {
        if (targetFile.exists()) {
            runCatching { targetFile.delete() }
        }
        if (tempFile.renameTo(targetFile) && QuranAudioSourceCatalog.isValidMp3File(targetFile, minBytes)) {
            return targetFile
        }
        return try {
            tempFile.inputStream().use { input ->
                targetFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            runCatching { tempFile.delete() }
            if (QuranAudioSourceCatalog.isValidMp3File(targetFile, minBytes)) targetFile else {
                runCatching { targetFile.delete() }
                null
            }
        } catch (_: Exception) {
            runCatching { targetFile.delete() }
            null
        }
    }

    private fun updateSurahState(key: String, progress: SurahDownloadProgress) {
        val next = _surahDownloadStates.value.toMutableMap()
        next[key] = progress
        _surahDownloadStates.value = next
    }

    private data class SyncItem(
        val filename: String,
        val urls: List<String>,
        val minBytes: Long
    )

    private fun getAutoSyncItems(context: Context): List<SyncItem> {
        val list = mutableListOf<SyncItem>()

        // 1. Azan Alarm Bells (only download if not already bundled/present)
        listOf("makkah", "madina", "fajr").forEach { azanType ->
            list.add(
                SyncItem(
                    filename = "azan_${azanType}.mp3",
                    urls = QuranAudioSourceCatalog.getAzanCandidateUrls(azanType),
                    minBytes = QuranAudioSourceCatalog.MIN_AZAN_BYTES
                )
            )
        }

        // 2. High-Frequency Core Surahs (skip files already bundled in assets/audio)
        val coreSurahs = listOf(1, 112, 113, 114, 108, 103, 110, 111, 109, 67, 36, 55)
        for (s in coreSurahs) {
            list.add(
                SyncItem(
                    filename = "surah_${s}_mishary.mp3",
                    urls = candidateUrlsForSurah(s, "mishary"),
                    minBytes = requiredMinBytesForSurah(s)
                )
            )
        }

        // 3. Learning Academy Ayahs (Al-Fatihah 1..7 and Al-Ikhlas 1..4)
        for (a in 1..7) {
            list.add(
                SyncItem(
                    filename = "ayah_1_${a}_mishary.mp3",
                    urls = QuranAudioSourceCatalog.getAyahCandidateUrls(1, a, "mishary"),
                    minBytes = QuranAudioSourceCatalog.MIN_AYAH_BYTES
                )
            )
        }
        for (a in 1..4) {
            list.add(
                SyncItem(
                    filename = "ayah_112_${a}_mishary.mp3",
                    urls = QuranAudioSourceCatalog.getAyahCandidateUrls(112, a, "mishary"),
                    minBytes = QuranAudioSourceCatalog.MIN_AYAH_BYTES
                )
            )
        }

        return list.filter { item ->
            !QuranAudioSourceCatalog.isBundledAssetPresent(context, item.filename)
        }
    }

    /**
     * Test utility helper to reset in-memory states cleanly between Robolectric tests.
     */
    internal fun resetStateForTests() {
        syncJob?.cancel()
        syncJob = null
        bulkJob?.cancel()
        bulkJob = null
        activeSurahJobs.values.forEach { it.cancel() }
        activeSurahJobs.clear()
        activeCalls.values.forEach { it.cancel() }
        activeCalls.clear()
        failedSyncItems.clear()
        lastBulkPauseRequested = false
        isSyncing.value = false
        syncProgress.floatValue = 0f
        offlineAudioCount.intValue = 0
        isAutoSyncComplete.value = false
        _syncSummary.value = SyncStatusSummary()
        _downloadedSurahRecords.value = emptyList()
        _totalOfflineAudioBytes.value = 0L
        _surahDownloadStates.value = emptyMap()
        _bulkDownloadState.value = BulkQuranDownloadState()
    }
}
