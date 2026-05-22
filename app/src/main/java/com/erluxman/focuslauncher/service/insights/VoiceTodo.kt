package com.erluxman.focuslauncher.service.insights

/**
 * PROD-008 Voice-to-Todo — parser side.
 *
 * Speech recognition (the SpeechRecognizer wiring) is not in this V1.
 * What we own here is the pure transformation from a transcript to a
 * Todo shape, so callers can pipe in either Android STT, the existing
 * VoiceMantra path, or test fixtures.
 *
 * Supported patterns (case-insensitive):
 *   "X for Y minutes"       → (X, Y)
 *   "X for Y min"           → (X, Y)
 *   "X (Y min)"             → (X, Y)
 *   "X"                     → (X, null)
 */
object VoiceTodo {

    data class Parsed(val text: String, val estimateMin: Int?)

    private val forPattern = Regex(
        "(?i)\\bfor\\s+(\\d{1,3})\\s*(?:min|mins|minute|minutes)\\b"
    )
    private val parenPattern = Regex(
        "(?i)\\((\\d{1,3})\\s*(?:min|mins|minute|minutes)\\)"
    )

    fun parse(transcript: String): Parsed? {
        val raw = transcript.trim()
        if (raw.isEmpty()) return null
        val (min, withoutMin) = extractMinutes(raw)
        val text = withoutMin.trim().trimEnd(',', '.', ';').ifEmpty { return null }
        return Parsed(text = text, estimateMin = min)
    }

    private fun extractMinutes(raw: String): Pair<Int?, String> {
        forPattern.find(raw)?.let { m ->
            val n = m.groupValues[1].toIntOrNull()
            val without = raw.removeRange(m.range)
            if (n != null) return n to without
        }
        parenPattern.find(raw)?.let { m ->
            val n = m.groupValues[1].toIntOrNull()
            val without = raw.removeRange(m.range)
            if (n != null) return n to without
        }
        return null to raw
    }
}
