package com.erluxman.focuslauncher.ui.legacy

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
import com.erluxman.focuslauncher.data.repository.TodoRepository
import com.erluxman.focuslauncher.service.insights.LegacyCounter
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.flow.first

/**
 * Cumulative "builder minutes" you've put in. Completed todos × 15min,
 * focus sessions × 25min. Pure aggregator over Room + prefs.
 * Behind FlagKey.LEGACY_COUNTER.
 */
@Composable
fun LegacyCounterScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val focusSessionsToday by prefs.focusSessionsToday.collectAsState(initial = 0)

    val completedTodos by produceState(initialValue = 0, key1 = Unit) {
        value = runCatching {
            val repo = TodoRepository(AppDatabase.getDatabase(context).todoDao())
            repo.allTodos.first().count { it.isCompleted }
        }.getOrDefault(0)
    }

    val totalMinutes = remember(completedTodos, focusSessionsToday) {
        LegacyCounter.totalBuilderMinutes(completedTodos, focusSessionsToday)
    }
    val pretty = remember(totalMinutes) { LegacyCounter.format(totalMinutes) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("legacy"),
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
                    .testTag("legacy-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("legacy.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "every completed todo + focus session you've banked.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))
            Text("builder minutes", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = pretty,
                style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Normal),
                color = MinimalTheme.accent,
                modifier = Modifier.testTag("legacy-total"),
            )

            Spacer(Modifier.height(32.dp))
            Stat("completed todos", completedTodos, "legacy-todos")
            Stat("focus sessions today", focusSessionsToday, "legacy-focus")

            Spacer(Modifier.height(32.dp))
            Text(
                text = "todos count for ${LegacyCounter.TODO_MIN} min each. focus sessions for ${LegacyCounter.FOCUS_MIN}.",
                style = captionStyle,
                color = MinimalTheme.outline.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Stat(label: String, value: Int, tag: String) {
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
