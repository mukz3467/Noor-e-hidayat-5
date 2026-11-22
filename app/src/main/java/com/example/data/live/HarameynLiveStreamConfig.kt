package com.example.data.live

import android.content.Context
import android.util.Log
import com.example.data.model.AppLanguage
import com.example.data.repository.HarameynLocation
import com.example.data.repository.YouTubeRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.Locale

enum class LiveStreamSourceId {
    OFFICIAL_YOUTUBE,
    QURAN_HAYAT_HLS,
    HARAMAIN_VOICE
}

enum class LivePlayerTransport {
    YOUTUBE_VIDEO_EMBED,
    YOUTUBE_CHANNEL_LIVE_EMBED,
    HLS_NATIVE
}

enum class LiveConnectionPhase {
    CONNECTING,
    LIVE,
    RECONNECTING,
    OFFLINE,
    SOURCE_UNAVAILABLE
}

enum class LiveSourceAttemptStatus {
    PENDING,
    TESTING,
    ACTIVE_LIVE,
    FAILED
}

data class LiveStreamSourceConfig(
    val id: LiveStreamSourceId,
    val location: HarameynLocation,
    val priorityOrder: Int,
    val labelEn: String,
    val labelUr: String,
    val labelAr: String,
    val labelHi: String,
    val isRelayOfOfficialBroadcaster: Boolean,
    val officialChannelId: String,
    val officialHandle: String,
    val startingVideoId: String? = null,
    val directHlsUrl: String? = null,
    val expectedHlsLocationToken: String? = null,
    val sourcePageUrl: String
) {
    fun localizedLabel(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> labelEn
        AppLanguage.URDU -> labelUr
        AppLanguage.ARABIC -> labelAr
        AppLanguage.HINDI -> labelHi
    }
}

data class ResolvedLiveStreamSource(
    val config: LiveStreamSourceConfig,
    val transport: LivePlayerTransport,
    val youtubeVideoId: String? = null,
    val youtubeChannelId: String? = null,
    val hlsStreamUrl: String? = null,
    val resolvedTitle: String = "",
    val externalOpenUrl: String = config.sourcePageUrl,
    val isPreVerifiedLivePlaylist: Boolean = false
)

data class LiveSourceAttempt(
    val source: LiveStreamSourceConfig,
    val status: LiveSourceAttemptStatus,
    val detail: String = ""
)

data class SourceProbeOutcome(
    val resolved: ResolvedLiveStreamSource? = null,
    val failureReason: String? = null
)

/**
 * Location-isolated, verified live broadcast source configurations for Makkah (Masjid al-Haram)
 * and Madinah (Masjid an-Nabawi).
 *
 * Third-party relay sources (Quran Hayat, Haramain Voice) relay the official Saudi Broadcasting
 * Authority signal (Saudi Quran TV / Saudi Sunnah TV) and are explicitly labeled as relays,
 * never as independent cameras or broadcasters.
 */
object HarameynLiveSources {

    const val MAKKAH_CHANNEL_ID = "UCos52azQNBgW63_9uDJoPDA"
    const val MAKKAH_HANDLE = "@SaudiQuranTv"
    const val MAKKAH_STARTING_VIDEO_ID = "24JXS383N1c"
    const val MAKKAH_QURAN_HAYAT_HLS =
        "https://cdn-globecast.akamaized.net/live/eds/saudi_quran/hls_roku/index.m3u8"

    const val MADINAH_CHANNEL_ID = "UCROKYPep-UuODNwyipe6JMw"
    const val MADINAH_HANDLE = "@SaudiSunnahTv"
    const val MADINAH_STARTING_VIDEO_ID = "ge2Xn-Lwk3U"
    const val MADINAH_QURAN_HAYAT_HLS =
        "https://cdn-globecast.akamaized.net/live/eds/saudi_sunnah/hls_roku/index.m3u8"

    const val QURAN_HAYAT_PAGE_URL = "https://quranhayat.com/en/live"
    const val HARAMAIN_VOICE_PAGE_URL = "https://www.haramainvoice.com/en/live"

