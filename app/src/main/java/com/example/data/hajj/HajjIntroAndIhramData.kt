package com.example.data.hajj

internal object HajjIntroAndIhramData {

    fun introCards(): List<Pair<LocalizedText, LocalizedText>> = listOf(
        LocalizedText(
            en = "What is Hajj? (The Fifth Pillar of Islam)",
            ur = "حج کیا ہے؟ (اسلام کا پانچواں رکن)",
            ar = "ما هو الحج؟ (الركن الخامس من أركان الإسلام)",
            hi = "हज क्या है? (इस्लाम का पाँचवाँ स्तंभ)"
        ) to LocalizedText(
            en = "Hajj is the sacred annual pilgrimage to Makkah obligatory once in a lifetime upon every adult, sane Muslim who possesses physical and financial ability (Istita'ah), performed on fixed days of Dhul-Hijjah (8th–13th). Quran 3:97; 22:27–29.",
            ur = "حج زندگی میں ایک بار ہر عاقل، بالغ اور صاحبِ استطاعت مسلمان پر فرض ہے جو ذوالحجہ کی مقررہ تاریخوں (8 تا 13 ذوالحجہ) میں مکہ مکرمہ میں ادا کیا جاتا ہے۔ (قرآن 3:97، 22:27–29)",
            ar = "الحج قصدُ بيت الله الحرام في أيام معلومات (8 إلى 13 ذي الحجة) لأداء المناسك، وهو فرض عين مرة في العمر على المستطيع. (آل عمران: 97؛ الحج: 27–29)",
            hi = "हज जीवन में एक बार प्रत्येक समझदार, वयस्क और सक्षम (इस्तिताअत वाले) मुसलमान पर फ़र्ज़ है, जो 8 से 13 ज़िल-हिज्जा के निर्धारित दिनों में मक्का में अदा किया जाता है। (क़ुरआन 3:97; 22:27-29)"
        ),
        LocalizedText(
            en = "What is Umrah & How Does It Differ from Hajj?",
            ur = "عمرہ کیا ہے اور حج سے کیسے مختلف ہے؟",
            ar = "ما هي العمرة وبمَ تختلف عن الحج؟",
            hi = "उमराह क्या है और हज से कैसे अलग है?"
        ) to LocalizedText(
            en = "Umrah consists of Ihram, Tawaf around the Ka'bah, Sa'i between Safa and Marwah, and shaving/trimming hair. Unlike Hajj, Umrah may be performed any time of year and does not include standing at Arafat, Muzdalifah, Mina, stoning Jamarat, or obligatory Hady sacrifice. (Note: Hanafi & Maliki classify Umrah as Sunnah Mu'akkadah, whereas Shafi'i & Hanbali classify it as obligatory once in a lifetime; Quran 2:196).",
            ur = "عمرہ میں احرام، طواف، صفا و مروہ کی سعی اور حلق یا قصر شامل ہے۔ حج کے برعکس عمرہ سال کے کسی بھی وقت کیا جا سکتا ہے اور اس میں عرفات، مزدلفہ، منیٰ، رمی جمرات یا دمِ ہدی شامل نہیں۔ (وضاحت: حنفی و مالکی فقہ میں عمرہ سنتِ مؤکدہ جبکہ شافعی و حنبلی میں زندگی میں ایک بار فرض ہے؛ قرآن 2:196)",
            ar = "العمرة إحرامٌ وطوافٌ وسعيٌ وحلقٌ أو تقصير، وتُؤدى في أي وقت من السنة ولا تشمل الوقوف بعرفة ولا المزدلفة ولا رمي الجمرات. (تنبيه فقهي: العمرة واجبة مرة في العمر عند الشافعية والحنابلة، وسنة مؤكدة عند الحنفية والمالكية؛ البقرة: 196)",
            hi = "उमराह में इहराम, तवाफ़, सफ़ा-मरवाह की सई और हल्क़/क़स्र शामिल हैं। यह साल के किसी भी समय किया जा सकता है और इसमें अरफ़ात, मुज़दलिफ़ा, मिना या जमरह की रमी नहीं होती। (फ़िक़्ह में अंतर: हनफ़ी/मालिकी के निकट सुन्नत-ए-मुअक्कदा और शाफ़ेई/हंबली के निकट जीवन में एक बार फ़र्ज़; क़ुरआन 2:196)"
        ),
        LocalizedText(
            en = "Three Forms of Hajj: Tamattu', Qiran & Ifrad",
            ur = "حج کی تین اقسام: تمتع، قران اور افراد",
            ar = "أنساك الحج الثلاثة: التمتع والقران والإفراد",
            hi = "हज के तीन प्रकार: तमत्तु, क़िरान और इफ़राद"
        ) to LocalizedText(
            en = "• Tamattu': Enter Ihram for Umrah during Hajj months (Shawwal–Dhul-Hijjah), complete Umrah and exit Ihram, then enter a new Ihram for Hajj on 8th Dhul-Hijjah (Hady sacrifice obligatory per 2:196).\n• Qiran: Enter single Ihram combining Umrah and Hajj together until 10th Dhul-Hijjah (Hady obligatory).\n• Ifrad: Enter Ihram for Hajj only (no Hady sacrifice obligatory). (Bukhari 1562; Muslim 1211).",
            ur = "• حجِ تمتع: اشہرِ حج میں عمرہ کا احرام باندھ کر عمرہ کے بعد احرام کھول دینا، پھر 8 ذوالحجہ کو حج کا نیا احرام باندھنا (ہدی/قربانی واجب؛ قرآن 2:196)۔\n• حجِ قران: ایک ہی احرام میں عمرہ اور حج دونوں کی نیت کرنا اور 10 ذوالحجہ تک احرام میں رہنا (ہدی واجب)۔\n• حجِ افراد: صرف حج کا احرام باندھنا (قربانی واجب نہیں)۔ (بخاری 1562، مسلم 1211)",
            ar = "• التمتع: الإحرام بالعمرة في أشهر الحج ثم التحلل منها، ثم الإحرام بالحج يوم 8 ذي الحجة (يلزمه الهدي: البقرة 196).\n• القران: الإحرام بالعمرة والحج معاً والبقاء محرماً إلى يوم النحر (يلزمه الهدي).\n• الإفراد: الإحرام بالحج وحده دون عمرة (لا يجب عليه هدي).",
            hi = "• तमत्तु: हज के महीनों में उमराह कर इहराम खोल देना और 8 ज़िल-हिज्जा को हज का नया इहराम बाँधना (हदी क़ुर्बानी अनिवार्य; 2:196)।\n• क़िरान: एक ही इहराम में उमराह व हज दोनों की नीयत रखना (हदी अनिवार्य)।\n• इफ़राद: केवल हज का इहराम बाँधना (हदी अनिवार्य नहीं)।"
        ),
        LocalizedText(
            en = "Prerequisites & Official Regulations Notice",
            ur = "شرائط، تیاری اور سرکاری ضوابط کی ہدایت",
            ar = "الشروط والاستعداد وتنبيه الأنظمة الرسمية",
            hi = "शर्तें, तैयारी और आधिकारिक नियमों की सूचना"
        ) to LocalizedText(
            en = "Sincere intention for Allah alone, lawful (Halal) earnings free of unjust debt, seeking forgiveness from people wronged, physical/financial Istita'ah, and learning the rites. Travel visas, Nusuk permits, vaccination requirements, and transport quotas change annually—always confirm current regulations directly on official portals (haj.gov.sa / nusuk.sa).",
            ur = "خلوصِ نیت، حلال کمائی، قرضوں و حقوق العباد کی ادائیگی، جسمانی و مالی استطاعت، اور مناسک کا علم ضروری ہے۔ ویزا، نسک پرمٹ اور سفری ضوابط ہر سال بدل سکتے ہیں، اس لیے تازہ ترین ہدایات صرف سرکاری ویب سائٹس (haj.gov.sa / nusuk.sa) سے چیک کریں۔",
            ar = "إخلاص النية، المال الحلال، قضاء الديون ورد المظالم، الاستطاعة البدنية والمالية، وتعلّم المناسك. تتغير أنظمة التأشيرات وتصاريح نسك والنقل سنوياً، لذا يُرجى مراجعة البوابات الرسمية (haj.gov.sa / nusuk.sa).",
            hi = "सच्ची नीयत, हलाल कमाई, कर्ज़ व हक़ अदा करना, शारीरिक व आर्थिक क्षमता और अरकान का ज्ञान आवश्यक है। वीज़ा, नुसुक परमिट और यात्रा नियम हर वर्ष बदलते हैं—ताज़ा नियमों के लिए सदैव आधिकारिक पोर्टल (haj.gov.sa / nusuk.sa) देखें।"
        )
    )

