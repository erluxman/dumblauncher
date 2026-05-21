package com.erluxman.focuslauncher.service

object SadSelfEngine {

    private val templatesWithWhy = listOf(
        "Future-you wanted: \"{why}\". Today-you is here instead.",
        "You wrote: \"{why}\". Right now you're {state}.",
        "The you who wrote \"{why}\" is asking what happened.",
        "\"{why}\" — those were your words. Reconcile.",
        "You promised yourself \"{why}\". {state} is the receipt."
    )

    private val templatesNoWhy = listOf(
        "You're {state}. The next decision is the only one that counts.",
        "{state}. You can argue with that or change it.",
        "You called yourself a builder. Right now, the data says consumer.",
        "Whatever you tell yourself, the clock just told the truth.",
        "{state} is the score. The play is yours."
    )

    /**
     * Stable pick: same (state, why, seed) returns the same string. Use the day-of-year
     * (or any once-per-day token) as `seed` so the message rotates daily but doesn't
     * thrash mid-day on every recomposition.
     */
    fun pick(state: String, why: String, seed: Int): String {
        val pool = if (why.isBlank()) templatesNoWhy else templatesWithWhy
        val tpl = pool[((seed % pool.size) + pool.size) % pool.size]
        return tpl.replace("{state}", state).replace("{why}", why.trim())
    }
}
