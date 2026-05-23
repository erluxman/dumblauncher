package com.erluxman.focuslauncher.ui.consumption

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.caffeineDoses
import com.erluxman.focuslauncher.data.prefs.clearCaffeineLog
import com.erluxman.focuslauncher.data.prefs.clearDrinkLog
import com.erluxman.focuslauncher.data.prefs.drinkLog
import com.erluxman.focuslauncher.data.prefs.logCaffeine
import com.erluxman.focuslauncher.data.prefs.logDrink
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * Caffeine + alcohol quick logger. Each row of presets writes a timestamped
 * dose; the prefs layer prunes anything older than 24h automatically.
 * Behind FlagKey.CONSUMPTION_LOG.
 */
@Composable
fun ConsumptionScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val caffeine by prefs.caffeineDoses.collectAsState(initial = emptySet())
    val drinks by prefs.drinkLog.collectAsState(initial = emptySet())

    val caffeineTotalMg = remember(caffeine) {
        caffeine.sumOf { it.substringAfter("|").toIntOrNull() ?: 0 }
    }
    val drinkTotalUnits = remember(drinks) {
        drinks.sumOf { it.substringAfter("|").toDoubleOrNull() ?: 0.0 }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("consumption"),
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
                    .testTag("consumption-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("today.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "rolling 24h. older entries auto-prune.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("caffeine", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$caffeineTotalMg mg",
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Normal),
                color = if (caffeineTotalMg > 0) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("consumption-caffeine-total"),
            )
            Spacer(Modifier.height(12.dp))
            QuickRow(
                presets = listOf("+espresso 65" to 65, "+drip 95" to 95, "+cold brew 200" to 200, "+tea 40" to 40),
                tagPrefix = "consumption-caffeine",
                onClick = { mg -> scope.launch { prefs.logCaffeine(mg) } },
            )
            if (caffeineTotalMg > 0) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "clear",
                    style = captionStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("consumption-caffeine-clear")
                        .clickable { scope.launch { prefs.clearCaffeineLog() } }
                        .padding(8.dp),
                )
            }

            Spacer(Modifier.height(40.dp))
            Text("drinks", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "%.1f units".format(drinkTotalUnits),
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Normal),
                color = if (drinkTotalUnits > 0) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("consumption-drinks-total"),
            )
            Spacer(Modifier.height(12.dp))
            QuickRowDouble(
                presets = listOf("+beer 1.0" to 1.0, "+wine 1.0" to 1.0, "+cocktail 1.5" to 1.5, "+double 2.0" to 2.0),
                tagPrefix = "consumption-drinks",
                onClick = { units -> scope.launch { prefs.logDrink(units) } },
            )
            if (drinkTotalUnits > 0) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "clear",
                    style = captionStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("consumption-drinks-clear")
                        .clickable { scope.launch { prefs.clearDrinkLog() } }
                        .padding(8.dp),
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickRow(
    presets: List<Pair<String, Int>>,
    tagPrefix: String,
    onClick: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        presets.forEachIndexed { i, (label, value) ->
            Text(
                label,
                style = bodyStyle.copy(fontSize = 16.sp),
                color = MinimalTheme.accent,
                modifier = Modifier
                    .testTag("$tagPrefix-preset-$i")
                    .clickable { onClick(value) }
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickRowDouble(
    presets: List<Pair<String, Double>>,
    tagPrefix: String,
    onClick: (Double) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        presets.forEachIndexed { i, (label, value) ->
            Text(
                label,
                style = bodyStyle.copy(fontSize = 16.sp),
                color = MinimalTheme.accent,
                modifier = Modifier
                    .testTag("$tagPrefix-preset-$i")
                    .clickable { onClick(value) }
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            )
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
