package com.example.data.seerah

internal object SeerahCatalogPart3b {
    val articles: List<SeerahArticle> = listOf(
        SeerahArticle(
            id = "seerah_13_battle_khaybar",
            era = SeerahEra.BATTLES_EXPEDITIONS,
            orderIndex = 13,
            title = localized4(
                "Expedition of Khaybar (7 AH)",
                "غزوہ خیبر (7 ہجری)",
                "غزوة خيبر (7 هـ)",
                "ख़ैबर का अभियान (7 हिजरी)"
            ),
            approxChronology = localized4(
                "Muharram – Safar, 7 AH (~May–June 628 CE).",
                "محرم تا صفر 7 ہجری (مئی–جون 628 عیسوی)۔",
                "المحرم – صفر سنة 7 هـ (نحو مايو 628م).",
                "मुहर्रम – सफ़र, 7 हिजरी (मई 628 ई.)।"
            ),
            overview = localized4(
                "Khaybar's fortified oases north of Madinah had become a staging hub for hostilities alongside Quraysh during the Trench coalition. With Hudaybiyyah securing peace to the south, the Prophet ﷺ marched to Khaybar with the companions of Al-Ridwan. After days of siege at Al-Qamus, the banner was handed to Ali ibn Abi Talib رضي الله عنه with instructions first to invite the defenders peacefully to Islam and good terms. Afterward, the Khaybar farmers agreed to remain on their lands under a 50% crop-sharing agreement.",
                "خیبر کے قلعے غزوہ خندق میں مدینہ پر حملہ آوروں کا بڑا مرکز رہے تھے۔ حدیبیہ سے جنوبی امن کے بعد آپ ﷺ شرکائے بیعتِ رضوان کے ساتھ خیبر تشریف لے گئے۔ قلعہ قموص کے محاصرے کے بعد علم حضرت علیؓ کو عطا فرمایا اور پہلے صلح و اسلام کی دعوت دینے کا حکم دیا۔ فتح کے بعد کاشتکاروں کو نصف پیداوار پر زمینوں پر برقرار رکھا گیا۔",
                "كانت حصون خيبر مركز تحريض وتجمع مع الأحزاب، فتوجه إليها ﷺ مع أهل الحديبية، وأعطى الراية علياً رضي الله عنه آمراً إياه بدعوتهم أولاً. وبعد الصلح أقرّ أهلها على زراعة الأرض بنصف الثمر.",
                "ख़ैबर के किले अहज़ाब युद्ध में मदीना विरोधी गठजोड़ का केंद्र बने थे। हुदैबिया की शांति के बाद नबी ﷺ रिज़वान के साथियों संग ख़ैबर गए। अंतिम किले पर ध्वज हज़रत अली को देते हुए पहले शांतिपूर्ण आमंत्रण का निर्देश दिया गया। बाद में किसानों को आधा उपज साझा करने की संधि पर वहीं खेती जारी रखने दी गई।"
            ),
            contextPeoplePlaces = localized4(
                "People: Ali ibn Abi Talib, Ja'far ibn Abi Talib (returned from Abyssinia), Abd Allah ibn Rawahah. Places: Khaybar oasis (~150 km north of Madinah).",
                "شخصیات: حضرت علیؓ، حضرت جعفر طیارؓ (حبشہ سے واپسی)، عبداللہ بن رواحہؓ۔ مقامات: وادیِ خیبر۔",
                "الشخصيات: علي بن أبي طالب، جعفر بن أبي طالب، عبد الله بن رواحة. الأماكن: حصون خيبر شمال المدينة.",
                "व्यक्ति: अली, जाफ़र (हबशा से लौटे), अब्दुल्लाह बिन रवाहा। स्थान: ख़ैबर के किले।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Book of Expeditions)",
                    referenceCode = "Sahih al-Bukhari 4210",
                    arabicQuote = "فَوَاللَّهِ لَأَنْ يَهْدِيَ اللَّهُ بِكَ رَجُلًا وَاحِدًا خَيْرٌ لَكَ مِنْ أَنْ يَكُونَ لَكَ حُمْرُ النَّعَمِ",
                    quoteTranslation = localized4(
                        "By Allah, if Allah guides even one person through you, that is better for you than possessing red camels.",
                        "اللہ کی قسم! اگر اللہ تمہارے ذریعے ایک شخص کو بھی ہدایت دے دے تو یہ تمہارے لیے سرخ اونٹوں سے بہتر ہے۔",
                        "فوالله لأن يهدي الله بك رجلاً واحداً خير لك من أن يكون لك حمر النعم.",
                        "अल्लाह की क़सम! यदि अल्लाह तुम्हारे माध्यम से एक व्यक्ति को भी सीधा मार्ग दिखा दे तो यह तुम्हारे लिए लाल ऊँटों से भी बेहतर है।"
                    ),
                    reliabilityNote = localized4(
                        "Muttafaqun Alayh (Bukhari 4210; Muslim 2406) stating the priority of peaceful guidance over combat.",
                        "صحیح بخاری (4210) و مسلم کی متفق علیہ حدیث جو جنگ پر ہدایت و امن کو ترجیح دیتی ہے۔",
                        "حديث متفق عليه (البخاري 4210) يقدم الهداية والسلم على القتال.",
                        "बुख़ारी (4210) और मुस्लिम की प्रामाणिक हदीस जो शांति और मार्गदर्शन को प्राथमिकता देती है।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Historians chronicle slight variations in the precise order in which individual Khaybar fortresses (Na'im, Al-Sa'b, Al-Qamus) fell.",
                "مؤرخین میں خیبر کے مختلف قلعوں کی فتح کی جزئی ترتیب میں معمولی اختلاف ملتا ہے۔",
                "تتنوع تفاصيل ترتيب فتح الحصون الفردية في خيبر بين المؤرخين.",
                "ख़ैबर के अलग-अलग किलों के क्रम में इतिहासकारों के बीच हल्का अंतर मिलता है।"
            ),
            lessonsToday = localized4(
                "Even in conflict, guiding a single heart sincerely outweighs material spoils, and agricultural contracts built on clear mutual consent ensure stability.",
                "اختلاف و کشمکش میں بھی ایک انسان کی خیرخواہی مادی فوائد سے بڑھ کر ہے، اور باہمی رضا مندی کے معاہدے معاشرتی استحکام لاتے ہیں۔",
                "إن هداية إنسان واحد بالخير أعظم من الغنائم، والعقود الواضحة تحقق الاستقرار الاقتصادي.",
                "संघर्ष में भी एक व्यक्ति की भलाई भौतिक लाभ से ऊपर है, और स्पष्ट आपसी समझौते स्थिरता लाते हैं।"
            ),
            nextMilestoneNote = localized4(
                "Next: The Peaceful Conquest of Makkah (8 AH).",
                "اگلا مرحلہ: فتحِ مکہ (8 ہجری)۔",
                "المرحلة التالية: فتح مكة المكرمة (8 هـ).",
                "अगला पड़ाव: मक्का विजय (8 हिजरी)।"
            ),
            referenceLinks = listOf(
                "Sahih al-Bukhari 4210 (Sunnah.com)" to "https://sunnah.com/bukhari:4210"
            ),
            keywords = listOf("khaybar", "ali", "bukhari 4210", "red camels", "jafar", "7 ah")
        ),
        SeerahArticle(
            id = "seerah_14_conquest_makkah",
            era = SeerahEra.BATTLES_EXPEDITIONS,
            orderIndex = 14,
            title = localized4(
                "Conquest of Makkah & Universal Amnesty",
                "فتحِ مکہ اور عام معافی کا تاریخی اعلان",
                "فتح مكة المكرمة والعفو العام",
                "मक्का विजय और आम क्षमा की ऐतिहासिक घोषणा (8 हिजरी)"
            ),
            approxChronology = localized4(
                "Ramadan, 8 AH (~January 630 CE).",
                "رمضان 8 ہجری (جنوری 630 عیسوی)۔",
                "رمضان سنة 8 هـ (نحو يناير 630م).",
                "रमज़ान, 8 हिजरी (जनवरी 630 ई.)।"
            ),
            overview = localized4(
                "When Quraysh's allies broke the Treaty of Hudaybiyyah by attacking Banu Khuza'ah inside the sanctuary, the Prophet ﷺ marched quietly with 10,000 companions toward Makkah so bloodshed might be avoided. Entering with his head bowed in deep humility onto his mount while reciting Surah Al-Fath, he entered Makkah nearly bloodlessly, cleared the Ka'bah of idols declaring 'Truth has come and falsehood has vanished' (17:81), and asked his former persecutors: 'What do you think I will do to you?' Receiving hope in his nobility, he proclaimed: 'No blame upon you today; go, for you are free.'",
                "جب قریش کے حلیفوں نے صلحِ حدیبیہ توڑ کر بنو خزاعہ پر حملہ کیا تو آپ ﷺ دس ہزار صحابہؓ کے ساتھ نہایت خاموشی سے مکہ روانہ ہوئے تاکہ خونریزی نہ ہو۔ فتح کے دن آپ ﷺ عاجزی سے سر جھکائے، سورۃ الفتح کی تلاوت کرتے ہوئے داخل ہوئے۔ کعبہ کو بتوں سے پاک فرمایا اور مکہ کے سابق دشمنوں کو عام معافی دیتے ہوئے فرمایا: 'آج تم پر کوئی ملامت نہیں، جاؤ تم سب آزاد ہو'۔",
                "بعد نقض حلفاء قريش لصلح الحديبية، سار النبي ﷺ بعشرة آلاف مقاتل بسرية لتفادي القتال، فدخل مكة مطأطئ الرأس تواضعاً وهو يقرأ سورة الفتح، وطهّر الكعبة من الأصنام، ثم أعلن العفو العام لأهل مكة: «اذهبوا فأنتم الطلقاء».",
                "जब कुरैश के सहयोगियों ने हुदैबिया की संधि तोड़ी, तो नबी ﷺ ने रक्तपात टालने के लिए 10,000 साथियों संग गुप्त रूप से कूच किया। विजय के समय विनम्रता से सिर झुकाए, सूरह फ़तह पढ़ते हुए मक्का में प्रवेश किया, काबा को मूर्तियों से मुक्त कराया और वर्षों के सताने वालों को आम माफ़ी दे दी: 'आज तुम पर कोई दोष नहीं, जाओ तुम सब स्वतंत्र हो।'"
            ),
            contextPeoplePlaces = localized4(
                "People: Abu Sufyan ibn Harb, Al-Abbas, Bilal ibn Rabah (calling Adhan atop the Ka'bah), Khalid ibn al-Walid, Uthman ibn Talhah. Places: Marr al-Zahran, Makkah, Al-Masjid al-Haram.",
                "شخصیات: ابوسفیانؓ، حضرت عباسؓ، حضرت بلالؓ (کعبہ کی چھت پر اذان)، عثمان بن طلحہؓ۔ مقامات: مکہ مکرمہ، مسجدِ حرام۔",
                "الشخصيات: أبو سفيان، العباس، بلال (الأذان على الكعبة)، عثمان بن طلحة. الأماكن: مكة المكرمة والمسجد الحرام.",
                "व्यक्ति: अबू सुफ़ियान, अब्बास, बिलाल (काबा पर अज़ान), उस्मान बिन तल्हा। स्थान: मक्का, मस्जिदुल-हराम।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Isra 17:81 & Surah An-Nasr 110)",
                    referenceCode = "Quran 17:81; 110:1-3",
                    arabicQuote = "وَقُلْ جَاءَ الْحَقُّ وَزَهَقَ الْبَاطِلُ ۚ إِنَّ الْبَاطِلَ كَانَ زَهُوقًا",
                    quoteTranslation = localized4(
                        "And say, 'Truth has come, and falsehood has departed. Indeed falsehood, by nature, is ever bound to depart.'",
                        "اور فرما دیجیے کہ حق آ گیا اور باطل مٹ گیا، بے شک باطل تو مٹنے والا ہی ہے۔",
                        "وَقُلْ جَاءَ الْحَقُّ وَزَهَقَ الْبَاطِلُ إِنَّ الْبَاطِلَ كَانَ زَهُوقًا",
                        "और कह दीजिए: 'सत्य आ गया और असत्य मिट गया; निःसंदेह असत्य तो मिटने वाला ही है।'"
                    ),
                    reliabilityNote = localized4(
                        "Sahih al-Bukhari 4287 records the Prophet ﷺ reciting Quran 17:81 while clearing the Ka'bah.",
                        "صحیح بخاری (4287) میں فتحِ مکہ کے موقع پر اس آیت کی تلاوت ثابت ہے۔",
                        "ثبت في البخاري (4287) تلاوته لهذه الآية يوم الفتح.",
                        "सहीह बुख़ारी (4287) में मक्का विजय पर इस आयत के पाठ का प्रामाणिक प्रमाण।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_2_CLASSICAL_SEERAH,
                    sourceName = "Sirat Ibn Hisham / Al-Bayhaqi",
                    referenceCode = "Sermon at the Ka'bah Door ('Go, you are free')",
                    arabicQuote = "لَا تَثْرِيبَ عَلَيْكُمُ الْيَوْمَ، اذْهَبُوا فَأَنْتُمُ الطُّلَقَاءُ",
                    quoteTranslation = localized4(
                        "What do you think I will do to you? They said: 'Good — a noble brother and son of a noble brother.' He replied: 'No blame upon you today; go, for you are free.'",
                        "تمہارا کیا خیال ہے میں تمہارے ساتھ کیا کروں گا؟ انہوں نے کہا: بھلائی، آپ کریم بھائی اور کریم بھائی کے بیٹے ہیں۔ فرمایا: آج تم پر کوئی سرزنش نہیں، جاؤ تم سب آزاد ہو۔",
                        "ما تظنون أني فاعل بكم؟ قالوا: خيراً أخ كريم وابن أخ كريم. قال: اذهبوا فأنتم الطلقاء.",
                        "तुम्हारा क्या विचार है मैं तुम्हारे साथ क्या करूँगा? उन्होंने कहा: भलाई। आपने कहा: आज तुम पर कोई दोष नहीं, जाओ तुम सब आज़ाद हो।"
                    ),
                    reliabilityNote = localized4(
                        "Famous report across classical Seerah (Ibn Ishaq / Bayhaqi); note scholars discuss its chain individually (Tier 2), while Bukhari/Muslim independently affirm the sweeping amnesty except a small handful.",
                        "سیرت و بیہقی کی مشہور تاریخی روایت (درجہ دوم)؛ جبکہ عام معافی کا اصول صحیح احادیث سے بھی ثابت ہے۔",
                        "رواية سيرية شهيرة في السيرة والبيهقي (المستوى ٢)، وأصل العفو والرفق ثابت في الصحيحين.",
                        "प्रसिद्ध ऐतिहासिक विवरण (स्तर 2), जबकि आम क्षमा का मूल सिद्धांत सहीह हदीसों से भी प्रमाणित है।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Chroniclers mention 10th or 20th Ramadan 8 AH for the entry into Makkah, with complete agreement on minimal skirmishes and widespread clemency.",
                "داخلہ مکہ کی تاریخ 10 یا 20 رمضان بیان کی جاتی ہے، اور عام معافی و تقریباً بے خون فتح پر اتفاق ہے۔",
                "ذُكر الدخول في العاشر أو العشرين من رمضان، مع الإجماع على الفتح السلمي الواسع والعفو العام.",
                "मक्का प्रवेश की तिथि 10 या 20 रमज़ान मानी जाती है, किंतु शांतिपूर्ण प्रवेश व क्षमा पर पूर्ण सहमति है।"
            ),
            lessonsToday = localized4(
                "True victory is proven by humility and forgiveness at the height of power—never arrogance or vengeance.",
                "حقیقی فتح طاقت ملنے پر تکبر و انتقام کے بجائے عاجزی اور عفو و درگزر سے پہچانی جاتی ہے۔",
                "الانتصار الحق يتجلى بالتواضع والعفو عند المقدرة لا بالانتقام والبطش.",
                "सच्ची विजय शक्ति मिलने पर अहंकार व प्रतिशोध के बजाय विनम्रता और क्षमा से प्रकट होती है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Battle of Hunayn & Expedition of Tabuk (8 – 9 AH).",
                "اگلا مرحلہ: غزوہ حنین اور غزوہ تبوک (8 – 9 ہجری)۔",
                "المرحلة التالية: غزوة حنين وغزوة تبوك (8 – 9 هـ).",
                "अगला पड़ाव: हुनैन और तबूक के अभियान (8 – 9 हिजरी)।"
            ),
            referenceLinks = listOf(
                "Quran 17:81 (Quran.com)" to "https://quran.com/17/81",
                "Sahih al-Bukhari 4287 (Sunnah.com)" to "https://sunnah.com/bukhari:4287"
            ),
            keywords = listOf("makkah", "conquest", "fath", "amnesty", "17:81", "110:1", "bukhari 4287", "bilal")
        ),
        SeerahArticle(
            id = "seerah_15_battle_hunayn",
            era = SeerahEra.BATTLES_EXPEDITIONS,
            orderIndex = 15,
            title = localized4(
                "Battle of Hunayn & Reconciling Hearts",
                "غزوہ حنین اور تالیفِ قلب",
                "غزوة حنين وتأليف القلوب",
                "हुनैन का युद्ध और दिलों को जोड़ना (8 हिजरी)"
            ),
            approxChronology = localized4(
                "Shawwal, 8 AH (~February 630 CE), weeks after the Conquest of Makkah.",
                "شوال 8 ہجری (فروری 630 عیسوی)، فتحِ مکہ کے چند ہفتے بعد۔",
                "شوال سنة 8 هـ (نحو فبراير 630م)، بعد فتح مكة بأسابيع.",
                "शव्वाल, 8 हिजरी (फ़रवरी 630 ई.), मक्का विजय के कुछ सप्ताह बाद।"
            ),
            overview = localized4(
                "Hawazin and Thaqif mobilized in the narrow valley of Hunayn to attack Makkah. Marching with 12,000 troops, some newer converts remarked, 'We shall not be defeated today due to small numbers.' Walking straight into a dawn ambush, the ranks scattered momentarily, leaving the Prophet ﷺ firm at the center calling through Al-Abbas's loud voice to the Ansar and people of Samurah. Regrouped around him, the Muslims achieved victory, and the Prophet ﷺ generously returned captives and reassured the Ansar with unmatched affection when spoils were shared to win new Makkan hearts.",
                "فتحِ مکہ کے بعد ہوازن اور ثقیف حنین کی وادی میں جمع ہوئے۔ بارہ ہزار کے لشکر میں بعض افراد نے تعداد پر ناز کیا، مگر گھات میں بیٹھے دشمن کے اچانک تیر برسنے سے صفیں بکھر گئیں۔ آپ ﷺ تنہا ثابت قدم رہے اور حضرت عباسؓ کی بلند آواز سے انصار و اصحابِ شجرہ کو پکارا۔ پلٹ کر حملہ کرنے پر فتح ملی، اور آپ ﷺ نے انصار کو اپنے ساتھ کی لازوال محبت سے تسلی دی۔",
                "تجمعت هوازن وثقيف بوادٍ ضيق في حنين. خرج المسلمون باثني عشر ألفاً فأعجبتهم الكثرة، فباغتهم كمين فتفرقت الصفوف، وثبت النبي ﷺ ونادى العباس في الأنصار وأهل السمرة فكرّوا وانتصروا. ثم قسّم الغنائم لتأليف قلوب حديثي العهد وطيّب خاطر الأنصار بقوله إنه يرجع معهم.",
                "हवाज़िन और सक़ीफ़ जनजातियों ने हुनैन की संकरी घाटी में घात लगाई। 12,000 की बड़ी संख्या पर कुछ नए साथियों को गर्व हुआ, लेकिन अचानक तीरों के हमले से अफरातफरी मच गई। नबी ﷺ अकेले अडिग खड़े रहे और अब्बास के माध्यम से अंसार को पुकारा। साथी लौटकर एकत्र हुए और विजय मिली; फिर आपने अंसार को अनमोल प्रेम शब्दों से संतुष्ट किया।"
            ),
            contextPeoplePlaces = localized4(
                "People: Al-Abbas ibn Abd al-Muttalib, Abu Bakr, Umar, Ali, Ansar. Places: Hunayn valley between Makkah and Ta'if; Ji'ranah.",
                "شخصیات: حضرت عباسؓ، انصارِ مدینہ، اصحابِ شجرہ۔ مقامات: وادیِ حنین، جعرانہ۔",
                "الشخصيات: العباس بن عبد المطلب، الأنصار، أهل السمرة. الأماكن: وادي حنين، الجعرانة.",
                "व्यक्ति: अब्बास, अंसार। स्थान: हुनैन घाटी, जिरआना।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah At-Tawbah 9:25-26)",
                    referenceCode = "Quran 9:25-26",
                    arabicQuote = "وَيَوْمَ حُنَيْنٍ ۙ إِذْ أَعْجَبَتْكُمْ كَثْرَتُكُمْ فَلَمْ تُغْنِ عَنكُمْ شَيْئًا",
                    quoteTranslation = localized4(
                        "And [on] the day of Hunayn, when your great number pleased you, it did not avail you at all... Then Allah sent down His tranquility upon His Messenger and the believers.",
                        "اور حنین کے دن جب تمہیں اپنی کثرت پر خوشی ہوئی تو وہ تمہارے کچھ کام نہ آئی... پھر اللہ نے اپنے رسول اور مومنوں پر سکینت نازل فرمائی۔",
                        "وَيَوْمَ حُنَيْنٍ إِذْ أَعْجَبَتْكُمْ كَثْرَتُكُمْ فَلَمْ تُغْنِ عَنكُمْ شَيْئًا",
                        "और हुनैन के दिन जब तुम्हें अपनी बड़ी संख्या पर गर्व हुआ, वह तुम्हारे किसी काम न आई... फिर अल्लाह ने अपने रसूल और ईमान वालों पर शांति उतारी।"
                    ),
                    reliabilityNote = localized4(
                        "Quranic lesson reminding believers that victory comes from Allah, not numbers.",
                        "غزوہ حنین سے متعلق قرآنی سبق کہ نصرت کثرت سے نہیں اللہ کی مدد سے آتی ہے۔",
                        "آية قرآنية تقرر درس حنين في التوكل لا الاغترار بالكثرة.",
                        "हुनैन के अवसर पर संख्या पर गर्व न करने की क़ुरआनी शिक्षा।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Expedition of Ta'if & Hunayn)",
                    referenceCode = "Sahih al-Bukhari 4330",
                    arabicQuote = "أَلَا تَرْضَوْنَ أَنْ يَذْهَبَ النَّاسُ بِالشَّاةِ وَالْبَعِيرِ وَتَرْجِعُونَ بِرَسُولِ اللَّهِ إِلَى رِحَالِكُمْ",
                    quoteTranslation = localized4(
                        "Will you not be pleased that others go home with sheep and camels while you return home with the Messenger of Allah ﷺ?",
                        "کیا تم اس بات پر راضی نہیں ہو کہ لوگ بکریاں اور اونٹ لے کر جائیں اور تم اپنے گھروں کو اللہ کے رسول ﷺ کو لے کر لوٹو؟",
                        "ألا ترضون أن يذهب الناس بالشاة والبعير وترجعون برسول الله إلى رحالكم.",
                        "क्या तुम इससे प्रसन्न नहीं हो कि लोग बकरियाँ और ऊँट लेकर जाएँ और तुम अल्लाह के रसूल ﷺ को साथ लेकर अपने घर लौटो?"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari 4330 moving the Ansar to tears.",
                        "صحیح بخاری (4330) کی نہایت رقت انگیز مستند حدیث۔",
                        "حديث صحيح في البخاري (4330) أبكى الأنصار فرحاً برسول الله ﷺ.",
                        "सहीह बुख़ारी (4330) की प्रामाणिक और हृदयस्पर्शी हदीस।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Details of Hawazin prisoners freed (reported as ~6,000) and specific cattle counts distributed at Ji'ranah show slight numeric rounding across classical chronicles.",
                "ہوازن کے چھ ہزار قیدیوں کی رہائی اور جعرانہ میں تقسیمِ اموال کی بعض تعداد میں مؤرخین کے معمولی عددی فرق ملتے ہیں۔",
                "ترد أرقام متقاربة في أعداد سبايا هوازن المعتَقين ونصيب المؤلفة قلوبهم في الجعرانة.",
                "रिहा किए गए हवाज़िन बंदियों और वितरण की संख्या में थोड़ा-बहुत अनुमानित अंतर मिलता है।"
            ),
            lessonsToday = localized4(
                "Never boast in numbers, equipment, or status; victory rests on humility before Allah, and valuing spiritual companionship over material gain.",
                "تعداد، وسائل یا منصب پر غرور نہ کریں؛ کامیابی اللہ پر عاجزانہ بھروسے اور مادی مال پر ایمانی رشتے کو ترجیح دینے میں ہے۔",
                "الابتعاد عن الغرور بالكثرة والأسباب، وتقديم صحبة الخير والإيمان على حطام الدنيا.",
                "संसाधनों या संख्या का अहंकार न करें; सफलता विनम्र भरोसे और भौतिक लाभ से ऊपर आध्यात्मिक संबंध को रखने में है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Expedition of Tabuk (9 AH).",
                "اگلا مرحلہ: غزوہ تبوک (9 ہجری)۔",
                "المرحلة التالية: غزوة تبوك (9 هـ).",
                "अगला पड़ाव: तबूक का अभियान (9 हिजरी)।"
            ),
            referenceLinks = listOf(
                "Quran 9:25 (Quran.com)" to "https://quran.com/9/25",
                "Sahih al-Bukhari 4330 (Sunnah.com)" to "https://sunnah.com/bukhari:4330"
            ),
            keywords = listOf("hunayn", "9:25", "bukhari 4330", "ansar", "abbas", "hawazin")
        ),
        SeerahArticle(
            id = "seerah_16_expedition_tabuk",
            era = SeerahEra.BATTLES_EXPEDITIONS,
            orderIndex = 16,
            title = localized4(
                "Expedition of Tabuk (Expedition of Hardship)",
                "غزوہ تبوک (جیش العسرۃ / تنگدستی کا سفر)",
                "غزوة تبوك (غزوة العُسرة)",
                "तबूक का अभियान (कठिनाई का सफ़र, 9 हिजरी)"
            ),
            approxChronology = localized4(
                "Rajab, 9 AH (October 630 CE).",
                "رجب 9 ہجری (اکتوبر 630 عیسوی)۔",
                "رجب سنة 9 هـ (نحو أكتوبر 630م).",
                "रजब, 9 हिजरी (अक्टूबर 630 ई.)।"
            ),
            overview = localized4(
                "Hearing reports of Byzantine military mobilization near northern Arabia during peak summer heat and ripening harvests, the Prophet ﷺ openly announced the long ~700 km march to Tabuk so preparations could be thorough. Companions sacrificed generously—Uthman ibn Affan funded much of the expedition, Abu Bakr gave all his wealth, and Umar gave half—while sincere believers too poor to find mounts wept in longing (Al-Bakkain). Reaching Tabuk with ~30,000 companions, no enemy force met them; the Prophet ﷺ concluded peaceful protection treaties with regional rulers before returning cleanly.",
                "شدید گرمی اور فصل پکنے کے موسم میں رومی لشکر کی نقل و حرکت کی اطلاع پر آپ ﷺ نے تبوک کی طویل مہم کا اعلان فرمایا۔ حضرت عثمانؓ نے بھاری مالی اعانت کی، حضرت ابوبکرؓ نے اپنا پورا مال اور حضرت عمرؓ نے نصف مال پیش کیا، جبکہ سواری نہ پانے والے مخلص فقراء غم سے رو پڑے۔ تقریباً تیس ہزار صحابہ کے ساتھ تبوک پہنچے تو دشمن مقابلے پر نہ آیا اور سرحدی معاہدے طے پا گئے۔",
                "عند أنباء تحرك الروم صيفاً في حرٍّ شديد وحاجة، أعلن ﷺ الوجهة لبعد المسافة، فبذل عثمان وأبو بكر وعمر أموالهم، وبكى العاجزون عن الراحلة صدقاً. وصل ﷺ بتبوك في ثلاثين ألفاً فلم يجد كيداً وأبرم عهود سلم مع أمراء الحدود.",
                "भीषण गर्मी में उत्तरी सीमा पर सैन्य जमावड़े की सूचना पर नबी ﷺ ने ~700 किमी दूर तबूक यात्रा की घोषणा की। हज़रत उस्मान ने भारी आर्थिक सहयोग दिया, अबू बक्र ने सारा और उमर ने आधा धन अर्पित किया। तबूक पहुँचने पर कोई शत्रु सेना सामने न मिली और क्षेत्रीय शासकों से शांति संधियाँ कर सेना वापस लौट आई।"
            ),
            contextPeoplePlaces = localized4(
                "People: Uthman ibn Affan, Abu Bakr, Umar, Abu Dharr, Ka'b ibn Malik. Places: Tabuk (northern frontier of Arabia), Hijr oasis.",
                "شخصیات: حضرت عثمانؓ، ابوبکرؓ، عمرؓ، ابوذرؓ، کعب بن مالکؓ۔ مقامات: تبوک، وادیِ حجر۔",
                "الشخصيات: عثمان، أبو بكر، عمر، أبو ذر، كعب بن مالك. الأماكن: تبوك في شمال الجزيرة.",
                "व्यक्ति: उस्मान, अबू बक्र, उमर, अबू ज़र, काब बिन मालिक। स्थान: तबूक (उत्तरी अरब)।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah At-Tawbah 9:92 & 9:117-118)",
                    referenceCode = "Quran 9:92; 9:117-118",
                    arabicQuote = "تَوَلَّوْا وَّأَعْيُنُهُمْ تَفِيضُ مِنَ الدَّمْعِ حَزَنًا أَلَّا يَجِدُوا مَا يُنفِقُونَ",
                    quoteTranslation = localized4(
                        "They turned back while their eyes overflowed with tears out of sorrow that they could not find the means to spend.",
                        "وہ اس حالت میں لوٹ گئے کہ ان کی آنکھوں سے اس غم میں آنسو بہہ رہے تھے کہ انہیں خرچ کرنے کے لیے کچھ میسر نہ تھا۔",
                        "تَوَلَّوْا وَّأَعْيُنُهُمْ تَفِيضُ مِنَ الدَّمْعِ حَزَنًا أَلَّا يَجِدُوا مَا يُنفِقُونَ",
                        "वे इस दुःख से आँखों में आँसू बहाते हुए लौट गए कि उनके पास खर्च करने के लिए कुछ नहीं था।"
                    ),
                    reliabilityNote = localized4(
                        "Quranic testimony honoring the sincerity of impoverished companions and the repentance of the three left behind.",
                        "غزوہ تبوک کے مخلصین اور پیچھے رہ جانے والے تین صحابہ کی توبہ کا قرآنی بیان۔",
                        "آيات محكمات في سورة التوبة تخلد صدق البكائين وتوبة الثلاثة.",
                        "सूरह अत-तौबा में गरीब साथियों की सच्ची निष्ठा और तीन साथियों की तौबा का क़ुरआनी प्रमाण।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Story of Ka'b ibn Malik)",
                    referenceCode = "Sahih al-Bukhari 4418",
                    arabicQuote = "فَلَقَدْ أَنْجَانِي اللَّهُ بِالصِّدْقِ",
                    quoteTranslation = localized4(
                        "By Allah, Allah saved me solely through truthfulness (Ka'b ibn Malik after declining false excuses upon the Prophet's return from Tabuk).",
                        "اللہ کی قسم! اللہ نے مجھے سچ بولنے کی بدولت ہی نجات عطا فرمائی۔",
                        "ولقد أنجاني الله بالصدق.",
                        "अल्लाह की क़सम! अल्लाह ने मुझे केवल सच बोलने के कारण ही मुक्ति दी।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari 4418.",
                        "صحیح بخاری (4418) میں حضرت کعب بن مالکؓ کی سچائی کا مشہور واقعہ۔",
                        "حديث كعب بن مالك الطويل الصحيح في البخاري (4418).",
                        "सहीह बुख़ारी (4418) की प्रामाणिक हदीस।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "While Tabuk is unanimously the final expedition led by the Prophet ﷺ in 9 AH, classic chronicles vary slightly on the exact number of days camped at Tabuk (between 10 and 20 days).",
                "غزوہ تبوک آپ ﷺ کا آخری غزوہ تھا؛ وہاں قیام کی مدت دس سے بیس دن تک مختلف روایات میں ملتی ہے۔",
                "غزوة تبوك هي آخر غزواته ﷺ بالإجماع، مع اختلاف يسير في أيام المقام بتبوك بين عشرة وعشرين يوماً.",
                "तबूक नबी ﷺ का अंतिम अभियान था; वहाँ ठहरने के दिनों (10 से 20 दिन) में थोड़ा ऐतिहासिक अंतर है।"
            ),
            lessonsToday = localized4(
                "Unflinching honesty—even when facing embarrassment like Ka'b ibn Malik—prevails spiritually, and true faith shines brightest during seasons of discomfort.",
                "غلطی ہو جانے پر جھوٹے عذر کے بجائے سچ بولنا (جیسے کعب بن مالکؓ) اور تنگی میں ایثار کرنا ایمان کی علامت ہے۔",
                "الصدق في الاعتراف بالتقصير كما فعل كعب بن مالك منجاة، والبذل وقت الشدة برهان الإيمان.",
                "गलती होने पर झूठे बहाने बनाने के बजाय सत्य बोलना (काब बिन मालिक की तरह) ही आध्यात्मिक मुक्ति दिलाता है।"
            ),
            nextMilestoneNote = localized4(
                "Next Era: Important Treaties & Events — starting with the Constitution of Madinah.",
                "اگلا باب: اہم معاہدے اور واقعات — میثاقِ مدینہ سے آغاز۔",
                "الباب التالي: المواثيق والأحداث الكبرى — بدءاً بصحيفة المدينة.",
                "अगला अध्याय: महत्वपूर्ण संधियाँ और घटनाएँ — मदीना संविधान से आरंभ।"
            ),
            referenceLinks = listOf(
                "Quran 9:92 (Quran.com)" to "https://quran.com/9/92",
                "Sahih al-Bukhari 4418 (Sunnah.com)" to "https://sunnah.com/bukhari:4418"
            ),
            keywords = listOf("tabuk", "9:92", "uthman", "kab ibn malik", "bukhari 4418", "truthfulness", "9 ah")
        )
    )
}
