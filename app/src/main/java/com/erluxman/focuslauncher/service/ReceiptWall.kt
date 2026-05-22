package com.erluxman.focuslauncher.service

/**
 * SOCIAL-012 Receipt Wall — classifier.
 *
 * The social tier surfaces a wall of "restraint receipts" — small
 * tiles for each time the user opened a flagged app but put it down
 * within 3 minutes. This pure-fn is the gate: was that session a win?
 */
object ReceiptWall {

    const val THRESHOLD_SEC = 180

    fun isQuickQuit(sessionSec: Int): Boolean = sessionSec in 1..THRESHOLD_SEC

    /** Headline copy for a single quick-quit. */
    fun receiptFor(appLabel: String, sessionSec: Int): String? {
        if (!isQuickQuit(sessionSec)) return null
        val app = appLabel.trim().ifEmpty { return null }
        val mins = sessionSec / 60
        val secs = sessionSec % 60
        val timeStr = if (mins == 0) "${secs}s" else "${mins}m ${secs}s"
        return "Put $app down after $timeStr. Win."
    }
}
