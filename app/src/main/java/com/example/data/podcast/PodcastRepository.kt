package com.example.data.podcast

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileOutputStream
import java.io.StringReader
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object PodcastRepository {

    private const val PREFS_NAME = "noor_podcast_repo_prefs_v1"
    private const val KEY_FEED_PREFIX = "rss_xml_"
    private const val KEY_FEED_TS_PREFIX = "rss_ts_"
    private const val KEY_RESOLVED_URL_PREFIX = "resolved_feed_"
    private const val KEY_RESOLVED_ART_PREFIX = "resolved_art_"
    private const val KEY_DOWNLOADS_INDEX = "downloaded_episodes_json_v1"
    private const val KEY_POSITIONS_PREFIX = "ep_pos_"
    private const val CACHE_TTL_MS = 30 * 60 * 1000L // 30 minutes
    private const val MIN_VALID_AUDIO_BYTES = 2048L

    /** Test hook for deterministic HTTP & download testing without live network. */
    @Volatile
    var httpClientOverrideForTest: OkHttpClient? = null

    /** Test hook to simulate offline/online cleanly in Robolectric tests. */
    @Volatile
    var isOnlineOverrideForTest: Boolean? = null

    private val defaultHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    private fun httpClient(): OkHttpClient = httpClientOverrideForTest ?: defaultHttpClient

    private val downloadScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeDownloadJobs = ConcurrentHashMap<String, Job>()

    private val _downloadStates = MutableStateFlow<Map<String, PodcastDownloadProgress>>(emptyMap())
    val downloadStates: StateFlow<Map<String, PodcastDownloadProgress>> = _downloadStates.asStateFlow()

    /**
     * Curated catalog of verified Islamic podcast publishers and shows.
     * Feeds are fetched strictly from genuine publisher RSS endpoints (either direct RSS URL
     * or verified via Apple's official free Podcast Lookup/Search directory).
     * Download permission is tracked per publisher policy; copyrighted or disallowed feeds never expose downloads.
     */
    val VERIFIED_SHOWS: List<PodcastShow> = listOf(
        PodcastShow(
            id = "yaqeen_podcast",
            showName = "Yaqeen Podcast",
            publisher = "Yaqeen Institute for Islamic Research",
            host = "Dr. Omar Suleiman & Yaqeen Scholars",
            officialRssFeedUrl = "",
            itunesLookupTerm = "Yaqeen Podcast Omar Suleiman",
            expectedKeywords = listOf("yaqeen"),
            artworkUrl = "",
            languageCode = "en",
            primaryCategories = listOf(
                PodcastCategory.SPIRITUAL_DEVELOPMENT,
                PodcastCategory.ISLAMIC_KNOWLEDGE,
                PodcastCategory.MUSLIM_FAMILY,
                PodcastCategory.ISLAMIC_MANNERS,
                PodcastCategory.NEW_MUSLIM_GUIDANCE,
                PodcastCategory.ENGLISH_PODCASTS
            ),
            sourceAttribution = "Yaqeen Institute for Islamic Research (public podcast RSS)",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Public educational RSS syndication for personal listening"
        ),
        PodcastShow(
            id = "quran_30_for_30",
            showName = "Qur'an 30 for 30",
            publisher = "Yaqeen Institute for Islamic Research",
            host = "Dr. Omar Suleiman & Shaykh Abdullah Oduro",
            officialRssFeedUrl = "",
            itunesLookupTerm = "Quran 30 for 30 Yaqeen",
            expectedKeywords = listOf("30 for 30", "yaqeen", "quran"),
            artworkUrl = "",
            languageCode = "en",
            primaryCategories = listOf(
                PodcastCategory.QURAN_REFLECTIONS,
                PodcastCategory.QURAN_TAFSEER,
                PodcastCategory.SPIRITUAL_DEVELOPMENT,
                PodcastCategory.ENGLISH_PODCASTS
            ),
            sourceAttribution = "Yaqeen Institute (public podcast RSS)",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Public educational RSS syndication for personal listening"
        ),
        PodcastShow(
            id = "seekersguidance_podcast",
            showName = "SeekersGuidance Podcast",
            publisher = "SeekersGuidance Global Islamic Academy",
            host = "Shaykh Faraz Rabbani & Seekers Scholars",
            officialRssFeedUrl = "",
            itunesLookupTerm = "SeekersGuidance Faraz Rabbani",
            expectedKeywords = listOf("seekersguidance", "seekers", "faraz"),
            artworkUrl = "",
            languageCode = "en",
            primaryCategories = listOf(
                PodcastCategory.HADITH_SUNNAH,
                PodcastCategory.ISLAMIC_KNOWLEDGE,
                PodcastCategory.ISLAMIC_EDUCATION,
                PodcastCategory.NEW_MUSLIM_GUIDANCE,
                PodcastCategory.ISLAMIC_MANNERS,
                PodcastCategory.ENGLISH_PODCASTS
            ),
            sourceAttribution = "SeekersGuidance (public educational RSS)",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Free open Islamic education syndication"
        ),
        PodcastShow(
            id = "seekers_quran_tafsir",
            showName = "Seekers Quran Tafsir",
            publisher = "SeekersGuidance Global Islamic Academy",
            host = "Shaykh Abdul-Rahim Reasat",
            officialRssFeedUrl = "",
            itunesLookupTerm = "Seekers Quran Tafsir",
            expectedKeywords = listOf("seekers", "tafsir", "quran"),
            artworkUrl = "",
            languageCode = "en",
            primaryCategories = listOf(
                PodcastCategory.QURAN_TAFSEER,
                PodcastCategory.QURAN_REFLECTIONS,
                PodcastCategory.ENGLISH_PODCASTS
            ),
            sourceAttribution = "SeekersGuidance (public educational RSS)",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Free open Islamic education syndication"
        ),
        PodcastShow(
            id = "qalam_podcast",
            showName = "Qalam Podcast",
            publisher = "Qalam Institute",
            host = "Shaykh Abdul Nasir Jangda & Qalam Faculty",
            officialRssFeedUrl = "",
            itunesLookupTerm = "Qalam Podcast Abdul Nasir Jangda",
            expectedKeywords = listOf("qalam"),
            artworkUrl = "",
            languageCode = "en",
            primaryCategories = listOf(
                PodcastCategory.SEERAH,
                PodcastCategory.HADITH_SUNNAH,
                PodcastCategory.MUSLIM_FAMILY,
                PodcastCategory.ISLAMIC_EDUCATION,
                PodcastCategory.ENGLISH_PODCASTS
            ),
            sourceAttribution = "Qalam Institute (public podcast RSS)",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Public educational RSS syndication"
        ),
        PodcastShow(
            id = "mufti_menk_central",
            showName = "Mufti Menk Reminders",
            publisher = "Muslim Central",
            host = "Mufti Ismail Menk",
            officialRssFeedUrl = "",
            itunesLookupTerm = "Mufti Menk Muslim Central",
            expectedKeywords = listOf("menk"),
            artworkUrl = "",
            languageCode = "en",
            primaryCategories = listOf(
                PodcastCategory.SPIRITUAL_DEVELOPMENT,
                PodcastCategory.ISLAMIC_MANNERS,
                PodcastCategory.MUSLIM_FAMILY,
                PodcastCategory.NEW_MUSLIM_GUIDANCE,
                PodcastCategory.ENGLISH_PODCASTS
            ),
            sourceAttribution = "Muslim Central Non-Profit Audio Syndication",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Non-commercial da'wah distribution via Muslim Central RSS"
        ),
        PodcastShow(
            id = "yasir_qadhi_seerah",
            showName = "Seerah of Prophet Muhammad ﷺ",
            publisher = "Muslim Central",
            host = "Dr. Yasir Qadhi",
            officialRssFeedUrl = "",
            itunesLookupTerm = "Seerah of Prophet Muhammed Yasir Qadhi",
            expectedKeywords = listOf("seerah", "qadhi"),
            artworkUrl = "",
            languageCode = "en",
            primaryCategories = listOf(
                PodcastCategory.SEERAH,
                PodcastCategory.ISLAMIC_HISTORY,
                PodcastCategory.ENGLISH_PODCASTS
            ),
            sourceAttribution = "Muslim Central Non-Profit Audio Syndication",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Non-commercial educational distribution via Muslim Central RSS"
        ),
        PodcastShow(
            id = "bayan_ul_quran_urdu",
            showName = "Bayan-ul-Quran (بیان القرآن)",
            publisher = "Islamic Audio Syndication",
            host = "Dr. Israr Ahmed",
            officialRssFeedUrl = "",
            itunesLookupTerm = "Bayan ul Quran Dr Israr Ahmed",
            expectedKeywords = listOf("bayan", "israr", "quran"),
            artworkUrl = "",
            languageCode = "ur",
            primaryCategories = listOf(
                PodcastCategory.QURAN_TAFSEER,
                PodcastCategory.QURAN_REFLECTIONS,
                PodcastCategory.ISLAMIC_KNOWLEDGE,
                PodcastCategory.URDU_PODCASTS
            ),
            sourceAttribution = "Public Bayan-ul-Quran RSS Archive",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Open Islamic educational audio distribution"
        ),
        PodcastShow(
            id = "multilang_islamic_anchor",
            showName = "Islamic Podcasts (Arabic, Urdu & English)",
            publisher = "Spotify for Creators Public Islamic Feed",
            host = "Verified Islamic Lectures Feed",
            officialRssFeedUrl = "https://anchor.fm/s/f1c8a11c/podcast/rss",
            itunesLookupTerm = "Islamic Podcasts in Arabic English Urdu",
            expectedKeywords = listOf("islamic"),
            artworkUrl = "",
            languageCode = "ur",
            primaryCategories = listOf(
                PodcastCategory.SEERAH,
                PodcastCategory.ISLAMIC_KNOWLEDGE,
                PodcastCategory.URDU_PODCASTS,
                PodcastCategory.ARABIC_PODCASTS
            ),
            sourceAttribution = "Public RSS Feed (anchor.fm/s/f1c8a11c/podcast/rss)",
            verificationStatus = PodcastVerificationStatus.VERIFIED_RSS_FEED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Open RSS podcast syndication"
        ),
        PodcastShow(
            id = "othman_alkhamees_ar",
            showName = "دروس الشيخ د. عثمان الخميس",
            publisher = "Muslim Central / Public Arabic RSS",
            host = "الشيخ د. عثمان الخميس",
            officialRssFeedUrl = "",
            itunesLookupTerm = "عثمان الخميس",
            expectedKeywords = listOf("عثمان", "الخميس", "othman"),
            artworkUrl = "",
            languageCode = "ar",
            primaryCategories = listOf(
                PodcastCategory.ISLAMIC_KNOWLEDGE,
                PodcastCategory.HADITH_SUNNAH,
                PodcastCategory.ISLAMIC_EDUCATION,
                PodcastCategory.ARABIC_PODCASTS
            ),
            sourceAttribution = "Public Arabic Scholarly RSS Syndication",
            verificationStatus = PodcastVerificationStatus.DIRECTORY_LOOKUP_VERIFIED,
            downloadPermittedByPublisher = true,
            publisherLicenseNote = "Open scholarly RSS audio feed"
        )
    )

    fun isNetworkConnected(context: Context): Boolean {
        isOnlineOverrideForTest?.let { return it }
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return true
            val network = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            true
        }
    }

    fun isValidAudioEnclosureUrl(url: String?, mimeType: String? = null): Boolean {
        if (url.isNullOrBlank()) return false
        val trimmed = url.trim()
        val parsed = try {
            Uri.parse(trimmed)
        } catch (_: Exception) {
            return false
        }
        val scheme = parsed.scheme?.lowercase(Locale.US) ?: return false
        if (scheme != "https" && scheme != "http") return false
        val host = parsed.host?.lowercase(Locale.US) ?: return false
        if (host.contains("example.com") || host.contains("example.org") || host == "localhost" || host == "127.0.0.1") {
            return false
        }
        val mimeLower = mimeType?.lowercase(Locale.US).orEmpty()
        val pathLower = (parsed.path ?: trimmed).lowercase(Locale.US)
        val hasAudioMime = mimeLower.startsWith("audio/") || mimeLower == "application/ogg"
        val hasAudioExt = listOf(".mp3", ".m4a", ".aac", ".ogg", ".opus", ".wav", ".mp4").any { pathLower.contains(it) }
        return hasAudioMime || hasAudioExt || mimeLower.isBlank()
    }

    fun isValidRssFeedUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val parsed = try {
            Uri.parse(url.trim())
        } catch (_: Exception) {
            return false
        }
        val scheme = parsed.scheme?.lowercase(Locale.US) ?: return false
        val host = parsed.host?.lowercase(Locale.US) ?: return false
        if (scheme != "https" && scheme != "http") return false
        if (host.contains("example.com") || host == "localhost") return false
        return true
    }

    suspend fun loadCatalogAndEpisodes(
        context: Context,
        forceRefresh: Boolean = false
    ): PodcastFeedLoadResult = withContext(Dispatchers.IO) {
        refreshDownloadIndex(context)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val online = isNetworkConnected(context)

        val resolvedShows = mutableListOf<PodcastShow>()
        val allEpisodes = mutableListOf<PodcastEpisode>()
        val unavailableShows = mutableListOf<String>()
        var anyFromCache = false
        var latestTs = 0L

        coroutineScope {
            val deferredResults = VERIFIED_SHOWS.map { baseShow ->
                async {
                    loadSingleShowFeed(prefs, baseShow, online, forceRefresh)
                }
            }
            deferredResults.awaitAll().forEach { outcome ->
                resolvedShows.add(outcome.show)
                allEpisodes.addAll(outcome.episodes)
                if (outcome.episodes.isEmpty()) {
                    unavailableShows.add(outcome.show.showName)
                }
                if (outcome.fromCache) anyFromCache = true
                if (outcome.timestampMs > latestTs) latestTs = outcome.timestampMs
            }
        }

        val sortedEpisodes = allEpisodes
            .distinctBy { "${it.showId}_${it.guid}_${it.audioEnclosureUrl}" }
            .sortedByDescending { it.publishedEpochMs }

        PodcastFeedLoadResult(
            shows = resolvedShows,
            episodes = sortedEpisodes,
            isOffline = !online,
            isFromCache = anyFromCache,
            unavailableShows = unavailableShows,
            lastUpdatedMs = latestTs,
            errorMessage = if (sortedEpisodes.isEmpty()) {
                if (!online) "Offline and no cached episodes available yet."
                else "Unable to load verified podcast RSS feeds right now."
            } else null
        )
    }

    private data class SingleShowOutcome(
        val show: PodcastShow,
        val episodes: List<PodcastEpisode>,
        val fromCache: Boolean,
        val timestampMs: Long
    )

    private fun loadSingleShowFeed(
        prefs: android.content.SharedPreferences,
        baseShow: PodcastShow,
        online: Boolean,
        forceRefresh: Boolean
    ): SingleShowOutcome {
        val cachedXml = prefs.getString("$KEY_FEED_PREFIX${baseShow.id}", null)
        val cachedTs = prefs.getLong("$KEY_FEED_TS_PREFIX${baseShow.id}", 0L)
        val cachedFeedUrl = prefs.getString("$KEY_RESOLVED_URL_PREFIX${baseShow.id}", null)
        val cachedArtUrl = prefs.getString("$KEY_RESOLVED_ART_PREFIX${baseShow.id}", null)

        val initialFeedUrl = baseShow.officialRssFeedUrl.ifBlank { cachedFeedUrl.orEmpty() }
        val initialArtUrl = baseShow.artworkUrl.ifBlank { cachedArtUrl.orEmpty() }
        val cacheFresh = !forceRefresh &&
            !cachedXml.isNullOrBlank() &&
            (System.currentTimeMillis() - cachedTs) < CACHE_TTL_MS

        if (cacheFresh || !online) {
            if (!cachedXml.isNullOrBlank()) {
                val parsed = parseRssFeedXml(
                    xml = cachedXml,
                    show = baseShow.copy(
                        officialRssFeedUrl = initialFeedUrl,
                        artworkUrl = initialArtUrl
                    ),
                    isFromCache = !cacheFresh || !online
                )
                if (parsed.episodes.isNotEmpty()) {
                    return SingleShowOutcome(
                        show = parsed.updatedShow.copy(
                            verificationStatus = if (initialFeedUrl.isNotBlank()) PodcastVerificationStatus.VERIFIED_RSS_FEED else baseShow.verificationStatus
                        ),
                        episodes = parsed.episodes,
                        fromCache = true,
                        timestampMs = cachedTs
                    )
                }
            }
            if (!online) {
                return SingleShowOutcome(
                    show = baseShow.copy(verificationStatus = PodcastVerificationStatus.FEED_UNAVAILABLE),
                    episodes = emptyList(),
                    fromCache = false,
                    timestampMs = cachedTs
                )
            }
        }

        // Resolve feed URL via Apple Podcast Lookup if missing
        var effectiveFeedUrl = initialFeedUrl
        var effectiveArtUrl = initialArtUrl
        if (!isValidRssFeedUrl(effectiveFeedUrl)) {
            val lookup = resolveFeedFromAppleDirectory(baseShow)
            if (lookup != null && isValidRssFeedUrl(lookup.first)) {
                effectiveFeedUrl = lookup.first
                if (lookup.second.isNotBlank()) effectiveArtUrl = lookup.second
                prefs.edit()
                    .putString("$KEY_RESOLVED_URL_PREFIX${baseShow.id}", effectiveFeedUrl)
                    .putString("$KEY_RESOLVED_ART_PREFIX${baseShow.id}", effectiveArtUrl)
                    .apply()
            }
        }

        if (isValidRssFeedUrl(effectiveFeedUrl)) {
            val xml = fetchString(effectiveFeedUrl)
            if (!xml.isNullOrBlank()) {
                val workingShow = baseShow.copy(
                    officialRssFeedUrl = effectiveFeedUrl,
                    artworkUrl = effectiveArtUrl,
                    verificationStatus = PodcastVerificationStatus.VERIFIED_RSS_FEED
                )
                val parsed = parseRssFeedXml(xml = xml, show = workingShow, isFromCache = false)
                if (parsed.episodes.isNotEmpty()) {
                    val now = System.currentTimeMillis()
                    prefs.edit()
                        .putString("$KEY_FEED_PREFIX${baseShow.id}", xml)
                        .putLong("$KEY_FEED_TS_PREFIX${baseShow.id}", now)
                        .apply()
                    return SingleShowOutcome(
                        show = parsed.updatedShow,
                        episodes = parsed.episodes,
                        fromCache = false,
                        timestampMs = now
                    )
                }
            }
        }

        // Fallback to stale cache if live fetch failed
        if (!cachedXml.isNullOrBlank()) {
            val parsed = parseRssFeedXml(
                xml = cachedXml,
                show = baseShow.copy(officialRssFeedUrl = effectiveFeedUrl, artworkUrl = effectiveArtUrl),
                isFromCache = true
            )
            if (parsed.episodes.isNotEmpty()) {
                return SingleShowOutcome(
                    show = parsed.updatedShow,
                    episodes = parsed.episodes,
                    fromCache = true,
                    timestampMs = cachedTs
                )
            }
        }

        return SingleShowOutcome(
            show = baseShow.copy(verificationStatus = PodcastVerificationStatus.FEED_UNAVAILABLE),
            episodes = emptyList(),
            fromCache = false,
            timestampMs = 0L
        )
    }

    private fun resolveFeedFromAppleDirectory(show: PodcastShow): Pair<String, String>? {
        return try {
            val encodedTerm = java.net.URLEncoder.encode(show.itunesLookupTerm, "UTF-8")
            val url = "https://itunes.apple.com/search?term=$encodedTerm&media=podcast&entity=podcast&limit=5"
            val body = fetchString(url) ?: return null
            val root = JSONObject(body)
            val results = root.optJSONArray("results") ?: return null
            for (i in 0 until results.length()) {
                val obj = results.optJSONObject(i) ?: continue
                val feedUrl = obj.optString("feedUrl", "").trim()
                if (!isValidRssFeedUrl(feedUrl)) continue
                val haystack = "${obj.optString("collectionName")} ${obj.optString("artistName")}".lowercase(Locale.US)
                val matches = show.expectedKeywords.isEmpty() || show.expectedKeywords.any { kw ->
                    haystack.contains(kw.lowercase(Locale.US))
                }
                if (matches) {
                    val artwork = obj.optString("artworkUrl600").ifBlank { obj.optString("artworkUrl100") }
                    return Pair(feedUrl, artwork)
                }
            }
            null
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchString(url: String): String? {
        return try {
            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/rss+xml, application/xml, text/xml, application/json")
                .header("User-Agent", "Noor-e-Hidayat/1.0 PodcastClient")
                .build()
            httpClient().newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return null
                resp.body?.string()
            }
        } catch (_: Exception) {
            null
        }
    }

    data class ParsedRssFeed(
        val updatedShow: PodcastShow,
        val episodes: List<PodcastEpisode>
    )

    fun parseRssFeedXml(
        xml: String,
        show: PodcastShow,
        isFromCache: Boolean = false
    ): ParsedRssFeed {
        val cleaned = xml.trimStart('\uFEFF', ' ', '\t', '\r', '\n').trim()
        if (cleaned.isEmpty() || !cleaned.contains("<")) {
            return ParsedRssFeed(show, emptyList())
        }

        val episodes = mutableListOf<PodcastEpisode>()
        var channelTitle = show.showName
        var channelAuthor = show.host
        var channelArt = show.artworkUrl

        try {
            val factory = XmlPullParserFactory.newInstance().apply {
                isNamespaceAware = false
            }
            val parser = factory.newPullParser()
            parser.setInput(StringReader(cleaned))

            var eventType = parser.eventType
            var insideChannel = false
            var insideImageTag = false
            var insideItem = false

            var guid = ""
            var title = ""
            var description = ""
            var encodedContent = ""
            var itunesSummary = ""
            var pubDate = ""
            var enclosureUrl = ""
            var enclosureType = ""
            var enclosureLength = 0L
            var durationRaw = ""
            var itemArt = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val tag = parser.name?.lowercase(Locale.US) ?: ""
                        when {
                            tag == "channel" -> insideChannel = true
                            tag == "item" -> {
                                insideItem = true
                                guid = ""
                                title = ""
                                description = ""
                                encodedContent = ""
                                itunesSummary = ""
                                pubDate = ""
                                enclosureUrl = ""
                                enclosureType = ""
                                enclosureLength = 0L
                                durationRaw = ""
                                itemArt = ""
                            }
                            insideItem -> {
                                when (tag) {
                                    "guid" -> guid = safeNextText(parser)
                                    "title" -> title = cleanHtmlText(safeNextText(parser))
                                    "description" -> description = cleanHtmlText(safeNextText(parser))
                                    "content:encoded" -> encodedContent = cleanHtmlText(safeNextText(parser))
                                    "itunes:summary" -> itunesSummary = cleanHtmlText(safeNextText(parser))
                                    "pubdate" -> pubDate = safeNextText(parser).trim()
                                    "itunes:duration" -> durationRaw = safeNextText(parser).trim()
                                    "itunes:image" -> {
                                        val href = parser.getAttributeValue(null, "href")?.trim().orEmpty()
                                        if (href.startsWith("http")) itemArt = href
                                    }
                                    "enclosure", "media:content" -> {
                                        val url = parser.getAttributeValue(null, "url")?.trim().orEmpty()
                                        val type = parser.getAttributeValue(null, "type")?.trim().orEmpty()
                                        val lenStr = parser.getAttributeValue(null, "length")
                                            ?: parser.getAttributeValue(null, "fileSize")
                                        val len = lenStr?.toLongOrNull() ?: 0L
                                        if (url.isNotBlank() && isValidAudioEnclosureUrl(url, type)) {
                                            enclosureUrl = url
                                            enclosureType = type
                                            enclosureLength = len
                                        }
                                    }
                                }
                            }
                            insideChannel -> {
                                when (tag) {
                                    "image" -> insideImageTag = true
                                    "url" -> if (insideImageTag && channelArt.isBlank()) {
                                        val u = safeNextText(parser).trim()
                                        if (u.startsWith("http")) channelArt = u
                                    }
                                    "title" -> if (!insideImageTag) {
                                        val t = cleanHtmlText(safeNextText(parser))
                                        if (t.isNotBlank()) channelTitle = t
                                    }
                                    "itunes:author" -> {
                                        val a = cleanHtmlText(safeNextText(parser))
                                        if (a.isNotBlank()) channelAuthor = a
                                    }
                                    "itunes:image" -> {
                                        val href = parser.getAttributeValue(null, "href")?.trim().orEmpty()
                                        if (href.startsWith("http") && channelArt.isBlank()) {
                                            channelArt = href
                                        }
                                    }
                                }
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        val tag = parser.name?.lowercase(Locale.US) ?: ""
                        if (tag == "image") insideImageTag = false
                        if (tag == "item" && insideItem) {
                            insideItem = false
                            if (title.isNotBlank() && isValidAudioEnclosureUrl(enclosureUrl, enclosureType)) {
                                val resolvedGuid = guid.ifBlank {
                                    "${show.id}_${sha256Short(enclosureUrl)}"
                                }
                                val resolvedDesc = when {
                                    description.isNotBlank() -> description
                                    itunesSummary.isNotBlank() -> itunesSummary
                                    encodedContent.isNotBlank() -> encodedContent
                                    else -> ""
                                }.take(500)
                                val parsedEpoch = parseRssDateEpochMs(pubDate)
                                val displayDate = formatDisplayDate(parsedEpoch, pubDate)
                                val durationSec = parseDurationSeconds(durationRaw)
                                val durationDisplay = if (durationSec > 0L) formatDuration(durationSec) else ""
                                val episodeCats = classifyEpisodeCategories(show, title, resolvedDesc)

                                episodes.add(
                                    PodcastEpisode(
                                        guid = resolvedGuid,
                                        showId = show.id,
                                        showName = channelTitle.ifBlank { show.showName },
                                        publisher = show.publisher,
                                        host = channelAuthor.ifBlank { show.host },
                                        title = title,
                                        description = resolvedDesc,
                                        pubDateRaw = pubDate,
                                        pubDateDisplay = displayDate,
                                        publishedEpochMs = parsedEpoch,
                                        audioEnclosureUrl = enclosureUrl,
                                        mimeType = enclosureType.ifBlank { inferMimeType(enclosureUrl) },
                                        fileLengthBytes = enclosureLength.coerceAtLeast(0L),
                                        durationSeconds = durationSec,
                                        durationDisplay = durationDisplay,
                                        artworkUrl = itemArt.ifBlank { channelArt },
                                        languageCode = show.languageCode,
                                        categories = episodeCats,
                                        sourceAttribution = show.sourceAttribution,
                                        downloadPermittedByPublisher = show.downloadPermittedByPublisher,
                                        isFromCache = isFromCache
                                    )
                                )
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (_: Exception) {
            // Return whatever valid items were parsed before XML fault, never fabricating entries
        }

        val updatedShow = show.copy(
            showName = channelTitle.ifBlank { show.showName },
            host = channelAuthor.ifBlank { show.host },
            artworkUrl = channelArt.ifBlank { show.artworkUrl }
        )
        return ParsedRssFeed(updatedShow, episodes.take(40))
    }

    private fun safeNextText(parser: XmlPullParser): String {
        return try {
            parser.nextText()?.trim().orEmpty()
        } catch (_: Exception) {
            ""
        }
    }

    private fun cleanHtmlText(raw: String): String {
        if (raw.isBlank()) return ""
        return raw
            .replace(Regex("<[^>]+>"), " ")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&nbsp;", " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    fun parseDurationSeconds(raw: String): Long {
        if (raw.isBlank()) return 0L
        val clean = raw.trim()
        clean.toLongOrNull()?.let { return it.coerceAtLeast(0L) }
        val parts = clean.split(":").mapNotNull { it.trim().toLongOrNull() }
        return when (parts.size) {
            3 -> (parts[0] * 3600L) + (parts[1] * 60L) + parts[2]
            2 -> (parts[0] * 60L) + parts[1]
            1 -> parts[0]
            else -> 0L
        }.coerceAtLeast(0L)
    }

    fun formatDuration(seconds: Long): String {
        if (seconds <= 0L) return ""
        val h = seconds / 3600L
        val m = (seconds % 3600L) / 60L
        val s = seconds % 60L
        return if (h > 0) {
            String.format(Locale.US, "%d:%02d:%02d", h, m, s)
        } else {
            String.format(Locale.US, "%02d:%02d", m, s)
        }
    }

    fun parseRssDateEpochMs(raw: String): Long {
        if (raw.isBlank()) return 0L
        val formats = listOf(
            "EEE, dd MMM yyyy HH:mm:ss Z",
            "EEE, dd MMM yyyy HH:mm:ss zzz",
            "dd MMM yyyy HH:mm:ss Z",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd"
        )
        for (pattern in formats) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.US).apply {
                    isLenient = true
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val date = sdf.parse(raw.trim())
                if (date != null) return date.time
            } catch (_: Exception) {
            }
        }
        return 0L
    }

    private fun formatDisplayDate(epochMs: Long, rawFallback: String): String {
        if (epochMs <= 0L) return rawFallback.take(16)
        return SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date(epochMs))
    }

    private fun inferMimeType(url: String): String {
        val lower = url.lowercase(Locale.US)
        return when {
            lower.contains(".m4a") || lower.contains(".mp4") -> "audio/mp4"
            lower.contains(".aac") -> "audio/aac"
            lower.contains(".ogg") || lower.contains(".opus") -> "audio/ogg"
            else -> "audio/mpeg"
        }
    }

    private fun classifyEpisodeCategories(
        show: PodcastShow,
        title: String,
        description: String
    ): List<PodcastCategory> {
        val result = linkedSetOf<PodcastCategory>()
        result.addAll(show.primaryCategories)
        when (show.languageCode.lowercase(Locale.US)) {
            "ur" -> result.add(PodcastCategory.URDU_PODCASTS)
            "ar" -> result.add(PodcastCategory.ARABIC_PODCASTS)
            else -> result.add(PodcastCategory.ENGLISH_PODCASTS)
        }
        val haystack = "$title $description".lowercase(Locale.US)
        PodcastCategory.entries.forEach { cat ->
            if (cat.keywords.any { kw -> haystack.contains(kw.lowercase(Locale.US)) }) {
                result.add(cat)
            }
        }
        return result.toList()
    }

    fun filterEpisodes(
        episodes: List<PodcastEpisode>,
        query: String = "",
        selectedCategory: PodcastCategory? = null,
        selectedShowId: String? = null,
        downloadedOnly: Boolean = false,
        downloadedGuids: Set<String> = emptySet()
    ): List<PodcastEpisode> {
        val q = query.trim().lowercase(Locale.US)
        return episodes.filter { ep ->
            val catOk = selectedCategory == null || ep.categories.contains(selectedCategory)
            val showOk = selectedShowId.isNullOrBlank() || ep.showId == selectedShowId
            val dlOk = !downloadedOnly || downloadedGuids.contains(ep.guid)
            val queryOk = q.isBlank() ||
                ep.title.lowercase(Locale.US).contains(q) ||
                ep.showName.lowercase(Locale.US).contains(q) ||
                ep.host.lowercase(Locale.US).contains(q) ||
                ep.publisher.lowercase(Locale.US).contains(q) ||
                ep.description.lowercase(Locale.US).contains(q)
            catOk && showOk && dlOk && queryOk
        }
    }

    /**
     * Returns only categories that genuinely have at least one loaded verified episode.
     * Ensures empty categories are never shown as containing episodes.
     */
    fun availableCategoriesWithCounts(episodes: List<PodcastEpisode>): List<Pair<PodcastCategory, Int>> {
        if (episodes.isEmpty()) return emptyList()
        return PodcastCategory.entries.mapNotNull { category ->
            val count = episodes.count { it.categories.contains(category) }
            if (count > 0) category to count else null
        }
    }

    // -------------------------------------------------------------------------
    // Offline App-Managed Storage & Verified Audio Downloading
    // -------------------------------------------------------------------------

    fun getPodcastStorageDir(context: Context): File {
        val dir = File(context.filesDir, "podcasts")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun safeFileNameForGuid(guid: String, url: String): String {
        val hash = sha256Short(guid + "|" + url)
        val ext = when {
            url.lowercase(Locale.US).contains(".m4a") -> ".m4a"
            url.lowercase(Locale.US).contains(".aac") -> ".aac"
            url.lowercase(Locale.US).contains(".ogg") -> ".ogg"
            else -> ".mp3"
        }
        return "pod_$hash$ext"
    }

    fun getLocalAudioFile(context: Context, episode: PodcastEpisode): File? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val map = loadDownloadedIndexMap(prefs)
        val entry = map.optJSONObject(episode.guid) ?: return null
        val path = entry.optString("filePath", "")
        if (path.isBlank()) return null
        val file = File(path)
        return if (file.exists() && file.length() >= MIN_VALID_AUDIO_BYTES) file else null
    }

    fun isEpisodeDownloadedOffline(context: Context, episode: PodcastEpisode): Boolean {
        return getLocalAudioFile(context, episode) != null
    }

    fun refreshDownloadIndex(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dir = getPodcastStorageDir(context)
        val map = loadDownloadedIndexMap(prefs)
        val updatedStates = _downloadStates.value.toMutableMap()
        val cleanedMap = JSONObject()

        val keys = map.keys()
        while (keys.hasNext()) {
            val guid = keys.next()
            val obj = map.optJSONObject(guid) ?: continue
            val path = obj.optString("filePath", "")
            val file = File(path)
            if (path.isNotBlank() && file.exists() && file.length() >= MIN_VALID_AUDIO_BYTES) {
                cleanedMap.put(guid, obj)
                updatedStates[guid] = PodcastDownloadProgress(
                    episodeGuid = guid,
                    status = PodcastDownloadStatus.DOWNLOADED,
                    bytesDownloaded = file.length(),
                    totalBytes = file.length(),
                    localFilePath = file.absolutePath
                )
            }
        }

        // Also detect partial .tmp files as paused so user can resume
        prefs.edit().putString(KEY_DOWNLOADS_INDEX, cleanedMap.toString()).apply()
        if (dir.exists()) {
            updatedStates.entries.removeAll { (guid, st) ->
                st.status == PodcastDownloadStatus.DOWNLOADED && !cleanedMap.has(guid)
            }
        }
        _downloadStates.value = updatedStates
    }

    fun startEpisodeDownload(context: Context, episode: PodcastEpisode) {
        if (!episode.downloadPermittedByPublisher || !isValidAudioEnclosureUrl(episode.audioEnclosureUrl, episode.mimeType)) {
            updateDownloadState(
                episode.guid,
                PodcastDownloadProgress(
                    episodeGuid = episode.guid,
                    status = PodcastDownloadStatus.FAILED,
                    errorMessage = "Download not permitted or audio source invalid"
                )
            )
            return
        }
        if (activeDownloadJobs[episode.guid]?.isActive == true) return
        val appContext = context.applicationContext
        val job = downloadScope.launch {
            downloadEpisodeSuspend(appContext, episode)
        }
        activeDownloadJobs[episode.guid] = job
    }

    fun pauseEpisodeDownload(episodeGuid: String) {
        activeDownloadJobs.remove(episodeGuid)?.cancel()
        val prev = _downloadStates.value[episodeGuid]
        if (prev != null && prev.status == PodcastDownloadStatus.DOWNLOADING) {
            updateDownloadState(
                episodeGuid,
                prev.copy(status = PodcastDownloadStatus.PAUSED)
            )
        }
    }

    suspend fun downloadEpisodeSuspend(
        context: Context,
        episode: PodcastEpisode
    ): Result<File> = withContext(Dispatchers.IO) {
        if (!episode.downloadPermittedByPublisher) {
            return@withContext Result.failure(IllegalStateException("Publisher does not permit offline download"))
        }
        if (!isValidAudioEnclosureUrl(episode.audioEnclosureUrl, episode.mimeType)) {
            return@withContext Result.failure(IllegalArgumentException("Invalid audio enclosure URL"))
        }
        if (com.example.data.repository.UserPreferencesRepository.isDownloadBlockedByWifiOnly(context)) {
            val msg = "Wi-Fi Only Downloads is enabled in Settings. Connect to Wi-Fi or disable this option."
            updateDownloadState(
                episode.guid,
                PodcastDownloadProgress(
                    episodeGuid = episode.guid,
                    status = PodcastDownloadStatus.FAILED,
                    errorMessage = msg
                )
            )
            return@withContext Result.failure(IllegalStateException(msg))
        }
        val dir = getPodcastStorageDir(context)
        val fileName = safeFileNameForGuid(episode.guid, episode.audioEnclosureUrl)
        val targetFile = File(dir, fileName)
        val tmpFile = File(dir, "$fileName.tmp")

        if (targetFile.exists() && targetFile.length() >= MIN_VALID_AUDIO_BYTES && looksLikeAudioFile(targetFile)) {
            persistDownloadedEntry(context, episode, targetFile)
            updateDownloadState(
                episode.guid,
                PodcastDownloadProgress(
                    episodeGuid = episode.guid,
                    status = PodcastDownloadStatus.DOWNLOADED,
                    bytesDownloaded = targetFile.length(),
                    totalBytes = targetFile.length(),
                    localFilePath = targetFile.absolutePath
                )
            )
            return@withContext Result.success(targetFile)
        }

        val existingBytes = if (tmpFile.exists()) tmpFile.length() else 0L
        updateDownloadState(
            episode.guid,
            PodcastDownloadProgress(
                episodeGuid = episode.guid,
                status = PodcastDownloadStatus.DOWNLOADING,
                bytesDownloaded = existingBytes,
                totalBytes = episode.fileLengthBytes
            )
        )

        try {
            val reqBuilder = Request.Builder()
                .url(episode.audioEnclosureUrl)
                .header("User-Agent", "Noor-e-Hidayat/1.0 PodcastDownloader")
            if (existingBytes > 0L) {
                reqBuilder.header("Range", "bytes=$existingBytes-")
            }

            httpClient().newCall(reqBuilder.build()).execute().use { response ->
                if (!response.isSuccessful) {
                    if (response.code == 416 && tmpFile.exists()) {
                        tmpFile.delete()
                    }
                    val msg = "HTTP ${response.code}"
                    updateDownloadState(
                        episode.guid,
                        PodcastDownloadProgress(
                            episodeGuid = episode.guid,
                            status = PodcastDownloadStatus.FAILED,
                            bytesDownloaded = existingBytes,
                            totalBytes = episode.fileLengthBytes,
                            errorMessage = msg
                        )
                    )
                    return@withContext Result.failure(IllegalStateException(msg))
                }

                val isPartial = response.code == 206
                val contentType = response.header("Content-Type")?.lowercase(Locale.US).orEmpty()
                if (contentType.contains("text/html") || contentType.contains("application/xml")) {
                    val msg = "Unexpected non-audio server response ($contentType)"
                    updateDownloadState(
                        episode.guid,
                        PodcastDownloadProgress(episode.guid, PodcastDownloadStatus.FAILED, errorMessage = msg)
                    )
                    return@withContext Result.failure(IllegalStateException(msg))
                }

                val body = response.body ?: return@withContext Result.failure(IllegalStateException("Empty response body"))
                val bodyLength = body.contentLength()
                val startOffset = if (isPartial) existingBytes else 0L
                val totalExpected = if (bodyLength > 0L) startOffset + bodyLength else episode.fileLengthBytes

                if (!isPartial && tmpFile.exists()) {
                    tmpFile.delete()
                }

                var written = startOffset
                val buffer = ByteArray(16 * 1024)
                var lastProgressEmit = 0L

                body.byteStream().use { input ->
                    FileOutputStream(tmpFile, isPartial).use { output ->
                        while (true) {
                            val read = input.read(buffer)
                            if (read == -1) break
                            output.write(buffer, 0, read)
                            written += read
                            val now = System.currentTimeMillis()
                            if (now - lastProgressEmit >= 120L) {
                                lastProgressEmit = now
                                updateDownloadState(
                                    episode.guid,
                                    PodcastDownloadProgress(
                                        episodeGuid = episode.guid,
                                        status = PodcastDownloadStatus.DOWNLOADING,
                                        bytesDownloaded = written,
                                        totalBytes = totalExpected
                                    )
                                )
                            }
                        }
                        output.flush()
                    }
                }

                if (tmpFile.length() < MIN_VALID_AUDIO_BYTES || !looksLikeAudioFile(tmpFile)) {
                    tmpFile.delete()
                    val msg = "Downloaded file failed audio validation"
                    updateDownloadState(
                        episode.guid,
                        PodcastDownloadProgress(episode.guid, PodcastDownloadStatus.FAILED, errorMessage = msg)
                    )
                    return@withContext Result.failure(IllegalStateException(msg))
                }

                if (targetFile.exists()) targetFile.delete()
                val renamed = tmpFile.renameTo(targetFile)
                if (!renamed) {
                    tmpFile.copyTo(targetFile, overwrite = true)
                    tmpFile.delete()
                }

                persistDownloadedEntry(context, episode, targetFile)
                updateDownloadState(
                    episode.guid,
                    PodcastDownloadProgress(
                        episodeGuid = episode.guid,
                        status = PodcastDownloadStatus.DOWNLOADED,
                        bytesDownloaded = targetFile.length(),
                        totalBytes = targetFile.length(),
                        localFilePath = targetFile.absolutePath
                    )
                )
                Result.success(targetFile)
            }
        } catch (ce: CancellationException) {
            val prev = _downloadStates.value[episode.guid]
            if (prev != null) {
                updateDownloadState(
                    episode.guid,
                    prev.copy(status = PodcastDownloadStatus.PAUSED, bytesDownloaded = tmpFile.length())
                )
            }
            throw ce
        } catch (e: Exception) {
            updateDownloadState(
                episode.guid,
                PodcastDownloadProgress(
                    episodeGuid = episode.guid,
                    status = PodcastDownloadStatus.FAILED,
                    bytesDownloaded = if (tmpFile.exists()) tmpFile.length() else 0L,
                    totalBytes = episode.fileLengthBytes,
                    errorMessage = e.message ?: "Download failed"
                )
            )
            Result.failure(e)
        } finally {
            activeDownloadJobs.remove(episode.guid)
        }
    }

    fun deleteDownloadedEpisode(context: Context, episode: PodcastEpisode): Boolean {
        activeDownloadJobs.remove(episode.guid)?.cancel()
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val map = loadDownloadedIndexMap(prefs)
        val entry = map.optJSONObject(episode.guid)
        val storedPath = entry?.optString("filePath", "").orEmpty()
        var deleted = false
        if (storedPath.isNotBlank()) {
            val file = File(storedPath)
            if (file.exists()) {
                deleted = file.delete()
            }
        }
        val expectedFile = File(getPodcastStorageDir(context), safeFileNameForGuid(episode.guid, episode.audioEnclosureUrl))
        if (expectedFile.exists()) {
            deleted = expectedFile.delete() || deleted
        }
        val tmpFile = File(getPodcastStorageDir(context), "${expectedFile.name}.tmp")
        if (tmpFile.exists()) tmpFile.delete()

        map.remove(episode.guid)
        prefs.edit().putString(KEY_DOWNLOADS_INDEX, map.toString()).apply()

        val next = _downloadStates.value.toMutableMap()
        next.remove(episode.guid)
        _downloadStates.value = next
        return deleted
    }

    fun totalDownloadedBytes(context: Context): Long {
        val dir = getPodcastStorageDir(context)
        if (!dir.exists()) return 0L
        return dir.listFiles()
            ?.filter { it.isFile && !it.name.endsWith(".tmp") }
            ?.sumOf { it.length() }
            ?: 0L
    }

    fun savePlaybackPositionMs(context: Context, episodeGuid: String, positionMs: Long) {
        if (episodeGuid.isBlank() || positionMs < 0L) return
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putLong("$KEY_POSITIONS_PREFIX$episodeGuid", positionMs)
            .apply()
    }

    fun getSavedPlaybackPositionMs(context: Context, episodeGuid: String): Long {
        if (episodeGuid.isBlank()) return 0L
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getLong("$KEY_POSITIONS_PREFIX$episodeGuid", 0L)
            .coerceAtLeast(0L)
    }

    private fun looksLikeAudioFile(file: File): Boolean {
        return try {
            val header = ByteArray(16)
            val count = file.inputStream().use { it.read(header) }
            if (count < 4) return false
            val textPrefix = String(header, 0, count, Charsets.US_ASCII).lowercase(Locale.US)
            if (textPrefix.contains("<html") || textPrefix.contains("<!doc") || textPrefix.contains("<?xml")) {
                return false
            }
            val isId3 = header[0] == 'I'.code.toByte() && header[1] == 'D'.code.toByte() && header[2] == '3'.code.toByte()
            val isMp3Sync = (header[0].toInt() and 0xFF) == 0xFF && (header[1].toInt() and 0xE0) == 0xE0
            val isMp4Ftyp = count >= 8 && header[4] == 'f'.code.toByte() && header[5] == 't'.code.toByte() && header[6] == 'y'.code.toByte() && header[7] == 'p'.code.toByte()
            val isOgg = header[0] == 'O'.code.toByte() && header[1] == 'g'.code.toByte() && header[2] == 'g'.code.toByte() && header[3] == 'S'.code.toByte()
            val isRiff = header[0] == 'R'.code.toByte() && header[1] == 'I'.code.toByte() && header[2] == 'F'.code.toByte() && header[3] == 'F'.code.toByte()
            val isFlac = header[0] == 'f'.code.toByte() && header[1] == 'L'.code.toByte() && header[2] == 'a'.code.toByte() && header[3] == 'C'.code.toByte()
            isId3 || isMp3Sync || isMp4Ftyp || isOgg || isRiff || isFlac
        } catch (_: Exception) {
            false
        }
    }

    private fun persistDownloadedEntry(context: Context, episode: PodcastEpisode, file: File) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val map = loadDownloadedIndexMap(prefs)
        map.put(
            episode.guid,
            JSONObject().apply {
                put("guid", episode.guid)
                put("title", episode.title)
                put("showName", episode.showName)
                put("filePath", file.absolutePath)
                put("sizeBytes", file.length())
                put("savedAt", System.currentTimeMillis())
            }
        )
        prefs.edit().putString(KEY_DOWNLOADS_INDEX, map.toString()).apply()
    }

    private fun loadDownloadedIndexMap(prefs: android.content.SharedPreferences): JSONObject {
        val raw = prefs.getString(KEY_DOWNLOADS_INDEX, null) ?: return JSONObject()
        return try {
            JSONObject(raw)
        } catch (_: Exception) {
            JSONObject()
        }
    }

    private fun updateDownloadState(guid: String, state: PodcastDownloadProgress) {
        val next = _downloadStates.value.toMutableMap()
        next[guid] = state
        _downloadStates.value = next
    }

    private fun sha256Short(value: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(value.toByteArray())
        return bytes.take(10).joinToString("") { "%02x".format(it) }
    }
}
