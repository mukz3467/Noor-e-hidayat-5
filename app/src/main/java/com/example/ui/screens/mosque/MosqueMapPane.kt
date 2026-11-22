package com.example.ui.screens.mosque

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.model.AppLanguage
import com.example.data.mosque.MapBoundingBox
import com.example.data.mosque.MapStylePreset
import com.example.data.mosque.MosqueFinderRepository
import com.example.data.mosque.MosqueItem
import com.example.data.mosque.MosqueRouteResult
import com.example.data.mosque.OsmVerificationLevel
import com.example.data.mosque.OverpassMosqueService
import com.example.localization.AppLocalization
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapLibreMapOptions
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.FillExtrusionLayer
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point
import org.maplibre.geojson.Polygon
import kotlin.math.*

private const val VERIFIED_SOURCE_ID = "noor-mosques-verified-source"
private const val VERIFIED_LAYER_ID = "noor-mosques-verified-layer"
private const val CANDIDATE_SOURCE_ID = "noor-mosques-candidate-source"
private const val CANDIDATE_LAYER_ID = "noor-mosques-candidate-layer"
private const val SELECTED_SOURCE_ID = "noor-mosque-selected-source"
private const val SELECTED_HALO_LAYER_ID = "noor-mosque-selected-halo-layer"
private const val SELECTED_PIN_LAYER_ID = "noor-mosque-selected-pin-layer"
private const val SELECTED_LABEL_LAYER_ID = "noor-mosque-selected-label-layer"
private const val FOOTPRINT_SOURCE_ID = "noor-mosque-footprint-source"
private const val FOOTPRINT_FILL_LAYER_ID = "noor-mosque-footprint-fill-layer"
private const val FOOTPRINT_LINE_LAYER_ID = "noor-mosque-footprint-line-layer"
private const val FOOTPRINT_EXTRUSION_LAYER_ID = "noor-mosque-footprint-3d-layer"
private const val ROUTE_SOURCE_ID = "noor-osrm-route-source"
private const val ROUTE_CASING_LAYER_ID = "noor-osrm-route-casing-layer"
private const val ROUTE_LINE_LAYER_ID = "noor-osrm-route-line-layer"
private const val USER_SOURCE_ID = "noor-user-location-source"
private const val USER_ACCURACY_SOURCE_ID = "noor-user-accuracy-source"
private const val USER_ACCURACY_FILL_LAYER_ID = "noor-user-accuracy-fill-layer"
private const val USER_ACCURACY_LINE_LAYER_ID = "noor-user-accuracy-line-layer"
private const val USER_HALO_LAYER_ID = "noor-user-location-halo-layer"
private const val USER_LAYER_ID = "noor-user-location-layer"
private const val VERIFIED_ICON_ID = "noor-mosque-verified-icon"
private const val CANDIDATE_ICON_ID = "noor-mosque-candidate-icon"
private const val SELECTED_ICON_ID = "noor-mosque-selected-icon"
private const val NOOR_BUILDING_3D_LAYER_ID = "noor-building-3d-extrusion"
private const val BUILTIN_LIBERTY_3D_LAYER_ID = "building-3d"
private const val PROP_OSM_KEY = "osm_id_key"
private const val PROP_SELECTED_LABEL = "selected_label"

internal object MapLibreRuntimeHelper {
    @Volatile
    private var initialized = false

    fun canRenderNativeMap(context: Context): Boolean {
        if (Build.FINGERPRINT.equals("robolectric", ignoreCase = true)) {
            return false
        }
        return runCatching {
            if (!initialized) {
                MapLibre.getInstance(context.applicationContext)
                initialized = true
            }
            true
        }.getOrDefault(false)
    }

    fun buildPinBitmap(fillColorInt: Int, borderColorInt: Int, isCandidate: Boolean): Bitmap {
        // 2x HiDPI supersampled bitmap (rendered at iconSize = 0.56f) for razor-sharp pins on xxhdpi/xxxhdpi screens
        val sizePx = 128
        val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val cx = sizePx / 2f
        val cy = sizePx / 2f - 6f
        val radius = 38f

        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(90, 0, 0, 0)
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx, cy + 5f, radius + 4.5f, shadowPaint)

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = borderColorInt
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx, cy, radius + 6f, borderPaint)

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fillColorInt
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx, cy, radius, fillPaint)

        val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            style = if (isCandidate) Paint.Style.STROKE else Paint.Style.FILL
            strokeWidth = 5f
        }
        val archRect = RectF(cx - 14f, cy - 7f, cx + 14f, cy + 16f)
        canvas.drawRoundRect(archRect, 14f, 14f, innerPaint)
        canvas.drawCircle(cx, cy - 15f, 4.5f, innerPaint)
        return bmp
    }

    /**
     * 2x HiDPI crimson teardrop marker with white border & mosque dome silhouette (rendered at iconSize = 0.58f).
     */
    fun buildSelectedTeardropBitmap(): Bitmap {
        val width = 160
        val height = 196
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val cx = width / 2f
        val circleCy = 66f
        val radius = 52f
        val tipY = height - 14f

        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.argb(85, 0, 0, 0)
            style = Paint.Style.FILL
        }
        canvas.drawOval(RectF(cx - 28f, tipY - 8f, cx + 28f, tipY + 7f), shadowPaint)

        val pinPath = Path().apply {
            addCircle(cx, circleCy, radius + 6f, Path.Direction.CW)
            moveTo(cx - radius * 0.72f, circleCy + radius * 0.65f)
            lineTo(cx, tipY)
            lineTo(cx + radius * 0.72f, circleCy + radius * 0.65f)
            close()
        }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawPath(pinPath, borderPaint)

        val innerPath = Path().apply {
            addCircle(cx, circleCy, radius, Path.Direction.CW)
            moveTo(cx - radius * 0.62f, circleCy + radius * 0.60f)
            lineTo(cx, tipY - 9f)
            lineTo(cx + radius * 0.62f, circleCy + radius * 0.60f)
            close()
        }
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#DC2626")
            style = Paint.Style.FILL
        }
        canvas.drawPath(innerPath, fillPaint)

        val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRect(cx - 19f, circleCy - 2f, cx + 19f, circleCy + 19f, whitePaint)
        canvas.drawArc(RectF(cx - 14f, circleCy - 21f, cx + 14f, circleCy + 7f), 180f, 180f, true, whitePaint)
        canvas.drawRect(cx - 24.5f, circleCy - 19f, cx - 18.5f, circleCy + 19f, whitePaint)
        canvas.drawRect(cx + 18.5f, circleCy - 19f, cx + 24.5f, circleCy + 19f, whitePaint)
        canvas.drawCircle(cx, circleCy - 25f, 3.5f, whitePaint)
        return bmp
    }

    /**
     * Builds a real-scale geographic circle polygon representing the live GPS accuracy radius in meters.
     */
    fun buildAccuracyCircleRing(
        centerLat: Double,
        centerLon: Double,
        accuracyMeters: Float,
        segments: Int = 48
    ): List<Point> {
        val clampedMeters = accuracyMeters.toDouble().coerceIn(2.0, 500.0)
        val earthRadiusMeters = 6_371_000.0
        val latRad = Math.toRadians(centerLat)
        val cosLat = cos(latRad).coerceAtLeast(0.01)
        val dLat = Math.toDegrees(clampedMeters / earthRadiusMeters)
        val dLon = Math.toDegrees(clampedMeters / (earthRadiusMeters * cosLat))
        val ring = ArrayList<Point>(segments + 1)
        for (i in 0..segments) {
            val theta = 2.0 * PI * i / segments
            val pLat = (centerLat + dLat * sin(theta)).coerceIn(-85.0, 85.0)
            val pLon = (centerLon + dLon * cos(theta)).coerceIn(-179.99, 179.99)
            ring.add(Point.fromLngLat(pLon, pLat))
        }
        return ring
    }

    fun computeVisibleBoundingBox(centerLat: Double, centerLon: Double, zoom: Double, widthPx: Int, heightPx: Int): MapBoundingBox {
        val w = widthPx.coerceAtLeast(360)
        val h = heightPx.coerceAtLeast(360)
        val worldSize = 256.0 * 2.0.pow(zoom)
        val cx = (centerLon + 180.0) / 360.0 * worldSize
        val rad = Math.toRadians(centerLat.coerceIn(-85.0511, 85.0511))
        val cy = (1.0 - ln(tan(rad) + 1.0 / cos(rad)) / PI) / 2.0 * worldSize

        val west = ((cx - w / 2.0) / worldSize * 360.0 - 180.0).coerceIn(-179.9, 179.9)
        val east = ((cx + w / 2.0) / worldSize * 360.0 - 180.0).coerceIn(-179.9, 179.9)
        val northFrac = (cy - h / 2.0) / worldSize
        val southFrac = (cy + h / 2.0) / worldSize
        val north = Math.toDegrees(atan(sinh(PI * (1.0 - 2.0 * northFrac)))).coerceIn(-85.0, 85.0)
        val south = Math.toDegrees(atan(sinh(PI * (1.0 - 2.0 * southFrac)))).coerceIn(-85.0, 85.0)
        return MapBoundingBox(
            south = min(south, north),
            west = min(west, east),
            north = max(south, north),
            east = max(west, east)
        )
    }

    fun zoomForRadiusMeters(radiusMeters: Int): Double = when {
        radiusMeters <= 1000 -> 14.5
        radiusMeters <= 3000 -> 13.2
        radiusMeters <= 5000 -> 12.4
        else -> 11.4
    }
}

