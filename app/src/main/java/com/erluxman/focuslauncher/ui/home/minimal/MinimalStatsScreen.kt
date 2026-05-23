package com.erluxman.focuslauncher.ui.home.minimal

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.commitLog
import com.erluxman.focuslauncher.data.prefs.meditationLog
import com.erluxman.focuslauncher.data.prefs.mortalityWidgetsOptIn
import com.erluxman.focuslauncher.data.prefs.readingLog
import com.erluxman.focuslauncher.data.prefs.workoutLog
import com.erluxman.focuslauncher.service.habits.MeditationLog
import com.erluxman.focuslauncher.service.habits.ReadingLog
import com.erluxman.focuslauncher.service.fitness.WorkoutLog
import com.erluxman.focuslauncher.service.launcher.HealthSource
import com.erluxman.focuslauncher.service.mortality.ActuarialMath
import com.erluxman.focuslauncher.service.tracks.GraduateState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Sentence-only dashboard. One colour, opacity for hierarchy. Reached
 * via swipe-down from the minimal home; swipe-up returns.
 */
@Composable
fun MinimalStatsScreen(
    prefs: UserPrefs,
    onReturn: () -> Unit,
    onOpenTransparency: () -> Unit,
    onOpenUninstall: () -> Unit,
    onOpenDashboard: () -> Unit,
) {
    val context = LocalContext.current
    val now = remember { Date() }
    val timeStr = remember(now) { SimpleDateFormat("HH:mm", Locale.US).format(now) }
    val todayIso = remember(now) { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(now) }
    val last7 = remember(todayIso) {
        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        (0..6).map { offset ->
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_MONTH, -offset)
            fmt.format(c.time)
        }
    }

    val streakDays by prefs.streakDays.collectAsState(initial = 0)
    val commitEntries by prefs.commitLog.collectAsState(initial = emptySet())
    val meditation by prefs.meditationLog.collectAsState(initial = emptySet())
    val reading by prefs.readingLog.collectAsState(initial = emptySet())
    val workout by prefs.workoutLog.collectAsState(initial = emptySet())
    val mortalityOptIn by prefs.mortalityWidgetsOptIn.collectAsState(initial = false)
    val userAge by prefs.userAge.collectAsState(initial = 0)
    val trackLevel by prefs.trackLevel.collectAsState(initial = 1)
    val onboardingCompletedAt by prefs.onboardingCompletedAt.collectAsState(initial = 0L)

    val healthMins by produceState(initialValue = 0 to 0, key1 = todayIso) {
        value = runCatching {
            HealthSource.todaySteps(context) to HealthSource.lastNightSleepMinutes(context)
        }.getOrDefault(0 to 0)
    }

    val medSessions = remember(meditation) { MeditationLog.parse(meditation) }
    val readSessions = remember(reading) { ReadingLog.parse(reading) }
    val workSessions = remember(workout) { WorkoutLog.parse(workout) }

    val todayMedMin = MeditationLog.minutesOn(todayIso, medSessions)
    val weekMedMin = MeditationLog.minutesIn(last7, medSessions)
    val weekMedDays = last7.count { d -> medSessions.any { it.isoDate == d } }
    val weekReadMin = ReadingLog.minutesIn(last7, readSessions)
    val weekReadDays = last7.count { d -> readSessions.any { it.isoDate == d } }
    val weekGymDays = last7.count { d -> workSessions.any { it.isoDate == d } }

    val commitsToday = remember(commitEntries, todayIso) {
        commitEntries.firstOrNull { it.startsWith("$todayIso|") }
            ?.substringAfter("|")?.toIntOrNull() ?: 0
    }

    val gradStat = remember(trackLevel, onboardingCompletedAt) {
        GraduateState.compute(trackLevel, onboardingCompletedAt)
    }
    val beachSat = remember(userAge) {
        if (userAge in 1..120) ActuarialMath.beachSaturdaysRemaining(userAge) else 0
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("minimal-stats"),
        color = MinimalTheme.bg,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dy ->
                        if (dy < -40f) onReturn()
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(48.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(timeStr, style = captionStyle, color = MinimalTheme.outline)
                    Text(
                        "return",
                        style = captionStyle,
                        color = MinimalTheme.outline,
                        modifier = Modifier
                            .testTag("stats-return")
                            .padding(4.dp)
                            .pointerInput(Unit) { detectTapGestures { onReturn() } },
                    )
                }
                Spacer(Modifier.height(40.dp))

                Group("today") {
                    if (streakDays > 0) {
                        val word = if (streakDays == 1) "day" else "days"
                        Line("$streakDays $word of work.")
                    }
                    if (healthMins.second > 0) {
                        val h = healthMins.second / 60
                        val m = healthMins.second % 60
                        Line("${h}h ${m}m asleep last night.")
                    }
                    if (commitsToday > 0) {
                        val word = if (commitsToday == 1) "commit" else "commits"
                        Line("$commitsToday $word.")
                    }
                    if (todayMedMin > 0) Line("$todayMedMin minutes of meditation.")
                    if (healthMins.first > 0) Line("${"%,d".format(healthMins.first)} steps so far.")
                }

                Group("this week") {
                    if (weekMedMin > 0) Line("$weekMedMin minutes meditation across $weekMedDays days.")
                    if (weekReadMin > 0) Line("$weekReadMin minutes reading across $weekReadDays days.")
                    if (weekGymDays > 0) {
                        val word = if (weekGymDays == 1) "day" else "days"
                        Line("$weekGymDays $word at the gym.")
                    }
                }

                if (gradStat.atTopLevel || (mortalityOptIn && beachSat > 0)) {
                    Group("this year") {
                        if (gradStat.atTopLevel && gradStat.daysRemaining > 0) {
                            Line("${gradStat.daysRemaining} days to graduate.")
                        }
                        if (gradStat.isGraduate) Line("graduate.")
                        if (mortalityOptIn && beachSat > 0) {
                            Line("$beachSat beach saturdays left in your life.")
                        }
                    }
                }

                Spacer(Modifier.height(48.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FooterLink("transparency", onOpenTransparency, "stats-transparency")
                    FooterLink("uninstall", onOpenUninstall, "stats-uninstall")
                    FooterLink("dashboard", onOpenDashboard, "stats-dashboard")
                }
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun Group(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
        Text(title, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(16.dp))
        content()
    }
}

@Composable
private fun Line(text: String) {
    Text(
        text = text,
        style = bodyStyle,
        color = MinimalTheme.fg,
        modifier = Modifier.padding(vertical = 4.dp).testTag("stats-line"),
    )
}

private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)

@Composable
private fun FooterLink(label: String, onClick: () -> Unit, tag: String) {
    Text(
        text = label,
        style = captionStyle,
        color = MinimalTheme.outline,
        modifier = Modifier
            .padding(4.dp)
            .testTag(tag)
            .pointerInput(Unit) { detectTapGestures { onClick() } },
    )
}
