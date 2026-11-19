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
import com.example.data.huqooq.*
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.repository.QuranData
import com.example.data.repository.UserPreferencesRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.huqooq.HuqooqAcademyScreen
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
class HuqooqAcademyTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application
    private lateinit var inMemoryDb: HuqooqAcademyDatabase
    private lateinit var repository: HuqooqAcademyRepository

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        QuranData.init(appContext)
        inMemoryDb = Room.inMemoryDatabaseBuilder(appContext, HuqooqAcademyDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = HuqooqAcademyRepository(inMemoryDb.huqooqDao())
    }

    @After
    fun tearDown() {
        inMemoryDb.close()
    }

    @Test
    fun homeScreen_hasDesignatedHuqooqAcademyBox() {
        var navigatedToHuqooq = false

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                HomeScreen(
                    language = AppLanguage.ENGLISH,
                    onNavigateToQuran = {},
                    onNavigateToLive = {},
                    onNavigateToLectures = {},
                    onNavigateToLearning = {},
                    onNavigateToMoreHub = {},
                    onNavigateToPrayerTimes = {},
                    onNavigateToSettings = {},
                    onNavigateToHuqooqAcademy = { navigatedToHuqooq = true }
                )
            }
        }

        composeRule.onNodeWithTag("main_option_huqooq_academy")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertTrue(navigatedToHuqooq)
    }

    @Test
    fun offlineContent_allFourDepartments146Lessons_andVerbatimCitations() {
        val ready = HuqooqContentLoader.loadAll(appContext)
        assertEquals(146, ready.lessons.size)

        val dept1 = ready.lessons.filter { it.department == HuqooqDepartment.RIGHTS_RESPONSIBILITIES }
        val dept2 = ready.lessons.filter { it.department == HuqooqDepartment.ILM_UL_FARAID }
        val dept3 = ready.lessons.filter { it.department == HuqooqDepartment.ISLAH_MUASHRA }
        val dept4 = ready.lessons.filter { it.department == HuqooqDepartment.AKHLAQ_ADAAB }

        assertEquals(30, dept1.size)
        assertEquals(26, dept2.size)
        assertEquals(65, dept3.size)
        assertEquals(25, dept4.size)

        assertEquals((1..30).toList(), dept1.map { it.order })
        assertEquals((1..26).toList(), dept2.map { it.order })
        assertEquals((1..65).toList(), dept3.map { it.order })
        assertEquals((1..25).toList(), dept4.map { it.order })

        // Verify Dept 3 parsed list fields properly
        dept3.forEach { lesson ->
            assertNotNull("Missing rootCauses in ${lesson.id}", lesson.rootCauses)
            assertTrue("Empty individualSolution in ${lesson.id}", lesson.individualSolution?.en?.isNotEmpty() == true)
            assertTrue("Empty familySocietySolution in ${lesson.id}", lesson.familySocietySolution?.ur?.isNotEmpty() == true)
        }

        // Verify all referenced hadithIds exist in catalog
        ready.lessons.forEach { lesson ->
            lesson.hadithIds.forEach { hid ->
                assertNotNull("Missing hadith $hid referenced in ${lesson.id}", ready.hadithCatalog[hid])
            }
        }

        // Verify verbatim Quran resolution for key Huqooq and Inheritance ayahs
        val specs = listOf(
            HuqooqQuranRefSpec(4, 11, 12, HuqooqLocalized4("Faraid verses", "آیات وراثت")),
            HuqooqQuranRefSpec(4, 176, 176, HuqooqLocalized4("Kalalah", "کلالہ")),
            HuqooqQuranRefSpec(17, 23, 24, HuqooqLocalized4("Parents", "والدین")),
            HuqooqQuranRefSpec(49, 11, 12, HuqooqLocalized4("Social dignity", "معاشرتی ادب"))
        )
        val resolved = HuqooqContentLoader.resolveQuranCitations(appContext, specs)
        assertEquals(4, resolved.size)
        resolved.forEach { c ->
            assertTrue("Blank Arabic for ${c.surahNumber}:${c.startAyah}", c.arabicText.isNotBlank())
            assertTrue("Blank English for ${c.surahNumber}:${c.startAyah}", c.englishTranslation.isNotBlank())
            assertTrue("Blank Urdu for ${c.surahNumber}:${c.startAyah}", c.urduTranslation.isNotBlank())
        }

        // Offline search in English & Urdu
        assertTrue(HuqooqContentLoader.searchAll(ready, "Kalalah", AppLanguage.ENGLISH).isNotEmpty())
        assertTrue(HuqooqContentLoader.searchAll(ready, "والدین", AppLanguage.URDU).isNotEmpty())
    }

    @Test
    fun inheritanceCalculator_verifiesExactQuranicShares_Umariyyatayn_Awl_Radd_Hajb_andEdgeCases() {
        // 1. Nuclear family: Wife (1/8) + Father (1/6) + Mother (1/6) + 2 Sons + 1 Daughter -> residue 13/24 split 2:2:1
        val nuclear = InheritanceCalculator.calculate(
            InheritanceInput(
                totalEstate = 240_000.0,
                wifeCount = 1,
                hasFather = true,
                hasMother = true,
                sons = 2,
                daughters = 1
            )
        )
        assertEquals(CalculatorStatus.SUCCESS, nuclear.status)
        assertEquals("STANDARD", nuclear.specialRuleApplied)
        val wifeShare = nuclear.shares.first { it.heirKey == "wife" }
        assertEquals(Rational(1, 8), wifeShare.aggregateFraction)
        assertEquals(30_000.0, wifeShare.aggregateAmount, 0.01)
        val sumFrac = nuclear.shares.fold(Rational.ZERO) { acc, s -> acc + s.aggregateFraction }.reduced
        assertEquals(Rational.ONE, sumFrac)

        // 2. Umariyyatayn (Husband + Mother + Father)
        val umari = InheritanceCalculator.calculate(
            InheritanceInput(totalEstate = 60_000.0, hasHusband = true, hasFather = true, hasMother = true)
        )
        assertEquals(CalculatorStatus.SUCCESS, umari.status)
        assertEquals("UMARIYYATAYN", umari.specialRuleApplied)
        assertEquals(Rational(1, 2), umari.shares.first { it.heirKey == "husband" }.aggregateFraction)
        assertEquals(Rational(1, 6), umari.shares.first { it.heirKey == "mother" }.aggregateFraction)
        assertEquals(Rational(1, 3), umari.shares.first { it.heirKey == "father" }.aggregateFraction)

        // 3. Awl 6 -> 7 (Husband 1/2 + 2 Full Sisters 2/3 => 3/7 & 4/7)
        val awl = InheritanceCalculator.calculate(
            InheritanceInput(totalEstate = 70_000.0, hasHusband = true, fullSisters = 2)
        )
        assertEquals(CalculatorStatus.SUCCESS, awl.status)
        assertEquals("AWL", awl.specialRuleApplied)
        assertEquals(Rational(3, 7), awl.shares.first { it.heirKey == "husband" }.aggregateFraction)
        assertEquals(Rational(4, 7), awl.shares.first { it.heirKey == "full_sisters" }.aggregateFraction)

        // 4. Radd excluding spouse (Wife + Mother -> Wife 1/4, Mother 3/4)
        val radd = InheritanceCalculator.calculate(
            InheritanceInput(totalEstate = 100_000.0, wifeCount = 1, hasMother = true)
        )
        assertEquals(CalculatorStatus.SUCCESS, radd.status)
        assertEquals("RADD", radd.specialRuleApplied)
        assertEquals(Rational(1, 4), radd.shares.first { it.heirKey == "wife" }.aggregateFraction)
        assertEquals(Rational(3, 4), radd.shares.first { it.heirKey == "mother" }.aggregateFraction)

        // 5. Hajb exclusions (Father blocks grandfather & siblings; Son blocks grandsons)
        val hajb = InheritanceCalculator.calculate(
            InheritanceInput(
                totalEstate = 120_000.0,
                hasFather = true,
                hasPaternalGrandfather = true,
                sons = 1,
                grandsons = 2,
                fullBrothers = 1
            )
        )
        assertEquals(CalculatorStatus.SUCCESS, hajb.status)
        assertEquals(3, hajb.excludedHeirs.size)
        assertEquals(Rational(1, 6), hajb.shares.first { it.heirKey == "father" }.aggregateFraction)
        assertEquals(Rational(5, 6), hajb.shares.first { it.heirKey == "sons" }.aggregateFraction)

        // 6. Over-1/3 wasiyyat without consent is rejected
        val badWill = InheritanceCalculator.calculate(
            InheritanceInput(totalEstate = 90_000.0, debts = 30_000.0, wasiyyat = 30_000.0, sons = 1)
        )
        assertEquals(CalculatorStatus.INVALID_INPUT, badWill.status)

        // 7. Grandfather + Full Brother flags SCHOLARLY_REVIEW_REQUIRED
        val gfBrother = InheritanceCalculator.calculate(
            InheritanceInput(totalEstate = 100_000.0, hasPaternalGrandfather = true, fullBrothers = 1)
        )
        assertEquals(CalculatorStatus.SCHOLARLY_REVIEW_REQUIRED, gfBrother.status)
    }

    @Test
    fun roomPersistence_tracksProgressBookmarksQuizAndTextScale() = runBlocking {
        val ready = HuqooqContentLoader.loadAll(appContext)
        val sample = ready.lessons.first { it.id == "islah_01" }

        repository.markLessonOpened(sample, sample.title.en)
        repository.setLessonCompleted(sample.id, sample.department.id, true)
        assertTrue(repository.toggleBookmark(sample.id, sample.department.id, sample.title.en))
        repository.saveQuizResult(sample.id, sample.department.id, 2, 2)
        repository.updateLessonTextScale(1.25f)

        val progress = repository.observeAllProgress().first()
        assertTrue(progress.any { it.itemId == "islah_01" && it.isCompleted })

        val bookmarks = repository.observeBookmarks().first()
        assertEquals(1, bookmarks.size)

        val quiz = repository.observeQuizResults().first()
        assertEquals(2, quiz.first { it.itemId == "islah_01" }.bestScore)

        val meta = repository.observeMeta().first()
        assertEquals("islah_01", meta?.lastViewedLessonId)
        assertEquals(1.25f, meta?.lessonTextScale ?: 0f, 0.01f)
    }

    @Test
    fun huqooqAcademyUi_navigatesDepartmentsLessonsCalculatorAndLanguagesCleanly() {
        var activeLang by mutableStateOf(AppLanguage.ENGLISH)
        val vm = HuqooqAcademyViewModel(appContext, repository)

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                HuqooqAcademyScreen(
                    language = activeLang,
                    onBack = {},
                    vm = vm
                )
            }
        }

        runBlocking { delay(700) }
        composeRule.waitForIdle()

        // Hub Home shows all 4 department cards (scroll LazyColumn cleanly)
        composeRule.onNodeWithTag("huqooq_academy_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("hq_home_list").performScrollToNode(hasTestTag("huqooq_dept_rights"))
        composeRule.onNodeWithTag("huqooq_dept_rights").assertIsDisplayed()
        composeRule.onNodeWithTag("hq_home_list").performScrollToNode(hasTestTag("huqooq_dept_faraid"))
        composeRule.onNodeWithTag("huqooq_dept_faraid").assertIsDisplayed()
        composeRule.onNodeWithTag("hq_home_list").performScrollToNode(hasTestTag("huqooq_dept_akhlaq"))
        composeRule.onNodeWithTag("huqooq_dept_akhlaq").assertIsDisplayed()
        composeRule.onNodeWithTag("hq_home_list").performScrollToNode(hasTestTag("huqooq_dept_islah"))
        composeRule.onNodeWithTag("huqooq_dept_islah").assertIsDisplayed().performClick()
        composeRule.waitForIdle()

        // Dept 3 (Islah-e-Muashra) -> open islah_01 -> verify detail screen
        composeRule.onNodeWithTag("hq_department_screen_dept_islah").assertIsDisplayed()
        composeRule.onNodeWithTag("hq_lesson_card_islah_01").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hq_lesson_detail_islah_01").assertIsDisplayed()

        // Navigate back to Hub Home, open Inheritance Calculator, run Umariyyatayn calculation
        composeRule.runOnIdle {
            vm.navigateBack() // back to dept
            vm.navigateBack() // back to home
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hq_home_list").performScrollToNode(hasTestTag("hq_open_calculator_btn"))
        composeRule.onNodeWithTag("hq_open_calculator_btn").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hq_calculator_screen").assertIsDisplayed()

        composeRule.onNodeWithTag("hq_calculator_screen").performScrollToNode(hasTestTag("calc_husband_switch"))
        composeRule.onNodeWithTag("calc_husband_switch").performClick()
        composeRule.onNodeWithTag("hq_calculator_screen").performScrollToNode(hasTestTag("calc_father_switch"))
        composeRule.onNodeWithTag("calc_father_switch").performClick()
        composeRule.onNodeWithTag("hq_calculator_screen").performScrollToNode(hasTestTag("calc_mother_switch"))
        composeRule.onNodeWithTag("calc_mother_switch").performClick()
        composeRule.onNodeWithTag("hq_calculator_screen").performScrollToNode(hasTestTag("calc_calculate_btn"))
        composeRule.onNodeWithTag("calc_calculate_btn").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("hq_calculator_screen").performScrollToNode(hasTestTag("calc_result_card"))
        composeRule.onNodeWithTag("calc_result_card").assertIsDisplayed()
        composeRule.onNodeWithTag("calc_share_husband").assertExists()
        composeRule.onNodeWithTag("calc_share_father").assertExists()
        composeRule.onNodeWithTag("calc_share_mother").assertExists()

        // Switch global language across Urdu, Arabic, Hindi seamlessly
        composeRule.runOnIdle { activeLang = AppLanguage.URDU }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hq_calculator_screen").assertIsDisplayed()

        composeRule.runOnIdle { activeLang = AppLanguage.ARABIC }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hq_calculator_screen").assertIsDisplayed()

        composeRule.runOnIdle { activeLang = AppLanguage.HINDI }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hq_calculator_screen").assertIsDisplayed()
    }
}
