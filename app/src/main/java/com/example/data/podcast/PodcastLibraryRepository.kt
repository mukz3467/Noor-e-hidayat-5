package com.example.data.podcast

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.data.lectures.YouTubeRssParser
import com.example.data.repository.YouTubeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.IOException
import java.io.StringReader
import java.net.URLEncoder
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Official YouTube Atom RSS + optional YouTube Data API v3 repository for the
 * Noor-e-Hidayat Multilingual Podcast Library.
 *
 * Guarantees:
 * - Zero fake videos, zero fabricated counts, zero invented channel IDs.
 * - Strict, non-bypassable [PodcastLongFormFilter] applied to RSS, API, and cached data.
 * - Graceful quota exhaustion handling (no wasted quota, preserves existing verified cache).
 * - Compact JSON persistence with 30-minute staleness threshold for low-RAM (4GB) devices.
 */
object PodcastLibraryRepository {

    private const val TAG = "PodcastLibraryRepo"
    private const val PREFS_NAME = "noor_podcast_library_v2"
    private const val KEY_SCHOLAR_CACHE_PREFIX = "scholar_cache_v2_"
    private const val KEY_LANG_SUGGESTIONS_PREFIX = "lang_suggestions_v2_"
    private const val KEY_LAST_LANGUAGE = "last_selected_podcast_lang"
    private const val KEY_ROTATION_INDEX = "bg_refresh_lang_rotation"
    private const val KEY_QUOTA_BLOCKED_UNTIL = "yt_api_quota_blocked_until_ms"

    const val STALE_CACHE_MS = 30L * 60L * 1000L // 30 minutes
    const val QUOTA_COOLDOWN_MS = 60L * 60L * 1000L // 60 minutes cooldown after 403 quotaExceeded
    private const val MAX_CONCURRENT_REQUESTS = 8
    private const val MAX_CACHED_VIDEOS_PER_SCHOLAR = 300
    const val MAX_CACHED_SUGGESTIONS_PER_LANG = 1500
    private const val CHANNELS_PER_SUGGESTIONS_BATCH = 45
    private const val ARCHIVE_DIR_NAME = "podcast_library_archives"
    private const val API_PAGE_SIZE = 50
    private const val TARGET_INITIAL_API_PAGES = 3

    private val defaultHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    @Volatile
    var httpClient: OkHttpClient = defaultHttpClient

    @Volatile
    var networkChecker: ((Context) -> Boolean)? = null

    @Volatile
    var currentTimeProvider: () -> Long = { System.currentTimeMillis() }

    /**
     * Optional official YouTube Data API v3 key provider.
     * Returns blank when no API key is configured, in which case the repository uses
     * zero-key official YouTube Atom RSS feeds cleanly without errors.
     */
    @Volatile
    var youtubeApiKeyProvider: () -> String = { resolveConfiguredApiKey() }

    private val memoryCache = ConcurrentHashMap<String, ScholarPodcastLoadResult>()
    private val suggestionsMemoryCache = ConcurrentHashMap<PodcastLanguage, LanguageSuggestionsLoadResult>()
    private val inFlightScholars = ConcurrentHashMap<String, Boolean>()

    private fun resolveConfiguredApiKey(): String {
        return runCatching {
            val clazz = Class.forName("com.example.BuildConfig")
            val field = clazz.getDeclaredField("YOUTUBE_API_KEY")
            (field.get(null) as? String)?.trim().orEmpty()
        }.getOrDefault("")
    }

