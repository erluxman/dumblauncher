package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.data.prefs.clearMeditationLog

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
class MeditationCardTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun reset() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
            prefs.setLegacyHome(true)
            prefs.clearMeditationLog()
        }
    }

    @Test
    fun meditationCard_visible() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("home-list")
            .performScrollToNode(hasTestTag("meditation-card"))
        rule.onNodeWithTag("meditation-card").assertIsDisplayed()
        rule.onNodeWithTag("meditation-today").assertIsDisplayed()
        rule.onNodeWithTag("meditation-week").assertIsDisplayed()
        rule.onNodeWithTag("med-add-10").assertIsDisplayed()
    }
}
