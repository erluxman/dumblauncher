package com.erluxman.focuslauncher.ui.quarterly

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.commitLog
import com.erluxman.focuslauncher.data.prefs.meditationLog
import com.erluxman.focuslauncher.data.prefs.readingLog
import com.erluxman.focuslauncher.data.prefs.workoutLog
import com.erluxman.focuslauncher.service.fitness.WorkoutLog
import com.erluxman.focuslauncher.service.habits.CommitLog
import com.erluxman.focuslauncher.service.habits.MeditationLog
import com.erluxman.focuslauncher.service.habits.ReadingLog
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * LIFE-004 — Quarterly Self-Audit. 90-day rollup of the four daily logs +
 * streak best. Behind FlagKey.QUARTERLY.
 */
@Composable
fun QuarterlyAuditScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val last90Iso = remember {
        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        (0..89).map {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_MONTH, -it)
            fmt.format(c.time)
        }.toSet()
    }
    val commitEntries by prefs.commitLog.collectAsState(initial = emptySet())
    val medEntries by prefs.meditationLog.collectAsState(initial = emptySet())
    val readEntries by prefs.readingLog.collectAsState(initial = emptySet())
    val workEntries by prefs.workoutLog.collectAsState(initial = emptySet())
    val streakBest by prefs.streakBest.collectAsState(initial = 0)

    val commits = remember(commitEntries) {
        CommitLog.parse(commitEntries).filter { it.isoDate in last90Iso }.sumOf { it.commits }
    }
    val medMin = remember(medEntries) {
        MeditationLog.parse(medEntries).filter { it.isoDate in last90Iso }.sumOf { it.minutes }
    }
    val readMin = remember(readEntries) {
        ReadingLog.parse(readEntries).filter { it.isoDate in last90Iso }.sumOf { it.minutes }
    }
    val workMin = remember(workEntries) {
        WorkoutLog.parse(workEntries).filter { it.isoDate in last90Iso }.sumOf { it.minutes }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("quarterly"),
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
                    .testTag("quarterly-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("90-day audit.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "the comp-report version. ship this quarterly to your future you.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))
            Row("commits", "$commits", "quarterly-commits")
            Row("meditation minutes", "$medMin", "quarterly-med")
            Row("reading minutes", "$readMin", "quarterly-read")
            Row("workout minutes", "$workMin", "quarterly-work")
            Row("best streak", "$streakBest days", "quarterly-streak")

            Spacer(Modifier.height(40.dp))
            Text(
                text = when {
                    commits + medMin + readMin + workMin == 0 ->
                        "nothing logged this quarter. that is also data."
                    commits + medMin + readMin + workMin > 1000 ->
                        "compounding. don't break it."
                    else -> "directionally right. tighten the loop."
                },
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("quarterly-verdict"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Row(label: String, value: String, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(2.dp))
        Text(value, style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
            color = MinimalTheme.fg, modifier = Modifier.testTag(tag))
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
