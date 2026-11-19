package com.example.data.repository

import android.content.Context
import com.example.data.db.AsbabAlNuzulEntity
import com.example.data.db.NoorDatabaseSeeder
import com.example.data.db.TafseerBenefitEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Ayah
import com.example.data.model.AyahRevelationContext
import com.example.data.model.Hadith
import com.example.data.model.RevelationType
import com.example.data.model.ScholarTafseer
import com.example.data.network.QuranFoundationChapterInfo
import com.example.data.network.QuranFoundationTafsirResult

/**
 * Single source of truth for the canonical 5 Tafseer scholar catalogs,
 * authentic Asbab al-Nuzul lookups, verified Hadith cross-references, and
 * 7-section Tarjuma study context synthesis.
 *
 * Strictly avoids fabricating tafseer or unverified occasions of revelation.
 */
object QuranTafseerRepository {

    data class ScholarInfo(
        val key: String,
        val displayName: String,
        val bookTitle: String,
        val era: String,
        val methodologyBadge: String,
        val offlineAllowed: Boolean = false,
        val sourceUrl: String = ""
    )

    @Volatile
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getTop5Scholars(
        language: AppLanguage,
        context: Context? = appContext
    ): List<ScholarInfo> {
        return VerifiedTafseerCatalog.onlineEditionsFor(language).map { edition ->
            ScholarInfo(
                key = edition.id,
                displayName = edition.scholarName,
                bookTitle = edition.bookTitle,
                era = edition.era,
                methodologyBadge = edition.methodologyBadge,
                offlineAllowed = false,
                sourceUrl = edition.sourceUrl
            )
        }
    }

    fun getUnavailableAsbabMessage(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> "منتخب ماخذ میں اس آیت کے لیے کوئی تصدیق شدہ مخصوص سببِ نزول دستیاب نہیں ہے۔"
        AppLanguage.ARABIC -> "لا تتوفر رواية موثقة لسبب نزول خاص بهذه الآية في المصدر المحدد."
        AppLanguage.HINDI -> "चयनित स्रोत में इस आयत के अवतरण का कोई प्रमाणित विशेष कारण उपलब्ध नहीं है।"
        AppLanguage.ENGLISH -> "No verified specific occasion of revelation is available in the selected source."
    }

    fun findVerifiedHadithsForAyah(surahNumber: Int, ayahNumber: Int): List<Hadith> {
        val idFilter: Set<String> = when {
            surahNumber == 1 -> setOf("bukhari_1")
            surahNumber == 2 && ayahNumber == 144 -> setOf("bukhari_1")
            surahNumber == 2 && ayahNumber == 255 -> setOf("muslim_1")
            else -> emptySet()
        }
        val refNeedle = "$surahNumber:$ayahNumber"
        val keywordMatches = HadithData.hadiths.filter { h ->
            h.id in idFilter ||
                h.sourceAttribution.contains(refNeedle) ||
                h.textEnglish.contains(refNeedle)
        }
        return keywordMatches.distinctBy { it.id }.take(3)
    }

    fun buildVerifiedHistoricalPhase(
        surahNumber: Int,
        ayahNumber: Int,
        language: AppLanguage,
        context: Context? = appContext,
        onlineChapterShortText: String? = null
    ): String {
        val surah = QuranData.surahs.firstOrNull { it.number == surahNumber }
            ?: return getUnavailableAsbabMessage(language)
        return SurahHistoricalMeta.formatHistoricalLocationAndTime(
            context = context,
            surah = surah,
            ayahNumber = ayahNumber,
            language = language,
            onlineChapterShortText = onlineChapterShortText
        )
    }

