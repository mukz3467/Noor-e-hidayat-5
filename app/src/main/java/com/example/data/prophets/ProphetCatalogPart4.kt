package com.example.data.prophets

import com.example.data.model.AppLanguage

internal val PROPHET_CATALOG_PART4: List<ProphetStory> = listOf(
    ProphetStory(
        id = "zakariya",
        orderNumber = 22,
        arabicName = "زكريا عليه السلام",
        names = langMap("Zakariya (AS)", "حضرت زکریا علیہ السلام", "زكريا عليه السلام", "हज़रत ज़करिया (अ.स.)"),
        titles = langMap("Guardian of Maryam & Prophet of Secret Dua", "مریمؑ کے کفیل اور خفیہ دعا کے پیکر", "كفيل مريم وصاحب النداء الخفي", "मरयम (अ.स.) के संरक्षक व विनम्र दुआ के नबी"),
        category = ProphetCategory.BANI_ISRAIL_ERA,
        summaryIntro = langMap(
            "Guardian of Maryam (AS) in the Mihrab who beheld Allah's out-of-season provision for her and called softly upon his Lord in old age — receiving glad tidings of Prophet Yahya (AS).",
            "محراب میں حضرت مریمؑ کے کفیل، جنہوں نے بے موسم رزقِ الٰہی دیکھ کر بڑھاپے میں دھیمی آواز سے نیک وارث کی دعا مانگی اور حضرت یحییٰؑ کی بشارت پائی۔",
            "كفيل مريم عليها السلام، رأى عندها الرزق غير المعتاد فدعا ربه نداءً خفياً في كبره، فبشرته الملائكة بيحيى عليه السلام.",
            "मेहराब में मरयम (अ.स.) के संरक्षक, जिन्होंने बेमौसम रिज़्क़ देखकर बुढ़ापे में धीमी आवाज़ में दुआ की और पुत्र यहया (अ.स.) की खुशखबरी पाई।"
        ),
        detailedIntro = langMap(
            "Entrusted with the care of Maryam (AS), Zakariya (AS) repeatedly found fresh fruit out of season beside her in the sanctuary (3:37). Inspired by certainty that Allah provides without measure, he stood praying gently in secret despite gray hair and a barren wife: 'My Lord, do not leave me alone, while You are the best of inheritors' (19:2-6, 21:89). Angels called to him as he stood in prayer with the glad tidings of a pure son named Yahya, unmatched in name before him (3:39, 19:7).",
            "بیت المقدس میں حضرت مریمؑ کی کفالت کے دوران جب بھی حجرے میں تشریف لے جاتے تو بے موسم تازہ پھل موجود پاتے (3:37)۔ اس سے یقینِ کامل نے جوش مارا اور بڑھاپے میں نہایت عاجزی و خفیہ دعا کے ساتھ اللہ سے نیک جانشین مانگا (19:2-6، 21:89)۔ نماز ہی کی حالت میں فرشتوں نے حضرت یحییٰؑ کی بشارت دی (3:39)۔",
            "كلما دخل على مريم المحراب وجد عندها رزقاً، فاستبشر بقدرة الله ودعا ربه في الكبر نداءً خفياً أن يرزقه ولياً يرثه ويرث من آل يعقوب (مريم 19:2-6)، فبشرته الملائكة وهو قائم يصلي في المحراب بغلام اسمه يحيى (آل عمران 3:39).",
            "जब भी वे मेहराब में मरयम (अ.स.) के पास जाते तो बेमौसम फल पाते (3:37)। अल्लाह की असीम शक्ति पर विश्वास से उन्होंने बुढ़ापे में धीमे स्वर में नेक संतान की दुआ की (19:2-6, 21:89), और नमाज़ की अवस्था में यहया (अ.स.) की खुशखबरी मिली (3:39)।"
        ),
        quranicContext = langMap(
            "Narrated in Aal-e-Imran (3:37-41), Maryam (19:2-11), and Al-Anbiya (21:89-90).",
            "آل عمران (3:37-41)، مریم (19:2-11) اور الانبیاء (21:89-90) میں مفصل بیان ہے۔",
            "في آل عمران (3:37-41) ومريم (19:2-11) والأنبياء (21:89-90).",
            "आल-ए-इमरान (3:37-41), मरयम (19:2-11) और अंबिया (21:89-90) में वर्णित।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Caring for young Maryam in the sanctuary and witnessing out-of-season provision (3:37).",
                "Secret heartfelt prayer in old age praising Allah's faithfulness (19:2-6, 21:89).",
                "Angels announcing Yahya while Zakariya stood praying in the Mihrab (3:39, 19:7).",
                "Sign of three days communicating only by gesture and remembering Allah morning and evening (3:41, 19:10-11)."
            ),
            listOf(
                "محراب میں حضرت مریمؑ کی کفالت اور بے موسم رزق کا مشاہدہ (3:37)۔",
                "بڑھاپے میں خفیہ اور عاجزانہ دعا کہ اے رب مجھے تنہا نہ چھوڑ (19:2-6، 21:89)۔",
                "حالتِ نماز میں فرشتوں کی جانب سے حضرت یحییٰؑ کی بشارت (3:39)۔",
                "تین دن اشاروں سے گفتگو کی نشانی اور صبح و شام تسبیح کا حکم (3:41، 19:10-11)۔"
            ),
            listOf(
                "كفالة مريم ومشاهدة الرزق غير المعتاد في المحراب (آل عمران 3:37).",
                "النداء الخفي في الكبر وحسن الظن بالله (مريم 19:2-6، الأنبياء 21:89).",
                "البشارة بيحيى وهو قائم يصلي في المحراب (آل عمران 3:39).",
                "آية الصمت ثلاثة أيام مع كثرة التسبيح بكرة وعشياً (آل عمران 3:41، مريم 19:10-11)."
            ),
            listOf(
                "मरयम (अ.स.) का संरक्षण व बेमौसम रिज़्क़ देखना (3:37)।",
                "बुढ़ापे में विनम्र और धीमी दुआ (19:2-6, 21:89)।",
                "मेहराब में नमाज़ के दौरान यहया (अ.स.) की शुभ सूचना (3:39)।",
                "तीन दिन संकेतों से संवाद और प्रातः-सायं अल्लाह की तस्बीह की निशानी (3:41)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Zakariya (AS), his righteous wife, son Yahya (AS), Maryam (AS)", "The Sanctuary (Al-Mihrab) in Jerusalem"),
            listOf("حضرت زکریاؑ، نیک زوجہ، فرزند یحییٰؑ، حضرت مریمؑ", "بیت المقدس (محراب)"),
            listOf("زكريا وزوجه الصالحة وابنه يحيى ومريم عليها السلام", "المحراب في بيت المقدس"),
            listOf("ज़करिया (अ.स.), पत्नी, पुत्र यहया (अ.स.), मरयम (अ.स.)", "यरूशलेम (मेहराब)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(19, 2, 7, langMap("The Secret Call & Glad Tidings", "خفیہ دعا اور بشارتِ یحییٰؑ", "النداء الخفي والبشارة", "धीमी पुकार और शुभ सूचना")),
            ProphetVerseRef(21, 89, 90, langMap("Hasting to Good Deeds & Humility", "نیکیوں میں سبقت اور عاجزی", "المسارعة في الخيرات", "नेकियों में आगे रहना व विनम्रता"))
        ),
        hadithRefs = emptyList(),
        lessons = listLangMap(
            listOf(
                "Witnessing Allah's mercy upon others should inspire hopeful dua, never envy.",
                "Praying softly in hope and awe ('raghaban wa rahaban') brings answered prayers (21:90)."
            ),
            listOf(
                "دوسروں پر اللہ کی نعمت دیکھ کر حسد نہیں بلکہ امید کے ساتھ دعا کرنی چاہیے۔",
                "نیکیوں میں جلدی اور رغبت و خوف کے ساتھ دعا قبولیت کا سبب ہے (21:90)۔"
            ),
            listOf(
                "رؤية نعمة الله على الغير تبعث في القلب الأمل والدعاء لا الحسد.",
                "الدعاء رغباً ورهباً مع المسارعة في الخيرات سبب للاستجابة (الأنبياء 21:90)."
            ),
            listOf(
                "दूसरों पर अल्लाह की कृपा देखकर ईर्ष्या के बजाय आशा व दुआ करनी चाहिए।",
                "नेकी में तत्परता व आशा-भय के साथ दुआ स्वीकार होती है (21:90)।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(21, 89, 89, langMap("Leave me not childless; You are the best inheritor", "رب لا تذرنی فرداً وانت خیر الوارثین", "رب لا تذرني فرداً وأنت خير الوارثين", "ऐ रब, मुझे अकेला न छोड़ और तू सबसे उत्तम वारिस है")),
            ProphetDuaRef(3, 38, 38, langMap("Grant me from Yourself good offspring", "رب ہب لی من لدنک ذریۃ طیبۃ", "رب هب لي من لدنك ذرية طيبة", "मुझे अपनी ओर से नेक संतान प्रदान कर"))
        ),
        reliabilityNotes = langMap(
            "Drawn exclusively from Quran 3:37-41, 19:2-11, and 21:89-90.",
            "مکمل طور پر قرآنِ مجید (آل عمران، مریم اور الانبیاء) پر مبنی۔",
            "مستند إلى الآيات القرآنية الصريحة في آل عمران ومريم والأنبياء.",
            "पूर्णतः क़ुरआन 3:37-41, 19:2-11 व 21:89-90 पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Where was Zakariya (AS) when the angels announced the birth of Yahya (AS) in Quran 3:39?",
                "قرآن (3:39) کے مطابق فرشتوں نے حضرت زکریاؑ کو یحییٰؑ کی بشارت کس حالت میں دی؟",
                "في أي حال بشّرت الملائكة زكريا بيحيى عليه السلام (آل عمران 3:39)؟",
                "क़ुरआन 3:39 के अनुसार जब फ़रिश्तों ने यहया (अ.स.) की खुशखबरी दी तब ज़करिया (अ.स.) क्या कर रहे थे?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Standing in prayer inside the Mihrab (sanctuary)", "Sailing across the sea", "Walking in the marketplace"),
                AppLanguage.URDU to listOf("محراب میں کھڑے نماز پڑھ رہے تھے", "سمندر کا سفر کر رہے تھے", "بازار میں چل رہے تھے"),
                AppLanguage.ARABIC to listOf("قائماً يصلي في المحراب", "يبحر في السفينة", "يمشي في السوق"),
                AppLanguage.HINDI to listOf("मेहराब में खड़े होकर नमाज़ पढ़ रहे थे", "समुद्री यात्रा कर रहे थे", "बाज़ार में टहल रहे थे")
            ),
            0,
            langMap(
                "Quran 3:39 notes: 'So the angels called him while he was standing in prayer in the chamber [Al-Mihrab]...'",
                "آل عمران 3:39: فرشتوں نے انہیں آواز دی جبکہ وہ محراب میں کھڑے نماز پڑھ رہے تھے۔",
                "آل عمران 3:39: فنادته الملائكة وهو قائم يصلي في المحراب.",
                "क़ुरआन 3:39 में मेहराब में खड़े होकर नमाज़ पढ़ने का स्पष्ट उल्लेख है।"
            )
        )
    ),
    ProphetStory(
        id = "yahya",
        orderNumber = 23,
        arabicName = "يحيى عليه السلام",
        names = langMap("Yahya (AS)", "حضرت یحییٰ علیہ السلام", "يحيى عليه السلام", "हज़रत यहया (अ.स.)"),
        titles = langMap("Pure, Compassionate & Dutiful Prophet", "پاکیزہ نفس، نرم دل اور مطیع نبی", "السيد الحصور والنبي الصالح", "पवित्र, दयालु व माता-पिता के आज्ञाकारी नबी"),
        category = ProphetCategory.BANI_ISRAIL_ERA,
        summaryIntro = langMap(
            "Miraculous son granted to elderly Zakariya (AS), endowed with wisdom in childhood, tender compassion, purity, Taqwa, and filial devotion, confirming the Word from Allah (Isa).",
            "حضرت زکریاؑ کے معجزانہ فرزند، جنہیں بچپن ہی میں حکمت، نرمی و شفقت، پاکیزگی، تقویٰ اور والدین کے ساتھ حسنِ سلوک عطا ہوا اور حضرت عیسیٰؑ کی تصدیق فرمائی۔",
            "وهبه الله لزكريا في الكبر، وآتاه الحكم صبياً، وحناناً وزكاةً، وكان تقياً وبراً بوالديه، ومصدقاً بكلمة من الله (عيسى).",
            "बुज़ुर्ग ज़करिया (अ.स.) के चमत्कारी पुत्र, जिन्हें बचपन में ही बुद्धिमत्ता, करुणा, पवित्रता, तक़वा और माता-पिता की सेवा का गुण मिला।"
        ),
        detailedIntro = langMap(
            "Even before his birth, Allah gave Zakariya glad tidings of Yahya: 'confirming a word from Allah [Isa], and [being] honorable, abstaining [from women], and a prophet from among the righteous' (3:39). Allah addressed him early in life: 'O Yahya, take the Scripture with determination,' granting him judgment while still a youth, heartfelt tenderness (hanan), purity (zakah), Taqwa, dutifulness to parents, and freedom from arrogance (19:12-14).",
            "پیدائش سے قبل ہی بشارت دی گئی کہ وہ اللہ کے کلمہ (حضرت عیسیٰؑ) کی تصدیق کرنے والے، سردار، نفس پر قابو رکھنے والے اور صالح نبی ہوں گے (3:39)۔ بچپن ہی میں انہیں کتاب کو مضبوطی سے تھامنے کا حکم اور فہم و حکمت ملی؛ نیز نرم دلی، پاکیزگی، تقویٰ اور والدین کی خدمت آپ کی نمایاں صفات تھیں (19:12-14)۔",
            "بُشّر به قبل ولادته مصدقاً بكلمة من الله وسيداً وحصوراً ونبياً من الصالحين (آل عمران 3:39). وخاطبه الله: يا يحيى خذ الكتاب بقوة وآتيناه الحكم صبياً، واتصف بالحنان والزكاة والتقوى وبر الوالدين ولم يكن جباراً عصياً (مريم 19:12-15).",
            "जन्म से पूर्व ही उनके नेक नबी व ईसा (अ.स.) की पुष्टि करने वाले होने की सूचना मिली (3:39)। बचपन में ही हिकमत, करुणा, पवित्रता, तक़वा तथा माता-पिता की सेवा से विभूषित हुए (19:12-15)।"
        ),
        quranicContext = langMap(
            "Described in Aal-e-Imran (3:39), Al-An'am (6:85), Maryam (19:7, 12-15), and Al-Anbiya (21:90).",
            "آل عمران (3:39)، الانعام (6:85)، مریم (19:7، 12-15) اور الانبیاء (21:90) میں ذکر ہے۔",
            "في آل عمران (3:39) والأنعام (6:85) ومريم (19:7، 12-15) والأنبياء (21:90).",
            "आल-ए-इमरान (3:39), अनआम (6:85), मरयम (19:7, 12-15) और अंबिया (21:90) में वर्णित।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Named uniquely by Allah Himself ('We have not made to him before [anyone of] his name', 19:7).",
                "Commanded to hold firmly to the Scripture and granted wisdom in childhood (19:12).",
                "Distinguished by compassion, purity, Taqwa, and gentle obedience to parents (19:13-14).",
                "Blessed with Allah's three-fold greeting of peace — day of birth, death, and resurrection (19:15)."
            ),
            listOf(
                "اللہ تعالیٰ کی جانب سے خود 'یحییٰ' نام رکھنا (19:7)۔",
                "کتابِ الٰہی کو مضبوطی سے تھامنے اور بچپن میں حکمت ملنے کا بیان (19:12)۔",
                "نرم دلی، پاکیزگی، تقویٰ اور والدین سے حسنِ سلوک کا اعزاز (19:13-14)۔",
                "پیدائش، وفات اور قیامت کے دن سلامتی کی بشارت (19:15)۔"
            ),
            listOf(
                "تسميته من الله باسم لم يُسبق إليه (مريم 19:7).",
                "الأمر بأخذ الكتاب بقوة وإيتاؤه الحكم صبياً (مريم 19:12).",
                "الجمع بين الحنان والزكاة والتقوى وبر الوالدين (مريم 19:13-14).",
                "سلام الله عليه يوم ولد ويوم يموت ويوم يبعث حياً (مريم 19:15)."
            ),
            listOf(
                "अल्लाह द्वारा स्वयं अनोखा नाम 'यहया' रखना (19:7)।",
                "बचपन में ही धर्मग्रंथ व बुद्धिमत्ता की प्राप्ति (19:12)।",
                "करुणा, पवित्रता, तक़वा व माता-पिता की सेवा (19:13-14)।",
                "जन्म, मृत्यु व पुनरुत्थान के दिन अल्लाह का सलाम (19:15)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Yahya (AS), father Zakariya (AS), cousin Isa (AS)", "Sacred Land / Jerusalem"),
            listOf("حضرت یحییٰؑ، والد زکریاؑ، حضرت عیسیٰؑ", "ارضِ مقدس / بیت المقدس"),
            listOf("يحيى ووالده زكريا وعيسى بن مريم عليهم السلام", "الأرض المقدسة"),
            listOf("यहया (अ.स.), पिता ज़करिया (अ.स.), ईसा (अ.स.)", "पवित्र भूमि (यरूशलेम)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(19, 12, 15, langMap("Wisdom in Youth & Three Greetings of Peace", "بچپن میں حکمت اور سلامِ الٰہی", "الحكم صبياً والسلام عليه", "युवावस्था में हिकमत व सलाम")),
            ProphetVerseRef(3, 39, 39, langMap("Confirming a Word from Allah", "کلمۃ اللہ کی تصدیق", "مصدقاً بكلمة من الله", "अल्लाह के वचन की पुष्टि"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih Muslim 162a",
                "https://sunnah.com/muslim:162a",
                langMap(
                    "During the Night Journey (Mi'raj), the Prophet ﷺ met maternal cousins Yahya (AS) and Isa (AS) together in the second heaven.",
                    "معراج کی رات نبی اکرم ﷺ نے دوسرے آسمان پر حضرت یحییٰؑ اور حضرت عیسیٰؑ سے ملاقات فرمائی۔",
                    "في حديث المعراج لقي النبي ﷺ ابني الخالة يحيى وعيسى عليهما السلام في السماء الثانية.",
                    "मेराज की रात नबी ﷺ की दूसरे आसमान में यहया और ईसा (अ.स.) से भेंट हुई।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Holding firmly to revelation begins in youth with sincerity, not postponement.",
                "Combining deep knowledge with tenderness and respect for parents defines true piety."
            ),
            listOf(
                "دین پر مضبوطی سے عمل جوانی اور بچپن ہی سے شروع ہونا چاہیے۔",
                "علم کے ساتھ نرم دلی اور والدین کا ادب حقیقی تقویٰ کی علامت ہے۔"
            ),
            listOf(
                "أخذ الدين بقوة وصدق يبدأ منذ الصغر.",
                "لا يكتمل الصلاح إلا ببر الوالدين ولين القلب ونبذ الكبر."
            ),
            listOf(
                "धर्म पर दृढ़ता से चलना युवावस्था से ही प्रारंभ होना चाहिए।",
                "ज्ञान के साथ करुणा व माता-पिता की सेवा ही सच्चा तक़वा है।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Anchored strictly in Quran 3:39, 19:7, 19:12-15 and Sahih Muslim 162a.",
            "قرآن کریم اور صحیح مسلم (162a) سے مکمل مستند۔",
            "مستند إلى القرآن الكريم وصحيح مسلم (162a).",
            "क़ुरआन और सहीह मुस्लिम 162a से प्रमाणित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Which virtue toward family is highlighted for Yahya (AS) in Quran 19:14?",
                "قرآن (19:14) میں حضرت یحییٰؑ کی خاندان کے حوالے سے کس صفت کی تعریف کی گئی ہے؟",
                "ما الخلق العائلي الذي أُثني به على يحيى في سورة مريم (19:14)؟",
                "क़ुरआन 19:14 में यहया (अ.स.) के किस पारिवारिक गुण की प्रशंसा है?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Dutifulness to his parents; never arrogant or disobedient", "Accumulating palaces", "Leading sea trade"),
                AppLanguage.URDU to listOf("والدین کے ساتھ حسنِ سلوک اور تکبر و نافرمانی سے پاک ہونا", "محل تعمیر کرنا", "سمندری تجارت"),
                AppLanguage.ARABIC to listOf("برّه بوالديه وأنه لم يكن جباراً عصياً", "تشييد القصور", "قيادة التجارة البحرية"),
                AppLanguage.HINDI to listOf("माता-पिता के प्रति सेवाभाव व अहंकार से मुक्त होना", "महल बनाना", "समुद्री व्यापार")
            ),
            0,
            langMap(
                "Quran 19:14 praises Yahya (AS): 'And dutiful to his parents, and he was not a disobedient tyrant.'",
                "سورۃ مریم 19:14: اور اپنے والدین کے ساتھ نیکی کرنے والے تھے، سرکش و نافرمان نہ تھے۔",
                "مريم 19:14: وبراً بوالديه ولم يكن جباراً عصياً.",
                "क़ुरआन 19:14 में माता-पिता के प्रति सेवाभाव की स्पष्ट प्रशंसा है।"
            )
        )
    ),
    ProphetStory(
        id = "isa",
        orderNumber = 24,
        arabicName = "عيسى عليه السلام",
        names = langMap("Isa (AS) / Al-Masih", "حضرت عیسیٰ علیہ السلام", "عيسى ابن مريم عليه السلام", "हज़रत ईसा (अ.स.)"),
        titles = langMap("Al-Masih & Messenger of Pure Tawhid", "روح اللہ، کلمۃ اللہ اور اولوالعزم پیغمبر", "المسيح ورسول الله وكلمته", "मसीह व उलुल-अज़्म रसूल"),
        category = ProphetCategory.ULUL_AZM,
        summaryIntro = langMap(
            "Miraculously born without a father to the pure virgin Maryam (AS), given the Injil, speaking in the cradle as the servant and Messenger of Allah, and raised alive unto Allah without being crucified (4:157-158).",
            "حضرت مریمؑ سے بن باپ معجزانہ ولادت، گہوارے میں 'میں اللہ کا بندہ اور نبی ہوں' کا اعلان، انجیل کی عطا، معجزاتِ شفا اور صلیب سے محفوظ رکھ کر آسمان پر زندہ اٹھائے جانے والے اولوالعزم پیغمبر (4:157-158)۔",
            "المسيح ابن مريم، ولد بمعجزة من غير أب وتكلم في المهد: إني عبد الله آتاني الكتاب وجعلني نبياً، وأُوتي الإنجيل، وبرّأه الله ورفعه إليه حياً ولم يُقتل ولم يُصلب (النساء 4:157-158).",
            "कुँवारी मरयम (अ.स.) से बिना पिता चमत्कारी जन्म, पालने में ही ख़ुद को अल्लाह का बंदा व नबी घोषित किया, इंजील प्राप्त हुई, और सूली से बचाकर अल्लाह ने उन्हें जीवित ऊपर उठा लिया (4:157-158)।"
        ),
        detailedIntro = langMap(
            "One of the five Ulul-Azm Messengers, Isa son of Maryam (AS) was conceived by Allah's command 'Be, and it is' (3:47, 3:59). In infancy he defended his pure mother by speaking from the cradle (19:30-33), called Bani Isra'il cleanly to worship Allah alone (5:72), received the Injil confirming the Torah, and performed miracles of healing and raising the dead strictly 'by Allah's permission' (3:49, 5:110). When plotters sought his life, Allah protected and raised him up unto Himself (4:157-158), and authentic Hadith affirms his return near the end of time to establish justice.",
            "اولوالعزم پیغمبروں میں سے حضرت عیسیٰ ابن مریمؑ کی ولادت حکمِ الٰہی 'کُن فیکون' سے ہوئی (3:59)۔ گہوارے میں کلام فرما کر اپنی والدہ کی براءت اور اپنی بندگی و نبوت کا اعلان فرمایا (19:30)۔ آپ نے صرف اللہ کی عبادت کی دعوت دی (5:72)، انجیل ملی اور اللہ کے اذن سے مردے زندہ کیے و بیماروں کو شفا دی (3:49)۔ قرآن (4:157-158) کے مطابق آپ قتل یا صلیب پر نہیں چڑھائے گئے بلکہ اللہ نے آپ کو زندہ اٹھا لیا، اور صحیح احادیث میں قربِ قیامت ان کے عادل حکمراں بن کر نزول کی خبر ہے۔",
            "من أولي العزم من الرسل. خلقه الله بكلمة منه كخلق آدم (آل عمران 3:59)، فأنطقه في المهد (مريم 19:30-33)، ودعا بني إسرائيل إلى توحيد الله، وآتاه الله الإنجيل ومعجزات الشفاء بإذن الله (آل عمران 3:49). وأكد القرآن: وما قتلوه وما صلبوه ولكن شبّه لهم بل رفعه الله إليه (النساء 4:157-158)، وتواترت الأحاديث الصحيحة بنزوله قبل الساعة حكماً مقسطاً.",
            "उलुल-अज़्म रसूलों में एक ईसा (अ.स.) की रचना अल्लाह के वचन 'कुन' से हुई (3:59)। पालने में बोलकर अपनी माता की पवित्रता व अपनी बंदगी सिद्ध की (19:30), इंजील मिली और 'अल्लाह की अनुमति से' चमत्कार दिखाए (3:49)। क़ुरआन 4:157-158 के अनुसार वे सूली पर नहीं चढ़े बल्कि अल्लाह ने उन्हें ऊपर उठा लिया, और सहीह हदीस के अनुसार वे क़ियामत से पूर्व न्याय स्थापित करने उतरेंगे।"
        ),
        quranicContext = langMap(
            "Major Quranic passages appear in Aal-e-Imran (3:45-59), An-Nisa (4:157-159, 171), Al-Ma'idah (5:110-118), Maryam (19:16-36), and As-Saff (61:6).",
            "آل عمران (3:45-59)، النساء (4:157-159، 171)، المائدہ (5:110-118)، مریم (19:16-36) اور الصف (61:6) میں مفصل بیان ہے۔",
            "في آل عمران (3:45-59) والنساء (4:157-159، 171) والمائدة (5:110-118) ومريم (19:16-36) والصف (61:6).",
            "आल-ए-इमरान (3:45-59), निसा (4:157-159, 171), मायदा (5:110-118), मरयम (19:16-36) और सफ़ (61:6) में विस्तृत वर्णन।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Miraculous virgin birth to Maryam (AS) and speaking as an infant (3:45-47, 19:29-33).",
                "Revelation of the Injil, affirmation of pure Tawhid, and foretelling Ahmad / Muhammad ﷺ (5:72, 61:6).",
                "Miracles performed strictly 'by Allah's permission' (bi-idhnillah) and steadfastness of Al-Hawariyyun (3:49, 3:52).",
                "Protected from crucifixion and raised alive unto Allah (4:157-158)."
            ),
            listOf(
                "حضرت مریمؑ سے معجزانہ ولادت اور گہوارے میں کلام (3:45-47، 19:29-33)۔",
                "نزولِ انجیل، خالص توحید کی دعوت اور نبی آخرالزماں احمد ﷺ کی بشارت (5:72، 61:6)۔",
                "اللہ کے اذن (باذن اللہ) سے معجزات اور حواریوں کا نصرتِ دین پر بیعت کرنا (3:49، 3:52)۔",
                "قتل و صلیب سے حفاظت اور آسمان کی طرف زندہ اٹھایا جانا (4:157-158)۔"
            ),
            listOf(
                "ولادته المعجزة من مريم وكلامه في المهد (آل عمران 3:45، مريم 19:30).",
                "إنزال الإنجيل والدعوة للتوحيد والبشارة بأحمد ﷺ (المائدة 5:72، الصف 61:6).",
                "معجزات الشفاء وإحياء الموتى بإذن الله ومؤازرة الحواريين (آل عمران 3:49، 52).",
                "نجاته من القتل والصلب ورفعه إلى الله (النساء 4:157-158)."
            ),
            listOf(
                "चमत्कारी जन्म और पालने में संवाद (3:45-47, 19:29-33)।",
                "इंजील का अवतरण, तौहीद का संदेश व अहमद ﷺ की शुभ सूचना (5:72, 61:6)।",
                "अल्लाह की अनुमति से चमत्कार व हवारियों का साथ (3:49, 52)।",
                "सूली व हत्या से रक्षा और जीवित ऊपर उठाया जाना (4:157-158)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Isa ibn Maryam (AS), mother Maryam (AS), Al-Hawariyyun (the Disciples)", "Jerusalem / Baitul-Maqdis and Palestine"),
            listOf("حضرت عیسیٰؑ، والدہ حضرت مریمؑ، حواریین", "بیت المقدس / ارضِ مقدس"),
            listOf("عيسى ابن مريم وأمه مريم والحواريون", "بيت المقدس والأرض المباركة"),
            listOf("ईसा (अ.स.), माता मरयम (अ.स.), हवारी", "यरूशलेम व फ़िलिस्तीन")
        ),
        verseRefs = listOf(
            ProphetVerseRef(19, 30, 34, langMap("Statement in the Cradle", "گہوارے میں کلامِ نبوت", "قوله في المهد", "पालने में घोषणा")),
            ProphetVerseRef(4, 157, 158, langMap("Raised Unto Allah, Not Crucified", "صلیب سے محفوظ اور رفعِ آسمانی", "الرفع إلى الله", "सूली से रक्षा व आरोहण")),
            ProphetVerseRef(5, 114, 118, langMap("Al-Ma'idah & Dialogue of Servitude", "دعائے مائدہ اور توحید کی گواہی", "دعاء المائدة وبراءة التوحيد", "मायदा की दुआ व तौहीद की गवाही"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3448",
                "https://sunnah.com/bukhari:3448",
                langMap(
                    "Affirms the return of Isa son of Maryam (AS) as a just ruler breaking false symbols and establishing peace.",
                    "صحیح بخاری (3448): حضرت عیسیٰ ابن مریمؑ کے عادل حکمران کی حیثیت سے نزول اور قیامِ عدل کی صحیح حدیث۔",
                    "صحيح البخاري (3448): حديث نزول عيسى ابن مريم حَكَماً عدلاً مقسطاً.",
                    "सहीह बुख़ारी 3448: ईसा इब्ने मरयम (अ.स.) के न्यायपूर्ण शासक के रूप में उतरने की प्रामाणिक हदीस।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Isa (AS) himself declared before all humanity that his highest honor is being 'Abdullah' — a servant of Allah (19:30).",
                "Miracles demonstrate Allah's power, never divinity belonging to a messenger."
            ),
            listOf(
                "حضرت عیسیٰؑ نے خود سب سے بڑا اعزاز 'انی عبداللہ' (میں اللہ کا بندہ ہوں) بیان فرمایا (19:30)۔",
                "تمام معجزات 'باذن اللہ' ہوتے ہیں، جو صرف خالقِ کائنات کی قدرت کے دلائل ہیں۔"
            ),
            listOf(
                "أعلى شرف لعيسى عليه السلام عبوديته الخالصة لله: إني عبد الله (مريم 19:30).",
                "المعجزات تقع بإذن الله دلالةً على صدق الرسالة لا ألوهية الرسول."
            ),
            listOf(
                "ईसा (अ.स.) ने स्वयं अपना सर्वोच्च गौरव 'अल्लाह का बंदा होना' बताया (19:30)।",
                "चमत्कार 'अल्लाह की अनुमति से' होते हैं, जो सृष्टिकर्ता की शक्ति दर्शाते हैं।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(5, 114, 114, langMap("Send down a table spread as a sign & provision", "اللہم ربنا انزل علینا مائدۃ", "اللهم ربنا أنزل علينا مائدة من السماء", "ऐ अल्लाह हमारे लिए आसमान से दस्तरख़्वान उतार"))
        ),
        reliabilityNotes = langMap(
            "Anchored strictly in Quran 3:45-59, 4:157-158, 5:110-118, 19:16-36, 61:6 and Sahih al-Bukhari 3448.",
            "قرآن مجید اور صحیح بخاری (3448) سے مکمل طور پر مستند۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3448).",
            "क़ुरआन और सहीह बुख़ारी 3448 से सीधे प्रमाणित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "What were the first words infant Isa (AS) spoke from the cradle in Quran 19:30?",
                "قرآن (19:30) کے مطابق گہوارے میں حضرت عیسیٰؑ کا سب سے پہلا جملہ کیا تھا؟",
                "ما أول ما نطق به عيسى عليه السلام في المهد (مريم 19:30)؟",
                "क़ुरआन 19:30 के अनुसार पालने में ईसा (अ.स.) ने सबसे पहले क्या कहा?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("'Indeed, I am the servant of Allah (Abdullah)'", "'I am the ruler of Rome'", "'Look at the mountains'"),
                AppLanguage.URDU to listOf("'بے شک میں اللہ کا بندہ ہوں' (انی عبداللہ)", "'میں روم کا بادشاہ ہوں'", "'پہاڑوں کی طرف دیکھو'"),
                AppLanguage.ARABIC to listOf("قال إني عبد الله", "أنا ملك الروم", "انظروا إلى الجبال"),
                AppLanguage.HINDI to listOf("'निःसंदेह मैं अल्लाह का बंदा हूँ' (इन्नी अब्दुल्लाह)", "'मैं रोम का राजा हूँ'", "'पहाड़ों को देखो'")
            ),
            0,
            langMap(
                "Quran 19:30 preserves his exact words: 'He said: Indeed, I am the servant of Allah. He has given me the Scripture and made me a prophet.'",
                "سورۃ مریم 19:30: آپ نے فرمایا، میں اللہ کا بندہ ہوں، اس نے مجھے کتاب دی اور نبی بنایا۔",
                "مريم 19:30: قال إني عبد الله آتاني الكتاب وجعلني نبياً.",
                "सूरह मरयम 19:30 में उनके प्रथम शब्द 'मैं अल्लाह का बंदा हूँ' दर्ज हैं।"
            )
        )
    ),
    ProphetStory(
        id = "muhammad",
        orderNumber = 25,
        arabicName = "محمد ﷺ",
        names = langMap("Muhammad ﷺ", "حضرت محمد ﷺ", "محمد رسول الله ﷺ", "हज़रत मुहम्मद ﷺ"),
        titles = langMap("Seal of the Prophets & Mercy to the Worlds", "خاتم النبیین اور رحمۃ للعالمین ﷺ", "خاتم النبيين ورحمة للعالمين", "ख़ातमुन-नबिय्यीन व रहमतुल-लिल-आलमीन"),
        category = ProphetCategory.ULUL_AZM,
        summaryIntro = langMap(
            "Final Messenger and Seal of all Prophets (33:40), sent with the Noble Quran as mercy to all worlds (21:107) — embodying Al-Amin truthfulness, Makkan perseverance, Madinan justice, and universal brotherhood.",
            "خاتم النبیین ﷺ اور رحمۃ للعالمین (21:107، 33:40)، جن پر قرآن مجید نازل ہوا؛ الصادق الامین کے اخلاق، مکی صبر و استقامت، مدنی عدل و اخوت اور حجة الوداع کے عالمگیر پیغام کے حامل۔",
            "خاتم النبيين (الأحزاب 33:40) والرحمة المهداة للعالمين (الأنبياء 21:107)، أُنزل عليه القرآن الكريم، وبنى أمة التوحيد والعدل والإحسان والأخوة الإنسانية.",
            "अंतिम रसूल व ख़ातमुन-नबिय्यीन (33:40), जिन्हें समस्त संसार के लिए दया बनाकर भेजा गया (21:107); पवित्र क़ुरआन के वाहक, सत्यनिष्ठा, धैर्य, न्याय व मानव बंधुत्व के आदर्श।"
        ),
        detailedIntro = langMap(
            "Born in Makkah in the lineage of Ismail and Ibrahim (AS) and revered long before revelation as As-Sadiq Al-Amin (the Truthful, the Trustworthy), Muhammad ﷺ received the first revelation in Cave Hira ('Read in the name of your Lord...', 96:1-5). For thirteen years in Makkah he endured persecution with patience, calling peacefully to Tawhid and moral dignity, blessed with Al-Isra & Al-Mi'raj (17:1) where the five daily prayers were enjoined. After migrating to Madinah, he established brotherhood, the Charter of Madinah, mercy at the Conquest of Makkah, and the Farewell Pilgrimage affirming human equality and leaving humanity the Quran and Sunnah.",
            "مکہ مکرمہ میں حضرت اسماعیلؑ کی نسل میں ولادت ہوئی اور بعثت سے پہلے ہی 'الصادق الامین' کے لقب سے معروف تھے۔ غارِ حرا میں پہلی وحی 'اقرأ باسم ربک' (96:1-5) نازل ہوئی۔ مکہ میں تیرہ سال صبر و استقامت، معراج و پنجگانہ نماز کا تحفہ (17:1)، ہجرتِ مدینہ، مواخات و میثاقِ مدینہ، فتح مکہ پر عام معافی اور خطبۂ حجة الوداع میں مساواتِ انسانی کا ابدی پیغام آپ کی حیاتِ طیبہ کے روشن ابواب ہیں۔",
            "وُلد بمكة من ذرية إسماعيل عليه السلام وعُرف بالصادق الأمين قبل النبوة، ثم نزل عليه الوحي في غار حراء (العلق 96:1-5). صبر في مكة ثلاثة عشر عاماً، وأُسري به وعُرج ففُرضت الصلوات الخمس (الإسراء 17:1)، ثم هاجر إلى المدينة وأرسى المؤاخاة والعدل، وفتح مكة بعفو عام، وأوصى في حجة الوداع بحقوق الإنسان والتمسك بالقرآن والسنة.",
            "मक्का में इस्माईल (अ.स.) के वंश में जन्मे और नबूवत से पूर्व 'अस-सादिक़ अल-अमीन' कहलाए। ग़ार-ए-हिरा में प्रथम वह्य 'इक़रा...' (96:1-5) उतरी। मक्का में 13 वर्ष के धैर्य, मेराज में पाँच नमाज़ों के उपहार (17:1), मदीना में हिजरत व भाईचारा, मक्का विजय पर आम क्षमा और हज्जतुल-वदा के अंतिम उपदेश में मानव समानता का संदेश दिया।"
        ),
        quranicContext = langMap(
            "Addressed throughout the entire Quran, including Surah Muhammad (47), Al-Isra (17:1), Al-Ahzab (33:21, 33:40), Al-Anbiya (21:107), Al-Qalam (68:4), and Al-Alaq (96:1-5). (Also explore the dedicated Seerah module in More!)",
            "پورا قرآن مجید آپ پر نازل ہوا؛ بالخصوص سورۃ محمد (47)، بنی اسرائیل (17:1)، الاحزاب (33:21، 33:40)، الانبیاء (21:107)، القلم (68:4) اور العلق (96:1-5)۔ (مفصل تاریخ کے لیے 'سیرتِ نبی ﷺ' سیکشن بھی ملاحظہ فرمائیں)",
            "أنزل عليه القرآن كله، ومن أبرز الآيات في سيرته ورسالته: الإسراء (17:1) والأنبياء (21:107) والأحزاب (33:21، 40) والقلم (68:4) والعلق (96:1-5)، وتفصيلها في قسم السيرة النبوية بالتطبيق.",
            "संपूर्ण क़ुरआन उन पर उतारा गया; विशेषतः सूरह मुहम्मद (47), इसरा (17:1), अहज़ाब (33:21, 40), अंबिया (21:107), क़लम (68:4) व अलक़ (96:1-5)। (विस्तृत जीवनी ऐप के 'सीरत' सेक्शन में भी पढ़ें)"
        ),
        mainEvents = listLangMap(
            listOf(
                "First revelation at Cave Hira (Quran 96:1-5) and Makkan call to Tawhid and orphan/poor care.",
                "Al-Isra & Al-Mi'raj and gift of the five daily prayers (Quran 17:1; Bukhari 349).",
                "Hijrah to Madinah, building Masjid an-Nabawi, and brotherhood between Muhajirun and Ansar.",
                "Bloodless Conquest of Makkah with general amnesty, and the Farewell Sermon at Arafat (Muslim 1218a; Quran 5:3)."
            ),
            listOf(
                "غارِ حرا میں پہلی وحی (96:1-5) اور مکہ میں توحید و مساکین کی مدد کی دعوت۔",
                "واقعۂ معراج اور پانچ نمازوں کی فرضیت (17:1، بخاری 349)۔",
                "ہجرتِ مدینہ، مسجدِ نبوی کی تعمیر اور مہاجرین و انصار کے درمیان مواخات۔",
                "فتحِ مکہ پر بے مثال معافی اور خطبۂ حجة الوداع میں انسانی وقار کا اعلان (مسلم 1218a، 5:3)۔"
            ),
            listOf(
                "بدء الوحي في غار حراء (العلق 96:1-5) والدعوة إلى التوحيد ومكارم الأخلاق.",
                "الإسراء والمعراج وفرض الصلوات الخمس (الإسراء 17:1، البخاري 349).",
                "الهجرة إلى المدينة وبناء المسجد النبوي والمؤاخاة.",
                "فتح مكة بالعفو والتواضع وخطبة حجة الوداع (مسلم 1218a، المائدة 5:3)."
            ),
            listOf(
                "ग़ार-ए-हिरा में प्रथम वह्य (96:1-5) और मक्का में तौहीद व दया का संदेश।",
                "इसरा व मेराज तथा पाँच नमाज़ों की फ़र्ज़ियत (17:1, बुख़ारी 349)।",
                "मदीना हिजरत, मस्जिद-ए-नबवी का निर्माण और भाईचारा।",
                "मक्का विजय पर आम क्षमा और हज्जतुल-वदा का संदेश (मुस्लिम 1218a, 5:3)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Prophet Muhammad ﷺ, Khadijah (RA), Abu Bakr, Umar, Uthman, Ali (RA) & Sahabah", "Makkah, Cave Hira, Madinah, Masjid an-Nabawi, Masjid al-Aqsa"),
            listOf("نبی کریم ﷺ، امہات المومنین، خلفائے راشدین اور صحابہ کرامؓ", "مکہ، غارِ حرا، مدینہ منورہ، مسجدِ نبوی، مسجدِ اقصیٰ"),
            listOf("النبي محمد ﷺ وأمهات المؤمنين والصحابة الكرام", "مكة وحراء والمدينة والقدس الشريف"),
            listOf("पैग़ंबर मुहम्मद ﷺ, ख़दीजा (रज़ि.), चारों ख़लीफ़ा और सहाबा", "मक्का, हिरा, मदीना और मस्जिदुल-अक़्सा")
        ),
        verseRefs = listOf(
            ProphetVerseRef(21, 107, 107, langMap("Mercy to All Worlds", "تمام جہانوں کے لیے رحمت", "رحمة للعالمين", "समस्त संसार के लिए दया")),
            ProphetVerseRef(33, 21, 21, langMap("The Excellent Example", "بہترین اسوۂ حسنہ", "الأسوة الحسنة", "सर्वोत्तम आदर्श")),
            ProphetVerseRef(33, 40, 40, langMap("Seal of the Prophets", "خاتم النبیین ﷺ", "خاتم النبيين", "अंतिम नबी"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih Muslim 1218a",
                "https://sunnah.com/muslim:1218a",
                langMap(
                    "Farewell Pilgrimage sermon affirming sanctity of life, property, women's rights, brotherhood, and clinging to the Book of Allah.",
                    "صحیح مسلم (1218a): خطبۂ حجة الوداع میں جان و مال کے تحفظ، خواتین کے حقوق اور کتاب اللہ کو تھامے رکھنے کی وصیت۔",
                    "صحيح مسلم (1218a): خطبة حجة الوداع في حرمة الدماء والأموال والحقوق والتمسك بكتاب الله.",
                    "सहीह मुस्लिम 1218a: हज्जतुल-वदा में जान-माल की सुरक्षा, महिला अधिकार व क़ुरआन को थामे रखने की वसीयत।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Follow his comprehensive mercy, honesty, patience, prayer, and family kindness as an integrated life model (33:21).",
                "Hold steadfastly to the Quran and authentic Sunnah in worship and character."
            ),
            listOf(
                "عبادات کے ساتھ سچائی، امانت، صبر، رحمدلی اور حسنِ اخلاق کو اسوۂ حسنہ کے طور پر اپنانا (33:21)۔",
                "زندگی کے ہر موڑ پر قرآن اور مستند سنتِ نبوی ﷺ سے رہنمائی حاصل کرنا۔"
            ),
            listOf(
                "التأسي بالرسول ﷺ في رحمته وصدقه وصلاته وخلقه العظيم (الأحزاب 33:21).",
                "التمسك بالقرآن الكريم والسنة الصحيحة عصمة للمسلم."
            ),
            listOf(
                "ईमान, सत्य, करुणा, धैर्य और पारिवारिक स्नेह में उनके आदर्श का अनुसरण करना (33:21)।",
                "जीवन भर क़ुरआन और प्रामाणिक सुन्नत को थामे रखना।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(17, 80, 80, langMap("Cause me to enter a sound entrance & exit", "رب ادخلنی مدخل صدق", "رب أدخلني مدخل صدق وأخرجني مخرج صدق", "मुझे सच्चाई के साथ प्रवेश करा व सच्चाई से निकाल"))
        ),
        reliabilityNotes = langMap(
            "Anchored directly in the Noble Quran and Sahih al-Bukhari & Sahih Muslim. For 15 complete chronological chapters, open 'Seerah of Prophet Muhammad ﷺ' in More.",
            "قرآن کریم، صحیح بخاری اور صحیح مسلم سے مکمل مستند۔ مزید 15 تاریخی ابواب کے لیے 'سیرتِ نبی ﷺ' سیکشن دیکھیں۔",
            "مستند إلى القرآن الكريم والصحيحين؛ ولمطالعة 15 فصلاً تاريخياً مفصلاً راجع قسم السيرة النبوية.",
            "क़ुरआन, सहीह बुख़ारी व मुस्लिम से प्रमाणित। विस्तृत 15 अध्यायों हेतु 'सीरत-ए-नबी ﷺ' सेक्शन देखें।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "How does Surah Al-Anbiya (21:107) describe the mission of Prophet Muhammad ﷺ?",
                "سورۃ الانبیاء (21:107) میں حضرت محمد ﷺ کی بعثت کا مقصد کس عظیم لفظ سے بیان کیا گیا ہے؟",
                "كيف وصفت سورة الأنبياء (21:107) رسالة النبي محمد ﷺ؟",
                "सूरह अल-अंबिया (21:107) में पैग़ंबर मुहम्मद ﷺ को क्या बनाकर भेजे जाने का उल्लेख है?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("As a mercy to all the worlds (Rahmatan lil-'Alamin)", "Only for one city", "Only as a poet"),
                AppLanguage.URDU to listOf("تمام جہانوں کے لیے رحمت (رحمۃ للعالمین)", "صرف ایک شہر کے لیے", "صرف شاعر کے طور پر"),
                AppLanguage.ARABIC to listOf("رحمةً للعالمين", "لبلد واحد فقط", "شاعراً فقط"),
                AppLanguage.HINDI to listOf("समस्त संसार के लिए दया (रहमतुल-लिल-आलमीन)", "केवल एक नगर हेतु", "केवल कवि के रूप में")
            ),
            0,
            langMap(
                "Quran 21:107 proclaims: 'And We have not sent you, [O Muhammad], except as a mercy to the worlds.'",
                "الانبیاء 21:107: اور ہم نے آپ ﷺ کو تمام جہانوں کے لیے رحمت بنا کر ہی بھیجا ہے۔",
                "الأنبياء 21:107: وما أرسلناك إلا رحمة للعالمين.",
                "क़ुरआन 21:107 में उन्हें 'समस्त संसार के लिए दया' कहा गया है।"
            )
        )
    )
)
