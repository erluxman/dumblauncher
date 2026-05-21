package com.erluxman.focuslauncher.ui.lobby

import kotlin.random.Random

data class TaxProblem(val a: Int, val b: Int, val op: Op) {
    val prompt: String get() = when (op) {
        Op.ADD -> "$a + $b"
        Op.SUB -> "$a - $b"
        Op.MUL -> "$a × $b"
    }
    val answer: Int get() = when (op) {
        Op.ADD -> a + b
        Op.SUB -> a - b
        Op.MUL -> a * b
    }
}

enum class Op { ADD, SUB, MUL }

object CognitiveTax {
    /** Generates a problem hard enough to defeat reflex but easy enough to do in your head. */
    fun generate(random: Random = Random.Default): TaxProblem {
        // Pick op: ~40% add, 40% sub, 20% mul (mul is harder, less frequent).
        val opRoll = random.nextInt(10)
        val op = when {
            opRoll < 4 -> Op.ADD
            opRoll < 8 -> Op.SUB
            else -> Op.MUL
        }
        return when (op) {
            Op.ADD -> {
                val a = random.nextInt(11, 50)
                val b = random.nextInt(11, 50)
                TaxProblem(a, b, op)
            }
            Op.SUB -> {
                val a = random.nextInt(20, 99)
                val b = random.nextInt(5, a - 1)
                TaxProblem(a, b, op)
            }
            Op.MUL -> {
                val a = random.nextInt(3, 13)
                val b = random.nextInt(3, 13)
                TaxProblem(a, b, op)
            }
        }
    }
}
