package com.example.ui.screens.prophets

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.prophets.ProphetRepository
import com.example.data.prophets.ProphetStory
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

@Composable
fun ProphetDetailScreen(
    story: ProphetStory,
    language: AppLanguage,
    isRead: Boolean,
    isBookmarked: Boolean,
    isQuizCompleted: Boolean,
    summaryMode: Boolean,
    onToggleSummaryMode: (Boolean) -> Unit,
    onToggleRead: () -> Unit,
    onToggleBookmark: () -> Unit,
    onQuizAnsweredCorrectly: () -> Unit,
    onNavigateToProphet: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val (prevId, nextId) = remember(story.id) { ProphetRepository.getAdjacentIds(story.id) }

    var selectedQuizOption by remember(story.id) { mutableStateOf<Int?>(null) }

    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = story.localizedName(language),
                language = language,
                onBackClick = onBack,
                actions = {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("prophets_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = AppLocalization.get("seerah_bookmark_action", language),
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = onToggleRead,
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("prophets_mark_read_btn")
                    ) {
                        Icon(
                            imageVector = if (isRead) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = AppLocalization.get("seerah_mark_read", language),
                            tint = if (isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, ProphetRepository.buildShareText(story, language))
                            }
                            runCatching {
                                context.startActivity(Intent.createChooser(shareIntent, story.localizedName(language)))
                            }
                        },
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("prophets_share_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = AppLocalization.get("seerah_share_title", language)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("prophets_detail_scroll")
        ) {
            // Header Card + Summary vs Detailed Mode Toggle
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${story.orderNumber} / 25 • ${story.arabicName}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = story.localizedName(language),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = story.localizedTitle(language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = !summaryMode,
                                onClick = { onToggleSummaryMode(false) },
                                label = {
                                    Text(
                                        when (language) {
                                            AppLanguage.URDU -> "تفصیلی مطالعہ"
                                            AppLanguage.ARABIC -> "القراءة المفصلة"
                                            AppLanguage.HINDI -> "विस्तृत मोड"
                                            AppLanguage.ENGLISH -> "Detailed Mode"
                                        }
                                    )
                                },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.minimumInteractiveComponentSize()
                            )
                            FilterChip(
                                selected = summaryMode,
                                onClick = { onToggleSummaryMode(true) },
                                label = {
                                    Text(
                                        when (language) {
                                            AppLanguage.URDU -> "خلاصہ موڈ"
                                            AppLanguage.ARABIC -> "وضع الملخص"
                                            AppLanguage.HINDI -> "सारांश मोड"
                                            AppLanguage.ENGLISH -> "Summary Mode"
                                        }
                                    )
                                },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.minimumInteractiveComponentSize().testTag("prophets_mode_toggle")
                            )
                        }
                    }
                }
            }

            // Section 1: Introduction (Summary & Detailed)
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "1. تعارف"
                        AppLanguage.ARABIC -> "1. المقدمة"
                        AppLanguage.HINDI -> "1. परिचय"
                        AppLanguage.ENGLISH -> "1. Introduction"
                    },
                    modifier = Modifier.testTag("prophets_sec_intro")
                ) {
                    val text = if (summaryMode) {
                        story.summaryIntro[language] ?: story.summaryIntro[AppLanguage.ENGLISH].orEmpty()
                    } else {
                        story.detailedIntro[language] ?: story.detailedIntro[AppLanguage.ENGLISH].orEmpty()
                    }
                    Text(text = text, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Section 2: Historical & Quranic Context
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "2. قرآنی و تاریخی پس منظر"
                        AppLanguage.ARABIC -> "2. السياق القرآني والتاريخي"
                        AppLanguage.HINDI -> "2. ऐतिहासिक व क़ुरआनी संदर्भ"
                        AppLanguage.ENGLISH -> "2. Historical & Quranic Context"
                    },
                    modifier = Modifier.testTag("prophets_sec_context")
                ) {
                    Text(
                        text = story.quranicContext[language] ?: story.quranicContext[AppLanguage.ENGLISH].orEmpty(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Section 3: Main Events
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "3. اہم واقعات"
                        AppLanguage.ARABIC -> "3. أبرز الأحداث"
                        AppLanguage.HINDI -> "3. प्रमुख घटनाएँ"
                        AppLanguage.ENGLISH -> "3. Main Events"
                    },
                    modifier = Modifier.testTag("prophets_sec_events")
                ) {
                    val events = story.mainEvents[language] ?: story.mainEvents[AppLanguage.ENGLISH].orEmpty()
                    events.forEachIndexed { idx, ev ->
                        Text(
                            text = "${idx + 1}. $ev",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                }
            }

            // Section 4: Important People & Places
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "4. اہم شخصیات اور مقامات"
                        AppLanguage.ARABIC -> "4. الشخصيات والأماكن"
                        AppLanguage.HINDI -> "4. प्रमुख व्यक्ति व स्थान"
                        AppLanguage.ENGLISH -> "4. Important People & Places"
                    },
                    modifier = Modifier.testTag("prophets_sec_people_places")
                ) {
                    val items = story.peopleAndPlaces[language] ?: story.peopleAndPlaces[AppLanguage.ENGLISH].orEmpty()
                    items.forEach { p ->
                        Text(
                            text = "• $p",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            // Section 5: Relevant Quranic Verses (Verbatim Offline Resolution from QuranData!)
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "5. متعلقہ قرآنی آیات (مستند عربی و ترجمہ)"
                        AppLanguage.ARABIC -> "5. الآيات القرآنية الكريمة"
                        AppLanguage.HINDI -> "5. संबंधित क़ुरआनी आयतें (मूल व अनुवाद)"
                        AppLanguage.ENGLISH -> "5. Relevant Quranic Verses"
                    },
                    modifier = Modifier.testTag("prophets_sec_verses")
                ) {
                    story.verseRefs.forEach { vRef ->
                        val resolved = remember(story.id, vRef.surah, vRef.startAyah, vRef.endAyah, language) {
                            ProphetRepository.resolveQuranVerses(
                                context = context,
                                surah = vRef.surah,
                                startAyah = vRef.startAyah,
                                endAyah = vRef.endAyah,
                                language = language
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    runCatching { uriHandler.openUri("https://quran.com/${vRef.surah}/${vRef.startAyah}") }
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Quran ${vRef.referenceLabel} — ${vRef.topic[language] ?: vRef.topic[AppLanguage.ENGLISH].orEmpty()}",
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.OpenInNew,
                                        contentDescription = "Open Quran Reference",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                                if (resolved.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    resolved.take(if (summaryMode) 2 else 5).forEach { ay ->
                                        Text(
                                            text = "${ay.arabicText} ﴿${ay.ayah}﴾",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (ay.translationText.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "[${ay.surah}:${ay.ayah}] ${ay.translationText}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 6: Verified Hadith References (Explicitly honest when unrecorded)
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "6. مستند احادیث کے حوالے"
                        AppLanguage.ARABIC -> "6. الأحاديث النبوية الصحيحة"
                        AppLanguage.HINDI -> "6. प्रामाणिक हदीस संदर्भ"
                        AppLanguage.ENGLISH -> "6. Verified Hadith References"
                    },
                    modifier = Modifier.testTag("prophets_sec_hadith")
                ) {
                    if (story.hadithRefs.isEmpty()) {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "اس نبی کی سیرت بنیادی طور پر قرآنِ مجید کی مذکورہ آیات سے ثابت ہے؛ اس حوالے سے کوئی الگ مرفوع صحیح حدیث درکار یا منقول نہیں۔"
                                AppLanguage.ARABIC -> "تثبت قصة هذا النبي أساساً بالآيات القرآنية الصريحة أعلاه، ولا توجد رواية مرفوعة صحيحة مستقلة زائدة عنها."
                                AppLanguage.HINDI -> "इस नबी का विवरण मुख्यतः ऊपर दी गई क़ुरआनी आयतों से प्रमाणित है; इसके अतिरिक्त कोई अलग मर्फ़ू सहीह हदीस आवश्यक या उद्धृत नहीं है।"
                                AppLanguage.ENGLISH -> "This prophet's account rests directly upon the Quranic citations above; no separate distinct Hadith narration is additionally needed or cited."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        story.hadithRefs.forEach { h ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { runCatching { uriHandler.openUri(h.url) } }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = h.citation,
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Icon(
                                            imageVector = Icons.Default.OpenInNew,
                                            contentDescription = "Open Hadith Citation",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = h.summary[language] ?: h.summary[AppLanguage.ENGLISH].orEmpty(),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 7: Lessons & Guidance
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "7. علمی و عملی اسباق"
                        AppLanguage.ARABIC -> "7. الدروس والعبر التربوية"
                        AppLanguage.HINDI -> "7. शिक्षाएँ एवं मार्गदर्शन"
                        AppLanguage.ENGLISH -> "7. Lessons & Guidance"
                    },
                    modifier = Modifier.testTag("prophets_sec_lessons")
                ) {
                    val lessons = story.lessons[language] ?: story.lessons[AppLanguage.ENGLISH].orEmpty()
                    lessons.forEach { lesson ->
                        Text(
                            text = "• $lesson",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                }
            }

            // Section 8: Important Duas Mentioned in the Quran
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "8. قرآن میں مذکور دعائیں"
                        AppLanguage.ARABIC -> "8. الأدعية القرآنية المأثورة"
                        AppLanguage.HINDI -> "8. क़ुरआन में वर्णित दुआएँ"
                        AppLanguage.ENGLISH -> "8. Important Duas in the Quran"
                    },
                    modifier = Modifier.testTag("prophets_sec_duas")
                ) {
                    if (story.quranicDuas.isEmpty()) {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "اس نبی سے منسوب کوئی مخصوص لفظی دعا قرآن میں صراحتاً نقل نہیں ہوئی۔"
                                AppLanguage.ARABIC -> "لم يُنقل دعاء نصّي محدد عن هذا النبي في القرآن الكريم."
                                AppLanguage.HINDI -> "इस नबी की कोई विशिष्ट शाब्दिक दुआ क़ुरआन में अलग से उद्धृत नहीं है।"
                                AppLanguage.ENGLISH -> "No separate verbatim supplication text is recorded in the Quran for this prophet."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        story.quranicDuas.forEach { d ->
                            val resolved = remember(story.id, d.surah, d.startAyah, d.endAyah, language) {
                                ProphetRepository.resolveQuranVerses(context, d.surah, d.startAyah, d.endAyah, language)
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "${d.title[language] ?: d.title[AppLanguage.ENGLISH].orEmpty()} (${d.referenceLabel})",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    resolved.forEach { ay ->
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = ay.arabicText, style = MaterialTheme.typography.bodyLarge)
                                        if (ay.translationText.isNotBlank()) {
                                            Text(text = ay.translationText, style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 9: References & Source Reliability Notes
            item {
                SectionCard(
                    title = when (language) {
                        AppLanguage.URDU -> "9. مآخذ اور استنادی وضاحت"
                        AppLanguage.ARABIC -> "9. المصادر وملاحظات الموثوقية"
                        AppLanguage.HINDI -> "9. स्रोत व विश्वसनीयता टिप्पणियाँ"
                        AppLanguage.ENGLISH -> "9. References & Reliability Notes"
                    },
                    modifier = Modifier.testTag("prophets_sec_references")
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = story.reliabilityNotes[language] ?: story.reliabilityNotes[AppLanguage.ENGLISH].orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Verified Quranic Reflection Quiz Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("prophets_quiz_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Quiz, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "مصدقہ قرآنی کوئز"
                                    AppLanguage.ARABIC -> "اختبار الفهم القرآني الموثق"
                                    AppLanguage.HINDI -> "प्रामाणिक क़ुरआनी प्रश्नोत्तरी"
                                    AppLanguage.ENGLISH -> "Verified Reflection Quiz"
                                } + if (isQuizCompleted) " ✓" else "",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = story.quizQuestion.question[language] ?: story.quizQuestion.question[AppLanguage.ENGLISH].orEmpty(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val opts = story.quizQuestion.options[language] ?: story.quizQuestion.options[AppLanguage.ENGLISH].orEmpty()
                        opts.forEachIndexed { idx, opt ->
                            val isSelected = selectedQuizOption == idx
                            val isCorrect = idx == story.quizQuestion.correctIndex
                            OutlinedButton(
                                onClick = {
                                    selectedQuizOption = idx
                                    if (isCorrect) {
                                        onQuizAnsweredCorrectly()
                                    }
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = when {
                                        isSelected && isCorrect -> MaterialTheme.colorScheme.primaryContainer
                                        isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("prophets_quiz_opt_$idx")
                            ) {
                                Text(text = opt, modifier = Modifier.fillMaxWidth())
                            }
                        }

                        if (selectedQuizOption != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = story.quizQuestion.explanation[language]
                                        ?: story.quizQuestion.explanation[AppLanguage.ENGLISH].orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Previous / Next Navigation
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { prevId?.let { onNavigateToProphet(it) } },
                        enabled = prevId != null,
                        modifier = Modifier.weight(1f).height(48.dp).testTag("prophets_prev_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(AppLocalization.get("seerah_prev_article", language))
                    }
                    Button(
                        onClick = { nextId?.let { onNavigateToProphet(it) } },
                        enabled = nextId != null,
                        modifier = Modifier.weight(1f).height(48.dp).testTag("prophets_next_btn")
                    ) {
                        Text(AppLocalization.get("seerah_next_article", language))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
