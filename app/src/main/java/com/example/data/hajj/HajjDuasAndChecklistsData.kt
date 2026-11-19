package com.example.data.hajj

import com.example.data.model.AppLanguage

internal object HajjDuasAndChecklistsData {

    val duas: List<HajjDuaEntry> = listOf(
        HajjDuaEntry(
            id = "hajj_dua_talbiyah",
            linkedDuaDataId = "dua_hajj_talbiyah",
            occasion = LocalizedText("1. Talbiyah (Entering Ihram & Throughout Journey)", "1۔ تلبیہ (احرام باندھتے وقت اور سفرِ حج میں)", "1. التلبية عند الإحرام وخلال المناسك", "1. तलबिया (इहराम के समय व यात्रा में)"),
            arabic = "لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لَا شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لَا شَرِيكَ لَكَ",
            transliteration = "Labbayk Allahumma labbayk, labbayka la sharika laka labbayk, innal-hamda wan-ni'mata laka wal-mulk, la sharika lak.",
            translation = LocalizedText(
                en = "Here I am at Your service, O Allah, here I am. You have no partner, here I am. Truly all praise, grace, and dominion belong to You; You have no partner.",
                ur = "حاضر ہوں اے اللہ میں حاضر ہوں، تیرا کوئی شریک نہیں، میں حاضر ہوں۔ بے شک تمام تعریفیں، نعمتیں اور بادشاہی تیری ہی ہیں، تیرا کوئی شریک نہیں۔",
                ar = "لبيك اللهم لبيك، إقراراً بالتوحيد والثناء على الله بالحمد والنعمة والملك.",
                hi = "हाज़िर हूँ ऐ अल्लाह, मैं हाज़िर हूँ! तेरा कोई साझी नहीं। निश्चय ही समस्त प्रशंसा, नेमतें और प्रभुत्व तेरा ही है।"
            ),
            reference = "Sahih al-Bukhari 1549 / Sahih Muslim 1184",
            sourceUrl = "https://sunnah.com/bukhari:1549"
        ),
        HajjDuaEntry(
            id = "hajj_dua_entering_masjid",
            occasion = LocalizedText("2. Entering Masjid al-Haram", "2۔ مسجدِ حرام میں داخل ہونے کی دعا", "2. دعاء دخول المسجد الحرام", "2. मस्जिद अल-हरम में प्रवेश की दुआ"),
            arabic = "بِسْمِ اللَّهِ، وَالصَّلَاةُ وَالسَّلَامُ عَلَى رَسُولِ اللَّهِ، اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ",
            transliteration = "Bismillahi was-salatu was-salamu 'ala Rasulillah, Allahumma-ftah li abwaba rahmatik.",
            translation = LocalizedText(
                en = "In the name of Allah, and peace and blessings upon the Messenger of Allah. O Allah, open for me the gates of Your mercy.",
                ur = "اللہ کے نام سے، اور رسول اللہ ﷺ پر درود و سلام۔ اے اللہ! میرے لیے اپنی رحمت کے دروازے کھول دے۔",
                ar = "الدعاء المأثور عند دخول المسجد بطلب فتح أبواب رحمة الله.",
                hi = "अल्लाह के नाम से और रसूलुल्लाह ﷺ पर सलाम। ऐ अल्लाह! मेरे लिए अपनी दया के द्वार खोल दे।"
            ),
            reference = "Sahih Muslim 713 / Sunan Ibn Majah 771",
            sourceUrl = "https://sunnah.com/muslim:713a"
        ),
        HajjDuaEntry(
            id = "hajj_dua_rukn_yamani",
            linkedDuaDataId = "dua_rabbana_atina_dunya",
            occasion = LocalizedText("3. Between Yemeni Corner & Black Stone (Tawaf)", "3۔ طواف میں رکنِ یمانی اور حجرِ اسود کے درمیان", "3. الدعاء بين الركن اليماني والحجر الأسود", "3. तवाफ़ में रुक्ने-यमानी व हजर-ए-असवद के बीच"),
            arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
            transliteration = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.",
            translation = LocalizedText(
                en = "Our Lord! Grant us good in this world and good in the Hereafter, and protect us from the punishment of the Fire.",
                ur = "اے ہمارے رب! ہمیں دنیا میں بھی بھلائی عطا فرما اور آخرت میں بھی بھلائی عطا فرما، اور ہمیں آگ کے عذاب سے بچا لے۔",
                ar = "آية قرآنية جامعة (البقرة 201) كان النبي ﷺ يدعو بها بين الركن اليماني والحجر الأسود.",
                hi = "ऐ हमारे रब! हमें इस दुनिया में भी भलाई दे और आख़िरत में भी भलाई दे, तथा हमें आग के अज़ाब से बचा।"
            ),
            reference = "Quran 2:201; Sunan Abi Dawud 1892 (Hasan)",
            sourceUrl = "https://quran.com/2/201"
        ),
        HajjDuaEntry(
            id = "hajj_dua_safa_marwah",
            occasion = LocalizedText("4. Upon Reaching Safa & Marwah (Sa'i)", "4۔ کوہِ صفا و مروہ پر ذکر و دعا", "4. الذكر والدعاء على الصفا والمروة", "4. सफ़ा व मरवाह पर ज़िक्र व दुआ"),
            arabic = "إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ ۖ نَبْدَأُ بِمَا بَدَأَ اللَّهُ بِهِ — لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ، لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ، أَنْجَزَ وَعْدَهُ، وَنَصَرَ عَبْدَهُ، وَهَزَمَ الْأَحْزَابَ وَحْدَهُ",
            transliteration = "Innas-Safa wal-Marwata min sha'a'irillah (2:158); Nabda'u bima bada'Allahu bih. La ilaha illAllahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa Huwa 'ala kulli shay'in qadir; La ilaha illAllahu wahdah, anjaza wa'dah, wa nasara 'abdah, wa hazamal-ahzaba wahdah.",
            translation = LocalizedText(
                en = "Indeed Safa and Marwah are among the symbols of Allah... There is no deity worthy of worship except Allah alone without partner; to Him belongs dominion and praise, and He is over all things capable; He fulfilled His promise, aided His servant, and defeated the confederates alone.",
                ur = "بے شک صفا اور مروہ اللہ کی نشانیوں میں سے ہیں... اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے اس کا کوئی شریک نہیں، اسی کی بادشاہی اور تعریف ہے، اس نے اپنا وعدہ سچا کیا، اپنے بندے کی مدد فرمائی اور تمام لشکروں کو تنہا شکست دی۔",
                ar = "الذكر الثابت عن النبي ﷺ على الصفا والمروة ثلاث مرات مع الدعاء بينها (مسلم 1218).",
                hi = "निश्चय ही सफ़ा व मरवाह अल्लाह की निशानियों में से हैं... अल्लाह के सिवा कोई पूज्य नहीं, वह अकेला है, उसी की बादशाही व प्रशंसा है; उसने अपना वादा पूरा किया व अपने बंदे की मदद की।"
            ),
            reference = "Quran 2:158; Sahih Muslim 1218",
            sourceUrl = "https://sunnah.com/muslim:1218a"
        ),
        HajjDuaEntry(
            id = "hajj_dua_arafah",
            linkedDuaDataId = "dua_arafah_best_supplication",
            occasion = LocalizedText("5. Best Supplication on the Day of Arafah (9 Dhul-Hijjah)", "5۔ یومِ عرفہ کی بہترین دعا", "5. أفضل دعاء يوم عرفة", "5. अरफ़ात के दिन की सर्वश्रेष्ठ दुआ"),
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "La ilaha illAllahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa Huwa 'ala kulli shay'in qadir.",
            translation = LocalizedText(
                en = "There is no deity worthy of worship except Allah alone without partner. To Him belongs all sovereignty and all praise, and He is Omnipotent over everything.",
                ur = "اللہ کے سوا کوئی معبودِ برحق نہیں، وہ اکیلا ہے اس کا کوئی شریک نہیں، اسی کے لیے بادشاہت اور تمام تعریفیں ہیں، اور وہ ہر چیز پر قادر ہے۔",
                ar = "قال ﷺ: «خير الدعاء دعاء يوم عرفة، وخير ما قلت أنا والنبيون من قبلي: لا إله إلا الله وحده لا شريك له...» (الترمذي 3585).",
                hi = "अल्लाह के सिवा कोई पूज्य नहीं, वह अकेला है उसका कोई साझी नहीं, उसी का राज्य और उसी की प्रशंसा है और वह हर चीज़ पर सर्वसमर्थ है।"
            ),
            reference = "Jami' at-Tirmidhi 3585 (Hasan); Al-Muwatta 500",
            sourceUrl = "https://sunnah.com/tirmidhi:3585"
        ),
        HajjDuaEntry(
            id = "hajj_dua_ibrahim",
            occasion = LocalizedText("6. Quranic Dua of Ibrahim & Isma'il (AS) at the Ka'bah", "6۔ تعمیرِ کعبہ کے وقت حضرت ابراہیمؑ کی قرآنی دعا", "6. دعاء إبراهيم وإسماعيل عليهما السلام عند الكعبة", "6. काबा निर्माण पर इब्राहीम व इस्माईल (अ.स.) की दुआ"),
            arabic = "رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنتَ السَّمِيعُ الْعَلِيمُ ۝ رَبَّنَا وَاجْعَلْنَا مُسْلِمَيْنِ لَكَ وَمِن ذُرِّيَّتِنَا أُمَّةً مُّسْلِمَةً لَّكَ وَأَرِنَا مَنَاسِكَنَا وَتُبْ عَلَيْنَا ۖ إِنَّكَ أَنتَ التَّوَّابُ الرَّحِيمُ",
            transliteration = "Rabbana taqabbal minna innaka Antas-Sami'ul-'Alim. Rabbana waj'alna muslimayni laka wa min dhurriyyatina ummatan muslimatan laka wa arina manasikana wa tub 'alayna innaka Antat-Tawwabur-Rahim.",
            translation = LocalizedText(
                en = "Our Lord! Accept [this] from us; indeed You are the All-Hearing, the All-Knowing. Our Lord! Make us both Muslims [submitting] to You, and from our descendants a Muslim community, and show us our rites of worship and accept our repentance.",
                ur = "اے ہمارے رب! ہم سے قبول فرما، بے شک تو ہی سننے والا جاننے والا ہے۔ اے ہمارے رب! ہمیں اپنا فرمانبردار بنا، ہماری اولاد میں سے بھی ایک فرمانبردار امت پیدا فرما، ہمیں ہماری عبادت کے طریقے (مناسک) سکھا اور ہماری توبہ قبول فرما۔",
                ar = "آيتان كريمتان من سورة البقرة (127–128) في دعاء القبول وتعليم المناسك والتوبة.",
                hi = "ऐ हमारे रब! हमसे यह इबादत क़बूल फरमा, बेशक तू ही सुनने व जानने वाला है। हमें अपना आज्ञाकारी बना और हमें हमारे हज/इबादत के तरीके दिखा दे तथा हमारी तौबा क़बूल कर।"
            ),
            reference = "Quran 2:127–128",
            sourceUrl = "https://quran.com/2/127-128"
        )
    )

