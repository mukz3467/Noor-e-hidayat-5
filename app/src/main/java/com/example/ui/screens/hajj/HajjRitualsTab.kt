package com.example.ui.screens.hajj

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.hajj.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.components.noorSelectableChipColors

@Composable
fun HajjRitualsTab(
    language: AppLanguage,
    state: HajjGuideState,
    onSelectMode: (PilgrimageMode) -> Unit,
    onSelectSubType: (HajjSubType) -> Unit,
    onSelectSchool: (FiqhSchool) -> Unit,
    onToggleStepComplete: (String) -> Unit
) {
    val steps = remember(state.selectedMode) {
        HajjGuideRepository.allStepsForMode(state.selectedMode)
    }
    val completedCount = steps.count { state.completedStepIds.contains(it.id) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("hajj_rituals_list")
    ) {
        item(key = "mode_and_progress") {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("hajj_select_guide_mode", language),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = state.selectedMode == PilgrimageMode.UMRAH,
                            onClick = { onSelectMode(PilgrimageMode.UMRAH) },
                            label = { Text(AppLocalization.get("hajj_mode_umrah", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .testTag("hajj_mode_umrah_btn")
                        )
                        FilterChip(
                            selected = state.selectedMode == PilgrimageMode.HAJJ,
                            onClick = { onSelectMode(PilgrimageMode.HAJJ) },
                            label = { Text(AppLocalization.get("hajj_mode_hajj", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .testTag("hajj_mode_hajj_btn")
                        )
                    }

                    if (state.selectedMode == PilgrimageMode.HAJJ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = AppLocalization.get("hajj_subtype_label", language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            HajjSubType.entries.forEach { st ->
                                val label = when (st) {
                                    HajjSubType.TAMATTU -> AppLocalization.get("hajj_type_tamattu", language)
                                    HajjSubType.QIRAN -> AppLocalization.get("hajj_type_qiran", language)
                                    HajjSubType.IFRAD -> AppLocalization.get("hajj_type_ifrad", language)
                                }
                                FilterChip(
                                    selected = state.selectedSubType == st,
                                    onClick = { onSelectSubType(st) },
                                    label = { Text(label, maxLines = 1) },
                                    colors = noorSelectableChipColors(),
                                    modifier = Modifier
                                        .heightIn(min = 48.dp)
                                        .testTag("hajj_subtype_${st.name.lowercase()}")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("hajj_progress_label", language),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "$completedCount / ${steps.size}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag("hajj_progress_count")
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { if (steps.isEmpty()) 0f else completedCount.toFloat() / steps.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .testTag("hajj_progress_bar")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = HajjDuasAndChecklistsData.validationDisclaimer(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                        modifier = Modifier.testTag("hajj_validity_disclaimer")
                    )
                }
            }
        }

        item(key = "madhhab_filter_rituals") {
            FiqhSchoolSelectorCard(
                language = language,
                selectedSchool = state.selectedSchool,
                onSelectSchool = onSelectSchool
            )
        }

        items(steps, key = { it.id }) { step ->
            val isCompleted = state.completedStepIds.contains(step.id)
            GuideStepCard(
                step = step,
                language = language,
                selectedSchool = state.selectedSchool,
                isCompleted = isCompleted,
                onToggleCompleted = { onToggleStepComplete(step.id) }
            )
        }
    }
}

@Composable
private fun GuideStepCard(
    step: GuideStep,
    language: AppLanguage,
    selectedSchool: FiqhSchool,
    isCompleted: Boolean,
    onToggleCompleted: () -> Unit
) {
    var showDiff by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hajj_step_card_${step.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    AssistChip(
                        onClick = {},
                        label = { Text(step.stageBadge.get(language), style = MaterialTheme.typography.labelSmall) }
                    )
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = HajjGuideRepository.rulingLabel(step.rulingLevel, language),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    )
                }
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { onToggleCompleted() },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("hajj_step_check_${step.id}")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = step.title.get(language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = step.summary.get(language),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = step.instructions.get(language),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            step.menVsWomenNote?.let { note ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "${AppLocalization.get("hajj_men_women_label", language)}: ${note.get(language)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            step.hajjFormNote?.let { note ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f))
                        .padding(10.dp)
                ) {
                    Text(
                        text = note.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${AppLocalization.get("hajj_ref_label", language)}: ${step.references}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (step.madhhabNotes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { showDiff = !showDiff },
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("hajj_step_diff_${step.id}")
                ) {
                    Icon(
                        imageVector = if (showDiff) Icons.Default.ExpandLess else Icons.Default.Balance,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppLocalization.get("hajj_differences_btn", language))
                }
                if (showDiff) {
                    step.madhhabNotes.forEach { note ->
                        MadhhabNoteCard(
                            note = note,
                            selectedSchool = selectedSchool,
                            language = language
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FiqhSchoolSelectorCard(
    language: AppLanguage,
    selectedSchool: FiqhSchool,
    onSelectSchool: (FiqhSchool) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = AppLocalization.get("hajj_school_filter_label", language),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(FiqhSchool.entries) { school ->
                    FilterChip(
                        selected = selectedSchool == school,
                        onClick = { onSelectSchool(school) },
                        label = { Text(schoolDisplayName(school, language)) },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .testTag("hajj_school_${school.name.lowercase()}")
                    )
                }
            }
        }
    }
}

@Composable
fun MadhhabNoteCard(
    note: MadhhabDifferenceNote,
    selectedSchool: FiqhSchool,
    language: AppLanguage
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = note.topic.get(language),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = note.viewForSchool(selectedSchool, language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.reference,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun schoolDisplayName(school: FiqhSchool, language: AppLanguage): String = when (school) {
    FiqhSchool.COMPARATIVE -> AppLocalization.get("hajj_school_comparative", language)
    FiqhSchool.HANAFI -> AppLocalization.get("hajj_school_hanafi", language)
    FiqhSchool.SHAFII -> AppLocalization.get("hajj_school_shafii", language)
    FiqhSchool.MALIKI -> AppLocalization.get("hajj_school_maliki", language)
    FiqhSchool.HANBALI -> AppLocalization.get("hajj_school_hanbali", language)
}
