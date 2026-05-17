package com.erluxman.focuslauncher.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erluxman.focuslauncher.data.local.dao.ProjectDao
import com.erluxman.focuslauncher.data.local.dao.TodoDao
import com.erluxman.focuslauncher.data.local.entity.ProjectEntity
import com.erluxman.focuslauncher.data.local.entity.TodoEntity

@Database(entities = [TodoEntity::class, ProjectEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focus_launcher_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
