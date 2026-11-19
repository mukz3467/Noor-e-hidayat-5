package com.example.data.zakat

import android.content.Context
import java.util.Currency
import java.util.Locale

data class IsoCurrencyInfo(
    val code: String,
    val name: String,
    val symbol: String,
    val decimalDigits: Int,
    val regionLabel: String
)

data class CountryRegionInfo(
    val isoCode: String,
    val flagEmoji: String,
    val displayName: String,
    val primaryCurrencyCode: String,
    val legitimateCurrencies: List<String>
)

object ZakatCurrencyCatalog {

    const val VERIFIED_DATASET_UPDATED = "2026-07-01"
    private const val PREFS_NAME = "noor_global_zakat_prefs_v1"
    private const val KEY_COUNTRY_ISO = "selected_country_iso"
    private const val KEY_CURRENCY_CODE = "selected_currency_code"

    /**
     * Explicit ISO 4217 excluded non-national / historical / special accounting codes so they never
     * pollute the active national currency selector even if returned by JVM Currency.getAvailableCurrencies().
     */
    val EXCLUDED_NON_NATIONAL_OR_OBSOLETE_CODES = setOf(
        // Special / Metals / Supranational Testing & Accounting Funds
        "XAU", "XAG", "XPT", "XPD", "XTS", "XXX", "XDR", "XSU", "XUA",
        "XBA", "XBB", "XBC", "XBD", "XCG",
        "USN", "USS", "BOV", "CLF", "COU", "MXV", "UYI", "UYW", "CHE", "CHW", "VED",
        // Obsolete / Withdrawn Historical Currencies
        "ADP", "ATS", "BEF", "CYP", "DEM", "EEK", "ESP", "FIM", "FRF", "GRD",
        "HRK", "IEP", "ITL", "LTL", "LUF", "LVL", "MTL", "NLG", "PTE", "SIT", "SKK",
        "AYM", "AZM", "BGL", "BYB", "BYR", "CSD", "CUC", "DDM", "ECS", "ECV",
        "GHC", "GWP", "MGF", "MRO", "MZM", "ROL", "RUR", "SDD", "SKK", "SLL",
        "SRG", "STD", "TMM", "TPE", "TRL", "VEB", "VEF", "YUM", "ZMK", "ZWD", "ZWL", "ZWN", "ZWR"
    )

    private data class CurrencySeed(
        val code: String,
        val name: String,
        val symbol: String,
        val decimals: Int,
        val region: String
    )

