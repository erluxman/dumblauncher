package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.social.ReceiptWall
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReceiptWallTest {

    @Test
    fun isQuickQuit_borders() {
        assertFalse(ReceiptWall.isQuickQuit(0))
        assertTrue(ReceiptWall.isQuickQuit(1))
        assertTrue(ReceiptWall.isQuickQuit(180))
        assertFalse(ReceiptWall.isQuickQuit(181))
        assertFalse(ReceiptWall.isQuickQuit(-30))
    }

    @Test
    fun receiptFor_nullPastThreshold() {
        assertNull(ReceiptWall.receiptFor("TikTok", 600))
    }

    @Test
    fun receiptFor_blankAppNoReceipt() {
        assertNull(ReceiptWall.receiptFor("", 30))
        assertNull(ReceiptWall.receiptFor("  ", 30))
    }

    @Test
    fun receiptFor_quickQuitProducesLine() {
        val s = ReceiptWall.receiptFor("TikTok", 45)
        assertNotNull(s)
        assertTrue(s!!.contains("TikTok"))
        assertTrue(s.contains("Win"))
    }
}
