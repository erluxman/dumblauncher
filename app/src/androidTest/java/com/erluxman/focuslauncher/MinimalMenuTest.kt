package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.setSleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.setSleepWakeHour
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Flow test: every feature is reachable from the minimal home through the menu.
 *
 * Verifies the entry-point fix the user asked for — the home now has tappable
 * affordances (↓ hint, settings dock, inline placeholder) that all converge on
 * the menu, and the menu fans out to every feature screen.
 */
@RunWith(AndroidJUnit4::class)
class MinimalMenuTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    private val featureRows = listOf(
        "menu-dashboard",
        "menu-transparency",
        "menu-vip",
        "menu-focus",
        "menu-mantra",
        "menu-boredom",
        "menu-breath",
        "menu-future-self",
        "menu-onboarding",
        "menu-uninstall",
    )

    @Before
    fun setupMinimalHomeNotDream() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
            prefs.setLegacyHome(false)
            // cutoff == wake disables the sleep window entirely → never dream-mode in tests.
            prefs.setSleepCutoffHour(0)
            prefs.setSleepWakeHour(0)
        }
    }

    private fun waitForHome() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("minimal-home").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun waitForMenu() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("minimal-menu").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun home_dashboardHintTap_opensMenu() {
        waitForHome()
        rule.onNodeWithTag("dashboard-hint").performClick()
        waitForMenu()
        rule.onNodeWithTag("minimal-menu").assertIsDisplayed()
    }

    @Test
    fun home_settingsDockTap_opensMenu() {
        waitForHome()
        rule.onNodeWithTag("dock-settings").performClick()
        waitForMenu()
        rule.onNodeWithTag("minimal-menu").assertIsDisplayed()
    }

    @Test
    fun menu_exposesEveryFeatureRow() {
        waitForHome()
        rule.onNodeWithTag("dashboard-hint").performClick()
        waitForMenu()
        featureRows.forEach { tag ->
            rule.onNodeWithTag(tag).performScrollTo().assertIsDisplayed()
        }
    }

    @Test
    fun menu_tappingTransparency_navigatesToTransparencyScreen() {
        waitForHome()
        rule.onNodeWithTag("dashboard-hint").performClick()
        waitForMenu()
        rule.onNodeWithTag("menu-transparency").performScrollTo().performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("transparency").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("transparency").assertIsDisplayed()
    }

    @Test
    fun menu_backReturnsToHome() {
        waitForHome()
        rule.onNodeWithTag("dock-settings").performClick()
        waitForMenu()
        rule.onNodeWithTag("menu-back").performClick()
        waitForHome()
        rule.onNodeWithTag("minimal-home").assertIsDisplayed()
    }
}
