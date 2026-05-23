package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erluxman.focuslauncher.config.FeatureFlagsRepository
import com.erluxman.focuslauncher.config.FlagKey
import com.erluxman.focuslauncher.ui.flags.FeatureFlagsScreen
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeatureFlagsScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext
    private val repo by lazy { FeatureFlagsRepository(ctx.applicationContext) }

    @Before
    fun clearOverrides() {
        runBlocking { repo.resetAll() }
    }

    @After
    fun cleanup() {
        runBlocking { repo.resetAll() }
    }

    @Test
    fun screen_rendersAndShowsRequiredAndOptionalFlags() {
        rule.setContent {
            FocusLauncherTheme {
                FeatureFlagsScreen(repo = repo, onBack = {})
            }
        }
        rule.onNodeWithTag("feature-flags").assertIsDisplayed()
        // A required flag is locked-on
        rule.onNodeWithTag("flag-switch-${FlagKey.LAUNCHER_HOME}").performScrollTo().assertIsOn()
        // An optional Stage-1 flag defaults to on
        rule.onNodeWithTag("flag-switch-${FlagKey.STATS_SHEET}").performScrollTo().assertIsOn()
        // A Stage-3 flag still gated off until external dep resolved
        rule.onNodeWithTag("flag-switch-COUPLES_MODE").performScrollTo().assertIsOff()
    }

    @Test
    fun toggling_writesOverride_andSurvivesRecompose() {
        rule.setContent {
            FocusLauncherTheme {
                FeatureFlagsScreen(repo = repo, onBack = {})
            }
        }
        val tag = "flag-switch-${FlagKey.STATS_SHEET}"
        rule.onNodeWithTag(tag).performScrollTo().assertIsOn().performClick()
        rule.waitUntil(2_000) {
            runCatching { rule.onNodeWithTag(tag).assertIsOff() }.isSuccess
        }
        rule.onNodeWithTag(tag).assertIsOff()
    }
}
