package com.erluxman.focuslauncher.ui.absurd

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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.absurdKazoo
import com.erluxman.focuslauncher.data.prefs.absurdNarrator
import com.erluxman.focuslauncher.data.prefs.absurdVoiceGate
import com.erluxman.focuslauncher.data.prefs.setAbsurdKazoo
import com.erluxman.focuslauncher.data.prefs.setAbsurdNarrator
import com.erluxman.focuslauncher.data.prefs.setAbsurdVoiceGate
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Three absurdist-tier toggles combined into one settings screen:
 * - ABSURD-001 Kazoo Mode
 * - ABSURD-002 Narrator
 * - ABSURD-004 Voice Authenticity Gate
 *
 * The toggles are persisted; the actual audio/TTS wiring lands in
 * follow-up commits.  Behind FlagKey.ABSURD.
 */
@Composable
fun AbsurdistScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val kazoo by prefs.absurdKazoo.collectAsState(initial = false)
    val narrator by prefs.absurdNarrator.collectAsState(initial = false)
    val voiceGate by prefs.absurdVoiceGate.collectAsState(initial = false)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("absurd"),
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
                    .testTag("absurd-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("absurdist toggles.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "extreme techniques. you have to opt in. audio wiring lands in follow-ups.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            ToggleRow(
                title = "kazoo mode",
                desc = "speaker plays a kazoo in distraction apps. can't mute.",
                tag = "absurd-kazoo",
                value = kazoo,
                onChange = { scope.launch { prefs.setAbsurdKazoo(it) } },
            )
            ToggleRow(
                title = "narrator",
                desc = "tts reads your actions aloud: 'he opened twitter. again.'",
                tag = "absurd-narrator",
                value = narrator,
                onChange = { scope.launch { prefs.setAbsurdNarrator(it) } },
            )
            ToggleRow(
                title = "voice authenticity gate",
                desc = "before opening a distraction app, say aloud: 'i am choosing distraction over my goals.'",
                tag = "absurd-voice-gate",
                value = voiceGate,
                onChange = { scope.launch { prefs.setAbsurdVoiceGate(it) } },
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    desc: String,
    tag: String,
    value: Boolean,
    onChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = bodyStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(2.dp))
            Text(desc, style = captionStyle, color = MinimalTheme.outline)
        }
        Switch(
            checked = value,
            onCheckedChange = onChange,
            modifier = Modifier.testTag("$tag-switch"),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MinimalTheme.accent,
                checkedTrackColor = MinimalTheme.accent.copy(alpha = 0.4f),
                uncheckedThumbColor = MinimalTheme.outline,
                uncheckedTrackColor = Color.Transparent,
                uncheckedBorderColor = MinimalTheme.outline,
            ),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