    fun isOnline(context: Context): Boolean {
        networkChecker?.let { return it(context) }
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return true
            val net = cm.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(net) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Exception) {
            true
        }
    }

    fun getLastSelectedLanguage(context: Context): PodcastLanguage? {
        val id = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LAST_LANGUAGE, null)
        return PodcastLanguage.fromId(id)
    }

    fun saveLastSelectedLanguage(context: Context, language: PodcastLanguage?) {
        val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        if (language == null) {
            editor.remove(KEY_LAST_LANGUAGE)
        } else {
            editor.putString(KEY_LAST_LANGUAGE, language.id)
        }
        editor.apply()
    }

    fun isQuotaBlocked(context: Context): Boolean {
        val now = currentTimeProvider()
        val blockedUntil = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getLong(KEY_QUOTA_BLOCKED_UNTIL, 0L)
        return blockedUntil > now
    }

    private fun markQuotaBlocked(context: Context) {
        val until = currentTimeProvider() + QUOTA_COOLDOWN_MS
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putLong(KEY_QUOTA_BLOCKED_UNTIL, until)
            .apply()
    }

    fun isCacheStale(context: Context, scholarId: String): Boolean {
        val cached = memoryCache[scholarId] ?: readDiskCache(context, scholarId) ?: return true
        val ageMs = currentTimeProvider() - cached.lastUpdatedMs
        return cached.lastUpdatedMs <= 0L || ageMs >= STALE_CACHE_MS
    }

    fun getCachedVerifiedCount(context: Context, scholarId: String): Int {
        val mem = memoryCache[scholarId]
        if (mem != null) return mem.verifiedVideoCount
        val disk = readDiskCache(context, scholarId)
        return disk?.verifiedVideoCount ?: 0
    }

    fun getCachedCountsForLanguage(context: Context, language: PodcastLanguage): Map<String, Int> {
        val profiles = PodcastScholarRegistry.profilesForLanguage(language)
        val map = LinkedHashMap<String, Int>(profiles.size)
        for (p in profiles) {
            val count = getCachedVerifiedCount(context, p.id)
            if (count > 0) {
                map[p.id] = count
            }
        }
        return map
    }

    /**
     * Loads verified long-form videos for a single scholar or channel profile.
     *
     * - If the scholar has no verified sources -> returns [PodcastFeedStatus.UNAVAILABLE_NO_SOURCES].
     * - If offline -> serves strictly re-filtered cache ([PodcastFeedStatus.OFFLINE_CACHE]) or honest error.
     * - If fresh cache (< 30 min) exists and [forceRefresh] & [loadNextPage] are false -> serves cache immediately.
     * - Fetches official Atom RSS and (when YouTube Data API key is configured and quota allows)
     *   official YouTube Data API v3 paginated long-form results.
     * - Every video ALWAYS passes through [PodcastLongFormFilter.filterAndDeduplicate].
     */
    suspend fun loadScholarProfile(
        context: Context,
        scholar: PodcastScholarProfile,
        forceRefresh: Boolean = false,
        loadNextPage: Boolean = false
    ): ScholarPodcastLoadResult = withContext(Dispatchers.IO) {
        if (!scholar.isAvailable || scholar.sources.isEmpty()) {
            return@withContext ScholarPodcastLoadResult(
                scholar = scholar,
                videos = emptyList(),
                status = PodcastFeedStatus.UNAVAILABLE_NO_SOURCES,
                errorMessage = scholar.verificationNote.ifBlank {
                    "No verified official YouTube channel is currently available for this scholar."
                }
            )
        }

        val cached = memoryCache[scholar.id] ?: readDiskCache(context, scholar.id)
        val now = currentTimeProvider()

        if (!forceRefresh && !loadNextPage && cached != null && cached.lastUpdatedMs > 0L) {
            val ageMs = now - cached.lastUpdatedMs
            if (ageMs < STALE_CACHE_MS) {
                val revalidated = revalidateCachedVideos(context, cached, scholar)
                memoryCache[scholar.id] = revalidated
                return@withContext revalidated.copy(status = PodcastFeedStatus.CACHED_FRESH)
            }
        }

        if (!isOnline(context)) {
            return@withContext if (cached != null) {
                val revalidated = revalidateCachedVideos(context, cached, scholar)
                memoryCache[scholar.id] = revalidated
                revalidated.copy(status = PodcastFeedStatus.OFFLINE_CACHE)
            } else {
                ScholarPodcastLoadResult(
                    scholar = scholar,
                    videos = emptyList(),
                    status = PodcastFeedStatus.ERROR_NO_CACHE,
                    errorMessage = "Offline — no cached verified podcasts yet for ${scholar.displayNameEn}."
                )
            }
        }

        // Prevent duplicate concurrent fetches for the same scholar unless explicitly loading next page
        if (!loadNextPage && inFlightScholars.putIfAbsent(scholar.id, true) != null) {
            if (cached != null) {
                return@withContext revalidateCachedVideos(context, cached, scholar)
            }
        }

        try {
            val semaphore = Semaphore(MAX_CONCURRENT_REQUESTS)
            val allCandidates = mutableListOf<PodcastCandidateVideo>()
            var anyNetworkSucceeded = false
            var quotaExceeded = false
            var nextRemoteToken: String? = null
            var lastError: String? = null

            val apiKey = youtubeApiKeyProvider().trim()
            val canUseApi = apiKey.isNotBlank() && !isQuotaBlocked(context)

            if (loadNextPage && cached?.nextPageToken != null && canUseApi) {
                // Paginate further via official YouTube Data API v3 on the primary source
                val primarySource = scholar.sources.first()
                when (val apiOutcome = fetchApiPageForSource(primarySource, apiKey, cached.nextPageToken)) {
                    is ApiFetchOutcome.Success -> {
                        anyNetworkSucceeded = true
                        allCandidates.addAll(apiOutcome.candidates)
                        nextRemoteToken = apiOutcome.nextPageToken
                    }
                    is ApiFetchOutcome.QuotaExceeded -> {
                        quotaExceeded = true
                        markQuotaBlocked(context)
                        lastError = apiOutcome.message
                    }
                    is ApiFetchOutcome.Failure -> {
                        lastError = apiOutcome.message
                    }
                }
            } else {
                // 1. Always fetch official YouTube Atom RSS feeds (zero API quota cost)
                val rssOutcomes = coroutineScope {
                    scholar.sources.map { source ->
                        async {
                            semaphore.withPermit {
                                fetchRssCandidatesForSource(context, source)
                            }
                        }
                    }.awaitAll()
                }
                for (outcome in rssOutcomes) {
                    if (outcome.succeeded) {
                        anyNetworkSucceeded = true
                        allCandidates.addAll(outcome.candidates)
                    } else if (outcome.errorMessage != null) {
                        lastError = outcome.errorMessage
                    }
                }

                // 2. When official YouTube Data API v3 key is configured and quota allows,
                //    enrich with exact ISO-8601 duration, viewCount, and multi-page history (up to 100+)
                if (canUseApi) {
                    val primarySource = scholar.sources.first()
                    var pageToken: String? = null
                    var pagesFetched = 0
                    while (pagesFetched < TARGET_INITIAL_API_PAGES) {
                        when (val apiOutcome = fetchApiPageForSource(primarySource, apiKey, pageToken)) {
                            is ApiFetchOutcome.Success -> {
                                anyNetworkSucceeded = true
                                allCandidates.addAll(apiOutcome.candidates)
                                pageToken = apiOutcome.nextPageToken
                                nextRemoteToken = pageToken
                                pagesFetched++
                                if (pageToken == null) break
                            }
                            is ApiFetchOutcome.QuotaExceeded -> {
                                quotaExceeded = true
                                markQuotaBlocked(context)
                                lastError = apiOutcome.message
                                break
                            }
                            is ApiFetchOutcome.Failure -> {
                                lastError = apiOutcome.message
                                break
                            }
                        }
                    }
                }
            }

            // Enrich any candidate missing duration with player-verified duration cache
            val enrichedCandidates = allCandidates.map { c ->
                val playerDur = YouTubeRepository.getPlayerVerifiedDuration(context, c.videoId)
                if (c.durationSeconds == null && playerDur != null) {
                    c.copy(durationSeconds = playerDur)
                } else {
                    c
                }
            }

            // Merge with previously cached verified videos so pagination & history persist cleanly
            // while STILL re-validating every single cached item through PodcastLongFormFilter!
            val mergedCandidates = mutableListOf<PodcastCandidateVideo>()
            mergedCandidates.addAll(enrichedCandidates)
            if (cached != null) {
                for (old in cached.videos) {
                    val playerDur = YouTubeRepository.getPlayerVerifiedDuration(context, old.videoId)
                    mergedCandidates.add(
                        PodcastCandidateVideo(
                            videoId = old.videoId,
                            title = old.title,
                            description = old.description,
                            channelId = old.channelId,
                            channelName = old.channelName,
                            videoUrl = old.videoUrl,
                            thumbnailUrl = old.thumbnailUrl,
                            publishedIso = old.publishedIso,
                            publishedDisplay = old.publishedDisplay,
                            publishedEpochMs = old.publishedEpochMs,
                            durationSeconds = playerDur ?: old.durationSeconds,
                            viewCount = old.viewCount,
                            sourceKind = old.sourceKind,
                            requiresScholarAttribution = old.sourceKind == PodcastSourceKind.HOSTED_INTERVIEW
                        )
                    )
                }
            }

            val (verifiedVideos, rejections) = PodcastLongFormFilter.filterAndDeduplicate(
                candidates = mergedCandidates,
                profile = scholar
            )
            val rejectedTotal = rejections.values.sum()

            val resultStatus = when {
                quotaExceeded && (verifiedVideos.isNotEmpty() || cached != null) ->
                    PodcastFeedStatus.QUOTA_EXCEEDED_CACHE
                anyNetworkSucceeded ->
                    PodcastFeedStatus.ONLINE_FRESH
                cached != null ->
                    PodcastFeedStatus.OFFLINE_CACHE
                else ->
                    PodcastFeedStatus.ERROR_NO_CACHE
            }

            val finalTimestamp = if (anyNetworkSucceeded) now else (cached?.lastUpdatedMs ?: 0L)
            val finalResult = ScholarPodcastLoadResult(
                scholar = scholar,
                videos = verifiedVideos.take(MAX_CACHED_VIDEOS_PER_SCHOLAR),
                status = resultStatus,
                rejectedCount = rejectedTotal,
                rejectionBreakdown = rejections,
                lastUpdatedMs = finalTimestamp,
                nextPageToken = nextRemoteToken ?: (if (!loadNextPage) cached?.nextPageToken else null),
                hasMoreRemotePages = (nextRemoteToken != null) && !quotaExceeded && apiKey.isNotBlank(),
                errorMessage = if (resultStatus == PodcastFeedStatus.ERROR_NO_CACHE || quotaExceeded) lastError else null
            )

            if (anyNetworkSucceeded || finalResult.videos.isNotEmpty()) {
                memoryCache[scholar.id] = finalResult
                saveDiskCache(context, finalResult)
            }
            finalResult
        } finally {
            inFlightScholars.remove(scholar.id)
        }
    }

    /**
     * Records a player-measured duration and immediately re-filters the scholar's cached videos.
     * If a video turns out to be shorter than [PodcastLongFormFilter.DEFAULT_MIN_DURATION_SECONDS],
     * it is purged from both memory and disk cache immediately.
     */
    fun reapplyPlayerDuration(
        context: Context,
        scholar: PodcastScholarProfile,
        videoId: String,
        durationSeconds: Int
    ): ScholarPodcastLoadResult? {
        if (durationSeconds <= 0) return memoryCache[scholar.id]
        YouTubeRepository.recordPlayerVerifiedDuration(context, videoId, durationSeconds)
        val existing = memoryCache[scholar.id] ?: readDiskCache(context, scholar.id) ?: return null
        val revalidated = revalidateCachedVideos(context, existing, scholar)
        memoryCache[scholar.id] = revalidated
        saveDiskCache(context, revalidated)
        return revalidated
    }

    /**
     * Searches within verified videos locally while re-enforcing [PodcastLongFormFilter]
     * so Shorts or invalid entries can never leak through search paths.
     */
    fun searchVerifiedVideos(
        videos: List<VerifiedPodcastVideo>,
        query: String,
        profile: PodcastScholarProfile? = null
    ): List<VerifiedPodcastVideo> {
        val clean = query.trim().lowercase(Locale.ROOT)
        val filtered = videos.filter { video ->
            val stillValid = !video.videoUrl.contains("/shorts/", ignoreCase = true) &&
                (video.durationSeconds == null || video.durationSeconds >= PodcastLongFormFilter.DEFAULT_MIN_DURATION_SECONDS) &&
                (profile == null || video.language == profile.language)
            if (!stillValid) return@filter false
            if (clean.isBlank()) return@filter true
            video.title.lowercase(Locale.ROOT).contains(clean) ||
                video.scholarName.lowercase(Locale.ROOT).contains(clean) ||
                video.channelName.lowercase(Locale.ROOT).contains(clean) ||
                video.description.lowercase(Locale.ROOT).contains(clean)
        }
        return filtered.distinctBy { it.videoId }
    }

    /**
     * Refreshes up to [maxScholars] stale scholars for a language section.
     * Used by [PodcastRefreshWorker] for low-memory, battery-friendly ~30-minute background updates.
     */
    suspend fun refreshStaleLanguageBatch(
        context: Context,
        language: PodcastLanguage? = null,
        maxScholars: Int = 4
    ): Int = withContext(Dispatchers.IO) {
        if (!isOnline(context)) return@withContext 0
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val targetLang = language ?: getLastSelectedLanguage(context) ?: run {
            val idx = prefs.getInt(KEY_ROTATION_INDEX, 0)
            val langs = PodcastLanguage.entries
            val chosen = langs[idx % langs.size]
            prefs.edit().putInt(KEY_ROTATION_INDEX, (idx + 1) % langs.size).apply()
            chosen
        }

        val candidates = PodcastScholarRegistry.profilesForLanguage(targetLang)
            .filter { it.isAvailable }
            .filter { isCacheStale(context, it.id) }
            .take(maxScholars)

        var refreshedCount = 0
        // Refresh language-level 100+ long-form suggestions feed if stale
        if (isLanguageSuggestionsStale(context, targetLang)) {
            val langRes = loadLanguageSuggestions(context, targetLang, forceRefresh = true, loadNextBatch = false)
            if (langRes.status == PodcastFeedStatus.ONLINE_FRESH) {
                refreshedCount++
            }
        }
        for (scholar in candidates) {
            val res = loadScholarProfile(context, scholar, forceRefresh = true, loadNextPage = false)
            if (res.status == PodcastFeedStatus.ONLINE_FRESH) {
                refreshedCount++
            }
        }
        return@withContext refreshedCount
    }

    /**
     * Sweeps fresh verified channel batches across ALL 3 languages (Urdu, English, Arabic —
     * 349 verified channels total) every ~30 minutes, cumulatively merging newly published
     * real long-form videos into compact per-language archives (up to 1,500 per language /
     * 4,500 total across Urdu, English, and Arabic; strictly zero Shorts).
     */
    suspend fun refreshAllLanguagesCumulative(
        context: Context,
        sweepAllBatches: Boolean = false
    ): Int = withContext(Dispatchers.IO) {
        if (!isOnline(context)) return@withContext 0
        var totalAccumulated = 0
        for (lang in PodcastLanguage.entries) {
            var res = loadLanguageSuggestions(
                context = context,
                language = lang,
                forceRefresh = true,
                loadNextBatch = false
            )
            if (sweepAllBatches) {
                while (res.hasMoreChannelBatches) {
                    res = loadLanguageSuggestions(
                        context = context,
                        language = lang,
                        forceRefresh = false,
                        loadNextBatch = true
                    )
                }
            } else if (res.hasMoreChannelBatches) {
                // Advance one rotating batch per 30-min cycle so all 114-120 channels per language stay fresh
                res = loadLanguageSuggestions(
                    context = context,
                    language = lang,
                    forceRefresh = false,
                    loadNextBatch = true
                )
            }
            totalAccumulated += res.verifiedVideoCount
        }
        totalAccumulated
    }

    fun getTotalAccumulatedPodcastVideos(context: Context): Int {
        return PodcastLanguage.entries.sumOf { lang ->
            val cached = suggestionsMemoryCache[lang] ?: readLanguageSuggestionsCache(context, lang)
            cached?.verifiedVideoCount ?: 0
        }
    }

    fun isLanguageSuggestionsStale(context: Context, language: PodcastLanguage): Boolean {
        val cached = suggestionsMemoryCache[language] ?: readLanguageSuggestionsCache(context, language)
            ?: return true
        val ageMs = currentTimeProvider() - cached.lastUpdatedMs
        return cached.lastUpdatedMs <= 0L || ageMs >= STALE_CACHE_MS
    }

    /**
     * Loads 100+ real, verified long-form podcast & lecture video suggestions for a single
     * [PodcastLanguage] across the 110+ verified channels in that language (350+ channels total).
     *
     * - Strictly language-isolated (Urdu channels for Urdu, English for English, Arabic for Arabic).
     * - Fetches in bounded batches of 35 priority-ordered channels (`Semaphore(6)`), filtering
     *   every single entry through [PodcastLongFormFilter] (zero Shorts, zero clips).
     * - Supports [loadNextBatch] to pull the next 35 channels from the 110+ verified channel pool.
     */
    suspend fun loadLanguageSuggestions(
        context: Context,
        language: PodcastLanguage,
        forceRefresh: Boolean = false,
        loadNextBatch: Boolean = false
    ): LanguageSuggestionsLoadResult = withContext(Dispatchers.IO) {
        val allChannels = PodcastCatalogBridge.channelsForLanguage(context, language)
        val totalChannels = allChannels.size
        val cached = suggestionsMemoryCache[language] ?: readLanguageSuggestionsCache(context, language)
        val now = currentTimeProvider()

        if (!forceRefresh && !loadNextBatch && cached != null && cached.lastUpdatedMs > 0L) {
            val ageMs = now - cached.lastUpdatedMs
            if (ageMs < STALE_CACHE_MS && cached.videos.isNotEmpty()) {
                val revalidated = revalidateCachedLanguageSuggestions(context, cached, allChannels)
                suggestionsMemoryCache[language] = revalidated
                return@withContext revalidated.copy(
                    status = PodcastFeedStatus.CACHED_FRESH,
                    totalVerifiedChannelsInLanguage = totalChannels
                )
            }
        }

        if (!isOnline(context)) {
            return@withContext if (cached != null && cached.videos.isNotEmpty()) {
                val revalidated = revalidateCachedLanguageSuggestions(context, cached, allChannels)
                suggestionsMemoryCache[language] = revalidated
                revalidated.copy(
                    status = PodcastFeedStatus.OFFLINE_CACHE,
                    totalVerifiedChannelsInLanguage = totalChannels
                )
            } else {
                LanguageSuggestionsLoadResult(
                    language = language,
                    videos = emptyList(),
                    status = PodcastFeedStatus.ERROR_NO_CACHE,
                    totalVerifiedChannelsInLanguage = totalChannels,
                    errorMessage = "Offline — no cached verified long-form videos yet."
                )
            }
        }

        val startOffset = if (loadNextBatch && cached != null) {
            cached.nextBatchOffset.coerceIn(0, totalChannels)
        } else {
            0
        }
        val batchChannels = allChannels.drop(startOffset).take(CHANNELS_PER_SUGGESTIONS_BATCH)
        val nextOffset = (startOffset + batchChannels.size).coerceAtMost(totalChannels)

        val semaphore = Semaphore(MAX_CONCURRENT_REQUESTS)
        val freshPairs = mutableListOf<Pair<PodcastCandidateVideo, PodcastCatalogChannelEntry>>()
        var anySucceeded = false
        var lastError: String? = null

        val outcomes = coroutineScope {
            batchChannels.map { entry ->
                async {
                    semaphore.withPermit {
                        val outcome = fetchRssCandidatesForSource(context, entry.toChannelSource())
                        entry to outcome
                    }
                }
            }.awaitAll()
        }

        for ((entry, outcome) in outcomes) {
            if (outcome.succeeded) {
                anySucceeded = true
                for (cand in outcome.candidates) {
                    val playerDur = YouTubeRepository.getPlayerVerifiedDuration(context, cand.videoId)
                    val enriched = if (cand.durationSeconds == null && playerDur != null) {
                        cand.copy(durationSeconds = playerDur)
                    } else {
                        cand
                    }
                    freshPairs.add(enriched to entry)
                }
            } else if (outcome.errorMessage != null) {
                lastError = outcome.errorMessage
            }
        }

        // Merge with existing cached videos when paginating (still re-validating every item!)
        val combinedPairs = mutableListOf<Pair<PodcastCandidateVideo, PodcastCatalogChannelEntry>>()
        combinedPairs.addAll(freshPairs)
        if (cached != null) {
            val channelById = allChannels.associateBy { it.channelId.lowercase(Locale.ROOT) }
            for (old in cached.videos) {
                val entry = channelById[old.channelId.lowercase(Locale.ROOT)] ?: PodcastCatalogChannelEntry(
                    channelId = old.channelId,
                    handle = "",
                    nameNative = old.scholarName,
                    nameEn = old.channelName,
                    language = language,
                    priority = 10,
                    sourceKind = old.sourceKind,
                    scholarId = old.scholarId,
                    scholarName = old.scholarName,
                    requiresScholarAttribution = old.sourceKind == PodcastSourceKind.HOSTED_INTERVIEW
                )
                val playerDur = YouTubeRepository.getPlayerVerifiedDuration(context, old.videoId)
                combinedPairs.add(
                    PodcastCandidateVideo(
                        videoId = old.videoId,
                        title = old.title,
                        description = old.description,
                        channelId = old.channelId,
                        channelName = old.channelName,
                        videoUrl = old.videoUrl,
                        thumbnailUrl = old.thumbnailUrl,
                        publishedIso = old.publishedIso,
                        publishedDisplay = old.publishedDisplay,
                        publishedEpochMs = old.publishedEpochMs,
                        durationSeconds = playerDur ?: old.durationSeconds,
                        viewCount = old.viewCount,
                        sourceKind = old.sourceKind,
                        requiresScholarAttribution = old.sourceKind == PodcastSourceKind.HOSTED_INTERVIEW
                    ) to entry
                )
            }
        }

        val (verifiedVideos, rejections) = PodcastLongFormFilter.filterCatalogBatch(
            candidatesWithChannel = combinedPairs,
            language = language
        )
        val rejectedTotal = rejections.values.sum()

        val status = when {
            anySucceeded -> PodcastFeedStatus.ONLINE_FRESH
            cached != null && verifiedVideos.isNotEmpty() -> PodcastFeedStatus.OFFLINE_CACHE
            else -> PodcastFeedStatus.ERROR_NO_CACHE
        }

        val result = LanguageSuggestionsLoadResult(
            language = language,
            videos = verifiedVideos.take(MAX_CACHED_SUGGESTIONS_PER_LANG),
            status = status,
            totalVerifiedChannelsInLanguage = totalChannels,
            channelsFetchedCount = maxOf(nextOffset, cached?.channelsFetchedCount ?: 0),
            nextBatchOffset = nextOffset,
            hasMoreChannelBatches = nextOffset < totalChannels,
            rejectedCount = rejectedTotal,
            lastUpdatedMs = if (anySucceeded) now else (cached?.lastUpdatedMs ?: 0L),
            errorMessage = if (status == PodcastFeedStatus.ERROR_NO_CACHE) lastError else null
        )

        if (anySucceeded || result.videos.isNotEmpty()) {
            suggestionsMemoryCache[language] = result
            saveLanguageSuggestionsCache(context, result)
        }
        result
    }

    fun reapplyPlayerDurationToLanguage(
        context: Context,
        language: PodcastLanguage,
        videoId: String,
        durationSeconds: Int
    ): LanguageSuggestionsLoadResult? {
        if (durationSeconds <= 0) return suggestionsMemoryCache[language]
        YouTubeRepository.recordPlayerVerifiedDuration(context, videoId, durationSeconds)
        val existing = suggestionsMemoryCache[language] ?: readLanguageSuggestionsCache(context, language) ?: return null
        val allChannels = PodcastCatalogBridge.channelsForLanguage(context, language)
        val revalidated = revalidateCachedLanguageSuggestions(context, existing, allChannels)
        suggestionsMemoryCache[language] = revalidated
        saveLanguageSuggestionsCache(context, revalidated)
        return revalidated
    }

    private fun revalidateCachedLanguageSuggestions(
        context: Context,
        cached: LanguageSuggestionsLoadResult,
        allChannels: List<PodcastCatalogChannelEntry>
    ): LanguageSuggestionsLoadResult {
        val channelById = allChannels.associateBy { it.channelId.lowercase(Locale.ROOT) }
        val pairs = cached.videos.map { v ->
            val entry = channelById[v.channelId.lowercase(Locale.ROOT)] ?: PodcastCatalogChannelEntry(
                channelId = v.channelId,
                handle = "",
                nameNative = v.scholarName,
                nameEn = v.channelName,
                language = cached.language,
                priority = 10,
                sourceKind = v.sourceKind,
                scholarId = v.scholarId,
                scholarName = v.scholarName,
                requiresScholarAttribution = v.sourceKind == PodcastSourceKind.HOSTED_INTERVIEW
            )
            val playerDur = YouTubeRepository.getPlayerVerifiedDuration(context, v.videoId)
            PodcastCandidateVideo(
                videoId = v.videoId,
                title = v.title,
                description = v.description,
                channelId = v.channelId,
                channelName = v.channelName,
                videoUrl = v.videoUrl,
                thumbnailUrl = v.thumbnailUrl,
                publishedIso = v.publishedIso,
                publishedDisplay = v.publishedDisplay,
                publishedEpochMs = v.publishedEpochMs,
                durationSeconds = playerDur ?: v.durationSeconds,
                viewCount = v.viewCount,
                sourceKind = v.sourceKind,
                requiresScholarAttribution = v.sourceKind == PodcastSourceKind.HOSTED_INTERVIEW
            ) to entry
        }
        val (verified, rejections) = PodcastLongFormFilter.filterCatalogBatch(pairs, cached.language)
        return cached.copy(
            videos = verified.map { it.copy(isFromCache = true) },
            rejectedCount = maxOf(cached.rejectedCount, rejections.values.sum())
        )
    }

    private fun archiveFileForLanguage(context: Context, language: PodcastLanguage): File {
        val dir = File(context.filesDir, ARCHIVE_DIR_NAME)
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "lang_${language.id}.json")
    }

    private fun saveLanguageSuggestionsCache(context: Context, result: LanguageSuggestionsLoadResult) {
        try {
            val root = JSONObject()
            root.put("lastUpdatedMs", result.lastUpdatedMs)
            root.put("rejectedCount", result.rejectedCount)
            root.put("channelsFetchedCount", result.channelsFetchedCount)
            root.put("nextBatchOffset", result.nextBatchOffset)
            val arr = JSONArray()
            result.videos.take(MAX_CACHED_SUGGESTIONS_PER_LANG).forEach { v ->
                val obj = JSONObject()
                obj.put("id", v.videoId)
                obj.put("t", v.title)
                obj.put("d", v.description.take(180))
                obj.put("sid", v.scholarId)
                obj.put("sn", v.scholarName)
                obj.put("cid", v.channelId)
                obj.put("cn", v.channelName)
                obj.put("th", v.thumbnailUrl)
                obj.put("u", v.videoUrl)
                obj.put("pi", v.publishedIso)
                obj.put("pd", v.publishedDisplay)
                obj.put("pe", v.publishedEpochMs)
                if (v.durationSeconds != null) obj.put("dur", v.durationSeconds)
                if (v.viewCount != null) obj.put("vc", v.viewCount)
                obj.put("sk", v.sourceKind.wireValue)
                arr.put(obj)
            }
            root.put("videos", arr)
            val target = archiveFileForLanguage(context, result.language)
            val tmp = File(target.parentFile, "${target.name}.tmp")
            tmp.writeText(root.toString(), Charsets.UTF_8)
            if (!tmp.renameTo(target)) {
                target.writeText(root.toString(), Charsets.UTF_8)
                tmp.delete()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed saving language suggestions archive: ${e.message}")
        }
    }

    private fun readLanguageSuggestionsCache(
        context: Context,
        language: PodcastLanguage
    ): LanguageSuggestionsLoadResult? {
        val file = archiveFileForLanguage(context, language)
        val raw = if (file.exists()) {
            runCatching { file.readText(Charsets.UTF_8) }.getOrNull()
        } else {
            // Backward-compatible fallback if an older SharedPreferences entry exists
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString("$KEY_LANG_SUGGESTIONS_PREFIX${language.id}", null)
        } ?: return null
        return try {
            val allChannels = PodcastCatalogBridge.channelsForLanguage(context, language)
            val channelById = allChannels.associateBy { it.channelId.lowercase(Locale.ROOT) }
            val root = JSONObject(raw)
            val updatedMs = root.optLong("lastUpdatedMs", 0L)
            val rejectedCount = root.optInt("rejectedCount", 0)
            val fetchedCount = root.optInt("channelsFetchedCount", 0)
            val nextOffset = root.optInt("nextBatchOffset", fetchedCount)
            val arr = root.optJSONArray("videos") ?: JSONArray()
            val pairs = mutableListOf<Pair<PodcastCandidateVideo, PodcastCatalogChannelEntry>>()
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val vid = obj.optString("id")
                val cid = obj.optString("cid")
                val sid = obj.optString("sid")
                val sn = obj.optString("sn")
                val cn = obj.optString("cn")
                val sk = PodcastSourceKind.fromWire(obj.optString("sk"))
                val dur = if (obj.has("dur")) obj.optInt("dur") else null
                val vc = if (obj.has("vc")) obj.optLong("vc") else null
                val playerDur = YouTubeRepository.getPlayerVerifiedDuration(context, vid)
                val entry = channelById[cid.lowercase(Locale.ROOT)] ?: PodcastCatalogChannelEntry(
                    channelId = cid,
                    handle = "",
                    nameNative = sn.ifBlank { cn },
                    nameEn = cn,
                    language = language,
                    priority = 10,
                    sourceKind = sk,
                    scholarId = sid,
                    scholarName = sn.ifBlank { cn },
                    requiresScholarAttribution = sk == PodcastSourceKind.HOSTED_INTERVIEW
                )
                pairs.add(
                    PodcastCandidateVideo(
                        videoId = vid,
                        title = obj.optString("t"),
                        description = obj.optString("d"),
                        channelId = cid,
                        channelName = cn,
                        videoUrl = obj.optString("u"),
                        thumbnailUrl = obj.optString("th"),
                        publishedIso = obj.optString("pi"),
                        publishedDisplay = obj.optString("pd"),
                        publishedEpochMs = obj.optLong("pe", 0L),
                        durationSeconds = playerDur ?: dur,
                        viewCount = vc,
                        sourceKind = sk,
                        requiresScholarAttribution = sk == PodcastSourceKind.HOSTED_INTERVIEW
                    ) to entry
                )
            }
            val (verified, rejections) = PodcastLongFormFilter.filterCatalogBatch(pairs, language)
            LanguageSuggestionsLoadResult(
                language = language,
                videos = verified.map { it.copy(isFromCache = true) },
                status = PodcastFeedStatus.CACHED_FRESH,
                totalVerifiedChannelsInLanguage = allChannels.size,
                channelsFetchedCount = fetchedCount,
                nextBatchOffset = nextOffset,
                hasMoreChannelBatches = nextOffset < allChannels.size,
                rejectedCount = maxOf(rejectedCount, rejections.values.sum()),
                lastUpdatedMs = updatedMs
            )
        } catch (e: Exception) {
            Log.w(TAG, "Failed reading language suggestions cache: ${e.message}")
            null
        }
    }

    private data class RssFetchOutcome(
        val succeeded: Boolean,
        val candidates: List<PodcastCandidateVideo>,
        val errorMessage: String? = null
    )

    private fun fetchRssCandidatesForSource(
        context: Context,
        source: PodcastChannelSource
    ): RssFetchOutcome {
        return try {
            val req = Request.Builder()
                .url(source.rssUrl)
                .header("User-Agent", "NoorEHidayat/5.0 (Android; Official Podcast RSS)")
                .build()
            httpClient.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    return RssFetchOutcome(false, emptyList(), "HTTP ${resp.code} from RSS feed")
                }
                val xml = resp.body?.string().orEmpty()
                if (xml.isBlank()) {
                    return RssFetchOutcome(false, emptyList(), "Empty RSS response")
                }
                val candidates = parseAtomFeedToCandidates(context, xml, source)
                RssFetchOutcome(true, candidates)
            }
        } catch (e: IOException) {
            Log.w(TAG, "RSS fetch failed for ${source.channelId}: ${e.message}")
            RssFetchOutcome(false, emptyList(), e.message ?: "Network error")
        } catch (e: Exception) {
            Log.w(TAG, "Unexpected RSS parse error for ${source.channelId}: ${e::class.java.simpleName}")
            RssFetchOutcome(false, emptyList(), e.message ?: "Unexpected error")
        }
    }

    /**
     * Parses official YouTube Atom RSS XML directly into [PodcastCandidateVideo] instances,
     * preserving `/shorts/` hrefs, optional duration attributes, and optional media dimensions
     * so [PodcastLongFormFilter] has complete fidelity to reject Shorts and short clips.
     */
    internal fun parseAtomFeedToCandidates(
        context: Context,
        xml: String,
        source: PodcastChannelSource
    ): List<PodcastCandidateVideo> {
        val cleanXml = xml.trimStart('\uFEFF', ' ', '\t', '\r', '\n').trim()
        if (cleanXml.isBlank()) return emptyList()

        val result = mutableListOf<PodcastCandidateVideo>()
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(StringReader(cleanXml))

            val nsYt = "http://www.youtube.com/xml/schemas/2015"
            val nsMedia = "http://search.yahoo.com/mrss/"

            var eventType = parser.eventType
            var insideEntry = false
            var insideAuthor = false

            var videoId = ""
            var channelId = source.channelId
            var title = ""
            var author = source.channelName
            var description = ""
            var published = ""
            var updated = ""
            var link = ""
            var thumb = ""
            var durationSec: Int? = null
            var views: Long? = null
            var width: Int? = null
            var height: Int? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name ?: ""
                        val ns = parser.namespace ?: ""
                        if (name == "entry") {
                            insideEntry = true
                            insideAuthor = false
                            videoId = ""
                            channelId = source.channelId
                            title = ""
                            author = source.channelName
                            description = ""
                            published = ""
                            updated = ""
                            link = ""
                            thumb = ""
                            durationSec = null
                            views = null
                            width = null
                            height = null
                        } else if (insideEntry) {
                            when {
                                name == "videoId" && (ns.isEmpty() || ns == nsYt) -> {
                                    videoId = safeNextText(parser)
                                }
                                name == "channelId" && (ns.isEmpty() || ns == nsYt) -> {
                                    val cid = safeNextText(parser)
                                    if (YouTubeRssParser.isValidChannelId(cid)) channelId = cid
                                }
                                name == "title" && ns != nsMedia -> {
                                    val t = safeNextText(parser)
                                    if (t.isNotBlank()) title = t
                                }
                                name == "title" && ns == nsMedia -> {
                                    val t = safeNextText(parser)
                                    if (title.isBlank() && t.isNotBlank()) title = t
                                }
                                name == "description" && (ns == nsMedia || ns.isEmpty()) -> {
                                    val d = safeNextText(parser)
                                    if (d.isNotBlank() && description.isBlank()) description = d.take(600)
                                }
                                name == "author" -> insideAuthor = true
                                name == "name" && insideAuthor -> {
                                    val a = safeNextText(parser)
                                    if (a.isNotBlank()) author = a
                                }
                                name == "published" -> published = safeNextText(parser)
                                name == "updated" -> updated = safeNextText(parser)
                                name == "link" -> {
                                    val rel = parser.getAttributeValue(null, "rel") ?: "alternate"
                                    val href = parser.getAttributeValue(null, "href")?.trim().orEmpty()
                                    if (rel == "alternate" && href.isNotBlank()) {
                                        link = href
                                        if (videoId.isBlank()) {
                                            videoId = when {
                                                href.contains("/shorts/", ignoreCase = true) ->
                                                    href.substringAfter("/shorts/").substringBefore("?").substringBefore("/")
                                                href.contains("v=") ->
                                                    href.substringAfter("v=").substringBefore("&")
                                                else -> ""
                                            }.trim()
                                        }
                                    }
                                }
                                name == "thumbnail" -> {
                                    val u = parser.getAttributeValue(null, "url")?.trim().orEmpty()
                                    if (u.startsWith("https://") && thumb.isBlank()) thumb = u
                                }
                                name == "content" && ns == nsMedia -> {
                                    val durAttr = parser.getAttributeValue(null, "duration")?.trim()?.toIntOrNull()
                                    if (durAttr != null && durAttr > 0 && durationSec == null) durationSec = durAttr
                                    val wAttr = parser.getAttributeValue(null, "width")?.trim()?.toIntOrNull()
                                    val hAttr = parser.getAttributeValue(null, "height")?.trim()?.toIntOrNull()
                                    if (wAttr != null && hAttr != null && wAttr > 0 && hAttr > 0) {
                                        width = wAttr
                                        height = hAttr
                                    }
                                }
                                name == "duration" && (ns == nsYt || ns == nsMedia || ns.isEmpty()) -> {
                                    val sAttr = parser.getAttributeValue(null, "seconds")?.trim()?.toIntOrNull()
                                        ?: parser.getAttributeValue(null, "duration")?.trim()?.toIntOrNull()
                                    if (sAttr != null && sAttr > 0 && durationSec == null) durationSec = sAttr
                                }
                                name == "statistics" && (ns == nsMedia || ns.isEmpty()) -> {
                                    views = parser.getAttributeValue(null, "views")?.trim()?.toLongOrNull()
                                }
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        val name = parser.name ?: ""
                        if (name == "author" && insideAuthor) {
                            insideAuthor = false
                        } else if (name == "entry" && insideEntry) {
                            insideEntry = false
                            val effectiveIso = published.ifBlank { updated }
                            val playerDur = if (videoId.isNotBlank()) {
                                YouTubeRepository.getPlayerVerifiedDuration(context, videoId)
                            } else {
                                null
                            }
                            result.add(
                                PodcastCandidateVideo(
                                    videoId = videoId,
                                    title = title,
                                    description = description,
                                    channelId = channelId,
                                    channelName = author,
                                    videoUrl = link.ifBlank { "https://www.youtube.com/watch?v=$videoId" },
                                    thumbnailUrl = thumb.ifBlank { "https://i.ytimg.com/vi/$videoId/hqdefault.jpg" },
                                    publishedIso = effectiveIso,
                                    publishedDisplay = YouTubeRssParser.formatIsoDateShort(effectiveIso),
                                    publishedEpochMs = YouTubeRssParser.parseIsoDateEpoch(effectiveIso),
                                    durationSeconds = durationSec ?: playerDur,
                                    viewCount = views,
                                    videoWidth = width,
                                    videoHeight = height,
                                    sourceKind = source.sourceKind,
                                    requiresScholarAttribution = source.requiresScholarAttribution
                                )
                            )
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Atom candidate XML parse error: ${e::class.java.simpleName}")
        }
        return result
    }

    private fun safeNextText(parser: XmlPullParser): String {
        return try {
            parser.nextText()?.trim().orEmpty()
        } catch (_: Exception) {
            ""
        }
    }

    private sealed class ApiFetchOutcome {
        data class Success(
            val candidates: List<PodcastCandidateVideo>,
            val nextPageToken: String?
        ) : ApiFetchOutcome()

        data class QuotaExceeded(val message: String) : ApiFetchOutcome()
        data class Failure(val message: String) : ApiFetchOutcome()
    }

    /**
     * Fetches one page (up to 50 items) from official YouTube Data API v3 `search.list`
     * + enriches durations & view counts via `videos.list` (`part=snippet,contentDetails,statistics`).
     */
    private fun fetchApiPageForSource(
        source: PodcastChannelSource,
        apiKey: String,
        pageToken: String?
    ): ApiFetchOutcome {
        return try {
            val encodedKey = URLEncoder.encode(apiKey, "UTF-8")
            val encodedCid = URLEncoder.encode(source.channelId, "UTF-8")
            val tokenParam = if (!pageToken.isNullOrBlank()) {
                "&pageToken=${URLEncoder.encode(pageToken, "UTF-8")}"
            } else {
                ""
            }
            val searchUrl = "https://www.googleapis.com/youtube/v3/search" +
                "?part=snippet&type=video&order=date&maxResults=$API_PAGE_SIZE" +
                "&channelId=$encodedCid$tokenParam&key=$encodedKey"

            val searchReq = Request.Builder().url(searchUrl).build()
            val (videoIds, nextToken) = httpClient.newCall(searchReq).execute().use { resp ->
                val bodyStr = resp.body?.string().orEmpty()
                if (resp.code == 403 && isQuotaErrorBody(bodyStr)) {
                    return ApiFetchOutcome.QuotaExceeded("YouTube Data API daily quota exceeded; showing verified cache.")
                }
                if (!resp.isSuccessful) {
                    return ApiFetchOutcome.Failure("YouTube Data API search HTTP ${resp.code}")
                }
                val root = JSONObject(bodyStr)
                val items = root.optJSONArray("items") ?: JSONArray()
                val ids = mutableListOf<String>()
                for (i in 0 until items.length()) {
                    val item = items.optJSONObject(i) ?: continue
                    val idObj = item.optJSONObject("id")
                    val vid = idObj?.optString("videoId").orEmpty().trim()
                    if (YouTubeRssParser.isValidVideoId(vid)) {
                        ids.add(vid)
                    }
                }
                val next = root.optString("nextPageToken", "").ifBlank { null }
                ids to next
            }

            if (videoIds.isEmpty()) {
                return ApiFetchOutcome.Success(emptyList(), nextToken)
            }

            val idsCsv = URLEncoder.encode(videoIds.joinToString(","), "UTF-8")
            val videosUrl = "https://www.googleapis.com/youtube/v3/videos" +
                "?part=snippet,contentDetails,statistics&id=$idsCsv&key=$encodedKey"
            val videosReq = Request.Builder().url(videosUrl).build()
            httpClient.newCall(videosReq).execute().use { resp ->
                val bodyStr = resp.body?.string().orEmpty()
                if (resp.code == 403 && isQuotaErrorBody(bodyStr)) {
                    return ApiFetchOutcome.QuotaExceeded("YouTube Data API daily quota exceeded; showing verified cache.")
                }
                if (!resp.isSuccessful) {
                    return ApiFetchOutcome.Failure("YouTube Data API videos HTTP ${resp.code}")
                }
                val root = JSONObject(bodyStr)
                val items = root.optJSONArray("items") ?: JSONArray()
                val candidates = mutableListOf<PodcastCandidateVideo>()
                for (i in 0 until items.length()) {
                    val item = items.optJSONObject(i) ?: continue
                    val vid = item.optString("id").trim()
                    val snippet = item.optJSONObject("snippet") ?: continue
                    val contentDetails = item.optJSONObject("contentDetails")
                    val stats = item.optJSONObject("statistics")
                    val thumbs = snippet.optJSONObject("thumbnails")
                    val highThumb = thumbs?.optJSONObject("high") ?: thumbs?.optJSONObject("medium")
                    val thumbUrl = highThumb?.optString("url").orEmpty()
                        .ifBlank { "https://i.ytimg.com/vi/$vid/hqdefault.jpg" }
                    val publishedAt = snippet.optString("publishedAt").orEmpty()
                    val isoDur = contentDetails?.optString("duration")
                    val durationSec = PodcastLongFormFilter.parseIso8601DurationSeconds(isoDur)
                    val viewCount = stats?.optString("viewCount")?.toLongOrNull()

                    candidates.add(
                        PodcastCandidateVideo(
                            videoId = vid,
                            title = snippet.optString("title").orEmpty(),
                            description = snippet.optString("description").orEmpty().take(600),
                            channelId = snippet.optString("channelId").ifBlank { source.channelId },
                            channelName = snippet.optString("channelTitle").ifBlank { source.channelName },
                            videoUrl = "https://www.youtube.com/watch?v=$vid",
                            thumbnailUrl = thumbUrl,
                            publishedIso = publishedAt,
                            publishedDisplay = YouTubeRssParser.formatIsoDateShort(publishedAt),
                            publishedEpochMs = YouTubeRssParser.parseIsoDateEpoch(publishedAt),
                            durationSeconds = durationSec,
                            viewCount = viewCount,
                            sourceKind = source.sourceKind,
                            requiresScholarAttribution = source.requiresScholarAttribution
                        )
                    )
                }
                ApiFetchOutcome.Success(candidates, nextToken)
            }
        } catch (e: Exception) {
            ApiFetchOutcome.Failure(e.message ?: "YouTube Data API error")
        }
    }

    private fun isQuotaErrorBody(body: String): Boolean {
        val lower = body.lowercase(Locale.ROOT)
        return lower.contains("quotaexceeded") ||
            lower.contains("dailylimitexceeded") ||
            lower.contains("ratelimitexceeded")
    }

    private fun revalidateCachedVideos(
        context: Context,
        cached: ScholarPodcastLoadResult,
        scholar: PodcastScholarProfile
    ): ScholarPodcastLoadResult {
        val candidates = cached.videos.map { v ->
            val playerDur = YouTubeRepository.getPlayerVerifiedDuration(context, v.videoId)
            PodcastCandidateVideo(
                videoId = v.videoId,
                title = v.title,
                description = v.description,
                channelId = v.channelId,
                channelName = v.channelName,
                videoUrl = v.videoUrl,
                thumbnailUrl = v.thumbnailUrl,
                publishedIso = v.publishedIso,
                publishedDisplay = v.publishedDisplay,
                publishedEpochMs = v.publishedEpochMs,
                durationSeconds = playerDur ?: v.durationSeconds,
                viewCount = v.viewCount,
                sourceKind = v.sourceKind,
                requiresScholarAttribution = v.sourceKind == PodcastSourceKind.HOSTED_INTERVIEW
            )
        }
        val (verified, rejections) = PodcastLongFormFilter.filterAndDeduplicate(candidates, scholar)
        return cached.copy(
            videos = verified.map { it.copy(isFromCache = true) },
            rejectedCount = maxOf(cached.rejectedCount, rejections.values.sum()),
            rejectionBreakdown = if (rejections.isNotEmpty()) rejections else cached.rejectionBreakdown
        )
    }

    private fun saveDiskCache(context: Context, result: ScholarPodcastLoadResult) {
        try {
            val root = JSONObject()
            root.put("lastUpdatedMs", result.lastUpdatedMs)
            root.put("rejectedCount", result.rejectedCount)
            root.put("nextPageToken", result.nextPageToken ?: "")
            val arr = JSONArray()
            result.videos.take(MAX_CACHED_VIDEOS_PER_SCHOLAR).forEach { v ->
                val obj = JSONObject()
                obj.put("id", v.videoId)
                obj.put("t", v.title)
                obj.put("d", v.description.take(280))
                obj.put("cid", v.channelId)
                obj.put("cn", v.channelName)
                obj.put("th", v.thumbnailUrl)
                obj.put("u", v.videoUrl)
                obj.put("pi", v.publishedIso)
                obj.put("pd", v.publishedDisplay)
                obj.put("pe", v.publishedEpochMs)
                if (v.durationSeconds != null) obj.put("dur", v.durationSeconds)
                if (v.viewCount != null) obj.put("vc", v.viewCount)
                obj.put("sk", v.sourceKind.wireValue)
                arr.put(obj)
            }
            root.put("videos", arr)
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("$KEY_SCHOLAR_CACHE_PREFIX${result.scholar.id}", root.toString())
                .apply()
        } catch (e: Exception) {
            Log.w(TAG, "Failed saving podcast cache: ${e.message}")
        }
    }

    private fun readDiskCache(context: Context, scholarId: String): ScholarPodcastLoadResult? {
        val scholar = PodcastScholarRegistry.findById(scholarId) ?: return null
        val raw = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString("$KEY_SCHOLAR_CACHE_PREFIX$scholarId", null)
            ?: return null
        return try {
            val root = JSONObject(raw)
            val updatedMs = root.optLong("lastUpdatedMs", 0L)
            val rejectedCount = root.optInt("rejectedCount", 0)
            val nextToken = root.optString("nextPageToken", "").ifBlank { null }
            val arr = root.optJSONArray("videos") ?: JSONArray()
            val candidates = mutableListOf<PodcastCandidateVideo>()
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val vid = obj.optString("id")
                val dur = if (obj.has("dur")) obj.optInt("dur") else null
                val vc = if (obj.has("vc")) obj.optLong("vc") else null
                val sk = PodcastSourceKind.fromWire(obj.optString("sk"))
                val playerDur = YouTubeRepository.getPlayerVerifiedDuration(context, vid)
                candidates.add(
                    PodcastCandidateVideo(
                        videoId = vid,
                        title = obj.optString("t"),
                        description = obj.optString("d"),
                        channelId = obj.optString("cid"),
                        channelName = obj.optString("cn"),
                        videoUrl = obj.optString("u"),
                        thumbnailUrl = obj.optString("th"),
                        publishedIso = obj.optString("pi"),
                        publishedDisplay = obj.optString("pd"),
                        publishedEpochMs = obj.optLong("pe", 0L),
                        durationSeconds = playerDur ?: dur,
                        viewCount = vc,
                        sourceKind = sk,
                        requiresScholarAttribution = sk == PodcastSourceKind.HOSTED_INTERVIEW
                    )
                )
            }
            val (verified, rejections) = PodcastLongFormFilter.filterAndDeduplicate(candidates, scholar)
            ScholarPodcastLoadResult(
                scholar = scholar,
                videos = verified.map { it.copy(isFromCache = true) },
                status = PodcastFeedStatus.CACHED_FRESH,
                rejectedCount = maxOf(rejectedCount, rejections.values.sum()),
                rejectionBreakdown = rejections,
                lastUpdatedMs = updatedMs,
                nextPageToken = nextToken,
                hasMoreRemotePages = nextToken != null && youtubeApiKeyProvider().isNotBlank()
            )
        } catch (e: Exception) {
            Log.w(TAG, "Failed reading podcast cache: ${e.message}")
            null
        }
    }

    fun clearTestCaches(context: Context? = null) {
        memoryCache.clear()
        suggestionsMemoryCache.clear()
        inFlightScholars.clear()
        context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            ?.edit()
            ?.clear()
            ?.commit()
        if (context != null) {
            runCatching { File(context.filesDir, ARCHIVE_DIR_NAME).deleteRecursively() }
        }
    }
}