@Composable
fun MosqueMapPane(
    mosques: List<MosqueItem>,
    centerLat: Double,
    centerLon: Double,
    hasConfirmedCenter: Boolean,
    radiusMeters: Int = 5000,
    userLat: Double,
    userLon: Double,
    hasUserCoords: Boolean,
    userAccuracyMeters: Float? = null,
    language: AppLanguage,
    selectedMosque: MosqueItem? = null,
    selectedFootprint: List<Pair<Double, Double>> = emptyList(),
    route: MosqueRouteResult? = null,
    is3dEnabled: Boolean = true,
    stylePreset: MapStylePreset = MapStylePreset.HYBRID_3D,
    onSelectStylePreset: (MapStylePreset) -> Unit = {},
    isSearchingOverpass: Boolean = false,
    focusRequestCounter: Int = 0,
    bottomControlsPadding: Dp = 44.dp,
    onSelectMosque: (MosqueItem) -> Unit,
    onSearchVisibleBounds: (MapBoundingBox) -> Unit,
    onRecenter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val useNativeMap = remember(context) { MapLibreRuntimeHelper.canRenderNativeMap(context) }

    val primaryArgb = android.graphics.Color.parseColor("#0B6E4F")
    val candidateArgb = android.graphics.Color.parseColor("#D97706")
    val whiteArgb = android.graphics.Color.WHITE

    val initialLat = if (hasConfirmedCenter) centerLat else 21.4225
    val initialLon = if (hasConfirmedCenter) centerLon else 39.8262
    val radiusZoom = remember(radiusMeters) { MapLibreRuntimeHelper.zoomForRadiusMeters(radiusMeters) }
    val initialZoom = if (hasConfirmedCenter) radiusZoom else 2.2
    val effective3dActive = is3dEnabled && stylePreset != MapStylePreset.SATELLITE_HD
    val initialTilt = if (hasConfirmedCenter && effective3dActive) 48.0 else 0.0

    var mapLibreRef by remember { mutableStateOf<MapLibreMap?>(null) }
    var loadedStyleRef by remember { mutableStateOf<Style?>(null) }
    var loadedStyleUri by remember { mutableStateOf<String?>(null) }
    var requestedStyleUri by remember { mutableStateOf<String?>(null) }
    var tileLoadFailed by remember { mutableStateOf(false) }
    var isStyleLoading by remember { mutableStateOf(useNativeMap) }
    var lastVisibleSearchMs by remember { mutableLongStateOf(-1L) }

    val latestMosques by rememberUpdatedState(mosques)
    val latestOnSelect by rememberUpdatedState(onSelectMosque)
    val latestSelectedMosque by rememberUpdatedState(selectedMosque)
    val latestFootprint by rememberUpdatedState(selectedFootprint)
    val latestRoute by rememberUpdatedState(route)
    val latestIs3d by rememberUpdatedState(effective3dActive)
    val latestStylePreset by rememberUpdatedState(stylePreset)
    val latestUserAccuracy by rememberUpdatedState(userAccuracyMeters)

    // Headless JVM / Robolectric fallback state
    var fallbackZoom by remember(hasConfirmedCenter, radiusMeters) { mutableDoubleStateOf(initialZoom) }
    var fallbackLat by remember(initialLat, initialLon) { mutableDoubleStateOf(initialLat) }
    var fallbackLon by remember(initialLat, initialLon) { mutableDoubleStateOf(initialLon) }
    var fallbackTilt by remember(effective3dActive) { mutableDoubleStateOf(if (effective3dActive) 40.0 else 0.0) }
    var fallbackBearing by remember { mutableDoubleStateOf(0.0) }

    // Synchronize camera when search center, radius, or returned mosque set changes so all nearby pins are framed
    LaunchedEffect(centerLat, centerLon, radiusMeters, mosques.size, hasConfirmedCenter, mapLibreRef) {
        if (!hasConfirmedCenter) return@LaunchedEffect
        val map = mapLibreRef ?: run {
            fallbackLat = centerLat
            fallbackLon = centerLon
            fallbackZoom = MapLibreRuntimeHelper.zoomForRadiusMeters(radiusMeters)
            return@LaunchedEffect
        }
        if (latestRoute != null) return@LaunchedEffect

        if (mosques.size >= 2) {
            val points = buildList {
                add(LatLng(centerLat, centerLon))
                mosques.take(25).forEach { m -> add(LatLng(m.latitude, m.longitude)) }
            }
            val bounds = runCatching { LatLngBounds.Builder().includes(points).build() }.getOrNull()
            val camPos = bounds?.let { map.getCameraForLatLngBounds(it, intArrayOf(90, 150, 90, 220)) }
            if (camPos != null) {
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder(camPos)
                            .zoom(camPos.zoom.coerceIn(10.0, 15.2))
                            .tilt(if (latestIs3d) 42.0 else 0.0)
                            .build()
                    ),
                    500
                )
                return@LaunchedEffect
            }
        }

        val targetZoom = MapLibreRuntimeHelper.zoomForRadiusMeters(radiusMeters)
        val targetTilt = if (latestIs3d) 48.0 else 0.0
        map.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(centerLat, centerLon))
                    .zoom(targetZoom)
                    .tilt(targetTilt)
                    .build()
            ),
            450
        )
    }

    // Focus camera directly onto a selected mosque when user clicks a card/chip/pin
    LaunchedEffect(focusRequestCounter) {
        if (focusRequestCounter <= 0) return@LaunchedEffect
        val m = latestSelectedMosque ?: return@LaunchedEffect
        val map = mapLibreRef ?: run {
            fallbackLat = m.latitude
            fallbackLon = m.longitude
            fallbackZoom = 16.0
            return@LaunchedEffect
        }
        map.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(m.latitude, m.longitude))
                    .zoom(max(map.cameraPosition.zoom, 17.5).coerceAtMost(latestStylePreset.maxCameraZoom))
                    .tilt(if (latestIs3d) 50.0 else 0.0)
                    .build()
            ),
            550
        )
    }

    // Fit camera bounds smoothly when an OSRM road route loads
    LaunchedEffect(route, mapLibreRef) {
        val activeRoute = route ?: return@LaunchedEffect
        if (activeRoute.coordinates.size < 2) return@LaunchedEffect
        val map = mapLibreRef ?: return@LaunchedEffect
        val latLngs = activeRoute.coordinates.map { (lat, lon) -> LatLng(lat, lon) }
        val bounds = runCatching {
            LatLngBounds.Builder().includes(latLngs).build()
        }.getOrNull() ?: return@LaunchedEffect
        val camPos = map.getCameraForLatLngBounds(bounds, intArrayOf(90, 150, 90, 260))
        if (camPos != null) {
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder(camPos)
                        .zoom(camPos.zoom.coerceAtMost(latestStylePreset.maxCameraZoom))
                        .tilt(if (latestIs3d) 38.0 else 0.0)
                        .build()
                ),
                600
            )
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100), 600)
        }
    }

    // Apply Hybrid HD / HD Satellite / Liberty 3D / Bright style preset changes cleanly & clamp maxZoom
    LaunchedEffect(stylePreset, mapLibreRef) {
        val map = mapLibreRef ?: return@LaunchedEffect
        map.setMaxZoomPreference(stylePreset.maxCameraZoom)
        if (map.cameraPosition.zoom > stylePreset.maxCameraZoom) {
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder(map.cameraPosition)
                        .zoom(stylePreset.maxCameraZoom)
                        .build()
                ),
                250
            )
        }
        if (loadedStyleUri == stylePreset.styleUrl || requestedStyleUri == stylePreset.styleUrl) return@LaunchedEffect
        requestedStyleUri = stylePreset.styleUrl
        isStyleLoading = true
        tileLoadFailed = false
        map.setStyle(Style.Builder().fromUri(stylePreset.styleUrl)) { style ->
            isStyleLoading = false
            loadedStyleUri = stylePreset.styleUrl
            setupMosqueMapStyle(
                style = style,
                primaryArgb = primaryArgb,
                candidateArgb = candidateArgb,
                whiteArgb = whiteArgb,
                is3dEnabled = latestIs3d,
                allowBuilding3dInjection = stylePreset != MapStylePreset.SATELLITE_HD
            )
            loadedStyleRef = style
            syncMapLibreSources(
                style = style,
                mosques = latestMosques,
                selectedMosque = latestSelectedMosque,
                selectedFootprint = latestFootprint,
                route = latestRoute,
                userLat = userLat,
                userLon = userLon,
                hasUserCoords = hasUserCoords,
                userAccuracyMeters = latestUserAccuracy
            )
        }
    }

    // Apply 2D <-> 3D building extrusion visibility & crisp optical camera pitch
    LaunchedEffect(effective3dActive, loadedStyleRef, mapLibreRef) {
        val style = loadedStyleRef
        if (style != null) {
            configureBuilding3dLayer(
                style = style,
                is3dEnabled = effective3dActive,
                allowBuilding3dInjection = latestStylePreset != MapStylePreset.SATELLITE_HD
            )
        }
        val map = mapLibreRef ?: run {
            fallbackTilt = if (effective3dActive) 50.0 else 0.0
            return@LaunchedEffect
        }
        val current = map.cameraPosition
        val targetTilt = if (effective3dActive) 50.0 else 0.0
        val targetZoom = if (effective3dActive && hasConfirmedCenter) {
            max(current.zoom, 15.5).coerceAtMost(latestStylePreset.maxCameraZoom)
        } else {
            current.zoom.coerceAtMost(latestStylePreset.maxCameraZoom)
        }
        if (abs(current.tilt - targetTilt) > 2.0 || abs(current.zoom - targetZoom) > 0.2) {
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder(current)
                        .tilt(targetTilt)
                        .zoom(targetZoom)
                        .build()
                ),
                400
            )
        }
    }

    // Sync GeoJSON sources (OSM mosques, selected red pin + callout/footprint, OSRM blue route, GPS user point + accuracy ring)
    LaunchedEffect(mosques, selectedMosque, selectedFootprint, route, userLat, userLon, hasUserCoords, userAccuracyMeters, loadedStyleRef) {
        val style = loadedStyleRef ?: return@LaunchedEffect
        syncMapLibreSources(
            style = style,
            mosques = mosques,
            selectedMosque = selectedMosque,
            selectedFootprint = selectedFootprint,
            route = route,
            userLat = userLat,
            userLon = userLon,
            hasUserCoords = hasUserCoords,
            userAccuracyMeters = userAccuracyMeters
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1D3329))
            .testTag("mosque_map_canvas_container")
    ) {
        val isCompactMap = maxHeight < 420.dp
        if (useNativeMap) {
            val mapView = remember {
                // TextureView + full native display density prevents SurfaceView black frames on Compose zoom
                // and renders vectors, pins, and tiles at native xxhdpi/xxxhdpi sharpness.
                val nativePixelRatio = context.resources.displayMetrics.density.coerceIn(1.5f, 4.0f)
                val mapOptions = MapLibreMapOptions.createFromAttributes(context)
                    .textureMode(true)
                    .pixelRatio(nativePixelRatio)
                MapView(context, mapOptions).apply {
                    onCreate(null)
                }
            }

            DisposableEffect(lifecycleOwner, mapView) {
                val initialState = lifecycleOwner.lifecycle.currentState
                if (initialState.isAtLeast(Lifecycle.State.STARTED)) {
                    runCatching { mapView.onStart() }
                }
                if (initialState.isAtLeast(Lifecycle.State.RESUMED)) {
                    runCatching { mapView.onResume() }
                }
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_START -> runCatching { mapView.onStart() }
                        Lifecycle.Event.ON_RESUME -> runCatching { mapView.onResume() }
                        Lifecycle.Event.ON_PAUSE -> runCatching { mapView.onPause() }
                        Lifecycle.Event.ON_STOP -> runCatching { mapView.onStop() }
                        else -> Unit
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                    runCatching { mapView.onPause() }
                    runCatching { mapView.onStop() }
                    runCatching { mapView.onDestroy() }
                }
            }

            AndroidView(
                factory = {
                    mapView.addOnDidFailLoadingMapListener {
                        isStyleLoading = false
                        if (loadedStyleRef == null) {
                            requestedStyleUri = null
                            tileLoadFailed = true
                        }
                    }
                    mapView.addOnDidFinishLoadingStyleListener {
                        isStyleLoading = false
                        tileLoadFailed = false
                    }
                    mapView.getMapAsync { map ->
                        mapLibreRef = map
                        map.uiSettings.isAttributionEnabled = false
                        map.uiSettings.isLogoEnabled = false
                        map.uiSettings.isCompassEnabled = false
                        map.uiSettings.isRotateGesturesEnabled = true
                        map.uiSettings.isTiltGesturesEnabled = true
                        map.uiSettings.isZoomGesturesEnabled = true
                        map.uiSettings.isScrollGesturesEnabled = true
                        map.setMaxPitchPreference(60.0)
                        map.setMinZoomPreference(2.0)
                        map.setMaxZoomPreference(latestStylePreset.maxCameraZoom)
                        map.setPrefetchZoomDelta(4)

                        map.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(initialLat, initialLon))
                            .zoom(initialZoom.coerceAtMost(latestStylePreset.maxCameraZoom))
                            .tilt(initialTilt)
                            .build()

                        map.addOnMapClickListener { clickedLatLng ->
                            val screenPoint = map.projection.toScreenLocation(clickedLatLng)
                            val hitRect = RectF(
                                screenPoint.x - 36f,
                                screenPoint.y - 36f,
                                screenPoint.x + 36f,
                                screenPoint.y + 36f
                            )
                            val features = map.queryRenderedFeatures(
                                hitRect,
                                SELECTED_PIN_LAYER_ID,
                                VERIFIED_LAYER_ID,
                                CANDIDATE_LAYER_ID,
                                FOOTPRINT_EXTRUSION_LAYER_ID,
                                FOOTPRINT_FILL_LAYER_ID
                            )
                            val hit = features.firstOrNull()
                            val osmKey = hit?.getStringProperty(PROP_OSM_KEY)
                            val matched = osmKey?.let { key -> latestMosques.firstOrNull { it.id == key } }
                            if (matched != null) {
                                latestOnSelect(matched)
                                true
                            } else {
                                false
                            }
                        }

                        requestedStyleUri = latestStylePreset.styleUrl
                        isStyleLoading = true
                        map.setStyle(Style.Builder().fromUri(latestStylePreset.styleUrl)) { style ->
                            isStyleLoading = false
                            tileLoadFailed = false
                            loadedStyleUri = latestStylePreset.styleUrl
                            setupMosqueMapStyle(
                                style = style,
                                primaryArgb = primaryArgb,
                                candidateArgb = candidateArgb,
                                whiteArgb = whiteArgb,
                                is3dEnabled = latestIs3d,
                                allowBuilding3dInjection = latestStylePreset != MapStylePreset.SATELLITE_HD
                            )
                            loadedStyleRef = style
                            syncMapLibreSources(
                                style = style,
                                mosques = latestMosques,
                                selectedMosque = latestSelectedMosque,
                                selectedFootprint = latestFootprint,
                                route = latestRoute,
                                userLat = userLat,
                                userLon = userLon,
                                hasUserCoords = hasUserCoords,
                                userAccuracyMeters = latestUserAccuracy
                            )
                        }
                    }
                    mapView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            RobolectricMapProjectionHost(
                mosques = mosques,
                selectedMosque = selectedMosque,
                route = route,
                centerLat = fallbackLat,
                centerLon = fallbackLon,
                zoom = (fallbackZoom.toInt() - if (isCompactMap) 1 else 0).coerceIn(2, 18),
                userLat = userLat,
                userLon = userLon,
                hasUserCoords = hasUserCoords,
                onSelectMosque = onSelectMosque
            )
        }

        // Top-Start / Top-Center translucent controls: Search Visible Area + Style Switcher + Fast Loader
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp, start = 10.dp, end = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val now = System.currentTimeMillis()
                        if (lastVisibleSearchMs >= 0L && (now - lastVisibleSearchMs) < 1200L) return@Button
                        lastVisibleSearchMs = now

                        val map = mapLibreRef
                        val bounds = if (map != null) {
                            val b = map.projection.visibleRegion.latLngBounds
                            val latSpan = abs(b.latitudeNorth - b.latitudeSouth)
                            val lonSpan = abs(b.longitudeEast - b.longitudeWest)
                            if (latSpan > 0.35 || lonSpan > 0.35) {
                                val target = map.cameraPosition.target
                                val cLat = target?.latitude ?: initialLat
                                val cLon = target?.longitude ?: initialLon
                                OverpassMosqueService.boundingBoxFromRadius(cLat, cLon, 5000)
                            } else {
                                MapBoundingBox(
                                    south = b.latitudeSouth,
                                    west = b.longitudeWest,
                                    north = b.latitudeNorth,
                                    east = b.longitudeEast
                                )
                            }
                        } else {
                            MapLibreRuntimeHelper.computeVisibleBoundingBox(
                                centerLat = fallbackLat,
                                centerLon = fallbackLon,
                                zoom = fallbackZoom.coerceAtLeast(12.0),
                                widthPx = 800,
                                heightPx = 800
                            )
                        }
                        onSearchVisibleBounds(bounds)
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xEA0B4F30),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("mosque_search_visible_area_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppLocalization.get("mosque_search_visible_area", language),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Style Preset Switcher (Hybrid HD -> HD Satellite -> Liberty 3D Vector -> Bright Vector)
                Surface(
                    onClick = {
                        val next = when (stylePreset) {
                            MapStylePreset.HYBRID_3D -> MapStylePreset.SATELLITE_HD
                            MapStylePreset.SATELLITE_HD -> MapStylePreset.LIBERTY
                            MapStylePreset.LIBERTY -> MapStylePreset.BRIGHT
                            MapStylePreset.BRIGHT -> MapStylePreset.HYBRID_3D
                        }
                        onSelectStylePreset(next)
                    },
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xEBFFFFFF),
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("mosque_map_style_toggle")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = AppLocalization.get("mosque_switch_style_cd", language),
                            tint = Color(0xFF0B4F30),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        val labelKey = when (stylePreset) {
                            MapStylePreset.HYBRID_3D -> "mosque_style_hybrid"
                            MapStylePreset.SATELLITE_HD -> "mosque_style_satellite_hd"
                            MapStylePreset.LIBERTY -> "mosque_style_liberty"
                            MapStylePreset.BRIGHT -> "mosque_style_bright"
                        }
                        Text(
                            text = AppLocalization.get(labelKey, language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF0F172A),
                            maxLines = 1
                        )
                    }
                }
            }

            if (isSearchingOverpass || isStyleLoading) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xDE0F172A),
                    tonalElevation = 4.dp,
                    modifier = Modifier.testTag("mosque_map_loading_indicator")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF34D399)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppLocalization.get("mosque_searching", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }

            if (tileLoadFailed) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    tonalElevation = 2.dp,
                    modifier = Modifier.testTag("mosque_map_tile_error_banner")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppLocalization.get("mosque_tiles_error", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                tileLoadFailed = false
                                requestedStyleUri = stylePreset.styleUrl
                                isStyleLoading = true
                                mapLibreRef?.setStyle(Style.Builder().fromUri(stylePreset.styleUrl)) { style ->
                                    isStyleLoading = false
                                    loadedStyleUri = stylePreset.styleUrl
                                    setupMosqueMapStyle(
                                        style = style,
                                        primaryArgb = primaryArgb,
                                        candidateArgb = candidateArgb,
                                        whiteArgb = whiteArgb,
                                        is3dEnabled = latestIs3d,
                                        allowBuilding3dInjection = stylePreset != MapStylePreset.SATELLITE_HD
                                    )
                                    loadedStyleRef = style
                                    syncMapLibreSources(
                                        style = style,
                                        mosques = latestMosques,
                                        selectedMosque = latestSelectedMosque,
                                        selectedFootprint = latestFootprint,
                                        route = latestRoute,
                                        userLat = userLat,
                                        userLon = userLon,
                                        hasUserCoords = hasUserCoords,
                                        userAccuracyMeters = latestUserAccuracy
                                    )
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = AppLocalization.get("mosque_retry", language),
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Right-side crisp white floating cards (+ / - / Compass Arrow + Tilt / Recenter) matching Picture 3
        val onToggleTiltAction: () -> Unit = {
            val map = mapLibreRef
            if (map != null) {
                val cur = map.cameraPosition
                val nextTilt = when {
                    cur.tilt < 15.0 -> 42.0
                    cur.tilt < 48.0 -> 55.0
                    else -> 0.0
                }
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder(cur)
                            .tilt(nextTilt)
                            .zoom(
                                if (nextTilt > 0.0) {
                                    max(cur.zoom, 16.0).coerceAtMost(latestStylePreset.maxCameraZoom)
                                } else {
                                    cur.zoom.coerceAtMost(latestStylePreset.maxCameraZoom)
                                }
                            )
                            .build()
                    )
                )
            } else {
                fallbackTilt = if (fallbackTilt < 30.0) 48.0 else 0.0
            }
        }
        val onResetNorthAction: () -> Unit = {
            val map = mapLibreRef
            if (map != null) {
                val cur = map.cameraPosition
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder(cur)
                            .bearing(0.0)
                            .build()
                    )
                )
            } else {
                fallbackBearing = 0.0
            }
        }
        val onZoomInAction: () -> Unit = {
            val map = mapLibreRef
            if (map != null) {
                val cur = map.cameraPosition
                val nextZoom = (cur.zoom + 1.0).coerceAtMost(latestStylePreset.maxCameraZoom)
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder(cur)
                            .zoom(nextZoom)
                            .build()
                    ),
                    250
                )
            } else {
                fallbackZoom = (fallbackZoom + 1.0).coerceAtMost(latestStylePreset.maxCameraZoom)
            }
        }
        val onZoomOutAction: () -> Unit = {
            val map = mapLibreRef
            if (map != null) {
                val cur = map.cameraPosition
                val nextZoom = (cur.zoom - 1.0).coerceAtLeast(2.0)
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder(cur)
                            .zoom(nextZoom)
                            .build()
                    ),
                    250
                )
            } else {
                fallbackZoom = (fallbackZoom - 1.0).coerceAtLeast(2.0)
            }
        }
        val onRecenterAction: () -> Unit = {
            val map = mapLibreRef
            if (map != null && hasConfirmedCenter) {
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(LatLng(centerLat, centerLon))
                            .zoom(16.5.coerceAtMost(latestStylePreset.maxCameraZoom))
                            .tilt(if (latestIs3d) 48.0 else 0.0)
                            .build()
                    )
                )
            } else {
                fallbackLat = initialLat
                fallbackLon = initialLon
            }
            onRecenter()
        }

        if (isCompactMap) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = bottomControlsPadding)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    WhiteFloatingMapControlButton(
                        onClick = onToggleTiltAction,
                        icon = Icons.Default.ViewInAr,
                        contentDescription = AppLocalization.get("mosque_tilt_cd", language),
                        testTag = "mosque_map_tilt_btn"
                    )
                    WhiteFloatingMapControlButton(
                        onClick = onResetNorthAction,
                        icon = Icons.Default.NearMe,
                        contentDescription = AppLocalization.get("mosque_reset_north", language),
                        testTag = "mosque_map_north_btn"
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    WhiteFloatingMapControlButton(
                        onClick = onZoomInAction,
                        icon = Icons.Default.Add,
                        contentDescription = AppLocalization.get("mosque_zoom_in", language),
                        testTag = "mosque_map_zoom_in"
                    )
                    WhiteFloatingMapControlButton(
                        onClick = onZoomOutAction,
                        icon = Icons.Default.Remove,
                        contentDescription = AppLocalization.get("mosque_zoom_out", language),
                        testTag = "mosque_map_zoom_out"
                    )
                    WhiteFloatingMapControlButton(
                        onClick = onRecenterAction,
                        icon = Icons.Default.MyLocation,
                        contentDescription = AppLocalization.get("mosque_recenter", language),
                        testTag = "mosque_map_recenter"
                    )
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp, bottom = 28.dp)
            ) {
                WhiteFloatingMapControlButton(
                    onClick = onZoomInAction,
                    icon = Icons.Default.Add,
                    contentDescription = AppLocalization.get("mosque_zoom_in", language),
                    testTag = "mosque_map_zoom_in"
                )
                WhiteFloatingMapControlButton(
                    onClick = onZoomOutAction,
                    icon = Icons.Default.Remove,
                    contentDescription = AppLocalization.get("mosque_zoom_out", language),
                    testTag = "mosque_map_zoom_out"
                )
                WhiteFloatingMapControlButton(
                    onClick = onResetNorthAction,
                    icon = Icons.Default.NearMe,
                    contentDescription = AppLocalization.get("mosque_reset_north", language),
                    testTag = "mosque_map_north_btn"
                )
                WhiteFloatingMapControlButton(
                    onClick = onToggleTiltAction,
                    icon = Icons.Default.ViewInAr,
                    contentDescription = AppLocalization.get("mosque_tilt_cd", language),
                    testTag = "mosque_map_tilt_btn"
                )
                WhiteFloatingMapControlButton(
                    onClick = onRecenterAction,
                    icon = Icons.Default.MyLocation,
                    contentDescription = AppLocalization.get("mosque_recenter", language),
                    testTag = "mosque_map_recenter"
                )
            }
        }

        // Clickable attribution pill matching Picture 3 bottom-right dark badge
        Surface(
            onClick = {
                runCatching {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.openstreetmap.org/copyright"))
                    )
                }
            },
            shape = RoundedCornerShape(10.dp),
            color = Color(0xD91E293B),
            tonalElevation = 2.dp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .widthIn(max = 225.dp)
                .testTag("mosque_osm_attribution")
        ) {
            Text(
                text = "© OpenStreetMap contributors • OpenFreeMap • Esri",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun WhiteFloatingMapControlButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xF5FFFFFF),
        shadowElevation = 6.dp,
        modifier = Modifier
            .size(48.dp)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color(0xFF0F172A),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

private fun configureBuilding3dLayer(
    style: Style,
    is3dEnabled: Boolean,
    allowBuilding3dInjection: Boolean = true
) {
    // Include ALL mapped OSM building footprints (not just those with explicit OSM height tags)
    // so residential neighborhoods, markets, and mosques across every region render in real 3D.
    val allBuildingsFilter = Expression.neq(Expression.get("hide_3d"), Expression.literal(true))
    val effectiveVis = is3dEnabled && allowBuilding3dInjection
    val visProp = PropertyFactory.visibility(if (effectiveVis) Property.VISIBLE else Property.NONE)

    val footprint3d = style.getLayer(FOOTPRINT_EXTRUSION_LAYER_ID) as? FillExtrusionLayer
    footprint3d?.setProperties(visProp)

    val builtIn = style.getLayer(BUILTIN_LIBERTY_3D_LAYER_ID) as? FillExtrusionLayer
    if (builtIn != null) {
        builtIn.setFilter(allBuildingsFilter)
        builtIn.setProperties(visProp)
        return
    }

    val custom3d = style.getLayer(NOOR_BUILDING_3D_LAYER_ID) as? FillExtrusionLayer
    if (custom3d != null) {
        custom3d.setFilter(allBuildingsFilter)
        custom3d.setProperties(visProp)
        return
    }

    if (!allowBuilding3dInjection) return

    // Attach solid sunlit 3D extrusion layer below the first SymbolLayer for styles without built-in 3D
    val openMapTilesSource = style.getSource("openmaptiles") ?: return
    if (openMapTilesSource.id.isNotBlank()) {
        val extrusionLayer = FillExtrusionLayer(NOOR_BUILDING_3D_LAYER_ID, openMapTilesSource.id).apply {
            setSourceLayer("building")
            minZoom = 14.5f
            setFilter(allBuildingsFilter)
            setProperties(
                PropertyFactory.fillExtrusionHeight(
                    Expression.coalesce(
                        Expression.get("render_height"),
                        Expression.get("height"),
                        Expression.literal(8)
                    )
                ),
                PropertyFactory.fillExtrusionBase(
                    Expression.coalesce(
                        Expression.get("render_min_height"),
                        Expression.get("min_height"),
                        Expression.literal(0)
                    )
                ),
                PropertyFactory.fillExtrusionColor("#D9CEBC"),
                PropertyFactory.fillExtrusionOpacity(0.88f),
                PropertyFactory.fillExtrusionVerticalGradient(true),
                visProp
            )
        }
        val firstLabelLayer = style.layers.firstOrNull { it is SymbolLayer }
        if (firstLabelLayer != null) {
            style.addLayerBelow(extrusionLayer, firstLabelLayer.id)
        } else {
            style.addLayer(extrusionLayer)
        }
    }
}

private fun setupMosqueMapStyle(
    style: Style,
    primaryArgb: Int,
    candidateArgb: Int,
    whiteArgb: Int,
    is3dEnabled: Boolean,
    allowBuilding3dInjection: Boolean = true
) {
    configureBuilding3dLayer(style, is3dEnabled, allowBuilding3dInjection)

    if (style.getImage(VERIFIED_ICON_ID) == null) {
        style.addImage(
            VERIFIED_ICON_ID,
            MapLibreRuntimeHelper.buildPinBitmap(primaryArgb, whiteArgb, isCandidate = false)
        )
    }
    if (style.getImage(CANDIDATE_ICON_ID) == null) {
        style.addImage(
            CANDIDATE_ICON_ID,
            MapLibreRuntimeHelper.buildPinBitmap(candidateArgb, whiteArgb, isCandidate = true)
        )
    }
    if (style.getImage(SELECTED_ICON_ID) == null) {
        style.addImage(
            SELECTED_ICON_ID,
            MapLibreRuntimeHelper.buildSelectedTeardropBitmap()
        )
    }

    // Selected mosque building footprint polygon + real 3D emerald mosque extrusion
    if (style.getSource(FOOTPRINT_SOURCE_ID) == null) {
        style.addSource(GeoJsonSource(FOOTPRINT_SOURCE_ID, FeatureCollection.fromFeatures(emptyArray())))
    }
    if (style.getLayer(FOOTPRINT_FILL_LAYER_ID) == null) {
        style.addLayer(
            FillLayer(FOOTPRINT_FILL_LAYER_ID, FOOTPRINT_SOURCE_ID).withProperties(
                PropertyFactory.fillColor("#0B6E4F"),
                PropertyFactory.fillOpacity(0.28f)
            )
        )
    }
    if (style.getLayer(FOOTPRINT_EXTRUSION_LAYER_ID) == null) {
        val mosque3dVis = if (is3dEnabled && allowBuilding3dInjection) Property.VISIBLE else Property.NONE
        style.addLayer(
            FillExtrusionLayer(FOOTPRINT_EXTRUSION_LAYER_ID, FOOTPRINT_SOURCE_ID).withProperties(
                PropertyFactory.fillExtrusionColor("#0B6E4F"),
                PropertyFactory.fillExtrusionHeight(15f),
                PropertyFactory.fillExtrusionBase(0f),
                PropertyFactory.fillExtrusionOpacity(0.88f),
                PropertyFactory.fillExtrusionVerticalGradient(true),
                PropertyFactory.visibility(mosque3dVis)
            )
        )
    }
    if (style.getLayer(FOOTPRINT_LINE_LAYER_ID) == null) {
        style.addLayer(
            LineLayer(FOOTPRINT_LINE_LAYER_ID, FOOTPRINT_SOURCE_ID).withProperties(
                PropertyFactory.lineColor("#F59E0B"),
                PropertyFactory.lineWidth(2.6f)
            )
        )
    }

    // OSRM road route casing + bright blue line matching Picture 3
    if (style.getSource(ROUTE_SOURCE_ID) == null) {
        style.addSource(GeoJsonSource(ROUTE_SOURCE_ID, FeatureCollection.fromFeatures(emptyArray())))
    }
    if (style.getLayer(ROUTE_CASING_LAYER_ID) == null) {
        style.addLayer(
            LineLayer(ROUTE_CASING_LAYER_ID, ROUTE_SOURCE_ID).withProperties(
                PropertyFactory.lineColor("#FFFFFF"),
                PropertyFactory.lineWidth(9.5f),
                PropertyFactory.lineOpacity(0.95f),
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
            )
        )
    }
    if (style.getLayer(ROUTE_LINE_LAYER_ID) == null) {
        style.addLayer(
            LineLayer(ROUTE_LINE_LAYER_ID, ROUTE_SOURCE_ID).withProperties(
                PropertyFactory.lineColor("#1D63ED"),
                PropertyFactory.lineWidth(6.0f),
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
            )
        )
    }

    // Real-scale GPS accuracy radius ring (drawn from real FusedLocationProviderClient accuracyMeters)
    if (style.getSource(USER_ACCURACY_SOURCE_ID) == null) {
        style.addSource(GeoJsonSource(USER_ACCURACY_SOURCE_ID, FeatureCollection.fromFeatures(emptyArray())))
    }
    if (style.getLayer(USER_ACCURACY_FILL_LAYER_ID) == null) {
        style.addLayer(
            FillLayer(USER_ACCURACY_FILL_LAYER_ID, USER_ACCURACY_SOURCE_ID).withProperties(
                PropertyFactory.fillColor("#3B82F6"),
                PropertyFactory.fillOpacity(0.14f)
            )
        )
    }
    if (style.getLayer(USER_ACCURACY_LINE_LAYER_ID) == null) {
        style.addLayer(
            LineLayer(USER_ACCURACY_LINE_LAYER_ID, USER_ACCURACY_SOURCE_ID).withProperties(
                PropertyFactory.lineColor("#60A5FA"),
                PropertyFactory.lineWidth(1.5f),
                PropertyFactory.lineOpacity(0.65f)
            )
        )
    }

    // User location glowing blue dot matching Picture 3
    if (style.getSource(USER_SOURCE_ID) == null) {
        style.addSource(GeoJsonSource(USER_SOURCE_ID, FeatureCollection.fromFeatures(emptyArray())))
    }
    if (style.getLayer(USER_HALO_LAYER_ID) == null) {
        style.addLayer(
            CircleLayer(USER_HALO_LAYER_ID, USER_SOURCE_ID).withProperties(
                PropertyFactory.circleRadius(18.0f),
                PropertyFactory.circleColor("#3B82F6"),
                PropertyFactory.circleOpacity(0.28f),
                PropertyFactory.circleStrokeColor("#60A5FA"),
                PropertyFactory.circleStrokeWidth(1.5f)
            )
        )
    }
    if (style.getLayer(USER_LAYER_ID) == null) {
        style.addLayer(
            CircleLayer(USER_LAYER_ID, USER_SOURCE_ID).withProperties(
                PropertyFactory.circleRadius(8.5f),
                PropertyFactory.circleColor("#1D63ED"),
                PropertyFactory.circleStrokeColor("#FFFFFF"),
                PropertyFactory.circleStrokeWidth(3.0f)
            )
        )
    }

    if (style.getSource(CANDIDATE_SOURCE_ID) == null) {
        style.addSource(GeoJsonSource(CANDIDATE_SOURCE_ID, FeatureCollection.fromFeatures(emptyArray())))
    }
    if (style.getLayer(CANDIDATE_LAYER_ID) == null) {
        style.addLayer(
            SymbolLayer(CANDIDATE_LAYER_ID, CANDIDATE_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(CANDIDATE_ICON_ID),
                PropertyFactory.iconSize(0.56f),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAnchor(Property.ICON_ANCHOR_CENTER)
            )
        )
    }
    if (style.getSource(VERIFIED_SOURCE_ID) == null) {
        style.addSource(GeoJsonSource(VERIFIED_SOURCE_ID, FeatureCollection.fromFeatures(emptyArray())))
    }
    if (style.getLayer(VERIFIED_LAYER_ID) == null) {
        style.addLayer(
            SymbolLayer(VERIFIED_LAYER_ID, VERIFIED_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(VERIFIED_ICON_ID),
                PropertyFactory.iconSize(0.56f),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAnchor(Property.ICON_ANCHOR_CENTER)
            )
        )
    }

    // Selected mosque teardrop crimson pin + soft highlight ring + callout label
    if (style.getSource(SELECTED_SOURCE_ID) == null) {
        style.addSource(GeoJsonSource(SELECTED_SOURCE_ID, FeatureCollection.fromFeatures(emptyArray())))
    }
    if (style.getLayer(SELECTED_HALO_LAYER_ID) == null) {
        style.addLayer(
            CircleLayer(SELECTED_HALO_LAYER_ID, SELECTED_SOURCE_ID).withProperties(
                PropertyFactory.circleRadius(18f),
                PropertyFactory.circleColor("#DC2626"),
                PropertyFactory.circleOpacity(0.24f),
                PropertyFactory.circleStrokeColor("#FFFFFF"),
                PropertyFactory.circleStrokeWidth(2.0f)
            )
        )
    }
    if (style.getLayer(SELECTED_PIN_LAYER_ID) == null) {
        style.addLayer(
            SymbolLayer(SELECTED_PIN_LAYER_ID, SELECTED_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(SELECTED_ICON_ID),
                PropertyFactory.iconSize(0.58f),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM)
            )
        )
    }
    if (style.getLayer(SELECTED_LABEL_LAYER_ID) == null) {
        style.addLayer(
            SymbolLayer(SELECTED_LABEL_LAYER_ID, SELECTED_SOURCE_ID).withProperties(
                PropertyFactory.textField(Expression.get(PROP_SELECTED_LABEL)),
                PropertyFactory.textFont(arrayOf("Noto Sans Bold")),
                PropertyFactory.textSize(12.5f),
                PropertyFactory.textColor("#FFFFFF"),
                PropertyFactory.textHaloColor("#0B4F30"),
                PropertyFactory.textHaloWidth(2.8f),
                PropertyFactory.textOffset(arrayOf(0f, -3.5f)),
                PropertyFactory.textAnchor(Property.TEXT_ANCHOR_BOTTOM),
                PropertyFactory.textAllowOverlap(true),
                PropertyFactory.textIgnorePlacement(true),
                PropertyFactory.textMaxWidth(12f)
            )
        )
    }
}

