package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
class OnboardingFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val ctx get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun resetOnboarding() {
        // Mark onboarding incomplete so the activity routes us into onboarding.
        runBlocking {
            val prefs = UserPrefs(ctx.applicationContext)
            prefs.setOnboardingComplete(false)
            prefs.setWhyHere("")
        }
    }

    @Test
    fun welcomeStep_continueAdvancesToWhyHere() {
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("Welcome.").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Welcome.").assertIsDisplayed()
        composeTestRule.onNodeWithTag("onboarding-next").performClick()
        composeTestRule.onNodeWithText("Why are you here?").assertIsDisplayed()
    }

    @Test
    fun whyHere_requiresMinimumLengthBeforeContinue() {
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("Welcome.").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("onboarding-next").performClick()  // -> WhyHere
        composeTestRule.onNodeWithTag("onboarding-next").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("why-here-input").performTextInput("Tired of scrolling away my evenings.")
        composeTestRule.onNodeWithTag("onboarding-next").assertIsEnabled()
    }

    @Test
    fun fullFlow_completesAndShowsHome() {
        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithText("Welcome.").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("onboarding-next").performClick()  // -> WhyHere
        composeTestRule.onNodeWithTag("why-here-input").performTextInput("Make leaving the phone easier than picking it up.")
        composeTestRule.onNodeWithTag("onboarding-next").performClick()  // -> Permissions
        composeTestRule.onNodeWithTag("onboarding-next").performClick()  // -> Finish
        composeTestRule.onNodeWithTag("onboarding-next").performClick()  // -> Home

        composeTestRule.waitUntil(5_000) {
            composeTestRule.onAllNodesWithTag("home").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("home").assertIsDisplayed()
        composeTestRule.onNodeWithTag("why-here-card").assertIsDisplayed()
    }
}
