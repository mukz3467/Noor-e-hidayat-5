package com.example.ui.screens.seerah

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
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
import com.example.data.seerah.SeerahArticle
import com.example.data.seerah.SeerahEra
import com.example.data.seerah.SeerahRepository
import com.example.data.seerah.SourceCredibilityTier
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

@Composable
fun SeerahScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val progress by SeerahRepository.progress.collectAsStateWithLifecycle()

    var selectedArticleId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedEra by rememberSaveable { mutableStateOf<SeerahEra?>(null) }
    var bookmarkedOnly by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        SeerahRepository.init(context)
    }

    val openArticle = remember(selectedArticleId) {
        SeerahRepository.getArticleById(selectedArticleId)
    }

    if (openArticle != null) {
        BackHandler {
            selectedArticleId = null
        }
        SeerahArticleDetailScreen(
            article = openArticle,
            language = language,
            isRead = progress.readArticleIds.contains(openArticle.id),
            isBookmarked = progress.bookmarkedArticleIds.contains(openArticle.id),
            onToggleRead = { SeerahRepository.toggleReadStatus(context, openArticle.id) },
            onToggleBookmark = { SeerahRepository.toggleBookmark(context, openArticle.id) },
            onNavigateToArticle = { nextId ->
                SeerahRepository.markArticleOpened(context, nextId)
                selectedArticleId = nextId
            },
            onBack = { selectedArticleId = null }
        )
        return
    }

    val filteredArticles = remember(searchQuery, selectedEra, bookmarkedOnly, progress.bookmarkedArticleIds, language) {
        SeerahRepository.searchArticles(
            query = searchQuery,
            eraFilter = selectedEra,
            bookmarkedOnly = bookmarkedOnly,
            language = language
        )
    }

    val totalCount = SeerahRepository.allArticles.size
    val readCount = progress.readArticleIds.count { id -> SeerahRepository.allArticles.any { it.id == id } }
    val completionFraction = if (totalCount > 0) readCount.toFloat() / totalCount.toFloat() else 0f
    val lastReadArticle = remember(progress.lastReadArticleId) {
        SeerahRepository.getArticleById(progress.lastReadArticleId)
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("seerah", language),
                language = language,
                onBackClick = onBack
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
                .testTag("seerah_list_container")
        ) {
            // Source Credibility Hierarchy Banner
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.72f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("seerah_methodology_banner")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppLocalization.get("seerah_hierarchy_title", language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "1. ${SourceCredibilityTier.TIER_1_QURAN_HADITH.label(language)}\n" +
                                "2. ${SourceCredibilityTier.TIER_2_CLASSICAL_SEERAH.label(language)}\n" +
                                "3. ${SourceCredibilityTier.TIER_3_SCHOLARLY_BIOGRAPHY.label(language)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Progress & Resume Last Read
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("seerah_progress_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = AppLocalization.get("seerah_progress_label", language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "$readCount / $totalCount",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.testTag("seerah_progress_count")
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { completionFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .testTag("seerah_progress_bar")
                        )

                        if (lastReadArticle != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            FilledTonalButton(
                                onClick = {
                                    SeerahRepository.markArticleOpened(context, lastReadArticle.id)
                                    selectedArticleId = lastReadArticle.id
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 48.dp)
                                    .testTag("seerah_resume_card")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${AppLocalization.get("seerah_resume_last", language)}: ${lastReadArticle.textFor(lastReadArticle.title, language)}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Search Field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text(AppLocalization.get("seerah_search_hint", language)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("seerah_search_input")
                )
            }

            // Era Selector + Bookmarks Filter
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        FilterChip(
                            selected = selectedEra == null && !bookmarkedOnly,
                            onClick = {
                                selectedEra = null
                                bookmarkedOnly = false
                            },
                            label = { Text(AppLocalization.get("seerah_all_eras", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("seerah_era_all")
                        )
                        FilterChip(
                            selected = bookmarkedOnly,
                            onClick = { bookmarkedOnly = !bookmarkedOnly },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (bookmarkedOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            label = { Text(AppLocalization.get("seerah_bookmarks_filter", language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("seerah_filter_bookmarks")
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        SeerahEra.entries.forEach { era ->
                            FilterChip(
                                selected = selectedEra == era,
                                onClick = { selectedEra = if (selectedEra == era) null else era },
                                label = { Text("${era.orderNumber}. ${era.title(language)}") },
                                colors = noorSelectableChipColors(),
                                modifier = Modifier.testTag("seerah_era_${era.id}")
                            )
                        }
                    }
                }
            }

            if (filteredArticles.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = AppLocalization.get("seerah_empty_title", language),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = AppLocalization.get("seerah_empty_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredArticles, key = { it.id }) { article ->
                    SeerahArticleCard(
                        article = article,
                        language = language,
                        isRead = progress.readArticleIds.contains(article.id),
                        isBookmarked = progress.bookmarkedArticleIds.contains(article.id),
                        onClick = {
                            SeerahRepository.markArticleOpened(context, article.id)
                            selectedArticleId = article.id
                        },
                        onToggleBookmark = {
                            SeerahRepository.toggleBookmark(context, article.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SeerahArticleCard(
    article: SeerahArticle,
    language: AppLanguage,
    isRead: Boolean,
    isBookmarked: Boolean,
    onClick: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("seerah_article_${article.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                if (isRead) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Read",
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = article.orderIndex.toString(),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${article.era.orderNumber}. ${article.era.title(language)} • ${article.era.periodLabel(language)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = article.textFor(article.title, language),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = article.textFor(article.approxChronology, language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = onToggleBookmark,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("seerah_bookmark_${article.id}")
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = AppLocalization.get("seerah_bookmark_action", language),
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
