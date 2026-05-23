package com.erluxman.focuslauncher.ui.outdoor

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
import com.erluxman.focuslauncher.data.prefs.logOutdoor
import com.erluxman.focuslauncher.data.prefs.outdoorLog
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * LOC-005 — Outdoor minutes (manual quick-log). The full spec uses
 * GPS+steps+light fusion; this is the lightest shippable version.
 * Behind FlagKey.OUTDOOR.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OutdoorScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val raw by prefs.outdoorLog.collectAsState(initial = emptySet())
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val last7 = remember {
        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        (0..6).map {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_MONTH, -it)
            fmt.format(c.time)
        }
    }
    val byDate = remember(raw) {
        raw.mapNotNull { e ->
            val parts = e.split("|", limit = 2)
            val d = parts.getOrNull(0) ?: return@mapNotNull null
            val m = parts.getOrNull(1)?.toIntOrNull() ?: return@mapNotNull null
            d to m
        }.toMap()
    }
    val todayMin = byDate[today] ?: 0
    val weekTotal = last7.sumOf { byDate[it] ?: 0 }
    val weekDays = last7.count { (byDate[it] ?: 0) > 0 }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("outdoor"),
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
                    .testTag("outdoor-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("outdoor.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "minutes spent outside. add as you go — daylight compounds.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("today", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                "$todayMin min",
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = if (todayMin >= 30) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("outdoor-today"),
            )

            Spacer(Modifier.height(16.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(10, 20, 30, 60).forEachIndexed { i, v ->
                    Text(
                        "+$v",
                        style = bodyStyle,
                        color = MinimalTheme.accent,
                        modifier = Modifier
                            .testTag("outdoor-preset-$i")
                            .clickable {
                                scope.launch { prefs.logOutdoor(today, todayMin + v) }
                            }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
            Text("last 7 days", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                "$weekTotal minutes across $weekDays day${if (weekDays == 1) "" else "s"}.",
                style = bodyStyle,
                color = MinimalTheme.fg,
                modifier = Modifier.testTag("outdoor-week"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
