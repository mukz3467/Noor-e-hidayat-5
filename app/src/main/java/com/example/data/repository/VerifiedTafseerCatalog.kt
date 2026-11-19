package com.example.data.repository

import com.example.data.db.TafseerSourceEntity
import com.example.data.model.AppLanguage

/**
 * Verified online scholarly Tafseer catalog across Urdu, English, Hindi, and Arabic.
 *
 * - Full multi-volume Tafseer corpora are fetched live online from Quran.com / Quran Foundation API v4
 *   and `spa5k/tafsir_api` (sourced from QUL / Tarteel and Al-Tafsir.com).
 * - Full Tafseer databases are never bundled offline in the APK, and Quran translations are never
 *   substituted under a Tafseer heading.
 */
object VerifiedTafseerCatalog {

    data class OnlineTafseerEdition(
        val id: String,
        val language: AppLanguage,
        val displayOrder: Int,
        val scholarName: String,
        val bookTitle: String,
        val era: String,
        val methodologyBadge: String,
        val publisher: String,
        val editionTitle: String,
        val attribution: String,
        val sourceUrl: String,
        val quranFoundationId: String? = null,
        val spa5kSlug: String? = null,
        val spa5kFallbackSlug: String? = null
    )

    val onlineEditions: List<OnlineTafseerEdition> = listOf(
        // ==================== URDU (5 Detailed Online Scholars) ====================
        OnlineTafseerEdition(
            id = "ur_ibn_kathir",
            language = AppLanguage.URDU,
            displayOrder = 1,
            scholarName = "حافظ عماد الدین ابنِ کثیر (ت 774ھ)",
            bookTitle = "تفسیر ابنِ کثیر (اردو)",
            era = "آٹھویں صدی ہجری • تفصیلی تفسیر بالمأثور",
            methodologyBadge = "مفصل تفسیر مع احادیث",
            publisher = "Quran.com / QUL (Tafsir ID: 160)",
            editionTitle = "تفسیر القرآن العظیم — اردو (مکمل 114 سورتیں آن لائن)",
            attribution = "حافظ عماد الدین ابنِ کثیرؒ • ماخذ: Quran.com API v4 / QUL (ur-tafseer-ibn-e-kaseer)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "160",
            spa5kSlug = "ur-tafseer-ibn-e-kaseer",
            spa5kFallbackSlug = "tafseer-ibn-e-kaseer-urdu"
        ),
        OnlineTafseerEdition(
            id = "ur_bayan_ul_quran",
            language = AppLanguage.URDU,
            displayOrder = 2,
            scholarName = "ڈاکٹر اسرار احمدؒ",
            bookTitle = "تفسیر بیان القرآن",
            era = "عصری تحقیقی و تدبری تفسیر",
            methodologyBadge = "تفصیلی تدبر و ربطِ آیات",
            publisher = "انجمن خدام القرآن / Quran.com (ID: 159)",
            editionTitle = "بیان القرآن (ڈاکٹر اسرار احمد) — اردو",
            attribution = "ڈاکٹر اسرار احمدؒ • ماخذ: Quran.com API v4 / QUL (ur-tafsir-bayan-ul-quran)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "159",
            spa5kSlug = "ur-tafsir-bayan-ul-quran",
            spa5kFallbackSlug = "tafsir-bayan-ul-quran"
        ),
        OnlineTafseerEdition(
            id = "ur_fi_zilal",
            language = AppLanguage.URDU,
            displayOrder = 3,
            scholarName = "سید قطب شہیدؒ",
            bookTitle = "فی ظلال القرآن (اردو)",
            era = "عصری ادبی و تحریکی تفسیر",
            methodologyBadge = "تفصیلی علمی و فکری تفسیر",
            publisher = "Quran.com / QUL (ID: 157)",
            editionTitle = "فی ظلال القرآن — اردو",
            attribution = "سید قطبؒ • ماخذ: Quran.com API v4 / QUL (tafsir-fe-zalul-quran-syed-qatab)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "157",
            spa5kSlug = "tafsir-fe-zalul-quran-syed-qatab",
            spa5kFallbackSlug = "ur-tafsir-fe-zalul-quran-syed-qatab"
        ),
        OnlineTafseerEdition(
            id = "ur_saadi",
            language = AppLanguage.URDU,
            displayOrder = 4,
            scholarName = "علامہ عبد الرحمن بن ناصر السعدیؒ",
            bookTitle = "تفسیر السعدی (تیسیر الکریم الرحمن)",
            era = "چودہویں صدی ہجری",
            methodologyBadge = "واضح و جامع علمی تفسیر",
            publisher = "QUL / spa5k tafsir_api",
            editionTitle = "تیسیر الکریم الرحمن (تفسیر السعدی) — اردو",
            attribution = "شیخ عبد الرحمن بن ناصر السعدیؒ • ماخذ: QUL / spa5k tafsir_api (ur-tafsir-as-saadi-urdu)",
            sourceUrl = "https://qul.tarteel.ai/",
            quranFoundationId = null,
            spa5kSlug = "ur-tafsir-as-saadi-urdu",
            spa5kFallbackSlug = "ur-tafsir-as-saadi-urdu"
        ),
        OnlineTafseerEdition(
            id = "ur_tazkirul_quran",
            language = AppLanguage.URDU,
            displayOrder = 5,
            scholarName = "مولانا وحید الدین خانؒ",
            bookTitle = "تذکیر القرآن (اردو)",
            era = "معاصر دعوتی تفسیر",
            methodologyBadge = "فکری و عملی تفسیر",
            publisher = "Quran.com / QUL (ID: 818)",
            editionTitle = "تذکیر القرآن — اردو",
            attribution = "مولانا وحید الدین خانؒ • ماخذ: Quran.com API v4 / QUL (tazkiru-quran-ur)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "818",
            spa5kSlug = "ur-tazkirul-quran",
            spa5kFallbackSlug = "tazkiru-quran-ur"
        ),

        // ==================== ENGLISH (5 Scholarly Online Editions) ====================
        OnlineTafseerEdition(
            id = "en_ibn_kathir",
            language = AppLanguage.ENGLISH,
            displayOrder = 6,
            scholarName = "Hafiz Imad al-Din Ibn Kathir (d. 774 AH)",
            bookTitle = "Tafsir Ibn Kathir (10 Vols.)",
            era = "8th Century AH (Abridged Eng.)",
            methodologyBadge = "Detailed Tafsir bil-Ma'thur",
            publisher = "Darussalam / Quran.com API v4 (ID: 169)",
            editionTitle = "Tafsir Ibn Kathir (Abridged English, Safiur-Rahman al-Mubarakpuri)",
            attribution = "Hafiz Ibn Kathir • Source: Quran.com API v4 / QUL (en-tafisr-ibn-kathir)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "169",
            spa5kSlug = "en-tafisr-ibn-kathir"
        ),
        OnlineTafseerEdition(
            id = "en_maariful_quran",
            language = AppLanguage.ENGLISH,
            displayOrder = 7,
            scholarName = "Mufti Muhammad Shafi Usmani (RA)",
            bookTitle = "Ma'arif al-Qur'an (8 Vols.)",
            era = "20th Century CE",
            methodologyBadge = "Comprehensive Fiqh & Hadith Commentary",
            publisher = "Quran.com API v4 / QUL (ID: 168)",
            editionTitle = "Ma'arif al-Qur'an (English Complete 8-Volume Exegesis)",
            attribution = "Mufti Muhammad Shafi Usmani • Source: Quran.com API v4 / QUL (en-tafsir-maarif-ul-quran)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "168",
            spa5kSlug = "en-tafsir-maarif-ul-quran"
        ),
        OnlineTafseerEdition(
            id = "en_jalalayn",
            language = AppLanguage.ENGLISH,
            displayOrder = 8,
            scholarName = "Imam al-Mahalli & Imam al-Suyuti",
            bookTitle = "Tafsir al-Jalalayn",
            era = "9th–10th Century AH (tr. Feras Hamza)",
            methodologyBadge = "Classical Analytical Gloss",
            publisher = "Al-Tafsir.com / Royal Aal al-Bayt Institute (via spa5k)",
            editionTitle = "Tafsir al-Jalalayn — English Translation",
            attribution = "Al-Mahalli & Al-Suyuti (tr. Feras Hamza) • Source: Al-Tafsir.com / spa5k tafsir_api",
            sourceUrl = "https://www.altafsir.com/",
            quranFoundationId = null,
            spa5kSlug = "en-al-jalalayn",
            spa5kFallbackSlug = "tafsir-al-jalalayn"
        ),
        OnlineTafseerEdition(
            id = "en_ibn_abbas",
            language = AppLanguage.ENGLISH,
            displayOrder = 9,
            scholarName = "Attributed to Ibn Abbas / Al-Firuzabadi",
            bookTitle = "Tanwir al-Miqbas min Tafsir Ibn Abbas",
            era = "Classical Commentary (tr. Mokrane Guezzou)",
            methodologyBadge = "Classical Narration Gloss",
            publisher = "Al-Tafsir.com / Royal Aal al-Bayt Institute (via spa5k)",
            editionTitle = "Tanwir al-Miqbas min Tafsir Ibn 'Abbas (English)",
            attribution = "Al-Firuzabadi / Ibn Abbas • Source: Al-Tafsir.com / spa5k tafsir_api",
            sourceUrl = "https://www.altafsir.com/",
            quranFoundationId = null,
            spa5kSlug = "en-tafsir-ibn-abbas"
        ),
        OnlineTafseerEdition(
            id = "en_tazkirul_quran",
            language = AppLanguage.ENGLISH,
            displayOrder = 10,
            scholarName = "Maulana Wahiduddin Khan",
            bookTitle = "Tazkirul Quran (English)",
            era = "Contemporary Reflective Commentary",
            methodologyBadge = "Reflective & Thematic Exegesis",
            publisher = "Quran.com API v4 / QUL (ID: 817)",
            editionTitle = "Tazkirul Quran — English",
            attribution = "Maulana Wahiduddin Khan • Source: Quran.com API v4 / QUL (tazkirul-quran-en)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "817",
            spa5kSlug = "en-tazkirul-quran",
            spa5kFallbackSlug = "tazkirul-quran-en"
        ),

        // ==================== HINDI (Verified Published Online Edition) ====================
        OnlineTafseerEdition(
            id = "hi_al_mukhtasar",
            language = AppLanguage.HINDI,
            displayOrder = 11,
            scholarName = "तफ़सीर सेंटर फॉर क़ुरआनिक स्टडीज़",
            bookTitle = "अल-मुख़्तसर फ़ी तफ़सीर अल-क़ुरआन (हिन्दी)",
            era = "समकालीन प्रामाणिक तफ़सीर",
            methodologyBadge = "संक्षिप्त प्रामाणिक तफ़सीर (ऑनलाइन)",
            publisher = "QUL / spa5k tafsir_api (hindi-mokhtasar)",
            editionTitle = "अल-मुख़्तसर फ़ी तफ़सीर अल-क़ुरआन — हिन्दी ऑनलाइन संस्करण",
            attribution = "तफ़सीर सेंटर फॉर क़ुरआनिक स्टडीज़ • स्रोत: QUL / spa5k tafsir_api (hindi-mokhtasar)",
            sourceUrl = "https://qul.tarteel.ai/",
            quranFoundationId = null,
            spa5kSlug = "hindi-mokhtasar"
        ),

        // ==================== ARABIC (7 Classical & Scholarly Online Editions) ====================
        OnlineTafseerEdition(
            id = "ar_ibn_kathir",
            language = AppLanguage.ARABIC,
            displayOrder = 12,
            scholarName = "الحافظ عماد الدين ابن كثير (ت ٧٧٤ هـ)",
            bookTitle = "تفسير القرآن العظيم (ابن كثير)",
            era = "القرن الثامن الهجري",
            methodologyBadge = "تفسير بالمأثور موسوعي",
            publisher = "Quran.com API v4 / QUL (ID: 14)",
            editionTitle = "تفسير القرآن العظيم للحافظ ابن كثير",
            attribution = "الحافظ ابن كثير الدمشقي • المصدر: Quran.com API v4 / QUL (ar-tafsir-ibn-kathir)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "14",
            spa5kSlug = "ar-tafsir-ibn-kathir"
        ),
        OnlineTafseerEdition(
            id = "ar_tabari",
            language = AppLanguage.ARABIC,
            displayOrder = 13,
            scholarName = "الإمام ابن جرير الطبري (ت ٣١٠ هـ)",
            bookTitle = "جامع البيان (تفسير الطبري)",
            era = "القرن الثالث الهجري",
            methodologyBadge = "أمّ التفاسير بالمأثور واللغة",
            publisher = "Quran.com API v4 / QUL (ID: 15)",
            editionTitle = "جامع البيان عن تأويل آي القرآن — الإمام الطبري",
            attribution = "الإمام محمد بن جرير الطبري • المصدر: Quran.com API v4 / QUL (ar-tafsir-al-tabari)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "15",
            spa5kSlug = "ar-tafsir-al-tabari"
        ),
        OnlineTafseerEdition(
            id = "ar_qurtubi",
            language = AppLanguage.ARABIC,
            displayOrder = 14,
            scholarName = "الإمام أبو عبد الله القرطبي (ت ٦٧١ هـ)",
            bookTitle = "الجامع لأحكام القرآن (القرطبي)",
            era = "القرن السابع الهجري",
            methodologyBadge = "تفسير فقهي ولغوي موسوعي",
            publisher = "Quran.com API v4 / QUL (ID: 90)",
            editionTitle = "الجامع لأحكام القرآن للإمام القرطبي",
            attribution = "الإمام شمس الدين القرطبي • المصدر: Quran.com API v4 / QUL (ar-tafseer-al-qurtubi)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "90",
            spa5kSlug = "ar-tafseer-al-qurtubi"
        ),
        OnlineTafseerEdition(
            id = "ar_baghawi",
            language = AppLanguage.ARABIC,
            displayOrder = 15,
            scholarName = "الإمام محيي السنة البغوي (ت ٥١٦ هـ)",
            bookTitle = "معالم التنزيل (البغوي)",
            era = "القرن السادس الهجري",
            methodologyBadge = "تفسير أثري معتدل",
            publisher = "Quran.com API v4 / QUL (ID: 94)",
            editionTitle = "معالم التنزيل في تفسير القرآن — الإمام البغوي",
            attribution = "الإمام البغوي • المصدر: Quran.com API v4 / QUL (ar-tafsir-al-baghawi)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "94",
            spa5kSlug = "ar-tafsir-al-baghawi"
        ),
        OnlineTafseerEdition(
            id = "ar_saadi",
            language = AppLanguage.ARABIC,
            displayOrder = 16,
            scholarName = "الشيخ عبد الرحمن بن ناصر السعدي",
            bookTitle = "تيسير الكريم الرحمن (السعدي)",
            era = "القرن الرابع عشر الهجري",
            methodologyBadge = "تفسير تحليلي ميسّر",
            publisher = "Quran.com API v4 / QUL (ID: 91)",
            editionTitle = "تيسير الكريم الرحمن في تفسير كلام المنان",
            attribution = "الشيخ عبد الرحمن السعدي • المصدر: Quran.com API v4 / QUL (ar-tafseer-al-saddi)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "91",
            spa5kSlug = "ar-tafseer-al-saddi"
        ),
        OnlineTafseerEdition(
            id = "ar_wasit",
            language = AppLanguage.ARABIC,
            displayOrder = 17,
            scholarName = "الشيخ محمد سيد طنطاوي",
            bookTitle = "التفسير الوسيط للقرآن الكريم",
            era = "العصر الحديث",
            methodologyBadge = "تفسير بياني معاصر",
            publisher = "Quran.com API v4 / QUL (ID: 93)",
            editionTitle = "التفسير الوسيط للقرآن الكريم",
            attribution = "د. محمد سيد طنطاوي • المصدر: Quran.com API v4 / QUL (ar-tafsir-al-wasit)",
            sourceUrl = "https://quran.com/",
            quranFoundationId = "93",
            spa5kSlug = "ar-tafsir-al-wasit"
        ),
        OnlineTafseerEdition(
            id = "ar_jalalayn",
            language = AppLanguage.ARABIC,
            displayOrder = 18,
            scholarName = "الإمامان المحلي والسيوطي",
            bookTitle = "تفسير الجلالين",
            era = "القرن التاسع والعاشر الهجري",
            methodologyBadge = "تفسير لغوي موجز",
            publisher = "Al-Tafsir.com / spa5k tafsir_api",
            editionTitle = "تفسير الجلالين — العربية",
            attribution = "جلال الدين المحلي وجلال الدين السيوطي • المصدر: Al-Tafsir.com / spa5k tafsir_api",
            sourceUrl = "https://www.altafsir.com/",
            quranFoundationId = null,
            spa5kSlug = "ar-tafsir-al-jalalayn"
        )
    )