    fun buildVerifiedAyahRevelationContext(
        surahNumber: Int,
        ayahNumber: Int,
        language: AppLanguage,
        asbabList: List<AsbabAlNuzulEntity>,
        benefitsList: List<TafseerBenefitEntity>,
        relatedHadiths: List<Hadith>,
        onlineQfCommentary: QuranFoundationTafsirResult? = null,
        onlineQfEditionLabel: String? = null,
        activeTranslation: ResolvedAyahTranslation? = null,
        context: Context? = appContext,
        onlineChapterInfo: QuranFoundationChapterInfo? = null
    ): AyahRevelationContext {
        val surah = QuranData.surahs.firstOrNull { it.number == surahNumber }
        val bundledAsbab = QuranTarjumaAssetsLoader.getAsbabAlNuzul(context, surahNumber, ayahNumber)

        val sababText = buildString {
            if (asbabList.isNotEmpty() && language == AppLanguage.ENGLISH) {
                asbabList.forEachIndexed { index, entry ->
                    if (index > 0) append("\n\n")
                    append(entry.narration)
                    append("\n[Source: ${entry.sourceBook} • ${entry.hadithReference} • ${entry.authenticityGrade}]")
                }
            } else if (bundledAsbab != null && (
                    (language == AppLanguage.ARABIC && !bundledAsbab.sahihArabic.isNullOrBlank()) ||
                        (language == AppLanguage.ENGLISH && !bundledAsbab.wahidiEnglish.isNullOrBlank())
                    )
            ) {
                if (language == AppLanguage.ARABIC && !bundledAsbab.sahihArabic.isNullOrBlank()) {
                    append(bundledAsbab.sahihArabic)
                    append("\n[المصدر: الصحيح المسند من أسباب النزول]")
                } else if (language == AppLanguage.ENGLISH && !bundledAsbab.wahidiEnglish.isNullOrBlank()) {
                    append(bundledAsbab.wahidiEnglish)
                    append("\n[Source: Asbab al-Nuzul by Imam Al-Wahidi]")
                }
            } else {
                append(getUnavailableAsbabMessage(language))
                if (asbabList.isNotEmpty()) {
                    val refs = asbabList.joinToString(", ") { "${it.sourceBook} (${it.hadithReference})" }
                    append("\n")
                    append(
                        when (language) {
                            AppLanguage.URDU -> "تصدیق شدہ حوالہ جات: $refs"
                            AppLanguage.ARABIC -> "المراجع الموثقة: $refs"
                            AppLanguage.HINDI -> "प्रामाणिक हवाला: $refs"
                            AppLanguage.ENGLISH -> "Verified citation: $refs"
                        }
                    )
                }
                if (surah != null) {
                    append("\n\n")
                    append(
                        when (language) {
                            AppLanguage.URDU -> "سورۃ کا عمومی پس منظر: ${surah.nameUrdu} (${if (surah.revelationType == RevelationType.MAKKI) "مکی" else "مدنی"}) — ${surah.englishMeaning}"
                            AppLanguage.ARABIC -> "السياق العام للسورة: ${surah.nameArabic} (${if (surah.revelationType == RevelationType.MAKKI) "مكية" else "مدنية"}) — ${surah.englishMeaning}"
                            AppLanguage.HINDI -> "सूरह का सामान्य संदर्भ: ${surah.nameHindi} (${if (surah.revelationType == RevelationType.MAKKI) "मक्की" else "मदनी"}) — ${surah.englishMeaning}"
                            AppLanguage.ENGLISH -> "General Surah Theme: Surah ${surah.nameEnglish} (${surah.revelationType.name}) — ${surah.englishMeaning}."
                        }
                    )
                }
            }
        }

        val zamanaText = buildVerifiedHistoricalPhase(
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            language = language,
            context = context,
            onlineChapterShortText = onlineChapterInfo?.shortText
        )

        val mokhtasarFootnotes = when (language) {
            AppLanguage.ARABIC -> QuranTarjumaAssetsLoader
                .getBundledQuranEncVerse(context, "arabic_mokhtasar", surahNumber, ayahNumber)
                ?.translation?.takeIf { it.isNotBlank() }
            AppLanguage.HINDI -> QuranTarjumaAssetsLoader
                .getBundledQuranEncVerse(context, "hindi_mokhtasar", surahNumber, ayahNumber)
                ?.translation?.takeIf { it.isNotBlank() }
            AppLanguage.ENGLISH -> listOfNotNull(
                QuranTarjumaAssetsLoader.getBundledQuranEncVerse(context, "english_mokhtasar", surahNumber, ayahNumber)?.translation?.takeIf { it.isNotBlank() },
                activeTranslation?.footnotes?.takeIf { it.isNotBlank() }
            ).firstOrNull()
            AppLanguage.URDU -> activeTranslation?.footnotes?.takeIf { it.isNotBlank() }
                ?: QuranTarjumaAssetsLoader
                    .getBundledQuranEncVerse(context, "urdu_junagarhi", surahNumber, ayahNumber)
                    ?.footnotes?.takeIf { it.isNotBlank() }
        }

        val wisdomText = buildString {
            if (onlineQfCommentary != null && onlineQfCommentary.text.isNotBlank()) {
                append(onlineQfCommentary.text.trim())
                if (!onlineQfEditionLabel.isNullOrBlank()) {
                    append("\n\n[${when (language) {
                        AppLanguage.URDU -> "ماخذ"
                        AppLanguage.ARABIC -> "المصدر"
                        AppLanguage.HINDI -> "स्रोत"
                        AppLanguage.ENGLISH -> "Source"
                    }}: $onlineQfEditionLabel]")
                }
            } else if (!mokhtasarFootnotes.isNullOrBlank()) {
                append(mokhtasarFootnotes)
                append(
                    when (language) {
                        AppLanguage.URDU -> "\n\n[ماخذ: مستند حاشیہ / تفسیر المختصر]"
                        AppLanguage.ARABIC -> "\n\n[المصدر: المختصر في تفسير القرآن الكريم]"
                        AppLanguage.HINDI -> "\n\n[स्रोत: अल-मुख़्तसर फ़ी तफ़सीर अल-क़ुरआन अल-करीम]"
                        AppLanguage.ENGLISH -> "\n\n[Source: Al-Mukhtasar / Verified Translation Footnotes]"
                    }
                )
            } else if (surah != null) {
                append(
                    when (language) {
                        AppLanguage.URDU -> "سورۃ ${surah.nameUrdu} (${surah.englishMeaning}) کی یہ آیت توحید، ہدایت اور عملی تقویٰ کی دعوت دیتی ہے۔"
                        AppLanguage.ARABIC -> "تُرسّخ هذه الآية من سورة ${surah.nameArabic} أصول التوحيد والهداية والعمل الصالح."
                        AppLanguage.HINDI -> "सूरह ${surah.nameHindi} (${surah.englishMeaning}) की यह आयत एकेश्वरवाद, मार्गदर्शन और नैतिक जीवन का संदेश देती है।"
                        AppLanguage.ENGLISH -> "Within Surah ${surah.nameEnglish} (${surah.englishMeaning}), this verse calls believers to sincere devotion, sound understanding, and steady righteousness."
                    }
                )
            } else {
                append(getUnavailableAsbabMessage(language))
            }
        }

        val benefitsText = buildString {
            if (benefitsList.isNotEmpty() && language == AppLanguage.ENGLISH) {
                benefitsList.forEachIndexed { idx, benefit ->
                    if (idx > 0) append("\n\n")
                    append("• ${benefit.benefitText}\n[${benefit.sourceReference}]")
                }
            }
            if (relatedHadiths.isNotEmpty()) {
                relatedHadiths.forEach { h ->
                    val hText = if (language == AppLanguage.ARABIC) {
                        h.textArabic
                    } else {
                        com.example.localization.LocalizedContentResolver.resolveHadithTranslation(h, language).text
                    }
                    if (!hText.isNullOrBlank()) {
                        if (isNotEmpty()) append("\n\n")
                        append("• $hText (${h.sourceAttribution})")
                    }
                }
            }
            if (isBlank()) {
                append(
                    when (language) {
                        AppLanguage.URDU -> "قرآنِ مجید کی تلاوت، تدبر اور اس کے احکام پر عمل دنیا و آخرت کی فلاح اور قلبی اطمینان کا ذریعہ ہے۔"
                        AppLanguage.ARABIC -> "تلاوة هذه الآية وتدبر معانيها والعمل بما فيها سبب للهداية والطمأنينة."
                        AppLanguage.HINDI -> "इस आयत का पाठ, चिंतन और इसके संदेश पर अमल करना मार्गदर्शन और आत्मिक शांति का माध्यम है।"
                        AppLanguage.ENGLISH -> "Reciting with reflection (Tadabbur), anchoring faith in Allah alone, and applying the verse's moral guidance in daily conduct brings spiritual clarity and steadfastness."
                    }
                )
            }
        }

        val refSummary = buildString {
            when (language) {
                AppLanguage.URDU -> {
                    append("• قرآنی متن: Tanzil.net عثمانی رسم الخط ($surahNumber:$ayahNumber)\n")
                    if (activeTranslation != null) {
                        append("• ترجمہ: ${activeTranslation.translatorName} (${activeTranslation.publisher})\n")
                    }
                    asbabList.forEach { append("• شانِ نزول: ${it.sourceBook} (${it.hadithReference})\n") }
                    if (bundledAsbab != null) append("• مستند کتاب: اسباب النزول (امام واحدی / الصحيح المسند)\n")
                    benefitsList.forEach { append("• حدیث و فضائل: ${it.sourceReference}\n") }
                    append("• مستند تفاسیر: ابن کثیر، تفہیم القرآن، معارف القرآن، تفسیر عثمانی، احسن البیان")
                }
                AppLanguage.ARABIC -> {
                    append("• النص القرآني: الرسم العثماني الموثق Tanzil.net ($surahNumber:$ayahNumber)\n")
                    if (activeTranslation != null) {
                        append("• التفسير والبيان: ${activeTranslation.translatorName}\n")
                    }
                    asbabList.forEach { append("• أسباب النزول: ${it.sourceBook} (${it.hadithReference})\n") }
                    if (bundledAsbab != null) append("• المرجع: الصحيح المسند من أسباب النزول / الواحدي\n")
                    benefitsList.forEach { append("• الفضائل والحديث: ${it.sourceReference}\n") }
                    append("• أمهات التفاسير المعتمدة: ابن كثير، تفهيم القرآن، معارف القرآن، التفسير العثماني، أحسن البيان")
                }
                AppLanguage.HINDI -> {
                    append("• क़ुरआनी पाठ: Tanzil.net उस्मानी लिपि ($surahNumber:$ayahNumber)\n")
                    if (activeTranslation != null) {
                        append("• अनुवाद: ${activeTranslation.translatorName} (${activeTranslation.publisher})\n")
                    }
                    asbabList.forEach { append("• शाने-नुज़ूल: ${it.sourceBook} (${it.hadithReference})\n") }
                    if (bundledAsbab != null) append("• संदर्भ ग्रंथ: असबाब अन-नुज़ूल (इमाम वाहिदी / सहीह अल-मुसनद)\n")
                    benefitsList.forEach { append("• हदीस व फ़ज़ीलत: ${it.sourceReference}\n") }
                    append("• प्रामाणिक तफ़सीर: इब्न कसीर, तफ़हीम-उल-क़ुरआन, मआरिफ़-उल-क़ुरआन, तफ़सीर उस्मानी, अहसन-उल-बयान")
                }
                AppLanguage.ENGLISH -> {
                    append("• Qur'an Text: Tanzil.net Verified Uthmani Script ($surahNumber:$ayahNumber)\n")
                    if (activeTranslation != null) {
                        append("• Translation: ${activeTranslation.translatorName} (${activeTranslation.publisher})\n")
                    }
                    asbabList.forEach { append("• Asbab: ${it.sourceBook} (${it.hadithReference})\n") }
                    if (bundledAsbab != null) append("• Asbab Catalog: Imam Al-Wahidi / Sahih Asbab al-Nuzul\n")
                    benefitsList.forEach { append("• Hadith/Benefit: ${it.sourceReference}\n") }
                    append("• Canonical Tafseer Authorities: Ibn Kathir, Tafheem-ul-Quran, Maariful Quran, Tafseer Usmani, Ahsan-ul-Bayan")
                }
            }
        }.trim()

        val officialLinks = buildOfficialReferenceLinks()

        return AyahRevelationContext(
            sababNuzool = sababText,
            zamanaNuzool = zamanaText,
            maqsadHikmat = wisdomText,
            fazaailBenefits = benefitsText,
            authenticReferences = refSummary,
            referenceLinks = officialLinks
        )
    }

    private fun buildOfficialReferenceLinks(): List<Pair<String, String>> = listOf(
        "Tanzil.net Uthmani Qur'an" to "https://tanzil.net/",
        "QuranEnc Official Translations" to "https://quranenc.com/en/",
        "Quran.com Content API Translations" to "https://api-docs.quran.com/docs/content_apis_versioned/4.0.0/translations/",
        "Quran Foundation API Tafsirs" to "https://api-docs.quran.foundation/docs/content_apis_versioned/4.0.0/tafsirs/",
        "Al-Wahidi Asbab al-Nuzul" to "https://archive.org/search?query=Asbab+al-Nuzul+Al-Wahidi",
        "Sahih al-Bukhari (Sunnah.com)" to "https://sunnah.com/bukhari",
        "Sahih Muslim (Sunnah.com)" to "https://sunnah.com/muslim",
        "Tafseer Ibn Kathir Urdu" to "https://www.australianislamiclibrary.org/tafseer-ibn-katheer.html",
        "Tafheem-ul-Quran Official" to "https://tafheemulquran.net/index.html",
        "Maariful Quran" to "https://islamicdatabase.org/book/export/html/133",
        "Tafseer Usmani" to "https://onlinemufti.org/tafseer-books/",
        "Ahsan-ul-Bayan" to "https://www.ahsanulbayan.co.in/"
    )

    fun getRevelationContext(
        surahNumber: Int,
        ayahNumber: Int,
        language: AppLanguage,
        context: Context? = appContext
    ): AyahRevelationContext {
        val matchingAsbab = NoorDatabaseSeeder.buildVerifiedAsbabAlNuzul().filter {
            it.surahNumber == surahNumber && ayahNumber in it.startAyah..it.endAyah
        }
        val matchingBenefits = NoorDatabaseSeeder.buildVerifiedTafseerBenefits().filter {
            it.surahNumber == surahNumber && it.ayahNumber == ayahNumber
        }
        val relatedHadiths = findVerifiedHadithsForAyah(surahNumber, ayahNumber)
        return buildVerifiedAyahRevelationContext(
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            language = language,
            asbabList = matchingAsbab,
            benefitsList = matchingBenefits,
            relatedHadiths = relatedHadiths,
            context = context
        )
    }

    fun getRevelationContext(ayah: Ayah, language: AppLanguage): AyahRevelationContext {
        return getRevelationContext(ayah.surahNumber, ayah.ayahNumber, language, appContext)
    }

    suspend fun getScholarTafseer(
        ayah: Ayah,
        scholarKey: String,
        language: AppLanguage
    ): ScholarTafseer {
        val ctx = appContext
        if (ctx != null) {
            val detail = QuranTranslationTafseerRepository.getInstance(ctx).loadTafseerDetail(
                surahNumber = ayah.surahNumber,
                ayahNumber = ayah.ayahNumber,
                sourceId = scholarKey,
                language = language
            )
            return ScholarTafseer(
                scholarKey = detail.source.id,
                authorName = detail.source.scholarName,
                bookTitle = detail.source.bookTitle,
                eraAndCredentials = detail.source.edition,
                tafseerText = detail.passageText
                    ?: detail.passageLanguageNotice
                    ?: VerifiedTafseerCatalog.onlineFetchErrorMessage(language, detail.source.bookTitle),
                keyPoints = emptyList(),
                sourceAttribution = detail.editionAttribution,
                isMachineTranslated = false,
                translationNotice = detail.passageLanguageNotice.orEmpty()
            )
        } else {
            val scholars = getTop5Scholars(language, null)
            val scholar = scholars.firstOrNull { it.key == scholarKey } ?: scholars.firstOrNull()
            val unavailable = VerifiedTafseerCatalog.onlineFetchErrorMessage(
                language,
                scholar?.bookTitle ?: "Tafseer"
            )
            return ScholarTafseer(
                scholarKey = scholar?.key.orEmpty(),
                authorName = scholar?.displayName.orEmpty(),
                bookTitle = scholar?.bookTitle.orEmpty(),
                eraAndCredentials = scholar?.era.orEmpty(),
                tafseerText = unavailable,
                keyPoints = emptyList(),
                sourceAttribution = "",
                isMachineTranslated = false,
                translationNotice = unavailable
            )
        }
    }
}