    /**
     * Complete verified registry of active official ISO 4217 national currencies worldwide
     * across Asia, Middle East, Africa, Europe, North America, South America, and Oceania.
     */
    private val ACTIVE_CURRENCY_SEEDS: List<CurrencySeed> = listOf(
        // Major Global & OIC / Muslim-Majority Currencies
        CurrencySeed("PKR", "Pakistani Rupee", "Rs", 2, "Asia"),
        CurrencySeed("SAR", "Saudi Riyal", "﷼", 2, "Asia"),
        CurrencySeed("AED", "UAE Dirham", "د.إ", 2, "Asia"),
        CurrencySeed("USD", "US Dollar", "$", 2, "North America"),
        CurrencySeed("GBP", "British Pound Sterling", "£", 2, "Europe"),
        CurrencySeed("EUR", "Euro", "€", 2, "Europe"),
        CurrencySeed("KWD", "Kuwaiti Dinar", "د.ك", 3, "Asia"),
        CurrencySeed("QAR", "Qatari Riyal", "ر.ق", 2, "Asia"),
        CurrencySeed("BHD", "Bahraini Dinar", "د.ب", 3, "Asia"),
        CurrencySeed("OMR", "Omani Rial", "ر.ع.", 3, "Asia"),
        CurrencySeed("JOD", "Jordanian Dinar", "د.أ", 3, "Asia"),
        CurrencySeed("EGP", "Egyptian Pound", "ج.م", 2, "Africa"),
        CurrencySeed("INR", "Indian Rupee", "₹", 2, "Asia"),
        CurrencySeed("BDT", "Bangladeshi Taka", "৳", 2, "Asia"),
        CurrencySeed("MYR", "Malaysian Ringgit", "RM", 2, "Asia"),
        CurrencySeed("IDR", "Indonesian Rupiah", "Rp", 2, "Asia"),
        CurrencySeed("TRY", "Turkish Lira", "₺", 2, "Asia/Europe"),
        CurrencySeed("CAD", "Canadian Dollar", "CA$", 2, "North America"),
        CurrencySeed("AUD", "Australian Dollar", "A$", 2, "Oceania"),
        // Rest of Asia & Middle East
        CurrencySeed("AFN", "Afghan Afghani", "؋", 2, "Asia"),
        CurrencySeed("AMD", "Armenian Dram", "֏", 2, "Asia"),
        CurrencySeed("AZN", "Azerbaijani Manat", "₼", 2, "Asia"),
        CurrencySeed("BND", "Brunei Dollar", "B$", 2, "Asia"),
        CurrencySeed("BTN", "Bhutanese Ngultrum", "Nu.", 2, "Asia"),
        CurrencySeed("CNY", "Chinese Yuan Renminbi", "¥", 2, "Asia"),
        CurrencySeed("GEL", "Georgian Lari", "₾", 2, "Asia"),
        CurrencySeed("HKD", "Hong Kong Dollar", "HK$", 2, "Asia"),
        CurrencySeed("ILS", "Israeli New Shekel", "₪", 2, "Asia"),
        CurrencySeed("IQD", "Iraqi Dinar", "ع.د", 3, "Asia"),
        CurrencySeed("IRR", "Iranian Rial", "﷼", 2, "Asia"),
        CurrencySeed("JPY", "Japanese Yen", "¥", 0, "Asia"),
        CurrencySeed("KGS", "Kyrgystani Som", "с", 2, "Asia"),
        CurrencySeed("KHR", "Cambodian Riel", "៛", 2, "Asia"),
        CurrencySeed("KPW", "North Korean Won", "₩", 2, "Asia"),
        CurrencySeed("KRW", "South Korean Won", "₩", 0, "Asia"),
        CurrencySeed("KZT", "Kazakhstani Tenge", "₸", 2, "Asia"),
        CurrencySeed("LAK", "Lao Kip", "₭", 2, "Asia"),
        CurrencySeed("LBP", "Lebanese Pound", "ل.ل", 2, "Asia"),
        CurrencySeed("LKR", "Sri Lankan Rupee", "Rs", 2, "Asia"),
        CurrencySeed("MMK", "Myanmar Kyat", "K", 2, "Asia"),
        CurrencySeed("MNT", "Mongolian Tugrik", "₮", 2, "Asia"),
        CurrencySeed("MOP", "Macanese Pataca", "MOP$", 2, "Asia"),
        CurrencySeed("MVR", "Maldivian Rufiyaa", "Rf", 2, "Asia"),
        CurrencySeed("NPR", "Nepalese Rupee", "Rs", 2, "Asia"),
        CurrencySeed("PHP", "Philippine Peso", "₱", 2, "Asia"),
        CurrencySeed("SGD", "Singapore Dollar", "S$", 2, "Asia"),
        CurrencySeed("SYP", "Syrian Pound", "£S", 2, "Asia"),
        CurrencySeed("THB", "Thai Baht", "฿", 2, "Asia"),
        CurrencySeed("TJS", "Tajikistani Somoni", "SM", 2, "Asia"),
        CurrencySeed("TMT", "Turkmenistani Manat", "m", 2, "Asia"),
        CurrencySeed("TWD", "New Taiwan Dollar", "NT$", 2, "Asia"),
        CurrencySeed("UZS", "Uzbekistani Som", "soʻm", 2, "Asia"),
        CurrencySeed("VND", "Vietnamese Dong", "₫", 0, "Asia"),
        CurrencySeed("YER", "Yemeni Rial", "﷼", 2, "Asia"),
        // Africa
        CurrencySeed("AOA", "Angolan Kwanza", "Kz", 2, "Africa"),
        CurrencySeed("BIF", "Burundian Franc", "FBu", 0, "Africa"),
        CurrencySeed("BWP", "Botswana Pula", "P", 2, "Africa"),
        CurrencySeed("CDF", "Congolese Franc", "FC", 2, "Africa"),
        CurrencySeed("CVE", "Cape Verdean Escudo", "Esc", 2, "Africa"),
        CurrencySeed("DJF", "Djiboutian Franc", "Fdj", 0, "Africa"),
        CurrencySeed("DZD", "Algerian Dinar", "د.ج", 2, "Africa"),
        CurrencySeed("ERN", "Eritrean Nakfa", "Nfk", 2, "Africa"),
        CurrencySeed("ETB", "Ethiopian Birr", "Br", 2, "Africa"),
        CurrencySeed("GHS", "Ghanaian Cedi", "₵", 2, "Africa"),
        CurrencySeed("GMD", "Gambian Dalasi", "D", 2, "Africa"),
        CurrencySeed("GNF", "Guinean Franc", "FG", 0, "Africa"),
        CurrencySeed("KES", "Kenyan Shilling", "KSh", 2, "Africa"),
        CurrencySeed("KMF", "Comorian Franc", "CF", 0, "Africa"),
        CurrencySeed("LRD", "Liberian Dollar", "L$", 2, "Africa"),
        CurrencySeed("LSL", "Lesotho Loti", "L", 2, "Africa"),
        CurrencySeed("LYD", "Libyan Dinar", "ل.د", 3, "Africa"),
        CurrencySeed("MAD", "Moroccan Dirham", "د.م.", 2, "Africa"),
        CurrencySeed("MGA", "Malagasy Ariary", "Ar", 2, "Africa"),
        CurrencySeed("MRU", "Mauritanian Ouguiya", "UM", 2, "Africa"),
        CurrencySeed("MUR", "Mauritian Rupee", "₨", 2, "Africa"),
        CurrencySeed("MWK", "Malawian Kwacha", "MK", 2, "Africa"),
        CurrencySeed("MZN", "Mozambican Metical", "MT", 2, "Africa"),
        CurrencySeed("NAD", "Namibian Dollar", "N$", 2, "Africa"),
        CurrencySeed("NGN", "Nigerian Naira", "₦", 2, "Africa"),
        CurrencySeed("RWF", "Rwandan Franc", "FRw", 0, "Africa"),
        CurrencySeed("SCR", "Seychellois Rupee", "₨", 2, "Africa"),
        CurrencySeed("SDG", "Sudanese Pound", "ج.س.", 2, "Africa"),
        CurrencySeed("SHP", "Saint Helena Pound", "£", 2, "Africa"),
        CurrencySeed("SLE", "Sierra Leonean Leone", "Le", 2, "Africa"),
        CurrencySeed("SOS", "Somali Shilling", "Sh", 2, "Africa"),
        CurrencySeed("SSP", "South Sudanese Pound", "£", 2, "Africa"),
        CurrencySeed("STN", "Sao Tome and Principe Dobra", "Db", 2, "Africa"),
        CurrencySeed("SZL", "Eswatini Lilangeni", "E", 2, "Africa"),
        CurrencySeed("TND", "Tunisian Dinar", "د.ت", 3, "Africa"),
        CurrencySeed("TZS", "Tanzanian Shilling", "TSh", 2, "Africa"),
        CurrencySeed("UGX", "Ugandan Shilling", "USh", 0, "Africa"),
        CurrencySeed("XAF", "Central African CFA Franc", "FCFA", 0, "Africa"),
        CurrencySeed("XOF", "West African CFA Franc", "CFA", 0, "Africa"),
        CurrencySeed("ZAR", "South African Rand", "R", 2, "Africa"),
        CurrencySeed("ZMW", "Zambian Kwacha", "ZK", 2, "Africa"),
        CurrencySeed("ZWG", "Zimbabwe Gold", "ZiG", 2, "Africa"),
        // Europe (Non-Eurozone + Eurozone covered by EUR)
        CurrencySeed("ALL", "Albanian Lek", "L", 2, "Europe"),
        CurrencySeed("BAM", "Bosnia-Herzegovina Convertible Mark", "KM", 2, "Europe"),
        CurrencySeed("BGN", "Bulgarian Lev", "лв", 2, "Europe"),
        CurrencySeed("BYN", "Belarusian Ruble", "Br", 2, "Europe"),
        CurrencySeed("CHF", "Swiss Franc", "CHF", 2, "Europe"),
        CurrencySeed("CZK", "Czech Koruna", "Kč", 2, "Europe"),
        CurrencySeed("DKK", "Danish Krone", "kr", 2, "Europe"),
        CurrencySeed("GIP", "Gibraltar Pound", "£", 2, "Europe"),
        CurrencySeed("HUF", "Hungarian Forint", "Ft", 2, "Europe"),
        CurrencySeed("ISK", "Icelandic Krona", "kr", 0, "Europe"),
        CurrencySeed("MDL", "Moldovan Leu", "L", 2, "Europe"),
        CurrencySeed("MKD", "Macedonian Denar", "ден", 2, "Europe"),
        CurrencySeed("NOK", "Norwegian Krone", "kr", 2, "Europe"),
        CurrencySeed("PLN", "Polish Zloty", "zł", 2, "Europe"),
        CurrencySeed("RON", "Romanian Leu", "lei", 2, "Europe"),
        CurrencySeed("RSD", "Serbian Dinar", "дин.", 2, "Europe"),
        CurrencySeed("RUB", "Russian Ruble", "₽", 2, "Europe"),
        CurrencySeed("SEK", "Swedish Krona", "kr", 2, "Europe"),
        CurrencySeed("UAH", "Ukrainian Hryvnia", "₴", 2, "Europe"),
        // North America, Central America & Caribbean
        CurrencySeed("AWG", "Aruban Florin", "ƒ", 2, "North America"),
        CurrencySeed("BBD", "Barbadian Dollar", "Bds$", 2, "North America"),
        CurrencySeed("BMD", "Bermudian Dollar", "BD$", 2, "North America"),
        CurrencySeed("BSD", "Bahamian Dollar", "B$", 2, "North America"),
        CurrencySeed("BZD", "Belize Dollar", "BZ$", 2, "North America"),
        CurrencySeed("CRC", "Costa Rican Colon", "₡", 2, "North America"),
        CurrencySeed("CUP", "Cuban Peso", "$", 2, "North America"),
        CurrencySeed("DOP", "Dominican Peso", "RD$", 2, "North America"),
        CurrencySeed("GTQ", "Guatemalan Quetzal", "Q", 2, "North America"),
        CurrencySeed("HNL", "Honduran Lempira", "L", 2, "North America"),
        CurrencySeed("HTG", "Haitian Gourde", "G", 2, "North America"),
        CurrencySeed("JMD", "Jamaican Dollar", "J$", 2, "North America"),
        CurrencySeed("KYD", "Cayman Islands Dollar", "CI$", 2, "North America"),
        CurrencySeed("MXN", "Mexican Peso", "MX$", 2, "North America"),
        CurrencySeed("NIO", "Nicaraguan Cordoba", "C$", 2, "North America"),
        CurrencySeed("PAB", "Panamanian Balboa", "B/.", 2, "North America"),
        CurrencySeed("TTD", "Trinidad and Tobago Dollar", "TT$", 2, "North America"),
        CurrencySeed("XCD", "East Caribbean Dollar", "EC$", 2, "North America"),
        // South America
        CurrencySeed("ARS", "Argentine Peso", "AR$", 2, "South America"),
        CurrencySeed("BOB", "Bolivian Boliviano", "Bs.", 2, "South America"),
        CurrencySeed("BRL", "Brazilian Real", "R$", 2, "South America"),
        CurrencySeed("CLP", "Chilean Peso", "CLP$", 0, "South America"),
        CurrencySeed("COP", "Colombian Peso", "COL$", 2, "South America"),
        CurrencySeed("FKP", "Falkland Islands Pound", "£", 2, "South America"),
        CurrencySeed("GYD", "Guyanese Dollar", "G$", 2, "South America"),
        CurrencySeed("PEN", "Peruvian Sol", "S/", 2, "South America"),
        CurrencySeed("PYG", "Paraguayan Guarani", "₲", 0, "South America"),
        CurrencySeed("SRD", "Surinamese Dollar", "Sr$", 2, "South America"),
        CurrencySeed("UYU", "Uruguayan Peso", "\$U", 2, "South America"),
        CurrencySeed("VES", "Venezuelan Bolivar Soberano", "Bs.S", 2, "South America"),
        // Oceania
        CurrencySeed("FJD", "Fijian Dollar", "FJ$", 2, "Oceania"),
        CurrencySeed("NZD", "New Zealand Dollar", "NZ$", 2, "Oceania"),
        CurrencySeed("PGK", "Papua New Guinean Kina", "K", 2, "Oceania"),
        CurrencySeed("SBD", "Solomon Islands Dollar", "SI$", 2, "Oceania"),
        CurrencySeed("TOP", "Tongan Paʻanga", "T$", 2, "Oceania"),
        CurrencySeed("VUV", "Vanuatu Vatu", "VT", 0, "Oceania"),
        CurrencySeed("WST", "Samoan Tala", "WS$", 2, "Oceania"),
        CurrencySeed("XPF", "CFP Franc", "₣", 0, "Oceania")
    )

