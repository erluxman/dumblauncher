package com.erluxman.focuslauncher.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erluxman.focuslauncher.data.local.dao.JournalDao
import com.erluxman.focuslauncher.data.local.dao.ProjectDao
import com.erluxman.focuslauncher.data.local.dao.TodoDao
import com.erluxman.focuslauncher.data.local.entity.JournalEntryEntity
import com.erluxman.focuslauncher.data.local.entity.ProjectEntity
import com.erluxman.focuslauncher.data.local.entity.TodoEntity

@Database(
    entities = [TodoEntity::class, ProjectEntity::class, JournalEntryEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun projectDao(): ProjectDao
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focus_launcher_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
