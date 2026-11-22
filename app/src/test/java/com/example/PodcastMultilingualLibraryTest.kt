package com.example

import android.app.Application
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
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.data.lectures.YouTubeRssParser
import com.example.data.model.AppLanguage
import com.example.data.podcast.PodcastCandidateVideo
import com.example.data.podcast.PodcastCatalogBridge
import com.example.data.podcast.PodcastFeedStatus
import com.example.data.podcast.PodcastLanguage
import com.example.data.podcast.PodcastLibraryRepository
import com.example.data.podcast.PodcastLongFormFilter
import com.example.data.podcast.PodcastProfileCategory
import com.example.data.podcast.PodcastProfileType
import com.example.data.podcast.PodcastRefreshWorker
import com.example.data.podcast.PodcastRejectionReason
import com.example.data.podcast.PodcastScholarRegistry
import com.example.data.podcast.PodcastSourceKind
import com.example.data.repository.YouTubeRepository
import com.example.ui.screens.PodcastsScreen
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PodcastMultilingualLibraryTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application
    private val originalClient = PodcastLibraryRepository.httpClient

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        PodcastCatalogBridge.clearCacheForTest()
        PodcastLibraryRepository.httpClient = originalClient
        PodcastLibraryRepository.networkChecker = { true }
        PodcastLibraryRepository.currentTimeProvider = { 1_763_600_000_000L }
        PodcastLibraryRepository.youtubeApiKeyProvider = { "" }
        PodcastLibraryRepository.clearTestCaches(appContext)
        YouTubeRepository.clearTestCaches()
    }

    @After
    fun tearDown() {
        PodcastLibraryRepository.httpClient = originalClient
        PodcastLibraryRepository.networkChecker = null
        PodcastLibraryRepository.currentTimeProvider = { System.currentTimeMillis() }
        PodcastLibraryRepository.youtubeApiKeyProvider = { "" }
        PodcastLibraryRepository.clearTestCaches(appContext)
        YouTubeRepository.clearTestCaches()
    }

    private fun buildAtomFeed(
        channelId: String,
        authorName: String,
        entries: List<AtomEntrySpec>
    ): String {
        val entriesXml = entries.joinToString("\n") { e ->
            val mediaDur = if (e.durationSeconds != null) {
                val wh = if (e.width != null && e.height != null) " width=\"${e.width}\" height=\"${e.height}\"" else ""
                "  <media:group><media:content duration=\"${e.durationSeconds}\"$wh/>" +
                    (if (e.views != null) "<media:community><media:statistics views=\"${e.views}\"/></media:community>" else "") +
                    "<media:description>${e.description}</media:description></media:group>\n"
            } else {
                "  <media:group><media:description>${e.description}</media:description></media:group>\n"
            }
            "<entry>\n" +
                "  <id>yt:video:${e.videoId}</id>\n" +
                "  <yt:videoId>${e.videoId}</yt:videoId>\n" +
                "  <yt:channelId>$channelId</yt:channelId>\n" +
                "  <title>${e.title}</title>\n" +
                "  <link rel=\"alternate\" href=\"${e.url}\"/>\n" +
                "  <author><name>$authorName</name></author>\n" +
                "  <published>${e.publishedIso}</published>\n" +
                mediaDur +
                "</entry>"
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<feed xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\" " +
            "xmlns:media=\"http://search.yahoo.com/mrss/\" " +
            "xmlns=\"http://www.w3.org/2005/Atom\">\n" +
            "  <title>$authorName</title>\n" +
            entriesXml + "\n" +
            "</feed>"
    }

    private data class AtomEntrySpec(
        val videoId: String,
        val title: String,
        val url: String = "https://www.youtube.com/watch?v=$videoId",
        val description: String = "",
        val publishedIso: String = "2026-11-18T10:00:00Z",
        val durationSeconds: Int? = 1800,
        val views: Long? = 12_500L,
        val width: Int? = 1920,
        val height: Int? = 1080
    )

    // =========================================================================
    // 1. REGISTRY & 300+ VERIFIED CHANNEL CATALOG INTEGRITY TESTS
    // =========================================================================

    @Test
    fun catalogBridge_exposesOver300VerifiedChannelsStrictlySeparatedByLanguageAndExcludesTaqiUsmani() {
        val total = PodcastCatalogBridge.totalVerifiedChannelCount(appContext)
        val urdu = PodcastCatalogBridge.channelsForLanguage(appContext, PodcastLanguage.URDU)
        val english = PodcastCatalogBridge.channelsForLanguage(appContext, PodcastLanguage.ENGLISH)
        val arabic = PodcastCatalogBridge.channelsForLanguage(appContext, PodcastLanguage.ARABIC)

        assertTrue("Expected 300+ verified podcast channels, got $total", total >= 300)
        assertTrue("Expected 100+ verified Urdu channels, got ${urdu.size}", urdu.size >= 100)
        assertTrue("Expected 100+ verified English channels, got ${english.size}", english.size >= 100)
        assertTrue("Expected 100+ verified Arabic channels, got ${arabic.size}", arabic.size >= 100)

        // Every channel ID must be a valid UC channel ID and must exclude Mufti Taqi Usmani
        val all = urdu + english + arabic
        all.forEach { entry ->
            assertTrue("Invalid UC ID: ${entry.channelId}", YouTubeRssParser.isValidChannelId(entry.channelId))
            assertFalse("Mufti Taqi Usmani must be excluded", entry.nameEn.lowercase().contains("taqi usmani"))
            assertFalse("Mufti Taqi Usmani UC ID must be excluded", entry.channelId == "UCLfMxJctTnF5BidnWSHKsNQ")
        }

        // Major requested scholars & Great History of Islam Channel must be present
        assertNotNull(PodcastScholarRegistry.findById("ur_dr_israr_ahmed"))
        assertNotNull(PodcastScholarRegistry.findById("ur_dr_zakir_naik"))
        assertNotNull(PodcastScholarRegistry.findById("ur_peer_ajmal_raza_qadri"))
        assertEquals(PodcastProfileType.CHANNEL, PodcastScholarRegistry.GREAT_HISTORY_OF_ISLAM_CHANNEL.profileType)
        assertEquals(
            "UCRNMo453JQk8dpYsAGrIwFQ",
            PodcastScholarRegistry.GREAT_HISTORY_OF_ISLAM_CHANNEL.sources.first().channelId
        )
        assertEquals(27, PodcastScholarRegistry.ENGLISH_SCHOLARS.size)
        assertEquals(27, PodcastScholarRegistry.ARABIC_SCHOLARS.size)
        assertTrue(PodcastScholarRegistry.URDU_SCHOLARS.none { it.displayNameEn.lowercase().contains("taqi usmani") })
    }

    // =========================================================================
    // 2. EXHAUSTIVE SHORTS FILTERING TESTS
    // =========================================================================

    @Test
    fun longFormFilter_rejectsAllShortsAndClipsAndAcceptsVerifiedLongFormVideos() {
        val scholar = PodcastScholarRegistry.findById("en_mufti_menk")!!
        val cid = scholar.sources.first().channelId

        fun candidate(
            id: String,
            title: String,
            url: String = "https://www.youtube.com/watch?v=$id",
            durationSec: Int?,
            w: Int? = 1920,
            h: Int? = 1080
        ) = PodcastCandidateVideo(
            videoId = id,
            title = title,
            channelId = cid,
            channelName = "Mufti Menk",
            videoUrl = url,
            durationSeconds = durationSec,
            videoWidth = w,
            videoHeight = h,
            sourceKind = PodcastSourceKind.OFFICIAL_CHANNEL
        )

        // 1. 30-second vertical video -> REJECT
        val r30 = PodcastLongFormFilter.evaluate(
            candidate("vidShort030", "Patience in Islam Full Lecture", durationSec = 30, w = 1080, h = 1920),
            scholar
        )
        assertFalse(r30.accepted)
        assertEquals(PodcastRejectionReason.VERTICAL_OR_SQUARE_SHORT, r30.rejectionReason)

        // 2. 60-second vertical video -> REJECT
        val r60 = PodcastLongFormFilter.evaluate(
            candidate("vidShort060", "Hope and Mercy Discussion", durationSec = 60, w = 1080, h = 1920),
            scholar
        )
        assertFalse(r60.accepted)
        assertEquals(PodcastRejectionReason.VERTICAL_OR_SQUARE_SHORT, r60.rejectionReason)

        // 3. 2-minute vertical video -> REJECT
        val r120 = PodcastLongFormFilter.evaluate(
            candidate("vidShort120", "Understanding Faith Discussion", durationSec = 120, w = 1080, h = 1920),
            scholar
        )
        assertFalse(r120.accepted)
        assertEquals(PodcastRejectionReason.VERTICAL_OR_SQUARE_SHORT, r120.rejectionReason)

        // 4. 3-minute (180s) vertical YouTube Short -> REJECT
        val r180 = PodcastLongFormFilter.evaluate(
            candidate("vidShort180", "Complete Reminder on Prayer", durationSec = 180, w = 1080, h = 1920),
            scholar
        )
        assertFalse(r180.accepted)
        assertEquals(PodcastRejectionReason.VERTICAL_OR_SQUARE_SHORT, r180.rejectionReason)

        // 5. Video with /shorts/ URL -> REJECT (even if duration looks long)
        val rUrl = PodcastLongFormFilter.evaluate(
            candidate(
                "vidShortUrl",
                "Full Podcast Episode on Mercy",
                url = "https://www.youtube.com/shorts/vidShortUrl",
                durationSec = 600
            ),
            scholar
        )
        assertFalse(rUrl.accepted)
        assertEquals(PodcastRejectionReason.EXPLICIT_SHORTS_URL, rUrl.rejectionReason)

        // 6. Short lecture clip -> REJECT
        val rClip = PodcastLongFormFilter.evaluate(
            candidate("vidClip1111", "Best Lecture Clip on Patience and Gratitude", durationSec = 400),
            scholar
        )
        assertFalse(rClip.accepted)
        assertEquals(PodcastRejectionReason.CLIP_TRAILER_OR_PROMO, rClip.rejectionReason)

        // 7. 5-minute (300s) verified long-form video -> ACCEPT
        val r5m = PodcastLongFormFilter.evaluate(
            candidate("vidLong300s", "Islamic Q&A on Patience and Family", durationSec = 300, w = 1920, h = 1080),
            scholar
        )
        assertTrue(r5m.accepted)

        // 8. 30-minute (1800s) full interview -> ACCEPT
        val r30m = PodcastLongFormFilter.evaluate(
            candidate("vidInt1800s", "Full Interview with Mufti Menk on Modern Youth", durationSec = 1800, w = 1920, h = 1080),
            scholar
        )
        assertTrue(r30m.accepted)
        assertTrue(PodcastProfileCategory.FULL_INTERVIEWS in r30m.categories)
        assertTrue(PodcastProfileCategory.YOUTH_FAMILY in r30m.categories)

        // 9. Unknown-orientation video at <= 3 minutes (180s) -> REJECT
        val rUnknown180 = PodcastLongFormFilter.evaluate(
            candidate("vidUnk18000", "Podcast Episode on Heart Purification", durationSec = 180, w = null, h = null),
            scholar
        )
        assertFalse(rUnknown180.accepted)
        assertEquals(PodcastRejectionReason.UNVERIFIED_SHORT_DURATION, rUnknown180.rejectionReason)

        // 10. Unknown-duration ambiguous upload without long-form markers -> REJECT
        val rAmbig = PodcastLongFormFilter.evaluate(
            candidate("vidAmbig001", "Look at this amazing moment today", durationSec = null, w = null, h = null),
            scholar
        )
        assertFalse(rAmbig.accepted)
        assertEquals(PodcastRejectionReason.UNVERIFIED_LONG_FORM_METADATA, rAmbig.rejectionReason)
    }

    // =========================================================================
    // 3. CONTENT VERIFICATION TESTS (WRONG SCHOLAR, LANGUAGE, DUPES, URL, <100 HONEST COUNT)
    // =========================================================================

    @Test
    fun contentVerification_rejectsWrongScholarWrongLanguageDuplicatesBrokenUrlAndMissingMetadata() {
        val hostedScholar = PodcastScholarRegistry.findById("en_mohamed_hoblos")!!
        val hostedCid = hostedScholar.sources.first().channelId

        val wrongScholar = PodcastCandidateVideo(
            videoId = "wrngSch0001",
            title = "Full Podcast Episode on History with Someone Else",
            channelId = hostedCid,
            channelName = "OnePath Network",
            videoUrl = "https://www.youtube.com/watch?v=wrngSch0001",
            durationSeconds = 2400,
            sourceKind = PodcastSourceKind.HOSTED_INTERVIEW,
            requiresScholarAttribution = true
        )
        assertEquals(
            PodcastRejectionReason.WRONG_SCHOLAR,
            PodcastLongFormFilter.evaluate(wrongScholar, hostedScholar).rejectionReason
        )

        val urduScholar = PodcastScholarRegistry.findById("ur_dr_israr_ahmed")!!
        val urduCid = urduScholar.sources.first().channelId
        val englishInUrdu = PodcastCandidateVideo(
            videoId = "engInUr0001",
            title = "The Complete English Podcast Discussion About Modern Finance",
            channelId = urduCid,
            channelName = "Tanzeem-e-Islami",
            videoUrl = "https://www.youtube.com/watch?v=engInUr0001",
            durationSeconds = 1800,
            sourceKind = PodcastSourceKind.OFFICIAL_CHANNEL
        )
        assertEquals(
            PodcastRejectionReason.WRONG_LANGUAGE,
            PodcastLongFormFilter.evaluate(englishInUrdu, urduScholar).rejectionReason
        )

        val brokenUrl = PodcastCandidateVideo(
            videoId = "brknUrl0001",
            title = "قرآن کی مکمل تفسیر اور بیان",
            channelId = urduCid,
            channelName = "Tanzeem",
            videoUrl = "not-a-valid-url",
            durationSeconds = 1800
        )
        assertEquals(
            PodcastRejectionReason.INVALID_VIDEO_ID_OR_URL,
            PodcastLongFormFilter.evaluate(brokenUrl, urduScholar).rejectionReason
        )

        val deletedTitle = PodcastCandidateVideo(
            videoId = "delTitl0001",
            title = "[Deleted video]",
            channelId = urduCid,
            channelName = "Tanzeem",
            videoUrl = "https://www.youtube.com/watch?v=delTitl0001",
            durationSeconds = 1800
        )
        assertEquals(
            PodcastRejectionReason.MISSING_TITLE_OR_DELETED,
            PodcastLongFormFilter.evaluate(deletedTitle, urduScholar).rejectionReason
        )

        // Deduplication test + honest < 100 count (37 unique videos -> exactly 37, never padded!)
        val batch = (1..37).map { i ->
            val vid = String.format("urLong%05d", i)
            PodcastCandidateVideo(
                videoId = vid,
                title = "بیان القرآن مکمل درس نمبر $i - ڈاکٹر اسرار احمد",
                channelId = urduCid,
                channelName = "Tanzeem-e-Islami Official",
                videoUrl = "https://www.youtube.com/watch?v=$vid",
                durationSeconds = 2400,
                publishedEpochMs = 1_760_000_000_000L + i * 1000L
            )
        } + listOf(
            // Duplicate of #1
            PodcastCandidateVideo(
                videoId = "urLong00001",
                title = "بیان القرآن مکمل درس نمبر 1 - ڈاکٹر اسرار احمد",
                channelId = urduCid,
                channelName = "Tanzeem-e-Islami Official",
                videoUrl = "https://www.youtube.com/watch?v=urLong00001",
                durationSeconds = 2400
            )
        )

        val (verified, rejections) = PodcastLongFormFilter.filterAndDeduplicate(batch, urduScholar)
        assertEquals(37, verified.size)
        assertEquals(1, rejections[PodcastRejectionReason.DUPLICATE_VIDEO])
    }

    // =========================================================================
    // 4. 100+ LONG-FORM SUGGESTIONS ACROSS 300+ CHANNELS & ZERO SHORTS TEST
    // =========================================================================

    @Test
    fun loadLanguageSuggestions_aggregatesOver100RealLongFormVideosWithZeroShortsAndSupportsLoadMore() = runBlocking {
        val requestedUrls = CopyOnWriteArrayList<String>()
        val urduChannels = PodcastCatalogBridge.channelsForLanguage(appContext, PodcastLanguage.URDU)
        assertTrue(urduChannels.size >= 100)

        PodcastLibraryRepository.httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val url = chain.request().url.toString()
                requestedUrls.add(url)
                val cid = url.substringAfter("channel_id=").substringBefore("&")
                val idx = urduChannels.indexOfFirst { it.channelId == cid }.coerceAtLeast(0)
                // Each channel emits 4 real long-form Urdu videos + 1 Short (must be rejected!) + 1 English video (must be rejected!)
                val entries = (1..4).map { n ->
                    AtomEntrySpec(
                        videoId = String.format("u%03dLong%03d", idx, n),
                        title = "مکمل علمی بیان اور قرآن کی تفسیر قسط $n - چینل $idx",
                        durationSeconds = 1800 + n * 120,
                        views = 5000L * n
                    )
                } + listOf(
                    AtomEntrySpec(
                        videoId = String.format("u%03dShrt001", idx),
                        title = "مکمل بیان #shorts",
                        url = "https://www.youtube.com/shorts/${String.format("u%03dShrt001", idx)}",
                        durationSeconds = 45,
                        width = 1080,
                        height = 1920
                    ),
                    AtomEntrySpec(
                        videoId = String.format("u%03dEngl001", idx),
                        title = "The Complete English Podcast Discussion About Modern History",
                        durationSeconds = 2400
                    )
                )
                val xml = buildAtomFeed(cid, "Verified Urdu Channel $idx", entries)
                Response.Builder()
                    .request(chain.request()).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
            })
            .build()

        val firstBatch = PodcastLibraryRepository.loadLanguageSuggestions(
            context = appContext,
            language = PodcastLanguage.URDU,
            forceRefresh = true
        )

        assertEquals(PodcastFeedStatus.ONLINE_FRESH, firstBatch.status)
        assertTrue("Expected 100+ long-form suggestions, got ${firstBatch.verifiedVideoCount}", firstBatch.verifiedVideoCount >= 100)
        assertTrue("Expected Shorts and wrong-language videos to be filtered out", firstBatch.rejectedCount >= 70)
        assertTrue("Must never contain any /shorts/ URL", firstBatch.videos.none { it.videoUrl.contains("/shorts/") })
        assertTrue("Must never contain any video < 240s", firstBatch.videos.all { (it.durationSeconds ?: 300) >= 240 })
        assertTrue("Must be strictly URDU", firstBatch.videos.all { it.language == PodcastLanguage.URDU })
        assertTrue("Should have more channel batches in 100+ channel catalog", firstBatch.hasMoreChannelBatches)

        // Load next batch of channels -> increases verified long-form videos further
        val secondBatch = PodcastLibraryRepository.loadLanguageSuggestions(
            context = appContext,
            language = PodcastLanguage.URDU,
            loadNextBatch = true
        )
        assertTrue(secondBatch.verifiedVideoCount > firstBatch.verifiedVideoCount)
        assertTrue(secondBatch.channelsFetchedCount > firstBatch.channelsFetchedCount)
    }

    // =========================================================================
    // 5. REFRESH (NEW SHORT REJECTED / NEW LONG-FORM ACCEPTED), QUOTA, CACHE, OFFLINE, PLAYER DURATION
    // =========================================================================

    @Test
    fun refresh_rejectsNewlyUploadedShortAcceptsNewLongFormHandlesQuotaAndOfflineCache() = runBlocking {
        val scholar = PodcastScholarRegistry.findById("ur_peer_ajmal_raza_qadri")!!
        val primaryCid = scholar.sources.first().channelId
        val refreshStage = AtomicInteger(1)

        PodcastLibraryRepository.httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val url = chain.request().url.toString()
                if (url.contains("googleapis.com/youtube/v3")) {
                    return@Interceptor Response.Builder()
                        .request(chain.request()).protocol(Protocol.HTTP_1_1).code(403).message("Forbidden")
                        .body("""{"error":{"errors":[{"reason":"quotaExceeded"}]}}""".toResponseBody("application/json".toMediaType()))
                        .build()
                }
                val entries = if (url.contains(primaryCid)) {
                    if (refreshStage.get() == 1) {
                        listOf(
                            AtomEntrySpec("ajmLong0001", "سیرت النبی مکمل بیان - پیر اجمل رضا قادری", durationSeconds = 2700)
                        )
                    } else {
                        listOf(
                            AtomEntrySpec("ajmLong0001", "سیرت النبی مکمل بیان - پیر اجمل رضا قادری", durationSeconds = 2700),
                            AtomEntrySpec(
                                "ajmNewShrt1",
                                "نیا شارٹ بیان #shorts",
                                url = "https://www.youtube.com/shorts/ajmNewShrt1",
                                durationSeconds = 55,
                                width = 1080,
                                height = 1920
                            ),
                            AtomEntrySpec("ajmNewLong2", "توبہ اور اصلاح نفس مکمل خطاب - پیر اجمل رضا قادری", durationSeconds = 3100)
                        )
                    }
                } else {
                    emptyList()
                }
                Response.Builder()
                    .request(chain.request()).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(buildAtomFeed(primaryCid, "Peer Ajmal Raza Qadri Official", entries).toResponseBody("application/atom+xml".toMediaType()))
                    .build()
            })
            .build()

        // Initial fetch
        val res1 = PodcastLibraryRepository.loadScholarProfile(appContext, scholar, forceRefresh = true)
        assertEquals(PodcastFeedStatus.ONLINE_FRESH, res1.status)
        assertEquals(1, res1.verifiedVideoCount)

        // Advance time by 35 minutes -> cache becomes stale, refresh runs: rejects new Short, accepts new Long-Form
        PodcastLibraryRepository.currentTimeProvider = { 1_763_600_000_000L + 35L * 60L * 1000L }
        assertTrue(PodcastLibraryRepository.isCacheStale(appContext, scholar.id))
        refreshStage.set(2)
        val res2 = PodcastLibraryRepository.loadScholarProfile(appContext, scholar, forceRefresh = false)
        assertEquals(2, res2.verifiedVideoCount)
        assertTrue(res2.videos.any { it.videoId == "ajmNewLong2" })
        assertFalse(res2.videos.any { it.videoId == "ajmNewShrt1" })

        // Quota exceeded with YouTube API key configured -> preserves verified cache and sets quota cooldown
        PodcastLibraryRepository.youtubeApiKeyProvider = { "FAKE_TEST_API_KEY" }
        val resQuota = PodcastLibraryRepository.loadScholarProfile(appContext, scholar, forceRefresh = true)
        assertEquals(PodcastFeedStatus.QUOTA_EXCEEDED_CACHE, resQuota.status)
        assertEquals(2, resQuota.verifiedVideoCount)
        assertTrue(PodcastLibraryRepository.isQuotaBlocked(appContext))

        // Offline mode -> serves verified cache cleanly with OFFLINE_CACHE
        PodcastLibraryRepository.youtubeApiKeyProvider = { "" }
        PodcastLibraryRepository.networkChecker = { false }
        val resOffline = PodcastLibraryRepository.loadScholarProfile(appContext, scholar, forceRefresh = true)
        assertEquals(PodcastFeedStatus.OFFLINE_CACHE, resOffline.status)
        assertEquals(2, resOffline.verifiedVideoCount)

        // Player verifies real duration of ajmLong0001 is 90s (< 240s) -> immediately purged!
        val afterPlayer = PodcastLibraryRepository.reapplyPlayerDuration(appContext, scholar, "ajmLong0001", 90)
        assertNotNull(afterPlayer)
        assertEquals(1, afterPlayer!!.verifiedVideoCount)
        assertFalse(afterPlayer.videos.any { it.videoId == "ajmLong0001" })
    }

    // =========================================================================
    // 6. WORKMANAGER 30-MINUTE BACKGROUND REFRESH WORKER TEST
    // =========================================================================

    @Test
    fun podcastRefreshWorker_builds30MinConnectedRequestAndExecutesDoWorkSuccessfully() = runBlocking {
        val request = PodcastRefreshWorker.buildPeriodicRequest()
        assertEquals(NetworkType.CONNECTED, request.workSpec.constraints.requiredNetworkType)
        assertEquals(30L * 60L * 1000L, request.workSpec.intervalDuration)

        PodcastLibraryRepository.saveLastSelectedLanguage(appContext, PodcastLanguage.ENGLISH)
        PodcastLibraryRepository.httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val cid = chain.request().url.toString().substringAfter("channel_id=").substringBefore("&")
                val xml = buildAtomFeed(
                    cid,
                    "Verified English Scholar",
                    listOf(AtomEntrySpec("enBgWrk0001", "Complete Podcast Episode on Faith and Resilience", durationSeconds = 2400))
                )
                Response.Builder()
                    .request(chain.request()).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
            })
            .build()

        val worker = TestListenableWorkerBuilder<PodcastRefreshWorker>(appContext).build()
        val result = worker.doWork()
        assertEquals(ListenableWorker.Result.success(), result)
    }

    // =========================================================================
    // 7. COMPOSE UI INTEGRATION: LANGUAGES, SCHOLARS, PROFILE, NON-EMPTY CATEGORIES, PLAYBACK, SEARCH
    // =========================================================================

    @Test
    fun podcastsScreen_supportsScholarsDirectoryProfileDynamicCategoriesPlaybackAndNoShorts() {
        val israrCid = PodcastScholarRegistry.findById("ur_dr_israr_ahmed")!!.sources.first().channelId

        PodcastLibraryRepository.httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val url = chain.request().url.toString()
                val xml = if (url.contains(israrCid)) {
                    buildAtomFeed(
                        israrCid,
                        "Tanzeem-e-Islami Official",
                        listOf(
                            AtomEntrySpec(
                                videoId = "isrQrn00001",
                                title = "بیان القرآن مکمل تفسیر سورۃ البقرہ - ڈاکٹر اسرار احمد",
                                durationSeconds = 3600,
                                views = 95_000L
                            ),
                            AtomEntrySpec(
                                videoId = "isrShrt0001",
                                title = "مختصر کلپ #shorts",
                                url = "https://www.youtube.com/shorts/isrShrt0001",
                                durationSeconds = 40,
                                width = 1080,
                                height = 1920
                            )
                        )
                    )
                } else {
                    buildAtomFeed("UCempty0000000000000000", "Empty", emptyList())
                }
                Response.Builder()
                    .request(chain.request()).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
            })
            .build()

        composeRule.setContent {
            NooreHidayatTheme {
                PodcastsScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // 1. Select Urdu Podcasts -> loads long-form video, filters out Short!
        composeRule.onNodeWithTag("pod_lang_ur").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000L) {
            composeRule.onAllNodesWithTag("podcast_card_isrQrn00001").fetchSemanticsNodes().isNotEmpty()
        }
        assertEquals(0, composeRule.onAllNodesWithTag("podcast_card_isrShrt0001").fetchSemanticsNodes().size)

        // 2. Switch to Scholars & Channels -> verify Great History of Islam Channel + Dr. Israr Ahmed + Peer Ajmal Raza Qadri
        composeRule.onNodeWithTag("pod_subtab_scholars").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("podcasts_lazy_column")
            .performScrollToNode(hasTestTag("podcast_scholar_ch_great_history_of_islam"))
        composeRule.onNodeWithTag("podcast_scholar_ch_great_history_of_islam").assertIsDisplayed()
        composeRule.onNodeWithTag("podcasts_lazy_column")
            .performScrollToNode(hasTestTag("podcast_scholar_ur_dr_israr_ahmed"))
        composeRule.onNodeWithTag("podcast_scholar_ur_dr_israr_ahmed").assertIsDisplayed()

        // 3. Open Dr. Israr Ahmed Scholar Profile -> displays honest verified count & ONLY non-empty categories
        composeRule.onNodeWithTag("podcast_scholar_ur_dr_israr_ahmed").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000L) {
            composeRule.onAllNodesWithTag("pod_cat_quran_tafseer").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("podcasts_lazy_column")
            .performScrollToNode(hasTestTag("podcast_scholar_profile_header"))
        composeRule.onNodeWithTag("podcast_scholar_profile_header").assertIsDisplayed()
        composeRule.onNodeWithTag("podcasts_lazy_column")
            .performScrollToNode(hasTestTag("podcast_profile_categories_row"))
        composeRule.onNodeWithTag("pod_cat_all_eligible").assertIsDisplayed()
        composeRule.onNodeWithTag("pod_cat_quran_tafseer").assertIsDisplayed()
        // Empty category (e.g. youth_family has 0 matching videos) must NOT be displayed!
        assertEquals(0, composeRule.onAllNodesWithTag("pod_cat_youth_family").fetchSemanticsNodes().size)

        // 4. Tap video card -> opens official In-App YouTube Player
        composeRule.onNodeWithTag("podcasts_lazy_column")
            .performScrollToNode(hasTestTag("podcast_card_isrQrn00001"))
        composeRule.onNodeWithTag("podcast_card_isrQrn00001").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000L) {
            composeRule.onAllNodesWithTag("in_app_podcast_player").fetchSemanticsNodes().isNotEmpty()
        }

        // 5. Search for Short title -> never leaks Shorts through search
        composeRule.onNodeWithTag("podcasts_lazy_column")
            .performScrollToNode(hasTestTag("podcast_search_input"))
        composeRule.onNodeWithTag("podcast_search_input").performTextInput("shorts")
        composeRule.waitForIdle()
        assertEquals(0, composeRule.onAllNodesWithTag("podcast_card_isrShrt0001").fetchSemanticsNodes().size)

        // 6. Back to Scholars Directory button works cleanly
        composeRule.onNodeWithTag("podcasts_lazy_column")
            .performScrollToNode(hasTestTag("podcast_back_to_directory_btn"))
        composeRule.onNodeWithTag("podcast_back_to_directory_btn").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("podcasts_lazy_column")
            .performScrollToNode(hasTestTag("podcast_scholar_ur_dr_israr_ahmed"))
        composeRule.onNodeWithTag("podcast_scholar_ur_dr_israr_ahmed").assertIsDisplayed()
    }

    // =========================================================================
    // 8. 3,000+ CUMULATIVE LONG-FORM ARCHIVE (400+ PER CATEGORY) & 30-MIN AUTO-REFRESH
    // =========================================================================

    @Test
    fun cumulativeArchive_accumulatesOver3000LongFormPodcastsAnd400PerCategoryWithZeroShorts() = runBlocking {
        val urduChannels = PodcastCatalogBridge.channelsForLanguage(appContext, PodcastLanguage.URDU)
        val englishChannels = PodcastCatalogBridge.channelsForLanguage(appContext, PodcastLanguage.ENGLISH)
        val arabicChannels = PodcastCatalogBridge.channelsForLanguage(appContext, PodcastLanguage.ARABIC)
        val langByCid = HashMap<String, Pair<PodcastLanguage, Int>>()
        urduChannels.forEachIndexed { idx, ch -> langByCid[ch.channelId] = PodcastLanguage.URDU to idx }
        englishChannels.forEachIndexed { idx, ch -> langByCid[ch.channelId] = PodcastLanguage.ENGLISH to idx }
        arabicChannels.forEachIndexed { idx, ch -> langByCid[ch.channelId] = PodcastLanguage.ARABIC to idx }

        PodcastLibraryRepository.httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val cid = chain.request().url.toString().substringAfter("channel_id=").substringBefore("&")
                val (lang, idx) = langByCid[cid] ?: (PodcastLanguage.ENGLISH to 0)
                // 10 real long-form videos + 2 Shorts per channel across all 349 channels -> 3,490 real long-form videos!
                val entries = (1..10).map { n ->
                    val prefix = when (lang) {
                        PodcastLanguage.URDU -> "u"
                        PodcastLanguage.ENGLISH -> "e"
                        PodcastLanguage.ARABIC -> "a"
                    }
                    val vid = String.format(java.util.Locale.US, "%s%03dLg%05d", prefix, idx, n)
                    val title = when (lang) {
                        PodcastLanguage.URDU -> "قرآن کی مکمل تفسیر اور سیرت بیان قسط $n - چینل $idx"
                        PodcastLanguage.ENGLISH -> "Complete Podcast Episode on Quran Tafseer and Islamic History Part $n"
                        PodcastLanguage.ARABIC -> "تفسير القرآن والسيرة النبوية محاضرة كاملة الحلقة $n"
                    }
                    AtomEntrySpec(videoId = vid, title = title, durationSeconds = 2400 + n * 60, views = 10_000L * n)
                } + listOf(
                    AtomEntrySpec(
                        videoId = String.format(java.util.Locale.US, "s%03dSh00001", idx),
                        title = "Quick Clip #shorts",
                        url = "https://www.youtube.com/shorts/${String.format(java.util.Locale.US, "s%03dSh00001", idx)}",
                        durationSeconds = 45,
                        width = 1080,
                        height = 1920
                    )
                )
                Response.Builder()
                    .request(chain.request()).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(buildAtomFeed(cid, "Verified Channel $idx", entries).toResponseBody("application/atom+xml".toMediaType()))
                    .build()
            })
            .build()

        val totalAccumulated = PodcastLibraryRepository.refreshAllLanguagesCumulative(
            context = appContext,
            sweepAllBatches = true
        )
        assertTrue("Expected 3,000+ real long-form podcasts across 349 channels, got $totalAccumulated", totalAccumulated >= 3000)

        // Check Urdu archive independently has 400+ videos in Quran & Tafseer category and ZERO Shorts
        val urduRes = PodcastLibraryRepository.loadLanguageSuggestions(appContext, PodcastLanguage.URDU, forceRefresh = false)
        assertTrue("Expected 1,000+ Urdu long-form videos, got ${urduRes.verifiedVideoCount}", urduRes.verifiedVideoCount >= 1000)
        val quranCategoryCount = urduRes.videosForCategory(PodcastProfileCategory.QURAN_TAFSEER).size
        assertTrue("Expected 400+ verified videos in Quran & Tafseer category, got $quranCategoryCount", quranCategoryCount >= 400)
        assertTrue("Zero Shorts may ever leak into archive", urduRes.videos.none { it.videoUrl.contains("/shorts/") })
    }

    @Test
    fun lectureCatalogRefresh_accumulatesOver3000LecturesAndMergesNewUploadsEvery30Minutes() = runBlocking {
        val originalYtClient = YouTubeRepository.httpClient
        val originalYtNet = YouTubeRepository.networkChecker
        val originalYtTime = YouTubeRepository.currentTimeProvider
        try {
            YouTubeRepository.clearTestCaches()
            YouTubeRepository.networkChecker = { true }
            var fakeNow = 1_763_600_000_000L
            YouTubeRepository.currentTimeProvider = { fakeNow }

            val allChannels = YouTubeRepository.getVerifiedChannels(appContext)
            val idxByCid = allChannels.mapIndexed { i, c -> c.channelId to i }.toMap()
            val wave = AtomicInteger(1)

            YouTubeRepository.httpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val cid = chain.request().url.toString().substringAfter("channel_id=").substringBefore("&")
                    val idx = idxByCid[cid] ?: 0
                    val w = wave.get()
                    val baseEntries = (1..10).map { n ->
                        AtomEntrySpec(
                            videoId = String.format(java.util.Locale.US, "l%03dW1n%04d", idx, n),
                            title = "Complete Quran Tafseer and Seerah Lecture Part $n Channel $idx",
                            durationSeconds = 2700,
                            publishedIso = "2026-11-18T10:00:00Z"
                        )
                    }
                    val entries = if (w == 2) {
                        listOf(
                            AtomEntrySpec(
                                videoId = String.format(java.util.Locale.US, "l%03dW2n0001", idx),
                                title = "Brand New Quran Tafseer Session After 30 Minutes Channel $idx",
                                durationSeconds = 3000,
                                publishedIso = "2026-11-19T12:00:00Z"
                            )
                        ) + baseEntries
                    } else {
                        baseEntries
                    }
                    Response.Builder()
                        .request(chain.request()).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                        .body(buildAtomFeed(cid, "Verified Scholar $idx", entries).toResponseBody("application/atom+xml".toMediaType()))
                        .build()
                })
                .build()

            // Wave 1: Full sweep of all 325 verified lecture channels -> 3,250 real long-form lectures!
            val summary1 = YouTubeRepository.refreshCumulativeLectureCatalog(
                context = appContext,
                forceAllChannels = true
            )
            assertTrue("Expected 3,000+ real lectures, got ${summary1.totalAccumulatedLectures}", summary1.totalAccumulatedLectures >= 3000)

            // Advance 31 minutes -> Wave 2 publishes new lectures; 30-min batch sweep merges new lectures without losing old ones!
            fakeNow += 31L * 60L * 1000L
            wave.set(2)
            val summary2 = YouTubeRepository.refreshCumulativeLectureCatalog(
                context = appContext,
                batchSize = 20,
                forceAllChannels = false
            )
            assertTrue("Expected newly uploaded lectures to be merged automatically (+${summary2.newVideosAdded})", summary2.newVideosAdded >= 15)
            assertTrue(summary2.totalAccumulatedLectures > summary1.totalAccumulatedLectures)
        } finally {
            YouTubeRepository.httpClient = originalYtClient
            YouTubeRepository.networkChecker = originalYtNet
            YouTubeRepository.currentTimeProvider = originalYtTime
            YouTubeRepository.clearTestCaches()
        }
    }
}