    val ACTIVE_CURRENCIES: List<IsoCurrencyInfo> by lazy {
        val jvmCurrencies = Currency.getAvailableCurrencies().associateBy { it.currencyCode }
        ACTIVE_CURRENCY_SEEDS.map { seed ->
            val jvm = jvmCurrencies[seed.code]
            val decimals = jvm?.defaultFractionDigits?.takeIf { it >= 0 } ?: seed.decimals
            val resolvedName = jvm?.getDisplayName(Locale.ENGLISH)?.takeIf { it.isNotBlank() && it != seed.code } ?: seed.name
            val resolvedSymbol = jvm?.getSymbol(Locale.US)?.takeIf { it.isNotBlank() && it != seed.code } ?: seed.symbol
            IsoCurrencyInfo(
                code = seed.code,
                name = resolvedName,
                symbol = resolvedSymbol,
                decimalDigits = decimals,
                regionLabel = seed.region
            )
        }.sortedWith(compareBy<IsoCurrencyInfo> { it.code })
    }

    private val CURRENCY_BY_CODE: Map<String, IsoCurrencyInfo> by lazy {
        ACTIVE_CURRENCIES.associateBy { it.code }
    }

    fun getCurrencyOrDefault(code: String): IsoCurrencyInfo {
        return CURRENCY_BY_CODE[code.uppercase(Locale.US)] ?: CURRENCY_BY_CODE.getValue("USD")
    }