    val MAKKAH_SOURCES: List<LiveStreamSourceConfig> = listOf(
        LiveStreamSourceConfig(
            id = LiveStreamSourceId.OFFICIAL_YOUTUBE,
            location = HarameynLocation.MAKKAH,
            priorityOrder = 1,
            labelEn = "Saudi Quran TV (Official YouTube)",
            labelUr = "سعودی قرآن ٹی وی (آفیشل یوٹیوب)",
            labelAr = "قناة القرآن الكريم (يوتيوب الرسمي)",
            labelHi = "सऊदी कुरान टीवी (आधिकारिक यूट्यूब)",
            isRelayOfOfficialBroadcaster = false,
            officialChannelId = MAKKAH_CHANNEL_ID,
            officialHandle = MAKKAH_HANDLE,
            startingVideoId = MAKKAH_STARTING_VIDEO_ID,
            sourcePageUrl = "https://www.youtube.com/$MAKKAH_HANDLE"
        ),
        LiveStreamSourceConfig(
            id = LiveStreamSourceId.QURAN_HAYAT_HLS,
            location = HarameynLocation.MAKKAH,
            priorityOrder = 2,
            labelEn = "Quran Hayat (Official Saudi Quran TV Relay)",
            labelUr = "قرآن حیات (سرکاری سعودی قرآن ٹی وی ریلے)",
            labelAr = "قرآن حياة (بث رسمي لقناة القرآن الكريم)",
            labelHi = "कुरान हयात (आधिकारिक सऊदी कुरान टीवी रिले)",
            isRelayOfOfficialBroadcaster = true,
            officialChannelId = MAKKAH_CHANNEL_ID,
            officialHandle = MAKKAH_HANDLE,
            directHlsUrl = MAKKAH_QURAN_HAYAT_HLS,
            expectedHlsLocationToken = "saudi_quran",
            sourcePageUrl = QURAN_HAYAT_PAGE_URL
        ),
        LiveStreamSourceConfig(
            id = LiveStreamSourceId.HARAMAIN_VOICE,
            location = HarameynLocation.MAKKAH,
            priorityOrder = 3,
            labelEn = "Haramain Voice (Official Saudi Quran TV Relay)",
            labelUr = "حرمین وائس (سرکاری سعودی قرآن ٹی وی ریلے)",
            labelAr = "صوت الحرمين (بث رسمي لقناة القرآن الكريم)",
            labelHi = "हरमैन वॉइस (आधिकारिक सऊदी कुरान टीवी रिले)",
            isRelayOfOfficialBroadcaster = true,
            officialChannelId = MAKKAH_CHANNEL_ID,
            officialHandle = MAKKAH_HANDLE,
            sourcePageUrl = HARAMAIN_VOICE_PAGE_URL
        )
    )

