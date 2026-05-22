package com.erluxman.focuslauncher.service.fitness

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

/**
 * FIT-004 Sport-Specific Clocks.
 *
 * Derived from WorkoutLog. Pure-fn so the card stays a render.
 *
 * Conventions: dates are ISO yyyy-MM-dd; "todayIso" must be the user's
 * local today.
 */
object SportClock {

    private val isoFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        .also { it.timeZone = TimeZone.getDefault() }

    fun daysSinceLast(kind: String, sessions: List<WorkoutLog.Session>, todayIso: String): Int? {
        val sameKind = sessions.filter { it.kind.equals(kind, ignoreCase = true) }
        val latest = sameKind.maxByOrNull { it.isoDate } ?: return null
        return diffDays(latest.isoDate, todayIso)
    }

    /** Last 7 days hit ≥7 workout days = overtraining flag. */
    fun overtraining(sessions: List<WorkoutLog.Session>, todayIso: String, lookbackDays: Int = 7): Boolean {
        val cutoff = shiftIso(todayIso, -lookbackDays + 1) ?: return false
        val distinctDays = sessions.map { it.isoDate }
            .filter { it >= cutoff && it <= todayIso }
            .toSet()
        return distinctDays.size >= lookbackDays
    }

    private fun diffDays(fromIso: String, toIso: String): Int? {
        val from = isoFmt.parse(fromIso) ?: return null
        val to = isoFmt.parse(toIso) ?: return null
        val ms = to.time - from.time
        if (ms < 0) return 0
        return (ms / 86_400_000L).toInt()
    }

    private fun shiftIso(iso: String, deltaDays: Int): String? {
        val date: Date = isoFmt.parse(iso) ?: return null
        val cal = GregorianCalendar().apply { time = date; add(Calendar.DAY_OF_MONTH, deltaDays) }
        return isoFmt.format(cal.time)
    }
}
