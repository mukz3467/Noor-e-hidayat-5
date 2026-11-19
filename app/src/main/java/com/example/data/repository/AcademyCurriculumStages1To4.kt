package com.example.data.repository

import com.example.data.model.AcademyLesson
import com.example.data.model.AcademyStage
import com.example.data.model.IslamicReference
import com.example.data.model.PrayerStepRecitation
import com.example.data.model.StageToolType

object AcademyCurriculumStages1To4 {

    val stage1 = AcademyStage(
        stageNumber = 1,
        id = "stage_1",
        titleEn = "Stage 1: Introduction to Islam",
        titleUr = "مرحلہ 1: اسلام کا بنیادی تعارف",
        titleAr = "المرحلة ١: مدخل إلى الإسلام",
        titleHi = "चरण 1: इस्लाम का परिचय",
        subtitleEn = "Foundations for beginners & new Muslims",
        subtitleUr = "نئے مسلمانوں اور مبتدی طلبہ کے لیے بنیادی رہنمائی",
        subtitleAr = "الأساسيات للمبتدئين والمسلمين الجدد",
        subtitleHi = "शुरुआती और नए मुसलमानों के लिए बुनियादी मार्गदर्शन",
        overviewEn = "Begin peacefully with the core meaning of Islam, knowing Allah (Tawheed), Messenger Muhammad ﷺ, the Holy Quran, the Shahadah, and your first practical steps.",
        overviewUr = "اس مرحلے میں اسلام کا حقیقی مفہوم، اللہ کی معرفت (توحید)، رسول اللہ ﷺ کا تعارف، قرآن مجید، کلمہ شہادت اور ابتدائی عملی ترجیحات سیکھیں۔",
        overviewAr = "تعرّف في هذه المرحلة على معنى الإسلام، وتوحيد الله، وسيرة النبي ﷺ، والقرآن الكريم، والشهادتين، وأولويات المسلم الجديد.",
        overviewHi = "इस चरण में इस्लाम का अर्थ, अल्लाह की पहचान (तौहीद), पैगंबर मुहम्मद ﷺ, पवित्र क़ुरआन, शहादा और शुरुआती प्राथमिकताएं सीखें।",
        interactiveToolType = StageToolType.NONE,
        lessons = listOf(
            AcademyLesson(
                id = "s1_l1",
                stageNumber = 1,
                lessonNumber = 1,
                titleEn = "1. What Is Islam? Meaning of Submission & Purpose of Life",
                titleUr = "1. اسلام کیا ہے؟ اطاعت، سلامتی اور زندگی کا مقصد",
                titleAr = "١. ما هو الإسلام؟ معنى التسليم وغاية الحياة",
                titleHi = "1. इस्लाम क्या है? समर्पण, शांति और जीवन का उद्देश्य",
                summaryEn = "Islam means willingly submitting to the One Creator, finding inner peace and living with purpose.",
                summaryUr = "اسلام کا معنی اللہ وحدہ لا شریک کی رضا کے سامنے سرِ تسلیم خم کرنا اور حقیقی امن پانا ہے۔",
                summaryAr = "الإسلام هو الاستسلام لله بالتوحيد والانقياد له بالطاعة والعيش بسلام وغاية.",
                summaryHi = "इस्लाम का अर्थ एक ईश्वर (अल्लाह) की आज्ञा के आगे विनम्रता से झुकना और शांति पाना है।",
                bodyEn = "The Arabic word 'Islam' shares its root with 'Salaam' (peace) and signifies peaceful, conscious submission to Allah, the Creator of the universe. Every human soul is created with a clear purpose: to recognize the Creator, worship Him sincerely, uphold justice, and show mercy to creation.\n\nIslam does not ask you to abandon reason or human warmth. Rather, it connects everyday acts—kind words, honest work, caring for family—to a higher spiritual meaning when done with sincere intention.",
                bodyUr = "عربی لفظ 'اسلام' کا مادہ 'سَلَم' ہے جس کے معنی امن، سلامتی اور خود کو اللہ کے سپرد کر دینے کے ہیں۔ اسلام کا بنیادی پیغام یہ ہے کہ انسان اپنے خالق کو پہچانے، اس کی خالص عبادت کرے اور اس کی مخلوق کے ساتھ عدل و رحمت کا برتاؤ کرے۔\n\nاسلام فطرت کے عین مطابق دین ہے۔ اگر نیت خالص ہو تو روزمرہ کے اچھے کام—جیسے دیانت داری سے روزی کمانا، ماں باپ کی خدمت کرنا اور مسکرا کر بات کرنا—بھی عبادت بن جاتے ہیں۔",
                bodyAr = "كلمة 'الإسلام' مأخوذة من الاستسلام لله بالتوحيد والانقياد له بالطاعة، وهي مرتبطة بجذر السلام والطمأنينة. خُلق الإنسان لغاية سامية: معرفة الله وعبادته وإعمار الأرض بالعدل والرحمة.\n\nالإسلام دين الفطرة؛ فلا يفصل بين الروح والجسد، بل يجعل الأعمال اليومية المباحة عبادةً إذا صاحبتها النية الصالحة.",
                bodyHi = "अरबी शब्द 'इस्लाम' की जड़ 'सलाम' (शांति) से जुड़ी है और इसका अर्थ है एक सृष्टिकर्ता अल्लाह के सामने शांतिपूर्ण और स्वेच्छा से समर्पण करना। इंसान के जीवन का उद्देश्य अल्लाह को पहचानना, उसकी इबादत करना, और सृष्टि के साथ न्याय व दया से रहना है।\n\nइस्लाम रोज़मर्रा के अच्छे कामों—जैसे ईमानदारी से काम करना, परिवार की देखभाल और मीठा बोलना—को भी नेक नीयत के साथ इबादत बना देता है।",
                examplesEn = listOf(
                    "Helping a neighbor or smiling kindly counts as charity (Sadaqah) when done for Allah's sake.",
                    "A Muslim greets others with 'As-Salamu Alaykum' (Peace be upon you)."
                ),
                examplesUr = listOf(
                    "مسلمان ملاقات کے وقت 'السلام علیکم' (تم پر سلامتی ہو) کہہ کر امن کی دعا دیتا ہے۔",
                    "مسکرا کر ملنا اور راستے سے تکلیف دہ چیز ہٹانا بھی صدقہ ہے۔"
                ),
                examplesAr = listOf(
                    "تحية المسلم هي 'السلام عليكم' دعاءً بالأمن والرحمة.",
                    "تبسمك في وجه أخيك وإماطة الأذى عن الطريق صدقة."
                ),
                examplesHi = listOf(
                    "मुसलमान मिलने पर 'अस्सलामु अलैकुम' (आप पर शांति हो) कहकर अभिवादन करते हैं।",
                    "मुस्कुराकर बात करना और किसी की मदद करना भी नेकी (सदक़ा) है।"
                ),
                practicalTaskEn = "Pause for two minutes today and reflect on your blessings; thank Allah sincerely in your own words.",
                practicalTaskUr = "آج دو منٹ خاموشی سے بیٹھ کر اپنی نعمتوں پر غور کریں اور دل سے اللہ کا شکر ادا کریں۔",
                practicalTaskAr = "خصص دقيقتين اليوم للتفكر في نعم الله عليك واشكره بصدق.",
                practicalTaskHi = "आज दो मिनट रुककर अपने जीवन की नेमतों पर विचार करें और दिल से अल्लाह का शुक्र अदा करें।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Adh-Dhariyat (51:56)", "Purpose of creation: worshiping Allah alone"),
                    IslamicReference("Holy Quran", "Surah Al-Baqarah (2:256)", "No compulsion in religion"),
                    IslamicReference("Sahih Muslim", "Hadith 1009 / Abu Hurairah", "Good deeds and removing harm count as charity")
                ),
                quizId = "q_s1_1"
            ),
            AcademyLesson(
                id = "s1_l2",
                stageNumber = 1,
                lessonNumber = 2,
                titleEn = "2. Who Is Allah? Oneness (Tawheed) & Divine Mercy",
                titleUr = "2. اللہ کون ہے؟ توحید اور رحمتِ الٰہی",
                titleAr = "٢. من هو الله؟ التوحيد والرحمة الإلهية",
                titleHi = "2. अल्लाह कौन है? एकेश्वरवाद (तौहीद) और दया",
                summaryEn = "Allah is the Arabic name for the One and Only God—the Creator with no partners, equals, or limits.",
                summaryUr = "اللہ اس ایک وحدہ لا شریک خالق کا ذاتی نام ہے جو تمام جہانوں کا رب، بے نیاز اور نہایت رحم والا ہے۔",
                summaryAr = "الله هو الخالق الواحد الأحد الذي ليس كمثله شيء، المتصف بصفات الكمال والرحمة الشاملة.",
                summaryHi = "अल्लाह उस एकमात्र सृष्टिकर्ता का नाम है जिसका कोई साझी, माता-पिता या संतान नहीं है।",
                bodyEn = "'Allah' is the proper Arabic name for the One True God—used also by Arabic-speaking Jews and Christians. In the Quran, Surah Al-Ikhlas (Chapter 112) summarizes Allah's perfection in four short verses:\n1. He is Allah, the One.\n2. Allah, the Eternal Refuge upon whom all depend.\n3. He neither begets nor is born.\n4. Nor is there to Him any equivalent.\n\nAllah has no gender, body parts like humans, or limitations. He hears every whispered prayer directly without needing any intermediary.",
                bodyUr = "'اللہ' اس اکیلے معبود کا نام ہے جس نے آسمان، زمین اور ہمیں پیدا کیا۔ سورۃ الإخلاص (112) چار مختصر آیات میں اللہ کی پہچان کراتی ہے: وہ ایک ہے، بے نیاز ہے، نہ اس کی کوئی اولاد ہے اور نہ وہ کسی کی اولاد ہے، اور کوئی اس کے برابر نہیں۔\n\nاللہ تک پہنچنے کے لیے کسی واسطے یا سفارش کی ضرورت نہیں؛ وہ بندے کی شہ رگ سے بھی زیادہ قریب ہے اور دل کی خاموش دعا بھی سنتا ہے۔",
                bodyAr = "لفظ الجلالة 'الله' هو اسم المعبود الحق الخالق المدبّر. وتلخّص سورة الإخلاص (١١٢) عقيدة التوحيد الخالص: هو الله أحد، الله الصمد الذي تقصده الخلائق، لم يلد ولم يولد، ولم يكن له كفواً أحد.\n\nالله سبحانه منزه عن الشبيه والشريك، قريب من عباده يجيب دعوة الداع إذا دعاه بلا واسطة.",
                bodyHi = "'अल्लाह' उस एक सच्चे ईश्वर का अरबी नाम है। पवित्र क़ुरआन की सूरह अल-इख़्लास (112) चार छोटी आयतों में उनका परिचय देती है: वह एक है, स्वावलंबी (जिस पर सब निर्भर हैं) है, न उसकी कोई संतान है न वह किसी से जन्मा है, और उसके बराबर कोई नहीं।\n\nअपनी बात अल्लाह तक पहुँचाने के लिए किसी मध्यस्थ की ज़रूरत नहीं; वह दिल की हर सच्ची पुकार सीधे सुनता है।",
                examplesEn = listOf(
                    "Bismillah Ar-Rahman Ar-Rahim: Two of Allah's most repeated names celebrate His vast and continual mercy.",
                    "Quran 2:255 (Ayat al-Kursi) describes His eternal life, complete knowledge, and effortless sovereignty."
                ),
                examplesUr = listOf(
                    "قرآن کی تقریباً ہر سورۃ 'بسم اللہ الرحمن الرحیم' سے شروع ہوتی ہے جو رحمت کی علامت ہے۔",
                    "آیت الکرسی (2:255) اللہ کی حیاتِ دائمی اور کامل علم کو بیان کرتی ہے۔"
                ),
                examplesAr = listOf(
                    "تبدأ سور القرآن بـ 'بسم الله الرحمن الرحيم' لإبراز سعة رحمة الله.",
                    "آية الكرسي (البقرة: ٢٥٥) أعظم آية في بيان كمال قيومية الله وعلمه."
                ),
                examplesHi = listOf(
                    "क़ुरआन की लगभग हर सूरह 'बिस्मिल्लाहिर रहमानिर रहीम' से शुरू होती है, जो उनकी अपार दया को दर्शाती है।",
                    "आयतल कुर्सी (2:255) उनके संपूर्ण ज्ञान और अनंत जीवन का वर्णन करती है।"
                ),
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Ikhlas (112:1-4)"),
                    IslamicReference("Holy Quran", "Surah Al-Baqarah (2:255 - Ayat al-Kursi)"),
                    IslamicReference("Holy Quran", "Surah Al-Fatihah (1:1-4)")
                ),
                quizId = "q_s1_2"
            ),
            AcademyLesson(
                id = "s1_l3",
                stageNumber = 1,
                lessonNumber = 3,
                titleEn = "3. Who Was Prophet Muhammad ﷺ?",
                titleUr = "3. حضرت محمد ﷺ کا تعارف اور پیغامِ رحمت",
                titleAr = "٣. من هو النبي محمد ﷺ؟",
                titleHi = "3. पैगंबर मुहम्मद ﷺ कौन थे?",
                summaryEn = "The final Messenger sent by Allah to confirm previous prophets and bring universal mercy.",
                summaryUr = "اللہ کے آخری نبی اور رسول ﷺ، جنہیں تمام انسانوں کے لیے رحمت بنا کر بھیجا گیا۔",
                summaryAr = "خاتم الأنبياء والمرسلين، بعثه الله رحمةً للعالمين ومتمماً لمكارم الأخلاق.",
                summaryHi = "अल्लाह के अंतिम पैगंबर ﷺ जिन्हें पूरी मानवता के लिए दया और उत्तम चरित्र की शिक्षा देकर भेजा गया।",
                bodyEn = "Muslims believe in all prophets sent by Allah—including Adam, Noah (Nuh), Abraham (Ibrahim), Moses (Musa), and Jesus (Isa), peace be upon them all. Muhammad ﷺ (570–632 CE) was born in Makkah and known even before prophethood as 'As-Sadiq Al-Amin' (The Truthful, The Trustworthy).\n\nAt age 40, while meditating in the Cave of Hira, he received the first revelation of the Quran through Angel Jibreel. He did not claim divinity; he was a humble human servant and the final Messenger carrying guidance for all humanity.",
                bodyUr = "مسلمان اللہ کے تمام انبیاء—بشمول حضرت آدم، نوح، ابراہیم، موسیٰ اور عیسیٰ علیہم السلام—پر ایمان رکھتے ہیں۔ حضرت محمد ﷺ مکہ مکرمہ میں پیدا ہوئے اور نبوت سے پہلے ہی 'صادق و امین' کے لقب سے مشہور تھے۔\n\nچالیس سال کی عمر میں غارِ حرا میں آپ ﷺ پر پہلی وحی نازل ہوئی۔ آپ ﷺ اللہ کے بندے اور آخری رسول ہیں جو پوری انسانیت کے لیے امن، عدل اور اخلاق کا کامل نمونہ لے کر آئے۔",
                bodyAr = "يؤمن المسلمون بجميع الأنبياء كآدم ونوح وإبراهيم وموسى وعيسى عليهم السلام. ومحمد بن عبد الله ﷺ هو خاتمهم، عُرف في مكة قبل البعثة بـ 'الصادق الأمين'.\n\nفي سن الأربعين نزل عليه الوحي في غار حراء، فبلغ الرسالة بأمانة، وكان عبداً رسولاً يُقتدى بهديه لا يُعبد من دون الله.",
                bodyHi = "मुसलमान आदम, नूह, इब्राहीम, मूसा और ईसा (अलैहिमुस्सलाम) सहित सभी पैगंबरों पर ईमान रखते हैं। मुहम्मद ﷺ मक्का में जन्मे और पैगंबरी से पहले ही 'अस-सादिक़ अल-अमीन' (सच्चे और ईमानदार) के रूप में प्रसिद्ध थे।\n\n40 वर्ष की आयु में ग़ार-ए-हिरा में उन पर पहला संदेश (वही) उतरा। वे अल्लाह के बंदे और अंतिम संदेशवाहक थे, जिनका जीवन दया, नम्रता और न्याय की मिसाल है।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Anbiya (21:107)", "Sent as a mercy to the worlds"),
                    IslamicReference("Holy Quran", "Surah Al-Ahzab (33:40)", "Seal of the Prophets"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 3", "Account of the first revelation in Cave Hira")
                )
            ),
            AcademyLesson(
                id = "s1_l4",
                stageNumber = 1,
                lessonNumber = 4,
                titleEn = "4. What Is the Holy Quran?",
                titleUr = "4. قرآن مجید کیا ہے؟ وحی، حفاظت اور ہدایت",
                titleAr = "٤. ما هو القرآن الكريم؟",
                titleHi = "4. पवित्र क़ुरआन क्या है?",
                summaryEn = "The verbatim Arabic Word of Allah revealed to Prophet Muhammad ﷺ over 23 years as guidance for humanity.",
                summaryUr = "اللہ تعالیٰ کا محفوظ کلام جو 23 برس کے عرصے میں حضرت محمد ﷺ پر نازل ہوا۔",
                summaryAr = "كلام الله المنزل على نبيه محمد ﷺ المعجز بلفظه المتعبد بتلاوته والمحفوظ من التحريف.",
                summaryHi = "अल्लाह की वह वाणी जो 23 वर्षों में पैगंबर मुहम्मद ﷺ पर अरबी भाषा में अवतरित हुई और आज भी सुरक्षित है।",
                bodyEn = "The Quran is not merely a biography or collection of human essays; Muslims believe it is the direct Word of Allah revealed in Arabic through Angel Jibreel over 23 years. It consists of 114 chapters (Surahs) organized for study and recitation.\n\nSince its revelation, millions of believers (Huffaz) have memorized its Arabic text word-for-word alongside written manuscripts. Beginners are warmly encouraged to read translations in their own language while gradually learning the Arabic script.",
                bodyUr = "قرآن مجید کسی انسان کی تصنیف نہیں بلکہ اللہ تعالیٰ کی آخری اور محفوظ کتاب ہے جو جبرائیل علیہ السلام کے ذریعے 23 سال میں نازل ہوئی۔ اس میں 114 سورتیں اور 30 پارے ہیں۔\n\nنزول سے لے کر آج تک کروڑوں حفاظ کے سینوں اور مصاحف میں یہ کتاب حرف بہ حرف محفوظ ہے۔ نئے سیکھنے والے اپنی زبان میں ترجمہ پڑھ کر سمجھ سکتے ہیں اور ساتھ ساتھ عربی قراءت سیکھ سکتے ہیں۔",
                bodyAr = "القرآن الكريم هو كلام الله الذي نزل به الروح الأمين جبريل على قلب النبي محمد ﷺ خلال ٢٣ عاماً. يتكون من ١١٤ سورة موزعة في ٣٠ جزءاً.\n\nتكفل الله بحفظه عبر الأجيال في الصدور والسطور، ويُثاب القارئ على تعلّمه وتلاوته ولو لم يتقنه بعد كما ورد في الحديث الصحيح.",
                bodyHi = "क़ुरआन किसी इंसान की लिखी पुस्तक नहीं, बल्कि अल्लाह का प्रत्यक्ष संदेश है जो 23 वर्षों में फ़रिश्ते जिब्रील के माध्यम से उतरा। इसमें 114 अध्याय (सूरह) हैं।\n\nलाखों लोगों ने इसे शब्द-दर-शब्द याद (हिफ़्ज़) रखा है। शुरुआती सीखने वाले अपनी भाषा में इसका अनुवाद पढ़कर अर्थ समझ सकते हैं और धीरे-धीरे अरबी पढ़ना सीख सकते हैं।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Hijr (15:9)", "Promise of preservation"),
                    IslamicReference("Sahih Muslim", "Hadith 798 / Aisha", "Those who struggle reading Arabic carefully earn double reward")
                )
            ),
            AcademyLesson(
                id = "s1_l5",
                stageNumber = 1,
                lessonNumber = 5,
                titleEn = "5. Entering Islam (The Shahadah)",
                titleUr = "5. دائرہ اسلام میں داخلہ: کلمہ شہادت",
                titleAr = "٥. الدخول في الإسلام: الشهادتان",
                titleHi = "5. इस्लाम में प्रवेश: शहादा (कलमा)",
                summaryEn = "Sincere declaration of faith—no intermediaries, fees, or complicated ceremonies required.",
                summaryUr = "سچے دل سے اللہ کی وحدانیت اور محمد ﷺ کی رسالت کی گواہی دینا—بغیر کسی رسمی رکاوٹ کے۔",
                summaryAr = "النطق بالشهادتين عن يقين وإخلاص من غير تكلف أو رسوم.",
                summaryHi = "सच्चे दिल से अल्लाह के एक होने और मुहम्मद ﷺ के पैगंबर होने की गवाही देना।",
                bodyEn = "Becoming a Muslim requires only sincere belief in the heart and testifying with the tongue:\n\"Ash-hadu an la ilaha illallah, wa ash-hadu anna Muhammadan 'abduhu wa rasuluhu\"\n(I bear witness that there is none worthy of worship except Allah, and I bear witness that Muhammad is His servant and Messenger).\n\nUpon entering Islam sincerely, all past sins are wiped clean like a fresh page (Sahih Muslim 121). You do not need to change an honorable birth name unless it contradicts Islamic monotheism.",
                bodyUr = "اسلام قبول کرنے کے لیے کسی پیچیدہ رسم یا فیس کی ضرورت نہیں، بس دل کے یقین کے ساتھ زبان سے کلمہ شہادت کا اقرار کافی ہے:\n«أَشْهَدُ أَنْ لَا إِلَٰهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ»\n\nسچے دل سے اسلام لانے پر پچھلے تمام گناہ معاف ہو جاتے ہیں اور زندگی کا ایک نیا پاک صفحہ کھل جاتا ہے (صحیح مسلم: 121)۔ اپنا باعزت نام بدلنا ضروری نہیں جب تک اس کا معنی شرکیہ یا برا نہ ہو۔",
                bodyAr = "الدخول في الإسلام يسير قائم على القناعة القلبية والنطق بالشهادتين: 'أشهد أن لا إله إلا الله وأشهد أن محمداً عبده ورسوله'.\n\nالإسلام يهدم ما كان قبله من الذنوب (صحيح مسلم: ١٢١)، ولا يُشترط تغيير الاسم ما لم يحمل معنىً محرماً.",
                bodyHi = "इस्लाम स्वीकार करने के लिए किसी जटिल कर्मकांड की आवश्यकता नहीं। दिल की सच्चाई से जुबान से शहादा पढ़ना ही प्रवेश है: 'अश-हदु अन ला इलाहा इल्लल्लाह, व अश-हदु अन्ना मुहम्मदन अब्दुहु व रसूलुहु' (मैं गवाही देता हूँ कि अल्लाह के सिवा कोई पूज्य नहीं और मुहम्मद ﷺ उसके बंदे और पैगंबर हैं)।\n\nसच्चे ईमान पर पिछले सारे गुनाह माफ़ हो जाते हैं (सहीह मुस्लिम: 121)।",
                recitations = listOf(
                    PrayerStepRecitation(
                        labelEn = "Declaration of Faith (Shahadah)",
                        labelUr = "کلمہ شہادت",
                        labelAr = "نطق الشهادتين",
                        labelHi = "शहादा (ईमान की गवाही)",
                        arabic = "أَشْهَدُ أَنْ لَا إِلَٰهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا رَسُولُ اللَّهِ",
                        transliteration = "Ash-hadu an la ilaha illallah, wa ash-hadu anna Muhammadan Rasul-ullah",
                        translationEn = "I bear witness that there is no deity worthy of worship except Allah, and I bear witness that Muhammad is the Messenger of Allah.",
                        translationUr = "میں گواہی دیتا ہوں کہ اللہ کے سوا کوئی معبود نہیں اور میں گواہی دیتا ہوں کہ محمد ﷺ اللہ کے رسول ہیں۔",
                        translationAr = "أقرّ وأشهد بأن لا معبود بحق إلا الله، وأن محمداً رسول الله ﷺ.",
                        translationHi = "मैं गवाही देता हूँ कि अल्लाह के सिवा कोई इबादत के लायक़ नहीं, और मुहम्मद ﷺ अल्लाह के रसूल हैं।",
                        explanationEn = "Affirms exclusive worship of Allah and following Prophet Muhammad ﷺ as His final messenger.",
                        explanationUr = "اس میں اللہ کی خالص بندگی اور رسول اللہ ﷺ کی رسالت کی تصدیق کا اقرار ہے۔",
                        explanationAr = "تتضمن إفراد الله بالعبادة والالتزام بهدي النبي ﷺ.",
                        explanationHi = "यह केवल अल्लाह की इबादत करने और नबी ﷺ के बताए रास्ते पर चलने का संकल्प है।"
                    )
                ),
                references = listOf(
                    IslamicReference("Sahih Muslim", "Hadith 121", "Islam wipes away previous sins"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 8", "Islam is built upon five pillars")
                )
            ),
            AcademyLesson(
                id = "s1_l6",
                stageNumber = 1,
                lessonNumber = 6,
                titleEn = "6. First Priorities for a New Muslim",
                titleUr = "6. نئے مسلمان کی ابتدائی عملی ترجیحات",
                titleAr = "٦. أولويات المسلم الجديد",
                titleHi = "6. नए मुसलमान के लिए पहली प्राथमिकताएं",
                summaryEn = "Grow steadily without burnout: focus on faith, daily prayer, kindness, and supportive community.",
                summaryUr = "جلد بازی یا گھبراہٹ کے بجائے تدریج سے آگے بڑھیں: عقیدہ، نماز، اخلاق اور اچھا ماحول۔",
                summaryAr = "التدرج الهادئ: ترسيخ التوحيد، وتعلّم الطهارة والصلاة، وبناء صحبة صالحة.",
                summaryHi = "जल्दबाज़ी या दबाव के बिना धीरे-धीरे आगे बढ़ें: ईमान, वुज़ू-नमाज़, अच्छा व्यवहार और नेक संगति।",
                bodyEn = "Remember the Quran itself was revealed gradually over 23 years so hearts could absorb guidance gently. Do not feel overwhelmed if you cannot memorize Arabic words in your first week.\n\nPractical 4-Step First Month Plan:\n1. Learn Wudu and the 5 daily prayers step-by-step (Stage 2 & 3).\n2. Speak to Allah daily in Dua using your own native language.\n3. Transition food, habits, and friendships gracefully at a sustainable pace.\n4. Stay connected with kind, knowledgeable community members.",
                bodyUr = "یاد رکھیں قرآن مجید بھی 23 برس میں تدریج کے ساتھ نازل ہوا تاکہ دل اطمینان سے دین کو قبول کریں۔ پہلے ہی ہفتے میں سب کچھ یاد نہ ہونے پر مایوس نہ ہوں۔\n\nپہلے مہینے کے 4 آسان قدم:\n1. وضو اور پانچ وقت کی نماز آہستہ آہستہ سیکھیں۔\n2. اللہ سے اپنی زبان میں روزانہ دعا مانگیں۔\n3. حلال و حرام کے معاملات میں بتدریج بہتری لائیں۔\n4. بااخلاق اور خیر خواہ اہلِ علم یا دوستوں سے جڑے رہیں۔",
                bodyAr = "تذكّر أن القرآن نزل منجماً على ٢٣ عاماً تدرجاً بالقلوب. لا ترهق نفسك في أسبوعك الأول:\n١. تعلّم الوضوء والصلوات الخمس خطوةً بخطوة.\n٢. أكثر من الدعاء بلغتك.\n٣. تدرّج في تصحيح العادات والبيئة برفق.\n٤. ابحث عن صحبة صالحة ومصادر علمية موثوقة.",
                bodyHi = "याद रखें क़ुरआन भी 23 वर्षों में धीरे-धीरे उतरा। पहले सप्ताह में सब कुछ न याद होने पर घबराएं नहीं।\n\nपहले महीने के 4 आसान कदम:\n1. वुज़ू और पाँच समय की नमाज़ चरण-दर-चरण सीखें।\n2. अपनी भाषा में रोज़ अल्लाह से दुआ करें।\n3. खान-पान और आदतों में समझदारी से सुधार लाएं।\n4. अच्छे और मददगार लोगों से जुड़े रहें।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 39", "Religion is ease; practice with moderation and steadiness")
                )
            )
        )
    )

    val stage2 = AcademyStage(
        stageNumber = 2,
        id = "stage_2",
        titleEn = "Stage 2: Purification & Cleanliness (Taharah)",
        titleUr = "مرحلہ 2: طہارت و پاکیزگی کے احکام",
        titleAr = "المرحلة ٢: الطهارة والنظافة",
        titleHi = "चरण 2: पवित्रता और स्वच्छता (तहारत)",
        subtitleEn = "Wudu, Ghusl, Istinja, Tayammum & clean prayer",
        subtitleUr = "استنجا، وضو، غسل، تیمم اور موزوں پر مسح کی مکمل رہنمائی",
        subtitleAr = "الاستنجاء، الوضوء، الغسل، التيمم والمسح على الخفين",
        subtitleHi = "इस्तिंजा, वुज़ू, ग़ुस्ल, तयम्मुम और मोज़ों पर मसह",
        overviewEn = "Learn physical cleanliness and spiritual readiness before standing in prayer, with clear steps and honest notes on recognized Fiqh differences.",
        overviewUr = "نماز سے پہلے ظاہری و باطنی پاکیزگی کے طریقے، مستند دلائل اور فقہی مذاہب کے معتبر اختلافات کی وضاحت کے ساتھ سیکھیں۔",
        overviewAr = "تعلّم أحكام النظافة والطهارة الشرعية قبل الصلاة بخطوات واضحة مع بيان اختلافات المذاهب الفقهية بأمانة.",
        overviewHi = "नमाज़ से पहले शरीर, कपड़े और जगह की शुद्धता के सही नियम और मान्यता प्राप्त फ़िक़ही मतभेदों की जानकारी सीखें।",
        interactiveToolType = StageToolType.NONE,
        lessons = listOf(
            AcademyLesson(
                id = "s2_l1",
                stageNumber = 2,
                lessonNumber = 1,
                titleEn = "1. Meaning of Taharah & Cleanliness for Prayer",
                titleUr = "1. طہارت کا مفہوم اور نماز کی شرائطِ پاکیزگی",
                titleAr = "١. مفهوم الطهارة وشروط نظافة الصلاة",
                titleHi = "1. तहारत (पवित्रता) का अर्थ और नमाज़ की शुद्धता",
                summaryEn = "Purification covers three cleanings: body, clothing, and prayer place—plus ritual purity (Wudu/Ghusl).",
                summaryUr = "طہارت میں جسم، کپڑوں اور نماز کی جگہ کی صفائی کے ساتھ وضو یا غسل سے حکمی پاکیزگی شامل ہے۔",
                summaryAr = "تشمل الطهارة طهارة البدن والثوب والمكان من النجاسة، والطهارة الحكمية بالوضوء والغسل.",
                summaryHi = "तहारत में शरीर, कपड़ों और नमाज़ की जगह की सफ़ाई के साथ वुज़ू या ग़ुस्ल शामिल है।",
                bodyEn = "Islam places immense importance on cleanliness—Prophet Muhammad ﷺ called purity 'half of faith' (Sahih Muslim 223). Before prayer, three things must be free of physical impurities (urine, stool, flowing blood, alcohol, dog saliva, etc.): your body, your clothes, and the surface where you pray.\n\nIf you are unsure whether something is impure, Islamic law treats things as pure by default unless clear evidence (smell, color, stain) proves impurity. Do not fall into obsessive doubts (Waswasah).",
                bodyUr = "نبی کریم ﷺ نے پاکیزگی کو ایمان کا نصف قرار دیا ہے (صحیح مسلم: 223)۔ نماز سے پہلے تین چیزوں کا پاک ہونا ضروری ہے: بدن، لباس اور سجدے کی جگہ۔\n\nاصول یہ ہے کہ ہر چیز اصل میں پاک ہے جب تک نجاست (پیشاب، پاخانہ، بہتا خون وغیرہ) کا یقین نہ ہو۔ محض شک یا وہم (وسوسہ) کی وجہ سے پاکی مت توڑیں۔",
                bodyAr = "جعل النبي ﷺ الطهور شطر الإيمان (صحيح مسلم: ٢٢٣). يجب قبل الصلاة التأكد من طهارة البدن والثوب ومكان الصلاة من النجاسات.\n\nالأصل في الأشياء الطهارة ولا يزول اليقين بالشك، فينبغي تجنب الوسوسة.",
                bodyHi = "पैगंबर ﷺ ने पवित्रता को आधा ईमान बताया है (सहीह मुस्लिम: 223)। नमाज़ से पहले शरीर, कपड़े और नमाज़ की जगह का पाक होना ज़रूरी है।\n\nबुनियादी नियम यह है कि जब तक गंदगी का पक्का सबूत न हो, हर चीज़ पाक मानी जाती है। बेवजह के शक (वस्वसा) से बचें।",
                references = listOf(
                    IslamicReference("Sahih Muslim", "Hadith 223", "Purity is half of faith"),
                    IslamicReference("Holy Quran", "Surah Al-Muddaththir (74:4)", "Purify your garments")
                )
            ),
            AcademyLesson(
                id = "s2_l2",
                stageNumber = 2,
                lessonNumber = 2,
                titleEn = "2. Istinja & Restroom Etiquette",
                titleUr = "2. استنجا اور قضائے حاجت کے آداب",
                titleAr = "٢. الاستنجاء وآداب قضاء الحاجة",
                titleHi = "2. इस्तिंजा और शौचालय के शिष्टाचार",
                summaryEn = "Practical hygiene after using the restroom so clothes and skin stay clean for prayer.",
                summaryUr = "رفع حاجت کے بعد پانی یا پاک ٹشو سے صفائی تاکہ کپڑے اور جسم نماز کے لیے پاک رہیں۔",
                summaryAr = "التنظيف بالماء (الاستنجاء) أو بالمناديل الطاهرة (الاستجمار) باليد اليسرى وتجنب الرذاذ.",
                summaryHi = "शौच के बाद बाएँ हाथ से पानी (इस्तिंजा) या साफ़ टिश्यू से अच्छी तरह सफ़ाई करने का तरीक़ा।",
                bodyEn = "After using the toilet:\n1. Use the left hand for cleansing—never the right hand.\n2. Washing with clean water (Istinja) is best; if water is unavailable, clean toilet paper/tissues used an odd number of times (at least three wipes until clean) suffices according to Sunnah (Istijmar).\n3. Guard carefully against urine splashes on skin or trousers.\n4. Wash hands well with soap and water afterward.",
                bodyUr = "قضائے حاجت کے بعد پاکیزگی کا طریقہ:\n1. صفائی کے لیے ہمیشہ بایاں ہاتھ استعمال کریں۔\n2. پانی سے دھونا (استنجا) سب سے افضل ہے؛ اگر پانی نہ ہو تو پاک ٹشو سے کم از کم تین بار صاف کر لینا (استجمار) کافی ہے۔\n3. پیشاب کی چھینٹوں سے بدن اور کپڑوں کو بچانے کا خاص اہتمام کریں۔\n4. آخر میں ہاتھ صابن اور پانی سے اچھی طرح دھوئیں۔",
                bodyAr = "١. استعمال اليد اليسرى في التنظيف.\n٢. التطهر بالماء الطهور (الاستنجاء)، أو بالمناديل الجافة الطاهرة ثلاث مسحات فأكثر حتى الإنقاء (الاستجمار).\n٣. الاحتراز من رذاذ البول على الملابس أو البدن.\n٤. غسل اليدين جيداً بالماء والصابون بعد الانتهاء.",
                bodyHi = "शौचालय के बाद सफ़ाई के नियम:\n1. सफ़ाई के लिए हमेशा बाएँ (उल्टे) हाथ का उपयोग करें।\n2. पानी से धोना (इस्तिंजा) सबसे उत्तम है; पानी न हो तो साफ़ टिश्यू पेपर से कम से कम तीन बार (या पूरी तरह साफ़ होने तक) पोंछ लें।\n3. कपड़ों पर छींटें न पड़ने दें और अंत में साबुन-पानी से हाथ धोएं।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 154, 156"),
                    IslamicReference("Sahih Muslim", "Hadith 262, 267")
                )
            ),
            AcademyLesson(
                id = "s2_l3",
                stageNumber = 2,
                lessonNumber = 3,
                titleEn = "3. Complete Step-by-Step Wudu (Ablution)",
                titleUr = "3. وضو کا مکمل عملی طریقہ (قرآن و سنت کے مطابق)",
                titleAr = "٣. صفة الوضوء الكاملة خطوة بخطوة",
                titleHi = "3. वुज़ू करने का पूरा चरण-दर-चरण तरीक़ा",
                summaryEn = "Detailed washing sequence from Bismillah and hands to face, arms, wiping head/ears, and washing feet.",
                summaryUr = "بسم اللہ اور ہاتھ دھونے سے لے کر کلی، ناک، چہرہ، کہنیاں، سر کا مسح اور ٹخنوں تک پاؤں دھونے کی مکمل ترتیب۔",
                summaryAr = "خطوات الوضوء العملية من التسمية وغسل الكفين والمضمضة والاستنشاق والوجه واليدين ومسح الرأس إلى غسل الرجلين.",
                summaryHi = "बिस्मिल्लाह से लेकर हाथ, मुँह, नाक, चेहरा, कुहनियों तक हाथ, सिर का मसह और टखनों तक पैर धोने की पूरी विधि।",
                bodyEn = "Quran 5:6 establishes four obligatory (Fard) acts of Wudu: washing the face, washing forearms up to elbows, wiping the head, and washing feet up to ankles. Following the Sunnah of Prophet Muhammad ﷺ:\n1. Intend purification in the heart and say 'Bismillah'.\n2. Wash both hands up to wrists (3x).\n3. Rinse mouth (3x) and sniff water gently into nostrils and blow out with left hand (3x).\n4. Wash entire face from hairline to chin, ear to ear (3x).\n5. Wash right arm from fingertips to above elbow (3x), then left arm (3x).\n6. Wipe wet hands over head once (front to back and back to front) and wipe inside/back of ears.\n7. Wash right foot up to ankle including between toes (3x), then left foot (3x).\n8. Conclude with: 'Ash-hadu an la ilaha illallah... wa ash-hadu anna Muhammadan 'abduhu wa rasuluhu.'",
                bodyUr = "قرآن مجید (المائدہ 5:6) میں وضو کے چار بنیادی فرائض بیان ہوئے ہیں: چہرہ دھونا، کہنیوں سمیت ہاتھ دھونا، سر کا مسح کرنا اور ٹخنوں سمیت پاؤں دھونا۔ مسنون مکمل ترتیب:\n1. دل میں نیت کریں اور 'بسم اللہ' پڑھیں۔\n2. دونوں ہاتھ گٹوں تک تین بار دھوئیں۔\n3. تین بار کلی کریں اور تین بار ناک میں پانی چڑھا کر بائیں ہاتھ سے صاف کریں۔\n4. پورا چہرہ پیشانی کے بالوں سے ٹھوڑی کے نیچے تک اور ایک کان سے دوسرے کان تک تین بار دھوئیں۔\n5. پہلے دایاں پھر بایاں ہاتھ کہنیوں سمیت تین تین بار دھوئیں۔\n6. بھیگے ہاتھوں سے سر کا مسح کریں اور کانوں کا اندرونی و بیرونی حصہ صاف کریں۔\n7. پہلے دایاں پھر بایاں پاؤں ٹخنوں سمیت انگلیوں کے خلال کے ساتھ تین تین بار دھوئیں۔\n8. وضو کے بعد کلمہ شہادت پڑھیں۔",
                bodyAr = "حدّدت آية المائدة (٥:٦) فروض الوضوء الأربعة، وتفصل السنة النبوية صفته الكاملة:\n١. النية بالقلب والتسمية.\n٢. غسل الكفين ثلاثاً.\n٣. المضمضة والاستنشاق ثلاثاً والاستنثار باليسرى.\n٤. غسل الوجه كاملاً ثلاثاً.\n٥. غسل اليدين إلى المرفقين ثلاثاً بدءاً باليمين.\n٦. مسح الرأس والأذنين مرة واحدة.\n٧. غسل الرجلين إلى الكعبين ثلاثاً مع تخليل الأصابع.\n٨. قول: أشهد أن لا إله إلا الله وأشهد أن محمداً عبده ورسوله.",
                bodyHi = "क़ुरआन (5:6) में वुज़ू के 4 फ़र्ज़ हैं: चेहरा, कुहनियों तक हाथ, सिर का मसह और टखनों तक पैर। सुन्नत के अनुसार पूरा तरीक़ा:\n1. दिल में नीयत करें और 'बिस्मिल्लाह' कहें।\n2. दोनों हाथ कलाइयों तक 3 बार धोएं।\n3. 3 बार कुल्ली करें और 3 बार नाक में पानी डालकर बाएँ हाथ से साफ़ करें।\n4. पूरा चेहरा 3 बार धोएं।\n5. पहले दायाँ फिर बायाँ हाथ कुहनियों समेत 3-3 बार धोएं।\n6. गीले हाथों से पूरे सिर का मसह करें और कानों को साफ़ करें।\n7. पहले दायाँ फिर बायाँ पैर टखनों समेत उंगलियों के बीच धोते हुए 3-3 बार धोएं।\n8. अंत में शहादा पढ़कर दुआ करें।\n\nवुज़ू तोड़ने वाली बातें: पेशाब-पाखाना, हवा निकलना, गहरी नींद।",
                fiqhDifferencesEn = "Recognized Madhhab Differences:\n• Wiping the head: Hanafi holds wiping 1/4 of the head meets Fard; Shafi'i considers even a small portion valid; Maliki & Hanbali require wiping the entire head.\n• Order (Tartib) & Continuity: Obligatory in Shafi'i & Hanbali; Sunnah in Hanafi (Maliki requires continuity without intentional delay).\n• Invalidators: Bleeding/pus breaks Wudu in Hanafi & Hanbali (if excessive), not in Shafi'i/Maliki. Touching skin of a spouse of the opposite sex with desire or without barrier differs between Shafi'i, Maliki, Hanafi, and Hanbali—consult your teacher on the school you follow.",
                fiqhDifferencesUr = "فقہی مذاہب کے معتبر اختلافات:\n• سر کا مسح: حنفیہ کے نزدیک چوتھائی سر کا مسح فرض ہے؛ شوافع کے نزدیک تھوڑا حصہ کافی ہے؛ مالکیہ و حنابلہ پورے سر کا مسح واجب قرار دیتے ہیں (پورا مسح سب کے نزدیک افضل ہے)۔\n• ترتیب: شوافع و حنابلہ کے ہاں ترتیب فرض ہے، جبکہ حنفیہ کے ہاں سنتِ مؤکدہ ہے۔\n• نواقضِ وضو: بہنے والے خون سے حنفیہ اور حنابلہ (کثیر خون میں) کے نزدیک وضو ٹوٹتا ہے، شوافع و مالکیہ کے ہاں نہیں۔",
                fiqhDifferencesAr = "اختلافات المذاهب المعتبرة:\n• مسح الرأس: يُجزئ ربع الرأس عند الحنفية، وأدنى مسح عند الشافعية، ويجب تعميم الرأس عند المالكية والحنابلة (والأكمل التعميم).\n• الترتيب: ركن عند الشافعية والحنابلة، وسنة عند الحنفية.\n• نواقض الوضوء: ينقض الدم السائل عند الحنفية، ولا ينقض عند الشافعية والمالكية.",
                fiqhDifferencesHi = "मान्यता प्राप्त फ़िक़ही मतभेद:\n• सिर का मसह: हनफ़ी मत में एक-चौथाई सिर का मसह फ़र्ज़ है, शाफ़िई में कुछ हिस्सा काफ़ी है, जबकि मालिकी और हंबली मत में पूरे सिर का मसह फ़र्ज़ है (पूरा मसह करना सबसे बेहतर है)।\n• क्रम (तरतीब): शाफ़िई व हंबली में फ़र्ज़, हनफ़ी में सुन्नत।\n• ख़ून निकलने से हनफ़ी मत में वुज़ू टूटता है, शाफ़िई व मालिकी में नहीं।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Ma'idah (5:6)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 159 (Description of Prophet's Wudu by Uthman)")
                ),
                quizId = "q_s2_1"
            ),
            AcademyLesson(
                id = "s2_l4",
                stageNumber = 2,
                lessonNumber = 4,
                titleEn = "4. Ghusl (Major Ritual Bath)",
                titleUr = "4. غسلِ جنابت و طہارت کے فرائض اور مسنون طریقہ",
                titleAr = "٤. الغسل: موجباته وفرائضه وصفته المسنونة",
                titleHi = "4. ग़ुस्ल (बड़ा स्नान): कारण और सही तरीक़ा",
                summaryEn = "When major ritual purification (Ghusl) is required and how to perform it completely.",
                summaryUr = "غسل کب واجب ہوتا ہے اور مسنون طریقے سے مکمل غسل کیسے کیا جاتا ہے۔",
                summaryAr = "متى يجب الغسل، وما هي فروضه وسننه لتتم الطهارة الكبرى.",
                summaryHi = "ग़ुस्ल कब ज़रूरी होता है और पूरे शरीर पर पानी पहुँचाने का सही तरीक़ा।",
                bodyEn = "Ghusl becomes obligatory after sexual intercourse or emission (Janabah), completion of menstruation (Hayd), and post-childbirth bleeding (Nifas), and is recommended (Sunnah) upon accepting Islam and before Jumu'ah prayer.\n\nSunnah Method of Ghusl:\n1. Form intention in the heart and say Bismillah.\n2. Wash hands and clean private area.\n3. Perform a complete Wudu (some delay washing feet if standing where water collects).\n4. Pour water over the head 3x ensuring it reaches hair roots.\n5. Wash the right side of the body, then the left side, ensuring water reaches every part of the skin without leaving any dry spot.",
                bodyUr = "جنابت، احتلام یا حیض و نفاس سے فراغت پر غسل فرض ہوتا ہے، جبکہ قبولِ اسلام اور جمعہ کے دن غسل مسنون ہے۔\n\nغسل کا مسنون طریقہ:\n1. نیت کر کے بسم اللہ پڑھیں اور دونوں ہاتھ دھوئیں۔\n2. استنجا اور جسم پر لگی ناپاکی صاف کریں۔\n3. مکمل وضو کریں۔\n4. سر پر تین بار پانی بہائیں تاکہ بالوں کی جڑوں تک پہنچ جائے۔\n5. پھر دائیں اور بائیں جانب پورے جسم پر پانی بہائیں کہ بال برابر جگہ بھی خشک نہ رہے۔",
                bodyAr = "يجب الغسل من الجنابة وانقطاع الحيض والنفاس، ويُسن عند دخول الإسلام ولصلاة الجمعة.\n\nالصفة المسنونة: النية والتسمية، غسل اليدين والفرج، الوضوء الكامل، إفاضة الماء على الرأس ثلاثاً، ثم تعميم سائر الجسد بدءاً باليمين فالشّمال مع الدلك والتأكد من وصول الماء لكل موضع.",
                bodyHi = "जनाबत (सहवास या वीर्यपात), मासिक धर्म (हैज़) या प्रसव के रक्त के समाप्त होने पर ग़ुस्ल फ़र्ज़ होता है, और इस्लाम स्वीकार करने या जुमुआ के दिन सुन्नत है।\n\nग़ुस्ल का सुन्नत तरीक़ा:\n1. नीयत करके दोनों हाथ धोएं और शरीर की गंदगी साफ़ करें।\n2. पूरा वुज़ू करें।\n3. सिर पर तीन बार पानी डालें ताकि जड़ों तक पहुंचे।\n4. पहले दाहिने फिर बाएँ हिस्से सहित पूरे शरीर पर पानी बहाएं ताकि कोई जगह सूखी न रहे।",
                fiqhDifferencesEn = "School Differences on Ghusl:\n• Rinsing the mouth and nose: Obligatory (Fard) in Hanafi and Hanbali schools; strongly recommended Sunnah in Shafi'i and Maliki schools.\n• Rubbing the body (Dalk) is obligatory in the Maliki school.",
                fiqhDifferencesUr = "فقہی اختلاف: حنفیہ اور حنابلہ کے نزدیک غسل میں کلی کرنا اور ناک میں پانی ڈالنا فرض ہے؛ شوافع اور مالکیہ کے ہاں یہ سنت ہے۔ مالکیہ کے نزدیک جسم ملنا (دَلک) بھی واجب ہے۔",
                fiqhDifferencesAr = "اختلاف المذاهب: المضمضة والاستنشاق فرض في الغسل عند الحنفية والحنابلة، وسنة مؤكدة عند الشافعية والمالكية.",
                fiqhDifferencesHi = "फ़िक़ही मतभेद: हनफ़ी और हंबली मत में ग़ुस्ल में कुल्ली करना और नाक में पानी डालना फ़र्ज़ है, जबकि शाफ़िई और मालिकी मत में यह सुन्नत है।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Ma'idah (5:6)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 248 / Aisha")
                )
            ),
            AcademyLesson(
                id = "s2_l5",
                stageNumber = 2,
                lessonNumber = 5,
                titleEn = "5. Tayammum (Dry Ablution) & Wiping Over Socks",
                titleUr = "5. تیمم کا طریقہ اور موزوں (خفین) پر مسح",
                titleAr = "٥. التيمم والمسح على الخفين والجوربين",
                titleHi = "5. तयम्मुम और चमड़े/मोटे मोज़ों पर मसह",
                summaryEn = "Ease in Islam when water is absent/harmful, plus wiping over valid footwear.",
                summaryUr = "پانی نہ ملنے یا بیماری کی صورت میں تیمم کی سہولت اور مسح کے قواعد۔",
                summaryAr = "التيسير الشرعي بالتيمم عند فقد الماء أو الضرر، وضوابط المسح على الخفين.",
                summaryHi = "पानी न मिलने या बीमारी में तयम्मुम की सहूलत और मोज़ों पर मसह के नियम।",
                bodyEn = "Tayammum (Quran 5:6): If water is unavailable or using it would harm a wound/illness:\n1. Make intention and say Bismillah.\n2. Strike both palms gently once on clean earth, dust, stone, or natural clay.\n3. Blow off excess dust if needed, wipe over your entire face, and wipe the backs of your hands up to the wrists.\n\nWiping Over Khuffain (Leather/Thick Footwear): After putting them on while in full Wudu, residents may wipe wet fingers over the top of the footwear for 24 hours (travelers: 72 hours), instead of washing feet each Wudu (breaks if removed or if Ghusl becomes required).",
                bodyUr = "تیمم: جب پانی دستیاب نہ ہو یا بیماری کی وجہ سے نقصان دہ ہو:\n1. نیت کر کے پاک مٹی یا پتھر پر دونوں ہتھیلیاں ماریں۔\n2. زیادہ گرد ہو تو جھاڑ کر پورا چہرہ مسح کریں اور دونوں ہاتھوں پر کلائیوں تک مسح کریں۔\n\nموزوں پر مسح: باوضو ہو کر چمڑے یا موٹے مقررہ شرائط والے موزے پہن لیے جائیں تو مقیم 24 گھنٹے اور مسافر 72 گھنٹے تک پاؤں دھونے کے بجائے موزے کے اوپر بھیگے ہاتھ پھیر سکتا ہے (غسل فرض ہونے پر مسح ختم ہو جاتا ہے)۔",
                bodyAr = "التيمم (المائدة: ٦): عند فقد الماء أو العجز عن استعماله، يضرب المسلم بكفيه الصعيد الطاهر ضربةً واحدة، ويمسح بهما وجهه وكفيه.\n\nالمسح على الخفين: من لبسهما على طهارة كاملة جاز له المسح على ظاهرهما يوماً وليلة للمقيم، وثلاثة أيام بلياليهن للمسافر ما لم يوجب الغسل.",
                bodyHi = "तयम्मुम: पानी न होने या बीमारी में नुकसान होने पर:\n1. नीयत करके दोनों हथेलियाँ साफ़ मिट्टी या पत्थर पर मारें।\n2. पूरे चेहरे और कलाइयों तक दोनों हाथों पर फेर लें।\n\nमोज़ों पर मसह: वुज़ू की हालत में चमड़े या नियमों के अनुसार मोटे मोज़े पहनने पर घर में 24 घंटे और सफ़र में 72 घंटे तक पैर धोने की जगह ऊपरी हिस्से पर गीले हाथ से मसह किया जा सकता है।",
                fiqhDifferencesEn = "Madhhab Notes:\n• Cotton/thin socks: Hanafi permits wiping only over leather Khuff or thick opaque socks that stay upright without tying and protect the foot during walking (Jawrabayn); Hanbali and some contemporary scholars allow thick durable wool/cotton socks meeting opacity/coverage criteria.\n• Strikes in Tayammum: One strike is authentic in Bukhari/Muslim (Hanbali); Hanafi & Shafi'i prescribe two strikes and wiping up to elbows.",
                fiqhDifferencesUr = "فقہی اختلافات: عام باریک سوتی جرابوں پر مسح جائز نہیں؛ چمڑے کے موزے یا ایسے موٹے، غیر شفاف اور مضبوط موزے جن سے پاؤں نہ جھلکیں ان پر حنابلہ و بعض فقہاء مسح کی اجازت دیتے ہیں۔ تیمم میں حنفیہ و شوافع دو ضربیں اور کہنیوں تک مسح کہتے ہیں، جبکہ حنابلہ صحیح حدیث کے مطابق ایک ضرب اور کلائیوں تک مسح کو کافی قرار دیتے ہیں۔",
                fiqhDifferencesAr = "اختلافات المذاهب: التيمم ضربة واحدة للوجه والكفين عند الحنابلة، وضربتان إلى المرفقين عند الحنفية والشافعية. أما الجورب غير الجلدي فيُشترط فيه عند الجمهور أن يكون ثخيناً ساتراً يمكن تتابع المشي فيه.",
                fiqhDifferencesHi = "फ़िक़ही मतभेद: पतले सूती मोज़ों पर मसह जायज़ नहीं। हनफ़ी व शाफ़िई मत में तयम्मुम में दो बार मिट्टी पर हाथ मारकर कुहनियों तक मसह किया जाता है, जबकि हंबली मत में सहीह हदीस के अनुसार एक बार हाथ मारकर कलाइयों तक काफ़ी है।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 338 (Tayammum)"),
                    IslamicReference("Sahih Muslim", "Hadith 276 (Duration of wiping over Khuffain)")
                )
            )
        )
    )

    val stage3 = AcademyStage(
        stageNumber = 3,
        id = "stage_3",
        titleEn = "Stage 3: Complete Salah (Prayer) Course",
        titleUr = "مرحلہ 3: مکمل نماز کورس (ابتدا سے سلام تک)",
        titleAr = "المرحلة ٣: دورة الصلاة الكاملة",
        titleHi = "चरण 3: संपूर्ण नमाज़ कोर्स (तकबीर से सलाम तक)",
        subtitleEn = "Daily prayers, exact steps, Arabic words & meanings",
        subtitleUr = "پنج وقتہ نماز کے اوقات، ہر رکن، عربی کلمات، ترجمہ اور تشریح",
        subtitleAr = "الصلوات الخمس، الأركان، الأذكار العربية وترجمتها وشرحها",
        subtitleHi = "पाँचों नमाज़ें, हर चरण, अरबी पाठ, अर्थ और विवरण",
        overviewEn = "Learn every single posture and recitation of Salah—with authentic Arabic text, transliteration, translation, meaning, and transparent Madhhab notes.",
        overviewUr = "نماز کی ہر حرکت اور ہر دعا (تکبیر، ثنا، فاتحہ، رکوع، سجدہ، جلسہ، تشہد، درود اور سلام) عربی، ترجمے اور تفہیم کے ساتھ سیکھیں۔",
        overviewAr = "تعلّم أفعال الصلاة وأقوالها بدقة مع النصوص العربية والترجمة والشرح وبيان الفروق المذهبية المعتبرة.",
        overviewHi = "नमाज़ के हर कदम और पढ़ी जाने वाली दुआओं को सही अरबी, उच्चारण, अनुवाद और अर्थ के साथ सीखें।",
        interactiveToolType = StageToolType.NONE,
        lessons = listOf(
            AcademyLesson(
                id = "s3_l1",
                stageNumber = 3,
                lessonNumber = 1,
                titleEn = "1. The Five Daily Prayers & Rak'ah Structure",
                titleUr = "1. پانچ وقت کی نمازیں اور رکعات کا جدول",
                titleAr = "١. الصلوات الخمس وعدد ركعاتها وأوقاتها",
                titleHi = "1. पाँच समय की नमाज़ें और रकअतों की संख्या",
                summaryEn = "Obligatory (Fard) and Sunnah Rak'ahs for Fajr, Dhuhr, Asr, Maghrib, Isha, and Witr.",
                summaryUr = "فجر، ظہر، عصر، مغرب، عشاء اور وتر کی فرض و سنت رکعتوں کی آسان ترتیب۔",
                summaryAr = "جدول الصلوات المفروضة والسنن الرواتب والوتر.",
                summaryHi = "फ़ज्र, ज़ुहर, अस्र, मग़रिब, इशा और वित्र की फ़र्ज़ व सुन्नत रकअतें।",
                bodyEn = "Muslims pray five obligatory prayers daily—like five peaceful checkpoints connecting your soul back to Allah:\n• Fajr (Dawn before sunrise): 2 Sunnah before + 2 Fard\n• Dhuhr (Midday after sun passes zenith): 4 Sunnah before + 4 Fard + 2 Sunnah after\n• Asr (Late afternoon): 4 Fard (optional Sunnah before)\n• Maghrib (Right after sunset): 3 Fard + 2 Sunnah after\n• Isha (Night): 4 Fard + 2 Sunnah after + Witr (1 or 3 Rak'ahs; Witr is Wajib in Hanafi and Sunnah Mu'akkadah in others).\n\nIf you are brand new, prioritize the Fard (obligatory) Rak'ahs consistently first, then build your Sunnah routine gradually.",
                bodyUr = "روزانہ پانچ فرض نمازیں مومن کا اللہ سے براہِ راست رابطہ ہیں:\n• فجر (صبح صادق تا طلوعِ آفتاب): 2 سنت مؤکدہ + 2 فرض\n• ظہر (زوال کے بعد): 4 سنت + 4 فرض + 2 سنت\n• عصر (دوپہر کے بعد): 4 فرض (پہلے غیر مؤکدہ سنتیں مستحب)\n• مغرب (غروب آفتاب کے فوراً بعد): 3 فرض + 2 سنت\n• عشاء (رات): 4 فرض + 2 سنت + وتر (حنفیہ کے ہاں 3 وتر واجب ہیں، جبکہ دیگر مذاہب میں 1 یا 3 سنت مؤکدہ)۔",
                bodyAr = "الصلوات المفروضة خمس في اليوم والليلة:\n• الفجر: ركعتان فرضاً (وقبلهما ركعتا سنة الفجر).\n• الظهر: ٤ فرضاً (وقبلها ٤ سنة وبعدها ٢).\n• العصر: ٤ فرضاً.\n• المغرب: ٣ فرضاً (وبعدها ٢ سنة).\n• العشاء: ٤ فرضاً (وبعدها ٢ سنة ثم صلاة الوتر).",
                bodyHi = "प्रतिदिन पाँच फ़र्ज़ नमाज़ें:\n• फ़ज्र (सूर्योदय से पहले): 2 सुन्नत + 2 फ़र्ज़\n• ज़ुहर (दोपहर ढलने पर): 4 सुन्नत + 4 फ़र्ज़ + 2 सुन्नत\n• अस्र (तीसरा पहर): 4 फ़र्ज़\n• मग़रिब (सूर्यास्त के तुरंत बाद): 3 फ़र्ज़ + 2 सुन्नत\n• इशा (रात): 4 फ़र्ज़ + 2 सुन्नत + वित्र (हनफ़ी में 3 वाजिब, अन्य में 1 या 3 सुन्नत मुअक्कदा)।\n\nशुरुआत में पाँचों फ़र्ज़ नमाज़ों की पाबंदी करें, फिर धीरे-धीरे सुन्नतें जोड़ें।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 528"),
                    IslamicReference("Sahih Muslim", "Hadith 728 (Rawatib Sunnahs)")
                )
            ),
            AcademyLesson(
                id = "s3_l2",
                stageNumber = 3,
                lessonNumber = 2,
                titleEn = "2. Standing, Qiblah, Intention, Takbeer & Thana",
                titleUr = "2. قبلہ رخ، نیت، تکبیرِ تحریمہ اور ثنا",
                titleAr = "٢. استقبال القبلة والنية وتكبيرة الإحرام ودعاء الاستفتاح",
                titleHi = "2. क़िबला, नीयत, तकबीर-ए-तहरीमा और सना",
                summaryEn = "How to begin prayer calmly with Allahu Akbar and the opening praise (Thana).",
                summaryUr = "قبلہ رخ کھڑے ہو کر نیت، تکبیرِ تحریمہ اور ثنا (سبحانک اللھم) پڑھنے کا طریقہ اور مفہوم۔",
                summaryAr = "كيفية الدخول في الصلاة بالنية والتكبير وقراءة الاستفتاح مع معانيها.",
                summaryHi = "क़िबला की ओर मुँह करके नीयत, अल्लाहु अकबर कहना और शुरुआती दुआ (सना) सीखना।",
                bodyEn = "1. Face Qiblah (towards Makkah) in Wudu, covering Awrah modestly.\n2. Intention (Niyyah): Firm conscious awareness in your heart of which prayer you are offering (e.g., 2 Fard of Fajr). Saying it aloud is not required.\n3. Raise hands near earlobes or shoulders and say 'Allahu Akbar' (Allah is the Greatest). Now worldly speech and eating cease until Salam.\n4. Place right hand over left and recite opening praise (Thana) silently.",
                bodyUr = "1. باوضو ہو کر ستر ڈھانپے ہوئے قبلہ رخ کھڑے ہوں۔\n2. نیت: دل میں پکا ارادہ کریں کہ آپ کون سی نماز پڑھ رہے ہیں (زبان سے کہنا شرط نہیں)۔\n3. دونوں ہاتھ کانوں یا کندھوں کے برابر اٹھا کر 'اللہ اکبر' کہیں۔\n4. ہاتھ باندھ کر آہستہ آواز میں ثنا (سبحانک اللھم) اور تعوذ و تسمیہ پڑھیں۔",
                bodyAr = "١. استقبل القبلة متطهراً ساتراً للعورة.\n٢. انوِ بقلبك الصلاة التي تصليها.\n٣. ارفع يديك حذو المنكبين أو الأذنين وقل 'الله أكبر' (تكبيرة الإحرام).\n٤. ضع اليمنى على اليسرى واقرأ دعاء الاستفتاح سراً ثم الاستعاذة والبسملة.",
                bodyHi = "1. वुज़ू के साथ क़िबला की ओर मुँह करके खड़े हों।\n2. नीयत: दिल में तय करें कि कौन-सी नमाज़ पढ़ रहे हैं।\n3. हाथ कंधों या कानों तक उठाकर 'अल्लाहु अकबर' कहें।\n4. दायाँ हाथ बाएँ पर रखकर धीरे से सना और बिस्मिल्लाह पढ़ें।",
                fiqhDifferencesEn = "Hand placement in standing (Qiyam): Hanafi folds right hand over left below the navel (and women on chest); Shafi'i & Hanbali generally place hands above navel/below chest; Maliki commonly leaves hands by the sides (Sadl) or folds. Raised-hands height: earlobes or shoulder-level are both in authentic Hadith.",
                fiqhDifferencesUr = "قیام میں ہاتھ باندھنے کا اختلاف: حنفیہ کے ہاں مرد ناف کے نیچے اور خواتین سینے پر ہاتھ رکھتی ہیں؛ شوافع اور حنابلہ کے ہاں ناف سے اوپر؛ مالکیہ میں ہاتھ چھوڑنے (اِرسال) یا باندھنے دونوں کا رواج ہے۔",
                fiqhDifferencesAr = "اختلاف المذاهب: وضع اليدين تحت السرة (الحنفية)، وفوق السرة تحت الصدر (الشافعية والحنابلة)، أو الإرسال (المشهور عند المالكية)؛ وكلها هيئات واردة في الفقه الإسلامي.",
                fiqhDifferencesHi = "खड़े होने में हाथ बाँधने का मतभेद: हनफ़ी मत में पुरुष नाभि के नीचे (महिलाएं सीने पर), शाफ़िई व हंबली में नाभि के ऊपर/सीने के पास रखते हैं, जबकि मालिकी मत में हाथ नीचे छोड़ने (सदल) का तरीका भी प्रचलित है।",
                recitations = listOf(
                    PrayerStepRecitation(
                        labelEn = "Opening Takbeer (Takbeer-e-Tahreema)",
                        labelUr = "تکبیرِ تحریمہ",
                        labelAr = "تكبيرة الإحرام",
                        labelHi = "तकबीर-ए-तहरीमा",
                        arabic = "اللَّهُ أَكْبَرُ",
                        transliteration = "Allahu Akbar",
                        translationEn = "Allah is the Greatest.",
                        translationUr = "اللہ سب سے بڑا ہے۔",
                        translationAr = "الله أعظم وأجلّ من كل شيء.",
                        translationHi = "अल्लाह सबसे बड़ा है।",
                        explanationEn = "Reminds the heart that Allah is greater than all daily worries, money, or stress as you enter prayer.",
                        explanationUr = "اس تکبیر سے بندہ اعلان کرتا ہے کہ دنیا کی ہر فکر اور کام سے اللہ کی عظمت سب سے بڑی ہے۔",
                        explanationAr = "إعلان الدخول في مناجاة الخالق الذي هو أكبر من كل شواغل الدنيا.",
                        explanationHi = "यह दिल को याद दिलाता है कि दुनिया की हर चिंता से बड़ा केवल अल्लाह है।"
                    ),
                    PrayerStepRecitation(
                        labelEn = "Opening Supplication (Thana)",
                        labelUr = "دعائے استفتاح / ثنا",
                        labelAr = "دعاء الاستفتاح",
                        labelHi = "सना (प्रारंभिक स्तुति)",
                        arabic = "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَى جَدُّكَ، وَلَا إِلَهَ غَيْرُكَ",
                        transliteration = "Subhanaka Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruka",
                        translationEn = "Glory be to You, O Allah, and praise be to You; blessed is Your name, exalted is Your majesty, and there is no deity worthy of worship besides You.",
                        translationUr = "اے اللہ! تو ہر عیب سے پاک ہے اپنی تعریفوں سمیت، تیرا نام بابرکت ہے، تیری شان بہت بلند ہے اور تیرے سوا کوئی معبود نہیں۔",
                        translationAr = "تنزيهك يا الله مع الثناء عليك، عظمت بركة اسمك وارتفعت عظمتك ولا معبود بحق سواك.",
                        translationHi = "ऐ अल्लाह! तू अपनी प्रशंसा के साथ पवित्र है, तेरा नाम बरकत वाला है, तेरी शान सबसे ऊँची है और तेरे सिवा कोई पूज्य नहीं।",
                        explanationEn = "A humble greeting of praise and reverence before reciting the Quran (Sahih Muslim / Abu Dawud / Tirmidhi).",
                        explanationUr = "تلاوتِ قرآن شروع کرنے سے پہلے اللہ کی پاکی، حمد اور عظمت کا اعتراف۔",
                        explanationAr = "ثناء وتمجيد لله تعالى قبل الشروع في تلاوة القرآن.",
                        explanationHi = "क़ुरआन की तिलावत शुरू करने से पहले अल्लाह की महिमा और पवित्रता की स्तुति।"
                    )
                ),
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 757 (Prophet's Prayer)"),
                    IslamicReference("Sunan Abi Dawud", "Hadith 775-776 (Thana)")
                )
            ),
            AcademyLesson(
                id = "s3_l3",
                stageNumber = 3,
                lessonNumber = 3,
                titleEn = "3. Reciting Surah Al-Fatihah & Short Surah",
                titleUr = "3. سورۃ الفاتحہ اور قراءتِ قرآن (مکمل ترجمے سمیت)",
                titleAr = "٣. قراءة سورة الفاتحة وما تيسر من القرآن",
                titleHi = "3. सूरह अल-फ़ातिहा और छोटी सूरह का पाठ",
                summaryEn = "Every word of Surah Al-Fatihah with its spiritual dialogue between servant and Allah.",
                summaryUr = "نماز کی روح یعنی سورۃ الفاتحہ کی سات آیات، ان کا ترجمہ اور بندے کا رب سے مکالمہ۔",
                summaryAr = "تلاوة سورة الفاتحة ركن الصلاة ومعانيها بوصفها مناجاة بين العبد وربه.",
                summaryHi = "नमाज़ का सबसे मुख्य भाग सूरह अल-फ़ातिहा की सभी 7 आयतें, उच्चारण और अर्थ।",
                bodyEn = "In every Rak'ah, say 'A'udhu billahi minash-shaytanir-rajeem' (in Rak'ah 1) and 'Bismillahir-Rahmanir-Rahim', then recite Surah Al-Fatihah. In the first two Rak'ahs of prayer, follow Al-Fatihah with another short Surah (such as Al-Ikhlas or Al-Kawthar). Look at the floor/place of prostration with humility.",
                bodyUr = "پہلی رکعت میں تعوذ و تسمیہ کے بعد سورۃ الفاتحہ پڑھیں۔ پہلی دو رکعتوں میں الفاتحہ کے بعد کوئی اور سورت (مثلاً سورۃ الإخلاص یا الکوثر) ملائی جاتی ہے، جبکہ تیسری اور چوتھی فرض رکعت میں صرف الفاتحہ پڑھی جاتی ہے۔ نظر سجدے کی جگہ پر رکھیں۔",
                bodyAr = "تُقرأ الفاتحة في كل ركعة، وتُضم إليها سورة قصيرة في الركعتين الأوليين مع النظر إلى موضع السجود خشوعاً.",
                bodyHi = "हर रकअत में सूरह अल-फ़ातिहा पढ़ी जाती है। पहली दो रकअतों में इसके बाद एक छोटी सूरह (जैसे सूरह अल-इख़्लास) मिलाई जाती है। निगाह सजदे की जगह पर रखें।",
                fiqhDifferencesEn = "Reciting Al-Fatihah behind an Imam: In the Shafi'i school, the follower recites Al-Fatihah behind the Imam in all prayers. In the Hanafi school, the follower listens silently behind the Imam. In Maliki & Hanbali, the follower recites in silent prayers (Dhuhr/Asr) and listens in loud prayers.",
                fiqhDifferencesUr = "امام کے پیچھے قراءت: شوافع کے نزدیک مقتدی ہر نماز میں فاتحہ پڑھے گا؛ حنفیہ کے ہاں مقتدی خاموش رہ کر امام کی قراءت سنتا ہے؛ مالکیہ و حنابلہ کے ہاں سری نمازوں (ظہر، عصر) میں پڑھتا ہے اور جہری میں سنتا ہے۔",
                fiqhDifferencesAr = "قراءة الفاتحة خلف الإمام: واجبة عند الشافعية مطلقاً، وتكفي قراءة الإمام عند الحنفية، وتُقرأ في السرية دون الجهرية عند المالكية والحنابلة.",
                fiqhDifferencesHi = "इमाम के पीछे फ़ातिहा पढ़ना: शाफ़िई मत में मुक़्तदी हर नमाज़ में पढ़ता है; हनफ़ी मत में इमाम की क़िरात ही काफ़ी है; मालिकी व हंबली में धीमी नमाज़ों (ज़ुहर/अस्र) में पढ़ी जाती है।",
                recitations = listOf(
                    PrayerStepRecitation(
                        labelEn = "Surah Al-Fatihah (Complete Chapter 1)",
                        labelUr = "مکمل سورۃ الفاتحہ",
                        labelAr = "سورة الفاتحة كاملة",
                        labelHi = "संपूर्ण सूरह अल-फ़ातिहा",
                        arabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمَٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                        transliteration = "Bismillahir-Rahmanir-Rahim. Al-hamdu lillahi Rabbil-'alamin. Ar-Rahmanir-Rahim. Maliki yawmid-din. Iyyaka na'budu wa iyyaka nasta'in. Ihdinas-siratal-mustaqim. Siratal-ladhina an'amta 'alayhim ghayril-maghdubi 'alayhim wa lad-dallin.",
                        translationEn = "In the name of Allah, the Entirely Merciful, the Especially Merciful. All praise is due to Allah, Lord of the worlds. The Entirely Merciful, the Especially Merciful. Sovereign of the Day of Recompense. It is You we worship and You we ask for help. Guide us to the straight path—the path of those upon whom You have bestowed favor, not of those who have evoked anger or of those who are astray.",
                        translationUr = "شروع اللہ کے نام سے جو بڑا مہربان نہایت رحم والا ہے۔ سب تعریفیں اللہ کے لیے ہیں جو تمام جہانوں کا پالنے والا ہے۔ بڑا مہربان نہایت رحم والا۔ بدلے کے دن کا مالک۔ ہم صرف تیری ہی عبادت کرتے ہیں اور تجھ ہی سے مدد مانگتے ہیں۔ ہمیں سیدھے راستے پر چلا؛ ان لوگوں کے راستے پر جن پر تو نے انعام فرمایا، نہ کہ ان پر جن پر غضب ہوا اور نہ بھٹکے ہوؤں کے۔",
                        translationAr = "أم الكتاب والسبع المثاني: الثناء على الله، وإفراده بالعبادة والاستعانة، والدعاء بالهداية إلى الصراط المستقيم.",
                        translationHi = "अल्लाह के नाम से जो बड़ा मेहरबान, बहुत रहम वाला है। सब तारीफ़ें अल्लाह के लिए हैं जो सारे जहाँ का मालिक है। बड़ा मेहरबान, बहुत रहम वाला। बदले के दिन का मालिक। हम केवल तेरी ही इबादत करते हैं और तुझी से मदद माँगते हैं। हमें सीधा रास्ता दिखा—उन लोगों का रास्ता जिन पर तूने इनाम किया, न उनका जिन पर क्रोध हुआ और न गुमराह लोगों का।",
                        explanationEn = "In Sahih Muslim 395, Allah says: 'I have divided the prayer between Myself and My servant into two halves'—praise first, then asking for guidance.",
                        explanationUr = "صحیح مسلم (395) کی حدیثِ قدسی کے مطابق سورۃ الفاتحہ بندے اور اللہ کے درمیان براہِ راست مکالمہ ہے—پہلے حمد و ثنا اور پھر ہدایت کی دعا۔",
                        explanationAr = "في الحديث القدسي (صحيح مسلم: ٣٩٥): 'قسمت الصلاة بيني وبين عبدي نصفين'، نصفها ثناء ونصفها دعاء ومناجاة.",
                        explanationHi = "सहीह मुस्लिम (395) के अनुसार यह सूरह बंदे और अल्लाह के बीच सीधा संवाद है—पहले स्तुति, फिर सीधे रास्ते की दुआ।"
                    )
                ),
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Fatihah (1:1-7)"),
                    IslamicReference("Sahih Muslim", "Hadith 395 (Dialogue with Allah)")
                ),
                quizId = "q_s3_1"
            ),
            AcademyLesson(
                id = "s3_l4",
                stageNumber = 3,
                lessonNumber = 4,
                titleEn = "4. Ruku (Bowing) & Standing Upright (Qawmah)",
                titleUr = "4. رکوع اور قومہ (رکوع سے سیدھا کھڑا ہونا)",
                titleAr = "٤. الركوع والاعتدال منه",
                titleHi = "4. रुकू (झुकना) और क़ौमा (सीधे खड़े होना)",
                summaryEn = "Bowing with a straight back and knees held firmly, followed by rising calmly.",
                summaryUr = "کمر سیدھی رکھ کر گھٹنوں کو تھامتے ہوئے رکوع اور پھر اطمینان سے سیدھے کھڑے ہونے کا طریقہ۔",
                summaryAr = "كيفية الركوع مع استواء الظهر وأذكار التعظيم، ثم الرفع والاطمئنان.",
                summaryHi = "पीठ सीधी रखकर रुकू में झुकने और फिर शांत होकर सीधे खड़े होने के शब्द व अर्थ।",
                bodyEn = "1. Say 'Allahu Akbar' and bow forward, placing palms firmly on knees with fingers spread, keeping your back flat and level (head neither drooping nor lifted high).\n2. Say 'Subhana Rabbiyal 'Adheem' calmly at least 3 times.\n3. Rise back to full upright posture saying 'Sami'Allahu liman hamidah'.\n4. Once standing completely still (Tuma'ninah), say 'Rabbana wa lakal hamd'.",
                bodyUr = "1. 'اللہ اکبر' کہتے ہوئے رکوع میں جھکیں؛ دونوں ہاتھوں سے گھٹنے مضبوطی سے پکڑیں اور کمر بالکل سیدھی رکھیں۔\n2. اطمینان سے کم از کم تین بار 'سبحان ربی العظیم' پڑھیں۔\n3. 'سمع اللہ لمن حمدہ' کہتے ہوئے بالکل سیدھے کھڑے ہو جائیں (قومہ)۔\n4. سیدھے کھڑے ہو کر 'ربنا و لک الحمد' پڑھیں۔",
                bodyAr = "١. كبّر واركع واضعاً كفيك على ركبتيك مع استواء الظهر باطمئنان.\n٢. قل 'سبحان ربي العظيم' ثلاثاً.\n٣. ارفع رأسك قائلاً 'سمع الله لمن حمده'.\n٤. عند الاعتدال التام قل 'ربنا ولك الحمد'.",
                bodyHi = "1. 'अल्लाहु अकबर' कहते हुए झुकें, दोनों हाथों से घुटनों को पकड़ें और पीठ सीधी रखें।\n2. कम से कम तीन बार 'सुब्हाना रब्बियल अज़ीम' पढ़ें।\n3. 'समिअल्लाहु लिमन हमिदा' कहते हुए पूरी तरह सीधे खड़े हो जाएं।\n4. सीधे खड़े होकर 'रब्बना व लकल हम्द' कहें।",
                fiqhDifferencesEn = "Raising hands before/after Ruku (Raf'ul-Yadayn): Shafi'i and Hanbali raise hands before going into Ruku and upon rising from it (also narrated in Bukhari/Muslim); Hanafi only raises hands at the initial Takbeer-e-Tahreema (citing narrations of Ibn Mas'ud).",
                fiqhDifferencesUr = "رفع الیدین کا اختلاف: شوافع اور حنابلہ رکوع میں جاتے اور اٹھتے وقت ہاتھ اٹھاتے ہیں (بخاری و مسلم کی روایات کی بنا پر)؛ جبکہ حنفیہ صرف تکبیرِ تحریمہ پر ہاتھ اٹھاتے ہیں (حضرت ابن مسعودؓ کی روایت کی بنا پر)۔",
                fiqhDifferencesAr = "رفع اليدين عند الركوع: سنة عند الشافعية والحنابلة، ويكتفي الحنفية بالتكبير الأول، وكلٌّ مستند إلى مرويات صحيحة.",
                fiqhDifferencesHi = "रफ़-उल-यदैन (रुकू में जाते-उठते हाथ उठाना): शाफ़िई व हंबली मत में सुन्नत है, जबकि हनफ़ी मत में केवल पहली तकबीर में हाथ उठाए जाते हैं।",
                recitations = listOf(
                    PrayerStepRecitation(
                        labelEn = "In Ruku (Bowing)",
                        labelUr = "رکوع کی تسبیح",
                        labelAr = "تسبيح الركوع",
                        labelHi = "रुकू की तस्बीह",
                        arabic = "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
                        transliteration = "Subhana Rabbiyal-'Adheem",
                        translationEn = "Glory be to my Lord, the Supreme.",
                        translationUr = "میرا عظمت والا رب ہر عیب سے پاک ہے۔",
                        translationAr = "أنزّه ربي العظيم عن كل نقص.",
                        translationHi = "मेरा महान रब हर दोष से पवित्र है।",
                        explanationEn = "Bowing physically lowers our pride before the Supreme Creator.",
                        explanationUr = "جھک کر انسان اپنے عظیم رب کے سامنے عاجزی اور تعظیم کا اظہار کرتا ہے۔",
                        explanationAr = "خضوع الجسد مع تعظيم القلب لله العظيم.",
                        explanationHi = "झुककर हम अपने महान रब के सामने अपनी नम्रता प्रकट करते हैं।"
                    ),
                    PrayerStepRecitation(
                        labelEn = "Rising & Standing Straight (Qawmah)",
                        labelUr = "تسمیع اور تحمید (قومہ)",
                        labelAr = "التسميع والتحميد",
                        labelHi = "क़ौमा (सीधे खड़े होने के शब्द)",
                        arabic = "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ • رَبَّنَا وَلَكَ الْحَمْدُ",
                        transliteration = "Sami'Allahu liman hamidah • Rabbana wa lakal-hamd",
                        translationEn = "Allah hears whoever praises Him • Our Lord, to You belongs all praise.",
                        translationUr = "اللہ نے اس کی بات سن لی جس نے اس کی تعریف کی • اے ہمارے رب! سب تعریفیں تیرے ہی لیے ہیں۔",
                        translationAr = "استجاب الله لمن أثنى عليه • ربنا ولك جميع المحامد.",
                        translationHi = "अल्लाह ने उसकी पुकार सुन ली जिसने उसकी प्रशंसा की • ऐ हमारे रब! सारी प्रशंसा तेरे ही लिए है।",
                        explanationEn = "Assures the praying person that Allah hears every sincere praise and prayer.",
                        explanationUr = "بندے کو یقین دلاتا ہے کہ اللہ ہر تعریف اور مناجات کو سن رہا ہے۔",
                        explanationAr = "استشعار سماع الله لعبده وإجابته لثنائه.",
                        explanationHi = "यह विश्वास दिलाता है कि अल्लाह हमारी हर पुकार और प्रशंसा सुनता है।"
                    )
                ),
                references = listOf(
                    IslamicReference("Sahih Muslim", "Hadith 772 (Tasbeeh in Ruku & Sujood)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 789")
                )
            ),
            AcademyLesson(
                id = "s3_l5",
                stageNumber = 3,
                lessonNumber = 5,
                titleEn = "5. Sujood (Prostration) & Jalsah (Sitting)",
                titleUr = "5. سجدہ اور دونوں سجدوں کے درمیان بیٹھنا (جلسہ)",
                titleAr = "٥. السجود والجلوس بين السجدتين",
                titleHi = "5. सजदा और दो सजदों के बीच बैठना (जलसा)",
                summaryEn = "The closest moment to Allah: prostrating on seven bones and pausing in calm supplication.",
                summaryUr = "رب سے قریب ترین حالت (سجدہ) اور دو سجدوں کے درمیان اطمینان سے بیٹھنے کی دعا۔",
                summaryAr = "أقرب ما يكون العبد من ربه وهو ساجد، مع صفة الجلوس بين السجدتين ودعائه.",
                summaryHi = "बंदे का अल्लाह के सबसे क़रीब होने की अवस्था (सजदा) और दोनों सजदों के बीच बैठने की दुआ।",
                bodyEn = "Prophet Muhammad ﷺ taught: 'The closest a servant comes to his Lord is while he is in prostration, so increase supplication then' (Sahih Muslim 482).\n\nHow to Prostrate:\n1. Say 'Allahu Akbar' and descend into Sujood placing 7 body parts on the floor: forehead with nose, both palms, both knees, and toes of both feet pointing toward Qiblah.\n2. Keep elbows lifted away from the floor (not resting forearms flat like an animal).\n3. Say 'Subhana Rabbiyal A'la' calmly 3 times.\n4. Say 'Allahu Akbar', sit upright (left foot under you, right foot upright toes pointing Qiblah—Jalsah), say 'Rabbighfir li' (My Lord, forgive me), then perform the second Sajdah just like the first.",
                bodyUr = "نبی ﷺ نے فرمایا: 'بندہ اپنے رب کے سب سے زیادہ قریب سجدے کی حالت میں ہوتا ہے، پس اس میں خوب دعا مانگو' (صحیح مسلم: 482)۔\n\nسجدے کا طریقہ:\n1. 'اللہ اکبر' کہتے ہوئے 7 اعضاء زمین پر ٹکائیں: پیشانی اور ناک کی نوک، دونوں ہتھیلیاں، دونوں گھٹنے اور دونوں پاؤں کی انگلیاں۔\n2. کہنیاں زمین سے اوپر اٹھائے رکھیں۔\n3. کم از کم تین بار 'سبحان ربی الاعلیٰ' پڑھیں۔\n4. 'اللہ اکبر' کہہ کر دونوں سجدوں کے درمیان اطمینان سے بیٹھیں (جلسہ) اور 'رب اغفر لی' پڑھیں، پھر دوسرا سجدہ کریں۔",
                bodyAr = "قال ﷺ: 'أقرب ما يكون العبد من ربه وهو ساجد فأكثروا الدعاء' (صحيح مسلم: ٤٨٢).\n\nيسجد المصلي على سبعة أعضاء (الجبهة مع الأنف، الكفين، الركبتين، وأطراف القدمين) مع مجافاة الذراعين عن الأرض، ويقول 'سبحان ربي الأعلى' ثلاثاً، ثم يجلس مطمئناً بين السجدتين قائلاً 'رب اغفر لي'، ثم يسجد الثانية.",
                bodyHi = "पैगंबर ﷺ ने फ़रमाया: 'बंदा अपने रब के सबसे क़रीब सजदे की हालत में होता है' (सहीह मुस्लिम: 482)।\n\nसजदे का तरीक़ा:\n1. 'अल्लाहु अकबर' कहते हुए 7 अंग ज़मीन पर टिकाएं: माथा व नाक, दोनों हथेलियाँ, दोनों घुटने और दोनों पैरों की उंगलियाँ (क़िबला की ओर मुड़ी)।\n2. कुहनियाँ ज़मीन से ऊपर रखें और 3 बार 'सुब्हाना रब्बियल आला' कहें।\n3. फिर उठकर शांत बैठें और 'रब्बिग़फ़िर ली' कहें, फिर दूसरा सजदा करें।",
                recitations = listOf(
                    PrayerStepRecitation(
                        labelEn = "In Sujood (Prostration)",
                        labelUr = "سجدے کی تسبیح",
                        labelAr = "تسبيح السجود",
                        labelHi = "सजदे की तस्बीह",
                        arabic = "سُبْحَانَ رَبِّيَ الْأَعْلَى",
                        transliteration = "Subhana Rabbiyal-A'la",
                        translationEn = "Glory be to my Lord, the Most High.",
                        translationUr = "میرا بلند و برتر رب ہر عیب سے پاک ہے۔",
                        translationAr = "أنزّه ربي الأعلى فوق كل شيء عن كل نقص.",
                        translationHi = "मेरा सबसे ऊँचा रब हर दोष से पवित्र है।",
                        explanationEn = "Placing the highest part of the body (forehead) on the ground while affirming Allah is the Most High.",
                        explanationUr = "اپنی پیشانی زمین پر رکھ کر اس حقیقت کا اعتراف کہ بلندی صرف اللہ کے لیے ہے۔",
                        explanationAr = "وضع أشرف الأعضاء على الأرض اعترافاً بعلوّ الله المطلق.",
                        explanationHi = "अपने माथे को ज़मीन पर रखकर यह स्वीकार करना कि सबसे ऊँची शान केवल अल्लाह की है।"
                    ),
                    PrayerStepRecitation(
                        labelEn = "Sitting Between the Two Prostrations (Jalsah)",
                        labelUr = "دونوں سجدوں کے درمیان کی دعا",
                        labelAr = "الدعاء بين السجدتين",
                        labelHi = "दो सजदों के बीच की दुआ",
                        arabic = "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي",
                        transliteration = "Rabbighfir li, Rabbighfir li",
                        translationEn = "My Lord, forgive me; my Lord, forgive me.",
                        translationUr = "اے میرے رب! مجھے بخش دے، اے میرے رب! مجھے بخش دے۔",
                        translationAr = "يا ربي تجاوز عن ذنوبي واغفر لي.",
                        translationHi = "ऐ मेरे रब! मुझे क्षमा कर दे, ऐ मेरे रब! मुझे क्षमा कर दे।",
                        explanationEn = "A brief, heartfelt pause for forgiveness before bowing down again (Abu Dawud 874 / Ibn Majah 897).",
                        explanationUr = "دوسرے سجدے سے پہلے اپنے رب سے بخشش طلب کرنے کا خوبصورت وقفہ۔",
                        explanationAr = "وقفة خاشعة لطلب المغفرة بين السجدتين.",
                        explanationHi = "दूसरे सजदे से पहले अपने रब से माफ़ी माँगने का शांत क्षण।"
                    )
                ),
                references = listOf(
                    IslamicReference("Sahih Muslim", "Hadith 482, 490 (Seven bones in Sujood)"),
                    IslamicReference("Sunan Ibn Majah", "Hadith 897 (Rabbighfir li)")
                )
            ),
            AcademyLesson(
                id = "s3_l6",
                stageNumber = 3,
                lessonNumber = 6,
                titleEn = "6. Tashahhud, Durood Ibrahim, Final Dua & Salam",
                titleUr = "6. قعدہ: التحیات، درودِ ابراہیم، دعا اور سلام",
                titleAr = "٦. التشهد والصلاة الإبراهيمية والدعاء والتسليم",
                titleHi = "6. तशह्हुद (अत्तहिय्यात), दुरूद इब्राहीम, दुआ और सलाम",
                summaryEn = "Complete final sitting (Qa'dah) texts—every authentic Arabic word with meaning—and concluding with Salam.",
                summaryUr = "نماز کے آخری قعدے میں پڑھی جانے والی التحیات، درودِ ابراہیمی، سلام سے پہلے کی دعا اور سلام پھیرنے کا طریقہ۔",
                summaryAr = "نصوص الجلوس للتشهد الأول والأخير، والصلاة على النبي ﷺ، والدعاء قبل السلام، والتسليم.",
                summaryHi = "नमाज़ के अंत में बैठकर अत्तहिय्यात, दुरूद इब्राहीम, सलाम से पहले की दुआ और सलाम फेरना।",
                bodyEn = "After completing 2 Rak'ahs (and at the final Rak'ah of 3- or 4-Rak'ah prayers), sit calmly in Qa'dah. Place hands on thighs/knees and recite Tashahhud (At-Tahiyyat). In a 3- or 4-Rak'ah prayer, rise for the remaining Rak'ahs after At-Tahiyyat.\n\nIn the FINAL sitting (Qa'dah Akhirah), after Tashahhud recite Durood Ibrahim, then a concise prophetic Dua (such as asking protection from hardship or asking good in both worlds), and turn your head right then left saying 'As-Salamu Alaykum wa Rahmatullah'.",
                bodyUr = "دو رکعت کے بعد اور آخری رکعت کے دو سجدوں کے بعد اطمینان سے بیٹھیں (قعدہ)۔ اگر نماز 3 یا 4 رکعت کی ہو تو پہلے قعدے میں صرف التحیات پڑھ کر 'اللہ اکبر' کہتے ہوئے تیسری رکعت کے لیے کھڑے ہو جائیں۔\n\nآخری قعدے میں التحیات کے بعد درودِ ابراہیمی اور سلام سے پہلے کی مسنون دعا (مثلاً 'اللہم انی اعوذ بک...' یا 'ربنا آتنا فی الدنیا...') پڑھیں، اور پھر دائیں اور بائیں جانب چہرہ پھیر کر 'السلام علیکم و رحمۃ اللہ' کہہ کر نماز مکمل کریں۔",
                bodyAr = "في الجلوس للتشهد: يقرأ المصلي 'التحيات'، فإن كانت صلاةً ثلاثية أو رباعية قام بعد التشهد الأول، وفي التشهد الأخير يزيد الصلاة الإبراهيمية ثم يدعو بما شاء من الأدعية المأثورة، ويختم الصلاة بالتسليم يميناً ويساراً: 'السلام عليكم ورحمة الله'.",
                bodyHi = "2 रकअत के बाद और अंतिम रकअत में दोनों सजदों के बाद शांत बैठें (क़अदा)। यदि 3 या 4 रकअत की नमाज़ हो तो पहले क़अदा में केवल अत्तहिय्यात पढ़कर उठ जाएं।\n\nअंतिम क़अदा में अत्तहिय्यात के बाद दुरूद इब्राहीम, फिर सलाम से पहले की दुआ (जैसे 'रब्बना आतिना...') पढ़ें, और अंत में पहले दाएँ फिर बाएँ सिर घुमाकर 'अस्सलामु अलैकुम व रहमतुल्लाह' कहें।",
                fiqhDifferencesEn = "Finger movement in Tashahhud & Salam wording: Raising/pointing the right index finger during Tashahhud is practiced across schools (some hold steady at 'illallah', others gently point throughout). Salam: Hanafi, Shafi'i & Hanbali turn right then left; Maliki turns once right for single Salam (in solitary prayer). Also slight Sunnah variations exist in the wording of Tashahhud (Ibn Mas'ud vs Ibn Abbas)—all are authentic.",
                fiqhDifferencesUr = "شہادت کی انگلی اور تشہد کے الفاظ: تشہد میں شہادت کی انگلی اٹھانے کی کیفیت اور تشہد کے مختلف مستند الفاظ (ابن مسعودؓ یا ابن عباسؓ) میں جزوی فرق فقہی کتب میں موجود ہے اور سب معتبر ہیں۔ مالکیہ کے ہاں منفرد کے لیے ایک بار دائیں سلام پھیرنے کا رواج ہے، جبکہ دیگر تین مذاہب میں دائیں اور بائیں دو سلام مسنون ہیں۔",
                fiqhDifferencesAr = "اختلاف يسير في صيغ التشهد المأثورة وتحريك السبابة؛ وكلها ثابتة في الصحيحين والسنن.",
                fiqhDifferencesHi = "तशह्हुद में शहादत की उंगली उठाने के तरीक़े और अत्तहिय्यात की शब्दावली में हल्के फ़िक़ही अंतर हैं जो सभी सहीह हदीसों से प्रमाणित हैं।",
                recitations = listOf(
                    PrayerStepRecitation(
                        labelEn = "Tashahhud (At-Tahiyyat)",
                        labelUr = "التحیات (تشہد)",
                        labelAr = "التشهد",
                        labelHi = "तशह्हुद (अत्तहिय्यात)",
                        arabic = "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
                        transliteration = "At-tahiyyatu lillahi was-salawatu wat-tayyibat. As-salamu 'alayka ayyuhan-Nabiyyu wa rahmatullahi wa barakatuh. As-salamu 'alayna wa 'ala 'ibadillahis-salihin. Ash-hadu an la ilaha illallah, wa ash-hadu anna Muhammadan 'abduhu wa rasuluh.",
                        translationEn = "All compliments, prayers and pure words are due to Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no deity worthy of worship except Allah, and I bear witness that Muhammad is His servant and Messenger.",
                        translationUr = "تمام قولی، بدنی اور مالی عبادتیں اللہ کے لیے ہیں۔ اے نبی ﷺ آپ پر سلام اور اللہ کی رحمت اور برکتیں نازل ہوں۔ ہم پر اور اللہ کے تمام نیک بندوں پر سلامتی ہو۔ میں گواہی دیتا ہوں کہ اللہ کے سوا کوئی معبود نہیں اور محمد ﷺ اس کے بندے اور رسول ہیں۔",
                        translationAr = "كل التعظيم والصلوات والأقوال والأعمال الطيبة لله، والسلام والرحمة على النبي ﷺ وعلينا وعلى عباد الله الصالحين، مع النطق بالشهادتين.",
                        translationHi = "सब इबादतें, नमाज़ें और पाक चीज़ें अल्लाह के लिए हैं। ऐ नबी ﷺ, आप पर सलामती और अल्लाह की रहमत व बरकतें हों। हम पर और अल्लाह के नेक बंदों पर सलामती हो। मैं गवाही देता हूँ कि अल्लाह के सिवा कोई पूज्य नहीं और मुहम्मद ﷺ उसके बंदे और रसूल हैं।",
                        explanationEn = "Combines greetings of worship to Allah, peace upon the Prophet ﷺ and righteous believers everywhere, and renewal of the Shahadah (Sahih al-Bukhari 831).",
                        explanationUr = "اس میں اللہ کی تعظیم، نبی ﷺ اور دنیا بھر کے نیک بندوں کے لیے سلامتی کی دعا اور تجدیدِ ایمان شامل ہے۔",
                        explanationAr = "يجمع بين تعظيم الله، والسلام على النبي ﷺ وعموم الصالحين، وتجديد الشهادتين (البخاري: ٨٣١).",
                        explanationHi = "इसमें अल्लाह की स्तुति, पैगंबर ﷺ व सभी नेक लोगों के लिए शांति की दुआ और ईमान का नवीनीकरण है।"
                    ),
                    PrayerStepRecitation(
                        labelEn = "Durood Ibrahim (Sending Blessings Upon the Prophet ﷺ)",
                        labelUr = "درودِ ابراہیمی",
                        labelAr = "الصلاة الإبراهيمية",
                        labelHi = "दुरूद इब्राहीम",
                        arabic = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
                        transliteration = "Allahumma salli 'ala Muhammadin wa 'ala ali Muhammadin kama sallayta 'ala Ibrahima wa 'ala ali Ibrahima innaka Hamidun Majid. Allahumma barik 'ala Muhammadin wa 'ala ali Muhammadin kama barakta 'ala Ibrahima wa 'ala ali Ibrahima innaka Hamidun Majid.",
                        translationEn = "O Allah, send grace and honor upon Muhammad and upon the family of Muhammad, just as You sent grace and honor upon Ibrahim and upon the family of Ibrahim; indeed You are Praiseworthy, Most Glorious. O Allah, bless Muhammad and the family of Muhammad, just as You blessed Ibrahim and the family of Ibrahim; indeed You are Praiseworthy, Most Glorious.",
                        translationUr = "اے اللہ! رحمت نازل فرما محمد ﷺ پر اور آپ کی آل پر جیسا کہ تو نے رحمت نازل فرمائی ابراہیمؑ اور ان کی آل پر، بے شک تو تعریف کے لائق اور بزرگی والا ہے۔ اے اللہ! برکت عطا فرما محمد ﷺ پر اور آپ کی آل پر جیسا کہ تو نے برکت دی ابراہیمؑ اور ان کی آل کو، بے شک تو قابلِ تعریف اور بزرگی والا ہے۔",
                        translationAr = "طلب الرحمة والتعظيم والبركة للنبي محمد ﷺ وآله كما أُنعم بها على نبي الله إبراهيم وآله.",
                        translationHi = "ऐ अल्लाह! रहमत भेज मुहम्मद ﷺ और उनके परिवार पर जैसे तूने रहमत भेजी इब्राहीम (अलै.) और उनके परिवार पर, निश्चय ही तू प्रशंसनीय और महान है। ऐ अल्लाह! बरकत दे मुहम्मद ﷺ और उनके परिवार को जैसे तूने बरकत दी इब्राहीम (अलै.) और उनके परिवार को, निश्चय ही तू प्रशंसनीय और महान है।",
                        explanationEn = "The exact prayer Prophet Muhammad ﷺ taught his companions when they asked how to send blessings upon him (Sahih al-Bukhari 3370).",
                        explanationUr = "جب صحابہؓ نے آپ ﷺ سے درود پڑھنے کا طریقہ پوچھا تو آپ نے یہی درود سکھایا (صحیح بخاری: 3370)۔",
                        explanationAr = "الصيغة النبوية التي علمها ﷺ أصحابه لما سألوه كيف نصلي عليك (البخاري: ٣٣٧٠).",
                        explanationHi = "जब सहाबा ने पूछा कि आप पर दुरूद कैसे पढ़ें, तो पैगंबर ﷺ ने यही दुरूद सिखाया (सहीह बुख़ारी: 3370)।"
                    ),
                    PrayerStepRecitation(
                        labelEn = "Dua Before Salam & Ending Salam",
                        labelUr = "سلام سے پہلے کی دعا اور سلام",
                        labelAr = "الدعاء قبل السلام والتسليم",
                        labelHi = "सलाम से पहले की दुआ और सलाम",
                        arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ • السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
                        transliteration = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar • As-salamu 'alaykum wa rahmatullah",
                        translationEn = "Our Lord, grant us good in this world and good in the Hereafter and protect us from the punishment of the Fire • Peace and mercy of Allah be upon you.",
                        translationUr = "اے ہمارے رب! ہمیں دنیا میں بھی بھلائی عطا فرما اور آخرت میں بھی بھلائی دے اور ہمیں آگ کے عذاب سے بچا • تم پر سلامتی اور اللہ کی رحمت ہو۔",
                        translationAr = "دعاء جامع لخير الدنيا والآخرة، ثم التسليم عن اليمين وعن الشمال لتحليل الصلاة ونشر السلام.",
                        translationHi = "ऐ हमारे रब! हमें दुनिया में भी भलाई दे और आख़िरत में भी भलाई दे और आग के अज़ाब से बचा • आप पर शांति और अल्लाह की रहमत हो।",
                        explanationEn = "Closes the personal conversation with Allah by sending peace to the angels and people on your right and left.",
                        explanationUr = "دنیا و آخرت کی بھلائی کی جامع دعا کے بعد دائیں بائیں فرشتوں اور نمازیوں پر سلامتی کی دعا کے ساتھ نماز کا اختتام۔",
                        explanationAr = "ختام المناجاة بدعاء جامع ثم إلقاء السلام يميناً ويساراً على الملائكة والمصلين.",
                        explanationHi = "दोनों दुनिया की भलाई की दुआ के बाद दाएँ-बाएँ फ़रिश्तों और नमाज़ियों को सलाम के साथ नमाज़ पूरी होती है।"
                    )
                ),
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 831, 3370"),
                    IslamicReference("Sahih Muslim", "Hadith 588 (Seeking refuge from four trials before Salam)")
                ),
                quizId = "q_s3_2"
            ),
            AcademyLesson(
                id = "s3_l7",
                stageNumber = 3,
                lessonNumber = 7,
                titleEn = "7. Congregational Prayer (Jama'ah), Jumu'ah & Travel",
                titleUr = "7. باجماعت نماز، جمعہ اور سفر میں قصر کے احکام",
                titleAr = "٧. صلاة الجماعة والجمعة وقصر المسافر",
                titleHi = "7. जमाअत से नमाज़, जुमुआ और सफ़र (क़स्र)",
                summaryEn = "How to follow an Imam smoothly, make up missed Rak'ahs, attend Friday prayer, and shorten prayer while traveling.",
                summaryUr = "امام کے پیچھے نماز پڑھنے، چھوٹی ہوئی رکعت پوری کرنے، نمازِ جمعہ اور سفر میں 4 رکعت کو 2 کرنے کا طریقہ۔",
                summaryAr = "متابعة الإمام وإدراك الفائت، وفضل صلاة الجمعة، ورخصة القصر في السفر.",
                summaryHi = "इमाम के पीछे नमाज़, छूटी हुई रकअत पूरी करना, जुमुआ की नमाज़ और यात्रा में क़स्र के नियम।",
                bodyEn = "• Congregational Prayer (Jama'ah): Prophet ﷺ taught that prayer in congregation is 27 times more rewarding than praying alone (Bukhari 645). Stand shoulder-to-shoulder calmly in straight rows and follow the Imam—never move ahead of the Imam.\n• Joining Late: Enter the prayer immediately in whatever posture the Imam is in (standing, bowing, or prostrating). Note that you only count a Rak'ah as caught if you joined the Imam in Ruku before he rises. After the Imam finishes with Salam, stand up calmly without saying Salam to complete the missed Rak'ah(s).\n• Friday (Jumu'ah): Replaces Dhuhr with a short Khutbah (sermon)—listen quietly—followed by 2 Fard Rak'ahs behind the Imam.\n• Travel (Qasr): While traveling a valid journey distance (~78–85 km depending on Madhhab), 4-Rak'ah Fard prayers (Dhuhr, Asr, Isha) are shortened to 2 Rak'ahs.",
                bodyUr = "• باجماعت نماز: جماعت کی نماز تنہا نماز سے 27 درجہ زیادہ ثواب رکھتی ہے (صحیح بخاری: 645)۔ صفیں سیدھی رکھیں اور امام کی پیروی کریں، امام سے سبقت نہ کریں۔\n• مسبوق (دیر سے شامل ہونا): امام جس حالت میں ہو تکبیر کہہ کر شامل ہو جائیں۔ اگر رکوع میں امام کو پا لیا تو وہ رکعت شمار ہوگی۔ امام کے سلام پھیرنے کے بعد سلام نہ پھیریں بلکہ کھڑے ہو کر اپنی باقی رکعتیں مکمل کریں۔\n• جمعہ: ظہر کے وقت خطبہ غور سے سنیں اور امام کے پیچھے 2 فرض پڑھیں۔\n• سفر میں قصر: شرعی مسافت (تقریباً 78 تا 88 کلومیٹر) کے سفر میں 4 رکعت والی فرض نمازیں (ظہر، عصر، عشاء) 2 رکعت پڑھی جاتی ہیں۔",
                bodyAr = "• صلاة الجماعة تفضل صلاة الفذ بسبع وعشرين درجة (البخاري: ٦٤٥)، ويجب متابعة الإمام وعدم مسابقته.\n• من أدرك الإمام في الركوع فقد أدرك الركعة، وما فاته يتمه بعد تسليم الإمام.\n• صلاة الجمعة ركعتان بعد الخطبتين بإنصات.\n• يُشرع للمسافر قصر الرباعية (الظهر والعصر والعشاء) إلى ركعتين.",
                bodyHi = "• जमाअत से नमाज़: अकेले से 27 गुना ज़्यादा सवाब रखती है (सहीह बुख़ारी: 645)। कतारें सीधी रखें और इमाम से आगे न बढ़ें।\n• देर से शामिल होना: इमाम जिस अवस्था में हों जुड़ जाएं; यदि रुकू मिल जाए तो वह रकअत गिनी जाएगी। इमाम के सलाम फेरने के बाद खुद उठकर छूटी रकअतें पूरी करें।\n• जुमुआ: ज़ुहर की जगह ख़ुत्बा ध्यान से सुनें और इमाम के साथ 2 फ़र्ज़ पढ़ें।\n• सफ़र में क़स्र: लगभग 80 किमी से अधिक सफ़र में 4 फ़र्ज़ रकअत वाली नमाज़ें (ज़ुहर, अस्र, इशा) 2 रकअत पढ़ी जाती हैं।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 645, 883"),
                    IslamicReference("Holy Quran", "Surah An-Nisa (4:101)", "Shortening prayer during travel")
                )
            ),
            AcademyLesson(
                id = "s3_l8",
                stageNumber = 3,
                lessonNumber = 8,
                titleEn = "8. Avoiding Common Mistakes & Building Khushu'",
                titleUr = "8. نماز کی عام غلطیاں اور خشوع و خضوع کا حصول",
                titleAr = "٨. تجنب الأخطاء الشائعة وتحصيل الخشوع",
                titleHi = "8. नमाज़ की आम गलतियों से बचाव और ख़ुशू (एकाग्रता)",
                summaryEn = "Practical checklist: calm pauses (Tuma'ninah), clear focus, and gentle improvement.",
                summaryUr = "عجلت سے بچنا، ارکان میں اطمینان (تعديلِ ارکان) اور دل جمعی پیدا کرنے کے عملی طریقے۔",
                summaryAr = "الاطمئنان في الأركان، وتدبر المعاني، وتجنب العجلة والالتفات.",
                summaryHi = "जल्दबाज़ी से बचना, हर मुद्रा में शांति से रुकना और ध्यान लगाने के आसान उपाय।",
                bodyEn = "Common pitfalls to avoid:\n1. Rushing postures like pecking (missing Tuma'ninah)—pause briefly so every joint settles in Ruku, Qawmah, Sujood, and Jalsah.\n2. Letting the phone ring without silent mode.\n3. Looking around or checking a watch mid-prayer.\n4. Feeling discouraged if thoughts wander—every mind wanders; gently return focus to the words you are reciting.\n\nPro tip for Khushu': Learn the meaning of Al-Fatihah and the short Tasbeeh phrases (covered in this stage!) so every sentence becomes real conversation.",
                bodyUr = "ان عام کوتاہیوں سے بچیں:\n1. جلدی جلدی ٹھونگے مارنے کی طرح نماز پڑھنا—ہر رکن (رکوع، قومہ، سجدہ، جلسہ) میں کم از کم ایک تسبیح کی مقدار سکون سے ٹھہریں۔\n2. نماز سے پہلے فون سائلنٹ کرنا بھول جانا۔\n3. ادھر ادھر دیکھنا یا گھڑی بار بار دیکھنا۔\n4. خیالات آنے پر ہمت نہ ہاریں؛ بس نرمی سے توجہ دوبارہ تلاوت کے مفہوم پر لے آئیں۔",
                bodyAr = "تجنب الاستعجال في الأركان دون طمأنينة، وتأكد من وضع الهاتف على الصامت، وتجنب الالتفات وكثرة الحركة. وعند شتات الذهن أعد قلبك بلطف لتدبر معاني الفاتحة والأذكار.",
                bodyHi = "इन गलतियों से बचें:\n1. जल्दबाज़ी में नमाज़ पढ़ना—हर रुकू, क़ौमा, सजदा और जलसे में शरीर को पूरा शांत करें।\n2. नमाज़ से पहले फ़ोन साइलेंट रखना न भूलें।\n3. इधर-उधर देखना या घड़ी देखना।\n4. मन भटके तो निराश न हों—धीरे से ध्यान फिर से अल्लाह के शब्दों के अर्थ पर ले आएं।",
                practicalTaskEn = "Practice reciting every Salah phrase once slowly out loud today while reading its meaning alongside.",
                practicalTaskUr = "آج ثنا، فاتحہ، رکوع، سجدہ اور تشہد کے الفاظ ترجمے کے سامنے رکھ کر آہستہ آہستہ پڑھنے کی مشق کریں۔",
                practicalTaskAr = "تدرب اليوم على ترديد أذكار الصلاة ببطء مع قراءة ترجمتها لتثبيت الخشوع.",
                practicalTaskHi = "आज सना, फ़ातिहा, रुकू, सजदा और तशह्हुद के शब्द अर्थ के साथ धीरे-धीरे दोहराने का अभ्यास करें।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 757 (Hadith of the man who prayed hastily—teaching calm Tuma'ninah)")
                )
            )
        )
    )

    val stage4 = AcademyStage(
        stageNumber = 4,
        id = "stage_4",
        titleEn = "Stage 4: Daily Islamic Life & Character",
        titleUr = "مرحلہ 4: روزمرہ اسلامی زندگی اور اخلاق",
        titleAr = "المرحلة ٤: الحياة اليومية والأخلاق الإسلامية",
        titleHi = "चरण 4: दैनिक इस्लामिक जीवन और चरित्र",
        subtitleEn = "Honesty, family ties, Halal living, speech & community",
        subtitleUr = "سچائی، والدین و رشتہ دار، حلال و حرام، زبان کی حفاظت اور معاشرہ",
        subtitleAr = "الصدق والأمانة، بر الوالدين، الحلال والحرام، حفظ اللسان وحسن الجوار",
        subtitleHi = "ईमानदारी, माता-पिता व परिवार, हलाल जीवन, वाणी और समाज",
        overviewEn = "Bring Islamic ethics into daily conduct: truthfulness, compassion toward all family (Muslim and non-Muslim), Halal lifestyle, and guarding speech.",
        overviewUr = "عبادات کے ساتھ اپنے اخلاق، خاندانی تعلقات، حلال غذا و لباس، اور گفتگو کو سنتِ نبوی ﷺ کے مطابق سنوارنے کی عملی تعلیم۔",
        overviewAr = "تطبيق الأخلاق النبوية في المعاملات اليومية: الأمانة، وصلة الأهل، وضوابط الطعام واللباس وحفظ اللسان.",
        overviewHi = "दैनिक जीवन में ईमानदारी, परिवार के प्रति सम्मान, हलाल खान-पान, शालीन पोशाक और मधुर वाणी अपनाने की शिक्षा।",
        interactiveToolType = StageToolType.NONE,
        lessons = listOf(
            AcademyLesson(
                id = "s4_l1",
                stageNumber = 4,
                lessonNumber = 1,
                titleEn = "1. Truthfulness, Trust (Amanah) & Controlling Anger",
                titleUr = "1. سچائی، امانت داری اور غصے پر قابو",
                titleAr = "١. الصدق والأمانة وضبط الغضب",
                titleHi = "1. सच्चाई, ईमानदारी (अमानत) और क्रोध पर नियंत्रण",
                summaryEn = "Noble character sits at the very heart of Islam: keeping promises, truthfulness, and patience.",
                summaryUr = "بہترین اخلاق، وعدے کی پابندی اور غصے کو پی جانا دین کی پہچان ہے۔",
                summaryAr = "أقربكم مني مجلساً يوم القيامة أحاسنكم أخلاقاً: الصدق والوفاء والحلم.",
                summaryHi = "सच्चाई, वादा निभाना और गुस्से पर क़ाबू रखना एक मुसलमान के चरित्र की पहचान है।",
                bodyEn = "Prophet Muhammad ﷺ declared: 'I was only sent to perfect noble character' (Musnad Ahmad / Al-Adab al-Mufrad 273). Faith is seen in how reliably we keep promises, speak truth even when inconvenient, and manage anger.\n\nWhen angry, the Prophetic advice is concrete: seek refuge in Allah, fall silent, change posture (sit down if standing), or make Wudu with cool water.",
                bodyUr = "نبی اکرم ﷺ نے فرمایا: 'میں بہترین اخلاق کی تکمیل کے لیے بھیجا گیا ہوں'۔ ایمان کا عملی ثبوت سچ بولنے، امانت لوٹانے، وعدہ پورا کرنے اور غصے کے وقت خود پر قابو رکھنے سے ملتا ہے۔\n\nغصہ آئے تو سنت طریقہ یہ ہے کہ خاموش ہو جائیں، کھڑے ہوں تو بیٹھ جائیں، تعوذ پڑھیں اور وضو کریں۔",
                bodyAr = "قال النبي ﷺ: 'إنما بعثت لأتمم صالح الأخلاق'. الصدق، وأداء الأمانة، والوفاء بالعهد من صميم الإيمان. وعند الغضب أوصى النبي ﷺ بالاستعاذة، والسكوت، وتغيير الهيئة، والوضوء.",
                bodyHi = "पैगंबर ﷺ ने फ़रमाया: 'मैं उत्तम चरित्र को पूर्ण करने के लिए भेजा गया हूँ।' सच्चाई, वादा निभाना और अमानत की रक्षा करना ईमान का हिस्सा है।\n\nगुस्सा आने पर चुप रहना, खड़े हों तो बैठ जाना और ठंडे पानी से वुज़ू करना नबी ﷺ का व्यावहारिक सुझाव है।",
                references = listOf(
                    IslamicReference("Sahih al-Bukhari", "Hadith 6116 (Do not get angry), 6094 (Truthfulness leads to righteousness)"),
                    IslamicReference("Al-Adab al-Mufrad", "Hadith 273 (Perfecting noble character)")
                )
            ),
            AcademyLesson(
                id = "s4_l2",
                stageNumber = 4,
                lessonNumber = 2,
                titleEn = "2. Parents, Family & Relatives of Other Faiths",
                titleUr = "2. والدین، خاندان اور غیر مسلم رشتہ داروں سے حسنِ سلوک",
                titleAr = "٢. بر الوالدين وصلة الأهل والأقارب من غير المسلمين",
                titleHi = "2. माता-पिता, परिवार और अन्य धर्मों के रिश्तेदारों से सद्व्यवहार",
                summaryEn = "Islam commands deep respect and kindness toward parents and family—even when they hold different beliefs.",
                summaryUr = "والدین اور رشتہ داروں سے حسنِ سلوک، خواہ ان کا مذہب مختلف ہو—شرک کے بغیر ان کی خدمت لازم ہے۔",
                summaryAr = "وجوب الإحسان للوالدين وصلة الرحم، والحفاظ على البر حتى مع اختلاف الدين.",
                summaryHi = "माता-पिता और परिवार की सेवा और सम्मान हर हाल में ज़रूरी है, चाहे उनकी आस्था अलग ही क्यों न हो।",
                bodyEn = "For many new Muslims, family relationships are an immediate concern. The Quran (31:15) addresses this directly: even if parents pressure you away from faith in One God, do not obey them in disbelief—yet 'accompany them in this world with kindness.'\n\nIslam strengthens family mercy: visit, call, share meals, care for aging parents, and maintain honorable character so your life becomes a living reflection of Islam's beauty.",
                bodyUr = "نئے مسلمانوں کے لیے خاندانی تعلقات اہم مسئلہ ہوتے ہیں۔ قرآن مجید (سورۃ لقمان 31:15) واضح رہنمائی دیتا ہے کہ اگر والدین شرک پر مجبور کریں تو ان کی اس بات میں اطاعت نہ کی جائے، لیکن 'دنیا میں ان کے ساتھ نہایت اچھے طریقے سے رہو'۔\n\nوالدین اور رشتہ داروں کی خدمت، خبر گیری اور احترام برقرار رکھیں۔",
                bodyAr = "أوضح القرآن الكريم (لقمان: ٣١:١٥) أنه حتى لو جاهد الوالدان ابنهما على الشرك فلا يطعهما في ذلك، 'وصاحبهما في الدنيا معروفا'. فالإسلام يأمر ببر الوالدين والإحسان للأقارب والجيران أياً كانت معتقداتهم.",
                bodyHi = "पवित्र क़ुरआन (31:15) स्पष्ट करता है कि यदि माता-पिता अलग आस्था रखते हों, तब भी 'दुनिया में उनके साथ भलाई और सम्मान से पेश आओ।' केवल ईश्वर से विमुख होने की बात न मानो, बाकी हर सेवा, प्रेम और आदर बनाए रखो।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Luqman (31:14-15)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 2620 (Asma bint Abi Bakr keeping ties with her non-Muslim mother)")
                )
            ),
            AcademyLesson(
                id = "s4_l3",
                stageNumber = 4,
                lessonNumber = 3,
                titleEn = "3. Halal & Haram Food, Drink & Modest Dress",
                titleUr = "3. حلال و حرام غذا، مشروبات اور حیا دار لباس",
                titleAr = "٣. الضوابط الشرعية في الطعام والشراب واللباس",
                titleHi = "3. हलाल व हराम खान-पान और शालीन पहनावा",
                summaryEn = "Clear overview of permissible food/drink, alcohol/pork prohibition, and practical modesty guidelines.",
                summaryUr = "طیب اور حلال خوراک، شراب و خنزیر کی حرمت اور ستر و حیا کے بنیادی اصول۔",
                summaryAr = "تناول الطيبات واجتناب الخنزير والمسكرات والميتة، وضوابط اللباس والستر.",
                summaryHi = "शुद्ध और हलाल भोजन, शराब व पोर्क से परहेज़ और शालीन वस्त्रों के नियम।",
                bodyEn = "Eating & Drinking:\n• Prohibited (Haram): Pork and all pork by-products, blood, carrion (dead animals), animals not slaughtered properly in Allah's name, and all intoxicants (alcohol and recreational drugs).\n• Everything pure and wholesome from plants, grains, fruits, dairy, eggs, and properly slaughtered Halal meat (and fish/seafood—noting Hanafi excludes non-fish shellfish while others allow all sea creatures) is Halal.\n\nDress & Modesty:\nIslam values dignity and cleanliness for both men and women: clothing should be clean, non-see-through, not overly tight, and cover the Awrah (navel-to-knee minimum for men; whole body except face and hands in public for women across majority scholarship).",
                bodyUr = "خوراک و مشروبات:\n• حرام: سور اور اس سے بنی اشیاء، مردار، بہتا ہوا خون، بغیر ذبح کے جانور، اور ہر قسم کی شراب و نشہ آور چیزیں۔\n• حلال: تمام پاک سبزیاں، پھل، دودھ، اناج اور شرعی طریقے سے ذبح کیا گیا حلال گوشت و مچھلی (سمندری جھینگے وغیرہ میں حنفی و دیگر مذاہب کے درمیان فقہی اختلاف ہے)۔\n\nلباس و حیا:\nمرد و عورت دونوں کے لیے سادگی، صفائی اور ستر پوشی ضروری ہے؛ لباس شفاف یا بے حد چست نہ ہو۔",
                bodyAr = "• الأطعمة: أحل الله الطيبات وحرّم الميتة والدم ولحم الخنزير وما أهلّ لغير الله به والمسكرات، مع مراعاة التذكية الشرعية.\n• اللباس: يشترط النظافة والستر وعدم التشبه المنهي عنه، فالعورة للرجل من السرة إلى الركبة، وللمرأة جميع البدن عدا الوجه والكفين أمام الأجانب وفق جمهور الفقهاء.",
                bodyHi = "खान-पान:\n• वर्जित (हराम): पोर्क (सूअर का मांस और उससे बने उत्पाद), बहता रक्त, मरे हुए जानवर, बिना बिस्मिल्लाह के काटा गया मांस और शराब व सभी नशीले पदार्थ।\n• हलाल: शुद्ध फल, सब्ज़ियाँ, अनाज, दूध, अंडे, मछली और विधिवत हलाल ज़बह किया गया मांस।\n\nपहनावा: कपड़े साफ़, शालीन और ढीले हों जो निर्धारित अंगों (सतर) को ठीक से ढकें।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Ma'idah (5:3, 5:90)"),
                    IslamicReference("Holy Quran", "Surah Al-A'raf (7:31-32)")
                )
            ),
            AcademyLesson(
                id = "s4_l4",
                stageNumber = 4,
                lessonNumber = 4,
                titleEn = "4. Guarding the Tongue, Privacy & Humility",
                titleUr = "4. زبان کی حفاظت، تجسس سے بچاؤ اور عاجزی",
                titleAr = "٤. حفظ اللسان واجتناب الغيبة وحرمة الخصوصية",
                titleHi = "4. वाणी पर संयम, निजता का सम्मान और विनम्रता",
                summaryEn = "Avoiding backbiting, slander, mockery, and prying into people's private lives.",
                summaryUr = "غیبت، چغلی، تمسخر، بدگمانی اور دوسروں کے عیب ٹٹولنے سے بچنے کی قرآنی تعلیم۔",
                summaryAr = "توجيهات سورة الحجرات في اجتناب السخرية والظن السيء والتجسس والغيبة.",
                summaryHi = "पीठ पीछे बुराई (ग़ीबत), मज़ाक, बुरे शक और दूसरों की जासूसी से बचने की क़ुरआनी सीख।",
                bodyEn = "Surah Al-Hujurat (Chapter 49) gives an ethical blueprint for healthy families, friendships, and online behavior:\n• Verify news before sharing or reacting (49:6).\n• Do not mock or demean others (49:11).\n• Avoid negative assumptions about people's motives (49:12).\n• Never spy on private affairs or backbite someone behind their back (49:12).\n\n'Whoever believes in Allah and the Last Day should speak good or remain silent' (Bukhari 6018).",
                bodyUr = "سورۃ الحجرات (پارہ 26) ایک پاکیزہ معاشرے کے زریں اصول سکھاتی ہے:\n• بغیر تحقیق کسی خبر کو آگے نہ پھیلائیں (49:6)۔\n• کسی کا مذاق نہ اڑائیں اور برے القاب سے نہ پکاریں (49:11)۔\n• بدگمانی، تجسس (جاسوسی) اور غیبت سے بچیں (49:12)۔\n\nحدیثِ نبوی ﷺ: 'جو اللہ اور آخرت پر ایمان رکھتا ہو، وہ اچھی بات کہے یا خاموش رہے' (صحیح بخاری: 6018)۔",
                bodyAr = "ترسم سورة الحجرات ميثاق الأخلاق الاجتماعية: التثبت من الأخبار (٦)، ترك السخرية والتنابز بالألقاب (١١)، واجتناب سوء الظن والتجسس والغيبة (١٢). وقال ﷺ: 'من كان يؤمن بالله واليوم الآخر فليقل خيراً أو ليصمت' (البخاري: ٦٠١٨).",
                bodyHi = "सूरह अल-हुजुरात (अध्याय 49) समाज और सोशल मीडिया के लिए सुंदर नियम सिखाती है:\n• किसी ख़बर को आगे बढ़ाने से पहले जांचें (49:6)।\n• दूसरों का मज़ाक न उड़ाएं (49:11)।\n• बुरे शक, जासूसी और पीठ पीछे बुराई (ग़ीबत) से बचें (49:12)।\n\nपैगंबर ﷺ ने कहा: 'जो अल्लाह और आख़िरत पर ईमान रखता है, वह भली बात कहे या चुप रहे' (सहीह बुख़ारी: 6018)।",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Hujurat (49:6-13)"),
                    IslamicReference("Sahih al-Bukhari", "Hadith 6018")
                )
            ),
            AcademyLesson(
                id = "s4_l5",
                stageNumber = 4,
                lessonNumber = 5,
                titleEn = "5. Honest Livelihood, Generosity & Serving Others",
                titleUr = "5. حلال روزی، سخاوت اور مخلوقِ خدا کی خدمت",
                titleAr = "٥. الكسب الحلال والجود وخدمة المجتمع",
                titleHi = "5. ईमानदार आजीविका, उदारता और मानव सेवा",
                summaryEn = "Working ethically, honoring contracts, avoiding interest/exploitation, and supporting those in need.",
                summaryUr = "محنت و دیانت داری سے کمانا، حقوق العباد کی ادائیگی اور ضرورت مندوں کی مدد کرنا۔",
                summaryAr = "إتقان العمل والكسب الحلال، وتجنب الغش والربا، والمبادرة في نفع الناس.",
                summaryHi = "मेहनत व ईमानदारी से कमाना, मिलावट-ब्याज से बचना और ज़रूरतमंदों की मदद करना।",
                bodyEn = "Worship in Islam extends directly into your workplace and finances:\n• Fulfill contracts honestly and give fair value without cheating or false advertising.\n• Avoid interest-bearing exploitation (Riba), gambling, and deception.\n• Remember the prophetic standard: 'The best of people are those most beneficial to others.' Giving charity—money, time, mentoring, or caring for creation—brings Barakah (blessing) to both wealth and heart.",
                bodyUr = "اسلام میں عبادت صرف مسجد تک محدود نہیں بلکہ کاروبار اور ملازمت میں دیانت داری بھی دین ہے:\n• معاہدوں کی پابندی کریں اور ناپ تول یا کام میں دھوکہ نہ دیں۔\n• سود، جوا اور ناجائز کمائی سے بچیں۔\n• حدیثِ مبارکہ ہے کہ لوگوں میں سب سے بہتر وہ ہے جو دوسروں کو فائدہ پہنچائے۔",
                bodyAr = "العبادة تمتد إلى العمل والمال: إتقان المهنة، الوفاء بالعقود، اجتناب الغش والربا والميسر، والسعي في قضاء حوائج الناس؛ فخير الناس أنفعهم للناس.",
                bodyHi = "इस्लाम में इबादत आपके काम और कमाई तक फैली है:\n• अपने कार्य और समझौतों में पूरी ईमानदारी रखें। मिलावट, धोखा, जुआ और ब्याज (रिबा) से बचें।\n• पैगंबर ﷺ का मार्गदर्शन है: 'लोगों में सबसे उत्तम वह है जो दूसरों के लिए सबसे अधिक लाभकारी हो।'",
                references = listOf(
                    IslamicReference("Holy Quran", "Surah Al-Mutaffifin (83:1-3)"),
                    IslamicReference("Sahih Muslim", "Hadith 1015 (Allah is Pure and accepts only what is pure)")
                ),
                quizId = "q_s4_1"
            )
        )
    )

    val allStages1To4: List<AcademyStage> = listOf(stage1, stage2, stage3, stage4)
}
