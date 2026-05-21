package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.erluxman.focuslauncher.ui.lobby.LobbyContent
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LobbyTest {

    @get:Rule val rule = createComposeRule()

    @Test
    fun lobbyRenders_andContinueStaysDisabledUntilGated() {
        rule.setContent {
            FocusLauncherTheme {
                LobbyContent(
                    targetPackage = "com.example.distract",
                    onAcknowledged = {},
                    onAborted = {}
                )
            }
        }
        // Lobby visible
        rule.onNodeWithTag("lobby").assertIsDisplayed()
        // Continue is disabled initially (countdown active + nothing typed)
        rule.onNodeWithTag("lobby-continue").assertIsNotEnabled()
        // Intent declaration field present
        rule.onNodeWithTag("lobby-intent-input").assertIsDisplayed()
        // Cognitive tax field present
        rule.onNodeWithTag("lobby-tax-input").assertIsDisplayed()
        // Abort button available
        rule.onNodeWithTag("lobby-abort").assertIsDisplayed()
    }

    @Test
    fun lobby_showsInterventionBanner_whenCountSet() {
        rule.setContent {
            FocusLauncherTheme {
                LobbyContent(
                    targetPackage = "com.example.distract",
                    interventionCount = 14,
                    onAcknowledged = {},
                    onAborted = {}
                )
            }
        }
        rule.onNodeWithTag("lobby-intervention").assertIsDisplayed()
    }

    @Test
    fun lobby_typingIntentAlone_doesNotEnableContinue() {
        rule.setContent {
            FocusLauncherTheme {
                LobbyContent(
                    targetPackage = "com.example.distract",
                    onAcknowledged = {},
                    onAborted = {}
                )
            }
        }
        rule.onNodeWithTag("lobby-intent-input").performTextInput("reply to Sam")
        // Even with intent, tax unsolved -> continue still disabled.
        rule.onNodeWithTag("lobby-continue").assertIsNotEnabled()
    }
}
