package com.example.ui.screens.huqooq

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.huqooq.*
import com.example.data.model.AppLanguage
import com.example.localization.AppLocalization
import com.example.ui.components.AppDirectionProvider
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

@Composable
fun HuqooqAcademyScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    vm: HuqooqAcademyViewModel = viewModel(factory = HuqooqAcademyViewModel.Factory(LocalContext.current))
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val currentRoute by vm.currentRoute.collectAsStateWithLifecycle()
    val searchQuery by vm.searchQuery.collectAsStateWithLifecycle()
    val selectedGroupFilter by vm.selectedGroupFilter.collectAsStateWithLifecycle()
    val progressMap by vm.progressMap.collectAsStateWithLifecycle()
    val bookmarksList by vm.bookmarksList.collectAsStateWithLifecycle()
    val bookmarksSet by vm.bookmarksSet.collectAsStateWithLifecycle()
    val quizResultsMap by vm.quizResultsMap.collectAsStateWithLifecycle()
    val metaState by vm.metaState.collectAsStateWithLifecycle()

    AppDirectionProvider(language = language) {
        if (currentRoute !is HuqooqRoute.Home) {
            BackHandler {
                if (!vm.navigateBack()) onBack()
            }
        }

        val ready = uiState as? HuqooqAcademyUiState.Ready
        when (val route = currentRoute) {
            is HuqooqRoute.InheritanceCalculator -> {
                HuqooqInheritanceCalculatorScreen(
                    language = language,
                    onBack = { vm.navigateBack() }
                )
                return@AppDirectionProvider
            }
            is HuqooqRoute.LessonDetail -> {
                val lesson = ready?.lessons?.firstOrNull { it.id == route.lessonId }
                if (ready != null && lesson != null) {
                    val citations = remember(lesson.id) { vm.resolveQuranCitations(lesson.quranRefSpecs) }
                    HuqooqLessonDetailScreen(
                        lesson = lesson,
                        quranCitations = citations,
                        hadithCatalog = ready.hadithCatalog,
                        isBookmarked = bookmarksSet.contains(lesson.id),
                        isCompleted = progressMap[lesson.id]?.isCompleted == true,
                        existingQuizResult = quizResultsMap[lesson.id],
                        textScale = metaState?.lessonTextScale ?: 1.0f,
                        language = language,
                        onAdjustTextScale = { vm.adjustTextScale(it) },
                        onToggleBookmark = { vm.toggleBookmark(lesson, language) },
                        onToggleCompleted = { vm.toggleCompleted(lesson) },
                        onSaveQuizScore = { s, t -> vm.saveQuizScore(lesson, s, t) },
                        onOpenCalculator = { vm.openCalculator() },
                        onBack = { vm.navigateBack() }
                    )
                    return@AppDirectionProvider
                }
            }
            is HuqooqRoute.Department -> {
                if (ready != null) {
                    HuqooqDepartmentScreen(
                        department = route.department,
                        allLessons = ready.lessons,
                        selectedGroupFilter = selectedGroupFilter,
                        progressMap = progressMap,
                        bookmarksSet = bookmarksSet,
                        language = language,
                        onSelectGroupFilter = { vm.selectGroupFilter(it) },
                        onOpenLesson = { vm.openLesson(it, language) },
                        onOpenCalculator = { vm.openCalculator() },
                        onBack = { vm.navigateBack() }
                    )
                    return@AppDirectionProvider
                }
            }
            is HuqooqRoute.Bookmarks -> {
                HuqooqBookmarksScreen(
                    bookmarks = bookmarksList,
                    lessons = ready?.lessons.orEmpty(),
                    progressMap = progressMap,
                    language = language,
                    onOpenLesson = { vm.openLesson(it, language) },
                    onBack = { vm.navigateBack() }
                )
                return@AppDirectionProvider
            }
            HuqooqRoute.Home -> Unit
        }

        // Hub Home Screen
        Scaffold(
            topBar = {
                NoorTopBar(
                    title = AppLocalization.get("huqooq_academy", language),
                    language = language,
                    onBackClick = onBack,
                    actions = {
                        IconButton(
                            onClick = { vm.openBookmarks() },
                            modifier = Modifier.minimumInteractiveComponentSize().testTag("hq_open_bookmarks_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmarks,
                                contentDescription = AppLocalization.get("hq_bookmarks_btn", language)
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
                    .testTag("huqooq_academy_screen")
            ) {
                when (val state = uiState) {
                    is HuqooqAcademyUiState.Loading -> {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator()
                        }
                    }
                    is HuqooqAcademyUiState.Error -> {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(24.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(state.message, color = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(onClick = { vm.loadContent() }) {
                                    Text(AppLocalization.get("hq_retry", language))
                                }
                            }
                        }
                    }
                    is HuqooqAcademyUiState.Ready -> {
                        val totalLessons = state.lessons.size
                        val completedTotal = progressMap.values.count { it.isCompleted }

                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { vm.updateSearchQuery(it) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = AppLocalization.get("hq_search_placeholder", language))
                            },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(onClick = { vm.updateSearchQuery("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                                    }
                                }
                            },
                            placeholder = {
                                Text(
                                    AppLocalization.get("hq_search_placeholder", language),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("hq_search_input")
                        )

                        if (searchQuery.isNotBlank()) {
                            val hits = remember(searchQuery, language) { vm.search(searchQuery, language) }
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxSize().testTag("hq_search_results")
                            ) {
                                if (hits.isEmpty()) {
                                    item {
                                        Text(
                                            text = AppLocalization.get("hq_no_search_results", language),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    items(hits, key = { it.id }) { hit ->
                                        HuqooqLessonListCard(
                                            title = hit.title.get(language),
                                            subtitle = "${hit.department.title(language)} • ${hit.subtitle.get(language)}",
                                            badgeText = hit.department.id.removePrefix("dept_").uppercase(),
                                            isCompleted = progressMap[hit.id]?.isCompleted == true,
                                            isBookmarked = bookmarksSet.contains(hit.id),
                                            tag = "hq_search_hit_${hit.id}",
                                            onClick = { vm.openLesson(hit.id, language) }
                                        )
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize().testTag("hq_home_list")
                            ) {
                                // Hero Banner
                                item {
                                    HuqooqHeroCard(
                                        language = language,
                                        completedCount = completedTotal,
                                        totalCount = totalLessons
                                    )
                                }

                                // Continue Reading Card (if meta has last opened lesson)
                                val lastLessonId = metaState?.lastViewedLessonId
                                val lastLesson = if (lastLessonId != null) {
                                    state.lessons.firstOrNull { it.id == lastLessonId }
                                } else null
                                if (lastLesson != null) {
                                    item {
                                        Card(
                                            onClick = { vm.openLesson(lastLesson.id, language) },
                                            shape = RoundedCornerShape(14.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                                            modifier = Modifier.fillMaxWidth().testTag("hq_continue_reading_card")
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth().padding(14.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PlayArrow,
                                                    contentDescription = AppLocalization.get("hq_resume", language),
                                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = AppLocalization.get("hq_continue_reading", language),
                                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                    Text(
                                                        text = lastLesson.title.get(language),
                                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                                FilledTonalButton(
                                                    onClick = { vm.openLesson(lastLesson.id, language) },
                                                    modifier = Modifier.height(48.dp)
                                                ) {
                                                    Text(AppLocalization.get("hq_resume", language))
                                                }
                                            }
                                        }
                                    }
                                }

                                // Quick Inheritance Calculator Card
                                item {
                                    Card(
                                        onClick = { vm.openCalculator() },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        modifier = Modifier.fillMaxWidth().testTag("hq_open_calculator_btn")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(MaterialTheme.colorScheme.primary)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Calculate,
                                                    contentDescription = AppLocalization.get("hq_open_calculator_title", language),
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(14.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = AppLocalization.get("hq_open_calculator_title", language),
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = AppLocalization.get("hq_open_calculator_sub", language),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }

                                item {
                                    Text(
                                        text = AppLocalization.get("hq_departments_heading", language),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                // Four Department Cards
                                items(HuqooqDepartment.entries, key = { it.id }) { dept ->
                                    val deptLessons = state.lessons.filter { it.department == dept }
                                    val deptDone = deptLessons.count { progressMap[it.id]?.isCompleted == true }
                                    HuqooqDepartmentCard(
                                        department = dept,
                                        icon = departmentIcon(dept),
                                        completedCount = deptDone,
                                        totalCount = deptLessons.size,
                                        language = language,
                                        onClick = { vm.openDepartment(dept) }
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
private fun HuqooqHeroCard(
    language: AppLanguage,
    completedCount: Int,
    totalCount: Int
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                    ) {
                        Text(
                            text = AppLocalization.get("hq_offline_badge", language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = "$completedCount / $totalCount ${AppLocalization.get("hq_completed_lessons", language)}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = AppLocalization.get("hq_hero_subtitle", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                LinearProgressIndicator(
                    progress = { if (totalCount == 0) 0f else completedCount.toFloat() / totalCount },
                    color = MaterialTheme.colorScheme.onPrimary,
                    trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.28f),
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                )
            }
        }
    }
}

@Composable
private fun HuqooqDepartmentCard(
    department: HuqooqDepartment,
    icon: ImageVector,
    completedCount: Int,
    totalCount: Int,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("huqooq_${department.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = department.title(language),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = department.title(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = department.subtitle(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { if (totalCount == 0) 0f else completedCount.toFloat() / totalCount },
                    modifier = Modifier.weight(1f).height(5.dp).clip(RoundedCornerShape(3.dp))
                )
                Text(
                    text = "$completedCount/$totalCount ${AppLocalization.get("hq_lessons_count", language)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun HuqooqDepartmentScreen(
    department: HuqooqDepartment,
    allLessons: List<HuqooqLesson>,
    selectedGroupFilter: String,
    progressMap: Map<String, HuqooqProgressEntity>,
    bookmarksSet: Set<String>,
    language: AppLanguage,
    onSelectGroupFilter: (String) -> Unit,
    onOpenLesson: (String) -> Unit,
    onOpenCalculator: () -> Unit,
    onBack: () -> Unit
) {
    val deptLessons = remember(allLessons, department) { allLessons.filter { it.department == department } }
    val groups = remember(deptLessons, language) {
        deptLessons.distinctBy { it.groupCode }.map { it.groupCode to it.groupLabel.get(language) }
    }
    val filtered = remember(deptLessons, selectedGroupFilter) {
        if (selectedGroupFilter == "ALL") deptLessons else deptLessons.filter { it.groupCode == selectedGroupFilter }
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = department.title(language),
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
                .testTag("hq_department_screen_${department.id}")
        ) {
            if (groups.size > 1) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    FilterChip(
                        selected = selectedGroupFilter == "ALL",
                        onClick = { onSelectGroupFilter("ALL") },
                        label = { Text(AppLocalization.get("all_label", language)) },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.testTag("hq_group_chip_ALL")
                    )
                    groups.forEach { (code, label) ->
                        FilterChip(
                            selected = selectedGroupFilter == code,
                            onClick = { onSelectGroupFilter(code) },
                            label = { Text(label) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.testTag("hq_group_chip_$code")
                        )
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize().testTag("hq_department_lessons_list")
            ) {
                if (department == HuqooqDepartment.ILM_UL_FARAID) {
                    item {
                        Card(
                            onClick = onOpenCalculator,
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth().testTag("hq_dept_open_calculator")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = AppLocalization.get("hq_open_calculator_title", language),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = AppLocalization.get("hq_open_calculator_title", language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                items(filtered, key = { it.id }) { lesson ->
                    HuqooqLessonListCard(
                        title = lesson.title.get(language),
                        subtitle = lesson.introduction.get(language),
                        badgeText = "#${lesson.order} • ${lesson.groupLabel.get(language)}",
                        isCompleted = progressMap[lesson.id]?.isCompleted == true,
                        isBookmarked = bookmarksSet.contains(lesson.id),
                        tag = "hq_lesson_card_${lesson.id}",
                        onClick = { onOpenLesson(lesson.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HuqooqBookmarksScreen(
    bookmarks: List<HuqooqBookmarkEntity>,
    lessons: List<HuqooqLesson>,
    progressMap: Map<String, HuqooqProgressEntity>,
    language: AppLanguage,
    onOpenLesson: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("hq_bookmarks_btn", language),
                language = language,
                onBackClick = onBack
            )
        }
    ) { innerPadding ->
        if (bookmarks.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp)
                    .testTag("hq_bookmarks_empty")
            ) {
                Text(
                    text = AppLocalization.get("hq_no_bookmarks", language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .testTag("hq_bookmarks_list")
            ) {
                items(bookmarks, key = { it.itemId }) { bm ->
                    val lesson = lessons.firstOrNull { it.id == bm.itemId }
                    HuqooqLessonListCard(
                        title = lesson?.title?.get(language) ?: bm.titleSnapshot,
                        subtitle = lesson?.groupLabel?.get(language) ?: bm.departmentId,
                        badgeText = lesson?.let { "#${it.order}" } ?: bm.departmentId,
                        isCompleted = progressMap[bm.itemId]?.isCompleted == true,
                        isBookmarked = true,
                        tag = "hq_bookmark_card_${bm.itemId}",
                        onClick = { onOpenLesson(bm.itemId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HuqooqLessonListCard(
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
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

private fun departmentIcon(dept: HuqooqDepartment): ImageVector = when (dept) {
    HuqooqDepartment.RIGHTS_RESPONSIBILITIES -> Icons.Default.Handshake
    HuqooqDepartment.ILM_UL_FARAID -> Icons.Default.Balance
    HuqooqDepartment.ISLAH_MUASHRA -> Icons.Default.Diversity3
    HuqooqDepartment.AKHLAQ_ADAAB -> Icons.Default.VolunteerActivism
}
