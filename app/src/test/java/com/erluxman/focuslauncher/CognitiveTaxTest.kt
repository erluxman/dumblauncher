package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.ui.lobby.CognitiveTax
import com.erluxman.focuslauncher.ui.lobby.Op
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class CognitiveTaxTest {

    @Test
    fun generated_addition_problemAnswerIsCorrect() {
        repeat(200) {
            val p = CognitiveTax.generate(Random(it.toLong()))
            assertEquals(
                "${p.prompt} mismatch",
                when (p.op) {
                    Op.ADD -> p.a + p.b
                    Op.SUB -> p.a - p.b
                    Op.MUL -> p.a * p.b
                },
                p.answer
            )
        }
    }

    @Test
    fun subtraction_neverGoesNegative() {
        repeat(500) {
            val p = CognitiveTax.generate(Random(it.toLong()))
            if (p.op == Op.SUB) assertTrue("$p went negative", p.answer >= 0)
        }
    }

    @Test
    fun addition_isInReasonableRange() {
        repeat(500) {
            val p = CognitiveTax.generate(Random(it.toLong()))
            if (p.op == Op.ADD) {
                assertTrue("$p too small", p.answer in 22..98)
            }
        }
    }
}
