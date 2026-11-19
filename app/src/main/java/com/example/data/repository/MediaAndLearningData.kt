package com.example.data.repository

import com.example.data.model.*

object MediaAndLearningData {

    val lectureCategories = listOf(
        "All",
        "Quran Explanation",
        "Hadith Lessons",
        "Islamic History",
        "Seerah of Prophet Muhammad ﷺ",
        "Islamic Character",
        "Daily Islamic Guidance",
        "Family and Social Life",
        "Islamic Education"
    )

    val lectures = listOf(
        Lecture(
            id = "lec_1",
            titleEn = "Finding Peace in Times of Anxiety and Hardship",
            titleUr = "پریشانی اور آزمائش کے لمحات میں سکونِ قلب کی تلاش",
            titleHi = "कठिनाई और चिंता के समय मन की शांति",
            scholar = "Mufti Ismail Menk",
            categoryEn = "Daily Islamic Guidance",
            categoryUr = "روزمرہ اسلامی رہنمائی",
            duration = "28:45",
            videoId = "0l2hHqxS-0M",
            isTrending = true,
            datePublished = "2024-05-12"
        ),
        Lecture(
            id = "lec_2",
            titleEn = "Deep Reflections on the Holy Quran",
            titleUr = "فہم القرآن: قرآنی حکمتوں اور فکری گہرائیوں کا بیان",
            titleHi = "पवित्र क़ुरआन की गहरी समझ और विचार",
            scholar = "Ustadh Nouman Ali Khan",
            categoryEn = "Quran Explanation",
            categoryUr = "فہم القرآن و تفسیر",
            duration = "45:12",
            videoId = "rQcj09k6TIw",
            isTrending = true,
            datePublished = "2024-06-20"
        ),
        Lecture(
            id = "lec_3",
            titleEn = "Haqiqat-e-Iman (The Essence of True Faith)",
            titleUr = "حقیقتِ ایمان اور قرآنی منہجِ انقلاب",
            titleHi = "ईमान की वास्तविकता और क़ुरआनी मार्गदर्शन",
            scholar = "Dr. Israr Ahmed (رحمه الله)",
            categoryEn = "Hadith Lessons",
            categoryUr = "دروسِ حدیث و عقیدہ",
            duration = "52:10",
            videoId = "yKJ05juuxBI",
            isTrending = false,
            datePublished = "Archive Selection"
        ),
        Lecture(
            id = "lec_4",
            titleEn = "Zindagi Khushaal Bananay Ka Nuskha (Prosperous Family Life)",
            titleUr = "گھریلو زندگی کو خوشحال اور پرسکون بنانے کا نسخہ",
            titleHi = "पारिवारिक जीवन को खुशहाल बनाने का तरीक़ा",
            scholar = "Maulana Tariq Jameel",
            categoryEn = "Family and Social Life",
            categoryUr = "خاندانی اور معاشرتی زندگی",
            duration = "36:20",
            videoId = "ZwgrfuSjX_c",
            isTrending = true,
            datePublished = "2024-07-15"
        ),
        Lecture(
            id = "lec_5",
            titleEn = "Lessons from the Prophetic Seerah",
            titleUr = "سیرتِ نبوی ﷺ: مدنی زندگی کے تابناک نقوش اور اسباق",
            titleHi = "मदीना का जीवन: पैगंबर ﷺ की सीरत से सबक",
            scholar = "Dr. Yasir Qadhi",
            categoryEn = "Seerah of Prophet Muhammad ﷺ",
            categoryUr = "سیرتِ نبوی ﷺ",
            duration = "1:02:15",
            videoId = "boocPQ4imHA",
            isTrending = false,
            datePublished = "2024-03-10"
        ),
        Lecture(
            id = "lec_6",
            titleEn = "Dealing with Hardships and Trials",
            titleUr = "صبر، استقامت اور مصائب کا سامنا کیسے کریں؟",
            titleHi = "मुसीबतों और आज़माइशों का सामना कैसे करें?",
            scholar = "Mufti Ismail Menk",
            categoryEn = "Daily Islamic Guidance",
            categoryUr = "روزمرہ اسلامی رہنمائی",
            duration = "22:30",
            videoId = "PYbU9cRrtFc",
            isTrending = true,
            datePublished = "2024-08-01"
        ),
        Lecture(
            id = "lec_7",
            titleEn = "Finding Hope in Times of Darkness",
            titleUr = "مایوسی کے اندھیروں میں قرآنی امید کی کرن",
            titleHi = "निराशा के समय में क़ुरआनी उम्मीद",
            scholar = "Ustadh Nouman Ali Khan",
            categoryEn = "Quran Explanation",
            categoryUr = "فہم القرآن و تفسیر",
            duration = "34:10",
            videoId = "8wJN3GecQY0",
            isTrending = false,
            datePublished = "2024-07-22"
        ),
        Lecture(
            id = "lec_8",
            titleEn = "Maa Baap Ki Azmat Aur Huqooq (Status of Parents)",
            titleUr = "والدین کی عظمت اور اولاد کے فرائض",
            titleHi = "माता-पिता का सम्मान और संतान के कर्तव्य",
            scholar = "Maulana Tariq Jameel",
            categoryEn = "Islamic Character",
            categoryUr = "اخلاق و تربیت",
            duration = "41:50",
            videoId = "M6crlVWXC-k",
            isTrending = true,
            datePublished = "2024-06-05"
        ),
        Lecture(
            id = "lec_9",
            titleEn = "Dawat-e-Quran Aur Inqilabi Fikr",
            titleUr = "دعوتِ قرآن اور امتِ مسلمہ کا مطلوبہ کردار",
            titleHi = "क़ुरआन का संदेश और उम्मत की ज़िम्मेदारी",
            scholar = "Dr. Israr Ahmed (رحمه الله)",
            categoryEn = "Islamic Education",
            categoryUr = "تعلیم و تربیت",
            duration = "48:15",
            videoId = "TVk8m31rmw4",
            isTrending = false,
            datePublished = "Archive Selection"
        )
    )

