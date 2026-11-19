package com.example.data.salah

import com.example.data.model.AppLanguage

enum class SalahPrayerType(val id: String, val order: Int) {
    FAJR("fajr", 1),
    DHUHR("dhuhr", 2),
    ASR("asr", 3),
    MAGHRIB("maghrib", 4),
    ISHA("isha", 5);

    fun label(lang: AppLanguage): String = when (this) {
        FAJR -> when (lang) {
            AppLanguage.URDU -> "فجر"
            AppLanguage.ARABIC -> "الفجر"
            AppLanguage.HINDI -> "फ़ज्र"
            AppLanguage.ENGLISH -> "Fajr"
        }
        DHUHR -> when (lang) {
            AppLanguage.URDU -> "ظہر"
            AppLanguage.ARABIC -> "الظهر"
            AppLanguage.HINDI -> "ज़ुहर"
            AppLanguage.ENGLISH -> "Dhuhr"
        }
        ASR -> when (lang) {
            AppLanguage.URDU -> "عصر"
            AppLanguage.ARABIC -> "العصر"
            AppLanguage.HINDI -> "अस्र"
            AppLanguage.ENGLISH -> "Asr"
        }
        MAGHRIB -> when (lang) {
            AppLanguage.URDU -> "مغرب"
            AppLanguage.ARABIC -> "المغرب"
            AppLanguage.HINDI -> "मग़रिब"
            AppLanguage.ENGLISH -> "Maghrib"
        }
        ISHA -> when (lang) {
            AppLanguage.URDU -> "عشاء"
            AppLanguage.ARABIC -> "العشاء"
            AppLanguage.HINDI -> "इशा"
            AppLanguage.ENGLISH -> "Isha"
        }
    }
}

enum class MissedReasonCategory(val id: String) {
    SLEEP_OR_FORGETFULNESS("sleep_or_forgetfulness"),
    ILLNESS_TRAVEL_HARDSHIP("illness_or_hardship"),
    DELIBERATE_OR_NEGLIGENCE("deliberate_or_negligence"),
    JURISPRUDENCE_EXEMPT("jurisprudence_exempt"),
    OTHER("other");

    fun label(lang: AppLanguage): String = when (this) {
        SLEEP_OR_FORGETFULNESS -> when (lang) {
            AppLanguage.URDU -> "نیند یا بھول چوک"
            AppLanguage.ARABIC -> "نوم أو نسيان"
            AppLanguage.HINDI -> "नींद या भूल जाना"
            AppLanguage.ENGLISH -> "Sleep or Forgetfulness"
        }
        ILLNESS_TRAVEL_HARDSHIP -> when (lang) {
            AppLanguage.URDU -> "بیماری، سفر یا شدید عذر"
            AppLanguage.ARABIC -> "مرض أو سفر أو مشقة"
            AppLanguage.HINDI -> "बीमारी, यात्रा या कठिनाई"
            AppLanguage.ENGLISH -> "Illness, Travel or Hardship"
        }
        DELIBERATE_OR_NEGLIGENCE -> when (lang) {
            AppLanguage.URDU -> "جان بوجھ کر / غفلت سے ترک"
            AppLanguage.ARABIC -> "ترك عمداً أو تهاوناً"
            AppLanguage.HINDI -> "जानबूझकर / लापरवाही से छूट"
            AppLanguage.ENGLISH -> "Deliberate / Negligence"
        }
        JURISPRUDENCE_EXEMPT -> when (lang) {
            AppLanguage.URDU -> "شرعی عذر / قضا سے معاف حالت"
            AppLanguage.ARABIC -> "حالة معذورة لا قضاء عليها شرعاً"
            AppLanguage.HINDI -> "शरीअत द्वारा क़ज़ा से मुक्त स्थिति"
            AppLanguage.ENGLISH -> "Fiqh-Exempt (No Qada Required)"
        }
        OTHER -> when (lang) {
            AppLanguage.URDU -> "دیگر وجہ"
            AppLanguage.ARABIC -> "سبب آخر"
            AppLanguage.HINDI -> "अन्य कारण"
            AppLanguage.ENGLISH -> "Other Reason"
        }
    }
}

