package com.example.data.mosque

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

open class OverpassException(message: String, cause: Throwable? = null) : IllegalStateException(message, cause)
class OverpassRateLimitException(message: String) : OverpassException(message)
class OverpassServerException(val code: Int, message: String) : OverpassException(message)
class OverpassNetworkException(message: String, cause: Throwable? = null) : OverpassException(message, cause)
class OverpassParseException(message: String, cause: Throwable? = null) : OverpassException(message, cause)

data class OverpassFetchResult(
    val items: List<MosqueItem>,
    val endpointUsed: String,
    val fetchedAtMs: Long
)

object OverpassMosqueService {

    const val USER_AGENT = "Noor-e-Hidayat-Android/1.0 (https://noor-e-hidayat.app; openstreetmap-client)"
    const val POSTPASS_ENDPOINT = "https://postpass.geofabrik.de/api/0.2/interpreter"
    private const val PHOTON_ENDPOINT = "https://photon.komoot.io/api"
    private const val NOMINATIM_SEARCH_ENDPOINT = "https://nominatim.openstreetmap.org/search"
    private const val MAX_RETRIES_SINGLE_ENDPOINT = 1
    private const val RACE_TIMEOUT_MS = 8000L
    private const val HEDGE_DELAY_MS = 1000L

    val DEFAULT_ENDPOINTS = listOf(
        POSTPASS_ENDPOINT,
        "https://overpass-api.de/api/interpreter",
        "https://overpass.kumi.systems/api/interpreter",
        "https://overpass.private.coffee/api/interpreter"
    )

    internal var httpClientForTest: OkHttpClient? = null
    internal var endpointsForTest: List<String>? = null
    internal var backoffBaseMsForTest: Long = 350L

