package com.example.data.prophets

import com.example.data.model.AppLanguage

internal val PROPHET_CATALOG_PART3: List<ProphetStory> = listOf(
    ProphetStory(
        id = "harun",
        orderNumber = 15,
        arabicName = "هارون عليه السلام",
        names = langMap("Harun (AS)", "حضرت ہارون علیہ السلام", "هارون عليه السلام", "हज़रत हारून (अ.स.)"),
        titles = langMap("Eloquent Brother & Minister to Musa", "حضرت موسیٰؑ کے معاون و وزیر نبی", "أخو موسى ووزيره النقي", "मूसा (अ.स.) के वाक्पटु भाई व सहायक नबी"),
        category = ProphetCategory.BANI_ISRAIL_ERA,
        summaryIntro = langMap(
            "Appointed prophet in direct response to Musa's supplication ('And appoint for me a minister from my family — Harun, my brother'), supporting him before Pharaoh and caring deeply for unity.",
            "حضرت موسیٰؑ کی دعا پر بطور نبی و معاون مقرر ہوئے؛ فرعون کے دربار میں دعوت میں شریک رہے اور بنی اسرائیل کے اتحاد و توحید کی حفاظت کے لیے کوشاں رہے۔",
            "وهبه الله نبياً ووزيراً لأخيه موسى استجابةً لدعائه، فسانده أمام فرعون وحرص على حفظ وحدة بني إسرائيل وعدم تفرقهم.",
            "मूसा (अ.स.) की दुआ पर उनके सहायक नबी बनाए गए; फ़िरऔन के समक्ष दावत में साथ रहे और बनी इस्राईल की एकता की रक्षा का प्रयास किया।"
        ),
        detailedIntro = langMap(
            "Musa (AS) praised Harun's superior eloquence when asking Allah to appoint him as partner in the mission (20:29-32, 28:34). Allah granted his request out of mercy (19:53). During Musa's retreat at Mount Tur, when As-Samiri misled Bani Isra'il into idolizing the golden calf, Harun warned them firmly that their Lord was Ar-Rahman (20:90) and patiently avoided bloodshed that would fracture the community until Musa returned (20:94).",
            "حضرت موسیٰؑ نے عرض کیا کہ میرے بھائی ہارونؑ کی زبان زیادہ فصیح ہے، انہیں میرا شریکِ کار بنا دیجیے (20:29-32، 28:34)، جو اللہ نے اپنی رحمت سے قبول فرمایا (19:53)۔ کوہِ طور سے موسیٰؑ کی واپسی تک جب سامری نے گوسالہ پرستی کا فتنہ اٹھایا تو حضرت ہارونؑ نے صاف تنبیہ فرمائی کہ تمہارا رب تو رحمٰن ہے (20:90) اور خونریزی سے بچ کر اتحاد برقرار رکھنے کی کوشش کی (20:94)۔",
            "طلب موسى من ربه أن يشد به أزره لأنه أفصح لساناً (طه 20:29-32، القصص 28:34)، فاستجاب الله له ووهبه هارون نبياً (مريم 19:53). وحين افتتن بنو إسرائيل بالعجل في غياب موسى نصحهم هارون بصراحة (طه 20:90) وآثر حقن الدماء وحفظ الجماعة حتى يرجع موسى.",
            "मूसा (अ.स.) ने हारून (अ.स.) की स्पष्ट वाणी के कारण उन्हें सहायक नबी बनाने की दुआ की जिसे अल्लाह ने स्वीकार किया (19:53)। जब सामरी ने बछड़े का फ़ितना खड़ा किया तो हारून (अ.स.) ने लोगों को साफ़ चेतावनी दी कि तुम्हारा रब रहमान है (20:90) और गृहयुद्ध से बचाया (20:94)।"
        ),
        quranicContext = langMap(
            "Mentioned across Maryam (19:53), Taha (20:29-36, 90-94), Al-Anbiya (21:48), and As-Saffat (37:114-122).",
            "مریم (19:53)، طٰہٰ (20:29-36، 90-94)، الانبیاء (21:48) اور الصافات (37:114-122) میں مذکور ہے۔",
            "في مريم (19:53) وطه (20:29-36، 90-94) والأنبياء (21:48) والصافات (37:114-122).",
            "मरयम (19:53), ताहा (20:29-36, 90-94), अंबिया (21:48) और साफ़्फ़ात (37:114-122) में वर्णित।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Appointed prophet and partner to Musa (AS) by divine grace (19:53, 20:29-36).",
                "Appeared alongside Musa before Pharaoh with gentleness and clarity (20:43-48).",
                "Admonished Bani Isra'il firmly against the calf while safeguarding unity until Musa returned (20:90-94)."
            ),
            listOf(
                "رحمتِ الٰہی سے حضرت موسیٰؑ کے معاون و نبی مقرر ہونا (19:53، 20:29-36)۔",
                "فرعون کے سامنے نرمی اور حکمت سے دعوت میں شرکت (20:43-48)۔",
                "بچھڑے کی پرستش پر تنبیہ اور واپسیِ موسیٰؑ تک امت کے اتحاد کا تحفظ (20:90-94)۔"
            ),
            listOf(
                "تعيينه نبياً ومسانداً لأخيه موسى (مريم 19:53، طه 20:29-36).",
                "مشاركة موسى في دعوة فرعون بالقول اللين (طه 20:43-48).",
                "الإنكار على عبادة العجل مع الحرص على وحدة الصف (طه 20:90-94)."
            ),
            listOf(
                "मूसा (अ.स.) के सहायक व नबी के रूप में नियुक्ति (19:53, 20:29-36)।",
                "फ़िरऔन के समक्ष नरम और स्पष्ट दावत (20:43-48)।",
                "बछड़े की पूजा का विरोध व समुदाय की एकता की रक्षा (20:90-94)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Harun (AS), brother Musa (AS), Bani Isra'il", "Egypt and Sinai wilderness"),
            listOf("حضرت ہارونؑ، بھائی حضرت موسیٰؑ، بنی اسرائیل", "مصر اور صحرائے سینا"),
            listOf("هارون وأخوه موسى وبنو إسرائيل", "مصر وسيناء"),
            listOf("हारून (अ.स.), भाई मूसा (अ.स.), बनी इस्राईल", "मिस्र व सीना प्रांत")
        ),
        verseRefs = listOf(
            ProphetVerseRef(20, 29, 36, langMap("Appointment as Partner Prophet", "معاونِ نبوت کا عطا ہونا", "مؤازرة هارون لموسى", "सहायक नबी की नियुक्ति")),
            ProphetVerseRef(20, 90, 94, langMap("Wisdom & Unity During the Trial", "آزمائش میں حکمت و اتحاد", "الحكمة وحفظ الجماعة", "संकट में धैर्य व एकता"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih Muslim 162a",
                "https://sunnah.com/muslim:162a",
                langMap(
                    "During Al-Isra & Mi'raj, the Prophet ﷺ met Harun (AS) in the fifth heaven, who welcomed him warmly.",
                    "معراج کی رات نبی اکرم ﷺ کی پانچویں آسمان پر حضرت ہارونؑ سے ملاقات ہوئی۔",
                    "في حديث المعراج لقي النبي ﷺ هارون عليه السلام في السماء الخامسة.",
                    "मेराज की रात नबी ﷺ की पाँचवें आसमान में हज़रत हारून (अ.स.) से भेंट हुई।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "True brotherhood rejoices in cooperating for righteousness without rivalry (20:29-32).",
                "Wise leaders uphold both clear truth and community cohesion during crises."
            ),
            listOf(
                "سچا بھائی چارہ نیکی کے کاموں میں حسد کے بجائے ایک دوسرے کا سہارا بننے کا نام ہے۔",
                "دانا قیادت حق کے اعلان کے ساتھ امت کو خونریزی و انتشار سے بچاتی ہے۔"
            ),
            listOf(
                "الأخوة الإيمانية تقوم على التعاون في الدعوة ونبذ التنافس.",
                "الحكمة تجمع بين إنكار المنكر وحقن الدماء وحفظ الجماعة."
            ),
            listOf(
                "सच्चा भाईचारा नेकी के कार्य में बिना ईर्ष्या एक-दूसरे का सहयोग करना है।",
                "समझदार नेतृत्व सत्य पर अडिग रहते हुए भी समाज को टूटने से बचाता है।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Anchored strictly in Quran 19:53, 20:29-36, 20:90-94, 37:114-122 and Sahih Muslim 162a.",
            "قرآن کریم (19، 20، 37) اور صحیح مسلم (162a) پر مبنی۔",
            "مستند إلى القرآن الكريم وصحيح مسلم (162a).",
            "क़ुरआन (19, 20, 37) और सहीह मुस्लिम 162a पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Why did Musa (AS) ask Allah to appoint Harun (AS) with him in Quran 28:34?",
                "قرآن (28:34) میں حضرت موسیٰؑ نے حضرت ہارونؑ کو ساتھ بھیجنے کی دعا کس وجہ سے فرمائی؟",
                "لماذا طلب موسى من الله إرسال هارون معه (القصص 28:34)؟",
                "क़ुरआन 28:34 में मूसा (अ.स.) ने हारून (अ.स.) को साथ भेजने की दुआ क्यों की?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Harun was more fluent in speech to affirm Musa's truth", "Harun knew sea navigation", "Harun ruled Egypt"),
                AppLanguage.URDU to listOf("حضرت ہارونؑ کی زبان زیادہ فصیح تھی تاکہ تصدیق و معاونت کریں", "سمندری سفر کے ماہر تھے", "مصر کے بادشاہ تھے"),
                AppLanguage.ARABIC to listOf("لأنه أفصح لساناً ليرسله ردءاً يصدّقه", "لخبرته بالبحار", "لأنه كان حاكم مصر"),
                AppLanguage.HINDI to listOf("हारून (अ.स.) वाणी में अधिक स्पष्ट व प्रभावी थे", "समुद्री यात्रा जानते थे", "मिस्र के शासक थे")
            ),
            0,
            langMap(
                "Quran 28:34 states: 'And my brother Harun is more fluent than me in tongue, so send him with me as a support.'",
                "القصص 28:34: میرا بھائی ہارونؑ مجھ سے زیادہ فصیح اللسان ہے۔",
                "القصص 28:34: وأخي هارون هو أفصح مني لساناً فأرسله معي ردءاً يصدقني.",
                "क़ुरआन 28:34 में उनकी वाक्पटुता का स्पष्ट उल्लेख है।"
            )
        )
    ),
    ProphetStory(
        id = "dhulkifl",
        orderNumber = 16,
        arabicName = "ذو الكفل عليه السلام",
        names = langMap("Dhul-Kifl (AS)", "حضرت ذوالکفل علیہ السلام", "ذو الكفل عليه السلام", "हज़रत ज़ुल-किफ़्ल (अ.स.)"),
        titles = langMap("Steadfast Prophet Among the Best", "صبر و صلاح والے نبی", "من الصابرين الأخيار", "धैर्यवान व श्रेष्ठ नबियों में से"),
        category = ProphetCategory.CONCISE_MENTION,
        summaryIntro = langMap(
            "Explicitly honored in the Quran alongside Ismail and Idris among the steadfast (As-Sabirin) and alongside Ismail and Al-Yasa among the best of the chosen (Al-Akhyar) — Quran 21:85-86, 38:48.",
            "قرآن کریم میں حضرت اسماعیلؑ اور حضرت ادریسؑ کے ساتھ صابروں میں، اور حضرت الیسعؑ کے ساتھ بہترین برگزیدہ ہستیوں میں شمار فرمائے گئے ہیں (21:85-86، 38:48)۔",
            "ذُكر في القرآن الكريم في موضعين فقط (الأنبياء 21:85-86 وص 38:48) مع إسماعيل وإدريس واليسع بالصبر والصلاح والاصطفاء من الأخيار.",
            "क़ुरआन में उनका संक्षिप्त किंतु अत्यंत सम्माननीय उल्लेख इस्माईल, इदरीस और अल-यसा (अ.स.) के साथ 'अत्यंत धैर्यवान व श्रेष्ठतम' लोगों में हुआ है (21:85-86, 38:48)।"
        ),
        detailedIntro = langMap(
            "The Quran mentions Dhul-Kifl (AS) twice, pairing him with prophets of extraordinary patience and righteousness (21:85-86, 38:48). Because revelation gives no biographical details of his city, tribe, or specific miracles, and early commentators differed on whether Dhul-Kifl was his title for fulfilling great responsibilities or his personal name, faithful scholarship preserves these boundaries strictly without turning unverified stories into certainty.",
            "قرآن مجید میں آپؑ کا ذکر دو مقامات (21:85-86، 38:48) پر انتہائی اختصار مگر غیر معمولی تعظیم کے ساتھ 'صابرین' اور 'اخیار' میں آیا ہے۔ مفسرین نے 'ذوالکفل' کے لقب اور نسب پر مختلف آراء بیان کی ہیں، مگر قرآن و صحیح حدیث میں مزید تفصیل ثابت نہ ہونے کے باعث غیر مصدقہ واقعات کو یقینی بنا کر پیش نہیں کیا جاتا۔",
            "لم يذكر القرآن سوى ثنائه العظيم على ذي الكفل بالصبر والدخول في رحمة الله وأنه من الصالحين والأخيار (21:85-86، 38:48). ومع تعدد أقوال المفسرين في سبب تسميته ونسبه، فإنه لا يُجزم بتفاصيل تاريخية لم يثبت بها نص صريح.",
            "क़ुरआन में उन्हें केवल दो स्थानों (21:85-86, 38:48) पर अत्यंत सम्मान से धैर्यवान व श्रेष्ठ लोगों में गिना गया है। उनकी बस्ती या विशिष्ट घटनाओं पर अपुष्ट विवरणों से बचते हुए यही प्रामाणिक सीमा सुरक्षित रखी गई है।"
        ),
        quranicContext = langMap(
            "Mentioned concisely twice in the Quran: Surah Al-Anbiya (21:85-86) and Surah Sad (38:48).",
            "قرآن میں صرف دو بار ذکر آیا ہے: سورۃ الانبیاء (21:85-86) اور سورۃ ص (38:48)۔",
            "ذُكر مرتين بإيجاز في القرآن: الأنبياء (21:85-86) وص (38:48).",
            "क़ुरआन में केवल दो बार उल्लेख: सूरह अल-अंबिया (21:85-86) और सूरह साद (38:48)।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Commended alongside Ismail and Idris as people of steadfast patience (21:85).",
                "Admitted into Allah's mercy among the righteous — As-Salihin (21:86).",
                "Remembered alongside Ismail and Al-Yasa as 'among the best of the chosen' — min al-akhyar (38:48)."
            ),
            listOf(
                "حضرت اسماعیلؑ اور ادریسؑ کے ساتھ 'صابرین' میں شمولیت (21:85)۔",
                "رحمتِ الٰہی میں داخل اور صالحین میں شمار ہونا (21:86)۔",
                "حضرت الیسعؑ اور اسماعیلؑ کے ساتھ 'من الاخیار' (برگزیدہ ترین) کا لقب (38:48)۔"
            ),
            listOf(
                "الثناء عليه مع إسماعيل وإدريس بالصبر (الأنبياء 21:85).",
                "إدخاله في رحمة الله لأنه من الصالحين (الأنبياء 21:86).",
                "ذكره مع إسماعيل واليسع بقوله: وكل من الأخيار (ص 38:48)."
            ),
            listOf(
                "इस्माईल व इदरीस (अ.स.) संग धैर्यवानों में उल्लेख (21:85)।",
                "नेक (सालिहीन) होने के नाते अल्लाह की दया में प्रवेश (21:86)।",
                "इस्माईल व अल-यसा (अ.स.) संग 'सर्वश्रेष्ठ चुनिंदा' बताया जाना (38:48)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Dhul-Kifl (AS); Quran specifies neither a city nor specific companions."),
            listOf("حضرت ذوالکفلؑ؛ قرآن مجید میں کسی شہر یا ساتھیوں کا نام متعین نہیں ہے۔"),
            listOf("ذو الكفل عليه السلام؛ ولم يرد في القرآن تحديد لمكان أو قوم."),
            listOf("हज़रत ज़ुल-किफ़्ल (अ.स.); क़ुरआन में किसी नगर या साथी का नाम निर्दिष्ट नहीं है।")
        ),
        verseRefs = listOf(
            ProphetVerseRef(21, 85, 86, langMap("Patience & Righteousness", "صبر اور صالحیت", "الصبر والصلاح", "धैर्य व नेकी")),
            ProphetVerseRef(38, 48, 48, langMap("Among the Best (Min al-Akhyar)", "برگزیدہ ترین ہستیاں", "كلٌّ من الأخيار", "सर्वश्रेष्ठ चुनिंदा"))
        ),
        hadithRefs = emptyList(),
        lessons = listLangMap(
            listOf(
                "Honor in Allah's sight comes through steadfast patience and righteous character rather than lengthy fame.",
                "Authentic Islamic history distinguishes clearly between verified revelation and unverified historical speculation."
            ),
            listOf(
                "اللہ کے نزدیک عظمت شہرت سے نہیں بلکہ صبر اور صلاحِ عمل سے ملتی ہے۔",
                "مستند دینی علم میں وحی کی حدود کا احترام اور غیر ثابت روایات سے اجتناب بنیادی اصول ہے۔"
            ),
            listOf(
                "القدر عند الله بالصبر والصلاح لا بكثرة القصص المروية.",
                "الصدق العلمي يقتضي الوقوف عند النصوص الثابتة وعدم ترويج الإسرائيليات."
            ),
            listOf(
                "अल्लाह के निकट सम्मान प्रसिद्धि से नहीं, धैर्य और नेक आचरण से मिलता है।",
                "प्रामाणिक इस्लामी ज्ञान प्रमाणित वह्य से आगे अटकलें नहीं लगाता।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Intentionally concise, restricted strictly to Quran 21:85-86 and 38:48. Scholarly discussions on his historical identity or reason for the title 'Dhul-Kifl' do not rest on a Marfu' Sahih hadith and therefore are not presented as definitive facts.",
            "مکمل احتیاط کے ساتھ صرف قرآن (21:85-86، 38:48) تک محدود؛ مفسرین کے درمیان اسم و لقب کی تفصیلات میں اختلاف ہے اور کوئی صریح صحیح مرفوع حدیث موجود نہیں۔",
            "مقتصر بأمانة على الآيات الصريحة (21:85-86، 38:48)؛ ولا توجد رواية مرفوعة صحيحة تفصل سيرته، فلا يُجزم بأقوال غير ثابتة.",
            "पूर्णतः क़ुरआन 21:85-86 व 38:48 तक सीमित; नाम के अर्थ व ऐतिहासिक पहचान पर कोई मर्फ़ू सहीह हदीस न होने से अटकलें शामिल नहीं की गई हैं।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Alongside patience and righteousness, how does Surah Sad (38:48) honor Dhul-Kifl (AS)?",
                "سورۃ ص (38:48) میں حضرت ذوالکفلؑ کی کس حیثیت سے تعریف کی گئی ہے؟",
                "بمَ أثنى الله على ذي الكفل وإسماعيل واليسع في سورة ص (38:48)؟",
                "सूरह साद (38:48) में ज़ुल-किफ़्ल (अ.स.) की क्या प्रशंसा की गई है?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Each of them is among the best / chosen righteous (Min al-Akhyar)", "He built Babylon", "He parted the Euphrates"),
                AppLanguage.URDU to listOf("یہ سب بہترین برگزیدہ بندوں (من الاخیار) میں سے ہیں", "انہوں نے بابل تعمیر کیا", "دریائے فرات کو دو حصوں میں تقسیم کیا"),
                AppLanguage.ARABIC to listOf("أن كلاً منهم من الأخيار", "بنى بابل", "شق نهر الفرات"),
                AppLanguage.HINDI to listOf("वे सभी सर्वश्रेष्ठ चुनिंदा लोगों (मिनल-अख़यार) में से हैं", "उन्होंने बेबीलोन बसाया", "फ़रात नदी बाँटी")
            ),
            0,
            langMap(
                "Quran 38:48 affirms: 'And remember Ismail, Al-Yasa, and Dhul-Kifl, and all are among the best.'",
                "سورۃ ص 38:48 میں فرمایا: اور یہ سب بہترین لوگوں (من الاخیار) میں سے ہیں۔",
                "ص 38:48: وكلٌّ من الأخيار.",
                "क़ुरआन 38:48: 'वे सभी सर्वश्रेष्ठ चुनिंदा लोगों में से हैं।'"
            )
        )
    ),
    ProphetStory(
        id = "dawud",
        orderNumber = 17,
        arabicName = "داود عليه السلام",
        names = langMap("Dawud (AS)", "حضرت داؤد علیہ السلام", "داود عليه السلام", "हज़रत दाऊद (अ.स.)"),
        titles = langMap("Prophet-King & Recipient of Zabur", "نبی، بادشاہ اور صاحبِ زبور", "النبي الملك وصاحب الزبور", "नबी, सम्राट व ज़बूर प्राप्त"),
        category = ProphetCategory.BANI_ISRAIL_ERA,
        summaryIntro = langMap(
            "Blessed with prophethood, righteous kingship, and the Book of Zabur. Mountains and birds echoed his glorification of Allah, iron softened in his hands, and he judged with justice and repentance.",
            "آپ کو نبوت، عادلانہ بادشاہت اور کتاب 'زبور' عطا ہوئی۔ پہاڑ اور پرندے آپ کے ساتھ تسبیح کرتے تھے، لوہا آپ کے ہاتھ میں نرم ہو جاتا اور آپ انصاف و رجوع الی اللہ کا نمونہ تھے۔",
            "آتاه الله النبوة والملك والزبور، وسخّر الجبال والطير يسبحن معه، وألان له الحديد، وعلّمه فصل الخطاب والعدل.",
            "उन्हें नबूवत, न्यायपूर्ण राज्य व ज़बूर प्रदान की गई। पर्वत व पक्षी उनके साथ तस्बीह करते, लोहा उनके हाथों में नरम हो जाता और वे न्याय व तौबा की मिसाल थे।"
        ),
        detailedIntro = langMap(
            "As a courageous young believer in Talut's army, Dawud (AS) defeated Jalut by Allah's leave (2:251) and later received sovereignty, wisdom, decisive judgment, and the Zabur (4:163, 38:20). Allah made iron pliable for him to craft protective armor while earning his own livelihood (34:10-11, Bukhari 2072), and taught him humble repentance (38:24) and gratitude across his household (34:13).",
            "طالوت کے لشکر میں جالوت کا مقابلہ کر کے فتح پانے کے بعد (2:251) اللہ نے آپ کو بادشاہت، حکمت، فصل الخطاب اور زبور عطا فرمائی (38:20)۔ پہاڑ اور پرندے آپ کے ساتھ حمد پڑھتے، لوہا موم کی طرح نرم ہو جاتا جس سے آپ زرہیں بنا کر اپنی کمائی سے رزق کھاتے (34:10-11، بخاری 2072) اور ہر حال میں اللہ کی طرف رجوع فرماتے (38:24)۔",
            "قتل جالوت بإذن الله فآتاه الله الملك والحكمة والزبور (البقرة 2:251، ص 38:20). كانت الجبال والطير تسبح معه، وألان الله له الحديد ليصنع الدروع السابغات ويأكل من عمل يده (سبأ 34:10-11، البخاري 2072)، وعُرف بكثرة الأوبة والاستغفار.",
            "तालूत की सेना में जालूत को पराजित कर (2:251) अल्लाह ने उन्हें राज्य, हिकमत व ज़बूर दी (38:20)। पर्वत व पक्षी उनके साथ अल्लाह की प्रशंसा करते, लोहा नरम हो जाता जिससे वे अपने हाथों की कमाई से आजीविका कमाते (34:10-11, बुख़ारी 2072)।"
        ),
        quranicContext = langMap(
            "Recounted in Al-Baqarah (2:251), Al-Anbiya (21:78-80), Saba (34:10-11), and Sad (38:17-26).",
            "البقرہ (2:251)، الانبیاء (21:78-80)، سبا (34:10-11) اور ص (38:17-26) میں مفصل بیان ہے۔",
            "في البقرة (2:251) والأنبياء (21:78-80) وسبأ (34:10-11) وص (38:17-26).",
            "बक़रा (2:251), अंबिया (21:78-80), सबा (34:10-11) और साद (38:17-26) में वर्णित।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Defeat of Jalut and bestowal of kingship and wisdom (2:251).",
                "Revelation of the Zabur; mountains and birds joining in glorification (21:79, 34:10).",
                "Iron made pliable for crafting armor; dignity of manual labor (21:80, 34:10-11).",
                "Seeking forgiveness and falling in prostration (Sajdah), appointed vicegerent to judge in truth (38:24-26)."
            ),
            listOf(
                "جالوت پر فتح اور حکومت و حکمت کی عطا (2:251)۔",
                "نزولِ زبور اور پہاڑوں و پرندوں کی ہم آہنگ تسبیح (21:79، 34:10)۔",
                "لوہے کا نرم ہونا اور محنت سے رزق کمانا (21:80، 34:10-11)۔",
                "سجدہ و استغفار اور حق کے ساتھ فیصلہ کرنے کا حکم (38:24-26)۔"
            ),
            listOf(
                "قتل جالوت وإيتاؤه الملك والحكمة (البقرة 2:251).",
                "إنزال الزبور وتسبيح الجبال والطير معه (الأنبياء 21:79، سبأ 34:10).",
                "إلانة الحديد وصناعة الدروع والأكل من كسب اليد (سبأ 34:10-11).",
                "الاستغفار والسجود والأمر بالحكم بالعدل (ص 38:24-26)."
            ),
            listOf(
                "जालूत पर विजय व राज्य-हिकमत की प्राप्ति (2:251)।",
                "ज़बूर का अवतरण तथा पर्वतों-पक्षियों की तस्बीह (21:79, 34:10)।",
                "लोहे का नरम होना और हाथ की मेहनत से कमाना (34:10-11)।",
                "क्षमा-याचना, सजदा व न्याय से निर्णय का आदेश (38:24-26)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Dawud (AS), son Sulayman (AS), Talut, Jalut", "Jerusalem / Sacred Land"),
            listOf("حضرت داؤدؑ، فرزند سلیمانؑ، طالوت اور جالوت", "ارضِ مقدس / بیت المقدس"),
            listOf("داود وابنه سليمان وطالوت وجالوت", "الأرض المقدسة"),
            listOf("दाऊद (अ.स.), पुत्र सुलैमान, तालूत व जालूत", "पवित्र भूमि (यरूशलेम)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(34, 10, 11, langMap("Iron Softened & Gratitude", "لوہے کی نرمی اور شکرِ الٰہی", "إلانة الحديد والعمل بالشكر", "लोहा नरम होना व कृतज्ञता")),
            ProphetVerseRef(38, 24, 26, langMap("Repentance & Just Governance", "توبہ اور عدل و انصاف", "الأوبة والحكم بالحق", "तौबा व न्यायपूर्ण शासन"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 2072",
                "https://sunnah.com/bukhari:2072",
                langMap(
                    "The Prophet ﷺ said: 'No one has ever eaten better food than what one earns with his own hands, and Allah's Prophet Dawud used to eat from the earnings of his hands.'",
                    "صحیح بخاری (2072): کسی نے اپنے ہاتھ کی کمائی سے بہتر کھانا نہیں کھایا، اور اللہ کے نبی داؤدؑ اپنے ہاتھ کی کمائی کھاتے تھے۔",
                    "صحيح البخاري (2072): ما أكل أحد طعاماً قط خيراً من أن يأكل من عمل يده، وإن نبي الله داود كان يأكل من عمل يده.",
                    "सहीह बुख़ारी 2072: दाऊद (अ.स.) अपने हाथों की कमाई खाते थे।"
                )
            ),
            ProphetHadithRef(
                "Sahih al-Bukhari 1131",
                "https://sunnah.com/bukhari:1131",
                langMap(
                    "The most beloved fasting and night prayer to Allah is the pattern of Dawud (AS) — fasting alternate days and praying in the stillness of the night.",
                    "صحیح بخاری (1131): اللہ کو سب سے محبوب روزہ اور تہجد حضرت داؤدؑ کا طریقہ ہے۔",
                    "صحيح البخاري (1131): أحب الصيام وأحب الصلاة إلى الله صيام داود وصلاته.",
                    "सहीह बुख़ारी 1131: अल्लाह को दाऊद (अ.स.) के रोज़े व रात की इबादत अत्यंत प्रिय हैं।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Power and kingship never excuse a believer from honest manual work and humble worship.",
                "Just leadership rejects personal bias and remembers accountability before Allah (38:26)."
            ),
            listOf(
                "بادشاہت کے باوجود حلال محنت، عاجزی اور تہجد کا التزام انبیاء کا طریقہ ہے۔",
                "انصاف کرنے والے کو ذاتی خواہش کے بجائے حق اور جواب دہی یاد رکھنی چاہیے (38:26)۔"
            ),
            listOf(
                "عظمة الملك لا تمنع من الكسب الحلال باليد وقيام الليل.",
                "الحكم بالحق والابتعاد عن الهوى أساس العدل (ص 38:26)."
            ),
            listOf(
                "शासन और पद के बावजूद ईमानदार श्रम व विनम्र इबादत सर्वश्रेष्ठ हैं।",
                "न्याय में व्यक्तिगत झुकाव त्यागकर अल्लाह के प्रति जवाबदेही याद रखनी चाहिए (38:26)।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Grounded directly in Quran 2:251, 21:78-80, 34:10-13, 38:17-26 and Sahih al-Bukhari 2072 & 1131.",
            "قرآن مجید اور صحیح بخاری (2072، 1131) سے مکمل مستند۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (2072، 1131).",
            "क़ुरआन व सहीह बुख़ारी (2072, 1131) से प्रमाणित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Which divine scripture was revealed to Prophet Dawud (AS)?",
                "حضرت داؤدؑ پر کون سی آسمانی کتاب نازل فرمائی گئی؟",
                "ما الكتاب السماوي الذي أُنزل على داود عليه السلام؟",
                "हज़रत दाऊद (अ.स.) पर कौन-सी आसमानी किताब उतारी गई?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Az-Zabur (Psalms)", "At-Tawrah (Torah)", "Al-Injil (Gospel)"),
                AppLanguage.URDU to listOf("زبور", "تورات", "انجیل"),
                AppLanguage.ARABIC to listOf("الزبور", "التوراة", "الإنجيل"),
                AppLanguage.HINDI to listOf("ज़बूर", "तौरात", "इंजील")
            ),
            0,
            langMap(
                "Quran 4:163 and 17:55 affirm: 'And to Dawud We gave the Zabur.'",
                "النساء 4:163 اور بنی اسرائیل 17:55: اور ہم نے داؤدؑ کو زبور عطا فرمائی۔",
                "النساء 4:163 والإسراء 17:55: وآتينا داود زبوراً.",
                "क़ुरआन 4:163 व 17:55 में दाऊद (अ.स.) को ज़बूर दिए जाने का उल्लेख है।"
            )
        )
    ),
    ProphetStory(
        id = "sulayman",
        orderNumber = 18,
        arabicName = "سليمان عليه السلام",
        names = langMap("Sulayman (AS)", "حضرت سلیمان علیہ السلام", "سليمان عليه السلام", "हज़रत सुलैमान (अ.स.)"),
        titles = langMap("Prophet-King of Gratitude & Wisdom", "شکر گزار نبی و عظیم فرمانروا", "النبي الملك الشكور", "कृतज्ञ व बुद्धिमान नबी-सम्राट"),
        category = ProphetCategory.BANI_ISRAIL_ERA,
        summaryIntro = langMap(
            "Son of Dawud (AS), gifted unmatched wisdom, understanding of the speech of birds and ants, and dominion over the wind and jinn — yet always crediting every blessing to Allah's grace as 'a test of gratitude.'",
            "حضرت داؤدؑ کے جانشین، جنہیں بے مثال حکمت، پرندوں و چیونٹیوں کی بولی کی سمجھ، ہوا اور جنات کی تسخیر عطا ہوئی، مگر ہر نعمت کو اللہ کا فضل اور شکر کا امتحان قرار دیا۔",
            "وارث داود عليهما السلام، أوتي الحكمة وفهم منطق الطير والنمل وتسخير الريح والجن، وكان مع ملكه العظيم شاكراً متواضعاً: هذا من فضل ربي ليبلوني أأشكر أم أكفر.",
            "दाऊद (अ.स.) के पुत्र व उत्तराधिकारी, जिन्हें अद्वितीय बुद्धिमत्ता, पक्षियों-चींटियों की भाषा का ज्ञान और वायु व जिन्नों पर अधिकार मिला, फिर भी सबकुछ अल्लाह की कृपा की परीक्षा माना।"
        ),
        detailedIntro = langMap(
            "Inheriting wisdom and judgment from Dawud (27:16), Sulayman (AS) understood the speech of birds and smiled gratefully at the warning of a tiny ant so his army would not crush the colony (27:18-19). Through the report of the hoopoe (Hudhud), he invited Queen of Sheba (Saba) and her people from sun worship to worshipping Allah alone; upon seeing his miraculous throne, she submitted sincerely with Sulayman to Allah, Lord of the Worlds (27:20-44). Allah explicitly cleared Sulayman of sorcery in Quran 2:102.",
            "آپ نے حضرت داؤدؑ سے علم و نبوت پائی (27:16)۔ چیونٹی کی بات سن کر مسکرا دیے اور شکر کی دعا مانگی (27:18-19)۔ ہدہد کی خبر پر ملکہ سبا (بلقیس) کو سورج پرستی چھوڑ کر توحید کی دعوت دی اور وہ اللہ رب العالمین پر ایمان لے آئیں (27:20-44)۔ قرآنِ مجید (2:102) نے صراحت سے حضرت سلیمانؑ کو جادو سے بری قرار دیا۔",
            "ورث داود في النبوة والحكمة (النمل 27:16)، وتبسم من قول النملة ودعا أن يوزعه الله شكر نعمته (27:18-19)، ودعا ملكة سبأ إلى التوحيد فأسلمت مع سليمان لله رب العالمين (27:20-44). وقد برّأه الله في القرآن من السحر (البقرة 2:102).",
            "दाऊद (अ.स.) से हिकमत पाई (27:16), चींटी की बात सुनकर कृतज्ञता की दुआ की (27:18-19), हुदहुद के माध्यम से सबा की रानी को तौहीद की दावत दी जिससे वह अल्लाह पर ईमान लाई (27:20-44)। क़ुरआन 2:102 में उन्हें जादू से पूरी तरह निर्दोष घोषित किया गया है।"
        ),
        quranicContext = langMap(
            "Prominently featured in Al-Baqarah (2:102), Al-Anbiya (21:78-82), An-Naml (27:15-44), Saba (34:12-14), and Sad (38:30-40).",
            "البقرہ (2:102)، الانبیاء (21:78-82)، النمل (27:15-44)، سبا (34:12-14) اور ص (38:30-40) میں مفصل ذکر ہے۔",
            "في البقرة (2:102) والأنبياء (21:78-82) والنمل (27:15-44) وسبأ (34:12-14) وص (38:30-40).",
            "बक़रा (2:102), अंबिया (21:78-82), नम्ल (27:15-44), सबा (34:12-14) और साद (38:30-40) में विस्तृत वर्णन।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Gifted wise judgment alongside Dawud and taught the language of birds (21:78-79, 27:16).",
                "Attentive mercy toward the Valley of Ants and prayer for lifelong gratitude (27:18-19).",
                "Calling Queen of Sheba from sun worship to Tawhid and acknowledging blessings as a test (27:22-44).",
                "Cleared in Quran 2:102 from all accusations of magic; humble passing showing only Allah knows the unseen (34:14)."
            ),
            listOf(
                "حکمت و قضاوت اور پرندوں کی زبان کا علم (21:78-79، 27:16)۔",
                "وادیِ نمل میں چیونٹیوں پر رحم اور شکرگزاری کی دعا (27:18-19)۔",
                "ملکہ سبا کو توحید کی دعوت اور نعمتوں کو آزمائش سمجھنا (27:22-44)۔",
                "سورۃ البقرہ 2:102 میں جادو کے الزام سے براءت اور علمِ غیب کا اللہ کے لیے خاص ہونا (34:14)۔"
            ),
            listOf(
                "النبوة والحكمة وتعليم منطق الطير (الأنبياء 21:78-79، النمل 27:16).",
                "الرحمة بالنمل والدعاء بشكر النعمة (النمل 27:18-19).",
                "دعوة ملكة سبأ إلى التوحيد وإسلامها لله (النمل 27:22-44).",
                "تبرئته من الكفر والسحر (البقرة 2:102)، وبيان اختصاص الله بعلم الغيب عند وفاته (سبأ 34:14)."
            ),
            listOf(
                "न्याय की समझ और पक्षियों की भाषा का ज्ञान (21:78-79, 27:16)।",
                "चींटियों पर दया और कृतज्ञता की दुआ (27:18-19)।",
                "सबा की रानी को तौहीद का संदेश और ईमान लाना (27:22-44)।",
                "सूरह बक़रा 2:102 में जादू के आरोप से निर्दोष सिद्ध होना (34:14)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Sulayman (AS), Dawud (AS), Queen of Sheba (Saba), Asif / person with knowledge of the Book", "Jerusalem & Kingdom of Saba (Yemen)"),
            listOf("حضرت سلیمانؑ، حضرت داؤدؑ، ملکہ سبا", "بیت المقدس اور سبا (یمن)"),
            listOf("سليمان وأبوه داود وملكة سبأ", "بيت المقدس ومملكة سبأ"),
            listOf("सुलैमान (अ.स.), दाऊद (अ.स.), सबा की रानी", "यरूशलेम व सबा (यमन)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(27, 16, 19, langMap("Valley of Ants & Prayer of Gratitude", "وادی نمل اور دعائے شکر", "وادي النمل وشكر النعمة", "चींटी घाटी व कृतज्ञता की दुआ")),
            ProphetVerseRef(2, 102, 102, langMap("Sulayman Cleared of Disbelief/Magic", "سلیمانؑ کی براءت", "تبرئة سليمان من السحر", "जादू के आरोप से मुक्ति"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3423",
                "https://sunnah.com/bukhari:3423",
                langMap(
                    "Illustrates an example of Sulayman's wise judicial insight compared to his father Dawud's ruling regarding a disputed child.",
                    "صحیح بخاری (3423) میں دو خواتین اور بچے کے مقدمے میں حضرت سلیمانؑ کے حکیمانہ فیصلے کی روایت۔",
                    "في صحيح البخاري (3423) قصة حكم سليمان الحكيم في قضية الطفل المتنازع عليه.",
                    "सहीह बुख़ारी 3423 में विवादित बच्चे के मामले में सुलैमान (अ.स.) के न्याय की प्रसिद्ध हदीस।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Worldly blessings are a trust testing gratitude, not an excuse for pride (27:40).",
                "True strength shows tenderness even toward the smallest creatures like ants."
            ),
            listOf(
                "بڑی سے بڑی نعمت شکر اور عاجزی کا امتحان ہوتی ہے (27:40)۔",
                "طاقت کا حسن یہ ہے کہ چھوٹی سے چھوٹی مخلوق پر بھی رحم کیا جائے۔"
            ),
            listOf(
                "النعم اختبار للشكر وليست مدعاة للكبر (النمل 27:40).",
                "الرحمة بالضعفاء وأصغر الكائنات خلق نبوي رفيع."
            ),
            listOf(
                "हर सुख-समृद्धि कृतज्ञता की परीक्षा है, घमंड का कारण नहीं (27:40)।",
                "सच्ची शक्ति सबसे छोटे प्राणी पर भी दया करने में है।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(27, 19, 19, langMap("Inspire me to be grateful for Your favors", "رب اوزعنی ان اشکر نعمتک", "رب أوزعني أن أشكر نعمتك", "मुझे अपनी नेमतों का शुक्र करने की तौफ़ीक़ दे"))
        ),
        reliabilityNotes = langMap(
            "Anchored strictly in Quran Surahs 2, 21, 27, 34, 38 and Sahih al-Bukhari 3423.",
            "قرآن کریم اور صحیح بخاری (3423) سے مکمل طور پر مستند۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3423).",
            "क़ुरआन और सहीह बुख़ारी 3423 से सीधे प्रमाणित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "What did Sulayman (AS) pray immediately upon hearing the ant speak in Quran 27:19?",
                "قرآن (27:19) میں چیونٹی کی بات سن کر حضرت سلیمانؑ نے کیا دعا فرمائی؟",
                "بماذا دعا سليمان عليه السلام حين سمع قول النملة (النمل 27:19)؟",
                "क़ुरआन 27:19 में चींटी की बात सुनकर सुलैमान (अ.स.) ने क्या दुआ की?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("That Allah inspire him to be grateful and do righteous deeds", "That the wind stop forever", "For larger gold treasuries"),
                AppLanguage.URDU to listOf("نعمت کا شکر ادا کرنے اور نیک عمل کی توفیق مانگی", "ہوا ہمیشہ کے لیے رک جائے", "مزید سونے کے خزانے مانگے"),
                AppLanguage.ARABIC to listOf("أن يوزعه الله شكر نعمته والعمل الصالح", "أن تتوقف الريح", "زيادة خزائن الذهب"),
                AppLanguage.HINDI to listOf("अल्लाह की नेमतों का शुक्र व नेक कर्म करने की तौफ़ीक़ माँगी", "हवा हमेशा रुक जाए", "अधिक स्वर्ण ख़ज़ाने माँगे")
            ),
            0,
            langMap(
                "Quran 27:19 records his heartfelt prayer: 'My Lord, enable me to be grateful for Your favor which You have bestowed upon me and upon my parents...'",
                "النمل 27:19 میں آپ نے شکرِ نعمت اور عملِ صالح کی توفیق کی دعا مانگی۔",
                "النمل 27:19: رب أوزعني أن أشكر نعمتك التي أنعمت علي وعلى والدي وأن أعمل صالحاً ترضاه.",
                "क़ुरआन 27:19 में कृतज्ञता व नेक अमल की दुआ दर्ज है।"
            )
        )
    ),
    ProphetStory(
        id = "ilyas",
        orderNumber = 19,
        arabicName = "إلياس عليه السلام",
        names = langMap("Ilyas (AS)", "حضرت الیاس علیہ السلام", "إلياس عليه السلام", "हज़रत इल्यास (अ.स.)"),
        titles = langMap("Caller Back from Ba'l Idol Worship", "شرک و بت پرستی کے خلاف داعی", "الداعي إلى الله والناهي عن عبادة بعل", "बअल की मूर्तिपूजा से रोकने वाले नबी"),
        category = ProphetCategory.CONCISE_MENTION,
        summaryIntro = langMap(
            "Briefly yet powerfully portrayed in the Quran calling his people away from worshipping the idol Ba'l back to Allah, the Best of Creators, and greeted with eternal peace upon him (37:123-132).",
            "قرآن مجید میں آپ کا ذکر مختصر مگر نہایت مؤثر انداز میں آیا ہے کہ آپ نے اپنی قوم کو 'بعل' نامی بت کی پوجا چھوڑ کر احسن الخالقین کی عبادت کی دعوت دی، اور آپ پر سلامِ الٰہی نازل ہوا (37:123-132)۔",
            "ورد ذكره في موضعين من القرآن (الأنعام 6:85 والصافات 37:123-132)؛ دعا قومه إلى ترك عبادة صنم (بعل) وعبادة الله أحسن الخالقين، فسلّم الله عليه في العالمين.",
            "क़ुरआन में उनका संक्षिप्त पर प्रभावशाली उल्लेख है जहाँ उन्होंने क़ौम को 'बअल' मूर्ति की पूजा छोड़ सर्वोत्तम सृष्टिकर्ता अल्लाह की इबादत की ओर बुलाया (37:123-132)।"
        ),
        detailedIntro = langMap(
            "Quran 37:123-132 affirms that Ilyas (AS) was among the Messengers who confronted his people for worshipping Ba'l and forsaking Allah, the Lord of them and their forefathers. They rejected him except the sincere servants of Allah, and Allah left for him honorable praise among later generations: 'Peace be upon Ilyasin — indeed, Thus do We reward the doers of good' (37:130-131). Quran 6:85 also lists him among the righteous alongside Zakariya, Yahya, and Isa.",
            "سورۃ الصافات (37:123-132) میں صراحت ہے کہ حضرت الیاسؑ نے اپنی قوم کو بت 'بعل' چھوڑ کر اللہ کی عبادت کرنے کا پیغام دیا۔ مخلص بندوں کے سوا قوم نے جھٹلایا، اور اللہ نے بعد کی نسلوں میں آپ پر سلام 'سلام علی ال یاسین' باقی رکھا۔ سورۃ الانعام (6:85) میں آپ کو صالحین میں شمار کیا گیا ہے۔",
            "بيّنت سورة الصافات (37:123-132) رسالته لقومه وإنكاره لعبادتهم (بعلاً) وتركهم أحسن الخالقين، وخلّد الله ثناءه: سلام على إل ياسين. وذُكر في الأنعام (6:85) مع زكريا ويحيى وعيسى من الصالحين.",
            "सूरह साफ़्फ़ात (37:123-132) बताती है कि इल्यास (अ.स.) ने लोगों को 'बअल' की पूजा छोड़ने की दावत दी; अल्लाह ने आने वाली पीढ़ियों में उन पर सलाम बाक़ी रखा। अनआम 6:85 में भी वे नेक नबियों में गिने गए हैं।"
        ),
        quranicContext = langMap(
            "Mentioned concisely in two passages: Al-An'am (6:85) and As-Saffat (37:123-132).",
            "صرف دو مقامات پر ذکر ہے: الانعام (6:85) اور الصافات (37:123-132)۔",
            "ذُكر بإيجاز في موضعين فقط: الأنعام (6:85) والصافات (37:123-132).",
            "केवल दो स्थानों पर संक्षिप्त उल्लेख: अनआम (6:85) और साफ़्फ़ात (37:123-132)।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Sent as an upright Messenger calling people away from worshipping the idol Ba'l (37:123-126).",
                "Rejected by his people except sincere believing servants (37:127-128).",
                "Honored eternally with Allah's greeting of peace: 'Peace be upon Ilyasin' (37:129-132)."
            ),
            listOf(
                "رسول کی حیثیت سے 'بعل' کی بت پرستی کے خلاف دعوتِ توحید (37:123-126)۔",
                "مخلص بندوں کے سوا قوم کی طرف سے تکذیب (37:127-128)۔",
                "بعد کی امتوں میں ذکرِ خیر اور 'سلام علی ال یاسین' کا اعزاز (37:129-132)۔"
            ),
            listOf(
                "إرساله لدعوة قومه إلى ترك عبادة (بعل) (الصافات 37:123-126).",
                "تكذيب قومه إلا عباد الله المخلصين (37:127-128).",
                "تخليد الثناء عليه: سلام على إل ياسين (الصافات 37:129-132)."
            ),
            listOf(
                "'बअल' मूर्तिपूजा छोड़ तौहीद अपनाने की दावत (37:123-126)।",
                "सच्चे ईमानवालों के सिवा क़ौम द्वारा इनकार (37:127-128)।",
                "पीढ़ियों तक सम्मान व 'सलामुन अला इल्यासीन' की प्रशंसा (37:129-132)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Prophet Ilyas (AS) and his people (worshippers of Ba'l)"),
            listOf("حضرت الیاسؑ اور آپ کی قوم (جو بعل کی پرستش کرتی تھی)"),
            listOf("إلياس عليه السلام وقومه الذين عبدوا بعلاً"),
            listOf("हज़रत इल्यास (अ.स.) व उनकी क़ौम (बअल के उपासक)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(37, 123, 132, langMap("Message Against Ba'l & Eternal Peace", "بعل سے منع اور سلامِ الٰہی", "النهي عن عبادة بعل والسلام عليه", "बअल से रोक व शांति का संदेश")),
            ProphetVerseRef(6, 85, 85, langMap("Among the Righteous", "صالحین انبیاء میں ذکر", "من الصالحين", "नेक नबियों में उल्लेख"))
        ),
        hadithRefs = emptyList(),
        lessons = listLangMap(
            listOf(
                "Standing courageously for pure Tawhid leaves a lasting legacy of peace and honor (37:129-131).",
                "A small faithful minority ('Ibad Allah al-Mukhlasin') remains protected amidst widespread rejection."
            ),
            listOf(
                "خالص توحید کی دعوت انسان کا ذکرِ خیر ہمیشہ زندہ رکھتی ہے (37:129-131)۔",
                "اکثریت کے انکار میں بھی مخلص بندے اللہ کی حفاظت میں رہتے ہیں۔"
            ),
            listOf(
                "الثبات على التوحيد يخلّد الذكر الحسن والسلام (الصافات 37:129-131).",
                "الإخلاص ينجّي صاحبه ولو كذّب الأكثرون."
            ),
            listOf(
                "तौहीद पर दृढ़ रहना स्थायी शांति व सम्मान दिलाता है (37:129-131)।",
                "बहुसंख्यक इनकार के बीच भी निष्ठावान लोग सुरक्षित रहते हैं।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Kept intentionally concise, matching Quran 6:85 and 37:123-132. Later legends or unauthenticated Isra'iliyyat regarding his disappearance or travels are not presented as confirmed facts.",
            "مکمل طور پر قرآن (6:85، 37:123-132) تک محدود؛ غیبی یا غیر مستند قصوں کو یقینی قرار نہیں دیا گیا۔",
            "مقتصر على النصوص القرآنية الصريحة (الأنعام 6:85، الصافات 37:123-132) دون سرد روايات غير موثقة.",
            "पूर्णतः क़ुरआन 6:85 व 37:123-132 तक सीमित; अपुष्ट चमत्कारी कथाएँ नहीं जोड़ी गई हैं।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "In Quran 37:125, which false idol did Ilyas (AS) rebuke his people for calling upon?",
                "قرآن (37:125) کے مطابق حضرت الیاسؑ نے اپنی قوم کو کس بت کو پکارنے سے منع فرمایا؟",
                "ما اسم الصنم الذي أنكر إلياس على قومه دعاءه (الصافات 37:125)؟",
                "क़ुरआन 37:125 में इल्यास (अ.स.) ने किस मूर्ति की पूजा न करने को कहा?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Ba'l", "Yaghuth", "Manat"),
                AppLanguage.URDU to listOf("بعل", "یغوث", "منات"),
                AppLanguage.ARABIC to listOf("بعل", "يغوث", "مناة"),
                AppLanguage.HINDI to listOf("बअल", "यग़ूस", "मनात")
            ),
            0,
            langMap(
                "Quran 37:125 asks: 'Do you call upon Ba'l and leave the best of creators?'",
                "الصافات 37:125: کیا تم بعل کو پکارتے ہو اور بہترین خالق کو چھوڑ دیتے ہو؟",
                "الصافات 37:125: أتدعون بعلاً وتذرون أحسن الخالقين.",
                "क़ुरआन 37:125 में 'बअल' की पूजा से रोका गया है।"
            )
        )
    ),
    ProphetStory(
        id = "alyasa",
        orderNumber = 20,
        arabicName = "اليسع عليه السلام",
        names = langMap("Al-Yasa (AS)", "حضرت الیسع علیہ السلام", "اليسع عليه السلام", "हज़रत अल-यसा (अ.स.)"),
        titles = langMap("Prophet Favored Above the Worlds", "فضیلت یافتہ برگزیدہ نبی", "المفضَّل على العالمين ومن الأخيار", "जगत में श्रेष्ठ व चुनिंदा नबी"),
        category = ProphetCategory.CONCISE_MENTION,
        summaryIntro = langMap(
            "Mentioned by name twice in the Quran (6:86, 38:48) among the messengers favored over the worlds and numbered among the best and chosen servants of Allah.",
            "قرآن کریم میں آپ کا نام مبارک دو مقامات (6:86، 38:48) پر آیا ہے جہاں آپ کو عالمین پر فضیلت یافتہ اور بہترین برگزیدہ ہستیوں میں شمار فرمایا گیا ہے۔",
            "ذُكر اسمه في القرآن مرتين فقط (الأنعام 6:86 وص 38:48) مع أنبياء فضّلهم الله على العالمين ووصفهم بأنهم من الأخيار.",
            "क़ुरआन में उनका नाम दो बार आया है (6:86, 38:48), जहाँ उन्हें जगत पर श्रेष्ठ व अल्लाह के सर्वश्रेष्ठ चुनिंदा लोगों में गिना गया है।"
        ),
        detailedIntro = langMap(
            "The Quran includes Al-Yasa (AS) directly in Surah Al-An'am alongside Ismail, Yunus, and Lut as prophets each favored above the worlds and guided to the straight path (6:86-87), and in Surah Sad alongside Ismail and Dhul-Kifl as being among the best — min al-akhyar (38:48). Because revelation does not narrate extended events or miracles for him, faithful Islamic scholarship reports strictly what the Quran confirms without unverified extrapolation.",
            "قرآنِ مجید میں سورۃ الانعام (6:86-87) میں حضرت اسماعیلؑ، یونسؑ اور لوطؑ کے ساتھ آپؑ کو عالمین پر فضیلت اور صراطِ مستقیم پر ہدایت یافتہ قرار دیا گیا، اور سورۃ ص (38:48) میں 'من الاخیار' فرمایا گیا۔ وحی میں چونکہ مفصل واقعات بیان نہیں ہوئے اس لیے مستند علمی طریقہ یہی ہے کہ صرف ثابت شدہ ذکر پر اکتفا کیا جائے۔",
            "اكتفى القرآن الكريم في ذكر اليسع عليه السلام بتعظيم قدره وتفضيله على العالمين وهديه إلى صراط مستقيم (الأنعام 6:86-87) وأنه من الأخيار (ص 38:48)؛ ولعدم ثبوت تفاصيل سردية إضافية في الوحي يُكتفى بما ثبت يقيناً.",
            "सूरह अल-अनाम (6:86-87) और सूरह साद (38:48) में उन्हें सीधे मार्ग पर चलने वाला और सर्वश्रेष्ठ (मिनल-अख़यार) बताया गया है। इसके अतिरिक्त कोई विस्तृत घटना क़ुरआन या सहीह हदीस में न होने से अपुष्ट विवरण नहीं जोड़े जाते।"
        ),
        quranicContext = langMap(
            "Mentioned strictly in two Quranic passages: Surah Al-An'am (6:86) and Surah Sad (38:48).",
            "قرآن میں صرف دو مقامات پر ذکر ہے: سورۃ الانعام (6:86) اور سورۃ ص (38:48)۔",
            "ذُكر في موضعين فقط: سورة الأنعام (6:86) وسورة ص (38:48).",
            "केवल दो स्थानों पर उल्लेख: सूरह अल-अनाम (6:86) और सूरह साद (38:48)।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Honored among the messengers favored by Allah over all the worlds (6:86).",
                "Chosen and guided onto a straight path (6:87).",
                "Remembered alongside Ismail and Dhul-Kifl as 'among the best' (38:48)."
            ),
            listOf(
                "عالمین پر فضیلت پانے والے انبیاء میں شمار (6:86)۔",
                "برگزیدگی اور صراطِ مستقیم کی ہدایت (6:87)۔",
                "حضرت اسماعیلؑ اور ذوالکفلؑ کے ساتھ 'من الاخیار' کے طور پر تذکرہ (38:48)۔"
            ),
            listOf(
                "تفضيله على العالمين مع إسماعيل ويونس ولوط (الأنعام 6:86).",
                "الاجتباء والهداية إلى صراط مستقيم (الأنعام 6:87).",
                "ذكره مع إسماعيل وذي الكفل في سورة ص: وكل من الأخيار (ص 38:48)."
            ),
            listOf(
                "जगत पर श्रेष्ठ नबियों में उल्लेख (6:86)।",
                "चुने जाने और सीधे मार्ग पर मार्गदर्शन की प्रशंसा (6:87)।",
                "इस्माईल व ज़ुल-किफ़्ल संग 'मिनल-अख़यार' कहा जाना (38:48)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Al-Yasa (AS); no specific town or detailed narrative is named in the Quran."),
            listOf("حضرت الیسعؑ؛ قرآن مجید میں کسی مخصوص شہر یا مزید تفصیل کا ذکر نہیں۔"),
            listOf("اليسع عليه السلام؛ ولم يذكر القرآن مكاناً أو تفاصيل أخرى."),
            listOf("हज़रत अल-यसा (अ.स.); क़ुरआन में किसी नगर या अन्य घटना का उल्लेख नहीं।")
        ),
        verseRefs = listOf(
            ProphetVerseRef(6, 86, 87, langMap("Favored Above the Worlds", "عالمین پر فضیلت", "التفضيل على العالمين", "जगत पर श्रेष्ठता")),
            ProphetVerseRef(38, 48, 48, langMap("Among the Chosen Best", "برگزیدہ ترین ہستیوں میں", "كلٌّ من الأخيار", "सर्वश्रेष्ठ चुनिंदा"))
        ),
        hadithRefs = emptyList(),
        lessons = listLangMap(
            listOf(
                "Allah preserves the honor of every truthful prophet even when few earthly details are recorded.",
                "Intellectual honesty avoids mixing unverified legends with Quranic certainty."
            ),
            listOf(
                "دنیا میں تفصیل کم معلوم ہونے کے باوجود اللہ کے ہاں ہر نبی کا مقام محفوظ رہتا ہے۔",
                "سچا علمی منہج غیر مصدقہ قصوں کو وحی کے ساتھ نہیں ملاتا۔"
            ),
            listOf(
                "حفظ الله قدر كل نبي وإن أوجز القرآن في أخباره.",
                "الالتزام بالوحي وحفظ التثبت أساس الأمانة العلمية."
            ),
            listOf(
                "अल्लाह हर सच्चे नबी का सम्मान सुरक्षित रखता है।",
                "प्रामाणिक शिक्षा प्रमाणित पाठ के बाहर अटकलें नहीं लगाती।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Kept faithfully concise strictly matching Quran 6:86-87 and 38:48. External narratives from non-authenticated sources are intentionally excluded.",
            "مکمل دیانت کے ساتھ صرف قرآن (6:86-87، 38:48) تک محدود؛ غیر مستند روایات شامل نہیں کی گئیں۔",
            "مقتصر على ما ثبت في القرآن الكريم (الأنعام 6:86-87، ص 38:48) دون روايات غير مسندة.",
            "निष्ठापूर्वक केवल क़ुरआन 6:86-87 व 38:48 तक सीमित; बाहरी अपुष्ट कथाएँ अलग रखी गई हैं।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "In which two Surahs of the Quran is Prophet Al-Yasa (AS) mentioned by name?",
                "قرآن مجید کی کن دو سورتوں میں حضرت الیسعؑ کا نام آیا ہے؟",
                "في أي سورتين ذُكر اسم نبي الله اليسع في القرآن؟",
                "क़ुरआन की किन दो सूरह में अल-यसा (अ.स.) का नाम आया है?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Surah Al-An'am (6:86) and Surah Sad (38:48)", "Surah Nuh and Surah Yasin", "Surah Al-Kahf and Surah Luqman"),
                AppLanguage.URDU to listOf("سورۃ الانعام (6:86) اور سورۃ ص (38:48)", "سورۃ نوح اور سورۃ یٰسین", "سورۃ الکہف اور سورۃ لقمان"),
                AppLanguage.ARABIC to listOf("الأنعام (6:86) وص (38:48)", "نوح ويس", "الكهف ولقمان"),
                AppLanguage.HINDI to listOf("सूरह अल-अनाम (6:86) और सूरह साद (38:48)", "सूरह नूह व यासीन", "सूरह कहफ़ व लुक़मान")
            ),
            0,
            langMap(
                "Al-Yasa (AS) is mentioned solely in Al-An'am 6:86 and Sad 38:48.",
                "حضرت الیسعؑ کا ذکر صرف الانعام (6:86) اور ص (38:48) میں ہے۔",
                "ذُكر في الأنعام 6:86 وص 38:48 فقط.",
                "उनका उल्लेख केवल 6:86 और 38:48 में हुआ है।"
            )
        )
    ),
    ProphetStory(
        id = "yunus",
        orderNumber = 21,
        arabicName = "يونس عليه السلام",
        names = langMap("Yunus (AS)", "حضرت یونس علیہ السلام", "يونس عليه السلام", "हज़रत यूनुस (अ.स.)"),
        titles = langMap("Dhun-Nun — Prophet of Repentance & Hope", "صاحبِ حوت (ذوالنون) اور دعائے استغفار", "ذو النون وصاحب التسبيح في الظلمات", "ज़ुन-नून (मछली वाले नबी)"),
        category = ProphetCategory.EARLY_NATIONS,
        summaryIntro = langMap(
            "Dhun-Nun, who departed his town in frustration before Allah's command and was swallowed by a huge fish. Calling out Allah's glorification inside three layers of darkness, he was saved, and his entire people repented and were spared (10:98).",
            "ذوالنون، جو حکمِ الٰہی سے قبل قوم سے روانہ ہو گئے اور بڑی مچھلی کے پیٹ میں تین تاریکیوں سے اللہ کی تسبیح و استغفار کر کے نجات پائی؛ آپ کی پوری قوم توبہ کر کے عذاب سے بچ گئی (10:98)۔",
            "ذو النون، التقمَه الحوت فسبّح الله في الظلمات الثلاث: لا إله إلا أنت سبحانك إني كنت من الظالمين، فنجاه الله، وآمن قومه أجمعون فكُشف عنهم العذاب (يونس 10:98).",
            "ज़ुन-नून, जिन्हें बड़ी मछली ने निगला पर तीन अँधेरों में अल्लाह की तस्बीह व क्षमा मांगकर बचाव पाया, और उनकी पूरी क़ौम तौबा करके बच गई (10:98)।"
        ),
        detailedIntro = langMap(
            "After calling a vast population of over one hundred thousand to Tawhid, Yunus (AS) departed angrily before receiving Allah's leave (21:87). Boarded on a laden ship where lots fell on him, he was swallowed by a giant fish without harm (37:140-142). In the darkness of night, sea, and the fish's belly, he cried: 'There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers' (21:87). Allah answered him, sheltered him under a gourd plant (Yaqtin), and his entire town believed sincerely so the punishment was lifted from them (10:98).",
            "ایک لاکھ یا اس سے زائد آبادی کو دعوت دینے کے بعد جب وہ حکمِ الٰہی سے پہلے ناراض ہو کر نکل آئے تو بھرے جہاز میں قرعہ آپ کے نام نکلا اور بڑی مچھلی نے نگل لیا (37:140-142)۔ سمندر اور پیٹ کی تین تاریکیوں میں آپ نے 'لا الہ الا انت سبحانک انی کنت من الظالمین' پکارا تو اللہ نے نجات عطا فرمائی، کدو کی بیل سے سایہ دیا اور ادھر آپ کی پوری قوم توبہ کر کے عذاب سے بچ گئی (10:98)۔",
            "خرج مغاضباً قبل أن يأذن الله له، فالتقمه الحوت بعد المساهمة في الفلك المشحون (الصافات 37:140-142). فنادى في الظلمات بتسبيحه العظيم (الأنبياء 21:87)، فاستجاب الله له وأنبت عليه شجرة من يقطين، وآمن قومه في نينوى أجمعون فمتّعهم الله إلى حين (يونس 10:98).",
            "लाख से अधिक लोगों को संदेश देने के बाद बिना आज्ञा निकलने पर बड़ी मछली ने निगल लिया (37:140-142)। तीन अँधेरों में उन्होंने पुकारा: 'ला इलाहा इल्ला अंता सुब्हानका इन्नी कुंतु मिनज़्ज़ालिमीन' (21:87)। अल्लाह ने उन्हें बचाया व उनकी पूरी बस्ती ईमान लाकर बच गई (10:98)।"
        ),
        quranicContext = langMap(
            "Surah Yunus (10:98) bears his name, alongside detailed accounts in Al-Anbiya (21:87-88), As-Saffat (37:139-148), and Al-Qalam (68:48-50).",
            "سورۃ یونس (10:98) آپ کے نام پر ہے، نیز الانبیاء (21:87-88)، الصافات (37:139-148) اور القلم (68:48-50) میں ذکر ہے۔",
            "باسمه سورة يونس (10:98)، وفُصّلت قصته في الأنبياء (21:87-88) والصافات (37:139-148) والقلم (68:48-50).",
            "उनके नाम पर सूरह यूनुस (10:98) है तथा अंबिया (21:87-88), साफ़्फ़ात (37:139-148) और क़लम (68:48-50) में वर्णन है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Leaving his town prior to Allah's permission (21:87); boarding the ship and lots falling on him (37:140-141).",
                "Swallowed by the huge fish and crying out in glorification amidst darkness (21:87, 37:142-144).",
                "Deliverance onto the shore sheltered by the Yaqtin plant and sent to over 100,000 people (37:145-147).",
                "His entire people repented before punishment struck and were blessed with respite (10:98)."
            ),
            listOf(
                "اجازتِ الٰہی سے قبل بستی چھوڑنا اور کشتی کے قرعے میں نام آنا (21:87، 37:140-141)۔",
                "مچھلی کے پیٹ میں تسبیح و استغفار اور نجات (21:87، 37:142-144)۔",
                "ساحل پر یقطین (کدو کی بیل) کا سایہ اور ایک لاکھ سے زائد لوگوں کی طرف بعثت (37:145-147)۔",
                "پوری قوم کی اجتماعی توبہ اور عذاب کا ٹل جانا (10:98)۔"
            ),
            listOf(
                "خروجه مغاضباً ومساهمة الفلك (الأنبياء 21:87، الصافات 37:140-141).",
                "التقام الحوت له وتسبيحه في الظلمات (الأنبياء 21:87، الصافات 37:142-144).",
                "إنقاذه وإنبات اليقطين وإرساله إلى مائة ألف أو يزيدون (الصافات 37:145-147).",
                "إيمان قومه وكشف العذاب عنهم (يونس 10:98)."
            ),
            listOf(
                "आज्ञा से पहले निकलना व नाव में पर्ची पड़ना (21:87, 37:140-141)।",
                "मछली के पेट में तस्बीह व क्षमा-याचना से बचाव (21:87, 37:142-144)।",
                "तट पर यक़्तीन की छाया व एक लाख से अधिक लोगों में वापसी (37:145-147)।",
                "पूरी क़ौम की तौबा से दंड का टल जाना (10:98)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Yunus (AS), his repentant people (>100,000), the sea and shore"),
            listOf("حضرت یونسؑ، توبہ کرنے والی قوم (ایک لاکھ سے زائد)، سمندر اور ساحل"),
            listOf("يونس عليه السلام وقومه (مائة ألف أو يزيدون) والبحر"),
            listOf("यूनुस (अ.स.), तौबा करने वाली एक लाख+ जनता, समुद्र और तट")
        ),
        verseRefs = listOf(
            ProphetVerseRef(21, 87, 88, langMap("Supplication in Darkness & Promise to Believers", "تاریکیوں میں دعا اور مومنین سے وعدہ", "دعاء ذي النون وبشارة المؤمنين", "अँधेरे में दुआ व मोमिनों को आश्वासन")),
            ProphetVerseRef(37, 139, 148, langMap("The Fish, Glorification & 100,000 People", "مچھلی کا واقعہ اور ایک لاکھ قوم", "الحوت والتسبيح ومآل قومه", "मछली का प्रसंग व एक लाख लोग"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Jami' at-Tirmidhi 3505 (Sahih)",
                "https://sunnah.com/tirmidhi:3505",
                langMap(
                    "The Prophet ﷺ taught that no Muslim invokes Allah in distress with the supplication of Dhun-Nun ('La ilaha illa Anta Subhanaka...') except that Allah answers him.",
                    "جامع ترمذی (3505): نبی اکرم ﷺ نے فرمایا جو مسلمان کسی مصیبت میں ذوالنونؑ کی دعا مانگے اللہ اس کی دعا قبول فرماتا ہے۔",
                    "جامع الترمذي (3505): دعوة ذي النون لا يدعو بها رجل مسلم في شيء قط إلا استجاب الله له.",
                    "तिर्मिज़ी 3505: जो मुस्लिम कठिनाई में ज़ुन-नून की दुआ करता है अल्लाह उसे स्वीकार करता है।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Glorifying Allah while admitting our shortcomings opens the door to deliverance (21:88).",
                "Sincere collective repentance turns away looming hardship (10:98)."
            ),
            listOf(
                "پریشانی میں اللہ کی پاکی اور اپنی خطا کا اعتراف نجات کا دروازہ کھول دیتا ہے (21:88)۔",
                "اجتماعی سچی توبہ آنے والے عذابوں کو ٹال دیتی ہے (10:98)۔"
            ),
            listOf(
                "الجمع بين تسبيح الله والاعتراف بالتقصير مفتاح الفرج (الأنبياء 21:87-88).",
                "التوبة الصادقة تدفع العذاب وتبدّل الحال (يونس 10:98)."
            ),
            listOf(
                "संकट में अल्लाह की महिमा व अपनी भूल स्वीकार करना मुक्ति का मार्ग है (21:88)।",
                "सच्ची सामूहिक तौबा आने वाले कष्टों को टाल देती है (10:98)।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(21, 87, 87, langMap("None has right to be worshipped but You; Glory to You", "لا الہ الا انت سبحانک انی کنت من الظالمین", "لا إله إلا أنت سبحانك إني كنت من الظالمين", "तेरे सिवा कोई पूज्य नहीं, तू पवित्र है, निश्चय ही मैं दोषी था"))
        ),
        reliabilityNotes = langMap(
            "Anchored directly in Quran 10:98, 21:87-88, 37:139-148, 68:48-50 and Jami' at-Tirmidhi 3505.",
            "قرآن مجید (10، 21، 37، 68) اور جامع ترمذی (3505) سے مکمل مستند۔",
            "مستند إلى القرآن الكريم وجامع الترمذي (3505).",
            "क़ुरआन (10, 21, 37, 68) और तिर्मिज़ी 3505 से प्रमाणित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "According to Quran 21:87-88, what saved Yunus (AS) from overwhelming grief inside the fish?",
                "قرآن (21:87-88) کے مطابق مچھلی کے پیٹ کی تاریکی میں حضرت یونسؑ کی نجات کا سبب کیا عمل بنا؟",
                "ما الذي نجّى يونس عليه السلام من الغم في بطن الحوت (الأنبياء 21:87-88)؟",
                "क़ुरआन 21:87-88 के अनुसार मछली के पेट में यूनुस (अ.स.) के बचाव का कारण क्या बना?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("He glorified Allah and confessed his fault in Tawhid", "He built a raft inside", "He called sailors with a horn"),
                AppLanguage.URDU to listOf("اللہ کی تسبیح کی اور اپنی خطا کا اعتراف کرتے ہوئے دعا مانگی", "اندر کشتی بنائی", "ملاحوں کو آواز دی"),
                AppLanguage.ARABIC to listOf("تسبيحه لله واعترافه بالخطأ في الدعاء", "بناؤه قارباً", "مناداته للملاحين"),
                AppLanguage.HINDI to listOf("अल्लाह की तस्बीह करते हुए अपनी भूल मानकर दुआ की", "नाव बनाई", "नाविकों को बुलाया")
            ),
            0,
            langMap(
                "Quran 37:143-144 and 21:87-88 affirm that had he not been of those who glorify Allah, he would have remained in its belly; 'Thus do We save the believers.'",
                "الصافات 37:143-144 اور الانبیاء 21:87-88 میں تسبیح و استغفار کو نجات کا سبب بیان کیا گیا ہے۔",
                "الصافات 37:143 والأنبياء 21:87-88: فلولا أنه كان من المسبحين... وكذلك ننجي المؤمنين.",
                "क़ुरआन 37:143 व 21:87-88 में तस्बीह व विनम्र दुआ को बचाव का कारण बताया गया है।"
            )
        )
    )
)
