package com.erluxman.focuslauncher.ui.phantom

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.bumpPhantomCheck
import com.erluxman.focuslauncher.data.prefs.phantomChecks
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * PSYCH-011 — Phantom vibration counter. Each time you check the phone
 * for no reason, tap +1. Pattern surfaces over time. Behind PHANTOM.
 */
@Composable
fun PhantomVibrationScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val raw by prefs.phantomChecks.collectAsState(initial = emptySet())
    val byDate = remember(raw) {
        raw.mapNotNull {
            val parts = it.split("|", limit = 2)
            val d = parts.getOrNull(0) ?: return@mapNotNull null
            val n = parts.getOrNull(1)?.toIntOrNull() ?: return@mapNotNull null
            d to n
        }.toMap()
    }
    val todayCount = byDate[today] ?: 0
    val last7 = remember {
        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        (0..6).map {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_MONTH, -it)
            fmt.format(c.time)
        }
    }
    val weekTotal = last7.sumOf { byDate[it] ?: 0 }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("phantom"),
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
                    .testTag("phantom-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("phantom checks.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "every time you checked your phone for no reason. self-recorded.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))
            Text("today", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                "$todayCount",
                style = TextStyle(fontSize = 80.sp, fontWeight = FontWeight.Normal),
                color = if (todayCount >= 20) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("phantom-today"),
            )

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text("+1", style = TextStyle(fontSize = 32.sp), color = MinimalTheme.accent,
                    modifier = Modifier.testTag("phantom-plus")
                        .clickable { scope.launch { prefs.bumpPhantomCheck(today, +1) } }
                        .padding(12.dp))
                Text("−1", style = TextStyle(fontSize = 32.sp), color = MinimalTheme.outline,
                    modifier = Modifier.testTag("phantom-minus")
                        .clickable { scope.launch { prefs.bumpPhantomCheck(today, -1) } }
                        .padding(12.dp))
            }

            Spacer(Modifier.height(40.dp))
            Text("last 7 days", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                "$weekTotal · avg ${weekTotal / 7}/day",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.fg,
                modifier = Modifier.testTag("phantom-week"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
