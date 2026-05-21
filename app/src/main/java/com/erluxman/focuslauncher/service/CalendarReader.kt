package com.erluxman.focuslauncher.service

import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import java.util.Calendar

/**
 * PROD-015 Time Blocking + RESTRICT-010 Context-Aware Locks.
 *
 * Reads today's calendar events from [CalendarContract]. The current and
 * next event are surfaced on the home screen; any event whose title matches
 * [FOCUS_KEYWORDS] flips the user into "focus block" mode, which makes
 * RESTRICT-010 force even nourishing apps through the Lobby.
 */
object CalendarReader {

    data class Event(val title: String, val startMs: Long, val endMs: Long)

    val FOCUS_KEYWORDS = listOf("focus", "deep work", "writing", "meeting", "interview")

    fun hasPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED

    fun todayEvents(context: Context, nowMs: Long = System.currentTimeMillis()): List<Event> {
        if (!hasPermission(context)) return emptyList()
        val cal = Calendar.getInstance().apply {
            timeInMillis = nowMs
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val dayStart = cal.timeInMillis
        val dayEnd = dayStart + 24L * 60 * 60 * 1000
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )
        val selection = "${CalendarContract.Events.DTEND} > ? AND ${CalendarContract.Events.DTSTART} < ?"
        val args = arrayOf(dayStart.toString(), dayEnd.toString())
        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection, selection, args,
            "${CalendarContract.Events.DTSTART} ASC"
        ) ?: return emptyList()
        val out = mutableListOf<Event>()
        cursor.use { c ->
            while (c.moveToNext()) {
                val title = c.getString(0) ?: continue
                val start = c.getLong(1)
                val end = c.getLong(2).takeIf { it > start } ?: (start + 30L * 60 * 1000)
                out.add(Event(title, start, end))
            }
        }
        return out
    }

    fun activeEvent(events: List<Event>, nowMs: Long = System.currentTimeMillis()): Event? =
        events.firstOrNull { nowMs in it.startMs..it.endMs }

    fun isFocusBlock(event: Event?): Boolean {
        val t = event?.title?.lowercase() ?: return false
        return FOCUS_KEYWORDS.any { t.contains(it) }
    }
}
