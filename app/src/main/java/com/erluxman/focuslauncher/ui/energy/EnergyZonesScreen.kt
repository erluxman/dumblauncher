package com.erluxman.focuslauncher.ui.energy

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
import com.erluxman.focuslauncher.service.tracks.EnergyZones
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * 6 four-hour windows, each tagged high/med/low. Highlights the current
 * window + its suggestion. Behind FlagKey.ENERGY_ZONES.
 */
@Composable
fun EnergyZonesScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val stored by prefs.energyZones.collectAsState(initial = emptySet())
    val nowHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val activeIdx = EnergyZones.windowIndex(nowHour)
    val activeEnergy = EnergyZones.activeEnergy(nowHour, stored)
    val suggestion = EnergyZones.suggestion(activeEnergy)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("energy-zones"),
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
                    .testTag("energy-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("energy zones.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "tag each 4-hour window. the suggestion adapts.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("now (${EnergyZones.WINDOW_LABELS[activeIdx]})", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = activeEnergy.name.lowercase(),
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("energy-active"),
            )
            Spacer(Modifier.height(8.dp))
            Text(suggestion, style = bodyStyle, color = MinimalTheme.fg,
                modifier = Modifier.testTag("energy-suggestion"))

            Spacer(Modifier.height(40.dp))
            Text("set", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(12.dp))
            EnergyZones.WINDOW_LABELS.forEachIndexed { i, label ->
                val current = EnergyZones.activeEnergy(i * 4, stored)
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = label,
                            style = bodyStyle.copy(fontSize = 16.sp),
                            color = if (i == activeIdx) MinimalTheme.accent else MinimalTheme.outline,
                            modifier = Modifier.padding(end = 16.dp),
                        )
                        listOf(EnergyZones.Energy.HIGH, EnergyZones.Energy.MED, EnergyZones.Energy.LOW).forEach { e ->
                            val isSelected = e == current
                            Text(
                                text = e.name.lowercase(),
                                style = bodyStyle.copy(fontSize = 16.sp),
                                color = if (isSelected) MinimalTheme.accent else MinimalTheme.outline,
                                modifier = Modifier
                                    .testTag("energy-pick-$label-${e.name}")
                                    .clickable {
                                        scope.launch { prefs.setEnergyZone(label, e.name) }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
