package com.example.data.business

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class StartupProjectStepTemplate(
    val stepNumber: Int,
    val title: LocalizedText4,
    val prompt: LocalizedText4
)

class BusinessAcademyRepository(private val dao: BusinessDao) {

    fun observeAllProgress(): Flow<List<BusinessProgressEntity>> = dao.observeAllProgress()
    fun observeAllQuizResults(): Flow<List<BusinessQuizResultEntity>> = dao.observeAllQuizResults()
    fun observeBookmarks(): Flow<List<BusinessBookmarkEntity>> = dao.observeBookmarks()
    fun observeNotes(): Flow<List<BusinessNoteEntity>> = dao.observeNotes()
    fun observeStartupProjectSteps(): Flow<List<BusinessStartupProjectEntity>> = dao.observeStartupProjectSteps()

    suspend fun markOpened(itemId: String, itemType: String) {
        val existing = dao.getProgressById(itemId)
        dao.upsertProgress(
            BusinessProgressEntity(
                itemId = itemId,
                itemType = itemType,
                isCompleted = existing?.isCompleted ?: false,
                lastOpenedAt = System.currentTimeMillis(),
                completedAt = existing?.completedAt
            )
        )
    }

    suspend fun setCompleted(itemId: String, itemType: String, completed: Boolean) {
        dao.upsertProgress(
            BusinessProgressEntity(
                itemId = itemId,
                itemType = itemType,
                isCompleted = completed,
                lastOpenedAt = System.currentTimeMillis(),
                completedAt = if (completed) System.currentTimeMillis() else null
            )
        )
    }

