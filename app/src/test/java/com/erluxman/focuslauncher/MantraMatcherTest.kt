package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.ui.mantra.MantraMatcher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MantraMatcherTest {

    @Test
    fun exactMatch_matches() {
        assertTrue(MantraMatcher.matches("Build, don't scroll.", "Build, don't scroll."))
    }

    @Test
    fun caseInsensitive_matches() {
        assertTrue(MantraMatcher.matches("BUILD, DON'T SCROLL.", "build, don't scroll."))
    }

    @Test
    fun whitespaceCollapses() {
        assertTrue(MantraMatcher.matches("  Build,   don't  scroll. ", "Build, don't scroll."))
    }

    @Test
    fun differentText_doesNotMatch() {
        assertFalse(MantraMatcher.matches("Wrong text", "Build, don't scroll."))
    }

    @Test
    fun emptyMantra_alwaysMatches() {
        // mantraMode is off when mantra blank; matches() should not block anything.
        assertTrue(MantraMatcher.matches("anything", ""))
    }

    @Test
    fun trailingPunctuationStaysRelevant() {
        // We do not strip punctuation — typing a sentence without the period should fail.
        assertFalse(MantraMatcher.matches("Build, don't scroll", "Build, don't scroll."))
    }
}
