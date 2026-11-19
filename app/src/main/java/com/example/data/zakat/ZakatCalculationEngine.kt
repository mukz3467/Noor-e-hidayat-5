package com.example.data.zakat

import com.example.data.model.AppLanguage
import com.example.data.model.NisabType
import com.example.data.repository.IslamicCalendarRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class GoldPurity(val labelKey: String, val purityRatio: BigDecimal, val karat: Int?) {
    K24("zakat_purity_24k", BigDecimal("1.0000"), 24),
    K22("zakat_purity_22k", BigDecimal("0.9167"), 22),
    K21("zakat_purity_21k", BigDecimal("0.8750"), 21),
    K18("zakat_purity_18k", BigDecimal("0.7500"), 18),
    CUSTOM("zakat_purity_custom", BigDecimal("1.0000"), null)
}

enum class SilverPurity(val labelKey: String, val purityRatio: BigDecimal) {
    FINE_999("zakat_silver_999", BigDecimal("1.0000")),
    STERLING_925("zakat_silver_925", BigDecimal("0.9250")),
    CUSTOM("zakat_purity_custom", BigDecimal("1.0000"))
}

enum class JewelleryMethodology {
    ZAKATABLE_ALL_JEWELLERY, // Hanafi position: all gold/silver jewellery is zakatable
    EXEMPT_PERSONAL_USE      // Shafi'i / Maliki / Hanbali majority: personal-use worn jewellery exempt
}

enum class InvestmentMethodology {
    FULL_MARKET_VALUE,          // Trading shares, funds, liquid investments at 2.5% of full market value
    USER_ZAKATABLE_PORTION      // Long-term equity holdings where user specifies zakatable liquid asset %
}

enum class ReceivableMethodology {
    STRONG_ONLY_EXCLUDE_DOUBTFUL, // Standard: only strong/recoverable debts due back are currently zakatable
    INCLUDE_STRONG_AND_DOUBTFUL   // Conservative: include strong + doubtful receivables (still exclude unrecoverable)
}

enum class DebtDeductionMethodology {
    CURRENT_DUE_ONLY,                 // Overdue debts, due bills & current short-term obligations only
    SHORT_TERM_AND_CURRENT_INSTALLMENT // Plus the current due installment/12-month portion of qualifying non-riba debt
}

enum class HawlStatus {
    CONFIRMED_COMPLETE,
    NOT_YET_COMPLETE,
    UNVERIFIED_MISSING
}

enum class ZakatEligibilityReason {
    ZAKAT_DUE,
    BELOW_NISAB,
    ZERO_OR_NEGATIVE_WEALTH,
    HAWL_NOT_COMPLETE,
    HAWL_UNVERIFIED,
    PRICE_MISSING
}

data class HawlEvaluation(
    val status: HawlStatus,
    val reachedNisabGregorianIso: String,
    val anniversaryGregorianIso: String,
    val anniversaryHijriFormatted: String,
    val lunarDaysElapsed: Long?,
    val remainingLunarDays: Long?,
    val isHijriEstimate: Boolean
)