    val MADINAH_SOURCES: List<LiveStreamSourceConfig> = listOf(
        LiveStreamSourceConfig(
            id = LiveStreamSourceId.OFFICIAL_YOUTUBE,
            location = HarameynLocation.MADINAH,
            priorityOrder = 1,
            labelEn = "Saudi Sunnah TV (Official YouTube)",
            labelUr = "سعودی سنہ ٹی وی (آفیشل یوٹیوب)",
            labelAr = "قناة السنة النبوية (يوتيوب الرسمي)",
            labelHi = "सऊदी सुन्नह टीवी (आधिकारिक यूट्यूब)",
            isRelayOfOfficialBroadcaster = false,
            officialChannelId = MADINAH_CHANNEL_ID,
            officialHandle = MADINAH_HANDLE,
            startingVideoId = MADINAH_STARTING_VIDEO_ID,
            sourcePageUrl = "https://www.youtube.com/$MADINAH_HANDLE"
        ),
        LiveStreamSourceConfig(
            id = LiveStreamSourceId.QURAN_HAYAT_HLS,
            location = HarameynLocation.MADINAH,
            priorityOrder = 2,
            labelEn = "Quran Hayat (Official Saudi Sunnah TV Relay)",
            labelUr = "قرآن حیات (سرکاری سعودی سنہ ٹی وی ریلے)",
            labelAr = "قرآن حياة (بث رسمي لقناة السنة النبوية)",
            labelHi = "कुरान हयात (आधिकारिक सऊदी सुन्नह टीवी रिले)",
            isRelayOfOfficialBroadcaster = true,
            officialChannelId = MADINAH_CHANNEL_ID,
            officialHandle = MADINAH_HANDLE,
            directHlsUrl = MADINAH_QURAN_HAYAT_HLS,
            expectedHlsLocationToken = "saudi_sunnah",
            sourcePageUrl = QURAN_HAYAT_PAGE_URL
        ),
        LiveStreamSourceConfig(
            id = LiveStreamSourceId.HARAMAIN_VOICE,
            location = HarameynLocation.MADINAH,
            priorityOrder = 3,
            labelEn = "Haramain Voice (Official Saudi Sunnah TV Relay)",
            labelUr = "حرمین وائس (سرکاری سعودی سنہ ٹی وی ریلے)",
            labelAr = "صوت الحرمين (بث رسمي لقناة السنة النبوية)",
            labelHi = "हरमैन वॉइस (आधिकारिक सऊदी सुन्नह टीवी रिले)",
            isRelayOfOfficialBroadcaster = true,
            officialChannelId = MADINAH_CHANNEL_ID,
            officialHandle = MADINAH_HANDLE,
            sourcePageUrl = HARAMAIN_VOICE_PAGE_URL
        )
    )

    fun sourcesFor(location: HarameynLocation): List<LiveStreamSourceConfig> = when (location) {
        HarameynLocation.MAKKAH -> MAKKAH_SOURCES
        HarameynLocation.MADINAH -> MADINAH_SOURCES
    }
}

/**
 * Remembers the user's selected Harameyn location (Makkah or Madinah) across navigation.
 */
object HarameynLiveStreamPrefs {
    private const val PREFS_NAME = "noor_harameyn_live_prefs_v1"
    private const val KEY_SELECTED_LOCATION = "selected_location"

    fun saveSelectedLocation(context: Context, location: HarameynLocation) {
        runCatching {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_SELECTED_LOCATION, location.name)
                .apply()
        }
    }

    fun loadSelectedLocation(context: Context): HarameynLocation {
        return runCatching {
            val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_SELECTED_LOCATION, null)
            HarameynLocation.entries.firstOrNull { it.name == raw } ?: HarameynLocation.MAKKAH
        }.getOrDefault(HarameynLocation.MAKKAH)
    }

    internal fun clearForTest(context: Context) {
        runCatching {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit()
        }
    }
}

/**
 * Validates and resolves playable live broadcast endpoints for Makkah & Madinah without
 * fabricating live status, scraping YouTube search/watch HTML, or mixing locations.
 */
object HarameynLiveSourceResolver {

    private const val TAG = "HarameynLiveResolver"
    private const val USER_AGENT =
        "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36"

    private val JSON_LD_M3U8_REGEX = Regex("""https://[^"'\s\\<>]+\.m3u8""")
    private val YOUTUBE_EMBED_ID_REGEX = Regex("""youtube\.com/embed/([A-Za-z0-9_-]{11})""")

    fun probeSource(
        source: LiveStreamSourceConfig,
        httpClient: OkHttpClient = YouTubeRepository.httpClient
    ): SourceProbeOutcome {
        return when (source.id) {
            LiveStreamSourceId.OFFICIAL_YOUTUBE -> probeOfficialYouTube(source, httpClient)
            LiveStreamSourceId.QURAN_HAYAT_HLS -> probeQuranHayatHls(source, httpClient)
            LiveStreamSourceId.HARAMAIN_VOICE -> probeHaramainVoice(source, httpClient)
        }
    }

