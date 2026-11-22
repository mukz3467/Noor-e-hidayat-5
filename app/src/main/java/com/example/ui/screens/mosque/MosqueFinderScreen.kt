package com.example.ui.screens.mosque

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.mosque.GeocodedArea
import com.example.data.mosque.LocationPrecisionMode
import com.example.data.mosque.MapBoundingBox
import com.example.data.mosque.MapStylePreset
import com.example.data.mosque.MosqueCategory
import com.example.data.mosque.MosqueFinderRepository
import com.example.data.mosque.MosqueItem
import com.example.data.mosque.MosqueRouteState
import com.example.data.mosque.NominatimGeocoderService
import com.example.data.mosque.OSRMRouteProfile
import com.example.data.mosque.OsmVerificationLevel
import com.example.data.mosque.OsrmRoutingService
import com.example.data.mosque.OverpassMosqueService
import com.example.localization.AppLocalization
import com.example.ui.components.noorSelectableChipColors
import kotlinx.coroutines.launch
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val HeaderEmerald = Color(0xFF0A4D2E)
private val HeaderEmeraldActive = Color(0xFF157A4C)
private val EmeraldAction = Color(0xFF0B6E4F)
private val HeaderGold = Color(0xFFFBBF24)

@Composable
fun MosqueFinderScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val searchState by MosqueFinderRepository.state.collectAsStateWithLifecycle()
    val routeState by MosqueFinderRepository.routeState.collectAsStateWithLifecycle()
    val selectedFootprintPair by MosqueFinderRepository.selectedFootprint.collectAsStateWithLifecycle()
    val appLocation by AppLocationManager.currentLocation.collectAsStateWithLifecycle()
    val isLocatingGps by AppLocationManager.isLocating.collectAsStateWithLifecycle()
    val locationMgrError by AppLocationManager.locationError.collectAsStateWithLifecycle()

    var areaQuery by remember { mutableStateOf("") }
    var keywordFilter by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<MosqueCategory?>(null) }
    var isMapView by remember { mutableStateOf(true) }
    var is3dEnabled by remember { mutableStateOf(true) }
    var stylePreset by remember { mutableStateOf(MapStylePreset.HYBRID_3D) }
    var showFilters by remember { mutableStateOf(false) }
    var focusedMosque by remember { mutableStateOf<MosqueItem?>(null) }
    var sheetMosque by remember { mutableStateOf<MosqueItem?>(null) }
    var focusRequestCounter by remember { mutableIntStateOf(0) }
    var areaMatches by remember { mutableStateOf<List<GeocodedArea>>(emptyList()) }
    var areaLookupError by remember { mutableStateOf<String?>(null) }
    var isGeocoding by remember { mutableStateOf(false) }
    var showEndpointDialog by remember { mutableStateOf(false) }
    var locationFixError by remember { mutableStateOf<String?>(null) }
    var visibleListCount by remember { mutableIntStateOf(MosqueFinderRepository.DEFAULT_PAGE_SIZE) }

    val hasPermission = AppLocationManager.hasLocationPermission(context)
    val isGpsEnabled = AppLocationManager.isGpsEnabled(context)

    val triggerNearbySearch: (Double, Double, Int, String, LocationPrecisionMode, Boolean) -> Unit =
        { lat, lon, radius, label, mode, force ->
            scope.launch {
                MosqueFinderRepository.searchNearbyMosques(
                    context = context,
                    lat = lat,
                    lon = lon,
                    radiusMeters = radius,
                    label = label,
                    precisionMode = mode,
                    forceRefresh = force
                )
            }
        }

    val triggerBboxSearch: (MapBoundingBox) -> Unit = { bounds ->
        val originLat = if (MosqueFinderRepository.hasConfirmedCoordinates(appLocation)) {
            appLocation.latitude
        } else {
            bounds.centerLat
        }
        val originLon = if (MosqueFinderRepository.hasConfirmedCoordinates(appLocation)) {
            appLocation.longitude
        } else {
            bounds.centerLon
        }
        val precision = if (hasPermission && MosqueFinderRepository.hasConfirmedCoordinates(appLocation)) {
            MosqueFinderRepository.resolvePrecisionMode(context)
        } else {
            LocationPrecisionMode.MANUAL_PRESET
        }
        scope.launch {
            MosqueFinderRepository.searchMosquesInVisibleBounds(
                context = context,
                bounds = bounds,
                distanceOriginLat = originLat,
                distanceOriginLon = originLon,
                label = AppLocalization.get("mosque_map_area_label", language),
                precisionMode = precision,
                forceRefresh = true
            )
        }
    }

    val fetchRealGpsAndSearch: () -> Unit = {
        locationFixError = null
        AppLocationManager.requestLocationUpdate(context, forceFresh = true) { ok, msg ->
            if (ok) {
                val loc = AppLocationManager.currentLocation.value
                if (MosqueFinderRepository.hasConfirmedCoordinates(loc)) {
                    triggerNearbySearch(
                        loc.latitude,
                        loc.longitude,
                        searchState.radiusMeters,
                        loc.cityName,
                        MosqueFinderRepository.resolvePrecisionMode(context),
                        true
                    )
                }
            } else {
                locationFixError = msg
            }
        }
    }

    val selectAndFocusMosque: (MosqueItem, Boolean) -> Unit = { mosque, openSheet ->
        focusedMosque = mosque
        focusRequestCounter += 1
        if (openSheet) {
            sheetMosque = mosque
        }
        scope.launch {
            MosqueFinderRepository.fetchFootprintForMosque(mosque)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        MosqueFinderRepository.refreshPermissionState(context)
        if (AppLocationManager.hasLocationPermission(context)) {
            fetchRealGpsAndSearch()
        }
    }

    LaunchedEffect(Unit) {
        AppLocationManager.init(context)
        MosqueFinderRepository.refreshPermissionState(context)
        val hadCache = if (!searchState.hasSearched) {
            MosqueFinderRepository.loadLastCachedSession(context)
        } else {
            false
        }
        val permNow = AppLocationManager.hasLocationPermission(context)
        val gpsNow = AppLocationManager.isGpsEnabled(context)
        val loc = AppLocationManager.currentLocation.value
        val hasRealSavedCoords = MosqueFinderRepository.hasConfirmedCoordinates(loc)

        // Strict integrity rule: NEVER auto-search using DEFAULT_ESTIMATE (timezone guess)!
        if (permNow && gpsNow) {
            if (!searchState.hasSearched) {
                fetchRealGpsAndSearch()
            }
        } else if (!searchState.hasSearched && !hadCache && hasRealSavedCoords) {
            val mode = if (permNow) {
                MosqueFinderRepository.resolvePrecisionMode(context)
            } else {
                LocationPrecisionMode.MANUAL_PRESET
            }
            triggerNearbySearch(loc.latitude, loc.longitude, searchState.radiusMeters, loc.cityName, mode, false)
        }
    }

    val filteredMosques = remember(searchState.mosques, keywordFilter, selectedCategory) {
        MosqueFinderRepository.filterMosques(searchState.mosques, keywordFilter, selectedCategory)
    }

    LaunchedEffect(searchState.mosques, keywordFilter, selectedCategory, searchState.radiusMeters) {
        visibleListCount = MosqueFinderRepository.DEFAULT_PAGE_SIZE
    }

    val nextLargerRadius = remember(searchState.radiusMeters) {
        MosqueFinderRepository.nextLargerRadiusMeters(searchState.radiusMeters)
    }

    // Auto-select nearest mosque cleanly for Picture-3 bottom preview card & red pin without popping modal sheet
    LaunchedEffect(filteredMosques) {
        val current = focusedMosque
        if (filteredMosques.isEmpty()) {
            focusedMosque = null
        } else if (current == null || filteredMosques.none { it.id == current.id }) {
            val nearest = filteredMosques.first()
            focusedMosque = nearest
            scope.launch { MosqueFinderRepository.fetchFootprintForMosque(nearest) }
        }
    }

    val hasConfirmedUserLoc = hasPermission && MosqueFinderRepository.hasConfirmedCoordinates(appLocation)
    val hasSearchCenter = searchState.searchCenterLat != 0.0 || searchState.searchCenterLon != 0.0
    val hasEffectiveCenter = hasSearchCenter || hasConfirmedUserLoc
    val effectiveLat = when {
        hasSearchCenter -> searchState.searchCenterLat
        hasConfirmedUserLoc -> appLocation.latitude
        else -> 0.0
    }
    val effectiveLon = when {
        hasSearchCenter -> searchState.searchCenterLon
        hasConfirmedUserLoc -> appLocation.longitude
        else -> 0.0
    }
    val routeOriginLat = if (hasConfirmedUserLoc) appLocation.latitude else effectiveLat
    val routeOriginLon = if (hasConfirmedUserLoc) appLocation.longitude else effectiveLon

    val submitAreaLookup: () -> Unit = {
        val q = areaQuery.trim()
        if (q.length >= 2 && !isGeocoding) {
            keyboardController?.hide()
            focusManager.clearFocus()
            showFilters = false
            isMapView = true
            scope.launch {
                isGeocoding = true
                areaLookupError = null
                val res = NominatimGeocoderService.searchSubmittedArea(
                    rawQuery = q,
                    biasLat = if (hasEffectiveCenter) effectiveLat else null,
                    biasLon = if (hasEffectiveCenter) effectiveLon else null
                )
                isGeocoding = false
                res.onSuccess { list ->
                    if (list.isEmpty()) {
                        areaMatches = emptyList()
                        areaLookupError = AppLocalization.get("mosque_area_not_found", language)
                    } else {
                        val first = list.first()
                        areaMatches = if (list.size > 1) list else emptyList()
                        keywordFilter = ""
                        MosqueFinderRepository.clearRoute()
                        focusedMosque = null
                        triggerNearbySearch(
                            first.latitude,
                            first.longitude,
                            searchState.radiusMeters,
                            first.shortName,
                            LocationPrecisionMode.MANUAL_PRESET,
                            true
                        )
                    }
                }.onFailure { err ->
                    areaLookupError = err.message ?: AppLocalization.get("mosque_area_not_found", language)
                }
            }
        }
    }

    val activeFootprint = remember(selectedFootprintPair, focusedMosque) {
        val pair = selectedFootprintPair
        if (pair != null && focusedMosque?.id == pair.first) pair.second else emptyList()
    }

    val poorAccuracyMeters = if (appLocation.isGpsFix && AppLocationManager.isPoorAccuracy(appLocation.accuracyMeters)) {
        appLocation.accuracyMeters?.toInt()
    } else null

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Surface(
                color = HeaderEmerald,
                contentColor = Color.White,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    // Row 1: Back + Mosque Emblem + Title + 2D|3D Pill + GPS + Endpoint Mirror Settings
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("top_bar_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = AppLocalization.get("back_cd", language),
                                tint = Color.White
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = null,
                            tint = HeaderGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("mosque_finder", language),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            val sub = searchState.searchLabel.takeIf { it.isNotBlank() }
                                ?: AppLocalization.get("app_name", language)
                            Text(
                                text = sub,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                color = Color.White.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Picture-3 Segmented 2D | 3D Capsule Toggle
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0x33000000),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                            modifier = Modifier.padding(end = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(2.dp)
                            ) {
                                if (!is3dEnabled) {
                                    Surface(
                                        onClick = { is3dEnabled = true },
                                        shape = RoundedCornerShape(18.dp),
                                        color = HeaderEmeraldActive,
                                        modifier = Modifier
                                            .defaultMinSize(minHeight = 36.dp)
                                            .testTag("mosque_map_3d_toggle")
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = AppLocalization.get("mosque_mode_2d", language),
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                                                color = Color.White
                                            )
                                        }
                                    }
                                    Text(
                                        text = AppLocalization.get("mosque_mode_3d", language),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White.copy(alpha = 0.72f),
                                        modifier = Modifier
                                            .clickable { is3dEnabled = true }
                                            .padding(horizontal = 9.dp, vertical = 6.dp)
                                    )
                                } else {
                                    Text(
                                        text = AppLocalization.get("mosque_mode_2d", language),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White.copy(alpha = 0.72f),
                                        modifier = Modifier
                                            .clickable { is3dEnabled = false }
                                            .padding(horizontal = 9.dp, vertical = 6.dp)
                                    )
                                    Surface(
                                        onClick = { is3dEnabled = false },
                                        shape = RoundedCornerShape(18.dp),
                                        color = HeaderEmeraldActive,
                                        modifier = Modifier
                                            .defaultMinSize(minHeight = 36.dp)
                                            .testTag("mosque_map_3d_toggle")
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = AppLocalization.get("mosque_mode_3d", language),
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Picture-3 Top-Right GPS Button
                        IconButton(
                            onClick = {
                                if (!hasPermission) {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                } else if (!isGpsEnabled) {
                                    AppLocationManager.openLocationSettings(context)
                                } else {
                                    fetchRealGpsAndSearch()
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("mosque_use_gps_btn")
                        ) {
                            if (isLocatingGps) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = HeaderGold
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = AppLocalization.get("mosque_use_my_location", language),
                                    tint = Color.White
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEndpointDialog = true },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("mosque_endpoint_settings_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SettingsEthernet,
                                contentDescription = AppLocalization.get("mosque_endpoint_settings_title", language),
                                tint = Color.White.copy(alpha = 0.88f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Row 2: Crisp White Pill Search Bar + Filters + List/Map View Switch
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 44.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 10.dp, end = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = HeaderEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    contentAlignment = Alignment.CenterStart,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (areaQuery.isEmpty()) {
                                        Text(
                                            text = AppLocalization.get("mosque_search_area_hint", language),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF64748B),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    BasicTextField(
                                        value = areaQuery,
                                        onValueChange = { areaQuery = it },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            color = Color(0xFF0F172A),
                                            fontSize = 13.5.sp
                                        ),
                                        cursorBrush = SolidColor(HeaderEmerald),
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                        keyboardActions = KeyboardActions(onSearch = { submitAreaLookup() }),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("mosque_search_input")
                                    )
                                }
                                if (areaQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { areaQuery = "" },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null,
                                            tint = Color(0xFF64748B),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = submitAreaLookup,
                                    enabled = !isGeocoding && areaQuery.trim().length >= 2,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .testTag("mosque_search_submit")
                                ) {
                                    if (isGeocoding) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = HeaderEmerald
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.NearMe,
                                            contentDescription = AppLocalization.get("mosque_search_btn", language),
                                            tint = if (areaQuery.trim().length >= 2) HeaderEmerald else Color(0xFF94A3B8),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Surface(
                            onClick = { showFilters = !showFilters },
                            shape = RoundedCornerShape(20.dp),
                            color = if (showFilters) HeaderEmeraldActive else Color(0x33FFFFFF),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .sizeIn(minWidth = 48.dp, minHeight = 44.dp)
                                .testTag("mosque_filters_toggle")
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = AppLocalization.get("mosque_filters_label", language),
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = { isMapView = false },
                            modifier = Modifier
                                .size(48.dp)
                                .semantics { selected = !isMapView }
                                .testTag("mosque_view_toggle_list")
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = AppLocalization.get("mosque_list_view", language),
                                tint = if (!isMapView) HeaderGold else Color.White.copy(alpha = 0.78f)
                            )
                        }

                        IconButton(
                            onClick = { isMapView = true },
                            modifier = Modifier
                                .size(48.dp)
                                .semantics { selected = isMapView }
                                .testTag("mosque_view_toggle_map")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = AppLocalization.get("mosque_map_view", language),
                                tint = if (isMapView) HeaderGold else Color.White.copy(alpha = 0.78f)
                            )
                        }
                    }

                    // Row 3: Always-accessible Radius Options (1/3/5/10 km) + Live Result Count Pill
                    val headerChipColors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent,
                        labelColor = Color.White,
                        selectedContainerColor = HeaderGold,
                        selectedLabelColor = HeaderEmerald
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        MosqueFinderRepository.RADIUS_OPTIONS_METERS.forEach { meters ->
                            val km = meters / 1000
                            val selected = searchState.radiusMeters == meters
                            FilterChip(
                                selected = selected,
                                onClick = {
                                    if (hasEffectiveCenter) {
                                        triggerNearbySearch(
                                            effectiveLat,
                                            effectiveLon,
                                            meters,
                                            searchState.searchLabel,
                                            searchState.precisionMode,
                                            true
                                        )
                                    }
                                },
                                label = { Text("$km km") },
                                colors = headerChipColors,
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selected,
                                    borderColor = Color.White.copy(alpha = 0.45f)
                                ),
                                modifier = Modifier.testTag("mosque_radius_$km")
                            )
                        }

                        if (searchState.hasSearched && !searchState.isLoading) {
                            val radiusKm = (searchState.radiusMeters / 1000).coerceAtLeast(1)
                            val countLabel = if (filteredMosques.size == 1) {
                                String.format(
                                    Locale.getDefault(),
                                    AppLocalization.get("mosque_result_single_fmt", language),
                                    radiusKm
                                )
                            } else {
                                String.format(
                                    Locale.getDefault(),
                                    AppLocalization.get("mosque_results_count_fmt", language),
                                    filteredMosques.size,
                                    radiusKm
                                )
                            }
                            Surface(
                                onClick = { isMapView = !isMapView },
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0x38FFFFFF),
                                border = BorderStroke(1.dp, HeaderGold.copy(alpha = 0.7f)),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 32.dp)
                                    .testTag("mosque_results_count_pill")
                            ) {
                                Text(
                                    text = countLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = HeaderGold,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Collapsible Filter Tray (Category + Local Name Filter)
                    AnimatedVisibility(visible = showFilters) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp, bottom = 2.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 4.dp)
                            ) {
                                FilterChip(
                                    selected = selectedCategory == null,
                                    onClick = { selectedCategory = null },
                                    label = { Text(AppLocalization.get("mosque_cat_all", language)) },
                                    colors = headerChipColors,
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = selectedCategory == null,
                                        borderColor = Color.White.copy(alpha = 0.45f)
                                    ),
                                    modifier = Modifier.testTag("mosque_cat_all")
                                )
                                MosqueCategory.entries.forEach { cat ->
                                    val isCatSelected = selectedCategory == cat
                                    FilterChip(
                                        selected = isCatSelected,
                                        onClick = { selectedCategory = if (isCatSelected) null else cat },
                                        label = { Text(cat.label(language)) },
                                        colors = headerChipColors,
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = isCatSelected,
                                            borderColor = Color.White.copy(alpha = 0.45f)
                                        ),
                                        modifier = Modifier.testTag("mosque_cat_${cat.id}")
                                    )
                                }
                            }
                            if (searchState.mosques.isNotEmpty()) {
                                OutlinedTextField(
                                    value = keywordFilter,
                                    onValueChange = { keywordFilter = it },
                                    placeholder = {
                                        Text(
                                            text = AppLocalization.get("mosque_filter_name_short", language),
                                            color = Color.White.copy(alpha = 0.7f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = HeaderGold,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                                    ),
                                    trailingIcon = {
                                        if (keywordFilter.isNotBlank()) {
                                            IconButton(onClick = { keywordFilter = "" }) {
                                                Icon(Icons.Default.Clear, contentDescription = null, tint = Color.White)
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                        .testTag("mosque_local_filter_input")
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val isCompactViewport = maxHeight < 620.dp
            val hasBottomPreviewCard = isMapView && sheetMosque == null && (
                focusedMosque != null ||
                    routeState.route != null ||
                    routeState.isLoading ||
                    !routeState.errorMessage.isNullOrBlank()
                )

            if (isMapView) {
                Column(modifier = Modifier.fillMaxSize()) {
                    val hasAnyOverlay = areaMatches.isNotEmpty() ||
                        areaLookupError != null ||
                        !hasPermission ||
                        !isGpsEnabled ||
                        poorAccuracyMeters != null ||
                        (searchState.isFromCache && searchState.cacheTimestampMs != null) ||
                        !(searchState.errorMessage ?: locationFixError ?: locationMgrError).isNullOrBlank()

                    if (hasAnyOverlay) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(HeaderEmerald.copy(alpha = 0.94f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            if (areaMatches.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("mosque_area_matches_bar")
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .weight(1f)
                                            .horizontalScroll(rememberScrollState())
                                    ) {
                                        areaMatches.forEachIndexed { idx, candidate ->
                                            val isSelectedCity = searchState.searchLabel.equals(
                                                candidate.shortName,
                                                ignoreCase = true
                                            )
                                            Surface(
                                                onClick = {
                                                    keyboardController?.hide()
                                                    focusManager.clearFocus()
                                                    showFilters = false
                                                    isMapView = true
                                                    keywordFilter = ""
                                                    MosqueFinderRepository.clearRoute()
                                                    focusedMosque = null
                                                    triggerNearbySearch(
                                                        candidate.latitude,
                                                        candidate.longitude,
                                                        searchState.radiusMeters,
                                                        candidate.shortName,
                                                        LocationPrecisionMode.MANUAL_PRESET,
                                                        true
                                                    )
                                                },
                                                shape = RoundedCornerShape(18.dp),
                                                color = if (isSelectedCity) HeaderGold else Color.White.copy(alpha = 0.16f),
                                                border = BorderStroke(
                                                    1.dp,
                                                    if (isSelectedCity) HeaderGold else Color.White.copy(alpha = 0.35f)
                                                ),
                                                modifier = Modifier
                                                    .defaultMinSize(minHeight = 36.dp)
                                                    .testTag("mosque_area_match_$idx")
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.LocationOn,
                                                        contentDescription = null,
                                                        tint = if (isSelectedCity) HeaderEmerald else HeaderGold,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = candidate.shortName,
                                                        style = MaterialTheme.typography.labelMedium.copy(
                                                            fontWeight = if (isSelectedCity) FontWeight.Bold else FontWeight.Medium
                                                        ),
                                                        color = if (isSelectedCity) Color(0xFF0A3320) else Color.White,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    IconButton(
                                        onClick = { areaMatches = emptyList() },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .testTag("mosque_area_matches_close")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Dismiss city suggestions",
                                            tint = Color.White.copy(alpha = 0.85f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            if (areaLookupError != null) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = areaLookupError!!,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }

                            FloatingStatusOverlays(
                                hasPermission = hasPermission,
                                isGpsEnabled = isGpsEnabled,
                                poorAccuracyMeters = poorAccuracyMeters,
                                isFromCache = searchState.isFromCache,
                                isCacheStale = searchState.isCacheStale,
                                cacheTimestampMs = searchState.cacheTimestampMs,
                                activeEndpoint = searchState.activeEndpoint,
                                errorMessage = searchState.errorMessage ?: locationFixError ?: locationMgrError,
                                canRetry = hasEffectiveCenter,
                                compact = isCompactViewport,
                                language = language,
                                onRequestPermission = {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                },
                                onOpenLocationSettings = { AppLocationManager.openLocationSettings(context) },
                                onRetry = {
                                    if (hasEffectiveCenter) {
                                        triggerNearbySearch(
                                            effectiveLat,
                                            effectiveLon,
                                            searchState.radiusMeters,
                                            searchState.searchLabel,
                                            searchState.precisionMode,
                                            true
                                        )
                                    }
                                }
                            )
                        }
                    }

                    BoxWithConstraints(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        val isRouteHudActive = routeState.route != null ||
                            routeState.isLoading ||
                            !routeState.errorMessage.isNullOrBlank()
                        val showBottomPreviewOverlay = hasBottomPreviewCard &&
                            (maxHeight >= 250.dp || isRouteHudActive)

                        MosqueMapPane(
                            mosques = filteredMosques,
                            centerLat = effectiveLat,
                            centerLon = effectiveLon,
                            hasConfirmedCenter = hasEffectiveCenter,
                            radiusMeters = searchState.radiusMeters,
                            userLat = appLocation.latitude,
                            userLon = appLocation.longitude,
                            hasUserCoords = hasConfirmedUserLoc,
                            userAccuracyMeters = appLocation.accuracyMeters,
                            language = language,
                            selectedMosque = focusedMosque,
                            selectedFootprint = activeFootprint,
                            route = routeState.route,
                            is3dEnabled = is3dEnabled,
                            stylePreset = stylePreset,
                            onSelectStylePreset = { stylePreset = it },
                            isSearchingOverpass = searchState.isLoading,
                            focusRequestCounter = focusRequestCounter,
                            bottomControlsPadding = if (showBottomPreviewOverlay) 142.dp else 44.dp,
                            onSelectMosque = { selectAndFocusMosque(it, true) },
                            onSearchVisibleBounds = triggerBboxSearch,
                            onRecenter = {
                                if (hasPermission && isGpsEnabled) {
                                    fetchRealGpsAndSearch()
                                }
                            }
                        )

                        // Honest unset-location overlay when neither GPS nor search has been set
                        if (!searchState.hasSearched && !hasEffectiveCenter && !searchState.isLoading) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(horizontal = 12.dp, vertical = 42.dp)
                            ) {
                                LocationUnsetPromptCard(
                                    hasPermission = hasPermission,
                                    isGpsEnabled = isGpsEnabled,
                                    language = language,
                                    onRequestPermission = {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    },
                                    onOpenLocationSettings = { AppLocationManager.openLocationSettings(context) }
                                )
                            }
                        }

                        // Picture-3 Floating Bottom Mosque Card with multi-mosque switcher synced to map pins
                        if (showBottomPreviewOverlay) {
                            val activeTarget = focusedMosque
                                ?: filteredMosques.firstOrNull { it.id == routeState.targetMosqueId }
                            val isRouteHudVisible = isRouteHudActive
                            val activeIndex = if (activeTarget != null) {
                                filteredMosques.indexOfFirst { it.id == activeTarget.id }
                            } else {
                                -1
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 12.dp, end = 12.dp, bottom = 36.dp)
                                    .widthIn(max = 520.dp)
                            ) {
                                FloatingPicture3MosqueCard(
                                    mosque = activeTarget,
                                    mosqueIndex = activeIndex,
                                    totalMosques = filteredMosques.size,
                                    routeState = routeState,
                                    isRouteHudVisible = isRouteHudVisible,
                                    hasValidOrigin = hasEffectiveCenter,
                                    language = language,
                                    onPreviousMosque = {
                                        if (filteredMosques.size > 1 && activeIndex >= 0) {
                                            val prevIdx = (activeIndex - 1 + filteredMosques.size) % filteredMosques.size
                                            selectAndFocusMosque(filteredMosques[prevIdx], false)
                                        }
                                    },
                                    onNextMosque = {
                                        if (filteredMosques.size > 1 && activeIndex >= 0) {
                                            val nextIdx = (activeIndex + 1) % filteredMosques.size
                                            selectAndFocusMosque(filteredMosques[nextIdx], false)
                                        }
                                    },
                                    onOpenDetails = {
                                        val target = activeTarget ?: searchState.mosques.firstOrNull { it.id == routeState.targetMosqueId }
                                        if (target != null) {
                                            sheetMosque = target
                                        }
                                    },
                                    onNavigate = { prof ->
                                        val target = activeTarget ?: searchState.mosques.firstOrNull { it.id == routeState.targetMosqueId }
                                        if (target != null && hasEffectiveCenter) {
                                            scope.launch {
                                                MosqueFinderRepository.fetchRouteToMosque(
                                                    originLat = routeOriginLat,
                                                    originLon = routeOriginLon,
                                                    mosque = target,
                                                    profile = prof
                                                )
                                            }
                                        }
                                    },
                                    onClearRoute = { MosqueFinderRepository.clearRoute() }
                                )
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("mosque_list_container")
                ) {
                    if (searchState.mosques.isNotEmpty()) {
                        item {
                            OutlinedTextField(
                                value = keywordFilter,
                                onValueChange = { keywordFilter = it },
                                placeholder = { Text(AppLocalization.get("mosque_filter_results_hint", language)) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("mosque_local_filter_input")
                            )
                        }
                    }
                    item {
                        FloatingStatusOverlays(
                            hasPermission = hasPermission,
                            isGpsEnabled = isGpsEnabled,
                            poorAccuracyMeters = poorAccuracyMeters,
                            isFromCache = searchState.isFromCache,
                            isCacheStale = searchState.isCacheStale,
                            cacheTimestampMs = searchState.cacheTimestampMs,
                            activeEndpoint = searchState.activeEndpoint,
                            errorMessage = searchState.errorMessage ?: locationFixError ?: locationMgrError,
                            canRetry = hasEffectiveCenter,
                            compact = false,
                            language = language,
                            onRequestPermission = {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            onOpenLocationSettings = { AppLocationManager.openLocationSettings(context) },
                            onRetry = {
                                if (hasEffectiveCenter) {
                                    triggerNearbySearch(
                                        effectiveLat,
                                        effectiveLon,
                                        searchState.radiusMeters,
                                        searchState.searchLabel,
                                        searchState.precisionMode,
                                        true
                                    )
                                }
                            }
                        )
                    }
                    if (searchState.isLoading) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = AppLocalization.get("mosque_searching", language),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else if (!searchState.hasSearched && !hasEffectiveCenter) {
                        item {
                            LocationUnsetPromptCard(
                                hasPermission = hasPermission,
                                isGpsEnabled = isGpsEnabled,
                                language = language,
                                onRequestPermission = {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                },
                                onOpenLocationSettings = { AppLocationManager.openLocationSettings(context) }
                            )
                        }
                    } else if (filteredMosques.isEmpty() && searchState.hasSearched && searchState.errorMessage.isNullOrBlank()) {
                        item {
                            EmptyMosqueStateCard(language = language)
                        }
                    }

                    val displayedMosques = filteredMosques.take(visibleListCount)
                    items(displayedMosques, key = { it.id }) { mosque ->
                        MosqueListItemCard(
                            mosque = mosque,
                            isSelected = focusedMosque?.id == mosque.id,
                            language = language,
                            onClick = { selectAndFocusMosque(mosque, true) }
                        )
                    }

                    if (filteredMosques.size > visibleListCount) {
                        item {
                            val remaining = filteredMosques.size - visibleListCount
                            OutlinedButton(
                                onClick = { visibleListCount += MosqueFinderRepository.DEFAULT_PAGE_SIZE },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("mosque_load_more_btn")
                            ) {
                                Icon(Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = String.format(
                                        Locale.getDefault(),
                                        AppLocalization.get("mosque_load_more_fmt", language),
                                        remaining
                                    ),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    if (nextLargerRadius != null && hasEffectiveCenter && searchState.hasSearched && !searchState.isLoading) {
                        item {
                            val nextKm = nextLargerRadius / 1000
                            FilledTonalButton(
                                onClick = {
                                    triggerNearbySearch(
                                        effectiveLat,
                                        effectiveLon,
                                        nextLargerRadius,
                                        searchState.searchLabel,
                                        searchState.precisionMode,
                                        true
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("mosque_expand_radius_btn")
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = String.format(
                                        Locale.getDefault(),
                                        AppLocalization.get("mosque_expand_radius_fmt", language),
                                        nextKm
                                    ),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    item {
                        OsmCoverageFooterCard(language = language)
                    }
                }
            }
        }
    }

    if (showEndpointDialog) {
        OverpassEndpointDialog(
            currentPreferred = searchState.preferredEndpoint,
            activeEndpoint = searchState.activeEndpoint,
            language = language,
            onSelectEndpoint = { selectedUrl ->
                MosqueFinderRepository.setPreferredEndpoint(context, selectedUrl)
                showEndpointDialog = false
            },
            onDismiss = { showEndpointDialog = false }
        )
    }

    sheetMosque?.let { mosque ->
        MosqueDetailSheet(
            mosque = mosque,
            originLat = routeOriginLat,
            originLon = routeOriginLon,
            hasValidOrigin = hasEffectiveCenter,
            routeState = routeState,
            language = language,
            onFocusOnMap = {
                sheetMosque = null
                isMapView = true
                focusedMosque = mosque
                focusRequestCounter += 1
            },
            onRequestRoute = { profile ->
                scope.launch {
                    MosqueFinderRepository.fetchRouteToMosque(
                        originLat = routeOriginLat,
                        originLon = routeOriginLon,
                        mosque = mosque,
                        profile = profile
                    )
                    if (MosqueFinderRepository.routeState.value.route != null) {
                        sheetMosque = null
                        isMapView = true
                    }
                }
            },
            onClearRoute = { MosqueFinderRepository.clearRoute() },
            onDismiss = { sheetMosque = null }
        )
    }
}

@Composable
private fun FloatingPicture3MosqueCard(
    mosque: MosqueItem?,
    mosqueIndex: Int,
    totalMosques: Int,
    routeState: MosqueRouteState,
    isRouteHudVisible: Boolean,
    hasValidOrigin: Boolean,
    language: AppLanguage,
    onPreviousMosque: () -> Unit,
    onNextMosque: () -> Unit,
    onOpenDetails: () -> Unit,
    onNavigate: (OSRMRouteProfile) -> Unit,
    onClearRoute: () -> Unit
) {
    val activeRoute = routeState.route
    val cardTitle = mosque?.name ?: routeState.targetMosqueName ?: AppLocalization.get("mosque_route_section_title", language)
    val baseModifier = if (isRouteHudVisible) {
        Modifier.fillMaxWidth().testTag("mosque_active_route_hud")
    } else {
        Modifier.fillMaxWidth().testTag("mosque_bottom_preview_card")
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 10.dp,
        modifier = baseModifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Mosque arch preview badge (tappable to open full OSM metadata sheet)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE6F4EE))
                        .clickable(onClick = onOpenDetails)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mosque,
                        contentDescription = null,
                        tint = EmeraldAction,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onOpenDetails)
                ) {
                    Text(
                        text = cardTitle,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val shownDistance = activeRoute?.distanceMeters ?: mosque?.distanceMeters
                        if (shownDistance != null) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF334155),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = MosqueFinderRepository.formatStraightLineDistance(shownDistance),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color(0xFF0F172A)
                            )
                        }
                        if (activeRoute?.durationSeconds != null) {
                            val profileIcon = when (activeRoute.profile) {
                                OSRMRouteProfile.DRIVING -> Icons.Default.DirectionsCar
                                OSRMRouteProfile.WALKING -> Icons.Default.DirectionsWalk
                                OSRMRouteProfile.CYCLING -> Icons.Default.DirectionsBike
                            }
                            Icon(
                                imageVector = profileIcon,
                                contentDescription = null,
                                tint = Color(0xFF334155),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = OsrmRoutingService.formatDuration(activeRoute.durationSeconds),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color(0xFF0F172A)
                            )
                        }
                    }

                    val subtitle = when {
                        !activeRoute?.roadSummary.isNullOrBlank() -> "Via ${activeRoute?.roadSummary}"
                        !mosque?.address.isNullOrBlank() -> mosque?.address!!
                        mosque != null -> mosque.category.label(language)
                        else -> ""
                    }
                    if (subtitle.isNotBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (isRouteHudVisible) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onOpenDetails,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Directions,
                                contentDescription = AppLocalization.get("mosque_get_directions", language),
                                tint = EmeraldAction
                            )
                        }
                        IconButton(
                            onClick = onClearRoute,
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("mosque_hud_clear_route")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = AppLocalization.get("mosque_route_clear", language),
                                tint = Color(0xFF475569)
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = { onNavigate(routeState.profile) },
                        enabled = hasValidOrigin && mosque != null,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldAction,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        modifier = Modifier
                            .defaultMinSize(minHeight = 48.dp)
                            .testTag("mosque_card_navigate_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = AppLocalization.get("mosque_navigate_btn", language),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                    }
                }
            }

            if (routeState.isLoading) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp,
                        color = EmeraldAction
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppLocalization.get("mosque_route_loading", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF334155)
                    )
                }
            } else if (!routeState.errorMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = routeState.errorMessage,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            } else if (activeRoute != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    OSRMRouteProfile.entries.forEach { prof ->
                        val key = when (prof) {
                            OSRMRouteProfile.DRIVING -> "mosque_route_profile_driving"
                            OSRMRouteProfile.WALKING -> "mosque_route_profile_walking"
                            OSRMRouteProfile.CYCLING -> "mosque_route_profile_cycling"
                        }
                        FilterChip(
                            selected = routeState.profile == prof,
                            onClick = { onNavigate(prof) },
                            label = {
                                Text(
                                    text = AppLocalization.get(key, language),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = noorSelectableChipColors()
                        )
                    }
                }
            } else if (totalMosques > 1 && mosqueIndex >= 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    IconButton(
                        onClick = onPreviousMosque,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("mosque_prev_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = AppLocalization.get("mosque_prev_cd", language),
                            tint = EmeraldAction
                        )
                    }
                    Text(
                        text = "${mosqueIndex + 1} / $totalMosques",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF334155),
                        modifier = Modifier.testTag("mosque_pager_index")
                    )
                    IconButton(
                        onClick = onNextMosque,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("mosque_next_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = AppLocalization.get("mosque_next_cd", language),
                            tint = EmeraldAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingStatusOverlays(
    hasPermission: Boolean,
    isGpsEnabled: Boolean,
    poorAccuracyMeters: Int?,
    isFromCache: Boolean,
    isCacheStale: Boolean,
    cacheTimestampMs: Long?,
    activeEndpoint: String,
    errorMessage: String?,
    canRetry: Boolean,
    compact: Boolean,
    language: AppLanguage,
    onRequestPermission: () -> Unit,
    onOpenLocationSettings: () -> Unit,
    onRetry: () -> Unit
) {
    val hostName = remember(activeEndpoint) {
        runCatching { URI(activeEndpoint).host ?: activeEndpoint }.getOrDefault(activeEndpoint)
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (!hasPermission) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xF0FFFFFF),
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mosque_permission_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = if (compact) 4.dp else 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOff,
                        contentDescription = null,
                        tint = HeaderEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppLocalization.get("mosque_permission_hint", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF0F172A),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = onRequestPermission,
                        modifier = Modifier
                            .defaultMinSize(minHeight = 40.dp)
                            .testTag("mosque_grant_location_btn")
                    ) {
                        Text(
                            text = AppLocalization.get("grant_location", language),
                            color = EmeraldAction,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            if (!isGpsEnabled) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mosque_gps_disabled_banner")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppLocalization.get("mosque_gps_disabled_hint", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = onOpenLocationSettings,
                            modifier = Modifier.defaultMinSize(minHeight = 40.dp)
                        ) {
                            Text(AppLocalization.get("mosque_open_settings", language))
                        }
                    }
                }
            }

            if (poorAccuracyMeters != null) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mosque_accuracy_warning")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                AppLocalization.get("mosque_gps_accuracy_poor", language),
                                poorAccuracyMeters
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

        if (isFromCache && cacheTimestampMs != null) {
            val formattedTime = remember(cacheTimestampMs) {
                SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(cacheTimestampMs))
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xEAFFFFFF),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mosque_cache_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (isCacheStale) Icons.Default.Warning else Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isCacheStale) {
                            "${AppLocalization.get("mosque_cache_stale_notice", language)} ($formattedTime • $hostName)"
                        } else {
                            "${AppLocalization.get("mosque_cache_saved_notice", language)} ($formattedTime • $hostName)"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF334155),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        if (!errorMessage.isNullOrBlank()) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mosque_error_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (canRetry) {
                        TextButton(
                            onClick = onRetry,
                            modifier = Modifier.testTag("mosque_retry_btn")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppLocalization.get("mosque_retry", language))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationUnsetPromptCard(
    hasPermission: Boolean,
    isGpsEnabled: Boolean,
    language: AppLanguage,
    onRequestPermission: () -> Unit,
    onOpenLocationSettings: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mosque_location_unset_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = AppLocalization.get("mosque_location_unset_title", language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = AppLocalization.get("mosque_location_unset_desc", language),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF475569)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (!hasPermission) {
                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldAction),
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppLocalization.get("grant_location", language))
                }
            } else if (!isGpsEnabled) {
                OutlinedButton(
                    onClick = onOpenLocationSettings,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                ) {
                    Text(AppLocalization.get("mosque_open_settings", language))
                }
            }
        }
    }
}

@Composable
private fun MosqueListItemCard(
    mosque: MosqueItem,
    isSelected: Boolean = false,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .testTag("mosque_card_${mosque.osmId}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (mosque.verificationLevel == OsmVerificationLevel.BUILDING_MOSQUE_CANDIDATE) {
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f)
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        },
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Mosque,
                    contentDescription = mosque.name,
                    tint = if (mosque.verificationLevel == OsmVerificationLevel.BUILDING_MOSQUE_CANDIDATE) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mosque.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = mosque.category.label(language),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                if (mosque.verificationLevel == OsmVerificationLevel.BUILDING_MOSQUE_CANDIDATE) {
                    Text(
                        text = mosque.verificationLevel.label(language),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                if (!mosque.address.isNullOrBlank()) {
                    Text(
                        text = mosque.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = MosqueFinderRepository.formatStraightLineDistance(mosque.distanceMeters),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = AppLocalization.get("mosque_straight_line_short", language),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyMosqueStateCard(language: AppLanguage) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mosque_empty_state")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = AppLocalization.get("mosque_empty_title", language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = AppLocalization.get("mosque_empty_desc", language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OsmCoverageFooterCard(language: AppLanguage) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mosque_coverage_notice")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = AppLocalization.get("mosque_coverage_title", language),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = AppLocalization.get("mosque_coverage_body", language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "© OpenStreetMap contributors — openstreetmap.org/copyright",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable {
                        runCatching {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.openstreetmap.org/copyright"))
                            )
                        }
                    }
                    .padding(vertical = 4.dp)
                    .testTag("mosque_footer_osm_attribution")
            )
        }
    }
}

@Composable
private fun OverpassEndpointDialog(
    currentPreferred: String?,
    activeEndpoint: String,
    language: AppLanguage,
    onSelectEndpoint: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(AppLocalization.get("mosque_endpoint_settings_title", language)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = AppLocalization.get("mosque_endpoint_rate_note", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectEndpoint(null) }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(selected = currentPreferred == null, onClick = { onSelectEndpoint(null) })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppLocalization.get("mosque_endpoint_auto", language),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                OverpassMosqueService.DEFAULT_ENDPOINTS.forEach { url ->
                    val host = runCatching { URI(url).host ?: url }.getOrDefault(url)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectEndpoint(url) }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = currentPreferred == url, onClick = { onSelectEndpoint(url) })
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = host, style = MaterialTheme.typography.bodyMedium)
                            if (activeEndpoint == url) {
                                Text(
                                    text = AppLocalization.get("mosque_live_data_badge", language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(AppLocalization.get("close", language).ifBlank { "Close" })
            }
        }
    )
}