    val hajjFormMadhhabNote = MadhhabDifferenceNote(
        topic = LocalizedText(
            en = "Which of the Three Hajj Forms (Tamattu', Qiran, Ifrad) is Preferable?",
            ur = "تمتع، قران اور افراد میں سے کون سا طریقۂ حج افضل ہے؟",
            ar = "أي الأنساك الثلاثة أفضل (التمتع أم القران أم الإفراد)؟",
            hi = "तीनों हज प्रकारों में कौन-सा श्रेष्ठ है?"
        ),
        hanafi = LocalizedText(
            en = "Qiran is considered most virtuous (combining both rites), followed by Tamattu', then Ifrad.",
            ur = "احناف کے نزدیک قران سب سے افضل ہے، اس کے بعد تمتع اور پھر افراد۔",
            ar = "يرى الحنفية أن القران أفضل الأنساك، يليه التمتع، ثم الإفراد.",
            hi = "हनफ़ी मत में क़िरान सर्वाधिक श्रेष्ठ है, फिर तमत्तु और उसके बाद इफ़राद।"
        ),
        shafii = LocalizedText(
            en = "Ifrad (or Tamattu' according to the alternate Shafi'i view) is held preferable; Shafi'i himself favored Ifrad as the Prophet performed Tamattu'/Qiran for legislation.",
            ur = "امام شافعی کے راجح قول میں افراد (اور ایک قول میں تمتع) افضل ہے۔",
            ar = "المشهور عند الشافعية تفضيل الإفراد ثم التمتع ثم القران.",
            hi = "शाफ़ेई मत के प्रसिद्ध दृष्टिकोण में इफ़राद, फिर तमत्तु व क़िरान श्रेष्ठ है।"
        ),
        maliki = LocalizedText(
            en = "Ifrad is considered most virtuous, followed by Qiran, then Tamattu'.",
            ur = "مالکیہ کے نزدیک افراد افضل ہے، اس کے بعد قران اور پھر تمتع۔",
            ar = "يرى المالكية أن الإفراد أفضل، ثم القران، ثم التمتع.",
            hi = "मालिकी मत में इफ़राद श्रेष्ठ है, फिर क़िरान और फिर तमत्तु।"
        ),
        hanbali = LocalizedText(
            en = "Tamattu' is most virtuous for non-Makkans without sacrificial animals brought from home, based on the Prophet's command at Hajjat al-Wada'.",
            ur = "حنابلہ کے نزدیک مسافر کے لیے تمتع سب سے افضل ہے جیسا کہ حجۃ الوداع میں نبی ﷺ نے ترغیب دی۔",
            ar = "يرى الحنابلة أن التمتع أفضل الأنساك لغير المسوق للهدي لأمر النبي ﷺ أصحابه به.",
            hi = "हंबली मत में यात्री के लिए तमत्तु सबसे श्रेष्ठ है जैसा पैगंबर ﷺ ने हज्जतुल-वदा पर प्रेरित किया।"
        ),
        reference = "Sahih al-Bukhari 1562 / Sahih Muslim 1211"
    )

