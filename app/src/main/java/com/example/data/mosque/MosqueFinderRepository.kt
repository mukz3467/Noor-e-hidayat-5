package com.example.data.mosque

import android.content.Context
import com.example.data.location.AppLocationManager
import com.example.data.location.LocationSource
import com.example.data.location.UserLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.Locale

object MosqueFinderRepository {

    const val CACHE_STALE_AGE_MS = 24 * 60 * 60 * 1000L // 24 hours
    const val DEDUP_WINDOW_MS = 1800L
    const val DEFAULT_PAGE_SIZE = 20
    val RADIUS_OPTIONS_METERS = listOf(1000, 3000, 5000, 10_000)

    fun nextLargerRadiusMeters(currentRadiusMeters: Int): Int? =
        RADIUS_OPTIONS_METERS.firstOrNull { it > currentRadiusMeters }

    private const val CACHE_DIR_NAME = "mosque_cache"
    private const val LAST_SEARCH_FILE = "last_mosque_search.json"
    private const val PREFS_NAME = "mosque_finder_prefs"
    private const val KEY_ENDPOINT_URL = "overpass_preferred_endpoint"

    private val _state = MutableStateFlow(MosqueSearchState())
    val state: StateFlow<MosqueSearchState> = _state.asStateFlow()

    private val _routeState = MutableStateFlow(MosqueRouteState())
    val routeState: StateFlow<MosqueRouteState> = _routeState.asStateFlow()

    private val _selectedFootprint = MutableStateFlow<Pair<String, List<Pair<Double, Double>>>?>(null)
    val selectedFootprint: StateFlow<Pair<String, List<Pair<Double, Double>>>?> = _selectedFootprint.asStateFlow()

    private val footprintCache = LinkedHashMap<String, List<Pair<Double, Double>>>(32, 0.75f, true)

    @Volatile
    private var inFlightQueryKey: String? = null
    @Volatile
    private var lastCompletedQueryKey: String? = null
    @Volatile
    private var lastCompletedTimestampMs: Long = 0L

    internal fun resetForTest() {
        inFlightQueryKey = null
        lastCompletedQueryKey = null
        lastCompletedTimestampMs = 0L
        _state.value = MosqueSearchState()
        _routeState.value = MosqueRouteState()
        _selectedFootprint.value = null
        synchronized(footprintCache) { footprintCache.clear() }
    }

    /**
     * Strictly distinguishes real GPS/Network fixes or explicit user selections from
     * AppLocationManager's timezone DEFAULT_ESTIMATE fallback.
     */
    fun hasConfirmedCoordinates(location: UserLocation): Boolean {
        if (location.sourceType == LocationSource.DEFAULT_ESTIMATE) return false
        if (location.latitude == 0.0 && location.longitude == 0.0) return false
        return location.latitude in -90.0..90.0 && location.longitude in -180.0..180.0
    }

