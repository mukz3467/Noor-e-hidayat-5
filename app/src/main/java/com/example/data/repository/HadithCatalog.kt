package com.example.data.repository

import com.example.data.model.AppLanguage

data class HadithBookInfo(
    val collection: String,
    val bookNumber: Int,
    val volumeNumber: Int,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val hadithRange: String,
    val expectedNarrationCount: Int,
    val apiSectionId: Int,
    val sourceUrl: String
) {
    fun localizedTitle(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> titleUr
        AppLanguage.ARABIC -> titleAr
        AppLanguage.HINDI -> titleHi
        AppLanguage.ENGLISH -> titleEn
    }
}

data class HadithVolumeInfo(
    val volumeNumber: Int,
    val titleEn: String,
    val titleUr: String,
    val titleAr: String,
    val titleHi: String,
    val bookStart: Int,
    val bookEnd: Int,
    val hadithRange: String,
    val editionReference: String
) {
    fun localizedTitle(language: AppLanguage): String = when (language) {
        AppLanguage.URDU -> titleUr
        AppLanguage.ARABIC -> titleAr
        AppLanguage.HINDI -> titleHi
        AppLanguage.ENGLISH -> titleEn
    }
}

data class HadithEditionManifest(
    val collection: String,
    val titleArabic: String,
    val compiler: String,
    val totalBooks: Int,
    val totalVolumes: Int,
    val canonicalTotalNarrations: Int,
    val volumeEditionName: String,
    val primarySourceName: String,
    val primarySourceUrl: String,
    val openDatasetRepo: String,
    val openDatasetLicense: String,
    val arabicEditionId: String,
    val englishEditionId: String,
    val englishTranslator: String,
    val urduEditionId: String,
    val urduTranslator: String,
    val hindiStatusNote: String,
    val attributionNotice: String
)

object HadithCatalog {

    const val COLLECTION_BUKHARI = "Sahih al-Bukhari"
    const val COLLECTION_MUSLIM = "Sahih Muslim"

    val bukhariManifest = HadithEditionManifest(
        collection = COLLECTION_BUKHARI,
        titleArabic = "الجامع المسند الصحيح المختصر من أمور رسول الله ﷺ وسننه وأيامه (صحيح البخاري)",
        compiler = "Imam Muhammad ibn Isma'il al-Bukhari (194–256 AH)",
        totalBooks = 97,
        totalVolumes = 7,
        canonicalTotalNarrations = 7563,
        volumeEditionName = "7-Volume Unabridged Structural Index (Maulana Dawood Raz / Maktaba Quddusia 7-Jild Mapping to Canonical 97 Kutub)",
        primarySourceName = "Sunnah.com & Fawazahmed0 Hadith API CDN",
        primarySourceUrl = "https://sunnah.com/bukhari",
        openDatasetRepo = "https://github.com/fawazahmed0/hadith-api",
        openDatasetLicense = "The Unlicense (Public Domain Dedication) for open CDN editions (ara-bukhari, eng-bukhari, urd-bukhari)",
        arabicEditionId = "ara-bukhari",
        englishEditionId = "eng-bukhari",
        englishTranslator = "Dr. Muhammad Muhsin Khan (Open Public-Domain Hadith API Edition)",
        urduEditionId = "urd-bukhari",
        urduTranslator = "Urdu Open Hadith API Edition (aligned with Maulana Dawood Raz / Kitab-o-Sunnat index)",
        hindiStatusNote = "Verified curated offline narrations include 4-language Hindi; full-book downloads supply complete Arabic, English & Urdu (open Hindi full-collection JSON is not provided by fawazahmed0/hadith-api).",
        attributionNotice = "Texts sourced from open-licensed fawazahmed0/hadith-api (The Unlicense) with canonical book numbering cross-referenced to Sunnah.com (7,563 narrations across 97 books)."
    )

    val muslimManifest = HadithEditionManifest(
        collection = COLLECTION_MUSLIM,
        titleArabic = "المسند الصحيح المختصر بنقل العدل عن العدل إلى رسول الله ﷺ (صحيح مسلم)",
        compiler = "Imam Muslim ibn al-Hajjaj al-Naysaburi (204–261 AH)",
        totalBooks = 57, // Introduction (Book 0) + 56 Thematic Books (Books 1..56)
        totalVolumes = 1,
        canonicalTotalNarrations = 3033, // Main Abdul Baqi chapters spanning 7,500+ sub-narrations
        volumeEditionName = "Canonical 57 Sections (Introduction + 56 Kutub, Independent Sahih Muslim Numbering)",
        primarySourceName = "Sunnah.com & Fawazahmed0 Hadith API CDN",
        primarySourceUrl = "https://sunnah.com/muslim",
        openDatasetRepo = "https://github.com/fawazahmed0/hadith-api",
        openDatasetLicense = "The Unlicense (Public Domain Dedication) for open CDN editions (ara-muslim, eng-muslim, urd-muslim)",
        arabicEditionId = "ara-muslim",
        englishEditionId = "eng-muslim",
        englishTranslator = "Imam Abdul Hamid Siddiqui (Open Public-Domain Hadith API Edition)",
        urduEditionId = "urd-muslim",
        urduTranslator = "Urdu Open Hadith API Edition",
        hindiStatusNote = "Verified curated offline narrations include 4-language Hindi; full-book downloads supply complete Arabic, English & Urdu (open Hindi full-collection JSON is not provided by fawazahmed0/hadith-api).",
        attributionNotice = "Sahih Muslim numbering is kept strictly independent from Sahih al-Bukhari. Sourced from fawazahmed0/hadith-api (The Unlicense) and cross-referenced to Sunnah.com."
    )

