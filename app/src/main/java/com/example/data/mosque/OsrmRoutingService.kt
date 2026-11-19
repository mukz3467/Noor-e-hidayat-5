package com.example.data.mosque

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URI
import java.util.Locale
import java.util.concurrent.TimeUnit

open class OsrmException(message: String, cause: Throwable? = null) : IllegalStateException(message, cause)
class OsrmRateLimitException(message: String) : OsrmException(message)
class OsrmNoRouteException(message: String) : OsrmException(message)
class OsrmNetworkException(message: String, cause: Throwable? = null) : OsrmException(message, cause)
class OsrmParseException(message: String, cause: Throwable? = null) : OsrmException(message, cause)

object OsrmRoutingService {

    const val MIN_REQUEST_INTERVAL_MS = 1100L
    private const val CACHE_TTL_MS = 5 * 60 * 1000L
    private const val MAX_CACHE_ENTRIES = 24

    // Public OSRM & FOSSGIS OSM.de demo HTTPS servers (zero API keys, shared rate limits).
    // routing.openstreetmap.de serves split car/foot/bike graphs; router.project-osrm.org serves driving globally.
    val DRIVING_ENDPOINTS = listOf(
        "https://routing.openstreetmap.de/routed-car/route/v1/driving",
        "https://router.project-osrm.org/route/v1/driving"
    )
    val WALKING_ENDPOINTS = listOf(
        "https://routing.openstreetmap.de/routed-foot/route/v1/foot"
    )
    val CYCLING_ENDPOINTS = listOf(
        "https://routing.openstreetmap.de/routed-bike/route/v1/bike"
    )

    internal var httpClientForTest: OkHttpClient? = null
    internal var endpointsForTest: Map<OSRMRouteProfile, List<String>>? = null
    internal var clockMsForTest: (() -> Long)? = null
    internal var networkRequestCountForTest: Int = 0
        private set

    private val throttleMutex = Mutex()
    private var lastRequestTimestampMs: Long = 0L
    private val routeCache = LinkedHashMap<String, MosqueRouteResult>(MAX_CACHE_ENTRIES, 0.75f, true)

