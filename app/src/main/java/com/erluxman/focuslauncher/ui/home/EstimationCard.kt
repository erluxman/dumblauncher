package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import com.erluxman.focuslauncher.service.EstimationAccuracy

@Composable
fun EstimationCard(
    todos: List<TodoEntity>,
    modifier: Modifier = Modifier,
) {
    val pairs = todos.mapNotNull { t ->
        val e = t.estimatedMinutes ?: return@mapNotNull null
        val a = t.actualMinutes ?: return@mapNotNull null
        if (e > 0 && a > 0) e to a else null
    }
    if (pairs.isEmpty()) return
    val stats = EstimationAccuracy.compute(pairs)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("estimation-card")) {
        Text(
            "ESTIMATION",
            style = MaterialTheme.typography.labelLarge,
            color = outline,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("ACCURACY", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "${stats.accuracyPct}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("estimation-accuracy")
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("BIAS", style = MaterialTheme.typography.labelSmall, color = outline, letterSpacing = 1.5.sp)
                        Text(
                            text = "${if (stats.biasPct >= 0) "+" else ""}${stats.biasPct}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("estimation-bias")
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${stats.sample} completed todos, median ratio ${"%.2f".format(stats.medianRatio)}× est/actual",
                    style = MaterialTheme.typography.bodySmall,
                    color = outline,
                    modifier = Modifier.testTag("estimation-detail")
                )
            }
        }
    }
}
