package com.example

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.repository.AudioPlaybackSource
import com.example.data.repository.BulkPhase
import com.example.data.repository.QuranAudioManager
import com.example.data.repository.QuranAudioSourceCatalog
import com.example.data.repository.QuranData
import com.example.data.repository.QuranOfflineSyncManager
import com.example.data.repository.SurahDownloadStatus
import com.example.data.repository.SyncStatus
import com.example.ui.screens.QuranReaderScreen
import com.example.ui.theme.NooreHidayatTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowMediaPlayer
import org.robolectric.shadows.util.DataSource
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class QuranAudioOfflineVerificationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Application

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        QuranAudioManager.stopMedia()
        QuranAudioManager.clearAudioError()
        QuranAudioManager.networkRequestCountForTest.set(0L)
        QuranOfflineSyncManager.resetStateForTests()
        QuranOfflineSyncManager.httpClientOverrideForTest = null
        QuranOfflineSyncManager.surahUrlResolverOverrideForTest = null
        QuranOfflineSyncManager.minSurahBytesOverrideForTest = null
        QuranAudioSourceCatalog.isOnlineOverrideForTest = true
        QuranAudioSourceCatalog.clearVerifiedUrlCache()
        cleanOfflineDirectories()
    }

    @After
    fun tearDown() {
        QuranAudioManager.stopMedia()
        QuranOfflineSyncManager.resetStateForTests()
        QuranOfflineSyncManager.httpClientOverrideForTest = null
        QuranOfflineSyncManager.surahUrlResolverOverrideForTest = null
        QuranOfflineSyncManager.minSurahBytesOverrideForTest = null
        QuranAudioSourceCatalog.isOnlineOverrideForTest = null
        cleanOfflineDirectories()
    }

    private fun cleanOfflineDirectories() {
        QuranOfflineSyncManager.getOfflineStorageDir(context).listFiles()?.forEach { it.delete() }
        File(context.cacheDir, "quran_audio").listFiles()?.forEach { it.delete() }
    }

    private fun buildValidMp3Bytes(sizeBytes: Int): ByteArray {
        val bytes = ByteArray(sizeBytes)
        // ID3v2 header ("ID3\x03\x00") + valid MPEG1 Layer3 frame sync (0xFF 0xFB 0x90 0x00)
        bytes[0] = 'I'.code.toByte()
        bytes[1] = 'D'.code.toByte()
        bytes[2] = '3'.code.toByte()
        bytes[3] = 3
        bytes[4] = 0
        bytes[10] = 0xFF.toByte()
        bytes[11] = 0xFB.toByte()
        bytes[12] = 0x90.toByte()
        bytes[13] = 0x00.toByte()
        return bytes
    }

    private fun clientWithInterceptor(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private fun makeResponse(
        request: okhttp3.Request,
        code: Int,
        contentType: String,
        bytes: ByteArray,
        extraHeaders: Map<String, String> = emptyMap()
    ): Response {
        val builder = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(if (code in 200..299) "OK" else "Error")
            .header("Content-Type", contentType)
            .header("Content-Length", bytes.size.toString())
        extraHeaders.forEach { (k, v) -> builder.header(k, v) }
        val body = if (request.method.equals("HEAD", ignoreCase = true)) {
            ByteArray(0).toResponseBody(contentType.toMediaType())
        } else {
            bytes.toResponseBody(contentType.toMediaType())
        }
        return builder.body(body).build()
    }

    @Test
    fun all114SurahsAndAll4RecitersHaveDistinctFullSurahUrlsWithoutAyahFallback() {
        assertEquals(4, QuranData.reciters.size)
        val reciterIds = listOf("mishary", "abdulbasit", "husary", "ghamdi")

        for (surah in 1..114) {
            val perReciterUrls = reciterIds.map { rec ->
                val urls = QuranAudioSourceCatalog.getFullSurahCandidateUrls(surah, rec)
                assertTrue("Expected full Surah mirrors for $rec surah $surah", urls.isNotEmpty())
                urls.forEach { url ->
                    assertFalse(
                        "Full Surah URL must NEVER point to single-ayah 001.mp3 stub: $url",
                        url.contains("everyayah.com", ignoreCase = true)
                    )
                }
                urls.first()
            }
            assertEquals(
                "Reciters must map to distinct recitation directories for Surah $surah",
                4,
                perReciterUrls.distinct().size
            )
        }

        // Verify Ayah URLs never silently switch another reciter to Alafasy
        val husaryAyah = QuranAudioSourceCatalog.getAyahCandidateUrls(2, 255, "husary")
        assertTrue(husaryAyah.all { !it.contains("Alafasy", ignoreCase = true) && !it.contains("alafasy", ignoreCase = true) })
        val ghamdiAyah = QuranAudioSourceCatalog.getAyahCandidateUrls(2, 255, "ghamdi")
        assertEquals(1, ghamdiAyah.size)
        assertTrue(ghamdiAyah.first().contains("Ghamadi_40kbps/002255.mp3"))
    }

    @Test
    fun longSurahAlBaqarahRejectsSingleAyahStubAndAcceptsFullLengthMp3() {
        runBlocking {
            // Surah 2 has 286 ayahs -> minimum proportional size > 715,000 bytes
            val stub20Kb = buildValidMp3Bytes(20_000)
            val full800Kb = buildValidMp3Bytes(800_000)

            // First test: server returns a 20KB single-ayah stub for Al-Baqarah -> MUST be rejected!
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                makeResponse(chain.request(), 200, "audio/mpeg", stub20Kb)
            }

            val stubResult = QuranOfflineSyncManager.downloadSurahSuspend(context, 2, "mishary")
            assertTrue("20KB single-ayah payload must be rejected for Surah Al-Baqarah", stubResult.isFailure)
            assertNull(QuranOfflineSyncManager.getOfflineSurahFile(context, 2, "mishary"))

            // Second test: server returns proportional 800KB MP3 -> MUST succeed!
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                makeResponse(chain.request(), 200, "audio/mpeg", full800Kb)
            }

            val validResult = QuranOfflineSyncManager.downloadSurahSuspend(context, 2, "mishary")
            assertTrue("Valid full-length Al-Baqarah MP3 should succeed", validResult.isSuccess)
            val saved = validResult.getOrNull()!!
            assertEquals(800_000L, saved.length())
            assertFalse(saved.name.endsWith(".tmp"))
            assertTrue(QuranOfflineSyncManager.isSurahAvailableOffline(context, 2, "mishary"))
        }
    }

    @Test
    fun downloadValidationRejects404HtmlAndCorruptBytesWithoutLeavingTmpOrFakeFiles() {
        runBlocking {
            // Case A: HTTP 404
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                makeResponse(chain.request(), 404, "text/plain", "Not Found".toByteArray())
            }
            val res404 = QuranOfflineSyncManager.downloadSurahSuspend(context, 18, "husary")
            assertTrue(res404.isFailure)
            assertNull(QuranOfflineSyncManager.getOfflineSurahFile(context, 18, "husary"))

            // Case B: HTTP 200 HTML error page disguised as OK
            val htmlError = "<html><body><h1>503 Service Unavailable</h1></body></html>".repeat(400).toByteArray()
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                makeResponse(chain.request(), 200, "text/html", htmlError)
            }
            val resHtml = QuranOfflineSyncManager.downloadSurahSuspend(context, 18, "husary")
            assertTrue("HTML response must fail validation", resHtml.isFailure)

            // Case C: Corrupt non-MP3 bytes with audio/mpeg Content-Type
            val corruptBytes = ByteArray(150_000) { 0x41 } // ASCII 'AAAA...'
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                makeResponse(chain.request(), 200, "audio/mpeg", corruptBytes)
            }
            val resCorrupt = QuranOfflineSyncManager.downloadSurahSuspend(context, 18, "husary")
            assertTrue("Corrupt non-MP3 bytes must fail MP3 header check", resCorrupt.isFailure)

            val dir = QuranOfflineSyncManager.getOfflineStorageDir(context)
            assertTrue("No .tmp or corrupt files should remain on disk", dir.listFiles()?.isEmpty() != false)
        }
    }

    @Test
    fun restartPersistencePreservesDownloadedSurahsAcrossFreshIndexScan() {
        runBlocking {
            val surah114Bytes = buildValidMp3Bytes(30_000)
            val surah2Bytes = buildValidMp3Bytes(780_000)
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                val url = chain.request().url.toString()
                val payload = if (url.contains("002") || url.endsWith("/2.mp3")) surah2Bytes else surah114Bytes
                makeResponse(chain.request(), 200, "audio/mpeg", payload)
            }

            assertTrue(QuranOfflineSyncManager.downloadSurahSuspend(context, 2, "abdulbasit").isSuccess)
            assertTrue(QuranOfflineSyncManager.downloadSurahSuspend(context, 114, "ghamdi").isSuccess)

            // Simulate full app process restart: clear in-memory state and re-scan internal storage
            QuranOfflineSyncManager.resetStateForTests()
            QuranOfflineSyncManager.refreshOfflineIndex(context)

            val records = QuranOfflineSyncManager.downloadedSurahRecords.value
            assertEquals(2, records.size)
            assertEquals(780_000L + 30_000L, QuranOfflineSyncManager.totalOfflineAudioBytes.value)
            assertEquals(SurahDownloadStatus.COMPLETED, QuranOfflineSyncManager.getSurahOfflineStatus(context, 2, "abdulbasit").status)
            assertEquals(SurahDownloadStatus.COMPLETED, QuranOfflineSyncManager.getSurahOfflineStatus(context, 114, "ghamdi").status)
        }
    }

    @Test
    fun offlineModePlaysLocalWithoutNetworkAndShowsExplicitErrorWhenNotDownloaded() {
        runBlocking {
            // Prepare downloaded Surah 108 for Husary (bundled mishary does NOT satisfy husary)
            val surah108Bytes = buildValidMp3Bytes(24_000)
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                makeResponse(chain.request(), 200, "audio/mpeg", surah108Bytes)
            }
            val downloadedFile = QuranOfflineSyncManager.downloadSurahSuspend(context, 108, "husary").getOrNull()!!
            QuranOfflineSyncManager.refreshOfflineIndex(context)

            ShadowMediaPlayer.addMediaInfo(
                DataSource.toDataSource(downloadedFile.absolutePath),
                ShadowMediaPlayer.MediaInfo(45_000, 10)
            )
            context.assets.openFd("audio/surah_1_mishary.mp3").use { afd ->
                ShadowMediaPlayer.addMediaInfo(
                    DataSource.toDataSource(afd.fileDescriptor, afd.startOffset, afd.length),
                    ShadowMediaPlayer.MediaInfo(60_000, 10)
                )
            }

            // Enable Airplane Mode (Offline)
            QuranAudioSourceCatalog.isOnlineOverrideForTest = false
            QuranAudioManager.networkRequestCountForTest.set(0L)

            // 1. Downloaded Surah plays locally with ZERO network calls
            QuranAudioManager.playSurah(context, 108, "Al-Kawthar", "husary")
            assertEquals(AudioPlaybackSource.OFFLINE_FILE, QuranAudioManager.lastPlaybackSource.value)
            assertEquals(0L, QuranAudioManager.networkRequestCountForTest.get())
            assertNull(QuranAudioManager.audioErrorMessage.value)

            // 2. Bundled Surah 1 for Mishary resolves directly from bundled assets with zero network calls
            val bundledLen = context.assets.openFd("audio/surah_1_mishary.mp3").use { it.length }
            assertTrue("Bundled Surah 1 MP3 must be genuine full audio", bundledLen > 500_000L)
            QuranAudioManager.playSurah(context, 1, "Al-Fatihah", "mishary")
            assertEquals(AudioPlaybackSource.BUNDLED_ASSET, QuranAudioManager.lastPlaybackSource.value)
            assertEquals(0L, QuranAudioManager.networkRequestCountForTest.get())
            val bundledErr = QuranAudioManager.audioErrorMessage.value
            assertTrue(
                "Bundled asset should never produce a network/offline error: $bundledErr",
                bundledErr == null || bundledErr.contains("Don't know what to do with dataSource")
            )

            // 3. Un-downloaded Surah 36 while offline -> stops buffering immediately, shows exact error, ZERO network calls, ZERO TTS
            QuranAudioManager.playSurah(context, 36, "Ya-Sin", "ghamdi")
            assertFalse("Buffering must never stay stuck spinning offline", QuranAudioManager.isBuffering.value)
            assertFalse(QuranAudioManager.isAudioPlaying.value)
            assertEquals(0L, QuranAudioManager.networkRequestCountForTest.get())
            assertEquals(
                "This Surah has not been downloaded for offline playback.",
                QuranAudioManager.audioErrorMessage.value
            )
        }
    }

    @Test
    fun autoSyncDoesNotReport100PercentOnFailuresAndSupportsRetry() {
        runBlocking {
            val attempts = AtomicInteger(0)
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                attempts.incrementAndGet()
                // Force all non-bundled auto-sync targets to fail HTTP 500
                makeResponse(chain.request(), 500, "text/plain", "Server Error".toByteArray())
            }

            QuranOfflineSyncManager.startAutoSync(context)
            var waited = 0
            while (QuranOfflineSyncManager.isSyncing.value && waited < 5000) {
                delay(50)
                waited += 50
            }

            assertFalse("isAutoSyncComplete must remain false when items fail", QuranOfflineSyncManager.isAutoSyncComplete.value)
            assertEquals(SyncStatus.SOME_FAILED, QuranOfflineSyncManager.syncSummary.value.status)
            assertTrue(QuranOfflineSyncManager.syncSummary.value.failedCount > 0)
            assertTrue(QuranOfflineSyncManager.syncProgress.floatValue < 1.0f)

            // Offline auto-sync check
            QuranAudioSourceCatalog.isOnlineOverrideForTest = false
            QuranOfflineSyncManager.startAutoSync(context)
            assertEquals(SyncStatus.NO_NETWORK, QuranOfflineSyncManager.syncSummary.value.status)
            assertFalse(QuranOfflineSyncManager.isAutoSyncComplete.value)
        }
    }

    @Test
    fun deleteSurahAudioRemovesOfflineAvailabilityAndRecalculatesStorage() {
        runBlocking {
            val bytes = buildValidMp3Bytes(30_000)
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                makeResponse(chain.request(), 200, "audio/mpeg", bytes)
            }
            assertTrue(QuranOfflineSyncManager.downloadSurahSuspend(context, 108, "abdulbasit").isSuccess)
            QuranOfflineSyncManager.refreshOfflineIndex(context)
            assertEquals(30_000L, QuranOfflineSyncManager.totalOfflineAudioBytes.value)

            val deleted = QuranOfflineSyncManager.deleteSurahAudio(context, 108, "abdulbasit")
            assertTrue(deleted)
            assertEquals(0L, QuranOfflineSyncManager.totalOfflineAudioBytes.value)
            assertTrue(QuranOfflineSyncManager.downloadedSurahRecords.value.isEmpty())
            assertEquals(SurahDownloadStatus.NOT_DOWNLOADED, QuranOfflineSyncManager.getSurahOfflineStatus(context, 108, "abdulbasit").status)

            // Confirm offline playback now refuses and shows unavailable message
            QuranAudioSourceCatalog.isOnlineOverrideForTest = false
            QuranAudioManager.playSurah(context, 108, "Al-Kawthar", "abdulbasit")
            assertEquals("This Surah has not been downloaded for offline playback.", QuranAudioManager.audioErrorMessage.value)
        }
    }

    @Test
    fun bulkEstimateMeasuresSizesSkipsAlreadyDownloadedAndSupportsBulkDownload() {
        runBlocking {
            // Test fast deterministic 114-Surah measurement & subset download
            QuranOfflineSyncManager.minSurahBytesOverrideForTest = { 8_000L }
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                val payload = buildValidMp3Bytes(12_000)
                makeResponse(
                    chain.request(),
                    200,
                    "audio/mpeg",
                    payload,
                    extraHeaders = mapOf("Accept-Ranges" to "bytes")
                )
            }

            // Pre-download Surah 1 and Surah 2 for husary so bulk skips them cleanly
            assertTrue(QuranOfflineSyncManager.downloadSurahSuspend(context, 1, "husary").isSuccess)
            assertTrue(QuranOfflineSyncManager.downloadSurahSuspend(context, 2, "husary").isSuccess)

            val estimate = QuranOfflineSyncManager.estimateReciterFullQuranSize(context, "husary")
            assertEquals("husary", estimate.reciterId)
            assertEquals(2, estimate.alreadyOfflineCount)
            assertEquals(112, estimate.remainingToDownloadCount)
            assertEquals(112, estimate.verifiedByServerCount)
            assertEquals(0, estimate.estimatedByHeuristicCount)
            assertEquals(112L * 12_000L, estimate.estimatedRemainingBytes)
            assertTrue(estimate.hasEnoughStorage)

            // Run bulk download on Surahs 1..5 to verify skip of 1..2 and download of 3..5
            QuranOfflineSyncManager.startFullQuranDownload(context, "husary", listOf(1, 2, 3, 4, 5))
            var waited = 0
            while (QuranOfflineSyncManager.bulkDownloadState.value.phase == BulkPhase.DOWNLOADING && waited < 10_000) {
                delay(50)
                waited += 50
            }
            val state = QuranOfflineSyncManager.bulkDownloadState.value
            assertEquals(BulkPhase.COMPLETED, state.phase)
            assertTrue(QuranOfflineSyncManager.isSurahAvailableOffline(context, 3, "husary"))
            assertTrue(QuranOfflineSyncManager.isSurahAvailableOffline(context, 5, "husary"))
            assertTrue(state.skippedSurahsCount >= 2)
            assertEquals(3, state.completedSurahsCount)
        }
    }

    @Test
    fun quranOfflineAudioDownloadAndDeletePersistAccuratelyOnDisk() {
        runBlocking {
            val bytes = buildValidMp3Bytes(780_000)
            QuranOfflineSyncManager.httpClientOverrideForTest = clientWithInterceptor { chain ->
                makeResponse(chain.request(), 200, "audio/mpeg", bytes)
            }
            assertTrue(QuranOfflineSyncManager.downloadSurahSuspend(context, 2, "mishary").isSuccess)
            QuranOfflineSyncManager.refreshOfflineIndex(context)
        }

        assertEquals(1, QuranOfflineSyncManager.downloadedSurahRecords.value.size)
        val record = QuranOfflineSyncManager.downloadedSurahRecords.value.first()
        assertEquals(2, record.surahNumber)
        assertEquals(780_000L, record.fileSizeBytes)
        assertTrue(QuranOfflineSyncManager.isSurahAvailableOffline(context, 2, "mishary"))

        // Deleting removes the actual MP3 file on disk and clears the index
        assertTrue(QuranOfflineSyncManager.deleteSurahAudio(context, 2, "mishary"))
        assertTrue(QuranOfflineSyncManager.downloadedSurahRecords.value.isEmpty())
        assertEquals(0L, QuranOfflineSyncManager.totalOfflineAudioBytes.value)
    }

    @Test
    fun quranReaderScreenPreservesTafseerAndShowsHonestOfflineAudioErrors() {
        QuranAudioSourceCatalog.isOnlineOverrideForTest = false

        composeRule.setContent {
            NooreHidayatTheme {
                QuranReaderScreen(
                    surahNumber = 2,
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    onNavigateToSurah = {}
                )
            }
        }
        composeRule.waitForIdle()

        // Quran reader content preserved
        composeRule.onNodeWithTag("quran_reader_ayah_list").assertIsDisplayed()
        composeRule.onNodeWithTag("play_pause_audio_btn").assertIsDisplayed()
        composeRule.onNodeWithTag("download_surah_audio_btn").assertIsDisplayed()

        // Tap Play on un-downloaded Surah 2 offline -> shows explicit error banner cleanly
        composeRule.onNodeWithTag("play_pause_audio_btn").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("quran_audio_error_banner").assertIsDisplayed()
        composeRule.onNodeWithText("This Surah has not been downloaded for offline playback.").assertIsDisplayed()
    }
}
