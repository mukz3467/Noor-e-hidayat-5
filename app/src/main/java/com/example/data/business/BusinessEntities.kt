package com.example.data.business

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_progress")
data class BusinessProgressEntity(
    @PrimaryKey val itemId: String,
    val itemType: String, // "lesson", "contract", "sector", "specialized"
    val isCompleted: Boolean = false,
    val lastOpenedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "business_quiz_results")
data class BusinessQuizResultEntity(
    @PrimaryKey val itemId: String,
    val itemType: String,
    val score: Int,
    val totalQuestions: Int,
    val bestScore: Int,
    val attemptedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "business_bookmarks")
data class BusinessBookmarkEntity(
    @PrimaryKey val itemId: String,
    val itemType: String,
    val titleSnapshot: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "business_notes")
data class BusinessNoteEntity(
    @PrimaryKey val itemId: String,
    val itemType: String,
    val noteText: String,
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Module 24 — Complete 13-Step Halal Business Startup Project persisted locally offline.
 */
@Entity(tableName = "business_startup_project")
data class BusinessStartupProjectEntity(
    @PrimaryKey val stepNumber: Int, // 1..13
    val answerText: String,
    val isCompleted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
