package com.erluxman.focuslauncher.ui.promises

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.local.AppDatabase
import com.erluxman.focuslauncher.data.repository.TodoRepository
import com.erluxman.focuslauncher.service.social.PromiseRatio
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * Promise kept ratio — todos completed / (completed + stale).
 * Behind FlagKey.PROMISE_RATIO.
 */
@Composable
fun PromiseRatioScreen(
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val repo = remember { TodoRepository(AppDatabase.getDatabase(context).todoDao()) }
    val todos by repo.allTodos.collectAsState(initial = emptyList())
    val snapshot = remember(todos) { PromiseRatio.compute(todos) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("promises"),
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
                    .testTag("promises-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("promises.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "todos you wrote vs todos you kept. older than 7 days = broken.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(40.dp))
            Text("ratio kept", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${snapshot.ratioPct}%",
                style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Normal),
                color = if (snapshot.ratioPct >= 70) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("promises-ratio"),
            )

            Spacer(Modifier.height(32.dp))
            Stat("kept", snapshot.kept, "promises-kept", emphasis = snapshot.kept > 0)
            Stat("broken", snapshot.broken, "promises-broken", emphasis = false)
            Stat("pending", snapshot.pending, "promises-pending", emphasis = false)

            Spacer(Modifier.height(40.dp))
            Text(
                text = when {
                    snapshot.kept + snapshot.broken == 0 -> "no completed or stale todos yet."
                    snapshot.ratioPct >= 80 -> "you keep what you write down."
                    snapshot.ratioPct >= 50 -> "more than half. better than most."
                    else -> "you write down more than you keep. write less, finish more."
                },
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
                modifier = Modifier.testTag("promises-verdict"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Stat(label: String, value: Int, tag: String, emphasis: Boolean) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "$value",
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
            color = if (emphasis) MinimalTheme.accent else MinimalTheme.fg,
            modifier = Modifier.testTag(tag),
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
