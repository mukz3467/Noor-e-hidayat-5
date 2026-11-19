package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.notification.DailyReminderScheduler
import com.example.data.notification.PrayerNotificationManager
import com.example.data.ramadan.RamadanDailyRefreshWorker
import com.example.data.ramadan.RamadanRepository
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.QuranAudioManager
import com.example.data.repository.QuranData
import com.example.data.repository.QuranOfflineSyncManager
import com.example.data.repository.QuranTafseerRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.ui.components.AppDirectionProvider
import com.example.ui.components.FullscreenVideoController
import com.example.ui.components.LocalFullscreenVideoController
import com.example.ui.screens.*
import com.example.ui.screens.asma.AsmaUlHusnaScreen
import com.example.ui.screens.hajj.HajjUmrahScreen
import com.example.ui.screens.mosque.MosqueFinderScreen
import com.example.ui.screens.ramadan.RamadanCompanionScreen
import com.example.data.prophets.ProphetRepository
import com.example.data.salah.MissedSalahRepository
import com.example.ui.screens.prophets.StoriesOfProphetsScreen
import com.example.ui.screens.salah.MissedSalahScreen
import com.example.ui.screens.seerah.SeerahScreen
import com.example.ui.screens.business.BusinessAcademyScreen
import com.example.ui.screens.huqooq.HuqooqAcademyScreen
import com.example.ui.screens.shifa.ShifaRuqyahScreen
import com.example.ui.theme.NooreHidayatTheme

sealed class Screen {
    object Home : Screen()
    object QuranList : Screen()
    data class QuranReader(val surahNumber: Int) : Screen()
    object LiveStream : Screen()
    data class Lectures(val initialQuery: String? = null) : Screen()
    object Learning : Screen()
    object MoreHub : Screen()
    data class Hadith(val initialHadithId: String? = null) : Screen()
    data class PrayerTimes(val openAzanSettings: Boolean = false) : Screen()
    object Qibla : Screen()
    object Zakat : Screen()
    data class Duas(val initialDuaId: String? = null) : Screen()
    object Tasbeeh : Screen()
    object Calendar : Screen()
    object Podcasts : Screen()
    object Bookmarks : Screen()
    object RamadanCompanion : Screen()
    object AsmaUlHusna : Screen()
    object HajjUmrah : Screen()
    object MosqueFinder : Screen()
    object Seerah : Screen()
    object MissedSalahTracker : Screen()
    object StoriesOfProphets : Screen()
    data class BusinessAcademy(val returnToMoreHub: Boolean = false) : Screen()
    object HuqooqAcademy : Screen()
    object ShifaRuqyah : Screen()
    object Settings : Screen()
}

class MainActivity : ComponentActivity() {

    private lateinit var preferencesRepo: UserPreferencesRepository
    private val fullscreenController = FullscreenVideoController()