    /**
     * Source 1: Official Saudi Quran TV / Saudi Sunnah TV on YouTube.
     *
     * Validates the configured starting video ID via official YouTube oEmbed.
     * - If oEmbed returns playable metadata (non-blank title + matching official channel handle),
     *   returns YOUTUBE_VIDEO_EMBED for that exact video ID.
     * - If oEmbed returns HTTP 200 JSON without title/author (stream session rotated/ended on YouTube),
     *   falls back to YouTube's official channel live embed (`embed/live_stream?channel=UC...`)
     *   so the real player verifies whether the channel's current active live broadcast is playable.
     * - If YouTube oEmbed fails (non-200, non-JSON, timeout), returns failure so fallback advances
     *   immediately to Source 2 (Quran Hayat HLS).
     */
    internal fun probeOfficialYouTube(
        source: LiveStreamSourceConfig,
        httpClient: OkHttpClient
    ): SourceProbeOutcome {
        val startingId = source.startingVideoId
        if (startingId.isNullOrBlank() || !YouTubeRepository.isValidVideoId(startingId)) {
            return SourceProbeOutcome(failureReason = "Official YouTube starting stream ID unavailable.")
        }

        val oEmbedUrl =
            "https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=$startingId&format=json"
        val response = fetchText(oEmbedUrl, httpClient)
        val body = response.body
            ?: return SourceProbeOutcome(
                failureReason = response.errorMessage ?: "Official YouTube endpoint unreachable."
            )

        val json = runCatching { JSONObject(body) }.getOrNull()
            ?: return SourceProbeOutcome(failureReason = "Official YouTube oEmbed returned invalid response.")

        if (!json.optString("provider_name").equals("YouTube", ignoreCase = true)) {
            return SourceProbeOutcome(failureReason = "Unexpected oEmbed provider response.")
        }

        val title = json.optString("title", "").trim()
        val authorName = json.optString("author_name", "").trim()
        val authorUrl = json.optString("author_url", "").trim()

        // Active playable YouTube live streams include title + author_name + matching official channel handle.
        if (title.isNotBlank() && authorName.isNotBlank() && matchesExpectedOfficialChannel(source, authorUrl, authorName)) {
            return SourceProbeOutcome(
                resolved = ResolvedLiveStreamSource(
                    config = source,
                    transport = LivePlayerTransport.YOUTUBE_VIDEO_EMBED,
                    youtubeVideoId = startingId,
                    youtubeChannelId = source.officialChannelId,
                    resolvedTitle = title,
                    externalOpenUrl = "https://www.youtube.com/watch?v=$startingId",
                    isPreVerifiedLivePlaylist = false
                )
            )
        }

        // Starting video session rotated/ended on YouTube -> use official YouTube channel live embed
        // which dynamically targets the channel's active live broadcast; player will confirm LIVE or fail over.
        return SourceProbeOutcome(
            resolved = ResolvedLiveStreamSource(
                config = source,
                transport = LivePlayerTransport.YOUTUBE_CHANNEL_LIVE_EMBED,
                youtubeVideoId = null,
                youtubeChannelId = source.officialChannelId,
                resolvedTitle = source.labelEn,
                externalOpenUrl = source.sourcePageUrl,
                isPreVerifiedLivePlaylist = false
            )
        )
    }