    private val sharedClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(2500, TimeUnit.MILLISECONDS)
            .readTimeout(2500, TimeUnit.MILLISECONDS)
            .callTimeout(2800, TimeUnit.MILLISECONDS)
            .build()
    }

    internal fun resetForTest() {
        httpClientForTest = null
        endpointsForTest = null
        clockMsForTest = null
        lastRequestTimestampMs = 0L
        networkRequestCountForTest = 0
        synchronized(routeCache) { routeCache.clear() }
    }

    fun endpointsForProfile(profile: OSRMRouteProfile): List<String> {
        endpointsForTest?.get(profile)?.let { return it }
        return when (profile) {
            OSRMRouteProfile.DRIVING -> DRIVING_ENDPOINTS
            OSRMRouteProfile.WALKING -> WALKING_ENDPOINTS
            OSRMRouteProfile.CYCLING -> CYCLING_ENDPOINTS
        }
    }

    fun buildRouteUrl(
        baseEndpoint: String,
        originLat: Double,
        originLon: Double,
        destLat: Double,
        destLon: Double
    ): String {
        val cleanBase = baseEndpoint.trimEnd('/')
        return String.format(
            Locale.US,
            "%s/%.6f,%.6f;%.6f,%.6f?overview=full&geometries=geojson&steps=true",
            cleanBase,
            originLon,
            originLat,
            destLon,
            destLat
        )
    }

    suspend fun fetchRoute(
        originLat: Double,
        originLon: Double,
        destLat: Double,
        destLon: Double,
        profile: OSRMRouteProfile = OSRMRouteProfile.DRIVING
    ): Result<MosqueRouteResult> = withContext(Dispatchers.IO) {
        if (originLat !in -90.0..90.0 || originLon !in -180.0..180.0 ||
            destLat !in -90.0..90.0 || destLon !in -180.0..180.0
        ) {
            return@withContext Result.failure(OsrmException("Invalid route coordinates."))
        }

        val nowMs = (clockMsForTest ?: { System.currentTimeMillis() })()
        val cacheKey = String.format(
            Locale.US,
            "%s|%.4f,%.4f|%.4f,%.4f",
            profile.id,
            originLat,
            originLon,
            destLat,
            destLon
        )
        synchronized(routeCache) {
            val cached = routeCache[cacheKey]
            if (cached != null && (nowMs - cached.fetchedAtMs) in 0 until CACHE_TTL_MS) {
                return@withContext Result.success(cached)
            }
        }

        throttleMutex.withLock {
            val clock = clockMsForTest ?: { System.currentTimeMillis() }
            val current = clock()
            val elapsed = current - lastRequestTimestampMs
            if (lastRequestTimestampMs > 0L && elapsed in 0 until MIN_REQUEST_INTERVAL_MS) {
                return@withContext Result.failure(
                    OsrmRateLimitException(
                        "Please wait a second before requesting another OSRM route (shared demo server limit)."
                    )
                )
            }
            lastRequestTimestampMs = current
        }

        val endpoints = endpointsForProfile(profile)
        val client = httpClientForTest ?: sharedClient
        var lastError: Throwable? = null

        for (endpoint in endpoints) {
            if (!endpoint.startsWith("https://") && httpClientForTest == null) continue
            val url = buildRouteUrl(endpoint, originLat, originLon, destLat, destLon)
            val host = runCatching { URI(endpoint).host ?: endpoint }.getOrDefault(endpoint)
            try {
                networkRequestCountForTest++
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", OverpassMosqueService.USER_AGENT)
                    .header("Accept", "application/json")
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    val code = response.code
                    if (code == 429) {
                        lastError = OsrmRateLimitException(
                            "Public OSRM demo server is rate-limiting requests (HTTP 429 on $host). Please wait or open your external Maps app."
                        )
                        return@use
                    }
                    if (!response.isSuccessful) {
                        lastError = OsrmNetworkException(
                            "OSRM routing server returned HTTP $code ($host). Try external Maps app."
                        )
                        return@use
                    }
                    val raw = response.body?.string().orEmpty()
                    if (raw.isBlank()) {
                        lastError = OsrmParseException("Empty response from OSRM server ($host).")
                        return@use
                    }
                    val parsed = parseOsrmResponse(
                        rawJson = raw,
                        profile = profile,
                        originLat = originLat,
                        originLon = originLon,
                        destLat = destLat,
                        destLon = destLon,
                        providerHost = host,
                        fetchedAtMs = (clockMsForTest ?: { System.currentTimeMillis() })()
                    )
                    synchronized(routeCache) {
                        if (routeCache.size >= MAX_CACHE_ENTRIES) {
                            val eldest = routeCache.keys.firstOrNull()
                            if (eldest != null) routeCache.remove(eldest)
                        }
                        routeCache[cacheKey] = parsed
                    }
                    return@withContext Result.success(parsed)
                }
            } catch (e: OsrmException) {
                lastError = e
                // If routing engine explicitly said NoRoute, don't keep trying unless another engine might succeed
            } catch (e: JSONException) {
                lastError = OsrmParseException("Invalid JSON from OSRM server ($host): ${e.message}", e)
            } catch (e: IOException) {
                lastError = OsrmNetworkException(
                    "Unable to reach OSRM routing server ($host): ${e.message}. Use external Maps app if offline.",
                    e
                )
            } catch (t: Throwable) {
                lastError = t
            }
        }

        Result.failure(
            lastError ?: OsrmNetworkException("Could not reach any OSRM routing demo server.")
        )
    }

    fun parseOsrmResponse(
        rawJson: String,
        profile: OSRMRouteProfile,
        originLat: Double,
        originLon: Double,
        destLat: Double,
        destLon: Double,
        providerHost: String = "router.project-osrm.org",
        fetchedAtMs: Long = System.currentTimeMillis()
    ): MosqueRouteResult {
        val trimmed = rawJson.trim()
        if (!trimmed.startsWith("{")) {
            throw OsrmParseException("Expected JSON response from OSRM.")
        }
        val root = JSONObject(trimmed)
        val statusCode = root.optString("code", "")
        if (!statusCode.equals("Ok", ignoreCase = true)) {
            val msg = root.optString("message", "").ifBlank { statusCode.ifBlank { "NoRoute" } }
            throw OsrmNoRouteException("No road route returned by OSRM ($msg).")
        }
        val routes = root.optJSONArray("routes")
            ?: throw OsrmNoRouteException("No road route geometry returned by OSRM.")
        if (routes.length() == 0) {
            throw OsrmNoRouteException("No road route found between these points.")
        }
        val routeObj = routes.getJSONObject(0)
        val geometry = routeObj.optJSONObject("geometry")
            ?: throw OsrmParseException("Missing GeoJSON geometry in OSRM response.")
        val coordsArray = geometry.optJSONArray("coordinates")
            ?: throw OsrmParseException("Missing route coordinates in OSRM response.")

        val coords = ArrayList<Pair<Double, Double>>(coordsArray.length())
        for (i in 0 until coordsArray.length()) {
            val pt = coordsArray.optJSONArray(i) ?: continue
            if (pt.length() < 2) continue
            val lon = pt.optDouble(0, Double.NaN)
            val lat = pt.optDouble(1, Double.NaN)
            if (!lat.isNaN() && !lon.isNaN() && lat in -90.0..90.0 && lon in -180.0..180.0) {
                coords.add(lat to lon)
            }
        }
        if (coords.size < 2) {
            throw OsrmNoRouteException("OSRM returned an empty road path.")
        }

        val distanceMeters = routeObj.optDouble("distance", Double.NaN)
        if (distanceMeters.isNaN() || distanceMeters < 0.0) {
            throw OsrmParseException("OSRM response omitted valid route distance.")
        }

        // Only report travel time if OSRM returned a positive numeric duration.
        val rawDuration = if (routeObj.has("duration") && !routeObj.isNull("duration")) {
            routeObj.optDouble("duration", Double.NaN)
        } else {
            Double.NaN
        }
        val durationSeconds = if (!rawDuration.isNaN() && rawDuration > 0.0) rawDuration else null

        val legs = routeObj.optJSONArray("legs")
        var roadSummary: String? = null
        val stepInfos = mutableListOf<RouteStepInfo>()
        if (legs != null && legs.length() > 0) {
            val leg0 = legs.optJSONObject(0)
            if (leg0 != null) {
                roadSummary = leg0.optString("summary", "").trim().takeIf { it.isNotBlank() }
                val steps = leg0.optJSONArray("steps")
                if (steps != null) {
                    for (s in 0 until steps.length()) {
                        val stepObj = steps.optJSONObject(s) ?: continue
                        val road = stepObj.optString("name", "").trim()
                        val maneuver = stepObj.optJSONObject("maneuver")
                        val mType = maneuver?.optString("type", "").orEmpty().trim()
                        val mMod = maneuver?.optString("modifier", "").orEmpty().trim()
                        val stepDist = stepObj.optDouble("distance", 0.0).coerceAtLeast(0.0)
                        val stepDur = if (stepObj.has("duration") && !stepObj.isNull("duration")) {
                            stepObj.optDouble("duration", Double.NaN).takeIf { !it.isNaN() && it > 0.0 }
                        } else null
                        val label = buildStepInstruction(mType, mMod, road)
                        if (label.isNotBlank()) {
                            stepInfos.add(
                                RouteStepInfo(
                                    instruction = label,
                                    roadName = road,
                                    distanceMeters = stepDist,
                                    durationSeconds = stepDur
                                )
                            )
                        }
                    }
                }
            }
        }

        return MosqueRouteResult(
            profile = profile,
            originLat = originLat,
            originLon = originLon,
            destinationLat = destLat,
            destinationLon = destLon,
            coordinates = coords,
            distanceMeters = distanceMeters,
            durationSeconds = durationSeconds,
            roadSummary = roadSummary,
            steps = stepInfos,
            providerHost = providerHost,
            fetchedAtMs = fetchedAtMs
        )
    }

    private fun buildStepInstruction(type: String, modifier: String, road: String): String {
        val cleanType = type.replace('_', ' ').replaceFirstChar { it.uppercase(Locale.US) }
        val cleanMod = modifier.replace('_', ' ')
        val action = when {
            cleanType.equals("Depart", ignoreCase = true) -> if (cleanMod.isNotBlank()) "Head $cleanMod" else "Start"
            cleanType.equals("Arrive", ignoreCase = true) -> "Arrive at destination"
            cleanType.equals("Turn", ignoreCase = true) && cleanMod.isNotBlank() -> "Turn $cleanMod"
            cleanMod.isNotBlank() && cleanType.isNotBlank() -> "$cleanType $cleanMod"
            cleanType.isNotBlank() -> cleanType
            else -> "Continue"
        }
        return if (road.isNotBlank() && !cleanType.equals("Arrive", ignoreCase = true)) {
            "$action onto $road"
        } else {
            action
        }
    }

    fun formatDuration(seconds: Double): String {
        val totalMin = (seconds / 60.0).toInt().coerceAtLeast(1)
        val hours = totalMin / 60
        val mins = totalMin % 60
        return when {
            hours > 0 && mins > 0 -> "${hours}h ${mins}m"
            hours > 0 -> "${hours}h"
            else -> "$mins min"
        }
    }
}
