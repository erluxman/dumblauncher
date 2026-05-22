package com.erluxman.focuslauncher.service

/**
 * SOCIAL-011 Built Verification Badge — eligibility check.
 *
 * Spec: 365 days installed + 3 shipped projects + 3 vouches. Until the
 * social tier ships, we ignore vouches and compute the local-only part
 * so a user can see how far off they are.
 */
object BuiltVerification {

    const val MIN_DAYS = 365
    const val MIN_PROJECTS = 3

    data class Status(
        val isEligible: Boolean,
        val daysInstalled: Int,
        val projectsShipped: Int,
        val daysShort: Int,
        val projectsShort: Int,
    )

    fun compute(daysInstalled: Int, projectsShipped: Int): Status {
        val daysShort = (MIN_DAYS - daysInstalled).coerceAtLeast(0)
        val projShort = (MIN_PROJECTS - projectsShipped).coerceAtLeast(0)
        return Status(
            isEligible = daysShort == 0 && projShort == 0,
            daysInstalled = daysInstalled.coerceAtLeast(0),
            projectsShipped = projectsShipped.coerceAtLeast(0),
            daysShort = daysShort,
            projectsShort = projShort,
        )
    }
}