    /**
     * Authentic 7-Volume Sahih al-Bukhari mapping to the 97 canonical books.
     * Based on the 7-Jild Dawood Raz / Maktaba Quddusia unabridged thematic grouping
     * aligned seamlessly across Books 1..97 without splitting any Book across volumes.
     */
    val bukhari7Volumes: List<HadithVolumeInfo> = listOf(
        HadithVolumeInfo(
            volumeNumber = 1,
            titleEn = "Vol. 1: Revelation, Faith, Knowledge & Early Prayer",
            titleUr = "جلد 1: وحی، ایمان، علم، طہارت اور نماز (کتاب 1 تا 11)",
            titleAr = "المجلد الأول: بدء الوحي والإيمان والعلم والطهارة والجمعة",
            titleHi = "जिल्द 1: वह्य, ईमान, इल्म, वुज़ू और नमाज़ (किताब 1–11)",
            bookStart = 1,
            bookEnd = 11,
            hadithRange = "1–941",
            editionReference = "7-Volume Index • Jild 1 (Books 1–11)"
        ),
        HadithVolumeInfo(
            volumeNumber = 2,
            titleEn = "Vol. 2: Voluntary Prayers, Funerals, Zakat & Hajj",
            titleUr = "جلد 2: نوافل، جنازہ، زکوٰۃ، حج اور فضائلِ مدینہ (کتاب 12 تا 29)",
            titleAr = "المجلد الثاني: صلاة الخوف والجنائز والزكاة والحج وفضائل المدينة",
            titleHi = "जिल्द 2: नमाज़ें, जनाज़ा, ज़कात और हज (किताब 12–29)",
            bookStart = 12,
            bookEnd = 29,
            hadithRange = "942–1890",
            editionReference = "7-Volume Index • Jild 2 (Books 12–29)"
        ),
        HadithVolumeInfo(
            volumeNumber = 3,
            titleEn = "Vol. 3: Fasting, Sales, Contracts, Oppressions & Wills",
            titleUr = "جلد 3: روزہ، بیوع، قرض، مظالم، ہبہ اور وصایا (کتاب 30 تا 55)",
            titleAr = "المجلد الثالث: الصوم والبيوع والقرض والمظالم والوصايا",
            titleHi = "जिल्द 3: रोज़ा, तिजारत, क़र्ज़ और वसीयत (किताब 30–55)",
            bookStart = 30,
            bookEnd = 55,
            hadithRange = "1891–2781",
            editionReference = "7-Volume Index • Jild 3 (Books 30–55)"
        ),
        HadithVolumeInfo(
            volumeNumber = 4,
            titleEn = "Vol. 4: Jihad, Creation, Prophets, Virtues & Expeditions",
            titleUr = "جلد 4: جہاد، بدء الخلق، انبیاء، مناقب اور مغازی (کتاب 56 تا 64)",
            titleAr = "المجلد الرابع: الجهاد وبدء الخلق والأنبياء والمناقب والمغازي",
            titleHi = "जिल्द 4: जिहाद, पैग़म्बर, सहाबा और ग़ज़वात (किताब 56–64)",
            bookStart = 56,
            bookEnd = 64,
            hadithRange = "2782–4473",
            editionReference = "7-Volume Index • Jild 4 (Books 56–64)"
        ),
        HadithVolumeInfo(
            volumeNumber = 5,
            titleEn = "Vol. 5: Prophetic Tafseer, Virtues of Quran, Marriage & Food",
            titleUr = "جلد 5: تفسیر القرآن، فضائلِ قرآن، نکاح، طلاق اور اطعمہ (کتاب 65 تا 71)",
            titleAr = "المجلد الخامس: التفسير وفضائل القرآن والنكاح والطلاق والأطعمة",
            titleHi = "जिल्द 5: तफ़सीर, क़ुरआन की फ़ज़ीलत, निकाह और भोजन (किताब 65–71)",
            bookStart = 65,
            bookEnd = 71,
            hadithRange = "4474–5474",
            editionReference = "7-Volume Index • Jild 5 (Books 65–71)"
        ),
        HadithVolumeInfo(
            volumeNumber = 6,
            titleEn = "Vol. 6: Slaughtering, Medicine, Dress, Manners, Duas & Qadar",
            titleUr = "جلد 6: ذبائح، طب، لباس، ادب، دعوات، رقاق اور قدر (کتاب 72 تا 82)",
            titleAr = "المجلد السادس: الذبائح والطب واللباس والأدب والدعوات والرقاق والقدر",
            titleHi = "जिल्द 6: इलाज, लिबास, अदब, दुआएं और तक़दीर (किताब 72–82)",
            bookStart = 72,
            bookEnd = 82,
            hadithRange = "5475–6620",
            editionReference = "7-Volume Index • Jild 6 (Books 72–82)"
        ),
        HadithVolumeInfo(
            volumeNumber = 7,
            titleEn = "Vol. 7: Oaths, Inheritance, Hudud, Dreams, Fitan & Tawheed",
            titleUr = "جلد 7: ایمان و نذور، فرائض، حدود، تعبیر، فتن، احکام اور توحید (کتاب 83 تا 97)",
            titleAr = "المجلد السابع: الأيمان والفرائض والحدود والفتن والأحكام والتوحيد",
            titleHi = "जिल्द 7: क़सम, विरासत, हदूद, फ़ितने और तौहीद (किताब 83–97)",
            bookStart = 83,
            bookEnd = 97,
            hadithRange = "6621–7563",
            editionReference = "7-Volume Index • Jild 7 (Books 83–97)"
        )
    )