private fun syncMapLibreSources(
    style: Style,
    mosques: List<MosqueItem>,
    selectedMosque: MosqueItem?,
    selectedFootprint: List<Pair<Double, Double>>,
    route: MosqueRouteResult?,
    userLat: Double,
    userLon: Double,
    hasUserCoords: Boolean,
    userAccuracyMeters: Float? = null
) {
    val verifiedFeatures = mutableListOf<Feature>()
    val candidateFeatures = mutableListOf<Feature>()

    mosques.forEach { mosque ->
        if (selectedMosque?.id == mosque.id) return@forEach
        val feature = Feature.fromGeometry(Point.fromLngLat(mosque.longitude, mosque.latitude))
        feature.addStringProperty(PROP_OSM_KEY, mosque.id)
        feature.addStringProperty("name", mosque.name)
        if (mosque.verificationLevel == OsmVerificationLevel.BUILDING_MOSQUE_CANDIDATE) {
            candidateFeatures.add(feature)
        } else {
            verifiedFeatures.add(feature)
        }
    }

    style.getSourceAs<GeoJsonSource>(VERIFIED_SOURCE_ID)
        ?.setGeoJson(FeatureCollection.fromFeatures(verifiedFeatures))
    style.getSourceAs<GeoJsonSource>(CANDIDATE_SOURCE_ID)
        ?.setGeoJson(FeatureCollection.fromFeatures(candidateFeatures))

    val selectedFeatures = if (selectedMosque != null) {
        val feat = Feature.fromGeometry(Point.fromLngLat(selectedMosque.longitude, selectedMosque.latitude))
        feat.addStringProperty(PROP_OSM_KEY, selectedMosque.id)
        val distText = MosqueFinderRepository.formatStraightLineDistance(selectedMosque.distanceMeters)
        feat.addStringProperty(PROP_SELECTED_LABEL, "${selectedMosque.name}\n$distText")
        listOf(feat)
    } else emptyList()
    style.getSourceAs<GeoJsonSource>(SELECTED_SOURCE_ID)
        ?.setGeoJson(FeatureCollection.fromFeatures(selectedFeatures))

    val footprintFeatures = if (selectedFootprint.size >= 3) {
        val ring = selectedFootprint.map { (lat, lon) -> Point.fromLngLat(lon, lat) }.toMutableList()
        if (ring.first() != ring.last()) ring.add(ring.first())
        val feat = Feature.fromGeometry(Polygon.fromLngLats(listOf(ring)))
        if (selectedMosque != null) {
            feat.addStringProperty(PROP_OSM_KEY, selectedMosque.id)
        }
        listOf(feat)
    } else emptyList()
    style.getSourceAs<GeoJsonSource>(FOOTPRINT_SOURCE_ID)
        ?.setGeoJson(FeatureCollection.fromFeatures(footprintFeatures))

    val routeFeatures = if (route != null && route.coordinates.size >= 2) {
        val pts = route.coordinates.map { (lat, lon) -> Point.fromLngLat(lon, lat) }
        listOf(Feature.fromGeometry(LineString.fromLngLats(pts)))
    } else emptyList()
    style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
        ?.setGeoJson(FeatureCollection.fromFeatures(routeFeatures))

    val userFeatures = if (hasUserCoords) {
        listOf(Feature.fromGeometry(Point.fromLngLat(userLon, userLat)))
    } else {
        emptyList()
    }
    style.getSourceAs<GeoJsonSource>(USER_SOURCE_ID)
        ?.setGeoJson(FeatureCollection.fromFeatures(userFeatures))

    val accuracyFeatures = if (hasUserCoords && userAccuracyMeters != null && userAccuracyMeters in 1f..500f) {
        val ring = MapLibreRuntimeHelper.buildAccuracyCircleRing(userLat, userLon, userAccuracyMeters)
        listOf(Feature.fromGeometry(Polygon.fromLngLats(listOf(ring))))
    } else {
        emptyList()
    }
    style.getSourceAs<GeoJsonSource>(USER_ACCURACY_SOURCE_ID)
        ?.setGeoJson(FeatureCollection.fromFeatures(accuracyFeatures))
}

