package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppLanguage
import com.example.data.model.BookmarkItem
import com.example.data.model.BookmarkType
import com.example.data.model.Dua
import com.example.data.repository.DuaAudioPlayer
import com.example.data.repository.DuaData
import com.example.data.repository.UserPreferencesRepository
import com.example.localization.AppLocalization
import com.example.localization.LocalizedContentResolver
import com.example.ui.components.NoorTopBar
import com.example.ui.theme.QuranNaskhFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuasScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    initialDuaId: String? = null
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    val prefsRepo = remember(context) { UserPreferencesRepository(context) }

    val favoriteIds by prefsRepo.favoriteDuaIds.collectAsStateWithLifecycle()
    val recentViewedIds by prefsRepo.recentViewedDuaIds.collectAsStateWithLifecycle()
    val arabicFontSizePref by prefsRepo.arabicFontSize.collectAsStateWithLifecycle()

    var selectedCategory by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDuaForDetails by remember { mutableStateOf<Dua?>(null) }
    var showFontSizeDialog by remember { mutableStateOf(false) }

    val currentlyPlayingId by DuaAudioPlayer.currentDuaId.collectAsStateWithLifecycle()
    val isPlayingAudio by DuaAudioPlayer.isPlaying.collectAsStateWithLifecycle()
    val isBufferingAudio by DuaAudioPlayer.isBuffering.collectAsStateWithLifecycle()
    val currentLoop by DuaAudioPlayer.currentLoop.collectAsStateWithLifecycle()
    val targetRepeat by DuaAudioPlayer.repeatCount.collectAsStateWithLifecycle()
    val audioError by DuaAudioPlayer.errorMessage.collectAsStateWithLifecycle()
    val isDownloadingPack by DuaAudioPlayer.isDownloadingPack.collectAsStateWithLifecycle()
    val offlinePackProgress by DuaAudioPlayer.offlinePackProgress.collectAsStateWithLifecycle()
    val cachedTrackCount by DuaAudioPlayer.cachedTrackCount.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        DuaAudioPlayer.init(context)
        prefsRepo.reloadBookmarksFromPrefs()
    }

    DisposableEffect(Unit) {
        onDispose {
            DuaAudioPlayer.pause()
        }
    }

    LaunchedEffect(initialDuaId) {
        if (!initialDuaId.isNullOrBlank()) {
            val found = DuaData.duas.firstOrNull { it.id == initialDuaId }
            if (found != null) {
                prefsRepo.recordViewedDua(found.id)
                selectedDuaForDetails = found
            }
        }
    }

    LaunchedEffect(audioError) {
        audioError?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            DuaAudioPlayer.clearError()
        }
    }

    val dailyFeaturedDua = remember { DuaData.getDailyFeaturedDua() }

    val filteredDuas = remember(selectedCategory, searchQuery, favoriteIds, recentViewedIds) {
        DuaData.duas.filter { dua ->
            val matchCategory = when (selectedCategory) {
                "all" -> true
                "favorites" -> favoriteIds.contains(dua.id)
                "recent" -> recentViewedIds.contains(dua.id)
                else -> dua.categoryId == selectedCategory
            }

            val q = searchQuery.trim()
            val matchQuery = if (q.isBlank()) true else {
                dua.titleEn.contains(q, ignoreCase = true) ||
                    dua.titleUr.contains(q) ||
                    dua.titleAr.contains(q) ||
                    dua.titleHi.contains(q) ||
                    dua.arabic.contains(q) ||
                    dua.transliteration.contains(q, ignoreCase = true) ||
                    dua.translationEn.contains(q, ignoreCase = true) ||
                    dua.translationUr.contains(q) ||
                    dua.translationHi.contains(q, ignoreCase = true) ||
                    dua.narrator.contains(q, ignoreCase = true) ||
                    dua.occasionEn.contains(q, ignoreCase = true) ||
                    dua.occasionUr.contains(q) ||
                    dua.occasionHi.contains(q, ignoreCase = true) ||
                    dua.bookName.contains(q, ignoreCase = true) ||
                    dua.hadithOrAyahNumber.contains(q, ignoreCase = true) ||
                    dua.reference.contains(q, ignoreCase = true) ||
                    dua.grade.contains(q, ignoreCase = true)
            }

            matchCategory && matchQuery
        }
    }

    fun toggleFavoriteAndSyncBookmark(dua: Dua) {
        val added = prefsRepo.toggleFavoriteDua(dua.id)
        val currentlyBookmarked = prefsRepo.isDuaBookmarked(dua.id)
        if ((added && !currentlyBookmarked) || (!added && currentlyBookmarked)) {
            prefsRepo.toggleBookmark(
                BookmarkItem(
                    id = "bm_dua_${dua.id}",
                    type = BookmarkType.DUA,
                    title = LocalizedContentResolver.resolveDuaTitle(dua, language),
                    subtitle = dua.arabic.take(90),
                    duaId = dua.id
                )
            )
        }
        val msg = if (added) AppLocalization.get("dua_added_fav", language) else AppLocalization.get("dua_removed_fav", language)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("duas", language),
                language = language,
                onBackClick = onBack,
                actions = {
                    IconButton(
                        onClick = { showFontSizeDialog = true },
                        modifier = Modifier.testTag("duas_font_size_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = AppLocalization.get("arabic_font_size", language),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = AppLocalization.get("search_duas_hint", language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = AppLocalization.get("clear_search", language))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .testTag("duas_search_input")
            )

            // 20 Category Filter Chips + All / Favorites / Recent
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == "all",
                        onClick = { selectedCategory = "all" },
                        leadingIcon = {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        label = {
                            Text("${AppLocalization.get("all_duas", language)} (${DuaData.duas.size})")
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("dua_chip_all")
                    )
                }

                item {
                    FilterChip(
                        selected = selectedCategory == "favorites",
                        onClick = { selectedCategory = "favorites" },
                        leadingIcon = {
                            Icon(
                                imageVector = if (selectedCategory == "favorites") Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = {
                            Text("${AppLocalization.get("favorite_duas", language)} (${favoriteIds.size})")
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("dua_chip_favorites")
                    )
                }

                item {
                    FilterChip(
                        selected = selectedCategory == "recent",
                        onClick = { selectedCategory = "recent" },
                        leadingIcon = {
                            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        label = {
                            Text(AppLocalization.get("recent_duas", language))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("dua_chip_recent")
                    )
                }

                // 20 Authentic Masnoon Dua Categories
                items(DuaData.categories, key = { it.id }) { cat ->
                    val catTitle = when (language) {
                        AppLanguage.URDU -> cat.titleUr
                        AppLanguage.ARABIC -> cat.titleAr
                        AppLanguage.HINDI -> cat.titleHi
                        AppLanguage.ENGLISH -> cat.titleEn
                    }
                    FilterChip(
                        selected = selectedCategory == cat.id,
                        onClick = { selectedCategory = cat.id },
                        label = { Text(catTitle) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("dua_chip_${cat.id}")
                    )
                }
            }

            // Duas List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (selectedCategory == "all" && searchQuery.isBlank()) {
                    item(key = "daily_featured_dua_banner") {
                        DailyFeaturedDuaCard(
                            dua = dailyFeaturedDua,
                            language = language,
                            arabicFontSize = arabicFontSizePref,
                            onOpenDetails = {
                                prefsRepo.recordViewedDua(dailyFeaturedDua.id)
                                selectedDuaForDetails = dailyFeaturedDua
                            },
                            isPlaying = currentlyPlayingId == dailyFeaturedDua.id && isPlayingAudio,
                            isBuffering = currentlyPlayingId == dailyFeaturedDua.id && isBufferingAudio,
                            onToggleAudio = {
                                prefsRepo.recordPlayedDua(dailyFeaturedDua.id)
                                DuaAudioPlayer.playDua(
                                    context = context,
                                    dua = dailyFeaturedDua,
                                    repeatTarget = 1
                                )
                            }
                        )
                    }

                    // Offline Qari Audio Pack Banner for Quranic Adhkar
                    item(key = "dua_qari_offline_pack_banner") {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text(
                                    text = AppLocalization.get("dua_genuine_audio_badge", language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = String.format(AppLocalization.get("dua_audio_pack_summary", language), cachedTrackCount),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                if (isDownloadingPack) {
                                    LinearProgressIndicator(
                                        progress = { offlinePackProgress },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                DuaAudioPlayer.downloadAllGenuineDuaAudios(context, DuaData.duas)
                                                    .onSuccess { count ->
                                                        Toast.makeText(
                                                            context,
                                                            String.format(AppLocalization.get("dua_cached_tracks_toast", language), count),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    .onFailure { err ->
                                                        Toast.makeText(
                                                            context,
                                                            "${AppLocalization.get("dua_audio_cache_failed", language)}: ${err.message.orEmpty()}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 48.dp)
                                    ) {
                                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = AppLocalization.get("dua_download_audio_pack", language),
                                            style = MaterialTheme.typography.labelMedium,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (filteredDuas.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (selectedCategory == "favorites") {
                                        AppLocalization.get("dua_no_favorites", language)
                                    } else {
                                        AppLocalization.get("dua_no_results", language)
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(filteredDuas, key = { it.id }) { dua ->
                        val isFav = favoriteIds.contains(dua.id)
                        val isThisPlaying = currentlyPlayingId == dua.id && isPlayingAudio
                        val isThisBuffering = currentlyPlayingId == dua.id && isBufferingAudio
                        val isOfflineReady = remember(dua.id, cachedTrackCount) {
                            DuaAudioPlayer.isDuaOfflineReady(context, dua)
                        }

                        DuaCard(
                            dua = dua,
                            language = language,
                            arabicFontSize = arabicFontSizePref,
                            isFavorite = isFav,
                            isPlaying = isThisPlaying,
                            isBuffering = isThisBuffering,
                            isOfflineAudioReady = isOfflineReady,
                            currentLoop = if (currentlyPlayingId == dua.id) currentLoop else 1,
                            targetRepeat = if (currentlyPlayingId == dua.id) targetRepeat else 1,
                            onToggleFavorite = { toggleFavoriteAndSyncBookmark(dua) },
                            onPlayAudio = { repeatCount ->
                                prefsRepo.recordPlayedDua(dua.id)
                                DuaAudioPlayer.playDua(
                                    context = context,
                                    dua = dua,
                                    repeatTarget = repeatCount
                                )
                            },
                            onPauseAudio = { DuaAudioPlayer.pause() },
                            onOpenDetails = {
                                prefsRepo.recordViewedDua(dua.id)
                                selectedDuaForDetails = dua
                            },
                            onOpenUrl = { url -> runCatching { uriHandler.openUri(url) } }
                        )
                    }
                }
            }
        }
    }

    selectedDuaForDetails?.let { detailedDua ->
        DuaDetailBottomSheet(
            dua = detailedDua,
            language = language,
            arabicFontSize = arabicFontSizePref,
            isFavorite = favoriteIds.contains(detailedDua.id),
            isPlaying = currentlyPlayingId == detailedDua.id && isPlayingAudio,
            isBuffering = currentlyPlayingId == detailedDua.id && isBufferingAudio,
            currentLoop = if (currentlyPlayingId == detailedDua.id) currentLoop else 1,
            targetRepeat = if (currentlyPlayingId == detailedDua.id) targetRepeat else 1,
            onDismiss = { selectedDuaForDetails = null },
            onToggleFavorite = { toggleFavoriteAndSyncBookmark(detailedDua) },
            onPlayAudio = { repeatTarget ->
                prefsRepo.recordPlayedDua(detailedDua.id)
                DuaAudioPlayer.playDua(
                    context = context,
                    dua = detailedDua,
                    repeatTarget = repeatTarget
                )
            },
            onPauseAudio = { DuaAudioPlayer.pause() },
            onOpenUrl = { url -> runCatching { uriHandler.openUri(url) } }
        )
    }

    if (showFontSizeDialog) {
        AlertDialog(
            onDismissRequest = { showFontSizeDialog = false },
            title = {
                Text(
                    text = AppLocalization.get("arabic_font_size", language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = QuranNaskhFontFamily,
                            fontSize = arabicFontSizePref.sp,
                            lineHeight = (arabicFontSizePref * 1.9f).sp,
                            fontWeight = FontWeight.Normal,
                            textDirection = TextDirection.Rtl
                        ),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )

                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(20f, 24f, 28f, 32f).forEach { size ->
                            val labelKey = when (size) {
                                20f -> "size_small"
                                24f -> "size_medium"
                                28f -> "size_large"
                                else -> "size_extra"
                            }
                            FilterChip(
                                selected = arabicFontSizePref == size,
                                onClick = { prefsRepo.setArabicFontSize(size) },
                                label = { Text(AppLocalization.get(labelKey, language), maxLines = 1) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFontSizeDialog = false }) {
                    Text(AppLocalization.get("ok", language))
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyFeaturedDuaCard(
    dua: Dua,
    language: AppLanguage,
    arabicFontSize: Float,
    onOpenDetails: () -> Unit,
    isPlaying: Boolean,
    isBuffering: Boolean,
    onToggleAudio: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    Color(0xFFE6A100)
                )
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("daily_featured_dua_card")
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFE6A100),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = AppLocalization.get("daily_featured_dua", language),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = dua.grade,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = dua.arabic,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = QuranNaskhFontFamily,
                    fontSize = arabicFontSize.sp,
                    lineHeight = (arabicFontSize * 1.9f).sp,
                    fontWeight = FontWeight.Normal,
                    textDirection = TextDirection.Rtl
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(3.dp))

            if (language == AppLanguage.ARABIC) {
                Text(
                    text = LocalizedContentResolver.resolveDuaTitle(dua, AppLanguage.ARABIC),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        textDirection = TextDirection.Rtl
                    ),
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                val resolvedFeatured = LocalizedContentResolver.resolveDuaTranslation(dua, language)
                Text(
                    text = resolvedFeatured.text ?: resolvedFeatured.unavailableNotice
                        ?: AppLocalization.get("dua_translation_unavailable", language),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDirection = if (language == AppLanguage.URDU) TextDirection.Rtl else TextDirection.Ltr
                    ),
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = dua.reference,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dua.hasGenuineAudio) {
                        FilledTonalIconButton(
                            onClick = onToggleAudio,
                            modifier = Modifier.size(48.dp)
                        ) {
                            if (isBuffering) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = AppLocalization.get(if (isPlaying) "pause" else "play", language),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onOpenDetails,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 48.dp)
                    ) {
                        Text(
                            text = AppLocalization.get("dua_recite_details", language),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DuaCard(
    dua: Dua,
    language: AppLanguage,
    arabicFontSize: Float,
    isFavorite: Boolean,
    isPlaying: Boolean,
    isBuffering: Boolean,
    isOfflineAudioReady: Boolean,
    currentLoop: Int,
    targetRepeat: Int,
    onToggleFavorite: () -> Unit,
    onPlayAudio: (Int) -> Unit,
    onPauseAudio: () -> Unit,
    onOpenDetails: () -> Unit,
    onOpenUrl: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedRepeat by remember { mutableIntStateOf(dua.repeatCount.coerceAtLeast(1)) }

    val title = remember(dua, language) { LocalizedContentResolver.resolveDuaTitle(dua, language) }
    val resolvedTranslation = remember(dua, language) { LocalizedContentResolver.resolveDuaTranslation(dua, language) }
    val occasionText = remember(dua, language) { LocalizedContentResolver.resolveDuaOccasion(dua, language).orEmpty() }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dua_card_${dua.id}")
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = dua.grade,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    if (dua.recommendedTime.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE6A100).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = dua.recommendedTime,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFB57E00),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = AppLocalization.get("favorite_duas", language),
                        tint = if (isFavorite) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textDirection = if (language == AppLanguage.URDU || language == AppLanguage.ARABIC) TextDirection.Rtl else TextDirection.Ltr
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            if (occasionText.isNotBlank()) {
                Text(
                    text = occasionText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            val appSettings = com.example.ui.theme.LocalAppSettings.current
            val resolvedTransSize = appSettings.translationFontSize.coerceIn(12f, 22f)

            Text(
                text = dua.arabic,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = QuranNaskhFontFamily,
                    fontSize = arabicFontSize.sp,
                    lineHeight = (arabicFontSize * 1.9f).sp,
                    fontWeight = FontWeight.Normal,
                    textDirection = TextDirection.Rtl
                ),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = dua.transliteration,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontStyle = FontStyle.Italic,
                    lineHeight = 16.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
            )

            if (language != AppLanguage.ARABIC && appSettings.showInlineTranslation) {
                Spacer(modifier = Modifier.height(4.dp))

                if (resolvedTranslation.isAvailable && !resolvedTranslation.text.isNullOrBlank()) {
                    Text(
                        text = resolvedTranslation.text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = resolvedTransSize.sp,
                            lineHeight = (resolvedTransSize * 1.38f).sp,
                            textDirection = if (language == AppLanguage.URDU) TextDirection.Rtl else TextDirection.Ltr
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dua_single_translation_${dua.id}")
                    )
                } else {
                    Text(
                        text = resolvedTranslation.unavailableNotice
                            ?: AppLocalization.get("dua_translation_unavailable", language),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (dua.narrator.isNotBlank()) {
                Text(
                    text = "${AppLocalization.get("hadith_narrator_label", language)}: ${dua.narrator}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = dua.reference,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .let { mod ->
                        if (dua.sourceUrl.isNotBlank()) {
                            mod.clickable { onOpenUrl(dua.sourceUrl) }
                        } else mod
                    }
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Genuine Audio Bar OR Honest "Audio not available" Bar + Copy/Share/Details
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    if (dua.hasGenuineAudio) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            IconButton(
                                onClick = {
                                    if (isPlaying) onPauseAudio() else onPlayAudio(selectedRepeat)
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("dua_play_btn_${dua.id}")
                            ) {
                                if (isBuffering) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                        contentDescription = AppLocalization.get(if (isPlaying) "pause" else "play", language),
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }

                            Column {
                                if (isBuffering) {
                                    Text(
                                        text = AppLocalization.get("dua_buffering", language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else if (isPlaying) {
                                    Text(
                                        text = String.format(
                                            AppLocalization.get("dua_reciting_loop", language),
                                            currentLoop,
                                            targetRepeat
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        listOf(1, 3, 7).forEach { times ->
                                            val isCur = selectedRepeat == times
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isCur) MaterialTheme.colorScheme.primary else Color.Transparent)
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (isCur) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                                        shape = RoundedCornerShape(6.dp)
                                                    )
                                                    .clickable { selectedRepeat = times }
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "${times}x",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = if (isCur) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                    if (isOfflineAudioReady && dua.audioReciter.isNotBlank()) {
                                        Text(
                                            text = "${dua.audioReciter} • ${AppLocalization.get("dua_audio_offline_ready", language)}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                                .testTag("dua_audio_unavailable_${dua.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeOff,
                                contentDescription = AppLocalization.get("dua_audio_not_available", language),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppLocalization.get("dua_audio_not_available", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }
                    }

                    val copyShareTranslation = if (language != AppLanguage.ARABIC && resolvedTranslation.isAvailable) {
                        "\n\n${resolvedTranslation.text}"
                    } else ""

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText(
                                    title,
                                    "${dua.arabic}\n\n${dua.transliteration}$copyShareTranslation\n\n[${dua.reference} - ${AppLocalization.get("app_name", language)}]"
                                )
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, AppLocalization.get("dua_copied", language), Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = AppLocalization.get("copy_dua", language), modifier = Modifier.size(18.dp))
                        }

                        IconButton(
                            onClick = {
                                val shareText = buildString {
                                    append("$title\n\n")
                                    append("${dua.arabic}\n\n")
                                    append("${AppLocalization.get("dua_transliteration_label", language)}\n${dua.transliteration}\n\n")
                                    if (language != AppLanguage.ARABIC && resolvedTranslation.isAvailable) {
                                        append("${AppLocalization.get("dua_active_translation", language)}:\n${resolvedTranslation.text}\n\n")
                                    }
                                    append("${AppLocalization.get("dua_reference_label", language)}: ${dua.reference} (${dua.grade})\n")
                                    append(AppLocalization.get("app_name", language))
                                }
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, title)
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                runCatching {
                                    context.startActivity(Intent.createChooser(intent, AppLocalization.get("share_dua", language)))
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = AppLocalization.get("share_dua", language), modifier = Modifier.size(18.dp))
                        }

                        IconButton(
                            onClick = onOpenDetails,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = AppLocalization.get("dua_recite_details", language),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DuaDetailBottomSheet(
    dua: Dua,
    language: AppLanguage,
    arabicFontSize: Float,
    isFavorite: Boolean,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentLoop: Int,
    targetRepeat: Int,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit,
    onPlayAudio: (Int) -> Unit,
    onPauseAudio: () -> Unit,
    onOpenUrl: (String) -> Unit
) {
    val context = LocalContext.current
    var sheetRepeatTarget by remember { mutableIntStateOf(dua.repeatCount.coerceAtLeast(1)) }
    val isRtl = language == AppLanguage.URDU || language == AppLanguage.ARABIC

    val localizedTitle = remember(dua, language) { LocalizedContentResolver.resolveDuaTitle(dua, language) }
    val localizedTranslation = remember(dua, language) { LocalizedContentResolver.resolveDuaTranslation(dua, language) }
    val localizedMethod = remember(dua, language) { LocalizedContentResolver.resolveDuaMethod(dua, language) }
    val localizedOccasion = remember(dua, language) { LocalizedContentResolver.resolveDuaOccasion(dua, language).orEmpty() }
    val localizedVirtues = remember(dua, language) { LocalizedContentResolver.resolveDuaVirtues(dua, language) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = dua.grade,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE6A100).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = dua.hadithOrAyahNumber.ifBlank { dua.reference },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFB57E00),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = AppLocalization.get("favorite_duas", language),
                        tint = if (isFavorite) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = localizedTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textDirection = if (isRtl) TextDirection.Rtl else TextDirection.Ltr
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            if (localizedOccasion.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = localizedOccasion,
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDirection = if (isRtl) TextDirection.Rtl else TextDirection.Ltr
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                modifier = Modifier.fillMaxWidth()
            ) {
                val sheetArabicSize = (arabicFontSize + 2f)
                Text(
                    text = dua.arabic,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = QuranNaskhFontFamily,
                        fontSize = sheetArabicSize.sp,
                        lineHeight = (sheetArabicSize * 1.9f).sp,
                        fontWeight = FontWeight.Normal,
                        textDirection = TextDirection.Rtl
                    ),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = AppLocalization.get("dua_transliteration_label", language),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = dua.transliteration,
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (language != AppLanguage.ARABIC) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Text(
                            text = "${AppLocalization.get("dua_active_translation", language)}:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = if (language == AppLanguage.URDU) TextAlign.End else TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = localizedTranslation.text
                                ?: localizedTranslation.unavailableNotice
                                ?: AppLocalization.get("dua_translation_unavailable", language),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDirection = if (language == AppLanguage.URDU) TextDirection.Rtl else TextDirection.Ltr,
                                lineHeight = if (language == AppLanguage.URDU) 22.sp else 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            if (localizedMethod.isAvailable && !localizedMethod.text.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Text(
                            text = AppLocalization.get("dua_method_label", language),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = localizedMethod.text,
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDirection = if (isRtl) TextDirection.Rtl else TextDirection.Ltr
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            if (localizedVirtues.isAvailable && !localizedVirtues.text.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE6A100).copy(alpha = 0.1f),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(Color(0xFFE6A100), Color(0xFFFFA000)))
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFFC88A00), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppLocalization.get("virtues_benefits", language),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8D6200)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = localizedVirtues.text,
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDirection = if (isRtl) TextDirection.Rtl else TextDirection.Ltr
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Attribution, Narrator & Direct Verified URL
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    if (dua.narrator.isNotBlank()) {
                        Text(
                            text = "${AppLocalization.get("hadith_narrator_label", language)}: ${dua.narrator}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Text(
                        text = "${dua.bookName} • ${dua.reference} (${dua.grade})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (dua.sourceUrl.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { onOpenUrl(dua.sourceUrl) },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 32.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = AppLocalization.get("hadith_open_source_link", language),
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Genuine Audio Player Controls OR Honest "Audio not available" Box
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    if (dua.hasGenuineAudio) {
                        Text(
                            text = "${AppLocalization.get("dua_genuine_audio_badge", language)} • ${dua.audioReciter}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (dua.audioSourceName.isNotBlank()) {
                            Text(
                                text = "${dua.audioSourceName} (${dua.audioLicense})",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    if (isPlaying) onPauseAudio() else onPlayAudio(sheetRepeatTarget)
                                },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 34.dp)
                            ) {
                                if (isBuffering) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = when {
                                        isBuffering -> AppLocalization.get("dua_buffering", language)
                                        isPlaying -> AppLocalization.get("pause", language)
                                        else -> AppLocalization.get("play", language)
                                    },
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(1, 3, 7, 10).forEach { r ->
                                    val isCur = sheetRepeatTarget == r
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .height(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isCur) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                                            .border(
                                                width = 1.dp,
                                                color = if (isCur) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable { sheetRepeatTarget = r }
                                            .padding(horizontal = 10.dp)
                                    ) {
                                        Text(
                                            text = "${r}x",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (isCur) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        if (isPlaying) {
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { currentLoop.toFloat() / targetRepeat.toFloat().coerceAtLeast(1f) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = String.format(
                                    AppLocalization.get("dua_reciting_loop", language),
                                    currentLoop,
                                    targetRepeat
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VolumeOff,
                                contentDescription = AppLocalization.get("dua_audio_not_available", language),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppLocalization.get("dua_audio_not_available", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val sheetShareTrans = if (language != AppLanguage.ARABIC && localizedTranslation.isAvailable) {
                "\n\n${localizedTranslation.text}"
            } else ""

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(
                            localizedTitle,
                            "$localizedTitle\n\n${dua.arabic}\n\n${dua.transliteration}$sheetShareTrans\n\n[${dua.reference} - Noor-e-Hidayat]"
                        )
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, AppLocalization.get("dua_copied", language), Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minWidth = 1.dp, minHeight = 36.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = AppLocalization.get("copy_dua", language),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, localizedTitle)
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "$localizedTitle\n\n${dua.arabic}$sheetShareTrans\n\n[${dua.reference} (${dua.grade}) - Noor-e-Hidayat]"
                            )
                        }
                        runCatching {
                            context.startActivity(Intent.createChooser(intent, AppLocalization.get("share_dua", language)))
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minWidth = 1.dp, minHeight = 48.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = AppLocalization.get("share_dua", language),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
