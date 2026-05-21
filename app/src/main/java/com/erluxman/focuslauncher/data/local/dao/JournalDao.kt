package com.erluxman.focuslauncher.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.erluxman.focuslauncher.data.local.entity.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC LIMIT 200")
    fun recent(): Flow<List<JournalEntryEntity>>

    @Insert
    suspend fun insert(entry: JournalEntryEntity)
}
