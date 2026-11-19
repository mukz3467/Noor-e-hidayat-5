package com.example

import android.app.Application
import android.content.Context
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.model.AppSettingsState
import com.example.data.model.AppTheme
import com.example.data.model.AppearanceMode
import com.example.data.repository.UserPreferencesRepository
import com.example.data.shifa.ShifaCategory
import com.example.ui.components.noorSelectableChipColors
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.HadithScreen
import com.example.ui.screens.HijriCalendarScreen
import com.example.ui.screens.QuranListScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TasbeehScreen
import com.example.ui.screens.ZakatScreen
import com.example.ui.screens.shifa.ShifaRuqyahScreen
import com.example.ui.theme.NooreHidayatTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ButtonSelectionBehaviorTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        appContext.getSharedPreferences("tasbeeh_prefs", Context.MODE_PRIVATE)
            .edit().clear().commit()
        UserPreferencesRepository(appContext).resetPreferencesOnly()
    }

    @Test
    fun noorSelectableChipColors_preservesUnselectedDefaults_andUsesPrimaryOnlyWhenSelected() {
        val scenarios = listOf(
            AppSettingsState(theme = AppTheme.EMERALD_GOLD, appearanceMode = AppearanceMode.LIGHT),
            AppSettingsState(theme = AppTheme.MIDNIGHT_BLUE, appearanceMode = AppearanceMode.DARK),
            AppSettingsState(
                theme = AppTheme.WARM_CREAM,
                appearanceMode = AppearanceMode.LIGHT,
                customAccentHex = 0xFF6D28D9L
            )
        )

        var currentSettings by mutableStateOf(scenarios.first())
        var matchesExpectedColors = false
        var differsFromDefaultCream = false

        composeRule.setContent {
            NooreHidayatTheme(settings = currentSettings) {
                val scheme = MaterialTheme.colorScheme
                val actualColors = noorSelectableChipColors()
                val expectedColors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = scheme.primary,
                    selectedLabelColor = scheme.onPrimary,
                    selectedLeadingIconColor = scheme.onPrimary,
                    selectedTrailingIconColor = scheme.onPrimary
                )
                val defaultColors = FilterChipDefaults.filterChipColors()
                matchesExpectedColors = (actualColors == expectedColors)
                differsFromDefaultCream = (actualColors != defaultColors)
            }
        }

        scenarios.forEach { scenario ->
            currentSettings = scenario
            composeRule.waitForIdle()
            assertEquals(true, matchesExpectedColors)
            assertEquals(true, differsFromDefaultCream)
        }
    }

    @Test
    fun settingsScreen_languageThemeAndAppearance_exclusiveSelectionAndIndependentGroups() {
        var selectedLang by mutableStateOf(AppLanguage.ENGLISH)
        var selectedTheme by mutableStateOf(AppTheme.EMERALD_GOLD)

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

        // Language group: initially ONLY English is selected
        composeRule.onNodeWithTag("lang_option_en").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("lang_option_ur").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("lang_option_ar").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("lang_option_hi").performScrollTo().assertIsNotSelected()

        // Click Urdu -> ONLY Urdu is selected; English returns to unselected
        composeRule.onNodeWithTag("lang_option_ur").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("lang_option_ur").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("lang_option_en").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("lang_option_ar").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("lang_option_hi").performScrollTo().assertIsNotSelected()

        // Click Arabic -> ONLY Arabic is selected; Urdu returns to unselected
        composeRule.onNodeWithTag("lang_option_ar").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("lang_option_ar").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("lang_option_ur").performScrollTo().assertIsNotSelected()

        // Theme group (independent from Language): initially ONLY Emerald Gold is selected
        composeRule.onNodeWithTag("theme_option_emerald_gold").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("theme_option_midnight_blue").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("theme_option_royal_teal").performScrollTo().assertIsNotSelected()

        // Click Midnight Blue -> ONLY Midnight Blue is selected; Language stays Arabic
        composeRule.onNodeWithTag("theme_option_midnight_blue").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("theme_option_midnight_blue").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("theme_option_emerald_gold").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("lang_option_ar").performScrollTo().assertIsSelected()

        // Appearance Mode chips (independent from Theme & Language)
        composeRule.onNodeWithTag("appearance_mode_dark").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("appearance_mode_dark").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("appearance_mode_light").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("appearance_mode_system").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("theme_option_midnight_blue").performScrollTo().assertIsSelected()

        // Ordinary action button (Reset Theme) must NOT have permanent Selected semantics
        composeRule.onNodeWithTag("reset_theme_btn")
            .performScrollTo()
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Selected))
    }

    @Test
    fun quranListScreen_tabs_onlyActiveTabSelected() {
        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState()) {
                QuranListScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    onSelectSurah = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("tab_surahs").assertIsSelected()
        composeRule.onNodeWithTag("tab_paras").assertIsNotSelected()

        composeRule.onNodeWithTag("tab_paras").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("tab_paras").assertIsSelected()
        composeRule.onNodeWithTag("tab_surahs").assertIsNotSelected()

        composeRule.onNodeWithTag("tab_surahs").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("tab_surahs").assertIsSelected()
        composeRule.onNodeWithTag("tab_paras").assertIsNotSelected()
    }

    @Test
    fun hijriCalendarScreen_tabs_onlyActiveTabSelected() {
        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState()) {
                HijriCalendarScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("hijri_tab_calendar").assertIsSelected()
        composeRule.onNodeWithTag("hijri_tab_events").assertIsNotSelected()
        composeRule.onNodeWithTag("hijri_tab_ramadan").assertIsNotSelected()

        composeRule.onNodeWithTag("hijri_tab_events").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hijri_tab_events").assertIsSelected()
        composeRule.onNodeWithTag("hijri_tab_calendar").assertIsNotSelected()
        composeRule.onNodeWithTag("hijri_tab_ramadan").assertIsNotSelected()

        composeRule.onNodeWithTag("hijri_tab_ramadan").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hijri_tab_ramadan").assertIsSelected()
        composeRule.onNodeWithTag("hijri_tab_events").assertIsNotSelected()
        composeRule.onNodeWithTag("hijri_tab_calendar").assertIsNotSelected()
    }

    @Test
    fun bookmarksScreen_filterChips_exclusiveSelection() {
        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState()) {
                BookmarksScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    onNavigateToSurah = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("bookmarks_filter_all").assertIsSelected()
        composeRule.onNodeWithTag("bookmarks_filter_quran").assertIsNotSelected()
        composeRule.onNodeWithTag("bookmarks_filter_hadith").assertIsNotSelected()
        composeRule.onNodeWithTag("bookmarks_filter_dua").assertIsNotSelected()

        composeRule.onNodeWithTag("bookmarks_filter_quran").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("bookmarks_filter_quran").assertIsSelected()
        composeRule.onNodeWithTag("bookmarks_filter_all").assertIsNotSelected()
        composeRule.onNodeWithTag("bookmarks_filter_hadith").assertIsNotSelected()

        composeRule.onNodeWithTag("bookmarks_filter_dua").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("bookmarks_filter_dua").assertIsSelected()
        composeRule.onNodeWithTag("bookmarks_filter_quran").assertIsNotSelected()
        composeRule.onNodeWithTag("bookmarks_filter_all").assertIsNotSelected()
    }

    @Test
    fun hadithScreen_collectionAndVolumeFilters_exclusiveAndIndependentGroups() {
        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState()) {
                HadithScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("hadith_filter_all").assertIsSelected()
        composeRule.onNodeWithTag("hadith_filter_bukhari").assertIsNotSelected()
        composeRule.onNodeWithTag("hadith_filter_muslim").assertIsNotSelected()

        // Switch to Bukhari collection -> ONLY Bukhari chip selected
        composeRule.onNodeWithTag("hadith_filter_bukhari").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hadith_filter_bukhari").assertIsSelected()
        composeRule.onNodeWithTag("hadith_filter_all").assertIsNotSelected()
        composeRule.onNodeWithTag("hadith_filter_muslim").assertIsNotSelected()

        // Independent Bukhari Volume chip group
        composeRule.onNodeWithTag("bukhari_vol_chip_0").assertIsSelected()
        composeRule.onNodeWithTag("bukhari_vol_chip_1").assertIsNotSelected()
        composeRule.onNodeWithTag("bukhari_vol_chip_1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("bukhari_vol_chip_1").assertIsSelected()
        composeRule.onNodeWithTag("bukhari_vol_chip_0").assertIsNotSelected()
        // Collection group unaffected
        composeRule.onNodeWithTag("hadith_filter_bukhari").assertIsSelected()
    }

    @Test
    fun zakatScreen_nisabAndHawlGroups_exclusiveAndIndependent() {
        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState()) {
                ZakatScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {}
                )
            }
        }
        composeRule.waitForIdle()

        // Nisab group: default Silver
        composeRule.onNodeWithTag("nisab_silver_chip").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("nisab_gold_chip").performScrollTo().assertIsNotSelected()

        composeRule.onNodeWithTag("nisab_gold_chip").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("nisab_gold_chip").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("nisab_silver_chip").performScrollTo().assertIsNotSelected()

        // Hawl group (independent from Nisab)
        composeRule.onNodeWithTag("hawl_no_chip").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("hawl_no_chip").performScrollTo().assertIsSelected()
        composeRule.onNodeWithTag("hawl_yes_chip").performScrollTo().assertIsNotSelected()
        composeRule.onNodeWithTag("hawl_unknown_chip").performScrollTo().assertIsNotSelected()
        // Nisab stays Gold
        composeRule.onNodeWithTag("nisab_gold_chip").performScrollTo().assertIsSelected()
    }

    @Test
    fun shifaRuqyahScreen_categoryAndSavedFilters_exclusiveAndIndependent() {
        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState()) {
                ShifaRuqyahScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {}
                )
            }
        }
        composeRule.waitForIdle()

        val catQuranic = ShifaCategory.QURANIC_SHIFA.testTag
        val catRuqyah = ShifaCategory.RUQYAH_SHARIYYAH.testTag

        composeRule.onNodeWithTag(catQuranic).assertIsSelected()
        composeRule.onNodeWithTag(catRuqyah).assertIsNotSelected()

        composeRule.onNodeWithTag(catRuqyah).performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag(catRuqyah).assertIsSelected()
        composeRule.onNodeWithTag(catQuranic).assertIsNotSelected()

        // Saved filter group is independent of category group
        composeRule.onNodeWithTag("shifa_filter_all").assertIsSelected()
        composeRule.onNodeWithTag("shifa_filter_favorites").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("shifa_filter_favorites").assertIsSelected()
        composeRule.onNodeWithTag("shifa_filter_all").assertIsNotSelected()
        composeRule.onNodeWithTag(catRuqyah).assertIsSelected()
    }

    @Test
    @Config(sdk = [36], qualifiers = "w411dp-h891dp")
    fun tasbeehScreen_targetChips_exclusiveSelection_andActionButtonsNonSelectable() {
        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState()) {
                TasbeehScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("tasbeeh_target_chip_33").assertIsSelected()
        composeRule.onNodeWithTag("tasbeeh_target_chip_99").assertIsNotSelected()
        composeRule.onNodeWithTag("tasbeeh_target_chip_100").assertIsNotSelected()

        composeRule.onNodeWithTag("tasbeeh_target_chip_99").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("tasbeeh_target_chip_99").assertIsSelected()
        composeRule.onNodeWithTag("tasbeeh_target_chip_33").assertIsNotSelected()

        composeRule.onNodeWithTag("tasbeeh_target_chip_100").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("tasbeeh_target_chip_100").assertIsSelected()
        composeRule.onNodeWithTag("tasbeeh_target_chip_99").assertIsNotSelected()

        // Ordinary back navigation button is not a selectable toggle
        composeRule.onNodeWithTag("top_bar_back_button")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Selected))
    }
}
