package com.erluxman.focuslauncher.data.repository

import com.erluxman.focuslauncher.data.local.dao.TodoDao
import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: Flow<List<TodoEntity>> = todoDao.getAllTodos()

    fun completedSince(sinceMs: Long): Flow<List<Long>> = todoDao.completedSince(sinceMs)

    suspend fun insert(todo: TodoEntity) {
        todoDao.insertTodo(todo)
    }

    suspend fun update(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }

    suspend fun delete(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }
}
