package com.example.ui.screens.huqooq

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.huqooq.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import java.util.Locale

@Composable
fun HuqooqInheritanceCalculatorScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    var totalEstateText by remember { mutableStateOf("100000") }
    var funeralText by remember { mutableStateOf("0") }
    var debtsText by remember { mutableStateOf("0") }
    var wasiyyatText by remember { mutableStateOf("0") }
    var otherDeductionsText by remember { mutableStateOf("0") }
    var heirsConsentOverThird by remember { mutableStateOf(false) }

    var hasHusband by remember { mutableStateOf(false) }
    var wifeCount by remember { mutableIntStateOf(0) }
    var hasFather by remember { mutableStateOf(false) }
    var hasMother by remember { mutableStateOf(false) }
    var hasPgf by remember { mutableStateOf(false) }
    var hasPgm by remember { mutableStateOf(false) }
    var hasMgm by remember { mutableStateOf(false) }

    var sons by remember { mutableIntStateOf(0) }
    var daughters by remember { mutableIntStateOf(0) }
    var grandsons by remember { mutableIntStateOf(0) }
    var granddaughters by remember { mutableIntStateOf(0) }

    var fullBrothers by remember { mutableIntStateOf(0) }
    var fullSisters by remember { mutableIntStateOf(0) }
    var consBrothers by remember { mutableIntStateOf(0) }
    var consSisters by remember { mutableIntStateOf(0) }
    var uterineSiblings by remember { mutableIntStateOf(0) }

    var result by remember { mutableStateOf<InheritanceCalculationResult?>(null) }

    fun resetAll() {
        totalEstateText = "100000"
        funeralText = "0"
        debtsText = "0"
        wasiyyatText = "0"
        otherDeductionsText = "0"
        heirsConsentOverThird = false
        hasHusband = false
        wifeCount = 0
        hasFather = false
        hasMother = false
        hasPgf = false
        hasPgm = false
        hasMgm = false
        sons = 0
        daughters = 0
        grandsons = 0
        granddaughters = 0
        fullBrothers = 0
        fullSisters = 0
        consBrothers = 0
        consSisters = 0
        uterineSiblings = 0
        result = null
    }

    fun runCalculate() {
        val input = InheritanceInput(
            totalEstate = totalEstateText.trim().toDoubleOrNull() ?: -1.0,
            funeralExpenses = funeralText.trim().ifEmpty { "0" }.toDoubleOrNull() ?: -1.0,
            debts = debtsText.trim().ifEmpty { "0" }.toDoubleOrNull() ?: -1.0,
            wasiyyat = wasiyyatText.trim().ifEmpty { "0" }.toDoubleOrNull() ?: -1.0,
            otherDeductions = otherDeductionsText.trim().ifEmpty { "0" }.toDoubleOrNull() ?: -1.0,
            wasiyyatOverOneThirdHeirsConsent = heirsConsentOverThird,
            hasHusband = hasHusband,
            wifeCount = wifeCount,
            hasFather = hasFather,
            hasMother = hasMother,
            hasPaternalGrandfather = hasPgf,
            hasPaternalGrandmother = hasPgm,
            hasMaternalGrandmother = hasMgm,
            sons = sons,
            daughters = daughters,
            grandsons = grandsons,
            granddaughters = granddaughters,
            fullBrothers = fullBrothers,
            fullSisters = fullSisters,
            consanguineBrothers = consBrothers,
            consanguineSisters = consSisters,
            uterineSiblings = uterineSiblings
        )
        result = InheritanceCalculator.calculate(input)
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("hq_calc_title", language),
                language = language,
                onBackClick = onBack
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
                .testTag("hq_calculator_screen")
        ) {
            // 1. Estate & Deductions
            item {
                HuqooqSectionCard(
                    title = AppLocalization.get("hq_calc_estate_section", language),
                    icon = Icons.Default.AccountBalanceWallet
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        MoneyField(
                            value = totalEstateText,
                            onValueChange = { totalEstateText = it },
                            label = AppLocalization.get("hq_calc_total_estate", language),
                            tag = "calc_estate_input"
                        )
                        MoneyField(
                            value = funeralText,
                            onValueChange = { funeralText = it },
                            label = AppLocalization.get("hq_calc_funeral", language),
                            tag = "calc_funeral_input"
                        )
                        MoneyField(
                            value = debtsText,
                            onValueChange = { debtsText = it },
                            label = AppLocalization.get("hq_calc_debts", language),
                            tag = "calc_debts_input"
                        )
                        MoneyField(
                            value = wasiyyatText,
                            onValueChange = { wasiyyatText = it },
                            label = AppLocalization.get("hq_calc_wasiyyat", language),
                            tag = "calc_wasiyyat_input"
                        )
                        MoneyField(
                            value = otherDeductionsText,
                            onValueChange = { otherDeductionsText = it },
                            label = AppLocalization.get("hq_calc_other_deductions", language),
                            tag = "calc_other_deductions_input"
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = heirsConsentOverThird,
                                onCheckedChange = { heirsConsentOverThird = it },
                                modifier = Modifier.testTag("calc_wasiyyat_consent_checkbox")
                            )
                            Text(
                                text = AppLocalization.get("hq_calc_consent_toggle", language),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 2. Spouses
            item {
                HuqooqSectionCard(
                    title = AppLocalization.get("hq_calc_spouses_section", language),
                    icon = Icons.Default.Favorite
                ) {
                    HeirSwitchRow(
                        label = AppLocalization.get("hq_calc_husband", language),
                        checked = hasHusband,
                        tag = "calc_husband_switch",
                        onCheckedChange = {
                            hasHusband = it
                            if (it) wifeCount = 0
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    HeirStepperRow(
                        label = AppLocalization.get("hq_calc_wives", language),
                        count = wifeCount,
                        max = 4,
                        incTag = "calc_wives_inc",
                        decTag = "calc_wives_dec",
                        onChanged = {
                            wifeCount = it
                            if (it > 0) hasHusband = false
                        }
                    )
                }
            }

            // 3. Parents & Grandparents
            item {
                HuqooqSectionCard(
                    title = AppLocalization.get("hq_calc_ascendants_section", language),
                    icon = Icons.Default.FamilyRestroom
                ) {
                    HeirSwitchRow(AppLocalization.get("hq_calc_father", language), hasFather, "calc_father_switch") { hasFather = it }
                    HeirSwitchRow(AppLocalization.get("hq_calc_mother", language), hasMother, "calc_mother_switch") { hasMother = it }
                    HeirSwitchRow(AppLocalization.get("hq_calc_pgf", language), hasPgf, "calc_pgf_switch") { hasPgf = it }
                    HeirSwitchRow(AppLocalization.get("hq_calc_pgm", language), hasPgm, "calc_pgm_switch") { hasPgm = it }
                    HeirSwitchRow(AppLocalization.get("hq_calc_mgm", language), hasMgm, "calc_mgm_switch") { hasMgm = it }
                }
            }

            // 4. Descendants
            item {
                HuqooqSectionCard(
                    title = AppLocalization.get("hq_calc_descendants_section", language),
                    icon = Icons.Default.ChildCare
                ) {
                    HeirStepperRow(AppLocalization.get("hq_calc_sons", language), sons, 20, "calc_sons_inc", "calc_sons_dec") { sons = it }
                    HeirStepperRow(AppLocalization.get("hq_calc_daughters", language), daughters, 20, "calc_daughters_inc", "calc_daughters_dec") { daughters = it }
                    HeirStepperRow(AppLocalization.get("hq_calc_grandsons", language), grandsons, 20, "calc_grandsons_inc", "calc_grandsons_dec") { grandsons = it }
                    HeirStepperRow(AppLocalization.get("hq_calc_granddaughters", language), granddaughters, 20, "calc_granddaughters_inc", "calc_granddaughters_dec") { granddaughters = it }
                }
            }

            // 5. Siblings
            item {
                HuqooqSectionCard(
                    title = AppLocalization.get("hq_calc_siblings_section", language),
                    icon = Icons.Default.Groups
                ) {
                    HeirStepperRow(AppLocalization.get("hq_calc_full_brothers", language), fullBrothers, 20, "calc_fb_inc", "calc_fb_dec") { fullBrothers = it }
                    HeirStepperRow(AppLocalization.get("hq_calc_full_sisters", language), fullSisters, 20, "calc_fs_inc", "calc_fs_dec") { fullSisters = it }
                    HeirStepperRow(AppLocalization.get("hq_calc_pat_brothers", language), consBrothers, 20, "calc_cb_inc", "calc_cb_dec") { consBrothers = it }
                    HeirStepperRow(AppLocalization.get("hq_calc_pat_sisters", language), consSisters, 20, "calc_cs_inc", "calc_cs_dec") { consSisters = it }
                    HeirStepperRow(AppLocalization.get("hq_calc_uterine_siblings", language), uterineSiblings, 20, "calc_ut_inc", "calc_ut_dec") { uterineSiblings = it }
                }
            }

            // Calculate / Reset Action Row
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { resetAll() },
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.widthIn(max = 130.dp).height(48.dp).testTag("calc_reset_btn")
                    ) {
                        Text(
                            text = AppLocalization.get("hq_calc_reset", language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Button(
                        onClick = { runCalculate() },
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.weight(1f).height(48.dp).testTag("calc_calculate_btn")
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppLocalization.get("hq_calc_calculate", language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Results Section
            result?.let { res ->
                item {
                    CalculationResultSection(res = res, language = language)
                }
            }
        }
    }
}

@Composable
private fun CalculationResultSection(
    res: InheritanceCalculationResult,
    language: AppLanguage
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("calc_result_card")
    ) {
        if (res.status != CalculatorStatus.SUCCESS) {
            val isReview = res.status == CalculatorStatus.SCHOLARLY_REVIEW_REQUIRED
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth().testTag("calc_error_or_review_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isReview) {
                        HuqooqEvidenceBadge(HuqooqEvidenceType.MUFTI_REVIEW_REQUIRED, language)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        text = res.errorOrReviewMessage?.get(language) ?: res.specialRuleApplied,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            return
        }

        // Net Estate & Rule Summary
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppLocalization.get("hq_calc_net_estate", language),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = formatMoney(res.netDistributableEstate),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.testTag("calc_net_estate_value")
                )
                Spacer(modifier = Modifier.height(6.dp))
                AssistChip(
                    onClick = {},
                    label = {
                        Text("${AppLocalization.get("hq_calc_rule_applied", language)}: ${res.specialRuleApplied}")
                    }
                )
            }
        }

        // Heir Shares Breakdown
        res.shares.forEach { share ->
            OutlinedCard(
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth().testTag("calc_share_${share.heirKey}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${share.label.get(language)} (×${share.count})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "${share.aggregateFraction.formattedFraction()} (${String.format(Locale.US, "%.2f", share.aggregatePercentage)}%)",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${share.shareRole.get(language)} • ${share.quranReference}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = AppLocalization.get("hq_calc_total_group", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formatMoney(share.aggregateAmount),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        if (share.count > 1) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${AppLocalization.get("hq_calc_each_person", language)} (${share.perPersonFraction.formattedFraction()})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = formatMoney(share.perPersonAmount),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = share.explanation.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Excluded Heirs (Hajb)
        if (res.excludedHeirs.isNotEmpty()) {
            HuqooqSectionCard(
                title = AppLocalization.get("hq_calc_excluded_heading", language),
                icon = Icons.Default.Block
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    res.excludedHeirs.forEach { ex ->
                        Text(
                            text = "• ${ex.label.get(language)} (×${ex.count}) — ${AppLocalization.get("hq_calc_excluded_by", language)}: ${ex.excludedBy.get(language)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Step-by-Step Reasoning
        if (res.stepByStepExplanation.isNotEmpty()) {
            HuqooqSectionCard(
                title = AppLocalization.get("hq_calc_steps_heading", language),
                icon = Icons.Default.FormatListNumbered
            ) {
                HuqooqBulletList(res.stepByStepExplanation.map { it.get(language) }, "•")
            }
        }

        // Warnings & Disclaimers
        if (res.warningsAndAssumptions.isNotEmpty()) {
            HuqooqSectionCard(
                title = AppLocalization.get("hq_calc_warnings_heading", language),
                icon = Icons.Default.Info
            ) {
                HuqooqBulletList(res.warningsAndAssumptions.map { it.get(language) }, "!")
            }
        }
    }
}

@Composable
private fun MoneyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    tag: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth().testTag(tag)
    )
}

@Composable
private fun HeirSwitchRow(
    label: String,
    checked: Boolean,
    tag: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(tag)
        )
    }
}

@Composable
private fun HeirStepperRow(
    label: String,
    count: Int,
    max: Int,
    incTag: String,
    decTag: String,
    onChanged: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (count > 0) onChanged(count - 1) },
                enabled = count > 0,
                modifier = Modifier.minimumInteractiveComponentSize().testTag(decTag)
            ) {
                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Decrease $label")
            }
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
                onClick = { if (count < max) onChanged(count + 1) },
                enabled = count < max,
                modifier = Modifier.minimumInteractiveComponentSize().testTag(incTag)
            ) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = "Increase $label")
            }
        }
    }
}

private fun formatMoney(amount: Double): String =
    String.format(Locale.US, "%,.2f", amount)