    private val sharedClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .callTimeout(7000, TimeUnit.MILLISECONDS)
            .build()
    }

    private fun activeClient(): OkHttpClient = httpClientForTest ?: sharedClient

    private fun resolveEndpoints(preferredEndpoint: String?): List<String> {
        val base = endpointsForTest ?: DEFAULT_ENDPOINTS
        if (preferredEndpoint.isNullOrBlank()) return base
        val clean = preferredEndpoint.trim()
        return if (clean in base) {
            listOf(clean) + base.filter { it != clean }
        } else {
            listOf(clean) + base
        }
    }

    fun boundingBoxFromRadius(lat: Double, lon: Double, radiusMeters: Int): MapBoundingBox {
        val safeRadius = radiusMeters.coerceIn(250, 50_000).toDouble()
        val latDelta = safeRadius / 111_320.0
        val cosLat = cos(Math.toRadians(lat)).coerceAtLeast(0.15)
        val lonDelta = safeRadius / (111_320.0 * cosLat)
        return MapBoundingBox(
            south = (lat - latDelta).coerceIn(-89.9, 89.9),
            west = (lon - lonDelta).coerceIn(-179.9, 179.9),
            north = (lat + latDelta).coerceIn(-89.9, 89.9),
            east = (lon + lonDelta).coerceIn(-179.9, 179.9)
        )
    }

    fun buildOverpassQuery(lat: Double, lon: Double, radiusMeters: Int): String {
        val safeRadius = radiusMeters.coerceIn(250, 50_000)
        val latStr = String.format(Locale.US, "%.6f", lat)
        val lonStr = String.format(Locale.US, "%.6f", lon)
        return """
            [out:json][timeout:6];
            (
              nwr["amenity"="place_of_worship"]["religion"="muslim"](around:$safeRadius,$latStr,$lonStr);
              nwr["building"="place_of_worship"]["religion"="muslim"](around:$safeRadius,$latStr,$lonStr);
              nwr["building"="mosque"](around:$safeRadius,$latStr,$lonStr);
              nwr["amenity"="prayer_room"]["religion"="muslim"](around:$safeRadius,$latStr,$lonStr);
              nwr["amenity"="prayer_room"]["muslim"="yes"](around:$safeRadius,$latStr,$lonStr);
              nwr["musalla"="yes"](around:$safeRadius,$latStr,$lonStr);
            );
            out center tags;
        """.trimIndent()
    }

    fun buildOverpassBboxQuery(bounds: MapBoundingBox): String {
        val s = String.format(Locale.US, "%.6f", bounds.south.coerceIn(-89.9, 89.9))
        val w = String.format(Locale.US, "%.6f", bounds.west.coerceIn(-179.9, 179.9))
        val n = String.format(Locale.US, "%.6f", bounds.north.coerceIn(-89.9, 89.9))
        val e = String.format(Locale.US, "%.6f", bounds.east.coerceIn(-179.9, 179.9))
        return """
            [out:json][timeout:6];
            (
              nwr["amenity"="place_of_worship"]["religion"="muslim"]($s,$w,$n,$e);
              nwr["building"="place_of_worship"]["religion"="muslim"]($s,$w,$n,$e);
              nwr["building"="mosque"]($s,$w,$n,$e);
              nwr["amenity"="prayer_room"]["religion"="muslim"]($s,$w,$n,$e);
              nwr["amenity"="prayer_room"]["muslim"="yes"]($s,$w,$n,$e);
              nwr["musalla"="yes"]($s,$w,$n,$e);
            );
            out center tags;
        """.trimIndent()
    }

    /**
     * Indexed fast quad-tile Overpass bbox query (3 concise tag predicates instead of 6 spherical scans).
     */
    private fun buildFastIndexedOverpassBboxQuery(bounds: MapBoundingBox): String {
        val s = String.format(Locale.US, "%.6f", bounds.south.coerceIn(-89.9, 89.9))
        val w = String.format(Locale.US, "%.6f", bounds.west.coerceIn(-179.9, 179.9))
        val n = String.format(Locale.US, "%.6f", bounds.north.coerceIn(-89.9, 89.9))
        val e = String.format(Locale.US, "%.6f", bounds.east.coerceIn(-179.9, 179.9))
        return """
            [out:json][timeout:6];
            (
              nwr["religion"="muslim"]($s,$w,$n,$e);
              nwr["building"="mosque"]($s,$w,$n,$e);
              nwr["musalla"="yes"]($s,$w,$n,$e);
            );
            out center tags;
        """.trimIndent()
    }

    fun buildPostpassBboxSql(bounds: MapBoundingBox): String {
        val s = String.format(Locale.US, "%.6f", bounds.south.coerceIn(-89.9, 89.9))
        val w = String.format(Locale.US, "%.6f", bounds.west.coerceIn(-179.9, 179.9))
        val n = String.format(Locale.US, "%.6f", bounds.north.coerceIn(-89.9, 89.9))
        val e = String.format(Locale.US, "%.6f", bounds.east.coerceIn(-179.9, 179.9))
        return """
            SELECT osm_id, osm_type, tags, ST_Centroid(geom) AS geom
            FROM postpass_pointpolygon
            WHERE geom && ST_MakeEnvelope($w, $s, $e, $n, 4326)
              AND (
                (tags->>'amenity' = 'place_of_worship' AND tags->>'religion' = 'muslim')
                OR (tags->>'building' = 'place_of_worship' AND tags->>'religion' = 'muslim')
                OR tags->>'building' = 'mosque'
                OR (tags->>'amenity' = 'prayer_room' AND (tags->>'religion' = 'muslim' OR tags->>'muslim' = 'yes'))
                OR tags->>'musalla' = 'yes'
              )
            LIMIT 160
        """.trimIndent()
    }

    suspend fun fetchNearbyMosques(
        lat: Double,
        lon: Double,
        radiusMeters: Int,
        preferredEndpoint: String? = null
    ): Result<List<MosqueItem>> =
        fetchNearbyMosquesDetailed(lat, lon, radiusMeters, preferredEndpoint).map { it.items }

    suspend fun fetchNearbyMosquesDetailed(
        lat: Double,
        lon: Double,
        radiusMeters: Int,
        preferredEndpoint: String? = null
    ): Result<OverpassFetchResult> {
        val useTestPath = endpointsForTest != null || httpClientForTest != null
        if (useTestPath) {
            val query = buildOverpassQuery(lat, lon, radiusMeters)
            return executeOverpassQuery(
                query = query,
                originLat = lat,
                originLon = lon,
                preferredEndpoint = preferredEndpoint
            )
        }
        val bounds = boundingBoxFromRadius(lat, lon, radiusMeters)
        return executeMultiEngineOsmQuery(
            bounds = bounds,
            originLat = lat,
            originLon = lon,
            maxDistanceMeters = radiusMeters.coerceIn(250, 50_000).toDouble() * 1.05,
            preferredEndpoint = preferredEndpoint
        )
    }

    suspend fun fetchMosquesInBoundsDetailed(
        bounds: MapBoundingBox,
        distanceOriginLat: Double = bounds.centerLat,
        distanceOriginLon: Double = bounds.centerLon,
        preferredEndpoint: String? = null
    ): Result<OverpassFetchResult> {
        if (!bounds.isValid()) {
            return Result.failure(OverpassException("Invalid map bounding box coordinates."))
        }
        val useTestPath = endpointsForTest != null || httpClientForTest != null
        if (useTestPath) {
            val query = buildOverpassBboxQuery(bounds)
            return executeOverpassQuery(
                query = query,
                originLat = distanceOriginLat,
                originLon = distanceOriginLon,
                preferredEndpoint = preferredEndpoint
            )
        }
        return executeMultiEngineOsmQuery(
            bounds = bounds,
            originLat = distanceOriginLat,
            originLon = distanceOriginLon,
            maxDistanceMeters = null,
            preferredEndpoint = preferredEndpoint
        )
    }

    /**
     * Fetches actual OSM polygon coordinates for a mapped way or relation mosque/building.
     * Nodes have no building footprint and return an empty list without network calls.
     */
    suspend fun fetchElementFootprint(
        osmType: String,
        osmId: Long,
        preferredEndpoint: String? = null
    ): Result<List<Pair<Double, Double>>> = withContext(Dispatchers.IO) {
        val cleanType = osmType.lowercase(Locale.ROOT)
        if (cleanType != "way" && cleanType != "relation") {
            return@withContext Result.success(emptyList())
        }
        if (osmId <= 0L) {
            return@withContext Result.success(emptyList())
        }
        val query = """
            [out:json][timeout:5];
            $cleanType($osmId);
            out geom;
        """.trimIndent()
        val bodyStr = "data=" + URLEncoder.encode(query, "UTF-8")
        val mediaType = "application/x-www-form-urlencoded; charset=utf-8".toMediaType()
        val endpoints = resolveEndpoints(preferredEndpoint)
            .filter { !it.contains("postpass.", ignoreCase = true) }
        var lastError: Throwable? = null
        for (endpoint in endpoints) {
            if (!endpoint.startsWith("https://") && httpClientForTest == null) continue
            try {
                val request = Request.Builder()
                    .url(endpoint)
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "application/json")
                    .post(bodyStr.toRequestBody(mediaType))
                    .build()
                activeClient().newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        lastError = OverpassServerException(response.code, "HTTP ${response.code}")
                        return@use
                    }
                    val raw = response.body?.string().orEmpty()
                    val pts = parseFootprintGeometry(raw)
                    return@withContext Result.success(pts)
                }
            } catch (t: Throwable) {
                lastError = t
            }
        }
        Result.failure(lastError ?: OverpassNetworkException("Unable to fetch footprint geometry."))
    }

    fun parseFootprintGeometry(rawJson: String): List<Pair<Double, Double>> {
        val trimmed = rawJson.trim()
        if (!trimmed.startsWith("{")) return emptyList()
        val root = JSONObject(trimmed)
        val elements = root.optJSONArray("elements") ?: return emptyList()
        if (elements.length() == 0) return emptyList()
        val el = elements.optJSONObject(0) ?: return emptyList()
        val directGeom = el.optJSONArray("geometry")
        if (directGeom != null && directGeom.length() >= 3) {
            return extractGeomRing(directGeom)
        }
        val members = el.optJSONArray("members") ?: return emptyList()
        for (i in 0 until members.length()) {
            val member = members.optJSONObject(i) ?: continue
            val role = member.optString("role", "")
            if (role.isBlank() || role == "outer") {
                val memberGeom = member.optJSONArray("geometry")
                if (memberGeom != null && memberGeom.length() >= 3) {
                    return extractGeomRing(memberGeom)
                }
            }
        }
        return emptyList()
    }

    private fun extractGeomRing(geomArray: JSONArray): List<Pair<Double, Double>> {
        val pts = ArrayList<Pair<Double, Double>>(geomArray.length())
        for (i in 0 until geomArray.length()) {
            val pt = geomArray.optJSONObject(i) ?: continue
            val lat = pt.optDouble("lat", Double.NaN)
            val lon = pt.optDouble("lon", Double.NaN)
            if (!lat.isNaN() && !lon.isNaN() && lat in -90.0..90.0 && lon in -180.0..180.0) {
                pts.add(lat to lon)
            }
        }
        return if (pts.size >= 3) pts else emptyList()
    }

    private enum class EngineTier { AUTHORITATIVE_OSM, SEARCH_HEDGE }

    private data class EngineOutcome(
        val tier: EngineTier,
        val result: Result<OverpassFetchResult>
    )

    /**
     * Multi-engine live OSM query race:
     * 1) Geofabrik Postpass (PostGIS GIST indexed, sub-second, zero Overpass limits)
     * 2) Fast BBox Overpass mirrors (overpass-api.de, kumi.systems, private.coffee)
     * 3) Delayed 1s Hedge -> Komoot Photon + OSM Nominatim extratags bounded POI search
     *
     * Authoritative Postpass/Overpass answers win immediately; Search Hedge rescues within ~1.5s
     * if Overpass/Postpass mirrors are busy or throttled, eliminating multi-minute timeouts.
     */
    private suspend fun executeMultiEngineOsmQuery(
        bounds: MapBoundingBox,
        originLat: Double,
        originLon: Double,
        maxDistanceMeters: Double?,
        preferredEndpoint: String?
    ): Result<OverpassFetchResult> = withContext(Dispatchers.IO) {
        val overpassQuery = buildFastIndexedOverpassBboxQuery(bounds)
        val overpassBodyStr = "data=" + URLEncoder.encode(overpassQuery, "UTF-8")
        val postpassSql = buildPostpassBboxSql(bounds)

        if (!preferredEndpoint.isNullOrBlank()) {
            val clean = preferredEndpoint.trim()
            if (clean.contains("postpass.", ignoreCase = true)) {
                return@withContext queryPostpassEndpoint(
                    endpoint = clean,
                    sqlQuery = postpassSql,
                    originLat = originLat,
                    originLon = originLon,
                    bounds = if (maxDistanceMeters == null) bounds else null,
                    maxDistanceMeters = maxDistanceMeters
                )
            }
            return@withContext querySingleEndpoint(
                endpoint = clean,
                bodyStr = overpassBodyStr,
                originLat = originLat,
                originLon = originLon,
                maxRetries = 1,
                bounds = if (maxDistanceMeters == null) bounds else null,
                maxDistanceMeters = maxDistanceMeters
            )
        }

        val overpassMirrors = DEFAULT_ENDPOINTS.filter { !it.contains("postpass.", ignoreCase = true) }
        val totalWorkers = 1 + overpassMirrors.size + 1 // Postpass + Overpass mirrors + Hedge track
        val raced = withTimeoutOrNull(RACE_TIMEOUT_MS) {
            coroutineScope {
                val channel = Channel<EngineOutcome>(totalWorkers)
                val jobs = mutableListOf<kotlinx.coroutines.Job>()

                // Track 1: Geofabrik Postpass (t = 0ms)
                jobs += launch {
                    val res = queryPostpassEndpoint(
                        endpoint = POSTPASS_ENDPOINT,
                        sqlQuery = postpassSql,
                        originLat = originLat,
                        originLon = originLon,
                        bounds = if (maxDistanceMeters == null) bounds else null,
                        maxDistanceMeters = maxDistanceMeters
                    )
                    channel.trySend(EngineOutcome(EngineTier.AUTHORITATIVE_OSM, res))
                }

                // Track 2: Fast BBox Overpass Mirrors (t = 0ms)
                overpassMirrors.forEach { mirror ->
                    jobs += launch {
                        val res = querySingleEndpoint(
                            endpoint = mirror,
                            bodyStr = overpassBodyStr,
                            originLat = originLat,
                            originLon = originLon,
                            maxRetries = 0,
                            bounds = if (maxDistanceMeters == null) bounds else null,
                            maxDistanceMeters = maxDistanceMeters
                        )
                        channel.trySend(EngineOutcome(EngineTier.AUTHORITATIVE_OSM, res))
                    }
                }

                // Track 3: Komoot Photon + OSM Nominatim rapid rescue hedge (t = 1000ms)
                jobs += launch {
                    delay(HEDGE_DELAY_MS)
                    val res = queryPhotonAndNominatimFallback(
                        bounds = bounds,
                        originLat = originLat,
                        originLon = originLon,
                        maxDistanceMeters = maxDistanceMeters
                    )
                    channel.trySend(EngineOutcome(EngineTier.SEARCH_HEDGE, res))
                }

                val authoritativeCount = 1 + overpassMirrors.size
                var authoritativeRemaining = authoritativeCount
                var bestAuthoritativeEmpty: OverpassFetchResult? = null
                var lastErr: Throwable? = null

                repeat(totalWorkers) {
                    val outcome = channel.receive()
                    if (outcome.tier == EngineTier.AUTHORITATIVE_OSM) {
                        authoritativeRemaining -= 1
                    }
                    val res = outcome.result
                    if (res.isSuccess) {
                        val payload = res.getOrNull()!!
                        if (payload.items.isNotEmpty()) {
                            jobs.forEach { it.cancel() }
                            return@coroutineScope Result.success(payload)
                        } else if (outcome.tier == EngineTier.AUTHORITATIVE_OSM) {
                            bestAuthoritativeEmpty = payload
                        }
                    } else {
                        lastErr = res.exceptionOrNull()
                    }
                    if (authoritativeRemaining <= 0 && bestAuthoritativeEmpty != null && lastErr == null) {
                        jobs.forEach { it.cancel() }
                        return@coroutineScope Result.success(bestAuthoritativeEmpty!!)
                    }
                }

                bestAuthoritativeEmpty?.let { return@coroutineScope Result.success(it) }
                Result.failure(lastErr ?: OverpassNetworkException("Unable to reach OpenStreetMap servers."))
            }
        }
        raced ?: Result.failure(
            OverpassNetworkException("OpenStreetMap query timed out. Tap Retry or try another area.")
        )
    }

    private suspend fun executeOverpassQuery(
        query: String,
        originLat: Double,
        originLon: Double,
        preferredEndpoint: String?
    ): Result<OverpassFetchResult> = withContext(Dispatchers.IO) {
        val bodyStr = "data=" + URLEncoder.encode(query, "UTF-8")
        val endpoints = resolveEndpoints(preferredEndpoint)
            .filter { it.startsWith("https://") || httpClientForTest != null }
        if (endpoints.isEmpty()) {
            return@withContext Result.failure(OverpassNetworkException("No HTTPS Overpass endpoints configured."))
        }

        if (endpoints.size == 1) {
            return@withContext querySingleEndpoint(
                endpoint = endpoints.first(),
                bodyStr = bodyStr,
                originLat = originLat,
                originLon = originLon,
                maxRetries = MAX_RETRIES_SINGLE_ENDPOINT
            )
        }

        val raced = withTimeoutOrNull(RACE_TIMEOUT_MS) {
            coroutineScope {
                val resultsChannel = Channel<Result<OverpassFetchResult>>(endpoints.size)
                val jobs = endpoints.map { endpoint ->
                    launch {
                        val res = querySingleEndpoint(
                            endpoint = endpoint,
                            bodyStr = bodyStr,
                            originLat = originLat,
                            originLon = originLon,
                            maxRetries = 0
                        )
                        resultsChannel.trySend(res)
                    }
                }
                var lastErr: Throwable? = null
                repeat(endpoints.size) {
                    val outcome = resultsChannel.receive()
                    if (outcome.isSuccess) {
                        jobs.forEach { it.cancel() }
                        return@coroutineScope outcome
                    }
                    lastErr = outcome.exceptionOrNull()
                }
                Result.failure(
                    lastErr ?: OverpassNetworkException("Unable to reach OpenStreetMap Overpass servers.")
                )
            }
        }
        raced ?: Result.failure(
            OverpassNetworkException("OpenStreetMap Overpass query timed out. Tap Retry or adjust area.")
        )
    }

    private fun queryPostpassEndpoint(
        endpoint: String,
        sqlQuery: String,
        originLat: Double,
        originLon: Double,
        bounds: MapBoundingBox?,
        maxDistanceMeters: Double?
    ): Result<OverpassFetchResult> {
        return try {
            val mediaType = "application/x-www-form-urlencoded; charset=utf-8".toMediaType()
            val bodyStr = "data=" + URLEncoder.encode(sqlQuery, "UTF-8")
            val request = Request.Builder()
                .url(endpoint)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/json")
                .post(bodyStr.toRequestBody(mediaType))
                .build()
            activeClient().newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.failure(
                        OverpassServerException(
                            response.code,
                            "Postpass request failed with HTTP ${response.code} on ${hostLabel(endpoint)}."
                        )
                    )
                }
                val raw = response.body?.string().orEmpty()
                if (raw.isBlank()) {
                    return Result.failure(OverpassParseException("Empty response from Postpass."))
                }
                val nowMs = System.currentTimeMillis()
                val items = parsePostpassGeoJson(
                    rawJson = raw,
                    originLat = originLat,
                    originLon = originLon,
                    fetchedAtMs = nowMs,
                    bounds = bounds,
                    maxDistanceMeters = maxDistanceMeters
                )
                Result.success(
                    OverpassFetchResult(
                        items = items,
                        endpointUsed = endpoint,
                        fetchedAtMs = nowMs
                    )
                )
            }
        } catch (e: JSONException) {
            Result.failure(OverpassParseException("Invalid GeoJSON from Postpass: ${e.message}", e))
        } catch (e: IOException) {
            Result.failure(OverpassNetworkException("Network error contacting ${hostLabel(endpoint)}: ${e.message}", e))
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    private suspend fun queryPhotonAndNominatimFallback(
        bounds: MapBoundingBox,
        originLat: Double,
        originLon: Double,
        maxDistanceMeters: Double?
    ): Result<OverpassFetchResult> = coroutineScope {
        val searchLat = if (maxDistanceMeters != null) originLat else bounds.centerLat
        val searchLon = if (maxDistanceMeters != null) originLon else bounds.centerLon
        val effectiveRadiusM = maxDistanceMeters ?: (haversineDistanceMeters(
            bounds.south, bounds.west, bounds.north, bounds.east
        ) / 2.0).coerceIn(1000.0, 50_000.0)

        val photonMasjidJob = async(Dispatchers.IO) {
            fetchPhotonMosques(searchLat, searchLon, "masjid", effectiveRadiusM, originLat, originLon, bounds, maxDistanceMeters)
        }
        val photonMosqueJob = async(Dispatchers.IO) {
            fetchPhotonMosques(searchLat, searchLon, "mosque", effectiveRadiusM, originLat, originLon, bounds, maxDistanceMeters)
        }
        val nominatimJob = async(Dispatchers.IO) {
            fetchNominatimBoundedMosques(bounds, originLat, originLon, maxDistanceMeters)
        }

        val combined = LinkedHashMap<String, MosqueItem>()
        val allResults = awaitAll(nominatimJob, photonMasjidJob, photonMosqueJob)
        var usedEndpoint: String? = null
        var lastError: Throwable? = null
        allResults.forEach { res ->
            res.onSuccess { (ep, items) ->
                if (items.isNotEmpty() && usedEndpoint == null) usedEndpoint = ep
                items.forEach { item ->
                    val existing = combined[item.id]
                    if (existing == null || item.verificationLevel.ordinal < existing.verificationLevel.ordinal) {
                        combined[item.id] = item
                    }
                }
            }.onFailure { err ->
                lastError = err
            }
        }

        if (combined.isNotEmpty()) {
            Result.success(
                OverpassFetchResult(
                    items = combined.values.sortedBy { it.distanceMeters },
                    endpointUsed = usedEndpoint ?: NOMINATIM_SEARCH_ENDPOINT,
                    fetchedAtMs = System.currentTimeMillis()
                )
            )
        } else {
            Result.failure(lastError ?: OverpassNetworkException("No fallback OSM mosques resolved."))
        }
    }

    private fun fetchPhotonMosques(
        centerLat: Double,
        centerLon: Double,
        term: String,
        radiusLimitMeters: Double,
        originLat: Double,
        originLon: Double,
        bounds: MapBoundingBox,
        maxDistanceMeters: Double?
    ): Result<Pair<String, List<MosqueItem>>> {
        return try {
            val url = String.format(
                Locale.US,
                "%s?q=%s&lat=%.6f&lon=%.6f&limit=35&osm_tag=amenity:place_of_worship",
                PHOTON_ENDPOINT,
                URLEncoder.encode(term, "UTF-8"),
                centerLat,
                centerLon
            )
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/json")
                .build()
            activeClient().newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    return Result.failure(OverpassServerException(resp.code, "Photon HTTP ${resp.code}"))
                }
                val raw = resp.body?.string().orEmpty()
                val items = parsePhotonGeoJson(
                    rawJson = raw,
                    originLat = originLat,
                    originLon = originLon,
                    bounds = if (maxDistanceMeters == null) bounds else null,
                    maxDistanceMeters = radiusLimitMeters
                )
                Result.success(PHOTON_ENDPOINT to items)
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    private fun fetchNominatimBoundedMosques(
        bounds: MapBoundingBox,
        originLat: Double,
        originLon: Double,
        maxDistanceMeters: Double?
    ): Result<Pair<String, List<MosqueItem>>> {
        return try {
            val viewbox = String.format(
                Locale.US,
                "%.6f,%.6f,%.6f,%.6f",
                bounds.west, bounds.north, bounds.east, bounds.south
            )
            val url = "$NOMINATIM_SEARCH_ENDPOINT?q=mosque&format=jsonv2&extratags=1&addressdetails=1&bounded=1&limit=40&viewbox=$viewbox"
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/json")
                .build()
            activeClient().newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    return Result.failure(OverpassServerException(resp.code, "Nominatim POI HTTP ${resp.code}"))
                }
                val raw = resp.body?.string().orEmpty()
                val items = parseNominatimPoiJson(
                    rawJson = raw,
                    originLat = originLat,
                    originLon = originLon,
                    bounds = if (maxDistanceMeters == null) bounds else null,
                    maxDistanceMeters = maxDistanceMeters
                )
                Result.success(NOMINATIM_SEARCH_ENDPOINT to items)
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    private suspend fun querySingleEndpoint(
        endpoint: String,
        bodyStr: String,
        originLat: Double,
        originLon: Double,
        maxRetries: Int,
        bounds: MapBoundingBox? = null,
        maxDistanceMeters: Double? = null
    ): Result<OverpassFetchResult> {
        val mediaType = "application/x-www-form-urlencoded; charset=utf-8".toMediaType()
        var lastError: Throwable? = null

        for (attempt in 0..maxRetries) {
            try {
                val request = Request.Builder()
                    .url(endpoint)
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "application/json")
                    .post(bodyStr.toRequestBody(mediaType))
                    .build()

                val outcome = activeClient().newCall(request).execute().use { response ->
                    val code = response.code
                    if (code == 429) {
                        val retryAfterSec = response.header("Retry-After")?.toLongOrNull()
                        val delayMs = ((retryAfterSec ?: 1L) * backoffBaseMsForTest)
                            .coerceIn(0L, 1200L)
                        lastError = OverpassRateLimitException(
                            "OpenStreetMap Overpass rate limit reached (HTTP 429) on ${hostLabel(endpoint)}."
                        )
                        return@use RetryOutcome(shouldRetry = attempt < maxRetries, waitMs = delayMs)
                    }
                    if (code in 500..599) {
                        lastError = OverpassServerException(
                            code,
                            "Overpass server temporarily unavailable (HTTP $code) on ${hostLabel(endpoint)}."
                        )
                        val delayMs = backoffBaseMsForTest * (attempt + 1)
                        return@use RetryOutcome(shouldRetry = attempt < maxRetries, waitMs = delayMs)
                    }
                    if (!response.isSuccessful) {
                        lastError = OverpassServerException(
                            code,
                            "Overpass request failed with HTTP $code on ${hostLabel(endpoint)}."
                        )
                        return@use RetryOutcome(shouldRetry = false)
                    }
                    val raw = response.body?.string().orEmpty()
                    if (raw.isBlank()) {
                        lastError = OverpassParseException("Empty response from Overpass mirror ${hostLabel(endpoint)}.")
                        return@use RetryOutcome(shouldRetry = false)
                    }
                    val nowMs = System.currentTimeMillis()
                    val parsed = parseOverpassJson(raw, originLat, originLon, nowMs).filter { item ->
                        val distOk = maxDistanceMeters == null || item.distanceMeters <= maxDistanceMeters
                        val bboxOk = bounds == null || (
                            item.latitude in bounds.south..bounds.north &&
                                item.longitude in bounds.west..bounds.east
                            )
                        distOk && bboxOk
                    }
                    return@use RetryOutcome(
                        successResult = OverpassFetchResult(
                            items = parsed,
                            endpointUsed = endpoint,
                            fetchedAtMs = nowMs
                        )
                    )
                }

                if (outcome.successResult != null) {
                    return Result.success(outcome.successResult)
                }
                if (outcome.shouldRetry && outcome.waitMs > 0L) {
                    delay(outcome.waitMs)
                } else if (!outcome.shouldRetry) {
                    break
                }
            } catch (e: JSONException) {
                lastError = OverpassParseException("Invalid JSON from ${hostLabel(endpoint)}: ${e.message}", e)
                break
            } catch (e: IOException) {
                lastError = OverpassNetworkException("Network error contacting ${hostLabel(endpoint)}: ${e.message}", e)
                if (attempt < maxRetries && backoffBaseMsForTest > 0L) {
                    delay(backoffBaseMsForTest)
                }
            } catch (t: Throwable) {
                lastError = t
                break
            }
        }
        return Result.failure(
            lastError ?: OverpassNetworkException("Unable to reach ${hostLabel(endpoint)}.")
        )
    }

    private data class RetryOutcome(
        val successResult: OverpassFetchResult? = null,
        val shouldRetry: Boolean = false,
        val waitMs: Long = 0L
    )

    private fun hostLabel(url: String): String =
        runCatching { java.net.URI(url).host ?: url }.getOrDefault(url)

    fun parseOverpassJson(
        rawJson: String,
        originLat: Double,
        originLon: Double,
        fetchedAtMs: Long = System.currentTimeMillis()
    ): List<MosqueItem> {
        val trimmed = rawJson.trim()
        if (!trimmed.startsWith("{")) {
            throw OverpassParseException("Expected JSON object from Overpass API.")
        }
        val root = JSONObject(trimmed)
        if (root.has("remark") && !root.has("elements")) {
            throw OverpassParseException("Overpass remark: ${root.optString("remark")}")
        }
        val elements: JSONArray = root.optJSONArray("elements") ?: return emptyList()
        val dedupedByKey = LinkedHashMap<String, MosqueItem>()

        for (i in 0 until elements.length()) {
            val el = elements.optJSONObject(i) ?: continue
            val osmType = el.optString("type", "node").lowercase(Locale.ROOT).ifBlank { "node" }
            val osmId = el.optLong("id", -1L)
            if (osmId <= 0L) continue

            val coords = extractCoordinates(el) ?: continue
            val (lat, lon) = coords
            if (lat !in -90.0..90.0 || lon !in -180.0..180.0) continue

            val tags = el.optJSONObject("tags") ?: JSONObject()
            if (!isEligibleIslamicWorshipElement(tags)) continue
            val uniqueKey = "$osmType/$osmId"
            val name = extractName(tags)
            val category = classifyCategory(tags, name)
            val verification = classifyVerificationLevel(tags, category)
            val address = extractAddress(tags)
            val phone = nonBlankTag(tags, "phone") ?: nonBlankTag(tags, "contact:phone")
            val website = nonBlankTag(tags, "website") ?: nonBlankTag(tags, "contact:website")
            val openingHours = nonBlankTag(tags, "opening_hours")
            val wheelchair = nonBlankTag(tags, "wheelchair")
            val denomination = nonBlankTag(tags, "denomination")
            val tagsSummary = buildOsmTagsSummary(tags)

            val distance = haversineDistanceMeters(originLat, originLon, lat, lon)
            val candidate = MosqueItem(
                id = uniqueKey,
                osmType = osmType,
                osmId = osmId,
                name = name,
                latitude = lat,
                longitude = lon,
                distanceMeters = distance,
                category = category,
                address = address,
                phone = phone,
                website = website,
                openingHours = openingHours,
                wheelchair = wheelchair,
                lastUpdatedMs = fetchedAtMs,
                verificationLevel = verification,
                denomination = denomination,
                osmTagsSummary = tagsSummary
            )

            val existing = dedupedByKey[uniqueKey]
            if (existing == null || verification.ordinal < existing.verificationLevel.ordinal) {
                dedupedByKey[uniqueKey] = candidate
            }
        }
        return dedupedByKey.values.sortedBy { it.distanceMeters }
    }

    fun parsePostpassGeoJson(
        rawJson: String,
        originLat: Double,
        originLon: Double,
        fetchedAtMs: Long = System.currentTimeMillis(),
        bounds: MapBoundingBox? = null,
        maxDistanceMeters: Double? = null
    ): List<MosqueItem> {
        val trimmed = rawJson.trim()
        if (!trimmed.startsWith("{")) {
            throw OverpassParseException("Expected GeoJSON object from Postpass API.")
        }
        val root = JSONObject(trimmed)
        val features = root.optJSONArray("features") ?: return emptyList()
        val dedupedByKey = LinkedHashMap<String, MosqueItem>()

        for (i in 0 until features.length()) {
            val feature = features.optJSONObject(i) ?: continue
            val props = feature.optJSONObject("properties") ?: continue
            val rawId = props.optLong("osm_id", 0L)
            val osmId = abs(rawId)
            if (osmId <= 0L) continue

            val osmTypeRaw = props.optString("osm_type", "").trim().uppercase(Locale.ROOT)
            val osmType = when {
                osmTypeRaw == "N" || osmTypeRaw == "NODE" -> "node"
                osmTypeRaw == "W" || osmTypeRaw == "WAY" -> "way"
                osmTypeRaw == "R" || osmTypeRaw == "RELATION" || rawId < 0L -> "relation"
                else -> "node"
            }

            val geom = feature.optJSONObject("geometry") ?: continue
            val (lat, lon) = extractGeoJsonCenter(geom) ?: continue
            if (lat !in -90.0..90.0 || lon !in -180.0..180.0) continue

            val tagsObj = props.optJSONObject("tags") ?: run {
                val rawTags = props.optString("tags", "").trim()
                if (rawTags.startsWith("{")) runCatching { JSONObject(rawTags) }.getOrNull() else null
            } ?: JSONObject()

            if (!isEligibleIslamicWorshipElement(tagsObj)) continue
            if (bounds != null && (lat !in bounds.south..bounds.north || lon !in bounds.west..bounds.east)) continue
            val distance = haversineDistanceMeters(originLat, originLon, lat, lon)
            if (maxDistanceMeters != null && distance > maxDistanceMeters) continue

            val uniqueKey = "$osmType/$osmId"
            val name = extractName(tagsObj)
            val category = classifyCategory(tagsObj, name)
            val verification = classifyVerificationLevel(tagsObj, category)
            val candidate = MosqueItem(
                id = uniqueKey,
                osmType = osmType,
                osmId = osmId,
                name = name,
                latitude = lat,
                longitude = lon,
                distanceMeters = distance,
                category = category,
                address = extractAddress(tagsObj),
                phone = nonBlankTag(tagsObj, "phone") ?: nonBlankTag(tagsObj, "contact:phone"),
                website = nonBlankTag(tagsObj, "website") ?: nonBlankTag(tagsObj, "contact:website"),
                openingHours = nonBlankTag(tagsObj, "opening_hours"),
                wheelchair = nonBlankTag(tagsObj, "wheelchair"),
                lastUpdatedMs = fetchedAtMs,
                verificationLevel = verification,
                denomination = nonBlankTag(tagsObj, "denomination"),
                osmTagsSummary = buildOsmTagsSummary(tagsObj)
            )
            val existing = dedupedByKey[uniqueKey]
            if (existing == null || verification.ordinal < existing.verificationLevel.ordinal) {
                dedupedByKey[uniqueKey] = candidate
            }
        }
        return dedupedByKey.values.sortedBy { it.distanceMeters }
    }

    fun parsePhotonGeoJson(
        rawJson: String,
        originLat: Double,
        originLon: Double,
        fetchedAtMs: Long = System.currentTimeMillis(),
        bounds: MapBoundingBox? = null,
        maxDistanceMeters: Double? = null
    ): List<MosqueItem> {
        val trimmed = rawJson.trim()
        if (!trimmed.startsWith("{")) return emptyList()
        val root = JSONObject(trimmed)
        val features = root.optJSONArray("features") ?: return emptyList()
        val dedupedByKey = LinkedHashMap<String, MosqueItem>()

        for (i in 0 until features.length()) {
            val feature = features.optJSONObject(i) ?: continue
            val props = feature.optJSONObject("properties") ?: continue
            val rawId = props.optLong("osm_id", 0L)
            val osmId = abs(rawId)
            if (osmId <= 0L) continue

            val osmKey = props.optString("osm_key", "").lowercase(Locale.ROOT)
            val osmValue = props.optString("osm_value", "").lowercase(Locale.ROOT)
            val isWorshipTag = (osmKey == "amenity" && (osmValue == "place_of_worship" || osmValue == "prayer_room")) ||
                (osmKey == "building" && osmValue == "mosque")
            if (!isWorshipTag) continue

            val name = props.optString("name", "").trim()
            if (!isConfirmedIslamicPoiName(name)) continue

            val geom = feature.optJSONObject("geometry") ?: continue
            val (lat, lon) = extractGeoJsonCenter(geom) ?: continue
            if (lat !in -90.0..90.0 || lon !in -180.0..180.0) continue
            if (bounds != null && (lat !in bounds.south..bounds.north || lon !in bounds.west..bounds.east)) continue

            val distance = haversineDistanceMeters(originLat, originLon, lat, lon)
            if (maxDistanceMeters != null && distance > maxDistanceMeters) continue

            val osmTypeRaw = props.optString("osm_type", "N").trim().uppercase(Locale.ROOT)
            val osmType = when (osmTypeRaw) {
                "W", "WAY" -> "way"
                "R", "RELATION" -> "relation"
                else -> "node"
            }
            val syntheticTags = JSONObject().apply {
                put("amenity", if (osmValue == "prayer_room") "prayer_room" else "place_of_worship")
                put("religion", "muslim")
                if (osmKey == "building" && osmValue == "mosque") put("building", "mosque")
                put("name", name)
            }
            val category = classifyCategory(syntheticTags, name)
            val verification = classifyVerificationLevel(syntheticTags, category)
            val addrParts = listOfNotNull(
                props.optString("housenumber", "").trim().takeIf { it.isNotBlank() },
                props.optString("street", "").trim().takeIf { it.isNotBlank() },
                props.optString("district", "").trim().takeIf { it.isNotBlank() }
                    ?: props.optString("locality", "").trim().takeIf { it.isNotBlank() },
                props.optString("city", "").trim().takeIf { it.isNotBlank() }
                    ?: props.optString("county", "").trim().takeIf { it.isNotBlank() }
            )
            val uniqueKey = "$osmType/$osmId"
            val item = MosqueItem(
                id = uniqueKey,
                osmType = osmType,
                osmId = osmId,
                name = name,
                latitude = lat,
                longitude = lon,
                distanceMeters = distance,
                category = category,
                address = addrParts.takeIf { it.isNotEmpty() }?.joinToString(", "),
                phone = null,
                website = null,
                openingHours = null,
                wheelchair = null,
                lastUpdatedMs = fetchedAtMs,
                verificationLevel = verification,
                denomination = null,
                osmTagsSummary = "amenity=${syntheticTags.optString("amenity")}, religion=muslim"
            )
            dedupedByKey.putIfAbsent(uniqueKey, item)
        }
        return dedupedByKey.values.sortedBy { it.distanceMeters }
    }

    fun parseNominatimPoiJson(
        rawJson: String,
        originLat: Double,
        originLon: Double,
        fetchedAtMs: Long = System.currentTimeMillis(),
        bounds: MapBoundingBox? = null,
        maxDistanceMeters: Double? = null
    ): List<MosqueItem> {
        val trimmed = rawJson.trim()
        if (!trimmed.startsWith("[")) return emptyList()
        val arr = JSONArray(trimmed)
        val dedupedByKey = LinkedHashMap<String, MosqueItem>()

        for (i in 0 until arr.length()) {
            val obj = arr.optJSONObject(i) ?: continue
            val osmId = abs(obj.optLong("osm_id", 0L))
            if (osmId <= 0L) continue

            val osmTypeRaw = obj.optString("osm_type", "node").lowercase(Locale.ROOT)
            val osmType = when (osmTypeRaw) {
                "way", "w" -> "way"
                "relation", "r" -> "relation"
                else -> "node"
            }

            val lat = obj.optString("lat", "").toDoubleOrNull() ?: obj.optDouble("lat", Double.NaN)
            val lon = obj.optString("lon", "").toDoubleOrNull() ?: obj.optDouble("lon", Double.NaN)
            if (lat.isNaN() || lon.isNaN() || lat !in -90.0..90.0 || lon !in -180.0..180.0) continue
            if (bounds != null && (lat !in bounds.south..bounds.north || lon !in bounds.west..bounds.east)) continue

            val distance = haversineDistanceMeters(originLat, originLon, lat, lon)
            if (maxDistanceMeters != null && distance > maxDistanceMeters) continue

            val extratags = obj.optJSONObject("extratags") ?: JSONObject()
            val placeClass = obj.optString("class", obj.optString("category", "")).lowercase(Locale.ROOT)
            val placeType = obj.optString("type", "").lowercase(Locale.ROOT)
            val rawName = obj.optString("name", "").trim().ifBlank {
                obj.optString("display_name", "").substringBefore(",").trim()
            }

            val religion = extratags.optString("religion", "").lowercase(Locale.ROOT)
            if (religion.isNotBlank() && religion != "muslim") continue

            val tagsObj = JSONObject().apply {
                if (placeClass == "amenity") put("amenity", placeType)
                if (placeClass == "building") put("building", placeType)
                extratags.keys().forEach { key ->
                    put(key, extratags.optString(key, ""))
                }
                if (rawName.isNotBlank() && optString("name", "").isBlank()) {
                    put("name", rawName)
                }
            }

            val isEligible = isEligibleIslamicWorshipElement(tagsObj) || (
                religion.isBlank() &&
                    (placeType == "place_of_worship" || placeType == "prayer_room" || placeType == "mosque") &&
                    isConfirmedIslamicPoiName(rawName)
                )
            if (!isEligible) continue

            if (tagsObj.optString("religion", "").isBlank() && isConfirmedIslamicPoiName(rawName)) {
                tagsObj.put("religion", "muslim")
            }

            val name = extractName(tagsObj)
            val category = classifyCategory(tagsObj, name)
            val verification = classifyVerificationLevel(tagsObj, category)

            val addrObj = obj.optJSONObject("address")
            val address = extractAddress(tagsObj) ?: addrObj?.let { a ->
                listOfNotNull(
                    nonBlankTag(a, "house_number"),
                    nonBlankTag(a, "road") ?: nonBlankTag(a, "street"),
                    nonBlankTag(a, "suburb") ?: nonBlankTag(a, "neighbourhood"),
                    nonBlankTag(a, "city") ?: nonBlankTag(a, "town") ?: nonBlankTag(a, "county")
                ).takeIf { it.isNotEmpty() }?.joinToString(", ")
            }

            val uniqueKey = "$osmType/$osmId"
            val item = MosqueItem(
                id = uniqueKey,
                osmType = osmType,
                osmId = osmId,
                name = name,
                latitude = lat,
                longitude = lon,
                distanceMeters = distance,
                category = category,
                address = address,
                phone = nonBlankTag(tagsObj, "phone") ?: nonBlankTag(tagsObj, "contact:phone"),
                website = nonBlankTag(tagsObj, "website") ?: nonBlankTag(tagsObj, "contact:website"),
                openingHours = nonBlankTag(tagsObj, "opening_hours"),
                wheelchair = nonBlankTag(tagsObj, "wheelchair"),
                lastUpdatedMs = fetchedAtMs,
                verificationLevel = verification,
                denomination = nonBlankTag(tagsObj, "denomination"),
                osmTagsSummary = buildOsmTagsSummary(tagsObj)
            )
            val existing = dedupedByKey[uniqueKey]
            if (existing == null || verification.ordinal < existing.verificationLevel.ordinal) {
                dedupedByKey[uniqueKey] = item
            }
        }
        return dedupedByKey.values.sortedBy { it.distanceMeters }
    }

    private fun extractGeoJsonCenter(geometry: JSONObject): Pair<Double, Double>? {
        val type = geometry.optString("type", "")
        val coords = geometry.optJSONArray("coordinates") ?: return null
        return when (type) {
            "Point" -> {
                if (coords.length() < 2) return null
                val lon = coords.optDouble(0, Double.NaN)
                val lat = coords.optDouble(1, Double.NaN)
                if (lat.isNaN() || lon.isNaN()) null else lat to lon
            }
            "MultiPoint", "LineString" -> {
                if (coords.length() == 0) return null
                averageLonLatRing(coords)
            }
            "Polygon" -> {
                val outerRing = coords.optJSONArray(0) ?: return null
                averageLonLatRing(outerRing)
            }
            "MultiPolygon" -> {
                val firstPoly = coords.optJSONArray(0) ?: return null
                val outerRing = firstPoly.optJSONArray(0) ?: return null
                averageLonLatRing(outerRing)
            }
            else -> null
        }
    }

    private fun averageLonLatRing(ring: JSONArray): Pair<Double, Double>? {
        var sumLat = 0.0
        var sumLon = 0.0
        var count = 0
        for (i in 0 until ring.length()) {
            val pt = ring.optJSONArray(i) ?: continue
            if (pt.length() < 2) continue
            val lon = pt.optDouble(0, Double.NaN)
            val lat = pt.optDouble(1, Double.NaN)
            if (!lat.isNaN() && !lon.isNaN()) {
                sumLat += lat
                sumLon += lon
                count += 1
            }
        }
        return if (count > 0) (sumLat / count) to (sumLon / count) else null
    }

    private fun isConfirmedIslamicPoiName(name: String): Boolean {
        val lower = name.trim().lowercase(Locale.ROOT)
        if (lower.isBlank()) return false
        val excludedNonMuslimKeywords = listOf(
            "church", "chapel", "cathedral", "parish", "baptist", "methodist",
            "lutheran", "presbyterian", "catholic", "anglican", "kingdom hall",
            "temple", "mandir", "gurdwara", "synagogue", "shrine", "pagoda", "monastery"
        )
        if (excludedNonMuslimKeywords.any { lower.contains(it) }) return false

        val positiveIslamicKeywords = listOf(
            "masjid", "mosque", "jamia", "jame", "jami", "musalla", "musalah",
            "prayer room", "namaz", "eidgah", "eid gah", "imambargah", "markaz",
            "مسجد", "جامع", "مصلى", "مصلی", "عید گاہ", "عیدگاہ", "نماز"
        )
        return positiveIslamicKeywords.any { lower.contains(it) }
    }

    private fun extractCoordinates(el: JSONObject): Pair<Double, Double>? {
        val centerObj = el.optJSONObject("center")
        val boundsObj = el.optJSONObject("bounds")
        val lat = when {
            el.has("lat") -> el.optDouble("lat", Double.NaN)
            centerObj != null -> centerObj.optDouble("lat", Double.NaN)
            boundsObj != null -> {
                val minLat = boundsObj.optDouble("minlat", Double.NaN)
                val maxLat = boundsObj.optDouble("maxlat", Double.NaN)
                if (!minLat.isNaN() && !maxLat.isNaN()) (minLat + maxLat) / 2.0 else Double.NaN
            }
            else -> Double.NaN
        }
        val lon = when {
            el.has("lon") -> el.optDouble("lon", Double.NaN)
            centerObj != null -> centerObj.optDouble("lon", Double.NaN)
            boundsObj != null -> {
                val minLon = boundsObj.optDouble("minlon", Double.NaN)
                val maxLon = boundsObj.optDouble("maxlon", Double.NaN)
                if (!minLon.isNaN() && !maxLon.isNaN()) (minLon + maxLon) / 2.0 else Double.NaN
            }
            else -> Double.NaN
        }
        if (lat.isNaN() || lon.isNaN()) return null
        return lat to lon
    }

    fun isEligibleIslamicWorshipElement(tags: JSONObject): Boolean {
        val amenity = tags.optString("amenity", "").lowercase(Locale.ROOT)
        val religion = tags.optString("religion", "").lowercase(Locale.ROOT)
        val building = tags.optString("building", "").lowercase(Locale.ROOT)
        val musalla = tags.optString("musalla", "").lowercase(Locale.ROOT)
        val muslim = tags.optString("muslim", "").lowercase(Locale.ROOT)

        if (religion.isNotBlank() && religion != "muslim") {
            return false
        }
        return (amenity == "place_of_worship" && religion == "muslim") ||
            (building == "place_of_worship" && religion == "muslim") ||
            building == "mosque" ||
            (amenity == "prayer_room" && (religion == "muslim" || muslim == "yes")) ||
            musalla == "yes"
    }

    fun classifyVerificationLevel(tags: JSONObject, category: MosqueCategory): OsmVerificationLevel {
        val amenity = tags.optString("amenity", "").lowercase(Locale.ROOT)
        val religion = tags.optString("religion", "").lowercase(Locale.ROOT)
        val building = tags.optString("building", "").lowercase(Locale.ROOT)
        val musalla = tags.optString("musalla", "").lowercase(Locale.ROOT)

        return when {
            (amenity == "place_of_worship" || building == "place_of_worship") && religion == "muslim" ->
                OsmVerificationLevel.VERIFIED_MUSLIM_PLACE_OF_WORSHIP
            amenity == "prayer_room" || musalla == "yes" || category == MosqueCategory.PRAYER_ROOM ->
                OsmVerificationLevel.PRAYER_ROOM_MUSALLA
            building == "mosque" ->
                OsmVerificationLevel.BUILDING_MOSQUE_CANDIDATE
            else -> OsmVerificationLevel.VERIFIED_MUSLIM_PLACE_OF_WORSHIP
        }
    }

    private fun buildOsmTagsSummary(tags: JSONObject): String {
        val parts = mutableListOf<String>()
        nonBlankTag(tags, "amenity")?.let { parts.add("amenity=$it") }
        nonBlankTag(tags, "religion")?.let { parts.add("religion=$it") }
        nonBlankTag(tags, "building")?.let { parts.add("building=$it") }
        nonBlankTag(tags, "musalla")?.let { parts.add("musalla=$it") }
        return parts.joinToString(", ")
    }

    private fun extractName(tags: JSONObject): String {
        val candidates = listOf(
            tags.optString("name", ""),
            tags.optString("name:en", ""),
            tags.optString("name:ur", ""),
            tags.optString("name:ar", ""),
            tags.optString("official_name", "")
        )
        return candidates.firstOrNull { it.isNotBlank() }?.trim()
            ?: "Unnamed Mosque (OpenStreetMap)"
    }

    fun classifyCategory(tags: JSONObject, resolvedName: String): MosqueCategory {
        val nameLower = resolvedName.lowercase(Locale.ROOT)
        val amenity = tags.optString("amenity", "").lowercase(Locale.ROOT)
        val building = tags.optString("building", "").lowercase(Locale.ROOT)
        val designation = tags.optString("designation", "").lowercase(Locale.ROOT)
        val mosqueTag = tags.optString("mosque", "").lowercase(Locale.ROOT)
        val musalla = tags.optString("musalla", "").lowercase(Locale.ROOT)
        val denomination = tags.optString("denomination", "").lowercase(Locale.ROOT)
        val powType = tags.optString("place_of_worship:type", "").lowercase(Locale.ROOT)

        if (nameLower.contains("eidgah") ||
            nameLower.contains("eid gah") ||
            nameLower.contains("eid ground") ||
            nameLower.contains("عید گاہ") ||
            nameLower.contains("عیدگاہ") ||
            nameLower.contains("مصلى العيد") ||
            mosqueTag == "eidgah"
        ) {
            return MosqueCategory.EID_GROUND
        }

        if (amenity == "prayer_room" ||
            building == "prayer_room" ||
            musalla == "yes" ||
            powType == "musalla" ||
            nameLower.contains("prayer room") ||
            nameLower.contains("musalla") ||
            nameLower.contains("namaz") ||
            nameLower.contains("نماز گاہ") ||
            nameLower.contains("مصلى")
        ) {
            return MosqueCategory.PRAYER_ROOM
        }

        if (mosqueTag == "jami" ||
            mosqueTag == "juma" ||
            designation.contains("jami") ||
            denomination.contains("jami") ||
            nameLower.contains("jami ") ||
            nameLower.contains("jamia") ||
            nameLower.contains("jame ") ||
            nameLower.contains("juma ") ||
            nameLower.contains("central mosque") ||
            nameLower.contains("جامع") ||
            nameLower.contains("जामा")
        ) {
            return MosqueCategory.JAMI_MOSQUE
        }

        return MosqueCategory.MOSQUE
    }

    private fun extractAddress(tags: JSONObject): String? {
        nonBlankTag(tags, "addr:full")?.let { return it }
        val parts = listOfNotNull(
            nonBlankTag(tags, "addr:housenumber"),
            nonBlankTag(tags, "addr:street"),
            nonBlankTag(tags, "addr:suburb") ?: nonBlankTag(tags, "addr:neighbourhood"),
            nonBlankTag(tags, "addr:city") ?: nonBlankTag(tags, "addr:town")
        )
        return parts.takeIf { it.isNotEmpty() }?.joinToString(", ")
    }

    private fun nonBlankTag(tags: JSONObject, key: String): String? {
        val v = tags.optString(key, "").trim()
        return v.takeIf { it.isNotBlank() }
    }

    fun haversineDistanceMeters(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadiusM = 6_371_000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val rLat1 = Math.toRadians(lat1)
        val rLat2 = Math.toRadians(lat2)
        val a = sin(dLat / 2).pow(2.0) + cos(rLat1) * cos(rLat2) * sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusM * c
    }
}
