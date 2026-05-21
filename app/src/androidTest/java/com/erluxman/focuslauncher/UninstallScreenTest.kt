package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
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
            prefs.setNuclearPassphrase("twenty_chars_or_more_passphrase")
            prefs.setFutureSelfLetter("Dear future self, you started this for a reason.")
        }
    }

    @Test
    fun openUninstall_showsLastDayTest_thenStartCta_thenCountdown() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("settings-button").performClick()
        rule.onNodeWithTag("open-uninstall").performScrollTo().performClick()

        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("last-day-test").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("last-day-acknowledge").assertIsDisplayed().performClick()

        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("passphrase-gate").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("passphrase-verify").performTextInput("twenty_chars_or_more_passphrase")
        rule.onNodeWithTag("passphrase-continue").assertIsDisplayed().performClick()

        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("uninstall").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("uninstall-start").assertIsDisplayed().performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("uninstall-countdown").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("uninstall-countdown").assertIsDisplayed()
        rule.onNodeWithTag("uninstall-cancel").assertIsDisplayed().performClick()
    }

    @Test
    fun passphraseSetupMode_appearsWhenNoneSet() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setNuclearPassphrase("")
            prefs.setFutureSelfLetter("")
        }
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("settings-button").performClick()
        rule.onNodeWithTag("open-uninstall").performScrollTo().performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("last-day-test").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("last-day-acknowledge").performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("passphrase-gate").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("passphrase-new").assertIsDisplayed()
        rule.onNodeWithTag("passphrase-letter").assertIsDisplayed()
    }
}
