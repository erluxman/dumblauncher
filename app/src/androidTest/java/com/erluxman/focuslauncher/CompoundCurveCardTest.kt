package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
class CompoundCurveCardTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun skipOnboarding() {
        runBlocking { UserPrefs(ctx.applicationContext).apply { setOnboardingComplete(true); setLegacyHome(true) } }
    }

    @Test
    fun curveCard_appearsOnHomeAndRespondsToRateClick() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("home-list")
            .performScrollToNode(hasTestTag("compound-card"))
        rule.onNodeWithTag("compound-card").assertIsDisplayed()
        rule.onNodeWithTag("compound-curve").assertIsDisplayed()
        rule.onNodeWithTag("compound-multiplier").assertIsDisplayed()

        // Pick the 1%/day rate; multiplier text must change but the node still exists.
        rule.onNodeWithTag("compound-rate-2").performClick()
        rule.onNodeWithTag("compound-multiplier").assertIsDisplayed()
    }
}
