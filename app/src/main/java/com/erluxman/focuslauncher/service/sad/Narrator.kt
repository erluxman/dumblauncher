package com.erluxman.focuslauncher.service.sad

/**
 * ABSURD-002 Narrator — pure line generator.
 *
 * Generates a short, drily-narrated string each time the user opens a
 * flagged app today. We never actually play TTS for V1; the caller can
 * pipe these into a toast (like Applause) when the user opens an app.
 */
object Narrator {

    /**
     * Returns a line for the [openCount]th opening of [appLabel] today, or
     * null when the user is well under any threshold worth narrating.
     */
    fun lineFor(appLabel: String, openCount: Int): String? {
        val app = appLabel.trim().ifEmpty { return null }
        return when {
            openCount <= 2 -> null
            openCount == 3 -> "He opened $app. Three times today."
            openCount in 4..9 -> "He opened $app. Again. ${openCount}th time today."
            openCount in 10..19 -> "He opened $app for the ${openCount}th time. The phone is winning."
            else -> "He opened $app for the ${openCount}th time. The phone has won."
        }
    }
}
