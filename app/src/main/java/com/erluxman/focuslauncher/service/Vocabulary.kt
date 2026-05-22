package com.erluxman.focuslauncher.service

/**
 * READ-003 Vocabulary Growth Curve — pure-fn.
 *
 * Given chunks of text (journal entries, highlights, etc.) build a
 * dedup'd set of lowercase words ≥ 3 chars. The growth between two
 * time-buckets is the net new word count, capturing vocabulary
 * expansion without surfacing the actual words.
 */
object Vocabulary {

    private val tokenizer = Regex("[a-zA-Z]{3,}")

    fun uniqueWords(texts: List<String>): Set<String> {
        val out = HashSet<String>()
        for (t in texts) {
            tokenizer.findAll(t).forEach { out += it.value.lowercase() }
        }
        return out
    }

    /** Net new words in [after] that were not in [before]. */
    fun growth(before: List<String>, after: List<String>): Int {
        val baseline = uniqueWords(before)
        val newSet = uniqueWords(after)
        return (newSet - baseline).size
    }
}
