package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.QuranAudioManager
import com.example.data.repository.QuranLearningRepository
import com.example.ui.components.*

@Composable
fun LearningScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // 3-level Navigation inside Academy: Roadmap -> Stage Detail -> Lesson Detail
    var selectedStageNumber by remember { mutableStateOf<Int?>(null) }
    var selectedLessonId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Sub-tool states for Stages 7, 8, 11
    var stage7ToolTab by rememberSaveable { mutableIntStateOf(0) }
    var selectedQaidaIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedWritingLetter by remember { mutableStateOf(QuranLearningRepository.writingExercises.first()) }
    var stage8ToolTab by rememberSaveable { mutableIntStateOf(0) }
    var selectedWbwSurahFilter by rememberSaveable { mutableIntStateOf(0) }
    var selectedQuizStageFilter by rememberSaveable { mutableIntStateOf(0) }

    var progressRefreshTrigger by remember { mutableIntStateOf(0) }
    val progress = remember(progressRefreshTrigger) {
        QuranLearningRepository.getProgress(context)
    }

    LaunchedEffect(Unit) {
        QuranAudioManager.init(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            QuranAudioManager.stopMedia()
        }
    }

    val navigateBackInAcademy: () -> Unit = {
        when {
            selectedLessonId != null -> selectedLessonId = null
            selectedStageNumber != null -> selectedStageNumber = null
            else -> onBack()
        }
    }

    BackHandler(enabled = selectedLessonId != null || selectedStageNumber != null) {
        navigateBackInAcademy()
    }

    val activeStage = selectedStageNumber?.let { QuranLearningRepository.getStageByNumber(it) }
    val activeLesson = selectedLessonId?.let { QuranLearningRepository.getLessonById(it) }

    val allCurriculumLessonIds = remember {
        QuranLearningRepository.allStages.flatMap { stage -> stage.lessons.map { it.id } }.toSet()
    }
    val completedCurriculumCount = progress.completedLessonIds.count { it in allCurriculumLessonIds }
    val totalCurriculumCount = allCurriculumLessonIds.size.coerceAtLeast(1)

    val isRtl = language.isRtl

    CompositionLocalProvider(LocalLayoutDirection provides if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        Scaffold(
            topBar = {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        IconButton(
                            onClick = navigateBackInAcademy,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("academy_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = com.example.localization.AppLocalization.get("back_cd", language),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            val topTitle = when {
                                activeLesson != null -> activeLesson.titleFor(language)
                                activeStage != null -> activeStage.titleFor(language)
                                else -> when (language) {
                                    AppLanguage.ARABIC -> "أكاديمية تعليم القرآن"
                                    AppLanguage.URDU -> "قرآن لرننگ اکیڈمی"
                                    AppLanguage.HINDI -> "क़ुरआन लर्निंग एकेडमी"
                                    AppLanguage.ENGLISH -> "Quran Learning Academy"
                                }
                            }
                            val topSubtitle = when {
                                activeLesson != null && activeStage != null ->
                                    activeStage.titleFor(language)
                                activeStage != null ->
                                    activeStage.subtitleFor(language)
                                else -> when (language) {
                                    AppLanguage.ARABIC -> "منهج متكامل في ١١ مرحلة للقرآن والإسلام"
                                    AppLanguage.URDU -> "نئے مسلمانوں اور طلبہ کے لیے 11 مراحل کا مکمل نصاب"
                                    AppLanguage.HINDI -> "11 चरणों का संपूर्ण इस्लामी व क़ुरआनी पाठ्यक्रम"
                                    AppLanguage.ENGLISH -> "11-Stage Islam & Quran Course"
                                }
                            }
                            Text(
                                text = topTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = topSubtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when {
                    activeLesson != null && activeStage != null -> {
                        LessonDetailContent(
                            stage = activeStage,
                            lesson = activeLesson,
                            language = language,
                            isCompleted = progress.completedLessonIds.contains(activeLesson.id),
                            onLessonOpened = {
                                QuranLearningRepository.recordLessonOpened(
                                    context = context,
                                    stageNumber = activeStage.stageNumber,
                                    lessonId = activeLesson.id,
                                    lessonTitle = "${com.example.localization.AppLocalization.get("learning_stage_prefix", language)} ${activeStage.stageNumber} · ${activeLesson.titleFor(language)}"
                                )
                                progressRefreshTrigger++
                            },
                            onMarkComplete = {
                                QuranLearningRepository.markLessonCompleted(
                                    context = context,
                                    lessonId = activeLesson.id,
                                    lessonTitle = "${com.example.localization.AppLocalization.get("learning_stage_prefix", language)} ${activeStage.stageNumber} · ${activeLesson.titleFor(language)}",
                                    moduleId = "stage_${activeStage.stageNumber}",
                                    stageNumber = activeStage.stageNumber
                                )
                                progressRefreshTrigger++
                                Toast.makeText(context, "✓", Toast.LENGTH_SHORT).show()
                            },
                            onQuizPassed = { quizId ->
                                QuranLearningRepository.recordQuizPass(context, quizId)
                                progressRefreshTrigger++
                            },
                            onNavigateLesson = { targetLessonId ->
                                selectedLessonId = targetLessonId
                            }
                        )
                    }

                    activeStage != null -> {
                        StageDetailContent(
                            stage = activeStage,
                            language = language,
                            progress = progress,
                            stage7ToolTab = stage7ToolTab,
                            onSelectStage7ToolTab = { stage7ToolTab = it },
                            selectedQaidaIndex = selectedQaidaIndex,
                            onSelectQaidaIndex = { selectedQaidaIndex = it },
                            selectedWritingLetter = selectedWritingLetter,
                            onSelectWritingLetter = { selectedWritingLetter = it },
                            stage8ToolTab = stage8ToolTab,
                            onSelectStage8ToolTab = { stage8ToolTab = it },
                            selectedWbwSurahFilter = selectedWbwSurahFilter,
                            onSelectWbwSurahFilter = { selectedWbwSurahFilter = it },
                            selectedQuizStageFilter = selectedQuizStageFilter,
                            onSelectQuizStageFilter = { selectedQuizStageFilter = it },
                            onOpenLesson = { lessonId ->
                                selectedLessonId = lessonId
                            },
                            onProgressChanged = {
                                progressRefreshTrigger++
                            }
                        )
                    }

                    else -> {
                        AcademyRoadmapContent(
                            language = language,
                            progress = progress,
                            completedCurriculumCount = completedCurriculumCount,
                            totalCurriculumCount = totalCurriculumCount,
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it },
                            onContinueLearning = {
                                selectedStageNumber = progress.lastActiveStageNumber
                                selectedLessonId = progress.lastActiveLessonId
                            },
                            onOpenStage = { stageNumber ->
                                selectedStageNumber = stageNumber
                            },
                            onOpenSearchResult = { stageNumber, lessonId ->
                                selectedStageNumber = stageNumber
                                selectedLessonId = lessonId
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AcademyRoadmapContent(
    language: AppLanguage,
    progress: StudentAcademyProgress,
    completedCurriculumCount: Int,
    totalCurriculumCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onContinueLearning: () -> Unit,
    onOpenStage: (Int) -> Unit,
    onOpenSearchResult: (Int, String) -> Unit
) {
    val searchResults = remember(searchQuery, language) {
        QuranLearningRepository.searchLessons(searchQuery, language)
    }

    val dashboardTitle = when (language) {
        AppLanguage.ARABIC -> "لوحة تقدم الطالب"
        AppLanguage.URDU -> "آپ کی تعلیمی پیش رفت"
        AppLanguage.HINDI -> "आपकी शिक्षण प्रगति"
        AppLanguage.ENGLISH -> "Student Learning Dashboard"
    }
    val continuePrefix = when (language) {
        AppLanguage.ARABIC -> "متابعة التعلم: "
        AppLanguage.URDU -> "تعلیم جاری رکھیں: "
        AppLanguage.HINDI -> "पढ़ाई जारी रखें: "
        AppLanguage.ENGLISH -> "Continue: "
    }
    val searchHint = when (language) {
        AppLanguage.URDU -> "اسباق، وضو، نماز، تجوید یا موضوع تلاش کریں..."
        AppLanguage.ARABIC -> "ابحث في الدروس: الوضوء، الصلاة، التجويد..."
        AppLanguage.HINDI -> "सबक़, वुज़ू, नमाज़, तजवीद या विषय खोजें..."
        AppLanguage.ENGLISH -> "Search lessons, Wudu, Salah, Tajweed..."
    }
    val roadmapHeading = when (language) {
        AppLanguage.URDU -> "مکمل 11 مراحل پر مشتمل نصاب"
        AppLanguage.ARABIC -> "المنهج الكامل (١١ مرحلة)"
        AppLanguage.HINDI -> "संपूर्ण 11-चरणीय इस्लामी पाठ्यक्रम"
        AppLanguage.ENGLISH -> "Complete 11-Stage Learning Roadmap"
    }
    val statsLine = when (language) {
        AppLanguage.URDU -> "$completedCurriculumCount / $totalCurriculumCount اسباق • ${progress.totalQuizzesPassed} کوئز • ${progress.memorizedSurahNumbers.size}/114 سورتیں"
        AppLanguage.ARABIC -> "$completedCurriculumCount / $totalCurriculumCount درساً • ${progress.totalQuizzesPassed} اختباراً • ${progress.memorizedSurahNumbers.size}/114 سورة"
        AppLanguage.HINDI -> "$completedCurriculumCount / $totalCurriculumCount पाठ • ${progress.totalQuizzesPassed} क्विज़ • ${progress.memorizedSurahNumbers.size}/114 सूरह"
        AppLanguage.ENGLISH -> "$completedCurriculumCount / $totalCurriculumCount Lessons • ${progress.totalQuizzesPassed} Quizzes Passed • ${progress.memorizedSurahNumbers.size}/114 Surahs"
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("academy_roadmap_list")
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("academy_progress_dashboard")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = dashboardTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = statsLine,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            val streakLabel = when (language) {
                                AppLanguage.URDU -> "${progress.currentStreakDays} دن تسلسل"
                                AppLanguage.ARABIC -> "${progress.currentStreakDays} يوم مواظبة"
                                AppLanguage.HINDI -> "${progress.currentStreakDays} दिन स्ट्रीक"
                                AppLanguage.ENGLISH -> "${progress.currentStreakDays}d Streak"
                            }
                            Text(
                                text = streakLabel,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val progressRatio = (completedCurriculumCount.toFloat() / totalCurriculumCount.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val continueTitle = remember(progress.lastActiveLessonId, language) {
                        val lesson = QuranLearningRepository.getLessonById(progress.lastActiveLessonId)
                        if (lesson != null) {
                            "${com.example.localization.AppLocalization.get("learning_stage_prefix", language)} ${lesson.stageNumber} · ${lesson.titleFor(language)}"
                        } else {
                            progress.lastActiveLessonTitle
                        }
                    }

                    Surface(
                        onClick = onContinueLearning,
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .testTag("continue_learning_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircleFilled,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$continuePrefix$continueTitle",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text(searchHint, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = com.example.localization.AppLocalization.get("clear_search", language)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("learning_search_input")
            )
        }

        if (searchQuery.isNotBlank()) {
            if (searchResults.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "کوئی متعلقہ سبق نہیں ملا۔"
                                AppLanguage.ARABIC -> "لم يتم العثور على دروس مطابقة."
                                AppLanguage.HINDI -> "कोई संबंधित सबक़ नहीं मिला।"
                                AppLanguage.ENGLISH -> "No matching lessons found."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(searchResults, key = { it.id }) { lesson ->
                    Card(
                        onClick = { onOpenSearchResult(lesson.stageNumber, lesson.id) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_result_${lesson.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "${com.example.localization.AppLocalization.get("learning_stage_prefix", language)} ${lesson.stageNumber} · ${lesson.titleFor(language)}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = lesson.summaryFor(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else {
            item {
                Text(
                    text = roadmapHeading,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(QuranLearningRepository.allStages, key = { it.id }) { stage ->
                val doneCount = stage.lessons.count { progress.completedLessonIds.contains(it.id) }
                val totalCount = stage.lessons.size
                val isStageComplete = totalCount > 0 && doneCount == totalCount

                Card(
                    onClick = { onOpenStage(stage.stageNumber) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = if (isStageComplete) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stage_card_${stage.stageNumber}")
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
                                .background(
                                    if (isStageComplete) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.primaryContainer
                                )
                        ) {
                            Text(
                                text = "${stage.stageNumber}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isStageComplete) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stage.titleFor(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = "$doneCount/$totalCount",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stage.subtitleFor(language),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stage.overviewFor(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StageDetailContent(
    stage: AcademyStage,
    language: AppLanguage,
    progress: StudentAcademyProgress,
    stage7ToolTab: Int,
    onSelectStage7ToolTab: (Int) -> Unit,
    selectedQaidaIndex: Int,
    onSelectQaidaIndex: (Int) -> Unit,
    selectedWritingLetter: Pair<String, String>,
    onSelectWritingLetter: (Pair<String, String>) -> Unit,
    stage8ToolTab: Int,
    onSelectStage8ToolTab: (Int) -> Unit,
    selectedWbwSurahFilter: Int,
    onSelectWbwSurahFilter: (Int) -> Unit,
    selectedQuizStageFilter: Int,
    onSelectQuizStageFilter: (Int) -> Unit,
    onOpenLesson: (String) -> Unit,
    onProgressChanged: () -> Unit
) {
    val context = LocalContext.current
    val lessonsHeading = when (language) {
        AppLanguage.URDU -> "اس مرحلے کے اسباق"
        AppLanguage.ARABIC -> "دروس هذه المرحلة"
        AppLanguage.HINDI -> "इस चरण के पाठ"
        AppLanguage.ENGLISH -> "Stage Lessons"
    }
    val completedLabel = when (language) {
        AppLanguage.URDU -> "مکمل ✓"
        AppLanguage.ARABIC -> "مكتمل ✓"
        AppLanguage.HINDI -> "पूर्ण ✓"
        AppLanguage.ENGLISH -> "Completed ✓"
    }
    val notCompletedLabel = when (language) {
        AppLanguage.URDU -> "نامکمل"
        AppLanguage.ARABIC -> "غير مكتمل"
        AppLanguage.HINDI -> "बाक़ी"
        AppLanguage.ENGLISH -> "Not Completed"
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("stage_detail_${stage.stageNumber}")
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stage.titleFor(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stage.overviewFor(language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    )
                }
            }
        }

        item {
            Text(
                text = lessonsHeading,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(stage.lessons, key = { it.id }) { lesson ->
            val isDone = progress.completedLessonIds.contains(lesson.id)
            Card(
                onClick = { onOpenLesson(lesson.id) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lesson_card_${lesson.id}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDone) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                    ) {
                        Text(
                            text = "${lesson.lessonNumber}",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (isDone) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lesson.titleFor(language),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = lesson.summaryFor(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isDone) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = if (isDone) completedLabel else notCompletedLabel,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDone) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        when (stage.interactiveToolType) {
            StageToolType.QAIDA_TAJWEED_WRITING -> stage7QaidaWorkspace(
                context = context,
                language = language,
                progress = progress,
                activeToolTab = stage7ToolTab,
                onSelectToolTab = onSelectStage7ToolTab,
                selectedQaidaIndex = selectedQaidaIndex,
                onSelectQaidaIndex = onSelectQaidaIndex,
                selectedWritingLetter = selectedWritingLetter,
                onSelectWritingLetter = onSelectWritingLetter,
                onProgressChanged = onProgressChanged
            )

            StageToolType.QURAN_UNDERSTANDING -> stage8QuranUnderstandingWorkspace(
                context = context,
                language = language,
                progress = progress,
                activeToolTab = stage8ToolTab,
                onSelectToolTab = onSelectStage8ToolTab,
                selectedWbwSurahFilter = selectedWbwSurahFilter,
                onSelectWbwSurahFilter = onSelectWbwSurahFilter,
                onProgressChanged = onProgressChanged
            )

            StageToolType.HIFZ_TRACKER -> stage9HifzWorkspace(
                context = context,
                language = language,
                progress = progress,
                onProgressChanged = onProgressChanged
            )

            StageToolType.ASSESSMENTS_HUB -> stage11AssessmentsWorkspace(
                context = context,
                language = language,
                selectedQuizStageFilter = selectedQuizStageFilter,
                onSelectQuizStageFilter = onSelectQuizStageFilter,
                onProgressChanged = onProgressChanged
            )

            StageToolType.NONE -> Unit
        }
    }
}

@Composable
private fun LessonDetailContent(
    stage: AcademyStage,
    lesson: AcademyLesson,
    language: AppLanguage,
    isCompleted: Boolean,
    onLessonOpened: () -> Unit,
    onMarkComplete: () -> Unit,
    onQuizPassed: (String) -> Unit,
    onNavigateLesson: (String) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(lesson.id) {
        onLessonOpened()
    }

    val currentIndex = stage.lessons.indexOfFirst { it.id == lesson.id }
    val prevLesson = if (currentIndex > 0) stage.lessons[currentIndex - 1] else null
    val nextLesson = if (currentIndex in 0 until stage.lessons.lastIndex) stage.lessons[currentIndex + 1] else null
    val linkedQuiz = lesson.quizId?.let { qid ->
        QuranLearningRepository.academyQuizzes.firstOrNull { it.id == qid }
    }

    val examplesHeading = when (language) {
        AppLanguage.URDU -> "عملی مثالیں"
        AppLanguage.ARABIC -> "أمثلة تطبيقية"
        AppLanguage.HINDI -> "व्यावहारिक उदाहरण"
        AppLanguage.ENGLISH -> "Key Examples"
    }
    val fiqhHeading = when (language) {
        AppLanguage.URDU -> "فقہی مکاتبِ فکر میں معتبر اختلاف"
        AppLanguage.ARABIC -> "مسائل الخلاف الفقهي المعتبر"
        AppLanguage.HINDI -> "मान्य फ़िक़्ही मतभेद (संतुलित जानकारी)"
        AppLanguage.ENGLISH -> "Recognized Scholarly Differences"
    }
    val taskHeading = when (language) {
        AppLanguage.URDU -> "عملی مشق (آج کا عمل)"
        AppLanguage.ARABIC -> "التطبيق العملي"
        AppLanguage.HINDI -> "व्यावहारिक कार्य"
        AppLanguage.ENGLISH -> "Practical Action Step"
    }
    val markBtnLabel = if (isCompleted) {
        when (language) {
            AppLanguage.URDU -> "سبق مکمل ہو گیا ✓"
            AppLanguage.ARABIC -> "تم إكمال الدرس ✓"
            AppLanguage.HINDI -> "पाठ पूर्ण ✓"
            AppLanguage.ENGLISH -> "Lesson Completed ✓"
        }
    } else {
        when (language) {
            AppLanguage.URDU -> "سبق مکمل قرار دیں"
            AppLanguage.ARABIC -> "تحديد الدرس كمكتمل"
            AppLanguage.HINDI -> "पाठ पूर्ण चिह्नित करें"
            AppLanguage.ENGLISH -> "Mark Lesson Complete"
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("lesson_detail_${lesson.id}")
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "${stage.titleFor(language)} · #${lesson.lessonNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lesson.titleFor(language),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = lesson.summaryFor(language),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = lesson.bodyFor(language),
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 25.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (lesson.recitations.isNotEmpty()) {
            items(lesson.recitations.size) { idx ->
                val step = lesson.recitations[idx]
                PrayerStepRecitationCard(
                    step = step,
                    index = idx + 1,
                    language = language,
                    onPlayArabic = {
                        QuranAudioManager.speakArabic(context, step.arabic, step.transliteration)
                    }
                )
            }
        }

        val examples = lesson.examplesFor(language)
        if (examples.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = examplesHeading,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        examples.forEach { eg ->
                            Text(
                                text = "• $eg",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        val fiqhNote = lesson.fiqhDifferencesFor(language)
        if (fiqhNote.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = fiqhHeading,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = fiqhNote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        val taskNote = lesson.practicalTaskFor(language)
        if (taskNote.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = taskHeading,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = taskNote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

        if (lesson.references.isNotEmpty()) {
            item {
                IslamicReferenceSection(references = lesson.references, language = language)
            }
        }

        if (linkedQuiz != null) {
            item {
                InteractiveQuizCard(
                    quiz = linkedQuiz,
                    language = language,
                    onQuizPassed = { onQuizPassed(linkedQuiz.id) }
                )
            }
        }

        item {
            Button(
                onClick = onMarkComplete,
                shape = RoundedCornerShape(14.dp),
                colors = if (isCompleted) {
                    ButtonDefaults.filledTonalButtonColors()
                } else {
                    ButtonDefaults.buttonColors()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .testTag("mark_lesson_complete_btn")
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Done,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(markBtnLabel, fontWeight = FontWeight.Bold)
            }
        }

        if (prevLesson != null || nextLesson != null) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (prevLesson != null) {
                        OutlinedButton(
                            onClick = { onNavigateLesson(prevLesson.id) },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(prevLesson.titleFor(language), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    if (nextLesson != null) {
                        Button(
                            onClick = { onNavigateLesson(nextLesson.id) },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                        ) {
                            Text(nextLesson.titleFor(language), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
