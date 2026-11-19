package com.example.data.repository

import com.example.data.model.AcademyLesson
import com.example.data.model.AcademyQuizQuestion
import com.example.data.model.AcademyStage
import com.example.data.model.IslamicReference
import com.example.data.model.StageToolType

object AcademyCurriculumStages5To10 {

    val stage5 = AcademyStage(
        stageNumber = 5,
        id = "stage_5",
        titleEn = "Stage 5: The Five Pillars of Islam",
        titleUr = "مرحلہ 5: اسلام کے پانچ بنیادی ارکان",
        titleAr = "المرحلة ٥: أركان الإسلام الخمسة",
        titleHi = "चरण 5: इस्लाम के पाँच स्तंभ (अरकान-ए-इस्लाम)",
        subtitleEn = "5 dedicated courses: Shahadah, Salah, Zakat, Fasting & Hajj",
        subtitleUr = "کلمہ شہادت، نماز، زکوٰۃ، روزہ اور حج پر الگ الگ مستند دروس",
        subtitleAr = "خمس دورات مستقلة: الشهادتان، الصلاة، الزكاة، الصيام، والحج",
        subtitleHi = "5 अलग-अलग पाठ्यक्रम: शहादा, नमाज़, ज़कात, रोज़ा और हज",
        overviewEn = "Explore the five structural pillars of a Muslim's practice (Sahih al-Bukhari 8), distinguishing basic obligations clearly from detailed Fiqh rules.",
        overviewUr = "اسلام کی عملی عمارت کے پانچ ستون (صحیح بخاری: 8) — بنیادی فرضیت اور تفصیلی فقہی مسائل کے فرق کی وضاحت کے ساتھ۔",
        overviewAr = "دراسة أركان الإسلام الخمسة (حديث ابن عمر في الصحيحين) مع التمييز الواضح بين المبادئ الأساسية والتفاصيل الفقهية.",
        overviewHi = "इस्लाम के पाँच मुख्य स्तंभों का अध्ययन—बुनियादी सिद्धांतों और विस्तृत फ़िक़ही नियमों के स्पष्ट अंतर के साथ।",
        interactiveToolType = StageToolType.NONE,
        lessons = listOf(
            AcademyLesson(
                id = "s5_l1",
                stageNumber = 5,
                lessonNumber = 1,
                titleEn = "1. Pillar 1 — Shahadah (Testimony of Faith)",
                titleUr = "1. پہلا رکن: کلمہ شہادت (توحید و رسالت کا اقرار)",
                titleAr = "١. الركن الأول: شهادة أن لا إله إلا الله وأن محمداً رسول الله",
                titleHi = "1. पहला स्तंभ: शहादा (ईमान की गवाही)",
                summaryEn = "Living by 'La ilaha illallah, Muhammadur Rasul-ullah' with knowledge, sincerity, love, and obedience.",
                summaryUr = "کلمہ طیبہ کے تقاضے: علم، یقین، اخلاص، سچائی اور اتباعِ سنتِ رسول ﷺ۔",
                summaryAr = "معنى الشهادتين ومقتضاهما من العلم واليقين والقبول والإخلاص والاتباع.",
                summaryHi = "शहादा के सच्चे अर्थ: ज्ञान, विश्वास, निष्ठा और पैगंबर ﷺ के मार्ग का अनुसरण।",
                bodyEn = "The first pillar is the foundation of the entire structure: testifying that none is worthy of worship except Allah, and Muhammad ﷺ is His final Messenger.\n\nScholars emphasize that 'La ilaha illallah' is not merely spoken once—it shapes what we love, fear, seek help from, and obey every day. Worship (Dua, sacrifice, prostration, ultimate reliance) belongs solely to Allah, while how we worship follows the authentic way of Prophet Muhammad ﷺ.",
                bodyUr = "اسلام کا پہلا اور سب سے بنیادی رکن توحید و رسالت کی گواہی ہے۔ اس کا صرف زبان سے ادا کرنا کافی نہیں بلکہ اس کا تقاضا ہے کہ عبادت، دعا، سجدہ، نذر اور توکل صرف اللہ کے لیے خاص ہوں، اور عبادت کا طریقہ صرف رسول اللہ ﷺ کی سنت کے مطابق ہو۔\n\nاہلِ علم نے کلمہ شہادت کی بنیادی شرطیں علم، یقین، اخلاص، صدق، محبت اور انقیاد بیان فرمائی ہیں۔",
                bodyAr = "الشهادتان هما أصل الإسلام: إفراد الله بالعبادة (لا معبود بحق إلا الله)، واتباع هدي النبي ﷺ وطاعته فيما أمر.\n\nوتقوم الشهادة على العلم واليقين والقبول والانقياد والصدق والإخلاص والمحبة.",
                bodyHi = "शहादा पूरे इस्लाम की नींव है। इसका अर्थ केवल एक बार जुबान से कहना नहीं, बल्कि हर इबादत, दुआ, सजदा और भरोसा केवल अल्लाह तक सीमित रखना और इबादत का तरीक़ा पैगंबर ﷺ की सुन्नत के अनुसार रखना है।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 8 (Islam is built upon five)"),
                    IslamicReference("Holy Quran", "Surah Muhammad (47:19)")
                ),
                quizId = "q_s5_1"
            ),
            AcademyLesson(
                id = "s5_l2",
                stageNumber = 5,
                lessonNumber = 2,
                titleEn = "2. Pillar 2 — Salah (Establishing Daily Prayer)",
                titleUr = "2. دوسرا رکن: اقامتِ نماز",
                titleAr = "٢. الركن الثاني: إقام الصلاة",
                titleHi = "2. दूसरा स्तंभ: नमाज़ क़ायम करना (सलाह)",
                summaryEn = "Establishing the five daily prayers on time as the daily pillar of a believer's spiritual life.",
                summaryUr = "نماز کی پابندی، وقت پر ادائیگی اور ایمان میں اس کا بنیادی مقام۔",
                summaryAr = "المحافظة على الصلوات الخمس في أوقاتها بوصفها عمود الدين وصلة العبد بربه.",
                summaryHi = "समय पर पाँचों नमाज़ें क़ायम करने का महत्व और आध्यात्मिक लाभ।",
                bodyEn = "While Shahadah enters one into Islam, Salah is the daily pillar that sustains faith. Prophet Muhammad ﷺ called prayer 'the coolness of my eyes' (An-Nasa'i 3940). Note the Quran repeatedly says 'establish prayer' (Aqimus-Salah)—meaning praying consistently on time with focus, Wudu, and steady improvement rather than treating it as a rushed chore.\n\nIf you ever accidentally oversleep or forget a prayer, perform it sincerely as soon as you remember (Bukhari 597).",
                bodyUr = "کلمہ کے بعد نماز دین کا ستون ہے۔ قرآن کریم میں بار بار 'اقیموا الصلاۃ' (نماز قائم کرو) کا حکم آیا ہے، جس کا مطلب نماز کو اس کے اوقات، شرائط، خشوع اور پابندی کے ساتھ ادا کرنا ہے۔ نبی ﷺ نے فرمایا کہ نماز میری آنکھوں کی ٹھنڈک ہے۔\n\nاگر کبھی بھول سے یا سونے کی وجہ سے نماز رہ جائے تو یاد آتے ہی قضا پڑھ لینی چاہیے (صحیح بخاری: 597)۔",
                bodyAr = "الصلاة هي عمود الدين وأول ما يُحاسب عليه العبد يوم القيامة. وقد أمر الله بإقامتها بأركانها وخشوعها ومواقيتها.\n\nومن نام عن صلاة أو نسيها فليصلها إذا ذكرها (متفق عليه).",
                bodyHi = "शहादा के बाद नमाज़ दीन का स्तंभ है। क़ुरआन में बार-बार 'नमाज़ क़ायम करने' का हुक्म है—यानी समय, पवित्रता और ध्यान के साथ नियमित पढ़ना। यदि कभी भूल से या नींद में नमाज़ छूट जाए तो याद आते ही तुरंत अदा करें (सहीह बुख़ारी: 597)।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Ankabut (29:45)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 597 (Making up forgotten prayer)")
                )
            ),
            AcademyLesson(
                id = "s5_l3",
                stageNumber = 5,
                lessonNumber = 3,
                titleEn = "3. Pillar 3 — Zakat (Intro Principle vs Detailed Fiqh)",
                titleUr = "3. تیسرا رکن: زکوٰۃ (بنیادی اصول بمقابلہ تفصیلی فقہ)",
                titleAr = "٣. الركن الثالث: الزكاة (الأصل المبسط والتفصيل الفقهي)",
                titleHi = "3. तीसरा स्तंभ: ज़कात (बुनियादी नियम बनाम विस्तृत फ़िक़ह)",
                summaryEn = "Purifying wealth for the poor—clearly separating core introductory principle from detailed Fiqh calculations.",
                summaryUr = "صاحبِ نصاب پر سالانہ 2.5 فیصد زکوٰۃ کا بنیادی اصول اور تفصیلی فقہی مسائل کی واضح تقسیم۔",
                summaryAr = "تطهير المال بإخراج 2.5٪ لمن بلغ النصاب وحال عليه الحول، والتمييز بين الأساس والتفاصيل.",
                summaryHi = "गरीबों के लिए धन की शुद्धि—सरल बुनियादी सिद्धांत और विस्तृत फ़िक़ही गणना का स्पष्ट भेद।",
                bodyEn = "• Basic Introductory Principle (For Every Beginner): Zakat is an obligatory annual charity—not optional tipping—owed only by Muslims whose net savings/wealth exceed a minimum threshold (Nisab) for one full lunar year (Hawl). The standard rate on cash, gold, silver, and trade goods is 2.5%. Quran 9:60 specifies 8 eligible categories led by the poor and needy.\n\n• Detailed Fiqh (Learn Later / Use Calculator & Scholar Guidance): Detailed juristic rulings cover business inventory formulas, stocks/investments, retirement funds, jewelry exemptions, agricultural produce (5% or 10% Ushr), and livestock.",
                bodyUr = "• بنیادی اصول (ہر مبتدی کے لیے): زکوٰۃ صرف اس صاحبِ نصاب مسلمان پر فرض ہے جس کے پاس قرض منہا کر کے بنیادی نصاب کے برابر بچت پورا ایک قمری سال (حول) برقرار رہے۔ نقدی، سونے چاندی اور مالِ تجارت پر سالانہ شرح 2.5 فیصد ہے۔ قرآن (التوبہ 9:60) میں زکوٰۃ کے 8 مصارف (فقراء، مساکین وغیرہ) بیان ہوئے ہیں۔\n\n• تفصیلی فقہی مسائل: کاروباری اسٹاک، شیئرز، پینشن فنڈز، زرعی پیداوار (عشر 5% یا 10%) اور مویشیوں کی تفصیلی شرائط کا مطالعہ اگلے درجے میں عالم کی رہنمائی سے کیا جاتا ہے۔",
                bodyAr = "• المبدأ الأساسي: تجب الزكاة سنوياً (٢.٥٪) على المسلم إذا ملك ما يبلغ النصاب من النقود أو الذهب والفضة أو عروض التجارة وحال عليه الحول القمري، وتُصرف في الأصناف الثمانية (التوبة: ٦٠).\n\n• التفصيل الفقهي: تشمل مسائل زكاة الحلي، والأسهم، والمحافظ الاستثمارية، والزروع والثمار (العشر أو نصفه)، والأنعام، وتُراجع مع المختصين وحاسبة الزكاة.",
                bodyHi = "• बुनियादी सिद्धांत (शुरुआती के लिए): ज़कात केवल उन सक्षम मुसलमानों पर फ़र्ज़ है जिनकी कुल बचत (क़र्ज़ घटाकर) एक न्यूनतम सीमा (निसाब) से ऊपर पूरे एक चाँद वर्ष (हौल) तक रहे। नक़दी, सोना-चाँदी व व्यापारिक माल पर यह 2.5% है, जिसे क़ुरआन (9:60) की 8 श्रेणियों (गरीब, ज़रूरतमंद आदि) को दिया जाता है।\n\n• विस्तृत फ़िक़ह: व्यापारिक स्टॉक, निवेश/शेयर, आभूषण, कृषि उपज (उश्र) आदि की गणना आगे विद्वानों व ज़कात कैलकुलेटर की मदद से की जाती है।",
                fiqhDifferencesEn = "Nisab Standard & Jewelry:\n• Gold vs Silver Nisab: Hanafi frequently advises using Silver Nisab (612.36g / ~52.5 tola) when assessing mixed cash because it benefits the poor more, while many Shafi'i/Maliki/Hanbali and contemporary scholars benchmark cash against Gold Nisab (87.48g / 85g).\n• Personal Women's Gold Jewelry: Hanafi requires Zakat if over Nisab; Shafi'i, Maliki & Hanbali generally exempt lawful personal jewelry worn regularly.",
                fiqhDifferencesUr = "فقہی اختلافات: نقدی کے لیے چاندی کا نصاب (612.36 گرام) یا سونے کا نصاب (87.48 گرام) معتبر ہے—احناف غریبوں کی مصلحت میں چاندی کے نصاب کو ترجیح دیتے ہیں۔ عورت کے ذاتی استعمال کے سونے کے زیورات پر حنفیہ کے نزدیک زکوٰۃ واجب ہے، جبکہ جمہور (شوافع، مالکیہ، حنابلہ) کے ہاں مباح ذاتی زیور پر زکوٰۃ نہیں۔",
                fiqhDifferencesAr = "اختلاف المذاهب: حساب النصاب بالذهب (٨٥غ) أو الفضة (٥٩٥-٦١٢غ)؛ وزكاة الحلي المستعمل تجب عند الحنفية ولا تجب عند الجمهور.",
                fiqhDifferencesHi = "फ़िक़ही मतभेद: नक़दी के लिए सोने (87.48g/85g) या चाँदी (612.36g) के निसाब का आकलन तथा महिलाओं के रोज़मर्रा के पहनने वाले सोने के गहनों पर ज़कात में हनफ़ी मत (वाजिब) और अन्य तीन मतों (छूट) में अंतर है।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah At-Tawbah (9:60 - Eight recipients of Zakat)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 1395 (Sending Mu'adh to Yemen)")
                ),
                quizId = "q_s5_2"
            ),
            AcademyLesson(
                id = "s5_l4",
                stageNumber = 5,
                lessonNumber = 4,
                titleEn = "4. Pillar 4 — Sawm (Fasting in Ramadan)",
                titleUr = "4. چوتھا رکن: رمضان المبارک کے روزے",
                titleAr = "٤. الركن الرابع: صوم رمضان",
                titleHi = "4. चौथा स्तंभ: रमज़ान के रोज़े (उपवास)",
                summaryEn = "Fasting from dawn to sunset for Taqwa (self-discipline & God-consciousness), with compassionate exemptions.",
                summaryUr = "سحری سے افطار تک بھوک پیاس اور گناہوں سے پرہیز، اور مریض و مسافر کے لیے شرعی رخصتیں۔",
                summaryAr = "الإمساك من طلوع الفجر إلى غروب الشمس تعبداً لله وتحصيلاً للتقوى مع رخص الإفطار للمعذورين.",
                summaryHi = "भोर (सुबह सादिक़) से सूर्यास्त तक खाने-पीने व बुरे कामों से रुकना और बीमार/यात्री के लिए छूट।",
                bodyEn = "Every adult, healthy Muslim fasts during the ninth Islamic month, Ramadan, from Fajr (true dawn) until Maghrib (sunset)—abstaining from food, drink, smoking, and marital intimacy during daylight hours.\n\nPurpose (Quran 2:183): 'So that you may attain Taqwa (God-consciousness)'—strengthening self-control, empathy for the hungry, and gratitude.\n\nMerciful Exemptions (Quran 2:184-185): Those who are sick, traveling, pregnant, nursing, or experiencing menstruation/post-natal bleeding do not fast during hardship; they make up missed days later (or pay Fidyah if chronically ill without expectation of recovery).",
                bodyUr = "ہر عاقل، بالغ اور صحت مند مسلمان پر ماہِ رمضان میں صبح صادق سے غروبِ آفتاب تک کھانے پینے، سگریٹ نوشی اور ازدواجی تعلق سے رکنا فرض ہے۔ روزے کا اصل مقصد تقویٰ اور ضبطِ نفس حاصل کرنا ہے (البقرہ 2:183)۔\n\nشرعی رخصتیں: مسافر، مریض، حاملہ یا دودھ پلانے والی خاتون (اگر نقصان کا اندیشہ ہو) اور ایامِ حیض و نفاس والی خواتین روزہ بعد میں قضا کریں؛ دائمی لاعلاج مریض ہر روزے کے بدلے فدیہ دیں۔",
                bodyAr = "يصوم المسلم البالغ العاقل شهر رمضان من طلوع الفجر الصادق حتى غروب الشمس تقرباً لله وتحصيلاً للتقوى (البقرة: ١٨٣).\n\nوقد رخّص الله للمسافر والمريض أن يفطرا ويقضيا أياماً أخر، وللمريض مرضاً مزمناً لا يُرجى برؤه إطعام مسكين عن كل يوم.",
                bodyHi = "रमज़ान के महीने में हर स्वस्थ वयस्क मुसलमान भोर (फ़ज्र) से सूर्यास्त (मग़रिब) तक खाने-पीने और बुराई से रुकता है। इसका उद्देश्य तक़वा (आत्म-संयम और ईश्वर का ध्यान) बढ़ाना है (क़ुरआन 2:183)।\n\nरोगी, यात्री, गर्भवती/स्तनपान कराने वाली या मासिक धर्म वाली महिलाओं को रोज़ा बाद में पूरा करने की छूट है; स्थायी रोगी फ़िदया (गरीब को भोजन) देते हैं।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Baqarah (2:183-185)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 1901, 1903 (Guarding against falsehood while fasting)")
                )
            ),
            AcademyLesson(
                id = "s5_l5",
                stageNumber = 5,
                lessonNumber = 5,
                titleEn = "5. Pillar 5 — Hajj (Intro Principle vs Detailed Fiqh)",
                titleUr = "5. پانچواں رکن: حج (بنیادی فرضیت بمقابلہ تفصیلی مناسک)",
                titleAr = "٥. الركن الخامس: الحج (الفرض الأساسي والمناسك التفصيلية)",
                titleHi = "5. पाँचवाँ स्तंभ: हज (बुनियादी फ़र्ज़ बनाम विस्तृत नियम)",
                summaryEn = "Once-in-a-lifetime pilgrimage to Makkah for those physically and financially able, uniting humanity before Allah.",
                summaryUr = "صاحبِ استطاعت پر زندگی میں ایک بار حج کی فرضیت اور حج کے تفصیلی مناسک کا فرق۔",
                summaryAr = "وجوب الحج مرةً في العمر على المستطيع بدنياً ومالياً، والتمييز بين الفرض الأساسي وأحكام المناسك.",
                summaryHi = "शारीरिक और आर्थिक रूप से सक्षम व्यक्ति पर जीवन में एक बार हज की अनिवार्यता और इसके विस्तृत नियम।",
                bodyEn = "• Basic Introductory Principle: Hajj is obligatory ONCE in a lifetime only for a Muslim who is physically healthy, mentally sound, and financially able without harming dependents' essential needs (Quran 3:97). If you cannot afford it or are unable, there is zero sin upon you.\n\n• Detailed Fiqh of Rituals (Learned Before Traveling): Covers entering Ihram from Miqat, choosing one of three forms (Tamattu', Qiran, Ifrad), Tawaf around the Kaaba, Sa'i between Safa and Marwah, standing at Arafah on 9th Dhul-Hijjah, Muzdalifah, and Rami in Mina.",
                bodyUr = "• بنیادی اصول: حج زندگی میں صرف ایک بار فرض ہے اور وہ بھی صرف اس شخص پر جو جسمانی اور مالی طور پر سفر و اخراجات اور اہلِ خانہ کے نفقے کی استطاعت رکھتا ہو (آل عمران 3:97)۔ استطاعت نہ ہو تو کوئی گناہ نہیں۔\n\n• تفصیلی فقہِ مناسک: سفرِ حج سے پہلے احرام، تمتع/قران/افراد کی اقسام، طواف، سعی، وقوفِ عرفہ (9 ذوالحجہ)، مزدلفہ اور رمی جمرات کے تفصیلی احکام سیکھے جاتے ہیں۔",
                bodyAr = "• المبدأ الأساسي: يجب الحج مرةً واحدة في العمر على المستطيع مالياً وبدنياً مع أمن الطريق (آل عمران: ٩٧)، ولا إثم على غير المستطيع.\n\n• التفصيل الفقهي: يُتعلم قبل السفر ويشمل الإحرام وأنساك الحج (التمتع والقران والإفراد)، والطواف، والسعي، والوقوف بعرفة، والمبيت بمزدلفة ومنى، ورمي الجمرات.",
                bodyHi = "• बुनियादी सिद्धांत: हज जीवन में केवल एक बार फ़र्ज़ है—वह भी सिर्फ़ उन लोगों पर जो शारीरिक और आर्थिक रूप से सक्षम हों और घरवालों के खर्च का इंतज़ाम हो (क़ुरआन 3:97)।\n\n• विस्तृत फ़िक़ह: हज पर जाने से पहले एहराम, तमत्तु/क़िरान/इफ़राद, तवाफ़, सई, अरफ़ात में ठहरना और मीना के नियमों को विस्तार से सीखा जाता है।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Aal-Imran (3:97)"),
                    IslamicReference("Sahih Muslim", "Hadith 1337 (Hajj is once in a lifetime)")
                )
            )
        )
    )

    val stage6 = AcademyStage(
        stageNumber = 6,
        id = "stage_6",
        titleEn = "Stage 6: Six Foundations of Faith (Aqeedah & Iman)",
        titleUr = "مرحلہ 6: ایمان کے چھ بنیادی عقائد (ایمانِ مفصل)",
        titleAr = "المرحلة ٦: أركان الإيمان الستة والعقيدة",
        titleHi = "चरण 6: ईमान के छह बुनियादी स्तंभ (अक़ीदा)",
        subtitleEn = "Allah, Angels, Scriptures, Prophets, Day of Judgment & Qadr",
        subtitleUr = "اللہ، فرشتے، آسمانی کتب، انبیاء، یومِ آخرت اور تقدیر پر ایمان",
        subtitleAr = "الإيمان بالله وملائكته وكتبه ورسله واليوم الآخر والقدر خيره وشره",
        subtitleHi = "अल्लाह, फ़रिश्ते, आसमानी किताबें, पैगंबर, आख़िरत और तक़दीर पर विश्वास",
        overviewEn = "Build unshakeable inner certainty around the six pillars of faith answered by Prophet Muhammad ﷺ in the Hadith of Jibreel (Sahih Muslim 8).",
        overviewUr = "حدیثِ جبرائیل (صحیح مسلم: 8) میں بیان کردہ ایمان کے چھ بنیادی ستونوں کا جامع اور سادہ فہم۔",
        overviewAr = "ترسيخ أركان الإيمان الستة كما بيّنها النبي ﷺ في حديث جبريل عليه السلام (صحيح مسلم: ٨).",
        overviewHi = "हदीस-ए-जिब्रील (सहीह मुस्लिम: 8) में बताए गए ईमान के छह स्तंभों की गहरी और स्पष्ट समझ।",
        interactiveToolType = StageToolType.NONE,
        lessons = listOf(
            AcademyLesson(
                id = "s6_l1",
                stageNumber = 6,
                lessonNumber = 1,
                titleEn = "1. Belief in Allah (Creator, Sovereign & Only Worthy of Worship)",
                titleUr = "1. اللہ پر ایمان (ربوبیت، الوہیت اور اسماء و صفات)",
                titleAr = "١. الإيمان بالله تعالى ربوبيته وألوهيته وأسمائه الحسنى",
                titleHi = "1. अल्लाह पर ईमान (सृष्टिकर्ता, स्वामी और एकमात्र पूज्य)",
                summaryEn = "Knowing Allah through His creation, exclusive worship, and beautiful Names (Al-Asma Al-Husna).",
                summaryUr = "اللہ کو اس کی کائنات، خالص عبادت اور اسماءِ حسنیٰ کے ذریعے پہچاننا۔",
                summaryAr = "الإقرار بوجود الله وربوبيته وألوهيته وتفرده بالأسماء الحسنى والصفات العلى.",
                summaryHi = "अल्लाह के अस्तित्व, सृष्टि के नियंत्रण, केवल उसी की इबादत और सुंदर नामों पर ईमान।",
                bodyEn = "Belief in Allah encompasses three harmonious dimensions:\n1. He alone is the Creator, Provider, and Sustainer of the universe (Rububiyyah).\n2. He alone deserves our worship, prayer, sacrifice, and ultimate devotion (Uluhiyyah).\n3. He possesses perfect Names and Attributes (Al-Rahman, Al-Hakim, Al-Ghafur) without human imperfections or likeness.",
                bodyUr = "اللہ پر ایمان کے تین پہلو ہیں: (1) وہی اکیلا خالق، رازق اور کائنات کا مدبر ہے؛ (2) عبادت، دعا، سجدہ اور توکل صرف اسی کا حق ہے؛ (3) وہ تمام کمالات و اسماءِ حسنیٰ (الرحمٰن، الحکیم، الغفور) کا مالک ہے اور ہر عیب و مثال سے پاک ہے۔",
                bodyAr = "يتضمن الإيمان بالله الإيمان بربوبيته (الخالق الرازق المدبر)، وألوهيته (إفراده بالعبادة)، وأسمائه الحسنى وصفاته العليا على وجه الكمال اللائق به.",
                bodyHi = "अल्लाह पर ईमान के तीन मुख्य अंग हैं: (1) वही अकेला सृष्टिकर्ता और पालनहार है; (2) केवल उसी की इबादत और दुआ की जाती है; (3) वह अनंत गुणों और सुंदर नामों (अर-रहमान, अल-ग़फ़ूर, अल-हकीम) वाला है।",
                references = listOf(
                    IslamicReference("Sahih Muslim", "Hadith 8 (Hadith of Jibreel)"),
                    IslamicReference("Holy Quran", "Surah Ash-Shura (42:11)", "There is nothing like unto Him")
                ),
                quizId = "q_s6_1"
            ),
            AcademyLesson(
                id = "s6_l2",
                stageNumber = 6,
                lessonNumber = 2,
                titleEn = "2. Belief in the Angels (Al-Mala'ikah)",
                titleUr = "2. فرشتوں پر ایمان",
                titleAr = "٢. الإيمان بالملائكة",
                titleHi = "2. फ़रिश्तों पर ईमान",
                summaryEn = "Honored beings created from light who obey Allah without sin or fatigue.",
                summaryUr = "نور سے پیدا کی گئی اللہ کی معصوم مخلوق جو اس کے احکام بجا لاتی ہے۔",
                summaryAr = "مخلوقات نورانية كريمة يعبدون الله ولا يعصونه ما أمرهم ويفعلون ما يؤمرون.",
                summaryHi = "नूर से बनी अल्लाह की पाक मख़लूक़ जो कभी अल्लाह की आज्ञा नहीं टालती।",
                bodyEn = "Angels are unseen spiritual beings created from light (Sahih Muslim 2996). They have no free choice to sin, neither eat nor marry, and carry out Allah's commands tirelessly. Key angels named in revelation include:\n• Jibreel (Gabriel) — entrusted with delivering Divine revelation.\n• Mika'il — entrusted with rain and provision.\n• Israfil — who will blow the Trumpet before the Day of Judgment.\n• Kiraman Katibin — honorable angels recording every person's deeds.",
                bodyUr = "فرشتے نور سے پیدا کی گئی مخلوق ہیں جو کبھی اللہ کی نافرمانی نہیں کرتے (صحیح مسلم: 2996)۔ مشہور فرشتوں میں حضرت جبرائیلؑ (وحی لانے والے)، میکائیلؑ (بارش و رزق)، اسرافیلؑ (صور پھونکنے والے)، ملک الموت اور کراماً کاتبین (اعمال لکھنے والے) شامل ہیں۔",
                bodyAr = "الملائكة مخلوقات من نور (صحيح مسلم: ٢٩٩٦)، مكرمون لا يعصون الله. ومنهم جبريل أمين الوحي، وميكائيل الموكل بالقطر، وإسرافيل الموكل بالصور، والكرام الكاتبون.",
                bodyHi = "फ़रिश्ते नूर (प्रकाश) से बने हैं और हर पल अल्लाह के हुक्म का पालन करते हैं (सहीह मुस्लिम: 2996)। इनमें जिब्रील (वही पहुँचाने वाले), मीकाईल, इस्राफ़ील, और इंसान के अच्छे-बुरे कर्म लिखने वाले (किरामन कातिबीन) शामिल हैं।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Baqarah (2:285)"),
                    IslamicReference("Sahih Muslim", "Hadith 2996 (Angels created from light)")
                )
            ),
            AcademyLesson(
                id = "s6_l3",
                stageNumber = 6,
                lessonNumber = 3,
                titleEn = "3. Belief in the Revealed Books (Kutub Allah)",
                titleUr = "3. آسمانی کتابوں پر ایمان",
                titleAr = "٣. الإيمان بالكتب السماوية المنزلة",
                titleHi = "3. आसमानी किताबों (ग्रंथों) पर ईमान",
                summaryEn = "Affirming all original scriptures sent to prophets, culminating in the preserved Quran.",
                summaryUr = "سابقہ تمام آسمانی صحیفوں (تورات، زبور، انجیل) کی اصل وحی پر ایمان اور قرآن کی آخری حفاظت کا عقیدہ۔",
                summaryAr = "التصديق بجميع الكتب التي أنزلها الله على رسله في أصلها، والإيمان بأن القرآن الكريم خاتمها والمهيمن عليها.",
                summaryHi = "पिछली सभी आसमानी किताबों (तौरात, ज़बूर, इंजील) के मूल संदेश और अंतिम सुरक्षित क़ुरआन पर ईमान।",
                bodyEn = "Allah never left humanity without guidance; He revealed scriptures including the Scrolls of Ibrahim, Torah (Tawrah) to Musa, Psalms (Zabur) to Dawud, Gospel (Injil) to Isa, and the Final Quran to Muhammad ﷺ.\n\nMuslims revere the original revelation sent to previous prophets while recognizing that historical texts changed over centuries—whereas Allah guaranteed the complete preservation of the Quran (15:9) as the final benchmark confirming truth.",
                bodyUr = "اللہ نے ہر دور میں کتابیں اور صحیفے نازل فرمائے: صحائفِ ابراہیم، تورات (حضرت موسیٰؑ)، زبور (حضرت داؤدؑ)، انجیل (حضرت عیسیٰؑ) اور آخری کتاب قرآن مجید (حضرت محمد ﷺ)۔\n\nمسلمان سابقہ کتب کی اصل آسمانی وحی پر ایمان رکھتے ہیں، جبکہ قرآن مجید آخری اور مکمل محفوظ کتاب ہے (الحجر 15:9)۔",
                bodyAr = "نؤمن بأن الله أنزل كتباً لهداية البشر كالتوراة والزبور والإنجيل، وأن القرآن الكريم هو الكتاب الخاتم المحفوظ من التحريف (الحجر: ١٥:٩) والمهيمن على ما سبقه.",
                bodyHi = "अल्लाह ने समय-समय पर किताबें उतारीं: तौरात (मूसा अलै.), ज़बूर (दाऊद अलै.), इंजील (ईसा अलै.) और अंतिम पुस्तक क़ुरआन (मुहम्मद ﷺ)। मुसलमान पिछली किताबों की मूल शिक्षा पर विश्वास करते हैं, जबकि क़ुरआन अंतिम और पूरी तरह सुरक्षित किताब है।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Ma'idah (5:48)"),
                    IslamicReference("Holy Quran", "Surah Al-Hijr (15:9)")
                )
            ),
            AcademyLesson(
                id = "s6_l4",
                stageNumber = 6,
                lessonNumber = 4,
                titleEn = "4. Belief in All Prophets & Messengers (Rusul)",
                titleUr = "4. تمام انبیاء و رسل علیہم السلام پر ایمان",
                titleAr = "٤. الإيمان بجميع الأنبياء والمرسلين",
                titleHi = "4. सभी पैगंबरों और रसूलों पर ईमान",
                summaryEn = "Respecting every prophet without discrimination—from Adam and Ibrahim to Musa, Isa, and Muhammad ﷺ.",
                summaryUr = "بغیر کسی تفریق کے تمام انبیاء (آدم، نوح، ابراہیم، موسیٰ، عیسیٰ اور محمد ﷺ) کی تعظیم و تصدیق۔",
                summaryAr = "الإيمان بجميع الأنبياء دون تفريق، فهم إخوة دعوا جميعاً إلى توحيد الله وعبادته.",
                summaryHi = "बिना किसी भेदभाव के आदम, नूह, इब्राहीम, मूसा, ईसा और मुहम्मद ﷺ सहित सभी पैगंबरों पर ईमान।",
                bodyEn = "Muslims believe Allah sent messengers to every nation carrying the same essential core message: worship One God and live righteously (16:36). Twenty-five prophets are named in the Quran—all honored deeply: 'We make no distinction between any of His messengers' (2:285).\n\nProphets are noble, truthful humans chosen by Allah—not divine or gods—who modeled patience and mercy, ending with Prophet Muhammad ﷺ.",
                bodyUr = "اللہ نے ہر قوم میں ہدایت کے لیے انبیاء بھیجے جن کا مشترکہ پیغام ایک اللہ کی عبادت تھا (النحل 16:36)۔ قرآن میں 25 انبیاء کے نام آئے ہیں اور مومن اعلان کرتے ہیں: 'ہم اللہ کے رسولوں میں سے کسی میں تفریق نہیں کرتے' (البقرہ 2:285)۔\n\nتمام انبیاء بشر، معصوم عن الکبائر اور نہایت قابلِ احترام تھے، اور نبوت کا سلسلہ حضرت محمد ﷺ پر مکمل ہوا۔",
                bodyAr = "أرسل الله في كل أمة رسولاً يدعو إلى توحيد الله، ونحن نؤمن بهم جميعاً دون تفريق (البقرة: ٢٨٥). وهم بشر مصطفى صادقون بلغوا الرسالة بأمانة، وختمهم الله بالنبي محمد ﷺ.",
                bodyHi = "अल्लाह ने हर समुदाय में पैगंबर भेजे जिनका मूल संदेश एक ही था: एक अल्लाह की इबादत करो (16:36)। क़ुरआन में 25 पैगंबरों के नाम हैं और हम उन सब पर बिना किसी भेदभाव के ईमान रखते हैं (2:285)।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Baqarah (2:285)"),
                    IslamicReference("Holy Quran", "Surah An-Nahl (16:36)")
                )
            ),
            AcademyLesson(
                id = "s6_l5",
                stageNumber = 6,
                lessonNumber = 5,
                titleEn = "5. Belief in the Last Day, Accountability & Hereafter",
                titleUr = "5. یومِ آخرت، حساب کتاب اور جنت و جہنم پر ایمان",
                titleAr = "٥. الإيمان باليوم الآخر والحساب والبعث",
                titleHi = "5. अंतिम दिन (क़यामत), हिसाब और आख़िरत पर ईमान",
                summaryEn = "Ultimate justice, resurrection of souls, and the eternal hope of Allah's mercy and Paradise.",
                summaryUr = "موت کے بعد دوبارہ زندہ ہونا، کامل عدلِ الٰہی، میزانِ عمل اور ابدی جنت کی بشارت۔",
                summaryAr = "الإيمان بالبعث والجزاء والعدل الإلهي المطلق يوم القيامة، والجنة دار النعيم الأبدي.",
                summaryHi = "मृत्यु के बाद पुनर्जीवन, पूर्ण न्याय और अल्लाह की दया व जन्नत की आशा।",
                bodyEn = "Life does not end blindly in dust; every oppressor, victim, honest worker, and sincere heart will stand before Allah for absolute justice. Not an atom's weight of good or evil goes unnoticed (99:7-8).\n\nFaith in the Hereafter gives moral courage in hard times, consoles grief over injustice, and inspires daily kindness because every good deed endures forever with Allah.",
                bodyUr = "یہ دنیا عارضی امتحان گاہ ہے۔ موت کے بعد ہر انسان دوبارہ اٹھایا جائے گا اور ذرہ برابر نیکی یا برائی کا بھی پورا انصاف ہوگا (الزلزال 99:7-8)۔\n\nآخرت پر ایمان انسان کو ظلم سے روکتا ہے، تکلیفوں میں حوصلہ دیتا ہے اور ہر چھوٹی نیکی کی قدر سکھاتا ہے۔",
                bodyAr = "الحياة الدنيا ممر إلى دار القرار. يبعث الله الناس للحساب العادل حيث لا تُظلم نفس شيئاً (الزلزلة: ٧-٨)، فتطمئن قلوب المظلومين ويسعى المؤمن للخير.",
                bodyHi = "यह दुनिया एक परीक्षा है। मृत्यु के बाद हर इंसान को दोबारा उठाया जाएगा और ज़रा बराबर नेकी या बुराई का पूरा हिसाब होगा (क़ुरआन 99:7-8)। यह विश्वास हमें नेक रहने और हर अन्याय से बचने की प्रेरणा देता है।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Az-Zalzalah (99:7-8)"),
                    IslamicReference("Holy Quran", "Surah Al-Mulk (67:2)")
                )
            ),
            AcademyLesson(
                id = "s6_l6",
                stageNumber = 6,
                lessonNumber = 6,
                titleEn = "6. Belief in Divine Decree (Al-Qadr) & Human Responsibility",
                titleUr = "6. تقدیر (قضا و قدر) اور انسانی کوشش و ذمہ داری",
                titleAr = "٦. الإيمان بالقدر خيره وشره والعمل بالأسباب",
                titleHi = "6. तक़दीर (क़द्र) और इंसान के प्रयास व ज़िम्मेदारी पर ईमान",
                summaryEn = "Balancing trust in Allah's all-encompassing wisdom with responsible effort and choice.",
                summaryUr = "اللہ کے کامل علم و حکمت پر بھروسہ اور ساتھ ہی اسباب اختیار کرنے اور عمل کرنے کا توازن۔",
                summaryAr = "الجمع بين الإيمان بعلم الله الشامل ومشيئته، وبين الأخذ بالأسباب ومسؤولية الإنسان عن اختياراته.",
                summaryHi = "अल्लाह के संपूर्ण ज्ञान व इच्छा पर विश्वास और इंसान की अपनी कोशिश व चुनाव की ज़िम्मेदारी का संतुलन।",
                bodyEn = "Al-Qadr means Allah knows all things past, present, and future, wrote them in His knowledge, wills everything that occurs with wisdom, and created the universe—yet He also gave humans genuine choice, responsibility, and reason.\n\nProphetic balance: When planning something good, say 'Tie your camel first, then put your trust in Allah' (At-Tirmidhi 2517). Never use Qadr as an excuse for laziness or sin; instead use it to stay humble in success and peaceful during hardship.",
                bodyUr = "تقدیر پر ایمان کے چار درجے ہیں: اللہ کا کامل علم، لوحِ محفوظ میں تحریر، مشیتِ الٰہی، اور تخلیق۔ اس کے ساتھ اللہ نے انسان کو ارادہ، اختیار اور عقل دی ہے جس پر جزا و سزا ہوگی۔\n\nنبوی توازن: 'پہلے اونٹ کا گھٹنا باندھو، پھر اللہ پر بھروسہ کرو'۔ کامیابی پر غرور نہ کریں اور نقصان پر صبر کریں۔",
                bodyAr = "الإيمان بالقدر يقوم على أربع مراتب: العلم، والكتابة، والمشيئة، والخلق؛ مع إثبات إرادة الإنسان واختياره ومحاسبته، كما قال ﷺ: 'اعقلها وتوكل' (الترمذي: ٢٥١٧).",
                bodyHi = "क़द्र (तक़दीर) पर ईमान का अर्थ है कि अल्लाह सब जानता है और उसकी मर्ज़ी व हिकमत से सृष्टि चलती है—परंतु उसने इंसान को समझ और चुनाव की आज़ादी भी दी है। नबी ﷺ ने सिखाया: 'पहले ऊँट बांधो, फिर अल्लाह पर भरोसा (तवक्कुल) करो' (तिर्मिज़ी: 2517)।",
                references = listOf(
                    IslamicReference("Sahih Muslim", "Hadith 2664 (Strive for what benefits you and seek Allah's help)"),
                    IslamicReference("Jami' at-Tirmidhi", "Hadith 2517 (Tie your camel and trust Allah)")
                ),
                quizId = "q_s6_2"
            )
        )
    )

    val stage10 = AcademyStage(
        stageNumber = 10,
        id = "stage_10",
        titleEn = "Stage 10: Seerah, Caliphs, History & Character",
        titleUr = "مرحلہ 10: سیرتِ نبوی ﷺ، خلفائے راشدین اور اسلامی تاریخ",
        titleAr = "المرحلة ١٠: السيرة النبوية والخلفاء الراشدون والتاريخ",
        titleHi = "चरण 10: सीरत, ख़लीफ़ा, इस्लामिक इतिहास और चरित्र",
        subtitleEn = "Authentic Seerah, Mothers of the Believers & Four Caliphs",
        subtitleUr = "مکی و مدنی سیرت، امہات المومنین، خلفائے راشدین اور تاریخ کی تحقیق",
        subtitleAr = "السيرة المكية والمدنية، أمهات المؤمنين، الخلفاء الأربعة، ومنهج التثبت التاريخي",
        subtitleHi = "मक्की व मदनी सीरत, उम्मुल मोमिनीन, चार ख़लीफ़ा और इतिहास की परख",
        overviewEn = "Walk through the inspiring life of Prophet Muhammad ﷺ and early Muslim community, clearly separating historical consensus from debated reports.",
        overviewUr = "رسول اللہ ﷺ، ازواجِ مطہرات اور خلفائے راشدین کی مستند تاریخ—متفقہ واقعات اور اختلافی تاریخی روایات کی واضح پہچان کے ساتھ۔",
        overviewAr = "رحلة عبر السيرة النبوية وعصر الصحابة والخلفاء الراشدين بأسلوب موثق يميز بين الثابت والمختلف في تفاصيله.",
        overviewHi = "पैगंबर मुहम्मद ﷺ, अहले-बैत और सहाबा का प्रामाणिक इतिहास—सर्वमान्य घटनाओं और मतभेदों के स्पष्ट उल्लेख के साथ।",
        interactiveToolType = StageToolType.NONE,
        lessons = listOf(
            AcademyLesson(
                id = "s10_l1",
                stageNumber = 10,
                lessonNumber = 1,
                titleEn = "1. Makkan Period — Steadfastness, Mercy & First Believers",
                titleUr = "1. مکی دور: صبر، دعوتِ توحید اور سابقون الاولون",
                titleAr = "١. العهد المكي: الصبر والدعوة والسابقون الأولون",
                titleHi = "1. मक्की काल: धैर्य, सत्य का संदेश और पहले विश्वासी",
                summaryEn = "13 years of peaceful moral resilience under persecution, showing how faith shines through hardship.",
                summaryUr = "مکہ کے تیرہ سالہ صبر و استقامت اور حضرت خدیجہؓ و اولین صحابہؓ کی قربانیاں۔",
                summaryAr = "ثلاث عشرة سنة من الصبر والثبات في مكة وتربية الجيل الأول رغم الأذى والحصار.",
                summaryHi = "मक्का में 13 वर्षों का धैर्य, अत्याचार के बीच शांति और शुरुआती विश्वासियों का साहस।",
                bodyEn = "For 13 years in Makkah, the Prophet ﷺ taught monotheism, care for orphans/the poor, and accountability before Allah while enduring mockery, social boycott in Shi'b Abi Talib, and persecution of vulnerable converts like Bilal and the family of Yasir (رضي الله عنهم).\n\nNotice how the Quran trained the community in mercy, prayer, and self-restraint before any statehood existed. Khadijah (رضي الله عنها) was the very first person to believe and comfort him.",
                bodyUr = "نبوت کے ابتدائی 13 سال مکہ میں گزرے، جہاں آپ ﷺ نے توحید، اخلاق اور کمزوروں کی مدد کی دعوت دی۔ سب سے پہلے ام المومنین حضرت خدیجہؓ، حضرت ابوبکرؓ، حضرت علیؓ اور حضرت زیدؓ نے اسلام قبول کیا۔\n\nشعبِ ابی طالب کے بائیکاٹ اور حضرت بلالؓ و آلِ یاسرؓ پر مظالم کے باوجود مسلمانوں نے صبر اور دعوت کا دامن نہ چھوڑا۔",
                bodyAr = "دام العهد المكي ١٣ عاماً تركزت فيها الدعوة على التوحيد والأخلاق والآخرة. وكانت أم المؤمنين خديجة بنت خويلد أول من آمن، وصمد الصحابة كبلال وآل ياسر أمام الاضطهاد والمقاطعة.",
                bodyHi = "मक्का के 13 वर्षों में पैगंबर ﷺ ने एकेश्वरवाद और उच्च नैतिकता की शिक्षा दी। हज़रत ख़दीजा (रज़ि.) सबसे पहले ईमान लाने वाली थीं। तमाम कष्टों और सामाजिक बहिष्कार के बावजूद शुरुआती मुसलमानों ने धैर्य और सत्य का साथ नहीं छोड़ा।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 3, 3855 (Support of Khadijah)"),
                    IslamicReference("Agreed Historical Consensus", "13-year Makkan period culminating in Hijrah (622 CE)")
                ),
                quizId = "q_s10_1"
            ),
            AcademyLesson(
                id = "s10_l2",
                stageNumber = 10,
                lessonNumber = 2,
                titleEn = "2. Madinan Era — Brotherhood, Charter & Forgiveness at Makkah",
                titleUr = "2. مدنی دور: مواخات، میثاقِ مدینہ اور فتحِ مکہ کا عفوِ عام",
                titleAr = "٢. العهد المدني: المؤاخاة، وثيقة المدينة، والعفو عند فتح مكة",
                titleHi = "2. मदनी काल: भाईचारा, मदीना का चार्टर और मक्का विजय पर क्षमा",
                summaryEn = "Building a just pluralistic community, uniting Muhajirun & Ansar, and forgiving former persecutors.",
                summaryUr = "انصار و مہاجرین کی مواخات، عدلِ اجتماعی اور فتحِ مکہ کے دن بے مثال معافی۔",
                summaryAr = "تأسيس مجتمع المدينة على المؤاخاة والعدل، والتتويج بالعفو العام عند فتح مكة.",
                summaryHi = "मुहाजिर और अंसार का भाईचारा, न्यायपूर्ण समाज और मक्का विजय पर शत्रुओं को माफ़ी।",
                bodyEn = "Upon migrating to Madinah (Year 1 AH / 622 CE), the Prophet's first three acts were building Masjid an-Nabawi, pairing migrants (Muhajirun) and locals (Ansar) in brotherhood, and establishing civic mutual protection agreements.\n\nWhen Makkah opened peacefully eight years later (8 AH), instead of taking revenge on those who had persecuted him for 21 years, the Prophet ﷺ quoted Prophet Yusuf's words: 'No blame upon you today; may Allah forgive you' (Quran 12:92) and declared universal amnesty.",
                bodyUr = "ہجرتِ مدینہ (1 ہجری / 622ء) کے بعد آپ ﷺ نے مسجدِ نبوی کی تعمیر، مہاجرین و انصار میں مواخات (بھائی چارہ) اور عدل و تحفظ کے معاہدے قائم فرمائے۔\n\n8 ہجری میں جب مکہ فتح ہوا تو آپ ﷺ نے سالہا سال ظلم کرنے والوں سے انتقام لینے کے بجائے فرمایا: 'آج تم پر کوئی ملامت نہیں، جاؤ تم سب آزاد ہو'—جو انسانی تاریخ کی عظیم ترین مثال ہے۔",
                bodyAr = "بعد الهجرة (١ هـ / ٦٢٢ م) بنى النبي ﷺ المسجد النبوي، وآخى بين المهاجرين والأنصار، وأرسى وثيقة التعايش. وفي فتح مكة (٨ هـ) ضرب أروع مثل في الصفح بقوله: 'لا تثريب عليكم اليوم، اذهبوا فأنتم الطلقاء'.",
                bodyHi = "मदीना हिजरत (622 ई.) के बाद पैगंबर ﷺ ने मस्जिद बनाई, अंसार व मुहाजिर में भाईचारा कायम किया और न्याय के समझौते किए। 8 हिजरी में मक्का विजय पर उन्होंने अपने वर्षों पुराने अत्याचारियों से बदला लेने के बजाय सबको माफ़ कर दिया।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 3937, 4280 (Conquest of Makkah)"),
                    IslamicReference("Holy Quran", "Surah Al-Anfal / Surah Al-Fath")
                )
            ),
            AcademyLesson(
                id = "s10_l3",
                stageNumber = 10,
                lessonNumber = 3,
                titleEn = "3. Mothers of the Believers & Notable Companions",
                titleUr = "3. امہات المومنین اور صحابہ و صحابیات کے روشن کردار",
                titleAr = "٣. أمهات المؤمنين ومآثر الصحابة والصحابيات",
                titleHi = "3. उम्मुल मोमिनीन और सहाबा-सहाबियात का आदर्श जीवन",
                summaryEn = "Women scholars, generosity, humility, and diversity among the Companions (رضي الله عنهم).",
                summaryUr = "حضرت خدیجہؓ، حضرت عائشہؓ، حضرت بلالؓ اور حضرت سلمان فارسیؓ کی زندگیوں سے اسباق۔",
                summaryAr = "نماذج مشرقة في العلم والبذل والتآخي بين العرب وغيرهم في مدرسة النبوة.",
                summaryHi = "हज़रत ख़दीजा, हज़रत आयशा, बिलाल व सलमान फ़ारसी (रज़ि.) के ज्ञान, उदारता और समानता से सीख।",
                bodyEn = "The Companions came from every background—Arabs, Africans (Bilal ibn Rabah), Persians (Salman al-Farisi), Romans (Suhayb ar-Rumi)—united solely by faith and righteousness.\n\nWomen held central scholarly and charitable roles: Khadijah bint Khuwaylid financed the early community with unwavering faith, while Aisha bint Abi Bakr (رضي الله عنهما) became one of the foremost jurists and narrators of over 2,200 authentic Hadith—senior male companions regularly sought her rulings.",
                bodyUr = "صحابہ کرامؓ میں عرب، حبشی (حضرت بلالؓ)، فارسی (حضرت سلمانؓ) اور رومی (حضرت صہیبؓ) سب ایمان اور تقویٰ کی بنیاد پر برابر تھے۔\n\nخواتین نے علم و خدمت میں بنیادی کردار ادا کیا: حضرت خدیجہؓ نے ہر مشکل میں مال و جان سے ساتھ دیا اور ام المومنین حضرت عائشہؓ اسلام کی عظیم فقیہہ اور محدثہ تھیں جن سے بڑے بڑے صحابہؓ مسائل پوچھا کرتے تھے۔",
                bodyAr = "تنوع الصحابة من كل الأجناس (بلال الحبشي، سلمان الفارسي، صهيب الرومي) في أخوة إيمانية خالصة. وكان للنساء دور علمي بارز: كخديجة بنت خويلد في التضحية والدعم، وعائشة بنت أبي بكر رضي الله عنها بوصفها من كبار الفقهاء والمحدثين.",
                bodyHi = "सहाबा में अरब, हब्शी (हज़रत बिलाल), फ़ारसी (सलमान फ़ारसी) और रोमन (सुहैब रूमी) सभी बराबरी से शामिल थे। महिलाओं ने शिक्षा व सेवा में अग्रणी भूमिका निभाई—हज़रत ख़दीजा (रज़ि.) प्रथम समर्थक थीं और हज़रत आयशा (रज़ि.) इस्लाम की महान विद्वान व 2,200 से अधिक हदीसों की रिवायतकर्ता थीं।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 3754 (Virtues of Companions), 3816"),
                    IslamicReference("Sahih Muslim", "Hadith 2432")
                )
            ),
            AcademyLesson(
                id = "s10_l4",
                stageNumber = 10,
                lessonNumber = 4,
                titleEn = "4. The Four Rightly Guided Caliphs (Al-Khulafa Ar-Rashidun)",
                titleUr = "4. خلفائے راشدین (ابوبکر، عمر، عثمان اور علی رضی اللہ عنہم)",
                titleAr = "٤. الخلفاء الراشدون الأربعة رضي الله عنهم",
                titleHi = "4. चार नेक ख़लीफ़ा (अबू बक्र, उमर, उस्मान, अली रज़ि.)",
                summaryEn = "Leadership rooted in consultation (Shura), accountability, preserving the Quran, and public welfare.",
                summaryUr = "مشاورت، عدلِ فاروقی، تدوین و اشاعتِ قرآن اور علم و شجاعت کا مثالی دور۔",
                summaryAr = "نماذج الحكم القائم على الشورى والعدل وجمع المصحف ورعاية حقوق الناس.",
                summaryHi = "परामर्श (शूरा), न्याय, क़ुरआन का संकलन और जन-कल्याण पर आधारित नेतृत्व।",
                bodyEn = "1. Abu Bakr As-Siddiq (11–13 AH): Kept the community united after the Prophet's passing and initiated the single bound compilation of the Quran.\n2. Umar ibn Al-Khattab (13–23 AH): Famous for impartial justice, establishing state stipends for the elderly/widows/orphans, and the Hijri calendar.\n3. Uthman ibn Affan (23–35 AH): Standard-bearer of generosity and distributed uniform verified Mushaf copies across major regions.\n4. Ali ibn Abi Talib (35–40 AH): Renowned for deep knowledge of Quran, eloquence, justice, and courage during difficult civil trials.",
                bodyUr = "1. حضرت ابوبکر صدیقؓ (11-13ھ): وصالِ نبوی کے بعد امت کو متحد رکھا اور قرآن مجید کو یکجا صحیفے کی شکل میں جمع فرمایا۔\n2. حضرت عمر فاروقؓ (13-23ھ): بے مثال عدل، بیت المال کا نظام، عوام کی خبرگیری اور ہجری تقویم کا آغاز۔\n3. حضرت عثمان غنیؓ (23-35ھ): سخاوت کا پیکر اور مصاحفِ قرآنی کے مستند نسخوں کی اشاعت۔\n4. حضرت علی المرتضیٰؓ (35-40ھ): علم و حکمت، عدالت، خطابت اور مشکل دور میں استقامت کا نمونہ۔",
                bodyAr = "١. أبو بكر الصديق (١١-١٣ هـ): ثبّت الأمة وجمع القرآن في مصحف واحد.\n٢. عمر بن الخطاب (١٣-٢٣ هـ): العدل المؤسسي والدواوين والتقويم الهجري.\n٣. عثمان بن عفان (٢٣-٣٥ هـ): الجود ونسخ المصحف الإمام وتوزيعه.\n٤. علي بن أبي طالب (٣٥-٤٠ هـ): القضاء والعلم والشجاعة.",
                bodyHi = "1. हज़रत अबू बक्र सिद्दीक़ (11-13 हि.): समुदाय को एकजुट रखा और क़ुरआन को एक ग्रंथ में संकलित कराया।\n2. हज़रत उमर फ़ारूक़ (13-23 हि.): निष्पक्ष न्याय, जन-कल्याण विभाग और हिजरी कैलेंडर की शुरुआत।\n3. हज़रत उस्मान ग़नी (23-35 हि.): उदारता व प्रामाणिक क़ुरआन प्रतियों का प्रसार।\n4. हज़रत अली (35-40 हि.): गहन ज्ञान, न्याय और कठिन समय में धैर्य।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 3668, 3689, 3695, 3706 (Virtues of the Four Caliphs)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 4986-4987 (Compilation of Quran under Abu Bakr & Uthman)")
                ),
                quizId = "q_s10_2"
            ),
            AcademyLesson(
                id = "s10_l5",
                stageNumber = 10,
                lessonNumber = 5,
                titleEn = "5. Reading History Responsibly: Consensus vs Disputed Reports",
                titleUr = "5. تاریخ کے مطالعے کا اصول: متفقہ حقائق بمقابلہ اختلافی روایات",
                titleAr = "٥. منهج قراءة التاريخ: الثابت المتفق عليه مقابل المرويات المختلف فيها",
                titleHi = "5. इतिहास पढ़ने का सही तरीक़ा: सर्वमान्य तथ्य बनाम विवादित विवरण",
                summaryEn = "Distinguishing firmly authenticated facts from later unverified stories or exact-date debates.",
                summaryUr = "مستند احادیث و متفقہ تاریخ کو کمزور قصوں اور دقیق تاریخوں کے اختلاف سے الگ رکھنا۔",
                summaryAr = "التفريق العلمي بين السيرة الصحيحة والإجماع التاريخي وبين الأخبار الضعيفة أو خلاف التواريخ.",
                summaryHi = "प्रमाणित इतिहास को कमज़ोर किस्सों और सटीक तारीख़ों के मतभेदों से अलग समझना।",
                bodyEn = "Responsible Islamic scholarship teaches intellectual honesty:\n• Certainty Tier (Sahih / Consensus): Five daily prayers, Hijrah, Revelation in Makkah & Madinah, Badr/Uhud/Khandaq/Conquest of Makkah, Farewell Pilgrimage—all multiply attested.\n• Historical Estimate / Debate Tier: Some secondary chronology dates (e.g., exact day of Prophet's birth in Rabi al-Awwal—9th vs 12th vs others—or minor battle casualty numbers) differ across early historians like Ibn Ishaq, Al-Waqidi, and Al-Tabari.\n\nWe state clearly what is established versus where historians differ, and never treat unverified popular legends as binding faith.",
                bodyUr = "تحقیقی اسلامی تاریخ میں دو درجوں کا واضح فرق رکھا جاتا ہے:\n• قطعی و متفقہ تاریخ: ہجرت، غزواتِ بدر و احد و خندق، صلحِ حدیبیہ، فتحِ مکہ اور حجۃ الوداع صحیح اسناد اور تواتر سے ثابت ہیں۔\n• اختلافی یا ظنی تفصیلات: بعض ثانوی تاریخیں (مثلاً یومِ ولادت کی دقیق تاریخ 9 یا 12 ربیع الاول، یا بعض مبالغہ آمیز قصے) مورخین میں مختلف فیہ ہیں۔ مستند علم متفقہ حقائق کو کمزور قصوں سے الگ رکھتا ہے۔",
                bodyAr = "يميز المؤرخون والعلماء بين:\n١. الثابت القطعي (كالهجرة وبدر وأحد والفتح وحجة الوداع وجمع القرآن).\n٢. المختلف فيه أو التقديري (كالتاريخ الدقيق ليوم المولد بين ٩ و١٢ ربيع الأول وبعض تفاصيل الروايات التاريخية غير المسندة).\nوهكذا نلتزم الأمانة العلمية دون جزم بما لم يثبت.",
                bodyHi = "सही ऐतिहासिक दृष्टिकोण दो स्तरों में अंतर करता है:\n• सर्वमान्य तथ्य: हिजरत, बद्र, उहुद, मक्का विजय, अंतिम हज और क़ुरआन का संकलन पूर्णतः प्रमाणित हैं।\n• मतभेद के बिंदु: कुछ द्वितीयक तारीख़ें (जैसे पैदाइश की सटीक तारीख़ 9 या 12 रबी-उल-अव्वल) या बिना सनद की कहानियाँ इतिहासकारों में अलग-अलग मिलती हैं। हम प्रमाणित तथ्यों को अनुमान से अलग रखते हैं।",
                references = listOf(
                    IslamicReference("Methodology Note", "Distinction between Sahih Hadith standards & Akhbar/Tarikh chronicles (Ibn Kathir's Al-Bidayah)")
                )
            )
        )
    )

    val allStages5To6And10: List<AcademyStage> = listOf(stage5, stage6, stage10)

    val stageCurriculumQuizzes: List<AcademyQuizQuestion> = listOf(
        AcademyQuizQuestion(
            id = "q_s1_1",
            stageNumber = 1,
            category = "Stage 1 · Islam Basics",
            questionEn = "What does the word 'Islam' literally signify?",
            questionUr = "لفظ 'اسلام' کا بنیادی لغوی مفہوم کیا ہے؟",
            questionAr = "ما هو المعنى الأساسي لكلمة 'الإسلام'؟",
            questionHi = "शब्द 'इस्लाम' का मूल अर्थ क्या है?",
            arabicPrompt = "الْإِسْلَام",
            optionsEn = listOf(
                "Peaceful, willing submission to the One Creator (Allah)",
                "Following only one cultural tribe",
                "Worshiping angels and saints",
                "Monastic isolation from society"
            ),
            optionsUr = listOf(
                "ایک خالق (اللہ) کے سامنے سلامتی اور رضا سے جھک جانا",
                "صرف کسی ایک قبیلے کی ثقافت",
                "فرشتوں کی عبادت",
                "دنیا و معاشرے سے مکمل کٹ جانا"
            ),
            optionsAr = listOf(
                "الاستسلام لله بالتوحيد والانقياد له بالطاعة",
                "الانتماء لقبيلة محددة",
                "عبادة الملائكة",
                "الرهبنة وترك المجتمع"
            ),
            optionsHi = listOf(
                "एक ईश्वर (अल्लाह) के सामने शांतिपूर्वक समर्पण करना",
                "केवल एक कबीले की परंपरा मानना",
                "फ़रिश्तों की पूजा करना",
                "समाज से पूरी तरह दूर रहना"
            ),
            correctIndex = 0,
            explanationEn = "Islam comes from the root S-L-M (peace/safety) and means peacefully submitting to Allah alone.",
            explanationUr = "اسلام کا مادہ س-ل-م ہے جس کا معنی امن، سلامتی اور اللہ کی اطاعت ہے۔",
            explanationAr = "الإسلام هو الاستسلام لله بالتوحيد والطاعة مع تحقيق السلام الداخلي والاجتماعي.",
            explanationHi = "इस्लाम का अर्थ एक सृष्टिकर्ता अल्लाह की आज्ञा में शांतिपूर्ण समर्पण करना है।"
        ),
        AcademyQuizQuestion(
            id = "q_s1_2",
            stageNumber = 1,
            category = "Stage 1 · Knowing Allah",
            questionEn = "Which short Surah summarizes Allah's Absolute Oneness in four verses?",
            questionUr = "کون سی مختصر سورت چار آیات میں اللہ کی وحدانیتِ خالص بیان کرتی ہے؟",
            questionAr = "أي سورة قصيرة تلخص توحيد الله الخالص في أربع آيات؟",
            questionHi = "कौन-सी छोटी सूरह चार आयतों में अल्लाह की एकता (तौहीद) को स्पष्ट करती है?",
            arabicPrompt = "قُلْ هُوَ اللَّهُ أَحَدٌ",
            optionsEn = listOf("Surah Al-Ikhlas (112)", "Surah Al-Fil (105)", "Surah Al-Asr (103)", "Surah Al-Kawthar (108)"),
            optionsUr = listOf("سورۃ الإخلاص (112)", "سورۃ الفیل (105)", "سورۃ العصر (103)", "سورۃ الکوثر (108)"),
            optionsAr = listOf("سورة الإخلاص (١١٢)", "سورة الفيل (١٠٥)", "سورة العصر (١٠٣)", "سورة الكوثر (١٠٨)"),
            optionsHi = listOf("सूरह अल-इख़्लास (112)", "सूरह अल-फ़ील (105)", "सूरह अल-अस्र (103)", "सूरह अल-कौसर (108)"),
            correctIndex = 0,
            explanationEn = "Surah Al-Ikhlas (Chapter 112) affirms Allah is One, Eternal Refuge, with no parent, child, or equal.",
            explanationUr = "سورۃ الإخلاص خالص توحید یعنی اللہ کے ایک اور بے نیاز ہونے کا واضح اعلان ہے۔",
            explanationAr = "سورة الإخلاص (١١٢) هي سورة التوحيد الخالص وتنزيه الله عن الشبيه والولد والوالد.",
            explanationHi = "सूरह अल-इख़्लास (112) बताती है कि अल्लाह एक है, स्वावलंबी है और उसका कोई बराबर नहीं।"
        ),
        AcademyQuizQuestion(
            id = "q_s2_1",
            stageNumber = 2,
            category = "Stage 2 · Purification",
            questionEn = "Which four parts are explicitly listed as obligatory acts of Wudu in Quran 5:6?",
            questionUr = "سورۃ المائدہ (5:6) میں وضو کے کن چار اعضاء کا حکم صراحت سے آیا ہے؟",
            questionAr = "ما هي فروض الوضوء الأربعة المنصوص عليها في سورة المائدة (٥:٦)؟",
            questionHi = "क़ुरआन (5:6) में वुज़ू के किन चार अंगों का स्पष्ट उल्लेख है?",
            optionsEn = listOf(
                "Face, forearms to elbows, wiping head, and feet to ankles",
                "Chest, back, shoulders, and knees",
                "Hands only",
                "Full body shower every prayer"
            ),
            optionsUr = listOf(
                "چہرہ، کہنیوں سمیت ہاتھ، سر کا مسح اور ٹخنوں سمیت پاؤں",
                "سینہ، کمر، کندھے اور گھٹنے",
                "صرف دونوں ہاتھ",
                "ہر نماز کے لیے پورا غسل"
            ),
            optionsAr = listOf(
                "الوجه، واليدان إلى المرفقين، ومسح الرأس، والرجلان إلى الكعبين",
                "الصدر والظهر والركبتان",
                "الكفان فقط",
                "الاغتسال الكامل لكل صلاة"
            ),
            optionsHi = listOf(
                "चेहरा, कुहनियों तक हाथ, सिर का मसह और टखनों तक पैर",
                "सीना, पीठ, कंधे और घुटने",
                "केवल हाथ",
                "हर नमाज़ से पहले पूरा स्नान"
            ),
            correctIndex = 0,
            explanationEn = "Quran 5:6 lists washing the face, arms to elbows, wiping the head, and washing feet to ankles.",
            explanationUr = "سورۃ المائدہ 5:6 میں وضو کے یہ چار بنیادی فرائض بیان کیے گئے ہیں۔",
            explanationAr = "ذكرت آية المائدة (٥:٦) هذه الأعضاء الأربعة أساساً للوضوء، مع اختلاف معتبر في قدر مسح الرأس.",
            explanationHi = "क़ुरआन (5:6) में चेहरा, कुहनियों तक हाथ, सिर का मसह और टखनों तक पैर का वर्णन है।"
        ),
        AcademyQuizQuestion(
            id = "q_s3_1",
            stageNumber = 3,
            category = "Stage 3 · Salah Steps",
            questionEn = "Which Surah is recited in every Rak'ah of Salah and serves as a dialogue with Allah?",
            questionUr = "نماز کی ہر رکعت میں کون سی سورت پڑھی جاتی ہے جو بندے اور رب کا مکالمہ ہے؟",
            questionAr = "ما هي السورة التي تُقرأ في كل ركعة من الصلاة وتُسمى أم الكتاب؟",
            questionHi = "नमाज़ की हर रकअत में कौन-सी सूरह पढ़ी जाती है?",
            arabicPrompt = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            optionsEn = listOf("Surah Al-Fatihah", "Surah Al-Kahf", "Surah Yasin", "Surah Ar-Rahman"),
            optionsUr = listOf("سورۃ الفاتحہ", "سورۃ الکہف", "سورۃ یٰسین", "سورۃ الرحمن"),
            optionsAr = listOf("سورة الفاتحة", "سورة الكهف", "سورة يس", "سورة الرحمن"),
            optionsHi = listOf("सूरह अल-फ़ातिहा", "सूरह अल-कहफ़", "सूरह यासीन", "सूरह अर-रहमान"),
            correctIndex = 0,
            explanationEn = "Surah Al-Fatihah is essential in prayer; Sahih Muslim 395 explains Allah responds to each verse.",
            explanationUr = "سورۃ الفاتحہ نماز کا بنیادی رکن ہے (صحیح مسلم: 395)۔",
            explanationAr = "سورة الفاتحة ركن أساسي في الصلاة ويستجيب الله فيها لدعاء عبده (صحيح مسلم: ٣٩٥).",
            explanationHi = "सूरह अल-फ़ातिहा नमाज़ का अनिवार्य हिस्सा है (सहीह मुस्लिम: 395)।"
        ),
        AcademyQuizQuestion(
            id = "q_s3_2",
            stageNumber = 3,
            category = "Stage 3 · Salah Words",
            questionEn = "What is said while in Sujood (prostration)?",
            questionUr = "سجدے کی حالت میں کون سی تسبیح پڑھی جاتی ہے؟",
            questionAr = "ما هو التسبيح المشروع في السجود؟",
            questionHi = "सजदे की हालत में क्या पढ़ा जाता है?",
            arabicPrompt = "سُبْحَانَ رَبِّيَ الْأَعْلَى",
            optionsEn = listOf(
                "Subhana Rabbiyal-A'la (Glory be to my Lord, the Most High)",
                "Sami'Allahu liman hamidah",
                "Rabbana wa lakal-hamd",
                "Qul Huwa Allahu Ahad"
            ),
            optionsUr = listOf(
                "سبحان ربی الاعلیٰ (میرا بلند و برتر رب پاک ہے)",
                "سمع اللہ لمن حمدہ",
                "ربنا و لک الحمد",
                "قل ھو اللہ احد"
            ),
            optionsAr = listOf(
                "سبحان ربي الأعلى",
                "سمع الله لمن حمده",
                "ربنا ولك الحمد",
                "قل هو الله أحد"
            ),
            optionsHi = listOf(
                "सुब्हाना रब्बियल आला (मेरा सर्वोच्च रब पवित्र है)",
                "समिअल्लाहु लिमन हमिदा",
                "रब्बना व लकल हम्द",
                "क़ुल हुवल्लाहु अहद"
            ),
            correctIndex = 0,
            explanationEn = "In Ruku we say Subhana Rabbiyal-'Adheem; in Sujood we say Subhana Rabbiyal-A'la (Sahih Muslim 772).",
            explanationUr = "رکوع میں 'سبحان ربی العظیم' اور سجدے میں 'سبحان ربی الاعلیٰ' پڑھا جاتا ہے۔",
            explanationAr = "في الركوع يُقال 'سبحان ربي العظيم' وفي السجود 'سبحان ربي الأعلى' (صحيح مسلم: ٧٧٢).",
            explanationHi = "रुकू में 'सुब्हाना रब्बियल अज़ीम' और सजदे में 'सुब्हाना रब्बियल आला' कहा जाता है (सहीह मुस्लिम: 772)।"
        ),
        AcademyQuizQuestion(
            id = "q_s4_1",
            stageNumber = 4,
            category = "Stage 4 · Islamic Character",
            questionEn = "According to Quran 31:15, how must a Muslim treat parents who hold a different faith?",
            questionUr = "سورۃ لقمان (31:15) کے مطابق مختلف عقیدے کے والدین کے ساتھ کیسا برتاؤ لازم ہے؟",
            questionAr = "كيف يأمر القرآن الكريم (لقمان: ١٥) بمعاملة الوالدين غير المسلمين؟",
            questionHi = "क़ुरआन (31:15) के अनुसार अलग आस्था रखने वाले माता-पिता के साथ कैसा व्यवहार करना चाहिए?",
            optionsEn = listOf(
                "Accompany and care for them with kindness while politely declining any act of Shirk",
                "Cut off all family speech permanently",
                "Respond harshly whenever they speak",
                "Follow Shirk to please them"
            ),
            optionsUr = listOf(
                "شرک میں اطاعت کے بغیر دنیا میں ان کے ساتھ نہایت اچھا اور باادب سلوک رکھنا",
                "ان سے تمام تعلق بالکل ختم کر دینا",
                "ان سے سخت لہجے میں بات کرنا",
                "خوش کرنے کے لیے شرک قبول کر لینا"
            ),
            optionsAr = listOf(
                "عدم طاعتهما في الشرك مع مصاحبتهما في الدنيا بالمعروف والإحسان",
                "مقاطعتهما نهائياً",
                "الغلظة عليهما في القول",
                "طاعتهما في الشرك"
            ),
            optionsHi = listOf(
                "शिर्क की बात न मानते हुए दुनिया में उनके साथ सम्मान और भलाई से पेश आना",
                "उनसे सारे संबंध हमेशा के लिए तोड़ देना",
                "उनसे कठोर भाषा में बात करना",
                "उन्हें खुश करने के लिए शिर्क मान लेना"
            ),
            correctIndex = 0,
            explanationEn = "Quran 31:15 commands: 'Accompany them in this world with kindness'—Islam upholds family compassion.",
            explanationUr = "سورۃ لقمان 31:15 کا حکم ہے: دنیا میں والدین کے ساتھ بھلائی اور حسنِ سلوک سے رہو۔",
            explanationAr = "قوله تعالى 'وصاحبهما في الدنيا معروفا' يجمع بين ثبات العقيدة وبر الوالدين.",
            explanationHi = "क़ुरआन (31:15) सिखाता है कि दुनिया में माता-पिता के साथ सम्मान और सेवा का संबंध बनाए रखें।"
        ),
        AcademyQuizQuestion(
            id = "q_s5_1",
            stageNumber = 5,
            category = "Stage 5 · Five Pillars",
            questionEn = "How many Pillars of Islam did Prophet Muhammad ﷺ mention in the well-known Hadith of Ibn Umar (Bukhari 8)?",
            questionUr = "صحیح بخاری (حدیث 8) میں اسلام کی بنیاد کتنے ارکان پر بتائی گئی ہے؟",
            questionAr = "كم عدد أركان الإسلام المذكورة في حديث ابن عمر (البخاري: ٨)؟",
            questionHi = "सहीह बुख़ारी (हदीस 8) के अनुसार इस्लाम कितने स्तंभों पर आधारित है?",
            optionsEn = listOf(
                "Five: Shahadah, Salah, Zakat, Hajj, and Fasting Ramadan",
                "Three pillars only",
                "Ten pillars",
                "Seven pillars"
            ),
            optionsUr = listOf(
                "پانچ: شہادت، نماز، زکوٰۃ، حج اور رمضان کے روزے",
                "صرف تین",
                "دس",
                "سات"
            ),
            optionsAr = listOf(
                "خمسة: الشهادتان وإقام الصلاة وإيتاء الزكاة والحج وصوم رمضان",
                "ثلاثة فقط",
                "عشرة",
                "سبعة"
            ),
            optionsHi = listOf(
                "पाँच: शहादा, नमाज़, ज़कात, हज और रमज़ान के रोज़े",
                "केवल तीन",
                "दस",
                "सात"
            ),
            correctIndex = 0,
            explanationEn = "Bukhari 8 & Muslim 16: Islam is built upon five pillars—Shahadah, Salah, Zakat, Hajj, and Ramadan fasting.",
            explanationUr = "متفق علیہ حدیث کے مطابق اسلام کے پانچ بنیادی ارکان ہیں (شہادت، نماز، زکوٰۃ، حج، روزہ)۔",
            explanationAr = "بني الإسلام على خمس (متفق عليه).",
            explanationHi = "सहीह बुख़ारी (8) में इस्लाम के पाँच स्तंभ बताए गए हैं।"
        ),
        AcademyQuizQuestion(
            id = "q_s5_2",
            stageNumber = 5,
            category = "Stage 5 · Zakat Intro",
            questionEn = "What is the standard annual Zakat rate on eligible savings kept above Nisab for one lunar year?",
            questionUr = "نصاب سے زائد سال بھر کی جمع پونجی پر زکوٰۃ کی مقررہ شرح کیا ہے؟",
            questionAr = "ما هي نسبة الزكاة السنوية على المدخرات البالغة للنصاب بعد تمام الحول؟",
            questionHi = "निसाब से अधिक पूरे चाँद वर्ष की बचत पर ज़कात की मानक दर क्या है?",
            optionsEn = listOf("2.5% (one-fortieth)", "10% on all salary automatically", "50% of all property", "1% monthly"),
            optionsUr = listOf("2.5 فیصد (چالیسواں حصہ)", "10 فیصد تمام آمدنی پر", "50 فیصد", "1 فیصد ماہانہ"),
            optionsAr = listOf("٢.٥٪ (ربع العشر)", "١٠٪ من كل راتب", "٥٠٪", "١٪ شهرياً"),
            optionsHi = listOf("2.5% (चालीसवाँ हिस्सा)", "10% सभी आय पर", "50%", "1% मासिक"),
            correctIndex = 0,
            explanationEn = "Annual Zakat on net qualifying cash, gold, silver, and trade goods held one lunar year above Nisab is 2.5%.",
            explanationUr = "نقدی، سونے چاندی اور تجارتی مال پر سالانہ زکوٰۃ کی شرح 2.5 فیصد ہے۔",
            explanationAr = "الواجب في النقود وعروض التجارة ربع العشر أي ٢.٥٪ إذا بلغت النصاب وحال عليها الحول.",
            explanationHi = "निसाब से अधिक एक चाँद वर्ष तक रहने वाली नक़दी व सोने-चाँदी पर वार्षिक ज़कात 2.5% है।"
        ),
        AcademyQuizQuestion(
            id = "q_s6_1",
            stageNumber = 6,
            category = "Stage 6 · Pillars of Iman",
            questionEn = "Which of the following is NOT one of the Six Pillars of Iman?",
            questionUr = "درج ذیل میں سے کون سا ایمان کے چھ بنیادی ارکان میں شامل نہیں ہے؟",
            questionAr = "أي من الآتي ليس من أركان الإيمان الستة؟",
            questionHi = "निम्न में से कौन-सा ईमान के छह स्तंभों में शामिल नहीं है?",
            optionsEn = listOf(
                "Belief in stars controlling destiny",
                "Belief in Angels & Revealed Books",
                "Belief in Allah & His Messengers",
                "Belief in the Last Day & Divine Decree (Al-Qadr)"
            ),
            optionsUr = listOf(
                "ستاروں کے تقدیر بدلنے کا عقیدہ",
                "فرشتوں اور آسمانی کتابوں پر ایمان",
                "اللہ اور رسولوں پر ایمان",
                "آخرت اور تقدیر پر ایمان"
            ),
            optionsAr = listOf(
                "الاعتقاد بتأثير النجوم في القدر",
                "الإيمان بالملائكة والكتب",
                "الإيمان بالله ورسله",
                "الإيمان باليوم الآخر والقدر"
            ),
            optionsHi = listOf(
                "सितारों द्वारा भाग्य तय करने का विश्वास",
                "फ़रिश्तों और आसमानी किताबों पर ईमान",
                "अल्लाह और पैगंबरों पर ईमान",
                "आख़िरत और तक़दीर पर ईमान"
            ),
            correctIndex = 0,
            explanationEn = "The 6 Pillars of Iman (Muslim 8) are Allah, His Angels, His Books, His Messengers, the Last Day, and Al-Qadr.",
            explanationUr = "ستاروں یا فال سے قسمت ماننا اسلام میں باطل ہے؛ ایمان کے 6 ارکان حدیثِ جبرائیل میں واضح ہیں۔",
            explanationAr = "أركان الإيمان ستة (صحيح مسلم: ٨)، ولا أثر للنجوم أو التنجيم في القدر.",
            explanationHi = "सहीह मुस्लिम (8) के अनुसार ईमान के छह स्तंभ हैं—अल्लाह, फ़रिश्ते, किताबें, पैगंबर, आख़िरत और क़द्र।"
        ),
        AcademyQuizQuestion(
            id = "q_s6_2",
            stageNumber = 6,
            category = "Stage 6 · Balance of Qadr",
            questionEn = "How did Prophet Muhammad ﷺ teach balancing effort with trust in Allah (Tawakkul)?",
            questionUr = "نبی ﷺ نے تدبیر (کوشش) اور توکل کے درمیان کیا توازن سکھایا؟",
            questionAr = "كيف علّم النبي ﷺ التوازن بين الأخذ بالأسباب والتوكل على الله؟",
            questionHi = "पैगंबर ﷺ ने अपनी कोशिश और अल्लाह पर भरोसे (तवक्कुल) के बीच क्या संतुलन सिखाया?",
            optionsEn = listOf(
                "Tie your camel first, then put your trust in Allah",
                "Leave everything unguarded and do nothing",
                "Rely solely on effort and never make Dua",
                "Wait passively without working"
            ),
            optionsUr = listOf(
                "پہلے اونٹ باندھو (اسباب اختیار کرو)، پھر اللہ پر بھروسہ کرو",
                "بغیر تدبیر سب کھلا چھوڑ دو",
                "صرف محنت پر بھروسہ کرو اور دعا نہ مانگو",
                "بغیر کوشش ہاتھ پر ہاتھ رکھ کر بیٹھے رہو"
            ),
            optionsAr = listOf(
                "اعقلها وتوكل (الأخذ بالأسباب مع الاعتماد القلبي على الله)",
                "إهمال الأسباب كلياً",
                "الاعتماد على النفس دون دعاء",
                "الكسل وانتظار الرزق بلا سعي"
            ),
            optionsHi = listOf(
                "पहले अपना ऊँट बांधो (पूरी कोशिश करो), फिर अल्लाह पर भरोसा रखो",
                "बिना प्रयास किए सब छोड़ दो",
                "केवल अपनी मेहनत पर घमंड करो",
                "आलस्य में बैठे रहो"
            ),
            correctIndex = 0,
            explanationEn = "'Tie your camel and trust in Allah' (At-Tirmidhi 2517) balances human responsibility and reliance on Allah.",
            explanationUr = "'اعقلها وتوكل' اسباب اختیار کرنے اور دل سے اللہ پر بھروسہ رکھنے کی جامع تعلیم ہے۔",
            explanationAr = "حديث الترمذي (٢٥١٧) يجمع بين الأخذ الجاد بالأسباب والتوكل الصادق على الله.",
            explanationHi = "'अपना ऊँट बांधो और अल्लाह पर भरोसा रखो' प्रयास और विश्वास का सुंदर संतुलन है।"
        ),
        AcademyQuizQuestion(
            id = "q_s8_1",
            stageNumber = 8,
            category = "Stage 8 · Word-by-Word",
            questionEn = "In Surah Al-Asr (103:1), what does 'Al-Asr' refer to?",
            questionUr = "سورۃ العصر میں 'وَالْعَصْرِ' میں 'عصر' سے کیا مراد ہے؟",
            questionAr = "في سورة العصر، ما معنى قوله تعالى 'وَالْعَصْرِ'؟",
            questionHi = "सूरह अल-अस्र (103:1) में 'अल-अस्र' का अर्थ क्या है?",
            arabicPrompt = "وَالْعَصْرِ",
            optionsEn = listOf("Time / Passing Ages", "Mountains", "Oceans", "Stars"),
            optionsUr = listOf("گزرتا ہوا زمانہ / وقت", "پہاڑ", "سمندر", "ستارے"),
            optionsAr = listOf("الزمن والدهر", "الجبال", "البحار", "النجوم"),
            optionsHi = listOf("गुज़रता समय / युग", "पहाड़", "समुद्र", "तारे"),
            correctIndex = 0,
            explanationEn = "Allah swears by time/the passing age (Al-Asr) to remind humanity that life's hours are precious capital.",
            explanationUr = "اللہ نے گزرتے وقت کی قسم کھا کر انسان کو اس کی قدر و قیمت سے آگاہ فرمایا ہے۔",
            explanationAr = "أقسم الله بالعصر أي الزمن للدلالة على خطورة إهدار العمر دون إيمان وعمل صالح.",
            explanationHi = "अल्लाह ने समय (अल-अस्र) की क़सम खाकर बताया है कि इंसान की असली पूँजी उसका समय है।"
        ),
        AcademyQuizQuestion(
            id = "q_s9_1",
            stageNumber = 9,
            category = "Stage 9 · Hifz Method",
            questionEn = "In traditional Hifz planning, what do 'Sabaq', 'Sabqi', and 'Dhor' represent?",
            questionUr = "حفظِ قرآن کے نظام میں 'سبق'، 'سبقی' اور 'دہر (منزل)' سے کیا مراد ہے؟",
            questionAr = "في منهج حفظ القرآن، ماذا تعني مراحل: الدرس الجديد والمراجعة القريبة والبعيدة؟",
            questionHi = "हिफ़्ज़ की पद्धति में 'सबक़', 'सबक़ी' और 'दोहर' क्या हैं?",
            optionsEn = listOf(
                "New portion (Sabaq), recent portion revision (Sabqi), and older cumulative revision (Dhor)",
                "Three types of food",
                "Three chapters only",
                "Memorizing without ever revising"
            ),
            optionsUr = listOf(
                "آج کا نیا سبق، پچھلے حالیہ پارے کی دہرائی (سبقی)، اور پرانے حفظ کی پختہ دہرائی (منزل)",
                "تین قسم کے کھانے",
                "صرف تین سورتیں",
                "بغیر دہرائے صرف آگے بڑھنا"
            ),
            optionsAr = listOf(
                "الحفظ الجديد، ومراجعة القريب، ومراجعة المحفوظ القديم",
                "ثلاثة أنواع من الأطعمة",
                "ثلاث سور فقط",
                "الحفظ دون مراجعة"
            ),
            optionsHi = listOf(
                "नया पाठ (सबक़), हाल के पाठ का दोहराव (सबक़ी) और पुराने याद किए हिस्से का नियमित दोहराव (दोहर)",
                "तीन प्रकार के भोजन",
                "केवल तीन सूरह",
                "बिना दोहराए आगे बढ़ना"
            ),
            correctIndex = 0,
            explanationEn = "Sustainable Hifz combines new memorization (Sabaq) with consistent daily revision (Sabqi & Dhor).",
            explanationUr = "مضبوط حفظ کا راز نئے سبق کے ساتھ روزانہ سبقی اور منزل کی پابندی ہے۔",
            explanationAr = "يثبت القرآن في الصدر بالتعاهد المنتظم بين الجديد والقديم.",
            explanationHi = "क़ुरआन को मज़बूती से याद रखने के लिए नए पाठ के साथ नियमित दोहराव सबसे ज़रूरी है।"
        ),
        AcademyQuizQuestion(
            id = "q_s10_1",
            stageNumber = 10,
            category = "Stage 10 · Seerah",
            questionEn = "Who was the very first person to believe in Prophet Muhammad's ﷺ message?",
            questionUr = "رسول اللہ ﷺ کی نبوت پر سب سے پہلے کون ایمان لائے؟",
            questionAr = "من أول من آمن بدعوة النبي محمد ﷺ؟",
            questionHi = "पैगंबर मुहम्मद ﷺ के संदेश पर सबसे पहले किसने ईमान लाया?",
            optionsEn = listOf(
                "Khadijah bint Khuwaylid (رضي الله عنها)",
                "Heraclius of Rome",
                "Najashi initially in Year 8",
                "Abu Sufyan in Year 1"
            ),
            optionsUr = listOf(
                "ام المومنین حضرت خدیجہ بنت خویلدؓ",
                "شاہِ روم ہرقل",
                "نجاشی 8 ہجری میں",
                "ابو سفیان 1 نبوی میں"
            ),
            optionsAr = listOf(
                "أم المؤمنين خديجة بنت خويلد رضي الله عنها",
                "هرقل الروم",
                "النجاشي في السنة الثامنة",
                "أبو سفيان في السنة الأولى"
            ),
            optionsHi = listOf(
                "हज़रत ख़दीजा बिंत ख़ुवैलिद (रज़ि.)",
                "रोम के शासक हिरक़्ल",
                "8 हिजरी में नजाशी",
                "पहले साल में अबू सुफ़यान"
            ),
            correctIndex = 0,
            explanationEn = "Authentic Hadith in Bukhari/Muslim confirms Khadijah (رضي الله عنها) supported and believed immediately.",
            explanationUr = "صحیح بخاری کے مطابق ام المومنین حضرت خدیجہؓ سب سے پہلے ایمان لائیں اور تسلی دی۔",
            explanationAr = "تؤكد الروايات الصحيحة أن خديجة رضي الله عنها كانت أول من صدّق وآزر النبي ﷺ.",
            explanationHi = "सहीह बुख़ारी के अनुसार हज़रत ख़दीजा (रज़ि.) सबसे पहले ईमान लाईं और नबी ﷺ को सहारा दिया।"
        ),
        AcademyQuizQuestion(
            id = "q_s10_2",
            stageNumber = 10,
            category = "Stage 10 · Caliphs",
            questionEn = "During whose caliphates was the single unified Mushaf compilation and distribution carried out?",
            questionUr = "قرآن مجید کو یکجا صحیفے میں جمع کرنے اور پھر مستند نقول پھیلانے کا کارنامہ کن خلفاء کے دور میں ہوا؟",
            questionAr = "في عهد أي من الخلفاء تم جمع القرآن في مصحف واحد ثم استنساخ المصاحف وتوزيعها؟",
            questionHi = "किन ख़लीफ़ाओं के समय क़ुरआन को एक ग्रंथ में संकलित कर प्रामाणिक प्रतियां वितरित की गईं?",
            optionsEn = listOf(
                "Compiled under Abu Bakr (رضي الله عنه) and distributed uniformly under Uthman (رضي الله عنه)",
                "Only after 300 years",
                "Only in Europe in 1800",
                "Never written down"
            ),
            optionsUr = listOf(
                "حضرت ابوبکرؓ کے دور میں یکجا جمع ہوا اور حضرت عثمانؓ کے دور میں نقول پھیلائی گئیں",
                "300 سال بعد پہلی بار لکھا گیا",
                "1800ء میں پہلی بار لکھا گیا",
                "کبھی لکھا ہی نہیں گیا"
            ),
            optionsAr = listOf(
                "جُمع في عهد أبي بكر رضي الله عنه ونُسخ ووُزّع في عهد عثمان رضي الله عنه",
                "بعد ٣٠٠ سنة",
                "عام ١٨٠٠م",
                "لم يُكتب قط"
            ),
            optionsHi = listOf(
                "हज़रत अबू बक्र (रज़ि.) के समय संकलित हुआ और हज़रत उस्मान (रज़ि.) के समय समान प्रतियां बांटी गईं",
                "300 साल बाद पहली बार लिखा गया",
                "1800 ई. में लिखा गया",
                "कभी लिखा नहीं गया"
            ),
            correctIndex = 0,
            explanationEn = "Sahih al-Bukhari (4986-4987) documents Abu Bakr's compilation and Uthman's uniform distribution.",
            explanationUr = "صحیح بخاری (4986-4987) میں حضرت ابوبکرؓ کا جمعِ قرآن اور حضرت عثمانؓ کی اشاعتِ مصاحف تفصیل سے مذکور ہے۔",
            explanationAr = "وثّق صحيح البخاري (٤٩٨٦-٤٩٨٧) جمع القرآن في عهد أبي بكر ثم استنساخه في عهد عثمان رضي الله عنهما.",
            explanationHi = "सहीह बुख़ारी (4986-4987) में अबू बक्र (रज़ि.) द्वारा संकलन और उस्मान (रज़ि.) द्वारा प्रतियों के वितरण का प्रमाण है।"
        )
    )
}
