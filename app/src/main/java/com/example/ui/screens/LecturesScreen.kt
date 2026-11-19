package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TouchApp
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AppLanguage
import com.example.data.repository.FeedLoadStatus
import com.example.data.repository.LectureCategory
import com.example.data.repository.LectureFeedResult
import com.example.data.repository.YouTubeRepository
import com.example.data.repository.YouTubeVideoItem
import com.example.localization.AppLocalization
import com.example.ui.components.InAppYouTubePlayer
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

private enum class LectureLibraryTab {
    ALL_FEEDS,
    FAVORITES,
    RECENTLY_WATCHED
}

@Composable
fun LecturesScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    initialQuery: String? = null
) {
    val context = LocalContext.current

    var selectedTab by rememberSaveable { mutableStateOf(LectureLibraryTab.ALL_FEEDS) }
    var selectedChannelKey by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedCategory by remember(initialQuery) {
        mutableStateOf<LectureCategory?>(if (!initialQuery.isNullOrBlank()) LectureCategory.LATEST_LECTURES else null)
    }
    var selectedLanguageFilter by rememberSaveable { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf(initialQuery ?: "") }

    var isLoading by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableIntStateOf(0) }
    var feedResult by remember {
        mutableStateOf(
            LectureFeedResult(
                videos = emptyList(),
                status = FeedLoadStatus.IDLE
            )
        )
    }

    var favoriteLectures by remember { mutableStateOf(YouTubeRepository.getFavoriteLectures(context)) }
    var recentLectures by remember { mutableStateOf(YouTubeRepository.getRecentlyWatchedLectures(context)) }
    var activeVideo by remember { mutableStateOf<YouTubeVideoItem?>(null) }

    // Topic-based / channel-based on-demand fetch ONLY when user selects a topic or scholar.
    // Never fetches every configured channel on initial screen open.
    LaunchedEffect(selectedCategory, selectedChannelKey, refreshTrigger) {
        val topic = selectedCategory
        val chKey = selectedChannelKey
        if (topic == null && chKey == null) {
            isLoading = false
            feedResult = LectureFeedResult(videos = emptyList(), status = FeedLoadStatus.IDLE)
            return@LaunchedEffect
        }
        isLoading = true
        feedResult = YouTubeRepository.fetchLecturesForTopic(
            context = context,
            topic = topic,
            channelKey = chKey,
            forceRefresh = refreshTrigger > 0
        )
        isLoading = false
    }

    val favoriteIds = remember(favoriteLectures) { favoriteLectures.map { it.id }.toSet() }

    val visibleVideos = remember(
        selectedTab,
        feedResult.videos,
        favoriteLectures,
        recentLectures,
        searchQuery,
        selectedCategory,
        selectedChannelKey,
        selectedLanguageFilter
    ) {
        val baseList = when (selectedTab) {
            LectureLibraryTab.ALL_FEEDS -> feedResult.videos
            LectureLibraryTab.FAVORITES -> favoriteLectures
            LectureLibraryTab.RECENTLY_WATCHED -> recentLectures
        }
        YouTubeRepository.searchLocalVerifiedLectures(
            items = baseList,
            query = searchQuery,
            category = if (selectedTab == LectureLibraryTab.ALL_FEEDS && selectedChannelKey == null) selectedCategory else null,
            channelKey = if (selectedTab == LectureLibraryTab.ALL_FEEDS) selectedChannelKey else null,
            languageFilter = selectedLanguageFilter
        )
    }

    fun selectAndPlay(video: YouTubeVideoItem) {
        activeVideo = video
        recentLectures = YouTubeRepository.recordWatchedLecture(context, video)
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("lectures", language),
                language = language,
                onBackClick = onBack,
                actions = {
                    if (selectedCategory != null || selectedChannelKey != null) {
                        IconButton(
                            onClick = { refreshTrigger++ },
                            modifier = Modifier
                                .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                                .testTag("lectures_refresh_icon_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = AppLocalization.get("lectures_refresh", language)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val columns = if (maxWidth >= 600.dp) 3 else 2

            LazyColumn(
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("lectures_lazy_column")
            ) {
                // Header Section
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Text(
                            text = AppLocalization.get("lectures_banner_tag", language),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppLocalization.get("lectures_header_title", language),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppLocalization.get("lectures_header_subtitle", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Offline / Expired Cache / Partial Unavailable Status Banners
                if (feedResult.status == FeedLoadStatus.OFFLINE_CACHE) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("lectures_offline_cache_banner")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = AppLocalization.get("lectures_offline_cache_banner", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                } else if (feedResult.status == FeedLoadStatus.EXPIRED_CACHE_FALLBACK) {
                    item {
                        val updatedText = YouTubeRepository.formatLastUpdatedDisplay(feedResult.lastUpdatedMs)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("lectures_expired_cache_banner")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = String.format(
                                        AppLocalization.get("lectures_expired_cache_banner", language),
                                        updatedText.ifBlank { "-" }
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                if (feedResult.unavailableChannels.isNotEmpty() && feedResult.videos.isNotEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("lectures_partial_unavailable_banner")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = String.format(
                                        AppLocalization.get("lectures_partial_unavailable", language),
                                        feedResult.unavailableChannels.joinToString(", ")
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Active Official YouTube IFrame Player + Metadata Card
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val currentPlaying = activeVideo
                        if (currentPlaying != null) {
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Black),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(230.dp)
                                    .testTag("in_app_lecture_player")
                            ) {
                                key(currentPlaying.id) {
                                    InAppYouTubePlayer(
                                        videoId = currentPlaying.id,
                                        language = language,
                                        modifier = Modifier.fillMaxSize(),
                                        autoplay = true
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = currentPlaying.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = currentPlaying.channel,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (currentPlaying.publishedDisplay.isNotBlank()) {
                                            Text(
                                                text = "${AppLocalization.get("lectures_published_label", language)}: ${currentPlaying.publishedDisplay}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    val isFav = favoriteIds.contains(currentPlaying.id)
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                YouTubeRepository.toggleFavoriteLecture(context, currentPlaying)
                                                favoriteLectures = YouTubeRepository.getFavoriteLectures(context)
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .sizeIn(minHeight = 48.dp)
                                                .testTag("active_lecture_bookmark_btn")
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
                                                        Intent(Intent.ACTION_VIEW, Uri.parse(currentPlaying.videoUrl))
                                                    )
                                                } catch (_: Exception) {
                                                }
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .sizeIn(minHeight = 48.dp)
                                                .testTag("active_lecture_open_youtube_btn")
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
                        } else {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("lecture_player_placeholder")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircleOutline,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = AppLocalization.get("lectures_select_prompt", language),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }

                // Search Bar (Local filter within fetched topic/channel feeds only — not global YouTube search)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = AppLocalization.get("lectures_search_hint", language),
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = AppLocalization.get("lectures_search_btn", language),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(
                                        onClick = { searchQuery = "" },
                                        modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = AppLocalization.get("lectures_clear", language),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { }),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_lectures_input")
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppLocalization.get("lectures_local_search_note", language),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                // Library Tabs: Topic Feeds | Saved Favorites | Recently Watched
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedTab == LectureLibraryTab.ALL_FEEDS,
                                onClick = { selectedTab = LectureLibraryTab.ALL_FEEDS },
                                label = { Text(AppLocalization.get("lectures_categories_title", language)) },
                                leadingIcon = {
                                    Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("tab_lectures_all")
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedTab == LectureLibraryTab.FAVORITES,
                                onClick = { selectedTab = LectureLibraryTab.FAVORITES },
                                label = {
                                    Text("${AppLocalization.get("lectures_saved_favorites", language)} (${favoriteLectures.size})")
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("tab_lectures_favorites")
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedTab == LectureLibraryTab.RECENTLY_WATCHED,
                                onClick = { selectedTab = LectureLibraryTab.RECENTLY_WATCHED },
                                label = {
                                    Text("${AppLocalization.get("lectures_recently_watched", language)} (${recentLectures.size})")
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("tab_lectures_recent")
                            )
                        }
                    }
                }

                // Language Filter Chips (All / EN / UR / AR)
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val langOptions = listOf(
                            null to "lectures_all_languages",
                            "en" to "lectures_lang_en",
                            "ur" to "lectures_lang_ur",
                            "ar" to "lectures_lang_ar"
                        )
                        items(langOptions) { (code, key) ->
                            FilterChip(
                                selected = selectedLanguageFilter == code,
                                onClick = { selectedLanguageFilter = code },
                                label = { Text(AppLocalization.get(key, language)) },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("lang_chip_${code ?: "all"}")
                            )
                        }
                    }
                }

                // Topic Categories & Verified Scholar Channels (shown in Topics tab)
                if (selectedTab == LectureLibraryTab.ALL_FEEDS) {
                    // 12 Predefined Islamic Lecture Topics (Select a topic to fetch only its mapped channels)
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = AppLocalization.get("lectures_categories_title", language),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(LectureCategory.entries, key = { it.id }) { cat ->
                                    val isCatSelected = selectedCategory == cat
                                    FilterChip(
                                        selected = isCatSelected,
                                        onClick = {
                                            selectedChannelKey = null
                                            selectedCategory = if (isCatSelected) null else cat
                                        },
                                        label = { Text(AppLocalization.get(cat.labelKey, language), fontSize = 12.sp) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = noorSelectableChipColors(),
                                        modifier = Modifier.testTag("category_chip_${cat.id}")
                                    )
                                }
                            }
                        }
                    }

                    // Verified Scholar Channels Row (allows on-demand single-channel fetch)
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = AppLocalization.get("lectures_verified_scholars", language),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(YouTubeRepository.VERIFIED_CHANNELS, key = { it.channelId }) { channel ->
                                    val isSel = selectedChannelKey == channel.key
                                    Card(
                                        onClick = {
                                            if (isSel) {
                                                selectedChannelKey = null
                                            } else {
                                                selectedChannelKey = channel.key
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                        ),
                                        border = if (isSel) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                                        modifier = Modifier
                                            .semantics { selected = isSel }
                                            .testTag("scholar_chip_${channel.key}")
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                                .width(185.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = channel.localizedName(language),
                                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(
                                                    imageVector = Icons.Default.Verified,
                                                    contentDescription = null,
                                                    tint = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = channel.localizedSpecialty(language),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isSel) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Count + Last Updated + Refresh Bar (only shown once a topic/channel is selected or inside Favorites/Recent)
                if (selectedTab != LectureLibraryTab.ALL_FEEDS || selectedCategory != null || selectedChannelKey != null) {
                    item {
                        val lastUpdatedStr = YouTubeRepository.formatLastUpdatedDisplay(feedResult.lastUpdatedMs)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = String.format(
                                        AppLocalization.get("lectures_results_count", language),
                                        visibleVideos.size
                                    ),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (selectedTab == LectureLibraryTab.ALL_FEEDS && lastUpdatedStr.isNotBlank()) {
                                    Text(
                                        text = String.format(
                                            AppLocalization.get("lectures_last_updated", language),
                                            lastUpdatedStr
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            if (selectedTab == LectureLibraryTab.ALL_FEEDS) {
                                TextButton(
                                    onClick = { refreshTrigger++ },
                                    modifier = Modifier.sizeIn(minHeight = 48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(AppLocalization.get("lectures_refresh", language), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                // Initial IDLE state before selecting a topic (Zero unnecessary network calls on open!)
                if (selectedTab == LectureLibraryTab.ALL_FEEDS &&
                    selectedCategory == null &&
                    selectedChannelKey == null
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("lectures_topic_prompt_card")
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TouchApp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(34.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = AppLocalization.get("lectures_select_topic_prompt", language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else if (isLoading && visibleVideos.isEmpty()) {
                    // Loading State
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)
                                .testTag("lectures_loading_state")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = AppLocalization.get("lectures_loading_feeds", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else if (selectedTab == LectureLibraryTab.ALL_FEEDS &&
                    feedResult.status == FeedLoadStatus.INVALID_MAPPING
                ) {
                    // Invalid or Missing Topic Mapping Error
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("lectures_invalid_mapping_error")
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = AppLocalization.get("lectures_invalid_mapping", language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else if (selectedTab == LectureLibraryTab.ALL_FEEDS &&
                    feedResult.status == FeedLoadStatus.ERROR_NO_CACHE &&
                    visibleVideos.isEmpty()
                ) {
                    // Offline & No Cache / Feed Error State
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("lectures_offline_error_state")
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
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
                                    Text(AppLocalization.get("lectures_retry", language))
                                }
                            }
                        }
                    }
                } else if (visibleVideos.isEmpty()) {
                    // Honest Empty State (Never substitute fake unrelated videos!)
                    item {
                        val isTopicEmptyWithoutSearch = selectedTab == LectureLibraryTab.ALL_FEEDS && searchQuery.isBlank()
                        val emptyText = when {
                            selectedTab == LectureLibraryTab.FAVORITES ->
                                AppLocalization.get("lectures_empty_favorites", language)
                            selectedTab == LectureLibraryTab.RECENTLY_WATCHED ->
                                AppLocalization.get("lectures_empty_recent", language)
                            isTopicEmptyWithoutSearch ->
                                AppLocalization.get("lectures_no_topic_videos", language)
                            else ->
                                AppLocalization.get("lectures_empty_search_desc", language)
                        }
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .testTag("lectures_empty_state")
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                if (selectedTab == LectureLibraryTab.ALL_FEEDS && searchQuery.isNotBlank()) {
                                    Text(
                                        text = AppLocalization.get("lectures_empty_search_title", language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                                Text(
                                    text = emptyText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                                if (searchQuery.isNotBlank() || selectedLanguageFilter != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    OutlinedButton(
                                        onClick = {
                                            searchQuery = ""
                                            selectedLanguageFilter = null
                                        },
                                        modifier = Modifier.sizeIn(minHeight = 48.dp)
                                    ) {
                                        Text(AppLocalization.get("lectures_clear", language))
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Adaptive 2 or 3-Column Grid of Verified Channel Videos
                    val videoRows = visibleVideos.chunked(columns)
                    items(videoRows, key = { row -> row.joinToString("_") { it.id } }) { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            for (video in rowItems) {
                                Box(modifier = Modifier.weight(1f)) {
                                    VerifiedLectureGridCard(
                                        video = video,
                                        language = language,
                                        isPlayingNow = activeVideo?.id == video.id,
                                        isFavorite = favoriteIds.contains(video.id),
                                        onClick = { selectAndPlay(video) },
                                        onToggleFavorite = {
                                            YouTubeRepository.toggleFavoriteLecture(context, video)
                                            favoriteLectures = YouTubeRepository.getFavoriteLectures(context)
                                        }
                                    )
                                }
                            }
                            repeat(columns - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VerifiedLectureGridCard(
    video: YouTubeVideoItem,
    language: AppLanguage,
    isPlayingNow: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlayingNow) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlayingNow) 3.dp else 1.5.dp),
        border = if (isPlayingNow) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("grid_lecture_card_${video.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = video.thumbnail,
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f))
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = AppLocalization.get("lectures_watch_in_app", language),
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Publication Date badge (real Atom <published> metadata)
                if (video.publishedDisplay.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Black.copy(alpha = 0.8f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = video.publishedDisplay,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        lineHeight = 16.sp
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
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = video.channel,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = AppLocalization.get("lectures_watch_in_app", language),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                            .testTag("card_fav_${video.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = AppLocalization.get(
                                if (isFavorite) "lectures_bookmark_remove" else "lectures_bookmark_add",
                                language
                            ),
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
