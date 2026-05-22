package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
class OneThingTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun seed() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
            prefs.setLegacyHome(true)
            prefs.clearOneThing()
        }
    }

    @Test
    fun settingOneThing_swapsCardToTextDisplay() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("one-thing").assertIsDisplayed()
        rule.onNodeWithTag("one-thing-input").performTextInput("Ship the onboarding refactor")
        rule.onNodeWithTag("one-thing-save").performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("one-thing-text").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("one-thing-text").assertIsDisplayed()
        rule.onNodeWithTag("one-thing-clear").assertIsDisplayed().performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("one-thing-input").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