    val arabicAlphabet = listOf(
        ArabicLetter("ا", "أَلِف", "Alif", "Produced from the emptiness of the mouth and throat (Jawf).", "حلق اور منہ کے خالی حصے (جوف) سے ادا ہوتا ہے۔", "गले और मुँह के खाली हिस्से (जौफ़) से निकलता है।", "ا", "ا", "ـا", "ـا", "أَبٌ", "Father", makhrajAr = "يخرج من الجوف (خلاء الفم والحلق)."),
        ArabicLetter("ب", "بَاء", "Baa", "Produced by closing the wet inner part of both lips firmly.", "دونوں ہونٹوں کے تری والے اندرونی حصے کو آپس میں ملانے سے۔", "दोनों होंठों के भीतरी गीले हिस्से को आपस में मिलाने से।", "ب", "بـ", "ـبـ", "ـب", "بَيْتٌ", "House", makhrajAr = "يخرج من باطن الشفتين بانطباقهما معاً."),
        ArabicLetter("ت", "تَاء", "Taa", "Tip of the tongue touching the roots of the top two incisors.", "زبان کی نوک جب اوپر والے سامنے کے دو دانتوں کی جڑ سے لگے۔", "ज़बान की नोक ऊपर के सामने वाले दो दाँतों की जड़ से लगती है।", "ت", "تـ", "ـتـ", "ـت", "تِينٌ", "Fig", makhrajAr = "يخرج من طرف اللسان مع أصول الثنايا العليا."),
        ArabicLetter("ث", "ثَاء", "Thaa", "Tip of the tongue touching the edges of the top two incisors.", "زبان کے سرے کو اوپر کے سامنے والے دونوں دانتوں کے کناروں سے ملانے سے۔", "ज़बान का सिरा ऊपर के सामने वाले दोनों दाँतों के किनारों से लगता है।", "ث", "ثـ", "ـثـ", "ـث", "ثَمَرٌ", "Fruit", makhrajAr = "يخرج من طرف اللسان مع أطراف الثنايا العليا."),
        ArabicLetter("ج", "جِيم", "Jeem", "Middle of the tongue touching the upper palate.", "زبان کا درمیانی حصہ جب اوپر کے تالو سے لگے۔", "ज़बान का बीच का हिस्सा ऊपर के तालू से लगता है।", "ج", "جـ", "ـجـ", "ـج", "جَنَّةٌ", "Paradise", makhrajAr = "يخرج من وسط اللسان مع ما يحاذيه من الحنك الأعلى."),
        ArabicLetter("ح", "حَاء", "Haa", "Middle of the throat (Wasat al-Halq), clear and aspirated.", "حلق کے درمیانی حصے سے صاف اور کھنکھار کے بغیر ادا ہوتا ہے۔", "गले के बीच के हिस्से से साफ़ और बिना खनखनाहट के निकलता है।", "ح", "حـ", "ـحـ", "ـح", "حَقٌّ", "Truth", makhrajAr = "يخرج من وسط الحلق بوضوح دون بحة."),
        ArabicLetter("خ", "خَاء", "Khaa", "Top of the throat nearest to the mouth (Adna al-Halq).", "حلق کا وہ حصہ جو منہ کی طرف سب سے قریب ہے۔", "गले का वह ऊपरी हिस्सा जो मुँह के सबसे क़रीब है।", "خ", "خـ", "ـخـ", "ـخ", "خَيْرٌ", "Good", makhrajAr = "يخرج من أدنى الحلق مما يلي الفم."),
        ArabicLetter("د", "دَال", "Daal", "Tip of the tongue touching the roots of top incisors (Qalqalah letter).", "زبان کی نوک اوپر کے سامنے والے دانتوں کی جڑ سے لگے (حروفِ قلقلہ میں سے)۔", "ज़बान की नोक ऊपर के दाँतों की जड़ से लगती है (क़लक़ला अक्षर)।", "د", "د", "ـد", "ـد", "دِينٌ", "Religion", makhrajAr = "يخرج من طرف اللسان مع أصول الثنايا العليا (من حروف القلقلة)."),
        ArabicLetter("ر", "رَاء", "Raa", "Tip of the tongue with slight back touching the gum of incisors.", "زبان کی نوک کی پشت جب سامنے کے دانتوں کے مسوڑھے سے لگے۔", "ज़बान की नोक का पिछला हिस्सा सामने के दाँतों के मसूड़ों से लगता है।", "ر", "ر", "ـر", "ـر", "رَحْمَةٌ", "Mercy", makhrajAr = "يخرج من طرف اللسان مع ظهره قليلاً مما يلي لثة الثنايا العليا."),
        ArabicLetter("س", "سِين", "Seen", "Tip of the tongue just above the bottom teeth, making a sharp whistle.", "زبان کا سرا جب نیچے کے سامنے کے دانتوں کے اندرونی کنارے سے لگے۔", "ज़बान का सिरा नीचे के सामने वाले दाँतों के अंदरूनी किनारे से लगता है।", "س", "سـ", "ـسـ", "ـس", "سَلَامٌ", "Peace", makhrajAr = "يخرج من طرف اللسان مع ما بين الثنايا العليا والسفلى بصفير.")
    )

