package com.example.data.shifa

/**
 * Verified Quranic Shifa, Ruqyah Shar'iyyah, and Quranic Guidance passages.
 *
 * Original Arabic Uthmani script, translations (Urdu, Hindi Devanagari, English),
 * and offline Tafseer are resolved strictly from Noor-e-Hidayat's verified local
 * Quran & QuranEnc corpora — zero Quranic verses are ever fabricated.
 */
object ShifaQuranAndRuqyahData {

    val items: List<ShifaRuqyahItem> = listOf(
        // =========================================================================
        // A. QURANIC SHIFA — شفائے قرآن
        // =========================================================================
        ShifaRuqyahItem(
            id = "shifa_q_17_82",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Quran as Healing & Mercy",
                ur = "قرآن بطور شفا و رحمت",
                ar = "القرآن شفاء ورحمة",
                hi = "क़ुरआन: शिफ़ा और रहमत"
            ),
            title = LocalizedText4(
                en = "Surah Al-Isra (17:82) — Healing and Mercy for Believers",
                ur = "سورۃ الإسراء (17:82) — قرآن ایمان والوں کے لیے شفا اور رحمت",
                ar = "سورة الإسراء (١٧: ٨٢) — وننزل من القرآن ما هو شفاء ورحمة",
                hi = "सूरह अल-इस्रा (17:82) — ईमान वालों के लिए शिफ़ा और रहमत"
            ),
            surahNumber = 17,
            ayahStart = 82,
            ayahEnd = 82,
            islamicContext = LocalizedText4(
                en = "Classical commentators (Ibn Kathir, Al-Mukhtasar) explain that the Noble Quran removes spiritual diseases of the heart—doubt, hypocrisy, ignorance, and despair—while Allah may also grant relief by His permission when believers recite it with faith. It is not declared a guaranteed cure for a named physical illness without specific evidence.",
                ur = "مفسرینِ کرام (ابن کثیر، المختصر) بیان کرتے ہیں کہ قرآن مجید دلوں کے روحانی امراض—شک، نفاق، جہالت اور مایوسی—کے لیے شفا اور ایمان والوں کے لیے رحمت ہے، اور اللہ کے حکم سے تلاوتِ قرآن باعثِ خیر و سکون بنتی ہے۔ اسے بغیر خاص دلیل کسی مخصوص جسمانی مرض کا حتمی علاج قرار نہیں دیا جاتا۔",
                ar = "يبيّن المفسرون أن القرآن الكريم شفاء لأمراض القلوب من الشك والنفاق والجهل، ورحمة للمؤمنين، ويُنال به الشفاء والسكينة بإذن الله.",
                hi = "मुफ़स्सिरीन के अनुसार क़ुरआन दिलों की रूहानी बीमारियों—संदेह, पाखंड, अज्ञान और निराशा—से शिफ़ा और ईमान वालों के लिए रहमत है। बिना विशेष दलील इसे किसी एक शारीरिक रोग का निश्चित इलाज घोषित नहीं किया जाता।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Isra 17:82",
                bookAndNumber = "Quran 17:82",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/17/82"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Quran.com Portal", "Noble Quran", "Verified Corpus", "https://quran.com/")
            ),
            searchKeywords = listOf("shifa", "healing", "quran", "mercy", "rehmat", "roohani pareshani", "روحانی پریشانی", "شفا", "शिफ़ा", "रूहानी परेशानी"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "shifa_q_10_57",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Healing for What Is in the Breasts",
                ur = "سینوں کی بیماریوں کی شفا",
                ar = "شفاء لما في الصدور",
                hi = "सीनों (दिलों) की शिफ़ा"
            ),
            title = LocalizedText4(
                en = "Surah Yunus (10:57) — Admonition, Healing for Hearts & Guidance",
                ur = "سورۃ یونس (10:57) — نصیحت، دلوں کی شفا اور ہدایت",
                ar = "سورة يونس (١٠: ٥٧) — موعظة وشفاء لما في الصدور",
                hi = "सूरह यूनुस (10:57) — नसीहत, दिलों की शिफ़ा और मार्गदर्शन"
            ),
            surahNumber = 10,
            ayahStart = 57,
            ayahEnd = 57,
            islamicContext = LocalizedText4(
                en = "Addressed to all mankind, this verse describes the Quran as an admonition from the Lord, healing for inner spiritual ailments, restlessness, and doubts in the chest, and mercy for believers.",
                ur = "پوری انسانیت کو خطاب کرتے ہوئے یہ آیت قرآن کو رب کی طرف سے نصیحت، سینوں کے اندر موجود روحانی بے چینی و امراض کی شفا اور اہلِ ایمان کے لیے ہدایت و رحمت قرار دیتی ہے۔",
                ar = "خطاب للناس كافة بأن القرآن موعظة من الله، وشفاء لما في الصدور من أمراض القلوب والشبهات والغموم، وهدى ورحمة للمؤمنين.",
                hi = "समस्त मानवजाति को संबोधित यह आयत क़ुरआन को रब की ओर से नसीहत, सीनों की अंदरूनी बेचैनी व संदेहों के लिए शिफ़ा और ईमान वालों के लिए रहमत बताती है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Yunus 10:57",
                bookAndNumber = "Quran 10:57",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/10/57"
            ),
            searchKeywords = listOf("shifa", "dil ka gham", "healing", "peace", "roohani pareshani", "غم", "دل کی شفا", "दिल का सुकून"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "shifa_q_26_80",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Tawakkul: Allah Alone Heals",
                ur = "عقیدہ توحید: شفا دینے والا صرف اللہ ہے",
                ar = "التوكل: وإذا مرضت فهو يشفين",
                hi = "तवक्कुल: शिफ़ा केवल अल्लाह देता है"
            ),
            title = LocalizedText4(
                en = "Surah Ash-Shu'ara (26:80) — \"And When I Am Ill, It Is He Who Cures Me\"",
                ur = "سورۃ الشعراء (26:80) — جب میں بیمار ہوتا ہوں تو وہی مجھے شفا دیتا ہے",
                ar = "سورة الشعراء (٢٦: ٨٠) — وإذا مرضت فهو يشفين",
                hi = "सूरह अश-शुअरा (26:80) — \"और जब मैं बीमार होता हूँ, तो वही मुझे शिफ़ा देता है\""
            ),
            surahNumber = 26,
            ayahStart = 80,
            ayahEnd = 80,
            islamicContext = LocalizedText4(
                en = "Prophet Ibrahim (AS) attributed illness to human frailty out of reverence while attributing healing solely to Allah. Every lawful remedy or Ruqyah is merely a means; true Shifa comes strictly from Allah.",
                ur = "حضرت ابراہیم علیہ السلام نے ادباً بیماری کی نسبت اپنی طرف کی اور شفا کی نسبت خالصتاً اللہ تعالیٰ کی طرف فرمائی۔ ہر جائز سبب یا دم صرف وسیلہ ہے، حقیقی شفا صرف اللہ کے ہاتھ میں ہے۔",
                ar = "نسب نبي الله إبراهيم عليه السلام المرض إلى نفسه تأدباً مع الله، وأسند الشفاء إليه وحده سبحانه، فالأسباب وسائل والشافي هو الله وحده.",
                hi = "हज़रत इब्राहीम (अलैहि.) ने अदब के साथ बीमारी की ओर संकेत अपनी कमज़ोरी से किया और शिफ़ा को पूरी तरह अल्लाह के हाथ में बताया।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Ash-Shu'ara 26:80",
                bookAndNumber = "Quran 26:80",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/26/80"
            ),
            searchKeywords = listOf("shifa", "tawakkul", "illness", "bimar", "cure", "بیماری", "شفا", "तवक्कुल"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "shifa_q_41_44",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Guidance & Healing",
                ur = "ہدایت اور شفا",
                ar = "هدى وشفاء",
                hi = "मार्गदर्शन और शिफ़ा"
            ),
            title = LocalizedText4(
                en = "Surah Fussilat (41:44) — \"Say: It Is, for Those Who Believe, Guidance and Cure\"",
                ur = "سورۃ فصلت (41:44) — فرما دیجیے یہ ایمان والوں کے لیے ہدایت اور شفا ہے",
                ar = "سورة فصلت (٤١: ٤٤) — قل هو للذين آمنوا هدى وشفاء",
                hi = "सूरह फ़ुस्सिलात (41:44) — \"कह दीजिए: यह ईमान वालों के लिए मार्गदर्शन और शिफ़ा है\""
            ),
            surahNumber = 41,
            ayahStart = 44,
            ayahEnd = 44,
            islamicContext = LocalizedText4(
                en = "Revealed in Makkah affirming that faith opens the heart to receive the Quran's healing and guidance.",
                ur = "مکی سورت میں بیان فرمایا گیا کہ ایمان کے ساتھ قرآن سے رجوع کرنے والوں کے لیے یہ کتاب ہدایت اور شفا کا سرچشمہ ہے۔",
                ar = "آية مكية تؤكد أن الإيمان والتصديق بالقرآن يجعلانه نوراً وهدى وشفاءً لقلب المؤمن.",
                hi = "मक्की आयत जो बताती है कि सच्चे ईमान के साथ क़ुरआन की ओर लौटने वालों के लिए यह हिदायत और शिफ़ा है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Fussilat 41:44",
                bookAndNumber = "Quran 41:44",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/41/44"
            ),
            searchKeywords = listOf("shifa", "guidance", "quran", "hidayat", "شفا", "ہدایت", "शिफ़ा"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "shifa_q_13_28",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Tranquility of Heart",
                ur = "دل کا سکون",
                ar = "طمأنينة القلب",
                hi = "दिल का सुकून"
            ),
            title = LocalizedText4(
                en = "Surah Ar-Ra'd (13:28) — Hearts Find Rest in the Remembrance of Allah",
                ur = "سورۃ الرعد (13:28) — خبردار! اللہ کے ذکر ہی سے دلوں کو اطمینان نصیب ہوتا ہے",
                ar = "سورة الرعد (١٣: ٢٨) — ألا بذكر الله تطمئن القلوب",
                hi = "सूरह अर-राअद (13:28) — निश्चय ही अल्लाह के ज़िक्र से ही दिलों को सुकून मिलता है"
            ),
            surahNumber = 13,
            ayahStart = 28,
            ayahEnd = 28,
            islamicContext = LocalizedText4(
                en = "Establishes Zikrullah (remembrance of Allah) and recitation of the Quran as the divine source of deep peace in times of anxiety and restlessness.",
                ur = "گھبراہٹ، بے چینی اور خوف کے لمحات میں دل کے حقیقی اطمینان کا ذریعہ ذکرِ الٰہی ہے۔",
                ar = "تُبيّن الآية أن السكينة الحقيقية وطمأنينة النفس تتحققان بذكر الله تعالى وتلاوة كتابه.",
                hi = "घबराहट, बेचैनी और डर के समय में अल्लाह का ज़िक्र और क़ुरआन दिल के सच्चे सुकून का स्रोत हैं।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Ar-Ra'd 13:28",
                bookAndNumber = "Quran 13:28",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/13/28"
            ),
            searchKeywords = listOf("sukoon", "peace", "anxiety", "bechaini", "khauf", "dil ka gham", "سکون", "दिल का सुकून"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "shifa_q_2_153",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Patience & Prayer",
                ur = "صبر اور نماز سے مدد",
                ar = "الصبر والصلاة",
                hi = "सब्र और नमाज़ से सहायता"
            ),
            title = LocalizedText4(
                en = "Surah Al-Baqarah (2:153) — Seek Help Through Patience and Prayer",
                ur = "سورۃ البقرہ (2:153) — صبر اور نماز کے ذریعے اللہ سے مدد طلب کرو",
                ar = "سورة البقرة (٢: ١٥٣) — استعينوا بالصبر والصلاة",
                hi = "सूरह अल-बक़रा (2:153) — सब्र और नमाज़ के ज़रिए मदद माँगो"
            ),
            surahNumber = 2,
            ayahStart = 153,
            ayahEnd = 153,
            islamicContext = LocalizedText4(
                en = "Guidance during hardship, grief, or trial: combine steadfast patience (Sabr) with turning directly to Allah in Salah.",
                ur = "آزمائش، غم اور پریشانی میں مومن کے دو مضبوط سہارے صبر اور نماز ہیں، اور اللہ صبر کرنے والوں کے ساتھ ہے۔",
                ar = "إرشاد رباني عند الشدائد بالاستعانة بالصبر على البلاء واللجوء إلى الله في الصلاة.",
                hi = "कठिनाई और ग़म में मोमिन का सहारा सब्र (धैर्य) और नमाज़ है, और अल्लाह सब्र करने वालों के साथ है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Baqarah 2:153",
                bookAndNumber = "Quran 2:153",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/2"
            ),
            searchKeywords = listOf("sabr", "patience", "salah", "pareshani", "help", "madad", "صبر", "सब्र"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "shifa_q_39_53",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Hope & Mercy of Allah",
                ur = "اللہ کی رحمت سے امید",
                ar = "الرجاء ورحمة الله",
                hi = "अल्लाह की रहमत से उम्मीद"
            ),
            title = LocalizedText4(
                en = "Surah Az-Zumar (39:53) — Do Not Despair of the Mercy of Allah",
                ur = "سورۃ الزمر (39:53) — اللہ کی رحمت سے ہرگز مایوس نہ ہو",
                ar = "سورة الزمر (٣٩: ٥٣) — لا تقنطوا من رحمة الله",
                hi = "सूरह अज़-ज़ुमर (39:53) — अल्लाह की रहमत से निराश मत हो"
            ),
            surahNumber = 39,
            ayahStart = 53,
            ayahEnd = 53,
            islamicContext = LocalizedText4(
                en = "Consoles hearts weighed down by guilt, hopelessness, or sorrow by reminding believers that Allah's mercy and forgiveness encompass all shortcomings upon sincere repentance.",
                ur = "ندامت، مایوسی اور ٹوٹے ہوئے دلوں کو تسلی دینے والی عظیم آیت کہ سچی توبہ سے اللہ سب گناہ معاف فرمانے والا اور بے حد رحم فرمانے والا ہے۔",
                ar = "تبعث هذه الآية أعظم الأمل في القلوب المثقلة بالهم والذنب، بأن رحمة الله ومغفرته وسعت كل شيء لمن تاب.",
                hi = "टूटे और निराश दिलों को दिलासा देने वाली आयत कि सच्ची तौबा से अल्लाह अत्यंत क्षमाशील और दयावान है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Az-Zumar 39:53",
                bookAndNumber = "Quran 39:53",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/39/53"
            ),
            searchKeywords = listOf("rehmat", "mercy", "hope", "gham", "despair", "umeed", "رحمت", "उम्मीद"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "shifa_q_2_186",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Allah Is Near & Answers Dua",
                ur = "اللہ کی قربت اور قبولیتِ دعا",
                ar = "قرب الله واستجابة الدعاء",
                hi = "अल्लाह की निकटता और दुआ"
            ),
            title = LocalizedText4(
                en = "Surah Al-Baqarah (2:186) — \"Indeed I Am Near; I Respond to the Invocation\"",
                ur = "سورۃ البقرہ (2:186) — بے شک میں قریب ہوں، پکارنے والے کی دعا سنتا ہوں",
                ar = "سورة البقرة (٢: ١٨٦) — فإني قريب أجيب دعوة الداع",
                hi = "सूरह अल-बक़रा (2:186) — \"निश्चय ही मैं निकट हूँ; पुकारने वाले की दुआ क़ुबूल करता हूँ\""
            ),
            surahNumber = 2,
            ayahStart = 186,
            ayahEnd = 186,
            islamicContext = LocalizedText4(
                en = "Placed immediately among the verses of fasting, affirming direct access to Allah without any intermediary when calling upon Him in distress or need.",
                ur = "بندے اور رب کے درمیان کسی واسطے کے بغیر براہِ راست دعا و مناجات کی بشارت۔",
                ar = "تؤكد قرب الله من عبده وإجابة دعائه مباشرة دون واسطة متى دعاه بإخلاص.",
                hi = "बंदे और रब के बीच बिना किसी माध्यम के सीधे दुआ और पुकार सुनने की बशारत।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Baqarah 2:186",
                bookAndNumber = "Quran 2:186",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/2"
            ),
            searchKeywords = listOf("dua", "help", "madad", "near", "qareeb", "دعا", "दुआ"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "shifa_q_3_173",
            category = ShifaCategory.QURANIC_SHIFA,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Fear & Tawakkul",
                ur = "خوف میں توکل اور تسلی",
                ar = "التوكل عند الخوف",
                hi = "डर में तवक्कुल"
            ),
            title = LocalizedText4(
                en = "Surah Ali 'Imran (3:173) — \"Sufficient for Us Is Allah, Best Disposer of Affairs\"",
                ur = "سورۃ آل عمران (3:173) — ہمیں اللہ کافی ہے اور وہ بہترین کارساز ہے",
                ar = "سورة آل عمران (٣: ١٧٣) — حسبنا الله ونعم الوكيل",
                hi = "सूरह आल इमरान (3:173) — \"हमारे लिए अल्लाह काफ़ी है, और वह उत्तम कार्यसाधक है\""
            ),
            surahNumber = 3,
            ayahStart = 173,
            ayahEnd = 173,
            islamicContext = LocalizedText4(
                en = "In Sahih al-Bukhari (4563), Ibn Abbas narrated that this statement was spoken by Prophet Ibrahim (AS) when thrown into the fire and by Prophet Muhammad ﷺ when threatened by enemies.",
                ur = "صحیح بخاری (4563) میں حضرت ابن عباسؓ سے مروی ہے کہ یہ کلمات حضرت ابراہیمؑ نے آگ میں ڈالتے وقت اور نبی کریم ﷺ نے خطرات کے موقع پر ادا فرمائے۔",
                ar = "روى البخاري (٤٥٦٣) عن ابن عباس رضي الله عنهما أنها كلمة قالها إبراهيم عليه السلام حين أُلقي في النار، وقالها محمد صلى الله عليه وسلم عند الشدائد.",
                hi = "सहीह बुख़ारी (4563) में वर्णित है कि संकट और भय के समय यह तवक्कुल का महान वाक्य कहा गया।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Ali 'Imran 3:173",
                bookAndNumber = "Quran 3:173",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/3/173"
            ),
            searchKeywords = listOf("khauf", "fear", "tawakkul", "anxiety", "protection", "خوف", "توکل", "डर"),
            supportsQuranAudio = true
        ),

        // =========================================================================
        // B. RUQYAH SHAR'IYYAH — رقیہ شرعیہ
        // =========================================================================
        ShifaRuqyahItem(
            id = "ruqyah_q_fatihah",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Core Quranic Ruqyah",
                ur = "رقیہ کی بنیادی سورت",
                ar = "أعظم سورة للرقية",
                hi = "रुक़या की प्रमुख सूरह"
            ),
            title = LocalizedText4(
                en = "Surah Al-Fatihah (1:1–7) — Verified as Ruqyah by the Prophet ﷺ",
                ur = "سورۃ الفاتحہ (1:1–7) — حدیثِ نبوی ﷺ سے ثابت شدہ رقیہ",
                ar = "سورة الفاتحة (١: ١-٧) — الشافية والرقية الثابتة في الصحيحين",
                hi = "सूरह अल-फ़ातिहा (1:1–7) — सहीह बुख़ारी से प्रमाणित रुक़या"
            ),
            surahNumber = 1,
            ayahStart = 1,
            ayahEnd = 7,
            islamicContext = LocalizedText4(
                en = "Established in Sahih al-Bukhari 5736: when a Companion recited Surah Al-Fatihah over a tribal chief stung by a venomous creature, the Prophet ﷺ approved it and asked: 'How did you know it is a Ruqyah?'",
                ur = "صحیح بخاری (5736) میں ثابت ہے کہ ایک صحابیؓ نے ڈسے ہوئے شخص پر سورۃ الفاتحہ پڑھ کر دم کیا تو نبی کریم ﷺ نے فرمایا: 'تمہیں کیسے معلوم ہوا کہ یہ رقیہ (دم) ہے؟'",
                ar = "ثبت في صحيح البخاري (٥٧٣٦) إقرار النبي صلى الله عليه وسلم للصحابي الذي رقى اللديغ بفاتحة الكتاب بقوله: «وما يدريك أنها رقية؟».",
                hi = "सहीह बुख़ारी (5736) में प्रमाणित है कि एक सहाबी ने डंसे हुए व्यक्ति पर सूरह फ़ातिहा से दम किया तो नबी ﷺ ने इसकी पुष्टि फ़रमाई।"
            ),
            recitationMethod = LocalizedText4(
                en = "Recite completely in clear Arabic with presence of heart and reliance upon Allah; the Companion lightly blew/applied saliva (Bukhari 5736) where the sting occurred.",
                ur = "مکمل سورۃ الفاتحہ خالص توکلِ الٰہی اور توجہِ قلب کے ساتھ پڑھ کر ہلکا پھونک (نفث) کریں (صحیح بخاری 5736)۔",
                ar = "تُقرأ الفاتحة كاملة بيقين وتوكل على الله مع النفث الخفيف على الموضع أو في اليدين.",
                hi = "पूरी सूरह फ़ातिहा पूर्ण आस्था व तवक्कुल के साथ पढ़ें और हल्की फूँक (नफ़्स) करें (सहीह बुख़ारी 5736)।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5736 (Al-Fatihah as Ruqyah)",
                bookAndNumber = "Sahih al-Bukhari 5736",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5736"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Quran.com — Surah Al-Fatihah", "Quran 1:1–7", "Noble Quran", "https://quran.com/1")
            ),
            searchKeywords = listOf("ruqyah", "fatiha", "fatihah", "shifa", "roohani pareshani", "فاتحہ", "رقیہ", "फ़ातिहा"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "ruqyah_q_kursi",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Protection Against Devils",
                ur = "شیطان سے حفاظت",
                ar = "آية الحفظ الكبرى",
                hi = "सुरक्षा की महान आयत"
            ),
            title = LocalizedText4(
                en = "Ayat-ul-Kursi — Surah Al-Baqarah (2:255)",
                ur = "آیۃ الکرسی — سورۃ البقرہ (2:255)",
                ar = "آية الكرسي — سورة البقرة (٢: ٢٥٥)",
                hi = "आयत-उल-कुर्सी — सूरह अल-बक़रा (2:255)"
            ),
            surahNumber = 2,
            ayahStart = 255,
            ayahEnd = 255,
            islamicContext = LocalizedText4(
                en = "Affirmed in Sahih al-Bukhari 2311: whoever recites Ayat-ul-Kursi before sleeping has a guardian from Allah remain over them and no devil approaches them until morning.",
                ur = "صحیح بخاری (2311) میں نبی ﷺ نے تصدیق فرمائی کہ سونے سے پہلے آیۃ الکرسی پڑھنے والے پر اللہ کی جانب سے ایک محافظ مقرر رہتا ہے اور صبح تک شیطان قریب نہیں آتا۔",
                ar = "أقرّ النبي صلى الله عليه وسلم في صحيح البخاري (٢٣١١) أن من قرأها عند النوم لا يزال عليه من الله حافظ ولا يقربه شيطان حتى يصبح.",
                hi = "सहीह बुख़ारी (2311) में नबी ﷺ ने पुष्टि फ़रमाई कि सोने से पहले इसे पढ़ने वाले पर सुबह तक अल्लाह का रक्षक रहता है।"
            ),
            recitationMethod = LocalizedText4(
                en = "Recite after each obligatory Salah (verified in An-Nasa'i al-Kubra) and once before sleep (Bukhari 2311).",
                ur = "سونے سے پہلے ایک بار (بخاری 2311) اور فرض نمازوں کے بعد پڑھنا مسنون ہے۔",
                ar = "تُقرأ مرة عند النوم (صحيح البخاري ٢٣١١) ودبر الصلوات المكتوبات.",
                hi = "सोने से पहले एक बार (बुख़ारी 2311) और फ़र्ज़ नमाज़ों के बाद पढ़ें।"
            ),
            establishedCountNote = LocalizedText4(
                en = "Established frequency: 1 time before sleeping (Sahih al-Bukhari 2311).",
                ur = "ثابت شدہ تعداد: سونے سے پہلے ایک مرتبہ (صحیح بخاری 2311)۔",
                ar = "العدد الثابت: مرة واحدة عند النوم (صحيح البخاري ٢٣١١).",
                hi = "प्रमाणित संख्या: सोने से पहले 1 बार (सहीह बुख़ारी 2311)।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Baqarah 2:255",
                bookAndNumber = "Quran 2:255",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/2"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih al-Bukhari 2311", "Sahih al-Bukhari 2311", "Sahih", "https://sunnah.com/bukhari:2311")
            ),
            searchKeywords = listOf("ayat ul kursi", "sleep", "neend ki pareshani", "protection", "ruqyah", "آیۃ الکرسی", "नींद"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "ruqyah_q_baqarah_end",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Last Two Verses of Al-Baqarah",
                ur = "سورۃ البقرہ کی آخری دو آیات",
                ar = "خواتيم سورة البقرة",
                hi = "सूरह बक़रा की आख़िरी दो आयतें"
            ),
            title = LocalizedText4(
                en = "Surah Al-Baqarah (2:285–286) — Sufficiency in the Night",
                ur = "سورۃ البقرہ (2:285–286) — رات کو کافی ہو جانے والی دو آیات",
                ar = "سورة البقرة (٢: ٢٨٥-٢٨٦) — من قرأهما في ليلة كفتاه",
                hi = "सूरह अल-बक़रा (2:285–286) — रात को पर्याप्त होने वाली दो आयतें"
            ),
            surahNumber = 2,
            ayahStart = 285,
            ayahEnd = 286,
            islamicContext = LocalizedText4(
                en = "In Sahih al-Bukhari (5008) and Sahih Muslim (807), the Prophet ﷺ said: 'Whoever recites the last two verses of Surah Al-Baqarah at night, they will suffice him'—protecting from harm and uplifting faith. Also in Sahih Muslim (780), Shaytan flees from a home where Surah Al-Baqarah is recited.",
                ur = "صحیح بخاری (5008) و صحیح مسلم (807) میں ہے کہ جس نے رات کو سورۃ البقرہ کی آخری دو آیات پڑھیں وہ اس کے لیے کافی ہو جائیں گی۔ اور صحیح مسلم (780) میں ہے کہ جس گھر میں سورۃ البقرہ پڑھی جائے وہاں سے شیطان بھاگ جاتا ہے۔",
                ar = "في الصحيحين (البخاري ٥٠٠٨ ومسلم ٨٠٧): «من قرأ بالآيتين من آخر سورة البقرة في ليلة كفتاه»، وفي مسلم (٧٨٠) أن الشيطان ينفر من البيت الذي تُقرأ فيه سورة البقرة.",
                hi = "सहीह बुख़ारी (5008) व मुस्लिम (807) में है कि रात को इन दो आयतों को पढ़ने वाले के लिए ये काफ़ी हो जाती हैं।"
            ),
            establishedCountNote = LocalizedText4(
                en = "Established practice: once each night (Sahih al-Bukhari 5008; Sahih Muslim 807).",
                ur = "ثابت شدہ معمول: ہر رات ایک مرتبہ (صحیح بخاری 5008؛ صحیح مسلم 807)۔",
                ar = "الثابت في الحديث: قراءتهما في الليل (صحيح البخاري ٥٠٠٨).",
                hi = "प्रमाणित अमल: हर रात एक बार पढ़ें (सहीह बुख़ारी 5008)।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Baqarah",
                bookAndNumber = "Quran 2:285–286",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/2"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih al-Bukhari 5008", "Sahih al-Bukhari 5008", "Sahih", "https://sunnah.com/bukhari:5008"),
                ShifaReferenceSource("Sahih Muslim 780", "Sahih Muslim 780", "Sahih", "https://sunnah.com/muslim:780")
            ),
            searchKeywords = listOf("baqarah", "amanar rasul", "neend ki pareshani", "night", "sihr", "ruqyah", "بقرہ"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "ruqyah_q_ikhlas",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Al-Mu'awwidhat (Part 1 of 3)",
                ur = "معوذات (تین قل — 1/3)",
                ar = "المعوذات (سورة الإخلاص)",
                hi = "मुअव्विज़ात (1/3)"
            ),
            title = LocalizedText4(
                en = "Surah Al-Ikhlas (112:1–4) — Pure Monotheism & Sunnah Protection",
                ur = "سورۃ الإخلاص (112:1–4) — خالص توحید اور نبوی دم کا حصہ",
                ar = "سورة الإخلاص (١١٢: ١-٤) — من المعوذات في الرقية",
                hi = "सूरह अल-इख़लास (112:1–4) — तौहीद और रुक़या का हिस्सा"
            ),
            surahNumber = 112,
            ayahStart = 1,
            ayahEnd = 4,
            islamicContext = LocalizedText4(
                en = "Recited alongside Surah Al-Falaq and Surah An-Naas in the Prophetic Mu'awwidhat Ruqyah (Sahih al-Bukhari 5735, 5017).",
                ur = "نبی کریم ﷺ بیماری میں اور ہر رات سونے سے پہلے سورۃ الإخلاص، الفلق اور الناس پڑھ کر ہاتھوں میں دم کرتے اور جسم پر پھیرتے (صحیح بخاری 5735، 5017)۔",
                ar = "يُقرأ مع الفلق والناس في الرقية النبوية عند المرض وعند النوم (صحيح البخاري ٥٧٣٥ و ٥٠١٧).",
                hi = "सहीह बुख़ारी (5735, 5017) के अनुसार इसे सूरह फ़लक़ और नास के साथ पढ़कर हाथों पर फूँककर शरीर पर फेरा जाता है।"
            ),
            establishedCountNote = LocalizedText4(
                en = "Established count before sleeping: 3 times with cupping palms and wiping over body (Sahih al-Bukhari 5017).",
                ur = "ثابت شدہ تعداد: رات کو سونے سے پہلے 3 مرتبہ ہتھیلیوں میں پھونک کر جسم پر پھیرنا (صحیح بخاری 5017)۔",
                ar = "العدد الثابت عند النوم: ثلاث مرات مع الجمع والنفث والمسح (صحيح البخاري ٥٠١٧).",
                hi = "सोने से पहले प्रमाणित संख्या: 3 बार हथेलियों में फूँककर शरीर पर फेरें (सहीह बुख़ारी 5017)।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5735 — Mu'awwidhat Ruqyah",
                bookAndNumber = "Sahih al-Bukhari 5735",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5735"
            ),
            searchKeywords = listOf("ikhlas", "qul", "muawwidhat", "ruqyah", "sleep", "neend ki pareshani", "قل هو الله أحد"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "ruqyah_q_falaq",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Al-Mu'awwidhat (Part 2 of 3)",
                ur = "معوذات (تین قل — 2/3)",
                ar = "سورة الفلق",
                hi = "सूरह अल-फ़लक़ (2/3)"
            ),
            title = LocalizedText4(
                en = "Surah Al-Falaq (113:1–5) — Refuge from Harm, Darkness, Magic & Envy",
                ur = "سورۃ الفلق (113:1–5) — مخلوق کے شر، سحر اور حسد سے پناہ",
                ar = "سورة الفلق (١١٣: ١-٥) — الاستعاذة من النفاثات والحاسد",
                hi = "सूरह अल-फ़लक़ (113:1–5) — बुराई, जादू और ईर्ष्या (हसद) से पनाह"
            ),
            surahNumber = 113,
            ayahStart = 1,
            ayahEnd = 5,
            islamicContext = LocalizedText4(
                en = "One of the two dedicated Surahs of refuge (Al-Mu'awwidhatayn), specifically seeking Allah's protection from evil created things, darkness, those who blow on knots (sihr), and the envier when he envies.",
                ur = "معوذتین میں پہلی سورت، جس میں خاص طور پر رات کی تاریکی، گرہوں میں پھونکنے والوں (جادوگروں) اور حسد کرنے والے کے شر سے اللہ کی پناہ مانگی گئی ہے۔",
                ar = "إحدى المعوذتين العظيمتين في الاستعاذة من شر المخلوقات والغاسق والنفاثات في العقد والحاسد إذا حسد.",
                hi = "अल्लाह की पनाह माँगने वाली दो विशेष सूरहों में से एक, जिसमें जादूगरों और ईर्ष्यालु (हासद) के शर से पनाह माँगी गई है।"
            ),
            establishedCountNote = LocalizedText4(
                en = "Established count: 3 times before sleep with Al-Ikhlas & An-Naas (Bukhari 5017); also morning/evening Sunnah.",
                ur = "ثابت شدہ تعداد: سونے سے پہلے 3 بار (بخاری 5017)۔",
                ar = "العدد الثابت عند النوم: ثلاث مرات (صحيح البخاري ٥٠١٧).",
                hi = "सोने से पहले 3 बार प्रमाणित (सहीह बुख़ारी 5017)।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Falaq",
                bookAndNumber = "Quran 113:1–5",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/113"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih al-Bukhari 5735", "Sahih al-Bukhari 5735", "Sahih", "https://sunnah.com/bukhari:5735")
            ),
            searchKeywords = listOf("falaq", "hasad", "sihr", "nazar", "ruqyah", "protection", "الفلق", "حسد", "سحر", "फ़लक़"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "ruqyah_q_naas",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Al-Mu'awwidhat (Part 3 of 3)",
                ur = "معوذات (تین قل — 3/3)",
                ar = "سورة الناس",
                hi = "सूरह अन-नास (3/3)"
            ),
            title = LocalizedText4(
                en = "Surah An-Naas (114:1–6) — Refuge from the Whisperer Who Withdraws",
                ur = "سورۃ الناس (114:1–6) — وسوسہ ڈالنے والے شیطان کے شر سے پناہ",
                ar = "سورة الناس (١١٤: ١-٦) — الاستعاذة من شر الوسواس الخناس",
                hi = "सूरह अन-नास (114:1–6) — वसवसा डालने वाले के शर से पनाह"
            ),
            surahNumber = 114,
            ayahStart = 1,
            ayahEnd = 6,
            islamicContext = LocalizedText4(
                en = "Seeks refuge directly in the Lord, Sovereign, and God of mankind against the retreating whisperer (al-Waswas al-Khannas) from among both jinn and people.",
                ur = "انسانوں کے رب، بادشاہ اور معبود کی پناہ طلب کرنا وسوسہ ڈال کر پیچھے ہٹ جانے والے شیطان (جنات اور انسانوں میں سے) کے شر سے۔",
                ar = "الاستعاذة برب الناس وملكهم وإلههم من شر الشيطان الذي يوسوس في صدور الناس ثم يخنس عند ذكر الله.",
                hi = "इंसानों के रब, बादशाह और माबूद की पनाह माँगना उन वसवसों से जो जिन्नों या इंसानों की ओर से दिल में डाले जाते हैं।"
            ),
            establishedCountNote = LocalizedText4(
                en = "Established count: 3 times before sleep with Al-Ikhlas & Al-Falaq (Bukhari 5017).",
                ur = "ثابت شدہ تعداد: سونے سے پہلے 3 مرتبہ (بخاری 5017)۔",
                ar = "العدد الثابت عند النوم: ثلاث مرات (صحيح البخاري ٥٠١٧).",
                hi = "सोने से पहले 3 बार प्रमाणित (सहीह बुख़ारी 5017)।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah An-Naas",
                bookAndNumber = "Quran 114:1–6",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/114"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih al-Bukhari 5735", "Sahih al-Bukhari 5735", "Sahih", "https://sunnah.com/bukhari:5735")
            ),
            searchKeywords = listOf("naas", "nas", "waswase", "waswasa", "nazar", "sihr", "ruqyah", "الناس", "وسوسے", "वसवसे"),
            supportsQuranAudio = true
        ),

        // =========================================================================
        // D. SIHR / KALA ILM — السحر (Quranic Verses)
        // =========================================================================
        ShifaRuqyahItem(
            id = "sihr_q_2_102",
            category = ShifaCategory.SIHR_PROTECTION,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Quranic Truth on Sihr",
                ur = "سحر کے بارے میں قرآنی حقیقت",
                ar = "حقيقة السحر وتعلّمه",
                hi = "सिह्र के बारे में क़ुरआनी सत्य"
            ),
            title = LocalizedText4(
                en = "Surah Al-Baqarah (2:102) — Harm Occurs Only by Allah's Permission",
                ur = "سورۃ البقرہ (2:102) — کوئی اللہ کے اذن کے بغیر کسی کو نقصان نہیں پہنچا سکتا",
                ar = "سورة البقرة (٢: ١٠٢) — وما هم بضارين به من أحد إلا بإذن الله",
                hi = "सूरह अल-बक़रा (2:102) — वे अल्लाह के हुक्म के बिना किसी को नुक़सान नहीं पहुँचा सकते"
            ),
            surahNumber = 2,
            ayahStart = 102,
            ayahEnd = 102,
            islamicContext = LocalizedText4(
                en = "What the Quran teaches about Sihr: (1) Sihr is falsehood and major sin/kufr taught by devils; (2) Magicians have no independent power—'They do not harm anyone through it except by permission of Allah'; (3) Never rely on suspicion or unproven accusations of black magic for hardships or dreams.",
                ur = "سحر کے بارے میں قرآن کیا بتاتا ہے: (1) جادو شیطانی عمل، باطل اور صریح گمراہی ہے؛ (2) جادوگروں کے پاس کوئی ذاتی اختیار نہیں—'وہ اس کے ذریعے اللہ کے حکم کے بغیر کسی کو ذرا نقصان نہیں پہنچا سکتے'؛ (3) محض خواب، وہم یا دنیاوی مشکلات کی بنیاد پر کسی پر کالے علم کا الزام یا فیصلہ ہرگز نہ لگائیں۔",
                ar = "توضح الآية أن السحر كفر وباطل، وأن السحرة لا يملكون ضراً ولا نفعاً إلا بإذن الله وقدره، مما يزرع التوكل ويقطع التعلق بالخرافات والأوهام.",
                hi = "क़ुरआन सिखाता है कि सिह्र (जादू) शैतानी और बातिल काम है, और वे अल्लाह की अनुमति के बिना किसी को कोई हानि नहीं पहुँचा सकते। केवल सपनों या परेशानियों के आधार पर किसी पर जादू का इल्ज़ाम न लगाएँ।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Baqarah 2:102",
                bookAndNumber = "Quran 2:102",
                authenticityGrade = "Noble Quran (Verified Uthmani Text)",
                url = "https://quran.com/2"
            ),
            searchKeywords = listOf("sihr", "kala ilm", "magic", "سحر", "کالا علم", "जादू", "सिह्र"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "sihr_q_7_117_122",
            category = ShifaCategory.SIHR_PROTECTION,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Truth Nullifies Falsehood",
                ur = "حق کے سامنے باطل کا مٹ جانا",
                ar = "إبطال السحر بالحق",
                hi = "सत्य के सामने बातिल का मिटना"
            ),
            title = LocalizedText4(
                en = "Surah Al-A'raf (7:117–122) — What They Fabricated Was Swallowed by the Truth",
                ur = "سورۃ الأعراف (7:117–122) — حق ظاہر ہوا اور سارا جادوگرانہ مکر باطل ہو گیا",
                ar = "سورة الأعراف (٧: ١١٧-١٢٢) — فوقع الحق وبطل ما كانوا يعملون",
                hi = "सूरह अल-आराफ़ (7:117–122) — सत्य स्थापित हुआ और उनका रचा झूठ ध्वस्त हो गया"
            ),
            surahNumber = 7,
            ayahStart = 117,
            ayahEnd = 122,
            islamicContext = LocalizedText4(
                en = "Relates how Allah aided Prophet Musa (AS) so truth stood firm and illusion collapsed—reminding believers that seeking refuge in Allah and adhering to Sunnah Ruqyah defeats fear of falsehood.",
                ur = "حضرت موسیٰ علیہ السلام کے واقعے سے واضح ہوتا ہے کہ اللہ کی نصرت سے حق غالب آتا ہے اور جادوگروں کی فریب کاری مٹ جاتی ہے۔",
                ar = "بيان أن الحق يظهر ويبطل الله به كيد السحرة، مع التوكل على الله وحده.",
                hi = "हज़रत मूसा (अलैहि.) के प्रसंग से स्पष्ट होता है कि अल्लाह की मदद से सत्य विजयी होता है और जादूगरों का झूठ मिट जाता है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-A'raf 7:117–122",
                bookAndNumber = "Quran 7:117–122",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/7"
            ),
            searchKeywords = listOf("sihr", "kala ilm", "araf", "musa", "سحر", "کالا علم"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "sihr_q_10_81_82",
            category = ShifaCategory.SIHR_PROTECTION,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Allah Nullifies Sihr",
                ur = "اللہ باطل کو مٹا دیتا ہے",
                ar = "إن الله سيبطله",
                hi = "अल्लाह बातिल को निष्क्रिय कर देता है"
            ),
            title = LocalizedText4(
                en = "Surah Yunus (10:81–82) — \"Indeed, Allah Will Expose It as Worthless\"",
                ur = "سورۃ یونس (10:81–82) — بے شک اللہ اسے ضرور باطل کر دے گا",
                ar = "سورة يونس (١٠: ٨١-٨٢) — ما جئتم به السحر إن الله سيبطله",
                hi = "सूरह यूनुस (10:81–82) — \"निश्चय ही अल्लाह इसे बेकार कर देगा\""
            ),
            surahNumber = 10,
            ayahStart = 81,
            ayahEnd = 82,
            islamicContext = LocalizedText4(
                en = "Musa (AS) declared with absolute certainty in Allah: 'What you have brought is magic; indeed, Allah will nullify it. Indeed, Allah does not amend the work of corrupters.'",
                ur = "مومن کا اعتماد جادو یا تعویذ گنڈوں پر نہیں بلکہ اس بات پر ہوتا ہے کہ اللہ ہی مفسدوں کے عمل کو باطل فرماتا اور حق کو ثابت رکھتا ہے۔",
                ar = "إعلان التوكل التام على الله الذي لا يصلح عمل المفسدين ويبطل السحر ويحق الحق بكلماته.",
                hi = "अल्लाह पर पूर्ण भरोसा कि वह फ़साद करने वालों के काम को सँवारता नहीं, बल्कि अपने हुक्म से सत्य को स्थापित करता है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Yunus 10:81–82",
                bookAndNumber = "Quran 10:81–82",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/10/81"
            ),
            searchKeywords = listOf("sihr", "yunus", "kala ilm", "سحر"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "sihr_q_20_69",
            category = ShifaCategory.SIHR_PROTECTION,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Magicians Never Succeed",
                ur = "جادوگر کبھی فلاح نہیں پاتا",
                ar = "ولا يفلح الساحر حيث أتى",
                hi = "जादूगर कभी सफल नहीं होता"
            ),
            title = LocalizedText4(
                en = "Surah Taha (20:69) — \"The Magician Will Not Succeed Wherever He Goes\"",
                ur = "سورۃ طٰہٰ (20:69) — اور جادوگر جہاں کہیں جائے کامیاب نہیں ہو سکتا",
                ar = "سورة طه (٢٠: ٦٩) — ولا يفلح الساحر حيث أتى",
                hi = "सूरह ता-हा (20:69) — \"जादूगर जहाँ भी जाए, कभी सफल नहीं हो सकता\""
            ),
            surahNumber = 20,
            ayahStart = 69,
            ayahEnd = 69,
            islamicContext = LocalizedText4(
                en = "Dispels fear of sorcerers by reminding believers that falsehood is ultimately powerless against Allah's command and sincere adherence to the Quran and Sunnah.",
                ur = "یہ آیت خوف و ہراس دور کرتی ہے کہ جادوگر کبھی فلاح نہیں پاتا۔ تاہم کسی فرد پر بغیر قطعی شرعی ثبوت جادو کا الزام لگانا یا کسی عامل کے پاس جانا سخت ممنوع ہے۔",
                ar = "تُذهب الآية الوهم والخوف من السحرة، وتؤكد أن النجاة بالالتجاء إلى الله واتباع الرقية الشرعية وحدها دون الكهنة والمشعوذين.",
                hi = "यह आयत भय दूर करती है कि जादूगर कभी सफल नहीं होता। बिना प्रमाण किसी पर जादू का शक न करें और न ही किसी तांत्रिक के पास जाएँ।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Taha 20:69",
                bookAndNumber = "Quran 20:69",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/20"
            ),
            searchKeywords = listOf("sihr", "taha", "kala ilm", "سحر", "کالا علم"),
            supportsQuranAudio = true
        ),

        // =========================================================================
        // E. NAZAR & HASAD — العين والحسد (Quranic Passages)
        // =========================================================================
        ShifaRuqyahItem(
            id = "nazar_q_68_51_52",
            category = ShifaCategory.NAZAR_HASAD,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Quranic Reference on Envious Gaze",
                ur = "بدنظری و حسد کا قرآنی ذکر",
                ar = "آية العين في سورة القلم",
                hi = "नज़र व ईर्ष्या का क़ुरआनी संदर्भ"
            ),
            title = LocalizedText4(
                en = "Surah Al-Qalam (68:51–52) — Envious Gazes & The Quran as a Reminder",
                ur = "سورۃ القلم (68:51–52) — حاسدوں کی بدنظری اور قرآن بطور نصیحت",
                ar = "سورة القلم (٦٨: ٥١-٥٢) — وإن يكاد الذين كفروا ليزلقونك بأبصارهم",
                hi = "सूरह अल-क़लम (68:51–52) — बुरी नज़र और क़ुरआन की नसीहत"
            ),
            surahNumber = 68,
            ayahStart = 51,
            ayahEnd = 52,
            islamicContext = LocalizedText4(
                en = "Ibn Kathir and Al-Mukhtasar mention that the disbelievers looked at the Messenger of Allah ﷺ with intense hostility and envious gazes wishing to strike him with the evil eye; Allah protected him and affirmed the Quran as a reminder to all worlds. Protection is strictly through Quranic Mu'awwidhat and authentic Sunnah Duas—not blue beads, amulets, or superstitions.",
                ur = "تفسیر ابن کثیر اور المختصر کے مطابق کفار حسد اور عداوت سے نبی کریم ﷺ کی طرف بدنظری سے دیکھتے تھے، مگر اللہ نے آپ کی حفاظت فرمائی۔ نظرِ بد سے حفاظت صرف مسنون رقیہ و دعا سے ہے، دھاگوں، تعویذوں یا توہمات سے نہیں۔",
                ar = "ذكر ابن كثير وغيره أن الكفار كانوا يصيبون النبي صلى الله عليه وسلم بأعينهم حسداً وبغضاً فعصمه الله. والوقاية منه بالرقية الشرعية والتبريك دون التمائم والخرافات.",
                hi = "तफ़सीर इब्न कसीर के अनुसार काफ़िर ईर्ष्या से नबी ﷺ को बुरी नज़र से देखते थे, और अल्लाह ने आपकी रक्षा की। नज़र से बचाव केवल मसनून रुक़या व दुआ से है, तावीज़ या अंधविश्वास से नहीं।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Qalam 68:51–52",
                bookAndNumber = "Quran 68:51–52",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/68/51"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Quran.com — Surah Al-Falaq (Refuge from Hasad)", "Quran 113:5", "Noble Quran", "https://quran.com/113"),
                ShifaReferenceSource("Quran.com — Surah An-Naas", "Quran 114:1–6", "Noble Quran", "https://quran.com/114")
            ),
            searchKeywords = listOf("nazar", "hasad", "evil eye", "qalam", "نظر", "حسد", "नज़र", "हसद"),
            supportsQuranAudio = true
        ),

        // =========================================================================
        // F. WASWASE — الوسوسة (Quranic Verses)
        // =========================================================================
        ShifaRuqyahItem(
            id = "waswasa_q_7_200",
            category = ShifaCategory.WASWASE,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Refuge from Shaytan's Prompting",
                ur = "شیطانی وسوسے پر اللہ کی پناہ",
                ar = "الاستعاذة عند نزغ الشيطان",
                hi = "शैतानी वसवसे में अल्लाह की शरण"
            ),
            title = LocalizedText4(
                en = "Surah Al-A'raf (7:200) — Seeking Refuge Immediately Upon Whispering",
                ur = "سورۃ الأعراف (7:200) — جب شیطان کی طرف سے کوئی وسوسہ آئے تو فوراً اللہ کی پناہ مانگو",
                ar = "سورة الأعراف (٧: ٢٠٠) — وإما ينزغنك من الشيطان نزغ فاستعذ بالله",
                hi = "सूरह अल-आराफ़ (7:200) — जब शैतान कोई उकसावा डाले, तो अल्लाह की पनाह माँगो"
            ),
            surahNumber = 7,
            ayahStart = 200,
            ayahEnd = 200,
            islamicContext = LocalizedText4(
                en = "Allah's direct prescription whenever an unwanted prompting or whisper touches the heart: turn immediately to Allah with 'A'udhu billahi minash-shaytanir-rajim'—He is All-Hearing and All-Knowing.",
                ur = "جب بھی دل میں کوئی ناپسندیدہ وسوسہ، غصہ یا شیطانی تحریک پیدا ہو، تو فوراً 'أعوذ بالله من الشيطان الرجيم' کہہ کر اللہ کی پناہ لیں اور اس خیال کو نظر انداز کر دیں۔",
                ar = "التوجيه القرآني المباشر عند خطور الوساوس أو نزغات الشيطان: الفزع الفوري إلى الاستعاذة بالله وعدم التمادي مع الخاطر.",
                hi = "दिल में कोई अवांछित विचार या शैतानी वसवसा आते ही तुरंत 'अऊज़ु बिल्लाहि मिनश-शैतानिर-रजीम' पढ़कर अल्लाह की शरण माँगें।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-A'raf 7:200",
                bookAndNumber = "Quran 7:200",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/7"
            ),
            searchKeywords = listOf("waswase", "waswasa", "whispers", "thoughts", "doubts", "وسوسے", "वसवसे"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "waswasa_q_41_36",
            category = ShifaCategory.WASWASE,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Quranic Remedy for Inner Doubts",
                ur = "وسوسوں کے خلاف قرآنی ہدایت",
                ar = "دفع الوسوسة بالاستعاذة",
                hi = "वसवसे से बचाव का क़ुरआनी मार्ग"
            ),
            title = LocalizedText4(
                en = "Surah Fussilat (41:36) — \"Seek Refuge in Allah; Indeed He Is All-Hearing\"",
                ur = "سورۃ فصلت (41:36) — پس اللہ کی پناہ مانگو، بے شک وہ سب کچھ سننے اور جاننے والا ہے",
                ar = "سورة فصلت (٤١: ٣٦) — فاستعذ بالله إنه هو السميع العليم",
                hi = "सूरह फ़ुस्सिलात (41:36) — \"अल्लाह की शरण माँगो; निश्चय ही वह सुनने और जानने वाला है\""
            ),
            surahNumber = 41,
            ayahStart = 36,
            ayahEnd = 36,
            islamicContext = LocalizedText4(
                en = "Reassures believers that experiencing an intrusive whisper is NOT a sin; resisting and ignoring it by seeking refuge in Allah is a sign of sincere faith (Sahih Muslim 132a). Do NOT assume intrusive thoughts are jinn possession or black magic.",
                ur = "ضروری تنبیہ: دل میں ناپسندیدہ وسوسے آنا گناہ نہیں اور نہ ہی یہ کسی جن یا جادو کی علامت ہے۔ ایسے خیال کو بُرا سمجھ کر اللہ کی پناہ مانگنا ایمان کی علامت ہے (صحیح مسلم 132a)۔",
                ar = "تنبيه مهم: لا يُؤاخذ المسلم بالخاطر العارض الذي يكرهه، ولا يُعد ذلك مساً ولا سحراً؛ بل كراهيته والاستعاذة بالله منه من صريح الإيمان.",
                hi = "महत्वपूर्ण: मन में अनचाहे वसवसे आना न तो गुनाह है और न ही किसी जिन्न या जादू का प्रमाण; उनसे नफ़रत करना और अल्लाह की शरण माँगना ईमान की निशानी है (सहीह मुस्लिम 132a)।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Fussilat 41:36",
                bookAndNumber = "Quran 41:36",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/41/36"
            ),
            searchKeywords = listOf("waswase", "waswasa", "ibadat", "doubts", "salah", "وسوسے", "वसवसे"),
            supportsQuranAudio = true
        ),

        // =========================================================================
        // G. SHIFA-E-QALB AUR SUKOON — شفاء القلب والسكينة (Quranic Verses)
        // =========================================================================
        ShifaRuqyahItem(
            id = "qalb_q_94_5_6",
            category = ShifaCategory.SHIFA_E_QALB,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Relief With Hardship",
                ur = "تنگی کے ساتھ آسانی کا وعدہ",
                ar = "إن مع العسر يسراً",
                hi = "कठिनाई के साथ आसानी"
            ),
            title = LocalizedText4(
                en = "Surah Ash-Sharh (94:5–6) — \"Indeed, With Hardship Comes Ease\"",
                ur = "سورۃ الانشراح (94:5–6) — بے شک ہر تنگی کے ساتھ آسانی ہے",
                ar = "سورة الشرح (٩٤: ٥-٦) — فإن مع العسر يسراً",
                hi = "सूरह अश-शरह (94:5–6) — \"निश्चय ही कठिनाई के साथ आसानी है\""
            ),
            surahNumber = 94,
            ayahStart = 5,
            ayahEnd = 6,
            islamicContext = LocalizedText4(
                en = "Repeated twice for certainty: Allah places ease alongside every trial, uplifting anxious or sorrowful hearts toward hope and patience.",
                ur = "دگنی تاکید کے ساتھ تسلی کہ کوئی مشکل مستقل نہیں؛ اللہ تعالیٰ ہر تنگی کے ساتھ کشادگی اور راحت پیدا فرماتا ہے۔",
                ar = "بشارة إلهية مؤكدة بأن الفرج مقترن بالشدة وأن اليسر غالب على العسر.",
                hi = "दोहरी तसल्ली कि कोई कठिनाई स्थायी नहीं; अल्लाह हर मुश्किल के साथ आसानी प्रदान करता है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Ash-Sharh 94:5–6",
                bookAndNumber = "Quran 94:5–6",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/94/5"
            ),
            searchKeywords = listOf("dil ka gham", "gham", "sabr", "pareshani", "sukoon", "ease", "غم", "پریشانی", "दिल का ग़म"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "qalb_q_21_87",
            category = ShifaCategory.SHIFA_E_QALB,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Supplication of Prophet Yunus (AS)",
                ur = "دعائے یونسؑ (کرب و غم سے نجات)",
                ar = "دعاء ذي النون عند الكرب",
                hi = "हज़रत यूनुस (अलैहि.) की दुआ"
            ),
            title = LocalizedText4(
                en = "Surah Al-Anbiya (21:87) — La Ilaha Illa Anta Subhanaka Inni Kuntu Minaz-Zalimin",
                ur = "سورۃ الأنبیاء (21:87) — مصیبت اور گھٹن میں حضرت یونسؑ کی قرآنی دعا",
                ar = "سورة الأنبياء (٢١: ٨٧) — لا إله إلا أنت سبحانك إني كنت من الظالمين",
                hi = "सूरह अल-अम्बिया (21:87) — संकट में हज़रत यूनुस (अलैहि.) की क़ुरआनी दुआ"
            ),
            surahNumber = 21,
            ayahStart = 87,
            ayahEnd = 87,
            islamicContext = LocalizedText4(
                en = "In Jami' at-Tirmidhi (3505, Hasan/Sahih), the Prophet ﷺ said no Muslim calls upon Allah with this supplication in any difficulty except that Allah responds.",
                ur = "جامع ترمذی (3505) میں نبی کریم ﷺ نے فرمایا کہ جو مسلمان کسی بھی مصیبت و غم میں اس دعا سے اللہ کو پکارے، اللہ اس کی دعا قبول فرماتا ہے۔",
                ar = "في جامع الترمذي (٣٥٠٥): ما دعا بها مسلم ربه في شيء قط إلا استجاب الله له.",
                hi = "जामिअ तिर्मिज़ी (3505) में है कि जब कोई मुसलमान किसी भी संकट में इस दुआ से अल्लाह को पुकारता है, तो अल्लाह उसकी दुआ सुनता है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah Al-Anbiya 21:87",
                bookAndNumber = "Quran 21:87",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/21/87"
            ),
            searchKeywords = listOf("gham", "dil ka gham", "distress", "fear", "khauf", "yunus", "غم", "ग़म"),
            supportsQuranAudio = true
        ),
        ShifaRuqyahItem(
            id = "qalb_q_9_40",
            category = ShifaCategory.SHIFA_E_QALB,
            type = ShifaItemType.QURAN_AYAH,
            topicTag = LocalizedText4(
                en = "Allah's Sakinah Upon the Heart",
                ur = "خوف و غم میں سکینت کا نزول",
                ar = "لا تحزن إن الله معنا",
                hi = "डर में अल्लाह की ओर से सुकून"
            ),
            title = LocalizedText4(
                en = "Surah At-Tawbah (9:40) — \"Do Not Grieve; Indeed Allah Is With Us\"",
                ur = "سورۃ التوبہ (9:40) — غم نہ کرو، بے شک اللہ ہمارے ساتھ ہے",
                ar = "سورة التوبة (٩: ٤٠) — لا تحزن إن الله معنا فأنزل الله سكينته عليه",
                hi = "सूरह अत-तौबा (9:40) — \"ग़म मत करो, निश्चय ही अल्लाह हमारे साथ है\""
            ),
            surahNumber = 9,
            ayahStart = 40,
            ayahEnd = 40,
            islamicContext = LocalizedText4(
                en = "Spoken by the Prophet ﷺ to Abu Bakr As-Siddiq (RA) in the cave of Thawr, after which Allah sent down His Sakinah (tranquility) upon him.",
                ur = "غارِ ثور کے نازک لمحات میں نبی کریم ﷺ نے حضرت ابوبکر صدیقؓ کو تسلی دی اور اللہ تعالیٰ نے قلبِ اطہر پر سکینت نازل فرمائی۔",
                ar = "كلمة الطمأنينة في غار ثور حين أحاط الخوف، فأنزل الله سكينته ونصره.",
                hi = "ग़ार-ए-सौर के कठिन समय में नबी ﷺ ने हज़रत अबू बक्र (रज़ि.) को दिलासा दिया और अल्लाह ने सुकून नाज़िल किया।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Quran.com — Surah At-Tawbah 9:40",
                bookAndNumber = "Quran 9:40",
                authenticityGrade = "Noble Quran",
                url = "https://quran.com/9/40"
            ),
            searchKeywords = listOf("dil ka gham", "khauf", "fear", "sakinah", "sukoon", "غم", "سुकून"),
            supportsQuranAudio = true
        )
    )
}
