package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.model.AppTheme
import com.example.data.notification.PrayerNotificationManager
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.QuranTranslationTafseerRepository
import com.example.data.repository.StorageBreakdown
import com.example.data.repository.UserPreferencesRepository
import com.example.data.repository.VerifiedTafseerCatalog
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.screens.settings.*
import com.example.ui.theme.LocalAppSettings

private data class SettingsSectionMeta(
    val id: String,
    val titleKey: String,
    val keywords: List<String>,
    val icon: ImageVector
)

@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage,
    currentTheme: AppTheme,
    onLanguageSelected: (AppLanguage) -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onNavigateToBookmarks: () -> Unit = {},
    onNavigateToPrayerTimes: () -> Unit = {},
    onNavigateToQibla: () -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prefsRepo = remember(context) { UserPreferencesRepository(context) }
    val appSettings by prefsRepo.appSettings.collectAsStateWithLifecycle()
    val localSettings = LocalAppSettings.current
    val effectiveSettings = remember(appSettings, localSettings, currentTheme, currentLanguage) {
        appSettings.copy(
            language = currentLanguage,
            theme = currentTheme
        )
    }

    val catalogRepo = remember(context) { QuranTranslationTafseerRepository.getInstance(context) }
    val tafseerSources by catalogRepo.observeTafseerSources().collectAsStateWithLifecycle(initialValue = emptyList())
    val translationCatalog by catalogRepo.observeTranslationCatalog().collectAsStateWithLifecycle(initialValue = emptyList())
    val selectedTranslatorIds by catalogRepo.selectedTranslatorIds.collectAsStateWithLifecycle()

    LaunchedEffect(context) {
        AppLocationManager.init(context)
        PrayerCalculator.init(context)
        IslamicCalendarRepository.init(context)
        PrayerNotificationManager.init(context)
        catalogRepo.ensureSeeded()
    }

    var searchQuery by remember { mutableStateOf("") }
    var storageBreakdown by remember { mutableStateOf(prefsRepo.getStorageBreakdown()) }

    // Language, Appearance (with 7 themes), and Internet/Storage start expanded so primary settings & tests are immediately visible
    var expandedSections by remember {
        mutableStateOf(setOf("language", "appearance", "storage"))
    }

    var showPinDialog by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showResetPrefsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val sectionsMeta = remember {
        listOf(
            SettingsSectionMeta(
                id = "language",
                titleKey = "settings_sec_language",
                keywords = listOf("language", "urdu", "arabic", "hindi", "english", "rtl", "country", "currency", "date", "number", "timezone", "زبان", "ملک", "اللغة", "भाषा"),
                icon = Icons.Default.Translate
            ),
            SettingsSectionMeta(
                id = "appearance",
                titleKey = "settings_sec_appearance",
                keywords = listOf("theme", "dark", "light", "system", "accent", "color", "font", "scale", "card", "home", "reset theme", "تھیم", "رنگ", "المظهر", "थीम"),
                icon = Icons.Default.Palette
            ),
            SettingsSectionMeta(
                id = "quran",
                titleKey = "settings_sec_quran",
                keywords = listOf("quran", "arabic font", "amiri", "noto", "naskh", "spacing", "ayah", "inline", "bookmark", "قرآن", "خط", "القرآن", "क़ुरआन"),
                icon = Icons.Default.MenuBook
            ),
            SettingsSectionMeta(
                id = "tafseer",
                titleKey = "settings_sec_tafseer",
                keywords = listOf("tafseer", "tafsir", "translator", "scholar", "ibn kathir", "jalalayn", "maarif", "تفسیر", "ترجمہ", "التفسير", "तफ़सीर"),
                icon = Icons.Default.AutoStories
            ),
            SettingsSectionMeta(
                id = "audio",
                titleKey = "settings_sec_audio",
                keywords = listOf("audio", "reciter", "qari", "speed", "repeat", "alafasy", "abdul basit", "sudais", "تلاوت", "قاری", "القارئ", "ऑडियो"),
                icon = Icons.Default.Headphones
            ),
            SettingsSectionMeta(
                id = "prayer",
                titleKey = "settings_sec_prayer",
                keywords = listOf("prayer", "qibla", "hijri", "calendar", "ruet", "umm al-qura", "asr", "hanafi", "method", "location", "compass", "نماز", "قبلہ", "ہجری", "القبلة", "नमाज़"),
                icon = Icons.Default.Explore
            ),
            SettingsSectionMeta(
                id = "notifications",
                titleKey = "settings_sec_notifications",
                keywords = listOf("notification", "alert", "azan", "adhan", "reminder", "adhkar", "hadith", "dua", "dnd", "quiet", "lead time", "vibration", "اذان", "اطلاع", "تنبيه", "सूचना"),
                icon = Icons.Default.Notifications
            ),
            SettingsSectionMeta(
                id = "learning",
                titleKey = "settings_sec_learning",
                keywords = listOf("learning", "academy", "hifz", "revision", "goal", "streak", "lesson", "quiz", "تعلیم", "حفظ", "التعلم", "लर्निंग"),
                icon = Icons.Default.School
            ),
            SettingsSectionMeta(
                id = "storage",
                titleKey = "settings_sec_storage",
                keywords = listOf("wifi", "wi-fi", "mobile data", "streaming", "storage", "cache", "clear cache", "offline", "download", "اسٹوریج", "کیش", "وائی فائی", "التخزين", "कैश"),
                icon = Icons.Default.Storage
            ),
            SettingsSectionMeta(
                id = "privacy",
                titleKey = "settings_sec_privacy",
                keywords = listOf("privacy", "security", "pin", "lock", "permission", "exact alarm", "battery", "history", "reset preferences", "policy", "رازداری", "پن", "الخصوصية", "गोपनीयता"),
                icon = Icons.Default.Security
            ),
            SettingsSectionMeta(
                id = "accessibility",
                titleKey = "settings_sec_accessibility",
                keywords = listOf("accessibility", "contrast", "high contrast", "motion", "reduced motion", "large text", "تباين", "کنٹراسٹ", "सुगम्यता"),
                icon = Icons.Default.AccessibilityNew
            ),
            SettingsSectionMeta(
                id = "about",
                titleKey = "settings_sec_about",
                keywords = listOf("about", "version", "provenance", "source", "diagnostic", "feedback", "tanzil", "help", "ذرائع", "تعارف", "حول", "स्रोत"),
                icon = Icons.Default.Info
            )
        )
    }

    val trimmedQuery = searchQuery.trim()
    val filteredSections = remember(trimmedQuery, currentLanguage) {
        if (trimmedQuery.isBlank()) {
            sectionsMeta
        } else {
            sectionsMeta.filter { meta ->
                val title = AppLocalization.get(meta.titleKey, currentLanguage)
                title.contains(trimmedQuery, ignoreCase = true) ||
                    meta.keywords.any { it.contains(trimmedQuery, ignoreCase = true) }
            }
        }
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("settings", currentLanguage),
                language = currentLanguage,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val scrollState = rememberScrollState()
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .testTag("settings_list")
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = AppLocalization.get("clear_search", currentLanguage))
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = AppLocalization.get("settings_search_hint", currentLanguage),
                            maxLines = 1
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_search_input")
                )

                filteredSections.forEach { meta ->
                    key(meta.id) {
                        val isExpanded = if (trimmedQuery.isNotBlank()) true else expandedSections.contains(meta.id)
                        val modeLabel = when (effectiveSettings.appearanceMode) {
                            com.example.data.model.AppearanceMode.SYSTEM -> AppLocalization.get("settings_mode_system", currentLanguage)
                            com.example.data.model.AppearanceMode.LIGHT -> AppLocalization.get("settings_mode_light", currentLanguage)
                            com.example.data.model.AppearanceMode.DARK -> AppLocalization.get("settings_mode_dark", currentLanguage)
                        }
                        val summary = when (meta.id) {
                            "language" -> "${currentLanguage.nativeName} • ${effectiveSettings.dateFormat.id.uppercase()}"
                            "appearance" -> "${currentTheme.localizedTitle(currentLanguage)} • $modeLabel"
                            "quran" -> "${effectiveSettings.arabicFontSize.toInt()}sp • ${effectiveSettings.arabicFontOption.name}"
                            "tafseer" -> {
                                val langSources = tafseerSources.filter {
                                    it.onlineAllowed && it.originalLanguage.equals(currentLanguage.code, ignoreCase = true)
                                }
                                langSources.firstOrNull { it.id == effectiveSettings.defaultTafseerScholarId }?.bookTitle
                                    ?: langSources.firstOrNull()?.bookTitle
                                    ?: VerifiedTafseerCatalog.onlineEditionsFor(currentLanguage).first().bookTitle
                            }
                            "audio" -> loc(
                                currentLanguage,
                                "${effectiveSettings.audioPlaybackSpeed}x • Repeat ${effectiveSettings.ayahRepeatCount}x",
                                "${effectiveSettings.audioPlaybackSpeed}x • تکرار ${effectiveSettings.ayahRepeatCount}x",
                                "${effectiveSettings.audioPlaybackSpeed}x • تكرار ${effectiveSettings.ayahRepeatCount}x",
                                "${effectiveSettings.audioPlaybackSpeed}x • दोहराव ${effectiveSettings.ayahRepeatCount}x"
                            )
                            "prayer" -> "${PrayerCalculator.calcConfig.value.method.defaultCountry} • ${PrayerCalculator.calcConfig.value.asrJuristic.name}"
                            "notifications" -> if (effectiveSettings.notificationsEnabled) {
                                loc(currentLanguage, "Active", "فعال", "مفعّل", "सक्रिय")
                            } else {
                                loc(currentLanguage, "Paused", "بند", "متوقف", "रुका हुआ")
                            }
                            "learning" -> loc(
                                currentLanguage,
                                "${effectiveSettings.dailyGoalLevel.minutesPerDay} min/day",
                                "${effectiveSettings.dailyGoalLevel.minutesPerDay} منٹ یومیہ",
                                "${effectiveSettings.dailyGoalLevel.minutesPerDay} دقيقة/يوم",
                                "${effectiveSettings.dailyGoalLevel.minutesPerDay} मिनट/दिन"
                            )
                            "storage" -> {
                                val netLabel = if (effectiveSettings.wifiOnlyDownloads) {
                                    loc(currentLanguage, "Wi-Fi Only", "صرف وائی فائی", "واي فاي فقط", "केवल वाई-फ़ाई")
                                } else {
                                    loc(currentLanguage, "Any Network", "تمام نیٹ ورکس", "أي شبكة", "कोई भी नेटवर्क")
                                }
                                val cacheLabel = loc(currentLanguage, "Cache", "کیش", "مؤقت", "कैश")
                                "$netLabel • $cacheLabel ${formatBytesReadable(storageBreakdown.cacheBytes)}"
                            }
                            "privacy" -> if (effectiveSettings.isPinLockEnabled) {
                                loc(currentLanguage, "PIN Lock Active", "پن لاک فعال", "القفل مفعّل", "पिन लॉक सक्रिय")
                            } else {
                                loc(currentLanguage, "No PIN Lock", "پن لاک بند", "بدون قفل", "पिन लॉक नहीं")
                            }
                            "accessibility" -> loc(
                                currentLanguage,
                                "${(effectiveSettings.uiTextScale * 100).toInt()}% text",
                                "${(effectiveSettings.uiTextScale * 100).toInt()}% متن",
                                "${(effectiveSettings.uiTextScale * 100).toInt()}% نص",
                                "${(effectiveSettings.uiTextScale * 100).toInt()}% टेक्स्ट"
                            )
                            "about" -> "v${com.example.BuildConfig.VERSION_NAME}"
                            else -> ""
                        }

                        SettingsSectionCard(
                            sectionId = meta.id,
                            title = AppLocalization.get(meta.titleKey, currentLanguage),
                            summary = summary,
                            icon = meta.icon,
                            expanded = isExpanded,
                            onToggleExpand = {
                                expandedSections = if (expandedSections.contains(meta.id)) {
                                    expandedSections - meta.id
                                } else {
                                    expandedSections + meta.id
                                }
                            }
                        ) {
                            when (meta.id) {
                                "language" -> LanguageRegionSectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    context = context,
                                    prefsRepo = prefsRepo,
                                    onLanguageSelected = { newLang ->
                                        prefsRepo.setLanguage(newLang)
                                        onLanguageSelected(newLang)
                                    }
                                )
                                "appearance" -> AppearanceSectionContent(
                                    language = currentLanguage,
                                    currentTheme = currentTheme,
                                    settings = effectiveSettings,
                                    prefsRepo = prefsRepo,
                                    onThemeSelected = { newTheme ->
                                        prefsRepo.setTheme(newTheme)
                                        onThemeSelected(newTheme)
                                    }
                                )
                                "quran" -> QuranReadingSectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    prefsRepo = prefsRepo,
                                    onNavigateToBookmarks = onNavigateToBookmarks
                                )
                                "tafseer" -> TafseerTranslationSectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    prefsRepo = prefsRepo,
                                    tafseerSources = tafseerSources,
                                    translationCatalog = translationCatalog,
                                    selectedTranslatorIds = selectedTranslatorIds,
                                    catalogRepo = catalogRepo
                                )
                                "audio" -> AudioPlaybackSectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    prefsRepo = prefsRepo
                                )
                                "prayer" -> PrayerLocationSectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    context = context,
                                    prefsRepo = prefsRepo,
                                    onNavigateToPrayerTimes = onNavigateToPrayerTimes,
                                    onNavigateToQibla = onNavigateToQibla
                                )
                                "notifications" -> NotificationsRemindersSectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    context = context,
                                    prefsRepo = prefsRepo
                                )
                                "learning" -> LearningRoutineSectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    context = context,
                                    prefsRepo = prefsRepo
                                )
                                "storage" -> InternetStorageSectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    context = context,
                                    prefsRepo = prefsRepo,
                                    storageBreakdown = storageBreakdown,
                                    onRefreshStorage = { storageBreakdown = prefsRepo.getStorageBreakdown() }
                                )
                                "privacy" -> PrivacySecuritySectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    context = context,
                                    onOpenPinDialog = { showPinDialog = true },
                                    onOpenClearHistoryDialog = { showClearHistoryDialog = true },
                                    onOpenResetPrefsDialog = { showResetPrefsDialog = true },
                                    onOpenPrivacyDialog = { showPrivacyDialog = true }
                                )
                                "accessibility" -> AccessibilitySectionContent(
                                    language = currentLanguage,
                                    settings = effectiveSettings,
                                    prefsRepo = prefsRepo
                                )
                                "about" -> AboutProvenanceSectionContent(
                                    language = currentLanguage,
                                    context = context
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPinDialog) {
        var pinInput by remember { mutableStateOf("") }
        var pinError by remember { mutableStateOf<String?>(null) }
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = {
                Text(
                    if (effectiveSettings.isPinLockEnabled) {
                        loc(currentLanguage, "Update or Disable App PIN", "ایپ پن تبدیل یا بند کریں", "تحديث أو تعطيل رمز التطبيق", "ऐप पिन बदलें या हटाएं")
                    } else {
                        loc(currentLanguage, "Set 4-Digit App PIN", "4 ہندسوں کا پن سیٹ کریں", "تعيين رمز PIN من 4 أرقام", "4-अंकीय ऐप पिन सेट करें")
                    }
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { if (it.length <= 4 && it.all { ch -> ch.isDigit() }) { pinInput = it; pinError = null } },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        label = { Text(loc(currentLanguage, "4-digit PIN", "4 ہندسوں کا پن", "رمز من 4 أرقام", "4 अंकों का पिन")) },
                        isError = pinError != null,
                        supportingText = pinError?.let { err -> { Text(err) } },
                        modifier = Modifier.fillMaxWidth().testTag("pin_setup_input")
                    )
                    if (effectiveSettings.isPinLockEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                prefsRepo.setAppPin(null)
                                showPinDialog = false
                                Toast.makeText(
                                    context,
                                    loc(currentLanguage, "App PIN Lock disabled", "ایپ پن لاک ہٹا دیا گیا", "تم تعطيل رمز القفل", "ऐप पिन लॉक बंद हो गया"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.testTag("disable_pin_btn")
                        ) {
                            Text(loc(currentLanguage, "Disable Existing PIN", "موجودہ پن ہٹائیں", "إلغاء الرمز الحالي", "मौजूदा पिन हटाएं"), color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (pinInput.length == 4) {
                            prefsRepo.setAppPin(pinInput)
                            showPinDialog = false
                            Toast.makeText(
                                context,
                                loc(currentLanguage, "App PIN saved", "ایپ پن محفوظ ہو گیا", "تم حفظ الرمز", "ऐप पिन सहेजा गया"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            pinError = loc(currentLanguage, "Please enter 4 digits", "براہِ کرم 4 ہندسے درج کریں", "يرجى إدخال 4 أرقام", "कृपया 4 अंक दर्ज करें")
                        }
                    },
                    modifier = Modifier.testTag("save_pin_btn")
                ) {
                    Text(loc(currentLanguage, "Save PIN", "محفوظ کریں", "حفظ", "सहेजें"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text(loc(currentLanguage, "Cancel", "منسوخ", "إلغاء", "रद्द करें"))
                }
            }
        )
    }

    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = { Text(loc(currentLanguage, "Clear Reading History?", "مطالعہ کا ریکارڈ صاف کریں؟", "مسح سجل القراءة؟", "पठन इतिहास साफ़ करें?")) },
            text = {
                Text(
                    loc(
                        currentLanguage,
                        "This clears recent Dua & Hadith history only. Saved Bookmarks, Favorites, and Learning progress remain safe.",
                        "صرف حالیہ دعا و حدیث کی تاریخ صاف ہوگی۔ بک مارکس، پسندیدہ اور تعلیمی پیشرفت محفوظ رہے گی۔",
                        "سيتم مسح سجل القراءة الأخير فقط مع بقاء المفضلة وتقدم التعلم.",
                        "केवल हालिया इतिहास साफ़ होगा। सहेजे गए बुकमार्क व प्रगति सुरक्षित रहेंगे।"
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        prefsRepo.clearReadingHistory()
                        showClearHistoryDialog = false
                        Toast.makeText(
                            context,
                            loc(currentLanguage, "Recent history cleared", "حالیہ تاریخ صاف ہو گئی", "تم مسح السجل الحديث", "हालिया इतिहास साफ़ हुआ"),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.testTag("confirm_clear_history_btn")
                ) {
                    Text(loc(currentLanguage, "Clear History", "صاف کریں", "مسح", "साफ़ करें"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text(loc(currentLanguage, "Cancel", "منسوخ", "إلغاء", "रद्द करें"))
                }
            }
        )
    }

    if (showResetPrefsDialog) {
        AlertDialog(
            onDismissRequest = { showResetPrefsDialog = false },
            title = { Text(AppLocalization.get("settings_reset_prefs", currentLanguage)) },
            text = {
                Text(
                    loc(
                        currentLanguage,
                        "Themes, fonts, playback speed, and reminder toggles will return to defaults. Your Bookmarks, Offline Audio, Podcasts, Hadith Packs, Tasbeeh, and Academy progress will NOT be touched.",
                        "تھیم، فونٹ اور یاد دہانیاں اصل حالت پر آ جائیں گی۔ آپ کے بک مارکس، آف لائن آڈیو، پوڈکاسٹس، تسبیح اور اکیڈمی ریکارڈ ہرگز حذف نہیں ہوں گے۔",
                        "ستعود الإعدادات للافتراضي دون المساس بالمفضلة أو التنزيلات أو التقدم.",
                        "थीम, फ़ॉन्ट व रिमाइंडर डिफ़ॉल्ट हो जाएंगे। आपके बुकमार्क, डाउनलोड, तस्बीह और प्रगति पूरी तरह सुरक्षित रहेंगे।"
                    )
                )
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    onClick = {
                        prefsRepo.resetPreferencesOnly()
                        onLanguageSelected(AppLanguage.ENGLISH)
                        onThemeSelected(AppTheme.EMERALD_GOLD)
                        showResetPrefsDialog = false
                        Toast.makeText(
                            context,
                            loc(currentLanguage, "Preferences reset to defaults", "ترتیبات اصل حالت پر بحال ہو گئیں", "تمت إعادة التفضيلات إلى الافتراضي", "प्राथमिकताएं डिफ़ॉल्ट पर रीसेट हुईं"),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.testTag("confirm_reset_prefs_btn")
                ) {
                    Text(loc(currentLanguage, "Reset", "ری سیٹ", "إعادة ضبط", "रीसेट"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetPrefsDialog = false }) {
                    Text(loc(currentLanguage, "Cancel", "منسوخ", "إلغاء", "रद्द करें"))
                }
            }
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text(loc(currentLanguage, "Privacy & Transparency", "رازداری اور شفافیت", "الخصوصية والشفافية", "गोपनीयता और पारदर्शिता")) },
            text = {
                Text(
                    when (currentLanguage) {
                        AppLanguage.URDU -> "• کوئی اکاؤنٹ لاگ ان یا اینالیٹکس ٹریکنگ شامل نہیں ہے۔\n" +
                            "• مقام کے کوآرڈینیٹس صرف آپ کے فون پر نماز اور قبلہ کے حساب کے لیے محفوظ رہتے ہیں۔\n" +
                            "• بک مارکس، تسبیح، اکیڈمی ریکارڈ اور ایپ پن صرف آپ کی ڈیوائس پر محفوظ رہتے ہیں۔\n" +
                            "• انٹرنیٹ صرف تلاوت، پوڈکاسٹس، لائیو حرمین، آن لائن تفسیر یا کرنسی ریٹ کے لیے استعمال ہوتا ہے۔"
                        AppLanguage.ARABIC -> "• لا يوجد تسجيل دخول ولا تتبع إحصائي للمستخدم.\n" +
                            "• تبقى إحداثيات الموقع محلياً على جهازك لحساب المواقيت واتجاه القبلة.\n" +
                            "• تبقى المفضلة والتسبيح وتقدم التعلم ورمز القفل محلياً على الجهاز.\n" +
                            "• يُستخدم الإنترنت فقط عند تشغيل التلاوات أو البث المباشر أو تنزيل التفاسير والبيانات."
                        AppLanguage.HINDI -> "• कोई अकाउंट लॉगिन या एनालिटिक्स ट्रैकिंग नहीं की जाती।\n" +
                            "• स्थान निर्देशांक केवल आपके डिवाइस पर नमाज़ और क़िबला गणना के लिए रहते हैं।\n" +
                            "• बुकमार्क, तस्बीह, लर्निंग प्रगति और ऐप पिन हैश पूरी तरह डिवाइस पर सुरक्षित रहते हैं।\n" +
                            "• इंटरनेट केवल तिलावत, पॉडकास्ट, लाइव हरमैन या तफ़सीर डाउनलोड के लिए उपयोग होता है।"
                        AppLanguage.ENGLISH -> "• No account login or analytics tracking is used.\n" +
                            "• Location coordinates stay local on your device for Prayer Times and Qibla calculations (system Geocoder is used only when resolving city names).\n" +
                            "• Bookmarks, Tasbeeh counts, Academy progress, and App PIN hash (salted SHA-256) remain strictly on device.\n" +
                            "• Network access is used solely when you choose to stream/download Quran audio, Podcasts, Live Haramain feeds, Tafseer online editions, or Zakat currency rates."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text(loc(currentLanguage, "Understood", "ٹھیک ہے", "موافق", "समझ गया"))
                }
            }
        )
    }
}
