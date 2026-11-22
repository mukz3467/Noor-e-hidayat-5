package com.example.ui.screens.ramadan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.ramadan.FastingStatus
import com.example.data.ramadan.QuranTargetMode
import com.example.data.ramadan.RamadanCompanionState
import com.example.data.ramadan.RamadanRepository
import com.example.data.ramadan.formatDateKey
import com.example.data.ramadan.parseDateKeyToCalendar
import com.example.data.repository.QuranData
import com.example.localization.AppLocalization
import com.example.ui.components.noorSelectableChipColors
import java.util.Calendar

@Composable
fun RamadanFastingAndGoalsTab(
    state: RamadanCompanionState,
    language: AppLanguage,
    onOpenQuranReader: (Int) -> Unit
) {
    val context = LocalContext.current
    val totalRamadanDays = state.periodStatus.totalRamadanDays
    val ramadanStartKey = state.periodStatus.confirmedStartDateKey ?: state.periodStatus.projectedStartDateKey

    val dayKeys = remember(ramadanStartKey, totalRamadanDays) {
        val baseCal = parseDateKeyToCalendar(ramadanStartKey) ?: Calendar.getInstance()
        (1..totalRamadanDays).map { dayIdx ->
            val cal = (baseCal.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, dayIdx - 1) }
            dayIdx to formatDateKey(cal)
        }
    }

    var editingDay by remember { mutableStateOf<Pair<Int?, String>?>(null) }
    var showVoluntaryDialog by remember { mutableStateOf(false) }
    var showLogReadingDialog by remember { mutableStateOf(false) }
    var quickSurahNumber by remember { mutableIntStateOf(1) }

    val ramadanEntries = state.fastingEntries.values.filter { !it.isVoluntary }
    val completedCount = ramadanEntries.count { it.status == FastingStatus.COMPLETED }
    val missedCount = ramadanEntries.count { it.status == FastingStatus.MISSED }
    val excusedCount = ramadanEntries.count { it.status == FastingStatus.EXCUSED }
    val voluntaryEntries = state.fastingEntries.values.filter { it.isVoluntary }
        .sortedByDescending { it.dateKey }

    val pagesTotal = state.quranLogs.sumOf { it.pagesRead }
    val quranGoal = state.quranGoal
    val progressFrac = (pagesTotal.toFloat() / quranGoal.totalMushafPages.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize().testTag("ramadan_fasting_quran_list")
    ) {
        // 1. Personal Fasting Tracker Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("ram_fasting_tracker", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_fasting_disclaimer", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(10.dp).testTag("ram_fasting_disclaimer")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        StatPill(AppLocalization.get("ram_status_completed", language), "$completedCount", Modifier.weight(1f))
                        StatPill(AppLocalization.get("ram_status_missed", language), "$missedCount", Modifier.weight(1f))
                        StatPill(AppLocalization.get("ram_status_excused", language), "$excusedCount", Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    // 6-column x 5-row grid (30 days max) without nested scrollables
                    dayKeys.chunked(6).forEach { rowPairs ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                        ) {
                            rowPairs.forEach { (rDay, dKey) ->
                                val status = state.fastingEntries[dKey]?.status ?: FastingStatus.NOT_RECORDED
                                val bg = when (status) {
                                    FastingStatus.COMPLETED -> MaterialTheme.colorScheme.primary
                                    FastingStatus.MISSED -> MaterialTheme.colorScheme.error
                                    FastingStatus.EXCUSED -> MaterialTheme.colorScheme.tertiary
                                    FastingStatus.NOT_RECORDED -> MaterialTheme.colorScheme.surfaceVariant
                                }
                                val fg = when (status) {
                                    FastingStatus.COMPLETED -> MaterialTheme.colorScheme.onPrimary
                                    FastingStatus.MISSED -> MaterialTheme.colorScheme.onError
                                    FastingStatus.EXCUSED -> MaterialTheme.colorScheme.onTertiary
                                    FastingStatus.NOT_RECORDED -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .heightIn(min = 48.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(bg)
                                        .clickable { editingDay = rDay to dKey }
                                        .testTag("ram_fast_day_$rDay")
                                ) {
                                    Text(
                                        text = "$rDay",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = fg,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            if (rowPairs.size < 6) {
                                repeat(6 - rowPairs.size) { Spacer(modifier = Modifier.weight(1f)) }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_voluntary_fasts", language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.weight(1f)
                        )
                        FilledTonalButton(
                            onClick = { showVoluntaryDialog = true },
                            modifier = Modifier.heightIn(min = 48.dp).testTag("ram_add_voluntary_fast_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Log")
                        }
                    }
                    voluntaryEntries.take(5).forEach { v ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(v.dateKey, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                if (v.note.isNotBlank()) {
                                    Text(v.note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            IconButton(
                                onClick = {
                                    RamadanRepository.setFastingStatus(
                                        context,
                                        v.dateKey,
                                        FastingStatus.NOT_RECORDED,
                                        isVoluntary = true,
                                        note = ""
                                    )
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove voluntary fast")
                            }
                        }
                    }
                }
            }
        }

        // 2. Quran Ramadan Goals Card
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
                            text = AppLocalization.get("ram_quran_khatam_goal", language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Switch(
                            checked = quranGoal.khatamGoalEnabled,
                            onCheckedChange = {
                                RamadanRepository.updateQuranGoal(context, quranGoal.copy(khatamGoalEnabled = it))
                            },
                            modifier = Modifier.testTag("ram_quran_goal_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$pagesTotal / ${quranGoal.totalMushafPages} ${AppLocalization.get("ram_pages_read", language)} (${(progressFrac * 100).toInt()}%)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progressFrac },
                        modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp))
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${AppLocalization.get("ram_daily_target", language)}: ${quranGoal.dailyTargetValue} ${quranGoal.dailyTargetMode.id}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(QuranTargetMode.entries) { mode ->
                            FilterChip(
                                selected = quranGoal.dailyTargetMode == mode,
                                onClick = {
                                    val defVal = when (mode) {
                                        QuranTargetMode.PAGES -> 20
                                        QuranTargetMode.AYAAT -> 50
                                        QuranTargetMode.SURAHS -> 2
                                    }
                                    RamadanRepository.updateQuranGoal(
                                        context,
                                        quranGoal.copy(dailyTargetMode = mode, dailyTargetValue = defVal)
                                    )
                                },
                                label = { Text(mode.id.replaceFirstChar { it.uppercase() }) },
                                colors = noorSelectableChipColors()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(5, 10, 20).forEach { pgs ->
                                OutlinedButton(
                                    onClick = { RamadanRepository.logQuranReading(context, pagesRead = pgs) },
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .heightIn(min = 48.dp)
                                        .testTag("ram_quick_log_${pgs}_btn")
                                ) {
                                    Text("+$pgs", maxLines = 1, softWrap = false)
                                }
                            }
                        }
                        Button(
                            onClick = { showLogReadingDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("ram_log_quran_btn")
                        ) {
                            Text(
                                text = AppLocalization.get("ram_log_reading", language),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    val targetSurah = QuranData.surahs.getOrNull(quickSurahNumber - 1)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = { if (quickSurahNumber > 1) quickSurahNumber-- }
                            ) { Text("-") }
                            Text(
                                text = "Surah $quickSurahNumber: ${targetSurah?.nameEnglish ?: ""}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { if (quickSurahNumber < 114) quickSurahNumber++ }
                            ) { Text("+") }
                        }
                        FilledTonalButton(
                            onClick = { onOpenQuranReader(quickSurahNumber) },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("ram_open_quran_btn")
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppLocalization.get("ram_open_quran_reader", language),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (state.quranLogs.isNotEmpty()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        state.quranLogs.take(5).forEach { log ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${log.dateKey}: +${log.pagesRead}p" + if (log.note.isNotBlank()) " (${log.note})" else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { RamadanRepository.deleteQuranLog(context, log.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Quran log", modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    editingDay?.let { (rDay, dateKey) ->
        val existing = state.fastingEntries[dateKey]
        var selectedStatus by remember(dateKey) { mutableStateOf(existing?.status ?: FastingStatus.COMPLETED) }
        var noteText by remember(dateKey) { mutableStateOf(existing?.note.orEmpty()) }
        AlertDialog(
            onDismissRequest = { editingDay = null },
            title = { Text("Day ${rDay ?: ""} ($dateKey)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FastingStatus.entries.forEach { st ->
                        val label = when (st) {
                            FastingStatus.COMPLETED -> AppLocalization.get("ram_status_completed", language)
                            FastingStatus.MISSED -> AppLocalization.get("ram_status_missed", language)
                            FastingStatus.EXCUSED -> AppLocalization.get("ram_status_excused", language)
                            FastingStatus.NOT_RECORDED -> AppLocalization.get("ram_status_not_recorded", language)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (selectedStatus == st) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedStatus = st }
                                .padding(10.dp)
                                .testTag("ram_status_option_${st.id}")
                        ) {
                            RadioButton(selected = selectedStatus == st, onClick = { selectedStatus = st })
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(label)
                        }
                    }
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("Note (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        RamadanRepository.setFastingStatus(
                            context = context,
                            dateKey = dateKey,
                            status = selectedStatus,
                            isVoluntary = false,
                            ramadanDay = rDay,
                            note = noteText
                        )
                        editingDay = null
                    },
                    modifier = Modifier.testTag("ram_save_fast_status_btn")
                ) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { editingDay = null }) { Text("Cancel") }
            }
        )
    }

    if (showVoluntaryDialog) {
        var dateInput by remember { mutableStateOf(formatDateKey(Calendar.getInstance())) }
        var noteInput by remember { mutableStateOf("Monday / Thursday Sunnah fast") }
        AlertDialog(
            onDismissRequest = { showVoluntaryDialog = false },
            title = { Text(AppLocalization.get("ram_voluntary_fasts", language)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = dateInput,
                        onValueChange = { dateInput = it },
                        label = { Text("Date (YYYY-MM-DD)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        label = { Text("Note (Mon/Thu, Shawwal, etc.)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (parseDateKeyToCalendar(dateInput) != null) {
                        RamadanRepository.setFastingStatus(
                            context = context,
                            dateKey = dateInput.trim(),
                            status = FastingStatus.COMPLETED,
                            isVoluntary = true,
                            ramadanDay = null,
                            note = noteInput
                        )
                        showVoluntaryDialog = false
                    }
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showVoluntaryDialog = false }) { Text("Cancel") } }
        )
    }

    if (showLogReadingDialog) {
        var pagesText by remember { mutableStateOf("4") }
        var noteText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showLogReadingDialog = false },
            title = { Text(AppLocalization.get("ram_log_reading", language)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = pagesText,
                        onValueChange = { if (it.all { c -> c.isDigit() }) pagesText = it },
                        label = { Text(AppLocalization.get("ram_pages_read", language)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.testTag("ram_quran_pages_input")
                    )
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("Note / Surah (optional)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pgs = pagesText.toIntOrNull() ?: 0
                        if (pgs > 0) {
                            RamadanRepository.logQuranReading(context, pagesRead = pgs, note = noteText)
                        }
                        showLogReadingDialog = false
                    },
                    modifier = Modifier.testTag("ram_confirm_log_quran_btn")
                ) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showLogReadingDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun StatPill(label: String, count: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Text(count, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center, maxLines = 1)
        }
    }
}
