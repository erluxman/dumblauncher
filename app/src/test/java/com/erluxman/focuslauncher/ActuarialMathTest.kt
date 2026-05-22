package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.mortality.ActuarialMath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

class ActuarialMathTest {

    private val utc = TimeZone.getTimeZone("UTC")

    @Test
    fun daysRemaining_zeroWhenAgeAtOrPastEnd() {
        assertEquals(0, ActuarialMath.daysRemaining(currentAge = 0))
        assertEquals(0, ActuarialMath.daysRemaining(currentAge = 80, endAge = 80))
        assertEquals(0, ActuarialMath.daysRemaining(currentAge = 95, endAge = 80))
    }

    @Test
    fun daysRemaining_matchesYearsAt365_25() {
        // 40 years left = 14_610 days at 365.25/yr
        assertEquals((40 * 365.25).toInt(), ActuarialMath.daysRemaining(40))
    }

    @Test
    fun beachSaturdays_zeroWhenNoYearsLeft() {
        val nowMs = isoMs(2026, Calendar.JUNE, 1)
        assertEquals(0, ActuarialMath.beachSaturdaysRemaining(80, nowMs = nowMs, tz = utc))
    }

    @Test
    fun beachSaturdays_currentYearTailOnly() {
        // Pick Aug 1 2026 — only the remaining Saturdays of August 2026 should count.
        // endAge == currentAge → returns 0 early. So use one-year span and subtract
        // the full following summer to isolate the current-year tail.
        val nowMs = isoMs(2026, Calendar.AUGUST, 1, hour = 1)
        val tail2026 = countSummerSaturdaysAtOrAfter(2026, nowMs)
        val all2027 = countSummerSaturdaysAtOrAfter(2027, isoMs(2027, Calendar.JANUARY, 1))
        assertEquals(tail2026 + all2027, ActuarialMath.beachSaturdaysRemaining(79, 80, nowMs, utc))
    }

    @Test
    fun beachSaturdays_multipleYears_growsPredictably() {
        val nowMs = isoMs(2026, Calendar.JUNE, 1)
        val twoYears = ActuarialMath.beachSaturdaysRemaining(78, 80, nowMs, utc)
        val tenYears = ActuarialMath.beachSaturdaysRemaining(70, 80, nowMs, utc)
        // Both upper-bounded by 14 weekends per summer × years.
        assertTrue(twoYears in 1..(3 * 14))
        assertTrue(tenYears in 1..(11 * 14))
        assertTrue("more years should mean more saturdays", tenYears > twoYears)
    }

    @Test
    fun nextBeachSeason_pastSummerRollsToNextYear() {
        val nowMs = isoMs(2026, Calendar.DECEMBER, 15)
        val n = ActuarialMath.nextBeachSeasonSaturdays(nowMs, utc)
        val expected = countSummerSaturdaysAtOrAfter(2027, isoMs(2027, Calendar.JANUARY, 1))
        assertEquals("counts every Saturday in next year's Jun-Aug", expected, n)
        assertTrue("Jun-Aug has 13 or 14 Saturdays", n in 13..14)
    }

    @Test
    fun nextBeachSeason_midSummerCountsRemaining() {
        val nowMs = isoMs(2026, Calendar.JULY, 15, hour = 1)
        val n = ActuarialMath.nextBeachSeasonSaturdays(nowMs, utc)
        // Whatever's left this summer must be > 0 and < 14.
        assertTrue(n in 1..13)
    }

    private fun isoMs(year: Int, month: Int, day: Int, hour: Int = 12): Long {
        return GregorianCalendar(utc).apply {
            clear()
            set(year, month, day, hour, 0, 0)
        }.timeInMillis
    }

    private fun countSummerSaturdaysAtOrAfter(year: Int, nowMs: Long): Int {
        val cal = GregorianCalendar(utc).apply { clear(); set(year, Calendar.JUNE, 1, 12, 0, 0) }
        val now = GregorianCalendar(utc).apply { timeInMillis = nowMs }
        var n = 0
        while (cal.get(Calendar.MONTH) <= Calendar.AUGUST && cal.get(Calendar.YEAR) == year) {
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && !cal.before(now)) n++
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return n
    }
}
