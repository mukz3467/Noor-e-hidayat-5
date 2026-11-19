package com.example.data.mosque

import com.example.data.location.AppLocationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.util.Locale
import java.util.concurrent.TimeUnit

object NominatimGeocoderService {

    const val MIN_INTERVAL_MS = 1100L
    const val BASE_URL = "https://nominatim.openstreetmap.org/search"
    const val PHOTON_BASE_URL = "https://photon.komoot.io/api/"

    internal var httpClientForTest: OkHttpClient? = null
    internal var endpointForTest: String? = null
    internal var clockMsForTest: (() -> Long)? = null
    internal var networkRequestCountForTest: Int = 0
        private set

    private val throttleMutex = Mutex()
    private var lastRequestTimestampMs: Long = 0L

    private val sharedClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(4, TimeUnit.SECONDS)
            .callTimeout(4500, TimeUnit.MILLISECONDS)
            .build()
    }

    internal fun resetForTest() {
        httpClientForTest = null
        endpointForTest = null
        clockMsForTest = null
        lastRequestTimestampMs = 0L
        networkRequestCountForTest = 0
    }

    /**
     * Parses direct "lat, lon" input if user enters explicit coordinates cleanly without hitting Nominatim.
     */
    fun parseCoordinateInput(raw: String): GeocodedArea? {
        val parts = raw.trim().split(",", limit = 2).map { it.trim() }
        if (parts.size != 2) return null
        val lat = parts[0].toDoubleOrNull() ?: return null
        val lon = parts[1].toDoubleOrNull() ?: return null
        if (lat !in -90.0..90.0 || lon !in -180.0..180.0) return null
        val label = String.format(Locale.US, "%.4f, %.4f", lat, lon)
        return GeocodedArea(
            displayName = "Coordinates: $label",
            shortName = label,
            latitude = lat,
            longitude = lon,
            isOfflinePreset = false
        )
    }

    /**
     * Submit-only geocoder (never called on keystrokes).
     * Strictly enforces >= 1100 ms between live Nominatim HTTP calls, supports fast Photon OSM geocoding,
     * and provides instant offline preset matching.
     */
    suspend fun searchSubmittedArea(
        rawQuery: String,
        limit: Int = 5,
        biasLat: Double? = null,
        biasLon: Double? = null
    ): Result<List<GeocodedArea>> = withContext(Dispatchers.IO) {
        val q = rawQuery.trim()
        if (q.length < 2) {
            return@withContext Result.success(emptyList())
        }

        parseCoordinateInput(q)?.let { coordArea ->
            return@withContext Result.success(listOf(coordArea))
        }

        val presetMatches = AppLocationManager.searchCities(q).take(limit).map { preset ->
            GeocodedArea(
                displayName = "${preset.nameEn}, ${preset.country}",
                shortName = preset.nameEn,
                latitude = preset.latitude,
                longitude = preset.longitude,
                isOfflinePreset = true
            )
        }

        val exactPreset = presetMatches.firstOrNull {
            it.shortName.equals(q, ignoreCase = true)
        }
        if (exactPreset != null && endpointForTest == null) {
            return@withContext Result.success(listOf(exactPreset))
        }

        throttleMutex.withLock {
            val clock = clockMsForTest ?: { System.currentTimeMillis() }
            val now = clock()
            val elapsed = now - lastRequestTimestampMs
            if (lastRequestTimestampMs > 0L && elapsed in 0 until MIN_INTERVAL_MS) {
                return@withContext if (presetMatches.isNotEmpty()) {
                    Result.success(presetMatches)
                } else {
                    Result.failure(
                        IllegalStateException(
                            "Please wait at least 1 second between area lookups."
                        )
                    )
                }
            }
            lastRequestTimestampMs = now
        }

        val encoded = URLEncoder.encode(q, "UTF-8")
        val client = httpClientForTest ?: sharedClient

        // Try fast Komoot Photon OSM geocoder first in production (~250ms response), capped at 2.4s
        if (endpointForTest == null) {
            val biasQuery = if (biasLat != null && biasLon != null && (biasLat != 0.0 || biasLon != 0.0)) {
                String.format(Locale.US, "&lat=%.4f&lon=%.4f", biasLat, biasLon)
            } else ""
            val photonUrl = "$PHOTON_BASE_URL?q=$encoded&limit=${limit.coerceIn(1, 8)}$biasQuery"
            val photonResults = kotlinx.coroutines.withTimeoutOrNull(2400L) {
                runCatching {
                    val req = Request.Builder()
                        .url(photonUrl)
                        .header("User-Agent", OverpassMosqueService.USER_AGENT)
                        .header("Accept", "application/json")
                        .get()
                        .build()
                    client.newCall(req).execute().use { resp ->
                        if (!resp.isSuccessful) return@use emptyList()
                        parsePhotonJson(resp.body?.string().orEmpty(), q)
                    }
                }.getOrDefault(emptyList())
            }.orEmpty()
            if (photonResults.isNotEmpty()) {
                return@withContext Result.success(photonResults)
            }
        }

        val base = endpointForTest ?: BASE_URL
        val url = "$base?format=jsonv2&addressdetails=1&limit=${limit.coerceIn(1, 8)}&q=$encoded"

        return@withContext try {
            networkRequestCountForTest++
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", OverpassMosqueService.USER_AGENT)
                .header("Accept", "application/json")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.code == 429) {
                    return@use if (presetMatches.isNotEmpty()) {
                        Result.success(presetMatches)
                    } else {
                        Result.failure(IllegalStateException("Area search rate-limited (HTTP 429). Please wait a moment."))
                    }
                }
                if (!response.isSuccessful) {
                    return@use if (presetMatches.isNotEmpty()) {
                        Result.success(presetMatches)
                    } else {
                        Result.failure(IllegalStateException("Geocoder HTTP ${response.code}"))
                    }
                }
                val bodyStr = response.body?.string().orEmpty()
                val arr = JSONArray(bodyStr)
                val results = mutableListOf<GeocodedArea>()
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val lat = obj.optString("lat").toDoubleOrNull() ?: continue
                    val lon = obj.optString("lon").toDoubleOrNull() ?: continue
                    val display = obj.optString("display_name", "").ifBlank { q }
                    val short = obj.optString("name", "").ifBlank {
                        display.substringBefore(",").trim().ifBlank { q }
                    }
                    results.add(
                        GeocodedArea(
                            displayName = display,
                            shortName = short,
                            latitude = lat,
                            longitude = lon,
                            isOfflinePreset = false
                        )
                    )
                }
                if (results.isEmpty() && presetMatches.isNotEmpty()) {
                    Result.success(presetMatches)
                } else {
                    Result.success(results)
                }
            }
        } catch (t: Throwable) {
            if (presetMatches.isNotEmpty()) {
                Result.success(presetMatches)
            } else {
                Result.failure(t)
            }
        }
    }

    internal fun parsePhotonJson(rawJson: String, fallbackQuery: String): List<GeocodedArea> {
        val trimmed = rawJson.trim()
        if (!trimmed.startsWith("{")) return emptyList()
        val root = JSONObject(trimmed)
        val features = root.optJSONArray("features") ?: return emptyList()
        val out = mutableListOf<GeocodedArea>()
        for (i in 0 until features.length()) {
            val feat = features.optJSONObject(i) ?: continue
            val geom = feat.optJSONObject("geometry") ?: continue
            val coords = geom.optJSONArray("coordinates") ?: continue
            if (coords.length() < 2) continue
            val lon = coords.optDouble(0, Double.NaN)
            val lat = coords.optDouble(1, Double.NaN)
            if (lat.isNaN() || lon.isNaN() || lat !in -90.0..90.0 || lon !in -180.0..180.0) continue
            val props = feat.optJSONObject("properties") ?: JSONObject()
            val name = props.optString("name", "").trim()
            val city = props.optString("city", "").trim()
            val state = props.optString("state", "").trim()
            val country = props.optString("country", "").trim()
            val shortName = name.ifBlank { city.ifBlank { fallbackQuery } }
            val display = listOf(name, city, state, country)
                .filter { it.isNotBlank() }
                .distinct()
                .joinToString(", ")
                .ifBlank { shortName }
            out.add(
                GeocodedArea(
                    displayName = display,
                    shortName = shortName,
                    latitude = lat,
                    longitude = lon,
                    isOfflinePreset = false
                )
            )
        }
        return out
    }
}
