package com.example

import android.app.Application
import android.content.Context
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.model.AppSettingsState
import com.example.data.model.AppTheme
import com.example.data.repository.UserPreferencesRepository
import com.example.ui.components.AppDirectionProvider
import com.example.ui.components.MutedHeaderVideoConfig
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoreHubScreen
import com.example.ui.theme.LocalAppSettings
import com.example.ui.theme.NooreHidayatTheme
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class HomeScreenRedesignTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
    }

    @Test
    fun homeScreen_preservesHeaderWithoutSettingsButton_preservesPrayerCard_andRendersExact8BoxesInOrder() {
        val clicked = mutableListOf<String>()

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                AppDirectionProvider(language = AppLanguage.ENGLISH) {
                    HomeScreen(
                        language = AppLanguage.ENGLISH,
                        onNavigateToQuran = { clicked.add("quran") },
                        onNavigateToLive = { clicked.add("live") },
                        onNavigateToLectures = { clicked.add("lectures") },
                        onNavigateToLearning = { clicked.add("learning") },
                        onNavigateToMoreHub = { clicked.add("more") },
                        onNavigateToPrayerTimes = { clicked.add("prayer_times") },
                        onNavigateToSettings = { clicked.add("settings") },
                        onNavigateToHuqooqAcademy = { clicked.add("huqooq_academy") },
                        onNavigateToBusinessAcademy = { clicked.add("business_course") }
                    )
                }
            }
        }

        // 1. Header preserved with Kaaba header image & Settings shortcut removed from Header
        composeRule.onNodeWithTag("home_header").assertIsDisplayed()
        composeRule.onNodeWithTag("home_header_image").assertIsDisplayed()
        composeRule.onAllNodesWithTag("home_header_video").assertCountEquals(0)
        composeRule.onAllNodesWithTag("home_settings_button").assertCountEquals(0)

        // 2. Prayer Times card preserved and positioned between Header and 8-box Grid
        composeRule.onNodeWithTag("home_prayer_card").assertIsDisplayed()
        val headerBounds = composeRule.onNodeWithTag("home_header").getUnclippedBoundsInRoot()
        val prayerBounds = composeRule.onNodeWithTag("home_prayer_card").getUnclippedBoundsInRoot()
        val quranBounds = composeRule.onNodeWithTag("main_option_quran").getUnclippedBoundsInRoot()
        assertTrue(prayerBounds.top >= headerBounds.bottom)
        assertTrue(quranBounds.top > prayerBounds.bottom)

        composeRule.onNodeWithTag("home_prayer_card").performClick()
        assertEquals(listOf("prayer_times"), clicked)

        // 3. Exact 8 boxes in 2-column x 4-row grid
        val expectedOrder = listOf(
            "main_option_quran" to "quran",
            "main_option_live" to "live",
            "main_option_lectures" to "lectures",
            "main_option_learning" to "learning",
            "main_option_business_course" to "business_course",
            "main_option_huqooq_academy" to "huqooq_academy",
            "more_options_card" to "more",
            "main_option_settings" to "settings"
        )

        expectedOrder.forEach { (tag, dest) ->
            composeRule.onNodeWithTag(tag).performScrollTo().assertIsDisplayed().performClick()
            assertEquals(dest, clicked.last())
        }

        // Verify 2-column row geometry in LTR
        val r1Left = composeRule.onNodeWithTag("main_option_quran").getUnclippedBoundsInRoot()
        val r1Right = composeRule.onNodeWithTag("main_option_live").getUnclippedBoundsInRoot()
        val r2Left = composeRule.onNodeWithTag("main_option_lectures").getUnclippedBoundsInRoot()
        val r2Right = composeRule.onNodeWithTag("main_option_learning").getUnclippedBoundsInRoot()
        val r3Left = composeRule.onNodeWithTag("main_option_business_course").getUnclippedBoundsInRoot()
        val r3Right = composeRule.onNodeWithTag("main_option_huqooq_academy").getUnclippedBoundsInRoot()
        val r4Left = composeRule.onNodeWithTag("more_options_card").getUnclippedBoundsInRoot()
        val r4Right = composeRule.onNodeWithTag("main_option_settings").getUnclippedBoundsInRoot()

        assertTrue(r1Left.left < r1Right.left)
        assertTrue(r2Left.left < r2Right.left)
        assertTrue(r3Left.left < r3Right.left)
        assertTrue(r4Left.left < r4Right.left)
        assertTrue(r1Left.bottom <= r2Left.top)
        assertTrue(r2Left.bottom <= r3Left.top)
        assertTrue(r3Left.bottom <= r4Left.top)
    }

    @Test
    fun homeScreen_respectsShowHomePrayerCardToggle() {
        var showPrayer by mutableStateOf(true)

        composeRule.setContent {
            NooreHidayatTheme(settings = AppSettingsState(showHomePrayerCard = showPrayer)) {
                HomeScreen(
                    language = AppLanguage.ENGLISH,
                    onNavigateToQuran = {},
                    onNavigateToLive = {},
                    onNavigateToLectures = {},
                    onNavigateToLearning = {},
                    onNavigateToMoreHub = {},
                    onNavigateToPrayerTimes = {},
                    onNavigateToSettings = {}
                )
            }
        }

        composeRule.onAllNodesWithTag("home_prayer_card").assertCountEquals(1)
        showPrayer = false
        composeRule.waitForIdle()
        composeRule.onAllNodesWithTag("home_prayer_card").assertCountEquals(0)
    }

    @Test
    @Config(sdk = [36], qualifiers = "w320dp-h560dp")
    fun homeScreen_supportsUrduArabicHindiEnglishAndRtlLayoutOnSmallScreens() {
        var activeLanguage by mutableStateOf(AppLanguage.URDU)

        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                AppDirectionProvider(language = activeLanguage) {
                    HomeScreen(
                        language = activeLanguage,
                        onNavigateToQuran = {},
                        onNavigateToLive = {},
                        onNavigateToLectures = {},
                        onNavigateToLearning = {},
                        onNavigateToMoreHub = {},
                        onNavigateToPrayerTimes = {},
                        onNavigateToSettings = {}
                    )
                }
            }
        }

        // Urdu (RTL): first column box is placed to the RIGHT of second column box
        composeRule.onNodeWithTag("main_option_quran").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("اسلامی کاروبار").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("مزید").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("ترتیبات").performScrollTo().assertIsDisplayed()

        val urFirst = composeRule.onNodeWithTag("main_option_quran").getUnclippedBoundsInRoot()
        val urSecond = composeRule.onNodeWithTag("main_option_live").getUnclippedBoundsInRoot()
        assertTrue("Expected RTL ordering in Urdu", urFirst.left > urSecond.left)

        // Arabic (RTL)
        activeLanguage = AppLanguage.ARABIC
        composeRule.waitForIdle()
        composeRule.onNodeWithText("التجارة الإسلامية").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("المزيد").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("الإعدادات").performScrollTo().assertIsDisplayed()
        val arFirst = composeRule.onNodeWithTag("main_option_quran").getUnclippedBoundsInRoot()
        val arSecond = composeRule.onNodeWithTag("main_option_live").getUnclippedBoundsInRoot()
        assertTrue("Expected RTL ordering in Arabic", arFirst.left > arSecond.left)

        // Hindi (LTR)
        activeLanguage = AppLanguage.HINDI
        composeRule.waitForIdle()
        composeRule.onNodeWithText("इस्लामी व्यापार").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("अधिक").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("सेटिंग्स").performScrollTo().assertIsDisplayed()
        val hiFirst = composeRule.onNodeWithTag("main_option_quran").getUnclippedBoundsInRoot()
        val hiSecond = composeRule.onNodeWithTag("main_option_live").getUnclippedBoundsInRoot()
        assertTrue("Expected LTR ordering in Hindi", hiFirst.left < hiSecond.left)

        // English (LTR)
        activeLanguage = AppLanguage.ENGLISH
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Islami Business Course").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("More").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Settings").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun moreHub_preservesAllExistingFeatures() {
        composeRule.setContent {
            NooreHidayatTheme(selectedTheme = AppTheme.EMERALD_GOLD) {
                MoreHubScreen(
                    language = AppLanguage.ENGLISH,
                    onBack = {},
                    onNavigate = {}
                )
            }
        }

        val expectedTags = listOf(
            "hub_item_hadith",
            "hub_item_duas",
            "hub_item_tasbeeh",
            "hub_item_prayer",
            "hub_item_qibla",
            "hub_item_zakat",
            "hub_item_calendar",
            "hub_item_podcasts",
            "hub_item_bookmarks",
            "hub_item_ramadan",
            "hub_item_asma",
            "hub_item_hajj_umrah",
            "hub_item_mosque_finder",
            "hub_item_seerah",
            "hub_item_missed_salah",
            "hub_item_stories_prophets",
            "hub_item_shifa_ruqyah"
        )
        expectedTags.forEach { tag ->
            composeRule.onNodeWithTag("more_hub_grid").performScrollToNode(hasTestTag(tag))
            composeRule.onNodeWithTag(tag).assertIsDisplayed()
        }
        composeRule.onAllNodesWithTag("hub_item_business_academy").assertCountEquals(0)
    }

    @Test
    fun homeHeaderVideoAssetExistsAndCenterCropMathPreservesAspectRatio() {
        assertEquals(
            "asset:///video/home_header_video.mp4",
            MutedHeaderVideoConfig.buildAssetUri().toString()
        )
        // Verify bundled video file actually exists in APK assets
        appContext.assets.open(MutedHeaderVideoConfig.HOME_HEADER_VIDEO_ASSET_PATH).use { stream ->
            assertTrue("Bundled home header video must be non-empty", stream.read() != -1)
        }

        // Portrait video (720x1280) inside wide header container (1080x600)
        val (scaleXPortrait, scaleYPortrait) = MutedHeaderVideoConfig.computeCenterCropScales(
            viewWidth = 1080,
            viewHeight = 600,
            videoWidth = 720,
            videoHeight = 1280
        )
        assertEquals(1f, scaleXPortrait, 0.0001f)
        assertTrue(scaleYPortrait > 3f)
        val effectiveRatioPortrait = (1080f * scaleXPortrait) / (600f * scaleYPortrait)
        assertEquals(720f / 1280f, effectiveRatioPortrait, 0.001f)

        // Landscape video (1920x1080) inside square container (500x500)
        val (scaleXLand, scaleYLand) = MutedHeaderVideoConfig.computeCenterCropScales(
            viewWidth = 500,
            viewHeight = 500,
            videoWidth = 1920,
            videoHeight = 1080
        )
        assertEquals(1f, scaleYLand, 0.0001f)
        val effectiveRatioLand = (500f * scaleXLand) / (500f * scaleYLand)
        assertEquals(1920f / 1080f, effectiveRatioLand, 0.001f)
    }
}
