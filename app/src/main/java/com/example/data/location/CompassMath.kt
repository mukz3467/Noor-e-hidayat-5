package com.example.data.location

import android.hardware.Sensor
import android.hardware.SensorManager
import android.view.Surface
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Pure Kotlin compass, hybrid sensor detection, solar reference, and orientation math helpers
 * for Qibla Finder. Handles 0°/360° wrap-around smoothing, continuous rotation angle unwrapping,
 * display-rotation + tilt coordinate remapping, hybrid mode auto-selection, and manual validation.
 */
object CompassMath {

    const val ALIGNMENT_TOLERANCE_DEGREES = 3.5f

    enum class CompassMode {
        ROTATION_VECTOR,
        ACCEL_MAG_FALLBACK,
        UNAVAILABLE
    }

    enum class HybridQiblaMode {
        LIVE_COMPASS,
        ASSISTED_DIRECTION,
        MANUAL_QIBLA
    }

    enum class LiveSensorSource {
        ROTATION_VECTOR,
        GEOMAGNETIC_ROTATION_VECTOR,
        GRAVITY_MAGNETOMETER,
        ACCEL_MAGNETOMETER,
        NONE
    }

    enum class AssistedSensorSource {
        GAME_ROTATION_VECTOR,
        GYROSCOPE,
        NONE
    }

    data class SensorCapabilities(
        val hasRotationVector: Boolean = false,
        val hasGeomagneticRotationVector: Boolean = false,
        val hasAccelerometer: Boolean = false,
        val hasGravity: Boolean = false,
        val hasMagnetometer: Boolean = false,
        val hasGyroscope: Boolean = false,
        val hasGameRotationVector: Boolean = false
    ) {
        val bestLiveSource: LiveSensorSource
            get() = when {
                hasRotationVector -> LiveSensorSource.ROTATION_VECTOR
                hasGeomagneticRotationVector -> LiveSensorSource.GEOMAGNETIC_ROTATION_VECTOR
                hasGravity && hasMagnetometer -> LiveSensorSource.GRAVITY_MAGNETOMETER
                hasAccelerometer && hasMagnetometer -> LiveSensorSource.ACCEL_MAGNETOMETER
                else -> LiveSensorSource.NONE
            }

        val canUseLiveCompass: Boolean
            get() = bestLiveSource != LiveSensorSource.NONE

        val bestAssistedSource: AssistedSensorSource
            get() = when {
                hasGameRotationVector -> AssistedSensorSource.GAME_ROTATION_VECTOR
                hasGyroscope -> AssistedSensorSource.GYROSCOPE
                else -> AssistedSensorSource.NONE
            }

        val canUseAssistedDirection: Boolean
            get() = bestAssistedSource != AssistedSensorSource.NONE

        /**
         * Automatically selects the most reliable mode without ever pretending a gyroscope
         * alone knows True North. When a magnetometer-backed live source exists, selects
         * [HybridQiblaMode.LIVE_COMPASS]; otherwise selects [HybridQiblaMode.MANUAL_QIBLA]
         * (with Assisted Direction available once the user locks an external Sun/North reference).
         */
        val recommendedMode: HybridQiblaMode
            get() = if (canUseLiveCompass) {
                HybridQiblaMode.LIVE_COMPASS
            } else {
                HybridQiblaMode.MANUAL_QIBLA
            }

        val autoModeReasonKey: String
            get() = when (bestLiveSource) {
                LiveSensorSource.ROTATION_VECTOR -> "qibla_reason_live_rv"
                LiveSensorSource.GEOMAGNETIC_ROTATION_VECTOR -> "qibla_reason_live_geomag"
                LiveSensorSource.GRAVITY_MAGNETOMETER -> "qibla_reason_live_grav_mag"
                LiveSensorSource.ACCEL_MAGNETOMETER -> "qibla_reason_live_accel_mag"
                LiveSensorSource.NONE -> if (canUseAssistedDirection) {
                    "qibla_reason_no_mag_assisted_avail"
                } else {
                    "qibla_reason_no_mag_manual_only"
                }
            }
    }

    data class SolarPosition(
        val azimuthDeg: Float,
        val altitudeDeg: Float,
        val isUsableForReference: Boolean
    )

    data class CoordinateValidationResult(
        val isValid: Boolean,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val latErrorKey: String? = null,
        val lonErrorKey: String? = null
    )

