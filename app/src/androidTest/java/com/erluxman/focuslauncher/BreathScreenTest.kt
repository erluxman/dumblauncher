package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.erluxman.focuslauncher.ui.breath.BreathScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BreathScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun breathScreen_rendersAndStartButtonWorks() {
        var backTapped = false
        rule.setContent {
            BreathScreen(onBack = { backTapped = true })
        }
        rule.onNodeWithTag("breath-screen").assertIsDisplayed()
        rule.onNodeWithTag("breath-cue").assertIsDisplayed()
        rule.onNodeWithTag("breath-start").assertIsDisplayed().performClick()
        // After tapping start, the elapsed counter should be present.
        rule.onNodeWithTag("breath-elapsed").assertIsDisplayed()
        rule.onNodeWithTag("breath-back").performClick()
        assert(backTapped) { "back should fire onBack" }
    }
}
