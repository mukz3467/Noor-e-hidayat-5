package com.example.data.seerah

internal object SeerahCatalogPart2 {
    val articles: List<SeerahArticle> = listOf(
        SeerahArticle(
            id = "seerah_07_hijrah_to_madinah",
            era = SeerahEra.HIJRAH_TO_MADINAH,
            orderIndex = 7,
            title = localized4(
                "The Hijrah: Cave Thawr, Quba & Arrival in Madinah",
                "ہجرتِ مدینہ: غارِ ثور، مسجدِ قبا اور مدینہ منورہ آمد",
                "الهجرة النبوية: غار ثور وقباء ودخول المدينة المنورة",
                "मदीना हिजरत: गार-ए-सौर, क़ुबा और मदीना में आगमन"
            ),
            approxChronology = localized4(
                "Safar – Rabi' al-Awwal, 1 AH (September ~622 CE). Marks Year 1 of the Islamic Hijri Calendar.",
                "صفر تا ربیع الاول 1 ہجری (ستمبر ~622 عیسوی)۔ اسلامی ہجری تقویم کا آغاز۔",
                "صفر – ربيع الأول 1 هـ (سبتمبر ~622م)، وبه بدأ التاريخ الهجري.",
                "सफ़र – रबीउल-अव्वल 1 हिजरी (सितंबर ~622 ई.)। इसी से इस्लामी हिजरी कैलेंडर का आरंभ हुआ।"
            ),
            overview = localized4(
                "After Makkan leaders plotted to assassinate him, the Prophet ﷺ left Ali ibn Abi Talib رضي الله عنه in his bed to return entrusted valuables to their Makkan owners, then set out with Abu Bakr al-Siddiq رضي الله عنه to Cave Thawr for three nights. When searchers stood right outside, the Prophet ﷺ comforted Abu Bakr: 'Do not grieve; indeed Allah is with us' (Quran 9:40). Guided along coastal routes by Abdullah ibn Urayqit, they stopped at Quba to establish the first mosque built on piety, then entered Yathrib—henceforth Madinah al-Nabi ﷺ—amid joyful welcome from Muhajirun and Ansar.",
                "جب قریش نے قتل کی سازش کی تو آپ ﷺ نے مکہ والوں کی امانتیں لوٹانے کے لیے حضرت علیؓ کو بستر پر چھوڑا اور حضرت ابوبکرؓ کے ساتھ غارِ ثور میں تین راتیں قیام فرمایا۔ تلاش کرنے والے جب غار کے دہانے پر پہنچے تو آپ ﷺ نے فرمایا: 'غم نہ کرو، بے شک اللہ ہمارے ساتھ ہے' (القرآن 9:40)۔ قبا پہنچ کر تقویٰ کی پہلی مسجد تعمیر فرمائی اور پھر مدینہ منورہ میں انصار و مہاجرین کے والہانہ استقبال کے ساتھ تشریف لائے۔",
                "لما تآمرت قريش، استخلف ﷺ علياً لردّ الودائع لأهلها، وخرج مع أبي بكر إلى غار ثور ثلاث ليالٍ قائلاً: «لا تحزن إن الله معنا» (التوبة 9:40). ثم سلكا طريق الساحل، وأسس ﷺ مسجد قباء، ثم دخل المدينة المنورة فاستقبله الأنصار والمهاجرون بالفرح.",
                "जब कुरैश ने षड्यंत्र रचा तो नबी ﷺ ने मक्का वालों की अमानतें लौटाने के लिए अली को अपने बिस्तर पर छोड़ा और अबू बक्र के साथ गार-ए-सौर में तीन रातें बिताईं। जब खोजने वाले गुफ़ा के मुहाने तक आए तो आपने कहा: 'शोक न करो, निःसंदेह अल्लाह हमारे साथ है' (क़ुरआन 9:40)। क़ुबा में पहली मस्जिद स्थापित कर आप मदीना पहुँचे जहाँ अंसार व मुहाजिरीन ने भव्य स्वागत किया।"
            ),
            contextPeoplePlaces = localized4(
                "People: Abu Bakr al-Siddiq, Ali ibn Abi Talib, Asma bint Abi Bakr, Abdullah ibn Abi Bakr, Amir ibn Fuhayrah, Suraqah ibn Malik. Places: Makkah, Cave Thawr, Quba, Yathrib (Madinah).",
                "شخصیات: حضرت ابوبکرؓ، حضرت علیؓ، حضرت اسماءؓ، سراقہ بن مالک۔ مقامات: غارِ ثور، قبا، مدینہ منورہ۔",
                "الشخصيات: أبو بكر، علي، أسماء بنت أبي بكر، سراقة بن مالك. الأماكن: غار ثور، قباء، المدينة المنورة.",
                "व्यक्ति: अबू बक्र, अली, अस्मा बिन्त अबी बक्र, सुराक़ा बिन मालिक। स्थान: गार-ए-सौर, क़ुबा, मदीना।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah At-Tawbah 9:40)",
                    referenceCode = "Quran 9:40",
                    arabicQuote = "إِذْ يَقُولُ لِصَاحِبِهِ لَا تَحْزَنْ إِنَّ اللَّهَ مَعَنَا",
                    quoteTranslation = localized4(
                        "When he said to his companion [Abu Bakr], 'Do not grieve; indeed Allah is with us.'",
                        "جب وہ اپنے ساتھی (ابوبکرؓ) سے کہہ رہے تھے کہ غم نہ کرو، بے شک اللہ ہمارے ساتھ ہے۔",
                        "إِذْ يَقُولُ لِصَاحِبِهِ لَا تَحْزَنْ إِنَّ اللَّهَ مَعَنَا",
                        "जब उन्होंने अपने साथी (अबू बक्र) से कहा, 'शोक न करो; निःसंदेह अल्लाह हमारे साथ है।'"
                    ),
                    reliabilityNote = localized4(
                        "Explicit Quranic attestation of the Cave Thawr sanctuary during Hijrah.",
                        "غارِ ثور میں معیتِ الٰہی کی قطعی قرآنی شہادت۔",
                        "نص قرآني قطعي في قصة الغار والهجرة.",
                        "गार-ए-सौर की घटना का प्रत्यक्ष क़ुरआनी प्रमाण।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Book of Merits of Ansar)",
                    referenceCode = "Sahih al-Bukhari 3906",
                    arabicQuote = "مَا ظَنُّكَ يَا أَبَا بَكْرٍ بِاثْنَيْنِ اللَّهُ ثَالِثُهُمَا",
                    quoteTranslation = localized4(
                        "What do you think, O Abu Bakr, of two whose third is Allah?",
                        "اے ابوبکر! ان دو کے بارے میں تمہارا کیا گمان ہے جن کے ساتھ تیسرا اللہ ہو؟",
                        "ما ظنك يا أبا بكر باثنين الله ثالثهما.",
                        "ऐ अबू बक्र, उन दो के बारे में तुम्हारा क्या विचार है जिनका तीसरा स्वयं अल्लाह हो?"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari 3906.",
                        "صحیح بخاری (3906) کی متفقہ مستند روایت۔",
                        "حديث صحيح في صحيح البخاري (رقم 3906).",
                        "सहीह बुख़ारी (3906) की प्रामाणिक हदीस।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_2_CLASSICAL_SEERAH,
                    sourceName = "Classical Seerah Chronicles (Ibn Hisham / Al-Bayhaqi)",
                    referenceCode = "Details of Hijrah welcome poetry",
                    arabicQuote = null,
                    quoteTranslation = localized4(
                        "Classical works cite popular traditions such as 'Tala'al-Badru Alayna' and detailed route stops across the coastal trail.",
                        "کلاسیکی کتبِ سیرت میں ہجرت کے راستے کی منازل اور استقبال کے اشعار نقل کیے گئے ہیں۔",
                        "تروي كتب السيرة محطات الطريق الساحلي وما نُقل من الترحيب الشعبي عند القدوم.",
                        "प्राचीन सीरत ग्रंथों में तटीय मार्ग के पड़ावों और स्वागत का विस्तृत उल्लेख है।"
                    ),
                    reliabilityNote = localized4(
                        "Historical Seerah traditions (Tier 2). Hadith critics note the famous welcome poem has weak or mixed isnad chains compared to Bukhari narrations.",
                        "تاریخی روایت (درجہ دوم)؛ محدثین کے نزدیک استقبال کے مشہور اشعار کی سند بخاری کے درجے کی نہیں، اس لیے اسے حدیثِ صحیح قرار نہیں دیا جاتا۔",
                        "رواية تاريخية (المستوى ٢)؛ نبه المحدثون إلى ضعف أسانيد بعض التفاصيل القصصية كنشيد الترحيب مقارنة بالصحيحين.",
                        "प्राचीन सीरत विवरण (स्तर 2); हदीस विद्वानों के अनुसार कुछ प्रसिद्ध स्वागत-गीतों की सनद सहीह बुख़ारी स्तर की नहीं है।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Historians give slightly varying days in Rabi' al-Awwal for arriving in Quba (often Monday, 8th or 12th Rabi' al-Awwal), while Umar ibn al-Khattab later anchored the Hijri era officially to Muharram 1 AH.",
                "قبا پہنچنے کی تاریخ میں 8 یا 12 ربیع الاول کے اقوال ملتے ہیں، اور بعد ازاں حضرت عمرؓ کے دور میں ہجری سال کا سرکاری آغاز یکم محرم سے مقرر کیا گیا۔",
                "تتنوع التواريخ في يوم الوصول إلى قباء (كالثامن أو الثاني عشر من ربيع الأول)، واتُّخذ محرم بداية التقويم الهجري لاحقاً في عهد عمر رضي الله عنه.",
                "क़ुबा पहुँचने की तारीख पर 8 या 12 रबीउल-अव्वल के भिन्न विवरण मिलते हैं, और बाद में उमर (रज़ि.) के समय मुहर्रम से हिजरी संवत आरंभ किया गया।"
            ),
            lessonsToday = localized4(
                "Trust in Allah works hand-in-hand with meticulous planning: returning trusts honestly, securing guides, and choosing wise discretion.",
                "توکل علی اللہ اور بہترین دنیاوی تدبیر ساتھ ساتھ چلتے ہیں؛ دشمنوں کی بھی امانتیں واپس لوٹانا اسلامی اخلاق کا کمال ہے۔",
                "التوكل الحقيقي يقترن بإحكام التخطيط والأخذ بالأسباب، وأداء الأمانات واجب حتى مع المخالفين.",
                "ईश्वर पर भरोसे के साथ सुदृढ़ योजना आवश्यक है, और विरोधियों की धरोहर भी ईमानदारी से लौटाना इस्लाम की शिक्षा है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Establishing Al-Masjid al-Nabawi & the Brotherhood (Mu'akhat) in Madinah.",
                "اگلا مرحلہ: مسجدِ نبوی کی تعمیر اور مواخاتِ مدینہ۔",
                "المرحلة التالية: بناء المسجد النبوي والمؤاخاة في المدينة.",
                "अगला पड़ाव: मस्जिद-ए-नबवी का निर्माण और मदीना में भाईचारा (मुआख़ात)।"
            ),
            referenceLinks = listOf(
                "Quran 9:40 (Quran.com)" to "https://quran.com/9/40",
                "Sahih al-Bukhari 3906 (Sunnah.com)" to "https://sunnah.com/bukhari:3906"
            ),
            keywords = listOf("hijrah", "thawr", "abu bakr", "9:40", "quba", "madinah", "bukhari 3906")
        ),
        SeerahArticle(
            id = "seerah_08_madinah_masjid_brotherhood",
            era = SeerahEra.MADINAH_PERIOD,
            orderIndex = 8,
            title = localized4(
                "Building Al-Masjid al-Nabawi, Mu'akhat Brotherhood & Civic Madinah",
                "مسجدِ نبوی کی تعمیر، مواخاتِ مدینہ اور اسلامی معاشرے کی تشکیل",
                "بناء المسجد النبوي والمؤاخاة وتأسيس المجتمع المدني",
                "मस्जिद-ए-नबवी का निर्माण, मुआख़ात भाईचारा और मदीना समाज"
            ),
            approxChronology = localized4(
                "1 AH – 2 AH (~622 – 624 CE).",
                "1 تا 2 ہجری (~622 تا 624 عیسوی)۔",
                "السنة الأولى والثانية للهجرة (نحو 622 – 624م).",
                "1 – 2 हिजरी (~622 – 624 ई.)।"
            ),
            overview = localized4(
                "Upon entering Madinah, the Prophet ﷺ purchased land from two young orphans (Sahl and Suhayl) and personally carried bricks alongside his companions to build Al-Masjid al-Nabawi—a place of worship, learning (Al-Suffah), and public consultation. He established the formal Adhan, paired Muhajirun immigrants with local Ansar hosts in profound brotherhood (Mu'akhat) so no displaced family fell into poverty, and nurtured a transparent market economy free of fraud.",
                "مدینہ منورہ پہنچ کر آپ ﷺ نے دو یتیم بچوں (سہل و سہیل) سے زمین خریدی اور صحابہؓ کے ساتھ مل کر مسجدِ نبوی تعمیر فرمائی جو عبادت، تعلیم (اصحابِ صفہ) اور مشاورت کا مرکز تھی۔ اذان کی ابتدا ہوئی، مہاجرین و انصار کے درمیان تاریخی مواخات (بھائی چارہ) قائم کی گئی اور دیانتدارانہ بازار قائم کیا گیا۔",
                "اشترى النبي ﷺ أرض المسجد النبوي من يتيمين وشارك الصحابة في بنائه ليكون جامعاً للعبادة والتعليم (أهل الصفة) والشورى. كما شرع الأذان، وآخى بين المهاجرين والأنصار في تكافل تاريخي فريد، وأسس سوقاً قائماً على العدل ومنع الغش.",
                "मदीना आकर नबी ﷺ ने दो अनाथ बच्चों से ज़मीन खरीदकर साथियों संग मस्जिद-ए-नबवी का निर्माण किया, जो इबादत, शिक्षा (असहाबे-सुफ़्फ़ा) और परामर्श का केंद्र बनी। अज़ान की शुरुआत हुई और मुहाजिरीन व अंसार के बीच अद्वितीय भाईचारा (मुआख़ात) स्थापित किया गया।"
            ),
            contextPeoplePlaces = localized4(
                "People: Muhajirun, Ansar, Abu Ayyub al-Ansari, Abdur-Rahman ibn Awf & Sa'd ibn al-Rabi, Bilal ibn Rabah (first Mu'adhdhin). Places: Al-Masjid al-Nabawi, Al-Suffah.",
                "شخصیات: مہاجرین و انصار، حضرت ابو ایوب انصاریؓ، حضرت عبدالرحمٰن بن عوفؓ، حضرت سعد بن ربیعؓ، حضرت بلالؓ۔ مقامات: مسجدِ نبوی، صفہ۔",
                "الشخصيات: أبو أيوب الأنصاري، عبد الرحمن بن عوف، سعد بن الربيع، بلال بن رباح. الأماكن: المسجد النبوي، الصُّفّة.",
                "व्यक्ति: अबू अय्यूब अंसारी, अब्दुर्रहमान बिन औफ़, साद बिन रबी, बिलाल (प्रथम मुअज़्ज़िन)। स्थान: मस्जिद-ए-नबवी।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari",
                    referenceCode = "Sahih al-Bukhari 2049 & 3934",
                    arabicQuote = "بَارَكَ اللَّهُ لَكَ فِي أَهْلِكَ وَمَالِكَ، دُلُّونِي عَلَى السُّوقِ",
                    quoteTranslation = localized4(
                        "'May Allah bless you in your family and wealth; show me the way to the market.' — Abdur-Rahman ibn Awf's dignified response to Sa'd ibn al-Rabi's generous offer.",
                        "'اللہ آپ کے اہل اور مال میں برکت دے، مجھے بس بازار کا راستہ دکھا دیجیے' — حضرت سعد بن ربیعؓ کی پیشکش پر حضرت عبدالرحمٰن بن عوفؓ کا خوددار جواب۔",
                        "بارك الله لك في أهلك ومالك، دلوني على السوق.",
                        "'अल्लाह आपके परिवार और धन में बरकत दे, मुझे बस बाज़ार का रास्ता बता दीजिए' — साद बिन रबी के उदार प्रस्ताव पर अब्दुर्रहमान बिन औफ़ का स्वाभिमानी उत्तर।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narrations in Sahih al-Bukhari showcasing both Ansar generosity and Muhajirun self-reliance.",
                        "صحیح بخاری کی مستند روایات جو انصار کی فیاضی اور مہاجرین کی خودداری کو واضح کرتی ہیں۔",
                        "صحيح البخاري (2049 و3934) في المآخاة وتعفف المهاجرين.",
                        "सहीह बुख़ारी (2049, 3934) में अंसार की उदारता और मुहाजिरीन के स्वावलंबन का प्रामाणिक प्रमाण।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Hashr 59:9)",
                    referenceCode = "Quran 59:9",
                    arabicQuote = "وَيُؤْثِرُونَ عَلَىٰ أَنفُسِهِمْ وَلَوْ كَانَ بِهِمْ خَصَاصَةٌ",
                    quoteTranslation = localized4(
                        "And they give [them] preference over themselves, even though they themselves are in privation.",
                        "اور وہ دوسروں کو اپنی جانوں پر ترجیح دیتے ہیں اگرچہ خود محتاج ہوں۔",
                        "وَيُؤْثِرُونَ عَلَىٰ أَنفُسِهِمْ وَلَوْ كَانَ بِهِمْ خَصَاصَةٌ",
                        "और वे दूसरों को स्वयं पर प्राथमिकता देते हैं, चाहे वे स्वयं तंगी में ही क्यों न हों।"
                    ),
                    reliabilityNote = localized4(
                        "Quranic praise of the Ansar's altruism.",
                        "انصارِ مدینہ کے ایثار کی قرآنی تحسین۔",
                        "آية قرآنية تشهد بإيثار الأنصار رضي الله عنهم.",
                        "अंसार के निस्वार्थ त्याग की क़ुरआनी प्रशंसा।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Scholars note the Change of Qiblah toward the Ka'bah in Makkah happened roughly 16–17 months after Hijrah in Rajab or Sha'ban 2 AH (recorded clearly in Quran 2:144 and Sahih al-Bukhari 40).",
                "تحویلِ قبلہ (بیت المقدس سے خانہ کعبہ کی طرف) ہجرت کے سولہ تا سترہ ماہ بعد رجب یا شعبان 2 ہجری میں واقع ہوئی، جس کا ذکر قرآن (2:144) اور صحیح بخاری میں ہے۔",
                "وقع تحويل القبلة من بيت المقدس إلى الكعبة بعد ستة عشر أو سبعة عشر شهراً من الهجرة في رجب أو شعبان سنة 2 هـ.",
                "क़िबला परिवर्तन (मक्का की ओर) हिजरत के 16–17 माह बाद 2 हिजरी में हुआ, जिसका स्पष्ट उल्लेख क़ुरआन 2:144 में है।"
            ),
            lessonsToday = localized4(
                "Mosques should serve as welcoming community hubs of prayer, learning, and dignity, where newcomers are assisted toward self-sufficiency rather than dependency.",
                "مساجد کو عبادت کے ساتھ تعلیم و سماجی خیرخواہی کا مرکز ہونا چاہیے اور نئے آنے والوں کو باعزت روزگار کے قابل بنانا سنت ہے۔",
                "ينبغي أن تجمع المساجد بين العبادة والعلم والتكافل، مع دعم المحتاج نحو الاستقلال والكسب الشريف.",
                "मस्जिदों को इबादत के साथ शिक्षा व समुदाय सेवा का केंद्र होना चाहिए, और ज़रूरतमंदों को स्वावलंबी बनाने में सहयोग करना चाहिए।"
            ),
            nextMilestoneNote = localized4(
                "Next Era: Major Battles & Expeditions — beginning with Badr (2 AH).",
                "اگلا باب: غزوات و سرایا — غزوہ بدر (2 ہجری) سے آغاز۔",
                "الباب التالي: الغزوات والسرايا — بدءاً بغزوة بدر الكبرى.",
                "अगला अध्याय: प्रमुख ग़ज़वात — बद्र से आरंभ।"
            ),
            referenceLinks = listOf(
                "Quran 59:9 (Quran.com)" to "https://quran.com/59/9",
                "Sahih al-Bukhari 2049 (Sunnah.com)" to "https://sunnah.com/bukhari:2049"
            ),
            keywords = listOf("madinah", "masjid nabawi", "muakhat", "brotherhood", "ansar", "muhajirun", "59:9", "qiblah")
        )
    )
}