    fun onlineEditionsFor(language: AppLanguage): List<OnlineTafseerEdition> =
        onlineEditions.filter { it.language == language }.sortedBy { it.displayOrder }

    fun resolveOnlineEdition(
        sourceId: String,
        language: AppLanguage
    ): OnlineTafseerEdition {
        val editions = onlineEditionsFor(language)
        return editions.firstOrNull { it.id == sourceId } ?: editions.first()
    }

    fun resolveStrictOnlineEdition(
        sourceId: String,
        language: AppLanguage
    ): OnlineTafseerEdition? =
        onlineEditions.firstOrNull { it.id == sourceId && it.language == language }

    fun toTafseerSourceEntities(): List<TafseerSourceEntity> =
        onlineEditions.map { edition ->
            TafseerSourceEntity(
                id = edition.id,
                displayOrder = edition.displayOrder,
                scholarName = edition.scholarName,
                translatorName = edition.publisher,
                reviewers = "",
                bookTitle = edition.bookTitle,
                originalLanguage = edition.language.code,
                publisher = edition.publisher,
                edition = edition.editionTitle,
                sourceUrl = edition.sourceUrl,
                licenseEvidenceUrl = "https://cdn.jsdelivr.net/gh/spa5k/tafsir_api@v1.2.3/tafsir/editions.json",
                permissionStatus = "ONLINE_LIVE_API",
                verificationStatus = "AUTHORIZED_ONLINE_EDITION",
                offlineAllowed = false,
                onlineAllowed = true,
                isDownloaded = false,
                downloadedBytes = 0L,
                totalPassagesCached = 0,
                lastSyncedTimestamp = 0L
            )
        }

