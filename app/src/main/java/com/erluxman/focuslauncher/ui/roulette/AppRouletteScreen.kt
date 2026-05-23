package com.erluxman.focuslauncher.ui.roulette

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
import com.erluxman.focuslauncher.data.prefs.addRouletteApp
import com.erluxman.focuslauncher.data.prefs.removeRouletteApp
import com.erluxman.focuslauncher.data.prefs.roulettePickApp
import com.erluxman.focuslauncher.data.prefs.roulettePickDate
import com.erluxman.focuslauncher.data.prefs.roulettePool
import com.erluxman.focuslauncher.data.prefs.spinRoulette
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * RESTRICT-013 — App roulette. Pick up to 3 apps; the system grants one
 * per day. Behind FlagKey.ROULETTE.
 */
@Composable
fun AppRouletteScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pool by prefs.roulettePool.collectAsState(initial = emptySet())
    val pickDate by prefs.roulettePickDate.collectAsState(initial = "")
    val pickApp by prefs.roulettePickApp.collectAsState(initial = "")
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val freshPick = pickDate == today && pickApp.isNotBlank()
    var name by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("roulette"),
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
                    .testTag("roulette-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("app roulette.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "pick up to 3. system grants one per day.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("today's pick", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                if (freshPick) pickApp else "(none yet — spin to draw)",
                style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Normal),
                color = if (freshPick) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier.testTag("roulette-pick"),
            )

            Spacer(Modifier.height(16.dp))
            Text(
                if (freshPick) "already spun today" else "spin",
                style = bodyStyle,
                color = if (pool.isEmpty() || freshPick) MinimalTheme.outline else MinimalTheme.accent,
                modifier = Modifier
                    .testTag("roulette-spin")
                    .clickable {
                        if (pool.isNotEmpty() && !freshPick) {
                            scope.launch { prefs.spinRoulette(today) }
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            Spacer(Modifier.height(32.dp))
            Text("pool (${pool.size}/3)", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            if (pool.isEmpty()) {
                Text("nothing.", style = bodyStyle, color = MinimalTheme.outline)
            } else {
                pool.forEach { app ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).testTag("roulette-row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(app, style = bodyStyle, color = MinimalTheme.fg)
                        Text("×", style = bodyStyle, color = MinimalTheme.outline,
                            modifier = Modifier.clickable { scope.launch { prefs.removeRouletteApp(app) } }.padding(8.dp))
                    }
                }
            }

            if (pool.size < 3) {
                Spacer(Modifier.height(16.dp))
                BasicTextField(
                    value = name,
                    onValueChange = { name = it.take(40) },
                    singleLine = true,
                    textStyle = bodyStyle.copy(color = MinimalTheme.fg),
                    cursorBrush = SolidColor(MinimalTheme.accent),
                    modifier = Modifier.fillMaxWidth().testTag("roulette-input"),
                    decorationBox = { inner ->
                        Column {
                            if (name.isEmpty()) {
                                Text("app name", style = bodyStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
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
                Spacer(Modifier.height(8.dp))
                Text(
                    "add to pool",
                    style = bodyStyle,
                    color = if (name.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("roulette-add")
                        .clickable {
                            if (name.isNotBlank()) {
                                scope.launch { prefs.addRouletteApp(name.trim()) }
                                name = ""
                            }
                        }
                        .padding(vertical = 8.dp),
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
