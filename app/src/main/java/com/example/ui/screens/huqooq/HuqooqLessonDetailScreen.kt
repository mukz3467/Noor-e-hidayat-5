package com.example.ui.screens.huqooq

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.huqooq.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar

@Composable
fun HuqooqLessonDetailScreen(
    lesson: HuqooqLesson,
    quranCitations: List<HuqooqQuranCitation>,
    hadithCatalog: Map<String, HuqooqHadithCitation>,
    isBookmarked: Boolean,
    isCompleted: Boolean,
    existingQuizResult: HuqooqQuizResultEntity?,
    textScale: Float,
    language: AppLanguage,
    onAdjustTextScale: (Float) -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    onSaveQuizScore: (Int, Int) -> Unit,
    onOpenCalculator: () -> Unit,
    onBack: () -> Unit
) {
    val actionChecked = remember(lesson.id) { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = lesson.groupLabel.get(language),
                language = language,
                onBackClick = onBack,
                actions = {
                    IconButton(
                        onClick = { onAdjustTextScale(-0.10f) },
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("hq_font_decrease")
                    ) {
                        Icon(
                            imageVector = Icons.Default.TextDecrease,
                            contentDescription = "Decrease font size"
                        )
                    }
                    IconButton(
                        onClick = { onAdjustTextScale(0.10f) },
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("hq_font_increase")
                    ) {
                        Icon(
                            imageVector = Icons.Default.TextIncrease,
                            contentDescription = "Increase font size"
                        )
                    }
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("hq_top_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = AppLocalization.get("hq_bookmark", language),
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
                .testTag("hq_lesson_detail_${lesson.id}")
        ) {
            // Header Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text("${lesson.department.title(language)} • #${lesson.order}")
                                }
                            )
                            HuqooqEvidenceBadge(lesson.evidenceBadge, language)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lesson.title.get(language),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = (20f * textScale).sp,
                                lineHeight = (28f * textScale).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Quick shortcut to Inheritance Calculator for Dept 2 (Ilm-ul-Faraid)
            if (lesson.department == HuqooqDepartment.ILM_UL_FARAID) {
                item {
                    Card(
                        onClick = onOpenCalculator,
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth().testTag("hq_detail_open_calculator")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = AppLocalization.get("hq_open_calculator_title", language),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = AppLocalization.get("hq_open_calculator_title", language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = AppLocalization.get("hq_open_calculator_sub", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Introduction & Detailed Explanation
            item {
                HuqooqSectionCard(AppLocalization.get("hq_sec_intro", language), Icons.Default.Info) {
                    Text(
                        text = lesson.introduction.get(language),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (14f * textScale).sp,
                            lineHeight = (21f * textScale).sp
                        )
                    )
                }
            }

            item {
                HuqooqSectionCard(AppLocalization.get("hq_sec_explanation", language), Icons.Default.MenuBook) {
                    Text(
                        text = lesson.explanation.get(language),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (14f * textScale).sp,
                            lineHeight = (22f * textScale).sp
                        )
                    )
                }
            }

            // Dept 1 Specific Blocks
            lesson.dutyBearer?.let { db ->
                item {
                    HuqooqSectionCard(AppLocalization.get("hq_sec_duty_bearer", language), Icons.Default.AssignmentInd) {
                        Text(
                            text = db.get(language),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14f * textScale).sp)
                        )
                    }
                }
            }

            lesson.mutualBoundaries?.let { mb ->
                item {
                    HuqooqSectionCard(AppLocalization.get("hq_sec_boundaries", language), Icons.Default.Balance) {
                        Text(
                            text = mb.get(language),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14f * textScale).sp)
                        )
                    }
                }
            }

            lesson.rightsViolatedAction?.let { rva ->
                item {
                    HuqooqSectionCard(AppLocalization.get("hq_sec_remedies", language), Icons.Default.Gavel) {
                        Text(
                            text = rva.get(language),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14f * textScale).sp)
                        )
                    }
                }
            }

            // Dept 3 Specific Blocks (Islah-e-Muashra)
            lesson.rootCauses?.let { rc ->
                val list = rc.get(language)
                if (list.isNotEmpty()) {
                    item {
                        HuqooqSectionCard(AppLocalization.get("hq_sec_root_causes", language), Icons.Default.Search) {
                            HuqooqBulletList(list, "•", textScale)
                        }
                    }
                }
            }

            lesson.individualSolution?.let { ind ->
                val list = ind.get(language)
                if (list.isNotEmpty()) {
                    item {
                        HuqooqSectionCard(AppLocalization.get("hq_sec_individual_solution", language), Icons.Default.Person) {
                            HuqooqBulletList(list, "✓", textScale)
                        }
                    }
                }
            }

            lesson.familySocietySolution?.let { fam ->
                val list = fam.get(language)
                if (list.isNotEmpty()) {
                    item {
                        HuqooqSectionCard(AppLocalization.get("hq_sec_family_society_solution", language), Icons.Default.Groups) {
                            HuqooqBulletList(list, "✓", textScale)
                        }
                    }
                }
            }

            lesson.safetyAndLegalSupport?.let { safety ->
                val safetyText = safety.get(language)
                if (safetyText.isNotBlank()) {
                    item {
                        HuqooqSafetyBannerCard(
                            title = AppLocalization.get("hq_sec_safety_legal", language),
                            bodyText = safetyText,
                            textScale = textScale
                        )
                    }
                }
            }

            // Dept 4 Specific Daily Exercise
            lesson.dailyExercise?.let { de ->
                val text = de.get(language)
                if (text.isNotBlank()) {
                    item {
                        HuqooqSectionCard(
                            title = AppLocalization.get("hq_sec_daily_exercise", language),
                            icon = Icons.Default.SelfImprovement,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f)
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14f * textScale).sp)
                            )
                        }
                    }
                }
            }

            // Verbatim Quranic Citations
            if (quranCitations.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        quranCitations.forEach { citation ->
                            HuqooqQuranCitationCard(citation, language, textScale)
                        }
                    }
                }
            }

            // Authentic Hadith Citations
            val resolvedHadiths = lesson.hadithIds.mapNotNull { hadithCatalog[it] }
            if (resolvedHadiths.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        resolvedHadiths.forEach { hadith ->
                            HuqooqHadithCitationCard(hadith, language, textScale)
                        }
                    }
                }
            }

            // Practical Ways, Real-Life Examples, Common Mistakes
            val practical = lesson.practicalWays.get(language)
            if (practical.isNotEmpty()) {
                item {
                    HuqooqSectionCard(AppLocalization.get("hq_sec_practical_ways", language), Icons.Default.Lightbulb) {
                        HuqooqBulletList(practical, "•", textScale)
                    }
                }
            }

            val examples = lesson.realLifeExamples.get(language)
            if (examples.isNotEmpty()) {
                item {
                    HuqooqSectionCard(AppLocalization.get("hq_sec_examples", language), Icons.Default.AutoStories) {
                        HuqooqBulletList(examples, "→", textScale)
                    }
                }
            }

            val mistakes = lesson.commonMistakes.get(language)
            if (mistakes.isNotEmpty()) {
                item {
                    HuqooqSectionCard(
                        title = AppLocalization.get("hq_sec_mistakes", language),
                        icon = Icons.Default.Warning,
                        accentColor = MaterialTheme.colorScheme.error
                    ) {
                        HuqooqBulletList(mistakes, "✕", textScale)
                    }
                }
            }

            // Interactive Action Steps Checklist
            val actions = lesson.actionSteps.get(language)
            if (actions.isNotEmpty()) {
                item {
                    HuqooqSectionCard(AppLocalization.get("hq_sec_action_steps", language), Icons.Default.Checklist) {
                        actions.forEachIndexed { idx, step ->
                            val checked = actionChecked[idx] == true
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .clickable { actionChecked[idx] = !checked }
                                    .padding(vertical = 2.dp)
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { actionChecked[idx] = it }
                                )
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14f * textScale).sp),
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Scholarly Nuance & Law Distinction
            lesson.scholarlyNuance?.let { nuance ->
                val text = nuance.get(language)
                if (text.isNotBlank()) {
                    item {
                        HuqooqSectionCard(
                            title = AppLocalization.get("hq_sec_scholarly_nuance", language),
                            icon = Icons.Default.AccountBalance
                        ) {
                            if (lesson.isDisputedOrComplex) {
                                HuqooqEvidenceBadge(HuqooqEvidenceType.MADHHAB_DIFFERENCE, language)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14f * textScale).sp)
                            )
                        }
                    }
                }
            }

            // Source Links Attribution
            if (lesson.sourceLinks.isNotEmpty()) {
                item {
                    HuqooqSectionCard(AppLocalization.get("hq_sec_sources", language), Icons.Default.LibraryBooks) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            lesson.sourceLinks.forEach { src ->
                                Column {
                                    Text(
                                        text = src.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Text(
                                        text = "${src.attribution} • ${src.url}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Interactive MCQ Section
            item {
                HuqooqMcqSection(
                    lessonId = lesson.id,
                    mcqs = lesson.mcqs,
                    existingResult = existingQuizResult,
                    language = language,
                    textScale = textScale,
                    onSaveQuizScore = onSaveQuizScore
                )
            }

            // Bottom Action Bar: Bookmark & Complete
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    OutlinedButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("hq_btn_bookmark_${lesson.id}")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = AppLocalization.get("hq_bookmark", language)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (isBookmarked) AppLocalization.get("hq_bookmarked", language)
                            else AppLocalization.get("hq_bookmark", language)
                        )
                    }
                    Button(
                        onClick = onToggleCompleted,
                        colors = if (isCompleted) {
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        } else {
                            ButtonDefaults.buttonColors()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("hq_btn_complete_${lesson.id}")
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = AppLocalization.get("hq_mark_complete", language)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (isCompleted) AppLocalization.get("hq_completed", language)
                            else AppLocalization.get("hq_mark_complete", language)
                        )
                    }
                }
            }
        }
    }
}
