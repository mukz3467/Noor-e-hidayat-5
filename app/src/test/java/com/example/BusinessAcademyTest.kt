package com.example

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.business.*
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.repository.QuranData
import com.example.data.repository.UserPreferencesRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.business.BusinessAcademyScreen
import com.example.ui.theme.NooreHidayatTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class BusinessAcademyTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application
    private lateinit var inMemoryDb: BusinessAcademyDatabase
    private lateinit var repository: BusinessAcademyRepository

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        QuranData.init(appContext)
        inMemoryDb = Room.inMemoryDatabaseBuilder(appContext, BusinessAcademyDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = BusinessAcademyRepository(inMemoryDb.businessDao())
    }

    @After
    fun tearDown() {
        inMemoryDb.close()
    }

    @Test
    fun navigation_onlyOnHomeScreen_absentFromMoreHub() {
        var showMore by mutableStateOf(false)
        var openedFromHome = false

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
                        onNavigateToBusinessAcademy = { openedFromHome = true },
                        onLanguageChanged = {}
                    )
                } else {
                    MoreHubScreen(
                        language = AppLanguage.ENGLISH,
                        onBack = {},
                        onNavigate = {}
                    )
                }
            }
        }

        composeRule.onNodeWithTag("main_option_business_course").performScrollTo().assertIsDisplayed().performClick()
        assertTrue(openedFromHome)

        composeRule.runOnIdle { showMore = true }
        composeRule.waitForIdle()

        composeRule.onAllNodesWithTag("hub_item_business_academy").assertCountEquals(0)
    }

    @Test
    fun offlineContent_contractsSectorsSpecialLessons_andVerbatimQuranHadith() {
        val ready = BusinessContentLoader.loadAll(appContext)

        assertEquals(14, ready.contracts.size)
        assertEquals((1..14).toList(), ready.contracts.map { it.order })

        assertEquals(30, ready.sectors.size)
        assertEquals((1..30).toList(), ready.sectors.map { it.sectorNumber })
        ready.sectors.forEach { s ->
            assertTrue("Missing non-blanket note in ${s.id}", s.nonBlanketAssessmentNote.en.isNotBlank())
            assertTrue("Missing section A in ${s.id}", s.introductionA.en.isNotBlank())
            assertTrue("Missing section R in ${s.id}", s.referencesR.isNotEmpty())
        }

        assertEquals(23, ready.specializedTopics.size)
        assertEquals((1..23).toList(), ready.specializedTopics.map { it.order })

        val crypto = ready.specializedTopics.first { it.order == 15 }
        assertTrue(crypto.isDisputedContemporaryIssue)
        assertEquals(EvidenceCategory.SCHOLARLY_REVIEW_REQUIRED, crypto.evidenceCategory)
        assertTrue(crypto.classicalAndContemporaryPositions.en.contains("TWO MAJOR CONTEMPORARY POSITIONS"))

        // Modules 1..24 covered across Levels 1, 2, 4
        val mods = ready.lessons.map { it.moduleNumber }.toSet()
        assertEquals((1..24).toSet(), mods)

        // Mandatory Hadiths present with Arabic and grading nuance notes
        listOf("bukhari_2079", "muslim_1532a", "muslim_102", "bukhari_2400", "ibnmajah_2443").forEach { hid ->
            val h = ready.hadithCatalog[hid]
            assertNotNull("Missing hadith $hid", h)
            assertTrue(h!!.arabicText.isNotBlank())
            assertTrue(h.gradingSourceAndNotes.en.isNotBlank())
            assertTrue(h.gradingSourceAndNotes.ur.isNotBlank())
        }

        // All 14 primary Quran citations resolve verbatim from bundled Surah files
        val specs = listOf(
            2 to 275..275,
            2 to 276..276,
            2 to 278..279,
            2 to 280..280,
            2 to 282..282,
            4 to 29..29,
            5 to 1..1,
            17 to 35..35,
            83 to 1..3,
            62 to 9..10,
            2 to 267..267,
            9 to 60..60,
            4 to 58..58,
            2 to 188..188
        ).map { (surah, range) ->
            BusinessQuranRefSpec(
                surahNumber = surah,
                startAyah = range.first,
                endAyah = range.last,
                contextNote = LocalizedText4("Context", "سیاق"),
                businessRelevance = LocalizedText4("Relevance", "اطلاق")
            )
        }
        val citations = BusinessContentLoader.resolveQuranCitations(appContext, specs)
        assertEquals(14, citations.size)
        citations.forEach { c ->
            assertTrue("Blank Arabic for ${c.surahNumber}:${c.startAyah}", c.arabicText.isNotBlank())
            assertTrue("Blank English for ${c.surahNumber}:${c.startAyah}", c.englishTranslation.isNotBlank())
            assertTrue("Blank Urdu for ${c.surahNumber}:${c.startAyah}", c.urduTranslation.isNotBlank())
            assertTrue("Blank Hindi for ${c.surahNumber}:${c.startAyah}", c.hindiTranslation.isNotBlank())
        }

        // All 7 EvidenceCategories produce distinct localized labels
        val labelsEn = EvidenceCategory.entries.map { it.label(AppLanguage.ENGLISH) }.toSet()
        assertEquals(7, labelsEn.size)
    }

    @Test
    fun offlineSearch_roomPersistence_andPdfCertificateGeneration() = runBlocking {
        val ready = BusinessContentLoader.loadAll(appContext)
        val hitsEn = BusinessContentLoader.searchAll(ready, "Murabaha", AppLanguage.ENGLISH)
        assertTrue(hitsEn.any { it.id == "contract_murabaha" })

        val hitsUr = BusinessContentLoader.searchAll(ready, "مرابحہ", AppLanguage.URDU)
        assertTrue(hitsUr.any { it.id == "contract_murabaha" })

        // Room persistence CRUD verification
        repository.markOpened("lesson_l1_m1", "lesson")
        repository.setCompleted("lesson_l1_m1", "lesson", true)
        repository.saveQuizResult("lesson_l1_m1", "lesson", 2, 2)
        assertTrue(repository.toggleBookmark("contract_murabaha", "contract", "1. Murabaha"))
        repository.saveNote("contract_murabaha", "contract", "Check constructive Qabd receipts before Murabaha contract.")
        repository.saveStartupStep(1, "Halal organic honey supply chain.", true)

        val progress = repository.observeAllProgress().first()
        assertTrue(progress.any { it.itemId == "lesson_l1_m1" && it.isCompleted })

        val quizzes = repository.observeAllQuizResults().first()
        assertEquals(2, quizzes.first { it.itemId == "lesson_l1_m1" }.bestScore)

        val bookmarks = repository.observeBookmarks().first()
        assertEquals(1, bookmarks.size)

        val notes = repository.observeNotes().first()
        assertTrue(notes.first().noteText.contains("Qabd"))

        val startupSteps = repository.observeStartupProjectSteps().first()
        assertTrue(startupSteps.first { it.stepNumber == 1 }.isCompleted)

        // Generate Offline PDF Certificate
        val certFile = repository.generateCompletionCertificatePdf(
            context = appContext,
            learnerName = "Muhammad Ali",
            completedCount = 5,
            totalItemsCount = 91,
            quizzesPassedCount = 4,
            startupStepsCompleted = 13
        )
        assertTrue(certFile.exists())
        assertTrue(certFile.length() > 500)
        val header = certFile.readBytes().take(5).toByteArray().toString(Charsets.US_ASCII)
        assertTrue("Expected PDF magic header, got $header", header.startsWith("%PDF-"))
    }

    @Test
    fun businessAcademyUi_rendersAllTabs_searchAndGlobalLanguageSwitchPreservesScreen() {
        var activeLang by mutableStateOf(AppLanguage.ENGLISH)
        val vm = BusinessAcademyViewModel(appContext, repository)

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                BusinessAcademyScreen(
                    language = activeLang,
                    onBack = {},
                    vm = vm
                )
            }
        }

        // Wait for async IO asset load inside ViewModel
        runBlocking { delay(600) }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("business_academy_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("business_search_input").assertIsDisplayed()

        // Navigate tabs cleanly (Contracts -> Sectors -> Special Topics -> Startup/Tools)
        composeRule.onNodeWithTag("business_tab_1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("contracts_list").assertIsDisplayed()

        composeRule.onNodeWithTag("business_tab_2").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("sectors_list").assertIsDisplayed()

        composeRule.onNodeWithTag("business_tab_3").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("specialized_list").assertIsDisplayed()

        // Switch global app language to Urdu (RTL) cleanly without crashing or resetting tab
        composeRule.runOnIdle { activeLang = AppLanguage.URDU }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("specialized_list").assertIsDisplayed()

        // Switch to Arabic & Hindi cleanly
        composeRule.runOnIdle { activeLang = AppLanguage.ARABIC }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("business_academy_screen").assertIsDisplayed()

        composeRule.runOnIdle { activeLang = AppLanguage.HINDI }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("business_academy_screen").assertIsDisplayed()
    }
}
