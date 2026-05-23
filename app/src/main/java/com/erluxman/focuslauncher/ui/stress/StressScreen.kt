package com.erluxman.focuslauncher.ui.stress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.insights.StressIndex
import com.erluxman.focuslauncher.service.launcher.HealthSource
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * Stress weather report. Sleep auto-fetched from HealthConnect; unlocks
 * typed by the user (no built-in unlocks counter yet).
 * Behind FlagKey.STRESS.
 */
@Composable
fun StressScreen(
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val sleepMin by produceState(initialValue = 0, key1 = Unit) {
        value = runCatching { HealthSource.lastNightSleepMinutes(context) }.getOrDefault(0)
    }
    var unlocksTxt by remember { mutableStateOf("") }
    val unlocks = unlocksTxt.toIntOrNull() ?: 0
    val report = StressIndex.compute(unlocksToday = unlocks, sleepMinutesLastNight = sleepMin)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("stress"),
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
                    .testTag("stress-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("stress weather.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "sleep + checking behavior. worst signal wins.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("weather", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = report.label.lowercase(),
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = when {
                    report.total >= 70 -> MinimalTheme.accent
                    report.total >= 45 -> MinimalTheme.fg
                    else -> MinimalTheme.outline
                },
                modifier = Modifier.testTag("stress-label"),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "score ${report.total} / 100",
                style = captionStyle,
                color = MinimalTheme.outline,
                modifier = Modifier.testTag("stress-score"),
            )

            Spacer(Modifier.height(32.dp))
            Sub("sleep subscore", report.sleepSub, sleepMin > 0,
                detail = if (sleepMin > 0) "${sleepMin / 60}h ${sleepMin % 60}m last night"
                else "no sleep data — assumed worst",
                tag = "stress-sleep")

            Sub("unlock subscore", report.unlocksSub, unlocks > 0,
                detail = if (unlocks > 0) "$unlocks unlocks today" else "no count — assumed zero",
                tag = "stress-unlocks")

            Spacer(Modifier.height(24.dp))
            Text("estimate unlocks today", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            BasicTextField(
                value = unlocksTxt,
                onValueChange = { unlocksTxt = it.filter { c -> c.isDigit() }.take(4) },
                singleLine = true,
                textStyle = bodyStyle.copy(color = MinimalTheme.fg, fontSize = 24.sp),
                cursorBrush = SolidColor(MinimalTheme.accent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().testTag("stress-unlocks-input"),
                decorationBox = { inner ->
                    if (unlocksTxt.isEmpty()) {
                        Text("0", style = bodyStyle.copy(fontSize = 24.sp),
                            color = MinimalTheme.outline.copy(alpha = 0.5f))
                    }
                    inner()
                },
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MinimalTheme.outline.copy(alpha = 0.4f))
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Sub(label: String, score: Int, hasData: Boolean, detail: String, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            "$score / 100",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
            color = if (hasData) MinimalTheme.fg else MinimalTheme.outline,
            modifier = Modifier.testTag(tag),
        )
        Spacer(Modifier.height(2.dp))
        Text(detail, style = captionStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
