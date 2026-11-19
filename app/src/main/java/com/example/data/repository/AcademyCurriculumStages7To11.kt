package com.example.data.repository

import com.example.data.model.AcademyLesson
import com.example.data.model.AcademyStage
import com.example.data.model.HifzTarget
import com.example.data.model.IslamicReference
import com.example.data.model.QaidaItem
import com.example.data.model.QaidaLesson
import com.example.data.model.QuranicWord
import com.example.data.model.StageToolType
import com.example.data.model.SurahExegesis
import com.example.data.model.WordByWordAyah
import com.example.data.model.WordPart

object AcademyCurriculumStages7To11 {

    // Extended Noorani Qaida (Lessons 9 to 15) added after the core 8 in QuranLearningRepository
    val extraQaidaLessons: List<QaidaLesson> = listOf(
        QaidaLesson(
            id = "qaida_9",
            lessonNumber = 9,
            titleEn = "Lesson 9: Al-Huroof Al-Muqatta'at (Disjoined Opening Letters)",
            titleUr = "سبق نمبر 9: حروفِ مقطعات (سورتوں کے ابتدائی حروف)",
            titleAr = "الدرس التاسع: الحروف المقطعة في أوائل السور",
            titleHi = "सबक़ 9: हुरूफ़-ए-मुक़त्तआत (सूरतों के शुरुआती अक्षर)",
            descriptionEn = "Read disconnected opening letters in the Quran by their exact letter names with proper 2- or 6-count Madd elongation.",
            descriptionUr = "سورتوں کے شروع میں آنے والے حروفِ مقطعات کو ان کے اصل ناموں اور مد کی صحیح مقدار کے ساتھ پڑھنے کی مشق۔",
            descriptionAr = "نطق الحروف المقطعة بأسمائها مع تطبيق المد اللازم الحرفي والمد الطبيعي الحرفي.",
            descriptionHi = "सूरतों की शुरुआत में आने वाले अलग-अलग अक्षरों को उनके नाम और सही मद की लंबाई के साथ पढ़ना।",
            items = listOf(
                QaidaItem("الم", "Alif · Laaam · Meeem", "Alif (no Madd), Laam (6 counts), Meem (6 counts)", "الف بغیر مد، لام 6 حرکات، میم 6 حرکات", "ألف بلا مد، لام ست حركات، ميم ست حركات", "अलिफ़ (बिना मद), लाम (6 हरकत), मीम (6 हरकत)"),
                QaidaItem("حم", "Haa · Meeem", "Haa (2 counts), Meem (6 counts)", "حا 2 حرکات، میم 6 حرکات", "حاء حركتان، ميم ست حركات", "हा (2 हरकत), मीम (6 हरकत)"),
                QaidaItem("يس", "Yaa · Seeen", "Yaa (2 counts), Seen (6 counts)", "یا 2 حرکات، سین 6 حرکات", "ياء حركتان، سين ست حركات", "या (2 हरकत), सीन (6 हरकत)"),
                QaidaItem("طه", "Taa · Haa", "Both elongated naturally for 2 counts", "طا اور ہا دونوں 2، 2 حرکات", "طاء وهاء حركتان لكل منهما", "ता और हा दोनों 2-2 हरकत")
            )
        ),
        QaidaLesson(
            id = "qaida_10",
            lessonNumber = 10,
            titleEn = "Lesson 10: Standing Vowels & Inverted Damma (Khari Harakaat)",
            titleUr = "سبق نمبر 10: کھڑی حرکات (کھڑا زبر، کھڑی زیر، الٹا پیش)",
            titleAr = "الدرس العاشر: الحركات الطويلة (الألف والياء والواو الصغرى)",
            titleHi = "सबक़ 10: खड़ी हरकतें (खड़ा ज़बर, खड़ी ज़ेर, उल्टा पेश)",
            descriptionEn = "Standing Fatha, standing Kasra, and inverted Damma equal 2 counts—just like Alif, Yaa, and Waw Maddah.",
            descriptionUr = "کھڑا زبر، کھڑی زیر اور الٹا پیش دو حرکات کے برابر لمبے کر کے پڑھے جاتے ہیں۔",
            descriptionAr = "الألف الخنجرية والياء والواو الصغرى تُمد بمقدار حركتين كحروف المد الطبيعي.",
            descriptionHi = "खड़ा ज़बर, खड़ी ज़ेर और उल्टा पेश को 2 हरकत तक खींचकर पढ़ा जाता है।",
            items = listOf(
                QaidaItem("رَحْمَٰن", "Rah-maa-ni", "Standing Fatha on Meem (2 counts)", "میم پر کھڑا زبر (2 حرکات)", "ألف صغيرة بعد الميم", "मीम पर खड़ा ज़बर"),
                QaidaItem("إِيلَٰفِهِمْ", "Ee-laa-fi-him", "Standing Fatha on Laam (2 counts)", "لام پر کھڑا زبر", "ألف خنجرية على اللام", "लाम पर खड़ा ज़बर"),
                QaidaItem("بِهِۦ", "Bi-heee", "Inverted Kasra / Yaa Silah (2 counts)", "ہا کے نیچے الٹی زیر / یائے صلہ", "ياء صلة صغرى", "हा के नीचे छोटी या (2 हरकत)"),
                QaidaItem("لَهُۥ", "La-hooo", "Small Waw Silah (2 counts)", "الٹا پیش / واؤ صلہ (2 حرکات)", "واو صلة صغرى", "उल्टा पेश (2 हरकत)")
            )
        ),
        QaidaLesson(
            id = "qaida_11",
            lessonNumber = 11,
            titleEn = "Lesson 11: Huroof-e-Leen (Soft Diphthongs: وَ / يْ)",
            titleUr = "سبق نمبر 11: حروفِ لین (واؤ لین اور یائے لین)",
            titleAr = "الدرس الحادي عشر: حرفا اللين (الواو والياء الساكنتان المفتوح ما قبلهما)",
            titleHi = "सबक़ 11: हुरूफ़-ए-लीन (नरम आवाज़)",
            descriptionEn = "Waw Sakinah or Yaa Sakinah preceded by a Fatha are pronounced smoothly and softly without jerking.",
            descriptionUr = "واؤ ساکن یا یائے ساکن سے پہلے زبر ہو تو انہیں بغیر جھٹکے نرمی سے ادا کیا جاتا ہے۔",
            descriptionAr = "الواو والياء الساكنتان بعد فتح تُنطقان بليونة ويسر دون غلظة.",
            descriptionHi = "वाव साकिन या या साकिन से पहले ज़बर आए तो बिना झटके नर्मी से पढ़ा जाता है।",
            items = listOf(
                QaidaItem("خَوْفٍ", "Khawf", "Fatha followed by Waw Sakinah", "خا پر زبر اور اس کے بعد واؤ ساکن", "واو لينة بعد فتح", "ख़ौफ़"),
                QaidaItem("بَيْتٍ", "Bayt", "Fatha followed by Yaa Sakinah", "با پر زبر اور اس کے بعد یائے ساکن", "ياء لينة بعد فتح", "बैत"),
                QaidaItem("قُرَيْشٍ", "Quraysh", "Raa with Fatha followed by Yaa Sakinah", "را پر زبر کے بعد یائے لین", "ياء لينة في قريش", "क़ुरैश"),
                QaidaItem("يَوْمَ", "Yaw-ma", "Yaa with Fatha followed by Waw Sakinah", "یا پر زبر کے بعد واؤ لین", "واو لينة في يوم", "यौम")
            )
        ),
        QaidaLesson(
            id = "qaida_12",
            lessonNumber = 12,
            titleEn = "Lesson 12: Heavy vs Light Letters (Tafkheem & Tarqeeq)",
            titleUr = "سبق نمبر 12: پر (موٹے) اور باریک حروف (تفخیم و ترقیق)",
            titleAr = "الدرس الثاني عشر: التفخيم والترقيق (حروف الاستعلاء)",
            titleHi = "सबक़ 12: भारी और हल्के अक्षर (तफ़ख़ीम व तरक़ीक़)",
            descriptionEn = "Always pronounce the 7 elevated letters (خ ص ض غ ط ق ظ) with a full heavy mouth; keep others light unless Raa/Laam conditions apply.",
            descriptionUr = "سات حروفِ مستعلیہ (خ ص ض غ ط ق ظ) ہمیشہ منہ بھر کر موٹے پڑھے جاتے ہیں، جبکہ باقی حروف اصل میں باریک ہیں۔",
            descriptionAr = "حروف 'خص ضغط قظ' السبعة مفخمة دائماً، بينما بقية الحروف مرققة عدا الراء واللام في أحوال محددة.",
            descriptionHi = "सात अक्षर (ख़, स़, द़, ग़, त़, क़, ज़़) हमेशा भारी पढ़े जाते हैं; र और लाम परिस्थितियों के अनुसार भारी/हल्के होते हैं।",
            items = listOf(
                QaidaItem("خ ص ض غ ط ق ظ", "Khussa Daghtin Qiz", "7 always-heavy letters", "ہمیشہ موٹے پڑھے جانے والے 7 حروف", "حروف الاستعلاء السبعة", "हमेशा भारी 7 अक्षर"),
                QaidaItem("رَبِّ / رِزْقًا", "Rabbi (Heavy) vs Rizqan (Light)", "Raa is heavy with Fatha/Damma, light with Kasra", "را زبر/پیش پر موٹی اور زیر پر باریک", "تفخيم الراء المفتوحة وترقيق المكسورة", "ज़बर पर भारी र, ज़ेर पर हल्की र"),
                QaidaItem("هُوَ اللَّهُ / بِسْمِ اللَّهِ", "Allahu (Heavy Laam) vs Bismillahi (Light Laam)", "Laam in Allah is heavy after Fatha/Damma, light after Kasra", "لفظ اللہ کا لام زبر/پیش کے بعد موٹا اور زیر کے بعد باریک", "تغليظ لام لفظ الجلالة وترقيقها", "अल्लाह का लाम नियम")
            )
        ),
        QaidaLesson(
            id = "qaida_13",
            lessonNumber = 13,
            titleEn = "Lesson 13: Applying Noon & Meem Sakinah + Ghunnah",
            titleUr = "سبق نمبر 13: نون و میم ساکن اور غنہ کا عملی اطلاق",
            titleAr = "الدرس الثالث عشر: تطبيق أحكام النون والميم الساكنتين والغنة",
            titleHi = "सबक़ 13: नून व मीम साकिना और ग़ुन्ना का अभ्यास",
            descriptionEn = "Distinguish clear Izhar, nasal Ikhfa, merging Idgham, and converting Iqlab accurately inside real Quranic phrases.",
            descriptionUr = "قرآنی الفاظ میں اظہار، ادغام، اقلاب اور اخفاء کے ساتھ ناک میں غنہ کی صحیح ادائیگی کی مشق۔",
            descriptionAr = "التمييز العملي بين الإظهار والإدغام والإقلاب والإخفاء في الأمثلة القرآنية.",
            descriptionHi = "क़ुरआनी शब्दों में इज़हार, इदग़ाम, इक़्लाब और इख़्फ़ा का व्यावहारिक अभ्यास।",
            items = listOf(
                QaidaItem("مِنْ خَوْفٍ", "Min Khawf (Izhar)", "Clear Noon without extra nasal hold", "بغیر غنہ صاف نون", "إظهار حلقي", "साफ़ नून (इज़हार)"),
                QaidaItem("فَمَن يَعْمَلْ", "Famay-ya'mal (Idgham)", "Merge Noon into Yaa with 2-count Ghunnah", "نون کو یا میں غنہ کے ساتھ ملائیں", "إدغام بغنة", "मिलाकर ग़ुन्ना (इदग़ाम)"),
                QaidaItem("لَيُنبَذَنَّ", "Layumbadhanna (Iqlab)", "Noon turns into hidden Meem before Baa", "با سے پہلے نون کو میم میں بدلیں", "إقلاب مع الغنة", "नून को मीम में बदलना (इक़्लाब)"),
                QaidaItem("أَنتُمْ", "Antum (Ikhfa)", "Hide Noon smoothly in nasal passage before Taa", "تا سے پہلے اخفاء اور غنہ", "إخفاء حقيقي", "नाक में ग़ुन्ना (इख़्फ़ा)")
            )
        ),
        QaidaLesson(
            id = "qaida_14",
            lessonNumber = 14,
            titleEn = "Lesson 14: Joining Phrases & Waqf (Stopping on Ayah Ends)",
            titleUr = "سبق نمبر 14: جملے جوڑنا اور آیت کے آخر پر وقف کا طریقہ",
            titleAr = "الدرس الرابع عشر: وصل الكلمات وأحكام الوقف على أواخر الآي",
            titleHi = "सबक़ 14: वाक्य जोड़ना और आयत के अंत पर रुकना (वक़्फ़)",
            descriptionEn = "How short vowels turn into Sukoon upon stopping, Tanween Fatha turns into Alif Maddah, and Taa Marbutah turns into soft Haa.",
            descriptionUr = "وقف کرتے وقت آخری زبر، زیر، پیش ساکن ہو جاتے ہیں، دو زبر الف میں بدلتے ہیں اور گول 'ۃ' ساکن 'ہ' بن جاتی ہے۔",
            descriptionAr = "السكون العارض عند الوقف، ومد العوض في التنوين بالفتح، وإبدال تاء التأنيث المربوطة هاءً ساكنة.",
            descriptionHi = "रुकते समय अंतिम हरकत सुकूं बन जाती है, दो ज़बर अलिफ़ बन जाता है और गोल ता (ة) 'ह' साकिन पढ़ी जाती है।",
            items = listOf(
                QaidaItem("نَسْتَعِينُ ← نَسْتَعِينْ", "Nasta'eenu -> Nasta'een", "Drop final vowel to Sukoon at stop", "وقف پر آخری پیش ساکن ہو گیا", "تسكين الآخر عند الوقف", "रुकने पर पेश सुकूं बन गया"),
                QaidaItem("عَلِيمًا ← عَلِيمَا", "'Aleeman -> 'Aleemaa", "Tanween Fatha becomes 2-count Alif at stop (Madd Iwad)", "دو زبر وقف پر الف بن گیا", "مد العوض عند الوقف", "दो ज़बर अलिफ़ बन गया"),
                QaidaItem("قَارِعَةٌ ← قَارِعَهْ", "Qaari'atun -> Qaari'ah", "Round Taa (ة) sounds like soft Haa at stop", "گول تا وقف پر ہا پڑھی جاتی ہے", "الوقف على التاء المربوطة بالهاء", "गोल ता रुकने पर 'ह' बन जाती है")
            )
        ),
        QaidaLesson(
            id = "qaida_15",
            lessonNumber = 15,
            titleEn = "Lesson 15: Completing Short Surahs & Transitioning to the Mushaf",
            titleUr = "سبق نمبر 15: مختصر سورتوں کی تکمیل اور مصحفِ قرآن کا آغاز",
            titleAr = "الدرس الخامس عشر: تلاوة قصار السور والانتقال للمصحف الشريف",
            titleHi = "सबक़ 15: छोटी सूरतों का पाठ और संपूर्ण क़ुरआन पढ़ने की शुरुआत",
            descriptionEn = "Congratulations! Combine every Qaida & Tajweed rule smoothly across Surahs Al-Ikhlas, Al-Falaq, An-Nas, and step confidently into the full Mushaf.",
            descriptionUr = "قاعدہ و تجوید کے تمام اصولوں کے ساتھ سورۃ الإخلاص، الفلق اور الناس کی مکمل تلاوت کر کے قرآن پاک کے باقاعدہ ناظرے کا آغاز کریں۔",
            descriptionAr = "تطبيق متكامل لقواعد التجويد في قصار السور تمهيداً لختم المصحف الشريف بإتقان.",
            descriptionHi = "सभी क़ायदा व तजवीद नियमों के साथ सूरह इख़्लास, फ़लक़ और नास पढ़कर संपूर्ण क़ुरआन पढ़ने की शुरुआत करें।",
            items = listOf(
                QaidaItem("قُلْ هُوَ اللَّهُ أَحَدٌ ۝", "Qul Huwa Allahu Ahad", "Heavy Laam in Allah + Qalqalah Kubra on Daal", "لفظ اللہ کا موٹا لام + دال پر قلقلہ کبریٰ", "تفخيم لام الجلالة وقلقلة الدال", "अल्लाह का भारी लाम + द पर क़लक़ला"),
                QaidaItem("قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝", "Qul A'udhu bi-Rabbil-Falaq", "Clear Ayn + Qalqalah on Qaaf at stop", "عین صاف + قاف پر وقف میں قلقلہ", "تحقيق العين وقلقلة القاف", "ऐन का स्पष्ट उच्चारण + क़ाफ़ पर क़लक़ला"),
                QaidaItem("مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۝", "Min sharril-waswasil-khannas", "Ikhfa on Noon + Madd 'Arid lis-Sukun at stop", "نون پر اخفاء + وقف پر مدِ عارض للسكون", "إخفاء حقيقي ومد عارض للسكون", "नून पर इख़्फ़ा + वक़्फ़ पर मद")
            )
        )
    )