    fun findCurrency(code: String): IsoCurrencyInfo? {
        return CURRENCY_BY_CODE[code.uppercase(Locale.US)]
    }

    fun searchCurrencies(query: String): List<IsoCurrencyInfo> {
        val q = query.trim().lowercase(Locale.US)
        if (q.isBlank()) return ACTIVE_CURRENCIES
        return ACTIVE_CURRENCIES.filter {
            it.code.lowercase(Locale.US).contains(q) ||
                it.name.lowercase(Locale.US).contains(q) ||
                it.symbol.lowercase(Locale.US).contains(q) ||
                it.regionLabel.lowercase(Locale.US).contains(q)
        }
    }

    /**
     * Multi-currency and shared-currency country mapping per ISO 3166 -> ISO 4217.
     */
    private val COUNTRY_CURRENCY_OVERRIDES: Map<String, List<String>> = mapOf(
        "PK" to listOf("PKR"),
        "SA" to listOf("SAR"),
        "AE" to listOf("AED"),
        "US" to listOf("USD"),
        "GB" to listOf("GBP"),
        "IN" to listOf("INR"),
        "BD" to listOf("BDT"),
        "MY" to listOf("MYR"),
        "ID" to listOf("IDR"),
        "TR" to listOf("TRY"),
        "EG" to listOf("EGP"),
        "KW" to listOf("KWD"),
        "QA" to listOf("QAR"),
        "BH" to listOf("BHD"),
        "OM" to listOf("OMR"),
        "JO" to listOf("JOD"),
        "MA" to listOf("MAD"),
        "DZ" to listOf("DZD"),
        "TN" to listOf("TND"),
        "LY" to listOf("LYD"),
        "SD" to listOf("SDG"),
        "SO" to listOf("SOS"),
        "NG" to listOf("NGN"),
        "KE" to listOf("KES"),
        "ZA" to listOf("ZAR"),
        "CA" to listOf("CAD"),
        "AU" to listOf("AUD"),
        "NZ" to listOf("NZD"),
        "JP" to listOf("JPY"),
        "CN" to listOf("CNY"),
        "KR" to listOf("KRW"),
        "SG" to listOf("SGD"),
        "CH" to listOf("CHF"),
        // Eurozone countries sharing EUR
        "AT" to listOf("EUR"), "BE" to listOf("EUR"), "CY" to listOf("EUR"), "DE" to listOf("EUR"),
        "EE" to listOf("EUR"), "ES" to listOf("EUR"), "FI" to listOf("EUR"), "FR" to listOf("EUR"),
        "GR" to listOf("EUR"), "HR" to listOf("EUR"), "IE" to listOf("EUR"), "IT" to listOf("EUR"),
        "LT" to listOf("EUR"), "LU" to listOf("EUR"), "LV" to listOf("EUR"), "MT" to listOf("EUR"),
        "NL" to listOf("EUR"), "PT" to listOf("EUR"), "SI" to listOf("EUR"), "SK" to listOf("EUR"),
        // Shared CFA zones
        "BJ" to listOf("XOF"), "BF" to listOf("XOF"), "CI" to listOf("XOF"), "GW" to listOf("XOF"),
        "ML" to listOf("XOF"), "NE" to listOf("XOF"), "SN" to listOf("XOF"), "TG" to listOf("XOF"),
        "CM" to listOf("XAF"), "CF" to listOf("XAF"), "TD" to listOf("XAF"), "CG" to listOf("XAF"),
        "GQ" to listOf("XAF"), "GA" to listOf("XAF"),
        // Multi-currency or dollarized countries
        "EC" to listOf("USD"), "SV" to listOf("USD", "BTC"), "TL" to listOf("USD"),
        "PA" to listOf("PAB", "USD"),
        "ZW" to listOf("ZWG", "USD", "ZAR"),
        "LB" to listOf("LBP", "USD"),
        "PS" to listOf("ILS", "JOD"),
        "BT" to listOf("BTN", "INR"),
        "LS" to listOf("LSL", "ZAR"),
        "NA" to listOf("NAD", "ZAR"),
        "SZ" to listOf("SZL", "ZAR"),
        "BN" to listOf("BND", "SGD"),
        "BS" to listOf("BSD", "USD"),
        "HT" to listOf("HTG", "USD"),
        "KH" to listOf("KHR", "USD"),
        "BA" to listOf("BAM")
    )

