package com.example

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ApplicationProvider
import com.example.data.location.AppLocationManager
import com.example.data.location.LocationSource
import com.example.data.location.UserLocation
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.mosque.*
import com.example.data.repository.UserPreferencesRepository
import com.example.data.seerah.*
import com.example.localization.AppLocalization
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.mosque.MapLibreRuntimeHelper
import com.example.ui.screens.mosque.MosqueFinderScreen
import com.example.ui.screens.seerah.SeerahScreen
import com.example.ui.theme.NooreHidayatTheme
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import java.io.File
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MosqueFinderAndSeerahTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        appContext.getSharedPreferences("mosque_finder_prefs", Context.MODE_PRIVATE)
            .edit().clear().commit()
        appContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
            .edit().clear().commit()
        AppLocationManager.resetForTest()
        MosqueFinderRepository.resetForTest()
        OverpassMosqueService.httpClientForTest = null
        OverpassMosqueService.endpointsForTest = null
        OverpassMosqueService.backoffBaseMsForTest = 0L
        NominatimGeocoderService.resetForTest()
        OsrmRoutingService.resetForTest()
        SeerahRepository.resetForTest(appContext)
        File(appContext.filesDir, "mosque_cache").deleteRecursively()
    }

    @After
    fun tearDown() {
        AppLocationManager.resetForTest()
        OverpassMosqueService.httpClientForTest = null
        OverpassMosqueService.endpointsForTest = null
        OverpassMosqueService.backoffBaseMsForTest = 650L
        NominatimGeocoderService.resetForTest()
        OsrmRoutingService.resetForTest()
        MosqueFinderRepository.resetForTest()
        SeerahRepository.resetForTest(appContext)
        File(appContext.filesDir, "mosque_cache").deleteRecursively()
    }

    @Test
    fun phase2Navigation_onlyInMoreHub_andAbsentFromHomeScreen() {
        var showMore by mutableStateOf(false)
        var navigatedDestination = ""
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                if (!showMore) {
                    HomeScreen(
                        language = AppLanguage.ENGLISH,
                        onNavigateToQuran = {},
                        onNavigateToLive = {},
                        onNavigateToLectures = {},
                        onNavigateToLearning = {},
                        onNavigateToMoreHub = { showMore = true },
                        onNavigateToPrayerTimes = {},
                        onNavigateToSettings = {},
                        onLanguageChanged = {}
                    )
                } else {
                    MoreHubScreen(
                        language = AppLanguage.ENGLISH,
                        onBack = {},
                        onNavigate = { navigatedDestination = it }
                    )
                }
            }
        }

        // Must NEVER appear on HomeScreen
        composeRule.onAllNodesWithTag("hub_item_mosque_finder").assertCountEquals(0)
        composeRule.onAllNodesWithTag("hub_item_seerah").assertCountEquals(0)
        composeRule.onAllNodesWithText("Mosque Finder").assertCountEquals(0)
        composeRule.onAllNodesWithText("Seerah of Prophet Muhammad ﷺ").assertCountEquals(0)

        // Open MoreHub
        composeRule.runOnIdle { showMore = true }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag("hub_item_mosque_finder"))
        composeRule.onNodeWithTag("hub_item_mosque_finder").assertIsDisplayed().performClick()
        assertEquals("mosque_finder", navigatedDestination)

        composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag("hub_item_seerah"))
        composeRule.onNodeWithTag("hub_item_seerah").assertIsDisplayed().performClick()
        assertEquals("seerah", navigatedDestination)
    }

    @Test
    fun manifestPermissions_neverIncludesBackgroundLocation() {
        val pkgInfo = appContext.packageManager.getPackageInfo(
            appContext.packageName,
            PackageManager.GET_PERMISSIONS
        )
        val perms = pkgInfo.requestedPermissions?.toList().orEmpty()
        assertTrue(perms.contains(Manifest.permission.ACCESS_FINE_LOCATION))
        assertTrue(perms.contains(Manifest.permission.ACCESS_COARSE_LOCATION))
        assertFalse(
            "ACCESS_BACKGROUND_LOCATION must never be requested",
            perms.contains("android.permission.ACCESS_BACKGROUND_LOCATION")
        )
    }

    @Test
    fun mosqueFinder_neverUsesTimezoneGuessAsRealLocation_andPromptsHonestlyWhenUnset() {
        var networkCalls = 0
        OverpassMosqueService.endpointsForTest = listOf("https://overpass.mock/api/interpreter")
        OverpassMosqueService.httpClientForTest = OkHttpClient.Builder()
            .addInterceptor { chain ->
                networkCalls++
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body("""{"elements":[]}""".toResponseBody("application/json".toMediaType()))
                    .build()
            }
            .build()

        // Default timezone guess must NEVER qualify as confirmed coordinates
        assertFalse(
            MosqueFinderRepository.hasConfirmedCoordinates(
                UserLocation(24.8607, 67.0011, "Karachi", "Pakistan", "Asia/Karachi", sourceType = LocationSource.DEFAULT_ESTIMATE)
            )
        )
        assertTrue(
            MosqueFinderRepository.hasConfirmedCoordinates(
                UserLocation(31.5204, 74.3587, "Lahore", "Pakistan", "Asia/Karachi", isGpsFix = true, sourceType = LocationSource.GPS)
            )
        )

        Shadows.shadowOf(appContext).denyPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                MosqueFinderScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // No fake auto-search was fired for Karachi/Islamabad/Makkah without permission/selection
        assertEquals(0, networkCalls)
        composeRule.onNodeWithTag("mosque_permission_banner").assertIsDisplayed()
        composeRule.onNodeWithTag("mosque_location_unset_card").assertIsDisplayed()
    }

    @Test
    fun permissionPrecisionModes_resolveFineCoarseAndDeniedCorrectly() {
        Shadows.shadowOf(appContext).denyPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        assertEquals(LocationPrecisionMode.PERMISSION_DENIED, MosqueFinderRepository.resolvePrecisionMode(appContext))

        Shadows.shadowOf(appContext).grantPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
        assertEquals(LocationPrecisionMode.APPROXIMATE, MosqueFinderRepository.resolvePrecisionMode(appContext))

        Shadows.shadowOf(appContext).grantPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        assertEquals(LocationPrecisionMode.GPS_PRECISE, MosqueFinderRepository.resolvePrecisionMode(appContext))
    }

    @Test
    fun overpassService_parsingCategoriesVerificationLevelsDeduplicationAndBboxQuery() {
        runBlocking {
            val capturedAgents = mutableListOf<String>()
            val capturedUrls = mutableListOf<String>()
            val sampleJson = """
                {
                  "elements": [
                    {
                      "type": "node",
                      "id": 1001,
                      "lat": 33.7294,
                      "lon": 73.0372,
                      "tags": {
                        "name": "Faisal Mosque",
                        "amenity": "place_of_worship",
                        "religion": "muslim",
                        "mosque": "jami",
                        "denomination": "sunni",
                        "addr:city": "Islamabad"
                      }
                    },
                    {
                      "type": "way",
                      "id": 2002,
                      "center": { "lat": 33.6850, "lon": 73.0480 },
                      "tags": {
                        "name": "Sector G-9 Musalla",
                        "amenity": "prayer_room",
                        "religion": "muslim"
                      }
                    },
                    {
                      "type": "relation",
                      "id": 3003,
                      "bounds": { "minlat": 33.6890, "minlon": 73.0490, "maxlat": 33.6910, "maxlon": 73.0510 },
                      "tags": {
                        "name": "Central Eidgah Ground",
                        "amenity": "place_of_worship",
                        "religion": "muslim"
                      }
                    },
                    {
                      "type": "node",
                      "id": 4004,
                      "lat": 33.6845,
                      "lon": 73.0475,
                      "tags": {
                        "building": "mosque"
                      }
                    },
                    {
                      "type": "node",
                      "id": 4004,
                      "lat": 33.6845,
                      "lon": 73.0475,
                      "tags": {
                        "amenity": "place_of_worship",
                        "religion": "muslim",
                        "building": "mosque"
                      }
                    }
                  ]
                }
            """.trimIndent()

            var callIndex = 0
            OverpassMosqueService.endpointsForTest = listOf(
                "https://mirror1.invalid/api/interpreter",
                "https://mirror2.valid/api/interpreter"
            )
            OverpassMosqueService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val req = chain.request()
                    capturedAgents.add(req.header("User-Agent").orEmpty())
                    capturedUrls.add(req.url.toString())
                    callIndex++
                    // Fail mirror1 on initial + retry with 503, then succeed on mirror2
                    if (req.url.host == "mirror1.invalid") {
                        return@addInterceptor Response.Builder()
                            .request(req)
                            .protocol(Protocol.HTTP_1_1)
                            .code(503)
                            .message("Service Unavailable")
                            .body("busy".toResponseBody("text/plain".toMediaType()))
                            .build()
                    }
                    Response.Builder()
                        .request(req)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body(sampleJson.toResponseBody("application/json".toMediaType()))
                        .build()
                }
                .build()

            val result = OverpassMosqueService.fetchNearbyMosquesDetailed(33.6844, 73.0479, 5000)
            assertTrue(result.isSuccess)
            val payload = result.getOrThrow()
            assertEquals("https://mirror2.valid/api/interpreter", payload.endpointUsed)
            val items = payload.items
            assertEquals(4, items.size) // Deduplicated 4004!
            assertTrue(capturedAgents.all { it == OverpassMosqueService.USER_AGENT })

            // Sorted nearest-first by Haversine
            assertTrue(items.zipWithNext { a, b -> a.distanceMeters <= b.distanceMeters }.all { it })

            val faisal = items.first { it.osmId == 1001L }
            assertEquals(MosqueCategory.JAMI_MOSQUE, faisal.category)
            assertEquals(OsmVerificationLevel.VERIFIED_MUSLIM_PLACE_OF_WORSHIP, faisal.verificationLevel)
            assertEquals("sunni", faisal.denomination)
            assertNull("Unprovided tags must stay null", faisal.phone)
            assertNull(faisal.openingHours)
            assertEquals("https://www.openstreetmap.org/node/1001", faisal.osmUrl)

            val musalla = items.first { it.osmId == 2002L }
            assertEquals(MosqueCategory.PRAYER_ROOM, musalla.category)
            assertEquals(OsmVerificationLevel.PRAYER_ROOM_MUSALLA, musalla.verificationLevel)

            // Relation center extracted from bounds average
            val eidgah = items.first { it.osmId == 3003L }
            assertEquals(MosqueCategory.EID_GROUND, eidgah.category)
            assertEquals(33.6900, eidgah.latitude, 0.0001)
            assertEquals(73.0500, eidgah.longitude, 0.0001)

            // Duplicate node/4004 preferred VERIFIED_MUSLIM_PLACE_OF_WORSHIP over candidate-only entry
            val deduped = items.first { it.osmId == 4004L }
            assertEquals(OsmVerificationLevel.VERIFIED_MUSLIM_PLACE_OF_WORSHIP, deduped.verificationLevel)
            assertEquals("Unnamed Mosque (OpenStreetMap)", deduped.name)

            // Verify bbox Overpass query structure and building=mosque candidate classification
            val bboxQuery = OverpassMosqueService.buildOverpassBboxQuery(MapBoundingBox(33.60, 73.00, 33.70, 73.10))
            assertTrue(bboxQuery.contains("(33.600000,73.000000,33.700000,73.100000)"))
            assertTrue(bboxQuery.contains("""nwr["amenity"="place_of_worship"]["religion"="muslim"]"""))
            assertTrue(bboxQuery.contains("""nwr["building"="mosque"]"""))
            assertTrue(bboxQuery.contains("out center tags;"))

            val candidateOnly = OverpassMosqueService.parseOverpassJson(
                """{"elements":[{"type":"way","id":777,"center":{"lat":33.65,"lon":73.05},"tags":{"building":"mosque"}}]}""",
                33.65,
                73.05
            ).single()
            assertEquals(OsmVerificationLevel.BUILDING_MOSQUE_CANDIDATE, candidateOnly.verificationLevel)
        }
    }

    @Test
    fun overpassService_handlesRateLimit429_malformedJson_andRequestDeduplication() {
        runBlocking {
            var httpCalls = 0
            OverpassMosqueService.endpointsForTest = listOf("https://overpass.mock/api/interpreter")
            OverpassMosqueService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    httpCalls++
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(429)
                        .header("Retry-After", "1")
                        .message("Too Many Requests")
                        .body("rate limit".toResponseBody("text/plain".toMediaType()))
                        .build()
                }
                .build()

            MosqueFinderRepository.searchNearbyMosques(
                context = appContext,
                lat = 21.4225,
                lon = 39.8262,
                radiusMeters = 3000,
                label = "Makkah",
                precisionMode = LocationPrecisionMode.MANUAL_PRESET
            )
            val state429 = MosqueFinderRepository.state.value
            assertTrue(state429.isRateLimited)
            assertTrue(state429.errorMessage?.contains("429") == true)
            // Controlled retries (1 initial + 1 retry = 2 calls max, never infinite)
            assertEquals(2, httpCalls)

            // Malformed non-JSON error page
            var parseErr: Throwable? = null
            try {
                OverpassMosqueService.parseOverpassJson("<html>502 Bad Gateway</html>", 0.0, 0.0)
            } catch (t: Throwable) {
                parseErr = t
            }
            assertTrue(parseErr is OverpassParseException)

            // Verify duplicate in-window queries deduplicate cleanly
            var okCalls = 0
            OverpassMosqueService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    okCalls++
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body("""{"elements":[]}""".toResponseBody("application/json".toMediaType()))
                        .build()
                }
                .build()

            MosqueFinderRepository.searchNearbyMosques(
                context = appContext,
                lat = 51.5074,
                lon = -0.1278,
                radiusMeters = 5000,
                label = "London",
                precisionMode = LocationPrecisionMode.MANUAL_PRESET
            )
            MosqueFinderRepository.searchNearbyMosques(
                context = appContext,
                lat = 51.5074,
                lon = -0.1278,
                radiusMeters = 5000,
                label = "London",
                precisionMode = LocationPrecisionMode.MANUAL_PRESET
            )
            assertEquals(1, okCalls)
            assertTrue(MosqueFinderRepository.state.value.isFreshLiveResult)
            assertFalse(MosqueFinderRepository.state.value.isFromCache)
        }
    }

    @Test
    fun nominatimService_enforcesRateLimit_coordinateInput_andOfflinePresetFallback() {
        runBlocking {
            var fakeTime = 10_000L
            NominatimGeocoderService.clockMsForTest = { fakeTime }
            NominatimGeocoderService.endpointForTest = "https://nominatim.mock/search"
            NominatimGeocoderService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body("""[{"lat":"31.5204","lon":"74.3587","display_name":"Lahore, Punjab, Pakistan"}]""".toResponseBody("application/json".toMediaType()))
                        .build()
                }
                .build()

            // Direct coordinates parse locally without consuming a Nominatim request
            val directCoord = NominatimGeocoderService.searchSubmittedArea("33.7294, 73.0372")
            assertTrue(directCoord.isSuccess)
            assertEquals(33.7294, directCoord.getOrThrow().first().latitude, 0.0001)
            assertEquals(0, NominatimGeocoderService.networkRequestCountForTest)

            // First area query hits Nominatim
            val first = NominatimGeocoderService.searchSubmittedArea("Gulberg UnknownTown")
            assertTrue(first.isSuccess)
            assertEquals(1, NominatimGeocoderService.networkRequestCountForTest)

            // Rapid second call within <1100 ms must NOT make a network call
            fakeTime += 500L
            val throttled = NominatimGeocoderService.searchSubmittedArea("UnlistedVillageXY")
            assertTrue(throttled.isFailure)
            assertEquals(1, NominatimGeocoderService.networkRequestCountForTest)

            // Instant offline preset fallback matches Lahore even when throttled or offline
            val presetWhileThrottled = NominatimGeocoderService.searchSubmittedArea("Lahore")
            assertTrue(presetWhileThrottled.isSuccess)
            assertTrue(presetWhileThrottled.getOrThrow().first().isOfflinePreset)
            assertEquals(1, NominatimGeocoderService.networkRequestCountForTest)
        }
    }

    @Test
    fun mosqueFinderScreen_showsCacheBanner_mapPinDetailSheet_bboxQuery_andSmallScreenLayout() {
        runBlocking {
            val sampleJson = """
                {
                  "elements": [
                    {
                      "type": "node",
                      "id": 9101,
                      "lat": 33.6910,
                      "lon": 73.0490,
                      "tags": { "name": "Jamia Masjid Al-Falah", "amenity": "place_of_worship", "religion": "muslim" }
                    },
                    {
                      "type": "way",
                      "id": 9202,
                      "center": { "lat": 33.6860, "lon": 73.0460 },
                      "tags": { "building": "mosque", "name": "Unverified Mosque Hall" }
                    }
                  ]
                }
            """.trimIndent()
            var failNetwork = false
            var lastPostBody = ""
            OverpassMosqueService.endpointsForTest = listOf("https://overpass.mock/api/interpreter")
            OverpassMosqueService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val req = chain.request()
                    val buf = okio.Buffer()
                    req.body?.writeTo(buf)
                    lastPostBody = java.net.URLDecoder.decode(buf.readUtf8(), "UTF-8")
                    if (failNetwork) throw IOException("Offline")
                    Response.Builder()
                        .request(req)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body(sampleJson.toResponseBody("application/json".toMediaType()))
                        .build()
                }
                .build()

            // Populate disk cache first, then simulate offline load failure to trigger stale cache notice
            MosqueFinderRepository.searchNearbyMosques(
                context = appContext,
                lat = 33.6844,
                lon = 73.0479,
                radiusMeters = 5000,
                label = "Islamabad",
                precisionMode = LocationPrecisionMode.MANUAL_PRESET
            )
            failNetwork = true
            MosqueFinderRepository.searchNearbyMosques(
                context = appContext,
                lat = 33.6844,
                lon = 73.0479,
                radiusMeters = 5000,
                label = "Islamabad",
                precisionMode = LocationPrecisionMode.MANUAL_PRESET,
                forceRefresh = true
            )

            Shadows.shadowOf(appContext).denyPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            // Constrained small screen test (320dp x 520dp)
            composeRule.setContent {
                NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                    Box(modifier = Modifier.size(320.dp, 520.dp)) {
                        MosqueFinderScreen(language = AppLanguage.ENGLISH, onBack = {})
                    }
                }
            }
            composeRule.waitForIdle()

            // Denied permission banner & stale cache banner + Map-first controls present even on small 320x520 screen
            composeRule.onNodeWithTag("mosque_permission_banner").assertIsDisplayed()
            composeRule.onNodeWithTag("mosque_cache_banner").assertExists()
            composeRule.onNodeWithTag("mosque_osm_attribution").assertIsDisplayed()
            composeRule.onNodeWithTag("mosque_search_visible_area_btn").assertIsDisplayed()
            composeRule.onNodeWithTag("mosque_map_3d_toggle").assertIsDisplayed()
            composeRule.onNodeWithTag("mosque_map_style_toggle").assertIsDisplayed()

            // Verify 3D <-> 2D toggle updates label cleanly
            composeRule.onNodeWithTag("mosque_map_3d_toggle").assertTextEquals("3D")
            composeRule.onNodeWithTag("mosque_map_3d_toggle").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("mosque_map_3d_toggle").assertTextEquals("2D")
            composeRule.onNodeWithTag("mosque_map_3d_toggle").performClick()
            composeRule.waitForIdle()

            // Clicking visible-area button runs real Overpass bbox query when network returns
            failNetwork = false
            composeRule.onNodeWithTag("mosque_search_visible_area_btn").performClick()
            composeRule.waitUntil(3_000L) { lastPostBody.contains("out center tags;") }
            composeRule.waitForIdle()
            assertTrue(lastPostBody.contains("out center tags;"))

            // Switch to List View and verify card exists, then switch back to Map View
            composeRule.onNodeWithTag("mosque_view_toggle_list").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("mosque_list_container").performScrollToNode(hasTestTag("mosque_card_9101"))
            composeRule.onNodeWithTag("mosque_card_9101").assertIsDisplayed()

            composeRule.onNodeWithTag("mosque_view_toggle_map").performClick()
            composeRule.waitForIdle()

            // Tapping map pin opens exact matching MosqueDetailSheet with OSRM controls + OSM metadata
            composeRule.onNodeWithTag("mosque_pin_9101").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("mosque_detail_sheet").assertIsDisplayed()
            composeRule.onAllNodes(
                hasText("Jamia Masjid Al-Falah") or hasContentDescription("Jamia Masjid Al-Falah")
            ).assertCountEquals(2)
            composeRule.onAllNodesWithText(AppLocalization.get("mosque_not_provided_osm", AppLanguage.ENGLISH))
                .assertCountEquals(5)
            composeRule.onNodeWithTag("mosque_show_route_btn").assertExists()
            composeRule.onNodeWithTag("mosque_directions_btn").assertExists()
            composeRule.onNodeWithTag("mosque_osm_edit_btn").assertExists()

            // Verify visible bounding box math stays valid across hemisphere bounds
            val bbox = MapLibreRuntimeHelper.computeVisibleBoundingBox(33.6844, 73.0479, 14.0, 1080, 1200)
            assertTrue(bbox.isValid())
            assertTrue(bbox.north > 33.6844 && bbox.south < 33.6844)
        }
    }

    @Test
    fun osrmRoutingService_parsesProfiles_missingDuration_rateLimit_andFaisalabadPlusGlobalRoutes() {
        runBlocking {
            var fakeNow = 100_000L
            OsrmRoutingService.clockMsForTest = { fakeNow }
            OsrmRoutingService.endpointsForTest = mapOf(
                OSRMRouteProfile.DRIVING to listOf("https://osrm.mock/routed-car/route/v1/driving"),
                OSRMRouteProfile.WALKING to listOf("https://osrm.mock/routed-foot/route/v1/foot"),
                OSRMRouteProfile.CYCLING to listOf("https://osrm.mock/routed-bike/route/v1/bike")
            )

            // Route 1: Faisalabad -> local mosque (driving) with full steps
            val requestedUrls = mutableListOf<String>()
            OsrmRoutingService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val url = chain.request().url.toString()
                    requestedUrls.add(url)
                    val json = if (url.contains("/foot/")) {
                        // Omitting duration to verify we NEVER fabricate travel time
                        """
                        {
                          "code": "Ok",
                          "routes": [{
                            "distance": 840.5,
                            "geometry": { "type": "LineString", "coordinates": [[139.6503, 35.6762], [139.6550, 35.6800], [139.6601, 35.6820]] },
                            "legs": [{ "summary": "Meiji-dori", "steps": [] }]
                          }]
                        }
                        """.trimIndent()
                    } else {
                        """
                        {
                          "code": "Ok",
                          "routes": [{
                            "distance": 3450.2,
                            "duration": 420.0,
                            "geometry": { "type": "LineString", "coordinates": [[73.1350, 31.4504], [73.1420, 31.4550], [73.1510, 31.4610]] },
                            "legs": [{
                              "summary": "Sargodha Road",
                              "steps": [
                                { "name": "Sargodha Road", "distance": 1200.0, "duration": 150.0, "maneuver": { "type": "depart", "modifier": "north" } },
                                { "name": "", "distance": 0.0, "duration": 0.0, "maneuver": { "type": "arrive" } }
                              ]
                            }]
                          }]
                        }
                        """.trimIndent()
                    }
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body(json.toResponseBody("application/json".toMediaType()))
                        .build()
                }
                .build()

            val fsdRes = OsrmRoutingService.fetchRoute(31.4504, 73.1350, 31.4610, 73.1510, OSRMRouteProfile.DRIVING)
            assertTrue(fsdRes.isSuccess)
            val fsdRoute = fsdRes.getOrThrow()
            assertEquals(3, fsdRoute.coordinates.size)
            assertEquals(31.4504, fsdRoute.coordinates.first().first, 0.0001)
            assertEquals(73.1350, fsdRoute.coordinates.first().second, 0.0001)
            assertEquals(3450.2, fsdRoute.distanceMeters, 0.1)
            assertEquals(420.0, fsdRoute.durationSeconds!!, 0.1)
            assertEquals("Sargodha Road", fsdRoute.roadSummary)
            assertEquals(2, fsdRoute.steps.size)

            // Non-Faisalabad global test: Tokyo walking route omitting duration
            fakeNow += 2000L
            val tokyoRes = OsrmRoutingService.fetchRoute(35.6762, 139.6503, 35.6820, 139.6601, OSRMRouteProfile.WALKING)
            assertTrue(tokyoRes.isSuccess)
            val tokyoRoute = tokyoRes.getOrThrow()
            assertEquals(OSRMRouteProfile.WALKING, tokyoRoute.profile)
            assertNull("Missing OSRM duration must remain null and never be fabricated", tokyoRoute.durationSeconds)
            assertEquals(840.5, tokyoRoute.distanceMeters, 0.1)

            // Rapid rate-limit check + HTTP 429 error honesty
            val throttled = OsrmRoutingService.fetchRoute(51.5074, -0.1278, 51.5150, -0.1200, OSRMRouteProfile.CYCLING)
            assertTrue(throttled.isFailure)

            fakeNow += 2000L
            OsrmRoutingService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(429)
                        .message("Too Many Requests")
                        .body("rate limited".toResponseBody("text/plain".toMediaType()))
                        .build()
                }
                .build()
            val err429 = OsrmRoutingService.fetchRoute(51.5074, -0.1278, 51.5150, -0.1200, OSRMRouteProfile.CYCLING)
            assertTrue(err429.isFailure)
            assertTrue(err429.exceptionOrNull() is OsrmRateLimitException)
        }
    }

    @Test
    fun overpassService_buildingPlaceOfWorshipMuslimOnly_andWayFootprintExtraction() {
        runBlocking {
            val q = OverpassMosqueService.buildOverpassQuery(31.4504, 73.1350, 3000)
            assertTrue(q.contains("""nwr["building"="place_of_worship"]["religion"="muslim"]"""))
            assertTrue(q.contains("31.450400,73.135000"))

            // Non-Muslim place of worship must NEVER pass filter
            val mixedJson = """
                {
                  "elements": [
                    {
                      "type": "way",
                      "id": 5001,
                      "center": { "lat": 31.451, "lon": 73.136 },
                      "tags": { "name": "Jamia Masjid Al-Noor", "building": "place_of_worship", "religion": "muslim" }
                    },
                    {
                      "type": "node",
                      "id": 5002,
                      "lat": 31.452,
                      "lon": 73.137,
                      "tags": { "name": "St. Mary Church", "building": "place_of_worship", "religion": "christian" }
                    },
                    {
                      "type": "node",
                      "id": 5003,
                      "lat": 31.453,
                      "lon": 73.138,
                      "tags": { "name": "Unmarked Building", "building": "place_of_worship" }
                    }
                  ]
                }
            """.trimIndent()
            val parsed = OverpassMosqueService.parseOverpassJson(mixedJson, 31.4504, 73.1350)
            assertEquals(1, parsed.size)
            assertEquals(5001L, parsed.single().osmId)
            assertEquals(OsmVerificationLevel.VERIFIED_MUSLIM_PLACE_OF_WORSHIP, parsed.single().verificationLevel)

            // Verify way polygon footprint parsing
            val geomJson = """
                {
                  "elements": [{
                    "type": "way",
                    "id": 5001,
                    "geometry": [
                      { "lat": 31.4510, "lon": 73.1360 },
                      { "lat": 31.4512, "lon": 73.1360 },
                      { "lat": 31.4512, "lon": 73.1363 },
                      { "lat": 31.4510, "lon": 73.1363 }
                    ]
                  }]
                }
            """.trimIndent()
            val pts = OverpassMosqueService.parseFootprintGeometry(geomJson)
            assertEquals(4, pts.size)
            assertEquals(31.4510, pts.first().first, 0.0001)
        }
    }

    @Test
    fun mosqueFinderScreen_inAppOsrmRouteOverlay_andPoorGpsAccuracyWarning() {
        runBlocking {
            assertTrue(AppLocationManager.isPoorAccuracy(250f))
            assertFalse(AppLocationManager.isPoorAccuracy(18f))

            OverpassMosqueService.endpointsForTest = listOf("https://overpass.mock/api/interpreter")
            OverpassMosqueService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val json = """
                        {
                          "elements": [{
                            "type": "node",
                            "id": 8801,
                            "lat": 31.4580,
                            "lon": 73.1420,
                            "tags": { "name": "Jamia Masjid Faisalabad", "amenity": "place_of_worship", "religion": "muslim" }
                          }]
                        }
                    """.trimIndent()
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body(json.toResponseBody("application/json".toMediaType()))
                        .build()
                }
                .build()

            OsrmRoutingService.endpointsForTest = mapOf(
                OSRMRouteProfile.DRIVING to listOf("https://osrm.mock/routed-car/route/v1/driving")
            )
            OsrmRoutingService.httpClientForTest = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val json = """
                        {
                          "code": "Ok",
                          "routes": [{
                            "distance": 1650.0,
                            "duration": 240.0,
                            "geometry": { "type": "LineString", "coordinates": [[73.1350, 31.4504], [73.1385, 31.4540], [73.1420, 31.4580]] },
                            "legs": [{ "summary": "Canal Expressway", "steps": [] }]
                          }]
                        }
                    """.trimIndent()
                    Response.Builder()
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .message("OK")
                        .body(json.toResponseBody("application/json".toMediaType()))
                        .build()
                }
                .build()

            AppLocationManager.setManualCoordinates(appContext, 31.4504, 73.1350, "Faisalabad", "Pakistan", "Asia/Karachi")
            MosqueFinderRepository.searchNearbyMosques(
                context = appContext,
                lat = 31.4504,
                lon = 73.1350,
                radiusMeters = 5000,
                label = "Faisalabad",
                precisionMode = LocationPrecisionMode.MANUAL_PRESET
            )

            composeRule.setContent {
                NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                    MosqueFinderScreen(language = AppLanguage.ENGLISH, onBack = {})
                }
            }
            composeRule.waitForIdle()

            // Click Faisalabad mosque pin -> click Show Route on Map -> verify route polyline & HUD render
            composeRule.onNodeWithTag("mosque_pin_8801").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("mosque_show_route_btn").performScrollTo().performClick()
            composeRule.waitForIdle()

            composeRule.onNodeWithTag("mosque_map_route_line").assertExists()
            composeRule.onNodeWithTag("mosque_active_route_hud").assertIsDisplayed()

            // Clear route cleanly
            composeRule.onNodeWithTag("mosque_hud_clear_route").performClick()
            composeRule.waitForIdle()
            composeRule.onAllNodesWithTag("mosque_map_route_line").assertCountEquals(0)
        }
    }

    @Test
    fun seerahCatalog_coversAll9Eras_all8Battles_all13Traits_and3TierIntegrity() {
        val articles = SeerahRepository.allArticles
        assertEquals(33, articles.size)
        assertEquals((1..33).toList(), articles.map { it.orderIndex })

        // All 9 eras represented
        assertEquals(SeerahEra.entries.toSet(), articles.map { it.era }.toSet())

        // All 8 battles/expeditions
        val battleIds = articles.filter { it.era == SeerahEra.BATTLES_EXPEDITIONS }.map { it.id }
        assertEquals(8, battleIds.size)
        listOf("badr", "uhud", "khandaq", "hudaybiyyah", "khaybar", "conquest_makkah", "hunayn", "tabuk").forEach { key ->
            assertTrue("Missing battle $key", battleIds.any { it.contains(key) })
        }

        // All 13 character traits
        val charCount = articles.count { it.era == SeerahEra.CHARACTER_PERSONAL_LIFE }
        assertEquals(13, charCount)

        // Tier-2 / Tier-3 honesty check: no Tier-2 citation falsely claims Sahih Hadith
        articles.forEach { article ->
            assertTrue("Missing citations for ${article.id}", article.citations.isNotEmpty())
            assertTrue("Missing external reference links for ${article.id}", article.referenceLinks.isNotEmpty())
            AppLanguage.entries.forEach { lang ->
                assertTrue(article.textFor(article.title, lang).isNotBlank())
                assertTrue(article.textFor(article.overview, lang).isNotBlank())
                assertTrue(article.textFor(article.lessonsToday, lang).isNotBlank())
            }
            article.citations.filter { it.tier == SourceCredibilityTier.TIER_2_CLASSICAL_SEERAH }.forEach { cit ->
                val badge = cit.tier.shortBadge(AppLanguage.ENGLISH)
                assertTrue(badge.contains("Not Sahih Hadith"))
            }
        }
    }

    @Test
    fun seerahScreen_searchBookmarksProgressResumeAndAll10ReaderSections() {
        // Test search across Quran ref & Hadith ref
        val iqraResults = SeerahRepository.searchArticles("96:1")
        assertTrue(iqraResults.any { it.id == "seerah_03_cave_hira_first_revelation" })
        val hadithResults = SeerahRepository.searchArticles("Bukhari 349")
        assertTrue(hadithResults.any { it.id == "seerah_06_isra_miraj_aqabah" })

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                SeerahScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // Progress starts at 0 / 33
        composeRule.onNodeWithTag("seerah_progress_count").assertTextEquals("0 / 33")

        // Filter to Beginning of Revelation and open Cave Hira
        composeRule.onNodeWithTag("seerah_era_beginning_of_revelation")
            .performScrollTo()
            .performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("seerah_list_container")
            .performScrollToNode(hasTestTag("seerah_article_seerah_03_cave_hira_first_revelation"))
        composeRule.onNodeWithTag("seerah_article_seerah_03_cave_hira_first_revelation").performClick()
        composeRule.waitForIdle()

        // Verify key 10 reader sections + Prev/Next navigation
        composeRule.onNodeWithTag("seerah_detail_title").assertExists()
        composeRule.onNodeWithTag("seerah_detail_chronology").assertExists()
        composeRule.onNodeWithTag("seerah_detail_overview").assertExists()
        composeRule.onNodeWithTag("seerah_detail_context").assertExists()
        composeRule.onNodeWithTag("seerah_detail_tier1_citations").assertExists()
        composeRule.onNodeWithTag("seerah_detail_secondary_citations").assertExists()
        composeRule.onNodeWithTag("seerah_detail_differences").assertExists()
        composeRule.onNodeWithTag("seerah_detail_lessons").assertExists()
        composeRule.onNodeWithTag("seerah_detail_next_milestone").assertExists()
        composeRule.onNodeWithTag("seerah_detail_links").assertExists()

        // Bookmark and move to Next article (#4)
        composeRule.onNodeWithTag("seerah_detail_bookmark_btn").performClick()
        composeRule.onNodeWithTag("seerah_next_btn").performClick()
        composeRule.waitForIdle()

        // Navigate back to list -> Progress updated to 2 / 33 and Resume card shown
        composeRule.onNodeWithTag("top_bar_back_button").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("seerah_progress_count").assertTextEquals("2 / 33")
        composeRule.onNodeWithTag("seerah_resume_card").assertExists()
    }

    @Test
    fun overpassService_parsesPostpassPhotonAndNominatimPoiFeeds_andRejectsNonMuslimSites() {
        val bounds = OverpassMosqueService.boundingBoxFromRadius(31.4181, 73.0836, 5000)
        assertTrue(bounds.isValid())
        val sql = OverpassMosqueService.buildPostpassBboxSql(bounds)
        assertTrue(sql.contains("postpass_pointpolygon"))
        assertTrue(sql.contains("ST_MakeEnvelope"))
        assertTrue(sql.contains("ST_Centroid(geom)"))

        val postpassGeoJson = """
            {
              "type": "FeatureCollection",
              "features": [
                {
                  "type": "Feature",
                  "properties": {
                    "osm_id": 5001,
                    "osm_type": "N",
                    "tags": {
                      "amenity": "place_of_worship",
                      "religion": "muslim",
                      "name": "Jamia Masjid Rizvia",
                      "addr:city": "Faisalabad"
                    }
                  },
                  "geometry": { "type": "Point", "coordinates": [73.0840, 31.4190] }
                },
                {
                  "type": "Feature",
                  "properties": {
                    "osm_id": -6002,
                    "osm_type": "R",
                    "tags": {
                      "building": "mosque",
                      "name": "Masjid Al-Quba"
                    }
                  },
                  "geometry": {
                    "type": "Polygon",
                    "coordinates": [[[73.081, 31.417], [73.082, 31.417], [73.082, 31.418], [73.081, 31.418], [73.081, 31.417]]]
                  }
                },
                {
                  "type": "Feature",
                  "properties": {
                    "osm_id": 7003,
                    "osm_type": "W",
                    "tags": {
                      "amenity": "place_of_worship",
                      "religion": "christian",
                      "name": "St Peter Church"
                    }
                  },
                  "geometry": { "type": "Point", "coordinates": [73.0850, 31.4185] }
                }
              ]
            }
        """.trimIndent()

        val postpassItems = OverpassMosqueService.parsePostpassGeoJson(
            rawJson = postpassGeoJson,
            originLat = 31.4181,
            originLon = 73.0836,
            fetchedAtMs = 1_700_000_000_000L
        )
        assertEquals(2, postpassItems.size)
        assertEquals("node/5001", postpassItems.first { it.osmId == 5001L }.id)
        assertEquals(MosqueCategory.JAMI_MOSQUE, postpassItems.first { it.osmId == 5001L }.category)
        val relItem = postpassItems.first { it.osmId == 6002L }
        assertEquals("relation", relItem.osmType)
        assertTrue(relItem.latitude in 31.417..31.418)
        assertTrue(postpassItems.none { it.osmId == 7003L })

        val photonGeoJson = """
            {
              "type": "FeatureCollection",
              "features": [
                {
                  "type": "Feature",
                  "geometry": { "type": "Point", "coordinates": [73.0842, 31.4189] },
                  "properties": {
                    "osm_id": 8101,
                    "osm_type": "N",
                    "osm_key": "amenity",
                    "osm_value": "place_of_worship",
                    "name": "Masjid-e-Noor",
                    "street": "Jinnah Colony",
                    "city": "Faisalabad"
                  }
                },
                {
                  "type": "Feature",
                  "geometry": { "type": "Point", "coordinates": [73.0845, 31.4192] },
                  "properties": {
                    "osm_id": 8102,
                    "osm_type": "N",
                    "osm_key": "amenity",
                    "osm_value": "place_of_worship",
                    "name": "Trinity Mosque Road Church",
                    "city": "Faisalabad"
                  }
                }
              ]
            }
        """.trimIndent()

        val photonItems = OverpassMosqueService.parsePhotonGeoJson(
            rawJson = photonGeoJson,
            originLat = 31.4181,
            originLon = 73.0836
        )
        assertEquals(1, photonItems.size)
        assertEquals("Masjid-e-Noor", photonItems.first().name)
        assertEquals("Jinnah Colony, Faisalabad", photonItems.first().address)

        val nominatimJson = """
            [
              {
                "osm_type": "way",
                "osm_id": 9201,
                "lat": "31.4195",
                "lon": "73.0851",
                "name": "Faisalabad Central Mosque",
                "class": "amenity",
                "type": "place_of_worship",
                "extratags": {
                  "religion": "muslim",
                  "opening_hours": "04:30-22:00"
                },
                "address": {
                  "road": "Kotwali Road",
                  "city": "Faisalabad"
                }
              },
              {
                "osm_type": "node",
                "osm_id": 9202,
                "lat": "31.4197",
                "lon": "73.0853",
                "name": "Sri Mandir",
                "class": "amenity",
                "type": "place_of_worship",
                "extratags": {
                  "religion": "hindu"
                }
              }
            ]
        """.trimIndent()

        val nomItems = OverpassMosqueService.parseNominatimPoiJson(
            rawJson = nominatimJson,
            originLat = 31.4181,
            originLon = 73.0836
        )
        assertEquals(1, nomItems.size)
        assertEquals("way/9201", nomItems.first().id)
        assertEquals(MosqueCategory.JAMI_MOSQUE, nomItems.first().category)
        assertEquals("04:30-22:00", nomItems.first().openingHours)
        assertEquals("Kotwali Road, Faisalabad", nomItems.first().address)
    }
}
