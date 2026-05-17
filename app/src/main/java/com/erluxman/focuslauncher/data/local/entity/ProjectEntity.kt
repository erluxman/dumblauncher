package com.erluxman.focuslauncher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val progress: Float = 0f, // 0.0 to 1.0
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
