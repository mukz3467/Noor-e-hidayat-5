package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.NisabType
import com.example.data.zakat.DebtDeductionMethodology
import com.example.data.zakat.GlobalZakatInput
import com.example.data.zakat.GlobalZakatResult
import com.example.data.zakat.GoldPurity
import com.example.data.zakat.InvestmentMethodology
import com.example.data.zakat.JewelleryMethodology
import com.example.data.zakat.MetalsSpotQuote
import com.example.data.zakat.ReceivableMethodology
import com.example.data.zakat.SilverPurity
import com.example.data.zakat.ZakatCalculationEngine
import com.example.data.zakat.ZakatCurrencyCatalog
import com.example.data.zakat.ZakatCurrencyFormatter
import com.example.data.zakat.ZakatMarketDataRepository
import com.example.localization.AppLocalization
import com.example.ui.components.GoldSectionContent
import com.example.ui.components.InvestmentsAndReceivablesGuidance
import com.example.ui.components.LiabilitiesSectionContent
import com.example.ui.components.NoorTopBar
import com.example.ui.components.ReceivablesSectionContent
import com.example.ui.components.SilverSectionContent
import com.example.ui.components.ZakatCountryPickerDialog
import com.example.ui.components.ZakatCurrencyPickerDialog
import com.example.ui.components.ZakatExpandableCategoryCard
import com.example.ui.components.ZakatNumberField
import com.example.ui.components.ZakatResultSection
import com.example.ui.components.noorSelectableChipColors
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZakatScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val deviceSuggestedCountry = remember { ZakatCurrencyCatalog.detectDeviceSuggestedCountry() }
    val persistedPair = remember { ZakatCurrencyCatalog.loadPersistedSelection(context) }

    var selectedCountry by remember { mutableStateOf(persistedPair.first) }
    var selectedCurrency by remember { mutableStateOf(persistedPair.second) }
    var showCountryPicker by remember { mutableStateOf(false) }
    var showCurrencyPicker by remember { mutableStateOf(false) }

    // Market spot prices & FX state
    var isFetchingMarket by remember { mutableStateOf(false) }
    var spotQuote by remember { mutableStateOf<MetalsSpotQuote?>(null) }
    var manualExchangeRateText by remember { mutableStateOf("") }
    var useManualMetalPrices by remember { mutableStateOf(false) }
    var manualGoldPricePerGramText by remember { mutableStateOf("") }
    var manualSilverPricePerGramText by remember { mutableStateOf("") }

    // 1. Cash & Bank
    var cashAtHome by remember { mutableStateOf("") }
    var currentAccounts by remember { mutableStateOf("") }
    var savingsAccounts by remember { mutableStateOf("") }
    var eligibleDeposits by remember { mutableStateOf("") }
    var otherCash by remember { mutableStateOf("") }

    // 2. Gold
    var goldGrams by remember { mutableStateOf("") }
    var goldPurity by remember { mutableStateOf(GoldPurity.K24) }
    var customGoldPct by remember { mutableStateOf("91.67") }
    var goldPersonalWear by remember { mutableStateOf(false) }
    var jewelleryMethodology by remember { mutableStateOf(JewelleryMethodology.ZAKATABLE_ALL_JEWELLERY) }

    // 3. Silver
    var silverGrams by remember { mutableStateOf("") }
    var silverPurity by remember { mutableStateOf(SilverPurity.FINE_999) }
    var customSilverPct by remember { mutableStateOf("92.5") }
    var silverPersonalWear by remember { mutableStateOf(false) }

    // 4. Business Liquid Assets
    var businessCash by remember { mutableStateOf("") }
    var resaleInventory by remember { mutableStateOf("") }
    var merchandiseForResale by remember { mutableStateOf("") }
    var tradeReceivables by remember { mutableStateOf("") }
    var otherLiquidBusiness by remember { mutableStateOf("") }

    // 5. Investments
    var sharesValue by remember { mutableStateOf("") }
    var mutualFundsValue by remember { mutableStateOf("") }
    var otherInvestments by remember { mutableStateOf("") }
    var investmentMethodology by remember { mutableStateOf(InvestmentMethodology.FULL_MARKET_VALUE) }
    var customInvestmentPct by remember { mutableStateOf("30") }

    // 6. Receivables
    var strongReceivables by remember { mutableStateOf("") }
    var doubtfulReceivables by remember { mutableStateOf("") }
    var unrecoverableReceivables by remember { mutableStateOf("") }
    var receivableMethodology by remember { mutableStateOf(ReceivableMethodology.STRONG_ONLY_EXCLUDE_DOUBTFUL) }

    // 7. Liabilities
    var overdueDebts by remember { mutableStateOf("") }
    var currentlyDueBills by remember { mutableStateOf("") }
    var shortTermObligations by remember { mutableStateOf("") }
    var currentDueLoanInstallment by remember { mutableStateOf("") }
    var otherLiabilities by remember { mutableStateOf("") }
    var debtMethodology by remember { mutableStateOf(DebtDeductionMethodology.CURRENT_DUE_ONLY) }

    // Nisab & Hawl
    var selectedNisab by remember { mutableStateOf(NisabType.SILVER) }
    var hawlConfirmed by remember { mutableStateOf<Boolean?>(null) }
    var hawlStartDateIso by remember { mutableStateOf("") }

    var calculationResult by remember { mutableStateOf<GlobalZakatResult?>(null) }

    fun refreshMarketRates(forceRefresh: Boolean) {
        val manualFx = ZakatCurrencyFormatter.parseDecimalOrNull(manualExchangeRateText)
            ?.takeIf { it > BigDecimal.ZERO }
        coroutineScope.launch {
            isFetchingMarket = true
            spotQuote = ZakatMarketDataRepository.fetchGoldAndSilverPrices(
                context = context,
                targetCurrencyCode = selectedCurrency.code,
                manualExchangeRateOverride = manualFx,
                forceRefresh = forceRefresh
            )
            isFetchingMarket = false
        }
    }

    LaunchedEffect(selectedCurrency.code) {
        refreshMarketRates(forceRefresh = false)
    }

    val effectiveGoldPrice: BigDecimal? = remember(useManualMetalPrices, manualGoldPricePerGramText, spotQuote, manualExchangeRateText, selectedCurrency) {
        if (useManualMetalPrices) {
            ZakatCurrencyFormatter.parseDecimalOrNull(manualGoldPricePerGramText)?.takeIf { it > BigDecimal.ZERO }
        } else {
            val manualFx = ZakatCurrencyFormatter.parseDecimalOrNull(manualExchangeRateText)?.takeIf { it > BigDecimal.ZERO }
            val usdG = spotQuote?.gold24kPerGramUsd
            if (spotQuote?.gold24kPerGramLocal != null) {
                spotQuote?.gold24kPerGramLocal
            } else if (usdG != null && manualFx != null) {
                usdG.multiply(manualFx).setScale(selectedCurrency.decimalDigits, RoundingMode.HALF_UP)
            } else null
        }
    }

    val effectiveSilverPrice: BigDecimal? = remember(useManualMetalPrices, manualSilverPricePerGramText, spotQuote, manualExchangeRateText, selectedCurrency) {
        if (useManualMetalPrices) {
            ZakatCurrencyFormatter.parseDecimalOrNull(manualSilverPricePerGramText)?.takeIf { it > BigDecimal.ZERO }
        } else {
            val manualFx = ZakatCurrencyFormatter.parseDecimalOrNull(manualExchangeRateText)?.takeIf { it > BigDecimal.ZERO }
            val usdG = spotQuote?.silverPurePerGramUsd
            if (spotQuote?.silverPurePerGramLocal != null) {
                spotQuote?.silverPurePerGramLocal
            } else if (usdG != null && manualFx != null) {
                usdG.multiply(manualFx).setScale(selectedCurrency.decimalDigits, RoundingMode.HALF_UP)
            } else null
        }
    }

    fun buildInputAndCalculate(): GlobalZakatResult {
        val manualFx = ZakatCurrencyFormatter.parseDecimalOrNull(manualExchangeRateText)?.takeIf { it > BigDecimal.ZERO }
        val resolvedFx = spotQuote?.exchangeRateUsed?.rate ?: manualFx
        val fxLabel = if (spotQuote?.exchangeRateUsed?.rate != null) {
            spotQuote!!.exchangeRateUsed!!.sourceName
        } else if (manualFx != null) {
            "User-Provided Manual FX"
        } else {
            ""
        }
        val input = GlobalZakatInput(
            countryIsoCode = selectedCountry.isoCode,
            currencyCode = selectedCurrency.code,
            cashAtHome = ZakatCurrencyFormatter.parseDecimalOrZero(cashAtHome),
            currentAccounts = ZakatCurrencyFormatter.parseDecimalOrZero(currentAccounts),
            savingsAccounts = ZakatCurrencyFormatter.parseDecimalOrZero(savingsAccounts),
            eligibleDeposits = ZakatCurrencyFormatter.parseDecimalOrZero(eligibleDeposits),
            otherCashEquivalents = ZakatCurrencyFormatter.parseDecimalOrZero(otherCash),
            goldGrams = ZakatCurrencyFormatter.parseDecimalOrZero(goldGrams),
            goldPurity = goldPurity,
            customGoldPurityPercent = ZakatCurrencyFormatter.parseDecimalOrNull(customGoldPct) ?: BigDecimal("100"),
            gold24kPricePerGram = effectiveGoldPrice,
            goldPriceSourceLabel = if (useManualMetalPrices) "Manual Local Retail Price" else (spotQuote?.sourceName ?: ""),
            goldPriceDate = if (useManualMetalPrices) "Manual" else (spotQuote?.updatedAtText?.take(10) ?: ""),
            isGoldPriceManual = useManualMetalPrices,
            goldJewelleryIsPersonalWearOnly = goldPersonalWear,
            jewelleryMethodology = jewelleryMethodology,
            silverGrams = ZakatCurrencyFormatter.parseDecimalOrZero(silverGrams),
            silverPurity = silverPurity,
            customSilverPurityPercent = ZakatCurrencyFormatter.parseDecimalOrNull(customSilverPct) ?: BigDecimal("100"),
            silverPurePricePerGram = effectiveSilverPrice,
            silverPriceSourceLabel = if (useManualMetalPrices) "Manual Local Retail Price" else (spotQuote?.sourceName ?: ""),
            silverPriceDate = if (useManualMetalPrices) "Manual" else (spotQuote?.updatedAtText?.take(10) ?: ""),
            isSilverPriceManual = useManualMetalPrices,
            silverJewelleryIsPersonalWearOnly = silverPersonalWear,
            businessCash = ZakatCurrencyFormatter.parseDecimalOrZero(businessCash),
            resaleInventory = ZakatCurrencyFormatter.parseDecimalOrZero(resaleInventory),
            merchandiseForResale = ZakatCurrencyFormatter.parseDecimalOrZero(merchandiseForResale),
            tradeReceivables = ZakatCurrencyFormatter.parseDecimalOrZero(tradeReceivables),
            otherLiquidBusinessAssets = ZakatCurrencyFormatter.parseDecimalOrZero(otherLiquidBusiness),
            sharesMarketValue = ZakatCurrencyFormatter.parseDecimalOrZero(sharesValue),
            mutualFundsValue = ZakatCurrencyFormatter.parseDecimalOrZero(mutualFundsValue),
            otherQualifyingInvestments = ZakatCurrencyFormatter.parseDecimalOrZero(otherInvestments),
            investmentMethodology = investmentMethodology,
            userSpecifiedInvestmentZakatablePercent = ZakatCurrencyFormatter.parseDecimalOrNull(customInvestmentPct),
            strongReceivables = ZakatCurrencyFormatter.parseDecimalOrZero(strongReceivables),
            doubtfulReceivables = ZakatCurrencyFormatter.parseDecimalOrZero(doubtfulReceivables),
            unrecoverableReceivables = ZakatCurrencyFormatter.parseDecimalOrZero(unrecoverableReceivables),
            receivableMethodology = receivableMethodology,
            overdueDebts = ZakatCurrencyFormatter.parseDecimalOrZero(overdueDebts),
            currentlyDueBills = ZakatCurrencyFormatter.parseDecimalOrZero(currentlyDueBills),
            shortTermObligations = ZakatCurrencyFormatter.parseDecimalOrZero(shortTermObligations),
            currentDueLoanRepayments = ZakatCurrencyFormatter.parseDecimalOrZero(currentDueLoanInstallment),
            otherQualifyingLiabilities = ZakatCurrencyFormatter.parseDecimalOrZero(otherLiabilities),
            debtMethodology = debtMethodology,
            nisabType = selectedNisab,
            hawlConfirmedByUser = hawlConfirmed,
            hawlStartGregorianIso = hawlStartDateIso,
            exchangeRateSource = fxLabel,
            exchangeRateDate = spotQuote?.exchangeRateUsed?.rateDate ?: "",
            originalMetalPriceUsdPerGramGold = spotQuote?.gold24kPerGramUsd,
            originalMetalPriceUsdPerGramSilver = spotQuote?.silverPurePerGramUsd,
            exchangeRateUsdToSelected = resolvedFx
        )
        return ZakatCalculationEngine.calculate(input, language)
    }

    fun resetAllInputs() {
        cashAtHome = ""; currentAccounts = ""; savingsAccounts = ""; eligibleDeposits = ""; otherCash = ""
        goldGrams = ""; goldPurity = GoldPurity.K24; customGoldPct = "91.67"; goldPersonalWear = false
        silverGrams = ""; silverPurity = SilverPurity.FINE_999; customSilverPct = "92.5"; silverPersonalWear = false
        businessCash = ""; resaleInventory = ""; merchandiseForResale = ""; tradeReceivables = ""; otherLiquidBusiness = ""
        sharesValue = ""; mutualFundsValue = ""; otherInvestments = ""; customInvestmentPct = "30"
        strongReceivables = ""; doubtfulReceivables = ""; unrecoverableReceivables = ""
        overdueDebts = ""; currentlyDueBills = ""; shortTermObligations = ""; currentDueLoanInstallment = ""; otherLiabilities = ""
        hawlConfirmed = null; hawlStartDateIso = ""; calculationResult = null
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("zakat", language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(16.dp)
                .widthIn(max = 640.dp)
        ) {
            Text(
                text = AppLocalization.get("zakat_global_subtitle", language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 1. Independent Country & Currency Selector Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { showCountryPicker = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 52.dp)
                                .testTag("zakat_country_button")
                        ) {
                            Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${selectedCountry.flagEmoji} ${selectedCountry.displayName}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        OutlinedButton(
                            onClick = { showCurrencyPicker = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 52.dp)
                                .testTag("zakat_currency_button")
                        ) {
                            Icon(Icons.Default.CurrencyExchange, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${selectedCurrency.code} (${selectedCurrency.symbol})",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (selectedCountry.primaryCurrencyCode != selectedCurrency.code) {
                        AssistChip(
                            onClick = {
                                val suggested = ZakatCurrencyCatalog.getCurrencyOrDefault(selectedCountry.primaryCurrencyCode)
                                selectedCurrency = suggested
                                ZakatCurrencyCatalog.saveSelectedCurrency(context, suggested.code)
                                manualExchangeRateText = ""
                            },
                            label = {
                                Text("${AppLocalization.get("zakat_use_country_currency", language)}: ${selectedCountry.primaryCurrencyCode}")
                            },
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("use_country_currency_chip")
                        )
                    }
                }
            }

            // 2. Live Metals Spot & Frankfurter FX Card (with manual local price fallback)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppLocalization.get("zakat_market_prices_title", language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = if (useManualMetalPrices) {
                                    AppLocalization.get("zakat_manual_price_badge", language)
                                } else {
                                    spotQuote?.sourceName?.ifBlank { AppLocalization.get("zakat_metal_auto_badge", language) }
                                        ?: AppLocalization.get("zakat_metal_auto_badge", language)
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        FilledTonalButton(
                            onClick = { refreshMarketRates(forceRefresh = true) },
                            enabled = !isFetchingMarket,
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("fetch_live_rates_button")
                        ) {
                            if (isFetchingMarket) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AppLocalization.get("zakat_fetch_live_rates", language), maxLines = 1)
                        }
                    }

                    spotQuote?.let { sq ->
                        if (sq.gold24kPerGramUsd != null && sq.silverPurePerGramUsd != null) {
                            Text(
                                text = "${AppLocalization.get("zakat_spot_original_usd", language)}: Gold $${ZakatCurrencyFormatter.formatCompactNumber(sq.gold24kPerGramUsd, 2)}/g · Silver $${ZakatCurrencyFormatter.formatCompactNumber(sq.silverPurePerGramUsd, 2)}/g",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        sq.exchangeRateUsed?.rate?.let { fx ->
                            if (selectedCurrency.code != "USD") {
                                Text(
                                    text = "${AppLocalization.get("zakat_fx_rate_used", language)}: 1 USD = ${ZakatCurrencyFormatter.formatCompactNumber(fx, 4)} ${selectedCurrency.code} (${sq.exchangeRateUsed.rateDate})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (sq.isStale) {
                            Text(
                                text = AppLocalization.get("zakat_stale_warning", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        if (!useManualMetalPrices && (sq.gold24kPerGramLocal == null || sq.silverPurePerGramLocal == null)) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = AppLocalization.get("zakat_fx_unsupported_notice", language),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                            if (selectedCurrency.code != "USD") {
                                ZakatNumberField(
                                    label = "${AppLocalization.get("zakat_manual_rate_label", language)} (USD → ${selectedCurrency.code})",
                                    value = manualExchangeRateText,
                                    onValueChange = { manualExchangeRateText = it },
                                    placeholder = "e.g. 278.50",
                                    testTag = "manual_fx_rate_input"
                                )
                            }
                        }
                    }

                    if (effectiveGoldPrice != null && effectiveSilverPrice != null) {
                        Text(
                            text = "24K Gold: ${ZakatCurrencyFormatter.formatAmount(effectiveGoldPrice, selectedCurrency.code)}/g  |  Pure Silver: ${ZakatCurrencyFormatter.formatAmount(effectiveSilverPrice, selectedCurrency.code)}/g",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) {
                        Text(
                            text = AppLocalization.get("zakat_metal_manual_toggle", language),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        Switch(
                            checked = useManualMetalPrices,
                            onCheckedChange = { useManualMetalPrices = it },
                            modifier = Modifier.testTag("manual_metal_prices_switch")
                        )
                    }

                    if (useManualMetalPrices) {
                        ZakatNumberField(
                            label = "${AppLocalization.get("zakat_manual_gold_label", language)} (${selectedCurrency.code}/g)",
                            value = manualGoldPricePerGramText,
                            onValueChange = { manualGoldPricePerGramText = it },
                            suffixText = "${selectedCurrency.code}/g",
                            testTag = "manual_gold_price_input"
                        )
                        ZakatNumberField(
                            label = "${AppLocalization.get("zakat_manual_silver_label", language)} (${selectedCurrency.code}/g)",
                            value = manualSilverPricePerGramText,
                            onValueChange = { manualSilverPricePerGramText = it },
                            suffixText = "${selectedCurrency.code}/g",
                            testTag = "manual_silver_price_input"
                        )
                    }
                }
            }

            // 3. Nisab & Hawl Controls Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = AppLocalization.get("zakat_nisab_section", language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = selectedNisab == NisabType.SILVER,
                            onClick = { selectedNisab = NisabType.SILVER },
                            label = { Text(AppLocalization.get("zakat_silver_nisab", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.heightIn(min = 48.dp).testTag("nisab_silver_chip")
                        )
                        FilterChip(
                            selected = selectedNisab == NisabType.GOLD,
                            onClick = { selectedNisab = NisabType.GOLD },
                            label = { Text(AppLocalization.get("zakat_gold_nisab", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.heightIn(min = 48.dp).testTag("nisab_gold_chip")
                        )
                    }
                    Text(
                        text = if (selectedNisab == NisabType.SILVER) {
                            AppLocalization.get("zakat_nisab_silver_note", language)
                        } else {
                            AppLocalization.get("zakat_nisab_gold_note", language)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        text = AppLocalization.get("zakat_hawl_section", language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = AppLocalization.get("zakat_hawl_question", language),
                        style = MaterialTheme.typography.bodySmall
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = hawlConfirmed == true,
                            onClick = { hawlConfirmed = true },
                            label = { Text(AppLocalization.get("zakat_hawl_yes", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.heightIn(min = 48.dp).testTag("hawl_yes_chip")
                        )
                        FilterChip(
                            selected = hawlConfirmed == false,
                            onClick = { hawlConfirmed = false },
                            label = { Text(AppLocalization.get("zakat_hawl_no", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.heightIn(min = 48.dp).testTag("hawl_no_chip")
                        )
                        FilterChip(
                            selected = hawlConfirmed == null,
                            onClick = { hawlConfirmed = null },
                            label = { Text(AppLocalization.get("zakat_hawl_unknown", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.heightIn(min = 48.dp).testTag("hawl_unknown_chip")
                        )
                    }

                    OutlinedTextField(
                        value = hawlStartDateIso,
                        onValueChange = { hawlStartDateIso = it },
                        label = { Text(AppLocalization.get("zakat_hawl_date_label", language)) },
                        placeholder = { Text("2025-03-01") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("hawl_date_input")
                    )

                    val liveHawlPreview = remember(hawlConfirmed, hawlStartDateIso, language) {
                        if (hawlStartDateIso.trim().length == 10) {
                            ZakatCalculationEngine.evaluateHawl(hawlConfirmed, hawlStartDateIso, uiLanguage = language)
                        } else null
                    }
                    liveHawlPreview?.takeIf { it.anniversaryGregorianIso.isNotBlank() }?.let { prev ->
                        Text(
                            text = "${AppLocalization.get("zakat_hawl_anniversary_label", language)}: ${prev.anniversaryGregorianIso} (${prev.anniversaryHijriFormatted})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 4. Seven Asset & Liability Input Sections
            ZakatExpandableCategoryCard(
                title = AppLocalization.get("zakat_sec_cash", language),
                initiallyExpanded = true,
                testTag = "section_cash"
            ) {
                ZakatNumberField(AppLocalization.get("zakat_cash_hand", language), cashAtHome, { cashAtHome = it }, selectedCurrency.code, testTag = "cash_input")
                ZakatNumberField(AppLocalization.get("zakat_bank_current", language), currentAccounts, { currentAccounts = it }, selectedCurrency.code, testTag = "bank_input")
                ZakatNumberField(AppLocalization.get("zakat_bank_savings", language), savingsAccounts, { savingsAccounts = it }, selectedCurrency.code, testTag = "bank_savings_input")
                ZakatNumberField(AppLocalization.get("zakat_bank_deposits", language), eligibleDeposits, { eligibleDeposits = it }, selectedCurrency.code, testTag = "bank_deposits_input")
                ZakatNumberField(AppLocalization.get("zakat_cash_other", language), otherCash, { otherCash = it }, selectedCurrency.code, testTag = "cash_other_input")
            }

            ZakatExpandableCategoryCard(
                title = AppLocalization.get("zakat_sec_gold", language),
                initiallyExpanded = false,
                testTag = "section_gold"
            ) {
                GoldSectionContent(
                    language = language,
                    goldGrams = goldGrams,
                    onGoldGramsChange = { goldGrams = it },
                    goldPurity = goldPurity,
                    onGoldPurityChange = { goldPurity = it },
                    customGoldPct = customGoldPct,
                    onCustomGoldPctChange = { customGoldPct = it },
                    isPersonalWear = goldPersonalWear,
                    onIsPersonalWearChange = { goldPersonalWear = it },
                    jewelleryMethodology = jewelleryMethodology,
                    onJewelleryMethodologyChange = { jewelleryMethodology = it }
                )
            }

            ZakatExpandableCategoryCard(
                title = AppLocalization.get("zakat_sec_silver", language),
                initiallyExpanded = false,
                testTag = "section_silver"
            ) {
                SilverSectionContent(
                    language = language,
                    silverGrams = silverGrams,
                    onSilverGramsChange = { silverGrams = it },
                    silverPurity = silverPurity,
                    onSilverPurityChange = { silverPurity = it },
                    customSilverPct = customSilverPct,
                    onCustomSilverPctChange = { customSilverPct = it },
                    isPersonalWear = silverPersonalWear,
                    onIsPersonalWearChange = { silverPersonalWear = it }
                )
            }

            ZakatExpandableCategoryCard(
                title = AppLocalization.get("zakat_sec_business", language),
                initiallyExpanded = false,
                testTag = "section_business"
            ) {
                ZakatNumberField(AppLocalization.get("zakat_biz_cash", language), businessCash, { businessCash = it }, selectedCurrency.code, testTag = "biz_cash_input")
                ZakatNumberField(AppLocalization.get("zakat_biz_inventory", language), resaleInventory, { resaleInventory = it }, selectedCurrency.code, testTag = "inventory_input")
                ZakatNumberField(AppLocalization.get("zakat_biz_merchandise", language), merchandiseForResale, { merchandiseForResale = it }, selectedCurrency.code, testTag = "biz_merchandise_input")
                ZakatNumberField(AppLocalization.get("zakat_biz_receivables", language), tradeReceivables, { tradeReceivables = it }, selectedCurrency.code, testTag = "biz_receivables_input")
                ZakatNumberField(AppLocalization.get("zakat_biz_other", language), otherLiquidBusiness, { otherLiquidBusiness = it }, selectedCurrency.code, testTag = "biz_other_input")
                Text(
                    text = AppLocalization.get("zakat_biz_exclusion_note", language),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ZakatExpandableCategoryCard(
                title = AppLocalization.get("zakat_sec_investments", language),
                initiallyExpanded = false,
                testTag = "section_investments"
            ) {
                InvestmentsAndReceivablesGuidance(
                    language = language,
                    investmentMethodology = investmentMethodology,
                    onInvestmentMethodologyChange = { investmentMethodology = it },
                    customInvPortionPct = customInvestmentPct,
                    onCustomInvPortionPctChange = { customInvestmentPct = it },
                    currencyCode = selectedCurrency.code,
                    sharesValue = sharesValue,
                    onSharesValueChange = { sharesValue = it },
                    fundsValue = mutualFundsValue,
                    onFundsValueChange = { mutualFundsValue = it },
                    otherInvValue = otherInvestments,
                    onOtherInvValueChange = { otherInvestments = it }
                )
            }

            ZakatExpandableCategoryCard(
                title = AppLocalization.get("zakat_sec_receivables", language),
                initiallyExpanded = false,
                testTag = "section_receivables"
            ) {
                ReceivablesSectionContent(
                    language = language,
                    currencyCode = selectedCurrency.code,
                    strongRec = strongReceivables,
                    onStrongRecChange = { strongReceivables = it },
                    doubtfulRec = doubtfulReceivables,
                    onDoubtfulRecChange = { doubtfulReceivables = it },
                    unrecoverableRec = unrecoverableReceivables,
                    onUnrecoverableRecChange = { unrecoverableReceivables = it },
                    receivableMethodology = receivableMethodology,
                    onReceivableMethodologyChange = { receivableMethodology = it }
                )
            }

            ZakatExpandableCategoryCard(
                title = AppLocalization.get("zakat_sec_liabilities", language),
                initiallyExpanded = false,
                testTag = "section_liabilities"
            ) {
                LiabilitiesSectionContent(
                    language = language,
                    currencyCode = selectedCurrency.code,
                    overdueDebts = overdueDebts,
                    onOverdueDebtsChange = { overdueDebts = it },
                    dueBills = currentlyDueBills,
                    onDueBillsChange = { currentlyDueBills = it },
                    shortTermObs = shortTermObligations,
                    onShortTermObsChange = { shortTermObligations = it },
                    currentInstallment = currentDueLoanInstallment,
                    onCurrentInstallmentChange = { currentDueLoanInstallment = it },
                    otherLiabilities = otherLiabilities,
                    onOtherLiabilitiesChange = { otherLiabilities = it },
                    debtMethodology = debtMethodology,
                    onDebtMethodologyChange = { debtMethodology = it }
                )
            }

            // Primary Calculate Action Button
            Button(
                onClick = {
                    calculationResult = buildInputAndCalculate()
                    coroutineScope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .testTag("calculate_zakat_button")
            ) {
                Icon(Icons.Default.Calculate, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppLocalization.get("calculate", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Calculation Results Breakdown + Actions
            calculationResult?.let { res ->
                ZakatResultSection(
                    result = res,
                    language = language,
                    onEditInputs = { coroutineScope.launch { scrollState.animateScrollTo(0) } },
                    onRecalculate = { calculationResult = buildInputAndCalculate() },
                    onReset = { resetAllInputs() }
                )
            }
        }
    }

    if (showCountryPicker) {
        ZakatCountryPickerDialog(
            language = language,
            selectedCountry = selectedCountry,
            deviceSuggestedCountry = deviceSuggestedCountry,
            onSelectCountry = { country ->
                selectedCountry = country
                ZakatCurrencyCatalog.saveSelectedCountry(context, country.isoCode)
                showCountryPicker = false
            },
            onDismiss = { showCountryPicker = false }
        )
    }

    if (showCurrencyPicker) {
        ZakatCurrencyPickerDialog(
            language = language,
            selectedCurrency = selectedCurrency,
            countrySuggestedCodes = selectedCountry.legitimateCurrencies,
            onSelectCurrency = { currency ->
                selectedCurrency = currency
                ZakatCurrencyCatalog.saveSelectedCurrency(context, currency.code)
                manualExchangeRateText = ""
                showCurrencyPicker = false
            },
            onDismiss = { showCurrencyPicker = false }
        )
    }
}
