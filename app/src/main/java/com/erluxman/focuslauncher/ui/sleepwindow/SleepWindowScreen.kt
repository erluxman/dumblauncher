package com.erluxman.focuslauncher.ui.sleepwindow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.setSleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.setSleepWakeHour
import com.erluxman.focuslauncher.data.prefs.sleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.sleepWakeHour
import com.erluxman.focuslauncher.service.lobby.SleepWindow
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import java.util.Calendar
import kotlinx.coroutines.launch

/**
 * Sleep-window cutoff + wake-hour pickers. Hours stored as 0..23 ints.
 * Behind FlagKey.SLEEP_WINDOW.
 */
@Composable
fun SleepWindowScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val cutoff by prefs.sleepCutoffHour.collectAsState(initial = SleepWindow.DEFAULT_CUTOFF_HOUR)
    val wake by prefs.sleepWakeHour.collectAsState(initial = SleepWindow.DEFAULT_WAKE_HOUR)
    val nowHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val inWindow = SleepWindow.isInWindow(nowHour, cutoff, wake)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("sleep-window"),
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
                    .testTag("sleep-window-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("sleep window.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                if (inWindow) "you're inside the window now. dream mode is on."
                else "${formatHour(cutoff)} → ${formatHour(wake)} ${if (cutoff == wake) "(disabled)" else ""}",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            HourPicker(
                label = "cutoff",
                hour = cutoff,
                tagPrefix = "sleep-cutoff",
                onChange = { scope.launch { prefs.setSleepCutoffHour(it) } },
            )

            Spacer(Modifier.height(24.dp))
            HourPicker(
                label = "wake",
                hour = wake,
                tagPrefix = "sleep-wake",
                onChange = { scope.launch { prefs.setSleepWakeHour(it) } },
            )

            Spacer(Modifier.height(40.dp))
            Text(
                "set cutoff = wake to disable dream mode entirely.",
                style = captionStyle,
                color = MinimalTheme.outline.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun HourPicker(
    label: String,
    hour: Int,
    tagPrefix: String,
    onChange: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Stepper("–", "$tagPrefix-down") { onChange(((hour - 1) + 24) % 24) }
            Text(
                text = formatHour(hour),
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.fg,
                modifier = Modifier.testTag("$tagPrefix-value"),
            )
            Stepper("+", "$tagPrefix-up") { onChange((hour + 1) % 24) }
            Spacer(Modifier.width(0.dp))
        }
    }
}

@Composable
private fun Stepper(symbol: String, tag: String, onClick: () -> Unit) {
    Text(
        text = symbol,
        style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Normal),
        color = MinimalTheme.accent,
        modifier = Modifier
            .testTag(tag)
            .clickable { onClick() }
            .padding(16.dp),
    )
}

private fun formatHour(h: Int): String {
    val hh = ((h % 12).let { if (it == 0) 12 else it })
    val ampm = if (h < 12) "am" else "pm"
    return "$hh $ampm"
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
