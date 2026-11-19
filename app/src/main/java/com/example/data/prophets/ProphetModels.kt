package com.example.data.prophets

import com.example.data.model.AppLanguage

enum class ProphetCategory(val id: String) {
    ULUL_AZM("ulul_azm"),
    EARLY_NATIONS("early_nations"),
    HOUSE_OF_IBRAHIM("house_of_ibrahim"),
    BANI_ISRAIL_ERA("bani_israil_era"),
    CONCISE_MENTION("concise_mention");

    fun label(lang: AppLanguage): String = when (this) {
        ULUL_AZM -> when (lang) {
            AppLanguage.URDU -> "اولوالعزم پیغمبر"
            AppLanguage.ARABIC -> "أولو العزم من الرسل"
            AppLanguage.HINDI -> "उलुल-अज़्म पैग़ंबर"
            AppLanguage.ENGLISH -> "Ulul-Azm (Steadfast)"
        }
        EARLY_NATIONS -> when (lang) {
            AppLanguage.URDU -> "قدیم اقوام کے انبیاء"
            AppLanguage.ARABIC -> "أنبياء الأمم الأولى"
            AppLanguage.HINDI -> "प्राचीन जातियों के नबी"
            AppLanguage.ENGLISH -> "Ancient Nations"
        }
        HOUSE_OF_IBRAHIM -> when (lang) {
            AppLanguage.URDU -> "آلِ ابراہیمؑ"
            AppLanguage.ARABIC -> "آل إبراهيم عليه السلام"
            AppLanguage.HINDI -> "आल-ए-इब्राहीम"
            AppLanguage.ENGLISH -> "Family of Ibrahim"
        }
        BANI_ISRAIL_ERA -> when (lang) {
            AppLanguage.URDU -> "بنی اسرائیل و زکریاؑ"
            AppLanguage.ARABIC -> "أنبياء بني إسرائيل"
            AppLanguage.HINDI -> "बनी इस्राईल के नबी"
            AppLanguage.ENGLISH -> "Bani Isra'il Prophets"
        }
        CONCISE_MENTION -> when (lang) {
            AppLanguage.URDU -> "مختصر قرآنی ذکر"
            AppLanguage.ARABIC -> "أنبياء ذُكروا بإيجاز"
            AppLanguage.HINDI -> "संक्षिप्त क़ुरआनी उल्लेख"
            AppLanguage.ENGLISH -> "Concise Quranic Mention"
        }
    }
}

data class ProphetVerseRef(
    val surah: Int,
    val startAyah: Int,
    val endAyah: Int = startAyah,
    val topic: Map<AppLanguage, String>
) {
    val referenceLabel: String
        get() = if (startAyah == endAyah) "$surah:$startAyah" else "$surah:$startAyah-$endAyah"
}

data class ProphetHadithRef(
    val citation: String,
    val url: String,
    val summary: Map<AppLanguage, String>
)

data class ProphetDuaRef(
    val surah: Int,
    val startAyah: Int,
    val endAyah: Int = startAyah,
    val title: Map<AppLanguage, String>
) {
    val referenceLabel: String
        get() = if (startAyah == endAyah) "$surah:$startAyah" else "$surah:$startAyah-$endAyah"
}

data class ProphetQuizQuestion(
    val question: Map<AppLanguage, String>,
    val options: Map<AppLanguage, List<String>>,
    val correctIndex: Int,
    val explanation: Map<AppLanguage, String>
)

data class ProphetStory(
    val id: String,
    val orderNumber: Int,
    val arabicName: String,
    val names: Map<AppLanguage, String>,
    val titles: Map<AppLanguage, String>,
    val category: ProphetCategory,
    val summaryIntro: Map<AppLanguage, String>,
    val detailedIntro: Map<AppLanguage, String>,
    val quranicContext: Map<AppLanguage, String>,
    val mainEvents: Map<AppLanguage, List<String>>,
    val peopleAndPlaces: Map<AppLanguage, List<String>>,
    val verseRefs: List<ProphetVerseRef>,
    val hadithRefs: List<ProphetHadithRef>,
    val lessons: Map<AppLanguage, List<String>>,
    val quranicDuas: List<ProphetDuaRef>,
    val reliabilityNotes: Map<AppLanguage, String>,
    val quizQuestion: ProphetQuizQuestion
) {
    fun localizedName(lang: AppLanguage): String = names[lang] ?: names[AppLanguage.ENGLISH].orEmpty()
    fun localizedTitle(lang: AppLanguage): String = titles[lang] ?: titles[AppLanguage.ENGLISH].orEmpty()
}

internal fun langMap(en: String, ur: String, ar: String, hi: String): Map<AppLanguage, String> = mapOf(
    AppLanguage.ENGLISH to en,
    AppLanguage.URDU to ur,
    AppLanguage.ARABIC to ar,
    AppLanguage.HINDI to hi
)

internal fun listLangMap(
    en: List<String>,
    ur: List<String>,
    ar: List<String>,
    hi: List<String>
): Map<AppLanguage, List<String>> = mapOf(
    AppLanguage.ENGLISH to en,
    AppLanguage.URDU to ur,
    AppLanguage.ARABIC to ar,
    AppLanguage.HINDI to hi
)