    private fun bukhariVolumeForBook(bookNum: Int): Int = when (bookNum) {
        in 1..11 -> 1
        in 12..29 -> 2
        in 30..55 -> 3
        in 56..64 -> 4
        in 65..71 -> 5
        in 72..82 -> 6
        else -> 7
    }

    private fun b(
        num: Int,
        en: String,
        ur: String,
        ar: String,
        hi: String,
        range: String,
        count: Int
    ) = HadithBookInfo(
        collection = COLLECTION_BUKHARI,
        bookNumber = num,
        volumeNumber = bukhariVolumeForBook(num),
        titleEn = en,
        titleUr = ur,
        titleAr = ar,
        titleHi = hi,
        hadithRange = range,
        expectedNarrationCount = count,
        apiSectionId = num,
        sourceUrl = "https://sunnah.com/bukhari/$num"
    )

    private fun m(
        num: Int,
        en: String,
        ur: String,
        ar: String,
        hi: String,
        range: String,
        count: Int
    ) = HadithBookInfo(
        collection = COLLECTION_MUSLIM,
        bookNumber = num,
        volumeNumber = 1,
        titleEn = en,
        titleUr = ur,
        titleAr = ar,
        titleHi = hi,
        hadithRange = range,
        expectedNarrationCount = count,
        apiSectionId = num,
        sourceUrl = if (num == 0) "https://sunnah.com/muslim/introduction" else "https://sunnah.com/muslim/$num"
    )

