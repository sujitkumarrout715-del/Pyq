package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey val id: String,
    val name: String,
    val icon: String,
    val colorHex: String,
    val description: String
)

@Entity(tableName = "papers")
data class Paper(
    @PrimaryKey val id: String,
    val subjectId: String,
    val year: Int,
    val title: String,
    val isDownloaded: Boolean = false,
    val downloadProgress: Float = 0f,
    val isCompleted: Boolean = false,
    val totalQuestions: Int = 10,
    val solvedQuestions: Int = 0
)

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey val id: String,
    val paperId: String,
    val subjectId: String,
    val year: Int,
    val chapter: String,
    val questionNumber: String,
    val marks: Int,
    val questionText: String,
    val hint: String,
    val solutionText: String, // Step-by-step solutions
    val isBookmarked: Boolean = false,
    val userNotes: String? = null,
    val isCompleted: Boolean = false
)
