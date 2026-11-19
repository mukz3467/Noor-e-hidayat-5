package com.example.ui.screens.business

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.business.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import java.util.Locale

@Composable
fun BusinessToolsAndProjectTab(
    language: AppLanguage,
    startupStepsMap: Map<Int, BusinessStartupProjectEntity>,
    onSaveStartupStep: (Int, String, Boolean) -> Unit
) {
    var expandedStep by remember { mutableStateOf<Int?>(1) }
    val completedStepsCount = startupStepsMap.values.count { it.isCompleted }

    // Business Zakat Calculator State (AAOIFI SS 35)
    var cashStr by remember { mutableStateOf("") }
    var stockStr by remember { mutableStateOf("") }
    var receivablesStr by remember { mutableStateOf("") }
    var liabilitiesStr by remember { mutableStateOf("") }

    val cash = cashStr.toDoubleOrNull() ?: 0.0
    val stock = stockStr.toDoubleOrNull() ?: 0.0
    val receivables = receivablesStr.toDoubleOrNull() ?: 0.0
    val liabilities = liabilitiesStr.toDoubleOrNull() ?: 0.0
    val netZakatable = maxOf(0.0, cash + stock + receivables - liabilities)
    val businessZakatDue = netZakatable * 0.025

    // Pricing & Cost Calculator State
    var unitCostStr by remember { mutableStateOf("") }
    var overheadStr by remember { mutableStateOf("") }
    var marginPctStr by remember { mutableStateOf("20") }
    val unitTotal = (unitCostStr.toDoubleOrNull() ?: 0.0) + (overheadStr.toDoubleOrNull() ?: 0.0)
    val marginPct = (marginPctStr.toDoubleOrNull() ?: 0.0).coerceAtLeast(0.0)
    val sellingPrice = unitTotal * (1.0 + marginPct / 100.0)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize().testTag("business_project_tools_list")
    ) {
        // Module 24: 13-Step Startup Project Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("ba_startup_project_title", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$completedStepsCount / 13 ${AppLocalization.get("ba_steps_completed", language)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { completedStepsCount / 13f },
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                    )
                }
            }
        }

        items(BusinessAcademyRepository.startupStepTemplates, key = { it.stepNumber }) { tpl ->
            val saved = startupStepsMap[tpl.stepNumber]
            val isOpen = expandedStep == tpl.stepNumber
            var draft by remember(tpl.stepNumber, saved?.answerText) {
                mutableStateOf(saved?.answerText.orEmpty())
            }
            var isDone by remember(tpl.stepNumber, saved?.isCompleted) {
                mutableStateOf(saved?.isCompleted == true)
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().testTag("startup_step_card_${tpl.stepNumber}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                            .clickable { expandedStep = if (isOpen) null else tpl.stepNumber }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = tpl.title.get(language),
                                tint = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tpl.title.get(language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                        Icon(
                            imageVector = if (isOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand step"
                        )
                    }
                    if (isOpen) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tpl.prompt.get(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = draft,
                            onValueChange = { draft = it },
                            minLines = 2,
                            maxLines = 4,
                            placeholder = { Text(AppLocalization.get("ba_startup_write_plan", language)) },
                            modifier = Modifier.fillMaxWidth().testTag("startup_step_input_${tpl.stepNumber}")
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isDone = !isDone }.weight(1f)
                            ) {
                                Checkbox(checked = isDone, onCheckedChange = { isDone = it })
                                Text(AppLocalization.get("ba_mark_complete", language), style = MaterialTheme.typography.bodySmall)
                            }
                            Button(
                                onClick = { onSaveStartupStep(tpl.stepNumber, draft, isDone) },
                                modifier = Modifier.height(48.dp).testTag("startup_step_save_${tpl.stepNumber}")
                            ) {
                                Text(AppLocalization.get("ba_save_note", language))
                            }
                        }
                    }
                }
            }
        }

        // Offline Business Zakat Calculator (AAOIFI SS 35)
        item {
            BusinessSectionCard(AppLocalization.get("ba_zakat_calc_title", language), Icons.Default.Calculate) {
                Text(
                    text = AppLocalization.get("ba_zakat_calc_subtitle", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                NumericCalcField(cashStr, { cashStr = it }, AppLocalization.get("ba_zakat_cash", language), "zakat_cash_input")
                Spacer(modifier = Modifier.height(8.dp))
                NumericCalcField(stockStr, { stockStr = it }, AppLocalization.get("ba_zakat_stock", language), "zakat_stock_input")
                Spacer(modifier = Modifier.height(8.dp))
                NumericCalcField(receivablesStr, { receivablesStr = it }, AppLocalization.get("ba_zakat_receivables", language), "zakat_recv_input")
                Spacer(modifier = Modifier.height(8.dp))
                NumericCalcField(liabilitiesStr, { liabilitiesStr = it }, AppLocalization.get("ba_zakat_liabilities", language), "zakat_liab_input")
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "${AppLocalization.get("ba_zakat_net_base", language)}: ${String.format(Locale.US, "%.2f", netZakatable)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${AppLocalization.get("ba_zakat_due", language)}: ${String.format(Locale.US, "%.2f", businessZakatDue)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag("zakat_due_output")
                        )
                    }
                }
            }
        }

        // Halal Pricing & Margin Calculator
        item {
            BusinessSectionCard(AppLocalization.get("ba_pricing_calc_title", language), Icons.Default.Sell) {
                Text(
                    text = AppLocalization.get("ba_pricing_non_pct_note", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                NumericCalcField(unitCostStr, { unitCostStr = it }, AppLocalization.get("ba_pricing_material", language), "pricing_mat_input")
                Spacer(modifier = Modifier.height(8.dp))
                NumericCalcField(overheadStr, { overheadStr = it }, AppLocalization.get("ba_pricing_overhead", language), "pricing_overhead_input")
                Spacer(modifier = Modifier.height(8.dp))
                NumericCalcField(marginPctStr, { marginPctStr = it }, AppLocalization.get("ba_pricing_margin_pct", language), "pricing_margin_input")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${AppLocalization.get("ba_pricing_suggested", language)}: ${String.format(Locale.US, "%.2f", sellingPrice)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun BusinessSavedAndCertTab(
    language: AppLanguage,
    bookmarks: List<BusinessBookmarkEntity>,
    notes: List<BusinessNoteEntity>,
    completedCount: Int,
    totalItemsCount: Int,
    quizzesPassedCount: Int,
    startupStepsCompleted: Int,
    certificatePath: String?,
    onOpenSavedItem: (String, String) -> Unit,
    onGenerateCertificate: (String) -> Unit
) {
    var learnerName by remember { mutableStateOf("") }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize().testTag("business_saved_cert_list")
    ) {
        item {
            BusinessSectionCard(AppLocalization.get("ba_cert_title", language), Icons.Default.WorkspacePremium) {
                Text(
                    text = "${AppLocalization.get("ba_progress_summary", language)}: $completedCount / $totalItemsCount • Quizzes: $quizzesPassedCount • Project: $startupStepsCompleted / 13",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = learnerName,
                    onValueChange = { learnerName = it },
                    label = { Text(AppLocalization.get("ba_learner_name", language)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("cert_learner_name_input")
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { onGenerateCertificate(learnerName) },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("business_generate_cert_btn")
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = AppLocalization.get("ba_generate_pdf_cert", language))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppLocalization.get("ba_generate_pdf_cert", language))
                }
                if (!certificatePath.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.fillMaxWidth().testTag("cert_generated_banner")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = AppLocalization.get("ba_cert_saved_offline", language),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = certificatePath,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        item {
            BusinessSectionCard("${AppLocalization.get("ba_bookmarks_heading", language)} (${bookmarks.size})", Icons.Default.Bookmarks) {
                if (bookmarks.isEmpty()) {
                    Text(AppLocalization.get("ba_no_bookmarks", language), style = MaterialTheme.typography.bodySmall)
                } else {
                    bookmarks.forEach { bm ->
                        ListItem(
                            headlineContent = { Text(bm.titleSnapshot) },
                            supportingContent = { Text(bm.itemType.uppercase()) },
                            trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = bm.titleSnapshot) },
                            modifier = Modifier.clickable { onOpenSavedItem(bm.itemId, bm.itemType) }
                        )
                    }
                }
            }
        }

        item {
            BusinessSectionCard("${AppLocalization.get("ba_personal_notes", language)} (${notes.size})", Icons.Default.EditNote) {
                if (notes.isEmpty()) {
                    Text(AppLocalization.get("ba_no_notes", language), style = MaterialTheme.typography.bodySmall)
                } else {
                    notes.forEach { note ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onOpenSavedItem(note.itemId, note.itemType) }
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(note.itemId, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                Text(note.noteText, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NumericCalcField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    tag: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { raw ->
            if (raw.all { it.isDigit() || it == '.' }) onChange(raw)
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth().testTag(tag)
    )
}
