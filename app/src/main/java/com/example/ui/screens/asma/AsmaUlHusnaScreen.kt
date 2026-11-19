package com.example.ui.screens.asma

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.asma.AsmaData
import com.example.data.asma.AsmaFilterOption
import com.example.data.asma.AsmaName
import com.example.data.asma.AsmaPronunciationController
import com.example.data.asma.AsmaRepository
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors
import com.example.ui.theme.QuranNaskhFontFamily

@Composable
fun AsmaUlHusnaScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        AsmaRepository.init(context)
    }
    DisposableEffect(Unit) {
        onDispose { AsmaPronunciationController.stop() }
    }

    val state by AsmaRepository.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf(AsmaFilterOption.ALL) }
    var selectedName by remember { mutableStateOf<AsmaName?>(null) }
    var showScholarlyNoteExpanded by remember { mutableStateOf(false) }

    val dailyName = remember { AsmaRepository.dailyName() }
    val filteredNames = remember(searchQuery, selectedFilter, state.favoriteNumbers) {
        AsmaData.filterAndSearch(searchQuery, selectedFilter, state.favoriteNumbers)
    }

    BackHandler(enabled = selectedName != null) {
        AsmaPronunciationController.stop()
        selectedName = null
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("asma_ul_husna", language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .widthIn(max = 680.dp)
                    .fillMaxSize()
                    .testTag("asma_names_list")
            ) {
                item(key = "daily_hero") {
                    DailyAsmaCard(
                        dailyName = dailyName,
                        language = language,
                        exploredCount = state.exploredNumbers.size,
                        favoriteCount = state.favoriteNumbers.size,
                        onOpenDaily = {
                            AsmaRepository.markExplored(context, dailyName.number)
                            selectedName = dailyName
                        }
                    )
                }

                item(key = "scholarly_note") {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showScholarlyNoteExpanded = !showScholarlyNoteExpanded }
                            .testTag("asma_scholarly_note_toggle")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = AppLocalization.get("asma_scholarly_note_title", language),
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = AppLocalization.get("asma_scholarly_note_title", language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (showScholarlyNoteExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AsmaData.scholarlyDiversitySummary(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                maxLines = if (showScholarlyNoteExpanded) Int.MAX_VALUE else 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.testTag("asma_scholarly_note_text")
                            )
                        }
                    }
                }

                item(key = "search_bar") {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.testTag("asma_search_clear")
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        placeholder = { Text(AppLocalization.get("asma_search_hint", language)) },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("asma_search_input")
                    )
                }

                item(key = "filters_row") {
                    val filters = listOf(
                        AsmaFilterOption.ALL to AppLocalization.get("asma_filter_all", language),
                        AsmaFilterOption.QURANIC to AppLocalization.get("asma_filter_quranic", language),
                        AsmaFilterOption.HADITH to AppLocalization.get("asma_filter_hadith", language),
                        AsmaFilterOption.COMPILATION to AppLocalization.get("asma_filter_compilation", language),
                        AsmaFilterOption.FAVORITES to "${AppLocalization.get("asma_filter_favorites", language)} (${state.favoriteNumbers.size})"
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filters) { (opt, label) ->
                            FilterChip(
                                selected = selectedFilter == opt,
                                onClick = { selectedFilter = opt },
                                label = { Text(label) },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("asma_filter_${opt.name.lowercase()}")
                            )
                        }
                    }
                }

                if (filteredNames.isEmpty()) {
                    item(key = "empty_state") {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("asma_empty_state")
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = AppLocalization.get("asma_empty_results", language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(filteredNames, key = { it.number }) { item ->
                        val isFav = state.favoriteNumbers.contains(item.number)
                        val isExplored = state.exploredNumbers.contains(item.number)
                        AsmaNameRowCard(
                            name = item,
                            language = language,
                            isFavorite = isFav,
                            isExplored = isExplored,
                            onClick = {
                                AsmaRepository.markExplored(context, item.number)
                                selectedName = item
                            },
                            onToggleFavorite = {
                                AsmaRepository.toggleFavorite(context, item.number)
                            }
                        )
                    }
                }
            }
        }
    }

    selectedName?.let { current ->
        AsmaDetailSheet(
            name = current,
            language = language,
            isFavorite = state.favoriteNumbers.contains(current.number),
            onToggleFavorite = { AsmaRepository.toggleFavorite(context, current.number) },
            onDismiss = { selectedName = null }
        )
    }
}

@Composable
private fun DailyAsmaCard(
    dailyName: AsmaName,
    language: AppLanguage,
    exploredCount: Int,
    favoriteCount: Int,
    onOpenDaily: () -> Unit
) {
    Card(
        onClick = onOpenDaily,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("asma_daily_name_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = AppLocalization.get("asma_daily_title", language),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${AppLocalization.get("asma_explored_label", language)}: $exploredCount / 99",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.testTag("asma_explored_count")
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (exploredCount / 99f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .testTag("asma_progress_bar")
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dailyName.transliteration,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = dailyName.localizedMeaning(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${AppLocalization.get("asma_favorites_count", language)}: $favoriteCount",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                }
                Text(
                    text = dailyName.arabic,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = QuranNaskhFontFamily,
                        fontSize = 32.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun AsmaNameRowCard(
    name: AsmaName,
    language: AppLanguage,
    isFavorite: Boolean,
    isExplored: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("asma_card_${name.number}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isExplored) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primaryContainer
                    )
            ) {
                Text(
                    text = name.number.toString(),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (isExplored) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name.transliteration,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = name.localizedMeaning(language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = evidenceLabel(name.evidenceCategory, language) +
                        (name.quranReference?.let { " · Quran $it" } ?: ""),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name.arabic,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = QuranNaskhFontFamily,
                    fontSize = 24.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("asma_fav_btn_${name.number}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite ${name.transliteration}",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
