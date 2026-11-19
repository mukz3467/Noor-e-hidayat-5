package com.example

import android.app.Application
import android.content.Context
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.LayoutDirection
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.notification.PrayerNotificationManager
import com.example.data.repository.MediaAndLearningData
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.QuranLearningRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.ui.screens.LearningScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.NooreHidayatTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LearningAcademyAndCleanupTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences("noor_learning_academy_prefs", Context.MODE_PRIVATE)
            .edit().clear().commit()
        appContext.getSharedPreferences("noor_e_hidayat_prefs", Context.MODE_PRIVATE)
            .edit().clear().commit()
    }

    @Test
    fun curriculum_hasAll11Stages_inExactOrder_withComplete4LanguageContent() {
        val stages = QuranLearningRepository.allStages
        assertEquals(11, stages.size)
        assertEquals((1..11).toList(), stages.map { it.stageNumber })

        stages.forEach { stage ->
            assertTrue("Stage ${stage.stageNumber} titleEn blank", stage.titleEn.isNotBlank())
            assertTrue("Stage ${stage.stageNumber} titleUr blank", stage.titleUr.isNotBlank())
            assertTrue("Stage ${stage.stageNumber} titleAr blank", stage.titleAr.isNotBlank())
            assertTrue("Stage ${stage.stageNumber} titleHi blank", stage.titleHi.isNotBlank())
            assertTrue("Stage ${stage.stageNumber} has no lessons", stage.lessons.isNotEmpty())

            stage.lessons.forEach { lesson ->
                assertEquals(stage.stageNumber, lesson.stageNumber)
                assertTrue("${lesson.id} bodyEn blank", lesson.bodyEn.isNotBlank())
                assertTrue("${lesson.id} bodyUr blank", lesson.bodyUr.isNotBlank())
                assertTrue("${lesson.id} bodyAr blank", lesson.bodyAr.isNotBlank())
                assertTrue("${lesson.id} bodyHi blank", lesson.bodyHi.isNotBlank())
                assertTrue("${lesson.id} missing references", lesson.references.isNotEmpty())
            }
        }
    }

    @Test
    fun expansions_qaida15Lessons_wordByWord28Ayahs_vocab25_hifz16() {
        assertEquals(15, QuranLearningRepository.qaidaLessons.size)
        assertEquals((1..15).toList(), QuranLearningRepository.qaidaLessons.map { it.lessonNumber })
        assertEquals(28, QuranLearningRepository.wordByWordAllAyat.size)
        assertEquals(25, QuranLearningRepository.quranicVocabulary.size)
        assertEquals(4, QuranLearningRepository.surahIntroductions.size)
        assertEquals(16, QuranLearningRepository.hifzTargets.size)
        assertTrue(QuranLearningRepository.academyQuizzes.size >= 18)

        // Verify Stage 3 Salah has complete PrayerStepRecitation items
        val stage3 = QuranLearningRepository.getStageByNumber(3)
        assertNotNull(stage3)
        val totalRecitations = stage3!!.lessons.sumOf { it.recitations.size }
        assertTrue("Expected complete Salah recitations in Stage 3", totalRecitations >= 9)

        // Verify makhrajHi has pure Devanagari Hindi and no Arabic-script characters
        val arabicScriptRegex = Regex("[\u0600-\u06FF]")
        MediaAndLearningData.arabicAlphabet.forEach { letter ->
            assertFalse(
                "makhrajHi for ${letter.nameTranslit} contains Arabic/Urdu script: ${letter.makhrajHi}",
                arabicScriptRegex.containsMatchIn(letter.makhrajHi)
            )
            assertTrue(
                "makhrajHi for ${letter.nameTranslit} missing Devanagari",
                letter.makhrajHi.any { it in '\u0900'..'\u097F' }
            )
        }
    }

    @Test
    fun learningAcademy_followsGlobalSettingsLanguage_withoutSeparatePreference() {
        val userPrefs = UserPreferencesRepository(appContext)
        AppLanguage.entries.forEach { lang ->
            userPrefs.setLanguage(lang)
            assertEquals(lang, userPrefs.language.value)
            assertEquals(lang, UserPreferencesRepository(appContext).language.value)
            assertEquals(11, QuranLearningRepository.allStages.size)
            assertTrue(QuranLearningRepository.allStages.all { it.titleFor(lang).isNotBlank() })
        }
    }

    @Test
    fun progressTracking_recordOpenedDoesNotMarkCompleted_andMarkCompletedPersists() {
        QuranLearningRepository.recordLessonOpened(
            context = appContext,
            stageNumber = 3,
            lessonId = "s3_l2",
            lessonTitle = "Stage 3 · 2. Starting Prayer: Takbir, Thana & Al-Fatihah"
        )
        val afterOpen = QuranLearningRepository.getProgress(appContext)
        assertEquals(3, afterOpen.lastActiveStageNumber)
        assertEquals("s3_l2", afterOpen.lastActiveLessonId)
        assertFalse(afterOpen.completedLessonIds.contains("s3_l2"))

        QuranLearningRepository.markLessonCompleted(
            context = appContext,
            lessonId = "s3_l2",
            lessonTitle = "Stage 3 · 2. Starting Prayer",
            moduleId = "stage_3",
            stageNumber = 3
        )
        val afterComplete = QuranLearningRepository.getProgress(appContext)
        assertTrue(afterComplete.completedLessonIds.contains("s3_l2"))

        val revCount = QuranLearningRepository.incrementHifzRevision(appContext, 112)
        assertEquals(1, revCount)
        assertEquals(1, QuranLearningRepository.getProgress(appContext).hifzRevisionCounts[112])

        QuranLearningRepository.setHifzRevisionReminderEnabled(appContext, false)
        assertFalse(QuranLearningRepository.getProgress(appContext).hifzRevisionReminderEnabled)

        QuranLearningRepository.recordQuizPass(appContext, "q_s3_1")
        assertTrue(QuranLearningRepository.getProgress(appContext).completedQuizIds.contains("q_s3_1"))
    }

    @Test
    fun searchLessons_matchesAcrossStagesAndLanguages() {
        val wuduEn = QuranLearningRepository.searchLessons("Wudu", AppLanguage.ENGLISH)
        assertTrue(wuduEn.any { it.stageNumber == 2 })

        val namazUr = QuranLearningRepository.searchLessons("نماز", AppLanguage.URDU)
        assertTrue(namazUr.any { it.stageNumber == 3 })

        val zakatHi = QuranLearningRepository.searchLessons("ज़कात", AppLanguage.HINDI)
        assertTrue(zakatHi.any { it.stageNumber == 5 })
    }

    @Test
    fun learningScreen_hasNoSeparateLanguageSelector_andFollowsGlobalRtlLtr() {
        var capturedLayoutDirection: LayoutDirection? = null

        composeRule.setContent {
            NooreHidayatTheme {
                LearningScreen(language = AppLanguage.ENGLISH, onBack = {})
                capturedLayoutDirection = LocalLayoutDirection.current
            }
        }
        composeRule.waitForIdle()

        // No separate language picker or 4 inline pill row inside Academy; follows Settings language only
        assertEquals(0, composeRule.onAllNodesWithTag("learning_language_btn").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithTag("learning_language_dialog").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("عربي").fetchSemanticsNodes().size)
        assertEquals(LayoutDirection.Ltr, capturedLayoutDirection)
    }

    @Test
    fun learningScreen_urduGlobalLanguage_usesRtlAndUrduTitle() {
        composeRule.setContent {
            NooreHidayatTheme {
                LearningScreen(language = AppLanguage.URDU, onBack = {})
            }
        }
        composeRule.waitForIdle()

        val nodeDirection = composeRule.onNodeWithTag("academy_back_btn").fetchSemanticsNode().layoutInfo.layoutDirection
        assertEquals(LayoutDirection.Rtl, nodeDirection)
        assertEquals(0, composeRule.onAllNodesWithTag("learning_language_btn").fetchSemanticsNodes().size)
        composeRule.onNodeWithText("قرآن لرننگ اکیڈمی").assertIsDisplayed()
    }

    @Test
    fun learningScreen_honestCompletion_search_andContinueLearning() {
        QuranLearningRepository.recordLessonOpened(
            context = appContext,
            stageNumber = 3,
            lessonId = "s3_l1",
            lessonTitle = "Stage 3 · 1. Overview of the 5 Daily Prayers"
        )

        composeRule.setContent {
            NooreHidayatTheme {
                LearningScreen(language = AppLanguage.ENGLISH, onBack = {})
            }
        }
        composeRule.waitForIdle()

        // Continue Learning opens saved Stage 3 Lesson 1 directly without marking it completed
        composeRule.onNodeWithTag("continue_learning_btn").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("lesson_detail_s3_l1").assertIsDisplayed()
        assertFalse(QuranLearningRepository.getProgress(appContext).completedLessonIds.contains("s3_l1"))

        // Scrolling down and tapping Mark Lesson Complete records completion
        composeRule.onNodeWithTag("lesson_detail_s3_l1")
            .performScrollToNode(hasTestTag("mark_lesson_complete_btn"))
        composeRule.onNodeWithTag("mark_lesson_complete_btn").performClick()
        composeRule.waitForIdle()
        assertTrue(QuranLearningRepository.getProgress(appContext).completedLessonIds.contains("s3_l1"))

        // Go back to Stage 3 then Roadmap, and verify Search finds Wudu lesson
        composeRule.onNodeWithTag("academy_back_btn").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("academy_back_btn").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("learning_search_input").performTextInput("Tayammum")
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("search_result_s2_l5").assertIsDisplayed()
    }

    @Test
    fun moreHubScreen_removedDuplicateTafseerAzanAndDownloads_preserves9Features() {
        composeRule.setContent {
            NooreHidayatTheme {
                MoreHubScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    onNavigate = {}
                )
            }
        }
        composeRule.waitForIdle()

        assertEquals(0, composeRule.onAllNodesWithTag("hub_item_tafseer").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithTag("hub_item_azan").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithTag("hub_item_downloads").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("Tafseer Library").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("Azan Notifications").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("Downloaded Content").fetchSemanticsNodes().size)

        listOf(
            "hub_item_hadith",
            "hub_item_duas",
            "hub_item_tasbeeh",
            "hub_item_prayer",
            "hub_item_qibla",
            "hub_item_zakat",
            "hub_item_calendar",
            "hub_item_podcasts",
            "hub_item_bookmarks"
        ).forEach { tag ->
            composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag(tag))
            composeRule.onNodeWithTag(tag).assertExists()
        }
    }

    @Test
    fun settingsScreen_removedDuplicateAzanToggle_preservesLanguageThemesAndWifi() {
        composeRule.setContent {
            NooreHidayatTheme {
                SettingsScreen(
                    currentLanguage = AppLanguage.ENGLISH,
                    currentTheme = AppTheme.EMERALD_GOLD,
                    onLanguageSelected = {},
                    onThemeSelected = {},
                    onBack = {}
                )
            }
        }
        composeRule.waitForIdle()

        assertEquals(0, composeRule.onAllNodesWithTag("azan_notif_switch").fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("Azan Notifications").fetchSemanticsNodes().size)
        composeRule.onNodeWithTag("lang_option_en").assertExists()
        composeRule.onNodeWithTag("lang_option_ur").assertExists()
        composeRule.onNodeWithTag("wifi_download_switch").assertExists()

        // Backend PrayerCalculator & PrayerNotificationManager remain intact
        val userPrefs = UserPreferencesRepository(appContext)
        assertTrue(userPrefs.notificationsEnabled.value)
        val times = PrayerCalculator.calculatePrayerTimes(
            latitude = 21.4225,
            longitude = 39.8262,
            calendar = Calendar.getInstance()
        )
        assertTrue(times.fajr.isNotBlank() && times.maghrib.isNotBlank())
        assertTrue(PrayerNotificationManager.CHANNEL_ID.isNotBlank())
    }
}
