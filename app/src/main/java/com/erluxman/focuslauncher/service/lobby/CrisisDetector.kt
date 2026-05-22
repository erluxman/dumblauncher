package com.erluxman.focuslauncher.service.lobby

object CrisisDetector {

    /**
     * Returns true if the recent behavior pattern looks like a crisis-shaped one.
     * MVP rule: at least THRESHOLD_CONSECUTIVE_DROWNING entries at the head of the
     * window are DROWNING. Older history doesn't help; we only react to recent state.
     */
    fun isCrisis(recentStates: List<String>, threshold: Int = THRESHOLD_CONSECUTIVE_DROWNING): Boolean {
        if (recentStates.size < threshold) return false
        for (i in 0 until threshold) {
            if (recentStates[i] != "DROWNING") return false
        }
        return true
    }

    const val THRESHOLD_CONSECUTIVE_DROWNING = 3
}
