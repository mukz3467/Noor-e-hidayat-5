package com.example.data.huqooq

import com.example.data.model.AppLanguage
import java.math.BigInteger
import kotlin.math.abs

/**
 * Exact rational arithmetic so all Quranic shares (1/2, 1/3, 1/4, 1/6, 1/8, 2/3), Awl, and Radd
 * avoid floating-point rounding drift before currency formatting.
 */
data class Rational(val num: Long, val den: Long) {
    val reduced: Rational
        get() {
            if (num == 0L) return ZERO
            val g = gcd(abs(num), abs(den))
            val sign = if (den < 0) -1L else 1L
            return Rational((num * sign) / g, abs(den) / g)
        }

    operator fun plus(other: Rational): Rational {
        val a = BigInteger.valueOf(num).multiply(BigInteger.valueOf(other.den))
            .add(BigInteger.valueOf(other.num).multiply(BigInteger.valueOf(den)))
        val b = BigInteger.valueOf(den).multiply(BigInteger.valueOf(other.den))
        return fromBig(a, b)
    }

    operator fun minus(other: Rational): Rational {
        val a = BigInteger.valueOf(num).multiply(BigInteger.valueOf(other.den))
            .subtract(BigInteger.valueOf(other.num).multiply(BigInteger.valueOf(den)))
        val b = BigInteger.valueOf(den).multiply(BigInteger.valueOf(other.den))
        return fromBig(a, b)
    }

    operator fun times(other: Rational): Rational {
        val a = BigInteger.valueOf(num).multiply(BigInteger.valueOf(other.num))
        val b = BigInteger.valueOf(den).multiply(BigInteger.valueOf(other.den))
        return fromBig(a, b)
    }

    operator fun div(other: Rational): Rational {
        require(other.num != 0L) { "Division by zero rational" }
        val a = BigInteger.valueOf(num).multiply(BigInteger.valueOf(other.den))
        val b = BigInteger.valueOf(den).multiply(BigInteger.valueOf(other.num))
        return fromBig(a, b)
    }

    operator fun compareTo(other: Rational): Int {
        val left = BigInteger.valueOf(num).multiply(BigInteger.valueOf(other.den))
        val right = BigInteger.valueOf(other.num).multiply(BigInteger.valueOf(den))
        return left.compareTo(right)
    }

    fun toDouble(): Double = num.toDouble() / den.toDouble()
    fun formattedFraction(): String = if (den == 1L) "$num/1" else "$num/$den"

    companion object {
        val ZERO = Rational(0, 1)
        val ONE = Rational(1, 1)

        private fun gcd(a: Long, b: Long): Long {
            var x = a
            var y = b
            while (y != 0L) {
                val t = x % y
                x = y
                y = t
            }
            return if (x == 0L) 1L else x
        }

        private fun fromBig(n: BigInteger, d: BigInteger): Rational {
            if (n == BigInteger.ZERO) return ZERO
            val signD = if (d.signum() < 0) BigInteger.valueOf(-1) else BigInteger.ONE
            val absD = d.abs()
            val signedN = n.multiply(signD)
            val g = signedN.abs().gcd(absD)
            return Rational(signedN.divide(g).toLong(), absD.divide(g).toLong())
        }
    }
}

data class InheritanceInput(
    val totalEstate: Double,
    val funeralExpenses: Double = 0.0,
    val debts: Double = 0.0,
    val wasiyyat: Double = 0.0,
    val otherDeductions: Double = 0.0,
    val wasiyyatOverOneThirdHeirsConsent: Boolean = false,
    val hasHusband: Boolean = false,
    val wifeCount: Int = 0,
    val hasFather: Boolean = false,
    val hasMother: Boolean = false,
    val hasPaternalGrandfather: Boolean = false,
    val hasPaternalGrandmother: Boolean = false,
    val hasMaternalGrandmother: Boolean = false,
    val sons: Int = 0,
    val daughters: Int = 0,
    val grandsons: Int = 0, // Son's sons
    val granddaughters: Int = 0, // Son's daughters
    val fullBrothers: Int = 0,
    val fullSisters: Int = 0,
    val consanguineBrothers: Int = 0, // Paternal half-brothers
    val consanguineSisters: Int = 0, // Paternal half-sisters
    val uterineSiblings: Int = 0 // Maternal half-brothers/sisters
)

enum class CalculatorStatus {
    SUCCESS,
    ESTATE_EXHAUSTED_BY_OBLIGATIONS,
    INVALID_INPUT,
    SCHOLARLY_REVIEW_REQUIRED
}

data class HeirShareResult(
    val heirKey: String,
    val label: HuqooqLocalized4,
    val count: Int,
    val shareRole: HuqooqLocalized4,
    val aggregateFraction: Rational,
    val perPersonFraction: Rational,
    val aggregatePercentage: Double,
    val aggregateAmount: Double,
    val perPersonAmount: Double,
    val quranReference: String,
    val explanation: HuqooqLocalized4
)

data class ExcludedHeirInfo(
    val label: HuqooqLocalized4,
    val count: Int,
    val excludedBy: HuqooqLocalized4
)

data class InheritanceCalculationResult(
    val status: CalculatorStatus,
    val totalEstate: Double,
    val funeralExpenses: Double,
    val debts: Double,
    val wasiyyatApplied: Double,
    val otherDeductions: Double,
    val netDistributableEstate: Double,
    val specialRuleApplied: String, // "STANDARD", "AWL", "RADD", "UMARIYYATAYN"
    val shares: List<HeirShareResult> = emptyList(),
    val excludedHeirs: List<ExcludedHeirInfo> = emptyList(),
    val stepByStepExplanation: List<HuqooqLocalized4> = emptyList(),
    val warningsAndAssumptions: List<HuqooqLocalized4> = emptyList(),
    val errorOrReviewMessage: HuqooqLocalized4? = null,
    val quranicReferences: List<String> = emptyList()
)

object InheritanceCalculator {

    private data class RawShare(
        val key: String,
        val label: HuqooqLocalized4,
        val count: Int,
        var role: HuqooqLocalized4,
        var fraction: Rational,
        val isSpouse: Boolean = false,
        val isRaddEligible: Boolean = true,
        val quranRef: String,
        var explanation: HuqooqLocalized4
    )