    /**
     * Source 2: Quran Hayat (`https://quranhayat.com/en/live`) — Official Saudi Broadcasting Authority
     * Globecast/Akamai HLS live stream (`saudi_quran` for Makkah, `saudi_sunnah` for Madinah).
     *
     * Verifies BOTH:
     * 1. Master playlist contains `#EXTM3U` and matches the requested mosque token (`saudi_quran` vs `saudi_sunnah`).
     * 2. Media variant playlist contains `#EXT-X-MEDIA-SEQUENCE` and does NOT contain `#EXT-X-ENDLIST`
     *    (proving a live sliding window, never a recorded VOD).
     */
    internal fun probeQuranHayatHls(
        source: LiveStreamSourceConfig,
        httpClient: OkHttpClient
    ): SourceProbeOutcome {
        val locationToken = source.expectedHlsLocationToken
            ?: return SourceProbeOutcome(failureReason = "Missing mosque HLS token.")

        val primaryUrl = source.directHlsUrl
        if (!primaryUrl.isNullOrBlank() && primaryUrl.contains(locationToken, ignoreCase = true)) {
            val directCheck = verifyLiveHlsPlaylist(primaryUrl, locationToken, httpClient)
            if (directCheck.isLiveSlidingWindow) {
                return SourceProbeOutcome(
                    resolved = ResolvedLiveStreamSource(
                        config = source,
                        transport = LivePlayerTransport.HLS_NATIVE,
                        hlsStreamUrl = primaryUrl,
                        resolvedTitle = source.labelEn,
                        externalOpenUrl = source.sourcePageUrl,
                        isPreVerifiedLivePlaylist = true
                    )
                )
            }
        }

        // Dynamic fallback: inspect Quran Hayat's live page JSON-LD strictly for the matching mosque HLS URL.
        val pageRes = fetchText(source.sourcePageUrl, httpClient)
        val pageBody = pageRes.body
            ?: return SourceProbeOutcome(
                failureReason = pageRes.errorMessage ?: "Quran Hayat live stream unavailable."
            )

        val discoveredUrl = extractQuranHayatHlsUrl(pageBody, locationToken)
            ?: return SourceProbeOutcome(
                failureReason = "Quran Hayat did not expose a verified ${source.location.name.lowercase(Locale.US)} HLS stream."
            )

        val dynamicCheck = verifyLiveHlsPlaylist(discoveredUrl, locationToken, httpClient)
        if (!dynamicCheck.isLiveSlidingWindow) {
            return SourceProbeOutcome(
                failureReason = dynamicCheck.errorReason ?: "Quran Hayat HLS playlist is not an active live window."
            )
        }

        return SourceProbeOutcome(
            resolved = ResolvedLiveStreamSource(
                config = source,
                transport = LivePlayerTransport.HLS_NATIVE,
                hlsStreamUrl = discoveredUrl,
                resolvedTitle = source.labelEn,
                externalOpenUrl = source.sourcePageUrl,
                isPreVerifiedLivePlaylist = true
            )
        )
    }

    /**
     * Source 3: Haramain Voice (`https://www.haramainvoice.com/en/live`).
     *
     * Note: Haramain Voice sets `X-Frame-Options: SAMEORIGIN`, so its webpage cannot be embedded
     * directly in an iframe. Instead, we inspect its structured stream config strictly for the
     * selected mosque (`makkah` vs `madinah`) to obtain its embedded official Saudi YouTube stream ID,
     * then verify via YouTube oEmbed that the stream is active and owned by the correct official channel.
     */
    internal fun probeHaramainVoice(
        source: LiveStreamSourceConfig,
        httpClient: OkHttpClient
    ): SourceProbeOutcome {
        val pageRes = fetchText(source.sourcePageUrl, httpClient)
        val pageBody = pageRes.body
            ?: return SourceProbeOutcome(
                failureReason = pageRes.errorMessage ?: "Haramain Voice page unreachable."
            )

        val extractedVideoId = extractHaramainVoiceVideoId(pageBody, source.location)
            ?: return SourceProbeOutcome(
                failureReason = "Haramain Voice did not provide a valid ${source.location.name.lowercase(Locale.US)} stream embed."
            )

        val oEmbedUrl =
            "https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=$extractedVideoId&format=json"
        val oEmbedRes = fetchText(oEmbedUrl, httpClient)
        val oEmbedBody = oEmbedRes.body
            ?: return SourceProbeOutcome(
                failureReason = oEmbedRes.errorMessage ?: "Haramain Voice stream verification failed."
            )

        val json = runCatching { JSONObject(oEmbedBody) }.getOrNull()
            ?: return SourceProbeOutcome(failureReason = "Invalid stream verification response.")

        val title = json.optString("title", "").trim()
        val authorName = json.optString("author_name", "").trim()
        val authorUrl = json.optString("author_url", "").trim()

        if (title.isBlank() || authorName.isBlank()) {
            return SourceProbeOutcome(
                failureReason = "Haramain Voice embedded stream is currently ended or unplayable."
            )
        }
        if (!matchesExpectedOfficialChannel(source, authorUrl, authorName)) {
            return SourceProbeOutcome(
                failureReason = "Haramain Voice stream does not match the official ${source.location.name.lowercase(Locale.US)} channel."
            )
        }
        if (!looksLikeLiveBroadcastTitle(title)) {
            return SourceProbeOutcome(
                failureReason = "Haramain Voice video appears to be a recorded upload rather than a live broadcast."
            )
        }

        return SourceProbeOutcome(
            resolved = ResolvedLiveStreamSource(
                config = source,
                transport = LivePlayerTransport.YOUTUBE_VIDEO_EMBED,
                youtubeVideoId = extractedVideoId,
                youtubeChannelId = source.officialChannelId,
                resolvedTitle = title,
                externalOpenUrl = source.sourcePageUrl,
                isPreVerifiedLivePlaylist = false
            )
        )
    }

