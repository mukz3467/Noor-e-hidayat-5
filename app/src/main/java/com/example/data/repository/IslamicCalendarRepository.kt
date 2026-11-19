package com.example.data.repository

import android.content.Context
import android.os.Build
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.floor

enum class HijriCalendarConvention(val id: String) {
    PAKISTAN_RUET("pakistan_ruet"),
    UMM_AL_QURA("umm_al_qura"),
    ASTRONOMICAL_TABULAR("astronomical_tabular"),
    MANUAL_LOCAL_SIGHTING("manual_local_sighting");

    companion object {
        fun fromId(raw: String?): HijriCalendarConvention =
            entries.firstOrNull { it.id == raw } ?: PAKISTAN_RUET
    }
}

data class HijriDate(
    val day: Int,
    val month: Int, // 1-12
    val monthNameEn: String,
    val monthNameUr: String,
    val monthNameAr: String,
    val year: Int,
    val monthNameHi: String = "",
    val isConfirmedByRuetHilal: Boolean = false,
    val isEstimate: Boolean = !isConfirmedByRuetHilal,
    val sourceDescription: String = "Astronomical / Regional Estimate"
) {
    fun monthName(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> monthNameUr
        AppLanguage.ARABIC -> monthNameAr
        AppLanguage.HINDI -> monthNameHi.ifBlank { IslamicCalendarRepository.HIJRI_MONTHS_HI.getOrElse(month - 1) { monthNameEn } }
        AppLanguage.ENGLISH -> monthNameEn
    }

    fun formatted(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> "$day ${monthName(language)} $year ھ"
        AppLanguage.ARABIC -> "$day ${monthName(language)} $year هـ"
        AppLanguage.HINDI -> "$day ${monthName(language)} $year हिजरी"
        AppLanguage.ENGLISH -> "$day ${monthName(language)} $year AH"
    }
}

data class IslamicCalendarEvent(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val hijriDay: Int,
    val hijriMonth: Int, // 1-12
    val hijriMonthNameEn: String,
    val hijriMonthNameUr: String,
    val isMoonSightingDependent: Boolean,
    val descriptionEn: String,
    val descriptionUr: String,
    val descriptionAr: String = "",
    val descriptionHi: String = ""
) {
    fun localizedTitle(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> titleEn
        AppLanguage.URDU -> titleUr.ifBlank { titleEn }
        AppLanguage.ARABIC -> titleAr.ifBlank { titleEn }
        AppLanguage.HINDI -> titleHi.ifBlank { titleEn }
    }

    fun localizedDescription(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> descriptionEn
        AppLanguage.URDU -> descriptionUr.ifBlank { descriptionEn }
        AppLanguage.ARABIC -> descriptionAr.ifBlank { descriptionEn }
        AppLanguage.HINDI -> descriptionHi.ifBlank { descriptionEn }
    }

    fun localizedMonthName(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> hijriMonthNameEn
        AppLanguage.URDU -> hijriMonthNameUr
        AppLanguage.ARABIC -> IslamicCalendarRepository.HIJRI_MONTHS_AR.getOrElse(hijriMonth - 1) { hijriMonthNameEn }
        AppLanguage.HINDI -> IslamicCalendarRepository.HIJRI_MONTHS_HI.getOrElse(hijriMonth - 1) { hijriMonthNameEn }
    }
}

data class RamadanDaySchedule(
    val ramadanDay: Int, // 1-30
    val gregorianDateStr: String,
    val hijriDateStr: String,
    val sehriTime: String,
    val iftarTime: String,
    val dayOfWeek: String
)

object IslamicCalendarRepository {

    private const val PREFS_NAME = "hijri_calendar_prefs"
    private const val KEY_MANUAL_ADJUSTMENT = "hijri_day_adjustment"
    private const val KEY_CALENDAR_SOURCE = "hijri_calendar_source"
    private const val KEY_CONVENTION = "hijri_calendar_convention"

