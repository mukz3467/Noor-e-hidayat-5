package com.example.data.asma

import com.example.data.model.AppLanguage

object AsmaData {
    val allNames: List<AsmaName> by lazy {
        AsmaCatalogPart1.entries + AsmaCatalogPart2.entries
    }

    fun getByNumber(number: Int): AsmaName? = allNames.firstOrNull { it.number == number }

    fun filterAndSearch(
        query: String,
        filter: AsmaFilterOption,
        favorites: Set<Int>
    ): List<AsmaName> {
        val base = when (filter) {
            AsmaFilterOption.ALL -> allNames
            AsmaFilterOption.QURANIC -> allNames.filter { it.evidenceCategory == NameEvidenceCategory.QURAN_EXPLICIT }
            AsmaFilterOption.HADITH -> allNames.filter { it.evidenceCategory == NameEvidenceCategory.AUTHENTIC_HADITH }
            AsmaFilterOption.COMPILATION -> allNames.filter { it.evidenceCategory == NameEvidenceCategory.SCHOLARLY_COMPILATION }
            AsmaFilterOption.FAVORITES -> allNames.filter { favorites.contains(it.number) }
        }
        val q = query.trim().lowercase()
        if (q.isEmpty()) return base
        return base.filter { name ->
            name.number.toString() == q ||
                name.arabic.contains(q) ||
                name.transliteration.lowercase().contains(q) ||
                name.meaningEn.lowercase().contains(q) ||
                name.meaningUr.contains(q) ||
                name.meaningHi.contains(q) ||
                name.explanationEn.lowercase().contains(q)
        }
    }

    fun scholarlyDiversitySummary(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH ->
            "Scholarly Reference Note: Allah says in the Quran (7:180): 'To Allah belong the Most Beautiful Names, so invoke Him by them.' Authentic Hadith (Sahih al-Bukhari 2736 & Sahih Muslim 2677) state Allah has 99 Names, yet Allah's Names are not limited to 99 (Musnad Ahmad 3712), and no single narration unanimously fixes the exact 99-name list. While the popular 99-name list preserved via Jami' at-Tirmidhi 3507 is widely memorized, Hadith scholars (including al-Tirmidhi, Ibn Hazm, Ibn Kathir, and Ibn Hajar) noted variations across Quranic and Sunnah compilations. Each entry below distinguishes explicit Quranic Names, Hadith-established Names, and traditional scholarly compilations."
        AppLanguage.URDU ->
            "علمی و تحقیقی وضاحت: قرآنِ مجید (الأعراف 7:180) میں ارشاد ہے: 'اور اللہ ہی کے لیے بہترین نام ہیں، پس اسے انہی ناموں سے پکارو۔' صحیح بخاری (2736) اور صحیح مسلم (2677) میں 99 اسماء کا ذکر ہے، تاہم مسند احمد (3712) کے مطابق اللہ کے اسماء صرف 99 تک محدود نہیں، اور تمام علماء کسی ایک متعین فہرست پر متفق نہیں ہیں۔ جامع ترمذی (3507) کی معروف فہرست کے علاوہ حافظ ابن حجر، ابن کثیر اور دیگر محدثین کی فہرستوں میں بعض ناموں کا اختلاف ملتا ہے۔ ذیل میں ہر اسم کے ساتھ صریح قرآنی، صحیح حدیث، یا روایتی علمی فہرست کی حیثیت واضح کر دی گئی ہے۔"
        AppLanguage.ARABIC ->
            "تنبيه علمي ومنهجي: قال الله تعالى: ﴿وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا﴾ (الأعراف: 180)، وثبت في الصحيحين (البخاري 2736 ومسلم 2677) أن لله تسعةً وتسعين اسماً، مع عدم حصر أسماء الله في هذا العدد (مسند أحمد 3712). ولم يثبت حديث مرفوع يُجمع العلماء على تعيين قائمة التسعة والتسعين بتمامها؛ فقد أشار الترمذي (3507) وابن حجر وابن كثير إلى تفاوت اجتهادات العلماء في جمعها من القرآن والسنة. لذا يُميَّز في هذا العرض بين الأسماء الصريحة في القرآن، والثابتة في السنة الصحيحة، وما ورد في القوائم العلمية التراثية."
        AppLanguage.HINDI ->
            "विद्वानों का संदर्भ नोट: क़ुरआन (7:180) में फरमाया गया है: 'अल्लाह के लिए सबसे सुंदर नाम हैं, अतः उसे उन्हीं से पुकारो।' सहीह बुखारी (2736) और सहीह मुस्लिम (2677) में 99 नामों का उल्लेख है, परंतु मुसनद अहमद (3712) के अनुसार अल्लाह के नाम केवल 99 तक सीमित नहीं हैं और कोई एकल हदीस सर्वसम्मत रूप से सभी 99 नामों की सूची तय नहीं करती। तिर्मिज़ी (3507) की प्रचलित सूची के साथ इब्न हजर और अन्य मुहद्दिसीन के संकलनों में थोड़ा अंतर मिलता है। नीचे प्रत्येक नाम के साथ क़ुरआनी, हदीस या विद्वान-संकलन का प्रमाण स्पष्ट किया गया है।"
    }
}