    val terminologyList: List<GuideTerm> = listOf(
        GuideTerm("miqat", "Miqat", "مِيقَات", LocalizedText(
            en = "Designated boundary station where pilgrims travelling toward Makkah must enter Ihram before crossing.",
            ur = "وہ مقررہ سرحدی مقام جہاں سے مکہ جانے والے مسافر پر احرام باندھنا لازم ہوتا ہے۔",
            ar = "الموضع المحدد شرعاً الذي يجب أن يُحرم منه القاصد لمكة للحج أو العمرة.",
            hi = "निर्धारित सीमा-स्थान जहाँ से मक्का जाने वाले यात्री को इहराम बाँधना अनिवार्य है।"
        )),
        GuideTerm("ihram", "Ihram", "إِحْرَام", LocalizedText(
            en = "Sacred state entered with intention (Niyyah) and Talbiyah—not merely the two white sheets.",
            ur = "نیت اور تلبیہ کے ساتھ حج یا عمرہ کی مقدس حالت میں داخل ہونا (صرف دو چادروں کا نام نہیں)۔",
            ar = "نية الدخول في النسك مع التلبية، وتلزم به محظورات الإحرام.",
            hi = "नीयत और तलबिया के साथ इबादत की पवित्र अवस्था में प्रवेश (केवल दो चादरें नहीं)।"
        )),
        GuideTerm("talbiyah", "Talbiyah", "تَلْبِيَة", LocalizedText(
            en = "'Labbayk Allahumma labbayk...' — Sunnah proclamation answering Allah's call from entering Ihram.",
            ur = "'لبیک اللهم لبیک...' احرام کے آغاز سے طواف یا جمرۂ عقبہ تک پڑھا جانے والا شعار۔",
            ar = "قول «لبيك اللهم لبيك...» إعلاناً للاستجابة لنداء الله عند الدخول في النسك.",
            hi = "'लब्बैक अल्लाहुम्मा लब्बैक...' इहराम से शुरू की जाने वाली पवित्र पुकार।"
        )),
        GuideTerm("tawaf", "Tawaf", "طَوَاف", LocalizedText(
            en = "Seven counter-clockwise circuits around the Ka'bah starting and ending aligned with the Black Stone (Hajar al-Aswad).",
            ur = "حجرِ اسود سے شروع کر کے کعبۃ اللہ کے گرد سات چکر لگانا۔",
            ar = "الدوران حول الكعبة المشرفة سبعة أشواط بدءاً من الحجر الأسود وانتهاءً به.",
            hi = "हजर-ए-असवद से आरंभ कर काबा शरीफ़ के चारों ओर सात चक्कर लगाना।"
        )),
        GuideTerm("sai", "Sa'i", "سَعْي", LocalizedText(
            en = "Seven walking/brisk-paced laps between Mount Safa and Mount Marwah commemorating Hajar (AS).",
            ur = "صفا سے مروہ اور مروہ سے صفا تک سات چکر (صفا تا مروہ 1 شمار ہوتا ہے)۔",
            ar = "المشي بين الصفا والمروة سبعة أشواط (من الصفا إلى المروة شوط ومنها إلى الصفا شوط).",
            hi = "सफ़ा और मरवाह की पहाड़ियों के बीच सात फेरे (सफ़ा से मरवाह 1 फेरा)।"
        )),
        GuideTerm("idtiba", "Idtiba' & Raml", "اضْطِبَاع وَرَمَل", LocalizedText(
            en = "Sunnah for men only during Tawaf of Umrah/Qudum: exposing right shoulder (Idtiba') and brisk walking in first 3 rounds (Raml).",
            ur = "صرف مردوں کے لیے طوافِ عمرہ/قدوم میں دایاں کندھا کھلا رکھنا (اضطباع) اور پہلے تین چکروں میں اکڑ کر تیز چلنا (رمل)۔",
            ar = "سنتان للرجال في طواف القدوم والعمرة: كشف الكتف الأيمن (الاضطباع) والإسراع في الأشواط الثلاثة الأولى (الرمل).",
            hi = "केवल पुरुषों के लिए सुन्नत: दाहिना कंधा खुला रखना (इज़तिबा) और पहले 3 चक्कर तेज़ चलना (रमल)।"
        )),
        GuideTerm("halq", "Halq / Taqsir", "حَلْق / تَقْصِير", LocalizedText(
            en = "Shaving the head completely (Halq — men only, higher merit) or trimming hair (Taqsir — men & women).",
            ur = "پورے سر کے بال منڈوانا (حلق — مردوں کے لیے افضل) یا کم از کم انگلی کے پورے جتنے بال کٹوانا (قصر)۔",
            ar = "حلق جميع الرأس (للرجال وهو أفضل) أو تقصير الشعر (للرجال والنساء).",
            hi = "सिर मुंडवाना (हल्क़ — केवल पुरुष) या बाल कटवाना (क़स्र — पुरुष व महिला दोनों)।"
        )),
        GuideTerm("wuquf", "Wuquf", "وُقُوف", LocalizedText(
            en = "Being present within the boundaries of the Plain of Arafat on 9th Dhul-Hijjah—the supreme pillar of Hajj.",
            ur = "9 ذوالحجہ کو میدانِ عرفات کی حدود میں ٹھہرنا، جو حج کا سب سے بڑا رکن ہے۔",
            ar = "الحضور في حدود عرفة يوم التاسع من ذي الحجة، وهو ركن الحج الأعظم.",
            hi = "9 ज़िल-हिज्जा को अरफ़ात के मैदान में ठहरना, जो हज का सबसे प्रमुख स्तंभ है।"
        )),
        GuideTerm("rami", "Rami & Hady", "رَمْي وَهَدْي", LocalizedText(
            en = "Rami: throwing seven small pebbles at the Jamarat pillars in Mina. Hady: sacrificial animal offered on 10th Dhul-Hijjah (or Tamattu'/Qiran atonement).",
            ur = "رمی: منیٰ میں جمرات پر کنکریاں مارنا۔ ہدی: حج تمتع و قران کا شکریہ کا جانور قربان کرنا۔",
            ar = "الرمي: قذف الجمرات السبع بالحصى في منى. الهدي: ما يُذبح في الحرم شكراً لله (كهدي التمتع والقران).",
            hi = "रमी: मिना में जमरह पर कंकड़ियाँ मारना। हदी: हज के अवसर पर क़ुर्बानी का जानवर ज़बह करना।"
        ))
    )

