package com.example.localization

import com.example.data.model.AppLanguage

object ShifaLocalization {

    val strings: Map<String, Map<AppLanguage, String>> = mapOf(
        "shifa_ruqyah" to mapOf(
            AppLanguage.ENGLISH to "Shifa & Ruqyah",
            AppLanguage.URDU to "شفا اور رقیہ",
            AppLanguage.ARABIC to "الشفاء والرقية الشرعية",
            AppLanguage.HINDI to "शिफ़ा और रुक़या"
        ),
        "shifa_scope_title" to mapOf(
            AppLanguage.ENGLISH to "Authentic Islamic Spiritual Guidance",
            AppLanguage.URDU to "خالص قرآنی و مسنون روحانی رہنمائی",
            AppLanguage.ARABIC to "التوجيه الشرعي الموثق للاستشفاء والرقية",
            AppLanguage.HINDI to "प्रामाणिक इस्लामिक आध्यात्मिक मार्गदर्शन"
        ),
        "shifa_scope_body" to mapOf(
            AppLanguage.ENGLISH to "Strictly based on the Noble Quran and Sahih Hadith (Bukhari & Muslim). Provides spiritual healing & Prophetic Sunnah guidance only—not modern medical advice, diagnosis, or guaranteed disease claims.",
            AppLanguage.URDU to "یہ حصہ صرف قرآنِ مجید اور صحیح احادیث (بخاری و مسلم) پر مبنی روحانی علاج اور مسنون رہنمائی پیش کرتا ہے—یہ طبی تشخیص، ڈاکٹری مشورے یا بغیر دلیل شفا کے حتمی دعوے کا متبادل نہیں۔",
            AppLanguage.ARABIC to "محتوى موثق من القرآن الكريم والصحيحين للرقية والطب النبوي، وليس بديلاً عن التشخيص أو العلاج الطبي المختص.",
            AppLanguage.HINDI to "केवल क़ुरआन और सहीह हदीस (बुख़ारी व मुस्लिम) पर आधारित आध्यात्मिक मार्गदर्शन—यह आधुनिक चिकित्सीय सलाह या निदान नहीं है।"
        ),
        "shifa_unmapped_title" to mapOf(
            AppLanguage.ENGLISH to "No Specific Verified Islamic Evidence Found",
            AppLanguage.URDU to "کوئی مخصوص تصدیق شدہ شرعی دلیل موجود نہیں",
            AppLanguage.ARABIC to "لا يوجد دليل خاص بهذه الحالة في المصادر الموثقة",
            AppLanguage.HINDI to "इस स्थिति के लिए कोई विशेष प्रमाणित दलील नहीं मिली"
        ),
        "shifa_unmapped_localized_note" to mapOf(
            AppLanguage.ENGLISH to "No specific verified Quranic verse or Masnoon treatment was found for this condition. Explore General Quranic Shifa and Authentic Ruqyah below.",
            AppLanguage.URDU to "اس کیفیت کے لیے کوئی مخصوص قرآنی آیت یا مسنون علاج ہمارے مستند ماخذ میں ثابت نہیں ملا؛ آپ نیچے عمومی شفائے قرآن اور مسنون رقیہ دیکھ سکتے ہیں۔",
            AppLanguage.ARABIC to "لم يثبت نص خاص لهذه الحالة؛ يمكنك الاطلاع على آيات الشفاء العامة والرقية الشرعية الثابتة أدناه.",
            AppLanguage.HINDI to "इस स्थिति के लिए हमारे प्रमाणित स्रोतों में कोई विशेष आयत या मसनून उपचार साबित नहीं मिला। आप नीचे सामान्य क़ुरआनी शिफ़ा व रुक़या देख सकते हैं।"
        ),
        "shifa_cat_quranic" to mapOf(
            AppLanguage.ENGLISH to "Quranic Shifa",
            AppLanguage.URDU to "شفائے قرآن",
            AppLanguage.ARABIC to "الشفاء في القرآن",
            AppLanguage.HINDI to "क़ुरआनी शिफ़ा"
        ),
        "shifa_cat_ruqyah" to mapOf(
            AppLanguage.ENGLISH to "Ruqyah Shar'iyyah",
            AppLanguage.URDU to "رقیہ شرعیہ",
            AppLanguage.ARABIC to "الرقية الشرعية",
            AppLanguage.HINDI to "रुक़या शरइय्या"
        ),
        "shifa_action_favorite" to mapOf(
            AppLanguage.ENGLISH to "Favorite",
            AppLanguage.URDU to "پسندیدہ",
            AppLanguage.ARABIC to "تفضيل",
            AppLanguage.HINDI to "पसंदीदा"
        ),
        "shifa_action_bookmark" to mapOf(
            AppLanguage.ENGLISH to "Bookmark",
            AppLanguage.URDU to "بک مارک",
            AppLanguage.ARABIC to "حفظ",
            AppLanguage.HINDI to "बुकमार्क"
        ),
        "shifa_action_share" to mapOf(
            AppLanguage.ENGLISH to "Share",
            AppLanguage.URDU to "شیئر کریں",
            AppLanguage.ARABIC to "مشاركة",
            AppLanguage.HINDI to "साझा करें"
        ),
        "shifa_section_arabic" to mapOf(
            AppLanguage.ENGLISH to "Original Arabic Text",
            AppLanguage.URDU to "اصل عربی متن",
            AppLanguage.ARABIC to "النص الأصلي",
            AppLanguage.HINDI to "मूल अरबी पाठ"
        ),
        "shifa_action_audio" to mapOf(
            AppLanguage.ENGLISH to "Audio Recitation",
            AppLanguage.URDU to "تلاوت سنیں",
            AppLanguage.ARABIC to "التلاوة الصوتية",
            AppLanguage.HINDI to "तिलावत सुनें"
        ),
        "shifa_audio_loading" to mapOf(
            AppLanguage.ENGLISH to "Loading Verified Audio...",
            AppLanguage.URDU to "تلاوت لوڈ ہو رہی ہے...",
            AppLanguage.ARABIC to "جاري تحميل التلاوة...",
            AppLanguage.HINDI to "तिलावत लोड हो रही है..."
        ),
        "shifa_audio_stop" to mapOf(
            AppLanguage.ENGLISH to "Stop Recitation",
            AppLanguage.URDU to "تلاوت روکیں",
            AppLanguage.ARABIC to "إيقاف التلاوة",
            AppLanguage.HINDI to "तिलावत रोकें"
        ),
        "shifa_audio_play" to mapOf(
            AppLanguage.ENGLISH to "Listen to Quran Recitation",
            AppLanguage.URDU to "مستند قرآنی تلاوت سنیں",
            AppLanguage.ARABIC to "استمع للتلاوة القرآنية",
            AppLanguage.HINDI to "क़ुरआन तिलावत सुनें"
        ),
        "shifa_section_translation" to mapOf(
            AppLanguage.ENGLISH to "Translation",
            AppLanguage.URDU to "ترجمہ",
            AppLanguage.ARABIC to "البيان الميسّر",
            AppLanguage.HINDI to "अनुवाद"
        ),
        "shifa_section_tafseer" to mapOf(
            AppLanguage.ENGLISH to "Authentic Tafseer",
            AppLanguage.URDU to "مستند تفسیر",
            AppLanguage.ARABIC to "التفسير المعتمد",
            AppLanguage.HINDI to "प्रामाणिक तफ़सीर"
        ),
        "shifa_tafseer_offline_note" to mapOf(
            AppLanguage.ENGLISH to "Tap below to load full classical Tafseer online.",
            AppLanguage.URDU to "تفصیلی مستند تفسیرِ ابنِ کثیر پڑھنے کے لیے نیچے بٹن دبائیں (انٹرنیٹ درکار ہے)۔",
            AppLanguage.ARABIC to "اضغط أدناه لتحميل التفسير الكامل عبر الإنترنت.",
            AppLanguage.HINDI to "विस्तृत तफ़सीर ऑनलाइन पढ़ने के लिए नीचे दबाएँ।"
        ),
        "shifa_load_online_tafseer" to mapOf(
            AppLanguage.ENGLISH to "Load Full Scholarly Tafseer Online",
            AppLanguage.URDU to "مکمل تفسیر آن لائن لوڈ کریں",
            AppLanguage.ARABIC to "تحميل التفسير الموسوعي أونلاين",
            AppLanguage.HINDI to "पूर्ण तफ़सीर ऑनलाइन लोड करें"
        ),
        "shifa_section_context" to mapOf(
            AppLanguage.ENGLISH to "Islamic Context & Guidance",
            AppLanguage.URDU to "شرعی سیاق و سباق اور رہنمائی",
            AppLanguage.ARABIC to "السياق الشرعي والفهم الصحيح",
            AppLanguage.HINDI to "इस्लामिक संदर्भ व मार्गदर्शन"
        ),
        "shifa_prophetic_action_heading" to mapOf(
            AppLanguage.ENGLISH to "Prophet's ﷺ Established Action & Wording",
            AppLanguage.URDU to "نبی کریم ﷺ کا ثابت شدہ عمل اور الفاظ",
            AppLanguage.ARABIC to "ما ثبت عن النبي ﷺ نصاً وفعلاً",
            AppLanguage.HINDI to "नबी ﷺ का प्रमाणित अमल व कथन"
        ),
        "shifa_scholar_explanation_heading" to mapOf(
            AppLanguage.ENGLISH to "Classical Scholars' Explanation",
            AppLanguage.URDU to "اہلِ علم اور شارحینِ حدیث کی تشریح",
            AppLanguage.ARABIC to "شرح العلماء وأئمة الحديث",
            AppLanguage.HINDI to "उलमा व मुहद्दिसीन की व्याख्या"
        ),
        "shifa_recitation_method_heading" to mapOf(
            AppLanguage.ENGLISH to "Sunnah Recitation Method & Proof",
            AppLanguage.URDU to "مسنون طریقہ اور ثابت شدہ تعداد",
            AppLanguage.ARABIC to "صفة الرقية والعدد الثابت",
            AppLanguage.HINDI to "मसनून तरीक़ा व प्रमाणित संख्या"
        ),
        "shifa_verified_references" to mapOf(
            AppLanguage.ENGLISH to "Verified Primary References (Tap to Open)",
            AppLanguage.URDU to "تصدیق شدہ حوالہ جات (کھولنے کے لیے ٹیپ کریں)",
            AppLanguage.ARABIC to "المصادر والمراجع الموثقة",
            AppLanguage.HINDI to "प्रमाणित संदर्भ (खोलने के लिए टैप करें)"
        ),
        "shifa_search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search Shifa, Bukhar, Nazar, Waswase, Sihr...",
            AppLanguage.URDU to "شفا، بخار، نظر، وسوسے، سحر تلاش کریں...",
            AppLanguage.ARABIC to "ابحث عن الشفاء أو الرقية أو الحالة...",
            AppLanguage.HINDI to "शिफ़ा, बुख़ार, नज़र, वसवसे, सिह्र खोजें..."
        ),
        "shifa_filter_all" to mapOf(
            AppLanguage.ENGLISH to "All",
            AppLanguage.URDU to "سب",
            AppLanguage.ARABIC to "الكل",
            AppLanguage.HINDI to "सभी"
        ),
        "shifa_filter_favorites" to mapOf(
            AppLanguage.ENGLISH to "Favorites",
            AppLanguage.URDU to "پسندیدہ",
            AppLanguage.ARABIC to "المفضلة",
            AppLanguage.HINDI to "पसंदीदा"
        ),
        "shifa_filter_bookmarks" to mapOf(
            AppLanguage.ENGLISH to "Saved",
            AppLanguage.URDU to "محفوظ",
            AppLanguage.ARABIC to "المحفوظة",
            AppLanguage.HINDI to "सहेजे गए"
        ),
        "shifa_index_heading" to mapOf(
            AppLanguage.ENGLISH to "Select a Condition or Search Above",
            AppLanguage.URDU to "متعلقہ کیفیت منتخب کریں یا اوپر تلاش کریں",
            AppLanguage.ARABIC to "اختر الحالة أو ابحث في الأعلى",
            AppLanguage.HINDI to "विषय चुनें या ऊपर खोजें"
        ),
        "shifa_empty_saved_msg" to mapOf(
            AppLanguage.ENGLISH to "No items saved under this filter in the active category yet.",
            AppLanguage.URDU to "اس زمرے میں ابھی کوئی آئٹم محفوظ نہیں کیا گیا۔",
            AppLanguage.ARABIC to "لا توجد عناصر محفوظة في هذا القسم بعد.",
            AppLanguage.HINDI to "इस श्रेणी में अभी तक कोई सामग्री सहेजी नहीं गई है।"
        ),
        "shifa_online_tafseer_error" to mapOf(
            AppLanguage.ENGLISH to "Unable to load online Tafseer right now. Please check your connection.",
            AppLanguage.URDU to "اس وقت آن لائن تفسیر لوڈ نہیں ہو سکی؛ براہِ کرم انٹرنیٹ چیک کریں۔",
            AppLanguage.ARABIC to "تعذر تحميل التفسير أونلاين حالياً؛ يرجى التحقق من الاتصال.",
            AppLanguage.HINDI to "अभी ऑनलाइन तफ़सीर लोड नहीं हो सकी; कृपया इंटरनेट कनेक्शन जांचें।"
        )
    )
}
