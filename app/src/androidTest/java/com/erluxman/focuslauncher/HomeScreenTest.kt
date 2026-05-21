package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
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
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun skipOnboardingAndSeedDeclaration() {
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(true)
            prefs.setWhyHere("Build, don't scroll.")
        }
    }

    @Test
    fun home_displaysCoreSections() {
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("home").assertIsDisplayed()
        composeTestRule.onNodeWithTag("behavior-indicator").assertIsDisplayed()
        composeTestRule.onNodeWithTag("why-here-card").assertIsDisplayed()
        composeTestRule.onNodeWithTag("one-thing").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home-list").performScrollToNode(hasTestTag("streak-row"))
        composeTestRule.onNodeWithTag("streak-row").assertIsDisplayed()
    }

    @Test
    fun addingTodo_persistsAndAppears() {
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        val text = "UI test todo ${System.currentTimeMillis()}"
        composeTestRule.onNodeWithTag("home-list").performScrollToNode(hasTestTag("add-task-input"))
        composeTestRule.onNodeWithTag("add-task-input").performTextInput(text)
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun openingSetup_revealsTransparencyEntry() {
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("settings-button").performClick()
        composeTestRule.onNodeWithTag("open-transparency").performScrollTo().performClick()
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithTag("transparency").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("transparency").assertIsDisplayed()
    }
}
