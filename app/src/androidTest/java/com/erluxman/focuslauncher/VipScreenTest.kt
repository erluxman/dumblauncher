package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VipScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun seed() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
            prefs.setVipContacts(emptySet())
        }
    }

    @Test
    fun addAndDeleteVip_persistsAndDisappears() {
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("settings-button").performClick()
        rule.onNodeWithTag("open-vip").performScrollTo().performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("vip").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("vip-input").performTextInput("5551234567")
        rule.onNodeWithTag("vip-add").performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("vip-row-5551234567").fetchSemanticsNodes().isNotEmpty()
        }
        rule.onNodeWithTag("vip-row-5551234567").assertIsDisplayed()
        rule.onNodeWithTag("vip-delete-5551234567").performClick()
        rule.waitUntil(5_000) {
            rule.onAllNodesWithTag("vip-row-5551234567").fetchSemanticsNodes().isEmpty()
        }
    }
}
