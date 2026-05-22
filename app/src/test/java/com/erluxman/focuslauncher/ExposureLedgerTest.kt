package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.places.ExposureLedger
import org.junit.Assert.assertEquals
import org.junit.Test

class ExposureLedgerTest {

    @Test
    fun parse_dropsMalformedAndNegative() {
        val s = ExposureLedger.parse(setOf(
            "2026-05-22|12.5|3.0",
            "noPipe",
            "2026-05-22|abc|3.0",
            "2026-05-21|-1|3.0",
        ))
        assertEquals(1, s.size)
        assertEquals(12.5, s.first().pm25, 0.0)
    }

    @Test
    fun pm25OnDate_isSum() {
        val s = ExposureLedger.parse(setOf(
            "2026-05-22|10.0|3.0",
            "2026-05-22|5.0|2.0",
            "2026-05-21|20.0|1.0",
        ))
        assertEquals(15.0, ExposureLedger.pm25OnDate("2026-05-22", s), 1e-9)
        assertEquals(20.0, ExposureLedger.pm25OnDate("2026-05-21", s), 1e-9)
    }

    @Test
    fun lifetime_sums() {
        val s = ExposureLedger.parse(setOf(
            "2026-05-22|10.0|3.0",
            "2026-05-21|20.0|1.0",
        ))
        assertEquals(30.0, ExposureLedger.lifetimePm25(s), 1e-9)
        assertEquals(4.0, ExposureLedger.lifetimeUv(s), 1e-9)
    }
}