    val defaultChecklists: List<GuideChecklistItem> = listOf(
        // Ihram Prep
        GuideChecklistItem("chk_ihram_sheets", ChecklistCategory.IHRAM_PREP,
            LocalizedText("Two clean unstitched white Ihram sheets + belt/pouch (Men)", "دو صاف اَن سِلی چادریں اور احرام بیلٹ (مرد)", "إزار ورداء نظيفان وحزام لحفظ الأغراض (للرجال)", "दो साफ़ बिना सिले इहराम कपड़े व बेल्ट (पुरुष)")),
        GuideChecklistItem("chk_ihram_women_attire", ChecklistCategory.IHRAM_PREP,
            LocalizedText("Comfortable modest clothing + breathable scarf without niqab/gloves (Women)", "آرام دہ ساتر لباس اور اسکارف بغیر نقاب و دستانوں کے (خواتین)", "ثياب فضفاضة ساترة ومريحة دون نقاب وقفازين (للنساء)", "आरामदायक शालीन कपड़े व स्कार्फ़ बिना नक़ाब/दस्ताने (महिलाएँ)")),
        GuideChecklistItem("chk_ihram_sandals", ChecklistCategory.IHRAM_PREP,
            LocalizedText("Broken-in open sandals exposing top foot bone / ankles", "آرام دہ کھلی چپل جس سے پاؤں کی اوپری ہڈی/ٹخنے کھلے رہیں", "نعل مريح يكشف ظهر القدم والكعبين", "आरामदायक खुली चप्पल (पैर की ऊपरी हड्डी व टखने खुले)")),
        GuideChecklistItem("chk_ihram_unscented", ChecklistCategory.IHRAM_PREP,
            LocalizedText("Fragrance-free soap, unscented moisturizer & wet wipes", "بغیر خوشبو والا صابن، لوشن اور وائپس", "صابون ومرطب ومناديل خالية من العطور", "बिना सुगंध वाला साबुन, मॉइस्चराइज़र व वाइप्स")),
        // Packing
        GuideChecklistItem("chk_pack_meds", ChecklistCategory.PACKING,
            LocalizedText("Prescription medications + doctor's letter & blister plasters", "ڈاکٹر کے نسخے والی ادویات اور چھالوں کی پٹیاں", "الأدوية الشخصية مع الوصفة الطبية ولاصقات القدم", "नियमित दवाइयाँ, डॉक्टर का पर्चा व फुट पैड")),
        GuideChecklistItem("chk_pack_bag", ChecklistCategory.PACKING,
            LocalizedText("Small drawstring bag for shoes & pebbles in Haram/Mina", "مسجد میں جوتے اور کنکریاں رکھنے کے لیے چھوٹا بیگ", "حقيبة خفيفة لحفظ الحذاء والحصى في الحرم ومنى", "चप्पल व कंकड़ियाँ रखने का छोटा थैला")),
        GuideChecklistItem("chk_pack_hydration", ChecklistCategory.PACKING,
            LocalizedText("Refillable water flask, umbrella/hat-free sun shade & power bank", "پانی کی بوتل، دھوپ سے بچاؤ کے لیے چھتری اور پاور بینک", "قارورة ماء ومظلة للوقاية من الشمس وشاحن متنقل", "पानी की बोतल, धूप के लिए छाता और पावर बैंक")),
        // Travel Prep
        GuideChecklistItem("chk_prep_docs", ChecklistCategory.TRAVEL_PREP,
            LocalizedText("Valid passport, Hajj/Umrah visa, Nusuk permit & emergency contacts", "پاسپورٹ، ویزا، نسک پرمٹ اور ہنگامی رابطہ نمبرز", "جواز السفر والتأشيرة وتصريح نسك وأرقام الطوارئ", "पासपोर्ट, वीज़ा, नुसुक परमिट व आपातकालीन नंबर")),
        GuideChecklistItem("chk_prep_vaccines", ChecklistCategory.TRAVEL_PREP,
            LocalizedText("Confirm current Saudi Ministry of Health vaccinations on haj.gov.sa", "سعودی وزارتِ صحت کے تازہ ویکسین ضوابط (haj.gov.sa) کی تصدیق", "التحقق من الاشتراطات الصحية واللقاحات عبر haj.gov.sa", "haj.gov.sa पर नवीनतम टीकाकरण व स्वास्थ्य नियमों की जांच")),
        GuideChecklistItem("chk_prep_debts", ChecklistCategory.TRAVEL_PREP,
            LocalizedText("Settle debts, write will (Wasiyyah), and seek forgiveness from others", "قرضوں کی ادائیگی/تصفیہ، وصیت اور لوگوں سے معافی تلافی", "أداء الديون وكتابة الوصية وطلب المسامحة ورد المظالم", "कर्ज़ चुकाना, वसीयत लिखना और लोगों से क्षमा माँगना"))
    )

