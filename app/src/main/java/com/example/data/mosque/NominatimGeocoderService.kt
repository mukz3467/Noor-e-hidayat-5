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

    private data class CountryHubConfig(
        val canonicalCountry: String,
        val primaryCityName: String,
        val fallbackCities: List<GeocodedArea> = emptyList()
    )

    private val COUNTRY_HUB_CONFIGS: List<Pair<List<String>, CountryHubConfig>> = listOf(
        listOf("pakistan", "pk", "pak", "پاکستان", "باكستان", "पाकिस्तान") to
            CountryHubConfig("Pakistan", "Islamabad"),
        listOf("saudi arabia", "saudi", "ksa", "sa", "سعودی عرب", "السعودية", "सऊदी अरब") to
            CountryHubConfig("Saudi Arabia", "Makkah"),
        listOf("india", "bharat", "hindustan", "in", "بھارت", "انڈیا", "الهند", "भारत") to
            CountryHubConfig("India", "Delhi"),
        listOf("turkey", "turkiye", "türkiye", "tr", "ترکی", "تركيا", "तुर्की") to
            CountryHubConfig(
                "Turkey",
                "Istanbul",
                listOf(
                    GeocodedArea("Ankara, Turkey", "Ankara", 39.9334, 32.8597, true),
                    GeocodedArea("Izmir, Turkey", "Izmir", 38.4237, 27.1428, true),
                    GeocodedArea("Bursa, Turkey", "Bursa", 40.1885, 29.0610, true)
                )
            ),
        listOf("egypt", "eg", "مصر", "मिस्र") to
            CountryHubConfig("Egypt", "Cairo"),
        listOf("uae", "united arab emirates", "emirates", "امارات", "الإمارات", "यूएई") to
            CountryHubConfig("UAE", "Dubai"),
        listOf("united kingdom", "uk", "britain", "great britain", "england", "برطانیہ", "المملكة المتحدة") to
            CountryHubConfig("United Kingdom", "London"),
        listOf("united states", "usa", "us", "america", "united states of america", "امریکہ", "أمريكا") to
            CountryHubConfig("United States", "New York"),
        listOf("bangladesh", "bd", "بنگلہ دیش", "بنغلاديش", "बांग्लादेश") to
            CountryHubConfig("Bangladesh", "Dhaka"),
        listOf("indonesia", "id", "انڈونیشیا", "إندونيسيا", "इंडोनेशिया") to
            CountryHubConfig("Indonesia", "Jakarta"),
        listOf("malaysia", "my", "ملائیشیا", "ماليزيا", "मलेशिया") to
            CountryHubConfig("Malaysia", "Kuala Lumpur"),
        listOf("qatar", "qa", "قطر") to
            CountryHubConfig("Qatar", "Doha"),
        listOf("kuwait", "kw", "کویت", "الكويت") to
            CountryHubConfig("Kuwait", "Kuwait City"),
        listOf("oman", "om", "عمان") to
            CountryHubConfig("Oman", "Muscat"),
        listOf("bahrain", "bh", "بحرین", "البحرين") to
            CountryHubConfig("Bahrain", "Manama"),
        listOf("jordan", "jo", "اردن", "الأردن") to
            CountryHubConfig("Jordan", "Amman"),
        listOf("iraq", "iq", "عراق", "العراق") to
            CountryHubConfig("Iraq", "Baghdad"),
        listOf("palestine", "ps", "فلسطین", "فلسطين") to
            CountryHubConfig("Palestine", "Jerusalem"),
        listOf("lebanon", "lb", "لبنان") to
            CountryHubConfig("Lebanon", "Beirut"),
        listOf("morocco", "ma", "مراکش", "المغرب") to
            CountryHubConfig("Morocco", "Casablanca"),
        listOf("algeria", "dz", "الجزائر") to
            CountryHubConfig("Algeria", "Algiers"),
        listOf("tunisia", "tn", "تیونس", "تونس") to
            CountryHubConfig("Tunisia", "Tunis"),
        listOf("sudan", "sd", "سوڈان", "السودان") to
            CountryHubConfig("Sudan", "Khartoum"),
        listOf("afghanistan", "af", "افغانستان", "أفغانستان") to
            CountryHubConfig("Afghanistan", "Kabul"),
        listOf("iran", "ir", "ایران", "إيران") to
            CountryHubConfig(
                "Iran",
                "Tehran",
                listOf(
                    GeocodedArea("Tehran, Iran", "Tehran", 35.6892, 51.3890, true),
                    GeocodedArea("Mashhad, Iran", "Mashhad", 36.2605, 59.6168, true),
                    GeocodedArea("Isfahan, Iran", "Isfahan", 32.6546, 51.6680, true)
                )
            ),
        listOf("uzbekistan", "uz", "ازبکستان", "أوزبكستان") to
            CountryHubConfig("Uzbekistan", "Tashkent"),
        listOf("azerbaijan", "az", "آذربائیجان", "أذربيجان") to
            CountryHubConfig("Azerbaijan", "Baku"),
        listOf("kazakhstan", "kz", "قازقستان", "كازاخستان") to
            CountryHubConfig("Kazakhstan", "Almaty"),
        listOf("france", "fr", "فرانس", "فرنسا") to
            CountryHubConfig("France", "Paris"),
        listOf("germany", "de", "جرمنی", "ألمانيا") to
            CountryHubConfig("Germany", "Berlin"),
        listOf("netherlands", "holland", "nl", "ہالینڈ", "هولندا") to
            CountryHubConfig("Netherlands", "Amsterdam"),
        listOf("belgium", "be", "بیلجیم", "بلجيكا") to
            CountryHubConfig("Belgium", "Brussels"),
        listOf("italy", "it", "اٹلی", "إيطاليا") to
            CountryHubConfig("Italy", "Rome"),
        listOf("spain", "es", "سپین", "إسبانيا") to
            CountryHubConfig("Spain", "Madrid"),
        listOf("austria", "at", "آسٹریا", "النمسا") to
            CountryHubConfig("Austria", "Vienna"),
        listOf("sweden", "se", "سویڈن", "السويد") to
            CountryHubConfig("Sweden", "Stockholm"),
        listOf("ireland", "ie", "آئرلینڈ", "أيرلندا") to
            CountryHubConfig("Ireland", "Dublin"),
        listOf("russia", "ru", "روس", "روسيا") to
            CountryHubConfig("Russia", "Moscow"),
        listOf("canada", "ca", "کینیڈا", "كندا") to
            CountryHubConfig("Canada", "Toronto"),
        listOf("australia", "au", "آسٹریلیا", "أستراليا") to
            CountryHubConfig("Australia", "Sydney"),
        listOf("new zealand", "nz", "نیوزی لینڈ", "نيوزيلندا") to
            CountryHubConfig("New Zealand", "Auckland"),
        listOf("singapore", "sg", "سنگاپور", "سنغافورة") to
            CountryHubConfig("Singapore", "Singapore"),
        listOf("thailand", "th", "تھائی لینڈ", "تايلاند") to
            CountryHubConfig("Thailand", "Bangkok"),
        listOf("japan", "jp", "جاپان", "اليابان") to
            CountryHubConfig("Japan", "Tokyo"),
        listOf("south korea", "korea", "kr", "جنوبی کوریا", "كوريا الجنوبية") to
            CountryHubConfig("South Korea", "Seoul"),
        listOf("china", "cn", "چین", "الصين") to
            CountryHubConfig("China", "Beijing"),
        listOf("nigeria", "ng", "نائیجیریا", "نيجيريا") to
            CountryHubConfig("Nigeria", "Lagos"),
        listOf("south africa", "za", "جنوبی افریقہ", "جنوب أفريقيا") to
            CountryHubConfig("South Africa", "Johannesburg"),
        listOf("kenya", "ke", "کینیا", "كينيا") to
            CountryHubConfig("Kenya", "Nairobi"),
        listOf("yemen", "ye", "یمن", "اليمن") to
            CountryHubConfig(
                "Yemen",
                "Sana'a",
                listOf(
                    GeocodedArea("Sana'a, Yemen", "Sana'a", 15.3694, 44.1910, true),
                    GeocodedArea("Aden, Yemen", "Aden", 12.7855, 45.0187, true)
                )
            ),
        listOf("syria", "sy", "شام", "سوريا") to
            CountryHubConfig(
                "Syria",
                "Damascus",
                listOf(
                    GeocodedArea("Damascus, Syria", "Damascus", 33.5138, 36.2765, true),
                    GeocodedArea("Aleppo, Syria", "Aleppo", 36.2021, 37.1343, true)
                )
            ),
        listOf("libya", "ly", "لیبیا", "ليبيا") to
            CountryHubConfig(
                "Libya",
                "Tripoli",
                listOf(
                    GeocodedArea("Tripoli, Libya", "Tripoli", 32.8872, 13.1913, true),
                    GeocodedArea("Benghazi, Libya", "Benghazi", 32.1194, 20.0868, true)
                )
            ),
        listOf("somalia", "so", "صومالیہ", "الصومال") to
            CountryHubConfig(
                "Somalia",
                "Mogadishu",
                listOf(GeocodedArea("Mogadishu, Somalia", "Mogadishu", 2.0469, 45.3182, true))
            ),
        listOf("bosnia", "bosnia and herzegovina", "بوسنیا") to
            CountryHubConfig(
                "Bosnia and Herzegovina",
                "Sarajevo",
                listOf(GeocodedArea("Sarajevo, Bosnia and Herzegovina", "Sarajevo", 43.8563, 18.4131, true))
            ),
        listOf("sri lanka", "lk", "سری لنکا") to
            CountryHubConfig(
                "Sri Lanka",
                "Colombo",
                listOf(GeocodedArea("Colombo, Sri Lanka", "Colombo", 6.9271, 79.8612, true))
            ),
        listOf("maldives", "mv", "مالدیپ") to
            CountryHubConfig(
                "Maldives",
                "Malé",
                listOf(GeocodedArea("Malé, Maldives", "Malé", 4.1755, 73.5093, true))
            ),
        listOf("brunei", "bn", "برونائی") to
            CountryHubConfig(
                "Brunei",
                "Bandar Seri Begawan",
                listOf(GeocodedArea("Bandar Seri Begawan, Brunei", "Bandar Seri Begawan", 4.9031, 114.9398, true))
            )
    )

    private val COUNTRY_ALIAS_LOOKUP: Map<String, CountryHubConfig> by lazy {
        buildMap {
            COUNTRY_HUB_CONFIGS.forEach { (aliases, config) ->
                aliases.forEach { alias ->
                    put(alias.lowercase(Locale.ROOT), config)
                }
            }
        }
    }

    /**
     * When a user searches a country name (e.g. "Pakistan", "پاکستان", "Saudi Arabia", "India"),
     * resolve directly to that country's capital / primary mosque hub followed by its major cities
     * instead of dropping the user into an empty geographic midpoint farm/desert centroid.
     */
    internal fun resolveCountryHubCities(rawQuery: String, limit: Int = 6): List<GeocodedArea> {
        val normalized = rawQuery.trim().lowercase(Locale.ROOT).replace(Regex("\\s+"), " ")
        val config = COUNTRY_ALIAS_LOOKUP[normalized] ?: return emptyList()

        val presetCities = AppLocationManager.PRESET_CITIES
            .filter { it.country.equals(config.canonicalCountry, ignoreCase = true) }
            .map { preset ->
                GeocodedArea(
                    displayName = "${preset.nameEn}, ${preset.country}",
                    shortName = preset.nameEn,
                    latitude = preset.latitude,
                    longitude = preset.longitude,
                    isOfflinePreset = true
                )
            }

        val combined = (presetCities + config.fallbackCities)
            .distinctBy { it.shortName.lowercase(Locale.ROOT) }

        if (combined.isEmpty()) return emptyList()

        val primary = combined.firstOrNull { it.shortName.equals(config.primaryCityName, ignoreCase = true) }
        val ordered = if (primary != null) {
            listOf(primary) + combined.filterNot { it.shortName.equals(primary.shortName, ignoreCase = true) }
        } else {
            combined
        }
        return ordered.take(limit.coerceIn(1, 8))
    }

    /**
     * Submit-only geocoder (never called on keystrokes).
     * Strictly enforces >= 1100 ms between live Nominatim HTTP calls, resolves country searches
     * directly to real capitals & major cities (never empty geographic centroids), supports fast
     * Photon OSM geocoding, and provides instant offline preset matching.
     */
    suspend fun searchSubmittedArea(
        rawQuery: String,
        limit: Int = 6,
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

        // Country searches must land in real capitals & major cities (with mosques + 3D buildings),
        // never in an empty rural/desert geographic centroid.
        if (endpointForTest == null) {
            val countryHubs = resolveCountryHubCities(q, limit)
            if (countryHubs.isNotEmpty()) {
                return@withContext Result.success(countryHubs)
            }
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
            val photonParsed = kotlinx.coroutines.withTimeoutOrNull(2400L) {
                runCatching {
                    val req = Request.Builder()
                        .url(photonUrl)
                        .header("User-Agent", OverpassMosqueService.USER_AGENT)
                        .header("Accept", "application/json")
                        .get()
                        .build()
                    client.newCall(req).execute().use { resp ->
                        if (!resp.isSuccessful) return@use null
                        parsePhotonJsonWithMeta(resp.body?.string().orEmpty(), q)
                    }
                }.getOrNull()
            }
            if (photonParsed != null && photonParsed.areas.isNotEmpty()) {
                // If Photon matched an unlisted country polygon centroid, automatically fetch real cities in that country
                if (photonParsed.topHitIsCountry) {
                    val countryName = photonParsed.topHitCountryName.ifBlank { q }
                    val cityHubs = fetchMajorCitiesForCountry(client, countryName, limit)
                    if (cityHubs.isNotEmpty()) {
                        return@withContext Result.success(cityHubs)
                    }
                }
                return@withContext Result.success(photonParsed.areas)
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
                var topIsCountry = false
                var topCountryName = q
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val lat = obj.optString("lat").toDoubleOrNull() ?: continue
                    val lon = obj.optString("lon").toDoubleOrNull() ?: continue
                    val display = obj.optString("display_name", "").ifBlank { q }
                    val short = obj.optString("name", "").ifBlank {
                        display.substringBefore(",").trim().ifBlank { q }
                    }
                    if (results.isEmpty()) {
                        val addrType = obj.optString("addresstype", "").lowercase(Locale.ROOT)
                        val placeType = obj.optString("type", "").lowercase(Locale.ROOT)
                        if (addrType == "country" || placeType == "country") {
                            topIsCountry = true
                            topCountryName = short
                        }
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
                if (endpointForTest == null && topIsCountry) {
                    val cityHubs = fetchMajorCitiesForCountry(client, topCountryName, limit)
                    if (cityHubs.isNotEmpty()) {
                        return@use Result.success(cityHubs)
                    }
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

    private suspend fun fetchMajorCitiesForCountry(
        client: OkHttpClient,
        countryName: String,
        limit: Int
    ): List<GeocodedArea> {
        val localHubs = resolveCountryHubCities(countryName, limit)
        if (localHubs.isNotEmpty()) return localHubs

        val encoded = URLEncoder.encode(countryName, "UTF-8")
        val photonCitiesUrl = "$PHOTON_BASE_URL?q=$encoded&osm_tag=place:city&limit=${limit.coerceIn(1, 6)}"
        return kotlinx.coroutines.withTimeoutOrNull(2200L) {
            runCatching {
                val req = Request.Builder()
                    .url(photonCitiesUrl)
                    .header("User-Agent", OverpassMosqueService.USER_AGENT)
                    .header("Accept", "application/json")
                    .get()
                    .build()
                client.newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) return@use emptyList()
                    parsePhotonJson(resp.body?.string().orEmpty(), countryName)
                        .distinctBy { it.shortName.lowercase(Locale.ROOT) }
                }
            }.getOrDefault(emptyList())
        }.orEmpty()
    }

    private data class PhotonParseResult(
        val areas: List<GeocodedArea>,
        val topHitIsCountry: Boolean,
        val topHitCountryName: String
    )

    internal fun parsePhotonJson(rawJson: String, fallbackQuery: String): List<GeocodedArea> =
        parsePhotonJsonWithMeta(rawJson, fallbackQuery).areas

    private fun parsePhotonJsonWithMeta(rawJson: String, fallbackQuery: String): PhotonParseResult {
        val trimmed = rawJson.trim()
        if (!trimmed.startsWith("{")) return PhotonParseResult(emptyList(), false, "")
        val root = JSONObject(trimmed)
        val features = root.optJSONArray("features") ?: return PhotonParseResult(emptyList(), false, "")
        val out = mutableListOf<GeocodedArea>()
        var topIsCountry = false
        var topCountryName = ""
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
            val osmValue = props.optString("osm_value", "").lowercase(Locale.ROOT)
            val featureType = props.optString("type", "").lowercase(Locale.ROOT)
            val shortName = name.ifBlank { city.ifBlank { fallbackQuery } }
            if (out.isEmpty() && (osmValue == "country" || featureType == "country")) {
                topIsCountry = true
                topCountryName = country.ifBlank { name }
            }
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
        return PhotonParseResult(out, topIsCountry, topCountryName)
    }
}
