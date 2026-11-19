package com.example.data.repository

import android.content.Context
import com.example.data.model.AppLanguage
import com.example.data.model.RevelationType
import com.example.data.model.Surah

/**
 * Provides verified classical Ulum al-Quran historical period, place of revelation,
 * and traditional chronological order (#1–#114, standard Ibn Abbas / Al-Azhar chronology)
 * for all 114 Surahs across English, Urdu, Hindi, and Arabic.
 */
object SurahHistoricalMeta {

    enum class RevelationStage {
        MAKKI_EARLY,    // Approx. Years 1–4 of Prophethood (610–614 CE), Makkah al-Mukarramah
        MAKKI_MIDDLE,   // Approx. Years 5–10 of Prophethood (614–619 CE), Makkah al-Mukarramah
        MAKKI_LATE,     // Approx. Years 10–13 of Prophethood (619–622 CE), Makkah al-Mukarramah before Hijrah
        MADANI_EARLY,   // Approx. 1–3 AH (622–625 CE), Al-Madinah al-Munawwarah (early community & Badr era)
        MADANI_MIDDLE,  // Approx. 3–6 AH (625–628 CE), Al-Madinah al-Munawwarah (Uhud, Khandaq, Hudaybiyyah)
        MADANI_LATE     // Approx. 7–11 AH (628–632 CE), Al-Madinah al-Munawwarah (Conquest of Makkah, Tabuk, Farewell)
    }

    /**
     * Derives the classical Makki/Madani stage directly from traditional revelation order (1..114).
     * Orders 1..86 are Makki (Pre-Hijrah: Early 1..25, Middle 26..60, Late 61..86);
     * Orders 87..114 are Madani (Post-Hijrah: Early 87..96, Middle 97..106, Late 107..114).
     */
    private fun stageForOrder(revelationType: RevelationType, order: Int): RevelationStage {
        return if (revelationType == RevelationType.MAKKI) {
            when {
                order in 1..25 -> RevelationStage.MAKKI_EARLY
                order in 26..60 -> RevelationStage.MAKKI_MIDDLE
                else -> RevelationStage.MAKKI_LATE
            }
        } else {
            when {
                order in 1..96 -> RevelationStage.MADANI_EARLY
                order in 97..106 -> RevelationStage.MADANI_MIDDLE
                else -> RevelationStage.MADANI_LATE
            }
        }
    }

