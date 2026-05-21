package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.erluxman.focuslauncher.ui.home.InterventionBanner
import com.erluxman.focuslauncher.ui.home.InterventionDialog
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InterventionTest {

    @get:Rule val rule = createComposeRule()

    @Test
    fun banner_showsDeclarationAndPauseCta() {
        var paused = false
        rule.setContent {
            FocusLauncherTheme {
                InterventionBanner(
                    state = "DROWNING",
                    whyHere = "Build, don't scroll.",
                    onPause = { paused = true }
                )
            }
        }
        rule.onNodeWithTag("intervention-banner").assertIsDisplayed()
        rule.onNodeWithText("PAUSE").assertIsDisplayed()
        rule.onNodeWithTag("intervention-pause").performClick()
        assert(paused) { "Pause callback should fire" }
    }

    @Test
    fun dialog_savesOnlyWhenNoteIsNotBlank() {
        var saved: String? = null
        rule.setContent {
            FocusLauncherTheme {
                InterventionDialog(
                    state = "SINKING",
                    whyHere = "Build, don't scroll.",
                    onDismiss = {},
                    onSave = { saved = it }
                )
            }
        }
        // Save disabled initially
        rule.onNodeWithTag("intervention-save").assertIsNotEnabled()
        rule.onNodeWithTag("intervention-note-input").performTextInput("Doomscrolling reddit out of boredom.")
        rule.onNodeWithTag("intervention-save").assertIsEnabled().performClick()
        assert(saved == "Doomscrolling reddit out of boredom.") { "Save callback should fire with text" }
    }
}