    internal data class HlsPlaylistCheck(
        val isLiveSlidingWindow: Boolean,
        val errorReason: String? = null
    )

    internal fun verifyLiveHlsPlaylist(
        playlistUrl: String,
        expectedLocationToken: String,
        httpClient: OkHttpClient
    ): HlsPlaylistCheck {
        if (!playlistUrl.contains(expectedLocationToken, ignoreCase = true)) {
            return HlsPlaylistCheck(false, "HLS URL does not match requested mosque stream.")
        }
        val masterRes = fetchText(playlistUrl, httpClient)
        val masterBody = masterRes.body
            ?: return HlsPlaylistCheck(false, masterRes.errorMessage ?: "HLS master playlist unavailable.")

        if (!masterBody.contains("#EXTM3U")) {
            return HlsPlaylistCheck(false, "Response is not a valid HLS playlist.")
        }

        // If this is already a media playlist (contains segments / media sequence), evaluate directly.
        if (masterBody.contains("#EXT-X-MEDIA-SEQUENCE") || masterBody.contains("#EXTINF:")) {
            return evaluateMediaPlaylist(masterBody)
        }

        // Master playlist -> follow first variant media playlist to confirm live sliding window (no #EXT-X-ENDLIST).
        val variantPath = masterBody.lineSequence()
            .map { it.trim() }
            .firstOrNull { it.isNotEmpty() && !it.startsWith("#") }
            ?: return HlsPlaylistCheck(false, "HLS master playlist contains no playable variants.")

        val variantUrl = resolveRelativeUrl(playlistUrl, variantPath)
        val variantRes = fetchText(variantUrl, httpClient)
        val variantBody = variantRes.body
            ?: return HlsPlaylistCheck(false, variantRes.errorMessage ?: "HLS media variant unreachable.")

        return evaluateMediaPlaylist(variantBody)
    }

    private fun evaluateMediaPlaylist(body: String): HlsPlaylistCheck {
        if (!body.contains("#EXTM3U")) {
            return HlsPlaylistCheck(false, "HLS media variant is malformed.")
        }
        if (body.contains("#EXT-X-ENDLIST")) {
            return HlsPlaylistCheck(false, "HLS stream is a recorded VOD (#EXT-X-ENDLIST), not a live broadcast.")
        }
        val hasLiveSequence = body.contains("#EXT-X-MEDIA-SEQUENCE") && body.contains("#EXTINF:")
        return if (hasLiveSequence) {
            HlsPlaylistCheck(true)
        } else {
            HlsPlaylistCheck(false, "HLS variant does not contain active live segments.")
        }
    }

    internal fun extractQuranHayatHlsUrl(html: String, locationToken: String): String? {
        val unescaped = html.replace("\\/", "/").replace("\\u0026", "&")
        return JSON_LD_M3U8_REGEX.findAll(unescaped)
            .map { it.value.trimEnd('\\') }
            .firstOrNull { url ->
                url.contains(locationToken, ignoreCase = true) &&
                    url.endsWith(".m3u8", ignoreCase = true)
            }
    }

