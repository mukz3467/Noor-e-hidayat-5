package com.example.ui.screens.ramadan

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppLanguage
import com.example.data.model.BookmarkItem
import com.example.data.model.BookmarkType
import com.example.data.model.Dua
import com.example.data.ramadan.DeedCategory
import com.example.data.ramadan.RamadanCompanionState
import com.example.data.ramadan.RamadanRepository
import com.example.data.ramadan.formatDateKey
import com.example.data.repository.DuaAudioPlayer
import com.example.data.repository.DuaData
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.localization.LocalizedContentResolver
import com.example.ui.theme.QuranNaskhFontFamily
import java.util.Calendar

@Composable
fun RamadanDuasDeedsTab(
    state: RamadanCompanionState,
    language: AppLanguage
) {
    val context = LocalContext.current
    val prefsRepo = remember(context) { UserPreferencesRepository(context) }
    val favoriteIds by prefsRepo.favoriteDuaIds.collectAsStateWithLifecycle()
    val playingDuaId by DuaAudioPlayer.currentDuaId.collectAsStateWithLifecycle()
    val isPlayingAudio by DuaAudioPlayer.isPlaying.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        DuaAudioPlayer.init(context)
        prefsRepo.reloadBookmarksFromPrefs()
    }

    val ramadanDuas = remember {
        DuaData.duas.filter { it.categoryId == "ramadan_fasting_qadr" || it.id == "dua_beneficial_knowledge_rizq_amal" }
    }

    val todayKey = remember { formatDateKey(Calendar.getInstance()) }
    val completedDeedsToday = state.completedDeedIdsByDate[todayKey].orEmpty()
    val checkedToday = state.checkedChecklistIdsByDate[todayKey].orEmpty()

    var showAddDeedDialog by remember { mutableStateOf(false) }
    var showAddSadaqahDialog by remember { mutableStateOf(false) }
    var showAddChecklistDialog by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize().testTag("ramadan_duas_deeds_list")
    ) {
        // 1. Authentic Ramadan Duas Header + Scholarly Verification Disclaimer
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppLocalization.get("ram_authentic_duas", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_dua_scholarly_note", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(10.dp).testTag("ram_dua_scholarly_note")
                        )
                    }
                }
            }
        }

        items(ramadanDuas, key = { it.id }) { dua ->
            RamadanDuaCard(
                dua = dua,
                language = language,
                isBookmarked = favoriteIds.contains(dua.id),
                isPlaying = playingDuaId == dua.id && isPlayingAudio,
                hasGenuineAudio = dua.audioSources.isNotEmpty() || !dua.audioAssetPath.isNullOrBlank(),
                onPlayToggle = {
                    if (playingDuaId == dua.id && isPlayingAudio) {
                        DuaAudioPlayer.stop()
                    } else {
                        DuaAudioPlayer.playDua(context, dua, repeatTarget = 1)
                    }
                },
                onBookmarkToggle = {
                    val added = prefsRepo.toggleFavoriteDua(dua.id)
                    val currentlyBm = prefsRepo.isDuaBookmarked(dua.id)
                    if ((added && !currentlyBm) || (!added && currentlyBm)) {
                        prefsRepo.toggleBookmark(
                            BookmarkItem(
                                id = "bm_dua_${dua.id}",
                                type = BookmarkType.DUA,
                                title = dua.titleEn,
                                subtitle = dua.arabic.take(90),
                                duaId = dua.id
                            )
                        )
                    }
                }
            )
        }

        // 2. Private Good Deeds & Sadaqah Tracker
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
                            text = AppLocalization.get("ram_good_deeds", language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        FilledTonalButton(
                            onClick = { showAddDeedDialog = true },
                            modifier = Modifier.heightIn(min = 48.dp).testTag("ram_add_deed_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "شامل کریں"
                                    AppLanguage.ARABIC -> "إضافة"
                                    AppLanguage.HINDI -> "जोड़ें"
                                    AppLanguage.ENGLISH -> "Add"
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    state.deeds.forEach { deed ->
                        val checked = completedDeedsToday.contains(deed.id)
                        val title = deed.titleKey?.let { AppLocalization.get(it, language) } ?: deed.title
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { RamadanRepository.toggleDeedForDate(context, deed.id, todayKey) },
                                modifier = Modifier.testTag("ram_deed_checkbox_${deed.id}")
                            )
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            if (deed.isCustom) {
                                IconButton(onClick = { RamadanRepository.deleteCustomDeed(context, deed.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete deed", modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppLocalization.get("ram_sadaqah_log", language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        FilledTonalButton(
                            onClick = { showAddSadaqahDialog = true },
                            modifier = Modifier.heightIn(min = 48.dp).testTag("ram_add_sadaqah_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppLocalization.get("ram_add_sadaqah", language))
                        }
                    }
                    state.sadaqahRecords.take(6).forEach { rec ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        ) {
                            Text(
                                text = "${rec.dateKey}: ${rec.amountText} ${rec.currencyCode} — ${rec.recipientOrCause}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { RamadanRepository.deleteSadaqahRecord(context, rec.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Sadaqah", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // 3. Iftar Preparation Checklist Card
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
                            text = AppLocalization.get("ram_iftar_checklist", language),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f)
                        )
                        FilledTonalButton(
                            onClick = { showAddChecklistDialog = true },
                            modifier = Modifier.heightIn(min = 48.dp).testTag("ram_add_checklist_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "شامل کریں"
                                    AppLanguage.ARABIC -> "إضافة"
                                    AppLanguage.HINDI -> "जोड़ें"
                                    AppLanguage.ENGLISH -> "Add"
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    state.checklistItems.forEach { item ->
                        val isChecked = checkedToday.contains(item.id)
                        val title = item.titleKey?.let { AppLocalization.get(it, language) } ?: item.title
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { RamadanRepository.toggleChecklistItem(context, item.id, todayKey) },
                                modifier = Modifier.testTag("ram_chk_box_${item.id}")
                            )
                            Text(title, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                            if (item.isCustom) {
                                IconButton(onClick = { RamadanRepository.deleteChecklistItem(context, item.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove checklist item", modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDeedDialog) {
        var titleInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDeedDialog = false },
            title = { Text(AppLocalization.get("ram_add_custom_deed", language)) },
            text = {
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "نیکی / عبادت کا عنوان"
                                AppLanguage.ARABIC -> "اسم العمل الصالح"
                                AppLanguage.HINDI -> "नेकी / इबादत का नाम"
                                AppLanguage.ENGLISH -> "Activity title"
                            }
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.testTag("ram_custom_deed_input")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isNotBlank()) {
                            RamadanRepository.addCustomDeed(context, titleInput, DeedCategory.WORSHIP)
                        }
                        showAddDeedDialog = false
                    },
                    modifier = Modifier.testTag("ram_confirm_add_deed_btn")
                ) { Text(AppLocalization.get("save", language)) }
            },
            dismissButton = { TextButton(onClick = { showAddDeedDialog = false }) { Text(AppLocalization.get("cancel", language)) } }
        )
    }

    if (showAddSadaqahDialog) {
        var amountText by remember { mutableStateOf("") }
        var currencyText by remember { mutableStateOf("USD") }
        var causeText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddSadaqahDialog = false },
            title = { Text(AppLocalization.get("ram_add_sadaqah", language)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = {
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "رقم (اختیاری)"
                                    AppLanguage.ARABIC -> "المبلغ (اختياري)"
                                    AppLanguage.HINDI -> "राशि (वैकल्पिक)"
                                    AppLanguage.ENGLISH -> "Amount (optional)"
                                }
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.testTag("ram_sadaqah_amount_input")
                    )
                    OutlinedTextField(
                        value = currencyText,
                        onValueChange = { currencyText = it.uppercase().take(5) },
                        label = {
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "کرنسی (PKR, USD, SAR)"
                                    AppLanguage.ARABIC -> "العملة (PKR, USD, SAR)"
                                    AppLanguage.HINDI -> "मुद्रा (PKR, USD, SAR)"
                                    AppLanguage.ENGLISH -> "Currency (PKR, USD, SAR...)"
                                }
                            )
                        },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = causeText,
                        onValueChange = { causeText = it },
                        label = {
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "مصرف / مستحق"
                                    AppLanguage.ARABIC -> "الجهة / المستحق"
                                    AppLanguage.HINDI -> "मदद का उद्देश्य / प्राप्तकर्ता"
                                    AppLanguage.ENGLISH -> "Cause / Recipient note"
                                }
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.testTag("ram_sadaqah_cause_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        RamadanRepository.addSadaqahRecord(context, amountText, currencyText, causeText)
                        showAddSadaqahDialog = false
                    },
                    modifier = Modifier.testTag("ram_confirm_add_sadaqah_btn")
                ) { Text(AppLocalization.get("save", language)) }
            },
            dismissButton = { TextButton(onClick = { showAddSadaqahDialog = false }) { Text(AppLocalization.get("cancel", language)) } }
        )
    }

    if (showAddChecklistDialog) {
        var titleInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddChecklistDialog = false },
            title = { Text(AppLocalization.get("ram_add_checklist", language)) },
            text = {
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "تیاری کا نکتہ"
                                AppLanguage.ARABIC -> "عنصر الاستعداد"
                                AppLanguage.HINDI -> "तैयारी का आइटम"
                                AppLanguage.ENGLISH -> "Preparation item"
                            }
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.testTag("ram_custom_checklist_input")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isNotBlank()) {
                            RamadanRepository.addChecklistItem(context, titleInput)
                        }
                        showAddChecklistDialog = false
                    },
                    modifier = Modifier.testTag("ram_confirm_add_checklist_btn")
                ) { Text(AppLocalization.get("save", language)) }
            },
            dismissButton = { TextButton(onClick = { showAddChecklistDialog = false }) { Text(AppLocalization.get("cancel", language)) } }
        )
    }
}

