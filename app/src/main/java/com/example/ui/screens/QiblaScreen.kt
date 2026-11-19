package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.location.AppLocationManager
import com.example.data.location.LocationSource
import com.example.data.model.AppLanguage
import com.example.data.repository.PrayerCalculator
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private enum class CompassSensorMode {
    ROTATION_VECTOR,
    ACCEL_MAGNETOMETER,
    UNAVAILABLE
}

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

    var rawMagneticHeading by remember { mutableFloatStateOf(0f) }
    var hasValidCompassReading by remember { mutableStateOf(false) }
    var sensorAccuracy by remember { mutableIntStateOf(SensorManager.SENSOR_STATUS_ACCURACY_HIGH) }
    var sensorMode by remember { mutableStateOf(CompassSensorMode.UNAVAILABLE) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        var listener: SensorEventListener? = null

        if (rotationSensor != null) {
            sensorMode = CompassSensorMode.ROTATION_VECTOR
            listener = object : SensorEventListener {
                private val rotationMatrix = FloatArray(9)
                private val orientationAngles = FloatArray(3)

                override fun onSensorChanged(event: SensorEvent?) {
                    val values = event?.values ?: return
                    if (values.size < 3 || values.any { it.isNaN() || it.isInfinite() }) return
                    try {
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, values)
                        SensorManager.getOrientation(rotationMatrix, orientationAngles)
                        val rad = orientationAngles[0]
                        if (rad.isNaN() || rad.isInfinite()) return
                        val azimuthDegrees = Math.toDegrees(rad.toDouble()).toFloat()
                        val normalized = (azimuthDegrees + 360f) % 360f
                        rawMagneticHeading = if (hasValidCompassReading) {
                            lowPass(normalized, rawMagneticHeading)
                        } else {
                            normalized
                        }
                        hasValidCompassReading = true
                    } catch (_: Exception) {}
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    sensorAccuracy = accuracy
                }
            }
            sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        } else if (accelSensor != null && magSensor != null) {
            sensorMode = CompassSensorMode.ACCEL_MAGNETOMETER
            listener = object : SensorEventListener {
                private val gravity = FloatArray(3)
                private val geomagnetic = FloatArray(3)
                private var hasGravity = false
                private var hasMag = false
                private val rotationMatrix = FloatArray(9)
                private val orientationAngles = FloatArray(3)

                override fun onSensorChanged(event: SensorEvent?) {
                    val e = event ?: return
                    val vals = e.values ?: return
                    if (vals.size < 3 || vals.any { it.isNaN() || it.isInfinite() }) return
                    if (e.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        System.arraycopy(vals, 0, gravity, 0, 3)
                        hasGravity = true
                    } else if (e.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                        System.arraycopy(vals, 0, geomagnetic, 0, 3)
                        hasMag = true
                    }

                    if (hasGravity && hasMag &&
                        SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)
                    ) {
                        SensorManager.getOrientation(rotationMatrix, orientationAngles)
                        val rad = orientationAngles[0]
                        if (rad.isNaN() || rad.isInfinite()) return
                        val azimuth = Math.toDegrees(rad.toDouble()).toFloat()
                        val normalized = (azimuth + 360f) % 360f
                        rawMagneticHeading = if (hasValidCompassReading) {
                            lowPass(normalized, rawMagneticHeading)
                        } else {
                            normalized
                        }
                        hasValidCompassReading = true
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    sensorAccuracy = accuracy
                }
            }
            sensorManager.registerListener(listener, accelSensor, SensorManager.SENSOR_DELAY_UI)
            sensorManager.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            sensorMode = CompassSensorMode.UNAVAILABLE
        }

        onDispose {
            listener?.let { sensorManager.unregisterListener(it) }
        }
    }

    val hasCompassSensor = sensorMode != CompassSensorMode.UNAVAILABLE
    val trueHeading = ((rawMagneticHeading + magneticDeclination) + 360f) % 360f
    val relativeAngle = ((qiblaBearing.toFloat() - trueHeading) + 360f) % 360f
    val signedTurnDiff = ((relativeAngle + 180f) % 360f) - 180f

    val animatedCompassRotation by animateFloatAsState(
        targetValue = if (hasValidCompassReading) -trueHeading else 0f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
        label = "compass_rotation"
    )

    val isAligned = hasCompassSensor &&
        hasValidCompassReading &&
        sensorAccuracy != SensorManager.SENSOR_STATUS_UNRELIABLE &&
        sensorAccuracy != SensorManager.SENSOR_STATUS_ACCURACY_LOW &&
        abs(signedTurnDiff) <= 3.5f

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

            Spacer(modifier = Modifier.height(14.dp))

            // Low calibration warning when live sensor is active but low accuracy
            if (hasCompassSensor && (sensorAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE || sensorAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW)) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
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
                Spacer(modifier = Modifier.height(14.dp))
            }

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
                        .padding(bottom = 14.dp)
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

            if (hasCompassSensor) {
                val primaryColor = MaterialTheme.colorScheme.primary
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(8.dp)
                        .testTag("qibla_compass_dial")
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(animatedCompassRotation)
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
                            .rotate(animatedCompassRotation)
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
                            .rotate(animatedCompassRotation + qiblaBearing.toFloat())
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
                                text = "${qiblaBearing.roundToInt()}°",
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
            } else {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag("qibla_unavailable_card")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExploreOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
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
                        Text(
                            text = AppLocalization.get("qibla_no_sensor_title", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = AppLocalization.get("qibla_no_sensor_desc", language),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "${AppLocalization.get("qibla_calculated_bearing", language)}: ${qiblaBearing.roundToInt()}°",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Directional Metrics Card
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
                                text = String.format(Locale.US, "%.1f°", qiblaBearing),
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
                                text = if (hasCompassSensor && hasValidCompassReading) {
                                    String.format(Locale.US, "%.1f°", trueHeading)
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

                    val sensorModeLabel = when (sensorMode) {
                        CompassSensorMode.ROTATION_VECTOR -> AppLocalization.get("qibla_sensor_rotation_vector", language)
                        CompassSensorMode.ACCEL_MAGNETOMETER -> AppLocalization.get("qibla_sensor_accel_mag", language)
                        CompassSensorMode.UNAVAILABLE -> AppLocalization.get("qibla_sensor_unavailable", language)
                    }

                    val accLabel = when {
                        !hasCompassSensor -> AppLocalization.get("qibla_sensor_unavailable", language)
                        !hasValidCompassReading -> AppLocalization.get("qibla_acc_waiting", language)
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
                        val lat = latInput.trim().toDoubleOrNull()
                        val lon = lonInput.trim().toDoubleOrNull()
                        var valid = true
                        if (lat == null) {
                            latError = AppLocalization.get("qibla_error_invalid_number", language)
                            valid = false
                        } else if (lat !in -90.0..90.0) {
                            latError = AppLocalization.get("qibla_error_lat_range", language)
                            valid = false
                        }
                        if (lon == null) {
                            lonError = AppLocalization.get("qibla_error_invalid_number", language)
                            valid = false
                        } else if (lon !in -180.0..180.0) {
                            lonError = AppLocalization.get("qibla_error_lon_range", language)
                            valid = false
                        }
                        if (valid && lat != null && lon != null) {
                            AppLocationManager.setManualCoordinates(
                                context,
                                lat,
                                lon,
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

private fun lowPass(current: Float, previous: Float): Float {
    val alpha = 0.25f
    var diff = current - previous
    while (diff < -180f) diff += 360f
    while (diff > 180f) diff -= 360f
    return (previous + alpha * diff + 360f) % 360f
}
