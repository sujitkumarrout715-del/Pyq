package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    data class SubjectDetail(val subjectId: String) : Screen()
    data class PaperView(val paperId: String, val openSolutionForQuestionId: String? = null) : Screen()
    object Bookmarks : Screen()
    object Downloads : Screen()
    object Stats : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = PyqRepository(db.pyqDao())

    // UI Screen navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Navigation history list for deep backstack support!
    private val backstack = mutableListOf<Screen>()

    // Global items fetched from flows
    val subjects: StateFlow<List<Subject>> = repository.allSubjects.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val papers: StateFlow<List<Paper>> = repository.allPapers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val downloadedPapers: StateFlow<List<Paper>> = repository.downloadedPapers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookmarks: StateFlow<List<Question>> = repository.bookmarkedQuestions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Active Subject Papers and Chapters
    private val _activeSubjectId = MutableStateFlow<String?>(null)
    val activeSubjectId: StateFlow<String?> = _activeSubjectId.asStateFlow()

    val activeSubjectPapers: StateFlow<List<Paper>> = _activeSubjectId
        .filterNotNull()
        .flatMapLatest { repository.getPapersBySubject(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeSubjectChapters: StateFlow<List<String>> = _activeSubjectId
        .filterNotNull()
        .flatMapLatest { repository.getChaptersBySubject(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Paper Questions
    private val _activePaperId = MutableStateFlow<String?>(null)
    val activePaperId: StateFlow<String?> = _activePaperId.asStateFlow()

    val activePaperQuestions: StateFlow<List<Question>> = _activePaperId
        .filterNotNull()
        .flatMapLatest { repository.getQuestionsByPaper(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active search parameters
    val searchQuery = MutableStateFlow("")
    val searchSubject = MutableStateFlow("All")
    val searchYear = MutableStateFlow<Int?>(null)
    val searchChapter = MutableStateFlow("All")

    val searchResults: StateFlow<List<Question>> = combine(
        searchQuery,
        searchSubject,
        searchYear,
        searchChapter
    ) { query, textSubj, textYear, textChpt ->
        val subjId = if (textSubj == "All") null else textSubj.lowercase().replace(" English Litt.", "english")
        Quadruple(query, subjId, textYear, textChpt)
    }.flatMapLatest { (query, subjId, textYear, textChpt) ->
        repository.searchQuestions(
            query = query,
            subjectId = subjId,
            year = textYear,
            chapter = textChpt
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active download states - maps paperId -> progress (0.0 to 1.0)
    private val _downloadProgresses = MutableStateFlow<Map<String, Float>>(emptyMap())
    val downloadProgresses: StateFlow<Map<String, Float>> = _downloadProgresses.asStateFlow()

    // Dark Mode state (persists in session)
    val isDarkMode = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.seedIfNeeded()
        }
    }

    // Navigation controller helpers
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            backstack.add(_currentScreen.value)
            _currentScreen.value = screen
            // Synchronize active screen IDs
            when (screen) {
                is Screen.SubjectDetail -> {
                    _activeSubjectId.value = screen.subjectId
                }
                is Screen.PaperView -> {
                    _activePaperId.value = screen.paperId
                }
                else -> {}
            }
        }
    }

    fun navigateBack(): Boolean {
        if (backstack.isNotEmpty()) {
            val prev = backstack.removeAt(backstack.size - 1)
            _currentScreen.value = prev
            when (prev) {
                is Screen.SubjectDetail -> {
                    _activeSubjectId.value = prev.subjectId
                }
                is Screen.PaperView -> {
                    _activePaperId.value = prev.paperId
                }
                else -> {}
            }
            return true
        }
        return false
    }

    // Bookmarking Actions
    fun toggleBookmark(question: Question, isBookmarked: Boolean, notes: String? = null) {
        viewModelScope.launch {
            repository.updateBookmark(question.id, isBookmarked, notes ?: question.userNotes)
        }
    }

    // Set Completion Actions
    fun toggleQuestionCompleted(question: Question, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateQuestionCompletion(question.id, isCompleted, question.paperId)
        }
    }

    // Simulate Offline Download with live delays (satisfies PRD for beautiful downloads)
    fun downloadPaper(paper: Paper) {
        if (paper.isDownloaded || _downloadProgresses.value.containsKey(paper.id)) return

        viewModelScope.launch {
            _downloadProgresses.update { it + (paper.id to 0f) }
            repository.updatePaperDownload(paper.id, isDownloaded = false, progress = 0.0f)
            
            for (step in 1..10) {
                delay(120) // Simulated packet download
                val progress = step / 10f
                _downloadProgresses.update { it + (paper.id to progress) }
                repository.updatePaperDownload(paper.id, isDownloaded = step == 10, progress = progress)
            }
            
            _downloadProgresses.update { it - paper.id }
        }
    }

    // Uninstall downloaded paper to free cache storage
    fun deletePaperDownload(paper: Paper) {
        viewModelScope.launch {
            repository.updatePaperDownload(paper.id, isDownloaded = false, progress = 0f)
        }
    }

    // Clear search values
    fun resetSearchFilters() {
        searchQuery.value = ""
        searchSubject.value = "All"
        searchYear.value = null
        searchChapter.value = "All"
    }
}

// Simple data wrapper helper
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