/**
 * Headless JVM / Robolectric fallback host for unit testing projection, markers, and route overlays.
 */
@Composable
private fun RobolectricMapProjectionHost(
    mosques: List<MosqueItem>,
    selectedMosque: MosqueItem?,
    route: MosqueRouteResult?,
    centerLat: Double,
    centerLon: Double,
    zoom: Int,
    userLat: Double,
    userLon: Double,
    hasUserCoords: Boolean,
    onSelectMosque: (MosqueItem) -> Unit
) {
    val density = LocalDensity.current
    val primaryColor = Color(0xFF0B6E4F)
    val selectedColor = Color(0xFFDC2626)
    val candidateColor = Color(0xFFD97706)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        val pinHalfPx = with(density) { 24.dp.toPx() }

        val n = 2.0.pow(zoom)
        val cx = (centerLon + 180.0) / 360.0 * n * 256.0
        val centerRad = Math.toRadians(centerLat.coerceIn(-85.0511, 85.0511))
        val cy = (1.0 - ln(tan(centerRad) + 1.0 / cos(centerRad)) / PI) / 2.0 * n * 256.0

        if (route != null && route.coordinates.size >= 2) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(4.dp)
                    .background(Color(0xFF1D63ED))
                    .testTag("mosque_map_route_line")
            )
        }

        if (hasUserCoords) {
            val ux = ((userLon + 180.0) / 360.0 * n * 256.0 - cx + widthPx / 2.0).toFloat()
            val uRad = Math.toRadians(userLat.coerceIn(-85.0511, 85.0511))
            val uy = ((1.0 - ln(tan(uRad) + 1.0 / cos(uRad)) / PI) / 2.0 * n * 256.0 - cy + heightPx / 2.0).toFloat()
            Box(
                modifier = Modifier
                    .offset { IntOffset((ux - 10f).roundToInt(), (uy - 10f).roundToInt()) }
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1D63ED))
                    .border(2.dp, Color.White, CircleShape)
                    .testTag("mosque_user_location_dot")
            )
        }

        mosques.forEach { mosque ->
            val mx = ((mosque.longitude + 180.0) / 360.0 * n * 256.0 - cx + widthPx / 2.0).toFloat()
            val mRad = Math.toRadians(mosque.latitude.coerceIn(-85.0511, 85.0511))
            val my = ((1.0 - ln(tan(mRad) + 1.0 / cos(mRad)) / PI) / 2.0 * n * 256.0 - cy + heightPx / 2.0).toFloat()
            if (mx in -40f..(widthPx + 40f) && my in -40f..(heightPx + 40f)) {
                val isSelected = selectedMosque?.id == mosque.id
                val bg = when {
                    isSelected -> selectedColor
                    mosque.verificationLevel == OsmVerificationLevel.BUILDING_MOSQUE_CANDIDATE -> candidateColor
                    else -> primaryColor
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (mx - pinHalfPx).roundToInt(),
                                (my - pinHalfPx).roundToInt()
                            )
                        }
                        .size(48.dp)
                        .clickable { onSelectMosque(mosque) }
                        .testTag("mosque_pin_${mosque.osmId}")
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(if (isSelected) 38.dp else 34.dp)
                            .clip(CircleShape)
                            .background(bg)
                            .border(if (isSelected) 3.dp else 2.dp, Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = mosque.name,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
