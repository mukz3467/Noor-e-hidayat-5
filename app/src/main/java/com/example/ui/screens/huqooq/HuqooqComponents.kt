package com.example.ui.screens.huqooq

import androidx.compose.foundation.BorderStroke
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
import com.example.data.huqooq.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.theme.LocalAppSettings
import com.example.ui.theme.resolveArabicFontFamily

@Composable
fun HuqooqEvidenceBadge(
    evidenceType: HuqooqEvidenceType,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (bg, fg) = when (evidenceType) {
        HuqooqEvidenceType.QURAN -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        HuqooqEvidenceType.AUTHENTIC_HADITH -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        HuqooqEvidenceType.SCHOLARLY_CONSENSUS -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        HuqooqEvidenceType.MADHHAB_DIFFERENCE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) to MaterialTheme.colorScheme.primary
        HuqooqEvidenceType.NATIONAL_LAW_DISTINCTION -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
        HuqooqEvidenceType.SAFETY_PROFESSIONAL_HELP -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f) to MaterialTheme.colorScheme.secondary
        HuqooqEvidenceType.MUFTI_REVIEW_REQUIRED -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
        modifier = modifier.testTag("hq_evidence_badge_${evidenceType.id}")
    ) {
        Text(
            text = evidenceType.label(language),
            color = fg,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun HuqooqSectionCard(
    title: String,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    accentColor: Color = MaterialTheme.colorScheme.primary,
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
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = accentColor
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun HuqooqSafetyBannerCard(
    title: String,
    bodyText: String,
    textScale: Float = 1f,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f),
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.35f)),
        modifier = modifier.fillMaxWidth().testTag("hq_safety_banner")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.HealthAndSafety,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bodyText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (14f * textScale).sp,
                    lineHeight = (21f * textScale).sp
                ),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun HuqooqQuranCitationCard(
    citation: HuqooqQuranCitation,
    language: AppLanguage,
    textScale: Float = 1f,
    modifier: Modifier = Modifier
) {
    val arabicFont = resolveArabicFontFamily(LocalAppSettings.current.arabicFontOption)
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
        modifier = modifier
            .fillMaxWidth()
            .testTag("hq_quran_citation_${citation.surahNumber}_${citation.startAyah}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HuqooqEvidenceBadge(HuqooqEvidenceType.QURAN, language)
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
                        fontFamily = arabicFont,
                        fontSize = (20f * textScale).sp,
                        lineHeight = (34f * textScale).sp,
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
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (14f * textScale).sp,
                        lineHeight = (21f * textScale).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            val contextText = citation.contextExplanation.get(language)
            if (contextText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${AppLocalization.get("hq_context_label", language)}: $contextText",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = (13f * textScale).sp,
                        lineHeight = (19f * textScale).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
fun HuqooqHadithCitationCard(
    hadith: HuqooqHadithCitation,
    language: AppLanguage,
    textScale: Float = 1f,
    modifier: Modifier = Modifier
) {
    val arabicFont = resolveArabicFontFamily(LocalAppSettings.current.arabicFontOption)
    val translation = when (language) {
        AppLanguage.URDU -> hadith.urduTranslation
        AppLanguage.ENGLISH -> hadith.englishTranslation
        AppLanguage.HINDI -> hadith.hindiTranslation
        AppLanguage.ARABIC -> ""
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.42f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)),
        modifier = modifier.fillMaxWidth().testTag("hq_hadith_citation_${hadith.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HuqooqEvidenceBadge(HuqooqEvidenceType.AUTHENTIC_HADITH, language)
                Text(
                    text = "${hadith.collection} #${hadith.hadithNumber}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            if (hadith.bookChapter.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = hadith.bookChapter,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (hadith.arabicText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hadith.arabicText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = arabicFont,
                        fontSize = (18f * textScale).sp,
                        lineHeight = (31f * textScale).sp,
                        textDirection = TextDirection.Rtl
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (translation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (14f * textScale).sp,
                        lineHeight = (21f * textScale).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${AppLocalization.get("hq_narrator_label", language)}: ${hadith.narrator} • ${hadith.grading}",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            val note = hadith.sourceNote.get(language)
            if (note.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun HuqooqBulletList(
    items: List<String>,
    bulletPrefix: String = "•",
    textScale: Float = 1f
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { line ->
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = bulletPrefix,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = (14f * textScale).sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (14f * textScale).sp,
                        lineHeight = (20f * textScale).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun HuqooqMcqSection(
    lessonId: String,
    mcqs: List<HuqooqMcq>,
    existingResult: HuqooqQuizResultEntity?,
    language: AppLanguage,
    textScale: Float = 1f,
    onSaveQuizScore: (Int, Int) -> Unit
) {
    if (mcqs.isEmpty()) return
    val selections = remember(lessonId) { mutableStateMapOf<String, Int>() }
    var submitted by remember(lessonId) { mutableStateOf(false) }

    HuqooqSectionCard(
        title = AppLocalization.get("hq_quiz_title", language),
        icon = Icons.Default.Quiz
    ) {
        if (existingResult != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "${AppLocalization.get("hq_best_score", language)}: ${existingResult.bestScore}/${existingResult.totalQuestions}",
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
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (14f * textScale).sp
                ),
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
                    border = BorderStroke(
                        if (isSelected || (submitted && optIdx == mcq.correctIndex)) 2.dp else 1.dp,
                        borderColor
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .defaultMinSize(minHeight = 48.dp)
                        .testTag("hq_mcq_${mcq.id}_opt_$optIdx")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        RadioButton(selected = isSelected, onClick = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = optText,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = (13f * textScale).sp)
                        )
                    }
                }
            }
            if (submitted) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mcq.explanation.get(language),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = (13f * textScale).sp),
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
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("hq_quiz_submit_$lessonId")
            ) {
                Text(AppLocalization.get("hq_check_answers", language))
            }
            if (submitted) {
                OutlinedButton(
                    onClick = {
                        selections.clear()
                        submitted = false
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("hq_quiz_retry_$lessonId")
                ) {
                    Text(AppLocalization.get("hq_retry", language))
                }
            }
        }
    }
}
