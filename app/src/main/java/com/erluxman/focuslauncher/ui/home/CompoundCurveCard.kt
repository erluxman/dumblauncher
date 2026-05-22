package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.CompoundCurve

/**
 * LIFE-010 Compounding Curve. Pick a daily improvement rate; see the
 * curve over 1, 3, and 5 years on a small log-y chart. Headline is the
 * "X× baseline" multiplier at the selected horizon.
 */
@Composable
fun CompoundCurveCard(
    modifier: Modifier = Modifier,
) {
    val rates = listOf(
        "0.1%/day" to 0.001,
        "0.5%/day" to 0.005,
        "1%/day" to 0.010,
    )
    val horizons = listOf("1y" to 365, "3y" to 365 * 3, "5y" to 365 * 5)

    var ratePick by remember { mutableStateOf(1) }
    var horizonPick by remember { mutableStateOf(0) }

    val daily = rates[ratePick].second
    val days = horizons[horizonPick].second
    val multiplier = CompoundCurve.valueAt(days, daily)

    val accent = MaterialTheme.colorScheme.primary
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("compound-card")) {
        Text(
            "COMPOUND",
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
                            "BASELINE × ${rates[ratePick].first} → ${horizons[horizonPick].first}",
                            style = MaterialTheme.typography.labelSmall,
                            color = outline,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "%.1fx".format(multiplier),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("compound-multiplier")
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .testTag("compound-curve")
                ) {
                    val steps = 80
                    val series = DoubleArray(steps + 1) {
                        CompoundCurve.valueAt(days * it / steps, daily)
                    }
                    val maxV = series.last()
                    val w = size.width
                    val h = size.height
                    val path = Path()
                    for (i in 0..steps) {
                        val x = w * i / steps
                        val y = h - (series[i] / maxV * h).toFloat().coerceIn(0f, h)
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
                Spacer(Modifier.height(12.dp))
                Text("RATE", style = MaterialTheme.typography.labelSmall, color = outline)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rates.forEachIndexed { i, (label, _) ->
                        AssistChip(
                            onClick = { ratePick = i },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (i == ratePick) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag("compound-rate-$i")
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("HORIZON", style = MaterialTheme.typography.labelSmall, color = outline)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    horizons.forEachIndexed { i, (label, _) ->
                        AssistChip(
                            onClick = { horizonPick = i },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (i == horizonPick) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag("compound-horizon-$i")
                        )
                    }
                }
            }
        }
    }
}
