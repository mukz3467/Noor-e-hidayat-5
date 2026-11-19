package com.example.data.salah

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
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "missed_salah_entries")
data class MissedSalahEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val prayerType: String,
    val missedDateKey: String,
    val reasonCategory: String,
    val optionalNote: String = "",
    val status: String,
    val completedDateKey: String? = null,
    val createdAtMs: Long = System.currentTimeMillis(),
    val updatedAtMs: Long = System.currentTimeMillis()
) {
    fun prayerEnum(): SalahPrayerType =
        runCatching { SalahPrayerType.valueOf(prayerType) }.getOrDefault(SalahPrayerType.FAJR)

    fun reasonEnum(): MissedReasonCategory =
        runCatching { MissedReasonCategory.valueOf(reasonCategory) }
            .getOrDefault(MissedReasonCategory.SLEEP_OR_FORGETFULNESS)

    fun statusEnum(): QadaEntryStatus =
        runCatching { QadaEntryStatus.valueOf(status) }.getOrDefault(QadaEntryStatus.PENDING)
}

@Dao
interface MissedSalahDao {
    @Query("SELECT * FROM missed_salah_entries ORDER BY missedDateKey DESC, updatedAtMs DESC")
    fun observeAllEntries(): Flow<List<MissedSalahEntryEntity>>

    @Query("SELECT * FROM missed_salah_entries ORDER BY missedDateKey DESC, updatedAtMs DESC")
    suspend fun getAllEntriesOnce(): List<MissedSalahEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: MissedSalahEntryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<MissedSalahEntryEntity>)

    @Update
    suspend fun updateEntry(entry: MissedSalahEntryEntity)

    @Query("DELETE FROM missed_salah_entries WHERE id = :entryId")
    suspend fun deleteById(entryId: Long)

    @Query("DELETE FROM missed_salah_entries")
    suspend fun deleteAllEntries()
}

@Database(
    entities = [MissedSalahEntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MissedSalahDatabase : RoomDatabase() {
    abstract fun missedSalahDao(): MissedSalahDao

    companion object {
        @Volatile
        private var INSTANCE: MissedSalahDatabase? = null

        fun getInstance(context: Context): MissedSalahDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MissedSalahDatabase::class.java,
                    "missed_salah_private.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