    val COUNTRIES: List<CountryRegionInfo> by lazy {
        Locale.getISOCountries()
            .filter { it.length == 2 }
            .mapNotNull { iso ->
                val displayName = Locale("", iso).getDisplayCountry(Locale.ENGLISH)
                if (displayName.isBlank()) return@mapNotNull null
                val mapped = COUNTRY_CURRENCY_OVERRIDES[iso]
                    ?.filter { CURRENCY_BY_CODE.containsKey(it) }
                    ?.takeIf { it.isNotEmpty() }
                val jvmCurrencyCode = try {
                    Currency.getInstance(Locale("", iso))?.currencyCode
                } catch (_: Exception) {
                    null
                }
                val resolvedList = when {
                    mapped != null -> mapped
                    jvmCurrencyCode != null && CURRENCY_BY_CODE.containsKey(jvmCurrencyCode) -> listOf(jvmCurrencyCode)
                    else -> listOf("USD")
                }
                CountryRegionInfo(
                    isoCode = iso,
                    flagEmoji = isoToFlagEmoji(iso),
                    displayName = displayName,
                    primaryCurrencyCode = resolvedList.first(),
                    legitimateCurrencies = resolvedList
                )
            }
            .sortedBy { it.displayName }
    }

    fun isoToFlagEmoji(isoCountryCode: String): String {
        val clean = isoCountryCode.trim().uppercase(Locale.US)
        if (clean.length != 2 || !clean[0].isLetter() || !clean[1].isLetter()) return "🌐"
        val first = Character.codePointAt(clean, 0) - 0x41 + 0x1F1E6
        val second = Character.codePointAt(clean, 1) - 0x41 + 0x1F1E6
        return String(Character.toChars(first)) + String(Character.toChars(second))
    }

