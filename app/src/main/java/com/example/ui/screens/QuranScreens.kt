package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.*
import com.example.data.repository.QuranAudioManager
import com.example.data.repository.QuranData
import com.example.data.repository.QuranOfflineSyncManager
import com.example.data.repository.QuranTafseerRepository
import com.example.data.repository.QuranTranslationTafseerRepository
import com.example.data.repository.ResolvedAyahTranslation
import com.example.data.repository.ResolvedTafseerDetail
import com.example.data.repository.SurahDownloadStatus
import com.example.data.repository.UserPreferencesRepository
import com.example.data.repository.VerifiedTafseerCatalog
import com.example.localization.AppLocalization
import com.example.localization.LocalizedContentResolver
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors
import com.example.ui.theme.QuranNaskhFontFamily
import kotlinx.coroutines.launch

@Composable
fun QuranListScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    onSelectSurah: (Int) -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) } // 0 = Surahs, 1 = Paras (Juz)
    var searchQuery by remember { mutableStateOf("") }

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) QuranData.surahs
        else {
            QuranData.surahs.filter {
                it.number.toString().contains(searchQuery.trim()) ||
                        it.nameEnglish.contains(searchQuery.trim(), ignoreCase = true) ||
                        it.nameArabic.contains(searchQuery.trim()) ||
                        it.nameUrdu.contains(searchQuery.trim()) ||
                        it.nameHindi.contains(searchQuery.trim())
            }
        }
    }

    val filteredParas = remember(searchQuery) {
        if (searchQuery.isBlank()) QuranData.paras
        else {
            val q = searchQuery.trim()
            QuranData.paras.filter {
                it.number.toString().contains(q) ||
                        it.nameEnglish.contains(q, ignoreCase = true) ||
                        it.nameArabic.contains(q) ||
                        it.nameUrdu.contains(q) ||
                        it.nameHindi.contains(q, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("quran", language),
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
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(AppLocalization.get("search_hint", language)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("quran_search_field")
            )

            // Tabs: Surahs (114) vs Paras (30)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = {
                        Text(
                            text = "${AppLocalization.get("surah", language)} (114)",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_surahs")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = {
                        Text(
                            text = "${AppLocalization.get("para", language)} (30)",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("tab_paras")
                )
            }

            // List Content
            if (selectedTab == 0) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredSurahs, key = { it.number }) { surah ->
                        SurahListItem(
                            surah = surah,
                            language = language,
                            onClick = { onSelectSurah(surah.number) }
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredParas, key = { it.number }) { para ->
                        ParaListItem(
                            para = para,
                            language = language,
                            onClick = { onSelectSurah(para.startSurahNumber) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SurahListItem(
    surah: Surah,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("surah_item_${surah.number}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Number Medallion
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = surah.number.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Surah Details
            Column(modifier = Modifier.weight(1f)) {
                val displayName = when (language) {
                    AppLanguage.URDU -> surah.nameUrdu
                    AppLanguage.HINDI -> surah.nameHindi
                    AppLanguage.ARABIC -> surah.nameArabic
                    AppLanguage.ENGLISH -> surah.nameEnglish
                }
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (surah.revelationType == RevelationType.MAKKI) AppLocalization.get("makki", language) else AppLocalization.get("madani", language),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = " • ${surah.ayahCount} ${AppLocalization.get("ayah_count", language)} • ${AppLocalization.get("para", language)} ${surah.startingPara}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Arabic Calligraphy Name (Uthmani Naskh Script)
            Text(
                text = surah.nameArabic,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = QuranNaskhFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ParaListItem(
    para: Para,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("para_item_${para.number}")
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
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = para.number.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                val paraTitle = when (language) {
                    AppLanguage.URDU -> "${AppLocalization.get("para", language)} ${para.number}: ${para.nameUrdu}"
                    AppLanguage.ARABIC -> "${AppLocalization.get("para", language)} ${para.number}: ${para.nameArabic}"
                    AppLanguage.HINDI -> "${AppLocalization.get("para", language)} ${para.number}: ${para.nameHindi}"
                    AppLanguage.ENGLISH -> "${AppLocalization.get("para", language)} ${para.number}: ${para.nameEnglish}"
                }
                Text(
                    text = paraTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                val startsAtText = when (language) {
                    AppLanguage.URDU -> "آغاز: سورت ${para.startSurahNumber}، آیت ${para.startAyahNumber}"
                    AppLanguage.ARABIC -> "يبدأ من سورة ${para.startSurahNumber}، آية ${para.startAyahNumber}"
                    AppLanguage.HINDI -> "आरंभ: सूरह ${para.startSurahNumber}, आयत ${para.startAyahNumber}"
                    AppLanguage.ENGLISH -> "Starts at Surah ${para.startSurahNumber}, Ayah ${para.startAyahNumber}"
                }
                Text(
                    text = startsAtText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = para.nameArabic,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    surahNumber: Int,
    language: AppLanguage,
    onBack: () -> Unit,
    onNavigateToSurah: (Int) -> Unit
) {
    val context = LocalContext.current
    val surah = remember(surahNumber) {
        QuranData.surahs.firstOrNull { it.number == surahNumber } ?: QuranData.surahs[0]
    }
    val ayahs = remember(surahNumber) { QuranData.getAyahsForSurah(context, surahNumber) }

    var selectedReciter by remember { mutableStateOf(QuranData.reciters[0]) }
    var showReciterMenu by remember { mutableStateOf(false) }
    var showFontSizeDialog by remember { mutableStateOf(false) }
    var arabicFontSize by remember { mutableStateOf(24f) }

    val downloadStates by QuranOfflineSyncManager.surahDownloadStates.collectAsState()
    val surahDownloadKey = remember(surah.number, selectedReciter.id) {
        QuranOfflineSyncManager.surahKey(surah.number, selectedReciter.id)
    }
    val liveDownloadState = downloadStates[surahDownloadKey]
    val isDownloadedOffline = remember(surah.number, selectedReciter.id, liveDownloadState) {
        liveDownloadState?.status == SurahDownloadStatus.COMPLETED ||
            liveDownloadState?.status == SurahDownloadStatus.BUNDLED_OFFLINE ||
            QuranOfflineSyncManager.isSurahAvailableOffline(context, surah.number, selectedReciter.id)
    }
    val isDownloadingAudio = (liveDownloadState?.status == SurahDownloadStatus.DOWNLOADING)

    LaunchedEffect(surahNumber) {
        QuranAudioManager.init(context)
        QuranTafseerRepository.init(context)
        // If audio from a different Surah was playing, stop it upon opening this Surah
        if (QuranAudioManager.currentPlayingSurahNumber.value != null &&
            QuranAudioManager.currentPlayingSurahNumber.value != surahNumber) {
            QuranAudioManager.stopMedia()
        }
    }

    DisposableEffect(surahNumber) {
        onDispose {
            QuranAudioManager.stopMedia()
        }
    }

    // Bottom sheet state for Translation & Tafseer (strictly follows global Settings language)
    var activeTranslationAyah by remember { mutableStateOf<Ayah?>(null) }
    var activeTafseerAyah by remember { mutableStateOf<Ayah?>(null) }
    val uriHandler = LocalUriHandler.current
    val translationRepo = remember(context) { QuranTranslationTafseerRepository.getInstance(context) }
    val prefsRepo = remember(context) { UserPreferencesRepository(context) }
    val bookmarks by prefsRepo.bookmarks.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        prefsRepo.reloadBookmarksFromPrefs()
    }

    val localizedSurahName = when (language) {
        AppLanguage.URDU -> surah.nameUrdu
        AppLanguage.ARABIC -> surah.nameArabic
        AppLanguage.HINDI -> surah.nameHindi
        AppLanguage.ENGLISH -> surah.nameEnglish
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = if (language == AppLanguage.ARABIC) "${surah.number}. ${surah.nameArabic}" else "${surah.number}. $localizedSurahName (${surah.nameArabic})",
                language = language,
                onBackClick = onBack,
                actions = {
                    IconButton(
                        onClick = { showFontSizeDialog = true },
                        modifier = Modifier.testTag("font_size_btn")
                    ) {
                        Icon(Icons.Default.FormatSize, contentDescription = AppLocalization.get("font_size", language))
                    }
                }
            )
        },
        bottomBar = {
            // Audio Reciter & Player Bar
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    val audioErr = QuranAudioManager.audioErrorMessage.value
                    if (!audioErr.isNullOrBlank()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .testTag("quran_audio_error_banner")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = AppLocalization.get("close", language),
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = audioErr,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { QuranAudioManager.clearAudioError() },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = AppLocalization.get("close", language),
                                        tint = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val reciterDisplay = if (language == AppLanguage.ARABIC || language == AppLanguage.URDU) {
                            selectedReciter.nameArabic.ifBlank { selectedReciter.nameEnglish }
                        } else {
                            selectedReciter.nameEnglish
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showReciterMenu = true }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "${surah.number}. ${surah.nameArabic}" else "${surah.number}. $localizedSurahName (${surah.nameArabic})",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = AppLocalization.get("reciter", language),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = if (QuranAudioManager.isBuffering.value && QuranAudioManager.currentPlayingSurahNumber.value == surah.number) {
                                    AppLocalization.get("quran_buffering", language)
                                } else if (QuranAudioManager.isAudioPlaying.value && QuranAudioManager.currentPlayingSurahNumber.value == surah.number) {
                                    "${AppLocalization.get("quran_playing", language)}: $reciterDisplay"
                                } else {
                                    "$reciterDisplay • ${AppLocalization.get("quran_tap_to_play", language)}"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = if (QuranAudioManager.isAudioPlaying.value && QuranAudioManager.currentPlayingSurahNumber.value == surah.number) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            DropdownMenu(
                                expanded = showReciterMenu,
                                onDismissRequest = { showReciterMenu = false }
                            ) {
                                QuranData.reciters.forEach { reciter ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(reciter.nameEnglish, fontWeight = FontWeight.Bold)
                                                Text(reciter.style, style = MaterialTheme.typography.labelSmall)
                                            }
                                        },
                                        onClick = {
                                            selectedReciter = reciter
                                            showReciterMenu = false
                                            if (QuranAudioManager.isAudioPlaying.value && QuranAudioManager.currentPlayingSurahNumber.value == surah.number) {
                                                QuranAudioManager.playSurah(
                                                    context = context,
                                                    surahNumber = surah.number,
                                                    surahName = surah.nameEnglish,
                                                    reciterId = reciter.id
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        val isSurahPlaying = QuranAudioManager.isAudioPlaying.value && 
                            QuranAudioManager.currentPlayingSurahNumber.value == surah.number && 
                            QuranAudioManager.currentPlayingAyahNumber.value == null
                        val isSurahBuffering = QuranAudioManager.isBuffering.value && 
                            QuranAudioManager.currentPlayingSurahNumber.value == surah.number && 
                            QuranAudioManager.currentPlayingAyahNumber.value == null

                        // Play / Pause recitation button
                        IconButton(
                            onClick = {
                                if (isSurahPlaying) {
                                    QuranAudioManager.togglePlayPause()
                                } else {
                                    QuranAudioManager.playSurah(
                                        context = context,
                                        surahNumber = surah.number,
                                        surahName = surah.nameEnglish,
                                        reciterId = selectedReciter.id
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .testTag("play_pause_audio_btn")
                        ) {
                            if (isSurahBuffering) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Icon(
                                    imageVector = if (isSurahPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isSurahPlaying) "Pause" else "Play",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Download Surah Recitation using genuine QuranOfflineSyncManager
                        IconButton(
                            onClick = {
                                if (isDownloadingAudio) {
                                    QuranOfflineSyncManager.cancelSurahDownload(context, surah.number, selectedReciter.id)
                                    Toast.makeText(context, AppLocalization.get("quran_download_cancelled", language), Toast.LENGTH_SHORT).show()
                                } else if (!isDownloadedOffline) {
                                    QuranOfflineSyncManager.downloadSurah(context, surah.number, selectedReciter.id) { res ->
                                        res.onSuccess {
                                            Toast.makeText(context, "${AppLocalization.get("offline_ready", language)}: $localizedSurahName", Toast.LENGTH_SHORT).show()
                                        }.onFailure { err ->
                                            Toast.makeText(context, err.message ?: AppLocalization.get("mosque_retry", language), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, AppLocalization.get("offline_ready", language), Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.testTag("download_surah_audio_btn")
                        ) {
                            if (isDownloadingAudio) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(
                                    imageVector = if (isDownloadedOffline) Icons.Default.CheckCircle else Icons.Default.Download,
                                    contentDescription = AppLocalization.get("audio_download", language),
                                    tint = if (isDownloadedOffline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("quran_reader_ayah_list")
        ) {
            // Surah Header Banner
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = surah.nameArabic,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontFamily = QuranNaskhFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) {
                                "${surah.nameEnglish} • ${surah.englishMeaning}"
                            } else {
                                localizedSurahName
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val revTypeLabel = if (surah.revelationType == RevelationType.MAKKI) {
                            AppLocalization.get("makki", language)
                        } else {
                            AppLocalization.get("madani", language)
                        }
                        Text(
                            text = "$revTypeLabel • ${surah.ayahCount} ${AppLocalization.get("ayah_count", language)} • ${AppLocalization.get("para", language)} ${surah.startingPara}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        if (surah.number != 9 && surah.number != 1) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = QuranNaskhFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 26.sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Ayahs
            items(ayahs, key = { it.ayahNumber }) { ayah ->
                val isAyahPlaying = QuranAudioManager.isAudioPlaying.value &&
                    QuranAudioManager.currentPlayingSurahNumber.value == ayah.surahNumber &&
                    QuranAudioManager.currentPlayingAyahNumber.value == ayah.ayahNumber
                val isAyahBookmarked = bookmarks.any {
                    it.surahNumber == ayah.surahNumber && it.ayahNumber == ayah.ayahNumber
                }
                AyahCard(
                    ayah = ayah,
                    language = language,
                    fontSize = arabicFontSize,
                    isPlaying = isAyahPlaying,
                    isBookmarked = isAyahBookmarked,
                    onToggleBookmark = {
                        val added = prefsRepo.toggleBookmark(
                            BookmarkItem(
                                id = "bm_quran_${ayah.surahNumber}_${ayah.ayahNumber}",
                                type = BookmarkType.QURAN,
                                title = "${AppLocalization.get("surah", language)} $localizedSurahName, ${ayah.surahNumber}:${ayah.ayahNumber}",
                                subtitle = ayah.textArabic.take(90),
                                surahNumber = ayah.surahNumber,
                                ayahNumber = ayah.ayahNumber
                            )
                        )
                        Toast.makeText(
                            context,
                            AppLocalization.get(if (added) "bookmarked" else "bookmark_removed", language),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onPlayAyah = {
                        if (isAyahPlaying) {
                            QuranAudioManager.togglePlayPause()
                        } else {
                            QuranAudioManager.playAyah(
                                context = context,
                                surahNumber = ayah.surahNumber,
                                ayahNumber = ayah.ayahNumber,
                                reciterId = selectedReciter.id
                            )
                        }
                    },
                    onOpenTranslation = { activeTranslationAyah = ayah },
                    onOpenTafseer = { activeTafseerAyah = ayah }
                )
            }

            // Surah Navigation Footer (Previous / Next Surah)
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp)
                ) {
                    if (surah.number > 1) {
                        OutlinedButton(
                            onClick = { onNavigateToSurah(surah.number - 1) },
                            modifier = Modifier.testTag("prev_surah_btn")
                        ) {
                            Text(AppLocalization.get("quran_prev_surah", language))
                        }
                    } else {
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    if (surah.number < 114) {
                        Button(
                            onClick = { onNavigateToSurah(surah.number + 1) },
                            modifier = Modifier.testTag("next_surah_btn")
                        ) {
                            Text(AppLocalization.get("quran_next_surah", language))
                        }
                    }
                }
            }
        }

        // Tarjuma Bottom Sheet — Strictly follows Global Settings Language
        if (activeTranslationAyah != null) {
            val curAyah = activeTranslationAyah!!
            val catalogForLang by translationRepo
                .observeTranslationsByLanguage(language)
                .collectAsState(initial = emptyList())
            val selectedTransIds by translationRepo.selectedTranslatorIds.collectAsState()
            val activeTransId = selectedTransIds[language.code]

            var resolvedTranslation by remember(curAyah.surahNumber, curAyah.ayahNumber, language, activeTransId) {
                mutableStateOf<ResolvedAyahTranslation?>(null)
            }
            var studyContext by remember(curAyah.surahNumber, curAyah.ayahNumber, language, activeTransId) {
                mutableStateOf<AyahRevelationContext?>(null)
            }

            LaunchedEffect(curAyah.surahNumber, curAyah.ayahNumber, language, activeTransId) {
                val resolved = translationRepo.resolveAyahTranslation(curAyah, language, activeTransId)
                resolvedTranslation = resolved
                studyContext = translationRepo.loadTarjumaStudyContext(
                    ayah = curAyah,
                    language = language,
                    activeTranslation = resolved,
                    fetchOnlineWisdom = false
                )
            }

            val tarjumaSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                sheetState = tarjumaSheetState,
                onDismissRequest = { activeTranslationAyah = null }
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tarjuma_sheet_list")
                ) {
                    item {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "ترجمہ و مطالعہ: آیت ${curAyah.surahNumber}:${curAyah.ayahNumber}"
                                AppLanguage.ARABIC -> "النص العربي وسياق الآية: ${curAyah.surahNumber}:${curAyah.ayahNumber}"
                                AppLanguage.HINDI -> "अनुवाद व अध्ययन: आयत ${curAyah.surahNumber}:${curAyah.ayahNumber}"
                                AppLanguage.ENGLISH -> "Tarjuma & Study: Ayah ${curAyah.surahNumber}:${curAyah.ayahNumber}"
                            },
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // 1. Arabic Quranic Ayah
                    item {
                        Text(
                            text = AppLocalization.get("quran_sec_1_arabic", language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${curAyah.textArabic} ۝${curAyah.ayahNumber}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = QuranNaskhFontFamily,
                                    fontSize = 24.sp,
                                    lineHeight = 44.sp
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }

                    // 2. Authentic Published Translation (strictly matching Settings language)
                    item {
                        Text(
                            text = AppLocalization.get("quran_sec_2_translation", language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (catalogForLang.size > 1) {
                            Spacer(modifier = Modifier.height(6.dp))
                            androidx.compose.foundation.lazy.LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(catalogForLang, key = { it.translationId }) { option ->
                                    FilterChip(
                                        selected = (activeTransId == option.translationId ||
                                            (activeTransId == null && resolvedTranslation?.translationId == option.translationId)),
                                        onClick = {
                                            translationRepo.selectTranslator(language, option.translationId)
                                        },
                                        label = { Text(option.translator, style = MaterialTheme.typography.labelSmall) },
                                        colors = noorSelectableChipColors()
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        val trans = resolvedTranslation
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (trans != null) {
                                    Text(
                                        text = trans.translatorName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = trans.isUnavailableNotice ?: trans.translatedText,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp, lineHeight = 26.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "${trans.publisher} • ${trans.editionVersion}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                                    )
                                } else {
                                    val fallback = curAyah.translations.firstOrNull { it.language == language }
                                    val fallbackDisplay = when {
                                        language == AppLanguage.ARABIC -> curAyah.textArabic
                                        fallback != null && fallback.translationText.isNotBlank() -> fallback.translationText
                                        else -> LocalizedContentResolver.unavailableTranslationNotice(language)
                                    }
                                    Text(
                                        text = fallbackDisplay,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    val ctx = studyContext
                    if (ctx == null) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                Text(AppLocalization.get("quran_loading_context", language), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    } else {
                        // 3. WHEN WAS THIS AYAH REVEALED? (Shaan-e-Nuzool removed per user request; lives in Tafseer)
                        item {
                            ContextInfoCard(
                                title = AppLocalization.get("quran_sec_4_when", language),
                                body = ctx.zamanaNuzool,
                                icon = Icons.Default.Schedule,
                                accentColor = Color(0xFFD97706)
                            )
                        }

                        // 5. HIDDEN WISDOM AND DEEP MEANING
                        item {
                            ContextInfoCard(
                                title = AppLocalization.get("quran_sec_5_wisdom", language),
                                body = ctx.maqsadHikmat,
                                icon = Icons.Default.Lightbulb,
                                accentColor = Color(0xFF0F766E)
                            )
                        }

                        // 6. BENEFITS AND PRACTICAL LESSONS
                        item {
                            ContextInfoCard(
                                title = AppLocalization.get("quran_sec_6_lessons", language),
                                body = ctx.fazaailBenefits,
                                icon = Icons.Default.Star,
                                accentColor = Color(0xFF7C3AED)
                            )
                        }

                        // 7. AUTHENTIC REFERENCES
                        item {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = AppLocalization.get("quran_sec_7_refs", language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = ctx.authenticReferences,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (ctx.referenceLinks.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        ctx.referenceLinks.forEach { (label, url) ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        try {
                                                            uriHandler.openUri(url)
                                                        } catch (_: Exception) {}
                                                    }
                                                    .padding(vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = label,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                                    contentDescription = label,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
            }
        }

        // Tafseer Bottom Sheet — Online Scholarly Exegesis, Auto-Synced to Settings Language, RTL/LTR Aware
        if (activeTafseerAyah != null) {
            val curAyah = activeTafseerAyah!!
            val scholars = remember(context, language) {
                QuranTafseerRepository.getTop5Scholars(language, context)
            }
            var selectedScholarKey by remember(language, scholars) {
                mutableStateOf(scholars.firstOrNull()?.key ?: "")
            }
            var retryTrigger by remember { mutableIntStateOf(0) }
            var isLoadingTafseer by remember(curAyah.surahNumber, curAyah.ayahNumber, selectedScholarKey, language, retryTrigger) {
                mutableStateOf(true)
            }
            var tafseerDetail by remember(curAyah.surahNumber, curAyah.ayahNumber, selectedScholarKey, language, retryTrigger) {
                mutableStateOf<ResolvedTafseerDetail?>(null)
            }

            LaunchedEffect(curAyah.surahNumber, curAyah.ayahNumber, selectedScholarKey, language, retryTrigger) {
                isLoadingTafseer = true
                tafseerDetail = try {
                    translationRepo.loadTafseerDetail(
                        surahNumber = curAyah.surahNumber,
                        ayahNumber = curAyah.ayahNumber,
                        sourceId = selectedScholarKey,
                        language = language,
                        forceRefresh = retryTrigger > 0
                    )
                } finally {
                    isLoadingTafseer = false
                }
            }

            val curIndex = ayahs.indexOfFirst { it.ayahNumber == curAyah.ayahNumber }
            val hasPrevAyah = curIndex > 0
            val hasNextAyah = curIndex in 0 until (ayahs.size - 1)
            val sheetLayoutDirection = if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
            val contentTextDirection = if (language.isRtl) TextDirection.Rtl else TextDirection.Ltr

            val tafseerSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                sheetState = tafseerSheetState,
                onDismissRequest = { activeTafseerAyah = null }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides sheetLayoutDirection) {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tafseer_sheet_list")
                    ) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = when (language) {
                                        AppLanguage.URDU -> "آیت ${curAyah.surahNumber}:${curAyah.ayahNumber} کی تفسیر"
                                        AppLanguage.ARABIC -> "تفسير الآية ${curAyah.surahNumber}:${curAyah.ayahNumber}"
                                        AppLanguage.HINDI -> "आयत ${curAyah.surahNumber}:${curAyah.ayahNumber} की तफ़सीर"
                                        AppLanguage.ENGLISH -> "Scholarly Tafseer — Ayah ${curAyah.surahNumber}:${curAyah.ayahNumber}"
                                    },
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        textDirection = contentTextDirection
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    IconButton(
                                        enabled = hasPrevAyah,
                                        onClick = {
                                            if (hasPrevAyah) {
                                                activeTafseerAyah = ayahs[curIndex - 1]
                                            }
                                        },
                                        modifier = Modifier.testTag("tafseer_prev_ayah_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = when (language) {
                                                AppLanguage.URDU -> "پچھلی آیت"
                                                AppLanguage.ARABIC -> "الآية السابقة"
                                                AppLanguage.HINDI -> "पिछली आयत"
                                                AppLanguage.ENGLISH -> "Previous Ayah"
                                            }
                                        )
                                    }
                                    IconButton(
                                        enabled = hasNextAyah,
                                        onClick = {
                                            if (hasNextAyah) {
                                                activeTafseerAyah = ayahs[curIndex + 1]
                                            }
                                        },
                                        modifier = Modifier.testTag("tafseer_next_ayah_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = when (language) {
                                                AppLanguage.URDU -> "اگلی آیت"
                                                AppLanguage.ARABIC -> "الآية التالية"
                                                AppLanguage.HINDI -> "अगली आयत"
                                                AppLanguage.ENGLISH -> "Next Ayah"
                                            }
                                        )
                                    }
                                    IconButton(
                                        onClick = { activeTafseerAyah = null },
                                        modifier = Modifier.testTag("tafseer_close_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = AppLocalization.get("close", language)
                                        )
                                    }
                                }
                            }
                        }

                        // Arabic Quranic Ayah Header (Always Preserved in Original Uthmani Script & RTL)
                        item {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("tafseer_arabic_ayah_header")
                                ) {
                                    Text(
                                        text = "${curAyah.textArabic} ۝${curAyah.ayahNumber}",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontFamily = QuranNaskhFontFamily,
                                            fontSize = 24.sp,
                                            lineHeight = 44.sp,
                                            textDirection = TextDirection.Rtl
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }

                        // Scholar Chips (strictly synchronized to active Settings language)
                        item {
                            androidx.compose.foundation.lazy.LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(scholars, key = { it.key }) { scholar ->
                                    val isSelected = selectedScholarKey == scholar.key
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedScholarKey = scholar.key },
                                        label = { Text(scholar.bookTitle, fontWeight = FontWeight.SemiBold) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                        } else null,
                                        colors = noorSelectableChipColors(),
                                        modifier = Modifier.testTag("scholar_chip_${scholar.key}")
                                    )
                                }
                            }
                        }

                        val activeScholarInfo = scholars.firstOrNull { it.key == selectedScholarKey } ?: scholars.firstOrNull()
                        val detail = tafseerDetail

                        // 1. Primary Scholarly Tafseer State (Loading / Error + Retry / Online Commentary)
                        if (isLoadingTafseer) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("tafseer_loading_card")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.padding(18.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .testTag("tafseer_loading_indicator"),
                                            strokeWidth = 2.5.dp
                                        )
                                        Column {
                                            Text(
                                                text = activeScholarInfo?.bookTitle.orEmpty(),
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    textDirection = contentTextDirection
                                                ),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = when (language) {
                                                    AppLanguage.URDU -> "آن لائن مستند تفسیر حاصل کی جا رہی ہے..."
                                                    AppLanguage.ARABIC -> "جارٍ جلب التفسير الموثق عبر الإنترنت..."
                                                    AppLanguage.HINDI -> "ऑनलाइन प्रामाणिक तफ़सीर लोड हो रही है..."
                                                    AppLanguage.ENGLISH -> "Fetching detailed scholarly Tafseer online..."
                                                },
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    textDirection = contentTextDirection
                                                ),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        } else if (detail != null && detail.passageText.isNullOrBlank()) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("tafseer_error_card")
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.padding(18.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CloudOff,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                            Text(
                                                text = activeScholarInfo?.bookTitle.orEmpty(),
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    textDirection = contentTextDirection
                                                ),
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                        Text(
                                            text = detail.onlineError
                                                ?: detail.passageLanguageNotice
                                                ?: VerifiedTafseerCatalog.onlineFetchErrorMessage(language, activeScholarInfo?.bookTitle.orEmpty()),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                lineHeight = 24.sp,
                                                textDirection = contentTextDirection
                                            ),
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                        Button(
                                            onClick = { retryTrigger++ },
                                            modifier = Modifier
                                                .defaultMinSize(minHeight = 48.dp)
                                                .testTag("tafseer_retry_btn")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = when (language) {
                                                    AppLanguage.URDU -> "دوبارہ کوشش کریں"
                                                    AppLanguage.ARABIC -> "إعادة المحاولة"
                                                    AppLanguage.HINDI -> "पुनः प्रयास करें"
                                                    AppLanguage.ENGLISH -> "Retry"
                                                },
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        } else if (detail != null && !detail.passageText.isNullOrBlank()) {
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Card(
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("tafseer_only_passage_card")
                                    ) {
                                        Column(modifier = Modifier.padding(18.dp)) {
                                            Text(
                                                text = detail.source.bookTitle.ifBlank { activeScholarInfo?.bookTitle.orEmpty() },
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    textDirection = contentTextDirection
                                                ),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = listOfNotNull(
                                                    detail.source.scholarName.takeIf { it.isNotBlank() },
                                                    detail.volumeAndPage?.takeIf { it.isNotBlank() }
                                                ).joinToString(" • "),
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    textDirection = contentTextDirection
                                                ),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = when (language) {
                                                    AppLanguage.URDU -> "تفصیلی علمی تفسیر (آن لائن)"
                                                    AppLanguage.ARABIC -> "التفسير العلمي المفصّل (عبر الإنترنت)"
                                                    AppLanguage.HINDI -> "प्रामाणिक तफ़सीर (ऑनलाइन)"
                                                    AppLanguage.ENGLISH -> "Detailed Scholarly Tafseer (Online)"
                                                },
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    textDirection = contentTextDirection
                                                ),
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = detail.passageText,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 16.sp,
                                                    lineHeight = 27.sp,
                                                    textDirection = contentTextDirection
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            val attribution = detail.editionAttribution.takeIf { it.isNotBlank() }
                                            if (attribution != null) {
                                                Spacer(modifier = Modifier.height(14.dp))
                                                HorizontalDivider(
                                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = attribution,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        textDirection = contentTextDirection
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }

                                    val langScopeNote = detail.languageAvailabilityNote?.takeIf { it.isNotBlank() }
                                    if (langScopeNote != null) {
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("tafseer_language_scope_note")
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.Top,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.padding(14.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Info,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.secondary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = langScopeNote,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        lineHeight = 20.sp,
                                                        textDirection = contentTextDirection
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 2. Dedicated Asbab-e-Nuzool Section
                        if (detail != null) {
                            item {
                                ContextInfoCard(
                                    title = when (language) {
                                        AppLanguage.URDU -> "شانِ نزول (اسبابِ نزول)"
                                        AppLanguage.ARABIC -> "أسباب النزول الموثقة"
                                        AppLanguage.HINDI -> "असबाब-ए-नुज़ूल (अवतरण का कारण)"
                                        AppLanguage.ENGLISH -> "Asbab-e-Nuzool (Occasion of Revelation)"
                                    },
                                    body = detail.asbabDisplayText,
                                    icon = Icons.Default.History,
                                    accentColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.testTag("tafseer_asbab_section")
                                )
                            }

                            // 3. Historical & Surah Revelation Context
                            item {
                                ContextInfoCard(
                                    title = when (language) {
                                        AppLanguage.URDU -> "تاریخی پس منظر و دورِ نزول"
                                        AppLanguage.ARABIC -> "السياق التاريخي وزمن النزول"
                                        AppLanguage.HINDI -> "ऐतिहासिक एवं अवतरण संदर्भ"
                                        AppLanguage.ENGLISH -> "Historical & Surah Revelation Context"
                                    },
                                    body = detail.historicalBackground,
                                    icon = Icons.Default.Schedule,
                                    accentColor = Color(0xFFD97706),
                                    modifier = Modifier.testTag("tafseer_historical_section")
                                )
                            }

                            // 4. Authenticated Hadith Cross-References & Benefits
                            if (detail.benefitsList.isNotEmpty() || detail.relatedHadiths.isNotEmpty()) {
                                val hadithAndBenefitsBody = buildString {
                                    detail.benefitsList.forEachIndexed { idx, benefit ->
                                        if (idx > 0) append("\n\n")
                                        append("• ${benefit.benefitText}\n[${benefit.sourceReference}]")
                                    }
                                    detail.relatedHadiths.forEach { h ->
                                        val hText = if (language == AppLanguage.ARABIC) {
                                            h.textArabic
                                        } else {
                                            LocalizedContentResolver.resolveHadithTranslation(h, language).text
                                        }
                                        if (!hText.isNullOrBlank()) {
                                            if (isNotEmpty()) append("\n\n")
                                            append("• $hText (${h.sourceAttribution})")
                                        }
                                    }
                                }
                                if (hadithAndBenefitsBody.isNotBlank()) {
                                    item {
                                        ContextInfoCard(
                                            title = when (language) {
                                                AppLanguage.URDU -> "مستند احادیث و علمی فوائد"
                                                AppLanguage.ARABIC -> "الأحاديث الصحيحة والفوائد المستنبطة"
                                                AppLanguage.HINDI -> "प्रामाणिक हदीस एवं संदर्भ"
                                                AppLanguage.ENGLISH -> "Authenticated Hadith & References"
                                            },
                                            body = hadithAndBenefitsBody,
                                            icon = Icons.Default.AutoStories,
                                            accentColor = Color(0xFF0F766E),
                                            modifier = Modifier.testTag("tafseer_hadith_benefits_section")
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(28.dp))
                        }
                    }
                }
            }
        }

        // Font Size Dialog
        if (showFontSizeDialog) {
            AlertDialog(
                onDismissRequest = { showFontSizeDialog = false },
                title = { Text(AppLocalization.get("font_size", language)) },
                text = {
                    Column {
                        Text(
                            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            fontSize = arabicFontSize.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )
                        Slider(
                            value = arabicFontSize,
                            onValueChange = { arabicFontSize = it },
                            valueRange = 18f..36f,
                            steps = 8
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showFontSizeDialog = false }) {
                        Text(AppLocalization.get("done", language))
                    }
                }
            )
        }
    }
}

@Composable
fun AyahCard(
    ayah: Ayah,
    language: AppLanguage,
    fontSize: Float,
    isPlaying: Boolean,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onPlayAyah: () -> Unit,
    onOpenTranslation: () -> Unit,
    onOpenTafseer: () -> Unit
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ayah_card_${ayah.ayahNumber}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Ayah Action Toolbar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                // Ayah Index Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${ayah.surahNumber}:${ayah.ayahNumber}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Play Ayah
                    IconButton(
                        onClick = onPlayAyah,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = "Play Ayah",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Bookmark Ayah
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("ayah_${ayah.ayahNumber}_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = AppLocalization.get("bookmarks", language),
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Copy Ayah
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Quran Ayah", "${ayah.textArabic}\n(Surah ${ayah.surahNumber}:${ayah.ayahNumber})")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, AppLocalization.get("copied", language), Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = AppLocalization.get("copy", language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Share Ayah
                    IconButton(
                        onClick = {
                            val activeTrans = ayah.translations.firstOrNull { it.language == language }
                            val shareTransPart = if (language == AppLanguage.ARABIC || activeTrans == null || activeTrans.translationText.isBlank()) {
                                ""
                            } else {
                                "\n\n${activeTrans.translationText}"
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "${ayah.textArabic}$shareTransPart\n(${AppLocalization.get("surah", language)} ${ayah.surahNumber}:${ayah.ayahNumber} - ${AppLocalization.get("app_name", language)})")
                            }
                            runCatching {
                                context.startActivity(Intent.createChooser(intent, AppLocalization.get("share", language)))
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = AppLocalization.get("share", language),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Arabic Text (Uthmani Verified Script in Naskh Calligraphy)
            Text(
                text = "${ayah.textArabic} ۝${ayah.ayahNumber}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = QuranNaskhFontFamily,
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * 1.9f).sp,
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Primary Translation Preview strictly matching active Settings language (Arabic mode preserves pure Arabic Ayah without English fallback)
            if (language != AppLanguage.ARABIC) {
                val currentTrans = ayah.translations.firstOrNull { it.language == language }
                val previewText = currentTrans?.translationText?.takeIf { it.isNotBlank() }
                    ?: LocalizedContentResolver.unavailableTranslationNotice(language)
                Text(
                    text = previewText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(6.dp))
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            // CRITICAL REQUIREMENT: TWO SEPARATE BUTTONS DIRECTLY BELOW EVERY AYAH
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                // Button 1: Translation
                OutlinedButton(
                    onClick = onOpenTranslation,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ayah_${ayah.ayahNumber}_translation_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppLocalization.get("translation_btn", language),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Button 2: Tafseer
                Button(
                    onClick = onOpenTafseer,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ayah_${ayah.ayahNumber}_tafseer_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppLocalization.get("tafseer_btn", language),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun ContextInfoCard(
    title: String,
    body: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = accentColor
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
