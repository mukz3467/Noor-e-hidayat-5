package com.example.data.hajj

internal object HajjDaysStepsData {

    val steps: List<GuideStep> = listOf(
        GuideStep(
            id = "hajj_1_day8_mina",
            mode = PilgrimageMode.HAJJ,
            orderIndex = 1,
            stageBadge = LocalizedText("8 Dhul-Hijjah · Tarwiyah", "8 ذوالحجہ · یوم الترویہ", "8 ذو الحجة · التروية", "8 ज़िल-हिज्जा · तरविया"),
            title = LocalizedText("Ihram for Hajj & Staying in Mina", "حج کا احرام اور منیٰ میں قیام", "الإحرام بالحج والمبيت بمنى", "हज का इहराम और मिना में ठहराव"),
            rulingLevel = RulingLevel.SUNNAH_RECOMMENDED,
            summary = LocalizedText(
                en = "Tamattu' pilgrims re-enter Ihram for Hajj from their Makkah accommodation; everyone proceeds to Mina before Dhuhr and prays 5 shortened prayers.",
                ur = "حجِ تمتع والے مکہ میں اپنی رہائش سے حج کا احرام باندھ کر منیٰ روانہ ہوں اور ظہر، عصر، مغرب، عشاء و فجر قصر (بغیر جمع کیے) پڑھیں۔",
                ar = "يُحرم المتمتع بالحج من مكانه، ويخرج الحجاج إلى منى فيصلون فيها الظهر والعصر والمغرب والعشاء والفجر قصراً بلا جمع.",
                hi = "तमत्तु करने वाले मक्का में अपने निवास से हज का इहराम बाँधें और सभी हाजी मिना जाकर 5 नमाज़ें क़स्र (बिना मिलाए) पढ़ें।"
            ),
            instructions = LocalizedText(
                en = "• Tamattu': Perform Ghusl, wear Ihram, make intention 'Labbayk Allahumma Hajjan' in the morning and recite Talbiyah.\n• Qiran / Ifrad: Already in Ihram from Miqat.\n• Proceed to Mina and pray Dhuhr (2), Asr (2), Maghrib (3), Isha (2), and Fajr of 9th Dhul-Hijjah on time—shortened (Qasr) without combining (Sahih Muslim 1218).",
                ur = "• تمتع والے صبح غسل کر کے احرام پہنیں اور «لبیک اللهم حجاً» کہہ کر تلبیہ پڑھتے ہوئے منیٰ جائیں۔\n• قران و افراد والے اپنے پہلے ہی احرام میں رہیں گے۔\n• منیٰ میں ظہر، عصر اور عشاء دو دو رکعت (قصر) اور مغرب و فجر اپنے اپنے وقت پر ادا کریں۔",
                ar = "• المتمتع يغتسل ويحرم صباحاً قائلاً «لبيك اللهم حجاً»، والمفرد والقارن على إحرامهما.\n• تُصلى بمنى الصلوات الخمس قصراً في أوقاتها (مسلم 1218).",
                hi = "• तमत्तु वाले सुबह ग़ुस्ल कर 'लब्बैक अल्लाहुम्मा हज्जन' की नीयत करें।\n• क़िरान व इफ़राद वाले पहले से ही इहराम में रहेंगे।\n• मिना में प्रत्येक नमाज़ अपने समय पर क़स्र पढ़ी जाएगी (मुस्लिम 1218)।"
            ),
            hajjFormNote = LocalizedText(
                en = "Only Tamattu' pilgrims enter a new Ihram today; Qiran and Ifrad pilgrims remain in their original Ihram.",
                ur = "آج نیا احرام صرف حجِ تمتع والوں کو باندھنا ہوتا ہے؛ قران اور افراد والے پہلے سے احرام میں ہوتے ہیں۔",
                ar = "يُحرم اليوم المتمتع فقط، أما القارن والمفرد فهما باقيان على إحرامهما.",
                hi = "आज नया इहराम केवल तमत्तु वालों को बाँधना है; क़िरान व इफ़राद वाले पहले से इहराम में हैं।"
            ),
            references = "Sahih al-Bukhari 1653; Sahih Muslim 1218"
        ),
        GuideStep(
            id = "hajj_2_day9_arafat",
            mode = PilgrimageMode.HAJJ,
            orderIndex = 2,
            stageBadge = LocalizedText("9 Dhul-Hijjah · Arafat", "9 ذوالحجہ · یومِ عرفہ", "9 ذو الحجة · عرفة", "9 ज़िल-हिज्जा · अरफ़ात"),
            title = LocalizedText("Wuquf at Arafat (The Supreme Pillar of Hajj)", "وقوفِ عرفات (حج کا رکنِ اعظم)", "الوقوف بعرفة (ركن الحج الأعظم)", "अरफ़ात में ठहराव (हज का सर्वोच्च स्तंभ)"),
            rulingLevel = RulingLevel.RUKN_PILLAR,
            summary = LocalizedText(
                en = "Proceed to Arafat after sunrise, pray Dhuhr & Asr combined and shortened at Dhuhr time, and devote the afternoon to Dua until sunset.",
                ur = "طلوعِ آفتاب کے بعد عرفات جائیں، ظہر کے وقت ظہر و عصر جمع و قصر کر کے پڑھیں اور غروبِ آفتاب تک دعا و استغفار میں مشغول رہیں۔",
                ar = "التوجه إلى عرفة بعد الشروق، وصلاة الظهر والعصر جمعاً وقصراً في وقت الظهر، ثم التفرغ للدعاء حتى الغروب.",
                hi = "सूर्योदय के बाद अरफ़ात जाएँ, ज़ुहर के समय ज़ुहर व अस्र मिलाकर क़स्र पढ़ें और सूर्यास्त तक दुआ में मग्न रहें।"
            ),
            instructions = LocalizedText(
                en = "• Verify you are strictly INSIDE the marked boundaries of Arafat (do not stay outside like Masjid Namirah's western courtyard unless inside Arafat).\n• Listen to the Arafat Khutbah and offer Dhuhr (2) + Asr (2) combined with one Adhan and two Iqamahs (Muslim 1218).\n• Stand/sit facing Qiblah with humility, Talbiyah, repentance, and the best Arafah Dua: 'La ilaha illAllahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa Huwa 'ala kulli shay'in qadir' (Tirmidhi 3585).\n• Do NOT leave Arafat before actual sunset (maghrib).",
                ur = "• یقینی بنائیں کہ آپ عرفات کی مقررہ حدود کے اندر موجود ہیں (مسجد نمرہ کا اگلا حصہ حدودِ عرفات سے باہر ہے)۔\n• ظہر و عصر ایک اذان اور دو اقامتوں کے ساتھ دو دو رکعت جمع تقدیم پڑھیں۔\n• غروبِ آفتاب تک قبلہ رخ ہو کر تلبیہ، توبہ اور «لا إله إلا الله وحده لا شريك له...» (ترمذی 3585) کثرت سے پڑھیں۔ غروب سے پہلے ہرگز نہ نکلیں۔",
                ar = "• التأكد من التواجد داخل حدود عرفة الشرعية.\n• جمع الظهر والعصر تقديماً وقصراً بأذان وإقامتين (مسلم 1218).\n• الإكثار من الذكر والدعاء مستقبل القبلة حتى تغرب الشمس (الترمذي 3585) ولا يُدفع قبل الغروب.",
                hi = "• सुनिश्चित करें कि आप अरफ़ात की सीमा के भीतर हैं।\n• ज़ुहर व अस्र एक अज़ान और दो इक़ामत से मिलाकर क़स्र पढ़ें (मुस्लिम 1218)।\n• सूर्यास्त तक क़िब्ला की ओर मुँह कर 'ला इलाहा इल्लल्लाहु वहदहू ला शरीका लह...' (तिर्मिज़ी 3585) और दिल से दुआ करें; सूर्यास्त से पहले न निकलें।"
            ),
            references = "Sahih Muslim 1218; Jami' at-Tirmidhi 889 (Al-Hajju Arafah), 3585"
        ),
        GuideStep(
            id = "hajj_3_night_muzdalifah",
            mode = PilgrimageMode.HAJJ,
            orderIndex = 3,
            stageBadge = LocalizedText("Night 9/10 · Muzdalifah", "شب 10 ذوالحجہ · مزدلفہ", "ليلة النحر · مزدلفة", "रात 9/10 · मुज़दलिफ़ा"),
            title = LocalizedText("Muzdalifah Overnight, Combined Maghrib/Isha & Fajr", "مزدلفہ میں رات، مغرب و عشاء جمع اور فجر", "المبيت بمزدلفة وجمع المغرب والعشاء", "मुज़दलिफ़ा में रात्रि विश्राम व फ़ज्र"),
            rulingLevel = RulingLevel.WAJIB_NECESSARY,
            summary = LocalizedText(
                en = "Leave Arafat calmly after sunset without praying Maghrib there; pray Maghrib & Isha combined upon reaching Muzdalifah, sleep, and pray Fajr early.",
                ur = "غروبِ آفتاب کے بعد سکون سے مزدلفہ پہنچیں، وہاں مغرب اور عشاء قصر و جمع کر کے پڑھیں، رات گزاریں اور اول وقت فجر پڑھ کر مشعر الحرام پر دعا کریں۔",
                ar = "الإفاضة إلى مزدلفة بعد الغروب، وصلاة المغرب والعشاء جمعاً وقصراً عند الوصول، والمبيت وصلاة الفجر مبكراً.",
                hi = "सूर्यास्त के बाद शांति से मुज़दलिफ़ा पहुँचकर मग़रिब व ईशा मिलाकर क़स्र पढ़ें, रात बिताएँ और सवेरे फ़ज्र पढ़कर दुआ करें।"
            ),
            instructions = LocalizedText(
                en = "• Pray Maghrib (3) and Isha (2) together with one Adhan and two Iqamahs upon arrival in Muzdalifah (Bukhari 1672).\n• Rest/sleep until Fajr (no night Sunnah prayers noted in Prophet's night at Muzdalifah).\n• Pray Fajr at early dawn, stand at al-Mash'ar al-Haram facing Qiblah making Dua until just before sunrise (Quran 2:198).\n• Collect 7 small chickpea-sized pebbles for today's Jamarat al-Aqabah (or all pebbles from Muzdalifah/Mina—any clean spot is valid).",
                ur = "• مزدلفہ پہنچ کر ایک اذان و دو اقامت سے مغرب (3) اور عشاء (2) جمع کر کے پڑھیں (بخاری 1672)۔\n• فجر اول وقت پڑھیں اور طلوعِ آفتاب سے کچھ پہلے تک مشعر الحرام پر دعا کریں (قرآن 2:198)۔\n• چنے کے دانے کے برابر 7 کنکریاں چن لیں (منیٰ یا مزدلفہ کہیں سے بھی صاف کنکریاں لی جا سکتی ہیں)۔",
                ar = "• يُصلي المغرب ثلاثاً والعشاء ركعتين بأذان وإقامتين عند الوصول (البخاري 1672).\n• يصلي الفجر بغلس ثم يدعو عند المشعر الحرام حتى يسفر جداً (البقرة 198).\n• يلتقط سبع حصيات لرمي جمرة العقبة (ويجوز من مزدلفة أو منى).",
                hi = "• पहुँचकर मग़रिब (3) और ईशा (2) मिलाकर पढ़ें (बुखारी 1672)।\n• सुबह सवेरे फ़ज्र पढ़कर मशअर अल-हरम पर सूर्योदय से थोड़ा पहले तक दुआ करें (क़ुरआन 2:198)।\n• जमरह अल-अक़बा के लिए चने के दाने के बराबर 7 कंकड़ियाँ चुन लें।"
            ),
            references = "Quran 2:198; Sahih al-Bukhari 1672, 1676; Sahih Muslim 1218",
            madhhabNotes = listOf(
                MadhhabDifferenceNote(
                    topic = LocalizedText("Duration of Muzdalifah Stay & Concession for Weak/Women", "مزدلفہ میں قیام کی مقدار اور ضعیف/خواتین کی رخصت", "مقدار المكث بمزدلفة ورخصة الضعفة والنساء", "मुज़दलिफ़ा में ठहरने की अवधि व कमज़ोर यात्रियों को छूट"),
                    hanafi = LocalizedText("Passing through Muzdalifah at night is Sunnah; being present between dawn (Fajr) and sunrise is Wajib (exempted for valid excuse/weakness/crowding).", "رات کا قیام سنت ہے؛ فجر سے طلوعِ آفتاب کے درمیان وقوفِ مزدلفہ واجب ہے (بیمار، خواتین و کمزور معذور ہیں)۔", "المبيت ليلاً سنة، والوقوف بعد الفجر وقبل الشروق واجب يسقط بالعذر.", "रात में ठहरना सुन्नत; फ़ज्र के बाद सूर्योदय से पहले रुकना वाजिब है (बीमार, महिला व वृद्ध को छूट है)।"),
                    shafii = LocalizedText("Being present for any portion of the second half of the night (after midnight) fulfills the Wajib; remaining until Fajr is Sunnah.", "آدھی رات کے بعد کچھ دیر موجود رہنے سے واجب ادا ہو جاتا ہے، فجر تک رہنا سنت ہے۔", "الواجب التواجد ولو لحظة في النصف الثاني من الليل، والأفضل البقاء للفجر.", "आधी रात के बाद थोड़ी देर रुकने से वाजिब पूरा हो जाता है; फ़ज्र तक रुकना सुन्नत है।"),
                    maliki = LocalizedText("Passing/stopping sufficiently to unload baggage and pray counts as Wajib; women/weak may leave after passing/resting.", "سواری اتار کر مغرب/عشاء اور تھوڑے آرام کی مقدار ٹھہرنا واجب ہے؛ کمزوروں کو رخصت ہے۔", "الواجب النزول بقدر حط الرحال والصلاة، والأفضل المبيت للفجر.", "सामान उतारने व नमाज़ पढ़ने भर रुकना वाजिब है; फ़ज्र तक रहना श्रेष्ठ है।"),
                    hanbali = LocalizedText("Staying past midnight is Wajib for able-bodied pilgrims; women and weak persons may depart after midnight (Bukhari 1676).", "قادر شخص کے لیے آدھی رات تک ٹھہرنا واجب ہے، خواتین و کمزور آدھی رات کے بعد جا سکتے ہیں (بخاری 1676)۔", "المبيت إلى بعد نصف الليل واجب، ويرخص للضعفة والنساء الدفع بعده (البخاري 1676).", "सक्षम व्यक्तियों के लिए आधी रात के बाद तक रुकना वाजिब है; महिलाओं व कमज़ोरों को आधी रात बाद जाने की अनुमति है (बुखारी 1676)।"),
                    reference = "Sahih al-Bukhari 1676 / Sahih Muslim 1293"
                )
            )
        ),
        GuideStep(
            id = "hajj_4_day10_nahr",
            mode = PilgrimageMode.HAJJ,
            orderIndex = 4,
            stageBadge = LocalizedText("10 Dhul-Hijjah · Yawm al-Nahr", "10 ذوالحجہ · یوم النحر", "10 ذو الحجة · يوم النحر", "10 ज़िल-हिज्जा · यौम अन-नहर"),
            title = LocalizedText("Eid Day: Rami Aqabah, Hady, Halq/Taqsir, Tawaf al-Ifadah & Sa'i", "یوم النحر: جمرۂ عقبہ، قربانی، حلق، طوافِ افاضہ اور سعی", "أعمال يوم النحر: الرمي والهدي والحلق والطواف والسعي", "ईद का दिन: रमी, क़ुर्बानी, हल्क़, तवाफ़-ए-इफ़ादा व सई"),
            rulingLevel = RulingLevel.RUKN_PILLAR,
            summary = LocalizedText(
                en = "Four key Day 10 acts: stone Jamarat al-Aqabah (7 pebbles), offer Hady sacrifice (Tamattu'/Qiran), shave/trim hair (First Tahallul), and perform Tawaf al-Ifadah (Rukn).",
                ur = "10 ذوالحجہ کے چار اعمال: 1) جمرۂ عقبہ کو 7 کنکریاں، 2) ہدی/قربانی (تمتع و قران پر لازم)، 3) حلق یا قصر (پہلا تحلل)، 4) طوافِ افاضہ (رکنِ حج) و سعی۔",
                ar = "أعمال يوم العيد أربعة: رمي جمرة العقبة بسبع حصيات، ذبح الهدي (للمتمتع والقارن)، الحلق أو التقصير، وطواف الإفاضة (ركن).",
                hi = "10 ज़िल-हिज्जा के चार कार्य: 1) बड़े जमरह को 7 कंकड़ियाँ, 2) हदी क़ुर्बानी (तमत्तु/क़िरान), 3) हल्क़/क़स्र (प्रथम तहल्लुल), 4) तवाफ़-ए-इफ़ादा (रुकन) व सई।"
            ),
            instructions = LocalizedText(
                en = "1. Rami Jamarat al-Aqabah: Stop Talbiyah upon throwing the first pebble; throw 7 pebbles individually saying 'Allahu Akbar' each time.\n2. Hady Sacrifice: Obligatory for Tamattu' and Qiran pilgrims (Quran 2:196); optional for Ifrad. Verify sacrifice completion via official Adahi/bank confirmation before shaving if following sequence.\n3. Halq or Taqsir: Yields First Tahallul—you may change into regular clothes and lift all Ihram prohibitions except marital intimacy.\n4. Tawaf al-Ifadah (7 circuits — Pillar/Rukn of Hajj, Quran 22:29) + Sa'i (obligatory for Tamattu', and for Qiran/Ifrad if not done earlier). Completes Full Tahallul.",
                ur = "1۔ جمرۂ عقبہ (بڑے شیطان) پر پہلی کنکری مارتے وقت تلبیہ بند کریں اور 7 کنکریاں ایک ایک کر کے اللہ اکبر کہتے ہوئے ماریں۔\n2۔ ہدی (قربانی): تمتع و قران پر واجب ہے (2:196)۔\n3۔ حلق یا قصر: اس سے تحللِ اصغر حاصل ہو کر سِلا لباس پہننا جائز ہو جاتا ہے۔\n4۔ طوافِ افاضہ (رکنِ حج، قرآن 22:29) اور سعی (تمتع پر لازم؛ قران/افراد پر اگر پہلے طواف قدوم کے ساتھ نہ کی ہو)۔ اس سے مکمل تحلل ہو جاتا ہے۔",
                ar = "1. يرمي جمرة العقبة الكبرى بسبع حصيات ويكبر مع كل حصاة ويقطع التلبية.\n2. ذبح الهدي (واجب على المتمتع والقارن: البقرة 196).\n3. الحلق أو التقصير (به يحصل التحلل الأول).\n4. طواف الإفاضة (ركن: الحج 29) والسعي (لمن لم يسع بعد طواف القدوم من قارن ومفرد، وعلى المتمتع مطلقاً). وبه يتم التحلل الأكبر.",
                hi = "1. जमरह अल-अक़बा पर 7 कंकड़ियाँ एक-एक कर 'अल्लाहु अकबर' कहते हुए मारें और तलबिया बंद करें।\n2. हदी क़ुर्बानी: तमत्तु व क़िरान पर अनिवार्य (2:196); इफ़राद पर वैकल्पिक।\n3. हल्क़/क़स्र: प्रथम तहल्लुल पूर्ण होकर सामान्य वस्त्र पहन सकते हैं।\n4. तवाफ़-ए-इफ़ादा (हज का रुकन, 22:29) व सई (तमत्तु के लिए अनिवार्य) — पूर्ण तहल्लुल।"
            ),
            hajjFormNote = LocalizedText(
                en = "• Tamattu': Hady is obligatory + full new Sa'i after Tawaf al-Ifadah.\n• Qiran: Hady is obligatory; Sa'i only needed if not already performed after Tawaf al-Qudum.\n• Ifrad: No Hady obligatory; Sa'i only needed if not performed after Tawaf al-Qudum.",
                ur = "• تمتع: قربانی اور طوافِ افاضہ کے بعد نئی سعی دونوں لازم ہیں۔\n• قران: قربانی لازم ہے؛ سعی تب ہوگی اگر طوافِ قدوم کے بعد نہ کی ہو۔\n• افراد: قربانی لازم نہیں؛ سعی تب ہوگی اگر طوافِ قدوم کے بعد نہ کی ہو۔",
                ar = "• المتمتع: يجب عليه الهدي وسعي الحج بعد طواف الإفاضة.\n• القارن: يجب الهدي، فإن كان سعى بعد طواف القدوم كفاه.\n• المفرد: لا هدي عليه، وإن سعى بعد القدوم كفاه.",
                hi = "• तमत्तु: हदी क़ुर्बानी व तवाफ़-ए-इफ़ादा के बाद नई सई दोनों अनिवार्य।\n• क़िरान: हदी अनिवार्य; सई केवल तब जब तवाफ़-ए-क़ुदूम के बाद न की हो।\n• इफ़राद: हदी अनिवार्य नहीं; सई केवल तब जब पहले न की हो।"
            ),
            references = "Quran 2:196, 22:29; Sahih al-Bukhari 83, 1736; Sahih Muslim 1218, 1306",
            madhhabNotes = listOf(
                MadhhabDifferenceNote(
                    topic = LocalizedText("Order (Tartib) of Day 10 Acts (Rami, Hady, Halq, Tawaf)", "10 ذوالحجہ کے اعمال میں ترتیب کا حکم", "حكم الترتيب بين أعمال يوم النحر", "10 ज़िल-हिज्जा के कार्यों का क्रम (तरतीब)"),
                    hanafi = LocalizedText("Maintaining sequence (Rami → Hady for Tamattu'/Qiran → Halq) is Wajib; shaving before Rami or Hady requires Dam atonement. (Tawaf al-Ifadah may follow later).", "رمی، پھر (تمتع/قران میں) قربانی، پھر حلق کی ترتیب واجب ہے؛ ترتیب کی خلاف ورزی پر دم لازم آتا ہے۔", "الترتيب بين الرمي والذبح (للمتمتع والقارن) والحلق واجب يوجب الدم عند المخالفة.", "रमी → हदी (तमत्तु/क़िरान) → हल्क़ का क्रम वाजिब है; क्रम भंग होने पर दम लाज़िम है।"),
                    shafii = LocalizedText("Order is Sunnah, not obligatory; changing sequence due to forgetfulness, ignorance, or need incurs no penalty (Bukhari 83).", "ترتیب مسنون ہے واجب نہیں؛ آگے پیچھے ہو جانے پر کوئی دم نہیں (بخاری 83)۔", "الترتيب سنة وليس بواجب، ولا فدية في التقديم والتأخير (البخاري 83).", "क्रम सुन्नत है, अनिवार्य नहीं; आगे-पीछे होने पर कोई दंड नहीं (बुखारी 83)।"),
                    maliki = LocalizedText("Order between Rami and Halq/Tawaf is Wajib in certain cases (halq before Rami triggers Dam).", "حلق کا رمی سے پہلے ہونا دم واجب کرتا ہے، باقی ترتیب میں تفصیل ہے (طواف و سعی کے بعد بھی رمی سے پہلے حلق نہ ہو)۔", "يجب تقديم الرمي على الحلق وإلا فعليه دم، أما تقديم الطواف على غيره فجائز مع الكراهة.", "हल्क़ का रमी से पहले होना दम लाज़िम करता है; तवाफ़ आदि में लचीलापन है।"),
                    hanbali = LocalizedText("Sequence (Rami → Sacrifice → Halq → Tawaf) is Sunnah based on the Prophet's words 'Do it, no harm' (Bukhari 83).", "ترتیب سنت ہے؛ آگے پیچھے ہو جانے پر حدیث «افعل ولا حرج» کے مطابق کوئی حرج یا دم نہیں۔", "الترتيب سنة في أعمال يوم النحر، ولا حرج في التقديم والتأخير (البخاري 83).", "क्रम सुन्नत है; 'अफ़अल व ला हरज' (बुखारी 83) के अनुसार आगे-पीछे करने में कोई हरज नहीं।"),
                    reference = "Sahih al-Bukhari 83 / Sahih Muslim 1306"
                )
            )
        ),
        GuideStep(
            id = "hajj_5_days11_13_tashreeq",
            mode = PilgrimageMode.HAJJ,
            orderIndex = 5,
            stageBadge = LocalizedText("11–13 Dhul-Hijjah · Tashreeq", "11 تا 13 ذوالحجہ · ایامِ تشریق", "11–13 ذو الحجة · التشريق", "11-13 ज़िल-हिज्जा · अय्याम-ए-तशरीक़"),
            title = LocalizedText("Mina Stay & Stoning All Three Jamarat (Ayyam al-Tashreeq)", "منیٰ میں قیام اور تینوں جمرات کی رمی", "المبيت بمنى ورمي الجمرات الثلاث", "मिना में विश्राम और तीनों जमरह पर रमी"),
            rulingLevel = RulingLevel.WAJIB_NECESSARY,
            summary = LocalizedText(
                en = "Spend the nights in Mina and stone all three Jamarat in order (Small → Middle → Large) after Dhuhr begins (Zawal) on 11th & 12th (and 13th if staying).",
                ur = "منیٰ میں راتیں گزاریں اور زوال کے بعد ترتیب سے چھوٹے، درمیانے اور بڑے جمرہ کو سات سات کنکریاں ماریں (11، 12 اور چاہیں تو 13 کو بھی)۔",
                ar = "المبيت بمنى ورمي الجمرات الثلاث (الصغرى فالوسطى فالكبرى) بعد الزوال أيام 11 و12 (و13 لمن تأخر).",
                hi = "मिना में रात बिताएँ और ज़वाल (ज़ुहर शुरू) के बाद क्रम से छोटे, मध्यम व बड़े जमरह को 7-7 कंकड़ियाँ मारें (11 व 12 और चाहें तो 13 को भी)।"
            ),
            instructions = LocalizedText(
                en = "• Timing: Begins after solar zenith (Zawal / Dhuhr start) each day.\n• Stone Jamarat al-Sughra (7 pebbles), step aside facing Qiblah and make long Dua; stone Jamarat al-Wusta (7 pebbles), step aside facing Qiblah and make long Dua; stone Jamarat al-Aqabah (7 pebbles) and move on without pausing for Dua (Sahih al-Bukhari 1753).\n• Leaving Early (Nafar al-Awwal): You may depart Mina on 12th Dhul-Hijjah before sunset (Quran 2:203). If sunset catches you still in Mina, stay the night of 13th and stone again after Zawal.",
                ur = "• وقت: 11، 12 اور 13 ذوالحجہ کو رمی کا وقت زوالِ آفتاب کے بعد شروع ہوتا ہے۔\n• پہلے جمرۂ صغریٰ (7 کنکریاں) مار کر ایک طرف قبلہ رخ طویل دعا کریں، پھر جمرۂ وسطیٰ (7) مار کر قبلہ رخ دعا کریں، پھر جمرۂ عقبہ (7) مار کر بغیر ٹھہرے واپس آئیں (بخاری 1753)۔\n• 12 ذوالحجہ کو غروبِ آفتاب سے پہلے منیٰ سے نکلنے کی اجازت ہے (قرآن 2:203)؛ اگر غروب منیٰ میں ہو جائے تو 13 کی رمی بھی کریں۔",
                ar = "• الوقت يبدأ بعد زوال الشمس.\n• يرمي الصغرى بسبع ويقف مستقبلاً القبلة يدعو طويلاً، ثم الوسطى بسبع ويدعو، ثم العقبة بسبع ولا يقف عندها (البخاري 1753).\n• يجوز التعجل بالخروج قبل غروب يوم 12 (البقرة 203)، ومن أدركه الغروب بمنى بات ليلة 13 ورمى.",
                hi = "• समय: ज़वाल (दोपहर ढलने) के बाद शुरू होता है।\n• पहले छोटे जमरह (7) पर रमी कर किनारे खड़े होकर क़िब्ला की ओर लंबी दुआ करें; फिर मध्यम (7) पर रमी कर दुआ करें; फिर बड़े (7) पर रमी कर बिना रुके आगे बढ़ जाएँ (बुखारी 1753)।\n• 12 ज़िल-हिज्जा को सूर्यास्त से पहले मिना छोड़ सकते हैं (2:203); वरना 13 को भी रमी करें।"
            ),
            references = "Quran 2:203; Sahih al-Bukhari 1753; Sahih Muslim 1296",
            madhhabNotes = listOf(
                MadhhabDifferenceNote(
                    topic = LocalizedText("Night Stay in Mina (Mabit) During Ayyam al-Tashreeq", "ایامِ تشریق میں منیٰ کی راتوں کے قیام کا حکم", "حكم المبيت بمنى ليالي التشريق", "तशरीक़ की रातों में मिना में ठहरना"),
                    hanafi = LocalizedText("Staying overnight in Mina on Tashreeq nights is Sunnah Mu'akkadah (leaving outside Mina is discouraged but does not require Dam).", "منیٰ میں رات گزارنا سنتِ مؤکدہ ہے، باہر گزارنے سے دم لازم نہیں آتا۔", "المبيت بمنى ليالي التشريق سنة مؤكدة لا يجب بتركه دم.", "मिना में रात बिताना सुन्नत-ए-मुअक्कदा है; छूटने पर दम वाजिब नहीं।"),
                    shafii = LocalizedText("Spending most of the night in Mina on nights 11 & 12 (and 13 if staying) is Wajib; omission without excuse incurs Dam.", "رات کا اکثر حصہ منیٰ میں گزارنا واجب ہے، بلا عذر چھوڑنے پر دم ہے۔", "المبيت معظم الليل بمنى واجب يجبر بدم عند تركه بلا عذر.", "रात का अधिकांश भाग मिना में बिताना वाजिब है; बिना कारण छूटने पर दम।"),
                    maliki = LocalizedText("Spending most of the night in Mina is Wajib unless excused.", "بیشتر رات منیٰ میں رہنا واجب ہے۔", "المبيت بمنى معظم الليل واجب وتركه بلا عذر يوجب الدم.", "अधिकांश रात मिना में बिताना वाजिब है।"),
                    hanbali = LocalizedText("Spending more than half the night in Mina is Wajib (exempted for shepherds/water-carriers/medical duties per Bukhari 1634).", "نصف رات سے زیادہ منیٰ میں قیام واجب ہے (خدمتِ حجاج یا عذر والوں کو رخصت ہے، بخاری 1634)۔", "المبيت أكثر من نصف الليل واجب إلا لأصحاب الأعذار (البخاري 1634).", "आधी रात से अधिक मिना में ठहरना वाजिब है (अस्वस्थ व सेवाकर्मियों को छूट, बुखारी 1634)।"),
                    reference = "Sahih al-Bukhari 1634, 1745"
                )
            )
        ),
        GuideStep(
            id = "hajj_6_tawaf_wada",
            mode = PilgrimageMode.HAJJ,
            orderIndex = 6,
            stageBadge = LocalizedText("Final Act · Farewell", "آخری عمل · طوافِ وداع", "الختام · طواف الوداع", "अंतिम चरण · तवाफ़-ए-विदा"),
            title = LocalizedText("Tawaf al-Wada (Farewell Tawaf Before Departing Makkah)", "طوافِ وداع (مکہ سے رخصتی سے قبل آخری طواف)", "طواف الوداع عند مغادرة مكة", "तवाफ़-ए-विदा (मक्का से विदाई तवाफ़)"),
            rulingLevel = RulingLevel.WAJIB_NECESSARY,
            summary = LocalizedText(
                en = "Perform 7 final circuits around the Ka'bah and 2 Raka'at right before departing Makkah—menstruating and post-partum women are explicitly exempted.",
                ur = "مکہ مکرمہ سے روانہ ہونے سے پہلے آخری طواف اور دو رکعت نماز ادا کریں۔ حائضہ اور نفاس والی خواتین کو اس سے مکمل رخصت ہے۔",
                ar = "الطواف سبعة أشواط وصلاة ركعتين ليكون آخر عهد الحاج بالبيت؛ وتعفى منه الحائض والنفساء.",
                hi = "मक्का से विदा होने से पहले अंतिम तवाफ़ व 2 रकात नमाज़ पढ़ें; माहवारी वाली महिलाओं को इससे पूरी छूट है।"
            ),
            instructions = LocalizedText(
                en = "• Ibn Abbas (RA) narrated: 'The people were commanded that their last act in Makkah be Tawaf of the House, except that concession was granted to menstruating women' (Bukhari 1755 / Muslim 1328).\n• Perform 7 circuits normally (no Idtiba' or Raml, and no Sa'i afterwards), pray 2 Raka'at, drink Zamzam, and depart Makkah.",
                ur = "• ابن عباسؓ فرماتے ہیں: لوگوں کو حکم دیا گیا کہ ان کا آخری عمل بیت اللہ کا طواف ہو، البتہ حائضہ عورتوں کو رخصت دے دی گئی (بخاری 1755، مسلم 1328)۔\n• اس میں اضطباع، رمل اور سعی نہیں ہوتی۔ طواف و دو رکعت کے بعد رخصت ہو جائیں۔",
                ar = "• عن ابن عباس: «أُمر الناس أن يكون آخر عهدهم بالبيت إلا أنه خُفّف عن الحائض» (البخاري 1755).\n• لا اضطباع فيه ولا رمل ولا سعي بعده.",
                hi = "• इब्न अब्बास (रज़ि.) से वर्णित है कि अंतिम कार्य बैतुल्लाह का तवाफ़ हो, परंतु माहवारी वाली महिलाओं को छूट दी गई (बुखारी 1755)।\n• इसमें इज़तिबा, रमल और बाद में सई नहीं होती।"
            ),
            menVsWomenNote = LocalizedText(
                en = "Menstruating (Ha'id) and post-natal (Nufasa) women do NOT perform Tawaf al-Wada and owe zero penalty upon departing.",
                ur = "حیض و نفاس کی حالت میں خواتین طوافِ وداع کیے بغیر روانہ ہو سکتی ہیں اور ان پر کوئی دم نہیں۔",
                ar = "تسقط عن الحائض والنفساء بلا دم باتفاق المذاهب الأربعة.",
                hi = "माहवारी (हैज़ व निफ़ास) में महिलाएँ बिना तवाफ़-ए-विदा जा सकती हैं और उन पर कोई दम नहीं।"
            ),
            references = "Sahih al-Bukhari 1755; Sahih Muslim 1327, 1328",
            madhhabNotes = listOf(
                MadhhabDifferenceNote(
                    topic = LocalizedText("Ruling on Tawaf al-Wada (Wajib vs Sunnah)", "طوافِ وداع واجب ہے یا سنت؟", "حكم طواف الوداع بين المذاهب", "तवाफ़-ए-विदा वाजिब है या सुन्नत?"),
                    hanafi = LocalizedText("Wajib for all non-Makkans (resident outside Miqat); omission incurs Dam.", "آفاقی (میقات سے باہر کے) حجاج پر واجب ہے، ترک پر دم لازم ہے۔", "واجب على الآفاقي وتجب الفدية بتركه.", "मीक़ात के बाहर के यात्रियों पर वाजिब है; छूटने पर दम।"),
                    shafii = LocalizedText("Wajib according to the relied-upon Shafi'i position; leaving without it requires Dam.", "معتمد شافعی قول کے مطابق واجب ہے، ترک پر دم آتا ہے۔", "واجب على الصحيح ويجب الدم بتركه.", "प्रमुख शाफ़ेई मत में वाजिब है; छूटने पर दम।"),
                    maliki = LocalizedText("Sunnah Mu'akkadah (highly recommended); omitting it incurs no Dam sacrifice.", "سنتِ مؤکدہ ہے، چھوٹنے پر دم واجب نہیں۔", "سنة مؤكدة وليس بواجب فلا دم بتركه.", "सुन्नत-ए-मुअक्कदा है; छूटने पर कोई दम नहीं।"),
                    hanbali = LocalizedText("Wajib for departing pilgrims (excluding Makkah residents and menstruating women).", "مکہ سے واپس جانے والوں پر واجب ہے، ترک کرنے پر دم ہے۔", "واجب على غير المكي والحائض والنفساء، وتركه يوجب الدم.", "मक्का से लौटने वालों पर वाजिब है (माहवारी में छूट); छूटने पर दम।"),
                    reference = "Sahih al-Bukhari 1755; Sahih Muslim 1328"
                )
            )
        )
    )
}
