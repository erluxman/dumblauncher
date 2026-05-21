package com.erluxman.focuslauncher.ui.uninstall

object CooldownMath {
    const val COOLDOWN_MS: Long = 72L * 60L * 60L * 1000L  // 72 hours

    /** Remaining cooldown in ms (>= 0). */
    fun remainingMs(startMs: Long, nowMs: Long, cooldownMs: Long = COOLDOWN_MS): Long {
        val elapsed = nowMs - startMs
        val rem = cooldownMs - elapsed
        return if (rem < 0L) 0L else rem
    }

    fun isElapsed(startMs: Long, nowMs: Long, cooldownMs: Long = COOLDOWN_MS): Boolean =
        remainingMs(startMs, nowMs, cooldownMs) == 0L

    /** Formats remaining ms as "Xh Ym Zs". */
    fun format(remainingMs: Long): String {
        val totalSec = remainingMs / 1000L
        val h = totalSec / 3600L
        val m = (totalSec % 3600L) / 60L
        val s = totalSec % 60L
        return "${h}h ${m}m ${s}s"
    }
}
