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
import com.example.data.repository.QuranData
import com.example.data.repository.UserPreferencesRepository
import com.example.data.shifa.*
import com.example.localization.AppLocalization
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.screens.shifa.ShifaRuqyahScreen
import com.example.ui.theme.NooreHidayatTheme
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
class ShifaRuqyahTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        QuranData.init(appContext)
        ShifaRuqyahRepository.resetForTest(appContext)
    }

    @After
    fun tearDown() {
        ShifaRuqyahRepository.resetForTest(appContext)
    }

    @Test
    fun placement_onlyInMoreHub_andAbsentFromHomeScreen() {
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

        // Home screen MUST NOT show any Shifa & Ruqyah shortcut card
        composeRule.onAllNodesWithTag("hub_item_shifa_ruqyah").assertCountEquals(0)

        // Navigate to MoreHub and verify tile opens shifa_ruqyah
        showMore = true
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("more_hub_grid")
            .performScrollToNode(hasTestTag("hub_item_shifa_ruqyah"))
        composeRule.onNodeWithTag("hub_item_shifa_ruqyah")
            .assertIsDisplayed()
            .performClick()

        assertEquals("shifa_ruqyah", navigatedDestination)
    }

    @Test
    fun all22MandatoryReferenceUrls_existInRepositoryCatalog() {
        val expectedUrls = listOf(
            "https://quran.com/",
            "https://quran.com/2",
            "https://quran.com/113",
            "https://quran.com/114",
            "https://quran.com/17/82",
            "https://quran.com/10/57",
            "https://quran.com/26/80",
            "https://quran.com/7",
            "https://quran.com/20",
            "https://sunnah.com/bukhari:5736",
            "https://sunnah.com/bukhari:5735",
            "https://sunnah.com/bukhari:5738",
            "https://sunnah.com/bukhari:5688",
            "https://sunnah.com/bukhari:5684",
            "https://sunnah.com/bukhari:5681",
            "https://sunnah.com/bukhari:5689",
            "https://sunnah.com/muslim:2191c",
            "https://sunnah.com/muslim:2195a",
            "https://sunnah.com/muslim:2196b",
            "https://sunnah.com/muslim:2187",
            "https://sunnah.com/muslim:2194",
            "https://islamqa.info/en/answers/3476"
        )
        val actualUrls = ShifaRuqyahRepository.allReferenceUrls()
        expectedUrls.forEach { expected ->
            assertTrue("Missing mandatory reference URL: $expected", expected in actualUrls)
        }
    }

    @Test
    fun quranItems_resolveOfflineCleanly_andPreserveArabicAcrossAll4Languages() {
        val quranItems = ShifaRuqyahRepository.allItems.filter { it.type == ShifaItemType.QURAN_AYAH }
        assertTrue("Expected at least 15 Quranic Shifa/Ruqyah entries", quranItems.size >= 15)

        quranItems.forEach { item ->
            val en = ShifaRuqyahRepository.resolveEntry(appContext, item, AppLanguage.ENGLISH)
            val ur = ShifaRuqyahRepository.resolveEntry(appContext, item, AppLanguage.URDU)
            val ar = ShifaRuqyahRepository.resolveEntry(appContext, item, AppLanguage.ARABIC)
            val hi = ShifaRuqyahRepository.resolveEntry(appContext, item, AppLanguage.HINDI)

            // Arabic script must never change across languages and never be blank
            assertTrue("Blank Arabic for ${item.id}", en.arabicText.isNotBlank())
            assertEquals("Arabic drifted on Urdu for ${item.id}", en.arabicText, ur.arabicText)
            assertEquals("Arabic drifted on Arabic for ${item.id}", en.arabicText, ar.arabicText)
            assertEquals("Arabic drifted on Hindi for ${item.id}", en.arabicText, hi.arabicText)

            // All 4 translations non-blank
            assertTrue("Blank EN translation for ${item.id}", en.translationText.isNotBlank())
            assertTrue("Blank UR translation for ${item.id}", ur.translationText.isNotBlank())
            assertTrue("Blank AR text for ${item.id}", ar.translationText.isNotBlank())
            assertTrue("Blank HI translation for ${item.id}", hi.translationText.isNotBlank())

            // Hindi translation MUST use Devanagari script (no Urdu/Arabic letters mixed in)
            assertTrue(
                "Hindi translation lacks Devanagari script for ${item.id}: ${hi.translationText}",
                hi.translationText.any { it in '\u0900'..'\u097F' }
            )
            assertFalse(
                "Hindi translation contains Arabic/Urdu script for ${item.id}",
                hi.translationText.any { it in '\u0600'..'\u06FF' }
            )

            // Offline Al-Mukhtasar Tafseer must load for EN, AR, HI and NEVER substitute Quran translation in UR
            assertFalse("Missing offline EN Tafseer for ${item.id}", en.offlineTafseerText.isNullOrBlank())
            assertFalse("Missing offline AR Tafseer for ${item.id}", ar.offlineTafseerText.isNullOrBlank())
            assertFalse("Missing offline HI Tafseer for ${item.id}", hi.offlineTafseerText.isNullOrBlank())
            assertNull("Urdu must not display translation as offline Tafseer for ${item.id}", ur.offlineTafseerText)
        }
    }

    @Test
    fun allCategoriesAndHindiDevanagariValidation() {
        ShifaCategory.entries.filter { it != ShifaCategory.TREATMENT_INDEX }.forEach { cat ->
            val items = ShifaRuqyahRepository.getItemsForCategory(cat)
            assertTrue("Category ${cat.id} should not be empty", items.isNotEmpty())
        }

        // Strict Tib-e-Nabawi separation of Prophetic Action vs Scholarly Explanation
        val tibItems = ShifaRuqyahRepository.getItemsForCategory(ShifaCategory.TIB_E_NABAWI)
        assertTrue("Expected at least 5 Tib-e-Nabawi items", tibItems.size >= 5)
        tibItems.forEach { tib ->
            assertNotNull("Missing propheticAction for ${tib.id}", tib.propheticAction)
            assertNotNull("Missing scholarlyExplanation for ${tib.id}", tib.scholarlyExplanation)
        }

        // Strict Hindi Devanagari validation on all Hindi labels and localization keys
        ShifaCategory.entries.forEach { cat ->
            assertTrue("Category ${cat.id} titleHi missing Devanagari", cat.titleHi.any { it in '\u0900'..'\u097F' })
            assertFalse("Category ${cat.id} titleHi leaked Urdu script", cat.titleHi.any { it in '\u0600'..'\u06FF' })
        }
        assertTrue(AppLocalization.get("shifa_ruqyah", AppLanguage.HINDI).any { it in '\u0900'..'\u097F' })
    }

    @Test
    fun bookmarksAndFavorites_persistAndFilterCleanly() {
        val sampleId = "ruqyah_q_fatihah"
        assertFalse(sampleId in ShifaRuqyahRepository.favoriteIds.value)
        assertFalse(sampleId in ShifaRuqyahRepository.bookmarkedIds.value)

        ShifaRuqyahRepository.toggleFavorite(appContext, sampleId)
        ShifaRuqyahRepository.toggleBookmark(appContext, sampleId)

        assertTrue(sampleId in ShifaRuqyahRepository.favoriteIds.value)
        assertTrue(sampleId in ShifaRuqyahRepository.bookmarkedIds.value)

        val favResults = ShifaRuqyahRepository.searchEntries(
            context = appContext,
            language = AppLanguage.ENGLISH,
            query = "",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            savedFilter = ShifaSavedFilter.FAVORITES
        )
        assertEquals(listOf(sampleId), favResults.map { it.item.id })
    }

    @Test
    fun treatmentSearchIndex_matchesConditions_andShowsExactFallbackForUnverifiedQueries() {
        val bukharResults = ShifaRuqyahRepository.searchEntries(
            context = appContext,
            language = AppLanguage.ENGLISH,
            query = "bukhar",
            category = ShifaCategory.TREATMENT_INDEX,
            savedFilter = ShifaSavedFilter.ALL
        )
        assertTrue(bukharResults.any { it.item.id == "tib_fever_water" })

        assertTrue(
            ShifaRuqyahRepository.isUnmappedConditionQuery(appContext, AppLanguage.ENGLISH, "diabetes")
        )
        assertFalse(
            ShifaRuqyahRepository.isUnmappedConditionQuery(appContext, AppLanguage.ENGLISH, "waswase")
        )

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                ShifaRuqyahScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    initialCategory = ShifaCategory.TREATMENT_INDEX
                )
            }
        }

        // Select Bukhar condition chip and verify verified proof renders
        composeRule.onNodeWithTag("shifa_entries_list")
            .performScrollToNode(hasTestTag("shifa_topic_cond_bukhar"))
        composeRule.onNodeWithTag("shifa_topic_cond_bukhar").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("shifa_topic_scope_cond_bukhar").assertIsDisplayed()

        // Search unmapped medical condition and verify exact required fallback notice renders
        composeRule.onNodeWithTag("shifa_search_input").performTextInput("diabetes")
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("shifa_unmapped_condition_banner").assertIsDisplayed()
        composeRule.onNodeWithText(ShifaTibHadithData.EXACT_UNMAPPED_CONDITION_NOTICE).assertIsDisplayed()
    }
}