enum class QadaEntryStatus(val id: String) {
    PENDING("pending"),
    COMPLETED("completed"),
    EXEMPT_NOT_REQUIRED("exempt_not_required")
}

enum class FiqhGuidancePerspective(val id: String) {
    COMPARATIVE_OVERVIEW("comparative_overview"),
    MAJORITY_FOUR_SCHOOLS("majority_four_schools"),
    UNINTENTIONAL_ONLY_VIEW("unintentional_only_view");

    fun title(lang: AppLanguage): String = when (this) {
        COMPARATIVE_OVERVIEW -> when (lang) {
            AppLanguage.URDU -> "غیر جانبدار فقہی جائزہ (تمام مستند آراء)"
            AppLanguage.ARABIC -> "عرض فقهي مقارن (لجميع الآراء المعتبرة)"
            AppLanguage.HINDI -> "तुलनात्मक फ़िक़्ह अवलोकन (सभी मत)"
            AppLanguage.ENGLISH -> "Neutral Comparative Scholarly Overview"
        }
        MAJORITY_FOUR_SCHOOLS -> when (lang) {
            AppLanguage.URDU -> "جمہور فقہاء (حنفی، مالکی، شافعی، حنبلی) کی رائے"
            AppLanguage.ARABIC -> "رأي جمهور الفقهاء (المذاهب الأربعة)"
            AppLanguage.HINDI -> "बहुमत फ़िक़्ह मत ( चारों प्रमुख स्कूल)"
            AppLanguage.ENGLISH -> "Majority View (Four Classical Sunni Schools)"
        }
        UNINTENTIONAL_ONLY_VIEW -> when (lang) {
            AppLanguage.URDU -> "صرف بھول/نیند کی قضا (ظاہریہ و ابن تیمیہؒ کی رائے)"
            AppLanguage.ARABIC -> "رأي اقتصار القضاء على الناسي والنائم (ابن حزم وابن تيمية)"
            AppLanguage.HINDI -> "केवल भूल/नींद पर क़ज़ा मत (इब्न हज़्म व इब्न तैमिय्या)"
            AppLanguage.ENGLISH -> "Sleep/Forgetfulness Only (Ibn Hazm & Ibn Taymiyyah View)"
        }
    }

