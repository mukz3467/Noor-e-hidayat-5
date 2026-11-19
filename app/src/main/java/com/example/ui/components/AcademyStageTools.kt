package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.MediaAndLearningData
import com.example.data.repository.QuranAudioManager
import com.example.data.repository.QuranLearningRepository
import com.example.ui.theme.QuranNaskhFontFamily

/**
 * Stage 7 Embedded Workspace: 15-Lesson Noorani Qaida, Tajweed Rules, Arabic Writing Pad, Audio & Makharij.
 */
fun LazyListScope.stage7QaidaWorkspace(
    context: Context,
    language: AppLanguage,
    progress: StudentAcademyProgress,
    activeToolTab: Int,
    onSelectToolTab: (Int) -> Unit,
    selectedQaidaIndex: Int,
    onSelectQaidaIndex: (Int) -> Unit,
    selectedWritingLetter: Pair<String, String>,
    onSelectWritingLetter: (Pair<String, String>) -> Unit,
    onProgressChanged: () -> Unit
) {
    val tabLabels = when (language) {
        AppLanguage.URDU -> listOf("نورانی قاعدہ (15)", "قواعدِ تجوید", "مشقِ کتابت", "مخارج و آواز")
        AppLanguage.ARABIC -> listOf("القاعدة النورانية (15)", "أحكام التجويد", "الكتابة باللمس", "المخارج والصوت")
        AppLanguage.HINDI -> listOf("नूरानी क़ाइदा (15)", "तजवीद नियम", "अरबी लेखन", "मख़ारिज व उच्चारण")
        AppLanguage.ENGLISH -> listOf("Qaida (15 Lessons)", "Tajweed Rules", "Writing Pad", "Makharij & Audio")
    }

    item {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(tabLabels) { idx, label ->
                FilterChip(
                    selected = activeToolTab == idx,
                    onClick = { onSelectToolTab(idx) },
                    label = { Text(label, fontWeight = if (activeToolTab == idx) FontWeight.Bold else FontWeight.Normal) },
                    colors = noorSelectableChipColors(),
                    modifier = Modifier.heightIn(min = 48.dp).testTag("stage7_tab_$idx")
                )
            }
        }
    }

    when (activeToolTab) {
        0 -> {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(QuranLearningRepository.qaidaLessons) { idx, ql ->
                        val isSel = idx == selectedQaidaIndex
                        val isDone = progress.completedLessonIds.contains(ql.id)
                        FilterChip(
                            selected = isSel,
                            onClick = { onSelectQaidaIndex(idx) },
                            label = {
                                Text(
                                    text = "L${ql.lessonNumber}${if (isDone) " ✓" else ""}",
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.heightIn(min = 48.dp).testTag("qaida_lesson_chip_${ql.lessonNumber}")
                        )
                    }
                }
            }

            val curLesson = QuranLearningRepository.qaidaLessons.getOrElse(selectedQaidaIndex) {
                QuranLearningRepository.qaidaLessons.first()
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> curLesson.titleUr
                                    AppLanguage.ARABIC -> curLesson.titleAr
                                    AppLanguage.HINDI -> curLesson.titleHi
                                    AppLanguage.ENGLISH -> curLesson.titleEn
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                            val doneLabel = when (language) {
                                AppLanguage.URDU -> "مکمل ✓"
                                AppLanguage.ARABIC -> "مكتمل ✓"
                                AppLanguage.HINDI -> "पूर्ण ✓"
                                AppLanguage.ENGLISH -> "Done ✓"
                            }
                            val localizedQaidaTitle = when (language) {
                                AppLanguage.URDU -> curLesson.titleUr
                                AppLanguage.ARABIC -> curLesson.titleAr
                                AppLanguage.HINDI -> curLesson.titleHi
                                AppLanguage.ENGLISH -> curLesson.titleEn
                            }
                            Button(
                                onClick = {
                                    QuranLearningRepository.markLessonCompleted(
                                        context = context,
                                        lessonId = curLesson.id,
                                        lessonTitle = localizedQaidaTitle,
                                        moduleId = "stage_7",
                                        stageNumber = 7
                                    )
                                    onProgressChanged()
                                    Toast.makeText(context, "${curLesson.lessonNumber} ✓", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.heightIn(min = 40.dp)
                            ) {
                                Text(doneLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> curLesson.descriptionUr
                                AppLanguage.ARABIC -> curLesson.descriptionAr
                                AppLanguage.HINDI -> curLesson.descriptionHi
                                AppLanguage.ENGLISH -> curLesson.descriptionEn
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            items(curLesson.items) { qItem ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            QuranAudioManager.speakArabic(context, qItem.arabic, qItem.transliteration)
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            ) {
                                Text(
                                    text = qItem.arabic,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontFamily = QuranNaskhFontFamily,
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = qItem.transliteration,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                val note = when (language) {
                                    AppLanguage.URDU -> qItem.noteUr
                                    AppLanguage.ARABIC -> qItem.noteAr
                                    AppLanguage.HINDI -> qItem.noteHi
                                    AppLanguage.ENGLISH -> qItem.noteEn
                                }
                                if (note.isNotBlank()) {
                                    Text(
                                        text = note,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                QuranAudioManager.speakArabic(context, qItem.arabic, qItem.transliteration)
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = com.example.localization.AppLocalization.get("play", language),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        1 -> {
            items(QuranLearningRepository.advancedTajweedRules) { rule ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> rule.titleUr
                                AppLanguage.ARABIC -> rule.titleAr
                                AppLanguage.HINDI -> rule.titleHi
                                AppLanguage.ENGLISH -> rule.titleEn
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val exp = when (language) {
                            AppLanguage.URDU -> rule.explanationUr
                            AppLanguage.ARABIC -> rule.explanationAr.ifBlank { rule.titleAr }
                            AppLanguage.HINDI -> rule.explanationHi
                            AppLanguage.ENGLISH -> rule.explanationEn
                        }
                        Text(
                            text = exp,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        rule.examples.forEach { eg ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = eg.arabicText,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontFamily = QuranNaskhFontFamily,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        val noteText = eg.noteFor(language)
                                        Text(
                                            text = if (noteText.isNotBlank()) "$noteText (${eg.surahReference})" else eg.surahReference,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            QuranAudioManager.speakArabic(context, eg.arabicText, eg.surahReference)
                                        },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = com.example.localization.AppLocalization.get("play", language),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        2 -> {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(QuranLearningRepository.writingExercises) { (letter, hint) ->
                        val isSel = letter == selectedWritingLetter.first
                        FilterChip(
                            selected = isSel,
                            onClick = { onSelectWritingLetter(Pair(letter, hint)) },
                            label = { Text(letter, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.heightIn(min = 48.dp)
                        )
                    }
                }
            }

            item {
                ArabicWritingPad(
                    targetLetterOrWord = selectedWritingLetter.first,
                    guidanceHint = QuranLearningRepository.writingGuidanceFor(selectedWritingLetter.first, language),
                    language = language
                )
            }
        }

        else -> {
            item {
                AudioPronunciationController(surahNumber = 1, ayahNumber = 1, language = language)
            }
            items(MediaAndLearningData.arabicAlphabet) { letter ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            QuranAudioManager.speakArabic(context, letter.letter, letter.nameTranslit)
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = letter.letter,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontFamily = QuranNaskhFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${letter.nameArabic} (${letter.nameTranslit})",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                val makhraj = when (language) {
                                    AppLanguage.URDU -> letter.makhrajUr
                                    AppLanguage.ARABIC -> letter.makhrajAr.ifBlank { letter.nameArabic }
                                    AppLanguage.HINDI -> letter.makhrajHi
                                    AppLanguage.ENGLISH -> letter.makhrajEn
                                }
                                Text(
                                    text = makhraj,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                QuranAudioManager.speakArabic(context, letter.letter, letter.nameTranslit)
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = com.example.localization.AppLocalization.get("play", language),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Stage 8 Embedded Workspace: 25 Quranic Words, 28 Word-by-Word Ayahs across 6 Surahs, & Surah Introductions.
 */
fun LazyListScope.stage8QuranUnderstandingWorkspace(
    context: Context,
    language: AppLanguage,
    progress: StudentAcademyProgress,
    activeToolTab: Int,
    onSelectToolTab: (Int) -> Unit,
    selectedWbwSurahFilter: Int,
    onSelectWbwSurahFilter: (Int) -> Unit,
    onProgressChanged: () -> Unit
) {
    val tabLabels = when (language) {
        AppLanguage.URDU -> listOf("لفظ بہ لفظ (28)", "قرآنی الفاظ (25)", "اسبابِ نزول و تعارف")
        AppLanguage.ARABIC -> listOf("كلمة بكلمة (28)", "مفردات القرآن (25)", "أسباب النزول")
        AppLanguage.HINDI -> listOf("शब्द-दर-शब्द (28)", "क़ुरआनी शब्द (25)", "असबाब-ए-नुज़ूल")
        AppLanguage.ENGLISH -> listOf("Word-by-Word (28)", "Quranic Words (25)", "Surah Intros")
    }

    item {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(tabLabels) { idx, label ->
                FilterChip(
                    selected = activeToolTab == idx,
                    onClick = { onSelectToolTab(idx) },
                    label = { Text(label, fontWeight = if (activeToolTab == idx) FontWeight.Bold else FontWeight.Normal) },
                    colors = noorSelectableChipColors(),
                    modifier = Modifier.heightIn(min = 48.dp).testTag("stage8_tab_$idx")
                )
            }
        }
    }

    when (activeToolTab) {
        0 -> {
            val surahFilters = listOf(0, 1, 103, 108, 112, 113, 114)
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(surahFilters) { sNum ->
                        val chipTitle = when (sNum) {
                            0 -> when (language) {
                                AppLanguage.URDU -> "سب سورتیں"
                                AppLanguage.ARABIC -> "جميع السور"
                                AppLanguage.HINDI -> "सभी सूरह"
                                AppLanguage.ENGLISH -> "All Surahs"
                            }
                            else -> {
                                val sObj = com.example.data.repository.QuranData.surahs.firstOrNull { it.number == sNum }
                                if (sObj != null) {
                                    "$sNum. ${com.example.localization.LocalizedContentResolver.resolveSurahName(sObj, language)}"
                                } else {
                                    "$sNum"
                                }
                            }
                        }
                        FilterChip(
                            selected = selectedWbwSurahFilter == sNum,
                            onClick = { onSelectWbwSurahFilter(sNum) },
                            label = { Text(chipTitle) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.heightIn(min = 48.dp)
                        )
                    }
                }
            }

            val filteredAyat = if (selectedWbwSurahFilter == 0) {
                QuranLearningRepository.wordByWordAllAyat
            } else {
                QuranLearningRepository.wordByWordAllAyat.filter { it.surahNumber == selectedWbwSurahFilter }
            }

            items(filteredAyat) { wAyah ->
                WordByWordAyahCard(ayahData = wAyah, language = language)
            }
        }

        1 -> {
            items(QuranLearningRepository.quranicVocabulary) { qWord ->
                QuranicWordFlashcard(
                    word = qWord,
                    language = language,
                    isLearned = progress.learnedWordIds.contains(qWord.id),
                    onToggleLearned = {
                        QuranLearningRepository.toggleLearnedWord(context, qWord.id)
                        onProgressChanged()
                    }
                )
            }
        }

        else -> {
            items(QuranLearningRepository.surahIntroductions) { exegesis ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val exegesisSurahObj = com.example.data.repository.QuranData.surahs.firstOrNull { it.number == exegesis.surahNumber }
                            val exegesisTitle = if (exegesisSurahObj != null) {
                                "${exegesis.surahNumber}. ${com.example.localization.LocalizedContentResolver.resolveSurahName(exegesisSurahObj, language)}"
                            } else if (language == AppLanguage.ARABIC) {
                                "${exegesis.surahNumber}. ${exegesis.nameArabic}"
                            } else {
                                "${exegesis.surahNumber}. ${exegesis.nameEnglish}"
                            }
                            Text(
                                text = exegesisTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                            val localizedRevPlace = if (exegesisSurahObj != null) {
                                com.example.localization.LocalizedContentResolver.resolveRevelationType(exegesisSurahObj.revelationType, language)
                            } else {
                                exegesis.revelationPlace
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = localizedRevPlace,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val asbab = when (language) {
                            AppLanguage.URDU -> exegesis.asbabNuzoolUr
                            AppLanguage.ARABIC -> exegesis.asbabNuzoolAr
                            AppLanguage.HINDI -> exegesis.asbabNuzoolHi
                            AppLanguage.ENGLISH -> exegesis.asbabNuzoolEn
                        }
                        Text(
                            text = asbab,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        val lessons = when (language) {
                            AppLanguage.URDU -> exegesis.practicalLessonsUr
                            AppLanguage.ARABIC -> exegesis.practicalLessonsAr
                            AppLanguage.HINDI -> exegesis.practicalLessonsHi
                            AppLanguage.ENGLISH -> exegesis.practicalLessonsEn
                        }
                        if (lessons.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            lessons.forEach { tip ->
                                Text(
                                    text = "• $tip",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = exegesis.sourceAttribution,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Stage 9 Embedded Workspace: Hifz Tracker with Audio Playback, Revision Counter, & Memorization Reminder toggle.
 */
fun LazyListScope.stage9HifzWorkspace(
    context: Context,
    language: AppLanguage,
    progress: StudentAcademyProgress,
    onProgressChanged: () -> Unit
) {
    item {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "روزانہ حفظ و دہرائی کی یاد دہانی"
                                AppLanguage.ARABIC -> "تذكير المراجعة والحفظ اليومي"
                                AppLanguage.HINDI -> "दैनिक हिफ़्ज़ दोहराने की याद दिलाएँ"
                                AppLanguage.ENGLISH -> "Daily Hifz Revision Schedule"
                            },
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "سبق، سبقی اور منزل کی پابندی"
                                AppLanguage.ARABIC -> "حفظ الجديد والربط والمراجعة"
                                AppLanguage.HINDI -> "सबक़, सबक़ी और मंज़िल दोहराव"
                                AppLanguage.ENGLISH -> "Track Sabaq, Sabqi & Dhor daily goals"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = progress.hifzRevisionReminderEnabled,
                        onCheckedChange = { enabled ->
                            QuranLearningRepository.setHifzRevisionReminderEnabled(context, enabled)
                            onProgressChanged()
                        },
                        modifier = Modifier.testTag("hifz_reminder_switch")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                AudioPronunciationController(surahNumber = 112, ayahNumber = 1, language = language)
            }
        }
    }

    items(QuranLearningRepository.hifzTargets) { target ->
        val isMem = progress.memorizedSurahNumbers.contains(target.surahNumber)
        val revCount = progress.hifzRevisionCounts[target.surahNumber] ?: 0
        val isSurahPlaying = QuranAudioManager.isAudioPlaying.value &&
            QuranAudioManager.currentPlayingSurahNumber.value == target.surahNumber &&
            QuranAudioManager.currentPlayingAyahNumber.value == null

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(14.dp)
            ) {
                val targetSurahObj = com.example.data.repository.QuranData.surahs.firstOrNull { it.number == target.surahNumber }
                val targetTitle = if (targetSurahObj != null) {
                    val localizedName = com.example.localization.LocalizedContentResolver.resolveSurahName(targetSurahObj, language)
                    if (language == AppLanguage.ARABIC) {
                        "${target.surahNumber}. $localizedName"
                    } else {
                        "${target.surahNumber}. $localizedName (${target.surahNameArabic})"
                    }
                } else {
                    "${target.surahNumber}. ${target.surahNameArabic}"
                }
                val memStatusText = if (isMem) {
                    when (language) {
                        AppLanguage.URDU -> "یاد شدہ ✓"
                        AppLanguage.ARABIC -> "محفوظة ✓"
                        AppLanguage.HINDI -> "याद ✓"
                        AppLanguage.ENGLISH -> "Memorized ✓"
                    }
                } else {
                    when (language) {
                        AppLanguage.URDU -> "جاری"
                        AppLanguage.ARABIC -> "قيد الحفظ"
                        AppLanguage.HINDI -> "जारी"
                        AppLanguage.ENGLISH -> "In Progress"
                    }
                }
                val ayahsLabel = com.example.localization.AppLocalization.get("learning_ayahs_label", language)
                val revisionsLabel = com.example.localization.AppLocalization.get("learning_revisions_label", language)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = targetTitle,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${target.totalAyahs} $ayahsLabel • $revisionsLabel: $revCount • $memStatusText",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isMem) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalButton(
                        onClick = {
                            QuranLearningRepository.incrementHifzRevision(context, target.surahNumber)
                            onProgressChanged()
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.heightIn(min = 38.dp)
                    ) {
                        Text("+1", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    IconButton(
                        onClick = {
                            if (isSurahPlaying) {
                                QuranAudioManager.togglePlayPause()
                            } else {
                                val localizedName = if (targetSurahObj != null) {
                                    com.example.localization.LocalizedContentResolver.resolveSurahName(targetSurahObj, language)
                                } else {
                                    target.surahNameArabic
                                }
                                QuranAudioManager.playSurah(context, target.surahNumber, localizedName)
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isSurahPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = com.example.localization.AppLocalization.get("learning_play_surah", language),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Checkbox(
                        checked = isMem,
                        onCheckedChange = {
                            QuranLearningRepository.toggleMemorizedSurah(context, target.surahNumber)
                            onProgressChanged()
                        }
                    )
                }
            }
        }
    }
}

/**
 * Stage 11 Embedded Workspace: All 18 Academy Quizzes filterable by Stage.
 */
fun LazyListScope.stage11AssessmentsWorkspace(
    context: Context,
    language: AppLanguage,
    selectedQuizStageFilter: Int,
    onSelectQuizStageFilter: (Int) -> Unit,
    onProgressChanged: () -> Unit
) {
    val stageNums = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    item {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(stageNums) { sNum ->
                val label = if (sNum == 0) {
                    when (language) {
                        AppLanguage.URDU -> "تمام امتحانات"
                        AppLanguage.ARABIC -> "جميع الاختبارات"
                        AppLanguage.HINDI -> "सभी क्विज़"
                        AppLanguage.ENGLISH -> "All Stages"
                    }
                } else {
                    when (language) {
                        AppLanguage.URDU -> "مرحلہ $sNum"
                        AppLanguage.ARABIC -> "المرحلة $sNum"
                        AppLanguage.HINDI -> "चरण $sNum"
                        AppLanguage.ENGLISH -> "Stage $sNum"
                    }
                }
                FilterChip(
                    selected = selectedQuizStageFilter == sNum,
                    onClick = { onSelectQuizStageFilter(sNum) },
                    label = { Text(label) },
                    colors = noorSelectableChipColors(),
                    modifier = Modifier.heightIn(min = 48.dp)
                )
            }
        }
    }

    val filteredQuizzes = if (selectedQuizStageFilter == 0) {
        QuranLearningRepository.academyQuizzes
    } else {
        QuranLearningRepository.academyQuizzes.filter { it.stageNumber == selectedQuizStageFilter }
    }

    items(filteredQuizzes, key = { it.id }) { quiz ->
        InteractiveQuizCard(
            quiz = quiz,
            language = language,
            onQuizPassed = {
                QuranLearningRepository.recordQuizPass(context, quiz.id)
                onProgressChanged()
                Toast.makeText(context, "✓", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
