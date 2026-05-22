package com.erluxman.focuslauncher.service

/**
 * PSYCH-013 Anchoring Attack.
 *
 * Shows a "most disciplined user" stat next to the viewer's own. The
 * anchor is *not* real — it's textbook anchoring meant to recalibrate
 * the viewer's sense of normal. Surface honestly: ETHICS-001 transparency
 * page lists this and lets the user disable it.
 *
 * To stay defensible we cap the anchor to a believable floor (12m) and
 * never claim it represents real users. Caller is responsible for the
 * label copy.
 */
object Anchoring {

    /** "Most disciplined user today" anchor in minutes. */
    const val ANCHOR_MIN_FLOOR = 12

    /** Strict floor so the bar feels reachable, not infinite. */
    fun anchorMinutes(userTodayMin: Int, floor: Int = ANCHOR_MIN_FLOOR): Int {
        val raw = (userTodayMin / 4).coerceAtLeast(floor)
        return raw.coerceAtMost(60) // believable cap
    }

    /** Ratio of user vs anchor; > 1 = user is doing worse. */
    fun multiplier(userMin: Int, anchorMin: Int): Double {
        if (anchorMin <= 0) return 0.0
        return userMin.toDouble() / anchorMin
    }
}
