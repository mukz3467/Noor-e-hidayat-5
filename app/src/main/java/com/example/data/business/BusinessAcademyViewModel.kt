package com.example.data.business

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

sealed interface BusinessDetailDestination {
    data class Lesson(val lessonId: String) : BusinessDetailDestination
    data class Contract(val contractId: String) : BusinessDetailDestination
    data class Sector(val sectorId: String) : BusinessDetailDestination
    data class Specialized(val topicId: String) : BusinessDetailDestination
}

class BusinessAcademyViewModel(
    private val appContext: Context,
    private val repository: BusinessAcademyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BusinessAcademyUiState>(BusinessAcademyUiState.Loading)
    val uiState: StateFlow<BusinessAcademyUiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0=Curriculum, 1=Contracts, 2=Sectors, 3=Specialized, 4=Project/Tools, 5=Saved/Cert
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedLevel = MutableStateFlow(0) // 0=All, 1=L1, 2=L2, 3=L3(Sectors), 4=L4
    val selectedLevel: StateFlow<Int> = _selectedLevel.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeDetail = MutableStateFlow<BusinessDetailDestination?>(null)
    val activeDetail: StateFlow<BusinessDetailDestination?> = _activeDetail.asStateFlow()

    private val _certificatePath = MutableStateFlow<String?>(null)
    val certificatePath: StateFlow<String?> = _certificatePath.asStateFlow()

    val progressMap: StateFlow<Map<String, BusinessProgressEntity>> =
        repository.observeAllProgress()
            .map { list -> list.associateBy { it.itemId } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val quizResultsMap: StateFlow<Map<String, BusinessQuizResultEntity>> =
        repository.observeAllQuizResults()
            .map { list -> list.associateBy { it.itemId } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val bookmarksList: StateFlow<List<BusinessBookmarkEntity>> =
        repository.observeBookmarks()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val bookmarksSet: StateFlow<Set<String>> =
        bookmarksList
            .map { list -> list.map { it.itemId }.toSet() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    val notesMap: StateFlow<Map<String, BusinessNoteEntity>> =
        repository.observeNotes()
            .map { list -> list.associateBy { it.itemId } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val startupStepsMap: StateFlow<Map<Int, BusinessStartupProjectEntity>> =
        repository.observeStartupProjectSteps()
            .map { list -> list.associateBy { it.stepNumber } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    init {
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            _uiState.value = BusinessAcademyUiState.Loading
            try {
                val ready = withContext(Dispatchers.IO) {
                    BusinessContentLoader.loadAll(appContext)
                }
                _uiState.value = ready
            } catch (e: Exception) {
                _uiState.value = BusinessAcademyUiState.Error(e.message ?: "Failed to load Business Academy content")
            }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun selectLevel(level: Int) {
        _selectedLevel.value = level
    }

    fun updateSearchQuery(q: String) {
        _searchQuery.value = q
    }

    fun openDetail(dest: BusinessDetailDestination) {
        _activeDetail.value = dest
        viewModelScope.launch(Dispatchers.IO) {
            when (dest) {
                is BusinessDetailDestination.Lesson -> repository.markOpened(dest.lessonId, "lesson")
                is BusinessDetailDestination.Contract -> repository.markOpened(dest.contractId, "contract")
                is BusinessDetailDestination.Sector -> repository.markOpened(dest.sectorId, "sector")
                is BusinessDetailDestination.Specialized -> repository.markOpened(dest.topicId, "specialized")
            }
        }
    }

    fun closeDetail() {
        _activeDetail.value = null
    }

    fun toggleCompleted(itemId: String, itemType: String) {
        val currentlyCompleted = progressMap.value[itemId]?.isCompleted == true
        viewModelScope.launch(Dispatchers.IO) {
            repository.setCompleted(itemId, itemType, !currentlyCompleted)
        }
    }

    fun saveQuizScore(itemId: String, itemType: String, score: Int, total: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveQuizResult(itemId, itemType, score, total)
        }
    }

    fun toggleBookmark(itemId: String, itemType: String, titleSnapshot: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleBookmark(itemId, itemType, titleSnapshot)
        }
    }

    fun saveNote(itemId: String, itemType: String, noteText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveNote(itemId, itemType, noteText)
        }
    }

    fun saveStartupStep(stepNumber: Int, answerText: String, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveStartupStep(stepNumber, answerText, isCompleted)
        }
    }

    fun resolveQuranCitations(specs: List<BusinessQuranRefSpec>): List<BusinessQuranCitation> {
        return BusinessContentLoader.resolveQuranCitations(appContext, specs)
    }

    fun search(query: String, language: AppLanguage): List<BusinessSearchHit> {
        val ready = _uiState.value as? BusinessAcademyUiState.Ready ?: return emptyList()
        return BusinessContentLoader.searchAll(ready, query, language)
    }

    fun generateCertificate(learnerName: String) {
        val ready = _uiState.value as? BusinessAcademyUiState.Ready ?: return
        val total = ready.lessons.size + ready.contracts.size + ready.sectors.size + ready.specializedTopics.size
        val completed = progressMap.value.values.count { it.isCompleted }
        val quizzesPassed = quizResultsMap.value.values.count { it.totalQuestions > 0 && it.bestScore * 2 >= it.totalQuestions }
        val stepsCompleted = startupStepsMap.value.values.count { it.isCompleted }
        viewModelScope.launch(Dispatchers.IO) {
            val file = repository.generateCompletionCertificatePdf(
                context = appContext,
                learnerName = learnerName,
                completedCount = completed,
                totalItemsCount = total,
                quizzesPassedCount = quizzesPassed,
                startupStepsCompleted = stepsCompleted
            )
            _certificatePath.value = file.absolutePath
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val dao = BusinessAcademyDatabase.getInstance(context).businessDao()
            return BusinessAcademyViewModel(
                appContext = context.applicationContext,
                repository = BusinessAcademyRepository(dao)
            ) as T
        }
    }
}
