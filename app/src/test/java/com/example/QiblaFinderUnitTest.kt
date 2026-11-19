package com.example

import android.hardware.SensorManager
import android.view.Surface
import com.example.data.location.AppLocationManager
import com.example.data.location.CompassMath
import com.example.data.model.AppLanguage
import com.example.data.repository.PrayerCalculator
import com.example.localization.AppLocalization
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QiblaFinderUnitTest {

    @Test
    fun qiblaBearing_globalCitiesAndKaaba_areAccurate() {
        val faisalabad = PrayerCalculator.calculateQiblaBearing(31.4504, 73.1350)
        val karachi = PrayerCalculator.calculateQiblaBearing(24.8607, 67.0011)
        val lahore = PrayerCalculator.calculateQiblaBearing(31.5204, 74.3587)
        val london = PrayerCalculator.calculateQiblaBearing(51.5074, -0.1278)
        val newYork = PrayerCalculator.calculateQiblaBearing(40.7128, -74.0060)
        val tokyo = PrayerCalculator.calculateQiblaBearing(35.6762, 139.6503)
        val makkah = PrayerCalculator.calculateQiblaBearing(
            PrayerCalculator.KAABA_LAT,
            PrayerCalculator.KAABA_LON
        )

        assertEquals(259.6, faisalabad, 1.0)
        assertEquals(267.6, karachi, 1.0)
        assertEquals(260.3, lahore, 1.0)
        assertEquals(119.0, london, 1.0)
        assertEquals(58.5, newYork, 1.0)
        assertEquals(293.0, tokyo, 1.0)
        assertEquals(0.0, makkah, 0.0001)

        // All results normalized strictly in [0, 360)
        listOf(faisalabad, karachi, lahore, london, newYork, tokyo, makkah).forEach { b ->
            assertTrue("Bearing $b must be >= 0", b >= 0.0)
            assertTrue("Bearing $b must be < 360", b < 360.0)
        }
    }

    @Test
    fun qiblaBearing_invalidAndPolarCoordinates_handledSafely() {
        assertEquals(0.0, PrayerCalculator.calculateQiblaBearing(Double.NaN, 67.0), 0.0001)
        assertEquals(0.0, PrayerCalculator.calculateQiblaBearing(24.0, Double.POSITIVE_INFINITY), 0.0001)
        assertEquals(0.0, PrayerCalculator.calculateQiblaBearing(91.0, 10.0), 0.0001)
        assertEquals(0.0, PrayerCalculator.calculateQiblaBearing(10.0, -181.0), 0.0001)
        assertEquals(180.0, PrayerCalculator.calculateQiblaBearing(90.0, 0.0), 0.0001)
        assertEquals(0.0, PrayerCalculator.calculateQiblaBearing(-90.0, 0.0), 0.0001)

        // Antimeridian continuity check: 179.9 and -179.9 are 0.2° apart and yield nearly equal bearings
        val eastAntimeridian = PrayerCalculator.calculateQiblaBearing(10.0, 179.9)
        val westAntimeridian = PrayerCalculator.calculateQiblaBearing(10.0, -179.9)
        val diff = kotlin.math.abs(
            CompassMath.shortestSignedDelta(eastAntimeridian.toFloat(), westAntimeridian.toFloat())
        )
        assertTrue("Antimeridian bearings should be smooth (diff=$diff)", diff < 1.0f)
    }

    @Test
    fun kaabaDistance_offlineHaversineCalculation_isAccurate() {
        assertEquals(0.0, PrayerCalculator.calculateDistanceToKaabaKm(PrayerCalculator.KAABA_LAT, PrayerCalculator.KAABA_LON), 0.01)

        val karachiDist = PrayerCalculator.calculateDistanceToKaabaKm(24.8607, 67.0011)
        val londonDist = PrayerCalculator.calculateDistanceToKaabaKm(51.5074, -0.1278)
        val newYorkDist = PrayerCalculator.calculateDistanceToKaabaKm(40.7128, -74.0060)
        val tokyoDist = PrayerCalculator.calculateDistanceToKaabaKm(35.6762, 139.6503)

        assertEquals(2805.0, karachiDist, 40.0)
        assertEquals(4785.0, londonDist, 40.0)
        assertEquals(10315.0, newYorkDist, 50.0)
        assertEquals(9500.0, tokyoDist, 50.0)

        assertEquals(0.0, PrayerCalculator.calculateDistanceToKaabaKm(Double.NaN, 0.0), 0.0001)
    }

    @Test
    fun compassWrapAroundSmoothing_andContinuousUnwrap_neverJumpBackward() {
        // First reading returns exact current without zero-lag
        val first = CompassMath.lowPassSmoothHeading(currentDeg = 275f, previousDeg = 0f, hasPreviousReading = false)
        assertEquals(275f, first, 0.001f)

        // Crossing 359 -> 1 clockwise (+2 deg delta): result stays near 359.44, NEVER ~180
        val acrossNorthCw = CompassMath.lowPassSmoothHeading(
            currentDeg = 1f,
            previousDeg = 359f,
            hasPreviousReading = true,
            alpha = 0.25f
        )
        assertEquals(359.5f, acrossNorthCw, 0.01f)

        // Crossing 1 -> 359 counter-clockwise (-2 deg delta): result stays near 0.5
        val acrossNorthCcw = CompassMath.lowPassSmoothHeading(
            currentDeg = 359f,
            previousDeg = 1f,
            hasPreviousReading = true,
            alpha = 0.25f
        )
        assertEquals(0.5f, acrossNorthCcw, 0.01f)

        // Continuous unwrapped dial target: -359 deg moving to -1 deg (359° -> 1° heading) continues to -361 deg
        val unwrapped = CompassMath.unwrapContinuousAngle(
            currentTargetAngle = -1f,
            previousUnwrappedAngle = -359f,
            hasPrevious = true
        )
        assertEquals(-361f, unwrapped, 0.01f)
    }

    @Test
    fun compassAlignment_declination_andDisplayRotationAxes() {
        assertEquals(5.0f, CompassMath.magneticToTrueHeading(358.0f, 7.0f), 0.01f)
        assertEquals(355.0f, CompassMath.magneticToTrueHeading(2.0f, -7.0f), 0.01f)

        assertEquals(SensorManager.AXIS_X to SensorManager.AXIS_Y, CompassMath.getRemapAxesForDisplayRotation(Surface.ROTATION_0))
        assertEquals(SensorManager.AXIS_Y to SensorManager.AXIS_MINUS_X, CompassMath.getRemapAxesForDisplayRotation(Surface.ROTATION_90))
        assertEquals(SensorManager.AXIS_MINUS_X to SensorManager.AXIS_MINUS_Y, CompassMath.getRemapAxesForDisplayRotation(Surface.ROTATION_180))
        assertEquals(SensorManager.AXIS_MINUS_Y to SensorManager.AXIS_X, CompassMath.getRemapAxesForDisplayRotation(Surface.ROTATION_270))

        // Alignment must NEVER report true before reading or on low/unreliable sensor
        assertFalse(CompassMath.isAlignedWithQibla(false, true, SensorManager.SENSOR_STATUS_ACCURACY_HIGH, 260f, 260f))
        assertFalse(CompassMath.isAlignedWithQibla(true, false, SensorManager.SENSOR_STATUS_ACCURACY_HIGH, 260f, 260f))
        assertFalse(CompassMath.isAlignedWithQibla(true, true, SensorManager.SENSOR_STATUS_UNRELIABLE, 260f, 260f))
        assertFalse(CompassMath.isAlignedWithQibla(true, true, SensorManager.SENSOR_STATUS_ACCURACY_LOW, 260f, 260f))

        assertTrue(CompassMath.isAlignedWithQibla(true, true, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM, 258f, 260f))
        assertTrue(CompassMath.isAlignedWithQibla(true, true, SensorManager.SENSOR_STATUS_ACCURACY_HIGH, 359f, 1.5f))
        assertFalse(CompassMath.isAlignedWithQibla(true, true, SensorManager.SENSOR_STATUS_ACCURACY_HIGH, 250f, 260f))
    }

    @Test
    fun sensorVectorValidation_rejectsNaNInfinityZeroAndShortArrays() {
        assertFalse(CompassMath.isValidSensorVector(null))
        assertFalse(CompassMath.isValidSensorVector(floatArrayOf(1f, 2f)))
        assertFalse(CompassMath.isValidSensorVector(floatArrayOf(Float.NaN, 0f, 1f)))
        assertFalse(CompassMath.isValidSensorVector(floatArrayOf(0f, Float.POSITIVE_INFINITY, 1f)))
        assertTrue(CompassMath.isValidSensorVector(floatArrayOf(0.1f, -0.2f, 0.9f)))

        assertFalse(CompassMath.hasNonZeroMagnitude(floatArrayOf(0f, 0f, 0f)))
        assertTrue(CompassMath.hasNonZeroMagnitude(floatArrayOf(0f, 9.8f, 0f)))
    }

    @Test
    fun manualCoordinateValidation_enforcesBoundsAndRejectsInvalidText() {
        val valid = CompassMath.validateManualCoordinates(" 21.4225 ", " 39.8252 ")
        assertTrue(valid.isValid)
        assertEquals(21.4225, valid.latitude!!, 0.0001)
        assertEquals(39.8252, valid.longitude!!, 0.0001)
        assertNull(valid.latErrorKey)
        assertNull(valid.lonErrorKey)

        val invalidText = CompassMath.validateManualCoordinates("abc", "")
        assertFalse(invalidText.isValid)
        assertEquals("qibla_error_invalid_number", invalidText.latErrorKey)
        assertEquals("qibla_error_invalid_number", invalidText.lonErrorKey)

        val outOfRange = CompassMath.validateManualCoordinates("95.0", "-181.5")
        assertFalse(outOfRange.isValid)
        assertEquals("qibla_error_lat_range", outOfRange.latErrorKey)
        assertEquals("qibla_error_lon_range", outOfRange.lonErrorKey)

        val boundary = CompassMath.validateManualCoordinates("-90", "180")
        assertTrue(boundary.isValid)
    }

    @Test
    fun citySearch_supportsEnglishUrduArabicHindiAndCountryQueries() {
        assertTrue(AppLocationManager.searchCities("Karachi").any { it.nameEn == "Karachi" })
        assertTrue(AppLocationManager.searchCities("کراچی").any { it.nameEn == "Karachi" })
        assertTrue(AppLocationManager.searchCities("كراتشي").any { it.nameEn == "Karachi" })
        assertTrue(AppLocationManager.searchCities("कराची").any { it.nameEn == "Karachi" })
        assertTrue(AppLocationManager.searchCities("United Kingdom").any { it.nameEn == "London" })
        assertTrue(AppLocationManager.searchCities("لندن").any { it.nameEn == "London" })
        assertTrue(AppLocationManager.searchCities("लंदन").any { it.nameEn == "London" })
    }

    @Test
    fun allQiblaLocalizationKeys_presentInAllFourLanguages() {
        val qiblaKeys = listOf(
            "qibla_perm_title",
            "qibla_perm_desc",
            "qibla_perm_allow",
            "qibla_perm_denied_toast",
            "qibla_gps_off_title",
            "qibla_gps_off_desc",
            "qibla_gps_turn_on",
            "qibla_update_gps_cd",
            "qibla_change_location",
            "qibla_source_gps",
            "qibla_source_approximate",
            "qibla_source_manual",
            "qibla_source_estimate",
            "qibla_calibration_title",
            "qibla_calibration_desc",
            "qibla_aligned_banner",
            "qibla_angle_label",
            "qibla_calculated_bearing",
            "qibla_calculated_only_badge",
            "qibla_distance_label",
            "qibla_km_unit",
            "qibla_metrics_title",
            "qibla_true_bearing",
            "qibla_current_heading",
            "qibla_difference_label",
            "qibla_turn_right",
            "qibla_turn_left",
            "qibla_turn_aligned",
            "qibla_magnetic_declination",
            "qibla_sensor_mode_label",
            "qibla_sensor_rotation_vector",
            "qibla_sensor_accel_mag",
            "qibla_sensor_unavailable",
            "qibla_accuracy_label",
            "qibla_acc_high",
            "qibla_acc_medium",
            "qibla_acc_low",
            "qibla_acc_unreliable",
            "qibla_acc_waiting",
            "qibla_no_sensor_title",
            "qibla_no_sensor_desc",
            "qibla_no_sensor_guide",
            "qibla_offline_note",
            "qibla_city_dialog_title",
            "qibla_city_search_hint",
            "qibla_no_city_results",
            "qibla_custom_coords_cd",
            "qibla_close",
            "qibla_manual_title",
            "qibla_manual_city_label",
            "qibla_manual_lat_label",
            "qibla_manual_lon_label",
            "qibla_apply_coords",
            "qibla_cancel",
            "qibla_error_invalid_number",
            "qibla_error_lat_range",
            "qibla_error_lon_range",
            "qibla_north_short",
            "qibla_south_short",
            "qibla_east_short",
            "qibla_west_short"
        )
        val langs = listOf(AppLanguage.ENGLISH, AppLanguage.URDU, AppLanguage.ARABIC, AppLanguage.HINDI)
        langs.forEach { lang ->
            qiblaKeys.forEach { key ->
                val text = AppLocalization.get(key, lang)
                assertNotNull(text)
                assertTrue("Missing translation for $key in $lang", text.isNotBlank() && text != key)
            }
        }
    }
}
