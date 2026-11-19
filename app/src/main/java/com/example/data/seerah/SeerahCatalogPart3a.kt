package com.example.data.seerah

internal object SeerahCatalogPart3a {
    val articles: List<SeerahArticle> = listOf(
        SeerahArticle(
            id = "seerah_09_battle_badr",
            era = SeerahEra.BATTLES_EXPEDITIONS,
            orderIndex = 9,
            title = localized4(
                "Battle of Badr (Yawm al-Furqan)",
                "غزوہ بدر الکبریٰ (یوم الفرقان)",
                "غزوة بدر الكبرى (يوم الفرقان)",
                "बद्र का युद्ध (यौमुल फ़ुरक़ान)"
            ),
            approxChronology = localized4(
                "17 Ramadan, 2 AH (March 624 CE).",
                "17 رمضان 2 ہجری (مارچ 624 عیسوی)۔",
                "السابع عشر من رمضان سنة 2 هـ (مارس 624م).",
                "17 रमज़ान, 2 हिजरी (मार्च 624 ई.)।"
            ),
            overview = localized4(
                "Following persistent hostility and confiscation of Muslim property in Makkah, the Prophet ﷺ marched with around 313 poorly armed companions toward a Quraysh caravan, only to confront a 1,000-strong army led by Abu Jahl. The Prophet ﷺ consulted Muhajirun and Ansar, camped near the water wells upon Al-Hubab ibn al-Mundhir's advice, and spent the night in earnest supplication. Allah sent tranquility and angelic reinforcement, granting a decisive victory against the odds.",
                "مکہ میں مسلمانوں کے اموال کی ضبطی اور مسلسل مخالفت کے بعد، جب 313 صحابہؓ کا مختصر قافلہ نکلا تو ابوجہل کے ایک ہزار مسلح لشکر سے آمنا سامنا ہو گیا۔ آپ ﷺ نے مہاجرین و انصار سے مشاورت فرمائی، حضرت حباب بن منذرؓ کے مشورے سے پانی کے چشموں کے قریب پڑاؤ ڈالا اور رات بھر گریہ و زاری سے دعا فرمائی۔ اللہ کی نصرت اور فرشتوں کی تائید سے فیصلہ کن فتح نصیب ہوئی۔",
                "بعد مصادرة قريش لأموال المهاجرين، خرج ﷺ بنحو 313 صحابياً فقابلوا جيش قريش البالغ نحو ألف مقاتل. استشار ﷺ المهاجرين والأنصار، ونزل عند الماء بمشورة الحباب بن المنذر، وألحّ في الدعاء، فأنزل الله السكينة وأمدهم بالملائكة في نصر حاسم.",
                "मक्का में संपत्ति हड़पे जाने और लगातार शत्रुता के बीच जब ~313 साथी निकले तो अबू जहल की ~1,000 की सशस्त्र सेना से सामना हो गया। नबी ﷺ ने मुहाजिरीन व अंसार से परामर्श किया, हुबाब बिन मुंज़िर के सुझाव पर पानी के पास पड़ाव डाला और रात भर दुआ की। अल्लाह की सहायता से निर्णायक विजय मिली।"
            ),
            contextPeoplePlaces = localized4(
                "People: Abu Bakr, Umar, Ali, Hamzah, Sa'd ibn Mu'adh, Al-Hubab ibn al-Mundhir, Abu Jahl. Places: Badr valley (~130 km southwest of Madinah).",
                "شخصیات: حضرت ابوبکرؓ، عمرؓ، علیؓ، حمزہؓ، سعد بن معاذؓ، حباب بن منذرؓ۔ مقامات: وادیِ بدر۔",
                "الشخصيات: أبو بكر، عمر، علي، حمزة، سعد بن معاذ، الحباب بن المنذر. الأماكن: وادي بدر.",
                "व्यक्ति: अबू बक्र, उमर, अली, हमज़ा, साद बिन मुआज़, हुबाब बिन मुंज़िर। स्थान: बद्र घाटी।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Anfal 8:9 & Aal-Imran 3:123)",
                    referenceCode = "Quran 8:9; 3:123",
                    arabicQuote = "وَلَقَدْ نَصَرَكُمُ اللَّهُ بِبَدْرٍ وَأَنتُمْ أَذِلَّةٌ",
                    quoteTranslation = localized4(
                        "And already had Allah given you victory at Badr while you were few in number/weak.",
                        "اور بے شک اللہ نے بدر میں تمہاری مدد فرمائی جب تم کمزور (تعداد میں کم) تھے۔",
                        "وَلَقَدْ نَصَرَكُمُ اللَّهُ بِبَدْرٍ وَأَنتُمْ أَذِلَّةٌ",
                        "और निःसंदेह अल्लाह ने बद्र में तुम्हारी मदद की जब तुम संख्या में कम और कमज़ोर थे।"
                    ),
                    reliabilityNote = localized4(
                        "Quranic testimony describing the Battle of Badr and its spiritual meaning.",
                        "غزوہ بدر کی قطعی قرآنی شہادت۔",
                        "نص قرآني قطعي في أحداث بدر.",
                        "बद्र की घटना का स्पष्ट क़ुरआनी प्रमाण।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih Muslim (Book of Jihad)",
                    referenceCode = "Sahih Muslim 1763",
                    arabicQuote = "اللَّهُمَّ أَنْجِزْ لِي مَا وَعَدْتَنِي",
                    quoteTranslation = localized4(
                        "O Allah, fulfill for me what You have promised me — the Prophet's ﷺ supplication before Badr until his cloak slipped from his shoulders.",
                        "اے اللہ! مجھ سے اپنا وعدہ پورا فرما — میدانِ بدر میں آپ ﷺ کی عاجزانہ دعا یہاں تک کہ چادرِ مبارک شانوں سے گر گئی۔",
                        "اللهم أنجز لي ما وعدتني، اللهم آت ما وعدتني.",
                        "ऐ अल्लाह, मुझसे किया अपना वादा पूरा कर — बद्र में नबी ﷺ की विनम्र दुआ जब आपकी चादर कंधों से गिर गई।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih Muslim 1763.",
                        "صحیح مسلم (1763) کی مستند حدیث۔",
                        "حديث صحيح في صحيح مسلم (1763).",
                        "सहीह मुस्लिम (1763) की प्रामाणिक हदीस।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Lists of Badri companions range between 313 and 317 across Ibn Ishaq and Bukhari (due to excused scouts such as Uthman ibn Affan and Talhah who received equal shares).",
                "اصحابِ بدر کی تعداد 313 سے 317 تک بیان کی جاتی ہے (کیونکہ حضرت عثمانؓ اور طلحہؓ جیسے معذور صحابہ کو بھی اہلِ بدر میں شمار کیا گیا)۔",
                "تتراوح أعداد البدريين بين 313 و317 لاختلاف عدّ من استُخلف أو أُرسل في مهمة كعثمان وطلحة رضي الله عنهما.",
                "बद्र के साथियों की संख्या 313 से 317 बताई जाती है क्योंकि कुछ अनुपस्थित साथियों (जैसे उस्मान) को भी शामिल गिना गया।"
            ),
            lessonsToday = localized4(
                "Consultation (Shura), humane treatment of captives—many freed by teaching Madinah's children to read—and reliance on Allah remain timeless standards.",
                "مشاورت کی اہمیت، قیدیوں سے حسنِ سلوک (پڑھانا بطور فدیہ) اور اللہ پر اعتماد ہر دور کے لیے رہنمائی ہے۔",
                "الشورى، والإحسان إلى الأسرى حتى جعل تعليم القراءة فداءً، والاعتماد على الله دروس خالدة.",
                "परामर्श (शूरा), बंदियों से मानवीय व्यवहार (शिक्षा को मुक्ति-शुल्क बनाना) और ईश्वर पर भरोसा शाश्वत शिक्षाएँ हैं।"
            ),
            nextMilestoneNote = localized4(
                "Next: Battle of Uhud (3 AH).",
                "اگلا مرحلہ: غزوہ احد (3 ہجری)۔",
                "المرحلة التالية: غزوة أحد (3 هـ).",
                "अगला पड़ाव: उहुद का युद्ध (3 हिजरी)।"
            ),
            referenceLinks = listOf(
                "Quran 3:123 (Quran.com)" to "https://quran.com/3/123",
                "Sahih Muslim 1763 (Sunnah.com)" to "https://sunnah.com/muslim:1763"
            ),
            keywords = listOf("badr", "furqan", "313", "3:123", "8:9", "muslim 1763", "captives")
        ),
        SeerahArticle(
            id = "seerah_10_battle_uhud",
            era = SeerahEra.BATTLES_EXPEDITIONS,
            orderIndex = 10,
            title = localized4(
                "Battle of Uhud (Discipline, Trial & Resilience)",
                "غزوہ احد (اطاعت، آزمائش اور استقامت)",
                "غزوة أحد (دروس الطاعة والابتلاء والثبات)",
                "उहुद का युद्ध (अनुशासन, परीक्षा और धैर्य)"
            ),
            approxChronology = localized4(
                "Shawwal, 3 AH (March 625 CE).",
                "شوال 3 ہجری (مارچ 625 عیسوی)۔",
                "شوال سنة 3 هـ (مارس 625م).",
                "शव्वाल, 3 हिजरी (मार्च 625 ई.)।"
            ),
            overview = localized4(
                "Seeking revenge for Badr, Quraysh marched to Mount Uhud north of Madinah with 3,000 fighters. Despite Abd Allah ibn Ubayy's sudden desertion with a third of the force, the Muslims initially broke Quraysh lines while fifty archers under Abdullah ibn Jubayr guarded Ainayn hill. When most archers left their post prematurely for spoils, Khalid ibn al-Walid counterattacked from behind. Seventy companions—including Hamzah ibn Abd al-Muttalib and Mus'ab ibn Umayr—were martyred, and the Prophet ﷺ himself was wounded yet held firm.",
                "بدر کا بدلہ لینے کے لیے قریش کا تین ہزار لشکر جبلِ احد پر آیا۔ عبداللہ بن ابی کی منافقت کے باوجود مسلمانوں نے ابتدا میں برتری حاصل کی، اور جبلِ رماۃ پر پچاس تیر انداز متعین کیے گئے۔ جب اکثر تیر انداز حکم کے خلاف مورچہ چھوڑ کر اترے تو خالد بن ولید نے پیچھے سے حملہ کر دیا۔ حضرت حمزہؓ اور مصعب بن عمیرؓ سمیت ستر صحابہ شہید ہوئے اور خود نبی کریم ﷺ زخمی ہوئے مگر ثابت قدم رہے۔",
                "زحفت قريش بثلاثة آلاف مقاتل إلى جبل أحد. رغم انخذال عبد الله بن أبيّ بثلث الجيش، تقدّم المسلمون أولاً، وثبت الرماة حتى خالف أكثرهم أمر النبي ﷺ ونزلوا للغنائم، فالتف خالد بن الوليد. استُشهد نحو سبعين منهم حمزة ومصعب بن عمير، وجُرح النبي ﷺ وثبت حتى انصرف العدو.",
                "बद्र का बदला लेने कुरैश की ~3,000 की सेना उहुद पहुँची। शुरुआत में मुस्लिम सेना हावी रही और 50 तीरंदाज़ों को टीले की रक्षा का सख्त आदेश था। अधिकांश तीरंदाज़ों के आदेश से पहले पद छोड़ देने पर खालिद बिन वलीद ने पीछे से आक्रमण कर दिया। हज़रत हमज़ा और मुसअब बिन उमैर सहित ~70 साथी शहीद हुए और नबी ﷺ स्वयं घायल हुए, पर अडिग रहे।"
            ),
            contextPeoplePlaces = localized4(
                "People: Hamzah ibn Abd al-Muttalib, Mus'ab ibn Umayr, Nusaybah bint Ka'b (Umm Umarah), Abdullah ibn Jubayr, Abu Dujanah. Places: Mount Uhud, Jabal al-Rumat (Archers' Hill), Madinah.",
                "شخصیات: حضرت حمزہؓ، مصعب بن عمیرؓ، حضرت نسیبہؓ (ام عمارہ)، عبداللہ بن جبیرؓ۔ مقامات: جبلِ احد، جبلِ رماۃ۔",
                "الشخصيات: حمزة بن عبد المطلب، مصعب بن عمير، نسيبة بنت كعب، عبد الله بن جبير. الأماكن: جبل أحد وجبل الرماة.",
                "व्यक्ति: हमज़ा, मुसअब बिन उमैर, नुसैबा बिन्त काब (उम्मे उमारा), अब्दुल्लाह बिन जुबैर। स्थान: उहुद पर्वत।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Aal-Imran 3:152 & 3:159)",
                    referenceCode = "Quran 3:152; 3:159",
                    arabicQuote = "فَبِمَا رَحْمَةٍ مِّنَ اللَّهِ لِنتَ لَهُمْ",
                    quoteTranslation = localized4(
                        "So by mercy from Allah, you were lenient with them. And if you had been rude and harsh in heart, they would have disbanded from about you. So pardon them and consult them in the matter.",
                        "پس اللہ کی رحمت سے آپ ان کے لیے نرم خو رہے، اور اگر آپ تندخو اور سخت دل ہوتے تو وہ آپ کے گرد سے بکھر جاتے؛ پس انہیں معاف کر دیجیے اور معاملات میں مشورہ کیجیے۔",
                        "فَبِمَا رَحْمَةٍ مِّنَ اللَّهِ لِنتَ لَهُمْ وَلَوْ كُنتَ فَظًّا غَلِيظَ الْقَلْبِ لَانفَضُّوا مِنْ حَوْلِكَ",
                        "अल्लाह की दया से ही आप उनके प्रति नम्र रहे; यदि आप कठोर हृदय होते तो वे आपके पास से बिखर जाते। अतः उन्हें क्षमा करें और उनसे परामर्श करते रहें।"
                    ),
                    reliabilityNote = localized4(
                        "Quranic reflection revealed after the setback at Uhud.",
                        "غزوہ احد کے بعد نازل ہونے والا قرآنی تبصرہ و رہنمائی۔",
                        "توجيه قرآني كريم عقب غزوة أحد.",
                        "उहुद के बाद उतरी क़ुरआनी शिक्षा।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Book of Military Expeditions)",
                    referenceCode = "Sahih al-Bukhari 4043",
                    arabicQuote = "إِنْ رَأَيْتُمُونَا تَخْطَفُنَا الطَّيْرُ فَلَا تَبْرَحُوا مَكَانَكُمْ",
                    quoteTranslation = localized4(
                        "Even if you see birds snatching us away, do not leave this post until I send word to you.",
                        "اگر تم دیکھو کہ پرندے ہمیں اچک کر لے جا رہے ہیں تب بھی اپنی جگہ نہ چھوڑنا جب تک میں پیغام نہ بھیجوں۔",
                        "إن رأيتمونا تخطفنا الطير فلا تبرحوا مكانكم هذا حتى أرسل إليكم.",
                        "यदि तुम देखो कि पक्षी हमें नोच कर ले जा रहे हैं, तब भी जब तक मैं संदेश न भेजूं अपना स्थान न छोड़ना।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari 4043.",
                        "صحیح بخاری (4043) میں تیر اندازوں کو دی جانے والی ہدایت۔",
                        "حديث صحيح في البخاري (4043) بشأن أمر الرماة.",
                        "सहीह बुख़ारी (4043) की प्रामाणिक हदीस।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Some historical sources give 7th Shawwal and others 15th Shawwal 3 AH; commentators also explain whether the mistake by archers stemmed from sincere misinterpretation rather than malice (forgiven in Quran 3:155).",
                "تاریخِ جنگ 7 یا 15 شوال منقول ہے؛ مفسرین واضح کرتے ہیں کہ تیر اندازوں کی خطا اجتہادی غلط فہمی تھی جسے قرآن (3:155) نے معاف فرما دیا۔",
                "تختلف التواريخ بين 7 و15 شوال، وبيّن المفسرون أن خطأ الرماة كان تأويلاً ظنوا فيه انتهاء القتال وقد عفا الله عنهم.",
                "तारीख 7 या 15 शव्वाल बताई जाती है, और क़ुरआन 3:155 के अनुसार तीरंदाज़ों की भूल गलतफहमी से हुई थी जिसे अल्लाह ने क्षमा किया।"
            ),
            lessonsToday = localized4(
                "Momentary neglect of discipline and pursuit of short-term gain can undo hard-won progress; true leadership corrects mistakes with gentleness and forgiveness.",
                "نظم و ضبط کی خلاف ورزی اور ذاتی مفاد اجتماعی نقصان کا باعث بنتا ہے، جبکہ حقیقی قائد غلطی پر سختی کے بجائے درگزر و مشاورت سے جوڑتا ہے۔",
                "الإخلال بالانضباط يضر بالجماعة، والقيادة الحكيمة تعالج الخطأ بالرحمة والشورى.",
                "अनुशासनहीनता और तात्कालिक लालच बड़ी उपलब्धि को खतरे में डाल सकते हैं, और सच्चा नेतृत्व गलतियों के बाद भी करुणा से जोड़ता है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Battle of the Trench / Khandaq (5 AH).",
                "اگلا مرحلہ: غزوہ خندق / احزاب (5 ہجری)۔",
                "المرحلة التالية: غزوة الخندق / الأحزاب (5 هـ).",
                "अगला पड़ाव: ख़न्दक़ (अहज़ाब) का युद्ध (5 हिजरी)।"
            ),
            referenceLinks = listOf(
                "Quran 3:159 (Quran.com)" to "https://quran.com/3/159",
                "Sahih al-Bukhari 4043 (Sunnah.com)" to "https://sunnah.com/bukhari:4043"
            ),
            keywords = listOf("uhud", "archers", "hamzah", "musab", "3:152", "3:159", "bukhari 4043")
        ),
        SeerahArticle(
            id = "seerah_11_battle_khandaq",
            era = SeerahEra.BATTLES_EXPEDITIONS,
            orderIndex = 11,
            title = localized4(
                "Battle of the Trench (Khandaq / Al-Ahzab)",
                "غزوہ خندق (غزوہ احزاب)",
                "غزوة الخندق (الأحزاب)",
                "ख़न्दक़ / अहज़ाब का युद्ध (5 हिजरी)"
            ),
            approxChronology = localized4(
                "Shawwal – Dhu al-Qa'dah, 5 AH (March – April 627 CE).",
                "شوال تا ذوالقعدہ 5 ہجری (مارچ تا اپریل 627 عیسوی)۔",
                "شوال – ذو القعدة سنة 5 هـ (نحو مارس 627م).",
                "शव्वाल – ज़ुल-क़ादा, 5 हिजरी (मार्च 627 ई.)।"
            ),
            overview = localized4(
                "A coalition of tribes (~10,000 strong) besieged Madinah. Acting on Salman al-Farsi's strategic suggestion, the Prophet ﷺ and his companions dug a defensive trench along Madinah's exposed northern approach during cold winter scarcity. Through weeks of siege, internal tension from Banu Qurayzah breaking their defense pact, and biting winds, the trench prevented full-scale invasion until Nu'aym ibn Mas'ud sowed distrust among the coalition and Allah sent a fierce storm that scattered the besiegers.",
                "مختلف قبائل کا تقریباً دس ہزار افراد پر مشتمل اتحادی لشکر مدینہ پر چڑھ آیا۔ حضرت سلمان فارسیؓ کے مشورے پر مدینہ کے کھلے شمالی حصے پر دفاعی خندق کھودی گئی۔ سردی، فاقہ کشی اور بنو قریظہ کی عہد شکنی کے باوجود صحابہؓ ڈٹے رہے، پھر نعیم بن مسعود کی حکمت اور اللہ کی بھیجی تیز آندھی نے محاصرہ کرنے والوں کے حوصلے توڑ دیے۔",
                "تحزّبت قريش وحلفاؤها بنحو عشرة آلاف مقاتل لحصار المدينة، فاقترح سلمان الفارسي رضي الله عنه حفر خندق في الجهة الشمالية المكشوفة. ومع الجوع والبرد ونقض بني قريظة للعهد، ثبّت الله المؤمنين وأرسل ريحاً عاصفةً فرّقت شمل الأحزاب.",
                "लगभग 10,000 सैनिकों के गठबंधन ने मदीना को घेर लिया। सलमान फ़ारसी के रणनीतिक सुझाव पर मदीना के खुले उत्तरी हिस्से में गहरी खाई (ख़न्दक़) खोदी गई। भूख, ठंड और बनू कुरैज़ा के विश्वासघात के बावजूद धैर्य और अल्लाह द्वारा भेजे गए भीषण तूफ़ान से घेराबंदी टूट गई।"
            ),
            contextPeoplePlaces = localized4(
                "People: Salman al-Farsi, Ali ibn Abi Talib, Hudhayfah ibn al-Yaman, Nu'aym ibn Mas'ud, Sa'd ibn Mu'adh. Places: Northern approach of Madinah (Mount Sila').",
                "شخصیات: حضرت سلمان فارسیؓ، حضرت علیؓ، حضرت حذیفہؓ، نعیم بن مسعودؓ، سعد بن معاذؓ۔ مقامات: مدینہ منورہ (جبلِ سلع)۔",
                "الشخصيات: سلمان الفارسي، علي بن أبي طالب، حذيفة بن اليمان، نعيم بن مسعود، سعد بن معاذ. الأماكن: شمال المدينة وجبل سلع.",
                "व्यक्ति: सलमान फ़ारसी, अली, हुज़ैफ़ा बिन यमान, नुऐम बिन मसूद। स्थान: मदीना का उत्तरी क्षेत्र (सिला पर्वत)।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Ahzab 33:9 & 33:21)",
                    referenceCode = "Quran 33:9; 33:21",
                    arabicQuote = "فَأَرْسَلْنَا عَلَيْهِمْ رِيحًا وَجُنُودًا لَّمْ تَرَوْهَا",
                    quoteTranslation = localized4(
                        "So We sent upon them a wind and armies [of angels] you did not see — and indeed you have in the Messenger of Allah an excellent example.",
                        "تو ہم نے ان پر تیز آندھی اور ایسے لشکر بھیجے جنہیں تم نے نہ دیکھا — اور بے شک تمہارے لیے رسول اللہ ﷺ کی ذات میں بہترین نمونہ ہے۔",
                        "فَأَرْسَلْنَا عَلَيْهِمْ رِيحًا وَجُنُودًا لَّمْ تَرَوْهَا",
                        "तो हमने उन पर एक तेज आँधी और ऐसी सेनाएँ भेजीं जिन्हें तुमने नहीं देखा।"
                    ),
                    reliabilityNote = localized4(
                        "Quranic testimony in Surah Al-Ahzab.",
                        "سورۃ الاحزاب میں غزوہ خندق کا قرآنی بیان۔",
                        "نص قرآني في سورة الأحزاب.",
                        "सूरह अल-अहज़ाब में ख़न्दक़ का प्रत्यक्ष क़ुरआनी उल्लेख।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari",
                    referenceCode = "Sahih al-Bukhari 4101–4106",
                    arabicQuote = "الْحَرْبُ خَدْعَةٌ",
                    quoteTranslation = localized4(
                        "The Prophet ﷺ carried earth himself while digging the trench alongside the believers and taught prudent strategy.",
                        "آپ ﷺ خود صحابہؓ کے ساتھ خندق کھودنے کی مٹی اٹھاتے اور امت کے لیے ہمت بڑھانے والے کلمات دہراتے رہے۔",
                        "نقل ﷺ التراب بنفسه مع الصحابة في حفر الخندق وثبّتهم في المحنة.",
                        "नबी ﷺ स्वयं साथियों के साथ खाई खोदते और मिट्टी ढोते हुए सबका मनोबल बढ़ाते रहे।"
                    ),
                    reliabilityNote = localized4(
                        "Recorded across Sahih al-Bukhari (Book of Expeditions 4101–4106).",
                        "صحیح بخاری (4101–4106) میں غزوہ خندق کی مستند روایات۔",
                        "روايات صحيحة في البخاري (4101–4106).",
                        "सहीह बुख़ारी (4101–4106) में प्रामाणिक वर्णन।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Some sources record the siege lasting around 15–27 nights depending on when auxiliary tribes withdrew before the final windstorm.",
                "محاصرے کی مدت پندرہ سے ستائیس دن تک مختلف روایات میں بیان کی گئی ہے۔",
                "تتراوح مدة الحصار في المصادر بين خمسة عشر وسبعة وعشرين يوماً.",
                "घेराबंदी की अवधि विभिन्न विवरणों में 15 से 27 दिनों तक बताई गई है।"
            ),
            lessonsToday = localized4(
                "Creative problem-solving (accepting Salman's unfamiliar trench method) and solidarity between leaders and workers bring resilience in crises.",
                "دوسروں کے مفید تجربات (سلمان فارسیؓ کی خندق کی تجویز) کو اپنانا اور قائد کا کارکنوں کے شانہ بشانہ محنت کرنا کامیابی کی کنجی ہے۔",
                "الاستفادة من الخبرات النافعة ومشاركة القائد لأصحابه في المشقة يصنعان النصر.",
                "रचनात्मक विचार अपनाना और नेतृत्व द्वारा स्वयं कठिन परिश्रम में हाथ बंटाना संकट से उबारता है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Expedition & Treaty of Hudaybiyyah (6 AH).",
                "اگلا مرحلہ: صلحِ حدیبیہ (6 ہجری)۔",
                "المرحلة التالية: غزوة وصلح الحديبية (6 هـ).",
                "अगला पड़ाव: हुदैबिया की संधि (6 हिजरी)।"
            ),
            referenceLinks = listOf(
                "Quran 33:9 (Quran.com)" to "https://quran.com/33/9",
                "Sahih al-Bukhari 4102 (Sunnah.com)" to "https://sunnah.com/bukhari:4102"
            ),
            keywords = listOf("khandaq", "trench", "ahzab", "salman", "33:9", "33:21", "bukhari 4102")
        ),
        SeerahArticle(
            id = "seerah_12_hudaybiyyah_expedition",
            era = SeerahEra.BATTLES_EXPEDITIONS,
            orderIndex = 12,
            title = localized4(
                "Expedition of Al-Hudaybiyyah & Pledge of Al-Ridwan",
                "غزوہ و عمرہ حدیبیہ اور بیعتِ رضوان",
                "غزوة الحديبية وبيعة الرضوان",
                "हुदैबिया अभियान और बैअतुल-रिज़वान (6 हिजरी)"
            ),
            approxChronology = localized4(
                "Dhu al-Qa'dah, 6 AH (March 628 CE).",
                "ذوالقعدہ 6 ہجری (مارچ 628 عیسوی)۔",
                "ذو القعدة سنة 6 هـ (مارس 628م).",
                "ज़ुल-क़ादा, 6 हिजरी (मार्च 628 ई.)।"
            ),
            overview = localized4(
                "Following a prophetic vision, the Prophet ﷺ led ~1,400 companions dressed strictly in pilgrim Ihram toward Makkah peacefully to perform Umrah. Quraysh blocked access at Al-Hudaybiyyah. When Uthman ibn Affan was sent as envoy and rumored slain, companions pledged steadfast loyalty beneath a tree (Bay'at al-Ridwan). When Quraysh instead chose negotiation, the Prophet ﷺ demonstrated extraordinary patience—even erasing 'Messenger of Allah' from the treaty preamble at Suhayl ibn Amr's objection—securing a 10-year truce that the Quran named a 'Manifest Victory' (Fath Mubin).",
                "خوابِ مبارک کے بعد آپ ﷺ تقریباً 1,400 صحابہؓ کے ساتھ احرام باندھ کر پر امن عمرے کے لیے روانہ ہوئے۔ قریش نے حدیبیہ پر راستہ روکا۔ حضرت عثمانؓ کی شہادت کی افواہ پر درخت تلے بیعتِ رضوان لی گئی۔ بالآخر قریش سے دس سالہ صلح ہوئی؛ آپ ﷺ نے حکمت سے معاہدے پر 'رسول اللہ' مٹانے کی شرط بھی قبول فرمائی، جسے قرآن نے 'فتحِ مبین' قرار دیا۔",
                "انطلق ﷺ بنحو 1400 صحابي محرمين بالعمرة سلمياً، فمنعتهم قريش بالحديبية. وعند شائعة مقتل عثمان رضي الله عنه تمت بيعة الرضوان تحت الشجرة. ثم أُبرم صلح عشر سنوات بمرونة نبوية عظيمة سمّاه القرآن «فتحاً مبيناً».",
                "लगभग 1,400 साथियों के साथ शांतिपूर्ण उमरा के लिए इहराम पहनकर मक्का की ओर बढ़े। कुरैश ने हुदैबिया में रोक लिया। हज़रत उस्मान के मारे जाने की अफवाह पर पेड़ के नीचे 'बैअतुल-रिज़वान' हुई। अंततः 10 वर्ष का शांति समझौता हुआ जिसे क़ुरआन ने 'स्पष्ट विजय' (फ़तह मुबीन) कहा।"
            ),
            contextPeoplePlaces = localized4(
                "People: Uthman ibn Affan, Umar ibn al-Khattab, Ali ibn Abi Talib, Suhayl ibn Amr, Umm Salamah رضي الله عنها. Places: Al-Hudaybiyyah (west edge of Makkah Haram).",
                "شخصیات: حضرت عثمانؓ، عمرؓ، علیؓ، ام سلمہؓ، سہیل بن عمرو۔ مقامات: حدیبیہ۔",
                "الشخصيات: عثمان، عمر، علي، أم سلمة، سهيل بن عمرو. الأماكن: الحديبية.",
                "व्यक्ति: उस्मान, उमर, अली, उम्मे सलमा, सुहैल बिन अम्र। स्थान: हुदैबिया।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Fath 48:1 & 48:18)",
                    referenceCode = "Quran 48:1; 48:18",
                    arabicQuote = "إِنَّا فَتَحْنَا لَكَ فَتْحًا مُّبِينًا",
                    quoteTranslation = localized4(
                        "Indeed, We have given you a clear victory — and Allah was pleased with the believers when they pledged allegiance under the tree.",
                        "بے شک ہم نے آپ کو کھلی فتح عطا فرمائی — اور اللہ مومنوں سے راضی ہو گیا جب وہ درخت کے نیچے آپ سے بیعت کر رہے تھے۔",
                        "إِنَّا فَتَحْنَا لَكَ فَتْحًا مُّبِينًا",
                        "निःसंदेह हमने आपको स्पष्ट विजय प्रदान की — और अल्लाह उन ईमान वालों से प्रसन्न हुआ जब वे पेड़ के नीचे आपसे प्रतिज्ञा कर रहे थे।"
                    ),
                    reliabilityNote = localized4(
                        "Surah Al-Fath revealed specifically after the Treaty of Hudaybiyyah.",
                        "صلحِ حدیبیہ کے بعد نازل ہونے والی سورۃ الفتح کی آیات۔",
                        "سورة الفتح التي نزلت في شأن صلح الحديبية وبيعة الرضوان.",
                        "हुदैबिया की संधि के तुरंत बाद अवतरित सूरह अल-फ़तह।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Book of Conditions)",
                    referenceCode = "Sahih al-Bukhari 2731–2732",
                    arabicQuote = "اذْبَحْ ثُمَّ احْلِقْ",
                    quoteTranslation = localized4(
                        "Umm Salamah رضي الله عنها wisely advised the Prophet ﷺ to offer his sacrifice and shave first silently; all companions immediately followed suit.",
                        "حضرت ام سلمہؓ نے حکمت بھرا مشورہ دیا کہ آپ ﷺ کسی سے کلام کیے بغیر اپنی قربانی ذبح کریں اور سر منڈوائیں؛ سب صحابہ نے فوراً اتباع کی۔",
                        "فأشارت أم سلمة أن يخرج فلا يكلم أحداً حتى ينحر ويحلق، ففعل فتبعه الناس.",
                        "हज़रत उम्मे सलमा ने बुद्धिमत्तापूर्ण सलाह दी कि आप चुपचाप अपनी क़ुरबानी कर सिर मुंडवा लें; सभी साथियों ने तुरंत वैसा ही किया।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari 2731–2732.",
                        "صحیح بخاری (2731–2732) میں صلحِ حدیبیہ اور ام سلمہؓ کے مشورے کی مستند روایت۔",
                        "صحيح البخاري (2731–2732) حول بنود الصلح ومشورة أم سلمة.",
                        "सहीह बुख़ारी (2731–2732) की प्रामाणिक हदीस।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Participants of Hudaybiyyah are reported as either 1,400 or 1,500 across Sahih narrations (Bukhari 4152–4155).",
                "صحیح بخاری میں شرکائے حدیبیہ کی تعداد چودہ سو یا پندرہ سو منقول ہے۔",
                "ورد في الصحيحين عدّ أهل الحديبية بألف وأربعمائة أو ألف وخمسمائة.",
                "सहीह बुख़ारी में हुदैबिया के साथियों की संख्या 1,400 या 1,500 वर्णित है।"
            ),
            lessonsToday = localized4(
                "Strategic peace can achieve far more than short-sighted conflict, and seeking wise counsel from spouses brings calm to collective grief.",
                "پائیدار امن کا معاہدہ وقتی جذباتیت سے کہیں بہتر نتائج دیتا ہے، اور اہلِ خانہ کا صائب مشورہ بڑی مشکلات حل کر دیتا ہے۔",
                "الصلح الحقيقي قد يفتح آفاقاً أعظم من القتال، ومشاورة المرأة العاقلة بركة للأمة.",
                "शांति समझौता दूरदर्शिता से बड़े द्वार खोलता है, और जीवनसाथी का विवेकपूर्ण परामर्श संकट शांत करता है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Battle of Khaybar (7 AH) & Conquest of Makkah (8 AH).",
                "اگلا مرحلہ: غزوہ خیبر (7 ہجری) اور فتحِ مکہ (8 ہجری)۔",
                "المرحلة التالية: غزوة خيبر (7 هـ) وفتح مكة (8 هـ).",
                "अगला पड़ाव: ख़ैबर का युद्ध (7 हिजरी) और मक्का विजय (8 हिजरी)।"
            ),
            referenceLinks = listOf(
                "Quran 48:1 (Quran.com)" to "https://quran.com/48/1",
                "Sahih al-Bukhari 2731 (Sunnah.com)" to "https://sunnah.com/bukhari:2731"
            ),
            keywords = listOf("hudaybiyyah", "ridwan", "48:1", "48:18", "umm salamah", "peace", "truce", "bukhari 2731")
        )
    )
}
