package com.example.localization

import com.example.data.model.AppLanguage
import com.example.data.model.Dua
import com.example.data.model.Hadith
import com.example.data.model.RevelationType
import com.example.data.model.Surah

/**
 * Single centralized content-resolution & honesty helper for the 4 global languages
 * (Urdu, English, Arabic, Hindi) selected exclusively from Settings.
 *
 * Strictly enforces:
 * 1. Arabic sacred texts remain original Arabic regardless of UI language.
 * 2. UI labels and explanations follow active [AppLanguage].
 * 3. Missing verified translations or explanations NEVER silently fall back to English
 *    or another unrequested language; instead they mark availability=false and supply an
 *    honest localized unavailable notice.
 */
data class ResolvedLocalizedText(
    val text: String?,
    val isAvailable: Boolean,
    val unavailableNotice: String? = null
)

object LocalizedContentResolver {

    fun unavailableTranslationNotice(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> "Verified English translation is not available for this entry in the bundled source."
        AppLanguage.URDU -> "اس متن کا مستند اردو ترجمہ دستیاب ماخذ میں موجود نہیں ہے۔"
        AppLanguage.ARABIC -> "الترجمة العربية أو التفسير المختصر الإضافي غير مطلوب هنا؛ النص العربي الأصلي معروض أعلاه."
        AppLanguage.HINDI -> "इस पाठ का प्रामाणिक हिन्दी अनुवाद उपलब्ध स्रोत में मौजूद नहीं है।"
    }

    fun unavailableExplanationNotice(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> "No verified explanation is available in English in the selected source."
        AppLanguage.URDU -> "منتخب ماخذ میں اس کی مستند اردو تشریح دستیاب نہیں ہے۔"
        AppLanguage.ARABIC -> "لا يتوفر شرح موثق إضافي في المصدر المحدد."
        AppLanguage.HINDI -> "चयनित स्रोत में इसकी प्रामाणिक हिन्दी व्याख्या उपलब्ध नहीं है।"
    }

    fun unavailableHadithTranslationNotice(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> "Verified English translation is not included in the offline core for this hadith."
        AppLanguage.URDU -> "اس حدیث کا مستند اردو ترجمہ آف لائن ذخیرے میں شامل نہیں ہے؛ اصل عربی متن اوپر ملاحظہ کریں۔"
        AppLanguage.ARABIC -> "النص العربي النبوي الشريف معروض أعلاه؛ لا حاجة لترجمة إضافية."
        AppLanguage.HINDI -> "इस हदीस का प्रामाणिक हिन्दी अनुवाद इस संस्करण में उपलब्ध नहीं है। मूल अरबी पाठ ऊपर देखें।"
    }

    fun unavailableTafseerEditionNotice(
        language: AppLanguage,
        scholarTitle: String,
        officialUrl: String? = null
    ): String {
        val base = when (language) {
            AppLanguage.ENGLISH -> "$scholarTitle is currently not available for offline/inline English display in this edition."
            AppLanguage.URDU -> "$scholarTitle اس وقت ایپ کے منتخب اردو ماخذ میں براہِ راست دستیاب نہیں ہے۔ انگریزی متن متبادل کے طور پر نہیں دکھایا جا رہا۔"
            AppLanguage.ARABIC -> "تفسير ($scholarTitle) غير متاح حالياً للعرض المضمّن باللغة العربية في هذه النسخة."
            AppLanguage.HINDI -> "$scholarTitle इस समय हिन्दी में सीधे इन-ऐप उपलब्ध नहीं है। भाषा शुद्धता के लिए अंग्रेज़ी पाठ नहीं दिखाया गया है।"
        }
        return if (!officialUrl.isNullOrBlank()) {
            val suffix = when (language) {
                AppLanguage.ENGLISH -> " Verify on the official publisher source: $officialUrl"
                AppLanguage.URDU -> " تصدیق و مطالعہ کے لیے مستند ویب ماخذ کھولیں: $officialUrl"
                AppLanguage.ARABIC -> " يمكنك المراجعة عبر المصدر الرسمي الموثوق: $officialUrl"
                AppLanguage.HINDI -> " प्रामाणिक अध्ययन के लिए आधिकारिक स्रोत देखें: $officialUrl"
            }
            base + suffix
        } else {
            base
        }
    }

    fun selectOptional(
        language: AppLanguage,
        english: String?,
        urdu: String?,
        arabic: String?,
        hindi: String?,
        noticeIfMissing: String = unavailableTranslationNotice(language)
    ): ResolvedLocalizedText {
        val candidate = when (language) {
            AppLanguage.ENGLISH -> english
            AppLanguage.URDU -> urdu
            AppLanguage.ARABIC -> arabic
            AppLanguage.HINDI -> hindi
        }?.trim()

        return if (!candidate.isNullOrEmpty()) {
            ResolvedLocalizedText(text = candidate, isAvailable = true, unavailableNotice = null)
        } else {
            ResolvedLocalizedText(text = null, isAvailable = false, unavailableNotice = noticeIfMissing)
        }
    }

