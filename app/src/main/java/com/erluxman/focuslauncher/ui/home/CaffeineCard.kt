package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.habits.CaffeineMath
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * SUB-003 Caffeine Half-Life Graph.
 *
 * Three-part card:
 *   - The current "in your bloodstream" mg headline + a midnight projection.
 *   - A sparkline drawing the next-24h decay curve from now.
 *   - Preset chips for one-tap logging plus a clear button.
 */
@Composable
fun CaffeineCard(
    doses: List<CaffeineMath.Dose>,
    nowMs: Long,
    onLog: (Int) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = MaterialTheme.colorScheme.primary
    val outline = MaterialTheme.colorScheme.outline

    val nowMg = CaffeineMath.remainingMgAt(doses, nowMs)
    val midnightMs = nextMidnightMs(nowMs)
    val midnightMg = CaffeineMath.remainingMgAt(doses, midnightMs)

    Column(modifier = modifier.testTag("caffeine-card")) {
        Text(
            text = "CAFFEINE",
            style = MaterialTheme.typography.labelLarge,
            color = outline,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "IN YOUR SYSTEM",
                            style = MaterialTheme.typography.labelSmall,
                            color = outline,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "${nowMg.toInt()} mg",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("caffeine-now")
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "AT MIDNIGHT",
                            style = MaterialTheme.typography.labelSmall,
                            color = outline,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "${midnightMg.toInt()} mg",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.testTag("caffeine-midnight")
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .testTag("caffeine-sparkline")
                ) {
                    val totalHours = 24.0
                    val steps = 48
                    val peak = doses.sumOf { it.mg.toDouble() }.coerceAtLeast(nowMg).coerceAtLeast(1.0)
                    val w = size.width
                    val h = size.height
                    val path = Path()
                    for (i in 0..steps) {
                        val hoursAhead = totalHours * i / steps
                        val tMs = nowMs + (hoursAhead * 3_600_000.0).toLong()
                        val v = CaffeineMath.remainingMgAt(doses, tMs)
                        val x = w * i / steps
                        val y = h - (v / peak * h).toFloat().coerceIn(0f, h)
                        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    drawLine(
                        color = outline.copy(alpha = 0.25f),
                        start = Offset(0f, h - 1f),
                        end = Offset(w, h - 1f),
                        strokeWidth = 1f
                    )
                    drawPath(path = path, color = accent, style = Stroke(width = 3f))
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (doses.isEmpty()) "Tap a preset below to log a drink."
                    else "Half-life ${CaffeineMath.DEFAULT_HALF_LIFE_HOURS.toInt()}h — pairs with sleep stats.",
                    style = MaterialTheme.typography.bodySmall,
                    color = outline
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CaffeineMath.PRESETS) { preset ->
                        AssistChip(
                            onClick = { onLog(preset.mg) },
                            label = { Text("${preset.label} +${preset.mg}mg") },
                            modifier = Modifier.testTag("caffeine-preset-${preset.mg}")
                        )
                    }
                }
                if (doses.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    TextButton(
                        onClick = onClear,
                        modifier = Modifier.testTag("caffeine-clear")
                    ) { Text("Clear today (${doses.size})") }
                }
            }
        }
    }
}

private fun nextMidnightMs(nowMs: Long): Long {
    val cal = Calendar.getInstance().apply {
        timeInMillis = nowMs
        add(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return cal.timeInMillis
}

@Suppress("unused")
private fun isoMinute(ms: Long): String =
    SimpleDateFormat("HH:mm", Locale.US).format(java.util.Date(ms))
