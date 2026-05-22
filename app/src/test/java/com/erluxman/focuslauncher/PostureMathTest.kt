package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.PostureMath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PostureMathTest {

    @Test
    fun upright_portrait() {
        assertEquals(PostureMath.Pose.UPRIGHT, PostureMath.classify(0f, 9.8f, 0f))
    }

    @Test
    fun flatFaceUp_isFlatFaceUp() {
        assertEquals(PostureMath.Pose.FLAT_FACEUP, PostureMath.classify(0f, 0f, 9.8f))
    }

    @Test
    fun flatFaceDown_isFlatFaceDown() {
        assertEquals(PostureMath.Pose.FLAT_FACEDOWN, PostureMath.classify(0f, 0f, -9.8f))
    }

    @Test
    fun tilted_isTilted() {
        // 45-degree tilt: about 6.9 m/s² on two axes.
        val v = 6.9f
        assertEquals(PostureMath.Pose.TILTED, PostureMath.classify(v, v, 0f))
    }

    @Test
    fun isInBed_onlyTrueForFaceUp() {
        assertTrue(PostureMath.isInBed(PostureMath.Pose.FLAT_FACEUP))
        assertFalse(PostureMath.isInBed(PostureMath.Pose.UPRIGHT))
        assertFalse(PostureMath.isInBed(PostureMath.Pose.TILTED))
        assertFalse(PostureMath.isInBed(PostureMath.Pose.FLAT_FACEDOWN))
    }
}
