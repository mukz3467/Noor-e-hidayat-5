package com.example.data.seerah

import com.example.data.model.AppLanguage

enum class SeerahEra(
    val id: String,
    val orderNumber: Int
) {
    BEFORE_PROPHETHOOD("before_prophethood", 1),
    BEGINNING_OF_REVELATION("beginning_of_revelation", 2),
    EARLY_MAKKAH("early_makkah", 3),
    IMPORTANT_MAKKAH_EVENTS("important_makkah_events", 4),
    HIJRAH_TO_MADINAH("hijrah_to_madinah", 5),
    MADINAH_PERIOD("madinah_period", 6),
    BATTLES_EXPEDITIONS("battles_expeditions", 7),
    TREATIES_EVENTS("treaties_events", 8),
    CHARACTER_PERSONAL_LIFE("character_personal_life", 9);

    fun title(lang: AppLanguage): String = when (this) {
        BEFORE_PROPHETHOOD -> when (lang) {
            AppLanguage.URDU -> "نبوت سے قبل"
            AppLanguage.ARABIC -> "ما قبل النبوة"
            AppLanguage.HINDI -> "नुबुव्वत से पहले"
            AppLanguage.ENGLISH -> "Before Prophethood"
        }
        BEGINNING_OF_REVELATION -> when (lang) {
            AppLanguage.URDU -> "وحی کا آغاز"
            AppLanguage.ARABIC -> "بدء الوحي"
            AppLanguage.HINDI -> "वह्य की शुरुआत"
            AppLanguage.ENGLISH -> "Beginning of Revelation"
        }
        EARLY_MAKKAH -> when (lang) {
            AppLanguage.URDU -> "مکہ میں ابتدائی دعوت"
            AppLanguage.ARABIC -> "بداية الدعوة في مكة"
            AppLanguage.HINDI -> "मक्का में प्रारंभिक दावत"
            AppLanguage.ENGLISH -> "Early Islamic Mission in Makkah"
        }
        IMPORTANT_MAKKAH_EVENTS -> when (lang) {
            AppLanguage.URDU -> "مکہ کے اہم واقعات"
            AppLanguage.ARABIC -> "أبرز أحداث العهد المكي"
            AppLanguage.HINDI -> "मक्का की प्रमुख घटनाएँ"
            AppLanguage.ENGLISH -> "Important Makkah Events"
        }
        HIJRAH_TO_MADINAH -> when (lang) {
            AppLanguage.URDU -> "ہجرتِ مدینہ"
            AppLanguage.ARABIC -> "الهجرة إلى المدينة"
            AppLanguage.HINDI -> "मदीना की ओर हिजरत"
            AppLanguage.ENGLISH -> "Hijrah to Madinah"
        }
        MADINAH_PERIOD -> when (lang) {
            AppLanguage.URDU -> "مدنی دور"
            AppLanguage.ARABIC -> "العهد المدني"
            AppLanguage.HINDI -> "मदीना का दौर"
            AppLanguage.ENGLISH -> "Madinah Period"
        }
        BATTLES_EXPEDITIONS -> when (lang) {
            AppLanguage.URDU -> "غزوات و سرایا"
            AppLanguage.ARABIC -> "الغزوات والسرايا الكبرى"
            AppLanguage.HINDI -> "प्रमुख ग़ज़वात और अभियान"
            AppLanguage.ENGLISH -> "Major Battles & Expeditions"
        }
        TREATIES_EVENTS -> when (lang) {
            AppLanguage.URDU -> "اہم معاہدے اور واقعات"
            AppLanguage.ARABIC -> "المواثيق والأحداث الكبرى"
            AppLanguage.HINDI -> "महत्वपूर्ण संधियाँ और घटनाएँ"
            AppLanguage.ENGLISH -> "Important Treaties & Events"
        }
        CHARACTER_PERSONAL_LIFE -> when (lang) {
            AppLanguage.URDU -> "اخلاقِ نبوی اور ذاتی زندگی ﷺ"
            AppLanguage.ARABIC -> "الشمائل المحمدية والحياة الشخصية ﷺ"
            AppLanguage.HINDI -> "नबी ﷺ का चरित्र और व्यक्तिगत जीवन"
            AppLanguage.ENGLISH -> "Character & Personal Life"
        }
    }

    fun periodLabel(lang: AppLanguage): String = when (this) {
        BEFORE_PROPHETHOOD -> "~570 – 610 CE"
        BEGINNING_OF_REVELATION -> "~610 CE"
        EARLY_MAKKAH -> "~610 – 615 CE"
        IMPORTANT_MAKKAH_EVENTS -> "~615 – 622 CE"
        HIJRAH_TO_MADINAH -> "622 CE / 1 AH"
        MADINAH_PERIOD -> "1 – 11 AH (622 – 632 CE)"
        BATTLES_EXPEDITIONS -> "2 – 9 AH (624 – 630 CE)"
        TREATIES_EVENTS -> "1 – 10 AH (622 – 632 CE)"
        CHARACTER_PERSONAL_LIFE -> when (lang) {
            AppLanguage.URDU -> "سیرتِ طیبہ کے اخلاقی پہلو"
            AppLanguage.ARABIC -> "الشمائل النبوية الشاملة"
            AppLanguage.HINDI -> "सीरत के नैतिक पहलू"
            AppLanguage.ENGLISH -> "Timeless Prophetic Conduct"
        }
    }
}

