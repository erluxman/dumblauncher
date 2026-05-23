package com.erluxman.focuslauncher.ui.export

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
import com.erluxman.focuslauncher.data.repository.JournalRepository
import com.erluxman.focuslauncher.data.repository.ProjectRepository
import com.erluxman.focuslauncher.data.repository.TodoRepository
import com.erluxman.focuslauncher.service.launcher.ExportBuilder
import com.erluxman.focuslauncher.service.launcher.ExportSnapshot
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Minimal data-export surface. Tap "json" or "csv" → fires an ACTION_SEND
 * intent with the dump as text. Behind FlagKey.DATA_EXPORT.
 */
@Composable
fun ExportScreen(
    prefs: UserPrefs,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { AppDatabase.getDatabase(context) }
    val todos = remember { TodoRepository(db.todoDao()) }
    val projects = remember { ProjectRepository(db.projectDao()) }
    val journal = remember { JournalRepository(db.journalDao()) }
    var status by remember { mutableStateOf("") }

    fun share(mime: String, content: String, fileLabel: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_SUBJECT, "FocusLauncher export — $fileLabel")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(intent, "share export").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    suspend fun snapshot(): ExportSnapshot = ExportSnapshot(
        whyHere = prefs.whyHere.first(),
        mantra = prefs.mantraPhrase.first(),
        dailyTargetMin = prefs.dailyTargetMin.first(),
        streakDays = prefs.streakDays.first(),
        streakBest = prefs.streakBest.first(),
        vipContacts = prefs.vipContacts.first(),
        distractionPackages = prefs.distractionPackages.first(),
        todos = todos.allTodos.first(),
        projects = projects.activeProjects.first(),
        journal = journal.recent.first(),
    )

    Surface(
        modifier = Modifier.fillMaxSize().testTag("export"),
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
                    .testTag("export-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("export.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "your data, on demand. shares via the system picker.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))

            Text(
                "share as json",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("export-json")
                    .clickable {
                        scope.launch {
                            val s = snapshot()
                            val text = ExportBuilder.buildJson(s, System.currentTimeMillis())
                            share("application/json", text, "json")
                            status = "shared ${text.length} bytes as json"
                        }
                    }
                    .padding(vertical = 14.dp),
            )

            Text(
                "share as csv",
                style = bodyStyle,
                color = MinimalTheme.accent,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("export-csv")
                    .clickable {
                        scope.launch {
                            val s = snapshot()
                            val text = ExportBuilder.buildCsv(s, System.currentTimeMillis())
                            share("text/csv", text, "csv")
                            status = "shared ${text.length} bytes as csv"
                        }
                    }
                    .padding(vertical = 14.dp),
            )

            if (status.isNotBlank()) {
                Spacer(Modifier.height(24.dp))
                Text(status, style = captionStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("export-status"))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
