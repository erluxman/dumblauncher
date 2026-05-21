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
class GraceRowTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun skipOnboarding() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
        }
    }

    @Test
    fun graceRow_isVisibleOnHome() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("home-list")
            .performScrollToNode(hasTestTag("grace-row"))
        rule.onNodeWithTag("grace-row").assertIsDisplayed()
        rule.onNodeWithTag("grace-freezes").assertIsDisplayed()
        rule.onNodeWithTag("grace-declare-tomorrow").assertIsDisplayed()
    }
}
