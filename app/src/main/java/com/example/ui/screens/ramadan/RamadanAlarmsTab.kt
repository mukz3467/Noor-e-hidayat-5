package com.example.ui.screens.ramadan

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.ramadan.AlarmSoundType
import com.example.data.ramadan.NightWorshipConfig
import com.example.data.ramadan.RamadanAlarmConfig
import com.example.data.ramadan.RamadanAlarmScheduler
import com.example.data.ramadan.RamadanCompanionState
import com.example.data.ramadan.RamadanRepository
import com.example.localization.AppLocalization
import com.example.ui.components.noorSelectableChipColors

@Composable
fun RamadanAlarmsTab(
    state: RamadanCompanionState,
    language: AppLanguage
) {
    val context = LocalContext.current
    val cfg = state.alarmConfig
    val night = state.nightWorship

    var customSehriText by remember(cfg.sehriOffsetMinutes) { mutableStateOf(cfg.sehriOffsetMinutes.toString()) }
    var customIftarText by remember(cfg.iftarOffsetMinutes) { mutableStateOf(cfg.iftarOffsetMinutes.toString()) }

    val notifLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        RamadanRepository.rescheduleAlarmsOfflineFallback(context, reason = "notif_perm_result")
    }

    fun updateCfg(updated: RamadanAlarmConfig) {
        RamadanRepository.updateAlarmConfig(context, updated)
    }

    fun updateNight(updated: NightWorshipConfig) {
        RamadanRepository.updateNightWorshipConfig(context, updated)
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("ramadan_alarms_list")
    ) {
        // Permission alerts
        if (!state.hasNotificationPermission) {
            item {
                PermissionWarningCard(
                    message = AppLocalization.get("ram_perm_notif_missing", language),
                    actionLabel = AppLocalization.get("ram_grant_permission", language),
                    tag = "ram_grant_notif_btn",
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                )
            }
        }
        if (!state.canScheduleExactAlarms && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            item {
                PermissionWarningCard(
                    message = AppLocalization.get("ram_perm_exact_missing", language),
                    actionLabel = AppLocalization.get("ram_grant_permission", language),
                    tag = "ram_grant_exact_btn",
                    onClick = {
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        runCatching { context.startActivity(intent) }
                    }
                )
            }
        }

        // Master & Sehri/Iftar Configuration Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_enable_alarms", language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = cfg.alarmsMasterEnabled,
                            onCheckedChange = { updateCfg(cfg.copy(alarmsMasterEnabled = it)) },
                            modifier = Modifier.testTag("ram_alarm_master_switch")
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    // Auto-pause outside Ramadan
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_ramadan_only_label", language),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = cfg.activeOnlyDuringRamadan,
                            onCheckedChange = { updateCfg(cfg.copy(activeOnlyDuringRamadan = it)) },
                            modifier = Modifier.testTag("ram_alarm_ramadan_only_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sehri Pre-Alarm
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_sehri_pre_label", language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = cfg.sehriPreAlarmEnabled,
                            onCheckedChange = { updateCfg(cfg.copy(sehriPreAlarmEnabled = it)) },
                            modifier = Modifier.testTag("ram_sehri_pre_switch")
                        )
                    }
                    if (cfg.sehriPreAlarmEnabled) {
                        val sehriPresets = listOf(10, 15, 20, 30, 45, 60)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(sehriPresets) { mins ->
                                FilterChip(
                                    selected = cfg.sehriOffsetMinutes == mins,
                                    onClick = { updateCfg(cfg.copy(sehriOffsetMinutes = mins)) },
                                    label = { Text("${mins}m") },
                                    colors = noorSelectableChipColors(),
                                    modifier = Modifier.testTag("ram_sehri_chip_$mins")
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = customSehriText,
                                onValueChange = { txt ->
                                    if (txt.length <= 3 && txt.all { it.isDigit() }) {
                                        customSehriText = txt
                                        txt.toIntOrNull()?.takeIf { it in 1..180 }?.let { valid ->
                                            updateCfg(cfg.copy(sehriOffsetMinutes = valid))
                                        }
                                    }
                                },
                                label = { Text("Custom (1-180 min)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("ram_sehri_custom_input")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_sehri_fajr_end_label", language),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = cfg.sehriFajrEndAlarmEnabled,
                            onCheckedChange = { updateCfg(cfg.copy(sehriFajrEndAlarmEnabled = it)) },
                            modifier = Modifier.testTag("ram_sehri_fajr_switch")
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    // Iftar Pre-Reminder
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_iftar_pre_label", language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = cfg.iftarPreReminderEnabled,
                            onCheckedChange = { updateCfg(cfg.copy(iftarPreReminderEnabled = it)) },
                            modifier = Modifier.testTag("ram_iftar_pre_switch")
                        )
                    }
                    if (cfg.iftarPreReminderEnabled) {
                        val iftarPresets = listOf(5, 10, 15, 20)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(iftarPresets) { mins ->
                                FilterChip(
                                    selected = cfg.iftarOffsetMinutes == mins,
                                    onClick = { updateCfg(cfg.copy(iftarOffsetMinutes = mins)) },
                                    label = { Text("${mins}m") },
                                    colors = noorSelectableChipColors(),
                                    modifier = Modifier.testTag("ram_iftar_chip_$mins")
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = customIftarText,
                            onValueChange = { txt ->
                                if (txt.length <= 3 && txt.all { it.isDigit() }) {
                                    customIftarText = txt
                                    txt.toIntOrNull()?.takeIf { it in 1..120 }?.let { valid ->
                                        updateCfg(cfg.copy(iftarOffsetMinutes = valid))
                                    }
                                }
                            },
                            label = { Text("Custom (1-120 min)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("ram_iftar_custom_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_iftar_main_label", language),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = cfg.iftarMainAlarmEnabled,
                            onCheckedChange = { updateCfg(cfg.copy(iftarMainAlarmEnabled = it)) },
                            modifier = Modifier.testTag("ram_iftar_main_switch")
                        )
                    }
                }
            }
        }

        // Sound, Vibration & Snooze
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("ram_sound_label", language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(AlarmSoundType.entries) { st ->
                            val label = when (st) {
                                AlarmSoundType.ADHAN_MAKKAH -> "Makkah Adhan"
                                AlarmSoundType.ADHAN_MADINA -> "Madina Adhan"
                                AlarmSoundType.ADHAN_FAJR -> "Fajr Adhan"
                                AlarmSoundType.NOTIFICATION_TONE -> "Alarm Tone"
                                AlarmSoundType.VIBRATE_ONLY -> "Vibrate"
                                AlarmSoundType.SILENT -> "Silent"
                            }
                            FilterChip(
                                selected = cfg.alarmSoundType == st,
                                onClick = { updateCfg(cfg.copy(alarmSoundType = st)) },
                                label = { Text(label) },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("ram_sound_${st.id}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(AppLocalization.get("ram_vibration_label", language))
                        Switch(
                            checked = cfg.vibrationEnabled,
                            onCheckedChange = { updateCfg(cfg.copy(vibrationEnabled = it)) },
                            modifier = Modifier.testTag("ram_vibration_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = AppLocalization.get("ram_snooze_label", language),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5, 10, 15).forEach { snooze ->
                            FilterChip(
                                selected = cfg.snoozeMinutes == snooze,
                                onClick = { updateCfg(cfg.copy(snoozeMinutes = snooze)) },
                                label = { Text("${snooze}m") },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("ram_snooze_${snooze}")
                            )
                        }
                    }
                }
            }
        }

        // Night Worship Reminders Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("ram_night_worship", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${AppLocalization.get("ram_taraweeh_reminder", language)} (${String.format("%02d:%02d", night.taraweehHour, night.taraweehMinute)})")
                        Switch(
                            checked = night.taraweehReminderEnabled,
                            onCheckedChange = { updateNight(night.copy(taraweehReminderEnabled = it)) },
                            modifier = Modifier.testTag("ram_taraweeh_switch")
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${AppLocalization.get("ram_tahajjud_reminder", language)} (-${night.tahajjudOffsetBeforeFajrMinutes}m)")
                        Switch(
                            checked = night.tahajjudReminderEnabled,
                            onCheckedChange = { updateNight(night.copy(tahajjudReminderEnabled = it)) },
                            modifier = Modifier.testTag("ram_tahajjud_switch")
                        )
                    }
                    if (night.tahajjudReminderEnabled) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(30, 45, 60, 90).forEach { mins ->
                                FilterChip(
                                    selected = night.tahajjudOffsetBeforeFajrMinutes == mins,
                                    onClick = { updateNight(night.copy(tahajjudOffsetBeforeFajrMinutes = mins, tahajjudUseCustomFixedTime = false)) },
                                    label = { Text("-${mins}m") },
                                    colors = noorSelectableChipColors()
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_last10_reminder", language),
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = night.lastTenNightsReminderEnabled,
                            onCheckedChange = { updateNight(night.copy(lastTenNightsReminderEnabled = it)) },
                            modifier = Modifier.testTag("ram_last10_switch")
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = AppLocalization.get("ram_last10_note", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Test Alarm & Upcoming Schedule Preview
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            RamadanAlarmScheduler.handleAlarmFired(
                                context = context,
                                alarmKey = "test_ramadan",
                                title = AppLocalization.get("ram_alarm_sehri_pre_title", language),
                                timeFormatted = state.todayTiming?.fajrDisplay ?: "04:30 AM",
                                soundTypeId = cfg.alarmSoundType.id,
                                snoozeMinutes = cfg.snoozeMinutes,
                                isTest = true
                            )
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("ram_test_alarm_btn")
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(AppLocalization.get("ram_test_alarm", language))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = AppLocalization.get("ram_upcoming_alarms", language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (state.upcomingAlarms.isEmpty()) {
                        Text(
                            text = AppLocalization.get("ram_no_upcoming_alarms", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        state.upcomingAlarms.take(8).forEach { alarm ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(Icons.Default.Alarm, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(alarm.title, style = MaterialTheme.typography.bodyMedium)
                                }
                                Text(
                                    text = alarm.formattedTime,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionWarningCard(
    message: String,
    actionLabel: String,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onClick, modifier = Modifier.heightIn(min = 48.dp).testTag(tag)) {
                Text(actionLabel)
            }
        }
    }
}
