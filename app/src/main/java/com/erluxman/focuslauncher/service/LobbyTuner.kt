package com.erluxman.focuslauncher.service
import com.erluxman.focuslauncher.service.tracks.UserLevel

object LobbyTuner {

    const val BASE_SECONDS = 10

    /**
     * Returns the lobby countdown seconds for visit `visitOrdinal` (0-indexed) given
     * whether escalating and variable-ratio techniques are enabled.
     * Pure for unit tests; randomness is provided by the caller via `randomRoll` in [0,1).
     */
    fun countdownSeconds(
        base: Int = BASE_SECONDS,
        visitOrdinal: Int,
        escalating: Boolean,
        variableRatio: Boolean,
        randomRoll: Double,
        userLevel: Int = 0
    ): Int {
        val eased = UserLevel.easedLobbySeconds(base, userLevel)
        var s = eased
        if (escalating && visitOrdinal > 0) s += visitOrdinal * ESCALATE_STEP_S
        if (variableRatio && randomRoll < VARIABLE_RATIO_CHANCE) s += VARIABLE_RATIO_PENALTY_S
        return s
    }

    fun isHarderMath(variableRatio: Boolean, randomRoll: Double): Boolean =
        variableRatio && randomRoll < VARIABLE_RATIO_CHANCE

    /** Stable replacement suggestion based on a day seed. */
    fun replacement(seed: Long): String = REPLACEMENTS[((seed % REPLACEMENTS.size) + REPLACEMENTS.size).toInt() % REPLACEMENTS.size]

    const val ESCALATE_STEP_S = 5
    const val VARIABLE_RATIO_CHANCE = 0.20
    const val VARIABLE_RATIO_PENALTY_S = 15

    val REPLACEMENTS = listOf(
        "Read for 5 minutes instead",
        "Take 10 deep breaths and reassess",
        "Walk to the window. Just look outside.",
        "Do 10 push-ups. Reset your body.",
        "Drink a glass of water first.",
        "Write one line in the journal.",
        "Stretch your back for two minutes."
    )
}