    val extraVocabularyWords: List<QuranicWord> = listOf(
        QuranicWord("voc_16", "قَلْب", "Qalb", "Heart", "دل", "القلب", "दिल / हृदय", 132, "Noun", "إِلَّا مَنْ أَتَى اللَّهَ بِقَلْبٍ سَلِيمٍ", "Ash-Shu'ara: 89"),
        QuranicWord("voc_17", "صَبْر", "Sabr", "Patience & Perseverance", "صبر و استقامت", "الصبر والثبات", "धैर्य / सब्र", 103, "Noun", "إِنَّ اللَّهَ مَعَ الصَّابِرِينَ", "Al-Baqarah: 153"),
        QuranicWord("voc_18", "هُدًى", "Hudan", "Guidance", "ہدایت / رہنمائی", "الهداية والرشاد", "मार्गदर्शन / हिदायत", 85, "Noun", "هُدًى لِّلْمُتَّقِينَ", "Al-Baqarah: 2"),
        QuranicWord("voc_19", "رَحْمَة", "Rahmah", "Mercy & Compassion", "رحمت و مہربانی", "الرحمة والرأفة", "दया / रहमत", 114, "Noun", "وَرَحْمَتِي وَسِعَتْ كُلَّ شَيْءٍ", "Al-A'raf: 156"),
        QuranicWord("voc_20", "عِلْم", "'Ilm", "Knowledge", "علم و آگاہی", "العلم والمعرفة", "ज्ञान / इल्म", 105, "Noun", "وَقُل رَّبِّ زِدْنِي عِلْمًا", "Taha: 114"),
        QuranicWord("voc_21", "حَقّ", "Haqq", "Truth / Right / Justice", "سچ / حق / انصاف", "الحق والعدل", "सत्य / हक़", 287, "Noun", "وَتَوَاصَوْا بِالْحَقِّ", "Al-Asr: 3"),
        QuranicWord("voc_22", "نَفْس", "Nafs", "Soul / Self", "جان / نفس", "النفس والروح", "आत्मा / स्वयं", 295, "Noun", "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا", "Al-Baqarah: 286"),
        QuranicWord("voc_23", "أَرْض", "Ard", "Earth / Land", "زمین", "الأرض", "धरती / ज़मीन", 461, "Noun", "الَّذِي جَعَلَ لَكُمُ الْأَرْضَ فِرَاشًا", "Al-Baqarah: 22"),
        QuranicWord("voc_24", "سَمَاء", "Sama'", "Sky / Heaven", "آسمان", "السماء", "आसमान / आकाश", 310, "Noun", "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ", "An-Nur: 35"),
        QuranicWord("voc_25", "شُكْر", "Shukr", "Gratitude / Thankfulness", "شکرگزاری", "الشكر والحمد", "कृतज्ञता / शुक्र", 75, "Noun", "لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ", "Ibrahim: 7")
    )

