package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.location.AppLocationManager
import com.example.data.location.CompassMath
import com.example.data.location.CompassMath.AssistedSensorSource
import com.example.data.location.CompassMath.HybridQiblaMode
import com.example.data.location.CompassMath.LiveSensorSource
import com.example.data.location.LocationSource
import com.example.data.model.AppLanguage
import com.example.data.repository.PrayerCalculator
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        AppLocationManager.init(context)
    }

    val userLocation by AppLocationManager.currentLocation.collectAsStateWithLifecycle()
    val isLocating by AppLocationManager.isLocating.collectAsStateWithLifecycle()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            AppLocationManager.requestLocationUpdate(context, forceFresh = true) { _, msg ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                context,
                AppLocalization.get("qibla_perm_denied_toast", language),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    var showCityPicker by remember { mutableStateOf(false) }
    var citySearchQuery by remember { mutableStateOf("") }
    var showManualCoordsDialog by remember { mutableStateOf(false) }

    val qiblaBearing = remember(userLocation.latitude, userLocation.longitude) {
        PrayerCalculator.calculateQiblaBearing(userLocation.latitude, userLocation.longitude)
    }
    val qiblaCardinal = remember(qiblaBearing) {
        CompassMath.cardinalLabel(qiblaBearing.toFloat())
    }
    val distanceKm = remember(userLocation.latitude, userLocation.longitude) {
        PrayerCalculator.calculateDistanceToKaabaKm(userLocation.latitude, userLocation.longitude)
    }

    val magneticDeclination = remember(userLocation.latitude, userLocation.longitude) {
        try {
            val field = GeomagneticField(
                userLocation.latitude.toFloat(),
                userLocation.longitude.toFloat(),
                0f,
                System.currentTimeMillis()
            )
            field.declination
        } catch (_: Exception) {
            0f
        }
    }

    // Automatic device sensor capability detection
    val sensorCapabilities = remember(context) {
        val sm = runCatching {
            context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        }.getOrNull()
        CompassMath.detectSensorCapabilities(sm)
    }

    var selectedMode by remember(sensorCapabilities) {
        mutableStateOf(sensorCapabilities.recommendedMode)
    }
    var isAutoSelectedMode by remember(sensorCapabilities) {
        mutableStateOf(true)
    }

    // Live Compass state
    var rawMagneticHeading by remember { mutableFloatStateOf(0f) }
    var hasValidLiveReading by remember { mutableStateOf(false) }
    var sensorAccuracy by remember { mutableIntStateOf(SensorManager.SENSOR_STATUS_ACCURACY_HIGH) }
    var liveUnwrappedDialAngle by remember { mutableFloatStateOf(0f) }
    var hasUnwrappedLiveAngle by remember { mutableStateOf(false) }

    // Assisted Direction state (relative orientation sensor + verified external reference anchor)
    var relativeYawDeg by remember { mutableFloatStateOf(0f) }
    var hasRelativeYawReading by remember { mutableStateOf(false) }
    var assistedAnchored by remember { mutableStateOf(false) }
    var anchorTrueAzimuthDeg by remember { mutableFloatStateOf(0f) }
    var anchorRelativeYawDeg by remember { mutableFloatStateOf(0f) }
    var assistedUnwrappedDialAngle by remember { mutableFloatStateOf(0f) }
    var hasUnwrappedAssistedAngle by remember { mutableStateOf(false) }

    // Current astronomical solar position for Assisted Direction external reference
    var solarTickMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val solarPosition = remember(userLocation.latitude, userLocation.longitude, solarTickMs) {
        CompassMath.calculateSolarPosition(userLocation.latitude, userLocation.longitude, solarTickMs)
    }

    // Register appropriate Android sensors for the active mode (cleanly unregistered on mode switch or dispose)
    DisposableEffect(selectedMode, sensorCapabilities) {
        val sensorManager = runCatching {
            context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        }.getOrNull()

        var listener: SensorEventListener? = null

        if (sensorManager != null && selectedMode == HybridQiblaMode.LIVE_COMPASS && sensorCapabilities.canUseLiveCompass) {
            val liveSource = sensorCapabilities.bestLiveSource
            when (liveSource) {
                LiveSensorSource.ROTATION_VECTOR,
                LiveSensorSource.GEOMAGNETIC_ROTATION_VECTOR -> {
                    val sensorType = if (liveSource == LiveSensorSource.ROTATION_VECTOR) {
                        Sensor.TYPE_ROTATION_VECTOR
                    } else {
                        Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR
                    }
                    val rotSensor = sensorManager.getDefaultSensor(sensorType)
                    if (rotSensor != null) {
                        listener = object : SensorEventListener {
                            private val rotationMatrix = FloatArray(9)

                            override fun onSensorChanged(event: SensorEvent?) {
                                val values = event?.values ?: return
                                if (!CompassMath.isValidSensorVector(values, 3)) return
                                try {
                                    SensorManager.getRotationMatrixFromVector(rotationMatrix, values)
                                    val azimuth = CompassMath.extractTiltAwareAzimuthFromRotationMatrix(
                                        rotationMatrix = rotationMatrix,
                                        displayRotation = getDisplayRotation(context)
                                    ) ?: return
                                    rawMagneticHeading = CompassMath.lowPassSmoothHeading(
                                        currentDeg = azimuth,
                                        previousDeg = rawMagneticHeading,
                                        hasPreviousReading = hasValidLiveReading
                                    )
                                    hasValidLiveReading = true
                                } catch (_: Exception) {
                                }
                            }

                            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                                sensorAccuracy = accuracy
                            }
                        }
                        sensorManager.registerListener(listener, rotSensor, SensorManager.SENSOR_DELAY_UI)
                    }
                }

                LiveSensorSource.GRAVITY_MAGNETOMETER,
                LiveSensorSource.ACCEL_MAGNETOMETER -> {
                    val gravityType = if (liveSource == LiveSensorSource.GRAVITY_MAGNETOMETER) {
                        Sensor.TYPE_GRAVITY
                    } else {
                        Sensor.TYPE_ACCELEROMETER
                    }
                    val gravitySensor = sensorManager.getDefaultSensor(gravityType)
                    val magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
                    if (gravitySensor != null && magSensor != null) {
                        listener = object : SensorEventListener {
                            private val gravity = FloatArray(3)
                            private val geomagnetic = FloatArray(3)
                            private var hasGravity = false
                            private var hasMag = false
                            private val rotationMatrix = FloatArray(9)

                            override fun onSensorChanged(event: SensorEvent?) {
                                val e = event ?: return
                                val vals = e.values ?: return
                                if (!CompassMath.isValidSensorVector(vals, 3)) return
                                if (!CompassMath.hasNonZeroMagnitude(vals)) return

                                when (e.sensor.type) {
                                    gravityType -> {
                                        if (hasGravity && gravityType == Sensor.TYPE_ACCELEROMETER) {
                                            // Low-pass filter raw accelerometer to isolate gravity vector
                                            val alpha = 0.8f
                                            gravity[0] = alpha * gravity[0] + (1f - alpha) * vals[0]
                                            gravity[1] = alpha * gravity[1] + (1f - alpha) * vals[1]
                                            gravity[2] = alpha * gravity[2] + (1f - alpha) * vals[2]
                                        } else {
                                            System.arraycopy(vals, 0, gravity, 0, 3)
                                            hasGravity = true
                                        }
                                    }
                                    Sensor.TYPE_MAGNETIC_FIELD -> {
                                        if (hasMag) {
                                            val alpha = 0.7f
                                            geomagnetic[0] = alpha * geomagnetic[0] + (1f - alpha) * vals[0]
                                            geomagnetic[1] = alpha * geomagnetic[1] + (1f - alpha) * vals[1]
                                            geomagnetic[2] = alpha * geomagnetic[2] + (1f - alpha) * vals[2]
                                        } else {
                                            System.arraycopy(vals, 0, geomagnetic, 0, 3)
                                            hasMag = true
                                        }
                                    }
                                }

                                if (hasGravity && hasMag &&
                                    SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
                                ) {
                                    val azimuth = CompassMath.extractTiltAwareAzimuthFromRotationMatrix(
                                        rotationMatrix = rotationMatrix,
                                        displayRotation = getDisplayRotation(context)
                                    ) ?: return
                                    rawMagneticHeading = CompassMath.lowPassSmoothHeading(
                                        currentDeg = azimuth,
                                        previousDeg = rawMagneticHeading,
                                        hasPreviousReading = hasValidLiveReading
                                    )
                                    hasValidLiveReading = true
                                }
                            }

                            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                                if (sensor == null || sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                                    sensorAccuracy = accuracy
                                }
                            }
                        }
                        sensorManager.registerListener(listener, gravitySensor, SensorManager.SENSOR_DELAY_UI)
                        sensorManager.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_UI)
                    }
                }

                LiveSensorSource.NONE -> Unit
            }
        } else if (sensorManager != null && selectedMode == HybridQiblaMode.ASSISTED_DIRECTION && sensorCapabilities.canUseAssistedDirection) {
            when (sensorCapabilities.bestAssistedSource) {
                AssistedSensorSource.GAME_ROTATION_VECTOR -> {
                    val gameRot = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
                    if (gameRot != null) {
                        listener = object : SensorEventListener {
                            private val rotationMatrix = FloatArray(9)
                            override fun onSensorChanged(event: SensorEvent?) {
                                val vals = event?.values ?: return
                                if (!CompassMath.isValidSensorVector(vals, 3)) return
                                try {
                                    SensorManager.getRotationMatrixFromVector(rotationMatrix, vals)
                                    val yaw = CompassMath.extractTiltAwareAzimuthFromRotationMatrix(
                                        rotationMatrix = rotationMatrix,
                                        displayRotation = getDisplayRotation(context)
                                    ) ?: return
                                    relativeYawDeg = CompassMath.lowPassSmoothHeading(
                                        currentDeg = yaw,
                                        previousDeg = relativeYawDeg,
                                        hasPreviousReading = hasRelativeYawReading,
                                        alpha = 0.25f
                                    )
                                    hasRelativeYawReading = true
                                } catch (_: Exception) {
                                }
                            }

                            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
                        }
                        sensorManager.registerListener(listener, gameRot, SensorManager.SENSOR_DELAY_UI)
                    }
                }

                AssistedSensorSource.GYROSCOPE -> {
                    val gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
                    if (gyro != null) {
                        listener = object : SensorEventListener {
                            private var lastTimestampNs: Long = 0L
                            override fun onSensorChanged(event: SensorEvent?) {
                                val e = event ?: return
                                val vals = e.values ?: return
                                if (!CompassMath.isValidSensorVector(vals, 3)) return
                                val ts = e.timestamp
                                if (lastTimestampNs != 0L && ts > lastTimestampNs) {
                                    val dtSec = ((ts - lastTimestampNs) / 1_000_000_000.0).coerceIn(0.0, 0.25)
                                    // Positive Z rotation is counter-clockwise; clockwise azimuth decreases with +Z
                                    val deltaDeg = -Math.toDegrees(vals[2] * dtSec).toFloat()
                                    if (deltaDeg.isFinite()) {
                                        relativeYawDeg = CompassMath.normalizeDegrees(relativeYawDeg + deltaDeg)
                                    }
                                }
                                lastTimestampNs = ts
                                hasRelativeYawReading = true
                            }

                            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
                        }
                        sensorManager.registerListener(listener, gyro, SensorManager.SENSOR_DELAY_UI)
                    }
                }

                AssistedSensorSource.NONE -> Unit
            }
        }

        onDispose {
            if (sensorManager != null && listener != null) {
                runCatching { sensorManager.unregisterListener(listener) }
            }
        }
    }

    val isLiveModeActive = selectedMode == HybridQiblaMode.LIVE_COMPASS && sensorCapabilities.canUseLiveCompass
    val isAssistedActive = selectedMode == HybridQiblaMode.ASSISTED_DIRECTION
    val isManualModeActive = selectedMode == HybridQiblaMode.MANUAL_QIBLA ||
        (selectedMode == HybridQiblaMode.LIVE_COMPASS && !sensorCapabilities.canUseLiveCompass)

    val liveTrueHeading = CompassMath.magneticToTrueHeading(rawMagneticHeading, magneticDeclination)
    val assistedTrueHeading = if (assistedAnchored) {
        CompassMath.computeAssistedTrueHeading(
            anchorTrueAzimuthDeg = anchorTrueAzimuthDeg,
            currentRelativeYawDeg = relativeYawDeg,
            anchorRelativeYawDeg = anchorRelativeYawDeg
        )
    } else {
        0f
    }

    // Update unwrapped angles to prevent 359° <-> 0° animation whip-back
    LaunchedEffect(liveTrueHeading, hasValidLiveReading) {
        if (hasValidLiveReading) {
            liveUnwrappedDialAngle = CompassMath.unwrapContinuousAngle(
                currentTargetAngle = -liveTrueHeading,
                previousUnwrappedAngle = liveUnwrappedDialAngle,
                hasPrevious = hasUnwrappedLiveAngle
            )
            hasUnwrappedLiveAngle = true
        }
    }

    LaunchedEffect(assistedTrueHeading, assistedAnchored) {
        if (assistedAnchored) {
            assistedUnwrappedDialAngle = CompassMath.unwrapContinuousAngle(
                currentTargetAngle = -assistedTrueHeading,
                previousUnwrappedAngle = assistedUnwrappedDialAngle,
                hasPrevious = hasUnwrappedAssistedAngle
            )
            hasUnwrappedAssistedAngle = true
        } else {
            hasUnwrappedAssistedAngle = false
            assistedUnwrappedDialAngle = 0f
        }
    }

    val animatedLiveRotation by animateFloatAsState(
        targetValue = if (hasValidLiveReading) liveUnwrappedDialAngle else 0f,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = 260f),
        label = "live_compass_rotation"
    )

    val animatedAssistedRotation by animateFloatAsState(
        targetValue = if (assistedAnchored) assistedUnwrappedDialAngle else 0f,
        animationSpec = spring(dampingRatio = 0.82f, stiffness = 260f),
        label = "assisted_compass_rotation"
    )

    val activeHeadingAvailable = (isLiveModeActive && hasValidLiveReading) ||
        (isAssistedActive && assistedAnchored)
    val activeTrueHeading = when {
        isLiveModeActive && hasValidLiveReading -> liveTrueHeading
        isAssistedActive && assistedAnchored -> assistedTrueHeading
        else -> 0f
    }
    val signedTurnDiff = if (activeHeadingAvailable) {
        CompassMath.shortestSignedDelta(activeTrueHeading, qiblaBearing.toFloat())
    } else {
        0f
    }

    val isAligned = when {
        isLiveModeActive -> CompassMath.isAlignedWithQibla(
            hasCompassSensor = sensorCapabilities.canUseLiveCompass,
            hasValidReading = hasValidLiveReading,
            sensorAccuracy = sensorAccuracy,
            trueHeadingDeg = liveTrueHeading,
            qiblaBearingDeg = qiblaBearing.toFloat()
        )
        isAssistedActive && assistedAnchored -> abs(signedTurnDiff) <= CompassMath.ALIGNMENT_TOLERANCE_DEGREES
        else -> false
    }

    val modeExplanationText = when {
        isLiveModeActive -> AppLocalization.get(sensorCapabilities.autoModeReasonKey, language)
        isAssistedActive -> when {
            !sensorCapabilities.canUseAssistedDirection ->
                AppLocalization.get("qibla_reason_assisted_no_sensor", language)
            assistedAnchored ->
                AppLocalization.get("qibla_reason_assisted_anchored", language)
            else ->
                AppLocalization.get("qibla_reason_assisted_unanchored", language)
        }
        !sensorCapabilities.canUseLiveCompass ->
            AppLocalization.get(sensorCapabilities.autoModeReasonKey, language)
        else ->
            AppLocalization.get("qibla_reason_manual_user", language)
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("qibla", language),
                language = language,
                onBackClick = onBack,
                actions = {
                    IconButton(
                        onClick = {
                            if (AppLocationManager.hasLocationPermission(context)) {
                                AppLocationManager.requestLocationUpdate(context, forceFresh = true)
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        if (isLocating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = AppLocalization.get("qibla_update_gps_cd", language),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            val isGpsActive = AppLocationManager.isGpsEnabled(context)
            val hasPerm = AppLocationManager.hasLocationPermission(context)

            if (!hasPerm) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("qibla_permission_card")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationSearching,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("qibla_perm_title", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = AppLocalization.get("qibla_perm_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            modifier = Modifier.heightIn(min = 48.dp)
                        ) {
                            Text(AppLocalization.get("qibla_perm_allow", language))
                        }
                    }
                }
            } else if (!isGpsActive) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("qibla_gps_off_card")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationDisabled,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("qibla_gps_off_title", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = AppLocalization.get("qibla_gps_off_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { AppLocationManager.openLocationSettings(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.heightIn(min = 48.dp)
                        ) {
                            Text(AppLocalization.get("qibla_gps_turn_on", language))
                        }
                    }
                }
            }

            // Location Header Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (userLocation.isGpsFix) Icons.Default.GpsFixed else Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${userLocation.cityName}${if (userLocation.countryName.isNotBlank()) ", " + userLocation.countryName else ""}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = String.format(
                                        Locale.US,
                                        "%.4f°, %.4f°",
                                        userLocation.latitude,
                                        userLocation.longitude
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { showCityPicker = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("qibla_change_location_btn")
                        ) {
                            Text(
                                text = AppLocalization.get("qibla_change_location", language),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val sourceBadgeText = when {
                        userLocation.sourceType == LocationSource.GPS ->
                            AppLocalization.get("qibla_source_gps", language)
                        userLocation.sourceType == LocationSource.NETWORK ->
                            AppLocalization.get("qibla_source_approximate", language)
                        userLocation.sourceType == LocationSource.MANUAL || userLocation.sourceType == LocationSource.PRESET_CITY ->
                            AppLocalization.get("qibla_source_manual", language)
                        else ->
                            AppLocalization.get("qibla_source_estimate", language)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.testTag("qibla_location_source_badge")
                        ) {
                            Text(
                                text = sourceBadgeText,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "${AppLocalization.get("qibla_distance_label", language)}: ${distanceKm.roundToInt()} ${AppLocalization.get("qibla_km_unit", language)}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag("qibla_distance_text")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hybrid Mode Selector (Live Compass / Assisted Direction / Manual Qibla)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("qibla_mode_selector")
            ) {
                FilterChip(
                    selected = selectedMode == HybridQiblaMode.LIVE_COMPASS,
                    enabled = sensorCapabilities.canUseLiveCompass,
                    onClick = {
                        selectedMode = HybridQiblaMode.LIVE_COMPASS
                        isAutoSelectedMode = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = {
                        Text(
                            text = AppLocalization.get("qibla_mode_live", language),
                            maxLines = 1
                        )
                    },
                    colors = noorSelectableChipColors(),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("qibla_mode_chip_live")
                )

                FilterChip(
                    selected = selectedMode == HybridQiblaMode.ASSISTED_DIRECTION,
                    onClick = {
                        selectedMode = HybridQiblaMode.ASSISTED_DIRECTION
                        isAutoSelectedMode = false
                        solarTickMs = System.currentTimeMillis()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = {
                        Text(
                            text = AppLocalization.get("qibla_mode_assisted", language),
                            maxLines = 1
                        )
                    },
                    colors = noorSelectableChipColors(),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("qibla_mode_chip_assisted")
                )

                FilterChip(
                    selected = selectedMode == HybridQiblaMode.MANUAL_QIBLA,
                    onClick = {
                        selectedMode = HybridQiblaMode.MANUAL_QIBLA
                        isAutoSelectedMode = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CompassCalibration,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = {
                        Text(
                            text = AppLocalization.get("qibla_mode_manual", language),
                            maxLines = 1
                        )
                    },
                    colors = noorSelectableChipColors(),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("qibla_mode_chip_manual")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Low calibration warning when live sensor is active with low/unreliable accuracy
            if (isLiveModeActive && CompassMath.needsCalibration(sensorAccuracy)) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("qibla_calibration_card")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppLocalization.get("qibla_calibration_title", language),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = AppLocalization.get("qibla_calibration_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Alignment Banner (ONLY when genuine live/anchored heading is aligned within tolerance)
            AnimatedVisibility(visible = isAligned) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(MaterialTheme.colorScheme.primary, Color(0xFFE6A100))
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("qibla_aligned_banner")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = AppLocalization.get("qibla_aligned_banner", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Active Mode Primary Visual
            when {
                // MODE 1: LIVE COMPASS MODE
                isLiveModeActive -> {
                    QiblaMovingCompassDial(
                        dialRotationDeg = animatedLiveRotation,
                        qiblaBearingDeg = qiblaBearing.toFloat(),
                        isAligned = isAligned,
                        language = language,
                        dialTestTag = "qibla_compass_dial"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val turnHintText = when {
                        !hasValidLiveReading -> AppLocalization.get("qibla_waiting_sensor_hint", language)
                        isAligned -> AppLocalization.get("qibla_aligned_banner", language)
                        signedTurnDiff > 0f -> String.format(
                            Locale.getDefault(),
                            AppLocalization.get("qibla_turn_right", language),
                            abs(signedTurnDiff).roundToInt()
                        )
                        else -> String.format(
                            Locale.getDefault(),
                            AppLocalization.get("qibla_turn_left", language),
                            abs(signedTurnDiff).roundToInt()
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isAligned) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                        },
                        modifier = Modifier.testTag("qibla_turn_guidance_badge")
                    ) {
                        Text(
                            text = turnHintText,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (isAligned) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            },
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }

                // MODE 2: ASSISTED DIRECTION MODE (Strictly Reference-Locked — NEVER fakes North)
                isAssistedActive -> {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("qibla_assisted_card")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = AppLocalization.get("qibla_assisted_title", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (!sensorCapabilities.canUseAssistedDirection) {
                                Icon(
                                    imageVector = Icons.Default.ExploreOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = AppLocalization.get("qibla_reason_assisted_no_sensor", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        selectedMode = HybridQiblaMode.MANUAL_QIBLA
                                        isAutoSelectedMode = false
                                    },
                                    modifier = Modifier
                                        .defaultMinSize(minHeight = 48.dp)
                                        .testTag("qibla_assisted_switch_manual_btn")
                                ) {
                                    Text(AppLocalization.get("qibla_assisted_switch_manual", language))
                                }
                            } else {
                                // Show moving assisted dial ONLY AFTER user locks a verified external reference
                                if (assistedAnchored) {
                                    QiblaMovingCompassDial(
                                        dialRotationDeg = animatedAssistedRotation,
                                        qiblaBearingDeg = qiblaBearing.toFloat(),
                                        isAligned = isAligned,
                                        language = language,
                                        dialTestTag = "qibla_assisted_dial"
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    val turnText = if (isAligned) {
                                        AppLocalization.get("qibla_aligned_banner", language)
                                    } else if (signedTurnDiff > 0f) {
                                        String.format(
                                            Locale.getDefault(),
                                            AppLocalization.get("qibla_turn_right", language),
                                            abs(signedTurnDiff).roundToInt()
                                        )
                                    } else {
                                        String.format(
                                            Locale.getDefault(),
                                            AppLocalization.get("qibla_turn_left", language),
                                            abs(signedTurnDiff).roundToInt()
                                        )
                                    }
                                    Text(
                                        text = turnText,
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    OutlinedButton(
                                        onClick = { assistedAnchored = false },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .defaultMinSize(minHeight = 48.dp)
                                            .testTag("qibla_assisted_reset_btn")
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(AppLocalization.get("qibla_assisted_reset_lock", language))
                                    }
                                } else {
                                    // Unanchored: Static reference diagram + Lock Reference buttons (NO fake moving needle!)
                                    QiblaStaticReferenceDial(
                                        qiblaBearingDeg = qiblaBearing.toFloat(),
                                        qiblaCardinal = qiblaCardinal,
                                        language = language,
                                        testTag = "qibla_manual_reference_dial"
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (solarPosition.isUsableForReference) {
                                                String.format(
                                                    Locale.getDefault(),
                                                    AppLocalization.get("qibla_assisted_sun_status", language),
                                                    solarPosition.azimuthDeg.roundToInt(),
                                                    CompassMath.cardinalLabel(solarPosition.azimuthDeg),
                                                    solarPosition.altitudeDeg.roundToInt()
                                                )
                                            } else {
                                                AppLocalization.get("qibla_assisted_sun_below", language)
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    if (solarPosition.isUsableForReference) {
                                        Button(
                                            onClick = {
                                                solarTickMs = System.currentTimeMillis()
                                                val freshSun = CompassMath.calculateSolarPosition(
                                                    userLocation.latitude,
                                                    userLocation.longitude,
                                                    solarTickMs
                                                )
                                                anchorTrueAzimuthDeg = freshSun.azimuthDeg
                                                anchorRelativeYawDeg = relativeYawDeg
                                                assistedAnchored = true
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .defaultMinSize(minHeight = 48.dp)
                                                .testTag("qibla_assisted_lock_sun_btn")
                                        ) {
                                            Icon(Icons.Default.WbSunny, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                String.format(
                                                    Locale.getDefault(),
                                                    AppLocalization.get("qibla_assisted_lock_sun", language),
                                                    solarPosition.azimuthDeg.roundToInt()
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            anchorTrueAzimuthDeg = 0f
                                            anchorRelativeYawDeg = relativeYawDeg
                                            assistedAnchored = true
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .defaultMinSize(minHeight = 48.dp)
                                            .testTag("qibla_assisted_lock_north_btn")
                                    ) {
                                        Icon(Icons.Default.North, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(AppLocalization.get("qibla_assisted_lock_north", language))
                                    }
                                }
                            }
                        }
                    }
                }

                // MODE 3: MANUAL QIBLA MODE (Works on EVERY phone, even without Magnetometer)
                else -> {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("qibla_unavailable_card")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.testTag("qibla_calculated_only_badge")
                            ) {
                                Text(
                                    text = AppLocalization.get("qibla_calculated_only_badge", language),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            // Static N/S/E/W reference compass diagram (NEVER animates falsely)
                            QiblaStaticReferenceDial(
                                qiblaBearingDeg = qiblaBearing.toFloat(),
                                qiblaCardinal = qiblaCardinal,
                                language = language,
                                testTag = "qibla_manual_reference_dial"
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = AppLocalization.get("qibla_static_ref_label", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            if (!sensorCapabilities.canUseLiveCompass) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = AppLocalization.get("qibla_no_sensor_title", language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = AppLocalization.get("qibla_no_sensor_desc", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "${AppLocalization.get("qibla_calculated_bearing", language)}: ${qiblaBearing.roundToInt()}° ($qiblaCardinal)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = String.format(
                                            Locale.US,
                                            AppLocalization.get("qibla_no_sensor_guide", language),
                                            qiblaBearing.roundToInt(),
                                            String.format(Locale.US, "%+.1f", magneticDeclination)
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mode Explanation Strip (explains why active mode was auto-selected or active)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("qibla_mode_explanation_card")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        if (isAutoSelectedMode) {
                            Text(
                                text = AppLocalization.get("qibla_mode_auto_badge", language),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = modeExplanationText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Directional Metrics Card (strictly separates Qibla Bearing vs Live Device Heading)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("qibla_metrics_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("qibla_metrics_title", language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = AppLocalization.get("qibla_true_bearing", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format(Locale.US, "%.1f° %s", qiblaBearing, qiblaCardinal),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Column {
                            Text(
                                text = AppLocalization.get("qibla_current_heading", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (activeHeadingAvailable) {
                                    String.format(Locale.US, "%.1f°", activeTrueHeading)
                                } else {
                                    "--"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {
                            Text(
                                text = AppLocalization.get("qibla_magnetic_declination", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format(Locale.US, "%+.1f°", magneticDeclination),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC88A00)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    val sensorModeLabel = when {
                        isLiveModeActive -> when (sensorCapabilities.bestLiveSource) {
                            LiveSensorSource.ROTATION_VECTOR -> AppLocalization.get("qibla_sensor_rotation_vector", language)
                            LiveSensorSource.GEOMAGNETIC_ROTATION_VECTOR -> AppLocalization.get("qibla_sensor_geomag_rv", language)
                            LiveSensorSource.GRAVITY_MAGNETOMETER -> AppLocalization.get("qibla_sensor_gravity_mag", language)
                            LiveSensorSource.ACCEL_MAGNETOMETER -> AppLocalization.get("qibla_sensor_accel_mag", language)
                            LiveSensorSource.NONE -> AppLocalization.get("qibla_sensor_unavailable", language)
                        }
                        isAssistedActive -> when (sensorCapabilities.bestAssistedSource) {
                            AssistedSensorSource.GAME_ROTATION_VECTOR -> AppLocalization.get("qibla_sensor_game_rv", language)
                            AssistedSensorSource.GYROSCOPE -> AppLocalization.get("qibla_sensor_gyroscope", language)
                            AssistedSensorSource.NONE -> AppLocalization.get("qibla_sensor_unavailable", language)
                        }
                        else -> AppLocalization.get("qibla_mode_manual", language)
                    }

                    val accLabel = when {
                        isManualModeActive -> AppLocalization.get("qibla_calculated_only_badge", language)
                        isAssistedActive -> if (assistedAnchored) {
                            AppLocalization.get("qibla_assisted_title", language)
                        } else {
                            AppLocalization.get("qibla_acc_waiting", language)
                        }
                        !sensorCapabilities.canUseLiveCompass -> AppLocalization.get("qibla_sensor_unavailable", language)
                        !hasValidLiveReading -> AppLocalization.get("qibla_acc_waiting", language)
                        sensorAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> AppLocalization.get("qibla_acc_high", language)
                        sensorAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> AppLocalization.get("qibla_acc_medium", language)
                        sensorAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW -> AppLocalization.get("qibla_acc_low", language)
                        else -> AppLocalization.get("qibla_acc_unreliable", language)
                    }

                    Text(
                        text = "${AppLocalization.get("qibla_sensor_mode_label", language)}: $sensorModeLabel",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${AppLocalization.get("qibla_accuracy_label", language)}: $accLabel",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = AppLocalization.get("qibla_offline_note", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // City Selection Dialog
    if (showCityPicker) {
        val filteredCities = remember(citySearchQuery) {
            AppLocationManager.searchCities(citySearchQuery)
        }

        AlertDialog(
            onDismissRequest = {
                showCityPicker = false
                citySearchQuery = ""
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = AppLocalization.get("qibla_city_dialog_title", language),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            showCityPicker = false
                            showManualCoordsDialog = true
                        },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("qibla_open_manual_coords_btn")
                    ) {
                        Icon(
                            Icons.Default.PinDrop,
                            contentDescription = AppLocalization.get("qibla_custom_coords_cd", language),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = citySearchQuery,
                        onValueChange = { citySearchQuery = it },
                        placeholder = { Text(AppLocalization.get("qibla_city_search_hint", language), fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (citySearchQuery.isNotEmpty()) {
                                IconButton(onClick = { citySearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    if (filteredCities.isEmpty()) {
                        Text(
                            text = AppLocalization.get("qibla_no_city_results", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.heightIn(max = 320.dp)) {
                            items(filteredCities) { city ->
                                val isSel = (userLocation.cityName == city.nameEn)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            AppLocationManager.selectPresetCity(context, city)
                                            showCityPicker = false
                                            citySearchQuery = ""
                                        }
                                        .padding(vertical = 10.dp, horizontal = 4.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${city.nameEn} (${city.country})",
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${city.nameUr} • ${city.latitude}, ${city.longitude}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (isSel) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showCityPicker = false
                    citySearchQuery = ""
                }) {
                    Text(AppLocalization.get("qibla_close", language))
                }
            }
        )
    }

    // Manual Coordinates Dialog
    if (showManualCoordsDialog) {
        var latInput by remember { mutableStateOf(userLocation.latitude.toString()) }
        var lonInput by remember { mutableStateOf(userLocation.longitude.toString()) }
        var cityInput by remember { mutableStateOf(userLocation.cityName) }
        var latError by remember { mutableStateOf<String?>(null) }
        var lonError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showManualCoordsDialog = false },
            title = { Text(AppLocalization.get("qibla_manual_title", language), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = cityInput,
                        onValueChange = { cityInput = it },
                        label = { Text(AppLocalization.get("qibla_manual_city_label", language)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("qibla_manual_city_field")
                    )
                    OutlinedTextField(
                        value = latInput,
                        onValueChange = {
                            latInput = it
                            latError = null
                        },
                        isError = latError != null,
                        label = { Text(AppLocalization.get("qibla_manual_lat_label", language)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("qibla_manual_lat_field")
                    )
                    if (latError != null) {
                        Text(
                            text = latError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.testTag("qibla_lat_error_text")
                        )
                    }
                    OutlinedTextField(
                        value = lonInput,
                        onValueChange = {
                            lonInput = it
                            lonError = null
                        },
                        isError = lonError != null,
                        label = { Text(AppLocalization.get("qibla_manual_lon_label", language)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("qibla_manual_lon_field")
                    )
                    if (lonError != null) {
                        Text(
                            text = lonError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val validation = CompassMath.validateManualCoordinates(latInput, lonInput)
                        latError = validation.latErrorKey?.let { AppLocalization.get(it, language) }
                        lonError = validation.lonErrorKey?.let { AppLocalization.get(it, language) }
                        if (validation.isValid && validation.latitude != null && validation.longitude != null) {
                            AppLocationManager.setManualCoordinates(
                                context,
                                validation.latitude,
                                validation.longitude,
                                cityInput.trim().ifBlank { "Custom Location" },
                                "",
                                userLocation.timeZoneId
                            )
                            showManualCoordsDialog = false
                        }
                    },
                    modifier = Modifier.testTag("qibla_apply_coords_btn")
                ) {
                    Text(AppLocalization.get("qibla_apply_coords", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualCoordsDialog = false }) {
                    Text(AppLocalization.get("qibla_cancel", language))
                }
            }
        )
    }
}

@Composable
private fun QiblaMovingCompassDial(
    dialRotationDeg: Float,
    qiblaBearingDeg: Float,
    isAligned: Boolean,
    language: AppLanguage,
    dialTestTag: String
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(300.dp)
            .padding(8.dp)
            .testTag(dialTestTag)
    ) {
        // Fixed top phone-heading pointer notch
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 6.dp, height = 14.dp)
                .background(
                    color = if (isAligned) Color(0xFFE6A100) else primaryColor,
                    shape = RoundedCornerShape(3.dp)
                )
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(dialRotationDeg)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2 - 12.dp.toPx()

            drawCircle(
                color = primaryColor.copy(alpha = 0.18f),
                radius = radius,
                center = center,
                style = Stroke(width = 3.dp.toPx())
            )

            for (deg in 0 until 360 step 5) {
                val angleRad = Math.toRadians((deg - 90).toDouble())
                val isMajor = (deg % 30 == 0)
                val isCardinal = (deg % 90 == 0)
                val tickLength = if (isCardinal) 16.dp.toPx() else if (isMajor) 10.dp.toPx() else 5.dp.toPx()
                val strokeWidth = if (isCardinal) 3.dp.toPx() else if (isMajor) 2.dp.toPx() else 1.dp.toPx()
                val tickColor = if (deg == 0) Color(0xFFE53935) else if (isMajor) primaryColor else Color.Gray.copy(alpha = 0.5f)

                val startX = (center.x + (radius - tickLength) * cos(angleRad)).toFloat()
                val startY = (center.y + (radius - tickLength) * sin(angleRad)).toFloat()
                val endX = (center.x + radius * cos(angleRad)).toFloat()
                val endY = (center.y + radius * sin(angleRad)).toFloat()

                drawLine(
                    color = tickColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = strokeWidth
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(dialRotationDeg)
        ) {
            Text(
                text = AppLocalization.get("qibla_north_short", language),
                color = Color(0xFFE53935),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 18.dp)
            )
            Text(
                text = AppLocalization.get("qibla_south_short", language),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 18.dp)
            )
            Text(
                text = AppLocalization.get("qibla_east_short", language),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 18.dp)
            )
            Text(
                text = AppLocalization.get("qibla_west_short", language),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 18.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(dialRotationDeg + qiblaBearingDeg)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 28.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isAligned) Color(0xFFE6A100) else primaryColor,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = "Kaaba Qibla Direction",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Canvas(modifier = Modifier.size(16.dp, 28.dp)) {
                    val p = Path().apply {
                        moveTo(size.width / 2, 0f)
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(p, color = if (isAligned) Color(0xFFE6A100) else primaryColor)
                }
            }
        }

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            modifier = Modifier.size(110.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "${qiblaBearingDeg.roundToInt()}°",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = AppLocalization.get("qibla_angle_label", language),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Static N/S/E/W visual reference dial for Manual Qibla Mode (and unanchored Assisted Mode).
 * Fixed with True North at top (0°) so users on magnetometer-less phones see the exact
 * Qibla bearing angle relative to North, East, South, and West without any fake live movement.
 */
@Composable
private fun QiblaStaticReferenceDial(
    qiblaBearingDeg: Float,
    qiblaCardinal: String,
    language: AppLanguage,
    testTag: String
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(240.dp)
            .padding(6.dp)
            .testTag(testTag)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2 - 12.dp.toPx()

            drawCircle(
                color = primaryColor.copy(alpha = 0.22f),
                radius = radius,
                center = center,
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Crosshair N-S and E-W reference axes
            drawLine(
                color = primaryColor.copy(alpha = 0.14f),
                start = Offset(center.x, center.y - radius + 16.dp.toPx()),
                end = Offset(center.x, center.y + radius - 16.dp.toPx()),
                strokeWidth = 1.5.dp.toPx()
            )
            drawLine(
                color = primaryColor.copy(alpha = 0.14f),
                start = Offset(center.x - radius + 16.dp.toPx(), center.y),
                end = Offset(center.x + radius - 16.dp.toPx(), center.y),
                strokeWidth = 1.5.dp.toPx()
            )

            for (deg in 0 until 360 step 15) {
                val angleRad = Math.toRadians((deg - 90).toDouble())
                val isCardinal = (deg % 90 == 0)
                val tickLength = if (isCardinal) 13.dp.toPx() else 6.dp.toPx()
                val tickColor = if (deg == 0) Color(0xFFE53935) else if (isCardinal) primaryColor else Color.Gray.copy(alpha = 0.45f)
                val startX = (center.x + (radius - tickLength) * cos(angleRad)).toFloat()
                val startY = (center.y + (radius - tickLength) * sin(angleRad)).toFloat()
                val endX = (center.x + radius * cos(angleRad)).toFloat()
                val endY = (center.y + radius * sin(angleRad)).toFloat()
                drawLine(
                    color = tickColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (isCardinal) 2.5.dp.toPx() else 1.2.dp.toPx()
                )
            }

            // Static Qibla bearing ray from center to perimeter
            val qiblaRad = Math.toRadians((qiblaBearingDeg - 90f).toDouble())
            val rayEnd = Offset(
                (center.x + (radius - 28.dp.toPx()) * cos(qiblaRad)).toFloat(),
                (center.y + (radius - 28.dp.toPx()) * sin(qiblaRad)).toFloat()
            )
            drawLine(
                color = primaryColor,
                start = center,
                end = rayEnd,
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Fixed N / E / S / W cardinal labels
        Text(
            text = AppLocalization.get("qibla_north_short", language),
            color = Color(0xFFE53935),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 14.dp)
        )
        Text(
            text = AppLocalization.get("qibla_south_short", language),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 14.dp)
        )
        Text(
            text = AppLocalization.get("qibla_east_short", language),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 14.dp)
        )
        Text(
            text = AppLocalization.get("qibla_west_short", language),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 14.dp)
        )

        // Kaaba marker positioned at static Qibla bearing angle
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(qiblaBearingDeg)
        ) {
            Surface(
                shape = CircleShape,
                color = primaryColor,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 24.dp)
                    .size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Mosque,
                        contentDescription = "Qibla Direction Reference",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
            modifier = Modifier.size(86.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "${qiblaBearingDeg.roundToInt()}°",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = qiblaCardinal,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun getDisplayRotation(context: Context): Int {
    return runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display?.rotation ?: Surface.ROTATION_0
        } else {
            @Suppress("DEPRECATION")
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            @Suppress("DEPRECATION")
            wm?.defaultDisplay?.rotation ?: Surface.ROTATION_0
        }
    }.getOrDefault(Surface.ROTATION_0)
}
