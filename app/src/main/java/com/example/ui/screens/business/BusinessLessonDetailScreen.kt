package com.example.ui.screens.business

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
import com.example.data.business.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar

@Composable
fun BusinessLessonDetailScreen(
    lesson: BusinessLesson,
    quranCitations: List<BusinessQuranCitation>,
    hadithCatalog: Map<String, BusinessHadithCitation>,
    isBookmarked: Boolean,
    isCompleted: Boolean,
    existingNote: String,
    existingQuizResult: BusinessQuizResultEntity?,
    language: AppLanguage,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    onSaveNote: (String) -> Unit,
    onSaveQuizScore: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    val checklistChecked = remember(lesson.id) { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = lesson.moduleTitle.get(language),
                language = language,
                onBackClick = onBack
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
                .testTag("lesson_detail_${lesson.id}")
        ) {
            // Parts 1–4: Header, Category, Title, Learning Objectives
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
                                label = { Text("${AppLocalization.get("ba_level_prefix", language)} ${lesson.level} • ${AppLocalization.get("ba_module_prefix", language)} ${lesson.moduleNumber}") }
                            )
                            EvidenceCategoryBadge(lesson.primaryEvidenceCategory, language)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lesson.title.get(language),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            item {
                BusinessSectionCard(AppLocalization.get("ba_learning_objectives", language), Icons.Default.TrackChanges) {
                    BulletListSection(lesson.learningObjectives.get(language), "✓")
                }
            }

            // Parts 5–8: Introduction & Detailed Multilingual Explanation
            item {
                BusinessSectionCard(AppLocalization.get("ba_introduction", language), Icons.Default.Info) {
                    Text(lesson.introduction.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }

            item {
                BusinessSectionCard(AppLocalization.get("ba_detailed_explanation", language), Icons.Default.MenuBook) {
                    Text(lesson.explanationFor(language), style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Parts 9–12: Quranic References
            if (quranCitations.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        quranCitations.forEach { citation ->
                            QuranCitationCard(citation, language)
                        }
                    }
                }
            }

            // Parts 13–15: Hadith References
            val resolvedHadiths = lesson.hadithIds.mapNotNull { hadithCatalog[it] }
            if (resolvedHadiths.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        resolvedHadiths.forEach { hadith ->
                            HadithCitationCard(hadith, language)
                        }
                    }
                }
            }

            // Parts 16–19: Classical Fiqh, Contemporary AAOIFI/SBP/IIFA, Madhhab Differences, Modern Example
            item {
                BusinessSectionCard(AppLocalization.get("ba_classical_fiqh", language), Icons.Default.HistoryEdu) {
                    EvidenceCategoryBadge(EvidenceCategory.CLASSICAL_FIQH, language)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(lesson.classicalFiqh.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }

            item {
                BusinessSectionCard(AppLocalization.get("ba_contemporary_guidance", language), Icons.Default.AccountBalance) {
                    EvidenceCategoryBadge(EvidenceCategory.CONTEMPORARY_RESOLUTION, language)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(lesson.contemporaryGuidance.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }

            item {
                BusinessSectionCard(AppLocalization.get("ba_madhhab_differences", language), Icons.Default.Balance) {
                    Text(lesson.madhhabDifferences.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }

            item {
                BusinessSectionCard(AppLocalization.get("ba_modern_example", language), Icons.Default.Storefront) {
                    Text(lesson.modernExample.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Part 20: Halal vs Prohibited Comparison Table
            if (lesson.halalVsProhibited.isNotEmpty()) {
                item {
                    BusinessSectionCard(AppLocalization.get("ba_halal_vs_prohibited", language), Icons.Default.CompareArrows) {
                        lesson.halalVsProhibited.forEach { row ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "✓ ${row.halalPractice.get(language)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.65f),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "✕ ${row.prohibitedPractice.get(language)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Parts 21–23: Common Mistakes, Interactive Checklist, Summary
            item {
                BusinessSectionCard(AppLocalization.get("ba_common_mistakes", language), Icons.Default.WarningAmber) {
                    BulletListSection(lesson.commonMistakes.get(language), "!")
                }
            }

            item {
                BusinessSectionCard(AppLocalization.get("ba_compliance_checklist", language), Icons.Default.Checklist) {
                    lesson.complianceChecklist.get(language).forEachIndexed { idx, stepText ->
                        val checked = checklistChecked[idx] == true
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { checklistChecked[idx] = !checked }
                                .defaultMinSize(minHeight = 48.dp)
                                .padding(vertical = 2.dp)
                        ) {
                            Checkbox(checked = checked, onCheckedChange = { checklistChecked[idx] = it })
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(stepText, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            item {
                BusinessSectionCard(AppLocalization.get("ba_summary", language), Icons.Default.Summarize) {
                    Text(lesson.summary.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Part 24: MCQs
            item {
                InteractiveMcqSection(
                    itemId = lesson.id,
                    mcqs = lesson.mcqs,
                    existingResult = existingQuizResult,
                    language = language,
                    onSaveQuizScore = onSaveQuizScore
                )
            }

            // Part 25: Practical Assignment
            item {
                BusinessSectionCard(
                    title = AppLocalization.get("ba_practical_assignment", language),
                    icon = Icons.Default.Assignment,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f)
                ) {
                    Text(lesson.practicalAssignment.get(language), style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Action Bar & Offline Notes + Part 26 Sources
            item {
                DetailActionAndNotesBar(
                    itemId = lesson.id,
                    isBookmarked = isBookmarked,
                    isCompleted = isCompleted,
                    existingNote = existingNote,
                    language = language,
                    onToggleBookmark = onToggleBookmark,
                    onToggleCompleted = onToggleCompleted,
                    onSaveNote = onSaveNote
                )
            }

            item {
                SourceRefsSection(lesson.sources, language)
            }
        }
    }
}