    val HIJRI_MONTHS_EN = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    val HIJRI_MONTHS_UR = listOf(
        "محرم الحرام", "صفر المظفر", "ربیع الاول", "ربیع الثانی",
        "جمادی الاول", "جمادی الثانی", "رجب المرجب", "شعبان المعظم",
        "رمضان المبارک", "شوال المکرم", "ذوالقعدہ", "ذوالحجہ"
    )

    val HIJRI_MONTHS_AR = listOf(
        "محرّم", "صفر", "ربيع الأول", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوّال", "ذو القعدة", "ذو الحجة"
    )

    val HIJRI_MONTHS_HI = listOf(
        "मुहर्रम", "सफ़र", "रबी-उल-अव्वल", "रबी-उल-आख़िर",
        "जुमादा-उल-अव्वल", "जुमादा-उल-आख़िर", "रजब", "शाबान",
        "रमज़ान", "शव्वाल", "ज़ुल-क़ादा", "ज़ुल-हिज्जा"
    )

    val ISLAMIC_EVENTS: List<IslamicCalendarEvent> = listOf(
        IslamicCalendarEvent(
            id = "islamic_new_year",
            titleEn = "Islamic New Year",
            titleUr = "یکم محرم الحرام (ہجری سالِ نو)",
            titleAr = "رأس السنة الهجرية",
            titleHi = "इस्लामी नया साल (1 मुहर्रम)",
            hijriDay = 1,
            hijriMonth = 1,
            hijriMonthNameEn = "Muharram",
            hijriMonthNameUr = "محرم الحرام",
            isMoonSightingDependent = true,
            descriptionEn = "Commencement of the Hijri year marking the blessed migration of Prophet Muhammad ﷺ to Madinah.",
            descriptionUr = "ہجری تقویم کا آغاز جو حضور اکرم ﷺ کی مکہ سے مدینہ منورہ کی مبارک ہجرت کی یاد دلاتا ہے۔",
            descriptionAr = "بداية السنة الهجرية التي تخلّد ذكرى الهجرة النبوية المباركة من مكة المكرمة إلى المدينة المنورة.",
            descriptionHi = "हिजरी वर्ष की शुरुआत, जो पैग़म्बर मुहम्मद ﷺ की मक्का से मदीना की मुबारक हिजरत की याद दिलाती है।"
        ),
        IslamicCalendarEvent(
            id = "ashura",
            titleEn = "Day of Ashura",
            titleUr = "یومِ عاشورہ (دس محرم الحرام)",
            titleAr = "يوم عاشوراء",
            titleHi = "आशूरा (10 मुहर्रम)",
            hijriDay = 10,
            hijriMonth = 1,
            hijriMonthNameEn = "Muharram",
            hijriMonthNameUr = "محرم الحرام",
            isMoonSightingDependent = true,
            descriptionEn = "Fasting on the 10th of Muharram expiates sins of the previous year (Sahih Muslim).",
            descriptionUr = "یومِ عاشورہ کا روزہ پچھلے سال کے گناہوں کا کفارہ بن جاتا ہے (صحیح مسلم)۔",
            descriptionAr = "صيام اليوم العاشر من محرم يكفّر ذنوب السنة الماضية كما ثبت في صحيح مسلم.",
            descriptionHi = "10 मुहर्रम (आशूरा) का रोज़ा पिछले वर्ष के गुनाहों का कफ़्फ़ारा बन जाता है (सहीह मुस्लिम)।"
        ),
        IslamicCalendarEvent(
            id = "mawlid_un_nabi",
            titleEn = "12 Rabi' al-Awwal (Mawlid an-Nabi ﷺ)",
            titleUr = "۱۲ ربیع الاول (ولادتِ باسعادت نبی آخر الزماں ﷺ)",
            titleAr = "المولد النبوي الشريف",
            titleHi = "12 रबी-उल-अव्वल (ईद-मिलाद-उन-नबी ﷺ)",
            hijriDay = 12,
            hijriMonth = 3,
            hijriMonthNameEn = "Rabi' al-Awwal",
            hijriMonthNameUr = "ربیع الاول",
            isMoonSightingDependent = true,
            descriptionEn = "Historical commemoration observed in many regions marking the birth month of Prophet Muhammad ﷺ.",
            descriptionUr = "نبی کریم ﷺ کے یومِ ولادت کی تاریخی یاد (مختلف مکاتبِ فکر میں مختلف آراء کے ساتھ)۔",
            descriptionAr = "ذكرى تاريخية في شهر ربيع الأول لميلاد خاتم الأنبياء محمد ﷺ مع تنوّع الآراء الفقهية.",
            descriptionHi = "पैग़म्बर मुहम्मद ﷺ के विलादत के महीने की ऐतिहासिक स्मृति (विद्वानों में विभिन्न मतों के साथ)।"
        ),
        IslamicCalendarEvent(
            id = "shab_e_miraj",
            titleEn = "27 Rajab (Al-Isra' wal-Mi'raj)",
            titleUr = "۲۷ رجب المرجب (شبِ معراج)",
            titleAr = "ذكرى الإسراء والمعراج",
            titleHi = "27 रजब (शब-ए-मेराज)",
            hijriDay = 27,
            hijriMonth = 7,
            hijriMonthNameEn = "Rajab",
            hijriMonthNameUr = "رجب المرجب",
            isMoonSightingDependent = false,
            descriptionEn = "Commemorating the miraculous Night Journey and Heavenly Ascension where the five daily prayers were prescribed.",
            descriptionUr = "سفرِ اسراء اور معراجِ سماوی کا عظیم معجزہ جس میں امت کو پانچ وقت کی نماز کا تحفہ ملا۔",
            descriptionAr = "ذكرى معجزة الإسراء والمعراج التي فُرضت فيها الصلوات الخمس على أمة الإسلام.",
            descriptionHi = "इसरा और मेराज की मुबारक रात, जिसमें उम्मत को पाँच वक़्त की नमाज़ का तोहफ़ा मिला।"
        ),
        IslamicCalendarEvent(
            id = "shab_e_barat",
            titleEn = "15 Sha'ban (Mid-Sha'ban)",
            titleUr = "۱۵ شعبان المعظم (نصف شعبان)",
            titleAr = "ليلة النصف من شعبان",
            titleHi = "15 शाबान (निस्फ़ शाबान)",
            hijriDay = 15,
            hijriMonth = 8,
            hijriMonthNameEn = "Sha'ban",
            hijriMonthNameUr = "شعبان المعظم",
            isMoonSightingDependent = false,
            descriptionEn = "Mid-Sha'ban night remembered for seeking Allah's forgiveness and reflection (regional & scholarly customs vary).",
            descriptionUr = "نصف شعبان کی رات جس میں استغفار و عبادت کا اہتمام کیا جاتا ہے۔",
            descriptionAr = "ليلة النصف من شعبان، تُذكر فيها المغفرة والإنابة إلى الله مع مراعاة اختلاف العادات الإقليمية.",
            descriptionHi = "शाबान की 15वीं रात (निस्फ़ शाबान), जिसमें अल्लाह से मग़फ़िरत और इबादत की जाती है।"
        ),
        IslamicCalendarEvent(
            id = "ramadan_start",
            titleEn = "1st Ramadan (Beginning of Holy Month)",
            titleUr = "یکم رمضان المبارک (نزولِ قرآن و آغازِ صیام)",
            titleAr = "غرة شهر رمضان المبارك",
            titleHi = "1 रमज़ान (पवित्र माह का आरंभ)",
            hijriDay = 1,
            hijriMonth = 9,
            hijriMonthNameEn = "Ramadan",
            hijriMonthNameUr = "رمضان المبارک",
            isMoonSightingDependent = true,
            descriptionEn = "Start of the sacred month of fasting and Qur'an revelation (depends on verified local crescent sighting).",
            descriptionUr = "برکتوں اور رحمتوں کے مقدس مہینے کا آغاز جو مقامی رویتِ ہلال پر موقوف ہے۔",
            descriptionAr = "بداية شهر الصيام المبارك ونزول القرآن الكريم، وتثبت برؤية الهلال المعتمدة محلياً.",
            descriptionHi = "रोज़ों और क़ुरआन के अवतरण के पवित्र महीने रमज़ान की शुरुआत (स्थानीय चाँद देखने पर आधारित)।"
        ),
        IslamicCalendarEvent(
            id = "laylatul_qadr",
            titleEn = "Laylat al-Qadr (Last 10 Odd Nights of Ramadan)",
            titleUr = "لیلۃ القدر (رمضان کا آخری عشرہ — خصوصاً 27ویں شب)",
            titleAr = "ليلة القدر المباركة",
            titleHi = "शब-ए-क़द्र (रमज़ान की आख़िरी ताक़ रातें)",
            hijriDay = 27,
            hijriMonth = 9,
            hijriMonthNameEn = "Ramadan",
            hijriMonthNameUr = "رمضان المبارک",
            isMoonSightingDependent = false,
            descriptionEn = "The Night of Power, better than a thousand months (Surah Al-Qadr), sought across the odd nights of the last ten days.",
            descriptionUr = "ہزار مہینوں سے افضل رات جسے آخری عشرے کی طاق راتوں میں تلاش کرنے کا حکم ہے۔",
            descriptionAr = "ليلة القدر خير من ألف شهر (سورة القدر)، وتُتحرّى في الليالي الوتر من العشر الأواخر من رمضان.",
            descriptionHi = "लैलतुल-क़द्र, जो हज़ार महीनों से बेहतर है (सूरह अल-क़द्र), जिसे आख़िरी दस दिनों की ताक़ रातों में तलाशा जाता है।"
        ),
        IslamicCalendarEvent(
            id = "eid_al_fitr",
            titleEn = "Eid al-Fitr (1st Shawwal)",
            titleUr = "عید الفطر المبارک (یکم شوال)",
            titleAr = "عيد الفطر المبارك",
            titleHi = "ईद-उल-फ़ित्र (1 शव्वाल)",
            hijriDay = 1,
            hijriMonth = 10,
            hijriMonthNameEn = "Shawwal",
            hijriMonthNameUr = "شوال المکرم",
            isMoonSightingDependent = true,
            descriptionEn = "Festival of breaking the fast celebrated upon verified sighting of the Shawwal crescent.",
            descriptionUr = "رمضان المبارک کے روزوں کی تکمیل پر اللہ تعالیٰ کی طرف سے شکرانے کا عظیم تہوار۔",
            descriptionAr = "عيد الفطر السعيد احتفاءً بإتمام صيام رمضان المبارك عند ثبوت رؤية هلال شوال.",
            descriptionHi = "रमज़ान के रोज़ों की तकमील पर शव्वाल का चाँद देखने के बाद मनाया जाने वाला ईद-उल-फ़ित्र का त्योहार।"
        ),
        IslamicCalendarEvent(
            id = "day_of_arafah",
            titleEn = "Day of Arafah (9 Dhu al-Hijjah)",
            titleUr = "یومِ عرفہ (۹ ذوالحجہ)",
            titleAr = "يوم عرفة المبارك",
            titleHi = "यौम-ए-अरफ़ा (9 ज़िलहिज्जा)",
            hijriDay = 9,
            hijriMonth = 12,
            hijriMonthNameEn = "Dhu al-Hijjah",
            hijriMonthNameUr = "ذوالحجہ",
            isMoonSightingDependent = true,
            descriptionEn = "The pinnacle of Hajj at Mount Arafat; fasting on this day for non-pilgrims expiates two years of sins (Sahih Muslim).",
            descriptionUr = "حج کا سب سے بڑا رکن، غیر حجاج کے لیے اس دن کا روزہ دو سال کے گناہوں کا کفارہ ہے۔",
            descriptionAr = "الركن الأعظم للحج بالوقوف بعرفات؛ وصيامه لغير الحاج يكفّر سنتين (صحيح مسلم).",
            descriptionHi = "हज का सबसे अहम दिन (मैदान-ए-अरफ़ात); ग़ैर-हाजियों के लिए इसका रोज़ा दो साल के गुनाहों का कफ़्फ़ारा है (सहीह मुस्लिम)।"
        ),
        IslamicCalendarEvent(
            id = "eid_al_adha",
            titleEn = "Eid al-Adha (10 Dhu al-Hijjah)",
            titleUr = "عید الاضحیٰ (۱۰ ذوالحجہ - سنتِ ابراہیمی)",
            titleAr = "عيد الأضحى المبارك",
            titleHi = "ईद-उल-अज़हा (10 ज़िलहिज्जा)",
            hijriDay = 10,
            hijriMonth = 12,
            hijriMonthNameEn = "Dhu al-Hijjah",
            hijriMonthNameUr = "ذوالحجہ",
            isMoonSightingDependent = true,
            descriptionEn = "Festival of Sacrifice commemorating the supreme submission of Prophet Ibrahim (AS) and Ismail (AS).",
            descriptionUr = "قربانی کا مقدس تہوار جو حضرت ابراہیمؑ اور حضرت اسماعیلؑ کی لازوال اطاعت کی یاد دلاتا ہے۔",
            descriptionAr = "عيد الأضحى المبارك إحياءً لسنة نبي الله إبراهيم وإسماعيل عليهما السلام في التسليم والطاعة.",
            descriptionHi = "क़ुर्बानी का पवित्र त्योहार जो हज़रत इब्राहीम (अ.स.) और इस्माईल (अ.स.) की आज्ञाकारिता की याद दिलाता है।"
        )
    )

