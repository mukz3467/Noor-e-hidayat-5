package com.example.ui.screens.ramadan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.ramadan.RamadanCompanionState
import com.example.data.ramadan.RamadanRepository
import com.example.data.ramadan.parseDateKeyToCalendar
import com.example.data.repository.PrayerCalculator
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors
import kotlinx.coroutines.launch

@Composable
fun RamadanCompanionScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    onOpenQuranReader: (Int) -> Unit,
    onOpenPrayerTimes: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by RamadanRepository.state.collectAsStateWithLifecycle()
    val location by AppLocationManager.currentLocation.collectAsStateWithLifecycle()
    val calcConfig by PrayerCalculator.calcConfig.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        RamadanRepository.init(context)
        scope.launch {
            RamadanRepository.refreshTodayTimingsAndReschedule(
                context = context,
                preferNetwork = true,
                reason = "ramadan_screen_opened"
            )
        }
    }

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabKeys = listOf(
        "ram_tab_dashboard",
        "ram_tab_alarms",
        "ram_tab_fasting",
        "ram_tab_deeds",
        "ram_tab_settings"
    )

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("ramadan_companion", language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("ramadan_companion_screen")
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 680.dp)
                        .fillMaxSize()
                ) {
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        edgePadding = 12.dp,
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        tabKeys.forEachIndexed { idx, key ->
                            Tab(
                                selected = selectedTab == idx,
                                onClick = { selectedTab = idx },
                                selectedContentColor = MaterialTheme.colorScheme.primary,
                                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                text = {
                                    Text(
                                        text = AppLocalization.get(key, language),
                                        fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                modifier = Modifier.testTag("ram_tab_$idx")
                            )
                        }
                    }

                    when (selectedTab) {
                        0 -> RamadanDashboardTab(
                            state = state,
                            location = location,
                            calcConfig = calcConfig,
                            language = language,
                            onRefreshTimings = {
                                scope.launch {
                                    RamadanRepository.refreshTodayTimingsAndReschedule(
                                        context = context,
                                        preferNetwork = true,
                                        reason = "user_manual_refresh"
                                    )
                                }
                            },
                            onSelectTab = { selectedTab = it },
                            onOpenPrayerTimes = onOpenPrayerTimes
                        )
                        1 -> RamadanAlarmsTab(
                            state = state,
                            language = language
                        )
                        2 -> RamadanFastingAndGoalsTab(
                            state = state,
                            language = language,
                            onOpenQuranReader = onOpenQuranReader
                        )
                        3 -> RamadanDuasDeedsTab(
                            state = state,
                            language = language
                        )
                        4 -> RamadanInternalPrefsTab(
                            state = state,
                            language = language,
                            onOpenPrayerTimes = onOpenPrayerTimes
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RamadanInternalPrefsTab(
    state: RamadanCompanionState,
    language: AppLanguage,
    onOpenPrayerTimes: () -> Unit
) {
    val context = LocalContext.current
    var startDateText by remember(state.periodStatus.confirmedStartDateKey, state.periodStatus.projectedStartDateKey) {
        mutableStateOf(state.periodStatus.confirmedStartDateKey ?: state.periodStatus.projectedStartDateKey)
    }
    var monthLen by remember(state.periodStatus.totalRamadanDays) {
        mutableIntStateOf(state.periodStatus.totalRamadanDays)
    }
    var invalidDateError by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize().testTag("ramadan_prefs_list")
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("ram_internal_prefs", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = AppLocalization.get("ram_start_override_title", language),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = startDateText,
                        onValueChange = {
                            startDateText = it
                            invalidDateError = false
                        },
                        isError = invalidDateError,
                        label = { Text("YYYY-MM-DD") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("ram_confirmed_start_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(AppLocalization.get("ram_month_length", language), style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(29, 30).forEach { days ->
                            FilterChip(
                                selected = monthLen == days,
                                onClick = { monthLen = days },
                                label = { Text("$days") },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("ram_month_len_$days")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                if (parseDateKeyToCalendar(startDateText) == null) {
                                    invalidDateError = true
                                } else {
                                    RamadanRepository.setConfirmedRamadanStart(context, startDateText.trim(), monthLen)
                                }
                            },
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp).testTag("ram_apply_confirmed_start_btn")
                        ) {
                            Text(AppLocalization.get("ram_set_confirmed_start", language))
                        }
                        OutlinedButton(
                            onClick = {
                                RamadanRepository.setConfirmedRamadanStart(context, null, 30)
                            },
                            modifier = Modifier.heightIn(min = 48.dp).testTag("ram_clear_confirmed_start_btn")
                        ) {
                            Text(AppLocalization.get("ram_clear_confirmed_start", language))
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("prayer_times", language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedButton(
                        onClick = onOpenPrayerTimes,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) {
                        Text(AppLocalization.get("ram_prayer_city_method_btn", language))
                    }
                }
            }
        }
    }
}
