package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PyqRepository(private val pyqDao: PyqDao) {

    val allSubjects: Flow<List<Subject>> = pyqDao.getAllSubjects()
    val allPapers: Flow<List<Paper>> = pyqDao.getAllPapers()
    val downloadedPapers: Flow<List<Paper>> = pyqDao.getDownloadedPapers()
    val bookmarkedQuestions: Flow<List<Question>> = pyqDao.getBookmarkedQuestions()

    fun getPapersBySubject(subjectId: String): Flow<List<Paper>> {
        return pyqDao.getPapersBySubject(subjectId)
    }

    fun getQuestionsByPaper(paperId: String): Flow<List<Question>> {
        return pyqDao.getQuestionsByPaper(paperId)
    }

    fun getChaptersBySubject(subjectId: String): Flow<List<String>> {
        return pyqDao.getChaptersBySubject(subjectId)
    }

    suspend fun updateBookmark(questionId: String, isBookmarked: Boolean, notes: String?) {
        pyqDao.updateQuestionBookmark(questionId, isBookmarked, notes)
    }

    suspend fun updateQuestionCompletion(questionId: String, isCompleted: Boolean, paperId: String) {
        pyqDao.updateQuestionCompletionState(questionId, isCompleted)
        // Adjust solved statistics for the parent paper in database
        val questions = pyqDao.getQuestionsByPaper(paperId).first()
        val solvedCount = questions.count { it.isCompleted || (it.id == questionId && isCompleted) }
        val isPaperCompleted = solvedCount == questions.size && questions.isNotEmpty()
        pyqDao.updatePaperProgress(paperId, solvedCount, isPaperCompleted)
    }

    suspend fun updatePaperDownload(paperId: String, isDownloaded: Boolean, progress: Float) {
        pyqDao.updatePaperDownloadStatus(paperId, isDownloaded, progress)
    }

    fun searchQuestions(
        query: String,
        subjectId: String? = null,
        year: Int? = null,
        chapter: String? = null
    ): Flow<List<Question>> {
        return pyqDao.searchQuestions(
            query = query,
            subjectId = if (subjectId == "All") null else subjectId,
            year = year,
            chapter = if (chapter == "All") null else chapter
        )
    }

    // Seed database if empty
    suspend fun seedIfNeeded() {
        val currentSubjects = pyqDao.getAllSubjects().first()
        if (currentSubjects.isEmpty()) {
            pyqDao.insertSubjects(SeedingData.getInitialSubjects())
            pyqDao.insertPapers(SeedingData.getInitialPapers())
            pyqDao.insertQuestions(SeedingData.getInitialQuestions())
        }
    }
}
