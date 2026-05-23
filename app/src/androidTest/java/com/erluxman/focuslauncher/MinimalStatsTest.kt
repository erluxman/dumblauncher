package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.ui.home.minimal.MinimalStatsScreen
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MinimalStatsTest {

    @get:Rule
    val rule = createComposeRule()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun statsScreen_rendersFooterAndReturnLink() {
        val prefs = UserPrefs(ctx.applicationContext)
        runBlocking {
            // Seed enough to make at least one "today" line render.
            prefs.applyStreak(newDays = 12, newBest = 47, todayIso = "2026-05-22")
        }
        rule.setContent {
            FocusLauncherTheme {
                MinimalStatsScreen(
                    prefs = prefs,
                    onReturn = {},
                    onOpenTransparency = {},
                    onOpenUninstall = {},
                    onOpenDashboard = {},
                )
            }
        }
        rule.onNodeWithTag("minimal-stats").assertIsDisplayed()
        rule.onNodeWithTag("stats-return").assertIsDisplayed()
        rule.onNodeWithTag("stats-transparency").assertIsDisplayed()
        rule.onNodeWithTag("stats-uninstall").assertIsDisplayed()
        rule.onNodeWithTag("stats-dashboard").assertIsDisplayed()
    }
}
