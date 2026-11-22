package com.example.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.model.PrayerSchedule
import com.example.data.notification.PrayerNotificationManager
import com.example.data.notification.PrayerAlarmEntry
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.PrayerCalculator
import com.example.data.repository.QuranAudioManager
import com.example.localization.AppLocalization
import com.example.ui.components.MethodologyReferences
import com.example.ui.components.NoorTopBar
import com.example.ui.components.SourcesAndMethodologyCard
import com.example.ui.components.noorSelectableChipColors
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimesScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    openSettingsInitially: Boolean = false
) {
    val context = LocalContext.current

    // Shared Location & Calculation State
    val userLocation by AppLocationManager.currentLocation.collectAsState()
    val isLocating by AppLocationManager.isLocating.collectAsState()
    val calcConfig by PrayerCalculator.calcConfig.collectAsState()

    val selectedMethod = calcConfig.method
    val asrJuristic = calcConfig.asrJuristic
    val adjustments = calcConfig.adjustments

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    // Location Permission Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            AppLocationManager.requestLocationUpdate(context, forceFresh = true) { _, msg ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, AppLocalization.get("prayer_perm_denied_toast", language), Toast.LENGTH_SHORT).show()
        }
    }

    // Dialog & Sheet states
    var showCityDialog by remember { mutableStateOf(false) }
    var citySearchQuery by remember { mutableStateOf("") }
    var showManualCoordsDialog by remember { mutableStateOf(false) }
    var showSettingsSheet by remember(openSettingsInitially) { mutableStateOf(openSettingsInitially) }
    var showMonthlyTimetableDialog by remember { mutableStateOf(false) }

    // Notification settings
    val notifSettings by PrayerNotificationManager.settings.collectAsState()

    // Initialize audio, location, calculator & notifications
    LaunchedEffect(Unit) {
        QuranAudioManager.init(context)
        PrayerCalculator.init(context)
        PrayerNotificationManager.init(context)
        AppLocationManager.init(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            QuranAudioManager.stopMedia()
        }
    }

    // Live clock for updating countdown every second in selected location's timezone
    var currentClockCalendar by remember(userLocation.timeZoneId) {
        mutableStateOf(Calendar.getInstance(TimeZone.getTimeZone(userLocation.timeZoneId)))
    }
    LaunchedEffect(userLocation.timeZoneId) {
        val tz = TimeZone.getTimeZone(userLocation.timeZoneId)
        while (true) {
            currentClockCalendar = Calendar.getInstance(tz)
            delay(1000L)
        }
    }

    // Calculate current prayer schedule from shared calcConfig
    val schedule = remember(userLocation, calcConfig, currentClockCalendar.get(Calendar.DAY_OF_YEAR)) {
        PrayerCalculator.calculatePrayerTimes(
            latitude = userLocation.latitude,
            longitude = userLocation.longitude,
            calendar = currentClockCalendar,
            method = calcConfig.method,
            asrJuristic = calcConfig.asrJuristic,
            adjustments = calcConfig.adjustments,
            highLatitudeRule = calcConfig.highLatitudeRule,
            cityName = "${userLocation.cityName}, ${userLocation.countryName}",
            timeZoneId = userLocation.timeZoneId,
            customParams = calcConfig.customParams
        )
    }

    // Sync rolling 14-day prayer alarms whenever config, location, or settings change
    LaunchedEffect(userLocation, calcConfig, notifSettings) {
        PrayerNotificationManager.init(context)
        PrayerNotificationManager.rescheduleAllAlarms(context, "prayer_screen_update")
    }

    // Live Next Prayer & Countdown
    val nextPrayerInfo = remember(schedule, currentClockCalendar) {
        PrayerCalculator.calculateLiveNextPrayer(
            schedule = schedule,
            timeZoneId = userLocation.timeZoneId,
            nowCalendar = currentClockCalendar
        )
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("prayer_times", language),
                language = language,
                onBackClick = onBack,
                actions = {
                    IconButton(
                        onClick = { showMonthlyTimetableDialog = true },
                        modifier = Modifier.testTag("prayer_monthly_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = AppLocalization.get("prayer_monthly_title", language),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(
                        onClick = { showSettingsSheet = true },
                        modifier = Modifier.testTag("prayer_settings_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = AppLocalization.get("prayer_calc_settings_title", language),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            val isGpsActive = AppLocationManager.isGpsEnabled(context)
            val hasPerm = AppLocationManager.hasLocationPermission(context)
            val hasNotifPerm = PrayerNotificationManager.hasNotificationPermission(context)
            val hasExactAlarmPerm = PrayerNotificationManager.canScheduleExactAlarms(context)

            // Notification Permission Banner (Android 13+)
            if (!hasNotifPerm) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("prayer_notif_perm_banner")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(
                            Icons.Default.NotificationsOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("azan_perm_notif_title", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = AppLocalization.get("azan_perm_notif_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(AppLocalization.get("azan_grant_perm", language), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            // Exact Alarm Permission Banner (Android 12+)
            if (!hasExactAlarmPerm) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("prayer_exact_alarm_banner")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(
                            Icons.Default.AlarmOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("azan_exact_alarm_title", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = AppLocalization.get("azan_exact_alarm_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { PrayerNotificationManager.requestExactAlarmPermission(context) }
                        ) {
                            Text(AppLocalization.get("azan_grant_perm", language), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            // Alert 1: Permission Needed for Auto Worldwide Location
            if (!hasPerm) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationSearching,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("prayer_perm_auto_title", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = AppLocalization.get("prayer_perm_auto_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(AppLocalization.get("prayer_allow", language), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            } else if (!isGpsActive) {
                // Alert 2: GPS is turned OFF on mobile device
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationDisabled,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("prayer_gps_off_title", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = AppLocalization.get("prayer_gps_off_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { AppLocationManager.openLocationSettings(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(AppLocalization.get("prayer_turn_on", language), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            // Location & Date Header
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCityDialog = true }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (userLocation.isGpsFix) Icons.Default.GpsFixed else Icons.Default.LocationCity,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${userLocation.cityName}${if (userLocation.countryName.isNotBlank()) ", " + userLocation.countryName else ""}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (userLocation.isGpsFix) AppLocalization.get("prayer_gps_live", language) else AppLocalization.get("prayer_manual_preset", language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (userLocation.isGpsFix) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row {
                            IconButton(
                                onClick = { showCityDialog = true },
                                modifier = Modifier.testTag("prayer_city_picker_btn")
                            ) {
                                Icon(Icons.Default.EditLocationAlt, contentDescription = AppLocalization.get("prayer_select_city_title", language), tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(
                                onClick = {
                                    AppLocationManager.requestLocationUpdate(context, forceFresh = true) { _, msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            ) {
                                if (isLocating) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                } else {
                                    Icon(Icons.Default.MyLocation, contentDescription = AppLocalization.get("prayer_gps_live", language), tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "📅 ${schedule.dateStr}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "🌙 ${schedule.hijriDate}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hero Next Prayer Live Countdown Card
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("prayer_hero_card")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = AppLocalization.get("prayer_next_label", language),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${nextPrayerInfo.prayerNameEnglish} (${nextPrayerInfo.prayerNameUrdu})",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = nextPrayerInfo.prayerTimeFormatted,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Countdown Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = String.format(
                                    Locale.US,
                                    "-%02dh : %02dm : %02ds",
                                    nextPrayerInfo.remainingHours,
                                    nextPrayerInfo.remainingMinutes,
                                    nextPrayerInfo.remainingSeconds
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress indicator towards prayer
                    LinearProgressIndicator(
                        progress = { nextPrayerInfo.progressPercent },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Prayer Times Schedule Cards
            Text(
                text = AppLocalization.get("prayer_today_schedule", language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            val prayerItems = listOf(
                PrayerItemData("fajr", "Fajr", "فجر", schedule.fajr, notifSettings.fajrEnabled, adjustments.fajrMinutes),
                PrayerItemData("sunrise", "Sunrise", "طلوعِ آفتاب", schedule.sunrise, false, adjustments.sunriseMinutes, isSunrise = true),
                PrayerItemData("dhuhr", "Dhuhr", "ظہر", schedule.dhuhr, notifSettings.dhuhrEnabled, adjustments.dhuhrMinutes),
                PrayerItemData("asr", "Asr", "عصر", schedule.asr, notifSettings.asrEnabled, adjustments.asrMinutes),
                PrayerItemData("maghrib", "Maghrib", "مغرب", schedule.maghrib, notifSettings.maghribEnabled, adjustments.maghribMinutes),
                PrayerItemData("isha", "Isha", "عشاء", schedule.isha, notifSettings.ishaEnabled, adjustments.ishaMinutes)
            )

            prayerItems.forEach { item ->
                val isNext = (item.key == nextPrayerInfo.prayerKey)
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isNext) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (isNext) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("prayer_row_${item.key}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = when (item.key) {
                                            "fajr" -> Icons.Default.WbTwilight
                                            "sunrise" -> Icons.Default.WbSunny
                                            "dhuhr" -> Icons.Default.Brightness7
                                            "asr" -> Icons.Default.Brightness6
                                            "maghrib" -> Icons.Default.Brightness4
                                            else -> Icons.Default.NightsStay
                                        },
                                        contentDescription = null,
                                        tint = if (isNext) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.titleEn,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(${item.titleUr})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (item.adjustmentMinutes != 0) {
                                    Text(
                                        text = "${AppLocalization.get("prayer_adjusted_prefix", language)} ${if (item.adjustmentMinutes > 0) "+${item.adjustmentMinutes}" else "${item.adjustmentMinutes}"}m",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFC88A00)
                                    )
                                }
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.time,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )

                            if (!item.isSunrise) {
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        val updated = when (item.key) {
                                            "fajr" -> notifSettings.copy(fajrEnabled = !notifSettings.fajrEnabled)
                                            "dhuhr" -> notifSettings.copy(dhuhrEnabled = !notifSettings.dhuhrEnabled)
                                            "asr" -> notifSettings.copy(asrEnabled = !notifSettings.asrEnabled)
                                            "maghrib" -> notifSettings.copy(maghribEnabled = !notifSettings.maghribEnabled)
                                            else -> notifSettings.copy(ishaEnabled = !notifSettings.ishaEnabled)
                                        }
                                        PrayerNotificationManager.updateSettings(context, updated)
                                    },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = if (item.isAlarmEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                        contentDescription = AppLocalization.get("azan_notifications", language),
                                        tint = if (item.isAlarmEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Method & Juristic Badge Footer
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = AppLocalization.get("prayer_calc_settings_title", language),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "• ${AppLocalization.get("prayer_method_label", language)} ${selectedMethod.displayName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• ${AppLocalization.get("prayer_asr_label", language)} ${asrJuristic.displayName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "• ${AppLocalization.get("prayer_timezone_label", language)}: ${userLocation.timeZoneId}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = AppLocalization.get("prayer_mosque_disclaimer", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            SourcesAndMethodologyCard(
                language = language,
                references = MethodologyReferences.ALL_REFS
            )
        }
    }

    // Worldwide City Selection Dialog
    if (showCityDialog) {
        val filteredCities = remember(citySearchQuery) {
            if (citySearchQuery.isBlank()) {
                AppLocationManager.PRESET_CITIES
            } else {
                AppLocationManager.PRESET_CITIES.filter {
                    it.nameEn.contains(citySearchQuery, ignoreCase = true) ||
                            it.nameUr.contains(citySearchQuery) ||
                            it.country.contains(citySearchQuery, ignoreCase = true)
                }
            }
        }

        AlertDialog(
            onDismissRequest = {
                showCityDialog = false
                citySearchQuery = ""
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(AppLocalization.get("prayer_select_city_title", language), fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        showCityDialog = false
                        showManualCoordsDialog = true
                    }) {
                        Icon(Icons.Default.PinDrop, contentDescription = AppLocalization.get("prayer_manual_coords_title", language), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = citySearchQuery,
                        onValueChange = { citySearchQuery = it },
                        placeholder = { Text(AppLocalization.get("prayer_search_city_hint", language), fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (citySearchQuery.isNotEmpty()) {
                                IconButton(onClick = { citySearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = AppLocalization.get("close", language))
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    LazyColumn(modifier = Modifier.heightIn(max = 350.dp)) {
                        items(filteredCities) { city ->
                            val isSel = (userLocation.cityName == city.nameEn)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        AppLocationManager.selectPresetCity(context, city)
                                        if (calcConfig.isAutoMethodForLocation) {
                                            PrayerCalculator.updateConfig(
                                                context,
                                                calcConfig.copy(
                                                    method = PrayerCalculator.getRecommendedMethod(city.country, city.latitude, city.longitude),
                                                    asrJuristic = PrayerCalculator.getRecommendedAsr(city.country)
                                                )
                                            )
                                        }
                                        showCityDialog = false
                                        citySearchQuery = ""
                                    }
                                    .padding(vertical = 10.dp, horizontal = 4.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${city.nameEn} (${city.country})",
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${city.nameUr} • TZ: ${city.timeZoneId}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSel) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showCityDialog = false
                    citySearchQuery = ""
                }) { Text(AppLocalization.get("prayer_close", language)) }
            }
        )
    }

    // Manual Coordinates Entry Dialog
    if (showManualCoordsDialog) {
        var latInput by remember { mutableStateOf(userLocation.latitude.toString()) }
        var lonInput by remember { mutableStateOf(userLocation.longitude.toString()) }
        var cityInput by remember { mutableStateOf(userLocation.cityName) }
        var countryInput by remember { mutableStateOf(userLocation.countryName) }

        AlertDialog(
            onDismissRequest = { showManualCoordsDialog = false },
            title = { Text(AppLocalization.get("prayer_enter_coords_title", language), fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = AppLocalization.get("prayer_enter_coords_desc", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = latInput,
                        onValueChange = { latInput = it },
                        label = { Text(AppLocalization.get("prayer_lat_label", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = lonInput,
                        onValueChange = { lonInput = it },
                        label = { Text(AppLocalization.get("prayer_lon_label", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cityInput,
                        onValueChange = { cityInput = it },
                        label = { Text(AppLocalization.get("prayer_city_label", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = countryInput,
                        onValueChange = { countryInput = it },
                        label = { Text(AppLocalization.get("prayer_country_label", language)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val latVal = latInput.toDoubleOrNull()
                        val lonVal = lonInput.toDoubleOrNull()
                        if (latVal != null && lonVal != null && latVal in -90.0..90.0 && lonVal in -180.0..180.0) {
                            AppLocationManager.setManualCoordinates(
                                context = context,
                                lat = latVal,
                                lon = lonVal,
                                cityName = cityInput,
                                countryName = countryInput,
                                timeZoneId = userLocation.timeZoneId
                            )
                            if (calcConfig.isAutoMethodForLocation) {
                                PrayerCalculator.updateConfig(
                                    context,
                                    calcConfig.copy(
                                        method = PrayerCalculator.getRecommendedMethod(countryInput, latVal, lonVal),
                                        asrJuristic = PrayerCalculator.getRecommendedAsr(countryInput)
                                    )
                                )
                            }
                            showManualCoordsDialog = false
                            Toast.makeText(context, AppLocalization.get("prayer_coords_updated", language), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, AppLocalization.get("prayer_invalid_coords", language), Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(AppLocalization.get("prayer_apply", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualCoordsDialog = false }) { Text(AppLocalization.get("cancel", language)) }
            }
        )
    }

    // Calculation & Adjustments Settings Bottom Sheet
    if (showSettingsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSettingsSheet = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = AppLocalization.get("prayer_calc_settings_title", language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Calculation Method
                Text(AppLocalization.get("prayer_method_label", language), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                PrayerCalculator.CalculationMethod.values().forEach { method ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                PrayerCalculator.updateConfig(
                                    context,
                                    calcConfig.copy(method = method, isAutoMethodForLocation = false)
                                )
                            }
                            .padding(vertical = 6.dp)
                    ) {
                        RadioButton(
                            selected = selectedMethod == method,
                            onClick = {
                                PrayerCalculator.updateConfig(
                                    context,
                                    calcConfig.copy(method = method, isAutoMethodForLocation = false)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(method.displayName, style = MaterialTheme.typography.bodySmall)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Asr Juristic Method
                Text(AppLocalization.get("prayer_asr_label", language), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                PrayerCalculator.AsrJuristic.values().forEach { juristic ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                PrayerCalculator.updateConfig(context, calcConfig.copy(asrJuristic = juristic))
                            }
                            .padding(vertical = 6.dp)
                    ) {
                        RadioButton(
                            selected = asrJuristic == juristic,
                            onClick = {
                                PrayerCalculator.updateConfig(context, calcConfig.copy(asrJuristic = juristic))
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(juristic.displayName, style = MaterialTheme.typography.bodySmall)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Minute Adjustments
                Text(AppLocalization.get("prayer_manual_adj_label", language), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                AdjustmentStepper(AppLocalization.get("fajr", language), adjustments.fajrMinutes) {
                    PrayerCalculator.updateConfig(context, calcConfig.copy(adjustments = adjustments.copy(fajrMinutes = it)))
                }
                AdjustmentStepper(AppLocalization.get("dhuhr", language), adjustments.dhuhrMinutes) {
                    PrayerCalculator.updateConfig(context, calcConfig.copy(adjustments = adjustments.copy(dhuhrMinutes = it)))
                }
                AdjustmentStepper(AppLocalization.get("asr", language), adjustments.asrMinutes) {
                    PrayerCalculator.updateConfig(context, calcConfig.copy(adjustments = adjustments.copy(asrMinutes = it)))
                }
                AdjustmentStepper(AppLocalization.get("maghrib", language), adjustments.maghribMinutes) {
                    PrayerCalculator.updateConfig(context, calcConfig.copy(adjustments = adjustments.copy(maghribMinutes = it)))
                }
                AdjustmentStepper(AppLocalization.get("isha", language), adjustments.ishaMinutes) {
                    PrayerCalculator.updateConfig(context, calcConfig.copy(adjustments = adjustments.copy(ishaMinutes = it)))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Adhan Notification Sound Selector
                Text(AppLocalization.get("azan_audio_label", language), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        "makkah" to AppLocalization.get("azan_makkah", language),
                        "madina" to AppLocalization.get("azan_madinah", language),
                        "fajr" to AppLocalization.get("azan_fajr", language),
                        "silent" to AppLocalization.get("azan_silent", language)
                    ).forEach { (id, label) ->
                        FilterChip(
                            selected = notifSettings.adhanSound == id,
                            onClick = {
                                val updated = notifSettings.copy(adhanSound = id)
                                PrayerNotificationManager.updateSettings(context, updated)
                            },
                            label = { Text(label) },
                            colors = noorSelectableChipColors()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(AppLocalization.get("azan_vibration_toggle", language), style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = notifSettings.vibrationEnabled,
                        onCheckedChange = {
                            PrayerNotificationManager.updateSettings(context, notifSettings.copy(vibrationEnabled = it))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!PrayerNotificationManager.hasNotificationPermission(context)) {
                    OutlinedButton(
                        onClick = { notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(AppLocalization.get("azan_perm_notif_title", language))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (!PrayerNotificationManager.canUseExactAlarms(context)) {
                    OutlinedButton(
                        onClick = { PrayerNotificationManager.requestExactAlarmPermission(context) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Alarm, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(AppLocalization.get("azan_exact_alarm_title", language))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            PrayerNotificationManager.sendTestNotification(context, "Asr", schedule.asr)
                            Toast.makeText(context, AppLocalization.get("prayer_test_toast", language), Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppLocalization.get("azan_test_btn", language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    OutlinedButton(
                        onClick = { PrayerNotificationManager.stopAzanAudio(context) },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.widthIn(max = 140.dp).heightIn(min = 48.dp)
                    ) {
                        Text(
                            text = AppLocalization.get("azan_stop_btn", language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    // Monthly Timetable Dialog
    if (showMonthlyTimetableDialog) {
        val tzCalNow = remember(userLocation.timeZoneId) {
            Calendar.getInstance(TimeZone.getTimeZone(userLocation.timeZoneId))
        }
        var selectedMonthIndex by remember(userLocation.timeZoneId) { mutableIntStateOf(tzCalNow.get(Calendar.MONTH) + 1) }
        val currentYear = tzCalNow.get(Calendar.YEAR)

        val monthlySchedules = remember(selectedMonthIndex, userLocation, selectedMethod, asrJuristic, adjustments) {
            PrayerCalculator.generateMonthlyTimetable(
                year = currentYear,
                month = selectedMonthIndex,
                latitude = userLocation.latitude,
                longitude = userLocation.longitude,
                timeZoneId = userLocation.timeZoneId,
                method = selectedMethod,
                asrJuristic = asrJuristic,
                adjustments = adjustments
            )
        }

        AlertDialog(
            onDismissRequest = { showMonthlyTimetableDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(AppLocalization.get("prayer_monthly_title", language), fontWeight = FontWeight.Bold)
                    Row {
                        IconButton(onClick = { if (selectedMonthIndex > 1) selectedMonthIndex-- }) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = AppLocalization.get("back_cd", language))
                        }
                        IconButton(onClick = { if (selectedMonthIndex < 12) selectedMonthIndex++ }) {
                            Icon(Icons.Default.ChevronRight, contentDescription = AppLocalization.get("next", language))
                        }
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "$selectedMonthIndex/$currentYear • ${userLocation.cityName}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.height(350.dp)) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(vertical = 6.dp, horizontal = 4.dp)
                            ) {
                                Text(AppLocalization.get("prayer_day_col", language), modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(AppLocalization.get("fajr", language), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(AppLocalization.get("dhuhr", language), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(AppLocalization.get("asr", language), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(AppLocalization.get("maghrib", language), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(AppLocalization.get("isha", language), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        items(monthlySchedules) { row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            ) {
                                Text(row.dateStr.take(2), modifier = Modifier.weight(0.8f), fontSize = 11.sp)
                                Text(row.fajr.replace(" AM", ""), modifier = Modifier.weight(1f), fontSize = 11.sp)
                                Text(row.dhuhr.replace(" PM", ""), modifier = Modifier.weight(1f), fontSize = 11.sp)
                                Text(row.asr.replace(" PM", ""), modifier = Modifier.weight(1f), fontSize = 11.sp)
                                Text(row.maghrib.replace(" PM", ""), modifier = Modifier.weight(1f), fontSize = 11.sp)
                                Text(row.isha.replace(" PM", ""), modifier = Modifier.weight(1f), fontSize = 11.sp)
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val shareText = buildString {
                            append("${AppLocalization.get("prayer_monthly_title", language)} - ${userLocation.cityName} ($selectedMonthIndex/$currentYear)\n")
                            append("Method: ${selectedMethod.displayName}\n\n")
                            append("Day | Fajr | Dhuhr | Asr | Maghrib | Isha\n")
                            monthlySchedules.forEach { s ->
                                append("${s.dateStr.take(2)} | ${s.fajr} | ${s.dhuhr} | ${s.asr} | ${s.maghrib} | ${s.isha}\n")
                            }
                            append("\n${AppLocalization.get("app_name", language)}")
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        runCatching {
                            context.startActivity(Intent.createChooser(intent, AppLocalization.get("prayer_share_timetable", language)))
                        }
                    }
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppLocalization.get("prayer_share_timetable", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showMonthlyTimetableDialog = false }) { Text(AppLocalization.get("prayer_close", language)) }
            }
        )
    }
}

@Composable
fun AdjustmentStepper(name: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(name, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (value > -30) onValueChange(value - 1) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
            }
            Text(
                text = "${if (value > 0) "+$value" else "$value"} min",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
                onClick = { if (value < 30) onValueChange(value + 1) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}

data class PrayerItemData(
    val key: String,
    val titleEn: String,
    val titleUr: String,
    val time: String,
    val isAlarmEnabled: Boolean,
    val adjustmentMinutes: Int,
    val isSunrise: Boolean = false
)