    /**
     * Extracts ONLY the YouTube embed video ID assigned to [location] (`makkah` or `madinah`)
     * from Haramain Voice's structured stream object so Makkah and Madinah are never confused.
     */
    internal fun extractHaramainVoiceVideoId(html: String, location: HarameynLocation): String? {
        val normalized = html
            .replace("\\\"", "\"")
            .replace("\\/", "/")
            .replace("\\u0026", "&")
        val locationKey = if (location == HarameynLocation.MAKKAH) "makkah" else "madinah"
        val blockPattern = Regex(
            """"$locationKey"\s*:\s*\{[^\}]{0,600}youtube\.com/embed/([A-Za-z0-9_-]{11})""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        )
        blockPattern.find(normalized)?.groupValues?.getOrNull(1)?.let { id ->
            if (YouTubeRepository.isValidVideoId(id)) return id
        }

        // Secondary structured iframe fallback matching explicit mosque title attribute ONLY
        val expectedIframeTitle = if (location == HarameynLocation.MAKKAH) "Grand Mosque" else "Prophet's Mosque"
        val iframeRegex = Regex("""<iframe[^>]*>""", RegexOption.IGNORE_CASE)
        for (match in iframeRegex.findAll(normalized)) {
            val tag = match.value
            if (tag.contains(expectedIframeTitle, ignoreCase = true)) {
                val id = YOUTUBE_EMBED_ID_REGEX.find(tag)?.groupValues?.getOrNull(1)
                if (id != null && YouTubeRepository.isValidVideoId(id)) return id
            }
        }
        return null
    }

    internal fun matchesExpectedOfficialChannel(
        source: LiveStreamSourceConfig,
        authorUrl: String,
        authorName: String
    ): Boolean {
        val handleNoAt = source.officialHandle.removePrefix("@")
        if (authorUrl.contains(handleNoAt, ignoreCase = true) ||
            authorUrl.contains(source.officialChannelId, ignoreCase = true)
        ) {
            return true
        }
        val lowerAuthor = authorName.lowercase(Locale.ROOT)
        return when (source.location) {
            HarameynLocation.MAKKAH ->
                lowerAuthor.contains("القرآن") || lowerAuthor.contains("القران") ||
                    lowerAuthor.contains("quran") || lowerAuthor.contains("makkah")
            HarameynLocation.MADINAH ->
                lowerAuthor.contains("السنة") || lowerAuthor.contains("السنه") ||
                    lowerAuthor.contains("sunnah") || lowerAuthor.contains("madinah")
        }
    }

    internal fun looksLikeLiveBroadcastTitle(title: String): Boolean {
        val lower = title.lowercase(Locale.ROOT)
        return lower.contains("live") ||
            title.contains("بث مباشر") ||
            title.contains("مباشر") ||
            lower.contains("direct")
    }

    private fun resolveRelativeUrl(baseUrl: String, relative: String): String {
        if (relative.startsWith("http://") || relative.startsWith("https://")) return relative
        val prefix = baseUrl.substringBeforeLast('/')
        return "$prefix/$relative"
    }

    private data class SimpleHttpResult(
        val body: String? = null,
        val errorMessage: String? = null,
        val httpCode: Int? = null
    )

    private fun fetchText(url: String, httpClient: OkHttpClient): SimpleHttpResult {
        return try {
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .header("Accept-Language", "en-US,en;q=0.9")
                .get()
                .build()
            httpClient.newCall(req).execute().use { resp ->
                if (resp.isSuccessful) {
                    SimpleHttpResult(body = resp.body?.string(), httpCode = resp.code)
                } else {
                    SimpleHttpResult(errorMessage = "HTTP ${resp.code}", httpCode = resp.code)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Live probe failed for $url: ${e::class.java.simpleName}")
            SimpleHttpResult(errorMessage = e::class.java.simpleName)
        }
    }
}
