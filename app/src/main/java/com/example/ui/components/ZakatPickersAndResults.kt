package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.data.model.NisabType
import com.example.data.zakat.CountryRegionInfo
import com.example.data.zakat.GlobalZakatResult
import com.example.data.zakat.IsoCurrencyInfo
import com.example.data.zakat.ZakatCurrencyCatalog
import com.example.data.zakat.ZakatCurrencyFormatter
import com.example.data.zakat.ZakatEligibilityReason
import com.example.localization.AppLocalization
import java.math.BigDecimal

@Composable
fun ZakatCountryPickerDialog(
    language: AppLanguage,
    selectedCountry: CountryRegionInfo,
    deviceSuggestedCountry: CountryRegionInfo,
    onSelectCountry: (CountryRegionInfo) -> Unit,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filteredCountries = remember(query) { ZakatCurrencyCatalog.searchCountries(query) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.82f)
                .widthIn(max = 560.dp)
                .testTag("zakat_country_picker_dialog")
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = AppLocalization.get("zakat_country_label", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                            .testTag("close_country_picker_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppLocalization.get("cancel", language)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            text = AppLocalization.get("zakat_search_country", language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = { query = "" },
                                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = AppLocalization.get("cancel", language)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("zakat_country_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Device-detected suggestion chip
                AssistChip(
                    onClick = { onSelectCountry(deviceSuggestedCountry) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "${AppLocalization.get("zakat_device_suggested", language)}: ${deviceSuggestedCountry.flagEmoji} ${deviceSuggestedCountry.displayName}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("zakat_device_country_chip")
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (filteredCountries.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = AppLocalization.get("zakat_no_matches", language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredCountries, key = { it.isoCode }) { country ->
                            val isSelected = country.isoCode == selectedCountry.isoCode
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 52.dp)
                                    .clickable { onSelectCountry(country) }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                                    .testTag("country_item_${country.isoCode}")
                            ) {
                                Text(
                                    text = country.flagEmoji,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = country.displayName,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${country.isoCode} · ${country.legitimateCurrencies.joinToString(", ")}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZakatCurrencyPickerDialog(
    language: AppLanguage,
    selectedCurrency: IsoCurrencyInfo,
    countrySuggestedCodes: List<String>,
    onSelectCurrency: (IsoCurrencyInfo) -> Unit,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filteredCurrencies = remember(query) { ZakatCurrencyCatalog.searchCurrencies(query) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.82f)
                .widthIn(max = 560.dp)
                .testTag("zakat_currency_picker_dialog")
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = AppLocalization.get("zakat_currency_label", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                            .testTag("close_currency_picker_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppLocalization.get("cancel", language)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            text = AppLocalization.get("zakat_search_currency", language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = { query = "" },
                                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = AppLocalization.get("cancel", language)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("zakat_currency_search_input")
                )

                if (countrySuggestedCodes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        countrySuggestedCodes.forEach { code ->
                            ZakatCurrencyCatalog.findCurrency(code)?.let { info ->
                                FilterChip(
                                    selected = info.code == selectedCurrency.code,
                                    onClick = { onSelectCurrency(info) },
                                    label = { Text("${info.code} (${info.symbol})") },
                                    colors = noorSelectableChipColors(),
                                    modifier = Modifier
                                        .heightIn(min = 48.dp)
                                        .testTag("suggested_currency_chip_${info.code}")
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (filteredCurrencies.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = AppLocalization.get("zakat_no_matches", language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredCurrencies, key = { it.code }) { currency ->
                            val isSelected = currency.code == selectedCurrency.code
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 52.dp)
                                    .clickable { onSelectCurrency(currency) }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                                    .testTag("currency_item_${currency.code}")
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.width(64.dp)
                                ) {
                                    Text(
                                        text = currency.code,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${currency.name} (${currency.symbol})",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${currency.regionLabel} · ${currency.decimalDigits} dp",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZakatResultSection(
    result: GlobalZakatResult,
    language: AppLanguage,
    onEditInputs: () -> Unit,
    onRecalculate: () -> Unit,
    onReset: () -> Unit
) {
    val isDue = result.isEligible
    val statusText = when (result.eligibilityReason) {
        ZakatEligibilityReason.ZAKAT_DUE -> AppLocalization.get("zakat_status_due", language)
        ZakatEligibilityReason.BELOW_NISAB -> AppLocalization.get("zakat_status_below_nisab", language)
        ZakatEligibilityReason.ZERO_OR_NEGATIVE_WEALTH -> AppLocalization.get("zakat_status_below_nisab", language)
        ZakatEligibilityReason.HAWL_NOT_COMPLETE -> AppLocalization.get("zakat_status_hawl_incomplete", language)
        ZakatEligibilityReason.HAWL_UNVERIFIED -> AppLocalization.get("zakat_status_hawl_unverified", language)
        ZakatEligibilityReason.PRICE_MISSING -> AppLocalization.get("zakat_status_price_missing", language)
    }

    val bannerBg = if (isDue) {
        MaterialTheme.colorScheme.primaryContainer
    } else if (result.eligibilityReason == ZakatEligibilityReason.HAWL_UNVERIFIED ||
        result.eligibilityReason == ZakatEligibilityReason.PRICE_MISSING
    ) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val bannerFg = if (isDue) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else if (result.eligibilityReason == ZakatEligibilityReason.HAWL_UNVERIFIED ||
        result.eligibilityReason == ZakatEligibilityReason.PRICE_MISSING
    ) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("zakat_result_section")
    ) {
        // Eligibility & Payable Highlight Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = bannerBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isDue) Icons.Default.Verified else Icons.Default.Warning,
                        contentDescription = null,
                        tint = bannerFg
                    )
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = bannerFg,
                        modifier = Modifier.testTag("zakat_eligibility_status_text")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = AppLocalization.get("zakat_res_payable", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = bannerFg.copy(alpha = 0.85f)
                )
                Text(
                    text = ZakatCurrencyFormatter.formatAmount(result.zakatPayable, result.currencyCode),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = bannerFg,
                    modifier = Modifier.testTag("zakat_payable_amount_text")
                )

                if (result.hawlEvaluation.anniversaryGregorianIso.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${AppLocalization.get("zakat_hawl_anniversary_label", language)}: ${result.hawlEvaluation.anniversaryGregorianIso} (${result.hawlEvaluation.anniversaryHijriFormatted})",
                        style = MaterialTheme.typography.bodySmall,
                        color = bannerFg.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Itemized Transparent Breakdown Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = AppLocalization.get("zakat_res_title", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                BreakdownRow(
                    AppLocalization.get("zakat_sec_cash", language),
                    ZakatCurrencyFormatter.formatAmount(result.cashAndBankTotal, result.currencyCode)
                )
                if (result.pureGoldEquivalentGrams > BigDecimal.ZERO) {
                    BreakdownRow(
                        "${AppLocalization.get("zakat_res_pure_gold_g", language)}",
                        "${ZakatCurrencyFormatter.formatCompactNumber(result.pureGoldEquivalentGrams, 2)} g"
                    )
                }
                BreakdownRow(
                    AppLocalization.get("zakat_sec_gold", language),
                    ZakatCurrencyFormatter.formatAmount(result.goldValuation, result.currencyCode)
                )
                if (result.pureSilverEquivalentGrams > BigDecimal.ZERO) {
                    BreakdownRow(
                        AppLocalization.get("zakat_res_pure_silver_g", language),
                        "${ZakatCurrencyFormatter.formatCompactNumber(result.pureSilverEquivalentGrams, 2)} g"
                    )
                }
                BreakdownRow(
                    AppLocalization.get("zakat_sec_silver", language),
                    ZakatCurrencyFormatter.formatAmount(result.silverValuation, result.currencyCode)
                )
                BreakdownRow(
                    AppLocalization.get("zakat_sec_business", language),
                    ZakatCurrencyFormatter.formatAmount(result.businessAssetsTotal, result.currencyCode)
                )
                BreakdownRow(
                    AppLocalization.get("zakat_sec_investments", language),
                    ZakatCurrencyFormatter.formatAmount(result.qualifyingInvestmentsTotal, result.currencyCode)
                )
                BreakdownRow(
                    AppLocalization.get("zakat_sec_receivables", language),
                    ZakatCurrencyFormatter.formatAmount(result.eligibleReceivablesTotal, result.currencyCode)
                )
                if (result.excludedReceivablesTotal > BigDecimal.ZERO) {
                    BreakdownRow(
                        AppLocalization.get("zakat_res_excluded_rec", language),
                        ZakatCurrencyFormatter.formatAmount(result.excludedReceivablesTotal, result.currencyCode)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                BreakdownRow(
                    AppLocalization.get("zakat_res_gross_assets", language),
                    ZakatCurrencyFormatter.formatAmount(result.totalQualifyingAssets, result.currencyCode),
                    emphasized = true
                )
                BreakdownRow(
                    AppLocalization.get("zakat_res_liabilities", language),
                    "− ${ZakatCurrencyFormatter.formatAmount(result.eligibleDeductibleLiabilities, result.currencyCode)}"
                )
                BreakdownRow(
                    AppLocalization.get("zakat_res_net_wealth", language),
                    ZakatCurrencyFormatter.formatAmount(result.netZakatableWealth, result.currencyCode),
                    emphasized = true
                )

                val nisabLabel = if (result.nisabType == NisabType.GOLD) {
                    "${AppLocalization.get("zakat_res_nisab_threshold", language)} (${result.nisabWeightGrams}g Gold)"
                } else {
                    "${AppLocalization.get("zakat_res_nisab_threshold", language)} (${result.nisabWeightGrams}g Silver)"
                }
                BreakdownRow(
                    nisabLabel,
                    result.nisabThreshold?.let { ZakatCurrencyFormatter.formatAmount(it, result.currencyCode) } ?: "—"
                )

                // Pricing & FX Provenance
                if (result.goldPriceSourceLabel.isNotBlank() || result.silverPriceSourceLabel.isNotBlank() || result.exchangeRateSource.isNotBlank()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    if (result.goldPriceSourceLabel.isNotBlank()) {
                        Text(
                            text = "Gold: ${result.goldPriceSourceLabel} (${result.goldPriceDate})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (result.silverPriceSourceLabel.isNotBlank()) {
                        Text(
                            text = "Silver: ${result.silverPriceSourceLabel} (${result.silverPriceDate})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (result.exchangeRateUsdToSelected != null && result.currencyCode != "USD") {
                        Text(
                            text = "${AppLocalization.get("zakat_fx_rate_used", language)}: 1 USD = ${ZakatCurrencyFormatter.formatCompactNumber(result.exchangeRateUsdToSelected, 4)} ${result.currencyCode} (${result.exchangeRateSource})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = AppLocalization.get("zakat_res_formula", language),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Action Buttons: Edit Inputs / Recalculate / Reset
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onEditInputs,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
                    .testTag("zakat_edit_inputs_button")
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = AppLocalization.get("zakat_edit_inputs", language),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            OutlinedButton(
                onClick = onRecalculate,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
                    .testTag("zakat_recalculate_button")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = AppLocalization.get("zakat_recalculate", language),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            TextButton(
                onClick = onReset,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                modifier = Modifier
                    .widthIn(max = 110.dp)
                    .heightIn(min = 48.dp)
                    .testTag("zakat_reset_button")
            ) {
                Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = AppLocalization.get("zakat_reset", language),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Scholarly Methodology Differences + Quran 9:60 Guidance
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppLocalization.get("zakat_scholarly_diffs_title", language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Text(
                    text = AppLocalization.get("zakat_scholarly_diffs_body", language),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Text(
                    text = AppLocalization.get("zakat_tawbah_note", language),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun BreakdownRow(
    label: String,
    value: String,
    emphasized: Boolean = false
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal
            ),
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}
