package com.example.data.zakat

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object ZakatCurrencyFormatter {

    /**
     * Formats a BigDecimal monetary value using the target currency's official ISO 4217
     * minor-unit fraction digits (e.g. JPY=0, PKR=2, KWD=3) and locale-aware separators.
     */
    fun formatAmount(
        amount: BigDecimal,
        currencyCode: String,
        displayLocale: Locale = Locale.getDefault()
    ): String {
        val info = ZakatCurrencyCatalog.getCurrencyOrDefault(currencyCode)
        val scaled = amount.setScale(info.decimalDigits, RoundingMode.HALF_UP)

        val formattedNumber = try {
            val nf = NumberFormat.getNumberInstance(displayLocale)
            nf.minimumFractionDigits = info.decimalDigits
            nf.maximumFractionDigits = info.decimalDigits
            nf.isGroupingUsed = true
            nf.format(scaled)
        } catch (_: Exception) {
            scaled.toPlainString()
        }

        val symbol = try {
            Currency.getInstance(info.code).getSymbol(displayLocale)
                .takeIf { it.isNotBlank() && it != info.code }
                ?: info.symbol
        } catch (_: Exception) {
            info.symbol
        }

        return if (symbol.equals(info.code, ignoreCase = true)) {
            "${info.code} $formattedNumber"
        } else {
            "$symbol $formattedNumber (${info.code})"
        }
    }

    fun formatCompactNumber(amount: BigDecimal, decimalDigits: Int = 2): String {
        return amount.setScale(decimalDigits.coerceIn(0, 6), RoundingMode.HALF_UP).toPlainString()
    }

    /**
     * Safely parses user-entered decimal text (including Arabic-Indic ٠-٩ and Urdu/Persian ۰-۹ digits,
     * spaces, and thousands separators) into BigDecimal. Returns null if malformed.
     */
    fun parseDecimalOrNull(raw: String): BigDecimal? {
        val trimmed = raw.trim()
        if (trimmed.isEmpty()) return BigDecimal.ZERO
        val normalized = normalizeDigits(trimmed)
            .replace("\u00A0", "")
            .replace(" ", "")
            .replace("_", "")

        // Handle comma vs dot cleanly:
        // If only commas and no dots:
        //   - If single comma followed by 1..3 digits and total length small or looks like decimal, treat carefully;
        //   - Wait: if comma appears multiple times (1,000,000), strip commas.
        val cleaned = if (normalized.contains('.') && normalized.contains(',')) {
            val lastDot = normalized.lastIndexOf('.')
            val lastComma = normalized.lastIndexOf(',')
            if (lastDot > lastComma) {
                normalized.replace(",", "")
            } else {
                normalized.replace(".", "").replace(',', '.')
            }
        } else if (normalized.contains(',')) {
            val commaCount = normalized.count { it == ',' }
            if (commaCount > 1) {
                normalized.replace(",", "")
            } else {
                val afterComma = normalized.substringAfter(',')
                if (afterComma.length == 3 && normalized.substringBefore(',').length in 1..3 && normalized.length >= 5) {
                    normalized.replace(",", "")
                } else {
                    normalized.replace(',', '.')
                }
            }
        } else {
            normalized
        }

        return try {
            val bd = BigDecimal(cleaned)
            if (bd < BigDecimal.ZERO) BigDecimal.ZERO else bd
        } catch (_: Exception) {
            null
        }
    }

    fun parseDecimalOrZero(raw: String): BigDecimal = parseDecimalOrNull(raw) ?: BigDecimal.ZERO

    private fun normalizeDigits(input: String): String {
        val sb = StringBuilder(input.length)
        for (ch in input) {
            when (ch) {
                in '٠'..'٩' -> sb.append(('0'.code + (ch.code - '٠'.code)).toChar())
                in '۰'..'۹' -> sb.append(('0'.code + (ch.code - '۰'.code)).toChar())
                '٫' -> sb.append('.')
                '٬' -> sb.append(',')
                else -> sb.append(ch)
            }
        }
        return sb.toString()
    }
}
