package com.example

import android.Manifest
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.LocationManager
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.example.data.location.AppLocationManager
import com.example.data.location.LocationSource
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.screens.QiblaScreen
import com.example.ui.theme.NooreHidayatTheme
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.SensorEventBuilder
import org.robolectric.shadows.ShadowSensor
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class QiblaFinderRobolectricTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application
    private lateinit var originalTimeZone: TimeZone

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        originalTimeZone = TimeZone.getDefault()
        appContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        AppLocationManager.resetForTest()
    }

    @After
    fun tearDown() {
        TimeZone.setDefault(originalTimeZone)
        AppLocationManager.resetForTest()
    }

    @Test
    fun directQiblaEntry_restoresSavedManualLocationWithoutAssumingKarachi() {
        // Persist Tokyo manually in SharedPreferences as if from a previous app session
        val prefs = appContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putFloat("cached_latitude", 35.6762f)
            .putFloat("cached_longitude", 139.6503f)
            .putString("cached_city", "Tokyo")
            .putString("cached_country", "Japan")
            .putString("cached_timezone", "Asia/Tokyo")
            .putBoolean("cached_is_gps", false)
            .putString("cached_source_type", LocationSource.MANUAL.name)
            .commit()

        // Launch QiblaScreen directly without visiting PrayerTimesScreen first
        composeRule.setContent {
            NooreHidayatTheme {
                QiblaScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        assertEquals("Tokyo", AppLocationManager.currentLocation.value.cityName)
        assertEquals(LocationSource.MANUAL, AppLocationManager.currentLocation.value.sourceType)
        composeRule.onNodeWithText("Tokyo, Japan").assertIsDisplayed()
        composeRule.onNodeWithTag("qibla_location_source_badge").assertIsDisplayed()
        composeRule.onNodeWithTag("qibla_distance_text").assertIsDisplayed()
    }

    @Test
    fun noSavedLocation_doesNotAssumeKarachiAsDefault() {
        TimeZone.setDefault(TimeZone.getTimeZone("Pacific/Honolulu"))
        AppLocationManager.init(appContext)
        val initial = AppLocationManager.currentLocation.value
        assertNotEquals("Must not hardcode Karachi as global default", "Karachi", initial.cityName)
        assertEquals(LocationSource.DEFAULT_ESTIMATE, initial.sourceType)
    }

    @Test
    fun permissionDenied_showsPermissionPrompt() {
        val lm = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Shadows.shadowOf(lm).setProviderEnabled(LocationManager.GPS_PROVIDER, false)
        Shadows.shadowOf(lm).setProviderEnabled(LocationManager.NETWORK_PROVIDER, false)

        composeRule.setContent {
            NooreHidayatTheme {
                QiblaScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("qibla_permission_card").assertIsDisplayed()
    }

    @Test
    fun coarsePermissionAndGpsDisabled_showsGpsAlertHonestAccuracy() {
        val lm = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Shadows.shadowOf(lm).setProviderEnabled(LocationManager.GPS_PROVIDER, false)
        Shadows.shadowOf(lm).setProviderEnabled(LocationManager.NETWORK_PROVIDER, false)
        Shadows.shadowOf(appContext).grantPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)

        composeRule.setContent {
            NooreHidayatTheme {
                QiblaScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()
        assertTrue(AppLocationManager.hasCoarseLocationPermission(appContext))
        assertFalse(AppLocationManager.hasFineLocationPermission(appContext))
        composeRule.onNodeWithTag("qibla_gps_off_card").assertIsDisplayed()
    }

    @Test
    fun missingMagnetometer_showsSensorUnavailableModeWithCalculatedBearingOnly() {
        // Default Robolectric SensorManager has no registered sensors -> compassMode == UNAVAILABLE
        composeRule.setContent {
            NooreHidayatTheme {
                QiblaScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("qibla_unavailable_card").assertIsDisplayed()
        composeRule.onNodeWithTag("qibla_calculated_only_badge").assertIsDisplayed()
        assertEquals(0, composeRule.onAllNodesWithTag("qibla_compass_dial").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithTag("qibla_aligned_banner").fetchSemanticsNodes().size)
    }

    @Test
    fun rotationVectorSensor_handlesInvalidNaNReadingsAndValidCompassEvents() {
        val sm = appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotSensor = ShadowSensor.newInstance(Sensor.TYPE_ROTATION_VECTOR)
        Shadows.shadowOf(sm).addSensor(rotSensor)

        // Save New York (Qibla bearing ~58.5°)
        val ny = AppLocationManager.PRESET_CITIES.first { it.nameEn == "New York" }
        AppLocationManager.selectPresetCity(appContext, ny)

        composeRule.setContent {
            NooreHidayatTheme {
                QiblaScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // Live compass dial should be displayed, and NO false alignment before any sensor event
        composeRule.onNodeWithTag("qibla_compass_dial").assertIsDisplayed()
        assertEquals(0, composeRule.onAllNodesWithTag("qibla_aligned_banner").fetchSemanticsNodes().size)

        // Dispatch invalid NaN sensor frame -> must not crash or mark reading valid
        val nanEvent = SensorEventBuilder.newBuilder()
            .setSensor(rotSensor)
            .setValues(floatArrayOf(Float.NaN, 0f, 0f))
            .build()
        Shadows.shadowOf(sm).sendSensorEventToListeners(nanEvent)
        composeRule.waitForIdle()
        assertEquals(0, composeRule.onAllNodesWithTag("qibla_aligned_banner").fetchSemanticsNodes().size)

        // Dispatch a valid identity rotation vector (heading ~0° Magnetic North)
        val validEvent = SensorEventBuilder.newBuilder()
            .setSensor(rotSensor)
            .setValues(floatArrayOf(0f, 0f, 0f, 1f))
            .build()
        Shadows.shadowOf(sm).sendSensorEventToListeners(validEvent)
        composeRule.waitForIdle()

        // Metrics card should now show actual numeric heading & sensor mode text
        composeRule.onNodeWithTag("qibla_metrics_card").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText(
            AppLocalization.get("qibla_sensor_rotation_vector", AppLanguage.ENGLISH),
            substring = true
        )
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun missingRotationVector_fallsBackToAccelerometerAndMagnetometer() {
        val sm = appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelSensor = ShadowSensor.newInstance(Sensor.TYPE_ACCELEROMETER)
        val magSensor = ShadowSensor.newInstance(Sensor.TYPE_MAGNETIC_FIELD)
        val shadowSm = Shadows.shadowOf(sm)
        shadowSm.addSensor(accelSensor)
        shadowSm.addSensor(magSensor)

        composeRule.setContent {
            NooreHidayatTheme {
                QiblaScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("qibla_compass_dial").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText(
            AppLocalization.get("qibla_sensor_accel_mag", AppLanguage.ENGLISH),
            substring = true
        )
            .performScrollTo()
            .assertIsDisplayed()

        // Send realistic flat phone gravity + magnetic field pointing North
        val accelEvent = SensorEventBuilder.newBuilder()
            .setSensor(accelSensor)
            .setValues(floatArrayOf(0f, 0f, 9.81f))
            .build()
        val magEvent = SensorEventBuilder.newBuilder()
            .setSensor(magSensor)
            .setValues(floatArrayOf(0f, 25f, -40f))
            .build()
        shadowSm.sendSensorEventToListeners(accelEvent)
        shadowSm.sendSensorEventToListeners(magEvent)
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("qibla_metrics_card").assertIsDisplayed()
    }

    @Test
    fun manualCitySearchAndCoordinateValidation_rejectsOutOfRangeAndAppliesValid() {
        composeRule.setContent {
            NooreHidayatTheme {
                QiblaScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // Open City Picker dialog
        composeRule.onNodeWithTag("qibla_change_location_btn").performClick()
        composeRule.waitForIdle()

        // Open Manual Coordinates dialog via Pin button
        composeRule.onNodeWithTag("qibla_open_manual_coords_btn").performClick()
        composeRule.waitForIdle()

        // Enter invalid latitude 95.0 -> Apply must fail and display error message
        composeRule.onNodeWithTag("qibla_manual_lat_field").performTextClearance()
        composeRule.onNodeWithTag("qibla_manual_lat_field").performTextInput("95.0")
        composeRule.onNodeWithTag("qibla_apply_coords_btn").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("qibla_lat_error_text", useUnmergedTree = true).assertExists()
        assertNotEquals(95.0, AppLocationManager.currentLocation.value.latitude, 0.1)

        // Enter valid coordinates for London (51.5074, -0.1278) and Apply
        composeRule.onNodeWithTag("qibla_manual_city_field").performTextClearance()
        composeRule.onNodeWithTag("qibla_manual_city_field").performTextInput("London Test")
        composeRule.onNodeWithTag("qibla_manual_lat_field").performTextClearance()
        composeRule.onNodeWithTag("qibla_manual_lat_field").performTextInput("51.5074")
        composeRule.onNodeWithTag("qibla_manual_lon_field").performTextClearance()
        composeRule.onNodeWithTag("qibla_manual_lon_field").performTextInput("-0.1278")
        composeRule.onNodeWithTag("qibla_apply_coords_btn").performClick()
        composeRule.waitForIdle()

        assertEquals("London Test", AppLocationManager.currentLocation.value.cityName)
        assertEquals(51.5074, AppLocationManager.currentLocation.value.latitude, 0.001)
        assertEquals(-0.1278, AppLocationManager.currentLocation.value.longitude, 0.001)
        assertEquals(LocationSource.MANUAL, AppLocationManager.currentLocation.value.sourceType)
    }

    @Test
    fun qiblaScreen_rendersAllFourLanguagesProperly() {
        val languages = listOf(AppLanguage.ENGLISH, AppLanguage.URDU, AppLanguage.ARABIC, AppLanguage.HINDI)
        val sm = appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Shadows.shadowOf(sm).addSensor(ShadowSensor.newInstance(Sensor.TYPE_ROTATION_VECTOR))
        val activeLang = androidx.compose.runtime.mutableStateOf(AppLanguage.ENGLISH)

        composeRule.setContent {
            NooreHidayatTheme {
                QiblaScreen(language = activeLang.value, onBack = {})
            }
        }

        languages.forEach { lang ->
            activeLang.value = lang
            composeRule.waitForIdle()

            composeRule.onNodeWithText(AppLocalization.get("qibla", lang)).assertIsDisplayed()
            composeRule.onNodeWithText(AppLocalization.get("qibla_change_location", lang)).performScrollTo().assertIsDisplayed()
            composeRule.onNodeWithText(AppLocalization.get("qibla_metrics_title", lang)).performScrollTo().assertIsDisplayed()
        }
    }
}
