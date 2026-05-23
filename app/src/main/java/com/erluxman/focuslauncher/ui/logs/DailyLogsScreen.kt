package com.erluxman.focuslauncher.ui.logs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
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
import com.erluxman.focuslauncher.data.prefs.addCommits
import com.erluxman.focuslauncher.data.prefs.commitLog
import com.erluxman.focuslauncher.data.prefs.logMeditation
import com.erluxman.focuslauncher.data.prefs.logReading
import com.erluxman.focuslauncher.data.prefs.logWorkout
import com.erluxman.focuslauncher.data.prefs.meditationLog
import com.erluxman.focuslauncher.data.prefs.readingLog
import com.erluxman.focuslauncher.data.prefs.workoutLog
import com.erluxman.focuslauncher.service.fitness.WorkoutLog
import com.erluxman.focuslauncher.service.habits.CommitLog
import com.erluxman.focuslauncher.service.habits.MeditationLog
import com.erluxman.focuslauncher.service.habits.ReadingLog
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Hub for the four daily quick-logs: meditation, reading, workout, commits.
 * Each section: today's total + preset buttons. Behind FlagKey.DAILY_LOGS.
 */
@Composable
fun DailyLogsScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val today = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val medEntries by prefs.meditationLog.collectAsState(initial = emptySet())
    val readEntries by prefs.readingLog.collectAsState(initial = emptySet())
    val workEntries by prefs.workoutLog.collectAsState(initial = emptySet())
    val commitEntries by prefs.commitLog.collectAsState(initial = emptySet())

    val medToday = remember(medEntries, today) {
        MeditationLog.minutesOn(today, MeditationLog.parse(medEntries))
    }
    val readToday = remember(readEntries, today) {
        ReadingLog.minutesOn(today, ReadingLog.parse(readEntries))
    }
    val workToday = remember(workEntries, today) {
        WorkoutLog.minutesOn(today, WorkoutLog.parse(workEntries))
    }
    val commitsToday = remember(commitEntries, today) {
        CommitLog.parse(commitEntries).firstOrNull { it.isoDate == today }?.commits ?: 0
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("daily-logs"),
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
                    .testTag("logs-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("today.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "quick log. one tap, done.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            LogSection(
                title = "meditation",
                todayValue = "$medToday min",
                tagPrefix = "logs-med",
                presets = listOf(5, 10, 15, 20, 30),
            ) { mins -> scope.launch { prefs.logMeditation(today, mins, "breath") } }

            LogSection(
                title = "reading",
                todayValue = "$readToday min",
                tagPrefix = "logs-read",
                presets = listOf(10, 20, 30, 45, 60),
            ) { mins -> scope.launch { prefs.logReading(today, mins) } }

            LogSection(
                title = "workout",
                todayValue = "$workToday min",
                tagPrefix = "logs-workout",
                presets = listOf(15, 30, 45, 60, 90),
            ) { mins -> scope.launch { prefs.logWorkout(today, mins, "strength") } }

            LogSection(
                title = "commits",
                todayValue = "$commitsToday",
                tagPrefix = "logs-commits",
                presets = listOf(1, 3, 5),
            ) { n -> scope.launch { prefs.addCommits(today, n) } }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LogSection(
    title: String,
    todayValue: String,
    tagPrefix: String,
    presets: List<Int>,
    onPick: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Text(title, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            text = todayValue,
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
            color = MinimalTheme.fg,
            modifier = Modifier.testTag("$tagPrefix-today"),
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            presets.forEachIndexed { i, v ->
                Text(
                    text = "+$v",
                    style = bodyStyle,
                    color = MinimalTheme.accent,
                    modifier = Modifier
                        .testTag("$tagPrefix-preset-$i")
                        .clickable { onPick(v) }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
