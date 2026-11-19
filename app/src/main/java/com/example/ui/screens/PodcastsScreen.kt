package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AppLanguage
import com.example.data.repository.FeedLoadStatus
import com.example.data.repository.LectureFeedResult
import com.example.data.repository.PodcastLanguageSection
import com.example.data.repository.PodcastTopicFilter
import com.example.data.repository.YouTubeRepository
import com.example.data.repository.YouTubeVideoItem
import com.example.localization.AppLocalization
import com.example.ui.components.InAppYouTubePlayer
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

private enum class PodcastLibraryMode {
    LANGUAGE_FEEDS,
    FAVORITES,
    RECENTLY_WATCHED
}

@Composable
fun PodcastsScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var libraryMode by rememberSaveable { mutableStateOf(PodcastLibraryMode.LANGUAGE_FEEDS) }
    var selectedPodcastLang by rememberSaveable { mutableStateOf<PodcastLanguageSection?>(null) }
    var selectedTopic by rememberSaveable { mutableStateOf(PodcastTopicFilter.ALL_TOPICS) }
    var searchQuery by remember { mutableStateOf("") }
    var refreshTrigger by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    var feedResult by remember {
        mutableStateOf(
            LectureFeedResult(
                videos = emptyList(),
                status = FeedLoadStatus.IDLE
            )
        )
    }

    var favoritePodcasts by remember { mutableStateOf(YouTubeRepository.getFavoritePodcasts(context)) }
    var recentPodcasts by remember { mutableStateOf(YouTubeRepository.getRecentlyWatchedPodcasts(context)) }
    var activeEpisode by remember { mutableStateOf<YouTubeVideoItem?>(null) }

    // Fetch ONLY when a podcast language is explicitly selected (never mixes all languages by default)
    LaunchedEffect(selectedPodcastLang, selectedTopic, refreshTrigger) {
        val chosenLang = selectedPodcastLang
        if (chosenLang == null) {
            isLoading = false
            feedResult = LectureFeedResult(videos = emptyList(), status = FeedLoadStatus.IDLE)
            return@LaunchedEffect
        }
        isLoading = true
        feedResult = YouTubeRepository.fetchPodcastsForSelection(
            context = context,
            languageSection = chosenLang,
            topicFilter = selectedTopic,
            forceRefresh = refreshTrigger > 0
        )
        isLoading = false
    }

    val favoriteIds = remember(favoritePodcasts) { favoritePodcasts.map { it.id }.toSet() }

    val visibleEpisodes = remember(
        libraryMode,
        feedResult.videos,
        favoritePodcasts,
        recentPodcasts,
        searchQuery
    ) {
        val base = when (libraryMode) {
            PodcastLibraryMode.LANGUAGE_FEEDS -> feedResult.videos
            PodcastLibraryMode.FAVORITES -> favoritePodcasts
            PodcastLibraryMode.RECENTLY_WATCHED -> recentPodcasts
        }
        YouTubeRepository.searchLocalVerifiedLectures(items = base, query = searchQuery)
    }

    fun selectAndPlayEpisode(item: YouTubeVideoItem) {
        activeEpisode = item
        recentPodcasts = YouTubeRepository.recordWatchedPodcast(context, item)
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("podcasts", language),
                language = language,
                onBackClick = onBack,
                actions = {
                    if (selectedPodcastLang != null) {
                        IconButton(
                            onClick = { refreshTrigger++ },
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
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("podcasts_lazy_column")
        ) {
            // Active Official YouTube Player when an episode is selected
            activeEpisode?.let { playing ->
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                            .testTag("in_app_podcast_player")
                    ) {
                        key(playing.id) {
                            InAppYouTubePlayer(
                                videoId = playing.id,
                                language = language,
                                modifier = Modifier.fillMaxSize(),
                                autoplay = true
                            )
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = playing.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = playing.channel,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = AppLocalization.get(playing.verifiedPodcastLanguage.labelKey, language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            val isFav = favoriteIds.contains(playing.id)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        YouTubeRepository.toggleFavoritePodcast(context, playing)
                                        favoritePodcasts = YouTubeRepository.getFavoritePodcasts(context)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .sizeIn(minHeight = 48.dp)
                                        .testTag("active_podcast_fav_btn")
                                ) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = AppLocalization.get(
                                            if (isFav) "lectures_bookmark_remove" else "lectures_bookmark_add",
                                            language
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                OutlinedButton(
                                    onClick = {
                                        try {
                                            context.startActivity(
                                                Intent(Intent.ACTION_VIEW, Uri.parse(playing.videoUrl))
                                            )
                                        } catch (_: Exception) {
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .sizeIn(minHeight = 48.dp)
                                        .testTag("active_podcast_youtube_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = AppLocalization.get("lectures_watch_on_youtube", language),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Search Bar (filters loaded podcast episodes locally)
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            AppLocalization.get("pod_search_hint", language),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" },
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

            // Library Mode Tabs: Language Feeds | Saved Favorites | Recently Watched
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = libraryMode == PodcastLibraryMode.LANGUAGE_FEEDS,
                            onClick = { libraryMode = PodcastLibraryMode.LANGUAGE_FEEDS },
                            leadingIcon = { Icon(Icons.Default.Podcasts, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text(AppLocalization.get("pod_verified_rss", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("pod_tab_feeds")
                        )
                    }
                    item {
                        FilterChip(
                            selected = libraryMode == PodcastLibraryMode.FAVORITES,
                            onClick = { libraryMode = PodcastLibraryMode.FAVORITES },
                            leadingIcon = { Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text("${AppLocalization.get("pod_favorites_tab", language)} (${favoritePodcasts.size})") },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("pod_tab_favorites")
                        )
                    }
                    item {
                        FilterChip(
                            selected = libraryMode == PodcastLibraryMode.RECENTLY_WATCHED,
                            onClick = { libraryMode = PodcastLibraryMode.RECENTLY_WATCHED },
                            leadingIcon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            label = { Text("${AppLocalization.get("pod_recent_tab", language)} (${recentPodcasts.size})") },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("pod_tab_recent")
                        )
                    }
                }
            }

            // Mandatory Separated Language Selector: Urdu | Arabic | English | Language Not Verified
            if (libraryMode == PodcastLibraryMode.LANGUAGE_FEEDS) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PodcastLanguageSection.entries, key = { it.id }) { section ->
                            val isSel = selectedPodcastLang == section
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    selectedPodcastLang = if (isSel) null else section
                                    selectedTopic = PodcastTopicFilter.ALL_TOPICS
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                label = { Text(AppLocalization.get(section.labelKey, language)) },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("pod_lang_${section.id}")
                            )
                        }
                    }
                }

                // Topics inside the selected language (shown when Urdu, Arabic, or English is active)
                if (selectedPodcastLang != null && selectedPodcastLang != PodcastLanguageSection.LANGUAGE_NOT_VERIFIED) {
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(PodcastTopicFilter.entries, key = { it.id }) { topic ->
                                FilterChip(
                                    selected = selectedTopic == topic,
                                    onClick = { selectedTopic = topic },
                                    label = { Text(AppLocalization.get(topic.labelKey, language)) },
                                    colors = noorSelectableChipColors(),
                                    modifier = Modifier.testTag("pod_topic_${topic.id}")
                                )
                            }
                        }
                    }
                }
            }

            // Offline / Expired Cache / Last Updated Status Banner
            if (libraryMode == PodcastLibraryMode.LANGUAGE_FEEDS && selectedPodcastLang != null) {
                if (feedResult.status == FeedLoadStatus.OFFLINE_CACHE) {
                    item {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("podcast_offline_banner")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(Icons.Default.CloudOff, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = AppLocalization.get("pod_offline_banner", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                } else if (feedResult.status == FeedLoadStatus.EXPIRED_CACHE_FALLBACK) {
                    item {
                        val updatedText = YouTubeRepository.formatLastUpdatedDisplay(feedResult.lastUpdatedMs)
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("podcast_expired_cache_banner")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(Icons.Default.WarningAmber, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = String.format(
                                        AppLocalization.get("lectures_expired_cache_banner", language),
                                        updatedText.ifBlank { "-" }
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                } else if (feedResult.lastUpdatedMs > 0L) {
                    item {
                        val updatedText = YouTubeRepository.formatLastUpdatedDisplay(feedResult.lastUpdatedMs)
                        Text(
                            text = String.format(AppLocalization.get("pod_last_updated", language), updatedText),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }

            // Main Content Area
            if (libraryMode == PodcastLibraryMode.LANGUAGE_FEEDS && selectedPodcastLang == null) {
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
                        }
                    }
                }
            } else if (isLoading && visibleEpisodes.isEmpty()) {
                item {
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
            } else if (libraryMode == PodcastLibraryMode.LANGUAGE_FEEDS &&
                feedResult.status == FeedLoadStatus.ERROR_NO_CACHE &&
                visibleEpisodes.isEmpty()
            ) {
                item {
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
                                onClick = { refreshTrigger++ },
                                modifier = Modifier.sizeIn(minHeight = 48.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(AppLocalization.get("retry", language))
                            }
                        }
                    }
                }
            } else if (visibleEpisodes.isEmpty()) {
                item {
                    val emptyMsg = when (libraryMode) {
                        PodcastLibraryMode.FAVORITES -> AppLocalization.get("lectures_empty_favorites", language)
                        PodcastLibraryMode.RECENTLY_WATCHED -> AppLocalization.get("lectures_empty_recent", language)
                        PodcastLibraryMode.LANGUAGE_FEEDS -> if (searchQuery.isNotBlank()) {
                            AppLocalization.get("pod_empty_state", language)
                        } else {
                            AppLocalization.get("pod_no_episodes_for_selection", language)
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("podcast_empty_card")
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
                                text = emptyMsg,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            if (libraryMode == PodcastLibraryMode.LANGUAGE_FEEDS) {
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedButton(
                                    onClick = { refreshTrigger++ },
                                    modifier = Modifier.sizeIn(minHeight = 48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(AppLocalization.get("pod_refresh", language))
                                }
                            }
                        }
                    }
                }
            } else {
                items(visibleEpisodes, key = { "${it.channelId}_${it.id}" }) { episode ->
                    YouTubePodcastEpisodeCard(
                        episode = episode,
                        language = language,
                        isPlayingNow = activeEpisode?.id == episode.id,
                        isFavorite = favoriteIds.contains(episode.id),
                        onPlay = { selectAndPlayEpisode(episode) },
                        onToggleFavorite = {
                            YouTubeRepository.toggleFavoritePodcast(context, episode)
                            favoritePodcasts = YouTubeRepository.getFavoritePodcasts(context)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun YouTubePodcastEpisodeCard(
    episode: YouTubeVideoItem,
    language: AppLanguage,
    isPlayingNow: Boolean,
    isFavorite: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onPlay,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlayingNow) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isPlayingNow) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("podcast_card_${episode.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(116.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = episode.thumbnail,
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = AppLocalization.get("lectures_watch_in_app", language),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = episode.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        lineHeight = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = episode.channel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = AppLocalization.get(episode.verifiedPodcastLanguage.labelKey, language),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (episode.publishedDisplay.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = episode.publishedDisplay,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .testTag("podcast_fav_${episode.id}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = AppLocalization.get(
                        if (isFavorite) "lectures_bookmark_remove" else "lectures_bookmark_add",
                        language
                    ),
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
