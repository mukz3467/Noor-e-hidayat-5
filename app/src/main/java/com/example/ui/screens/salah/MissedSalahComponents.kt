package com.example.ui.screens.salah

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.salah.BacklogEstimateConfig
import com.example.data.salah.MissedReasonCategory
import com.example.data.salah.MissedSalahEntryEntity
import com.example.data.salah.MissedSalahRepository
import com.example.data.salah.QadaEntryStatus
import com.example.data.salah.QadaPlannerConfig
import com.example.data.salah.SalahPrayerType
import com.example.ui.components.noorSelectableChipColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val ISO_DATE_REGEX = Regex("^\\d{4}-\\d{2}-\\d{2}$")

@Composable
fun SalahCalendarCard(
    entries: List<MissedSalahEntryEntity>,
    selectedDateKey: String?,
    onSelectDateKey: (String?) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    var monthOffset by remember { mutableIntStateOf(0) }

    val cal = remember(monthOffset) {
        Calendar.getInstance(Locale.US).apply {
            set(Calendar.DAY_OF_MONTH, 1)
            add(Calendar.MONTH, monthOffset)
        }
    }
    val year = cal.get(Calendar.YEAR)
    val monthZeroBased = cal.get(Calendar.MONTH)
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeekOffset = (cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY).coerceAtLeast(0)

    val monthTitle = remember(monthOffset, language) {
        val loc = when (language) {
            AppLanguage.URDU -> Locale("ur")
            AppLanguage.ARABIC -> Locale("ar")
            AppLanguage.HINDI -> Locale("hi")
            AppLanguage.ENGLISH -> Locale.ENGLISH
        }
        SimpleDateFormat("MMMM yyyy", loc).format(cal.time)
    }

    val statusByDayKey = remember(entries, year, monthZeroBased) {
        val prefix = String.format(Locale.US, "%04d-%02d-", year, monthZeroBased + 1)
        val map = mutableMapOf<String, Pair<Boolean, Boolean>>() // (hasPending, hasCompleted)
        entries.forEach { e ->
            if (e.missedDateKey.startsWith(prefix)) {
                val cur = map[e.missedDateKey] ?: (false to false)
                val isPending = e.statusEnum() == QadaEntryStatus.PENDING
                val isDone = e.statusEnum() == QadaEntryStatus.COMPLETED
                map[e.missedDateKey] = (cur.first || isPending) to (cur.second || isDone)
            }
            if (e.completedDateKey != null && e.completedDateKey.startsWith(prefix)) {
                val cur = map[e.completedDateKey] ?: (false to false)
                map[e.completedDateKey] = cur.first to true
            }
        }
        map
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("salah_calendar_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { monthOffset -= 1 },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = when (language) {
                            AppLanguage.URDU -> "پچھلا مہینہ"
                            AppLanguage.ARABIC -> "الشهر السابق"
                            AppLanguage.HINDI -> "पिछला महीना"
                            AppLanguage.ENGLISH -> "Previous Month"
                        }
                    )
                }
                Text(
                    text = monthTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    onClick = { monthOffset += 1 },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = when (language) {
                            AppLanguage.URDU -> "اگلا مہینہ"
                            AppLanguage.ARABIC -> "الشهر التالي"
                            AppLanguage.HINDI -> "अगला महीना"
                            AppLanguage.ENGLISH -> "Next Month"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val dayHeaders = when (language) {
                AppLanguage.ARABIC -> listOf("ح", "ن", "ث", "ر", "خ", "ج", "س")
                AppLanguage.URDU -> listOf("اتو", "پیر", "منگ", "بدھ", "جمع", "جمعہ", "ہفت")
                AppLanguage.HINDI -> listOf("रवि", "सोम", "मंग", "बुध", "गुरु", "शुक्र", "शनि")
                AppLanguage.ENGLISH -> listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                dayHeaders.forEach { d ->
                    Text(
                        text = d,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val totalCells = firstDayOfWeekOffset + daysInMonth
            val rows = (totalCells + 6) / 7
            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNum = cellIndex - firstDayOfWeekOffset + 1
                        if (dayNum < 1 || dayNum > daysInMonth) {
                            Spacer(modifier = Modifier.weight(1f).height(48.dp))
                        } else {
                            val dateKey = String.format(Locale.US, "%04d-%02d-%02d", year, monthZeroBased + 1, dayNum)
                            val flags = statusByDayKey[dateKey]
                            val hasPending = flags?.first == true
                            val hasCompleted = flags?.second == true
                            val isSelected = selectedDateKey == dateKey

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isSelected -> MaterialTheme.colorScheme.primaryContainer
                                            hasPending -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f)
                                            hasCompleted -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                                            else -> MaterialTheme.colorScheme.surface
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 0.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        onSelectDateKey(if (isSelected) null else dateKey)
                                    }
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = dayNum.toString(),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = if (isSelected || hasPending || hasCompleted) FontWeight.Bold else FontWeight.Normal
                                        ),
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (hasPending || hasCompleted) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                            if (hasPending) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(CircleShape)
                                                        .background(MaterialTheme.colorScheme.tertiary)
                                                )
                                            }
                                            if (hasCompleted) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(CircleShape)
                                                        .background(MaterialTheme.colorScheme.primary)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.tertiary))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (language) {
                            AppLanguage.URDU -> "بقایا قضا"
                            AppLanguage.ARABIC -> "فائتة متبقية"
                            AppLanguage.HINDI -> "बकाया क़ज़ा"
                            AppLanguage.ENGLISH -> "Pending Qada"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (language) {
                            AppLanguage.URDU -> "مکمل شدہ"
                            AppLanguage.ARABIC -> "مقضية"
                            AppLanguage.HINDI -> "पूर्ण"
                            AppLanguage.ENGLISH -> "Completed"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (selectedDateKey != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { onSelectDateKey(null) },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "سب دکھائیں"
                                AppLanguage.ARABIC -> "عرض الكل"
                                AppLanguage.HINDI -> "सभी दिखाएँ"
                                AppLanguage.ENGLISH -> "Show All"
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditSalahSheet(
    editingEntry: MissedSalahEntryEntity?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (
        prayer: SalahPrayerType,
        missedDateKey: String,
        reason: MissedReasonCategory,
        note: String,
        completed: Boolean,
        completedDateKey: String?
    ) -> Unit,
    onDelete: ((Long) -> Unit)? = null
) {
    val today = remember { MissedSalahRepository.todayIsoDateKey() }
    val yesterday = remember(today) { MissedSalahRepository.offsetIsoDateKey(today, -1) }

    var selectedPrayer by remember(editingEntry) {
        mutableStateOf(editingEntry?.prayerEnum() ?: SalahPrayerType.FAJR)
    }
    var missedDateInput by remember(editingEntry) {
        mutableStateOf(editingEntry?.missedDateKey ?: today)
    }
    var selectedReason by remember(editingEntry) {
        mutableStateOf(editingEntry?.reasonEnum() ?: MissedReasonCategory.SLEEP_OR_FORGETFULNESS)
    }
    var noteInput by remember(editingEntry) {
        mutableStateOf(editingEntry?.optionalNote ?: "")
    }
    var markCompleted by remember(editingEntry) {
        mutableStateOf(editingEntry?.statusEnum() == QadaEntryStatus.COMPLETED)
    }
    var completedDateInput by remember(editingEntry) {
        mutableStateOf(editingEntry?.completedDateKey ?: today)
    }
    var dateError by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = if (editingEntry == null) {
                    when (language) {
                        AppLanguage.URDU -> "چھوٹی ہوئی نماز درج کریں"
                        AppLanguage.ARABIC -> "تسجيل صلاة فائتة"
                        AppLanguage.HINDI -> "छूटी हुई नमाज़ दर्ज करें"
                        AppLanguage.ENGLISH -> "Record Missed Salah"
                    }
                } else {
                    when (language) {
                        AppLanguage.URDU -> "اندراج میں ترمیم کریں"
                        AppLanguage.ARABIC -> "تعديل السجل"
                        AppLanguage.HINDI -> "इंदराज संपादित करें"
                        AppLanguage.ENGLISH -> "Edit Missed Salah Entry"
                    }
                },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = when (language) {
                    AppLanguage.URDU -> "نماز منتخب کریں"
                    AppLanguage.ARABIC -> "اختر الصلاة"
                    AppLanguage.HINDI -> "नमाज़ चुनें"
                    AppLanguage.ENGLISH -> "Select Prayer"
                },
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SalahPrayerType.entries.forEach { prayer ->
                    FilterChip(
                        selected = selectedPrayer == prayer,
                        onClick = { selectedPrayer = prayer },
                        label = { Text(prayer.label(language)) },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.minimumInteractiveComponentSize()
                            .testTag("salah_prayer_chip_${prayer.id}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = when (language) {
                    AppLanguage.URDU -> "تاریخ (YYYY-MM-DD)"
                    AppLanguage.ARABIC -> "التاريخ (YYYY-MM-DD)"
                    AppLanguage.HINDI -> "तिथि (YYYY-MM-DD)"
                    AppLanguage.ENGLISH -> "Date Missed (YYYY-MM-DD)"
                },
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {
                        missedDateInput = today
                        dateError = false
                    },
                    label = {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "آج"
                                AppLanguage.ARABIC -> "اليوم"
                                AppLanguage.HINDI -> "आज"
                                AppLanguage.ENGLISH -> "Today"
                            }
                        )
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                )
                AssistChip(
                    onClick = {
                        missedDateInput = yesterday
                        dateError = false
                    },
                    label = {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "کل"
                                AppLanguage.ARABIC -> "أمس"
                                AppLanguage.HINDI -> "कल"
                                AppLanguage.ENGLISH -> "Yesterday"
                            }
                        )
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = missedDateInput,
                onValueChange = {
                    missedDateInput = it
                    dateError = !ISO_DATE_REGEX.matches(it.trim())
                },
                singleLine = true,
                isError = dateError,
                label = { Text("YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth().testTag("salah_date_input")
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = when (language) {
                    AppLanguage.URDU -> "وجہ / زمرہ (اختیاری وضاحت)"
                    AppLanguage.ARABIC -> "السبب / التصنيف"
                    AppLanguage.HINDI -> "कारण / श्रेणी"
                    AppLanguage.ENGLISH -> "Reason Category"
                },
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MissedReasonCategory.entries.forEach { reason ->
                    FilterChip(
                        selected = selectedReason == reason,
                        onClick = {
                            selectedReason = reason
                            if (reason == MissedReasonCategory.JURISPRUDENCE_EXEMPT) {
                                markCompleted = false
                            }
                        },
                        label = { Text(reason.label(language)) },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.minimumInteractiveComponentSize()
                            .testTag("salah_reason_chip_${reason.id}")
                    )
                }
            }

            if (selectedReason != MissedReasonCategory.JURISPRUDENCE_EXEMPT) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Switch(
                        checked = markCompleted,
                        onCheckedChange = { markCompleted = it },
                        modifier = Modifier.testTag("salah_completed_switch")
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = when (language) {
                            AppLanguage.URDU -> "قضا مکمل ہو چکی ہے"
                            AppLanguage.ARABIC -> "تم قضاؤها"
                            AppLanguage.HINDI -> "क़ज़ा पूर्ण हो गई है"
                            AppLanguage.ENGLISH -> "Marked as Completed (Qada Done)"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (markCompleted) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = completedDateInput,
                        onValueChange = { completedDateInput = it },
                        singleLine = true,
                        label = {
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "مکمل کرنے کی تاریخ (YYYY-MM-DD)"
                                    AppLanguage.ARABIC -> "تاريخ القضاء (YYYY-MM-DD)"
                                    AppLanguage.HINDI -> "पूर्ण करने की तिथि (YYYY-MM-DD)"
                                    AppLanguage.ENGLISH -> "Completion Date (YYYY-MM-DD)"
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = when (language) {
                            AppLanguage.URDU -> "شرعی عذر والی حالت (مثلاً حیض و نفاس وغیرہ) قضا کے واجب شمار میں شامل نہیں کی جاتی۔ ایپ آپ سے کوئی ذاتی تفصیل نہیں مانگتی۔"
                            AppLanguage.ARABIC -> "الأعذار المعفو عنها شرعاً لا تُحسب ضمن الصلوات المطلوب قضاؤها، ولا يطلب التطبيق أي تفاصيل شخصية حساسة."
                            AppLanguage.HINDI -> "शरीअत द्वारा मुक्त स्थिति को बकाया क़ज़ा गिनती में शामिल नहीं किया जाता। ऐप कोई निजी विवरण नहीं माँगता।"
                            AppLanguage.ENGLISH -> "Jurisprudence-exempt entries are recorded for personal awareness only and excluded from pending Qada counts. Never enter sensitive details."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = noteInput,
                onValueChange = { noteInput = it },
                maxLines = 3,
                label = {
                    Text(
                        when (language) {
                            AppLanguage.URDU -> "ذاتی یادداشت (مکمل طور پر اختیاری)"
                            AppLanguage.ARABIC -> "ملاحظة شخصية (اختياري تماماً)"
                            AppLanguage.HINDI -> "वैकल्पिक व्यक्तिगत नोट"
                            AppLanguage.ENGLISH -> "Optional Note (Completely Optional)"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth().testTag("salah_note_input")
            )

            Spacer(modifier = Modifier.height(18.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (editingEntry != null && onDelete != null) {
                    OutlinedButton(
                        onClick = { onDelete(editingEntry.id) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.height(48.dp).testTag("salah_delete_entry_btn")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Entry")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "حذف"
                                AppLanguage.ARABIC -> "حذف"
                                AppLanguage.HINDI -> "हटाएँ"
                                AppLanguage.ENGLISH -> "Delete"
                            }
                        )
                    }
                }
                Button(
                    onClick = {
                        val cleanDate = missedDateInput.trim()
                        if (!ISO_DATE_REGEX.matches(cleanDate)) {
                            dateError = true
                            return@Button
                        }
                        val cleanCompleted = completedDateInput.trim().takeIf { ISO_DATE_REGEX.matches(it) } ?: today
                        onSave(selectedPrayer, cleanDate, selectedReason, noteInput, markCompleted, cleanCompleted)
                    },
                    modifier = Modifier.weight(1f).height(48.dp).testTag("salah_save_entry_btn")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        when (language) {
                            AppLanguage.URDU -> "محفوظ کریں"
                            AppLanguage.ARABIC -> "حفظ"
                            AppLanguage.HINDI -> "सहेजें"
                            AppLanguage.ENGLISH -> "Save Entry"
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BacklogEstimateSheet(
    current: BacklogEstimateConfig,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (BacklogEstimateConfig) -> Unit
) {
    var enabled by remember(current) { mutableStateOf(current.enabled) }
    var startDate by remember(current) { mutableStateOf(current.startDateKey) }
    var endDate by remember(current) { mutableStateOf(current.endDateKey) }
    var durationNote by remember(current) { mutableStateOf(current.durationNote) }
    var estMap by remember(current) {
        mutableStateOf(SalahPrayerType.entries.associateWith { (current.estimatedByPrayer[it] ?: 0).toString() })
    }
    var doneMap by remember(current) {
        mutableStateOf(SalahPrayerType.entries.associateWith { (current.completedByPrayer[it] ?: 0).toString() })
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = when (language) {
                    AppLanguage.URDU -> "پرانے بقایا کا اختیاری تخمینہ"
                    AppLanguage.ARABIC -> "التقدير الاختياري للصلوات الفائتة السابقة"
                    AppLanguage.HINDI -> "पिछली बकाया नमाज़ों का वैकल्पिक अनुमान"
                    AppLanguage.ENGLISH -> "Optional Historical Backlog Estimate"
                },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (language) {
                            AppLanguage.URDU -> "یہ صرف آپ کا ذاتی تخمینہ ہے، کوئی حتمی شرعی یا تاریخی عدد نہیں۔ ایپ آپ کی عمر یا ذاتی معلومات سے خودکار حساب نہیں لگاتی۔ یہ تصدیق شدہ روزانہ اندراجات سے الگ رکھا جاتا ہے۔"
                            AppLanguage.ARABIC -> "هذا مجرد تقدير شخصي وليس حقيقة شرعية أو تاريخية دقيقة. لا يحسب التطبيق أي أعداد تلقائياً من العمر، ويُعرض بشكل منفصل عن السجلات المؤكدة."
                            AppLanguage.HINDI -> "यह केवल एक व्यक्तिगत अनुमान है, सटीक धार्मिक या ऐतिहासिक तथ्य नहीं। ऐप आपकी उम्र से स्वतः गणना नहीं करता और इसे पक्के रिकॉर्ड से अलग रखा जाता है।"
                            AppLanguage.ENGLISH -> "Personal Estimate Only — Not an exact religious or historical fact. The app never auto-calculates obligations from age or personal info, and keeps this strictly separate from confirmed logs."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Switch(
                    checked = enabled,
                    onCheckedChange = { enabled = it },
                    modifier = Modifier.testTag("salah_estimate_enable_switch")
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when (language) {
                        AppLanguage.URDU -> "تخمینہ سیکشن فعال کریں"
                        AppLanguage.ARABIC -> "تفعيل قسم التقدير الشخصي"
                        AppLanguage.HINDI -> "बैकलाग अनुमान सक्षम करें"
                        AppLanguage.ENGLISH -> "Enable Personal Estimate Section"
                    },
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    singleLine = true,
                    label = {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "تخمینی آغاز"
                                AppLanguage.ARABIC -> "بداية تقريبية"
                                AppLanguage.HINDI -> "अनुमानित प्रारंभ"
                                AppLanguage.ENGLISH -> "Approx. Start"
                            }
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    singleLine = true,
                    label = {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "اختتامی تاریخ (اختیاری)"
                                AppLanguage.ARABIC -> "النهاية (اختياري)"
                                AppLanguage.HINDI -> "समाप्ति (वैकल्पिक)"
                                AppLanguage.ENGLISH -> "End Date (Optional)"
                            }
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = durationNote,
                onValueChange = { durationNote = it },
                singleLine = true,
                label = {
                    Text(
                        when (language) {
                            AppLanguage.URDU -> "مدت کی وضاحت (اختیاری)"
                            AppLanguage.ARABIC -> "ملاحظة المدة (اختياري)"
                            AppLanguage.HINDI -> "अवधि टिप्पणी (वैकल्पिक)"
                            AppLanguage.ENGLISH -> "Optional Duration Note"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))
            SalahPrayerType.entries.forEach { prayer ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(
                        text = prayer.label(language),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.weight(0.9f)
                    )
                    OutlinedTextField(
                        value = estMap[prayer] ?: "0",
                        onValueChange = { v ->
                            if (v.all { it.isDigit() } && v.length <= 5) {
                                estMap = estMap.toMutableMap().apply { put(prayer, v) }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "تخمینہ"
                                    AppLanguage.ARABIC -> "تقدير"
                                    AppLanguage.HINDI -> "अनुमान"
                                    AppLanguage.ENGLISH -> "Est."
                                }
                            )
                        },
                        modifier = Modifier.weight(1f).testTag("salah_est_input_${prayer.id}")
                    )
                    OutlinedTextField(
                        value = doneMap[prayer] ?: "0",
                        onValueChange = { v ->
                            if (v.all { it.isDigit() } && v.length <= 5) {
                                doneMap = doneMap.toMutableMap().apply { put(prayer, v) }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "مکمل"
                                    AppLanguage.ARABIC -> "مقضية"
                                    AppLanguage.HINDI -> "पूर्ण"
                                    AppLanguage.ENGLISH -> "Done"
                                }
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val estParsed = SalahPrayerType.entries.associateWith { (estMap[it]?.toIntOrNull() ?: 0).coerceAtLeast(0) }
                    val doneParsed = SalahPrayerType.entries.associateWith { p ->
                        val estVal = estParsed[p] ?: 0
                        (doneMap[p]?.toIntOrNull() ?: 0).coerceIn(0, estVal)
                    }
                    onSave(
                        BacklogEstimateConfig(
                            enabled = enabled,
                            startDateKey = startDate.trim(),
                            endDateKey = endDate.trim(),
                            durationNote = durationNote.trim(),
                            estimatedByPrayer = estParsed,
                            completedByPrayer = doneParsed
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("salah_estimate_save_btn")
            ) {
                Text(
                    when (language) {
                        AppLanguage.URDU -> "تخمینہ محفوظ کریں"
                        AppLanguage.ARABIC -> "حفظ التقدير"
                        AppLanguage.HINDI -> "अनुमान सहेजें"
                        AppLanguage.ENGLISH -> "Save Personal Estimate"
                    }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QadaPlannerSheet(
    current: QadaPlannerConfig,
    pendingRecorded: Int,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (QadaPlannerConfig) -> Unit
) {
    var active by remember(current) { mutableStateOf(current.isActive) }
    var paused by remember(current) { mutableStateOf(current.isPaused) }
    var dailyTarget by remember(current) { mutableIntStateOf(current.dailyTarget.coerceIn(1, 20)) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = when (language) {
                    AppLanguage.URDU -> "اختیاری قضا شیڈول پلانر"
                    AppLanguage.ARABIC -> "مخطط القضاء الاختياري"
                    AppLanguage.HINDI -> "वैकल्पिक क़ज़ा प्रगति योजनाकार"
                    AppLanguage.ENGLISH -> "Optional Qada Progress Planner"
                },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (language) {
                    AppLanguage.URDU -> "اپنی سہولت کے مطابق روزانہ ہدف مقرر کریں۔ آپ کسی بھی وقت ہدف بدل سکتے ہیں یا منصوبہ روک سکتے ہیں؛ آپ کے اندراجات پر کوئی اثر نہیں پڑے گا۔"
                    AppLanguage.ARABIC -> "حدد هدفاً يومياً مريحاً حسب استطاعتك. يمكنك تعديله أو إيقافه مؤقتاً في أي وقت دون أي تغيير في سجلاتك."
                    AppLanguage.HINDI -> "अपनी सुविधा के अनुसार दैनिक लक्ष्य चुनें। आप कभी भी इसे बदल या रोक सकते हैं; आपके रिकॉर्ड पर कोई दबाव या बदलाव नहीं होगा।"
                    AppLanguage.ENGLISH -> "Set a gentle daily pace at your convenience. You can change or pause anytime without any pressure or changes to your logged records."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Switch(
                    checked = active,
                    onCheckedChange = { active = it },
                    modifier = Modifier.testTag("salah_planner_active_switch")
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = when (language) {
                        AppLanguage.URDU -> "ذاتی پلانر فعال کریں"
                        AppLanguage.ARABIC -> "تفعيل المخطط الشخصي"
                        AppLanguage.HINDI -> "व्यक्तिगत योजनाकार सक्रिय करें"
                        AppLanguage.ENGLISH -> "Enable Personal Pace Planner"
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (language) {
                        AppLanguage.URDU -> "روزانہ قضا ہدف"
                        AppLanguage.ARABIC -> "الهدف اليومي"
                        AppLanguage.HINDI -> "दैनिक क़ज़ा लक्ष्य"
                        AppLanguage.ENGLISH -> "Daily Target Prayers"
                    },
                    style = MaterialTheme.typography.titleSmall
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (dailyTarget > 1) dailyTarget -= 1 },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease Target")
                    }
                    Text(
                        text = "$dailyTarget",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = { if (dailyTarget < 20) dailyTarget += 1 },
                        modifier = Modifier.minimumInteractiveComponentSize()
                            .testTag("salah_planner_inc_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase Target")
                    }
                }
            }

            if (active) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Switch(
                        checked = paused,
                        onCheckedChange = { paused = it },
                        modifier = Modifier.testTag("salah_planner_pause_switch")
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = when (language) {
                            AppLanguage.URDU -> "عارضی طور پر روکیں (Pause)"
                            AppLanguage.ARABIC -> "إيقاف مؤقت"
                            AppLanguage.HINDI -> "अस्थायी रूप से रोकें (Pause)"
                            AppLanguage.ENGLISH -> "Pause Plan Temporarily"
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                val estDays = if (pendingRecorded > 0) (pendingRecorded + dailyTarget - 1) / dailyTarget else 0
                Text(
                    text = when (language) {
                        AppLanguage.URDU -> "تخمینی مدت (موجودہ $pendingRecorded اندراجات): تقریباً $estDays دن"
                        AppLanguage.ARABIC -> "المدة التقديرية للسجلات المتبقية ($pendingRecorded): نحو $estDays يوماً"
                        AppLanguage.HINDI -> "वर्तमान $pendingRecorded बकाया के लिए अनुमानित अवधि: लगभग $estDays दिन"
                        AppLanguage.ENGLISH -> "Gentle Pace Projection ($pendingRecorded pending logged): ~$estDays days"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = { onSave(QadaPlannerConfig(isActive = active, isPaused = paused, dailyTarget = dailyTarget)) },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("salah_planner_save_btn")
            ) {
                Text(
                    when (language) {
                        AppLanguage.URDU -> "پلانر محفوظ کریں"
                        AppLanguage.ARABIC -> "حفظ المخطط"
                        AppLanguage.HINDI -> "योजना सहेजें"
                        AppLanguage.ENGLISH -> "Save Planner Settings"
                    }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
