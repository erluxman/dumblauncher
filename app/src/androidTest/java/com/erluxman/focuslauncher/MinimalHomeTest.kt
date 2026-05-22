package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
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

@RunWith(AndroidJUnit4::class)
class MinimalHomeTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

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

    @Test
    fun minimalHome_isVisibleByDefault() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("minimal-home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("minimal-home").assertIsDisplayed()
        rule.onNodeWithTag("dashboard-hint").assertIsDisplayed()
    }

    @Test
    fun minimalHome_showsExactlyOneSection() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("minimal-home").fetchSemanticsNodes().isNotEmpty()
        }
        val sections = listOf("section-morning", "section-work", "section-shutdown", "section-dream")
        val visible = sections.count {
            rule.onAllNodesWithTag(it).fetchSemanticsNodes().isNotEmpty()
        }
        assert(visible == 1) {
            "Expected exactly one hour-of-day section visible, but found $visible. " +
                "Sections: $sections"
        }
    }

    @Test
    fun dockSettings_visibleOnHome() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("minimal-home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("dock-settings").assertIsDisplayed()
    }
}
