package com.erluxman.focuslauncher.ui.sadself

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.service.sad.SadSelfEngine
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Sad-self preview + voice picker. Reads `whyHere`, generates a sample
 * line per voice, persists the selected voice. Behind FlagKey.SAD_SELF.
 */
@Composable
fun SadSelfScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val savedVoice by prefs.sadVoice.collectAsState(initial = "STERN")
    val whyHere by prefs.whyHere.collectAsState(initial = "")
    var seed by remember { mutableIntStateOf(0) }

    val voices = SadSelfEngine.Voice.values()
    val state = "47 minutes in TikTok"  // sample preview state

    Surface(
        modifier = Modifier.fillMaxSize().testTag("sad-self"),
        color = MinimalTheme.bg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                "← back",
                style = captionStyle,
                color = MinimalTheme.outline,
                modifier = Modifier
                    .testTag("sad-self-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("sad self.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "the voice that nudges you when you drift. preview each.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            Text(
                text = "shuffle",
                style = captionStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .testTag("sad-self-shuffle")
                    .clickable { seed += 1 }
                    .padding(4.dp),
            )

            Spacer(Modifier.height(24.dp))
            voices.forEach { voice ->
                val isSelected = voice.name == savedVoice
                val sample = SadSelfEngine.pick(state, whyHere, seed, voice)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .testTag("sad-self-voice-${voice.name}"),
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = voice.name.lowercase(),
                            style = bodyStyle,
                            color = if (isSelected) MinimalTheme.accent else MinimalTheme.fg,
                            modifier = Modifier
                                .clickable {
                                    if (!isSelected) scope.launch { prefs.setSadVoice(voice.name) }
                                }
                                .testTag("sad-self-pick-${voice.name}")
                                .padding(end = 12.dp),
                        )
                        if (isSelected) {
                            Text("· selected", style = captionStyle, color = MinimalTheme.outline)
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = sample,
                        style = bodyStyle.copy(fontSize = 16.sp),
                        color = MinimalTheme.outline,
                    )
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
