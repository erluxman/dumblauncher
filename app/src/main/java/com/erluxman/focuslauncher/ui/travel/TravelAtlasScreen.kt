package com.erluxman.focuslauncher.ui.travel

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.addTravel
import com.erluxman.focuslauncher.data.prefs.removeTravel
import com.erluxman.focuslauncher.data.prefs.travelAtlas
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * Where you've been. "year|location" entries.
 * Behind FlagKey.TRAVEL_ATLAS.
 */
@Composable
fun TravelAtlasScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val thisYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    var yearTxt by remember { mutableStateOf("$thisYear") }
    var place by remember { mutableStateOf("") }
    val raw by prefs.travelAtlas.collectAsState(initial = emptySet())

    val grouped = remember(raw) {
        raw.mapNotNull { entry ->
            val parts = entry.split("|", limit = 2)
            val y = parts.getOrNull(0)?.toIntOrNull() ?: return@mapNotNull null
            val loc = parts.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            Triple(entry, y, loc)
        }.groupBy { it.second }.toSortedMap(compareByDescending { it })
    }

    val totalPlaces = raw.size
    val years = grouped.size

    Surface(
        modifier = Modifier.fillMaxSize().testTag("travel"),
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
                    .testTag("travel-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("travel atlas.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "$totalPlaces place${if (totalPlaces == 1) "" else "s"} across $years year${if (years == 1) "" else "s"}.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                MinimalField(yearTxt, { yearTxt = it.filter { c -> c.isDigit() }.take(4) }, "year", "travel-add-year", KeyboardType.Number, Modifier.width(100.dp))
                Spacer(Modifier.width(16.dp))
                MinimalField(place, { place = it.take(60) }, "place", "travel-add-place", KeyboardType.Text, Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "add",
                style = bodyStyle,
                color = if (place.isNotBlank() && yearTxt.toIntOrNull() != null) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("travel-save")
                    .clickable {
                        val y = yearTxt.toIntOrNull() ?: return@clickable
                        if (place.isBlank()) return@clickable
                        scope.launch { prefs.addTravel(y, place.trim()) }
                        place = ""
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            if (grouped.isEmpty()) {
                Text("nothing yet.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                grouped.forEach { (year, entries) ->
                    Text("$year", style = captionStyle, color = MinimalTheme.outline)
                    Spacer(Modifier.height(8.dp))
                    entries.forEach { (entry, _, loc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .testTag("travel-row"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(loc, style = bodyStyle, color = MinimalTheme.fg)
                            Text(
                                "×",
                                style = bodyStyle,
                                color = MinimalTheme.outline,
                                modifier = Modifier
                                    .clickable { scope.launch { prefs.removeTravel(entry) } }
                                    .padding(8.dp),
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun MinimalField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    tag: String,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg),
            cursorBrush = SolidColor(MinimalTheme.accent),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth().testTag(tag),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(placeholder, style = bodyStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
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
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