    fun detectSensorCapabilities(sensorManager: SensorManager?): SensorCapabilities {
        if (sensorManager == null) return SensorCapabilities()
        return SensorCapabilities(
            hasRotationVector = runCatching {
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null
            }.getOrDefault(false),
            hasGeomagneticRotationVector = runCatching {
                sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) != null
            }.getOrDefault(false),
            hasAccelerometer = runCatching {
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
            }.getOrDefault(false),
            hasGravity = runCatching {
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null
            }.getOrDefault(false),
            hasMagnetometer = runCatching {
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null
            }.getOrDefault(false),
            hasGyroscope = runCatching {
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null
            }.getOrDefault(false),
            hasGameRotationVector = runCatching {
                sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null
            }.getOrDefault(false)
        )
    }

    fun normalizeDegrees(angleDeg: Float): Float {
        if (!angleDeg.isFinite()) return 0f
        val mod = ((angleDeg % 360f) + 360f) % 360f
        return if (mod >= 360f || mod == -0f) 0f else mod
    }

    /**
     * Shortest signed angular delta from [fromDeg] to [toDeg] in [-180f, 180f].
     * Positive means turn clockwise (right); negative means turn counter-clockwise (left).
     */
    fun shortestSignedDelta(fromDeg: Float, toDeg: Float): Float {
        if (!fromDeg.isFinite() || !toDeg.isFinite()) return 0f
        var diff = normalizeDegrees(toDeg) - normalizeDegrees(fromDeg)
        while (diff <= -180f) diff += 360f
        while (diff > 180f) diff -= 360f
        return if (diff == -0f) 0f else diff
    }

    /**
     * Circular low-pass filter handling the 359° -> 0° boundary without whipping across the dial.
     * On the very first reading ([hasPreviousReading] == false), returns the normalized current value directly.
     */
    fun lowPassSmoothHeading(
        currentDeg: Float,
        previousDeg: Float,
        hasPreviousReading: Boolean,
        alpha: Float = 0.22f
    ): Float {
        if (!currentDeg.isFinite()) return if (hasPreviousReading) normalizeDegrees(previousDeg) else 0f
        val cleanCurrent = normalizeDegrees(currentDeg)
        if (!hasPreviousReading || !previousDeg.isFinite()) return cleanCurrent
        val cleanPrev = normalizeDegrees(previousDeg)
        val delta = shortestSignedDelta(cleanPrev, cleanCurrent)
        return normalizeDegrees(cleanPrev + alpha.coerceIn(0.01f, 1f) * delta)
    }

    /**
     * Updates an unwrapped continuous dial rotation angle (e.g. -trueHeading) so Compose's
     * animateFloatAsState never spins 358° backward when crossing 0°/360°.
     */
    fun unwrapContinuousAngle(
        currentTargetAngle: Float,
        previousUnwrappedAngle: Float,
        hasPrevious: Boolean
    ): Float {
        if (!currentTargetAngle.isFinite()) return if (hasPrevious) previousUnwrappedAngle else 0f
        if (!hasPrevious || !previousUnwrappedAngle.isFinite()) return currentTargetAngle
        val step = shortestSignedDelta(normalizeDegrees(previousUnwrappedAngle), normalizeDegrees(currentTargetAngle))
        return previousUnwrappedAngle + step
    }

    fun magneticToTrueHeading(magneticHeadingDeg: Float, declinationDeg: Float): Float {
        if (!magneticHeadingDeg.isFinite()) return 0f
        val decl = if (declinationDeg.isFinite()) declinationDeg else 0f
        return normalizeDegrees(magneticHeadingDeg + decl)
    }

    /**
     * Computes Assisted Direction true heading ONLY relative to a verified external reference
     * (e.g. calculated astronomical Sun Azimuth or user-verified True North).
     */
    fun computeAssistedTrueHeading(
        anchorTrueAzimuthDeg: Float,
        currentRelativeYawDeg: Float,
        anchorRelativeYawDeg: Float
    ): Float {
        if (!anchorTrueAzimuthDeg.isFinite() || !currentRelativeYawDeg.isFinite() || !anchorRelativeYawDeg.isFinite()) {
            return normalizeDegrees(anchorTrueAzimuthDeg)
        }
        val relativeTurn = shortestSignedDelta(anchorRelativeYawDeg, currentRelativeYawDeg)
        return normalizeDegrees(anchorTrueAzimuthDeg + relativeTurn)
    }

    /**
     * Computes real astronomical Sun azimuth (degrees clockwise from True North) and altitude
     * (degrees above horizon) using standard USNO/NOAA low-precision solar ephemeris (~0.5° accuracy).
     * 100% offline pure math. Usable as an external heading reference when altitude is in [2°, 85°].
     */
    fun calculateSolarPosition(
        latitudeDeg: Double,
        longitudeDeg: Double,
        epochMillis: Long
    ): SolarPosition {
        if (!latitudeDeg.isFinite() || !longitudeDeg.isFinite()) {
            return SolarPosition(0f, -90f, false)
        }
        val jd = epochMillis / 86_400_000.0 + 2_440_587.5
        val n = jd - 2_451_545.0

        val meanLongDeg = ((280.460 + 0.9856474 * n) % 360.0 + 360.0) % 360.0
        val meanAnomDeg = ((357.528 + 0.9856003 * n) % 360.0 + 360.0) % 360.0
        val meanAnomRad = Math.toRadians(meanAnomDeg)

        val eclipticLongDeg = meanLongDeg + 1.915 * sin(meanAnomRad) + 0.020 * sin(2.0 * meanAnomRad)
        val eclipticLongRad = Math.toRadians(eclipticLongDeg)
        val obliquityRad = Math.toRadians(23.439 - 0.0000004 * n)

        val sinDec = sin(obliquityRad) * sin(eclipticLongRad)
        val decRad = asin(sinDec.coerceIn(-1.0, 1.0))
        val raRad = atan2(cos(obliquityRad) * sin(eclipticLongRad), cos(eclipticLongRad))
        val raDeg = ((Math.toDegrees(raRad) % 360.0) + 360.0) % 360.0

        // Greenwich Mean Sidereal Time in degrees
        val gmstDeg = ((280.46061837 + 360.98564736629 * n) % 360.0 + 360.0) % 360.0
        val lstDeg = ((gmstDeg + longitudeDeg) % 360.0 + 360.0) % 360.0
        val hourAngleRad = Math.toRadians(lstDeg - raDeg)

        val latRad = Math.toRadians(latitudeDeg.coerceIn(-89.999, 89.999))
        val sinAlt = sin(latRad) * sin(decRad) + cos(latRad) * cos(decRad) * cos(hourAngleRad)
        val altRad = asin(sinAlt.coerceIn(-1.0, 1.0))
        val altDeg = Math.toDegrees(altRad).toFloat()

        // Azimuth clockwise from True North
        val y = -sin(hourAngleRad) * cos(decRad)
        val x = sin(decRad) * cos(latRad) - cos(decRad) * sin(latRad) * cos(hourAngleRad)
        val azDeg = normalizeDegrees(Math.toDegrees(atan2(y, x)).toFloat())

        val usable = altDeg.isFinite() && azDeg.isFinite() && altDeg in 2.0f..85.0f
        return SolarPosition(azimuthDeg = azDeg, altitudeDeg = altDeg, isUsableForReference = usable)
    }

    /**
     * Returns an 8-point cardinal sector label (N, NE, E, SE, S, SW, W, NW) for a bearing.
     */
    fun cardinalLabel(bearingDeg: Float): String {
        val norm = normalizeDegrees(bearingDeg)
        val sectors = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val index = (((norm + 22.5f) % 360f) / 45f).toInt().coerceIn(0, 7)
        return sectors[index]
    }

    /**
     * Maps Android [Surface] display rotation constants to [SensorManager.remapCoordinateSystem] axes.
     */
    fun getRemapAxesForDisplayRotation(displayRotation: Int): Pair<Int, Int> {
        return when (displayRotation) {
            Surface.ROTATION_90 -> SensorManager.AXIS_Y to SensorManager.AXIS_MINUS_X
            Surface.ROTATION_180 -> SensorManager.AXIS_MINUS_X to SensorManager.AXIS_MINUS_Y
            Surface.ROTATION_270 -> SensorManager.AXIS_MINUS_Y to SensorManager.AXIS_X
            else -> SensorManager.AXIS_X to SensorManager.AXIS_Y
        }
    }

    fun isSensorAccuracyAcceptable(accuracy: Int): Boolean {
        return accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH ||
            accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM
    }

    fun needsCalibration(accuracy: Int): Boolean {
        return accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE ||
            accuracy == SensorManager.SENSOR_STATUS_NO_CONTACT ||
            accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW
    }

    fun isAlignedWithQibla(
        hasCompassSensor: Boolean,
        hasValidReading: Boolean,
        sensorAccuracy: Int,
        trueHeadingDeg: Float,
        qiblaBearingDeg: Float
    ): Boolean {
        if (!hasCompassSensor || !hasValidReading) return false
        if (!isSensorAccuracyAcceptable(sensorAccuracy)) return false
        if (!trueHeadingDeg.isFinite() || !qiblaBearingDeg.isFinite()) return false
        val signedDiff = shortestSignedDelta(trueHeadingDeg, qiblaBearingDeg)
        return abs(signedDiff) <= ALIGNMENT_TOLERANCE_DEGREES
    }

    fun isValidSensorVector(values: FloatArray?, minLength: Int = 3): Boolean {
        if (values == null || values.size < minLength) return false
        for (i in 0 until minLength) {
            if (!values[i].isFinite()) return false
        }
        return true
    }

    fun hasNonZeroMagnitude(values: FloatArray?, minSquaredNorm: Float = 1e-4f): Boolean {
        if (values == null || values.size < 3) return false
        val sq = values[0] * values[0] + values[1] * values[1] + values[2] * values[2]
        return sq.isFinite() && sq >= minSquaredNorm
    }

    /**
     * Extracts display-rotation-corrected azimuth in [0f, 360f) from a 9-element rotation matrix.
     */
    fun extractAzimuthFromRotationMatrix(
        rotationMatrix: FloatArray,
        displayRotation: Int
    ): Float? {
        if (rotationMatrix.size < 9) return null
        val (axisX, axisY) = getRemapAxesForDisplayRotation(displayRotation)
        val remapped = FloatArray(9)
        val mapped = SensorManager.remapCoordinateSystem(rotationMatrix, axisX, axisY, remapped)
        val matrixToUse = if (mapped) remapped else rotationMatrix
        val orientation = FloatArray(3)
        SensorManager.getOrientation(matrixToUse, orientation)
        val azimuthRad = orientation[0]
        if (!azimuthRad.isFinite()) return null
        val deg = Math.toDegrees(azimuthRad.toDouble()).toFloat()
        return if (deg.isFinite()) normalizeDegrees(deg) else null
    }

    /**
     * Extracts azimuth handling both flat and tilted/upright phone postures along with screen rotation.
     * When the phone is held steeply upright (|pitch| > 60°), remaps the camera/back-face axis so
     * compass azimuth doesn't suffer gimbal lock.
     */
    fun extractTiltAwareAzimuthFromRotationMatrix(
        rotationMatrix: FloatArray,
        displayRotation: Int
    ): Float? {
        if (rotationMatrix.size < 9) return null
        val (baseX, baseY) = getRemapAxesForDisplayRotation(displayRotation)
        val displayRemapped = FloatArray(9)
        val mappedDisplay = SensorManager.remapCoordinateSystem(rotationMatrix, baseX, baseY, displayRemapped)
        val active = if (mappedDisplay) displayRemapped else rotationMatrix

        val orientation = FloatArray(3)
        SensorManager.getOrientation(active, orientation)
        val pitchRad = orientation[1]
        if (!pitchRad.isFinite()) return null

        val uprightThresholdRad = Math.toRadians(60.0).toFloat()
        val finalOrientation = if (abs(pitchRad) > uprightThresholdRad) {
            val uprightRemapped = FloatArray(9)
            val uprightMapped = SensorManager.remapCoordinateSystem(
                active,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                uprightRemapped
            )
            if (uprightMapped) {
                val out = FloatArray(3)
                SensorManager.getOrientation(uprightRemapped, out)
                out
            } else {
                orientation
            }
        } else {
            orientation
        }

        val azimuthRad = finalOrientation[0]
        if (!azimuthRad.isFinite()) return null
        val deg = Math.toDegrees(azimuthRad.toDouble()).toFloat()
        return if (deg.isFinite()) normalizeDegrees(deg) else null
    }

    fun validateManualCoordinates(latText: String, lonText: String): CoordinateValidationResult {
        val latTrim = latText.trim()
        val lonTrim = lonText.trim()
        val lat = latTrim.toDoubleOrNull()
        val lon = lonTrim.toDoubleOrNull()

        val latErr = when {
            latTrim.isEmpty() || lat == null || !lat.isFinite() -> "qibla_error_invalid_number"
            lat < -90.0 || lat > 90.0 -> "qibla_error_lat_range"
            else -> null
        }
        val lonErr = when {
            lonTrim.isEmpty() || lon == null || !lon.isFinite() -> "qibla_error_invalid_number"
            lon < -180.0 || lon > 180.0 -> "qibla_error_lon_range"
            else -> null
        }
        return CoordinateValidationResult(
            isValid = (latErr == null && lonErr == null),
            latitude = if (latErr == null) lat else null,
            longitude = if (lonErr == null) lon else null,
            latErrorKey = latErr,
            lonErrorKey = lonErr
        )
    }
}
