package com.example.data.shifa

/**
 * Verified Prophetic Hadiths, Masnoon Ruqyah Duas, Tib-e-Nabawi remedies,
 * Ruqyah etiquette guides, and the Islamic Treatment Search Index.
 *
 * Every Hadith cites exact Sahih al-Bukhari or Sahih Muslim book & number
 * along with verified sunnah.com / islamqa.info references. Zero fabricated
 * medical claims, invented dosages, unverified counts, or non-Islamic practices.
 */
object ShifaTibHadithData {

    const val EXACT_UNMAPPED_CONDITION_NOTICE =
        "Is condition ke liye koi specific Quranic ayat ya masnoon treatment hamare verified sources mein sabit nahi mila. Aap general Quranic Shifa aur authentic Ruqyah guidance dekh sakte hain."

    val items: List<ShifaRuqyahItem> = listOf(
        // =========================================================================
        // B. RUQYAH SHAR'IYYAH — MASNOON DUAS & SUNNAH METHOD
        // =========================================================================
        ShifaRuqyahItem(
            id = "ruqyah_dua_rabban_nas",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.SUNNAH_DUA,
            topicTag = LocalizedText4(
                en = "Masnoon Ruqyah Supplication",
                ur = "مسنون دعائے شفا و رقیہ",
                ar = "دعاء الشفاء النبوي",
                hi = "मसनून रुक़या दुआ"
            ),
            title = LocalizedText4(
                en = "Allahumma Rabban-Nas (Remove Harm & Grant Healing)",
                ur = "اللَّهُمَّ رَبَّ النَّاسِ — بیماری دور فرمانے اور شفا مانگنے کی نبوی دعا",
                ar = "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَاسَ",
                hi = "अल्लाहुम्मा रब्बन-नास (कष्ट दूर करने और शिफ़ा की दुआ)"
            ),
            arabicText = "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَاسَ، اشْفِهِ وَأَنْتَ الشَّافِي، لَا شِفَاءَ إِلَّا شِفَاؤُكَ، شِفَاءً لَا يُغَادِرُ سَقَمًا",
            translation = LocalizedText4(
                en = "O Allah, Lord of mankind, remove the hardship; heal him/her, for You are the Healer. There is no healing except Your healing—a healing that leaves no illness behind.",
                ur = "اے اللہ، تمام انسانوں کے رب! تکلیف دور فرما دے، شفا عطا فرما، تو ہی شفا دینے والا ہے، تیری شفا کے سوا کوئی شفا نہیں، ایسی شفا عطا فرما جو کسی بیماری کو باقی نہ چھوڑے۔",
                ar = "اللهم يا رب الناس أذهب البأس والمرض، واشفه فأنت الشافي وحدك، لا شفاء إلا شفاؤك، شفاءً تاماً لا يترك أثراً للمرض.",
                hi = "ऐ अल्लाह, समस्त इंसानों के रब! इस तकलीफ़ को दूर फ़रमा, शिफ़ा अता फ़रमा, तू ही शिफ़ा देने वाला है; तेरी शिफ़ा के सिवा कोई शिफ़ा नहीं—ऐसी शिफ़ा जो कोई बीमारी बाक़ी न छोड़े।"
            ),
            narrator = "Aishah (RA) — Sahih Muslim 2191c & 2194",
            islamicContext = LocalizedText4(
                en = "Narrated by Aishah (RA) that when any member of his household fell ill, the Messenger of Allah ﷺ would wipe over them with his right hand and recite this supplication, affirming that all healing belongs strictly to Allah.",
                ur = "حضرت عائشہؓ سے روایت ہے کہ جب اہلِ خانہ میں سے کوئی بیمار ہوتا تو نبی کریم ﷺ اپنا دایاں ہاتھ اس پر پھیرتے اور یہ دعا پڑھ کر دم فرماتے۔",
                ar = "عن عائشة رضي الله عنها أن رسول الله صلى الله عليه وسلم كان إذا اشتكى إنسان منهم مسحه بيده اليمنى ثم قال هذا الدعاء.",
                hi = "हज़रत आइशा (रज़ि.) से रिवायत है कि जब कोई बीमार होता तो नबी ﷺ अपना दायाँ हाथ फेरते हुए यह दुआ पढ़ते थे।"
            ),
            recitationMethod = LocalizedText4(
                en = "Lightly pass the right hand over the sick person (or oneself) while reciting sincerely with Tawakkul.",
                ur = "دایاں ہاتھ مریض پر یا اپنے جسم پر پھیرتے ہوئے اخلاص و یقین کے ساتھ پڑھیں۔",
                ar = "يمسح باليد اليمنى على المريض أو على نفسه مع التوكل على الله.",
                hi = "दायाँ हाथ फेरते हुए पूर्ण विश्वास के साथ यह दुआ पढ़ें।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 2191c",
                bookAndNumber = "Sahih Muslim 2191c",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:2191c"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih Muslim 2194", "Sahih Muslim 2194", "Sahih", "https://sunnah.com/muslim:2194")
            ),
            searchKeywords = listOf("ruqyah", "dua", "rabban nas", "ishfi", "bukhar", "jism ka dard", "sar dard", "شفا", "رقیہ", "शिफ़ा"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "ruqyah_dua_jibril",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.SUNNAH_DUA,
            topicTag = LocalizedText4(
                en = "Jibril's (AS) Ruqyah over the Prophet ﷺ",
                ur = "حضرت جبرائیلؑ کا نبوی دم",
                ar = "رقية جبريل عليه السلام",
                hi = "हज़रत जिब्रील (अलैहि.) की रुक़या दुआ"
            ),
            title = LocalizedText4(
                en = "Bismillahi Arqika — In the Name of Allah I Perform Ruqyah for You",
                ur = "بِاسْمِ اللَّهِ أَرْقِيكَ — ہر تکلیف اور حسد سے بچاؤ کا مسنون دم",
                ar = "بِاسْمِ اللَّهِ أَرْقِيكَ مِنْ كُلِّ شَيْءٍ يُؤْذِيكَ",
                hi = "बिस्मिल्लाहि अर्क़ीका — हर तकलीफ़ व हसद से सुरक्षा की रुक़या"
            ),
            arabicText = "بِاسْمِ اللَّهِ أَرْقِيكَ، مِنْ كُلِّ شَيْءٍ يُؤْذِيكَ، مِنْ شَرِّ كُلِّ نَفْسٍ أَوْ عَيْنِ حَاسِدٍ، اللَّهُ يَشْفِيكَ، بِاسْمِ اللَّهِ أَرْقِيكَ",
            translation = LocalizedText4(
                en = "In the Name of Allah I perform Ruqyah for you, from everything that harms you, from the evil of every soul or envious eye; may Allah heal you. In the Name of Allah I perform Ruqyah for you.",
                ur = "اللہ کے نام کے ساتھ میں تم پر دم کرتا ہوں ہر اس چیز سے جو تمہیں تکلیف دے، ہر نفس کے شر یا حاسد کی نظرِ بد سے، اللہ تمہیں شفا عطا فرمائے، اللہ کے نام کے ساتھ میں تم پر دم کرتا ہوں۔",
                ar = "باسم الله أرقيك من كل ما يؤذيك، ومن شر كل نفس أو عين حاسد، الله يشفيك، باسم الله أرقيك.",
                hi = "अल्लाह के नाम के साथ मैं तुम पर दम करता हूँ हर उस चीज़ से जो तुम्हें तकलीफ़ दे, हर नफ़्स या हसद करने वाली बुरी नज़र से; अल्लाह तुम्हें शिफ़ा दे।"
            ),
            narrator = "Abu Sa'id al-Khudri (RA) — Sahih Muslim 2186",
            islamicContext = LocalizedText4(
                en = "Angel Jibril (AS) came to the Prophet ﷺ when he fell ill and recited this comprehensive Ruqyah covering physical harm, envious eyes, and spiritual affliction. Notice that Allah alone is asked to grant healing.",
                ur = "حضرت ابو سعید خدریؓ سے مروی ہے کہ جب نبی کریم ﷺ بیمار ہوئے تو حضرت جبرائیلؑ نے حاضر ہو کر آپ پر ان الفاظ سے دم فرمایا۔",
                ar = "أتى جبريل عليه السلام النبي صلى الله عليه وسلم حين اشتكى فرقاه بهذه الكلمات المباركة.",
                hi = "हज़रत अबू सईद ख़ुदरी (रज़ि.) की रिवायत है कि जब नबी ﷺ अस्वस्थ हुए तो जिब्रील (अलैहि.) ने इन शब्दों से आप पर दम किया।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 2186",
                bookAndNumber = "Sahih Muslim 2186",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:2186"
            ),
            searchKeywords = listOf("ruqyah", "jibril", "nazar", "hasad", "sihr", "roohani pareshani", "रुक़या", "نظر", "سحر"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "ruqyah_dua_pain_7x",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.SUNNAH_DUA,
            topicTag = LocalizedText4(
                en = "Self-Ruqyah for Localized Bodily Pain",
                ur = "جسم کے درد پر خود دم کرنے کی مسنون دعا",
                ar = "رقية الألم في الجسد",
                hi = "शरीर के दर्द पर मसनून दम"
            ),
            title = LocalizedText4(
                en = "Bismillah (3x) & A'udhu Billahi wa Qudratihi (7x) on the Pain",
                ur = "بِسْمِ اللَّهِ (3 بار) اور أَعُوذُ بِاللَّهِ وَقُدْرَتِهِ (7 بار) — جسم کے درد کا نبوی عمل",
                ar = "ضع يدك على الذي تألم من جسدك وقل بسم الله ثلاثاً...",
                hi = "दर्द वाली जगह पर हाथ रखकर बिस्मिल्लाह (3 बार) व अऊज़ु बिल्लाहि (7 बार)"
            ),
            arabicText = "بِسْمِ اللَّهِ (٣ مَرَّاتٍ) ۞ أَعُوذُ بِاللَّهِ وَقُدْرَتِهِ مِنْ شَرِّ مَا أَجِدُ وَأُحَاذِرُ (٧ مَرَّاتٍ)",
            translation = LocalizedText4(
                en = "Say 'Bismillah' (In the Name of Allah) three times, then say seven times: 'I seek refuge in Allah and His power from the evil of what I feel and fear.'",
                ur = "درد کی جگہ پر ہاتھ رکھ کر تین مرتبہ «بِسْمِ اللَّهِ» کہیں، پھر سات مرتبہ یہ دعا پڑھیں: «میں اللہ اور اس کی قدرت کی پناہ مانگتا ہوں اس تکلیف کے شر سے جو میں محسوس کر رہا ہوں اور جس کا مجھے اندیشہ ہے۔»",
                ar = "تضع يدك على موضع الألم وتقول: بسم الله ثلاثاً، ثم تقول سبع مرات: أعوذ بالله وقدرته من شر ما أجد وأحاذر.",
                hi = "जहाँ दर्द हो वहाँ हाथ रखकर तीन बार 'बिस्मिल्लाह' कहें, फिर सात बार कहें: 'मैं अल्लाह और उसकी क़ुदरत की पनाह माँगता हूँ उस तकलीफ़ के शर से जो मैं महसूस करता हूँ और जिससे डरता हूँ।'"
            ),
            narrator = "Uthman ibn Abi al-As (RA) — Sahih Muslim 2202",
            islamicContext = LocalizedText4(
                en = "Uthman ibn Abi al-As complained to the Prophet ﷺ about physical pain in his body since accepting Islam, and the Prophet ﷺ personally instructed him with this exact wording and repetition.",
                ur = "حضرت عثمان بن ابی العاصؓ نے نبی ﷺ سے جسمانی درد کی شکایت کی تو آپ ﷺ نے انہیں خود یہ ہاتھ رکھ کر پڑھنے کی دعا اور تعداد سکھائی۔",
                ar = "شكا عثمان بن أبي العاص رضي الله عنه وجعاً في جسده فأرشده النبي صلى الله عليه وسلم إلى وضع اليد على مكان الألم وترديد هذا الدعاء بالعدد المذكور.",
                hi = "हज़रत उसमान बिन अबिल-आस (रज़ि.) ने शरीर में दर्द की शिकायत की तो नबी ﷺ ने स्वयं यह दुआ और गिनती सिखाई।"
            ),
            recitationMethod = LocalizedText4(
                en = "Place your right hand directly on the area of discomfort, recite 'Bismillah' 3 times, then 'A'udhu billahi wa qudratihi min sharri ma ajidu wa uhadhiru' 7 times.",
                ur = "تکلیف کی جگہ پر اپنا دایاں ہاتھ رکھیں، 3 بار 'بسم اللہ' اور 7 بار 'أعوذ بالله وقدرته...' پڑھیں۔",
                ar = "وضع اليد على موضع الألم مع قراءة بسم الله ثلاثاً والدعاء سبع مرات.",
                hi = "दर्द की जगह हाथ रखकर 3 बार बिस्मिल्लाह और 7 बार यह दुआ पढ़ें।"
            ),
            establishedCountNote = LocalizedText4(
                en = "Explicitly proven count in Sahih Muslim 2202: Bismillah (3 times) + A'udhu billahi wa qudratihi... (7 times).",
                ur = "صحیح مسلم (2202) سے صراحتاً ثابت تعداد: بسم اللہ 3 مرتبہ اور دعا 7 مرتبہ۔",
                ar = "العدد المنصوص في صحيح مسلم (٢٢٠٢): بسم الله ثلاث مرات، والدعاء سبع مرات.",
                hi = "सहीह मुस्लिम (2202) से प्रमाणित संख्या: बिस्मिल्लाह 3 बार तथा दुआ 7 बार।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 2202",
                bookAndNumber = "Sahih Muslim 2202",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:2202"
            ),
            searchKeywords = listOf("jism ka dard", "pain", "body ache", "sar dard", "dard", "درد", "جسم کا درد", "दर्द"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "ruqyah_dua_hasan_husayn",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.SUNNAH_DUA,
            topicTag = LocalizedText4(
                en = "Protection for Children & Family",
                ur = "بچوں اور اہلِ خانہ کے لیے مسنون دم",
                ar = "تعويذ الأبناء بالرقية النبوية",
                hi = "बच्चों व परिवार के लिए मसनून दम"
            ),
            title = LocalizedText4(
                en = "U'idhukuma bi-Kalimatillahit-Tammah (Bukhari 3371)",
                ur = "بچوں کے لیے نبوی تعویذِ کلام (صحیح بخاری 3371)",
                ar = "أُعِيذُكُمَا بِكَلِمَاتِ اللَّهِ التَّامَّةِ",
                hi = "उईज़ुकुमा बि-कलिमातिल्लाहित-ताम्माह (सहीह बुख़ारी 3371)"
            ),
            arabicText = "أُعِيذُكُمَا بِكَلِمَاتِ اللَّهِ التَّامَّةِ، مِنْ كُلِّ شَيْطَانٍ وَهَامَّةٍ، وَمِنْ كُلِّ عَيْنٍ لَامَّةٍ",
            translation = LocalizedText4(
                en = "I seek protection for you both in the perfect words of Allah from every devil and every harmful creature, and from every evil, envious eye. (For one child: U'idhuka/U'idhuki)",
                ur = "میں تم دونوں کو اللہ کے کامل کلمات کی پناہ میں دیتا ہوں ہر شیطان سے، ہر زہریلے جانور سے اور ہر لگ جانے والی بد نظر سے۔ (ایک بچے کے لیے أُعِيذُكَ / لڑکی کے لیے أُعِيذُكِ کہیں)۔",
                ar = "أعيذكما بكلمات الله التامة من كل شيطان وهامة ومن كل عين تلمّ بصاحبها سوءاً.",
                hi = "मैं तुम दोनों को अल्लाह के पूर्ण शब्दों की शरण में देता हूँ हर शैतान, हर हानिकारक जीव और हर बुरी नज़र से। (एक बच्चे के लिए उईज़ुका/उईज़ुकि कहें)"
            ),
            narrator = "Ibn Abbas (RA) — Sahih al-Bukhari 3371",
            islamicContext = LocalizedText4(
                en = "The Prophet ﷺ used to seek Allah's protection for Al-Hasan and Al-Husayn with these words, explaining that Prophet Ibrahim (AS) likewise sought refuge for Ismail and Ishaq (AS) with them. No physical taweez/amulet is hung; words of supplication alone are recited.",
                ur = "نبی کریم ﷺ حضرت حسنؓ اور حضرت حسینؓ پر یہی کلمات پڑھ کر دم فرماتے اور فرماتے کہ حضرت ابراہیمؑ بھی حضرت اسماعیلؑ اور اسحاقؑ کے لیے یہی کلمات پڑھتے تھے۔ گلے میں تعویذ یا دھاگہ لٹکانے کے بجائے زبان سے دعا و رقیہ مسنون ہے۔",
                ar = "كان النبي صلى الله عليه وسلم يعوذ الحسن والحسين بهما، ويذكر أن إبراهيم عليه السلام كان يعوذ بهما إسماعيل وإسحاق.",
                hi = "नबी ﷺ हज़रत हसन और हुसैन (रज़ि.) पर इन्हीं शब्दों से दम फ़रमाते थे और बताया कि हज़रत इब्राहीम (अलैहि.) भी इन्हीं शब्दों से पनाह माँगते थे।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 3371",
                bookAndNumber = "Sahih al-Bukhari 3371",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:3371"
            ),
            searchKeywords = listOf("nazar", "children", "protection", "bachon", "evil eye", "نظر", "بچوں", "बच्चे"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "ruqyah_dua_kalimat_evening",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.SUNNAH_DUA,
            topicTag = LocalizedText4(
                en = "Protection Upon Stopping Anywhere",
                ur = "منزل یا رات کے وقت حفاظت کی مسنون دعا",
                ar = "الاستعاذة بكلمات الله التامات",
                hi = "अल्लाह के पूर्ण शब्दों से पनाह"
            ),
            title = LocalizedText4(
                en = "A'udhu bi-Kalimatillahit-Tammati min Sharri Ma Khalaq (Muslim 2708a)",
                ur = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
                ar = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
                hi = "अऊज़ु बि-कलिमातिल्लाहित-ताम्माति मिन शर्रि मा ख़लक़"
            ),
            arabicText = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
            translation = LocalizedText4(
                en = "I seek refuge in the perfect words of Allah from the evil of what He has created.",
                ur = "میں اللہ کے کامل کلمات کی پناہ مانگتا ہوں اس کی مخلوقات کے شر سے۔",
                ar = "أعوذ بكلمات الله التامات من شر ما خلق.",
                hi = "मैं अल्लाह के पूर्ण शब्दों की शरण माँगता हूँ उसकी रचनाओं की बुराई से।"
            ),
            narrator = "Khawlah bint Hakim (RA) — Sahih Muslim 2708a",
            islamicContext = LocalizedText4(
                en = "Whoever stops at a place or rests and recites these words is protected by Allah's permission until departing from that place.",
                ur = "جو شخص کسی جگہ اتر کر یہ کلمات کہہ لے، اللہ کے حکم سے اسے وہاں سے روانہ ہونے تک کوئی چیز نقصان نہیں پہنچا سکتی۔",
                ar = "من نزل منزلاً ثم قال هذه الكلمات لم يضره شيء حتى يرتحل من منزله ذلك.",
                hi = "जो व्यक्ति कहीं ठहरे और यह शब्द कह ले, तो वहाँ से चलने तक अल्लाह के हुक्म से उसे कोई नुकसान नहीं होगा।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 2708a",
                bookAndNumber = "Sahih Muslim 2708a",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:2708a"
            ),
            searchKeywords = listOf("ruqyah", "kalimat", "protection", "sleep", "neend ki pareshani", "حفاظت", "रुक़या"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "ruqyah_guide_self_vs_other",
            category = ShifaCategory.RUQYAH_SHARIYYAH,
            type = ShifaItemType.GUIDANCE_NOTE,
            topicTag = LocalizedText4(
                en = "Verified Sunnah Method",
                ur = "رقیہ کا مستند مسنون طریقہ",
                ar = "الطريقة النبوية للرقية (للنفس وللغير)",
                hi = "रुक़या का सहीह मसनून तरीक़ा"
            ),
            title = LocalizedText4(
                en = "Self-Ruqyah vs. Performing Ruqyah for Another Person — Sunnah Etiquette",
                ur = "خود پر رقیہ بمقابلہ دوسرے پر رقیہ کرنے کا مستند شرعی طریقہ",
                ar = "رقية الإنسان نفسه ورقية غيره وفق السنة الصحيحة",
                hi = "स्वयं पर रुक़या बनाम दूसरे व्यक्ति पर रुक़या का प्रामाणिक तरीक़ा"
            ),
            arabicText = "لاَ بَأْسَ بِالرُّقَى مَا لَمْ يَكُنْ فِيهِ شِرْكٌ — (صحيح مسلم ٢٢٠٠)",
            translation = LocalizedText4(
                en = "There is nothing wrong with Ruqyah so long as there is no Shirk (associating partners with Allah) in it. (Sahih Muslim 2200)",
                ur = "دم (رقیہ) کرنے میں کوئی حرج نہیں جب تک اس میں شرک نہ ہو۔ (صحیح مسلم 2200)",
                ar = "أباح النبي صلى الله عليه وسلم الرقى المشروعة ما خلت من الشرك والألفاظ المجهولة.",
                hi = "रुक़या (दम) करने में कोई हर्ज नहीं जब तक उसमें शिर्क न हो। (सहीह मुस्लिम 2200)"
            ),
            islamicContext = LocalizedText4(
                en = "• Self-Ruqyah (Recommended First): Recite Al-Fatihah (1:1–7), Ayat-ul-Kursi (2:255), Al-Baqarah (2:285–286), and the three Quls (112, 113, 114), cup both palms, lightly blow (nafth), and wipe over the face and body (Sahih al-Bukhari 5017, 5735), plus 'Bismillah 3x / A'udhu 7x' on painful spots (Muslim 2202).\n• Ruqyah for Another Person: Recite clearly understandable Quranic verses or Masnoon Duas over the patient ('Allahumma Rabban-Nas...' Muslim 2191c, or 'Bismillahi Arqika...' Muslim 2186), lightly blow, or wipe with the right hand (observing full Islamic modesty & Mahram boundaries).\n• Strictly Prohibited: Shirk, calling on other than Allah, wearing amulets/taweez with unknown symbols, claiming to speak with jinn or diagnose unseen witchcraft, or charging manipulative fees.",
                ur = "• خود پر دم کرنا (سب سے بہتر اور اصل سنت): سورۃ الفاتحہ، آیۃ الکرسی، سورۃ البقرہ کی آخری دو آیات اور تین قل پڑھ کر ہتھیلیوں میں ہلکا پھونک ماریں اور سر سے پورے جسم پر پھیر لیں (صحیح بخاری 5017، 5735)؛ درد کی جگہ پر ہاتھ رکھ کر 3 بار بسم اللہ اور 7 بار مسنون دعا پڑھیں (مسلم 2202)۔\n• دوسرے پر دم کرنے کا طریقہ: واضح عربی قرآنی آیات اور مسنون دعائیں (مسلم 2186، 2191c) بلند و واضح آواز سے پڑھ کر ہلکا پھونک کریں اور شرعی پردے و محرم حدود کی مکمل پابندی رکھیں۔\n• سخت ممنوع اعمال: غیر اللہ سے مدد مانگنا، شرکیہ کلمات، مجہول نقش و تعویذ لٹکانا، جھوٹے عاملوں کے پاس جانا یا کسی پر جادو/جنات کا بلا ثبوت دعویٰ کرنا۔",
                ar = "• رقية الإنسان نفسه: يقرأ الفاتحة وآية الكرسي وخواتيم البقرة والمعوذات، وينفث في كفيه ويمسح بهما جسده (البخاري ٥٠١٧)، مع وضع اليد على موضع الألم (مسلم ٢٢٠٢).\n• رقية الغير: قراءة القرآن والأدعية النبوية الصحيحة بصوت مسموع مفهوم، والنفث اللطيف مع الالتزام التام بالضوابط الشرعية والستر وعدم الخلوة.\n• المحرمات: الشرك، التمائم، الطلاسم، الكهنة، والتشخيص الموهوم بالسحر أو المس.",
                hi = "• स्वयं पर रुक़या (सबसे उत्तम सुन्नत): सूरह फ़ातिहा, आयत-उल-कुर्सी, सूरह बक़रा की अंतिम 2 आयतें और तीन क़ुल (112, 113, 114) पढ़कर हथेलियों पर हल्की फूँक मारें और शरीर पर फेरें (सहीह बुख़ारी 5017, 5735)।\n• दूसरे व्यक्ति पर रुक़या: स्पष्ट व अर्थपूर्ण क़ुरआनी आयतें और मसनून दुआएं (मुस्लिम 2186, 2191c) पढ़कर हल्की फूँक मारें और शराई पर्दा व महरम मर्यादा का पूरा पालन करें।\n• सख़्त मनाही: शिर्क, अज्ञात मंत्र/तावीज़ लटकाना, तांत्रिकों के पास जाना या बिना प्रमाण जादू/जिन्न का दावा करना।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "IslamQA #3476 — Ruqyah Guidelines in Islam",
                bookAndNumber = "Sunnah & Scholarly Fatwa #3476",
                authenticityGrade = "Verified Scholarly Guidance",
                url = "https://islamqa.info/en/answers/3476"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih Muslim 2200", "Sahih Muslim 2200", "Sahih", "https://sunnah.com/muslim:2200"),
                ShifaReferenceSource("Sahih al-Bukhari 5735", "Sahih al-Bukhari 5735", "Sahih", "https://sunnah.com/bukhari:5735"),
                ShifaReferenceSource("Sahih Muslim 2191c", "Sahih Muslim 2191c", "Sahih", "https://sunnah.com/muslim:2191c")
            ),
            searchKeywords = listOf("method", "how to", "ruqyah", "self", "tariqa", "طریقہ", "رقیہ", "तरीक़ा", "रुक़या"),
            supportsQuranAudio = false
        ),

        // =========================================================================
        // C. TIB-E-NABAWI — طبِ نبوی (Strictly Prophetic Action vs Scholarly Explanation)
        // =========================================================================
        ShifaRuqyahItem(
            id = "tib_honey",
            category = ShifaCategory.TIB_E_NABAWI,
            type = ShifaItemType.PROPHETIC_REMEDY,
            topicTag = LocalizedText4(
                en = "Prophetic Remedy • Honey (Al-Asal)",
                ur = "طبِ نبوی • شہد (العسل)",
                ar = "الطب النبوي • شربة عسل",
                hi = "तिब्ब-ए-नबवी • शहद"
            ),
            title = LocalizedText4(
                en = "Honey — Healing Mentioned in Quran (16:69) & Sahih al-Bukhari 5684",
                ur = "شہد — قرآنِ مجید (16:69) اور صحیح بخاری (5684) سے ثابت شفا کا ذریعہ",
                ar = "العسل — الشفاء في شربة عسل (صحيح البخاري ٥٦٨٤)",
                hi = "शहद — क़ुरआन (16:69) और सहीह बुख़ारी (5684) से प्रमाणित"
            ),
            arabicText = "الشِّفَاءُ فِي ثَلَاثَةٍ: فِي شَرْطَةِ مِحْجَمٍ، أَوْ شَرْبَةِ عَسَلٍ، أَوْ كَيَّةٍ بِنَارٍ، وَأَنْهَى أُمَّتِي عَنِ الْكَيِّ",
            translation = LocalizedText4(
                en = "Healing is in three things: in the incision of a cupper (Hijamah), in a drink of honey, or in cauterization with fire; and I forbid my Ummah from cauterization.",
                ur = "شفا تین چیزوں میں ہے: پچھنا لگوانے (حجامہ) میں، شہد کے ایک گھونٹ پینے میں، یا آگ سے داغنے میں؛ لیکن میں اپنی امت کو آگ سے داغنے سے منع کرتا ہوں۔",
                ar = "الشفاء في ثلاثة: شربة عسل، وشرطة محجم، وكية نار، وأنهى أمتي عن الكي.",
                hi = "शिफ़ा तीन चीज़ों में है: हिजामा (कपिंग) के चीरे में, शहद पीने में, या आग से दागने में; लेकिन मैं अपनी उम्मत को आग से दागने से मना करता हूँ।"
            ),
            narrator = "Ibn Abbas (RA) — Sahih al-Bukhari 5684",
            islamicContext = LocalizedText4(
                en = "Also supported by Surah An-Nahl (16:69): 'There emerges from their bellies a drink, varying in colors, in which there is healing for people.' Healing occurs strictly by Allah's will.",
                ur = "قرآن مجید میں سورۃ النحل (16:69) میں بھی شہد کے متعلق فرمایا گیا: «فِيهِ شِفَاءٌ لِّلنَّاسِ» (اس میں لوگوں کے لیے شفا ہے)۔",
                ar = "يوافقه قول الله تعالى في سورة النحل (١٦: ٦٩): ﴿فِيهِ شِفَاءٌ لِّلنَّاسِ﴾، والشفاء من الله وحده.",
                hi = "क़ुरआन की सूरह अन-नह्ल (16:69) में भी शहद के बारे में फ़रमाया गया: 'उसमें लोगों के लिए शिफ़ा है।'"
            ),
            propheticAction = LocalizedText4(
                en = "Prophet's ﷺ Established Action & Words: He explicitly named 'a drink of honey' among beneficial remedies in Sahih al-Bukhari 5684 (and similarly advised honey in Sahih al-Bukhari 5684/5716 without prescribing a rigid gram weight or multi-week schedule).",
                ur = "نبی کریم ﷺ کا ثابت شدہ عمل و ارشاد: صحیح بخاری (5684) میں آپ ﷺ نے 'شہد پینے' کو شفا کا ذریعہ قرار دیا؛ حدیث میں شہد کا کوئی مخصوص وزن، دنوں کی تعداد یا خوراک کا شیڈول مقرر نہیں کیا گیا۔",
                ar = "ما ثبت عن النبي صلى الله عليه وسلم: ذكره صراحةً أن الشفاء في «شربة عسل» دون تحديد مقدار وزني أو مدة زمنية ثابتة في هذا الحديث.",
                hi = "नबी ﷺ का प्रमाणित कथन: सहीह बुख़ारी (5684) में आप ﷺ ने केवल 'शहद पीने' को शिफ़ा का ज़रिया बताया; इसमें कोई मनगढ़ंत मात्रा, ख़ुराक या दिनों की गिनती तय नहीं की गई।"
            ),
            scholarlyExplanation = LocalizedText4(
                en = "Classical Scholars' Explanation: Ibn Hajar al-Asqalani (Fath al-Bari) and Ibn al-Qayyim explain that 'a drink of honey' (sharbat asal) indicates taking pure honey as a traditional Prophetic means of relief suited to one's constitution—without treating honey as a substitute for urgent care or inventing religious dosage rules.",
                ur = "اہلِ علم کی تشریح: حافظ ابن حجر عسقلانیؒ (فتح الباری) اور ابن القیمؒ بیان کرتے ہیں کہ شربتِ عسل سے مراد خالص شہد کا استعمال ہے؛ تاہم خود سے کوئی شرعی مقدار یا طبی دعویٰ گھڑنا درست نہیں۔",
                ar = "شرح العلماء: بيّن الحافظ ابن حجر في فتح الباري وابن القيم في الطب النبوي أن الحديث يشير إلى عموم التداوي بالعسل النقي بحسب الحاجة دون تخصيص مقادير توقيفية.",
                hi = "उलमा की व्याख्या: इब्न हजर (फ़तह अल-बारी) और इब्न अल-क़य्यिम स्पष्ट करते हैं कि इसका अर्थ शुद्ध शहद का उपयोग है, पर कोई धार्मिक मात्रा या गारंटीकृत रोग-निवारण दावा अपनी ओर से नहीं जोड़ना चाहिए।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5684",
                bookAndNumber = "Sahih al-Bukhari 5684",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5684"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Quran.com — Surah An-Nahl 16:69", "Quran 16:69", "Noble Quran", "https://quran.com/16/69")
            ),
            searchKeywords = listOf("honey", "shahad", "asal", "tib", "nabawi", "شہد", "طب نبوی", "शहद", "तिब्ब-ए-नबवी"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "tib_black_seed",
            category = ShifaCategory.TIB_E_NABAWI,
            type = ShifaItemType.PROPHETIC_REMEDY,
            topicTag = LocalizedText4(
                en = "Prophetic Remedy • Black Seed (Kalonji)",
                ur = "طبِ نبوی • کلونجی (الحبة السوداء)",
                ar = "الطب النبوي • الحبة السوداء",
                hi = "तिब्ब-ए-नबवी • कलौंजी (हब्बा सौदा)"
            ),
            title = LocalizedText4(
                en = "Al-Habbah as-Sawda (Black Seed / Kalonji) — Sahih al-Bukhari 5688",
                ur = "کلونجی (الحبۃ السوداء) — صحیح بخاری 5688",
                ar = "في الحبة السوداء شفاء من كل داء إلا السام (البخاري ٥٦٨٨)",
                hi = "कलौंजी (हब्बतुस-सौदा) — सहीह बुख़ारी 5688"
            ),
            arabicText = "إِنَّ هَذِهِ الْحَبَّةَ السَّوْدَاءَ شِفَاءٌ مِنْ كُلِّ دَاءٍ، إِلَّا مِنَ السَّامِ (قَالَتْ: وَمَا السَّامُ؟ قَالَ: الْمَوْتُ)",
            translation = LocalizedText4(
                en = "Indeed, this black seed is a healing from every disease except 'As-Sam'. She asked: 'What is As-Sam?' He replied: 'Death.'",
                ur = "بے شک یہ سیاہ دانہ (کلونجی) سوائے 'سام' کے ہر بیماری سے شفا ہے۔ عرض کیا گیا: 'سام کیا ہے؟' فرمایا: 'موت'۔",
                ar = "إن هذه الحبة السوداء شفاء من كل داء إلا السام، والسام: الموت.",
                hi = "निश्चय ही यह काला दाना (कलौंजी) 'साम' के सिवा हर बीमारी से शिफ़ा है। पूछा गया: 'साम क्या है?' फ़रमाया: 'मौत'।"
            ),
            narrator = "Aishah (RA) — Sahih al-Bukhari 5688",
            islamicContext = LocalizedText4(
                en = "Authenticated by both Imam al-Bukhari (5688) and Imam Muslim. Scholars emphasize understanding Arabic general expressions in light of classical commentary.",
                ur = "صحیح بخاری (5688) اور صحیح مسلم میں حضرت عائشہؓ اور حضرت ابوہریرہؓ سے ثابت شدہ حدیثِ مبارکہ۔",
                ar = "حديث صحيح متفق عليه، ويجب فهمه في ضوء شروح أئمة الحديث.",
                hi = "सहीह बुख़ारी (5688) व मुस्लिम में हज़रत आइशा (रज़ि.) से प्रमाणित हदीस।"
            ),
            propheticAction = LocalizedText4(
                en = "Prophet's ﷺ Established Words: He encouraged using Al-Habbah as-Sawda (Nigella sativa / Kalonji) as a general means of healing, without fixing any number of seeds, spoonfuls, oil drops, or days in this Hadith.",
                ur = "نبی کریم ﷺ کا ثابت شدہ ارشاد: آپ ﷺ نے کلونجی کو شفا کا ذریعہ قرار دیا؛ اس حدیث میں دانوں کی گنتی، چمچ، تیل کے قطروں یا دنوں کی کوئی پابند مقدار مقرر نہیں فرمائی گئی۔",
                ar = "ما ثبت في الحديث: التنصيص على بركة الحبة السوداء وكونها سبباً للشفاء دون تحديد عدد حبات أو جرعة معينة.",
                hi = "नबी ﷺ का प्रमाणित कथन: आपने कलौंजी में शिफ़ा की बात कही, परन्तु इस हदीस में दानों की गिनती, चम्मच, तेल की बूँदें या दिनों की कोई अवधि तय नहीं की।"
            ),
            scholarlyExplanation = LocalizedText4(
                en = "Classical Scholars' Explanation: Imam an-Nawawi and Ibn Hajar explain that 'from every disease' means many/cold-natured ailments in Arabic idiom (like Quran 46:25), prepared simply (such as crushing/taking with water or honey per classical custom), while healing always remains in Allah's decree—no fabricated clinical claims or fixed recipes may be attributed to the Prophet ﷺ.",
                ur = "اہلِ علم کی تشریح: امام نوویؒ اور حافظ ابن حجرؒ فرماتے ہیں کہ یہاں 'من کل داء' عربی اسلوب میں کثرت اور عام شفا کے معنی میں ہے؛ نہ کہ ہر نامزد مرض کا خودکار حتمی علاج یا من گھڑت نسخوں کا جواز۔",
                ar = "شرح العلماء: أوضح النووي وابن حجر أن قوله «من كل داء» من العام الذي يُراد به الكثرة أو بحسب ملاءمتها للحال في الطب التقليدي، دون اختراع مقادير لم ترد في السنة.",
                hi = "उलमा की व्याख्या: इमाम नववी और इब्न हजर स्पष्ट करते हैं कि 'हर बीमारी' अरबी मुहावरे में व्यापक लाभ को दर्शाता है; अपनी ओर से खुराकें या चिकित्सा दावे न जोड़ें।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5688",
                bookAndNumber = "Sahih al-Bukhari 5688",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5688"
            ),
            searchKeywords = listOf("kalonji", "black seed", "habbah sawda", "tib", "کلونجی", "कलौंजी", "तिब्ब-ए-नबवी"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "tib_hijamah",
            category = ShifaCategory.TIB_E_NABAWI,
            type = ShifaItemType.PROPHETIC_REMEDY,
            topicTag = LocalizedText4(
                en = "Prophetic Practice • Al-Hijamah",
                ur = "طبِ نبوی • حجامہ",
                ar = "الطب النبوي • الحجامة",
                hi = "तिब्ब-ए-नबवी • हिजामा"
            ),
            title = LocalizedText4(
                en = "Al-Hijamah (Cupping) — Sahih al-Bukhari 5681 & 5696",
                ur = "حجامہ — نبی کریم ﷺ کا پسندیدہ علاج (صحیح بخاری 5681)",
                ar = "خير ما تداويتم به الحجامة (صحيح البخاري ٥٦٨١ و ٥٦٩٦)",
                hi = "हिजामा — नबवी उपचारों में उत्तम (सहीह बुख़ारी 5681)"
            ),
            arabicText = "خَيْرُ مَا تَدَاوَيْتُمْ بِهِ الْحِجَامَةُ",
            translation = LocalizedText4(
                en = "The best of the remedies you use is cupping (Al-Hijamah).",
                ur = "جن چیزوں سے تم علاج کرتے ہو ان میں بہترین حجامہ ہے۔",
                ar = "إن خير ما تداويتم به الحجامة.",
                hi = "जिन तरीक़ों से तुम इलाज करते हो उनमें सबसे बेहतर हिजामा है।"
            ),
            narrator = "Anas ibn Malik (RA) — Sahih al-Bukhari 5696 & 5681",
            islamicContext = LocalizedText4(
                en = "In Sahih al-Bukhari 5681, Ibn Abbas (RA) narrated that the Prophet ﷺ himself underwent Hijamah and paid the practitioner.",
                ur = "صحیح بخاری (5681، 5696) میں حضرت ابن عباسؓ اور حضرت انسؓ سے مروی ہے کہ نبی کریم ﷺ نے خود حجامہ کروایا اور حجام کو اجرت عطا فرمائی۔",
                ar = "ثبت في صحيح البخاري (٥٦٨١) أن النبي صلى الله عليه وسلم احتجم وأعطى الحجام أجره.",
                hi = "सहीह बुख़ारी (5681) में प्रमाणित है कि नबी ﷺ ने स्वयं हिजामा करवाया और करने वाले को पारिश्रमिक दिया।"
            ),
            propheticAction = LocalizedText4(
                en = "Prophet's ﷺ Established Action: He commended Al-Hijamah and practiced it himself (Sahih al-Bukhari 5681). We do not add any unverified modern anatomical protocols or disease lists.",
                ur = "نبی کریم ﷺ کا ثابت شدہ عمل: آپ ﷺ نے خود حجامہ کروایا اور اسے بہترین علاج فرمایا (بخاری 5681)۔",
                ar = "ما ثبت عن النبي صلى الله عليه وسلم: مدحه للحجامة وممارسته لها بنفسه.",
                hi = "नबी ﷺ का प्रमाणित अमल: आप ﷺ ने स्वयं हिजामा करवाया और इसकी सराहना की (बुख़ारी 5681)।"
            ),
            scholarlyExplanation = LocalizedText4(
                en = "Classical Scholars' Explanation: Scholars like Ibn Hajar note that Hijamah was suited to warm climates and traditional practice; taking practical care with qualified hygiene and not promising cures beyond specific narrations reflects Prophetic wisdom.",
                ur = "اہلِ علم کی تشریح: محدثینِ کرام واضح کرتے ہیں کہ حجامہ ایک مسنون طبّی طریقہ ہے جس میں احتیاط، صفائی اور اہلیت ضروری ہے، اور کسی غیر منقول مرض کے یقینی خاتمے کا دعویٰ نہیں کیا جا سکتا۔",
                ar = "شرح العلماء: بيّن الشراح أن الحجامة سبب مشروع يناسب حال الشخص وموضعه عند الحاجة، دون دعوى علاجية غير ثابتة.",
                hi = "उलमा की व्याख्या: हदीस के व्याख्याकार स्पष्ट करते हैं कि हिजामा एक सुन्नत पारंपरिक उपाय है, जिसे सावधानी व आवश्यकता के अनुसार अपनाया जाता है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5681",
                bookAndNumber = "Sahih al-Bukhari 5681",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5681"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih al-Bukhari 5696", "Sahih al-Bukhari 5696", "Sahih", "https://sunnah.com/bukhari:5696")
            ),
            searchKeywords = listOf("hijamah", "cupping", "sar dard", "tib", "حجامہ", "हिजामा"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "tib_talbina",
            category = ShifaCategory.TIB_E_NABAWI,
            type = ShifaItemType.PROPHETIC_REMEDY,
            topicTag = LocalizedText4(
                en = "Prophetic Comfort • At-Talbinah",
                ur = "طبِ نبوی • تلبینہ (جو کا دلیہ)",
                ar = "الطب النبوي • التلبينة",
                hi = "तिब्ब-ए-नबवी • तलबीना (जौ का दलिया)"
            ),
            title = LocalizedText4(
                en = "At-Talbinah — Relieves the Heart & Removes Part of Grief (Bukhari 5689)",
                ur = "تلبینہ — مریض کے دل کو راحت اور غم میں کمی کا ذریعہ (صحیح بخاری 5689)",
                ar = "التَّلْبِينَةُ مُجِمَّةٌ لِفُؤَادِ الْمَرِيضِ، تَذْهَبُ بِبَعْضِ الْحُزْنِ",
                hi = "तलबीना — दिल को सुकून और ग़म हल्का करने वाला (सहीह बुख़ारी 5689)"
            ),
            arabicText = "التَّلْبِينَةُ مُجِمَّةٌ لِفُؤَادِ الْمَرِيضِ، تَذْهَبُ بِبَعْضِ الْحُزْنِ",
            translation = LocalizedText4(
                en = "Talbinah brings comfort to the heart of the sick person and takes away part of the grief.",
                ur = "تلبینہ (جو کا دلیہ) بیمار کے دل کو راحت پہنچاتا ہے اور غم کا کچھ حصہ دور کر دیتا ہے۔",
                ar = "التلبينة تريح قلب المريض وتنشطه وتذهب ببعض الحزن.",
                hi = "तलबीना बीमार के दिल को आराम देता है और ग़म के कुछ हिस्से को दूर कर देता है।"
            ),
            narrator = "Aishah (RA) — Sahih al-Bukhari 5689",
            islamicContext = LocalizedText4(
                en = "Aishah (RA) used to recommend Talbinah (a gentle broth made from barley flour and milk/honey) for the ill and for families experiencing bereavement, while saying: 'Though you dislike it, it is beneficial.'",
                ur = "حضرت عائشہؓ بیمار اور غمزدہ گھرانے کے لیے تلبینہ تیار کرنے کی تلقین فرمایا کرتی تھیں۔",
                ar = "كانت عائشة رضي الله عنها تأمر بالتلبينة للمريض وللمحزون على الهالك، مستشهدةً بهذا الحديث الصحيح.",
                hi = "हज़रत आइशा (रज़ि.) बीमार और शोकाकुल परिवार के लिए तलबीना तैयार करने को कहा करती थीं।"
            ),
            propheticAction = LocalizedText4(
                en = "Prophet's ﷺ Established Action & Words: He stated concisely that Talbinah soothes the sick person's heart and lessens some grief—without setting rigid cooking ratios or clinical mental-health claims.",
                ur = "نبی کریم ﷺ کا ثابت شدہ ارشاد: تلبینہ بیمار کے دل کو سکون دیتا اور غم کا کچھ حصہ ہلکا کرتا ہے (صحیح بخاری 5689)۔",
                ar = "ما ثبت عن النبي صلى الله عليه وسلم: بيان أثر التلبينة في إراحة قلب المريض وتخفيف الحزن دون تحديد كميات.",
                hi = "नबी ﷺ का प्रमाणित कथन: तलबीना बीमार के दिल को सुकून देता है और ग़म को कुछ कम करता है (बुख़ारी 5689)।"
            ),
            scholarlyExplanation = LocalizedText4(
                en = "Classical Scholars' Explanation: Ibn Hajar defines Talbinah as a light porridge made of barley meal (or bran) similar in whiteness to milk; Imam an-Nawawi notes it was comforting nourishment in times of sorrow or weakness.",
                ur = "اہلِ علم کی تشریح: محدثین کے مطابق تلبینہ جو کے آٹے اور دودھ وغیرہ سے تیار ہونے والی ہلکی غذا ہے جو غم اور کمزوری میں تسکین دیتی ہے۔",
                ar = "شرح العلماء: هي حساء يتخذ من دقيق الشعير أو نخالته ويلبّن بالحليب، تُقدّم للمريض والمحزون تسليةً وتقوية.",
                hi = "उलमा की व्याख्या: इब्न हजर व नववी के अनुसार तलबीना जौ के आटे से बना हल्का भोजन है जो शोक व कमज़ोरी में तसल्ली देता है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5689",
                bookAndNumber = "Sahih al-Bukhari 5689",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5689"
            ),
            searchKeywords = listOf("talbina", "talbinah", "dil ka gham", "gham", "barley", "تلبینہ", "غم", "तलबीना", "ग़म"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "tib_fever_water",
            category = ShifaCategory.TIB_E_NABAWI,
            type = ShifaItemType.PROPHETIC_REMEDY,
            topicTag = LocalizedText4(
                en = "Prophetic Guidance • Cooling Fever",
                ur = "طبِ نبوی • بخار کو پانی سے ٹھنڈا کرنا",
                ar = "الطب النبوي • تبريد الحمى بالماء",
                hi = "तिब्ब-ए-नबवी • बुख़ार को पानी से ठंडा करना"
            ),
            title = LocalizedText4(
                en = "Cooling Fever With Cool Water (Bukhari 5723)",
                ur = "بخار کی حدت کو پانی سے کم کرنا (صحیح بخاری 5723)",
                ar = "الحمى من فيح جهنم فأبردوها بالماء (البخاري ٥٧٢٣)",
                hi = "बुख़ार को पानी से ठंडा करना (सहीह बुख़ारी 5723)"
            ),
            arabicText = "الْحُمَّى مِنْ فَيْحِ جَهَنَّمَ، فَأَبْرِدُوهَا بِالْمَاءِ",
            translation = LocalizedText4(
                en = "Fever is from the heat of Hellfire, so cool it down with water.",
                ur = "بخار جہنم کی بھاپ (گرمی) کے اثر سے ہے، لہٰذا اسے پانی سے ٹھنڈا کرو۔",
                ar = "الحمى من فيح جهنم، فأبردوها بالماء.",
                hi = "बुख़ार जहन्नम की तपिश (भाप) से है, इसलिए उसे पानी से ठंडा करो।"
            ),
            narrator = "Abdullah ibn Umar (RA) — Sahih al-Bukhari 5723",
            islamicContext = LocalizedText4(
                en = "Multiple Companions (Ibn Umar, Aishah, Asma bint Abi Bakr, Rafi ibn Khadij) reported this Prophetic instruction in Sahih al-Bukhari and Sahih Muslim.",
                ur = "کئی صحابہ کرامؓ سے صحیح بخاری و صحیح مسلم میں بخار کی شدت کم کرنے کے لیے پانی استعمال کرنے کی یہ ہدایت مروی ہے۔",
                ar = "تواترت الروايات في الصحيحين عن عدة من الصحابة بهذا التوجيه النبوي لتبريد الحمى بالماء.",
                hi = "सहीह बुख़ारी व मुस्लिम में अनेक सहाबा से बुख़ार में पानी से ठंडा करने का यह निर्देश प्रमाणित है।"
            ),
            propheticAction = LocalizedText4(
                en = "Prophet's ﷺ Established Action: He instructed cooling fever with water (and Asma bint Abi Bakr RA would sprinkle water inside the collar of feverish patients as reported in Bukhari/Muslim), without inventing rigid temperatures or intervals.",
                ur = "نبی کریم ﷺ کا ثابت شدہ ارشاد: آپ ﷺ نے بخار کو پانی سے ٹھنڈا کرنے کا حکم فرمایا؛ حضرت اسماءؓ مریض کے سینے اور گریبان پر پانی ڈالتی تھیں۔",
                ar = "ما ثبت عن النبي صلى الله عليه وسلم: الأمر بتبريد الحمى بالماء، وفعل أسماء بنت أبي بكر رضي الله عنها في رشه.",
                hi = "नबी ﷺ का प्रमाणित अमल: आपने बुख़ार को पानी से ठंडा करने का निर्देश दिया; इसमें कोई निश्चित समय-अंतराल या तापमान नहीं बताया गया।"
            ),
            scholarlyExplanation = LocalizedText4(
                en = "Classical Scholars' Explanation: Ibn al-Qayyim and Ibn Hajar note that this teaches calming intense bodily heat gently with clean cool water according to one's condition, while pairing with sincere Dua and gratitude.",
                ur = "اہلِ علم کی تشریح: ابن حجرؒ اور ابن القیمؒ بیان کرتے ہیں کہ یہ گرمی و حدت کو نرم انداز میں پانی سے کم کرنے کی رہنمائی ہے، ساتھ ہی دعا و صبر کی تلقین ہے۔",
                ar = "شرح العلماء: بيّن ابن القيم وابن حجر أن المراد تخفيف حدة الحرارة بالماء الطاهر بلطف وبحسب الحال.",
                hi = "उलमा की व्याख्या: इब्न अल-क़य्यिम और इब्न हजर स्पष्ट करते हैं कि इसका आशय ताज़े पानी से गर्मी को सौम्यता से कम करना है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5723",
                bookAndNumber = "Sahih al-Bukhari 5723",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5723"
            ),
            searchKeywords = listOf("bukhar", "fever", "water", "humma", "بخار", "پانی", "बुख़ार"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "tib_ajwah_dates",
            category = ShifaCategory.TIB_E_NABAWI,
            type = ShifaItemType.PROPHETIC_REMEDY,
            topicTag = LocalizedText4(
                en = "Prophetic Protection • Ajwah Dates",
                ur = "طبِ نبوی • عجوہ کھجور (7 دانے)",
                ar = "الطب النبوي • سبع تمرات عجوة",
                hi = "तिब्ब-ए-नबवी • अजवा खजूर (7 दाने)"
            ),
            title = LocalizedText4(
                en = "Seven Ajwah Dates in the Morning (Bukhari 5445)",
                ur = "صبح 7 عجوہ کھجوریں کھانے کی فضیلت و حفاظت (صحیح بخاری 5445)",
                ar = "من تصبّح كل يوم سبع تمرات عجوة (صحيح البخاري ٥٤٤٥)",
                hi = "सुबह सात अजवा खजूर (सहीह बुख़ारी 5445)"
            ),
            arabicText = "مَنْ تَصَبَّحَ كُلَّ يَوْمٍ سَبْعَ تَمَرَاتٍ عَجْوَةً لَمْ يَضُرَّهُ فِي ذَلِكَ الْيَوْمِ سُمٌّ وَلَا سِحْرٌ",
            translation = LocalizedText4(
                en = "Whoever eats seven Ajwah dates every morning will not be harmed by poison or magic (Sihr) on that day.",
                ur = "جو شخص ہر روز صبح سات عجوہ کھجوریں کھائے، اس دن اسے نہ کوئی زہر نقصان پہنچا سکے گا اور نہ کوئی جادو (سحر)۔",
                ar = "من أكل في صباح كل يوم سبع تمرات عجوة لم يضره ذلك اليوم سم ولا سحر بإذن الله.",
                hi = "जो व्यक्ति हर रोज़ सुबह सात अजवा खजूर खाए, उस दिन उसे न ज़हर नुक़सान पहुँचाएगा और न जादू (सिह्र)।"
            ),
            narrator = "Sa'd ibn Abi Waqqas (RA) — Sahih al-Bukhari 5445",
            islamicContext = LocalizedText4(
                en = "One of the few remedies in Sahih Hadith where an exact count (seven dates in the morning) is explicitly reported from the Prophet ﷺ.",
                ur = "ان چند مستند احادیث میں سے ایک جن میں عدد (صبح 7 عجوہ کھجوریں) صراحتاً حدیث سے ثابت ہے۔",
                ar = "من المواضع القليلة التي ورد فيها العدد (سبع تمرات صباحاً) منصوصاً عليه في الصحيحين.",
                hi = "सहीह हदीस में गिने-चुने स्थानों में से एक जहाँ संख्या ('सात अजवा खजूर सुबह') स्पष्ट रूप से वर्णित है।"
            ),
            propheticAction = LocalizedText4(
                en = "Prophet's ﷺ Established Action & Count: Eating 7 Ajwah dates in the morning is explicitly stated verbatim in Sahih al-Bukhari 5445 / Sahih Muslim 2047b.",
                ur = "نبی کریم ﷺ کا ثابت شدہ عمل و تعداد: ہر صبح 7 عجوہ کھجوریں (صحیح بخاری 5445 و صحیح مسلم)۔",
                ar = "ما ثبت في الحديث: أكل سبع تمرات عجوة في الصباح (البخاري ٥٤٤٥).",
                hi = "नबी ﷺ का प्रमाणित कथन व संख्या: सुबह 7 अजवा खजूर (सहीह बुख़ारी 5445)।"
            ),
            scholarlyExplanation = LocalizedText4(
                en = "Classical Scholars' Explanation: Al-Khattabi, Ibn Hajar, and An-Nawawi explain this as a special Prophetic blessing (barakah) granted by Allah's will, especially tied to the blessed dates of Madinah al-Munawwarah—and not a license to neglect caution or self-protection.",
                ur = "اہلِ علم کی تشریح: امام خطابیؒ، حافظ ابن حجرؒ اور امام نوویؒ اسے نبی کریم ﷺ کی دعا اور مدینہ منورہ کی عجوہ کھجور کی خاص برکتِ الٰہی قرار دیتے ہیں۔",
                ar = "شرح العلماء: بيّن الخطابي والنووي وابن حجر أن هذا من بركة النبي صلى الله عليه وسلم ودعائه لمدينة النبوة، ويجري الأمر بإذن الله وقدره.",
                hi = "उलमा की व्याख्या: इमाम नववी और इब्न हजर इसे मदीना की अजवा खजूर पर नबवी दुआ और अल्लाह के हुक्म से विशेष बरकत बताते हैं।"
            ),
            establishedCountNote = LocalizedText4(
                en = "Explicitly proven count in Sahih al-Bukhari 5445: Seven (7) Ajwah dates in the morning.",
                ur = "صحیح بخاری (5445) سے صراحتاً ثابت تعداد: صبح 7 عجوہ کھجوریں۔",
                ar = "العدد الثابت بنص الحديث: سبع تمرات عجوة صباحاً (البخاري ٥٤٤٥).",
                hi = "सहीह बुख़ारी (5445) से प्रमाणित संख्या: सुबह 7 अजवा खजूर।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5445",
                bookAndNumber = "Sahih al-Bukhari 5445",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5445"
            ),
            searchKeywords = listOf("ajwah", "dates", "sihr", "poison", "tib", "عجوہ", "سحر", "अजवा", "सिह्र"),
            supportsQuranAudio = false
        ),

        // =========================================================================
        // D. SIHR / KALA ILM — السحر (Guidance & Sunnah Prevention)
        // =========================================================================
        ShifaRuqyahItem(
            id = "sihr_guide_no_accusations",
            category = ShifaCategory.SIHR_PROTECTION,
            type = ShifaItemType.GUIDANCE_NOTE,
            topicTag = LocalizedText4(
                en = "Aqeedah & Islamic Safeguards",
                ur = "شرعی رہنمائی اور توہمات سے بچاؤ",
                ar = "ضوابط التعامل مع السحر",
                hi = "शराई सावधानी और अंधविश्वास से बचाव"
            ),
            title = LocalizedText4(
                en = "Islamic Etiquette Regarding Sihr — Zero Unfounded Accusations & Zero Occult Practices",
                ur = "سحر کے معاملے میں شرعی اصول — بلا ثبوت الزام و توہمات سے مکمل بچاؤ",
                ar = "منهج المسلم الصحيح في الوقاية من السحر",
                hi = "सिह्र के मामले में शराई नियम — बिना प्रमाण आरोप और तांत्रिकों से बचाव"
            ),
            arabicText = "مَنْ أَتَى عَرَّافًا أَوْ كَاهِنًا فَصَدَّقَهُ بِمَا يَقُولُ، فَقَدْ كَفَرَ بِمَا أُنْزِلَ عَلَى مُحَمَّدٍ",
            translation = LocalizedText4(
                en = "Whoever goes to a fortune-teller or soothsayer and believes what he says has disbelieved in what was revealed to Muhammad ﷺ. (Authentic principle reported across Sunan / Ahmad from Abu Hurayrah)",
                ur = "جو شخص کسی کاہن یا جادوگر/نجومی کے پاس جائے اور اس کی بات کی تصدیق کرے، اس نے اس دین سے کفر کیا جو محمد ﷺ پر نازل ہوا۔",
                ar = "التحذير الشديد من إتيان السحرة والكهنة والعرّافين وتصديقهم.",
                hi = "जो किसी ज्योतिषी, काहिन या तांत्रिक के पास जाए और उसकी बात को सच माने, उसने उस पर कुफ़्र किया जो मुहम्मद ﷺ पर नाज़िल हुआ।"
            ),
            islamicContext = LocalizedText4(
                en = "• Never Diagnose Sihr From Hardships or Dreams: Ordinary life difficulties, family disputes, financial stress, bad dreams, or health struggles are NOT proof of black magic.\n• Never Accuse Relatives or Neighbors: Accusing another Muslim without clear evidence harms families and is severely sinful.\n• Valid Protection & Remedy Only: Regular Salah, daily morning/evening Adhkar, Surah Al-Baqarah (2:255, 2:285–286), Al-Falaq (113), An-Naas (114), and authentic Sunnah Ruqyah (Bukhari 5735, Muslim 2186).",
                ur = "• مشکلات یا خوابوں سے جادو کا فیصلہ نہ کریں: کاروباری تنگی، گھریلو ناچاقی، بیماری یا برے خواب سحر کا ثبوت نہیں ہوتے۔\n• کسی رشتہ دار یا پڑوسی پر الزام نہ لگائیں: محض شک یا کسی عامل کی بات پر کسی پر کالے علم کا الزام لگانا سخت گناہ اور قطع رحمی ہے۔\n• صرف مسنون علاج: پابندیِ نماز، صبح و شام کے اذکار، سورۃ البقرہ، آیۃ الکرسی، معوذات (113، 114) اور مسنون رقیہ (بخاری 5735، مسلم 2186)۔",
                ar = "• لا يُحكم بالسحر بمجرد الأحلام أو ضيق الرزق أو الخلافات.\n• يحرم اتهام الأقارب أو الناس بالسحر بالظن والوهم.\n• العلاج والوقاية محصوران في التوكل على الله والمحافظة على الصلوات وأذكار الصباح والمساء والرقية بالقرآن (الفاتحة والكرسي والبقرة والمعوذتين) والأدعية النبوية الصحيحة.",
                hi = "• परेशानियों या सपनों से सिह्र (जादू) का फ़ैसला न करें: आर्थिक तंगी, बीमारी या बुरे सपने जादू का प्रमाण नहीं होते।\n• किसी रिश्तेदार या पड़ोसी पर इल्ज़ाम न लगाएँ।\n• सहीह इलाज केवल अल्लाह पर तवक्कुल, नमाज़, सुबह-शाम के अज़कार, सूरह बक़रा, आयत-उल-कुर्सी और मुअव्विज़ात (113, 114) व मसनून रुक़या है (बुख़ारी 5735, मुस्लिम 2186)।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5735 (Sunnah Mu'awwidhat)",
                bookAndNumber = "Sahih al-Bukhari 5735",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5735"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih Muslim 2186", "Sahih Muslim 2186", "Sahih", "https://sunnah.com/muslim:2186"),
                ShifaReferenceSource("Quran.com — Surah Al-Falaq", "Quran 113:1–5", "Noble Quran", "https://quran.com/113")
            ),
            searchKeywords = listOf("sihr", "kala ilm", "magic", "witchcraft", "amils", "سحر", "کالا علم", "जादू", "सिह्र"),
            supportsQuranAudio = false
        ),

        // =========================================================================
        // E. NAZAR AUR HASAD — العين والحسد
        // =========================================================================
        ShifaRuqyahItem(
            id = "nazar_h_bukhari_5738",
            category = ShifaCategory.NAZAR_HASAD,
            type = ShifaItemType.HADITH_NARRATION,
            topicTag = LocalizedText4(
                en = "Reality of the Evil Eye",
                ur = "نظرِ بد کی حقیقت",
                ar = "إثبات حقيقة العين",
                hi = "नज़र-ए-बद की हक़ीक़त"
            ),
            title = LocalizedText4(
                en = "\"Al-'Aynu Haqq\" — The Evil Eye Is Real (Sahih al-Bukhari 5738)",
                ur = "نظرِ بد حق ہے — صحیح بخاری (5738)",
                ar = "الْعَيْنُ حَقٌّ — (صحيح البخاري ٥٧٣٨)",
                hi = "नज़र-ए-बद हक़ (सत्य) है — सहीह बुख़ारी (5738)"
            ),
            arabicText = "الْعَيْنُ حَقٌّ",
            translation = LocalizedText4(
                en = "The evil eye (Al-'Ayn) is real.",
                ur = "نظرِ بد کا لگ جانا حق (سچ) ہے۔",
                ar = "العين حق، يقع تأثيرها بإذن الله وقدره.",
                hi = "बुरी नज़र लगना हक़ (सच्चाई) है।"
            ),
            narrator = "Abu Hurayrah (RA) — Sahih al-Bukhari 5738",
            islamicContext = LocalizedText4(
                en = "Narrated by Abu Hurayrah (RA) in Sahih al-Bukhari (5738) alongside the prohibition of tattooing—affirming that the evil eye exists by Allah's decree while forbidding superstitious or unlawful remedies.",
                ur = "صحیح بخاری (5738) میں حضرت ابوہریرہؓ سے مروی ہے کہ نبی ﷺ نے فرمایا 'نظر حق ہے'—یعنی اللہ کے اذن سے نظرِ بد کا اثر واقعی ہوتا ہے، مگر اس کا علاج صرف مسنون رقیہ و دعا سے ہے، توہمات سے نہیں۔",
                ar = "روى البخاري (٥٧٣٨) عن أبي هريرة رضي الله عنه: «العين حق، ونهى عن الوشم».",
                hi = "सहीह बुख़ारी (5738) में हज़रत अबू हुरैरा (रज़ि.) से वर्णित है कि नबी ﷺ ने फ़रमाया 'नज़र हक़ है'—अर्थात अल्लाह के हुक्म से इसका असर होता है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 5738",
                bookAndNumber = "Sahih al-Bukhari 5738",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:5738"
            ),
            searchKeywords = listOf("nazar", "evil eye", "ayn", "hasad", "نظر", "حسد", "नज़र", "हसद"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "nazar_h_muslim_2187",
            category = ShifaCategory.NAZAR_HASAD,
            type = ShifaItemType.HADITH_NARRATION,
            topicTag = LocalizedText4(
                en = "Sunnah Method of Ightisal (Washing)",
                ur = "نظرِ بد کے لیے غسل/وضو (اغتسال) کا مسنون حکم",
                ar = "حديث العين حق والأمر بالاغتسال",
                hi = "नज़र उतारने का सुन्नत तरीक़ा (इग़तिसाल)"
            ),
            title = LocalizedText4(
                en = "Evil Eye, Qadar & Ightisal — Sahih Muslim 2187",
                ur = "نظر حق ہے اور اگر غسل/وضو کے پانی کا کہا جائے تو دے دو (صحیح مسلم 2187)",
                ar = "الْعَيْنُ حَقٌّ، وَلَوْ كَانَ شَيْءٌ سَابَقَ الْقَدَرَ سَبَقَتْهُ الْعَيْنُ، وَإِذَا اسْتُغْسِلْتُمْ فَاغْسِلُوا",
                hi = "नज़र हक़ है व इग़तिसाल का हुक्म — सहीह मुस्लिम 2187"
            ),
            arabicText = "الْعَيْنُ حَقٌّ، وَلَوْ كَانَ شَيْءٌ سَابَقَ الْقَدَرَ سَبَقَتْهُ الْعَيْنُ، وَإِذَا اسْتُغْسِلْتُمْ فَاغْسِلُوا",
            translation = LocalizedText4(
                en = "The evil eye is real; if anything were to overtake the Divine Decree, the evil eye would overtake it. And when you are asked to wash (for someone affected by your look), then wash.",
                ur = "نظرِ بد حق ہے؛ اگر کوئی چیز تقدیر سے آگے نکل سکتی تو نظرِ بد آگے نکل جاتی۔ اور جب تم سے (نظر لگ جانے پر) غسل/وضو کرنے کا کہا جائے تو دھو کر پانی دے دو۔",
                ar = "العين حق، ولو كان شيء سابق القدر سبقته العين، وإذا طُلب من أحدكم أن يغسل للرَّقي بالماء فليستجب ولا يمتنع.",
                hi = "बुरी नज़र हक़ है; यदि कोई चीज़ तक़दीर से आगे बढ़ सकती तो नज़र आगे बढ़ जाती। और जब तुमसे (नज़र लगने पर) वज़ू/ग़ुस्ल करके पानी देने को कहा जाए, तो धोकर दे दो।"
            ),
            narrator = "Ibn Abbas (RA) — Sahih Muslim 2187",
            islamicContext = LocalizedText4(
                en = "Teaches humility and communal care: (1) Always say 'Allahumma barik' (Tabrik / Ma sha Allah la quwwata illa billah) when admiring blessings so one's gaze never harms others; (2) If known who looked without Tabrik, the Sunnah washing (Ightisal) is poured over the affected person; (3) Never engage in chili/salt/shoe superstitions or false accusations.",
                ur = "اس حدیث سے اہم اصول ملتے ہیں: (1) جب کوئی اچھی چیز دیکھیں تو فوراً «بَارَكَ اللَّهُ فِيهِ / اللَّهُمَّ بَارِكْ» کہیں تاکہ اپنی نظر نہ لگے؛ (2) اگر کسی کی نظر لگنے کا یقین ہو تو وہ اخلاص سے اپنے اعضا دھو کر پانی دے دے؛ (3) مرچیں جلانا، جوتے اتارنا یا کالے دھاگے باندھنا سنت نہیں۔",
                ar = "يُستفاد من الحديث التبريك عند رؤية ما يعجب، والاستجابة للاغتسال المأذون به عند الحاجة، واجتناب الخرافات والتهم الباطلة.",
                hi = "(1) कुछ पसंद आए तो तुरंत 'अल्लाहुम्मा बारिक' कहें। (2) यदि किसी की नज़र लगना ज्ञात हो तो मसनून तरीक़े से धोया हुआ पानी दें। (3) मिर्च जलाना, काला धागा या तावीज़ बांधना सुन्नत नहीं है।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 2187",
                bookAndNumber = "Sahih Muslim 2187",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:2187"
            ),
            searchKeywords = listOf("nazar", "hasad", "ightisal", "tabrik", "barakallah", "نظر", "حسد", "नज़र"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "nazar_h_muslim_2195a_2196b",
            category = ShifaCategory.NAZAR_HASAD,
            type = ShifaItemType.HADITH_NARRATION,
            topicTag = LocalizedText4(
                en = "Prophetic Approval of Ruqyah for Evil Eye",
                ur = "نظرِ بد کے لیے رقیہ (دم) کرنے کا نبوی حکم",
                ar = "الأمر بالاسترقاء من العين",
                hi = "नज़र के लिए रुक़या करने का नबवी आदेश"
            ),
            title = LocalizedText4(
                en = "Seeking Ruqyah From the Evil Eye (Sahih Muslim 2195a & 2196b)",
                ur = "نظرِ بد لگنے پر رقیہ (دم) کروانا — صحیح مسلم 2195a اور 2196b",
                ar = "اسْتَرْقُوا لَهَا فَإِنَّ بِهَا النَّظْرَةَ — (صحيح مسلم ٢١٩٥أ و ٢١٩٦ب)",
                hi = "नज़र-ए-बद पर रुक़या करना — सहीह मुस्लिम 2195a व 2196b"
            ),
            arabicText = "اسْتَرْقُوا لَهَا، فَإِنَّ بِهَا النَّظْرَةَ",
            translation = LocalizedText4(
                en = "Seek Ruqyah for her, for she has been affected by an evil eye. (Sahih Muslim 2196b) — And Aishah (RA) reported: The Messenger of Allah ﷺ commanded me to seek Ruqyah from the evil eye. (Sahih Muslim 2195a)",
                ur = "اس بچی پر دم (رقیہ) کرواؤ کیونکہ اسے نظرِ بد لگی ہوئی ہے (صحیح مسلم 2196b)۔ اور حضرت عائشہؓ فرماتی ہیں کہ رسول اللہ ﷺ نے مجھے نظرِ بد سے دم کروانے کا حکم دیا (صحیح مسلم 2195a)۔",
                ar = "قال صلى الله عليه وسلم لجارية في بيت أم سلمة: «استرقوا لها فإن بها النظرة» (مسلم ٢١٩٦ب)، وعن عائشة: «أمرني أن أسترقي من العين» (مسلم ٢١٩٥أ).",
                hi = "इस पर दम (रुक़या) कराओ क्योंकि इसे बुरी नज़र लगी है (सहीह मुस्लिम 2196b)। और हज़रत आइशा (रज़ि.) ने फ़रमाया कि नबी ﷺ ने मुझे बुरी नज़र से रुक़या कराने का हुक्म दिया (सहीह मुस्लिम 2195a)।"
            ),
            narrator = "Aishah (RA) & Umm Salamah (RA) — Sahih Muslim 2195a, 2196b",
            islamicContext = LocalizedText4(
                en = "Establishes explicitly that the legitimate treatment for Nazar is Quranic & Sunnah Ruqyah (such as Al-Fatihah, Ayat-ul-Kursi, Al-Falaq, An-Naas, and the Prophetic Duas in Category B).",
                ur = "ان دونوں احادیث سے ثابت ہوتا ہے کہ نظرِ بد کا شرعی علاج قرآن و سنت کا رقیہ ہے، نہ کہ غیر شرعی ٹوٹکے یا تعویذات۔",
                ar = "تُثبت الروايتان أن الرقية الشرعية هي العلاج المأذون به للعين والنظرة.",
                hi = "इन दोनों हदीसों से साबित होता है कि बुरी नज़र का शराई इलाज क़ुरआनी और मसनून रुक़या है, न कि टोटके।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 2195a",
                bookAndNumber = "Sahih Muslim 2195a",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:2195a"
            ),
            additionalReferences = listOf(
                ShifaReferenceSource("Sahih Muslim 2196b", "Sahih Muslim 2196b", "Sahih", "https://sunnah.com/muslim:2196b")
            ),
            searchKeywords = listOf("nazar", "hasad", "nazrah", "evil eye", "ruqyah", "نظر", "حسد", "नज़र", "हसद"),
            supportsQuranAudio = false
        ),

        // =========================================================================
        // F. WASWASE — الوسوسة (Reassurance & Prophetic Remedy)
        // =========================================================================
        ShifaRuqyahItem(
            id = "waswasa_h_muslim_132a",
            category = ShifaCategory.WASWASE,
            type = ShifaItemType.HADITH_NARRATION,
            topicTag = LocalizedText4(
                en = "Heart & Faith Whispers (Reassurance)",
                ur = "ایمانی وسوسوں پر نبوی تسلی",
                ar = "وساوس الإيمان وبشارة الصحيحين",
                hi = "ईमान के वसवसों पर नबवी तसल्ली"
            ),
            title = LocalizedText4(
                en = "Unwanted Intrusive Thoughts Are NOT Sin — \"That Is Clear Faith\" (Muslim 132a)",
                ur = "ناپسندیدہ وسوسوں کو بُرا سمجھنا صریح ایمان کی علامت ہے (صحیح مسلم 132a)",
                ar = "ذَاكَ صَرِيحُ الإِيمَانِ — (صحيح مسلم ١٣٢أ)",
                hi = "अनचाहे वसवसों से नफ़रत करना सच्चे ईमान की निशानी है (मुस्लिम 132a)"
            ),
            arabicText = "جَاءَ نَاسٌ مِنْ أَصْحَابِ النَّبِيِّ ﷺ فَسَأَلُوهُ: إِنَّا نَجِدُ فِي أَنْفُسِنَا مَا يَتَعَاظَمُ أَحَدُنَا أَنْ يَتَكَلَّمَ بِهِ، قَالَ: «وَقَدْ وَجَدْتُمُوهُ؟» قَالُوا: نَعَمْ، قَالَ: «ذَاكَ صَرِيحُ الإِيمَانِ»",
            translation = LocalizedText4(
                en = "Some Companions came to the Prophet ﷺ and asked: 'We find in our hearts thoughts that any one of us considers too terrible to speak aloud.' He asked: 'Have you really found that (and hated it)?' They said: 'Yes.' He said: 'That is clear, pure faith.'",
                ur = "کچھ صحابہؓ نے نبی کریم ﷺ کی خدمت میں عرض کیا کہ ہمارے دلوں میں ایسے خیالات آتے ہیں جنہیں زبان پر لانا بھی ہمیں بہت بڑا گناہ لگتا ہے۔ آپ ﷺ نے فرمایا: 'کیا واقعی تم ایسا محسوس کرتے ہو؟' انہوں نے عرض کیا: 'جی ہاں۔' آپ ﷺ نے فرمایا: 'یہ تو صریح (خالص) ایمان کی علامت ہے!'",
                ar = "شكا الصحابة خواطر يكرهون النطق بها لعظمها، فبشّرهم النبي صلى الله عليه وسلم بأن كراهتها ودفعها هو «صريح الإيمان».",
                hi = "कुछ सहाबा (रज़ि.) ने अर्ज किया कि हमारे मन में ऐसे बुरे ख़्याल आते हैं जिन्हें मुँह से निकालना भी बहुत भारी लगता है। नबी ﷺ ने पूछा: 'क्या तुम उनसे घृणा करते हो?' उन्होंने कहा: 'हाँ।' फ़रमाया: 'यह तो स्पष्ट ईमान की निशानी है।'"
            ),
            narrator = "Abu Hurayrah (RA) — Sahih Muslim 132a",
            islamicContext = LocalizedText4(
                en = "• Essential Reassurance: Merely having unwanted intrusive doubts or thoughts about Allah or religion does NOT make a believer sinful, sinful in heart, or out of Islam.\n• Why It Is 'Clear Faith': Imam an-Nawawi explains that feeling distress and rejecting the whisper proves true faith resides firmly in the heart.\n• Do NOT self-diagnose as Sihr or Jinn possession; calmly ignore the whisper and move on.",
                ur = "• اہم اطمینان: دل میں غیر اختیاری ناپسندیدہ وسوسے آ جانے سے انسان گناہ گار نہیں ہوتا۔ ان خیالات پر گھبراہٹ اور نفرت کا ہونا اس بات کی دلیل ہے کہ دل میں ایمان زندہ ہے۔\n• ایسے وسوسوں کو ہرگز جنات یا جادو کا نام نہ دیں؛ ان پر دھیان نہ دیں اور توجہ ہٹا دیں۔",
                ar = "• طمأنينة عظيمة: مجرد خطور الوسوسة التي يكرهها المؤمن لا يضر إيمانه ولا يُعد إثماً ولا دليلاً على مس أو سحر.\n• قال النووي: استعظامكم الكلام به هو صريح الإيمان لأنه لا يستعظمه إلا من رسخ الإيمان في قلبه.",
                hi = "• महत्वपूर्ण दिलासा: अनचाहे बुरे ख़्यालों का आना गुनाह नहीं है; उनसे नफ़रत होना ही इस बात का सबूत है कि दिल में ईमान मज़बूत है। इसे जिन्न या जादू न समझें।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 132a",
                bookAndNumber = "Sahih Muslim 132a",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:132a"
            ),
            searchKeywords = listOf("waswase", "waswasa", "iman", "doubts", "intrusive thoughts", "وسوسے", "ایمان", "वसवसे"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "waswasa_h_muslim_134a",
            category = ShifaCategory.WASWASE,
            type = ShifaItemType.HADITH_NARRATION,
            topicTag = LocalizedText4(
                en = "Practical Sunnah Step: Seek Refuge & Stop",
                ur = "وسوسہ روکنے کا دو لفظی نبوی علاج",
                ar = "الاستعاذة والانتهاء عن الوسوسة",
                hi = "वसवसा रोकने का मसनून उपाय"
            ),
            title = LocalizedText4(
                en = "Seek Refuge in Allah & Disengage (Sahih Muslim 134a)",
                ur = "اللہ کی پناہ مانگو اور سوچنا بند کر دو (صحیح مسلم 134a)",
                ar = "فَلْيَسْتَعِذْ بِاللَّهِ وَلْيَنْتَهِ — (صحيح مسلم ١٣٤أ)",
                hi = "अल्लाह की शरण माँगो और बहस रोक दो (सहीह मुस्लिम 134a)"
            ),
            arabicText = "يَأْتِي الشَّيْطَانُ أَحَدَكُمْ فَيَقُولُ: مَنْ خَلَقَ كَذَا؟ مَنْ خَلَقَ كَذَا؟ حَتَّى يَقُولَ: مَنْ خَلَقَ رَبَّكَ؟ فَإِذَا بَلَغَهُ فَلْيَسْتَعِذْ بِاللَّهِ وَلْيَنْتَهِ",
            translation = LocalizedText4(
                en = "Shaytan comes to one of you and says: 'Who created this? Who created that?' until he says: 'Who created your Lord?' So when it reaches that point, let him seek refuge in Allah (say A'udhu billah) and stop (thinking about it).",
                ur = "شیطان تم میں سے کسی کے پاس آتا ہے اور کہتا ہے: 'فلاں چیز کو کس نے پیدا کیا؟ فلاں کو کس نے پیدا کیا؟' یہاں تک کہ کہتا ہے: 'تمہارے رب کو کس نے پیدا کیا؟' پس جب بات یہاں تک پہنچے تو وہ اللہ کی پناہ مانگے (أعوذ بالله پڑھے) اور اس خیال سے باز آ جائے (سوچنا بند کر دے)۔",
                ar = "إذا ألقى الشيطان الشك والوسوسة في أصل الإيمان، فالعلاج النبوي الحاسم: «فليستعذ بالله ولينته» أي يكفّ عن مجاراة الخاطر.",
                hi = "शैतान तुम्हारे पास आकर पूछता है 'यह किसने बनाया? वह किसने बनाया?' यहाँ तक कि कहता है 'तुम्हारे रब को किसने बनाया?' जब बात यहाँ तक पहुँचे तो अल्लाह की शरण माँगें (अऊज़ु बिल्लाह) और इस विचार को तुरंत रोक दें।"
            ),
            narrator = "Abu Hurayrah (RA) — Sahih Muslim 134a",
            islamicContext = LocalizedText4(
                en = "Two Prophetic steps: (1) Say 'A'udhu billahi minash-shaytanir-rajim' and affirm 'Amantu billahi wa rusulihi' (reported in adjacent narrations); (2) 'Wal-yantahi'—do NOT argue with or analyze the whisper.",
                ur = "دو عملی قدم: (1) فوراً «أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ» اور «آمَنْتُ بِاللَّهِ وَرُسُلِهِ» کہیں؛ (2) وسوسے سے بحث نہ کریں بلکہ توجہ فوراً کسی نفع بخش کام یا ذکر کی طرف موڑ دیں۔",
                ar = "خطوتان نبويتان: الاستعاذة بالله ثم الإعراض التام وعدم الاسترسال في مناقشة الوسوسة.",
                hi = "दो मसनून क़दम: (1) तुरंत 'अऊज़ु बिल्लाहि मिनश-शैतानिर-रजीम' पढ़ें; (2) विचार से बहस या विश्लेषण करना बंद कर दें।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 134a",
                bookAndNumber = "Sahih Muslim 134a",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:134a"
            ),
            searchKeywords = listOf("waswase", "waswasa", "thoughts", "faith", "aqeedah", "وسوسے", "वसवसे"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "waswasa_h_muslim_2203a",
            category = ShifaCategory.WASWASE,
            type = ShifaItemType.HADITH_NARRATION,
            topicTag = LocalizedText4(
                en = "Whispers During Salah (Khanzab)",
                ur = "نماز میں وسوسوں کا مسنون علاج",
                ar = "دفع الوسوسة في الصلاة",
                hi = "नमाज़ में वसवसे का इलाज"
            ),
            title = LocalizedText4(
                en = "Overcoming Distraction in Salah — Sahih Muslim 2203a",
                ur = "نماز میں دھیان بٹنے اور وسوسے کا نبوی علاج (صحیح مسلم 2203a)",
                ar = "تَعَوَّذْ بِاللَّهِ مِنْهُ وَاتْفُلْ عَلَى يَسَارِكَ ثَلَاثًا — (صحيح مسلم ٢٢٠٣أ)",
                hi = "नमाज़ में वसवसे का नबवी उपाय (सहीह मुस्लिम 2203a)"
            ),
            arabicText = "ذَاكَ شَيْطَانٌ يُقَالُ لَهُ خَنْزَبٌ، فَإِذَا أَحْسَسْتَهُ فَتَعَوَّذْ بِاللَّهِ مِنْهُ، وَاتْفُلْ عَلَى يَسَارِكَ ثَلَاثًا",
            translation = LocalizedText4(
                en = "When Uthman ibn Abi al-As complained that Shaytan interfered between him and his prayer/recitation, the Prophet ﷺ said: 'That is a devil called Khanzab. So when you feel him, seek refuge in Allah from him and dry-spit lightly to your left three times.'",
                ur = "حضرت عثمان بن ابی العاصؓ نے عرض کیا کہ شیطان میری نماز اور قراءت میں خلل ڈالتا ہے۔ آپ ﷺ نے فرمایا: 'یہ خنزب نامی شیطان ہے؛ جب تم اسے محسوس کرو تو اللہ کی پناہ مانگو اور اپنی بائیں جانب تین بار ہلکی تھتکار (پھونک) کر دو۔'",
                ar = "شكا عثمان بن أبي العاص أن الشيطان حال بينه وبين صلاته، فأرشده النبي ﷺ أن يستعيذ بالله منه ويتفل عن يساره ثلاثاً.",
                hi = "जब उसमान बिन अबिल-आस ने नमाज़ में ध्यान भटकने की शिकायत की, तो नबी ﷺ ने फ़रमाया: 'जब ऐसा महसूस हो तो अल्लाह की शरण माँगो और अपनी बाईं ओर तीन बार हल्की थू-थू (फूँक) करो।'"
            ),
            narrator = "Uthman ibn Abi al-As (RA) — Sahih Muslim 2203a",
            islamicContext = LocalizedText4(
                en = "Uthman (RA) said: 'I did that, and Allah removed it from me.' Note that inside congregational prayer one turns the head slightly/subtly without disturbing neighbors or breaks no prayer.",
                ur = "حضرت عثمانؓ فرماتے ہیں کہ میں نے اس پر عمل کیا تو اللہ نے مجھ سے وہ وسوسہ دور فرما دیا۔ (باجماعت نماز میں ساتھ والے نمازی کو تکلیف دیے بغیر ہلکا سا اشارہ/پھونک کافی ہے)۔",
                ar = "قال عثمان رضي الله عنه: «ففعلت ذلك فأذهبه الله عني».",
                hi = "हज़रत उसमान (रज़ि.) ने कहा कि मैंने ऐसा किया तो अल्लाह ने मुझसे वह वसवसा दूर कर दिया।"
            ),
            establishedCountNote = LocalizedText4(
                en = "Explicitly proven count in Sahih Muslim 2203a: Seek refuge in Allah + lightly dry-blow to the left three (3) times.",
                ur = "صحیح مسلم (2203a) سے ثابت تعداد: استعاذہ کے ساتھ بائیں طرف 3 بار ہلکی پھونک۔",
                ar = "العدد الثابت: ثلاث مرات على اليسار مع الاستعاذة (مسلم ٢٢٠٣أ).",
                hi = "सहीह मुस्लिम (2203a) से प्रमाणित संख्या: अल्लाह की पनाह माँगते हुए बाईं ओर 3 बार हल्की फूँक।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 2203a",
                bookAndNumber = "Sahih Muslim 2203a",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:2203a"
            ),
            searchKeywords = listOf("waswase", "salah", "namaz", "khanzab", "distraction", "نماز", "وسوسے", "नमाज़", "वसवसे"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "waswasa_h_bukhari_137",
            category = ShifaCategory.WASWASE,
            type = ShifaItemType.HADITH_NARRATION,
            topicTag = LocalizedText4(
                en = "Doubts in Wudu & Certainty Principle",
                ur = "وضو اور طہارت کے شک کا علاج",
                ar = "اليقين لا يزول بالشك في الوضوء",
                hi = "वज़ू और पाकी के संदेह का इलाज"
            ),
            title = LocalizedText4(
                en = "Do Not Break Prayer for Mere Doubt — Sahih al-Bukhari 137",
                ur = "محض شک و وسوسے سے وضو یا نماز نہ توڑو (صحیح بخاری 137)",
                ar = "لاَ يَنْصَرِفْ حَتَّى يَسْمَعَ صَوْتًا أَوْ يَجِدَ رِيحًا — (صحيح البخاري ١٣٧)",
                hi = "केवल संदेह से वज़ू या नमाज़ न तोड़ें (सहीह बुख़ारी 137)"
            ),
            arabicText = "لَا يَنْفَتِلْ — أَوْ لَا يَنْصَرِفْ — حَتَّى يَسْمَعَ صَوْتًا أَوْ يَجِدَ رِيحًا",
            translation = LocalizedText4(
                en = "When a man complained to the Prophet ﷺ about imagining something happened during prayer, he said: 'Let him not leave (the prayer) unless he hears a sound or detects an odor.'",
                ur = "ایک شخص نے نبی کریم ﷺ سے عرض کیا کہ مجھے نماز میں وضو ٹوٹ جانے کا گمان ہوتا ہے۔ آپ ﷺ نے فرمایا: 'وہ نماز سے ہرگز نہ پلٹے جب تک آواز نہ سن لے یا بدبو محسوس نہ کر لے۔'",
                ar = "شُكي إلى النبي صلى الله عليه وسلم الرجل يخيل إليه أنه يجد الشيء في الصلاة، فقال: «لا ينفتل حتى يسمع صوتاً أو يجد ريحاً».",
                hi = "जब किसी ने शिकायत की कि नमाज़ में उसे वज़ू टूटने का वहम होता है, तो नबी ﷺ ने फ़रमाया: 'वह नमाज़ से न लौटे जब तक कि आवाज़ न सुने या गंध न पाए।'"
            ),
            narrator = "Abbad ibn Tamim (from his uncle) — Sahih al-Bukhari 137",
            islamicContext = LocalizedText4(
                en = "Establishes the foundational Fiqh & spiritual cure for Wudu/Salah OCD-like doubts: certainty is never overridden by suspicion. Repeating Wudu endlessly only fuels Waswasah.",
                ur = "فقہِ اسلامی کا بنیادی اصول: 'الیقین لا یزول بالشک' — یقینی وضو صرف وہم سے نہیں ٹوٹتا۔ بار بار وضو یا نیت دہرانے سے وسوسہ بڑھتا ہے؛ سنت پر عمل کرتے ہوئے اسے نظر انداز کریں۔",
                ar = "أصل شرعي عظيم لعلاج وساوس الطهارة والوضوء: «اليقين لا يزول بالشك»، فلا يعاد الوضوء ولا الصلاة بالوهم.",
                hi = "यह फ़िक़्ह का महान नियम स्थापित करता है कि केवल संदेह से निश्चित वज़ू नहीं टूटता। बार-बार वज़ू या नियत दोहराना छोड़कर विश्वास पर टिकें।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 137",
                bookAndNumber = "Sahih al-Bukhari 137",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:137"
            ),
            searchKeywords = listOf("wudu", "waswase", "ibadat", "salah", "doubt", "وضو", "وسوسے", "वज़ू", "वसवसे"),
            supportsQuranAudio = false
        ),

        // =========================================================================
        // G. SHIFA-E-QALB AUR SUKOON — شفاء القلب والسكينة (Hadith)
        // =========================================================================
        ShifaRuqyahItem(
            id = "qalb_h_bukhari_6369",
            category = ShifaCategory.SHIFA_E_QALB,
            type = ShifaItemType.SUNNAH_DUA,
            topicTag = LocalizedText4(
                en = "Prophetic Dua for Worry & Grief",
                ur = "فکر اور غم سے پناہ کی نبوی دعا",
                ar = "دعاء الهم والحزن",
                hi = "चिंता व ग़म से पनाह की नबवी दुआ"
            ),
            title = LocalizedText4(
                en = "Allahumma Inni A'udhu Bika Minal-Hammi wal-Hazan (Bukhari 6369)",
                ur = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ — غم و فکر سے حفاظت کی دعا",
                ar = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ",
                hi = "अल्लाहुम्मा इन्नी अऊज़ु बिका मिनल-हम्मि वल-हज़न (बुख़ारी 6369)"
            ),
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْجُبْنِ وَالْبُخْلِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ",
            translation = LocalizedText4(
                en = "O Allah, I seek refuge in You from worry and grief, from incapacity and laziness, from cowardice and stinginess, and from the burden of debt and being overpowered by people.",
                ur = "اے اللہ! میں تیری پناہ مانگتا ہوں فکر اور غم سے، عاجزی اور سستی سے، بزدلی اور بخل سے، اور قرض کے بوجھ اور لوگوں کے غلبے سے۔",
                ar = "اللهم إني أعوذ بك من الهم والحزن، والعجز والكسل، والجبن والبخل، وغلبة الدين وقهر الرجال.",
                hi = "ऐ अल्लाह! मैं तेरी पनाह माँगता हूँ चिंता और ग़म से, असमर्थता और आलस्य से, कायरता और कंजूसी से, और क़र्ज़ के बोझ तथा लोगों के दबाव से।"
            ),
            narrator = "Anas ibn Malik (RA) — Sahih al-Bukhari 6369",
            islamicContext = LocalizedText4(
                en = "Anas (RA) reported that the Messenger of Allah ﷺ recited this supplication frequently, seeking Allah's relief from sorrow about the past (al-hazan) and anxiety about the future (al-hamm).",
                ur = "حضرت انسؓ فرماتے ہیں کہ نبی کریم ﷺ کثرت سے یہ دعا پڑھا کرتے تھے، جس میں مستقبل کی فکر (ھمّ) اور ماضی کے غم (حزن) دونوں سے پناہ مانگی گئی ہے۔",
                ar = "كان النبي صلى الله عليه وسلم يكثر من هذا الدعاء، والهمّ ما يُتوقع من المكروه، والحزن على ما مضى وفات.",
                hi = "हज़रत अनस (रज़ि.) कहते हैं कि नबी ﷺ अक्सर यह दुआ पढ़ा करते थे।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 6369",
                bookAndNumber = "Sahih al-Bukhari 6369",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:6369"
            ),
            searchKeywords = listOf("dil ka gham", "worry", "grief", "anxiety", "hamm", "sukoon", "غم", "فکر", "ग़म", "चिंता"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "qalb_h_bukhari_6346",
            category = ShifaCategory.SHIFA_E_QALB,
            type = ShifaItemType.SUNNAH_DUA,
            topicTag = LocalizedText4(
                en = "Supplication in Distress (Du'a al-Karb)",
                ur = "سخت بے چینی اور کرب کے وقت کی دعا",
                ar = "دعاء الكرب والشدة",
                hi = "कठिन संकट व बेचैनी में दुआ"
            ),
            title = LocalizedText4(
                en = "La Ilaha Illallahu Al-Azimul-Halim (Bukhari 6346)",
                ur = "سخت گھٹن اور پریشانی کے وقت کی نبوی دعا (صحیح بخاری 6346)",
                ar = "لاَ إِلَهَ إِلاَّ اللَّهُ الْعَظِيمُ الْحَلِيمُ — (صحيح البخاري ٦٣٤٦)",
                hi = "गहरी बेचैनी (कर्ब) के समय नबवी दुआ (सहीह बुख़ारी 6346)"
            ),
            arabicText = "لَا إِلَهَ إِلَّا اللَّهُ الْعَظِيمُ الْحَلِيمُ، لَا إِلَهَ إِلَّا اللَّهُ رَبُّ الْعَرْشِ الْعَظِيمِ، لَا إِلَهَ إِلَّا اللَّهُ رَبُّ السَّمَاوَاتِ وَرَبُّ الْأَرْضِ وَرَبُّ الْعَرْشِ الْكَرِيمِ",
            translation = LocalizedText4(
                en = "There is no god worthy of worship except Allah, the All-Mighty, the Forbearing; none has the right to be worshipped except Allah, Lord of the Mighty Throne; none has the right to be worshipped except Allah, Lord of the heavens, Lord of the earth, and Lord of the Noble Throne.",
                ur = "اللہ کے سوا کوئی معبود نہیں جو نہایت عظمت والا اور بردبار ہے؛ اللہ کے سوا کوئی معبود نہیں جو عرشِ عظیم کا رب ہے؛ اللہ کے سوا کوئی معبود نہیں جو آسمانوں کا رب، زمین کا رب اور عرشِ کریم کا رب ہے۔",
                ar = "لا إله إلا الله العظيم الحليم، لا إله إلا الله رب العرش العظيم، لا إله إلا الله رب السماوات ورب الأرض ورب العرش الكريم.",
                hi = "अल्लाह के सिवा कोई इबादत के योग्य नहीं जो महान व सहनशील है; अल्लाह के सिवा कोई पूज्य नहीं जो विशाल अर्श का रब है; अल्लाह के सिवा कोई पूज्य नहीं जो आकाशों, धरती और सम्माननीय अर्श का रब है।"
            ),
            narrator = "Ibn Abbas (RA) — Sahih al-Bukhari 6346",
            islamicContext = LocalizedText4(
                en = "Narrated by Ibn Abbas (RA) that the Prophet ﷺ would recite these words of pure Tawhid at times of severe grief and constriction (karb), grounding the heart in Allah's greatness.",
                ur = "نبی کریم ﷺ سخت بے چینی، گھٹن اور مصیبت کے وقت توحید و عظمت کے ان الفاظ سے رب کو پکارا کرتے تھے۔",
                ar = "كان النبي صلى الله عليه وسلم يدعو بهذا الدعاء العظيم عند الشدة والكرب.",
                hi = "कठिन संकट व बेचैनी के समय नबी ﷺ तौहीद के इन महान शब्दों को पढ़ा करते थे।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih al-Bukhari 6346",
                bookAndNumber = "Sahih al-Bukhari 6346",
                authenticityGrade = "Sahih (Verified in Bukhari)",
                url = "https://sunnah.com/bukhari:6346"
            ),
            searchKeywords = listOf("karb", "distress", "dil ka gham", "fear", "anxiety", "غم", "گھٹن", "सुकून"),
            supportsQuranAudio = false
        ),
        ShifaRuqyahItem(
            id = "qalb_h_muslim_2699",
            category = ShifaCategory.SHIFA_E_QALB,
            type = ShifaItemType.HADITH_NARRATION,
            topicTag = LocalizedText4(
                en = "How Sakinah (Tranquility) Descends",
                ur = "سکینت اور رحمت نازل ہونے کا ذریعہ",
                ar = "نزول السكينة والرحمة بتلاوة القرآن",
                hi = "सुकून (सकीना) नाज़िल होने का माध्यम"
            ),
            title = LocalizedText4(
                en = "Tranquility Descends Upon Those Who Recite & Study the Quran (Muslim 2699)",
                ur = "تلاوت و درسِ قرآن سے دلوں پر سکینت اور رحمت کا نزول (صحیح مسلم 2699)",
                ar = "إِلَّا نَزَلَتْ عَلَيْهِمُ السَّكِينَةُ وَغَشِيَتْهُمُ الرَّحْمَةُ — (صحيح مسلم ٢٦٩٩)",
                hi = "क़ुरआन की तिलावत से दिलों पर सुकून और रहमत का उतरना (मुस्लिम 2699)"
            ),
            arabicText = "وَمَا اجْتَمَعَ قَوْمٌ فِي بَيْتٍ مِنْ بُيُوتِ اللَّهِ، يَتْلُونَ كِتَابَ اللَّهِ، وَيَتَدَارَسُونَهُ بَيْنَهُمْ، إِلَّا نَزَلَتْ عَلَيْهِمُ السَّكِينَةُ، وَغَشِيَتْهُمُ الرَّحْمَةُ، وَحَفَّتْهُمُ الْمَلَائِكَةُ، وَذَكَرَهُمُ اللَّهُ فِيمَنْ عِنْدَهُ",
            translation = LocalizedText4(
                en = "No people gather in a house among the houses of Allah reciting the Book of Allah and studying it together except that tranquility (Sakinah) descends upon them, mercy envelops them, the angels surround them, and Allah mentions them among those in His presence.",
                ur = "جب بھی کچھ لوگ اللہ کے گھروں میں سے کسی گھر میں جمع ہو کر کتاب اللہ کی تلاوت کرتے اور آپس میں اس کا درس دیتے ہیں، تو ان پر سکینت نازل ہوتی ہے، رحمت انہیں ڈھانپ لیتی ہے، فرشتے انہیں گھیر لیتے ہیں اور اللہ اپنے مقرب فرشتوں میں ان کا تذکرہ فرماتا ہے۔",
                ar = "بشرى عظيمة أن تلاوة القرآن وتدارسه يجلبان السكينة والرحمة وحفظ الملائكة.",
                hi = "जब लोग अल्लाह के घरों में से किसी घर में अल्लाह की किताब पढ़ते और सीखते हैं, तो उन पर सकीना (शांति) उतरती है, रहमत उन्हें ढँक लेती है और फ़रिश्ते उन्हें घेर लेते हैं।"
            ),
            narrator = "Abu Hurayrah (RA) — Sahih Muslim 2699",
            islamicContext = LocalizedText4(
                en = "Reminds grieving or anxious hearts that regular Quran recitation, Zikr, and gathering with righteous believers invite Sakinah from Allah.",
                ur = "دل کے اضطراب اور تنہائی کا مسنون علاج: مسجد سے تعلق، تلاوتِ قرآن، ذکرِ الٰہی اور نیک ماحول میں بیٹھنا۔",
                ar = "توجيه عملي للقلب المضطرب بالارتباط بالقرآن والمساجد ومجالس الذكر.",
                hi = "दिल की बेचैनी का मसनून हल: क़ुरआन की तिलावत, अल्लाह का ज़िक्र और नेक मजलिसों से जुड़ना।"
            ),
            primaryReference = ShifaReferenceSource(
                label = "Sahih Muslim 2699",
                bookAndNumber = "Sahih Muslim 2699",
                authenticityGrade = "Sahih (Verified in Muslim)",
                url = "https://sunnah.com/muslim:2699"
            ),
            searchKeywords = listOf("sakinah", "sukoon", "peace", "quran", "dil ka gham", "سکون", "सुकून"),
            supportsQuranAudio = false
        )
    )

    // =============================================================================
    // H. ISLAMIC TREATMENT SEARCH INDEX — سرچ انڈیکس
    // =============================================================================
    val treatmentConditionTopics: List<TreatmentConditionTopic> = listOf(
        TreatmentConditionTopic(
            id = "cond_sar_dard",
            title = LocalizedText4(
                en = "Sar Dard (Headache)",
                ur = "سر درد",
                ar = "آلام الرأس / الصداع",
                hi = "सिर दर्द"
            ),
            matchedItemIds = listOf("ruqyah_q_fatihah", "ruqyah_dua_rabban_nas", "ruqyah_dua_pain_7x", "tib_hijamah"),
            scopeNote = LocalizedText4(
                en = "Verified Guidance: No specific Quranic verse singles out headaches exclusively. In authentic Sunnah, general Ruqyah (Al-Fatihah, Muslim 2191c, placing hand & saying Bismillah 3x / A'udhu 7x per Muslim 2202) and Al-Hijamah (Bukhari 5681) are established as lawful means, never a guaranteed substitute for medical care.",
                ur = "مستند وضاحت: قرآنِ کریم کی کوئی مخصوص آیت صرف سر درد کے لیے مقرر نہیں ہے۔ مسنون رہنمائی میں عمومی رقیہ (سورۃ الفاتحہ، مسلم 2191c، درد کی جگہ ہاتھ رکھ کر 3 بار بسم اللہ و 7 بار دعا مسلم 2202) اور حجامہ (بخاری 5681) ثابت ہیں، تاہم انہیں طبی معائنے کا متبادل یا حتمی دعویٰ قرار نہیں دیا جا سکتا۔",
                ar = "تنبيه موثق: لم يرد نص قرآني خاص بالصداع تحديداً، وإنما يُشرع فيه الرقية العامة (الفاتحة، الدعاء في مسلم ٢٢٠٢، ٢١٩١ج) والحجامة (البخاري ٥٦٨١) كأسباب مشروعة.",
                hi = "स्पष्टीकरण: किसी एक क़ुरआनी आयत में विशेष रूप से सिरदर्द का उल्लेख नहीं है। प्रामाणिक सुन्नत में सामान्य रुक़या (फ़ातिहा, मुस्लिम 2191c, दर्द पर हाथ रखकर मुस्लिम 2202) व हिजामा (बुख़ारी 5681) प्रमाणित हैं।"
            ),
            searchAliases = listOf("sar dard", "dard e sar", "headache", "head pain", "سر درد", "सिर दर्द", "صداع")
        ),
        TreatmentConditionTopic(
            id = "cond_bukhar",
            title = LocalizedText4(
                en = "Bukhar (Fever)",
                ur = "بخار",
                ar = "الحمى",
                hi = "बुख़ार"
            ),
            matchedItemIds = listOf("tib_fever_water", "ruqyah_dua_rabban_nas", "ruqyah_q_fatihah"),
            scopeNote = LocalizedText4(
                en = "Verified Proof: Sahih al-Bukhari 5723 explicitly directs cooling fever with water, alongside sincere Prophetic Ruqyah (Muslim 2191c) and Al-Fatihah (Bukhari 5736).",
                ur = "ثابت شدہ دلیل: صحیح بخاری (5723) میں بخار کو پانی سے ٹھنڈا کرنے کی واضح ہدایت موجود ہے، اور ساتھ مسنون دعائے شفا (مسلم 2191c) و سورۃ الفاتحہ پڑھنا مستند ہے۔",
                ar = "الدليل الصحيح: حديث البخاري (٥٧٢٣) في تبريد الحمى بالماء، مع الدعاء النبوي (مسلم ٢١٩١ج).",
                hi = "प्रमाणित हदीस: सहीह बुख़ारी (5723) में बुख़ार को पानी से ठंडा करने का स्पष्ट निर्देश है, साथ में मसनून दुआ (मुस्लिम 2191c) व फ़ातिहा पढ़ें।"
            ),
            searchAliases = listOf("bukhar", "fever", "humma", "hararat", "بخار", "बुख़ार", "حمى")
        ),
        TreatmentConditionTopic(
            id = "cond_jism_dard",
            title = LocalizedText4(
                en = "Jism ka Dard (Body Pain)",
                ur = "جسم کا درد",
                ar = "آلام الجسد",
                hi = "शरीर का दर्द"
            ),
            matchedItemIds = listOf("ruqyah_dua_pain_7x", "ruqyah_dua_rabban_nas", "ruqyah_q_fatihah"),
            scopeNote = LocalizedText4(
                en = "Verified Proof: Sahih Muslim 2202 explicitly teaches placing the right hand over the painful spot, saying Bismillah 3 times, and A'udhu billahi wa qudratihi... 7 times.",
                ur = "ثابت شدہ دلیل: صحیح مسلم (2202) میں درد والی جگہ ہاتھ رکھ کر 3 بار بسم اللہ اور 7 بار مسنون دعا پڑھنا صراحتاً ثابت ہے۔",
                ar = "الدليل الصحيح: وضع اليد على موضع الألم وقراءة الدعاء المأثور في صحيح مسلم (٢٢٠٢).",
                hi = "प्रमाणित हदीस: सहीह मुस्लिम (2202) में दर्द वाली जगह हाथ रखकर 3 बार बिस्मिल्लाह व 7 बार दुआ पढ़ना स्पष्ट रूप से साबित है।"
            ),
            searchAliases = listOf("jism ka dard", "body pain", "pain", "dard", "جسم کا درد", "درد", "शरीर का दर्द", "दर्द")
        ),
        TreatmentConditionTopic(
            id = "cond_neend",
            title = LocalizedText4(
                en = "Neend ki Pareshani (Sleep Distress)",
                ur = "نیند کی پریشانی",
                ar = "الأرق وفزع النوم",
                hi = "नींद की परेशानी"
            ),
            matchedItemIds = listOf("ruqyah_q_kursi", "ruqyah_q_baqarah_end", "ruqyah_q_ikhlas", "ruqyah_q_falaq", "ruqyah_q_naas", "ruqyah_dua_kalimat_evening"),
            scopeNote = LocalizedText4(
                en = "Verified Sunnah before sleep: Ayat-ul-Kursi (Bukhari 2311), last two verses of Al-Baqarah (Bukhari 5008), and 3x Al-Ikhlas, Al-Falaq, An-Naas wiping over the body (Bukhari 5017).",
                ur = "نیند سے قبل ثابت شدہ سنت: آیۃ الکرسی (بخاری 2311)، سورۃ البقرہ کی آخری دو آیات (بخاری 5008) اور 3 مرتبہ تین قل پڑھ کر ہاتھوں سے جسم پر پھیرنا (بخاری 5017)۔",
                ar = "الهدي النبوي قبل النوم: آية الكرسي (البخاري ٢٣١١) وخواتيم البقرة (٥٠٠٨) والمعوذات ثلاثاً مع المسح (٥٠١٧).",
                hi = "सोने से पहले मसनून अमल: आयत-उल-कुर्सी (बुख़ारी 2311), बक़रा की आख़िरी 2 आयतें (5008) और तीन क़ुल 3 बार (बुख़ारी 5017)।"
            ),
            searchAliases = listOf("neend", "neend ki pareshani", "sleep", "nightmares", "insomnia", "نیند", "नींद")
        ),
        TreatmentConditionTopic(
            id = "cond_dil_gham",
            title = LocalizedText4(
                en = "Dil ka Gham (Grief & Sorrow)",
                ur = "دل کا غم اور پریشانی",
                ar = "الحزن والهم",
                hi = "दिल का ग़म और चिंता"
            ),
            matchedItemIds = listOf("shifa_q_13_28", "shifa_q_39_53", "qalb_q_94_5_6", "qalb_q_21_87", "qalb_h_bukhari_6369", "qalb_h_bukhari_6346", "tib_talbina"),
            scopeNote = LocalizedText4(
                en = "Verified Quranic & Sunnah relief: Surah Ar-Ra'd 13:28, Dua of Yunus 21:87, Allahumma Inni A'udhu Bika Minal-Hammi wal-Hazan (Bukhari 6369), Du'a al-Karb (Bukhari 6346), and Talbinah (Bukhari 5689).",
                ur = "مستند قرآنی و نبوی تسلی: سورۃ الرعد (13:28)، دعائے یونسؑ (21:87)، غم و فکر کی دعا (بخاری 6369)، دعائے کرب (بخاری 6346) اور تلبینہ (بخاری 5689)۔",
                ar = "مواساة القرآن والسنة للحزن: الرعد ٢٨، دعاء ذي النون، دعاء الهم والحزن (البخاري ٦٣٦٩)، ودعاء الكرب (٦٣٤٦)، والتلبينة (٥٦٨٩).",
                hi = "प्रमाणित मार्ग: सूरह अर-राअद (13:28), दुआ-ए-यूनुस (21:87), बुख़ारी (6369, 6346) और तलबीना (5689)।"
            ),
            searchAliases = listOf("dil ka gham", "gham", "sadness", "grief", "anxiety", "depression", "worry", "sukoon", "غم", "دل", "ग़म", "सुकून")
        ),
        TreatmentConditionTopic(
            id = "cond_waswase",
            title = LocalizedText4(
                en = "Waswase (Intrusive Whispers & Doubts)",
                ur = "وسوسے",
                ar = "الوسوسة",
                hi = "वसवसे"
            ),
            matchedItemIds = listOf("ruqyah_q_naas", "waswasa_q_7_200", "waswasa_q_41_36", "waswasa_h_muslim_132a", "waswasa_h_muslim_134a", "waswasa_h_muslim_2203a", "waswasa_h_bukhari_137"),
            scopeNote = LocalizedText4(
                en = "Reassurance & Cure: Unwanted intrusive thoughts are NOT a sin and NOT proof of jinn/sihr. Seek refuge in Allah and disengage (Muslim 132a, 134a, 2203a; Bukhari 137).",
                ur = "اہم تسلی: ناپسندیدہ وسوسے آنا گناہ نہیں اور نہ ہی جن/سحر کی دلیل ہے؛ اللہ کی پناہ لیں اور ان خیالات پر توجہ نہ دیں (مسلم 132a، 134a، 2203a؛ بخاری 137)۔",
                ar = "الوسواس العارض المكروه لا يُؤاخذ عليه المسلم، وعلاجه بالاستعاذة والانتهاء (مسلم ١٣٢أ و ١٣٤أ والبخاري ١٣٧).",
                hi = "दिलासा: अनचाहे वसवसे गुनाह नहीं हैं; अल्लाह की पनाह माँगें और उन पर ध्यान देना रोक दें (मुस्लिम 132a, 134a, 2203a; बुख़ारी 137)।"
            ),
            searchAliases = listOf("waswase", "waswasa", "whispers", "doubts", "wudu doubt", "salah doubt", "وسوسے", "वसवसे")
        ),
        TreatmentConditionTopic(
            id = "cond_nazar",
            title = LocalizedText4(
                en = "Nazar (Evil Eye)",
                ur = "نظرِ بد",
                ar = "العين",
                hi = "नज़र-ए-बद"
            ),
            matchedItemIds = listOf("nazar_q_68_51_52", "ruqyah_q_falaq", "ruqyah_q_naas", "nazar_h_bukhari_5738", "nazar_h_muslim_2187", "nazar_h_muslim_2195a_2196b", "ruqyah_dua_hasan_husayn", "ruqyah_dua_jibril"),
            scopeNote = LocalizedText4(
                en = "Verified Guidance: Al-'Ayn is real (Bukhari 5738); treat strictly via Tabrik, Sunnah Ruqyah (Muslim 2195a, 2196b, 2186, Bukhari 3371), and Ightisal (Muslim 2187)—never amulets or superstitions.",
                ur = "مستند رہنمائی: نظرِ بد حق ہے (بخاری 5738)؛ اس کا علاج مسنون تبریک، رقیہ (مسلم 2195a، 2196b، 2186) اور اغتسال (مسلم 2187) ہے—ٹوٹکے یا تعویذ نہیں۔",
                ar = "العين حق (البخاري ٥٧٣٨) وتُعالج بالرقية الشرعية والتبريك والاغتسال المأذون (مسلم ٢١٨٧ و ٢١٩٥أ).",
                hi = "नज़र हक़ है (बुख़ारी 5738); इसका इलाज तबर्रुक (मा शा अल्लाह / अल्लाहुम्मा बारिक), मसनून रुक़या व इग़तिसाल है।"
            ),
            searchAliases = listOf("nazar", "nazar e bad", "evil eye", "ayn", "نظر", "नज़र")
        ),
        TreatmentConditionTopic(
            id = "cond_sihr",
            title = LocalizedText4(
                en = "Sihr (Magic / Black Magic)",
                ur = "سحر / کالا علم",
                ar = "السحر",
                hi = "सिह्र (जादू)"
            ),
            matchedItemIds = listOf("sihr_q_2_102", "sihr_q_7_117_122", "sihr_q_10_81_82", "sihr_q_20_69", "ruqyah_q_falaq", "ruqyah_q_naas", "tib_ajwah_dates", "sihr_guide_no_accusations"),
            scopeNote = LocalizedText4(
                en = "Verified Guidance: Harm occurs strictly by Allah's permission (2:102). Rely on Mu'awwidhat, Ayat-ul-Kursi, Al-Baqarah, and Sunnah Ruqyah—never guess, accuse anyone, or visit occult practitioners.",
                ur = "مستند رہنمائی: کوئی اللہ کے اذن کے بغیر نقصان نہیں پہنچا سکتا (2:102)۔ معوذات، آیۃ الکرسی اور مسنون رقیہ اختیار کریں، اور کسی پر جادو کا الزام یا عاملوں سے رجوع ہرگز نہ کریں۔",
                ar = "لا ضرر إلا بإذن الله (البقرة ١٠٢)، والوقاية والعلاج بالمعوذات والبقرة والرقية الصحيحة مع تجنب التهم والسحرة.",
                hi = "अल्लाह की अनुमति के बिना कोई हानि नहीं पहुँचा सकता (2:102)। मुअव्विज़ात व मसनून रुक़या अपनाएँ और किसी पर झूठा शक न करें।"
            ),
            searchAliases = listOf("sihr", "kala ilm", "black magic", "magic", "jadu", "سحر", "کالا علم", "جادو", "सिह्र", "जादू")
        ),
        TreatmentConditionTopic(
            id = "cond_hasad",
            title = LocalizedText4(
                en = "Hasad (Envy)",
                ur = "حسد",
                ar = "الحسد",
                hi = "हसद (ईर्ष्या)"
            ),
            matchedItemIds = listOf("ruqyah_q_falaq", "nazar_q_68_51_52", "ruqyah_dua_jibril", "nazar_h_muslim_2187"),
            scopeNote = LocalizedText4(
                en = "Verified Guidance: Surah Al-Falaq (113:5) explicitly teaches seeking Allah's refuge 'from the evil of an envier when he envies', along with Jibril's Ruqyah (Muslim 2186).",
                ur = "ثابت شدہ دلیل: سورۃ الفلق (113:5) میں خاص طور پر حاسد کے حسد سے پناہ مانگی گئی ہے اور جبرائیلؑ کے دم (مسلم 2186) میں بھی حسد سے حفاظت کی دعا ہے۔",
                ar = "سورة الفلق (١١٣: ٥) ورقية جبريل (مسلم ٢١٨٦) في الاستعاذة من شر الحاسد.",
                hi = "सूरह अल-फ़लक़ (113:5) व जिब्रील (अलैहि.) की दुआ (मुस्लिम 2186) में ईर्ष्यालु (हासद) के शर से पनाह माँगी गई है।"
            ),
            searchAliases = listOf("hasad", "envy", "jealousy", "jalan", "حسد", "हसद", "ईर्ष्या")
        ),
        TreatmentConditionTopic(
            id = "cond_roohani",
            title = LocalizedText4(
                en = "Roohani Pareshani (Spiritual Distress)",
                ur = "روحانی پریشانی",
                ar = "الضيق الروحي والهم الإيماني",
                hi = "रूहानी परेशानी"
            ),
            matchedItemIds = listOf("shifa_q_17_82", "shifa_q_10_57", "shifa_q_13_28", "shifa_q_2_153", "ruqyah_q_fatihah", "ruqyah_q_kursi", "qalb_h_muslim_2699"),
            scopeNote = LocalizedText4(
                en = "Verified Guidance: Return to Salah, Sabr (2:153), Quranic recitation (17:82, 10:57, Muslim 2699), Zikr (13:28), and Sunnah Ruqyah (Muslim 2186, Bukhari 5736).",
                ur = "مستند رہنمائی: نماز و صبر (2:153)، تلاوتِ قرآن (17:82، 10:57، مسلم 2699)، ذکرِ الٰہی (13:28) اور مسنون رقیہ اختیار کریں۔",
                ar = "العلاج الروحي الأصيل: المحافظة على الصلاة والصبر وتلاوة القرآن والذكر والرقية المشروعة.",
                hi = "प्रामाणिक मार्ग: नमाज़ व सब्र (2:153), क़ुरआन की तिलावत (17:82, 10:57, मुस्लिम 2699), ज़िक्र (13:28) और मसनून रुक़या।"
            ),
            searchAliases = listOf("roohani pareshani", "spiritual", "bechaini", "distress", "ruh", "روحانی پریشانی", "रूहानी परेशानी")
        )
    )
}
