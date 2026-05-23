package com.erluxman.focuslauncher.ui.wrapped

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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
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
import java.util.Calendar

/**
 * Built Wrapped — year-in-review recap, sentence-only. Aggregates whatever
 * per-day logs exist (commits / meditation / reading / workout) plus the
 * project repo to feed BuiltWrapped.headline + .score.
 *
 * Behind FlagKey.BUILT_WRAPPED.
 */
@Composable
fun WrappedScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val year = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val yearPrefix = "$year-"

    val commitEntries by prefs.commitLog.collectAsState(initial = emptySet())
    val medEntries by prefs.meditationLog.collectAsState(initial = emptySet())
    val readEntries by prefs.readingLog.collectAsState(initial = emptySet())
    val workEntries by prefs.workoutLog.collectAsState(initial = emptySet())
    val streakBest by prefs.streakBest.collectAsState(initial = 0)

    val commitsThisYear = remember(commitEntries, yearPrefix) {
        CommitLog.parse(commitEntries)
            .filter { it.isoDate.startsWith(yearPrefix) }
            .sumOf { it.commits }
    }
    val medMinThisYear = remember(medEntries, yearPrefix) {
        MeditationLog.parse(medEntries)
            .filter { it.isoDate.startsWith(yearPrefix) }
            .sumOf { it.minutes }
    }
    val readMinThisYear = remember(readEntries, yearPrefix) {
        ReadingLog.parse(readEntries)
            .filter { it.isoDate.startsWith(yearPrefix) }
            .sumOf { it.minutes }
    }
    val workMinThisYear = remember(workEntries, yearPrefix) {
        WorkoutLog.parse(workEntries)
            .filter { it.isoDate.startsWith(yearPrefix) }
            .sumOf { it.minutes }
    }

    // Heuristic only — no per-session focus log yet.
    val focusMinutes = medMinThisYear + workMinThisYear

    val projectsShipped by produceState(initialValue = 0, key1 = year) {
        value = runCatching {
            val db = AppDatabase.getDatabase(context)
            val repo = ProjectRepository(db.projectDao())
            repo.activeProjects.first().count { it.progress >= 1f }
        }.getOrDefault(0)
    }

    val yearAgg = BuiltWrapped.Year(
        totalFocusMinutes = focusMinutes,
        totalDistractionMinutes = 0,
        projectsShipped = projectsShipped,
        streakBest = streakBest,
        bookCount = (readMinThisYear / 300).coerceAtLeast(0),  // ~5h / book proxy
    )
    val headline = BuiltWrapped.headline(yearAgg)
    val score = BuiltWrapped.score(yearAgg)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("wrapped"),
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
                    .testTag("wrapped-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(40.dp))
            Text("$year, wrapped.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "what you've built this year.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))
            Text("score", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$score",
                style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("wrapped-score"),
            )

            Spacer(Modifier.height(32.dp))
            Text(
                text = headline,
                style = bodyStyle,
                color = MinimalTheme.fg,
                modifier = Modifier.testTag("wrapped-headline"),
            )

            Spacer(Modifier.height(40.dp))

            Section("focus minutes (meditation + workout)", focusMinutes, "wrapped-focus")
            Section("commits", commitsThisYear, "wrapped-commits")
            Section("reading minutes", readMinThisYear, "wrapped-reading")
            Section("books (read-time proxy)", yearAgg.bookCount, "wrapped-books")
            Section("projects shipped", projectsShipped, "wrapped-projects")
            Section("best streak", streakBest, "wrapped-best-streak")

            Spacer(Modifier.height(48.dp))
            Text(
                "distraction tracking + per-session focus log land next; numbers above use only what we already record.",
                style = TextStyle(fontSize = 12.sp),
                color = MinimalTheme.outline.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Section(label: String, value: Int, tag: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "$value",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
            color = MinimalTheme.fg,
            modifier = Modifier.testTag(tag),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
