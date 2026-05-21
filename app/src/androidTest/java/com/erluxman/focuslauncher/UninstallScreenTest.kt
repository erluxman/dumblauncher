package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.ui.uninstall.CooldownMath
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UninstallScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun seed() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
            prefs.setWhyHere("Build, don't scroll.")
            prefs.cancelUninstallRequest()
        }
    }

    @Test
    fun openUninstall_showsStartCta_thenSwitchesToCountdown() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("settings-button").performClick()
        rule.onNodeWithTag("open-uninstall").performScrollTo().performClick()

        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("uninstall").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("uninstall-start").assertIsDisplayed().performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("uninstall-countdown").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("uninstall-countdown").assertIsDisplayed()
        rule.onNodeWithTag("uninstall-cancel").assertIsDisplayed().performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("uninstall-start").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
