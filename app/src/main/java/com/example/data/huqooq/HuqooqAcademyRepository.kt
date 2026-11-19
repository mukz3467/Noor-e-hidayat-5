package com.example.data.huqooq

import kotlinx.coroutines.flow.Flow

class HuqooqAcademyRepository(private val dao: HuqooqDao) {

    fun observeAllProgress(): Flow<List<HuqooqProgressEntity>> = dao.observeAllProgress()
    fun observeBookmarks(): Flow<List<HuqooqBookmarkEntity>> = dao.observeBookmarks()
    fun observeQuizResults(): Flow<List<HuqooqQuizResultEntity>> = dao.observeQuizResults()
    fun observeMeta(): Flow<HuqooqAcademyMetaEntity?> = dao.observeMeta()

    suspend fun markLessonOpened(lesson: HuqooqLesson, localizedTitle: String) {
        val now = System.currentTimeMillis()
        val existingProgress = dao.getProgressById(lesson.id)
        dao.upsertProgress(
            HuqooqProgressEntity(
                itemId = lesson.id,
                departmentId = lesson.department.id,
                isCompleted = existingProgress?.isCompleted ?: false,
                lastOpenedAt = now,
                completedAt = existingProgress?.completedAt
            )
        )
        val existingMeta = dao.getMeta() ?: HuqooqAcademyMetaEntity()
        dao.upsertMeta(
            existingMeta.copy(
                lastViewedLessonId = lesson.id,
                lastViewedDepartmentId = lesson.department.id,
                lastViewedTitle = localizedTitle,
                updatedAt = now
            )
        )
    }

    suspend fun setLessonCompleted(itemId: String, departmentId: String, completed: Boolean) {
        val now = System.currentTimeMillis()
        dao.upsertProgress(
            HuqooqProgressEntity(
                itemId = itemId,
                departmentId = departmentId,
                isCompleted = completed,
                lastOpenedAt = now,
                completedAt = if (completed) now else null
            )
        )
    }

    suspend fun toggleBookmark(itemId: String, departmentId: String, titleSnapshot: String): Boolean {
        val existing = dao.getBookmarkById(itemId)
        return if (existing != null) {
            dao.deleteBookmarkById(itemId)
            false
        } else {
            dao.insertBookmark(
                HuqooqBookmarkEntity(
                    itemId = itemId,
                    departmentId = departmentId,
                    titleSnapshot = titleSnapshot
                )
            )
            true
        }
    }

    suspend fun saveQuizResult(itemId: String, departmentId: String, score: Int, totalQuestions: Int) {
        val existing = dao.getQuizResultById(itemId)
        val best = maxOf(existing?.bestScore ?: 0, score)
        dao.upsertQuizResult(
            HuqooqQuizResultEntity(
                itemId = itemId,
                departmentId = departmentId,
                score = score,
                totalQuestions = totalQuestions,
                bestScore = best,
                attemptedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateLessonTextScale(newScale: Float) {
        val clamped = newScale.coerceIn(0.85f, 1.50f)
        val existingMeta = dao.getMeta() ?: HuqooqAcademyMetaEntity()
        dao.upsertMeta(
            existingMeta.copy(
                lessonTextScale = clamped,
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}
