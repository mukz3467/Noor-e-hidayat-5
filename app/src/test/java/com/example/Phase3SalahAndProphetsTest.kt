package com.example

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.prophets.ProphetCategory
import com.example.data.prophets.ProphetRepository
import com.example.data.repository.QuranData
import com.example.data.repository.UserPreferencesRepository
import com.example.data.salah.BacklogEstimateConfig
import com.example.data.salah.FiqhGuidancePerspective
import com.example.data.salah.MissedReasonCategory
import com.example.data.salah.MissedSalahRepository
import com.example.data.salah.QadaEntryStatus
import com.example.data.salah.QadaPlannerConfig
import com.example.data.salah.SalahPrayerType
import com.example.ui.components.AppDirectionProvider
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.prophets.StoriesOfProphetsScreen
import com.example.ui.screens.salah.MissedSalahScreen
import com.example.ui.theme.NooreHidayatTheme
import kotlinx.coroutines.delay
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
class Phase3SalahAndProphetsTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        runBlocking {
            appContext = ApplicationProvider.getApplicationContext()
            appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
                .edit().clear().commit()
            QuranData.init(appContext)
            MissedSalahRepository.resetForTest(appContext)
            ProphetRepository.resetForTest(appContext)
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            MissedSalahRepository.resetForTest(appContext)
            ProphetRepository.resetForTest(appContext)
        }
    }

    @Test
    fun phase3Navigation_onlyInMoreHub_andAbsentFromHomeScreen() {
        var showMore by mutableStateOf(false)
        var navigatedDestination = ""
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                if (!showMore) {
                    HomeScreen(
                        language = AppLanguage.ENGLISH,
                        onNavigateToQuran = {},
                        onNavigateToLive = {},
                        onNavigateToLectures = {},
                        onNavigateToLearning = {},
                        onNavigateToMoreHub = { showMore = true },
                        onNavigateToPrayerTimes = {},
                        onNavigateToSettings = {},
                        onLanguageChanged = {}
                    )
                } else {
                    MoreHubScreen(
                        language = AppLanguage.ENGLISH,
                        onBack = {},
                        onNavigate = { navigatedDestination = it }
                    )
                }
            }
        }

        composeRule.onAllNodesWithTag("hub_item_missed_salah").assertCountEquals(0)
        composeRule.onAllNodesWithTag("hub_item_stories_prophets").assertCountEquals(0)
        composeRule.onAllNodesWithText("Missed Salah Tracker").assertCountEquals(0)
        composeRule.onAllNodesWithText("Stories of Prophets").assertCountEquals(0)

        composeRule.runOnIdle { showMore = true }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag("hub_item_missed_salah"))
        composeRule.onNodeWithTag("hub_item_missed_salah").assertIsDisplayed().performClick()
        assertEquals("missed_salah", navigatedDestination)

        composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag("hub_item_stories_prophets"))
        composeRule.onNodeWithTag("hub_item_stories_prophets").assertIsDisplayed().performClick()
        assertEquals("stories_prophets", navigatedDestination)
    }

    @Test
    fun missedSalah_crud_exemptions_guidancePerspectives_estimate_planner_and_backup() {
        runBlocking {
        MissedSalahRepository.init(appContext)
        delay(200)

        val initial = MissedSalahRepository.state.value
        assertEquals(0, initial.totalConfirmedMissed)
        assertEquals(0, initial.pendingQadaCount)
        assertEquals(0, initial.completedConfirmedCount)

        val today = MissedSalahRepository.todayIsoDateKey()
        val threeDaysAgo = MissedSalahRepository.offsetIsoDateKey(today, -3)

        // Add sleep/forgetfulness pending + deliberate pending + completed today + exempt
        MissedSalahRepository.addEntry(appContext, SalahPrayerType.FAJR, today, MissedReasonCategory.SLEEP_OR_FORGETFULNESS, "Overslept", markCompletedNow = false)
        MissedSalahRepository.addEntry(appContext, SalahPrayerType.ASR, threeDaysAgo, MissedReasonCategory.DELIBERATE_OR_NEGLIGENCE, "", markCompletedNow = false)
        MissedSalahRepository.addEntry(appContext, SalahPrayerType.ISHA, threeDaysAgo, MissedReasonCategory.ILLNESS_TRAVEL_HARDSHIP, "", markCompletedNow = true, completedDateKey = today)
        MissedSalahRepository.addEntry(appContext, SalahPrayerType.DHUHR, today, MissedReasonCategory.JURISPRUDENCE_EXEMPT, "", markCompletedNow = false)

        // Poll up to 2s for Room Flow emission
        var attempts = 0
        while (MissedSalahRepository.state.value.entries.size < 4 && attempts < 20) {
            delay(100)
            attempts++
        }

        val s1 = MissedSalahRepository.state.value
        assertEquals(4, s1.entries.size)
        assertEquals(1, s1.exemptCount)
        assertEquals(3, s1.totalConfirmedMissed) // excludes exempt
        assertEquals(2, s1.pendingQadaCount) // Comparative overview counts pending non-exempt
        assertEquals(1, s1.completedConfirmedCount)
        assertEquals(1, s1.completedTodayCount)
        assertEquals(1, s1.completedThisWeekCount)
        assertEquals(1, s1.prayerBreakdown[SalahPrayerType.FAJR]?.pendingQada)
        assertEquals(0, s1.prayerBreakdown[SalahPrayerType.DHUHR]?.pendingQada)

        // Switch scholarly guidance to Sleep/Forgetfulness only view -> separates deliberate entry from pending Qada
        MissedSalahRepository.setGuidancePerspective(appContext, FiqhGuidancePerspective.UNINTENTIONAL_ONLY_VIEW)
        val s2 = MissedSalahRepository.state.value
        assertEquals(1, s2.pendingQadaCount)
        assertEquals(1, s2.deliberateSeparatePendingCount)

        // Switch back to Majority view
        MissedSalahRepository.setGuidancePerspective(appContext, FiqhGuidancePerspective.MAJORITY_FOUR_SCHOOLS)
        assertEquals(2, MissedSalahRepository.state.value.pendingQadaCount)

        // Toggle completion on Fajr entry
        val fajrEntry = s2.entries.first { it.prayerEnum() == SalahPrayerType.FAJR }
        MissedSalahRepository.toggleCompleted(appContext, fajrEntry)
        delay(200)
        assertEquals(2, MissedSalahRepository.state.value.completedConfirmedCount)

        // Separate backlog estimate does NOT pollute confirmed records
        MissedSalahRepository.saveBacklogEstimate(
            appContext,
            BacklogEstimateConfig(
                enabled = true,
                startDateKey = "2020-01-01",
                estimatedByPrayer = mapOf(SalahPrayerType.FAJR to 10, SalahPrayerType.DHUHR to 5)
            )
        )
        MissedSalahRepository.logCompletedAgainstEstimate(appContext, SalahPrayerType.FAJR, +2)
        val s3 = MissedSalahRepository.state.value
        assertEquals(13, s3.estimate.remainingEstimated)
        assertEquals(3, s3.totalConfirmedMissed) // still strictly 3

        // Planner config
        MissedSalahRepository.savePlannerConfig(appContext, QadaPlannerConfig(isActive = true, isPaused = false, dailyTarget = 3))
        assertEquals(3, MissedSalahRepository.state.value.planner.dailyTarget)

        // Backup Export, Wipe DB, Restore from local JSON
        val backupRes = MissedSalahRepository.exportLocalBackupJson(appContext)
        assertTrue(backupRes.isSuccess)
        val backupFile = backupRes.getOrThrow()
        assertTrue(backupFile.exists())
        assertTrue(backupFile.readText().contains("SLEEP_OR_FORGETFULNESS"))

        // Copy backup temporarily, wipe, then restore
        val tempCopy = File(appContext.cacheDir, "test_backup_copy.json")
        backupFile.copyTo(tempCopy, overwrite = true)

        MissedSalahRepository.deleteAllPersonalData(appContext)
        delay(200)
        assertEquals(0, MissedSalahRepository.state.value.entries.size)
        assertFalse(backupFile.exists())

        val restoreRes = MissedSalahRepository.restoreFromBackupFile(appContext, tempCopy)
        assertTrue(restoreRes.isSuccess)
        assertEquals(4, restoreRes.getOrNull())
        tempCopy.delete()
        }
    }

    @Test
    fun missedSalah_allFiveVerifiedReferencesPresent_andMultiLanguageUiRenders() {
        assertEquals(5, MissedSalahRepository.VERIFIED_REFERENCES.size)
        val citations = MissedSalahRepository.VERIFIED_REFERENCES.map { it.citationTitle }
        assertTrue(citations.any { it.contains("Bukhari 597") })
        assertTrue(citations.any { it.contains("Muslim 684a") })
        assertTrue(citations.any { it.contains("Muslim 684c") })
        assertTrue(citations.any { it.contains("20:14") })
        assertTrue(citations.any { it.contains("4:103") })

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.OBSIDIAN_DARK) {
                AppDirectionProvider(language = AppLanguage.URDU) {
                    MissedSalahScreen(language = AppLanguage.URDU, onBack = {})
                }
            }
        }
        composeRule.onNodeWithTag("salah_dashboard_total").assertIsDisplayed()
        composeRule.onNodeWithTag("salah_dashboard_pending").assertIsDisplayed()
        composeRule.onNodeWithTag("salah_dashboard_completed").assertIsDisplayed()
        composeRule.onNodeWithTag("salah_add_btn").assertIsDisplayed().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("salah_save_entry_btn").assertExists()
    }

    @Test
    fun prophets_all25Present_exactQuranRefs_andLiveQuranDataOfflineResolution() {
        val all = ProphetRepository.allStories
        assertEquals(25, all.size)
        assertEquals((1..25).toList(), all.map { it.orderNumber })
        assertEquals("adam", all.first().id)
        assertEquals("muhammad", all.last().id)

        // Concise Quranic refs verification (Idris 19:56-57 & 21:85-86, Dhul-Kifl 21:85-86 & 38:48, Al-Yasa 6:86-87 & 38:48)
        val idris = ProphetRepository.getStoryById("idris")!!
        assertTrue(idris.verseRefs.any { it.surah == 19 && it.startAyah == 56 && it.endAyah == 57 })
        assertTrue(idris.verseRefs.any { it.surah == 21 && it.startAyah == 85 && it.endAyah == 86 })

        val dhulkifl = ProphetRepository.getStoryById("dhulkifl")!!
        assertTrue(dhulkifl.verseRefs.any { it.surah == 21 && it.startAyah == 85 })
        assertTrue(dhulkifl.verseRefs.any { it.surah == 38 && it.startAyah == 48 })

        val alyasa = ProphetRepository.getStoryById("alyasa")!!
        assertTrue(alyasa.verseRefs.any { it.surah == 6 && it.startAyah == 86 })
        assertTrue(alyasa.verseRefs.any { it.surah == 38 && it.startAyah == 48 })

        // Verify live verbatim resolution against bundled assets across ALL 25 Prophets & 4 languages
        all.forEach { story ->
            assertTrue("Missing 4-lang intro for ${story.id}", story.summaryIntro.size == 4 && story.detailedIntro.size == 4)
            assertTrue("Missing 9-section verse refs for ${story.id}", story.verseRefs.isNotEmpty())
            val firstRef = story.verseRefs.first()
            val enResolved = ProphetRepository.resolveQuranVerses(appContext, firstRef.surah, firstRef.startAyah, firstRef.endAyah, AppLanguage.ENGLISH)
            assertTrue("Expected offline ayahs for ${story.id} (${firstRef.referenceLabel})", enResolved.isNotEmpty())
            assertTrue("Arabic text missing for ${story.id}", enResolved.first().arabicText.isNotBlank())
            assertTrue("EN translation missing for ${story.id}", enResolved.first().translationText.isNotBlank())

            val urResolved = ProphetRepository.resolveQuranVerses(appContext, firstRef.surah, firstRef.startAyah, firstRef.endAyah, AppLanguage.URDU)
            assertTrue("UR translation missing for ${story.id}", urResolved.first().translationText.isNotBlank())

            val hiResolved = ProphetRepository.resolveQuranVerses(appContext, firstRef.surah, firstRef.startAyah, firstRef.endAyah, AppLanguage.HINDI)
            assertTrue("HI translation missing for ${story.id}", hiResolved.first().translationText.isNotBlank())
        }

        // Search & filter checks
        assertEquals(1, ProphetRepository.searchStories("19:56", null, false, AppLanguage.ENGLISH).count { it.id == "idris" })
        assertTrue(ProphetRepository.searchStories("Yusuf", null, false, AppLanguage.ENGLISH).any { it.id == "yusuf" })
        assertEquals(5, ProphetRepository.searchStories("", ProphetCategory.ULUL_AZM, false, AppLanguage.ENGLISH).size)
    }

    @Test
    fun prophets_ui_listAndReader9SectionsAndQuizFlow() {
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                AppDirectionProvider(language = AppLanguage.ARABIC) {
                    StoriesOfProphetsScreen(language = AppLanguage.ARABIC, onBack = {})
                }
            }
        }

        composeRule.onNodeWithTag("prophets_progress_count").assertTextEquals("0 / 25")
        composeRule.onNodeWithTag("prophets_list_container").performScrollToNode(hasTestTag("prophets_card_yunus"))
        composeRule.onNodeWithTag("prophets_card_yunus").performClick()
        composeRule.waitForIdle()

        // Reader renders Summary toggle, Introduction, Verses, and Quiz
        composeRule.onNodeWithTag("prophets_mode_toggle").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("prophets_sec_intro").assertIsDisplayed()
        composeRule.onNodeWithTag("prophets_detail_scroll").performScrollToNode(hasTestTag("prophets_sec_verses"))
        composeRule.onNodeWithTag("prophets_sec_verses").assertExists()
        composeRule.onNodeWithTag("prophets_detail_scroll").performScrollToNode(hasTestTag("prophets_quiz_card"))
        composeRule.onNodeWithTag("prophets_quiz_opt_0").performClick()
        composeRule.waitForIdle()
        assertTrue(ProphetRepository.progress.value.quizCompletedIds.contains("yunus"))
        assertTrue(ProphetRepository.progress.value.readIds.contains("yunus"))
    }
}
