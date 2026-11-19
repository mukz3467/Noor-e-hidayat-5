package com.example.ui.screens.prophets

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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppLanguage
import com.example.data.prophets.ProphetCategory
import com.example.data.prophets.ProphetRepository
import com.example.data.prophets.ProphetStory
import com.example.localization.AppLocalization
import com.example.ui.components.NoorTopBar
import com.example.ui.components.noorSelectableChipColors

@Composable
fun StoriesOfProphetsScreen(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val progress by ProphetRepository.progress.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        ProphetRepository.init(context)
    }

    var selectedProphetId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf<ProphetCategory?>(null) }
    var bookmarkedOnly by rememberSaveable { mutableStateOf(false) }

    val openStory = remember(selectedProphetId) {
        ProphetRepository.getStoryById(selectedProphetId)
    }

    if (openStory != null) {
        ProphetDetailScreen(
            story = openStory,
            language = language,
            isRead = progress.readIds.contains(openStory.id),
            isBookmarked = progress.bookmarkedIds.contains(openStory.id),
            isQuizCompleted = progress.quizCompletedIds.contains(openStory.id),
            summaryMode = progress.summaryModeEnabled,
            onToggleSummaryMode = { ProphetRepository.setSummaryMode(context, it) },
            onToggleRead = { ProphetRepository.toggleReadStatus(context, openStory.id) },
            onToggleBookmark = { ProphetRepository.toggleBookmark(context, openStory.id) },
            onQuizAnsweredCorrectly = { ProphetRepository.markQuizCompleted(context, openStory.id) },
            onNavigateToProphet = { nextId ->
                ProphetRepository.markOpened(context, nextId)
                selectedProphetId = nextId
            },
            onBack = { selectedProphetId = null }
        )
        return
    }

    val filteredStories = remember(
        searchQuery,
        selectedCategory,
        bookmarkedOnly,
        progress.bookmarkedIds,
        language
    ) {
        ProphetRepository.searchStories(
            query = searchQuery,
            categoryFilter = selectedCategory,
            bookmarkedOnly = bookmarkedOnly,
            language = language
        )
    }

    val totalCount = ProphetRepository.allStories.size
    val readCount = progress.readIds.count { id -> ProphetRepository.allStories.any { it.id == id } }
    val completionFraction = if (totalCount > 0) readCount.toFloat() / totalCount.toFloat() else 0f
    val lastReadStory = remember(progress.lastReadId) {
        ProphetRepository.getStoryById(progress.lastReadId)
    }

    Scaffold(
        topBar = {
            NoorTopBar(
                title = AppLocalization.get("stories_of_prophets", language),
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
                .testTag("prophets_list_container")
        ) {
            // Quran-First & Anti-Isra'iliyyat Verification Notice
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth().testTag("prophets_methodology_banner")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = when (language) {
                                AppLanguage.URDU -> "قرآنِ مجید میں نام بنام مذکور تمام 25 انبیائے کرامؑ — خالص قرآنی و صحیح حدیث حوالوں کے ساتھ مکمل آف لائن مطالعہ۔"
                                AppLanguage.ARABIC -> "الأنبياء الخمسة والعشرون المذكورون في القرآن الكريم — عرض موثق بالقرآن والصحيح دون إسرائيليات، متاح كاملاً دون اتصال."
                                AppLanguage.HINDI -> "क़ुरआन में नाम से वर्णित सभी 25 नबियों का जीवन-चरित्र — पूर्णतः ऑफ़लाइन एवं क़ुरआन व सहीह हदीस पर आधारित।"
                                AppLanguage.ENGLISH -> "All 25 Prophets Named in the Quran — Grounded strictly in Quranic revelation & Sahih Hadith, fully offline."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Reading Progress & Continue Reading Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "مطالعہ قصص الانبیاء"
                                    AppLanguage.ARABIC -> "تقدم قراءة قصص الأنبياء"
                                    AppLanguage.HINDI -> "नबियों की कथाएँ पठन प्रगति"
                                    AppLanguage.ENGLISH -> "Reading Progress"
                                },
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "$readCount / $totalCount",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.testTag("prophets_progress_count")
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { completionFraction },
                            modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp))
                        )
                        if (lastReadStory != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = { selectedProphetId = lastReadStory.id },
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("prophets_resume_card")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${AppLocalization.get("seerah_resume_last", language)}: ${lastReadStory.localizedName(language)}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = {
                        Text(
                            when (language) {
                                AppLanguage.URDU -> "نبی کا نام یا قرآنی حوالہ (مثلاً 19:56) تلاش کریں..."
                                AppLanguage.ARABIC -> "ابحث باسم النبي أو المرجع القرآني (مثل 19:56)..."
                                AppLanguage.HINDI -> "नबी का नाम या क़ुरआन संदर्भ (जैसे 19:56) खोजें..."
                                AppLanguage.ENGLISH -> "Search Prophet name or Quran ref (e.g. 19:56)..."
                            }
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("prophets_search_input")
                )
            }

            // Category & Bookmark Filters
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null && !bookmarkedOnly,
                        onClick = {
                            selectedCategory = null
                            bookmarkedOnly = false
                        },
                        label = {
                            Text(
                                when (language) {
                                    AppLanguage.URDU -> "تمام 25 انبیاء"
                                    AppLanguage.ARABIC -> "جميع الأنبياء (٢٥)"
                                    AppLanguage.HINDI -> "सभी 25 नबी"
                                    AppLanguage.ENGLISH -> "All 25 Prophets"
                                }
                            )
                        },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("prophets_cat_all")
                    )
                    FilterChip(
                        selected = bookmarkedOnly,
                        onClick = { bookmarkedOnly = !bookmarkedOnly },
                        label = { Text(AppLocalization.get("seerah_bookmarks_filter", language)) },
                        leadingIcon = { Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = noorSelectableChipColors(),
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("prophets_filter_bookmarks")
                    )
                    ProphetCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                            label = { Text(cat.label(language)) },
                            colors = noorSelectableChipColors(),
                            modifier = Modifier.minimumInteractiveComponentSize().testTag("prophets_cat_${cat.id}")
                        )
                    }
                }
            }

            if (filteredStories.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(24.dp)
                        ) {
                            Text(
                                text = when (language) {
                                    AppLanguage.URDU -> "کوئی متعلقہ نبی نہیں ملا"
                                    AppLanguage.ARABIC -> "لا توجد نتائج مطابقة"
                                    AppLanguage.HINDI -> "कोई संबंधित परिणाम नहीं मिला"
                                    AppLanguage.ENGLISH -> "No matching Prophets found"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            } else {
                items(filteredStories, key = { it.id }) { story ->
                    ProphetListCard(
                        story = story,
                        language = language,
                        isRead = progress.readIds.contains(story.id),
                        isBookmarked = progress.bookmarkedIds.contains(story.id),
                        onClick = {
                            ProphetRepository.markOpened(context, story.id)
                            selectedProphetId = story.id
                        },
                        onToggleBookmark = { ProphetRepository.toggleBookmark(context, story.id) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun ProphetListCard(
    story: ProphetStory,
    language: AppLanguage,
    isRead: Boolean,
    isBookmarked: Boolean,
    onClick: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("prophets_card_${story.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "${story.orderNumber}.",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = story.arabicName,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (isRead) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Read",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = story.localizedName(language),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = story.localizedTitle(language),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = story.summaryIntro[language] ?: story.summaryIntro[AppLanguage.ENGLISH].orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
