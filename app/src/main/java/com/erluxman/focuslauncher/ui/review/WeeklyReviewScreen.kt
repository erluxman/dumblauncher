package com.erluxman.focuslauncher.ui.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.meditationLog
import com.erluxman.focuslauncher.data.prefs.readingLog
import com.erluxman.focuslauncher.data.prefs.workoutLog
import com.erluxman.focuslauncher.service.fitness.WorkoutLog
import com.erluxman.focuslauncher.service.habits.MeditationLog
import com.erluxman.focuslauncher.service.habits.ReadingLog
import com.erluxman.focuslauncher.service.insights.WeeklyReview
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Last 7 days at a glance. Sentence-only.
 * Behind FlagKey.WEEKLY_REVIEW.
 */
@Composable
fun WeeklyReviewScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val last7 = remember {
        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        (0..6).map { offset ->
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_MONTH, -offset)
            fmt.format(c.time)
        }
    }

    val medEntries by prefs.meditationLog.collectAsState(initial = emptySet())
    val readEntries by prefs.readingLog.collectAsState(initial = emptySet())
    val workEntries by prefs.workoutLog.collectAsState(initial = emptySet())

    val summary = remember(medEntries, readEntries, workEntries) {
        WeeklyReview.summarize(
            last7DaysIso = last7,
            meditation = MeditationLog.parse(medEntries),
            reading = ReadingLog.parse(readEntries),
            workout = WorkoutLog.parse(workEntries),
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("weekly-review"),
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
                    .testTag("review-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("week in review.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "last 7 days. nothing more, nothing less.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))

            Line("meditation", summary.meditationMin, summary.meditationDays, "review-med")
            Line("reading", summary.readingMin, summary.readingDays, "review-read")
            Line("workout", summary.workoutMin, summary.workoutDays, "review-work")

            Spacer(Modifier.height(40.dp))
            val totalActiveDays = listOf(summary.meditationDays, summary.readingDays, summary.workoutDays).sum()
            Text(
                text = if (totalActiveDays == 0) "no signal yet. log something."
                else "$totalActiveDays domain-days of active practice.",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("review-headline"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Line(label: String, minutes: Int, days: Int, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (minutes == 0) "nothing yet."
            else "$minutes minutes across $days day${if (days == 1) "" else "s"}.",
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Normal),
            color = if (minutes > 0) MinimalTheme.fg else MinimalTheme.outline,
            modifier = Modifier.testTag(tag),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