    fun calculate(input: InheritanceInput): InheritanceCalculationResult {
        // 1. Strict Input Validation
        if (input.totalEstate < 0 || input.funeralExpenses < 0 || input.debts < 0 ||
            input.wasiyyat < 0 || input.otherDeductions < 0 ||
            input.wifeCount < 0 || input.sons < 0 || input.daughters < 0 ||
            input.grandsons < 0 || input.granddaughters < 0 ||
            input.fullBrothers < 0 || input.fullSisters < 0 ||
            input.consanguineBrothers < 0 || input.consanguineSisters < 0 ||
            input.uterineSiblings < 0
        ) {
            return invalidResult(
                input,
                HuqooqLocalized4(
                    en = "Negative values are invalid. Enter 0 or positive numbers for estate, deductions, and heir counts.",
                    ur = "منفی اعداد درست نہیں۔ ترکہ، اخراجات اور ورثاء کی تعداد کے لیے صفر یا مثبت عدد درج کریں۔",
                    ar = "القيم السالبة غير صحيحة. أدخل صفراً أو أرقاماً موجبة.",
                    hi = "ऋणात्मक मान अमान्य हैं। संपत्ति और वारिसों की संख्या के लिए 0 या धनात्मक संख्या दर्ज करें।"
                )
            )
        }

        if (input.hasHusband && input.wifeCount > 0) {
            return invalidResult(
                input,
                HuqooqLocalized4(
                    en = "Contradictory spouse input: A deceased person cannot simultaneously leave both a husband and wife/wives.",
                    ur = "متضاد اندراج: ایک مرحوم کے بیک وقت شوہر اور بیوی دونوں زندہ نہیں ہو سکتے۔ درست انتخاب کریں۔",
                    ar = "إدخال متعارض: لا يجتمع الزوج والزوجة في تركة متوفى واحد.",
                    hi = "विरोधाभासी प्रविष्टि: एक मृतक के एक साथ पति और पत्नी दोनों जीवित नहीं हो सकते।"
                )
            )
        }

        if (input.wifeCount > 4) {
            return invalidResult(
                input,
                HuqooqLocalized4(
                    en = "Islamic law permits at most 4 wives simultaneously. Please verify the number of surviving wives.",
                    ur = "شرعی طور پر بیک وقت زیادہ سے زیادہ 4 بیویاں ہو سکتی ہیں۔ براہِ کرم تعداد درست کریں۔",
                    ar = "الحد الشرعي لتعدد الزوجات هو أربع زوجات.",
                    hi = "इस्लामी क़ानून के अनुसार एक साथ अधिकतम 4 पत्नियाँ हो सकती हैं।"
                )
            )
        }

        // Priority 1 & 2: Funeral expenses & Debts
        val afterFuneralAndDebts = input.totalEstate - input.funeralExpenses - input.debts - input.otherDeductions
        if (input.totalEstate <= 0.0 || afterFuneralAndDebts <= 0.0) {
            return InheritanceCalculationResult(
                status = CalculatorStatus.ESTATE_EXHAUSTED_BY_OBLIGATIONS,
                totalEstate = input.totalEstate,
                funeralExpenses = input.funeralExpenses,
                debts = input.debts,
                wasiyyatApplied = 0.0,
                otherDeductions = input.otherDeductions,
                netDistributableEstate = 0.0,
                specialRuleApplied = "OBLIGATIONS_PRIORITY",
                stepByStepExplanation = listOf(
                    HuqooqLocalized4(
                        en = "In Ilm-ul-Faraid, funeral expenses and unpaid debts take absolute priority over Wasiyyat (bequest) and inheritance (Quran 4:11-12).",
                        ur = "علم الفرائض کے مطابق میت کی تجہیز و تکفین اور قرضوں کی ادائیگی وصیت اور وراثت سے مقدم ہے (النساء 4:11-12)۔",
                        ar = "تُقدَّم مؤونة التجهيز والديون على الوصية والميراث (النساء ١١-١٢).",
                        hi = "इल्म-उल-फ़राइज़ में अंतिम संस्कार खर्च और कर्ज़, वसीयत व विरासत से पहले अदा किए जाते हैं (क़ुरआन 4:11-12)।"
                    )
                ),
                errorOrReviewMessage = HuqooqLocalized4(
                    en = "No net estate remains for distribution after deducting funeral expenses, debts, and legal obligations.",
                    ur = "تجہیز و تکفین، قرضوں اور قانونی واجبات کے بعد تقسیم کے لیے کوئی ترکہ باقی نہیں بچا۔",
                    ar = "لم يتبقَّ مال للورثة بعد سداد التجهيز والديون.",
                    hi = "अंतिम संस्कार खर्च और कर्ज़ चुकाने के बाद वितरण के लिए कोई शेष संपत्ति नहीं बची है।"
                ),
                quranicReferences = listOf("Quran 4:11", "Quran 4:12")
            )
        }

        // Priority 3: Wasiyyat validation (<= 1/3 unless all adult heirs freely consent)
        val oneThirdMax = afterFuneralAndDebts / 3.0
        if (input.wasiyyat > oneThirdMax + 0.01 && !input.wasiyyatOverOneThirdHeirsConsent) {
            return invalidResult(
                input,
                HuqooqLocalized4(
                    en = "Wasiyyat (bequest) exceeds the 1/3 limit (max ${"%.2f".format(oneThirdMax)}). Under authentic Sunnah (Bukhari 2742), a bequest above 1/3 or to an heir is invalid unless all adult heirs freely consent after death.",
                    ur = "وصیت ایک تہائی (1/3، زیادہ سے زیادہ ${"%.2f".format(oneThirdMax)}) سے زائد ہے۔ صحیح بخاری (2742) کے مطابق ایک تہائی سے زائد وصیت تمام عاقل بالغ ورثاء کی آزادانہ رضامندی کے بغیر نافذ نہیں ہوتی۔",
                    ar = "الوصية تتجاوز الثلث (${"%.2f".format(oneThirdMax)})، ولا تنفذ الزيادة إلا بإجازة جميع الورثة الراشدين (البخاري ٢٧٤٢).",
                    hi = "वसीयत एक-तिहाई (1/3, अधिकतम ${"%.2f".format(oneThirdMax)}) से अधिक है। सहीह बुखारी (2742) के अनुसार सभी वयस्क वारिसों की सहमति के बिना यह लागू नहीं होती।"
                )
            )
        }

        val netEstate = (afterFuneralAndDebts - input.wasiyyat).coerceAtLeast(0.0)
        if (netEstate <= 0.0) {
            return InheritanceCalculationResult(
                status = CalculatorStatus.ESTATE_EXHAUSTED_BY_OBLIGATIONS,
                totalEstate = input.totalEstate,
                funeralExpenses = input.funeralExpenses,
                debts = input.debts,
                wasiyyatApplied = input.wasiyyat,
                otherDeductions = input.otherDeductions,
                netDistributableEstate = 0.0,
                specialRuleApplied = "OBLIGATIONS_PRIORITY",
                errorOrReviewMessage = HuqooqLocalized4(
                    en = "No net estate remains after deductions.",
                    ur = "واجبات کی کٹوتی کے بعد ترکہ باقی نہیں بچا۔",
                    ar = "لا يوجد باقٍ لتوزيعه بعد الخصومات.",
                    hi = "कटौतियों के बाद कोई शेष संपत्ति नहीं बची।"
                )
            )
        }

        val totalHeirsEntered = (if (input.hasHusband) 1 else 0) + input.wifeCount +
            (if (input.hasFather) 1 else 0) + (if (input.hasMother) 1 else 0) +
            (if (input.hasPaternalGrandfather) 1 else 0) +
            (if (input.hasPaternalGrandmother) 1 else 0) +
            (if (input.hasMaternalGrandmother) 1 else 0) +
            input.sons + input.daughters + input.grandsons + input.granddaughters +
            input.fullBrothers + input.fullSisters +
            input.consanguineBrothers + input.consanguineSisters +
            input.uterineSiblings

        if (totalHeirsEntered == 0) {
            return scholarlyReviewResult(
                input = input,
                netEstate = netEstate,
                reason = HuqooqLocalized4(
                    en = "No primary Quranic heirs or Asabah were entered. Cases involving only distant kindred (Dhawil-Arham) or unclaimed estates require qualified Mufti and legal review.",
                    ur = "کوئی بنیادی قرآنی وارث یا عصبہ منتخب نہیں کیا گیا۔ صرف ذوی الارحام یا لاوارث ترکہ کے لیے مستند مفتی و عدالت سے رجوع ضروری ہے۔",
                    ar = "لم يُحدد ورثة من أصحاب الفروض أو العصبات. حالات ذوي الأرحام تتطلب مراجعة مفتٍ مختص.",
                    hi = "कोई प्राथमिक क़ुरआनी वारिस या असबा नहीं चुना गया। दूर के रिश्तेदारों (ज़विल-अरहाम) के मामलों के लिए योग्य मुफ़्ती से समीक्षा आवश्यक है।"
                )
            )
        }

        // Check unsupported disputed classical case: Paternal Grandfather + Full/Consanguine Siblings (no father, no male descendant)
        val fatherAbsent = !input.hasFather
        val noMaleDescendant = input.sons == 0 && input.grandsons == 0
        if (input.hasPaternalGrandfather && fatherAbsent && noMaleDescendant &&
            (input.fullBrothers + input.fullSisters + input.consanguineBrothers + input.consanguineSisters) > 0
        ) {
            return scholarlyReviewResult(
                input = input,
                netEstate = netEstate,
                reason = HuqooqLocalized4(
                    en = "Specialist Scholarly Review Required: Paternal grandfather alongside full/paternal siblings involves an established classical difference between Hanafi fiqh (grandfather excludes siblings like the father) and Shafi'i/Maliki/Sahihayn view (grandfather shares with siblings via Muqasamah). Consult a qualified Mufti following your applicable school/jurisdiction.",
                    ur = "مستند مفتی کی رہنمائی درکار ہے: دادا اور حقیقی/علاتی بہن بھائیوں کے اجتماع (الجد مع الإخوة) میں فقہِ حنفی (دادا بہن بھائیوں کو محجوب کرتے ہیں) اور جمہور شافعیہ/مالکیہ (مقاسمہ) کے درمیان معروف فقہی اختلاف موجود ہے۔ مستند مفتی سے رجوع کریں۔",
                    ar = "مسألة تتطلب مراجعة عالم مختص: اجتماع الجد مع الإخوة فيه خلاف فقهي معروف بين الحنفية (حجب الإخوة) والجمهور (المقاسمة).",
                    hi = "योग्य मुफ़्ती समीक्षा आवश्यक: दादा और सगे/सौतेले (पिता की ओर) भाई-बहनों के एक साथ होने पर हनफ़ी और जम्हूर फ़िक़्ह में स्थापित मतभेद है। कृपया मुफ़्ती से परामर्श लें।"
                )
            )
        }

        val excluded = mutableListOf<ExcludedHeirInfo>()
        val steps = mutableListOf<HuqooqLocalized4>()
        val warnings = mutableListOf<HuqooqLocalized4>()
        val activeFurood = mutableListOf<RawShare>()

        steps.add(
            HuqooqLocalized4(
                en = "Step 1: Net distributable estate = ${"%.2f".format(input.totalEstate)} - ${"%.2f".format(input.funeralExpenses)} (funeral) - ${"%.2f".format(input.debts)} (debts) - ${"%.2f".format(input.wasiyyat)} (wasiyyat) - ${"%.2f".format(input.otherDeductions)} (other) = ${"%.2f".format(netEstate)}.",
                ur = "مرحلہ 1: قابلِ تقسیم خالص ترکہ = ${"%.2f".format(netEstate)} (تجہیز، قرض اور جائز وصیت کی کٹوتی کے بعد)۔",
                ar = "الخطوة ١: صافي التركة القابلة للتوزيع = ${"%.2f".format(netEstate)}.",
                hi = "चरण 1: शुद्ध वितरणीय संपत्ति = ${"%.2f".format(netEstate)} (खर्च, कर्ज़ व वसीयत घटाकर)।"
            )
        )

        // 2. Apply Hajb Hirman (Complete Exclusions)
        val hasAnyDescendant = (input.sons + input.daughters + input.grandsons + input.granddaughters) > 0
        val effectiveGrandfather = input.hasPaternalGrandfather && !input.hasFather
        if (input.hasPaternalGrandfather && input.hasFather) {
            excluded.add(ExcludedHeirInfo(label("Paternal Grandfather", "دادا", "الجد لأب", "दादा"), 1, label("Father", "والد (باپ)", "الأب", "पिता")))
        }

        val effectivePaternalGrandmother = input.hasPaternalGrandmother && !input.hasMother && !input.hasFather
        if (input.hasPaternalGrandmother && (input.hasMother || input.hasFather)) {
            val by = if (input.hasMother) label("Mother", "والدہ", "الأم", "माँ") else label("Father", "والد", "الأب", "पिता")
            excluded.add(ExcludedHeirInfo(label("Paternal Grandmother", "دادی", "الجدة لأب", "दादी"), 1, by))
        }
        val effectiveMaternalGrandmother = input.hasMaternalGrandmother && !input.hasMother
        if (input.hasMaternalGrandmother && input.hasMother) {
            excluded.add(ExcludedHeirInfo(label("Maternal Grandmother", "نانی", "الجدة لأم", "नानी"), 1, label("Mother", "والدہ", "الأم", "माँ")))
        }

        val effectiveGrandsons = if (input.sons > 0) 0 else input.grandsons
        if (input.sons > 0 && input.grandsons > 0) {
            excluded.add(ExcludedHeirInfo(label("Grandsons (Son's Sons)", "پوتے", "أبناء الابن", "पोते"), input.grandsons, label("Son(s)", "بیٹا / بیٹے", "الابن", "बेटा")))
        }

        val granddaughtersBlockedBySons = input.sons > 0
        val granddaughtersBlockedByTwoDaughters = !granddaughtersBlockedBySons && input.daughters >= 2 && effectiveGrandsons == 0
        val effectiveGranddaughters = if (granddaughtersBlockedBySons || granddaughtersBlockedByTwoDaughters) 0 else input.granddaughters
        if (granddaughtersBlockedBySons && input.granddaughters > 0) {
            excluded.add(ExcludedHeirInfo(label("Granddaughters (Son's Daughters)", "پوتیاں", "بنات الابن", "पोतियाँ"), input.granddaughters, label("Son(s)", "بیٹا / بیٹے", "الابن", "बेटा")))
        } else if (granddaughtersBlockedByTwoDaughters && input.granddaughters > 0) {
            excluded.add(ExcludedHeirInfo(label("Granddaughters (Son's Daughters)", "پوتیاں", "بنات الابن", "पोतियाँ"), input.granddaughters, label("2+ Daughters exhausting 2/3 female share", "دو یا زائد بیٹیاں (2/3 حصے کی تکمیل)", "بنتان فأكثر", "दो या अधिक बेटियाँ (2/3 पूर्ण)")))
        }

        val maleAscendantPresent = input.hasFather || effectiveGrandfather
        val maleDescendantPresent = input.sons > 0 || effectiveGrandsons > 0
        val femaleDescendantAsAsabahHelper = (input.daughters > 0 || effectiveGranddaughters > 0)

        // Uterine siblings excluded by ANY descendant OR father OR grandfather
        val effectiveUterine = if (hasAnyDescendant || maleAscendantPresent) 0 else input.uterineSiblings
        if (input.uterineSiblings > 0 && (hasAnyDescendant || maleAscendantPresent)) {
            val blocker = when {
                input.hasFather -> label("Father", "والد", "الأب", "पिता")
                effectiveGrandfather -> label("Paternal Grandfather", "دادا", "الجد", "दादा")
                else -> label("Children / Grandchildren (Descendant)", "اولاد / پوتے پوتیاں", "الفرع الوارث", "संतान / पोते")
            }
            excluded.add(ExcludedHeirInfo(label("Maternal (Uterine) Siblings", "اخیافی بہن بھائی", "الإخوة لأم", "मातृ सौतेले भाई-बहन"), input.uterineSiblings, blocker))
        }

        // Full siblings excluded by father or male descendant
        val fullSibsBlocked = input.hasFather || maleDescendantPresent
        val effectiveFullBrothers = if (fullSibsBlocked) 0 else input.fullBrothers
        val effectiveFullSisters = if (fullSibsBlocked) 0 else input.fullSisters
        if (fullSibsBlocked && (input.fullBrothers > 0 || input.fullSisters > 0)) {
            val blocker = if (input.hasFather) label("Father", "والد", "الأب", "पिता") else label("Male Descendant (Son / Grandson)", "مذکر اولاد (بیٹا / پوتا)", "الفرع الوارث المذكر", "बेटा / पोता")
            if (input.fullBrothers > 0) excluded.add(ExcludedHeirInfo(label("Full Brothers", "حقیقی بھائی", "الإخوة الأشقاء", "सगे भाई"), input.fullBrothers, blocker))
            if (input.fullSisters > 0) excluded.add(ExcludedHeirInfo(label("Full Sisters", "حقیقی بہنیں", "الأخوات الشقيقات", "सगी बहनें"), input.fullSisters, blocker))
        }

        // Consanguine siblings excluded by father, male descendant, full brother, OR full sister acting as Asabah ma'al ghayr (with daughter/granddaughter)
        val fullSisterIsAsabahWithDaughters = effectiveFullSisters > 0 && effectiveFullBrothers == 0 && femaleDescendantAsAsabahHelper
        val consanguineBlocked = input.hasFather || maleDescendantPresent || effectiveFullBrothers > 0 || fullSisterIsAsabahWithDaughters
        val effectiveConsanguineBrothers = if (consanguineBlocked) 0 else input.consanguineBrothers
        val consanguineSistersBlockedByTwoFullSisters = !consanguineBlocked && effectiveFullSisters >= 2 && input.consanguineBrothers == 0
        val effectiveConsanguineSisters = if (consanguineBlocked || consanguineSistersBlockedByTwoFullSisters) 0 else input.consanguineSisters

        if (consanguineBlocked && (input.consanguineBrothers > 0 || input.consanguineSisters > 0)) {
            val blocker = when {
                input.hasFather -> label("Father", "والد", "الأب", "पिता")
                maleDescendantPresent -> label("Male Descendant", "بیٹا / پوتا", "الفرع المذكر", "बेटा / पोता")
                effectiveFullBrothers > 0 -> label("Full Brother(s)", "حقیقی بھائی", "الأخ الشقيق", "सगे भाई")
                else -> label("Full Sister (Asabah with Daughter)", "حقیقی بہن (بیٹی کے ساتھ عصبہ)", "الأخت الشقيقة مع البنت", "सगी बहन (बेटी के साथ असबा)")
            }
            if (input.consanguineBrothers > 0) excluded.add(ExcludedHeirInfo(label("Paternal Half-Brothers", "علاتی بھائی", "الإخوة لأب", "पितृ सौतेले भाई"), input.consanguineBrothers, blocker))
            if (input.consanguineSisters > 0) excluded.add(ExcludedHeirInfo(label("Paternal Half-Sisters", "علاتی بہنیں", "الأخوات لأب", "पितृ सौतेली बहनें"), input.consanguineSisters, blocker))
        } else if (consanguineSistersBlockedByTwoFullSisters && input.consanguineSisters > 0) {
            excluded.add(ExcludedHeirInfo(label("Paternal Half-Sisters", "علاتی بہنیں", "الأخوات لأب", "पितृ सौतेली बहनें"), input.consanguineSisters, label("2+ Full Sisters exhausting 2/3 share", "دو یا زائد حقیقی بہنیں (2/3 مکمل)", "شقيقتان فأكثر", "दो+ सगी बहनें")))
        }

        // 3. Assign Fixed Quranic Shares (Ashab-ul-Furood)
        // Spouse
        if (input.hasHusband) {
            val frac = if (hasAnyDescendant) Rational(1, 4) else Rational(1, 2)
            activeFurood.add(
                RawShare(
                    key = "husband",
                    label = label("Husband", "شوہر", "الزوج", "पति"),
                    count = 1,
                    role = roleFixed(),
                    fraction = frac,
                    isSpouse = true,
                    isRaddEligible = false,
                    quranRef = "Quran 4:12",
                    explanation = if (hasAnyDescendant) label("1/4 because surviving descendant exists (4:12).", "اولاد/پوتے پوتیوں کی موجودگی میں 1/4 (النساء 4:12)۔", "الربع لوجود الفرع الوارث (النساء ١٢).", "संतान होने पर 1/4 हिस्सा (क़ुरआन 4:12)।")
                    else label("1/2 because no descendant exists (4:12).", "اولاد نہ ہونے کی وجہ سے 1/2 (النساء 4:12)۔", "النصف لعدم الفرع الوارث (النساء ١٢).", "संतान न होने पर 1/2 हिस्सा (क़ुरआन 4:12)।")
                )
            )
        } else if (input.wifeCount > 0) {
            val frac = if (hasAnyDescendant) Rational(1, 8) else Rational(1, 4)
            activeFurood.add(
                RawShare(
                    key = "wife",
                    label = if (input.wifeCount > 1) label("Wives", "بیویاں", "الزوجات", "पत्नियाँ") else label("Wife", "بیوی", "الزوجة", "पत्नी"),
                    count = input.wifeCount,
                    role = roleFixed(),
                    fraction = frac,
                    isSpouse = true,
                    isRaddEligible = false,
                    quranRef = "Quran 4:12",
                    explanation = if (hasAnyDescendant) label("1/8 shared equally because surviving descendant exists (4:12).", "اولاد/پوتے پوتیوں کی موجودگی میں 1/8 مشترکہ (النساء 4:12)۔", "الثمن لوجود الفرع الوارث (النساء ١٢).", "संतान होने पर 1/8 हिस्सा बराबर (क़ुरआन 4:12)।")
                    else label("1/4 shared equally because no descendant exists (4:12).", "اولاد نہ ہونے کی وجہ سے 1/4 مشترکہ (النساء 4:12)۔", "الربع لعدم الفرع الوارث (النساء ١٢).", "संतान न होने पर 1/4 हिस्सा बराबर (क़ुरआन 4:12)।")
                )
            )
        }

        // Check Umariyyatayn (Spouse + Mother + Father, NO descendants, <2 siblings)
        val totalSiblingsRaw = input.fullBrothers + input.fullSisters + input.consanguineBrothers + input.consanguineSisters + input.uterineSiblings
        val isUmariyyatayn = (input.hasHusband || input.wifeCount > 0) &&
            input.hasFather && input.hasMother &&
            !hasAnyDescendant && totalSiblingsRaw < 2

        if (isUmariyyatayn) {
            val spouseShare = activeFurood.first { it.isSpouse }.fraction
            val remainderAfterSpouse = Rational.ONE - spouseShare
            val motherFrac = remainderAfterSpouse * Rational(1, 3)
            val fatherFrac = remainderAfterSpouse - motherFrac
            activeFurood.add(
                RawShare(
                    key = "mother",
                    label = label("Mother", "والدہ (ماں)", "الأم", "माँ"),
                    count = 1,
                    role = label("Umariyyatayn (1/3 of Remainder)", "غراوین (باقی ماندہ کا تہائی)", "ثلث الباقي (العمريتان)", "उमरिय्यतैन (शेष का 1/3)"),
                    fraction = motherFrac,
                    quranRef = "Quran 4:11 (Sahabah Consensus)",
                    explanation = label(
                        "Umariyyatayn case: Mother takes 1/3 of the remainder after the spouse's share (${motherFrac.formattedFraction()}), preserving father's 2:1 ratio over mother.",
                        "مسئلۂ عمریتین (غراوین): زوج یا زوجہ کے حصے کے بعد باقی ترکے کا تہائی والدہ کو (${motherFrac.formattedFraction()}) ملتا ہے تاکہ باپ کا حصہ ماں سے دوگنا رہے۔",
                        "في العمريتين تأخذ الأم ثلث الباقي بعد فرض أحد الزوجين (${motherFrac.formattedFraction()}).",
                        "उमरिय्यतैन नियम: जीवनसाथी के हिस्से के बाद बची संपत्ति का 1/3 माँ को मिलता है (${motherFrac.formattedFraction()})।"
                    )
                )
            )
            activeFurood.add(
                RawShare(
                    key = "father",
                    label = label("Father", "والد (باپ)", "الأب", "पिता"),
                    count = 1,
                    role = roleAsabah(),
                    fraction = fatherFrac,
                    quranRef = "Quran 4:11",
                    explanation = label(
                        "Father takes the remaining 2/3 after spouse and mother (${fatherFrac.formattedFraction()}).",
                        "والد عصبہ ہونے کی حیثیت سے باقی ماندہ حصہ (${fatherFrac.formattedFraction()}) حاصل کرتے ہیں۔",
                        "يأخذ الأب الباقي تعصيباً (${fatherFrac.formattedFraction()}).",
                        "पिता शेष असबा हिस्सा (${fatherFrac.formattedFraction()}) प्राप्त करते हैं।"
                    )
                )
            )
            steps.add(
                HuqooqLocalized4(
                    en = "Applied classical Umariyyatayn precedent: Mother receives 1/3 of remainder after spouse.",
                    ur = "اجماعِ صحابہ کے مطابق مسئلۂ عمریتین لاگو کیا گیا۔",
                    ar = "تطبيق مسألة العمريتين بإجماع الصحابة.",
                    hi = "शास्त्रीय उमरिय्यतैन नियम लागू किया गया।"
                )
            )
            return buildFinalResult(
                input = input,
                netEstate = netEstate,
                specialRule = "UMARIYYATAYN",
                rawShares = activeFurood,
                excluded = excluded,
                steps = steps,
                warnings = warnings
            )
        }

        // Mother (non-Umariyyatayn)
        if (input.hasMother) {
            val motherGetsSixth = hasAnyDescendant || totalSiblingsRaw >= 2
            val frac = if (motherGetsSixth) Rational(1, 6) else Rational(1, 3)
            activeFurood.add(
                RawShare(
                    key = "mother",
                    label = label("Mother", "والدہ (ماں)", "الأم", "माँ"),
                    count = 1,
                    role = roleFixed(),
                    fraction = frac,
                    quranRef = "Quran 4:11",
                    explanation = if (motherGetsSixth) {
                        label("1/6 because descendant or 2+ siblings exist (4:11).", "اولاد یا دو یا زائد بہن بھائیوں کی موجودگی میں 1/6 (النساء 4:11)۔", "السدس لوجود الفرع الوارث أو جمع من الإخوة (النساء ١١).", "संतान या 2+ भाई-बहन होने पर 1/6 (क़ुरआन 4:11)।")
                    } else {
                        label("1/3 because no descendant and fewer than 2 siblings exist (4:11).", "اولاد اور دو بہن بھائیوں کی عدم موجودگی میں 1/3 (النساء 4:11)۔", "الثلث لعدم الفرع وجمع الإخوة (النساء ١١).", "संतान व 2+ भाई-बहन न होने पर 1/3 (क़ुरआन 4:11)।")
                    }
                )
            )
        }

        // Grandmothers (1/6 shared if active)
        val activeGmCount = (if (effectiveMaternalGrandmother) 1 else 0) + (if (effectivePaternalGrandmother) 1 else 0)
        if (activeGmCount > 0) {
            val eachGmFrac = if (activeGmCount == 2) Rational(1, 12) else Rational(1, 6)
            if (effectiveMaternalGrandmother) {
                activeFurood.add(
                    RawShare(
                        key = "maternal_grandmother",
                        label = label("Maternal Grandmother", "نانی", "الجدة لأم", "नानी"),
                        count = 1,
                        role = roleFixed(),
                        fraction = eachGmFrac,
                        quranRef = "Sunnah (Abu Dawud / Muwatta)",
                        explanation = label("Grandmothers collectively take 1/6 when mother is absent.", "والدہ کی عدم موجودگی میں جدات کو مجموعی طور پر 1/6 ملتا ہے۔", "للجدة أو الجدات السدس عند عدم الأم.", "माँ के न होने पर दादी/नानी को सामूहिक 1/6 मिलता है।")
                    )
                )
            }
            if (effectivePaternalGrandmother) {
                activeFurood.add(
                    RawShare(
                        key = "paternal_grandmother",
                        label = label("Paternal Grandmother", "دادی", "الجدة لأب", "दादी"),
                        count = 1,
                        role = roleFixed(),
                        fraction = eachGmFrac,
                        quranRef = "Sunnah (Abu Dawud / Muwatta)",
                        explanation = label("Grandmothers collectively take 1/6 when mother & father are absent.", "والدین کی عدم موجودگی میں دادی کو 1/6 (یا مشترک 1/12) ملتا ہے۔", "للجدة لأب السدس مع عدم الأب والأم.", "माँ-पिता के न होने पर दादी को 1/6 मिलता है।")
                    )
                )
            }
        }

        // Father / Paternal Grandfather fixed portion
        var fatherIndex = -1
        if (input.hasFather) {
            if (hasAnyDescendant) {
                fatherIndex = activeFurood.size
                activeFurood.add(
                    RawShare(
                        key = "father",
                        label = label("Father", "والد (باپ)", "الأب", "पिता"),
                        count = 1,
                        role = if (maleDescendantPresent) roleFixed() else roleFixedPlusAsabah(),
                        fraction = Rational(1, 6),
                        quranRef = "Quran 4:11",
                        explanation = label("1/6 fixed share with surviving descendants (plus residue if only female descendants exist).", "اولاد کی موجودگی میں والد کا فرض 1/6 ہے (اور اگر صرف بیٹیاں ہوں تو بقیہ بھی عصبہ کے طور پر ملے گا)۔", "للأب السدس فرضاً مع الفرع الوارث ويأخذ الباقي تعصيباً إن كان الفرع أنثى فقط.", "संतान होने पर पिता का 1/6 निश्चित हिस्सा (केवल बेटियाँ होने पर शेष भी)।")
                    )
                )
            }
        } else if (effectiveGrandfather && hasAnyDescendant) {
            fatherIndex = activeFurood.size
            activeFurood.add(
                RawShare(
                    key = "grandfather",
                    label = label("Paternal Grandfather", "دادا", "الجد لأب", "दादा"),
                    count = 1,
                    role = if (maleDescendantPresent) roleFixed() else roleFixedPlusAsabah(),
                    fraction = Rational(1, 6),
                    quranRef = "Quran 4:11 (Qiyas / Consensus)",
                    explanation = label("1/6 fixed share in place of father when descendants exist.", "والد کی عدم موجودگی میں دادا کو اولاد کے ساتھ 1/6 فرض ملتا ہے۔", "يقوم الجد مقام الأب فيأخذ السدس مع الفرع الوارث.", "पिता की अनुपस्थिति में दादा को 1/6 मिलता है।")
                )
            )
        }

        // Daughters (when sons == 0 -> fixed share)
        if (input.sons == 0 && input.daughters > 0) {
            val frac = if (input.daughters == 1) Rational(1, 2) else Rational(2, 3)
            activeFurood.add(
                RawShare(
                    key = "daughters",
                    label = if (input.daughters == 1) label("Daughter", "بیٹی", "البنت", "बेटी") else label("Daughters", "بیٹیاں", "البنات", "बेटियाँ"),
                    count = input.daughters,
                    role = roleFixed(),
                    fraction = frac,
                    quranRef = "Quran 4:11",
                    explanation = if (input.daughters == 1) label("1/2 for a single daughter when no son exists (4:11).", "بیٹا نہ ہونے پر اکیلی بیٹی کا حصہ 1/2 (النساء 4:11)۔", "للبنت الواحدة النصف لعدم الابن (النساء ١١).", "बेटा न होने पर एकलौती बेटी को 1/2 (क़ुरआन 4:11)।")
                    else label("2/3 shared equally among daughters when no son exists (4:11).", "بیٹا نہ ہونے پر دو یا زائد بیٹیوں کو مشترکہ 2/3 (النساء 4:11)۔", "للبنتين فأكثر الثلثان (النساء ١١).", "बेटा न होने पर दो या अधिक बेटियों को 2/3 बराबर (क़ुरआन 4:11)।")
                )
            )
        }

        // Granddaughters (when effectiveGrandsons == 0 and effectiveGranddaughters > 0)
        if (effectiveGrandsons == 0 && effectiveGranddaughters > 0) {
            val frac = when {
                input.daughters == 0 && effectiveGranddaughters == 1 -> Rational(1, 2)
                input.daughters == 0 && effectiveGranddaughters >= 2 -> Rational(2, 3)
                else -> Rational(1, 6) // input.daughters == 1 -> completing 2/3
            }
            activeFurood.add(
                RawShare(
                    key = "granddaughters",
                    label = label("Granddaughters (Son's Daughters)", "پوتیاں", "بنات الابن", "पोतियाँ"),
                    count = effectiveGranddaughters,
                    role = roleFixed(),
                    fraction = frac,
                    quranRef = "Bukhari 6736 & Quran 4:11",
                    explanation = if (input.daughters == 1) label("1/6 to complete the 2/3 female share alongside 1 daughter (Bukhari 6736).", "ایک بیٹی کے ساتھ پوتیوں کو 2/3 کی تکمیل کے لیے 1/6 ملتا ہے (بخاری 6736)۔", "لبنات الابن السدس تكملة للثلثين مع البنت الواحدة.", "एक बेटी के साथ 2/3 पूरा करने हेतु पोतियों को 1/6।")
                    else label("Fixed daughter-equivalent share in absence of closer children.", "بلاواسطہ اولاد نہ ہونے پر پوتیوں کو بیٹیوں کا مقررہ حصہ ملتا ہے۔", "تأخذ بنات الابن فرض البنات عند عدمهن.", "सीधी संतान न होने पर बेटियों जैसा हिस्सा।")
                )
            )
        }

        // Uterine Siblings (if active)
        if (effectiveUterine > 0) {
            val frac = if (effectiveUterine == 1) Rational(1, 6) else Rational(1, 3)
            activeFurood.add(
                RawShare(
                    key = "uterine_siblings",
                    label = label("Maternal (Uterine) Siblings", "اخیافی بہن بھائی", "الإخوة لأم", "मातृ सौतेले भाई-बहन"),
                    count = effectiveUterine,
                    role = roleFixed(),
                    fraction = frac,
                    quranRef = "Quran 4:12",
                    explanation = if (effectiveUterine == 1) label("1/6 in Kalalah when no parent or descendant exists (4:12).", "کلالہ کی صورت میں ایک اخیافی بھائی یا بہن کا حصہ 1/6 (النساء 4:12)۔", "للأخ أو الأخت لأم السدس في الكلالة (النساء ١٢).", "कलालह स्थिति में एक मातृ भाई/बहन को 1/6 (क़ुरआन 4:12)।")
                    else label("1/3 shared equally (males & females equal) in Kalalah (4:12).", "دو یا زائد اخیافی بہن بھائیوں میں 1/3 برابر تقسیم (مذکر و مؤنث برابر)۔", "للاثنين فأكثر من الإخوة لأم الثلث بينهم بالسوية (النساء ١٢).", "दो या अधिक मातृ भाई-बहनों में 1/3 समान विभाजन (क़ुरआन 4:12)।")
                )
            )
        }

        // Full Sisters (fixed only if no full brothers and not Asabah ma'al ghayr)
        if (effectiveFullBrothers == 0 && effectiveFullSisters > 0 && !femaleDescendantAsAsabahHelper) {
            val frac = if (effectiveFullSisters == 1) Rational(1, 2) else Rational(2, 3)
            activeFurood.add(
                RawShare(
                    key = "full_sisters",
                    label = if (effectiveFullSisters == 1) label("Full Sister", "حقیقی بہن", "الأخت الشقيقة", "सगी बहन") else label("Full Sisters", "حقیقی بہنیں", "الأخوات الشقيقات", "सगी बहनें"),
                    count = effectiveFullSisters,
                    role = roleFixed(),
                    fraction = frac,
                    quranRef = "Quran 4:176",
                    explanation = if (effectiveFullSisters == 1) label("1/2 for a single full sister in Kalalah (4:176).", "کلالہ کی صورت میں اکیلی حقیقی بہن کا حصہ 1/2 (النساء 4:176)۔", "للأخت الشقيقة الواحدة النصف (النساء ١٧٦).", "कलालह में एक सगी बहन को 1/2 (क़ुरआन 4:176)।")
                    else label("2/3 shared equally among full sisters in Kalalah (4:176).", "دو یا زائد حقیقی بہنوں کو مشترکہ 2/3 (النساء 4:176)۔", "للأختين الشقيقتين فأكثر الثلثان (النساء ١٧٦).", "दो या अधिक सगी बहनों को 2/3 (क़ुरआन 4:176)।")
                )
            )
        }

        // Consanguine Sisters (fixed only if no consanguine brothers and not Asabah ma'al ghayr)
        if (effectiveConsanguineBrothers == 0 && effectiveConsanguineSisters > 0 && !femaleDescendantAsAsabahHelper) {
            val frac = when {
                effectiveFullSisters == 1 -> Rational(1, 6)
                effectiveConsanguineSisters == 1 -> Rational(1, 2)
                else -> Rational(2, 3)
            }
            activeFurood.add(
                RawShare(
                    key = "consanguine_sisters",
                    label = label("Paternal Half-Sisters", "علاتی بہنیں", "الأخوات لأب", "पितृ सौतेली बहनें"),
                    count = effectiveConsanguineSisters,
                    role = roleFixed(),
                    fraction = frac,
                    quranRef = "Quran 4:176",
                    explanation = if (effectiveFullSisters == 1) label("1/6 completing 2/3 alongside one full sister.", "ایک حقیقی بہن کے ساتھ 2/3 مکمل کرنے کے لیے 1/6 حصہ۔", "السدس تكملة للثلثين مع الأخت الشقيقة الواحدة.", "एक सगी बहन के साथ 2/3 पूर्ण करने हेतु 1/6 हिस्सा।")
                    else label("Fixed sister share in absence of closer siblings.", "قریبی بھائی بہن نہ ہونے پر علاتی بہنوں کا مقررہ حصہ۔", "فرض الأخوات لأب عند عدم الأشقاء.", "सगे भाई-बहन न होने पर निश्चित हिस्सा।")
                )
            )
        }

        // 4. Sum fixed shares & resolve Asabah / Awl / Radd
        val fixedSum = activeFurood.fold(Rational.ZERO) { acc, item -> acc + item.fraction }

        val finalShares = activeFurood.toMutableList()
        var specialRule = "STANDARD"

        if (fixedSum > Rational.ONE) {
            // AWL (Proportional Reduction)
            specialRule = "AWL"
            finalShares.forEach { share ->
                share.fraction = share.fraction / fixedSum
                share.role = label("Fixed Share (Adjusted via Awl)", "مقررہ حصہ (عول کے ساتھ)", "فرض مع العول", "निश्चित हिस्सा (औल समायोजित)")
            }
            steps.add(
                HuqooqLocalized4(
                    en = "Sum of fixed Quranic shares (${fixedSum.formattedFraction()}) exceeded 1. Applied Awl (proportional reduction so shares sum cleanly to 1).",
                    ur = "اصحاب الفروض کے حصوں کا مجموعہ (${fixedSum.formattedFraction()}) کل ترکے سے بڑھ گیا، اس لیے عول (متناسب کمی) لاگو کیا گیا۔",
                    ar = "تجاوز مجموع الفروض الواحد الصحيح (${fixedSum.formattedFraction()})، فتم تطبيق العول بالتناسب.",
                    hi = "निश्चित हिस्सों का योग (${fixedSum.formattedFraction()}) 1 से अधिक होने पर औल (आनुपातिक कटौती) लागू किया गया।"
                )
            )
        } else {
            val remainder = Rational.ONE - fixedSum
            if (remainder > Rational.ZERO) {
                var remainderDistributed = false

                // Asabah Priority Order:
                // 1. Sons (+ daughters 2:1)
                if (input.sons > 0) {
                    val units = input.sons * 2 + input.daughters
                    val unitFrac = remainder * Rational(1, units.toLong())
                    finalShares.add(
                        RawShare(
                            key = "sons",
                            label = if (input.sons == 1) label("Son", "بیٹا", "الابن", "बेटा") else label("Sons", "بیٹے", "الأبناء", "बेटे"),
                            count = input.sons,
                            role = roleAsabah(),
                            fraction = unitFrac * Rational((input.sons * 2).toLong(), 1),
                            quranRef = "Quran 4:11",
                            explanation = label("Male receives twice the share of a female (2:1) from residue (4:11).", "عصبہ کے طور پر بیٹے کو بیٹی سے دوگنا حصہ ملتا ہے (النساء 4:11)۔", "يأخذ الابن ضعف حظ البنت تعصيباً (النساء ١١).", "असबा के रूप में बेटे को बेटी से दोगुना हिस्सा मिलता है (क़ुरआन 4:11)।")
                        )
                    )
                    if (input.daughters > 0) {
                        finalShares.add(
                            RawShare(
                                key = "daughters",
                                label = if (input.daughters == 1) label("Daughter", "بیٹی", "البنت", "बेटी") else label("Daughters", "بیٹیاں", "البنات", "बेटियाँ"),
                                count = input.daughters,
                                role = label("Asabah with Brother (2:1)", "بھائی کے ساتھ عصبہ (2:1)", "عصبة بالغير", "भाई के साथ असबा"),
                                fraction = unitFrac * Rational(input.daughters.toLong(), 1),
                                quranRef = "Quran 4:11",
                                explanation = label("Daughters share residue with sons in a 1:2 ratio (4:11).", "بیٹوں کے ساتھ مل کر 1:2 کے تناسب سے بقیہ ترکے میں شریک (النساء 4:11)۔", "تشترك البنات مع البنين للذكر مثل حظ الأنثيين.", "बेटों के साथ 1:2 अनुपात में शेष हिस्सा।")
                            )
                        )
                    }
                    remainderDistributed = true
                }
                // 2. Grandsons (+ effective granddaughters 2:1)
                else if (effectiveGrandsons > 0) {
                    val units = effectiveGrandsons * 2 + effectiveGranddaughters
                    val unitFrac = remainder * Rational(1, units.toLong())
                    finalShares.add(
                        RawShare(
                            key = "grandsons",
                            label = label("Grandsons (Son's Sons)", "پوتے", "أبناء الابن", "पोते"),
                            count = effectiveGrandsons,
                            role = roleAsabah(),
                            fraction = unitFrac * Rational((effectiveGrandsons * 2).toLong(), 1),
                            quranRef = "Quran 4:11 & Sunnah",
                            explanation = label("Son's sons inherit residue in place of sons.", "بیٹوں کی عدم موجودگی میں پوتے عصبہ بنتے ہیں۔", "أبناء الابن عصبة عند عدم الابن.", "बेटों की अनुपस्थिति में पोते असबा होते हैं।")
                        )
                    )
                    if (effectiveGranddaughters > 0) {
                        finalShares.add(
                            RawShare(
                                key = "granddaughters",
                                label = label("Granddaughters (Son's Daughters)", "پوتیاں", "بنات الابن", "पोतियाँ"),
                                count = effectiveGranddaughters,
                                role = label("Asabah with Grandson", "پوتے کے ساتھ عصبہ", "عصبة بالغير", "पोते के साथ असबा"),
                                fraction = unitFrac * Rational(effectiveGranddaughters.toLong(), 1),
                                quranRef = "Quran 4:11",
                                explanation = label("Granddaughters become Asabah with grandsons (2:1).", "پوتوں کے ساتھ پوتیاں 1:2 سے عصبہ بنتی ہیں۔", "تعصب بنت الابن بأخيها ابن الابن.", "पोतों के साथ 1:2 में असबा।")
                            )
                        )
                    }
                    remainderDistributed = true
                }
                // 3. Father (or Grandfather when no father & no sibling dispute)
                else if (input.hasFather) {
                    if (fatherIndex >= 0) {
                        val f = finalShares[fatherIndex]
                        f.fraction = f.fraction + remainder
                        f.role = roleFixedPlusAsabah()
                    } else {
                        finalShares.add(
                            RawShare(
                                key = "father",
                                label = label("Father", "والد (باپ)", "الأب", "पिता"),
                                count = 1,
                                role = roleAsabah(),
                                fraction = remainder,
                                quranRef = "Quran 4:11",
                                explanation = label("Father takes remaining estate as primary Asabah when no descendants exist.", "اولاد نہ ہونے پر والد عصبہ کے طور پر بقیہ پورا ترکہ حاصل کرتے ہیں۔", "يأخذ الأب الباقي تعصيباً لعدم الفرع الوارث.", "संतान न होने पर पिता शेष असबा संपत्ति लेते हैं।")
                            )
                        )
                    }
                    remainderDistributed = true
                } else if (effectiveGrandfather) {
                    if (fatherIndex >= 0) {
                        val gf = finalShares[fatherIndex]
                        gf.fraction = gf.fraction + remainder
                        gf.role = roleFixedPlusAsabah()
                    } else {
                        finalShares.add(
                            RawShare(
                                key = "grandfather",
                                label = label("Paternal Grandfather", "دادا", "الجد لأب", "दादा"),
                                count = 1,
                                role = roleAsabah(),
                                fraction = remainder,
                                quranRef = "Consensus (when father absent)",
                                explanation = label("Grandfather takes remainder when father & siblings are absent.", "والد اور بھائیوں کی عدم موجودگی میں دادا عصبہ کے طور پر باقی ترکہ پاتے ہیں۔", "يأخذ الجد الباقي تعصيباً.", "पिता और भाई न होने पर दादा शेष असबा लेते हैं।")
                            )
                        )
                    }
                    remainderDistributed = true
                }
                // 4. Full Brothers (+ Full Sisters 2:1)
                else if (effectiveFullBrothers > 0) {
                    val units = effectiveFullBrothers * 2 + effectiveFullSisters
                    val unitFrac = remainder * Rational(1, units.toLong())
                    finalShares.add(
                        RawShare(
                            key = "full_brothers",
                            label = label("Full Brothers", "حقیقی بھائی", "الإخوة الأشقاء", "सगे भाई"),
                            count = effectiveFullBrothers,
                            role = roleAsabah(),
                            fraction = unitFrac * Rational((effectiveFullBrothers * 2).toLong(), 1),
                            quranRef = "Quran 4:176",
                            explanation = label("Full brothers (with full sisters 2:1) take remaining estate in Kalalah (4:176).", "کلالہ میں حقیقی بھائی عصبہ بن کر باقی ترکہ لیتے ہیں (النساء 4:176)۔", "الإخوة الأشقاء عصبة في الكلالة (النساء ١٧٦).", "कलालह में सगे भाई शेष असबा लेते हैं (क़ुरआन 4:176)।")
                        )
                    )
                    if (effectiveFullSisters > 0) {
                        finalShares.add(
                            RawShare(
                                key = "full_sisters",
                                label = label("Full Sisters", "حقیقی بہنیں", "الأخوات الشقيقات", "सगी बहनें"),
                                count = effectiveFullSisters,
                                role = label("Asabah with Brother (2:1)", "بھائی کے ساتھ عصبہ", "عصبة بالغير", "भाई के साथ असबा"),
                                fraction = unitFrac * Rational(effectiveFullSisters.toLong(), 1),
                                quranRef = "Quran 4:176",
                                explanation = label("Full sisters share residue with full brothers 1:2 (4:176).", "حقیقی بھائیوں کے ساتھ 1:2 کے تناسب سے شریک (النساء 4:176)۔", "تشترك الأخت الشقيقة مع أخيها للذكر مثل حظ الأنثيين.", "भाई के साथ 1:2 अनुपात में असबा।")
                            )
                        )
                    }
                    remainderDistributed = true
                }
                // 5. Full Sisters as Asabah ma'al Ghayr (with daughters/granddaughters)
                else if (fullSisterIsAsabahWithDaughters) {
                    finalShares.add(
                        RawShare(
                            key = "full_sisters",
                            label = label("Full Sisters", "حقیقی بہنیں", "الأخوات الشقيقات", "सगी बहनें"),
                            count = effectiveFullSisters,
                            role = label("Asabah with Female Descendant", "بیٹی/پوتی کے ساتھ عصبہ مع الغیر", "عصبة مع الغير", "बेटी के साथ असबा मअल-ग़ैर"),
                            fraction = remainder,
                            quranRef = "Bukhari 6736",
                            explanation = label("Authentic Prophetic ruling: With daughters/granddaughters and no brothers, full sisters become residuary heirs (Bukhari 6736).", "صحیح بخاری (6736): بیٹیوں کی موجودگی اور بھائی کی عدم موجودگی میں حقیقی بہن عصبہ بن کر باقی ترکہ پاتی ہے۔", "الأخوات مع البنات عصبات (صحيح البخاري ٦٧٣٦).", "सहीह बुखारी (6736): बेटियों के साथ और भाई के बिना, सगी बहन असबा होती है।")
                        )
                    )
                    remainderDistributed = true
                }
                // 6. Consanguine Brothers (+ Consanguine Sisters 2:1)
                else if (effectiveConsanguineBrothers > 0) {
                    val units = effectiveConsanguineBrothers * 2 + effectiveConsanguineSisters
                    val unitFrac = remainder * Rational(1, units.toLong())
                    finalShares.add(
                        RawShare(
                            key = "consanguine_brothers",
                            label = label("Paternal Half-Brothers", "علاتی بھائی", "الإخوة لأب", "पितृ सौतेले भाई"),
                            count = effectiveConsanguineBrothers,
                            role = roleAsabah(),
                            fraction = unitFrac * Rational((effectiveConsanguineBrothers * 2).toLong(), 1),
                            quranRef = "Quran 4:176",
                            explanation = label("Paternal half-brothers inherit residue when closer Asabah are absent.", "قریبی عصبہ نہ ہونے پر علاتی بھائی باقی ترکہ پاتے ہیں۔", "الإخوة لأب عصبة عند عدم الأشقاء.", "सगे भाई न होने पर पितृ सौतेले भाई असबा होते हैं।")
                        )
                    )
                    if (effectiveConsanguineSisters > 0) {
                        finalShares.add(
                            RawShare(
                                key = "consanguine_sisters",
                                label = label("Paternal Half-Sisters", "علاتی بہنیں", "الأخوات لأب", "पितृ सौतेली बहनें"),
                                count = effectiveConsanguineSisters,
                                role = label("Asabah with Brother (2:1)", "بھائی کے ساتھ عصبہ", "عصبة بالغير", "भाई के साथ असबा"),
                                fraction = unitFrac * Rational(effectiveConsanguineSisters.toLong(), 1),
                                quranRef = "Quran 4:176",
                                explanation = label("Paternal half-sisters share residue with paternal half-brothers 1:2.", "علاتی بھائیوں کے ساتھ 1:2 میں عصبہ۔", "تشترك الأخت لأب مع أخيها للذكر مثل حظ الأنثيين.", "पितृ सौतेले भाई के साथ 1:2 में असबा।")
                            )
                        )
                    }
                    remainderDistributed = true
                }
                // 7. Consanguine Sisters as Asabah ma'al Ghayr (with daughters/granddaughters, no full sisters)
                else if (effectiveConsanguineSisters > 0 && femaleDescendantAsAsabahHelper) {
                    finalShares.add(
                        RawShare(
                            key = "consanguine_sisters",
                            label = label("Paternal Half-Sisters", "علاتی بہنیں", "الأخوات لأب", "पितृ सौतेली बहनें"),
                            count = effectiveConsanguineSisters,
                            role = label("Asabah with Female Descendant", "بیٹی کے ساتھ عصبہ", "عصبة مع الغير", "बेटी के साथ असबा"),
                            fraction = remainder,
                            quranRef = "Classical Sunni Fiqh",
                            explanation = label("With daughters/granddaughters and no full siblings, paternal half-sisters act as Asabah ma'al ghayr.", "حقیقی بہن بھائی نہ ہونے اور بیٹی کی موجودگی میں علاتی بہن عصبہ بنتی ہے۔", "الأخت لأب مع البنت عصبة مع الغير.", "सगी बहन न होने पर बेटी के साथ असबा।")
                        )
                    )
                    remainderDistributed = true
                }

                // 5. If fixedSum < 1 and NO Asabah existed -> RADD!
                if (!remainderDistributed) {
                    val bloodFurood = finalShares.filter { !it.isSpouse && it.isRaddEligible }
                    if (bloodFurood.isEmpty()) {
                        // Only spouse exists with no blood Furood and no Asabah
                        return scholarlyReviewResult(
                            input = input,
                            netEstate = netEstate,
                            reason = HuqooqLocalized4(
                                en = "Spouse Only Case (No Blood Furood or Asabah): In classical fiqh, a surviving spouse takes their fixed share (1/2 husband or 1/4 wife), while the remaining estate goes to distant kindred (Dhawil-Arham) if any exist, or Bait-ul-Mal / Radd under contemporary legal codes. Consult a qualified Mufti regarding surviving extended relatives.",
                                ur = "صرف میاں یا بیوی کا کیس: جب کوئی نسبی وارث یا عصبہ موجود نہ ہو تو زوج/زوجہ کو ان کا فرض ملتا ہے اور باقی ترکہ ذوی الارحام یا بیت المال/عدالتی قانون کے تحت طے ہوتا ہے۔ مستند مفتی سے رجوع کریں۔",
                                ar = "حالة وجود أحد الزوجين فقط دون فروع أو أصول أو عصبات تتطلب مراجعة مفتٍ للتحقق من ذوي الأرحام أو بيت المال.",
                                hi = "केवल जीवनसाथी का मामला: कोई रक्त वारिस न होने पर शेष संपत्ति हेतु दूर के रिश्तेदारों (ज़विल-अरहाम) या मुफ़्ती सलाह आवश्यक है।"
                            )
                        )
                    }

                    specialRule = "RADD"
                    val spouseTotal = finalShares.filter { it.isSpouse }.fold(Rational.ZERO) { a, s -> a + s.fraction }
                    val bloodBaseTotal = bloodFurood.fold(Rational.ZERO) { a, s -> a + s.fraction }
                    val poolForBloodHeirs = Rational.ONE - spouseTotal

                    bloodFurood.forEach { s ->
                        s.fraction = (s.fraction / bloodBaseTotal) * poolForBloodHeirs
                        s.role = label("Fixed + Radd Return", "فرض + رد (واپسی)", "فرض + رد", "निश्चित + रद (वापसी)")
                    }
                    steps.add(
                        HuqooqLocalized4(
                            en = "Surplus remainder (${remainder.formattedFraction()}) with no residuary Asabah -> Applied Radd (proportional return exclusively to blood-related Quranic heirs; spouse does not receive Radd under majority Sunni fiqh).",
                            ur = "عصبہ نہ ہونے کی وجہ سے بقیہ ترکہ (${remainder.formattedFraction()}) رد کے اصول کے تحت صرف نسبی اصحاب الفروض کو متناسب طور پر واپس لوٹا دیا گیا (جمہور فقہاء کے نزدیک میاں/بیوی پر رد نہیں ہوتا)۔",
                            ar = "تم تطبيق الرد بنسبة فروض الورثة النسبيين دون الزوجين على قول جمهور الفقهاء لعدم وجود عاصب.",
                            hi = "कोई असबा न होने से शेष भाग रद के तहत आनुपातिक रूप से रक्त-संबंधी क़ुरआनी वारिसों को लौटाया गया (बहुमत फ़िक़्ह के अनुसार जीवनसाथी को रद नहीं मिलता)।"
                        )
                    )
                    warnings.add(
                        HuqooqLocalized4(
                            en = "Fiqh Disclosure: Majority Sunni jurisprudence (Hanafi, Shafi'i, Hanbali) excludes spouses from Radd when blood Ashab-ul-Furood exist.",
                            ur = "فقہی وضاحت: جمہور اہلِ سنت فقہاء (حنفی، شافعی، حنبلی) کے مطابق نسبی اصحاب الفروض کی موجودگی میں زوج یا زوجہ کو رد میں سے حصہ نہیں ملتا۔",
                            ar = "توضيح فقهي: لا يرد على الزوجين مع وجود أصحاب فروض نسبيين.",
                            hi = "फ़िक़्ह नोट: रक्त वारिसों की उपस्थिति में जीवनसाथी को रद नहीं मिलता।"
                        )
                    )
                }
            }
        }

        // Check al-Mushtarakah / Himariyyah rare edge case:
        // Husband (1/2) + Mother/GM (1/6) + >=2 Uterine Siblings (1/3) + Full Brother(s) -> remainder == 0!
        if (input.hasHusband && (input.hasMother || effectiveMaternalGrandmother || effectivePaternalGrandmother) &&
            effectiveUterine >= 2 && effectiveFullBrothers > 0 && finalShares.none { it.key == "full_brothers" }
        ) {
            warnings.add(
                HuqooqLocalized4(
                    en = "Al-Mushtarakah (Al-Himariyyah) Classical Difference: Fixed shares (1/2 + 1/6 + 1/3) leave 0 residue for full brothers under Hanafi/Hanbali fiqh; Maliki/Shafi'i fiqh lets full brothers share the 1/3 equally with uterine siblings. Consult a Mufti.",
                    ur = "مسئلۂ مشترکہ (حماریہ): حنفی/حنبلی فقہ میں اخیافی بھائیوں کے بعد حقیقی بھائی محروم رہتے ہیں، جبکہ مالکی/شافعی فقہ میں 1/3 میں شریک ہوتے ہیں۔ مفتی سے رجوع کریں۔",
                    ar = "المسألة المشتركة: استُغرق المال بالفروض؛ وفيها خلاف بين الحنفية (يسقط الأشقاء) والمالكية والشافعية (يشتركون في الثلث).",
                    hi = "मुश्तरका मसला: निश्चित हिस्सों से संपत्ति पूर्ण हो जाने पर सगे भाई के हिस्से पर मज़हबी मतभेद है — मुफ़्ती से सलाह लें।"
                )
            )
        }

        return buildFinalResult(
            input = input,
            netEstate = netEstate,
            specialRule = specialRule,
            rawShares = finalShares,
            excluded = excluded,
            steps = steps,
            warnings = warnings
        )
    }

