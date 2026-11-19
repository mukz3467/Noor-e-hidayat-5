package com.example

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.repository.FeedLoadStatus
import com.example.data.repository.HarameynLocation
import com.example.data.repository.LectureCategory
import com.example.data.repository.LiveBroadcastState
import com.example.data.repository.PodcastLanguageSection
import com.example.data.repository.PodcastTopicFilter
import com.example.data.repository.YouTubeRepository
import com.example.localization.AppLocalization
import com.example.ui.components.FullscreenVideoController
import com.example.ui.components.YouTubeIFramePlayerConfig
import com.example.ui.screens.LecturesScreen
import com.example.ui.screens.LiveStreamScreen
import com.example.ui.theme.NooreHidayatTheme
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.net.SocketTimeoutException
import java.util.concurrent.CopyOnWriteArrayList

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class YouTubeLecturesAndPlayerTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context
    private val originalClient = YouTubeRepository.httpClient

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences("noor_youtube_feeds_v2", Context.MODE_PRIVATE).edit().clear().commit()
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
    }

    private fun buildMockClient(handler: (okhttp3.Request) -> Response): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain -> handler(chain.request()) })
            .build()
    }

    private fun sampleAtomFeed(channelId: String, authorName: String, entries: List<Triple<String, String, String>>): String {
        val entriesXml = entries.joinToString("\n") { (vid, title, published) ->
            "<entry>\n" +
                "  <id>yt:video:$vid</id>\n" +
                "  <yt:videoId>$vid</yt:videoId>\n" +
                "  <yt:channelId>$channelId</yt:channelId>\n" +
                "  <title>$title</title>\n" +
                "  <link rel=\"alternate\" href=\"https://www.youtube.com/watch?v=$vid\"/>\n" +
                "  <author><name>$authorName</name><uri>https://www.youtube.com/channel/$channelId</uri></author>\n" +
                "  <published>$published</published>\n" +
                "  <media:group><media:thumbnail url=\"https://i.ytimg.com/vi/$vid/hqdefault.jpg\" width=\"480\" height=\"360\"/></media:group>\n" +
                "</entry>"
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<feed xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\" xmlns:media=\"http://search.yahoo.com/mrss/\" xmlns=\"http://www.w3.org/2005/Atom\">\n" +
            "  <title>$authorName</title>\n" +
            entriesXml + "\n" +
            "</feed>"
    }

    @Test
    fun testProhibitedScrapingFakeLiveAndHardcodedIdsRemovedFromSources() {
        val projectRoot = File(System.getProperty("user.dir") ?: ".")
        val srcDir = if (File(projectRoot, "app/src/main/java").exists()) {
            File(projectRoot, "app/src/main/java")
        } else {
            File(projectRoot, "src/main/java")
        }
        assertTrue("Source directory must exist", srcDir.exists())

        val sourceFiles = srcDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList()
        val combinedText = sourceFiles.joinToString("\n") { it.readText() }

        val forbiddenPatterns = listOf(
            "ytInitialData",
            "youtube.com/results",
            "CANONICAL_WATCH_REGEX",
            "LIVE_META_TRUE_REGEX",
            "PROXY_SESSION_HEADERS",
            "NON_AUTH_SESSION_COOKIES",
            "YouTubeSignInDialog",
            "YouTubeAuthManager",
            "accounts.google.com/ServiceLogin",
            "LOGIN_INFO",
            "SAPISID",
            "CURATED_SCHOLAR_VIDEOS",
            "24/7 LIVE",
            "eC4LfEVxvKg",
            "Rs7St51oDDc"
        )
        forbiddenPatterns.forEach { forbidden ->
            assertFalse("Forbidden pattern '$forbidden' must not remain in app source", combinedText.contains(forbidden))
        }
    }

    @Test
    fun testVerifiedChannelsAndTopicMappingsUseValidUcIdsAndOfficialFeedUrls() {
        assertTrue(YouTubeRepository.VERIFIED_CHANNELS.size >= 10)
        YouTubeRepository.VERIFIED_CHANNELS.forEach { ch ->
            assertTrue("Channel ${ch.key} ID must match UC[22]", YouTubeRepository.isValidChannelId(ch.channelId))
            assertTrue("Handle must start with @", ch.handle.startsWith("@"))
            assertEquals("https://www.youtube.com/feeds/videos.xml?channel_id=${ch.channelId}", ch.feedUrl)
        }
        LectureCategory.entries.forEach { cat ->
            val mapped = YouTubeRepository.channelsForTopic(cat)
            assertTrue("Topic ${cat.id} must resolve to mapped subset of channels", !mapped.isNullOrEmpty())
            assertTrue("Topic ${cat.id} must not blindly map all channels", mapped!!.size < YouTubeRepository.VERIFIED_CHANNELS.size)
        }
        assertEquals("UCos52azQNBgW63_9uDJoPDA", YouTubeRepository.HARAMEYN_CHANNELS[HarameynLocation.MAKKAH]?.channelId)
        assertEquals("UCROKYPep-UuODNwyipe6JMw", YouTubeRepository.HARAMEYN_CHANNELS[HarameynLocation.MADINAH]?.channelId)
    }

    @Test
    fun testAtomFeedParserExtractsMetadataDeduplicatesAndHandlesMalformedOrEmptyXml() {
        val bayyinah = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "bayyinah" }
        val xml = sampleAtomFeed(
            channelId = bayyinah.channelId,
            authorName = "Nouman Ali Khan - Official - Bayyinah",
            entries = listOf(
                Triple("dQw4w9WgXcQ", "Surah Al-Kahf Tafseer &amp; Reflections", "2026-04-10T14:00:00+00:00"),
                Triple("bad_id", "Should be ignored because not 11 chars", "2026-04-09T10:00:00+00:00"),
                Triple("abcDEF12345", "Understanding Salah and Humility", "2026-04-11T09:15:00+00:00"),
                Triple("abcDEF12345", "Duplicate Entry Must Be Removed", "2026-04-11T09:15:00+00:00")
            )
        )
        val parsed = YouTubeRepository.parseAtomFeedXml(xml, bayyinah)

        assertEquals("Malformed & duplicate video IDs must be excluded", 2, parsed.size)
        assertEquals("abcDEF12345", parsed[0].id)
        assertEquals("2026-04-11", parsed[0].publishedDisplay)
        assertEquals("https://i.ytimg.com/vi/abcDEF12345/hqdefault.jpg", parsed[0].thumbnail)
        assertTrue(parsed[0].categories.contains(LectureCategory.SALAH_WORSHIP))

        assertEquals("dQw4w9WgXcQ", parsed[1].id)
        assertTrue(parsed[1].categories.contains(LectureCategory.QURAN_TAFSEER))

        // Empty and malformed XML must never crash
        assertTrue(YouTubeRepository.parseAtomFeedXml("", bayyinah).isEmpty())
        assertTrue(YouTubeRepository.parseAtomFeedXml("<feed><entry><title>Broken", bayyinah).isEmpty())
    }

    @Test
    fun testTopicBasedFetchingFetchesOnlyMappedChannelsAndRejectsInvalidMapping() = runBlocking {
        val requestedUrls = CopyOnWriteArrayList<String>()
        val bayyinahId = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "bayyinah" }.channelId
        val seekersId = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "seekersguidance" }.channelId

        YouTubeRepository.httpClient = buildMockClient { req ->
            val url = req.url.toString()
            requestedUrls.add(url)
            val body = when {
                url.contains(bayyinahId) -> sampleAtomFeed(
                    bayyinahId,
                    "Bayyinah",
                    listOf(
                        Triple("vid12345678", "Surah Yusuf Tafseer Part 1", "2026-04-12T10:00:00Z"),
                        Triple("vid99999999", "Train Your Mind", "2026-04-11T10:00:00Z")
                    )
                )
                url.contains(seekersId) -> sampleAtomFeed(
                    seekersId,
                    "SeekersGuidance",
                    listOf(Triple("vid22222222", "Hadith Lesson 1", "2026-04-10T10:00:00Z"))
                )
                else -> sampleAtomFeed("UCother0000000000000000", "Other", emptyList())
            }
            Response.Builder()
                .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                .body(body.toResponseBody("application/atom+xml".toMediaType())).build()
        }

        val tafseerRes = YouTubeRepository.fetchLecturesForTopic(context, LectureCategory.QURAN_TAFSEER, forceRefresh = true)
        assertEquals(FeedLoadStatus.ONLINE_FRESH, tafseerRes.status)
        // Only Bayyinah + SeekersGuidance should be requested for QURAN_TAFSEER (2 channels, NOT all 16)
        assertEquals(2, requestedUrls.size)
        assertTrue(requestedUrls.all { it.contains(bayyinahId) || it.contains(seekersId) })
        // Only the Surah Yusuf Tafseer item matches QURAN_TAFSEER (neither "Train Your Mind" nor "Hadith Lesson 1")
        assertEquals(1, tafseerRes.videos.size)
        assertEquals("vid12345678", tafseerRes.videos[0].id)

        // Invalid or missing topic mapping must NOT fall back to fetching all channels
        requestedUrls.clear()
        val invalidTopicRes = YouTubeRepository.fetchLecturesForTopic(context, topic = null, forceRefresh = true)
        assertEquals(FeedLoadStatus.INVALID_MAPPING, invalidTopicRes.status)
        assertTrue(invalidTopicRes.videos.isEmpty())
        assertTrue("No HTTP requests should fire on invalid mapping", requestedUrls.isEmpty())

        val invalidChannelRes = YouTubeRepository.fetchLecturesForTopic(
            context = context,
            topic = LectureCategory.HADITH,
            channelKey = "non_existent_channel_key",
            forceRefresh = true
        )
        assertEquals(FeedLoadStatus.INVALID_MAPPING, invalidChannelRes.status)
        assertTrue(requestedUrls.isEmpty())
    }

    @Test
    fun testCachingExpirationOfflineAndHttpTimeoutErrorHandling() = runBlocking {
        var fakeClock = 1_000_000L
        YouTubeRepository.currentTimeProvider = { fakeClock }
        var httpRequestsCount = 0

        val bayyinah = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "bayyinah" }
        val seekers = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "seekersguidance" }

        YouTubeRepository.httpClient = buildMockClient { req ->
            httpRequestsCount++
            val url = req.url.toString()
            when {
                url.contains(bayyinah.channelId) -> Response.Builder()
                    .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(
                        sampleAtomFeed(
                            bayyinah.channelId,
                            "Bayyinah",
                            listOf(Triple("vid12345678", "Surah Al-Kahf Tafseer", "2026-04-12T10:00:00Z"))
                        ).toResponseBody("application/atom+xml".toMediaType())
                    ).build()
                url.contains(seekers.channelId) -> Response.Builder()
                    .request(req).protocol(Protocol.HTTP_1_1).code(404).message("Not Found")
                    .body("".toResponseBody("text/plain".toMediaType())).build()
                else -> Response.Builder()
                    .request(req).protocol(Protocol.HTTP_1_1).code(500).message("Err")
                    .body("".toResponseBody("text/plain".toMediaType())).build()
            }
        }

        // 1. Fresh online fetch
        val onlineRes = YouTubeRepository.fetchLecturesForTopic(context, LectureCategory.QURAN_TAFSEER, forceRefresh = false)
        assertEquals(FeedLoadStatus.ONLINE_FRESH, onlineRes.status)
        assertEquals(1, onlineRes.videos.size)
        assertTrue(onlineRes.unavailableChannels.contains(seekers.handle))
        val callsAfterOnline = httpRequestsCount

        // 2. Within TTL -> Memory/Disk cache hit (no new HTTP requests for Bayyinah)
        fakeClock += 5 * 60 * 1000L
        YouTubeRepository.clearTestCaches()
        val cachedFreshRes = YouTubeRepository.fetchLecturesForTopic(context, LectureCategory.QURAN_TAFSEER, forceRefresh = false)
        assertEquals(1, cachedFreshRes.videos.size)
        assertFalse(cachedFreshRes.isCacheExpired)

        // 3. Offline -> returns OFFLINE_CACHE honestly without HTTP requests
        YouTubeRepository.clearTestCaches()
        YouTubeRepository.networkChecker = { false }
        val offlineRes = YouTubeRepository.fetchLecturesForTopic(context, LectureCategory.QURAN_TAFSEER, forceRefresh = false)
        assertEquals(FeedLoadStatus.OFFLINE_CACHE, offlineRes.status)
        assertEquals(1, offlineRes.videos.size)
        assertTrue(offlineRes.videos[0].isFromCache)

        // 4. Advance clock past 30 min TTL + simulate SocketTimeoutException online -> EXPIRED_CACHE_FALLBACK (never fresh!)
        fakeClock += YouTubeRepository.CACHE_TTL_MS + 60_000L
        YouTubeRepository.clearTestCaches()
        YouTubeRepository.networkChecker = { true }
        YouTubeRepository.httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { throw SocketTimeoutException("Simulated timeout") })
            .build()

        val expiredRes = YouTubeRepository.fetchLecturesForTopic(context, LectureCategory.QURAN_TAFSEER, forceRefresh = false)
        assertEquals(FeedLoadStatus.EXPIRED_CACHE_FALLBACK, expiredRes.status)
        assertTrue("Expired cache fallback must be flagged isCacheExpired=true", expiredRes.isCacheExpired)
        assertEquals(1, expiredRes.videos.size)

        // 5. Offline with zero cache -> ERROR_NO_CACHE
        YouTubeRepository.networkChecker = { false }
        val noCacheErr = YouTubeRepository.fetchLecturesForTopic(context, LectureCategory.ISLAMIC_QA, forceRefresh = false)
        assertEquals(FeedLoadStatus.ERROR_NO_CACHE, noCacheErr.status)
        assertTrue(noCacheErr.videos.isEmpty())
    }

    @Test
    fun testHonestLocalSearchReturnsEmptyWhenNoMatchInsteadOfFakeFallback() {
        val bayyinah = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "bayyinah" }
        val sampleItems = YouTubeRepository.parseAtomFeedXml(
            sampleAtomFeed(
                bayyinah.channelId,
                "Bayyinah",
                listOf(
                    Triple("vidAAA11111", "Seerah of the Prophet Muhammad", "2026-04-01T10:00:00Z"),
                    Triple("vidBBB22222", "Quran Tafseer Surah Yusuf", "2026-04-02T10:00:00Z")
                )
            ),
            bayyinah
        )

        val seerahHits = YouTubeRepository.searchLocalVerifiedLectures(sampleItems, query = "Seerah")
        assertEquals(1, seerahHits.size)
        assertEquals("vidAAA11111", seerahHits[0].id)

        val noHits = YouTubeRepository.searchLocalVerifiedLectures(sampleItems, query = "UnrelatedQuantumPhysicsQueryXYZ")
        assertTrue("Search with zero matches must return empty list (never substitute fake lectures)", noHits.isEmpty())
    }

    @Test
    fun testHarameynLiveVerificationNeverScrapesHtmlAndNeverFakesLive() = runBlocking {
        val requestedUrls = CopyOnWriteArrayList<String>()

        // 1. Offline check
        YouTubeRepository.networkChecker = { false }
        val offlineMakkah = YouTubeRepository.checkHarameynLiveStatus(context, HarameynLocation.MAKKAH)
        assertEquals(LiveBroadcastState.OFFLINE, offlineMakkah.state)
        assertNull(offlineMakkah.verifiedVideoId)

        // 2. Online HTTP error -> CURRENTLY_UNAVAILABLE
        YouTubeRepository.networkChecker = { true }
        YouTubeRepository.httpClient = buildMockClient { req ->
            requestedUrls.add(req.url.toString())
            Response.Builder()
                .request(req).protocol(Protocol.HTTP_1_1).code(503).message("Unavailable")
                .body("".toResponseBody("text/plain".toMediaType())).build()
        }
        val unavailMadinah = YouTubeRepository.checkHarameynLiveStatus(context, HarameynLocation.MADINAH)
        assertEquals(LiveBroadcastState.CURRENTLY_UNAVAILABLE, unavailMadinah.state)
        assertNull(unavailMadinah.verifiedVideoId)
        assertTrue("Must NEVER scrape /live HTML", requestedUrls.none { it.contains("/live") })

        // 3. Online valid official RSS -> LIVE_STATUS_UNCONFIRMED (recent uploads marked recorded, never LIVE)
        requestedUrls.clear()
        val makkahId = YouTubeRepository.HARAMEYN_CHANNELS.getValue(HarameynLocation.MAKKAH).channelId
        YouTubeRepository.httpClient = buildMockClient { req ->
            requestedUrls.add(req.url.toString())
            val xml = sampleAtomFeed(
                makkahId,
                "قناة القرآن الكريم",
                listOf(Triple("x9mC9QJIZH0", "صلاة العشاء من المسجد الحرام", "2026-11-14T17:54:17+00:00"))
            )
            Response.Builder()
                .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
        }
        val makkahRes = YouTubeRepository.checkHarameynLiveStatus(context, HarameynLocation.MAKKAH)
        assertEquals(LiveBroadcastState.LIVE_STATUS_UNCONFIRMED, makkahRes.state)
        assertNull("Without Data API credentials, verifiedVideoId must remain null (never fake LIVE)", makkahRes.verifiedVideoId)
        assertFalse(makkahRes.isEmbeddableConfirmed)
        assertEquals(1, makkahRes.recentRecordedUploads.size)
        assertEquals("x9mC9QJIZH0", makkahRes.recentRecordedUploads[0].id)
        assertTrue("Must only call official RSS feed", requestedUrls.all { it.contains("feeds/videos.xml") })
    }

    @Test
    fun testFavoritesAndRecentlyWatchedPersistence() {
        val sample = YouTubeRepository.parseAtomFeedXml(
            sampleAtomFeed(
                "UCRtiU-lpcBSi-ipFKyfIkug",
                "Bayyinah",
                listOf(Triple("favVid12345", "Patience in Trials", "2026-04-05T10:00:00Z"))
            ),
            null
        ).first()

        assertTrue(YouTubeRepository.toggleFavoriteLecture(context, sample))
        assertEquals(1, YouTubeRepository.getFavoriteLectures(context).size)
        assertFalse(YouTubeRepository.toggleFavoriteLecture(context, sample))
        assertTrue(YouTubeRepository.getFavoriteLectures(context).isEmpty())

        YouTubeRepository.recordWatchedLecture(context, sample)
        val recent = YouTubeRepository.getRecentlyWatchedLectures(context)
        assertEquals(1, recent.size)
        assertEquals("favVid12345", recent[0].id)

        // Podcast favorites & history also persist independently
        assertTrue(YouTubeRepository.toggleFavoritePodcast(context, sample))
        assertEquals(1, YouTubeRepository.getFavoritePodcasts(context).size)
        YouTubeRepository.recordWatchedPodcast(context, sample)
        assertEquals("favVid12345", YouTubeRepository.getRecentlyWatchedPodcasts(context)[0].id)
    }

    @Test
    fun testIFramePlayerHtmlAndAllErrorCodes() {
        val html = YouTubeIFramePlayerConfig.buildIFrameApiHtml("abc12345678", autoplay = true)
        assertTrue("Must load official YouTube IFrame API script", html.contains("https://www.youtube.com/iframe_api"))
        assertTrue("Must configure strict-origin-when-cross-origin", html.contains("strict-origin-when-cross-origin"))
        assertTrue("Must supply HTTPS origin matching loadDataWithBaseURL", html.contains("origin: '${YouTubeIFramePlayerConfig.APP_EMBED_ORIGIN}'"))
        assertTrue("Must preserve native YouTube controls & fullscreen", html.contains("controls: 1") && html.contains("fs: 1"))
        assertEquals(
            "https://www.youtube.com/embed/abc12345678?enablejsapi=1&autoplay=1&playsinline=1&rel=0&fs=1&origin=${YouTubeIFramePlayerConfig.APP_EMBED_ORIGIN}",
            YouTubeIFramePlayerConfig.buildOfficialEmbedUrl("abc12345678", autoplay = true)
        )
        assertEquals(
            "https://www.youtube.com/embed/abc12345678?autoplay=1&playsinline=1&rel=0&fs=1&modestbranding=1",
            YouTubeIFramePlayerConfig.buildDirectEmbedUrl("abc12345678", autoplay = true, useNoCookie = false)
        )
        assertEquals(
            "https://www.youtube-nocookie.com/embed/abc12345678?autoplay=1&playsinline=1&rel=0&fs=1&modestbranding=1",
            YouTubeIFramePlayerConfig.buildDirectEmbedUrl("abc12345678", autoplay = true, useNoCookie = true)
        )
        assertEquals(
            mapOf("Referer" to "https://com.aistudio.noorehidayat.vmgdjr/"),
            YouTubeIFramePlayerConfig.buildRefererHeaders("com.aistudio.noorehidayat.vmgdjr")
        )

        val err153 = YouTubeIFramePlayerConfig.formatErrorMessage(153, isNetworkError = false, isRendererCrash = false, AppLanguage.ENGLISH)
        assertTrue(err153.contains("153"))
        val err150 = YouTubeIFramePlayerConfig.formatErrorMessage(150, isNetworkError = false, isRendererCrash = false, AppLanguage.URDU)
        assertTrue(err150.contains("150"))
        val err100 = YouTubeIFramePlayerConfig.formatErrorMessage(100, isNetworkError = false, isRendererCrash = false, AppLanguage.ENGLISH)
        assertTrue(err100.contains("100"))
        val errNet = YouTubeIFramePlayerConfig.formatErrorMessage(408, isNetworkError = true, isRendererCrash = false, AppLanguage.ENGLISH)
        assertTrue(errNet.contains("Network"))
    }

    @Test
    fun testFullscreenControllerEntersLandscapeHidesViewAndRestoresOnExit() {
        val activity = Robolectric.buildActivity(Activity::class.java).create().start().resume().get()
        val host = FrameLayout(activity)
        val controller = FullscreenVideoController()
        controller.attachHost(activity, host)

        var callbackInvoked = false
        val fakeCustomView = View(activity)
        val callback = WebChromeClient.CustomViewCallback { callbackInvoked = true }

        controller.enterFullscreen(fakeCustomView, callback, activity)
        assertTrue(controller.isFullscreen)
        assertEquals(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, activity.requestedOrientation)
        assertEquals(View.VISIBLE, host.visibility)
        assertEquals(1, host.childCount)

        assertTrue(controller.exitFullscreen())
        assertFalse(controller.isFullscreen)
        assertTrue("CustomViewCallback.onCustomViewHidden must be called on exit", callbackInvoked)
        assertEquals(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, activity.requestedOrientation)
        assertEquals(View.GONE, host.visibility)
        assertEquals(0, host.childCount)
    }

    @Test
    fun testLecturesScreenDoesNotFetchOnOpenAndShowsNoVerifiedVideosMessageWhenEmpty() {
        val requestedUrls = CopyOnWriteArrayList<String>()
        YouTubeRepository.httpClient = buildMockClient { req ->
            requestedUrls.add(req.url.toString())
            // Return valid RSS with non-matching video title for QURAN_TAFSEER
            val body = sampleAtomFeed(
                "UCRtiU-lpcBSi-ipFKyfIkug",
                "Bayyinah",
                listOf(Triple("vid00011122", "General Channel Update", "2026-11-10T10:00:00Z"))
            )
            Response.Builder()
                .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                .body(body.toResponseBody("application/atom+xml".toMediaType())).build()
        }

        composeRule.setContent {
            NooreHidayatTheme {
                LecturesScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // 1. Must NOT auto-fetch all channels on open
        assertTrue("No network request should fire before topic selection", requestedUrls.isEmpty())
        composeRule.onNodeWithTag("lectures_lazy_column")
            .performScrollToNode(hasTestTag("lectures_topic_prompt_card"))
        composeRule.onNodeWithTag("lectures_topic_prompt_card").assertIsDisplayed()

        // 2. Select Quran Tafseer topic -> fetches ONLY mapped channels & displays honest empty message
        composeRule.onNodeWithTag("lectures_lazy_column")
            .performScrollToNode(hasTestTag("category_chip_${LectureCategory.QURAN_TAFSEER.id}"))
        composeRule.onNodeWithTag("category_chip_${LectureCategory.QURAN_TAFSEER.id}").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000L) {
            composeRule.onAllNodesWithTag("lectures_empty_state").fetchSemanticsNodes().isNotEmpty()
        }
        assertEquals(2, requestedUrls.size)
        composeRule.onNodeWithTag("lectures_lazy_column")
            .performScrollToNode(hasTestTag("lectures_empty_state"))
        composeRule.onNodeWithText("No verified videos are currently available for this topic.").assertIsDisplayed()
    }

    @Test
    fun testLiveStreamScreenNeverDisplaysFakeLiveBadge() {
        val makkahId = YouTubeRepository.HARAMEYN_CHANNELS.getValue(HarameynLocation.MAKKAH).channelId
        YouTubeRepository.httpClient = buildMockClient { req ->
            val body = sampleAtomFeed(
                makkahId,
                "Saudi Quran TV",
                listOf(Triple("x9mC9QJIZH0", "Isha Prayer Makkah", "2026-11-14T17:54:17+00:00"))
            )
            Response.Builder()
                .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                .body(body.toResponseBody("application/atom+xml".toMediaType())).build()
        }

        composeRule.setContent {
            NooreHidayatTheme {
                LiveStreamScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitUntil(timeoutMillis = 5_000L) {
            composeRule.onAllNodesWithText("Live broadcast status cannot be confirmed")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("live_status_card").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Live broadcast status cannot be confirmed").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Recorded Upload • Not Live").performScrollTo().assertIsDisplayed()
        assertEquals(0, composeRule.onAllNodesWithTag("embedded_live_player").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("24/7 LIVE").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("FEED").fetchSemanticsNodes().size)
    }

    @Test
    fun testAll12CategoriesAndNewKeysLocalizedInAll4Languages() {
        val keys = LectureCategory.entries.map { it.labelKey } + listOf(
            "lectures_banner_tag",
            "lectures_header_title",
            "lectures_search_hint",
            "lectures_saved_favorites",
            "lectures_recently_watched",
            "lectures_offline_cache_banner",
            "lectures_empty_search_title",
            "lectures_select_topic_prompt",
            "lectures_no_topic_videos",
            "lectures_invalid_mapping",
            "pod_select_language_prompt",
            "pod_lang_urdu",
            "pod_lang_arabic",
            "pod_lang_english",
            "pod_lang_not_verified",
            "live_tab_makkah",
            "live_tab_madinah",
            "live_status_no_active",
            "live_status_unconfirmed",
            "live_status_unavailable",
            "live_recorded_badge",
            "player_error_153"
        )
        assertEquals("Must cover all 12 categories", 12, LectureCategory.entries.size)

        keys.forEach { key ->
            val en = AppLocalization.get(key, AppLanguage.ENGLISH)
            val ur = AppLocalization.get(key, AppLanguage.URDU)
            val ar = AppLocalization.get(key, AppLanguage.ARABIC)
            val hi = AppLocalization.get(key, AppLanguage.HINDI)

            assertTrue("EN key '$key' missing", en != key && en.isNotBlank())
            assertTrue("UR key '$key' missing", ur != key && ur.isNotBlank())
            assertTrue("AR key '$key' missing", ar != key && ar.isNotBlank())
            assertTrue("HI key '$key' missing", hi != key && hi.isNotBlank())
        }
    }

    @Test
    fun testRendererAutoRecoveryOnlyRetriesTransientKillsOnce() {
        assertTrue(YouTubeIFramePlayerConfig.shouldAutoRecoverRenderer(didCrash = false, priorAttempts = 0))
        assertFalse(YouTubeIFramePlayerConfig.shouldAutoRecoverRenderer(didCrash = false, priorAttempts = 1))
        assertFalse(YouTubeIFramePlayerConfig.shouldAutoRecoverRenderer(didCrash = true, priorAttempts = 0))
    }

    @Test
    fun testDisposeWebViewSafelyIsIdempotentAndDetachesParent() {
        val activity = Robolectric.buildActivity(Activity::class.java).create().get()
        val parent = FrameLayout(activity)
        val webView = android.webkit.WebView(activity)
        parent.addView(webView)
        assertEquals(1, parent.childCount)

        val disposed = java.util.concurrent.atomic.AtomicBoolean(false)
        assertTrue(
            "First disposal must execute cleanly",
            YouTubeIFramePlayerConfig.disposeWebViewSafely(
                webView = webView,
                disposedFlag = disposed,
                rendererAlreadyGone = true
            )
        )
        assertEquals(0, parent.childCount)
        assertFalse(
            "Subsequent onRelease call must not double-destroy WebView",
            YouTubeIFramePlayerConfig.disposeWebViewSafely(
                webView = webView,
                disposedFlag = disposed,
                rendererAlreadyGone = false
            )
        )
        assertFalse(
            "Null WebView must safely no-op",
            YouTubeIFramePlayerConfig.disposeWebViewSafely(
                webView = null,
                disposedFlag = java.util.concurrent.atomic.AtomicBoolean(false),
                rendererAlreadyGone = false
            )
        )
    }

    @Test
    fun testSoftwareGpuEnvironmentAndSessionFallbackBehavior() {
        YouTubeIFramePlayerConfig.resetSessionFallbackForTest()
        try {
            // Virtualized Cuttlefish/Goldfish emulator without DRM rendernode -> software mode
            assertTrue(
                YouTubeIFramePlayerConfig.isSoftwareGpuEnvironment(
                    hardware = "cutf_cvm",
                    fingerprint = "google/aosp_cf_x86_64_phone/vsoc_x86_64",
                    product = "aosp_cf_x86_64_phone",
                    model = "Cuttlefish x86_64 phone",
                    hasRenderNode = false
                )
            )
            // Same emulator WITH a working DRM rendernode -> hardware mode allowed
            assertFalse(
                YouTubeIFramePlayerConfig.isSoftwareGpuEnvironment(
                    hardware = "cutf_cvm",
                    fingerprint = "google/aosp_cf_x86_64_phone/vsoc_x86_64",
                    product = "aosp_cf_x86_64_phone",
                    model = "Cuttlefish x86_64 phone",
                    hasRenderNode = true
                )
            )
            // Physical Android device -> hardware mode preserved
            assertFalse(
                YouTubeIFramePlayerConfig.isSoftwareGpuEnvironment(
                    hardware = "qcom",
                    fingerprint = "samsung/dm1qxxx/dm1q:14/UP1A",
                    product = "dm1qxxx",
                    model = "SM-S911B",
                    hasRenderNode = false
                )
            )
            // Autoplay disabled in software mode, preserved in hardware mode
            assertFalse(YouTubeIFramePlayerConfig.effectiveAutoplay(requestedAutoplay = true, softwareLayerActive = true))
            assertTrue(YouTubeIFramePlayerConfig.effectiveAutoplay(requestedAutoplay = true, softwareLayerActive = false))

            // Session-wide fallback persists once marked after a renderer crash
            assertFalse(YouTubeIFramePlayerConfig.shouldUseSoftwareLayer(localFallback = false))
            YouTubeIFramePlayerConfig.markSessionSoftwareFallback()
            assertTrue(YouTubeIFramePlayerConfig.shouldUseSoftwareLayer(localFallback = false))
        } finally {
            YouTubeIFramePlayerConfig.resetSessionFallbackForTest()
        }
    }
}