data class GlobalZakatInput(
    val countryIsoCode: String = "PK",
    val currencyCode: String = "PKR",

    // 1. Cash & Bank Assets
    val cashAtHome: BigDecimal = BigDecimal.ZERO,
    val currentAccounts: BigDecimal = BigDecimal.ZERO,
    val savingsAccounts: BigDecimal = BigDecimal.ZERO,
    val eligibleDeposits: BigDecimal = BigDecimal.ZERO,
    val otherCashEquivalents: BigDecimal = BigDecimal.ZERO,

    // 2. Gold
    val goldGrams: BigDecimal = BigDecimal.ZERO,
    val goldPurity: GoldPurity = GoldPurity.K24,
    val customGoldPurityPercent: BigDecimal = BigDecimal("100"),
    val gold24kPricePerGram: BigDecimal? = null,
    val goldPriceSourceLabel: String = "",
    val goldPriceDate: String = "",
    val isGoldPriceManual: Boolean = false,
    val goldJewelleryIsPersonalWearOnly: Boolean = false,
    val jewelleryMethodology: JewelleryMethodology = JewelleryMethodology.ZAKATABLE_ALL_JEWELLERY,

    // 3. Silver
    val silverGrams: BigDecimal = BigDecimal.ZERO,
    val silverPurity: SilverPurity = SilverPurity.FINE_999,
    val customSilverPurityPercent: BigDecimal = BigDecimal("100"),
    val silverPurePricePerGram: BigDecimal? = null,
    val silverPriceSourceLabel: String = "",
    val silverPriceDate: String = "",
    val isSilverPriceManual: Boolean = false,
    val silverJewelleryIsPersonalWearOnly: Boolean = false,

    // 4. Business Assets (excluding personal/operational fixed assets & machinery)
    val businessCash: BigDecimal = BigDecimal.ZERO,
    val resaleInventory: BigDecimal = BigDecimal.ZERO,
    val merchandiseForResale: BigDecimal = BigDecimal.ZERO,
    val tradeReceivables: BigDecimal = BigDecimal.ZERO,
    val otherLiquidBusinessAssets: BigDecimal = BigDecimal.ZERO,

    // 5. Investments
    val sharesMarketValue: BigDecimal = BigDecimal.ZERO,
    val mutualFundsValue: BigDecimal = BigDecimal.ZERO,
    val otherQualifyingInvestments: BigDecimal = BigDecimal.ZERO,
    val investmentMethodology: InvestmentMethodology = InvestmentMethodology.FULL_MARKET_VALUE,
    val userSpecifiedInvestmentZakatablePercent: BigDecimal? = null,

    // 6. Money Owed to User (Non-business personal/general receivables)
    val strongReceivables: BigDecimal = BigDecimal.ZERO,
    val doubtfulReceivables: BigDecimal = BigDecimal.ZERO,
    val unrecoverableReceivables: BigDecimal = BigDecimal.ZERO,
    val receivableMethodology: ReceivableMethodology = ReceivableMethodology.STRONG_ONLY_EXCLUDE_DOUBTFUL,

    // 7. Deductible Liabilities (excluding future living expenses & non-deductible interest/riba)
    val overdueDebts: BigDecimal = BigDecimal.ZERO,
    val currentlyDueBills: BigDecimal = BigDecimal.ZERO,
    val shortTermObligations: BigDecimal = BigDecimal.ZERO,
    val currentDueLoanRepayments: BigDecimal = BigDecimal.ZERO,
    val otherQualifyingLiabilities: BigDecimal = BigDecimal.ZERO,
    val debtMethodology: DebtDeductionMethodology = DebtDeductionMethodology.CURRENT_DUE_ONLY,

    // 8. Nisab & Hawl
    val nisabType: NisabType = NisabType.SILVER,
    val hawlConfirmedByUser: Boolean? = null, // null = unverified/not answered
    val hawlStartGregorianIso: String = "",
    val evaluationEpochMs: Long = System.currentTimeMillis(),

    // Exchange rate transparency
    val exchangeRateSource: String = "",
    val exchangeRateDate: String = "",
    val originalMetalPriceUsdPerGramGold: BigDecimal? = null,
    val originalMetalPriceUsdPerGramSilver: BigDecimal? = null,
    val exchangeRateUsdToSelected: BigDecimal? = null
)

data class GlobalZakatResult(
    val currencyCode: String,
    val currencyDecimals: Int,
    val cashAndBankTotal: BigDecimal,
    val pureGoldEquivalentGrams: BigDecimal,
    val goldValuation: BigDecimal,
    val pureSilverEquivalentGrams: BigDecimal,
    val silverValuation: BigDecimal,
    val businessAssetsTotal: BigDecimal,
    val qualifyingInvestmentsTotal: BigDecimal,
    val eligibleReceivablesTotal: BigDecimal,
    val excludedReceivablesTotal: BigDecimal,
    val totalQualifyingAssets: BigDecimal,
    val eligibleDeductibleLiabilities: BigDecimal,
    val rawSurplusBeforeFloor: BigDecimal,
    val netZakatableWealth: BigDecimal,
    val nisabType: NisabType,
    val nisabWeightGrams: BigDecimal,
    val nisabUnitPricePerGram: BigDecimal?,
    val nisabThreshold: BigDecimal?,
    val hawlEvaluation: HawlEvaluation,
    val zakatRate: BigDecimal,
    val isEligible: Boolean,
    val eligibilityReason: ZakatEligibilityReason,
    val zakatPayable: BigDecimal,
    val goldPriceSourceLabel: String,
    val goldPriceDate: String,
    val isGoldPriceManual: Boolean,
    val silverPriceSourceLabel: String,
    val silverPriceDate: String,
    val isSilverPriceManual: Boolean,
    val exchangeRateSource: String,
    val exchangeRateDate: String,
    val originalMetalPriceUsdPerGramGold: BigDecimal?,
    val originalMetalPriceUsdPerGramSilver: BigDecimal?,
    val exchangeRateUsdToSelected: BigDecimal?
)

