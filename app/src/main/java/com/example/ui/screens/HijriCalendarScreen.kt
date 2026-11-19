package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.location.AppLocationManager
import com.example.data.model.AppLanguage
import com.example.data.repository.HijriCalendarConvention
import com.example.data.repository.IslamicCalendarRepository
import com.example.data.repository.IslamicCalendarEvent
import com.example.data.repository.PrayerCalculator
import com.example.localization.AppLocalization
import com.example.ui.components.MethodologyReferences
import com.example.ui.components.NoorTopBar
import com.example.ui.components.SourcesAndMethodologyCard
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HijriCalendarScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Initialize repository
    LaunchedEffect(Unit) {
        IslamicCalendarRepository.init(context)
        PrayerCalculator.init(context)
    }

    val userLocation by AppLocationManager.currentLocation.collectAsState()
    val manualAdjustment by IslamicCalendarRepository.manualAdjustmentDays.collectAsState()
    val convention by IslamicCalendarRepository.calendarConvention.collectAsState()

    var activeTab by rememberSaveable { mutableStateOf("calendar") } // "calendar", "events", "ramadan"

    // Calendar Navigation State synchronized to selected city's timezone
    val locationTimeZone = remember(userLocation.timeZoneId) {
        TimeZone.getTimeZone(userLocation.timeZoneId)
    }
    val todayCal = remember(userLocation.timeZoneId) {
        Calendar.getInstance(locationTimeZone)
    }
    var displayedYear by remember { mutableIntStateOf(todayCal.get(Calendar.YEAR)) }
    var displayedMonth by remember { mutableIntStateOf(todayCal.get(Calendar.MONTH)) } // 0-11

    LaunchedEffect(userLocation.timeZoneId) {
        displayedYear = todayCal.get(Calendar.YEAR)
        displayedMonth = todayCal.get(Calendar.MONTH)
    }

    val currentDisplayedCal = remember(displayedYear, displayedMonth, userLocation.timeZoneId) {
        Calendar.getInstance(locationTimeZone).apply {
            set(Calendar.YEAR, displayedYear)
            set(Calendar.MONTH, displayedMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val currentTodayHijri = remember(todayCal, manualAdjustment, convention) {
        IslamicCalendarRepository.getHijriDate(todayCal, manualAdjustment, convention)
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("calendar", language),
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
        ) {
            // Tab Selector: Dual Calendar / Islamic Events / Ramadan Schedule
            TabRow(
                selectedTabIndex = when (activeTab) {
                    "events" -> 1
                    "ramadan" -> 2
                    else -> 0
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = activeTab == "calendar",
                    onClick = { activeTab = "calendar" },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "ہجری و شمسی"
                                AppLanguage.ARABIC -> "التقويم المزدوج"
                                AppLanguage.HINDI -> "द्वि-कैलेंडर"
                                AppLanguage.ENGLISH -> "Dual Calendar"
                            },
                            fontWeight = if (activeTab == "calendar") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("hijri_tab_calendar")
                )
                Tab(
                    selected = activeTab == "events",
                    onClick = { activeTab = "events" },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "اسلامی ایام"
                                AppLanguage.ARABIC -> "المناسبات الإسلامية"
                                AppLanguage.HINDI -> "इस्लामी दिवस"
                                AppLanguage.ENGLISH -> "Islamic Events"
                            },
                            fontWeight = if (activeTab == "events") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("hijri_tab_events")
                )
                Tab(
                    selected = activeTab == "ramadan",
                    onClick = { activeTab = "ramadan" },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "رمضان اوقات"
                                AppLanguage.ARABIC -> "إمساكية رمضان"
                                AppLanguage.HINDI -> "रमज़ान सारणी"
                                AppLanguage.ENGLISH -> "Ramadan Schedule"
                            },
                            fontWeight = if (activeTab == "ramadan") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = { Icon(Icons.Default.NightsStay, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("hijri_tab_ramadan")
                )
            }

            when (activeTab) {
                "calendar" -> {
                    CalendarView(
                        userLocation = userLocation,
                        language = language,
                        todayCal = todayCal,
                        currentDisplayedCal = currentDisplayedCal,
                        currentTodayHijri = currentTodayHijri,
                        displayedYear = displayedYear,
                        displayedMonth = displayedMonth,
                        manualAdjustment = manualAdjustment,
                        convention = convention,
                        onPrevMonth = {
                            if (displayedMonth == 0) {
                                displayedMonth = 11
                                displayedYear--
                            } else {
                                displayedMonth--
                            }
                        },
                        onNextMonth = {
                            if (displayedMonth == 11) {
                                displayedMonth = 0
                                displayedYear++
                            } else {
                                displayedMonth++
                            }
                        },
                        onResetToToday = {
                            displayedYear = todayCal.get(Calendar.YEAR)
                            displayedMonth = todayCal.get(Calendar.MONTH)
                        },
                        onAdjustmentChange = { newOffset ->
                            IslamicCalendarRepository.setManualAdjustment(context, newOffset)
                        },
                        onConventionSelect = { selectedConv ->
                            IslamicCalendarRepository.setCalendarConvention(context, selectedConv)
                        }
                    )
                }
                "events" -> {
                    IslamicEventsView(language = language)
                }
                "ramadan" -> {
                    RamadanScheduleView(
                        userLocation = userLocation,
                        language = language
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarView(
    userLocation: com.example.data.location.UserLocation,
    language: AppLanguage,
    todayCal: Calendar,
    currentDisplayedCal: Calendar,
    currentTodayHijri: com.example.data.repository.HijriDate,
    displayedYear: Int,
    displayedMonth: Int,
    manualAdjustment: Int,
    convention: HijriCalendarConvention,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onResetToToday: () -> Unit,
    onAdjustmentChange: (Int) -> Unit,
    onConventionSelect: (HijriCalendarConvention) -> Unit
) {
    val displayLocale = when (language) {
        AppLanguage.ARABIC -> Locale("ar")
        AppLanguage.URDU -> Locale("ur")
        AppLanguage.HINDI -> Locale("hi")
        AppLanguage.ENGLISH -> Locale.US
    }
    val displayedMonthHeader = remember(displayedYear, displayedMonth, language, userLocation.timeZoneId) {
        val fmt = SimpleDateFormat("LLLL yyyy", displayLocale).apply {
            timeZone = TimeZone.getTimeZone(userLocation.timeZoneId)
        }
        fmt.format(currentDisplayedCal.time)
    }

    // Calculate days for the grid
    val daysInMonth = currentDisplayedCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = currentDisplayedCal.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday...
    val leadingEmptySpaces = (firstDayOfWeek - 1) % 7

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("hijri_calendar_list")
    ) {
        // Today's Dual Date Hero Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hijri_hero_card")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (userLocation.isGpsFix) Icons.Default.GpsFixed else Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val cityDisplay = AppLocationManager.localizedCityDisplay(userLocation, language)
                            Text(
                                text = "$cityDisplay${if (userLocation.countryName.isNotBlank()) ", " + userLocation.countryName else ""} • ${userLocation.timeZoneId}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Text(
                        text = AppLocalization.get("hijri_hero_title", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentTodayHijri.formatted(language),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    if (language != AppLanguage.ARABIC) {
                        Text(
                            text = "${currentTodayHijri.day} ${currentTodayHijri.monthNameAr} ${currentTodayHijri.year} هـ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    val gregPrefix = when (language) {
                        AppLanguage.URDU -> "عیسوی"
                        AppLanguage.ARABIC -> "الميلادي"
                        AppLanguage.HINDI -> "अंग्रेज़ी तिथि"
                        AppLanguage.ENGLISH -> "Gregorian"
                    }
                    val displayLocale = when (language) {
                        AppLanguage.ARABIC -> Locale("ar")
                        AppLanguage.URDU -> Locale("ur")
                        AppLanguage.HINDI -> Locale("hi")
                        AppLanguage.ENGLISH -> Locale.US
                    }
                    Text(
                        text = "$gregPrefix: ${todayCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, displayLocale) ?: ""}, %02d %s %d".format(
                            todayCal.get(Calendar.DAY_OF_MONTH),
                            todayCal.getDisplayName(Calendar.MONTH, Calendar.LONG, displayLocale) ?: "",
                            todayCal.get(Calendar.YEAR)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val srcLabel = when (language) {
                        AppLanguage.URDU -> "ماخذ"
                        AppLanguage.ARABIC -> "المصدر"
                        AppLanguage.HINDI -> "स्रोत"
                        AppLanguage.ENGLISH -> "Source"
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "$srcLabel: ${currentTodayHijri.sourceDescription}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Ruet-e-Hilal vs Astronomical Mode & Manual Adjustment
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("hijri_convention_title", language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val conventions = listOf(
                        HijriCalendarConvention.PAKISTAN_RUET to Pair("hijri_conv_pakistan", "hijri_conv_pakistan_desc"),
                        HijriCalendarConvention.UMM_AL_QURA to Pair("hijri_conv_umm_al_qura", "hijri_conv_umm_al_qura_desc"),
                        HijriCalendarConvention.ASTRONOMICAL_TABULAR to Pair("hijri_conv_tabular", "hijri_conv_tabular_desc"),
                        HijriCalendarConvention.MANUAL_LOCAL_SIGHTING to Pair("hijri_conv_manual", "hijri_conv_manual_desc")
                    )
                    conventions.forEach { (conv, keys) ->
                        val isSelected = (convention == conv)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onConventionSelect(conv) }
                                .padding(vertical = 4.dp)
                                .testTag("hijri_conv_${conv.id}")
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onConventionSelect(conv) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = AppLocalization.get(keys.first, language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = AppLocalization.get(keys.second, language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "رویتِ ہلال میں دستی تبدیلی:"
                                AppLanguage.ARABIC -> "التعديل اليدوي للهلال:"
                                AppLanguage.HINDI -> "चाँद देखने का मैन्युअल समायोजन:"
                                AppLanguage.ENGLISH -> "Local Sighting Adjustment:"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (manualAdjustment == 0) {
                                when (language) {
                                    AppLanguage.URDU -> "کوئی تبدیلی نہیں"
                                    AppLanguage.ARABIC -> "بدون تعديل"
                                    AppLanguage.HINDI -> "कोई बदलाव नहीं"
                                    AppLanguage.ENGLISH -> "No manual adjustment"
                                }
                            } else {
                                val adj = if (manualAdjustment > 0) "+$manualAdjustment" else "$manualAdjustment"
                                when (language) {
                                    AppLanguage.URDU -> "$adj دن"
                                    AppLanguage.ARABIC -> "$adj يوم"
                                    AppLanguage.HINDI -> "$adj दिन"
                                    AppLanguage.ENGLISH -> "$adj day(s)"
                                }
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onAdjustmentChange((manualAdjustment - 1).coerceAtLeast(-2)) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("moon_adjust_minus")
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null)
                        }

                        Text(
                            text = "${if (manualAdjustment > 0) "+$manualAdjustment" else "$manualAdjustment"}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { onAdjustmentChange((manualAdjustment + 1).coerceAtMost(2)) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("moon_adjust_plus")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }
        }

        // Dual Monthly Calendar Grid Header & Nav
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = displayedMonthHeader,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            TextButton(
                                onClick = onResetToToday,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.testTag("hijri_today_button")
                            ) {
                                Text(AppLocalization.get("hijri_today_btn", language), style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        Row {
                            IconButton(onClick = onPrevMonth) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = AppLocalization.get("back_cd", language))
                            }
                            IconButton(
                                onClick = onNextMonth,
                                modifier = Modifier.testTag("hijri_next_month")
                            ) {
                                Icon(Icons.Default.ChevronRight, contentDescription = AppLocalization.get("next", language))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Day of Week Header
                    val dayHeaders = when (language) {
                        AppLanguage.URDU -> listOf("اتوار", "پیر", "منگل", "بدھ", "جمعرات", "جمعہ", "ہفتہ")
                        AppLanguage.ARABIC -> listOf("أحد", "إثنين", "ثلاثاء", "أربعاء", "خميس", "جمعة", "سبت")
                        AppLanguage.HINDI -> listOf("रवि", "सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि")
                        AppLanguage.ENGLISH -> listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        dayHeaders.forEachIndexed { idx, dayLabel ->
                            Text(
                                text = dayLabel,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (idx == 5) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                    // Grid Cells
                    val totalCells = leadingEmptySpaces + daysInMonth
                    val rows = (totalCells + 6) / 7

                    for (r in 0 until rows) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            for (c in 0 until 7) {
                                val cellIndex = r * 7 + c
                                val dayNum = cellIndex - leadingEmptySpaces + 1

                                if (dayNum in 1..daysInMonth) {
                                    val cellCal = Calendar.getInstance(TimeZone.getTimeZone(userLocation.timeZoneId)).apply {
                                        set(Calendar.YEAR, displayedYear)
                                        set(Calendar.MONTH, displayedMonth)
                                        set(Calendar.DAY_OF_MONTH, dayNum)
                                    }
                                    val cellHijri = IslamicCalendarRepository.getHijriDate(cellCal, manualAdjustment, convention)
                                    val isToday = (todayCal.get(Calendar.YEAR) == displayedYear &&
                                            todayCal.get(Calendar.MONTH) == displayedMonth &&
                                            todayCal.get(Calendar.DAY_OF_MONTH) == dayNum)

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isToday) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                            )
                                            .border(
                                                width = if (isToday) 1.5.dp else 0.5.dp,
                                                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "$dayNum",
                                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 12.sp,
                                                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "${cellHijri.day}",
                                                fontSize = 9.sp,
                                                color = Color(0xFFC88A00),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            SourcesAndMethodologyCard(
                language = language,
                references = MethodologyReferences.HIJRI_CALENDAR_REFS
            )
        }
    }
}

@Composable
fun IslamicEventsView(language: AppLanguage) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("hijri_events_list")
    ) {
        item {
            Text(
                text = AppLocalization.get("hijri_events_title", language),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = AppLocalization.get("hijri_events_disclaimer", language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(IslamicCalendarRepository.ISLAMIC_EVENTS, key = { it.id }) { event ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hijri_event_${event.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${event.hijriDay} ${event.localizedMonthName(language)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (event.isMoonSightingDependent) Color(0xFFFFF3CD) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = if (event.isMoonSightingDependent) {
                                    when (language) {
                                        AppLanguage.URDU -> "🌙 رویتِ ہلال پر موقوف"
                                        AppLanguage.ARABIC -> "🌙 يعتمد على رؤية الهلال"
                                        AppLanguage.HINDI -> "🌙 चाँद देखने पर आधारित"
                                        AppLanguage.ENGLISH -> "🌙 Moon Sighting Dependent"
                                    }
                                } else {
                                    when (language) {
                                        AppLanguage.URDU -> "✓ متعین تاریخ"
                                        AppLanguage.ARABIC -> "✓ تاريخ ثابت"
                                        AppLanguage.HINDI -> "✓ निर्धारित तिथि"
                                        AppLanguage.ENGLISH -> "✓ Established Date"
                                    }
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = if (event.isMoonSightingDependent) Color(0xFF856404) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = event.localizedTitle(language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = event.localizedDescription(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun RamadanScheduleView(
    userLocation: com.example.data.location.UserLocation,
    language: AppLanguage
) {
    val context = LocalContext.current
    val calcConfig by PrayerCalculator.calcConfig.collectAsState()
    val convention by IslamicCalendarRepository.calendarConvention.collectAsState()
    val manualOffset by IslamicCalendarRepository.manualAdjustmentDays.collectAsState()

    val ramadanStart = remember(convention, manualOffset, userLocation.timeZoneId) {
        IslamicCalendarRepository.findProjectedRamadanStart(
            fromCal = Calendar.getInstance(TimeZone.getTimeZone(userLocation.timeZoneId)),
            convention = convention,
            manualOffsetDays = manualOffset
        )
    }

    val schedule = remember(userLocation, calcConfig, ramadanStart) {
        IslamicCalendarRepository.generateRamadanSchedule(
            latitude = userLocation.latitude,
            longitude = userLocation.longitude,
            timeZoneId = userLocation.timeZoneId,
            gregorianStartCal = ramadanStart,
            config = calcConfig
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("hijri_ramadan_list")
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = AppLocalization.get("hijri_ramadan_title", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val ramadanCity = AppLocationManager.localizedCityDisplay(userLocation, language)
                    Text(
                        text = "$ramadanCity (${userLocation.countryName}) • ${AppLocalization.get("hijri_ramadan_note", language)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Text(AppLocalization.get("hijri_day_col", language), modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(AppLocalization.get("hijri_date_col", language), modifier = Modifier.weight(1.3f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(AppLocalization.get("hijri_sehri_col", language), modifier = Modifier.weight(1.3f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(AppLocalization.get("hijri_iftar_col", language), modifier = Modifier.weight(1.3f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        items(schedule) { day ->
            val dayLabel = when (language) {
                AppLanguage.URDU -> "دن ${day.ramadanDay}"
                AppLanguage.ARABIC -> "يوم ${day.ramadanDay}"
                AppLanguage.HINDI -> "दिन ${day.ramadanDay}"
                AppLanguage.ENGLISH -> "Day ${day.ramadanDay}"
            }
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                ) {
                    Text(dayLabel, modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("${day.dayOfWeek}, ${day.gregorianDateStr}", modifier = Modifier.weight(1.3f), fontSize = 12.sp)
                    Text(day.sehriTime, modifier = Modifier.weight(1.3f), fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Text(day.iftarTime, modifier = Modifier.weight(1.3f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFC88A00))
                }
            }
        }

        item {
            Button(
                onClick = {
                    val shareText = buildString {
                        append("Ramadan Timetable - ${userLocation.cityName}\n")
                        append("Day | Date | Sehri | Iftar\n")
                        schedule.forEach { d ->
                            append("Day ${d.ramadanDay} | ${d.gregorianDateStr} | ${d.sehriTime} | ${d.iftarTime}\n")
                        }
                        append("\nNoor-e-Hidayat")
                    }
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    runCatching {
                        context.startActivity(Intent.createChooser(intent, AppLocalization.get("hijri_share_ramadan", language)))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ramadan_share_button")
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppLocalization.get("hijri_share_ramadan", language))
            }
        }
    }
}
