package com.example.ui.screens.hajj

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.hajj.FiqhSchool
import com.example.data.hajj.HajjGuideState
import com.example.data.hajj.HajjIntroAndIhramData
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.theme.QuranNaskhFontFamily

@Composable
fun HajjIntroTab(
    language: AppLanguage,
    state: HajjGuideState,
    onSelectSchool: (FiqhSchool) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val cards = HajjIntroAndIhramData.introCards()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("hajj_intro_list")
    ) {
        item(key = "school_selector_intro") {
            FiqhSchoolSelectorCard(
                language = language,
                selectedSchool = state.selectedSchool,
                onSelectSchool = onSelectSchool
            )
        }

        items(cards.size) { index ->
            val (title, body) = cards[index]
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hajj_intro_card_$index")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title.get(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = body.get(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (index == cards.lastIndex) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { runCatching { uriHandler.openUri("https://www.haj.gov.sa/") } },
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("hajj_intro_moha_btn")
                            ) {
                                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("haj.gov.sa")
                            }
                            OutlinedButton(
                                onClick = { runCatching { uriHandler.openUri("https://www.nusuk.sa/") } },
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("hajj_intro_nusuk_btn")
                            ) {
                                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("nusuk.sa")
                            }
                        }
                    }
                }
            }
        }

        item(key = "hajj_form_madhhab") {
            val note = HajjIntroAndIhramData.hajjFormMadhhabNote
            MadhhabNoteCard(note = note, selectedSchool = state.selectedSchool, language = language)
        }

        item(key = "glossary_header") {
            Text(
                text = AppLocalization.get("hajj_terminology_title", language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(HajjIntroAndIhramData.terminologyList, key = { it.id }) { term ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hajj_term_${term.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = term.term,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = term.arabic,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = QuranNaskhFontFamily,
                                fontSize = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = term.definition.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun HajjIhramTab(
    language: AppLanguage,
    state: HajjGuideState
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("hajj_ihram_list")
    ) {
        item(key = "meeqat_header") {
            Text(
                text = AppLocalization.get("hajj_meeqat_title", language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(HajjIntroAndIhramData.meeqatPoints, key = { it.id }) { point ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hajj_meeqat_${point.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = point.name.get(language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = point.arabic,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = QuranNaskhFontFamily,
                                fontSize = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = point.servesRegions.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = point.reference,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item(key = "prohibitions_header") {
            Text(
                text = AppLocalization.get("hajj_prohibitions_title", language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(HajjIntroAndIhramData.ihramProhibitions, key = { it.id }) { rule ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hajj_prohibition_${rule.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = rule.title.get(language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = rule.scopeNote.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = rule.reference,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        items(HajjIntroAndIhramData.ihramMadhhabNotes.size) { idx ->
            MadhhabNoteCard(
                note = HajjIntroAndIhramData.ihramMadhhabNotes[idx],
                selectedSchool = state.selectedSchool,
                language = language
            )
        }
    }
}