    val meeqatPoints: List<MeeqatPoint> = listOf(
        MeeqatPoint("dhul_hulayfah", LocalizedText("Dhul-Hulayfah (Abyar Ali)", "ذوالحلیفہ (ابیارِ علی)", "ذو الحليفة (أبيار علي)", "ज़ुल-हुलैफ़ा (अब्यार अली)"), "ذُو الْحُلَيْفَة",
            LocalizedText("Pilgrims coming from or through Madinah.", "مدینہ منورہ سے آنے والوں کے لیے۔", "لأهل المدينة ومن مرَّ بها.", "मदीना की ओर से आने वालों के लिए।"), "Sahih al-Bukhari 1524"),
        MeeqatPoint("juhfah", LocalizedText("Al-Juhfah (near Rabigh)", "جحفہ (قریب رابغ)", "الجُحْفَة", "अल-जुहफ़ा (राबिग़ के पास)"), "الْجُحْفَة",
            LocalizedText("Pilgrims from Syria, Egypt, North & West Africa, Europe, Americas.", "شام، مصر، مغربی ممالک اور افریقہ سے آنے والوں کے لیے۔", "لأهل الشام ومصر والمغرب ومن مرّ بها.", "शाम, मिस्र, यूरोप, अमेरिका व अफ्रीका से आने वालों के लिए।"), "Sahih al-Bukhari 1524"),
        MeeqatPoint("qarn", LocalizedText("Qarn al-Manazil (Al-Sayl al-Kabir)", "قرن المنازل (السيل الكبير)", "قَرْنُ الْمَنَازِل", "क़र्न अल-मनाज़िल"), "قَرْنُ الْمَنَازِل",
            LocalizedText("Pilgrims from Najd, Riyadh, and the Gulf region.", "نجد، ریاض اور خلیجی ممالک سے آنے والوں کے لیے۔", "لأهل نجد ودول الخليج ومن مرّ بها.", "नज्द, रियाद और खाड़ी देशों से आने वालों के लिए।"), "Sahih al-Bukhari 1524"),
        MeeqatPoint("yalamlam", LocalizedText("Yalamlam (Al-Sa'diyah)", "یلملم (السعدیہ)", "يَلَمْلَم", "यलमलम"), "يَلَمْلَم",
            LocalizedText("Pilgrims from Yemen, South Asia (Pakistan, India, Bangladesh via sea/air southern route).", "یمن اور جنوبی ایشیا (پاکستان، انڈیا) کے جنوبی راستے سے آنے والوں کے لیے۔", "لأهل اليمن ومن مرّ بها من جنوب آسيا.", "यमन और दक्षिण एशिया (पाकिस्तान, भारत आदि) के मार्ग से आने वालों के लिए।"), "Sahih al-Bukhari 1524"),
        MeeqatPoint("dhat_irq", LocalizedText("Dhat 'Irq", "ذاتِ عرق", "ذَاتُ عِرْق", "ज़ात इर्क़"), "ذَاتُ عِرْق",
            LocalizedText("Pilgrims arriving from Iraq and eastern regions.", "عراق اور مشرق کی طرف سے آنے والوں کے لیے۔", "لأهل العراق والمشرق ومن مرّ بها.", "इराक़ और पूर्वी क्षेत्रों से आने वालों के लिए।"), "Sahih Muslim 1183"),
        MeeqatPoint("inside_miqat", LocalizedText("Residents Inside Miqat & Makkah", "میقات کے اندر کے رہائشی (حل و حرم)", "أهل ما دون الميقات ومكة", "मीक़ात के भीतर और मक्का के निवासी"), "الحِلّ / الحَرَم",
            LocalizedText("Those living inside the Miqat boundary enter Ihram from their residence. For Umrah from inside Makkah, scholars instruct stepping outside the Haram boundary (e.g., Tan'im / Masjid Aishah, Bukhari 1785); for Hajj, Makkah residents enter Ihram from their home.", "میقات کے اندر رہنے والے اپنے گھر سے احرام باندھیں۔ مکہ میں مقیم شخص عمرہ کے لیے حدودِ حرم سے باہر (مثلاً مسجدِ عائشہ/تنعیم، بخاری 1785) سے احرام باندھے، اور حج کا احرام مکہ میں اپنی رہائش سے باندھے۔", "من كان دون المواقيت يُحرم من مكانه؛ وأهل مكة يُحرمون للحج من مكة وللعمرة من الحِل كالتنعيم (البخاري 1785).", "मीक़ात के अंदर के निवासी अपने स्थान से इहराम बाँधते हैं। मक्का में रहने वाले उमराह के लिए हरम सीमा के बाहर (जैसे तनईम) और हज के लिए अपने निवास से इहराम बाँधते हैं।"), "Sahih al-Bukhari 1524, 1785")
    )

