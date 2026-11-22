package com.example.localization

import com.example.data.model.AppLanguage

object AppLocalization {

    fun get(key: String, language: AppLanguage): String {
        val map = strings[key] ?: HuqooqLocalization.strings[key] ?: ShifaLocalization.strings[key]
        return map?.get(language) ?: map?.get(AppLanguage.ENGLISH) ?: key
    }

    private val strings: Map<String, Map<AppLanguage, String>> by lazy {
        HashMap<String, Map<AppLanguage, String>>(2048).apply {
            putAll(stringsPart1())
            putAll(stringsPart2())
            putAll(stringsPart3())
        }
    }

    private fun stringsPart1(): Map<String, Map<AppLanguage, String>> = mapOf(
        "app_name" to mapOf(
            AppLanguage.ENGLISH to "Noor-e-Hidayat",
            AppLanguage.URDU to "نورِ ہدایت",
            AppLanguage.ARABIC to "نور الهداية",
            AppLanguage.HINDI to "नूर-ए-हिदायत"
        ),
        "app_tagline" to mapOf(
            AppLanguage.ENGLISH to "Your Authentic Islamic Companion",
            AppLanguage.URDU to "آپ کا مستند اور بااعتماد اسلامی رفیق",
            AppLanguage.ARABIC to "رفيقك الإسلامي الموثوق",
            AppLanguage.HINDI to "आपका प्रामाणिक इस्लामिक साथी"
        ),
        "home" to mapOf(
            AppLanguage.ENGLISH to "Home",
            AppLanguage.URDU to "صفحہ اول",
            AppLanguage.ARABIC to "الرئيسية",
            AppLanguage.HINDI to "होम"
        ),
        "quran" to mapOf(
            AppLanguage.ENGLISH to "Al-Quran",
            AppLanguage.URDU to "القرآن الکریم",
            AppLanguage.ARABIC to "القرآن الكريم",
            AppLanguage.HINDI to "अल-क़ुरआन"
        ),
        "live_stream" to mapOf(
            AppLanguage.ENGLISH to "Live Makkah & Madinah",
            AppLanguage.URDU to "لائیو مکہ و مدینہ",
            AppLanguage.ARABIC to "بث مباشر مكة والمدينة",
            AppLanguage.HINDI to "लाइव मक्का और मदीना"
        ),
        "lectures" to mapOf(
            AppLanguage.ENGLISH to "Islamic Lectures",
            AppLanguage.URDU to "اسلامی بیانات و دروس",
            AppLanguage.ARABIC to "المحاضرات والدروس الإسلامية",
            AppLanguage.HINDI to "इस्लामिक व्याख्यान"
        ),
        "learning" to mapOf(
            AppLanguage.ENGLISH to "Quran Learning",
            AppLanguage.URDU to "تعلیم القرآن و تجوید",
            AppLanguage.ARABIC to "تعليم القرآن والتجويد",
            AppLanguage.HINDI to "क़ुरआन शिक्षण"
        ),
        "more_options" to mapOf(
            AppLanguage.ENGLISH to "More Options",
            AppLanguage.URDU to "مزید سہولیات و خصوصیات",
            AppLanguage.ARABIC to "المزيد من الخدمات",
            AppLanguage.HINDI to "अधिक विकल्प"
        ),
        "hadith" to mapOf(
            AppLanguage.ENGLISH to "Hadith Collection",
            AppLanguage.URDU to "کتبِ احادیث مبارکہ",
            AppLanguage.ARABIC to "موسوعة الحديث الشريف",
            AppLanguage.HINDI to "हदीस संग्रह"
        ),
        "tafseer" to mapOf(
            AppLanguage.ENGLISH to "Tafseer Library",
            AppLanguage.URDU to "مکتبۂ تفاسیر",
            AppLanguage.ARABIC to "مكتبة التفاسير",
            AppLanguage.HINDI to "तफ़सीर लाइब्रेरी"
        ),
        "duas" to mapOf(
            AppLanguage.ENGLISH to "Islamic Duas",
            AppLanguage.URDU to "مسنون و قرآنی دعائیں",
            AppLanguage.ARABIC to "الأدعية والأذكار",
            AppLanguage.HINDI to "इस्लामिक दुआएं"
        ),
        "tasbeeh" to mapOf(
            AppLanguage.ENGLISH to "Tasbeeh Counter",
            AppLanguage.URDU to "ڈیجیٹل تسبیح کاؤنٹر",
            AppLanguage.ARABIC to "المسبحة الإلكترونية",
            AppLanguage.HINDI to "तस्बीह काउंटर"
        ),
        "prayer_times" to mapOf(
            AppLanguage.ENGLISH to "Prayer Times",
            AppLanguage.URDU to "اوقاتِ نماز",
            AppLanguage.ARABIC to "مواقيت الصلاة",
            AppLanguage.HINDI to "नमाज़ के औक़ात"
        ),
        "azan_notifications" to mapOf(
            AppLanguage.ENGLISH to "Azan Notifications",
            AppLanguage.URDU to "اذان الرٹس و اطلاعات",
            AppLanguage.ARABIC to "تنبيهات الأذان",
            AppLanguage.HINDI to "अज़ान सूचनाएं"
        ),
        "qibla" to mapOf(
            AppLanguage.ENGLISH to "Qibla Finder",
            AppLanguage.URDU to "قبلہ نما (قطب نما)",
            AppLanguage.ARABIC to "بوصلة القبلة",
            AppLanguage.HINDI to "क़िबला कम्पास"
        ),
        "zakat" to mapOf(
            AppLanguage.ENGLISH to "Zakat Calculator",
            AppLanguage.URDU to "زکوٰۃ کیلکولیٹر",
            AppLanguage.ARABIC to "حاسبة الزكاة",
            AppLanguage.HINDI to "ज़कात कैलकुलेटर"
        ),
        "calendar" to mapOf(
            AppLanguage.ENGLISH to "Hijri Calendar",
            AppLanguage.URDU to "ہجری تقویم",
            AppLanguage.ARABIC to "التقويم الهجري",
            AppLanguage.HINDI to "हिजरी कैलेंडर"
        ),
        "podcasts" to mapOf(
            AppLanguage.ENGLISH to "Islamic Podcasts",
            AppLanguage.URDU to "اسلامی پوڈکاسٹس",
            AppLanguage.ARABIC to "بودكاست إسلامي",
            AppLanguage.HINDI to "इस्लामिक पॉडकास्ट"
        ),
        "bookmarks" to mapOf(
            AppLanguage.ENGLISH to "Bookmarks",
            AppLanguage.URDU to "محفوظ شدہ (بک مارکس)",
            AppLanguage.ARABIC to "الإشارات المرجعية",
            AppLanguage.HINDI to "बुकमार्क"
        ),
        "downloads" to mapOf(
            AppLanguage.ENGLISH to "Downloaded Content",
            AppLanguage.URDU to "ڈاؤنلوڈ شدہ مواد",
            AppLanguage.ARABIC to "التنزيلات",
            AppLanguage.HINDI to "डाउनलोड किया गया"
        ),
        "settings" to mapOf(
            AppLanguage.ENGLISH to "App Settings",
            AppLanguage.URDU to "ترتیبات",
            AppLanguage.ARABIC to "الإعدادات",
            AppLanguage.HINDI to "सेटिंग्स"
        ),
        "next_prayer" to mapOf(
            AppLanguage.ENGLISH to "Next Prayer",
            AppLanguage.URDU to "اگلی نماز",
            AppLanguage.ARABIC to "الصلاة القادمة",
            AppLanguage.HINDI to "अगली नमाज़"
        ),
        "fajr" to mapOf(
            AppLanguage.ENGLISH to "Fajr",
            AppLanguage.URDU to "فجر",
            AppLanguage.ARABIC to "الفجر",
            AppLanguage.HINDI to "फ़ज्र"
        ),
        "sunrise" to mapOf(
            AppLanguage.ENGLISH to "Sunrise",
            AppLanguage.URDU to "طلوعِ آفتاب",
            AppLanguage.ARABIC to "الشروق",
            AppLanguage.HINDI to "शुरूक़ (सूर्योदय)"
        ),
        "dhuhr" to mapOf(
            AppLanguage.ENGLISH to "Dhuhr",
            AppLanguage.URDU to "ظہر",
            AppLanguage.ARABIC to "الظهر",
            AppLanguage.HINDI to "ज़ुहर"
        ),
        "asr" to mapOf(
            AppLanguage.ENGLISH to "Asr",
            AppLanguage.URDU to "عصر",
            AppLanguage.ARABIC to "العصر",
            AppLanguage.HINDI to "अस्र"
        ),
        "maghrib" to mapOf(
            AppLanguage.ENGLISH to "Maghrib",
            AppLanguage.URDU to "مغرب",
            AppLanguage.ARABIC to "المغرب",
            AppLanguage.HINDI to "मग़रिब"
        ),
        "isha" to mapOf(
            AppLanguage.ENGLISH to "Isha",
            AppLanguage.URDU to "عشاء",
            AppLanguage.ARABIC to "العشاء",
            AppLanguage.HINDI to "इशा"
        ),
        "surah" to mapOf(
            AppLanguage.ENGLISH to "Surah",
            AppLanguage.URDU to "سورۃ",
            AppLanguage.ARABIC to "سورة",
            AppLanguage.HINDI to "सूरह"
        ),
        "para" to mapOf(
            AppLanguage.ENGLISH to "Para (Juz)",
            AppLanguage.URDU to "پارہ (جزء)",
            AppLanguage.ARABIC to "جزء",
            AppLanguage.HINDI to "पारा (जुज़)"
        ),
        "makki" to mapOf(
            AppLanguage.ENGLISH to "Makki",
            AppLanguage.URDU to "مکی",
            AppLanguage.ARABIC to "مكية",
            AppLanguage.HINDI to "मक्की"
        ),
        "madani" to mapOf(
            AppLanguage.ENGLISH to "Madani",
            AppLanguage.URDU to "مدنی",
            AppLanguage.ARABIC to "مدنية",
            AppLanguage.HINDI to "मदनी"
        ),
        "ayah_count" to mapOf(
            AppLanguage.ENGLISH to "Verses",
            AppLanguage.URDU to "آیات",
            AppLanguage.ARABIC to "آيات",
            AppLanguage.HINDI to "आयात"
        ),
        "translation_btn" to mapOf(
            AppLanguage.ENGLISH to "Translation",
            AppLanguage.URDU to "ترجمہ",
            AppLanguage.ARABIC to "الترجمة",
            AppLanguage.HINDI to "अनुवाद (तर्जुमा)"
        ),
        "tafseer_btn" to mapOf(
            AppLanguage.ENGLISH to "Tafseer",
            AppLanguage.URDU to "تفسیر",
            AppLanguage.ARABIC to "التفسير",
            AppLanguage.HINDI to "तफ़सीर (विस्तार)"
        ),
        "reciter" to mapOf(
            AppLanguage.ENGLISH to "Reciter",
            AppLanguage.URDU to "قاری",
            AppLanguage.ARABIC to "القارئ",
            AppLanguage.HINDI to "क़ारी"
        ),
        "audio_download" to mapOf(
            AppLanguage.ENGLISH to "Download Audio",
            AppLanguage.URDU to "آڈیو ڈاؤنلوڈ کریں",
            AppLanguage.ARABIC to "تحميل التلاوة",
            AppLanguage.HINDI to "ऑडियो डाउनलोड करें"
        ),
        "copied" to mapOf(
            AppLanguage.ENGLISH to "Ayah copied to clipboard",
            AppLanguage.URDU to "آیت کاپی کر لی گئی",
            AppLanguage.ARABIC to "تم نسخ الآية",
            AppLanguage.HINDI to "आयत कॉपी कर ली गई"
        ),
        "bookmarked" to mapOf(
            AppLanguage.ENGLISH to "Saved to bookmarks",
            AppLanguage.URDU to "بک مارک میں شامل ہو گیا",
            AppLanguage.ARABIC to "تمت الإضافة للمفضلة",
            AppLanguage.HINDI to "बुकमार्क में सहेजा गया"
        ),
        "search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search by name or number...",
            AppLanguage.URDU to "نام یا نمبر کے ذریعے تلاش کریں...",
            AppLanguage.ARABIC to "ابحث بالاسم أو الرقم...",
            AppLanguage.HINDI to "नाम या संख्या द्वारा खोजें..."
        ),
        "theme_selector" to mapOf(
            AppLanguage.ENGLISH to "Select Theme",
            AppLanguage.URDU to "تھیم منتخب کریں",
            AppLanguage.ARABIC to "اختر المظهر",
            AppLanguage.HINDI to "थीम चुनें"
        ),
        "language_selector" to mapOf(
            AppLanguage.ENGLISH to "Select Language",
            AppLanguage.URDU to "زبان کا انتخاب",
            AppLanguage.ARABIC to "اختر اللغة",
            AppLanguage.HINDI to "भाषा चुनें"
        ),
        "font_size" to mapOf(
            AppLanguage.ENGLISH to "Arabic Font Size",
            AppLanguage.URDU to "عربی فونٹ سائز",
            AppLanguage.ARABIC to "حجم الخط القرآني",
            AppLanguage.HINDI to "अरबी फ़ॉन्ट आकार"
        ),
        "live_makkah_title" to mapOf(
            AppLanguage.ENGLISH to "Makkah Live (Masjid al-Haram)",
            AppLanguage.URDU to "مکہ مکرمہ لائیو (مسجد الحرام)",
            AppLanguage.ARABIC to "بث مباشر مكة المكرمة (المسجد الحرام)",
            AppLanguage.HINDI to "मक्का लाइव (मस्जिद अल-हरम)"
        ),
        "live_madinah_title" to mapOf(
            AppLanguage.ENGLISH to "Madinah Live (Masjid an-Nabawi)",
            AppLanguage.URDU to "مدینہ منورہ لائیو (مسجد نبوی ﷺ)",
            AppLanguage.ARABIC to "بث مباشر المدينة المنورة (المسجد النبوي)",
            AppLanguage.HINDI to "मदीना लाइव (मस्जिद अन-नबवी)"
        ),
        "official_source" to mapOf(
            AppLanguage.ENGLISH to "Official Saudi Broadcast Channel",
            AppLanguage.URDU to "سرکاری سعودی نشریاتی چینل",
            AppLanguage.ARABIC to "القناة الرسمية لهيئة البث والإذاعة السعودية",
            AppLanguage.HINDI to "आधिकारिक सऊदी प्रसारण चैनल"
        ),
        "bukhari" to mapOf(
            AppLanguage.ENGLISH to "Sahih al-Bukhari",
            AppLanguage.URDU to "صحیح البخاری",
            AppLanguage.ARABIC to "صحيح البخاري",
            AppLanguage.HINDI to "सहीह अल-बुख़ारी"
        ),
        "muslim" to mapOf(
            AppLanguage.ENGLISH to "Sahih Muslim",
            AppLanguage.URDU to "صحیح مسلم",
            AppLanguage.ARABIC to "صحيح مسلم",
            AppLanguage.HINDI to "सहीह मुस्लिम"
        ),
        "qibla_compass_bearing" to mapOf(
            AppLanguage.ENGLISH to "Qibla Bearing",
            AppLanguage.URDU to "قبلہ زاویہ",
            AppLanguage.ARABIC to "زاوية القبلة",
            AppLanguage.HINDI to "क़िबला कोण"
        ),
        "zakat_disclaimer" to mapOf(
            AppLanguage.ENGLISH to "Note: This calculator assists in computing your estimated Zakat (standard 2.5% on qualifying wealth above Nisab). For complex business structures or personalized rulings, consult a qualified Islamic scholar.",
            AppLanguage.URDU to "وضاحت: یہ کیلکولیٹر نصاب سے زائد قابل زکوٰۃ مال پر مروجہ 2.5 فیصد کے مطابق تخمینہ لگاتا ہے۔ تفصیلی اور پیچیدہ مالی مسائل میں مستند علمائے کرام سے رہنمائی لیں۔",
            AppLanguage.ARABIC to "ملاحظة: تساعد هذه الحاسبة في تقدير زكاتك (2.5٪ من المال البالغ للنصاب). للحالات المعقدة، يُرجى استشارة عالم شرعي معتمد.",
            AppLanguage.HINDI to "नोट: यह कैलकुलेटर निसाब से अधिक पात्र संपत्ति पर 2.5% की दर से आपकी अनुमानित ज़कात की गणना करता है। व्यक्तिगत मसलों के लिए किसी योग्य विद्वान से परामर्श करें।"
        ),
        "calculate" to mapOf(
            AppLanguage.ENGLISH to "Calculate Zakat",
            AppLanguage.URDU to "زکوٰۃ کا حساب لگائیں",
            AppLanguage.ARABIC to "احسب الزكاة",
            AppLanguage.HINDI to "ज़कात की गणना करें"
        ),
        "offline_ready" to mapOf(
            AppLanguage.ENGLISH to "Available Offline",
            AppLanguage.URDU to "آف لائن دستیاب",
            AppLanguage.ARABIC to "متاح دون اتصال",
            AppLanguage.HINDI to "ऑफ़लाइन उपलब्ध"
        ),
        "tap_to_count" to mapOf(
            AppLanguage.ENGLISH to "TAP TO COUNT",
            AppLanguage.URDU to "شمار کرنے کے لیے چھوئیں",
            AppLanguage.ARABIC to "اضغط للتسبيح",
            AppLanguage.HINDI to "गिनने के लिए टैप करें"
        ),
        "target" to mapOf(
            AppLanguage.ENGLISH to "Target",
            AppLanguage.URDU to "ہدف",
            AppLanguage.ARABIC to "الهدف",
            AppLanguage.HINDI to "लक्ष्य"
        ),
        "target_completed" to mapOf(
            AppLanguage.ENGLISH to "Target Completed!",
            AppLanguage.URDU to "ماشاء اللہ! ہدف مکمل ہوا",
            AppLanguage.ARABIC to "ما شاء الله! اكتمل الهدف",
            AppLanguage.HINDI to "माशाअल्लाह! लक्ष्य पूरा हुआ"
        ),
        "rounds" to mapOf(
            AppLanguage.ENGLISH to "Round",
            AppLanguage.URDU to "چکر",
            AppLanguage.ARABIC to "الدورة",
            AppLanguage.HINDI to "राउंड"
        ),
        "undo" to mapOf(
            AppLanguage.ENGLISH to "Undo",
            AppLanguage.URDU to "ایک عدد واپس",
            AppLanguage.ARABIC to "تراجع",
            AppLanguage.HINDI to "वापस लें"
        ),
        "reset" to mapOf(
            AppLanguage.ENGLISH to "Reset",
            AppLanguage.URDU to "دوبارہ شروع",
            AppLanguage.ARABIC to "إعادة ضبط",
            AppLanguage.HINDI to "रीसेट"
        ),
        "sound" to mapOf(
            AppLanguage.ENGLISH to "Sound",
            AppLanguage.URDU to "آواز",
            AppLanguage.ARABIC to "الصوت",
            AppLanguage.HINDI to "ध्वनि"
        ),
        "vibration" to mapOf(
            AppLanguage.ENGLISH to "Vibration",
            AppLanguage.URDU to "ارتعاش",
            AppLanguage.ARABIC to "الاهتزاز",
            AppLanguage.HINDI to "कंपन"
        ),
        "screen_awake" to mapOf(
            AppLanguage.ENGLISH to "Screen Awake",
            AppLanguage.URDU to "اسکرین آن رکھیں",
            AppLanguage.ARABIC to "إبقاء الشاشة مفعلة",
            AppLanguage.HINDI to "स्क्रीन चालू रखें"
        ),
        "history" to mapOf(
            AppLanguage.ENGLISH to "History",
            AppLanguage.URDU to "تاریخچہ",
            AppLanguage.ARABIC to "السجل",
            AppLanguage.HINDI to "इतिहास"
        ),
        "statistics" to mapOf(
            AppLanguage.ENGLISH to "Statistics",
            AppLanguage.URDU to "اعداد و شمار",
            AppLanguage.ARABIC to "الإحصائيات",
            AppLanguage.HINDI to "सांख्यिकी"
        ),
        "today_total" to mapOf(
            AppLanguage.ENGLISH to "Today's Total",
            AppLanguage.URDU to "آج کا کل شمار",
            AppLanguage.ARABIC to "مجموع اليوم",
            AppLanguage.HINDI to "आज का कुल"
        ),
        "all_time_total" to mapOf(
            AppLanguage.ENGLISH to "All-Time Total",
            AppLanguage.URDU to "ہمیشہ کا کل شمار",
            AppLanguage.ARABIC to "الإجمالي الكلي",
            AppLanguage.HINDI to "कुल पाठ"
        ),
        "custom_target" to mapOf(
            AppLanguage.ENGLISH to "Custom Target",
            AppLanguage.URDU to "اپنی مرضی کا ہدف",
            AppLanguage.ARABIC to "هدف مخصص",
            AppLanguage.HINDI to "कस्टम लक्ष्य"
        ),
        "add_custom_dhikr" to mapOf(
            AppLanguage.ENGLISH to "Add Custom Zikr",
            AppLanguage.URDU to "نیا ذکر شامل کریں",
            AppLanguage.ARABIC to "إضافة ذكر مخصص",
            AppLanguage.HINDI to "नया ज़िक्र जोड़ें"
        ),
        "change_dhikr" to mapOf(
            AppLanguage.ENGLISH to "Change Zikr",
            AppLanguage.URDU to "ذکر تبدیل کریں",
            AppLanguage.ARABIC to "تغيير الذكر",
            AppLanguage.HINDI to "ज़िक्र बदलें"
        ),
        "save_session" to mapOf(
            AppLanguage.ENGLISH to "Save Session",
            AppLanguage.URDU to "شمار محفوظ کریں",
            AppLanguage.ARABIC to "حفظ الجلسة",
            AppLanguage.HINDI to "सत्र सहेजें"
        ),
        "search_duas_hint" to mapOf(
            AppLanguage.ENGLISH to "Search authentic duas, topics or Arabic...",
            AppLanguage.URDU to "مسنون دعائیں، الفاظ یا عربی تلاش کریں...",
            AppLanguage.ARABIC to "ابحث في الأدعية المأثورة أو الكلمات...",
            AppLanguage.HINDI to "प्रमाणिक दुआएं, विषय या अरबी खोजें..."
        ),
        "favorite_duas" to mapOf(
            AppLanguage.ENGLISH to "Favorites",
            AppLanguage.URDU to "پسندیدہ دعائیں",
            AppLanguage.ARABIC to "الأدعية المفضلة",
            AppLanguage.HINDI to "पसंदीदा दुआएं"
        ),
        "recent_duas" to mapOf(
            AppLanguage.ENGLISH to "Recently Viewed",
            AppLanguage.URDU to "حالیہ دیکھی گئی دعائیں",
            AppLanguage.ARABIC to "شوهدت مؤخراً",
            AppLanguage.HINDI to "हाल ही में देखी गई"
        ),
        "daily_featured_dua" to mapOf(
            AppLanguage.ENGLISH to "Daily Authentic Dua",
            AppLanguage.URDU to "آج کی منتخب مسنون دعا",
            AppLanguage.ARABIC to "دعاء اليوم المأثور",
            AppLanguage.HINDI to "आज की चयनित मसनून दुआ"
        ),
        "all_duas" to mapOf(
            AppLanguage.ENGLISH to "All Duas",
            AppLanguage.URDU to "تمام دعائیں",
            AppLanguage.ARABIC to "جميع الأدعية",
            AppLanguage.HINDI to "सभी दुआएं"
        ),
        "arabic_font_size" to mapOf(
            AppLanguage.ENGLISH to "Arabic Font Size",
            AppLanguage.URDU to "عربی فونٹ کا سائز",
            AppLanguage.ARABIC to "حجم الخط العربي",
            AppLanguage.HINDI to "अरबी फ़ॉन्ट का आकार"
        ),
        "play_recitation" to mapOf(
            AppLanguage.ENGLISH to "Play Audio",
            AppLanguage.URDU to "تلاوت سنیں",
            AppLanguage.ARABIC to "استماع للتلاوة",
            AppLanguage.HINDI to "ऑडियो सुनें"
        ),
        "pause_recitation" to mapOf(
            AppLanguage.ENGLISH to "Pause",
            AppLanguage.URDU to "وقفہ کریں",
            AppLanguage.ARABIC to "إيقاف مؤقت",
            AppLanguage.HINDI to "रोकें"
        ),
        "repeat_playback" to mapOf(
            AppLanguage.ENGLISH to "Repeat",
            AppLanguage.URDU to "تکرار",
            AppLanguage.ARABIC to "تكرار",
            AppLanguage.HINDI to "दोहराएं"
        ),
        "hadith_authenticity" to mapOf(
            AppLanguage.ENGLISH to "Authenticity Grade",
            AppLanguage.URDU to "درجۂ صحت",
            AppLanguage.ARABIC to "درجة الحديث",
            AppLanguage.HINDI to "प्रमाणिकता श्रेणी"
        ),
        "reference_source" to mapOf(
            AppLanguage.ENGLISH to "Source Reference",
            AppLanguage.URDU to "مستند حوالہ",
            AppLanguage.ARABIC to "المصدر والتخريج",
            AppLanguage.HINDI to "प्रमाणिक संदर्भ"
        ),
        "recommended_time" to mapOf(
            AppLanguage.ENGLISH to "Occasion & Time",
            AppLanguage.URDU to "موقع و مناسب وقت",
            AppLanguage.ARABIC to "المناسبة والوقت",
            AppLanguage.HINDI to "अवसर और समय"
        ),
        "virtues_benefits" to mapOf(
            AppLanguage.ENGLISH to "Virtues & Benefits",
            AppLanguage.URDU to "فضائل و برکات",
            AppLanguage.ARABIC to "الفضائل والأجر",
            AppLanguage.HINDI to "फ़ज़ीलत और लाभ"
        ),
        "copy_dua" to mapOf(
            AppLanguage.ENGLISH to "Copy Dua",
            AppLanguage.URDU to "دعا کاپی کریں",
            AppLanguage.ARABIC to "نسخ الدعاء",
            AppLanguage.HINDI to "दुआ कॉपी करें"
        ),
        "share_dua" to mapOf(
            AppLanguage.ENGLISH to "Share Dua",
            AppLanguage.URDU to "دعا شیئر کریں",
            AppLanguage.ARABIC to "مشاركة الدعاء",
            AppLanguage.HINDI to "दुआ शेयर करें"
        ),
        "dua_added_fav" to mapOf(
            AppLanguage.ENGLISH to "Added to Favorites",
            AppLanguage.URDU to "پسندیدہ میں شامل کر دیا گیا",
            AppLanguage.ARABIC to "تمت الإضافة إلى المفضلة",
            AppLanguage.HINDI to "पसंदीदा में जोड़ा गया"
        ),
        "dua_removed_fav" to mapOf(
            AppLanguage.ENGLISH to "Removed from Favorites",
            AppLanguage.URDU to "پسندیدہ سے ہٹا دیا گیا",
            AppLanguage.ARABIC to "تم الحذف من المفضلة",
            AppLanguage.HINDI to "पसंदीदा से हटाया गया"
        ),
        "dua_copied" to mapOf(
            AppLanguage.ENGLISH to "Dua copied to clipboard",
            AppLanguage.URDU to "دعا کلپ بورڈ پر کاپی ہو گئی",
            AppLanguage.ARABIC to "تم نسخ الدعاء إلى الحافظة",
            AppLanguage.HINDI to "दुआ क्लिपबोर्ड पर कॉपी हो गई"
        ),
        // Qibla Finder Localization Keys
        "qibla_perm_title" to mapOf(
            AppLanguage.ENGLISH to "Location Permission Needed",
            AppLanguage.URDU to "مقام کی اجازت درکار ہے",
            AppLanguage.ARABIC to "مطلوب إذن الموقع",
            AppLanguage.HINDI to "स्थान अनुमति आवश्यक है"
        ),
        "qibla_perm_desc" to mapOf(
            AppLanguage.ENGLISH to "Your location is used offline on this device to calculate the Qibla direction and distance to the Kaaba.",
            AppLanguage.URDU to "آپ کے موجودہ مقام کی بنیاد پر آف لائن قبلہ رخ اور کعبہ کا فاصلہ معلوم کرنے کے لیے لوکیشن کی اجازت درکار ہے۔",
            AppLanguage.ARABIC to "يُستخدم موقعك محلياً على جهازك لحساب اتجاه القبلة والمسافة إلى الكعبة المشرفة دون إرساله لأي خادم.",
            AppLanguage.HINDI to "क़िबला दिशा और काबा की दूरी की ऑफ़लाइन गणना के लिए आपके स्थान की अनुमति आवश्यक है।"
        ),
        "qibla_perm_allow" to mapOf(
            AppLanguage.ENGLISH to "Allow",
            AppLanguage.URDU to "اجازت دیں",
            AppLanguage.ARABIC to "سماح",
            AppLanguage.HINDI to "अनुमति दें"
        ),
        "qibla_perm_denied_toast" to mapOf(
            AppLanguage.ENGLISH to "Location permission denied. Select a city or enter coordinates manually.",
            AppLanguage.URDU to "مقام کی اجازت نہیں ملی۔ آپ دستی طور پر شہر منتخب کر سکتے ہیں۔",
            AppLanguage.ARABIC to "تم رفض إذن الموقع. يمكنك اختيار المدينة أو إدخال الإحداثيات يدوياً.",
            AppLanguage.HINDI to "स्थान अनुमति अस्वीकृत। आप मैन्युअल रूप से शहर चुन सकते हैं।"
        ),
        "qibla_gps_off_title" to mapOf(
            AppLanguage.ENGLISH to "Device Location (GPS) is OFF",
            AppLanguage.URDU to "موبائل لوکیشن (GPS) بند ہے",
            AppLanguage.ARABIC to "خدمة الموقع (GPS) متوقفة",
            AppLanguage.HINDI to "डिवाइस लोकेशन (GPS) बंद है"
        ),
        "qibla_gps_off_desc" to mapOf(
            AppLanguage.ENGLISH to "Turn ON location services to refresh GPS coordinates automatically, or use your saved city.",
            AppLanguage.URDU to "اپنا مقام خودکار طور پر معلوم کرنے کے لیے موبائل لوکیشن آن کریں، یا محفوظ کردہ شہر استعمال کریں۔",
            AppLanguage.ARABIC to "شغّل خدمة الموقع لتحديث إحداثياتك تلقائياً، أو استخدم المدينة المحفوظة.",
            AppLanguage.HINDI to "अपना स्थान स्वतः अपडेट करने के लिए लोकेशन चालू करें, या सहेजे गए शहर का उपयोग करें।"
        ),
        "qibla_gps_turn_on" to mapOf(
            AppLanguage.ENGLISH to "Turn ON",
            AppLanguage.URDU to "آن کریں",
            AppLanguage.ARABIC to "تشغيل",
            AppLanguage.HINDI to "चालू करें"
        ),
        "qibla_update_gps_cd" to mapOf(
            AppLanguage.ENGLISH to "Refresh GPS Location",
            AppLanguage.URDU to "جی پی ایس مقام تازہ کریں",
            AppLanguage.ARABIC to "تحديث موقع GPS",
            AppLanguage.HINDI to "GPS स्थान ताज़ा करें"
        ),
        "qibla_change_location" to mapOf(
            AppLanguage.ENGLISH to "Change",
            AppLanguage.URDU to "تبدیل کریں",
            AppLanguage.ARABIC to "تغيير",
            AppLanguage.HINDI to "बदलें"
        ),
        "qibla_source_gps" to mapOf(
            AppLanguage.ENGLISH to "GPS Location",
            AppLanguage.URDU to "جی پی ایس مقام",
            AppLanguage.ARABIC to "موقع GPS دقيق",
            AppLanguage.HINDI to "GPS स्थान"
        ),
        "qibla_source_approximate" to mapOf(
            AppLanguage.ENGLISH to "Approximate Location",
            AppLanguage.URDU to "تخمینی مقام",
            AppLanguage.ARABIC to "موقع تقريبي",
            AppLanguage.HINDI to "अनुमानित स्थान"
        ),
        "qibla_source_manual" to mapOf(
            AppLanguage.ENGLISH to "Manually Selected",
            AppLanguage.URDU to "دستی منتخب کردہ",
            AppLanguage.ARABIC to "محدد يدوياً",
            AppLanguage.HINDI to "मैन्युअल चयनित"
        ),
        "qibla_source_estimate" to mapOf(
            AppLanguage.ENGLISH to "Default / Timezone Estimate",
            AppLanguage.URDU to "ابتدائی / ٹائم زون تخمینہ",
            AppLanguage.ARABIC to "تقدير مبدئي (المنطقة الزمنية)",
            AppLanguage.HINDI to "प्रारंभिक / समयक्षेत्र अनुमान"
        ),
        "qibla_calibration_title" to mapOf(
            AppLanguage.ENGLISH to "Compass Calibration Recommended",
            AppLanguage.URDU to "کمپاس کیلیبریشن ضروری ہے",
            AppLanguage.ARABIC to "يُنصح بمعايرة البوصلة",
            AppLanguage.HINDI to "कम्पास कैलिब्रेशन आवश्यक है"
        ),
        "qibla_calibration_desc" to mapOf(
            AppLanguage.ENGLISH to "Move your phone slowly in a figure-8 pattern away from magnetic cases, metal surfaces, or electronics.",
            AppLanguage.URDU to "مقناطیسی کور یا دھاتی اشیاء سے دور رہ کر فون کو ہوا میں 8 کی شکل (figure-8) میں آہستہ گھمائیں۔",
            AppLanguage.ARABIC to "حرّك هاتفك ببطء على شكل رقم 8 بعيداً عن الأغطية المغناطيسية والمعادن لتحسين دقة المستشعر.",
            AppLanguage.HINDI to "चुंबकीय कवर या धातु की वस्तुओं से दूर फ़ोन को हवा में 8 के आकार में धीरे-धीरे घुमाएं।"
        ),
        "qibla_aligned_banner" to mapOf(
            AppLanguage.ENGLISH to "Aligned Near Qibla (~±3.5°, subject to calibration)",
            AppLanguage.URDU to "قبلہ رخ کے قریب سیدھ میں (±3.5°، سینسر کیلیبریشن سے مشروط)",
            AppLanguage.ARABIC to "محاذاة تقريبية لاتجاه القبلة (±3.5° حسب معايرة المستشعر)",
            AppLanguage.HINDI to "क़िबला की दिशा के समीप संरेखित (~±3.5°, कैलिब्रेशन पर निर्भर)"
        ),
        "qibla_angle_label" to mapOf(
            AppLanguage.ENGLISH to "Qibla Angle",
            AppLanguage.URDU to "قبلہ زاویہ",
            AppLanguage.ARABIC to "زاوية القبلة",
            AppLanguage.HINDI to "क़िबला कोण"
        ),
        "qibla_calculated_bearing" to mapOf(
            AppLanguage.ENGLISH to "Calculated Bearing",
            AppLanguage.URDU to "حسابی قبلہ زاویہ",
            AppLanguage.ARABIC to "الزاوية المحسوبة",
            AppLanguage.HINDI to "गणित क़िबला कोण"
        ),
        "qibla_calculated_only_badge" to mapOf(
            AppLanguage.ENGLISH to "Calculated Bearing Only — Not Live Compass",
            AppLanguage.URDU to "صرف حسابی زاویہ — لائیو کمپاس دستیاب نہیں",
            AppLanguage.ARABIC to "زاوية محسوبة فقط — ليست بوصلة حيّة",
            AppLanguage.HINDI to "केवल गणित कोण — लाइव कम्पास नहीं"
        ),
        "qibla_distance_label" to mapOf(
            AppLanguage.ENGLISH to "Distance to Kaaba",
            AppLanguage.URDU to "کعبہ سے فاصلہ",
            AppLanguage.ARABIC to "المسافة إلى الكعبة",
            AppLanguage.HINDI to "काबा की दूरी"
        ),
        "qibla_km_unit" to mapOf(
            AppLanguage.ENGLISH to "km",
            AppLanguage.URDU to "کلومیٹر",
            AppLanguage.ARABIC to "كم",
            AppLanguage.HINDI to "किमी"
        ),
        "qibla_metrics_title" to mapOf(
            AppLanguage.ENGLISH to "Qibla & Compass Telemetry",
            AppLanguage.URDU to "قبلہ اور کمپاس کی تفصیلات",
            AppLanguage.ARABIC to "قياسات القبلة والبوصلة",
            AppLanguage.HINDI to "क़िबला और कम्पास विवरण"
        ),
        "qibla_true_bearing" to mapOf(
            AppLanguage.ENGLISH to "Qibla Bearing (True)",
            AppLanguage.URDU to "حقیقی قبلہ زاویہ",
            AppLanguage.ARABIC to "اتجاه القبلة الحقيقي",
            AppLanguage.HINDI to "वास्तविक क़िबला कोण"
        ),
        "qibla_current_heading" to mapOf(
            AppLanguage.ENGLISH to "Current Heading",
            AppLanguage.URDU to "موجودہ رخ",
            AppLanguage.ARABIC to "الاتجاه الحالي",
            AppLanguage.HINDI to "वर्तमान दिशा"
        ),
        "qibla_difference_label" to mapOf(
            AppLanguage.ENGLISH to "Turn Needed",
            AppLanguage.URDU to "مطلوبہ موڑ",
            AppLanguage.ARABIC to "مقدار الانحراف",
            AppLanguage.HINDI to "मोड़ का अंतर"
        ),
        "qibla_turn_right" to mapOf(
            AppLanguage.ENGLISH to "%d° Right",
            AppLanguage.URDU to "%d° دائیں مڑیں",
            AppLanguage.ARABIC to "%d° يميناً",
            AppLanguage.HINDI to "%d° दाएं मुड़ें"
        ),
        "qibla_turn_left" to mapOf(
            AppLanguage.ENGLISH to "%d° Left",
            AppLanguage.URDU to "%d° بائیں مڑیں",
            AppLanguage.ARABIC to "%d° يساراً",
            AppLanguage.HINDI to "%d° बाएं मुड़ें"
        ),
        "qibla_turn_aligned" to mapOf(
            AppLanguage.ENGLISH to "Facing Qibla",
            AppLanguage.URDU to "قبلہ رخ",
            AppLanguage.ARABIC to "نحو القبلة",
            AppLanguage.HINDI to "क़िबला की ओर"
        ),
        "qibla_magnetic_declination" to mapOf(
            AppLanguage.ENGLISH to "Declination",
            AppLanguage.URDU to "مقناطیسی انحراف",
            AppLanguage.ARABIC to "الانحراف المغناطيسي",
            AppLanguage.HINDI to "चुंबकीय झुकाव"
        ),
        "qibla_sensor_mode_label" to mapOf(
            AppLanguage.ENGLISH to "Sensor Mode",
            AppLanguage.URDU to "سینسر موڈ",
            AppLanguage.ARABIC to "وضع المستشعر",
            AppLanguage.HINDI to "सेंसर मोड"
        ),
        "qibla_sensor_rotation_vector" to mapOf(
            AppLanguage.ENGLISH to "Rotation Vector (Fused)",
            AppLanguage.URDU to "روٹیشن ویکٹر (اعلیٰ درستگی)",
            AppLanguage.ARABIC to "متجه الدوران (مدمج)",
            AppLanguage.HINDI to "रोटेशन वेक्टर"
        ),
        "qibla_sensor_accel_mag" to mapOf(
            AppLanguage.ENGLISH to "Accelerometer + Magnetometer",
            AppLanguage.URDU to "ایکسلیرومیٹر + میگنیٹومیٹر",
            AppLanguage.ARABIC to "مقياس التسارع + المغناطيسية",
            AppLanguage.HINDI to "एक्सेलेरोमीटर + मैग्नेटोमीटर"
        ),
        "qibla_sensor_unavailable" to mapOf(
            AppLanguage.ENGLISH to "Unavailable",
            AppLanguage.URDU to "دستیاب نہیں",
            AppLanguage.ARABIC to "غير متوفر",
            AppLanguage.HINDI to "अनुपलब्ध"
        ),
        "qibla_accuracy_label" to mapOf(
            AppLanguage.ENGLISH to "Sensor Accuracy",
            AppLanguage.URDU to "سینسر درستگی",
            AppLanguage.ARABIC to "دقة المستشعر",
            AppLanguage.HINDI to "सेंसर सटीकता"
        ),
        "qibla_acc_high" to mapOf(
            AppLanguage.ENGLISH to "High",
            AppLanguage.URDU to "اعلیٰ",
            AppLanguage.ARABIC to "عالية",
            AppLanguage.HINDI to "उच्च"
        ),
        "qibla_acc_medium" to mapOf(
            AppLanguage.ENGLISH to "Medium",
            AppLanguage.URDU to "درمیانی",
            AppLanguage.ARABIC to "متوسطة",
            AppLanguage.HINDI to "मध्यम"
        ),
        "qibla_acc_low" to mapOf(
            AppLanguage.ENGLISH to "Low (Calibrate)",
            AppLanguage.URDU to "کم (کیلیبریٹ کریں)",
            AppLanguage.ARABIC to "منخفضة (عاير الجهاز)",
            AppLanguage.HINDI to "कम (कैलिब्रेट करें)"
        ),
        "qibla_acc_unreliable" to mapOf(
            AppLanguage.ENGLISH to "Unreliable",
            AppLanguage.URDU to "ناقابل اعتماد",
            AppLanguage.ARABIC to "غير موثوقة",
            AppLanguage.HINDI to "अविश्वसनीय"
        ),
        "qibla_acc_waiting" to mapOf(
            AppLanguage.ENGLISH to "Waiting for reading...",
            AppLanguage.URDU to "ریڈنگ کا انتظار ہے...",
            AppLanguage.ARABIC to "بانتظار قراءة المستشعر...",
            AppLanguage.HINDI to "रीडिंग की प्रतीक्षा..."
        ),
        "qibla_no_sensor_title" to mapOf(
            AppLanguage.ENGLISH to "Compass Sensor Unavailable",
            AppLanguage.URDU to "کمپاس سینسر دستیاب نہیں",
            AppLanguage.ARABIC to "مستشعر البوصلة غير متوفر",
            AppLanguage.HINDI to "कम्पास सेंसर उपलब्ध नहीं"
        ),
        "qibla_no_sensor_desc" to mapOf(
            AppLanguage.ENGLISH to "This device does not have a working magnetometer. GPS alone cannot detect which way your phone is facing, so no live compass needle is shown.",
            AppLanguage.URDU to "اس فون میں میگنیٹومیٹر سینسر موجود نہیں۔ صرف جی پی ایس فون کا رخ معلوم نہیں کر سکتا، اس لیے لائیو سوئی نہیں دکھائی جا رہی۔",
            AppLanguage.ARABIC to "هذا الجهاز لا يحتوي على مستشعر مغناطيسي صالح. نظام GPS وحده لا يحدد اتجاه الهاتف، لذا يتم عرض الزاوية الحسابية فقط.",
            AppLanguage.HINDI to "इस डिवाइस में कार्यशील मैग्नेटोमीटर नहीं है। केवल GPS फ़ोन की दिशा नहीं बता सकता, इसलिए लाइव सुई नहीं दिखाई जा रही है।"
        ),
        "qibla_no_sensor_guide" to mapOf(
            AppLanguage.ENGLISH to "Manual Orientation Guide:\n1. Find True North (0°) using a physical compass, reliable map, or sun/shadow reference.\n2. From True North, measure %d° clockwise to face the Holy Kaaba.\n3. If using a physical magnetic compass, apply magnetic declination (%s°).",
            AppLanguage.URDU to "دستی رہنمائی:\n1. اصلی کمپاس، نقشے یا سورج کے رخ سے حقیقی شمال (0°) معلوم کریں۔\n2. حقیقی شمال سے گھڑی کی سوئی کی سمت %d° پر قبلہ رخ ہوگا۔\n3. اگر عام مقناطیسی کمپاس استعمال کریں تو مقناطیسی انحراف (%s°) کا لحاظ رکھیں۔",
            AppLanguage.ARABIC to "دليل التوجيه اليدوي:\n1. حدد الشمال الحقيقي (0°) باستخدام بوصلة فعلية أو خريطة أو موضع الشمس.\n2. اتجه بزاوية %d° مع عقارب الساعة من الشمال الحقيقي نحو الكعبة المشرفة.\n3. عند استخدام بوصلة مغناطيسية عادية، راعِ الانحراف المغناطيسي (%s°).",
            AppLanguage.HINDI to "मैन्युअल दिशा निर्देश:\n1. वास्तविक उत्तर (0°) भौतिक कम्पास, मानचित्र या सूर्य से पहचानें।\n2. वास्तविक उत्तर से घड़ी की दिशा में %d° पर काबा की दिशा होगी।\n3. यदि सामान्य चुंबकीय कम्पास उपयोग करें तो झुकाव (%s°) समायोजित करें।"
        ),
        "qibla_offline_note" to mapOf(
            AppLanguage.ENGLISH to "Calculated offline on your device • Compass accuracy varies with local magnetic interference; never rely blindly near heavy metal or magnets.",
            AppLanguage.URDU to "تمام حساب کتاب مکمل آف لائن آپ کے فون پر ہوتا ہے • مقناطیسی مداخلت کی وجہ سے کمپاس کی درستگی متاثر ہو سکتی ہے۔",
            AppLanguage.ARABIC to "يتم الحساب محلياً دون اتصال • تتأثر دقة البوصلة بالتداخل المغناطيسي القريب ويجب المعايرة عند الحاجة.",
            AppLanguage.HINDI to "सभी गणनाएँ आपके डिवाइस पर ऑफ़लाइन होती हैं • चुंबकीय व्यवधान से कम्पास सटीकता प्रभावित हो सकती है।"
        ),
        "qibla_mode_live" to mapOf(
            AppLanguage.ENGLISH to "Live",
            AppLanguage.URDU to "لائیو",
            AppLanguage.ARABIC to "مباشر",
            AppLanguage.HINDI to "लाइव"
        ),
        "qibla_mode_assisted" to mapOf(
            AppLanguage.ENGLISH to "Assisted",
            AppLanguage.URDU to "معاون",
            AppLanguage.ARABIC to "مساعِد",
            AppLanguage.HINDI to "सहायक"
        ),
        "qibla_mode_manual" to mapOf(
            AppLanguage.ENGLISH to "Manual",
            AppLanguage.URDU to "دستی",
            AppLanguage.ARABIC to "يدوي",
            AppLanguage.HINDI to "मैन्युअल"
        ),
        "qibla_mode_auto_badge" to mapOf(
            AppLanguage.ENGLISH to "Auto-selected",
            AppLanguage.URDU to "خودکار منتخب",
            AppLanguage.ARABIC to "محدد تلقائياً",
            AppLanguage.HINDI to "स्वचालित चयनित"
        ),
        "qibla_sensor_geomag_rv" to mapOf(
            AppLanguage.ENGLISH to "Geomagnetic Rotation Vector (No Gyro)",
            AppLanguage.URDU to "جیومیگنیٹک روٹیشن ویکٹر (بغیر جائرو)",
            AppLanguage.ARABIC to "متجه الدوران المغناطيسي (بدون جيروسكوب)",
            AppLanguage.HINDI to "जियोमैग्नेटिक रोटेशन वेक्टर (बिना जाइरो)"
        ),
        "qibla_sensor_gravity_mag" to mapOf(
            AppLanguage.ENGLISH to "Gravity + Magnetometer",
            AppLanguage.URDU to "گریویٹی + میگنیٹومیٹر",
            AppLanguage.ARABIC to "الجاذبية + المستشعر المغناطيسي",
            AppLanguage.HINDI to "ग्रेविटी + मैग्नेटोमीटर"
        ),
        "qibla_sensor_game_rv" to mapOf(
            AppLanguage.ENGLISH to "Game Rotation Vector (Relative)",
            AppLanguage.URDU to "گیم روٹیشن ویکٹر (اضافی رخ)",
            AppLanguage.ARABIC to "متجه الدوران النسبي (بدون مغناطيس)",
            AppLanguage.HINDI to "गेम रोटेशन वेक्टर (सापेक्ष)"
        ),
        "qibla_sensor_gyroscope" to mapOf(
            AppLanguage.ENGLISH to "Gyroscope (Relative Rotation)",
            AppLanguage.URDU to "جائروسکوپ (اضافی گردش)",
            AppLanguage.ARABIC to "جيروسكوب (دوران نسبي)",
            AppLanguage.HINDI to "जाइरोस्कोप (सापेक्ष घूर्णन)"
        ),
        "qibla_reason_live_rv" to mapOf(
            AppLanguage.ENGLISH to "Live Compass active using fused 9-axis orientation sensors (Magnetometer, Accelerometer & Gyroscope).",
            AppLanguage.URDU to "لائیو کمپاس فعال ہے (روٹیشن ویکٹر: میگنیٹومیٹر + ایکسلرومیٹر + جائروسکوپ)۔",
            AppLanguage.ARABIC to "البوصلة المباشرة نشطة عبر مستشعر متجه الدوران المدمج.",
            AppLanguage.HINDI to "रोटेशन वेक्टर सेंसर (मैग्नेटोमीटर + एक्सेलेरोमीटर + जाइरोस्कोप) से लाइव कम्पास सक्रिय है।"
        ),
        "qibla_reason_live_geomag" to mapOf(
            AppLanguage.ENGLISH to "Live Compass active using geomagnetic heading fusion (works accurately without a Gyroscope).",
            AppLanguage.URDU to "لائیو کمپاس فعال ہے (جیومیگنیٹک روٹیشن ویکٹر — جائروسکوپ کے بغیر بھی درست کام کرتا ہے)۔",
            AppLanguage.ARABIC to "البوصلة المباشرة نشطة عبر متجه الدوران المغناطيسي (تعمل بدون جيروسكوب).",
            AppLanguage.HINDI to "जियोमैग्नेटिक रोटेशन वेक्टर से लाइव कम्पास सक्रिय है (जाइरोस्कोप के बिना भी काम करता है)।"
        ),
        "qibla_reason_live_grav_mag" to mapOf(
            AppLanguage.ENGLISH to "Live Compass active using gravity tilt and magnetic heading sensors.",
            AppLanguage.URDU to "لائیو کمپاس فعال ہے (گریویٹی + میگنیٹومیٹر سینسر)۔",
            AppLanguage.ARABIC to "البوصلة المباشرة نشطة باستخدام مستشعر الجاذبية والمستشعر المغناطيسي.",
            AppLanguage.HINDI to "ग्रेविटी + मैग्नेटोमीटर सेंसर संयोजन से लाइव कम्पास सक्रिय है।"
        ),
        "qibla_reason_live_accel_mag" to mapOf(
            AppLanguage.ENGLISH to "Live Compass active using tilt and magnetic field sensors (works without a Gyroscope).",
            AppLanguage.URDU to "لائیو کمپاس فعال ہے (ایکسلرومیٹر + میگنیٹومیٹر — جائروسکوپ کے بغیر کام کرتا ہے)۔",
            AppLanguage.ARABIC to "البوصلة المباشرة نشطة باستخدام مقياس التسارع والمستشعر المغناطيسي (تعمل بدون جيروسكوب).",
            AppLanguage.HINDI to "एक्सेलेरोमीटर + मैग्नेटोमीटर से लाइव कम्पास सक्रिय है (जाइरोस्कोप के बिना काम करता है)।"
        ),
        "qibla_reason_no_mag_assisted_avail" to mapOf(
            AppLanguage.ENGLISH to "No Magnetometer detected, so Manual Qibla Mode was auto-selected. You can also switch to Assisted Mode using an external Sun or North reference.",
            AppLanguage.URDU to "میگنیٹومیٹر دستیاب نہیں، اس لیے دستی قبلہ موڈ خودکار منتخب ہوا۔ آپ سورج یا شمال کے حوالے سے معاون موڈ بھی استعمال کر سکتے ہیں۔",
            AppLanguage.ARABIC to "لا يوجد مستشعر مغناطيسي، لذا تم اختيار الوضع اليدوي تلقائياً. يمكنك التبديل للوضع المساعِد باستخدام مرجع الشمس أو الشمال.",
            AppLanguage.HINDI to "मैग्नेटोमीटर नहीं मिला, इसलिए मैन्युअल क़िबला मोड चुना गया। आप सूर्य या उत्तर संदर्भ से सहायक मोड भी उपयोग कर सकते हैं।"
        ),
        "qibla_reason_no_mag_manual_only" to mapOf(
            AppLanguage.ENGLISH to "No Magnetometer or relative orientation sensor detected. Manual Qibla Mode shows exact calculated bearing and N/S/E/W reference.",
            AppLanguage.URDU to "میگنیٹومیٹر یا گردش سینسر دستیاب نہیں۔ دستی قبلہ موڈ درست زاویہ اور شمال/جنوب/مشرق/مغرب رہنمائی دکھاتا ہے۔",
            AppLanguage.ARABIC to "لا يتوفر مستشعر مغناطيسي أو مستشعر اتجاه. يعرض الوضع اليدوي زاوية القبلة الدقيقة واتجاهات البوصلة الثابتة.",
            AppLanguage.HINDI to "मैग्नेटोमीटर या ओरिएंटेशन सेंसर उपलब्ध नहीं है। मैन्युअल मोड सटीक कोण और उत्तर/दक्षिण/पूर्व/पश्चिम संदर्भ दिखाता है।"
        ),
        "qibla_reason_manual_user" to mapOf(
            AppLanguage.ENGLISH to "Manual Qibla Mode selected. Showing static N/S/E/W reference and exact geographic bearing without relying on compass sensors.",
            AppLanguage.URDU to "دستی قبلہ موڈ منتخب ہے۔ سینسر کے بغیر درست جغرافیائی زاویہ اور ساکن شمال/جنوب/مشرق/مغرب نقشہ دکھایا جا رہا ہے۔",
            AppLanguage.ARABIC to "تم اختيار الوضع اليدوي. يتم عرض زاوية القبلة الجغرافية الدقيقة ومخطط الاتجاهات الثابت.",
            AppLanguage.HINDI to "मैन्युअल क़िबला मोड चुना गया। सेंसर के बिना सटीक कोण और स्थिर दिशा संदर्भ दिखाया जा रहा है।"
        ),
        "qibla_reason_assisted_unanchored" to mapOf(
            AppLanguage.ENGLISH to "Assisted Direction Mode: a gyroscope alone cannot detect True North. Point the top of your phone toward the Sun or verified True North, then tap Lock Reference.",
            AppLanguage.URDU to "معاون موڈ: صرف جائروسکوپ حقیقی شمال معلوم نہیں کر سکتا۔ فون کا اوپری حصہ سورج یا معلوم شمال کی طرف کر کے حوالہ لاک کریں۔",
            AppLanguage.ARABIC to "الوضع المساعِد: الجيروسكوب وحده لا يحدد الشمال الحقيقي. وجّه أعلى الهاتف نحو الشمس أو الشمال الحقيقي ثم اضغط تثبيت المرجع.",
            AppLanguage.HINDI to "सहायक मोड: केवल जाइरोस्कोप वास्तविक उत्तर नहीं बता सकता। फ़ोन का ऊपरी हिस्सा सूर्य या वास्तविक उत्तर की ओर कर संदर्भ लॉक करें।"
        ),
        "qibla_reason_assisted_anchored" to mapOf(
            AppLanguage.ENGLISH to "Assisted Direction active relative to your locked external reference. Re-lock if gyroscope drift occurs.",
            AppLanguage.URDU to "معاون سمت فعال ہے (لاک کیے گئے بیرونی حوالے کے مطابق)۔ اگر ڈرفٹ ہو تو دوبارہ لاک کریں۔",
            AppLanguage.ARABIC to "الاتجاه المساعِد نشط وفق المرجع الخارجي المثبّت. أعد التثبيت عند حدوث انحراف.",
            AppLanguage.HINDI to "लॉक किए गए बाहरी संदर्भ के अनुसार सहायक दिशा सक्रिय है। विचलन होने पर पुनः लॉक करें।"
        ),
        "qibla_reason_assisted_no_sensor" to mapOf(
            AppLanguage.ENGLISH to "No Gyroscope or Game Rotation sensor detected on this device. Assisted relative tracking is unavailable; use Manual Qibla Mode.",
            AppLanguage.URDU to "اس فون میں جائروسکوپ یا گیم روٹیشن سینسر موجود نہیں۔ براہِ کرم دستی قبلہ موڈ استعمال کریں۔",
            AppLanguage.ARABIC to "لا يوجد جيروسكوب أو مستشعر دوران نسبي في هذا الجهاز. استخدم الوضع اليدوي.",
            AppLanguage.HINDI to "इस डिवाइस में जाइरोस्कोप या गेम रोटेशन सेंसर उपलब्ध नहीं है। कृपया मैन्युअल क़िबला मोड उपयोग करें।"
        ),
        "qibla_assisted_title" to mapOf(
            AppLanguage.ENGLISH to "Assisted Direction (Reference-Locked)",
            AppLanguage.URDU to "معاون سمت (حوالہ لاک)",
            AppLanguage.ARABIC to "الاتجاه المساعِد (بمرجع مثبّت)",
            AppLanguage.HINDI to "सहायक दिशा (संदर्भ-लॉक्ड)"
        ),
        "qibla_assisted_sun_status" to mapOf(
            AppLanguage.ENGLISH to "Current Sun Azimuth: %d° (%s) • Altitude: %d°",
            AppLanguage.URDU to "موجودہ سورج کا زاویہ: %d° (%s) • بلندی: %d°",
            AppLanguage.ARABIC to "سمت الشمس الحالي: %d° (%s) • الارتفاع: %d°",
            AppLanguage.HINDI to "वर्तमान सूर्य कोण: %d° (%s) • ऊँचाई: %d°"
        ),
        "qibla_assisted_sun_below" to mapOf(
            AppLanguage.ENGLISH to "Sun is currently below the horizon or directly overhead — use True North lock or Manual Mode.",
            AppLanguage.URDU to "سورج اس وقت افق سے نیچے یا عین سر پر ہے — حقیقی شمال لاک یا دستی موڈ استعمال کریں۔",
            AppLanguage.ARABIC to "الشمس حالياً تحت الأفق أو في السمت — استخدم تثبيت الشمال الحقيقي أو الوضع اليدوي.",
            AppLanguage.HINDI to "सूर्य अभी क्षितिज के नीचे या ठीक सिर के ऊपर है — वास्तविक उत्तर लॉक या मैन्युअल मोड चुनें।"
        ),
        "qibla_assisted_lock_sun" to mapOf(
            AppLanguage.ENGLISH to "Lock to Sun (%d°)",
            AppLanguage.URDU to "سورج پر لاک کریں (%d°)",
            AppLanguage.ARABIC to "تثبيت على الشمس (%d°)",
            AppLanguage.HINDI to "सूर्य पर लॉक करें (%d°)"
        ),
        "qibla_assisted_lock_north" to mapOf(
            AppLanguage.ENGLISH to "Lock to True North (0°)",
            AppLanguage.URDU to "حقیقی شمال پر لاک کریں (0°)",
            AppLanguage.ARABIC to "تثبيت على الشمال الحقيقي (0°)",
            AppLanguage.HINDI to "वास्तविक उत्तर पर लॉक करें (0°)"
        ),
        "qibla_assisted_reset_lock" to mapOf(
            AppLanguage.ENGLISH to "Reset Reference Lock",
            AppLanguage.URDU to "حوالہ لاک ری سیٹ کریں",
            AppLanguage.ARABIC to "إعادة تعيين المرجع",
            AppLanguage.HINDI to "संदर्भ लॉक रीसेट करें"
        ),
        "qibla_assisted_switch_manual" to mapOf(
            AppLanguage.ENGLISH to "Open Manual Qibla Mode",
            AppLanguage.URDU to "دستی قبلہ موڈ کھولیں",
            AppLanguage.ARABIC to "فتح الوضع اليدوي للقبلة",
            AppLanguage.HINDI to "मैन्युअल क़िबला मोड खोलें"
        ),
        "qibla_static_ref_label" to mapOf(
            AppLanguage.ENGLISH to "Static N/S/E/W Reference (North at Top)",
            AppLanguage.URDU to "ساکن سمت کا نقشہ (شمال اوپر کی طرف)",
            AppLanguage.ARABIC to "مخطط اتجاهات ثابت (الشمال للأعلى)",
            AppLanguage.HINDI to "स्थिर दिशा संदर्भ (उत्तर ऊपर की ओर)"
        ),
        "qibla_turn_right" to mapOf(
            AppLanguage.ENGLISH to "Turn right %d° toward Qibla",
            AppLanguage.URDU to "قبلہ کی طرف %d° دائیں مڑیں",
            AppLanguage.ARABIC to "استدر يميناً %d° نحو القبلة",
            AppLanguage.HINDI to "क़िबला की ओर %d° दाएँ मुड़ें"
        ),
        "qibla_turn_left" to mapOf(
            AppLanguage.ENGLISH to "Turn left %d° toward Qibla",
            AppLanguage.URDU to "قبلہ کی طرف %d° بائیں مڑیں",
            AppLanguage.ARABIC to "استدر يساراً %d° نحو القبلة",
            AppLanguage.HINDI to "क़िबला की ओर %d° बाएँ मुड़ें"
        ),
        "qibla_waiting_sensor_hint" to mapOf(
            AppLanguage.ENGLISH to "Move phone gently to receive live heading reading...",
            AppLanguage.URDU to "لائیو سمت حاصل کرنے کے لیے فون کو آہستہ حرکت دیں...",
            AppLanguage.ARABIC to "حرّك الهاتف برفق لاستقبال قراءة البوصلة المباشرة...",
            AppLanguage.HINDI to "लाइव दिशा रीडिंग के लिए फ़ोन को हल्के से घुमाएँ..."
        ),
        "qibla_city_dialog_title" to mapOf(
            AppLanguage.ENGLISH to "Select Location Worldwide",
            AppLanguage.URDU to "دنیا بھر سے شہر منتخب کریں",
            AppLanguage.ARABIC to "اختر الموقع حول العالم",
            AppLanguage.HINDI to "दुनिया भर से स्थान चुनें"
        ),
        "qibla_city_search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search city or country (EN / UR / AR / HI)...",
            AppLanguage.URDU to "شہر یا ملک تلاش کریں (اردو، انگریزی، عربی، ہندی)...",
            AppLanguage.ARABIC to "ابحث عن مدينة أو دولة بأي لغة...",
            AppLanguage.HINDI to "शहर या देश खोजें (हिन्दी / अंग्रेज़ी / उर्दू / अरबी)..."
        ),
        "qibla_no_city_results" to mapOf(
            AppLanguage.ENGLISH to "No matching city found. Tap the pin icon for manual coordinates.",
            AppLanguage.URDU to "کوئی شہر نہیں ملا۔ دستی کوآرڈینیٹس کے لیے پن آئیکن دبائیں۔",
            AppLanguage.ARABIC to "لم يتم العثور على مدينة مطابقة. اضغط أيقونة الدبوس لإدخال الإحداثيات.",
            AppLanguage.HINDI to "कोई शहर नहीं मिला। मैन्युअल निर्देशांक के लिए पिन आइकन दबाएं।"
        ),
        "qibla_custom_coords_cd" to mapOf(
            AppLanguage.ENGLISH to "Custom Coordinates",
            AppLanguage.URDU to "دستی کوآرڈینیٹس",
            AppLanguage.ARABIC to "إحداثيات مخصصة",
            AppLanguage.HINDI to "मैन्युअल निर्देशांक"
        ),
        "qibla_close" to mapOf(
            AppLanguage.ENGLISH to "Close",
            AppLanguage.URDU to "بند کریں",
            AppLanguage.ARABIC to "إغلاق",
            AppLanguage.HINDI to "बंद करें"
        ),
        "qibla_manual_title" to mapOf(
            AppLanguage.ENGLISH to "Manual Coordinate Entry",
            AppLanguage.URDU to "دستی کوآرڈینیٹس درج کریں",
            AppLanguage.ARABIC to "إدخال الإحداثيات يدوياً",
            AppLanguage.HINDI to "मैन्युअल निर्देशांक दर्ज करें"
        ),
        "qibla_manual_city_label" to mapOf(
            AppLanguage.ENGLISH to "Location / City Name",
            AppLanguage.URDU to "شہر یا مقام کا نام",
            AppLanguage.ARABIC to "اسم المدينة أو الموقع",
            AppLanguage.HINDI to "शहर / स्थान का नाम"
        ),
        "qibla_manual_lat_label" to mapOf(
            AppLanguage.ENGLISH to "Latitude (-90 to +90)",
            AppLanguage.URDU to "عرض بلد (Latitude: -90 تا +90)",
            AppLanguage.ARABIC to "خط العرض (-90 إلى +90)",
            AppLanguage.HINDI to "अक्षांश (Latitude: -90 से +90)"
        ),
        "qibla_manual_lon_label" to mapOf(
            AppLanguage.ENGLISH to "Longitude (-180 to +180)",
            AppLanguage.URDU to "طول بلد (Longitude: -180 تا +180)",
            AppLanguage.ARABIC to "خط الطول (-180 إلى +180)",
            AppLanguage.HINDI to "देशांतर (Longitude: -180 से +180)"
        ),
        "qibla_apply_coords" to mapOf(
            AppLanguage.ENGLISH to "Apply Coordinates",
            AppLanguage.URDU to "کوآرڈینیٹس لاگو کریں",
            AppLanguage.ARABIC to "تطبيق الإحداثيات",
            AppLanguage.HINDI to "निर्देशांक लागू करें"
        ),
        "qibla_cancel" to mapOf(
            AppLanguage.ENGLISH to "Cancel",
            AppLanguage.URDU to "منسوخ کریں",
            AppLanguage.ARABIC to "إلغاء",
            AppLanguage.HINDI to "रद्द करें"
        ),
        "qibla_error_invalid_number" to mapOf(
            AppLanguage.ENGLISH to "Enter a valid decimal number.",
            AppLanguage.URDU to "درست عددی قدر درج کریں۔",
            AppLanguage.ARABIC to "أدخل رقماً عشرياً صحيحاً.",
            AppLanguage.HINDI to "कृपया मान्य दशमलव संख्या दर्ज करें।"
        ),
        "qibla_error_lat_range" to mapOf(
            AppLanguage.ENGLISH to "Latitude must be between -90 and +90.",
            AppLanguage.URDU to "عرض بلد -90 اور +90 کے درمیان ہونا چاہیے۔",
            AppLanguage.ARABIC to "يجب أن يكون خط العرض بين -90 و +90.",
            AppLanguage.HINDI to "अक्षांश -90 से +90 के बीच होना चाहिए।"
        ),
        "qibla_error_lon_range" to mapOf(
            AppLanguage.ENGLISH to "Longitude must be between -180 and +180.",
            AppLanguage.URDU to "طول بلد -180 اور +180 کے درمیان ہونا چاہیے۔",
            AppLanguage.ARABIC to "يجب أن يكون خط الطول بين -180 و +180.",
            AppLanguage.HINDI to "देशांतर -180 से +180 के बीच होना चाहिए।"
        ),
        "qibla_north_short" to mapOf(
            AppLanguage.ENGLISH to "N",
            AppLanguage.URDU to "شمال",
            AppLanguage.ARABIC to "ش",
            AppLanguage.HINDI to "उ"
        ),
        "qibla_south_short" to mapOf(
            AppLanguage.ENGLISH to "S",
            AppLanguage.URDU to "جنوب",
            AppLanguage.ARABIC to "ج",
            AppLanguage.HINDI to "द"
        ),
        "qibla_east_short" to mapOf(
            AppLanguage.ENGLISH to "E",
            AppLanguage.URDU to "مشرق",
            AppLanguage.ARABIC to "ق",
            AppLanguage.HINDI to "पू"
        ),
        "qibla_west_short" to mapOf(
            AppLanguage.ENGLISH to "W",
            AppLanguage.URDU to "مغرب",
            AppLanguage.ARABIC to "غ",
            AppLanguage.HINDI to "प"
        ),
        // Hijri Calendar Localization Keys
        "hijri_tab_calendar" to mapOf(
            AppLanguage.ENGLISH to "Dual Calendar",
            AppLanguage.URDU to "ہجری و عیسوی کیلنڈر",
            AppLanguage.ARABIC to "التقويم المزدوج",
            AppLanguage.HINDI to "दोहरा कैलेंडर"
        ),
        "hijri_tab_events" to mapOf(
            AppLanguage.ENGLISH to "Islamic Events",
            AppLanguage.URDU to "اسلامی ایام و تہوار",
            AppLanguage.ARABIC to "المناسبات الإسلامية",
            AppLanguage.HINDI to "इस्लामी अवसर"
        ),
        "hijri_tab_ramadan" to mapOf(
            AppLanguage.ENGLISH to "Ramadan Schedule",
            AppLanguage.URDU to "رمضان ٹائم ٹیبل",
            AppLanguage.ARABIC to "جدول رمضان",
            AppLanguage.HINDI to "रमज़ान समय-सारणी"
        ),
        "hijri_hero_title" to mapOf(
            AppLanguage.ENGLISH to "Today's Estimated Hijri & Gregorian Date",
            AppLanguage.URDU to "آج کی تخمینی ہجری و عیسوی تاریخ",
            AppLanguage.ARABIC to "التاريخ الهجري التقديري والميلادي لليوم",
            AppLanguage.HINDI to "आज की अनुमानित हिजरी और ग्रेगोरियन तिथि"
        ),
        "hijri_status_estimate" to mapOf(
            AppLanguage.ENGLISH to "Calculation / Regional Estimate — Pending Official Moon Sighting",
            AppLanguage.URDU to "تخمینی حساب — حتمی تاریخ کا انحصار سرکاری رویتِ ہلال پر ہے",
            AppLanguage.ARABIC to "تقدير حسابي — التاريخ الرسمي يعتمد على إعلان الرؤية الشرعية",
            AppLanguage.HINDI to "गणित अनुमान — आधिकारिक चाँद दिखने की घोषणा पर निर्भर"
        ),
        "hijri_status_umm_al_qura" to mapOf(
            AppLanguage.ENGLISH to "Saudi Umm Al-Qura Calendar Reference",
            AppLanguage.URDU to "ام القریٰ کیلنڈر (سعودی عرب حوالہ)",
            AppLanguage.ARABIC to "تقويم أم القرى الرسمي (المملكة العربية السعودية)",
            AppLanguage.HINDI to "उम्म अल-क़ुरा कैलेंडर (सऊदी संदर्भ)"
        ),
        "hijri_status_tabular" to mapOf(
            AppLanguage.ENGLISH to "Astronomical / Tabular Estimate (Unconfirmed)",
            AppLanguage.URDU to "فلکیاتی / جدولی تخمینہ (غیر مصدقہ)",
            AppLanguage.ARABIC to "تقدير فلكي / جدولي (غير مؤكد بالرؤية)",
            AppLanguage.HINDI to "खगोलीय / सारणीबद्ध अनुमान (अपुष्ट)"
        ),
        "hijri_status_manual" to mapOf(
            AppLanguage.ENGLISH to "Local Manual Moon-Sighting Adjustment",
            AppLanguage.URDU to "مقامی دستی رویتِ ہلال ترمیم",
            AppLanguage.ARABIC to "تعديل محلي يدوي للرؤية",
            AppLanguage.HINDI to "स्थानीय मैन्युअल चाँद समायोजन"
        ),
        "hijri_convention_title" to mapOf(
            AppLanguage.ENGLISH to "Calendar Convention & Moon Sighting",
            AppLanguage.URDU to "کیلنڈر کا طریقۂ کار اور رویتِ ہلال",
            AppLanguage.ARABIC to "معيار التقويم ورؤية الهلال",
            AppLanguage.HINDI to "कैलेंडर पद्धति और चाँद देखना"
        ),
        "hijri_conv_pakistan" to mapOf(
            AppLanguage.ENGLISH to "Pakistan — Central Ruet-e-Hilal Committee",
            AppLanguage.URDU to "پاکستان — مرکزی رویتِ ہلال کمیٹی (تخمینی)",
            AppLanguage.ARABIC to "باكستان — لجنة رؤية الهلال المركزية (تقديري)",
            AppLanguage.HINDI to "पाकिस्तान — केंद्रीय रुअत-ए-हिलाल समिति (अनुमानित)"
        ),
        "hijri_conv_pakistan_desc" to mapOf(
            AppLanguage.ENGLISH to "Applies typical South Asian lunar horizon offset (-1 day) as an offline estimate; check official MoRA announcement for final confirmation.",
            AppLanguage.URDU to "آف لائن تخمینہ (-1 دن علاقائی فرق کے ساتھ)۔ حتمی تصدیق کے لیے وزارتِ مذہبی امور کا سرکاری اعلان دیکھیں۔",
            AppLanguage.ARABIC to "تقدير دون اتصال بفارق (-1 يوم) لأفق الرؤية الإقليمي؛ لا يُعد تأكيداً رسمياً دون إعلان اللجنة.",
            AppLanguage.HINDI to "ऑफ़लाइन अनुमान (-1 दिन क्षेत्रीय अंतर के साथ); अंतिम पुष्टि के लिए आधिकारिक घोषणा देखें।"
        ),
        "hijri_conv_umm_al_qura" to mapOf(
            AppLanguage.ENGLISH to "Saudi Arabia — Umm Al-Qura",
            AppLanguage.URDU to "سعودی عرب — تقویمِ ام القریٰ",
            AppLanguage.ARABIC to "المملكة العربية السعودية — تقويم أم القرى",
            AppLanguage.HINDI to "सऊदी अरब — उम्म अल-क़ुरा"
        ),
        "hijri_conv_umm_al_qura_desc" to mapOf(
            AppLanguage.ENGLISH to "Uses the ICU Umm Al-Qura lunar calendar model used as the Saudi civil calendar reference.",
            AppLanguage.URDU to "سعودی عرب میں رائج ام القریٰ قمری کیلنڈر کے اصول پر مبنی حساب۔",
            AppLanguage.ARABIC to "يعتمد على تقويم أم القرى الفلكي المعتمد مرجعاً مدنياً في المملكة العربية السعودية.",
            AppLanguage.HINDI to "सऊदी नागरिक कैलेंडर संदर्भ के रूप में प्रयुक्त उम्म अल-क़ुरा चंद्र गणना।"
        ),
        "hijri_conv_tabular" to mapOf(
            AppLanguage.ENGLISH to "Astronomical / Tabular Hijri Calendar",
            AppLanguage.URDU to "فلکیاتی / جدولی ہجری کیلنڈر",
            AppLanguage.ARABIC to "التقويم الهجري الفلكي / الجدولي",
            AppLanguage.HINDI to "खगोलीय / सारणीबद्ध हिजरी कैलेंडर"
        ),
        "hijri_conv_tabular_desc" to mapOf(
            AppLanguage.ENGLISH to "Standard 30-year arithmetic tabular calculation; pure unconfirmed astronomical estimate.",
            AppLanguage.URDU to "معیاری 30 سالہ جدولی فلکیاتی حساب؛ خالص غیر مصدقہ تخمینہ۔",
            AppLanguage.ARABIC to "حساب جدولي قياسي بدورة 30 سنة؛ تقدير حسابي بحت غير مرتبط برؤية محلية.",
            AppLanguage.HINDI to "मानक 30-वर्षीय सारणीबद्ध गणना; शुद्ध अपुष्ट खगोलीय अनुमान।"
        ),
        "hijri_conv_manual" to mapOf(
            AppLanguage.ENGLISH to "Manual Local Moon-Sighting Adjustment",
            AppLanguage.URDU to "دستی مقامی رویتِ ہلال ترمیم",
            AppLanguage.ARABIC to "تعديل يدوي وفق الرؤية المحلية",
            AppLanguage.HINDI to "मैन्युअल स्थानीय चाँद समायोजन"
        ),
        "hijri_conv_manual_desc" to mapOf(
            AppLanguage.ENGLISH to "Uses tabular base plus your manual +/- day offset to match your local Hilal committee announcement.",
            AppLanguage.URDU to "مقامی ہلال کمیٹی کے اعلان سے مطابقت کے لیے اپنی مرضی سے دن کم یا زیادہ کریں۔",
            AppLanguage.ARABIC to "يطبّق إزاحة الأيام اليدوية (+/-) لمطابقة إعلان الجهة الشرعية في بلدك.",
            AppLanguage.HINDI to "अपनी स्थानीय हिलाल समिति की घोषणा से मिलान करने के लिए +/- दिन समायोजित करें।"
        ),
        "hijri_manual_offset_label" to mapOf(
            AppLanguage.ENGLISH to "Manual Day Adjustment (-2 to +2):",
            AppLanguage.URDU to "دستی دن کی ترمیم (-2 تا +2):",
            AppLanguage.ARABIC to "تعديل الأيام يدوياً (-2 إلى +2):",
            AppLanguage.HINDI to "मैन्युअल दिन समायोजन (-2 से +2):"
        ),
        "hijri_no_offset" to mapOf(
            AppLanguage.ENGLISH to "0 days (Convention base)",
            AppLanguage.URDU to "0 دن (بنیادی حساب)",
            AppLanguage.ARABIC to "0 يوم (الأساس المحدد)",
            AppLanguage.HINDI to "0 दिन (चयनित आधार)"
        ),
        "hijri_today_btn" to mapOf(
            AppLanguage.ENGLISH to "Today",
            AppLanguage.URDU to "آج",
            AppLanguage.ARABIC to "اليوم",
            AppLanguage.HINDI to "आज"
        ),
        "hijri_events_title" to mapOf(
            AppLanguage.ENGLISH to "Significant Islamic Dates & Occasions",
            AppLanguage.URDU to "اہم اسلامی ایام و تاریخیں",
            AppLanguage.ARABIC to "المناسبات والتواريخ الإسلامية المهمة",
            AppLanguage.HINDI to "महत्वपूर्ण इस्लामी तिथियाँ और अवसर"
        ),
        "hijri_events_disclaimer" to mapOf(
            AppLanguage.ENGLISH to "Lunar months depend on verified crescent sighting in your region. Regional/historical dates vary across scholarly traditions.",
            AppLanguage.URDU to "قمری مہینوں کا آغاز آپ کے علاقے میں رویتِ ہلال سے مشروط ہے۔ بعض تاریخی ایام علاقائی روایات کے مطابق مختلف ہو سکتے ہیں۔",
            AppLanguage.ARABIC to "تعتمد الشهور القمرية على ثبوت رؤية الهلال في بلدك، وتختلف بعض المناسبات التاريخية باختلاف المذاهب والأقاليم.",
            AppLanguage.HINDI to "चंद्र माह आपके क्षेत्र में चाँद दिखने पर निर्भर करते हैं। कुछ ऐतिहासिक तिथियाँ क्षेत्रीय परंपराओं के अनुसार भिन्न हो सकती हैं।"
        ),
        "hijri_badge_moon_sighting" to mapOf(
            AppLanguage.ENGLISH to "Moon Sighting Dependent",
            AppLanguage.URDU to "رویتِ ہلال سے مشروط",
            AppLanguage.ARABIC to "يعتمد على رؤية الهلال",
            AppLanguage.HINDI to "चाँद दिखने पर निर्भर"
        ),
        "hijri_badge_fixed_calendar" to mapOf(
            AppLanguage.ENGLISH to "Fixed Lunar Day (Month Sighting-Based)",
            AppLanguage.URDU to "مقررہ ہجری تاریخ",
            AppLanguage.ARABIC to "تاريخ قمري محدد بالشهر",
            AppLanguage.HINDI to "निर्धारित हिजरी तिथि"
        ),
        "hijri_badge_regional" to mapOf(
            AppLanguage.ENGLISH to "Historical / Regional Observance",
            AppLanguage.URDU to "تاریخی / علاقائی مناسبت",
            AppLanguage.ARABIC to "مناسبة تاريخية / إقليمية",
            AppLanguage.HINDI to "ऐतिहासिक / क्षेत्रीय अवसर"
        ),
        "hijri_ramadan_title" to mapOf(
            AppLanguage.ENGLISH to "Projected Ramadan Timetable",
            AppLanguage.URDU to "متوقع رمضان ٹائم ٹیبل (سحر و افطار)",
            AppLanguage.ARABIC to "جدول رمضان التقديري (الإمساك والإفطار)",
            AppLanguage.HINDI to "अनुमानित रमज़ान समय-सारणी (सहरी व इफ़्तार)"
        ),
        "hijri_ramadan_note" to mapOf(
            AppLanguage.ENGLISH to "Projected 1st Ramadan start based on selected convention; final start depends on official local moon sighting. Uses your active prayer calculation method and Asr setting.",
            AppLanguage.URDU to "منتخب کیلنڈر کے مطابق متوقع رمضان ٹائم ٹیبل؛ حتمی آغاز رویتِ ہلال پر منحصر ہے۔ اس میں آپ کا منتخب کردہ اوقاتِ نماز کا طریقہ استعمال ہوتا ہے۔",
            AppLanguage.ARABIC to "جدول تقديري لبدء رمضان وفق المعيار المختار، ويعتمد البدء الفعلي على الرؤية الشرعية، مع استخدام نفس إعدادات حساب الصلاة.",
            AppLanguage.HINDI to "चयनित कैलेंडर के अनुसार अनुमानित रमज़ान सारणी; अंतिम आरंभ स्थानीय चाँद दिखने पर निर्भर है। आपकी सक्रिय नमाज़ गणना विधि का उपयोग करता है।"
        ),
        "hijri_day_col" to mapOf(
            AppLanguage.ENGLISH to "Day",
            AppLanguage.URDU to "دن",
            AppLanguage.ARABIC to "اليوم",
            AppLanguage.HINDI to "दिन"
        ),
        "hijri_date_col" to mapOf(
            AppLanguage.ENGLISH to "Date",
            AppLanguage.URDU to "تاریخ",
            AppLanguage.ARABIC to "التاريخ",
            AppLanguage.HINDI to "तिथि"
        ),
        "hijri_sehri_col" to mapOf(
            AppLanguage.ENGLISH to "Sehri (Fajr)",
            AppLanguage.URDU to "سحری (فجر)",
            AppLanguage.ARABIC to "السحور (الفجر)",
            AppLanguage.HINDI to "सहरी (फ़ज्र)"
        ),
        "hijri_iftar_col" to mapOf(
            AppLanguage.ENGLISH to "Iftar (Maghrib)",
            AppLanguage.URDU to "افطار (مغرب)",
            AppLanguage.ARABIC to "الإفطار (المغرب)",
            AppLanguage.HINDI to "इफ़्तार (मग़रिब)"
        ),
        "hijri_share_ramadan" to mapOf(
            AppLanguage.ENGLISH to "Share Ramadan Timetable",
            AppLanguage.URDU to "رمضان ٹائم ٹیبل شیئر کریں",
            AppLanguage.ARABIC to "مشاركة جدول رمضان",
            AppLanguage.HINDI to "रमज़ान सारणी साझा करें"
        ),
        // Sources & Methodology Keys (Part 4)
        "sources_section_title" to mapOf(
            AppLanguage.ENGLISH to "Sources & Methodology",
            AppLanguage.URDU to "مستند ذرائع اور طریقۂ حساب",
            AppLanguage.ARABIC to "المصادر والمنهجية",
            AppLanguage.HINDI to "स्रोत और कार्यप्रणाली"
        ),
        "sources_section_subtitle" to mapOf(
            AppLanguage.ENGLISH to "Technical calculation libraries and civil references below are computation aids, not religious decrees. Always verify local crescent sightings and mosque schedules with your local religious authority.",
            AppLanguage.URDU to "نیچے دیے گئے سائنسی اور تکنیکی ذرائع محض حساب و تقویم کے حوالے ہیں، شرعی فتویٰ نہیں۔ مقامی رویتِ ہلال اور مسجد کے اوقات کے لیے اپنے مقامی علماء و مساجد کی پیروی کریں۔",
            AppLanguage.ARABIC to "المراجع التقنية أدناه أدوات حسابية ومصادر مرجعية، وليست بديلاً عن الفتوى أو الرؤية الشرعية المحلية. يُرجى اتباع أوقات المساجد والجهات الشرعية المعتمدة في منطقتك.",
            AppLanguage.HINDI to "नीचे दिए गए तकनीकी स्रोत केवल गणना संदर्भ हैं, धार्मिक फ़तवा नहीं। स्थानीय चाँद और मस्जिद के समय के लिए अपने स्थानीय धार्मिक प्राधिकार का पालन करें।"
        ),
        "src_pk_ruet_title" to mapOf(
            AppLanguage.ENGLISH to "Pakistan Ministry of Religious Affairs (MoRA)",
            AppLanguage.URDU to "وزارتِ مذہبی امور پاکستان (مرکزی رویتِ ہلال کمیٹی)",
            AppLanguage.ARABIC to "وزارة الشؤون الدينية الباكستانية (لجنة رؤية الهلال)",
            AppLanguage.HINDI to "पाकिस्तान धार्मिक मामलों का मंत्रालय (रुअत-ए-हिलाल)"
        ),
        "src_pk_ruet_desc" to mapOf(
            AppLanguage.ENGLISH to "Official source for Pakistan Central Ruet-e-Hilal Committee moon-sighting announcements.",
            AppLanguage.URDU to "پاکستان میں مرکزی رویتِ ہلال کمیٹی کے سرکاری چاند اعلانات کا اصل مرجع۔",
            AppLanguage.ARABIC to "المرجع الرسمي لإعلانات لجنة رؤية الهلال المركزية في باكستان.",
            AppLanguage.HINDI to "पाकिस्तान केंद्रीय रुअत-ए-हिलाल समिति की आधिकारिक घोषणाओं का स्रोत।"
        ),
        "src_umm_alqura_title" to mapOf(
            AppLanguage.ENGLISH to "Umm Al-Qura Official Calendar Portal",
            AppLanguage.URDU to "ام القریٰ آفیشل کیلنڈر پورٹل (سعودی عرب)",
            AppLanguage.ARABIC to "بوابة تقويم أم القرى الرسمية",
            AppLanguage.HINDI to "उम्म अल-क़ुरा आधिकारिक कैलेंडर पोर्टल"
        ),
        "src_umm_alqura_desc" to mapOf(
            AppLanguage.ENGLISH to "Official Saudi Arabian Umm Al-Qura calendar methodology and civil dates.",
            AppLanguage.URDU to "سعودی عرب کے ام القریٰ کیلنڈر کے طریقۂ کار اور تواریخ کا مستند حوالہ۔",
            AppLanguage.ARABIC to "المنهجية الرسمية لتقويم أم القرى والتواريخ المعتمدة في المملكة.",
            AppLanguage.HINDI to "सऊदी अरब के उम्म अल-क़ुरा कैलेंडर पद्धति और तिथियों का आधिकारिक संदर्भ।"
        ),
        "src_aladhan_methods_title" to mapOf(
            AppLanguage.ENGLISH to "AlAdhan Calculation Methods Reference",
            AppLanguage.URDU to "الاذان اوقاتِ نماز طریقۂ کار حوالہ",
            AppLanguage.ARABIC to "مرجع طرق حساب مواقيت الصلاة (AlAdhan)",
            AppLanguage.HINDI to "अल-अज़ान गणना विधियाँ संदर्भ"
        ),
        "src_aladhan_methods_desc" to mapOf(
            AppLanguage.ENGLISH to "Documents standard Fajr/Isha twilight depression angles across regional Islamic calculation bodies.",
            AppLanguage.URDU to "مختلف اسلامی اداروں کے فجر اور عشاء کے فلکیاتی زاویوں کی دستاویزی تفصیلات۔",
            AppLanguage.ARABIC to "يوثّق زوايا الشفق الفلكية للفجر والعشاء لدى الهيئات الإسلامية المعتمدة.",
            AppLanguage.HINDI to "विभिन्न इस्लामी संस्थानों के फ़ज्र और ईशा के खगोलीय कोणों का दस्तावेज़ीकरण।"
        ),
        "src_aladhan_api_title" to mapOf(
            AppLanguage.ENGLISH to "AlAdhan Prayer Times Specification",
            AppLanguage.URDU to "الاذان اوقاتِ نماز وضاحت",
            AppLanguage.ARABIC to "مواصفات حساب أوقات الصلاة (AlAdhan API)",
            AppLanguage.HINDI to "अल-अज़ान नमाज़ समय विनिर्देश"
        ),
        "src_aladhan_api_desc" to mapOf(
            AppLanguage.ENGLISH to "Technical reference used to cross-check offline astronomical parameters; times are computed offline on-device.",
            AppLanguage.URDU to "آف لائن حساب کے معیارات کی تصدیق کا تکنیکی حوالہ؛ تمام اوقات فون پر آف لائن نکالے جاتے ہیں۔",
            AppLanguage.ARABIC to "مرجع تقني لمراجعة معايير الحساب؛ تُحسب الأوقات محلياً دون اتصال.",
            AppLanguage.HINDI to "ऑफ़लाइन गणना मापदंडों की जांच हेतु तकनीकी संदर्भ; गणना डिवाइस पर ऑफ़लाइन होती है।"
        ),
        "src_praytimes_title" to mapOf(
            AppLanguage.ENGLISH to "PrayTimes Calculation Methodology",
            AppLanguage.URDU to "پرے ٹائمز (PrayTimes) فلکیاتی طریقۂ کار",
            AppLanguage.ARABIC to "منهجية PrayTimes لحساب المواقيت",
            AppLanguage.HINDI to "प्रे-टाइम्स (PrayTimes) गणना पद्धति"
        ),
        "src_praytimes_desc" to mapOf(
            AppLanguage.ENGLISH to "Astronomical equations for solar declination, equation of time, Asr shadow factors, and high-latitude rules.",
            AppLanguage.URDU to "سورج کے زاویے، عصر کے سائے کے اصول اور بلند عرضِ بلد کے قواعد کا فلکیاتی فارمولا۔",
            AppLanguage.ARABIC to "المعادلات الفلكية لميل الشمس ومعادلة الزمن وظل العصر وقواعد خطوط العرض العليا.",
            AppLanguage.HINDI to "सूर्य के झुकाव, अस्र छाया कारक और उच्च अक्षांश नियमों के खगोलीय समीकरण।"
        ),
        "src_iana_title" to mapOf(
            AppLanguage.ENGLISH to "IANA Time Zone Database",
            AppLanguage.URDU to "آئی اے این اے (IANA) ٹائم زون ڈیٹا بیس",
            AppLanguage.ARABIC to "قاعدة بيانات المناطق الزمنية (IANA)",
            AppLanguage.HINDI to "IANA समय-क्षेत्र डेटाबेस"
        ),
        "src_iana_desc" to mapOf(
            AppLanguage.ENGLISH to "Global city-specific IANA timezone IDs including regional daylight-saving rules.",
            AppLanguage.URDU to "دنیا کے ہر شہر کے درست مقامی ٹائم زون اور موسمی تبدیلی کا عالمی معیار۔",
            AppLanguage.ARABIC to "معرفات المناطق الزمنية الدقيقة لكل مدينة وقواعد التوقيت الصيفي العالمية.",
            AppLanguage.HINDI to "प्रत्येक शहर के सटीक समय क्षेत्र और डेलाइट-सेविंग नियमों का वैश्विक मानक।"
        ),
        "src_android_alarms_title" to mapOf(
            AppLanguage.ENGLISH to "Android Alarms & Scheduling Documentation",
            AppLanguage.URDU to "اینڈرائیڈ الارم اور شیڈولنگ دستاویزات",
            AppLanguage.ARABIC to "توثيق أندرويد لجدولة المنبهات",
            AppLanguage.HINDI to "एंड्रॉइड अलार्म और शेड्यूलिंग दस्तावेज़"
        ),
        "src_android_alarms_desc" to mapOf(
            AppLanguage.ENGLISH to "Official Android AlarmManager rules governing exact vs inexact alarms and battery policies.",
            AppLanguage.URDU to "درست وقت اور بیٹری بچت کی پالیسیوں کے مطابق اینڈرائیڈ الارم سسٹم کے اصول۔",
            AppLanguage.ARABIC to "سياسات أندرويد الرسمية لإدارة المنبهات الدقيقة والبدائل الموفرة للطاقة.",
            AppLanguage.HINDI to "सटीक बनाम अनुमानित अलार्म और बैटरी नीतियों हेतु आधिकारिक एंड्रॉइड दिशानिर्देश।"
        ),
        "src_android_notif_title" to mapOf(
            AppLanguage.ENGLISH to "Android Notification Runtime Permission",
            AppLanguage.URDU to "اینڈرائیڈ نوٹیفکیشن اجازت دستاویزات",
            AppLanguage.ARABIC to "توثيق إذن الإشعارات في أندرويد",
            AppLanguage.HINDI to "एंड्रॉइड सूचना अनुमति दस्तावेज़"
        ),
        "src_android_notif_desc" to mapOf(
            AppLanguage.ENGLISH to "Official Android 13+ notification permission standards protecting user control.",
            AppLanguage.URDU to "اینڈرائیڈ 13 اور جدید ورژن پر اطلاعات کی اجازت اور صارف کے اختیار کا معیار۔",
            AppLanguage.ARABIC to "معايير أندرويد 13+ الرسمية لمنح أذونات الإشعارات.",
            AppLanguage.HINDI to "एंड्रॉइड 13+ पर सूचना अनुमति के आधिकारिक मानक।"
        ),
        // Worldwide Prayer Times & Azan Notification Keys (Parts 2 & 3)
        "prayer_next_label" to mapOf(
            AppLanguage.ENGLISH to "Next Obligatory Prayer",
            AppLanguage.URDU to "اگلی فرض نماز",
            AppLanguage.ARABIC to "الصلاة المفروضة القادمة",
            AppLanguage.HINDI to "अगली फ़र्ज़ नमाज़"
        ),
        "prayer_today_schedule" to mapOf(
            AppLanguage.ENGLISH to "Today's Prayer Schedule",
            AppLanguage.URDU to "آج کے اوقاتِ نماز",
            AppLanguage.ARABIC to "مواقيت الصلاة اليوم",
            AppLanguage.HINDI to "आज की नमाज़ समय-सारणी"
        ),
        "prayer_calc_settings_title" to mapOf(
            AppLanguage.ENGLISH to "Prayer Calculation & Notification Settings",
            AppLanguage.URDU to "اوقاتِ نماز اور اذان کی ترتیبات",
            AppLanguage.ARABIC to "إعدادات حساب الصلاة وتنبيهات الأذان",
            AppLanguage.HINDI to "नमाज़ गणना और अज़ान सेटिंग्स"
        ),
        "prayer_method_label" to mapOf(
            AppLanguage.ENGLISH to "Calculation Method:",
            AppLanguage.URDU to "حساب کا طریقہ:",
            AppLanguage.ARABIC to "طريقة الحساب:",
            AppLanguage.HINDI to "गणना विधि:"
        ),
        "prayer_asr_label" to mapOf(
            AppLanguage.ENGLISH to "Asr Juristic Method:",
            AppLanguage.URDU to "عصر کا فقہی مسلک:",
            AppLanguage.ARABIC to "مذهب وقت العصر:",
            AppLanguage.HINDI to "अस्र फ़िक़ही विधि:"
        ),
        "prayer_high_lat_label" to mapOf(
            AppLanguage.ENGLISH to "High Latitude Rule:",
            AppLanguage.URDU to "بلند عرضِ بلد کا اصول:",
            AppLanguage.ARABIC to "قاعدة خطوط العرض العليا:",
            AppLanguage.HINDI to "उच्च अक्षांश नियम:"
        ),
        "prayer_custom_angles_title" to mapOf(
            AppLanguage.ENGLISH to "Custom Calculation Angles",
            AppLanguage.URDU to "حسبِ منشا فلکیاتی زاویے",
            AppLanguage.ARABIC to "زوايا الحساب المخصصة",
            AppLanguage.HINDI to "कस्टम गणना कोण"
        ),
        "prayer_custom_fajr_angle" to mapOf(
            AppLanguage.ENGLISH to "Fajr Angle (°)",
            AppLanguage.URDU to "فجر کا زاویہ (°)",
            AppLanguage.ARABIC to "زاوية الفجر (°)",
            AppLanguage.HINDI to "फ़ज्र कोण (°)"
        ),
        "prayer_custom_isha_interval_toggle" to mapOf(
            AppLanguage.ENGLISH to "Use fixed minutes after Maghrib for Isha",
            AppLanguage.URDU to "عشاء کے لیے مغرب کے بعد مقررہ منٹ استعمال کریں",
            AppLanguage.ARABIC to "استخدام دقائق ثابتة بعد المغرب للعشاء",
            AppLanguage.HINDI to "ईशा के लिए मग़रिब के बाद निश्चित मिनट उपयोग करें"
        ),
        "prayer_custom_isha_angle" to mapOf(
            AppLanguage.ENGLISH to "Isha Angle (°)",
            AppLanguage.URDU to "عشاء کا زاویہ (°)",
            AppLanguage.ARABIC to "زاوية العشاء (°)",
            AppLanguage.HINDI to "ईशा कोण (°)"
        ),
        "prayer_custom_isha_minutes" to mapOf(
            AppLanguage.ENGLISH to "Minutes after Maghrib",
            AppLanguage.URDU to "مغرب کے بعد منٹ",
            AppLanguage.ARABIC to "دقائق بعد المغرب",
            AppLanguage.HINDI to "मग़रिब के बाद मिनट"
        ),
        "prayer_manual_adj_label" to mapOf(
            AppLanguage.ENGLISH to "Manual Minute Adjustments (-30 to +30m):",
            AppLanguage.URDU to "دستی منٹ کی ترمیم (-30 تا +30 منٹ):",
            AppLanguage.ARABIC to "تعديل الدقائق يدوياً (-30 إلى +30):",
            AppLanguage.HINDI to "मैन्युअल मिनट समायोजन (-30 से +30):"
        ),
        "prayer_mosque_disclaimer" to mapOf(
            AppLanguage.ENGLISH to "Calculated offline on your device. Astronomical timings may differ slightly from your local mosque timetable; always follow your local mosque / religious authority.",
            AppLanguage.URDU to "یہ اوقات فون پر آف لائن فلکیاتی حساب سے نکالے جاتے ہیں اور مقامی مسجد کے وقت سے چند منٹ مختلف ہو سکتے ہیں؛ مقامی مسجد اور معتبر علماء کے اوقات کی پیروی کریں۔",
            AppLanguage.ARABIC to "تُحسب الأوقات محلياً دون اتصال وقد تختلف قليلاً عن تقويم المسجد القريب؛ يُنصح باتباع توقيت مسجدك المحلي.",
            AppLanguage.HINDI to "ये समय आपके डिवाइस पर ऑफ़लाइन गणना द्वारा निकाले गए हैं और स्थानीय मस्जिद के समय से थोड़े भिन्न हो सकते हैं; कृपया अपनी स्थानीय मस्जिद का पालन करें।"
        ),
        "prayer_timezone_label" to mapOf(
            AppLanguage.ENGLISH to "IANA Timezone",
            AppLanguage.URDU to "ٹائم زون (IANA)",
            AppLanguage.ARABIC to "المنطقة الزمنية",
            AppLanguage.HINDI to "समय क्षेत्र (IANA)"
        ),
        "prayer_monthly_title" to mapOf(
            AppLanguage.ENGLISH to "Monthly Timetable",
            AppLanguage.URDU to "ماہانہ اوقاتِ نماز",
            AppLanguage.ARABIC to "جدول الصلاة الشهري",
            AppLanguage.HINDI to "मासिक समय-सारणी"
        ),
        "prayer_share_timetable" to mapOf(
            AppLanguage.ENGLISH to "Share Timetable",
            AppLanguage.URDU to "ٹائم ٹیبل شیئر کریں",
            AppLanguage.ARABIC to "مشاركة الجدول",
            AppLanguage.HINDI to "सारणी साझा करें"
        ),
        "azan_audio_label" to mapOf(
            AppLanguage.ENGLISH to "Adhan Call & Notification Audio:",
            AppLanguage.URDU to "اذان اور نوٹیفکیشن کی آواز:",
            AppLanguage.ARABIC to "صوت الأذان والتنبيه:",
            AppLanguage.HINDI to "अज़ान और सूचना ध्वनि:"
        ),
        "azan_makkah" to mapOf(
            AppLanguage.ENGLISH to "Makkah",
            AppLanguage.URDU to "مکہ",
            AppLanguage.ARABIC to "مكة",
            AppLanguage.HINDI to "मक्का"
        ),
        "azan_madinah" to mapOf(
            AppLanguage.ENGLISH to "Madinah",
            AppLanguage.URDU to "مدینہ",
            AppLanguage.ARABIC to "المدينة",
            AppLanguage.HINDI to "मदीना"
        ),
        "azan_fajr" to mapOf(
            AppLanguage.ENGLISH to "Fajr Adhan",
            AppLanguage.URDU to "فجر اذان",
            AppLanguage.ARABIC to "أذان الفجر",
            AppLanguage.HINDI to "फ़ज्र अज़ान"
        ),
        "azan_silent" to mapOf(
            AppLanguage.ENGLISH to "Silent",
            AppLanguage.URDU to "خاموش",
            AppLanguage.ARABIC to "صامت",
            AppLanguage.HINDI to "साइलेंट"
        ),
        "azan_vibration_toggle" to mapOf(
            AppLanguage.ENGLISH to "Vibration on Prayer Alert",
            AppLanguage.URDU to "نماز الرٹ پر وائبریشن",
            AppLanguage.ARABIC to "الاهتزاز عند التنبيه",
            AppLanguage.HINDI to "अज़ान अलर्ट पर वाइब्रेशन"
        ),
        "azan_lead_time_label" to mapOf(
            AppLanguage.ENGLISH to "Reminder Before Prayer:",
            AppLanguage.URDU to "نماز سے پہلے یاد دہانی:",
            AppLanguage.ARABIC to "التذكير قبل الصلاة:",
            AppLanguage.HINDI to "नमाज़ से पहले अनुस्मारक:"
        ),
        "azan_lead_at_time" to mapOf(
            AppLanguage.ENGLISH to "At prayer time",
            AppLanguage.URDU to "عین وقت پر",
            AppLanguage.ARABIC to "في وقت الصلاة",
            AppLanguage.HINDI to "ठीक समय पर"
        ),
        "azan_test_btn" to mapOf(
            AppLanguage.ENGLISH to "Test Notification & Audio Preview",
            AppLanguage.URDU to "ٹیسٹ نوٹیفکیشن اور اذان سنیں",
            AppLanguage.ARABIC to "تجربة الإشعار ومعاينة الصوت",
            AppLanguage.HINDI to "परीक्षण सूचना और अज़ान सुनें"
        ),
        "azan_stop_btn" to mapOf(
            AppLanguage.ENGLISH to "Stop Audio",
            AppLanguage.URDU to "آواز بند کریں",
            AppLanguage.ARABIC to "إيقاف الصوت",
            AppLanguage.HINDI to "ऑडियो रोकें"
        ),
        "azan_perm_notif_title" to mapOf(
            AppLanguage.ENGLISH to "Notification Permission Required",
            AppLanguage.URDU to "نوٹیفکیشن کی اجازت درکار ہے",
            AppLanguage.ARABIC to "إذن الإشعارات مطلوب",
            AppLanguage.HINDI to "सूचना अनुमति आवश्यक है"
        ),
        "azan_perm_notif_desc" to mapOf(
            AppLanguage.ENGLISH to "Android 13+ requires notification permission to show prayer reminders and Adhan controls.",
            AppLanguage.URDU to "اینڈرائیڈ 13+ پر نماز کے اعلانات اور اذان دکھانے کے لیے نوٹیفکیشن کی اجازت ضروری ہے۔",
            AppLanguage.ARABIC to "يتطلب أندرويد 13+ إذن الإشعارات لعرض تنبيهات الصلاة والأذان.",
            AppLanguage.HINDI to "एंड्रॉइड 13+ पर नमाज़ अनुस्मारक और अज़ान दिखाने के लिए अनुमति आवश्यक है।"
        ),
        "azan_exact_alarm_title" to mapOf(
            AppLanguage.ENGLISH to "Exact Alarm Permission Not Granted",
            AppLanguage.URDU to "عین وقت کے الارم کی اجازت نہیں ملی",
            AppLanguage.ARABIC to "إذن المنبه الدقيق غير ممنوح",
            AppLanguage.HINDI to "सटीक अलार्म अनुमति नहीं दी गई"
        ),
        "azan_exact_alarm_desc" to mapOf(
            AppLanguage.ENGLISH to "Using Android inexact alarm fallback — alerts may arrive a few minutes late. Grant 'Alarms & Reminders' permission for on-time Azan.",
            AppLanguage.URDU to "اس وقت غیر دقیق الارم متبادل فعال ہے جس سے اذان چند منٹ تاخیر سے آ سکتی ہے۔ درست وقت کے لیے الارم کی اجازت دیں۔",
            AppLanguage.ARABIC to "يتم استخدام الجدولة المرنة كبديل — قد تتأخر التنبيهات بضع دقائق. امنح إذن المنبهات لدقة الأذان.",
            AppLanguage.HINDI to "अनुमानित अलार्म फ़ॉलबैक सक्रिय है — अलर्ट कुछ मिनट देरी से आ सकते हैं। सटीक अज़ान हेतु अनुमति दें।"
        ),
        "azan_grant_perm" to mapOf(
            AppLanguage.ENGLISH to "Grant",
            AppLanguage.URDU to "اجازت دیں",
            AppLanguage.ARABIC to "منح الإذن",
            AppLanguage.HINDI to "अनुमति दें"
        ),
        "azan_battery_title" to mapOf(
            AppLanguage.ENGLISH to "Battery Optimization Active",
            AppLanguage.URDU to "بیٹری آپٹیمائزیشن فعال ہے",
            AppLanguage.ARABIC to "تحسين البطارية نشط",
            AppLanguage.HINDI to "बैटरी अनुकूलन सक्रिय है"
        ),
        "azan_battery_desc" to mapOf(
            AppLanguage.ENGLISH to "Android may delay background alarms under strict battery saver. Set app battery usage to Unrestricted if Azan arrives late.",
            AppLanguage.URDU to "بیٹری سیور پسِ منظر میں اذان کے الارم کو مؤخر کر سکتا ہے۔ بہتر کارکردگی کے لیے ایپ کی بیٹری سیٹنگ غیر محدود رکھیں۔",
            AppLanguage.ARABIC to "قد يؤخر موفر البطارية تنبيهات الأذان في الخلفية. يُنصح بالسماح بالتشغيل غير المقيد للبطارية.",
            AppLanguage.HINDI to "बैटरी सेवर पृष्ठभूमि अलार्म में देरी कर सकता है। समय पर अज़ान के लिए अनरिस्ट्रिक्टेड बैटरी चुनें।"
        ),
        "azan_battery_settings_btn" to mapOf(
            AppLanguage.ENGLISH to "Battery Settings",
            AppLanguage.URDU to "بیٹری سیٹنگز",
            AppLanguage.ARABIC to "إعدادات البطارية",
            AppLanguage.HINDI to "बैटरी सेटिंग्स"
        ),
        "azan_rolling_status" to mapOf(
            AppLanguage.ENGLISH to "Rolling Auto-Replenishing Schedule Active",
            AppLanguage.URDU to "خودکار مسلسل الارم شیڈولنگ فعال ہے",
            AppLanguage.ARABIC to "الجدولة التلقائية المتجددة نشطة",
            AppLanguage.HINDI to "स्वचालित निरंतर अलार्म शेड्यूलिंग सक्रिय"
        ),
        "prayer_isha_next_day" to mapOf(
            AppLanguage.ENGLISH to "Fajr (Tomorrow)",
            AppLanguage.URDU to "فجر (کل)",
            AppLanguage.ARABIC to "الفجر (غداً)",
            AppLanguage.HINDI to "फ़ज्र (कल)"
        ),
        "gregorian_label" to mapOf(
            AppLanguage.ENGLISH to "Gregorian",
            AppLanguage.URDU to "عیسوی",
            AppLanguage.ARABIC to "ميلادي",
            AppLanguage.HINDI to "ग्रेगोरियन"
        ),
        "source_label" to mapOf(
            AppLanguage.ENGLISH to "Source",
            AppLanguage.URDU to "طریقۂ حساب",
            AppLanguage.ARABIC to "المصدر",
            AppLanguage.HINDI to "स्रोत"
        ),
        "hijri_reference_label" to mapOf(
            AppLanguage.ENGLISH to "Reference",
            AppLanguage.URDU to "حوالہ",
            AppLanguage.ARABIC to "المرجع",
            AppLanguage.HINDI to "संदर्भ"
        ),
        "prayer_perm_auto_title" to mapOf(
            AppLanguage.ENGLISH to "Auto Worldwide Location",
            AppLanguage.URDU to "خودکار عالمی مقام",
            AppLanguage.ARABIC to "الموقع التلقائي العالمي",
            AppLanguage.HINDI to "स्वचालित वैश्विक स्थान"
        ),
        "prayer_perm_auto_desc" to mapOf(
            AppLanguage.ENGLISH to "Allow device location to detect your city anywhere in the world offline or choose a city manually.",
            AppLanguage.URDU to "دنیا بھر میں خودکار طور پر اپنے شہر کے اوقاتِ نماز معلوم کرنے کے لیے لوکیشن کی اجازت دیں یا دستی شہر منتخب کریں۔",
            AppLanguage.ARABIC to "اسمح بالوصول إلى الموقع لتحديد مدينتك تلقائياً في أي مكان بالعالم، أو اختر مدينتك يدوياً.",
            AppLanguage.HINDI to "दुनिया भर में अपने शहर का स्वतः पता लगाने के लिए स्थान अनुमति दें या मैन्युअल रूप से शहर चुनें।"
        ),
        "prayer_gps_off_title" to mapOf(
            AppLanguage.ENGLISH to "Mobile Location (GPS) is OFF",
            AppLanguage.URDU to "موبائل لوکیشن (GPS) بند ہے",
            AppLanguage.ARABIC to "خدمة الموقع (GPS) متوقفة",
            AppLanguage.HINDI to "मोबाइल लोकेशन (GPS) बंद है"
        ),
        "prayer_gps_off_desc" to mapOf(
            AppLanguage.ENGLISH to "Turn ON location services so prayer times and Hijri date sync automatically with your location, or pick a city manually.",
            AppLanguage.URDU to "اوقاتِ نماز کو اپنے موجودہ مقام کے مطابق خودکار بنانے کے لیے موبائل لوکیشن آن کریں یا دستی شہر منتخب کریں۔",
            AppLanguage.ARABIC to "شغّل خدمات الموقع لمزامنة المواقيت مع موقعك تلقائياً أو اختر مدينتك يدوياً.",
            AppLanguage.HINDI to "नमाज़ के समय स्वतः अपडेट करने के लिए लोकेशन चालू करें या मैन्युअल शहर चुनें।"
        ),
        "prayer_select_city_title" to mapOf(
            AppLanguage.ENGLISH to "Select City Worldwide",
            AppLanguage.URDU to "دنیا بھر سے شہر منتخب کریں",
            AppLanguage.ARABIC to "اختر المدينة حول العالم",
            AppLanguage.HINDI to "दुनिया भर से शहर चुनें"
        ),
        "prayer_search_city_hint" to mapOf(
            AppLanguage.ENGLISH to "Search city or country (EN/UR/AR/HI)...",
            AppLanguage.URDU to "شہر یا ملک تلاش کریں (مثلاً لندن، مکہ، لاہور)...",
            AppLanguage.ARABIC to "ابحث عن المدينة أو الدولة...",
            AppLanguage.HINDI to "शहर या देश खोजें..."
        ),
        "prayer_manual_coords_title" to mapOf(
            AppLanguage.ENGLISH to "Manual Coordinates & Timezone",
            AppLanguage.URDU to "دستی عرض و طولِ بلد اور ٹائم زون",
            AppLanguage.ARABIC to "الإحداثيات اليدوية والمنطقة الزمنية",
            AppLanguage.HINDI to "मैन्युअल निर्देशांक और समय क्षेत्र"
        ),
        "prayer_manual_coords_desc" to mapOf(
            AppLanguage.ENGLISH to "Enter latitude, longitude, and select the city's official IANA timezone (DST handled automatically).",
            AppLanguage.URDU to "عرضِ بلد، طولِ بلد درج کریں اور شہر کا درست IANA ٹائم زون منتخب کریں۔",
            AppLanguage.ARABIC to "أدخل خطوط الطول والعرض واختر المنطقة الزمنية (IANA) الدقيقة للمدينة.",
            AppLanguage.HINDI to "अक्षांश, देशांतर दर्ज करें और शहर का सटीक IANA समय क्षेत्र चुनें।"
        ),
        "prayer_search_tz_hint" to mapOf(
            AppLanguage.ENGLISH to "Filter IANA timezones (e.g. America/New_York, Asia/Makassar)...",
            AppLanguage.URDU to "ٹائم زون تلاش کریں (مثلاً Asia/Karachi, America/Chicago)...",
            AppLanguage.ARABIC to "تصفية المناطق الزمنية (مثل Europe/London)...",
            AppLanguage.HINDI to "IANA समय क्षेत्र फ़िल्टर करें..."
        ),
        "prayer_city_label" to mapOf(
            AppLanguage.ENGLISH to "City Name",
            AppLanguage.URDU to "شہر کا نام",
            AppLanguage.ARABIC to "اسم المدينة",
            AppLanguage.HINDI to "शहर का नाम"
        ),
        "prayer_country_label" to mapOf(
            AppLanguage.ENGLISH to "Country Name",
            AppLanguage.URDU to "ملک کا نام",
            AppLanguage.ARABIC to "اسم الدولة",
            AppLanguage.HINDI to "देश का नाम"
        ),
        "prayer_close" to mapOf(
            AppLanguage.ENGLISH to "Close",
            AppLanguage.URDU to "بند کریں",
            AppLanguage.ARABIC to "إغلاق",
            AppLanguage.HINDI to "बंद करें"
        ),
        "prayer_auto_method_toggle" to mapOf(
            AppLanguage.ENGLISH to "Auto-recommend method when city changes",
            AppLanguage.URDU to "شہر بدلنے پر علاقائی طریقہ خودکار منتخب کریں",
            AppLanguage.ARABIC to "اختيار طريقة الحساب الإقليمية تلقائياً عند تغيير المدينة",
            AppLanguage.HINDI to "शहर बदलने पर क्षेत्रीय विधि स्वतः चुनें"
        ),
        "prayer_alarms_count_label" to mapOf(
            AppLanguage.ENGLISH to "Scheduled alarms in window: %d",
            AppLanguage.URDU to "شیڈول شدہ الارمز کی تعداد: %d",
            AppLanguage.ARABIC to "عدد التنبيهات المجدولة: %d",
            AppLanguage.HINDI to "शेड्यूल किए गए अलार्म: %d"
        ),
        "prayer_mode_exact" to mapOf(
            AppLanguage.ENGLISH to "Exact Alarm Mode Active",
            AppLanguage.URDU to "عین وقت کا الارم فعال ہے",
            AppLanguage.ARABIC to "وضع التنبيه الدقيق نشط",
            AppLanguage.HINDI to "सटीक अलार्म मोड सक्रिय"
        ),
        "prayer_mode_inexact" to mapOf(
            AppLanguage.ENGLISH to "Inexact Alarm Fallback Mode",
            AppLanguage.URDU to "تخمینی الارم متبادل موڈ",
            AppLanguage.ARABIC to "وضع التنبيه المرن الاحتياطي",
            AppLanguage.HINDI to "अनुमानित अलार्म फ़ॉलबैक मोड"
        ),
        "azan_stop_notif_action" to mapOf(
            AppLanguage.ENGLISH to "Stop Adhan",
            AppLanguage.URDU to "اذان بند کریں",
            AppLanguage.ARABIC to "إيقاف الأذان",
            AppLanguage.HINDI to "अज़ान रोकें"
        ),
        "day_sun" to mapOf(
            AppLanguage.ENGLISH to "Sun", AppLanguage.URDU to "اتوار", AppLanguage.ARABIC to "أحد", AppLanguage.HINDI to "रवि"
        ),
        "day_mon" to mapOf(
            AppLanguage.ENGLISH to "Mon", AppLanguage.URDU to "پیر", AppLanguage.ARABIC to "إثنين", AppLanguage.HINDI to "सोम"
        ),
        "day_tue" to mapOf(
            AppLanguage.ENGLISH to "Tue", AppLanguage.URDU to "منگل", AppLanguage.ARABIC to "ثلاثاء", AppLanguage.HINDI to "मंगल"
        ),
        "day_wed" to mapOf(
            AppLanguage.ENGLISH to "Wed", AppLanguage.URDU to "بدھ", AppLanguage.ARABIC to "أربعاء", AppLanguage.HINDI to "बुध"
        ),
        "day_thu" to mapOf(
            AppLanguage.ENGLISH to "Thu", AppLanguage.URDU to "جمعرات", AppLanguage.ARABIC to "خميس", AppLanguage.HINDI to "गुरु"
        ),
        "day_fri" to mapOf(
            AppLanguage.ENGLISH to "Fri", AppLanguage.URDU to "جمعہ", AppLanguage.ARABIC to "جمعة", AppLanguage.HINDI to "शुक्र"
        ),
        "day_sat" to mapOf(
            AppLanguage.ENGLISH to "Sat", AppLanguage.URDU to "ہفتہ", AppLanguage.ARABIC to "سبت", AppLanguage.HINDI to "शनि"
        ),
        "hadith_search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search Hadith text, narrator, book or #...",
            AppLanguage.URDU to "حدیث، راوی، کتاب یا نمبر تلاش کریں...",
            AppLanguage.ARABIC to "ابحث في متن الحديث أو الراوي أو الكتاب أو الرقم...",
            AppLanguage.HINDI to "हदीस, रावी, किताब या नंबर खोजें..."
        ),
        "hadith_collection_progress" to mapOf(
            AppLanguage.ENGLISH to "Collection Offline Progress",
            AppLanguage.URDU to "آف لائن مجموعے کی پیشرفت",
            AppLanguage.ARABIC to "تقدم التحميل دون اتصال",
            AppLanguage.HINDI to "ऑफ़लाइन संग्रह प्रगति"
        ),
        "hadith_manifest_btn" to mapOf(
            AppLanguage.ENGLISH to "Edition & Licence",
            AppLanguage.URDU to "نسخہ اور لائسنس",
            AppLanguage.ARABIC to "النسخة والترخيص",
            AppLanguage.HINDI to "संस्करण और लाइसेंस"
        ),
        "hadith_continue_reading" to mapOf(
            AppLanguage.ENGLISH to "Continue Reading",
            AppLanguage.URDU to "پڑھنا جاری رکھیں",
            AppLanguage.ARABIC to "متابعة القراءة",
            AppLanguage.HINDI to "पढ़ना जारी रखें"
        ),
        "hadith_all_volumes" to mapOf(
            AppLanguage.ENGLISH to "All 7 Vols (97 Books)",
            AppLanguage.URDU to "تمام 7 جلدیں (97 کتب)",
            AppLanguage.ARABIC to "جميع المجلدات ٧ (٩٧ كتاباً)",
            AppLanguage.HINDI to "सभी 7 जिल्दें (97 किताबें)"
        ),
        "hadith_download_book" to mapOf(
            AppLanguage.ENGLISH to "Download Full Book",
            AppLanguage.URDU to "مکمل کتاب ڈاؤن لوڈ کریں",
            AppLanguage.ARABIC to "تحميل الكتاب كاملاً",
            AppLanguage.HINDI to "पूरी किताब डाउनलोड करें"
        ),
        "hadith_download_volume" to mapOf(
            AppLanguage.ENGLISH to "Download Volume Offline",
            AppLanguage.URDU to "پوری جلد آف لائن ڈاؤن لوڈ کریں",
            AppLanguage.ARABIC to "تحميل المجلد كاملاً",
            AppLanguage.HINDI to "पूरी जिल्द डाउनलोड करें"
        ),
        "hadith_downloaded_badge" to mapOf(
            AppLanguage.ENGLISH to "Full Book Offline",
            AppLanguage.URDU to "مکمل کتاب آف لائن",
            AppLanguage.ARABIC to "الكتاب متاح دون اتصال",
            AppLanguage.HINDI to "पूरी किताब ऑफ़लाइन"
        ),
        "hadith_curated_only_badge" to mapOf(
            AppLanguage.ENGLISH to "Curated Core Offline • Tap Download for Full Book",
            AppLanguage.URDU to "منتخب احادیث آف لائن • مکمل کتاب ڈاؤن لوڈ کریں",
            AppLanguage.ARABIC to "نصوص مختارة • حمّل الكتاب الكامل",
            AppLanguage.HINDI to "चुनिंदा हदीसें ऑफ़लाइन • पूरी किताब डाउनलोड करें"
        ),
        "hadith_hindi_unavailable_notice" to mapOf(
            AppLanguage.ENGLISH to "Hindi translation is not included in the open-licensed full-book dataset; showing Arabic, English & Urdu.",
            AppLanguage.URDU to "اوپن سورس مکمل کتاب میں ہندی دستیاب نہیں؛ عربی، انگریزی اور اردو دکھائی جا رہی ہے۔",
            AppLanguage.ARABIC to "الترجمة الهندية غير متوفرة في النسخة الكاملة المفتوحة؛ يُعرض النص العربي والإنجليزي والأردي.",
            AppLanguage.HINDI to "ओपन-लाइसेंस पूर्ण पुस्तक डेटासेट में हिन्दी उपलब्ध नहीं है; अरबी, अंग्रेज़ी और उर्दू दिखाई जा रही है (चुनिंदा हदीसों में हिन्दी शामिल है)।"
        ),
        "hadith_open_source_link" to mapOf(
            AppLanguage.ENGLISH to "Open Verified Source",
            AppLanguage.URDU to "مستند اصل ماخذ کھولیں",
            AppLanguage.ARABIC to "فتح المصدر الموثق",
            AppLanguage.HINDI to "प्रामाणिक स्रोत खोलें"
        ),
        "hadith_narrator_label" to mapOf(
            AppLanguage.ENGLISH to "Narrator",
            AppLanguage.URDU to "راوی",
            AppLanguage.ARABIC to "الراوي",
            AppLanguage.HINDI to "रावी"
        ),
        "hadith_delete_offline" to mapOf(
            AppLanguage.ENGLISH to "Remove Offline Book",
            AppLanguage.URDU to "آف لائن کتاب حذف کریں",
            AppLanguage.ARABIC to "حذف الكتاب المحمّل",
            AppLanguage.HINDI to "ऑफ़लाइन किताब हटाएं"
        ),
        "hadith_books_count" to mapOf(
            AppLanguage.ENGLISH to "%d Books",
            AppLanguage.URDU to "%d کتب",
            AppLanguage.ARABIC to "%d كتاباً",
            AppLanguage.HINDI to "%d किताबें"
        ),
        "hadith_independent_muslim_note" to mapOf(
            AppLanguage.ENGLISH to "Sahih Muslim preserves its own independent 57-section canonical numbering (Introduction + Books 1–56).",
            AppLanguage.URDU to "صحیح مسلم کی ترتیب اور 57 ابواب کی نمبرنگ صحیح بخاری سے بالکل علیحدہ رکھی گئی ہے۔",
            AppLanguage.ARABIC to "يحتفظ صحيح مسلم بترقيمه المستقل (المقدمة + ٥٦ كتاباً) دون دمج مع صحيح البخاري.",
            AppLanguage.HINDI to "सहीह मुस्लिम की अपनी स्वतंत्र 57-अध्याय क्रम संख्या (परिचय + किताब 1–56) अलग रखी गई है।"
        ),
        "dua_audio_not_available" to mapOf(
            AppLanguage.ENGLISH to "Audio not available (no genuine licensed Qari recording for this Hadith text)",
            AppLanguage.URDU to "آڈیو دستیاب نہیں (اس حدیث کے لیے مستند قاری کی ریکارڈنگ موجود نہیں)",
            AppLanguage.ARABIC to "الصوت غير متوفر (لا يوجد تسجيل قرآني/صوتي موثق لهذا الدعاء)",
            AppLanguage.HINDI to "ऑडियो उपलब्ध नहीं (इस हदीस के लिए प्रामाणिक क़ारी रिकॉर्डिंग उपलब्ध नहीं)"
        ),
        "dua_genuine_audio_badge" to mapOf(
            AppLanguage.ENGLISH to "Genuine Qari Recitation",
            AppLanguage.URDU to "اصلی تلاوتِ قاری",
            AppLanguage.ARABIC to "تلاوة قارئ حقيقية",
            AppLanguage.HINDI to "वास्तविक क़ारी तिलावत"
        ),
        "dua_download_audio_pack" to mapOf(
            AppLanguage.ENGLISH to "Download Quranic Dua Audio",
            AppLanguage.URDU to "قرآنی دعاؤں کی آڈیو ڈاؤن لوڈ کریں",
            AppLanguage.ARABIC to "تحميل صوتيات الأدعية القرآنية",
            AppLanguage.HINDI to "क़ुरआनी दुआ ऑडियो डाउनलोड करें"
        ),
        "dua_audio_offline_ready" to mapOf(
            AppLanguage.ENGLISH to "Offline Qari Audio Ready",
            AppLanguage.URDU to "آڈیو آف لائن تیار ہے",
            AppLanguage.ARABIC to "الصوت متاح دون اتصال",
            AppLanguage.HINDI to "ऑफ़लाइन ऑडियो तैयार"
        ),
        "dua_reciting_loop" to mapOf(
            AppLanguage.ENGLISH to "Reciting %d/%d",
            AppLanguage.URDU to "تلاوت %d/%d",
            AppLanguage.ARABIC to "تكرار %d/%d",
            AppLanguage.HINDI to "तिलावत %d/%d"
        ),
        "dua_buffering" to mapOf(
            AppLanguage.ENGLISH to "Loading Audio...",
            AppLanguage.URDU to "آڈیو لوڈ ہو رہی ہے...",
            AppLanguage.ARABIC to "جاري تحميل الصوت...",
            AppLanguage.HINDI to "ऑडियो लोड हो रहा है..."
        ),
        "dua_all_4_languages" to mapOf(
            AppLanguage.ENGLISH to "All Languages",
            AppLanguage.URDU to "تمام زبانیں",
            AppLanguage.ARABIC to "جميع اللغات",
            AppLanguage.HINDI to "सभी भाषाएं"
        ),
        "dua_hide_translations" to mapOf(
            AppLanguage.ENGLISH to "Hide Translations",
            AppLanguage.URDU to "تراجم چھپائیں",
            AppLanguage.ARABIC to "إخفاء الترجمات",
            AppLanguage.HINDI to "अनुवाद छिपाएं"
        ),
        "dua_recite_details" to mapOf(
            AppLanguage.ENGLISH to "Details & Method",
            AppLanguage.URDU to "تفصیل اور طریقہ",
            AppLanguage.ARABIC to "التفاصيل والطريقة",
            AppLanguage.HINDI to "विवरण व तरीका"
        ),
        "dua_method_label" to mapOf(
            AppLanguage.ENGLISH to "When & How to Practice:",
            AppLanguage.URDU to "پڑھنے کا وقت اور عملی طریقہ:",
            AppLanguage.ARABIC to "وقت الذكر وكيفية العمل به:",
            AppLanguage.HINDI to "पढ़ने का समय व अमल का तरीका:"
        ),
        "dua_no_favorites" to mapOf(
            AppLanguage.ENGLISH to "No favorite Duas yet. Tap the heart icon on any Dua to save it here.",
            AppLanguage.URDU to "ابھی کوئی پسندیدہ دعا محفوظ نہیں۔ دل کے آئیکن پر ٹیپ کر کے محفوظ کریں۔",
            AppLanguage.ARABIC to "لا توجد أدعية مفضلة بعد. اضغط على أيقونة القلب لحفظ الدعاء.",
            AppLanguage.HINDI to "अभी कोई पसंदीदा दुआ सहेजी नहीं गई। सहेजने के लिए दिल आइकन दबाएं।"
        ),
        "dua_no_results" to mapOf(
            AppLanguage.ENGLISH to "No Duas found matching your search.",
            AppLanguage.URDU to "تلاش کے مطابق کوئی دعا نہیں ملی۔",
            AppLanguage.ARABIC to "لم يتم العثور على أدعية مطابقة.",
            AppLanguage.HINDI to "खोज से मेल खाती कोई दुआ नहीं मिली।"
        ),
        "bookmark_saved" to mapOf(
            AppLanguage.ENGLISH to "Saved to bookmarks",
            AppLanguage.URDU to "بک مارکس میں محفوظ ہو گیا",
            AppLanguage.ARABIC to "تم الحفظ في المفضلة",
            AppLanguage.HINDI to "बुकमार्क में सहेजा गया"
        ),
        "bookmark_removed" to mapOf(
            AppLanguage.ENGLISH to "Removed from bookmarks",
            AppLanguage.URDU to "بک مارک ہٹا دیا گیا",
            AppLanguage.ARABIC to "تمت الإزالة من المفضلة",
            AppLanguage.HINDI to "बुकमार्क हटा दिया गया"
        ),
        "downloads_hadith_section" to mapOf(
            AppLanguage.ENGLISH to "Hadith Collections Offline Storage",
            AppLanguage.URDU to "حدیث کتب کا آف لائن ذخیرہ",
            AppLanguage.ARABIC to "تخزين كتب الحديث دون اتصال",
            AppLanguage.HINDI to "हदीस संग्रह ऑफ़लाइन भंडारण"
        ),
        "downloads_dua_audio_section" to mapOf(
            AppLanguage.ENGLISH to "Quranic Dua Audio Cache",
            AppLanguage.URDU to "قرآنی دعاؤں کی آڈیو کیش",
            AppLanguage.ARABIC to "ملفات صوتيات الأدعية القرآنية",
            AppLanguage.HINDI to "क़ुरआनी दुआ ऑडियो कैश"
        ),
        // Islamic Lectures & Official YouTube RSS Feed Localization
        "lectures_banner_tag" to mapOf(
            AppLanguage.ENGLISH to "VERIFIED ISLAMIC LECTURES",
            AppLanguage.URDU to "مستند اسلامی بیانات و دروس",
            AppLanguage.ARABIC to "محاضرات ودروس إسلامية موثقة",
            AppLanguage.HINDI to "प्रामाणिक इस्लामिक व्याख्यान"
        ),
        "lectures_header_title" to mapOf(
            AppLanguage.ENGLISH to "Authentic Lectures From Verified Channels",
            AppLanguage.URDU to "مستند علمی چینلز سے دینی دروس و بیانات",
            AppLanguage.ARABIC to "محاضرات أصيلة من قنوات العلماء الرسمية",
            AppLanguage.HINDI to "सत्यापित विद्वानों के आधिकारिक व्याख्यान"
        ),
        "lectures_header_subtitle" to mapOf(
            AppLanguage.ENGLISH to "Streams official public YouTube channel feeds cleanly in-app. Metadata caches locally for offline browsing; video playback requires internet.",
            AppLanguage.URDU to "مستند یوٹیوب چینلز کی سرکاری فیڈ سے براہِ راست ایپ میں دروس دیکھیں۔ فہرست آف لائن محفوظ رہتی ہے جبکہ ویڈیو چلانے کے لیے انٹرنیٹ درکار ہے۔",
            AppLanguage.ARABIC to "يعرض التحديثات الرسمية لقنوات العلماء عبر يوتيوب داخل التطبيق. يتم حفظ البيانات مؤقتاً دون اتصال، بينما يتطلب تشغيل الفيديو اتصالاً بالإنترنت.",
            AppLanguage.HINDI to "आधिकारिक सार्वजनिक यूट्यूब चैनलों के व्याख्यान ऐप में देखें। सूची ऑफ़लाइन सहेजी रहती है; वीडियो चलाने के लिए इंटरनेट आवश्यक है।"
        ),
        "lectures_search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search verified feeds by scholar, topic or keyword...",
            AppLanguage.URDU to "عالم، موضوع یا عنوان کے مطابق تلاش کریں...",
            AppLanguage.ARABIC to "ابحث في القنوات الموثقة بالعالم أو الموضوع...",
            AppLanguage.HINDI to "विद्वान, विषय या शीर्षक से खोजें..."
        ),
        "lectures_search_btn" to mapOf(
            AppLanguage.ENGLISH to "Search",
            AppLanguage.URDU to "تلاش",
            AppLanguage.ARABIC to "بحث",
            AppLanguage.HINDI to "खोजें"
        ),
        "lectures_clear" to mapOf(
            AppLanguage.ENGLISH to "Clear",
            AppLanguage.URDU to "صاف کریں",
            AppLanguage.ARABIC to "مسح",
            AppLanguage.HINDI to "साफ़ करें"
        ),
        "lectures_refresh" to mapOf(
            AppLanguage.ENGLISH to "Refresh Feeds",
            AppLanguage.URDU to "تازہ کریں",
            AppLanguage.ARABIC to "تحديث القوائم",
            AppLanguage.HINDI to "रीफ़्रेश करें"
        ),
        "lectures_retry" to mapOf(
            AppLanguage.ENGLISH to "Retry",
            AppLanguage.URDU to "دوبارہ کوشش کریں",
            AppLanguage.ARABIC to "إعادة المحاولة",
            AppLanguage.HINDI to "पुनः प्रयास करें"
        ),
        "lectures_verified_scholars" to mapOf(
            AppLanguage.ENGLISH to "VERIFIED OFFICIAL CHANNELS",
            AppLanguage.URDU to "مستند سرکاری چینلز",
            AppLanguage.ARABIC to "القنوات الرسمية الموثقة",
            AppLanguage.HINDI to "सत्यापित आधिकारिक चैनल"
        ),
        "lectures_all_channels" to mapOf(
            AppLanguage.ENGLISH to "All Verified Channels",
            AppLanguage.URDU to "تمام مستند چینلز",
            AppLanguage.ARABIC to "جميع القنوات الموثقة",
            AppLanguage.HINDI to "सभी सत्यापित चैनल"
        ),
        "lectures_categories_title" to mapOf(
            AppLanguage.ENGLISH to "ISLAMIC CATEGORIES",
            AppLanguage.URDU to "اسلامی موضوعات",
            AppLanguage.ARABIC to "الأقسام الإسلامية",
            AppLanguage.HINDI to "इस्लामिक श्रेणियां"
        ),
        "lectures_saved_favorites" to mapOf(
            AppLanguage.ENGLISH to "Saved Favorites",
            AppLanguage.URDU to "پسندیدہ دروس",
            AppLanguage.ARABIC to "المحاضرات المفضلة",
            AppLanguage.HINDI to "पसंदीदा व्याख्यान"
        ),
        "lectures_recently_watched" to mapOf(
            AppLanguage.ENGLISH to "Recently Watched",
            AppLanguage.URDU to "حالیہ دیکھے گئے",
            AppLanguage.ARABIC to "شوهدت مؤخراً",
            AppLanguage.HINDI to "हाल ही में देखे गए"
        ),
        "lectures_all_languages" to mapOf(
            AppLanguage.ENGLISH to "All Languages",
            AppLanguage.URDU to "تمام زبانیں",
            AppLanguage.ARABIC to "كل اللغات",
            AppLanguage.HINDI to "सभी भाषाएं"
        ),
        "lectures_lang_en" to mapOf(
            AppLanguage.ENGLISH to "English",
            AppLanguage.URDU to "انگریزی",
            AppLanguage.ARABIC to "الإنجليزية",
            AppLanguage.HINDI to "अंग्रेज़ी"
        ),
        "lectures_lang_ur" to mapOf(
            AppLanguage.ENGLISH to "Urdu",
            AppLanguage.URDU to "اردو",
            AppLanguage.ARABIC to "الأردية",
            AppLanguage.HINDI to "उर्दू"
        ),
        "lectures_lang_ar" to mapOf(
            AppLanguage.ENGLISH to "Arabic",
            AppLanguage.URDU to "عربی",
            AppLanguage.ARABIC to "العربية",
            AppLanguage.HINDI to "अरबी"
        ),
        "lectures_lang_ur_hi" to mapOf(
            AppLanguage.ENGLISH to "Urdu / Hindi",
            AppLanguage.URDU to "اردو / ہندی",
            AppLanguage.ARABIC to "الأردية / الهندية",
            AppLanguage.HINDI to "उर्दू / हिंदी"
        ),
        "lectures_lang_unknown" to mapOf(
            AppLanguage.ENGLISH to "Language Unconfirmed",
            AppLanguage.URDU to "زبان غیر مصدقہ",
            AppLanguage.ARABIC to "اللغة غير مؤكدة",
            AppLanguage.HINDI to "भाषा अपुष्ट"
        ),
        "lectures_watch_in_app" to mapOf(
            AppLanguage.ENGLISH to "Play In-App",
            AppLanguage.URDU to "ایپ میں چلائیں",
            AppLanguage.ARABIC to "تشغيل داخل التطبيق",
            AppLanguage.HINDI to "ऐप में चलाएं"
        ),
        "lectures_watch_on_youtube" to mapOf(
            AppLanguage.ENGLISH to "Watch on YouTube",
            AppLanguage.URDU to "یوٹیوب پر دیکھیں",
            AppLanguage.ARABIC to "المشاهدة على يوتيوب",
            AppLanguage.HINDI to "यूट्यूब पर देखें"
        ),
        "lectures_bookmark_add" to mapOf(
            AppLanguage.ENGLISH to "Save Lecture",
            AppLanguage.URDU to "محفوظ کریں",
            AppLanguage.ARABIC to "حفظ المحاضرة",
            AppLanguage.HINDI to "सहेजें"
        ),
        "lectures_bookmark_remove" to mapOf(
            AppLanguage.ENGLISH to "Saved",
            AppLanguage.URDU to "محفوظ شدہ",
            AppLanguage.ARABIC to "محفوظة",
            AppLanguage.HINDI to "सहेजा गया"
        ),
        "lectures_published_label" to mapOf(
            AppLanguage.ENGLISH to "Published",
            AppLanguage.URDU to "تاریخِ اشاعت",
            AppLanguage.ARABIC to "تاريخ النشر",
            AppLanguage.HINDI to "प्रकाशन तिथि"
        ),
        "lectures_loading_feeds" to mapOf(
            AppLanguage.ENGLISH to "Loading official YouTube channel feeds...",
            AppLanguage.URDU to "سرکاری یوٹیوب فیڈز لوڈ ہو رہی ہیں...",
            AppLanguage.ARABIC to "جارٍ تحميل خلاصات القنوات الرسمية...",
            AppLanguage.HINDI to "आधिकारिक यूट्यूब फ़ीड लोड हो रही हैं..."
        ),
        "lectures_offline_cache_banner" to mapOf(
            AppLanguage.ENGLISH to "Offline mode — showing cached lecture metadata. Connect to the internet to stream video or refresh feeds.",
            AppLanguage.URDU to "آف لائن موڈ — محفوظ شدہ فہرست دکھائی جا رہی ہے۔ ویڈیو چلانے یا تازہ کرنے کے لیے انٹرنیٹ درکار ہے۔",
            AppLanguage.ARABIC to "وضع عدم الاتصال — يتم عرض البيانات المحفوظة مؤقتاً. يلزم الاتصال بالإنترنت لتشغيل الفيديو أو التحديث.",
            AppLanguage.HINDI to "ऑफ़लाइन मोड — सहेजी गई व्याख्यान सूची दिखाई जा रही है। वीडियो चलाने या रीफ़्रेश करने के लिए इंटरनेट आवश्यक है।"
        ),
        "lectures_offline_empty_error" to mapOf(
            AppLanguage.ENGLISH to "No internet connection and no cached lecture metadata available yet.",
            AppLanguage.URDU to "انٹرنیٹ دستیاب نہیں اور ابھی کوئی محفوظ شدہ فہرست موجود نہیں۔",
            AppLanguage.ARABIC to "لا يوجد اتصال بالإنترنت ولا تتوفر بيانات محفوظة بعد.",
            AppLanguage.HINDI to "इंटरनेट उपलब्ध नहीं है और अभी कोई कैश किया गया डेटा मौजूद नहीं है।"
        ),
        "lectures_partial_unavailable" to mapOf(
            AppLanguage.ENGLISH to "Some channel feeds were temporarily unavailable: %s",
            AppLanguage.URDU to "کچھ چینل فیڈز عارضی طور پر دستیاب نہیں ہو سکیں: %s",
            AppLanguage.ARABIC to "تعذر الوصول مؤقتاً إلى بعض القنوات: %s",
            AppLanguage.HINDI to "कुछ चैनल फ़ीड अस्थायी रूप से अनुपलब्ध रहीं: %s"
        ),
        "lectures_empty_search_title" to mapOf(
            AppLanguage.ENGLISH to "No matching lectures found",
            AppLanguage.URDU to "کوئی متعلقہ بیان نہیں ملا",
            AppLanguage.ARABIC to "لم يتم العثور على محاضرات مطابقة",
            AppLanguage.HINDI to "कोई मेल खाता व्याख्यान नहीं मिला"
        ),
        "lectures_empty_search_desc" to mapOf(
            AppLanguage.ENGLISH to "Search filters only across loaded verified scholar channel feeds (no unrelated fallback videos are substituted). Global YouTube keyword search requires YouTube Data API v3.",
            AppLanguage.URDU to "تلاش صرف مستند چینلز کی لوڈ شدہ فیڈز میں سے کی جاتی ہے (کوئی غیر متعلقہ ویڈیو خودکار طور پر نہیں دکھائی جاتی)۔ عالمی یوٹیوب سرچ کے لیے یوٹیوب ڈیٹا API v3 درکار ہوتی ہے۔",
            AppLanguage.ARABIC to "يقتصر البحث على القنوات الإسلامية الموثقة المحمّلة حالياً دون استبدال بنتائج غير ذات صلة. البحث العام عبر يوتيوب يتطلب واجهة YouTube Data API v3.",
            AppLanguage.HINDI to "खोज केवल सत्यापित चैनलों की लोड की गई फ़ीड में होती है (असंबंधित वीडियो नहीं दिखाए जाते)। वैश्विक यूट्यूब खोज के लिए YouTube Data API v3 आवश्यक है।"
        ),
        "lectures_empty_favorites" to mapOf(
            AppLanguage.ENGLISH to "No saved lectures yet. Tap the bookmark icon on any lecture card to save it here.",
            AppLanguage.URDU to "ابھی کوئی بیان محفوظ نہیں کیا گیا۔ محفوظ کرنے کے لیے کسی بھی کارڈ پر بک مارک آئیکن دبائیں۔",
            AppLanguage.ARABIC to "لا توجد محاضرات محفوظة بعد. اضغط على أيقونة الحفظ لإضافتها هنا.",
            AppLanguage.HINDI to "अभी कोई व्याख्यान सहेजा नहीं गया है। सहेजने के लिए बुकमार्क आइकन दबाएं।"
        ),
        "lectures_empty_recent" to mapOf(
            AppLanguage.ENGLISH to "No recently watched lectures yet. Select any lecture to start watching.",
            AppLanguage.URDU to "ابھی کوئی حالیہ دیکھا گیا بیان موجود نہیں۔ دیکھنے کے لیے کوئی بھی درس منتخب کریں۔",
            AppLanguage.ARABIC to "لم تشاهد أي محاضرة مؤخراً. اختر محاضرة لبدء المشاهدة.",
            AppLanguage.HINDI to "अभी कोई हालिया देखा गया व्याख्यान नहीं है। देखने के लिए कोई व्याख्यान चुनें।"
        ),
        "lectures_results_count" to mapOf(
            AppLanguage.ENGLISH to "%d lectures available",
            AppLanguage.URDU to "%d دروس دستیاب ہیں",
            AppLanguage.ARABIC to "%d محاضرة متاحة",
            AppLanguage.HINDI to "%d व्याख्यान उपलब्ध हैं"
        ),
        "lectures_select_prompt" to mapOf(
            AppLanguage.ENGLISH to "Select a lecture below to watch inside the official YouTube player",
            AppLanguage.URDU to "ایپ میں سرکاری یوٹیوب پلیئر پر دیکھنے کے لیے نیچے سے کوئی بیان منتخب کریں",
            AppLanguage.ARABIC to "اختر محاضرة أدناه للمشاهدة عبر مشغل يوتيوب الرسمي",
            AppLanguage.HINDI to "आधिकारिक यूट्यूब प्लेयर में देखने के लिए नीचे से एक व्याख्यान चुनें"
        )
    )

    private fun stringsPart2(): Map<String, Map<AppLanguage, String>> = mapOf(
        // 12 Lecture Categories
        "lecture_cat_popular_scholars" to mapOf(
            AppLanguage.ENGLISH to "Popular Scholars",
            AppLanguage.URDU to "معروف علمائے کرام",
            AppLanguage.ARABIC to "أشهر العلماء",
            AppLanguage.HINDI to "प्रमुख विद्वान"
        ),
        "lecture_cat_latest" to mapOf(
            AppLanguage.ENGLISH to "Latest Lectures",
            AppLanguage.URDU to "تازہ ترین بیانات",
            AppLanguage.ARABIC to "أحدث المحاضرات",
            AppLanguage.HINDI to "नवीनतम व्याख्यान"
        ),
        "lecture_cat_tafseer" to mapOf(
            AppLanguage.ENGLISH to "Quran Tafseer",
            AppLanguage.URDU to "تفسیرِ قرآن",
            AppLanguage.ARABIC to "تفسير القرآن",
            AppLanguage.HINDI to "क़ुरआन तफ़सीर"
        ),
        "lecture_cat_quran_explanation" to mapOf(
            AppLanguage.ENGLISH to "Quran Explanation",
            AppLanguage.URDU to "فہم و تدبرِ قرآن",
            AppLanguage.ARABIC to "تدبر وبيان القرآن",
            AppLanguage.HINDI to "क़ुरआन व्याख्या"
        ),
        "lecture_cat_hadith" to mapOf(
            AppLanguage.ENGLISH to "Hadith",
            AppLanguage.URDU to "درسِ حدیث",
            AppLanguage.ARABIC to "الحديث الشريف",
            AppLanguage.HINDI to "हदीस"
        ),
        "lecture_cat_seerah" to mapOf(
            AppLanguage.ENGLISH to "Seerah",
            AppLanguage.URDU to "سیرت النبی ﷺ",
            AppLanguage.ARABIC to "السيرة النبوية",
            AppLanguage.HINDI to "सीरत-उन-नबी ﷺ"
        ),
        "lecture_cat_history" to mapOf(
            AppLanguage.ENGLISH to "Islamic History",
            AppLanguage.URDU to "اسلامی تاریخ",
            AppLanguage.ARABIC to "التاريخ الإسلامي",
            AppLanguage.HINDI to "इस्लामी इतिहास"
        ),
        "lecture_cat_salah" to mapOf(
            AppLanguage.ENGLISH to "Salah and Worship",
            AppLanguage.URDU to "نماز اور عبادات",
            AppLanguage.ARABIC to "الصلاة والعبادات",
            AppLanguage.HINDI to "नमाज़ और इबादत"
        ),
        "lecture_cat_manners" to mapOf(
            AppLanguage.ENGLISH to "Islamic Manners",
            AppLanguage.URDU to "اسلامی اخلاق و آداب",
            AppLanguage.ARABIC to "الأخلاق والآداب",
            AppLanguage.HINDI to "इस्लामी शिष्टाचार"
        ),
        "lecture_cat_family" to mapOf(
            AppLanguage.ENGLISH to "Family and Society",
            AppLanguage.URDU to "خاندان اور معاشرہ",
            AppLanguage.ARABIC to "الأسرة والمجتمع",
            AppLanguage.HINDI to "परिवार और समाज"
        ),
        "lecture_cat_new_muslim" to mapOf(
            AppLanguage.ENGLISH to "New Muslim Education",
            AppLanguage.URDU to "نو مسلم رہنمائی",
            AppLanguage.ARABIC to "تعليم المسلمين الجدد",
            AppLanguage.HINDI to "नव-मुस्लिम मार्गदर्शन"
        ),
        "lecture_cat_qa" to mapOf(
            AppLanguage.ENGLISH to "Islamic Questions & Answers",
            AppLanguage.URDU to "سوال و جواب (مسائل)",
            AppLanguage.ARABIC to "الفتاوى والأسئلة الشرعية",
            AppLanguage.HINDI to "इस्लामी प्रश्नोत्तर"
        ),
        "lecture_cat_prophets" to mapOf(
            AppLanguage.ENGLISH to "Stories of the Prophets",
            AppLanguage.URDU to "قصص الانبیاء",
            AppLanguage.ARABIC to "قصص الأنبياء",
            AppLanguage.HINDI to "पैग़ंबरों के क़िस्से"
        ),
        "lecture_cat_battles" to mapOf(
            AppLanguage.ENGLISH to "Islamic Battles & Expeditions",
            AppLanguage.URDU to "غزوات و اسلامی جنگیں",
            AppLanguage.ARABIC to "الغزوات والمعارك الإسلامية",
            AppLanguage.HINDI to "ग़ज़वात और इस्लामी युद्ध"
        ),
        "lecture_cat_sahaba" to mapOf(
            AppLanguage.ENGLISH to "Sahaba Stories",
            AppLanguage.URDU to "واقعاتِ صحابہ کرام",
            AppLanguage.ARABIC to "قصص الصحابة",
            AppLanguage.HINDI to "सहाबा के वाक़िआत"
        ),
        "lecture_cat_caliphates" to mapOf(
            AppLanguage.ENGLISH to "Islamic Caliphates & Civilization",
            AppLanguage.URDU to "خلافت اور اسلامی تہذیب",
            AppLanguage.ARABIC to "الخلافة والحضارة الإسلامية",
            AppLanguage.HINDI to "इस्लामी ख़िलाफ़त और सभ्यता"
        ),
        "lectures_duration_min_fmt" to mapOf(
            AppLanguage.ENGLISH to "Long-form %d+ min",
            AppLanguage.URDU to "طویل بیانات %d+ منٹ",
            AppLanguage.ARABIC to "محاضرات طويلة %d+ دقيقة",
            AppLanguage.HINDI to "लंबे व्याख्यान %d+ मिनट"
        ),
        "lectures_duration_confirmed_fmt" to mapOf(
            AppLanguage.ENGLISH to "Long-Form • %s",
            AppLanguage.URDU to "طویل بیان • %s",
            AppLanguage.ARABIC to "محاضرة طويلة • %s",
            AppLanguage.HINDI to "लंबा व्याख्यान • %s"
        ),
        "lectures_duration_unverified_badge" to mapOf(
            AppLanguage.ENGLISH to "Duration unverified in RSS",
            AppLanguage.URDU to "دورانیہ RSS میں غیر مصدقہ",
            AppLanguage.ARABIC to "المدة غير متوفرة في RSS",
            AppLanguage.HINDI to "RSS में अवधि अपुष्ट"
        ),
        "lectures_duration_rss_notice" to mapOf(
            AppLanguage.ENGLISH to "Long-form filter (%d+ min, Shorts excluded). YouTube public RSS feeds omit video duration; durations are verified honestly when loaded in the player.",
            AppLanguage.URDU to "طویل بیانات فلٹر (%d+ منٹ، شارٹس خارج)۔ یوٹیوب RSS میں دورانیہ شامل نہیں ہوتا؛ پلیئر میں کھلنے پر حقیقی دورانیہ تصدیق ہوتا ہے۔",
            AppLanguage.ARABIC to "فلتر المحاضرات الطويلة (%d+ دقيقة، استبعاد المقاطع القصيرة). خلاصات يوتيوب RSS لا توفر المدة؛ يتم التحقق منها عند التشغيل.",
            AppLanguage.HINDI to "लंबे व्याख्यान फ़िल्टर (%d+ मिनट, शॉर्ट्स बाहर)। YouTube RSS में अवधि नहीं होती; प्लेयर में लोड होने पर वास्तविक अवधि सत्यापित होती है।"
        ),
        "lectures_duration_only_confirmed" to mapOf(
            AppLanguage.ENGLISH to "Confirmed Long-Form Only",
            AppLanguage.URDU to "صرف مصدقہ طویل بیانات",
            AppLanguage.ARABIC to "المؤكدة الطويلة فقط",
            AppLanguage.HINDI to "केवल सत्यापित लंबे वीडियो"
        ),
        "lectures_duration_include_unverified" to mapOf(
            AppLanguage.ENGLISH to "Exclude Shorts (Show RSS)",
            AppLanguage.URDU to "شارٹس خارج (RSS دکھائیں)",
            AppLanguage.ARABIC to "استبعاد القصير (عرض RSS)",
            AppLanguage.HINDI to "शॉर्ट्स हटाएं (RSS दिखाएं)"
        ),
        "lectures_shorts_filtered_fmt" to mapOf(
            AppLanguage.ENGLISH to "%d short clips excluded",
            AppLanguage.URDU to "%d مختصر کلپس خارج کیے گئے",
            AppLanguage.ARABIC to "تم استبعاد %d مقاطع قصيرة",
            AppLanguage.HINDI to "%d छोटे क्लिप हटाए गए"
        ),
        "lectures_short_detected_warning" to mapOf(
            AppLanguage.ENGLISH to "Verified duration is under %d minutes (short clip).",
            AppLanguage.URDU to "مصدقہ دورانیہ %d منٹ سے کم ہے (مختصر کلپ)۔",
            AppLanguage.ARABIC to "المدة المؤكدة أقل من %d دقائق (مقطع قصير).",
            AppLanguage.HINDI to "सत्यापित अवधि %d मिनट से कम है (छोटा क्लिप)।"
        ),
        "lectures_directory_btn" to mapOf(
            AppLanguage.ENGLISH to "Channel Directory (%d Verified)",
            AppLanguage.URDU to "چینل ڈائریکٹری (%d مصدقہ)",
            AppLanguage.ARABIC to "دليل القنوات (%d موثقة)",
            AppLanguage.HINDI to "चैनल निर्देशिका (%d सत्यापित)"
        ),
        "lectures_directory_title" to mapOf(
            AppLanguage.ENGLISH to "Verified Islamic Channel Directory",
            AppLanguage.URDU to "مصدقہ اسلامی چینل ڈائریکٹری",
            AppLanguage.ARABIC to "دليل القنوات الإسلامية الموثقة",
            AppLanguage.HINDI to "सत्यापित इस्लामी चैनल निर्देशिका"
        ),
        "lectures_directory_target_note" to mapOf(
            AppLanguage.ENGLISH to "Only verified official channels with canonical YouTube UC IDs appear in the feed. Unverified candidates are held separately until ownership and RSS XML are confirmed.",
            AppLanguage.URDU to "فیڈ میں صرف مصدقہ آفیشل چینلز (مستند UC ID کے ساتھ) شامل کیے جاتے ہیں۔ غیر مصدقہ امیدوار علیحدہ رکھے جاتے ہیں۔",
            AppLanguage.ARABIC to "تظهر القنوات الرسمية الموثقة فقط بمعرّف UC الصحيح. تُحفظ القنوات المرشحة منفصلة حتى يتم التحقق منها.",
            AppLanguage.HINDI to "फ़ीड में केवल सत्यापित आधिकारिक चैनल (UC ID के साथ) दिखाए जाते हैं। असत्यापित चैनल अलग रखे जाते हैं।"
        ),
        "lectures_load_more_channels" to mapOf(
            AppLanguage.ENGLISH to "Load More Verified Channels (%d/%d)",
            AppLanguage.URDU to "مزید مصدقہ چینلز لوڈ کریں (%d/%d)",
            AppLanguage.ARABIC to "تحميل المزيد من القنوات الموثقة (%d/%d)",
            AppLanguage.HINDI to "और सत्यापित चैनल लोड करें (%d/%d)"
        ),
        // Live Makkah & Madinah Verification & Player Error States
        "live_tab_makkah" to mapOf(
            AppLanguage.ENGLISH to "Live Makkah",
            AppLanguage.URDU to "لائیو مکہ مکرمہ",
            AppLanguage.ARABIC to "بث مكة مباشر",
            AppLanguage.HINDI to "लाइव मक्का"
        ),
        "live_tab_madinah" to mapOf(
            AppLanguage.ENGLISH to "Live Madinah",
            AppLanguage.URDU to "لائیو مدینہ منورہ",
            AppLanguage.ARABIC to "بث المدينة مباشر",
            AppLanguage.HINDI to "लाइव मदीना"
        ),
        "live_badge_confirmed" to mapOf(
            AppLanguage.ENGLISH to "LIVE",
            AppLanguage.URDU to "براہِ راست (LIVE)",
            AppLanguage.ARABIC to "مباشر الآن",
            AppLanguage.HINDI to "लाइव"
        ),
        "live_status_checking" to mapOf(
            AppLanguage.ENGLISH to "Checking official broadcast...",
            AppLanguage.URDU to "سرکاری نشریات کی تصدیق ہو رہی ہے...",
            AppLanguage.ARABIC to "جارٍ التحقق من البث الرسمي...",
            AppLanguage.HINDI to "आधिकारिक प्रसारण की जांच हो रही है..."
        ),
        "live_status_no_active" to mapOf(
            AppLanguage.ENGLISH to "No active live broadcast",
            AppLanguage.URDU to "اس وقت کوئی لائیو نشریات فعال نہیں",
            AppLanguage.ARABIC to "لا يوجد بث مباشر نشط حالياً",
            AppLanguage.HINDI to "अभी कोई सक्रिय लाइव प्रसारण नहीं है"
        ),
        "live_status_unavailable" to mapOf(
            AppLanguage.ENGLISH to "Currently unavailable",
            AppLanguage.URDU to "اس وقت دستیاب نہیں",
            AppLanguage.ARABIC to "غير متاح حالياً",
            AppLanguage.HINDI to "वर्तमान में अनुपलब्ध"
        ),
        "live_status_offline" to mapOf(
            AppLanguage.ENGLISH to "Offline — connect to internet",
            AppLanguage.URDU to "آف لائن — انٹرنیٹ سے منسلک ہوں",
            AppLanguage.ARABIC to "غير متصل بالإنترنت",
            AppLanguage.HINDI to "ऑफ़लाइन — इंटरनेट से जुड़ें"
        ),
        "live_no_active_desc" to mapOf(
            AppLanguage.ENGLISH to "The official Saudi broadcaster feed did not confirm an active live stream ID right now. Recorded uploads are not labeled as live.",
            AppLanguage.URDU to "سرکاری سعودی چینل کی جانب سے اس وقت کوئی فعال لائیو اسٹریمنگ تصدیق نہیں ہوئی۔ ریکارڈ شدہ ویڈیوز کو جھوٹا لائیو نہیں دکھایا جاتا۔",
            AppLanguage.ARABIC to "لم يتم تأكيد بث حي نشط من القناة السعودية الرسمية في هذه اللحظة، ولا يتم عرض التسجيلات المسجلة كبث مباشر.",
            AppLanguage.HINDI to "आधिकारिक सऊदी चैनल से अभी कोई सक्रिय लाइव स्ट्रीम सत्यापित नहीं हुई। रिकॉर्डेड वीडियो को लाइव नहीं दिखाया जाता।"
        ),
        "live_unavailable_desc" to mapOf(
            AppLanguage.ENGLISH to "Unable to verify or embed the official live broadcast right now. You can retry or open the official broadcaster channel on YouTube.",
            AppLanguage.URDU to "سرکاری لائیو نشریات کی تصدیق یا ایپ میں پلے بیک اس وقت ممکن نہیں ہو سکا۔ آپ دوبارہ کوشش کر سکتے ہیں یا سرکاری یوٹیوب چینل کھول سکتے ہیں۔",
            AppLanguage.ARABIC to "تعذر التحقق من البث المباشر أو تضمينه حالياً. يمكنك إعادة المحاولة أو فتح القناة الرسمية على يوتيوب.",
            AppLanguage.HINDI to "आधिकारिक लाइव प्रसारण अभी सत्यापित या एम्बेड नहीं हो सका। आप पुनः प्रयास कर सकते हैं या यूट्यूब पर आधिकारिक चैनल खोल सकते हैं।"
        ),
        "live_open_official_channel" to mapOf(
            AppLanguage.ENGLISH to "Open Official Channel on YouTube",
            AppLanguage.URDU to "سرکاری یوٹیوب چینل کھولیں",
            AppLanguage.ARABIC to "فتح القناة الرسمية على يوتيوب",
            AppLanguage.HINDI to "यूट्यूब पर आधिकारिक चैनल खोलें"
        ),
        "live_policy_footer" to mapOf(
            AppLanguage.ENGLISH to "Plays official Saudi Broadcasting Authority streams (Saudi Quran TV for Makkah, Saudi Sunnah TV for Madinah) with automatic fallback through verified relays. Status shows LIVE only when active playback is confirmed.",
            AppLanguage.URDU to "سرکاری سعودی نشریات (مکہ کے لیے سعودی قرآن ٹی وی، مدینہ کے لیے سعودی سنہ ٹی وی) مستند ریلے ذرائع کے خودکار بیک اپ کے ساتھ چلائی جاتی ہیں۔ لائیو بیج صرف حقیقی پلے بیک کی تصدیق پر دکھایا جاتا ہے۔",
            AppLanguage.ARABIC to "يعرض البث الرسمي لهيئة الإذاعة والتلفزيون السعودية (قناة القرآن الكريم لمكة وقناة السنة النبوية للمدينة) مع التحويل التلقائي عبر مصادر موثقة. لا تظهر شارة مباشر إلا بعد تأكيد التشغيل الفعلي.",
            AppLanguage.HINDI to "आधिकारिक सऊदी प्रसारण (मक्का के लिए सऊदी कुरान टीवी, मदीना के लिए सऊदी सुन्नह टीवी) सत्यापित रिले फ़ॉलबैक के साथ चलता है। लाइव बैज केवल वास्तविक प्लेबैक की पुष्टि होने पर ही दिखाया जाता है।"
        ),
        "player_connecting" to mapOf(
            AppLanguage.ENGLISH to "Initializing Official YouTube Player...",
            AppLanguage.URDU to "آفیشل یوٹیوب پلیئر لوڈ ہو رہا ہے...",
            AppLanguage.ARABIC to "جارٍ تهيئة مشغل يوتيوب الرسمي...",
            AppLanguage.HINDI to "आधिकारिक यूट्यूब प्लेयर लोड हो रहा है..."
        ),
        "player_error_title" to mapOf(
            AppLanguage.ENGLISH to "Playback Unavailable",
            AppLanguage.URDU to "ویڈیو دستیاب نہیں",
            AppLanguage.ARABIC to "تعذر تشغيل الفيديو",
            AppLanguage.HINDI to "वीडियो उपलब्ध नहीं है"
        ),
        "player_error_embed_disabled" to mapOf(
            AppLanguage.ENGLISH to "The channel owner has disabled embedded playback for this video (Error %d). You can watch it directly on YouTube.",
            AppLanguage.URDU to "اس ویڈیو کے مالک نے دیگر ایپس میں ایمبیڈ پلے بیک بند کر رکھا ہے (Error %d)۔ آپ اسے براہِ راست یوٹیوب پر دیکھ سکتے ہیں۔",
            AppLanguage.ARABIC to "قام مالك القناة بتعطيل التضمين لهذا الفيديو (الرمز %d). يمكنك مشاهدته مباشرة على يوتيوب.",
            AppLanguage.HINDI to "चैनल स्वामी ने इस वीडियो का एम्बेडेड प्लेबैक अक्षम किया है (त्रुटि %d)। आप इसे सीधे यूट्यूब पर देख सकते हैं।"
        ),
        "player_error_153" to mapOf(
            AppLanguage.ENGLISH to "Player configuration or origin verification error (Error 153). Please retry or watch directly on YouTube.",
            AppLanguage.URDU to "پلیئر کنفیگریشن یا اوریجن کی تصدیق کا مسئلہ (Error 153)۔ براہِ کرم دوبارہ کوشش کریں یا یوٹیوب پر دیکھیں۔",
            AppLanguage.ARABIC to "خطأ في إعدادات المشغل أو التحقق من المصدر (الرمز 153). يرجى إعادة المحاولة أو المشاهدة على يوتيوب.",
            AppLanguage.HINDI to "प्लेयर कॉन्फ़िगरेशन या मूल सत्यापन त्रुटि (त्रुटि 153)। कृपया पुनः प्रयास करें या यूट्यूब पर देखें।"
        ),
        "player_error_not_found" to mapOf(
            AppLanguage.ENGLISH to "This video is private, removed, or unavailable (Error 100).",
            AppLanguage.URDU to "یہ ویڈیو نجی (پرائیویٹ) ہے، حذف ہو چکی ہے یا دستیاب نہیں (Error 100)۔",
            AppLanguage.ARABIC to "هذا الفيديو خاص أو محذوف أو غير متاح (الرمز 100).",
            AppLanguage.HINDI to "यह वीडियो निजी है, हटा दिया गया है या अनुपलब्ध है (त्रुटि 100)।"
        ),
        "player_error_network" to mapOf(
            AppLanguage.ENGLISH to "Network connection lost or player could not reach YouTube.",
            AppLanguage.URDU to "انٹرنیٹ کنکشن منقطع ہو گیا یا یوٹیوب پلیئر تک رسائی نہیں ہو سکی۔",
            AppLanguage.ARABIC to "انقطع الاتصال بالشبكة أو تعذر الوصول إلى مشغل يوتيوب.",
            AppLanguage.HINDI to "नेटवर्क कनेक्शन टूट गया या यूट्यूब प्लेयर तक पहुंच नहीं हो सकी।"
        ),
        "player_error_renderer" to mapOf(
            AppLanguage.ENGLISH to "The video player renderer stopped unexpectedly. Tap Retry to reload.",
            AppLanguage.URDU to "ویڈیو رینڈرر عارضی طور پر رک گیا۔ دوبارہ چلانے کے لیے ری ٹرائی دبائیں۔",
            AppLanguage.ARABIC to "توقف عارض الفيديو بشكل غير متوقع. اضغط إعادة المحاولة.",
            AppLanguage.HINDI to "वीडियो रेंडरर अप्रत्याशित रूप से रुक गया। पुनः लोड करने के लिए रीट्राई दबाएं।"
        ),
        "player_error_generic" to mapOf(
            AppLanguage.ENGLISH to "Unable to play this video in-app right now (Code %d).",
            AppLanguage.URDU to "اس وقت یہ ویڈیو ایپ میں نہیں چلائی جا سکی (Code %d)۔",
            AppLanguage.ARABIC to "تعذر تشغيل هذا الفيديو داخل التطبيق حالياً (الرمز %d).",
            AppLanguage.HINDI to "अभी यह वीडियो ऐप में नहीं चलाया जा सका (कोड %d)।"
        ),
        // ==================== ISLAMIC PODCASTS PLATFORM ====================
        "pod_search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search verified shows, scholars & episodes...",
            AppLanguage.URDU to "مستند پروگرامز، علماء اور اقساط تلاش کریں...",
            AppLanguage.ARABIC to "ابحث في البرامج الموثوقة والحلقات والمشايخ...",
            AppLanguage.HINDI to "सत्यापित शो, विद्वान और एपिसोड खोजें..."
        ),
        "pod_all_categories" to mapOf(
            AppLanguage.ENGLISH to "All Categories",
            AppLanguage.URDU to "تمام زمرے",
            AppLanguage.ARABIC to "جميع التصنيفات",
            AppLanguage.HINDI to "सभी श्रेणियाँ"
        ),
        "pod_all_shows" to mapOf(
            AppLanguage.ENGLISH to "All Verified Shows",
            AppLanguage.URDU to "تمام مستند شوز",
            AppLanguage.ARABIC to "جميع البرامج الموثوقة",
            AppLanguage.HINDI to "सभी सत्यापित शो"
        ),
        "pod_downloaded_only" to mapOf(
            AppLanguage.ENGLISH to "Downloaded Offline",
            AppLanguage.URDU to "آف لائن ڈاؤنلوڈ شدہ",
            AppLanguage.ARABIC to "التنزيلات المتاحة دون اتصال",
            AppLanguage.HINDI to "ऑफ़लाइन डाउनलोड किए गए"
        ),
        "pod_verified_rss" to mapOf(
            AppLanguage.ENGLISH to "Verified Official RSS Feeds",
            AppLanguage.URDU to "مستند سرکاری RSS فیڈز",
            AppLanguage.ARABIC to "موجز RSS رسمي موثوق",
            AppLanguage.HINDI to "सत्यापित आधिकारिक RSS फ़ीड"
        ),
        "pod_loading" to mapOf(
            AppLanguage.ENGLISH to "Fetching verified Islamic podcast feeds...",
            AppLanguage.URDU to "مستند اسلامی پوڈکاسٹ فیڈز لوڈ ہو رہی ہیں...",
            AppLanguage.ARABIC to "جارٍ جلب موجزات البودكاست الإسلامي الموثوقة...",
            AppLanguage.HINDI to "सत्यापित इस्लामिक पॉडकास्ट फ़ीड लोड हो रहे हैं..."
        ),
        "pod_offline_banner" to mapOf(
            AppLanguage.ENGLISH to "Offline mode: showing cached RSS episodes and downloaded audio.",
            AppLanguage.URDU to "آف لائن موڈ: محفوظ شدہ RSS اقساط اور ڈاؤنلوڈ شدہ آڈیو دکھائے جا رہے ہیں۔",
            AppLanguage.ARABIC to "وضع عدم الاتصال: عرض الحلقات المخزنة والملفات الصوتية المنزلة.",
            AppLanguage.HINDI to "ऑफ़लाइन मोड: सहेजे गए RSS एपिसोड और डाउनलोड किया गया ऑडियो दिखाया जा रहा है।"
        ),
        "pod_empty_state" to mapOf(
            AppLanguage.ENGLISH to "No verified podcast episodes match your filter.",
            AppLanguage.URDU to "آپ کے فلٹر کے مطابق کوئی مستند پوڈکاسٹ قسط نہیں ملی۔",
            AppLanguage.ARABIC to "لا توجد حلقات بودكاست موثوقة تطابق خيارك الحالي.",
            AppLanguage.HINDI to "आपके फ़िल्टर से मेल खाने वाला कोई सत्यापित पॉडकास्ट एपिसोड नहीं मिला।"
        ),
        "pod_cat_quran_reflections" to mapOf(
            AppLanguage.ENGLISH to "Quran Reflections",
            AppLanguage.URDU to "تدبرِ قرآن",
            AppLanguage.ARABIC to "تدبر القرآن",
            AppLanguage.HINDI to "क़ुरआन चिंतन"
        ),
        "pod_cat_quran_tafseer" to mapOf(
            AppLanguage.ENGLISH to "Quran Tafseer",
            AppLanguage.URDU to "تفسیرِ قرآن",
            AppLanguage.ARABIC to "تفسير القرآن",
            AppLanguage.HINDI to "क़ुरआन तफ़सीर"
        ),
        "pod_cat_hadith_sunnah" to mapOf(
            AppLanguage.ENGLISH to "Hadith and Sunnah",
            AppLanguage.URDU to "حدیث و سنت",
            AppLanguage.ARABIC to "الحديث والسنة",
            AppLanguage.HINDI to "हदीस और सुन्नत"
        ),
        "pod_cat_seerah" to mapOf(
            AppLanguage.ENGLISH to "Seerah",
            AppLanguage.URDU to "سیرتِ نبوی ﷺ",
            AppLanguage.ARABIC to "السيرة النبوية",
            AppLanguage.HINDI to "सीरत-उन-नबी ﷺ"
        ),
        "pod_cat_islamic_history" to mapOf(
            AppLanguage.ENGLISH to "Islamic History",
            AppLanguage.URDU to "تاریخِ اسلام",
            AppLanguage.ARABIC to "التاريخ الإسلامي",
            AppLanguage.HINDI to "इस्लामी इतिहास"
        ),
        "pod_cat_islamic_knowledge" to mapOf(
            AppLanguage.ENGLISH to "Islamic Knowledge",
            AppLanguage.URDU to "علومِ اسلامیہ",
            AppLanguage.ARABIC to "العلم الشرعي",
            AppLanguage.HINDI to "इस्लामी ज्ञान"
        ),
        "pod_cat_spiritual_development" to mapOf(
            AppLanguage.ENGLISH to "Spiritual Development",
            AppLanguage.URDU to "روحانی تربیت و تزکیہ",
            AppLanguage.ARABIC to "تزكية النفس والروحانيات",
            AppLanguage.HINDI to "आध्यात्मिक विकास"
        ),
        "pod_cat_muslim_family" to mapOf(
            AppLanguage.ENGLISH to "Muslim Family",
            AppLanguage.URDU to "مسلم خاندان و معاشرہ",
            AppLanguage.ARABIC to "الأسرة المسلمة",
            AppLanguage.HINDI to "मुस्लिम परिवार"
        ),
        "pod_cat_islamic_manners" to mapOf(
            AppLanguage.ENGLISH to "Islamic Manners",
            AppLanguage.URDU to "اسلامی آداب و اخلاق",
            AppLanguage.ARABIC to "الآداب والأخلاق الإسلامية",
            AppLanguage.HINDI to "इस्लामी शिष्टाचार व अख़लाक़"
        ),
        "pod_cat_islamic_education" to mapOf(
            AppLanguage.ENGLISH to "Islamic Education",
            AppLanguage.URDU to "اسلامی تعلیم و تربیت",
            AppLanguage.ARABIC to "التعليم الإسلامي",
            AppLanguage.HINDI to "इस्लामी शिक्षा"
        ),
        "pod_cat_new_muslim_guidance" to mapOf(
            AppLanguage.ENGLISH to "New Muslim Guidance",
            AppLanguage.URDU to "نو مسلم رہنمائی",
            AppLanguage.ARABIC to "إرشاد المسلمين الجدد",
            AppLanguage.HINDI to "नव-मुस्लिम मार्गदर्शन"
        ),
        "pod_cat_urdu_podcasts" to mapOf(
            AppLanguage.ENGLISH to "Urdu Podcasts",
            AppLanguage.URDU to "اردو پوڈکاسٹس",
            AppLanguage.ARABIC to "بودكاست بالأردية",
            AppLanguage.HINDI to "उर्दू पॉडकास्ट"
        ),
        "pod_cat_english_podcasts" to mapOf(
            AppLanguage.ENGLISH to "English Podcasts",
            AppLanguage.URDU to "انگریزی پوڈکاسٹس",
            AppLanguage.ARABIC to "بودكاست بالإنجليزية",
            AppLanguage.HINDI to "अंग्रेज़ी पॉडकास्ट"
        ),
        "pod_cat_arabic_podcasts" to mapOf(
            AppLanguage.ENGLISH to "Arabic Podcasts",
            AppLanguage.URDU to "عربی پوڈکاسٹس",
            AppLanguage.ARABIC to "بودكاست بالعربية",
            AppLanguage.HINDI to "अरबी पॉडकास्ट"
        ),
        "pod_play" to mapOf(
            AppLanguage.ENGLISH to "Play",
            AppLanguage.URDU to "چلائیں",
            AppLanguage.ARABIC to "تشغيل",
            AppLanguage.HINDI to "चलाएं"
        ),
        "pod_pause" to mapOf(
            AppLanguage.ENGLISH to "Pause",
            AppLanguage.URDU to "روکیں",
            AppLanguage.ARABIC to "إيقاف مؤقت",
            AppLanguage.HINDI to "रोकें"
        ),
        "pod_stop" to mapOf(
            AppLanguage.ENGLISH to "Stop",
            AppLanguage.URDU to "بند کریں",
            AppLanguage.ARABIC to "إيقاف",
            AppLanguage.HINDI to "बंद करें"
        ),
        "pod_next" to mapOf(
            AppLanguage.ENGLISH to "Next Episode",
            AppLanguage.URDU to "اگلی قسط",
            AppLanguage.ARABIC to "الحلقة التالية",
            AppLanguage.HINDI to "अगला एपिसोड"
        ),
        "pod_prev" to mapOf(
            AppLanguage.ENGLISH to "Previous Episode",
            AppLanguage.URDU to "پچھلی قسط",
            AppLanguage.ARABIC to "الحلقة السابقة",
            AppLanguage.HINDI to "पिछला एपिसोड"
        ),
        "pod_queue" to mapOf(
            AppLanguage.ENGLISH to "Episode Queue",
            AppLanguage.URDU to "پلے لسٹ قطار",
            AppLanguage.ARABIC to "قائمة التشغيل",
            AppLanguage.HINDI to "एपिसोड कतार"
        ),
        "pod_add_queue" to mapOf(
            AppLanguage.ENGLISH to "Add to Queue",
            AppLanguage.URDU to "قطار میں شامل کریں",
            AppLanguage.ARABIC to "أضف إلى القائمة",
            AppLanguage.HINDI to "कतार में जोड़ें"
        ),
        "pod_download" to mapOf(
            AppLanguage.ENGLISH to "Download",
            AppLanguage.URDU to "ڈاؤنلوڈ",
            AppLanguage.ARABIC to "تنزيل",
            AppLanguage.HINDI to "डाउनलोड"
        ),
        "pod_downloading" to mapOf(
            AppLanguage.ENGLISH to "Downloading",
            AppLanguage.URDU to "ڈاؤنلوڈ جاری ہے",
            AppLanguage.ARABIC to "جارٍ التنزيل",
            AppLanguage.HINDI to "डाउनलोड हो रहा है"
        ),
        "pod_resume" to mapOf(
            AppLanguage.ENGLISH to "Resume",
            AppLanguage.URDU to "دوبارہ شروع کریں",
            AppLanguage.ARABIC to "استئناف",
            AppLanguage.HINDI to "फिर से शुरू करें"
        ),
        "pod_delete_offline" to mapOf(
            AppLanguage.ENGLISH to "Delete Download",
            AppLanguage.URDU to "ڈاؤنلوڈ حذف کریں",
            AppLanguage.ARABIC to "حذف التنزيل",
            AppLanguage.HINDI to "डाउनलोड हटाएं"
        ),
        "pod_offline_ready" to mapOf(
            AppLanguage.ENGLISH to "Saved Offline",
            AppLanguage.URDU to "آف لائن محفوظ",
            AppLanguage.ARABIC to "محفوظ دون اتصال",
            AppLanguage.HINDI to "ऑफ़लाइन सहेजा गया"
        ),
        "pod_buffering" to mapOf(
            AppLanguage.ENGLISH to "Buffering stream...",
            AppLanguage.URDU to "آڈیو لوڈ ہو رہی ہے...",
            AppLanguage.ARABIC to "جارٍ التحميل المؤقت...",
            AppLanguage.HINDI to "बफ़रिंग हो रही है..."
        ),
        "pod_speed" to mapOf(
            AppLanguage.ENGLISH to "Speed",
            AppLanguage.URDU to "رفتار",
            AppLanguage.ARABIC to "السرعة",
            AppLanguage.HINDI to "गति"
        ),
        "pod_refresh" to mapOf(
            AppLanguage.ENGLISH to "Refresh RSS Feeds",
            AppLanguage.URDU to "RSS فیڈز تازہ کریں",
            AppLanguage.ARABIC to "تحديث الموجزات",
            AppLanguage.HINDI to "RSS फ़ीड रीफ़्रेश करें"
        ),
        "lectures_select_topic_prompt" to mapOf(
            AppLanguage.ENGLISH to "Select a topic category above to fetch verified YouTube lectures from relevant official channels.",
            AppLanguage.URDU to "متعلقہ سرکاری چینلز سے مستند یوٹیوب دروس لوڈ کرنے کے لیے اوپر سے کوئی موضوع منتخب کریں۔",
            AppLanguage.ARABIC to "اختر تصنيفاً أعلاه لجلب المحاضرات الموثوقة من القنوات الرسمية ذات الصلة.",
            AppLanguage.HINDI to "संबंधित आधिकारिक चैनलों से सत्यापित यूट्यूब व्याख्यान लोड करने के लिए ऊपर से एक विषय चुनें।"
        ),
        "lectures_no_topic_videos" to mapOf(
            AppLanguage.ENGLISH to "No verified videos are currently available for this topic.",
            AppLanguage.URDU to "اس موضوع کے لیے اس وقت کوئی مستند ویڈیو دستیاب نہیں ہے۔",
            AppLanguage.ARABIC to "لا تتوفر حالياً مقاطع فيديو موثوقة لهذا الموضوع.",
            AppLanguage.HINDI to "इस विषय के लिए वर्तमान में कोई सत्यापित वीडियो उपलब्ध नहीं है।"
        ),
        "lectures_invalid_mapping" to mapOf(
            AppLanguage.ENGLISH to "The selected topic or channel mapping is invalid or missing. Please select another topic.",
            AppLanguage.URDU to "منتخب کردہ موضوع یا چینل کی ترتیب درست نہیں یا موجود نہیں۔ براہ کرم کوئی دوسرا موضوع منتخب کریں۔",
            AppLanguage.ARABIC to "تعيين الموضوع أو القناة المحددة غير صالح أو مفقود. يرجى اختيار موضوع آخر.",
            AppLanguage.HINDI to "चयनित विषय या चैनल मैपिंग अमान्य या अनुपस्थित है। कृपया कोई अन्य विषय चुनें।"
        ),
        "lectures_last_updated" to mapOf(
            AppLanguage.ENGLISH to "Last updated: %s",
            AppLanguage.URDU to "آخری تازہ کاری: %s",
            AppLanguage.ARABIC to "آخر تحديث: %s",
            AppLanguage.HINDI to "अंतिम अपडेट: %s"
        ),
        "lectures_expired_cache_banner" to mapOf(
            AppLanguage.ENGLISH to "Showing previously cached feed (%s). Live refresh failed — tap Refresh to try again.",
            AppLanguage.URDU to "پہلے سے محفوظ شدہ فہرست دکھائی جا رہی ہے (%s)۔ تازہ کاری ناکام رہی — دوبارہ کوشش کریں۔",
            AppLanguage.ARABIC to "عرض بيانات مخزنة سابقاً (%s). تعذر التحديث المباشر — اضغط تحديث للمحاولة.",
            AppLanguage.HINDI to "पहले से कैश की गई फ़ीड दिखाई जा रही है (%s)। रीफ़्रेश विफल रहा — पुनः प्रयास करें।"
        ),
        "lectures_local_search_note" to mapOf(
            AppLanguage.ENGLISH to "Filters loaded topic videos locally (global YouTube search requires API credentials).",
            AppLanguage.URDU to "صرف لوڈ شدہ موضوع کی ویڈیوز میں تلاش کرتا ہے (عالمی سرچ کے لیے API درکار ہے)۔",
            AppLanguage.ARABIC to "تصفية داخل مقاطع الموضوع المحمّلة فقط (البحث الشامل يتطلب مفتاح API).",
            AppLanguage.HINDI to "केवल लोड किए गए विषय के वीडियो में फ़िल्टर करता है (वैश्विक खोज के लिए API आवश्यक है)।"
        ),
        "pod_select_language_prompt" to mapOf(
            AppLanguage.ENGLISH to "Select a podcast language (Urdu, Arabic, or English) to load verified YouTube podcast episodes.",
            AppLanguage.URDU to "مستند یوٹیوب پوڈکاسٹ اقساط لوڈ کرنے کے لیے زبان (اردو، عربی یا انگریزی) منتخب کریں۔",
            AppLanguage.ARABIC to "اختر لغة البودكاست (الأردية أو العربية أو الإنجليزية) لتحميل حلقات يوتيوب الموثوقة.",
            AppLanguage.HINDI to "सत्यापित यूट्यूब पॉडकास्ट एपिसोड लोड करने के लिए भाषा (उर्दू, अरबी या अंग्रेज़ी) चुनें।"
        ),
        "pod_lang_urdu" to mapOf(
            AppLanguage.ENGLISH to "Urdu Podcasts",
            AppLanguage.URDU to "اردو پوڈکاسٹس",
            AppLanguage.ARABIC to "بودكاست أردو",
            AppLanguage.HINDI to "उर्दू पॉडकास्ट"
        ),
        "pod_lang_arabic" to mapOf(
            AppLanguage.ENGLISH to "Arabic Podcasts",
            AppLanguage.URDU to "عربی پوڈکاسٹس",
            AppLanguage.ARABIC to "بودكاست عربي",
            AppLanguage.HINDI to "अरबी पॉडकास्ट"
        ),
        "pod_lang_english" to mapOf(
            AppLanguage.ENGLISH to "English Podcasts",
            AppLanguage.URDU to "انگریزی پوڈکاسٹس",
            AppLanguage.ARABIC to "بودكاست إنجليزي",
            AppLanguage.HINDI to "अंग्रेज़ी पॉडकास्ट"
        ),
        "pod_lang_not_verified" to mapOf(
            AppLanguage.ENGLISH to "Language Not Verified",
            AppLanguage.URDU to "زبان غیر مصدقہ",
            AppLanguage.ARABIC to "لغة غير مؤكدة",
            AppLanguage.HINDI to "भाषा सत्यापित नहीं"
        ),
        "pod_favorites_tab" to mapOf(
            AppLanguage.ENGLISH to "Saved Favorites",
            AppLanguage.URDU to "محفوظ پسندیدہ",
            AppLanguage.ARABIC to "المفضلة المحفوظة",
            AppLanguage.HINDI to "सहेजे गए पसंदीदा"
        ),
        "pod_recent_tab" to mapOf(
            AppLanguage.ENGLISH to "Recently Watched",
            AppLanguage.URDU to "حالیہ دیکھے گئے",
            AppLanguage.ARABIC to "شوهد مؤخراً",
            AppLanguage.HINDI to "हाल ही में देखे गए"
        ),
        "pod_topic_all" to mapOf(
            AppLanguage.ENGLISH to "All Topics",
            AppLanguage.URDU to "تمام موضوعات",
            AppLanguage.ARABIC to "كل المواضيع",
            AppLanguage.HINDI to "सभी विषय"
        ),
        "pod_topic_quran" to mapOf(
            AppLanguage.ENGLISH to "Quran & Tafseer",
            AppLanguage.URDU to "قرآن و تفسیر",
            AppLanguage.ARABIC to "القرآن والتفسير",
            AppLanguage.HINDI to "क़ुरआन व तफ़सीर"
        ),
        "pod_topic_seerah" to mapOf(
            AppLanguage.ENGLISH to "Seerah & History",
            AppLanguage.URDU to "سیرت و تاریخ",
            AppLanguage.ARABIC to "السيرة والتاريخ",
            AppLanguage.HINDI to "सीरत व इतिहास"
        ),
        "pod_topic_spiritual" to mapOf(
            AppLanguage.ENGLISH to "Spiritual Reminders",
            AppLanguage.URDU to "روحانی تذکیر",
            AppLanguage.ARABIC to "الرقائق والإيمانيات",
            AppLanguage.HINDI to "आध्यात्मिक अनुस्मारक"
        ),
        "pod_topic_family" to mapOf(
            AppLanguage.ENGLISH to "Family & Youth",
            AppLanguage.URDU to "خاندان و نوجوان",
            AppLanguage.ARABIC to "الأسرة والشباب",
            AppLanguage.HINDI to "परिवार व युवा"
        ),
        "pod_topic_qa" to mapOf(
            AppLanguage.ENGLISH to "Q&A & Knowledge",
            AppLanguage.URDU to "سوال و جواب و علم",
            AppLanguage.ARABIC to "الفتاوى والعلم",
            AppLanguage.HINDI to "प्रश्नोत्तर व ज्ञान"
        ),
        "pod_no_episodes_for_selection" to mapOf(
            AppLanguage.ENGLISH to "No verified episodes are currently available for this language and topic.",
            AppLanguage.URDU to "اس زبان اور موضوع کے لیے اس وقت کوئی مستند قسط دستیاب نہیں ہے۔",
            AppLanguage.ARABIC to "لا تتوفر حلقات موثوقة حالياً لهذه اللغة والموضوع.",
            AppLanguage.HINDI to "इस भाषा और विषय के लिए वर्तमान में कोई सत्यापित एपिसोड उपलब्ध नहीं है।"
        ),
        "pod_last_updated" to mapOf(
            AppLanguage.ENGLISH to "Last updated: %s",
            AppLanguage.URDU to "آخری تازہ کاری: %s",
            AppLanguage.ARABIC to "آخر تحديث: %s",
            AppLanguage.HINDI to "अंतिम अपडेट: %s"
        ),
        "pod_source_official" to mapOf(
            AppLanguage.ENGLISH to "Official Channel",
            AppLanguage.URDU to "آفیشل چینل",
            AppLanguage.ARABIC to "قناة رسمية",
            AppLanguage.HINDI to "आधिकारिक चैनल"
        ),
        "pod_source_hosted" to mapOf(
            AppLanguage.ENGLISH to "Hosted Interview",
            AppLanguage.URDU to "میزبان انٹرویو",
            AppLanguage.ARABIC to "لقاء مستضاف",
            AppLanguage.HINDI to "होस्टेड इंटरव्यू"
        ),
        "pod_source_unavailable" to mapOf(
            AppLanguage.ENGLISH to "Channel Unavailable",
            AppLanguage.URDU to "چینل دستیاب نہیں",
            AppLanguage.ARABIC to "القناة غير متوفرة",
            AppLanguage.HINDI to "चैनल उपलब्ध नहीं"
        ),
        "pod_type_episode" to mapOf(
            AppLanguage.ENGLISH to "Podcast Episode",
            AppLanguage.URDU to "پوڈکاسٹ قسط",
            AppLanguage.ARABIC to "حلقة بودكاست",
            AppLanguage.HINDI to "पॉडकास्ट एपिसोड"
        ),
        "pod_type_interview" to mapOf(
            AppLanguage.ENGLISH to "Full Interview",
            AppLanguage.URDU to "مکمل انٹرویو",
            AppLanguage.ARABIC to "مقابلة كاملة",
            AppLanguage.HINDI to "पूर्ण इंटरव्यू"
        ),
        "pod_type_discussion" to mapOf(
            AppLanguage.ENGLISH to "Long-Form Discussion",
            AppLanguage.URDU to "طویل علمی نشست",
            AppLanguage.ARABIC to "حوار مطوّل",
            AppLanguage.HINDI to "दीर्घ चर्चा"
        ),
        "pod_type_qa" to mapOf(
            AppLanguage.ENGLISH to "Islamic Q&A",
            AppLanguage.URDU to "سوال و جواب",
            AppLanguage.ARABIC to "سؤال وجواب",
            AppLanguage.HINDI to "इस्लामी प्रश्नोत्तर"
        ),
        "pod_type_educational" to mapOf(
            AppLanguage.ENGLISH to "Educational Discussion",
            AppLanguage.URDU to "تعلیمی گفتگو",
            AppLanguage.ARABIC to "درس علمي",
            AppLanguage.HINDI to "शैक्षिक चर्चा"
        ),
        "pod_type_lecture" to mapOf(
            AppLanguage.ENGLISH to "Full Lecture",
            AppLanguage.URDU to "مکمل لیکچر",
            AppLanguage.ARABIC to "محاضرة كاملة",
            AppLanguage.HINDI to "पूर्ण व्याख्यान"
        ),
        "pod_cat_all_eligible" to mapOf(
            AppLanguage.ENGLISH to "All Eligible Videos",
            AppLanguage.URDU to "تمام مستند ویڈیوز",
            AppLanguage.ARABIC to "جميع المقاطع المؤهلة",
            AppLanguage.HINDI to "सभी योग्य वीडियो"
        ),
        "pod_cat_complete_episodes" to mapOf(
            AppLanguage.ENGLISH to "Complete Podcast Episodes",
            AppLanguage.URDU to "مکمل پوڈکاسٹ اقساط",
            AppLanguage.ARABIC to "حلقات البودكاست الكاملة",
            AppLanguage.HINDI to "पूर्ण पॉडकास्ट एपिसोड"
        ),
        "pod_cat_full_interviews" to mapOf(
            AppLanguage.ENGLISH to "Full Interviews",
            AppLanguage.URDU to "مکمل انٹرویوز",
            AppLanguage.ARABIC to "المقابلات الكاملة",
            AppLanguage.HINDI to "पूर्ण इंटरव्यू"
        ),
        "pod_cat_long_discussions" to mapOf(
            AppLanguage.ENGLISH to "Long-Form Discussions",
            AppLanguage.URDU to "طویل علمی مباحث",
            AppLanguage.ARABIC to "الحوارات المطوّلة",
            AppLanguage.HINDI to "दीर्घ चर्चाएँ"
        ),
        "pod_cat_islamic_qa" to mapOf(
            AppLanguage.ENGLISH to "Islamic Q&A",
            AppLanguage.URDU to "اسلامی سوال و جواب",
            AppLanguage.ARABIC to "الفتاوى والأسئلة",
            AppLanguage.HINDI to "इस्लामी प्रश्नोत्तर"
        ),
        "pod_cat_history_seerah" to mapOf(
            AppLanguage.ENGLISH to "Islamic History & Seerah",
            AppLanguage.URDU to "اسلامی تاریخ و سیرت",
            AppLanguage.ARABIC to "التاريخ الإسلامي والسيرة",
            AppLanguage.HINDI to "इस्लामी इतिहास व सीरत"
        ),
        "pod_cat_islamic_lifestyle" to mapOf(
            AppLanguage.ENGLISH to "Islamic Lifestyle",
            AppLanguage.URDU to "اسلامی طرزِ زندگی",
            AppLanguage.ARABIC to "أسلوب الحياة الإسلامي",
            AppLanguage.HINDI to "इस्लामी जीवनशैली"
        ),
        "pod_cat_youth_family" to mapOf(
            AppLanguage.ENGLISH to "Youth & Family",
            AppLanguage.URDU to "نوجوان اور خاندان",
            AppLanguage.ARABIC to "الشباب والأسرة",
            AppLanguage.HINDI to "युवा व परिवार"
        ),
        "pod_cat_repentance_self" to mapOf(
            AppLanguage.ENGLISH to "Repentance & Self-Improvement",
            AppLanguage.URDU to "توبہ اور اصلاحِ نفس",
            AppLanguage.ARABIC to "التوبة وإصلاح النفس",
            AppLanguage.HINDI to "तौबा व आत्म-सुधार"
        ),
        "pod_cat_latest_long_form" to mapOf(
            AppLanguage.ENGLISH to "Latest Long-Form Videos",
            AppLanguage.URDU to "تازہ ترین طویل ویڈیوز",
            AppLanguage.ARABIC to "أحدث المقاطع الطويلة",
            AppLanguage.HINDI to "नवीनतम दीर्घ वीडियो"
        ),
        "pod_cat_most_viewed" to mapOf(
            AppLanguage.ENGLISH to "Most Viewed Long-Form",
            AppLanguage.URDU to "سب سے زیادہ دیکھی گئی ویڈیوز",
            AppLanguage.ARABIC to "الأكثر مشاهدة",
            AppLanguage.HINDI to "सर्वाधिक देखे गए वीडियो"
        ),
        "pod_scholars_section" to mapOf(
            AppLanguage.ENGLISH to "Verified Scholars (%d)",
            AppLanguage.URDU to "مستند علمائے کرام (%d)",
            AppLanguage.ARABIC to "العلماء الموثوقون (%d)",
            AppLanguage.HINDI to "सत्यापित विद्वान (%d)"
        ),
        "pod_channels_section" to mapOf(
            AppLanguage.ENGLISH to "Verified Islamic History Channel",
            AppLanguage.URDU to "مستند اسلامی تاریخ چینل",
            AppLanguage.ARABIC to "قناة التاريخ الإسلامي الموثوقة",
            AppLanguage.HINDI to "सत्यापित इस्लामी इतिहास चैनल"
        ),
        "pod_verified_count" to mapOf(
            AppLanguage.ENGLISH to "%d verified long-form videos",
            AppLanguage.URDU to "%d مستند طویل ویڈیوز",
            AppLanguage.ARABIC to "%d مقطع طويل موثّق",
            AppLanguage.HINDI to "%d सत्यापित दीर्घ वीडियो"
        ),
        "pod_shorts_filtered_note" to mapOf(
            AppLanguage.ENGLISH to "%d Shorts/clips automatically filtered out",
            AppLanguage.URDU to "%d شارٹس اور کلپس خودکار طور پر ہٹا دیے گئے",
            AppLanguage.ARABIC to "تم استبعاد %d مقطع قصير تلقائياً",
            AppLanguage.HINDI to "%d शॉर्ट्स/क्लिप स्वतः फ़िल्टर किए गए"
        ),
        "pod_load_more" to mapOf(
            AppLanguage.ENGLISH to "Load More Verified Videos",
            AppLanguage.URDU to "مزید مستند ویڈیوز لوڈ کریں",
            AppLanguage.ARABIC to "تحميل المزيد من المقاطع الموثّقة",
            AppLanguage.HINDI to "और सत्यापित वीडियो लोड करें"
        ),
        "pod_quota_banner" to mapOf(
            AppLanguage.ENGLISH to "YouTube API quota limit reached — showing verified cached videos.",
            AppLanguage.URDU to "یوٹیوب API کی حد مکمل ہو گئی — محفوظ مستند ویڈیوز دکھائی جا رہی ہیں۔",
            AppLanguage.ARABIC to "تم الوصول للحد اليومي لواجهة يوتيوب — يتم عرض المقاطع الموثّقة المحفوظة.",
            AppLanguage.HINDI to "यूट्यूब API सीमा पूरी हो गई — सहेजे गए सत्यापित वीडियो दिखाए जा रहे हैं।"
        ),
        "pod_auto_refresh_note" to mapOf(
            AppLanguage.ENGLISH to "Auto-refreshes every ~30 min when online (subject to Android battery scheduling). YouTube videos stream online only.",
            AppLanguage.URDU to "آن لائن ہونے پر ہر ~30 منٹ بعد خودکار تازہ کاری (اینڈرائیڈ بیٹری شیڈولنگ کے مطابق)۔ یوٹیوب ویڈیوز صرف آن لائن چلتی ہیں۔",
            AppLanguage.ARABIC to "تحديث تلقائي كل ~30 دقيقة عند الاتصال (وفق جدولة بطارية أندرويد). مقاطع يوتيوب تُبث عبر الإنترنت فقط.",
            AppLanguage.HINDI to "ऑनलाइन होने पर हर ~30 मिनट में स्वतः रिफ्रेश (एंड्रॉइड बैटरी शेड्यूलिंग के अधीन)। यूट्यूब वीडियो केवल ऑनलाइन चलते हैं।"
        ),
        "pod_back_to_scholars" to mapOf(
            AppLanguage.ENGLISH to "All Scholars",
            AppLanguage.URDU to "تمام علماء",
            AppLanguage.ARABIC to "جميع العلماء",
            AppLanguage.HINDI to "सभी विद्वान"
        ),
        "pod_unavailable_scholar_msg" to mapOf(
            AppLanguage.ENGLISH to "No verified official YouTube channel with eligible long-form videos is currently available for this scholar.",
            AppLanguage.URDU to "اس عالم کے لیے اس وقت کوئی مستند آفیشل یوٹیوب چینل یا اہل طویل ویڈیو دستیاب نہیں ہے۔",
            AppLanguage.ARABIC to "لا تتوفر حالياً قناة يوتيوب رسمية موثّقة بمقاطع طويلة مؤهلة لهذا الشيخ.",
            AppLanguage.HINDI to "इस विद्वान के लिए वर्तमान में कोई सत्यापित आधिकारिक यूट्यूब चैनल या योग्य दीर्घ वीडियो उपलब्ध नहीं है।"
        ),
        "pod_search_scholars_hint" to mapOf(
            AppLanguage.ENGLISH to "Search scholars or channels...",
            AppLanguage.URDU to "علماء یا چینلز تلاش کریں...",
            AppLanguage.ARABIC to "ابحث عن العلماء أو القنوات...",
            AppLanguage.HINDI to "विद्वान या चैनल खोजें..."
        ),
        "pod_search_videos_hint" to mapOf(
            AppLanguage.ENGLISH to "Search verified long-form videos...",
            AppLanguage.URDU to "مستند طویل ویڈیوز تلاش کریں...",
            AppLanguage.ARABIC to "ابحث في المقاطع الطويلة الموثّقة...",
            AppLanguage.HINDI to "सत्यापित दीर्घ वीडियो खोजें..."
        ),
        "pod_subtab_suggestions" to mapOf(
            AppLanguage.ENGLISH to "Suggested Videos",
            AppLanguage.URDU to "تجویز کردہ ویڈیوز",
            AppLanguage.ARABIC to "المقاطع المقترحة",
            AppLanguage.HINDI to "सुझाए गए वीडियो"
        ),
        "pod_subtab_scholars" to mapOf(
            AppLanguage.ENGLISH to "Scholars & Channels",
            AppLanguage.URDU to "علماء اور چینلز",
            AppLanguage.ARABIC to "العلماء والقنوات",
            AppLanguage.HINDI to "विद्वान और चैनल"
        ),
        "pod_catalog_channels_badge" to mapOf(
            AppLanguage.ENGLISH to "%d+ verified channels • Long-form only (No Shorts)",
            AppLanguage.URDU to "%d+ مستند چینلز • صرف طویل ویڈیوز (شارٹس کے بغیر)",
            AppLanguage.ARABIC to "%d+ قناة موثّقة • مقاطع طويلة فقط (بدون شورتس)",
            AppLanguage.HINDI to "%d+ सत्यापित चैनल • केवल दीर्घ वीडियो (कोई शॉर्ट्स नहीं)"
        ),
        "pod_more_verified_channels" to mapOf(
            AppLanguage.ENGLISH to "More Verified Channels (%d)",
            AppLanguage.URDU to "مزید مستند چینلز (%d)",
            AppLanguage.ARABIC to "المزيد من القنوات الموثّقة (%d)",
            AppLanguage.HINDI to "अधिक सत्यापित चैनल (%d)"
        ),
        "live_status_unconfirmed" to mapOf(
            AppLanguage.ENGLISH to "Live broadcast status cannot be confirmed",
            AppLanguage.URDU to "لائیو نشریات کی تصدیق ممکن نہیں",
            AppLanguage.ARABIC to "لا يمكن تأكيد حالة البث المباشر",
            AppLanguage.HINDI to "लाइव प्रसारण की स्थिति की पुष्टि नहीं हो सकी"
        ),
        "live_unconfirmed_desc" to mapOf(
            AppLanguage.ENGLISH to "Real-time live stream verification requires YouTube Data API credentials (this app uses zero-API-key official RSS feeds only). You can open the official channel on YouTube or watch recent recorded uploads below.",
            AppLanguage.URDU to "لائیو اسٹریمنگ کی حقیقی وقت میں تصدیق کے لیے یوٹیوب ڈیٹا API درکار ہوتی ہے (یہ ایپ بغیر API کلید کے صرف سرکاری RSS فیڈز استعمال کرتی ہے)۔ آپ سرکاری یوٹیوب چینل کھول سکتے ہیں یا نیچے حالیہ ریکارڈ شدہ ویڈیوز دیکھ سکتے ہیں۔",
            AppLanguage.ARABIC to "يتطلب التحقق الفوري من البث المباشر بيانات YouTube Data API (هذا التطبيق يستخدم موجزات RSS الرسمية فقط بدون مفاتيح API). يمكنك فتح القناة الرسمية على يوتيوب أو مشاهدة التسجيلات الحديثة أدناه.",
            AppLanguage.HINDI to "रीयल-टाइम लाइव स्ट्रीम सत्यापन के लिए YouTube Data API क्रेडेंशियल आवश्यक हैं (यह ऐप बिना API कुंजी के केवल आधिकारिक RSS फ़ीड उपयोग करता है)। आप यूट्यूब पर आधिकारिक चैनल खोल सकते हैं या नीचे हाल के रिकॉर्डेड वीडियो देख सकते हैं।"
        ),
        "live_recorded_uploads_title" to mapOf(
            AppLanguage.ENGLISH to "Recent Official Channel Uploads (Recorded)",
            AppLanguage.URDU to "سرکاری چینل کی حالیہ ریکارڈ شدہ ویڈیوز",
            AppLanguage.ARABIC to "أحدث التسجيلات من القناة الرسمية (مسجلة)",
            AppLanguage.HINDI to "आधिकारिक चैनल के हालिया रिकॉर्डेड वीडियो"
        ),
        "live_recorded_badge" to mapOf(
            AppLanguage.ENGLISH to "Recorded Upload • Not Live",
            AppLanguage.URDU to "ریکارڈ شدہ ویڈیو • لائیو نہیں",
            AppLanguage.ARABIC to "تسجيل مرفوع • ليس بثاً مباشراً",
            AppLanguage.HINDI to "रिकॉर्डेड अपलोड • लाइव नहीं"
        ),
        "live_watch_recorded" to mapOf(
            AppLanguage.ENGLISH to "Play Recorded Upload",
            AppLanguage.URDU to "ریکارڈ شدہ ویڈیو چلائیں",
            AppLanguage.ARABIC to "تشغيل التسجيل",
            AppLanguage.HINDI to "रिकॉर्डेड वीडियो चलाएं"
        ),
        "live_phase_connecting" to mapOf(
            AppLanguage.ENGLISH to "CONNECTING",
            AppLanguage.URDU to "کنیکٹ ہو رہا ہے",
            AppLanguage.ARABIC to "جارٍ الاتصال",
            AppLanguage.HINDI to "कनेक्ट हो रहा है"
        ),
        "live_phase_reconnecting" to mapOf(
            AppLanguage.ENGLISH to "RECONNECTING",
            AppLanguage.URDU to "دوبارہ کنیکٹ ہو رہا ہے",
            AppLanguage.ARABIC to "إعادة الاتصال",
            AppLanguage.HINDI to "पुनः कनेक्ट हो रहा है"
        ),
        "live_phase_source_unavailable" to mapOf(
            AppLanguage.ENGLISH to "SOURCE UNAVAILABLE",
            AppLanguage.URDU to "ذریعہ دستیاب نہیں",
            AppLanguage.ARABIC to "المصدر غير متاح",
            AppLanguage.HINDI to "स्रोत अनुपलब्ध"
        ),
        "live_source_unavailable_desc" to mapOf(
            AppLanguage.ENGLISH to "All supported live sources (Official YouTube, Quran Hayat, and Haramain Voice) failed to start playback. Tap Retry or open a source in your browser.",
            AppLanguage.URDU to "تمام دستیاب لائیو ذرائع (آفیشل یوٹیوب، قرآن حیات، اور حرمین وائس) فی الحال شروع نہیں ہو سکے۔ دوبارہ کوشش کریں یا براؤزر میں کھولیں۔",
            AppLanguage.ARABIC to "تعذر تشغيل جميع مصادر البث المباشر المدعومة (يوتيوب الرسمي، قرآن حياة، وصوت الحرمين). اضغط على إعادة المحاولة أو افتح مصدراً في المتصفح.",
            AppLanguage.HINDI to "सभी समर्थित लाइव स्रोत (आधिकारिक यूट्यूब, कुरान हयात और हरमैन वॉइस) अभी शुरू नहीं हो सके। पुनः प्रयास करें या ब्राउज़र में खोलें।"
        ),
        "live_fallback_relay_note" to mapOf(
            AppLanguage.ENGLISH to "Playing via automatic fallback relay of the official Saudi broadcaster signal.",
            AppLanguage.URDU to "خودکار بیک اپ کے ذریعے سرکاری سعودی نشریات کا ریلے چل رہا ہے۔",
            AppLanguage.ARABIC to "يتم التشغيل عبر مصدر بديل تلقائي ينقل إشارة البث السعودي الرسمي.",
            AppLanguage.HINDI to "आधिकारिक सऊदी प्रसारण सिग्नल के स्वचालित फ़ॉलबैक रिले के माध्यम से चल रहा है।"
        ),
        "live_sources_label" to mapOf(
            AppLanguage.ENGLISH to "Stream Sources",
            AppLanguage.URDU to "نشریاتی ذرائع",
            AppLanguage.ARABIC to "مصادر البث",
            AppLanguage.HINDI to "स्ट्रीम स्रोत"
        ),
        "live_open_quranhayat" to mapOf(
            AppLanguage.ENGLISH to "Quran Hayat",
            AppLanguage.URDU to "قرآن حیات",
            AppLanguage.ARABIC to "قرآن حياة",
            AppLanguage.HINDI to "कुरान हयात"
        ),
        "live_open_haramainvoice" to mapOf(
            AppLanguage.ENGLISH to "Haramain Voice",
            AppLanguage.URDU to "حرمین وائس",
            AppLanguage.ARABIC to "صوت الحرمين",
            AppLanguage.HINDI to "हरमैन वॉइस"
        ),
        "live_return_to_live" to mapOf(
            AppLanguage.ENGLISH to "Return to Live Stream",
            AppLanguage.URDU to "لائیو نشریات پر واپس جائیں",
            AppLanguage.ARABIC to "العودة إلى البث المباشر",
            AppLanguage.HINDI to "लाइव स्ट्रीम पर लौटें"
        ),
        // ==================== GLOBAL ZAKAT CALCULATOR ====================
        "zakat_global_subtitle" to mapOf(
            AppLanguage.ENGLISH to "International ISO 4217 Zakat Engine",
            AppLanguage.URDU to "عالمی زکوٰۃ حساب کا نظام (ISO 4217)",
            AppLanguage.ARABIC to "نظام حساب الزكاة الدولي (ISO 4217)",
            AppLanguage.HINDI to "अंतर्राष्ट्रीय ज़कात गणना प्रणाली (ISO 4217)"
        ),
        "zakat_country_label" to mapOf(
            AppLanguage.ENGLISH to "Country / Region",
            AppLanguage.URDU to "ملک / خطہ",
            AppLanguage.ARABIC to "الدولة / المنطقة",
            AppLanguage.HINDI to "देश / क्षेत्र"
        ),
        "zakat_currency_label" to mapOf(
            AppLanguage.ENGLISH to "Calculation Currency",
            AppLanguage.URDU to "حساب کی کرنسی",
            AppLanguage.ARABIC to "عملة الحساب",
            AppLanguage.HINDI to "गणना मुद्रा"
        ),
        "zakat_search_country" to mapOf(
            AppLanguage.ENGLISH to "Search country or ISO code...",
            AppLanguage.URDU to "ملک یا ISO کوڈ تلاش کریں...",
            AppLanguage.ARABIC to "ابحث عن الدولة أو الرمز...",
            AppLanguage.HINDI to "देश या ISO कोड खोजें..."
        ),
        "zakat_search_currency" to mapOf(
            AppLanguage.ENGLISH to "Search ISO 4217 currency or name...",
            AppLanguage.URDU to "کرنسی کا نام یا ISO کوڈ تلاش کریں...",
            AppLanguage.ARABIC to "ابحث عن العملة أو رمز ISO...",
            AppLanguage.HINDI to "मुद्रा नाम या ISO कोड खोजें..."
        ),
        "zakat_use_country_currency" to mapOf(
            AppLanguage.ENGLISH to "Use suggested currency",
            AppLanguage.URDU to "ملکی کرنسی منتخب کریں",
            AppLanguage.ARABIC to "استخدام عملة الدولة",
            AppLanguage.HINDI to "सुझाई गई मुद्रा चुनें"
        ),
        "zakat_market_prices_title" to mapOf(
            AppLanguage.ENGLISH to "Gold, Silver & Exchange Rate",
            AppLanguage.URDU to "سونا، چاندی اور شرحِ مبادلہ",
            AppLanguage.ARABIC to "أسعار الذهب والفضة وسعر الصرف",
            AppLanguage.HINDI to "सोना, चाँदी और विनिमय दर"
        ),
        "zakat_fetch_live_rates" to mapOf(
            AppLanguage.ENGLISH to "Fetch Live Spot & FX",
            AppLanguage.URDU to "تازہ ترین ریٹس حاصل کریں",
            AppLanguage.ARABIC to "جلب الأسعار الحية",
            AppLanguage.HINDI to "लाइव दरें प्राप्त करें"
        ),
        "zakat_manual_rate_label" to mapOf(
            AppLanguage.ENGLISH to "User-Provided Exchange Rate (1 USD → %s)",
            AppLanguage.URDU to "صارف کی درج کردہ شرحِ مبادلہ (1 USD ← %s)",
            AppLanguage.ARABIC to "سعر الصرف اليدوي (1 USD ← %s)",
            AppLanguage.HINDI to "उपयोगकर्ता विनिमय दर (1 USD → %s)"
        ),
        "zakat_manual_gold_label" to mapOf(
            AppLanguage.ENGLISH to "24K Gold Price / gram (%s)",
            AppLanguage.URDU to "24 قیراط سونے کی فی گرام قیمت (%s)",
            AppLanguage.ARABIC to "سعر جرام الذهب عيار 24 (%s)",
            AppLanguage.HINDI to "24K सोने का मूल्य / ग्राम (%s)"
        ),
        "zakat_manual_silver_label" to mapOf(
            AppLanguage.ENGLISH to "Pure Silver Price / gram (%s)",
            AppLanguage.URDU to "خالص چاندی کی فی گرام قیمت (%s)",
            AppLanguage.ARABIC to "سعر جرام الفضة النقية (%s)",
            AppLanguage.HINDI to "शुद्ध चाँदी का मूल्य / ग्राम (%s)"
        ),
        "zakat_manual_price_badge" to mapOf(
            AppLanguage.ENGLISH to "User-Provided Manual Price",
            AppLanguage.URDU to "صارف کی درج کردہ دستی قیمت",
            AppLanguage.ARABIC to "سعر يدوي مدخل من المستخدم",
            AppLanguage.HINDI to "उपयोगकर्ता द्वारा दर्ज मूल्य"
        ),
        "zakat_stale_warning" to mapOf(
            AppLanguage.ENGLISH to "Warning: Cached rate/price may be outdated. Verify before paying Zakat.",
            AppLanguage.URDU to "انتباہ: محفوظ شدہ ریٹ پرانا ہو سکتا ہے۔ زکوٰۃ ادائیگی سے قبل تصدیق کر لیں۔",
            AppLanguage.ARABIC to "تنبيه: قد تكون الأسعار المخزنة قديمة. يرجى التحقق قبل إخراج الزكاة.",
            AppLanguage.HINDI to "चेतावनी: सहेजी गई दर पुरानी हो सकती है। ज़कात अदा करने से पहले पुष्टि करें।"
        ),
        "zakat_nisab_section" to mapOf(
            AppLanguage.ENGLISH to "Nisab Standard & Methodology",
            AppLanguage.URDU to "معیارِ نصاب اور فقہی منہج",
            AppLanguage.ARABIC to "معيار النصاب والمنهج الفقهي",
            AppLanguage.HINDI to "निसाब मानक और फ़िक़्ही पद्धति"
        ),
        "zakat_silver_nisab" to mapOf(
            AppLanguage.ENGLISH to "Silver Nisab (612.36 g)",
            AppLanguage.URDU to "چاندی کا نصاب (612.36 گرام)",
            AppLanguage.ARABIC to "نصاب الفضة (612.36 جم)",
            AppLanguage.HINDI to "चाँदी निसाब (612.36 ग्राम)"
        ),
        "zakat_gold_nisab" to mapOf(
            AppLanguage.ENGLISH to "Gold Nisab (87.48 g)",
            AppLanguage.URDU to "سونے کا نصاب (87.48 گرام)",
            AppLanguage.ARABIC to "نصاب الذهب (87.48 جم)",
            AppLanguage.HINDI to "सोना निसाब (87.48 ग्राम)"
        ),
        "zakat_hawl_section" to mapOf(
            AppLanguage.ENGLISH to "Hawl (Lunar Year Status)",
            AppLanguage.URDU to "حول (ایک قمری سال کا گزرنا)",
            AppLanguage.ARABIC to "الحول (مرور عام هجري كامل)",
            AppLanguage.HINDI to "हौल (एक हिजरी चंद्र वर्ष)"
        ),
        "zakat_hawl_question" to mapOf(
            AppLanguage.ENGLISH to "Has your wealth remained at/above Nisab for one full Islamic lunar year?",
            AppLanguage.URDU to "کیا آپ کے مال پر نصاب کی حد تک پورا ایک قمری سال (حول) گزر چکا ہے؟",
            AppLanguage.ARABIC to "هل حال على مالك النصاب عام هجري قمري كامل؟",
            AppLanguage.HINDI to "क्या आपके माल पर निसाब की सीमा तक पूरा एक हिजरी चंद्र वर्ष बीत चुका है?"
        ),
        "zakat_hawl_yes" to mapOf(
            AppLanguage.ENGLISH to "Yes, Complete",
            AppLanguage.URDU to "جی ہاں، مکمل ہے",
            AppLanguage.ARABIC to "نعم، اكتمل الحول",
            AppLanguage.HINDI to "हाँ, पूरा हो गया"
        ),
        "zakat_hawl_no" to mapOf(
            AppLanguage.ENGLISH to "Not Yet",
            AppLanguage.URDU to "ابھی مکمل نہیں",
            AppLanguage.ARABIC to "لم يكتمل بعد",
            AppLanguage.HINDI to "अभी पूरा नहीं"
        ),
        "zakat_hawl_unknown" to mapOf(
            AppLanguage.ENGLISH to "Unverified",
            AppLanguage.URDU to "غیر مصدقہ",
            AppLanguage.ARABIC to "غير مؤكد",
            AppLanguage.HINDI to "असत्यापित"
        ),
        "zakat_hawl_date_label" to mapOf(
            AppLanguage.ENGLISH to "Date Nisab Reached (YYYY-MM-DD, optional)",
            AppLanguage.URDU to "نصاب مکمل ہونے کی تاریخ (YYYY-MM-DD، اختیاری)",
            AppLanguage.ARABIC to "تاريخ بلوغ النصاب (YYYY-MM-DD، اختياري)",
            AppLanguage.HINDI to "निसाब पूर्ण होने की तिथि (YYYY-MM-DD, वैकल्पिक)"
        ),
        "zakat_sec_cash" to mapOf(
            AppLanguage.ENGLISH to "1. Cash & Bank Assets",
            AppLanguage.URDU to "1. نقد رقم اور بینک اثاثے",
            AppLanguage.ARABIC to "1. النقد والأرصدة البنكية",
            AppLanguage.HINDI to "1. नकद और बैंक संपत्ति"
        ),
        "zakat_sec_gold" to mapOf(
            AppLanguage.ENGLISH to "2. Gold Holdings",
            AppLanguage.URDU to "2. سونے کے اثاثے",
            AppLanguage.ARABIC to "2. ممتلكات الذهب",
            AppLanguage.HINDI to "2. सोने की संपत्ति"
        ),
        "zakat_sec_silver" to mapOf(
            AppLanguage.ENGLISH to "3. Silver Holdings",
            AppLanguage.URDU to "3. چاندی کے اثاثے",
            AppLanguage.ARABIC to "3. ممتلكات الفضة",
            AppLanguage.HINDI to "3. चाँदी की संपत्ति"
        ),
        "zakat_sec_business" to mapOf(
            AppLanguage.ENGLISH to "4. Business Liquid & Trade Assets",
            AppLanguage.URDU to "4. تجارتی مال اور کاروباری نقدی",
            AppLanguage.ARABIC to "4. عروض التجارة والأصول السائلة",
            AppLanguage.HINDI to "4. व्यापारिक माल और नकद"
        ),
        "zakat_sec_investments" to mapOf(
            AppLanguage.ENGLISH to "5. Shares, Funds & Investments",
            AppLanguage.URDU to "5. حصص، میوچل فنڈز اور سرمایہ کاری",
            AppLanguage.ARABIC to "5. الأسهم والصناديق والاستثمارات",
            AppLanguage.HINDI to "5. शेयर, म्यूचुअल फंड और निवेश"
        ),
        "zakat_sec_receivables" to mapOf(
            AppLanguage.ENGLISH to "6. Money Owed to You (Receivables)",
            AppLanguage.URDU to "6. قرضِ حسنہ اور واجب الوصول رقوم",
            AppLanguage.ARABIC to "6. الديون التي لك على الغير",
            AppLanguage.HINDI to "6. दूसरों पर बकाया राशि (प्राप्य)"
        ),
        "zakat_sec_liabilities" to mapOf(
            AppLanguage.ENGLISH to "7. Deductible Current Liabilities",
            AppLanguage.URDU to "7. قابلِ کٹوتی واجب الادا قرضے",
            AppLanguage.ARABIC to "7. الديون والالتزامات الحالة",
            AppLanguage.HINDI to "7. कटौती योग्य वर्तमान देनदारियाँ"
        ),
        "zakat_purity_24k" to mapOf(
            AppLanguage.ENGLISH to "24K (100%)",
            AppLanguage.URDU to "24 قیراط (100%)",
            AppLanguage.ARABIC to "عيار 24 (100%)",
            AppLanguage.HINDI to "24K (100%)"
        ),
        "zakat_purity_22k" to mapOf(
            AppLanguage.ENGLISH to "22K (91.67%)",
            AppLanguage.URDU to "22 قیراط (91.67%)",
            AppLanguage.ARABIC to "عيار 22 (91.67%)",
            AppLanguage.HINDI to "22K (91.67%)"
        ),
        "zakat_purity_21k" to mapOf(
            AppLanguage.ENGLISH to "21K (87.5%)",
            AppLanguage.URDU to "21 قیراط (87.5%)",
            AppLanguage.ARABIC to "عيار 21 (87.5%)",
            AppLanguage.HINDI to "21K (87.5%)"
        ),
        "zakat_purity_18k" to mapOf(
            AppLanguage.ENGLISH to "18K (75%)",
            AppLanguage.URDU to "18 قیراط (75%)",
            AppLanguage.ARABIC to "عيار 18 (75%)",
            AppLanguage.HINDI to "18K (75%)"
        ),
        "zakat_purity_custom" to mapOf(
            AppLanguage.ENGLISH to "Custom %",
            AppLanguage.URDU to "مخصوص %",
            AppLanguage.ARABIC to "نسبة مخصصة %",
            AppLanguage.HINDI to "कस्टम %"
        ),
        "zakat_silver_999" to mapOf(
            AppLanguage.ENGLISH to "Pure (99.9%)",
            AppLanguage.URDU to "خالص (99.9%)",
            AppLanguage.ARABIC to "نقية (99.9%)",
            AppLanguage.HINDI to "शुद्ध (99.9%)"
        ),
        "zakat_silver_925" to mapOf(
            AppLanguage.ENGLISH to "Sterling (92.5%)",
            AppLanguage.URDU to "اسٹرلنگ (92.5%)",
            AppLanguage.ARABIC to "إسترلينية (92.5%)",
            AppLanguage.HINDI to "स्टर्लिंग (92.5%)"
        ),
        "zakat_reset" to mapOf(
            AppLanguage.ENGLISH to "Reset",
            AppLanguage.URDU to "ری سیٹ کریں",
            AppLanguage.ARABIC to "إعادة تعيين",
            AppLanguage.HINDI to "रीसेट करें"
        ),
        "zakat_recalculate" to mapOf(
            AppLanguage.ENGLISH to "Recalculate",
            AppLanguage.URDU to "دوبارہ حساب کریں",
            AppLanguage.ARABIC to "إعادة الحساب",
            AppLanguage.HINDI to "पुनः गणना करें"
        ),
        "zakat_edit_inputs" to mapOf(
            AppLanguage.ENGLISH to "Edit Inputs",
            AppLanguage.URDU to "اندراجات میں ترمیم کریں",
            AppLanguage.ARABIC to "تعديل المدخلات",
            AppLanguage.HINDI to "इनपुट संपादित करें"
        ),
        "zakat_status_due" to mapOf(
            AppLanguage.ENGLISH to "Zakat Due (2.5%)",
            AppLanguage.URDU to "زکوٰۃ واجب ہے (2.5%)",
            AppLanguage.ARABIC to "الزكاة واجبة (2.5%)",
            AppLanguage.HINDI to "ज़कात वाजिब है (2.5%)"
        ),
        "zakat_status_below_nisab" to mapOf(
            AppLanguage.ENGLISH to "Below Nisab — No Zakat Due",
            AppLanguage.URDU to "نصاب سے کم — زکوٰۃ واجب نہیں",
            AppLanguage.ARABIC to "أقل من النصاب — لا زكاة واجبة",
            AppLanguage.HINDI to "निसाब से कम — ज़कात वाजिब नहीं"
        ),
        "zakat_status_hawl_incomplete" to mapOf(
            AppLanguage.ENGLISH to "Hawl Incomplete — Zakat Not Yet Due",
            AppLanguage.URDU to "حول مکمل نہیں — ابھی زکوٰۃ واجب نہیں",
            AppLanguage.ARABIC to "لم يكتمل الحول — لا زكاة واجبة بعد",
            AppLanguage.HINDI to "हौल अधूरा — ज़कात अभी वाजिब नहीं"
        ),
        "zakat_status_hawl_unverified" to mapOf(
            AppLanguage.ENGLISH to "Hawl Unverified — Confirm Lunar Year Status",
            AppLanguage.URDU to "حول کی تصدیق باقی ہے — قمری سال کی حالت منتخب کریں",
            AppLanguage.ARABIC to "الحول غير مؤكد — يرجى تحديد حالة العام الهجري",
            AppLanguage.HINDI to "हौल असत्यापित — हिजरी वर्ष की स्थिति चुनें"
        ),
        "zakat_status_price_missing" to mapOf(
            AppLanguage.ENGLISH to "Metal Price Required to Verify Nisab",
            AppLanguage.URDU to "نصاب کی تصدیق کے لیے سونے/چاندی کی قیمت درکار ہے",
            AppLanguage.ARABIC to "مطلوب سعر الذهب أو الفضة لتحديد النصاب",
            AppLanguage.HINDI to "निसाब की जांच के लिए धातु का मूल्य आवश्यक है"
        ),
        "zakat_tawbah_note" to mapOf(
            AppLanguage.ENGLISH to "Quran 9:60 defines the 8 categories of Zakat recipients (Al-Fuqara, Al-Masakin, Amil, Mu'allafah, Riqab, Gharimin, Fi Sabilillah, Ibn al-Sabil). Consult a qualified Islamic scholar for complex business equities, agricultural produce, or livestock.",
            AppLanguage.URDU to "سورۃ التوبہ 9:60 میں زکوٰۃ کے آٹھ مصارف بیان کیے گئے ہیں۔ پیچیدہ کاروباری حصص، زرعی پیداوار یا مویشیوں کی زکوٰۃ کے لیے مستند عالمِ دین سے رجوع کریں۔",
            AppLanguage.ARABIC to "حددت سورة التوبة (9:60) مصارف الزكاة الثمانية. استشر عالماً موثوقاً في المسائل المركبة كالزروع والأنعام والاستثمارات المعقدة.",
            AppLanguage.HINDI to "क़ुरआन 9:60 में ज़कात के आठ हकदार बताए गए हैं। जटिल व्यापारिक हिस्सेदारी, कृषि उपज या मवेशियों के लिए योग्य आलिम से सलाह लें।"
        ),
        "cancel" to mapOf(
            AppLanguage.ENGLISH to "Cancel",
            AppLanguage.URDU to "منسوخ کریں",
            AppLanguage.ARABIC to "إلغاء",
            AppLanguage.HINDI to "रद्द करें"
        ),
        "zakat_device_suggested" to mapOf(
            AppLanguage.ENGLISH to "Device Region",
            AppLanguage.URDU to "ڈیوائس کا علاقہ",
            AppLanguage.ARABIC to "منطقة الجهاز",
            AppLanguage.HINDI to "डिवाइस क्षेत्र"
        ),
        "zakat_no_matches" to mapOf(
            AppLanguage.ENGLISH to "No matching items found",
            AppLanguage.URDU to "کوئی مماثل نتیجہ نہیں ملا",
            AppLanguage.ARABIC to "لم يتم العثور على نتائج مطابقة",
            AppLanguage.HINDI to "कोई मिलान नहीं मिला"
        ),
        "zakat_spot_original_usd" to mapOf(
            AppLanguage.ENGLISH to "Original Spot Price (USD/g)",
            AppLanguage.URDU to "اصل عالمی قیمت (USD/گرام)",
            AppLanguage.ARABIC to "السعر الفوري الأصلي (دولار/غرام)",
            AppLanguage.HINDI to "मूल स्पॉट मूल्य (USD/ग्राम)"
        ),
        "zakat_fx_rate_used" to mapOf(
            AppLanguage.ENGLISH to "Exchange Rate Used",
            AppLanguage.URDU to "استعمال شدہ شرحِ مبادلہ",
            AppLanguage.ARABIC to "سعر الصرف المستخدم",
            AppLanguage.HINDI to "उपयोग की गई विनिमय दर"
        ),
        "zakat_fx_unsupported_notice" to mapOf(
            AppLanguage.ENGLISH to "Automatic exchange rate is unavailable for this currency pair. Enter a verified local exchange rate or local retail gold/silver prices below.",
            AppLanguage.URDU to "اس کرنسی جوڑے کے لیے خودکار شرحِ مبادلہ دستیاب نہیں۔ نیچے مستند مقامی شرحِ مبادلہ یا سونے/چاندی کی مقامی قیمت درج کریں۔",
            AppLanguage.ARABIC to "سعر الصرف التلقائي غير متاح لزوج العملات هذا. أدخل سعر الصرف المحلي أو أسعار الذهب/الفضة المحلية أدناه.",
            AppLanguage.HINDI to "इस मुद्रा जोड़े के लिए स्वचालित विनिमय दर उपलब्ध नहीं है। नीचे सत्यापित स्थानीय विनिमय दर या स्थानीय सोने/चांदी का भाव दर्ज करें।"
        ),
        "zakat_metal_manual_toggle" to mapOf(
            AppLanguage.ENGLISH to "Use local retail prices (override live spot)",
            AppLanguage.URDU to "مقامی مارکیٹ کی قیمتیں خود درج کریں",
            AppLanguage.ARABIC to "إدخال أسعار السوق المحلي يدوياً",
            AppLanguage.HINDI to "स्थानीय खुदरा मूल्य मैन्युअल रूप से दर्ज करें"
        ),
        "zakat_metal_auto_badge" to mapOf(
            AppLanguage.ENGLISH to "Auto Spot Feed",
            AppLanguage.URDU to "خودکار عالمی فیڈ",
            AppLanguage.ARABIC to "سعر فوري تلقائي",
            AppLanguage.HINDI to "स्वचालित स्पॉट फ़ीड"
        ),
        "zakat_nisab_silver_note" to mapOf(
            AppLanguage.ENGLISH to "Silver Nisab (612.36g pure silver): Hanafi & welfare-focused scholarly view benefiting more recipients.",
            AppLanguage.URDU to "چاندی کا نصاب (612.36 گرام): فقہ حنفی اور فقراء کے فائدے کے لیے محتاط جمہور رائے۔",
            AppLanguage.ARABIC to "نصاب الفضة (612.36 غرام): الرأي الأحوط للفقراء وعليه المذهب الحنفي.",
            AppLanguage.HINDI to "चांदी का निसाब (612.36 ग्राम): हनफ़ी मत और गरीबों के हित में अनुशंसित।"
        ),
        "zakat_nisab_gold_note" to mapOf(
            AppLanguage.ENGLISH to "Gold Nisab (87.48g 24K pure gold): Shafi'i, Maliki & Hanbali or gold-standard comparison.",
            AppLanguage.URDU to "سونے کا نصاب (87.48 گرام خالص سونا): شافعی، مالکی و حنبلی یا سونے کا معیار۔",
            AppLanguage.ARABIC to "نصاب الذهب (87.48 غرام عيار 24): معيار الذهب المعتمد عند الجمهور عند قياس النقد بالذهب.",
            AppLanguage.HINDI to "सोने का निसाब (87.48 ग्राम शुद्ध सोना): शाफ़ई, मालिकी व हंबली या स्वर्ण मानक।"
        ),
        "zakat_hawl_anniversary_label" to mapOf(
            AppLanguage.ENGLISH to "Estimated 1-Lunar-Year (354 days) Hawl Anniversary",
            AppLanguage.URDU to "تخمینی یک قمری سال (354 دن) حول کی تکمیل",
            AppLanguage.ARABIC to "تاريخ إتمام الحول القمري (354 يوماً)",
            AppLanguage.HINDI to "अनुमानित 1 हिजरी वर्ष (354 दिन) हौल की तिथि"
        ),
        "zakat_cash_hand" to mapOf(
            AppLanguage.ENGLISH to "Cash at home / wallet",
            AppLanguage.URDU to "گھر یا جیب میں نقد رقم",
            AppLanguage.ARABIC to "النقد في المنزل أو المحفظة",
            AppLanguage.HINDI to "घर या पास रखी नकदी"
        ),
        "zakat_bank_current" to mapOf(
            AppLanguage.ENGLISH to "Current bank accounts",
            AppLanguage.URDU to "کرنٹ بینک اکاؤنٹس",
            AppLanguage.ARABIC to "الحسابات البنكية الجارية",
            AppLanguage.HINDI to "करंट बैंक खाते"
        ),
        "zakat_bank_savings" to mapOf(
            AppLanguage.ENGLISH to "Savings bank accounts",
            AppLanguage.URDU to "سیونگز بینک اکاؤنٹس",
            AppLanguage.ARABIC to "حسابات التوفير البنكية",
            AppLanguage.HINDI to "बचत बैंक खाते"
        ),
        "zakat_bank_deposits" to mapOf(
            AppLanguage.ENGLISH to "Accessible certificates & deposits",
            AppLanguage.URDU to "قابلِ دسترس ڈپازٹس",
            AppLanguage.ARABIC to "الودائع والشهادات القابلة للسحب",
            AppLanguage.HINDI to "जमा और सुलभ डिपॉज़िट"
        ),
        "zakat_cash_other" to mapOf(
            AppLanguage.ENGLISH to "E-wallets & other cash equivalents",
            AppLanguage.URDU to "ڈیجیٹل والٹ اور دیگر نقدی",
            AppLanguage.ARABIC to "المحافظ الرقمية وما يعادل النقد",
            AppLanguage.HINDI to "डिजिटल वॉलेट व अन्य नकद समतुल्य"
        ),
        "zakat_gold_weight_g" to mapOf(
            AppLanguage.ENGLISH to "Gold weight (grams)",
            AppLanguage.URDU to "سونے کا وزن (گرام)",
            AppLanguage.ARABIC to "وزن الذهب (بالغرام)",
            AppLanguage.HINDI to "सोने का वज़न (ग्राम)"
        ),
        "zakat_silver_weight_g" to mapOf(
            AppLanguage.ENGLISH to "Silver weight (grams)",
            AppLanguage.URDU to "چاندی کا وزن (گرام)",
            AppLanguage.ARABIC to "وزن الفضة (بالغرام)",
            AppLanguage.HINDI to "चांदी का वज़न (ग्राम)"
        ),
        "zakat_custom_purity_pct" to mapOf(
            AppLanguage.ENGLISH to "Custom purity (%)",
            AppLanguage.URDU to "مخصوص خالص پن (%)",
            AppLanguage.ARABIC to "نسبة النقاء المخصصة (%)",
            AppLanguage.HINDI to "कस्टम शुद्धता (%)"
        ),
        "zakat_personal_jewellery_switch" to mapOf(
            AppLanguage.ENGLISH to "Held solely as personal worn jewellery",
            AppLanguage.URDU to "صرف ذاتی استعمال کے زیورات",
            AppLanguage.ARABIC to "حلي للزينة والاستعمال الشخصي فقط",
            AppLanguage.HINDI to "केवल व्यक्तिगत उपयोग के आभूषण"
        ),
        "zakat_jewellery_method_all" to mapOf(
            AppLanguage.ENGLISH to "Include all gold/silver jewellery (Hanafi)",
            AppLanguage.URDU to "تمام زیورات شامل کریں (حنفی)",
            AppLanguage.ARABIC to "تجب الزكاة في جميع الحلي (الحنفية)",
            AppLanguage.HINDI to "सभी आभूषण शामिल करें (हनफ़ी)"
        ),
        "zakat_jewellery_method_exempt" to mapOf(
            AppLanguage.ENGLISH to "Exempt personal-use worn jewellery (Majority)",
            AppLanguage.URDU to "ذاتی استعمال کے زیورات مستثنیٰ (جمہور)",
            AppLanguage.ARABIC to "إعفاء الحلي الملبوس الشخصي (الجمهور)",
            AppLanguage.HINDI to "व्यक्तिगत आभूषण मुक्त रखें (बहुमत)"
        ),
        "zakat_biz_cash" to mapOf(
            AppLanguage.ENGLISH to "Business cash & bank balances",
            AppLanguage.URDU to "کاروباری نقدی و بینک بیلنس",
            AppLanguage.ARABIC to "النقد والأرصدة البنكية للنشاط التجاري",
            AppLanguage.HINDI to "व्यापारिक नकदी व बैंक शेष"
        ),
        "zakat_biz_inventory" to mapOf(
            AppLanguage.ENGLISH to "Salable inventory & stock at market value",
            AppLanguage.URDU to "فروخت کے لیے موجود مالِ تجارت",
            AppLanguage.ARABIC to "عروض التجارة والمخزون بسعر البيع",
            AppLanguage.HINDI to "बिक्री योग्य व्यापारिक माल (बाज़ार मूल्य)"
        ),
        "zakat_biz_merchandise" to mapOf(
            AppLanguage.ENGLISH to "Goods in transit / finished merchandise",
            AppLanguage.URDU to "تيار مال اور تجارتی سامان",
            AppLanguage.ARABIC to "البضائع الجاهزة أو قيد الشحن",
            AppLanguage.HINDI to "तैयार माल व पारगमन वस्तुएँ"
        ),
        "zakat_biz_receivables" to mapOf(
            AppLanguage.ENGLISH to "Collectible trade receivables",
            AppLanguage.URDU to "وصول ہونے والے کاروباری واجبات",
            AppLanguage.ARABIC to "الذمم التجارية المرجو تحصيلها",
            AppLanguage.HINDI to "प्राप्य व्यापारिक बकाया"
        ),
        "zakat_biz_other" to mapOf(
            AppLanguage.ENGLISH to "Other liquid business assets",
            AppLanguage.URDU to "دیگر کاروباری سیال اثاثے",
            AppLanguage.ARABIC to "أصول تجارية سائلة أخرى",
            AppLanguage.HINDI to "अन्य तरल व्यावसायिक संपत्तियां"
        ),
        "zakat_biz_exclusion_note" to mapOf(
            AppLanguage.ENGLISH to "Fixed operational assets (buildings, land in use, vehicles, tools, machinery not for resale) are excluded from Zakat.",
            AppLanguage.URDU to "کاروبار کی عمارات، مشینری، گاڑیاں اور ذاتی استعمال کے آلات پر زکوٰۃ نہیں۔",
            AppLanguage.ARABIC to "الأصول الثابتة للتشغيل (المباني، الآلات، المركبات، المعدات) لا زكاة فيها.",
            AppLanguage.HINDI to "स्थायी परिचालन संपत्तियाँ (भवन, मशीनरी, वाहन, उपकरण) ज़कात से मुक्त हैं।"
        ),
        "zakat_inv_shares" to mapOf(
            AppLanguage.ENGLISH to "Shares & equities market value",
            AppLanguage.URDU to "شیئرز کی مارکیٹ ویلیو",
            AppLanguage.ARABIC to "القيمة السوقية للأسهم",
            AppLanguage.HINDI to "शेयर व इक्विटी का बाज़ार मूल्य"
        ),
        "zakat_inv_funds" to mapOf(
            AppLanguage.ENGLISH to "Mutual funds / ETFs / Sukuk",
            AppLanguage.URDU to "میوچل فنڈز / صکوک",
            AppLanguage.ARABIC to "الصناديق الاستثمارية والصكوك",
            AppLanguage.HINDI to "म्यूचुअल फंड / ईटीएफ / सुकूक"
        ),
        "zakat_inv_other" to mapOf(
            AppLanguage.ENGLISH to "Other qualifying zakatable investments",
            AppLanguage.URDU to "دیگر زکوٰۃ کے قابل سرمایہ کاری",
            AppLanguage.ARABIC to "استثمارات أخرى خاضعة للزكاة",
            AppLanguage.HINDI to "अन्य ज़कात योग्य निवेश"
        ),
        "zakat_inv_method_full" to mapOf(
            AppLanguage.ENGLISH to "Full market value (trading shares)",
            AppLanguage.URDU to "مکمل مارکیٹ ویلیو (تجارت کے لیے)",
            AppLanguage.ARABIC to "كامل القيمة السوقية (أسهم المضاربة)",
            AppLanguage.HINDI to "पूर्ण बाज़ार मूल्य (ट्रेडिंग शेयर)"
        ),
        "zakat_inv_method_portion" to mapOf(
            AppLanguage.ENGLISH to "Custom zakatable asset % (long-term)",
            AppLanguage.URDU to "مخصوص زکوٰۃ فیصد (طویل مدتی)",
            AppLanguage.ARABIC to "نسبة الأصول الزكوية المحددة (طويلة الأجل)",
            AppLanguage.HINDI to "कस्टम ज़कात योग्य हिस्सा % (दीर्घकालिक)"
        ),
        "zakat_inv_portion_pct" to mapOf(
            AppLanguage.ENGLISH to "Verified zakatable portion % (user entered)",
            AppLanguage.URDU to "صارف کا درج کردہ زکوٰۃ فیصد (%)",
            AppLanguage.ARABIC to "النسبة الزكوية المعتمدة التي يدخلها المستخدم (%)",
            AppLanguage.HINDI to "उपयोगकर्ता द्वारा दर्ज ज़कात योग्य प्रतिशत (%)"
        ),
        "zakat_rec_strong" to mapOf(
            AppLanguage.ENGLISH to "Strong / recoverable money owed to you",
            AppLanguage.URDU to "یقینی طور پر وصول ہونے والا قرض",
            AppLanguage.ARABIC to "ديون مرجوة الأداء (قوية)",
            AppLanguage.HINDI to "सुनिश्चित रूप से वसूल होने वाला बकाया"
        ),
        "zakat_rec_doubtful" to mapOf(
            AppLanguage.ENGLISH to "Doubtful / delayed receivables",
            AppLanguage.URDU to "مشکوک یا مؤخر وصولی",
            AppLanguage.ARABIC to "ديون متعثرة أو مشكوك في تحصيلها",
            AppLanguage.HINDI to "संदिग्ध या विलंबित प्राप्य"
        ),
        "zakat_rec_unrecoverable" to mapOf(
            AppLanguage.ENGLISH to "Unrecoverable / hopeless receivables (excluded)",
            AppLanguage.URDU to "ناقابلِ وصول قرض (مستثنیٰ)",
            AppLanguage.ARABIC to "ديون معدومة ميؤوس منها (مستبعدة)",
            AppLanguage.HINDI to "अवसूली योग्य / डूबा हुआ ऋण (छूट)"
        ),
        "zakat_rec_method_strong_only" to mapOf(
            AppLanguage.ENGLISH to "Include strong receivables only",
            AppLanguage.URDU to "صرف یقینی وصولیاں شامل کریں",
            AppLanguage.ARABIC to "احتساب الديون المرجوة فقط",
            AppLanguage.HINDI to "केवल सुनिश्चित प्राप्य शामिल करें"
        ),
        "zakat_rec_method_include_doubtful" to mapOf(
            AppLanguage.ENGLISH to "Include strong + doubtful receivables",
            AppLanguage.URDU to "یقینی اور مشکوک دونوں شامل کریں",
            AppLanguage.ARABIC to "احتساب القوية والمشكوك فيها معاً",
            AppLanguage.HINDI to "सुनिश्चित व संदिग्ध दोनों शामिल करें"
        ),
        "zakat_debt_overdue" to mapOf(
            AppLanguage.ENGLISH to "Overdue personal or business debts",
            AppLanguage.URDU to "واجب الادا قرضے",
            AppLanguage.ARABIC to "ديون حالة واجبة السداد فوراً",
            AppLanguage.HINDI to "तत्काल देय व्यक्तिगत या व्यापारिक ऋण"
        ),
        "zakat_debt_bills" to mapOf(
            AppLanguage.ENGLISH to "Currently due utility / household / rent bills",
            AppLanguage.URDU to "فوری واجب الادا بلز اور کرایہ",
            AppLanguage.ARABIC to "فواتير ومصروفات مستحقة الآن",
            AppLanguage.HINDI to "वर्तमान में देय बिल और किराया"
        ),
        "zakat_debt_short_term" to mapOf(
            AppLanguage.ENGLISH to "Short-term urgent obligations",
            AppLanguage.URDU to "قلیل مدتی فوری ذمہ داریاں",
            AppLanguage.ARABIC to "التزامات قصيرة الأجل مستحقة",
            AppLanguage.HINDI to "अल्पकालिक देय दायित्व"
        ),
        "zakat_debt_installment" to mapOf(
            AppLanguage.ENGLISH to "Current due loan repayment installment",
            AppLanguage.URDU to "قرض کی موجودہ واجب الادا قسط",
            AppLanguage.ARABIC to "القسط الحالي المستحق من القرض",
            AppLanguage.HINDI to "ऋण की वर्तमान देय किस्त"
        ),
        "zakat_debt_other" to mapOf(
            AppLanguage.ENGLISH to "Other immediately due qualifying liabilities",
            AppLanguage.URDU to "دیگر فوری واجب الادا ذمہ داریاں",
            AppLanguage.ARABIC to "التزامات مالية حالة أخرى",
            AppLanguage.HINDI to "अन्य तत्काल देय दायित्व"
        ),
        "zakat_debt_method_current" to mapOf(
            AppLanguage.ENGLISH to "Deduct current due obligations only",
            AppLanguage.URDU to "صرف فوری واجب الادا قرض منہا کریں",
            AppLanguage.ARABIC to "خصم الديون الحالة والمستحقة فقط",
            AppLanguage.HINDI to "केवल वर्तमान देय ऋण घटाएँ"
        ),
        "zakat_debt_method_installment" to mapOf(
            AppLanguage.ENGLISH to "Include current due loan installment",
            AppLanguage.URDU to "موجودہ واجب الادا قسط بھی شامل کریں",
            AppLanguage.ARABIC to "شمل القسط الحالي المستحق من التمويل",
            AppLanguage.HINDI to "वर्तमान देय ऋण किस्त शामिल करें"
        ),
        "zakat_debt_exclusion_note" to mapOf(
            AppLanguage.ENGLISH to "Future speculative expenses and non-qualifying interest/riba penalties are not deducted.",
            AppLanguage.URDU to "مستقبل کے غیر متوقع اخراجات اور سودی جرمانے منہا نہیں کیے جاتے۔",
            AppLanguage.ARABIC to "لا تُخصم النفقات المستقبلية غير المستحقة ولا الفوائد الربوية.",
            AppLanguage.HINDI to "भविष्य के अनुमानित खर्च और ब्याज दंड इसमें नहीं घटाए जाते।"
        ),
        "zakat_res_title" to mapOf(
            AppLanguage.ENGLISH to "Zakat Calculation Breakdown",
            AppLanguage.URDU to "زکوٰۃ کے حساب کی مکمل تفصیل",
            AppLanguage.ARABIC to "تفاصيل حساب الزكاة",
            AppLanguage.HINDI to "ज़कात गणना का पूरा विवरण"
        ),
        "zakat_res_gross_assets" to mapOf(
            AppLanguage.ENGLISH to "Total Qualifying Assets",
            AppLanguage.URDU to "کل قابلِ زکوٰۃ اثاثے",
            AppLanguage.ARABIC to "إجمالي الأصول الخاضعة للزكاة",
            AppLanguage.HINDI to "कुल ज़कात योग्य संपत्ति"
        ),
        "zakat_res_liabilities" to mapOf(
            AppLanguage.ENGLISH to "Deductible Liabilities",
            AppLanguage.URDU to "واجب المنہا ذمہ داریاں",
            AppLanguage.ARABIC to "الديون والالتزامات المخصومة",
            AppLanguage.HINDI to "कटौती योग्य देनदारियां"
        ),
        "zakat_res_net_wealth" to mapOf(
            AppLanguage.ENGLISH to "Net Zakatable Wealth",
            AppLanguage.URDU to "خالص قابلِ زکوٰۃ مالیت",
            AppLanguage.ARABIC to "صافي الوعاء الزكوي",
            AppLanguage.HINDI to "शुद्ध ज़कात योग्य संपत्ति"
        ),
        "zakat_res_nisab_threshold" to mapOf(
            AppLanguage.ENGLISH to "Selected Nisab Threshold",
            AppLanguage.URDU to "منتخب نصاب کی حد",
            AppLanguage.ARABIC to "حد النصاب المختار",
            AppLanguage.HINDI to "चयनित निसाब सीमा"
        ),
        "zakat_res_pure_gold_g" to mapOf(
            AppLanguage.ENGLISH to "24K Pure Gold Equivalent",
            AppLanguage.URDU to "خالص 24 قیراط سونا",
            AppLanguage.ARABIC to "مكافئ الذهب الخالص عيار 24",
            AppLanguage.HINDI to "24K शुद्ध सोना समतुल्य"
        ),
        "zakat_res_pure_silver_g" to mapOf(
            AppLanguage.ENGLISH to "Pure Silver Equivalent",
            AppLanguage.URDU to "خالص چاندی",
            AppLanguage.ARABIC to "مكافئ الفضة الخالصة",
            AppLanguage.HINDI to "शुद्ध चांदी समतुल्य"
        ),
        "zakat_res_excluded_rec" to mapOf(
            AppLanguage.ENGLISH to "Excluded Receivables (Unrecoverable / Unselected)",
            AppLanguage.URDU to "مستثنیٰ قرضے",
            AppLanguage.ARABIC to "الديون المستبعدة من الوعاء",
            AppLanguage.HINDI to "छूट प्राप्त बकाया"
        ),
        "zakat_res_payable" to mapOf(
            AppLanguage.ENGLISH to "Final Zakat Payable (2.5%)",
            AppLanguage.URDU to "واجب الادا زکوٰۃ (2.5%)",
            AppLanguage.ARABIC to "مقدار الزكاة المستحقة (2.5%)",
            AppLanguage.HINDI to "अंतिम देय ज़कात (2.5%)"
        ),
        "zakat_res_formula" to mapOf(
            AppLanguage.ENGLISH to "Formula: Net Wealth = Assets − Deductible Liabilities. If Net Wealth ≥ Nisab AND Hawl (1 lunar year) is complete, Zakat = Net Wealth × 2.5%.",
            AppLanguage.URDU to "فارمولا: خالص مالیت = اثاثے − واجب الادا قرضے۔ اگر نصاب اور ایک قمری سال (حول) مکمل ہو تو زکوٰۃ = خالص مالیت × 2.5%۔",
            AppLanguage.ARABIC to "المعادلة: صافي المال = الأصول − الديون المستحقة. إذا بلغ الصافي النصاب وحال عليه الحول، فالزكاة = صافي المال × 2.5%.",
            AppLanguage.HINDI to "सूत्र: शुद्ध संपत्ति = संपत्ति − देनदारियां। यदि शुद्ध संपत्ति ≥ निसाब और हौल (1 हिजरी वर्ष) पूरा है, तो ज़कात = शुद्ध संपत्ति × 2.5%।"
        ),
        "zakat_scholarly_diffs_title" to mapOf(
            AppLanguage.ENGLISH to "Key Scholarly Methodology Differences",
            AppLanguage.URDU to "اہم فقہی اختلافات و وضاحت",
            AppLanguage.ARABIC to "أبرز الفروق الفقهية المعتبرة",
            AppLanguage.HINDI to "प्रमुख विद्वत (फ़िक़ही) मतभेद"
        ),
        "zakat_scholarly_diffs_body" to mapOf(
            AppLanguage.ENGLISH to "• Silver vs Gold Nisab: Hanafi school prioritizes Silver Nisab (612.36g) for cash/wealth as more beneficial to the poor; other scholars allow Gold Nisab (87.48g).\n• Personal Jewellery: Hanafi position includes worn gold/silver jewellery; Shafi'i, Maliki & Hanbali majority exempt regular personal-use jewellery.\n• Long-Term Equities: Trading shares pay 2.5% of full market value; long-term investors may verify the company's zakatable liquid-asset proportion with a scholar.",
            AppLanguage.URDU to "• چاندی بمقابلہ سونا: فقہ حنفی میں نقدی اور مالِ تجارت کے لیے چاندی کا نصاب (612.36 گرام) راجح ہے جبکہ دیگر علماء سونے کے معیار (87.48 گرام) کی اجازت دیتے ہیں۔\n• ذاتی زیورات: احناف کے نزدیک استعمال کے سونے چاندی پر زکوٰۃ ہے، جبکہ جمہور شافعی، مالکی و حنبلی ذاتی استعمال کے زیورات کو مستثنیٰ قرار دیتے ہیں۔\n• طویل مدتی حصص: تجارتی شیئرز کی پوری مالیت پر 2.5% زکوٰۃ ہے؛ طویل مدتی حصص کے قابلِ زکوٰۃ تناسب کے لیے عالمِ دین سے رہنمائی لیں۔",
            AppLanguage.ARABIC to "• نصاب الفضة مقابل الذهب: يأخذ الحنفية بنصاب الفضة (612.36 جم) للأحوط للفقراء، بينما يعتمد آخرون نصاب الذهب (87.48 جم).\n• الحلي الشخصي: تجب فيه الزكاة عند الحنفية، ويعفى الحلي المعتاد الملبوس عند الجمهور.\n• الأسهم: أسهم المضاربة تزكى بنسبة 2.5% من القيمة السوقية؛ أما الأسهم طويلة الأجل فبحسب نسبة الأصول الزكوية الموثقة.",
            AppLanguage.HINDI to "• चांदी बनाम सोना निसाब: हनफ़ी मत गरीबों के हित में चांदी (612.36g) को प्राथमिकता देता है, जबकि अन्य विद्वान सोने (87.48g) के मानक की अनुमति देते हैं।\n• व्यक्तिगत आभूषण: हनफ़ी मत में पहने गए आभूषणों पर भी ज़कात है, जबकि अधिकांश अन्य मतों में नियमित व्यक्तिगत आभूषण मुक्त हैं।\n• शेयर निवेश: ट्रेडिंग शेयरों पर पूरे बाज़ार मूल्य का 2.5%; दीर्घकालिक निवेश में प्रमाणित ज़कात योग्य हिस्से का उपयोग करें।"
        ),
        // Home Screen Subtitles & Labels
        "home_sub_quran" to mapOf(
            AppLanguage.ENGLISH to "114 Surahs • 30 Paras • Ayah-wise Translations & Tafseer",
            AppLanguage.URDU to "114 سورتیں • 30 پارے • آیت بہ آیت ترجمہ و تفسیر",
            AppLanguage.ARABIC to "١١٤ سورة • ٣٠ جزءاً • ترجمة وتفسير كل آية",
            AppLanguage.HINDI to "114 सूरह • 30 पारे • आयत-वार अनुवाद और तफ़सीर"
        ),
        "home_sub_live" to mapOf(
            AppLanguage.ENGLISH to "Masjid al-Haram & Masjid an-Nabawi Official Broadcasts",
            AppLanguage.URDU to "مسجد الحرام اور مسجد نبوی ﷺ کی سرکاری براہِ راست نشریات",
            AppLanguage.ARABIC to "البث الرسمي المباشر من المسجد الحرام والمسجد النبوي",
            AppLanguage.HINDI to "मस्जिद अल-हरम और मस्जिद अन-नबवी का आधिकारिक प्रसारण"
        ),
        "home_sub_lectures" to mapOf(
            AppLanguage.ENGLISH to "Verified Scholars • Quran, Hadith, Seerah & Life Guidance",
            AppLanguage.URDU to "مستند علمائے کرام • قرآن، حدیث، سیرت اور اسلامی رہنمائی",
            AppLanguage.ARABIC to "علماء موثوقون • القرآن والحديث والسيرة والإرشاد",
            AppLanguage.HINDI to "प्रमाणिक विद्वान • क़ुरआन, हदीस, सीरह और मार्गदर्शन"
        ),
        "home_sub_learning" to mapOf(
            AppLanguage.ENGLISH to "11-Stage Islamic Academy • Qaida, Tajweed, Salah & Hifz",
            AppLanguage.URDU to "11 مراحل پر مشتمل اکیڈمی • قاعدہ، تجوید، نماز اور حفظ",
            AppLanguage.ARABIC to "أكاديمية من ١١ مرحلة • القاعدة والتجويد والصلاة والحفظ",
            AppLanguage.HINDI to "11-चरणीय इस्लामी अकादमी • क़ायदा, तजवीद, नमाज़ और हिफ़्ज़"
        ),
        "home_sub_more" to mapOf(
            AppLanguage.ENGLISH to "Hadith, Duas, Tasbeeh, Qibla, Zakat, Calendar & Podcasts",
            AppLanguage.URDU to "احادیث، دعائیں، تسبیح، قبلہ، زکوٰۃ، تقویم اور پوڈکاسٹس",
            AppLanguage.ARABIC to "الحديث، الأدعية، التسبيح، القبلة، الزكاة، التقويم والبودكاست",
            AppLanguage.HINDI to "हदीस, दुआएं, तस्बीह, क़िबला, ज़कात, कैलेंडर और पॉडकास्ट"
        ),
        "bookmarks_empty" to mapOf(
            AppLanguage.ENGLISH to "No saved bookmarks yet.\nTap the bookmark icon on any Ayah, Hadith, or Dua to save it here.",
            AppLanguage.URDU to "ابھی کوئی بک مارک محفوظ نہیں ہے۔\nکسی بھی آیت، حدیث یا دعا کو محفوظ کرنے کے لیے بک مارک آئیکن دبائیں۔",
            AppLanguage.ARABIC to "لا توجد إشارات مرجعية محفوظة بعد.\nاضغط على رمز الحفظ في أي آية أو حديث أو دعاء.",
            AppLanguage.HINDI to "अभी कोई बुकमार्क सहेजा नहीं गया है।\nकिसी भी आयत, हदीस या दुआ पर बुकमार्क आइकन दबाएं।"
        ),
        "bookmarks_all" to mapOf(
            AppLanguage.ENGLISH to "All",
            AppLanguage.URDU to "تمام",
            AppLanguage.ARABIC to "الكل",
            AppLanguage.HINDI to "सभी"
        ),
        // Centralized Settings Categories A-L
        "settings_search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search settings (language, theme, font, Azan, privacy...)",
            AppLanguage.URDU to "ترتیبات تلاش کریں (زبان، تھیم، فونٹ، اذان، رازداری...)",
            AppLanguage.ARABIC to "ابحث في الإعدادات (اللغة، المظهر، الخط، الأذان، الخصوصية...)",
            AppLanguage.HINDI to "सेटिंग्स खोजें (भाषा, थीम, फ़ॉन्ट, अज़ान, गोपनीयता...)"
        ),
        "settings_sec_language" to mapOf(
            AppLanguage.ENGLISH to "A. Language & Region",
            AppLanguage.URDU to "1. زبان اور علاقائی ترتیبات",
            AppLanguage.ARABIC to "١. اللغة والمنطقة",
            AppLanguage.HINDI to "1. भाषा और क्षेत्र"
        ),
        "settings_sec_appearance" to mapOf(
            AppLanguage.ENGLISH to "B. Appearance & Personalization",
            AppLanguage.URDU to "2. ظاہری شکل و تھیم",
            AppLanguage.ARABIC to "٢. المظهر والتخصيص",
            AppLanguage.HINDI to "2. रूप और थीम"
        ),
        "settings_sec_quran" to mapOf(
            AppLanguage.ENGLISH to "C. Quran & Reading Preferences",
            AppLanguage.URDU to "3. قرآن و تلاوت کی ترتیبات",
            AppLanguage.ARABIC to "٣. تفضيلات القرآن والقراءة",
            AppLanguage.HINDI to "3. क़ुरआन और पठन प्राथमिकताएं"
        ),
        "settings_sec_tafseer" to mapOf(
            AppLanguage.ENGLISH to "D. Tafseer & Translation Preferences",
            AppLanguage.URDU to "4. تفسیر و ترجمہ کی ترتیبات",
            AppLanguage.ARABIC to "٤. التفسير والترجمة",
            AppLanguage.HINDI to "4. तफ़सीर और अनुवाद"
        ),
        "settings_sec_audio" to mapOf(
            AppLanguage.ENGLISH to "E. Audio & Playback",
            AppLanguage.URDU to "5. آڈیو اور تلاوت پلے بیک",
            AppLanguage.ARABIC to "٥. الصوت والتشغيل",
            AppLanguage.HINDI to "5. ऑडियो और प्लेबैक"
        ),
        "settings_sec_prayer" to mapOf(
            AppLanguage.ENGLISH to "F. Prayer & Location",
            AppLanguage.URDU to "6. اوقاتِ نماز، قبلہ اور مقام",
            AppLanguage.ARABIC to "٦. الصلاة والقبلة والموقع",
            AppLanguage.HINDI to "6. नमाज़, क़िबला और स्थान"
        ),
        "settings_sec_notifications" to mapOf(
            AppLanguage.ENGLISH to "G. Notifications & Daily Reminders",
            AppLanguage.URDU to "7. اطلاعات اور روزانہ یاد دہانیاں",
            AppLanguage.ARABIC to "٧. التنبيهات والتذكيرات اليومية",
            AppLanguage.HINDI to "7. सूचनाएं और दैनिक रिमाइंडर"
        ),
        "settings_sec_learning" to mapOf(
            AppLanguage.ENGLISH to "H. Learning & Daily Routine",
            AppLanguage.URDU to "8. تعلیم القرآن اور روزانہ ہدف",
            AppLanguage.ARABIC to "٨. التعلم والروتين اليومي",
            AppLanguage.HINDI to "8. लर्निंग और दैनिक लक्ष्य"
        ),
        "settings_sec_storage" to mapOf(
            AppLanguage.ENGLISH to "I. Internet & Storage",
            AppLanguage.URDU to "9. انٹرنیٹ اور اسٹوریج",
            AppLanguage.ARABIC to "٩. الإنترنت والتخزين",
            AppLanguage.HINDI to "9. इंटरनेट और स्टोरेज"
        ),
        "settings_sec_privacy" to mapOf(
            AppLanguage.ENGLISH to "J. Privacy & Security",
            AppLanguage.URDU to "10. رازداری اور سیکیورٹی",
            AppLanguage.ARABIC to "١٠. الخصوصية والأمان",
            AppLanguage.HINDI to "10. गोपनीयता और सुरक्षा"
        ),
        "settings_sec_accessibility" to mapOf(
            AppLanguage.ENGLISH to "K. Accessibility",
            AppLanguage.URDU to "11. رسائی اور سہولت (Accessibility)",
            AppLanguage.ARABIC to "١١. سهولة الاستخدام",
            AppLanguage.HINDI to "11. सुगम्यता (Accessibility)"
        ),
        "settings_sec_about" to mapOf(
            AppLanguage.ENGLISH to "L. Help, Provenance & About",
            AppLanguage.URDU to "12. مدد، مستند ذرائع اور تعارف",
            AppLanguage.ARABIC to "١٢. المساعدة والمصادر وحول التطبيق",
            AppLanguage.HINDI to "12. सहायता, प्रामाणिक स्रोत और परिचय"
        ),
        "settings_appearance_mode" to mapOf(
            AppLanguage.ENGLISH to "Appearance Mode",
            AppLanguage.URDU to "لائٹ / ڈارک موڈ",
            AppLanguage.ARABIC to "وضع الإضاءة",
            AppLanguage.HINDI to "लाइट / डार्क मोड"
        ),
        "settings_mode_system" to mapOf(
            AppLanguage.ENGLISH to "System",
            AppLanguage.URDU to "سسٹم کے مطابق",
            AppLanguage.ARABIC to "النظام",
            AppLanguage.HINDI to "सिस्टम"
        ),
        "settings_mode_light" to mapOf(
            AppLanguage.ENGLISH to "Light",
            AppLanguage.URDU to "لائٹ (روشن)",
            AppLanguage.ARABIC to "نهاري",
            AppLanguage.HINDI to "लाइट"
        ),
        "settings_mode_dark" to mapOf(
            AppLanguage.ENGLISH to "Dark",
            AppLanguage.URDU to "ڈارک (تاریک)",
            AppLanguage.ARABIC to "ليلي",
            AppLanguage.HINDI to "डार्क"
        ),
        "settings_reset_theme" to mapOf(
            AppLanguage.ENGLISH to "Reset Theme to Default",
            AppLanguage.URDU to "تھیم کو اصل حالت پر بحال کریں",
            AppLanguage.ARABIC to "استعادة المظهر الافتراضي",
            AppLanguage.HINDI to "थीम को डिफ़ॉल्ट पर रीसेट करें"
        ),
        "settings_wifi_only" to mapOf(
            AppLanguage.ENGLISH to "Wi-Fi Only Downloads",
            AppLanguage.URDU to "صرف وائی فائی پر ڈاؤنلوڈ",
            AppLanguage.ARABIC to "التنزيل عبر الواي فاي فقط",
            AppLanguage.HINDI to "केवल वाई-फ़ाई पर डाउनलोड"
        ),
        "settings_wifi_only_desc" to mapOf(
            AppLanguage.ENGLISH to "Prevent large Quran audio and podcast downloads on cellular data",
            AppLanguage.URDU to "موبائل ڈیٹا بچانے کے لیے بڑی آڈیو فائلیں صرف وائی فائی پر ڈاؤنلوڈ ہوں گی",
            AppLanguage.ARABIC to "منع التنزيلات الكبيرة على بيانات الهاتف للحفاظ على الباقة",
            AppLanguage.HINDI to "मोबाइल डेटा बचाने के लिए बड़े ऑडियो डाउनलोड केवल वाई-फ़ाई पर"
        ),
        "settings_clear_cache" to mapOf(
            AppLanguage.ENGLISH to "Clear Temporary Cache",
            AppLanguage.URDU to "عارضی کیش صاف کریں",
            AppLanguage.ARABIC to "مسح ذاكرة التخزين المؤقت",
            AppLanguage.HINDI to "अस्थायी कैश साफ़ करें"
        ),
        "settings_reset_prefs" to mapOf(
            AppLanguage.ENGLISH to "Reset Preferences Only",
            AppLanguage.URDU to "صرف ترتیبات ری سیٹ کریں",
            AppLanguage.ARABIC to "إعادة ضبط الإعدادات فقط",
            AppLanguage.HINDI to "केवल प्राथमिकताएं रीसेट करें"
        ),
        // ==================== RAMADAN COMPANION ====================
        "ramadan_companion" to mapOf(
            AppLanguage.ENGLISH to "Ramadan Companion",
            AppLanguage.URDU to "رمضان کمپینین",
            AppLanguage.ARABIC to "رفيق رمضان",
            AppLanguage.HINDI to "रमज़ान कंपैनियन"
        ),
        "ram_tab_dashboard" to mapOf(
            AppLanguage.ENGLISH to "Overview",
            AppLanguage.URDU to "جائزہ",
            AppLanguage.ARABIC to "نظرة عامة",
            AppLanguage.HINDI to "सारांश"
        ),
        "ram_tab_alarms" to mapOf(
            AppLanguage.ENGLISH to "Alarms",
            AppLanguage.URDU to "الارمز",
            AppLanguage.ARABIC to "التنبيهات",
            AppLanguage.HINDI to "अलार्म"
        ),
        "ram_tab_fasting" to mapOf(
            AppLanguage.ENGLISH to "Fasting & Quran",
            AppLanguage.URDU to "روزے اور قرآن",
            AppLanguage.ARABIC to "الصيام والقرآن",
            AppLanguage.HINDI to "रोज़ा व क़ुरआन"
        ),
        "ram_tab_deeds" to mapOf(
            AppLanguage.ENGLISH to "Duas & Deeds",
            AppLanguage.URDU to "دعائیں اور اعمال",
            AppLanguage.ARABIC to "الأدعية والأعمال",
            AppLanguage.HINDI to "दुआएँ व आमाल"
        ),
        "ram_tab_settings" to mapOf(
            AppLanguage.ENGLISH to "Ramadan Prefs",
            AppLanguage.URDU to "رمضان ترتیبات",
            AppLanguage.ARABIC to "إعدادات رمضان",
            AppLanguage.HINDI to "रमज़ान सेटिंग्स"
        ),
        "ram_sehri_ends" to mapOf(
            AppLanguage.ENGLISH to "Sehri Ends (Fajr)",
            AppLanguage.URDU to "اختتامِ سحری (فجر)",
            AppLanguage.ARABIC to "نهاية السحور (الفجر)",
            AppLanguage.HINDI to "सहरी समाप्ति (फ़ज्र)"
        ),
        "ram_iftar_time" to mapOf(
            AppLanguage.ENGLISH to "Iftar Time (Maghrib)",
            AppLanguage.URDU to "وقتِ افطار (مغرب)",
            AppLanguage.ARABIC to "وقت الإفطار (المغرب)",
            AppLanguage.HINDI to "इफ़्तार समय (मग़रिब)"
        ),
        "ram_next_alarm" to mapOf(
            AppLanguage.ENGLISH to "Next Ramadan Alarm",
            AppLanguage.URDU to "اگلا رمضان الارم",
            AppLanguage.ARABIC to "التنبيه القادم",
            AppLanguage.HINDI to "अगला रमज़ान अलार्म"
        ),
        "ram_day_progress" to mapOf(
            AppLanguage.ENGLISH to "Ramadan Day %d of %d",
            AppLanguage.URDU to "رمضان المبارک دن %d / %d",
            AppLanguage.ARABIC to "اليوم %d من %d رمضان",
            AppLanguage.HINDI to "रमज़ान दिन %d / %d"
        ),
        "ram_days_until" to mapOf(
            AppLanguage.ENGLISH to "%d days until projected Ramadan",
            AppLanguage.URDU to "متوقع رمضان میں %d دن باقی",
            AppLanguage.ARABIC to "متبقي %d يوماً على رمضان التقديري",
            AppLanguage.HINDI to "अनुमानित रमज़ान में %d दिन शेष"
        ),
        "ram_source_api" to mapOf(
            AppLanguage.ENGLISH to "Synced via AlAdhan API",
            AppLanguage.URDU to "AlAdhan API سے تازہ شدہ",
            AppLanguage.ARABIC to "محدّث عبر AlAdhan API",
            AppLanguage.HINDI to "AlAdhan API से सिंक किया गया"
        ),
        "ram_source_offline" to mapOf(
            AppLanguage.ENGLISH to "Offline Astronomical Calculation",
            AppLanguage.URDU to "آف لائن فلکیاتی حساب",
            AppLanguage.ARABIC to "حساب فلكي محلي بدون إنترنت",
            AppLanguage.HINDI to "ऑफ़लाइन खगोलीय गणना"
        ),
        "ram_stale_warning" to mapOf(
            AppLanguage.ENGLISH to "Cached timing may be outdated — refresh when online.",
            AppLanguage.URDU to "محفوظ کردہ اوقات پرانے ہو سکتے ہیں — آن لائن ہو کر تازہ کریں۔",
            AppLanguage.ARABIC to "قد تكون المواقيت المخزنة قديمة — يرجى التحديث عند الاتصال.",
            AppLanguage.HINDI to "सहेजे गए समय पुराने हो सकते हैं — ऑनलाइन होने पर रीफ़्रेश करें।"
        ),
        "ram_confirmed_start_badge" to mapOf(
            AppLanguage.ENGLISH to "Locally Confirmed Crescent Start",
            AppLanguage.URDU to "مقامی تصدیق شدہ آغازِ رمضان",
            AppLanguage.ARABIC to "بداية مؤكدة برؤية الهلال المحلية",
            AppLanguage.HINDI to "स्थानीय चाँद पुष्टि से प्रारंभ"
        ),
        "ram_estimated_hijri_badge" to mapOf(
            AppLanguage.ENGLISH to "Estimated Hijri Date (Verify Local Crescent)",
            AppLanguage.URDU to "تخمینی ہجری تاریخ (مقامی رویت سے تصدیق کریں)",
            AppLanguage.ARABIC to "تاريخ هجري تقديري (يُتحقق من الرؤية المحلية)",
            AppLanguage.HINDI to "अनुमानित हिजरी तिथि (स्थानीय चाँद की पुष्टि करें)"
        ),
        "ram_refresh_timings" to mapOf(
            AppLanguage.ENGLISH to "Refresh Daily Timings",
            AppLanguage.URDU to "اوقات تازہ کریں",
            AppLanguage.ARABIC to "تحديث المواقيت",
            AppLanguage.HINDI to "दैनिक समय रीफ़्रेश करें"
        ),
        "ram_alarm_sehri_pre_title" to mapOf(
            AppLanguage.ENGLISH to "Sehri Preparation Alarm",
            AppLanguage.URDU to "سحری کی تیاری کا الارم",
            AppLanguage.ARABIC to "تنبيه الاستعداد للسحور",
            AppLanguage.HINDI to "सहरी तैयारी अलार्म"
        ),
        "ram_alarm_sehri_end_title" to mapOf(
            AppLanguage.ENGLISH to "Sehri Ends — Fajr Time",
            AppLanguage.URDU to "اختتامِ سحری — وقتِ فجر",
            AppLanguage.ARABIC to "انتهاء السحور — دخول الفجر",
            AppLanguage.HINDI to "सहरी समाप्त — फ़ज्र का समय"
        ),
        "ram_alarm_iftar_pre_title" to mapOf(
            AppLanguage.ENGLISH to "Iftar Approaching Reminder",
            AppLanguage.URDU to "افطار قریب ہے — دعا کا وقت",
            AppLanguage.ARABIC to "اقترب الإفطار — وقت الدعاء",
            AppLanguage.HINDI to "इफ़्तार का समय करीब है"
        ),
        "ram_alarm_iftar_main_title" to mapOf(
            AppLanguage.ENGLISH to "Iftar Time — Maghrib",
            AppLanguage.URDU to "وقتِ افطار — مغرب",
            AppLanguage.ARABIC to "حان وقت الإفطار — المغرب",
            AppLanguage.HINDI to "इफ़्तार का समय — मग़रिब"
        ),
        "ram_alarm_taraweeh_title" to mapOf(
            AppLanguage.ENGLISH to "Taraweeh Night Prayer Reminder",
            AppLanguage.URDU to "نمازِ تراویح کی یاددہانی",
            AppLanguage.ARABIC to "تذكير صلاة التراويح",
            AppLanguage.HINDI to "तरावीह नमाज़ अनुस्मारक"
        ),
        "ram_alarm_tahajjud_title" to mapOf(
            AppLanguage.ENGLISH to "Tahajjud Last-Third Night Reminder",
            AppLanguage.URDU to "تہجد کی یاددہانی",
            AppLanguage.ARABIC to "تذكير قيام الليل والتهجد",
            AppLanguage.HINDI to "तहज्जुद अनुस्मारक"
        ),
        "ram_alarm_last10_title" to mapOf(
            AppLanguage.ENGLISH to "Last 10 Nights Devotion Reminder",
            AppLanguage.URDU to "آخری عشرہ عبادت کی یاددہانی",
            AppLanguage.ARABIC to "تذكير العشر الأواخر المباركة",
            AppLanguage.HINDI to "आख़िरी 10 रातें इबादत अनुस्मारक"
        ),
        "ram_scheduled_for" to mapOf(
            AppLanguage.ENGLISH to "Scheduled for",
            AppLanguage.URDU to "مقررہ وقت:",
            AppLanguage.ARABIC to "الموعد المحدد:",
            AppLanguage.HINDI to "निर्धारित समय:"
        ),
        "ram_snooze" to mapOf(
            AppLanguage.ENGLISH to "Snooze",
            AppLanguage.URDU to "اسنوز",
            AppLanguage.ARABIC to "غفوة",
            AppLanguage.HINDI to "स्नूज़"
        ),
        "ram_dismiss" to mapOf(
            AppLanguage.ENGLISH to "Stop",
            AppLanguage.URDU to "بند کریں",
            AppLanguage.ARABIC to "إيقاف",
            AppLanguage.HINDI to "रोकें"
        ),
        "ram_enable_alarms" to mapOf(
            AppLanguage.ENGLISH to "Enable Sehri & Iftar Auto Alarms",
            AppLanguage.URDU to "خودکار سحری و افطار الارمز فعال کریں",
            AppLanguage.ARABIC to "تفعيل منبهات السحور والإفطار التلقائية",
            AppLanguage.HINDI to "स्वचालित सहरी व इफ़्तार अलार्म चालू करें"
        ),
        "ram_sehri_pre_label" to mapOf(
            AppLanguage.ENGLISH to "Sehri Alarm (Minutes Before Fajr)",
            AppLanguage.URDU to "سحری الارم (فجر سے قبل منٹ)",
            AppLanguage.ARABIC to "منبه السحور (قبل الفجر بالدقائق)",
            AppLanguage.HINDI to "सहरी अलार्म (फ़ज्र से पहले मिनट)"
        ),
        "ram_sehri_fajr_end_label" to mapOf(
            AppLanguage.ENGLISH to "Second Alarm at Fajr (Sehri Ends)",
            AppLanguage.URDU to "فجر کے وقت اختتامِ سحری کا دوسرا الارم",
            AppLanguage.ARABIC to "منبه ثانٍ عند أذان الفجر",
            AppLanguage.HINDI to "फ़ज्र पर दूसरा अलार्म (सहरी समाप्ति)"
        ),
        "ram_iftar_pre_label" to mapOf(
            AppLanguage.ENGLISH to "Pre-Iftar Dua Reminder (Before Maghrib)",
            AppLanguage.URDU to "افطار سے قبل دعا کی یاددہانی (مغرب سے پہلے)",
            AppLanguage.ARABIC to "تذكير ما قبل الإفطار (قبل المغرب)",
            AppLanguage.HINDI to "इफ़्तार-पूर्व दुआ अनुस्मारक (मग़रिब से पहले)"
        ),
        "ram_iftar_main_label" to mapOf(
            AppLanguage.ENGLISH to "Main Iftar Alarm at Maghrib",
            AppLanguage.URDU to "مغرب کے وقت افطار کا مرکزی الارم",
            AppLanguage.ARABIC to "المنبه الرئيسي للإفطار عند المغرب",
            AppLanguage.HINDI to "मग़रिब पर मुख्य इफ़्तार अलार्म"
        ),
        "ram_sound_label" to mapOf(
            AppLanguage.ENGLISH to "Alarm Audio / Sound",
            AppLanguage.URDU to "الارم کی آواز",
            AppLanguage.ARABIC to "صوت التنبيه",
            AppLanguage.HINDI to "अलार्म ध्वनि"
        ),
        "ram_vibration_label" to mapOf(
            AppLanguage.ENGLISH to "Vibration",
            AppLanguage.URDU to "وائبریشن",
            AppLanguage.ARABIC to "الاهتزاز",
            AppLanguage.HINDI to "कंपन"
        ),
        "ram_snooze_label" to mapOf(
            AppLanguage.ENGLISH to "Snooze Duration",
            AppLanguage.URDU to "اسنوز کا دورانیہ",
            AppLanguage.ARABIC to "مدة الغفوة",
            AppLanguage.HINDI to "स्नूज़ अवधि"
        ),
        "ram_ramadan_only_label" to mapOf(
            AppLanguage.ENGLISH to "Auto-Pause When Ramadan Ends",
            AppLanguage.URDU to "رمضان ختم ہونے پر الارمز خودکار روکیں",
            AppLanguage.ARABIC to "إيقاف التنبيهات تلقائياً عند انتهاء رمضان",
            AppLanguage.HINDI to "रमज़ान समाप्त होने पर अलार्म स्वतः रोकें"
        ),
        "ram_test_alarm" to mapOf(
            AppLanguage.ENGLISH to "Test Alarm Notification",
            AppLanguage.URDU to "الارم ٹیسٹ کریں",
            AppLanguage.ARABIC to "تجربة إشعار التنبيه",
            AppLanguage.HINDI to "अलार्म परीक्षण करें"
        ),
        "ram_upcoming_alarms" to mapOf(
            AppLanguage.ENGLISH to "Upcoming Scheduled Alarms",
            AppLanguage.URDU to "آنے والے مقررہ الارمز",
            AppLanguage.ARABIC to "التنبيهات المجدولة القادمة",
            AppLanguage.HINDI to "आगामी निर्धारित अलार्म"
        ),
        "ram_no_upcoming_alarms" to mapOf(
            AppLanguage.ENGLISH to "No active Ramadan alarms scheduled (paused outside Ramadan or disabled).",
            AppLanguage.URDU to "فی الحال کوئی رمضان الارم مقرر نہیں (رمضان سے باہر یا بند ہے)۔",
            AppLanguage.ARABIC to "لا توجد تنبيهات مجدولة حالياً (متوقف خارج رمضان أو معطل).",
            AppLanguage.HINDI to "अभी कोई रमज़ान अलार्म शेड्यूल नहीं है (रमज़ान से बाहर या बंद)।"
        ),
        "ram_perm_notif_missing" to mapOf(
            AppLanguage.ENGLISH to "Notification permission is off — alarms cannot alert you visibly.",
            AppLanguage.URDU to "نوٹیفکیشن کی اجازت بند ہے — الارم ظاہر نہیں ہو سکے گا۔",
            AppLanguage.ARABIC to "إذن الإشعارات معطّل — لن تظهر التنبيهات.",
            AppLanguage.HINDI to "नोटिफिकेशन अनुमति बंद है — अलार्म दिखाई नहीं देंगे।"
        ),
        "ram_perm_exact_missing" to mapOf(
            AppLanguage.ENGLISH to "Exact Alarm permission off — Android may delay alarms slightly.",
            AppLanguage.URDU to "درست وقت کے الارم کی اجازت بند ہے — اینڈرائیڈ تاخیر کر سکتا ہے۔",
            AppLanguage.ARABIC to "إذن المنبه الدقيق غير مفعل — قد يتأخر التنبيه قليلاً.",
            AppLanguage.HINDI to "सटीक अलार्म अनुमति बंद है — एंड्रॉइड थोड़ी देरी कर सकता है।"
        ),
        "ram_grant_permission" to mapOf(
            AppLanguage.ENGLISH to "Grant",
            AppLanguage.URDU to "اجازت دیں",
            AppLanguage.ARABIC to "منح الإذن",
            AppLanguage.HINDI to "अनुमति दें"
        ),
        "ram_night_worship" to mapOf(
            AppLanguage.ENGLISH to "Night Worship Reminders",
            AppLanguage.URDU to "شب بیداری و تراویح کی یاددہانیاں",
            AppLanguage.ARABIC to "تذكيرات قيام الليل والتراويح",
            AppLanguage.HINDI to "रात्रि इबादत अनुस्मारक"
        ),
        "ram_taraweeh_reminder" to mapOf(
            AppLanguage.ENGLISH to "Taraweeh Reminder",
            AppLanguage.URDU to "نمازِ تراویح",
            AppLanguage.ARABIC to "تذكير التراويح",
            AppLanguage.HINDI to "तरावीह अनुस्मारक"
        ),
        "ram_tahajjud_reminder" to mapOf(
            AppLanguage.ENGLISH to "Tahajjud Before Fajr",
            AppLanguage.URDU to "تہجد (فجر سے قبل)",
            AppLanguage.ARABIC to "التهجد قبل الفجر",
            AppLanguage.HINDI to "फ़ज्र से पहले तहज्जुद"
        ),
        "ram_last10_reminder" to mapOf(
            AppLanguage.ENGLISH to "Last 10 Nights Devotion",
            AppLanguage.URDU to "آخری عشرہ شب بیداری",
            AppLanguage.ARABIC to "إحياء العشر الأواخر",
            AppLanguage.HINDI to "आख़िरी 10 रातें इबादत"
        ),
        "ram_last10_note" to mapOf(
            AppLanguage.ENGLISH to "Note: Seek Laylat al-Qadr across all odd nights of the last 10 nights; exact calendar certainty cannot be guaranteed automatically.",
            AppLanguage.URDU to "نوٹ: شبِ قدر کو آخری عشرے کی تمام طاق راتوں میں تلاش کریں؛ کسی ایک تاریخ کا حتمی تعین خودکار نہیں ہو سکتا۔",
            AppLanguage.ARABIC to "ملاحظة: تُلتمس ليلة القدر في الليالي الوتر من العشر الأواخر كلها؛ ولا يمكن الجزم بليلة محددة آلياً.",
            AppLanguage.HINDI to "नोट: लैलतुल-क़द्र को आख़िरी 10 की सभी ताक़ रातों में तलाश करें; किसी एक निश्चित तारीख़ की गारंटी स्वचालित नहीं दी जा सकती।"
        ),
        "ram_fasting_tracker" to mapOf(
            AppLanguage.ENGLISH to "Fasting Tracker (Personal Log)",
            AppLanguage.URDU to "روزوں کا ذاتی ریکارڈ",
            AppLanguage.ARABIC to "سجل الصيام الشخصي",
            AppLanguage.HINDI to "रोज़ा ट्रैकर (व्यक्तिगत रिकॉर्ड)"
        ),
        "ram_status_completed" to mapOf(
            AppLanguage.ENGLISH to "Fasted",
            AppLanguage.URDU to "مکمل روزہ",
            AppLanguage.ARABIC to "صائم",
            AppLanguage.HINDI to "पूर्ण रोज़ा"
        ),
        "ram_status_missed" to mapOf(
            AppLanguage.ENGLISH to "Missed (Qada)",
            AppLanguage.URDU to "قضا / رہ گیا",
            AppLanguage.ARABIC to "فائت (قضاء)",
            AppLanguage.HINDI to "छूटा (क़ज़ा)"
        ),
        "ram_status_excused" to mapOf(
            AppLanguage.ENGLISH to "Excused",
            AppLanguage.URDU to "شرعی عذر",
            AppLanguage.ARABIC to "عذر شرعي",
            AppLanguage.HINDI to "उज़्र (छूट)"
        ),
        "ram_status_not_recorded" to mapOf(
            AppLanguage.ENGLISH to "Unlogged",
            AppLanguage.URDU to "غیر درج",
            AppLanguage.ARABIC to "غير مسجل",
            AppLanguage.HINDI to "दर्ज नहीं"
        ),
        "ram_voluntary_fasts" to mapOf(
            AppLanguage.ENGLISH to "Voluntary Fasts (Mon/Thu & Shawwal)",
            AppLanguage.URDU to "نفلی روزے (پیر، جمعرات و شوال)",
            AppLanguage.ARABIC to "صيام التطوع (الإثنين والخميس وشوال)",
            AppLanguage.HINDI to "नफ़्ली रोज़े (सोम/गुरु व शव्वाल)"
        ),
        "ram_fasting_disclaimer" to mapOf(
            AppLanguage.ENGLISH to "Personal recordkeeping only — consult a qualified scholar for personal rulings on makeup fasts (Qada) or exemptions.",
            AppLanguage.URDU to "یہ صرف ذاتی ریکارڈ کے لیے ہے؛ قضا یا شرعی عذر کے مسائل کے لیے مستند عالمِ دین سے رجوع کریں۔",
            AppLanguage.ARABIC to "للتوثيق الشخصي فقط — يُرجع لأهل العلم الموثوقين في أحكام القضاء والأعذار.",
            AppLanguage.HINDI to "यह केवल व्यक्तिगत रिकॉर्ड के लिए है; क़ज़ा या उज़्र के फ़िक़्ही मसलों के लिए विश्वसनीय आलिम से पूछें।"
        ),
        "ram_quran_khatam_goal" to mapOf(
            AppLanguage.ENGLISH to "Quran Ramadan Goal",
            AppLanguage.URDU to "رمضان قرآن ہدف",
            AppLanguage.ARABIC to "هدف ختم القرآن في رمضان",
            AppLanguage.HINDI to "रमज़ान क़ुरआन लक्ष्य"
        ),
        "ram_pages_read" to mapOf(
            AppLanguage.ENGLISH to "Pages Read",
            AppLanguage.URDU to "پڑھے گئے صفحات",
            AppLanguage.ARABIC to "الصفحات المقروءة",
            AppLanguage.HINDI to "पढ़े गए पृष्ठ"
        ),
        "ram_daily_target" to mapOf(
            AppLanguage.ENGLISH to "Daily Target",
            AppLanguage.URDU to "روزانہ ہدف",
            AppLanguage.ARABIC to "الهدف اليومي",
            AppLanguage.HINDI to "दैनिक लक्ष्य"
        ),
        "ram_log_reading" to mapOf(
            AppLanguage.ENGLISH to "Log Reading",
            AppLanguage.URDU to "تلاوت درج کریں",
            AppLanguage.ARABIC to "تسجيل القراءة",
            AppLanguage.HINDI to "पठन दर्ज करें"
        ),
        "ram_open_quran_reader" to mapOf(
            AppLanguage.ENGLISH to "Open Quran Reader",
            AppLanguage.URDU to "قرآن ریڈر کھولیں",
            AppLanguage.ARABIC to "فتح قارئ القرآن",
            AppLanguage.HINDI to "क़ुरआन रीडर खोलें"
        ),
        "ram_authentic_duas" to mapOf(
            AppLanguage.ENGLISH to "Authentic Ramadan Duas",
            AppLanguage.URDU to "مستند رمضان دعائیں",
            AppLanguage.ARABIC to "أدعية رمضان الصحيحة",
            AppLanguage.HINDI to "प्रमाणित रमज़ान दुआएँ"
        ),
        "ram_dua_scholarly_note" to mapOf(
            AppLanguage.ENGLISH to "Only verified Prophetic narrations are listed below. Note: Fast intention is in the heart; popular Sehri wording is not an authentic Hadith, and saying Bismillah before eating is Sunnah (Bukhari 5376).",
            AppLanguage.URDU to "نیچے صرف مستند احادیث سے ثابت شدہ دعائیں شامل ہیں۔ نوٹ: روزے کی نیت دل کا عمل ہے؛ مشہور سحری کے الفاظ حدیث سے ثابت نہیں، جبکہ کھانے سے پہلے بسم اللہ پڑھنا سنت ہے (بخاری 5376)۔",
            AppLanguage.ARABIC to "تُعرض أدناه الأدعية الثابتة فقط؛ والنية محلها القلب مع التسمية عند السحور (البخاري 5376)، ولا يثبت لفظ مخصوص لنية الصيام.",
            AppLanguage.HINDI to "नीचे केवल प्रमाणित हदीसों की दुआएँ हैं। नोट: रोज़े की नीयत दिल से होती है; प्रचलित सहरी के शब्द हदीस से साबित नहीं हैं, खाने से पहले बिस्मिल्लाह कहना सुन्नत है (बुख़ारी 5376)।"
        ),
        "ram_good_deeds" to mapOf(
            AppLanguage.ENGLISH to "Private Good Deeds Tracker",
            AppLanguage.URDU to "نیکیوں کا ذاتی چارٹ",
            AppLanguage.ARABIC to "متابعة الأعمال الصالحة اليومية",
            AppLanguage.HINDI to "निजी नेक आमाल ट्रैकर"
        ),
        "ram_add_custom_deed" to mapOf(
            AppLanguage.ENGLISH to "Add Custom Deed",
            AppLanguage.URDU to "نیا نیک عمل شامل کریں",
            AppLanguage.ARABIC to "إضافة عمل صالح مخصص",
            AppLanguage.HINDI to "कस्टम नेक काम जोड़ें"
        ),
        "ram_sadaqah_log" to mapOf(
            AppLanguage.ENGLISH to "Private Sadaqah Log",
            AppLanguage.URDU to "ذاتی صدقہ و خیرات کا ریکارڈ",
            AppLanguage.ARABIC to "سجل الصدقات الخاص",
            AppLanguage.HINDI to "निजी सदक़ा लॉग"
        ),
        "ram_add_sadaqah" to mapOf(
            AppLanguage.ENGLISH to "Log Sadaqah",
            AppLanguage.URDU to "صدقہ درج کریں",
            AppLanguage.ARABIC to "تسجيل صدقة",
            AppLanguage.HINDI to "सदक़ा दर्ज करें"
        ),
        "ram_iftar_checklist" to mapOf(
            AppLanguage.ENGLISH to "Iftar Preparation Checklist",
            AppLanguage.URDU to "افطار تیاری چیک لسٹ",
            AppLanguage.ARABIC to "قائمة تحضير الإفطار",
            AppLanguage.HINDI to "इफ़्तार तैयारी चेकलिस्ट"
        ),
        "ram_add_checklist" to mapOf(
            AppLanguage.ENGLISH to "Add Checklist Item",
            AppLanguage.URDU to "چیک لسٹ آئٹم شامل کریں",
            AppLanguage.ARABIC to "إضافة عنصر للقائمة",
            AppLanguage.HINDI to "चेकलिस्ट आइटम जोड़ें"
        ),
        "ram_deed_5_salah" to mapOf(
            AppLanguage.ENGLISH to "Prayed all 5 daily Salah on time",
            AppLanguage.URDU to "پانچوں وقت کی نمازیں وقت پر ادا کیں",
            AppLanguage.ARABIC to "أداء الصلوات الخمس في وقتها",
            AppLanguage.HINDI to "पाँचों वक़्त की नमाज़ समय पर अदा की"
        ),
        "ram_deed_taraweeh" to mapOf(
            AppLanguage.ENGLISH to "Offered Taraweeh / Night Prayer",
            AppLanguage.URDU to "نمازِ تراویح / قیام اللیل ادا کیا",
            AppLanguage.ARABIC to "صلاة التراويح وقيام الليل",
            AppLanguage.HINDI to "तरावीह / तहज्जुद नमाज़ अदा की"
        ),
        "ram_deed_quran" to mapOf(
            AppLanguage.ENGLISH to "Read daily Quran portion",
            AppLanguage.URDU to "قرآن مجید کی روزانہ تلاوت کی",
            AppLanguage.ARABIC to "قراءة الورد اليومي من القرآن",
            AppLanguage.HINDI to "क़ुरआन की दैनिक तिलावत की"
        ),
        "ram_deed_adhkar" to mapOf(
            AppLanguage.ENGLISH to "Morning & evening Adhkar / Istighfar",
            AppLanguage.URDU to "صبح و شام کے اذکار اور استغفار",
            AppLanguage.ARABIC to "أذكار الصباح والمساء والاستغفار",
            AppLanguage.HINDI to "सुबह व शाम के अज़कार और इस्तिग़फ़ार"
        ),
        "ram_deed_charity" to mapOf(
            AppLanguage.ENGLISH to "Gave charity / fed a fasting person",
            AppLanguage.URDU to "صدقہ دیا یا کسی روزہ دار کو افطار کرایا",
            AppLanguage.ARABIC to "تصدقت أو أفطرت صائماً",
            AppLanguage.HINDI to "सदक़ा दिया या रोज़ेदार को इफ़्तार कराया"
        ),
        "ram_deed_family" to mapOf(
            AppLanguage.ENGLISH to "Called or helped parents & family",
            AppLanguage.URDU to "والدین اور اہلِ خانہ کی خدمت / خبر گیری کی",
            AppLanguage.ARABIC to "بر الوالدين وصلة الرحم",
            AppLanguage.HINDI to "माता-पिता व परिवार की सेवा की"
        ),
        "ram_deed_patience" to mapOf(
            AppLanguage.ENGLISH to "Guarded tongue, patience & kindness",
            AppLanguage.URDU to "زبان کی حفاظت، صبر اور حسنِ اخلاق",
            AppLanguage.ARABIC to "حفظ اللسان والصبر وحسن الخلق",
            AppLanguage.HINDI to "ज़ुबान की हिफ़ाज़त, सब्र व अच्छा व्यवहार"
        ),
        "ram_chk_dates" to mapOf(
            AppLanguage.ENGLISH to "Dates (Tamr) & Fresh Water",
            AppLanguage.URDU to "کھجور اور صاف پانی تیار رکھیں",
            AppLanguage.ARABIC to "تجهيز التمر والماء",
            AppLanguage.HINDI to "खजूर और ताज़ा पानी तैयार करें"
        ),
        "ram_chk_dua" to mapOf(
            AppLanguage.ENGLISH to "10 mins silent Dua before Maghrib",
            AppLanguage.URDU to "مغرب سے 10 منٹ قبل دعا کا اہتمام",
            AppLanguage.ARABIC to "الدعاء قبل أذان المغرب بـ 10 دقائق",
            AppLanguage.HINDI to "मग़रिब से 10 मिनट पहले दुआ"
        ),
        "ram_chk_share" to mapOf(
            AppLanguage.ENGLISH to "Share a meal with family or neighbors",
            AppLanguage.URDU to "اہلِ خانہ یا ہمسایوں کے ساتھ افطاری بانٹیں",
            AppLanguage.ARABIC to "مشاركة الإفطار مع الأهل والجيران",
            AppLanguage.HINDI to "परिवार या पड़ोसियों के साथ इफ़्तार बाँटें"
        ),
        "ram_chk_wudu" to mapOf(
            AppLanguage.ENGLISH to "Prepare Wudu & prayer mat for Maghrib",
            AppLanguage.URDU to "نمازِ مغرب کے لیے وضو اور جائے نماز تیار",
            AppLanguage.ARABIC to "الوضوء وتجهيز المصلى للمغرب",
            AppLanguage.HINDI to "मग़रिब के लिए वुज़ू व मुसल्ला तैयार"
        ),
        "ram_internal_prefs" to mapOf(
            AppLanguage.ENGLISH to "Ramadan Start & Calculation Context",
            AppLanguage.URDU to "آغازِ رمضان اور حسابی ترتیبات",
            AppLanguage.ARABIC to "تأكيد دخول رمضان وإعدادات الحساب",
            AppLanguage.HINDI to "रमज़ान शुरुआत व गणना सेटिंग्स"
        ),
        "ram_start_override_title" to mapOf(
            AppLanguage.ENGLISH to "Confirm Local Ramadan 1st Date",
            AppLanguage.URDU to "مقامی رویت سے یکم رمضان کی تصدیق",
            AppLanguage.ARABIC to "تأكيد غرة رمضان محلياً (YYYY-MM-DD)",
            AppLanguage.HINDI to "स्थानीय चाँद से 1 रमज़ान की पुष्टि करें"
        ),
        "ram_set_confirmed_start" to mapOf(
            AppLanguage.ENGLISH to "Apply Confirmed Start Date",
            AppLanguage.URDU to "تصدیق شدہ تاریخ محفوظ کریں",
            AppLanguage.ARABIC to "اعتماد التاريخ المؤكد",
            AppLanguage.HINDI to "पुष्ट तिथि लागू करें"
        ),
        "ram_clear_confirmed_start" to mapOf(
            AppLanguage.ENGLISH to "Use Projected Hijri Calendar",
            AppLanguage.URDU to "ہجری کیلنڈر کے مطابق رہنے دیں",
            AppLanguage.ARABIC to "استخدام التقويم الهجري التقديري",
            AppLanguage.HINDI to "अनुमानित हिजरी कैलेंडर उपयोग करें"
        ),
        "ram_month_length" to mapOf(
            AppLanguage.ENGLISH to "Ramadan Length",
            AppLanguage.URDU to "ماہِ رمضان کے دن",
            AppLanguage.ARABIC to "مدة الشهر",
            AppLanguage.HINDI to "रमज़ान के दिन"
        ),
        // Common Action Keys for Phase 1
        "play" to mapOf(
            AppLanguage.ENGLISH to "Play",
            AppLanguage.URDU to "سنیں",
            AppLanguage.ARABIC to "تشغيل",
            AppLanguage.HINDI to "चलाएँ"
        ),
        "stop" to mapOf(
            AppLanguage.ENGLISH to "Stop",
            AppLanguage.URDU to "بند کریں",
            AppLanguage.ARABIC to "إيقاف",
            AppLanguage.HINDI to "रोकें"
        ),
        "share" to mapOf(
            AppLanguage.ENGLISH to "Share",
            AppLanguage.URDU to "شیئر کریں",
            AppLanguage.ARABIC to "مشاركة",
            AppLanguage.HINDI to "साझा करें"
        ),
        "delete" to mapOf(
            AppLanguage.ENGLISH to "Delete",
            AppLanguage.URDU to "حذف کریں",
            AppLanguage.ARABIC to "حذف",
            AppLanguage.HINDI to "हटाएँ"
        ),
        "edit" to mapOf(
            AppLanguage.ENGLISH to "Edit",
            AppLanguage.URDU to "ترمیم کریں",
            AppLanguage.ARABIC to "تعديل",
            AppLanguage.HINDI to "संपादित करें"
        ),
        "save" to mapOf(
            AppLanguage.ENGLISH to "Save",
            AppLanguage.URDU to "محفوظ کریں",
            AppLanguage.ARABIC to "حفظ",
            AppLanguage.HINDI to "सहेजें"
        ),
        // Asma-ul-Husna Keys
        "asma_ul_husna" to mapOf(
            AppLanguage.ENGLISH to "Asma-ul-Husna",
            AppLanguage.URDU to "اسماء الحسنیٰ (99 نام)",
            AppLanguage.ARABIC to "أسماء الله الحسنى",
            AppLanguage.HINDI to "अस्मा-उल-हुस्ना (99 नाम)"
        ),
        "asma_daily_title" to mapOf(
            AppLanguage.ENGLISH to "Name of the Day",
            AppLanguage.URDU to "آج کا اسمِ مبارک",
            AppLanguage.ARABIC to "اسم اليوم",
            AppLanguage.HINDI to "आज का नाम-ए-मुबारक"
        ),
        "asma_explored_label" to mapOf(
            AppLanguage.ENGLISH to "Explored",
            AppLanguage.URDU to "مطالعہ",
            AppLanguage.ARABIC to "تمت قراءته",
            AppLanguage.HINDI to "देखे गए"
        ),
        "asma_favorites_count" to mapOf(
            AppLanguage.ENGLISH to "Favorites",
            AppLanguage.URDU to "پسندیدہ",
            AppLanguage.ARABIC to "المفضلة",
            AppLanguage.HINDI to "पसंदीदा"
        ),
        "asma_scholarly_note_title" to mapOf(
            AppLanguage.ENGLISH to "Scholarly Note on the 99-Name Enumerations",
            AppLanguage.URDU to "99 اسماءِ حسنیٰ کی فہرست کے بارے میں علمی وضاحت",
            AppLanguage.ARABIC to "تنبيه علمي حول قوائم الأسماء الحسنى",
            AppLanguage.HINDI to "99 नामों की सूची पर विद्वानों का संदर्भ नोट"
        ),
        "asma_search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search Arabic, transliteration, or meaning...",
            AppLanguage.URDU to "عربی، تلفظ یا معنی تلاش کریں...",
            AppLanguage.ARABIC to "ابحث بالاسم العربي أو المعنى...",
            AppLanguage.HINDI to "अरबी, उच्चारण या अर्थ खोजें..."
        ),
        "asma_filter_all" to mapOf(
            AppLanguage.ENGLISH to "All (99)",
            AppLanguage.URDU to "سب (99)",
            AppLanguage.ARABIC to "الكل (99)",
            AppLanguage.HINDI to "सभी (99)"
        ),
        "asma_filter_quranic" to mapOf(
            AppLanguage.ENGLISH to "Quranic",
            AppLanguage.URDU to "قرآنی",
            AppLanguage.ARABIC to "قرآنية",
            AppLanguage.HINDI to "क़ुरआनी"
        ),
        "asma_filter_hadith" to mapOf(
            AppLanguage.ENGLISH to "Hadith",
            AppLanguage.URDU to "حدیث",
            AppLanguage.ARABIC to "سنة صحيحة",
            AppLanguage.HINDI to "हदीस"
        ),
        "asma_filter_compilation" to mapOf(
            AppLanguage.ENGLISH to "Compilation",
            AppLanguage.URDU to "علمی فہرست",
            AppLanguage.ARABIC to "قائمة علمية",
            AppLanguage.HINDI to "विद्वान संकलन"
        ),
        "asma_filter_favorites" to mapOf(
            AppLanguage.ENGLISH to "Favorites",
            AppLanguage.URDU to "پسندیدہ",
            AppLanguage.ARABIC to "المفضلة",
            AppLanguage.HINDI to "पसंदीदा"
        ),
        "asma_empty_results" to mapOf(
            AppLanguage.ENGLISH to "No matching Names found.",
            AppLanguage.URDU to "کوئی متعلقہ اسم نہیں ملا۔",
            AppLanguage.ARABIC to "لم يتم العثور على نتائج مطابقة.",
            AppLanguage.HINDI to "कोई मेल खाने वाला नाम नहीं मिला।"
        ),
        "asma_pronunciation_title" to mapOf(
            AppLanguage.ENGLISH to "Arabic Pronunciation",
            AppLanguage.URDU to "عربی تلفظ",
            AppLanguage.ARABIC to "النطق العربي",
            AppLanguage.HINDI to "अरबी उच्चारण"
        ),
        "asma_pronunciation_tts_note" to mapOf(
            AppLanguage.ENGLISH to "Uses on-device Arabic speech synthesis (no Qari studio recording bundled offline).",
            AppLanguage.URDU to "ڈیوائس کے عربی تلفظ سسٹم (TTS) کے ذریعے (قاری کی ریکارڈنگ شامل نہیں)۔",
            AppLanguage.ARABIC to "نطق آلي عبر محرك اللغة العربية بالجهاز (لا توجد تلاوة مسجلة لقارئ).",
            AppLanguage.HINDI to "डिवाइस की अरबी वाणी तकनीक द्वारा (क़ारी स्टूडियो रिकॉर्डिंग शामिल नहीं है)।"
        ),
        "asma_pronunciation_unavailable" to mapOf(
            AppLanguage.ENGLISH to "Device Arabic TTS is unavailable; pre-recorded Qari audio is not bundled offline.",
            AppLanguage.URDU to "اس ڈیوائس پر عربی آڈیو سروس دستیاب نہیں؛ ریکارڈ شدہ قاری آڈیو شامل نہیں ہے۔",
            AppLanguage.ARABIC to "النطق العربي الآلي غير متاح على هذا الجهاز؛ ولا توجد تسجيلات صوتية مرفقة.",
            AppLanguage.HINDI to "इस डिवाइस पर अरबी ऑडियो इंजन उपलब्ध नहीं है; रिकॉर्डेड ऑडियो शामिल नहीं है।"
        ),
        "asma_pronunciation_checking" to mapOf(
            AppLanguage.ENGLISH to "Checking device Arabic pronunciation support...",
            AppLanguage.URDU to "عربی تلفظ کی سہولت چیک کی جا رہی ہے...",
            AppLanguage.ARABIC to "جارٍ التحقق من توفر النطق العربي...",
            AppLanguage.HINDI to "डिवाइस अरबी उच्चारण जांच की जा रही है..."
        ),
        "asma_meaning_context" to mapOf(
            AppLanguage.ENGLISH to "Meaning & Scholarly Explanation",
            AppLanguage.URDU to "معنی اور علمی وضاحت",
            AppLanguage.ARABIC to "المعنى والشرح",
            AppLanguage.HINDI to "अर्थ और व्याख्या"
        ),
        "asma_sources_label" to mapOf(
            AppLanguage.ENGLISH to "Verified Primary Sources",
            AppLanguage.URDU to "مستند حوالہ جات",
            AppLanguage.ARABIC to "المصادر والمراجع",
            AppLanguage.HINDI to "प्रमाणित मूल स्रोत"
        ),
        "asma_saved_fav" to mapOf(
            AppLanguage.ENGLISH to "Favorited",
            AppLanguage.URDU to "پسندیدہ میں محفوظ",
            AppLanguage.ARABIC to "في المفضلة",
            AppLanguage.HINDI to "पसंदीदा में सहेजा"
        ),
        "asma_add_fav" to mapOf(
            AppLanguage.ENGLISH to "Favorite",
            AppLanguage.URDU to "پسندیدہ بنائیں",
            AppLanguage.ARABIC to "أضف للمفضلة",
            AppLanguage.HINDI to "पसंदीदा"
        )
    )

    private fun stringsPart3(): Map<String, Map<AppLanguage, String>> = mapOf(
        // Hajj & Umrah Guide Keys
        "hajj_umrah_guide" to mapOf(
            AppLanguage.ENGLISH to "Hajj & Umrah Guide",
            AppLanguage.URDU to "حج و عمرہ گائیڈ",
            AppLanguage.ARABIC to "دليل الحج والعمرة",
            AppLanguage.HINDI to "हज और उमराह गाइड"
        ),
        "hajj_tab_intro" to mapOf(
            AppLanguage.ENGLISH to "Intro",
            AppLanguage.URDU to "تعارف",
            AppLanguage.ARABIC to "المقدمة",
            AppLanguage.HINDI to "परिचय"
        ),
        "hajj_tab_ihram" to mapOf(
            AppLanguage.ENGLISH to "Ihram",
            AppLanguage.URDU to "احرام",
            AppLanguage.ARABIC to "الإحرام",
            AppLanguage.HINDI to "इहराम"
        ),
        "hajj_tab_rituals" to mapOf(
            AppLanguage.ENGLISH to "Steps",
            AppLanguage.URDU to "مناسک",
            AppLanguage.ARABIC to "المناسك",
            AppLanguage.HINDI to "अरकान (चरण)"
        ),
        "hajj_tab_duas" to mapOf(
            AppLanguage.ENGLISH to "Duas",
            AppLanguage.URDU to "دعائیں",
            AppLanguage.ARABIC to "الأدعية",
            AppLanguage.HINDI to "दुआएँ"
        ),
        "hajj_tab_practical" to mapOf(
            AppLanguage.ENGLISH to "Practical",
            AppLanguage.URDU to "عملی تیاری",
            AppLanguage.ARABIC to "التجهيز والملاحظات",
            AppLanguage.HINDI to "व्यावहारिक तैयारी"
        ),
        "hajj_terminology_title" to mapOf(
            AppLanguage.ENGLISH to "Essential Pilgrimage Terminology",
            AppLanguage.URDU to "حج و عمرہ کی بنیادی اصطلاحات",
            AppLanguage.ARABIC to "أهم مصطلحات الحج والعمرة",
            AppLanguage.HINDI to "हज व उमराह की मुख्य शब्दावली"
        ),
        "hajj_meeqat_title" to mapOf(
            AppLanguage.ENGLISH to "The Five Meeqat Boundaries",
            AppLanguage.URDU to "پانچ میقات کے مقامات",
            AppLanguage.ARABIC to "المواقيت المكانية الخمسة",
            AppLanguage.HINDI to "पाँच मीक़ात सीमा-स्थान"
        ),
        "hajj_prohibitions_title" to mapOf(
            AppLanguage.ENGLISH to "Ihram Prohibitions (Men & Women)",
            AppLanguage.URDU to "احرام کی پابندیاں (مرد و خواتین)",
            AppLanguage.ARABIC to "محظورات الإحرام",
            AppLanguage.HINDI to "इहराम की वर्जनाएँ (पुरुष व महिला)"
        ),
        "hajj_select_guide_mode" to mapOf(
            AppLanguage.ENGLISH to "Select Pilgrimage Mode",
            AppLanguage.URDU to "گائیڈ منتخب کریں",
            AppLanguage.ARABIC to "اختر الدليل",
            AppLanguage.HINDI to "मार्गदर्शिका चुनें"
        ),
        "hajj_mode_umrah" to mapOf(
            AppLanguage.ENGLISH to "Umrah Guide",
            AppLanguage.URDU to "عمرہ گائیڈ",
            AppLanguage.ARABIC to "دليل العمرة",
            AppLanguage.HINDI to "उमराह गाइड"
        ),
        "hajj_mode_hajj" to mapOf(
            AppLanguage.ENGLISH to "Hajj Guide",
            AppLanguage.URDU to "حج گائیڈ",
            AppLanguage.ARABIC to "دليل الحج",
            AppLanguage.HINDI to "हज गाइड"
        ),
        "hajj_subtype_label" to mapOf(
            AppLanguage.ENGLISH to "Hajj Form:",
            AppLanguage.URDU to "حج کی قسم:",
            AppLanguage.ARABIC to "نوع النسك:",
            AppLanguage.HINDI to "हज का प्रकार:"
        ),
        "hajj_type_tamattu" to mapOf(
            AppLanguage.ENGLISH to "Tamattu'",
            AppLanguage.URDU to "تمتع",
            AppLanguage.ARABIC to "تمتع",
            AppLanguage.HINDI to "तमत्तु"
        ),
        "hajj_type_qiran" to mapOf(
            AppLanguage.ENGLISH to "Qiran",
            AppLanguage.URDU to "قران",
            AppLanguage.ARABIC to "قران",
            AppLanguage.HINDI to "क़िरान"
        ),
        "hajj_type_ifrad" to mapOf(
            AppLanguage.ENGLISH to "Ifrad",
            AppLanguage.URDU to "افراد",
            AppLanguage.ARABIC to "إفراد",
            AppLanguage.HINDI to "इफ़राद"
        ),
        "hajj_progress_label" to mapOf(
            AppLanguage.ENGLISH to "Study / Planning Progress",
            AppLanguage.URDU to "مطالعہ و یاددہانی کی پیشرفت",
            AppLanguage.ARABIC to "تقدم المراجعة والاستعداد",
            AppLanguage.HINDI to "अध्ययन व तैयारी प्रगति"
        ),
        "hajj_men_women_label" to mapOf(
            AppLanguage.ENGLISH to "Men vs Women Note",
            AppLanguage.URDU to "مرد و خواتین میں فرق",
            AppLanguage.ARABIC to "الفرق بين الرجال والنساء",
            AppLanguage.HINDI to "पुरुष बनाम महिला नियम"
        ),
        "hajj_ref_label" to mapOf(
            AppLanguage.ENGLISH to "Sources",
            AppLanguage.URDU to "حوالہ جات",
            AppLanguage.ARABIC to "المراجع",
            AppLanguage.HINDI to "स्रोत"
        ),
        "hajj_differences_btn" to mapOf(
            AppLanguage.ENGLISH to "Scholarly & Madhhab Differences",
            AppLanguage.URDU to "فقہی مذاہب کے مابین اختلافات دیکھیں",
            AppLanguage.ARABIC to "أقوال المذاهب الأربعة",
            AppLanguage.HINDI to "विद्वानों व मज़हबों के मतभेद देखें"
        ),
        "hajj_school_filter_label" to mapOf(
            AppLanguage.ENGLISH to "Jurisprudence View:",
            AppLanguage.URDU to "فقہی نقطۂ نظر:",
            AppLanguage.ARABIC to "عرض الأحكام الفقهية:",
            AppLanguage.HINDI to "फ़िक़्ही दृष्टिकोण:"
        ),
        "hajj_school_comparative" to mapOf(
            AppLanguage.ENGLISH to "Comparative (All)",
            AppLanguage.URDU to "مقارنہ (سب)",
            AppLanguage.ARABIC to "مقارن (الجميع)",
            AppLanguage.HINDI to "तुलनात्मक (सभी)"
        ),
        "hajj_school_hanafi" to mapOf(
            AppLanguage.ENGLISH to "Hanafi",
            AppLanguage.URDU to "حنفی",
            AppLanguage.ARABIC to "حنفي",
            AppLanguage.HINDI to "हनफ़ी"
        ),
        "hajj_school_shafii" to mapOf(
            AppLanguage.ENGLISH to "Shafi'i",
            AppLanguage.URDU to "شافعی",
            AppLanguage.ARABIC to "شافعي",
            AppLanguage.HINDI to "शाफ़ेई"
        ),
        "hajj_school_maliki" to mapOf(
            AppLanguage.ENGLISH to "Maliki",
            AppLanguage.URDU to "مالکی",
            AppLanguage.ARABIC to "مالكي",
            AppLanguage.HINDI to "मालिकी"
        ),
        "hajj_school_hanbali" to mapOf(
            AppLanguage.ENGLISH to "Hanbali",
            AppLanguage.URDU to "حنبلی",
            AppLanguage.ARABIC to "حنبلي",
            AppLanguage.HINDI to "हंबली"
        ),
        "hajj_add_checklist_title" to mapOf(
            AppLanguage.ENGLISH to "Add Custom Preparation Item",
            AppLanguage.URDU to "ذاتی چیک لسٹ آئٹم شامل کریں",
            AppLanguage.ARABIC to "إضافة عنصر جديد للقائمة",
            AppLanguage.HINDI to "कस्टम तैयारी आइटम जोड़ें"
        ),
        "hajj_add_checklist_hint" to mapOf(
            AppLanguage.ENGLISH to "e.g., Extra safety pins, medical file...",
            AppLanguage.URDU to "مثلاً اضافی سیفٹی پن، میڈیکل رپورٹس...",
            AppLanguage.ARABIC to "مثال: دبابيس إضافية، تقارير طبية...",
            AppLanguage.HINDI to "जैसे अतिरिक्त सेफ़्टी पिन, मेडिकल फाइल..."
        ),
        "hajj_personal_notes_title" to mapOf(
            AppLanguage.ENGLISH to "Personal Pilgrimage Notes",
            AppLanguage.URDU to "ذاتی یادداشتیں",
            AppLanguage.ARABIC to "ملاحظاتي الشخصية",
            AppLanguage.HINDI to "व्यक्तिगत नोट्स"
        ),
        "hajj_new_note_btn" to mapOf(
            AppLanguage.ENGLISH to "New Note",
            AppLanguage.URDU to "نیا نوٹ",
            AppLanguage.ARABIC to "ملاحظة جديدة",
            AppLanguage.HINDI to "नया नोट"
        ),
        "hajj_notes_empty" to mapOf(
            AppLanguage.ENGLISH to "No personal notes saved yet. Add reminders, hotel contacts, or scholarly questions here.",
            AppLanguage.URDU to "ابھی کوئی ذاتی نوٹ محفوظ نہیں ہے۔ ہوٹل کی معلومات، دعائیں یا علمی سوالات یہاں لکھیں۔",
            AppLanguage.ARABIC to "لا توجد ملاحظات بعد. أضف أرقام الحملة أو أدعية خاصة أو تذكيرات.",
            AppLanguage.HINDI to "अभी कोई व्यक्तिगत नोट सहेजा नहीं गया है। यहाँ अनुस्मारक या प्रश्न सहेजें।"
        ),
        "hajj_official_links_title" to mapOf(
            AppLanguage.ENGLISH to "Official Authorities & References",
            AppLanguage.URDU to "سرکاری پورٹلز اور مستند حوالے",
            AppLanguage.ARABIC to "البوابات الرسمية والمراجع",
            AppLanguage.HINDI to "आधिकारिक पोर्टल और संदर्भ"
        ),
        "hajj_note_title_hint" to mapOf(
            AppLanguage.ENGLISH to "Note title",
            AppLanguage.URDU to "نوٹ کا عنوان",
            AppLanguage.ARABIC to "عنوان الملاحظة",
            AppLanguage.HINDI to "नोट का शीर्षक"
        ),
        "hajj_note_body_hint" to mapOf(
            AppLanguage.ENGLISH to "Write reminder or details...",
            AppLanguage.URDU to "تفصیل یہاں لکھیں...",
            AppLanguage.ARABIC to "اكتب التفاصيل هنا...",
            AppLanguage.HINDI to "विवरण यहाँ लिखें..."
        ),
        "hajj_chk_ihram" to mapOf(
            AppLanguage.ENGLISH to "Ihram Prep",
            AppLanguage.URDU to "احرام کی تیاری",
            AppLanguage.ARABIC to "تجهيز الإحرام",
            AppLanguage.HINDI to "इहराम तैयारी"
        ),
        "hajj_chk_packing" to mapOf(
            AppLanguage.ENGLISH to "Packing",
            AppLanguage.URDU to "سامانِ سفر",
            AppLanguage.ARABIC to "حقيبة السفر",
            AppLanguage.HINDI to "पैकिंग"
        ),
        "hajj_chk_travel" to mapOf(
            AppLanguage.ENGLISH to "Travel Prep",
            AppLanguage.URDU to "سفری تیاری",
            AppLanguage.ARABIC to "الاستعداد والسفر",
            AppLanguage.HINDI to "यात्रा तैयारी"
        ),
        // ---- Phase 2: Mosque Finder ----
        "mosque_finder" to mapOf(
            AppLanguage.ENGLISH to "Mosque Finder",
            AppLanguage.URDU to "مسجد تلاش کریں",
            AppLanguage.ARABIC to "الباحث عن المساجد",
            AppLanguage.HINDI to "मस्जिद खोजक"
        ),
        "grant_location" to mapOf(
            AppLanguage.ENGLISH to "Allow Location",
            AppLanguage.URDU to "لوکیشن اجازت دیں",
            AppLanguage.ARABIC to "منح الموقع",
            AppLanguage.HINDI to "लोकेशन अनुमति दें"
        ),
        "mosque_search_area_hint" to mapOf(
            AppLanguage.ENGLISH to "Search city or neighborhood (press Search)...",
            AppLanguage.URDU to "شہر یا علاقہ تلاش کریں (سرچ دبائیں)...",
            AppLanguage.ARABIC to "ابحث عن مدينة أو حي (اضغط بحث)...",
            AppLanguage.HINDI to "शहर या क्षेत्र खोजें (सर्च दबाएँ)..."
        ),
        "mosque_search_btn" to mapOf(
            AppLanguage.ENGLISH to "Search Area",
            AppLanguage.URDU to "علاقہ تلاش کریں",
            AppLanguage.ARABIC to "بحث المنطقة",
            AppLanguage.HINDI to "क्षेत्र खोजें"
        ),
        "mosque_area_not_found" to mapOf(
            AppLanguage.ENGLISH to "Area not found. Try a nearby city or search the map.",
            AppLanguage.URDU to "مطلوبہ علاقہ نہیں ملا۔ قریبی شہر لکھیں یا نقشہ استعمال کریں۔",
            AppLanguage.ARABIC to "لم يتم العثور على المنطقة. جرب مدينة قريبة أو استخدم الخريطة.",
            AppLanguage.HINDI to "क्षेत्र नहीं मिला। नज़दीकी शहर लिखें या मानचित्र का उपयोग करें।"
        ),
        "mosque_cat_all" to mapOf(
            AppLanguage.ENGLISH to "All",
            AppLanguage.URDU to "سب",
            AppLanguage.ARABIC to "الكل",
            AppLanguage.HINDI to "सभी"
        ),
        "mosque_filter_results_hint" to mapOf(
            AppLanguage.ENGLISH to "Filter loaded mosques by name or street...",
            AppLanguage.URDU to "موجود مساجد میں نام یا پتہ تلاش کریں...",
            AppLanguage.ARABIC to "تصفية المساجد حسب الاسم أو العنوان...",
            AppLanguage.HINDI to "मिले हुए मस्जिदों में नाम से फ़िल्टर करें..."
        ),
        "mosque_permission_hint" to mapOf(
            AppLanguage.ENGLISH to "Location permission denied or unset. Search any city manually or grant access.",
            AppLanguage.URDU to "لوکیشن کی اجازت نہیں ہے؛ آپ کوئی بھی شہر خود تلاش کر سکتے ہیں یا اجازت دے سکتے ہیں۔",
            AppLanguage.ARABIC to "إذن الموقع غير مفعّل؛ يمكنك البحث عن أي مدينة يدوياً أو منح الإذن.",
            AppLanguage.HINDI to "लोकेशन अनुमति नहीं मिली है; आप कोई भी शहर खोज सकते हैं या अनुमति दे सकते हैं।"
        ),
        "mosque_precision_precise" to mapOf(
            AppLanguage.ENGLISH to "Using precise GPS location",
            AppLanguage.URDU to "درست جی پی ایس لوکیشن استعمال ہو رہی ہے",
            AppLanguage.ARABIC to "يتم استخدام الموقع الدقيق (GPS)",
            AppLanguage.HINDI to "सटीक GPS लोकेशन उपयोग में है"
        ),
        "mosque_precision_approximate" to mapOf(
            AppLanguage.ENGLISH to "Using approximate location (distances are estimates)",
            AppLanguage.URDU to "تخمینی لوکیشن استعمال ہو رہی ہے (فاصلہ اندازاً ہے)",
            AppLanguage.ARABIC to "يتم استخدام موقع تقريبي (المسافات تقديرية)",
            AppLanguage.HINDI to "अनुमानित लोकेशन उपयोग में है (दूरी अनुमानित है)"
        ),
        "mosque_precision_manual" to mapOf(
            AppLanguage.ENGLISH to "Using manually selected area / city",
            AppLanguage.URDU to "منتخب کردہ شہر / علاقہ استعمال ہو رہا ہے",
            AppLanguage.ARABIC to "يتم استخدام المدينة أو المنطقة المحددة يدوياً",
            AppLanguage.HINDI to "चयनित क्षेत्र / शहर उपयोग में है"
        ),
        "mosque_use_my_location" to mapOf(
            AppLanguage.ENGLISH to "My Location",
            AppLanguage.URDU to "میری لوکیشن",
            AppLanguage.ARABIC to "موقعي الحالي",
            AppLanguage.HINDI to "मेरी लोकेशन"
        ),
        "mosque_cache_stale_notice" to mapOf(
            AppLanguage.ENGLISH to "Offline / cached results — data may be outdated",
            AppLanguage.URDU to "آف لائن محفوظ نتائج — معلومات پرانی ہو سکتی ہیں",
            AppLanguage.ARABIC to "نتائج محفوظة دون إنترنت — قد تكون البيانات قديمة",
            AppLanguage.HINDI to "ऑफ़लाइन सहेजे गए परिणाम — डेटा पुराना हो सकता है"
        ),
        "mosque_cache_saved_notice" to mapOf(
            AppLanguage.ENGLISH to "Loaded saved OpenStreetMap cache",
            AppLanguage.URDU to "محفوظ اوپن اسٹریٹ میپ کیش سے لوڈ کیا گیا",
            AppLanguage.ARABIC to "تم التحميل من ذاكرة خريطة الشارع المفتوحة",
            AppLanguage.HINDI to "सहेजे गए OpenStreetMap कैश से लोड किया गया"
        ),
        "mosque_list_view" to mapOf(
            AppLanguage.ENGLISH to "List View",
            AppLanguage.URDU to "فہرست",
            AppLanguage.ARABIC to "عرض القائمة",
            AppLanguage.HINDI to "सूची दृश्य"
        ),
        "mosque_map_view" to mapOf(
            AppLanguage.ENGLISH to "Map View",
            AppLanguage.URDU to "نقشہ",
            AppLanguage.ARABIC to "عرض الخريطة",
            AppLanguage.HINDI to "मानचित्र दृश्य"
        ),
        "mosque_search_visible_area" to mapOf(
            AppLanguage.ENGLISH to "Search Visible Area",
            AppLanguage.URDU to "اس علاقے میں تلاش کریں",
            AppLanguage.ARABIC to "البحث في هذه المنطقة",
            AppLanguage.HINDI to "इस क्षेत्र में खोजें"
        ),
        "mosque_map_area_label" to mapOf(
            AppLanguage.ENGLISH to "Selected Map Area",
            AppLanguage.URDU to "منتخب نقشہ علاقہ",
            AppLanguage.ARABIC to "منطقة الخريطة المحددة",
            AppLanguage.HINDI to "चयनित मानचित्र क्षेत्र"
        ),
        "mosque_searching" to mapOf(
            AppLanguage.ENGLISH to "Querying OpenStreetMap mosques...",
            AppLanguage.URDU to "قریبی مساجد تلاش کی جا رہی ہیں...",
            AppLanguage.ARABIC to "جارٍ البحث عن المساجد القريبة...",
            AppLanguage.HINDI to "आस-पास की मस्जिदें खोजी जा रही हैं..."
        ),
        "mosque_straight_line_short" to mapOf(
            AppLanguage.ENGLISH to "straight-line",
            AppLanguage.URDU to "سیدھا فاصلہ",
            AppLanguage.ARABIC to "مسافة مستقيمة",
            AppLanguage.HINDI to "सीधी दूरी"
        ),
        "mosque_straight_line_distance" to mapOf(
            AppLanguage.ENGLISH to "Straight-line distance",
            AppLanguage.URDU to "سیدھا (ہوائی) فاصلہ",
            AppLanguage.ARABIC to "المسافة المستقيمة",
            AppLanguage.HINDI to "सीधी दूरी"
        ),
        "mosque_route_external_note" to mapOf(
            AppLanguage.ENGLISH to "Driving/walking routes open in your external Maps app.",
            AppLanguage.URDU to "سڑک کا راستہ اور سفر کا وقت بیرونی میپس ایپ میں کھلے گا۔",
            AppLanguage.ARABIC to "تُفتح مسارات القيادة والمشي في تطبيق الخرائط الخارجي.",
            AppLanguage.HINDI to "सड़क मार्ग और यात्रा समय आपके बाहरी मैप्स ऐप में खुलेंगे।"
        ),
        "mosque_verified_osm_tags" to mapOf(
            AppLanguage.ENGLISH to "OpenStreetMap Record Details",
            AppLanguage.URDU to "اوپن اسٹریٹ میپ کی تصدیق شدہ تفصیلات",
            AppLanguage.ARABIC to "تفاصيل سجل خريطة الشارع المفتوحة",
            AppLanguage.HINDI to "OpenStreetMap अभिलेख विवरण"
        ),
        "mosque_not_provided_osm" to mapOf(
            AppLanguage.ENGLISH to "Not provided in OpenStreetMap",
            AppLanguage.URDU to "اوپن اسٹریٹ میپ پر درج نہیں",
            AppLanguage.ARABIC to "غير متوفر في خريطة الشارع المفتوحة",
            AppLanguage.HINDI to "OpenStreetMap में उपलब्ध नहीं"
        ),
        "mosque_address_label" to mapOf(
            AppLanguage.ENGLISH to "Address",
            AppLanguage.URDU to "پتہ",
            AppLanguage.ARABIC to "العنوان",
            AppLanguage.HINDI to "पता"
        ),
        "mosque_hours_label" to mapOf(
            AppLanguage.ENGLISH to "Opening Hours (OSM)",
            AppLanguage.URDU to "اوقات (OSM)",
            AppLanguage.ARABIC to "ساعات العمل",
            AppLanguage.HINDI to "खुलने का समय (OSM)"
        ),
        "mosque_phone_label" to mapOf(
            AppLanguage.ENGLISH to "Phone",
            AppLanguage.URDU to "فون",
            AppLanguage.ARABIC to "الهاتف",
            AppLanguage.HINDI to "फ़ोन"
        ),
        "mosque_website_label" to mapOf(
            AppLanguage.ENGLISH to "Website",
            AppLanguage.URDU to "ویب سائٹ",
            AppLanguage.ARABIC to "الموقع الإلكتروني",
            AppLanguage.HINDI to "वेबसाइट"
        ),
        "mosque_wheelchair_label" to mapOf(
            AppLanguage.ENGLISH to "Wheelchair Accessibility",
            AppLanguage.URDU to "وہیل چیئر سہولت",
            AppLanguage.ARABIC to "إمكانية الوصول للكراسي المتحركة",
            AppLanguage.HINDI to "व्हीलचेयर सुविधा"
        ),
        "mosque_get_directions" to mapOf(
            AppLanguage.ENGLISH to "Get Directions in Maps",
            AppLanguage.URDU to "راستہ دیکھیں (Maps)",
            AppLanguage.ARABIC to "الحصول على الاتجاهات",
            AppLanguage.HINDI to "मैप्स में दिशा-निर्देश देखें"
        ),
        "mosque_report_edit_osm" to mapOf(
            AppLanguage.ENGLISH to "Report / Edit on OpenStreetMap",
            AppLanguage.URDU to "اوپن اسٹریٹ میپ پر ترمیم / اطلاع کریں",
            AppLanguage.ARABIC to "الإبلاغ أو التعديل في OpenStreetMap",
            AppLanguage.HINDI to "OpenStreetMap पर सुधार / रिपोर्ट करें"
        ),
        "mosque_empty_title" to mapOf(
            AppLanguage.ENGLISH to "No mapped mosques found here",
            AppLanguage.URDU to "اس دائرے میں کوئی مسجد نہیں ملی",
            AppLanguage.ARABIC to "لم يُعثر على مساجد مسجلة في هذا النطاق",
            AppLanguage.HINDI to "इस दायरे में कोई सूचीबद्ध मस्जिद नहीं मिली"
        ),
        "mosque_empty_desc" to mapOf(
            AppLanguage.ENGLISH to "Try expanding the search radius, clearing filters, or searching another neighborhood.",
            AppLanguage.URDU to "دائرہ (کلومیٹر) بڑھائیں، فلٹر صاف کریں یا کوئی دوسرا علاقہ تلاش کریں۔",
            AppLanguage.ARABIC to "جرب توسيع نطاق البحث أو إزالة الفلاتر أو البحث عن حي آخر.",
            AppLanguage.HINDI to "खोज का दायरा बढ़ाएँ, फ़िल्टर हटाएँ या दूसरा क्षेत्र खोजें।"
        ),
        "mosque_coverage_title" to mapOf(
            AppLanguage.ENGLISH to "OpenStreetMap Community Data Notice",
            AppLanguage.URDU to "اوپن اسٹریٹ میپ ڈیٹا کی وضاحت",
            AppLanguage.ARABIC to "ملاحظة حول بيانات خريطة الشارع المفتوحة",
            AppLanguage.HINDI to "OpenStreetMap सामुदायिक डेटा सूचना"
        ),
        "mosque_coverage_body" to mapOf(
            AppLanguage.ENGLISH to "Mosque locations, names, and metadata come strictly from live OpenStreetMap records. Rural or newly built masajid may not yet be mapped; please verify locally.",
            AppLanguage.URDU to "مساجد کا ڈیٹا صرف اوپن اسٹریٹ میپ سے لیا جاتا ہے۔ دیہی یا نئی مساجد نقشے پر نہ ہونے کا امکان رہتا ہے، مقامی تصدیق ضرور کریں۔",
            AppLanguage.ARABIC to "تُستمد بيانات المساجد حصراً من خريطة الشارع المفتوحة. قد تغيب بعض المساجد الريفية أو الحديثة غير المضافة بعد.",
            AppLanguage.HINDI to "मस्जिदों की जानकारी केवल OpenStreetMap से आती है। कुछ ग्रामीण या नई मस्जिदें मानचित्र पर न हों; स्थानीय पुष्टि अवश्य करें।"
        ),
        "mosque_results_count_fmt" to mapOf(
            AppLanguage.ENGLISH to "%d mosques found (%d km)",
            AppLanguage.URDU to "%d مساجد ملیں (%d کلومیٹر)",
            AppLanguage.ARABIC to "تم العثور على %d مساجد (%d كم)",
            AppLanguage.HINDI to "%d मस्जिदें मिलीं (%d किमी)"
        ),
        "mosque_result_single_fmt" to mapOf(
            AppLanguage.ENGLISH to "1 mosque found (%d km)",
            AppLanguage.URDU to "1 مسجد ملی (%d کلومیٹر)",
            AppLanguage.ARABIC to "تم العثور على مسجد واحد (%d كم)",
            AppLanguage.HINDI to "1 मस्जिद मिली (%d किमी)"
        ),
        "mosque_load_more_fmt" to mapOf(
            AppLanguage.ENGLISH to "Show more mosques (%d remaining)",
            AppLanguage.URDU to "مزید مساجد دکھائیں (%d باقی)",
            AppLanguage.ARABIC to "عرض المزيد من المساجد (%d متبقية)",
            AppLanguage.HINDI to "और मस्जिदें दिखाएँ (%d शेष)"
        ),
        "mosque_expand_radius_fmt" to mapOf(
            AppLanguage.ENGLISH to "Expand search to %d km",
            AppLanguage.URDU to "دائرہ %d کلومیٹر تک بڑھائیں",
            AppLanguage.ARABIC to "توسيع البحث إلى %d كم",
            AppLanguage.HINDI to "खोज का दायरा %d किमी तक बढ़ाएँ"
        ),
        "mosque_prev_cd" to mapOf(
            AppLanguage.ENGLISH to "Previous mosque",
            AppLanguage.URDU to "پچھلی مسجد",
            AppLanguage.ARABIC to "المسجد السابق",
            AppLanguage.HINDI to "पिछली मस्जिद"
        ),
        "mosque_next_cd" to mapOf(
            AppLanguage.ENGLISH to "Next mosque",
            AppLanguage.URDU to "اگلی مسجد",
            AppLanguage.ARABIC to "المسجد التالي",
            AppLanguage.HINDI to "अगली मस्जिद"
        ),
        "mosque_live_data_badge" to mapOf(
            AppLanguage.ENGLISH to "Live OpenStreetMap data",
            AppLanguage.URDU to "تازہ اوپن اسٹریٹ میپ ڈیٹا",
            AppLanguage.ARABIC to "بيانات خريطة الشارع المفتوحة الحية",
            AppLanguage.HINDI to "लाइव OpenStreetMap डेटा"
        ),
        "mosque_gps_disabled_hint" to mapOf(
            AppLanguage.ENGLISH to "Device location (GPS) is turned off. Enable GPS or search any city manually.",
            AppLanguage.URDU to "ڈیوائس کی لوکیشن (GPS) بند ہے۔ GPS آن کریں یا کوئی شہر خود تلاش کریں۔",
            AppLanguage.ARABIC to "خدمة الموقع (GPS) متوقفة. فعّل GPS أو ابحث عن أي مدينة يدوياً.",
            AppLanguage.HINDI to "डिवाइस की लोकेशन (GPS) बंद है। GPS चालू करें या कोई शहर मैन्युअल रूप से खोजें।"
        ),
        "mosque_open_settings" to mapOf(
            AppLanguage.ENGLISH to "Location Settings",
            AppLanguage.URDU to "لوکیشن سیٹنگز",
            AppLanguage.ARABIC to "إعدادات الموقع",
            AppLanguage.HINDI to "लोकेशन सेटिंग्स"
        ),
        "mosque_location_unset_title" to mapOf(
            AppLanguage.ENGLISH to "No location selected yet",
            AppLanguage.URDU to "ابھی کوئی لوکیشن منتخب نہیں کی گئی",
            AppLanguage.ARABIC to "لم يتم تحديد موقع بعد",
            AppLanguage.HINDI to "अभी कोई लोकेशन नहीं चुनी गई है"
        ),
        "mosque_location_unset_desc" to mapOf(
            AppLanguage.ENGLISH to "Grant location permission for nearby mosques, or search any city or neighborhood above.",
            AppLanguage.URDU to "قریبی مساجد کے لیے لوکیشن کی اجازت دیں یا اوپر کسی بھی شہر یا علاقے کو تلاش کریں۔",
            AppLanguage.ARABIC to "امنح إذن الموقع للبحث عن المساجد القريبة، أو ابحث عن أي مدينة أو حي في الأعلى.",
            AppLanguage.HINDI to "आस-पास की मस्जिदों के लिए लोकेशन अनुमति दें या ऊपर किसी भी शहर या क्षेत्र को खोजें।"
        ),
        "mosque_endpoint_settings_title" to mapOf(
            AppLanguage.ENGLISH to "Overpass API Endpoint",
            AppLanguage.URDU to "اوور پاس API سرور",
            AppLanguage.ARABIC to "خادم Overpass API",
            AppLanguage.HINDI to "Overpass API सर्वर"
        ),
        "mosque_endpoint_auto" to mapOf(
            AppLanguage.ENGLISH to "Automatic Mirror Failover (Recommended)",
            AppLanguage.URDU to "خودکار سرور فیل اوور (تجویز کردہ)",
            AppLanguage.ARABIC to "التبديل التلقائي بين الخوادم (موصى به)",
            AppLanguage.HINDI to "स्वचालित सर्वर फ़ेलओवर (अनुशंसित)"
        ),
        "mosque_endpoint_rate_note" to mapOf(
            AppLanguage.ENGLISH to "Public OpenStreetMap Overpass mirrors have shared rate limits (HTTP 429). Queries are cached locally and throttled to stay respectful.",
            AppLanguage.URDU to "عوامی اوور پاس سرورز کی رفتار محدود ہوتی ہے۔ نتائج محفوظ کیے جاتے ہیں تاکہ بار بار درخواست نہ جائے۔",
            AppLanguage.ARABIC to "خوادم Overpass العامة لها حدود استخدام مشتركة (HTTP 429). يتم تخزين النتائج محلياً وتنظيم الطلبات.",
            AppLanguage.HINDI to "सार्वजनिक Overpass सर्वरों की साझा सीमा (HTTP 429) होती है। अनुरोध सीमित कर परिणाम कैश किए जाते हैं।"
        ),
        "mosque_candidate_warning" to mapOf(
            AppLanguage.ENGLISH to "Tagged as building=mosque without place_of_worship; active status unverified in OSM.",
            AppLanguage.URDU to "یہ صرف عمارت کے طور پر درج ہے؛ فعال مسجد کی حیثیت غیر مصدقہ ہے۔",
            AppLanguage.ARABIC to "مسجل كمبنى مسجد فقط دون وسم مكان عبادة؛ النشاط الحالي غير مؤكد في OSM.",
            AppLanguage.HINDI to "केवल building=mosque के रूप में दर्ज है; सक्रिय स्थिति अपुष्ट है।"
        ),
        "mosque_denomination_label" to mapOf(
            AppLanguage.ENGLISH to "Denomination (OSM)",
            AppLanguage.URDU to "مسلک / فقہ (OSM)",
            AppLanguage.ARABIC to "المذهب (OSM)",
            AppLanguage.HINDI to "संप्रदाय (OSM)"
        ),
        "mosque_retry" to mapOf(
            AppLanguage.ENGLISH to "Retry",
            AppLanguage.URDU to "دوبارہ کوشش",
            AppLanguage.ARABIC to "إعادة المحاولة",
            AppLanguage.HINDI to "पुनः प्रयास"
        ),
        "mosque_tiles_error" to mapOf(
            AppLanguage.ENGLISH to "Map tiles could not load right now. Switch to List View or tap Retry.",
            AppLanguage.URDU to "نقشے کی ٹائلیں لوڈ نہیں ہو سکیں۔ فہرست دیکھیں یا دوبارہ کوشش کریں۔",
            AppLanguage.ARABIC to "تعذر تحميل مربعات الخريطة الآن. انتقل إلى القائمة أو أعد المحاولة.",
            AppLanguage.HINDI to "मानचित्र टाइल्स लोड नहीं हो सकीं। सूची दृश्य देखें या पुनः प्रयास करें।"
        ),
        "mosque_mode_3d" to mapOf(
            AppLanguage.ENGLISH to "3D",
            AppLanguage.URDU to "3D",
            AppLanguage.ARABIC to "3D",
            AppLanguage.HINDI to "3D"
        ),
        "mosque_mode_2d" to mapOf(
            AppLanguage.ENGLISH to "2D",
            AppLanguage.URDU to "2D",
            AppLanguage.ARABIC to "2D",
            AppLanguage.HINDI to "2D"
        ),
        "mosque_toggle_3d_cd" to mapOf(
            AppLanguage.ENGLISH to "Toggle 2D / 3D Buildings",
            AppLanguage.URDU to "2D / 3D عمارتیں تبدیل کریں",
            AppLanguage.ARABIC to "تبديل المباني ثنائية / ثلاثية الأبعاد",
            AppLanguage.HINDI to "2D / 3D इमारतें बदलें"
        ),
        "mosque_style_liberty" to mapOf(
            AppLanguage.ENGLISH to "Liberty 3D",
            AppLanguage.URDU to "لبرٹی 3D",
            AppLanguage.ARABIC to "ليبرتي 3D",
            AppLanguage.HINDI to "लिबर्टी 3D"
        ),
        "mosque_style_bright" to mapOf(
            AppLanguage.ENGLISH to "Bright",
            AppLanguage.URDU to "برائٹ",
            AppLanguage.ARABIC to "برايت",
            AppLanguage.HINDI to "ब्राइट"
        ),
        "mosque_switch_style_cd" to mapOf(
            AppLanguage.ENGLISH to "Switch OpenFreeMap style",
            AppLanguage.URDU to "نقشہ اسٹائل تبدیل کریں",
            AppLanguage.ARABIC to "تغيير نمط الخريطة",
            AppLanguage.HINDI to "मानचित्र शैली बदलें"
        ),
        "mosque_tilt_cd" to mapOf(
            AppLanguage.ENGLISH to "Adjust 3D tilt",
            AppLanguage.URDU to "3D جھکاؤ تبدیل کریں",
            AppLanguage.ARABIC to "تعديل الميل ثلاثي الأبعاد",
            AppLanguage.HINDI to "3D झुकाव बदलें"
        ),
        "mosque_reset_north" to mapOf(
            AppLanguage.ENGLISH to "Reset North",
            AppLanguage.URDU to "شمال سیدھا کریں",
            AppLanguage.ARABIC to "إعادة توجيه للشمال",
            AppLanguage.HINDI to "उत्तर दिशा रीसेट करें"
        ),
        "mosque_gps_accuracy_poor" to mapOf(
            AppLanguage.ENGLISH to "Low GPS accuracy (±%dm) — move outdoors or search city manually",
            AppLanguage.URDU to "GPS کی درستگی کم ہے (±%d میٹر) — کھلی جگہ جائیں یا شہر خود تلاش کریں",
            AppLanguage.ARABIC to "دقة GPS منخفضة (±%dم) — انتقل للخارج أو ابحث يدوياً",
            AppLanguage.HINDI to "GPS सटीकता कम है (±%d मी.) — बाहर जाएँ या शहर खोजें"
        ),
        "mosque_gps_timeout" to mapOf(
            AppLanguage.ENGLISH to "GPS fix timed out. Using saved location or search any city manually.",
            AppLanguage.URDU to "GPS سگنل کا وقت ختم ہو گیا۔ محفوظ لوکیشن یا دستی تلاش استعمال کریں۔",
            AppLanguage.ARABIC to "انتهت مهلة GPS. استخدم الموقع المحفوظ أو ابحث يدوياً.",
            AppLanguage.HINDI to "GPS समय समाप्त। सहेजी गई लोकेशन या मैन्युअल खोज का उपयोग करें।"
        ),
        "mosque_route_section_title" to mapOf(
            AppLanguage.ENGLISH to "Road Route (OSRM)",
            AppLanguage.URDU to "سڑک کا راستہ (OSRM)",
            AppLanguage.ARABIC to "مسار الطريق (OSRM)",
            AppLanguage.HINDI to "सड़क मार्ग (OSRM)"
        ),
        "mosque_route_profile_driving" to mapOf(
            AppLanguage.ENGLISH to "Drive",
            AppLanguage.URDU to "ڈرائیو",
            AppLanguage.ARABIC to "سيارة",
            AppLanguage.HINDI to "ड्राइव"
        ),
        "mosque_route_profile_walking" to mapOf(
            AppLanguage.ENGLISH to "Walk",
            AppLanguage.URDU to "پیدل",
            AppLanguage.ARABIC to "مشي",
            AppLanguage.HINDI to "पैदल"
        ),
        "mosque_route_profile_cycling" to mapOf(
            AppLanguage.ENGLISH to "Cycle",
            AppLanguage.URDU to "سائیکل",
            AppLanguage.ARABIC to "دراجة",
            AppLanguage.HINDI to "साइकिल"
        ),
        "mosque_route_show_on_map" to mapOf(
            AppLanguage.ENGLISH to "Show Route on Map",
            AppLanguage.URDU to "نقشے پر راستہ دکھائیں",
            AppLanguage.ARABIC to "عرض المسار على الخريطة",
            AppLanguage.HINDI to "मानचित्र पर मार्ग दिखाएँ"
        ),
        "mosque_route_clear" to mapOf(
            AppLanguage.ENGLISH to "Clear Route",
            AppLanguage.URDU to "راستہ ہٹائیں",
            AppLanguage.ARABIC to "مسح المسار",
            AppLanguage.HINDI to "मार्ग हटाएँ"
        ),
        "mosque_route_loading" to mapOf(
            AppLanguage.ENGLISH to "Fetching road route from OSRM...",
            AppLanguage.URDU to "OSRM سے سڑک کا راستہ حاصل کیا جا رہا ہے...",
            AppLanguage.ARABIC to "جارٍ جلب مسار الطريق من OSRM...",
            AppLanguage.HINDI to "OSRM से सड़क मार्ग प्राप्त किया जा रहा है..."
        ),
        "mosque_route_duration_unavailable" to mapOf(
            AppLanguage.ENGLISH to "Travel time not provided by server",
            AppLanguage.URDU to "سفر کا وقت سرور سے دستیاب نہیں ہوا",
            AppLanguage.ARABIC to "وقت السفر غير متوفر من الخادم",
            AppLanguage.HINDI to "यात्रा समय सर्वर द्वारा उपलब्ध नहीं कराया गया"
        ),
        "mosque_route_demo_notice" to mapOf(
            AppLanguage.ENGLISH to "Public OSRM demo servers have shared rate limits and no uptime guarantee. Open external Maps app for live voice navigation.",
            AppLanguage.URDU to "عوامی OSRM ڈیمو سرورز کی رفتار محدود ہوتی ہے۔ لائیو آواز والی رہنمائی کے لیے بیرونی میپس ایپ کھولیں۔",
            AppLanguage.ARABIC to "خوادم OSRM التجريبية العامة محدودة الاستخدام. افتح تطبيق الخرائط الخارجي للملاحة الصوتية.",
            AppLanguage.HINDI to "सार्वजनिक OSRM डेमो सर्वर की सीमाएँ हैं। लाइव नेविगेशन के लिए बाहरी मैप्स ऐप खोलें।"
        ),
        "mosque_route_origin_needed" to mapOf(
            AppLanguage.ENGLISH to "Set your location (GPS or manual search) before calculating a road route.",
            AppLanguage.URDU to "سڑک کا راستہ دیکھنے کے لیے پہلے اپنی لوکیشن (GPS یا دستی تلاش) منتخب کریں۔",
            AppLanguage.ARABIC to "حدد موقعك (GPS أو البحث اليدوي) أولاً لحساب مسار الطريق.",
            AppLanguage.HINDI to "सड़क मार्ग जानने के लिए पहले अपनी लोकेशन (GPS या मैन्युअल खोज) चुनें।"
        ),
        "mosque_focus_on_map" to mapOf(
            AppLanguage.ENGLISH to "Focus on Map",
            AppLanguage.URDU to "نقشے پر دیکھیں",
            AppLanguage.ARABIC to "عرض على الخريطة",
            AppLanguage.HINDI to "मानचित्र पर देखें"
        ),
        "mosque_filter_name_short" to mapOf(
            AppLanguage.ENGLISH to "Filter loaded mosques by name...",
            AppLanguage.URDU to "لوڈ شدہ مساجد نام سے تلاش کریں...",
            AppLanguage.ARABIC to "تصفية المساجد بالاسم...",
            AppLanguage.HINDI to "मिली मस्जिदें नाम से फ़िल्टर करें..."
        ),
        "mosque_style_hybrid" to mapOf(
            AppLanguage.ENGLISH to "3D Satellite",
            AppLanguage.URDU to "3D سیٹلائٹ",
            AppLanguage.ARABIC to "قمر صناعي 3D",
            AppLanguage.HINDI to "3D सैटेलाइट"
        ),
        "mosque_style_satellite_hd" to mapOf(
            AppLanguage.ENGLISH to "HD Satellite",
            AppLanguage.URDU to "ایچ ڈی سیٹلائٹ",
            AppLanguage.ARABIC to "قمر صناعي HD",
            AppLanguage.HINDI to "HD सैटेलाइट"
        ),
        "mosque_street_view_3d" to mapOf(
            AppLanguage.ENGLISH to "Street View / 3D Maps",
            AppLanguage.URDU to "اسٹریٹ ویو / 3D میپس",
            AppLanguage.ARABIC to "التجول الافتراضي / خرائط 3D",
            AppLanguage.HINDI to "स्ट्रीट व्यू / 3D मैप्स"
        ),
        "mosque_filters_label" to mapOf(
            AppLanguage.ENGLISH to "Filters",
            AppLanguage.URDU to "فلٹرز",
            AppLanguage.ARABIC to "تصفية",
            AppLanguage.HINDI to "फ़िल्टर"
        ),
        "mosque_navigate_btn" to mapOf(
            AppLanguage.ENGLISH to "Navigate",
            AppLanguage.URDU to "راستہ دکھائیں",
            AppLanguage.ARABIC to "انطلاق",
            AppLanguage.HINDI to "रास्ता दिखाएँ"
        ),
        // ---- Phase 2: Seerah of Prophet Muhammad ﷺ ----
        "seerah" to mapOf(
            AppLanguage.ENGLISH to "Seerah of Prophet Muhammad ﷺ",
            AppLanguage.URDU to "سیرتِ نبی محمد ﷺ",
            AppLanguage.ARABIC to "السيرة النبوية ﷺ",
            AppLanguage.HINDI to "सीरत-ए-नबी मुहम्मद ﷺ"
        ),
        "seerah_hierarchy_title" to mapOf(
            AppLanguage.ENGLISH to "Strict 3-Tier Historical Source Methodology",
            AppLanguage.URDU to "تاریخی مآخذ کا سہ درجہ مستند معیار",
            AppLanguage.ARABIC to "منهجية التوثيق التاريخي من ٣ مستويات",
            AppLanguage.HINDI to "त्रि-स्तरीय प्रामाणिक ऐतिहासिक स्रोत प्रणाली"
        ),
        "seerah_progress_label" to mapOf(
            AppLanguage.ENGLISH to "Seerah Reading Progress",
            AppLanguage.URDU to "مطالعہ سیرت کی پیش رفت",
            AppLanguage.ARABIC to "تقدم قراءة السيرة",
            AppLanguage.HINDI to "सीरत पठन प्रगति"
        ),
        "seerah_resume_last" to mapOf(
            AppLanguage.ENGLISH to "Resume Last Read",
            AppLanguage.URDU to "گزشتہ مطالعہ جاری رکھیں",
            AppLanguage.ARABIC to "استئناف القراءة",
            AppLanguage.HINDI to "पिछला पाठ जारी रखें"
        ),
        "seerah_search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search title, keyword, Quran (96:1) or Hadith ref (Bukhari)...",
            AppLanguage.URDU to "عنوان، لفظ، قرآنی آیت (96:1) یا حدیث کا حوالہ تلاش کریں...",
            AppLanguage.ARABIC to "ابحث بالعنوان أو الكلمة أو الآية (96:1) أو الحديث...",
            AppLanguage.HINDI to "शीर्षक, शब्द, क़ुरआन (96:1) या हदीस संदर्भ खोजें..."
        ),
        "seerah_all_eras" to mapOf(
            AppLanguage.ENGLISH to "All 9 Eras",
            AppLanguage.URDU to "تمام 9 ادوار",
            AppLanguage.ARABIC to "جميع المراحل التسع",
            AppLanguage.HINDI to "सभी 9 कालखंड"
        ),
        "seerah_bookmarks_filter" to mapOf(
            AppLanguage.ENGLISH to "Bookmarked",
            AppLanguage.URDU to "بُک مارکس",
            AppLanguage.ARABIC to "المحفوظات",
            AppLanguage.HINDI to "बुकमार्क"
        ),
        "seerah_bookmark_action" to mapOf(
            AppLanguage.ENGLISH to "Toggle Bookmark",
            AppLanguage.URDU to "بک مارک محفوظ کریں",
            AppLanguage.ARABIC to "حفظ في المفضلة",
            AppLanguage.HINDI to "बुकमार्क सहेजें"
        ),
        "seerah_mark_read" to mapOf(
            AppLanguage.ENGLISH to "Toggle Read Status",
            AppLanguage.URDU to "پڑھا ہوا نشان زد کریں",
            AppLanguage.ARABIC to "تحديد كمقروء",
            AppLanguage.HINDI to "पठित के रूप में चिह्नित करें"
        ),
        "seerah_share_title" to mapOf(
            AppLanguage.ENGLISH to "Share Seerah Article",
            AppLanguage.URDU to "مضمون شیئر کریں",
            AppLanguage.ARABIC to "مشاركة المقال",
            AppLanguage.HINDI to "लेख साझा करें"
        ),
        "seerah_prev_article" to mapOf(
            AppLanguage.ENGLISH to "Previous",
            AppLanguage.URDU to "گزشتہ",
            AppLanguage.ARABIC to "السابق",
            AppLanguage.HINDI to "पिछला"
        ),
        "seerah_next_article" to mapOf(
            AppLanguage.ENGLISH to "Next",
            AppLanguage.URDU to "اگلا",
            AppLanguage.ARABIC to "التالي",
            AppLanguage.HINDI to "अगला"
        ),
        "seerah_empty_title" to mapOf(
            AppLanguage.ENGLISH to "No matching Seerah topics found",
            AppLanguage.URDU to "سیرت کا کوئی متعلقہ باب نہیں ملا",
            AppLanguage.ARABIC to "لا توجد مواضيع مطابقة",
            AppLanguage.HINDI to "कोई संबंधित सीरत लेख नहीं मिला"
        ),
        "seerah_empty_desc" to mapOf(
            AppLanguage.ENGLISH to "Try clearing your search or selecting 'All 9 Eras'.",
            AppLanguage.URDU to "تلاش صاف کریں یا 'تمام 9 ادوار' منتخب کریں۔",
            AppLanguage.ARABIC to "جرب مسح البحث أو اختيار جميع المراحل.",
            AppLanguage.HINDI to "खोज साफ़ करें या 'सभी 9 कालखंड' चुनें।"
        ),
        "seerah_sec_chronology" to mapOf(
            AppLanguage.ENGLISH to "Approximate Chronology & Date Note",
            AppLanguage.URDU to "تاریخی زمانہ و سن (تقریبی وضاحت)",
            AppLanguage.ARABIC to "التسلسل الزمني والتاريخ التقريبي",
            AppLanguage.HINDI to "अनुमानित कालक्रम एवं तिथि टिप्पणी"
        ),
        "seerah_sec_overview" to mapOf(
            AppLanguage.ENGLISH to "Verified Historical Overview",
            AppLanguage.URDU to "مستند تاریخی خلاصہ",
            AppLanguage.ARABIC to "الملخص التاريخي الموثق",
            AppLanguage.HINDI to "प्रामाणिक ऐतिहासिक विवरण"
        ),
        "seerah_sec_context" to mapOf(
            AppLanguage.ENGLISH to "Key Context, People & Places",
            AppLanguage.URDU to "اہم شخصیات، مقامات اور پس منظر",
            AppLanguage.ARABIC to "السياق والشخصيات والأماكن",
            AppLanguage.HINDI to "प्रमुख संदर्भ, व्यक्ति और स्थान"
        ),
        "seerah_sec_tier1" to mapOf(
            AppLanguage.ENGLISH to "Tier 1: Quran & Sahih Hadith Citations",
            AppLanguage.URDU to "درجہ اول: قرآن اور صحیح احادیث کے حوالے",
            AppLanguage.ARABIC to "المستوى ١: نصوص القرآن والصحيحين",
            AppLanguage.HINDI to "स्तर 1: क़ुरआन और सहीह हदीस उद्धरण"
        ),
        "seerah_sec_tier2_3" to mapOf(
            AppLanguage.ENGLISH to "Tier 2 & 3: Historical Seerah Chronicles",
            AppLanguage.URDU to "درجہ دوم و سوم: کتبِ سیرت و تاریخ",
            AppLanguage.ARABIC to "المستوى ٢ و٣: كتب السيرة والتاريخ",
            AppLanguage.HINDI to "स्तर 2 और 3: प्राचीन सीरत व ऐतिहासिक विवरण"
        ),
        "seerah_tier1_sufficient_note" to mapOf(
            AppLanguage.ENGLISH to "This milestone is grounded directly in Tier 1 Quran and Sahih Hadith narrations above.",
            AppLanguage.URDU to "یہ واقعہ براہِ راست اوپر بیان کردہ درجہ اول (قرآن و صحیح حدیث) سے ثابت ہے۔",
            AppLanguage.ARABIC to "هذا الحدث موثق مباشرةً بنصوص القرآن والصحيحين أعلاه.",
            AppLanguage.HINDI to "यह प्रसंग सीधे ऊपर दिए गए स्तर 1 क़ुरआन और सहीह हदीस प्रमाणों से सिद्ध है।"
        ),
        "seerah_sec_differences" to mapOf(
            AppLanguage.ENGLISH to "Differences of Historical Reports",
            AppLanguage.URDU to "تاریخی روایات کا اختلاف و وضاحت",
            AppLanguage.ARABIC to "اختلاف الروايات التاريخية والتدقيق",
            AppLanguage.HINDI to "ऐतिहासिक विवरणों में अंतर"
        ),
        "seerah_sec_lessons" to mapOf(
            AppLanguage.ENGLISH to "Spiritual & Practical Lessons Today",
            AppLanguage.URDU to "عصرِ حاضر کے لیے روحانی و عملی اسباق",
            AppLanguage.ARABIC to "الدروس الروحية والعملية المعاصرة",
            AppLanguage.HINDI to "आज के लिए आध्यात्मिक व व्यावहारिक शिक्षाएँ"
        ),
        "seerah_sec_next_milestone" to mapOf(
            AppLanguage.ENGLISH to "Next Chronological Milestone",
            AppLanguage.URDU to "اگلا تاریخی سنگِ میل",
            AppLanguage.ARABIC to "المحطة التالية في السيرة",
            AppLanguage.HINDI to "अगला कालानुक्रमिक पड़ाव"
        ),
        "seerah_sec_verification" to mapOf(
            AppLanguage.ENGLISH to "Primary Source Attribution & Verification Links",
            AppLanguage.URDU to "بنیادی مآخذ اور آن لائن تصدیق کے روابط",
            AppLanguage.ARABIC to "توثيق المصادر وروابط التحقق",
            AppLanguage.HINDI to "मूल स्रोत और सत्यापन लिंक"
        ),
        // ---- Phase 3: Missed Salah Tracker & Stories of Prophets ----
        "missed_salah_tracker" to mapOf(
            AppLanguage.ENGLISH to "Missed Salah Tracker",
            AppLanguage.URDU to "قضا نماز ٹریکر",
            AppLanguage.ARABIC to "متابعة الصلوات الفائتة",
            AppLanguage.HINDI to "छूटी नमाज़ ट्रैकर"
        ),
        "stories_of_prophets" to mapOf(
            AppLanguage.ENGLISH to "Stories of Prophets",
            AppLanguage.URDU to "قصص الانبیاء علیہم السلام",
            AppLanguage.ARABIC to "قصص الأنبياء عليهم السلام",
            AppLanguage.HINDI to "नबियों की कथाएँ"
        ),
        // ---- Mosque Map Controls ----
        "mosque_zoom_in" to mapOf(
            AppLanguage.ENGLISH to "Zoom in",
            AppLanguage.URDU to "بڑا کریں",
            AppLanguage.ARABIC to "تكبير",
            AppLanguage.HINDI to "ज़ूम इन"
        ),
        "mosque_zoom_out" to mapOf(
            AppLanguage.ENGLISH to "Zoom out",
            AppLanguage.URDU to "چھوٹا کریں",
            AppLanguage.ARABIC to "تصغير",
            AppLanguage.HINDI to "ज़ूम आउट"
        ),
        "mosque_recenter" to mapOf(
            AppLanguage.ENGLISH to "Recenter",
            AppLanguage.URDU to "مرکز پر لائیں",
            AppLanguage.ARABIC to "إعادة التمركز",
            AppLanguage.HINDI to "पुनः केंद्रित करें"
        ),
        // ---- Common Controls & Navigation Accessibility ----
        "close" to mapOf(
            AppLanguage.ENGLISH to "Close",
            AppLanguage.URDU to "بند کریں",
            AppLanguage.ARABIC to "إغلاق",
            AppLanguage.HINDI to "बंद करें"
        ),
        "done" to mapOf(
            AppLanguage.ENGLISH to "Done",
            AppLanguage.URDU to "مکمل",
            AppLanguage.ARABIC to "تم",
            AppLanguage.HINDI to "पूर्ण"
        ),
        "back_cd" to mapOf(
            AppLanguage.ENGLISH to "Back",
            AppLanguage.URDU to "واپس",
            AppLanguage.ARABIC to "رجوع",
            AppLanguage.HINDI to "वापस"
        ),
        "settings_cd" to mapOf(
            AppLanguage.ENGLISH to "Settings",
            AppLanguage.URDU to "ترتیبات",
            AppLanguage.ARABIC to "الإعدادات",
            AppLanguage.HINDI to "सेटिंग्स"
        ),
        "clear_search" to mapOf(
            AppLanguage.ENGLISH to "Clear search",
            AppLanguage.URDU to "تلاش صاف کریں",
            AppLanguage.ARABIC to "مسح البحث",
            AppLanguage.HINDI to "खोज साफ़ करें"
        ),
        // ---- Quran Reader & Study Context Sections ----
        "quran_sec_1_arabic" to mapOf(
            AppLanguage.ENGLISH to "1. Arabic Quranic Ayah",
            AppLanguage.URDU to "1. عربی قرآنی آیت",
            AppLanguage.ARABIC to "1. الآية القرآنية الكريمة",
            AppLanguage.HINDI to "1. अरबी क़ुरआन आयत"
        ),
        "quran_sec_2_translation" to mapOf(
            AppLanguage.ENGLISH to "2. Authentic Published Translation (English)",
            AppLanguage.URDU to "2. مستند مطبوعہ ترجمہ (اردو)",
            AppLanguage.ARABIC to "2. التفسير الميسر والبيان (العربية)",
            AppLanguage.HINDI to "2. प्रामाणिक प्रकाशित अनुवाद (हिन्दी)"
        ),
        "quran_sec_3_why" to mapOf(
            AppLanguage.ENGLISH to "3. WHY WAS THIS AYAH REVEALED?",
            AppLanguage.URDU to "3. شانِ نزول (آیت کیوں نازل ہوئی؟)",
            AppLanguage.ARABIC to "3. أسباب النزول (لماذا نزلت الآية؟)",
            AppLanguage.HINDI to "3. शाने-नुज़ूल (यह आयत क्यों नाज़िल हुई?)"
        ),
        "quran_sec_4_when" to mapOf(
            AppLanguage.ENGLISH to "4. WHEN WAS THIS AYAH REVEALED?",
            AppLanguage.URDU to "4. زمانۂ نزول (آیت کب نازل ہوئی؟)",
            AppLanguage.ARABIC to "4. وقت النزول والسياق التاريخي",
            AppLanguage.HINDI to "4. अवतरण का समय (आयत कब नाज़िल हुई?)"
        ),
        "quran_sec_5_wisdom" to mapOf(
            AppLanguage.ENGLISH to "5. HIDDEN WISDOM AND DEEP MEANING",
            AppLanguage.URDU to "5. حکمت اور گہرے مفاہیم",
            AppLanguage.ARABIC to "5. الحكمة والمقاصد القرآنية",
            AppLanguage.HINDI to "5. छिपी हिकमत और गहरा अर्थ"
        ),
        "quran_sec_6_lessons" to mapOf(
            AppLanguage.ENGLISH to "6. BENEFITS AND PRACTICAL LESSONS",
            AppLanguage.URDU to "6. فوائد اور عملی اسباق",
            AppLanguage.ARABIC to "6. الفوائد والدروس العملية",
            AppLanguage.HINDI to "6. लाभ और व्यावहारिक शिक्षाएँ"
        ),
        "quran_sec_7_refs" to mapOf(
            AppLanguage.ENGLISH to "7. AUTHENTIC REFERENCES",
            AppLanguage.URDU to "7. مستند علمی حوالہ جات",
            AppLanguage.ARABIC to "7. المصادر والمراجع المعتمدة",
            AppLanguage.HINDI to "7. प्रामाणिक संदर्भ"
        ),
        "quran_prev_surah" to mapOf(
            AppLanguage.ENGLISH to "← Previous Surah",
            AppLanguage.URDU to "پچھلی سورت →",
            AppLanguage.ARABIC to "السورة السابقة →",
            AppLanguage.HINDI to "← पिछली सूरह"
        ),
        "quran_next_surah" to mapOf(
            AppLanguage.ENGLISH to "Next Surah →",
            AppLanguage.URDU to "← اگلی سورت",
            AppLanguage.ARABIC to "← السورة التالية",
            AppLanguage.HINDI to "अगली सूरह →"
        ),
        "quran_loading_context" to mapOf(
            AppLanguage.ENGLISH to "Loading verified study context...",
            AppLanguage.URDU to "مستند علمی پس منظر لوڈ ہو رہا ہے...",
            AppLanguage.ARABIC to "جارٍ تحميل السياق العلمي الموثق...",
            AppLanguage.HINDI to "प्रामाणिक अध्ययन संदर्भ लोड हो रहा है..."
        ),
        "quran_loading_commentary" to mapOf(
            AppLanguage.ENGLISH to "Loading commentary...",
            AppLanguage.URDU to "تفسیر لوڈ ہو رہی ہے...",
            AppLanguage.ARABIC to "جارٍ تحميل التفسير...",
            AppLanguage.HINDI to "तफ़सीर लोड हो रही है..."
        ),
        "quran_reference_only_edition" to mapOf(
            AppLanguage.ENGLISH to "Reference-Only Edition (Full Commentary Text Not Bundled / Unavailable in Selected Language)",
            AppLanguage.URDU to "صرف حوالہ جاتی اشاعت (مکمل متن منتخب اردو زبان میں ایپ کے اندر موجود نہیں)",
            AppLanguage.ARABIC to "طبعة مرجعية فقط (النص الكامل غير مضمّن أو غير متوفر بالعربية هنا)",
            AppLanguage.HINDI to "केवल संदर्भ संस्करण (पूर्ण व्याख्या चयनित हिन्दी भाषा में उपलब्ध नहीं)"
        ),
        "quran_open_official_source" to mapOf(
            AppLanguage.ENGLISH to "Open Official Source",
            AppLanguage.URDU to "مستند ویب ماخذ کھولیں",
            AppLanguage.ARABIC to "فتح المصدر الرسمي",
            AppLanguage.HINDI to "आधिकारिक स्रोत खोलें"
        ),
        "quran_buffering" to mapOf(
            AppLanguage.ENGLISH to "Buffering recitation...",
            AppLanguage.URDU to "تلاوت بفر ہو رہی ہے...",
            AppLanguage.ARABIC to "جارٍ تحميل التلاوة...",
            AppLanguage.HINDI to "तिलावत बफ़र हो रही है..."
        ),
        "quran_playing" to mapOf(
            AppLanguage.ENGLISH to "Playing",
            AppLanguage.URDU to "تلاوت جاری ہے",
            AppLanguage.ARABIC to "جارٍ التشغيل",
            AppLanguage.HINDI to "चल रहा है"
        ),
        "quran_tap_to_play" to mapOf(
            AppLanguage.ENGLISH to "Tap to Play",
            AppLanguage.URDU to "چلانے کے لیے دبائیں",
            AppLanguage.ARABIC to "اضغط للتشغيل",
            AppLanguage.HINDI to "चलाने के लिए टैप करें"
        ),
        "quran_download_cancelled" to mapOf(
            AppLanguage.ENGLISH to "Download cancelled",
            AppLanguage.URDU to "ڈاؤن لوڈ منسوخ کر دیا گیا",
            AppLanguage.ARABIC to "تم إلغاء التنزيل",
            AppLanguage.HINDI to "डाउनलोड रद्द किया गया"
        ),
        // ---- Hadith Library Extras ----
        "hadith_bundled_core" to mapOf(
            AppLanguage.ENGLISH to "Bundled Core",
            AppLanguage.URDU to "شامل بنیادی ذخیرہ",
            AppLanguage.ARABIC to "المجموعة الأساسية المضمنة",
            AppLanguage.HINDI to "संग्रहित मुख्य भाग"
        ),
        "hadith_all_books" to mapOf(
            AppLanguage.ENGLISH to "All Books",
            AppLanguage.URDU to "تمام کتب",
            AppLanguage.ARABIC to "جميع الكتب",
            AppLanguage.HINDI to "सभी पुस्तकें"
        ),
        "hadith_downloading_pack" to mapOf(
            AppLanguage.ENGLISH to "Downloading verified offline pack...",
            AppLanguage.URDU to "مستند آف لائن ذخیرہ ڈاؤن لوڈ ہو رہا ہے...",
            AppLanguage.ARABIC to "جارٍ تنزيل الحزمة الموثوقة دون إنترنت...",
            AppLanguage.HINDI to "प्रामाणिक ऑफ़लाइन संग्रह डाउनलोड हो रहा है..."
        ),
        "hadith_sections_57" to mapOf(
            AppLanguage.ENGLISH to "57 Independent Sections",
            AppLanguage.URDU to "57 مستقل ابواب",
            AppLanguage.ARABIC to "57 كتاباً وباباً مستقلاً",
            AppLanguage.HINDI to "57 स्वतंत्र अध्याय"
        ),
        "hadith_edition_manifest" to mapOf(
            AppLanguage.ENGLISH to "Verified Edition Provenance",
            AppLanguage.URDU to "مستند اشاعت و مآخذ کی تفصیل",
            AppLanguage.ARABIC to "توثيق الطبعات والمصادر المعتمدة",
            AppLanguage.HINDI to "प्रामाणिक संस्करण व स्रोत विवरण"
        ),
        "hadith_copied" to mapOf(
            AppLanguage.ENGLISH to "Hadith copied to clipboard",
            AppLanguage.URDU to "حدیث کلپ بورڈ پر کاپی ہو گئی",
            AppLanguage.ARABIC to "تم نسخ الحديث الشريف",
            AppLanguage.HINDI to "हदीस क्लिपबोर्ड पर कॉपी हो गई"
        ),
        // ---- Daily Duas Extras ----
        "dua_transliteration_label" to mapOf(
            AppLanguage.ENGLISH to "Transliteration:",
            AppLanguage.URDU to "تلفظ (رومن):",
            AppLanguage.ARABIC to "النطق الصوتي:",
            AppLanguage.HINDI to "उच्चारण (लिप्यंतरण):"
        ),
        "dua_translation_unavailable" to mapOf(
            AppLanguage.ENGLISH to "Verified translation in this language is not available in the bundled source.",
            AppLanguage.URDU to "اس دعا کا مستند اردو ترجمہ اس اشاعت میں دستیاب نہیں ہے۔",
            AppLanguage.ARABIC to "الدعاء الأصلي بالعربية معروض أعلاه.",
            AppLanguage.HINDI to "इस दुआ का प्रामाणिक हिन्दी अनुवाद उपलब्ध स्रोत में नहीं है।"
        ),
        // ---- Quran Learning Academy Extras ----
        "learning_clear_canvas" to mapOf(
            AppLanguage.ENGLISH to "Clear Canvas",
            AppLanguage.URDU to "تختی صاف کریں",
            AppLanguage.ARABIC to "مسح اللوح",
            AppLanguage.HINDI to "कैनवास साफ़ करें"
        ),
        "learning_play_surah" to mapOf(
            AppLanguage.ENGLISH to "Play Surah",
            AppLanguage.URDU to "سورت کی تلاوت سنیں",
            AppLanguage.ARABIC to "استماع للسورة",
            AppLanguage.HINDI to "सूरह सुनें"
        ),
        "learning_stage_prefix" to mapOf(
            AppLanguage.ENGLISH to "Stage",
            AppLanguage.URDU to "مرحلہ",
            AppLanguage.ARABIC to "المرحلة",
            AppLanguage.HINDI to "चरण"
        ),
        "learning_ayahs_label" to mapOf(
            AppLanguage.ENGLISH to "Ayahs",
            AppLanguage.URDU to "آیات",
            AppLanguage.ARABIC to "آيات",
            AppLanguage.HINDI to "आयतें"
        ),
        "learning_revisions_label" to mapOf(
            AppLanguage.ENGLISH to "Revisions",
            AppLanguage.URDU to "دہرائی",
            AppLanguage.ARABIC to "المراجعة",
            AppLanguage.HINDI to "दोहराव"
        ),
        // ---- Location Labels ----
        "city_location_not_set" to mapOf(
            AppLanguage.ENGLISH to "Location Not Set",
            AppLanguage.URDU to "مقام منتخب نہیں",
            AppLanguage.ARABIC to "لم يتم تحديد الموقع",
            AppLanguage.HINDI to "स्थान चयनित नहीं"
        ),
        "city_detected_location" to mapOf(
            AppLanguage.ENGLISH to "Detected Location",
            AppLanguage.URDU to "موجودہ مقام",
            AppLanguage.ARABIC to "الموقع المكتشف",
            AppLanguage.HINDI to "पता लगाया गया स्थान"
        ),
        // ---- Background Notification Templates ----
        "notif_azan_title_suffix" to mapOf(
            AppLanguage.ENGLISH to "Prayer Time",
            AppLanguage.URDU to "نماز کا وقت",
            AppLanguage.ARABIC to "حان وقت الصلاة",
            AppLanguage.HINDI to "नमाज़ का समय"
        ),
        "notif_azan_stop" to mapOf(
            AppLanguage.ENGLISH to "Stop Azan",
            AppLanguage.URDU to "اذان بند کریں",
            AppLanguage.ARABIC to "إيقاف الأذان",
            AppLanguage.HINDI to "अज़ान रोकें"
        ),
        "notif_daily_hadith_title" to mapOf(
            AppLanguage.ENGLISH to "Daily Hadith Reminder",
            AppLanguage.URDU to "آج کی حدیث مبارکہ",
            AppLanguage.ARABIC to "حديث اليوم النبوي",
            AppLanguage.HINDI to "दैनिक हदीस स्मरण"
        ),
        "notif_daily_dua_title" to mapOf(
            AppLanguage.ENGLISH to "Daily Masnoon Dua & Adhkar",
            AppLanguage.URDU to "مسنون دعا و اذکار",
            AppLanguage.ARABIC to "الدعاء اليومي والأذكار",
            AppLanguage.HINDI to "दैनिक मसनून दुआ व अज़कार"
        ),
        "notif_ramadan_sehri_pre" to mapOf(
            AppLanguage.ENGLISH to "Sehri Ends Soon",
            AppLanguage.URDU to "سحری ختم ہونے والی ہے",
            AppLanguage.ARABIC to "اقتراب وقت الإمساك",
            AppLanguage.HINDI to "सहरी समाप्त होने वाली है"
        ),
        "notif_ramadan_sehri_end" to mapOf(
            AppLanguage.ENGLISH to "Sehri Ended / Fajr Begins",
            AppLanguage.URDU to "وقتِ سحر ختم — فجر شروع",
            AppLanguage.ARABIC to "انتهى السحور — حان وقت الفجر",
            AppLanguage.HINDI to "सहरी समाप्त — फ़ज्र शुरू"
        ),
        "notif_ramadan_iftar_pre" to mapOf(
            AppLanguage.ENGLISH to "Iftar Approaching",
            AppLanguage.URDU to "افطار قریب ہے",
            AppLanguage.ARABIC to "اقتراب موعد الإفطار",
            AppLanguage.HINDI to "इफ़्तार करीब है"
        ),
        "notif_ramadan_iftar_main" to mapOf(
            AppLanguage.ENGLISH to "Iftar Time / Maghrib",
            AppLanguage.URDU to "افطار کا وقت ہو گیا (مغرب)",
            AppLanguage.ARABIC to "حان وقت الإفطار (المغرب)",
            AppLanguage.HINDI to "इफ़्तार का समय (मग़रिब)"
        ),
        // ---- Additional Shared & Feature UI Strings ----
        "ok" to mapOf(
            AppLanguage.ENGLISH to "OK",
            AppLanguage.URDU to "ٹھیک ہے",
            AppLanguage.ARABIC to "حسناً",
            AppLanguage.HINDI to "ठीक है"
        ),
        "all_label" to mapOf(
            AppLanguage.ENGLISH to "All",
            AppLanguage.URDU to "سب",
            AppLanguage.ARABIC to "الكل",
            AppLanguage.HINDI to "सभी"
        ),
        "play" to mapOf(
            AppLanguage.ENGLISH to "Play Recitation",
            AppLanguage.URDU to "تلاوت چلائیں",
            AppLanguage.ARABIC to "تشغيل التلاوة",
            AppLanguage.HINDI to "तिलावत सुनें"
        ),
        "pause" to mapOf(
            AppLanguage.ENGLISH to "Pause",
            AppLanguage.URDU to "روکیں",
            AppLanguage.ARABIC to "إيقاف مؤقت",
            AppLanguage.HINDI to "रोकें"
        ),
        "size_small" to mapOf(
            AppLanguage.ENGLISH to "Small",
            AppLanguage.URDU to "چھوٹا",
            AppLanguage.ARABIC to "صغير",
            AppLanguage.HINDI to "छोटा"
        ),
        "size_medium" to mapOf(
            AppLanguage.ENGLISH to "Medium",
            AppLanguage.URDU to "درمیانہ",
            AppLanguage.ARABIC to "متوسط",
            AppLanguage.HINDI to "मध्यम"
        ),
        "size_large" to mapOf(
            AppLanguage.ENGLISH to "Large",
            AppLanguage.URDU to "بڑا",
            AppLanguage.ARABIC to "كبير",
            AppLanguage.HINDI to "बड़ा"
        ),
        "size_extra" to mapOf(
            AppLanguage.ENGLISH to "Extra",
            AppLanguage.URDU to "نہایت بڑا",
            AppLanguage.ARABIC to "كبير جداً",
            AppLanguage.HINDI to "बहुत बड़ा"
        ),
        "hadith_book_prefix" to mapOf(
            AppLanguage.ENGLISH to "Book",
            AppLanguage.URDU to "کتاب",
            AppLanguage.ARABIC to "كتاب",
            AppLanguage.HINDI to "किताब"
        ),
        "hadith_vol_prefix" to mapOf(
            AppLanguage.ENGLISH to "Vol",
            AppLanguage.URDU to "جلد",
            AppLanguage.ARABIC to "مجلد",
            AppLanguage.HINDI to "जिल्द"
        ),
        "hadith_offline_narrations" to mapOf(
            AppLanguage.ENGLISH to "Offline narrations loaded",
            AppLanguage.URDU to "آف لائن موجود احادیث",
            AppLanguage.ARABIC to "الأحاديث المحمّلة دون إنترنت",
            AppLanguage.HINDI to "ऑफ़लाइन उपलब्ध हदीसें"
        ),
        "hadith_curated_count" to mapOf(
            AppLanguage.ENGLISH to "curated offline",
            AppLanguage.URDU to "منتخب احادیث موجود",
            AppLanguage.ARABIC to "أحاديث منتقاة محلية",
            AppLanguage.HINDI to "संग्रहित ऑफ़लाइन"
        ),
        "hadith_tap_download" to mapOf(
            AppLanguage.ENGLISH to "Tap to download",
            AppLanguage.URDU to "ڈاؤن لوڈ کے لیے دبائیں",
            AppLanguage.ARABIC to "اضغط للتنزيل",
            AppLanguage.HINDI to "डाउनलोड करने के लिए टैप करें"
        ),
        "hadith_search_empty" to mapOf(
            AppLanguage.ENGLISH to "No matching narrations found in currently cached/curated books. Download specific Books to search all narrations offline.",
            AppLanguage.URDU to "موجودہ محفوظ یا بنیادی کتب میں کوئی مماثل حدیث نہیں ملی۔ مکمل تلاش کے لیے متعلقہ کتاب ڈاؤن لوڈ کریں۔",
            AppLanguage.ARABIC to "لم يُعثر على أحاديث مطابقة في الكتب المحفوظة حالياً. يمكنك تنزيل الكتاب للبحث الكامل دون إنترنت.",
            AppLanguage.HINDI to "मौजूदा ऑफ़लाइन पुस्तकों में कोई मिलती हदीस नहीं मिली। पूरी खोज के लिए संबंधित किताब डाउनलोड करें।"
        ),
        "hadith_compiler" to mapOf(
            AppLanguage.ENGLISH to "Compiler",
            AppLanguage.URDU to "مؤلف",
            AppLanguage.ARABIC to "المصنِّف",
            AppLanguage.HINDI to "संकलनकर्ता"
        ),
        "hadith_structure" to mapOf(
            AppLanguage.ENGLISH to "Structure",
            AppLanguage.URDU to "ساخت",
            AppLanguage.ARABIC to "الهيكل",
            AppLanguage.HINDI to "संरचना"
        ),
        "hadith_edition" to mapOf(
            AppLanguage.ENGLISH to "Edition",
            AppLanguage.URDU to "اشاعت",
            AppLanguage.ARABIC to "الطبعة",
            AppLanguage.HINDI to "संस्करण"
        ),
        "hadith_english_translator" to mapOf(
            AppLanguage.ENGLISH to "English Edition",
            AppLanguage.URDU to "انگریزی اشاعت",
            AppLanguage.ARABIC to "النسخة الإنجليزية",
            AppLanguage.HINDI to "अंग्रेज़ी संस्करण"
        ),
        "hadith_urdu_translator" to mapOf(
            AppLanguage.ENGLISH to "Urdu Edition",
            AppLanguage.URDU to "اردو اشاعت",
            AppLanguage.ARABIC to "النسخة الأردية",
            AppLanguage.HINDI to "उर्दू संस्करण"
        ),
        "hadith_hindi_status" to mapOf(
            AppLanguage.ENGLISH to "Hindi Status",
            AppLanguage.URDU to "ہندی زبان کی صورتحال",
            AppLanguage.ARABIC to "حالة اللغة الهندية",
            AppLanguage.HINDI to "हिन्दी उपलब्धता"
        ),
        "hadith_licence" to mapOf(
            AppLanguage.ENGLISH to "Licence",
            AppLanguage.URDU to "اجازت نامہ / لائسنس",
            AppLanguage.ARABIC to "الترخيص",
            AppLanguage.HINDI to "लाइसेंस"
        ),
        "dua_active_translation" to mapOf(
            AppLanguage.ENGLISH to "Translation",
            AppLanguage.URDU to "اردو ترجمہ",
            AppLanguage.ARABIC to "البيان والمقصد",
            AppLanguage.HINDI to "हिन्दी अनुवाद"
        ),
        "dua_reference_label" to mapOf(
            AppLanguage.ENGLISH to "Reference",
            AppLanguage.URDU to "حوالہ",
            AppLanguage.ARABIC to "المرجع",
            AppLanguage.HINDI to "संदर्भ"
        ),
        // ---- Islamic Business & Halal Entrepreneurship Academy ----
        "business_academy" to mapOf(
            AppLanguage.ENGLISH to "Business Academy",
            AppLanguage.URDU to "اسلامی بزنس اکیڈمی",
            AppLanguage.ARABIC to "أكاديمية التجارة الحلال",
            AppLanguage.HINDI to "इस्लामी व्यापार अकादमी"
        ),
        "ba_hero_subtitle" to mapOf(
            AppLanguage.ENGLISH to "Islamic Business & Halal Entrepreneurship Academy",
            AppLanguage.URDU to "اسلامی تجارت و حلال کاروبار کی مکمل اکیڈمی",
            AppLanguage.ARABIC to "أكاديمية التجارة الإسلامية وريادة الأعمال الحلال",
            AppLanguage.HINDI to "इस्लामी व्यापार और हलाल उद्यमिता की संपूर्ण अकादमी"
        ),
        "ba_offline_badge" to mapOf(
            AppLanguage.ENGLISH to "100% OFFLINE • VERIFIED SOURCES",
            AppLanguage.URDU to "مکمل آف لائن • مستند حوالہ جات",
            AppLanguage.ARABIC to "يعمل دون إنترنت • مصادر موثقة",
            AppLanguage.HINDI to "100% ऑफ़लाइन • प्रामाणिक संदर्भ"
        ),
        "ba_search_placeholder" to mapOf(
            AppLanguage.ENGLISH to "Search modules, 14 contracts, 30 sectors...",
            AppLanguage.URDU to "تلاش کریں: اسباق، 14 معاہدے، 30 کاروبار...",
            AppLanguage.ARABIC to "ابحث في الوحدات، العقود، القطاعات...",
            AppLanguage.HINDI to "खोजें: मॉड्यूल, 14 अनुबंध, 30 क्षेत्र..."
        ),
        "ba_no_search_results" to mapOf(
            AppLanguage.ENGLISH to "No matching Business Academy topics found offline.",
            AppLanguage.URDU to "بزنس اکیڈمی میں اس سے متعلق کوئی نتیجہ نہیں ملا۔",
            AppLanguage.ARABIC to "لم يتم العثور على نتائج مطابقة في الأكاديمية.",
            AppLanguage.HINDI to "व्यापार अकादमी में कोई मिलता-जुलता विषय नहीं मिला।"
        ),
        "ba_tab_curriculum" to mapOf(
            AppLanguage.ENGLISH to "Curriculum",
            AppLanguage.URDU to "نصاب",
            AppLanguage.ARABIC to "المنهج",
            AppLanguage.HINDI to "पाठ्यक्रम"
        ),
        "ba_tab_contracts" to mapOf(
            AppLanguage.ENGLISH to "Contracts",
            AppLanguage.URDU to "عقود",
            AppLanguage.ARABIC to "العقود",
            AppLanguage.HINDI to "अनुबंध"
        ),
        "ba_tab_sectors" to mapOf(
            AppLanguage.ENGLISH to "Sectors",
            AppLanguage.URDU to "کاروبار",
            AppLanguage.ARABIC to "القطاعات",
            AppLanguage.HINDI to "व्यापार क्षेत्र"
        ),
        "ba_tab_specialized" to mapOf(
            AppLanguage.ENGLISH to "Special Topics",
            AppLanguage.URDU to "خصوصی مسائل",
            AppLanguage.ARABIC to "قضايا متخصصة",
            AppLanguage.HINDI to "विशेष विषय"
        ),
        "ba_tab_tools" to mapOf(
            AppLanguage.ENGLISH to "Project & Tools",
            AppLanguage.URDU to "پروجیکٹ و آلات",
            AppLanguage.ARABIC to "المشروع والأدوات",
            AppLanguage.HINDI to "प्रोजेक्ट व कैलकुलेटर"
        ),
        "ba_tab_saved" to mapOf(
            AppLanguage.ENGLISH to "Saved & Cert",
            AppLanguage.URDU to "محفوظ و سرٹیفکیٹ",
            AppLanguage.ARABIC to "المحفوظات والشهادة",
            AppLanguage.HINDI to "सहेजा व प्रमाणपत्र"
        ),
        "ba_level_prefix" to mapOf(
            AppLanguage.ENGLISH to "Level",
            AppLanguage.URDU to "لیول",
            AppLanguage.ARABIC to "المستوى",
            AppLanguage.HINDI to "स्तर"
        ),
        "ba_module_prefix" to mapOf(
            AppLanguage.ENGLISH to "Module",
            AppLanguage.URDU to "ماڈیول",
            AppLanguage.ARABIC to "الوحدة",
            AppLanguage.HINDI to "मॉड्यूल"
        ),
        "ba_learning_objectives" to mapOf(
            AppLanguage.ENGLISH to "Learning Objectives",
            AppLanguage.URDU to "تعلیمی مقاصد",
            AppLanguage.ARABIC to "الأهداف التعليمية",
            AppLanguage.HINDI to "सीखने के उद्देश्य"
        ),
        "ba_introduction" to mapOf(
            AppLanguage.ENGLISH to "Introduction",
            AppLanguage.URDU to "تعارف",
            AppLanguage.ARABIC to "مقدمة",
            AppLanguage.HINDI to "परिचय"
        ),
        "ba_detailed_explanation" to mapOf(
            AppLanguage.ENGLISH to "Detailed Explanation",
            AppLanguage.URDU to "تفصیلی وضاحت",
            AppLanguage.ARABIC to "الشرح التفصيلي",
            AppLanguage.HINDI to "विस्तृत व्याख्या"
        ),
        "ba_context_note" to mapOf(
            AppLanguage.ENGLISH to "Quranic Context",
            AppLanguage.URDU to "آیت کا سیاق",
            AppLanguage.ARABIC to "سياق الآية",
            AppLanguage.HINDI to "आयत का संदर्भ"
        ),
        "ba_business_relevance" to mapOf(
            AppLanguage.ENGLISH to "Business Application",
            AppLanguage.URDU to "تجارتی اطلاق",
            AppLanguage.ARABIC to "التطبيق التجاري",
            AppLanguage.HINDI to "व्यापार में उपयोग"
        ),
        "ba_narrator" to mapOf(
            AppLanguage.ENGLISH to "Narrator",
            AppLanguage.URDU to "راوی",
            AppLanguage.ARABIC to "الراوي",
            AppLanguage.HINDI to "वर्णनकर्ता"
        ),
        "ba_classical_fiqh" to mapOf(
            AppLanguage.ENGLISH to "Classical Fiqh Foundation",
            AppLanguage.URDU to "کلاسیکی فقہی بنیاد",
            AppLanguage.ARABIC to "التأصيل الفقهي الكلاسيكي",
            AppLanguage.HINDI to "शास्त्रीय फ़िक़्ह आधार"
        ),
        "ba_contemporary_guidance" to mapOf(
            AppLanguage.ENGLISH to "Contemporary Standards (AAOIFI / SBP / SECP / IIFA)",
            AppLanguage.URDU to "معاصر معیارات (AAOIFI / اسٹیٹ بینک / SECP / مجمع الفقہ)",
            AppLanguage.ARABIC to "المعايير المعاصرة (أيوفي / المجمع الفقهي / البنوك المركزية)",
            AppLanguage.HINDI to "समकालीन मानक (AAOIFI / SBP / SECP / IIFA)"
        ),
        "ba_madhhab_differences" to mapOf(
            AppLanguage.ENGLISH to "Scholarly / Madhhab Differences",
            AppLanguage.URDU to "فقہی مذاہب کی آراء",
            AppLanguage.ARABIC to "الاختلافات الفقهية بين المذاهب",
            AppLanguage.HINDI to "फ़िक़्ह मज़हबों के मत"
        ),
        "ba_modern_example" to mapOf(
            AppLanguage.ENGLISH to "Practical Modern Business Example",
            AppLanguage.URDU to "جدید عملی کاروباری مثال",
            AppLanguage.ARABIC to "مثال تجاري عملي معاصر",
            AppLanguage.HINDI to "आधुनिक व्यावहारिक उदाहरण"
        ),
        "ba_halal_vs_prohibited" to mapOf(
            AppLanguage.ENGLISH to "Halal Practice vs Prohibited Practice",
            AppLanguage.URDU to "حلال طریقہ بمقابلہ ناجائز عمل",
            AppLanguage.ARABIC to "الممارسة الحلال مقابل الممارسة المحرمة",
            AppLanguage.HINDI to "हलाल तरीका बनाम वर्जित कार्य"
        ),
        "ba_common_mistakes" to mapOf(
            AppLanguage.ENGLISH to "Common Market Mistakes",
            AppLanguage.URDU to "کاروباری حلقوں کی عام غلطیاں",
            AppLanguage.ARABIC to "أخطاء شائعة في السوق",
            AppLanguage.HINDI to "आम बाज़ार गलतियाँ"
        ),
        "ba_compliance_checklist" to mapOf(
            AppLanguage.ENGLISH to "Compliance Verification Checklist",
            AppLanguage.URDU to "شرعی مطابقت چیک لسٹ",
            AppLanguage.ARABIC to "قائمة التحقق والالتزام الشرعي",
            AppLanguage.HINDI to "शरीअत अनुपालन चेकलिस्ट"
        ),
        "ba_summary" to mapOf(
            AppLanguage.ENGLISH to "Key Summary",
            AppLanguage.URDU to "خلاصہ سبق",
            AppLanguage.ARABIC to "ملخص الدرس",
            AppLanguage.HINDI to "सारांश"
        ),
        "ba_quiz_section" to mapOf(
            AppLanguage.ENGLISH to "Interactive Self-Assessment Quiz",
            AppLanguage.URDU to "مشقی سوالات (کوئز)",
            AppLanguage.ARABIC to "اختبار الفهم التفاعلي",
            AppLanguage.HINDI to "अभ्यास प्रश्नोत्तरी"
        ),
        "ba_best_score" to mapOf(
            AppLanguage.ENGLISH to "Best Score",
            AppLanguage.URDU to "بہترین اسکور",
            AppLanguage.ARABIC to "أعلى درجة",
            AppLanguage.HINDI to "सर्वश्रेष्ठ स्कोर"
        ),
        "ba_check_answers" to mapOf(
            AppLanguage.ENGLISH to "Check Answers",
            AppLanguage.URDU to "جوابات چیک کریں",
            AppLanguage.ARABIC to "تحقق من الإجابات",
            AppLanguage.HINDI to "उत्तर जांचें"
        ),
        "ba_retry" to mapOf(
            AppLanguage.ENGLISH to "Retry",
            AppLanguage.URDU to "دوبارہ کوشش",
            AppLanguage.ARABIC to "إعادة المحاولة",
            AppLanguage.HINDI to "पुनः प्रयास"
        ),
        "ba_practical_assignment" to mapOf(
            AppLanguage.ENGLISH to "Practical Business Assignment",
            AppLanguage.URDU to "عملی کاروباری اسائنمنٹ",
            AppLanguage.ARABIC to "التطبيق العملي والتكليف",
            AppLanguage.HINDI to "व्यावहारिक व्यवसाय कार्य"
        ),
        "ba_bookmark" to mapOf(
            AppLanguage.ENGLISH to "Bookmark",
            AppLanguage.URDU to "بک مارک",
            AppLanguage.ARABIC to "حفظ",
            AppLanguage.HINDI to "बुकमार्क"
        ),
        "ba_bookmarked" to mapOf(
            AppLanguage.ENGLISH to "Bookmarked",
            AppLanguage.URDU to "محفوظ شدہ",
            AppLanguage.ARABIC to "تم الحفظ",
            AppLanguage.HINDI to "सहेजा गया"
        ),
        "ba_mark_complete" to mapOf(
            AppLanguage.ENGLISH to "Mark Complete",
            AppLanguage.URDU to "مکمل نشان لگائیں",
            AppLanguage.ARABIC to "تحديد كمكتمل",
            AppLanguage.HINDI to "पूर्ण चिह्नित करें"
        ),
        "ba_completed" to mapOf(
            AppLanguage.ENGLISH to "Completed",
            AppLanguage.URDU to "مکمل",
            AppLanguage.ARABIC to "مكتمل",
            AppLanguage.HINDI to "पूर्ण"
        ),
        "ba_personal_notes" to mapOf(
            AppLanguage.ENGLISH to "Offline Study Notes",
            AppLanguage.URDU to "ذاتی آف لائن نوٹس",
            AppLanguage.ARABIC to "ملاحظات الدراسة الخاصة",
            AppLanguage.HINDI to "ऑफ़लाइन अध्ययन नोट्स"
        ),
        "ba_note_placeholder" to mapOf(
            AppLanguage.ENGLISH to "Write personal notes, questions for a scholar, or business action items...",
            AppLanguage.URDU to "اپنے کاروباری نکات یا مفتی سے پوچھنے کے سوالات یہاں لکھیں...",
            AppLanguage.ARABIC to "اكتب ملاحظاتك أو أسئلتك للمراجعة مع عالم...",
            AppLanguage.HINDI to "अपने नोट्स, व्यवसाय बिंदु या मुफ़्ती से पूछने हेतु प्रश्न लिखें..."
        ),
        "ba_save_note" to mapOf(
            AppLanguage.ENGLISH to "Save Offline",
            AppLanguage.URDU to "محفوظ کریں",
            AppLanguage.ARABIC to "حفظ محلياً",
            AppLanguage.HINDI to "ऑफ़लाइन सहेजें"
        ),
        "ba_saved_offline" to mapOf(
            AppLanguage.ENGLISH to "Saved cleanly to local storage",
            AppLanguage.URDU to "آف لائن محفوظ ہو گیا",
            AppLanguage.ARABIC to "تم الحفظ في الجهاز بنجاح",
            AppLanguage.HINDI to "डिवाइस में सहेजा गया"
        ),
        "ba_sources_attribution" to mapOf(
            AppLanguage.ENGLISH to "Verified Sources & Attribution",
            AppLanguage.URDU to "مستند مآخذ و حوالہ جات",
            AppLanguage.ARABIC to "المصادر والمراجع الموثقة",
            AppLanguage.HINDI to "प्रामाणिक स्रोत एवं संदर्भ"
        ),
        "ba_contract_definition" to mapOf(
            AppLanguage.ENGLISH to "Definition",
            AppLanguage.URDU to "تعریف",
            AppLanguage.ARABIC to "التعريف الفقهي",
            AppLanguage.HINDI to "परिभाषा"
        ),
        "ba_contract_purpose" to mapOf(
            AppLanguage.ENGLISH to "Commercial Purpose",
            AppLanguage.URDU to "تجارتی مقصد",
            AppLanguage.ARABIC to "الغرض التجاري",
            AppLanguage.HINDI to "व्यावसायिक उद्देश्य"
        ),
        "ba_shariah_principles" to mapOf(
            AppLanguage.ENGLISH to "Governing Shariah Principles",
            AppLanguage.URDU to "بنیادی شرعی اصول",
            AppLanguage.ARABIC to "الضوابط والأصول الشرعية",
            AppLanguage.HINDI to "मूल शरीअत सिद्धांत"
        ),
        "ba_contract_conditions" to mapOf(
            AppLanguage.ENGLISH to "Validity Conditions",
            AppLanguage.URDU to "جواز کی شرائط",
            AppLanguage.ARABIC to "شروط الصحة والنفاذ",
            AppLanguage.HINDI to "वैधता की शर्तें"
        ),
        "ba_party_rights" to mapOf(
            AppLanguage.ENGLISH to "Rights & Responsibilities of Parties",
            AppLanguage.URDU to "فریقین کے حقوق و فرائض",
            AppLanguage.ARABIC to "حقوق والتزامات الأطراف",
            AppLanguage.HINDI to "पक्षों के अधिकार और कर्तव्य"
        ),
        "ba_ownership_possession" to mapOf(
            AppLanguage.ENGLISH to "Ownership & Possession (Milkiyyah & Qabd)",
            AppLanguage.URDU to "ملکیت و قبضہ کے احکام",
            AppLanguage.ARABIC to "أحكام الملكية والقبض",
            AppLanguage.HINDI to "स्वामित्व और क़ब्ज़ नियम"
        ),
        "ba_profit_loss_rules" to mapOf(
            AppLanguage.ENGLISH to "Profit, Loss & Risk Rules",
            AppLanguage.URDU to "نفع، نقصان اور رسک کے اصول",
            AppLanguage.ARABIC to "قواعد الربح والخسارة والضمان",
            AppLanguage.HINDI to "लाभ, हानि व जोखिम नियम"
        ),
        "ba_invalid_structures" to mapOf(
            AppLanguage.ENGLISH to "Common Invalid / Sham Structures to Avoid",
            AppLanguage.URDU to "ناجائز یا فرضی شکلیں جن سے بچنا لازم ہے",
            AppLanguage.ARABIC to "تطبيقات صورية فاسدة يجب تجنبها",
            AppLanguage.HINDI to "बचने योग्य अमान्य/छद्म संरचनाएं"
        ),
        "ba_sbp_secp_guidance" to mapOf(
            AppLanguage.ENGLISH to "SBP / SECP / Institutional Practice Note",
            AppLanguage.URDU to "اسٹیٹ بینک / SECP عملی ضابطہ",
            AppLanguage.ARABIC to "التطبيق المصرفي والتنظيمي المعاصر",
            AppLanguage.HINDI to "SBP / SECP संस्थागत मार्गदर्शन"
        ),
        "ba_mufti_questions" to mapOf(
            AppLanguage.ENGLISH to "Questions to Confirm With a Qualified Mufti",
            AppLanguage.URDU to "مستند مفتی سے تصدیق طلب سوالات",
            AppLanguage.ARABIC to "أسئلة لمراجعتها مع مفتٍ مختص",
            AppLanguage.HINDI to "योग्य मुफ़्ती से पूछने योग्य प्रश्न"
        ),
        "ba_non_blanket_title" to mapOf(
            AppLanguage.ENGLISH to "Non-Blanket Scholarly Assessment Notice",
            AppLanguage.URDU to "اہم شرعی اصول: کاروبار کی مکمل حقیقت کا جائزہ",
            AppLanguage.ARABIC to "تنبيه منهجي: عدم التعميم وتقييم كل نشاط بتفاصيله",
            AppLanguage.HINDI to "महत्वपूर्ण चेतावनी: बिना विवरण जाने एकतरफ़ा फ़ैसला न लें"
        ),
        "ba_sec_intro" to mapOf(AppLanguage.ENGLISH to "Introduction", AppLanguage.URDU to "تعارف", AppLanguage.ARABIC to "المقدمة", AppLanguage.HINDI to "परिचय"),
        "ba_sec_operates" to mapOf(AppLanguage.ENGLISH to "How the Business Operates", AppLanguage.URDU to "کاروبار کیسے چلتا ہے", AppLanguage.ARABIC to "آلية عمل القطاع", AppLanguage.HINDI to "व्यापार संचालन प्रक्रिया"),
        "ba_sec_startup" to mapOf(AppLanguage.ENGLISH to "Startup Requirements", AppLanguage.URDU to "شروعاتی ضروریات", AppLanguage.ARABIC to "متطلبات التأسيس", AppLanguage.HINDI to "शुरुआती आवश्यकताएं"),
        "ba_sec_capital" to mapOf(AppLanguage.ENGLISH to "Capital & Halal Financing", AppLanguage.URDU to "سرمایہ اور حلال فنانسنگ", AppLanguage.ARABIC to "رأس المال والتمويل الحلال", AppLanguage.HINDI to "पूंजी एवं हलाल वित्त"),
        "ba_sec_suppliers" to mapOf(AppLanguage.ENGLISH to "Supplier Selection", AppLanguage.URDU to "سپلائرز کا انتخاب", AppLanguage.ARABIC to "اختيار الموردين", AppLanguage.HINDI to "आपूर्तिकर्ता चयन"),
        "ba_sec_procurement" to mapOf(AppLanguage.ENGLISH to "Purchasing & Procurement", AppLanguage.URDU to "خریداری و حصولِ مال", AppLanguage.ARABIC to "الشراء والتوريد", AppLanguage.HINDI to "खरीदारी और प्रापण"),
        "ba_sec_ownership" to mapOf(AppLanguage.ENGLISH to "Ownership & Possession Rules", AppLanguage.URDU to "ملکیت و قبضہ", AppLanguage.ARABIC to "الملكية والقبض", AppLanguage.HINDI to "स्वामित्व और क़ब्ज़"),
        "ba_sec_products" to mapOf(AppLanguage.ENGLISH to "Products & Services Shariah Screening", AppLanguage.URDU to "مصنوعات و خدمات کی حلال جانچ", AppLanguage.ARABIC to "فحص المنتجات والخدمات", AppLanguage.HINDI to "उत्पाद व सेवा हलाल जांच"),
        "ba_sec_pricing" to mapOf(AppLanguage.ENGLISH to "Pricing & Fair Profit Ethics", AppLanguage.URDU to "قیمت و منافع کے اخلاقی اصول", AppLanguage.ARABIC to "التسعير والربح العادل", AppLanguage.HINDI to "मूल्य और न्यायपूर्ण लाभ"),
        "ba_sec_customers" to mapOf(AppLanguage.ENGLISH to "Customer Rights & Disclosure", AppLanguage.URDU to "صارفین کے حقوق و شفافیت", AppLanguage.ARABIC to "حقوق العملاء والشفافية", AppLanguage.HINDI to "ग्राहक अधिकार और पारदर्शिता"),
        "ba_sec_employees" to mapOf(AppLanguage.ENGLISH to "Employee Rights & Safety", AppLanguage.URDU to "ملازمین کے حقوق اور تحفظ", AppLanguage.ARABIC to "حقوق العمال والسلامة", AppLanguage.HINDI to "कर्मचारी अधिकार एवं सुरक्षा"),
        "ba_sec_contracts" to mapOf(AppLanguage.ENGLISH to "Relevant Islamic Contracts", AppLanguage.URDU to "متعلقہ شرعی معاہدے", AppLanguage.ARABIC to "العقود الشرعية ذات الصلة", AppLanguage.HINDI to "संबंधित इस्लामी अनुबंध"),
        "ba_sec_prohibited" to mapOf(AppLanguage.ENGLISH to "Prohibited Practices & Traps", AppLanguage.URDU to "ناجائز امور اور خطرات", AppLanguage.ARABIC to "محاذير وممارسات محرمة", AppLanguage.HINDI to "निषिद्ध कार्य और जोखिम"),
        "ba_sec_case_study" to mapOf(AppLanguage.ENGLISH to "Realistic Case Study", AppLanguage.URDU to "عملی کیس اسٹڈی", AppLanguage.ARABIC to "دراسة حالة واقعية", AppLanguage.HINDI to "व्यावहारिक केस स्टडी"),
        "ba_disputed_chip" to mapOf(AppLanguage.ENGLISH to "DISPUTED", AppLanguage.URDU to "مختلف فیہ", AppLanguage.ARABIC to "مختلف فيه", AppLanguage.HINDI to "विवादित"),
        "ba_disputed_warning_title" to mapOf(
            AppLanguage.ENGLISH to "Contemporary Scholarly Disagreement — Handle Carefully",
            AppLanguage.URDU to "اہم تنبیہ: معاصر علماء کا مختلف فیہ مسئلہ",
            AppLanguage.ARABIC to "تنبيه مهم: مسألة معاصرة مختلف فيها بين الفقهاء",
            AppLanguage.HINDI to "चेतावनी: समकालीन विद्वानों में मतभेद वाला विषय"
        ),
        "ba_disputed_warning_body" to mapOf(
            AppLanguage.ENGLISH to "Qualified Islamic jurists and national regulators hold differing positions on this topic. Never simplify this into an absolute blanket ruling; study both viewpoints and consult a qualified Mufti alongside local law.",
            AppLanguage.URDU to "اس مسئلے پر مستند معاصر فقہاء اور قانونی اداروں میں سنجیدہ اختلافِ رائے موجود ہے؛ یکطرفہ سطحی فتویٰ نہ لگائیں بلکہ تمام پہلو پڑھ کر مستند مفتی و ملکی قانون سے رجوع کریں۔",
            AppLanguage.ARABIC to "يوجد خلاف معتبر بين الفقهاء والأنظمة المالية في هذه المسألة؛ يُرجى الاطلاع على كلا الرأيين ومراجعة مفتٍ مختص والالتزام بالأنظمة المحلية.",
            AppLanguage.HINDI to "योग्य मुफ़्तियों और वित्तीय नियामकों के इस पर अलग मत हैं; दोनों पक्ष पढ़ें और स्थानीय कानून व मुफ़्ती सलाह लें।"
        ),
        "ba_quran_hadith_basis" to mapOf(
            AppLanguage.ENGLISH to "Quran & Hadith Evidentiary Basis",
            AppLanguage.URDU to "قرآن و حدیث کے دلائل",
            AppLanguage.ARABIC to "الأدلة من القرآن والسنة",
            AppLanguage.HINDI to "क़ुरआन और हदीस आधार"
        ),
        "ba_classical_contemporary_positions" to mapOf(
            AppLanguage.ENGLISH to "Classical Fiqh & Contemporary Scholarly Positions",
            AppLanguage.URDU to "کلاسیکی فقہ اور معاصر علمی آراء",
            AppLanguage.ARABIC to "الأقوال الكلاسيكية والمعاصرة",
            AppLanguage.HINDI to "शास्त्रीय फ़िक़्ह व समकालीन विद्वान मत"
        ),
        "ba_practical_guidance" to mapOf(
            AppLanguage.ENGLISH to "Practical Guidance for Entrepreneurs",
            AppLanguage.URDU to "تاجروں کے لیے عملی رہنمائی",
            AppLanguage.ARABIC to "توجيه عملي لرواد الأعمال",
            AppLanguage.HINDI to "उद्यमियों हेतु व्यावहारिक मार्गदर्शन"
        ),
        "ba_scholarly_review_triggers" to mapOf(
            AppLanguage.ENGLISH to "When Mandatory Mufti Review Applies",
            AppLanguage.URDU to "مفتی سے مشورہ کب ضروری ہے",
            AppLanguage.ARABIC to "متى تجب استشارة المفتي",
            AppLanguage.HINDI to "कब मुफ़्ती परामर्श अनिवार्य है"
        ),
        "ba_startup_project_title" to mapOf(
            AppLanguage.ENGLISH to "Module 24: 13-Step Halal Business Startup Project",
            AppLanguage.URDU to "ماڈیول 24: 13 نکاتی حلال کاروباری اسٹارٹ اپ پروجیکٹ",
            AppLanguage.ARABIC to "الوحدة ٢٤: مشروع تأسيس شركة حلال في ١٣ خطوة",
            AppLanguage.HINDI to "मॉड्यूल 24: 13-चरणीय हलाल स्टार्टअप प्रोजेक्ट"
        ),
        "ba_steps_completed" to mapOf(
            AppLanguage.ENGLISH to "steps completed",
            AppLanguage.URDU to "مراحل مکمل",
            AppLanguage.ARABIC to "خطوات مكتملة",
            AppLanguage.HINDI to "चरण पूर्ण"
        ),
        "ba_startup_write_plan" to mapOf(
            AppLanguage.ENGLISH to "Write your business plan details offline...",
            AppLanguage.URDU to "اپنے کاروباری منصوبے کی تفصیل یہاں لکھیں...",
            AppLanguage.ARABIC to "اكتب خطة مشروعك هنا...",
            AppLanguage.HINDI to "अपनी व्यापार योजना यहाँ लिखें..."
        ),
        "ba_zakat_calc_title" to mapOf(
            AppLanguage.ENGLISH to "Offline Business Zakat Estimator (AAOIFI SS 35)",
            AppLanguage.URDU to "کاروباری زکوٰۃ کیلکولیٹر (AAOIFI معیار 35)",
            AppLanguage.ARABIC to "حاسبة زكاة التجارة (أيوفي ٣٥)",
            AppLanguage.HINDI to "व्यापारिक ज़कात कैलकुलेटर (AAOIFI 35)"
        ),
        "ba_zakat_calc_subtitle" to mapOf(
            AppLanguage.ENGLISH to "Operational fixed assets (shop building, vans, machinery) stay exempt; include tradable stock at current market valuation.",
            AppLanguage.URDU to "دکان کی عمارت، گاڑیاں اور مشینری زکوٰۃ سے مستثنیٰ ہیں؛ صرف سامانِ تجارت کی موجودہ مارکیٹ قیمت شامل کریں۔",
            AppLanguage.ARABIC to "الأصول الثابتة كالمباني والآلات معفاة؛ أدخل قيمة البضاعة المعدة للبيع بسعر السوق.",
            AppLanguage.HINDI to "दुकान, वाहन व मशीनरी ज़कात-मुक्त हैं; बिक्री योग्य माल का बाज़ार मूल्य जोड़ें।"
        ),
        "ba_zakat_cash" to mapOf(AppLanguage.ENGLISH to "Business Cash & Bank Balances", AppLanguage.URDU to "کاروباری نقد و بینک بیلنس", AppLanguage.ARABIC to "النقد والأرصدة البنكية", AppLanguage.HINDI to "व्यापार नकद व बैंक शेष"),
        "ba_zakat_stock" to mapOf(AppLanguage.ENGLISH to "Tradable Inventory (Market Value)", AppLanguage.URDU to "فروخت کے لیے مال (موجودہ مارکیٹ قیمت)", AppLanguage.ARABIC to "البضاعة المعدة للبيع (القيمة السوقية)", AppLanguage.HINDI to "बिक्री योग्य स्टॉक (बाज़ार मूल्य)"),
        "ba_zakat_receivables" to mapOf(AppLanguage.ENGLISH to "Collectible Trade Receivables", AppLanguage.URDU to "قابلِ وصول تجارتی قرضے", AppLanguage.ARABIC to "الديون المرجوة", AppLanguage.HINDI to "वसूल होने वाले बकाया"),
        "ba_zakat_liabilities" to mapOf(AppLanguage.ENGLISH to "Short-term Business Liabilities (-)", AppLanguage.URDU to "فوری واجب الادا قرضے (-)", AppLanguage.ARABIC to "الالتزامات القصيرة الحالة (-)", AppLanguage.HINDI to "अल्पकालिक देनदारियाँ (-)"),
        "ba_zakat_net_base" to mapOf(AppLanguage.ENGLISH to "Net Zakatable Business Base", AppLanguage.URDU to "خالص قابلِ زکوٰۃ رقم", AppLanguage.ARABIC to "الوعاء الزكوي الصافي", AppLanguage.HINDI to "शुद्ध ज़कात आधार"),
        "ba_zakat_due" to mapOf(AppLanguage.ENGLISH to "Estimated Annual Zakat (2.5%)", AppLanguage.URDU to "سالانہ زکوٰۃ (2.5%)", AppLanguage.ARABIC to "الزكاة المستحقة (٢.٥٪)", AppLanguage.HINDI to "वार्षिक ज़कात (2.5%)"),
        "ba_pricing_calc_title" to mapOf(
            AppLanguage.ENGLISH to "Halal Cost & Fair Margin Calculator",
            AppLanguage.URDU to "حلال لاگت و منصفانہ قیمت کیلکولیٹر",
            AppLanguage.ARABIC to "حاسبة التكلفة والتسعير العادل",
            AppLanguage.HINDI to "हलाल लागत एवं मूल्य कैलकुलेटर"
        ),
        "ba_pricing_non_pct_note" to mapOf(
            AppLanguage.ENGLISH to "Reminder: Shariah sets no arbitrary % profit cap — Halal vs Haram depends on zero fraud, zero monopoly hoarding, and free mutual consent.",
            AppLanguage.URDU to "یاددہانی: شریعت نے منافع کا کوئی مقررہ فیصد طے نہیں کیا؛ حلال و حرام کا مدار دھوکہ، اجارہ داری اور باہمی رضا پر ہے۔",
            AppLanguage.ARABIC to "تذكير: لم يحدد الشرع نسبة ربح مقطوعة؛ المعيار هو خلو البيع من الغش والاحتكار والغبن الفاحش.",
            AppLanguage.HINDI to "स्मरण: शरीअत में लाभ प्रतिशत की कोई निश्चित सीमा नहीं — ईमानदारी, शून्य जमाखोरी व सहमति कसौटी है।"
        ),
        "ba_pricing_material" to mapOf(AppLanguage.ENGLISH to "Direct Unit Material Cost", AppLanguage.URDU to "فی یونٹ سامان کی لاگت", AppLanguage.ARABIC to "تكلفة المواد للوحدة", AppLanguage.HINDI to "प्रति इकाई सामग्री लागत"),
        "ba_pricing_overhead" to mapOf(AppLanguage.ENGLISH to "Labor / Rent / Packaging per Unit", AppLanguage.URDU to "مزدوری، کرایہ و پیکنگ فی یونٹ", AppLanguage.ARABIC to "الأجور والتشغيل والتغليف", AppLanguage.HINDI to "श्रम, किराया व पैकिंग लागत"),
        "ba_pricing_margin_pct" to mapOf(AppLanguage.ENGLISH to "Transparent Margin %", AppLanguage.URDU to "منصفانہ منافع فیصد %", AppLanguage.ARABIC to "هامش الربح المتفق عليه %", AppLanguage.HINDI to "पारदर्शी लाभ प्रतिशत %"),
        "ba_pricing_suggested" to mapOf(AppLanguage.ENGLISH to "Transparent Unit Price", AppLanguage.URDU to "شفاف قیمتِ فروخت", AppLanguage.ARABIC to "السعر المقترح العادل", AppLanguage.HINDI to "पारदर्शी विक्रय मूल्य"),
        "ba_cert_title" to mapOf(
            AppLanguage.ENGLISH to "Offline Completion Certificate (PDF)",
            AppLanguage.URDU to "آف لائن تکمیلی سرٹیفکیٹ (PDF)",
            AppLanguage.ARABIC to "شهادة إتمام الدراسة دون إنترنت (PDF)",
            AppLanguage.HINDI to "ऑफ़लाइन पूर्णता प्रमाणपत्र (PDF)"
        ),
        "ba_progress_summary" to mapOf(
            AppLanguage.ENGLISH to "Completed Items",
            AppLanguage.URDU to "مکمل شدہ ابواب",
            AppLanguage.ARABIC to "العناصر المكتملة",
            AppLanguage.HINDI to "पूर्ण विषय"
        ),
        "ba_learner_name" to mapOf(
            AppLanguage.ENGLISH to "Learner Full Name on Certificate",
            AppLanguage.URDU to "سرٹیفکیٹ پر نام درج کریں",
            AppLanguage.ARABIC to "الاسم الكامل على الشهادة",
            AppLanguage.HINDI to "प्रमाणपत्र पर पूरा नाम"
        ),
        "ba_generate_pdf_cert" to mapOf(
            AppLanguage.ENGLISH to "Generate Offline PDF Certificate",
            AppLanguage.URDU to "آف لائن PDF سرٹیفکیٹ بنائیں",
            AppLanguage.ARABIC to "إصدار شهادة PDF محلية",
            AppLanguage.HINDI to "ऑफ़लाइन PDF प्रमाणपत्र बनाएं"
        ),
        "ba_cert_saved_offline" to mapOf(
            AppLanguage.ENGLISH to "Certificate PDF saved on device:",
            AppLanguage.URDU to "سرٹیفکیٹ فون میں محفوظ ہو گیا:",
            AppLanguage.ARABIC to "تم حفظ ملف الشهادة في الجهاز:",
            AppLanguage.HINDI to "प्रमाणपत्र PDF डिवाइस में सहेजा गया:"
        ),
        "ba_bookmarks_heading" to mapOf(
            AppLanguage.ENGLISH to "Saved Bookmarks",
            AppLanguage.URDU to "محفوظ شدہ بک مارکس",
            AppLanguage.ARABIC to "المفضلة المحفوظة",
            AppLanguage.HINDI to "सहेजे गए बुकमार्क"
        ),
        "ba_no_bookmarks" to mapOf(
            AppLanguage.ENGLISH to "No Business Academy bookmarks yet.",
            AppLanguage.URDU to "ابھی کوئی بزنس اکیڈمی بک مارک موجود نہیں۔",
            AppLanguage.ARABIC to "لا توجد موضوعات محفوظة بعد.",
            AppLanguage.HINDI to "अभी तक कोई बुकमार्क नहीं है।"
        ),
        "ba_no_notes" to mapOf(
            AppLanguage.ENGLISH to "No personal study notes yet.",
            AppLanguage.URDU to "ابھی کوئی ذاتی نوٹ محفوظ نہیں۔",
            AppLanguage.ARABIC to "لا توجد ملاحظات شخصية بعد.",
            AppLanguage.HINDI to "अभी तक कोई अध्ययन नोट्स नहीं हैं।"
        ),
        "dua_audio_pack_summary" to mapOf(
            AppLanguage.ENGLISH to "Shaykh Mishary Alafasy • Three Quls bundled offline; %d EveryAyah Ayah tracks cached",
            AppLanguage.URDU to "شیخ مشاری العفاسی • تینوں قل آف لائن شامل ہیں؛ %d قرآنی دعا ٹریکس محفوظ ہیں",
            AppLanguage.ARABIC to "الشيخ مشاري العفاسي • المعوذات متوفرة دون إنترنت؛ %d مقاطع مخزنة محلياً",
            AppLanguage.HINDI to "शैख़ मिशारी अलअफ़ासी • तीन क़ुल ऑफ़लाइन शामिल; %d आयत ऑडियो सहेजे गए"
        ),
        "dua_cached_tracks_toast" to mapOf(
            AppLanguage.ENGLISH to "Cached %d Quranic Dua audio tracks",
            AppLanguage.URDU to "%d قرآنی دعا آڈیو ٹریکس محفوظ ہو گئے",
            AppLanguage.ARABIC to "تم حفظ %d مقاطع صوتية للأدعية القرآنية",
            AppLanguage.HINDI to "%d क़ुरआनी दुआ ऑडियो ट्रैक सहेजे गए"
        ),
        "dua_audio_cache_failed" to mapOf(
            AppLanguage.ENGLISH to "Audio cache failed",
            AppLanguage.URDU to "آڈیو محفوظ کرنے میں ناکامی",
            AppLanguage.ARABIC to "تعذر حفظ الملفات الصوتية",
            AppLanguage.HINDI to "ऑडियो सहेजने में विफल"
        ),
        "prayer_perm_denied_toast" to mapOf(
            AppLanguage.ENGLISH to "Location permission not granted. You can select your city manually.",
            AppLanguage.URDU to "مقام کی اجازت نہیں ملی۔ آپ دستی طور پر اپنا شہر منتخب کر سکتے ہیں۔",
            AppLanguage.ARABIC to "لم يتم منح إذن الموقع. يمكنك اختيار مدينتك يدوياً.",
            AppLanguage.HINDI to "स्थान अनुमति नहीं मिली। आप अपना शहर मैन्युअल रूप से चुन सकते हैं।"
        ),
        "prayer_allow" to mapOf(
            AppLanguage.ENGLISH to "Allow",
            AppLanguage.URDU to "اجازت دیں",
            AppLanguage.ARABIC to "سماح",
            AppLanguage.HINDI to "अनुमति दें"
        ),
        "prayer_turn_on" to mapOf(
            AppLanguage.ENGLISH to "Turn ON",
            AppLanguage.URDU to "آن کریں",
            AppLanguage.ARABIC to "تشغيل",
            AppLanguage.HINDI to "चालू करें"
        ),
        "prayer_gps_live" to mapOf(
            AppLanguage.ENGLISH to "Auto GPS Fix (Live)",
            AppLanguage.URDU to "خودکار GPS مقام (لائیو)",
            AppLanguage.ARABIC to "تحديد تلقائي عبر GPS",
            AppLanguage.HINDI to "स्वचालित GPS स्थान (लाइव)"
        ),
        "prayer_manual_preset" to mapOf(
            AppLanguage.ENGLISH to "Manual Location Preset",
            AppLanguage.URDU to "دستی منتخب کردہ مقام",
            AppLanguage.ARABIC to "الموقع المختار يدوياً",
            AppLanguage.HINDI to "मैन्युअल चुना गया स्थान"
        ),
        "prayer_adjusted_prefix" to mapOf(
            AppLanguage.ENGLISH to "Adjusted:",
            AppLanguage.URDU to "ترمیم شدہ:",
            AppLanguage.ARABIC to "تعديل:",
            AppLanguage.HINDI to "समायोजित:"
        ),
        "prayer_enter_coords_title" to mapOf(
            AppLanguage.ENGLISH to "Enter Exact Coordinates",
            AppLanguage.URDU to "درست عرض و طولِ بلد درج کریں",
            AppLanguage.ARABIC to "أدخل الإحداثيات الدقيقة",
            AppLanguage.HINDI to "सटीक निर्देशांक दर्ज करें"
        ),
        "prayer_enter_coords_desc" to mapOf(
            AppLanguage.ENGLISH to "Enter any latitude and longitude worldwide for pinpoint calculation.",
            AppLanguage.URDU to "عین حساب کے لیے دنیا بھر سے کسی بھی مقام کا عرض و طولِ بلد درج کریں۔",
            AppLanguage.ARABIC to "أدخل خطوط الطول والعرض لأي موقع حول العالم لحساب دقيق.",
            AppLanguage.HINDI to "सटीक गणना के लिए दुनिया भर से अक्षांश और देशांतर दर्ज करें।"
        ),
        "prayer_lat_label" to mapOf(
            AppLanguage.ENGLISH to "Latitude (-90.0 to 90.0)",
            AppLanguage.URDU to "عرضِ بلد (-90.0 تا 90.0)",
            AppLanguage.ARABIC to "خط العرض (-90.0 إلى 90.0)",
            AppLanguage.HINDI to "अक्षांश (-90.0 से 90.0)"
        ),
        "prayer_lon_label" to mapOf(
            AppLanguage.ENGLISH to "Longitude (-180.0 to 180.0)",
            AppLanguage.URDU to "طولِ بلد (-180.0 تا 180.0)",
            AppLanguage.ARABIC to "خط الطول (-180.0 إلى 180.0)",
            AppLanguage.HINDI to "देशांतर (-180.0 से 180.0)"
        ),
        "prayer_apply" to mapOf(
            AppLanguage.ENGLISH to "Apply",
            AppLanguage.URDU to "لاگو کریں",
            AppLanguage.ARABIC to "تطبيق",
            AppLanguage.HINDI to "लागू करें"
        ),
        "prayer_coords_updated" to mapOf(
            AppLanguage.ENGLISH to "Coordinates updated successfully",
            AppLanguage.URDU to "مقام کے کوآرڈینیٹس کامیابی سے اپ ڈیٹ ہو گئے",
            AppLanguage.ARABIC to "تم تحديث الإحداثيات بنجاح",
            AppLanguage.HINDI to "निर्देशांक सफलतापूर्वक अपडेट हो गए"
        ),
        "prayer_invalid_coords" to mapOf(
            AppLanguage.ENGLISH to "Invalid coordinates",
            AppLanguage.URDU to "غلط عرض و طولِ بلد",
            AppLanguage.ARABIC to "إحداثيات غير صالحة",
            AppLanguage.HINDI to "अमान्य निर्देशांक"
        ),
        "prayer_test_toast" to mapOf(
            AppLanguage.ENGLISH to "Testing prayer notification & Adhan audio...",
            AppLanguage.URDU to "نماز کے نوٹیفکیشن اور اذان آڈیو کی جانچ کی جا رہی ہے...",
            AppLanguage.ARABIC to "جارٍ اختبار تنبيه الصلاة وصوت الأذان...",
            AppLanguage.HINDI to "नमाज़ सूचना और अज़ान ऑडियो का परीक्षण..."
        ),
        "prayer_day_col" to mapOf(
            AppLanguage.ENGLISH to "Day",
            AppLanguage.URDU to "دن",
            AppLanguage.ARABIC to "اليوم",
            AppLanguage.HINDI to "दिन"
        ),
        "ram_prayer_city_method_btn" to mapOf(
            AppLanguage.ENGLISH to "Prayer Times, City & Calculation Method",
            AppLanguage.URDU to "اوقاتِ نماز، شہر اور حساب کا طریقہ",
            AppLanguage.ARABIC to "مواقيت الصلاة والمدينة وطريقة الحساب",
            AppLanguage.HINDI to "नमाज़ के औक़ात, शहर और गणना विधि"
        )
    )
}