    fun getPreferredEndpoint(context: Context): String? {
        val saved = context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ENDPOINT_URL, null)
            ?.takeIf { it.isNotBlank() }
        if (_state.value.preferredEndpoint != saved) {
            _state.value = _state.value.copy(preferredEndpoint = saved)
        }
        return saved
    }

    fun setPreferredEndpoint(context: Context, endpointUrl: String?) {
        val clean = endpointUrl?.takeIf { it.isNotBlank() }
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().apply {
                if (clean == null) remove(KEY_ENDPOINT_URL) else putString(KEY_ENDPOINT_URL, clean)
            }.apply()
        _state.value = _state.value.copy(
            preferredEndpoint = clean,
            activeEndpoint = clean ?: OverpassMosqueService.DEFAULT_ENDPOINTS.first()
        )
    }

    fun resolvePrecisionMode(context: Context): LocationPrecisionMode {
        val hasFine = AppLocationManager.hasFineLocationPermission(context)
        val hasCoarse = AppLocationManager.hasCoarseLocationPermission(context)
        return when {
            hasFine -> LocationPrecisionMode.GPS_PRECISE
            hasCoarse -> LocationPrecisionMode.APPROXIMATE
            else -> LocationPrecisionMode.PERMISSION_DENIED
        }
    }

    fun refreshPermissionState(context: Context) {
        val mode = resolvePrecisionMode(context)
        val prefEndpoint = getPreferredEndpoint(context)
        _state.value = _state.value.copy(
            precisionMode = mode,
            preferredEndpoint = prefEndpoint,
            activeEndpoint = _state.value.activeEndpoint.ifBlank {
                prefEndpoint ?: OverpassMosqueService.DEFAULT_ENDPOINTS.first()
            }
        )
    }

    fun loadLastCachedSession(context: Context): Boolean {
        val cacheFile = File(cacheDir(context), LAST_SEARCH_FILE)
        if (!cacheFile.exists()) return false
        return try {
            val root = JSONObject(cacheFile.readText())
            val ts = root.optLong("timestampMs", 0L)
            val lat = root.optDouble("lat", Double.NaN)
            val lon = root.optDouble("lon", Double.NaN)
            val radius = root.optInt("radiusMeters", 5000)
            val label = root.optString("label", "")
            val endpoint = root.optString("endpoint", OverpassMosqueService.DEFAULT_ENDPOINTS.first())
            val items = decodeMosques(root.optJSONArray("mosques") ?: JSONArray(), lat, lon, ts)
            if (lat.isNaN() || lon.isNaN()) return false

            val now = System.currentTimeMillis()
            val isStale = (now - ts) > CACHE_STALE_AGE_MS
            _state.value = _state.value.copy(
                mosques = items,
                isLoading = false,
                isFromCache = true,
                isFreshLiveResult = false,
                cacheTimestampMs = ts,
                isCacheStale = isStale,
                searchCenterLat = lat,
                searchCenterLon = lon,
                searchLabel = label,
                radiusMeters = radius,
                precisionMode = resolvePrecisionMode(context),
                hasSearched = true,
                activeEndpoint = endpoint
            )
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun searchNearbyMosques(
        context: Context,
        lat: Double,
        lon: Double,
        radiusMeters: Int,
        label: String,
        precisionMode: LocationPrecisionMode = resolvePrecisionMode(context),
        forceRefresh: Boolean = false
    ) {
        val queryKey = String.format(Locale.US, "around:%.4f:%.4f:%d", lat, lon, radiusMeters)
        val nowCheck = System.currentTimeMillis()
        if (!forceRefresh &&
            (inFlightQueryKey == queryKey ||
                (lastCompletedQueryKey == queryKey && (nowCheck - lastCompletedTimestampMs) < DEDUP_WINDOW_MS))
        ) {
            return
        }

        inFlightQueryKey = queryKey
        val preferredEndpoint = getPreferredEndpoint(context)
        _state.value = _state.value.copy(
            isLoading = true,
            errorMessage = null,
            isRateLimited = false,
            searchCenterLat = lat,
            searchCenterLon = lon,
            searchLabel = label,
            radiusMeters = radiusMeters,
            precisionMode = precisionMode,
            lastQueryMode = "around"
        )

        val liveResult = OverpassMosqueService.fetchNearbyMosquesDetailed(
            lat = lat,
            lon = lon,
            radiusMeters = radiusMeters,
            preferredEndpoint = preferredEndpoint
        )
        inFlightQueryKey = null
        handleOverpassOutcome(
            context = context,
            liveResult = liveResult,
            queryKey = queryKey,
            centerLat = lat,
            centerLon = lon,
            radiusMeters = radiusMeters,
            label = label,
            precisionMode = precisionMode
        )
    }

    suspend fun searchMosquesInVisibleBounds(
        context: Context,
        bounds: MapBoundingBox,
        distanceOriginLat: Double,
        distanceOriginLon: Double,
        label: String,
        precisionMode: LocationPrecisionMode = resolvePrecisionMode(context),
        forceRefresh: Boolean = false
    ) {
        if (!bounds.isValid()) return
        val queryKey = String.format(
            Locale.US,
            "bbox:%.3f:%.3f:%.3f:%.3f",
            bounds.south, bounds.west, bounds.north, bounds.east
        )
        val nowCheck = System.currentTimeMillis()
        if (!forceRefresh &&
            (inFlightQueryKey == queryKey ||
                (lastCompletedQueryKey == queryKey && (nowCheck - lastCompletedTimestampMs) < DEDUP_WINDOW_MS))
        ) {
            return
        }

        inFlightQueryKey = queryKey
        val preferredEndpoint = getPreferredEndpoint(context)
        _state.value = _state.value.copy(
            isLoading = true,
            errorMessage = null,
            isRateLimited = false,
            searchCenterLat = bounds.centerLat,
            searchCenterLon = bounds.centerLon,
            searchLabel = label,
            precisionMode = precisionMode,
            lastQueryMode = "bbox"
        )

        val liveResult = OverpassMosqueService.fetchMosquesInBoundsDetailed(
            bounds = bounds,
            distanceOriginLat = distanceOriginLat,
            distanceOriginLon = distanceOriginLon,
            preferredEndpoint = preferredEndpoint
        )
        inFlightQueryKey = null
        handleOverpassOutcome(
            context = context,
            liveResult = liveResult,
            queryKey = queryKey,
            centerLat = distanceOriginLat,
            centerLon = distanceOriginLon,
            radiusMeters = _state.value.radiusMeters,
            label = label,
            precisionMode = precisionMode
        )
    }

    private fun handleOverpassOutcome(
        context: Context,
        liveResult: Result<OverpassFetchResult>,
        queryKey: String,
        centerLat: Double,
        centerLon: Double,
        radiusMeters: Int,
        label: String,
        precisionMode: LocationPrecisionMode
    ) {
        if (liveResult.isSuccess) {
            val payload = liveResult.getOrNull()!!
            val cleanSorted = OverpassMosqueService.deduplicateMosques(payload.items)
            lastCompletedQueryKey = queryKey
            lastCompletedTimestampMs = payload.fetchedAtMs
            saveCache(
                context = context,
                lat = centerLat,
                lon = centerLon,
                radiusMeters = radiusMeters,
                label = label,
                endpoint = payload.endpointUsed,
                items = cleanSorted,
                timestampMs = payload.fetchedAtMs
            )
            _state.value = _state.value.copy(
                mosques = cleanSorted,
                isLoading = false,
                errorMessage = null,
                isFromCache = false,
                isFreshLiveResult = true,
                cacheTimestampMs = payload.fetchedAtMs,
                isCacheStale = false,
                searchCenterLat = centerLat,
                searchCenterLon = centerLon,
                searchLabel = label,
                radiusMeters = radiusMeters,
                precisionMode = precisionMode,
                hasSearched = true,
                activeEndpoint = payload.endpointUsed,
                isRateLimited = false
            )
            return
        }

        val throwable = liveResult.exceptionOrNull()
        val isRateLimit = throwable is OverpassRateLimitException
        val err = throwable?.message ?: "Unable to reach OpenStreetMap Overpass servers."

        val cached = loadMatchingOrLastCache(context, centerLat, centerLon, radiusMeters)
        if (cached != null) {
            val recomputed = cached.items.map { item ->
                item.copy(
                    distanceMeters = OverpassMosqueService.haversineDistanceMeters(
                        centerLat, centerLon, item.latitude, item.longitude
                    )
                )
            }.sortedBy { it.distanceMeters }
            _state.value = _state.value.copy(
                mosques = recomputed,
                isLoading = false,
                errorMessage = err,
                isFromCache = true,
                isFreshLiveResult = false,
                cacheTimestampMs = cached.timestampMs,
                isCacheStale = true,
                searchLabel = label.ifBlank { cached.label },
                precisionMode = precisionMode,
                hasSearched = true,
                activeEndpoint = cached.endpoint,
                isRateLimited = isRateLimit
            )
        } else {
            _state.value = _state.value.copy(
                mosques = emptyList(),
                isLoading = false,
                errorMessage = err,
                isFromCache = false,
                isFreshLiveResult = false,
                cacheTimestampMs = null,
                isCacheStale = false,
                precisionMode = precisionMode,
                hasSearched = true,
                isRateLimited = isRateLimit
            )
        }
    }

    fun filterMosques(
        mosques: List<MosqueItem>,
        keyword: String,
        selectedCategory: MosqueCategory?
    ): List<MosqueItem> {
        val q = keyword.trim().lowercase(Locale.ROOT)
        return mosques.filter { item ->
            val catMatch = selectedCategory == null || item.category == selectedCategory
            val textMatch = q.isBlank() ||
                item.name.lowercase(Locale.ROOT).contains(q) ||
                (item.address?.lowercase(Locale.ROOT)?.contains(q) == true)
            catMatch && textMatch
        }
    }

    fun formatStraightLineDistance(distanceMeters: Double): String {
        return if (distanceMeters < 1000.0) {
            "${distanceMeters.toInt()} m"
        } else {
            String.format(Locale.US, "%.2f km", distanceMeters / 1000.0)
        }
    }

    suspend fun fetchRouteToMosque(
        originLat: Double,
        originLon: Double,
        mosque: MosqueItem,
        profile: OSRMRouteProfile
    ) {
        if ((originLat == 0.0 && originLon == 0.0) ||
            originLat !in -90.0..90.0 || originLon !in -180.0..180.0
        ) {
            _routeState.value = MosqueRouteState(
                targetMosqueId = mosque.id,
                targetMosqueName = mosque.name,
                profile = profile,
                isLoading = false,
                route = null,
                errorMessage = "Valid origin coordinates are required to compute a road route."
            )
            return
        }

        _routeState.value = MosqueRouteState(
            targetMosqueId = mosque.id,
            targetMosqueName = mosque.name,
            profile = profile,
            isLoading = true,
            route = null,
            errorMessage = null
        )

        val res = OsrmRoutingService.fetchRoute(
            originLat = originLat,
            originLon = originLon,
            destLat = mosque.latitude,
            destLon = mosque.longitude,
            profile = profile
        )
        res.onSuccess { route ->
            _routeState.value = MosqueRouteState(
                targetMosqueId = mosque.id,
                targetMosqueName = mosque.name,
                profile = profile,
                isLoading = false,
                route = route,
                errorMessage = null
            )
        }.onFailure { err ->
            _routeState.value = MosqueRouteState(
                targetMosqueId = mosque.id,
                targetMosqueName = mosque.name,
                profile = profile,
                isLoading = false,
                route = null,
                errorMessage = err.message ?: "Unable to fetch OSRM route."
            )
        }
    }

    fun clearRoute() {
        _routeState.value = MosqueRouteState()
    }

    suspend fun fetchFootprintForMosque(mosque: MosqueItem) {
        val typeLower = mosque.osmType.lowercase(Locale.ROOT)
        if (typeLower != "way" && typeLower != "relation") {
            _selectedFootprint.value = null
            return
        }
        synchronized(footprintCache) {
            val cached = footprintCache[mosque.id]
            if (cached != null) {
                _selectedFootprint.value = if (cached.size >= 3) mosque.id to cached else null
                return
            }
        }
        val res = OverpassMosqueService.fetchElementFootprint(
            osmType = typeLower,
            osmId = mosque.osmId,
            preferredEndpoint = _state.value.preferredEndpoint
        )
        res.onSuccess { pts ->
            synchronized(footprintCache) {
                footprintCache[mosque.id] = pts
            }
            _selectedFootprint.value = if (pts.size >= 3) mosque.id to pts else null
        }.onFailure {
            _selectedFootprint.value = null
        }
    }

    fun clearFootprint() {
        _selectedFootprint.value = null
    }

    private fun cacheDir(context: Context): File =
        File(context.applicationContext.filesDir, CACHE_DIR_NAME).apply { mkdirs() }

    private fun regionCacheFile(context: Context, lat: Double, lon: Double, radiusMeters: Int): File {
        val key = String.format(Locale.US, "r_%.2f_%.2f_%d.json", lat, lon, radiusMeters)
        return File(cacheDir(context), key)
    }

    private data class CachedBundle(
        val timestampMs: Long,
        val items: List<MosqueItem>,
        val label: String,
        val endpoint: String
    )

    private fun saveCache(
        context: Context,
        lat: Double,
        lon: Double,
        radiusMeters: Int,
        label: String,
        endpoint: String,
        items: List<MosqueItem>,
        timestampMs: Long
    ) {
        try {
            val payload = JSONObject().apply {
                put("timestampMs", timestampMs)
                put("lat", lat)
                put("lon", lon)
                put("radiusMeters", radiusMeters)
                put("label", label)
                put("endpoint", endpoint)
                put("mosques", encodeMosques(items))
            }.toString()
            regionCacheFile(context, lat, lon, radiusMeters).writeText(payload)
            File(cacheDir(context), LAST_SEARCH_FILE).writeText(payload)
        } catch (_: Exception) {}
    }

    private fun loadMatchingOrLastCache(
        context: Context,
        lat: Double,
        lon: Double,
        radiusMeters: Int
    ): CachedBundle? {
        val candidates = listOf(
            regionCacheFile(context, lat, lon, radiusMeters),
            File(cacheDir(context), LAST_SEARCH_FILE)
        )
        for (file in candidates) {
            if (!file.exists()) continue
            try {
                val root = JSONObject(file.readText())
                val ts = root.optLong("timestampMs", 0L)
                val label = root.optString("label", "")
                val endpoint = root.optString("endpoint", OverpassMosqueService.DEFAULT_ENDPOINTS.first())
                val items = decodeMosques(root.optJSONArray("mosques") ?: JSONArray(), lat, lon, ts)
                if (items.isNotEmpty() || ts > 0L) {
                    return CachedBundle(ts, items, label, endpoint)
                }
            } catch (_: Exception) {}
        }
        return null
    }

    private fun encodeMosques(items: List<MosqueItem>): JSONArray {
        val arr = JSONArray()
        items.forEach { m ->
            arr.put(
                JSONObject().apply {
                    put("id", m.id)
                    put("osmType", m.osmType)
                    put("osmId", m.osmId)
                    put("name", m.name)
                    put("lat", m.latitude)
                    put("lon", m.longitude)
                    put("category", m.category.name)
                    put("verificationLevel", m.verificationLevel.name)
                    m.address?.let { put("address", it) }
                    m.phone?.let { put("phone", it) }
                    m.website?.let { put("website", it) }
                    m.openingHours?.let { put("openingHours", it) }
                    m.wheelchair?.let { put("wheelchair", it) }
                    m.denomination?.let { put("denomination", it) }
                    if (m.osmTagsSummary.isNotBlank()) put("osmTagsSummary", m.osmTagsSummary)
                    put("lastUpdatedMs", m.lastUpdatedMs)
                }
            )
        }
        return arr
    }

    private fun decodeMosques(
        arr: JSONArray,
        originLat: Double,
        originLon: Double,
        fallbackTs: Long
    ): List<MosqueItem> {
        val list = mutableListOf<MosqueItem>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val lat = o.optDouble("lat", Double.NaN)
            val lon = o.optDouble("lon", Double.NaN)
            if (lat.isNaN() || lon.isNaN()) continue
            val osmType = o.optString("osmType", "node")
            val osmId = o.optLong("osmId", 0L)
            if (osmId <= 0L) continue
            val cat = runCatching { MosqueCategory.valueOf(o.optString("category")) }
                .getOrDefault(MosqueCategory.MOSQUE)
            val ver = runCatching { OsmVerificationLevel.valueOf(o.optString("verificationLevel")) }
                .getOrDefault(OsmVerificationLevel.VERIFIED_MUSLIM_PLACE_OF_WORSHIP)
            val dist = if (!originLat.isNaN() && !originLon.isNaN()) {
                OverpassMosqueService.haversineDistanceMeters(originLat, originLon, lat, lon)
            } else {
                0.0
            }
            list.add(
                MosqueItem(
                    id = o.optString("id", "$osmType/$osmId"),
                    osmType = osmType,
                    osmId = osmId,
                    name = o.optString("name", "Unnamed Mosque (OpenStreetMap)"),
                    latitude = lat,
                    longitude = lon,
                    distanceMeters = dist,
                    category = cat,
                    address = o.optString("address").takeIf { it.isNotBlank() },
                    phone = o.optString("phone").takeIf { it.isNotBlank() },
                    website = o.optString("website").takeIf { it.isNotBlank() },
                    openingHours = o.optString("openingHours").takeIf { it.isNotBlank() },
                    wheelchair = o.optString("wheelchair").takeIf { it.isNotBlank() },
                    lastUpdatedMs = o.optLong("lastUpdatedMs", fallbackTs),
                    verificationLevel = ver,
                    denomination = o.optString("denomination").takeIf { it.isNotBlank() },
                    osmTagsSummary = o.optString("osmTagsSummary", "")
                )
            )
        }
        return OverpassMosqueService.deduplicateMosques(list)
    }
}
