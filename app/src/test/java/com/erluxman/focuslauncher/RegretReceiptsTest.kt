package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.RegretReceipts
import org.junit.Assert.assertEquals
import org.junit.Test

class RegretReceiptsTest {

    @Test
    fun parse_dropsMalformed() {
        val r = RegretReceipts.parse(setOf("food|10.0|3", "x|y|z", "food|10.0|9"))
        assertEquals(1, r.size)
        assertEquals(3, r.first().rating)
    }

    @Test
    fun regretPct_zeroWhenNone() {
        assertEquals(0, RegretReceipts.regretPctFor("food", emptyList()))
    }

    @Test
    fun regretPct_halfWhenHalfLowRated() {
        val r = RegretReceipts.parse(setOf(
            "food|10|1",
            "food|10|5",
            "food|10|2",
            "food|10|4",
        ))
        assertEquals(50, RegretReceipts.regretPctFor("food", r))
    }

    @Test
    fun categoryComparisonIsCaseInsensitive() {
        val r = RegretReceipts.parse(setOf("Food|10|1", "FOOD|10|5"))
        assertEquals(50, RegretReceipts.regretPctFor("food", r))
    }

    @Test
    fun totalUsd_sumsCategory() {
        val r = RegretReceipts.parse(setOf("food|10|3", "food|5.5|3", "books|99|5"))
        assertEquals(15.5, RegretReceipts.totalUsd("food", r), 0.001)
        assertEquals(99.0, RegretReceipts.totalUsd("books", r), 0.001)
    }
}