    // Verified Word-by-Word Ayahs for Surahs 103, 108, 112, 113, 114 (21 ayahs, complementing all 7 of Surah Al-Fatihah -> 28 total!)
    val wordByWordShortSurahs: List<WordByWordAyah> = listOf(
        // Surah 103 Al-Asr (3 Ayahs)
        WordByWordAyah(
            surahNumber = 103, ayahNumber = 1,
            fullArabic = "وَالْعَصْرِ",
            translationEn = "By time,",
            translationUr = "عصر (گزرتے زمانے) کی قسم،",
            translationHi = "गवाह है गुज़रता समय,",
            words = listOf(
                WordPart("وَ", "Wa", "By (oath)", "قسم ہے", "واو القسم", "क़सम है"),
                WordPart("الْعَصْرِ", "Al-'Asri", "Time / The Passing Age", "زمانے / وقت کی", "الزمن والدهر", "गुज़रते समय की")
            )
        ),
        WordByWordAyah(
            surahNumber = 103, ayahNumber = 2,
            fullArabic = "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ",
            translationEn = "Indeed, mankind is in loss,",
            translationUr = "کہ بے شک انسان خسارے (نقصان) میں ہے،",
            translationHi = "कि वास्तव में मनुष्य घाटे में है,",
            words = listOf(
                WordPart("إِنَّ", "Inna", "Indeed", "بے شک", "حرف توكيد", "निश्चय ही"),
                WordPart("الْإِنسَانَ", "Al-Insaana", "mankind", "انسان", "البشر", "मनुष्य"),
                WordPart("لَفِي", "Lafee", "is surely in", "یقیناً ... میں ہے", "في", "निश्चय ही ... में है"),
                WordPart("خُسْرٍ", "Khusrin", "loss", "خسارہ / نقصان", "هلاك ونقصان", "घाटे / नुकसान")
            )
        ),
        WordByWordAyah(
            surahNumber = 103, ayahNumber = 3,
            fullArabic = "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ",
            translationEn = "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.",
            translationUr = "سوائے ان لوگوں کے جو ایمان لائے اور نیک عمل کیے اور آپس میں حق کی وصیت اور صبر کی تلقین کرتے رہے۔",
            translationHi = "सिवाय उन लोगों के जो ईमान लाए और अच्छे कर्म किए और एक-दूसरे को सत्य और धैर्य की ताकीद की।",
            words = listOf(
                WordPart("إِلَّا الَّذِينَ", "Illal-ladheena", "Except those who", "سوائے ان کے جو", "إلا الذين", "सिवाय उनके जो"),
                WordPart("آمَنُوا", "Aamanoo", "believed", "ایمان لائے", "صدقوا بالله", "ईमान लाए"),
                WordPart("وَعَمِلُوا الصَّالِحَاتِ", "Wa 'amilus-saalihaat", "and did righteous deeds", "اور نیک عمل کیے", "الأعمال الصالحة", "और अच्छे कर्म किए"),
                WordPart("وَتَوَاصَوْا بِالْحَقِّ", "Wa tawaasaw bil-haqq", "and advised each other to truth", "اور حق کی نصیحت کی", "أوصى بعضهم بالحق", "और सत्य की सीख दी"),
                WordPart("بِالصَّبْرِ", "Bis-sabr", "to patience", "اور صبر کی", "الثبات والصبر", "और धैर्य की")
            )
        ),
        // Surah 108 Al-Kawthar (3 Ayahs)
        WordByWordAyah(
            surahNumber = 108, ayahNumber = 1,
            fullArabic = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ",
            translationEn = "Indeed, We have granted you, [O Muhammad], al-Kawthar.",
            translationUr = "بے شک ہم نے آپ ﷺ کو کوثر (خیرِ کثیر و حوضِ کوثر) عطا فرمایا ہے۔",
            translationHi = "निश्चय ही हमने तुम्हें कौसर (अत्यधिक भलाई) प्रदान किया है।",
            words = listOf(
                WordPart("إِنَّا", "Innaa", "Indeed We", "بے شک ہم نے", "إنا", "निश्चय ही हमने"),
                WordPart("أَعْطَيْنَاكَ", "A'taynaaka", "have granted you", "آپ کو عطا فرمایا", "منحناك", "तुम्हें प्रदान किया"),
                WordPart("الْكَوْثَرَ", "Al-Kawthar", "abundance / Al-Kawthar", "کوثر / خیرِ کثیر", "نهر الكوثر والخير الكثير", "कौसर (अत्यधिक भलाई)")
            )
        ),
        WordByWordAyah(
            surahNumber = 108, ayahNumber = 2,
            fullArabic = "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
            translationEn = "So pray to your Lord and sacrifice [to Him alone].",
            translationUr = "پس اپنے رب ہی کے لیے نماز پڑھیں اور قربانی کریں۔",
            translationHi = "अतः अपने रब ही के लिए नमाज़ पढ़ो और क़ुरबानी करो।",
            words = listOf(
                WordPart("فَصَلِّ", "Fasalli", "So pray", "پس نماز پڑھیں", "فأقم الصلاة", "अतः नमाज़ पढ़ो"),
                WordPart("لِرَبِّكَ", "Li-rabbika", "to your Lord", "اپنے رب کے لیے", "لربك وحده", "अपने रब के लिए"),
                WordPart("وَانْحَرْ", "Wanhar", "and sacrifice", "اور قربانی کریں", "واذبح النسك له", "और क़ुरबानी करो")
            )
        ),
        WordByWordAyah(
            surahNumber = 108, ayahNumber = 3,
            fullArabic = "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
            translationEn = "Indeed, your enemy is the one cut off.",
            translationUr = "یقیناً آپ کا دشمن ہی بے نام و نشان (جڑ کٹا) ہے۔",
            translationHi = "निस्संदेह तुम्हारा वैरी ही जड़कटा (नाम-निशान से कटा) है।",
            words = listOf(
                WordPart("إِنَّ", "Inna", "Indeed", "بے شک", "إن", "निश्चय ही"),
                WordPart("شَانِئَكَ", "Shaani'aka", "your enemy / hater", "آپ کا دشمن", "مبغضك", "तुम्हारा दुश्मन"),
                WordPart("هُوَ الْأَبْتَرُ", "Huwal-abtar", "is the one cut off", "وہی بے نام و نشان ہے", "المنقطع عن كل خير", "वही नाम-निशान से कटा है")
            )
        ),
        // Surah 112 Al-Ikhlas (4 Ayahs)
        WordByWordAyah(
            surahNumber = 112, ayahNumber = 1,
            fullArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ",
            translationEn = "Say, \"He is Allah, [who is] One,",
            translationUr = "کہہ دیجیے: وہ اللہ ایک ہے۔",
            translationHi = "कहो, \"वह अल्लाह एक (अकेला) है,",
            words = listOf(
                WordPart("قُلْ", "Qul", "Say", "کہہ دیجیے", "قل", "कहो"),
                WordPart("هُوَ", "Huwa", "He", "وہ", "هو", "वह"),
                WordPart("اللَّهُ", "Allahu", "is Allah", "اللہ ہے", "الله", "अल्लाह"),
                WordPart("أَحَدٌ", "Ahad", "the One & Only", "اکیلا / یکتا", "الواحد المتفرد", "एक / अकेला")
            )
        ),
        WordByWordAyah(
            surahNumber = 112, ayahNumber = 2,
            fullArabic = "اللَّهُ الصَّمَدُ",
            translationEn = "Allah, the Eternal Refuge.",
            translationUr = "اللہ بے نیاز (سب کا سہارا) ہے۔",
            translationHi = "अल्लाह निरपेक्ष और सर्वाधार है,",
            words = listOf(
                WordPart("اللَّهُ", "Allahu", "Allah", "اللہ", "الله", "अल्लाह"),
                WordPart("الصَّمَدُ", "As-Samad", "Self-Sufficient / Eternal Refuge", "بے نیاز، سب اس کے محتاج", "المقصود في الحوائج", "स्वयं-निर्भर / सबका सहारा")
            )
        ),
        WordByWordAyah(
            surahNumber = 112, ayahNumber = 3,
            fullArabic = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
            translationEn = "He neither begets nor is born,",
            translationUr = "نہ اس نے کسی کو جنا اور نہ وہ جنا گیا،",
            translationHi = "न उसकी कोई संतान है और न वह किसी से जन्मा,",
            words = listOf(
                WordPart("لَمْ يَلِدْ", "Lam yalid", "He begets not", "نہ اس کی کوئی اولاد ہے", "ليس له ولد", "उसकी कोई संतान नहीं"),
                WordPart("وَلَمْ يُولَدْ", "Wa lam yoolad", "nor was He born", "اور نہ وہ کسی سے پیدا ہوا", "ليس له والد", "और न वह किसी से जन्मा")
            )
        ),
        WordByWordAyah(
            surahNumber = 112, ayahNumber = 4,
            fullArabic = "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            translationEn = "Nor is there to Him any equivalent.\"",
            translationUr = "اور کوئی اس کے ہمسر (برابر) نہیں۔",
            translationHi = "और न कोई उसके बराबर (समकक्ष) है।\"",
            words = listOf(
                WordPart("وَلَمْ يَكُن", "Wa lam yakun", "And there is not", "اور نہیں ہے", "وليس", "और नहीं है"),
                WordPart("لَّهُ", "Lahu", "to Him", "اس کے لیے", "له", "उसके"),
                WordPart("كُفُوًا", "Kufuwan", "equal / comparable", "ہمسر / برابر", "مثيل ونظير", "बराबर / समकक्ष"),
                WordPart("أَحَدٌ", "Ahad", "anyone", "کوئی ایک بھی", "أحد أبداً", "कोई भी")
            )
        ),
        // Surah 113 Al-Falaq (5 Ayahs)
        WordByWordAyah(
            surahNumber = 113, ayahNumber = 1,
            fullArabic = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
            translationEn = "Say, \"I seek refuge in the Lord of daybreak",
            translationUr = "کہہ دیجیے: میں صبح کے رب کی پناہ مانگتا ہوں،",
            translationHi = "कहो, \"मैं भोर (सुबह) के रब की शरण लेता हूँ,",
            words = listOf(
                WordPart("قُلْ", "Qul", "Say", "کہو", "قل", "कहो"),
                WordPart("أَعُوذُ", "A'oodhu", "I seek refuge", "میں پناہ مانگتا ہوں", "ألتجئ وأحتمي", "मैं शरण लेता हूँ"),
                WordPart("بِرَبِّ", "Bi-rabbi", "in the Lord of", "رب کی", "برب", "रब की"),
                WordPart("الْفَلَقِ", "Al-Falaq", "the daybreak", "صبح / روشنی پھاڑنے والے", "الصبح والخلق", "सुबह / भोर के")
            )
        ),
        WordByWordAyah(
            surahNumber = 113, ayahNumber = 2,
            fullArabic = "مِن شَرِّ مَا خَلَقَ",
            translationEn = "From the evil of that which He created",
            translationUr = "ہر اس چیز کے شر سے جو اس نے پیدا کی،",
            translationHi = "जो कुछ उसने पैदा किया उसकी बुराई से,",
            words = listOf(
                WordPart("مِن شَرِّ", "Min sharri", "From the evil of", "کی برائی سے", "من أذى", "की बुराई से"),
                WordPart("مَا خَلَقَ", "Maa khalaq", "what He created", "جو اس نے پیدا کیا", "كل المخلوقات", "जो उसने रचा")
            )
        ),
        WordByWordAyah(
            surahNumber = 113, ayahNumber = 3,
            fullArabic = "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
            translationEn = "And from the evil of darkness when it settles",
            translationUr = "اور اندھیری رات کے شر سے جب وہ چھا جائے،",
            translationHi = "और अँधेरी रात की बुराई से जब वह छा जाए,",
            words = listOf(
                WordPart("غَاسِقٍ", "Ghaasiqin", "darkness / night", "اندھیری رات", "الليل المظلم", "गहरे अँधेरे"),
                WordPart("إِذَا وَقَبَ", "Idhaa waqab", "when it deepens", "جب چھا جائے", "إذا دخل واشتد", "जब वह फैल जाए")
            )
        ),
        WordByWordAyah(
            surahNumber = 113, ayahNumber = 4,
            fullArabic = "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
            translationEn = "And from the evil of the blowers in knots",
            translationUr = "اور گرہوں میں پھونک مارنے والیوں (جادوگروں) کے شر سے،",
            translationHi = "और गाँठों में फूँक मारने वालों की बुराई से,",
            words = listOf(
                WordPart("النَّفَّاثَاتِ", "An-naffaathaat", "those who blow", "پھونک مارنے والیاں", "السواحر", "फूँक मारने वाले"),
                WordPart("فِي الْعُقَدِ", "Fil-'uqad", "upon knots", "گرہوں میں", "العقد السحرية", "गाँठों में")
            )
        ),
        WordByWordAyah(
            surahNumber = 113, ayahNumber = 5,
            fullArabic = "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            translationEn = "And from the evil of an envier when he envies.\"",
            translationUr = "اور حسد کرنے والے کے شر سے جب وہ حسد کرے۔",
            translationHi = "और ईर्ष्यालु की बुराई से जब वह ईर्ष्या करे।\"",
            words = listOf(
                WordPart("حَاسِدٍ", "Haasidin", "an envier", "حسد کرنے والا", "صاحب الحسد", "ईर्ष्या करने वाला"),
                WordPart("إِذَا حَسَدَ", "Idhaa hasad", "when he envies", "جب حسد کرے", "إذا أظهر حسده", "जब वह जलन करे")
            )
        ),
        // Surah 114 An-Nas (6 Ayahs)
        WordByWordAyah(
            surahNumber = 114, ayahNumber = 1,
            fullArabic = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
            translationEn = "Say, \"I seek refuge in the Lord of mankind,",
            translationUr = "کہہ دیجیے: میں لوگوں کے پروردگار کی پناہ مانگتا ہوں،",
            translationHi = "कहो, \"मैं मनुष्यों के रब की शरण लेता हूँ,",
            words = listOf(
                WordPart("قُلْ أَعُوذُ", "Qul a'oodhu", "Say: I seek refuge", "کہو: میں پناہ مانگتا ہوں", "ألتجئ وأعتصم", "कहो: मैं शरण लेता हूँ"),
                WordPart("بِرَبِّ النَّاسِ", "Bi-rabbin-naas", "in the Lord of mankind", "انسانوں کے رب کی", "بخالق الناس ومربيهم", "मनुष्यों के रब की")
            )
        ),
        WordByWordAyah(
            surahNumber = 114, ayahNumber = 2,
            fullArabic = "مَلِكِ النَّاسِ",
            translationEn = "The Sovereign of mankind.",
            translationUr = "لوگوں کے حقیقی بادشاہ کی،",
            translationHi = "मनुष्यों के सम्राट की,",
            words = listOf(
                WordPart("مَلِكِ", "Maliki", "The King / Sovereign", "بادشاہ", "مالك أمرهم", "बादशाह / सम्राट"),
                WordPart("النَّاسِ", "An-naas", "of mankind", "لوگوں کا", "البشر", "मनुष्यों का")
            )
        ),
        WordByWordAyah(
            surahNumber = 114, ayahNumber = 3,
            fullArabic = "إِلَٰهِ النَّاسِ",
            translationEn = "The God of mankind,",
            translationUr = "لوگوں کے معبود برحق کی،",
            translationHi = "मनुष्यों के उपास्य (पूज्य) की,",
            words = listOf(
                WordPart("إِلَٰهِ", "Ilaahi", "The God / Object of Worship", "معبود", "المعبود بحق", "पूज्य ईश्वर"),
                WordPart("النَّاسِ", "An-naas", "of mankind", "لوگوں کا", "الناس", "मनुष्यों का")
            )
        ),
        WordByWordAyah(
            surahNumber = 114, ayahNumber = 4,
            fullArabic = "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
            translationEn = "From the evil of the retreating whisperer -",
            translationUr = "وسوسہ ڈالنے والے شیطان کے شر سے جو اللہ کے ذکر پر پیچھے ہٹ جاتا ہے،",
            translationHi = "वसवसा डालने वाले और अल्लाह की याद पर खिसक जाने वाले की बुराई से,",
            words = listOf(
                WordPart("الْوَسْوَاسِ", "Al-waswaas", "the whisperer", "وسوسہ ڈالنے والا", "الشيطان الموسوس", "बुरे ख्याल डालने वाला"),
                WordPart("الْخَنَّاسِ", "Al-khannaas", "who withdraws at remembrance", "پیچھے ہٹ جانے والا", "يتراجع عند ذكر الله", "याद करने पर भाग जाने वाला")
            )
        ),
        WordByWordAyah(
            surahNumber = 114, ayahNumber = 5,
            fullArabic = "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
            translationEn = "Who whispers [evil] into the breasts of mankind -",
            translationUr = "جو لوگوں کے سینوں میں وسوسے ڈالتا ہے،",
            translationHi = "जो मनुष्यों के दिलों में वसवसा डालता है,",
            words = listOf(
                WordPart("يُوَسْوِسُ", "Yuwaswisu", "whispers doubts/evil", "وسوسے ڈالتا ہے", "يلقي الوساوس", "बहकाता है"),
                WordPart("فِي صُدُورِ", "Fee sudoori", "into the chests/hearts", "سینوں میں", "قلوب", "सीनों में")
            )
        ),
        WordByWordAyah(
            surahNumber = 114, ayahNumber = 6,
            fullArabic = "مِنَ الْجِنَّةِ وَالنَّاسِ",
            translationEn = "From among the jinn and mankind.\"",
            translationUr = "خواہ وہ جنوں میں سے ہو یا انسانوں میں سے۔",
            translationHi = "चाहे वह जिन्नों में से हो या मनुष्यों में से।\"",
            words = listOf(
                WordPart("الْجِنَّةِ", "Al-jinnah", "the jinn", "جنات میں سے", "الجن", "जिन्नों में से"),
                WordPart("وَالنَّاسِ", "Wan-naas", "and mankind", "اور انسانوں میں سے", "البشر", "और इंसानों में से")
            )
        )
    )

