@file:OptIn(ExperimentalLayoutApi::class)
package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.TafseerSourceEntity
import com.example.data.db.TranslationCatalogEntity
import com.example.data.model.*
import com.example.data.repository.QuranData
import com.example.data.repository.QuranTranslationTafseerRepository
import com.example.data.repository.UserPreferencesRepository
import com.example.data.repository.VerifiedTafseerCatalog
import com.example.data.zakat.ZakatCurrencyCatalog
import com.example.localization.AppLocalization
import com.example.ui.components.noorSelectableChipColors
import com.example.ui.theme.AppAccentPresets
import com.example.ui.theme.ThemeContrastUtils
import com.example.ui.theme.resolveArabicFontFamily
import kotlin.math.roundToInt

@Composable
internal fun LanguageRegionSectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    context: android.content.Context,
    prefsRepo: UserPreferencesRepository,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    val initialSelection = remember { ZakatCurrencyCatalog.loadPersistedSelection(context) }
    var savedCountryIso by remember { mutableStateOf(initialSelection.first.isoCode) }
    var savedCurrencyCode by remember { mutableStateOf(initialSelection.second.code) }
    var showCountryDialog by remember { mutableStateOf(false) }

    SettingsFieldLabel(AppLocalization.get("language_selector", language))

    AppLanguage.entries.forEach { lang ->
        val isSelected = language == lang
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else Color.Transparent
                )
                .selectable(
                    selected = isSelected,
                    role = Role.RadioButton,
                    onClick = { onLanguageSelected(lang) }
                )
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .testTag("lang_option_${lang.code}")
        ) {
            Column {
                Text(
                    text = lang.nativeName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    ),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
                val dirLabel = if (lang.isRtl) {
                    loc(language, "RTL (Right-to-Left)", "دائیں سے بائیں (RTL)", "من اليمين إلى اليسار (RTL)", "दाएँ से बाएँ (RTL)")
                } else {
                    loc(language, "LTR (Left-to-Right)", "بائیں سے دائیں (LTR)", "من اليسار إلى اليمين (LTR)", "बाएँ से दाएँ (LTR)")
                }
                Text(
                    text = "${lang.displayName} • $dirLabel",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = lang.nativeName,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }

    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

    // Country & Currency
    val activeCountry = ZakatCurrencyCatalog.findCountry(savedCountryIso)
    SettingsActionRow(
        title = loc(language, "Country & Currency", "ملک اور کرنسی", "البلد والعملة", "देश और मुद्रा"),
        subtitle = "${activeCountry.flagEmoji} ${activeCountry.displayName} • $savedCurrencyCode",
        icon = Icons.Default.Check,
        onClick = { showCountryDialog = true },
        testTag = "settings_country_currency_btn"
    )

    // Date Format
    SettingsFieldLabel(loc(language, "Date Display Format", "تاریخ کا انداز", "تنسيق التاريخ", "तिथि प्रारूप"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        val options = listOf(
            DateFormatOption.DMY to "DD MMM YYYY",
            DateFormatOption.MDY to "MMM DD, YYYY",
            DateFormatOption.YMD to "YYYY-MM-DD",
            DateFormatOption.HIJRI_FIRST to loc(language, "Hijri First", "ہجری پہلے", "الهجري أولاً", "हिजरी पहले")
        )
        options.forEach { (opt, label) ->
            FilterChip(
                selected = settings.dateFormat == opt,
                onClick = { prefsRepo.setDateFormat(opt) },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("date_format_${opt.id}")
            )
        }
    }

    // Number Format
    SettingsFieldLabel(loc(language, "Numerals Format", "اعداد کا انداز", "تنسيق الأرقام", "अंक प्रारूप"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = settings.numberFormat == NumberFormatOption.WESTERN,
            onClick = { prefsRepo.setNumberFormat(NumberFormatOption.WESTERN) },
            label = { Text(loc(language, "123 (Western)", "123 (انگریزی اعداد)", "123 (أرقام غربية)", "123 (अंग्रेज़ी अंक)")) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.testTag("number_format_western")
        )
        FilterChip(
            selected = settings.numberFormat == NumberFormatOption.LOCALE_INDIC,
            onClick = { prefsRepo.setNumberFormat(NumberFormatOption.LOCALE_INDIC) },
            label = { Text(loc(language, "١٢٣ (Arabic/Indic)", "۱۲۳ (مقامی اعداد)", "١٢٣ (أرقام عربية)", "१२३ (स्थानीय अंक)")) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.testTag("number_format_indic")
        )
    }

    SettingsSwitchRow(
        title = loc(language, "Show Timezone Label", "ٹائم زون لیبل دکھائیں", "إظهار المنطقة الزمنية", "समयक्षेत्र लेबल दिखाएँ"),
        subtitle = loc(language, "Display active city timezone on Prayer & Calendar screens", "نماز اور تقویم پر ٹائم زون دکھائیں", "عرض اسم المنطقة الزمنية في المواقيت", "नमाज़ और कैलेंडर में समयक्षेत्र दिखाएँ"),
        checked = settings.showTimezoneLabel,
        onCheckedChange = { prefsRepo.setShowTimezoneLabel(it) },
        switchTestTag = "timezone_label_switch"
    )

    if (showCountryDialog) {
        var countrySearch by remember { mutableStateOf("") }
        val filtered = remember(countrySearch) {
            ZakatCurrencyCatalog.searchCountries(countrySearch)
        }
        AlertDialog(
            onDismissRequest = { showCountryDialog = false },
            title = { Text(loc(language, "Select Country & Currency", "ملک اور کرنسی منتخب کریں", "اختر البلد والعملة", "देश और मुद्रा चुनें")) },
            text = {
                Column(modifier = Modifier.heightIn(max = 380.dp)) {
                    OutlinedTextField(
                        value = countrySearch,
                        onValueChange = { countrySearch = it },
                        singleLine = true,
                        label = { Text(loc(language, "Search country or currency", "تلاش کریں", "ابحث", "खोजें")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.lazy.LazyColumn {
                        items(filtered.size) { idx ->
                            val c = filtered[idx]
                            ListItem(
                                headlineContent = { Text("${c.flagEmoji} ${c.displayName}") },
                                supportingContent = { Text(c.primaryCurrencyCode) },
                                modifier = Modifier.clickable {
                                    ZakatCurrencyCatalog.saveSelectedCountry(context, c.isoCode)
                                    ZakatCurrencyCatalog.saveSelectedCurrency(context, c.primaryCurrencyCode)
                                    savedCountryIso = c.isoCode
                                    savedCurrencyCode = c.primaryCurrencyCode
                                    showCountryDialog = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCountryDialog = false }) {
                    Text(loc(language, "Close", "بند کریں", "إغلاق", "बंद करें"))
                }
            }
        )
    }
}

@Composable
internal fun AppearanceSectionContent(
    language: AppLanguage,
    currentTheme: AppTheme,
    settings: AppSettingsState,
    prefsRepo: UserPreferencesRepository,
    onThemeSelected: (AppTheme) -> Unit
) {
    SettingsFieldLabel(AppLocalization.get("theme_selector", language))

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AppTheme.entries.forEach { themeItem ->
            val isSelected = currentTheme == themeItem
            val bgColor = Color(themeItem.swatchBackgroundHex)
            val primColor = Color(themeItem.swatchPrimaryHex)
            val secColor = Color(themeItem.swatchSecondaryHex)
            val accColor = Color(themeItem.swatchAccentHex)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = { onThemeSelected(themeItem) }
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("theme_option_${themeItem.id}")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    // Multi-dot mini palette preview
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-6).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(bgColor, primColor, secColor, accColor).forEach { swatch ->
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(swatch)
                                    .border(1.5.dp, Color.White.copy(alpha = 0.9f), CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = themeItem.localizedTitle(language),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                            ),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (themeItem.isDarkDefault) {
                                loc(language, "Dark Palette", "تاریک تھیم", "مظهر داكن", "डार्क पैलेट")
                            } else {
                                loc(language, "Light Palette", "روشن تھیم", "مظهر نهاري", "लाइट पैलेट")
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected theme",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    SettingsFieldLabel(AppLocalization.get("settings_appearance_mode", language))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            AppearanceMode.SYSTEM to AppLocalization.get("settings_mode_system", language),
            AppearanceMode.LIGHT to AppLocalization.get("settings_mode_light", language),
            AppearanceMode.DARK to AppLocalization.get("settings_mode_dark", language)
        ).forEach { (mode, label) ->
            FilterChip(
                selected = settings.appearanceMode == mode,
                onClick = { prefsRepo.setAppearanceMode(mode) },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("appearance_mode_${mode.id}")
            )
        }
    }

    // Custom Accent Presets with live contrast validation
    SettingsFieldLabel(loc(language, "Custom Accent Color", "من پسند نمایاں رنگ", "لون التمييز المخصص", "कस्टम एक्सेंट रंग"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AppAccentPresets.forEach { preset ->
            val selected = (settings.customAccentHex ?: 0L) == preset.colorHex
            val swatch = if (preset.colorHex == 0L) Color(currentTheme.swatchPrimaryHex) else Color(preset.colorHex)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .selectable(
                        selected = selected,
                        role = Role.RadioButton,
                        onClick = { prefsRepo.setCustomAccentHex(if (preset.colorHex == 0L) null else preset.colorHex) }
                    )
                    .padding(4.dp)
                    .testTag("accent_preset_${preset.id}")
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(swatch)
                        .border(
                            width = if (selected) 3.dp else 1.dp,
                            color = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        )
                ) {
                    if (selected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = preset.localizedLabel(language),
                            tint = ThemeContrastUtils.bestOnColor(swatch),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = preset.localizedLabel(language),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1
                )
            }
        }
    }

    val primaryContrast = ThemeContrastUtils.contrastRatio(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.surface
    )
    Text(
        text = loc(
            language,
            "Active contrast ratio: ${String.format("%.1f", primaryContrast)}:1 (WCAG readability checked)",
            "موجودہ کنٹراسٹ تناسب: ${String.format("%.1f", primaryContrast)}:1",
            "نسبة التباين الحالية: ${String.format("%.1f", primaryContrast)}:1",
            "सक्रिय कंट्रास्ट अनुपात: ${String.format("%.1f", primaryContrast)}:1"
        ),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp)
    )

    // Interface Text Scale
    SettingsFieldLabel(
        loc(
            language,
            "Interface Text Scale (${(settings.uiTextScale * 100).roundToInt()}%)",
            "اسکرین متن کا سائز (${(settings.uiTextScale * 100).roundToInt()}%)",
            "مقياس نص الواجهة (${(settings.uiTextScale * 100).roundToInt()}%)",
            "इंटरफ़ेस टेक्स्ट आकार (${(settings.uiTextScale * 100).roundToInt()}%)"
        )
    )
    Slider(
        value = settings.uiTextScale,
        onValueChange = { prefsRepo.setUiTextScale(it) },
        valueRange = 0.85f..1.35f,
        steps = 4,
        modifier = Modifier.testTag("ui_text_scale_slider")
    )

    // Card Style & UI Font Style
    SettingsFieldLabel(loc(language, "Card Surface Style", "کارڈ کا انداز", "نمط البطاقات", "कार्ड शैली"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            CardAppearanceStyle.ELEVATED to loc(language, "Elevated", "سایہ دار", "بارز", "उभरा हुआ"),
            CardAppearanceStyle.OUTLINED to loc(language, "Outlined", "بارڈر والا", "محدد", "बॉर्डर"),
            CardAppearanceStyle.FLAT to loc(language, "Flat", "سادہ", "مسطح", "सपाट")
        ).forEach { (style, label) ->
            FilterChip(
                selected = settings.cardStyle == style,
                onClick = { prefsRepo.setCardStyle(style) },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("card_style_${style.id}")
            )
        }
    }

    SettingsFieldLabel(loc(language, "Interface Font Style", "فونٹ اسٹائل", "خط الواجهة", "फ़ॉन्ट शैली"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            UiFontStyleOption.DEFAULT to loc(language, "System Default", "سسٹم", "النظام", "सिस्टम"),
            UiFontStyleOption.SERIF to loc(language, "Classical Serif", "کلاسیکی سیرف", "كلاسيكي", "क्लासिक सेरिफ़"),
            UiFontStyleOption.SANS_CLEAN to loc(language, "Modern Sans", "ماڈرن سانس", "عصري", "आधुनिक सैंस")
        ).forEach { (style, label) ->
            FilterChip(
                selected = settings.uiFontStyle == style,
                onClick = { prefsRepo.setUiFontStyle(style) },
                label = { Text(label) },
                colors = noorSelectableChipColors()
            )
        }
    }

    SettingsSwitchRow(
        title = loc(language, "Show Quick Prayer Card on Home", "ہوم اسکرین پر نماز کارڈ دکھائیں", "إظهار بطاقة الصلاة بالرئيسية", "होम पर नमाज़ कार्ड दिखाएँ"),
        checked = settings.showHomePrayerCard,
        onCheckedChange = { prefsRepo.setShowHomePrayerCard(it) },
        switchTestTag = "home_prayer_card_switch"
    )

    OutlinedButton(
        onClick = { prefsRepo.resetThemeToDefault() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .height(48.dp)
            .testTag("reset_theme_btn")
    ) {
        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(AppLocalization.get("settings_reset_theme", language))
    }
}

@Composable
internal fun QuranReadingSectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    prefsRepo: UserPreferencesRepository,
    onNavigateToBookmarks: () -> Unit
) {
    // Live Arabic Preview
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                fontFamily = resolveArabicFontFamily(settings.arabicFontOption),
                fontSize = settings.arabicFontSize.sp,
                lineHeight = (settings.arabicFontSize * 1.75f).sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(textDirection = TextDirection.Rtl),
                modifier = Modifier.fillMaxWidth()
            )
            if (settings.showInlineTranslation) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = when (language) {
                        AppLanguage.URDU -> "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے"
                        AppLanguage.ARABIC -> "سورة الفاتحة — الآية ١"
                        AppLanguage.HINDI -> "अल्लाह के नाम से जो अत्यन्त कृपाशील और दयावान है"
                        AppLanguage.ENGLISH -> "In the name of Allah, the Entirely Merciful, the Especially Merciful."
                    },
                    fontSize = settings.translationFontSize.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    SettingsFieldLabel(loc(language, "Arabic Script Font", "عربی خط (فونٹ)", "الخط العربي", "अरबी फ़ॉन्ट"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            ArabicFontOption.AMIRI to "Amiri (Naskh)",
            ArabicFontOption.NOTO_NASKH to "Noto Naskh Arabic",
            ArabicFontOption.SYSTEM to loc(language, "System", "سسٹم", "النظام", "सिस्टम")
        ).forEach { (opt, label) ->
            FilterChip(
                selected = settings.arabicFontOption == opt,
                onClick = { prefsRepo.setArabicFontOption(opt) },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("arabic_font_${opt.id}")
            )
        }
    }

    SettingsFieldLabel("${AppLocalization.get("arabic_font_size", language)}: ${settings.arabicFontSize.roundToInt()} sp")
    Slider(
        value = settings.arabicFontSize,
        onValueChange = { prefsRepo.setArabicFontSize(it) },
        valueRange = 18f..36f,
        steps = 8,
        modifier = Modifier.testTag("arabic_font_size_slider")
    )

    SettingsFieldLabel(
        loc(
            language,
            "Translation Text Size: ${settings.translationFontSize.roundToInt()} sp",
            "ترجمہ کا سائز: ${settings.translationFontSize.roundToInt()} sp",
            "حجم خط الترجمة: ${settings.translationFontSize.roundToInt()} sp",
            "अनुवाद टेक्स्ट आकार: ${settings.translationFontSize.roundToInt()} sp"
        )
    )
    Slider(
        value = settings.translationFontSize,
        onValueChange = { prefsRepo.setTranslationFontSize(it) },
        valueRange = 12f..22f,
        steps = 4,
        modifier = Modifier.testTag("translation_font_size_slider")
    )

    SettingsFieldLabel(loc(language, "Ayah Card Spacing", "آیات کا درمیانی فاصلہ", "تباعد الآيات", "आयत अंतराल"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            AyahLayoutMode.COMPACT to loc(language, "Compact", "تنگ", "مضغوط", "सघन"),
            AyahLayoutMode.COMFORTABLE to loc(language, "Comfortable", "متوازن", "مريح", "संतुलित"),
            AyahLayoutMode.SPACIOUS to loc(language, "Spacious", "کشادہ", "واسع", "खुला")
        ).forEach { (mode, label) ->
            FilterChip(
                selected = settings.ayahLayoutMode == mode,
                onClick = { prefsRepo.setAyahLayoutMode(mode) },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("ayah_layout_${mode.id}")
            )
        }
    }

    SettingsSwitchRow(
        title = loc(language, "Show Inline Translation in Reader", "تلاوت کے ساتھ ترجمہ دکھائیں", "إظهار الترجمة أسفل الآية", "पढ़ते समय अनुवाद दिखाएँ"),
        checked = settings.showInlineTranslation,
        onCheckedChange = { prefsRepo.setShowInlineTranslation(it) },
        switchTestTag = "inline_translation_switch"
    )

    SettingsActionRow(
        title = AppLocalization.get("bookmarks", language),
        subtitle = loc(language, "View saved Quran, Hadith & Dua bookmarks", "محفوظ شدہ آیات، احادیث اور دعائیں دیکھیں", "عرض المفضلات المحفوظة", "सहेजे गए बुकमार्क देखें"),
        icon = Icons.Default.Bookmark,
        onClick = onNavigateToBookmarks,
        testTag = "settings_bookmarks_shortcut"
    )
}

@Composable
internal fun TafseerTranslationSectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    prefsRepo: UserPreferencesRepository,
    tafseerSources: List<TafseerSourceEntity>,
    translationCatalog: List<TranslationCatalogEntity>,
    selectedTranslatorIds: Map<String, String>,
    catalogRepo: QuranTranslationTafseerRepository
) {
    val langSources = tafseerSources.filter {
        it.onlineAllowed && it.originalLanguage.equals(language.code, ignoreCase = true)
    }.ifEmpty {
        VerifiedTafseerCatalog.toTafseerSourceEntities().filter {
            it.originalLanguage.equals(language.code, ignoreCase = true)
        }
    }
    SettingsFieldLabel(loc(language, "Default Tafseer Scholar (Online)", "بنیادی مفسر / آن لائن تفسیر", "المفسر الافتراضي (عبر الإنترنت)", "डिफ़ॉल्ट ऑनलाइन तफ़सीर विद्वान"))
    if (langSources.isEmpty()) {
        Text(
            text = VerifiedTafseerCatalog.onlineFetchErrorMessage(language, "Tafseer"),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    } else {
        langSources.forEach { source ->
            val selected = settings.defaultTafseerScholarId == source.id
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .selectable(
                        selected = selected,
                        role = Role.RadioButton,
                        onClick = { prefsRepo.setDefaultTafseerScholarId(source.id) }
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("default_scholar_${source.id}")
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = source.bookTitle,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${source.scholarName} • ${source.edition}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (selected) {
                    Icon(Icons.Default.Check, contentDescription = "Selected scholar", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }

    val activeTransForLang = translationCatalog.filter { it.languageCode == language.code }
    if (activeTransForLang.isNotEmpty()) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        SettingsFieldLabel(
            loc(
                language,
                "Default Translation (${language.displayName})",
                "بنیادی ترجمۂ قرآن (${language.nativeName})",
                "الترجمة الافتراضية (${language.nativeName})",
                "डिफ़ॉल्ट अनुवाद (${language.nativeName})"
            )
        )
        val activeId = selectedTranslatorIds[language.code]
        activeTransForLang.forEach { item ->
            val selected = item.translationId == activeId
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .selectable(
                        selected = selected,
                        role = Role.RadioButton,
                        onClick = { catalogRepo.selectTranslator(language, item.translationId) }
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.translator,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${item.editionVersion} • ${item.sourceProvider}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (selected) {
                    Icon(Icons.Default.Check, contentDescription = "Selected translation", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
internal fun AudioPlaybackSectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    prefsRepo: UserPreferencesRepository
) {
    SettingsFieldLabel(loc(language, "Default Quran Reciter", "بنیادی قاری منتخب کریں", "القارئ الافتراضي", "डिफ़ॉल्ट क़ारी चुनें"))
    QuranData.reciters.forEach { reciter ->
        val selected = settings.selectedReciterId == reciter.id
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                .selectable(
                    selected = selected,
                    role = Role.RadioButton,
                    onClick = { prefsRepo.setReciter(reciter.id) }
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("reciter_option_${reciter.id}")
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.URDU || language == AppLanguage.ARABIC) "${reciter.nameArabic} — ${reciter.nameEnglish}" else reciter.nameEnglish,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = reciter.style,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (selected) {
                Icon(Icons.Default.Check, contentDescription = "Selected reciter", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }

    SettingsFieldLabel(loc(language, "Default Playback Speed", "تلاوت و پوڈکاسٹ کی رفتار", "سرعة التشغيل الافتراضية", "डिफ़ॉल्ट प्लेबैक गति"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
            FilterChip(
                selected = kotlin.math.abs(settings.audioPlaybackSpeed - speed) < 0.05f,
                onClick = { prefsRepo.setAudioPlaybackSpeed(speed) },
                label = { Text("${speed}x") },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("audio_speed_${speed}")
            )
        }
    }

    SettingsFieldLabel(loc(language, "Default Ayah Repeat Count", "آیت کی تکرار", "تكرار الآية", "आयत दोहराव संख्या"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(1, 2, 3, 5, 7, 10).forEach { count ->
            FilterChip(
                selected = settings.ayahRepeatCount == count,
                onClick = { prefsRepo.setAyahRepeatCount(count) },
                label = { Text("${count}x") },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("ayah_repeat_${count}")
            )
        }
    }
}