@Composable
private fun RamadanDuaCard(
    dua: Dua,
    language: AppLanguage,
    isBookmarked: Boolean,
    isPlaying: Boolean,
    hasGenuineAudio: Boolean,
    onPlayToggle: () -> Unit,
    onBookmarkToggle: () -> Unit
) {
    val localizedTitle = LocalizedContentResolver.resolveDuaTitle(dua, language)
    val resolvedTranslation = LocalizedContentResolver.resolveDuaTranslation(dua, language)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("ram_dua_card_${dua.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = localizedTitle,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onBookmarkToggle, modifier = Modifier.testTag("ram_dua_bm_${dua.id}")) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = AppLocalization.get("bookmarks", language),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = dua.arabic,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = QuranNaskhFontFamily,
                    lineHeight = 36.sp,
                    textDirection = TextDirection.Rtl
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            if (language != AppLanguage.ARABIC) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = dua.transliteration,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = resolvedTranslation.text ?: resolvedTranslation.unavailableNotice.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${dua.reference} • ${dua.grade}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                if (hasGenuineAudio) {
                    TextButton(
                        onClick = onPlayToggle,
                        modifier = Modifier.heightIn(min = 48.dp).testTag("ram_dua_play_${dua.id}")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.VolumeUp,
                            contentDescription = if (isPlaying) AppLocalization.get("pause", language) else AppLocalization.get("play", language)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (isPlaying) {
                                AppLocalization.get("ram_stop", language)
                            } else {
                                AppLocalization.get("play", language)
                            }
                        )
                    }
                }
            }
        }
    }
}