    val extraSurahIntroductions: List<SurahExegesis> = listOf(
        SurahExegesis(
            surahNumber = 103,
            nameArabic = "سُورَةُ الْعَصْرِ",
            nameEnglish = "Surah Al-Asr (Time)",
            revelationPlace = "Makki",
            asbabNuzoolEn = "No single specific narration restricts its revelation; it is an early Makki surah summarizing salvation in 4 qualities. Imam Ash-Shafi'i famously remarked that if people pondered this Surah alone, it would suffice them for guidance.",
            asbabNuzoolUr = "اس سورت کے لیے کوئی ایک متعین واقعۂ نزول منقول نہیں؛ یہ ابتدائی مکی سورت ہے جو نجات کے چار اصول بیان کرتی ہے۔ امام شافعیؒ فرماتے ہیں کہ اگر لوگ صرف اسی سورت پر غور کر لیں تو ہدایت کے لیے کافی ہو جائے۔",
            asbabNuzoolAr = "سورة مكية ولم يثبت لها سبب نزول خاص واحد، وتلخص منهج النجاة في أربعة أصول. قال الشافعي: لو تدبر الناس هذه السورة لوسعتهم.",
            asbabNuzoolHi = "इस मक्की सूरह के लिए कोई एक विशेष अवतरण-प्रसंग अलग से दर्ज नहीं; यह मात्र तीन आयतों में सफलता के चार सूत्र बताती है।",
            mainThemesEn = listOf("Preciousness of passing time", "Four conditions for salvation: Faith, Righteous Deeds, Mutual Counsel in Truth, Mutual Patience"),
            mainThemesUr = listOf("وقت کی قدر و اہمیت", "نجات کی چار شرطیں: ایمان، عملِ صالح، تواصی بالحق اور تواصی بالصبر"),
            mainThemesAr = listOf("قيمة الزمن، وأركان النجاة الأربعة: الإيمان والعمل الصالح والتواصي بالحق والصبر"),
            mainThemesHi = listOf("समय का मूल्य", "सफलता के 4 सूत्र: ईमान, अच्छे कर्म, सत्य और धैर्य की प्रेरणा"),
            practicalLessonsEn = listOf("Invest daily hours purposefully rather than wasting life.", "Encourage friends gently toward truth and patience."),
            practicalLessonsUr = listOf("اپنا وقت بامقصد کاموں اور عبادت میں گزاریں۔", "اپنے ماحول میں حق اور صبر کی خیر خواہی پھیلائیں۔"),
            practicalLessonsAr = listOf("استثمار الوقت فيما ينفع، والتعاون الجماعي على الحق والصبر."),
            practicalLessonsHi = listOf("अपने समय का सदुपयोग करें और एक-दूसरे को धैर्य की सीख दें।"),
            sourceAttribution = "Tafseer Ibn Kathir (Surah Al-Asr)"
        ),
        SurahExegesis(
            surahNumber = 108,
            nameArabic = "سُورَةُ الْكَوْثَرِ",
            nameEnglish = "Surah Al-Kawthar (Abundance)",
            revelationPlace = "Makki (Majority view; some scholars say Madani)",
            asbabNuzoolEn = "Reported in classical Tafseer (Ibn Kathir / Al-Wahidi) that opponents such as Al-'As ibn Wa'il mocked the Prophet ﷺ after the passing of his young sons, calling him 'cut off' without legacy—so Allah revealed comfort promising Al-Kawthar.",
            asbabNuzoolUr = "کتبِ تفسیر (ابن کثیر وغیرہ) میں مروی ہے کہ جب نبی ﷺ کے صاحبزادوں کا انتقال ہوا تو بعض مشرکین (جیسے عاص بن وائل) نے طعنہ دیا کہ آپ کا نام مٹ جائے گا، جس کے جواب میں اللہ نے خیرِ کثیر و حوضِ کوثر کی خوشخبری نازل فرمائی۔",
            asbabNuzoolAr = "ورد في أسباب النزول أن بعض مشركي قريش عيّروا النبي ﷺ بموت أبنائه الذكور وقالوا إنه 'أبتر'، فأنزل الله بشارة الكوثر والذكر الباقي.",
            asbabNuzoolHi = "तफ़सीर की किताबों में वर्णित है कि मक्का के कुछ विरोधियों ने नबी ﷺ के बेटों की मृत्यु पर ताना मारा, तब अल्लाह ने दिलासा देते हुए अपार भलाई (कौसर) की यह सूरह नाज़िल की।",
            mainThemesEn = listOf("Abundant Divine blessing to the Prophet ﷺ", "Devoting prayer and sacrifice solely to Allah"),
            mainThemesUr = listOf("نبی ﷺ کو بے شمار نعمتیں و حوضِ کوثر کی عطا", "نماز اور قربانی کا صرف اللہ کے لیے خالص ہونا"),
            mainThemesAr = listOf("تكريم النبي ﷺ بالكوثر، وإخلاص الصلاة والنحر لله وحده"),
            mainThemesHi = listOf("पैगंबर ﷺ को अनंत सम्मान", "केवल अल्लाह के लिए नमाज़ और क़ुरबानी"),
            practicalLessonsEn = listOf("Respond to grief and mockery with gratitude, prayer, and generosity."),
            practicalLessonsUr = listOf("غم اور طعنوں کے وقت شکر، نماز اور اللہ کے راستے میں خرچ کرنے کو مضبوط کریں۔"),
            practicalLessonsAr = listOf("مواجهة الأذى بمزيد من الشكر والصلاة والإنفاق."),
            practicalLessonsHi = listOf("कष्ट और उपहास के समय भी प्रार्थना, कृतज्ञता और उदारता न छोड़ें।"),
            sourceAttribution = "Sahih Muslim (Book of Prayer: River of Al-Kawthar), Tafseer Ibn Kathir"
        )
    )