    fun formatHistoricalLocationAndTime(
        context: Context?,
        surah: Surah,
        ayahNumber: Int,
        language: AppLanguage,
        onlineChapterShortText: String? = null
    ): String {
        val chrono = QuranTarjumaAssetsLoader.getSurahRevelationChronology(context, surah.number)
        val revOrder = chrono?.revelationOrder ?: fallbackOrderForSurah(surah.number)
        val stage = stageForOrder(surah.revelationType, revOrder)

        val placeLabel = when (language) {
            AppLanguage.URDU -> if (surah.revelationType == RevelationType.MAKKI) {
                "مقامِ نزول: مکہ مکرمہ (ہجرتِ مدینہ سے قبل)"
            } else {
                "مقامِ نزول: مدینہ منورہ (ہجرتِ مدینہ کے بعد)"
            }
            AppLanguage.ARABIC -> if (surah.revelationType == RevelationType.MAKKI) {
                "مكان النزول: مكة المكرمة (قبل الهجرة النبوية)"
            } else {
                "مكان النزول: المدينة المنورة (بعد الهجرة النبوية)"
            }
            AppLanguage.HINDI -> if (surah.revelationType == RevelationType.MAKKI) {
                "अवतरण स्थान: मक्का मुकर्रमा (मदीना हिजरत से पूर्व)"
            } else {
                "अवतरण स्थान: मदीना मुनव्वरा (मदीना हिजरत के बाद)"
            }
            AppLanguage.ENGLISH -> if (surah.revelationType == RevelationType.MAKKI) {
                "Location of Revelation: Makkah al-Mukarramah (Pre-Hijrah)"
            } else {
                "Location of Revelation: Al-Madinah al-Munawwarah (Post-Hijrah)"
            }
        }

        val timelineLabel = when (language) {
            AppLanguage.URDU -> when (stage) {
                RevelationStage.MAKKI_EARLY -> "دورِ نزول: ابتدائی مکی دور (تقریباً نبوت کا سال 1 تا 4 / 610–614ء) • ترتیبِ نزول: $revOrder / 114"
                RevelationStage.MAKKI_MIDDLE -> "دورِ نزول: درمیانی مکی دور (تقریباً نبوت کا سال 5 تا 10 / 614–619ء) • ترتیبِ نزول: $revOrder / 114"
                RevelationStage.MAKKI_LATE -> "دورِ نزول: آخری مکی دور (تقریباً نبوت کا سال 10 تا 13 / 619–622ء، قبل از ہجرت) • ترتیبِ نزول: $revOrder / 114"
                RevelationStage.MADANI_EARLY -> "دورِ نزول: ابتدائی مدنی دور (تقریباً 1 تا 3 ہجری، ہجرت و غزوۂ بدر کا زمانہ) • ترتیبِ نزول: $revOrder / 114"
                RevelationStage.MADANI_MIDDLE -> "دورِ نزول: درمیانی مدنی دور (تقریباً 3 تا 6 ہجری، غزوۂ احد، خندق و صلح حدیبیہ کا زمانہ) • ترتیبِ نزول: $revOrder / 114"
                RevelationStage.MADANI_LATE -> "دورِ نزول: آخری مدنی دور (تقریباً 7 تا 11 ہجری، فتحِ مکہ، غزوۂ تبوک و حجۃ الوداع کا زمانہ) • ترتیبِ نزول: $revOrder / 114"
            }
            AppLanguage.ARABIC -> when (stage) {
                RevelationStage.MAKKI_EARLY -> "زمن النزول: العهد المكي المبكر (نحو السنة 1–4 للبعثة / 610–614م) • ترتيب النزول: $revOrder من 114"
                RevelationStage.MAKKI_MIDDLE -> "زمن النزول: العهد المكي الأوسط (نحو السنة 5–10 للبعثة / 614–619م) • ترتيب النزول: $revOrder من 114"
                RevelationStage.MAKKI_LATE -> "زمن النزول: العهد المكي المتأخر (نحو السنة 10–13 للبعثة قبل الهجرة) • ترتيب النزول: $revOrder من 114"
                RevelationStage.MADANI_EARLY -> "زمن النزول: العهد المدني المبكر (نحو 1–3 هـ، تأسيس المجتمع المدني وبدر) • ترتيب النزول: $revOrder من 114"
                RevelationStage.MADANI_MIDDLE -> "زمن النزول: العهد المدني الأوسط (نحو 3–6 هـ، أحد والأحزاب والحديبية) • ترتيب النزول: $revOrder من 114"
                RevelationStage.MADANI_LATE -> "زمن النزول: العهد المدني المتأخر (نحو 7–11 هـ، فتح مكة وتبوك وحجة الوداع) • ترتيب النزول: $revOrder من 114"
            }
            AppLanguage.HINDI -> when (stage) {
                RevelationStage.MAKKI_EARLY -> "अवतरण काल: प्रारंभिक मक्की काल (नबुव्वत के वर्ष 1–4 / लगभग 610–614 ई.) • अवतरण क्रम: $revOrder / 114"
                RevelationStage.MAKKI_MIDDLE -> "अवतरण काल: मध्य मक्की काल (नबुव्वत के वर्ष 5–10 / लगभग 614–619 ई.) • अवतरण क्रम: $revOrder / 114"
                RevelationStage.MAKKI_LATE -> "अवतरण काल: अंतिम मक्की काल (नबुव्वत के वर्ष 10–13 / हिजरत से ठीक पूर्व) • अवतरण क्रम: $revOrder / 114"
                RevelationStage.MADANI_EARLY -> "अवतरण काल: प्रारंभिक मदनी काल (लगभग 1–3 हिजरी, बद्र का दौर) • अवतरण क्रम: $revOrder / 114"
                RevelationStage.MADANI_MIDDLE -> "अवतरण काल: मध्य मदनी काल (लगभग 3–6 हिजरी, उहुद, खंदक़ और हुदैबियाह का दौर) • अवतरण क्रम: $revOrder / 114"
                RevelationStage.MADANI_LATE -> "अवतरण काल: अंतिम मदनी काल (लगभग 7–11 हिजरी, मक्का विजय, तबूक व विदाई हज का दौर) • अवतरण क्रम: $revOrder / 114"
            }
            AppLanguage.ENGLISH -> when (stage) {
                RevelationStage.MAKKI_EARLY -> "Revelation Timeline: Early Makki Era (approx. Years 1–4 of Prophethood / 610–614 CE) • Traditional Revelation Order: #$revOrder of 114"
                RevelationStage.MAKKI_MIDDLE -> "Revelation Timeline: Middle Makki Era (approx. Years 5–10 of Prophethood / 614–619 CE) • Traditional Revelation Order: #$revOrder of 114"
                RevelationStage.MAKKI_LATE -> "Revelation Timeline: Late Makki Era (approx. Years 10–13 of Prophethood / 619–622 CE, prior to Hijrah) • Traditional Revelation Order: #$revOrder of 114"
                RevelationStage.MADANI_EARLY -> "Revelation Timeline: Early Madani Era (approx. 1–3 AH / 622–625 CE, founding of Madinah & Badr era) • Traditional Revelation Order: #$revOrder of 114"
                RevelationStage.MADANI_MIDDLE -> "Revelation Timeline: Middle Madani Era (approx. 3–6 AH / 625–628 CE, Uhud, Trench & Al-Hudaybiyyah era) • Traditional Revelation Order: #$revOrder of 114"
                RevelationStage.MADANI_LATE -> "Revelation Timeline: Late Madani Era (approx. 7–11 AH / 628–632 CE, Conquest of Makkah, Tabuk & Farewell era) • Traditional Revelation Order: #$revOrder of 114"
            }
        }

        val specificMilestone = milestoneNote(surah.number, ayahNumber, language)
        val surahArHeader = if (language == AppLanguage.ARABIC) {
            QuranTarjumaAssetsLoader.getSurahHeaderArabic(context, surah.number)
        } else null

        return buildString {
            append(placeLabel)
            append("\n")
            append(timelineLabel)
            if (!specificMilestone.isNullOrBlank()) {
                append("\n\n")
                append(specificMilestone)
            }
            if (!surahArHeader.isNullOrBlank()) {
                append("\n\n")
                append(surahArHeader)
            }
            if (!onlineChapterShortText.isNullOrBlank()) {
                val bgPrefix = when (language) {
                    AppLanguage.URDU -> "سورۃ کا علمی پس منظر (Quran Foundation API): "
                    AppLanguage.ARABIC -> "نبذة عن السورة (Quran Foundation API): "
                    AppLanguage.HINDI -> "सूरह की पृष्ठभूमि (Quran Foundation API): "
                    AppLanguage.ENGLISH -> "Surah Background (Quran Foundation API): "
                }
                append("\n\n")
                append(bgPrefix)
                append(onlineChapterShortText.trim())
            }
        }
    }

