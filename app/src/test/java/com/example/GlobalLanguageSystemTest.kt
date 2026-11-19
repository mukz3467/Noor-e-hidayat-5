package com.example

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.core.app.ApplicationProvider
import com.example.data.location.AppLocationManager
import com.example.data.location.LocationSource
import com.example.data.location.UserLocation
import com.example.data.model.AppLanguage
import com.example.data.model.Dua
import com.example.data.model.Hadith
import com.example.data.notification.DailyReminderScheduler
import com.example.data.notification.DailyReminderType
import com.example.data.notification.PrayerNotificationManager
import com.example.data.ramadan.RamadanAlarmScheduler
import com.example.data.repository.DuaData
import com.example.data.repository.HadithData
import com.example.data.repository.QuranTafseerRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.localization.LocalizedContentResolver
import com.example.ui.components.AppDirectionProvider
import com.example.ui.screens.DuasScreen
import com.example.ui.screens.HadithScreen
import com.example.ui.screens.HijriCalendarScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.NooreHidayatTheme
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GlobalLanguageSystemTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        appContext = ApplicationProvider.getApplicationContext()
        appContext.getSharedPreferences(UserPreferencesRepository.PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().commit()
        PrayerNotificationManager.resetForTest()
        Shadows.shadowOf(appContext).grantPermissions(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    @Test
    fun singleLanguageSelectorInSettings_persistsAcrossInstances_andControlsLayoutDirection() {
        val repo = UserPreferencesRepository(appContext)
        repo.setLanguage(AppLanguage.ENGLISH)

        var dir: LayoutDirection? = null
        composeRule.setContent {
            val lang = repo.language.collectAsStateWithLifecycle().value
            val currentTheme = repo.theme.collectAsStateWithLifecycle().value
            NooreHidayatTheme {
                AppDirectionProvider(language = lang) {
                    dir = LocalLayoutDirection.current
                    SettingsScreen(
                        currentLanguage = lang,
                        currentTheme = currentTheme,
                        onLanguageSelected = { repo.setLanguage(it) },
                        onThemeSelected = { repo.setTheme(it) },
                        onBack = {}
                    )
                }
            }
        }
        composeRule.waitForIdle()
        assertEquals(LayoutDirection.Ltr, dir)

        composeRule.onNodeWithTag("lang_option_ar").performScrollTo().performClick()
        composeRule.waitForIdle()
        assertEquals(AppLanguage.ARABIC, repo.language.value)
        assertEquals(AppLanguage.ARABIC, UserPreferencesRepository(appContext).language.value)

        composeRule.onNodeWithTag("lang_option_hi").performScrollTo().performClick()
        composeRule.waitForIdle()
        assertEquals(AppLanguage.HINDI, repo.language.value)
    }

    @Test
    fun appDirectionProvider_setsRtlForUrduAndArabic_andLtrForEnglishAndHindi() {
        val captured = mutableMapOf<AppLanguage, LayoutDirection>()
        composeRule.setContent {
            AppLanguage.entries.forEach { lang ->
                AppDirectionProvider(language = lang) {
                    captured[lang] = LocalLayoutDirection.current
                }
            }
        }
        composeRule.waitForIdle()

        assertEquals(LayoutDirection.Rtl, captured[AppLanguage.URDU])
        assertEquals(LayoutDirection.Rtl, captured[AppLanguage.ARABIC])
        assertEquals(LayoutDirection.Ltr, captured[AppLanguage.ENGLISH])
        assertEquals(LayoutDirection.Ltr, captured[AppLanguage.HINDI])
    }

    @Test
    fun hadithResolver_neverMixesLanguagesOrSilentlySubstitutesEnglishForHindi() {
        val hindiUnavailableHadith = Hadith(
            id = "test_h_1",
            collection = "bukhari",
            bookNumber = 1,
            bookName = "Book of Faith",
            hadithNumber = 1,
            chapterArabic = "بدء الوحي",
            chapterEnglish = "Revelation",
            chapterUrdu = "وحی کی ابتدا",
            chapterHindi = "वह्य की शुरुआत",
            textArabic = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ",
            textEnglish = "Actions are judged by intentions.",
            textUrdu = "اعمال کا دارومدار نیتوں پر ہے۔",
            textHindi = "",
            narrator = "Umar ibn al-Khattab",
            grade = "Sahih",
            sourceAttribution = "Sahih al-Bukhari 1",
            isHindiAvailable = false
        )

        val enResolved = LocalizedContentResolver.resolveHadithTranslation(hindiUnavailableHadith, AppLanguage.ENGLISH)
        assertTrue(enResolved.isAvailable)
        assertEquals("Actions are judged by intentions.", enResolved.text)

        val urResolved = LocalizedContentResolver.resolveHadithTranslation(hindiUnavailableHadith, AppLanguage.URDU)
        assertTrue(urResolved.isAvailable)
        assertEquals("اعمال کا دارومدار نیتوں پر ہے۔", urResolved.text)

        val arResolved = LocalizedContentResolver.resolveHadithTranslation(hindiUnavailableHadith, AppLanguage.ARABIC)
        assertFalse(arResolved.isAvailable)
        assertNull(arResolved.text)

        val hiResolved = LocalizedContentResolver.resolveHadithTranslation(hindiUnavailableHadith, AppLanguage.HINDI)
        assertFalse(hiResolved.isAvailable)
        assertNull(hiResolved.text)
        assertNotNull(hiResolved.unavailableNotice)
        assertFalse(hiResolved.unavailableNotice!!.contains("Actions are judged by intentions"))
    }

    @Test
    fun duaScreen_rendersSingleSelectedLanguage_withoutMultiLanguageStacking() {
        val sampleDua = DuaData.duas.first()

        composeRule.setContent {
            NooreHidayatTheme {
                DuasScreen(language = AppLanguage.URDU, onBack = {}, initialDuaId = sampleDua.id)
            }
        }
        composeRule.waitForIdle()

        // No per-screen multi-language toggle chip exists
        assertEquals(0, composeRule.onAllNodesWithText("All Languages").fetchSemanticsNodes().size)
        // Arabic original Dua remains displayed even in Urdu mode
        composeRule.onNodeWithText(sampleDua.arabic, substring = true).assertExists()
        // Urdu translation is shown; English translation must NOT also stack below it
        composeRule.onNodeWithText(sampleDua.translationUr, substring = true).assertExists()
        if (sampleDua.translationEn != sampleDua.translationUr) {
            composeRule.onAllNodesWithText(sampleDua.translationEn).assertCountEquals(0)
        }
    }

    @Test
    fun hadithScreen_arabicMode_keepsArabicOriginalOnly_withoutEnglishOrUrduTranslation() {
        val firstHadith = HadithData.hadiths.first()

        composeRule.setContent {
            NooreHidayatTheme {
                HadithScreen(language = AppLanguage.ARABIC, onBack = {}, initialHadithId = firstHadith.id)
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithText(firstHadith.textArabic, substring = true).assertExists()
        composeRule.onAllNodesWithText(firstHadith.textEnglish).assertCountEquals(0)
        composeRule.onAllNodesWithText(firstHadith.textUrdu).assertCountEquals(0)
    }

    @Test
    fun hijriEvents_renderOnlySelectedLanguageTitleAndDescription() {
        composeRule.setContent {
            NooreHidayatTheme {
                HijriCalendarScreen(language = AppLanguage.HINDI, onBack = {})
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("hijri_tab_events").performClick()
        composeRule.waitForIdle()

        // Islamic New Year in Hindi shown, English & Urdu titles are not stacked simultaneously
        composeRule.onNodeWithText("इस्लामी नया साल (1 मुहर्रम)").assertIsDisplayed()
        composeRule.onAllNodesWithText("Islamic New Year").assertCountEquals(0)
        composeRule.onAllNodesWithText("یکم محرم الحرام (ہجری سالِ نو)").assertCountEquals(0)
    }

    @Test
    fun tafseerEditions_strictlyFollowSelectedLanguage_onlineSync() {
        val enScholars = QuranTafseerRepository.getTop5Scholars(AppLanguage.ENGLISH, appContext)
        val arScholars = QuranTafseerRepository.getTop5Scholars(AppLanguage.ARABIC, appContext)
        val hiScholars = QuranTafseerRepository.getTop5Scholars(AppLanguage.HINDI, appContext)
        val urScholars = QuranTafseerRepository.getTop5Scholars(AppLanguage.URDU, appContext)

        assertEquals(
            listOf("en_ibn_kathir", "en_maariful_quran", "en_jalalayn", "en_ibn_abbas", "en_tazkirul_quran"),
            enScholars.map { it.key }
        )
        assertEquals(
            listOf("ar_ibn_kathir", "ar_tabari", "ar_qurtubi", "ar_baghawi", "ar_saadi", "ar_wasit", "ar_jalalayn"),
            arScholars.map { it.key }
        )
        assertEquals(listOf("hi_al_mukhtasar"), hiScholars.map { it.key })
        assertEquals(
            listOf("ur_ibn_kathir", "ur_bayan_ul_quran", "ur_fi_zilal", "ur_saadi", "ur_tazkirul_quran"),
            urScholars.map { it.key }
        )

        // Missing language editions must NOT fallback silently to English notice text
        val noticeHi = LocalizedContentResolver.unavailableTafseerEditionNotice(
            language = AppLanguage.HINDI,
            scholarTitle = "Ma'ariful Quran",
            officialUrl = "https://www.quran.com"
        )
        assertTrue(noticeHi.contains("हिन्दी"))
        assertFalse(noticeHi.contains("currently not available for offline/inline English"))
    }

    @Test
    fun notificationsAndLocationDisplay_followActiveSettingsLanguageAcrossAll4Languages() {
        val repo = UserPreferencesRepository(appContext)
        val nm = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val shadowNm = Shadows.shadowOf(nm)

        // Prayer notification localized in Urdu
        repo.setLanguage(AppLanguage.URDU)
        PrayerNotificationManager.postPrayerNotification(appContext, "fajr", "05:10 AM", soundType = "silent", notifId = 501)
        val urNotif = shadowNm.getNotification(501)
        assertNotNull(urNotif)
        val urTitle = urNotif.extras.getString(Notification.EXTRA_TITLE) ?: ""
        assertTrue("Expected Urdu prayer title, got: $urTitle", urTitle.contains(AppLocalization.get("fajr", AppLanguage.URDU)))

        // Prayer notification localized in Arabic
        repo.setLanguage(AppLanguage.ARABIC)
        PrayerNotificationManager.postPrayerNotification(appContext, "maghrib", "06:30 PM", soundType = "silent", notifId = 502)
        val arNotif = shadowNm.getNotification(502)
        val arTitle = arNotif.extras.getString(Notification.EXTRA_TITLE) ?: ""
        assertTrue("Expected Arabic prayer title, got: $arTitle", arTitle.contains(AppLocalization.get("maghrib", AppLanguage.ARABIC)))

        // Daily Reminder notifications localize across all 4 languages
        repo.setLanguage(AppLanguage.HINDI)
        DailyReminderScheduler.postReminderNotification(appContext, DailyReminderType.QURAN, AppLanguage.HINDI)
        val hiReminder = shadowNm.getNotification(DailyReminderType.QURAN.requestCode)
        assertNotNull(hiReminder)
        assertEquals("दैनिक क़ुरआन तिलावत", hiReminder.extras.getString(Notification.EXTRA_TITLE))

        // Ramadan alarm titles localized per Settings language
        val titles = AppLanguage.entries.map { lang ->
            RamadanAlarmScheduler.localizedTitleForKey("sehri_pre", lang)!!
        }.toSet()
        assertEquals(4, titles.size)

        // Location city display localized
        val makkah = UserLocation(21.4225, 39.8262, "Makkah", "Saudi Arabia", "Asia/Riyadh", sourceType = LocationSource.PRESET_CITY)
        assertEquals("مکہ مکرمہ", AppLocationManager.localizedCityDisplay(makkah, AppLanguage.URDU))
        assertEquals("مكة المكرمة", AppLocationManager.localizedCityDisplay(makkah, AppLanguage.ARABIC))
        assertEquals("मक्का", AppLocationManager.localizedCityDisplay(makkah, AppLanguage.HINDI))
        assertEquals("Makkah", AppLocationManager.localizedCityDisplay(makkah, AppLanguage.ENGLISH))
    }
}
