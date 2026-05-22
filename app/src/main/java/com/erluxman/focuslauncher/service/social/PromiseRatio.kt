package com.erluxman.focuslauncher.service.social

import com.erluxman.focuslauncher.data.local.entity.TodoEntity

/**
 * IDENT-002 Promise Kept Ratio.
 *
 * Pure-fn aggregator over TodoEntity rows. A completed todo counts as
 * a kept promise; an uncompleted todo older than [staleDays] counts
 * as broken. Younger uncompleted todos are still pending.
 */
object PromiseRatio {

    const val DEFAULT_STALE_DAYS = 7

    data class Snapshot(
        val kept: Int,
        val broken: Int,
        val pending: Int,
        val ratioPct: Int,
    )

    fun compute(
        todos: List<TodoEntity>,
        nowMs: Long = System.currentTimeMillis(),
        staleDays: Int = DEFAULT_STALE_DAYS,
    ): Snapshot {
        val staleCutoff = nowMs - staleDays.toLong() * 86_400_000L
        var kept = 0
        var broken = 0
        var pending = 0
        for (t in todos) {
            when {
                t.isCompleted -> kept++
                t.createdAt < staleCutoff -> broken++
                else -> pending++
            }
        }
        val denom = kept + broken
        val ratio = if (denom <= 0) 0 else (kept.toDouble() / denom * 100.0).toInt()
        return Snapshot(kept = kept, broken = broken, pending = pending, ratioPct = ratio)
    }
}