enum class SourceCredibilityTier(val id: String) {
    TIER_1_QURAN_HADITH("tier_1_quran_hadith"),
    TIER_2_CLASSICAL_SEERAH("tier_2_classical_seerah"),
    TIER_3_SCHOLARLY_BIOGRAPHY("tier_3_scholarly_biography");

    fun label(lang: AppLanguage): String = when (this) {
        TIER_1_QURAN_HADITH -> when (lang) {
            AppLanguage.URDU -> "درجہ اول: قرآن و صحیح حدیث"
            AppLanguage.ARABIC -> "المستوى ١: القرآن والصحيحان"
            AppLanguage.HINDI -> "स्तर 1: क़ुरआन और सहीह हदीस"
            AppLanguage.ENGLISH -> "Tier 1: Quran & Sahih Hadith"
        }
        TIER_2_CLASSICAL_SEERAH -> when (lang) {
            AppLanguage.URDU -> "درجہ دوم: کلاسیکی کتبِ سیرت و تاریخ"
            AppLanguage.ARABIC -> "المستوى ٢: كتب السيرة والتاريخ الكلاسيكية"
            AppLanguage.HINDI -> "स्तर 2: प्राचीन सीरत व इतिहास ग्रंथ"
            AppLanguage.ENGLISH -> "Tier 2: Classical Seerah Chronicles"
        }
        TIER_3_SCHOLARLY_BIOGRAPHY -> when (lang) {
            AppLanguage.URDU -> "درجہ سوم: مستند تحقیقی سیرت"
            AppLanguage.ARABIC -> "المستوى ٣: السيرة العلمية المحققة"
            AppLanguage.HINDI -> "स्तर 3: प्रामाणिक शोधपरक जीवनी"
            AppLanguage.ENGLISH -> "Tier 3: Scholarly Seerah Synthesis"
        }
    }

    fun shortBadge(lang: AppLanguage): String = when (this) {
        TIER_1_QURAN_HADITH -> when (lang) {
            AppLanguage.URDU -> "صحیح بنیادی ماخذ"
            AppLanguage.ARABIC -> "مصدر أصيل متفق عليه"
            AppLanguage.HINDI -> "मूल प्रामाणिक स्रोत"
            AppLanguage.ENGLISH -> "Primary Sahih / Quran"
        }
        TIER_2_CLASSICAL_SEERAH -> when (lang) {
            AppLanguage.URDU -> "تاریخی روایت (صحیح حدیث نہیں)"
            AppLanguage.ARABIC -> "رواية تاريخية (ليست بحدّ الصحيح)"
            AppLanguage.HINDI -> "ऐतिहासिक विवरण (सहीह हदीस नहीं)"
            AppLanguage.ENGLISH -> "Classical History (Not Sahih Hadith)"
        }
        TIER_3_SCHOLARLY_BIOGRAPHY -> when (lang) {
            AppLanguage.URDU -> "تحقیقی ماخذ"
            AppLanguage.ARABIC -> "دراسة تاريخية معاصرة"
            AppLanguage.HINDI -> "शोध संकलन"
            AppLanguage.ENGLISH -> "Scholarly Biography"
        }
    }
}

data class SeerahCitation(
    val tier: SourceCredibilityTier,
    val sourceName: String,
    val referenceCode: String,
    val arabicQuote: String? = null,
    val quoteTranslation: Map<AppLanguage, String>,
    val reliabilityNote: Map<AppLanguage, String>
)

data class SeerahArticle(
    val id: String,
    val era: SeerahEra,
    val orderIndex: Int,
    val title: Map<AppLanguage, String>,
    val approxChronology: Map<AppLanguage, String>,
    val overview: Map<AppLanguage, String>,
    val contextPeoplePlaces: Map<AppLanguage, String>,
    val citations: List<SeerahCitation>,
    val historicalDifferences: Map<AppLanguage, String>,
    val lessonsToday: Map<AppLanguage, String>,
    val nextMilestoneNote: Map<AppLanguage, String>,
    val referenceLinks: List<Pair<String, String>>,
    val keywords: List<String> = emptyList()
) {
    fun textFor(map: Map<AppLanguage, String>, lang: AppLanguage): String =
        map[lang] ?: map[AppLanguage.ENGLISH] ?: ""
}

data class SeerahProgressState(
    val readArticleIds: Set<String> = emptySet(),
    val bookmarkedArticleIds: Set<String> = emptySet(),
    val lastReadArticleId: String? = null
)

internal fun localized4(
    en: String,
    ur: String,
    ar: String,
    hi: String
): Map<AppLanguage, String> = mapOf(
    AppLanguage.ENGLISH to en,
    AppLanguage.URDU to ur,
    AppLanguage.ARABIC to ar,
    AppLanguage.HINDI to hi
)
