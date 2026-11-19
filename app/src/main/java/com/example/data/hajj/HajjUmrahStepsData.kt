package com.example.data.hajj

internal object HajjUmrahStepsData {

    val steps: List<GuideStep> = listOf(
        GuideStep(
            id = "umrah_1_ihram",
            mode = PilgrimageMode.UMRAH,
            orderIndex = 1,
            stageBadge = LocalizedText("Step 1 · Miqat", "مرحلہ 1 · میقات", "الخطوة 1 · الميقات", "चरण 1 · मीक़ात"),
            title = LocalizedText("Purification, Ihram & Intention at Miqat", "میقات پر طہارت، احرام اور عمرہ کی نیت", "الغسل والإحرام والنية من الميقات", "मीक़ात पर स्नान, इहराम और नीयत"),
            rulingLevel = RulingLevel.RUKN_PILLAR,
            summary = LocalizedText(
                en = "Enter the state of Ihram before crossing your designated Miqat and begin reciting the Talbiyah.",
                ur = "اپنے مقررہ میقات سے گزرنے سے پہلے عمرہ کے احرام کی نیت کریں اور تلبیہ شروع کریں۔",
                ar = "الدخول في الإحرام بالنية قبل مجاوزة الميقات والبدء بالتلبية.",
                hi = "अपने मीक़ात को पार करने से पहले इहराम की नीयत करें और तलबिया पढ़ें।"
            ),
            instructions = LocalizedText(
                en = "1. Perform Ghusl (full bath), trim nails if needed, and men apply non-alcoholic perfume to the body (not the white cloths).\n2. Men wear two clean unstitched white cloths (Izar and Rida); women wear normal modest sewn attire.\n3. After an obligatory prayer or two Sunnah Raka'at, make silent intention: 'Labbayk Allahumma Umratan' and recite Talbiyah continuously until reaching the Ka'bah.",
                ur = "1۔ غسل کریں، ناخن و غیر ضروری بال صاف کریں؛ مرد صرف جسم پر خوشبو لگائیں، احرام کی چادروں پر نہیں۔\n2۔ مرد دو اَن سِلی چادریں (تہبند اور چادر) پہنیں، خواتین اپنا معمول کا ساتر لباس پہنیں۔\n3۔ فرض نماز یا دو رکعت نماز کے بعد دل میں عمرہ کی نیت کر کے «لبیک اللهم عمرة» کہیں اور تلبیہ پڑھتے رہیں۔",
                ar = "1. يُسن الاغتسال والتطيب في البدن قبل النية.\n2. يلبس الرجل الإزار والرداء، وتلبس المرأة ثيابها الساترة بلا نقاب ولا قفازين.\n3. ينوي العمرة قائلاً: «لبيك اللهم عمرة» ويكثر من التلبية إلى رؤية البيت.",
                hi = "1. ग़ुस्ल करें, नाखून आदि साफ़ करें और पुरुष शरीर पर खुशबू लगाएँ (कपड़ों पर नहीं)।\n2. पुरुष दो बिना सिले कपड़े पहनें; महिलाएँ सामान्य शालीन कपड़े (बिना नक़ाब व दस्ताने) पहनें।\n3. नमाज़ के बाद नीयत करें: 'लब्बैक अल्लाहुम्मा उमरतन' और काबा पहुँचने तक तलबिया पढ़ें।"
            ),
            menVsWomenNote = LocalizedText(
                en = "Men recite Talbiyah audibly; women recite it softly so only they or close companions hear (Sunan at-Tirmidhi; consensus).",
                ur = "مرد بلند آواز سے تلبیہ پڑھیں جبکہ خواتین آہستہ آواز میں پڑھیں۔",
                ar = "يرفع الرجال أصواتهم بالتلبية وتُسرّ النساء بها.",
                hi = "पुरुष ऊँचे स्वर में और महिलाएँ धीमे स्वर में तलबिया पढ़ें।"
            ),
            references = "Sahih al-Bukhari 1524, 1539, 1549; Sahih Muslim 1184"
        ),
        GuideStep(
            id = "umrah_2_masjid_entry",
            mode = PilgrimageMode.UMRAH,
            orderIndex = 2,
            stageBadge = LocalizedText("Step 2 · Masjid al-Haram", "مرحلہ 2 · مسجدِ حرام", "الخطوة 2 · المسجد الحرام", "चरण 2 · मस्जिद अल-हरम"),
            title = LocalizedText("Entering Masjid al-Haram & Stopping Talbiyah", "مسجدِ حرام میں داخلہ اور تلبیہ موقوف کرنا", "دخول المسجد الحرام وقطع التلبية", "मस्जिद अल-हरम में प्रवेश व तलबिया समाप्त करना"),
            rulingLevel = RulingLevel.SUNNAH_RECOMMENDED,
            summary = LocalizedText(
                en = "Enter Masjid al-Haram calmly with the right foot reciting the Prophetic Masjid Dua, and stop Talbiyah when beginning Tawaf.",
                ur = "مسجدِ حرام میں دایاں پاؤں پہلے رکھ کر مسنون دعا پڑھیں اور طواف شروع کرتے وقت تلبیہ بند کر دیں۔",
                ar = "دخول المسجد بالرجل اليمنى والدعاء المأثور، وقطع التلبية عند الشروع في الطواف.",
                hi = "दाहिने पैर से मस्जिद में प्रवेश करें, मस्जिद की दुआ पढ़ें और तवाफ़ शुरू करते समय तलबिया बंद कर दें।"
            ),
            instructions = LocalizedText(
                en = "Step in with your right foot saying: 'Bismillah was-salatu was-salamu 'ala Rasulillah, Allahumma-ftah li abwaba rahmatik' (Sahih Muslim 713). Pause in humility upon seeing the Ka'bah and raise sincere supplications. Men uncover the right shoulder (Idtiba') immediately before starting Tawaf.",
                ur = "دایاں قدم اندر رکھتے ہوئے «اللهم افتح لي أبواب رحمتك» پڑھیں۔ کعبہ پر پہلی نظر پڑتے ہی دل سے دعا مانگیں۔ طواف شروع کرنے سے پہلے مرد اپنی اوپری چادر دائیں بغل کے نیچے سے نکال کر بائیں کندھے پر ڈال لیں (اضطباع)۔",
                ar = "قل عند الدخول: «اللهم افتح لي أبواب رحمتك» (مسلم 713). يضطبع الرجال قبل الطواف (كشف الكتف الأيمن) ويُقطع التلبية عند استلام الحجر الأسود.",
                hi = "दायाँ पैर रखते हुए 'अल्लाहुम्मफ़-तह ली अबवाबा रहमतिका' (मुस्लिम 713) पढ़ें। तवाफ़ से पहले पुरुष दाहिना कंधा खुला करें (इज़तिबा)।"
            ),
            references = "Sahih Muslim 713, 1218"
        ),
        GuideStep(
            id = "umrah_3_tawaf",
            mode = PilgrimageMode.UMRAH,
            orderIndex = 3,
            stageBadge = LocalizedText("Step 3 · 7 Circuits", "مرحلہ 3 · 7 چکر", "الخطوة 3 · 7 أشواط", "चरण 3 · 7 चक्कर"),
            title = LocalizedText("Tawaf Around the Ka'bah (7 Counter-Clockwise Rounds)", "بیت اللہ کا طواف (سات چکر)", "الطواف حول الكعبة سبعة أشواط", "काबा का तवाफ़ (7 चक्कर)"),
            rulingLevel = RulingLevel.RUKN_PILLAR,
            summary = LocalizedText(
                en = "Perform 7 complete circuits keeping the Ka'bah on your left, beginning and ending in line with Hajar al-Aswad (Black Stone).",
                ur = "حجرِ اسود کے سامنے سے نیت کر کے کعبہ کو بائیں جانب رکھتے ہوئے سات چکر مکمل کریں۔",
                ar = "الطواف سبعة أشواط جاعلاً الكعبة عن يساره، يبدأ من الحجر الأسود وينتهي إليه.",
                hi = "काबा को बाईं ओर रखते हुए हजर-ए-असवद से शुरू कर सात पूर्ण चक्कर लगाएँ।"
            ),
            instructions = LocalizedText(
                en = "• Start aligned with the Black Stone (Hajar al-Aswad) facing it: touch/kiss it only if completely uncrowded without pushing anyone; otherwise face it, raise your right hand toward it and say 'Bismillahi Allahu Akbar' (Bukhari 1613).\n• Men only: practice Raml (brisk short steps) during the first 3 rounds and walk normally in the final 4.\n• Pass around the outside of Hateem (Hijr Isma'il), never cutting through it.\n• Touch the Yemeni Corner (Rukn al-Yamani) lightly if accessible without crowding (do not kiss or point if unreachable).\n• Between Rukn al-Yamani and Hajar al-Aswad, recite Quran 2:201 ('Rabbana atina fid-dunya hasanatan...').",
                ur = "• حجرِ اسود کے سامنے کھڑے ہو کر بِلا تکلیف بوسہ/استلام ممکن نہ ہو تو دور سے ہاتھ اٹھا کر «بسم اللہ اللہ اکبر» کہہ کر اشارہ کریں (بخاری 1613)۔\n• صرف مرد پہلے 3 چکروں میں رمل (قدرے تیز قدم) کریں، باقی 4 عام رفتار سے۔\n• حطیم (حجرِ اسماعیل) کے باہر سے گھومیں، اندر سے ہرگز نہ گزریں۔\n• رکنِ یمانی اور حجرِ اسود کے درمیان «ربنا آتنا فی الدنیا حسنۃ...» (2:201) پڑھیں۔",
                ar = "• يبدأ من الحجر الأسود مشيراً بيده قائلاً: «بسم الله والله أكبر» دون إيذاء أو مزاحمة (البخاري 1613).\n• للرجال: الرمل في الأشواط الثلاثة الأولى والمشي في الأربعة الباقية.\n• يجب الطواف من وراء الحِجْر (حِجْر إسماعيل).\n• يُقال بين الركن اليماني والحجر الأسود: «ربنا آتنا في الدنيا حسنة...» (أبو داود 1892).",
                hi = "• हजर-ए-असवद के सामने खड़े होकर भीड़ हो तो दूर से इशारा कर 'बिस्मिल्लाहि अल्लाहु अकबर' कहें (बुखारी 1613)।\n• केवल पुरुष पहले 3 चक्कर तेज़ चाल (रमल) से चलें।\n• हतीम (हिज्र इस्माईल) के बाहर से चक्कर लगाएँ।\n• रुक्ने-यमानी व हजर-ए-असवद के बीच 'रब्बना आतिना फ़िद-दुनिया हसनतन...' (क़ुरआन 2:201) पढ़ें।"
            ),
            menVsWomenNote = LocalizedText(
                en = "Idtiba' (exposing right shoulder) and Raml (brisk pace in first 3 rounds) apply strictly to men—never to women.",
                ur = "اضطباع اور رمل صرف مردوں کے لیے ہیں، خواتین عام باوقار چال سے طواف کریں۔",
                ar = "الاضطباع والرمل خاصان بالرجال دون النساء بإجماع العلماء.",
                hi = "इज़तिबा और रमल केवल पुरुषों के लिए हैं, महिलाएँ सामान्य शालीन चाल से चलें।"
            ),
            references = "Sahih al-Bukhari 1612, 1613, 1651; Sahih Muslim 1218; Sunan Abi Dawud 1892",
            madhhabNotes = listOf(
                MadhhabDifferenceNote(
                    topic = LocalizedText("Requirement of Wudu (Ablution) During Tawaf", "طواف میں وضو کی شرط کا فقہی حکم", "حكم اشتراط الطهارة (الوضوء) في الطواف", "तवाफ़ में वुज़ू (पाकी) का नियम"),
                    hanafi = LocalizedText("Wudu is Wajib (necessary), not a condition for validity: Tawaf without Wudu is valid but requires Dam (expiatory sacrifice) unless repeated in Wudu.", "وضو واجب ہے، شرطِ صحت نہیں؛ بے وضو طواف ہو جائے گا مگر دم (قربانی) واجب ہوگا اگر دوبارہ باوضو نہ کیا جائے۔", "الوضوء واجب وليس شرط صحة؛ فيصح الطواف ويلزمه دم ما لم يعده متوضئاً.", "वुज़ू वाजिब है (शर्त नहीं); बिना वुज़ू तवाफ़ पूरा होने पर दम (क़ुर्बानी) लाज़िम होता है जब तक दोहराया न जाए।"),
                    shafii = LocalizedText("Ritual purity (Wudu) from minor & major impurity is an essential condition (Shart); breaking Wudu invalidates the remaining circuits until purified.", "طہارتِ صغریٰ و کبریٰ طواف کی صحت کے لیے شرط ہے؛ وضو ٹوٹنے پر وضو کر کے طواف مکمل کرنا لازم ہے۔", "الطهارة من الحدثين شرط لصحة الطواف، وإذا انتقض وضوؤه توضأ واستأنف أو بنى على القولين.", "पाकी (वुज़ू) तवाफ़ की वैधता के लिए अनिवार्य शर्त है; वुज़ू टूटने पर फिर से वुज़ू करना आवश्यक है।"),
                    maliki = LocalizedText("Wudu is a mandatory condition (Shart) for Tawaf's validity.", "طواف کے صحیح ہونے کے لیے باوضو ہونا شرط ہے۔", "الطهارة شرط لصحة الطواف كالصلاة.", "तवाफ़ की वैधता के लिए वुज़ू अनिवार्य शर्त है।"),
                    hanbali = LocalizedText("Wudu is a strict condition (Shart) for validity; Tawaf is invalid if performed without Wudu.", "طواف کی صحت کے لیے وضو شرط ہے، بے وضو طواف درست نہیں۔", "تشترط الطهارة لصحة الطواف، ولا يصح بدون وضوء.", "तवाफ़ सही होने के लिए वुज़ू अनिवार्य शर्त है।"),
                    reference = "Sahih al-Bukhari 1641 / Sunan at-Tirmidhi 960"
                )
            )
        ),
        GuideStep(
            id = "umrah_4_maqam_zamzam",
            mode = PilgrimageMode.UMRAH,
            orderIndex = 4,
            stageBadge = LocalizedText("Step 4 · Prayer & Zamzam", "مرحلہ 4 · مقامِ ابراہیم و زمزم", "الخطوة 4 · المقام وزمزم", "चरण 4 · मक़ाम-ए-इब्राहीम व ज़मज़म"),
            title = LocalizedText("Two Raka'at Behind Maqam Ibrahim & Drinking Zamzam", "مقامِ ابراہیم کے پیچھے دو رکعت نماز اور آبِ زمزم", "ركعتا الطواف خلف المقام وشرب ماء زمزم", "मक़ाम-ए-इब्राहीम के पीछे 2 रकात नमाज़ व ज़मज़म"),
            rulingLevel = RulingLevel.WAJIB_NECESSARY,
            summary = LocalizedText(
                en = "Cover both shoulders (men), offer 2 Raka'at behind Maqam Ibrahim (or anywhere in the Masjid if crowded), then drink Zamzam water.",
                ur = "مرد دونوں کندھے ڈھانپ لیں، مقامِ ابراہیم کے پیچھے (یا رش میں مسجد میں کہیں بھی) دو رکعت نماز ادا کریں اور زمزم پئیں۔",
                ar = "تغطية الكتفين ثم صلاة ركعتين خلف المقام (أو في أي موضع بالمسجد) ثم الشرب من زمزم.",
                hi = "पुरुष दोनों कंधे ढँक लें, मक़ाम-ए-इब्राहीम के पीछे (या मस्जिद में कहीं भी) 2 रकात नमाज़ पढ़ें और ज़मज़म पिएँ।"
            ),
            instructions = LocalizedText(
                en = "• Recite 'Wattakhidhu min Maqami Ibrahima musalla' (Quran 2:125).\n• Pray 2 short Raka'at reciting Surah Al-Kafirun (109) in the 1st and Surah Al-Ikhlas (112) in the 2nd (Sahih Muslim 1218). If crowded behind Maqam Ibrahim, pray further back anywhere in Masjid al-Haram without obstructing Tawaf.\n• Drink Zamzam water and make heartfelt Dua (Sahih Muslim 2473). Touch/point toward Black Stone if clear before heading to Safa.",
                ur = "• «واتخذوا من مقام إبراهيم مصلى» (2:125) پڑھیں۔ پہلی رکعت میں سورہ الکافرون اور دوسری میں سورہ الاخلاص پڑھیں (مسلم 1218)۔ رش ہو تو مسجدِ حرام میں کہیں بھی پڑھ سکتے ہیں۔\n• آبِ زمزم پی کر خوب دعا مانگیں۔\n•Note: دو رکعت طوافِ حنفی فقہ میں واجب اور دیگر ائمہ کے نزدیک سنتِ مؤکدہ ہے۔",
                ar = "• يتلو ﴿وَاتَّخِذُوا مِن مَّقَامِ إِبْرَاهِيمَ مُصَلًّى﴾ (البقرة 125)، ويصلي ركعتين بالكافرون والإخلاص (مسلم 1218).\n• ثم يشرب من ماء زمزم ويدعو الله (مسلم 2473).\n• تنبيه فقهي: ركعتا الطواف واجبة عند الحنفية وسنة مؤكدة عند الشافعية والحنابلة (وواجبة عند المالكية في الطواف المفروض).",
                hi = "• 'वत्तख़िज़ू मिम मक़ामि इब्राहीमा मुसल्ला' (2:125) पढ़ें। पहली रकात में अल-काफ़िरून व दूसरी में अल-इख़्लास पढ़ें (मुस्लिम 1218)।\n• भीड़ में मस्जिद में कहीं भी नमाज़ पढ़ सकते हैं, फिर ज़मज़म पिएँ व दुआ करें। (फ़िक़्ह: हनफ़ी मत में वाजिब, शाफ़ेई/हंबली में सुन्नत-ए-मुअक्कदा)।"
            ),
            references = "Quran 2:125; Sahih Muslim 1218, 2473"
        ),
        GuideStep(
            id = "umrah_5_sai",
            mode = PilgrimageMode.UMRAH,
            orderIndex = 5,
            stageBadge = LocalizedText("Step 5 · 7 Laps", "مرحلہ 5 · سعی (7 چکر)", "الخطوة 5 · السعي", "चरण 5 · सई (7 फेरे)"),
            title = LocalizedText("Sa'i Between Mount Safa and Mount Marwah (7 Laps)", "صفا اور مروہ کے درمیان سعی (سات چکر)", "السعي بين الصفا والمروة سبعة أشواط", "सफ़ा और मरवाह के बीच सई (7 फेरे)"),
            rulingLevel = RulingLevel.RUKN_PILLAR,
            summary = LocalizedText(
                en = "Begin at Mount Safa and complete 7 laps alternating Safa → Marwah (1,3,5,7) and Marwah → Safa (2,4,6).",
                ur = "کوہِ صفا سے شروع کر کے مروہ پر ساتواں چکر مکمل کریں (صفا تا مروہ ایک چکر شمار ہوتا ہے)۔",
                ar = "البدء بالصفا وإتمام سبعة أشواط (من الصفا إلى المروة شوط ومن المروة إلى الصفا شوط).",
                hi = "सफ़ा पहाड़ी से आरंभ कर मरवाह पर सातवाँ फेरा समाप्त करें (सफ़ा से मरवाह 1 फेरा गिना जाता है)।"
            ),
            instructions = LocalizedText(
                en = "• Approaching Safa, recite: 'Innas-Safa wal-Marwata min sha'a'irillah' (Quran 2:158) and 'Nabda'u bima bada'Allahu bih' (Muslim 1218).\n• Stand on Safa facing the Ka'bah, raise your hands, say 'Allahu Akbar' three times and recite: 'La ilaha illAllahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa Huwa 'ala kulli shay'in qadir; La ilaha illAllahu wahdah, anjaza wa'dah, wa nasara 'abdah, wa hazamal-ahzaba wahdah'—then make personal Dua, repeating this 3 times (Muslim 1218).\n• Walk toward Marwah. Between the green fluorescent lights, men only jog briskly; women walk normally.\n• Repeat the facing-Qiblah remembrance and Dua at Marwah and each subsequent stop, finishing Lap 7 at Marwah.",
                ur = "• صفا پر آتے وقت «إن الصفا والمروة من شعائر الله» (2:158) اور «نبدأ بما بدأ الله به» پڑھیں۔\n• صفا پر قبلہ رخ ہو کر ہاتھ اٹھائیں، تین بار اللہ اکبر اور لا إله إلا الله وحده لا شريك له... پڑھ کر خوب دعا کریں (مسلم 1218)۔\n• مروہ کی طرف چلیں؛ سبز لائٹوں کے درمیان صرف مرد ہلکی دوڑ لگائیں۔\n• مروہ پر بھی قبلہ رخ ہو کر یہی ذکر و دعا کریں۔ ساتواں چکر مروہ پر ختم ہوگا۔",
                ar = "• عند الصفا يقرأ ﴿إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ﴾ ويقول: «نبدأ بما بدأ الله به» (مسلم 1218).\n• يستقبل القبلة ويكبر ثلاثاً ويهلل بالدعاء المأثور ويدعو.\n• يُسرع الرجال فقط بين العلمين الأخضرين؛ وتُنهى الأشواط السبعة على المروة.",
                hi = "• सफ़ा के पास 'इन्नस-सफ़ा वल-मरवता मिन शआइरिल्लाह' (2:158) व 'नबदउ बिमा बदअल्लाहु बिह' (मुस्लिम 1218) पढ़ें।\n• क़िब्ला की ओर मुँह कर तक्बीर, तहलील पढ़ें व दुआ करें। हरे संकेतों के बीच केवल पुरुष तेज़ चलें।\n• मरवाह पर सातवाँ फेरा समाप्त होगा।"
            ),
            menVsWomenNote = LocalizedText(
                en = "Brisk jogging between the green lights is exclusively for men; women walk normally throughout all 7 laps. Wudu is recommended for Sa'i, not strictly required (a menstruating woman may perform Sa'i after Tawaf).",
                ur = "سبز نشانوں کے درمیان دوڑنا صرف مردوں کے لیے ہے، خواتین عام چال سے چلیں۔ سعی کے لیے وضو مستحب ہے، شرط نہیں۔",
                ar = "الرمل بين العلمين للرجال فقط. ولا يُشترط الوضوء لصحة السعي عند جمهور العلماء، لكنه مستحب.",
                hi = "हरे संकेतों के बीच तेज़ दौड़ केवल पुरुषों के लिए है। सई के लिए वुज़ू मुस्तहब है, अनिवार्य शर्त नहीं।"
            ),
            references = "Quran 2:158; Sahih Muslim 1218",
            madhhabNotes = listOf(
                MadhhabDifferenceNote(
                    topic = LocalizedText("Legal Status of Sa'i (Pillar vs Wajib)", "سعی کا فقہی درجہ (رکن بمقابلہ واجب)", "حكم السعي: أهو ركن أم واجب؟", "सई की फ़िक़्ही स्थिति (रुकन बनाम वाजिब)"),
                    hanafi = LocalizedText("Wajib (Mandatory): Umrah/Hajj remains valid without it, though Dam (sacrifice) is due if omitted intentionally.", "واجب ہے (رکن نہیں): ترک کرنے سے عمرہ باطل نہیں ہوتا البتہ دم لازم آتا ہے۔", "واجب يجبر بالدم عند تركه، وليس ركناً.", "सई वाजिब है (रुकन नहीं); छूटने पर दम (क़ुर्बानी) देना होता है।"),
                    shafii = LocalizedText("Rukn (Essential Pillar): Umrah or Hajj is incomplete until Sa'i is performed.", "رکن (فرض) ہے: سعی کیے بغیر عمرہ یا حج مکمل نہیں ہوتا۔", "ركن لا يتم النسك ولا يتحلل إلا به.", "रुकन (अनिवार्य स्तंभ): सई के बिना उमराह या हज पूरा नहीं होता।"),
                    maliki = LocalizedText("Rukn (Essential Pillar): Must be performed for pilgrimage validity.", "رکن ہے: اس کے بغیر عمرہ و حج نامکمل ہے۔", "ركن من أركان الحج والعمرة.", "रुकन (अनिवार्य स्तंभ): इसके बिना हज/उमराह पूरा नहीं होता।"),
                    hanbali = LocalizedText("Rukn (Essential Pillar): Essential to completing Umrah and Hajj.", "رکن ہے: اس کے بغیر تحلل مکمل نہیں ہوتا۔", "ركن لا يُجبر بدم ولا بد من الإتيان به.", "रुकन (अनिवार्य स्तंभ): इसकी पूर्ति आवश्यक है।"),
                    reference = "Quran 2:158; Sahih Muslim 1218"
                )
            )
        ),
        GuideStep(
            id = "umrah_6_halq",
            mode = PilgrimageMode.UMRAH,
            orderIndex = 6,
            stageBadge = LocalizedText("Step 6 · Completion", "مرحلہ 6 · حلق یا قصر", "الخطوة 6 · الحلق أو التقصير", "चरण 6 · हल्क़ या क़स्र"),
            title = LocalizedText("Halq (Shaving) or Taqsir (Trimming) — Umrah Complete", "حلق یا قصر — تکمیلِ عمرہ", "الحلق أو التقصير — إتمام العمرة", "हल्क़ (मुंडन) या क़स्र — उमराह पूर्ण"),
            rulingLevel = RulingLevel.WAJIB_NECESSARY,
            summary = LocalizedText(
                en = "Men shave the entire head (preferred) or trim evenly; women trim a fingertip's length (~1–2 cm) from hair ends. All Ihram restrictions are lifted.",
                ur = "مرد پورا سر منڈوائیں (افضل) یا پورے سر کے بال چھوٹے کرائیں؛ خواتین صرف انگلی کے پورے جتنے بال کاٹیں۔ عمرہ مکمل ہو گیا۔",
                ar = "يحلق الرجل رأسه كله (أفضل) أو يقصره، وتقصر المرأة قدر أنملة من أطراف شعرها؛ وبذلك تتم العمرة.",
                hi = "पुरुष पूरा सिर मुंडवाएँ (श्रेष्ठ) या सभी बाल छोटे कराएँ; महिलाएँ बालों के सिरे से उँगली के एक पोर जितना काटें। उमराह पूर्ण!"
            ),
            instructions = LocalizedText(
                en = "• Men: Shaving (Halq) carries triple Prophetic supplication for forgiveness (Bukhari 1727), unless you are performing Tamattu' and Day 8 Dhul-Hijjah is very close—in which case trimming (Taqsir) saves shaving for 10th Dhul-Hijjah.\n• Women: Gather braids/hair tips and cut roughly a fingertip's length (approx. 1.5–2 cm) from the ends only; shaving is forbidden for women (Tirmidhi 914).\n• Your Umrah is now complete and normal clothing/activities resume.",
                ur = "• مرد: پورے سر کا حلق کرانا تین بار دعائے مغفرت کا باعث ہے (بخاری 1727)؛ تاہم اگر آپ حج تمتع کر رہے ہیں اور 8 ذوالحجہ قریب ہے تو قصر بہتر ہے تاکہ 10 ذوالحجہ کو حلق کرا سکیں۔\n• خواتین: بالوں کے کناروں سے انگلی کے ایک پورے کے برابر کٹوائیں؛ خواتین کے لیے سر منڈوانا منع ہے۔",
                ar = "• الرجل: الحلق أفضل لدعاء النبي ﷺ للمحلقين ثلاثاً (البخاري 1727)، وللمتمتع إذا قرب اليوم الثامن الأفضل التقصير ليحلق يوم النحر.\n• المرأة: تجمع شعرها وتأخذ من أطرافه قدر أنملة فقط ويحرم عليها الحلق (الترمذي 914).",
                hi = "• पुरुष: सिर मुंडवाना (हल्क़) श्रेष्ठ है क्योंकि पैगंबर ﷺ ने उनके लिए 3 बार दुआ की (बुखारी 1727); यदि तमत्तु कर रहे हैं और 8 ज़िल-हिज्जा करीब है तो क़स्र बेहतर है।\n• महिलाएँ: केवल बालों के सिरों से उँगली के पोर (लगभग 1.5-2 सेमी) काटें; मुंडन वर्जित है।"
            ),
            references = "Quran 48:27; Sahih al-Bukhari 1727; Jami' at-Tirmidhi 914",
            madhhabNotes = listOf(
                MadhhabDifferenceNote(
                    topic = LocalizedText("Minimum Hair Required for Taqsir / Halq", "قصر میں کم از کم بال کٹوانے کی مقدار", "القدر المجزئ في الحلق والتقصير", "बाल काटने की न्यूनतम मात्रा"),
                    hanafi = LocalizedText("Minimum of one-fourth (1/4) of the head must be shaved or trimmed by at least a fingertip length (more than half/all is Sunnah).", "کم از کم سر کے چوتھائی حصے کے بال انگلی کے پورے جتنے کٹوانا واجب ہے، پورے سر کا سنت ہے۔", "الواجب ربع الرأس، والكمال استيعاب الرأس.", "सिर के कम से कम चौथाई भाग से पोर बराबर बाल कटवाना वाजिब, पूरा सिर सुन्नत है।"),
                    shafii = LocalizedText("Even cutting three hairs fulfills the minimal pillar, though covering the entire head is Sunnah.", "کم از کم تین بال کاٹنا فرض ادا کرنے کو کافی ہے، پورے سر پر قصر/حلق سنت ہے۔", "أقله ثلاث شعرات، والأفضل تعميم الرأس.", "कम से कम 3 बाल काटना भी वैध है, पूरा सिर मुंडवाना/काटना सुन्नत है।"),
                    maliki = LocalizedText("Shaving or trimming must cover the entire head (all parts).", "پورے سر کے بال منڈوانا یا کٹوانا ضروری ہے۔", "يجب تعميم الرأس بالحلق أو التقصير.", "पूरे सिर के बालों को मुंडवाना या काटना आवश्यक है।"),
                    hanbali = LocalizedText("Must trim/shave across the whole head evenly (Quran 48:27).", "پورے سر سے برابر بال کٹوانا یا منڈوانا لازم ہے۔", "يجب تعميم الرأس كله في الحلق والتقصير.", "पूरे सिर से समान रूप से बाल काटना या मुंडवाना आवश्यक है।"),
                    reference = "Quran 48:27; Fiqh comparative manuals"
                )
            )
        )
    )
}
