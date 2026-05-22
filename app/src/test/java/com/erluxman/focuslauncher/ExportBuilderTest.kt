package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.launcher.ExportBuilder

import com.erluxman.focuslauncher.data.local.entity.JournalEntryEntity
import com.erluxman.focuslauncher.data.local.entity.ProjectEntity
import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import com.erluxman.focuslauncher.service.launcher.ExportSnapshot
import org.junit.Assert.assertTrue
import org.junit.Test

class ExportBuilderTest {

    private fun snapshot(
        whyHere: String = "Build, don't scroll.",
        todos: List<TodoEntity> = emptyList(),
        projects: List<ProjectEntity> = emptyList(),
        journal: List<JournalEntryEntity> = emptyList()
    ) = ExportSnapshot(
        whyHere = whyHere,
        mantra = "I am the builder.",
        dailyTargetMin = 180,
        streakDays = 5,
        streakBest = 12,
        vipContacts = setOf("5551234567"),
        distractionPackages = setOf("com.instagram.android"),
        todos = todos,
        projects = projects,
        journal = journal
    )

    @Test
    fun jsonIncludesTopLevelFields() {
        val json = ExportBuilder.buildJson(snapshot(), exportedAtMs = 1700000000000L)
        assertTrue(json.contains("\"version\":1"))
        assertTrue(json.contains("\"exportedAt\":1700000000000"))
        assertTrue(json.contains("\"whyHere\":\"Build, don't scroll.\""))
        assertTrue(json.contains("\"streakDays\":5"))
        assertTrue(json.contains("\"streakBest\":12"))
        assertTrue(json.contains("\"vipContacts\":[\"5551234567\"]"))
    }

    @Test
    fun escapesQuotesAndBackslashes() {
        val s = snapshot(whyHere = "She said \"hi\" and \\ went home.")
        val json = ExportBuilder.buildJson(s, exportedAtMs = 0L)
        assertTrue(json.contains("\\\"hi\\\""))
        assertTrue(json.contains("\\\\"))
    }

    @Test
    fun includesTodosProjectsJournal() {
        val todos = listOf(TodoEntity(id = 1, text = "Ship feature", isCompleted = false, createdAt = 100L))
        val projects = listOf(ProjectEntity(id = 2, title = "P1", description = "d", progress = 0.5f, isActive = true, createdAt = 200L))
        val journal = listOf(JournalEntryEntity(id = 3, text = "note", behaviorState = "DROWNING", screenMinutes = 200, createdAt = 300L))
        val json = ExportBuilder.buildJson(snapshot(todos = todos, projects = projects, journal = journal), exportedAtMs = 0L)
        assertTrue(json.contains("\"text\":\"Ship feature\""))
        assertTrue(json.contains("\"title\":\"P1\""))
        assertTrue(json.contains("\"behaviorState\":\"DROWNING\""))
    }

    @Test
    fun emptyCollections_renderEmptyArrays() {
        val json = ExportBuilder.buildJson(snapshot(), exportedAtMs = 0L)
        assertTrue(json.contains("\"todos\":[]"))
        assertTrue(json.contains("\"projects\":[]"))
        assertTrue(json.contains("\"journal\":[]"))
    }
}
