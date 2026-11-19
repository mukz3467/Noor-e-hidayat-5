package com.example

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.example.data.asma.*
import com.example.data.hajj.*
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.asma.AsmaUlHusnaScreen
import com.example.ui.screens.hajj.HajjUmrahScreen
import com.example.ui.theme.NooreHidayatTheme
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AsmaAndHajjGuideTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
        AsmaRepository.init(appContext)
        HajjGuideRepository.init(appContext)
    }

    @After
    fun tearDown() {
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

    @Test
    fun navigation_onlyInMoreHub_andAbsentFromHomeScreen() {
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

        // Absent from HomeScreen
        composeRule.onAllNodesWithTag("hub_item_asma").assertCountEquals(0)
        composeRule.onAllNodesWithTag("hub_item_hajj_umrah").assertCountEquals(0)
        composeRule.onAllNodesWithText("Asma-ul-Husna").assertCountEquals(0)
        composeRule.onAllNodesWithText("Hajj & Umrah Guide").assertCountEquals(0)

        // Switch to MoreHubScreen
        composeRule.runOnIdle { showMore = true }
        composeRule.waitForIdle()

        // Existing entries remain intact + both new entries present
        composeRule.onNodeWithTag("hub_item_hadith").assertExists()
        composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag("hub_item_ramadan"))
        composeRule.onNodeWithTag("hub_item_ramadan").assertIsDisplayed()

        composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag("hub_item_asma"))
        composeRule.onNodeWithTag("hub_item_asma").assertIsDisplayed().performClick()
        assertEquals("asma_ul_husna", navigatedDestination)

        composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag("hub_item_hajj_umrah"))
        composeRule.onNodeWithTag("hub_item_hajj_umrah").assertIsDisplayed().performClick()
        assertEquals("hajj_umrah", navigatedDestination)
    }

    @Test
    fun asma_catalogIntegrity_99UniqueEntries_andScholarlyTransparency() {
        val all = AsmaData.allNames
        assertEquals(99, all.size)
        assertEquals((1..99).toList(), all.map { it.number })
        assertEquals(99, all.map { it.arabic }.toSet().size)

        all.forEach { n ->
            assertTrue("Missing transliteration #${n.number}", n.transliteration.isNotBlank())
            assertTrue("Missing meaningEn #${n.number}", n.meaningEn.isNotBlank())
            assertTrue("Missing meaningUr #${n.number}", n.meaningUr.isNotBlank())
            assertTrue("Missing meaningHi #${n.number}", n.meaningHi.isNotBlank())
            assertTrue("Missing explanationAr #${n.number}", n.explanationAr.isNotBlank())
            assertTrue("Missing explanationEn #${n.number}", n.explanationEn.isNotBlank())
            assertTrue("Missing explanationUr #${n.number}", n.explanationUr.isNotBlank())
            assertTrue("Missing explanationHi #${n.number}", n.explanationHi.isNotBlank())
            assertTrue("Invalid URL #${n.number}", n.sourceUrl.startsWith("https://"))

            if (n.evidenceCategory == NameEvidenceCategory.QURAN_EXPLICIT) {
                assertFalse("Quranic name #${n.number} missing quranReference", n.quranReference.isNullOrBlank())
            } else if (n.evidenceCategory == NameEvidenceCategory.SCHOLARLY_COMPILATION) {
                assertNull("Compilation name #${n.number} should not claim explicit quranReference", n.quranReference)
            }
        }

        // Scholarly diversity statement must not invent single-list consensus
        AppLanguage.entries.forEach { lang ->
            val note = AsmaData.scholarlyDiversitySummary(lang)
            assertTrue("Note too short for $lang", note.length > 100)
            assertTrue(note.contains("2736"))
            assertTrue(note.contains("3507"))
        }
    }

    @Test
    fun asma_searchFiltersFavoritesProgressAndPersistence() {
        // Search by Arabic, transliteration, English meaning, Urdu meaning
        assertTrue(AsmaData.filterAndSearch("الرَّحْمَٰنُ", AsmaFilterOption.ALL, emptySet()).any { it.number == 1 })
        assertTrue(AsmaData.filterAndSearch("Al-Quddus", AsmaFilterOption.ALL, emptySet()).any { it.number == 4 })
        assertTrue(AsmaData.filterAndSearch("Forgiving", AsmaFilterOption.ALL, emptySet()).any { it.number == 14 || it.number == 34 })
        assertTrue(AsmaData.filterAndSearch("بادشاہ", AsmaFilterOption.ALL, emptySet()).any { it.number == 3 })

        // Filters
        assertTrue(AsmaData.filterAndSearch("", AsmaFilterOption.QURANIC, emptySet()).all {
            it.evidenceCategory == NameEvidenceCategory.QURAN_EXPLICIT
        })
        assertTrue(AsmaData.filterAndSearch("", AsmaFilterOption.HADITH, emptySet()).all {
            it.evidenceCategory == NameEvidenceCategory.AUTHENTIC_HADITH
        })
        assertTrue(AsmaData.filterAndSearch("", AsmaFilterOption.COMPILATION, emptySet()).all {
            it.evidenceCategory == NameEvidenceCategory.SCHOLARLY_COMPILATION
        })

        // Persistence across reload
        assertTrue(AsmaRepository.toggleFavorite(appContext, 1))
        assertTrue(AsmaRepository.toggleFavorite(appContext, 55))
        AsmaRepository.markExplored(appContext, 1)
        AsmaRepository.markExplored(appContext, 99)

        AsmaRepository.reload(appContext)
        assertEquals(setOf(1, 55), AsmaRepository.state.value.favoriteNumbers)
        assertEquals(setOf(1, 99), AsmaRepository.state.value.exploredNumbers)

        // Favorites filter reflects persisted favorites
        assertEquals(listOf(1, 55), AsmaData.filterAndSearch("", AsmaFilterOption.FAVORITES, AsmaRepository.state.value.favoriteNumbers).map { it.number })

        // Deterministic daily name
        val d1 = AsmaRepository.dailyName(LocalDate.of(2026, 4, 15))
        val d2 = AsmaRepository.dailyName(LocalDate.of(2026, 4, 15))
        assertEquals(d1.number, d2.number)

        // Share payload formatting
        val share = AsmaRepository.buildShareText(AsmaData.getByNumber(1)!!, AppLanguage.ENGLISH)
        assertTrue(share.contains("Ar-Rahman"))
        assertTrue(share.contains("Quran: 1:1, 59:22"))
        assertTrue(share.contains("https://quran.com/"))
    }

    @Test
    fun asma_uiScreen_searchOpenDetailFavoriteAndExploredProgress() {
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                AsmaUlHusnaScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithTag("asma_daily_name_card").assertExists()
        composeRule.onNodeWithTag("asma_explored_count", useUnmergedTree = true)
            .assertTextContains("0 / 99", substring = true)

        // Expand scholarly note
        composeRule.onNodeWithTag("asma_scholarly_note_toggle").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("asma_scholarly_note_text", useUnmergedTree = true).assertIsDisplayed()

        // Search and open #1 Ar-Rahman
        composeRule.onNodeWithTag("asma_search_input").performTextInput("Rahman")
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("asma_names_list").performScrollToNode(hasTestTag("asma_card_1"))
        composeRule.onNodeWithTag("asma_card_1").performClick()
        composeRule.waitForIdle()

        // Detail sheet shown, explored updated, toggle favorite
        composeRule.onNodeWithTag("asma_detail_sheet").assertIsDisplayed()
        composeRule.onNodeWithTag("asma_detail_fav_btn").performScrollTo().performClick()
        composeRule.waitForIdle()
        assertEquals(setOf(1), AsmaRepository.state.value.favoriteNumbers)
        assertTrue(AsmaRepository.state.value.exploredNumbers.contains(1))
    }

    @Test
    fun hajj_dataIntegrity_umrahAndHajjSequences_madhhabDifferences_andOfficialLinks() {
        val umrahSteps = HajjGuideRepository.allStepsForMode(PilgrimageMode.UMRAH)
        val hajjSteps = HajjGuideRepository.allStepsForMode(PilgrimageMode.HAJJ)
        assertEquals(6, umrahSteps.size)
        assertEquals(6, hajjSteps.size)

        // Verify key Umrah milestones
        assertTrue(umrahSteps.any { it.id.contains("tawaf") && it.rulingLevel == RulingLevel.RUKN_PILLAR })
        assertTrue(umrahSteps.any { it.id.contains("sai") })
        assertTrue(umrahSteps.any { it.id.contains("halq") })

        // Verify key Hajj Days 8-13 milestones
        assertTrue(hajjSteps.any { it.stageBadge.en.contains("8 Dhul-Hijjah") })
        assertTrue(hajjSteps.any { it.stageBadge.en.contains("9 Dhul-Hijjah") && it.rulingLevel == RulingLevel.RUKN_PILLAR })
        assertTrue(hajjSteps.any { it.stageBadge.en.contains("Muzdalifah") })
        assertTrue(hajjSteps.any { it.stageBadge.en.contains("10 Dhul-Hijjah") && it.instructions.en.contains("Hady") })
        assertTrue(hajjSteps.any { it.stageBadge.en.contains("11–13 Dhul-Hijjah") })
        assertTrue(hajjSteps.any { it.title.en.contains("Tawaf al-Wada") })

        // Official dynamic-rules links (haj.gov.sa / nusuk.sa)
        val urls = HajjDuasAndChecklistsData.officialLinks.map { it.second }
        assertTrue(urls.any { it.contains("haj.gov.sa") })
        assertTrue(urls.any { it.contains("nusuk.sa") })

        // Madhhab differences include all 4 schools
        val allNotes = (umrahSteps + hajjSteps).flatMap { it.madhhabNotes }
        assertTrue(allNotes.size >= 5)
        allNotes.forEach { note ->
            FiqhSchool.entries.forEach { school ->
                val txt = note.viewForSchool(school, AppLanguage.ENGLISH)
                assertTrue(txt.isNotBlank())
            }
        }

        // Validation disclaimer non-empty in all 4 languages
        AppLanguage.entries.forEach { lang ->
            assertTrue(HajjDuasAndChecklistsData.validationDisclaimer(lang).length > 80)
        }
    }

    @Test
    fun hajj_repositoryPersistence_modeSchoolChecklistsAndNotesCrud() {
        HajjGuideRepository.setMode(appContext, PilgrimageMode.HAJJ)
        HajjGuideRepository.setSubType(appContext, HajjSubType.QIRAN)
        HajjGuideRepository.setSchool(appContext, FiqhSchool.HANAFI)
        HajjGuideRepository.toggleStepCompleted(appContext, "hajj_1_day8_mina")
        HajjGuideRepository.toggleStepCompleted(appContext, "hajj_2_day9_arafat")
        HajjGuideRepository.toggleChecklistItem(appContext, "chk_ihram_sheets")
        HajjGuideRepository.addCustomChecklistItem(appContext, ChecklistCategory.PACKING, "Travel Prayer Mat")
        HajjGuideRepository.savePersonalNote(appContext, "Hotel Contact", "Room 412, Makkah")

        HajjGuideRepository.reload(appContext)
        val reloaded = HajjGuideRepository.state.value
        assertEquals(PilgrimageMode.HAJJ, reloaded.selectedMode)
        assertEquals(HajjSubType.QIRAN, reloaded.selectedSubType)
        assertEquals(FiqhSchool.HANAFI, reloaded.selectedSchool)
        assertEquals(setOf("hajj_1_day8_mina", "hajj_2_day9_arafat"), reloaded.completedStepIds)
        assertTrue(reloaded.checkedChecklistIds.contains("chk_ihram_sheets"))
        assertEquals(1, reloaded.customChecklistItems.size)
        assertEquals("Travel Prayer Mat", reloaded.customChecklistItems.first().title.en)
        assertEquals(1, reloaded.notes.size)
        val savedNoteId = reloaded.notes.first().id

        // Update & Delete note + Delete custom checklist
        HajjGuideRepository.savePersonalNote(appContext, "Hotel Updated", "Room 505", noteId = savedNoteId)
        assertEquals("Hotel Updated", HajjGuideRepository.state.value.notes.first().title)

        HajjGuideRepository.deletePersonalNote(appContext, savedNoteId)
        HajjGuideRepository.deleteCustomChecklistItem(appContext, reloaded.customChecklistItems.first().id)
        HajjGuideRepository.reload(appContext)
        assertTrue(HajjGuideRepository.state.value.notes.isEmpty())
        assertTrue(HajjGuideRepository.state.value.customChecklistItems.isEmpty())
    }

    @Test
    fun hajj_uiScreen_modeToggleMadhhabChecklistsAndNotes() {
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                HajjUmrahScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {}
                )
            }
        }

        // Starts on Rituals tab in UMRAH mode
        composeRule.onNodeWithTag("hajj_progress_count").assertTextEquals("0 / 6")
        composeRule.onNodeWithTag("hajj_rituals_list").performScrollToNode(hasTestTag("hajj_step_card_umrah_1_ihram"))
        composeRule.onNodeWithTag("hajj_step_check_umrah_1_ihram").performClick()
        composeRule.onNodeWithTag("hajj_rituals_list").performScrollToIndex(0)
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hajj_progress_count").assertTextEquals("1 / 6")

        // Switch to Hajj mode -> shows Tamattu'/Qiran/Ifrad chips
        composeRule.onNodeWithTag("hajj_mode_hajj_btn").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hajj_subtype_qiran").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("hajj_progress_count").assertTextEquals("0 / 6")

        // Switch to Intro tab -> verify Hajj intro & official links exist
        composeRule.onNodeWithTag("hajj_tab_intro").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hajj_intro_card_0").assertExists()

        // Switch to Ihram tab -> verify Meeqat points exist
        composeRule.onNodeWithTag("hajj_tab_ihram").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hajj_meeqat_dhul_hulayfah").assertExists()

        // Switch to Duas tab -> verify Talbiyah exists
        composeRule.onNodeWithTag("hajj_tab_duas").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hajj_dua_card_hajj_dua_talbiyah").assertExists()

        // Switch to Practical tab -> add custom checklist & personal note
        composeRule.onNodeWithTag("hajj_tab_practical").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hajj_custom_chk_input").performTextInput("Medical Card")
        composeRule.onNodeWithTag("hajj_add_chk_btn").performClick()
        composeRule.waitForIdle()
        assertTrue(HajjGuideRepository.state.value.customChecklistItems.any { it.title.en == "Medical Card" })

        composeRule.onNodeWithTag("hajj_practical_list").performScrollToNode(hasTestTag("hajj_add_note_btn"))
        composeRule.onNodeWithTag("hajj_add_note_btn").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hajj_note_title_input").performTextInput("Group Leader")
        composeRule.onNodeWithTag("hajj_note_body_input").performTextInput("Bus 3 Gate B")
        composeRule.onNodeWithTag("hajj_note_save_btn").performClick()
        composeRule.waitForIdle()
        assertTrue(HajjGuideRepository.state.value.notes.any { it.title == "Group Leader" })
    }

    @Test
    fun allFourLanguages_renderAsmaAndHajjScreensCleanly() {
        var lang by mutableStateOf(AppLanguage.ENGLISH)
        var screenMode by mutableStateOf(0)
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                if (screenMode == 0) {
                    AsmaUlHusnaScreen(language = lang, onBack = {})
                } else {
                    HajjUmrahScreen(language = lang, onBack = {})
                }
            }
        }

        AppLanguage.entries.forEach { currentLang ->
            composeRule.runOnIdle { lang = currentLang; screenMode = 0 }
            composeRule.waitForIdle()
            composeRule.onNodeWithText(AppLocalization.get("asma_ul_husna", currentLang)).assertExists()

            composeRule.runOnIdle { screenMode = 1 }
            composeRule.waitForIdle()
            composeRule.onNodeWithText(AppLocalization.get("hajj_umrah_guide", currentLang)).assertExists()
        }
    }
}
