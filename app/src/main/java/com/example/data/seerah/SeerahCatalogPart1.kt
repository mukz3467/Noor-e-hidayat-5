package com.example.data.seerah

internal object SeerahCatalogPart1 {
    val articles: List<SeerahArticle> = listOf(
        SeerahArticle(
            id = "seerah_01_birth_lineage",
            era = SeerahEra.BEFORE_PROPHETHOOD,
            orderIndex = 1,
            title = localized4(
                "Noble Lineage, Birth & Childhood in Makkah",
                "مکہ مکرمہ میں ولادت باسعادت اور بچپن",
                "النسب الشريف والمولد والنشأة في مكة",
                "मक्का में पावन जन्म, वंश और बचपन"
            ),
            approxChronology = localized4(
                "Year of the Elephant (~570 CE, approximately 53 BH). The exact day of birth in Rabi' al-Awwal is reported differently among historians (most commonly Monday, 9th or 12th).",
                "عام الفیل (~570 عیسوی، تقریباً 53 قبل ہجری)۔ ربیع الاول کی درست تاریخ میں مؤرخین کے مختلف اقوال ہیں (پیر 9 یا 12 ربیع الاول معروف ہیں)۔",
                "عام الفيل (نحو 570م، 53 قبل الهجرة تقريباً)؛ اتفق العلماء على ولادته يوم الاثنين واختلف المؤرخون في اليوم من ربيع الأول (كالتاسع أو الثاني عشر).",
                "आमुल-फ़ील (~570 ई., लगभग 53 हिजरी पूर्व)। रबीउल-अव्वल की सटीक तारीख पर इतिहासकारों में मतभेद है (सोमवार 9 या 12 अधिक प्रसिद्ध हैं)।"
            ),
            overview = localized4(
                "Prophet Muhammad ﷺ was born into Banu Hashim of Quraysh in Makkah after the passing of his father Abdullah. His mother Aminah bint Wahb sent him to the desert tribe of Banu Sa'd under Halimah al-Sa'diyyah, where he grew up in eloquence and resilience. After his mother passed away at Abwa when he was around six years old, his grandfather Abd al-Muttalib and later his uncle Abu Talib cared for him lovingly.",
                "رسول اللہ ﷺ کی ولادت مکہ مکرمہ میں قریش کے معزز قبیلے بنو ہاشم میں ہوئی۔ والد عبداللہ کا انتقال ولادت سے قبل ہو چکا تھا۔ والدہ آمنہ بنت وہب نے رضاعت کے لیے بنو سعد کی حضرت حلیمہ سعدیہ کے سپرد فرمایا۔ تقریباً چھ سال کی عمر میں والدہ کے انتقال کے بعد دادا عبدالمطلب اور پھر چچا ابو طالب نے نہایت شفقت سے پرورش فرمائی۔",
                "وُلد النبي محمد ﷺ في بني هاشم من قريش بمكة بعد وفاة أبيه عبد الله. أرضعته حليمة السعدية في بادية بني سعد، وتوفيت أمه آمنة بالأبواء وهو في نحو السادسة، فكفله جده عبد المطلب ثم عمه أبو طالب.",
                "पैगंबर मुहम्मद ﷺ का जन्म मक्का के कुरैश के प्रतिष्ठित बनू हाशिम वंश में पिता अब्दुल्लाह के निधन के बाद हुआ। माता आमिना ने दूध पिलाने और परवरिश के लिए बनू साद की हलीमा सादिया के पास भेजा। लगभग छह वर्ष की आयु में माता के निधन के बाद दादा अब्दुल मुत्तलिब और फिर चाचा अबू तालिब ने स्नेहपूर्वक पालन-पोषण किया।"
            ),
            contextPeoplePlaces = localized4(
                "People: Abdullah ibn Abd al-Muttalib, Aminah bint Wahb, Halimah al-Sa'diyyah, Abd al-Muttalib, Abu Talib, Barakah (Umm Ayman). Places: Makkah, Ta'if uplands (Banu Sa'd), Abwa.",
                "شخصیات: حضرت عبداللہ، حضرت آمنہ، حضرت حلیمہ سعدیہ، عبدالمطلب، ابو طالب، ام ایمن۔ مقامات: مکہ مکرمہ، وادیِ بنو سعد، ابواء۔",
                "الشخصيات: عبد الله بن عبد المطلب، آمنة بنت وهب، حليمة السعدية، عبد المطلب، أبو طالب، أم أيمن. الأماكن: مكة، بادية بني سعد، الأبواء.",
                "व्यक्ति: अब्दुल्लाह, आमिना, हलीमा सादिया, अब्दुल मुत्तलिब, अबू तालिब, उम्मे ऐमन। स्थान: मक्का, बनू साद, अबवा।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Ad-Duha 93:6)",
                    referenceCode = "Quran 93:6",
                    arabicQuote = "أَلَمْ يَجِدْكَ يَتِيمًا فَآوَىٰ",
                    quoteTranslation = localized4(
                        "Did He not find you an orphan and give [you] refuge?",
                        "کیا اس نے آپ کو یتیم نہیں پایا پھر پناہ دی؟",
                        "أَلَمْ يَجِدْكَ يَتِيمًا فَآوَىٰ",
                        "क्या उसने आपको अनाथ नहीं पाया और फिर आश्रय दिया?"
                    ),
                    reliabilityNote = localized4(
                        "Direct Quranic testimony affirming the Prophet's ﷺ orphanhood and divine care.",
                        "قرآنی نص جو نبی کریم ﷺ کی یتیمی اور ربانی کفالت کی تصدیق کرتی ہے۔",
                        "نص قرآني قطعي يثبت يتمه ﷺ ورعاية الله له.",
                        "क़ुरआन का प्रत्यक्ष प्रमाण जो नबी ﷺ के अनाथ बचपन और ईश्वरीय संरक्षण की पुष्टि करता है।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih Muslim",
                    referenceCode = "Sahih Muslim 2276 / Book of Virtues",
                    arabicQuote = "إِنَّ اللَّهَ اصْطَفَى كِنَانَةَ مِنْ وَلَدِ إِسْمَاعِيلَ وَاصْطَفَى قُرَيْشًا مِنْ كِنَانَةَ وَاصْطَفَى مِنْ قُرَيْشٍ بَنِي هَاشِمٍ",
                    quoteTranslation = localized4(
                        "Verily Allah chose Kinanah from the progeny of Ishmael, Quraysh from Kinanah, and Banu Hashim from Quraysh.",
                        "بے شک اللہ نے اولادِ اسماعیل میں سے کنانہ کو، کنانہ میں سے قریش کو اور قریش میں سے بنو ہاشم کو چُنا۔",
                        "إن الله اصطفى كنانة من ولد إسماعيل، واصطفى قريشاً من كنانة، واصطفى من قريش بني هاشم.",
                        "निःसंदेह अल्लाह ने इस्माईल की संतान में से किनाना को, किनाना में से कुरैश को और कुरैश में से बनू हाशिम को चुना।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih Muslim confirming his noble genealogy.",
                        "صحیح مسلم کی مستند روایت جو نسبِ مبارک کو ثابت کرتی ہے۔",
                        "حديث صحيح في صحيح مسلم يثبت شرف النسب.",
                        "सहीह मुस्लिम की प्रामाणिक हदीस जो पावन वंश की पुष्टि करती है।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_2_CLASSICAL_SEERAH,
                    sourceName = "Sirat Ibn Hisham (from Ibn Ishaq)",
                    referenceCode = "Vol. 1, Year of the Elephant & Fostering",
                    arabicQuote = null,
                    quoteTranslation = localized4(
                        "Ibn Ishaq records the Year of the Elephant, Aminah's journey to Madinah's maternal kin, and her passing at Abwa during the return journey.",
                        "ابن اسحاق و ابن ہشام نے عام الفیل، مدینہ منورہ کے سفر اور ابواء میں حضرت آمنہ کے وصال کی تفصیلات بیان کی ہیں۔",
                        "روى ابن إسحاق وابن هشام أحداث عام الفيل وزيارة أخواله بالمدينة ووفاة السيدة آمنة بالأبواء.",
                        "इब्न इस्हाक और इब्न हिशाम ने आमुल-फ़ील, मदीना यात्रा और अबवा में माता आमिना के देहांत का विवरण दर्ज किया है।"
                    ),
                    reliabilityNote = localized4(
                        "Classical historical report (Tier 2). Dates and narrative details are cited by Seerah historians rather than graded as Sahih Hadith.",
                        "کلاسیکی تاریخی روایت (درجہ دوم)؛ تاریخ کی کتب سے منقول ہے، اسے صحیح حدیث کا درجہ نہیں دیا جاتا۔",
                        "رواية سيرية كلاسيكية (المستوى ٢) تُنقل للاستئناس التاريخي ولا تُوسَم بدرجة الحديث الصحيح.",
                        "प्राचीन सीरत ग्रंथ (स्तर 2); इसे ऐतिहासिक विवरण के रूप में उद्धृत किया जाता है, सहीह हदीस के रूप में नहीं।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Early historians differed on the exact day of Rabi' al-Awwal (2nd, 8th, 9th, 10th, or 12th) and whether Abdullah passed away before birth or shortly after, while agreeing on the Year of the Elephant and Monday as the day of birth.",
                "مؤرخین میں ربیع الاول کی درست تاریخ (2، 8، 9، 10 یا 12) اور حضرت عبداللہ کے وصال کے وقت میں اختلاف ہے، تاہم عام الفیل اور پیر کے دن پر اتفاق ہے۔",
                "اختلف المؤرخون في تحديد اليوم من ربيع الأول وفي توقيت وفاة أبيه عبد الله قبل الولادة أو بعدها بقليل، واتفقوا على يوم الاثنين وعام الفيل.",
                "इतिहासकारों में रबीउल-अव्वल की तारीख और पिता के देहांत के सटीक समय पर मतभेद है, लेकिन सोमवार और आमुल-फ़ील पर सहमति है।"
            ),
            lessonsToday = localized4(
                "Hardship early in life, including losing parents, does not prevent someone from attaining dignity, integrity, and service to humanity through steadfast character.",
                "بچپن کی مشکلات اور یتیمی انسان کی عظمت یا خدمتِ خلق کی راہ میں رکاوٹ نہیں بنتیں، بلکہ صبر اور کردار کو نکھارتی ہیں۔",
                "إن الابتلاء واليتم في الصغر لا يعيقان بناء الشخصية السامية النافعة متى اجتمعت الرعاية والخلق القويم.",
                "जीवन की शुरुआती कठिनाइयाँ और अनाथपन उच्च चरित्र और मानव सेवा के मार्ग में बाधा नहीं बनते।"
            ),
            nextMilestoneNote = localized4(
                "Next: Youth in Makkah, Hilf al-Fudul, and marriage to Khadijah bint Khuwaylid رضي الله عنها.",
                "اگلا مرحلہ: مکہ میں جوانی، حلف الفضول اور حضرت خدیجہ رضی اللہ عنہا سے نکاح۔",
                "المرحلة التالية: الشباب في مكة وحلف الفضول والزواج من خديجة رضي الله عنها.",
                "अगला पड़ाव: मक्का में युवावस्था, हिल्फ़ुल फ़ुज़ूल और हज़रत ख़दीजा से विवाह।"
            ),
            referenceLinks = listOf(
                "Quran 93:6 (Quran.com)" to "https://quran.com/93/6",
                "Sahih Muslim 2276 (Sunnah.com)" to "https://sunnah.com/muslim:2276",
                "Sirat Ibn Hisham Archive" to "https://archive.org"
            ),
            keywords = listOf("birth", "makkah", "aminah", "abdullah", "halimah", "orphan", "hashim", "elephant", "93:6")
        ),
        SeerahArticle(
            id = "seerah_02_youth_hilf_khadijah",
            era = SeerahEra.BEFORE_PROPHETHOOD,
            orderIndex = 2,
            title = localized4(
                "Youth, Al-Sadiq al-Amin, Hilf al-Fudul & Marriage to Khadijah رضي الله عنها",
                "جوانی، الصادق الامین، حلف الفضول اور حضرت خدیجہؓ سے نکاح",
                "الشباب في مكة وحلف الفضول والزواج بخديجة رضي الله عنها",
                "युवावस्था, अमीन की उपाधि, हिल्फ़ुल फ़ुज़ूल और ख़दीजा से विवाह"
            ),
            approxChronology = localized4(
                "Youth through age 35 (~585 – 605 CE). Marriage to Khadijah رضي الله عنها occurred around age 25 (~595 CE); the Ka'bah arbitration around age 35 (~605 CE).",
                "جوانی تا عمر 35 سال (~585 تا 605 عیسوی)۔ نکاح تقریباً 25 برس کی عمر میں اور حجرِ اسود کی ثالثی تقریباً 35 برس کی عمر میں ہوئی۔",
                "من الشباب حتى سن الخامسة والثلاثين (نحو 585 – 605م)، والزواج من خديجة في نحو الخامسة والعشرين، وتحكيم الحجر الأسود في الخامسة والثلاثين.",
                "युवावस्था से 35 वर्ष की आयु तक (~585 – 605 ई.)। विवाह ~25 वर्ष में और काबा का हज्र-ए-असवद विवाद सुलझाना ~35 वर्ष में हुआ।"
            ),
            overview = localized4(
                "As a young man in Makkah, Muhammad ﷺ tended sheep and worked in trade with uprightness that earned him the titles Al-Sadiq (The Truthful) and Al-Amin (The Trustworthy). He witnessed Hilf al-Fudul, a pact in Makkah to defend every wronged person regardless of tribe, and later praised it in Islam. Impressed by his integrity during a trade caravan to Syria, Khadijah bint Khuwaylid رضي الله عنها proposed marriage, forming a deeply supportive 25-year partnership. At 35, when Makkah tribes disputed placing the Black Stone during the Ka'bah's rebuilding, he wisely resolved the crisis using a shared cloak.",
                "جوانی میں آپ ﷺ نے بکریاں چرائیں اور نہایت دیانتداری سے تجارت کی، جس پر اہلِ مکہ آپ کو صادق و امین کہتے تھے۔ آپ ﷺ نے مظلوموں کی مدد کے معاہدے حلف الفضول میں شرکت فرمائی۔ تجارت کی امانت داری دیکھ کر حضرت خدیجہؓ نے نکاح کا پیغام بھیجا۔ 35 سال کی عمر میں کعبہ کی تعمیرِ نو کے وقت حجرِ اسود رکھنے کے تنازعے کو چادر بچھا کر حکمت سے حل فرمایا۔",
                "عمل النبي ﷺ في رعي الغنم ثم في التجارة بأمانة بالغة فعُرف بالصادق الأمين. شهد حلف الفضول لنصرة المظلومين في مكة، ثم تزوج السيدة خديجة رضي الله عنها بعد أن شهدت أمانته. وفي الخامسة والثلاثين حلّ نزاع قبائل قريش في وضع الحجر الأسود بحكمة بالغة.",
                "युवावस्था में आपने बकरियाँ चराईं और व्यापार में ऐसी ईमानदारी दिखाई कि लोग आपको अस-सादिक़ अल-अमीन कहने लगे। मक्का में अन्याय के विरुद्ध हिल्फ़ुल फ़ुज़ूल संधि में भाग लिया। आपकी ईमानदारी देखकर हज़रत ख़दीजा ने विवाह का प्रस्ताव रखा। 35 वर्ष की आयु में काबा के पुनर्निर्माण पर हज्र-ए-असवद रखने के विवाद को चादर की युक्ति से शांतिपूर्वक सुलझाया।"
            ),
            contextPeoplePlaces = localized4(
                "People: Khadijah bint Khuwaylid رضي الله عنها, Maysarah, Abd Allah ibn Jud'an. Places: Makkah, Ka'bah, Busra (Syrian trade route).",
                "شخصیات: حضرت خدیجہؓ، میسرہ، عبداللہ بن جدعان۔ مقامات: مکہ مکرمہ، خانہ کعبہ، شام کا تجارتی راستہ۔",
                "الشخصيات: خديجة بنت خويلد، ميسرة، عبد الله بن جدعان. الأماكن: مكة، الكعبة المشرفة، طريق الشام التجاري.",
                "व्यक्ति: हज़रत ख़दीजा, मैसरा। स्थान: मक्का, काबा, शाम का व्यापार मार्ग।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari",
                    referenceCode = "Sahih al-Bukhari 2262",
                    arabicQuote = "مَا بَعَثَ اللَّهُ نَبِيًّا إِلَّا رَعَى الْغَنَمَ",
                    quoteTranslation = localized4(
                        "Allah did not send any prophet except that he shepherded sheep — and the Prophet ﷺ said he tended them for the people of Makkah for a few qirats.",
                        "اللہ نے کوئی نبی ایسا نہیں بھیجا جس نے بکریاں نہ چرائی ہوں، اور میں بھی اہلِ مکہ کی بکریاں چند قیراط پر چراتا تھا۔",
                        "ما بعث الله نبياً إلا رعى الغنم... نعم كنت أرعاها على قراريط لأهل مكة.",
                        "अल्लाह ने ऐसा कोई नबी नहीं भेजा जिसने बकरियाँ न चराई हों... मैं भी मक्का वालों की बकरियाँ कुछ क़ीरात के बदले चराता था।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari highlighting humble labor in youth.",
                        "صحیح بخاری کی مستند حدیث جو جوانی کی محنت و تواضع بیان کرتی ہے۔",
                        "حديث صحيح في البخاري يبرز شرف العمل والتواضع قبل البعثة.",
                        "सहीह बुख़ारी की प्रामाणिक हदीस जो युवावस्था की विनम्र मेहनत को दर्शाती है।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_2_CLASSICAL_SEERAH,
                    sourceName = "Musnad Ahmad / Sirat Ibn Hisham",
                    referenceCode = "Musnad Ahmad 1655; Ibn Hisham Vol. 1",
                    arabicQuote = "لَقَدْ شَهِدْتُ فِي دَارِ عَبْدِ اللَّهِ بْنِ جُدْعَانَ حِلْفًا مَا أُحِبُّ أَنَّ لِي بِهِ حُمْرَ النَّعَمِ",
                    quoteTranslation = localized4(
                        "I witnessed a pact in the house of Abdullah ibn Jud'an that I would not exchange for red camels; if I were invited to the like of it in Islam, I would respond.",
                        "میں نے عبداللہ بن جدعان کے گھر میں ایک ایسے عہد (حلف الفضول) میں شرکت کی جس کے بدلے سرخ اونٹ بھی نہ لوں، اور اگر اسلام میں بھی ویسے عہد کی دعوت دی جائے تو لبیک کہوں۔",
                        "لقد شهدت في دار عبد الله بن جدعان حلفاً ما أحب أن لي به حمر النعم، ولو أدعى به في الإسلام لأجبت.",
                        "मैंने अब्दुल्लाह बिन जुदआन के घर एक ऐसी संधि (हिल्फ़ुल फ़ुज़ूल) में भाग लिया जिसके बदले मैं लाल ऊँट भी न लूँ, और इस्लाम में भी ऐसे समझौते के लिए बुलाया जाऊँ तो अवश्य शामिल हूँ।"
                    ),
                    reliabilityNote = localized4(
                        "Reported across Musnad Ahmad and classical Seerah works regarding Hilf al-Fudul.",
                        "مسند احمد اور سیرتِ ابن ہشام سے مروی؛ سماجی انصاف میں نبوت سے قبل شرکت کا بیان۔",
                        "مروي في المسند وكتب السيرة حول حلف الفضول ونصرة المظلوم.",
                        "मुसनद अहमद और इब्न हिशाम में वर्णित न्याय-संधि का उल्लेख।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Biographers note slight variations regarding Khadijah's exact age at marriage (reported by some as 40 and by other classical chains as 28), while her stature as his beloved first supporter is unanimously established.",
                "نکاح کے وقت حضرت خدیجہؓ کی عمر کے بارے میں بعض روایات میں 40 سال اور بعض میں 28 سال منقول ہے، تاہم ان کی عظمت و وفا پر سب کا اتفاق ہے۔",
                "تختلف الروايات في سن السيدة خديجة عند الزواج بين أربعين أو دون ذلك، مع الإجماع على مكانتها الجليلة وأولويتها في الإسلام.",
                "विवाह के समय हज़रत ख़दीजा की आयु के विषय में कुछ ऐतिहासिक अंतर (40 या 28 वर्ष) पाए जाते हैं, पर उनके महान योगदान पर पूर्ण सहमति है।"
            ),
            lessonsToday = localized4(
                "Trustworthiness in daily work, honest trade, and standing against injustice — even in coalition with others for public good — are core Islamic values.",
                "کاروبار میں امانت داری اور مظلوم کی حمایت کے لیے اجتماعی معاہدوں میں شرکت اسلام کے بنیادی اخلاقی اصول ہیں۔",
                "الأمانة في المعاملات والانخراط في مواثيق نصرة المظلومين من صميم القيم الإسلامية.",
                "व्यापार में ईमानदारी और अन्याय के विरुद्ध जनहित के समझौतों में सहयोग इस्लाम के मूल सिद्धांत हैं।"
            ),
            nextMilestoneNote = localized4(
                "Next: Solitude in Cave Hira and the First Revelation (Surah Al-Alaq).",
                "اگلا مرحلہ: غارِ حرا کی تنہائی اور پہلی وحی کا نزول۔",
                "المرحلة التالية: الخلوة في غار حراء ونزول الوحي الأول.",
                "अगला पड़ाव: गार-ए-हिरा में एकांत और पहली वह्य का अवतरण।"
            ),
            referenceLinks = listOf(
                "Sahih al-Bukhari 2262 (Sunnah.com)" to "https://sunnah.com/bukhari:2262",
                "Ar-Raheeq Al-Makhtum Reference" to "https://archive.org"
            ),
            keywords = listOf("sadiq", "amin", "hilf", "fudul", "khadijah", "black stone", "ka'bah", "trade")
        ),
        SeerahArticle(
            id = "seerah_03_cave_hira_first_revelation",
            era = SeerahEra.BEGINNING_OF_REVELATION,
            orderIndex = 3,
            title = localized4(
                "Cave Hira & the First Revelation (Iqra)",
                "غارِ حرا اور پہلی وحی (اقرأ) کا نزول",
                "غار حراء وبدء نزول الوحي",
                "गार-ए-हिरा और पहली वह्य (इक़रा)"
            ),
            approxChronology = localized4(
                "Ramadan, ~610 CE (13 BH), when the Prophet ﷺ was approximately 40 years old.",
                "ماہِ رمضان، تقریباً 610 عیسوی (13 قبل ہجری)، جب آپ ﷺ کی عمرِ مبارک چالیس سال تھی۔",
                "شهر رمضان المبارك من نحو سنة 610م (13 قبل الهجرة) وعمره ﷺ أربعون عاماً.",
                "रमज़ान का महीना, लगभग 610 ई. (13 हिजरी पूर्व), जब नबी ﷺ की आयु लगभग 40 वर्ष थी।"
            ),
            overview = localized4(
                "Prior to prophethood, Muhammad ﷺ cherished seclusion in Cave Hira atop Jabal al-Nour, worshipping Allah according to the pure upright way of Ibrahim. During Ramadan, Angel Jibril came to him commanding 'Iqra!' (Read!) three times, revealing the opening verses of Surah Al-Alaq. Trembling, he returned home to Khadijah رضي الله عنها, who reassured him immediately: 'Never! Allah will never disgrace you, for you maintain ties of kinship, help the needy, honor guests, and support those afflicted by calamity.' She took him to Waraqah ibn Nawfal, who recognized the angel as the same Namus sent to Musa عليه السلام.",
                "بعثت سے قبل آپ ﷺ غارِ حرا میں خلوت نشینی اختیار فرماتے اور عبادتِ الٰہی کرتے۔ رمضان میں فرشتہ حضرت جبریلؑ تشریف لائے اور تین بار 'اقرأ' (پڑھو) فرمایا، یوں سورۃ العلق کی ابتدائی آیات نازل ہوئیں۔ لرزتے ہوئے گھر تشریف لائے تو حضرت خدیجہؓ نے تسلی دی کہ اللہ آپ کو ہرگز رسوا نہیں کرے گا کیونکہ آپ صلہ رحمی کرتے اور محتاجوں کا بوجھ اٹھاتے ہیں۔ ورقہ بن نوفل نے تصدیق کی کہ یہ وہی ناموس ہے جو حضرت موسیٰؑ پر نازل ہوتا تھا۔",
                "كان ﷺ يتعبد في غار حراء الليالي ذوات العدد، حتى جاءه الملك جبريل في رمضان بقوله: «اقرأ» وأنزل صدر سورة العلق. فرجع ترجف بوادره إلى خديجة رضي الله عنها فثبّتته بذكر محاسنه، ثم انطلقت به إلى ورقة بن نوفل الذي شهد بأنه الناموس الذي أنزل على موسى عليه السلام.",
                "नुबुव्वत से पहले आप ﷺ गार-ए-हिरा में एकांत में इबादत करते थे। रमज़ान में फ़रिश्ते जिब्रील (अलै.) आए और तीन बार 'इक़रा' (पढ़ो) कहा, जिससे सूरह अल-अलक़ की शुरुआती आयतें नाज़िल हुईं। काँपते हुए घर लौटने पर हज़रत ख़दीजा ने आपके उच्च चरित्र का स्मरण दिलाकर ढांढस बंधाया और वरक़ा बिन नौफ़ल के पास ले गईं जिन्होंने मूसा (अलै.) पर आने वाले वही दूत होने की पुष्टि की।"
            ),
            contextPeoplePlaces = localized4(
                "People: Angel Jibril عليه السلام, Khadijah رضي الله عنها, Waraqah ibn Nawfal. Places: Cave Hira (Jabal al-Nour), Makkah.",
                "شخصیات: حضرت جبریلؑ، حضرت خدیجہؓ، ورقہ بن نوفل۔ مقامات: غارِ حرا (جبلِ نور)، مکہ مکرمہ۔",
                "الشخصيات: جبريل عليه السلام، خديجة رضي الله عنها، ورقة بن نوفل. الأماكن: غار حراء بجبل النور، مكة.",
                "व्यक्ति: जिब्रील (अलै.), हज़रत ख़दीजा, वरक़ा बिन नौफ़ल। स्थान: गार-ए-हिरा (जबलुल-नूर), मक्का।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Alaq 96:1-5)",
                    referenceCode = "Quran 96:1-5",
                    arabicQuote = "اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ ۝ خَلَقَ الْإِنسَانَ مِنْ عَلَقٍ",
                    quoteTranslation = localized4(
                        "Read in the name of your Lord who created — created man from a clinging substance.",
                        "پڑھ اپنے رب کے نام سے جس نے پیدا کیا، جس نے انسان کو جمے ہوئے خون سے بنایا۔",
                        "اقْرَأْ بِاسْمِ رَبِّكَ الَّذِي خَلَقَ ۝ خَلَقَ الْإِنسَانَ مِنْ عَلَقٍ",
                        "पढ़ अपने रब के नाम से जिसने पैदा किया, जिसने इंसान को जमे हुए खून के लोथड़े से बनाया।"
                    ),
                    reliabilityNote = localized4(
                        "Unanimously transmitted opening revelation of the Noble Quran.",
                        "قرآنِ مجید کی پہلی وحی کی متفقہ آیاتِ مبارکہ۔",
                        "أول ما نزل من القرآن الكريم بإجماع الأمة.",
                        "क़ुरआन मजीद की सर्वसम्मत पहली वह्य।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Book of Revelation)",
                    referenceCode = "Sahih al-Bukhari 3",
                    arabicQuote = "كَلَّا وَاللَّهِ مَا يُخْزِيكَ اللَّهُ أَبَدًا إِنَّكَ لَتَصِلُ الرَّحِمَ",
                    quoteTranslation = localized4(
                        "Never! By Allah, Allah will never disgrace you. You uphold kinship ties, carry the burden of the weak, give to the destitute, honor guests, and assist in true calamities.",
                        "ہرگز نہیں! اللہ کی قسم، اللہ آپ کو کبھی رسوا نہیں کرے گا؛ آپ صلہ رحمی کرتے ہیں، بے سہاروں کا بوجھ اٹھاتے ہیں، مہمان نوازی کرتے اور حق کے کاموں میں مدد کرتے ہیں۔",
                        "كلا والله ما يخزيك الله أبداً، إنك لتصل الرحم وتصدق الحديث وتحمل الكل وتكسب المعدوم وتقري الضيف وتعين على نوائب الحق.",
                        "कदापि नहीं! अल्लाह की क़सम, अल्लाह आपको कभी अपमानित नहीं करेगा; आप रिश्ते निभाते हैं, कमज़ोरों का सहारा बनते हैं और मुसीबत में लोगों की मदद करते हैं।"
                    ),
                    reliabilityNote = localized4(
                        "Hadith narrated by A'ishah رضي الله عنها in Sahih al-Bukhari, Hadith #3.",
                        "صحیح بخاری (حدیث 3) میں حضرت عائشہؓ سے مروی نہایت مستند روایت۔",
                        "حديث صحيح رواه البخاري (رقم 3) عن أم المؤمنين عائشة رضي الله عنها.",
                        "सहीह बुख़ारी (हदीस 3) में हज़रत आइशा से वर्णित अत्यंत प्रामाणिक हदीस।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Scholars note slight differences regarding the duration of the brief pause in revelation (Fatrat al-Wahy) before Surah Al-Muddaththir, ranging from days to a few months.",
                "پہلی وحی کے بعد سورۃ المدثر تک فترتِ وحی (وحی کے وقفے) کی مدت چند دنوں سے لے کر چند ماہ تک مختلف روایات میں مذکور ہے۔",
                "تنوعت أقوال المؤرخين حول مدة فترة الوحي بين نزول العلق والمدثر بين أيام أو أشهر.",
                "पहली वह्य और सूरह अल-मुद्दस्सिर के बीच विराम की अवधि को लेकर कुछ दिन से कुछ महीनों तक का ऐतिहासिक अंतर मिलता है।"
            ),
            lessonsToday = localized4(
                "Sacred knowledge begins with humility and awe, and strong, compassionate family support is a mercy during moments of immense spiritual responsibility.",
                "علم و ہدایت کی ابتدا عاجزی اور خشیتِ الٰہی سے ہوتی ہے، اور کٹھن لمحات میں شریکِ حیات کی ہمدردی بڑی نعمت ہے۔",
                "إن طلب العلم يبدأ بالخشية والتواضع، ومؤازرة الأسرة الصالحة من أعظم النعم عند تحمل المسؤوليات الكبرى.",
                "ज्ञान की शुरुआत विनम्रता से होती है, और कठिन घड़ी में परिवार का संबल अत्यंत मूल्यवान होता है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Quiet invitations in Makkah, the House of Al-Arqam, and early persecution.",
                "اگلا مرحلہ: خفیہ دعوت، دارِ ارقم اور ابتدائی مصائب۔",
                "المرحلة التالية: الدعوة السرية ودار الأرقم وبداية الأذى.",
                "अगला पड़ाव: मक्का में गुप्त दावत, दार-ए-अरक़म और प्रारंभिक कठिनाइयाँ।"
            ),
            referenceLinks = listOf(
                "Quran 96:1-5 (Quran.com)" to "https://quran.com/96/1-5",
                "Sahih al-Bukhari 3 (Sunnah.com)" to "https://sunnah.com/bukhari:3"
            ),
            keywords = listOf("hira", "revelation", "iqra", "96:1", "alaq", "jibril", "waraqah", "khadijah", "bukhari 3")
        ),
        SeerahArticle(
            id = "seerah_04_early_mission_makkah",
            era = SeerahEra.EARLY_MAKKAH,
            orderIndex = 4,
            title = localized4(
                "Quiet Invitation, Dar al-Arqam & Open Call at Safa",
                "خفیہ دعوت، دارِ ارقم اور کوہِ صفا پر اعلانیہ دعوت",
                "الدعوة السرية ودار الأرقم والجهر بالدعوة على الصفا",
                "गुप्त दावत, दार-ए-अरक़म और सफ़ा पहाड़ी पर खुली घोषणा"
            ),
            approxChronology = localized4(
                "Years 1 – 4 of Prophethood (~610 – 614 CE).",
                "نبوت کے پہلے تا چوتھے سال (~610 تا 614 عیسوی)۔",
                "السنة الأولى إلى الرابعة من البعثة (نحو 610 – 614م).",
                "नुबुव्वत के वर्ष 1 – 4 (~610 – 614 ई.)।"
            ),
            overview = localized4(
                "For the first three years, invitations focused privately on sincere individuals known for character: Khadijah, Abu Bakr al-Siddiq, Ali ibn Abi Talib, Zayd ibn Harithah, and early companions gathered quietly at Dar al-Arqam ibn Abi al-Arqam near Mount Safa for Quranic recitation and moral cultivation. Upon the command of Surah Ash-Shu'ara (26:214) and Al-Hijr (15:94), the Prophet ﷺ climbed Mount Safa and openly called Quraysh to Tawhid and accountability.",
                "ابتدائی تین برس دعوت خاموشی سے قریبی اور مخلص افراد تک محدود رہی۔ حضرت خدیجہؓ، حضرت ابوبکر صدیقؓ، حضرت علیؓ اور حضرت زید بن حارثہؓ اولین ایمان لانے والوں میں تھے۔ دارِ ارقم میں قرآن و تربیت کی محفلیں منعقد ہوتیں۔ پھر سورۃ الشعراء اور الحجر کے حکم پر آپ ﷺ نے کوہِ صفا پر چڑھ کر قریش کو علانیہ توحید کی دعوت دی۔",
                "استمرت الدعوة سراً نحو ثلاث سنوات في دار الأرقم بن أبي الأرقم عند الصفا؛ فآمن في طليعة السابقين خديجة وأبو بكر وعلي وزيد بن حارثة رضي الله عنهم. ثم نزل الأمر بالصدع بالتوحيد فدعا النبي ﷺ قريشاً على جبل الصفا.",
                "शुरुआती तीन वर्षों तक दावत निष्ठावान लोगों तक निजी रूप से रही और सफ़ा के पास दार-ए-अरक़म में क़ुरआन व प्रशिक्षण होता रहा। ख़दीजा, अबू बक्र, अली और ज़ैद बिन हारिसा शुरुआती ईमान लाने वालों में थे। फिर अल्लाह के हुक्म पर सफ़ा पहाड़ी से कुरैश को खुला निमंत्रण दिया गया।"
            ),
            contextPeoplePlaces = localized4(
                "People: Abu Bakr, Ali ibn Abi Talib, Zayd ibn Harithah, Al-Arqam ibn Abi al-Arqam, Bilal ibn Rabah, Sumayyah, Yasir, Ammar. Places: Mount Safa, Dar al-Arqam, Makkah.",
                "شخصیات: حضرت ابوبکرؓ، حضرت علیؓ، حضرت زیدؓ، حضرت ارقمؓ، حضرت بلالؓ، حضرت سمیہؓ و یاسرؓ و عمارؓ۔ مقامات: کوہِ صفا، دارِ ارقم۔",
                "الشخصيات: أبو بكر وعلي وزيد والأرقم وبلال وآل ياسر رضي الله عنهم. الأماكن: جبل الصفا ودار الأرقم بمكة.",
                "व्यक्ति: अबू बक्र, अली, ज़ैद, अरक़म, बिलाल, सुमैया व यासिर परिवार। स्थान: सफ़ा पहाड़ी, दार-ए-अरक़म।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Hijr 15:94)",
                    referenceCode = "Quran 15:94",
                    arabicQuote = "فَاصْدَعْ بِمَا تُؤْمَرُ وَأَعْرِضْ عَنِ الْمُشْرِكِينَ",
                    quoteTranslation = localized4(
                        "Then declare what you are commanded and turn away from the polytheists.",
                        "پس جس بات کا آپ کو حکم دیا جا رہا ہے اسے کھل کر بیان کر دیجیے اور مشرکین سے اعراض کیجیے۔",
                        "فَاصْدَعْ بِمَا تُؤْمَرُ وَأَعْرِضْ عَنِ الْمُشْرِكِينَ",
                        "तो जिस बात का आपको आदेश दिया जा रहा है, उसे खुलकर सुना दीजिए और बहुदेववादियों से मुँह फेर लीजिए।"
                    ),
                    reliabilityNote = localized4(
                        "Direct Quranic injunction inaugurating the public phase of the mission.",
                        "اعلانیہ دعوت کا واضح قرآنی حکم۔",
                        "آية محكمة أمرت بالجهر بالدعوة.",
                        "खुली दावत का प्रत्यक्ष क़ुरआनी आदेश।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari",
                    referenceCode = "Sahih al-Bukhari 4770",
                    arabicQuote = "أَرَأَيْتَكُمْ لَوْ أَخْبَرْتُكُمْ أَنَّ خَيْلًا بِالْوَادِي تُرِيدُ أَنْ تُغِيرَ عَلَيْكُمْ أَكُنْتُمْ مُصَدِّقِيَّ",
                    quoteTranslation = localized4(
                        "If I told you that horsemen in the valley intended to attack you, would you believe me? They said: Yes, we have never experienced a lie from you.",
                        "اگر میں تمہیں بتاؤں کہ وادی کے پیچھے ایک لشکر تم پر حملہ کرنے والا ہے تو کیا تم میری تصدیق کرو گے؟ انہوں نے کہا: ہاں، ہم نے آپ سے کبھی جھوٹ نہیں سنا۔",
                        "أرأيتكم لو أخبرتكم أن خيلاً بالوادي تريد أن تغير عليكم أكنتم مصدقي؟ قالوا: نعم، ما جربنا عليك إلا صدقاً.",
                        "यदि मैं तुमसे कहूँ कि घाटी के पीछे एक सेना तुम पर हमला करने वाली है तो क्या तुम विश्वास करोगे? उन्होंने कहा: हाँ, हमने आपसे कभी झूठ नहीं सुना।"
                    ),
                    reliabilityNote = localized4(
                        "Authentic narration in Sahih al-Bukhari recording the Safa assembly.",
                        "صحیح بخاری میں کوہِ صفا کے خطبے کا مستند واقعہ۔",
                        "حديث صحيح في البخاري يوثق موقف جبل الصفا.",
                        "सहीह बुख़ारी में सफ़ा पहाड़ी की घटना का प्रामाणिक उल्लेख।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Biographers discuss which of Abu Bakr, Khadijah, Ali, and Zayd accepted first within their respective social circles (adult man, woman, youth, freedman); scholars reconcile this by noting Khadijah was first overall, while each led in their respective category.",
                "سب سے پہلے اسلام لانے میں حضرت خدیجہؓ اول ہیں؛ مردوں میں حضرت ابوبکرؓ، بچوں میں حضرت علیؓ اور آزاد کردہ غلاموں میں حضرت زیدؓ کی سبقت کو علما نے باہم تطبیق دی ہے۔",
                "ذكر المؤرخون أولية خديجة ثم أبا بكر وعلياً وزيداً كلٌّ في فئته، ولا تعارض في سبقهم رضي الله عنهم.",
                "प्रथम ईमान लाने वालों में हज़रत ख़दीजा, पुरुषों में अबू बक्र, किशोरों में अली और आज़ाद गुलामों में ज़ैद के क्रम पर विद्वानों में सुंदर समन्वय है।"
            ),
            lessonsToday = localized4(
                "Sound spiritual cultivation precedes public activism, and personal credibility built over years gives weight to one's message.",
                "عوامی دعوت سے قبل فکری و اخلاقی تربیت ضروری ہے، اور برسوں کی سچائی ہی دعوت میں وزن پیدا کرتی ہے۔",
                "التربية الإيمانية المتينة تسبق العمل العام، والصدق الشخصي هو أساس ثقة الناس بالداعية.",
                "सार्वजनिक कार्य से पहले नैतिक प्रशिक्षण आवश्यक है, और वर्षों की सत्यनिष्ठा ही बात को प्रभावी बनाती है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Persecution, Migration to Abyssinia, the Boycott, and the Night Journey.",
                "اگلا مرحلہ: مظالم، ہجرتِ حبشہ، شعبِ ابی طالب اور معراج۔",
                "المرحلة التالية: الاضطهاد والهجرة إلى الحبشة والمقاطعة والإسراء والمعراج.",
                "अगला पड़ाव: अत्याचार, हबशा की हिजरत, बहिष्कार और मेराज।"
            ),
            referenceLinks = listOf(
                "Quran 15:94 (Quran.com)" to "https://quran.com/15/94",
                "Sahih al-Bukhari 4770 (Sunnah.com)" to "https://sunnah.com/bukhari:4770"
            ),
            keywords = listOf("arqam", "safa", "15:94", "abu bakr", "ali", "zayd", "makkah", "public")
        ),
        SeerahArticle(
            id = "seerah_05_abyssinia_boycott_taif",
            era = SeerahEra.IMPORTANT_MAKKAH_EVENTS,
            orderIndex = 5,
            title = localized4(
                "Migration to Abyssinia, Shi'b Abi Talib Boycott & Year of Sorrow",
                "ہجرتِ حبشہ، شعبِ ابی طالب کا بائیکاٹ، عام الحزن اور سفرِ طائف",
                "الهجرة إلى الحبشة ومقاطعة الشعب وعام الحزن والرحلة إلى الطائف",
                "हबशा हिजरत, शिअ्ब अबी तालिब बहिष्कार, शोक वर्ष और ताइफ़"
            ),
            approxChronology = localized4(
                "Years 5 – 10 of Prophethood (~615 – 619 CE).",
                "نبوت کے پانچویں تا دسویں سال (~615 تا 619 عیسوی)۔",
                "من السنة الخامسة إلى العاشرة للبعثة (نحو 615 – 619م).",
                "नुबुव्वत के वर्ष 5 – 10 (~615 – 619 ई.)।"
            ),
            overview = localized4(
                "As persecution intensified against vulnerable Muslims, the Prophet ﷺ permitted two waves of migration to Abyssinia under the just Christian king Al-Najashi, who protected the believers after hearing Surah Maryam recited by Ja'far ibn Abi Talib رضي الله عنه. Quraysh later enforced a three-year social and economic boycott against Banu Hashim in Shi'b Abi Talib. Shortly after the boycott ended, Abu Talib and Khadijah رضي الله عنها both passed away in the Year of Sorrow (~619 CE). Seeking another sanctuary, the Prophet ﷺ walked to Ta'if, endured stoning and rejection, yet prayed for the guidance of their descendants rather than their destruction.",
                "جب کمزور مسلمانوں پر ظلم حد سے بڑھا تو آپ ﷺ نے حبشہ کی طرف ہجرت کی اجازت دی جہاں عادل حکمران نجاشی نے حضرت جعفر طیارؓ کی تقریر اور تلاوتِ سورہ مریم سن کر مسلمانوں کو پناہ دی۔ قریش نے بنو ہاشم کا تین سالہ بائیکاٹ کیا۔ بائیکاٹ ختم ہونے کے فوراً بعد ابو طالب اور حضرت خدیجہؓ کا انتقال ہو گیا (عام الحزن)۔ اس کے بعد آپ ﷺ طائف تشریف لے گئے جہاں پتھراؤ سہنے کے باوجود اہلِ طائف کے لیے بددعا کے بجائے ہدایت کی دعا فرمائی۔",
                "عندما اشتد الأذى أذن ﷺ بالهجرة إلى الحبشة عند الملك العادل النجاشي الذي أكرمهم بعد سماع سورة مريم من جعفر بن أبي طالب. ثم فرضت قريش حصاراً على بني هاشم في شِعب أبي طالب قرابة ثلاث سنوات، وأعقب ذلك وفاة أبي طالب والسيدة خديجة في عام الحزن. ثم توجه ﷺ إلى الطائف فرُدّ وآذوه فاختار الدعاء لهدايتهم.",
                "अत्याचार बढ़ने पर नबी ﷺ ने हबशा के न्यायप्रिय राजा नजाशी के राज्य में हिजरत की अनुमति दी, जिन्होंने जाफ़र बिन अबी तालिब से सूरह मरयम सुनकर शरण दी। इसके बाद कुरैश ने तीन साल का आर्थिक-सामाजिक बहिष्कार किया। इसके समाप्त होते ही अबू तालिब और हज़रत ख़दीजा का देहांत (शोक वर्ष) हो गया। इसके बाद आप ताइफ़ गए जहाँ पथराव सहकर भी उनके वंशजों के लिए मार्गदर्शन की दुआ की।"
            ),
            contextPeoplePlaces = localized4(
                "People: Ja'far ibn Abi Talib, Al-Najashi, Umm Salamah, Abu Talib, Khadijah, Zayd ibn Harithah. Places: Aksum (Abyssinia), Shi'b Abi Talib, Ta'if.",
                "شخصیات: حضرت جعفرؓ، نجاشی، حضرت ام سلمہؓ، ابو طالب، حضرت خدیجہؓ، حضرت زیدؓ۔ مقامات: حبشہ، شعبِ ابی طالب، طائف۔",
                "الشخصيات: جعفر بن أبي طالب، النجاشي، أم سلمة، أبو طالب، خديجة، زيد. الأماكن: الحبشة، شِعب أبي طالب، الطائف.",
                "व्यक्ति: जाफ़र बिन अबी तालिब, नजाशी, उम्मे सलमा, अबू तालिब, ख़दीजा। स्थान: हबशा, शिअ्ब अबी तालिब, ताइफ़।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari (Ta'if Narration)",
                    referenceCode = "Sahih al-Bukhari 3231",
                    arabicQuote = "بَلْ أَرْجُو أَنْ يُخْرِجَ اللَّهُ مِنْ أَصْلَابِهِمْ مَنْ يَعْبُدُ اللَّهَ وَحْدَهُ لَا يُشْرِكُ بِهِ شَيْئًا",
                    quoteTranslation = localized4(
                        "Rather, I hope Allah will bring forth from their loins people who will worship Allah alone without associating anything with Him.",
                        "بلکہ میں امید رکھتا ہوں کہ اللہ ان کی نسلوں سے ایسے لوگ پیدا فرمائے گا جو صرف ایک اللہ کی عبادت کریں گے۔",
                        "بل أرجو أن يخرج الله من أصلابهم من يعبد الله وحده لا يشرك به شيئاً.",
                        "बल्कि मुझे आशा है कि अल्लाह उनकी संतानों में ऐसे लोग पैदा करेगा जो केवल एक अल्लाह की इबादत करेंगे।"
                    ),
                    reliabilityNote = localized4(
                        "Sahih al-Bukhari 3231 recording the Prophet's ﷺ mercy after the ordeal at Ta'if.",
                        "صحیح بخاری (3231) میں سفرِ طائف کے بعد رحمتِ نبوی کی مستند روایت۔",
                        "حديث صحيح في البخاري (3231) يوثق رحمته ﷺ يوم الطائف.",
                        "सहीह बुख़ारी (3231) में ताइफ़ की घटना पर नबी ﷺ की करुणा का प्रामाणिक वर्णन।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_2_CLASSICAL_SEERAH,
                    sourceName = "Sirat Ibn Hisham / Musnad Ahmad",
                    referenceCode = "Ja'far before Al-Najashi",
                    arabicQuote = null,
                    quoteTranslation = localized4(
                        "Classical chronicles record Ja'far ibn Abi Talib's address before Al-Najashi describing pre-Islamic ignorance, the Prophet's moral reform, and recitation of Surah Maryam.",
                        "سیرت و مسند میں نجاشی کے دربار میں حضرت جعفرؓ کا خطاب اور تلاوتِ سورۂ مریم تفصیل سے منقول ہے۔",
                        "روت كتب السيرة خطبة جعفر أمام النجاشي وبيانه لما جاء به الإسلام وتلاوة سورة مريم.",
                        "सीरत ग्रंथों में नजाशी के दरबार में जाफ़र के भाषण और सूरह मरयम के पाठ का विस्तृत विवरण मिलता है।"
                    ),
                    reliabilityNote = localized4(
                        "Classical Seerah report (Tier 2). Certain additional phrases of the Ta'if supplication not in Bukhari are also transmitted through Seerah chains.",
                        "تاریخی روایت (درجہ دوم)؛ طائف کی مشہور دعا بھی کتبِ سیرت میں نقل کی جاتی ہے، صحیحین سے نہیں۔",
                        "أثر تاريخي وسيري من المستوى الثاني؛ وتُروى أدعية الطائف الإضافية في كتب السيرة لا في الصحيحين.",
                        "प्राचीन सीरत ग्रंथ (स्तर 2) से ऐतिहासिक विवरण।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Historians discuss whether the boycott lasted roughly two to three years and whether Al-Najashi's prayer in absentia upon his death happened in 9 AH; classical chronologies agree on the sequence leading to the Year of Sorrow.",
                "مقاطعہ کی مدت دو سے تین سال بتائی جاتی ہے؛ تاہم شعبِ ابی طالب، عام الحزن اور سفرِ طائف کی ترتیب پر مؤرخین کا اتفاق ہے۔",
                "تتراوح التقديرات لمدة الحصار بين سنتين وثلاث سنوات، ويتفق المؤرخون على تسلسل الأحداث حتى عام الحزن والطائف.",
                "बहिष्कार की अवधि 2 से 3 वर्ष मानी जाती है, किंतु घटनाओं के क्रम पर सभी विद्वान सहमत हैं।"
            ),
            lessonsToday = localized4(
                "When doors close in one place, Muslims seek lawful sanctuaries, cooperate fairly with just non-Muslim neighbors, and never trade compassion for vengeance.",
                "آزمائش میں باہمت رہنا، منصف غیر مسلم معاشروں کے ساتھ حسنِ سلوک اور مخالفت پر انتقام کے بجائے خیرخواہی سنتِ نبوی ہے۔",
                "الثبات عند الأزمات وطلب الجوار الآمن عند المنصفين ومقابلة الأذى بالحلم والرحمة من هدي النبوة.",
                "कठिनाई में धैर्य रखना, न्यायप्रिय समाज में सुरक्षित शरण खोजना और अत्याचार के बदले भी करुणा व भलाई चुनना सिखाता है।"
            ),
            nextMilestoneNote = localized4(
                "Next: Al-Isra wal-Mi'raj & Pledges of Aqabah.",
                "اگلا مرحلہ: اسراء و معراج اور بیعتِ عقبہ۔",
                "المرحلة التالية: الإسراء والمعراج وبيعتا العقبة.",
                "अगला पड़ाव: इसरा व मेराज और अक़बा की प्रतिज्ञाएँ।"
            ),
            referenceLinks = listOf(
                "Sahih al-Bukhari 3231 (Sunnah.com)" to "https://sunnah.com/bukhari:3231",
                "Sirat Ibn Hisham on Archive.org" to "https://archive.org"
            ),
            keywords = listOf("abyssinia", "najashi", "jafar", "boycott", "taif", "khadijah", "sorrow", "bukhari 3231")
        ),
        SeerahArticle(
            id = "seerah_06_isra_miraj_aqabah",
            era = SeerahEra.IMPORTANT_MAKKAH_EVENTS,
            orderIndex = 6,
            title = localized4(
                "Al-Isra wal-Mi'raj & the Pledges of Aqabah",
                "اسراء و معراج اور بیعتِ عقبہ اولیٰ و ثانیہ",
                "الإسراء والمعراج وبيعتا العقبة",
                "इसरा व मेराज और अक़बा की प्रतिज्ञाएँ"
            ),
            approxChronology = localized4(
                "Before Hijrah (~620 – 622 CE; Pledges of Aqabah during pilgrimage seasons of 11–13th years of Prophethood).",
                "ہجرت سے قبل (~620 تا 622 عیسوی؛ بیعتِ عقبہ نبوت کے گیارہویں تا تیرہویں سال حج کے موقع پر ہوئیں)۔",
                "قُبيل الهجرة (نحو 620 – 622م؛ وبيعتا العقبة في مواسم الحج قبيل الهجرة).",
                "हिजरत से पूर्व (~620 – 622 ई.; अक़बा की प्रतिज्ञाएँ हज के मौसम में हुईं)।"
            ),
            overview = localized4(
                "After deep trials, Allah honored the Prophet ﷺ with Al-Isra (the Night Journey from Al-Masjid al-Haram in Makkah to Al-Masjid al-Aqsa in Jerusalem) and Al-Mi'raj (ascension through the heavens), during which the five daily prayers were gifted directly as a spiritual anchor. During pilgrimage seasons, delegations from Yathrib (Madinah) met the Prophet ﷺ at Aqabah in Mina, embraced Islam, and pledged faithful protection alongside Mus'ab ibn Umayr's transformative teaching mission in Yathrib.",
                "کٹھن آزمائشوں کے بعد اللہ تعالیٰ نے آپ ﷺ کو اسراء (مسجدِ حرام سے مسجدِ اقصیٰ تک رات کا سفر) اور معراجِ سماوی عطا فرمائی، جہاں امت کو پنجگانہ نماز کا عظیم تحفہ ملا۔ حج کے موسموں میں یثرب (مدینہ) کے وفود نے منیٰ میں عقبہ کے مقام پر اسلام قبول کیا اور حضرت مصعب بن عمیرؓ کی تعلیمات کے بعد بیعتِ عقبہ ثانیہ میں تحفظ و نصرت کا عہد کیا۔",
                "بعد الشدائد أكرم الله نبيه ﷺ برحلة الإسراء من المسجد الحرام إلى المسجد الأقصى والمعراج إلى السماوات العلا، وفيها فُرضت الصلوات الخمس. وفي مواسم الحج التقى وفود يثرب بالعقبة فبايعوه على الإسلام، وبُعث مصعب بن عمير معلماً، ثم تمت بيعة العقبة الثانية.",
                "कठिन परीक्षाओं के बाद अल्लाह ने नबी ﷺ को मस्जिदुल-हराम से मस्जिदुल-अक़्सा (इसरा) और आकाशों की यात्रा (मेराज) कराई, जहाँ पाँच वक़्त की नमाज़ का उपहार मिला। हज के अवसरों पर मदीना के प्रतिनिधियों ने अक़बा में इस्लाम स्वीकार किया और मुसअब बिन उमैर के शिक्षण के बाद रक्षा की प्रतिज्ञा की।"
            ),
            contextPeoplePlaces = localized4(
                "People: Prophets in the heavens, Abu Bakr al-Siddiq, Mus'ab ibn Umayr, Sa'd ibn Mu'adh, As'ad ibn Zurarah. Places: Makkah, Al-Masjid al-Aqsa (Jerusalem), Mina (Aqabah), Yathrib.",
                "شخصیات: انبیاء علیہم السلام، حضرت ابوبکرؓ، حضرت مصعب بن عمیرؓ، حضرت سعد بن معاذؓ۔ مقامات: مکہ، مسجد اقصیٰ، عقبہ (منیٰ)، یثرب۔",
                "الشخصيات: الأنبياء الكرام، أبو بكر الصديق، مصعب بن عمير، سعد بن معاذ. الأماكن: مكة، المسجد الأقصى، العقبة بمنى، يثرب.",
                "व्यक्ति: अबू बक्र, मुसअब बिन उमैर, साद बिन मुआज़। स्थान: मक्का, मस्जिदुल-अक़्सा, अक़बा (मिना), मदीना।"
            ),
            citations = listOf(
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Quran (Surah Al-Isra 17:1)",
                    referenceCode = "Quran 17:1",
                    arabicQuote = "سُبْحَانَ الَّذِي أَسْرَىٰ بِعَبْدِهِ لَيْلًا مِّنَ الْمَسْجِدِ الْحَرَامِ إِلَى الْمَسْجِدِ الْأَقْصَى",
                    quoteTranslation = localized4(
                        "Exalted is He who took His Servant by night from Al-Masjid al-Haram to Al-Masjid al-Aqsa, whose surroundings We have blessed.",
                        "پاک ہے وہ ذات جس نے اپنے بندے کو راتوں رات مسجدِ حرام سے مسجدِ اقصیٰ تک سیر کرائی جس کے گرد ہم نے برکت رکھی۔",
                        "سُبْحَانَ الَّذِي أَسْرَىٰ بِعَبْدِهِ لَيْلًا مِّنَ الْمَسْجِدِ الْحَرَامِ إِلَى الْمَسْجِدِ الْأَقْصَى",
                        "पवित्र है वह जिसने अपने बंदे को रात में मस्जिदुल-हराम से मस्जिदुल-अक़्सा तक यात्रा कराई जिसके चारों ओर हमने बरकत रखी है।"
                    ),
                    reliabilityNote = localized4(
                        "Explicit Quranic verse establishing the Night Journey.",
                        "واقعہ اسراء کی قطعی قرآنی نص۔",
                        "نص قرآني صريح في إثبات رحلة الإسراء.",
                        "इसरा की यात्रा का स्पष्ट क़ुरआनी प्रमाण।"
                    )
                ),
                SeerahCitation(
                    tier = SourceCredibilityTier.TIER_1_QURAN_HADITH,
                    sourceName = "Sahih al-Bukhari & Sahih Muslim",
                    referenceCode = "Sahih al-Bukhari 349; Sahih Muslim 162",
                    arabicQuote = "هِيَ خَمْسٌ وَهِيَ خَمْسُونَ لَا يُبَدَّلُ الْقَوْلُ لَدَيَّ",
                    quoteTranslation = localized4(
                        "They are five [daily prayers], yet equal in reward to fifty; My Word does not change.",
                        "یہ تعداد میں پانچ ہیں اور ثواب میں پچاس کے برابر ہیں، میرے ہاں بات بدلی نہیں جاتی۔",
                        "هي خمس وهي خمسون لا يُبدّل القول لدي.",
                        "ये पाँच नमाज़ें हैं और सवाब में पचास के बराबर हैं; मेरे यहाँ बात बदली नहीं जाती।"
                    ),
                    reliabilityNote = localized4(
                        "Muttafaqun Alayh (Bukhari & Muslim) on the gifting of Salah.",
                        "بخاری و مسلم کی متفق علیہ حدیث، فرضیتِ نماز کے باب میں۔",
                        "حديث متفق عليه في البخاري ومسلم بشأن فرض الصلوات الخمس.",
                        "बुख़ारी व मुस्लिम की सर्वसम्मत हदीस जो पाँच नमाज़ों के उपहार का प्रमाण है।"
                    )
                )
            ),
            historicalDifferences = localized4(
                "Historians differ on the exact month and year of Al-Isra wal-Mi'raj (some place it before the Ta'if journey, others in Rajab or Rabi' al-Awwal in the 11th or 12th year before Hijrah).",
                "مؤرخین میں اسراء و معراج کے مہینے اور سال (رجب، ربیع الاول یا رمضان) کے تعین میں اختلاف ہے؛ بعض روایات اسے نبوت کے گیارہویں اور بعض بارہویں سال میں بیان کرتی ہیں۔",
                "اختلف المؤرخون في تحديد الشهر والسنة الدقيقة للإسراء والمعراج، دون خلاف في أصل الحدث أو فرضية الصلوات الخمس فيه.",
                "इतिहासकारों में इसरा व मेराज के सटीक महीने और वर्ष को लेकर भिन्न मत हैं, यद्यपि घटना और पाँच नमाज़ों के उपहार पर पूर्ण सहमति है।"
            ),
            lessonsToday = localized4(
                "Daily Salah is the believer's direct spiritual ascension out of hardship, and building peaceful community readiness prepares the ground for lasting growth.",
                "پنجگانہ نماز مومن کی روحانی معراج ہے، اور تعلیم و پختہ عہد کے ذریعے ہی مثبت معاشرتی تبدیلی آتی ہے۔",
                "الصلاة اليومية معراج روحي للمؤمن، والتمهيد بالعلم والتنظيم يصنع مجتمعاً راسخاً.",
                "पाँच समय की नमाज़ आस्थावान का आध्यात्मिक संबल है, और शिक्षा तथा सुदृढ़ तैयारी से ही स्थायी सकारात्मक परिवर्तन आता है।"
            ),
            nextMilestoneNote = localized4(
                "Next: The Historic Hijrah from Makkah to Madinah (622 CE / 1 AH).",
                "اگلا مرحلہ: مکہ سے مدینہ کی تاریخی ہجرت (1 ہجری)۔",
                "المرحلة التالية: الهجرة النبوية المباركة من مكة إلى المدينة.",
                "अगला पड़ाव: मक्का से मदीना की ऐतिहासिक हिजरत (1 हिजरी)।"
            ),
            referenceLinks = listOf(
                "Quran 17:1 (Quran.com)" to "https://quran.com/17/1",
                "Sahih al-Bukhari 349 (Sunnah.com)" to "https://sunnah.com/bukhari:349"
            ),
            keywords = listOf("isra", "miraj", "aqabah", "17:1", "prayer", "musab", "yathrib", "bukhari 349")
        )
    )
}
