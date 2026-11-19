package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.zakat.DebtDeductionMethodology
import com.example.data.zakat.GoldPurity
import com.example.data.zakat.InvestmentMethodology
import com.example.data.zakat.JewelleryMethodology
import com.example.data.zakat.ReceivableMethodology
import com.example.data.zakat.SilverPurity
import com.example.localization.AppLocalization

@Composable
fun ZakatExpandableCategoryCard(
    title: String,
    subtotalText: String? = null,
    initiallyExpanded: Boolean = false,
    testTag: String,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!subtotalText.isNullOrBlank()) {
                        Text(
                            text = subtotalText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    content()
                }
            }
        }
    }
}

@Composable
fun ZakatNumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    suffixText: String? = null,
    placeholder: String = "0",
    testTag: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, maxLines = 2, overflow = TextOverflow.Ellipsis) },
        placeholder = { Text(placeholder) },
        suffix = suffixText?.let { s -> { Text(s, style = MaterialTheme.typography.labelMedium) } },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoldSectionContent(
    language: AppLanguage,
    goldGrams: String,
    onGoldGramsChange: (String) -> Unit,
    goldPurity: GoldPurity,
    onGoldPurityChange: (GoldPurity) -> Unit,
    customGoldPct: String,
    onCustomGoldPctChange: (String) -> Unit,
    isPersonalWear: Boolean,
    onIsPersonalWearChange: (Boolean) -> Unit,
    jewelleryMethodology: JewelleryMethodology,
    onJewelleryMethodologyChange: (JewelleryMethodology) -> Unit
) {
    ZakatNumberField(
        label = AppLocalization.get("zakat_gold_weight_g", language),
        value = goldGrams,
        onValueChange = onGoldGramsChange,
        suffixText = "g",
        testTag = "gold_input"
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        GoldPurity.entries.forEach { purity ->
            FilterChip(
                selected = goldPurity == purity,
                onClick = { onGoldPurityChange(purity) },
                label = { Text(AppLocalization.get(purity.labelKey, language)) },
                colors = noorSelectableChipColors(),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("gold_purity_${purity.name}")
            )
        }
    }
    if (goldPurity == GoldPurity.CUSTOM) {
        ZakatNumberField(
            label = AppLocalization.get("zakat_custom_purity_pct", language),
            value = customGoldPct,
            onValueChange = onCustomGoldPctChange,
            suffixText = "%",
            placeholder = "91.67",
            testTag = "gold_custom_purity_input"
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Text(
            text = AppLocalization.get("zakat_personal_jewellery_switch", language),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        )
        Switch(
            checked = isPersonalWear,
            onCheckedChange = onIsPersonalWearChange,
            modifier = Modifier.testTag("gold_personal_wear_switch")
        )
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = jewelleryMethodology == JewelleryMethodology.ZAKATABLE_ALL_JEWELLERY,
            onClick = { onJewelleryMethodologyChange(JewelleryMethodology.ZAKATABLE_ALL_JEWELLERY) },
            label = { Text(AppLocalization.get("zakat_jewellery_method_all", language)) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.heightIn(min = 48.dp)
        )
        FilterChip(
            selected = jewelleryMethodology == JewelleryMethodology.EXEMPT_PERSONAL_USE,
            onClick = { onJewelleryMethodologyChange(JewelleryMethodology.EXEMPT_PERSONAL_USE) },
            label = { Text(AppLocalization.get("zakat_jewellery_method_exempt", language)) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.heightIn(min = 48.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SilverSectionContent(
    language: AppLanguage,
    silverGrams: String,
    onSilverGramsChange: (String) -> Unit,
    silverPurity: SilverPurity,
    onSilverPurityChange: (SilverPurity) -> Unit,
    customSilverPct: String,
    onCustomSilverPctChange: (String) -> Unit,
    isPersonalWear: Boolean,
    onIsPersonalWearChange: (Boolean) -> Unit
) {
    ZakatNumberField(
        label = AppLocalization.get("zakat_silver_weight_g", language),
        value = silverGrams,
        onValueChange = onSilverGramsChange,
        suffixText = "g",
        testTag = "silver_input"
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SilverPurity.entries.forEach { purity ->
            FilterChip(
                selected = silverPurity == purity,
                onClick = { onSilverPurityChange(purity) },
                label = { Text(AppLocalization.get(purity.labelKey, language)) },
                colors = noorSelectableChipColors(),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("silver_purity_${purity.name}")
            )
        }
    }
    if (silverPurity == SilverPurity.CUSTOM) {
        ZakatNumberField(
            label = AppLocalization.get("zakat_custom_purity_pct", language),
            value = customSilverPct,
            onValueChange = onCustomSilverPctChange,
            suffixText = "%",
            placeholder = "92.5",
            testTag = "silver_custom_purity_input"
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Text(
            text = AppLocalization.get("zakat_personal_jewellery_switch", language),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        )
        Switch(
            checked = isPersonalWear,
            onCheckedChange = onIsPersonalWearChange,
            modifier = Modifier.testTag("silver_personal_wear_switch")
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InvestmentsAndReceivablesGuidance(
    language: AppLanguage,
    investmentMethodology: InvestmentMethodology,
    onInvestmentMethodologyChange: (InvestmentMethodology) -> Unit,
    customInvPortionPct: String,
    onCustomInvPortionPctChange: (String) -> Unit,
    currencyCode: String,
    sharesValue: String,
    onSharesValueChange: (String) -> Unit,
    fundsValue: String,
    onFundsValueChange: (String) -> Unit,
    otherInvValue: String,
    onOtherInvValueChange: (String) -> Unit
) {
    ZakatNumberField(
        label = AppLocalization.get("zakat_inv_shares", language),
        value = sharesValue,
        onValueChange = onSharesValueChange,
        suffixText = currencyCode,
        testTag = "investments_input"
    )
    ZakatNumberField(
        label = AppLocalization.get("zakat_inv_funds", language),
        value = fundsValue,
        onValueChange = onFundsValueChange,
        suffixText = currencyCode,
        testTag = "inv_funds_input"
    )
    ZakatNumberField(
        label = AppLocalization.get("zakat_inv_other", language),
        value = otherInvValue,
        onValueChange = onOtherInvValueChange,
        suffixText = currencyCode,
        testTag = "inv_other_input"
    )
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = investmentMethodology == InvestmentMethodology.FULL_MARKET_VALUE,
            onClick = { onInvestmentMethodologyChange(InvestmentMethodology.FULL_MARKET_VALUE) },
            label = { Text(AppLocalization.get("zakat_inv_method_full", language)) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.heightIn(min = 48.dp)
        )
        FilterChip(
            selected = investmentMethodology == InvestmentMethodology.USER_ZAKATABLE_PORTION,
            onClick = { onInvestmentMethodologyChange(InvestmentMethodology.USER_ZAKATABLE_PORTION) },
            label = { Text(AppLocalization.get("zakat_inv_method_portion", language)) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.heightIn(min = 48.dp)
        )
    }
    if (investmentMethodology == InvestmentMethodology.USER_ZAKATABLE_PORTION) {
        ZakatNumberField(
            label = AppLocalization.get("zakat_inv_portion_pct", language),
            value = customInvPortionPct,
            onValueChange = onCustomInvPortionPctChange,
            suffixText = "%",
            placeholder = "30",
            testTag = "inv_custom_portion_pct_input"
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReceivablesSectionContent(
    language: AppLanguage,
    currencyCode: String,
    strongRec: String,
    onStrongRecChange: (String) -> Unit,
    doubtfulRec: String,
    onDoubtfulRecChange: (String) -> Unit,
    unrecoverableRec: String,
    onUnrecoverableRecChange: (String) -> Unit,
    receivableMethodology: ReceivableMethodology,
    onReceivableMethodologyChange: (ReceivableMethodology) -> Unit
) {
    ZakatNumberField(
        label = AppLocalization.get("zakat_rec_strong", language),
        value = strongRec,
        onValueChange = onStrongRecChange,
        suffixText = currencyCode,
        testTag = "receivables_input"
    )
    ZakatNumberField(
        label = AppLocalization.get("zakat_rec_doubtful", language),
        value = doubtfulRec,
        onValueChange = onDoubtfulRecChange,
        suffixText = currencyCode,
        testTag = "rec_doubtful_input"
    )
    ZakatNumberField(
        label = AppLocalization.get("zakat_rec_unrecoverable", language),
        value = unrecoverableRec,
        onValueChange = onUnrecoverableRecChange,
        suffixText = currencyCode,
        testTag = "rec_unrecoverable_input"
    )
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = receivableMethodology == ReceivableMethodology.STRONG_ONLY_EXCLUDE_DOUBTFUL,
            onClick = { onReceivableMethodologyChange(ReceivableMethodology.STRONG_ONLY_EXCLUDE_DOUBTFUL) },
            label = { Text(AppLocalization.get("zakat_rec_method_strong_only", language)) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.heightIn(min = 48.dp)
        )
        FilterChip(
            selected = receivableMethodology == ReceivableMethodology.INCLUDE_STRONG_AND_DOUBTFUL,
            onClick = { onReceivableMethodologyChange(ReceivableMethodology.INCLUDE_STRONG_AND_DOUBTFUL) },
            label = { Text(AppLocalization.get("zakat_rec_method_include_doubtful", language)) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.heightIn(min = 48.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LiabilitiesSectionContent(
    language: AppLanguage,
    currencyCode: String,
    overdueDebts: String,
    onOverdueDebtsChange: (String) -> Unit,
    dueBills: String,
    onDueBillsChange: (String) -> Unit,
    shortTermObs: String,
    onShortTermObsChange: (String) -> Unit,
    currentInstallment: String,
    onCurrentInstallmentChange: (String) -> Unit,
    otherLiabilities: String,
    onOtherLiabilitiesChange: (String) -> Unit,
    debtMethodology: DebtDeductionMethodology,
    onDebtMethodologyChange: (DebtDeductionMethodology) -> Unit
) {
    ZakatNumberField(
        label = AppLocalization.get("zakat_debt_overdue", language),
        value = overdueDebts,
        onValueChange = onOverdueDebtsChange,
        suffixText = currencyCode,
        testTag = "liabilities_input"
    )
    ZakatNumberField(
        label = AppLocalization.get("zakat_debt_bills", language),
        value = dueBills,
        onValueChange = onDueBillsChange,
        suffixText = currencyCode,
        testTag = "debt_bills_input"
    )
    ZakatNumberField(
        label = AppLocalization.get("zakat_debt_short_term", language),
        value = shortTermObs,
        onValueChange = onShortTermObsChange,
        suffixText = currencyCode,
        testTag = "debt_short_term_input"
    )
    ZakatNumberField(
        label = AppLocalization.get("zakat_debt_other", language),
        value = otherLiabilities,
        onValueChange = onOtherLiabilitiesChange,
        suffixText = currencyCode,
        testTag = "debt_other_input"
    )
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = debtMethodology == DebtDeductionMethodology.CURRENT_DUE_ONLY,
            onClick = { onDebtMethodologyChange(DebtDeductionMethodology.CURRENT_DUE_ONLY) },
            label = { Text(AppLocalization.get("zakat_debt_method_current", language)) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.heightIn(min = 48.dp)
        )
        FilterChip(
            selected = debtMethodology == DebtDeductionMethodology.SHORT_TERM_AND_CURRENT_INSTALLMENT,
            onClick = { onDebtMethodologyChange(DebtDeductionMethodology.SHORT_TERM_AND_CURRENT_INSTALLMENT) },
            label = { Text(AppLocalization.get("zakat_debt_method_installment", language)) },
            colors = noorSelectableChipColors(),
            modifier = Modifier.heightIn(min = 48.dp)
        )
    }
    if (debtMethodology == DebtDeductionMethodology.SHORT_TERM_AND_CURRENT_INSTALLMENT) {
        ZakatNumberField(
            label = AppLocalization.get("zakat_debt_installment", language),
            value = currentInstallment,
            onValueChange = onCurrentInstallmentChange,
            suffixText = currencyCode,
            testTag = "debt_installment_input"
        )
    }
    Text(
        text = AppLocalization.get("zakat_debt_exclusion_note", language),
        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
