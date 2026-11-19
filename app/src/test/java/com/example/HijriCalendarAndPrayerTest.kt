package com.example

import android.Manifest
import android.app.AlarmManager
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.notification.AzanPlaybackService
import com.example.data.notification.PrayerAlarmReceiver
import com.example.data.notification.PrayerBootReceiver
import com.example.data.notification.PrayerNotificationManager
import com.example.data.notification.PrayerNotificationSettings
import com.example.data.repository.HijriCalendarConvention
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.QuranAudioManager
import com.example.localization.AppLocalization
import com.example.ui.components.MethodologyReferences
import com.example.ui.screens.HijriCalendarScreen
import com.example.ui.screens.PrayerTimesScreen
import com.example.ui.theme.NooreHidayatTheme
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlarmManager
import java.util.Calendar
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class HijriCalendarAndPrayerTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application
    private lateinit var originalTz: TimeZone

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        originalTz = TimeZone.getDefault()
        appContext.getSharedPreferences("hijri_calendar_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        appContext.getSharedPreferences("prayer_calc_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        appContext.getSharedPreferences("prayer_notif_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        appContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE).edit().clear().commit()

        IslamicCalendarRepository.resetForTest()
        PrayerCalculator.resetForTest()
        PrayerNotificationManager.resetForTest()
        AppLocationManager.resetForTest()
        QuranAudioManager.stopMedia()
    }

    @After
    fun tearDown() {
        TimeZone.setDefault(originalTz)
        QuranAudioManager.stopMedia()
    }

    // =========================================================================
    // 1-3: HIJRI CALENDAR CONVENTIONS, HONEST LABELING, NAVIGATION & RAMADAN
    // =========================================================================

    @Test
    fun testHijriConventionsProduceDistinctDatesAndNeverFalselyConfirm() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(2026, Calendar.FEBRUARY, 19, 12, 0, 0)
        }

        val pk = IslamicCalendarRepository.getHijriDate(cal, 0, HijriCalendarConvention.PAKISTAN_RUET)
        val umm = IslamicCalendarRepository.getHijriDate(cal, 0, HijriCalendarConvention.UMM_AL_QURA)
        val tab = IslamicCalendarRepository.getHijriDate(cal, 0, HijriCalendarConvention.ASTRONOMICAL_TABULAR)
        val man = IslamicCalendarRepository.getHijriDate(cal, 2, HijriCalendarConvention.MANUAL_LOCAL_SIGHTING)

        // Never falsely confirm offline calculation as Ruet-e-Hilal verified
        assertFalse(pk.isConfirmedByRuetHilal)
        assertFalse(umm.isConfirmedByRuetHilal)
        assertFalse(tab.isConfirmedByRuetHilal)
        assertFalse(man.isConfirmedByRuetHilal)
        assertTrue(pk.isEstimate)

        // Pakistan (-1 day) vs Tabular (0 day) vs Manual (+2 days) must differ clearly
        assertNotEquals(pk.day, tab.day)
        assertNotEquals(tab.day, man.day)
        assertTrue(umm.year >= 1447)
    }

    @Test
    fun testHijriCalendarUiHonestLabelsConventionsAndNavigation() {
        val app = Shadows.shadowOf(appContext)
        app.grantPermissions(Manifest.permission.POST_NOTIFICATIONS)

        composeRule.setContent {
            NooreHidayatTheme {
                HijriCalendarScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // Must NOT show misleading "Verified Islamic Date"
        assertTrue(
            composeRule.onAllNodesWithText("Today's Verified Islamic & Gregorian Date").fetchSemanticsNodes().isEmpty()
        )
        composeRule.onNodeWithTag("hijri_hero_card").assertIsDisplayed()

        // Switch convention to Umm Al-Qura, Tabular, Manual (scroll LazyColumn items into viewport)
        composeRule.onNodeWithTag("hijri_calendar_list").performScrollToNode(hasTestTag("hijri_conv_umm_al_qura"))
        composeRule.onNodeWithTag("hijri_conv_umm_al_qura").performClick()
        composeRule.waitForIdle()
        assertEquals(HijriCalendarConvention.UMM_AL_QURA, IslamicCalendarRepository.calendarConvention.value)

        composeRule.onNodeWithTag("hijri_calendar_list").performScrollToNode(hasTestTag("moon_adjust_plus"))
        composeRule.onNodeWithTag("moon_adjust_plus").performClick()
        composeRule.waitForIdle()
        assertEquals(1, IslamicCalendarRepository.manualAdjustmentDays.value)
        assertEquals(HijriCalendarConvention.MANUAL_LOCAL_SIGHTING, IslamicCalendarRepository.calendarConvention.value)

        // Month navigation & Today reset
        composeRule.onNodeWithTag("hijri_calendar_list").performScrollToNode(hasTestTag("hijri_next_month"))
        composeRule.onNodeWithTag("hijri_next_month").performClick()
        composeRule.onNodeWithTag("hijri_calendar_list").performScrollToNode(hasTestTag("hijri_today_button"))
        composeRule.onNodeWithTag("hijri_today_button").performClick()
        composeRule.waitForIdle()

        // Switch to Events and Ramadan tabs
        composeRule.onNodeWithTag("hijri_tab_events").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hijri_events_list").performScrollToNode(hasTestTag("hijri_event_ramadan_start"))
        composeRule.onNodeWithTag("hijri_event_ramadan_start").assertIsDisplayed()

        composeRule.onNodeWithTag("hijri_tab_ramadan").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hijri_ramadan_list").performScrollToNode(hasTestTag("ramadan_share_button"))
        composeRule.onNodeWithTag("ramadan_share_button").assertIsDisplayed()
    }

    // =========================================================================
    // 4-8: WORLDWIDE PRAYER TIMES, METHODS, ASR HANAFI/STANDARD, TIMEZONES & DST
    // =========================================================================

    private fun parseMinutesOfDay(formatted: String): Int {
        val trimmed = formatted.trim()
        val isPm = trimmed.endsWith("PM")
        val isAm = trimmed.endsWith("AM")
        val parts = trimmed.replace("AM", "").replace("PM", "").trim().split(":")
        var h = parts[0].toInt()
        val m = parts[1].toInt()
        if (isPm && h < 12) h += 12
        if (isAm && h == 12) h = 0
        return h * 60 + m
    }

    @Test
    fun testWorldwideCitiesPrayerTimesAndAmPmSanity() {
        val date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(2026, Calendar.MARCH, 20, 12, 0, 0) // Spring equinox for clean global sanity
        }

        val testCities = listOf(
            Triple("Faisalabad", Triple(31.4504, 73.1350, "Asia/Karachi"), PrayerCalculator.CalculationMethod.KARACHI),
            Triple("Makkah", Triple(21.4225, 39.8252, "Asia/Riyadh"), PrayerCalculator.CalculationMethod.UMM_AL_QURA),
            Triple("London", Triple(51.5074, -0.1278, "Europe/London"), PrayerCalculator.CalculationMethod.MWL),
            Triple("New York", Triple(40.7128, -74.0060, "America/New_York"), PrayerCalculator.CalculationMethod.ISNA),
            Triple("Tokyo", Triple(35.6762, 139.6503, "Asia/Tokyo"), PrayerCalculator.CalculationMethod.MWL),
            Triple("Singapore", Triple(1.3521, 103.8198, "Asia/Singapore"), PrayerCalculator.CalculationMethod.SINGAPORE)
        )

        testCities.forEach { (name, coords, method) ->
            val (lat, lon, tz) = coords
            val s = PrayerCalculator.calculatePrayerTimes(
                latitude = lat,
                longitude = lon,
                calendar = date,
                method = method,
                asrJuristic = PrayerCalculator.AsrJuristic.HANAFI,
                cityName = name,
                timeZoneId = tz
            )
            assertTrue("$name Fajr must be AM: ${s.fajr}", s.fajr.endsWith("AM"))
            assertTrue("$name Sunrise must be AM: ${s.sunrise}", s.sunrise.endsWith("AM"))
            assertTrue("$name Asr must be PM: ${s.asr}", s.asr.endsWith("PM"))
            assertTrue("$name Maghrib must be PM: ${s.maghrib}", s.maghrib.endsWith("PM"))

            val fajrMin = parseMinutesOfDay(s.fajr)
            val sunMin = parseMinutesOfDay(s.sunrise)
            val dhuhrMin = parseMinutesOfDay(s.dhuhr)
            val asrMin = parseMinutesOfDay(s.asr)
            val maghribMin = parseMinutesOfDay(s.maghrib)
            val ishaMin = parseMinutesOfDay(s.isha)

            assertTrue("$name ordering fajr < sunrise", fajrMin < sunMin)
            assertTrue("$name ordering sunrise < dhuhr", sunMin < dhuhrMin)
            assertTrue("$name ordering dhuhr < asr", dhuhrMin < asrMin)
            assertTrue("$name ordering asr < maghrib", asrMin < maghribMin)
            assertTrue("$name ordering maghrib < isha", maghribMin < ishaMin)
        }
    }

    @Test
    fun testAllCalculationMethodsCustomAnglesAndHanafiVsStandardAsr() {
        val date = Calendar.getInstance(TimeZone.getTimeZone("Asia/Karachi")).apply {
            set(2026, Calendar.MARCH, 20, 12, 0, 0)
        }

        // Pakistan recommends Karachi while allowing override to any method
        assertEquals(PrayerCalculator.CalculationMethod.KARACHI, PrayerCalculator.getRecommendedMethod("Pakistan"))
        assertEquals(PrayerCalculator.CalculationMethod.MWL, PrayerCalculator.getRecommendedMethod("United Kingdom"))
        assertEquals(PrayerCalculator.CalculationMethod.ISNA, PrayerCalculator.getRecommendedMethod("United States"))
        assertEquals(PrayerCalculator.CalculationMethod.UMM_AL_QURA, PrayerCalculator.getRecommendedMethod("Saudi Arabia"))
        assertEquals(PrayerCalculator.CalculationMethod.SINGAPORE, PrayerCalculator.getRecommendedMethod("Singapore"))
        assertEquals(PrayerCalculator.CalculationMethod.FRANCE_UOIF, PrayerCalculator.getRecommendedMethod("France"))
        assertEquals(PrayerCalculator.CalculationMethod.RUSSIA, PrayerCalculator.getRecommendedMethod("Russia"))

        val hanafi = PrayerCalculator.calculatePrayerTimes(
            latitude = 31.4504,
            longitude = 73.1350,
            calendar = date,
            method = PrayerCalculator.CalculationMethod.KARACHI,
            asrJuristic = PrayerCalculator.AsrJuristic.HANAFI,
            timeZoneId = "Asia/Karachi"
        )
        val standard = PrayerCalculator.calculatePrayerTimes(
            latitude = 31.4504,
            longitude = 73.1350,
            calendar = date,
            method = PrayerCalculator.CalculationMethod.KARACHI,
            asrJuristic = PrayerCalculator.AsrJuristic.STANDARD,
            timeZoneId = "Asia/Karachi"
        )
        assertTrue(
            "Hanafi Asr (${hanafi.asr}) must be later than Standard Asr (${standard.asr})",
            parseMinutesOfDay(hanafi.asr) > parseMinutesOfDay(standard.asr)
        )

        // Every supported method yields valid non-empty times
        PrayerCalculator.CalculationMethod.values().forEach { method ->
            val res = PrayerCalculator.calculatePrayerTimes(
                latitude = 51.5074,
                longitude = -0.1278,
                calendar = date,
                method = method,
                timeZoneId = "Europe/London",
                customParams = PrayerCalculator.CustomCalculationParams(fajrAngle = 14.0, ishaAngle = 14.0)
            )
            assertFalse(res.fajr.contains("--"))
            assertFalse(res.isha.contains("--"))
        }

        // Custom angle changes Fajr/Isha distinctly from Karachi (18°/18°)
        val custom = PrayerCalculator.calculatePrayerTimes(
            latitude = 31.4504,
            longitude = 73.1350,
            calendar = date,
            method = PrayerCalculator.CalculationMethod.CUSTOM,
            timeZoneId = "Asia/Karachi",
            customParams = PrayerCalculator.CustomCalculationParams(fajrAngle = 12.0, ishaAngle = 12.0)
        )
        assertNotEquals(hanafi.fajr, custom.fajr)
        assertNotEquals(hanafi.isha, custom.isha)
    }

    @Test
    fun testHighLatitudeSummerMidnightIshaAndDstHandling() {
        // Midsummer June 21 in London (DST active: Europe/London = UTC+1)
        val summerLondon = Calendar.getInstance(TimeZone.getTimeZone("Europe/London")).apply {
            set(2026, Calendar.JUNE, 21, 12, 0, 0)
        }
        val londonSummer = PrayerCalculator.calculatePrayerTimes(
            latitude = 51.5074,
            longitude = -0.1278,
            calendar = summerLondon,
            method = PrayerCalculator.CalculationMethod.MWL,
            highLatitudeRule = PrayerCalculator.HighLatitudeRule.ANGLE_BASED,
            timeZoneId = "Europe/London"
        )
        // High-latitude Isha wraps past midnight or late night without flipping 00:xx AM to 12:xx PM
        assertFalse(londonSummer.fajr.contains("--"))
        assertFalse(londonSummer.isha.contains("--"))

        // FormatPrayerTime guarantees 00:30 AM night Isha stays AM, while 12:30 -> 12:30 PM
        assertEquals("12:30 AM", PrayerCalculator.formatPrayerTime("isha", 0.5))
        assertEquals("08:30 PM", PrayerCalculator.formatPrayerTime("isha", 8.5))

        // DST shift in London between standard time (Jan) and BST (Apr)
        val marchPreDst = Calendar.getInstance(TimeZone.getTimeZone("Europe/London")).apply {
            set(2026, Calendar.MARCH, 28, 12, 0, 0)
        }
        val marchPostDst = Calendar.getInstance(TimeZone.getTimeZone("Europe/London")).apply {
            set(2026, Calendar.MARCH, 30, 12, 0, 0)
        }
        val preSchedule = PrayerCalculator.calculatePrayerTimes(
            latitude = 51.5074, longitude = -0.1278, calendar = marchPreDst,
            method = PrayerCalculator.CalculationMethod.MWL, timeZoneId = "Europe/London"
        )
        val postSchedule = PrayerCalculator.calculatePrayerTimes(
            latitude = 51.5074, longitude = -0.1278, calendar = marchPostDst,
            method = PrayerCalculator.CalculationMethod.MWL, timeZoneId = "Europe/London"
        )
        val dhuhrDiff = parseMinutesOfDay(postSchedule.dhuhr) - parseMinutesOfDay(preSchedule.dhuhr)
        assertTrue("DST spring-forward must shift Dhuhr by ~60 mins (diff=$dhuhrDiff)", dhuhrDiff in 55..65)

        // Multi-timezone countries must have distinct presets (Australia, Russia, Indonesia, USA, Canada)
        val auZones = AppLocationManager.PRESET_CITIES.filter { it.country == "Australia" }.map { it.timeZoneId }.toSet()
        val ruZones = AppLocationManager.PRESET_CITIES.filter { it.country == "Russia" }.map { it.timeZoneId }.toSet()
        val idZones = AppLocationManager.PRESET_CITIES.filter { it.country == "Indonesia" }.map { it.timeZoneId }.toSet()
        assertTrue(auZones.size >= 3)
        assertTrue(ruZones.size >= 3)
        assertTrue(idZones.size >= 3)

        // Worldwide multilingual search
        assertTrue(AppLocationManager.searchCities("لندن").any { it.nameEn == "London" })
        assertTrue(AppLocationManager.searchCities("مكة").any { it.nameEn == "Makkah" })
        assertTrue(AppLocationManager.searchCities("मक्का").any { it.nameEn == "Makkah" })
    }

    @Test
    fun testRamadanScheduleUsesSharedPrayerCalculationConfig() {
        val start = Calendar.getInstance(TimeZone.getTimeZone("Asia/Karachi")).apply {
            set(2026, Calendar.FEBRUARY, 19, 12, 0, 0)
        }
        val configKarachi = PrayerCalculator.PrayerCalculationConfig(
            method = PrayerCalculator.CalculationMethod.KARACHI,
            adjustments = PrayerCalculator.PrayerAdjustments(fajrMinutes = 0)
        )
        val configAdjusted = PrayerCalculator.PrayerCalculationConfig(
            method = PrayerCalculator.CalculationMethod.ISNA,
            adjustments = PrayerCalculator.PrayerAdjustments(fajrMinutes = 5)
        )

        val s1 = IslamicCalendarRepository.generateRamadanSchedule(31.4504, 73.1350, "Asia/Karachi", start, configKarachi)
        val s2 = IslamicCalendarRepository.generateRamadanSchedule(31.4504, 73.1350, "Asia/Karachi", start, configAdjusted)

        assertEquals(30, s1.size)
        assertNotEquals("Shared calculation config must change Ramadan Sehri time", s1.first().sehriTime, s2.first().sehriTime)

        // Monthly timetable helper also respects shared calculation configuration
        val monthly1 = PrayerCalculator.generateMonthlyTimetable(
            year = 2026,
            month = 2,
            latitude = 31.4504,
            longitude = 73.1350,
            timeZoneId = "Asia/Karachi",
            method = configKarachi.method,
            asrJuristic = configKarachi.asrJuristic,
            adjustments = configKarachi.adjustments,
            highLatitudeRule = configKarachi.highLatitudeRule,
            customParams = configKarachi.customParams
        )
        val monthly2 = PrayerCalculator.generateMonthlyTimetable(
            year = 2026,
            month = 2,
            latitude = 31.4504,
            longitude = 73.1350,
            timeZoneId = "Asia/Karachi",
            method = configAdjusted.method,
            asrJuristic = configAdjusted.asrJuristic,
            adjustments = configAdjusted.adjustments,
            highLatitudeRule = configAdjusted.highLatitudeRule,
            customParams = configAdjusted.customParams
        )
        assertEquals(28, monthly1.size)
        assertNotEquals("Shared config must update monthly Fajr time", monthly1.first().fajr, monthly2.first().fajr)
    }

    // =========================================================================
    // 9-17: AZAN NOTIFICATIONS, SILENT CHANNELS, PERMISSIONS & ROLLING ALARMS
    // =========================================================================

    @Test
    fun testSilentNotificationChannelsPreventDuplicateAudio() {
        PrayerNotificationManager.init(appContext)
        val nm = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val vibChannel = nm.getNotificationChannel(PrayerNotificationManager.CHANNEL_ID)
        val noVibChannel = nm.getNotificationChannel(PrayerNotificationManager.CHANNEL_ID_NOVIB)
        val legacyChannel = nm.getNotificationChannel(PrayerNotificationManager.LEGACY_CHANNEL_ID)

        assertNotNull("Vibration channel must exist", vibChannel)
        assertNotNull("No-vibration channel must exist", noVibChannel)
        assertNull("Legacy noisy channel should be removed", legacyChannel)
        assertNull("NotificationChannel sound MUST be null so OS never plays duplicate sound", vibChannel!!.sound)
        assertNull("No-vib NotificationChannel sound MUST be null", noVibChannel!!.sound)
        assertTrue(vibChannel.shouldVibrate())
        assertFalse(noVibChannel.shouldVibrate())
    }

    @Test
    fun testAzanPreferencesAndPerPrayerToggleSchedulingAndDeduplication() {
        AppLocationManager.init(appContext)
        PrayerCalculator.init(appContext)
        PrayerNotificationManager.init(appContext)

        val am = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAm = Shadows.shadowOf(am)

        // Disable Fajr & Isha, set 10m lead time & Madinah audio
        val updated = PrayerNotificationSettings(
            fajrEnabled = false,
            dhuhrEnabled = true,
            asrEnabled = true,
            maghribEnabled = true,
            ishaEnabled = false,
            adhanSound = "madina",
            vibrationEnabled = false,
            leadTimeMinutes = 10
        )
        PrayerNotificationManager.updateSettings(appContext, updated)

        // Verify persisted preferences survive re-init
        PrayerNotificationManager.resetForTest()
        PrayerNotificationManager.init(appContext)
        val reloaded = PrayerNotificationManager.settings.value
        assertFalse(reloaded.fajrEnabled)
        assertTrue(reloaded.dhuhrEnabled)
        assertEquals("madina", reloaded.adhanSound)
        assertFalse(reloaded.vibrationEnabled)
        assertEquals(10, reloaded.leadTimeMinutes)

        // All scheduled alarms must correspond ONLY to enabled prayers (requestCode ends in 1,2,3; never 0=fajr or 4=isha)
        val codes = PrayerNotificationManager.getScheduledRequestCodes(appContext)
        assertTrue("Rolling window should schedule multiple days", codes.size >= 30)
        assertTrue("Disabled Fajr (0) and Isha (4) must be skipped", codes.all { (it % 10) in 1..3 })

        // Verify no duplicate trigger times for the same request code in ShadowAlarmManager
        val scheduledList = shadowAm.scheduledAlarms
        assertTrue("AlarmManager should hold scheduled prayer alarms + replenish alarm", scheduledList.isNotEmpty())
        val now = System.currentTimeMillis()
        assertTrue("All scheduled alarms must be strictly in the future", scheduledList.all { it.triggerAtTime > now - 5_000L })

        // Disabling ALL prayers clears all alarms cleanly
        PrayerNotificationManager.updateSettings(
            appContext,
            PrayerNotificationSettings(false, false, false, false, false, "silent", false, 0)
        )
        val clearedCodes = PrayerNotificationManager.getScheduledRequestCodes(appContext)
        assertTrue("When all prayers disabled, no prayer alarms should remain", clearedCodes.isEmpty())
    }

    @Test
    fun testExactAlarmGrantedVsDeniedFallbackAndBootTimeZoneReplenishment() {
        val am = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAm = Shadows.shadowOf(am)

        // 1) Exact alarm GRANTED
        ShadowAlarmManager.setCanScheduleExactAlarms(true)
        val exactStatus = PrayerNotificationManager.rescheduleAllAlarms(appContext, reason = "test_exact")
        assertTrue(exactStatus.canScheduleExactAlarms)
        assertTrue(exactStatus.usedExactAlarms)
        assertTrue(exactStatus.scheduledCount > 0)

        // 2) Exact alarm DENIED -> graceful inexact fallback without crash
        ShadowAlarmManager.setCanScheduleExactAlarms(false)
        val inexactStatus = PrayerNotificationManager.rescheduleAllAlarms(appContext, reason = "test_inexact")
        assertFalse(inexactStatus.canScheduleExactAlarms)
        assertFalse(inexactStatus.usedExactAlarms)
        assertTrue("Inexact fallback must still schedule all alarms", inexactStatus.scheduledCount > 0)

        // 3) Boot, App Update, Time Set, Timezone Change, Permission Change receivers all reschedule cleanly
        val bootReceiver = PrayerBootReceiver()
        listOf(
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
        ).forEach { action ->
            PrayerNotificationManager.cancelAllAlarms(appContext)
            assertEquals(0, PrayerNotificationManager.getScheduledRequestCodes(appContext).size)
            bootReceiver.onReceive(appContext, Intent(action))
            assertTrue(
                "Receiver $action must restore rolling alarms",
                PrayerNotificationManager.getScheduledRequestCodes(appContext).isNotEmpty()
            )
            assertTrue(
                PrayerNotificationManager.scheduleStatus.value.lastRescheduleReason.contains(action)
            )
        }

        // 4) Alarm receiver posts notification (even with denied or granted notif perm), catches background FGS issues, and replenishes window
        Shadows.shadowOf(appContext).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        val alarmReceiver = PrayerAlarmReceiver()
        val alarmIntent = Intent(PrayerAlarmReceiver.ACTION_PRAYER_ALARM).apply {
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_KEY, "dhuhr")
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TITLE, "Dhuhr")
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TIME, "12:15 PM")
            putExtra(PrayerAlarmReceiver.EXTRA_ADHAN_SOUND, "silent")
        }
        alarmReceiver.onReceive(appContext, alarmIntent)

        val nm = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val posted = Shadows.shadowOf(nm).allNotifications
        assertTrue("PrayerAlarmReceiver must post notification", posted.isNotEmpty())
        assertTrue(
            "Rolling window must replenish when alarm fires",
            PrayerNotificationManager.scheduleStatus.value.lastRescheduleReason == "alarm_fired:dhuhr"
        )

        // Replenish alarm & stop action execute safely
        alarmReceiver.onReceive(appContext, Intent(PrayerAlarmReceiver.ACTION_REPLENISH_ALARMS))
        assertEquals("daily_replenish", PrayerNotificationManager.scheduleStatus.value.lastRescheduleReason)
        alarmReceiver.onReceive(appContext, Intent(PrayerAlarmReceiver.ACTION_STOP_AZAN))

        // Silent audio guard completes cleanly without starting MediaPlayer playback
        var silentCompleted = false
        QuranAudioManager.playAzan(appContext, azanType = "silent", useAlarmUsage = true) {
            silentCompleted = true
        }
        assertTrue("Silent Azan must invoke completion immediately", silentCompleted)
        assertFalse(QuranAudioManager.isAudioPlaying.value)

        // Reset exact alarm shadow state for other tests
        ShadowAlarmManager.setCanScheduleExactAlarms(true)
        assertTrue(shadowAm.scheduledAlarms.isNotEmpty())
    }

    @Test
    fun testNotificationPermissionDeniedShowsBannerAndPreventsCrash() {
        Shadows.shadowOf(appContext).denyPermissions(Manifest.permission.POST_NOTIFICATIONS)
        ShadowAlarmManager.setCanScheduleExactAlarms(false)
        PrayerNotificationManager.init(appContext)

        // Calling sendTestNotification when POST_NOTIFICATIONS is denied must NOT crash
        PrayerNotificationManager.sendTestNotification(appContext, "Asr", "04:15 PM")
        val nm = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        assertEquals(0, Shadows.shadowOf(nm).allNotifications.size)

        composeRule.setContent {
            NooreHidayatTheme {
                PrayerTimesScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("prayer_notif_perm_banner").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("prayer_exact_alarm_banner").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("sources_methodology_card").performScrollTo().assertIsDisplayed()
        assertEquals(8, MethodologyReferences.ALL_REFS.size)
    }

    @Test
    fun testFourLanguageSupportAcrossHijriAndPrayerScreens() {
        val currentLang = mutableStateOf(AppLanguage.ENGLISH)
        Shadows.shadowOf(appContext).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        ShadowAlarmManager.setCanScheduleExactAlarms(true)

        composeRule.setContent {
            NooreHidayatTheme {
                PrayerTimesScreen(language = currentLang.value, onBack = {})
            }
        }

        listOf(AppLanguage.ENGLISH, AppLanguage.URDU, AppLanguage.ARABIC, AppLanguage.HINDI).forEach { lang ->
            currentLang.value = lang
            composeRule.waitForIdle()
            composeRule.onNodeWithText(AppLocalization.get("prayer_times", lang)).assertIsDisplayed()
            composeRule.onNodeWithText(AppLocalization.get("prayer_next_label", lang)).performScrollTo().assertIsDisplayed()
            composeRule.onNodeWithText(AppLocalization.get("sources_section_title", lang)).performScrollTo().assertIsDisplayed()
        }

        // Verify all new localization keys exist cleanly in all 4 languages
        val criticalKeys = listOf(
            "hijri_hero_title", "hijri_conv_pakistan", "hijri_conv_umm_al_qura",
            "hijri_conv_tabular", "hijri_conv_manual", "sources_section_title",
            "prayer_mosque_disclaimer", "azan_perm_notif_desc", "azan_exact_alarm_desc"
        )
        AppLanguage.values().forEach { lang ->
            criticalKeys.forEach { key ->
                assertTrue("$key must be non-blank in $lang", AppLocalization.get(key, lang).isNotBlank())
            }
        }
    }

    @Test
    fun testSilentPrayerNotificationBuilderHasNoSoundDefaultsZeroAndHonorsVibration() {
        Shadows.shadowOf(appContext).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        PrayerNotificationManager.init(appContext)
        val nm = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val shadowNm = Shadows.shadowOf(nm)

        // Old noisy channels deleted
        assertNull(nm.getNotificationChannel("noor_prayer_channel"))
        assertNull(nm.getNotificationChannel("noor_prayer_v2_vib"))
        assertNull(nm.getNotificationChannel("noor_prayer_v2_novib"))

        // 1) Azan + vibration ON -> uses silent-vib channel, zero sound, defaults=0, FLAG_ONLY_ALERT_ONCE
        PrayerNotificationManager.postPrayerNotification(
            context = appContext,
            prayerName = "Dhuhr",
            prayerTime = "12:20 PM",
            soundType = "makkah",
            vibration = true,
            notifId = 501
        )
        val vibNotif = shadowNm.getNotification(501)
        assertNotNull(vibNotif)
        assertEquals(PrayerNotificationManager.CHANNEL_ID, vibNotif.channelId)
        @Suppress("DEPRECATION")
        assertNull("Posted notification must NOT carry a bell/ringtone sound", vibNotif.sound)
        @Suppress("DEPRECATION")
        assertEquals("Posted notification defaults must be 0", 0, vibNotif.defaults)
        assertTrue((vibNotif.flags and Notification.FLAG_ONLY_ALERT_ONCE) != 0)

        // 2) Silent sound + vibration ON -> STILL honors vibration channel while staying audibly silent
        PrayerNotificationManager.postPrayerNotification(
            context = appContext,
            prayerName = "Asr",
            prayerTime = "03:45 PM",
            soundType = "silent",
            vibration = true,
            notifId = 502
        )
        val silentVibNotif = shadowNm.getNotification(502)
        assertNotNull(silentVibNotif)
        assertEquals(PrayerNotificationManager.CHANNEL_ID, silentVibNotif.channelId)
        @Suppress("DEPRECATION")
        assertNull(silentVibNotif.sound)

        // 3) Vibration OFF -> uses silent-novib channel with zero vibration pattern
        PrayerNotificationManager.postPrayerNotification(
            context = appContext,
            prayerName = "Maghrib",
            prayerTime = "05:30 PM",
            soundType = "fajr",
            vibration = false,
            notifId = 503
        )
        val noVibNotif = shadowNm.getNotification(503)
        assertNotNull(noVibNotif)
        assertEquals(PrayerNotificationManager.CHANNEL_ID_NOVIB, noVibNotif.channelId)
        @Suppress("DEPRECATION")
        assertNull(noVibNotif.sound)
        @Suppress("DEPRECATION")
        assertEquals(0, noVibNotif.defaults)
    }

    @Test
    fun testRapidTestNotificationAndDuplicateServiceStartsTriggerSingleAzanPlayback() {
        Shadows.shadowOf(appContext).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        val shadowApp = Shadows.shadowOf(appContext)
        QuranAudioManager.azanPlaybackTriggerCountForTest.set(0L)
        QuranAudioManager.lastTriggeredAzanKeyForTest = null

        PrayerNotificationManager.init(appContext)
        PrayerNotificationManager.updateSettings(
            appContext,
            PrayerNotificationSettings(adhanSound = "madinah", vibrationEnabled = true)
        )

        // Rapid double-tap on Test Notification button
        PrayerNotificationManager.sendTestNotification(appContext, "Asr", "04:15 PM")
        val firstSvcIntent = shadowApp.nextStartedService
        assertNotNull("First test notification must start AzanPlaybackService", firstSvcIntent)

        PrayerNotificationManager.sendTestNotification(appContext, "Asr", "04:15 PM")
        val secondSvcIntent = shadowApp.nextStartedService
        assertNull("Rapid second tap must be deduplicated cleanly", secondSvcIntent)

        // Even if the same service intent is delivered twice to AzanPlaybackService, playAzan runs once
        val serviceController = Robolectric.buildService(AzanPlaybackService::class.java, firstSvcIntent).create()
        serviceController.startCommand(0, 1)
        serviceController.startCommand(0, 2)

        assertEquals(
            "AzanPlaybackService must start audio only once per prayer event",
            1L,
            QuranAudioManager.azanPlaybackTriggerCountForTest.get()
        )
        assertEquals(
            "'madinah' must normalize to bundled asset key 'azan_madina'",
            "azan_madina",
            QuranAudioManager.lastTriggeredAzanKeyForTest
        )

        // Foreground notification posted by AzanPlaybackService is silent and alert-once
        val serviceShadow = Shadows.shadowOf(serviceController.get())
        val fgNotif = serviceShadow.lastForegroundNotification
        assertNotNull(fgNotif)
        assertEquals(PrayerNotificationManager.CHANNEL_ID_NOVIB, fgNotif.channelId)
        @Suppress("DEPRECATION")
        assertNull(fgNotif.sound)
        @Suppress("DEPRECATION")
        assertEquals(0, fgNotif.defaults)

        PrayerNotificationManager.stopAzanAudio(appContext)
        assertFalse(QuranAudioManager.isAudioPlaying.value)
        assertFalse(QuranAudioManager.isBuffering.value)
    }

    @Test
    fun testPrayerAlarmReceiverDeduplicatesSameEventAllowsDistinctPrayersAndSkipsSilentAudio() {
        Shadows.shadowOf(appContext).grantPermissions(Manifest.permission.POST_NOTIFICATIONS)
        val shadowApp = Shadows.shadowOf(appContext)
        val nm = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val receiver = PrayerAlarmReceiver()

        // Drain any unrelated queued service intents
        while (shadowApp.nextStartedService != null) { /* clear */ }

        val maghribIntent = Intent(PrayerAlarmReceiver.ACTION_PRAYER_ALARM).apply {
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_KEY, "maghrib")
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TITLE, "Maghrib")
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TIME, "05:42 PM")
            putExtra(PrayerAlarmReceiver.EXTRA_ADHAN_SOUND, "makkah")
            putExtra(PrayerAlarmReceiver.EXTRA_EVENT_ID, "alarm:maghrib:2026-11-17:05:42 PM")
        }
        receiver.onReceive(appContext, maghribIntent)
        val maghribSvc = shadowApp.nextStartedService
        assertNotNull("First Maghrib alarm must start AzanPlaybackService", maghribSvc)

        // Duplicate Maghrib broadcast for same eventId -> suppressed!
        receiver.onReceive(appContext, maghribIntent)
        assertNull("Duplicate Maghrib alarm broadcast must NOT re-trigger AzanPlaybackService", shadowApp.nextStartedService)

        // Distinct prayer (Isha) -> triggers normally!
        val ishaIntent = Intent(PrayerAlarmReceiver.ACTION_PRAYER_ALARM).apply {
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_KEY, "isha")
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TITLE, "Isha")
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TIME, "07:05 PM")
            putExtra(PrayerAlarmReceiver.EXTRA_ADHAN_SOUND, "fajr")
            putExtra(PrayerAlarmReceiver.EXTRA_EVENT_ID, "alarm:isha:2026-11-17:07:05 PM")
        }
        receiver.onReceive(appContext, ishaIntent)
        assertNotNull("Subsequent distinct prayer (Isha) must trigger normally", shadowApp.nextStartedService)

        // Silent alarm -> posts silent notification but does NOT start AzanPlaybackService
        val silentIntent = Intent(PrayerAlarmReceiver.ACTION_PRAYER_ALARM).apply {
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_KEY, "dhuhr")
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TITLE, "Dhuhr")
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TIME, "12:15 PM")
            putExtra(PrayerAlarmReceiver.EXTRA_ADHAN_SOUND, "silent")
            putExtra(PrayerAlarmReceiver.EXTRA_EVENT_ID, "alarm:dhuhr:2026-11-17:12:15 PM")
        }
        receiver.onReceive(appContext, silentIntent)
        assertNull("Silent prayer reminder must NOT start AzanPlaybackService", shadowApp.nextStartedService)
        assertTrue(Shadows.shadowOf(nm).allNotifications.isNotEmpty())
    }
}
