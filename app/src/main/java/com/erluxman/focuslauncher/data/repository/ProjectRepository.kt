package com.erluxman.focuslauncher.data.repository

import com.erluxman.focuslauncher.data.local.dao.ProjectDao
import com.erluxman.focuslauncher.data.local.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {
    val activeProjects: Flow<List<ProjectEntity>> = projectDao.getActiveProjects()

    suspend fun insert(project: ProjectEntity) {
        projectDao.insertProject(project)
    }

    suspend fun update(project: ProjectEntity) {
        projectDao.updateProject(project)
    }

    suspend fun delete(project: ProjectEntity) {
        projectDao.deleteProject(project)
    }
}