    val officialLinks: List<Pair<LocalizedText, String>> = listOf(
        LocalizedText("Saudi Ministry of Hajj & Umrah", "سعودی وزارتِ حج و عمرہ (سرکاری پورٹل)", "وزارة الحج والعمرة السعودية", "सऊदी हज व उमराह मंत्रालय") to "https://www.haj.gov.sa/",
        LocalizedText("Nusuk Official Pilgrim Portal", "نسک آفیشل پورٹل (پرمٹ و معلومات)", "منصة نسك الرسمية", "नुसुक आधिकारिक पोर्टल") to "https://www.nusuk.sa/",
        LocalizedText("Quran.com (Reference Verses)", "القرآن الکریم (مستند آیات)", "القرآن الكريم (Quran.com)", "क़ुरआन संदर्भ (Quran.com)") to "https://quran.com/",
        LocalizedText("Sunnah.com (Hadith References)", "مستند کتبِ احادیث (Sunnah.com)", "الموسوعة الحديثية (Sunnah.com)", "हदीस संदर्भ (Sunnah.com)") to "https://sunnah.com/"
    )

    fun validationDisclaimer(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH ->
            "Religious Validity Disclaimer: Step checklists and completion bars are personal study and planning aids only; checking off a box never validates worship by itself. Validity of Hajj and Umrah rests on sincere intention, fulfilling Allah's commands accurately, and qualified scholarly guidance for personal situations."
        AppLanguage.URDU ->
            "شرعی وضاحت: یہاں موجود چیک لسٹ اور پروگریس بار صرف آپ کی ذاتی یاددہانی اور مطالعے کے لیے ہیں؛ محض نشان لگانے سے عبادت ادا نہیں ہوتی۔ حج و عمرہ کی صحت اور قبولیت خلوصِ نیت، شرعی ارکان کی درست ادائیگی اور مخصوص مسائل میں مستند علماء سے رہنمائی پر موقوف ہے۔"
        AppLanguage.ARABIC ->
            "تنبيه شرعي: قوائم المهام ومؤشرات التقدم هنا وسائل تنظيمية وتعليمية للمراجعة الشخصية فقط ولا تثبت بها صحة النسك تلقائياً. صحة الحج والعمرة مبنية على الإخلاص وأداء الأركان والواجبات الشرعية، ويُرجع في المسائل الخاصة إلى أهل العلم الثقات."
        AppLanguage.HINDI ->
            "धार्मिक अस्वीकरण: यहाँ दी गई चेकलिस्ट और प्रगति सूचक केवल व्यक्तिगत अध्ययन व योजना के सहायक हैं; किसी बॉक्स को टिक करने से इबादत स्वतः मान्य नहीं होती। हज व उमराह की वैधता सच्ची नीयत, अरकान के सही पालन और जटिल स्थितियों में योग्य विद्वानों के मार्गदर्शन पर निर्भर करती है।"
    }
}
