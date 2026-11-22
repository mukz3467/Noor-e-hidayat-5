package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppLanguage
import com.example.data.podcast.PodcastFeedStatus
import com.example.data.podcast.PodcastLanguage
import com.example.data.repository.YouTubeRepository
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors
import com.example.ui.viewmodel.PodcastLanguageSubView
import com.example.ui.viewmodel.PodcastLibraryTab
import com.example.ui.viewmodel.PodcastsViewModel

@Composable
fun PodcastsScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = remember(context.applicationContext) {
        PodcastsViewModel(context.applicationContext)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var visibleExtraChannelsCount by remember(uiState.selectedLanguage) { mutableIntStateOf(20) }

    // Cleanly manage foreground ~30-minute refresh ticker + ON_RESUME staleness refresh
    DisposableEffect(viewModel, lifecycleOwner) {
        viewModel.startForegroundRefreshTicker()
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshIfStale()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.dispose()
        }
    }

    // Intercept system back button inside Scholar Profile to return to Directory/Suggestions first
    BackHandler(enabled = uiState.selectedScholar != null) {
        viewModel.closeScholarProfile()
    }

    val favoriteIds = remember(uiState.favoritePodcasts) {
        uiState.favoritePodcasts.map { it.id }.toSet()
    }

    val topBarTitle = uiState.selectedScholar?.displayNameNative
        ?: AppLocalization.get("podcasts", language)

    Scaffold(
        topBar = {
            NoorTopBar(
                title = topBarTitle,
                language = language,
                onBackClick = {
                    if (uiState.selectedScholar != null) {
                        viewModel.closeScholarProfile()
                    } else {
                        onBack()
                    }
                },
                actions = {
                    if (uiState.selectedLanguage != null) {
                        IconButton(
                            onClick = { viewModel.refreshActiveView() },
                            modifier = Modifier
                                .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                                .testTag("podcast_refresh_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = AppLocalization.get("pod_refresh", language)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val listState = rememberLazyListState()
        LaunchedEffect(uiState.activePlayableVideoId) {
            if (uiState.activePlayableVideoId != null) {
                listState.animateScrollToItem(0)
            }
        }
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("podcasts_lazy_column")
        ) {
            // Subtle progress bar when silently refreshing in foreground (keeps existing verified videos visible)
            if (uiState.isRefreshing) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("podcast_refreshing_indicator")
                    )
                }
            }

            // Active Official In-App YouTube Player when an episode is playing
            val activeId = uiState.activePlayableVideoId
            if (activeId != null) {
                item(key = "active_player_$activeId") {
                    val activeVerified = uiState.activeVideo
                    val langBadge = activeVerified?.language?.localizedBadge(language)
                        ?: uiState.activeLegacyVideo?.verifiedPodcastLanguage?.let {
                            AppLocalization.get(it.labelKey, language)
                        }.orEmpty()
                    val sourceBadge = activeVerified?.sourceKind?.let {
                        AppLocalization.get(it.labelKey, language)
                    }
                    ActivePodcastPlayerSection(
                        videoId = activeId,
                        title = uiState.activePlayableTitle.orEmpty(),
                        channelName = uiState.activePlayableChannel.orEmpty(),
                        videoUrl = uiState.activePlayableUrl ?: "https://www.youtube.com/watch?v=$activeId",
                        languageBadge = langBadge,
                        sourceBadge = sourceBadge,
                        language = language,
                        isFavorite = favoriteIds.contains(activeId),
                        onToggleFavorite = {
                            if (activeVerified != null) {
                                viewModel.toggleFavorite(activeVerified)
                            } else {
                                uiState.activeLegacyVideo?.let { viewModel.toggleFavoriteLegacy(it) }
                            }
                        },
                        onDurationDetected = { secs ->
                            viewModel.onPlayerVerifiedDuration(activeId, secs)
                        }
                    )
                }
            }

            // Search Bar (filters scholars/channels in Directory OR verified long-form videos in Feeds/Profile)
            item {
                val hintKey = if (uiState.selectedScholar != null ||
                    uiState.languageSubView == PodcastLanguageSubView.SUGGESTED_VIDEOS ||
                    uiState.libraryTab != PodcastLibraryTab.LANGUAGE_LIBRARY
                ) {
                    "pod_search_videos_hint"
                } else {
                    "pod_search_scholars_hint"
                }
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = {
                        Text(
                            AppLocalization.get(hintKey, language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { viewModel.updateSearchQuery("") },
                                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = AppLocalization.get("cancel", language))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("podcast_search_input")
                )
            }

            // Library Mode Tabs: Verified Library | Saved Favorites | Recently Watched
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = uiState.libraryTab == PodcastLibraryTab.LANGUAGE_LIBRARY,
                            onClick = { viewModel.selectLibraryTab(PodcastLibraryTab.LANGUAGE_LIBRARY) },
                            leadingIcon = { Icon(Icons.Default.Podcasts, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text(AppLocalization.get("pod_verified_rss", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("pod_tab_feeds")
                        )
                    }
                    item {
                        FilterChip(
                            selected = uiState.libraryTab == PodcastLibraryTab.FAVORITES,
                            onClick = { viewModel.selectLibraryTab(PodcastLibraryTab.FAVORITES) },
                            leadingIcon = { Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text("${AppLocalization.get("pod_favorites_tab", language)} (${uiState.favoritePodcasts.size})") },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("pod_tab_favorites")
                        )
                    }
                    item {
                        FilterChip(
                            selected = uiState.libraryTab == PodcastLibraryTab.RECENTLY_WATCHED,
                            onClick = { viewModel.selectLibraryTab(PodcastLibraryTab.RECENTLY_WATCHED) },
                            leadingIcon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text("${AppLocalization.get("pod_recent_tab", language)} (${uiState.recentPodcasts.size})") },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("pod_tab_recent")
                        )
                    }
                }
            }

            // STRICTLY SEPARATED 3 LANGUAGE SECTIONS: Urdu Podcasts | English Podcasts | Arabic Podcasts
            if (uiState.libraryTab == PodcastLibraryTab.LANGUAGE_LIBRARY) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PodcastLanguage.entries, key = { it.id }) { langOption ->
                            FilterChip(
                                selected = uiState.selectedLanguage == langOption,
                                onClick = { viewModel.selectLanguage(langOption) },
                                leadingIcon = {
                                    Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                label = { Text(AppLocalization.get(langOption.labelKey, language)) },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("pod_lang_${langOption.id}")
                            )
                        }
                    }
                }
            }

            // BRANCH 1: Saved Favorites / Recently Watched
            if (uiState.libraryTab == PodcastLibraryTab.FAVORITES ||
                uiState.libraryTab == PodcastLibraryTab.RECENTLY_WATCHED
            ) {
                val baseList = if (uiState.libraryTab == PodcastLibraryTab.FAVORITES) {
                    uiState.favoritePodcasts
                } else {
                    uiState.recentPodcasts
                }
                val filteredSaved = YouTubeRepository.searchLocalVerifiedLectures(
                    items = baseList,
                    query = uiState.searchQuery
                )
                if (filteredSaved.isEmpty()) {
                    item {
                        PodcastInfoCard(
                            tag = "podcast_empty_card",
                            message = AppLocalization.get(
                                if (uiState.libraryTab == PodcastLibraryTab.FAVORITES) "lectures_empty_favorites"
                                else "lectures_empty_recent",
                                language
                            )
                        )
                    }
                } else {
                    items(filteredSaved, key = { "saved_${it.id}" }) { episode ->
                        LegacySavedPodcastCard(
                            episode = episode,
                            language = language,
                            isPlayingNow = uiState.activePlayableVideoId == episode.id,
                            isFavorite = favoriteIds.contains(episode.id),
                            onPlay = { viewModel.playLegacyItem(episode) },
                            onToggleFavorite = { viewModel.toggleFavoriteLegacy(episode) }
                        )
                    }
                }
            }
            // BRANCH 2: No Language Selected Yet (Never mixes feeds on open!)
            else if (uiState.selectedLanguage == null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("podcast_select_language_prompt")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = AppLocalization.get("pod_select_language_prompt", language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            if (uiState.totalCatalogChannels > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                PodcastMetaPill(
                                    text = String.format(
                                        AppLocalization.get("pod_catalog_channels_badge", language),
                                        (uiState.totalCatalogChannels / 10) * 10
                                    ),
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = AppLocalization.get("pod_auto_refresh_note", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            // BRANCH 3: Inside a Specific Scholar or Channel Profile
            else if (uiState.selectedScholar != null) {
                val scholar = uiState.selectedScholar!!
                val result = uiState.scholarResult

                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("podcast_scholar_profile_header")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(
                                    onClick = { viewModel.closeScholarProfile() },
                                    modifier = Modifier
                                        .sizeIn(minHeight = 48.dp)
                                        .testTag("podcast_back_to_directory_btn")
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(AppLocalization.get("pod_back_to_scholars", language))
                                }
                                PodcastMetaPill(
                                    text = AppLocalization.get(scholar.primarySourceKind.labelKey, language),
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Text(
                                text = scholar.displayNameNative,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (!scholar.displayNameEn.equals(scholar.displayNameNative, ignoreCase = true)) {
                                Text(
                                    text = scholar.displayNameEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (result != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = String.format(
                                        AppLocalization.get("pod_verified_count", language),
                                        result.verifiedVideoCount
                                    ),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.testTag("podcast_verified_count_label")
                                )
                                if (result.rejectedCount > 0) {
                                    Text(
                                        text = String.format(
                                            AppLocalization.get("pod_shorts_filtered_note", language),
                                            result.rejectedCount
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                if (result != null) {
                    item {
                        PodcastStatusBanner(
                            status = result.status,
                            lastUpdatedMs = result.lastUpdatedMs,
                            language = language
                        )
                    }
                }

                // Dynamic non-empty category chips (NEVER displays empty categories!)
                val nonEmptyCats = uiState.nonEmptyScholarCategories
                if (nonEmptyCats.isNotEmpty()) {
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("podcast_profile_categories_row")
                        ) {
                            items(nonEmptyCats, key = { it.first.id }) { (category, count) ->
                                FilterChip(
                                    selected = uiState.selectedCategory == category,
                                    onClick = { viewModel.selectCategory(category) },
                                    label = { Text("${AppLocalization.get(category.labelKey, language)} ($count)") },
                                    colors = noorSelectableChipColors(),
                                    modifier = Modifier.testTag("pod_cat_${category.id}")
                                )
                            }
                        }
                    }
                }

                val pagedVideos = uiState.paginatedScholarVideos
                if (uiState.isLoading && pagedVideos.isEmpty()) {
                    item { PodcastLoadingBlock(language) }
                } else if (result?.status == PodcastFeedStatus.UNAVAILABLE_NO_SOURCES) {
                    item {
                        PodcastInfoCard(
                            tag = "podcast_unavailable_card",
                            message = AppLocalization.get("pod_unavailable_scholar_msg", language)
                        )
                    }
                } else if (result?.status == PodcastFeedStatus.ERROR_NO_CACHE && pagedVideos.isEmpty()) {
                    item {
                        PodcastErrorCard(
                            language = language,
                            onRetry = { viewModel.refreshActiveView() }
                        )
                    }
                } else if (pagedVideos.isEmpty()) {
                    item {
                        PodcastInfoCard(
                            tag = "podcast_empty_card",
                            message = AppLocalization.get(
                                if (uiState.searchQuery.isNotBlank()) "pod_empty_state" else "pod_no_episodes_for_selection",
                                language
                            ),
                            onRefresh = { viewModel.refreshActiveView() },
                            refreshLabel = AppLocalization.get("pod_refresh", language)
                        )
                    }
                } else {
                    items(pagedVideos, key = { "scholar_vid_${it.videoId}" }) { video ->
                        VerifiedPodcastVideoCard(
                            video = video,
                            language = language,
                            isPlayingNow = uiState.activePlayableVideoId == video.videoId,
                            isFavorite = favoriteIds.contains(video.videoId),
                            onPlay = { viewModel.playVideo(video) },
                            onToggleFavorite = { viewModel.toggleFavorite(video) }
                        )
                    }
                    if (uiState.canLoadMoreScholarVideos) {
                        item {
                            LoadMorePodcastsButton(
                                isLoadingMore = uiState.isLoadingMore,
                                language = language,
                                onClick = { viewModel.loadMoreVideos() }
                            )
                        }
                    }
                }
            }
            // BRANCH 4: Language Selected -> Suggested Videos (100+ Long-Form) OR Scholars & Channels (350+ Catalog)
            else {
                val activeLang = uiState.selectedLanguage!!
                val suggestionsResult = uiState.languageSuggestionsResult

                // Sub-view toggle: Suggested Videos (100+) | Scholars & Channels (110+)
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = uiState.languageSubView == PodcastLanguageSubView.SUGGESTED_VIDEOS,
                            onClick = { viewModel.selectLanguageSubView(PodcastLanguageSubView.SUGGESTED_VIDEOS) },
                            leadingIcon = { Icon(Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = {
                                val countSuffix = suggestionsResult?.verifiedVideoCount?.takeIf { it > 0 }?.let { " ($it)" }.orEmpty()
                                Text("${AppLocalization.get("pod_subtab_suggestions", language)}$countSuffix")
                            },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("pod_subtab_suggestions")
                        )
                        FilterChip(
                            selected = uiState.languageSubView == PodcastLanguageSubView.SCHOLARS_DIRECTORY,
                            onClick = { viewModel.selectLanguageSubView(PodcastLanguageSubView.SCHOLARS_DIRECTORY) },
                            leadingIcon = { Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = {
                                val chSuffix = if (uiState.languageChannelCount > 0) " (${uiState.languageChannelCount})" else ""
                                Text("${AppLocalization.get("pod_subtab_scholars", language)}$chSuffix")
                            },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("pod_subtab_scholars")
                        )
                    }
                }

                if (uiState.languageSubView == PodcastLanguageSubView.SUGGESTED_VIDEOS) {
                    if (suggestionsResult != null) {
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("podcast_suggestions_summary")
                            ) {
                                Text(
                                    text = String.format(
                                        AppLocalization.get("pod_verified_count", language),
                                        suggestionsResult.verifiedVideoCount
                                    ) + " • ${suggestionsResult.totalVerifiedChannelsInLanguage} ${activeLang.localizedBadge(language)} Channels" +
                                        if (suggestionsResult.rejectedCount > 0) " • ${suggestionsResult.rejectedCount} Shorts filtered" else "",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (suggestionsResult.status == PodcastFeedStatus.OFFLINE_CACHE ||
                                    suggestionsResult.status == PodcastFeedStatus.QUOTA_EXCEEDED_CACHE
                                ) {
                                    PodcastStatusBanner(
                                        status = suggestionsResult.status,
                                        lastUpdatedMs = suggestionsResult.lastUpdatedMs,
                                        language = language
                                    )
                                }
                            }
                        }
                    }

                    // Dynamic non-empty categories across the 100+ long-form language suggestions
                    val nonEmptyLangCats = uiState.nonEmptyLanguageCategories
                    if (nonEmptyLangCats.isNotEmpty()) {
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("podcast_language_categories_row")
                            ) {
                                items(nonEmptyLangCats, key = { it.first.id }) { (category, count) ->
                                    FilterChip(
                                        selected = uiState.selectedCategory == category,
                                        onClick = { viewModel.selectCategory(category) },
                                        label = { Text("${AppLocalization.get(category.labelKey, language)} ($count)") },
                                        colors = noorSelectableChipColors(),
                                        modifier = Modifier.testTag("pod_cat_${category.id}")
                                    )
                                }
                            }
                        }
                    }

                    val pagedSuggestions = uiState.paginatedLanguageVideos
                    if (uiState.isLoading && pagedSuggestions.isEmpty()) {
                        item { PodcastLoadingBlock(language) }
                    } else if (suggestionsResult?.status == PodcastFeedStatus.ERROR_NO_CACHE && pagedSuggestions.isEmpty()) {
                        item {
                            PodcastErrorCard(
                                language = language,
                                onRetry = { viewModel.refreshActiveView() }
                            )
                        }
                    } else if (pagedSuggestions.isEmpty()) {
                        item {
                            PodcastInfoCard(
                                tag = "podcast_empty_card",
                                message = AppLocalization.get(
                                    if (uiState.searchQuery.isNotBlank()) "pod_empty_state" else "pod_no_episodes_for_selection",
                                    language
                                ),
                                onRefresh = { viewModel.refreshActiveView() },
                                refreshLabel = AppLocalization.get("pod_refresh", language)
                            )
                        }
                    } else {
                        items(pagedSuggestions, key = { "lang_vid_${it.videoId}" }) { video ->
                            VerifiedPodcastVideoCard(
                                video = video,
                                language = language,
                                isPlayingNow = uiState.activePlayableVideoId == video.videoId,
                                isFavorite = favoriteIds.contains(video.videoId),
                                onPlay = { viewModel.playVideo(video) },
                                onToggleFavorite = { viewModel.toggleFavorite(video) }
                            )
                        }
                        if (uiState.canLoadMoreLanguageVideos) {
                            item {
                                LoadMorePodcastsButton(
                                    isLoadingMore = uiState.isLoadingMore,
                                    language = language,
                                    onClick = { viewModel.loadMoreVideos() }
                                )
                            }
                        }
                    }
                } else {
                    // SCHOLARS & CHANNELS DIRECTORY (Featured Scholars + Great History of Islam Channel + 100+ Verified Channels)
                    val dedicatedChannels = viewModel.channelsForSelectedLanguage()
                    if (dedicatedChannels.isNotEmpty()) {
                        item {
                            Text(
                                text = AppLocalization.get("pod_channels_section", language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        items(dedicatedChannels, key = { "ch_${it.id}" }) { channelProfile ->
                            PodcastScholarDirectoryCard(
                                scholar = channelProfile,
                                cachedVideoCount = uiState.cachedCountsByScholarId[channelProfile.id] ?: 0,
                                language = language,
                                onClick = { viewModel.openScholarProfile(channelProfile) }
                            )
                        }
                    }

                    val scholars = viewModel.scholarsForSelectedLanguage()
                    item {
                        Text(
                            text = String.format(AppLocalization.get("pod_scholars_section", language), scholars.size),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    items(scholars, key = { "sch_${it.id}" }) { scholar ->
                        PodcastScholarDirectoryCard(
                            scholar = scholar,
                            cachedVideoCount = uiState.cachedCountsByScholarId[scholar.id] ?: 0,
                            language = language,
                            onClick = { viewModel.openScholarProfile(scholar) }
                        )
                    }

                    val extraChannels = viewModel.extraCatalogChannelsForSelectedLanguage()
                    if (extraChannels.isNotEmpty()) {
                        item {
                            Text(
                                text = String.format(
                                    AppLocalization.get("pod_more_verified_channels", language),
                                    extraChannels.size
                                ),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        val shownExtra = extraChannels.take(visibleExtraChannelsCount)
                        items(shownExtra, key = { "extra_${it.id}" }) { extraChannel ->
                            PodcastScholarDirectoryCard(
                                scholar = extraChannel,
                                cachedVideoCount = uiState.cachedCountsByScholarId[extraChannel.id] ?: 0,
                                language = language,
                                onClick = { viewModel.openScholarProfile(extraChannel) }
                            )
                        }
                        if (extraChannels.size > visibleExtraChannelsCount) {
                            item {
                                OutlinedButton(
                                    onClick = { visibleExtraChannelsCount += 25 },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .sizeIn(minHeight = 48.dp)
                                        .testTag("podcast_load_more_channels_btn")
                                ) {
                                    Text(
                                        "${AppLocalization.get("pod_load_more", language)} (${shownExtra.size}/${extraChannels.size})"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PodcastLoadingBlock(language: AppLanguage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
            .testTag("podcast_loading_state")
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = AppLocalization.get("pod_loading", language),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PodcastErrorCard(language: AppLanguage, onRetry: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("podcast_error_card")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = AppLocalization.get("lectures_offline_empty_error", language),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.sizeIn(minHeight = 48.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(AppLocalization.get("retry", language))
            }
        }
    }
}

@Composable
private fun PodcastInfoCard(
    tag: String,
    message: String,
    onRefresh: (() -> Unit)? = null,
    refreshLabel: String? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            if (onRefresh != null && refreshLabel != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onRefresh,
                    modifier = Modifier.sizeIn(minHeight = 48.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(refreshLabel)
                }
            }
        }
    }
}

@Composable
private fun LoadMorePodcastsButton(
    isLoadingMore: Boolean,
    language: AppLanguage,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = !isLoadingMore,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 48.dp)
            .testTag("podcast_load_more_btn")
    ) {
        if (isLoadingMore) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(AppLocalization.get("pod_load_more", language))
    }
}
