package com.erluxman.focuslauncher.ui.compound

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.service.mortality.CompoundCurve
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * Compounding curve — pick a daily-improvement rate and a horizon, see the
 * multiplier and the days-to-2× / 3× / 10×. Read-only.
 * Behind FlagKey.COMPOUND_CURVE.
 */
@Composable
fun CompoundCurveScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val onboardingMs by prefs.onboardingCompletedAt.collectAsState(initial = 0L)
    val daysSince = remember(onboardingMs) {
        if (onboardingMs <= 0) 0
        else ((System.currentTimeMillis() - onboardingMs) / 86_400_000L).toInt()
    }

    val rates = listOf(0.001 to "0.1%/day", 0.005 to "0.5%/day", 0.01 to "1%/day")
    val horizons = listOf(365, 1095, 1825) // 1y, 3y, 5y
    var rateIdx by remember { mutableStateOf(0) }
    var horizonIdx by remember { mutableStateOf(1) }
    val rate = rates[rateIdx].first
    val horizon = horizons[horizonIdx]

    val multAtHorizon = remember(rate, horizon) { CompoundCurve.valueAt(horizon, rate) }
    val multToday = remember(rate, daysSince) { CompoundCurve.valueAt(daysSince, rate) }
    val daysTo2x = remember(rate) { CompoundCurve.daysToReach(2.0, rate) }
    val daysTo3x = remember(rate) { CompoundCurve.daysToReach(3.0, rate) }
    val daysTo10x = remember(rate) { CompoundCurve.daysToReach(10.0, rate) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("compound"),
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
                    .testTag("compound-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("compounding.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "small daily edge, exponential payoff.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("rate", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rates.forEachIndexed { i, (_, label) ->
                    Text(
                        text = label,
                        style = bodyStyle,
                        color = if (i == rateIdx) MinimalTheme.accent else MinimalTheme.outline,
                        modifier = Modifier
                            .testTag("compound-rate-$i")
                            .clickable { rateIdx = i }
                            .padding(8.dp),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("horizon", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                horizons.forEachIndexed { i, d ->
                    val label = when (d) { 365 -> "1y"; 1095 -> "3y"; else -> "5y" }
                    Text(
                        text = label,
                        style = bodyStyle,
                        color = if (i == horizonIdx) MinimalTheme.accent else MinimalTheme.outline,
                        modifier = Modifier
                            .testTag("compound-horizon-$i")
                            .clickable { horizonIdx = i }
                            .padding(8.dp),
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
            Text("multiplier at horizon", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "%.2fx".format(multAtHorizon),
                style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("compound-multiplier"),
            )

            Spacer(Modifier.height(32.dp))
            Section("you, today (day $daysSince)", "%.3fx".format(multToday), "compound-today")
            Section("days to 2×", "$daysTo2x", "compound-2x")
            Section("days to 3×", "$daysTo3x", "compound-3x")
            Section("days to 10×", "$daysTo10x", "compound-10x")
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Section(label: String, value: String, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(value, style = bodyStyle.copy(fontSize = 24.sp), color = MinimalTheme.fg,
            modifier = Modifier.testTag(tag))
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MinimalTheme.outline.copy(alpha = 0.15f))
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
