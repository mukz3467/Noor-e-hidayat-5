package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
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
import com.example.data.model.BookmarkType
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

@Composable
fun BookmarksScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    onNavigateToSurah: (Int) -> Unit,
    onNavigateToHadith: (String?) -> Unit = {},
    onNavigateToDuas: (String?) -> Unit = {}
) {
    val context = LocalContext.current
    val prefsRepo = remember(context) { UserPreferencesRepository(context) }
    val bookmarks by prefsRepo.bookmarks.collectAsStateWithLifecycle()
    var selectedFilter by rememberSaveable { mutableStateOf<BookmarkType?>(null) }

    LaunchedEffect(Unit) {
        prefsRepo.reloadBookmarksFromPrefs()
    }

    val filteredBookmarks = remember(bookmarks, selectedFilter) {
        if (selectedFilter == null) bookmarks else bookmarks.filter { it.type == selectedFilter }
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("bookmarks", language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == null,
                        onClick = { selectedFilter = null },
                        label = { Text("${AppLocalization.get("bookmarks_all", language)} (${bookmarks.size})") },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.testTag("bookmarks_filter_all")
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == BookmarkType.QURAN,
                        onClick = { selectedFilter = BookmarkType.QURAN },
                        label = { Text("${AppLocalization.get("quran", language)} (${bookmarks.count { it.type == BookmarkType.QURAN }})") },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.testTag("bookmarks_filter_quran")
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == BookmarkType.HADITH,
                        onClick = { selectedFilter = BookmarkType.HADITH },
                        label = { Text("${AppLocalization.get("hadith", language)} (${bookmarks.count { it.type == BookmarkType.HADITH }})") },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.testTag("bookmarks_filter_hadith")
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == BookmarkType.DUA,
                        onClick = { selectedFilter = BookmarkType.DUA },
                        label = { Text("${AppLocalization.get("duas", language)} (${bookmarks.count { it.type == BookmarkType.DUA }})") },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.testTag("bookmarks_filter_dua")
                    )
                }
            }

            if (filteredBookmarks.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Text(
                        text = AppLocalization.get("bookmarks_empty", language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredBookmarks, key = { it.id }) { bm ->
                        Card(
                            onClick = {
                                when (bm.type) {
                                    BookmarkType.QURAN -> {
                                        if (bm.surahNumber != null) onNavigateToSurah(bm.surahNumber)
                                    }
                                    BookmarkType.HADITH -> onNavigateToHadith(bm.hadithId)
                                    BookmarkType.DUA -> onNavigateToDuas(bm.duaId)
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("bookmark_${bm.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = bm.type.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = bm.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = bm.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        prefsRepo.removeBookmarkById(bm.id)
                                        Toast.makeText(
                                            context,
                                            AppLocalization.get("bookmark_removed", language),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = MaterialTheme.colorScheme.error
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
