package com.erluxman.focuslauncher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.erluxman.focuslauncher.ui.boredom.BoredomScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoredomScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun boredom_showsTimer() {
        rule.setContent { BoredomScreen(onBack = {}) }
        rule.onNodeWithTag("boredom").assertIsDisplayed()
        rule.onNodeWithTag("boredom-timer").assertIsDisplayed()
    }
}
