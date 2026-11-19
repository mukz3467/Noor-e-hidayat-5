package com.example.data.business

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {
    @Query("SELECT * FROM business_progress ORDER BY lastOpenedAt DESC")
    fun observeAllProgress(): Flow<List<BusinessProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(entry: BusinessProgressEntity)

    @Query("SELECT * FROM business_progress WHERE itemId = :itemId LIMIT 1")
    suspend fun getProgressById(itemId: String): BusinessProgressEntity?

    @Query("SELECT * FROM business_quiz_results ORDER BY attemptedAt DESC")
    fun observeAllQuizResults(): Flow<List<BusinessQuizResultEntity>>

    @Query("SELECT * FROM business_quiz_results WHERE itemId = :itemId LIMIT 1")
    suspend fun getQuizResultById(itemId: String): BusinessQuizResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuizResult(result: BusinessQuizResultEntity)

    @Query("SELECT * FROM business_bookmarks ORDER BY createdAt DESC")
    fun observeBookmarks(): Flow<List<BusinessBookmarkEntity>>

    @Query("SELECT * FROM business_bookmarks WHERE itemId = :itemId LIMIT 1")
    suspend fun getBookmarkById(itemId: String): BusinessBookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BusinessBookmarkEntity)

    @Query("DELETE FROM business_bookmarks WHERE itemId = :itemId")
    suspend fun deleteBookmarkById(itemId: String)

    @Query("SELECT * FROM business_notes ORDER BY updatedAt DESC")
    fun observeNotes(): Flow<List<BusinessNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: BusinessNoteEntity)

    @Query("DELETE FROM business_notes WHERE itemId = :itemId")
    suspend fun deleteNoteById(itemId: String)

    @Query("SELECT * FROM business_startup_project ORDER BY stepNumber ASC")
    fun observeStartupProjectSteps(): Flow<List<BusinessStartupProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStartupProjectStep(step: BusinessStartupProjectEntity)
}