    private fun buildFinalResult(
        input: InheritanceInput,
        netEstate: Double,
        specialRule: String,
        rawShares: List<RawShare>,
        excluded: List<ExcludedHeirInfo>,
        steps: List<HuqooqLocalized4>,
        warnings: List<HuqooqLocalized4>
    ): InheritanceCalculationResult {
        val shareResults = rawShares.map { raw ->
            val aggFrac = raw.fraction.reduced
            val perFrac = (aggFrac * Rational(1, raw.count.toLong())).reduced
            val aggRatio = aggFrac.toDouble()
            val aggAmt = netEstate * aggRatio
            val perAmt = aggAmt / raw.count
            HeirShareResult(
                heirKey = raw.key,
                label = raw.label,
                count = raw.count,
                shareRole = raw.role,
                aggregateFraction = aggFrac,
                perPersonFraction = perFrac,
                aggregatePercentage = aggRatio * 100.0,
                aggregateAmount = aggAmt,
                perPersonAmount = perAmt,
                quranReference = raw.quranRef,
                explanation = raw.explanation
            )
        }

        val allSteps = steps + HuqooqLocalized4(
            en = "Total distributed fraction verified = ${rawShares.fold(Rational.ZERO) { a, b -> a + b.fraction }.reduced.formattedFraction()} (100.00%).",
            ur = "کل تقسیم شدہ حصوں کا میزان = 100.00% درست تصدیق شدہ ہے۔",
            ar = "تم التحقق من أن مجموع الأنصبة يساوي ١٠٠٪.",
            hi = "कुल वितरित हिस्से = 100.00% सत्यापित।"
        )

        val allWarnings = warnings + HuqooqLocalized4(
            en = "Educational Calculator Disclaimer: For real estate settlements, disputed paternity, missing heirs, or local court succession decrees, always obtain a written Fatwa from a qualified Mufti and comply with applicable succession law.",
            ur = "علمی رہنمائی: عملی جائیداد کی تقسیم، متنازعہ ورثاء یا سرکاری سرٹیفکیٹ کے لیے ہمیشہ مستند مفتی سے تحریری فتویٰ اور ملکی قانونی کارروائی مکمل کروائیں۔",
            ar = "تنبيه تعليمي: لتقسيم التركات الفعلية يُرجى الحصول على فتوى موثقة من دار إفتاء أو عالم مختص.",
            hi = "शैक्षिक सूचना: वास्तविक संपत्ति बंटवारे के लिए हमेशा योग्य मुफ़्ती से लिखित फ़तवा और कानूनी प्रक्रिया पूरी कराएं।"
        )

        val refs = shareResults.map { it.quranReference }.distinct()
        return InheritanceCalculationResult(
            status = CalculatorStatus.SUCCESS,
            totalEstate = input.totalEstate,
            funeralExpenses = input.funeralExpenses,
            debts = input.debts,
            wasiyyatApplied = input.wasiyyat,
            otherDeductions = input.otherDeductions,
            netDistributableEstate = netEstate,
            specialRuleApplied = specialRule,
            shares = shareResults,
            excludedHeirs = excluded,
            stepByStepExplanation = allSteps,
            warningsAndAssumptions = allWarnings,
            quranicReferences = refs
        )
    }