    fun onlineFetchErrorMessage(language: AppLanguage, bookTitle: String): String = when (language) {
        AppLanguage.URDU -> "انٹرنیٹ سے «$bookTitle» کی تفسیر حاصل نہیں ہو سکی۔ براہِ کرم انٹرنیٹ کنکشن چیک کریں اور دوبارہ کوشش (Retry) کریں۔"
        AppLanguage.ARABIC -> "تعذر جلب تفسير «$bookTitle» عبر الإنترنت حالياً. يرجى التحقق من الاتصال ثم الضغط على إعادة المحاولة."
        AppLanguage.HINDI -> "इंटरनेट से \"$bookTitle\" की तफ़सीर लोड नहीं हो सकी। कृपया इंटरनेट कनेक्शन जाँचें और पुनः प्रयास करें।"
        AppLanguage.ENGLISH -> "Unable to fetch scholarly Tafseer from $bookTitle online. Please check your internet connection and tap Retry."
    }

    fun ayahNotIndexedNotice(
        language: AppLanguage,
        bookTitle: String,
        surahNumber: Int,
        ayahNumber: Int
    ): String = when (language) {
        AppLanguage.URDU -> "آن لائن ماخذ ($bookTitle) میں آیت $surahNumber:$ayahNumber کی مستقل اندراجی تفسیر فی الحال دستیاب نہیں ہے۔ براہِ کرم اوپر موجود کسی دوسرے مفسر کا انتخاب کریں یا دوبارہ کوشش کریں۔"
        AppLanguage.ARABIC -> "الآية $surahNumber:$ayahNumber غير مفهرسة كمدخل مستقل في المصدر الإلكتروني لـ ($bookTitle). يرجى اختيار مفسر آخر أعلاه أو إعادة المحاولة."
        AppLanguage.HINDI -> "ऑनलाइन स्रोत ($bookTitle) में आयत $surahNumber:$ayahNumber की अलग प्रविष्टि अभी उपलब्ध नहीं है। कृपया ऊपर से अन्य स्रोत चुनें या पुनः प्रयास करें।"
        AppLanguage.ENGLISH -> "A standalone online entry for Ayah $surahNumber:$ayahNumber was not returned by $bookTitle. Please select another scholar above or tap Retry."
    }

    fun languageDepthTransparencyNote(language: AppLanguage): String? = when (language) {
        AppLanguage.HINDI -> "नोट: हिन्दी भाषा में प्रमाणित ऑनलाइन स्रोतों (QUL / spa5k tafsir_api) पर वर्तमान में केवल 'अल-मुख़्तसर फ़ी तफ़सीर अल-क़ुरआन' उपलब्ध है; हिन्दी में 3-पृष्ठ जैसी विस्तृत बहु-खंडीय तफ़सीर अभी इन आधिकारिक स्रोतों पर उपलब्ध नहीं है। कोई मनगढ़ंत पाठ या अन्य भाषा का अनुवाद नहीं जोड़ा गया है।"
        else -> null
    }
}