    fun detectDeviceSuggestedCountry(): CountryRegionInfo {
        val iso = Locale.getDefault().country.uppercase(Locale.US)
        return COUNTRIES.firstOrNull { it.isoCode == iso }
            ?: COUNTRIES.firstOrNull { it.isoCode == "PK" }
            ?: COUNTRIES.first()
    }

    fun findCountry(isoCode: String): CountryRegionInfo {
        val clean = isoCode.trim().uppercase(Locale.US)
        return COUNTRIES.firstOrNull { it.isoCode == clean } ?: detectDeviceSuggestedCountry()
    }

    fun searchCountries(query: String): List<CountryRegionInfo> {
        val q = query.trim().lowercase(Locale.US)
        if (q.isBlank()) return COUNTRIES
        return COUNTRIES.filter {
            it.displayName.lowercase(Locale.US).contains(q) ||
                it.isoCode.lowercase(Locale.US).contains(q) ||
                it.primaryCurrencyCode.lowercase(Locale.US).contains(q)
        }
    }

    fun loadPersistedSelection(context: Context): Pair<CountryRegionInfo, IsoCurrencyInfo> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val deviceSuggestion = detectDeviceSuggestedCountry()
        val savedCountryIso = prefs.getString(KEY_COUNTRY_ISO, null)
        val country = if (!savedCountryIso.isNullOrBlank()) {
            findCountry(savedCountryIso)
        } else {
            deviceSuggestion
        }
        val savedCurrencyCode = prefs.getString(KEY_CURRENCY_CODE, null)
        val currency = if (!savedCurrencyCode.isNullOrBlank()) {
            getCurrencyOrDefault(savedCurrencyCode)
        } else {
            getCurrencyOrDefault(country.primaryCurrencyCode)
        }
        return country to currency
    }

    fun saveSelectedCountry(context: Context, isoCode: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_COUNTRY_ISO, isoCode.uppercase(Locale.US))
            .apply()
    }

    fun saveSelectedCurrency(context: Context, currencyCode: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CURRENCY_CODE, currencyCode.uppercase(Locale.US))
            .apply()
    }
}
