package com.example.data.location

import android.hardware.SensorManager
import android.view.Surface
import kotlin.math.abs

/**
 * Pure Kotlin compass & orientation math helpers for Qibla Finder.
 * Handles 0°/360° wrap-around smoothing, continuous rotation angle unwrapping,
 * display-rotation coordinate remapping, sensor validation, and manual coordinate validation.
 */
object CompassMath {

    const val ALIGNMENT_TOLERANCE_DEGREES = 3.5f

    enum class CompassMode {
        ROTATION_VECTOR,
        ACCEL_MAG_FALLBACK,
        UNAVAILABLE
    }

    data class CoordinateValidationResult(
        val isValid: Boolean,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val latErrorKey: String? = null,
        val lonErrorKey: String? = null
    )

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
