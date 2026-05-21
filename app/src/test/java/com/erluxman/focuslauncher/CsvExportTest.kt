package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import com.erluxman.focuslauncher.service.ExportBuilder
import com.erluxman.focuslauncher.service.ExportSnapshot
import org.junit.Assert.assertTrue
import org.junit.Test

class CsvExportTest {

    @Test
    fun csv_includesAllSectionHeaders() {
        val snap = ExportSnapshot(
            whyHere = "Build, not scroll",
            mantra = "today is mine",
            dailyTargetMin = 180,
            streakDays = 3,
            streakBest = 7,
            vipContacts = setOf("+15551234567"),
            distractionPackages = setOf("com.instagram.android"),
            todos = listOf(TodoEntity(text = "ship feature", isCompleted = true,
                createdAt = 1L, completedAt = 2L)),
            projects = emptyList(),
            journal = emptyList()
        )
        val csv = ExportBuilder.buildCsv(snap, exportedAtMs = 0L)
        listOf("## prefs", "## vipContacts", "## distractionPackages",
            "## todos", "## projects", "## journal").forEach {
            assertTrue("CSV missing section $it", csv.contains(it))
        }
    }

    @Test
    fun csv_escapesCommasAndQuotes() {
        val snap = ExportSnapshot(
            whyHere = "build, not, scroll",
            mantra = "she said \"yes\"",
            dailyTargetMin = 60,
            streakDays = 0,
            streakBest = 0,
            vipContacts = emptySet(),
            distractionPackages = emptySet(),
            todos = emptyList(),
            projects = emptyList(),
            journal = emptyList()
        )
        val csv = ExportBuilder.buildCsv(snap, exportedAtMs = 0L)
        // Comma-containing field is quoted; embedded quote is doubled.
        assertTrue(csv.contains("\"build, not, scroll\""))
        assertTrue(csv.contains("\"she said \"\"yes\"\"\""))
    }
}