    /**
     * All 97 Canonical Books of Sahih al-Bukhari mapped 1-to-1 with Sunnah.com & fawazahmed0/hadith-api.
     */
    val bukhari97Books: List<HadithBookInfo> = listOf(
        b(1, "Revelation", "بدء الوحی (وحی کی ابتدا)", "كتاب بدء الوحي", "वह्य (ईश्वरीय वाणी की शुरुआत)", "1–7", 7),
        b(2, "Belief (Al-Iman)", "کتاب الایمان", "كتاب الإيمان", "ईमान (आस्था) की किताब", "8–58", 51),
        b(3, "Knowledge (Al-'Ilm)", "کتاب العلم", "كتاب العلم", "इल्म (ज्ञान) की किताब", "59–134", 76),
        b(4, "Ablutions (Wudu')", "کتاب الوضوء", "كتاب الوضوء", "वुज़ू की किताब", "135–247", 113),
        b(5, "Bathing (Ghusl)", "کتاب الغسل", "كتاب الغسل", "ग़ुस्ल (स्नान) की किताब", "248–293", 46),
        b(6, "Menstrual Periods", "کتاب الحیض", "كتاب الحيض", "माहवारी (हैज़) की किताब", "294–333", 40),
        b(7, "Rubbing Hands and Feet with Dust (Tayammum)", "کتاب التیمم", "كتاب التيمم", "तयम्मुम की किताब", "334–348", 15),
        b(8, "Prayers (Salat)", "کتاب الصلاۃ", "كتاب الصلاة", "नमाज़ (सलात) की किताब", "349–520", 172),
        b(9, "Times of the Prayers", "کتاب مواقیت الصلاۃ", "كتاب مواقيت الصلاة", "नमाज़ के समय की किताब", "521–602", 82),
        b(10, "Call to Prayers (Adhaan)", "کتاب الاذان", "كتاب الأذان", "अज़ान की किताब", "603–875", 273),
        b(11, "Friday Prayer (Al-Jumu'ah)", "کتاب الجمعہ", "كتاب الجمعة", "जुमा की नमाज़", "876–941", 66),
        b(12, "Fear Prayer (Salat al-Khawf)", "کتاب صلاۃ الخوف", "كتاب صلاة الخوف", "ख़ौफ़ की नमाज़", "942–947", 6),
        b(13, "The Two Festivals (Eids)", "کتاب العیدین", "كتاب العيدين", "दोनों ईद की किताब", "948–989", 42),
        b(14, "Witr Prayer", "کتاب الوتر", "كتاب الوتر", "वित्र की नमाज़", "990–1004", 15),
        b(15, "Invoking Allah for Rain (Istisqaa)", "کتاب الاستسقاء", "كتاب الاستسقاء", "बारिश के लिए दुआ (इस्तिस्क़ा)", "1005–1039", 35),
        b(16, "Eclipses (Kusuf)", "کتاب الکسوف", "كتاب الكسوف", "सूर्य और चंद्र ग्रहण", "1040–1066", 27),
        b(17, "Prostration During Recital of Qur'an", "کتاب سجود القرآن", "كتاب سجود القرآن", "तिलावत के सजदे", "1067–1079", 13),
        b(18, "Shortening the Prayers (At-Taqseer)", "کتاب تقصیر الصلاۃ", "كتاب تقصير الصلاة", "सफ़र में नमाज़ क़स्र करना", "1080–1119", 40),
        b(19, "Prayer at Night (Tahajjud)", "کتاب التہجد", "كتاب التهجد", "तहज्जुद की नमाज़", "1120–1187", 68),
        b(20, "Virtues of Prayer at Masjid Makkah and Madinah", "فضائلِ نماز مسجدِ حرام و مسجدِ نبوی", "كتاب فضل الصلاة في مسجد مكة والمدينة", "मक्का व मदीना की मस्जिद में नमाज़ की फ़ज़ीलत", "1188–1197", 10),
        b(21, "Actions while Praying", "کتاب العمل فی الصلاۃ", "كتاب العمل في الصلاة", "नमाज़ के दौरान के अमल", "1198–1223", 26),
        b(22, "Forgetfulness in Prayer (As-Sahw)", "کتاب السہو", "كتاب السهو", "नमाज़ में भूल (सजदा-ए-सह्व)", "1224–1236", 13),
        b(23, "Funerals (Al-Janaa'iz)", "کتاب الجنائز", "كتاب الجنائز", "जनाज़े की किताब", "1237–1394", 158),
        b(24, "Obligatory Charity Tax (Zakat)", "کتاب الزکوٰۃ", "كتاب الزكاة", "ज़कात की किताब", "1395–1512", 118),
        b(25, "Hajj (Pilgrimage)", "کتاب الحج", "كتاب الحج", "हज की किताब", "1513–1772", 260),
        b(26, "'Umrah (Minor Pilgrimage)", "کتاب العمرہ", "كتاب العمرة", "उमरा की किताब", "1773–1805", 33),
        b(27, "Pilgrims Prevented from Completing Pilgrimage", "کتاب المحصر", "كتاب المحصر", "हज से रोके गए मुहरिम के अहकाम", "1806–1820", 15),
        b(28, "Penalty of Hunting while on Pilgrimage", "کتاب جزاء الصید", "كتاب جزاء الصيد", "एहराम में शिकार का जुर्माना", "1821–1866", 46),
        b(29, "Virtues of Madinah", "کتاب فضائل المدینہ", "كتاب فضائل المدينة", "मदीना मुनव्वरा की फ़ज़ीलत", "1867–1890", 24),
        b(30, "Fasting (As-Sawm)", "کتاب الصوم", "كتاب الصوم", "रोज़े की किताब", "1891–2007", 117),
        b(31, "Praying at Night in Ramadhan (Taraweeh)", "کتاب صلاۃ التراویح", "كتاب صلاة التراويح", "तरावीह की नमाज़", "2008–2013", 6),
        b(32, "Virtues of the Night of Qadr", "کتاب فضل لیلۃ القدر", "كتاب فضل ليلة القدر", "लैलतुल-क़द्र की फ़ज़ीलत", "2014–2024", 11),
        b(33, "Retiring to a Mosque for Remembrance (I'tikaf)", "کتاب الاعتکاف", "كتاب الاعتكاف", "एतिकाफ़ की किताब", "2025–2046", 22),
        b(34, "Sales and Trade (Al-Buyu')", "کتاب البیوع", "كتاب البيوع", "ख़रीद-फ़रोख़्त और तिजारत", "2047–2238", 192),
        b(35, "Sales with Advance Payment (As-Salam)", "کتاب السلم", "كتاب السلم", "अग्रिम भुगतान बिक्री (सलम)", "2239–2256", 18),
        b(36, "Shuf'a (Right of Pre-emption)", "کتاب الشفعہ", "كتاب الشفعة", "शुफ़ा (प्रथम क्रय अधिकार)", "2257–2259", 3),
        b(37, "Hiring (Al-Ijarah)", "کتاب الاجارہ", "كتاب الإجارة", "मज़दूरी और किरायेदारी", "2260–2286", 27),
        b(38, "Transferance of a Debt (Al-Hawaala)", "کتاب الحوالات", "كتاب الحوالات", "क़र्ज़ हस्तांतरण (हवाला)", "2287–2289", 3),
        b(39, "Kafalah (Guarantee)", "کتاب الکفالہ", "كتاب الكفالة", "ज़मानत (कफ़ाला)", "2290–2298", 9),
        b(40, "Representation & Authorization (Al-Wakala)", "کتاب الوکالہ", "كتاب الوكالة", "वकालत व प्रतिनिधित्व", "2299–2319", 21),
        b(41, "Agriculture (Al-Muzara'ah)", "کتاب المزارعہ", "كتاب المزارعة", "खेती-बाड़ी (मुज़ारआ)", "2320–2350", 31),
        b(42, "Distribution of Water (Al-Musaqah)", "کتاب المساقاۃ", "كتاب المساقاة", "सिंचाई और पानी का बंटवारा", "2351–2384", 34),
        b(43, "Loans, Freezing of Property & Bankruptcy", "کتاب الاستقراض و التفلیس", "كتاب في الاستقراض وأداء الديون", "क़र्ज़ लेना, चुकाना और दिवालियापन", "2385–2409", 25),
        b(44, "Khusoomaat (Disputes)", "کتاب الخصومات", "كتاب الخصومات", "झगड़े और मुक़दमे", "2410–2425", 16),
        b(45, "Lost Things Picked Up (Al-Luqatah)", "کتاب اللقطہ", "كتاب في اللقطة", "रास्ते में गिरी हुई चीज़ (लुक़ता)", "2426–2439", 14),
        b(46, "Oppressions (Al-Mazalim)", "کتاب المظالم", "كتاب المظالم", "ज़ुल्म और अन्याय के विरुद्ध", "2440–2482", 43),
        b(47, "Partnership (Ash-Sharika)", "کتاب الشرکہ", "كتاب الشركة", "साझेदारी (शराकत)", "2483–2507", 25),
        b(48, "Mortgaging (Ar-Rahn)", "کتاب الرہن", "كتاب الرهن", "गिरवी (रह्न)", "2508–2515", 8),
        b(49, "Manumission of Slaves (Al-'Itq)", "کتاب العتق", "كتاب العتق", "ग़ुलाम आज़ाद करने का बयान", "2516–2559", 44),
        b(50, "Makaatib (Written Emancipation)", "کتاب المکاتب", "كتاب المكاتب", "मुकातब (आज़ादी का अनुबंध)", "2560–2565", 6),
        b(51, "Gifts (Al-Hibah)", "کتاب الہبہ", "كتاب الهبة وفضلها والتحريض عليها", "तोहफ़ा और हिबा की किताब", "2566–2636", 71),
        b(52, "Witnesses (Ash-Shahadat)", "کتاب الشہادات", "كتاب الشهادات", "गवाही की किताब", "2637–2689", 53),
        b(53, "Peacemaking (As-Sulh)", "کتاب الصلح", "كتاب الصلح", "सुलह व समझौता", "2690–2710", 21),
        b(54, "Conditions (Ash-Shuroot)", "کتاب الشروط", "كتاب الشروط", "शर्तें व अनुबंध", "2711–2737", 27),
        b(55, "Wills and Testaments (Wasaayaa)", "کتاب الوصایا", "كتاب الوصايا", "वसीयत की किताब", "2738–2781", 44),
        b(56, "Fighting for the Cause of Allah (Jihaad)", "کتاب الجہاد والسیر", "كتاب الجهاد والسير", "जिहाद और मुहिम की किताब", "2782–3090", 309),
        b(57, "One-fifth of Booty (Khumus)", "کتاب فرض الخمس", "كتاب فرض الخمس", "ख़ुम्स (पाँचवाँ हिस्सा)", "3091–3155", 65),
        b(58, "Jizyah and Mawaada'ah (Treaties)", "کتاب الجزیہ والموادعہ", "كتاب الجزية والموادعة", "जिज़्या और संधियाँ", "3156–3189", 34),
        b(59, "Beginning of Creation (Bad' al-Khalq)", "کتاب بدء الخلق", "كتاب بدء الخلق", "सृष्टि की शुरुआत", "3190–3325", 136),
        b(60, "Prophets (Ahadith al-Anbiya)", "کتاب احادیث الانبیاء", "كتاب أحاديث الأنبياء", "पैग़म्बरों की हदीसें", "3326–3488", 163),
        b(61, "Virtues & Merits of Prophet ﷺ & Companions", "کتاب المناقب", "كتاب المناقب", "नबी ﷺ व सहाबा के फ़ज़ाइल", "3489–3648", 160),
        b(62, "Companions of the Prophet ﷺ", "کتاب فضائل اصحاب النبی ﷺ", "كتاب فضائل أصحاب النبي ﷺ", "सहाबा-ए-किराम की फ़ज़ीलत", "3649–3775", 127),
        b(63, "Merits of the Helpers in Madinah (Ansaar)", "کتاب مناقب الانصار", "كتاب مناقب الأنصار", "मदीना के अंसार की फ़ज़ीलत", "3776–3948", 173),
        b(64, "Military Expeditions led by Prophet ﷺ (Al-Maghaazi)", "کتاب المغازی", "كتاب المغازي", "नबवी ग़ज़वात की किताब", "3949–4473", 525),
        b(65, "Prophetic Commentary on Qur'an (Tafseer)", "کتاب التفسیر", "كتاب التفسير", "तफ़सीर-ए-क़ुरआन की किताब", "4474–4977", 504),
        b(66, "Virtues of the Qur'an", "کتاب فضائل القرآن", "كتاب فضائل القرآن", "क़ुरआन की फ़ज़ीलत की किताब", "4978–5062", 85),
        b(67, "Wedlock, Marriage (Nikaah)", "کتاب النکاح", "كتاب النكاح", "निकाह की किताब", "5063–5250", 188),
        b(68, "Divorce (At-Talaq)", "کتاب الطلاق", "كتاب الطلاق", "तलाक़ की किताब", "5251–5350", 100),
        b(69, "Supporting the Family (An-Nafaqat)", "کتاب النفقات", "كتاب النفقات", "परिवार के ख़र्च व भरण-पोषण", "5351–5372", 22),
        b(70, "Food, Meals (Al-At'imah)", "کتاب الاطعمہ", "كتاب الأطعمة", "भोजन व खान-पान", "5373–5466", 94),
        b(71, "Sacrifice on Occasion of Birth ('Aqiqa)", "کتاب العقیقہ", "كتاب العقيقة", "अक़ीक़ा की किताब", "5467–5474", 8),
        b(72, "Hunting & Slaughtering", "کتاب الذبائح والصید", "كتاب الذبائح والصيد", "शिकार और ज़बह की किताब", "5475–5544", 70),
        b(73, "Al-Adha Festival Sacrifice (Adahi)", "کتاب الاضحیہ", "كتاب الأضاحي", "क़ुर्बानी की किताब", "5545–5574", 30),
        b(74, "Drinks (Al-Ashribah)", "کتاب الاشربہ", "كتاب الأشربة", "पेय पदार्थों की किताब", "5575–5639", 65),
        b(75, "Patients (Al-Marda)", "کتاب المرضی", "كتاب المرضى", "बीमारों की किताब", "5640–5677", 38),
        b(76, "Medicine (At-Tibb)", "کتاب الطب", "كتاب الطب", "इलाज और तिब्ब की किताब", "5678–5782", 105),
        b(77, "Dress (Al-Libas)", "کتاب اللباس", "كتاب اللباس", "लिबास व पोशाक की किताब", "5783–5969", 187),
        b(78, "Good Manners and Form (Al-Adab)", "کتاب الادب", "كتاب الأدب", "अदब और शिष्टाचार", "5970–6226", 257),
        b(79, "Asking Permission (Al-Isti'dhan)", "کتاب الاستئذان", "كتاب الاستئذان", "अनुमति माँगना व सलाम", "6227–6303", 77),
        b(80, "Invocations (Ad-Da'awat)", "کتاب الدعوات", "كتاب الدعوات", "दुआओं की किताब", "6304–6411", 108),
        b(81, "To Make the Heart Tender (Ar-Riqaq)", "کتاب الرقاق", "كتاب الرقاق", "दिलों को नरम करने वाली बातें", "6412–6593", 182),
        b(82, "Divine Will (Al-Qadar)", "کتاب القدر", "كتاب القدر", "तक़दीर (अल्लाह का फ़ैसला)", "6594–6620", 27),
        b(83, "Oaths and Vows (Al-Aiman wan-Nudhur)", "کتاب الایمان والنذور", "كتاب الأيمان والنذور", "क़सम और मन्नत की किताब", "6621–6707", 87),
        b(84, "Expiation for Unfulfilled Oaths", "کتاب کفارات الایمان", "كتاب كفارات الأيمان", "क़सम का कफ़्फ़ारा", "6708–6722", 15),
        b(85, "Laws of Inheritance (Al-Faraa'id)", "کتاب الفرائض", "كتاب الفرائض", "विरासत के नियम", "6723–6771", 49),
        b(86, "Limits and Punishments set by Allah (Hudood)", "کتاب الحدود", "كتاب الحدود", "हदूद और सज़ाएं", "6772–6860", 89),
        b(87, "Blood Money (Ad-Diyat)", "کتاب الدیات", "كتاب الديات", "दियत (रक्त-मुआवज़ा)", "6861–6917", 57),
        b(88, "Apostates (Istitabat al-Murtaddin)", "کتاب استتابۃ المرتدین", "كتاب استتابة المرتدين والمعاندين وقتالهم", "मुरतद व बाग़ियों से तौबा", "6918–6939", 22),
        b(89, "(Statements under) Coercion (Al-Ikrah)", "کتاب الاکراہ", "كتاب الإكراه", "मजबूरी व ज़बरदस्ती (इकराह)", "6940–6952", 13),
        b(90, "Tricks / Legal Stratagems (Al-Hiyal)", "کتاب الحیل", "كتاب الحيل", "हीले (चतुराई व उपाय)", "6953–6981", 29),
        b(91, "Interpretation of Dreams (At-Ta'beer)", "کتاب التعبیر", "كتاب التعبير", "ख़्वाबों की ताबीर", "6982–7047", 66),
        b(92, "Afflictions & End of World (Al-Fitan)", "کتاب الفتن", "كتاب الفتن", "फ़ितने व क़यामत की निशानियाँ", "7048–7136", 89),
        b(93, "Judgments (Al-Ahkaam)", "کتاب الاحکام", "كتاب الأحكام", "न्याय व अहकाम की किताब", "7137–7225", 89),
        b(94, "Wishes (At-Tamanni)", "کتاب التمنی", "كتاب التمني", "तमन्ना व आरज़ू की किताब", "7226–7245", 20),
        b(95, "Truthful Solitary Reports (Akhbar al-Ahad)", "کتاب اخبار الآحاد", "كتاب أخبار الآحاد", "ख़बर-ए-वाहिद की किताब", "7246–7267", 22),
        b(96, "Holding Fast to Qur'an and Sunnah", "کتاب الاعتصام بالکتاب والسنہ", "كتاب الاعتصام بالكتاب والسنة", "क़ुरआन और सुन्नत को मज़बूती से थामना", "7268–7370", 103),
        b(97, "Oneness of Allah (At-Tawheed)", "کتاب التوحید", "كتاب التوحيد", "तौहीद (अल्लाह की एकता)", "7371–7563", 193)
    )