    private fun invalidResult(input: InheritanceInput, msg: HuqooqLocalized4): InheritanceCalculationResult {
        return InheritanceCalculationResult(
            status = CalculatorStatus.INVALID_INPUT,
            totalEstate = input.totalEstate,
            funeralExpenses = input.funeralExpenses,
            debts = input.debts,
            wasiyyatApplied = input.wasiyyat,
            otherDeductions = input.otherDeductions,
            netDistributableEstate = 0.0,
            specialRuleApplied = "VALIDATION_ERROR",
            errorOrReviewMessage = msg
        )
    }

    private fun scholarlyReviewResult(
        input: InheritanceInput,
        netEstate: Double,
        reason: HuqooqLocalized4
    ): InheritanceCalculationResult {
        return InheritanceCalculationResult(
            status = CalculatorStatus.SCHOLARLY_REVIEW_REQUIRED,
            totalEstate = input.totalEstate,
            funeralExpenses = input.funeralExpenses,
            debts = input.debts,
            wasiyyatApplied = input.wasiyyat,
            otherDeductions = input.otherDeductions,
            netDistributableEstate = netEstate,
            specialRuleApplied = "SCHOLARLY_REVIEW_REQUIRED",
            errorOrReviewMessage = reason,
            quranicReferences = listOf("Quran 4:11", "Quran 4:12", "Quran 4:176")
        )
    }

    private fun label(en: String, ur: String, ar: String, hi: String) =
        HuqooqLocalized4(en = en, ur = ur, ar = ar, hi = hi)

    private fun roleFixed() =
        HuqooqLocalized4("Ashab-ul-Furood (Fixed Share)", "مقررہ حصہ (ذوی الفروض)", "فرض مقدر", "निश्चित हिस्सा (असहाब-उल-फ़ुरूज़)")

    private fun roleAsabah() =
        HuqooqLocalized4("Asabah (Residuary)", "عصبہ (باقی ماندہ)", "تعصيب", "असबा (शेष वारिस)")

    private fun roleFixedPlusAsabah() =
        HuqooqLocalized4("Fixed + Asabah (Residuary)", "مقررہ حصہ + عصبہ", "فرض + تعصيب", "निश्चित + असबा")
}
