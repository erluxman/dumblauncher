package com.erluxman.focuslauncher.service

/**
 * CALLS-005 Silent Mode Enforcement — policy decision only.
 *
 * Pure rule: given the current focus signals, should the launcher
 * request system DND? VIP / emergency-pass bypass is enforced by the
 * call-side path; this is just the "is now a focus moment" gate.
 */
object SilentModePolicy {

    fun shouldEnforce(
        isFocusSessionActive: Boolean,
        isCalendarFocusBlock: Boolean,
        isDreamMode: Boolean,
    ): Boolean = isFocusSessionActive || isCalendarFocusBlock || isDreamMode
}
