package com.example.ui.screens.salah

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppLanguage
import com.example.data.salah.FiqhGuidancePerspective
import com.example.data.salah.MissedReasonCategory
import com.example.data.salah.MissedSalahEntryEntity
import com.example.data.salah.MissedSalahRepository
import com.example.data.salah.QadaEntryStatus
import com.example.data.salah.SalahPrayerType
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MissedSalahScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    val state by MissedSalahRepository.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        MissedSalahRepository.init(context)
    }

    var selectedDateFilter by remember { mutableStateOf<String?>(null) }
    var showAddSheet by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<MissedSalahEntryEntity?>(null) }
    var showEstimateSheet by remember { mutableStateOf(false) }
    var showPlannerSheet by remember { mutableStateOf(false) }
    var showDeleteAllConfirm by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    val visibleEntries = remember(state.entries, selectedDateFilter) {
        if (selectedDateFilter == null) state.entries
        else state.entries.filter {
            it.missedDateKey == selectedDateFilter || it.completedDateKey == selectedDateFilter
        }
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("missed_salah_tracker", language),
                language = language,
                onBackClick = onBack
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddSheet = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = {
                    Text(
                        when (language) {
                            AppLanguage.URDU -> "نماز درج کریں"
                            AppLanguage.ARABIC -> "تسجيل صلاة"
                            AppLanguage.HINDI -> "नमाज़ जोड़ें"
                            AppLanguage.ENGLISH -> "Log Salah"
                        }
                    )
                },
                modifier = Modifier.testTag("salah_add_btn")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("missed_salah_screen_list")
        ) {
            if (statusMessage != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = statusMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { statusMessage = null }) {
                                Text(AppLocalization.get("qibla_close", language))
                            }
                        }
                    }
                }
            }

            // Dashboard Summary Cards
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "ذاتی قضا ڈیش بورڈ"
                                AppLanguage.ARABIC -> "لوحة متابعة الصلوات"
                                AppLanguage.HINDI -> "व्यक्तिगत क़ज़ा डैशबोर्ड"
                                AppLanguage.ENGLISH -> "Personal Salah Dashboard"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MetricTile(
                                label = when (language) {
                                    AppLanguage.URDU -> "کل درج شدہ"
                                    AppLanguage.ARABIC -> "المسجلة"
                                    AppLanguage.HINDI -> "कुल दर्ज"
                                    AppLanguage.ENGLISH -> "Recorded"
                                },
                                value = state.totalConfirmedMissed.toString(),
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f).testTag("salah_dashboard_total")
                            )
                            MetricTile(
                                label = when (language) {
                                    AppLanguage.URDU -> "باقی قضا"
                                    AppLanguage.ARABIC -> "متبقي قضاء"
                                    AppLanguage.HINDI -> "बकाया क़ज़ा"
                                    AppLanguage.ENGLISH -> "Pending Qada"
                                },
                                value = state.pendingQadaCount.toString(),
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.weight(1f).testTag("salah_dashboard_pending")
                            )
                            MetricTile(
                                label = when (language) {
                                    AppLanguage.URDU -> "مکمل شدہ"
                                    AppLanguage.ARABIC -> "المقضية"
                                    AppLanguage.HINDI -> "पूर्ण"
                                    AppLanguage.ENGLISH -> "Completed"
                                },
                                value = state.completedConfirmedCount.toString(),
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.weight(1f).testTag("salah_dashboard_completed")
                            )
                        }

                        if (state.deliberateSeparatePendingCount > 0 || state.exemptCount > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "الگ شمار: عمداً ترک (${state.deliberateSeparatePendingCount}) • شرعی معاف (${state.exemptCount})"
                                    AppLanguage.ARABIC -> "منفصل: المتروكة عمداً (${state.deliberateSeparatePendingCount}) • المعذور عنها (${state.exemptCount})"
                                    AppLanguage.HINDI -> "अलग गिनती: जानबूझकर (${state.deliberateSeparatePendingCount}) • मुक्त (${state.exemptCount})"
                                    AppLanguage.ENGLISH -> "Separate: Deliberate logs (${state.deliberateSeparatePendingCount}) • Fiqh-Exempt (${state.exemptCount})"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { state.confirmedProgressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .testTag("salah_progress_bar")
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val pct = (state.confirmedProgressFraction * 100f).toInt()
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "تکمیل کی پیش رفت: $pct% (آج مکمل: ${state.completedTodayCount} • اس ہفتے: ${state.completedThisWeekCount})"
                                AppLanguage.ARABIC -> "نسبة الإنجاز: $pct% (اليوم: ${state.completedTodayCount} • هذا الأسبوع: ${state.completedThisWeekCount})"
                                AppLanguage.HINDI -> "पूर्णता प्रगति: $pct% (आज: ${state.completedTodayCount} • इस सप्ताह: ${state.completedThisWeekCount})"
                                AppLanguage.ENGLISH -> "Completion Progress: $pct% (Completed Today: ${state.completedTodayCount} • This Week: ${state.completedThisWeekCount})"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag("salah_daily_weekly_card")
                        )
                    }
                }
            }

            // Prayer-Wise Breakdown Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("salah_breakdown_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "ہر نماز کی الگ تفصیل"
                                AppLanguage.ARABIC -> "التفصيل حسب كل صلاة"
                                AppLanguage.HINDI -> "प्रत्येक नमाज़ का विवरण"
                                AppLanguage.ENGLISH -> "Prayer-Wise Breakdown"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        val breakdown = state.prayerBreakdown
                        SalahPrayerType.entries.forEach { prayer ->
                            val counts = breakdown[prayer]
                            if (counts != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = prayer.label(language),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = when (language) {
                                            AppLanguage.URDU -> "باقی: ${counts.pendingQada}  |  مکمل: ${counts.completed}"
                                            AppLanguage.ARABIC -> "متبقي: ${counts.pendingQada}  |  مقضية: ${counts.completed}"
                                            AppLanguage.HINDI -> "बकाया: ${counts.pendingQada}  |  पूर्ण: ${counts.completed}"
                                            AppLanguage.ENGLISH -> "Pending: ${counts.pendingQada}  |  Done: ${counts.completed}"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            }
                        }
                    }
                }
            }

            // Simple Calendar View
            item {
                SalahCalendarCard(
                    entries = state.entries,
                    selectedDateKey = selectedDateFilter,
                    onSelectDateKey = { selectedDateFilter = it },
                    language = language
                )
            }

            // Optional Historical Backlog Estimate Card (Kept strictly separate from confirmed logs)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("salah_estimate_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = when (language) {
                                        AppLanguage.URDU -> "پرانے بقایا کا اختیاری تخمینہ"
                                        AppLanguage.ARABIC -> "التقدير الاختياري للفوائت السابقة"
                                        AppLanguage.HINDI -> "पिछली बकाया का वैकल्पिक अनुमान"
                                        AppLanguage.ENGLISH -> "Optional Historical Backlog Estimate"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = when (language) {
                                        AppLanguage.URDU -> "ذاتی تخمینہ — حتمی شرعی یا تاریخی عدد نہیں (اوپر والے ریکارڈ سے الگ)"
                                        AppLanguage.ARABIC -> "تقدير شخصي غير ملزم (منفصل عن السجلات المؤكدة أعلاه)"
                                        AppLanguage.HINDI -> "व्यक्तिगत अनुमान — पक्का धार्मिक तथ्य नहीं (ऊपर के रिकॉर्ड से अलग)"
                                        AppLanguage.ENGLISH -> "Personal Estimate — Not an exact religious or historical fact (Kept separate above)"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            TextButton(
                                onClick = { showEstimateSheet = true },
                                modifier = Modifier.minimumInteractiveComponentSize().testTag("salah_open_estimate_btn")
                            ) {
                                Text(
                                    when (language) {
                                        AppLanguage.URDU -> "ترمیم"
                                        AppLanguage.ARABIC -> "تعديل"
                                        AppLanguage.HINDI -> "संपादित करें"
                                        AppLanguage.ENGLISH -> "Configure"
                                    }
                                )
                            }
                        }

                        if (state.estimate.enabled) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "تخمینی باقی: ${state.estimate.remainingEstimated} / ${state.estimate.totalEstimated} (مکمل: ${state.estimate.totalCompletedAgainstEstimate})"
                                    AppLanguage.ARABIC -> "المتبقي التقديري: ${state.estimate.remainingEstimated} / ${state.estimate.totalEstimated} (المقضي: ${state.estimate.totalCompletedAgainstEstimate})"
                                    AppLanguage.HINDI -> "अनुमानित बकाया: ${state.estimate.remainingEstimated} / ${state.estimate.totalEstimated} (पूर्ण: ${state.estimate.totalCompletedAgainstEstimate})"
                                    AppLanguage.ENGLISH -> "Estimated Remaining: ${state.estimate.remainingEstimated} / ${state.estimate.totalEstimated} (Completed: ${state.estimate.totalCompletedAgainstEstimate})"
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            SalahPrayerType.entries.forEach { prayer ->
                                val est = state.estimate.estimatedByPrayer[prayer] ?: 0
                                val done = state.estimate.completedByPrayer[prayer] ?: 0
                                if (est > 0) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${prayer.label(language)}: $done / $est",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        OutlinedButton(
                                            onClick = {
                                                MissedSalahRepository.logCompletedAgainstEstimate(context, prayer, +1)
                                            },
                                            enabled = done < est,
                                            modifier = Modifier.height(48.dp)
                                                .testTag("salah_est_plus_btn_${prayer.id}")
                                        ) {
                                            Text("+1")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Optional Qada Progress Planner Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("salah_planner_card")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "اختیاری قضا شیڈول پلانر"
                                    AppLanguage.ARABIC -> "مخطط القضاء اليومي الاختياري"
                                    AppLanguage.HINDI -> "वैकल्पिक क़ज़ा प्रगति योजनाकार"
                                    AppLanguage.ENGLISH -> "Optional Qada Progress Planner"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            val planSummary = if (!state.planner.isActive) {
                                when (language) {
                                    AppLanguage.URDU -> "غیر فعال — اپنی رفتار سے روزانہ ہدف سیٹ کریں"
                                    AppLanguage.ARABIC -> "غير مفعل — يمكنك تحديد وتيرة مريحة متى شئت"
                                    AppLanguage.HINDI -> "निष्क्रिय — अपनी सुविधानुसार दैनिक लक्ष्य चुनें"
                                    AppLanguage.ENGLISH -> "Inactive — Set a gentle daily pace anytime"
                                }
                            } else if (state.planner.isPaused) {
                                when (language) {
                                    AppLanguage.URDU -> "عارضی طور پر رکا ہوا (ہدف: ${state.planner.dailyTarget}/روز)"
                                    AppLanguage.ARABIC -> "متوقف مؤقتاً (الهدف: ${state.planner.dailyTarget}/يوم)"
                                    AppLanguage.HINDI -> "अस्थायी रूप से रुका हुआ (लक्ष्य: ${state.planner.dailyTarget}/दिन)"
                                    AppLanguage.ENGLISH -> "Paused gently (${state.planner.dailyTarget}/day target)"
                                }
                            } else {
                                val rem = state.pendingQadaCount
                                val days = if (rem > 0) (rem + state.planner.dailyTarget - 1) / state.planner.dailyTarget else 0
                                when (language) {
                                    AppLanguage.URDU -> "فعال ہدف: ${state.planner.dailyTarget}/روز • تقریباً $days دن ($rem باقی)"
                                    AppLanguage.ARABIC -> "الهدف النشط: ${state.planner.dailyTarget}/يوم • نحو $days يوماً ($rem متبقي)"
                                    AppLanguage.HINDI -> "सक्रिय लक्ष्य: ${state.planner.dailyTarget}/दिन • लगभग $days दिन ($rem बकाया)"
                                    AppLanguage.ENGLISH -> "Target: ${state.planner.dailyTarget}/day • ~$days days for $rem pending"
                                }
                            }
                            Text(
                                text = planSummary,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(
                            onClick = { showPlannerSheet = true },
                            modifier = Modifier.minimumInteractiveComponentSize().testTag("salah_open_planner_btn")
                        ) {
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "ترتیب دیں"
                                    AppLanguage.ARABIC -> "إعداد"
                                    AppLanguage.HINDI -> "सेट करें"
                                    AppLanguage.ENGLISH -> "Plan"
                                }
                            )
                        }
                    }
                }
            }

            // Recorded Entries List Header
            item {
                Text(
                    text = if (selectedDateFilter == null) {
                        when (language) {
                            AppLanguage.URDU -> "درج شدہ نمازوں کی فہرست (${visibleEntries.size})"
                            AppLanguage.ARABIC -> "سجل الصلوات المحفوظة (${visibleEntries.size})"
                            AppLanguage.HINDI -> "दर्ज नमाज़ों की सूची (${visibleEntries.size})"
                            AppLanguage.ENGLISH -> "Recorded Entries (${visibleEntries.size})"
                        }
                    } else {
                        when (language) {
                            AppLanguage.URDU -> "منتخب تاریخ $selectedDateFilter (${visibleEntries.size})"
                            AppLanguage.ARABIC -> "تاريخ $selectedDateFilter (${visibleEntries.size})"
                            AppLanguage.HINDI -> "चयनित तिथि $selectedDateFilter (${visibleEntries.size})"
                            AppLanguage.ENGLISH -> "Entries on $selectedDateFilter (${visibleEntries.size})"
                        }
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (visibleEntries.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth().testTag("salah_empty_entries_card")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(20.dp)
                        ) {
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "ابھی کوئی نماز درج نہیں کی گئی۔"
                                    AppLanguage.ARABIC -> "لا توجد صلوات مسجلة بعد."
                                    AppLanguage.HINDI -> "अभी तक कोई नमाज़ दर्ज नहीं की गई है।"
                                    AppLanguage.ENGLISH -> "No missed prayers recorded yet."
                                },
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "نیا اندراج شامل کرنے کے لیے نیچے '+ نماز درج کریں' بٹن استعمال کریں۔"
                                    AppLanguage.ARABIC -> "اضغط على زر التسجيل أدناه لإضافة سجل خاص جديد."
                                    AppLanguage.HINDI -> "अपनी पहली प्रविष्टि जोड़ने के लिए नीचे '+' बटन दबाएँ।"
                                    AppLanguage.ENGLISH -> "Tap '+ Log Salah' below to record entries privately on this device."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(visibleEntries, key = { it.id }) { entry ->
                    SalahEntryItemCard(
                        entry = entry,
                        language = language,
                        onToggleCompleted = {
                            scope.launch {
                                MissedSalahRepository.toggleCompleted(context, entry)
                            }
                        },
                        onEdit = { editingEntry = entry }
                    )
                }
            }

            // Islamic Scholarly Guidance & Verified Sources Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("salah_guidance_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "علمی رہنمائی اور مستند حوالہ جات"
                                    AppLanguage.ARABIC -> "التوجيه الشرعي والمصادر الموثوقة"
                                    AppLanguage.HINDI -> "शरई मार्गदर्शन व प्रामाणिक संदर्भ"
                                    AppLanguage.ENGLISH -> "Islamic Guidance & Verified References"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "یہ ٹریکر خود مختار فتویٰ جاری نہیں کرتا۔ بھول/نیند اور عمداً ترک میں فقہاء کا فرق اور علمی آراء نیچے واضح طور پر دی گئی ہیں؛ ذاتی مسئلے کے لیے مستند عالمِ دین سے مشورہ کریں۔"
                                AppLanguage.ARABIC -> "لا يصدر هذا التطبيق فتاوى مستقلة. يوضح الخلاف الفقهي المعتبر بين النسيان/النوم والترك العمدي، ويُرجع في الأحكام الخاصة إلى عالم موثوق."
                                AppLanguage.HINDI -> "यह ऐप स्वतंत्र फ़तवा जारी नहीं करता। भूल/नींद और जानबूझकर छोड़ने में विद्वानों का मतभेद नीचे दिया गया है; व्यक्तिगत निर्णय के लिए योग्य आलिम से सलाह लें।"
                                AppLanguage.ENGLISH -> "This app does not issue independent fatwas. Select how your dashboard counts deliberate logs below, and consult a qualified scholar for personal rulings."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FiqhGuidancePerspective.entries.forEach { mode ->
                                FilterChip(
                                    selected = state.guidance == mode,
                                    onClick = { MissedSalahRepository.setGuidancePerspective(context, mode) },
                                    label = { Text(mode.title(language)) },
                                    colors = noorSelectableChipColors(),
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                        .testTag("salah_guidance_chip_${mode.id}")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = state.guidance.summary(language),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        MissedSalahRepository.VERIFIED_REFERENCES.forEach { ref ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        runCatching { uriHandler.openUri(ref.url) }
                                    }
                                    .testTag("salah_ref_${ref.id}")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.MenuBook,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = ref.citationTitle,
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.OpenInNew,
                                            contentDescription = "Open Reference Link",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = ref.arabicText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = ref.translations[language] ?: ref.translations[AppLanguage.ENGLISH].orEmpty(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = ref.scopeNote[language] ?: ref.scopeNote[AppLanguage.ENGLISH].orEmpty(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Privacy & Local Backup Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("salah_privacy_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "رازداری اور مقامی بیک اپ"
                                    AppLanguage.ARABIC -> "الخصوصية والنسخ الاحتياطي المحلي"
                                    AppLanguage.HINDI -> "गोपनीयता और स्थानीय बैकअप"
                                    AppLanguage.ENGLISH -> "Privacy & Local JSON Backup"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "آپ کا تمام ریکارڈ 100% نجی ہے اور صرف اسی ڈیوائس پر محفوظ رہتا ہے۔ کوئی ڈیٹا کسی سرور پر اپ لوڈ نہیں ہوتا۔ بیک اپ فائل آپ کے ایپ کے نجی فولڈر (app/filesDir/missed_salah_backups) میں محفوظ ہوتی ہے۔"
                                AppLanguage.ARABIC -> "جميع سجلاتك خاصة ١٠٠٪ ومحفوظة محلياً فقط على جهازك دون رفع لأي خادم. تُحفظ ملفات النسخ الاحتياطي محلياً في مجلد التطبيق الخاص (filesDir/missed_salah_backups)."
                                AppLanguage.HINDI -> "आपका सारा डेटा 100% निजी है और केवल आपके डिवाइस पर सुरक्षित रहता है। कोई डेटा किसी सर्वर पर नहीं भेजा जाता। बैकअप फ़ाइल ऐप के निजी फ़ोल्डर (filesDir/missed_salah_backups) में सहेजी जाती है।"
                                AppLanguage.ENGLISH -> "100% Private Local Storage — Your records stay strictly on this device and are never uploaded to any server. Optional JSON backups are saved in your app's private storage folder (filesDir/missed_salah_backups)."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (!state.lastBackupFilePath.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Latest backup: ${File(state.lastBackupFilePath!!).name}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        val res = MissedSalahRepository.exportLocalBackupJson(context)
                                        statusMessage = res.fold(
                                            onSuccess = { "Backup saved locally: ${it.name}" },
                                            onFailure = { "Backup failed: ${it.message}" }
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f).height(48.dp).testTag("salah_export_backup_btn")
                            ) {
                                Icon(Icons.Default.Backup, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    when (language) {
                                        AppLanguage.URDU -> "بیک اپ بنائیں"
                                        AppLanguage.ARABIC -> "نسخ احتياطي"
                                        AppLanguage.HINDI -> "बैकअप"
                                        AppLanguage.ENGLISH -> "Export Backup"
                                    }
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    val path = state.lastBackupFilePath
                                    if (path != null) {
                                        scope.launch {
                                            val res = MissedSalahRepository.restoreFromBackupFile(context, File(path))
                                            statusMessage = res.fold(
                                                onSuccess = { count -> "Restored $count entries from local backup." },
                                                onFailure = { err -> "Restore failed: ${err.message}" }
                                            )
                                        }
                                    } else {
                                        statusMessage = when (language) {
                                            AppLanguage.URDU -> "ابھی کوئی مقامی بیک اپ موجود نہیں۔"
                                            AppLanguage.ARABIC -> "لا يوجد ملف نسخ احتياطي محلي بعد."
                                            AppLanguage.HINDI -> "अभी कोई स्थानीय बैकअप मौजूद नहीं है।"
                                            AppLanguage.ENGLISH -> "No local backup file found yet."
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(48.dp).testTag("salah_restore_backup_btn")
                            ) {
                                Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    when (language) {
                                        AppLanguage.URDU -> "بحال کریں"
                                        AppLanguage.ARABIC -> "استعادة"
                                        AppLanguage.HINDI -> "पुनर्स्थापित"
                                        AppLanguage.ENGLISH -> "Restore"
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showDeleteAllConfirm = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("salah_delete_all_btn")
                        ) {
                            Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "تمام ذاتی ڈیٹا حذف کریں"
                                    AppLanguage.ARABIC -> "حذف جميع البيانات الشخصية"
                                    AppLanguage.HINDI -> "सभी व्यक्तिगत डेटा हटाएँ"
                                    AppLanguage.ENGLISH -> "Delete All Personal Data"
                                }
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }

    if (showAddSheet || editingEntry != null) {
        AddEditSalahSheet(
            editingEntry = editingEntry,
            language = language,
            onDismiss = {
                showAddSheet = false
                editingEntry = null
            },
            onSave = { prayer, dateKey, reason, note, completed, compDate ->
                val targetEdit = editingEntry
                showAddSheet = false
                editingEntry = null
                scope.launch {
                    if (targetEdit == null) {
                        MissedSalahRepository.addEntry(
                            context = context,
                            prayer = prayer,
                            missedDateKey = dateKey,
                            reasonCategory = reason,
                            optionalNote = note,
                            markCompletedNow = completed,
                            completedDateKey = compDate
                        )
                    } else {
                        MissedSalahRepository.updateEntry(
                            context = context,
                            updated = targetEdit.copy(
                                prayerType = prayer.name,
                                missedDateKey = dateKey,
                                reasonCategory = reason.name,
                                optionalNote = note.trim(),
                                status = if (completed) QadaEntryStatus.COMPLETED.name else QadaEntryStatus.PENDING.name,
                                completedDateKey = if (completed) compDate else null
                            )
                        )
                    }
                }
            },
            onDelete = { id ->
                editingEntry = null
                scope.launch {
                    MissedSalahRepository.deleteEntry(context, id)
                }
            }
        )
    }

    if (showEstimateSheet) {
        BacklogEstimateSheet(
            current = state.estimate,
            language = language,
            onDismiss = { showEstimateSheet = false },
            onSave = { cfg ->
                MissedSalahRepository.saveBacklogEstimate(context, cfg)
                showEstimateSheet = false
            }
        )
    }

    if (showPlannerSheet) {
        QadaPlannerSheet(
            current = state.planner,
            pendingRecorded = state.pendingQadaCount,
            language = language,
            onDismiss = { showPlannerSheet = false },
            onSave = { cfg ->
                MissedSalahRepository.savePlannerConfig(context, cfg)
                showPlannerSheet = false
            }
        )
    }

    if (showDeleteAllConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteAllConfirm = false },
            title = {
                Text(
                    when (language) {
                        AppLanguage.URDU -> "تمام ذاتی ڈیٹا حذف کریں؟"
                        AppLanguage.ARABIC -> "تأكيد حذف جميع البيانات؟"
                        AppLanguage.HINDI -> "सभी डेटा हटाएँ?"
                        AppLanguage.ENGLISH -> "Delete All Personal Data?"
                    }
                )
            },
            text = {
                Text(
                    when (language) {
                        AppLanguage.URDU -> "اس سے آپ کے تمام درج شدہ اندراجات، تخمینہ، پلانر اور مقامی بیک اپ فائلیں ہمیشہ کے لیے حذف ہو جائیں گی۔"
                        AppLanguage.ARABIC -> "سيؤدي هذا إلى مسح جميع السجلات والتقدير والمخطط والنسخ الاحتياطية المحلية نهائياً."
                        AppLanguage.HINDI -> "इससे आपके सभी नमाज़ रिकॉर्ड, अनुमान, योजनाकार और स्थानीय बैकअप स्थायी रूप से हट जाएंगे।"
                        AppLanguage.ENGLISH -> "This will permanently erase all logged Salah entries, estimates, planner settings, and local backup files on this device."
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAllConfirm = false
                        scope.launch {
                            MissedSalahRepository.deleteAllPersonalData(context)
                            statusMessage = when (language) {
                                AppLanguage.URDU -> "تمام ذاتی ڈیٹا حذف کر دیا گیا۔"
                                AppLanguage.ARABIC -> "تم حذف جميع البيانات الشخصية."
                                AppLanguage.HINDI -> "सभी व्यक्तिगत डेटा हटा दिया गया।"
                                AppLanguage.ENGLISH -> "All personal Salah records deleted."
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("salah_confirm_delete_all_btn")
                ) {
                    Text(
                        when (language) {
                            AppLanguage.URDU -> "جی ہاں، حذف کریں"
                            AppLanguage.ARABIC -> "نعم، احذف الكل"
                            AppLanguage.HINDI -> "हाँ, हटाएँ"
                            AppLanguage.ENGLISH -> "Delete Everything"
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllConfirm = false }) {
                    Text(AppLocalization.get("qibla_cancel", language))
                }
            }
        )
    }
}

@Composable
private fun MetricTile(
    label: String,
    value: String,
    containerColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SalahEntryItemCard(
    entry: MissedSalahEntryEntity,
    language: AppLanguage,
    onToggleCompleted: () -> Unit,
    onEdit: () -> Unit
) {
    val status = entry.statusEnum()
    val isDone = status == QadaEntryStatus.COMPLETED
    val isExempt = status == QadaEntryStatus.EXEMPT_NOT_REQUIRED

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().testTag("salah_entry_card_${entry.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            if (!isExempt) {
                IconButton(
                    onClick = onToggleCompleted,
                    modifier = Modifier.minimumInteractiveComponentSize()
                        .testTag("salah_toggle_done_${entry.id}")
                ) {
                    Icon(
                        imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = if (isDone) "Completed" else "Mark Qada Done",
                        tint = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${entry.prayerEnum().label(language)} • ${entry.missedDateKey}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = entry.reasonEnum().label(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (entry.optionalNote.isNotBlank()) {
                    Text(
                        text = entry.optionalNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                if (isDone && !entry.completedDateKey.isNullOrBlank()) {
                    Text(
                        text = "Qada done: ${entry.completedDateKey}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            IconButton(
                onClick = onEdit,
                modifier = Modifier.minimumInteractiveComponentSize()
                    .testTag("salah_edit_entry_${entry.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Entry"
                )
            }
        }
    }
}
