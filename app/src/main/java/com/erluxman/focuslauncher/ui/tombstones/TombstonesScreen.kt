package com.erluxman.focuslauncher.ui.tombstones

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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * App tombstones — apps you killed. Behind FlagKey.TOMBSTONES.
 */
@Composable
fun TombstonesScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    val raw by prefs.appTombstones.collectAsState(initial = emptySet())

    val rows = remember(raw) {
        raw.mapNotNull { entry ->
            val parts = entry.split("|", limit = 2)
            val label = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val date = parts.getOrNull(1).orEmpty()
            Triple(entry, label, date)
        }.sortedByDescending { it.third }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("tombstones"),
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
                    .testTag("tombstones-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("tombstones.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "apps you killed. ${rows.size} so far.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(24.dp))
            BasicTextField(
                value = name,
                onValueChange = { name = it.take(40) },
                singleLine = true,
                textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                cursorBrush = SolidColor(MinimalTheme.accent),
                modifier = Modifier.fillMaxWidth().testTag("tombstones-input"),
                decorationBox = { inner ->
                    Column {
                        if (name.isEmpty()) {
                            Text(
                                "app name",
                                style = bodyStyle.copy(fontSize = 16.sp),
                                color = MinimalTheme.outline.copy(alpha = 0.6f),
                            )
                        }
                        inner()
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MinimalTheme.outline.copy(alpha = 0.4f))
                        )
                    }
                },
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "bury",
                style = bodyStyle,
                color = if (name.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("tombstones-save")
                    .clickable {
                        if (name.isNotBlank()) {
                            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                            scope.launch { prefs.addTombstone(name.trim(), today) }
                            name = ""
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(24.dp))
            if (rows.isEmpty()) {
                Text("nothing buried.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                rows.forEach { (entry, label, date) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .testTag("tombstones-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("💀 $label", style = bodyStyle, color = MinimalTheme.fg)
                            if (date.isNotBlank()) {
                                Text(date, style = captionStyle, color = MinimalTheme.outline)
                            }
                        }
                        Text(
                            "×",
                            style = bodyStyle,
                            color = MinimalTheme.outline,
                            modifier = Modifier
                                .clickable { scope.launch { prefs.removeTombstone(entry) } }
                                .padding(8.dp),
                        )
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
