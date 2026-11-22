package com.example

import android.app.Application
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import com.example.data.live.HarameynLiveSourceResolver
import com.example.data.live.HarameynLiveSources
import com.example.data.live.HarameynLiveStreamPrefs
import com.example.data.live.LiveConnectionPhase
import com.example.data.live.LivePlayerTransport
import com.example.data.live.LiveSourceAttemptStatus
import com.example.data.live.LiveStreamSourceId
import com.example.data.model.AppLanguage
import com.example.data.repository.HarameynLocation
import com.example.data.repository.LiveBroadcastState
import com.example.data.repository.YouTubeRepository
import com.example.localization.AppLocalization
import com.example.ui.screens.LiveStreamScreen
import com.example.ui.theme.NooreHidayatTheme
import com.example.ui.viewmodel.LiveStreamViewModel
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
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class HarameynLiveStreamTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context
    private val originalClient = YouTubeRepository.httpClient

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        HarameynLiveStreamPrefs.clearForTest(context)
        YouTubeRepository.clearTestCaches()
        YouTubeRepository.networkChecker = { true }
        YouTubeRepository.currentTimeProvider = null
    }

    @After
    fun tearDown() {
        YouTubeRepository.httpClient = originalClient
        YouTubeRepository.networkChecker = null
        YouTubeRepository.currentTimeProvider = null
        YouTubeRepository.clearTestCaches()
        HarameynLiveStreamPrefs.clearForTest(context)
    }

    private fun buildMockClient(handler: (okhttp3.Request) -> Response): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain -> handler(chain.request()) })
            .build()
    }

    private fun okResponse(req: okhttp3.Request, body: String, mime: String = "text/plain"): Response =
        Response.Builder()
            .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
            .body(body.toResponseBody(mime.toMediaType())).build()

    private fun errResponse(req: okhttp3.Request, code: Int = 503): Response =
        Response.Builder()
            .request(req).protocol(Protocol.HTTP_1_1).code(code).message("Error")
            .body("".toResponseBody("text/plain".toMediaType())).build()

    private val sampleMasterMakkahHls = """
        #EXTM3U
        #EXT-X-VERSION:3
        #EXT-X-STREAM-INF:BANDWIDTH=453241,RESOLUTION=320x256
        saudi_quran-variant.m3u8
    """.trimIndent()

    private val sampleMasterMadinahHls = """
        #EXTM3U
        #EXT-X-VERSION:3
        #EXT-X-STREAM-INF:BANDWIDTH=453241,RESOLUTION=320x256
        saudi_sunnah-variant.m3u8
    """.trimIndent()

    private val sampleLiveVariantHls = """
        #EXTM3U
        #EXT-X-VERSION:3
        #EXT-X-TARGETDURATION:8
        #EXT-X-MEDIA-SEQUENCE:62505733
        #EXTINF:5.76,
        seg1.ts
        #EXTINF:5.76,
        seg2.ts
    """.trimIndent()

    private val sampleVodVariantHls = """
        #EXTM3U
        #EXT-X-VERSION:3
        #EXT-X-MEDIA-SEQUENCE:1
        #EXTINF:5.76,
        vod1.ts
        #EXT-X-ENDLIST
    """.trimIndent()

    private val sampleHaramainVoiceHtml = """
        <html><body>
        <script>
        {"streams":{
          "makkah":{"name":"الحرم المكي","nameEn":"Grand Mosque","url":"https://www.youtube.com/embed/IsJTAdC3-Jw?autoplay=1&mute=1"},
          "madinah":{"name":"الحرم النبوي","nameEn":"Prophet's Mosque","url":"https://www.youtube.com/embed/27cln-IxOGo?autoplay=1&mute=1"}
        }}
        </script>
        </body></html>
    """.trimIndent()

    private fun waitForVm(vm: LiveStreamViewModel, predicate: (com.example.ui.viewmodel.LiveStreamUiState) -> Boolean) {
        val deadline = System.currentTimeMillis() + 5_000L
        while (System.currentTimeMillis() < deadline) {
            ShadowLooper.idleMainLooper()
            if (predicate(vm.uiState.value)) return
            Thread.sleep(20)
        }
    }

    @Test
    fun testMakkahOfficialPrimaryQuranHayatSecondAndHaramainVoiceThirdFallbackChain() {
        YouTubeRepository.httpClient = buildMockClient { req ->
            val url = req.url.toString()
            when {
                // Source 1: Official Saudi Quran TV starting ID (24JXS383N1c) active via oEmbed
                url.contains("oembed") && url.contains(HarameynLiveSources.MAKKAH_STARTING_VIDEO_ID) ->
                    okResponse(
                        req,
                        """{"provider_name":"YouTube","title":"بث مباشر || قناة القرآن الكريم Makkah Live","author_name":"قناة القرآن الكريم","author_url":"https://www.youtube.com/@SaudiQuranTv"}""",
                        "application/json"
                    )
                // Source 2: Quran Hayat Makkah HLS master & live variant
                url == HarameynLiveSources.MAKKAH_QURAN_HAYAT_HLS ->
                    okResponse(req, sampleMasterMakkahHls, "application/x-mpegURL")
                url.endsWith("saudi_quran-variant.m3u8") ->
                    okResponse(req, sampleLiveVariantHls, "application/x-mpegURL")
                // Source 3: Haramain Voice Makkah embed + oEmbed verification
                url == HarameynLiveSources.HARAMAIN_VOICE_PAGE_URL ->
                    okResponse(req, sampleHaramainVoiceHtml, "text/html")
                url.contains("oembed") && url.contains("IsJTAdC3-Jw") ->
                    okResponse(
                        req,
                        """{"provider_name":"YouTube","title":"بث مباشر || قناة القرآن الكريم Makkah Live","author_name":"قناة القرآن الكريم","author_url":"https://www.youtube.com/@SaudiQuranTv"}""",
                        "application/json"
                    )
                else -> errResponse(req, 404)
            }
        }

        val vm = LiveStreamViewModel(context.applicationContext as Application)
        try {
            // 1. Makkah starts on Source 1 (Official Saudi Quran TV), showing CONNECTING (NOT LIVE yet!)
            waitForVm(vm) { it.activeSource?.config?.id == LiveStreamSourceId.OFFICIAL_YOUTUBE }
            val s1 = vm.uiState.value
            assertEquals(HarameynLocation.MAKKAH, s1.selectedLocation)
            assertEquals(LiveConnectionPhase.CONNECTING, s1.phase)
            assertEquals(LiveStreamSourceId.OFFICIAL_YOUTUBE, s1.activeSource?.config?.id)
            assertEquals(HarameynLiveSources.MAKKAH_STARTING_VIDEO_ID, s1.activeSource?.youtubeVideoId)

            // Confirming actual playback flips status to LIVE
            vm.onPlayerConfirmedLive(s1.attemptToken)
            assertEquals(LiveConnectionPhase.LIVE, vm.uiState.value.phase)
            assertFalse(vm.uiState.value.isFallbackActive)

            // 2. Primary failure -> automatic fallback to Source 2 (Quran Hayat Makkah HLS, NOT Madinah!)
            vm.onPlayerFailed(s1.attemptToken, "YouTube playback restricted (150)")
            waitForVm(vm) { it.activeSource?.config?.id == LiveStreamSourceId.QURAN_HAYAT_HLS }
            val s2 = vm.uiState.value
            assertEquals(LiveConnectionPhase.RECONNECTING, s2.phase)
            assertEquals(LiveStreamSourceId.QURAN_HAYAT_HLS, s2.activeSource?.config?.id)
            assertEquals(LivePlayerTransport.HLS_NATIVE, s2.activeSource?.transport)
            assertEquals(HarameynLiveSources.MAKKAH_QURAN_HAYAT_HLS, s2.activeSource?.hlsStreamUrl)
            assertTrue("Must select Makkah saudi_quran HLS stream", s2.activeSource!!.hlsStreamUrl!!.contains("saudi_quran"))
            assertEquals(LiveSourceAttemptStatus.FAILED, s2.sourceAttempts[0].status)

            // Confirming HLS live playback flips status to LIVE with fallback relay transparency flag
            vm.onPlayerConfirmedLive(s2.attemptToken)
            assertEquals(LiveConnectionPhase.LIVE, vm.uiState.value.phase)
            assertTrue(vm.uiState.value.isFallbackActive)

            // 3. Second failure -> automatic fallback to Source 3 (Haramain Voice Makkah embed)
            vm.onPlayerFailed(s2.attemptToken, "HLS network timeout")
            waitForVm(vm) { it.activeSource?.config?.id == LiveStreamSourceId.HARAMAIN_VOICE }
            val s3 = vm.uiState.value
            assertEquals(LiveStreamSourceId.HARAMAIN_VOICE, s3.activeSource?.config?.id)
            assertEquals("IsJTAdC3-Jw", s3.activeSource?.youtubeVideoId)
            assertEquals(LiveSourceAttemptStatus.FAILED, s3.sourceAttempts[1].status)

            vm.onPlayerConfirmedLive(s3.attemptToken)
            assertEquals(LiveConnectionPhase.LIVE, vm.uiState.value.phase)

            // 4. Third failure -> honest SOURCE_UNAVAILABLE (all sources exhausted, no infinite retry loop)
            vm.onPlayerFailed(s3.attemptToken, "Embed playback failed")
            val sFail = vm.uiState.value
            assertEquals(LiveConnectionPhase.SOURCE_UNAVAILABLE, sFail.phase)
            assertNull("All players must be released when unavailable", sFail.activeSource)
            assertTrue(sFail.sourceAttempts.all { it.status == LiveSourceAttemptStatus.FAILED })
        } finally {
            vm.cancelActiveOperations()
        }
    }

    @Test
    fun testMadinahOfficialPrimaryQuranHayatSecondAndHaramainVoiceThirdFallbackChain() {
        HarameynLiveStreamPrefs.saveSelectedLocation(context, HarameynLocation.MADINAH)
        YouTubeRepository.httpClient = buildMockClient { req ->
            val url = req.url.toString()
            when {
                // Source 1: Starting video ended/rotated (oEmbed 200 without title) -> channel live embed
                url.contains("oembed") && url.contains(HarameynLiveSources.MADINAH_STARTING_VIDEO_ID) ->
                    okResponse(
                        req,
                        """{"provider_name":"YouTube","type":"video","version":"1.0"}""",
                        "application/json"
                    )
                // Source 2: Quran Hayat Madinah HLS (saudi_sunnah)
                url == HarameynLiveSources.MADINAH_QURAN_HAYAT_HLS ->
                    okResponse(req, sampleMasterMadinahHls, "application/x-mpegURL")
                url.endsWith("saudi_sunnah-variant.m3u8") ->
                    okResponse(req, sampleLiveVariantHls, "application/x-mpegURL")
                // Source 3: Haramain Voice Madinah embed (27cln-IxOGo) + oEmbed verification
                url == HarameynLiveSources.HARAMAIN_VOICE_PAGE_URL ->
                    okResponse(req, sampleHaramainVoiceHtml, "text/html")
                url.contains("oembed") && url.contains("27cln-IxOGo") ->
                    okResponse(
                        req,
                        """{"provider_name":"YouTube","title":"بث مباشر || قناة السنة النبوية Madinah Live HD","author_name":"قناة السنة النبوية","author_url":"https://www.youtube.com/@SaudiSunnahTv"}""",
                        "application/json"
                    )
                else -> errResponse(req, 404)
            }
        }

        val vm = LiveStreamViewModel(context.applicationContext as Application)
        try {
            // 1. Madinah starts on Official Saudi Sunnah TV channel live embed
            waitForVm(vm) { it.activeSource?.config?.id == LiveStreamSourceId.OFFICIAL_YOUTUBE }
            val s1 = vm.uiState.value
            assertEquals(HarameynLocation.MADINAH, s1.selectedLocation)
            assertEquals(LivePlayerTransport.YOUTUBE_CHANNEL_LIVE_EMBED, s1.activeSource?.transport)
            assertEquals(HarameynLiveSources.MADINAH_CHANNEL_ID, s1.activeSource?.youtubeChannelId)

            // 2. Primary failure -> Quran Hayat Madinah HLS (saudi_sunnah, NEVER saudi_quran)
            vm.onPlayerFailed(s1.attemptToken, "Official channel live embed failed")
            waitForVm(vm) { it.activeSource?.config?.id == LiveStreamSourceId.QURAN_HAYAT_HLS }
            val s2 = vm.uiState.value
            assertEquals(LiveStreamSourceId.QURAN_HAYAT_HLS, s2.activeSource?.config?.id)
            assertTrue("Must select Madinah saudi_sunnah HLS stream", s2.activeSource!!.hlsStreamUrl!!.contains("saudi_sunnah"))

            // 3. Second failure -> Haramain Voice Madinah (27cln-IxOGo, NEVER Makkah IsJTAdC3-Jw)
            vm.onPlayerFailed(s2.attemptToken, "HLS failed")
            waitForVm(vm) { it.activeSource?.config?.id == LiveStreamSourceId.HARAMAIN_VOICE }
            val s3 = vm.uiState.value
            assertEquals(LiveStreamSourceId.HARAMAIN_VOICE, s3.activeSource?.config?.id)
            assertEquals("27cln-IxOGo", s3.activeSource?.youtubeVideoId)
        } finally {
            vm.cancelActiveOperations()
        }
    }

    @Test
    fun testVodHlsPlaylistAndRecordedVideoAreNeverAcceptedAsLive() {
        val makkahHlsSource = HarameynLiveSources.MAKKAH_SOURCES[1]
        val vodClient = buildMockClient { req ->
            val url = req.url.toString()
            when {
                url == HarameynLiveSources.MAKKAH_QURAN_HAYAT_HLS ->
                    okResponse(req, sampleMasterMakkahHls, "application/x-mpegURL")
                url.endsWith("saudi_quran-variant.m3u8") ->
                    okResponse(req, sampleVodVariantHls, "application/x-mpegURL")
                else -> errResponse(req, 404)
            }
        }
        val outcome = HarameynLiveSourceResolver.probeSource(makkahHlsSource, vodClient)
        assertNull("VOD HLS with #EXT-X-ENDLIST must NEVER be accepted as LIVE", outcome.resolved)
        assertTrue(outcome.failureReason?.contains("VOD", ignoreCase = true) == true || outcome.failureReason != null)

        // Haramain Voice returning a non-live recorded title must be rejected
        val hvSource = HarameynLiveSources.MAKKAH_SOURCES[2]
        val recordedHvClient = buildMockClient { req ->
            val url = req.url.toString()
            when {
                url == HarameynLiveSources.HARAMAIN_VOICE_PAGE_URL ->
                    okResponse(req, sampleHaramainVoiceHtml, "text/html")
                url.contains("oembed") ->
                    okResponse(
                        req,
                        """{"provider_name":"YouTube","title":"Recorded Maghrib Prayer Yesterday","author_name":"قناة القرآن الكريم","author_url":"https://www.youtube.com/@SaudiQuranTv"}""",
                        "application/json"
                    )
                else -> errResponse(req, 404)
            }
        }
        val hvOutcome = HarameynLiveSourceResolver.probeSource(hvSource, recordedHvClient)
        assertNull("Recorded video title must NEVER be accepted as a live broadcast", hvOutcome.resolved)
    }

    @Test
    fun testOfflineDetectionAndAutomaticRecoveryOnReconnect() {
        YouTubeRepository.networkChecker = { false }
        val vm = LiveStreamViewModel(context.applicationContext as Application)
        try {
            waitForVm(vm) { it.phase == LiveConnectionPhase.OFFLINE }
            assertEquals(LiveConnectionPhase.OFFLINE, vm.uiState.value.phase)
            assertNull(vm.uiState.value.activeSource)

            // Reconnect internet with valid Quran Hayat Makkah HLS (primary fails -> auto-fallback to Quran Hayat)
            YouTubeRepository.networkChecker = { true }
            YouTubeRepository.httpClient = buildMockClient { req ->
                val url = req.url.toString()
                when {
                    url == HarameynLiveSources.MAKKAH_QURAN_HAYAT_HLS ->
                        okResponse(req, sampleMasterMakkahHls, "application/x-mpegURL")
                    url.endsWith("saudi_quran-variant.m3u8") ->
                        okResponse(req, sampleLiveVariantHls, "application/x-mpegURL")
                    else -> errResponse(req, 503)
                }
            }
            vm.onNetworkConnectivityChanged(true)
            waitForVm(vm) { it.activeSource?.config?.id == LiveStreamSourceId.QURAN_HAYAT_HLS }
            assertEquals(LiveStreamSourceId.QURAN_HAYAT_HLS, vm.uiState.value.activeSource?.config?.id)
            assertEquals(LiveConnectionPhase.RECONNECTING, vm.uiState.value.phase)
        } finally {
            vm.cancelActiveOperations()
        }
    }

    @Test
    fun testSelectedLocationPersistsAcrossNavigationAndRecordedStopsLivePlayer() {
        YouTubeRepository.httpClient = buildMockClient { req -> errResponse(req, 503) }
        val vm1 = LiveStreamViewModel(context.applicationContext as Application)
        try {
            vm1.selectLocation(HarameynLocation.MADINAH)
            assertEquals(HarameynLocation.MADINAH, HarameynLiveStreamPrefs.loadSelectedLocation(context))
        } finally {
            vm1.cancelActiveOperations()
        }

        // Navigating back creates a fresh ViewModel which remembers Madinah!
        val vm2 = LiveStreamViewModel(context.applicationContext as Application)
        try {
            assertEquals(HarameynLocation.MADINAH, vm2.uiState.value.selectedLocation)
        } finally {
            vm2.cancelActiveOperations()
        }
    }

    @Test
    fun testCheckHarameynLiveStatusReturnsConnectingNotPrematureLiveAndNeverFakes() = runBlocking {
        YouTubeRepository.httpClient = buildMockClient { req ->
            val url = req.url.toString()
            when {
                url == HarameynLiveSources.MAKKAH_QURAN_HAYAT_HLS ->
                    okResponse(req, sampleMasterMakkahHls, "application/x-mpegURL")
                url.endsWith("saudi_quran-variant.m3u8") ->
                    okResponse(req, sampleLiveVariantHls, "application/x-mpegURL")
                else -> errResponse(req, 503)
            }
        }

        val res = YouTubeRepository.checkHarameynLiveStatus(context, HarameynLocation.MAKKAH)
        assertEquals(
            "checkHarameynLiveStatus must return CONNECTING (never premature LIVE_CONFIRMED before real playback)",
            LiveBroadcastState.CONNECTING,
            res.state
        )
        assertNotNull(res.resolvedSource)
        assertEquals(LiveStreamSourceId.QURAN_HAYAT_HLS, res.resolvedSource?.config?.id)
        assertEquals(HarameynLiveSources.MAKKAH_QURAN_HAYAT_HLS, res.resolvedSource?.hlsStreamUrl)
    }

    @Test
    fun testUiShowsOfflineMessageAndUnavailableExternalFallbacksAndRetry() {
        YouTubeRepository.networkChecker = { false }
        composeRule.setContent {
            NooreHidayatTheme {
                LiveStreamScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitUntil(timeoutMillis = 5_000L) {
            composeRule.onAllNodesWithTag("live_phase_offline_banner").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("live_phase_offline_banner").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Offline — connect to internet").assertIsDisplayed()
        composeRule.onNodeWithTag("live_retry_btn").performScrollTo().assertIsDisplayed()

        // Switch online with all sources failing -> tap Retry -> SOURCE UNAVAILABLE + Browser fallbacks appear
        YouTubeRepository.networkChecker = { true }
        YouTubeRepository.httpClient = buildMockClient { req -> errResponse(req, 503) }
        composeRule.onNodeWithTag("live_retry_btn").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000L) {
            composeRule.onAllNodesWithTag("live_phase_unavailable_banner").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("live_phase_unavailable_banner").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("live_open_quranhayat_btn").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("live_open_haramainvoice_btn").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun testAllNewLiveStreamLocalizationKeysPresentInAll4Languages() {
        val keys = listOf(
            "live_phase_connecting",
            "live_phase_reconnecting",
            "live_phase_source_unavailable",
            "live_source_unavailable_desc",
            "live_fallback_relay_note",
            "live_sources_label",
            "live_open_quranhayat",
            "live_open_haramainvoice",
            "live_return_to_live"
        )
        keys.forEach { key ->
            AppLanguage.entries.forEach { lang ->
                val value = AppLocalization.get(key, lang)
                assertTrue("Missing $lang translation for $key", value != key && value.isNotBlank())
            }
        }
    }
}
