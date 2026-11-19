@file:OptIn(ExperimentalLayoutApi::class)
package com.example.ui.screens.settings

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.BuildConfig
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.model.AppSettingsState
import com.example.data.model.DailyGoalLevel
import com.example.data.notification.DailyReminderScheduler
import com.example.data.notification.PrayerNotificationManager
import com.example.data.notification.PrayerNotificationSettings
import com.example.data.repository.HijriCalendarConvention
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.QuranLearningRepository
import com.example.data.repository.StorageBreakdown
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.ui.components.noorSelectableChipColors

@Composable
internal fun PrayerLocationSectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    context: Context,
    prefsRepo: UserPreferencesRepository,
    onNavigateToPrayerTimes: () -> Unit,
    onNavigateToQibla: () -> Unit
) {
    val location by AppLocationManager.currentLocation.collectAsStateWithLifecycle()
    val calcConfig by PrayerCalculator.calcConfig.collectAsStateWithLifecycle()
    val hijriConvention by IslamicCalendarRepository.calendarConvention.collectAsStateWithLifecycle()
    val manualOffset by IslamicCalendarRepository.manualAdjustmentDays.collectAsStateWithLifecycle()
    var showMethodDialog by remember { mutableStateOf(false) }

    val cityDisplay = remember(location, language) {
        AppLocationManager.localizedCityDisplay(location, language)
    }
    val locationSubtitle = if (location.countryName.isNotBlank()) {
        "$cityDisplay, ${location.countryName} (${location.timeZoneId})"
    } else {
        "$cityDisplay (${location.timeZoneId})"
    }
    SettingsActionRow(
        title = loc(language, "Active Location", "موجودہ مقام", "الموقع الحالي", "सक्रिय स्थान"),
        subtitle = locationSubtitle,
        icon = Icons.Default.LocationOn,
        onClick = onNavigateToPrayerTimes,
        testTag = "settings_location_row"
    )

    SettingsActionRow(
        title = loc(language, "Calculation Method", "طریقۂ حسابِ نماز", "طريقة حساب المواقيت", "नमाज़ गणना पद्धति"),
        subtitle = calcConfig.method.displayName,
        icon = Icons.Default.Schedule,
        onClick = { showMethodDialog = true },
        testTag = "settings_calc_method_btn"
    )

    SettingsFieldLabel(loc(language, "Asr Juristic Method (Madhhab)", "فقہی مسلک (وقتِ عصر)", "مذهب وقت العصر", "असर फ़िक़ही मत"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PrayerCalculator.AsrJuristic.entries.forEach { asr ->
            FilterChip(
                selected = calcConfig.asrJuristic == asr,
                onClick = {
                    PrayerCalculator.updateConfig(context, calcConfig.copy(asrJuristic = asr))
                    PrayerNotificationManager.rescheduleAllAlarms(context, reason = "settings_asr_changed")
                },
                label = {
                    Text(
                        if (asr == PrayerCalculator.AsrJuristic.HANAFI) {
                            loc(language, "Hanafi (2x)", "حنفی (دو مثل)", "حنفي", "हनफ़ी (2x)")
                        } else {
                            loc(language, "Standard (Shafi/Maliki/Hanbali 1x)", "جمہور / شافعی (ایک مثل)", "الجمهور / شافعي", "मानक / शाफ़ई (1x)")
                        }
                    )
                },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("asr_method_${asr.name.lowercase()}")
            )
        }
    }

    SettingsFieldLabel(loc(language, "Hijri Calendar Convention", "ہجری تقویم کا معیار", "معيار التقويم الهجري", "हिजरी कैलेंडर मानक"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        val conventions = listOf(
            HijriCalendarConvention.PAKISTAN_RUET to loc(language, "Pakistan Ruet", "پاکستان رویتِ ہلال", "باكستان (رؤية)", "पाकिस्तान रुअत"),
            HijriCalendarConvention.UMM_AL_QURA to loc(language, "Umm Al-Qura", "ام القریٰ (سعودیہ)", "أم القرى", "उम्म अल-क़ुरा"),
            HijriCalendarConvention.ASTRONOMICAL_TABULAR to loc(language, "Astronomical", "فلکیاتی حساب", "فلكي / جدولي", "खगोलीय"),
            HijriCalendarConvention.MANUAL_LOCAL_SIGHTING to loc(language, "Local Manual", "دستی رویت", "رؤية محلية", "स्थानीय मैन्युअल")
        )
        conventions.forEach { (conv, label) ->
            FilterChip(
                selected = hijriConvention == conv,
                onClick = { IslamicCalendarRepository.setCalendarConvention(context, conv) },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("hijri_conv_${conv.id}")
            )
        }
    }

    SettingsFieldLabel(
        loc(
            language,
            "Manual Moon-Sighting Offset ($manualOffset days)",
            "چاند کی تاریخ میں دستی ردوبدل ($manualOffset دن)",
            "تعديل رؤية الهلال يدوياً ($manualOffset يوم)",
            "चाँद तिथि समायोजन ($manualOffset दिन)"
        )
    )
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(-2, -1, 0, 1, 2).forEach { offset ->
            FilterChip(
                selected = manualOffset == offset,
                onClick = { IslamicCalendarRepository.setManualAdjustment(context, offset) },
                label = { Text(if (offset > 0) "+$offset" else "$offset") },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("hijri_offset_$offset")
            )
        }
    }

    SettingsSwitchRow(
        title = loc(language, "Qibla True North Bearing", "قبلہ کمپاس: حقیقی شمال (True North)", "استخدام الشمال الحقيقي للقبلة", "क़िबला कम्पास: ट्रू नॉर्थ"),
        subtitle = loc(language, "Adjust magnetic compass using astronomical declination", "مقناطیسی انحراف کے مطابق درست سمت", "تصحيح الانحراف المغناطيسي تلقائياً", "चुंबकीय विचलन के लिए स्वतः सुधार"),
        checked = settings.qiblaUseTrueNorth,
        onCheckedChange = { prefsRepo.setQiblaUseTrueNorth(it) },
        switchTestTag = "qibla_true_north_switch"
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
        OutlinedButton(
            onClick = onNavigateToPrayerTimes,
            modifier = Modifier.weight(1f).height(48.dp).testTag("settings_open_prayer_btn")
        ) {
            Text(AppLocalization.get("prayer_times", language), maxLines = 1)
        }
        OutlinedButton(
            onClick = onNavigateToQibla,
            modifier = Modifier.weight(1f).height(48.dp).testTag("settings_open_qibla_btn")
        ) {
            Text(AppLocalization.get("qibla_finder", language), maxLines = 1)
        }
    }

    if (showMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMethodDialog = false },
            title = { Text(loc(language, "Prayer Calculation Method", "طریقۂ حسابِ نماز", "طريقة حساب المواقيت", "नमाज़ गणना पद्धति")) },
            text = {
                Column(modifier = Modifier.heightIn(max = 360.dp)) {
                    androidx.compose.foundation.lazy.LazyColumn {
                        items(PrayerCalculator.CalculationMethod.entries.size) { idx ->
                            val m = PrayerCalculator.CalculationMethod.entries[idx]
                            val selected = calcConfig.method == m
                            ListItem(
                                headlineContent = { Text(m.displayName, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
                                trailingContent = {
                                    if (selected) Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                modifier = Modifier.clickable {
                                    PrayerCalculator.updateConfig(
                                        context,
                                        calcConfig.copy(method = m, isAutoMethodForLocation = false)
                                    )
                                    PrayerNotificationManager.rescheduleAllAlarms(context, reason = "settings_method_changed")
                                    showMethodDialog = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMethodDialog = false }) {
                    Text(loc(language, "Close", "بند کریں", "إغلاق", "बंद करें"))
                }
            }
        )
    }
}

@Composable
internal fun NotificationsRemindersSectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    context: Context,
    prefsRepo: UserPreferencesRepository
) {
    val notifSettings by PrayerNotificationManager.settings.collectAsStateWithLifecycle()

    SettingsSwitchRow(
        title = loc(language, "Master Notifications", "تمام اطلاعات و یاد دہانیاں", "تفعيل جميع التنبيهات", "मुख्य सूचनाएं सक्षम करें"),
        subtitle = loc(language, "Enable or pause all scheduled prayer alerts and daily reminders", "نماز کے اعلانات اور روزانہ یاد دہانیاں آن یا بند کریں", "تشغيل أو إيقاف جميع تنبيهات الصلاة والتذكيرات", "सभी नमाज़ अलर्ट और दैनिक रिमाइंडर चालू/बंद करें"),
        checked = settings.notificationsEnabled,
        onCheckedChange = { prefsRepo.setNotificationsEnabled(it) },
        switchTestTag = "notif_master_switch"
    )

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    SettingsFieldLabel(loc(language, "Prayer Alerts & Adhan Sound", "نماز کے اوقات پر اطلاع اور آواز", "تنبيهات الصلوات وصوت الأذان", "नमाज़ अलर्ट और अज़ान ध्वनि"))

    val prayerRows = listOf(
        Triple("fajr", AppLocalization.get("fajr", language), notifSettings.fajrEnabled),
        Triple("dhuhr", AppLocalization.get("dhuhr", language), notifSettings.dhuhrEnabled),
        Triple("asr", AppLocalization.get("asr", language), notifSettings.asrEnabled),
        Triple("maghrib", AppLocalization.get("maghrib", language), notifSettings.maghribEnabled),
        Triple("isha", AppLocalization.get("isha", language), notifSettings.ishaEnabled)
    )
    prayerRows.forEach { (key, label, isEnabled) ->
        SettingsSwitchRow(
            title = label,
            checked = isEnabled,
            enabled = settings.notificationsEnabled,
            onCheckedChange = { checked ->
                val updated: PrayerNotificationSettings = when (key) {
                    "fajr" -> notifSettings.copy(fajrEnabled = checked)
                    "dhuhr" -> notifSettings.copy(dhuhrEnabled = checked)
                    "asr" -> notifSettings.copy(asrEnabled = checked)
                    "maghrib" -> notifSettings.copy(maghribEnabled = checked)
                    else -> notifSettings.copy(ishaEnabled = checked)
                }
                PrayerNotificationManager.updateSettings(context, updated)
            },
            switchTestTag = "prayer_alert_${key}_switch"
        )
    }

    SettingsFieldLabel(loc(language, "Adhan Voice", "آوازِ اذان", "صوت الأذان", "अज़ान आवाज़"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            "makkah" to AppLocalization.get("azan_makkah", language),
            "madina" to AppLocalization.get("azan_madinah", language),
            "fajr" to AppLocalization.get("azan_fajr", language),
            "silent" to AppLocalization.get("azan_silent", language)
        ).forEach { (soundId, label) ->
            FilterChip(
                selected = notifSettings.adhanSound == soundId,
                onClick = {
                    PrayerNotificationManager.updateSettings(context, notifSettings.copy(adhanSound = soundId))
                },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("adhan_sound_$soundId")
            )
        }
    }

    SettingsSwitchRow(
        title = loc(language, "Alert Vibration", "اطلاع پر وائبریشن", "الاهتزاز عند التنبيه", "अलर्ट पर वाइब्रेशन"),
        checked = notifSettings.vibrationEnabled,
        enabled = settings.notificationsEnabled,
        onCheckedChange = {
            PrayerNotificationManager.updateSettings(context, notifSettings.copy(vibrationEnabled = it))
        },
        switchTestTag = "notif_vibration_switch"
    )

    SettingsFieldLabel(loc(language, "Reminder Lead Time", "نماز سے قبل اطلاع", "التنبيه قبل الصلاة", "नमाज़ से पहले अलर्ट"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(0, 5, 10, 15).forEach { mins ->
            FilterChip(
                selected = notifSettings.leadTimeMinutes == mins,
                onClick = {
                    PrayerNotificationManager.updateSettings(context, notifSettings.copy(leadTimeMinutes = mins))
                },
                label = {
                    Text(
                        if (mins == 0) loc(language, "At Time", "عین وقت پر", "عند الوقت", "सही समय")
                        else "-${mins}m"
                    )
                },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("notif_lead_${mins}m")
            )
        }
    }

    OutlinedButton(
        onClick = { PrayerNotificationManager.sendTestNotification(context) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(48.dp).testTag("test_prayer_alert_btn")
    ) {
        Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(loc(language, "Send Test Prayer Alert", "آزمائشی اطلاع بھیجیں", "إرسال تنبيه تجريبي", "परीक्षण अलर्ट भेजें"))
    }

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    SettingsFieldLabel(loc(language, "Daily Islamic Reminders", "روزانہ اسلامی یاد دہانیاں", "التذكيرات الإسلامية اليومية", "दैनिक इस्लामी रिमाइंडर"))

    SettingsSwitchRow(
        title = loc(language, "Daily Quran Reminder (${settings.dailyQuranReminderHour}:00)", "روزانہ تلاوتِ قرآن یاد دہانی", "تذكير ورد القرآن اليومي", "दैनिक क़ुरआन रिमाइंडर"),
        checked = settings.dailyQuranReminderEnabled,
        enabled = settings.notificationsEnabled,
        onCheckedChange = { prefsRepo.setDailyQuranReminder(it, settings.dailyQuranReminderHour, 0) },
        switchTestTag = "daily_quran_reminder_switch"
    )
    if (settings.dailyQuranReminderEnabled) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(bottom = 6.dp)) {
            listOf(6, 7, 9, 13, 20).forEach { hr ->
                FilterChip(
                    selected = settings.dailyQuranReminderHour == hr,
                    onClick = { prefsRepo.setDailyQuranReminder(true, hr, 0) },
                    label = { Text(String.format("%02d:00", hr)) },
                    colors = noorSelectableChipColors()
                )
            }
        }
    }

    SettingsSwitchRow(
        title = loc(language, "Daily Hadith Reminder (10:00)", "روزانہ حدیثِ نبوی ﷺ یاد دہانی", "تذكير حديث اليوم", "दैनिक हदीस रिमाइंडर"),
        checked = settings.dailyHadithReminderEnabled,
        enabled = settings.notificationsEnabled,
        onCheckedChange = { prefsRepo.setDailyHadithReminder(it) },
        switchTestTag = "daily_hadith_reminder_switch"
    )
    SettingsSwitchRow(
        title = loc(language, "Daily Masnoon Dua Reminder (13:30)", "روزانہ مسنون دعا یاد دہانی", "تذكير الدعاء اليومي", "दैनिक दुआ रिमाइंडर"),
        checked = settings.dailyDuaReminderEnabled,
        enabled = settings.notificationsEnabled,
        onCheckedChange = { prefsRepo.setDailyDuaReminder(it) },
        switchTestTag = "daily_dua_reminder_switch"
    )
    SettingsSwitchRow(
        title = loc(language, "Morning Adhkar (06:00)", "صبح کے مسنون اذکار", "أذكار الصباح", "सुबह के अज़कार"),
        checked = settings.morningAdhkarReminderEnabled,
        enabled = settings.notificationsEnabled,
        onCheckedChange = { prefsRepo.setMorningAdhkarReminder(it) },
        switchTestTag = "morning_adhkar_switch"
    )
    SettingsSwitchRow(
        title = loc(language, "Evening Adhkar (17:30)", "شام کے مسنون اذکار", "أذكار المساء", "शाम के अज़कार"),
        checked = settings.eveningAdhkarReminderEnabled,
        enabled = settings.notificationsEnabled,
        onCheckedChange = { prefsRepo.setEveningAdhkarReminder(it) },
        switchTestTag = "evening_adhkar_switch"
    )

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    SettingsSwitchRow(
        title = loc(language, "Do Not Disturb (Quiet Hours)", "خاموشی کے اوقات (DND)", "ساعات الهدوء (عدم الإزعاج)", "डिस्टर्ब न करें (शांत घंटे)"),
        subtitle = loc(
            language,
            "Silence non-fard daily reminders between ${settings.dndStartHour}:00 and ${settings.dndEndHour}:00",
            "روزانہ یاد دہانیاں ${settings.dndStartHour}:00 سے ${settings.dndEndHour}:00 تک موقوف رہیں گی",
            "كتم التذكيرات اليومية من ${settings.dndStartHour}:00 إلى ${settings.dndEndHour}:00",
            "${settings.dndStartHour}:00 से ${settings.dndEndHour}:00 के बीच दैनिक रिमाइंडर मौन रहेंगे"
        ),
        checked = settings.dndEnabled,
        onCheckedChange = { prefsRepo.setDndQuietHours(it, settings.dndStartHour, settings.dndEndHour) },
        switchTestTag = "dnd_quiet_hours_switch"
    )
    if (settings.dndEnabled) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                Pair(22, 5) to "22:00 – 05:00",
                Pair(23, 6) to "23:00 – 06:00",
                Pair(21, 5) to "21:00 – 05:00"
            ).forEach { (window, label) ->
                FilterChip(
                    selected = settings.dndStartHour == window.first && settings.dndEndHour == window.second,
                    onClick = { prefsRepo.setDndQuietHours(true, window.first, window.second) },
                    label = { Text(label) },
                    colors = noorSelectableChipColors()
                )
            }
        }
    }
}

@Composable
internal fun LearningRoutineSectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    context: Context,
    prefsRepo: UserPreferencesRepository
) {
    var progress by remember { mutableStateOf(QuranLearningRepository.getProgress(context)) }

    SettingsSwitchRow(
        title = loc(language, "Daily Hifz & Academy Revision Reminder (20:00)", "حفظ اور اکیڈمی دہرائی کی یاد دہانی", "تذكير مراجعة الحفظ اليومي", "दैनिक हिफ़्ज़ दोहराव रिमाइंडर"),
        checked = progress.hifzRevisionReminderEnabled,
        onCheckedChange = { enabled ->
            QuranLearningRepository.setHifzRevisionReminderEnabled(context, enabled)
            progress = QuranLearningRepository.getProgress(context)
            DailyReminderScheduler.rescheduleAll(context)
        },
        switchTestTag = "hifz_revision_reminder_switch"
    )

    SettingsFieldLabel(loc(language, "Daily Study Goal", "روزانہ مطالعہ کا ہدف", "هدف التعلم اليومي", "दैनिक अध्ययन लक्ष्य"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            DailyGoalLevel.CASUAL_5M to loc(language, "5 min / 1 lesson", "5 منٹ / 1 سبق", "5 د / درس واحد", "5 मिनट / 1 पाठ"),
            DailyGoalLevel.STEADY_15M to loc(language, "15 min / 2 lessons", "15 منٹ / 2 اسباق", "15 د / درسان", "15 मिनट / 2 पाठ"),
            DailyGoalLevel.DEDICATED_30M to loc(language, "30 min / 3 lessons", "30 منٹ / 3 اسباق", "30 د / 3 دروس", "30 मिनट / 3 पाठ")
        ).forEach { (goal, label) ->
            FilterChip(
                selected = settings.dailyGoalLevel == goal,
                onClick = { prefsRepo.setDailyGoalLevel(goal) },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("daily_goal_${goal.id}")
            )
        }
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${progress.completedLessonIds.size}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(loc(language, "Lessons", "اسباق", "الدروس", "पाठ"), style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${progress.totalQuizzesPassed}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(loc(language, "Quizzes", "امتحانات", "اختبارات", "क्विज़"), style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${progress.currentStreakDays}d", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(loc(language, "Streak", "تسلسل", "التتابع", "स्ट्रिक"), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
internal fun InternetStorageSectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    context: Context,
    prefsRepo: UserPreferencesRepository,
    storageBreakdown: StorageBreakdown,
    onRefreshStorage: () -> Unit
) {
    SettingsSwitchRow(
        title = AppLocalization.get("settings_wifi_only", language),
        subtitle = AppLocalization.get("settings_wifi_only_desc", language),
        checked = settings.wifiOnlyDownloads,
        onCheckedChange = { prefsRepo.setWifiOnlyDownloads(it) },
        switchTestTag = "wifi_download_switch"
    )

    SettingsSwitchRow(
        title = loc(language, "Allow Mobile Data Streaming", "موبائل ڈیٹا پر اسٹریمنگ کی اجازت", "السماح بالبث عبر بيانات الهاتف", "मोबाइल डेटा स्ट्रीमिंग अनुमति"),
        subtitle = loc(language, "Stream Quran audio, Lectures & Podcasts when off Wi-Fi", "وائی فائی نہ ہونے پر آڈیو و بیانات سننے کی اجازت", "تشغيل الصوتيات والمحاضرات عبر شبكة الهاتف", "वाई-फ़ाई के बिना ऑडियो/पॉडकास्ट सुनें"),
        checked = settings.allowMobileDataStreaming,
        onCheckedChange = { prefsRepo.setAllowMobileDataStreaming(it) },
        switchTestTag = "mobile_streaming_switch"
    )

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    SettingsFieldLabel(loc(language, "Offline Storage Breakdown", "آف لائن اسٹوریج کی تفصیل", "تفاصيل التخزين على الجهاز", "ऑफ़लाइन स्टोरेज विवरण"))

    val rows = listOf(
        Triple(loc(language, "Temporary Cache (Safe to Clear)", "عارضی کیش", "ذاكرة مؤقتة", "अस्थायी कैश"), storageBreakdown.cacheBytes, "storage_cache_row"),
        Triple(loc(language, "Quran Offline Audio", "آف لائن تلاوتِ قرآن", "ملفات القرآن الصوتية", "क़ुरआन ऑफ़लाइन ऑडियो"), storageBreakdown.quranAudioBytes, "storage_quran_row"),
        Triple(loc(language, "Saved Podcasts", "محفوظ شدہ پوڈکاسٹس", "البودكاست المحفوظ", "सहेजे गए पॉडकास्ट"), storageBreakdown.podcastBytes, "storage_podcast_row"),
        Triple(loc(language, "Offline Hadith Packs", "آف لائن احادیث ذخیرہ", "حزم الحديث المحفوظة", "ऑफ़लाइन हदीस पैक"), storageBreakdown.hadithBytes, "storage_hadith_row"),
        Triple(loc(language, "App Database", "ایپ ڈیٹا بیس", "قاعدة البيانات", "ऐप डेटाबेस"), storageBreakdown.databaseBytes, "storage_db_row")
    )
    rows.forEach { (label, bytes, tag) ->
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag(tag)
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(formatBytesReadable(bytes), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        onClick = {
            val freed = prefsRepo.clearAppCacheSafe()
            onRefreshStorage()
            Toast.makeText(
                context,
                loc(
                    language,
                    "Cleared ${formatBytesReadable(freed)} temporary cache. Offline Quran, Hadith, Bookmarks & Progress preserved.",
                    "${formatBytesReadable(freed)} عارضی کیش صاف ہو گیا۔ محفوظ ڈیٹا برقرار ہے۔",
                    "تم مسح ${formatBytesReadable(freed)} من التخزين المؤقت مع حفظ كافة تنزيلاتك.",
                    "${formatBytesReadable(freed)} अस्थायी कैश साफ़ हुआ। सभी सहेजा डेटा सुरक्षित है।"
                ),
                Toast.LENGTH_SHORT
            ).show()
        },
        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("clear_cache_btn")
    ) {
        Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(AppLocalization.get("settings_clear_cache", language))
    }
}

@Composable
internal fun PrivacySecuritySectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    context: Context,
    onOpenPinDialog: () -> Unit,
    onOpenClearHistoryDialog: () -> Unit,
    onOpenResetPrefsDialog: () -> Unit,
    onOpenPrivacyDialog: () -> Unit
) {
    val hasLocationPerm = remember(context) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    val hasNotifPerm = remember(context) { PrayerNotificationManager.hasNotificationPermission(context) }
    val canExactAlarm = remember(context) { PrayerNotificationManager.canScheduleExactAlarms(context) }
    val ignoringBattery = remember(context) { PrayerNotificationManager.isIgnoringBatteryOptimizations(context) }

    SettingsFieldLabel(loc(language, "Device Permissions Status", "ڈیوائس اجازتوں کی صورتحال", "حالة أذونات الجهاز", "डिवाइस अनुमति स्थिति"))
    val permList = listOf(
        Triple(loc(language, "Location Access", "مقام تک رسائی", "إذن الموقع", "स्थान अनुमति"), hasLocationPerm) {
            runCatching {
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            }
            Unit
        },
        Triple(loc(language, "Notifications", "اطلاعات (Notifications)", "الإشعارات", "सूचनाएं"), hasNotifPerm) {
            runCatching {
                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                } else {
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
                context.startActivity(intent)
            }
            Unit
        },
        Triple(loc(language, "Exact Alarms (Timely Adhan)", "عین وقت پر اذان کا الارم", "المنبهات الدقيقة للأذان", "सटीक अलार्म (अज़ान)"), canExactAlarm) {
            PrayerNotificationManager.openExactAlarmSettings(context)
        },
        Triple(loc(language, "Battery Optimization Exempt", "بیٹری بچت سے استثنیٰ", "استثناء تحسين البطارية", "बैटरी अनुकूलन छूट"), ignoringBattery) {
            PrayerNotificationManager.openBatteryOptimizationSettings(context)
        }
    )

    permList.forEach { (title, granted, openAction) ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .clip(RoundedCornerShape(10.dp))
                .clickable { openAction() }
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(title, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (granted) Color(0xFF1B5E20).copy(alpha = 0.14f) else MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = if (granted) loc(language, "Granted", "اجازت ہے", "ممنوح", "स्वीकृत")
                    else loc(language, "Manage", "ترتیب دیں", "إدارة", "प्रबंधित करें"),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (granted) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

    SettingsActionRow(
        title = if (settings.isPinLockEnabled) {
            loc(language, "App Lock PIN: Active (4-digit)", "ایپ پن لاک: فعال", "قفل التطبيق برمز PIN: مفعّل", "ऐप लॉक पिन: सक्रिय")
        } else {
            loc(language, "Set App Lock PIN (4-digit)", "ایپ پن لاک سیٹ کریں", "تعيين رمز PIN للتطبيق", "ऐप लॉक पिन सेट करें")
        },
        subtitle = loc(
            language,
            "Local salted SHA-256 PIN protection on launch (Biometric hardware prompt not integrated)",
            "ایپ کھولنے پر مقامی پن حفاظت (بایومیٹرک شامل نہیں)",
            "حماية محلية برمز PIN عند الفتح (البصمة غير مفعّلة)",
            "ऐप खोलने पर स्थानीय पिन सुरक्षा (बायोमेट्रिक शामिल नहीं)"
        ),
        icon = Icons.Default.Lock,
        onClick = onOpenPinDialog,
        testTag = "app_pin_toggle_btn"
    )

    SettingsActionRow(
        title = loc(language, "Clear Recent Reading History", "حالیہ مطالعہ کا ریکارڈ صاف کریں", "مسح سجل القراءة الحديث", "हालिया पठन इतिहास साफ़ करें"),
        subtitle = loc(language, "Clears recent Dua/Hadith views without removing Bookmarks or Downloads", "صرف حالیہ تاریخ صاف ہوگی، بک مارکس محفوظ رہیں گے", "يمسح السجل الحديث فقط دون المفضلة", "केवल हालिया इतिहास साफ़ होगा, बुकमार्क सुरक्षित रहेंगे"),
        icon = Icons.Default.History,
        onClick = onOpenClearHistoryDialog,
        testTag = "clear_history_btn"
    )

    SettingsActionRow(
        title = AppLocalization.get("settings_reset_prefs", language),
        subtitle = loc(
            language,
            "Resets themes, fonts & reminder toggles only — preserves Downloads, Bookmarks & Progress",
            "صرف تھیم، فونٹ اور یاد دہانی کی ترتیبات بحال ہوں گی؛ ڈاؤنلوڈز اور بک مارکس محفوظ رہیں گے",
            "إعادة ضبط المظهر والتفضيلات فقط مع حفظ التنزيلات والمفضلة",
            "केवल थीम और प्राथमिकताएं रीसेट होंगी — डाउनलोड, बुकमार्क व प्रगति सुरक्षित रहेंगे"
        ),
        icon = Icons.Default.RestartAlt,
        tint = MaterialTheme.colorScheme.error,
        onClick = onOpenResetPrefsDialog,
        testTag = "reset_prefs_btn"
    )

    SettingsActionRow(
        title = loc(language, "Privacy Policy & Data Transparency", "رازداری کی پالیسی اور ڈیٹا شفافیت", "سياسة الخصوصية وشفافية البيانات", "गोपनीयता नीति और डेटा पारदर्शिता"),
        subtitle = loc(language, "Inspect what stays offline on device vs online content endpoints", "مکمل وضاحت دیکھیں کہ کون سا ڈیٹا صرف ڈیوائس پر رہتا ہے", "اطلع على ما يُخزّن محلياً وما يتصل بالإنترنت", "जानें कि डिवाइस पर क्या ऑफ़लाइन रहता है"),
        icon = Icons.Default.PrivacyTip,
        onClick = onOpenPrivacyDialog,
        testTag = "privacy_policy_btn"
    )
}

@Composable
internal fun AccessibilitySectionContent(
    language: AppLanguage,
    settings: AppSettingsState,
    prefsRepo: UserPreferencesRepository
) {
    SettingsFieldLabel(loc(language, "Quick UI Text Size Presets", "متن کے سائز کے فوری انتخاب", "مقاسات نص الواجهة السريعة", "त्वरित टेक्स्ट आकार"))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(1.0f to "100%", 1.15f to "115%", 1.3f to "130%").forEach { (scale, label) ->
            FilterChip(
                selected = kotlin.math.abs(settings.uiTextScale - scale) < 0.04f,
                onClick = { prefsRepo.setUiTextScale(scale) },
                label = { Text(label) },
                colors = noorSelectableChipColors(),
                modifier = Modifier.testTag("a11y_scale_${label.removeSuffix("%")}")
            )
        }
    }

    SettingsSwitchRow(
        title = loc(language, "High Contrast Mode", "زیادہ کنٹراسٹ موڈ", "وضع التباين العالي", "उच्च कंट्रास्ट मोड"),
        subtitle = loc(language, "Boost text & border contrast ratio for clearer legibility", "پڑھنے میں آسانی کے لیے رنگوں کا فرق نمایاں کریں", "زيادة تباين النصوص والحدود لوضوح أكبر", "बेहतर पठनीयता के लिए रंग कंट्रास्ट बढ़ाएं"),
        checked = settings.highContrastEnabled,
        onCheckedChange = { prefsRepo.setHighContrastEnabled(it) },
        switchTestTag = "high_contrast_switch"
    )

    SettingsSwitchRow(
        title = loc(language, "Reduced Motion", "حرکت اور اینیمیشن میں کمی", "تقليل الحركة والانتقالات", "कम एनीमेशन (Reduced Motion)"),
        subtitle = loc(language, "Minimize expandable and animated UI transitions", "اینیمیشنز کو محدود کر کے اسکرین مستحکم رکھیں", "تقليل التأثيرات الحركية داخل التطبيق", "एनीमेशन प्रभाव कम करें"),
        checked = settings.reducedMotionEnabled,
        onCheckedChange = { prefsRepo.setReducedMotionEnabled(it) },
        switchTestTag = "reduced_motion_switch"
    )
}

@Composable
internal fun AboutProvenanceSectionContent(
    language: AppLanguage,
    context: Context
) {
    val diagnostics = remember(context) {
        buildString {
            appendLine("App: Noor-e-Hidayat v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
            appendLine("Android API: ${Build.VERSION.SDK_INT} (${Build.MANUFACTURER} ${Build.MODEL})")
            appendLine("Language: ${UserPreferencesRepository(context).appSettings.value.language.code}")
            appendLine("Theme: ${UserPreferencesRepository(context).appSettings.value.theme.id}")
            appendLine("Prayer Method: ${PrayerCalculator.calcConfig.value.method.displayName}")
        }
    }

    Text(
        text = "${AppLocalization.get("app_name", language)} v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = when (language) {
            AppLanguage.URDU -> "• قرآن متن: مستند عثمانی رسم الخط (Tanzil.net) اور مستند تراجم\n" +
                "• تفسیر: ابنِ کثیر، جلالین، معارف القرآن اور قرآن فاؤنڈیشن\n" +
                "• حدیث: صحیح بخاری و صحیح مسلم کے منظم مقامی ایڈیشن\n" +
                "• اوقاتِ نماز: کراچی، رابطۂ عالم، ام القریٰ و دیگر مستند طریقے\n" +
                "• ہجری تقویم: رویتِ ہلال، ام القریٰ اور فلکیاتی معیارات\n" +
                "• براہِ راست نشریات: مکہ مکرمہ قرآن ٹی وی اور مدینہ منورہ سنہ ٹی وی"
            AppLanguage.ARABIC -> "• النص القرآني: مصحف تنزيل الموثق بالرسم العثماني والتراجم المعتمدة\n" +
                "• التفسير: ابن كثير والجلالين ومعارف القرآن ومصادر مؤسسة القرآن\n" +
                "• الحديث: صحيح البخاري وصحيح مسلم في طبعات محلية موثقة\n" +
                "• مواقيت الصلاة: رابطة العالم الإسلامي وأم القرى وكراتشي وغيرها\n" +
                "• التقويم الهجري: معيار أم القرى والرؤية والحساب الفلكي\n" +
                "• البث المباشر: قناة القرآن من مكة وقناة السنة من المدينة"
            AppLanguage.HINDI -> "• क़ुरआन पाठ: Tanzil.net प्रमाणित उस्मानी लिपि और प्रामाणिक अनुवाद\n" +
                "• तफ़सीर: इब्न कसीर, जलालैन, मआरिफ़ुल क़ुरआन व क़ुरआन फ़ाउंडेशन\n" +
                "• हदीस: सहीह बुख़ारी व सहीह मुस्लिम स्थानीय संस्करण\n" +
                "• नमाज़ के समय: कराची, MWL, इस्ना, उम्म अल-क़ुरा पद्धतियाँ\n" +
                "• हिजरी कैलेंडर: रुअत-ए-हिलाल, उम्म अल-क़ुरा व खगोलीय गणना\n" +
                "• लाइव प्रसारण: मक्का क़ुरआन टीवी और मदीना सुन्नाह टीवी"
            AppLanguage.ENGLISH -> "• Quran Text: Tanzil.net verified Uthmani script & authenticated translations\n" +
                "• Tafseer: Ibn Kathir, Al-Jalalayn, Ma'ariful Quran & Al-Quran Cloud / Quran Foundation\n" +
                "• Hadith: Sahih al-Bukhari & Sahih Muslim structured local editions\n" +
                "• Prayer Times: Karachi, MWL, ISNA, Umm Al-Qura, Dubai, Turkey, Egypt formulas\n" +
                "• Hijri Calendar: Regional Ruet-e-Hilal / Umm Al-Qura / Astronomical conventions\n" +
                "• Live Broadcasts: Saudi Quran TV (Makkah) & Saudi Sunnah TV (Madinah)"
        },
        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(10.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = {
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                cm?.setPrimaryClip(ClipData.newPlainText("Noor-e-Hidayat Diagnostics", diagnostics))
                Toast.makeText(
                    context,
                    loc(language, "Diagnostics copied to clipboard", "تفصیلات کاپی ہو گئیں", "تم نسخ معلومات التشخيص", "डायग्नोस्टिक्स कॉपी हो गया"),
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.weight(1f).height(48.dp).testTag("copy_diagnostics_btn")
        ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(loc(language, "Copy Diagnostics", "تفصیل کاپی کریں", "نسخ التشخيص", "डायग्नोस्टिक्स कॉपी"), maxLines = 1)
        }

        Button(
            onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Noor-e-Hidayat Feedback / Diagnostics")
                    putExtra(Intent.EXTRA_TEXT, diagnostics)
                }
                runCatching {
                    context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            loc(language, "Share Diagnostics", "شیئر کریں", "مشاركة", "शेयर करें")
                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            },
            modifier = Modifier.weight(1f).height(48.dp).testTag("share_diagnostics_btn")
        ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(loc(language, "Share Feedback", "رائے بھیجیں", "إرسال ملاحظة", "फ़ीडबैक भेजें"), maxLines = 1)
        }
    }
}