    suspend fun saveQuizResult(itemId: String, itemType: String, score: Int, totalQuestions: Int) {
        val existing = dao.getQuizResultById(itemId)
        val best = maxOf(existing?.bestScore ?: 0, score)
        dao.upsertQuizResult(
            BusinessQuizResultEntity(
                itemId = itemId,
                itemType = itemType,
                score = score,
                totalQuestions = totalQuestions,
                bestScore = best,
                attemptedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleBookmark(itemId: String, itemType: String, titleSnapshot: String): Boolean {
        val existing = dao.getBookmarkById(itemId)
        return if (existing != null) {
            dao.deleteBookmarkById(itemId)
            false
        } else {
            dao.insertBookmark(
                BusinessBookmarkEntity(
                    itemId = itemId,
                    itemType = itemType,
                    titleSnapshot = titleSnapshot
                )
            )
            true
        }
    }

    suspend fun saveNote(itemId: String, itemType: String, noteText: String) {
        val clean = noteText.trim()
        if (clean.isEmpty()) {
            dao.deleteNoteById(itemId)
        } else {
            dao.upsertNote(
                BusinessNoteEntity(
                    itemId = itemId,
                    itemType = itemType,
                    noteText = clean
                )
            )
        }
    }

    suspend fun saveStartupStep(stepNumber: Int, answerText: String, isCompleted: Boolean) {
        dao.upsertStartupProjectStep(
            BusinessStartupProjectEntity(
                stepNumber = stepNumber,
                answerText = answerText,
                isCompleted = isCompleted
            )
        )
    }

    /**
     * 100% Offline PDF Certificate generator saved in app internal storage (filesDir/certificates).
     */
    fun generateCompletionCertificatePdf(
        context: Context,
        learnerName: String,
        completedCount: Int,
        totalItemsCount: Int,
        quizzesPassedCount: Int,
        startupStepsCompleted: Int
    ): File {
        val dir = File(context.filesDir, "certificates").apply { mkdirs() }
        val safeName = learnerName.trim().ifBlank { "Student of Halal Tijarah" }
        val outFile = File(dir, "noor_business_academy_certificate.pdf")

        val generatedByAndroidPdf = runCatching {
            val pdf = PdfDocument()
            try {
                val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create() // A4 Landscape points
                val page = pdf.startPage(pageInfo)
                val canvas = page.canvas

                val bgPaint = Paint().apply { color = Color.rgb(250, 248, 242) }
                canvas.drawRect(0f, 0f, 842f, 595f, bgPaint)

                val emeraldBorder = Paint().apply {
                    color = Color.rgb(11, 95, 67)
                    style = Paint.Style.STROKE
                    strokeWidth = 10f
                }
                canvas.drawRect(24f, 24f, 818f, 571f, emeraldBorder)

                val goldInnerBorder = Paint().apply {
                    color = Color.rgb(197, 155, 39)
                    style = Paint.Style.STROKE
                    strokeWidth = 3f
                }
                canvas.drawRect(38f, 38f, 804f, 557f, goldInnerBorder)

                val titlePaint = Paint().apply {
                    color = Color.rgb(11, 95, 67)
                    textSize = 26f
                    isFakeBoldText = true
                    textAlign = Paint.Align.CENTER
                }
                val subtitlePaint = Paint().apply {
                    color = Color.rgb(156, 118, 22)
                    textSize = 18f
                    isFakeBoldText = true
                    textAlign = Paint.Align.CENTER
                }
                val bodyPaint = Paint().apply {
                    color = Color.rgb(45, 52, 54)
                    textSize = 15f
                    textAlign = Paint.Align.CENTER
                }
                val namePaint = Paint().apply {
                    color = Color.rgb(11, 95, 67)
                    textSize = 30f
                    isFakeBoldText = true
                    textAlign = Paint.Align.CENTER
                }
                val smallPaint = Paint().apply {
                    color = Color.rgb(95, 105, 108)
                    textSize = 12f
                    textAlign = Paint.Align.CENTER
                }

                val cx = 421f
                canvas.drawText("NOOR-E-HIDAYAT — ISLAMIC BUSINESS ACADEMY", cx, 105f, titlePaint)
                canvas.drawText("اسلامی تجارت و حلال کاروبار کی مکمل اکیڈمی", cx, 138f, subtitlePaint)
                canvas.drawText("CERTIFICATE OF OFFLINE STUDY COMPLETION", cx, 195f, bodyPaint)
                canvas.drawText(safeName, cx, 255f, namePaint)
                canvas.drawLine(190f, 272f, 652f, 272f, goldInnerBorder)

                canvas.drawText(
                    "Has diligently studied Islamic Commercial Jurisprudence, Halal Business Ethics & Contracts",
                    cx,
                    312f,
                    bodyPaint
                )
                canvas.drawText(
                    "Curriculum Completed: $completedCount / $totalItemsCount   •   Assessments Passed: $quizzesPassedCount   •   Startup Steps: $startupStepsCompleted / 13",
                    cx,
                    352f,
                    bodyPaint
                )

                val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date())
                canvas.drawText("Issued Offline on Device • Date: $dateStr", cx, 435f, smallPaint)
                canvas.drawText(
                    "Educational Record Only — Complex Fatwa or Legal Structuring Requires a Qualified Mufti / Counsel",
                    cx,
                    465f,
                    smallPaint
                )

                pdf.finishPage(page)
                FileOutputStream(outFile).use { fos ->
                    pdf.writeTo(fos)
                }
                true
            } finally {
                runCatching { pdf.close() }
            }
        }.getOrDefault(false)

        if (!generatedByAndroidPdf || !outFile.exists() || outFile.length() < 100L) {
            writeMinimalValidPdfFallback(
                outFile = outFile,
                safeName = safeName,
                completedCount = completedCount,
                totalItemsCount = totalItemsCount,
                quizzesPassedCount = quizzesPassedCount,
                startupStepsCompleted = startupStepsCompleted
            )
        }
        return outFile
    }

    private fun writeMinimalValidPdfFallback(
        outFile: File,
        safeName: String,
        completedCount: Int,
        totalItemsCount: Int,
        quizzesPassedCount: Int,
        startupStepsCompleted: Int
    ) {
        fun esc(s: String) = s.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")
        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date())
        val streamContent = buildString {
            append("BT /F1 20 Tf 72 520 Td (NOOR-E-HIDAYAT - ISLAMIC BUSINESS ACADEMY) Tj ET\n")
            append("BT /F1 14 Tf 72 480 Td (CERTIFICATE OF OFFLINE STUDY COMPLETION) Tj ET\n")
            append("BT /F1 22 Tf 72 425 Td (${esc(safeName)}) Tj ET\n")
            append("BT /F1 12 Tf 72 370 Td (Completed: $completedCount / $totalItemsCount | Quizzes Passed: $quizzesPassedCount | Startup Steps: $startupStepsCompleted / 13) Tj ET\n")
            append("BT /F1 11 Tf 72 320 Td (Issued Offline on Device - Date: $dateStr) Tj ET\n")
            append("BT /F1 10 Tf 72 280 Td (Educational Record Only - Consult a Qualified Mufti for Binding Fatwa) Tj ET\n")
        }
        val streamBytes = streamContent.toByteArray(Charsets.US_ASCII)
        val objs = listOf(
            "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n",
            "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n",
            "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 842 595] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n",
            "4 0 obj\n<< /Length ${streamBytes.size} >>\nstream\n$streamContent\nendstream\nendobj\n",
            "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n"
        )
        val sb = StringBuilder("%PDF-1.4\n")
        val offsets = mutableListOf<Int>()
        objs.forEach { obj ->
            offsets.add(sb.toString().toByteArray(Charsets.US_ASCII).size)
            sb.append(obj)
        }
        val xrefOffset = sb.toString().toByteArray(Charsets.US_ASCII).size
        sb.append("xref\n0 ${objs.size + 1}\n0000000000 65535 f \n")
        offsets.forEach { off ->
            sb.append(String.format(Locale.US, "%010d 00000 n \n", off))
        }
        sb.append("trailer\n<< /Size ${objs.size + 1} /Root 1 0 R >>\nstartxref\n$xrefOffset\n%%EOF\n")
        outFile.writeBytes(sb.toString().toByteArray(Charsets.US_ASCII))
    }

    companion object {
        val startupStepTemplates: List<StartupProjectStepTemplate> = listOf(
            StartupProjectStepTemplate(
                1,
                LocalizedText4("1. Halal Business Idea & Intention", "1. حلال کاروباری آئیڈیا اور نیت", "١. فكرة المشروع الحلال والنية", "1. हलाल व्यापार विचार और नीयत"),
                LocalizedText4("Define the lawful product/service solving a genuine need and renew your Niyyah for earning Halal & serving people.", "وہ جائز مصنوعات یا خدمت لکھیں جو حقیقی ضرورت پوری کرے اور حلال کمائی و خدمتِ خلق کی نیت تحریر کریں۔", "حدد المنتج أو الخدمة المباحة وجدّد نية الكسب الحلال.", "वैध उत्पाद/सेवा व हलाल कमाई की नीयत लिखें।")
            ),
            StartupProjectStepTemplate(
                2,
                LocalizedText4("2. Market Validation & Demand", "2. مارکیٹ کی ضرورت اور تصدیق", "٢. دراسة السوق والطلب", "2. बाज़ार मांग और सत्यापन"),
                LocalizedText4("Record target customers, real pain points verified locally, and competitors.", "ہدف صارفین، ان کا اصل مسئلہ اور مارکیٹ میں موجود حریفوں کا جائزہ لکھیں۔", "اذكر الفئة المستهدفة واحتياجهم الفعلي والمنافسين.", "लक्षित ग्राहक, वास्तविक आवश्यकता व प्रतिस्पर्धा दर्ज करें।")
            ),
            StartupProjectStepTemplate(
                3,
                LocalizedText4("3. Shariah Screening & Non-Haram Check", "3. شرعی اسکریننگ اور حرام سے بچاؤ", "٣. الفحص الشرعي الأولي", "3. शरीअत स्क्रीनिंग और हराम जांच"),
                LocalizedText4("Verify zero involvement of Riba, Gharar, Najis goods, deceit, or unlawful marketing.", "یقینی بنائیں کہ سود، غرر، حرام اجزاء اور فریب کا کوئی پہلو شامل نہ ہو۔", "تحقق من خلو النشاط من الربا والغرر والمحرمات.", "सूद, ग़रर, हराम वस्तु व धोखे से पूर्ण मुक्ति सत्यापित करें।")
            ),
            StartupProjectStepTemplate(
                4,
                LocalizedText4("4. Capital Source (Zero Riba)", "4. حلال سرمائے کا ذریعہ", "٤. مصدر رأس المال الحلال", "4. सूद-मुक्त पूंजी स्रोत"),
                LocalizedText4("Specify equity savings, family Qard Hasan, Mudarabah, or Musharakah funds without interest loans.", "ذاتی بچت، قرضِ حسنہ، مضاربہ یا مشارکہ کی تفصیل لکھیں (بغیر سودی قرض)۔", "حدد التمويل الذاتي أو القرض الحسن أو المضاربة دون قروض ربوية.", "निजी बचत, क़र्ज़-ए-हसन या मुशारकाह पूंजी बताएं।")
            ),
            StartupProjectStepTemplate(
                5,
                LocalizedText4("5. Contract Design (Musharakah / Murabaha / Ijarah)", "5. شرعی معاہدوں کی ساخت", "٥. هيكلة العقود الشرعية", "5. अनुबंध संरचना"),
                LocalizedText4("Choose exact primary contracts for suppliers, partners, staff, and customers.", "شراکت داروں، سپلائرز، عملے اور گاہکوں کے لیے درست شرعی عقود کا انتخاب کریں۔", "اختر العقود المناسبة للشركاء والموردين والعملاء.", "साझेदार, आपूर्तिकर्ता व ग्राहक अनुबंध चुनें।")
            ),
            StartupProjectStepTemplate(
                6,
                LocalizedText4("6. Cost Structure & Honest Pricing", "6. لاگت کا تخمینہ اور منصفانہ قیمت", "٦. التكلفة والتسعير العادل", "6. लागत संरचना और ईमानदार मूल्य"),
                LocalizedText4("Break down fixed + variable unit costs, fair market margin, and zero hidden charges.", "فی یونٹ لاگت، متوازن منافع اور خفیہ چارجز سے پاک قیمت درج کریں۔", "فصّل التكلفة الثابتة والمتغيرة وهامش الربح العادل.", "प्रति इकाई लागत, उचित लाभ और पारदर्शी मूल्य लिखें।")
            ),
            StartupProjectStepTemplate(
                7,
                LocalizedText4("7. Legal Registration & Tax/Licensing", "7. قانونی رجسٹریشن اور لائسنس", "٧. التسجيل النظامي والتراخيص", "7. कानूनी पंजीकरण और लाइसेंस"),
                LocalizedText4("List required local trade permits, NTN/tax registration, or food/health NOCs.", "مقامی تجارتی لائسنس، ٹیکس نمبر اور متعلقہ اداروں کی قانونی منظوری لکھیں۔", "سجل التراخيص البلدية والضريبية المطلوبة.", "आवश्यक व्यापार लाइसेंस, टैक्स व सुरक्षा अनुमति सूचीबद्ध करें।")
            ),
            StartupProjectStepTemplate(
                8,
                LocalizedText4("8. Transparent Accounting System", "8. شفاف اکاؤنٹنگ اور کھاتہ داری", "٨. النظام المحاسبي الشفاف", "8. पारदर्शी बहीखाता प्रणाली"),
                LocalizedText4("Separate personal household cash from business ledgers and document debts per Quran 2:282.", "گھریلو اور کاروباری حساب الگ کریں اور قرضوں کو 2:282 کے مطابق تحریر کرنے کا نظام بنائیں۔", "افصل ميزانية البيت عن المشروع ووثّق الديون (البقرة ٢٨٢).", "व्यक्तिगत और व्यापार खाते अलग करें व कर्ज़ लिखित रखें।")
            ),
            StartupProjectStepTemplate(
                9,
                LocalizedText4("9. Honest Branding & Modest Marketing", "9. سچی مارکیٹنگ اور برانڈنگ", "٩. التسويق الصادق والهوية", "9. ईमानदार ब्रांडिंग और मार्केटिंग"),
                LocalizedText4("Plan accurate product descriptions, truthful photos, and modest ethical advertising.", "درست تفصیلات، اصل تصاویر اور باحیا و سچی تشہیر کا منصوبہ لکھیں۔", "خطط لوصف صادق للمنتج وصور حقيقية وإعلان نزيه.", "सच्चे उत्पाद विवरण, असली फ़ोटो व नैतिक विज्ञापन योजना बनाएं।")
            ),
            StartupProjectStepTemplate(
                10,
                LocalizedText4("10. Ethical Operations & Employee Rights", "10. بااخلاق آپریشنز اور ملازمین کے حقوق", "١٠. التشغيل وحقوق الموظفين", "10. नैतिक संचालन और कर्मचारी अधिकार"),
                LocalizedText4("Commit to written contracts, workplace safety, fair wages, and timely payroll.", "تحریری ملازمت، محفوظ ماحول اور وقت پر تنخواہ ادا کرنے کا نظام تحریر کریں۔", "التزم بعقود العمل، السلامة المهنية، والأجور في موعدها.", "लिखित अनुबंध, कार्य सुरक्षा व समय पर वेतन सुनिश्चित करें।")
            ),
            StartupProjectStepTemplate(
                11,
                LocalizedText4("11. Customer Service, Warranty & Returns", "11. صارفین کے حقوق اور واپسی پالیسی", "١١. خدمة العملاء وضمان الاسترجاع", "11. ग्राहक सेवा और वापसी नीति"),
                LocalizedText4("Draft your defect disclosure, exchange window (Khiyar), and fast complaint resolution SLA.", "عیب ظاہر کرنے، واپسی و تبدیلی (خیارِ عیب) اور شکایت حل کرنے کی واضح پالیسی لکھیں۔", "اكتب سياسة كشف العيوب والاستبدال ومعالجة الشكاوى.", "दोष प्रकटीकरण व स्पष्ट वापसी-विनिमय नीति लिखें।")
            ),
            StartupProjectStepTemplate(
                12,
                LocalizedText4("12. Zakat, Charity & Growth Planning", "12. زکوٰۃ، صدقہ اور ترقی کی منصوبہ بندی", "١٢. التخطيط للزكاة والصدقة والنمو", "12. ज़कात, दान और विकास योजना"),
                LocalizedText4("Assign your annual lunar Hawl date for 2.5% Business Zakat and profit reinvestment.", "کاروباری زکوٰۃ (2.5%) کی سالانہ قمری تاریخ اور کاروبار بڑھانے کا منصوبہ طے کریں۔", "حدّد حول زكاة التجارة (٢.٥٪) وخطة إعادة استثمار الأرباح.", "2.5% व्यापारिक ज़कात की वार्षिक तिथि और पुनर्निवेश योजना तय करें।")
            ),
            StartupProjectStepTemplate(
                13,
                LocalizedText4("13. Pre-Launch Scholarly & Legal Checklist", "13. آغاز سے پہلے حتمی شرعی و قانونی جانچ", "١٣. المراجعة الشرعية والقانونية النهائية", "13. लॉन्च-पूर्व अंतिम मुफ़्ती व कानूनी जांच"),
                LocalizedText4("List specific custom contracts/terms to review with a qualified Mufti and legal counsel before launch.", "لانچ سے پہلے کن خاص معاہدوں یا شرائط پر مستند مفتی اور وکیل سے نظرِ ثانی کروانی ہے، درج کریں۔", "سجل العقود الخاصة التي ستراجعها مع مفتٍ مختص ومحامٍ قبل الإطلاق.", "लॉन्च से पहले मुफ़्ती व वकील से समीक्षा कराने वाले बिंदु लिखें।")
            )
        )
    }
}
