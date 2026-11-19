package com.example.ui.screens.hajj

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.hajj.*
import com.example.data.model.AppLanguage
import com.example.data.repository.DuaAudioPlayer
import com.example.data.repository.DuaData
import com.example.localization.AppLocalization
import com.example.ui.components.noorSelectableChipColors
import com.example.ui.theme.QuranNaskhFontFamily

@Composable
fun HajjDuasTab(
    language: AppLanguage
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val currentPlayingId by DuaAudioPlayer.currentDuaId.collectAsStateWithLifecycle()
    val isPlaying by DuaAudioPlayer.isPlaying.collectAsStateWithLifecycle()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("hajj_duas_list")
    ) {
        items(HajjDuasAndChecklistsData.duas, key = { it.id }) { dua ->
            val linkedDua = remember(dua.linkedDuaDataId) {
                dua.linkedDuaDataId?.let { id -> DuaData.duas.firstOrNull { it.id == id } }
            }
            val canPlayGenuineAudio = linkedDua?.hasGenuineAudio == true
            val isThisPlaying = linkedDua != null && currentPlayingId == linkedDua.id && isPlaying

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hajj_dua_card_${dua.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = dua.occasion.get(language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = dua.arabic,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = QuranNaskhFontFamily,
                                fontSize = 23.sp,
                                lineHeight = 36.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = dua.transliteration,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = dua.translation.get(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${AppLocalization.get("hajj_ref_label", language)}: ${dua.reference}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (canPlayGenuineAudio && linkedDua != null) {
                            FilledTonalButton(
                                onClick = { DuaAudioPlayer.playDua(context, linkedDua, repeatTarget = 1) },
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("hajj_dua_play_${dua.id}")
                            ) {
                                Icon(
                                    imageVector = if (isThisPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = AppLocalization.get("play", language)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isThisPlaying) AppLocalization.get("stop", language)
                                    else AppLocalization.get("play", language)
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { runCatching { uriHandler.openUri(dua.sourceUrl) } },
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("hajj_dua_source_${dua.id}")
                        ) {
                            Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppLocalization.get("source_label", language))
                        }

                        IconButton(
                            onClick = {
                                val text = buildString {
                                    appendLine(dua.occasion.get(language))
                                    appendLine(dua.arabic)
                                    appendLine(dua.transliteration)
                                    appendLine(dua.translation.get(language))
                                    append("${dua.reference} — ${dua.sourceUrl}")
                                }
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, text)
                                }
                                runCatching {
                                    context.startActivity(Intent.createChooser(intent, dua.occasion.get(language)))
                                }
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("hajj_dua_share_${dua.id}")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = AppLocalization.get("share", language))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HajjPracticalTab(
    language: AppLanguage,
    state: HajjGuideState,
    onToggleChecklist: (String) -> Unit,
    onAddCustomChecklist: (ChecklistCategory, String) -> Unit,
    onDeleteCustomChecklist: (String) -> Unit,
    onSaveNote: (title: String, content: String, noteId: String?) -> Unit,
    onDeleteNote: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val allItems = remember(state.customChecklistItems) {
        HajjDuasAndChecklistsData.defaultChecklists + state.customChecklistItems
    }

    var newChecklistInput by remember { mutableStateOf("") }
    var newChecklistCategory by remember { mutableStateOf(ChecklistCategory.PACKING) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<HajjPersonalNote?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("hajj_practical_list")
    ) {
        item(key = "disclaimer_card") {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = HajjDuasAndChecklistsData.validationDisclaimer(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        item(key = "add_custom_chk") {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = AppLocalization.get("hajj_add_checklist_title", language),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ChecklistCategory.entries.forEach { cat ->
                            FilterChip(
                                selected = newChecklistCategory == cat,
                                onClick = { newChecklistCategory = cat },
                                label = { Text(categoryLabel(cat, language), maxLines = 1) },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.heightIn(min = 48.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newChecklistInput,
                            onValueChange = { newChecklistInput = it },
                            singleLine = true,
                            placeholder = { Text(AppLocalization.get("hajj_add_checklist_hint", language)) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hajj_custom_chk_input")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newChecklistInput.isNotBlank()) {
                                    onAddCustomChecklist(newChecklistCategory, newChecklistInput)
                                    newChecklistInput = ""
                                }
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("hajj_add_chk_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }
            }
        }

        ChecklistCategory.entries.forEach { cat ->
            val catItems = allItems.filter { it.category == cat }
            item(key = "hdr_${cat.name}") {
                Text(
                    text = categoryLabel(cat, language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            items(catItems, key = { it.id }) { item ->
                val checked = state.checkedChecklistIds.contains(item.id)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hajj_chk_card_${item.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { onToggleChecklist(item.id) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("hajj_chk_box_${item.id}")
                        )
                        Text(
                            text = item.title.get(language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (item.isCustom) {
                            IconButton(
                                onClick = { onDeleteCustomChecklist(item.id) },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("hajj_chk_del_${item.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = AppLocalization.get("delete", language),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        item(key = "notes_header") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            ) {
                Text(
                    text = AppLocalization.get("hajj_personal_notes_title", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(
                    onClick = {
                        editingNote = null
                        showNoteDialog = true
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("hajj_add_note_btn")
                ) {
                    Icon(Icons.Default.EditNote, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppLocalization.get("hajj_new_note_btn", language))
                }
            }
        }

        if (state.notes.isEmpty()) {
            item(key = "empty_notes") {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = AppLocalization.get("hajj_notes_empty", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(state.notes, key = { it.id }) { note ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hajj_note_card_${note.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = note.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    editingNote = note
                                    showNoteDialog = true
                                },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("hajj_note_edit_${note.id}")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Note")
                            }
                            IconButton(
                                onClick = { onDeleteNote(note.id) },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("hajj_note_del_${note.id}")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Note", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        if (note.content.isNotBlank()) {
                            Text(
                                text = note.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        item(key = "official_links_hdr") {
            Text(
                text = AppLocalization.get("hajj_official_links_title", language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        items(HajjDuasAndChecklistsData.officialLinks.size) { idx ->
            val (label, url) = HajjDuasAndChecklistsData.officialLinks[idx]
            OutlinedCard(
                onClick = { runCatching { uriHandler.openUri(url) } },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hajj_official_link_$idx")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = label.get(language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = url,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Icon(Icons.Default.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    if (showNoteDialog) {
        HajjNoteEditorDialog(
            language = language,
            initialNote = editingNote,
            onDismiss = { showNoteDialog = false },
            onSave = { title, body ->
                onSaveNote(title, body, editingNote?.id)
                showNoteDialog = false
            }
        )
    }
}

@Composable
private fun HajjNoteEditorDialog(
    language: AppLanguage,
    initialNote: HajjPersonalNote?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember(initialNote) { mutableStateOf(initialNote?.title ?: "") }
    var content by remember(initialNote) { mutableStateOf(initialNote?.content ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialNote == null) AppLocalization.get("hajj_new_note_btn", language)
                else AppLocalization.get("edit", language)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    label = { Text(AppLocalization.get("hajj_note_title_hint", language)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hajj_note_title_input")
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    minLines = 3,
                    label = { Text(AppLocalization.get("hajj_note_body_hint", language)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hajj_note_body_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, content) },
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("hajj_note_save_btn")
            ) {
                Text(AppLocalization.get("save", language))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Text(AppLocalization.get("qibla_cancel", language))
            }
        }
    )
}

private fun categoryLabel(category: ChecklistCategory, language: AppLanguage): String = when (category) {
    ChecklistCategory.IHRAM_PREP -> AppLocalization.get("hajj_chk_ihram", language)
    ChecklistCategory.PACKING -> AppLocalization.get("hajj_chk_packing", language)
    ChecklistCategory.TRAVEL_PREP -> AppLocalization.get("hajj_chk_travel", language)
}
