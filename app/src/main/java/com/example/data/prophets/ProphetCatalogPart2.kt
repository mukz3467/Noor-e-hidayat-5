package com.example.data.prophets

import com.example.data.model.AppLanguage

internal val PROPHET_CATALOG_PART2: List<ProphetStory> = listOf(
    ProphetStory(
        id = "ismail",
        orderNumber = 8,
        arabicName = "إسماعيل عليه السلام",
        names = langMap("Ismail (AS)", "حضرت اسماعیل علیہ السلام", "إسماعيل عليه السلام", "हज़रत इस्माईल (अ.स.)"),
        titles = langMap("True to His Promise & Builder of the Ka'bah", "صادق الوعد اور معمارِ کعبہ", "صادق الوعد وباني الكعبة مع أبيه", "वादे के सच्चे और काबा के निर्माता"),
        category = ProphetCategory.HOUSE_OF_IBRAHIM,
        summaryIntro = langMap(
            "Eldest son of Ibrahim (AS), honored as 'true to his promise' (Sadiq al-Wa'd), who settled in Makkah with Hajar, submitted cheerfully to Allah's test, and helped raise the Ka'bah.",
            "حضرت ابراہیمؑ کے فرزند، صادق الوعد نبی جنہوں نے مکہ میں حضرت ہاجرہؑ کے ساتھ پرورش پائی، قربانی کے حکم پر سر تسلیم خم کیا اور کعبۃ اللہ کی تعمیر میں شریک رہے۔",
            "ابنه البكر لإبراهيم عليه السلام، نشأ في مكة عند بئر زمزم، امتثل لأمر الله في الرؤيا بصبر، وكان صادق الوعد ورفع قواعد البيت الحرام.",
            "इब्राहीम (अ.स.) के ज्येष्ठ पुत्र, वादे के सच्चे नबी, जो मक्का में ज़मज़म के पास पले, क़ुर्बानी में आज्ञाकारी रहे और काबा की नींव उठाई।"
        ),
        detailedIntro = langMap(
            "Placed as an infant with his mother Hajar in the barren valley of Makkah by Allah's command, Ismail (AS) saw the spring of Zamzam gush forth by mercy. Years later, when his father told him of the dream to sacrifice him, Ismail answered calmly: 'O my father, do as you are commanded; you will find me, if Allah wills, of the steadfast' (37:102). Allah redeemed him with a great sacrifice and honored him in rebuilding the Sacred House and commanding prayer and charity among his household (19:54-55).",
            "بچپن میں والدہ حضرت ہاجرہؑ کے ساتھ وادیِ غیر ذی زرع (مکہ) میں آباد ہوئے جہاں اللہ کی رحمت سے زمزم جاری ہوا۔ جوانی میں جب والد نے قربانی کے خواب کا ذکر کیا تو آپؑ نے فرمایا: ابا جان! جو حکم ملا ہے کر گزریے، ان شاءاللہ آپ مجھے صابر پائیں گے (37:102)۔ آپؑ نے والد کے ساتھ کعبہ کی بنیادیں اٹھائیں اور گھر والوں کو نماز و زکوٰۃ کا حکم دیتے رہے (19:54-55)۔",
            "نشأ في مكة حين فجّر الله له ولأمه هاجر ماء زمزم. ولما بلغ السعي استسلم لأمر الله في الذبح ففداه الله بذبح عظيم (الصافات 37:102-107). وصفه الله في القرآن بأنه كان صادق الوعد وكان يأمر أهله بالصلاة والزكاة وكان عند ربه مرضياً (مريم 19:54-55).",
            "मक्का की सूखी घाटी में माँ हाजरा के साथ बसे जहाँ ज़मज़म का चश्मा फूटा। युवा होने पर पिता के सपने को सुनकर धैर्यपूर्वक आज्ञा स्वीकार की (37:102), पिता संग काबा की नींव उठाई, और वे अपने वादे के सच्चे थे (19:54-55)।"
        ),
        quranicContext = langMap(
            "Narrated in Al-Baqarah (2:125-129), Maryam (19:54-55), Al-Anbiya (21:85), and As-Saffat (37:101-107).",
            "قرآن میں البقرہ (2:125-129)، مریم (19:54-55)، الانبیاء (21:85) اور الصافات (37:101-107) میں مذکور ہے۔",
            "ورد في البقرة (2:125-129) ومريم (19:54-55) والأنبياء (21:85) والصافات (37:101-107).",
            "बक़रा (2:125-129), मरयम (19:54-55), अंबिया (21:85) और साफ़्फ़ात (37:101-107) में वर्णन है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Settled at Makkah and blessed with the spring of Zamzam (Bukhari 3364, Quran 14:37).",
                "Willing submission to the trial of sacrifice and divine ransom (37:102-107).",
                "Raising the foundations of the Ka'bah and praying for a Messenger from his lineage (2:127-129).",
                "Honored as Sadiq al-Wa'd (True to His Promise) who taught prayer and zakah (19:54-55)."
            ),
            listOf(
                "مکہ میں قیام اور آبِ زمزم کا معجزہ (بخاری 3364، قرآن 14:37)۔",
                "قربانی کے حکم پر کامل تسلیم و رضا اور فدیہ (37:102-107)۔",
                "والد کے ساتھ خانہ کعبہ کی تعمیر اور امت کے لیے دعا (2:127-129)۔",
                "وعدے کا سچا ہونا اور اہلِ خانہ کو نماز و زکوٰۃ کی تلقین (19:54-55)۔"
            ),
            listOf(
                "الإسكان في مكة ونبع بئر زمزم (البخاري 3364، إبراهيم 14:37).",
                "طاعة والده في بلاء الذبح وفداؤه بذبح عظيم (الصافات 37:102-107).",
                "بناء الكعبة مع إبراهيم والدعاء (البقرة 2:127-129).",
                "صدق الوعد وأمر أهله بالصلاة والزكاة (مريم 19:54-55)."
            ),
            listOf(
                "मक्का में बसना और ज़मज़म का प्रकट होना (बुख़ारी 3364, क़ुरआन 14:37)।",
                "क़ुर्बानी के आदेश के प्रति विनम्र समर्पण (37:102-107)।",
                "इब्राहीम (अ.स.) संग काबा निर्माण (2:127-129)।",
                "वादे के सच्चे और नमाज़-ज़कात का आदेश देना (19:54-55)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Ismail (AS), Ibrahim (AS), Hajar (mother)", "Makkah, Zamzam well, Al-Bayt Al-Haram"),
            listOf("حضرت اسماعیلؑ، حضرت ابراہیمؑ، والدہ حضرت ہاجرہؑ", "مکہ مکرمہ، چاہِ زمزم، بیت اللہ"),
            listOf("إسماعيل وأبوه إبراهيم وأمه هاجر", "مكة وبئر زمزم والكعبة المشرفة"),
            listOf("इस्माईल (अ.स.), इब्राहीम (अ.स.), माता हाजरा", "मक्का, ज़मज़म और काबा")
        ),
        verseRefs = listOf(
            ProphetVerseRef(19, 54, 55, langMap("True to His Promise", "صادق الوعد اور نماز و زکوٰۃ", "صادق الوعد", "वादे के सच्चे")),
            ProphetVerseRef(37, 102, 107, langMap("The Trial of Submission", "قربانی اور صبر کی آزمائش", "بلاء الذبح والفداء", "क़ुर्बानी की परीक्षा"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3364",
                "https://sunnah.com/bukhari:3364",
                langMap(
                    "Describes Ismail's upbringing in Makkah, the tribe of Jurhum, and his father Ibrahim's visits culminating in rebuilding the Ka'bah.",
                    "مکہ میں حضرت اسماعیلؑ کی پرورش، قبیلہ جرہم اور کعبہ کی تعمیر کی مستند روایت۔",
                    "الحديث الصحيح في نشأة إسماعيل بمكة وقبيلة جرهم وبناء الكعبة.",
                    "मक्का में इस्माईल (अ.स.) की परवरिश और काबा निर्माण की प्रामाणिक हदीस।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Keeping promises is a prophetic mark of character (19:54).",
                "Youthful obedience to Allah and parents brings lasting spiritual honor."
            ),
            listOf(
                "وعدے کی پاسداری انبیائے کرام کا خاص شعار ہے (19:54)۔",
                "جوانی میں والدین کا ادب اور اللہ کی اطاعت ابدی شرف عطا کرتی ہے۔"
            ),
            listOf(
                "الوفاء بالوعد من أبرز صفات الأنبياء (مريم 19:54).",
                "البر والانقياد لأمر الله يورثان الذكر الحسن."
            ),
            listOf(
                "वादा निभाना नबियों के चरित्र की प्रमुख पहचान है (19:54)।",
                "अल्लाह और माता-पिता की आज्ञाकारिता स्थायी सम्मान दिलाती है।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(2, 127, 128, langMap("Our Lord, accept from us", "ربنا تقبل منا", "ربنا تقبل منا إنك أنت السميع العليم", "ऐ रब, हमसे स्वीकार फ़रमा"))
        ),
        reliabilityNotes = langMap(
            "Grounded in Quran 2:127-129, 19:54-55, 37:102-107 and Sahih al-Bukhari 3364.",
            "قرآن کریم اور صحیح بخاری (3364) پر مبنی۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3364).",
            "क़ुरआन और सहीह बुख़ारी 3364 पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "How does Surah Maryam (19:54) distinctively describe Ismail (AS)?",
                "سورۃ مریم (19:54) میں حضرت اسماعیلؑ کا کون سا امتیازی وصف بیان کیا گیا ہے؟",
                "بأي صفة مميزة وصف الله إسماعيل في سورة مريم (19:54)؟",
                "सूरह मरयम (19:54) में इस्माईल (अ.स.) का कौन-सा विशेष गुण बताया गया है?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("He was true to his promise (Sadiq al-Wa'd)", "He commanded the winds", "He was swallowed by a great fish"),
                AppLanguage.URDU to listOf("آپ وعدے کے سچے (صادق الوعد) تھے", "آپ کے لیے ہوائیں مسخر تھیں", "آپ کو مچھلی نے نگل لیا تھا"),
                AppLanguage.ARABIC to listOf("كان صادق الوعد", "سُخرت له الريح", "التقمه الحوت"),
                AppLanguage.HINDI to listOf("वे अपने वादे के सच्चे थे (सादिक़ुल-वाद)", "उनके लिए हवा वश में थी", "उन्हें मछली ने निगल लिया था")
            ),
            0,
            langMap(
                "Quran 19:54 explicitly says: 'Indeed, he was true to his promise, and he was a messenger and a prophet.'",
                "مریم 19:54 میں ہے: بے شک وہ وعدے کا سچا تھا اور رسول و نبی تھا۔",
                "قال تعالى: إنه كان صادق الوعد وكان رسولاً نبياً (مريم 19:54).",
                "क़ुरआन 19:54 में स्पष्ट है कि वे अपने वादे के सच्चे थे।"
            )
        )
    ),
    ProphetStory(
        id = "ishaq",
        orderNumber = 9,
        arabicName = "إسحاق عليه السلام",
        names = langMap("Ishaq (AS)", "حضرت اسحاق علیہ السلام", "إسحاق عليه السلام", "हज़रत इसहाक़ (अ.स.)"),
        titles = langMap("Blessed Son of Glad Tidings", "بشارت یافتہ اور بابرکت نبی", "النبي المبارك المبشَّر به", "शुभ समाचार प्राप्त नबी"),
        category = ProphetCategory.HOUSE_OF_IBRAHIM,
        summaryIntro = langMap(
            "Son of Ibrahim (AS) and Sarah, announced in their old age by angels as a gifted news of a knowledgeable boy, through whom came Yaqub (AS) and a noble line of prophets.",
            "حضرت ابراہیمؑ اور حضرت سارہؑ کے صاحبزادے جن کی ولادت کی بشارت بڑھاپے میں فرشتوں نے دی اور جن کی نسل سے بنی اسرائیل کے انبیاء آئے۔",
            "ابن إبراهيم وسارة عليهما السلام، بشّرت الملائكة بولادته وبابنه يعقوب في الكبر، ومن ذريته أنبياء بني إسرائيل.",
            "इब्राहीम (अ.स.) व सारा के पुत्र, जिनके जन्म की शुभ सूचना बुढ़ापे में फ़रिश्तों ने दी और जिनकी संतान से याक़ूब व अन्य नबी हुए।"
        ),
        detailedIntro = langMap(
            "Angels brought glad tidings to Ibrahim and Sarah of a knowledgeable son, Ishaq, and after him Yaqub (11:71, 51:28). Allah praised Ishaq (AS) for strength in worship and insight (38:45) and blessed him as a prophet among the righteous guiding people by Allah's command (21:72-73, 37:112-113).",
            "فرشتوں نے حضرت ابراہیمؑ اور سارہؑ کو ایک صاحبِ علم بیٹے اسحاقؑ اور ان کے بعد یعقوبؑ کی بشارت دی (11:71، 51:28)۔ قرآن نے آپ کو عبادت میں مضبوطی اور بصیرت (38:45) اور صالح نبی کے طور پر سراہا ہے (37:112)۔",
            "جاءت الملائكة بالبشارة لإبراهيم وسارة بغلام عليم هو إسحاق ومن ورائه يعقوب (هود 11:71، الذاريات 51:28). وأثنى الله عليه وعلى إبراهيم ويعقوب بأنهم أولو الأيدي والأبصار (ص 38:45).",
            "फ़रिश्तों ने इब्राहीम व सारा को ज्ञानी पुत्र इसहाक़ और उनके बाद याक़ूब की खुशखबरी दी (11:71, 51:28)। क़ुरआन ने उन्हें इबादत की शक्ति व अंतर्दृष्टि वाला नेक नबी बताया है (38:45, 37:112)।"
        ),
        quranicContext = langMap(
            "Mentioned in Hud (11:71), Al-Anbiya (21:72-73), As-Saffat (37:112-113), Sad (38:45-47), and Adh-Dhariyat (51:28-30).",
            "ہود (11:71)، الانبیاء (21:72-73)، الصافات (37:112-113) اور الذاریات (51:28-30) میں مذکور ہے۔",
            "ذُكر في هود (11:71) والأنبياء (21:72-73) والصافات (37:112-113) وص (38:45-47) والذاريات (51:28-30).",
            "सूरह 11:71, 21:72-73, 37:112-113, 38:45-47 और 51:28-30 में उल्लेख है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Miraculous glad tidings to Ibrahim and Sarah in their advanced age (11:71-73, 51:28-30).",
                "Gifted as a Prophet among the righteous (37:112) with strength in obedience and deep insight (38:45).",
                "Appointed among Imams guiding by Allah's command, establishing prayer and zakah (21:73)."
            ),
            listOf(
                "والدین کے بڑھاپے میں معجزانہ ولادت کی بشارت (11:71-73، 51:28-30)۔",
                "نیک نبی ہونے اور عبادت و بصیرت کی عطا کا بیان (37:112، 38:45)۔",
                "حکمِ الٰہی سے ہدایت کرنے والے پیشوا اور نماز و زکوٰۃ کے پابند (21:73)۔"
            ),
            listOf(
                "البشارة بولادته في كبر والديه وبابنه يعقوب (هود 11:71، الذاريات 51:28).",
                "النبوة والصلاح والقوة في الطاعة والبصيرة في الدين (الصافات 37:112، ص 38:45).",
                "جعلهم أئمة يهدون بأمر الله وإقام الصلاة وإيتاء الزكاة (الأنبياء 21:73)."
            ),
            listOf(
                "बुढ़ापे में चमत्कारी जन्म की शुभ सूचना (11:71-73, 51:28-30)।",
                "नेक नबी, भक्ति में शक्ति और गहरी समझ (37:112, 38:45)।",
                "अल्लाह के आदेश से मार्गदर्शन, नमाज़ और ज़कात की स्थापना (21:73)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Ishaq (AS), father Ibrahim (AS), mother Sarah, son Yaqub (AS)", "Sacred Land / Levant (Al-Ard al-Mubarakah)"),
            listOf("حضرت اسحاقؑ، والد ابراہیمؑ، والدہ سارہؑ، فرزند یعقوبؑ", "ارضِ مقدس / شام"),
            listOf("إسحاق ووالداه إبراهيم وسارة وابنه يعقوب", "الأرض المباركة (الشام)"),
            listOf("इसहाक़ (अ.स.), इब्राहीम, सारा, पुत्र याक़ूब", "पवित्र भूमि (शाम/फ़िलिस्तीन)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(11, 71, 73, langMap("Glad Tidings of Ishaq & Yaqub", "اسحاق و یعقوبؑ کی بشارت", "البشارة بإسحاق ويعقوب", "इसहाक़ और याक़ूब की खुशखबरी")),
            ProphetVerseRef(37, 112, 113, langMap("Blessed Prophet Among the Righteous", "نیک نبی کی بشارت", "بشارة الصالحين", "नेक नबी का आशीर्वाद"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3382",
                "https://sunnah.com/bukhari:3382",
                langMap(
                    "The Prophet ﷺ praised Prophet Yusuf (AS) as 'the noble one, son of the noble one (Yaqub), son of the noble one (Ishaq), son of the intimate friend of Allah (Ibrahim).'",
                    "صحیح بخاری (3382) میں نبی ﷺ نے فرمایا: کریم ابن کریم ابن کریم ابن کریم — یوسف بن یعقوب بن اسحاق بن ابراہیم علیہم السلام۔",
                    "قال النبي ﷺ: الكريم ابن الكريم ابن الكريم ابن الكريم يوسف بن يعقوب بن إسحاق بن إبراهيم.",
                    "सहीह बुख़ारी (3382) में नबी ﷺ ने यूसुफ़, याक़ूब, इसहाक़ और इब्राहीम (अ.स.) को 'करीम इब्ने करीम' बताया।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Nothing is too extraordinary for Allah's decree when He wills mercy.",
                "Consistent prayer, charity, and pure insight preserve faith across generations."
            ),
            listOf(
                "اللہ کے ارادے کے آگے کوئی سبب رکاوٹ نہیں بن سکتا۔",
                "نماز، زکوٰۃ اور بصیرت نسلوں میں ایمان کی حفاظت کرتے ہیں۔"
            ),
            listOf(
                "قدرة الله فوق الأسباب المعتادة حين يريد رحمة بعباده.",
                "إقام الصلاة وإيتاء الزكاة وحفظ الدين أساس بركة الذرية."
            ),
            listOf(
                "अल्लाह की इच्छा के समक्ष कोई भी कठिनाई असंभव नहीं।",
                "नमाज़, दान और सत्यनिष्ठा पीढ़ियों में ईमान को सुरक्षित रखती है।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Anchored strictly in Quran 11:71-73, 21:72-73, 37:112-113, 38:45-47 and Sahih al-Bukhari 3382.",
            "قرآن کریم اور صحیح بخاری (3382) سے مستند۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3382).",
            "क़ुरआन और सहीह बुख़ारी 3382 से प्रमाणित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Who were the parents of Prophet Ishaq (AS) named in the Quranic glad tidings (11:71)?",
                "قرآنی بشارت (11:71) کے مطابق حضرت اسحاقؑ کن کے صاحبزادے تھے؟",
                "مَن والدا نبي الله إسحاق في البشارة القرآنية (هود 11:71)؟",
                "क़ुरआनी शुभ सूचना (11:71) के अनुसार इसहाक़ (अ.स.) किनके पुत्र थे?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Ibrahim (AS) and his wife Sarah", "Zakariya (AS) and his wife", "Imran and his wife"),
                AppLanguage.URDU to listOf("حضرت ابراہیمؑ اور ان کی زوجہ سارہؑ", "حضرت زکریاؑ اور ان کی زوجہ", "عمران اور ان کی زوجہ"),
                AppLanguage.ARABIC to listOf("إبراهيم عليه السلام وزوجته سارة", "زكريا عليه السلام وزوجته", "عمران وزوجته"),
                AppLanguage.HINDI to listOf("इब्राहीम (अ.स.) और उनकी पत्नी सारा", "ज़करिया (अ.स.) और उनकी पत्नी", "इमरान और उनकी पत्नी")
            ),
            0,
            langMap(
                "Quran 11:71 records Ibrahim's wife laughing as the angels gave her glad tidings of Ishaq and after him Yaqub.",
                "سورۃ ہود 11:71 میں حضرت ابراہیمؑ کی زوجہ کو اسحاقؑ و یعقوبؑ کی بشارت دی گئی۔",
                "هود 11:71: فبشّرناها بإسحاق ومن وراء إسحاق يعقوب.",
                "सूरह हूद 11:71 में इब्राहीम की पत्नी को इसहाक़ व याक़ूब की खुशखबरी दी गई।"
            )
        )
    ),
    ProphetStory(
        id = "yaqub",
        orderNumber = 10,
        arabicName = "يعقوب عليه السلام",
        names = langMap("Yaqub (AS) / Isra'il", "حضرت یعقوب علیہ السلام", "يعقوب عليه السلام (إسرائيل)", "हज़रत याक़ूब (अ.स.)"),
        titles = langMap("Father of the Twelve Tribes & Beautiful Patience", "صبرِ جمیل والے نبی", "إسرائيل ذو الصبر الجميل", "सुंदर धैर्य (सब्र-ए-जमील) के नबी"),
        category = ProphetCategory.HOUSE_OF_IBRAHIM,
        summaryIntro = langMap(
            "Son of Ishaq (AS) and father of Yusuf (AS), whose decades of separation from his beloved son became an eternal model of Sabrun Jamil (beautiful patience) and unshaken hope in Allah.",
            "حضرت اسحاقؑ کے فرزند اور حضرت یوسفؑ کے والد، جن کی طویل جدائی 'صبرِ جمیل' اور رحمتِ الٰہی سے ناامید نہ ہونے کی عظیم مثال ہے۔",
            "ابن إسحاق ووالد يوسف عليهما السلام، ضرب أروع مثال في الصبر الجميل وحسن الظن بالله طوال سنوات الفراق.",
            "इसहाक़ (अ.स.) के पुत्र और यूसुफ़ (अ.स.) के पिता, जिनकी लंबी जुदाई 'सब्र-ए-जमील' और अल्लाह से आशा की अमर मिसाल है।"
        ),
        detailedIntro = langMap(
            "Known also as Isra'il (3:93, 19:58), Yaqub (AS) loved truth and nurtured twelve sons. When Yusuf disappeared and later Binyamin was detained in Egypt, Yaqub grieved deeply yet complained only to Allah (12:86), teaching his children never to despair of Allah's soothing mercy (12:87). Before passing away, he gathered his sons to confirm their lifelong covenant to worship Allah alone — the God of Ibrahim, Ismail, and Ishaq (2:133).",
            "آپؑ کا لقب اسرائیل تھا (3:93)۔ حضرت یوسفؑ اور پھر بنیامین کی جدائی میں شدید غم کے باوجود آپ نے اپنا دکھ صرف اللہ کے حضور پیش کیا (12:86) اور بیٹوں کو نصیحت کی کہ اللہ کی رحمت سے کبھی مایوس نہ ہونا (12:87)۔ وفات کے وقت بھی اولاد سے توحید پر قائم رہنے کا عہد لیا (2:133)۔",
            "لُقب بإسرائيل، واشتهر بصبره الجميل عند فراق يوسف ثم بنيامين، قائلاً: إنما أشكو بثي وحزني إلى الله (يوسف 12:86)، وموصياً بنيه ألا ييأسوا من روح الله (12:87)، وأوصى بنيه بالتوحيد عند الموت (البقرة 2:133).",
            "उनका एक नाम इस्राईल भी था (3:93)। यूसुफ़ व बिन्यामीन की जुदाई में उन्होंने अपना दुःख केवल अल्लाह के सामने रखा (12:86) और बेटों को अल्लाह की दया से निराश न होने का उपदेश दिया (12:87)। मृत्यु के समय भी तौहीद पर दृढ़ रहने की वसीयत की (2:133)।"
        ),
        quranicContext = langMap(
            "Featured in Al-Baqarah (2:132-133) and prominently throughout Surah Yusuf (12:4-101).",
            "البقرہ (2:132-133) اور مکمل سورۃ یوسف (12:4-101) میں نمایاں ذکر ہے۔",
            "ذُكر في البقرة (2:132-133) وبصورة رئيسية في سورة يوسف (12:4-101).",
            "सूरह बक़रा (2:132-133) और सूरह यूसुफ़ (12:4-101) में प्रमुखता से वर्णित।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Interpreting Yusuf's dream and cautioning him wisely (12:4-6).",
                "Enduring decades of separation with Sabrun Jamil while trusting Allah alone (12:18, 12:83, 12:86).",
                "Reunited with Yusuf in Egypt; sight restored through Yusuf's shirt by Allah's will (12:93-100).",
                "Deathbed covenant of pure monotheism with all his sons (2:133)."
            ),
            listOf(
                "حضرت یوسفؑ کے خواب کی تعبیر اور حکیمانہ نصیحت (12:4-6)۔",
                "طویل جدائی میں صبرِ جمیل اور اللہ سے قوی امید (12:83، 12:86)۔",
                "مصر میں یوسفؑ سے ملاقات اور بینائی کا لوٹ آنا (12:93-100)۔",
                "وفات کے وقت تمام بیٹوں سے خالص توحید کا عہد (2:133)۔"
            ),
            listOf(
                "رؤيا يوسف وإرشاده الحكيم (يوسف 12:4-6).",
                "الصبر الجميل وعدم اليأس من روح الله (يوسف 12:83-87).",
                "ارتداد بصره واجتماع الشمل في مصر (يوسف 12:93-100).",
                "وصيته لبنيه بالتوحيد الخالص عند الموت (البقرة 2:133)."
            ),
            listOf(
                "यूसुफ़ (अ.स.) के स्वप्न की बुद्धिमानी भरी सलाह (12:4-6)।",
                "लंबी जुदाई में सब्र-ए-जमील और अल्लाह से अटूट आशा (12:83-87)।",
                "मिस्र में पुनर्मिलन और आँखों की रोशनी लौटना (12:93-100)।",
                "मृत्यु के समय पुत्रों से तौहीद का वचन लेना (2:133)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Yaqub (AS), sons Yusuf, Binyamin, and their brothers", "Canaan (Al-Badw / Levant) and Egypt"),
            listOf("حضرت یعقوبؑ، فرزندان یوسفؑ، بنیامین اور بھائی", "کنعان اور مصر"),
            listOf("يعقوب وأبناؤه يوسف وبنيامين وإخوتهم", "أرض كنعان ومصر"),
            listOf("याक़ूब (अ.स.), पुत्र यूसुफ़, बिन्यामीन व भाई", "कनआन और मिस्र")
        ),
        verseRefs = listOf(
            ProphetVerseRef(12, 86, 87, langMap("Complaint to Allah Only & Hope", "صرف اللہ سے فریاد اور امید", "الشكوى إلى الله وعدم اليأس", "केवल अल्लाह से फ़रियाद व आशा")),
            ProphetVerseRef(2, 132, 133, langMap("Deathbed Covenant of Faith", "وفات کے وقت توحید کی وصیت", "وصية يعقوب عند الموت", "मृत्यु समय की वसीयत"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3382",
                "https://sunnah.com/bukhari:3382",
                langMap(
                    "Honored by the Prophet ﷺ in the noble prophetic lineage of Ibrahim, Ishaq, Yaqub, and Yusuf.",
                    "صحیح بخاری (3382) میں انبیاء کے کریم سلسلے میں ذکر۔",
                    "ذُكر في صحيح البخاري (3382) ضمن السلسلة النبوية الكريمة.",
                    "सहीह बुख़ारी 3382 में नबियों की पवित्र परंपरा में उल्लेख।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Grief does not contradict patience when one complains to Allah rather than losing hope.",
                "A parent's greatest concern for children is dying upon true faith (2:132-133)."
            ),
            listOf(
                "غم و الم صبر کے منافی نہیں بشرطیکہ فریاد اللہ سے ہو اور امید قائم رہے۔",
                "والدین کی سب سے بڑی فکر اولاد کے ایمان کی حفاظت ہونی چاہیے (2:132-133)۔"
            ),
            listOf(
                "الحزن الطبيعي لا ينافي الصبر الجميل إذا صاحبته الثقة بالله.",
                "أعظم همّ للوالدين ثبات الذرية على التوحيد."
            ),
            listOf(
                "सच्चा दुःख तब तक धैर्य के विरुद्ध नहीं जब तक आशा अल्लाह से जुड़ी हो।",
                "माता-पिता की सबसे बड़ी चिंता संतान के ईमान की सुरक्षा होनी चाहिए (2:132-133)।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Drawn directly from Quran Surahs 2, 12, and Sahih al-Bukhari 3382.",
            "قرآن مجید (البقرہ و یوسف) اور صحیح بخاری 3382 پر مبنی۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3382).",
            "क़ुरआन (बक़रा व यूसुफ़) और सहीह बुख़ारी 3382 पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "What did Yaqub (AS) advise his sons never to despair of in Quran 12:87?",
                "قرآن (12:87) میں حضرت یعقوبؑ نے اپنے بیٹوں کو کس چیز سے مایوس نہ ہونے کی نصیحت فرمائی؟",
                "مِمَّ نهى يعقوب عليه السلام بنيه عن اليأس منه (يوسف 12:87)؟",
                "क़ुरआन 12:87 में याक़ूब (अ.स.) ने बेटों को किससे निराश न होने को कहा?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Allah's soothing mercy (Rawh Allah)", "Wealth of Egypt", "Rain in summer"),
                AppLanguage.URDU to listOf("اللہ کی رحمت (رَوحِ الٰہی) سے", "مصر کی دولت سے", "گرمیوں کی بارش سے"),
                AppLanguage.ARABIC to listOf("من رَوح الله ورحمته", "من كنوز مصر", "من أمطار الصيف"),
                AppLanguage.HINDI to listOf("अल्लाह की दया (रौहिल्लाह) से", "मिस्र की संपत्ति से", "गर्मी की बारिश से")
            ),
            0,
            langMap(
                "Quran 12:87 teaches: 'Indeed, no one despairs of relief from Allah except the disbelieving people.'",
                "سورۃ یوسف 12:87: اللہ کی رحمت سے صرف کافر ہی مایوس ہوتے ہیں۔",
                "يوسف 12:87: ولا تيأسوا من رَوح الله إنه لا ييأس من رَوح الله إلا القوم الكافرون.",
                "सूरह यूसुफ़ 12:87: अल्लाह की दया से केवल काफ़िर ही निराश होते हैं।"
            )
        )
    ),
    ProphetStory(
        id = "yusuf",
        orderNumber = 11,
        arabicName = "يوسف عليه السلام",
        names = langMap("Yusuf (AS)", "حضرت یوسف علیہ السلام", "يوسف عليه السلام", "हज़रत यूसुफ़ (अ.स.)"),
        titles = langMap("Ahsan al-Qasas — The Best of Stories", "احسن القصص والے نبی", "صاحب أحسن القصص والصديق", "अहसनुल-क़सस (सर्वोत्तम कथा)"),
        category = ProphetCategory.HOUSE_OF_IBRAHIM,
        summaryIntro = langMap(
            "Revealed as 'the best of stories' (Ahsan al-Qasas), tracing Yusuf's journey from jealousy, the well, slavery, temptation, and prison to stewardship of Egypt and forgiving his brothers.",
            "قرآن میں آپ کے واقعے کو 'احسن القصص' کہا گیا؛ کنویں اور قید کی آزمائشوں سے گزر کر مصر کے منصب اور بھائیوں کو معاف کرنے تک عفت و صبر کی بے مثال داستان۔",
            "قصة كاملة في سورة يوسف وُصفت بـ(أحسن القصص)؛ من البئر والرق والسجن والفتنة إلى التمكين في مصر والعفو الكريم عن إخوته.",
            "सूरह यूसुफ़ को 'सर्वोत्तम कथा' कहा गया है; कुएँ, दासता और कारावास से मिस्र के शासन और भाइयों को क्षमा करने तक की प्रेरणादायी जीवनी।"
        ),
        detailedIntro = langMap(
            "After seeing eleven stars, the sun, and the moon bow to him in a dream (12:4), young Yusuf suffered his brothers' jealousy and was thrown into a well, sold into Egypt, and raised in a minister's home. Resisting fierce moral temptation out of fear of Allah (12:23), he chose prison rather than sin (12:33), preached Tawhid to fellow prisoners, interpreted the king's dream of seven lean years, saved the region through wise stewardship, and forgave his repentant brothers: 'No blame will there be upon you today' (12:92).",
            "بچپن میں گیارہ ستاروں، سورج اور چاند کو سجدہ کرتے دیکھا (12:4)۔ بھائیوں کے حسد سے کنویں میں ڈالے گئے اور مصر پہنچے۔ گناہ کی شدید آزمائش میں اللہ کے خوف سے پاکدامنی اختیار کی (12:23)، قید قبول کی اور قیدیوں کو توحید کی دعوت دی۔ بادشاہ کے خواب کی تعبیر سے ملک کو قحط سے بچایا اور آخر میں بھائیوں کو فراخ دلی سے معاف کر دیا (12:92)۔",
            "رأى أحد عشر كوكباً والشمس والقمر له ساجدين، فحسده إخوته وألقوه في الجب ثم بيع في مصر. استعصم بالله أمام الفتنة وآثر السجن، ودعا السجناء إلى التوحيد، وعبّر رؤيا الملك فأُسنِدت إليه خزائن الأرض، ثم عفا عن إخوته قائلاً: لا تثريب عليكم اليوم (يوسف 12:92).",
            "बचपन में ग्यारह तारे, सूर्य व चंद्रमा को सजदा करते देखा (12:4)। भाइयों ने ईर्ष्या से कुएँ में डाला और मिस्र में बेचे गए। प्रलोभन से ईमान बचाकर जेल चुनी (12:33), क़ैदियों को तौहीद सिखाई, राजा के स्वप्न का अर्थ बताकर अकाल से रक्षा की, और भाइयों को क्षमा कर दिया (12:92)।"
        ),
        quranicContext = langMap(
            "Narrated in complete continuous detail in Surah Yusuf (Surah 12:1-111) and mentioned in Al-An'am (6:84) and Ghafir (40:34).",
            "مکمل سورۃ یوسف (12:1-111) میں مسلسل بیان ہوا، نیز الانعام (6:84) اور غافر (40:34) میں ذکر ہے۔",
            "فُصّلت قصته كاملة في سورة يوسف (12:1-111)، وذُكر في الأنعام (6:84) وغافر (40:34).",
            "पूरी सूरह यूसुफ़ (12:1-111) में निरंतर वर्णन है, तथा 6:84 व 40:34 में उल्लेख है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Dream of eleven stars, the sun, and the moon (12:4); cast into the well by brothers (12:15).",
                "Upholding chastity in Egypt and preferring prison over sin (12:23-35).",
                "Preaching pure Tawhid in prison and interpreting the king's seven-year dream (12:39-49).",
                "Honest governance saving Egypt from famine, forgiving his brothers, and reuniting his family (12:55, 12:92-100)."
            ),
            listOf(
                "خوابِ نبوت اور بھائیوں کا کنویں میں ڈالنا (12:4، 12:15)۔",
                "عفت و پاکدامنی کی حفاظت اور گناہ پر قید کو ترجیح (12:23-35)۔",
                "قید خانے میں توحید کی دعوت اور بادشاہ کے خواب کی تعبیر (12:39-49)۔",
                "مصر کو قحط سے بچانا، بھائیوں کو معاف کرنا اور خاندان کا ملاپ (12:55، 12:92-100)۔"
            ),
            listOf(
                "الرؤيا وإلقاؤه في الجب (يوسف 12:4-15).",
                "العفة أمام الفتنة وتفضيل السجن على المعصية (12:23-35).",
                "الدعوة في السجن وتفسير رؤيا الملك (12:39-49).",
                "الأمانة في التدبير والعفو عن الإخوة واجتماع العائلة (12:55، 12:92-100)."
            ),
            listOf(
                "सपना देखना और भाइयों द्वारा कुएँ में डालना (12:4, 15)।",
                "प्रलोभन में पवित्रता की रक्षा और कारावास स्वीकारना (12:23-35)।",
                "जेल में तौहीद का संदेश और राजा के सपने की व्याख्या (12:39-49)।",
                "अकाल में कुशल प्रबंधन, भाइयों को क्षमा व परिवार का मिलन (12:55, 92-100)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Yusuf (AS), Yaqub (AS), Binyamin, the brothers, Aziz & King of Egypt", "Well (Jubb), Egypt (Misr)"),
            listOf("حضرت یوسفؑ، حضرت یعقوبؑ، بنیامین، بھائی، عزیزِ مصر", "کنواں اور مصر"),
            listOf("يوسف ويعقوب وبنيامين والإخوة وعزيز مصر وملكها", "الجب ومصر"),
            listOf("यूसुफ़, याक़ूब, बिन्यामीन, भाई, मिस्र का राजा", "कुआँ और मिस्र")
        ),
        verseRefs = listOf(
            ProphetVerseRef(12, 23, 24, langMap("Steadfast Chastity", "عفت و تقویٰ کا نمونہ", "الاستعصام والعفة", "पवित्रता व संयम")),
            ProphetVerseRef(12, 90, 92, langMap("Taqwa, Sabr & Noble Forgiveness", "تقویٰ، صبر اور معافی", "التقوى والصبر والعفو", "तक़वा, सब्र और क्षमा")),
            ProphetVerseRef(12, 101, 101, langMap("Closing Dua of Yusuf (AS)", "حضرت یوسفؑ کی اختتامی دعا", "دعاء يوسف بالوفاة على الإسلام", "यूसुफ़ (अ.स.) की अंतिम दुआ"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3382",
                "https://sunnah.com/bukhari:3382",
                langMap(
                    "The Prophet ﷺ honored Yusuf (AS) as 'the noble son of the noble...' emphasizing his lofty character.",
                    "صحیح بخاری (3382) میں نبی اکرم ﷺ نے حضرت یوسفؑ کو 'کریم ابن کریم' فرمایا۔",
                    "في صحيح البخاري (3382) أثنى النبي ﷺ على يوسف بن يعقوب بن إسحاق بن إبراهيم.",
                    "सहीह बुख़ारी 3382 में नबी ﷺ ने यूसुफ़ (अ.स.) की महानता की प्रशंसा की।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Whoever holds onto Taqwa and Sabr, Allah never lets their reward be lost (12:90).",
                "Forgiving when one holds complete power is the pinnacle of prophetic nobility (12:92)."
            ),
            listOf(
                "جو تقویٰ اور صبر اختیار کرے اللہ اس کا اجر ضائع نہیں فرماتا (12:90)۔",
                "اختیار و طاقت ملنے پر معاف کر دینا اعلیٰ ترین اخلاقی بلندی ہے (12:92)۔"
            ),
            listOf(
                "إن من يتقِ ويصبر فإن الله لا يضيع أجر المحسنين (يوسف 12:90).",
                "العفو عند المقدرة من أسمى أخلاق الأنبياء (يوسف 12:92)."
            ),
            listOf(
                "तक़वा व सब्र रखने वाले का प्रतिफल अल्लाह कभी नष्ट नहीं करता (12:90)।",
                "शक्ति होते हुए भी क्षमा कर देना श्रेष्ठतम चरित्र है (12:92)।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(12, 101, 101, langMap("Cause me to die as a Muslim & join the righteous", "توفنی مسلماً والحقنی بالصالحین", "توفني مسلماً وألحقني بالصالحين", "मुझे मुस्लिम अवस्था में मृत्यु दे और नेकों में मिला"))
        ),
        reliabilityNotes = langMap(
            "Drawn faithfully from Surah Yusuf (12) and Sahih al-Bukhari 3382.",
            "سورۃ یوسف (12) اور صحیح بخاری (3382) سے مستند۔",
            "مستند مباشرة إلى سورة يوسف وصحيح البخاري (3382).",
            "सूरह यूसुफ़ (12) और सहीह बुख़ारी 3382 पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "According to Quran 12:90, which two virtues did Yusuf (AS) credit for Allah's favor?",
                "قرآن (12:90) میں حضرت یوسفؑ نے اللہ کے فضل کا سبب کن دو صفات کو قرار دیا؟",
                "ما الصفتان اللتان ذكرهما يوسف في قوله: إنه من يتقِ ويصبر (12:90)؟",
                "क़ुरआन 12:90 में यूसुफ़ (अ.स.) ने किन दो गुणों से सफलता मिलने की बात कही?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Taqwa (God-consciousness) and Sabr (Patience)", "Wealth and military force", "Royalty and secrecy"),
                AppLanguage.URDU to listOf("تقویٰ اور صبر", "دولت اور فوجی طاقت", "شاہی نسب اور رازداری"),
                AppLanguage.ARABIC to listOf("التقوى والصبر", "المال والجند", "المُلك والكتمان"),
                AppLanguage.HINDI to listOf("तक़वा (संयम) और सब्र (धैर्य)", "संपत्ति और सेना", "राजवंश और गोपनीयता")
            ),
            0,
            langMap(
                "Quran 12:90 explicitly states: 'Indeed, he who fears Allah and is patient, then indeed, Allah does not allow to be lost the reward of those who do good.'",
                "سورۃ یوسف 12:90 میں تقویٰ اور صبر کا صراحت سے ذکر ہے۔",
                "يوسف 12:90: إنه من يتق ويصبر فإن الله لا يضيع أجر المحسنين.",
                "क़ुरआन 12:90 में तक़वा और सब्र का स्पष्ट उल्लेख है।"
            )
        )
    ),
    ProphetStory(
        id = "ayyub",
        orderNumber = 12,
        arabicName = "أيوب عليه السلام",
        names = langMap("Ayyub (AS)", "حضرت ایوب علیہ السلام", "أيوب عليه السلام", "हज़रत अय्यूब (अ.स.)"),
        titles = langMap("Model of Patience in Affliction", "صبر و شکر کے پیکر", "نبي الصبر والرجوع إلى الله", "कठिनाई में धैर्य की मिसाल"),
        category = ProphetCategory.EARLY_NATIONS,
        summaryIntro = langMap(
            "Tested severely in health, family, and wealth, Ayyub (AS) turned to Allah in humble supplication, and Allah restored his health with a cool spring and doubled his family in mercy.",
            "صحت، مال اور اولاد کی کڑی آزمائشوں میں صبر و رضا کا پیکر بنے رہے۔ آپؑ کی عاجزانہ دعا پر اللہ نے ٹھنڈے چشمے سے شفا عطا فرمائی اور نعمتیں دگنی لوٹا دیں۔",
            "ابتُلي في جسده وأهله وماله فصبر وتضرع إلى الله: مسني الضر وأنت أرحم الراحمين، فشفاه الله بماء بارد وردّ له أهله ومثلهم معهم.",
            "स्वास्थ्य, धन और परिवार की कठिन परीक्षाओं में धैर्यवान रहे; उनकी विनम्र दुआ पर अल्लाह ने शीतल झरने से स्वास्थ्य और दुगुना परिवार लौटा दिया।"
        ),
        detailedIntro = langMap(
            "When profound affliction touched him, Ayyub (AS) spoke with utmost reverence toward Allah, saying simply: 'Indeed, adversity has touched me, and You are the Most Merciful of the merciful' (21:83). Allah instructed him to strike the ground with his foot — a cool spring for bathing and drinking gushed forth (38:42), healing him completely and restoring his household twice over as a mercy and reminder for worshippers (21:84, 38:43-44).",
            "طویل بیماری اور آزمائش کے باوجود حضرت ایوبؑ کی زبان سے شکوہ نہیں بلکہ نہایت ادب سے یہ دعا نکلی: بے شک مجھے تکلیف پہنچی ہے اور تو سب رحم کرنے والوں سے بڑھ کر رحم والا ہے (21:83)۔ حکمِ الٰہی سے پاؤں مارنے پر ٹھنڈا چشمہ جاری ہوا جس سے شفا ملی اور اللہ نے رحمت کے طور پر آپ کے اہل و عیال کو دوگنا لوٹا دیا (38:42-43)۔",
            "دعا ربه بأدب بالغ: أني مسني الضر وأنت أرحم الراحمين (الأنبياء 21:83)، فأمره الله: اركض برجلك هذا مغتسل بارد وشراب (ص 38:42)، فأبرأه الله وآتاه أهله ومثلهم معهم رحمةً وذكرى للعابدين.",
            "कठिन पीड़ा में भी उन्होंने अत्यंत आदर से दुआ की: 'मुझे कष्ट ने आ घेरा है और तू सबसे बढ़कर दया करने वाला है' (21:83)। अल्लाह के आदेश से पैर मारने पर शीतल झरना फूटा जिससे वे स्वस्थ हुए और दुगुना परिवार मिला (38:42-43)।"
        ),
        quranicContext = langMap(
            "Mentioned in An-Nisa (4:163), Al-An'am (6:84), Al-Anbiya (21:83-84), and Sad (38:41-44).",
            "النساء (4:163)، الانعام (6:84)، الانبیاء (21:83-84) اور ص (38:41-44) میں مذکور ہے۔",
            "ذُكر في النساء (4:163) والأنعام (6:84) والأنبياء (21:83-84) وص (38:41-44).",
            "सूरह 4:163, 6:84, 21:83-84 और 38:41-44 में वर्णित।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Severe trial affecting health, wealth, and family met with steadfastness (38:41, 38:44).",
                "Humble supplication without bitterness (21:83, 38:41).",
                "Healing cool spring for bathing and drinking (38:42).",
                "Restoration of health and family twofold as a mercy from Allah (21:84, 38:43)."
            ),
            listOf(
                "جسم، مال اور اہل و عیال کی آزمائش میں ثابت قدمی (38:41، 38:44)۔",
                "کامل ادب کے ساتھ اللہ کے حضور دعا (21:83)۔",
                "ٹھنڈے چشمے سے غسل اور پینے کے ذریعے شفا (38:42)۔",
                "صحت اور دوگنے اہل و عیال کی واپسی (21:84، 38:43)۔"
            ),
            listOf(
                "الصبر على البلاء الشديد (ص 38:41، 44).",
                "الدعاء المليء بالأدب والرجاء (الأنبياء 21:83).",
                "الشفاء بالمغتسل البارد والشراب (ص 38:42).",
                "ردّ الأهل ومثلهم معهم رحمة وذكرى (الأنبياء 21:84، ص 38:43)."
            ),
            listOf(
                "कठिन परीक्षा में अडिग धैर्य (38:41, 44)।",
                "विनम्र दुआ बिना किसी शिकायत के (21:83)।",
                "शीतल झरने से स्नान व पान द्वारा उपचार (38:42)।",
                "अल्लाह की दया से दुगुना परिवार लौटाया जाना (21:84, 38:43)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Prophet Ayyub (AS) and his faithful spouse", "No specific city is named in the Quran"),
            listOf("حضرت ایوبؑ اور آپ کی وفادار زوجہ", "قرآن میں کسی مخصوص شہر کا نام متعین نہیں"),
            listOf("أيوب عليه السلام وزوجه الصابرة", "لم يحدد القرآن اسماً لبلده"),
            listOf("अय्यूब (अ.स.) व उनकी धैर्यवती पत्नी", "क़ुरआन में किसी विशेष नगर का नाम निर्दिष्ट नहीं")
        ),
        verseRefs = listOf(
            ProphetVerseRef(21, 83, 84, langMap("Supplication & Mercy", "دعائے ایوبؑ اور رحمتِ الٰہی", "دعاء أيوب والاستجابة", "दुआ और अल्लाह की दया")),
            ProphetVerseRef(38, 41, 44, langMap("Cool Spring & Praised Servant", "ٹھنڈا چشمہ اور نعم البدل", "المغتسل البارد ونعم العبد", "शीतल झरना व उत्तम बंदा"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 279",
                "https://sunnah.com/bukhari:279",
                langMap(
                    "While Ayyub (AS) bathed after healing, golden locusts showered upon him by Allah's grace, illustrating limitless divine generosity.",
                    "صحیح بخاری (279) میں شفا کے بعد حضرت ایوبؑ پر سونے کی ٹڈیوں کے نزول اور اللہ کی نعمت کا ذکر ہے۔",
                    "في صحيح البخاري (279) قصة جراد الذهب الذي أمطره الله على أيوب بعد شفائه دلالة على فضل الله.",
                    "सहीह बुख़ारी 279 में स्वस्थ होने के बाद सोने की टिड्डियों की वर्षा द्वारा अल्लाह की कृपा का उल्लेख है।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Turning to Allah in pain without despair or resentment is the essence of Sabr.",
                "Praising someone as 'an excellent servant constantly turning to Allah' (38:44) is the highest divine tribute."
            ),
            listOf(
                "تکلیف میں شکوہ شکایت کے بجائے اللہ کی طرف رجوع ہی اصل صبر ہے۔",
                "قرآن کا لقب 'نعم العبد انّہ اوّاب' (بہترین رجوع کرنے والا بندہ) سب سے بڑا اعزاز ہے۔"
            ),
            listOf(
                "التضرع إلى الله وقت الشدة بأدب هو حقيقة الصبر.",
                "الثناء الإلهي (نعم العبد إنه أواب) غاية الفضل."
            ),
            listOf(
                "पीड़ा में शिकायत के बिना अल्लाह को पुकारना ही सच्चा धैर्य है।",
                "'निअ्मल-अब्दु इन्नहु अव्वाब' (उत्तम व बार-बार लौटने वाला बंदा) सर्वोच्च प्रशंसा है।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(21, 83, 83, langMap("Adversity has touched me & You are Most Merciful", "انی مسنی الضر وانت ارحم الراحمین", "أني مسني الضر وأنت أرحم الراحمين", "मुझे कष्ट पहुँचा है और तू परम दयालु है"))
        ),
        reliabilityNotes = langMap(
            "Based on Quran 21:83-84, 38:41-44 and Sahih al-Bukhari 279. Speculative durations or unverified details outside authentic texts are omitted.",
            "قرآن کریم (21:83-84، 38:41-44) اور صحیح بخاری (279) سے مستند۔ غیر مصدقہ مدتوں اور روایات سے گریز کیا گیا ہے۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (279) دون إيراد القصص غير الثابتة عن مدة البلاء ونحوها.",
            "क़ुरआन (21:83-84, 38:41-44) व सहीह बुख़ारी 279 पर आधारित; अपुष्ट समय-अवधि कथाएँ नहीं जोड़ी गई हैं।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "What did Allah command Ayyub (AS) to do for healing in Quran 38:42?",
                "قرآن (38:42) میں اللہ نے حضرت ایوبؑ کو شفا کے لیے کیا حکم فرمایا؟",
                "بماذا أمر الله أيوب عليه السلام عند شفائه (ص 38:42)؟",
                "क़ुरआन 38:42 में अल्लाह ने अय्यूब (अ.स.) को स्वास्थ्य-लाभ के लिए क्या आदेश दिया?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Strike the ground with his foot for a cool spring", "Climb Mount Tur", "Build a stone tower"),
                AppLanguage.URDU to listOf("پاؤں زمین پر ماریں تاکہ ٹھنڈا چشمہ نکلے", "کوہِ طور پر چڑھیں", "پتھر کا مینار بنائیں"),
                AppLanguage.ARABIC to listOf("أن يركض برجله ليخرج مغتسل بارد وشراب", "أن يصعد الطور", "أن يبني صرحاً"),
                AppLanguage.HINDI to listOf("पैर से ज़मीन पर मारें जिससे शीतल झरना निकले", "तूर पर्वत पर चढ़ें", "पत्थर की मीनार बनाएँ")
            ),
            0,
            langMap(
                "Quran 38:42 commands: 'Strike [the ground] with your foot; this is a [spring for] a cool bath and drink.'",
                "سورۃ ص 38:42 میں حکم ہے کہ زمین پر پاؤں ماریں، یہ ٹھنڈا پانی نہانے اور پینے کے لیے ہے۔",
                "ص 38:42: اركض برجلك هذا مغتسل بارد وشراب.",
                "क़ुरआन 38:42 में शीतल स्नान व पीने के जल हेतु पैर मारने का आदेश है।"
            )
        )
    ),
    ProphetStory(
        id = "shuayb",
        orderNumber = 13,
        arabicName = "شعيب عليه السلام",
        names = langMap("Shu'ayb (AS)", "حضرت شعیب علیہ السلام", "شعيب عليه السلام", "हज़रत शुऐब (अ.स.)"),
        titles = langMap("Preacher of Economic Justice in Madyan", "دیانت داری اور عدلِ تجارت کے داعی", "نبي أهل مدين وداعية الأمانة في الميزان", "मदयन के नबी व व्यापारिक न्याय के संदेशवाहक"),
        category = ProphetCategory.EARLY_NATIONS,
        summaryIntro = langMap(
            "Sent to Madyan and the People of the Thicket (Aykah) to call them to worship Allah alone and abolish cheating in weights, measures, and commerce.",
            "آپ کو مدین اور اصحاب الایکہ کی طرف بھیجا گیا تاکہ انہیں توحید کے ساتھ ناپ تول میں کمی اور معاشی بددیانتی چھوڑنے کی دعوت دیں۔",
            "أرسله الله إلى مدين وأصحاب الأيكة ليدعوهم إلى عبادة الله وإيفاء الكيل والميزان وترك التطفيف والفساد.",
            "मदयन व अयकह के लोगों की ओर भेजे गए ताकि वे तौहीद अपनाएँ और नाप-तौल में बेईमानी व आर्थिक अन्याय छोड़ें।"
        ),
        detailedIntro = langMap(
            "Madyan's prosperity bred arrogance and commercial fraud — giving short measure and blocking fair trade (7:85, 11:84-85). Shu'ayb (AS) appealed to them with remarkable eloquence: 'Does your prayer command you that we leave what our fathers worshipped or do not do with our wealth what we will?' they mocked (11:87). He answered: 'I only intend reform as much as I am able, and my success is not but through Allah' (11:88). Rejecting reform, they were overtaken by the earthquake and the Day of the Shadow (26:189), while Shu'ayb and the believers were saved.",
            "مدین کے لوگ خوشحال تھے مگر ناپ تول میں کمی اور راستوں میں ظلم کرتے تھے (11:84-85)۔ حضرت شعیبؑ نے انہیں توحید اور معاشی انصاف کی تلقین فرمائی اور فرمایا: میں تو اپنی استطاعت کے مطابق صرف اصلاح چاہتا ہوں اور میری توفیق اللہ ہی سے ہے (11:88)۔ انکار پر زلزلہ اور سائبان کے دن کا عذاب ان پر آیا (26:189)۔",
            "كان أهل مدين يبخسون الناس أشياءهم وينقصون المكيال والميزان. دعاهم شعيب بحكمة: إن أريد إلا الإصلاح ما استطعت وما توفيقي إلا بالله (هود 11:88). فلما أصروا على البغي أخذتهم الرجفة وعذاب يوم الظلة، ونجّى الله شعيباً والمؤمنين.",
            "मदयन के लोग व्यापार में नाप-तौल कम करते थे (11:84-85)। शुऐब (अ.स.) ने नम्रता से कहा: 'मैं तो अपनी क्षमता भर सुधार ही चाहता हूँ और मेरी सफलता केवल अल्लाह से है' (11:88)। अस्वीकार करने पर भूकंप व छाया के दिन की सज़ा आई और ईमानवाले बच निकले।"
        ),
        quranicContext = langMap(
            "Narrated in Al-A'raf (7:85-93), Hud (11:84-95), Ash-Shu'ara (26:176-191), and Al-Ankabut (29:36-37).",
            "الاعراف (7:85-93)، ہود (11:84-95)، الشعراء (26:176-191) اور العنکبوت (29:36-37) میں مفصل بیان ہے۔",
            "ورد في الأعراف (7:85-93) وهود (11:84-95) والشعراء (26:176-191) والعنكبوت (29:36-37).",
            "आराफ़ (7:85-93), हूद (11:84-95), शुअरा (26:176-191) और अनकबूत (29:36-37) में वर्णित।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Preaching Tawhid and full, honest weights and measures (7:85, 11:84-85).",
                "Rejecting mockery that separated prayer from public business ethics (11:87).",
                "Declaring sincere commitment to reform reliant solely upon Allah (11:88).",
                "Earthquake and Day of the Shadow overtake the rejecters; believers are saved (7:91, 26:189)."
            ),
            listOf(
                "توحید اور پورے ناپ تول و امانت داری کی دعوت (7:85، 11:84-85)۔",
                "نماز کو معاشی معاملات سے الگ سمجھنے کے طعنے کا جواب (11:87)۔",
                "'میں استطاعت بھر اصلاح چاہتا ہوں' کا عظیم اعلان (11:88)۔",
                "عذابِ رجفہ اور یومِ ظلہ میں مجرموں کی تباہی اور مومنین کی نجات (7:91، 26:189)۔"
            ),
            listOf(
                "الدعوة إلى التوحيد وإيفاء الكيل والميزان بالقسط (الأعراف 7:85، هود 11:84).",
                "رفض الفصل بين الصلاة والأمانة المالية (هود 11:87).",
                "بيان منهج الإصلاح: إن أريد إلا الإصلاح ما استطعت (هود 11:88).",
                "عذاب الرجفة ويوم الظلة ونجاة المؤمنين (الأعراف 7:91، الشعراء 26:189)."
            ),
            listOf(
                "तौहीद व ईमानदार नाप-तौल की शिक्षा (7:85, 11:84-85)।",
                "इबादत को व्यापारिक नैतिकता से अलग मानने के उपहास का उत्तर (11:87)।",
                "'मेरा उद्देश्य केवल सुधार है' की घोषणा (11:88)।",
                "भूकंप व छाया-दिन के दंड से रक्षा (7:91, 26:189)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Prophet Shu'ayb (AS) and believing followers", "People of Madyan & Al-Aykah (People of the Thicket)"),
            listOf("حضرت شعیبؑ اور اہلِ ایمان", "اہلِ مدین اور اصحاب الایکہ"),
            listOf("شعيب عليه السلام والمؤمنون", "أهل مدين وأصحاب الأيكة"),
            listOf("हज़रत शुऐब (अ.स.) व ईमानवाले", "मदयन और अयकह के लोग")
        ),
        verseRefs = listOf(
            ProphetVerseRef(11, 84, 88, langMap("Honest Trade & Tawfiq Billah", "تجارت میں عدل اور توفیقِ الٰہی", "العدل في الميزان ومنهج الإصلاح", "व्यापारिक न्याय और सुधार की भावना")),
            ProphetVerseRef(7, 85, 89, langMap("Invocation for True Judgment", "ناپ تول اور دعائے فیصلہ", "القسط والدعاء بالفتح", "सत्य निर्णय की दुआ"))
        ),
        hadithRefs = emptyList(),
        lessons = listLangMap(
            listOf(
                "True worship transforms everyday business honesty, weights, and public fairness.",
                "What remains lawful and blessed by Allah is far better than dishonest gain (11:86)."
            ),
            listOf(
                "سچی عبادت انسان کے کاروبار، ناپ تول اور معاملات میں دیانت پیدا کرتی ہے۔",
                "اللہ کا دیا ہوا حلال رزق ناجائز منافع سے کہیں بہتر ہے (11:86)۔"
            ),
            listOf(
                "العبادة الحقيقية تشمل الأمانة في التجارة والمعاملات المالية.",
                "ما أبقى الله من الرزق الحلال خير من الربح الحرام (هود 11:86)."
            ),
            listOf(
                "सच्ची इबादत व्यापार और दैनिक व्यवहार में ईमानदारी लाती है।",
                "अल्लाह का दिया हलाल रिज़्क़ बेईमानी के लाभ से कहीं बेहतर है (11:86)।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(7, 89, 89, langMap("Our Lord, decide between us and our people in truth", "ربنا افتح بیننا وبین قومنا بالحق", "ربنا افتح بيننا وبين قومنا بالحق", "ऐ हमारे रब, हमारे व क़ौम के बीच सत्य फ़ैसला कर"))
        ),
        reliabilityNotes = langMap(
            "Drawn directly from Quran 7:85-93, 11:84-95, 26:176-191, and 29:36-37. Scholarly opinions vary on whether the elderly pious father in 28:25-27 was Shu'ayb (AS) or another righteous man of Madyan, so this detail is not overstated as definitive.",
            "قرآن کریم (7، 11، 26، 29) سے ماخوذ۔ سورۃ القصص (28:25) کے بزرگ کے حضرت شعیبؑ ہونے یا نہ ہونے میں علماء کی آراء مختلف ہیں، اس لیے اسے حتمی قرار نہیں دیا گیا۔",
            "مستند إلى القرآن الكريم (7، 11، 26، 29). ويُنبه إلى اختلاف المفسرين في هل شيخ مدين في سورة القصص هو شعيب عليه السلام أم غيره.",
            "क़ुरआन (7, 11, 26, 29) पर आधारित; सूरह क़सस (28:25) के बुज़ुर्ग के शुऐब (अ.स.) होने या न होने में विद्वानों के मतभेद हैं।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Which ethical sin did Shu'ayb (AS) repeatedly confront alongside idolatry in Madyan?",
                "حضرت شعیبؑ نے شرک کے ساتھ اپنی قوم کو کس معاشرتی و معاشی برائی سے منع فرمایا؟",
                "ما الفساد الاقتصادي الذي نهى عنه شعيب عليه السلام أهل مدين؟",
                "शुऐब (अ.स.) ने तौहीद के साथ किस आर्थिक बुराई को छोड़ने का उपदेश दिया?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Cheating in weights and measures", "Refusing to build boats", "Arguing about eclipses"),
                AppLanguage.URDU to listOf("ناپ تول میں کمی اور معاشی بددیانتی", "کشتیاں بنانے سے انکار", "چاند گرہن پر بحث"),
                AppLanguage.ARABIC to listOf("التطفيف في المكيال والميزان", "رفض بناء السفن", "الجدال حول الكسوف"),
                AppLanguage.HINDI to listOf("नाप-तौल में कमी और व्यापारिक बेईमानी", "नाव न बनाना", "ग्रहण पर विवाद")
            ),
            0,
            langMap(
                "Quran 11:84-85 emphasizes giving full measure and weight with justice and not depriving people of their due.",
                "ہود 11:84-85 میں ناپ تول پورا کرنے اور لوگوں کو ان کا حق پورا دینے کی تاکید ہے۔",
                "هود 11:84-85: ولا تنقصوا المكيال والميزان وأوفوا الكيل بالقسط.",
                "क़ुरआन 11:84-85 में पूरा नाप-तौल देने का स्पष्ट आदेश है।"
            )
        )
    ),
    ProphetStory(
        id = "musa",
        orderNumber = 14,
        arabicName = "موسى عليه السلام",
        names = langMap("Musa (AS)", "حضرت موسیٰ علیہ السلام", "موسى عليه السلام", "हज़रत मूसा (अ.स.)"),
        titles = langMap("Kalimullah — Most Mentioned Prophet in the Quran", "کلیم اللہ اور اولوالعزم پیغمبر", "كليم الله ورسوله إلى فرعون وبني إسرائيل", "कलीमुल्लाह (सबसे अधिक वर्णित नबी)"),
        category = ProphetCategory.ULUL_AZM,
        summaryIntro = langMap(
            "Mentioned more frequently by name in the Quran than any other prophet. Chosen directly by Allah at Tur, entrusted with the Torah, and sent with his brother Harun to confront Pharaoh's tyranny.",
            "قرآن میں سب سے زیادہ بار مذکور پیغمبر اور کلیم اللہ۔ کوہِ طور پر شرفِ ہمکلامی عطا ہوا، حضرت ہارونؑ کے ساتھ فرعون کے ظلم کا مقابلہ کیا اور بنی اسرائیل کو نجات دلائی۔",
            "أكثر الأنبياء ذكراً في القرآن، وكليم الله الذي كلّمه ربه بالوادي المقدس، وأرسله مع أخيه هارون إلى فرعون ونجّى بني إسرائيل وأُنزلت عليه التوراة.",
            "क़ुरआन में सर्वाधिक वर्णित नबी व कलीमुल्लाह। तूर पर अल्लाह से संवाद का सम्मान पाया, हारून (अ.स.) के साथ फ़िरऔन का सामना किया और तौरात प्राप्त हुई।"
        ),
        detailedIntro = langMap(
            "Protected as an infant floating down the Nile and raised inside Pharaoh's very palace under Allah's care (20:39, 28:7-13), Musa (AS) later spent years in Madyan before receiving prophethood at the Sacred Valley of Tuwa (20:9-14). Strengthened by his brother Harun (AS) and nine clear signs, he confronted Pharaoh, led Bani Isra'il safely across the parted sea (26:62-66), received revelation on Mount Tur, and sought wisdom during his journey with Khidr (18:60-82).",
            "بچپن میں دریا کی موجوں سے حفاظت فرما کر اللہ نے فرعون ہی کے محل میں پرورش کروائی (28:7-13)۔ مدین میں قیام کے بعد وادیِ مقدس طویٰ میں نبوت اور شرفِ ہمکلامی عطا ہوا (20:9-14)۔ حضرت ہارونؑ کی معاونت اور نو روشن نشانیوں کے ساتھ فرعون کو دعوت دی، سمندر شق ہوا (26:62-66)، تورات عطا ہوئی اور حضرت خضرؑ کے ساتھ سفرِ حکمت کا واقعہ پیش آیا (18:60-82)۔",
            "حفظه الله في التابوت ونشأ في قصر فرعون تحت عين الله (طه 20:39، القصص 28:7-13). وبعد إقامته بمدين ناداه الله بالوادي المقدس طوى وأيده بأخيه هارون، فدعوا فرعون بآيات بينات حتى انفلق البحر ونجا بنو إسرائيل (الشعراء 26:62-66) وأُوتي التوراة.",
            "बचपन में संदूक़ में नदी से बचाकर फ़िरऔन के महल में पले (28:7-13)। मदयन के बाद पवित्र घाटी तुवा में नबूवत मिली (20:9-14)। हारून (अ.स.) संग फ़िरऔन के सामने सत्य रखा, समुद्र फटा और बनी इस्राईल बचे (26:62-66), तौरात प्राप्त हुई व ख़िद्र (अ.स.) के साथ ज्ञान-यात्रा हुई (18:60-82)।"
        ),
        quranicContext = langMap(
            "Spans numerous Surahs including Al-Baqarah (2:49-74), Al-A'raf (7:103-162), Al-Kahf (18:60-82), Taha (20:9-98), Ash-Shu'ara (26:10-68), and Al-Qasas (28:3-46).",
            "البقرہ (2:49-74)، الاعراف (7:103-162)، الکہف (18:60-82)، طٰہٰ (20:9-98) اور القصص (28:3-46) میں مفصل بیان۔",
            "في سور البقرة (2:49-74) والأعراف (7:103-162) والكهف (18:60-82) وطه (20:9-98) والقصص (28:3-46).",
            "बक़रा (2:49-74), आराफ़ (7:103-162), कहफ़ (18:60-82), ताहा (20:9-98) और क़सस (28:3-46) में विस्तृत वर्णन।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Infant deliverance in the Nile and return to his nursing mother inside Pharaoh's palace (28:7-13).",
                "Direct call to prophethood in the Sacred Valley of Tuwa; command to establish Salah (20:11-14).",
                "Deliverance across the parted sea and drowning of Pharaoh's army (26:61-66).",
                "Revelation of the Torah on Mount Tur and the journey of humility with Al-Khidr (7:142-145, 18:60-82)."
            ),
            listOf(
                "صندوق میں دریا بردگی اور اللہ کے وعدے کے مطابق ماں کی آغوش میں واپسی (28:7-13)۔",
                "وادیِ مقدس طویٰ میں ہمکلامی اور اقامتِ صلوٰۃ کا حکم (20:11-14)۔",
                "سمندر میں راستہ بننا اور فرعون و لشکر کا غرق ہونا (26:61-66)۔",
                "کوہِ طور پر تورات کا نزول اور حضرت خضرؑ کے ساتھ حکمت کا سفر (7:142-145، 18:60-82)۔"
            ),
            listOf(
                "حفظه في اليم وردّه إلى أمه في قصر فرعون (القصص 28:7-13).",
                "تكليم الله له في طوى والأمر بإقامة الصلاة (طه 20:11-14).",
                "فلق البحر ونجاة بني إسرائيل وغرق فرعون (الشعراء 26:61-66).",
                "نزول التوراة على الطور ورحلة العلم مع الخضر (الأعراف 7:142، الكهف 18:60-82)."
            ),
            listOf(
                "नदी में रक्षा और माँ के पास वापसी (28:7-13)।",
                "पवित्र घाटी तुवा में अल्लाह से संवाद व नमाज़ का आदेश (20:11-14)।",
                "समुद्र का फटना और फ़िरऔन की सेना का डूबना (26:61-66)।",
                "तूर पर तौरात की प्राप्ति और ख़िद्र (अ.स.) संग विनम्रता की यात्रा (18:60-82)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Musa (AS), Harun (AS), Pharaoh, Asiyah (believing wife of Pharaoh), Al-Khidr", "Egypt (Nile), Madyan, Sacred Valley of Tuwa / Mount Tur"),
            listOf("حضرت موسیٰؑ، حضرت ہارونؑ، فرعون، حضرت آسیہؑ، حضرت خضرؑ", "مصر (نیل)، مدین، وادیِ طویٰ و کوہِ طور"),
            listOf("موسى وهارون وفرعون وآسية امرأة فرعون والخضر", "مصر ومدين ووادي طوى والطور"),
            listOf("मूसा (अ.स.), हारून (अ.स.), फ़िरऔन, आसिया, ख़िद्र", "मिस्र (नील), मदयन, तुवा व तूर पर्वत")
        ),
        verseRefs = listOf(
            ProphetVerseRef(20, 11, 14, langMap("Call at Tuwa & Command of Salah", "وادی طویٰ اور اقامتِ نماز", "النداء في طوى وإقامة الصلاة", "तुवा में पुकार और नमाज़ का आदेश")),
            ProphetVerseRef(20, 25, 28, langMap("Dua: Expand My Chest", "رب اشرح لی صدری", "رب اشرح لي صدري", "सीना खोलने की दुआ")),
            ProphetVerseRef(26, 62, 66, langMap("My Lord is With Me", "بے شک میرا رب میرے ساتھ ہے", "كلا إن معي ربي", "निश्चय ही मेरा रब मेरे साथ है"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3407",
                "https://sunnah.com/bukhari:3407",
                langMap(
                    "Full authentic narration of Musa (AS) and Al-Khidr emphasizing humility in seeking knowledge.",
                    "صحیح بخاری (3407) میں حضرت موسیٰؑ اور حضرت خضرؑ کا علم کے لیے عاجزی پر مبنی مفصل واقعہ۔",
                    "الحديث الطويل في صحيح البخاري (3407) لقصة موسى والخضر في التواضع لطلب العلم.",
                    "सहीह बुख़ारी 3407 में ज्ञान के प्रति विनम्रता सिखाने वाली मूसा व ख़िद्र की हदीस।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Say 'Kalla! Indeed, with me is my Lord; He will guide me' (26:62) even when hemmed in on all sides.",
                "Even the greatest scholar stays humble before Allah's boundless wisdom."
            ),
            listOf(
                "ہر طرف سے گھِر جانے پر بھی 'ان معی ربی سیھدین' کا یقین راستے کھول دیتا ہے (26:62)۔",
                "علم جتنا بھی زیادہ ہو، انسان کو اللہ کی حکمت کے سامنے متواضع رہنا چاہیے۔"
            ),
            listOf(
                "اليقين بالله في أشد الأزمات: كلا إن معي ربي سيهدين (الشعراء 26:62).",
                "التواضع في طلب العلم من أعظم سمات الأنبياء."
            ),
            listOf(
                "संकट में भी 'निःसंदेह मेरा रब मेरे साथ है' (26:62) का विश्वास राह खोलता है।",
                "ज्ञान कितना भी हो, विनम्रता सबसे बड़ा आभूषण है।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(20, 25, 28, langMap("Expand my chest & ease my task", "رب اشرح لی صدری و یسر لی امری", "رب اشرح لي صدري ويسر لي أمري", "मेरा सीना खोल दे और काम आसान कर दे")),
            ProphetDuaRef(28, 24, 24, langMap("My Lord, I am in need of whatever good You send me", "رب انی لما انزلت الی من خیر فقیر", "رب إني لما أنزلت إلي من خير فقير", "जो भलाई तू मेरी ओर उतारे मैं उसका मोहताज हूँ"))
        ),
        reliabilityNotes = langMap(
            "Grounded directly in Quran Surahs 2, 7, 18, 20, 26, 28 and Sahih al-Bukhari 3407.",
            "قرآن کریم اور صحیح بخاری (3407) سے مکمل طور پر مستند۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3407).",
            "क़ुरआन और सहीह बुख़ारी 3407 से सीधे प्रमाणित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "With what words of certainty did Musa (AS) calm his people before the sea parted (Quran 26:62)?",
                "سمندر شق ہونے سے پہلے حضرت موسیٰؑ نے کس یقین بھرے جملے سے اپنی قوم کو تسلی دی (26:62)؟",
                "ما كلمة اليقين التي قالها موسى لقومه قبل فلق البحر (الشعراء 26:62)؟",
                "समुद्र फटने से पहले मूसा (अ.स.) ने किन शब्दों से अपनी क़ौम को भरोसा दिलाया (26:62)?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("'No! Indeed, with me is my Lord; He will guide me'", "'Turn back and surrender'", "'Wait for next year'"),
                AppLanguage.URDU to listOf("'ہرگز نہیں! بے شک میرا رب میرے ساتھ ہے، وہ مجھے راستہ دکھائے گا'", "'واپس لوٹ جاؤ'", "'اگلے سال تک انتظار کرو'"),
                AppLanguage.ARABIC to listOf("كلا إن معي ربي سيهدين", "ارجعوا واستسلموا", "انتظروا عاماً آخر"),
                AppLanguage.HINDI to listOf("'कदापि नहीं! निःसंदेह मेरा रब मेरे साथ है, वह मेरा मार्गदर्शन करेगा'", "'लौट जाओ'", "'अगले साल प्रतीक्षा करो'")
            ),
            0,
            langMap(
                "Quran 26:62 records Musa's unwavering declaration: 'Kalla! Indeed, with me is my Lord; He will guide me.'",
                "الشعراء 26:62: ہرگز نہیں، بے شک میرا رب میرے ساتھ ہے، وہ میری رہنمائی فرمائے گا۔",
                "الشعراء 26:62: قال كلا إن معي ربي سيهدين.",
                "क़ुरआन 26:62 में मूसा (अ.स.) का यह अटल विश्वास दर्ज है।"
            )
        )
    )
)