    /**
     * Resolves a Dua's localized title strictly matching [language].
     */
    fun resolveDuaTitle(dua: Dua, language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> dua.titleEn
        AppLanguage.URDU -> dua.titleUr.ifBlank { dua.titleEn }
        AppLanguage.ARABIC -> dua.titleAr.ifBlank { dua.arabic.take(48) }
        AppLanguage.HINDI -> dua.titleHi.ifBlank { dua.titleEn }
    }

    fun resolveSurahName(surah: Surah, language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> surah.nameEnglish
        AppLanguage.URDU -> surah.nameUrdu.ifBlank { surah.nameEnglish }
        AppLanguage.ARABIC -> surah.nameArabic
        AppLanguage.HINDI -> surah.nameHindi.ifBlank { surah.nameEnglish }
    }

    fun resolveRevelationType(revelationType: RevelationType, language: AppLanguage): String =
        if (revelationType == RevelationType.MAKKI) {
            AppLocalization.get("makki", language)
        } else {
            AppLocalization.get("madani", language)
        }

    /**
     * Resolves a single verified Dua translation/explanation strictly for [language].
     * For [AppLanguage.ARABIC], original Arabic Dua is already shown so no translation text is duplicated.
     */
    fun resolveDuaTranslation(dua: Dua, language: AppLanguage): ResolvedLocalizedText = when (language) {
        AppLanguage.ARABIC -> ResolvedLocalizedText(text = null, isAvailable = false, unavailableNotice = null)
        AppLanguage.ENGLISH -> selectOptional(
            language,
            english = dua.translationEn,
            urdu = null,
            arabic = null,
            hindi = null
        )
        AppLanguage.URDU -> selectOptional(
            language,
            english = null,
            urdu = dua.translationUr,
            arabic = null,
            hindi = null
        )
        AppLanguage.HINDI -> selectOptional(
            language,
            english = null,
            urdu = null,
            arabic = null,
            hindi = dua.translationHi
        )
    }

    fun resolveDuaOccasion(dua: Dua, language: AppLanguage): String? = when (language) {
        AppLanguage.ENGLISH -> dua.occasionEn.takeIf { it.isNotBlank() }
        AppLanguage.URDU -> dua.occasionUr.takeIf { it.isNotBlank() }
        AppLanguage.ARABIC -> dua.occasionAr.takeIf { it.isNotBlank() }
        AppLanguage.HINDI -> dua.occasionHi.takeIf { it.isNotBlank() }
    }

    fun resolveDuaMethod(dua: Dua, language: AppLanguage): ResolvedLocalizedText = selectOptional(
        language = language,
        english = dua.methodEn,
        urdu = dua.methodUr,
        arabic = dua.methodAr,
        hindi = dua.methodHi,
        noticeIfMissing = unavailableExplanationNotice(language)
    )

    fun resolveDuaVirtues(dua: Dua, language: AppLanguage): ResolvedLocalizedText = selectOptional(
        language = language,
        english = dua.virtuesBenefitsEn,
        urdu = dua.virtuesBenefitsUr,
        arabic = dua.virtuesBenefitsAr,
        hindi = dua.virtuesBenefitsHi,
        noticeIfMissing = unavailableExplanationNotice(language)
    )

    private fun isPlaceholderTranslation(text: String): Boolean {
        val t = text.trim()
        return t.isEmpty() ||
            t.startsWith("English translation unavailable", ignoreCase = true) ||
            t.startsWith("اس روایت کا اردو ترجمہ دستیاب نہیں")
    }

    /**
     * Resolves a single Hadith translation strictly for [language] — never mixing multiple languages
     * and never substituting English/Urdu when Hindi is missing.
     */
    fun resolveHadithTranslation(hadith: Hadith, language: AppLanguage): ResolvedLocalizedText = when (language) {
        AppLanguage.ARABIC -> ResolvedLocalizedText(text = null, isAvailable = false, unavailableNotice = null)
        AppLanguage.ENGLISH -> if (!isPlaceholderTranslation(hadith.textEnglish)) {
            ResolvedLocalizedText(text = hadith.textEnglish, isAvailable = true)
        } else {
            ResolvedLocalizedText(text = null, isAvailable = false, unavailableNotice = unavailableHadithTranslationNotice(language))
        }
        AppLanguage.URDU -> if (!isPlaceholderTranslation(hadith.textUrdu)) {
            ResolvedLocalizedText(text = hadith.textUrdu, isAvailable = true)
        } else {
            ResolvedLocalizedText(text = null, isAvailable = false, unavailableNotice = unavailableHadithTranslationNotice(language))
        }
        AppLanguage.HINDI -> if (hadith.isHindiAvailable && hadith.textHindi.isNotBlank()) {
            ResolvedLocalizedText(text = hadith.textHindi, isAvailable = true)
        } else {
            ResolvedLocalizedText(
                text = null,
                isAvailable = false,
                unavailableNotice = unavailableHadithTranslationNotice(AppLanguage.HINDI)
            )
        }
    }
}
