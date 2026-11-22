package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import com.example.data.lectures.ChannelCatalogLoader
import com.example.data.lectures.ChannelFeedStatus
import com.example.data.lectures.ChannelVerificationStatus
import com.example.data.lectures.DurationFilterPolicy
import com.example.data.lectures.DurationVerificationStatus
import com.example.data.lectures.LectureDurationConfig
import com.example.data.lectures.LectureLanguageGroup
import com.example.data.lectures.YouTubeRssParser
import com.example.data.model.AppLanguage
import com.example.data.repository.FeedLoadStatus
import com.example.data.repository.LectureCategory
import com.example.data.repository.YouTubeRepository
import com.example.localization.AppLocalization
import com.example.ui.screens.LecturesScreen
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
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CopyOnWriteArrayList

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class VerifiedLecturesArchitectureTest {

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

    @Test
    fun testChannelCatalogSeedSourcesVerificationSeparationAndDuplicateGuards() {
        val catalog = ChannelCatalogLoader.load(context, forceReload = true)
        assertEquals(3, catalog.catalogVersion)
        assertTrue("Verified entries must exceed 300 live-verified channels", catalog.verifiedEntries.size >= 300)
        assertEquals("Verified entries must contain all 325 live-verified channels", 325, catalog.verifiedEntries.size)
        assertEquals(325, YouTubeRepository.VERIFIED_CHANNELS.size)
        assertEquals(108, catalog.coverageReport.urduHindiVerified)
        assertEquals(109, catalog.coverageReport.englishVerified)
        assertEquals(108, catalog.coverageReport.arabicVerified)

        // Verify The Islamic History & dedicated Islamic History RSS channels are wired first in ISLAMIC_HISTORY
        val theIslamicHistory = catalog.verifiedEntries.first { it.channelId == "the_islamic_history" }
        assertEquals("UCx8DxncMGxZ_4AQRit0P6bw", theIslamicHistory.youtubeChannelId)
        assertEquals("https://www.youtube.com/feeds/videos.xml?channel_id=UCx8DxncMGxZ_4AQRit0P6bw", theIslamicHistory.rssUrl)
        val historyChannels = YouTubeRepository.channelsForTopic(LectureCategory.ISLAMIC_HISTORY, context = context)!!
        assertEquals("the_islamic_history", historyChannels.first().key)
        assertTrue(historyChannels.any { it.key == "islamic_history_podcast" })
        assertTrue(historyChannels.any { it.key == "omar_of_the_orient" })
        assertTrue(historyChannels.any { it.key == "al_muqaddimah" })
        assertTrue(historyChannels.any { it.key == "jihad_alturbani_100_great" })
        assertTrue(historyChannels.any { it.key == "early_history_of_islam_ar" })

        // Active Dr. Israr Ahmed & Tanzeem-e-Islami official channels (replacing 404 legacy ID)
        val tanzeem = catalog.verifiedEntries.first { it.channelId == "tanzeem_islami" }
        assertEquals("UCwGzYwcG_N8rvrdS2EBQA-g", tanzeem.youtubeChannelId)
        assertEquals("https://www.youtube.com/feeds/videos.xml?channel_id=UCwGzYwcG_N8rvrdS2EBQA-g", tanzeem.rssUrl)
        assertEquals(LectureLanguageGroup.URDU_HINDI, tanzeem.primaryLanguage)

        val zamana = catalog.verifiedEntries.first { it.channelId == "zamana_gawah_hai" }
        assertEquals("UCX-oUdfYIKNwJt6gwVtIy9w", zamana.youtubeChannelId)

        val israr = catalog.verifiedEntries.first { it.channelId == "dr_israr_ahmed" }
        assertEquals("UCBj6SU_o5wZQJbZJZSkVzOw", israr.youtubeChannelId)
        assertEquals("https://www.youtube.com/feeds/videos.xml?channel_id=UCBj6SU_o5wZQJbZJZSkVzOw", israr.rssUrl)
        assertEquals(LectureLanguageGroup.URDU_HINDI, israr.primaryLanguage)

        // Terminated 404 legacy ID must NEVER enter verifiedEntries
        assertTrue(catalog.verifiedEntries.none { it.youtubeChannelId == "UCzpEKv_HM4vr7hJWzlcz80A" })
        assertTrue(catalog.coverageReport.invalidRssCount >= 1)

        // Dr. Zakir Naik Official & Peace TV Official
        val zakir = catalog.verifiedEntries.first { it.channelId == "dr_zakir_naik" }
        assertEquals("UC3YmP7nqf514I1zh1eVbzrA", zakir.youtubeChannelId)
        assertEquals("https://www.youtube.com/feeds/videos.xml?channel_id=UC3YmP7nqf514I1zh1eVbzrA", zakir.rssUrl)
        assertEquals("https://zakirnaik.com/", zakir.officialWebsite)

        val peaceTv = catalog.verifiedEntries.first { it.channelId == "peace_tv_official" }
        assertEquals("UCz8v3iym8sZeJ-kZZwxUcfQ", peaceTv.youtubeChannelId)

        // Additional requested recognized scholars across Urdu/Hindi, English, Arabic
        assertEquals("UCLfMxJctTnF5BidnWSHKsNQ", catalog.verifiedEntries.first { it.channelId == "mufti_taqi_usmani" }.youtubeChannelId)
        assertEquals("UCFnsCaT_2VKBg_SoGjhO1nA", catalog.verifiedEntries.first { it.channelId == "farhat_hashmi" }.youtubeChannelId)
        assertEquals("UCThbMTLFbeDGrZlb9DnTKDg", catalog.verifiedEntries.first { it.channelId == "engineer_ali_mirza" }.youtubeChannelId)
        assertEquals("UClUa7-iHJNKEM2e_zWYSPQg", catalog.verifiedEntries.first { it.channelId == "yasir_qadhi" }.youtubeChannelId)
        assertEquals("UC3vHW2h22WE-pNi5WJtRIjg", catalog.verifiedEntries.first { it.channelId == "yaqeen" }.youtubeChannelId)
        assertEquals("UCRtiU-lpcBSi-ipFKyfIkug", catalog.verifiedEntries.first { it.channelId == "bayyinah" }.youtubeChannelId)
        assertEquals("UCNB_OaI4524fASt8h0IL8dw", catalog.verifiedEntries.first { it.channelId == "mufti_menk" }.youtubeChannelId)
        assertEquals("UCWjCSGhmSGu0VLf2mPFS0Kg", catalog.verifiedEntries.first { it.channelId == "othman_alkamees" }.youtubeChannelId)

        // Every verified channel has unique valid UC ID and non-empty multilingual names
        assertEquals(catalog.verifiedEntries.size, catalog.verifiedEntries.map { it.youtubeChannelId }.distinct().size)
        assertTrue(catalog.verifiedEntries.all { YouTubeRepository.isValidChannelId(it.youtubeChannelId) })

        // Candidates must remain separate and NEVER enter verifiedEntries or generate RSS from handles
        assertTrue("Catalog must track unverified candidates separately", catalog.coverageReport.candidateCount > 0)
        assertTrue(catalog.verifiedEntries.all { it.verificationStatus == ChannelVerificationStatus.VERIFIED && it.enabled })
        val candidates = catalog.allEntries.filter { it.verificationStatus == ChannelVerificationStatus.CANDIDATE }
        assertTrue(candidates.all { !it.enabled && it.rssUrl.isBlank() })

        // Honest target coverage reporting (100 Urdu/Hindi + 100 English + 100 Arabic = 300)
        val report = catalog.coverageReport
        assertEquals(100, report.urduHindiTarget)
        assertEquals(100, report.englishTarget)
        assertEquals(100, report.arabicTarget)
        assertEquals(300, report.totalTarget)
        assertTrue("300+ verified channel target is now met across UR/HI, EN, AR (325 verified)", report.isTargetMet)

        // Duplicate & invalid UC ID guards in JSON parser
        val malformedCatalogJson = """
            {
              "catalogVersion": 2,
              "channels": [
                {
                  "channelId": "dup_1",
                  "channelName": "Valid One",
                  "youtubeChannelId": "UC3YmP7nqf514I1zh1eVbzrA",
                  "rssUrl": "https://www.youtube.com/feeds/videos.xml?channel_id=UC3YmP7nqf514I1zh1eVbzrA",
                  "primaryLanguage": "en",
                  "verificationStatus": "VERIFIED",
                  "enabled": true
                },
                {
                  "channelId": "dup_2",
                  "channelName": "Duplicate Same UC",
                  "youtubeChannelId": "UC3YmP7nqf514I1zh1eVbzrA",
                  "primaryLanguage": "en",
                  "verificationStatus": "VERIFIED",
                  "enabled": true
                },
                {
                  "channelId": "bad_handle_as_id",
                  "channelName": "Bad Handle Not UC",
                  "youtubeChannelId": "@SomeHandleNotUC",
                  "primaryLanguage": "en",
                  "verificationStatus": "VERIFIED",
                  "enabled": true
                }
              ]
            }
        """.trimIndent()
        val parsed = ChannelCatalogLoader.parseCatalogJson(malformedCatalogJson)
        assertEquals("Only the single valid non-duplicate channel may be verified", 1, parsed.verifiedEntries.size)
        assertEquals("dup_1", parsed.verifiedEntries[0].channelId)
        assertEquals(1, parsed.coverageReport.invalidRssCount)
    }

    @Test
    fun testLongFormDurationPolicyNeverInventsDurationAndRejectsShortsAndClips() {
        assertEquals(480, LectureDurationConfig.MIN_LECTURE_DURATION_SECONDS)

        // Under 60s -> reject
        assertEquals(
            DurationVerificationStatus.CONFIRMED_SHORT_REJECTED,
            LectureDurationConfig.evaluateDuration(durationSeconds = 45, isShortsUrl = false)
        )
        // 60s to 3 min -> reject
        assertEquals(
            DurationVerificationStatus.CONFIRMED_SHORT_REJECTED,
            LectureDurationConfig.evaluateDuration(durationSeconds = 150, isShortsUrl = false)
        )
        // 3 min to under 8 min -> reject by default
        assertEquals(
            DurationVerificationStatus.CONFIRMED_SHORT_REJECTED,
            LectureDurationConfig.evaluateDuration(durationSeconds = 479, isShortsUrl = false)
        )
        // 8 min or longer -> eligible
        assertEquals(
            DurationVerificationStatus.CONFIRMED_LONG_FORM,
            LectureDurationConfig.evaluateDuration(durationSeconds = 480, isShortsUrl = false)
        )
        // Raised minimum duration (15 min = 900s) -> 10 min video rejected
        assertEquals(
            DurationVerificationStatus.CONFIRMED_SHORT_REJECTED,
            LectureDurationConfig.evaluateDuration(durationSeconds = 600, isShortsUrl = false, minDurationSeconds = 900)
        )
        // Shorts URL -> always rejected even when duration unknown
        assertEquals(
            DurationVerificationStatus.CONFIRMED_SHORT_REJECTED,
            LectureDurationConfig.evaluateDuration(durationSeconds = null, isShortsUrl = true)
        )
        // Missing duration -> UNVERIFIED_FROM_RSS (never labeled confirmed long-form!)
        val unverified = LectureDurationConfig.evaluateDuration(durationSeconds = null, isShortsUrl = false)
        assertEquals(DurationVerificationStatus.UNVERIFIED_FROM_RSS, unverified)
        assertFalse(
            "Confirmed-only policy must not display unverified duration videos",
            LectureDurationConfig.isEligibleForDisplay(unverified, DurationFilterPolicy.CONFIRMED_LONG_FORM_ONLY)
        )
        assertTrue(
            LectureDurationConfig.isEligibleForDisplay(
                unverified,
                DurationFilterPolicy.EXCLUDE_CONFIRMED_SHORTS_SHOW_UNVERIFIED_WITH_BADGE
            )
        )
    }

    @Test
    fun testNamespaceAwareAtomParserMetadataDeletedRejectionAndPlayerDurationCache() {
        val israr = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "dr_israr_ahmed" }
        val richXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <feed xmlns:yt="http://www.youtube.com/xml/schemas/2015"
                  xmlns:media="http://search.yahoo.com/mrss/"
                  xmlns="http://www.w3.org/2005/Atom">
              <yt:channelId>UCBj6SU_o5wZQJbZJZSkVzOw</yt:channelId>
              <title>Dr Israr Ahmed Official</title>
              <entry>
                <yt:videoId>longVid1234</yt:videoId>
                <yt:channelId>UCBj6SU_o5wZQJbZJZSkVzOw</yt:channelId>
                <title>بیان القرآن - سورہ بقرہ مکمل درس</title>
                <link rel="alternate" href="https://www.youtube.com/watch?v=longVid1234"/>
                <author><name>Dr Israr Ahmed Official</name></author>
                <published>2026-10-05T12:00:00+00:00</published>
                <updated>2026-10-06T08:00:00+00:00</updated>
                <media:group>
                  <media:thumbnail url="https://i.ytimg.com/vi/longVid1234/hqdefault.jpg"/>
                  <media:content duration="2700"/>
                  <media:description>تفسیر قرآن اور خلافت راشدہ پر تفصیلی لیکچر</media:description>
                  <media:community><media:statistics views="125000"/></media:community>
                </media:group>
              </entry>
              <entry>
                <yt:videoId>shortClip11</yt:videoId>
                <yt:channelId>UCBj6SU_o5wZQJbZJZSkVzOw</yt:channelId>
                <title>Short Reminder #shorts</title>
                <link rel="alternate" href="https://www.youtube.com/shorts/shortClip11"/>
                <published>2026-10-04T12:00:00+00:00</published>
              </entry>
              <entry>
                <yt:videoId>noDurVid123</yt:videoId>
                <yt:channelId>UCBj6SU_o5wZQJbZJZSkVzOw</yt:channelId>
                <title>غزوہ بدر اور صلح حدیبیہ</title>
                <link rel="alternate" href="https://www.youtube.com/watch?v=noDurVid123"/>
                <published>2026-10-03T12:00:00+00:00</published>
              </entry>
              <entry>
                <yt:videoId>deleted1234</yt:videoId>
                <title>[Deleted video]</title>
                <published>2026-10-02T12:00:00+00:00</published>
              </entry>
            </feed>
        """.trimIndent()

        val parsed = YouTubeRepository.parseAtomFeedXml(richXml, israr)
        assertEquals("Deleted video must be excluded", 3, parsed.size)

        val longItem = parsed.first { it.id == "longVid1234" }
        assertEquals("UCBj6SU_o5wZQJbZJZSkVzOw", longItem.channelId)
        assertEquals("2026-10-06T08:00:00+00:00", longItem.updatedIso)
        assertEquals(2700, longItem.durationSeconds)
        assertEquals(DurationVerificationStatus.CONFIRMED_LONG_FORM, longItem.durationStatus)
        assertEquals(125000L, longItem.viewCount)
        assertEquals(LectureLanguageGroup.URDU_HINDI, longItem.languageGroup)
        assertTrue(longItem.categories.contains(LectureCategory.QURAN_TAFSEER))
        assertTrue("Description keywords must classify Islamic Caliphates", longItem.categories.contains(LectureCategory.ISLAMIC_CALIPHATES))

        val shortItem = parsed.first { it.id == "shortClip11" }
        assertEquals(DurationVerificationStatus.CONFIRMED_SHORT_REJECTED, shortItem.durationStatus)

        val noDurItem = parsed.first { it.id == "noDurVid123" }
        assertNull("Must NEVER invent duration when omitted by RSS", noDurItem.durationSeconds)
        assertEquals(DurationVerificationStatus.UNVERIFIED_FROM_RSS, noDurItem.durationStatus)
        assertTrue(noDurItem.categories.contains(LectureCategory.ISLAMIC_BATTLES))

        // Once player verifies real duration, local cache updates and promotes to CONFIRMED_LONG_FORM
        YouTubeRepository.recordPlayerVerifiedDuration(context, "noDurVid123", 1840)
        assertEquals(1840, YouTubeRepository.getPlayerVerifiedDuration(context, "noDurVid123"))
        val reparsed = YouTubeRepository.parseAtomFeedXml(richXml, israr)
        assertEquals(DurationVerificationStatus.CONFIRMED_LONG_FORM, reparsed.first { it.id == "noDurVid123" }.durationStatus)
    }

    @Test
    fun testNewTopicClassificationsAndCombinedUrduHindiLanguageFiltering() = runBlocking {
        // Verify multilingual language detection (Devanagari Hindi, Urdu, Roman Urdu, Arabic, English, Unknown)
        assertEquals(LectureLanguageGroup.URDU_HINDI, YouTubeRssParser.detectVideoLanguageGroup("हज़रत यूसुफ़ का क़िस्सा और सबक़"))
        assertEquals(LectureLanguageGroup.URDU_HINDI, YouTubeRssParser.detectVideoLanguageGroup("حضرت عمر بن خطاب کے واقعات"))
        assertEquals(LectureLanguageGroup.URDU_HINDI, YouTubeRssParser.detectVideoLanguageGroup("Ghazwa Badr ka waqia aur sabak"))
        assertEquals(LectureLanguageGroup.ARABIC, YouTubeRssParser.detectVideoLanguageGroup("قصص الأنبياء - قصة موسى عليه السلام"))
        assertEquals(LectureLanguageGroup.ENGLISH, YouTubeRssParser.detectVideoLanguageGroup("Stories of the Prophets: Prophet Ibrahim Full Lecture"))
        assertEquals(LectureLanguageGroup.UNKNOWN, YouTubeRssParser.detectVideoLanguageGroup("12345"))

        // Verify all 4 new categories & exclusion rules
        val prophetCats = YouTubeRssParser.classifyLectureCategories("Prophet Musa and Pharaoh - Stories of the Prophets")
        assertTrue(prophetCats.contains(LectureCategory.STORIES_OF_PROPHETS))

        val battleCats = YouTubeRssParser.classifyLectureCategories("Battle of Badr and Treaty of Hudaybiyyah")
        assertTrue(battleCats.contains(LectureCategory.ISLAMIC_BATTLES))

        val excludedBattle = YouTubeRssParser.classifyLectureCategories("Battle of Badr PUBG gaming clip")
        assertFalse("Exclusion rule must prevent gaming clip from matching Islamic Battles", excludedBattle.contains(LectureCategory.ISLAMIC_BATTLES))

        val sahabaCats = YouTubeRssParser.classifyLectureCategories("Abu Bakr and Umar ibn al-Khattab - Stories of Sahaba")
        assertTrue(sahabaCats.contains(LectureCategory.SAHABA_STORIES))

        val caliphateCats = YouTubeRssParser.classifyLectureCategories("The Rightly Guided Caliphs and Islamic Golden Age")
        assertTrue(caliphateCats.contains(LectureCategory.ISLAMIC_CALIPHATES))

        // Verify URDU_HINDI + QURAN_TAFSEER fetches Dr. Israr Ahmed Official and NEVER mixes English videos
        val requestedUrls = CopyOnWriteArrayList<String>()
        YouTubeRepository.httpClient = buildMockClient { req ->
            requestedUrls.add(req.url.toString())
            val xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <feed xmlns:yt="http://www.youtube.com/xml/schemas/2015" xmlns:media="http://search.yahoo.com/mrss/" xmlns="http://www.w3.org/2005/Atom">
                  <title>Dr Israr Ahmed Official</title>
                  <entry>
                    <yt:videoId>urduTaf0001</yt:videoId>
                    <title>بیان القرآن - سورہ الفاتحہ تفسیر</title>
                    <published>2026-11-01T10:00:00Z</published>
                    <media:group><media:content duration="1800"/></media:group>
                  </entry>
                  <entry>
                    <yt:videoId>engMix00002</yt:videoId>
                    <title>English Introduction to Tafseer of Surah Al-Fatiha</title>
                    <published>2026-11-01T09:00:00Z</published>
                    <media:group><media:content duration="1800"/></media:group>
                  </entry>
                  <entry>
                    <yt:videoId>shortTaf003</yt:videoId>
                    <title>تفسیر مختصر کلپ</title>
                    <published>2026-11-01T08:00:00Z</published>
                    <media:group><media:content duration="120"/></media:group>
                  </entry>
                </feed>
            """.trimIndent()
            Response.Builder()
                .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
        }

        val res = YouTubeRepository.fetchLecturesForTopic(
            context = context,
            topic = LectureCategory.QURAN_TAFSEER,
            languageGroup = LectureLanguageGroup.URDU_HINDI,
            forceRefresh = true
        )
        assertTrue(
            "Must fetch active Dr. Israr Ahmed / Tanzeem-e-Islami channels for Urdu/Hindi Tafseer",
            requestedUrls.any { it.contains("UCBj6SU_o5wZQJbZJZSkVzOw") } &&
                requestedUrls.any { it.contains("UCwGzYwcG_N8rvrdS2EBQA-g") }
        )
        assertEquals("Only long-form Urdu/Hindi lecture must pass (English & 2-min clip excluded)", 1, res.videos.size)
        assertEquals("urduTaf0001", res.videos[0].id)
        assertTrue(res.filteredShortsCount >= 1)
    }

    @Test
    fun testChannelBatchPaginationAndExponentialBackoff() = runBlocking {
        var fakeNow = 5_000_000L
        YouTubeRepository.currentTimeProvider = { fakeNow }

        val requestedUrls = CopyOnWriteArrayList<String>()
        var failBayyinah = true
        val bayyinahId = YouTubeRepository.VERIFIED_CHANNELS.first { it.key == "bayyinah" }.channelId

        YouTubeRepository.httpClient = buildMockClient { req ->
            val url = req.url.toString()
            requestedUrls.add(url)
            if (url.contains(bayyinahId) && failBayyinah) {
                Response.Builder()
                    .request(req).protocol(Protocol.HTTP_1_1).code(503).message("Unavailable")
                    .body("".toResponseBody("text/plain".toMediaType())).build()
            } else {
                val xml = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <feed xmlns:yt="http://www.youtube.com/xml/schemas/2015" xmlns="http://www.w3.org/2005/Atom">
                      <title>Verified Channel</title>
                      <entry>
                        <yt:videoId>batchVid001</yt:videoId>
                        <title>Stories of the Prophets - Prophet Yusuf</title>
                        <published>2026-11-10T10:00:00Z</published>
                      </entry>
                    </feed>
                """.trimIndent()
                Response.Builder()
                    .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                    .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
            }
        }

        // 1. Batch pagination with batchSize = 2 on STORIES_OF_PROPHETS (which has > 2 verified channels)
        val batch0 = YouTubeRepository.fetchLecturesForTopic(
            context = context,
            topic = LectureCategory.STORIES_OF_PROPHETS,
            forceRefresh = false,
            batchIndex = 0,
            batchSize = 2
        )
        assertEquals(2, batch0.loadedChannelCount)
        assertTrue(batch0.totalMatchingChannels > 2)
        assertTrue(batch0.hasMoreChannelBatches)

        // 2. Exponential backoff on Bayyinah after 503 failure
        val firstFail = YouTubeRepository.fetchLecturesForTopic(
            context = context,
            topic = LectureCategory.QURAN_TAFSEER,
            channelKey = "bayyinah",
            forceRefresh = false
        )
        assertEquals(FeedLoadStatus.ERROR_NO_CACHE, firstFail.status)
        val health = YouTubeRepository.getChannelHealth(bayyinahId)
        assertEquals(ChannelFeedStatus.TEMPORARILY_UNAVAILABLE, health.feedStatus)
        assertTrue("Channel must enter backoff cooldown after failure", health.isInBackoff(fakeNow))

        // Immediate non-forced retry must respect backoff without firing another HTTP request
        val countBeforeBackoffRetry = requestedUrls.size
        YouTubeRepository.fetchLecturesForTopic(
            context = context,
            topic = LectureCategory.QURAN_TAFSEER,
            channelKey = "bayyinah",
            forceRefresh = false
        )
        assertEquals("Backoff cooldown must prevent hammering failing RSS endpoint", countBeforeBackoffRetry, requestedUrls.size)

        // Advance clock past backoff window + recover HTTP 200 -> health resets to HEALTHY
        fakeNow += 60_000L
        failBayyinah = false
        val recovered = YouTubeRepository.fetchLecturesForTopic(
            context = context,
            topic = LectureCategory.QURAN_TAFSEER,
            channelKey = "bayyinah",
            forceRefresh = false
        )
        assertEquals(FeedLoadStatus.ONLINE_FRESH, recovered.status)
        assertEquals(ChannelFeedStatus.HEALTHY, YouTubeRepository.getChannelHealth(bayyinahId).feedStatus)
    }

    @Test
    fun testLecturesScreenLanguagePersistenceNewTopicsAndDirectoryDialog() {
        YouTubeRepository.httpClient = buildMockClient { req ->
            val xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <feed xmlns:yt="http://www.youtube.com/xml/schemas/2015" xmlns:media="http://search.yahoo.com/mrss/" xmlns="http://www.w3.org/2005/Atom">
                  <title>Dr Israr Ahmed Official</title>
                  <entry>
                    <yt:videoId>israrProp01</yt:videoId>
                    <title>قصص الانبیاء - حضرت ابراہیم علیہ السلام</title>
                    <published>2026-11-12T10:00:00Z</published>
                    <media:group><media:content duration="2400"/></media:group>
                  </entry>
                </feed>
            """.trimIndent()
            Response.Builder()
                .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
        }

        val vm = com.example.ui.viewmodel.LecturesViewModel(context.applicationContext as android.app.Application)
        composeRule.setContent {
            NooreHidayatTheme {
                LecturesScreen(language = AppLanguage.ENGLISH, onBack = {}, viewModel = vm)
            }
        }
        composeRule.waitForIdle()

        // Open Directory Status dialog & verify honest target progress display
        composeRule.onNodeWithTag("lectures_directory_badge").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("lectures_directory_dialog_body").assertIsDisplayed()
        composeRule.onNodeWithText(AppLocalization.get("close", AppLanguage.ENGLISH)).performClick()
        composeRule.waitForIdle()

        // Select combined Urdu / Hindi language filter
        composeRule.onNodeWithTag("lectures_lazy_column")
            .performScrollToNode(hasTestTag("lectures_language_row"))
        composeRule.onNodeWithTag("lectures_language_row")
            .performScrollToNode(hasTestTag("lang_chip_${LectureLanguageGroup.URDU_HINDI.id}"))
        composeRule.onNodeWithTag("lang_chip_${LectureLanguageGroup.URDU_HINDI.id}").performClick()
        composeRule.waitForIdle()

        // Select new Stories of the Prophets category (horizontally inside lectures_categories_row)
        composeRule.onNodeWithTag("lectures_lazy_column")
            .performScrollToNode(hasTestTag("lectures_categories_row"))
        composeRule.onNodeWithTag("lectures_categories_row")
            .performScrollToNode(hasTestTag("category_chip_${LectureCategory.STORIES_OF_PROPHETS.id}"))
        composeRule.onNodeWithTag("category_chip_${LectureCategory.STORIES_OF_PROPHETS.id}").performClick()

        composeRule.waitUntil(timeoutMillis = 5_000L) {
            runCatching {
                composeRule.onNodeWithTag("lectures_lazy_column")
                    .performScrollToNode(hasTestTag("grid_lecture_card_israrProp01"))
                true
            }.getOrDefault(false)
        }
        composeRule.onNodeWithTag("grid_lecture_card_israrProp01").assertIsDisplayed()
        vm.cancelActiveOperations()
    }

    @Test
    fun testViewModelAutoExpandsBatchesToDeliverFiftyPlusRealVideosPerCategory() {
        // Each verified channel returns 4 unique long-form Islamic History videos -> batch 0 yields 40,
        // so LecturesViewModel automatically expands into batch 1 to deliver >= 50 real videos.
        YouTubeRepository.httpClient = buildMockClient { req ->
            val cid = req.url.queryParameter("channel_id") ?: "UCunknown00000000000000"
            val tag = cid.takeLast(6)
            val entries = (1..4).joinToString("\n") { idx ->
                val vidId = "h${idx}${tag}ab0".take(11)
                """
                <entry>
                  <yt:videoId>$vidId</yt:videoId>
                  <yt:channelId>$cid</yt:channelId>
                  <title>Islamic History Lecture $idx - Caliphate and Civilization</title>
                  <published>2026-11-1${idx}T10:00:00Z</published>
                  <media:group><media:content duration="1800"/></media:group>
                </entry>
                """.trimIndent()
            }
            val xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <feed xmlns:yt="http://www.youtube.com/xml/schemas/2015" xmlns:media="http://search.yahoo.com/mrss/" xmlns="http://www.w3.org/2005/Atom">
                  <title>Verified Islamic History Feed</title>
                  $entries
                </feed>
            """.trimIndent()
            Response.Builder()
                .request(req).protocol(Protocol.HTTP_1_1).code(200).message("OK")
                .body(xml.toResponseBody("application/atom+xml".toMediaType())).build()
        }

        val vm = com.example.ui.viewmodel.LecturesViewModel(context.applicationContext as android.app.Application)
        vm.selectCategory(LectureCategory.ISLAMIC_HISTORY)

        val deadline = System.currentTimeMillis() + 8_000L
        while (System.currentTimeMillis() < deadline) {
            org.robolectric.shadows.ShadowLooper.idleMainLooper()
            val st = vm.uiState.value
            if (!st.isLoading && !st.isLoadingMoreBatches && st.feedResult.videos.size >= 50) break
            Thread.sleep(25)
        }

        val finalState = vm.uiState.value
        vm.cancelActiveOperations()
        assertTrue(
            "Expected >= 50 real videos after progressive batch expansion, got ${finalState.feedResult.videos.size}",
            finalState.feedResult.videos.size >= 50
        )
        assertTrue("Expected batchIndex >= 1 after auto-expansion", finalState.batchIndex >= 1)
    }
}
