package com.example.ui.screens.shifa

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppLanguage
import com.example.data.repository.QuranAudioManager
import com.example.data.shifa.*
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShifaRuqyahScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    initialCategory: ShifaCategory = ShifaCategory.QURANIC_SHIFA
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        ShifaRuqyahRepository.init(context)
    }

    val favoriteIds by ShifaRuqyahRepository.favoriteIds.collectAsStateWithLifecycle()
    val bookmarkedIds by ShifaRuqyahRepository.bookmarkedIds.collectAsStateWithLifecycle()

    var selectedCategory by rememberSaveable { mutableStateOf(initialCategory) }
    var searchQuery by remember { mutableStateOf("") }
    var savedFilter by rememberSaveable { mutableStateOf(ShifaSavedFilter.ALL) }
    var selectedTopic by remember { mutableStateOf<TreatmentConditionTopic?>(null) }

    var loadingOnlineIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var onlineErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var onlineRefreshVersion by remember { mutableIntStateOf(0) }
    var audioNotice by remember { mutableStateOf<String?>(null) }

    val isAudioPlaying by QuranAudioManager.isAudioPlaying
    val isBuffering by QuranAudioManager.isBuffering
    val activeAudioSurah by QuranAudioManager.currentPlayingSurahNumber
    val activeAudioAyah by QuranAudioManager.currentPlayingAyahNumber
    val managerAudioError by QuranAudioManager.audioErrorMessage

    val displayedEntries = remember(
        selectedCategory,
        searchQuery,
        savedFilter,
        selectedTopic,
        language,
        favoriteIds,
        bookmarkedIds,
        onlineRefreshVersion
    ) {
        if (selectedCategory == ShifaCategory.TREATMENT_INDEX && selectedTopic != null && searchQuery.isBlank()) {
            val base = selectedTopic!!.matchedItemIds.mapNotNull { id ->
                ShifaRuqyahRepository.allItems.firstOrNull { it.id == id }
            }
            val filtered = when (savedFilter) {
                ShifaSavedFilter.ALL -> base
                ShifaSavedFilter.FAVORITES -> base.filter { it.id in favoriteIds }
                ShifaSavedFilter.BOOKMARKS -> base.filter { it.id in bookmarkedIds }
            }
            filtered.map { ShifaRuqyahRepository.resolveEntry(context, it, language) }
        } else {
            ShifaRuqyahRepository.searchEntries(
                context = context,
                language = language,
                query = searchQuery,
                category = selectedCategory,
                savedFilter = savedFilter
            )
        }
    }

    val matchingTopics = remember(searchQuery) {
        ShifaRuqyahRepository.matchConditionTopics(searchQuery)
    }

    val showUnmappedNotice = remember(searchQuery, displayedEntries, matchingTopics, selectedCategory, language) {
        searchQuery.isNotBlank() &&
            displayedEntries.isEmpty() &&
            (selectedCategory == ShifaCategory.TREATMENT_INDEX && matchingTopics.isEmpty() ||
                ShifaRuqyahRepository.isUnmappedConditionQuery(context, language, searchQuery))
    }

    val listState = rememberLazyListState()
    val activeAudioBanner = managerAudioError ?: audioNotice
    val activeTopics = if (selectedTopic != null) {
        listOf(selectedTopic!!)
    } else if (searchQuery.isNotBlank()) {
        matchingTopics
    } else {
        emptyList()
    }

    LaunchedEffect(selectedTopic, showUnmappedNotice, selectedCategory, activeTopics.size, activeAudioBanner != null) {
        val baseIdx = 2 + if (!activeAudioBanner.isNullOrBlank()) 1 else 0
        val hasTreatmentTopics = selectedCategory == ShifaCategory.TREATMENT_INDEX
        val scopeIdx = baseIdx + if (hasTreatmentTopics) 1 else 0
        val unmappedIdx = scopeIdx + if (hasTreatmentTopics && activeTopics.isNotEmpty()) 1 else 0
        if (selectedTopic != null && hasTreatmentTopics && activeTopics.isNotEmpty()) {
            listState.scrollToItem(scopeIdx)
        } else if (showUnmappedNotice) {
            listState.scrollToItem(unmappedIdx)
        }
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("shifa_ruqyah", language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("shifa_ruqyah_screen")
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 720.dp)
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 6.dp)
                ) {
                    // Search Bar (pinned so scrolling results never recycles/unmounts the input)
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { newVal ->
                            searchQuery = newVal
                            if (newVal.isNotBlank() && selectedTopic != null) {
                                selectedTopic = null
                            }
                        },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = AppLocalization.get("shifa_search_hint", language)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = AppLocalization.get("close", language)
                                    )
                                }
                            }
                        },
                        placeholder = {
                            Text(
                                text = AppLocalization.get("shifa_search_hint", language),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("shifa_search_input")
                    )

                    // Category Tabs (A - H)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        ShifaCategory.entries.forEach { category ->
                            val isSelected = selectedCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedCategory = category
                                    if (category != ShifaCategory.TREATMENT_INDEX) {
                                        selectedTopic = null
                                    }
                                },
                                label = {
                                    Text(
                                        text = category.title(language),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag(category.testTag)
                            )
                        }
                    }
                }

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .testTag("shifa_entries_list")
                ) {
                    // Scope & Safety Disclaimer Banner
                    item(key = "scope_banner") {
                        ShifaScopeNoticeBanner(language = language)
                    }

                // Active Category Subtitle & Saved Filter Strip (All / Favorites / Bookmarks)
                item(key = "category_header_filters") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = selectedCategory.subtitle(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ShifaSavedFilter.entries.forEach { filter ->
                                val label = when (filter) {
                                    ShifaSavedFilter.ALL -> AppLocalization.get("shifa_filter_all", language)
                                    ShifaSavedFilter.FAVORITES -> "${AppLocalization.get("shifa_filter_favorites", language)} (${favoriteIds.size})"
                                    ShifaSavedFilter.BOOKMARKS -> "${AppLocalization.get("shifa_filter_bookmarks", language)} (${bookmarkedIds.size})"
                                }
                                FilterChip(
                                    selected = savedFilter == filter,
                                    onClick = { savedFilter = filter },
                                    label = { Text(label) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = when (filter) {
                                                ShifaSavedFilter.ALL -> Icons.Default.GridView
                                                ShifaSavedFilter.FAVORITES -> Icons.Default.Favorite
                                                ShifaSavedFilter.BOOKMARKS -> Icons.Default.Bookmark
                                            },
                                            contentDescription = label,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    colors = noorSelectableChipColors(),
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .testTag("shifa_filter_${filter.name.lowercase()}")
                                )
                            }
                        }
                    }
                }

                // Audio error or informational notice
                if (!activeAudioBanner.isNullOrBlank()) {
                    item(key = "audio_notice") {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = activeAudioBanner,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        QuranAudioManager.clearAudioError()
                                        audioNotice = null
                                    },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = AppLocalization.get("close", language),
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }
                }

                // H. Treatment Search Index Chips + Active Topic Scope Card
                if (selectedCategory == ShifaCategory.TREATMENT_INDEX) {
                    item(key = "treatment_index_topics") {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = AppLocalization.get("shifa_index_heading", language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                matchingTopics.forEach { topic ->
                                    val isTopicSelected = selectedTopic?.id == topic.id
                                    FilterChip(
                                        selected = isTopicSelected,
                                        onClick = {
                                            selectedTopic = if (isTopicSelected) null else topic
                                            searchQuery = ""
                                        },
                                        label = { Text(topic.title.get(language)) },
                                        colors = noorSelectableChipColors(),
                                        modifier = Modifier
                                            .minimumInteractiveComponentSize()
                                            .testTag("shifa_topic_${topic.id}")
                                    )
                                }
                            }
                        }
                    }

                    if (activeTopics.isNotEmpty()) {
                        item(key = "treatment_scope_cards") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                activeTopics.forEach { topic ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("shifa_topic_scope_${topic.id}")
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = topic.title.get(language),
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = topic.scopeNote.get(language),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Fallback Banner for unmapped / unverified conditions
                if (showUnmappedNotice) {
                    item(key = "unmapped_fallback") {
                        ShifaUnmappedConditionBanner(
                            language = language,
                            onOpenQuranicShifa = {
                                selectedCategory = ShifaCategory.QURANIC_SHIFA
                                searchQuery = ""
                            },
                            onOpenRuqyah = {
                                selectedCategory = ShifaCategory.RUQYAH_SHARIYYAH
                                searchQuery = ""
                            }
                        )
                    }
                } else if (displayedEntries.isEmpty() && savedFilter != ShifaSavedFilter.ALL) {
                    item(key = "empty_saved") {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("shifa_empty_saved")
                        ) {
                            Text(
                                text = AppLocalization.get("shifa_empty_saved_msg", language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                // Verified Entries List
                items(
                    items = displayedEntries,
                    key = { it.item.id }
                ) { baseEntry ->
                    val item = baseEntry.item
                    val enrichedEntry = baseEntry.copy(
                        isLoadingOnlineTafseer = item.id in loadingOnlineIds,
                        onlineTafseerError = onlineErrors[item.id]
                    )
                    val isThisPlaying = isAudioPlaying &&
                        activeAudioSurah == item.surahNumber &&
                        activeAudioAyah == item.ayahStart
                    val isThisBuffering = isBuffering &&
                        activeAudioSurah == item.surahNumber &&
                        activeAudioAyah == item.ayahStart

                    ShifaEntryCard(
                        context = context,
                        entry = enrichedEntry,
                        language = language,
                        isFavorite = item.id in favoriteIds,
                        isBookmarked = item.id in bookmarkedIds,
                        isPlayingAudio = isThisPlaying,
                        isBufferingAudio = isThisBuffering,
                        onToggleFavorite = { ShifaRuqyahRepository.toggleFavorite(context, item.id) },
                        onToggleBookmark = { ShifaRuqyahRepository.toggleBookmark(context, item.id) },
                        onToggleAudio = {
                            audioNotice = ShifaRuqyahRepository.toggleAudioPlayback(context, item)
                        },
                        onLoadOnlineTafseer = {
                            if (item.id !in loadingOnlineIds) {
                                loadingOnlineIds = loadingOnlineIds + item.id
                                onlineErrors = onlineErrors - item.id
                                scope.launch {
                                    val result = ShifaRuqyahRepository.loadOnlineScholarTafseer(
                                        context = context,
                                        item = item,
                                        language = language
                                    )
                                    loadingOnlineIds = loadingOnlineIds - item.id
                                    result.onSuccess {
                                        onlineRefreshVersion++
                                    }.onFailure { err ->
                                        onlineErrors = onlineErrors + (
                                            item.id to (err.message ?: AppLocalization.get("shifa_online_tafseer_error", language))
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
}