    val ihramProhibitions: List<IhramProhibition> = listOf(
        IhramProhibition("hair_nails",
            LocalizedText("Cutting or Removing Hair & Nails", "بال کاٹنا یا ناخن تراشنا", "حلق الشعر أو قص الأظافر", "बाल या नाखून काटना"),
            LocalizedText("Applies equally to men and women until exiting Ihram (except unintentional fallen hair).", "مرد و خواتین دونوں کے لیے احرام کھلنے تک ممنوع ہے۔", "يشمل الرجال والنساء حتى التحلل (إلا ما سقط بغير قصد).", "पुरुष व महिला दोनों के लिए इहराम खुलने तक वर्जित।"),
            "Quran 2:196"),
        IhramProhibition("perfume",
            LocalizedText("Applying Perfume / Scented Products", "خوشبو لگانا", "استعمال الطيب", "इत्र या सुगंध लगाना"),
            LocalizedText("Perfume on body before Niyyah is Sunnah (Bukhari 1539); after entering Ihram, scented oils, soaps, and perfumes are prohibited.", "نیت سے پہلے جسم پر خوشبو لگانا سنت ہے، احرام کے بعد خوشبو والا صابن یا عطر ممنوع ہے۔", "التطيب قبل النية سنة (البخاري 1539)، وبعد الإحرام يُحظر الطيب.", "नीयत से पहले शरीर पर खुशबू लगाना सुन्नत है, इहराम के बाद सुगंधित वस्तुएँ वर्जित हैं।"),
            "Sahih al-Bukhari 1539, 1838"),
        IhramProhibition("sewn_clothes",
            LocalizedText("Men's Sewn/Form-Fitted Garments & Head Covering", "مردوں کا سِلا ہوا لباس اور سر ڈھانپنا", "لبس المخيط وتغطية الرأس للرجال", "पुरुषों के सिले वस्त्र व सिर ढँकना"),
            LocalizedText("Men wear two unstitched cloths (Izar & Rida) and keep head uncovered (umbrellas held overhead are permitted by most scholars). Women wear regular modest sewn clothing without face-veil (Niqab) or gloves (Bukhari 1838).", "مرد دو اَن سِلی چادریں پہنیں اور سر کھلا رکھیں۔ خواتین اپنا معمول کا ساتر سِلا ہوا لباس پہنیں، صرف چہرے پر نقاب (کپڑا چہرے سے لگے بغیر لٹکایا جا سکتا ہے) اور دستانے نہ پہنیں (بخاری 1838)۔", "يلبس الرجل إزاراً ورداءً ولا يغطي رأسه، وتلبس المرأة ثيابها المعتادة دون نقاب ولا قفازين (البخاري 1838).", "पुरुष दो बिना सिले कपड़े पहनें और सिर खुला रखें। महिलाएँ सामान्य शालीन कपड़े पहनें, पर नक़ाब (चेहरे से चिपका पर्दा) व दस्ताने न पहनें (बुखारी 1838)।"),
            "Sahih al-Bukhari 1838 / Sahih Muslim 1177"),
        IhramProhibition("hunting_marriage",
            LocalizedText("Hunting Land Game, Marriage Contracts & Intimacy", "خشکی کا شکار، نکاح اور ازدواجی تعلقات", "صيد البر وعقد النكاح والرفث", "स्थलीय शिकार, निकाह और वैवाहिक संबंध"),
            LocalizedText("Strictly prohibited throughout Ihram for both men and women (Quran 2:197; 5:95; Muslim 1409).", "احرام کی حالت میں مرد و خواتین سب کے لیے قطعی حرام ہے۔", "محرم على المحرم ذكراً كان أو أنثى طوال الإحرام (البقرة 197؛ المائدة 95).", "इहराम के दौरान पुरुष व महिला दोनों के लिए पूर्णतः वर्जित (क़ुरआन 2:197; 5:95)।"),
            "Quran 2:197, 5:95; Sahih Muslim 1409")
    )

