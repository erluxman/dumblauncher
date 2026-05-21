package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.CheckInScheduler
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class CheckInSchedulerTest {

    @Test
    fun nextTrigger_isInFuture() {
        val now = System.currentTimeMillis()
        val next = CheckInScheduler.nextTrigger(9, 0, nowMs = now)
        assertTrue(next > now)
    }

    @Test
    fun nextTrigger_isOnTheRequestedHour() {
        val nowCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val triggerMs = CheckInScheduler.nextTrigger(9, 0, nowMs = nowCal.timeInMillis)
        val triggerCal = Calendar.getInstance().apply { timeInMillis = triggerMs }
        // 14:00 today → next 9:00 is tomorrow.
        assertTrue(triggerCal.get(Calendar.HOUR_OF_DAY) == 9)
        assertTrue(triggerCal.get(Calendar.MINUTE) == 0)
    }
}
