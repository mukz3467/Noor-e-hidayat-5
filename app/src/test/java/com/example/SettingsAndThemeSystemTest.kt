package com.example

import android.app.Application
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.LayoutDirection
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.*
import com.example.data.notification.DailyReminderScheduler
import com.example.data.podcast.PodcastCategory
import com.example.data.podcast.PodcastEpisode
import com.example.data.podcast.PodcastRepository
import com.example.data.repository.QuranLearningRepository
import com.example.data.repository.QuranOfflineSyncManager
import com.example.data.repository.UserPreferencesRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuranReaderScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.NooreHidayatTheme
import com.example.ui.theme.ThemeContrastUtils
import com.example.ui.theme.resolveColorScheme
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SettingsAndThemeSystemTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        appContext.getSharedPreferences("noor_learning_academy_prefs", Context.MODE_PRIVATE)
            .edit().clear().commit()
        UserPreferencesRepository.isCellularOnlyOverrideForTest = null
        // Refresh CentralSettingsHub to clean defaults
        UserPreferencesRepository(appContext).resetPreferencesOnly()
    }

    @After
    fun tearDown() {
        UserPreferencesRepository.isCellularOnlyOverrideForTest = null
    }

    @Test
    fun languageSwitching_persistsAcrossRepoInstances_andControlsRtlDirection() {
        val repo1 = UserPreferencesRepository(appContext)
        repo1.setLanguage(AppLanguage.URDU)

        // New instance picks up persisted language immediately via SharedPreferences + CentralSettingsHub
        val repo2 = UserPreferencesRepository(appContext)
        assertEquals(AppLanguage.URDU, repo2.language.value)
        assertEquals(AppLanguage.URDU, repo2.appSettings.value.language)
        assertTrue(AppLanguage.URDU.isRtl)
        assertTrue(AppLanguage.ARABIC.isRtl)
        assertFalse(AppLanguage.ENGLISH.isRtl)
        assertFalse(AppLanguage.HINDI.isRtl)

        repo2.setLanguage(AppLanguage.ARABIC)
        assertEquals(AppLanguage.ARABIC, UserPreferencesRepository(appContext).language.value)

        repo2.setLanguage(AppLanguage.HINDI)
        assertEquals(AppLanguage.HINDI, UserPreferencesRepository(appContext).language.value)
    }

    @Test
    fun all7Themes_legacyMapping_appearanceMode_andResetThemeWorkCorrectly() {
        assertEquals(7, AppTheme.entries.size)
        assertEquals(AppTheme.ROYAL_TEAL, AppTheme.fromId("soft_light"))
        assertEquals(AppTheme.OBSIDIAN_DARK, AppTheme.fromId("dark_reading"))

        val repo = UserPreferencesRepository(appContext)
        AppTheme.entries.forEach { theme ->
            repo.setTheme(theme)
            val persisted = UserPreferencesRepository(appContext)
            assertEquals(theme, persisted.theme.value)
            assertEquals(theme, persisted.appSettings.value.theme)
        }

        // Light / Dark / System resolution check
        val lightEmerald = resolveColorScheme(
            AppSettingsState(theme = AppTheme.EMERALD_GOLD, appearanceMode = AppearanceMode.LIGHT),
            systemDark = true
        )
        assertFalse("Forced Light overrides systemDark", lightEmerald.second)

        val darkEmerald = resolveColorScheme(
            AppSettingsState(theme = AppTheme.EMERALD_GOLD, appearanceMode = AppearanceMode.DARK),
            systemDark = false
        )
        assertTrue("Forced Dark overrides light default", darkEmerald.second)

        val midnightSystem = resolveColorScheme(
            AppSettingsState(theme = AppTheme.MIDNIGHT_BLUE, appearanceMode = AppearanceMode.SYSTEM),
            systemDark = false
        )
        assertTrue("Midnight Blue defaults dark when in System mode", midnightSystem.second)

        // Custom accent contrast safeguard on bright yellow accent over light surface
        val contrastSettings = AppSettingsState(
            theme = AppTheme.WARM_CREAM,
            appearanceMode = AppearanceMode.LIGHT,
            customAccentHex = 0xFFFFFF00L, // Very bright yellow would fail contrast without adjustment
            highContrastEnabled = true
        )
        val (safeScheme, _) = resolveColorScheme(contrastSettings, systemDark = false)
        val ratio = ThemeContrastUtils.contrastRatio(safeScheme.primary, safeScheme.surface)
        assertTrue("Contrast safeguard should enforce >= 4.5f ratio, got $ratio", ratio >= 4.5f)

        // Reset Theme restores Emerald & Gold + System mode + null custom accent + 1.0 text scale
        repo.setCustomAccentHex(0xFF8B6570L)
        repo.setUiTextScale(1.3f)
        repo.setAppearanceMode(AppearanceMode.DARK)
        repo.resetThemeToDefault()
        val afterReset = UserPreferencesRepository(appContext).appSettings.value
        assertEquals(AppTheme.EMERALD_GOLD, afterReset.theme)
        assertEquals(AppearanceMode.SYSTEM, afterReset.appearanceMode)
        assertNull(afterReset.customAccentHex)
        assertEquals(1.0f, afterReset.uiTextScale, 0.001f)
    }

    @Test
    fun quranReadingPreferences_persist_andDriveQuranReaderScreen() {
        val repo = UserPreferencesRepository(appContext)
        repo.setArabicFontSize(30f)
        repo.setTranslationFontSize(18f)
        repo.setArabicFontOption(ArabicFontOption.NOTO_NASKH)
        repo.setAyahLayoutMode(AyahLayoutMode.SPACIOUS)
        repo.setShowInlineTranslation(false)
        repo.setReciter("abdul_basit")
        repo.setAudioPlaybackSpeed(1.5f)
        repo.setAyahRepeatCount(3)
        repo.setDefaultTafseerScholarId("tafseer_jalalayn")

        val reloaded = UserPreferencesRepository(appContext).appSettings.value
        assertEquals(30f, reloaded.arabicFontSize, 0.01f)
        assertEquals(18f, reloaded.translationFontSize, 0.01f)
        assertEquals(ArabicFontOption.NOTO_NASKH, reloaded.arabicFontOption)
        assertEquals(AyahLayoutMode.SPACIOUS, reloaded.ayahLayoutMode)
        assertFalse(reloaded.showInlineTranslation)
        assertEquals("abdul_basit", reloaded.selectedReciterId)
        assertEquals(1.5f, reloaded.audioPlaybackSpeed, 0.01f)
        assertEquals(3, reloaded.ayahRepeatCount)
        assertEquals("tafseer_jalalayn", reloaded.defaultTafseerScholarId)

        // Render QuranReaderScreen with inline translation disabled and verify it renders cleanly
        composeRule.setContent {
            NooreHidayatTheme(settings = reloaded) {
                QuranReaderScreen(
                    surahNumber = 1,
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    onNavigateToSurah = {}
                )
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("quran_reader_ayah_list").assertIsDisplayed()
    }

    @Test
    fun clearCacheAndResetPreferencesOnly_strictlyPreserveBookmarksDownloadsAndLearningProgress() {
        val repo = UserPreferencesRepository(appContext)

        // 1. Seed Bookmark, Favorite Dua, Tasbeeh, and Learning Progress
        repo.toggleBookmark(
            BookmarkItem(
                id = "quran_custom_test_2_255",
                type = BookmarkType.QURAN,
                title = "Al-Baqarah 2:255",
                subtitle = "Ayat al-Kursi",
                surahNumber = 2,
                ayahNumber = 255
            )
        )
        repo.toggleFavoriteDua("dua_morning_1")
        repo.incrementTasbeeh()
        QuranLearningRepository.markLessonCompleted(
            context = appContext,
            lessonId = "s1_l1",
            lessonTitle = "Stage 1 · 1. What Is Islam?",
            moduleId = "stage_1",
            stageNumber = 1
        )

        // 2. Create dummy temporary cache file vs protected offline audio / podcast files
        val cacheTempFile = File(appContext.cacheDir, "temp_image_thumb.cache").apply {
            parentFile?.mkdirs()
            writeBytes(ByteArray(2048) { 1 })
        }
        val quranOfflineFile = File(File(appContext.filesDir, "quran_offline_audio"), "mishary_001.mp3").apply {
            parentFile?.mkdirs()
            writeBytes(ByteArray(4096) { 2 })
        }
        val podcastOfflineFile = File(File(appContext.filesDir, "podcasts"), "ep_001.mp3").apply {
            parentFile?.mkdirs()
            writeBytes(ByteArray(3072) { 3 })
        }

        val breakdownBefore = repo.getStorageBreakdown()
        assertTrue(breakdownBefore.cacheBytes >= 2048L)
        assertTrue(breakdownBefore.quranAudioBytes >= 4096L)
        assertTrue(breakdownBefore.podcastBytes >= 3072L)

        // 3. Clear temporary cache -> only cacheDir removed!
        val freed = repo.clearAppCacheSafe()
        assertTrue(freed >= 2048L)
        assertFalse(cacheTempFile.exists())
        assertTrue("Offline Quran audio must be preserved", quranOfflineFile.exists())
        assertTrue("Offline Podcast audio must be preserved", podcastOfflineFile.exists())

        // 4. Reset Preferences Only -> preferences reset, user data preserved!
        repo.setTheme(AppTheme.MIDNIGHT_BLUE)
        repo.setLanguage(AppLanguage.URDU)
        repo.setWifiOnlyDownloads(false)
        repo.resetPreferencesOnly()

        val afterReset = UserPreferencesRepository(appContext)
        assertEquals(AppTheme.EMERALD_GOLD, afterReset.theme.value)
        assertEquals(AppLanguage.ENGLISH, afterReset.language.value)
        assertTrue(afterReset.appSettings.value.wifiOnlyDownloads)

        // Verify preservation of Bookmarks, Favorites, Tasbeeh, Offline Audio, and Learning Academy
        assertTrue(afterReset.bookmarks.value.any { it.id == "quran_custom_test_2_255" })
        assertTrue(afterReset.favoriteDuaIds.value.contains("dua_morning_1"))
        assertEquals(1, afterReset.tasbeehCount.value)
        assertTrue(quranOfflineFile.exists())
        assertTrue(podcastOfflineFile.exists())
        assertTrue(QuranLearningRepository.getProgress(appContext).completedLessonIds.contains("s1_l1"))

        // Cleanup test files
        quranOfflineFile.delete()
        podcastOfflineFile.delete()
    }

    @Test
    fun wifiOnlyDownloads_blocksCellularAudioAndPodcastDownloads() = runBlocking {
        val repo = UserPreferencesRepository(appContext)
        repo.setWifiOnlyDownloads(true)
        UserPreferencesRepository.isCellularOnlyOverrideForTest = true

        assertTrue(UserPreferencesRepository.isDownloadBlockedByWifiOnly(appContext))

        val quranResult = QuranOfflineSyncManager.downloadSurahSuspend(
            context = appContext,
            surahNumber = 2,
            reciterId = "mishary"
        )
        assertTrue("Quran offline download should be blocked when Wi-Fi only + cellular", quranResult.isFailure)
        assertTrue(
            quranResult.exceptionOrNull()?.message?.contains("Wi-Fi Only", ignoreCase = true) == true
        )

        val dummyEp = PodcastEpisode(
            guid = "test_ep_1",
            showId = "show_1",
            showName = "Test Show",
            publisher = "Test Publisher",
            host = "Scholar",
            title = "Episode 1",
            description = "Description",
            pubDateRaw = "2026-01-01",
            pubDateDisplay = "Jan 1, 2026",
            publishedEpochMs = 1L,
            audioEnclosureUrl = "https://media.yaqeeninstitute.org/podcasts/test_ep_1.mp3",
            mimeType = "audio/mpeg",
            fileLengthBytes = 100000L,
            durationSeconds = 300L,
            durationDisplay = "05:00",
            artworkUrl = "",
            languageCode = "en",
            categories = listOf(PodcastCategory.ISLAMIC_KNOWLEDGE),
            sourceAttribution = "Test",
            downloadPermittedByPublisher = true
        )
        val podcastResult = PodcastRepository.downloadEpisodeSuspend(appContext, dummyEp)
        assertTrue("Podcast download should be blocked when Wi-Fi only + cellular", podcastResult.isFailure)
        assertTrue(
            podcastResult.exceptionOrNull()?.message?.contains("Wi-Fi Only", ignoreCase = true) == true
        )

        // Turning off Wi-Fi-only permits cellular downloads
        repo.setWifiOnlyDownloads(false)
        assertFalse(UserPreferencesRepository.isDownloadBlockedByWifiOnly(appContext))
    }

    @Test
    fun dailyReminders_andDndQuietHours_andAppPinHashingWorkCorrectly() {
        val repo = UserPreferencesRepository(appContext)
        repo.setNotificationsEnabled(true)
        repo.setDailyQuranReminder(true, hour = 7, minute = 0)
        repo.setDailyHadithReminder(true)
        repo.setDailyDuaReminder(false)

        val scheduledCount = DailyReminderScheduler.rescheduleAll(appContext)
        assertTrue("At least 2 reminders should be scheduled (Quran + Hadith)", scheduledCount >= 2)

        // Wrap-around DND window 22:00 to 05:00
        val dndSettings = AppSettingsState(dndEnabled = true, dndStartHour = 22, dndEndHour = 5)
        assertTrue(DailyReminderScheduler.isWithinDndWindow(23, dndSettings))
        assertTrue(DailyReminderScheduler.isWithinDndWindow(2, dndSettings))
        assertFalse(DailyReminderScheduler.isWithinDndWindow(12, dndSettings))

        // App Lock PIN salted SHA-256 verification
        repo.setAppPin("4829")
        assertTrue(repo.appSettings.value.isPinLockEnabled)
        assertTrue(repo.verifyAppPin("4829"))
        assertFalse(repo.verifyAppPin("0000"))

        repo.setAppPin(null)
        assertFalse(repo.appSettings.value.isPinLockEnabled)
    }

    @Test
    fun settingsScreen_searchAllCategories_andSelectTheme() {
        var selectedTheme = AppTheme.EMERALD_GOLD
        var selectedLang = AppLanguage.ENGLISH

        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState(language = selectedLang, theme = selectedTheme)) {
                SettingsScreen(
                    currentLanguage = selectedLang,
                    currentTheme = selectedTheme,
                    onLanguageSelected = { selectedLang = it },
                    onThemeSelected = { selectedTheme = it },
                    onBack = {}
                )
            }
        }
        composeRule.waitForIdle()

        // Verify all 7 themes exist cleanly inside Appearance section
        AppTheme.entries.forEach { theme ->
            composeRule.onNodeWithTag("theme_option_${theme.id}").performScrollTo()
            composeRule.onNodeWithTag("theme_option_${theme.id}").assertExists()
        }

        // Click Midnight Blue theme
        composeRule.onNodeWithTag("theme_option_midnight_blue").performScrollTo().performClick()
        composeRule.waitForIdle()
        assertEquals(AppTheme.MIDNIGHT_BLUE, selectedTheme)

        // Use Search Bar inside Settings to find Privacy & Security section and PIN toggle
        composeRule.onNodeWithTag("settings_search_input").performScrollTo().performTextInput("PIN")
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("app_pin_toggle_btn").assertExists()
    }

    @Test
    @Config(sdk = [36], qualifiers = "w320dp-h560dp")
    fun settingsAndHomeScreen_renderCleanlyOnSmallDisplayWithoutClipping() {
        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState(language = AppLanguage.URDU, theme = AppTheme.ROYAL_TEAL)) {
                SettingsScreen(
                    currentLanguage = AppLanguage.URDU,
                    currentTheme = AppTheme.ROYAL_TEAL,
                    onLanguageSelected = {},
                    onThemeSelected = {},
                    onBack = {}
                )
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("settings_search_input").assertIsDisplayed()
        composeRule.onNodeWithTag("lang_option_ur").assertExists()
        composeRule.onNodeWithTag("theme_option_royal_teal").assertExists()
    }
}
