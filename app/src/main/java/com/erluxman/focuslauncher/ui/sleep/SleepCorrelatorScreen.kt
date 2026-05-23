package com.erluxman.focuslauncher.ui.sleep

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.commitLog
import com.erluxman.focuslauncher.data.prefs.logSleepMinutes
import com.erluxman.focuslauncher.data.prefs.sleepLog
import com.erluxman.focuslauncher.service.habits.CommitLog
import com.erluxman.focuslauncher.service.launcher.HealthSource
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Sleep correlator — pairs nightly sleep minutes with next-day output
 * (commits) and reports a Pearson r. Honest about needing N≥4 samples.
 *
 * Today's sleep is seeded from HealthConnect once on launch; per-day
 * history persists in DataStore (sleepLog) so the correlation builds
 * over time even when HealthConnect is unavailable.
 *
 * Behind FlagKey.SLEEP_CORRELATOR.
 */
@Composable
fun SleepCorrelatorScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val todayIso = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val last14Iso = remember(todayIso) {
        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        (0..13).map { offset ->
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_MONTH, -offset)
            fmt.format(c.time)
        }
    }

    // Seed today's sleep from HealthConnect. Best-effort; failure = no write.
    LaunchedEffect(todayIso) {
        runCatching { HealthSource.lastNightSleepMinutes(context) }.getOrNull()?.let { mins ->
            if (mins > 0) prefs.logSleepMinutes(todayIso, mins)
        }
    }

    val sleepEntries by prefs.sleepLog.collectAsState(initial = emptySet())
    val commitEntries by prefs.commitLog.collectAsState(initial = emptySet())

    val sleepByDate: Map<String, Int> = remember(sleepEntries) {
        sleepEntries.mapNotNull { e ->
            val parts = e.split("|", limit = 2)
            val d = parts.getOrNull(0) ?: return@mapNotNull null
            val m = parts.getOrNull(1)?.toIntOrNull() ?: return@mapNotNull null
            d to m
        }.toMap()
    }
    val commitsByDate: Map<String, Int> = remember(commitEntries) {
        CommitLog.parse(commitEntries).associate { it.isoDate to it.commits }
    }

    // Pair sleep(d) with commits(d+1): "did good sleep give better next-day output?"
    val paired: List<Triple<String, Int, Int>> = remember(sleepByDate, commitsByDate) {
        sleepByDate.mapNotNull { (date, mins) ->
            val nextIso = nextDay(date) ?: return@mapNotNull null
            val nextCommits = commitsByDate[nextIso] ?: return@mapNotNull null
            Triple(date, mins, nextCommits)
        }.sortedByDescending { it.first }
    }

    val r = remember(paired) { pearson(paired.map { it.second.toDouble() to it.third.toDouble() }) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("sleep-correlator"),
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
                    .testTag("sleep-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("sleep ↔ output.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "how does last night's sleep predict tomorrow's commits?",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))

            if (paired.size < 4) {
                Text(
                    text = "${paired.size} paired day${if (paired.size == 1) "" else "s"} so far. need at least 4 to draw any line.",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier.testTag("sleep-need-more"),
                )
            } else {
                val verdict = when {
                    r == null -> "no correlation yet."
                    r > 0.6 -> "strong positive: sleep helps."
                    r > 0.3 -> "modest positive."
                    abs(r) <= 0.3 -> "no signal."
                    r > -0.6 -> "modest negative."
                    else -> "strong negative — odd. check the data."
                }
                Text(
                    text = verdict,
                    style = bodyStyle,
                    color = MinimalTheme.accent,
                    modifier = Modifier.testTag("sleep-verdict"),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (r == null) "r = n/a" else "r = ${"%.2f".format(r)} over ${paired.size} days",
                    style = captionStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier.testTag("sleep-pearson"),
                )
            }

            Spacer(Modifier.height(32.dp))
            Text("last 14 days", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(12.dp))

            last14Iso.forEach { iso ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .testTag("sleep-row-$iso"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(iso, style = bodyStyle.copy(fontSize = 14.sp), color = MinimalTheme.outline)
                    val mins = sleepByDate[iso]
                    val commits = commitsByDate[nextDay(iso) ?: ""]
                    val sleepStr = mins?.let { "${it / 60}h ${it % 60}m" } ?: "—"
                    val nextStr = commits?.let { "→ $it ${if (it == 1) "commit" else "commits"}" } ?: ""
                    Text(
                        text = "$sleepStr $nextStr".trim(),
                        style = bodyStyle.copy(fontSize = 14.sp),
                        color = if (mins != null) MinimalTheme.fg else MinimalTheme.outline.copy(alpha = 0.5f),
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MinimalTheme.outline.copy(alpha = 0.15f))
                )
            }

            Spacer(Modifier.height(32.dp))
            Text(
                "today's reading is seeded from health connect on launch; missing days are simply blank.",
                style = TextStyle(fontSize = 12.sp),
                color = MinimalTheme.outline.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private fun nextDay(iso: String): String? {
    val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val date = runCatching { fmt.parse(iso) }.getOrNull() ?: return null
    val cal = Calendar.getInstance().apply {
        time = date
        add(Calendar.DAY_OF_MONTH, 1)
    }
    return fmt.format(cal.time)
}

private fun pearson(pairs: List<Pair<Double, Double>>): Double? {
    if (pairs.size < 2) return null
    val n = pairs.size
    val sumX = pairs.sumOf { it.first }
    val sumY = pairs.sumOf { it.second }
    val sumXX = pairs.sumOf { it.first * it.first }
    val sumYY = pairs.sumOf { it.second * it.second }
    val sumXY = pairs.sumOf { it.first * it.second }
    val num = n * sumXY - sumX * sumY
    val den = sqrt((n * sumXX - sumX * sumX) * (n * sumYY - sumY * sumY))
    return if (den == 0.0) null else num / den
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