    val tajweedRules = listOf(
        TajweedRule(
            id = "noon_sakinah",
            titleEn = "Rules of Noon Sakinah & Tanween",
            titleUr = "احکامِ نون ساکن و تنوین",
            titleAr = "أحكام النون الساكنة والتنوين",
            titleHi = "नून साकिना और तनवीन के नियम",
            category = "Core Tajweed",
            explanationEn = "Noon Sakinah (نْ) and Tanween (ً ٍ ٌ) have four rules: 1. Izhar (Clear pronunciation without Ghunnah before throat letters: ء ه ع ح غ خ); 2. Idgham (Merging into ي ر م ل و ن); 3. Iqlab (Changing to Meem before ب); 4. Ikhfa (Hiding with nasal sound before the remaining 15 letters).",
            explanationUr = "نون ساکن اور تنوین کے چار بنیادی احکام ہیں: 1. اظہار (حروفِ حلقی کے سامنے بغیر غنہ کے ظاہر کرنا)؛ 2. ادغام (یرملون کے حروف میں ملانا)؛ 3. اقلاب (ب کے سامنے نون کو میم سے بدلنا)؛ 4. اخفاء (باقی 15 حروف کے سامنے ناک میں ہلکی آواز چھپانا)۔",
            explanationHi = "नून साकिना और तनवीन के 4 नियम हैं: इज़हार (हलक़ी अक्षरों से पहले बिना ग़ुन्ना पढ़ना), इदग़ाम (मिलाकर पढ़ना), इक़्लाब (नून को मीम में बदलना), और इख़्फ़ा (नाक में हल्की आवाज़ छिपाना)।",
            explanationAr = "للنون الساكنة والتنوين أربعة أحكام: ١. الإظهار الحلقي عند الحروف الستة (ء هـ ع ح غ خ)؛ ٢. الإدغام في حروف (يرملون)؛ ٣. الإقلاب إلى ميم مخفاة عند الباء؛ ٤. الإخفاء الحقيقي بغنة عند باقي الحروف الخمسة عشر.",
            examples = listOf(
                TajweedExample("مِنْ حَكِيمٍ", "Surah Fussilat: 42", "Izhar: Noon Sakinah before letter Haa (ح)", "اظہار: نون ساکن حروفِ حلقی 'ح' سے پہلے", "إظهار: نون ساكنة قبل حرف الحاء", "इज़हार: ह (ح) से पहले नून साकिना"),
                TajweedExample("مَن يَقُولُ", "Surah Al-Baqarah: 8", "Idgham with Ghunnah: Noon Sakinah before Yaa (ي)", "ادغام مع الغنہ: نون ساکن حرف 'ی' سے پہلے", "إدغام بغنة: نون ساكنة قبل حرف الياء", "इदग़ाम ग़ुन्ना के साथ: य (ي) से पहले"),
                TajweedExample("مِن بَعْدِ", "Surah Al-Baqarah: 27", "Iqlab: Noon Sakinah changed to Meem before Baa (ب)", "اقلاب: نون ساکن 'ب' سے پہلے میم میں تبدیل", "إقلاب: قلب النون الساكنة ميماً قبل الباء", "इक़्लाब: ब (ب) से पहले नून मीम में बदला"),
                TajweedExample("مِن قَبْلِ", "Surah Al-Baqarah: 25", "Ikhfa: Noon Sakinah hidden with nasalization before Qaaf (ق)", "اخفاء: نون ساکن 'ق' سے پہلے غنہ کے ساتھ پوشیدہ", "إخفاء: نون ساكنة مخفاة بغنة قبل القاف", "इख़्फ़ा: क़ (ق) से पहले नून ग़ुन्ना के साथ")
            )
        ),
        TajweedRule(
            id = "qalqalah",
            titleEn = "Rule of Qalqalah (Echoing / Bouncing)",
            titleUr = "قاعدۂ قلقلہ (گونجدار آواز)",
            titleAr = "أحكام القلقلة",
            titleHi = "क़लक़ला का नियम (गूंजती आवाज़)",
            category = "Pronunciation",
            explanationEn = "When any of the five letters of Qutb Jadd (ق ط ب ج د) carries a Sukun (or you stop upon it), the articulation point is shaken to produce a sharp echoing bounce.",
            explanationUr = "جب حروفِ قلقلہ (ق، ط، ب، ج، د - قطب جد) پر جزم (سکون) ہو یا ان پر وقف کیا جائے، تو مخرج سے جھٹکے کے ساتھ گونج پیدا کی جاتی ہے۔",
            explanationHi = "जब पाँच अक्षरों (क़ुतब जद्द: क़, त़, ब, ज, द) पर सुकूं हो या वक़्फ़ किया जाए, तो मख़रज से हल्की गूंजती आवाज़ निकाली जाती है।",
            explanationAr = "القلقلة هي اضطراب الصوت في مخرج أحد حروف (قطب جد: ق ط ب ج د) عند سكونه سكوناً أصلياً أو عارضاً للوقف حتى يُسمع له نبرة قوية.",
            examples = listOf(
                TajweedExample("قُلْ هُوَ اللَّهُ أَحَدٌ ۝", "Surah Al-Ikhlas: 1", "Major Qalqalah at stop on letter Daal (د)", "قلقلۂ کبریٰ: وقف پر حرف 'د' کی گونج", "قلقلة كبرى عند الوقف على الدال", "बड़ा क़लक़ला: द (د) पर रुकते समय"),
                TajweedExample("مِن فَلَقٍ ۝", "Surah Al-Falaq: 1", "Major Qalqalah at stop on letter Qaaf (ق)", "قلقلۂ کبریٰ: وقف پر حرف 'ق' کی گونج", "قلقلة كبرى عند الوقف على القاف", "बड़ा क़लक़ला: क़ (ق) पर रुकते समय"),
                TajweedExample("فِي حَبْلٍ", "Surah Al-Masad: 5", "Minor Qalqalah in middle on letter Baa (ب)", "قلقلۂ صغریٰ: کلمہ کے وسط میں حرف 'ب'", "قلقلة صغرى على الباء الساكنة في وسط الكلمة", "छोटा क़लक़ला: बीच में ब (ب) पर")
            )
        )
    )

