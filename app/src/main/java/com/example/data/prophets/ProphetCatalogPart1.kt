package com.example.data.prophets

import com.example.data.model.AppLanguage

internal val PROPHET_CATALOG_PART1: List<ProphetStory> = listOf(
    ProphetStory(
        id = "adam",
        orderNumber = 1,
        arabicName = "آدم عليه السلام",
        names = langMap("Adam (AS)", "حضرت آدم علیہ السلام", "آدم عليه السلام", "हज़रत आदम (अ.स.)"),
        titles = langMap("First Human & First Prophet", "ابوالبشر اور پہلے نبی", "أبو البشر وأول الأنبياء", "प्रथम मानव एवं प्रथम नबी"),
        category = ProphetCategory.EARLY_NATIONS,
        summaryIntro = langMap(
            "Created by Allah from clay as the first human being and appointed steward (Khalifah) on Earth. His story teaches repentance, dignity, and vigilance against Shaytan.",
            "اللہ تعالیٰ نے آپ کو مٹی سے تخلیق فرمایا اور زمین پر خلیفہ مقرر کیا۔ آپ کا واقعہ توبہ، انسان کی تکریم اور شیطان کے مکر سے ہوشیار رہنے کا درس دیتا ہے۔",
            "خلقه الله من تراب وجعله خليفة في الأرض، وتعلّم الأسماء كلها. تعلمنا قصته صدق التوبة وكرامة الإنسان والحذر من وسوسة الشيطان.",
            "अल्लाह ने उन्हें मिट्टी से रचा और धरती पर ख़लीफ़ा बनाया। उनकी कथा सच्ची तौबा, मानव सम्मान और शैतान के बहकावे से सावधानी सिखाती है।"
        ),
        detailedIntro = langMap(
            "Allah informed the angels that He would place a succession (Khalifah) upon Earth, taught Adam the names of all things, and commanded the angels to prostrate out of honor. Only Iblis refused out of arrogance. After tasting from the forbidden tree in Paradise, Adam and Hawwa turned immediately to Allah in sincere repentance, and Allah forgave them and sent humanity to Earth with the promise of divine guidance.",
            "اللہ تعالیٰ نے فرشتوں کو زمین پر خلیفہ بنانے کی خبر دی، حضرت آدمؑ کو تمام اشیاء کے نام سکھائے اور تعظیمی سجدے کا حکم دیا۔ ابلیس نے تکبر کرتے ہوئے انکار کیا۔ جنت میں ممنوعہ درخت کا پھل چکھنے کے بعد حضرت آدمؑ اور حضرت حواؑ نے فوراً سچی توبہ کی، جسے اللہ نے قبول فرمایا اور زمین پر ہدایت کے وعدے کے ساتھ اتارا۔",
            "أخبر الله الملائكة بخلق خليفة في الأرض، وعلّم آدم الأسماء كلها، ثم أمر الملائكة بالسجود له تشريفاً، فأبى إبليس استكباراً. وبعد الأكل من الشجرة في الجنة تاب آدم وزوجه إلى الله بصدق فتاب الله عليهما وأهبطهما إلى الأرض مع عهد الهداية.",
            "अल्लाह ने फ़रिश्तों को धरती पर ख़लीफ़ा बनाने की सूचना दी, आदम (अ.स.) को सभी नाम सिखाए और सम्मान में सजदे का आदेश दिया। इब्लीस ने अहंकार में इनकार किया। जन्नत में वर्जित वृक्ष से खाने के बाद उन्होंने तुरंत सच्ची तौबा की, जिसे अल्लाह ने स्वीकार किया।"
        ),
        quranicContext = langMap(
            "Narrated across Surahs Al-Baqarah (2:30-38), Al-A'raf (7:11-27), Al-Hijr (15:26-44), Taha (20:115-123), and Sad (38:71-85).",
            "آپؑ کا ذکر سورۃ البقرہ (2:30-38)، الاعراف (7:11-27)، الحجر (15:26-44)، طٰہٰ (20:115-123) اور ص (38:71-85) میں مفصل آیا ہے۔",
            "وردت قصته في سور البقرة (2:30-38) والأعراف (7:11-27) والحجر (15:26-44) وطه (20:115-123) وص (38:71-85).",
            "उनका वर्णन सूरह अल-बक़रा (2:30-38), अल-आराफ़ (7:11-27), अल-हिज्र (15:26-44), ताहा (20:115-123) और साद (38:71-85) में आया है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Allah announces the creation of a steward (Khalifah) on Earth (Quran 2:30).",
                "Adam is taught the names of all things; angels prostrate except arrogant Iblis (2:31-34).",
                "Test of the forbidden tree in Paradise through Iblis's deception (7:19-22).",
                "Immediate repentance using words inspired by Allah, and descent to Earth with guidance (2:37-38, 7:23)."
            ),
            listOf(
                "زمین پر خلیفہ بنانے کا اعلان اور تخلیقِ آدمؑ (قرآن 2:30)۔",
                "علم الاسماء کی فضیلت اور ابلیس کا تکبر سے سجدے سے انکار (2:31-34)۔",
                "جنت میں شیطانی وسوسے اور ممنوعہ درخت کی آزمائش (7:19-22)۔",
                "توبہ کے کلمات کا الہام، مغفرت اور زمین پر ہدایت کے ساتھ نزول (2:37-38، 7:23)۔"
            ),
            listOf(
                "إخبار الملائكة بخلق خليفة في الأرض (البقرة 2:30).",
                "تعليم الأسماء واستكبار إبليس عن السجود (البقرة 2:31-34).",
                "وسوسة الشيطان والابتلاء بالشجرة في الجنة (الأعراف 7:19-22).",
                "تلقي كلمات التوبة وقبولها والهبوط إلى الأرض مع وعد الهدى (البقرة 2:37-38، الأعراف 7:23)."
            ),
            listOf(
                "धरती पर ख़लीफ़ा की रचना की घोषणा (क़ुरआन 2:30)।",
                "सब नामों का ज्ञान और इब्लीस का अहंकार से सजदे से इनकार (2:31-34)।",
                "जन्नत में शैतान के बहकावे से वर्जित वृक्ष की परीक्षा (7:19-22)।",
                "तौबा के शब्द सिखाया जाना, क्षमा और मार्गदर्शन के साथ धरती पर उतरना (2:37-38, 7:23)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Adam (AS) and his spouse (Hawwa, named in Hadith)", "Angels and Iblis", "Paradise (Al-Jannah) and Earth"),
            listOf("حضرت آدمؑ اور آپ کی زوجہ (حدیث میں حضرت حواؑ)", "فرشتے اور ابلیس", "جنت اور زمین"),
            listOf("آدم عليه السلام وزوجه (حواء في السنة)", "الملائكة وإبليس", "الجنة والأرض"),
            listOf("हज़रत आदम (अ.स.) व पत्नी हव्वा", "फ़रिश्ते और इब्लीस", "जन्नत और धरती")
        ),
        verseRefs = listOf(
            ProphetVerseRef(2, 30, 34, langMap("Creation & Honoring of Adam", "تخلیق و تکریمِ آدمؑ", "خلق آدم وتكريمه", "आदम (अ.स.) की रचना और सम्मान")),
            ProphetVerseRef(2, 37, 38, langMap("Repentance Accepted", "قبولیتِ توبہ", "توبة آدم عليه السلام", "तौबा की स्वीकृति")),
            ProphetVerseRef(7, 23, 23, langMap("Dua of Repentance", "دعائے استغفار", "دعاء الاستغفار", "क्षमा की दुआ"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3326",
                "https://sunnah.com/bukhari:3326",
                langMap(
                    "Allah created Adam in his stature (sixty cubits) and commanded him to greet a group of angels with As-Salamu Alaikum.",
                    "اللہ تعالیٰ نے حضرت آدمؑ کو ساٹھ ہاتھ قد میں پیدا فرمایا اور فرشتوں کو سلام کہنے کا حکم دیا۔",
                    "خلق الله آدم على صورته طوله ستون ذراعاً وأمره بإفشاء السلام للملائكة.",
                    "अल्लाह ने आदम (अ.स.) को साठ हाथ कद में रचा और फ़रिश्तों को सलाम करने का हुक्म दिया।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "True repentance acknowledges responsibility immediately without blaming others.",
                "Knowledge and humility elevate creation, whereas arrogance caused Iblis's downfall.",
                "Shaytan's enmity toward humanity is enduring and requires constant awareness."
            ),
            listOf(
                "سچی توبہ عذر تراشنے کے بجائے فوراً اپنی غلطی کا اعتراف اور رجوع الی اللہ ہے۔",
                "علم اور عاجزی انسان کو بلند کرتی ہے جبکہ تکبر ابلیس کی ہلاکت کا سبب بنا۔",
                "انسانیت کے ساتھ شیطان کی دشمنی دائمی ہے جس سے ہر وقت ہوشیار رہنا ضروری ہے۔"
            ),
            listOf(
                "التوبة الصادقة تقتضي الاعتراف بالذنب والرجوع السريع دون تبرير.",
                "العلم والتواضع سبب الرفعة، بينما الكبر أهلك إبليس.",
                "عداوة الشيطان للإنسان دائمة وتستوجب الحذر والاستعاذة."
            ),
            listOf(
                "सच्ची तौबा बहाने बनाने के बजाय तुरंत अपनी गलती स्वीकार कर अल्लाह की ओर लौटना है।",
                "ज्ञान और विनम्रता ऊँचाई देते हैं, जबकि अहंकार ने इब्लीस को गिराया।",
                "शैतान की शत्रुता से सदैव सचेत रहना आवश्यक है।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(7, 23, 23, langMap("Our Lord, we have wronged ourselves", "ربنا ظلمنا انفسنا", "ربنا ظلمنا أنفسنا", "ऐ हमारे रब, हमने ख़ुद पर ज़ुल्म किया"))
        ),
        reliabilityNotes = langMap(
            "Grounded directly in Quran 2:30-38, 7:11-27, 20:115-123 and Sahih al-Bukhari 3326. Speculative details about physical geography on Earth without Sahih proof are omitted.",
            "تمام تفصیلات قرآنِ مجید اور صحیح بخاری (3326) سے ماخوذ ہیں۔ زمین پر اترنے کے مخصوص مقامات کی غیر مصدقہ روایات شامل نہیں کی گئیں۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3326) دون إيراد القصص غير الثابتة عن مكان الهبوط ونحوه.",
            "पूर्णतः क़ुरआन और सहीह बुख़ारी (3326) पर आधारित; धरती पर उतरने के विशिष्ट स्थलों की अप्रमाणित कथाएँ शामिल नहीं हैं।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "After tasting from the forbidden tree in Paradise, how did Adam (AS) and his wife respond?",
                "جنت میں ممنوعہ درخت سے کھانے کے بعد حضرت آدمؑ اور آپ کی زوجہ نے کیا کیا؟",
                "ماذا فعل آدم عليه السلام وزوجه بعد الأكل من الشجرة؟",
                "जन्नत में वर्जित वृक्ष से खाने के बाद आदम (अ.स.) और उनकी पत्नी ने क्या किया?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Admitted their wrong and begged Allah for forgiveness (7:23)", "Hid silently without speaking", "Blamed the angels"),
                AppLanguage.URDU to listOf("اپنی غلطی کا اعتراف کر کے اللہ سے معافی مانگی (7:23)", "خاموشی سے چھپ گئے", "فرشتوں کو الزام دیا"),
                AppLanguage.ARABIC to listOf("اعترفا بظلمهما وطلبَا المغفرة من الله (الأعراف 7:23)", "اختبآ بصمت دون دعاء", "ألقيا اللوم على الملائكة"),
                AppLanguage.HINDI to listOf("अपनी गलती मानकर अल्लाह से माफ़ी माँगी (7:23)", "चुपचाप छिप गए", "फ़रिश्तों को दोष दिया")
            ),
            0,
            langMap(
                "Quran 7:23 records their immediate supplication: 'Our Lord, we have wronged ourselves, and if You do not forgive us and have mercy upon us, we will surely be among the losers.'",
                "سورۃ الاعراف 7:23 میں ان کی فوری دعا مذکور ہے: اے ہمارے رب، ہم نے اپنی جانوں پر ظلم کیا۔",
                "سجّل القرآن في الأعراف 7:23 دعاءهما الفوري بالتوبة والاستغفار.",
                "क़ुरआन 7:23 में उनकी तत्काल क्षमा-याचना दर्ज है।"
            )
        )
    ),
    ProphetStory(
        id = "idris",
        orderNumber = 2,
        arabicName = "إدريس عليه السلام",
        names = langMap("Idris (AS)", "حضرت ادریس علیہ السلام", "إدريس عليه السلام", "हज़रत इदरीस (अ.स.)"),
        titles = langMap("Man of Truth & Prophet Raised High", "صدیق اور بلند مرتبہ نبی", "الصديق والنبي المرفوع مكاناً علياً", "सच्चे नबी और उच्च पद प्राप्त"),
        category = ProphetCategory.CONCISE_MENTION,
        summaryIntro = langMap(
            "Explicitly praised in the Quran as a truthful Prophet (Siddiqan Nabiyya) of steadfast patience whom Allah raised to an exalted station (Quran 19:56-57; 21:85-86).",
            "قرآن مجید میں آپؑ کا مختصر مگر نہایت عظیم ذکر صدیق، نبی اور صابر کی حیثیت سے آیا ہے جنہیں اللہ نے بلند مقام عطا فرمایا (19:56-57، 21:85-86)۔",
            "ذُكر في القرآن بإيجاز بليغ بأنه كان صديقاً نبياً صابراً رفعه الله مكاناً علياً (مريم 19:56-57، الأنبياء 21:85-86).",
            "क़ुरआन में उन्हें अत्यंत सत्यनिष्ठ नबी (सिद्दीक़) और धैर्यवान बताया गया है, जिन्हें अल्लाह ने उच्च स्थान प्रदान किया (19:56-57, 21:85-86)।"
        ),
        detailedIntro = langMap(
            "The Quran provides a concise, deeply honoring portrait of Idris (AS): he is described as truthful, patient, and among the righteous admitted into Allah's mercy, raised to a high station (makanan 'aliyyan). Because revelation gives only these brief facts, faithful scholarship avoids treating unverified historical claims or external legends as certainty.",
            "قرآن کریم میں حضرت ادریسؑ کا ذکر مختصر لیکن انتہائی باوقار الفاظ میں آیا ہے۔ آپ کو صدیق، نبی، صابر اور صالحین میں شمار کیا گیا ہے اور 'مكاناً علياً' عطا فرمانے کا ذکر ہے۔ مستند اسلامی تحقیق غیر مصدقہ تاریخی یا اسرائیلی تفصیلات کو یقینی حقائق کے طور پر بیان نہیں کرتی۔",
            "اكتفى القرآن الكريم في ذكر إدريس عليه السلام بوصفه بالصدق والنبوة والصبر والصلاح وبأن الله رفعه مكاناً علياً. ولذلك يلتزم المنهج الموثق بالاقتصار على ما صحّ دون الجزم بتفصيلات تاريخية غير مسندة.",
            "क़ुरआन में हज़रत इदरीस (अ.स.) का वर्णन संक्षिप्त किंतु अत्यंत सम्मानजनक है—सच्चे, नबी, धैर्यवान और नेक, जिन्हें उच्च स्थान मिला। प्रामाणिक स्रोत इससे आगे अपुष्ट कथाओं को तथ्य मानने से बचते हैं।"
        ),
        quranicContext = langMap(
            "Mentioned concisely by name twice in the Quran: Surah Maryam (19:56-57) and Surah Al-Anbiya (21:85-86).",
            "قرآن میں آپ کا نام مبارک دو مقامات پر آیا ہے: سورۃ مریم (19:56-57) اور سورۃ الانبیاء (21:85-86)۔",
            "ذُكر اسمه في موضعين في القرآن: سورة مريم (19:56-57) وسورة الأنبياء (21:85-86).",
            "क़ुरआन में उनका नाम दो बार आया है: सूरह मरयम (19:56-57) और सूरह अल-अंबिया (21:85-86)।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Sent as a truthful Prophet (Siddiqan Nabiyya) mentioned honorably in the Book (19:56).",
                "Raised by Allah to an exalted station — makanan 'aliyyan (19:57).",
                "Commended alongside Ismail and Dhul-Kifl for steadfast patience and righteousness (21:85-86)."
            ),
            listOf(
                "قرآن میں 'صدیقاً نبیاً' کے لقب سے ذکر (19:56)۔",
                "اللہ تعالیٰ کی جانب سے بلند مقام عطا فرمانا (19:57)۔",
                "حضرت اسماعیلؑ اور ذوالکفلؑ کے ساتھ صبر و صلاح کی تعریف (21:85-86)۔"
            ),
            listOf(
                "وصفه في القرآن بأنه كان صديقاً نبياً (مريم 19:56).",
                "رفعه الله مكاناً علياً (مريم 19:57).",
                "الثناء عليه مع إسماعيل وذي الكفل بالصبر والصلاح (الأنبياء 21:85-86)."
            ),
            listOf(
                "क़ुरआन में सत्यनिष्ठ नबी के रूप में उल्लेख (19:56)।",
                "अल्लाह द्वारा उच्च स्थान पर उठाया जाना (19:57)।",
                "इस्माईल और ज़ुल-किफ़्ल (अ.स.) के साथ धैर्य व नेकी की प्रशंसा (21:85-86)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Idris (AS); no specific homeland or earthly tribe is specified in the Quran."),
            listOf("حضرت ادریسؑ؛ قرآن مجید میں کسی خاص شہر یا قبیلے کا نام متعین نہیں کیا گیا۔"),
            listOf("إدريس عليه السلام؛ ولم يحدد القرآن مكاناً أو قوماً بعينه."),
            listOf("हज़रत इदरीस (अ.स.); क़ुरआन में किसी विशेष नगर या समुदाय का नाम निर्दिष्ट नहीं है।")
        ),
        verseRefs = listOf(
            ProphetVerseRef(19, 56, 57, langMap("Truthful Prophet Raised High", "صدیق نبی اور بلند مقام", "الصديق المرفوع", "सच्चे नबी और उच्च स्थान")),
            ProphetVerseRef(21, 85, 86, langMap("Steadfast Patience", "صبر اور رحمت الٰہی", "الصبر والرحمة", "धैर्य और अल्लाह की दया"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih Muslim 162a",
                "https://sunnah.com/muslim:162a",
                langMap(
                    "During the Night Journey (Isra & Mi'raj), the Prophet Muhammad ﷺ met Idris (AS) in the fourth heaven, who welcomed him.",
                    "معراج کی رات نبی اکرم ﷺ کی چوتھے آسمان پر حضرت ادریسؑ سے ملاقات ہوئی۔",
                    "في حديث المعراج لقي النبي ﷺ إدريس عليه السلام في السماء الرابعة فرحب به.",
                    "मेराज की रात पैग़ंबर मुहम्मद ﷺ की चौथे आसमान में हज़रत इदरीस (अ.स.) से भेंट हुई।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Truthfulness (Siddiqiyyah) and patient endurance are sufficient grounds for honor before Allah.",
                "Islamic knowledge values truthfulness about what is revealed without inventing unverified biographies."
            ),
            listOf(
                "سچائی (صدیقیت) اور صبر اللہ کے نزدیک بلند مقام پانے کے بنیادی اوصاف ہیں۔",
                "دینی علم کا تقاضا ہے کہ جتنی بات وحی سے ثابت ہو اسی پر اکتفا کیا جائے۔"
            ),
            listOf(
                "الصدق والصبر من أعظم أسباب الرفعة عند الله تعالى.",
                "الأمانة العلمية تقتضي الوقوف عند ما صح في الوحي دون اختلاق تفاصيل."
            ),
            listOf(
                "सत्यनिष्ठा और धैर्य अल्लाह के निकट सम्मान पाने के मुख्य गुण हैं।",
                "प्रामाणिक ज्ञान उतना ही प्रस्तुत करता है जितना वह्य से सिद्ध है।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Kept intentionally concise strictly matching Quran 19:56-57, 21:85-86, and the Mi'raj narration in Sahih Muslim 162a. Unverified attributions regarding ancient crafts or specific centuries are not presented as established facts.",
            "قرآنِ مجید (19:56-57، 21:85-86) اور صحیح مسلم (162a) میں جتنا ذکر ثابت ہے اتنا ہی بیان کیا گیا ہے؛ غیر مستند روایات شامل نہیں کی گئیں۔",
            "مقتصر على ما ثبت في القرآن الكريم (19:56-57، 21:85-86) وصحيح مسلم (162a) دون الجزم بأقوال غير موثقة.",
            "केवल क़ुरआन 19:56-57, 21:85-86 और सहीह मुस्लिम 162a में प्रमाणित विवरण तक ही सीमित; अपुष्ट कथाएँ नहीं जोड़ी गई हैं।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Which qualities does Surah Maryam (19:56-57) emphasize about Prophet Idris (AS)?",
                "سورۃ مریم (19:56-57) میں حضرت ادریسؑ کے کن اوصاف کا ذکر ہے؟",
                "بمَ وصفت سورة مريم (19:56-57) نبي الله إدريس عليه السلام؟",
                "सूरह मरयम (19:56-57) में हज़रत इदरीस (अ.स.) के किन गुणों का उल्लेख है?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("He was a truthful Prophet raised to a high station", "He built Solomon's palace", "He parted the Red Sea"),
                AppLanguage.URDU to listOf("آپ سچے نبی (صدیق) تھے جنہیں بلند مقام عطا ہوا", "آپ نے بیت المقدس تعمیر کیا", "آپ نے دریائے نیل پار کیا"),
                AppLanguage.ARABIC to listOf("كان صديقاً نبياً ورفعه الله مكاناً علياً", "بنى قصر سليمان", "شق البحر لقومه"),
                AppLanguage.HINDI to listOf("वे सत्यनिष्ठ नबी थे जिन्हें उच्च स्थान प्राप्त हुआ", "उन्होंने समुद्र को दो भागों में बाँटा", "वे महल के निर्माता थे")
            ),
            0,
            langMap(
                "Surah Maryam 19:56-57 states he was a man of truth and a Prophet whom Allah raised to an exalted station.",
                "سورۃ مریم 19:56-57 کے مطابق وہ صدیق نبی تھے اور اللہ نے انہیں بلند مقام پر اٹھایا۔",
                "نصّت الآيتان (19:56-57) على أنه كان صديقاً نبياً ورفعه الله مكاناً علياً.",
                "सूरह मरयम 19:56-57 में उन्हें सत्यनिष्ठ नबी और उच्च पद प्राप्त बताया गया है।"
            )
        )
    ),
    ProphetStory(
        id = "nuh",
        orderNumber = 3,
        arabicName = "نوح عليه السلام",
        names = langMap("Nuh (AS)", "حضرت نوح علیہ السلام", "نوح عليه السلام", "हज़रत नूह (अ.स.)"),
        titles = langMap("Prophet of 950 Years & the Ark", "پیغمبرِ صبر و دعوت", "شيخ المرسلين وصاحب السفينة", "सब्र व दावत के नबी (उलुल-अज़्म)"),
        category = ProphetCategory.ULUL_AZM,
        summaryIntro = langMap(
            "One of the five Ulul-Azm (Steadfast) Messengers who called his people to Tawhid night and day for 950 years before building the Ark under Allah's command.",
            "اولوالعزم پیغمبروں میں شامل جنہوں نے ساڑھے نو سو برس تک اپنی قوم کو توحید کی دعوت دی اور پھر اللہ کے حکم سے کشتی تیار فرمائی۔",
            "من أولي العزم من الرسل، دعا قومه إلى التوحيد ليلاً ونهاراً تسعمئة وخمسين عاماً قبل أن يصنع الفلك بأمر الله.",
            "पाँच उलुल-अज़्म रसूलों में से एक, जिन्होंने 950 वर्षों तक अपनी क़ौम को तौहीद की दावत दी और अल्लाह के हुक्म से कश्ती बनाई।"
        ),
        detailedIntro = langMap(
            "Sent when idolatry appeared among his people, Nuh (AS) preached openly and privately for 950 years (Quran 29:14). Mocked by arrogant elites for gathering humble believers, he patiently persevered until Allah commanded him to build the Ark. The great flood overwhelmed the rejecters, sparing the believers and pairs of creatures, and the Ark came to rest upon Mount Judi (11:44).",
            "جب آپ کی قوم بت پرستی میں مبتلا ہوئی تو حضرت نوحؑ نے ساڑھے نو سو سال تک کھلے اور خفیہ انداز میں تبلیغ فرمائی (29:14)۔ متکبر سرداروں کے تمسخر کے باوجود آپ ثابت قدم رہے، یہاں تک کہ وحی کے مطابق کشتی بنائی۔ طوفان کے بعد کشتی جودی پہاڑ پر جا ٹھہری (11:44)۔",
            "دعا قومه إلى عبادة الله وحده سراً وجهاراً تسعمئة وخمسين عاماً (العنكبوت 29:14). وبعد استهزاء الملأ أمره الله بصنع الفلك فنجّى الله المؤمنين وأغرق المكذبين، واستوت السفينة على الجودي (هود 11:44).",
            "जब उनकी क़ौम मूर्तिपूजा में डूब गई, तो उन्होंने 950 वर्ष रात-दिन तौहीद का संदेश दिया (29:14)। अहंकारी सरदारों के उपहास के बाद अल्लाह के आदेश से नाव बनाई और बाढ़ में ईमानवाले बच निकले; नाव जूदी पर्वत पर ठहरी (11:44)।"
        ),
        quranicContext = langMap(
            "Dedicated Surah Nuh (71) as well as major passages in Al-A'raf (7:59-64), Hud (11:25-49), Al-Mu'minun (23:23-30), and Al-Qamar (54:9-16).",
            "آپ کے نام پر مکمل سورۃ نوح (71) ہے؛ مزید ذکر الاعراف (7:59-64)، ہود (11:25-49) اور القمر (54:9-16) میں ہے۔",
            "سورة كاملة باسمه (سورة نوح 71)، وقصته المفصلة في هود (11:25-49) والأعراف (7:59-64) والقمر (54:9-16).",
            "उनके नाम पर पूरी सूरह नूह (71) है, तथा हूद (11:25-49), आराफ़ (7:59-64) और क़मर (54:9-16) में विस्तृत वर्णन है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "950 years of patient dawah calling people away from idols (29:14, 71:1-20).",
                "Building the Ark under Allah's direct watchful command despite mockery (11:37-38).",
                "The flood and the Ark sailing amidst waves like mountains; resting on Al-Judi (11:42-44).",
                "Lesson of spiritual kinship when his disbelieving son refused safety (11:45-47)."
            ),
            listOf(
                "ساڑھے نو سو سال کی مسلسل دعوتِ توحید (29:14، 71:1-20)۔",
                "مذاق اڑانے والوں کے سامنے اللہ کی نگرانی میں کشتی کی تیاری (11:37-38)۔",
                "طوفانِ عظیم اور کوہِ جودی پر کشتی کا ٹھہرنا (11:42-44)۔",
                "نافرمان بیٹے کے واقعے سے ایمان کے رشتے کی برتری کا درس (11:45-47)۔"
            ),
            listOf(
                "الدعوة المستمرة 950 سنة بالليل والنهار (29:14، 71:1-20).",
                "صنع الفلك بوحي الله وتحت رعايته (11:37-38).",
                "الطوفان العظيم واستقرار السفينة على الجودي (11:42-44).",
                "بيان أن القرابة الحقيقية هي قرابة الإيمان والعمل الصالح (11:45-47)."
            ),
            listOf(
                "950 वर्षों तक निरंतर दावत व सब्र (29:14, 71:1-20)।",
                "उपहास के बीच अल्लाह की देखरेख में कश्ती का निर्माण (11:37-38)।",
                "महाजलप्रलय और कश्ती का जूदी पर ठहरना (11:42-44)।",
                "ईमान और नेक अमल के रिश्ते की श्रेष्ठता की शिक्षा (11:45-47)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Nuh (AS) and believing followers", "His disbelieving son and wife", "Mount Al-Judi (11:44)"),
            listOf("حضرت نوحؑ اور مومنین", "آپ کا نافرمان بیٹا اور بیوی", "کوہِ جودی (11:44)"),
            listOf("نوح عليه السلام والمؤمنون", "ابنه وزوجته الكافران", "جبل الجودي (هود 11:44)"),
            listOf("हज़रत नूह (अ.स.) व ईमानवाले", "उनका अवज्ञाकारी पुत्र और पत्नी", "जूदी पर्वत (11:44)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(11, 36, 44, langMap("The Ark & Mount Judi", "کشتی اور کوہِ جودی", "صنع الفلك والجودي", "कश्ती और जूदी पर्वत")),
            ProphetVerseRef(29, 14, 15, langMap("950 Years of Mission", "950 سالہ دعوت", "950 عاماً في الدعوة", "950 वर्ष की दावत")),
            ProphetVerseRef(71, 28, 28, langMap("Dua of Nuh (AS)", "حضرت نوحؑ کی دعا", "دعاء نوح عليه السلام", "नूह (अ.स.) की दुआ"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 4712",
                "https://sunnah.com/bukhari:4712",
                langMap(
                    "In the Hadith of Intercession, people recognize Nuh (AS) as the first Messenger Allah sent to the people of Earth.",
                    "حدیثِ شفاعت میں حضرت نوحؑ کو اہلِ زمین کی طرف بھیجے جانے والا پہلا رسول قرار دیا گیا ہے۔",
                    "في حديث الشفاعة العظمى يُذكر نوح عليه السلام بأنه أول رسول بعثه الله إلى أهل الأرض.",
                    "शफ़ाअत की हदीस में नूह (अ.स.) को धरती वालों की ओर भेजे गए प्रथम रसूल कहा गया है।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Success in calling to truth is measured by faithfulness, not headcount.",
                "Faith and righteous conduct define true spiritual family (11:46)."
            ),
            listOf(
                "دعوت کی کامیابی تعداد سے نہیں بلکہ استقامت اور وفاداری سے ناپی جاتی ہے۔",
                "حقیقی رشتہ ایمان اور عملِ صالح کی بنیاد پر قائم ہوتا ہے۔"
            ),
            listOf(
                "العبرة في الدعوة بالثبات والإخلاص لا بكثرة الأتباع.",
                "الولاء الإيماني والعمل الصالح مقدّمان على مجرد النسب (11:46)."
            ),
            listOf(
                "सच्चाई की राह में सफलता संख्या से नहीं बल्कि निष्ठा व धैर्य से आंकी जाती है।",
                "सच्चा संबंध ईमान और नेक कर्म से होता है (11:46)।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(71, 28, 28, langMap("Forgive me, my parents, and believers", "رب اغفر لی ولوالدی", "رب اغفر لي ولوالدي", "ऐ रब, मुझे, माता-पिता व मोमिनों को क्षमा कर")),
            ProphetDuaRef(11, 47, 47, langMap("Seeking forgiveness & refuge from ignorance", "رب انی اعوذ بک ان اسئلک", "رب إني أعوذ بك أن أسألك ما ليس لي به علم", "बिना ज्ञान माँगने से पनाह की दुआ"))
        ),
        reliabilityNotes = langMap(
            "Anchored directly in Quran Surahs 11, 29, 71 and Sahih al-Bukhari 4712.",
            "قرآن کریم (ہود، العنکبوت، نوح) اور صحیح بخاری (4712) کی مستند نصوص پر مبنی۔",
            "مرتبط مباشرة بسور هود والعنكبوت ونوح وصحيح البخاري (4712).",
            "क़ुरआन (सूरह 11, 29, 71) और सहीह बुख़ारी 4712 पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "According to Quran 11:44, where did the Ark come to rest?",
                "قرآن (11:44) کے مطابق حضرت نوحؑ کی کشتی کہاں جا کر ٹھہری؟",
                "أين استوت سفينة نوح عليه السلام وفق القرآن (هود 11:44)؟",
                "क़ुरआन (11:44) के अनुसार नूह (अ.स.) की कश्ती कहाँ ठहरी?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Mount Al-Judi", "Mount Sinai", "Mount Uhud"),
                AppLanguage.URDU to listOf("کوہِ جودی پر", "کوہِ طور پر", "جبلِ احد پر"),
                AppLanguage.ARABIC to listOf("على الجودي", "على جبل الطور", "على جبل أحد"),
                AppLanguage.HINDI to listOf("जूदी पर्वत पर", "तूर पर्वत पर", "उहुद पर्वत पर")
            ),
            0,
            langMap(
                "Quran 11:44 explicitly states: 'And it came to rest upon Mount Judi.'",
                "سورۃ ہود 11:44 میں صراحت ہے: اور کشتی جودی پر جا ٹھہری۔",
                "قال تعالى في سورة هود 11:44: واستوت على الجودي.",
                "क़ुरआन 11:44 में स्पष्ट है कि नाव जूदी पर ठहरी।"
            )
        )
    ),
    ProphetStory(
        id = "hud",
        orderNumber = 4,
        arabicName = "هود عليه السلام",
        names = langMap("Hud (AS)", "حضرت ہود علیہ السلام", "هود عليه السلام", "हज़रत हूद (अ.स.)"),
        titles = langMap("Messenger to the People of 'Ad", "قومِ عاد کے پیغمبر", "رسول الله إلى قوم عاد", "क़ौम-ए-आद के रसूल"),
        category = ProphetCategory.EARLY_NATIONS,
        summaryIntro = langMap(
            "Sent to the mighty civilization of 'Ad at Al-Ahqaf, who prided themselves on towering monuments and physical strength while rejecting Allah.",
            "آپ کو طاقتور اور بلند عمارتوں پر غرور کرنے والی قومِ عاد (الاحقاف) کی طرف توحید کی دعوت کے لیے بھیجا گیا۔",
            "أرسله الله إلى قوم عاد في الأحقاف الذين اغتروا بقوتهم ومبانيهم واستكبروا عن عبادة الله وحده.",
            "उन्हें बलशाली और ऊँचे स्तंभों पर घमंड करने वाली क़ौम-ए-आद (अल-अहक़ाफ़) की ओर भेजा गया था।"
        ),
        detailedIntro = langMap(
            "'Ad lived in Al-Ahqaf (winding sand dunes) and built grand pillars unmatched in their era (89:6-8). Hud (AS) urged them to worship Allah alone, cease oppression, and seek forgiveness so Allah would bless them with abundant rain and strength. They scoffed, claiming 'Who is stronger than us?' until a fierce wind destroyed them while sparing Hud and the believers.",
            "قومِ عاد الاحقاف کے علاقے میں آباد تھی اور دنیا میں ان جیسی طاقتور عمارتیں بے مثال تھیں۔ حضرت ہودؑ نے انہیں توحید، ظلم چھوڑنے اور استغفار کی دعوت دی، لیکن انہوں نے اپنی طاقت پر غرور کیا اور تیز آندھی کے عذاب سے ہلاک ہوئے جبکہ مومنین نجات پا گئے۔",
            "كانت عاد في الأحقاف أصحاب قوة وبنيان عظيم. دعاهم هود عليه السلام إلى التوحيد والاستغفار فاستكبروا قائلين: من أشد منا قوة؟ فأهلكهم الله بريح صرصر عاتية ونجّى هوداً والذين آمنوا.",
            "आद के लोग अल-अहक़ाफ़ में रहते थे और भव्य निर्माण व शक्ति पर अहंकार करते थे। हूद (अ.स.) ने उन्हें तौहीद व क्षमा-याचना का संदेश दिया, पर इनकार करने पर प्रचंड आँधी ने उन्हें नष्ट कर दिया और ईमानवाले बच निकले।"
        ),
        quranicContext = langMap(
            "Narrated in Surah Al-A'raf (7:65-72), Surah Hud (11:50-60), Ash-Shu'ara (26:123-140), Al-Ahqaf (46:21-26), and Al-Haqqah (69:6-8).",
            "آپ کا تذکرہ الاعراف (7:65-72)، ہود (11:50-60)، الشعراء (26:123-140) اور الاحقاف (46:21-26) میں ہے۔",
            "وردت قصته في الأعراف (7:65-72) وهود (11:50-60) والشعراء (26:123-140) والأحقاف (46:21-26).",
            "उनका वर्णन आराफ़ (7:65-72), हूद (11:50-60), शुअरा (26:123-140) और अहक़ाफ़ (46:21-26) में है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Preaching Tawhid and humility to the powerful 'Ad (7:65, 11:50).",
                "Promise of blessing through istighfar — seeking forgiveness brings mercy and strength (11:52).",
                "Arrogant defiance ('Who is stronger than us in might?', 41:15).",
                "Seven nights and eight days of fierce wind; deliverance of Hud and believers (69:6-7, 11:58)."
            ),
            listOf(
                "طاقتور قومِ عاد کو توحید اور عاجزی کی دعوت (11:50)۔",
                "استغفار کی برکت سے بارش اور قوت میں اضافے کا وعدہ (11:52)۔",
                "قوم کا غرور 'ہم سے زیادہ طاقتور کون ہے؟' (41:15)۔",
                "سات راتیں اور آٹھ دن کی تند و تیز آندھی اور مومنین کی نجات (69:6-7، 11:58)۔"
            ),
            listOf(
                "دعوة عاد إلى التوحيد ونبذ الكبر (الأعراف 7:65، هود 11:50).",
                "الترغيب بالاستغفار لجلب المطر والقوة (هود 11:52).",
                "اغترارهم بقوتهم (فصلت 41:15).",
                "إهلاكهم بريح صرصر سبع ليالٍ وثمانية أيام ونجاة المؤمنين (الحاقة 69:6-7)."
            ),
            listOf(
                "आद को तौहीद और अन्याय त्यागने का संदेश (11:50)।",
                "इस्तिग़फ़ार से वर्षा और शक्ति की बरकत का वादा (11:52)।",
                "'हमसे अधिक बलवान कौन है?' का अहंकार (41:15)।",
                "सात रात आठ दिन की प्रचंड आँधी और ईमानवालों का बचाव (69:6-7)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Prophet Hud (AS) and believing followers", "People of 'Ad / Iram of the Pillars", "Al-Ahqaf (Winding Sand Dunes, 46:21)"),
            listOf("حضرت ہودؑ اور مومنین", "قومِ عاد / ارم ذات العماد", "الاحقاف (46:21)"),
            listOf("هود عليه السلام والمؤمنون", "قوم عاد وإرم ذات العماد", "الأحقاف (الأحقاف 46:21)"),
            listOf("हज़रत हूद (अ.स.) व ईमानवाले", "क़ौम-ए-आद / इरम", "अल-अहक़ाफ़ (46:21)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(11, 50, 58, langMap("Mission to 'Ad & Istighfar", "قوم عاد کو دعوت اور استغفار", "دعوة عاد والاستغفار", "आद को दावत और इस्तिग़फ़ार")),
            ProphetVerseRef(46, 21, 25, langMap("Al-Ahqaf & the Wind", "الاحقاف اور آندھی کا عذاب", "الأحقاف والريح", "अल-अहक़ाफ़ और आँधी"))
        ),
        hadithRefs = emptyList(),
        lessons = listLangMap(
            listOf(
                "Material architecture and worldly power cannot shield a society from moral collapse.",
                "Regular istighfar opens doors of mercy, provision, and inner strength (11:52)."
            ),
            listOf(
                "دنیوی طاقت اور بلند عمارتیں اخلاقی زوال سے نہیں بچا سکتیں۔",
                "استغفار رحمتِ الٰہی، رزق اور روحانی قوت کا ذریعہ ہے (11:52)۔"
            ),
            listOf(
                "القوة المادية والعمران لا تحمي من العذاب عند الكبر والظلم.",
                "الاستغفار سبب للبركة والرزق والقوة (هود 11:52)."
            ),
            listOf(
                "भौतिक शक्ति और भव्य भवन नैतिक पतन से नहीं बचा सकते।",
                "इस्तिग़फ़ार से दया, रिज़्क़ और शक्ति बढ़ती है (11:52)।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Drawn exclusively from Quranic Surahs 7, 11, 26, 46, and 69.",
            "مکمل طور پر قرآنِ مجید کی سورتوں الاعراف، ہود، الشعراء، الاحقاف اور الحاقہ پر مبنی۔",
            "مقتصر على النصوص القرآنية الصريحة في سور الأعراف وهود والشعراء والأحقاف والحاقة.",
            "पूर्णतः क़ुरआन की सूरह 7, 11, 26, 46 और 69 पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "What blessing did Hud (AS) say would follow if 'Ad sought forgiveness from Allah (11:52)?",
                "حضرت ہودؑ نے استغفار کرنے پر اپنی قوم کو کس نعمت کی بشارت دی (11:52)؟",
                "بماذا بشّر هود عليه السلام قومه إذا استغفروا ربهم (هود 11:52)؟",
                "हूद (अ.स.) ने इस्तिग़फ़ार करने पर क्या बरकत मिलने की बात कही (11:52)?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Abundant rain and added strength", "Gold mines", "Conquest of Rome"),
                AppLanguage.URDU to listOf("موسلا دھار بارش اور قوت میں اضافہ", "سونے کی کانیں", "روم کی فتح"),
                AppLanguage.ARABIC to listOf("إرسال السماء مدراراً وزيادة في القوة", "كنوز الذهب", "ملك الروم"),
                AppLanguage.HINDI to listOf("भरपूर वर्षा और अतिरिक्त शक्ति", "सोने की खदानें", "रोम की विजय")
            ),
            0,
            langMap(
                "Quran 11:52 promises Allah would send abundant rain from the sky and add strength to their strength.",
                "سورۃ ہود 11:52 میں بارش کی کثرت اور قوت میں اضافے کا ذکر ہے۔",
                "في هود 11:52: يرسل السماء عليكم مدراراً ويزدكم قوة إلى قوتكم.",
                "क़ुरआन 11:52 में आकाश से भरपूर वर्षा व शक्ति में वृद्धि का उल्लेख है।"
            )
        )
    ),
    ProphetStory(
        id = "salih",
        orderNumber = 5,
        arabicName = "صالح عليه السلام",
        names = langMap("Salih (AS)", "حضرت صالح علیہ السلام", "صالح عليه السلام", "हज़रत सालिह (अ.स.)"),
        titles = langMap("Messenger to Thamud", "قومِ ثمود کے پیغمبر", "رسول الله إلى ثمود", "क़ौम-ए-समूद के रसूल"),
        category = ProphetCategory.EARLY_NATIONS,
        summaryIntro = langMap(
            "Sent to the people of Thamud at Al-Hijr, who carved homes into mountains and demanded a clear sign — Allah sent them the miraculous she-camel, which they harmed.",
            "آپ کو قومِ ثمود (الحجر) کی طرف بھیجا گیا جو پہاڑ تراش کر گھر بناتے تھے۔ ان کے مطالبے پر اللہ نے معجزانہ اونٹنی بطور نشانی عطا فرمائی۔",
            "أرسله الله إلى ثمود في الحجر الذين كانوا ينحتون الجبال بيوتاً، وآتاهم الله الناقة آيةً مبصرة فعقروها.",
            "उन्हें अल-हिज्र की क़ौम-ए-समूद की ओर भेजा गया जो पहाड़ तराशकर घर बनाती थी; निशानी के रूप में ऊँटनी भेजी गई।"
        ),
        detailedIntro = langMap(
            "Thamud succeeded 'Ad and mastered carving dwellings in stone mountains (15:82). When Salih (AS) called them to worship Allah alone, they demanded a miraculous sign. Allah brought forth a she-camel (Naqatullah) with alternating water-drinking rights and forbade harming her (7:73). Nine wicked conspirators plotted and hamstrung the camel, challenging the punishment, and a mighty blast (Sayhah) overtook the wrongdoers.",
            "قومِ ثمود عاد کے بعد آباد ہوئی اور پہاڑوں میں تراشیدہ محل بناتی تھی (15:82)۔ حضرت صالحؑ کی دعوت پر انہوں نے نشانی مانگی تو اللہ نے ایک خاص اونٹنی عطا فرمائی جس کے پینے کا دن مقرر تھا، مگر سرکش لوگوں نے اسے ہلاک کر دیا اور ایک سخت چنگھاڑ (صیحہ) نے انہیں تباہ کر دیا۔",
            "سكنت ثمود الحجر ونحتت من الجبال بيوتاً آمنين. ولما طلبوا آية أخرج الله لهم ناقة وجعل لها شرباً معلوماً وحذّرهم من مسّها بسوء، فعقروها وأخذتهم الصيحة ونجّى الله صالحاً والمؤمنين.",
            "समूद पहाड़ों में तराशकर घर बनाते थे (15:82)। उनके निशानी माँगने पर अल्लाह ने ऊँटनी भेजी और उसे नुकसान न पहुँचाने की चेतावनी दी (7:73)। शरारती गुट ने उसे मार डाला और भयंकर आवाज़ (सयहह) ने उन्हें नष्ट किया।"
        ),
        quranicContext = langMap(
            "Recounted in Al-A'raf (7:73-79), Hud (11:61-68), Al-Hijr (15:80-84), Ash-Shu'ara (26:141-159), and Ash-Shams (91:11-15).",
            "قرآن میں الاعراف (7:73-79)، ہود (11:61-68)، الحجر (15:80-84) اور الشمس (91:11-15) میں مذکور ہے۔",
            "وردت في الأعراف (7:73-79) وهود (11:61-68) والحجر (15:80-84) والشعراء (26:141-159) والشمس (91:11-15).",
            "आराफ़ (7:73-79), हूद (11:61-68), हिज्र (15:80-84) और शम्स (91:11-15) में उल्लेख है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Salih reminds Thamud of Allah's blessings and calls them to repentance (11:61).",
                "The sign of the She-Camel of Allah and fair water-sharing rule (26:154-156).",
                "Rebellion and hamstringing of the she-camel; three days of respite before punishment (11:65).",
                "The mighty blast (Sayhah) destroys rejecters; Salih and believers are saved (11:66-67)."
            ),
            listOf(
                "ثمود کو اللہ کی نعمتیں یاد دلانا اور استغفار کی دعوت (11:61)۔",
                "ناقتہ اللہ کا معجزہ اور پانی کی منصفانہ باری (26:154-156)۔",
                "سرکشی میں اونٹنی کی کونچیں کاٹنا اور تین دن کی مہلت (11:65)۔",
                "صیحہ (سخت آواز) سے عذاب اور حضرت صالحؑ و مومنین کی نجات (11:66-67)۔"
            ),
            listOf(
                "تذكير ثمود بنعم الله والدعوة إلى التوحيد والاستغفار (هود 11:61).",
                "آية الناقة وتنظيم الشرب بالقسط (الشعراء 26:154-156).",
                "عقر الناقة وإمهالهم ثلاثة أيام (هود 11:65).",
                "أخذهم بالصيحة ونجاة صالح والذين آمنوا (هود 11:66-67)."
            ),
            listOf(
                "समूद को अल्लाह की नेमतें याद दिलाना और तौहीद की दावत (11:61)।",
                "ऊँटनी का चमत्कार और पानी की न्यायपूर्ण बारी (26:154-156)।",
                "ऊँटनी की हत्या और तीन दिन की मोहलत (11:65)।",
                "भयंकर चीख़ (सयहह) से विनाश और सालिह (अ.स.) का बचाव (11:66-67)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Prophet Salih (AS) and the believers", "People of Thamud", "Al-Hijr (north-west Arabia, 15:80)"),
            listOf("حضرت صالحؑ اور مومنین", "قومِ ثمود", "الحجر (15:80)"),
            listOf("صالح عليه السلام والمؤمنون", "قوم ثمود", "الحِجر (الحجر 15:80)"),
            listOf("हज़रत सालिह (अ.स.) व ईमानवाले", "क़ौम-ए-समूद", "अल-हिज्र (15:80)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(11, 61, 68, langMap("Mission to Thamud & the Camel", "ثمود کو دعوت اور اونٹنی", "دعوة ثمود والناقة", "समूद व ऊँटनी का प्रसंग")),
            ProphetVerseRef(91, 11, 15, langMap("Lesson of Thamud", "ثمود کی سرکشی کا انجام", "عاقبة طغيان ثمود", "समूद की अवज्ञा का परिणाम"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3380",
                "https://sunnah.com/bukhari:3380",
                langMap(
                    "Passing by Al-Hijr, the Prophet ﷺ taught not to enter places of punished nations except weeping or taking heed.",
                    "الحجر سے گزرتے ہوئے نبی اکرم ﷺ نے عذاب یافتہ مقامات سے عبرت اور خوفِ خدا کے ساتھ گزرنے کی تعلیم دی۔",
                    "مرّ النبي ﷺ بالحِجر ووجّه أصحابه إلى الاتعاظ والخشية عند المرور بمنازل المعذبين.",
                    "अल-हिज्र से गुज़रते समय नबी ﷺ ने दंडित जातियों के स्थलों पर सीख व भय के साथ जाने की शिक्षा दी।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Even miracles cannot soften hearts hardened by arrogance.",
                "Violating sacred trust and justice harms an entire community."
            ),
            listOf(
                "متکبر دلوں کو کھلے معجزات بھی قائل نہیں کر سکتے جب تک ہدایت کی طلب نہ ہو۔",
                "عہد شکنی اور ظلم پوری قوم کی تباہی کا باعث بنتے ہیں۔"
            ),
            listOf(
                "المعجزات لا تنفع من أصمّ الكبر قلبه.",
                "العدوان الجماعي والسكوت عن الظلم يعمّ بالعقوبة."
            ),
            listOf(
                "अहंकारी हृदय चमत्कार देखकर भी सत्य नहीं अपनाता।",
                "अमानत व न्याय का उल्लंघन पूरे समाज को हानि पहुँचाता है।"
            )
        ),
        quranicDuas = emptyList(),
        reliabilityNotes = langMap(
            "Based on Quran 7:73-79, 11:61-68, 15:80-84, 91:11-15 and Sahih al-Bukhari 3380.",
            "قرآن کریم (7:73-79، 11:61-68، 15:80-84، 91:11-15) اور صحیح بخاری 3380 پر مبنی۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3380).",
            "क़ुरआन (7:73-79, 11:61-68, 15:80-84, 91:11-15) और सहीह बुख़ारी 3380 पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "What specific sign did Allah give to Thamud as a test alongside Prophet Salih (AS)?",
                "حضرت صالحؑ کے ساتھ قومِ ثمود کو آزمائش کے لیے کون سی نشانی دی گئی؟",
                "ما الآية التي أرسلها الله لقوم ثمود مع صالح عليه السلام؟",
                "सालिह (अ.स.) के साथ क़ौम-ए-समूद को परीक्षा के रूप में कौन-सी निशानी दी गई?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("A miraculous she-camel with shared water rights", "A flying throne", "A spring of honey"),
                AppLanguage.URDU to listOf("معجزانہ اونٹنی اور پانی کی مقررہ باری", "اڑتا ہوا تخت", "شہد کا چشمہ"),
                AppLanguage.ARABIC to listOf("الناقة مع شرب ماء معلوم", "عرش طائر", "عين عسل"),
                AppLanguage.HINDI to listOf("चमत्कारी ऊँटनी और पानी की निश्चित बारी", "उड़ता सिंहासन", "शहद का झरना")
            ),
            0,
            langMap(
                "Quran 7:73 and 26:155 clearly describe the she-camel of Allah as a clear sign and test.",
                "قرآن (7:73، 26:155) میں ناقتہ اللہ کو واضح نشانی اور آزمائش قرار دیا گیا ہے۔",
                "ورد في الأعراف 7:73 والشعراء 26:155 ذكر الناقة آيةً لقوم ثمود.",
                "क़ुरआन 7:73 और 26:155 में अल्लाह की ऊँटनी को स्पष्ट निशानी बताया गया है।"
            )
        )
    ),
    ProphetStory(
        id = "ibrahim",
        orderNumber = 6,
        arabicName = "إبراهيم عليه السلام",
        names = langMap("Ibrahim (AS)", "حضرت ابراہیم علیہ السلام", "إبراهيم عليه السلام", "हज़रत इब्राहीम (अ.स.)"),
        titles = langMap("Khalilullah — Friend of Allah", "خلیل اللہ اور جدِ انبیاء", "خليل الرحمن وإمام الحنفاء", "ख़लीलुल्लाह व हनीफ़ों के इमाम"),
        category = ProphetCategory.ULUL_AZM,
        summaryIntro = langMap(
            "Khalilullah (Intimate Friend of Allah) and an Ulul-Azm Messenger whose unwavering devotion to pure Tawhid, triumph over the fire, and rebuilding of the Ka'bah shaped monotheism.",
            "خلیل اللہ اور اولوالعزم پیغمبر جن کی غیر متزلزل توحید، آگ کا ٹھنڈا ہونا اور خانہ کعبہ کی تعمیر توحید کی تاریخ کے روشن باب ہیں۔",
            "خليل الله وأبو الأنبياء، حطم الأصنام ودعا إلى التوحيد الخالص فنجاه الله من النار، ورفع قواعد البيت الحرام مع ابنه إسماعيل.",
            "ख़लीलुल्लाह व उलुल-अज़्म रसूल, जिनका अडिग तौहीद, आग का ठंडा होना और काबा का निर्माण एकेश्वरवाद के आधार स्तंभ हैं।"
        ),
        detailedIntro = langMap(
            "Raised among idolaters in Ur/Babylon, Ibrahim (AS) reasoned cleanly against idol, star, moon, and sun worship, declaring: 'I turn my face to Him who created the heavens and the earth' (6:79). When his people threw him into a blazing fire, Allah commanded the fire to be coolness and peace (21:69). Later, migrating with Sarah and Hajar to Makkah and Palestine, he passed the profound sacrifice trial with his son Ismail and raised the foundations of the Sacred House (2:127).",
            "بت پرست ماحول میں حضرت ابراہیمؑ نے ستاروں، چاند اور سورج کے بجائے خالقِ کائنات کی بندگی کا اعلان فرمایا (6:79)۔ جب قوم نے آپ کو دہکتی آگ میں ڈالا تو اللہ کے حکم سے آگ ٹھنڈی اور سلامتی بن گئی (21:69)۔ بعد ازاں مکہ میں حضرت ہاجرہؑ و اسماعیلؑ کو آباد فرمایا، عظیم قربانی کی آزمائش میں کامیاب ہوئے اور کعبۃ اللہ کی بنیادیں بلند کیں (2:127)۔",
            "ناظر قومه في عبادة الأصنام والكواكب وأعلن الحنيفية الخالصة (الأنعام 6:79)، فألقوه في النار فجعلها الله برداً وسلاماً عليه (الأنبياء 21:69). ثم هاجر وأسكن هاجر وإسماعيل بمكة، ونجح في بلاء الرؤيا العظيم، ورفع مع إسماعيل قواعد البيت الحرام (البقرة 2:127).",
            "मूर्तिपूजक परिवेश में उन्होंने नक्षत्रों व मूर्तियों के विरुद्ध तौहीद का ऐलान किया (6:79)। दहकती आग में डाले जाने पर अल्लाह के हुक्म से आग ठंडी और सुरक्षित हो गई (21:69)। मक्का में इस्माईल (अ.स.) के साथ काबा की नींव उठाई (2:127) और क़ुर्बानी की महान परीक्षा उत्तीर्ण की।"
        ),
        quranicContext = langMap(
            "Highlighted across Al-Baqarah (2:124-132), Al-An'am (6:74-83), Surah Ibrahim (14:35-41), Al-Anbiya (21:51-73), and As-Saffat (37:83-113).",
            "قرآن میں البقرہ (2:124-132)، الانعام (6:74-83)، ابراہیم (14:35-41)، الانبیاء (21:51-73) اور الصافات (37:83-113) میں مفصل آیا ہے۔",
            "وردت سيرته في البقرة (2:124-132) والأنعام (6:74-83) وإبراهيم (14:35-41) والأنبياء (21:51-73) والصافات (37:83-113).",
            "बक़रा (2:124-132), अनआम (6:74-83), इब्राहीम (14:35-41), अंबिया (21:51-73) और साफ़्फ़ात (37:83-113) में वर्णित।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Intellectual reasoning with his father and people against celestial and carved idols (6:74-83, 21:51-67).",
                "Cast into the fire, which became coolness and safety by Allah's command (21:68-69).",
                "Migration to the Levant & Makkah; trial of sacrifice with Ismail redeemed with a great sacrifice (37:102-107).",
                "Raising the foundations of the Ka'bah with Ismail (2:127) and calling humanity to Hajj (22:27)."
            ),
            listOf(
                "والد اور قوم کے سامنے بتوں اور ستاروں کے باطل ہونے کا استدلال (6:74-83، 21:51-67)۔",
                "آتشِ نمرود کا اللہ کے حکم سے ٹھنڈا اور سلامتی بن جانا (21:68-69)۔",
                "شام و مکہ کی ہجرت اور حضرت اسماعیلؑ کے ساتھ ذبحِ عظیم کی آزمائش (37:102-107)۔",
                "حضرت اسماعیلؑ کے ساتھ کعبۃ اللہ کی تعمیر اور حج کی ندا (2:127، 22:27)۔"
            ),
            listOf(
                "محاجّة أبيه وقومه في التوحيد وتحطيم الأصنام (الأنعام 6:74-83، الأنبياء 21:51-67).",
                "الإلقاء في النار وجعلها الله برداً وسلاماً (الأنبياء 21:68-69).",
                "الهجرة إلى الشام ومكة وبلاء الذبح العظيم وفداؤه (الصافات 37:102-107).",
                "رفع قواعد الكعبة مع إسماعيل والدعوة إلى الحج (البقرة 2:127، الحج 22:27)."
            ),
            listOf(
                "पिता व क़ौम से मूर्तिपूजा के विरुद्ध तर्क और मूर्तियाँ तोड़ना (6:74-83, 21:51-67)।",
                "दहकती आग का अल्लाह के हुक्म से शीतल और सुरक्षित होना (21:68-69)।",
                "मक्का में प्रवास व इस्माईल (अ.स.) के साथ क़ुर्बानी की परीक्षा (37:102-107)।",
                "इस्माईल (अ.स.) के साथ काबा की नींव उठाना और हज की पुकार (2:127, 22:27)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Ibrahim (AS), spouses Sarah & Hajar, sons Ismail & Ishaq, nephew Lut (AS)", "Makkah (Al-Bayt Al-Haram) & Sacred Land (Palestine)"),
            listOf("حضرت ابراہیمؑ، ازواج سارہؑ و ہاجرہؑ، فرزندان اسماعیلؑ و اسحاقؑ، بھتیجے لوطؑ", "مکہ مکرمہ (بیت اللہ) اور ارضِ مقدس"),
            listOf("إبراهيم وزوجتاه سارة وهاجر وابناه إسماعيل وإسحاق وابن أخيه لوط", "مكة المكرمة والأرض المباركة"),
            listOf("इब्राहीम (अ.स.), सारा व हाजरा, पुत्र इस्माईल व इसहाक़, लूत (अ.स.)", "मक्का (काबा) और पवित्र भूमि")
        ),
        verseRefs = listOf(
            ProphetVerseRef(2, 127, 129, langMap("Raising the Ka'bah Foundations", "کعبہ کی تعمیر اور دعا", "بناء الكعبة المشرفة", "काबा की नींव उठाना")),
            ProphetVerseRef(21, 68, 70, langMap("Saved from the Fire", "آگ کا سلامتی بننا", "النجاة من النار", "आग का ठंडा होना")),
            ProphetVerseRef(14, 35, 41, langMap("Duas for Makkah & Descendants", "مکہ اور اولاد کے لیے دعائیں", "دعاء إبراهيم لمكة والذرية", "मक्का व संतान हेतु दुआएँ"))
        ),
        hadithRefs = listOf(
            ProphetHadithRef(
                "Sahih al-Bukhari 3364",
                "https://sunnah.com/bukhari:3364",
                langMap(
                    "Long authentic narration of Ibrahim settling Hajar and infant Ismail in barren Makkah, the miracle of Zamzam, and building the Ka'bah.",
                    "صحیح بخاری میں حضرت ہاجرہؑ و اسماعیلؑ کو مکہ میں بسانے، زمزم کے معجزے اور کعبہ کی تعمیر کی مفصل حدیث۔",
                    "الحديث الطويل في إسكان هاجر وإسماعيل بمكة، ونبع ماء زمزم، وبناء الكعبة.",
                    "सहीह बुख़ारी में हाजरा व इस्माईल के मक्का में बसने, ज़मज़म और काबा निर्माण की विस्तृत हदीस।"
                )
            )
        ),
        lessons = listLangMap(
            listOf(
                "Pure devotion to Allah brings inner courage before any earthly authority.",
                "Legacy endures through righteous children, generosity, and grateful prayer."
            ),
            listOf(
                "خالص توحید انسان کو دنیا کی ہر طاقت کے سامنے بے خوف بنا دیتی ہے۔",
                "نیک اولاد، مہمان نوازی اور دعا دائمی صدقۂ جاریہ ہیں۔"
            ),
            listOf(
                "التوحيد الخالص يورث الشجاعة والثبات في وجه الطغيان.",
                "الدعاء الصالح للذرية والبلد وبناء المؤسسات الإيمانية أثر باقٍ."
            ),
            listOf(
                "ख़ालिस तौहीद इंसान को हर सांसारिक भय से ऊपर उठा देती है।",
                "नेक संतान, आतिथ्य-सत्कार और दुआ अमिट विरासत हैं।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(14, 40, 41, langMap("Make me & my offspring establishers of Salah", "رب اجعلنی مقیم الصلوٰۃ", "رب اجعلني مقيم الصلاة", "मुझे व मेरी संतान को नमाज़ क़ायम करने वाला बना")),
            ProphetDuaRef(26, 83, 85, langMap("Grant me wisdom and join me with the righteous", "رب ہب لی حکماً", "رب هب لي حكماً", "मुझे हिकमत दे और नेक लोगों में मिला"))
        ),
        reliabilityNotes = langMap(
            "Verified directly via Quran Surahs 2, 6, 14, 21, 37 and Sahih al-Bukhari 3364.",
            "قرآن مجید اور صحیح بخاری 3364 سے مکمل طور پر مستند و ثابت۔",
            "مستند إلى القرآن الكريم وصحيح البخاري (3364).",
            "क़ुरआन और सहीह बुख़ारी 3364 से सीधे प्रमाणित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "Which two figures raised the foundations of the Sacred House (Ka'bah) in Quran 2:127?",
                "قرآن (2:127) کے مطابق خانہ کعبہ کی بنیادیں کن دو پیغمبروں نے بلند کیں؟",
                "من هما النبيان اللذان رفعا قواعد البيت الحرام (البقرة 2:127)؟",
                "क़ुरआन 2:127 के अनुसार काबा की नींव किन दो नबियों ने उठाई?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Ibrahim (AS) and Ismail (AS)", "Musa (AS) and Harun (AS)", "Dawud (AS) and Sulayman (AS)"),
                AppLanguage.URDU to listOf("حضرت ابراہیمؑ اور حضرت اسماعیلؑ", "حضرت موسیٰؑ اور حضرت ہارونؑ", "حضرت داؤدؑ اور حضرت سلیمانؑ"),
                AppLanguage.ARABIC to listOf("إبراهيم وإسماعيل عليهما السلام", "موسى وهارون عليهما السلام", "داود وسليمان عليهما السلام"),
                AppLanguage.HINDI to listOf("इब्राहीम (अ.स.) और इस्माईल (अ.स.)", "मूसा और हारून (अ.स.)", "दाऊद और सुलैमान (अ.स.)")
            ),
            0,
            langMap(
                "Quran 2:127 states: 'And when Ibrahim was raising the foundations of the House and [with him] Ismail...'",
                "البقرہ 2:127 میں حضرت ابراہیمؑ اور حضرت اسماعیلؑ دونوں کا واضح ذکر ہے۔",
                "البقرة 2:127: وإذ يرفع إبراهيم القواعد من البيت وإسماعيل.",
                "क़ुरआन 2:127 में इब्राहीम व इस्माईल (अ.स.) द्वारा नींव उठाने का स्पष्ट उल्लेख है।"
            )
        )
    ),
    ProphetStory(
        id = "lut",
        orderNumber = 7,
        arabicName = "لوط عليه السلام",
        names = langMap("Lut (AS)", "حضرت لوط علیہ السلام", "لوط عليه السلام", "हज़रत लूत (अ.स.)"),
        titles = langMap("Nephew of Ibrahim & Preacher of Purity", "عفت و پاکیزگی کے داعی پیغمبر", "نبي الله الداعي إلى العفة والطهارة", "पवित्रता व नैतिकता के नबी"),
        category = ProphetCategory.EARLY_NATIONS,
        summaryIntro = langMap(
            "Believed in Ibrahim (AS) and migrated with him before being sent to the towns of Sodom to call them back from unprecedented moral corruption to purity.",
            "آپ حضرت ابراہیمؑ کے بھتیجے اور ان پر ایمان لانے والوں میں تھے۔ آپ کو بستی کی بے حیائی کے خلاف پاکیزگی اور تقویٰ کی دعوت کے لیے بھیجا گیا۔",
            "آمن بإبراهيم وهاجر معه ثم أرسله الله إلى قومه ليدعوهم إلى الطهارة والأخلاق، فكذبوه فأنجى الله لوطاً وأهله إلا امرأته.",
            "इब्राहीम (अ.स.) पर ईमान लाकर उनके साथ हिजरत की और अपनी बस्ती को अनैतिकता छोड़ पवित्रता अपनाने की दावत दी।"
        ),
        detailedIntro = langMap(
            "After believing in Ibrahim's message (29:26), Lut (AS) settled near the plains of Jordan/Dead Sea region. He repeatedly warned his people with gentleness and moral clarity against indecency, highway robbery, and brazen hostility to guests (29:28-29). When they threatened to expel him merely for calling to purity (7:82), angels visited Ibrahim first and then came as guests to Lut, guiding him to leave by night before the towns were overturned.",
            "حضرت ابراہیمؑ پر ایمان لانے کے بعد (29:26) حضرت لوطؑ کو اپنی قوم کی طرف بھیجا گیا۔ آپ نے انہیں بے حیائی اور مسافروں پر ظلم سے منع فرمایا (29:28-29)۔ انہوں نے پاکیزگی پسند ہونے پر آپ کو شہر بدر کرنے کی دھمکی دی (7:82)۔ بالآخر فرشتوں نے آپ کو رات کے وقت خاندان سمیت نکلنے کا حکم دیا اور نافرمان بستی الٹ دی گئی۔",
            "هاجر لوط مع إبراهيم ثم بعثه الله إلى قوم يأتون الفاحشة ويقطعون السبيل، فدعاهم إلى التقوى، فقالوا: أخرجوا آل لوط إنهم أناس يتطهرون. وجاءت الملائكة فأخرجوه بليل إلا امرأته، وجُعل عالي القرية سافلها.",
            "इब्राहीम (अ.स.) पर ईमान लाकर (29:26) लूत (अ.स.) ने अपनी बस्ती को अश्लीलता व राहजनी छोड़ने का संदेश दिया। बस्ती वालों ने पवित्रता के कारण उन्हें निकालने की धमकी दी (7:82)। तब फ़रिश्तों ने रात में निकलने का आदेश दिया और बस्ती पलट दी गई।"
        ),
        quranicContext = langMap(
            "Narrated in Al-A'raf (7:80-84), Hud (11:77-83), Al-Hijr (15:57-77), Ash-Shu'ara (26:160-175), and Al-Ankabut (29:26-35).",
            "آپ کا واقعہ الاعراف (7:80-84)، ہود (11:77-83)، الحجر (15:57-77) اور العنکبوت (29:26-35) میں ہے۔",
            "وردت قصته في الأعراف (7:80-84) وهود (11:77-83) والحجر (15:57-77) والعنكبوت (29:26-35).",
            "उनका वर्णन आराफ़ (7:80-84), हूद (11:77-83), हिज्र (15:57-77) और अनकबूत (29:26-35) में है।"
        ),
        mainEvents = listLangMap(
            listOf(
                "Believed in Ibrahim (AS) and migrated for the sake of Allah (29:26).",
                "Warned his people against indecency and mistreatment of travelers (29:28-29).",
                "Angelic messengers reassure Lut (11:81) and instruct him to depart at night without looking back.",
                "Deliverance of Lut and his believing daughters; destruction of the corrupt towns and his betraying wife (11:82-83)."
            ),
            listOf(
                "حضرت ابراہیمؑ پر ایمان اور راہِ خدا میں ہجرت (29:26)۔",
                "قوم کو فحاشی اور راہزنی سے منع فرمانا (29:28-29)۔",
                "فرشتوں کی آمد، تسلی اور رات میں بستی چھوڑنے کا حکم (11:81)۔",
                "حضرت لوطؑ اور ایماندار اہلِ خانہ کی نجات اور مجرم بستی کی ہلاکت (11:82-83)۔"
            ),
            listOf(
                "إيمانه بإبراهيم وهجرته معه (العنكبوت 29:26).",
                "نهي قومه عن الفاحشة وقطع السبيل (العنكبوت 29:28-29).",
                "بشارة الملائكة والأمر بالخروج ليلاً (هود 11:81).",
                "نجاة لوط وأهله إلا امرأته وهلاك القرية الظالمة (هود 11:82-83)."
            ),
            listOf(
                "इब्राहीम (अ.स.) पर ईमान और हिजरत (29:26)।",
                "अश्लीलता और यात्रियों के उत्पीड़न के विरुद्ध चेतावनी (29:28-29)।",
                "फ़रिश्तों का आगमन और रात में निकलने का निर्देश (11:81)।",
                "लूत (अ.स.) व बेटियों की रक्षा तथा बस्ती व अवज्ञाकारी पत्नी का विनाश (11:82-83)।"
            )
        ),
        peopleAndPlaces = listLangMap(
            listOf("Prophet Lut (AS), his believing family, angelic guests", "Wife of Lut (who sided with wrongdoers)", "Sodom / Al-Mu'tafikah (the overturned towns)"),
            listOf("حضرت لوطؑ، ایماندار اہلِ خانہ، مہمان فرشتے", "لوطؑ کی نافرمان بیوی", "المؤتفکات (الٹی ہوئی بستیاں)"),
            listOf("لوط عليه السلام وأهله المؤمنون والملائكة", "امرأته الخائنة", "المؤتفكات"),
            listOf("हज़रत लूत (अ.स.), ईमानवाले परिजन, फ़रिश्ते", "लूत (अ.स.) की पत्नी", "अल-मुअतफ़िकात (पलटी गई बस्तियाँ)")
        ),
        verseRefs = listOf(
            ProphetVerseRef(11, 77, 83, langMap("Guests & Deliverance at Dawn", "فرشتوں کی آمد اور نجات", "ضيوف لوط والنجاة", "फ़रिश्ते और भोर में बचाव")),
            ProphetVerseRef(29, 28, 30, langMap("Moral Warning & Dua", "اخلاقی تنبیہ اور دعا", "الدعوة إلى الطهارة", "नैतिक चेतावनी और दुआ"))
        ),
        hadithRefs = emptyList(),
        lessons = listLangMap(
            listOf(
                "Faith and personal accountability stand above family ties (Quran 66:10).",
                "Respecting guests and upholding moral boundaries protects society."
            ),
            listOf(
                "ایمان اور ذاتی جواب دہی خاندانی رشتوں سے بالاتر ہے (قرآن 66:10)۔",
                "مہمان کا احترام اور اخلاقی حدود کی پاسداری معاشرے کو محفوظ رکھتی ہے۔"
            ),
            listOf(
                "صلاح الفرد ومسؤوليته عند الله مستقلان عن القرابة (التحريم 66:10).",
                "إكرام الضيف وحفظ الأخلاق أمان للمجتمعات."
            ),
            listOf(
                "ईमान व व्यक्तिगत जवाबदेही पारिवारिक संबंधों से ऊपर है (66:10)।",
                "अतिथि-सम्मान व नैतिक मर्यादा समाज की रक्षा करती है।"
            )
        ),
        quranicDuas = listOf(
            ProphetDuaRef(26, 169, 169, langMap("Save me and my family from their deeds", "رب نجنی واہلی", "رب نجني وأهلي مما يعملون", "मेरे परिवार को इनके कर्मों से बचा ले"))
        ),
        reliabilityNotes = langMap(
            "Strictly derived from Quran Surahs 7, 11, 15, 26, 29, and 66.",
            "قرآن کریم (7، 11، 15، 26، 29 اور 66) کی آیات پر مبنی۔",
            "مستند إلى الآيات القرآنية الصريحة في سور الأعراف وهود والحجر والشعراء والعنكبوت والتحريم.",
            "पूर्णतः क़ुरआन की सूरह 7, 11, 15, 26, 29 और 66 पर आधारित।"
        ),
        quizQuestion = ProphetQuizQuestion(
            langMap(
                "In Quran 29:26, which Prophet did Lut (AS) believe in and accompany in migration?",
                "قرآن (29:26) کے مطابق حضرت لوطؑ کس نبی پر ایمان لائے اور ان کے ساتھ ہجرت فرمائی؟",
                "بمن آمن لوط وهاجر معه كما في سورة العنكبوت (29:26)؟",
                "क़ुरआन 29:26 के अनुसार लूत (अ.स.) किस नबी पर ईमान लाए और उनके साथ हिजरत की?"
            ),
            mapOf(
                AppLanguage.ENGLISH to listOf("Ibrahim (AS)", "Musa (AS)", "Nuh (AS)"),
                AppLanguage.URDU to listOf("حضرت ابراہیمؑ", "حضرت موسیٰؑ", "حضرت نوحؑ"),
                AppLanguage.ARABIC to listOf("إبراهيم عليه السلام", "موسى عليه السلام", "نوح عليه السلام"),
                AppLanguage.HINDI to listOf("हज़रत इब्राहीम (अ.स.)", "हज़रत मूसा (अ.स.)", "हज़रत नूह (अ.स.)")
            ),
            0,
            langMap(
                "Quran 29:26 states: 'And Lut believed him [Ibrahim]. [Ibrahim] said: Indeed I will emigrate for the sake of my Lord.'",
                "العنکبوت 29:26 میں بیان ہے کہ حضرت لوطؑ حضرت ابراہیمؑ پر ایمان لائے۔",
                "العنكبوت 29:26: فآمن له لوط.",
                "क़ुरआन 29:26 में उल्लेख है कि लूत (अ.स.) इब्राहीम (अ.स.) पर ईमान लाए।"
            )
        )
    )
)
