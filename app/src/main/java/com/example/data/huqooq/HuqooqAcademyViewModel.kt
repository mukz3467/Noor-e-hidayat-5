package com.example.data.huqooq

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface HuqooqRoute {
    object Home : HuqooqRoute
    data class Department(val department: HuqooqDepartment) : HuqooqRoute
    data class LessonDetail(val lessonId: String) : HuqooqRoute
    object InheritanceCalculator : HuqooqRoute
    object Bookmarks : HuqooqRoute
}

class HuqooqAcademyViewModel(
    private val appContext: Context,
    private val repository: HuqooqAcademyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HuqooqAcademyUiState>(HuqooqAcademyUiState.Loading)
    val uiState: StateFlow<HuqooqAcademyUiState> = _uiState.asStateFlow()

    private val _routeStack = MutableStateFlow<List<HuqooqRoute>>(listOf(HuqooqRoute.Home))
    val currentRoute: StateFlow<HuqooqRoute> = _routeStack
        .map { it.lastOrNull() ?: HuqooqRoute.Home }
        .stateIn(viewModelScope, SharingStarted.Eagerly, HuqooqRoute.Home)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGroupFilter = MutableStateFlow("ALL")
    val selectedGroupFilter: StateFlow<String> = _selectedGroupFilter.asStateFlow()

    val progressMap: StateFlow<Map<String, HuqooqProgressEntity>> =
        repository.observeAllProgress()
            .map { list -> list.associateBy { it.itemId } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val bookmarksList: StateFlow<List<HuqooqBookmarkEntity>> =
        repository.observeBookmarks()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val bookmarksSet: StateFlow<Set<String>> =
        bookmarksList
            .map { list -> list.map { it.itemId }.toSet() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    val quizResultsMap: StateFlow<Map<String, HuqooqQuizResultEntity>> =
        repository.observeQuizResults()
            .map { list -> list.associateBy { it.itemId } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val metaState: StateFlow<HuqooqAcademyMetaEntity?> =
        repository.observeMeta()
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            _uiState.value = HuqooqAcademyUiState.Loading
            try {
                val ready = withContext(Dispatchers.IO) {
                    HuqooqContentLoader.loadAll(appContext)
                }
                _uiState.value = ready
            } catch (e: Exception) {
                _uiState.value = HuqooqAcademyUiState.Error(e.message ?: "Failed to load Huqooq-ul-Ibad Academy")
            }
        }
    }

    fun openDepartment(department: HuqooqDepartment) {
        _selectedGroupFilter.value = "ALL"
        _routeStack.value = _routeStack.value + HuqooqRoute.Department(department)
    }

    fun openLesson(lessonId: String, language: AppLanguage) {
        _routeStack.value = _routeStack.value + HuqooqRoute.LessonDetail(lessonId)
        val ready = _uiState.value as? HuqooqAcademyUiState.Ready ?: return
        val lesson = ready.lessons.firstOrNull { it.id == lessonId } ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.markLessonOpened(lesson, lesson.title.get(language))
        }
    }

    fun openCalculator() {
        _routeStack.value = _routeStack.value + HuqooqRoute.InheritanceCalculator
    }

    fun openBookmarks() {
        _routeStack.value = _routeStack.value + HuqooqRoute.Bookmarks
    }

    /**
     * Pops internal Academy route. Returns true if popped inside Academy, false if at Home root.
     */
    fun navigateBack(): Boolean {
        val stack = _routeStack.value
        if (stack.size > 1) {
            _routeStack.value = stack.dropLast(1)
            return true
        }
        return false
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectGroupFilter(groupCode: String) {
        _selectedGroupFilter.value = groupCode
    }

    fun search(query: String, language: AppLanguage): List<HuqooqSearchHit> {
        val ready = _uiState.value as? HuqooqAcademyUiState.Ready ?: return emptyList()
        return HuqooqContentLoader.searchAll(ready, query, language)
    }

    fun resolveQuranCitations(specs: List<HuqooqQuranRefSpec>): List<HuqooqQuranCitation> {
        return HuqooqContentLoader.resolveQuranCitations(appContext, specs)
    }

    fun toggleBookmark(lesson: HuqooqLesson, language: AppLanguage) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleBookmark(lesson.id, lesson.department.id, lesson.title.get(language))
        }
    }

    fun toggleCompleted(lesson: HuqooqLesson) {
        val currentlyCompleted = progressMap.value[lesson.id]?.isCompleted == true
        viewModelScope.launch(Dispatchers.IO) {
            repository.setLessonCompleted(lesson.id, lesson.department.id, !currentlyCompleted)
        }
    }

    fun saveQuizScore(lesson: HuqooqLesson, score: Int, total: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveQuizResult(lesson.id, lesson.department.id, score, total)
        }
    }

    fun adjustTextScale(delta: Float) {
        val current = metaState.value?.lessonTextScale ?: 1.0f
        val next = (current + delta).coerceIn(0.85f, 1.50f)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateLessonTextScale(next)
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val dao = HuqooqAcademyDatabase.getInstance(context).huqooqDao()
            return HuqooqAcademyViewModel(
                appContext = context.applicationContext,
                repository = HuqooqAcademyRepository(dao)
            ) as T
        }
    }
}
