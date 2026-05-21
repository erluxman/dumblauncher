package com.erluxman.focuslauncher.service

import com.erluxman.focuslauncher.data.local.entity.JournalEntryEntity
import com.erluxman.focuslauncher.data.local.entity.ProjectEntity
import com.erluxman.focuslauncher.data.local.entity.TodoEntity

data class ExportSnapshot(
    val whyHere: String,
    val mantra: String,
    val dailyTargetMin: Int,
    val streakDays: Int,
    val streakBest: Int,
    val vipContacts: Set<String>,
    val distractionPackages: Set<String>,
    val todos: List<TodoEntity>,
    val projects: List<ProjectEntity>,
    val journal: List<JournalEntryEntity>
)

object ExportBuilder {

    fun buildJson(snapshot: ExportSnapshot, exportedAtMs: Long): String {
        val sb = StringBuilder()
        sb.append("{")
        sb.append(kv("version", 1)).append(",")
        sb.append(kv("exportedAt", exportedAtMs)).append(",")
        sb.append(qkv("whyHere", snapshot.whyHere)).append(",")
        sb.append(qkv("mantra", snapshot.mantra)).append(",")
        sb.append(kv("dailyTargetMin", snapshot.dailyTargetMin)).append(",")
        sb.append(kv("streakDays", snapshot.streakDays)).append(",")
        sb.append(kv("streakBest", snapshot.streakBest)).append(",")
        sb.append("\"vipContacts\":").append(stringArray(snapshot.vipContacts)).append(",")
        sb.append("\"distractionPackages\":").append(stringArray(snapshot.distractionPackages)).append(",")
        sb.append("\"todos\":[").append(snapshot.todos.joinToString(",") { todoJson(it) }).append("],")
        sb.append("\"projects\":[").append(snapshot.projects.joinToString(",") { projectJson(it) }).append("],")
        sb.append("\"journal\":[").append(snapshot.journal.joinToString(",") { journalJson(it) }).append("]")
        sb.append("}")
        return sb.toString()
    }

    private fun todoJson(t: TodoEntity): String = buildString {
        append("{")
        append(kv("id", t.id)).append(",")
        append(qkv("text", t.text)).append(",")
        append(kv("isCompleted", t.isCompleted)).append(",")
        append(kv("createdAt", t.createdAt))
        if (t.completedAt != null) append(",").append(kv("completedAt", t.completedAt))
        append("}")
    }

    private fun projectJson(p: ProjectEntity): String = buildString {
        append("{")
        append(kv("id", p.id)).append(",")
        append(qkv("title", p.title)).append(",")
        append(qkv("description", p.description)).append(",")
        append(kv("progress", p.progress)).append(",")
        append(kv("isActive", p.isActive)).append(",")
        append(kv("createdAt", p.createdAt))
        append("}")
    }

    private fun journalJson(j: JournalEntryEntity): String = buildString {
        append("{")
        append(kv("id", j.id)).append(",")
        append(qkv("text", j.text)).append(",")
        append(qkv("behaviorState", j.behaviorState)).append(",")
        append(kv("screenMinutes", j.screenMinutes)).append(",")
        append(kv("createdAt", j.createdAt))
        append("}")
    }

    private fun stringArray(items: Iterable<String>): String =
        "[" + items.joinToString(",") { "\"${escape(it)}\"" } + "]"

    private fun kv(key: String, value: Any): String = "\"$key\":$value"
    private fun qkv(key: String, value: String): String = "\"$key\":\"${escape(value)}\""

    private fun escape(s: String): String = buildString {
        for (c in s) {
            when (c) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (c.code < 0x20) append("\\u%04x".format(c.code)) else append(c)
            }
        }
    }
}
