package com.erluxman.focuslauncher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val behaviorState: String,
    val screenMinutes: Int,
    val createdAt: Long = System.currentTimeMillis()
)