    val hifzTargetSurahs: List<HifzTarget> = listOf(
        HifzTarget(1, "سُورَةُ الْفَاتِحَةِ", "Surah Al-Fatihah", 7),
        HifzTarget(114, "سُورَةُ النَّاسِ", "Surah An-Nas", 6),
        HifzTarget(113, "سُورَةُ الْفَلَقِ", "Surah Al-Falaq", 5),
        HifzTarget(112, "سُورَةُ الْإِخْلَاصِ", "Surah Al-Ikhlas", 4),
        HifzTarget(111, "سُورَةُ الْمَسَدِ", "Surah Al-Masad", 5),
        HifzTarget(110, "سُورَةُ النَّصْرِ", "Surah An-Nasr", 3),
        HifzTarget(109, "سُورَةُ الْكَافِرُونَ", "Surah Al-Kafirun", 6),
        HifzTarget(108, "سُورَةُ الْكَوْثَرِ", "Surah Al-Kawthar", 3),
        HifzTarget(107, "سُورَةُ الْمَاعُونِ", "Surah Al-Ma'un", 7),
        HifzTarget(106, "سُورَةُ قُرَيْشٍ", "Surah Quraysh", 4),
        HifzTarget(105, "سُورَةُ الْفِيلِ", "Surah Al-Fil", 5),
        HifzTarget(104, "سُورَةُ الْهُمَزَةِ", "Surah Al-Humazah", 9),
        HifzTarget(103, "سُورَةُ الْعَصْرِ", "Surah Al-Asr", 3),
        HifzTarget(102, "سُورَةُ التَّكَاثُرِ", "Surah At-Takathur", 8),
        HifzTarget(101, "سُورَةُ الْقَارِعَةِ", "Surah Al-Qari'ah", 11),
        HifzTarget(97, "سُورَةُ الْقَدْرِ", "Surah Al-Qadr", 5)
    )

