package com.example.data.business

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        BusinessProgressEntity::class,
        BusinessQuizResultEntity::class,
        BusinessBookmarkEntity::class,
        BusinessNoteEntity::class,
        BusinessStartupProjectEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BusinessAcademyDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: BusinessAcademyDatabase? = null

        fun getInstance(context: Context): BusinessAcademyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BusinessAcademyDatabase::class.java,
                    "noor_business_academy.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
