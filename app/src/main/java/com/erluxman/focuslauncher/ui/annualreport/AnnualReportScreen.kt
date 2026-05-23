package com.erluxman.focuslauncher.ui.annualreport

import android.content.Intent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.local.AppDatabase
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.commitLog
import com.erluxman.focuslauncher.data.prefs.meditationLog
import com.erluxman.focuslauncher.data.prefs.readingLog
import com.erluxman.focuslauncher.data.prefs.workoutLog
import com.erluxman.focuslauncher.data.repository.ProjectRepository
import com.erluxman.focuslauncher.service.fitness.WorkoutLog
import com.erluxman.focuslauncher.service.habits.CommitLog
import com.erluxman.focuslauncher.service.habits.MeditationLog
import com.erluxman.focuslauncher.service.habits.ReadingLog
import com.erluxman.focuslauncher.service.mortality.BuiltWrapped
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * LIFE-005 — Personal Annual Report. Plain-text composite of the year's
 * numbers; share via system picker. Behind FlagKey.ANNUAL_REPORT.
 */
@Composable
fun AnnualReportScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val year = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val yearPrefix = "$year-"
    val commitEntries by prefs.commitLog.collectAsState(initial = emptySet())
    val medEntries by prefs.meditationLog.collectAsState(initial = emptySet())
    val readEntries by prefs.readingLog.collectAsState(initial = emptySet())
    val workEntries by prefs.workoutLog.collectAsState(initial = emptySet())
    val streakBest by prefs.streakBest.collectAsState(initial = 0)
    var status by remember { mutableStateOf<String?>(null) }

    val commits = remember(commitEntries) {
        CommitLog.parse(commitEntries).filter { it.isoDate.startsWith(yearPrefix) }.sumOf { it.commits }
    }
    val medMin = remember(medEntries) {
        MeditationLog.parse(medEntries).filter { it.isoDate.startsWith(yearPrefix) }.sumOf { it.minutes }
    }
    val readMin = remember(readEntries) {
        ReadingLog.parse(readEntries).filter { it.isoDate.startsWith(yearPrefix) }.sumOf { it.minutes }
    }
    val workMin = remember(workEntries) {
        WorkoutLog.parse(workEntries).filter { it.isoDate.startsWith(yearPrefix) }.sumOf { it.minutes }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("annual-report"),
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
                    .testTag("annual-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("$year — annual report.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "what a public company files annually. for your life.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Row("commits", "$commits", "annual-commits")
            Row("meditation minutes", "$medMin", "annual-med")
            Row("reading minutes", "$readMin", "annual-read")
            Row("workout minutes", "$workMin", "annual-work")
            Row("best streak", "$streakBest days", "annual-streak")

            Spacer(Modifier.height(32.dp))
            Text(
                "share as text",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .testTag("annual-share")
                    .clickable {
                        scope.launch {
                            val shippedProjects = runCatching {
                                ProjectRepository(AppDatabase.getDatabase(context).projectDao())
                                    .activeProjects.first().count { it.progress >= 1f }
                            }.getOrDefault(0)
                            val yr = BuiltWrapped.Year(
                                totalFocusMinutes = medMin + workMin,
                                totalDistractionMinutes = 0,
                                projectsShipped = shippedProjects,
                                streakBest = streakBest,
                                bookCount = (readMin / 300).coerceAtLeast(0),
                            )
                            val text = buildString {
                                appendLine("$year — Annual Report")
                                appendLine()
                                appendLine("Score: ${BuiltWrapped.score(yr)}")
                                appendLine(BuiltWrapped.headline(yr))
                                appendLine()
                                appendLine("Commits:  $commits")
                                appendLine("Meditation:  ${medMin} min")
                                appendLine("Reading:  ${readMin} min")
                                appendLine("Workout:  ${workMin} min")
                                appendLine("Best streak:  ${streakBest} days")
                                appendLine("Projects shipped:  $shippedProjects")
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, text)
                                putExtra(Intent.EXTRA_SUBJECT, "$year — FocusLauncher Annual Report")
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            try {
                                context.startActivity(Intent.createChooser(intent, "share annual report").apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                })
                                status = "shared."
                            } catch (t: Throwable) {
                                status = "failed: ${t.message}"
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )
            status?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = captionStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("annual-status"))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Row(label: String, value: String, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(2.dp))
        Text(value, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
            color = MinimalTheme.fg, modifier = Modifier.testTag(tag))
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