    val ihramMadhhabNotes: List<MadhhabDifferenceNote> = listOf(
        MadhhabDifferenceNote(
            topic = LocalizedText("Men's Belt, Watch, Eyeglasses & Footwear in Ihram", "احرام میں بیلٹ، گھڑی، عینک اور جوتوں کا حکم", "حكم الحزام والساعة والنظارة والخفين للمحرم", "इहराम में बेल्ट, घड़ी, चश्मा और चप्पल"),
            hanafi = LocalizedText("Belt/money pouch, watch, and eyeglasses are permitted. Footwear must leave the raised top bone (arch) of the foot uncovered.", "بیلٹ، گھڑی اور عینک جائز ہے؛ چپل ایسی ہو جس سے پاؤں کے اوپر کی درمیانی ہڈی کھلی رہے۔", "يجوز الحزام والساعة والنظارة، ويجب كشف عظمة ظهر القدم في النعل.", "बेल्ट, घड़ी व चश्मा जायज़ है; चप्पल ऐसी हो कि पैर की ऊपरी हड्डी खुली रहे।"),
            shafii = LocalizedText("Belt/pouch, watch, and eyeglasses are permitted; sandals must expose heels/toes above ankles.", "بیلٹ، گھڑی اور عینک جائز؛ چپل ٹخنوں سے نیچے اور کھلی ہونی چاہیے۔", "يجوز الحزام والساعة والنظارة، ويُشترط في النعل عدم ستر الكعبين والقدم.", "बेल्ट, घड़ी व चश्मा जायज़; चप्पल टखनों से नीचे और खुली हो।"),
            maliki = LocalizedText("Unsewn money belt/watch permitted if needed; footwear exposing heels/toes required.", "ضرورتاً کمر بند، گھڑی اور عینک جائز ہے؛ جوتا ٹخنوں سے نیچے کھلا ہو۔", "يجوز الحزام والنظارة للحاجة، والنعل المكشوف أسفل الكعبين.", "आवश्यकतानुसार बेल्ट, घड़ी व चश्मा जायज़; चप्पल टखनों के नीचे खुली हो।"),
            hanbali = LocalizedText("Belt, ring, watch, and eyeglasses permitted without penalty.", "بیلٹ، انگوٹھی، گھڑی اور عینک پہننا جائز ہے۔", "يجوز للمحرم لبس الحزام والساعة والنظارة والهميان بلا فدية.", "बेल्ट, अंगूठी, घड़ी और चश्मा पहनना जायज़ है।"),
            reference = "Sahih al-Bukhari 1838; Sahih Muslim 1177"
        )
    )
}
