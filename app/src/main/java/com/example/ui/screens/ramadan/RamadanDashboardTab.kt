package com.example.ui.screens.ramadan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.location.UserLocation
import com.example.data.model.AppLanguage
import com.example.data.ramadan.FastingStatus
import com.example.data.ramadan.PeriodConfirmationSource
import com.example.data.ramadan.RamadanCompanionState
import com.example.data.ramadan.TimingSource
import com.example.data.ramadan.formatDateKey
import com.example.data.repository.PrayerCalculator
import com.example.localization.AppLocalization
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun RamadanDashboardTab(
    state: RamadanCompanionState,
    location: UserLocation,
    calcConfig: PrayerCalculator.PrayerCalculationConfig,
    language: AppLanguage,
    onRefreshTimings: () -> Unit,
    onSelectTab: (Int) -> Unit,
    onOpenPrayerTimes: () -> Unit
) {
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            nowMillis = System.currentTimeMillis()
            delay(1000L)
        }
    }

    val todayKey = remember(nowMillis) { formatDateKey(Calendar.getInstance()) }
    val nextAlarm = remember(state.upcomingAlarms, nowMillis) {
        state.upcomingAlarms.firstOrNull { it.triggerAtMillis > nowMillis }
    }
    val countdownText = remember(nextAlarm, nowMillis) {
        if (nextAlarm == null) "--:--:--"
        else {
            val sec = ((nextAlarm.triggerAtMillis - nowMillis) / 1000L).coerceAtLeast(0L)
            val h = sec / 3600L
            val m = (sec % 3600L) / 60L
            val s = sec % 60L
            String.format(Locale.US, "%02dh %02dm %02ds", h, m, s)
        }
    }

    val fastingCompletedCount = state.fastingEntries.values.count {
        !it.isVoluntary && it.status == FastingStatus.COMPLETED
    }
    val pagesReadTotal = state.quranLogs.sumOf { it.pagesRead }
    val quranProgress = (pagesReadTotal.toFloat() / state.quranGoal.totalMushafPages.coerceAtLeast(1).toFloat())
        .coerceIn(0f, 1f)
    val todayCompletedDeeds = state.completedDeedIdsByDate[todayKey]?.size ?: 0
    val totalDeeds = state.deeds.size.coerceAtLeast(1)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("ramadan_dashboard_list")
    ) {
        // 1. Hijri & Ramadan Status Hero Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth().testTag("ramadan_hero_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = state.periodStatus.activeHijriDate.formatted(language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.NightsStay,
                            contentDescription = AppLocalization.get("ramadan_companion", language),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    val statusText = if (state.periodStatus.isRamadanActive) {
                        val day = state.periodStatus.currentRamadanDay ?: 1
                        String.format(
                            Locale.getDefault(),
                            AppLocalization.get("ram_day_progress", language),
                            day,
                            state.periodStatus.totalRamadanDays
                        )
                    } else {
                        val daysLeft = state.periodStatus.daysUntilRamadan ?: 0
                        String.format(
                            Locale.getDefault(),
                            AppLocalization.get("ram_days_until", language),
                            daysLeft
                        )
                    }
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    if (state.periodStatus.isRamadanActive) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val dayFrac = (state.periodStatus.currentRamadanDay ?: 1).toFloat() /
                            state.periodStatus.totalRamadanDays.coerceAtLeast(1).toFloat()
                        LinearProgressIndicator(
                            progress = { dayFrac.coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    ) {
                        val badgeKey = if (state.periodStatus.activeSource == PeriodConfirmationSource.LOCALLY_CONFIRMED_START) {
                            "ram_confirmed_start_badge"
                        } else {
                            "ram_estimated_hijri_badge"
                        }
                        Text(
                            text = AppLocalization.get(badgeKey, language),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // 2. Sehri & Iftar Dual Cards + Source Info
        item {
            val timing = state.todayTiming
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("ramadan_timings_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = AppLocalization.get("ram_sehri_ends", language),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = timing?.fajrDisplay ?: "--:--",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.testTag("ram_sehri_time_text")
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = AppLocalization.get("ram_iftar_time", language),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = timing?.maghribDisplay ?: "--:--",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.testTag("ram_iftar_time_text")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    val sourceLabel = if (timing?.source == TimingSource.ALADHAN_API) {
                        AppLocalization.get("ram_source_api", language)
                    } else {
                        AppLocalization.get("ram_source_offline", language)
                    }
                    val updatedFmt = timing?.lastUpdatedMillis?.let {
                        SimpleDateFormat("dd MMM HH:mm", Locale.US).format(Date(it))
                    } ?: "--"
                    Text(
                        text = "$sourceLabel • ${location.cityName} (${location.timeZoneId}) • $updatedFmt",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${calcConfig.method.displayName} (${calcConfig.asrJuristic.name})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (timing == null || timing.isStale(nowMillis) || !timing.isForDateKey(todayKey)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = AppLocalization.get("ram_stale_warning", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = onRefreshTimings,
                            enabled = !state.isSyncingTimings,
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("ram_refresh_timings_btn")
                        ) {
                            if (state.isSyncingTimings) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AppLocalization.get("ram_refresh_timings", language), fontSize = 12.sp)
                        }
                        TextButton(
                            onClick = onOpenPrayerTimes,
                            modifier = Modifier.heightIn(min = 48.dp).testTag("ram_open_prayer_settings_btn")
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(location.cityName, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 3. Next Alarm Countdown Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("ramadan_next_alarm_card")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = AppLocalization.get("ram_next_alarm", language),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppLocalization.get("ram_next_alarm", language),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = nextAlarm?.let { "${it.title} • ${it.formattedTime}" }
                                ?: AppLocalization.get("ram_no_upcoming_alarms", language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (nextAlarm != null) {
                            Text(
                                text = countdownText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    TextButton(
                        onClick = { onSelectTab(1) },
                        modifier = Modifier.heightIn(min = 48.dp).testTag("ram_quick_alarms_btn")
                    ) {
                        Text(AppLocalization.get("ram_tab_alarms", language))
                    }
                }
            }
        }

        // 4. Progress Summary Cards Row (Fasting, Quran, Deeds)
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DashboardMiniCard(
                    title = AppLocalization.get("ram_tab_fasting", language),
                    value = "$fastingCompletedCount / ${state.periodStatus.totalRamadanDays}",
                    subtitle = AppLocalization.get("ram_status_completed", language),
                    icon = Icons.Default.CheckCircle,
                    tag = "ram_quick_fasting_card",
                    onClick = { onSelectTab(2) },
                    modifier = Modifier.weight(1f)
                )
                DashboardMiniCard(
                    title = AppLocalization.get("ram_quran_khatam_goal", language),
                    value = "${(quranProgress * 100).toInt()}%",
                    subtitle = "$pagesReadTotal ${AppLocalization.get("ram_pages_read", language)}",
                    icon = Icons.Default.MenuBook,
                    tag = "ram_quick_quran_card",
                    onClick = { onSelectTab(2) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            DashboardMiniCard(
                title = AppLocalization.get("ram_good_deeds", language),
                value = "$todayCompletedDeeds / $totalDeeds",
                subtitle = "${AppLocalization.get("ram_authentic_duas", language)} & ${AppLocalization.get("ram_iftar_checklist", language)}",
                icon = Icons.Default.Favorite,
                tag = "ram_quick_deeds_card",
                onClick = { onSelectTab(3) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DashboardMiniCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.testTag(tag)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}
