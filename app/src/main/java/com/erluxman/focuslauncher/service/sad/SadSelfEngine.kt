package com.erluxman.focuslauncher.service.sad

/**
 * SAD-001/002: personalised "sad self" templates. Four voice styles, each with
 * a "with-why" pool and a "no-why" pool. The active voice is chosen by the user.
 */
object SadSelfEngine {

    enum class Voice { STERN, COMPASSIONATE, WITTY, DRILL }

    private val sternWithWhy = listOf(
        "Future-you wanted: \"{why}\". Today-you is here instead.",
        "You wrote: \"{why}\". Right now you're {state}.",
        "The you who wrote \"{why}\" is asking what happened.",
        "\"{why}\" — those were your words. Reconcile.",
        "You promised yourself \"{why}\". {state} is the receipt."
    )

    private val sternNoWhy = listOf(
        "You're {state}. The next decision is the only one that counts.",
        "{state}. You can argue with that or change it.",
        "You called yourself a builder. Right now, the data says consumer.",
        "Whatever you tell yourself, the clock just told the truth.",
        "{state} is the score. The play is yours."
    )

    private val compassionateWithWhy = listOf(
        "Hey — you once wrote \"{why}\". You're {state} now, and that's information, not a verdict.",
        "Soft check-in: \"{why}\" was the goal. Notice that, without blame.",
        "You're {state}. Your past self who wrote \"{why}\" still believes in you.",
        "Be kind with yourself, but don't lie to yourself. {state} — what's one small reset?",
        "Breathe. \"{why}\" hasn't gone anywhere. You can pick it up again right now."
    )

    private val compassionateNoWhy = listOf(
        "You're {state}. Notice it, no story attached.",
        "Hi. {state}. What does the next 60 seconds look like?",
        "Soft truth: {state}. One small step is enough.",
        "Try not to argue with it — {state}. Just choose differently next.",
        "Be gentle with yourself. {state} is a snapshot, not an identity."
    )

    private val wittyWithWhy = listOf(
        "Plot twist: the you who wrote \"{why}\" is currently {state}. The protagonist must do better.",
        "\"{why}\" — said the brave hero before scrolling for 47 minutes. {state} now.",
        "Your past self is in the audience holding a sign: \"{why}\". Embarrassing — you're {state}.",
        "The director just yelled cut. \"{why}\" was the line; \"{state}\" is the blooper reel.",
        "If \"{why}\" were Yelp, {state} would be the one-star review."
    )

    private val wittyNoWhy = listOf(
        "Live look at your day so far: {state}. Critics agree it could be better.",
        "Performance review: {state}. Promotion postponed.",
        "Status: {state}. Status of phone: thriving.",
        "Today's app rating: ⭐ ({state}).",
        "Plot: {state}. Reviews: needs a rewrite."
    )

    private val drillWithWhy = listOf(
        "\"{why}\" — that's the mission. You're {state}. UNACCEPTABLE.",
        "What's the goal, recruit? \"{why}\". And the report says? {state}. Move.",
        "I don't care how you feel. \"{why}\" is the order. {state} is excuses. Pick up the phone-down.",
        "{state}?! The you who said \"{why}\" is watching. DO BETTER.",
        "Drop and give me one productive action. \"{why}\" is the mission. {state} is not it."
    )

    private val drillNoWhy = listOf(
        "{state}. UNACCEPTABLE. Move.",
        "You. Are. {state}. Fix it now.",
        "I see one option: get off the phone. {state} is not a strategy.",
        "Stop reading this and start working. {state} ends in 5...4...3.",
        "{state} doesn't ship. Action does. Go."
    )

    /**
     * Stable pick: same (state, why, seed, voice) returns the same string.
     */
    fun pick(state: String, why: String, seed: Int, voice: Voice = Voice.STERN): String {
        val pool = poolFor(voice, why.isNotBlank())
        val tpl = pool[((seed % pool.size) + pool.size) % pool.size]
        return tpl.replace("{state}", state).replace("{why}", why.trim())
    }

    private fun poolFor(voice: Voice, hasWhy: Boolean): List<String> = when (voice) {
        Voice.STERN -> if (hasWhy) sternWithWhy else sternNoWhy
        Voice.COMPASSIONATE -> if (hasWhy) compassionateWithWhy else compassionateNoWhy
        Voice.WITTY -> if (hasWhy) wittyWithWhy else wittyNoWhy
        Voice.DRILL -> if (hasWhy) drillWithWhy else drillNoWhy
    }
}
