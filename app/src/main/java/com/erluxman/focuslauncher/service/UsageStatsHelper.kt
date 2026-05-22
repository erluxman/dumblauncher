package com.erluxman.focuslauncher.service
import com.erluxman.focuslauncher.service.insights.HourlyHeatmap

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Process
import java.util.Calendar

object UsageStatsHelper {

    fun hasPermission(context: Context): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            }
            mode == AppOpsManager.MODE_ALLOWED
        } catch (_: Exception) {
            false
        }
    }

    /** Returns total foreground time across all apps today, in minutes. */
    fun todayScreenMinutes(context: Context): Int {
        if (!hasPermission(context)) return 0
        val usage = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return 0
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = cal.timeInMillis
        val end = System.currentTimeMillis()
        val stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
            ?: return 0
        val totalMs = stats.sumOf { it.totalTimeInForeground }
        return (totalMs / 60_000L).toInt()
    }

    /** Returns per-package foreground minutes today for the given packages. */
    fun todayMinutesByPackage(context: Context, packages: Set<String>): Map<String, Int> {
        if (!hasPermission(context) || packages.isEmpty()) return emptyMap()
        val usage = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return emptyMap()
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = cal.timeInMillis
        val end = System.currentTimeMillis()
        val stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
            ?: return emptyMap()
        return stats.filter { it.packageName in packages }
            .groupBy { it.packageName }
            .mapValues { (_, list) -> (list.sumOf { it.totalTimeInForeground } / 60_000L).toInt() }
    }

    /**
     * Returns today's per-hour foreground minutes as an IntArray(24).
     * Sessions spanning hour boundaries are split via [HourlyHeatmap.bucketize].
     */
    fun todayHourlyMinutes(context: Context): IntArray {
        if (!hasPermission(context)) return IntArray(24)
        val usage = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return IntArray(24)
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dayStart = cal.timeInMillis
        val now = System.currentTimeMillis()
        val events = usage.queryEvents(dayStart, now)
        val active = mutableMapOf<String, Long>()
        val sessions = mutableListOf<LongRange>()
        val ev = android.app.usage.UsageEvents.Event()
        while (events.hasNextEvent()) {
            events.getNextEvent(ev)
            when (ev.eventType) {
                android.app.usage.UsageEvents.Event.MOVE_TO_FOREGROUND ->
                    active[ev.packageName ?: continue] = ev.timeStamp
                android.app.usage.UsageEvents.Event.MOVE_TO_BACKGROUND -> {
                    val start = active.remove(ev.packageName ?: continue) ?: continue
                    sessions.add(start..ev.timeStamp)
                }
            }
        }
        active.forEach { (_, s) -> sessions.add(s..now) }
        return HourlyHeatmap.bucketize(sessions, dayStart, now)
    }

    /** Returns total foreground time for the day starting `daysAgo` days ago, in minutes. */
    fun screenMinutesForDay(context: Context, daysAgo: Int): Int {
        if (!hasPermission(context)) return 0
        val usage = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            ?: return 0
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, -daysAgo)
        }
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 1)
        val end = cal.timeInMillis
        val stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
            ?: return 0
        return (stats.sumOf { it.totalTimeInForeground } / 60_000L).toInt()
    }

    /** Computes the behavior state from today's screen-time relative to user's daily target (minutes). */
    fun deriveBehaviorState(screenMinutes: Int, targetMinutes: Int): BehaviorReading {
        // 5 buckets: THRIVING (<50%), NEUTRAL (50–100%), DRIFTING (100–150%), SINKING (150–200%), DROWNING (>200%)
        val ratio = if (targetMinutes <= 0) 0f else screenMinutes / targetMinutes.toFloat()
        val state = when {
            ratio < 0.5f -> "THRIVING"
            ratio < 1.0f -> "NEUTRAL"
            ratio < 1.5f -> "DRIFTING"
            ratio < 2.0f -> "SINKING"
            else -> "DROWNING"
        }
        // Progress bar: how close are we to fully consumed (1.0 = at-or-over target).
        val progress = (ratio).coerceIn(0f, 1f)
        // For THRIVING we invert so the bar looks reassuringly full.
        val displayProgress = when (state) {
            "THRIVING" -> 1f - progress.coerceAtMost(0.5f) * 2f * 0.3f
            else -> progress
        }
        return BehaviorReading(state = state, progress = displayProgress, screenMinutes = screenMinutes, targetMinutes = targetMinutes)
    }
}

data class BehaviorReading(
    val state: String,
    val progress: Float,
    val screenMinutes: Int,
    val targetMinutes: Int
)
