package com.example.ui.screens.business

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.business.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization

@Composable
fun EvidenceCategoryBadge(
    category: EvidenceCategory,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = when (category) {
        EvidenceCategory.DIRECT_QURAN -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        EvidenceCategory.AUTHENTIC_HADITH -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        EvidenceCategory.CLASSICAL_FIQH -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        EvidenceCategory.CONTEMPORARY_RESOLUTION -> MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) to MaterialTheme.colorScheme.primary
        EvidenceCategory.REGULATORY_LEGAL -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
        EvidenceCategory.EDUCATIONAL_BUSINESS -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f) to MaterialTheme.colorScheme.secondary
        EvidenceCategory.SCHOLARLY_REVIEW_REQUIRED -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
        modifier = modifier.testTag("evidence_badge_${category.id}")
    ) {
        Text(
            text = category.label(language),
            color = fg,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun BusinessSectionCard(
    title: String,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor, contentColor = contentColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun QuranCitationCard(
    citation: BusinessQuranCitation,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val activeTranslation = when (language) {
        AppLanguage.URDU -> citation.urduTranslation
        AppLanguage.ENGLISH -> citation.englishTranslation
        AppLanguage.HINDI -> citation.hindiTranslation
        AppLanguage.ARABIC -> ""
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)),
        modifier = modifier.fillMaxWidth().testTag("quran_citation_${citation.surahNumber}_${citation.startAyah}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                EvidenceCategoryBadge(EvidenceCategory.DIRECT_QURAN, language)
                Text(
                    text = if (citation.endAyah > citation.startAyah) {
                        "Quran ${citation.surahNumber}:${citation.startAyah}-${citation.endAyah}"
                    } else {
                        "Quran ${citation.surahNumber}:${citation.startAyah}"
                    },
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (citation.arabicText.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = citation.arabicText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        lineHeight = 32.sp,
                        textDirection = TextDirection.Rtl
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (activeTranslation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = activeTranslation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${AppLocalization.get("ba_context_note", language)}: ${citation.contextNote.get(language)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${AppLocalization.get("ba_business_relevance", language)}: ${citation.businessRelevance.get(language)}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = citation.sourceUrl,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun HadithCitationCard(
    hadith: BusinessHadithCitation,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val translation = when (language) {
        AppLanguage.URDU -> hadith.urduTranslation
        AppLanguage.ENGLISH -> hadith.englishTranslation
        AppLanguage.HINDI -> hadith.hindiTranslation
        AppLanguage.ARABIC -> ""
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)),
        modifier = modifier.fillMaxWidth().testTag("hadith_citation_${hadith.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                EvidenceCategoryBadge(EvidenceCategory.AUTHENTIC_HADITH, language)
                Text(
                    text = "${hadith.collection} #${hadith.hadithNumber}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = hadith.chapter,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = hadith.arabicText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 30.sp,
                    textDirection = TextDirection.Rtl
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            if (translation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${AppLocalization.get("ba_narrator", language)}: ${hadith.narrator} • ${hadith.grading}",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = hadith.gradingSourceAndNotes.get(language),
                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InteractiveMcqSection(
    itemId: String,
    mcqs: List<BusinessMcq>,
    existingResult: BusinessQuizResultEntity?,
    language: AppLanguage,
    onSaveQuizScore: (Int, Int) -> Unit
) {
    if (mcqs.isEmpty()) return
    val selections = remember(itemId) { mutableStateMapOf<String, Int>() }
    var submitted by remember(itemId) { mutableStateOf(false) }

    BusinessSectionCard(
        title = AppLocalization.get("ba_quiz_section", language),
        icon = Icons.Default.Quiz
    ) {
        if (existingResult != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "${AppLocalization.get("ba_best_score", language)}: ${existingResult.bestScore}/${existingResult.totalQuestions}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        mcqs.forEachIndexed { qIdx, mcq ->
            val options = mcq.options.get(language)
            val chosen = selections[mcq.id]
            Text(
                text = "${qIdx + 1}. ${mcq.question.get(language)}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            options.forEachIndexed { optIdx, optText ->
                val isSelected = chosen == optIdx
                val borderColor = when {
                    submitted && optIdx == mcq.correctIndex -> MaterialTheme.colorScheme.primary
                    submitted && isSelected && optIdx != mcq.correctIndex -> MaterialTheme.colorScheme.error
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outlineVariant
                }
                OutlinedCard(
                    onClick = { if (!submitted) selections[mcq.id] = optIdx },
                    border = BorderStroke(if (isSelected || (submitted && optIdx == mcq.correctIndex)) 2.dp else 1.dp, borderColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("mcq_${mcq.id}_opt_$optIdx")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        RadioButton(selected = isSelected, onClick = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = optText, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            if (submitted) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mcq.explanation.get(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val score = mcqs.count { selections[it.id] == it.correctIndex }
                    submitted = true
                    onSaveQuizScore(score, mcqs.size)
                },
                enabled = selections.size == mcqs.size && !submitted,
                modifier = Modifier.weight(1f).height(48.dp).testTag("quiz_submit_$itemId")
            ) {
                Text(AppLocalization.get("ba_check_answers", language))
            }
            if (submitted) {
                OutlinedButton(
                    onClick = {
                        selections.clear()
                        submitted = false
                    },
                    modifier = Modifier.height(48.dp).testTag("quiz_retry_$itemId")
                ) {
                    Text(AppLocalization.get("ba_retry", language))
                }
            }
        }
    }
}

@Composable
fun DetailActionAndNotesBar(
    itemId: String,
    isBookmarked: Boolean,
    isCompleted: Boolean,
    existingNote: String,
    language: AppLanguage,
    onToggleBookmark: () -> Unit,
    onToggleCompleted: () -> Unit,
    onSaveNote: (String) -> Unit
) {
    var noteDraft by remember(itemId, existingNote) { mutableStateOf(existingNote) }
    var noteSavedBanner by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onToggleBookmark,
                modifier = Modifier.weight(1f).height(48.dp).testTag("btn_bookmark_$itemId")
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = AppLocalization.get("ba_bookmark", language)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    if (isBookmarked) AppLocalization.get("ba_bookmarked", language)
                    else AppLocalization.get("ba_bookmark", language)
                )
            }
            Button(
                onClick = onToggleCompleted,
                colors = if (isCompleted) {
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                } else {
                    ButtonDefaults.buttonColors()
                },
                modifier = Modifier.weight(1f).height(48.dp).testTag("btn_complete_$itemId")
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = AppLocalization.get("ba_mark_complete", language)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    if (isCompleted) AppLocalization.get("ba_completed", language)
                    else AppLocalization.get("ba_mark_complete", language)
                )
            }
        }

        BusinessSectionCard(
            title = AppLocalization.get("ba_personal_notes", language),
            icon = Icons.Default.EditNote
        ) {
            OutlinedTextField(
                value = noteDraft,
                onValueChange = {
                    noteDraft = it
                    noteSavedBanner = false
                },
                placeholder = { Text(AppLocalization.get("ba_note_placeholder", language)) },
                minLines = 2,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth().testTag("note_input_$itemId")
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (noteSavedBanner) {
                    Text(
                        text = AppLocalization.get("ba_saved_offline", language),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Button(
                    onClick = {
                        onSaveNote(noteDraft)
                        noteSavedBanner = true
                    },
                    modifier = Modifier.height(48.dp).testTag("note_save_$itemId")
                ) {
                    Text(AppLocalization.get("ba_save_note", language))
                }
            }
        }
    }
}

@Composable
fun BulletListSection(items: List<String>, bulletPrefix: String = "•") {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { line ->
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = bulletPrefix,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun SourceRefsSection(sources: List<BusinessSourceRef>, language: AppLanguage) {
    if (sources.isEmpty()) return
    BusinessSectionCard(
        title = AppLocalization.get("ba_sources_attribution", language),
        icon = Icons.Default.LibraryBooks
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            sources.forEach { src ->
                Column {
                    EvidenceCategoryBadge(src.category, language)
                    Spacer(modifier = Modifier.height(4.dp))
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