    override fun onDestroy() {
        fullscreenController.detachHost()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesRepo = UserPreferencesRepository(applicationContext)
        QuranData.init(applicationContext)
        QuranAudioManager.init(applicationContext)
        AppLocationManager.initializeAndRefreshIfPermitted(applicationContext)
        PrayerCalculator.init(applicationContext)
        IslamicCalendarRepository.init(applicationContext)
        PrayerNotificationManager.init(applicationContext)
        PrayerNotificationManager.scheduleNextRollingWindow(applicationContext, reason = "app_cold_start")
        DailyReminderScheduler.rescheduleAll(applicationContext)
        RamadanRepository.init(applicationContext)
        RamadanDailyRefreshWorker.schedulePeriodicRefresh(applicationContext)
        QuranOfflineSyncManager.startAutoSync(applicationContext)
        QuranTafseerRepository.init(applicationContext)
        MissedSalahRepository.init(applicationContext)
        ProphetRepository.init(applicationContext)
        enableEdgeToEdge()

        setContent {
            val appSettings by preferencesRepo.appSettings.collectAsStateWithLifecycle()
            val language = appSettings.language
            val theme = appSettings.theme

            var isPinUnlocked by remember { mutableStateOf(!appSettings.isPinLockEnabled) }
            LaunchedEffect(appSettings.isPinLockEnabled) {
                if (!appSettings.isPinLockEnabled) {
                    isPinUnlocked = true
                }
            }

            var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

            // Exit fullscreen video BEFORE popping app navigation when Back is pressed
            if (fullscreenController.isFullscreen) {
                BackHandler {
                    fullscreenController.exitFullscreen()
                }
            } else if (currentScreen !is Screen.Home) {
                BackHandler {
                    currentScreen = when (val s = currentScreen) {
                        is Screen.QuranReader -> Screen.QuranList
                        is Screen.BusinessAcademy -> if (s.returnToMoreHub) Screen.MoreHub else Screen.Home
                        is Screen.RamadanCompanion,
                        is Screen.AsmaUlHusna,
                        is Screen.HajjUmrah,
                        is Screen.MosqueFinder,
                        is Screen.Seerah,
                        is Screen.MissedSalahTracker,
                        is Screen.StoriesOfProphets,
                        is Screen.ShifaRuqyah -> Screen.MoreHub
                        else -> Screen.Home
                    }
                }
            }

            CompositionLocalProvider(LocalFullscreenVideoController provides fullscreenController) {
                NooreHidayatTheme(selectedTheme = theme, settings = appSettings) {
                    AppDirectionProvider(language = language) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                if (appSettings.isPinLockEnabled && !isPinUnlocked) {
                                    PinUnlockOverlay(
                                        language = language,
                                        onVerifyPin = { pin -> preferencesRepo.verifyAppPin(pin) },
                                        onUnlocked = { isPinUnlocked = true }
                                    )
                                } else {
                                when (val screen = currentScreen) {
                            is Screen.Home -> {
                                HomeScreen(
                                    language = language,
                                    onNavigateToQuran = { currentScreen = Screen.QuranList },
                                    onNavigateToLive = { currentScreen = Screen.LiveStream },
                                    onNavigateToLectures = { query -> currentScreen = Screen.Lectures(query) },
                                    onNavigateToLearning = { currentScreen = Screen.Learning },
                                    onNavigateToMoreHub = { currentScreen = Screen.MoreHub },
                                    onNavigateToPrayerTimes = { currentScreen = Screen.PrayerTimes() },
                                    onNavigateToSettings = { currentScreen = Screen.Settings },
                                    onNavigateToHuqooqAcademy = { currentScreen = Screen.HuqooqAcademy },
                                    onNavigateToBusinessAcademy = { currentScreen = Screen.BusinessAcademy(returnToMoreHub = false) },
                                    onLanguageChanged = { newLang -> preferencesRepo.setLanguage(newLang) }
                                )
                            }
                            is Screen.QuranList -> {
                                QuranListScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home },
                                    onSelectSurah = { surahNumber ->
                                        currentScreen = Screen.QuranReader(surahNumber)
                                    }
                                )
                            }
                            is Screen.QuranReader -> {
                                QuranReaderScreen(
                                    surahNumber = screen.surahNumber,
                                    language = language,
                                    onBack = { currentScreen = Screen.QuranList },
                                    onNavigateToSurah = { newSurah ->
                                        currentScreen = Screen.QuranReader(newSurah)
                                    }
                                )
                            }
                            is Screen.LiveStream -> {
                                LiveStreamScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                            is Screen.Lectures -> {
                                LecturesScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home },
                                    initialQuery = screen.initialQuery
                                )
                            }
                            is Screen.Learning -> {
                                LearningScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                            is Screen.MoreHub -> {
                                MoreHubScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home },
                                    onNavigate = { destinationId ->
                                        currentScreen = when (destinationId) {
                                            "hadith" -> Screen.Hadith()
                                            "duas" -> Screen.Duas()
                                            "tasbeeh" -> Screen.Tasbeeh
                                            "prayer_times" -> Screen.PrayerTimes(openAzanSettings = false)
                                            "qibla" -> Screen.Qibla
                                            "zakat" -> Screen.Zakat
                                            "calendar" -> Screen.Calendar
                                            "podcasts" -> Screen.Podcasts
                                            "bookmarks" -> Screen.Bookmarks
                                            "ramadan_companion" -> Screen.RamadanCompanion
                                            "asma_ul_husna" -> Screen.AsmaUlHusna
                                            "hajj_umrah" -> Screen.HajjUmrah
                                            "mosque_finder" -> Screen.MosqueFinder
                                            "seerah" -> Screen.Seerah
                                            "missed_salah" -> Screen.MissedSalahTracker
                                            "stories_prophets" -> Screen.StoriesOfProphets
                                            "business_academy" -> Screen.BusinessAcademy(returnToMoreHub = true)
                                            "shifa_ruqyah" -> Screen.ShifaRuqyah
                                            "settings" -> Screen.Settings
                                            else -> Screen.Home
                                        }
                                    }
                                )
                            }
                            is Screen.Hadith -> {
                                HadithScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home },
                                    initialHadithId = screen.initialHadithId
                                )
                            }
                            is Screen.PrayerTimes -> {
                                PrayerTimesScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home },
                                    openSettingsInitially = screen.openAzanSettings
                                )
                            }
                            is Screen.Qibla -> {
                                QiblaScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                            is Screen.Zakat -> {
                                ZakatScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                            is Screen.Duas -> {
                                DuasScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home },
                                    initialDuaId = screen.initialDuaId
                                )
                            }
                            is Screen.Tasbeeh -> {
                                TasbeehScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                            is Screen.Calendar -> {
                                HijriCalendarScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                            is Screen.Podcasts -> {
                                PodcastsScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                            is Screen.Bookmarks -> {
                                BookmarksScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home },
                                    onNavigateToSurah = { surahNumber ->
                                        currentScreen = Screen.QuranReader(surahNumber)
                                    },
                                    onNavigateToHadith = { hadithId ->
                                        currentScreen = Screen.Hadith(initialHadithId = hadithId)
                                    },
                                    onNavigateToDuas = { duaId ->
                                        currentScreen = Screen.Duas(initialDuaId = duaId)
                                    }
                                )
                            }
                            is Screen.RamadanCompanion -> {
                                RamadanCompanionScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.MoreHub },
                                    onOpenQuranReader = { surahNum ->
                                        currentScreen = Screen.QuranReader(surahNum)
                                    },
                                    onOpenPrayerTimes = {
                                        currentScreen = Screen.PrayerTimes(openAzanSettings = false)
                                    }
                                )
                            }
                            is Screen.AsmaUlHusna -> {
                                AsmaUlHusnaScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.MoreHub }
                                )
                            }
                            is Screen.HajjUmrah -> {
                                HajjUmrahScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.MoreHub }
                                )
                            }
                            is Screen.MosqueFinder -> {
                                MosqueFinderScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.MoreHub }
                                )
                            }
                            is Screen.Seerah -> {
                                SeerahScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.MoreHub }
                                )
                            }
                            is Screen.MissedSalahTracker -> {
                                MissedSalahScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.MoreHub }
                                )
                            }
                            is Screen.StoriesOfProphets -> {
                                StoriesOfProphetsScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.MoreHub }
                                )
                            }
                            is Screen.BusinessAcademy -> {
                                BusinessAcademyScreen(
                                    language = language,
                                    onBack = {
                                        currentScreen = if (screen.returnToMoreHub) Screen.MoreHub else Screen.Home
                                    }
                                )
                            }
                            is Screen.HuqooqAcademy -> {
                                HuqooqAcademyScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                            is Screen.ShifaRuqyah -> {
                                ShifaRuqyahScreen(
                                    language = language,
                                    onBack = { currentScreen = Screen.MoreHub }
                                )
                            }
                            is Screen.Settings -> {
                                SettingsScreen(
                                    currentLanguage = language,
                                    currentTheme = theme,
                                    onLanguageSelected = { newLang ->
                                        preferencesRepo.setLanguage(newLang)
                                    },
                                    onThemeSelected = { newTheme ->
                                        preferencesRepo.setTheme(newTheme)
                                    },
                                    onNavigateToBookmarks = { currentScreen = Screen.Bookmarks },
                                    onNavigateToPrayerTimes = { currentScreen = Screen.PrayerTimes(openAzanSettings = true) },
                                    onNavigateToQibla = { currentScreen = Screen.Qibla },
                                    onBack = { currentScreen = Screen.Home }
                                )
                            }
                        }
                                }
                    }

                            // Fullscreen Video Overlay Container driven by WebChromeClient.onShowCustomView
                            if (fullscreenController.isFullscreen) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black)
                                )
                            }
                            AndroidView(
                                factory = { ctx ->
                                    FrameLayout(ctx).apply {
                                        visibility = View.GONE
                                        setBackgroundColor(android.graphics.Color.BLACK)
                                        fullscreenController.attachHost(this@MainActivity, this)
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PinUnlockOverlay(
    language: AppLanguage,
    onVerifyPin: (String) -> Boolean,
    onUnlocked: () -> Unit
) {
    var pinInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .testTag("app_pin_lock_overlay")
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.widthIn(max = 380.dp).fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = when (language) {
                        AppLanguage.URDU -> "ایپ لاک"
                        AppLanguage.ARABIC -> "قفل التطبيق"
                        AppLanguage.HINDI -> "ऐप लॉक"
                        AppLanguage.ENGLISH -> "App Lock"
                    },
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = when (language) {
                        AppLanguage.URDU -> "ایپ لاک — پن کوڈ درج کریں"
                        AppLanguage.ARABIC -> "قفل التطبيق — أدخل الرمز السري"
                        AppLanguage.HINDI -> "ऐप लॉक — पिन कोड दर्ज करें"
                        AppLanguage.ENGLISH -> "App Locked — Enter PIN"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = {
                        if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
                            pinInput = it
                            isError = false
                        }
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    isError = isError,
                    label = {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "4 ہندسوں کا پن"
                                AppLanguage.ARABIC -> "رمز من 4 أرقام"
                                AppLanguage.HINDI -> "4 अंकों का पिन"
                                AppLanguage.ENGLISH -> "4-Digit PIN"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().testTag("app_pin_unlock_input")
                )
                if (isError) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = when (language) {
                            AppLanguage.URDU -> "غلط پن کوڈ، دوبارہ کوشش کریں"
                            AppLanguage.ARABIC -> "الرمز غير صحيح، حاول مرة أخرى"
                            AppLanguage.HINDI -> "गलत पिन, पुनः प्रयास करें"
                            AppLanguage.ENGLISH -> "Incorrect PIN. Try again."
                        },
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (onVerifyPin(pinInput)) {
                            onUnlocked()
                        } else {
                            isError = true
                            pinInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("app_pin_unlock_btn")
                ) {
                    Text(
                        when (language) {
                            AppLanguage.URDU -> "ان لاک کریں"
                            AppLanguage.ARABIC -> "فتح التطبيق"
                            AppLanguage.HINDI -> "अनलॉक करें"
                            AppLanguage.ENGLISH -> "Unlock"
                        }
                    )
                }
            }
        }
    }
}