    private val _manualAdjustmentDays = MutableStateFlow(0)
    val manualAdjustmentDays: StateFlow<Int> = _manualAdjustmentDays.asStateFlow()

    private val _usePakistanOfficial = MutableStateFlow(true)
    val usePakistanOfficial: StateFlow<Boolean> = _usePakistanOfficial.asStateFlow()

    private val _calendarConvention = MutableStateFlow(HijriCalendarConvention.PAKISTAN_RUET)
    val calendarConvention: StateFlow<HijriCalendarConvention> = _calendarConvention.asStateFlow()

    @Volatile
    private var isPrefsLoaded = false

    fun resetForTest() {
        isPrefsLoaded = false
        _manualAdjustmentDays.value = 0
        _usePakistanOfficial.value = true
        _calendarConvention.value = HijriCalendarConvention.PAKISTAN_RUET
    }

    fun init(context: Context) {
        if (isPrefsLoaded) return
        isPrefsLoaded = true
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _manualAdjustmentDays.value = prefs.getInt(KEY_MANUAL_ADJUSTMENT, 0).coerceIn(-2, 2)
        _usePakistanOfficial.value = prefs.getBoolean(KEY_CALENDAR_SOURCE, true)
        val savedConv = prefs.getString(KEY_CONVENTION, null)
        _calendarConvention.value = if (savedConv != null) {
            HijriCalendarConvention.fromId(savedConv)
        } else if (_usePakistanOfficial.value) {
            HijriCalendarConvention.PAKISTAN_RUET
        } else {
            HijriCalendarConvention.UMM_AL_QURA
        }
    }

