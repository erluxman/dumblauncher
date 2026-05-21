package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.TimeDebt
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeDebtTest {

    @Test
    fun noDebt_effectiveEqualsBase() {
        assertEquals(180, TimeDebt.effectiveTarget(baseTargetMin = 180, debtMin = 0))
    }

    @Test
    fun debtHalvesEffectiveTarget_viaDoublePenalty() {
        // 30 min debt → 60 min penalty → effective = 180-60 = 120
        assertEquals(120, TimeDebt.effectiveTarget(baseTargetMin = 180, debtMin = 30))
    }

    @Test
    fun effectiveTarget_clampsAtFloor() {
        assertEquals(TimeDebt.FLOOR_MIN, TimeDebt.effectiveTarget(baseTargetMin = 60, debtMin = 100))
    }

    @Test
    fun nextDebt_growsWhenOver() {
        val newDebt = TimeDebt.nextDebt(
            currentDebtMin = 0,
            yesterdayScreenMin = 200,
            yesterdayEffectiveTargetMin = 180
        )
        assertEquals(20, newDebt)
    }

    @Test
    fun nextDebt_drainsWhenUnder() {
        val newDebt = TimeDebt.nextDebt(
            currentDebtMin = 30,
            yesterdayScreenMin = 100,
            yesterdayEffectiveTargetMin = 180
        )
        // under by 80 → debt drains from 30 to 0 (floored)
        assertEquals(0, newDebt)
    }

    @Test
    fun nextDebt_capsAtMax() {
        val newDebt = TimeDebt.nextDebt(
            currentDebtMin = TimeDebt.MAX_DEBT_MIN,
            yesterdayScreenMin = 500,
            yesterdayEffectiveTargetMin = 60
        )
        assertEquals(TimeDebt.MAX_DEBT_MIN, newDebt)
    }
}
