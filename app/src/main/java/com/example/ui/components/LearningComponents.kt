package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.QuranAudioManager
import com.example.ui.theme.QuranNaskhFontFamily

data class DrawingLine(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

/**
 * Interactive Touch-Based Arabic Letter & Word Writing Pad
 */
@Composable
fun ArabicWritingPad(
    targetLetterOrWord: String,
    guidanceHint: String,
    modifier: Modifier = Modifier,
    language: AppLanguage = AppLanguage.ENGLISH
) {
    val lines = remember { mutableStateListOf<DrawingLine>() }
    var currentLine by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var selectedColor by remember { mutableStateOf(Color(0xFF10B981)) }
    var strokeWidth by remember { mutableFloatStateOf(14f) }

    val titleText = when (language) {
        AppLanguage.URDU -> "مشقِ کتابت (ٹچ سے لکھیں)"
        AppLanguage.ARABIC -> "التدريب على الكتابة باللمس"
        AppLanguage.HINDI -> "अरबी लेखन अभ्यास (टच पैड)"
        AppLanguage.ENGLISH -> "Touch-Based Writing Practice"
    }
    val fineLabel = when (language) {
        AppLanguage.URDU -> "باریک"
        AppLanguage.ARABIC -> "رفيع"
        AppLanguage.HINDI -> "पतला"
        AppLanguage.ENGLISH -> "Fine"
    }
    val medLabel = when (language) {
        AppLanguage.URDU -> "درمیانہ"
        AppLanguage.ARABIC -> "متوسط"
        AppLanguage.HINDI -> "मध्यम"
        AppLanguage.ENGLISH -> "Medium"
    }
    val boldLabel = when (language) {
        AppLanguage.URDU -> "موٹا"
        AppLanguage.ARABIC -> "عريض"
        AppLanguage.HINDI -> "मोटा"
        AppLanguage.ENGLISH -> "Bold"
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = guidanceHint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = {
                        lines.clear()
                        currentLine = emptyList()
                    },
                    modifier = Modifier
                        .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                        .testTag("clear_writing_canvas_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = com.example.localization.AppLocalization.get("learning_clear_canvas", language),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        .border(
                            1.5.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(14.dp)
                        )
                        .pointerInput(selectedColor, strokeWidth) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentLine = listOf(offset)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentLine = currentLine + change.position
                                },
                                onDragEnd = {
                                    if (currentLine.isNotEmpty()) {
                                        lines.add(DrawingLine(currentLine, selectedColor, strokeWidth))
                                        currentLine = emptyList()
                                    }
                                }
                            )
                        }
                        .testTag("arabic_tracing_canvas")
                ) {
                    Text(
                        text = targetLetterOrWord,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontFamily = QuranNaskhFontFamily,
                            fontSize = 120.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        textAlign = TextAlign.Center
                    )

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        lines.forEach { line ->
                            if (line.points.size > 1) {
                                val path = Path().apply {
                                    moveTo(line.points.first().x, line.points.first().y)
                                    for (i in 1 until line.points.size) {
                                        lineTo(line.points[i].x, line.points[i].y)
                                    }
                                }
                                drawPath(
                                    path = path,
                                    color = line.color,
                                    style = Stroke(
                                        width = line.strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }

                        if (currentLine.size > 1) {
                            val path = Path().apply {
                                moveTo(currentLine.first().x, currentLine.first().y)
                                for (i in 1 until currentLine.size) {
                                    lineTo(currentLine[i].x, currentLine[i].y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = selectedColor,
                                style = Stroke(
                                    width = strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val inkColors = listOf(
                        Color(0xFF10B981),
                        Color(0xFFD97706),
                        Color(0xFF2563EB),
                        Color(0xFFDC2626)
                    )
                    inkColors.forEach { c ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .sizeIn(minWidth = 36.dp, minHeight = 36.dp)
                                .clip(CircleShape)
                                .clickable { selectedColor = c }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(c)
                                    .then(
                                        if (selectedColor == c) {
                                            Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                        } else {
                                            Modifier
                                        }
                                    )
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = { strokeWidth = 10f },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(fineLabel, fontSize = 11.sp)
                    }
                    FilledTonalButton(
                        onClick = { strokeWidth = 18f },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(medLabel, fontSize = 11.sp)
                    }
                    FilledTonalButton(
                        onClick = { strokeWidth = 26f },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(boldLabel, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

/**
 * Interactive Flashcard for Quranic Words & Vocabulary
 */
@Composable
fun QuranicWordFlashcard(
    word: QuranicWord,
    language: AppLanguage,
    isLearned: Boolean,
    onToggleLearned: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isFlipped by remember { mutableStateOf(false) }

    val flipPrompt = when (language) {
        AppLanguage.URDU -> if (isFlipped) "چھپانے کے لیے ٹیپ کریں" else "مثال دیکھنے کے لیے ٹیپ کریں"
        AppLanguage.ARABIC -> if (isFlipped) "اضغط للإخفاء" else "اضغط لعرض المثال"
        AppLanguage.HINDI -> if (isFlipped) "छिपाने के लिए टैप करें" else "उदाहरण के लिए टैप करें"
        AppLanguage.ENGLISH -> if (isFlipped) "Tap to hide details" else "Tap to flip"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFlipped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { isFlipped = !isFlipped }
            .testTag("flashcard_${word.id}")
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${word.category} • ${word.frequencyInQuran}x",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            QuranAudioManager.speakArabic(context, word.arabic, word.transliteration)
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = com.example.localization.AppLocalization.get("play", language),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Checkbox(
                        checked = isLearned,
                        onCheckedChange = { onToggleLearned() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = word.arabic,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = QuranNaskhFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 38.sp
                ),
                color = if (isFlipped) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = word.transliteration,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            val meaningText = when (language) {
                AppLanguage.URDU -> word.meaningUr
                AppLanguage.ARABIC -> word.meaningAr
                AppLanguage.HINDI -> word.meaningHi
                AppLanguage.ENGLISH -> word.meaningEn
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = meaningText,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = flipPrompt,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = isFlipped) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = word.exampleAyah,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = QuranNaskhFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = word.exampleSurahRef,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

/**
 * Word-by-Word Quran Ayah Display Component
 */
@Composable
fun WordByWordAyahCard(
    ayahData: WordByWordAyah,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val ayahBadge = when (language) {
        AppLanguage.URDU -> "سورۃ ${ayahData.surahNumber} • آیت ${ayahData.ayahNumber}"
        AppLanguage.ARABIC -> "سورة ${ayahData.surahNumber} • آية ${ayahData.ayahNumber}"
        AppLanguage.HINDI -> "सूरह ${ayahData.surahNumber} • आयत ${ayahData.ayahNumber}"
        AppLanguage.ENGLISH -> "Surah ${ayahData.surahNumber} • Ayah ${ayahData.ayahNumber}"
    }
    val analysisLabel = when (language) {
        AppLanguage.URDU -> "لفظ بہ لفظ تجزیہ:"
        AppLanguage.ARABIC -> "التحليل اللفظي للآية:"
        AppLanguage.HINDI -> "शब्द-दर-शब्द विश्लेषण:"
        AppLanguage.ENGLISH -> "Word-by-Word Analysis:"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = ayahBadge,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = ayahData.fullArabic,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = QuranNaskhFontFamily,
                    fontSize = 28.sp,
                    lineHeight = 48.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = analysisLabel,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                ayahData.words.forEach { wp ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.widthIn(min = 84.dp, max = 140.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                        ) {
                            Text(
                                text = wp.arabic,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = QuranNaskhFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = wp.translit,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val wordMeaning = when (language) {
                                AppLanguage.URDU -> wp.meaningUr
                                AppLanguage.ARABIC -> wp.meaningAr
                                AppLanguage.HINDI -> wp.meaningHi
                                AppLanguage.ENGLISH -> wp.meaningEn
                            }
                            Text(
                                text = wordMeaning,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (language != AppLanguage.ARABIC) {
                val fullTrans = when (language) {
                    AppLanguage.URDU -> ayahData.translationUr
                    AppLanguage.HINDI -> ayahData.translationHi
                    AppLanguage.ENGLISH -> ayahData.translationEn
                    AppLanguage.ARABIC -> ""
                }
                if (fullTrans.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = fullTrans,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Audio Recitation and Repeat Controller Bar
 */
@Composable
fun AudioPronunciationController(
    surahNumber: Int,
    ayahNumber: Int,
    modifier: Modifier = Modifier,
    language: AppLanguage = AppLanguage.ENGLISH
) {
    val context = LocalContext.current
    var repeatTarget by remember { mutableIntStateOf(1) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }

    val isPlaying = QuranAudioManager.isAudioPlaying.value &&
        QuranAudioManager.currentPlayingSurahNumber.value == surahNumber &&
        QuranAudioManager.currentPlayingAyahNumber.value == ayahNumber
    val isBuffering = QuranAudioManager.isBuffering.value &&
        QuranAudioManager.currentPlayingSurahNumber.value == surahNumber &&
        QuranAudioManager.currentPlayingAyahNumber.value == ayahNumber
    val currentLoop = QuranAudioManager.currentLoopCount.intValue

    DisposableEffect(surahNumber, ayahNumber) {
        onDispose {
            if (QuranAudioManager.currentPlayingSurahNumber.value == surahNumber &&
                QuranAudioManager.currentPlayingAyahNumber.value == ayahNumber
            ) {
                QuranAudioManager.stopMedia()
            }
        }
    }

    val statusText = when {
        isBuffering -> when (language) {
            AppLanguage.URDU -> "تلاوت لوڈ ہو رہی ہے..."
            AppLanguage.ARABIC -> "جاري تحميل التلاوة..."
            AppLanguage.HINDI -> "तिलावत लोड हो रही है..."
            AppLanguage.ENGLISH -> "Connecting Recitation..."
        }
        isPlaying -> when (language) {
            AppLanguage.URDU -> "تلاوت جاری ہے..."
            AppLanguage.ARABIC -> "التلاوة قيد التشغيل..."
            AppLanguage.HINDI -> "तिलावत चल रही है..."
            AppLanguage.ENGLISH -> "Playing Recitation..."
        }
        else -> when (language) {
            AppLanguage.URDU -> "سنیں اور دہرائیں"
            AppLanguage.ARABIC -> "استمع وكرر"
            AppLanguage.HINDI -> "सुनें और दोहराएँ"
            AppLanguage.ENGLISH -> "Listen & Pronounce"
        }
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = {
                        if (isPlaying || isBuffering) {
                            QuranAudioManager.stopMedia()
                        } else {
                            QuranAudioManager.playAyah(
                                context = context,
                                surahNumber = surahNumber,
                                ayahNumber = ayahNumber,
                                speed = playbackSpeed,
                                repeatTarget = repeatTarget
                            )
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    if (isBuffering) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = com.example.localization.AppLocalization.get(
                                if (isPlaying) "pause" else "play",
                                language
                            ),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${if (repeatTarget == Int.MAX_VALUE) "∞" else "$currentLoop/${repeatTarget}x"} • ${playbackSpeed}x",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilledTonalButton(
                    onClick = {
                        repeatTarget = when (repeatTarget) {
                            1 -> 3
                            3 -> 5
                            5 -> 10
                            else -> 1
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("${repeatTarget}x", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                FilledTonalButton(
                    onClick = {
                        playbackSpeed = if (playbackSpeed == 1.0f) 0.75f else 1.0f
                        if (isPlaying) {
                            QuranAudioManager.playAyah(
                                context = context,
                                surahNumber = surahNumber,
                                ayahNumber = ayahNumber,
                                speed = playbackSpeed,
                                repeatTarget = repeatTarget
                            )
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(if (playbackSpeed == 0.75f) "0.75x" else "1.0x", fontSize = 11.sp)
                }
            }
        }
    }
}

/**
 * Interactive Academy Assessment Quiz Card with Instant Quadrilingual Explanation
 */
@Composable
fun InteractiveQuizCard(
    quiz: AcademyQuizQuestion,
    language: AppLanguage,
    onQuizPassed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOptionIndex by remember(quiz.id) { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember(quiz.id) { mutableStateOf(false) }

    val questionText = when (language) {
        AppLanguage.URDU -> quiz.questionUr
        AppLanguage.ARABIC -> quiz.questionAr
        AppLanguage.HINDI -> quiz.questionHi
        AppLanguage.ENGLISH -> quiz.questionEn
    }

    val options = when (language) {
        AppLanguage.URDU -> quiz.optionsUr
        AppLanguage.ARABIC -> quiz.optionsAr
        AppLanguage.HINDI -> quiz.optionsHi
        AppLanguage.ENGLISH -> quiz.optionsEn
    }

    val explanationText = when (language) {
        AppLanguage.URDU -> quiz.explanationUr
        AppLanguage.ARABIC -> quiz.explanationAr
        AppLanguage.HINDI -> quiz.explanationHi
        AppLanguage.ENGLISH -> quiz.explanationEn
    }

    val checkAnswerLabel = when (language) {
        AppLanguage.URDU -> "جواب چیک کریں"
        AppLanguage.ARABIC -> "تحقق من الإجابة"
        AppLanguage.HINDI -> "उत्तर जाँचें"
        AppLanguage.ENGLISH -> "Check Answer"
    }

    val correctLabel = when (language) {
        AppLanguage.URDU -> "ماشاء اللہ! درست جواب۔"
        AppLanguage.ARABIC -> "أحسنت! إجابة صحيحة."
        AppLanguage.HINDI -> "बिल्कुल सही उत्तर!"
        AppLanguage.ENGLISH -> "Correct Answer."
    }

    val wrongPrefix = when (language) {
        AppLanguage.URDU -> "درست آپشن: "
        AppLanguage.ARABIC -> "الإجابة الصحيحة هي: "
        AppLanguage.HINDI -> "सही विकल्प है: "
        AppLanguage.ENGLISH -> "The correct option is: "
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("quiz_card_${quiz.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            ) {
                Text(
                    text = quiz.category,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!quiz.arabicPrompt.isNullOrBlank()) {
                Text(
                    text = quiz.arabicPrompt,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = QuranNaskhFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            Text(
                text = questionText,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            options.forEachIndexed { idx, opt ->
                val isSelected = selectedOptionIndex == idx
                val isCorrect = idx == quiz.correctIndex

                val optionBgColor by animateColorAsState(
                    targetValue = when {
                        !isAnswerSubmitted && isSelected -> MaterialTheme.colorScheme.primaryContainer
                        isAnswerSubmitted && isCorrect -> MaterialTheme.colorScheme.tertiaryContainer
                        isAnswerSubmitted && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    },
                    label = "opt_bg"
                )

                Surface(
                    onClick = {
                        if (!isAnswerSubmitted) {
                            selectedOptionIndex = idx
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    color = optionBgColor,
                    border = BorderStroke(
                        1.dp,
                        when {
                            isAnswerSubmitted && isCorrect -> MaterialTheme.colorScheme.primary
                            isAnswerSubmitted && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> Color.Transparent
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("quiz_option_${quiz.id}_$idx")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "${('A' + idx)}. ",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = opt,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isAnswerSubmitted) {
                            if (isCorrect) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            } else if (isSelected) {
                                Icon(
                                    Icons.Default.Cancel,
                                    contentDescription = "Wrong",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isAnswerSubmitted) {
                Button(
                    onClick = {
                        if (selectedOptionIndex != null) {
                            isAnswerSubmitted = true
                            if (selectedOptionIndex == quiz.correctIndex) {
                                onQuizPassed()
                            }
                        }
                    },
                    enabled = selectedOptionIndex != null,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .testTag("quiz_submit_${quiz.id}")
                ) {
                    Text(checkAnswerLabel, fontWeight = FontWeight.Bold)
                }
            } else {
                val isUserRight = selectedOptionIndex == quiz.correctIndex
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isUserRight) {
                        MaterialTheme.colorScheme.tertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isUserRight) correctLabel else "$wrongPrefix${('A' + quiz.correctIndex)}",
                            fontWeight = FontWeight.Bold,
                            color = if (isUserRight) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = explanationText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}



/**
 * Displays verified Arabic prayer / dhikr recitation with transliteration, translation, explanation & audio playback.
 */
@Composable
fun PrayerStepRecitationCard(
    step: PrayerStepRecitation,
    index: Int,
    language: AppLanguage,
    onPlayArabic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = "$index. ${step.labelFor(language)}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = onPlayArabic,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = com.example.localization.AppLocalization.get("play", language),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = step.arabic,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = QuranNaskhFontFamily,
                    fontSize = 24.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = step.transliteration,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = step.translationFor(language),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )

            val explanation = step.explanationFor(language)
            if (explanation.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Displays authentic Quran / Hadith / Classical Source references clearly.
 */
@Composable
fun IslamicReferenceSection(
    references: List<IslamicReference>,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    if (references.isEmpty()) return
    val heading = when (language) {
        AppLanguage.URDU -> "مستند حوالہ جات (قرآن و حدیث)"
        AppLanguage.ARABIC -> "المصادر والمراجع الموثقة"
        AppLanguage.HINDI -> "प्रामाणिक संदर्भ (क़ुरआन व हदीस)"
        AppLanguage.ENGLISH -> "Verified Sources & References"
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = heading,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            references.forEach { ref ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "${ref.sourceName} — ${ref.citation}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val note = ref.noteFor(language)
                        if (note.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = note,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
