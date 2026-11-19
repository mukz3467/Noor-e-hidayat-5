package com.example.data.huqooq

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "huqooq_progress")
data class HuqooqProgressEntity(
    @PrimaryKey val itemId: String,
    val departmentId: String,
    val isCompleted: Boolean = false,
    val lastOpenedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity(tableName = "huqooq_bookmarks")
data class HuqooqBookmarkEntity(
    @PrimaryKey val itemId: String,
    val departmentId: String,
    val titleSnapshot: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "huqooq_quiz_results")
data class HuqooqQuizResultEntity(
    @PrimaryKey val itemId: String,
    val departmentId: String,
    val score: Int,
    val totalQuestions: Int,
    val bestScore: Int,
    val attemptedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "huqooq_academy_meta")
data class HuqooqAcademyMetaEntity(
    @PrimaryKey val id: String = "singleton",
    val lastViewedLessonId: String? = null,
    val lastViewedDepartmentId: String? = null,
    val lastViewedTitle: String? = null,
    val lessonTextScale: Float = 1.0f,
    val updatedAt: Long = System.currentTimeMillis()
)

@Dao
interface HuqooqDao {
    @Query("SELECT * FROM huqooq_progress ORDER BY lastOpenedAt DESC")
    fun observeAllProgress(): Flow<List<HuqooqProgressEntity>>

    @Query("SELECT * FROM huqooq_progress WHERE itemId = :itemId LIMIT 1")
    suspend fun getProgressById(itemId: String): HuqooqProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(entry: HuqooqProgressEntity)

    @Query("SELECT * FROM huqooq_bookmarks ORDER BY createdAt DESC")
    fun observeBookmarks(): Flow<List<HuqooqBookmarkEntity>>

    @Query("SELECT * FROM huqooq_bookmarks WHERE itemId = :itemId LIMIT 1")
    suspend fun getBookmarkById(itemId: String): HuqooqBookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: HuqooqBookmarkEntity)

    @Query("DELETE FROM huqooq_bookmarks WHERE itemId = :itemId")
    suspend fun deleteBookmarkById(itemId: String)

    @Query("SELECT * FROM huqooq_quiz_results ORDER BY attemptedAt DESC")
    fun observeQuizResults(): Flow<List<HuqooqQuizResultEntity>>

    @Query("SELECT * FROM huqooq_quiz_results WHERE itemId = :itemId LIMIT 1")
    suspend fun getQuizResultById(itemId: String): HuqooqQuizResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuizResult(result: HuqooqQuizResultEntity)

    @Query("SELECT * FROM huqooq_academy_meta WHERE id = 'singleton' LIMIT 1")
    fun observeMeta(): Flow<HuqooqAcademyMetaEntity?>

    @Query("SELECT * FROM huqooq_academy_meta WHERE id = 'singleton' LIMIT 1")
    suspend fun getMeta(): HuqooqAcademyMetaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeta(meta: HuqooqAcademyMetaEntity)
}

@Database(
    entities = [
        HuqooqProgressEntity::class,
        HuqooqBookmarkEntity::class,
        HuqooqQuizResultEntity::class,
        HuqooqAcademyMetaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HuqooqAcademyDatabase : RoomDatabase() {
    abstract fun huqooqDao(): HuqooqDao

    companion object {
        @Volatile
        private var INSTANCE: HuqooqAcademyDatabase? = null

        fun getInstance(context: Context): HuqooqAcademyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HuqooqAcademyDatabase::class.java,
                    "noor_huqooq_academy.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