    /**
     * All 57 Canonical Sections of Sahih Muslim (Introduction Book 0 + Books 1..56).
     * Independent Sahih Muslim numbering (never merged with Sahih al-Bukhari).
     */
    val muslim57Books: List<HadithBookInfo> = listOf(
        m(0, "Introduction (Muqaddimah)", "مقدمہ صحیح مسلم", "المقدمة", "परिचय (मुक़द्दमा)", "1–7", 7),
        m(1, "The Book of Faith (Al-Iman)", "کتاب الایمان", "كتاب الإيمان", "ईमान की किताब", "8–222", 215),
        m(2, "The Book of Purification (At-Taharah)", "کتاب الطہارۃ", "كتاب الطهارة", "पवित्रता (तहारत)", "223–292", 70),
        m(3, "The Book of Menstruation (Al-Haid)", "کتاب الحیض", "كتاب الحيض", "माहवारी (हैज़)", "293–376", 84),
        m(4, "The Book of Prayers (As-Salat)", "کتاب الصلاۃ", "كتاب الصلاة", "नमाज़ (सलात)", "377–519", 143),
        m(5, "Mosques and Places of Prayer", "کتاب المساجد ومواضع الصلاۃ", "كتاب المساجد ومواضع الصلاة", "मस्जिदें और नमाज़ के स्थान", "520–684", 165),
        m(6, "Prayer — Travellers (Al-Musafirin)", "کتاب صلاۃ المسافرین", "كتاب صلاة المسافرين وقصرها", "मुसाफ़िर की नमाज़", "685–843", 159),
        m(7, "Prayer — Friday (Al-Jumu'ah)", "کتاب الجمعہ", "كتاب الجمعة", "जुमा की नमाज़", "844–883", 40),
        m(8, "Prayer — Two Eids (Al-Eidayn)", "کتاب صلاۃ العیدین", "كتاب صلاة العيدين", "दोनों ईद की नमाज़", "884–893", 10),
        m(9, "Prayer — Rain (Al-Istisqa')", "کتاب صلاۃ الاستسقاء", "كتاب صلاة الاستسقاء", "बारिश के लिए नमाज़", "894–900", 7),
        m(10, "Prayer — Eclipses (Al-Kusuf)", "کتاب الکسوف", "كتاب الكسوف", "ग्रहण की नमाज़", "901–915", 15),
        m(11, "Prayer — Funerals (Al-Jana'iz)", "کتاب الجنائز", "كتاب الجنائز", "जनाज़े की किताब", "916–978", 63),
        m(12, "The Book of Zakat", "کتاب الزکوٰۃ", "كتاب الزكاة", "ज़कात की किताब", "979–1078", 100),
        m(13, "The Book of Fasting (As-Siyam)", "کتاب الصیام", "كتاب الصيام", "रोज़े की किताब", "1079–1170", 92),
        m(14, "The Book of I'tikaf", "کتاب الاعتکاف", "كتاب الاعتكاف", "एतिकाफ़ की किताब", "1171–1176", 6),
        m(15, "The Book of Pilgrimage (Al-Hajj)", "کتاب الحج", "كتاب الحج", "हज की किताब", "1177–1399", 223),
        m(16, "The Book of Marriage (An-Nikah)", "کتاب النکاح", "كتاب النكاح", "निकाह की किताब", "1400–1443", 44),
        m(17, "The Book of Suckling (Ar-Rada')", "کتاب الرضاع", "كتاب الرضاع", "रज़ाअत (दूध का रिश्ता)", "1444–1470", 27),
        m(18, "The Book of Divorce (At-Talaq)", "کتاب الطلاق", "كتاب الطلاق", "तलाक़ की किताब", "1471–1491", 21),
        m(19, "Invoking Curses (Al-Li'an)", "کتاب اللعان", "كتاب اللعان", "लिआन की किताब", "1492–1500", 9),
        m(20, "Emancipating Slaves (Al-'Itq)", "کتاب العتق", "كتاب العتق", "ग़ुलाम आज़ाद करना", "1501–1510", 10),
        m(21, "The Book of Transactions (Al-Buyu')", "کتاب البیوع", "كتاب البيوع", "तिजारत और लेन-देन", "1511–1550", 40),
        m(22, "The Book of Musaqah", "کتاب المساقاۃ", "كتاب المساقاة", "मुसाक़ात और सिंचाई", "1551–1613", 63),
        m(23, "Rules of Inheritance (Al-Fara'id)", "کتاب الفرائض", "كتاب الفرائض", "विरासत के नियम", "1614–1619", 6),
        m(24, "The Book of Gifts (Al-Hibat)", "کتاب الہبات", "كتاب الهبات", "उपहार (हिबा)", "1620–1626", 7),
        m(25, "The Book of Wills (Al-Wasiyyah)", "کتاب الوصیہ", "كتاب الوصية", "वसीयत की किताब", "1627–1637", 11),
        m(26, "The Book of Vows (An-Nadhr)", "کتاب النذر", "كتاب النذر", "मन्नत (नज़्र) की किताब", "1638–1645", 8),
        m(27, "The Book of Oaths (Al-Aiman)", "کتاب الایمان", "كتاب الأيمان", "क़सम की किताब", "1646–1668", 23),
        m(28, "Oaths, Muharibin, Qasas & Diyat", "کتاب القسامۃ والمحاربین والقصاص", "كتاب القسامة والمحاربين والقصاص والديات", "क़सास और दियत", "1669–1683", 15),
        m(29, "Legal Punishments (Al-Hudud)", "کتاب الحدود", "كتاب الحدود", "हदूद (क़ानूनी सज़ाएं)", "1684–1710", 27),
        m(30, "Judicial Decisions (Al-Aqdiyah)", "کتاب الاقضیہ", "كتاب الأقضية", "न्याय व फ़ैसले", "1711–1721", 11),
        m(31, "Lost Property (Al-Luqatah)", "کتاب اللقطہ", "كتاب اللقطة", "रास्ते में मिली चीज़", "1722–1729", 8),
        m(32, "Jihad and Expeditions", "کتاب الجہاد والسیر", "كتاب الجهاد والسير", "जिहाद और अभियान", "1730–1817", 88),
        m(33, "The Book of Leadership (Al-Imarah)", "کتاب الامارۃ", "كتاب الإمارة", "नेतृत्व व हुकूमत", "1818–1928", 111),
        m(34, "Hunting, Slaughter & Lawful Animals", "کتاب الصید والذبائح", "كتاب الصيد والذبائح وما يؤكل من الحيوان", "शिकार और हलाल जानवर", "1929–1960", 32),
        m(35, "The Book of Sacrifices (Al-Adahi)", "کتاب الاضاحی", "كتاب الأضاحي", "क़ुर्बानी की किताब", "1961–1978", 18),
        m(36, "The Book of Drinks (Al-Ashribah)", "کتاب الاشربہ", "كتاب الأشربة", "पेय पदार्थ", "1979–2064", 86),
        m(37, "Clothes and Adornment", "کتاب اللباس والزینہ", "كتاب اللباس والزينة", "वस्त्र और श्रृंगार", "2065–2130", 66),
        m(38, "Manners and Etiquette (Al-Adab)", "کتاب الادب", "كتاب الآداب", "अदब और शिष्टाचार", "2131–2159", 29),
        m(39, "The Book of Greetings (As-Salam)", "کتاب السلام", "كتاب السلام", "सलाम और अभिवादन", "2160–2245", 86),
        m(40, "Use of Correct Words", "کتاب الالفاظ من الادب", "كتاب الألفاظ من الأدب وغيرها", "सही शब्दों का उपयोग", "2246–2254", 9),
        m(41, "The Book of Poetry (Ash-Shi'r)", "کتاب الشعر", "كتاب الشعر", "शायरी व कविता", "2255–2260", 6),
        m(42, "The Book of Visions (Ar-Ru'ya)", "کتاب الرؤیا", "كتاب الرؤيا", "सपने (ख़्वाब)", "2261–2275", 15),
        m(43, "The Book of Virtues (Al-Fada'il)", "کتاب الفضائل", "كتاب الفضائل", "नबी ﷺ के फ़ज़ाइल", "2276–2380", 105),
        m(44, "Virtues of the Companions", "کتاب فضائل الصحابہ", "كتاب فضائل الصحابة رضي الله عنهم", "सहाबा के फ़ज़ाइल", "2381–2547", 167),
        m(45, "Righteousness, Kindness & Kinship", "کتاب البر والصلۃ والآداب", "كتاب البر والصلة والآداب", "नेकी, रिश्तेदारी व सद्व्यवहार", "2548–2642", 95),
        m(46, "The Book of Destiny (Al-Qadar)", "کتاب القدر", "كتاب القدر", "तक़दीर की किताब", "2643–2664", 22),
        m(47, "The Book of Knowledge (Al-'Ilm)", "کتاب العلم", "كتاب العلم", "इल्म की किताब", "2665–2674", 10),
        m(48, "Remembrance, Dua, Repentance & Forgiveness", "کتاب الذکر والدعاء والتوبۃ", "كتاب الذكر والدعاء والتوبة والاستغفار", "ज़िक्र, दुआ और इस्तिग़फ़ार", "2675–2735", 61),
        m(49, "Heart-Melting Traditions (Ar-Riqaq)", "کتاب الرقاق", "كتاب الرقاق", "दिल नरम करने वाली बातें", "2736–2743", 8),
        m(50, "The Book of Repentance (At-Tawbah)", "کتاب التوبہ", "كتاب التوبة", "तौबा की किताब", "2744–2771", 28),
        m(51, "Characteristics of Hypocrites", "صفات المنافقین", "كتاب صفات المنافقين وأحكامهم", "मुनाफ़िक़ों के लक्षण", "2772–2784", 13),
        m(52, "Day of Judgment, Paradise & Hell", "صفۃ القیامۃ والجنۃ والنار", "كتاب صفة القيامة والجنة والنار", "क़यामत, जन्नत व जहन्नम", "2785–2821", 37),
        m(53, "Paradise, Its Bounties & Inhabitants", "کتاب الجنۃ وصفۃ نعیمہا", "كتاب الجنة وصفة نعيمها وأهلها", "जन्नत और उसकी नेमतें", "2822–2879", 58),
        m(54, "Tribulations & Portents of Last Hour", "کتاب الفتن واشراط الساعۃ", "كتاب الفتن وأشراط الساعة", "फ़ितने व क़यामत की निशानियाँ", "2880–2955", 76),
        m(55, "Asceticism & Softening of Hearts (Az-Zuhd)", "کتاب الزہد والرقائق", "كتاب الزهد والرقائق", "ज़ुहद व सादगी", "2956–3014", 59),
        m(56, "Commentary on the Qur'an (At-Tafsir)", "کتاب التفسیر", "كتاب التفسير", "क़ुरआन की तफ़सीर", "3015–3033", 19)
    )

    fun booksFor(collection: String): List<HadithBookInfo> =
        if (collection == COLLECTION_MUSLIM) muslim57Books else bukhari97Books

    fun booksInBukhariVolume(volumeNumber: Int): List<HadithBookInfo> =
        bukhari97Books.filter { it.volumeNumber == volumeNumber }

    fun getManifest(collection: String): HadithEditionManifest =
        if (collection == COLLECTION_MUSLIM) muslimManifest else bukhariManifest
}