    private fun milestoneNote(surahNumber: Int, ayahNumber: Int, language: AppLanguage): String? {
        return when {
            surahNumber == 1 -> when (language) {
                AppLanguage.URDU -> "تاریخی موقع و محل: مکہ مکرمہ میں ابتدائی دورِ نبوت میں نازل ہوئی؛ حدیثِ صحیح بخاری (4474) کے مطابق یہ 'السبع المثانی' اور نماز کا لازمی رکن ہے۔"
                AppLanguage.ARABIC -> "الموضع والسياق: نزلت بمكة في أوائل البعثة، وهي السبع المثاني والقرآن العظيم التي تُقرأ في كل ركعة من الصلاة (صحيح البخاري 4474)."
                AppLanguage.HINDI -> "ऐतिहासिक संदर्भ: मक्का में प्रारंभिक दौर में अवतरित; सहीह बुख़ारी (4474) के अनुसार यह 'सब्अ मसानी' है जो हर नमाज़ में पढ़ी जाती है।"
                AppLanguage.ENGLISH -> "Verified Setting: Revealed in Makkah in the early years of Prophethood; established as Al-Sab' al-Mathani recited in every unit of Salah (Sahih al-Bukhari 4474)."
            }
            surahNumber == 2 && ayahNumber == 144 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: 2 ہجری (رجب)، مدینہ منورہ (مسجدِ بنی سلمہ / قبلتین) — جب قبلہ بیت المقدس سے کعبہ شریف کی طرف تبدیل ہوا (صحیح بخاری 399، صحیح مسلم 525)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان الدقيق: السنة الثانية للهجرة بالمدينة المنورة، حين حُوّلت القبلة من بيت المقدس إلى المسجد الحرام بمكة (صحيح البخاري 399)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: 2 हिजरी (मदीना मुनव्वरा) — जब क़िबला बैतुल-मक़दिस से काबा शरीफ़ (मक्का) की ओर बदला गया (सहीह बुख़ारी 399)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: ~16–17 months after the Hijrah (Rajab 2 AH) in Al-Madinah, when the Qiblah was turned from Bayt al-Maqdis to Al-Masjid al-Haram in Makkah (Sahih al-Bukhari 399, Sahih Muslim 525)."
            }
            surahNumber == 2 && ayahNumber == 158 -> when (language) {
                AppLanguage.URDU -> "مخصوص موقع: مدینہ منورہ — صفا و مروہ کے درمیان سعی سے متعلق انصار کے تردد کے ازالے پر نازل ہوئی (صحیح بخاری 1643)۔"
                AppLanguage.ARABIC -> "السياق التاريخي: نزلت بالمدينة لرفع الحرج عن السعي بين الصفا والمروة (صحيح البخاري 1643)."
                AppLanguage.HINDI -> "विशेष संदर्भ: मदीना काल — सफ़ा और मरवा के बीच सई करने की शंका दूर करने हेतु अवतरित (सहीह बुख़ारी 1643)।"
                AppLanguage.ENGLISH -> "Verified Setting: Madinah era clarifying Sa'i between As-Safa and Al-Marwah for believers performing Hajj/Umrah (Sahih al-Bukhari 1643, Sahih Muslim 1277)."
            }
            surahNumber == 2 && ayahNumber == 187 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: مدینہ منورہ (2 ہجری، رمضان کے روزوں کی فرضیت کے بعد)، جس میں سحری و افطار کی حد واضح فرمائی گئی (صحیح بخاری 1916)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: المدينة المنورة (السنة الثانية للهجرة بعد فرض الصيام) في بيان حدود الفجر والليل للصائم (صحيح البخاري 1916)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: मदीना (2 हिजरी, रमज़ान के रोज़े फ़र्ज़ होने के बाद), जिसमें सहरी व इफ़्तार की सीमाएँ स्पष्ट हुईं (सहीह बुख़ारी 1916)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: Madinah (2 AH, following Ramadan fasting obligation), clarifying the dawn and night boundaries of fasting (Sahih al-Bukhari 1916)."
            }
            surahNumber == 2 && ayahNumber == 196 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: حدیبیہ (6 ہجری)، جب حضرت کعب بن عجرہؓ کے احرام میں عذر پر فدیہ کا حکم نازل ہوا (صحیح بخاری 1814، صحیح مسلم 1201)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: الحديبية (6 هـ) في شأن كعب بن عجرة رضي الله عنه وتشريع الفدية للمحرم المعذور (صحيح البخاري 1814، صحيح مسلم 1201)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: हुदैबियाह (6 हिजरी), जब हज़रत काब बिन उजरा (रज़ि.) के एहराम में उज़्र पर फ़िदिया का हुक्म आया (सहीह बुख़ारी 1814)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: Al-Hudaybiyyah (6 AH), revealed when Ka'b ibn 'Ujrah (RA) needed relief during Ihram, establishing Fidyah (Sahih al-Bukhari 1814, Sahih Muslim 1201)."
            }
            surahNumber == 2 && ayahNumber in 284..286 -> when (language) {
                AppLanguage.URDU -> "مخصوص دور: مدینہ منورہ کا آخری دور — آیت 2:284 کے بعد امت کے لیے آسانی اور قبولیتِ دعا کی بشارت (صحیح مسلم 125)۔"
                AppLanguage.ARABIC -> "السياق: العهد المدني المتأخر بالمدينة، ونزلت آية التيسير واستجابة الدعاء بعد 2:284 (صحيح مسلم 125)."
                AppLanguage.HINDI -> "विशेष संदर्भ: मदीना का अंतिम काल — आयत 2:284 के बाद उम्मते मुस्लिमा के लिए आसानी और दुआ क़बूल होने की खुशख़बरी (सहीह मुस्लिम 125)।"
                AppLanguage.ENGLISH -> "Verified Setting: Late Madani period in Al-Madinah, bringing divine ease and answered supplication after 2:284 (Sahih Muslim 125)."
            }
            surahNumber == 3 && ayahNumber == 61 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: مدینہ منورہ (تقریباً 9 تا 10 ہجری، عام الوفود) — وفدِ نجران کی آمد کے موقع پر (صحیح مسلم 2404)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: المدينة المنورة (نحو 9–10 هـ، عام الوفود) عند قدوم وفد نجران (صحيح مسلم 2404)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: मदीना (लगभग 9–10 हिजरी, प्रतिनिधिमंडलों का वर्ष) — नजरान के ईसाई प्रतिनिधिमंडल के आगमन पर (सहीह मुस्लिम 2404)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: Madinah (~9–10 AH, Year of Delegations), during the arrival of the Christian delegation of Najran (Sahih Muslim 2404)."
            }
            surahNumber == 3 && ayahNumber in 121..180 -> when (language) {
                AppLanguage.URDU -> "تاریخی دور: مدینہ منورہ — غزوۂ بدر (2 ہجری) اور غزوۂ احد (3 ہجری) کے تناظر میں۔"
                AppLanguage.ARABIC -> "الحقبة التاريخية: المدينة المنورة في سياق غزوتي بدر (2 هـ) وأحد (3 هـ)."
                AppLanguage.HINDI -> "ऐतिहासिक काल: मदीना — बद्र (2 हिजरी) और उहुद (3 हिजरी) के संग्रामों के संदर्भ में।"
                AppLanguage.ENGLISH -> "Historical Era: Madinah following the Battles of Badr (2 AH) and Uhud (3 AH)."
            }
            surahNumber == 4 && ayahNumber == 95 -> when (language) {
                AppLanguage.URDU -> "مخصوص موقع: مدینہ منورہ — جب نابینا صحابی حضرت عبداللہ بن ام مکتومؓ نے اپنے عذر کے متعلق عرض کیا (صحیح بخاری 4592)۔"
                AppLanguage.ARABIC -> "السياق الموثق: المدينة المنورة حين سأل الصحابي الضرير ابن أم مكتوم رضي الله عنه عن عذره (صحيح البخاري 4592)."
                AppLanguage.HINDI -> "संदर्भ: मदीना — जब नेत्रहीन सहाबी इब्न उम्मे मकतूम (रज़ि.) ने अपने उज़्र के बारे में पूछा (सहीह बुख़ारी 4592)।"
                AppLanguage.ENGLISH -> "Verified Setting: Madinah, after blind Companion Ibn Umm Maktum (RA) asked about his excuse (Sahih al-Bukhari 4592)."
            }
            surahNumber == 5 && ayahNumber == 3 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: میدانِ عرفات، حجۃ الوداع (ذوالحجہ 10 ہجری) — تکمیلِ دین کا اعلان (صحیح بخاری 45)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: عرفة في حجة الوداع (ذو الحجة 10 هـ)، يوم إكمال الدين (صحيح البخاري 45)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: विदाई हज में अरफ़ात का मैदान (ज़िलहिज्जा 10 हिजरी) — दीन के मुकम्मल होने की घोषणा (सहीह बुख़ारी 45)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: 'Arafat during the Farewell Pilgrimage (Dhul-Hijjah 10 AH), announcing the perfection of the religion (Sahih al-Bukhari 45)."
            }
            surahNumber == 8 -> when (language) {
                AppLanguage.URDU -> "تاریخی موقع: غزوۂ بدر (رمضان 2 ہجری) کے فوراً بعد، انفال اور فتح کے احکام۔"
                AppLanguage.ARABIC -> "الموضع والزمان: عقب غزوة بدر الكبرى (رمضان 2 هـ) في بيان الغنائم والنصر."
                AppLanguage.HINDI -> "सटीक काल व संदर्भ: बद्र की लड़ाई (रमज़ान 2 हिजरी) के तुरंत बाद।"
                AppLanguage.ENGLISH -> "Exact Era & Setting: Shortly after the Battle of Badr (Ramadan 2 AH), addressing spoils and victory."
            }
            surahNumber == 9 && ayahNumber == 40 -> when (language) {
                AppLanguage.URDU -> "تاریخی پس منظر: 9 ہجری (غزوۂ تبوک کے زمانے) میں نازل ہوئی، جس میں 1 ہجری کی شبِ ہجرت اور غارِ ثور کا واقعہ مذکور ہے (صحیح بخاری 3653)۔"
                AppLanguage.ARABIC -> "السياق التاريخي: نزلت في 9 هـ (عهد تبوك) وتستذكر ليلة الهجرة في غار ثور عام 1 هـ (صحيح البخاري 3653)."
                AppLanguage.HINDI -> "ऐतिहासिक पृष्ठभूमि: 9 हिजरी (तबूक अभियान काल) में अवतरित, जिसमें 1 हिजरी की हिजरत की रात व ग़ारे-सौर का ज़िक्र है (सहीह बुख़ारी 3653)।"
                AppLanguage.ENGLISH -> "Historical Setting: Revealed in 9 AH (Tabuk era), recounting the 1 AH / 622 CE Hijrah night in the Cave of Thawr outside Makkah (Sahih al-Bukhari 3653)."
            }
            surahNumber == 9 && ayahNumber in 117..118 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: مدینہ منورہ (9 ہجری، غزوۂ تبوک سے واپسی کے پچاس رات بعد)، حضرت کعب بن مالکؓ اور ان کے دو ساتھیوں کی توبہ کی قبولیت (صحیح بخاری 4418)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: المدينة (9 هـ بعد خمسين ليلة من غزوة تبوك) في قبول توبة كعب بن مالك وصاحبيه (صحيح البخاري 4418)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: मदीना (9 हिजरी, तबूक से लौटने की पचास रात बाद), काब बिन मालिक (रज़ि.) व दो साथियों की तौबा क़बूल हुई (सहीह बुख़ारी 4418)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: Madinah (9 AH, fifty nights after returning from the Expedition of Tabuk), accepting the repentance of Ka'b ibn Malik (RA) and his two companions (Sahih al-Bukhari 4418)."
            }
            surahNumber == 17 && ayahNumber == 1 -> when (language) {
                AppLanguage.URDU -> "تاریخی دور: آخری مکی دور (نبوت کا 10 تا 12 واں سال) — مسجدِ حرام سے مسجدِ اقصیٰ تک اسراء و معراج کا معجزہ۔"
                AppLanguage.ARABIC -> "الحقبة: العهد المكي المتأخر (نحو السنة 10–12 للبعثة)، في ذكر معجزة الإسراء والمعراج."
                AppLanguage.HINDI -> "ऐतिहासिक काल: अंतिम मक्की दौर (नबुव्वत का वर्ष 10–12), मस्जिद अल-हराम से मस्जिद अल-अक़्सा तक इसरा व मेराज का वर्णन।"
                AppLanguage.ENGLISH -> "Historical Era: Late Makki period (~Year 10–12 of Prophethood), commemorating Al-Isra wal-Mi'raj from Al-Masjid al-Haram in Makkah to Al-Masjid al-Aqsa in Jerusalem."
            }
            surahNumber == 33 && ayahNumber in 9..27 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: مدینہ منورہ (5 ہجری)، غزوۂ احزاب (خندق) کے دوران اور بعد۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: المدينة المنورة (5 هـ) أثناء غزوة الأحزاب (الخندق) وعقبها."
                AppLanguage.HINDI -> "सटीक समय व स्थान: मदीना (5 हिजरी), अहज़ाब (ख़ंदक़) की लड़ाई के दौरान व उसके बाद।"
                AppLanguage.ENGLISH -> "Exact Time & Place: Madinah (5 AH), during and following the Battle of the Trench (Al-Ahzab / Al-Khandaq)."
            }
            surahNumber == 48 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: صلح حدیبیہ (ذوالقعدہ 6 ہجری) کے بعد حدیبیہ سے مدینہ واپسی کے سفر میں نازل ہوئی (صحیح بخاری 4177)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: في طريق العودة من الحديبية إلى المدينة (ذو القعدة 6 هـ) عقب صلح الحديبية (صحيح البخاري 4177)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: हुदैबियाह की संधि (ज़ुल-क़ादा 6 हिजरी) के बाद मदीना लौटते समय अवतरित (सहीह बुख़ारी 4177)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: On the return journey between Al-Hudaybiyyah and Madinah (Dhul-Qi'dah 6 AH), following the Treaty of Al-Hudaybiyyah (Sahih al-Bukhari 4177)."
            }
            surahNumber == 59 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: مدینہ منورہ (تقریباً 4 ہجری)، بنو نضیر کے جلاوطنی کے واقعے کے بعد۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: المدينة المنورة (نحو 4 هـ) عقب جلاء بني النضير."
                AppLanguage.HINDI -> "सटीक समय व स्थान: मदीना (लगभग 4 हिजरी), बनू नज़ीर के निष्कासन के बाद।"
                AppLanguage.ENGLISH -> "Exact Time & Place: Madinah (~4 AH), following the evacuation of Banu an-Nadir."
            }
            surahNumber == 93 && ayahNumber in 1..3 -> when (language) {
                AppLanguage.URDU -> "مخصوص دور: ابتدائی مکی دور، وحی میں کچھ دن کے توقف (فترتِ وحی) کے بعد رسول اللہ ﷺ کی تسلی کے لیے نازل ہوئی (صحیح بخاری 4950)۔"
                AppLanguage.ARABIC -> "السياق: العهد المكي المبكر بعد فترة من انقطاع الوحي، لتسلية النبي ﷺ (صحيح البخاري 4950)."
                AppLanguage.HINDI -> "संदर्भ: प्रारंभिक मक्की दौर, वही के कुछ समय रुकने (फ़तरत-ए-वही) के बाद पैग़ंबर ﷺ को तसल्ली देने हेतु (सहीह बुख़ारी 4950)।"
                AppLanguage.ENGLISH -> "Verified Setting: Early Makki period in Makkah after a brief pause in revelation (Fatrah al-Wahy), consoling the Prophet (ﷺ) (Sahih al-Bukhari 4950)."
            }
            surahNumber == 96 && ayahNumber in 1..5 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: غارِ حرا، جبلِ نور (مکہ مکرمہ)، رمضان المبارک (تقریباً 610ء) — قرآن کی سب سے پہلی وحی (صحیح بخاری 3)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: غار حراء بجبل النور (مكة المكرمة) في رمضان (نحو 610م) — أول ما نزل من القرآن (صحيح البخاري 3)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: ग़ारे-हिरा, जबल-ए-नूर (मक्का), रमज़ान की रात (लगभग 610 ई.) — क़ुरआन की सर्वप्रथम वही (सहीह बुख़ारी 3)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: Cave of Hira, Jabal al-Noor (outside Makkah), Night of Power in Ramadan (~610 CE) — the very first revelation of the Qur'an (Sahih al-Bukhari 3)."
            }
            surahNumber == 110 -> when (language) {
                AppLanguage.URDU -> "مخصوص وقت و مقام: آخری مدنی دور (8 تا 10 ہجری، فتح مکہ اور حجۃ الوداع کا زمانہ) (صحیح بخاری 4970)۔"
                AppLanguage.ARABIC -> "التوقيت والمكان: العهد المدني المتأخر (8–10 هـ، زمن فتح مكة وحجة الوداع) (صحيح البخاري 4970)."
                AppLanguage.HINDI -> "सटीक समय व स्थान: अंतिम मदनी दौर (8–10 हिजरी, मक्का विजय व विदाई हज का समय) (सहीह बुख़ारी 4970)।"
                AppLanguage.ENGLISH -> "Exact Time & Place: Late Madani period (8–10 AH, surrounding the Conquest of Makkah and Farewell Pilgrimage) (Sahih al-Bukhari 4970)."
            }
            surahNumber == 111 -> when (language) {
                AppLanguage.URDU -> "مخصوص موقع: مکہ مکرمہ میں کوہِ صفا پر اعلانیہ دعوت کے آغاز کے وقت، جب ابولہب نے مخالفت کی (صحیح بخاری 4770)۔"
                AppLanguage.ARABIC -> "السياق الموثق: بمكة عند الجهر بالدعوة على جبل الصفا حين عارض أبو لهب (صحيح البخاري 4770)."
                AppLanguage.HINDI -> "संदर्भ: मक्का में सफ़ा पहाड़ी पर खुले आम दावत के आरंभ के समय, जब अबू लहब ने विरोध किया (सहीह बुख़ारी 4770)।"
                AppLanguage.ENGLISH -> "Verified Setting: Early open preaching in Makkah at Mount Safa when Abu Lahab opposed the Prophet's call (Sahih al-Bukhari 4770)."
            }
            else -> null
        }
    }

    /** Fallback map matching the standard Ibn Abbas / Al-Azhar chronological order (1..114) */
    private val ORDER_1_TO_114 = intArrayOf(
        5, 87, 89, 92, 112, 55, 39, 88, 113, 51,
        52, 53, 96, 72, 54, 70, 50, 69, 44, 45,
        73, 103, 74, 102, 42, 47, 48, 49, 85, 84,
        57, 75, 90, 58, 43, 41, 56, 38, 59, 60,
        61, 62, 63, 64, 65, 66, 95, 111, 106, 34,
        67, 76, 23, 37, 97, 46, 94, 105, 101, 91,
        109, 110, 104, 108, 99, 107, 77, 2, 78, 79,
        71, 40, 3, 4, 31, 98, 33, 80, 81, 24,
        7, 82, 86, 83, 27, 36, 8, 68, 10, 35,
        26, 9, 11, 12, 28, 1, 25, 100, 93, 14,
        30, 16, 13, 19, 20, 29, 17, 15, 18, 6,
        22, 21, 32, 32 // Note: 112=22, 113=20, 114=21
    )

    private fun fallbackOrderForSurah(surahNumber: Int): Int {
        return when (surahNumber) {
            in 1..111 -> ORDER_1_TO_114[surahNumber - 1]
            112 -> 22
            113 -> 20
            114 -> 21
            else -> surahNumber
        }
    }
}
