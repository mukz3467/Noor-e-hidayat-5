package com.example.data.seerah

internal object SeerahCatalogPart3c {
    val articles: List<SeerahArticle> = listOf(
        SeerahArticle(
            id = "seerah_17_constitution_of_madinah",
            era = SeerahEra.TREATIES_EVENTS,
            orderIndex = 17,
            title = localized4(
                "Constitution of Madinah (Sahifat al-Madinah)",
                "میثاقِ مدینہ (صحیفۃ المدینہ)",
                "صحيفة المدينة المنورة (الوثيقة الدستورية الأولى)",
                "मदीना का संविधान (मीसाक़-ए-मदीना)"
            ),
            approxChronology = localized4(
                "Year 1 AH (~622 – 623 CE), shortly after settling in Madinah.",
                "سن 1 ہجری (~622 تا 623 عیسوی)، مدینہ منورہ میں قیام کے فوراً بعد۔",
                "السنة الأولى للهجرة (نحو 622 – 623م) عقب استقرار النبي ﷺ بالمدينة.",
                "1 हिजरी (~622 – 623 ई.), मदीना में बसने के तुरंत बाद।"
            ),
            overview = localized4(
                "To transform diverse warring clans—Muhajirun, Aws, Khazraj, and allied Jewish tribes—into a peaceful, law-governed civic community, the Prophet ﷺ drafted a written covenant known as Sahifat al-Madinah. It affirmed mutual defense against outside aggression, banned treacherous bloodshed, guaranteed religious freedom for Jewish signatory tribes alongside Muslims ('the Jews have their religion and the Muslims have theirs'), and made Prophet Muhammad ﷺ the final neutral arbiter of disputes.",
                "مدینہ کے متنوع قبائل (مہاجرین، اوس و خزرج اور مقامی یہودی قبائل) کو امن و قانون کی لڑی میں پرونے کے لیے آپ ﷺ نے تحریری دستور 'میثاقِ مدینہ' مرتب فرمایا۔ اس معاہدے نے مشترکہ دفاع، ظلم و خونریزی کے خاتمے، مذہبی آزادی ('یہود کے لیے ان کا دین اور مسلمانوں کے لیے ان کا دین') اور تنازعات کے منصفانہ تصفیے کی ضمانت دی۔",
                "كتب النبي ﷺ وثيقة المدينة لتنظيم العيش المشترك بين المهاجرين والأنصار وقبائل المدينة من اليهود، فأقرّت الدفاع المشترك، وحرّمت البغي، وكفلت حرية الدين («لليهود دينهم وللمسلمين دينهم»)، وجعلت الفصل في الخصومات بالعدل.",
                "मदीना के विभिन्न वर्गों—मुहाजिरीन, औस, ख़ज़रज और यहूदी कबीलों—को एक शांत, विधि-सम्मत समाज में जोड़ने हेतु नबी ﷺ ने 'सहीफ़तुल-मदीना' लिखित संधि तैयार की। इसमें आपसी रक्षा, रक्तपात पर रोक, धार्मिक स्वतंत्रता और न्यायपूर्ण मध्यस्थता की गारंटी दी गई।"
            ),
            contextPeoplePlaces = localized4(
                "Signatories: Muhajirun, Ansar (Aws & Khazraj), Jewish tribes of Yathrib. Place: Madinah al-Munawwarah.",
                "فریقین: مہاجرین، انصار، یثرب کے قبائل۔ مقام: مدینہ منورہ۔",
                "الأطراف: المهاجرون والأنصار وقبائل يثرب. المكان: المدينة المنورة.",
                "पक्षकार: मुहाजिरीन, अंसार और यसरिब के कबीले। स्थान: मदीना।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_2_CLASSICAL_SEERAH,
                    sourceName = "Sirat Ibn Hisham / Abu Ubayd's Kitab al-Amwal",
                    referenceCode = "Full text of Sahifat al-Madinah",
                    arabicQuote = null,
                    quoteTranslation = localized4(
                        "This is a writing from Muhammad the Prophet between the believers of Quraysh and Yathrib and those who follow and join them... the Jews of Banu Awf are one community with the believers; to the Jews their religion and to the Muslims their religion.",
                        "یہ تحریر محمد نبی ﷺ کی جانب سے قریش و یثرب کے مومنین اور ان کے حلیفوں کے درمیان ہے... بنو عوف کے یہود مومنین کے ساتھ ایک امت ہیں، یہود کے لیے ان کا دین اور مسلمانوں کے لیے ان کا دین۔",
                        "هذا كتاب من محمد النبي بين المؤمنين والمسلمين من قريش ويثرب ومن تبعهم... وإن يهود بني عوف أمة مع المؤمنين، لليهود دينهم وللمسلمين دينهم.",
                        "यह पैगंबर मुहम्मद ﷺ की ओर से एक लिखित संधि है... बनू औफ़ के यहूदी ईमान वालों के साथ एक समुदाय हैं; यहूदियों के लिए उनका धर्म है और मुसलमानों के लिए उनका धर्म।"
                    ),
                    reliabilityNote = localized4(
                        "Transmitted primarily through classical historical compilations (Ibn Ishaq / Ibn Hisham and Kitab al-Amwal, Tier 2) with core principles corroborated across Sahih collections (e.g., sanctity of Madinah in Bukhari 1867).",
                        "میثاقِ مدینہ کا مکمل متن کتبِ سیرت و تاریخ (ابن ہشام، کتاب الاموال — درجہ دوم) سے مروی ہے، جبکہ مدینہ کی حرمت و اصول صحیح احادیث (بخاری 1867) سے بھی ثابت ہیں۔",
                        "نُقل النص الكامل في السيرة لابن هشام وكتاب الأموال لأبي عبيد (المستوى ٢)، وتؤيده أحاديث الصحاح في تحريم المدينة وعهدها.",
                        "पूर्ण पाठ इब्न हिशाम व किताबुल अमवाल (स्तर 2) से संकलित है, और इसके मुख्य सिद्धांत सहीह बुख़ारी (1867) द्वारा समर्थित हैं।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Virtues of Madinah)",
                    referenceCode = "Sahih al-Bukhari 1867 / 1870",
                    arabicQuote = "الْمَدِينَةُ حَرَمٌ مَا بَيْنَ عَيْرٍ إِلَى ثَوْرٍ",
                    quoteTranslation = localized4(
                        "Madinah is a sanctuary between [Mounts] Ayr and Thawr (recorded in a written document kept with Ali).",
                        "مدینہ عیر سے ثور تک حرم (امن کی جگہ) ہے۔",
                        "المدينة حرم ما بين عير إلى ثور.",
                        "मदीना ऐर से सौर पर्वत के बीच एक पवित्र सुरक्षित क्षेत्र है।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari confirming written Madinah covenant provisions.",
                        "صحیح بخاری (1867/1870) میں مدینہ کے تحریری صحیفے کے وجود کی تصدیق۔",
                        "حديث صحيح في البخاري يؤكد وجود صحيفة أحكام المدينة المكتوبة.",
                        "सहीह बुख़ारी (1867/1870) में मदीना के लिखित दस्तावेज़ की पुष्टि।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Modern historians debate whether the preserved text comprises one single charter written in 1 AH or two sequential covenants merged by transmitters around the period after Badr.",
                "محققین اس بات پر بحث کرتے ہیں کہ آیا موجودہ متن ایک ہی دفعہ 1 ہجری میں لکھا گیا یا ابتدائی دو معاہدوں کا مجموعہ ہے، تاہم اس کی تاریخی صداقت مسلم ہے۔",
                "يناقش الباحثون هل كُتبت الصحيفة دفعة واحدة سنة 1 هـ أم على مرحلتين قبل بدر وبعدها، مع إجماعهم على أصالتها التاريخية.",
                "शोधकर्ता चर्चा करते हैं कि यह एक ही दस्तावेज़ था या दो चरणों में जोड़ा गया, किंतु इसकी ऐतिहासिक प्रामाणिकता सर्वमान्य है।"
            ),
            lessonsToday = localized4(
                "Pluralistic societies flourish when citizenship rights, shared civic security, and religious freedom are protected by clear written law.",
                "متنوع معاشروں میں امن تب قائم ہوتا ہے جب شہری حقوق، مشترکہ دفاع اور مذہبی آزادی واضح تحریری قانون کے تحت محفوظ ہوں۔",
                "تزدهر المجتمعات التعددية بالدستور العادل وحفظ المواطنة وحرية المعتقد.",
                "विविधतापूर्ण समाज तभी फलते-फूलते हैं जब नागरिक अधिकार, साझा सुरक्षा और धार्मिक स्वतंत्रता स्पष्ट लिखित कानून से सुरक्षित हों।"
            ),
            nextMilestoneNote = localized4(
                "Next: Diplomatic Significance of the Treaty of Hudaybiyyah.",
                "اگلا مرحلہ: صلحِ حدیبیہ کی سفارتی اہمیت۔",
                "المرحلة التالية: الدلالة الدبلوماسية لصلح الحديبية.",
                "अगला पड़ाव: हुदैबिया की संधि का कूटनीतिक महत्व।"
            ),
            referenceLinks = listOf(
                "Sahih al-Bukhari 1870 (Sunnah.com)" to "https://sunnah.com/bukhari:1870",
                "Kitab al-Amwal / Ibn Hisham Archive" to "https://archive.org"
            ),
            keywords = listOf("constitution", "madinah", "sahifah", "pluralism", "covenant", "bukhari 1870")
        ),
        SeerahArticle(
            id = "seerah_18_treaty_hudaybiyyah_significance",
            era = SeerahEra.TREATIES_EVENTS,
            orderIndex = 18,
            title = localized4(
                "Diplomatic Significance of the Treaty of Hudaybiyyah",
                "صلحِ حدیبیہ کے معاہدے کی تاریخی و سفارتی اہمیت",
                "الأبعاد السياسية والدعوية لصلح الحديبية",
                "हुदैबिया की संधि का ऐतिहासिक और कूटनीतिक महत्व"
            ),
            approxChronology = localized4(
                "Dhu al-Qa'dah 6 AH (628 CE) – Ramadan 8 AH.",
                "ذوالقعدہ 6 ہجری تا رمضان 8 ہجری۔",
                "من ذي القعدة 6 هـ إلى رمضان 8 هـ.",
                "ज़ुल-क़ादा 6 हिजरी से रमज़ान 8 हिजरी तक।"
            ),
            overview = localized4(
                "At first glance, some terms of Hudaybiyyah seemed unfavorable—postponing Umrah for one year and returning Makkans who migrated without guardian permission. Yet lifting active warfare allowed peaceful dialogue across Arabia; more people embraced Islam in the two years after Hudaybiyyah than in the preceding eighteen years combined. When Quraysh's ally violated the peace clause protecting Banu Khuza'ah, the lawful breach dissolved the truce and opened the door to the bloodless liberation of Makkah.",
                "بظاہر صلحِ حدیبیہ کی بعض شرائط (جیسے ایک سال بعد عمرہ اور مکہ سے آنے والوں کی واپسی) سخت محسوس ہوئیں، مگر جنگ بندی سے پورے عرب میں پرامن تبادلۂ خیال کا راستہ کھل گیا۔ نتیجتاً صلح کے بعد دو برسوں میں اتنے لوگ ایمان لائے جتنے پچھلے اٹھارہ برس میں نہ آئے تھے، اور قریش کے عہد توڑنے پر فتحِ مکہ کا راستہ ہموار ہوا۔",
                "بدت بعض بنود الحديبية شاقة ظاهرياً، لكن وقف القتال أتاح حرية الدعوة والتواصل، فدخل الناس في دين الله أفواجاً خلال عامين أكثر من ذي قبل، وهو ما مهد لفتح مكة سلمياً.",
                "ऊपरी तौर पर हुदैबिया की कुछ शर्तें कठिन लगीं, लेकिन युद्धविराम ने पूरे अरब में शांतिपूर्ण संवाद के द्वार खोल दिए। संधि के बाद दो वर्षों में उतने ही अधिक लोगों ने इस्लाम अपनाया जितने पिछले अठारह वर्षों में भी नहीं। यही शांति आगे चलकर रक्तहीन मक्का विजय का कारण बनी।"
            ),
            contextPeoplePlaces = localized4(
                "People: Prophet Muhammad ﷺ, Suhayl ibn Amr, Abu Jandal, Banu Khuza'ah, Banu Bakr. Place: Al-Hudaybiyyah.",
                "شخصیات: سہیل بن عمرو، ابوجندلؓ، بنو خزاعہ۔ مقام: حدیبیہ۔",
                "الشخصيات: سهيل بن عمرو، أبو جندل، خزاعة. المكان: الحديبية.",
                "व्यक्ति: सुहैल बिन अम्र, अबू जंदल, बनू ख़ुज़ाआ। स्थान: हुदैबिया।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih Muslim (Book of Jihad)",
                    referenceCode = "Sahih Muslim 1784",
                    arabicQuote = "مَا فُتِحَ فِي الإِسْلَامِ فَتْحٌ قَبْلَهُ كَانَ أَعْظَمَ مِنْهُ",
                    quoteTranslation = localized4(
                        "Al-Bara' ibn Azib said: 'You consider the Conquest [of Makkah] to be Victory... whereas we consider the Pledge of Al-Ridwan on the Day of Hudaybiyyah to be the greatest Victory.'",
                        "حضرت براء بن عازبؓ فرماتے ہیں: تم فتحِ مکہ کو فتح سمجھتے ہو، جبکہ ہم صلحِ حدیبیہ کو اصل عظیم فتح شمار کرتے ہیں۔",
                        "تعدون أنتم الفتح فتح مكة، وقد كان فتح مكة فتحاً، ونحن نعد الفتح بيعة الرضوان يوم الحديبية.",
                        "बड़ा बिन आज़िब ने कहा: तुम मक्का की विजय को विजय मानते हो, जबकि हम हुदैबिया के दिन को ही सबसे बड़ी विजय गिनते हैं।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari 4283 & Sahih Muslim.",
                        "صحیح بخاری (4283) میں صلحِ حدیبیہ کی اسٹریٹجک برتری کی شہادت۔",
                        "حديث صحيح في البخاري يبرز أن الصلح كان أعظم فتح.",
                        "सहीह बुख़ारी में हुदैबिया को 'सबसे बड़ी विजय' बताने वाली प्रामाणिक हदीस।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Scholars analyze subtle juristic nuances in how Abu Basir and escaping individuals operated legally outside the Madinah jurisdiction under the treaty clause.",
                "ابو بصیرؓ کے معاہدے سے متعلق فقہی و قانونی پہلوؤں پر کتبِ سیرت میں تفصیلی علمی تبصرے ملتے ہیں۔",
                "يناقش الفقهاء والمؤرخون المسائل الحقوقية المتعلقة بشرط ردّ من جاء من مكة وتطبيق النبي ﷺ للوفاء بالعهد بدقة.",
                "विद्वानों ने संधि की शर्तों के सटीक पालन और मक्का से आए लोगों की कानूनी स्थिति पर विस्तृत विश्लेषण किया है।"
            ),
            lessonsToday = localized4(
                "Honoring treaties scrupulously and valuing peace over pride transforms dialogue into long-term moral triumph.",
                "معاہدوں کی پاسداری اور جذباتی ضد کے بجائے امن کو ترجیح دینا دیرپا اخلاقی فتح لاتا ہے۔",
                "الوفاء بالعهود وتقديم مصالح السلم بعيدة المدى يحول الصراع إلى فرصة للبناء.",
                "संधियों का पूर्ण ईमानदारी से पालन और तात्कालिक अहंकार पर शांति को प्राथमिकता देना दीर्घकालिक विजय दिलाता है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Letters Sent to Rulers & Governors (6–7 AH).",
                "اگلا مرحلہ: سلاطینِ عالم کے نام دعوتی خطوط۔",
                "المرحلة التالية: رسائل النبي ﷺ إلى الملوك والأمراء.",
                "अगला पड़ाव: विश्व शासकों को भेजे गए शांति व निमंत्रण पत्र।"
            ),
            referenceLinks = listOf(
                "Sahih al-Bukhari 4283 (Sunnah.com)" to "https://sunnah.com/bukhari:4283"
            ),
            keywords = listOf("hudaybiyyah", "treaty", "diplomacy", "fath", "suhayl", "bukhari 4283")
        ),
        SeerahArticle(
            id = "seerah_19_letters_to_rulers",
            era = SeerahEra.TREATIES_EVENTS,
            orderIndex = 19,
            title = localized4(
                "Diplomatic Letters to Regional Rulers",
                "سلاطین اور حکمرانوں کے نام سفارتی و دعوتی خطوط",
                "رسائل النبي ﷺ إلى الملوك والأمراء",
                "शासकों को भेजे गए कूटनीतिक निमंत्रण पत्र"
            ),
            approxChronology = localized4(
                "Late 6 AH – 7 AH (~628 CE), after the Treaty of Hudaybiyyah.",
                "آخر 6 ہجری تا 7 ہجری (~628 عیسوی)، صلحِ حدیبیہ کے بعد۔",
                "أواخر 6 هـ – 7 هـ (نحو 628م) بعد صلح الحديبية.",
                "6 हिजरी के अंत – 7 हिजरी (~628 ई.), हुदैबिया की संधि के बाद।"
            ),
            overview = localized4(
                "With peace secured at Hudaybiyyah, the Prophet ﷺ had a silver seal engraved 'Muhammad, Messenger of Allah' and dispatched courteous ambassadors bearing letters to contemporary rulers—including Heraclius (Byzantine Emperor), Al-Najashi (Abyssinia), Al-Muqawqis (Egypt), Khosrow (Persia), and Arabian chiefs. Each letter invited them respectfully to Tawhid without coercion, echoing Quran 3:64: 'Come to a common word between us and you.' Heraclius verified the Prophet's truthfulness carefully by questioning Abu Sufyan.",
                "صلحِ حدیبیہ کے امن کے بعد آپ ﷺ نے 'محمد رسول اللہ' کی مہر بنوائی اور قیصرِ روم ہرقل، نجاشی حبشہ، مقوقس مصر، کسریٰ فارس اور عرب امراء کے نام معزز سفیر اور خطوط روانہ فرمائے۔ ان خطوط میں شائستگی سے کلمۂ حق (القرآن 3:64) کی دعوت دی گئی؛ ہرقل نے ابوسفیان سے سوالات کر کے آپ ﷺ کی سچائی کی تصدیق کی۔",
                "بعد الحديبية اتخذ النبي ﷺ خاتماً نقشه «محمد رسول الله» وأرسل سفراء بكتب كريمة إلى هرقل والنجاشي والمقوقس وكسرى وأمراء العرب يدعوهم بالحسنى إلى كلمة سواء (آل عمران 3:64)، فسأل هرقل أبا سفيان فتحقق من صدق نبوته.",
                "हुदैबिया के बाद नबी ﷺ ने 'मुहम्मद रसूलुल्लाह' की मुहर बनवाकर हरक़ल (रोमन सम्राट), नजाशी, मुक़ौक़िस (मिस्र), खुसरौ (फ़ारस) व अन्य शासकों को शिष्टतापूर्ण दूत व पत्र भेजे। इन पत्रों में साझा सत्य (क़ुरआन 3:64) की ओर आमंत्रित किया गया।"
            ),
            contextPeoplePlaces = localized4(
                "Envoys: Dihyah al-Kalbi (Heraclius), Hatib ibn Abi Balta'ah (Muqawqis), Amr ibn Umayyah (Najashi), Abdullah ibn Hudhafah (Khosrow). Places: Jerusalem/Syria, Alexandria, Aksum, Ctesiphon.",
                "سفراء: دحیہ کلبیؓ، حاطب بن ابی بلتعہؓ، عمرو بن امیہؓ، عبداللہ بن حذافہؓ۔",
                "السفراء: دحية الكلبي، حاطب بن أبي بلتعة، عمرو بن أمية، عبد الله بن حذافة.",
                "दूत: दिह्या कल्बी, हातिब, अम्र बिन उमैया, अब्दुल्लाह बिन हुज़ाफ़ा।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Book of Revelation, Hadith #7)",
                    referenceCode = "Sahih al-Bukhari 7",
                    arabicQuote = "أَسْلِمْ تَسْلَمْ يُؤْتِكَ اللَّهُ أَجْرَكَ مَرَّتَيْنِ... قُلْ يَا أَهْلَ الْكِتَابِ تَعَالَوْا إِلَىٰ كَلِمَةٍ سَوَاءٍ بَيْنَنَا وَبَيْنَكُمْ",
                    quoteTranslation = localized4(
                        "Heraclius read the Prophet's ﷺ letter containing Quran 3:64 ('Come to a word that is equitable between us and you') and acknowledged his integrity.",
                        "ہرقل نے نبی کریم ﷺ کا مکتوبِ گرامی پڑھا جس میں آیتِ کریمہ (3:64) درج تھی، اور ابوسفیان کی گواہی کے بعد آپ ﷺ کی صداقت کا اعتراف کیا۔",
                        "قرأ هرقل كتاب النبي ﷺ المتضمن آية آل عمران 64 وشهد بصدقه بعد سؤال أبي سفيان.",
                        "हरक़ल ने नबी ﷺ का पत्र पढ़ा जिसमें आयत 3:64 दर्ज थी और अबू सुफ़ियान के उत्तरों से आपकी सत्यता की पुष्टि की।"
                    ),
                    reliabilityNote = localized4(
                        "One of the most famous narrations in Sahih al-Bukhari (Hadith #7).",
                        "صحیح بخاری (حدیث 7) کی نہایت مشہور و مستند روایت۔",
                        "حديث هرقل الطويل في صحيح البخاري (رقم 7).",
                        "सहीह बुख़ारी (हदीस 7) की प्रसिद्ध और प्रामाणिक हदीस।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "While Heraclius's letter is verbatim in Sahih al-Bukhari #7, the exact wording of the letters to Al-Muqawqis and others is transmitted mainly through classical historians (Al-Tabari / Ibn Sa'd, Tier 2); physical parchment claims remain debated by paleographers.",
                "ہرقل کے نام خط کا متن صحیح بخاری (حدیث 7) میں محفوظ ہے، جبکہ دیگر خطوط کی عبارات کتبِ تاریخ (طبری و ابن سعد — درجہ دوم) سے مروی ہیں۔",
                "نص رسالة هرقل ثابت في البخاري (7)، بينما نُقلت نصوص بقية الكتب في مصادر التاريخ كالطبري وابن سعد (المستوى ٢) مع اختلاف حول الوثائق المنسوبة مادياً.",
                "हरक़ल का पत्र सहीह बुख़ारी में प्रमाणित है, जबकि अन्य पत्रों का विवरण तबरी और इब्न साद जैसे इतिहास ग्रंथों (स्तर 2) से प्राप्त होता है।"
            ),
            lessonsToday = localized4(
                "Engage people of other faiths and leaders with dignity, courtesy, and shared moral common ground ('kalimatin sawa').",
                "دوسرے مذاہب اور قیادتوں سے باوقار انداز، احترام اور مشترکہ قدروں کی بنیاد پر مکالمہ کرنا سنتِ نبوی ہے۔",
                "مخاطبة الآخرين بأدب الدبلوماسية النبوية والبحث عن الكلمة السواء المشتركة.",
                "अन्य धर्मों और नेतृत्व के साथ सम्मान, शिष्टाचार और साझा नैतिक मूल्यों के आधार पर संवाद करना चाहिए।"
            ),
            nextMilestoneNote = localized4(
                "Next: The Farewell Pilgrimage (Hajjat al-Wada) & Passing of the Prophet ﷺ.",
                "اگلا مرحلہ: حجۃ الوداع اور وصالِ نبوی ﷺ۔",
                "المرحلة التالية: حجة الوداع وخطبتها والرفيق الأعلى.",
                "अगला पड़ाव: विदाई हज (हज्जतुल-वदा) और अंतिम उपदेश।"
            ),
            referenceLinks = listOf(
                "Quran 3:64 (Quran.com)" to "https://quran.com/3/64",
                "Sahih al-Bukhari 7 (Sunnah.com)" to "https://sunnah.com/bukhari:7"
            ),
            keywords = listOf("letters", "heraclius", "najashi", "diplomacy", "3:64", "bukhari 7")
        ),
        SeerahArticle(
            id = "seerah_20_farewell_pilgrimage",
            era = SeerahEra.TREATIES_EVENTS,
            orderIndex = 20,
            title = localized4(
                "Farewell Pilgrimage (Hajjat al-Wada) & Passing of the Prophet ﷺ",
                "حجۃ الوداع، خطبۂ حجۃ الوداع اور وصالِ باکمال ﷺ",
                "حجة الوداع وخطبتها ووفاته ﷺ إلى الرفيق الأعلى",
                "विदाई हज (हज्जतुल-वदा), अंतिम उपदेश और नबी ﷺ का विसाल"
            ),
            approxChronology = localized4(
                "Dhu al-Hijjah 10 AH (March 632 CE) – 12 Rabi' al-Awwal 11 AH (June 632 CE).",
                "ذوالحجہ 10 ہجری (مارچ 632 عیسوی) تا 12 ربیع الاول 11 ہجری (جون 632 عیسوی)۔",
                "ذو الحجة سنة 10 هـ (مارس 632م) – ربيع الأول سنة 11 هـ (يونيو 632م).",
                "ज़ुल-हिज्जा 10 हिजरी – 12 रबीउल-अव्वल 11 हिजरी (632 ई.)।"
            ),
            overview = localized4(
                "In Dhu al-Hijjah 10 AH, the Prophet ﷺ performed pilgrimage with over 100,000 companions and delivered the Farewell Sermon at Arafah and Mina—affirming the inviolability of human life and property, abolishing usury and blood feuds, upholding women's mutual rights, declaring no superiority of Arab over non-Arab except through righteousness (Taqwa), and commanding holding fast to Allah's Book. Soon after returning to Madinah, Allah revealed 'This day I have perfected for you your religion' (5:3). Following a final illness nursed in A'ishah's chamber while praying behind Abu Bakr, he chose 'the Highest Companion' on Monday in Rabi' al-Awwal 11 AH.",
                "ذوالحجہ 10 ہجری میں آپ ﷺ نے ایک لاکھ سے زائد صحابہؓ کے ساتھ حج فرمایا اور عرفات و منیٰ میں خطبہ دیا جس میں انسانی جان و مال کے احترام، سود و جاہلیت کے خاتمے، خواتین کے حقوق اور تقویٰ کے سوا ہر نسلی برتری کی نفی کا اعلان فرمایا۔ اس کے چند ماہ بعد مدینہ میں حضرت عائشہؓ کے حجرے میں علالت کے دوران ربیع الاول 11 ہجری میں پیر کے دن 'الرفیق الاعلیٰ' کی طرف کوچ فرمایا۔",
                "في ذي الحجة سنة 10 هـ حج النبي ﷺ في أكثر من مائة ألف وخطب خطبة الوداع مقرراً حرمة الدماء والأموال، وإسقاط الربا، والوصية بالنساء، والمساواة في الإنسانية إلا بالتقوى، والتمسك بكتاب الله. ثم مَرِض بالمدينة وتُوفي ﷺ يوم الاثنين في ربيع الأول سنة 11 هـ مختاراً الرفيق الأعلى.",
                "ज़ुल-हिज्जा 10 हिजरी में एक लाख से अधिक साथियों संग हज करते हुए नबी ﷺ ने अराफ़ात में ऐतिहासिक विदाई उपदेश दिया—मानव जीवन व संपत्ति की पवित्रता, सूद व रक्त-प्रतिशोध की समाप्ति, महिलाओं के अधिकारों की रक्षा, और कर्म-निष्ठा (तक़वा) के सिवा किसी नस्लीय श्रेष्ठता से इनकार की घोषणा की। रबीउल-अव्वल 11 हिजरी में सोमवार को मदीना में आपने 'सर्वोच्च साथी' (अल्लाह) की निकटता चुनी।"
            ),
            contextPeoplePlaces = localized4(
                "People: A'ishah bint Abi Bakr, Abu Bakr al-Siddiq, Umar, Fatimah رضي الله عنها. Places: Mount Arafah, Mina, Al-Masjid al-Nabawi (Madinah).",
                "شخصیات: حضرت عائشہؓ، ابوبکرؓ، عمرؓ، حضرت فاطمہؓ۔ مقامات: میدانِ عرفات، منیٰ، مسجدِ نبوی۔",
                "الشخصيات: عائشة، أبو بكر، عمر، فاطمة رضي الله عنهم. الأماكن: عرفات ومنى والمسجد النبوي.",
                "व्यक्ति: आइशा, अबू बक्र, उमर, फ़ातिमा। स्थान: अराफ़ात, मिना, मस्जिद-ए-नबवी।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Ma'idah 5:3)",
                    referenceCode = "Quran 5:3",
                    arabicQuote = "الْيَوْمَ أَكْمَلْتُ لَكُمْ دِينَكُمْ وَأَتْمَمْتُ عَلَيْكُمْ نِعْمَتِي",
                    quoteTranslation = localized4(
                        "This day I have perfected for you your religion and completed My favor upon you.",
                        "آج میں نے تمہارے لیے تمہارا دین مکمل کر دیا اور تم پر اپنی نعمت تمام کر دی۔",
                        "الْيَوْمَ أَكْمَلْتُ لَكُمْ دِينَكُمْ وَأَتْمَمْتُ عَلَيْكُمْ نِعْمَتِي",
                        "आज मैंने तुम्हारे लिए तुम्हारा धर्म पूर्ण कर दिया और तुम पर अपना अनुग्रह पूरा कर दिया।"
                    ),
                    reliabilityNote = localized4(
                        "Revealed on the Day of Arafah during the Farewell Pilgrimage (Bukhari 45).",
                        "صحیح بخاری (45) کے مطابق یہ آیت یومِ عرفہ (جمعہ) کو نازل ہوئی۔",
                        "نزلت يوم عرفة في حجة الوداع كما ثبت في البخاري (45).",
                        "सहीह बुख़ारी (45) के अनुसार अराफ़ात के दिन अवतरित।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih Muslim & Musnad Ahmad (Farewell Sermon)",
                    referenceCode = "Sahih Muslim 1218; Ahmad 23489",
                    arabicQuote = "فَإِنَّ دِمَاءَكُمْ وَأَمْوَالَكُمْ حَرَامٌ عَلَيْكُمْ كَحُرْمَةِ يَوْمِكُمْ هَذَا... وَتَرَكْتُ فِيكُمْ مَا لَنْ تَضِلُّوا بَعْدَهُ إِنِ اعْتَصَمْتُمْ بِهِ كِتَابَ اللَّهِ",
                    quoteTranslation = localized4(
                        "Verily your blood and property are sacred to one another like the sanctity of this day... Fear Allah concerning women... And I have left among you the Book of Allah; you will never go astray if you hold fast to it.",
                        "بے شک تمہارا خون اور مال ایک دوسرے پر حرام ہے... عورتوں کے بارے میں اللہ سے ڈرو... اور میں تم میں اللہ کی کتاب چھوڑے جا رہا ہوں۔",
                        "إن دماءكم وأموالكم حرام عليكم... فاتقوا الله في النساء... وقد تركت فيكم ما لن تضلوا بعده كتاب الله.",
                        "निःसंदेह तुम्हारा रक्त और धन एक-दूसरे के लिए पवित्र है... स्त्रियों के विषय में अल्लाह से डरो... और मैं तुम्हारे बीच अल्लाह की किताब छोड़ रहा हूँ।"
                    ),
                    reliabilityNote = localized4(
                        "Sahih Muslim 1218; note the explicit statement that no Arab is superior to a non-Arab except by piety appears authentically in Musnad Ahmad 23489.",
                        "صحیح مسلم (1218) اور مسند احمد (23489) کی مستند روایات؛ مختلف خطبات کے الفاظ انہی صحیح مآخذ سے جمع کیے جاتے ہیں۔",
                        "خطبة الوداع موثقة في صحيح مسلم (1218) ومسند أحمد (23489).",
                        "सहीह मुस्लिम (1218) व मुसनद अहमद (23489) में प्रमाणित।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Note: Specific wording across Arafah, Mina, and Nahr sermons is combined by some textbooks; historians also record the exact date in Rabi' al-Awwal 11 AH as Monday the 12th (most common) or 1st/2nd of the month, while unanimously affirming Monday.",
                "عرفات و منیٰ کے خطبات کے مختلف حصوں کو عموماً یکجا کر کے خطبۂ حجۃ الوداع کے طور پر پڑھایا جاتا ہے؛ وصالِ مبارک پیر کے دن (عموماً 12 ربیع الاول) پر سب متفق ہیں۔",
                "جمع بعض المصنفين فقرات خطب عرفة ومنى، واختلف المؤرخون في اليوم من ربيع الأول (كالثاني عشر أو غيره) مع الاتفاق على وفاته ﷺ يوم الاثنين.",
                "अराफ़ात व मिना के उपदेशों के अंश एकत्र कर पढ़ाए जाते हैं; विसाल का दिन सोमवार और प्रायः 12 रबीउल-अव्वल माना जाता है।"
            ),
            lessonsToday = localized4(
                "Racial prejudice, abuse of women, financial exploitation, and bloodshed have no place in Islam—all humanity shares equal dignity under piety and justice.",
                "نسلی تعصب، خواتین پر ظلم، سود اور ناحق خونریزی کی اسلام میں کوئی گنجائش نہیں؛ تمام انسان تقویٰ و انصاف کے معیار پر برابر ہیں۔",
                "الإسلام ينبذ العصبية العرقية والظلم، ويحفظ حقوق النساء والأنفس والأموال، ولا فضل إلا بالتقوى.",
                "इस्लाम में नस्लीय भेदभाव, महिलाओं के प्रति अन्याय और हिंसा का कोई स्थान नहीं; सदाचार व न्याय ही श्रेष्ठता का आधार है।"
            ),
            nextMilestoneNote = localized4(
                "Next Era: Character & Personal Life of Prophet Muhammad ﷺ (13 Essential Traits).",
                "اگلا باب: نبی کریم ﷺ کے اخلاقِ عالیہ اور ذاتی زندگی (13 اہم پہلو)۔",
                "الباب التالي: الشمائل المحمدية والحياة الشخصية ﷺ.",
                "अगला अध्याय: नबी ﷺ का चरित्र और व्यक्तिगत जीवन (13 प्रमुख गुण)।"
            ),
            referenceLinks = listOf(
                "Quran 5:3 (Quran.com)" to "https://quran.com/5/3",
                "Sahih Muslim 1218 (Sunnah.com)" to "https://sunnah.com/muslim:1218"
            ),
            keywords = listOf("farewell", "wada", "sermon", "arafah", "5:3", "muslim 1218", "equality", "passing")
        )
    )
}
