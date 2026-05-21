package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoodPingTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun skipOnboarding() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
        }
    }

    @Test
    fun moodPing_logButtonDisabledUntilEmojiSelected() {
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("home-list")
            .performScrollToNode(hasTestTag("mood-ping-card"))
        composeTestRule.onNodeWithTag("mood-ping-card").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mood-emoji-0").performClick()
        composeTestRule.onNodeWithTag("mood-note-input").performTextInput("tired")
        composeTestRule.onNodeWithTag("mood-log-button").performClick()
    }
}