object ZakatCalculationEngine {

    val GOLD_NISAB_GRAMS: BigDecimal = BigDecimal("87.48")
    val SILVER_NISAB_GRAMS: BigDecimal = BigDecimal("612.36")
    val ZAKAT_RATE: BigDecimal = BigDecimal("0.025")
    const val LUNAR_YEAR_DAYS = 354L

    fun calculate(input: GlobalZakatInput, uiLanguage: AppLanguage = AppLanguage.ENGLISH): GlobalZakatResult {
        val currencyInfo = ZakatCurrencyCatalog.getCurrencyOrDefault(input.currencyCode)
        val scale = currencyInfo.decimalDigits

        // 1. Cash & Bank Assets
        val cashTotal = safeNonNeg(input.cashAtHome)
            .add(safeNonNeg(input.currentAccounts))
            .add(safeNonNeg(input.savingsAccounts))
            .add(safeNonNeg(input.eligibleDeposits))
            .add(safeNonNeg(input.otherCashEquivalents))

        // 2. Gold valuation (respecting purity & jewellery scholarly methodology)
        val goldPurityRatio = when (input.goldPurity) {
            GoldPurity.CUSTOM -> clampPercent(input.customGoldPurityPercent).divide(BigDecimal("100"), 6, RoundingMode.HALF_UP)
            else -> input.goldPurity.purityRatio
        }
        val effectiveGoldGrams = if (input.goldJewelleryIsPersonalWearOnly &&
            input.jewelleryMethodology == JewelleryMethodology.EXEMPT_PERSONAL_USE
        ) {
            BigDecimal.ZERO
        } else {
            safeNonNeg(input.goldGrams)
        }
        val pureGoldGrams = effectiveGoldGrams.multiply(goldPurityRatio).setScale(4, RoundingMode.HALF_UP)
        val goldUnitPrice = input.gold24kPricePerGram?.takeIf { it > BigDecimal.ZERO }
        val goldValuation = if (goldUnitPrice != null && pureGoldGrams > BigDecimal.ZERO) {
            pureGoldGrams.multiply(goldUnitPrice).setScale(scale, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP)
        }

        // 3. Silver valuation
        val silverPurityRatio = when (input.silverPurity) {
            SilverPurity.CUSTOM -> clampPercent(input.customSilverPurityPercent).divide(BigDecimal("100"), 6, RoundingMode.HALF_UP)
            else -> input.silverPurity.purityRatio
        }
        val effectiveSilverGrams = if (input.silverJewelleryIsPersonalWearOnly &&
            input.jewelleryMethodology == JewelleryMethodology.EXEMPT_PERSONAL_USE
        ) {
            BigDecimal.ZERO
        } else {
            safeNonNeg(input.silverGrams)
        }
        val pureSilverGrams = effectiveSilverGrams.multiply(silverPurityRatio).setScale(4, RoundingMode.HALF_UP)
        val silverUnitPrice = input.silverPurePricePerGram?.takeIf { it > BigDecimal.ZERO }
        val silverValuation = if (silverUnitPrice != null && pureSilverGrams > BigDecimal.ZERO) {
            pureSilverGrams.multiply(silverUnitPrice).setScale(scale, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP)
        }

        // 4. Business liquid & resale assets
        val businessTotal = safeNonNeg(input.businessCash)
            .add(safeNonNeg(input.resaleInventory))
            .add(safeNonNeg(input.merchandiseForResale))
            .add(safeNonNeg(input.tradeReceivables))
            .add(safeNonNeg(input.otherLiquidBusinessAssets))

        // 5. Investments (never invent percentage; use user-provided portion when selected)
        val rawInvestments = safeNonNeg(input.sharesMarketValue)
            .add(safeNonNeg(input.mutualFundsValue))
            .add(safeNonNeg(input.otherQualifyingInvestments))
        val qualifyingInvestments = when (input.investmentMethodology) {
            InvestmentMethodology.FULL_MARKET_VALUE -> rawInvestments
            InvestmentMethodology.USER_ZAKATABLE_PORTION -> {
                val pct = clampPercent(input.userSpecifiedInvestmentZakatablePercent ?: BigDecimal("100"))
                rawInvestments.multiply(pct).divide(BigDecimal("100"), scale + 2, RoundingMode.HALF_UP)
            }
        }

        // 6. Receivables
        val strongRec = safeNonNeg(input.strongReceivables)
        val doubtfulRec = safeNonNeg(input.doubtfulReceivables)
        val unrecoverableRec = safeNonNeg(input.unrecoverableReceivables)
        val eligibleReceivables = when (input.receivableMethodology) {
            ReceivableMethodology.STRONG_ONLY_EXCLUDE_DOUBTFUL -> strongRec
            ReceivableMethodology.INCLUDE_STRONG_AND_DOUBTFUL -> strongRec.add(doubtfulRec)
        }
        val excludedReceivables = unrecoverableRec.add(
            if (input.receivableMethodology == ReceivableMethodology.STRONG_ONLY_EXCLUDE_DOUBTFUL) doubtfulRec else BigDecimal.ZERO
        )

        // 7. Total Qualifying Assets
        val totalAssets = cashTotal
            .add(goldValuation)
            .add(silverValuation)
            .add(businessTotal)
            .add(qualifyingInvestments)
            .add(eligibleReceivables)
            .setScale(scale, RoundingMode.HALF_UP)

        // 8. Eligible Deductible Liabilities
        val liabilities = safeNonNeg(input.overdueDebts)
            .add(safeNonNeg(input.currentlyDueBills))
            .add(safeNonNeg(input.shortTermObligations))
            .add(safeNonNeg(input.otherQualifyingLiabilities))
            .add(
                if (input.debtMethodology == DebtDeductionMethodology.SHORT_TERM_AND_CURRENT_INSTALLMENT) {
                    safeNonNeg(input.currentDueLoanRepayments)
                } else {
                    BigDecimal.ZERO
                }
            )
            .setScale(scale, RoundingMode.HALF_UP)

        val rawSurplus = totalAssets.subtract(liabilities)
        val netWealth = if (rawSurplus > BigDecimal.ZERO) {
            rawSurplus.setScale(scale, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP)
        }

        // 9. Nisab Threshold
        val nisabWeight = if (input.nisabType == NisabType.GOLD) GOLD_NISAB_GRAMS else SILVER_NISAB_GRAMS
        val nisabUnitPrice = if (input.nisabType == NisabType.GOLD) goldUnitPrice else silverUnitPrice
        val nisabThreshold = nisabUnitPrice?.let {
            nisabWeight.multiply(it).setScale(scale, RoundingMode.HALF_UP)
        }

        // 10. Hawl Evaluation
        val hawlEval = evaluateHawl(
            hawlConfirmedByUser = input.hawlConfirmedByUser,
            startIsoDate = input.hawlStartGregorianIso,
            evaluationEpochMs = input.evaluationEpochMs,
            uiLanguage = uiLanguage
        )

        // 11. Eligibility & Zakat Payable (never claim Zakat Due unless verified)
        val reason = when {
            nisabThreshold == null || nisabThreshold <= BigDecimal.ZERO -> ZakatEligibilityReason.PRICE_MISSING
            rawSurplus <= BigDecimal.ZERO -> ZakatEligibilityReason.ZERO_OR_NEGATIVE_WEALTH
            netWealth < nisabThreshold -> ZakatEligibilityReason.BELOW_NISAB
            hawlEval.status == HawlStatus.UNVERIFIED_MISSING -> ZakatEligibilityReason.HAWL_UNVERIFIED
            hawlEval.status == HawlStatus.NOT_YET_COMPLETE -> ZakatEligibilityReason.HAWL_NOT_COMPLETE
            else -> ZakatEligibilityReason.ZAKAT_DUE
        }

        val isEligible = reason == ZakatEligibilityReason.ZAKAT_DUE
        val zakatPayable = if (isEligible) {
            netWealth.multiply(ZAKAT_RATE).setScale(scale, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP)
        }

        return GlobalZakatResult(
            currencyCode = currencyInfo.code,
            currencyDecimals = scale,
            cashAndBankTotal = cashTotal.setScale(scale, RoundingMode.HALF_UP),
            pureGoldEquivalentGrams = pureGoldGrams,
            goldValuation = goldValuation,
            pureSilverEquivalentGrams = pureSilverGrams,
            silverValuation = silverValuation,
            businessAssetsTotal = businessTotal.setScale(scale, RoundingMode.HALF_UP),
            qualifyingInvestmentsTotal = qualifyingInvestments.setScale(scale, RoundingMode.HALF_UP),
            eligibleReceivablesTotal = eligibleReceivables.setScale(scale, RoundingMode.HALF_UP),
            excludedReceivablesTotal = excludedReceivables.setScale(scale, RoundingMode.HALF_UP),
            totalQualifyingAssets = totalAssets,
            eligibleDeductibleLiabilities = liabilities,
            rawSurplusBeforeFloor = rawSurplus.setScale(scale, RoundingMode.HALF_UP),
            netZakatableWealth = netWealth,
            nisabType = input.nisabType,
            nisabWeightGrams = nisabWeight,
            nisabUnitPricePerGram = nisabUnitPrice,
            nisabThreshold = nisabThreshold,
            hawlEvaluation = hawlEval,
            zakatRate = ZAKAT_RATE,
            isEligible = isEligible,
            eligibilityReason = reason,
            zakatPayable = zakatPayable,
            goldPriceSourceLabel = input.goldPriceSourceLabel,
            goldPriceDate = input.goldPriceDate,
            isGoldPriceManual = input.isGoldPriceManual,
            silverPriceSourceLabel = input.silverPriceSourceLabel,
            silverPriceDate = input.silverPriceDate,
            isSilverPriceManual = input.isSilverPriceManual,
            exchangeRateSource = input.exchangeRateSource,
            exchangeRateDate = input.exchangeRateDate,
            originalMetalPriceUsdPerGramGold = input.originalMetalPriceUsdPerGramGold,
            originalMetalPriceUsdPerGramSilver = input.originalMetalPriceUsdPerGramSilver,
            exchangeRateUsdToSelected = input.exchangeRateUsdToSelected
        )
    }

