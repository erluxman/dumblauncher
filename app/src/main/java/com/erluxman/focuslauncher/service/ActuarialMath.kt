package com.erluxman.focuslauncher.service

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

/**
 * Pure math for the opt-in mortality widgets:
 * - LIFE-009 Beach Days Remaining (Saturdays in Jun–Aug between now and the actuarial end)
 * - LIFE-012 Death Clock (days remaining to the actuarial end)
 *
 * Inputs are intentionally minimal — current age (already in prefs) and an
 * assumed life expectancy. Time zone defaults to the device default so the
 * widget tracks "today" from the user's perspective.
 */
object ActuarialMath {

    const val DEFAULT_LIFE_EXPECTANCY_YEARS = 80
    const val DAYS_PER_YEAR = 365.25

    /** Whole days remaining if the user lives to [endAge]. Floor at 0. */
    fun daysRemaining(currentAge: Int, endAge: Int = DEFAULT_LIFE_EXPECTANCY_YEARS): Int {
        if (currentAge <= 0 || endAge <= currentAge) return 0
        val years = endAge - currentAge
        return (years * DAYS_PER_YEAR).toInt()
    }

    /**
     * Beach Saturdays remaining = Saturdays falling between [nowMs] and the
     * actuarial end that land in the meteorological summer (Jun, Jul, Aug,
     * northern-hemisphere default).
     *
     * The first year is partial (subtract Saturdays before today). All later
     * years contribute their full ~13 summer Saturdays.
     */
    fun beachSaturdaysRemaining(
        currentAge: Int,
        endAge: Int = DEFAULT_LIFE_EXPECTANCY_YEARS,
        nowMs: Long = System.currentTimeMillis(),
        tz: TimeZone = TimeZone.getDefault(),
        summerMonths: IntRange = Calendar.JUNE..Calendar.AUGUST,
    ): Int {
        if (currentAge <= 0 || endAge <= currentAge) return 0
        val nowCal = GregorianCalendar(tz).apply { timeInMillis = nowMs }
        val startYear = nowCal.get(Calendar.YEAR)
        val endYear = startYear + (endAge - currentAge)
        var total = 0
        for (year in startYear..endYear) {
            total += summerSaturdaysInYear(year, tz, summerMonths) {
                year > startYear || it.after(nowCal) || it.equals(nowCal)
            }
        }
        return total
    }

    /**
     * Same as [beachSaturdaysRemaining] but only counts the next 12 months —
     * the "this season + next season" view the widget actually shows so the
     * number doesn't read like an abstract bucket.
     */
    fun nextBeachSeasonSaturdays(
        nowMs: Long = System.currentTimeMillis(),
        tz: TimeZone = TimeZone.getDefault(),
        summerMonths: IntRange = Calendar.JUNE..Calendar.AUGUST,
    ): Int {
        val nowCal = GregorianCalendar(tz).apply { timeInMillis = nowMs }
        val year = nowCal.get(Calendar.YEAR)
        // If we're past the season, the "next" season is next year.
        val targetYear = if (nowCal.get(Calendar.MONTH) > summerMonths.last) year + 1 else year
        return summerSaturdaysInYear(targetYear, tz, summerMonths) { sat ->
            targetYear != year || sat.after(nowCal) || sat.equals(nowCal)
        }
    }

    private inline fun summerSaturdaysInYear(
        year: Int,
        tz: TimeZone,
        summerMonths: IntRange,
        predicate: (Calendar) -> Boolean,
    ): Int {
        var count = 0
        val cal = GregorianCalendar(tz).apply {
            clear()
            set(year, summerMonths.first, 1, 12, 0, 0)
        }
        while (cal.get(Calendar.MONTH) <= summerMonths.last && cal.get(Calendar.YEAR) == year) {
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && predicate(cal)) count++
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return count
    }
}
