package com.erluxman.focuslauncher.data.repository

import com.erluxman.focuslauncher.data.local.dao.JournalDao
import com.erluxman.focuslauncher.data.local.entity.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val dao: JournalDao) {
    val recent: Flow<List<JournalEntryEntity>> = dao.recent()
    suspend fun insert(entry: JournalEntryEntity) = dao.insert(entry)
}
