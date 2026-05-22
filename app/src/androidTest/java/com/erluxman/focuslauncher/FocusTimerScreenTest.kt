package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FocusTimerScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun seed() {
        runBlocking {
            UserPrefs(ctx.applicationContext).apply { setOnboardingComplete(true); setLegacyHome(true) }
        }
    }

    @Test
    fun openFocus_showsTimerAndStartButton() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("settings-button").performClick()
        rule.onNodeWithTag("open-focus").performScrollTo().performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("focus-timer").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("focus-timer").assertIsDisplayed()
        rule.onNodeWithTag("focus-time-text").assertIsDisplayed()
        rule.onNodeWithText("Start").assertIsDisplayed()
    }
}
