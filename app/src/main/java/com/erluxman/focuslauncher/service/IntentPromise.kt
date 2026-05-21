package com.erluxman.focuslauncher.service

/**
 * Pure logic for the Intent Follow-through Lockout (V1 RESTRICT-006).
 *
 * After declaring intent in the Lobby, the user is granted a small budget
 * inside a distraction app. Exiting after exceeding that budget locks the
 * app for the rest of the day.
 */
object IntentPromise {

    /** Per-session budget in milliseconds — the implicit "I'll be quick" promise. */
    const val SESSION_BUDGET_MS = 10L * 60 * 1000  // 10 minutes

    fun didOverstay(elapsedMs: Long, budgetMs: Long = SESSION_BUDGET_MS): Boolean =
        elapsedMs > budgetMs
}
