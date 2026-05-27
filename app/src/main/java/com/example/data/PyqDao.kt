package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PyqDao {
    // Subject queries
    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<Subject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<Subject>)

    // Paper queries
    @Query("SELECT * FROM papers WHERE subjectId = :subjectId ORDER BY year DESC")
    fun getPapersBySubject(subjectId: String): Flow<List<Paper>>

    @Query("SELECT * FROM papers ORDER BY year DESC")
    fun getAllPapers(): Flow<List<Paper>>

    @Query("SELECT * FROM papers WHERE isDownloaded = 1")
    fun getDownloadedPapers(): Flow<List<Paper>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPapers(papers: List<Paper>)

    @Query("UPDATE papers SET isDownloaded = :isDownloaded, downloadProgress = :progress WHERE id = :paperId")
    suspend fun updatePaperDownloadStatus(paperId: String, isDownloaded: Boolean, progress: Float)

    @Query("UPDATE papers SET solvedQuestions = :solvedCount, isCompleted = :isCompleted WHERE id = :paperId")
    suspend fun updatePaperProgress(paperId: String, solvedCount: Int, isCompleted: Boolean)

    // Question queries
    @Query("SELECT * FROM questions WHERE paperId = :paperId")
    fun getQuestionsByPaper(paperId: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE isBookmarked = 1")
    fun getBookmarkedQuestions(): Flow<List<Question>>

    @Query("SELECT DISTINCT chapter FROM questions WHERE subjectId = :subjectId")
    fun getChaptersBySubject(subjectId: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("UPDATE questions SET isBookmarked = :isBookmarked, userNotes = :userNotes WHERE id = :questionId")
    suspend fun updateQuestionBookmark(questionId: String, isBookmarked: Boolean, userNotes: String?)

    @Query("UPDATE questions SET isCompleted = :isCompleted WHERE id = :questionId")
    suspend fun updateQuestionCompletionState(questionId: String, isCompleted: Boolean)

    // Global Search: Subject, Year, Chapter, or Text query mapping
    @Query("""
        SELECT * FROM questions 
        WHERE (:subjectId IS NULL OR subjectId = :subjectId)
          AND (:year IS NULL OR year = :year)
          AND (:chapter IS NULL OR chapter = :chapter)
          AND (questionText LIKE '%' || :query || '%' OR chapter LIKE '%' || :query || '%' OR solutionText LIKE '%' || :query || '%')
    """)
    fun searchQuestions(
        query: String = "",
        subjectId: String? = null,
        year: Int? = null,
        chapter: String? = null
    ): Flow<List<Question>>
}
