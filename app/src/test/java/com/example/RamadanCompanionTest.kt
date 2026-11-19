package com.example

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.notification.PrayerAlarmReceiver
import com.example.data.notification.PrayerBootReceiver
import com.example.data.ramadan.*
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.UserPreferencesRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.ramadan.RamadanCompanionScreen
import com.example.ui.theme.NooreHidayatTheme
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
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
import java.io.IOException
import java.util.Calendar
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class RamadanCompanionTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        clearAllRamadanPrefs()
        RamadanRepository.resetForTest()
        PrayerCalculator.resetForTest()
        RamadanPrayerTimingSync.resetTestOverride()
        UserPreferencesRepository(appContext).resetPreferencesOnly()
        AppLocationManager.init(appContext)
        PrayerCalculator.init(appContext)
        IslamicCalendarRepository.init(appContext)
    }

    @After
    fun tearDown() {
        RamadanPrayerTimingSync.resetTestOverride()
        RamadanRepository.resetForTest()
        PrayerCalculator.resetForTest()
        clearAllRamadanPrefs()
    }

    private fun clearAllRamadanPrefs() {
        listOf(
            "ramadan_companion_prefs",
            "ramadan_alarm_scheduler_prefs",
            "location_prefs",
            "prayer_calc_prefs",
            "hijri_calendar_prefs",
            UserPreferencesRepository.PREFS_NAME
        ).forEach { name ->
            appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit()
        }
    }

    private fun middayCalendar(year: Int, month1Based: Int, day: Int, tzId: String = "Asia/Karachi"): Calendar {
        return Calendar.getInstance(TimeZone.getTimeZone(tzId)).apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month1Based - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 10)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    @Test
    fun navigation_onlyOneRamadanEntryInMoreHub_andAbsentFromHomeScreen() {
        var showMore by androidx.compose.runtime.mutableStateOf(false)
        var navigatedToId = ""
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                if (!showMore) {
                    HomeScreen(
                        language = AppLanguage.ENGLISH,
                        onNavigateToQuran = {},
                        onNavigateToLive = {},
                        onNavigateToLectures = {},
                        onNavigateToLearning = {},
                        onNavigateToMoreHub = {},
                        onNavigateToPrayerTimes = {},
                        onNavigateToSettings = {},
                        onLanguageChanged = {}
                    )
                } else {
                    MoreHubScreen(
                        language = AppLanguage.ENGLISH,
                        onBack = {},
                        onNavigate = { navigatedToId = it }
                    )
                }
            }
        }
        composeRule.onAllNodesWithText("Ramadan Companion", substring = true).assertCountEquals(0)
        composeRule.onAllNodesWithTag("hub_item_ramadan").assertCountEquals(0)

        showMore = true
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag("hub_item_ramadan"))
        composeRule.onAllNodesWithTag("hub_item_ramadan").assertCountEquals(1)
        composeRule.onNodeWithTag("hub_item_ramadan").performClick()
        assertEquals("ramadan_companion", navigatedToId)
    }

    @Test
    fun sehriAndIftarAlarms_scheduleWithOffsetsAndStopAfterRamadanEnds() {
        AppLocationManager.setManualCoordinates(
            context = appContext,
            lat = 24.8607,
            lon = 67.0011,
            cityName = "Karachi",
            countryName = "Pakistan",
            timeZoneId = "Asia/Karachi"
        )
        val nowCal = middayCalendar(2026, 3, 5, "Asia/Karachi")
        val startKey = "2026-03-01"
        RamadanRepository.init(appContext, nowCal.timeInMillis)
        RamadanRepository.setConfirmedRamadanStart(appContext, startKey, totalDays = 30, nowMillis = nowCal.timeInMillis)
        RamadanRepository.rescheduleAlarmsOfflineFallback(appContext, nowMillis = nowCal.timeInMillis)

        var state = RamadanRepository.state.value
        assertTrue(state.periodStatus.isRamadanActive)
        assertEquals(5, state.periodStatus.currentRamadanDay)
        assertTrue(state.upcomingAlarms.any { it.alarmKey == "sehri_pre" })
        assertTrue(state.upcomingAlarms.any { it.alarmKey == "sehri_fajr" })
        assertTrue(state.upcomingAlarms.any { it.alarmKey == "iftar_pre" })
        assertTrue(state.upcomingAlarms.any { it.alarmKey == "iftar_main" })

        val sehriPre = state.upcomingAlarms.first { it.alarmKey == "sehri_pre" }
        val sehriEnd = state.upcomingAlarms.first { it.alarmKey == "sehri_fajr" }
        assertEquals(30L * 60_000L, sehriEnd.triggerAtMillis - sehriPre.triggerAtMillis)

        val iftarPre = state.upcomingAlarms.first { it.alarmKey == "iftar_pre" }
        val iftarMain = state.upcomingAlarms.first { it.alarmKey == "iftar_main" }
        assertEquals(10L * 60_000L, iftarMain.triggerAtMillis - iftarPre.triggerAtMillis)

        // Check custom offset recalculation
        RamadanRepository.updateAlarmConfig(
            appContext,
            state.alarmConfig.copy(sehriOffsetMinutes = 45, iftarOffsetMinutes = 15),
            nowMillis = nowCal.timeInMillis
        )
        state = RamadanRepository.state.value
        val updatedSehriPre = state.upcomingAlarms.first { it.alarmKey == "sehri_pre" }
        val updatedSehriEnd = state.upcomingAlarms.first { it.alarmKey == "sehri_fajr" }
        assertEquals(45L * 60_000L, updatedSehriEnd.triggerAtMillis - updatedSehriPre.triggerAtMillis)

        // Advance past Ramadan day 30 -> Shawwal: Ramadan-only alarms auto-stop cleanly
        val afterRamadanCal = middayCalendar(2026, 4, 2)
        RamadanRepository.rescheduleAlarmsOfflineFallback(appContext, nowMillis = afterRamadanCal.timeInMillis)
        state = RamadanRepository.state.value
        assertFalse(state.periodStatus.isRamadanActive)
        assertTrue(state.upcomingAlarms.isEmpty())
    }

    @Test
    fun locationMethodAndTimezoneChanges_recalculateSehriIftarTimesAutomatically() {
        val nowCal = middayCalendar(2026, 3, 5, "Asia/Karachi")
        AppLocationManager.setManualCoordinates(
            context = appContext,
            lat = 24.8607,
            lon = 67.0011,
            cityName = "Karachi",
            countryName = "Pakistan",
            timeZoneId = "Asia/Karachi"
        )
        RamadanRepository.init(appContext, nowCal.timeInMillis)
        RamadanRepository.setConfirmedRamadanStart(appContext, "2026-03-01", 30, nowMillis = nowCal.timeInMillis)
        RamadanRepository.rescheduleAlarmsOfflineFallback(appContext, nowMillis = nowCal.timeInMillis)
        val karachiTiming = RamadanRepository.state.value.todayTiming!!
        assertEquals("Karachi", karachiTiming.cityName)

        // Change location to London
        AppLocationManager.setManualCoordinates(
            context = appContext,
            lat = 51.5072,
            lon = -0.1276,
            cityName = "London",
            countryName = "United Kingdom",
            timeZoneId = "Europe/London"
        )
        RamadanRepository.rescheduleAlarmsOfflineFallback(appContext, nowMillis = nowCal.timeInMillis)
        val londonTiming = RamadanRepository.state.value.todayTiming!!
        assertEquals("London", londonTiming.cityName)
        assertEquals("Europe/London", londonTiming.timezoneId)
        assertNotEquals(karachiTiming.fajr24, londonTiming.fajr24)

        // Change calculation method
        val prevConfig = PrayerCalculator.calcConfig.value
        PrayerCalculator.updateConfig(
            appContext,
            prevConfig.copy(method = PrayerCalculator.CalculationMethod.ISNA, isAutoMethodForLocation = false)
        )
        RamadanRepository.rescheduleAlarmsOfflineFallback(appContext, nowMillis = nowCal.timeInMillis)
        val isnaTiming = RamadanRepository.state.value.todayTiming!!
        assertEquals(PrayerCalculator.CalculationMethod.ISNA.displayName, isnaTiming.methodName)
    }

    @Test
    fun alAdhanSync_parsesOnlineJsonAndFallsBackOfflineOnFailureWithStaleCheck() = runBlocking {
        val nowCal = middayCalendar(2026, 3, 5, "Asia/Karachi")
        val mockJson = """
            {
              "code": 200,
              "status": "OK",
              "data": {
                "timings": {
                  "Fajr": "05:18 (PKT)",
                  "Maghrib": "18:34 (PKT)"
                }
              }
            }
        """.trimIndent()
        val okClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(mockJson.toResponseBody("application/json".toMediaType()))
                    .build()
            })
            .build()

        RamadanPrayerTimingSync.overrideForTest("https://mock.aladhan.local", okClient)
        RamadanRepository.init(appContext, nowCal.timeInMillis)
        RamadanRepository.refreshTodayTimingsAndReschedule(
            context = appContext,
            preferNetwork = true,
            reason = "test_api_sync",
            nowMillis = nowCal.timeInMillis
        )
        val synced = RamadanRepository.state.value.todayTiming!!
        assertEquals(TimingSource.ALADHAN_API, synced.source)
        assertFalse(synced.isEstimateFallback)
        assertEquals("05:18", synced.fajr24)
        assertEquals("18:34", synced.maghrib24)
        assertFalse(synced.isStale(nowCal.timeInMillis))
        assertTrue(synced.isStale(nowCal.timeInMillis + 40L * 3600L * 1000L))

        // Failed network falls back cleanly to offline astronomical calculation
        val failClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { throw IOException("Offline") })
            .build()
        RamadanPrayerTimingSync.overrideForTest("https://mock.aladhan.local", failClient)
        RamadanRepository.refreshTodayTimingsAndReschedule(
            context = appContext,
            preferNetwork = true,
            reason = "test_offline_fallback",
            nowMillis = nowCal.timeInMillis
        )
        val fallback = RamadanRepository.state.value.todayTiming!!
        assertEquals(TimingSource.OFFLINE_ASTRONOMICAL, fallback.source)
        assertTrue(fallback.isEstimateFallback)
        assertTrue(fallback.fajr24.contains(":"))
        assertTrue(fallback.maghrib24.contains(":"))
    }

    @Test
    fun workManagerAndBootReceiver_restoreAlarmsAcrossBootAndTimezoneChange() = runBlocking {
        RamadanRepository.init(appContext)
        RamadanRepository.setConfirmedRamadanStart(appContext, formatDateKey(Calendar.getInstance()), 30)

        val worker = TestListenableWorkerBuilder<RamadanDailyRefreshWorker>(appContext).build()
        val result = worker.doWork()
        assertEquals(ListenableWorker.Result.success(), result)

        // Simulate device boot completed + timezone changed
        val bootReceiver = PrayerBootReceiver()
        bootReceiver.onReceive(appContext, Intent(Intent.ACTION_BOOT_COMPLETED))
        bootReceiver.onReceive(appContext, Intent(Intent.ACTION_TIMEZONE_CHANGED))

        val am = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val scheduledAlarms = Shadows.shadowOf(am).scheduledAlarms
        assertTrue(scheduledAlarms.isNotEmpty())
        assertTrue(RamadanRepository.state.value.upcomingAlarms.isNotEmpty())
    }

    @Test
    fun deniedNotificationPermission_doesNotCrashWhenAlarmFiresAndReschedulesSnoozeSafely() {
        val app = Shadows.shadowOf(appContext)
        app.denyPermissions(android.Manifest.permission.POST_NOTIFICATIONS)
        RamadanRepository.init(appContext)
        assertFalse(RamadanRepository.state.value.hasNotificationPermission)

        // Trigger alarm broadcast cleanly with missing permission -> must not throw
        val receiver = PrayerAlarmReceiver()
        val alarmIntent = Intent(RamadanAlarmScheduler.ACTION_RAMADAN_ALARM).apply {
            putExtra(RamadanAlarmScheduler.EXTRA_ALARM_KEY, "sehri_pre")
            putExtra(RamadanAlarmScheduler.EXTRA_ALARM_TITLE, "Sehri Test")
            putExtra(RamadanAlarmScheduler.EXTRA_ALARM_TIME, "04:15 AM")
            putExtra(RamadanAlarmScheduler.EXTRA_SOUND_TYPE, AlarmSoundType.SILENT.id)
            putExtra(RamadanAlarmScheduler.EXTRA_SNOOZE_MINUTES, 10)
        }
        receiver.onReceive(appContext, alarmIntent)

        // Notification was NOT posted without permission
        val nm = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        assertEquals(0, Shadows.shadowOf(nm).allNotifications.size)

        // Snooze broadcast still schedules future AlarmManager alarm safely
        val snoozeIntent = Intent(RamadanAlarmScheduler.ACTION_RAMADAN_SNOOZE).apply {
            putExtra(RamadanAlarmScheduler.EXTRA_ALARM_KEY, "sehri_pre")
            putExtra(RamadanAlarmScheduler.EXTRA_ALARM_TITLE, "Sehri Test")
            putExtra(RamadanAlarmScheduler.EXTRA_SOUND_TYPE, AlarmSoundType.SILENT.id)
            putExtra(RamadanAlarmScheduler.EXTRA_SNOOZE_MINUTES, 10)
            putExtra(RamadanAlarmScheduler.EXTRA_NOTIF_ID, 3101)
        }
        receiver.onReceive(appContext, snoozeIntent)
        val am = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        assertTrue(Shadows.shadowOf(am).scheduledAlarms.isNotEmpty())
    }

    @Test
    fun trackersCrudAndPersistence_fastingQuranDeedsSadaqahChecklistSurviveRestart() {
        RamadanRepository.init(appContext)
        RamadanRepository.setFastingStatus(appContext, "2026-03-01", FastingStatus.COMPLETED, ramadanDay = 1, note = "Day 1")
        RamadanRepository.setFastingStatus(appContext, "2026-03-02", FastingStatus.MISSED, ramadanDay = 2)
        // Edit Day 2 status
        RamadanRepository.setFastingStatus(appContext, "2026-03-02", FastingStatus.EXCUSED, ramadanDay = 2, note = "Travel")
        // Voluntary fast
        RamadanRepository.setFastingStatus(appContext, "2026-04-06", FastingStatus.COMPLETED, isVoluntary = true, note = "Shawwal 1")

        RamadanRepository.updateQuranGoal(appContext, QuranGoalConfig(true, 30, QuranTargetMode.PAGES, 25, 604))
        RamadanRepository.logQuranReading(appContext, pagesRead = 20, note = "Surah Al-Baqarah", dateKey = "2026-03-01")
        RamadanRepository.logQuranReading(appContext, pagesRead = 10, dateKey = "2026-03-01")
        val idToDelete = RamadanRepository.state.value.quranLogs.first().id
        RamadanRepository.deleteQuranLog(appContext, idToDelete)

        RamadanRepository.addCustomDeed(appContext, "Feed stray animals", DeedCategory.CHARITY_SADAQAH)
        val customDeedId = RamadanRepository.state.value.deeds.last().id
        RamadanRepository.toggleDeedForDate(appContext, customDeedId, "2026-03-01")
        RamadanRepository.addSadaqahRecord(appContext, "500", "PKR", "Local food drive", "2026-03-01")
        RamadanRepository.addChecklistItem(appContext, "Prepare Zamzam water")
        val customChkId = RamadanRepository.state.value.checklistItems.last().id
        RamadanRepository.toggleChecklistItem(appContext, customChkId, "2026-03-01")

        // Simulate full app restart (reset in-memory singleton, reload from SharedPreferences)
        RamadanRepository.resetForTest()
        RamadanRepository.init(appContext)
        val reloaded = RamadanRepository.state.value

        assertEquals(FastingStatus.COMPLETED, reloaded.fastingEntries["2026-03-01"]?.status)
        assertEquals(FastingStatus.EXCUSED, reloaded.fastingEntries["2026-03-02"]?.status)
        assertTrue(reloaded.fastingEntries["2026-04-06"]?.isVoluntary == true)

        assertEquals(25, reloaded.quranGoal.dailyTargetValue)
        assertEquals(1, reloaded.quranLogs.size)
        assertEquals(20, reloaded.quranLogs.sumOf { it.pagesRead })

        assertTrue(reloaded.deeds.any { it.title == "Feed stray animals" })
        assertTrue(reloaded.completedDeedIdsByDate["2026-03-01"]?.contains(customDeedId) == true)
        assertEquals(1, reloaded.sadaqahRecords.size)
        assertEquals("500", reloaded.sadaqahRecords.first().amountText)
        assertTrue(reloaded.checklistItems.any { it.title == "Prepare Zamzam water" })
        assertTrue(reloaded.checkedChecklistIdsByDate["2026-03-01"]?.contains(customChkId) == true)
    }

    @Test
    fun uiAllFourLanguagesAndTabsRenderAuthenticDuasAndControlsCleanly() {
        RamadanRepository.init(appContext)
        RamadanRepository.setConfirmedRamadanStart(appContext, formatDateKey(Calendar.getInstance()), 30)

        var selectedLang by androidx.compose.runtime.mutableStateOf(AppLanguage.ENGLISH)
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                RamadanCompanionScreen(
                    language = selectedLang,
                    onBack = {},
                    onOpenQuranReader = {},
                    onOpenPrayerTimes = {}
                )
            }
        }

        composeRule.onNodeWithTag("ramadan_companion_screen").assertExists()
        composeRule.onNodeWithTag("ram_sehri_time_text").assertExists()
        composeRule.onNodeWithTag("ram_iftar_time_text").assertExists()

        // Navigate to Alarms tab & verify permission/config controls (scrolling LazyColumn to bottom)
        composeRule.onNodeWithTag("ram_tab_1").performClick()
        composeRule.onNodeWithTag("ram_alarm_master_switch").assertExists()
        composeRule.onNodeWithTag("ramadan_alarms_list").performScrollToNode(hasTestTag("ram_test_alarm_btn"))
        composeRule.onNodeWithTag("ram_test_alarm_btn").assertExists()

        // Navigate to Fasting & Quran tab
        composeRule.onNodeWithTag("ram_tab_2").performClick()
        composeRule.onNodeWithTag("ram_fasting_disclaimer").assertExists()
        composeRule.onNodeWithTag("ram_fast_day_1").assertExists()
        composeRule.onNodeWithTag("ramadan_fasting_quran_list").performScrollToNode(hasTestTag("ram_open_quran_btn"))
        composeRule.onNodeWithTag("ram_open_quran_btn").assertExists()

        // Navigate to Duas & Deeds tab & verify authentic Ramadan Duas & scholarly note
        composeRule.onNodeWithTag("ram_tab_3").performClick()
        composeRule.onNodeWithTag("ram_dua_scholarly_note").assertExists()
        composeRule.onNodeWithTag("ramadan_duas_deeds_list").performScrollToNode(hasTestTag("ram_dua_card_dua_iftar_dhahaba_dhama"))
        composeRule.onNodeWithTag("ram_dua_card_dua_iftar_dhahaba_dhama").assertExists()

        // Check Urdu, Arabic, Hindi render cleanly without crash
        listOf(AppLanguage.URDU, AppLanguage.ARABIC, AppLanguage.HINDI).forEach { lang ->
            selectedLang = lang
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("ramadan_companion_screen").assertExists()
        }
    }
}