    fun summary(lang: AppLanguage): String = when (this) {
        COMPARATIVE_OVERVIEW -> when (lang) {
            AppLanguage.URDU -> "بھول یا نیند سے چھوٹی نماز کی قضا پر امت کا اتفاق ہے (صحیح بخاری 597، مسلم 684)۔ جان بوجھ کر چھوڑی گئی نماز کے حکم میں فقہاء کے درمیان اختلاف ہے؛ اپنے ذاتی مسئلے کے لیے مستند عالمِ دین سے رجوع فرمائیں۔"
            AppLanguage.ARABIC -> "أجمع العلماء على قضاء الصلاة الفائتة بنوم أو نسيان (البخاري 597، مسلم 684)، واختلفوا في حكم قضاء المتروكة عمداً؛ ويُرجع في الفتوى الخاصة إلى أهل العلم الثقات."
            AppLanguage.HINDI -> "नींद या भूल से छूटी नमाज़ की क़ज़ा पर सर्वसम्मति है (बुख़ारी 597, मुस्लिम 684)। जानबूझकर छोड़ी गई नमाज़ के बारे में विद्वानों में मतभेद हैं; व्यक्तिगत निर्णय हेतु किसी योग्य आलिम से परामर्श करें।"
            AppLanguage.ENGLISH -> "Scholars unanimously affirm Qada for prayers missed due to sleep or forgetfulness (Bukhari 597, Muslim 684). Jurists differ regarding deliberately omitted prayers; please consult a qualified scholar for personal rulings."
        }
        MAJORITY_FOUR_SCHOOLS -> when (lang) {
            AppLanguage.URDU -> "حنفی، مالکی، شافعی اور جمہور حنبلی فقہاء (مثلاً بدائع الصنائع، المجموع للنووی، المغنی لابن قدامہ) کے نزدیک جان بوجھ کر چھوڑی گئی نماز کی بھی سچی توبہ کے ساتھ قضا لازم ہے۔ یہ ایپ صرف آپ کی یادداشت کے اندراجات شمار کرتی ہے۔"
            AppLanguage.ARABIC -> "يرى جمهور الأئمة الأربعة (بدائع الصنائع، المدونة، المجموع للنووي، المغني لابن قدامة) وجوب قضاء الفائتة عمداً مع التوبة الصادقة."
            AppLanguage.HINDI -> "हनफ़ी, मालिकी, शाफ़ेई और हंबली बहुमत विद्वानों (अल-मुग़नी, अल-मजमू, बदाएउस-सनाए) के अनुसार सच्ची तौबा के साथ जानबूझकर छोड़ी गई नमाज़ की भी क़ज़ा आवश्यक है।"
            AppLanguage.ENGLISH -> "The majority of Hanafi, Maliki, Shafi'i, and Hanbali jurists (see Al-Mughni, Al-Majmu', Bada'i al-Sana'i) hold that Qada remains obligatory alongside sincere repentance. Consult a scholar for personal guidance."
        }
        UNINTENTIONAL_ONLY_VIEW -> when (lang) {
            AppLanguage.URDU -> "امام ابن حزمؒ (المحلیٰ) اور شیخ الاسلام ابن تیمیہؒ (مجموع الفتاویٰ) کے نزدیک نیند یا بھول سے رہ جانے والی نماز کی قضا ہے، جبکہ عمداً چھوڑی گئی نماز کے لیے سچی توبہ اور کثرتِ نوافل و استغفار ہے۔ اس انتخاب میں عمداً ترک کردہ اندراجات علیحدہ رکھے جاتے ہیں۔"
            AppLanguage.ARABIC -> "ذهب ابن حزم (المحلى) وابن تيمية (مجموع الفتاوى) إلى أن القضاء يُشرع للنائم والناسي كما في حديث البخاري 597 ومسلم 684، أما المتعمد فعليه التوبة الصادقة والإكثار من النوافل."
            AppLanguage.HINDI -> "इब्न हज़्म (अल-मुहल्ला) और इब्न तैमिय्या (मजमूउल-फ़तावा) के अनुसार क़ज़ा केवल नींद या भूल के लिए है, जबकि जानबूझकर छोड़ने पर सच्ची तौबा और नफ़्ल इबादत अधिक करनी चाहिए। इस विकल्प में ऐसे इंदराज अलग दिखाए जाते हैं।"
            AppLanguage.ENGLISH -> "Ibn Hazm (Al-Muhalla) and Ibn Taymiyyah (Majmu' al-Fatawa) held that Bukhari 597 & Muslim 684 specify Qada for sleep/forgetfulness, whereas deliberate omission requires sincere repentance and extra good deeds. Deliberate logs are kept separate from pending Qada."
        }
    }
}

data class BacklogEstimateConfig(
    val enabled: Boolean = false,
    val startDateKey: String = "",
    val endDateKey: String = "",
    val durationNote: String = "",
    val estimatedByPrayer: Map<SalahPrayerType, Int> = emptyMap(),
    val completedByPrayer: Map<SalahPrayerType, Int> = emptyMap()
) {
    val totalEstimated: Int
        get() = estimatedByPrayer.values.sum()

    val totalCompletedAgainstEstimate: Int
        get() = completedByPrayer.entries.sumOf { (p, done) ->
            done.coerceIn(0, estimatedByPrayer[p] ?: 0)
        }

    val remainingEstimated: Int
        get() = (totalEstimated - totalCompletedAgainstEstimate).coerceAtLeast(0)
}

data class QadaPlannerConfig(
    val isActive: Boolean = false,
    val isPaused: Boolean = false,
    val dailyTarget: Int = 2
)

data class PrayerBreakdownCounts(
    val totalRecorded: Int = 0,
    val pendingQada: Int = 0,
    val completed: Int = 0,
    val exemptCount: Int = 0
)

data class VerifiedSalahReference(
    val id: String,
    val citationTitle: String,
    val url: String,
    val arabicText: String,
    val translations: Map<AppLanguage, String>,
    val scopeNote: Map<AppLanguage, String>
)
