package com.erluxman.focuslauncher.ui.estimation

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
import com.erluxman.focuslauncher.service.insights.EstimationAccuracy
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlin.math.abs

/**
 * Effort estimation training. Pulls completed todos that have both
 * estimatedMinutes + actualMinutes and reports accuracy/bias/median.
 * Behind FlagKey.ESTIMATION.
 */
@Composable
fun EstimationScreen(
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val repo = remember { TodoRepository(AppDatabase.getDatabase(context).todoDao()) }
    val todos by repo.allTodos.collectAsState(initial = emptyList())

    val pairs = remember(todos) {
        todos.filter { it.isCompleted }
            .mapNotNull { t ->
                val est = t.estimatedMinutes ?: return@mapNotNull null
                val act = t.actualMinutes ?: return@mapNotNull null
                est to act
            }
    }
    val stats = remember(pairs) { EstimationAccuracy.compute(pairs) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("estimation"),
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
                    .testTag("estimation-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("estimation.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "how well do you predict how long things take?",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            if (stats.sample == 0) {
                Text(
                    "no completed todos with both estimated + actual minutes yet.",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier.testTag("estimation-empty"),
                )
            } else {
                Text("accuracy", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${stats.accuracyPct}%",
                    style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Normal),
                    color = if (stats.accuracyPct >= 70) MinimalTheme.accent else MinimalTheme.fg,
                    modifier = Modifier.testTag("estimation-accuracy"),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "over ${stats.sample} completed task${if (stats.sample == 1) "" else "s"}.",
                    style = captionStyle,
                    color = MinimalTheme.outline,
                )

                Spacer(Modifier.height(32.dp))
                Text("bias", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                val biasLabel = when {
                    stats.biasPct >= 10 -> "you over-estimate by ${stats.biasPct}%."
                    stats.biasPct <= -10 -> "you under-estimate by ${abs(stats.biasPct)}%."
                    else -> "calibrated within ±10%."
                }
                Text(biasLabel, style = bodyStyle, color = MinimalTheme.fg,
                    modifier = Modifier.testTag("estimation-bias"))

                Spacer(Modifier.height(24.dp))
                Text("median est ÷ actual", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "%.2f×".format(stats.medianRatio),
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
                    color = MinimalTheme.fg,
                    modifier = Modifier.testTag("estimation-median"),
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
