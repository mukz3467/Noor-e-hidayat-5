package com.example

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.repository.AudioFocusCoordinator
import com.example.data.repository.QuranData
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.ui.screens.HijriCalendarScreen
import com.example.ui.screens.QuranListScreen
import com.example.ui.screens.QuranReaderScreen
import com.example.ui.theme.NooreHidayatTheme
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AuditFixesVerificationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application
    private lateinit var originalTz: TimeZone

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        originalTz = TimeZone.getDefault()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        AudioFocusCoordinator.resetForTest()
        AppLocationManager.resetForTest()
    }

    @After
    fun tearDown() {
        TimeZone.setDefault(originalTz)
        AudioFocusCoordinator.resetForTest()
    }

    @Test
    fun audioFocusCoordinator_enforcesMutualExclusion_andHandlesBecomingNoisy() {
        var quranPaused = false
        var duaPaused = false
        var podcastPaused = false
        var azanStopped = false

        AudioFocusCoordinator.onPlaybackStarting(appContext, "quran", isAlarmUsage = false) {
            quranPaused = true
        }
        assertEquals(setOf("quran"), AudioFocusCoordinator.activeOwnerKeysForTest())

        // Starting Dua pauses Quran cleanly
        AudioFocusCoordinator.onPlaybackStarting(appContext, "dua", isAlarmUsage = false) {
            duaPaused = true
        }
        assertTrue("Quran should pause when Dua starts", quranPaused)
        assertEquals(setOf("dua"), AudioFocusCoordinator.activeOwnerKeysForTest())

        // Starting Podcast pauses Dua
        AudioFocusCoordinator.onPlaybackStarting(appContext, "podcast", isAlarmUsage = false) {
            podcastPaused = true
        }
        assertTrue("Dua should pause when Podcast starts", duaPaused)
        assertEquals(setOf("podcast"), AudioFocusCoordinator.activeOwnerKeysForTest())

        // ACTION_AUDIO_BECOMING_NOISY pauses active non-Azan media playback
        appContext.sendBroadcast(Intent(AudioManager.ACTION_AUDIO_BECOMING_NOISY).setPackage(appContext.packageName))
        org.robolectric.shadows.ShadowLooper.idleMainLooper()
        assertTrue("Podcast should pause when audio becomes noisy", podcastPaused)
        assertTrue(AudioFocusCoordinator.activeOwnerKeysForTest().isEmpty())

        // Azan alarm usage stays active even on becoming noisy
        AudioFocusCoordinator.onPlaybackStarting(appContext, "azan", isAlarmUsage = true) {
            azanStopped = true
        }
        appContext.sendBroadcast(Intent(AudioManager.ACTION_AUDIO_BECOMING_NOISY).setPackage(appContext.packageName))
        org.robolectric.shadows.ShadowLooper.idleMainLooper()
        assertFalse("Azan alarm audio must not be silenced by headset unplug", azanStopped)
        assertEquals(setOf("azan"), AudioFocusCoordinator.activeOwnerKeysForTest())

        AudioFocusCoordinator.onPlaybackStopped("azan")
        assertTrue(AudioFocusCoordinator.activeOwnerKeysForTest().isEmpty())
    }

    @Test
    fun quranDataAndParaSearchSupportHindiAndCorrectedSurah101UrduName() {
        val surah101 = QuranData.surahs.first { it.number == 101 }
        assertEquals("القارعة", surah101.nameUrdu)
        assertFalse(surah101.nameUrdu.contains("class"))

        composeRule.setContent {
            NooreHidayatTheme {
                QuranListScreen(
                    language = AppLanguage.HINDI,
                    onBack = {},
                    onSelectSurah = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("tab_paras").performClick()
        composeRule.waitForIdle()

        val para30Hindi = QuranData.paras.first { it.number == 30 }.nameHindi
        composeRule.onNodeWithTag("quran_search_field").performTextInput(para30Hindi)
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("para_item_30").assertIsDisplayed()
        composeRule.onNodeWithText("${AppLocalization.get("para", AppLanguage.HINDI)} 30: $para30Hindi")
            .assertIsDisplayed()
    }

    @Test
    fun quranReaderAyahBookmarkPersistsToUserPreferencesRepository() {
        val repoBefore = UserPreferencesRepository(appContext)
        assertFalse(repoBefore.isBookmarked(112, 1))

        composeRule.setContent {
            NooreHidayatTheme {
                QuranReaderScreen(
                    surahNumber = 112,
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    onNavigateToSurah = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("quran_reader_ayah_list")
            .performScrollToNode(hasTestTag("ayah_1_bookmark_btn"))
        composeRule.onNodeWithTag("ayah_1_bookmark_btn").performClick()
        composeRule.waitForIdle()

        // Verify persisted cleanly across a fresh repository instance
        val repoAfter = UserPreferencesRepository(appContext)
        assertTrue("Bookmark for Surah 112 Ayah 1 must persist", repoAfter.isBookmarked(112, 1))

        // Toggle off again and verify removal persists
        composeRule.onNodeWithTag("ayah_1_bookmark_btn").performClick()
        composeRule.waitForIdle()
        assertFalse("Second tap should remove persisted bookmark", UserPreferencesRepository(appContext).isBookmarked(112, 1))
    }

    @Test
    fun newLocalizationKeysExistInAllFourLanguages_andHijriUsesSelectedLocationTimezone() {
        val addedKeys = listOf(
            "dua_audio_pack_summary",
            "dua_cached_tracks_toast",
            "dua_audio_cache_failed",
            "prayer_perm_denied_toast",
            "prayer_allow",
            "prayer_turn_on",
            "prayer_gps_live",
            "prayer_manual_preset",
            "prayer_adjusted_prefix",
            "prayer_enter_coords_title",
            "prayer_enter_coords_desc",
            "prayer_lat_label",
            "prayer_lon_label",
            "prayer_apply",
            "prayer_coords_updated",
            "prayer_invalid_coords",
            "prayer_test_toast",
            "prayer_day_col",
            "ram_prayer_city_method_btn"
        )
        AppLanguage.entries.forEach { lang ->
            addedKeys.forEach { key ->
                val value = AppLocalization.get(key, lang)
                assertTrue("$key must be localized for $lang", value.isNotBlank() && value != key)
            }
        }

        val tokyo = AppLocationManager.PRESET_CITIES.first { it.timeZoneId == "Asia/Tokyo" }
        AppLocationManager.selectPresetCity(appContext, tokyo)

        composeRule.setContent {
            NooreHidayatTheme {
                HijriCalendarScreen(language = AppLanguage.HINDI, onBack = {})
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("hijri_hero_card").assertIsDisplayed()
        assertTrue(
            composeRule.onAllNodesWithText("Asia/Tokyo", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        )
    }
}