    val stage7 = AcademyStage(
        stageNumber = 7,
        id = "stage_7",
        titleEn = "Stage 7: Quran Learning From Zero (Qaida to Mushaf)",
        titleUr = "مرحلہ 7: صفر سے قرآن سیکھیں (نورانی قاعدہ تا ناظرہ)",
        titleAr = "المرحلة ٧: تعلّم القرآن من الصفر إلى التلاوة",
        titleHi = "चरण 7: शून्य से क़ुरआन सीखें (क़ाइदा से तिलावत तक)",
        subtitleEn = "15 Qaida lessons, Tajweed, Arabic Writing, Audio & Makharij",
        subtitleUr = "15 مکمل اسباقِ قاعدہ، مخارج، تجوید، کتابت اور صوتیات",
        subtitleAr = "١٥ درساً في القاعدة النورانية والمخارج والتجويد والكتابة والصوتيات",
        subtitleHi = "15 क़ाइदा पाठ, मख़ारिज, तजवीद, अरबी लेखन और उच्चारण अभ्यास",
        overviewEn = "Go beyond basic letters: master all 15 Noorani Qaida lessons, articulation points (Makharij), Tajweed rules, touch writing, and smooth Mushaf reading.",
        overviewUr = "صرف ابتدائی حروف نہیں بلکہ پورے 15 نورانی قاعدہ اسباق، مخارجِ حروف، تجوید، مشقِ کتابت اور قرآن پاک کی روانی سے تلاوت تک مکمل کورس۔",
        overviewAr = "منهج متكامل من الحروف المفردة عبر ١٥ درساً للقاعدة النورانية وأحكام التجويد والتدريب على الكتابة والنطق حتى القراءة المتقنة.",
        overviewHi = "शुरुआती अक्षरों से लेकर पूरे 15 नूरानी क़ाइदा पाठ, तजवीद के नियम, टच लेखन और धाराप्रवाह क़ुरआन पढ़ने तक की संपूर्ण तैयारी।",
        interactiveToolType = StageToolType.QAIDA_TAJWEED_WRITING,
        lessons = listOf(
            AcademyLesson(
                id = "s7_l1",
                stageNumber = 7,
                lessonNumber = 1,
                titleEn = "1. Mastering the Complete 15-Lesson Qaida Foundation",
                titleUr = "1. مکمل 15 اسباقِ نورانی قاعدہ کا تعارف و طریقۂ کار",
                titleAr = "١. منهج الدروس الخمسة عشر للقاعدة النورانية",
                titleHi = "1. पूरे 15-पाठ नूरानी क़ाइदा की संरचना",
                summaryEn = "Why authentic reading builds steadily from isolated shapes to Harakaat, Tanween, Sukoon, Tashdeed & Madd.",
                summaryUr = "مفردات اور مرکبات سے حرکات، تنوین، جزم، تشدید اور مدود تک مرحلہ وار مہارت۔",
                summaryAr = "التدرج الصوتي من الحروف المفردة إلى التشكيل والسكون والتشديد والمدود حتى الوصل الكامل.",
                summaryHi = "अकेले अक्षरों से हरकत, तनवीन, सुकूं, तशदीद और मद तक क्रमबद्ध अभ्यास।",
                bodyEn = "Learning Arabic Quran reading properly takes 15 structured steps—not a rushed summary:\nLessons 1–2: Isolated & Compound letters\nLessons 3–4: Short vowels (Harakat) & Tanween\nLessons 5–6: Sukoon & Tashdeed\nLessons 7–11: Madd elongations, Muqatta'at, Standing vowels & Leen\nLessons 12–15: Heavy/Light letters, Noon/Meem Sakinah, Waqf stopping rules, and Short Surahs.\n\nUse the interactive Qaida, Tajweed, Writing Pad & Audio Tabs below this guide to practice each item with voice.",
                bodyUr = "درست قرآنی تلفظ 8 نہیں بلکہ 15 جامع مراحل میں پختہ ہوتا ہے:\nاسباق 1-2: مفرد و مرکب حروف\nاسباق 3-4: حرکات اور تنوین\nاسباق 5-6: جزم اور تشدید\nاسباق 7-11: مدود، حروفِ مقطعات، کھڑی حرکات اور حروفِ لین\nاسباق 12-15: تفخیم و ترقیق، نون و میم ساکن، رموزِ وقف اور مختصر سورتیں۔\n\nذیل میں موجود انٹرایکٹو قاعدہ، تجوید، کتابت اور صوتیات کے ٹیبز سے مشق کریں۔",
                bodyAr = "يتدرج المنهج عبر ١٥ درساً متكاملاً لضبط القراءة القرآنية الصحيحة. استخدم علامات التبويب التفاعلية أسفله للتدرب على النطق والكتابة.",
                bodyHi = "सही क़ुरआनी पाठ 15 चरणों में विकसित होता है—अक्षर, हरकत, सुकूं, तशदीद, मद, भारी-हल्के अक्षर और रुकने के नियम। नीचे दिए गए इंटरैक्टिव क़ाइदा, तजवीद, लेखन पैड और ऑडियो टैब से अभ्यास करें।",
                references = listOf(
                    IslamicReference("Tajweed Tradition", "Classical Qaida & Jazariyyah Makharij framework"),
                    IslamicReference("Sahih Muslim", "Hadith 798 (Reward for striving to recite cleanly)")
                )
            ),
            AcademyLesson(
                id = "s7_l2",
                stageNumber = 7,
                lessonNumber = 2,
                titleEn = "2. Articulation Points (Makharij) & Tajweed Accuracy",
                titleUr = "2. مخارج الحروف اور تجوید کے بنیادی قواعد",
                titleAr = "٢. مخارج الحروف وأحكام التجويد الأساسية",
                titleHi = "2. मख़ारिज (उच्चारण स्थान) और तजवीद के नियम",
                summaryEn = "Learn the 5 main articulation areas, Qalqalah, Noon/Meem Sakinah, and Waqf stopping marks.",
                summaryUr = "حروف نکلنے کے 5 بڑے مقامات، قلقلہ، نون و میم ساکن اور وقف کے نشانات۔",
                summaryAr = "المخارج الخمسة العامة، القلقلة، أحكام النون والميم الساكنتين، وعلامات الوقف.",
                summaryHi = "उच्चारण के 5 क्षेत्र, क़लक़ला, नून-मीम साकिना और रुकने (वक़्फ़) के चिह्न।",
                bodyEn = "Tajweed simply means giving every Arabic letter its rightful place of exit (Makhraj) and characteristics (Sifat) so letters like س (Seen), ص (Saad), and ث (Thaa), or ت (Taa) and ط (Taa), never blur together.\n\nPractice slowly with the Audio & Makharij tool and trace every letter's shape on the Touch Writing Pad inside Stage 7!",
                bodyUr = "تجوید کا مطلب ہر حرف کو اس کے درست مخرج اور صفات کے ساتھ ادا کرنا ہے تاکہ ملتے جلتے حروف (مثلاً س، ص، ث یا ت، ط یا ح، ہ) آپس میں گڈمڈ نہ ہوں۔ اسٹیج 7 کے اندر آڈیو، تجوید اور تحریری مشق سے عملی تیاری کریں۔",
                bodyAr = "التجويد هو إعطاء كل حرف حقه ومستحقه مخرجاً وصفةً حتى لا تختلط الحروف المتقاربة مثل (س، ص، ث) أو (ت، ط) أو (ح، هـ).",
                bodyHi = "तजवीद का अर्थ हर अक्षर को उसके सही उच्चारण स्थान (मख़रज) से पढ़ना है ताकि मिलते-जुलते अक्षर अलग-अलग पहचाने जाएं। नीचे लेखन और ऑडियो अभ्यास का प्रयोग करें।",
                references = listOf(
                    IslamicReference("Al-Muqaddimah Al-Jazariyyah", "Imam Ibn al-Jazari — Standard Tajweed Primer")
                )
            ),
            AcademyLesson(
                id = "s7_l3",
                stageNumber = 7,
                lessonNumber = 3,
                titleEn = "3. Transition Roadmap: From Qaida to Fluent Mushaf Reading",
                titleUr = "3. قاعدہ سے مکمل قرآن پاک پڑھنے (ناظرہ) کا روڈ میپ",
                titleAr = "٣. خطة الانتقال من القاعدة إلى قراءة المصحف بطلاقة",
                titleHi = "3. क़ाइदा से संपूर्ण मुसहफ़ (क़ुरआन) पढ़ने की योजना",
                summaryEn = "A calm 4-step bridge to begin reading Juz Amma (Para 30) and Surah Al-Baqarah with confidence.",
                summaryUr = "تیسویں پارے کی سورتوں سے آغاز کر کے پورا قرآن روانی سے پڑھنے کی عملی ترتیب۔",
                summaryAr = "خطة عملية للبدء بقصار السور في جزء عمّ ثم التدرج في المصحف الشريف مع السماع المستمر.",
                summaryHi = "जुज़ अम्मा (पारा 30) की छोटी सूरतों से शुरू करके पूरा क़ुरआन पढ़ने की व्यावहारिक राह।",
                bodyEn = "Bridge Plan to the Full Quran:\n1. Start in Para 30 (An-Nas -> Al-A'la) because verses are short and rhythmic.\n2. Listen to 1 Ayah via slow (0.75x) reciter audio, then repeat aloud looking directly at the Arabic script.\n3. Never skip breath/stop marks; stop cleanly on a rounded Sukoon.\n4. Practice 10–15 calm minutes daily—consistency always beats long rare sessions.",
                bodyUr = "ناظرہ قرآن شروع کرنے کا 4 نکاتی فارمولا:\n1. پارہ 30 کی چھوٹی سورتوں (الناس تا الاعلیٰ) سے آغاز کریں۔\n2. قاری کی تلاوت (0.75x سلو اسپیڈ) سن کر عربی حروف پر انگلی رکھتے ہوئے دہرائیں۔\n3. روزانہ 10 سے 15 منٹ پابندی سے مشق کریں؛ تسلسل ہی کامیابی کی کنجی ہے۔",
                bodyAr = "ابدأ بقصار السور في جزء عمّ مع متابعة المصحف سماعاً وقراءةً يومية هادئة لمدة ١٠-١٥ دقيقة.",
                bodyHi = "1. पारा 30 की छोटी सूरतों से शुरुआत करें।\n2. धीमी गति (0.75x) से आयत सुनकर अरबी शब्दों पर नज़र रखते हुए दोहराएं।\n3. रोज़ाना सिर्फ 10-15 मिनट नियमित अभ्यास करें।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Muzzammil (73:4 - Recite in measured tones)")
                )
            )
        )
    )

    val stage8 = AcademyStage(
        stageNumber = 8,
        id = "stage_8",
        titleEn = "Stage 8: Quran Understanding & Word-by-Word",
        titleUr = "مرحلہ 8: فہمِ قرآن، لفظی ترجمہ اور تدبر",
        titleAr = "المرحلة ٨: فهم القرآن والترجمة الكلمية والتدبر",
        titleHi = "चरण 8: क़ुरआन की समझ, शब्द-दर-शब्द अनुवाद और तफ़सीर",
        subtitleEn = "25+ core Quranic words, 6 Word-by-Word Surahs & Asbab",
        subtitleUr = "کثرت سے آنے والے الفاظ، 6 سورتوں کا لفظی ترجمہ اور اسبابِ نزول",
        subtitleAr = "أهم مفردات القرآن، وترجمة كلمة بكلمة لست سور، وأسباب النزول",
        subtitleHi = "25+ मुख्य क़ुरआनी शब्द, 6 सूरतों का शब्द-दर-शब्द अर्थ और तफ़सीर",
        overviewEn = "Connect your heart to what you recite in prayer: explore high-frequency vocabulary flashcards, word-by-word translations across 6 key Surahs, and verified Asbab al-Nuzul.",
        overviewUr = "نماز میں پڑھی جانے والی آیات کا مفہوم سمجھیں: فلیش کارڈز، سورۃ الفاتحہ سمیت 6 سورتوں کا لفظی ترجمہ اور مستند اسبابِ نزول۔",
        overviewAr = "افهم ما تقرؤه في صلاتك عبر بطاقات المفردات، والترجمة الكلمية لست سور قصيرة، وأسباب النزول الموثقة.",
        overviewHi = "नमाज़ में पढ़ी जाने वाली सूरतों का अर्थ जानें: 25+ शब्दों के फ़्लैशकार्ड, 6 सूरतों का शब्द-दर-शब्द अनुवाद और प्रमाणित तफ़सीर।",
        interactiveToolType = StageToolType.QURAN_UNDERSTANDING,
        lessons = listOf(
            AcademyLesson(
                id = "s8_l1",
                stageNumber = 8,
                lessonNumber = 1,
                titleEn = "1. Why Word-by-Word Understanding Transforms Salah",
                titleUr = "1. لفظی ترجمہ سمجھنے سے نماز میں خشوع کیسے آتا ہے",
                titleAr = "١. أثر فهم معاني الكلمات في الخشوع بالصلاة",
                titleHi = "1. शब्द-दर-शब्द अर्थ समझने से नमाज़ में गहराई",
                summaryEn = "Over half of the Quran's vocabulary consists of fewer than 150 recurring roots and words—mastering the top 25 unlocks vast passages.",
                summaryUr = "قرآن کے کثرتِ استعمال والے الفاظ یاد کر لینے سے تلاوت سنتے ہی مفہوم ذہن میں روشن ہونے لگتا ہے۔",
                summaryAr = "حفظ الكلمات الأكثر تكراراً في القرآن يفتح باب التدبر والفهم المباشر أثناء التلاوة والصلاة.",
                summaryHi = "क़ुरआन में बार-बार आने वाले शब्दों को याद कर लेने से तिलावत सुनते ही अर्थ समझ आने लगता है।",
                bodyEn = "You do not need full university-level Arabic grammar before tasting the Quran's meaning. Words like 'Allah', 'Rabb', 'Min', 'Fee', 'Al-Ladhee', 'Qaala', 'Yawm', 'Qalb', and 'Rahmah' appear thousands of times.\n\nSwitch between the Vocabulary Flashcards, Word-by-Word Surahs (Al-Fatihah, Al-Asr, Al-Kawthar, Al-Ikhlas, Al-Falaq, An-Nas), and Tafseer Introductions tabs inside this stage.",
                bodyUr = "قرآن سمجھنے کے لیے مکمل نحوی عالم ہونا ضروری نہیں؛ محض بنیادی کثیر الاستعمال الفاظ (جیسے رب، قلب، رحمت، صبر، علم، یوم، من، فی) ہزاروں بار آتے ہیں۔ ذیل کے فلیش کارڈز اور 6 سورتوں کے لفظی ترجمے سے مشق کریں۔",
                bodyAr = "تعلّم المفردات الأكثر تكراراً والترجمة الكلمية لقصار السور يربط القلب مباشرة بمعاني الوحي.",
                bodyHi = "क़ुरआन समझने के लिए बहुत कठिन व्याकरण ज़रूरी नहीं; 'रब', 'क़ल्ब', 'रहमत', 'सब्र', 'इल्म', 'मिन', 'फ़ी' जैसे शब्द हज़ारों बार आते हैं। नीचे दिए टैब से अभ्यास करें।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Sad (38:29 - Reflect deeply upon its verses)")
                ),
                quizId = "q_s8_1"
            ),
            AcademyLesson(
                id = "s8_l2",
                stageNumber = 8,
                lessonNumber = 2,
                titleEn = "2. Understanding Themes, Context & Asbab al-Nuzul Responsibly",
                titleUr = "2. شانِ نزول اور قرآنی سیاق و سباق سمجھنے کے اصول",
                titleAr = "٢. ضوابط فهم أسباب النزول وسياق السورة",
                titleHi = "2. तफ़सीर और अवतरण के प्रसंग (असबाब-उन-नुज़ूल) की सही समझ",
                summaryEn = "Always honor authentic Asbab al-Nuzul reports and recognize when no single specific story is recorded.",
                summaryUr = "صحیح روایات کی بنیاد پر شانِ نزول سمجھنا اور جہاں کوئی مخصوص واقعہ ثابت نہ ہو وہاں عمومی ہدایت پر اکتفا کرنا۔",
                summaryAr = "الالتزام بما صح من أسباب النزول وتجنب القصص غير الموثقة.",
                summaryHi = "केवल प्रमाणित प्रसंगों का उल्लेख करना और बिना प्रमाण के कहानियाँ न जोड़ना।",
                bodyEn = "Knowing why a passage was revealed (Asbab al-Nuzul) illuminates its historical wisdom, while its guidance remains universal for all times. Wherever an authentic Hadith records a revelation context (as in Al-Ikhlas), we cite it—and wherever scholars note no single verified report exists (such as Surah Al-Asr), we state that clearly without inventing stories.",
                bodyUr = "شانِ نزول آیات کے تاریخی پس منظر کو واضح کرتا ہے جبکہ ہدایت قیامت تک کے لیے عام رہتی ہے۔ مستند تفاسیر (ابن کثیر، معارف القرآن، تفہیم القرآن) صرف ثابت شدہ واقعات بیان کرتی ہیں اور جہاں کوئی خاص واقعہ منقول نہ ہو وہاں صراحت سے اس کی نشان دہی کرتی ہیں۔",
                bodyAr = "معرفة أسباب النزول الصحيحة تعين على فهم حكمة التشريع مع بقاء العبرة بعموم اللفظ لا بخصوص السبب.",
                bodyHi = "अवतरण की पृष्ठभूमि जानने से आयत का ऐतिहासिक उद्देश्य स्पष्ट होता है, जबकि उसका संदेश हमेशा सबके लिए होता है। हम केवल प्रमाणित प्रसंग ही शामिल करते हैं।",
                references = listOf(
                    IslamicReference("Tafseer Ibn Kathir & Al-Wahidi", "Authenticated Asbab al-Nuzul principles")
                )
            )
        )
    )

    val stage9 = AcademyStage(
        stageNumber = 9,
        id = "stage_9",
        titleEn = "Stage 9: Hifz & Memorization Journey",
        titleUr = "مرحلہ 9: حفظِ قرآن اور دہرائی کا نظام",
        titleAr = "المرحلة ٩: رحلة الحفظ والمراجعة",
        titleHi = "चरण 9: हिफ़्ज़ (क़ुरआन याद करना) और दोहराव योजना",
        subtitleEn = "Daily targets, Sabaq/Sabqi/Dhor & streak progress",
        subtitleUr = "مختصر سورتیں، روزانہ سبق/سبقی/منزل کا نظام اور تکرار ٹریکر",
        subtitleAr = "خطط الحفظ اليومية، والمراجعة الثلاثية، وتتبع السور المحفوظة",
        subtitleHi = "दैनिक लक्ष्य, सबक़/सबक़ी/दोहर पद्धति और सूरह ट्रैकर",
        overviewEn = "Memorize essential Surahs for prayer and build lifelong retention using proven Sabaq (new), Sabqi (recent), and Dhor (cumulative) revision habits.",
        overviewUr = "نماز کی ضروری سورتوں کا حفظ، یومیہ ہدف، اور سبق/سبقی/منزل کے مستند طریقے سے مستقل یادداشت کی حفاظت۔",
        overviewAr = "احفظ السور الأساسية للصلاة وثبت حفظك عبر نظام المراجعة المتدرجة وتكرار التلاوة.",
        overviewHi = "नमाज़ के लिए आवश्यक सूरह याद करें और सबक़-सबक़ी-दोहर नियम से उन्हें हमेशा के लिए पक्का रखें।",
        interactiveToolType = StageToolType.HIFZ_TRACKER,
        lessons = listOf(
            AcademyLesson(
                id = "s9_l1",
                stageNumber = 9,
                lessonNumber = 1,
                titleEn = "1. The Sustainable Sabaq / Sabqi / Dhor Method",
                titleUr = "1. مضبوط حفظ کا راز: سبق، سبقی اور منزل (دہر)",
                titleAr = "١. منهج الحفظ المتين: الجديد والقريب والبعيد",
                titleHi = "1. पक्का हिफ़्ज़ रखने की विधि: सबक़, सबक़ी और दोहर",
                summaryEn = "Why steady small daily targets + consistent revision outperform cramming dozens of lines.",
                summaryUr = "تھوڑا مگر روزانہ نیا سبق اور پچھلے یاد کیے ہوئے حصے کی باقاعدہ دہرائی۔",
                summaryAr = "القليل الدائم مع التعاهد المنتظم أثبت من الكثرة المنقطعة.",
                summaryHi = "रोज़ थोड़ा नया याद करना और पुराने हिस्से को नियमित दोहराना सबसे कारगर है।",
                bodyEn = "Prophet Muhammad ﷺ advised renewing one's Quran regularly, for it slips away faster than an untied camel if neglected (Sahih al-Bukhari 5033).\n\nDaily 3-Part Formula:\n1. Sabaq (New Lesson): 2–3 new Ayahs listened 10x and recited fluently.\n2. Sabqi (Recent Revision): Recite the current Surah / recent 3 Surahs once without looking.\n3. Dhor (Older Revision): Rotate previously memorized Surahs inside your daily Sunnah/Nafl prayers.\n\nUse the Hifz Tracker below to log repetitions and check off memorized Surahs.",
                bodyUr = "نبی کریم ﷺ نے فرمایا: 'قرآن کی حفاظت (دہرائی) کرتے رہو، ورنہ یہ رسی سے کھلے ہوئے اونٹ سے بھی زیادہ تیزی سے نکل جاتا ہے' (صحیح بخاری: 5033)۔\n\nروزانہ کا تین نکاتی نظام:\n1. سبق: 2 سے 3 نئی آیات قاری سے سن کر پختہ کرنا۔\n2. سبقی: حالیہ یاد کی ہوئی سورتوں کو روز دہرانا۔\n3. منزل (دہر): پرانی یاد شدہ سورتوں کو روزانہ کی سنت و نفل نمازوں میں پڑھتے رہنا۔",
                bodyAr = "قال ﷺ: 'تعاهدوا القرآن، فوالذي نفسي بيده لهو أشد تفصياً من الإبل في عقلها' (البخاري: ٥٠٣٣). اقسم وقتك بين الجديد (الدرس)، والقريب، والقديم في صلواتك.",
                bodyHi = "पैगंबर ﷺ ने बताया कि क़ुरआन को नियमित दोहराते रहना ज़रूरी है (सहीह बुख़ारी: 5033)। रोज़ के तीन कदम: 1. सबक़ (2-3 नई आयतें सुनकर याद करना), 2. सबक़ी (हाल की सूरह दोहराना), 3. दोहर (पुरानी सूरतों को सुन्नत/नफ़्ल नमाज़ों में पढ़ते रहना)।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 5033 (Regularly revising the Quran)")
                ),
                quizId = "q_s9_1"
            )
        )
    )

    val stage11 = AcademyStage(
        stageNumber = 11,
        id = "stage_11",
        titleEn = "Stage 11: Assessments, Quizzes & Mastery Review",
        titleUr = "مرحلہ 11: امتحانات، کوئز اور عملی اعادہ",
        titleAr = "المرحلة ١١: الاختبارات والتقييم والمراجعة الشاملة",
        titleHi = "चरण 11: परीक्षा, क्विज़ और संपूर्ण दोहराव",
        subtitleEn = "Stage-by-stage tests, practical checks & spaced review",
        subtitleUr = "ہر مرحلے کے ٹیسٹ، عملی جانچ اور یاددہانی کا جامع جائزہ",
        subtitleAr = "اختبارات لكل مرحلة، ومراجعة عملية، وتقييم مستوى الإتقان",
        subtitleHi = "हर चरण के टेस्ट, व्यावहारिक जांच और अभ्यास दोहराव",
        overviewEn = "Verify what you have learned across all 10 preceding stages with quadrilingual explanations, practical daily checklists, and honest progress tracking.",
        overviewUr = "پچھلے تمام دس مراحل کی تعلیم کا مرحلہ وار جائزہ، فوری تشریح اور عملی احتساب کے ساتھ اپنا علمی معیار پرکھیں۔",
        overviewAr = "قيّم تحصيلك في المراحل العشر السابقة عبر اختبارات متدرجة وشروح وافية بجميع اللغات الأربع.",
        overviewHi = "पिछले सभी 10 चरणों की अपनी सीख को चार भाषाओं के विस्तृत उत्तरों और क्विज़ से परखें।",
        interactiveToolType = StageToolType.ASSESSMENTS_HUB,
        lessons = listOf(
            AcademyLesson(
                id = "s11_l1",
                stageNumber = 11,
                lessonNumber = 1,
                titleEn = "1. How Knowledge Check & Mastery Tracking Work",
                titleUr = "1. علمی جائزہ، اعادہ اور تکمیل کا معیار",
                titleAr = "١. ضوابط التقييم والمراجعة الذاتية",
                titleHi = "1. मूल्यांकन और प्रगति जांच के नियम",
                summaryEn = "Complete lessons honestly, pass stage quizzes with real comprehension, and schedule weekly revision.",
                summaryUr = "اسباق کی ایمانداری سے تکمیل، کوئز کے ذریعے فہم کی تصدیق اور ہفتہ وار دہرائی۔",
                summaryAr = "إتمام الدروس بوعي وفهم، واجتياز الاختبارات التفاعلية مع تخصيص مراجعة أسبوعية.",
                summaryHi = "हर पाठ को ईमानदारी से समझकर पूरा करना, क्विज़ पास करना और साप्ताहिक दोहराव रखना।",
                bodyEn = "Opening a lesson simply marks where you left off so your 'Continue Learning' shortcut always works cleanly—it NEVER marks the lesson mastered automatically.\n\nWhen you genuinely read through the teaching, practice the recitation/task, and pass the stage assessment below, confirm completion honestly to build an authentic lifelong foundation.",
                bodyUr = "کسی سبق کو محض کھولنے سے وہ مکمل شمار نہیں ہوتا—صرف آپ کی 'تکمیل کی تصدیق' یا امتحان پاس کرنے پر ہی پیش رفت محفوظ ہوتی ہے تاکہ علم پختہ اور حقیقی رہے۔",
                bodyAr = "مجرد فتح الدرس لا يحسبه مكتملاً، بل يضعه في 'مواصلة التعلم' فقط؛ ويُحتسب الإنجاز عند التأكيد الواعي أو اجتياز التقييم.",
                bodyHi = "किसी पाठ को सिर्फ़ खोलने से वह पूरा नहीं माना जाता (केवल 'पढ़ाई जारी रखें' के लिए सहेजा जाता है); पूरा समझने और अभ्यास के बाद ही 'पूर्ण करें' दबाएं या क्विज़ पास करें।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 71 (Whomever Allah wants good for, He grants understanding of the religion)")
                )
            )
        )
    )
}