    fun calculateZakat(input: ZakatCalculationInput): ZakatResult {
        val result = com.example.data.zakat.ZakatCalculationEngine.calculate(
            com.example.data.zakat.GlobalZakatInput(
                currencyCode = input.currency,
                cashAtHome = java.math.BigDecimal.valueOf(input.cashOnHand),
                currentAccounts = java.math.BigDecimal.valueOf(input.bankBalance),
                goldGrams = java.math.BigDecimal.valueOf(input.goldGrams),
                gold24kPricePerGram = if (input.goldGramPrice > 0) java.math.BigDecimal.valueOf(input.goldGramPrice) else null,
                silverGrams = java.math.BigDecimal.valueOf(input.silverGrams),
                silverPurePricePerGram = if (input.silverGramPrice > 0) java.math.BigDecimal.valueOf(input.silverGramPrice) else null,
                resaleInventory = java.math.BigDecimal.valueOf(input.businessInventory),
                strongReceivables = java.math.BigDecimal.valueOf(input.receivables),
                sharesMarketValue = java.math.BigDecimal.valueOf(input.otherInvestments),
                overdueDebts = java.math.BigDecimal.valueOf(input.debtsAndLiabilities),
                nisabType = input.selectedNisabType,
                hawlConfirmedByUser = true
            )
        )
        return ZakatResult(
            totalAssets = result.totalQualifyingAssets.toDouble(),
            deductibleLiabilities = result.eligibleDeductibleLiabilities.toDouble(),
            netZakatableWealth = result.netZakatableWealth.toDouble(),
            nisabThreshold = (result.nisabThreshold ?: java.math.BigDecimal.ZERO).toDouble(),
            isEligible = result.isEligible,
            zakatPayable = result.zakatPayable.toDouble(),
            currency = result.currencyCode
        )
    }
}
