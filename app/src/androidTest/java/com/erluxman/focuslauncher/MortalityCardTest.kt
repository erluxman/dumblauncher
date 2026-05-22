package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MortalityCardTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun skipOnboardingAndOptIn() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
            prefs.setUserAge(35)
            prefs.setMortalityWidgetsOptIn(true)
        }
    }

    @Test
    fun mortalityCard_showsWhenOptedIn() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("home-list")
            .performScrollToNode(hasTestTag("mortality-card"))
        rule.onNodeWithTag("mortality-card").assertIsDisplayed()
        rule.onNodeWithTag("mortality-beach-total").assertIsDisplayed()
        rule.onNodeWithTag("mortality-beach-next").assertIsDisplayed()
        rule.onNodeWithTag("mortality-days").assertIsDisplayed()
    }

    @Test
    fun mortalityCard_hiddenWhenOptedOut() {
        runBlocking {
            UserPrefs(ctx.applicationContext).setMortalityWidgetsOptIn(false)
        }
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        // No mortality-card node should exist anywhere in the tree.
        val matches = rule.onAllNodesWithTag("mortality-card").fetchSemanticsNodes()
        assert(matches.isEmpty()) {
            "mortality-card should be hidden when opt-in is false, found ${matches.size}"
        }
    }
}