    fun evaluateHawl(
        hawlConfirmedByUser: Boolean?,
        startIsoDate: String,
        evaluationEpochMs: Long = System.currentTimeMillis(),
        uiLanguage: AppLanguage = AppLanguage.ENGLISH
    ): HawlEvaluation {
        val cleanDate = startIsoDate.trim()
        val parsedStartCal = parseIsoDate(cleanDate)

        if (parsedStartCal != null) {
            val anniversaryCal = (parsedStartCal.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, LUNAR_YEAR_DAYS.toInt())
            }
            val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val anniversaryIso = isoFormat.format(anniversaryCal.time)
            val hijriAnniversary = IslamicCalendarRepository.getHijriDate(anniversaryCal)

            val daysElapsed = ((evaluationEpochMs - parsedStartCal.timeInMillis) / (24L * 60L * 60L * 1000L))
                .coerceAtLeast(0L)
            val remainingDays = (LUNAR_YEAR_DAYS - daysElapsed).coerceAtLeast(0L)

            val derivedStatus = when {
                hawlConfirmedByUser == false -> HawlStatus.NOT_YET_COMPLETE
                hawlConfirmedByUser == true -> HawlStatus.CONFIRMED_COMPLETE
                daysElapsed >= LUNAR_YEAR_DAYS -> HawlStatus.CONFIRMED_COMPLETE
                else -> HawlStatus.NOT_YET_COMPLETE
            }
            return HawlEvaluation(
                status = derivedStatus,
                reachedNisabGregorianIso = cleanDate,
                anniversaryGregorianIso = anniversaryIso,
                anniversaryHijriFormatted = hijriAnniversary.formatted(uiLanguage),
                lunarDaysElapsed = daysElapsed,
                remainingLunarDays = remainingDays,
                isHijriEstimate = hijriAnniversary.isEstimate
            )
        }

        // No date entered: respect explicit confirmation only; never assume Hawl is complete
        val status = when (hawlConfirmedByUser) {
            true -> HawlStatus.CONFIRMED_COMPLETE
            false -> HawlStatus.NOT_YET_COMPLETE
            null -> HawlStatus.UNVERIFIED_MISSING
        }
        return HawlEvaluation(
            status = status,
            reachedNisabGregorianIso = "",
            anniversaryGregorianIso = "",
            anniversaryHijriFormatted = "",
            lunarDaysElapsed = null,
            remainingLunarDays = null,
            isHijriEstimate = true
        )
    }

    private fun parseIsoDate(iso: String): Calendar? {
        if (!Regex("""^\d{4}-\d{2}-\d{2}$""").matches(iso)) return null
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
                isLenient = false
            }
            val date = sdf.parse(iso) ?: return null
            Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun safeNonNeg(value: BigDecimal): BigDecimal {
        return if (value < BigDecimal.ZERO) BigDecimal.ZERO else value
    }

    private fun clampPercent(value: BigDecimal): BigDecimal {
        return when {
            value < BigDecimal.ZERO -> BigDecimal.ZERO
            value > BigDecimal("100") -> BigDecimal("100")
            else -> value
        }
    }
}
