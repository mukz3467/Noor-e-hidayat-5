package com.example

import android.app.Application
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.podcast.PodcastCategory
import com.example.data.podcast.PodcastDownloadStatus
import com.example.data.podcast.PodcastEpisode
import com.example.data.podcast.PodcastPlayerManager
import com.example.data.podcast.PodcastRepository
import com.example.data.podcast.PodcastShow
import com.example.data.podcast.PodcastVerificationStatus
import com.example.data.repository.FeedLoadStatus
import com.example.data.repository.PodcastLanguageSection
import com.example.data.repository.PodcastTopicFilter
import com.example.data.repository.YouTubeRepository
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.PodcastsScreen
import com.example.ui.theme.NooreHidayatTheme
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
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
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PodcastPlatformAndCleanupTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application
    private val originalYtClient = YouTubeRepository.httpClient

    private val dummyShow = PodcastShow(
        id = "test_show",
        showName = "Test Islamic Podcast",
        publisher = "Test Institute",
        host = "Test Scholar",
        officialRssFeedUrl = "https://feeds.example.org/islamic.xml",
        itunesLookupTerm = "Test Islamic",
        expectedKeywords = listOf("test"),
        artworkUrl = "https://cdn.example.org/cover.jpg",
        languageCode = "en",
        primaryCategories = listOf(
            PodcastCategory.QURAN_REFLECTIONS,
            PodcastCategory.ENGLISH_PODCASTS
        ),
        sourceAttribution = "Test Institute Open RSS",
        verificationStatus = PodcastVerificationStatus.VERIFIED_RSS_FEED,
        downloadPermittedByPublisher = true,
        publisherLicenseNote = "Open RSS feed"
    )

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences("noor_podcast_repo_prefs_v1", Context.MODE_PRIVATE)
            .edit().clear().commit()
        appContext.getSharedPreferences("noor_youtube_feeds_v2", Context.MODE_PRIVATE)
            .edit().clear().commit()
        File(appContext.filesDir, "podcasts").deleteRecursively()
        PodcastRepository.httpClientOverrideForTest = null
        PodcastRepository.isOnlineOverrideForTest = null
        YouTubeRepository.httpClient = originalYtClient
        YouTubeRepository.networkChecker = { true }
        YouTubeRepository.clearTestCaches()
        PodcastPlayerManager.setPlaybackSpeed(1.0f)
    }

    @After
    fun tearDown() {
        PodcastRepository.httpClientOverrideForTest = null
        PodcastRepository.isOnlineOverrideForTest = null
        YouTubeRepository.httpClient = originalYtClient
        YouTubeRepository.networkChecker = null
        YouTubeRepository.clearTestCaches()
        File(appContext.filesDir, "podcasts").deleteRecursively()
    }

    private fun sampleYouTubeAtomFeed(
        channelId: String,
        authorName: String,
        entries: List<Triple<String, String, String>>
    ): String {
        val entriesXml = entries.joinToString("\n") { (vid, title, published) ->
            "<entry>\n" +
                "  <id>yt:video:$vid</id>\n" +
                "  <yt:videoId>$vid</yt:videoId>\n" +
                "  <yt:channelId>$channelId</yt:channelId>\n" +
                "  <title>$title</title>\n" +
                "  <link rel=\"alternate\" href=\"https://www.youtube.com/watch?v=$vid\"/>\n" +
                "  <author><name>$authorName</name></author>\n" +
                "  <published>$published</published>\n" +
                "</entry>"
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<feed xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\" xmlns=\"http://www.w3.org/2005/Atom\">\n" +
            "  <title>$authorName</title>\n" +
            entriesXml + "\n" +
            "</feed>"
    }

    @Test
    fun verifyPodcastLanguageFromContent_separatesUrduArabicEnglishAndUnverifiedHonestly() {
        assertEquals(
            PodcastLanguageSection.URDU,
            YouTubeRepository.verifyPodcastLanguageFromContent("قرآن کی تفسیر اور نماز کا بیان")
        )
        assertEquals(
            PodcastLanguageSection.URDU,
            YouTubeRepository.verifyPodcastLanguageFromContent("Allah ki Madad Kiu nahi aa rhi | Molana Tariq Jamil")
        )
        assertEquals(
            PodcastLanguageSection.ARABIC,
            YouTubeRepository.verifyPodcastLanguageFromContent("كيف تخاف وربك الله؟ - مصطفى حسني")
        )
        assertEquals(
            PodcastLanguageSection.ENGLISH,
            YouTubeRepository.verifyPodcastLanguageFromContent("Muslim Inside | Ep. 18 | Role of Hadith | Raja Zia ul Haq")
        )
        assertEquals(
            PodcastLanguageSection.LANGUAGE_NOT_VERIFIED,
            YouTubeRepository.verifyPodcastLanguageFromContent("28 2026")
        )
        assertEquals(
            PodcastLanguageSection.LANGUAGE_NOT_VERIFIED,
            YouTubeRepository.verifyPodcastLanguageFromContent("#12")
        )
    }

    @Test
    fun fetchPodcastsForSelection_neverMixesLanguagesAndPlacesEnglishTitlesOutOfUrduFeed() = runBlocking {
        val requestedUrls = CopyOnWriteArrayList<String>()
        val youthClubId = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "youth_club" }.channelId
        val tariqMasoodId = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "tariq_masood" }.channelId

        YouTubeRepository.httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val url = chain.request().url.toString()
                requestedUrls.add(url)
                val xml = when {
                    url.contains(youthClubId) -> sampleYouTubeAtomFeed(
                        youthClubId,
                        "Youth Club",
                        listOf(
                            Triple("engVid11111", "The Miracle of Quran and Youth Faith", "2026-11-15T10:00:00Z"),
                            Triple("urdVid22222", "Naujawanon Ki Zindagi Aur Deen Ki Ahmiyat Kya Hai", "2026-11-14T10:00:00Z"),
                            Triple("ambVid33333", "Ep #07", "2026-11-13T10:00:00Z")
                        )
                    )
                    url.contains(tariqMasoodId) -> sampleYouTubeAtomFeed(
                        tariqMasoodId,
                        "Mufti Tariq Masood",
                        listOf(Triple("urdVid44444", "Aaj Ka Bayan: Sabar Aur Namaz Ki Hifazat", "2026-11-16T10:00:00Z"))
                    )
                    else -> sampleYouTubeAtomFeed("UCempty0000000000000000", "Other", emptyList())
                }
                Response.Builder()
                    .request(chain.request()).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
            })
            .build()

        // Selecting URDU fetches ONLY Urdu channels (not Arabic or English channels)
        val urduResult = YouTubeRepository.fetchPodcastsForSelection(
            context = appContext,
            languageSection = PodcastLanguageSection.URDU,
            topicFilter = PodcastTopicFilter.ALL_TOPICS,
            forceRefresh = true
        )
        assertEquals(FeedLoadStatus.ONLINE_FRESH, urduResult.status)
        val arabicIds = YouTubeRepository.podcastChannelsForSelection(PodcastLanguageSection.ARABIC).map { it.channelId }
        assertTrue("Urdu fetch must NOT call Arabic channels", requestedUrls.none { u -> arabicIds.any { u.contains(it) } })
        // English video from Youth Club must NOT appear in Urdu Podcasts
        assertFalse(urduResult.videos.any { it.id == "engVid11111" })
        assertFalse(urduResult.videos.any { it.id == "ambVid33333" })
        assertTrue(urduResult.videos.any { it.id == "urdVid22222" })
        assertTrue(urduResult.videos.any { it.id == "urdVid44444" })

        // Unverifiable video "Ep #07" goes into LANGUAGE_NOT_VERIFIED
        val unverifiedRes = YouTubeRepository.fetchPodcastsForSelection(
            context = appContext,
            languageSection = PodcastLanguageSection.LANGUAGE_NOT_VERIFIED,
            topicFilter = PodcastTopicFilter.ALL_TOPICS,
            forceRefresh = false
        )
        assertTrue(unverifiedRes.videos.any { it.id == "ambVid33333" })
    }

    @Test
    fun podcastsScreen_doesNotMixFeedsOnOpenAndSeparatesUrduArabicEnglish() {
        val requestedUrls = CopyOnWriteArrayList<String>()
        val othmanId = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "othman_alkamees" }.channelId

        YouTubeRepository.httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val url = chain.request().url.toString()
                requestedUrls.add(url)
                val xml = if (url.contains(othmanId)) {
                    sampleYouTubeAtomFeed(
                        othmanId,
                        "Dr. Othman Alkamees",
                        listOf(Triple("arVid111111", "صحيح مسلم المجلس 41 - الشيخ د. عثمان الخميس", "2026-11-15T10:00:00Z"))
                    )
                } else {
                    sampleYouTubeAtomFeed("UCother0000000000000000", "Other", emptyList())
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

        // Must NOT fetch anything before user picks a language
        assertTrue("No channel feed should load before language selection", requestedUrls.isEmpty())
        composeRule.onNodeWithTag("podcast_search_input").assertIsDisplayed()
        composeRule.onNodeWithTag("podcast_select_language_prompt").assertIsDisplayed()
        composeRule.onNodeWithTag("pod_lang_${PodcastLanguageSection.URDU.id}").assertIsDisplayed()
        composeRule.onNodeWithTag("pod_lang_${PodcastLanguageSection.ARABIC.id}").assertIsDisplayed()
        composeRule.onNodeWithTag("pod_lang_${PodcastLanguageSection.ENGLISH.id}").assertIsDisplayed()
        composeRule.onNodeWithTag("pod_lang_${PodcastLanguageSection.LANGUAGE_NOT_VERIFIED.id}").assertIsDisplayed()

        // Tap Arabic Podcasts -> fetches ONLY Arabic channels and displays Arabic episode
        composeRule.onNodeWithTag("pod_lang_${PodcastLanguageSection.ARABIC.id}").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000L) {
            composeRule.onAllNodesWithText("صحيح مسلم المجلس 41 - الشيخ د. عثمان الخميس")
                .fetchSemanticsNodes().isNotEmpty()
        }
        assertTrue(requestedUrls.isNotEmpty())
        composeRule.onNodeWithText("صحيح مسلم المجلس 41 - الشيخ د. عثمان الخميس")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun verifiedShowCatalog_containsNoPlaceholderOrExampleUrls() {
        assertTrue(PodcastRepository.VERIFIED_SHOWS.isNotEmpty())
        PodcastRepository.VERIFIED_SHOWS.forEach { show ->
            assertFalse("Show ${show.id} must not use example.com", show.officialRssFeedUrl.contains("example.com"))
            assertTrue("Show ${show.id} must have source attribution", show.sourceAttribution.isNotBlank())
            assertTrue("Show ${show.id} must declare categories", show.primaryCategories.isNotEmpty())
        }
        assertFalse(PodcastRepository.isValidAudioEnclosureUrl("https://example.com/audio.mp3", "audio/mpeg"))
        assertFalse(PodcastRepository.isValidAudioEnclosureUrl("http://localhost/audio.mp3", "audio/mpeg"))
        assertTrue(PodcastRepository.isValidAudioEnclosureUrl("https://cdn.muslimcentral.com/audio/ep1.mp3", "audio/mpeg"))
    }

    @Test
    fun parseRssFeedXml_parsesValidItemsStripsHtmlAndRejectsFakeEnclosures() {
        val sampleRss = """
            <?xml version="1.0" encoding="UTF-8"?>
            <rss version="2.0" xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd">
              <channel>
                <title>Yaqeen Reflections</title>
                <itunes:author>Dr. Omar Suleiman</itunes:author>
                <itunes:image href="https://media.yaqeeninstitute.org/art.png"/>
                <item>
                  <guid>ep-valid-1</guid>
                  <title>Heart of the &lt;b&gt;Believer&lt;/b&gt;</title>
                  <description>&lt;p&gt;Reflect on Surah Al-Asr and patience.&lt;/p&gt;</description>
                  <pubDate>Mon, 01 Jul 2025 10:00:00 +0000</pubDate>
                  <itunes:duration>18:45</itunes:duration>
                  <enclosure url="https://media.yaqeeninstitute.org/ep1.mp3" type="audio/mpeg" length="4500000"/>
                </item>
                <item>
                  <guid>ep-fake-domain</guid>
                  <title>Fake Episode</title>
                  <enclosure url="https://example.com/fake.mp3" type="audio/mpeg" length="1024"/>
                </item>
              </channel>
            </rss>
        """.trimIndent()

        val parsed = PodcastRepository.parseRssFeedXml(sampleRss, dummyShow)
        assertEquals("Yaqeen Reflections", parsed.updatedShow.showName)
        assertEquals("Dr. Omar Suleiman", parsed.updatedShow.host)
        assertEquals(1, parsed.episodes.size)
        val ep = parsed.episodes.first()
        assertEquals("ep-valid-1", ep.guid)
        assertEquals("Heart of the Believer", ep.title)
        assertTrue(ep.description.contains("Reflect on Surah Al-Asr"))
        assertFalse(ep.description.contains("<p>"))
        assertEquals(1125L, ep.durationSeconds)
        assertEquals("18:45", ep.durationDisplay)
    }

    @Test
    fun parseRssFeedXml_handlesMalformedXmlSafelyWithoutCrashing() {
        val malformed = "<rss><channel><item><title>Broken"
        val parsed = PodcastRepository.parseRssFeedXml(malformed, dummyShow)
        assertTrue(parsed.episodes.isEmpty())
    }

    @Test
    fun availableCategoriesWithCounts_neverExposesEmptyCategories() {
        val ep1 = PodcastEpisode(
            guid = "g1",
            showId = "s1",
            showName = "Show",
            publisher = "Pub",
            host = "Host",
            title = "Seerah Lecture",
            description = "Prophetic biography",
            pubDateRaw = "",
            pubDateDisplay = "01 Jul 2025",
            publishedEpochMs = 1L,
            audioEnclosureUrl = "https://cdn.islamic.org/seerah.mp3",
            mimeType = "audio/mpeg",
            fileLengthBytes = 10000L,
            durationSeconds = 600L,
            durationDisplay = "10:00",
            artworkUrl = "",
            languageCode = "en",
            categories = listOf(PodcastCategory.SEERAH, PodcastCategory.ENGLISH_PODCASTS),
            sourceAttribution = "Verified",
            downloadPermittedByPublisher = true
        )
        val nonEmpty = PodcastRepository.availableCategoriesWithCounts(listOf(ep1))
        val cats = nonEmpty.map { it.first }
        assertTrue(cats.contains(PodcastCategory.SEERAH))
        assertTrue(cats.contains(PodcastCategory.ENGLISH_PODCASTS))
        assertFalse("Empty URDU_PODCASTS category must be omitted", cats.contains(PodcastCategory.URDU_PODCASTS))
        assertFalse("Empty HADITH_SUNNAH category must be omitted", cats.contains(PodcastCategory.HADITH_SUNNAH))
    }

    @Test
    fun downloadEpisodeSuspend_writesValidMp3ToDiskUpdatesStateAndDeletesCleanly() = runTest {
        val fakeMp3Bytes = ByteArray(4096) { idx ->
            when (idx) {
                0 -> 'I'.code.toByte()
                1 -> 'D'.code.toByte()
                2 -> '3'.code.toByte()
                else -> (idx % 120).toByte()
            }
        }
        PodcastRepository.httpClientOverrideForTest = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val url = chain.request().url.toString()
                if (url.contains("html-error")) {
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .header("Content-Type", "text/html")
                        .body("<html>404 Not Found</html>".toResponseBody("text/html".toMediaType()))
                        .build()
                } else {
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .header("Content-Type", "audio/mpeg")
                        .body(fakeMp3Bytes.toResponseBody("audio/mpeg".toMediaType()))
                        .build()
                }
            })
            .build()

        val validEp = PodcastEpisode(
            guid = "dl-ep-1",
            showId = "s1",
            showName = "Show",
            publisher = "Pub",
            host = "Host",
            title = "Real MP3 Episode",
            description = "Description",
            pubDateRaw = "",
            pubDateDisplay = "01 Jul 2025",
            publishedEpochMs = 100L,
            audioEnclosureUrl = "https://cdn.islamic.org/real-audio.mp3",
            mimeType = "audio/mpeg",
            fileLengthBytes = fakeMp3Bytes.size.toLong(),
            durationSeconds = 120L,
            durationDisplay = "02:00",
            artworkUrl = "",
            languageCode = "en",
            categories = listOf(PodcastCategory.ISLAMIC_KNOWLEDGE),
            sourceAttribution = "Verified",
            downloadPermittedByPublisher = true
        )

        val result = PodcastRepository.downloadEpisodeSuspend(appContext, validEp)
        assertTrue(result.isSuccess)
        val savedFile = result.getOrNull()
        assertNotNull(savedFile)
        assertTrue(savedFile!!.exists())
        assertEquals(fakeMp3Bytes.size.toLong(), savedFile.length())
        assertTrue(PodcastRepository.isEpisodeDownloadedOffline(appContext, validEp))
        assertEquals(PodcastDownloadStatus.DOWNLOADED, PodcastRepository.downloadStates.value["dl-ep-1"]?.status)

        val badEp = validEp.copy(guid = "bad-ep", audioEnclosureUrl = "https://cdn.islamic.org/html-error.mp3")
        val badResult = PodcastRepository.downloadEpisodeSuspend(appContext, badEp)
        assertTrue(badResult.isFailure)

        val deleted = PodcastRepository.deleteDownloadedEpisode(appContext, validEp)
        assertTrue(deleted)
        assertFalse(savedFile.exists())
        assertNull(PodcastRepository.getLocalAudioFile(appContext, validEp))
    }

    @Test
    fun playerManager_supportsAllRequestedSpeedsAndPositionPersistence() {
        assertEquals(listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f), PodcastPlayerManager.SUPPORTED_SPEEDS)
        assertEquals(1.25f, PodcastPlayerManager.cyclePlaybackSpeed(), 0.001f)
        assertEquals(1.5f, PodcastPlayerManager.cyclePlaybackSpeed(), 0.001f)
        assertEquals(2.0f, PodcastPlayerManager.cyclePlaybackSpeed(), 0.001f)
        assertEquals(0.75f, PodcastPlayerManager.cyclePlaybackSpeed(), 0.001f)

        PodcastRepository.savePlaybackPositionMs(appContext, "ep-resume-1", 94_500L)
        assertEquals(94_500L, PodcastRepository.getSavedPlaybackPositionMs(appContext, "ep-resume-1"))
    }

    @Test
    fun moreHubScreen_hasNoDownloadsTile() {
        composeRule.setContent {
            NooreHidayatTheme {
                MoreHubScreen(language = AppLanguage.ENGLISH, onBack = {}, onNavigate = {})
            }
        }
        composeRule.waitForIdle()
        assertEquals(0, composeRule.onAllNodesWithTag("hub_item_downloads").fetchSemanticsNodes().size)
    }
}