    fun setCalendarConvention(context: Context, convention: HijriCalendarConvention) {
        _calendarConvention.value = convention
        _usePakistanOfficial.value = (convention == HijriCalendarConvention.PAKISTAN_RUET)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_CONVENTION, convention.id)
            .putBoolean(KEY_CALENDAR_SOURCE, convention == HijriCalendarConvention.PAKISTAN_RUET)
            .apply()
    }

    fun setManualAdjustment(context: Context, days: Int) {
        val clamped = days.coerceIn(-2, 2)
        _manualAdjustmentDays.value = clamped
        _calendarConvention.value = HijriCalendarConvention.MANUAL_LOCAL_SIGHTING
        _usePakistanOfficial.value = false
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_MANUAL_ADJUSTMENT, clamped)
            .putString(KEY_CONVENTION, HijriCalendarConvention.MANUAL_LOCAL_SIGHTING.id)
            .putBoolean(KEY_CALENDAR_SOURCE, false)
            .apply()
    }

    fun setUsePakistanOfficial(context: Context, useOfficial: Boolean) {
        setCalendarConvention(
            context,
            if (useOfficial) HijriCalendarConvention.PAKISTAN_RUET else HijriCalendarConvention.UMM_AL_QURA
        )
    }

    /**
     * Converts a Gregorian Calendar date to a HijriDate according to the selected convention.
     * Calculation results are always flagged as estimates (isConfirmedByRuetHilal = false, isEstimate = true)
     * unless verified against live sighting announcements.
     */
    fun getHijriDate(
        calendar: Calendar,
        manualOffsetDays: Int = _manualAdjustmentDays.value,
        convention: HijriCalendarConvention = _calendarConvention.value
    ): HijriDate {
        if (convention == HijriCalendarConvention.UMM_AL_QURA && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val icuTz = android.icu.util.TimeZone.getTimeZone(calendar.timeZone.id)
                val icuCal = android.icu.util.IslamicCalendar(icuTz)
                icuCal.calculationType = android.icu.util.IslamicCalendar.CalculationType.ISLAMIC_UMALQURA
                icuCal.timeInMillis = calendar.timeInMillis
                val hYear = icuCal.get(android.icu.util.Calendar.YEAR)
                val hMonthIdx = icuCal.get(android.icu.util.Calendar.MONTH).coerceIn(0, 11)
                val hDay = icuCal.get(android.icu.util.Calendar.DAY_OF_MONTH).coerceIn(1, 30)
                return HijriDate(
                    day = hDay,
                    month = hMonthIdx + 1,
                    monthNameEn = HIJRI_MONTHS_EN[hMonthIdx],
                    monthNameUr = HIJRI_MONTHS_UR[hMonthIdx],
                    monthNameAr = HIJRI_MONTHS_AR[hMonthIdx],
                    year = hYear,
                    monthNameHi = HIJRI_MONTHS_HI[hMonthIdx],
                    isConfirmedByRuetHilal = false,
                    isEstimate = true,
                    sourceDescription = "Saudi Umm Al-Qura Astronomical Reference (Estimate)"
                )
            } catch (_: Throwable) {
                // Fall back to arithmetic formula below
            }
        }

        val effectiveOffset = when (convention) {
            HijriCalendarConvention.PAKISTAN_RUET -> -1
            HijriCalendarConvention.UMM_AL_QURA -> 0
            HijriCalendarConvention.ASTRONOMICAL_TABULAR -> 0
            HijriCalendarConvention.MANUAL_LOCAL_SIGHTING -> manualOffsetDays
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var m = month
        var y = year
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5

        val adjustedJd = jd + effectiveOffset
        val l = adjustedJd - 1948439.5 + 10632.0
        val n = floor((l - 1.0) / 10631.0)
        val l1 = l - 10631.0 * n + 354.0
        val j = (floor((10985.0 - l1) / 5316.0)) * (floor((50.0 * l1) / 17719.0)) +
            (floor(l1 / 5670.0)) * (floor((43.0 * l1) / 15238.0))
        val l2 = l1 - (floor((30.0 - j) / 15.0)) * (floor((17719.0 * j) / 50.0)) -
            (floor(j / 16.0)) * (floor((15238.0 * j) / 43.0)) + 29.0
        val hijriMonth = (floor((24.0 * l2) / 709.0)).toInt()
        val hijriDay = (l2 - floor((709.0 * hijriMonth) / 24.0)).toInt()
        val hijriYear = (30.0 * n + j - 30.0).toInt()

        val validMonth = ((hijriMonth - 1).coerceIn(0, 11))
        val validDay = hijriDay.coerceIn(1, 30)

        val source = when (convention) {
            HijriCalendarConvention.PAKISTAN_RUET ->
                "South Asian Horizon Estimate (-1d vs tabular); confirm with official Ruet-e-Hilal announcement."
            HijriCalendarConvention.UMM_AL_QURA ->
                "Umm Al-Qura Astronomical Estimate; verify local announcement."
            HijriCalendarConvention.ASTRONOMICAL_TABULAR ->
                "30-Year Astronomical Tabular Estimate (unconfirmed by moon sighting)."
            HijriCalendarConvention.MANUAL_LOCAL_SIGHTING ->
                "Manual Local Moon-Sighting Adjustment (${if (manualOffsetDays >= 0) "+$manualOffsetDays" else "$manualOffsetDays"}d)."
        }

        return HijriDate(
            day = validDay,
            month = validMonth + 1,
            monthNameEn = HIJRI_MONTHS_EN[validMonth],
            monthNameUr = HIJRI_MONTHS_UR[validMonth],
            monthNameAr = HIJRI_MONTHS_AR[validMonth],
            year = hijriYear,
            monthNameHi = HIJRI_MONTHS_HI[validMonth],
            isConfirmedByRuetHilal = false,
            isEstimate = true,
            sourceDescription = source
        )
    }

    fun getFormattedHijriDate(calendar: Calendar, manualOffsetDays: Int = _manualAdjustmentDays.value): String {
        return getHijriDate(calendar, manualOffsetDays, _calendarConvention.value).formatted(AppLanguage.ENGLISH)
    }

    fun getFormattedHijriDateUrdu(calendar: Calendar, manualOffsetDays: Int = _manualAdjustmentDays.value): String {
        return getHijriDate(calendar, manualOffsetDays, _calendarConvention.value).formatted(AppLanguage.URDU)
    }

    /**
     * Scans from `fromCal` to locate the 1st of Ramadan for the active/upcoming Hijri cycle.
     */
    fun findProjectedRamadanStart(
        fromCal: Calendar = Calendar.getInstance(),
        convention: HijriCalendarConvention = _calendarConvention.value,
        manualOffsetDays: Int = _manualAdjustmentDays.value
    ): Calendar {
        val probe = (fromCal.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val todayHijri = getHijriDate(probe, manualOffsetDays, convention)
        if (todayHijri.month == 9) {
            // Rewind to 1 Ramadan of current month
            for (i in 0 until 32) {
                val prev = (probe.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, -1) }
                val prevH = getHijriDate(prev, manualOffsetDays, convention)
                if (prevH.month != 9) return probe
                probe.add(Calendar.DAY_OF_MONTH, -1)
            }
            return probe
        }
        // Scan forward up to 380 days for 1 Ramadan
        for (i in 0..380) {
            val h = getHijriDate(probe, manualOffsetDays, convention)
            if (h.month == 9 && h.day == 1) {
                return probe
            }
            if (h.month == 9 && h.day <= 3) {
                return probe
            }
            probe.add(Calendar.DAY_OF_MONTH, 1)
        }
        return fromCal.clone() as Calendar
    }

    /**
     * Generate 30 days schedule for Ramadan using active prayer calculation settings.
     */
    fun generateRamadanSchedule(
        latitude: Double,
        longitude: Double,
        timeZoneId: String,
        gregorianStartCal: Calendar,
        config: PrayerCalculator.PrayerCalculationConfig = PrayerCalculator.calcConfig.value
    ): List<RamadanDaySchedule> {
        val list = mutableListOf<RamadanDaySchedule>()
        val cal = (gregorianStartCal.clone() as Calendar).apply {
            timeZone = TimeZone.getTimeZone(timeZoneId)
        }

        val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        for (dayNum in 1..30) {
            val schedule = PrayerCalculator.calculatePrayerTimes(
                latitude = latitude,
                longitude = longitude,
                calendar = cal,
                method = config.method,
                asrJuristic = config.asrJuristic,
                adjustments = config.adjustments,
                highLatitudeRule = config.highLatitudeRule,
                timeZoneId = timeZoneId,
                customParams = config.customParams
            )

            val gDate = String.format(
                Locale.US,
                "%02d %s",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) ?: ""
            )

            val dayOfWeek = dayNames[(cal.get(Calendar.DAY_OF_WEEK) - 1).coerceIn(0, 6)]

            list.add(
                RamadanDaySchedule(
                    ramadanDay = dayNum,
                    gregorianDateStr = gDate,
                    hijriDateStr = "$dayNum Ramadan",
                    sehriTime = schedule.fajr,
                    iftarTime = schedule.maghrib,
                    dayOfWeek = dayOfWeek
                )
            )

            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return list
    }
}
