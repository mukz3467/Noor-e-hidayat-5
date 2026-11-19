package com.example.ui.screens.business

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.business.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.components.AppDirectionProvider
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessAcademyScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    vm: BusinessAcademyViewModel = viewModel(factory = BusinessAcademyViewModel.Factory(LocalContext.current))
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val selectedTab by vm.selectedTab.collectAsStateWithLifecycle()
    val selectedLevel by vm.selectedLevel.collectAsStateWithLifecycle()
    val searchQuery by vm.searchQuery.collectAsStateWithLifecycle()
    val activeDetail by vm.activeDetail.collectAsStateWithLifecycle()
    val progressMap by vm.progressMap.collectAsStateWithLifecycle()
    val quizResultsMap by vm.quizResultsMap.collectAsStateWithLifecycle()
    val bookmarksList by vm.bookmarksList.collectAsStateWithLifecycle()
    val bookmarksSet by vm.bookmarksSet.collectAsStateWithLifecycle()
    val notesMap by vm.notesMap.collectAsStateWithLifecycle()
    val startupStepsMap by vm.startupStepsMap.collectAsStateWithLifecycle()
    val certificatePath by vm.certificatePath.collectAsStateWithLifecycle()

    AppDirectionProvider(language = language) {
        val ready = uiState as? BusinessAcademyUiState.Ready
        val detail = activeDetail
        if (ready != null && detail != null) {
            BackHandler { vm.closeDetail() }
            when (detail) {
                is BusinessDetailDestination.Lesson -> {
                    val lesson = ready.lessons.firstOrNull { it.id == detail.lessonId }
                    if (lesson != null) {
                        val citations = remember(lesson.id) { vm.resolveQuranCitations(lesson.quranRefSpecs) }
                        BusinessLessonDetailScreen(
                            lesson = lesson,
                            quranCitations = citations,
                            hadithCatalog = ready.hadithCatalog,
                            isBookmarked = bookmarksSet.contains(lesson.id),
                            isCompleted = progressMap[lesson.id]?.isCompleted == true,
                            existingNote = notesMap[lesson.id]?.noteText.orEmpty(),
                            existingQuizResult = quizResultsMap[lesson.id],
                            language = language,
                            onToggleBookmark = { vm.toggleBookmark(lesson.id, "lesson", lesson.title.get(language)) },
                            onToggleCompleted = { vm.toggleCompleted(lesson.id, "lesson") },
                            onSaveNote = { vm.saveNote(lesson.id, "lesson", it) },
                            onSaveQuizScore = { s, t -> vm.saveQuizScore(lesson.id, "lesson", s, t) },
                            onBack = { vm.closeDetail() }
                        )
                        return@AppDirectionProvider
                    }
                }
                is BusinessDetailDestination.Contract -> {
                    val contract = ready.contracts.firstOrNull { it.id == detail.contractId }
                    if (contract != null) {
                        val citations = remember(contract.id) { vm.resolveQuranCitations(contract.quranRefSpecs) }
                        BusinessContractDetailScreen(
                            contract = contract,
                            quranCitations = citations,
                            hadithCatalog = ready.hadithCatalog,
                            isBookmarked = bookmarksSet.contains(contract.id),
                            isCompleted = progressMap[contract.id]?.isCompleted == true,
                            existingNote = notesMap[contract.id]?.noteText.orEmpty(),
                            existingQuizResult = quizResultsMap[contract.id],
                            language = language,
                            onToggleBookmark = { vm.toggleBookmark(contract.id, "contract", contract.name.get(language)) },
                            onToggleCompleted = { vm.toggleCompleted(contract.id, "contract") },
                            onSaveNote = { vm.saveNote(contract.id, "contract", it) },
                            onSaveQuizScore = { s, t -> vm.saveQuizScore(contract.id, "contract", s, t) },
                            onBack = { vm.closeDetail() }
                        )
                        return@AppDirectionProvider
                    }
                }
                is BusinessDetailDestination.Sector -> {
                    val sector = ready.sectors.firstOrNull { it.id == detail.sectorId }
                    if (sector != null) {
                        val citations = remember(sector.id) { vm.resolveQuranCitations(sector.quranRefSpecs) }
                        BusinessSectorDetailScreen(
                            sector = sector,
                            quranCitations = citations,
                            hadithCatalog = ready.hadithCatalog,
                            isBookmarked = bookmarksSet.contains(sector.id),
                            isCompleted = progressMap[sector.id]?.isCompleted == true,
                            existingNote = notesMap[sector.id]?.noteText.orEmpty(),
                            existingQuizResult = quizResultsMap[sector.id],
                            language = language,
                            onToggleBookmark = { vm.toggleBookmark(sector.id, "sector", sector.name.get(language)) },
                            onToggleCompleted = { vm.toggleCompleted(sector.id, "sector") },
                            onSaveNote = { vm.saveNote(sector.id, "sector", it) },
                            onSaveQuizScore = { s, t -> vm.saveQuizScore(sector.id, "sector", s, t) },
                            onBack = { vm.closeDetail() }
                        )
                        return@AppDirectionProvider
                    }
                }
                is BusinessDetailDestination.Specialized -> {
                    val topic = ready.specializedTopics.firstOrNull { it.id == detail.topicId }
                    if (topic != null) {
                        val citations = remember(topic.id) { vm.resolveQuranCitations(topic.quranRefSpecs) }
                        SpecializedTopicDetailScreen(
                            topic = topic,
                            quranCitations = citations,
                            hadithCatalog = ready.hadithCatalog,
                            isBookmarked = bookmarksSet.contains(topic.id),
                            isCompleted = progressMap[topic.id]?.isCompleted == true,
                            existingNote = notesMap[topic.id]?.noteText.orEmpty(),
                            existingQuizResult = quizResultsMap[topic.id],
                            language = language,
                            onToggleBookmark = { vm.toggleBookmark(topic.id, "specialized", topic.title.get(language)) },
                            onToggleCompleted = { vm.toggleCompleted(topic.id, "specialized") },
                            onSaveNote = { vm.saveNote(topic.id, "specialized", it) },
                            onSaveQuizScore = { s, t -> vm.saveQuizScore(topic.id, "specialized", s, t) },
                            onBack = { vm.closeDetail() }
                        )
                        return@AppDirectionProvider
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                NoorTopBar(
                    title = AppLocalization.get("business_academy", language),
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
                    .testTag("business_academy_screen")
            ) {
                when (val state = uiState) {
                    is BusinessAcademyUiState.Loading -> {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator()
                        }
                    }
                    is BusinessAcademyUiState.Error -> {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(24.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(state.message, color = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(onClick = { vm.loadContent() }) {
                                    Text(AppLocalization.get("ba_retry", language))
                                }
                            }
                        }
                    }
                    is BusinessAcademyUiState.Ready -> {
                        val totalCount = state.lessons.size + state.contracts.size + state.sectors.size + state.specializedTopics.size
                        val completedCount = progressMap.values.count { it.isCompleted }
                        val quizzesPassedCount = quizResultsMap.values.count { it.totalQuestions > 0 && it.bestScore * 2 >= it.totalQuestions }
                        val stepsCompletedCount = startupStepsMap.values.count { it.isCompleted }

                        // Hero Banner Card
                        BusinessHeroHeader(
                            language = language,
                            completedCount = completedCount,
                            totalCount = totalCount
                        )

                        // Offline Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { vm.updateSearchQuery(it) },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = AppLocalization.get("ba_search_placeholder", language)) },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(onClick = { vm.updateSearchQuery("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                                    }
                                }
                            },
                            placeholder = { Text(AppLocalization.get("ba_search_placeholder", language), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .testTag("business_search_input")
                        )

                        if (searchQuery.isNotBlank()) {
                            val hits = remember(searchQuery, language) { vm.search(searchQuery, language) }
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxSize().testTag("business_search_results")
                            ) {
                                if (hits.isEmpty()) {
                                    item {
                                        Text(
                                            text = AppLocalization.get("ba_no_search_results", language),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    items(hits, key = { "${it.type}_${it.id}" }) { hit ->
                                        AcademyItemCard(
                                            title = hit.title.get(language),
                                            subtitle = hit.subtitle.get(language),
                                            badgeText = hit.type.uppercase(),
                                            isCompleted = progressMap[hit.id]?.isCompleted == true,
                                            isBookmarked = bookmarksSet.contains(hit.id),
                                            tag = "search_hit_${hit.id}",
                                            onClick = { openByTypeAndId(vm, hit.id, hit.type) }
                                        )
                                    }
                                }
                            }
                        } else {
                            val tabLabels = listOf(
                                AppLocalization.get("ba_tab_curriculum", language),
                                "${AppLocalization.get("ba_tab_contracts", language)} (14)",
                                "${AppLocalization.get("ba_tab_sectors", language)} (30)",
                                "${AppLocalization.get("ba_tab_specialized", language)} (23)",
                                AppLocalization.get("ba_tab_tools", language),
                                AppLocalization.get("ba_tab_saved", language)
                            )
                            PrimaryScrollableTabRow(
                                selectedTabIndex = selectedTab,
                                edgePadding = 12.dp
                            ) {
                                tabLabels.forEachIndexed { idx, label ->
                                    Tab(
                                        selected = selectedTab == idx,
                                        onClick = { vm.selectTab(idx) },
                                        selectedContentColor = MaterialTheme.colorScheme.primary,
                                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        text = {
                                            Text(
                                                text = label,
                                                maxLines = 1,
                                                fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        modifier = Modifier.testTag("business_tab_$idx")
                                    )
                                }
                            }

                            when (selectedTab) {
                                0 -> CurriculumTab(
                                    lessons = state.lessons,
                                    sectors = state.sectors,
                                    selectedLevel = selectedLevel,
                                    language = language,
                                    progressMap = progressMap,
                                    bookmarksSet = bookmarksSet,
                                    onSelectLevel = { vm.selectLevel(it) },
                                    onOpenLesson = { vm.openDetail(BusinessDetailDestination.Lesson(it)) },
                                    onOpenSector = { vm.openDetail(BusinessDetailDestination.Sector(it)) }
                                )
                                1 -> LazyColumn(
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize().testTag("contracts_list")
                                ) {
                                    items(state.contracts, key = { it.id }) { c ->
                                        AcademyItemCard(
                                            title = c.name.get(language),
                                            subtitle = "${c.arabicTerm} • ${c.aaoifiStandard}",
                                            badgeText = "#${c.order}",
                                            isCompleted = progressMap[c.id]?.isCompleted == true,
                                            isBookmarked = bookmarksSet.contains(c.id),
                                            tag = "contract_card_${c.id}",
                                            onClick = { vm.openDetail(BusinessDetailDestination.Contract(c.id)) }
                                        )
                                    }
                                }
                                2 -> LazyColumn(
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize().testTag("sectors_list")
                                ) {
                                    items(state.sectors, key = { it.id }) { s ->
                                        AcademyItemCard(
                                            title = s.name.get(language),
                                            subtitle = s.introductionA.get(language),
                                            badgeText = "S${s.sectorNumber}",
                                            isCompleted = progressMap[s.id]?.isCompleted == true,
                                            isBookmarked = bookmarksSet.contains(s.id),
                                            tag = "sector_card_${s.id}",
                                            onClick = { vm.openDetail(BusinessDetailDestination.Sector(s.id)) }
                                        )
                                    }
                                }
                                3 -> LazyColumn(
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize().testTag("specialized_list")
                                ) {
                                    items(state.specializedTopics, key = { it.id }) { t ->
                                        AcademyItemCard(
                                            title = t.title.get(language),
                                            subtitle = t.overview.get(language),
                                            badgeText = if (t.isDisputedContemporaryIssue) {
                                                AppLocalization.get("ba_disputed_chip", language)
                                            } else {
                                                "T${t.order}"
                                            },
                                            isCompleted = progressMap[t.id]?.isCompleted == true,
                                            isBookmarked = bookmarksSet.contains(t.id),
                                            tag = "specialized_card_${t.id}",
                                            onClick = { vm.openDetail(BusinessDetailDestination.Specialized(t.id)) }
                                        )
                                    }
                                }
                                4 -> BusinessToolsAndProjectTab(
                                    language = language,
                                    startupStepsMap = startupStepsMap,
                                    onSaveStartupStep = { step, text, done -> vm.saveStartupStep(step, text, done) }
                                )
                                5 -> BusinessSavedAndCertTab(
                                    language = language,
                                    bookmarks = bookmarksList,
                                    notes = notesMap.values.toList(),
                                    completedCount = completedCount,
                                    totalItemsCount = totalCount,
                                    quizzesPassedCount = quizzesPassedCount,
                                    startupStepsCompleted = stepsCompletedCount,
                                    certificatePath = certificatePath,
                                    onOpenSavedItem = { id, type -> openByTypeAndId(vm, id, type) },
                                    onGenerateCertificate = { name -> vm.generateCertificate(name) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun openByTypeAndId(vm: BusinessAcademyViewModel, id: String, type: String) {
    when (type) {
        "lesson" -> vm.openDetail(BusinessDetailDestination.Lesson(id))
        "contract" -> vm.openDetail(BusinessDetailDestination.Contract(id))
        "sector" -> vm.openDetail(BusinessDetailDestination.Sector(id))
        "specialized" -> vm.openDetail(BusinessDetailDestination.Specialized(id))
    }
}

@Composable
private fun BusinessHeroHeader(
    language: AppLanguage,
    completedCount: Int,
    totalCount: Int
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(130.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img_business_academy_hero),
                contentDescription = AppLocalization.get("business_academy", language),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.56f))
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize().padding(14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f)
                    ) {
                        Text(
                            text = AppLocalization.get("ba_offline_badge", language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = "$completedCount / $totalCount",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Column {
                    Text(
                        text = AppLocalization.get("ba_hero_subtitle", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { if (totalCount == 0) 0f else completedCount.toFloat() / totalCount },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun CurriculumTab(
    lessons: List<BusinessLesson>,
    sectors: List<BusinessSectorGuide>,
    selectedLevel: Int,
    language: AppLanguage,
    progressMap: Map<String, BusinessProgressEntity>,
    bookmarksSet: Set<String>,
    onSelectLevel: (Int) -> Unit,
    onOpenLesson: (String) -> Unit,
    onOpenSector: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            listOf(
                0 to AppLocalization.get("all_label", language),
                1 to "${AppLocalization.get("ba_level_prefix", language)} 1",
                2 to "${AppLocalization.get("ba_level_prefix", language)} 2",
                3 to "${AppLocalization.get("ba_level_prefix", language)} 3 (30)",
                4 to "${AppLocalization.get("ba_level_prefix", language)} 4"
            ).forEach { (lvl, lbl) ->
                FilterChip(
                    selected = selectedLevel == lvl,
                    onClick = { onSelectLevel(lvl) },
                    label = { Text(lbl) },
                    colors = noorSelectableChipColors(),
                    modifier = Modifier.testTag("level_chip_$lvl")
                )
            }
        }

        if (selectedLevel == 3) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sectors, key = { it.id }) { s ->
                    AcademyItemCard(
                        title = s.name.get(language),
                        subtitle = s.introductionA.get(language),
                        badgeText = "L3 • S${s.sectorNumber}",
                        isCompleted = progressMap[s.id]?.isCompleted == true,
                        isBookmarked = bookmarksSet.contains(s.id),
                        tag = "curriculum_sector_${s.id}",
                        onClick = { onOpenSector(s.id) }
                    )
                }
            }
        } else {
            val filtered = if (selectedLevel == 0) lessons else lessons.filter { it.level == selectedLevel }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize().testTag("curriculum_lessons_list")
            ) {
                items(filtered, key = { it.id }) { l ->
                    AcademyItemCard(
                        title = l.title.get(language),
                        subtitle = l.moduleTitle.get(language),
                        badgeText = "L${l.level} • M${l.moduleNumber}",
                        isCompleted = progressMap[l.id]?.isCompleted == true,
                        isBookmarked = bookmarksSet.contains(l.id),
                        tag = "lesson_card_${l.id}",
                        onClick = { onOpenLesson(l.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AcademyItemCard(
    title: String,
    subtitle: String,
    badgeText: String,
    isCompleted: Boolean,
    isBookmarked: Boolean,
    tag: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().testTag(tag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(14.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    if (isBookmarked) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Bookmarked",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
