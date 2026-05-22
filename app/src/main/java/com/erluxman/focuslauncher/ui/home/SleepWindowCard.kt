package com.erluxman.focuslauncher.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.erluxman.focuslauncher.service.SleepWindow
import java.util.Calendar

/**
 * SLEEP-003 Sleep Window Guardrails settings card.
 *
 * Two ±1h adjusters for cutoff and wake. Surfaces the current window
 * and a "DREAM ACTIVE" tag when we're inside it right now.
 */
@Composable
fun SleepWindowCard(
    cutoffHour: Int,
    wakeHour: Int,
    onSetCutoff: (Int) -> Unit,
    onSetWake: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val nowHour = remember24Hour()
    val active = SleepWindow.isInWindow(nowHour, cutoffHour, wakeHour)
    val outline = MaterialTheme.colorScheme.outline

    Column(modifier = modifier.testTag("sleep-window-card")) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "SLEEP WINDOW",
                style = MaterialTheme.typography.labelLarge,
                color = outline,
                letterSpacing = 2.sp
            )
            if (active) {
                Spacer(Modifier.height(1.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .testTag("sleep-active-tag")
                ) {
                    Text(
                        text = "DREAM ACTIVE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.5.sp
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${fmt(cutoffHour)} → ${fmt(wakeHour)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("sleep-window-range")
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Distraction apps lock and the home screen winds down between these hours.",
                    style = MaterialTheme.typography.bodySmall,
                    color = outline
                )
                Spacer(Modifier.height(12.dp))
                HourAdjuster(
                    label = "CUTOFF",
                    hour = cutoffHour,
                    onDec = { onSetCutoff((cutoffHour + 23) % 24) },
                    onInc = { onSetCutoff((cutoffHour + 1) % 24) },
                    decTag = "sleep-cutoff-dec",
                    incTag = "sleep-cutoff-inc",
                )
                Spacer(Modifier.height(8.dp))
                HourAdjuster(
                    label = "WAKE",
                    hour = wakeHour,
                    onDec = { onSetWake((wakeHour + 23) % 24) },
                    onInc = { onSetWake((wakeHour + 1) % 24) },
                    decTag = "sleep-wake-dec",
                    incTag = "sleep-wake-inc",
                )
            }
        }
    }
}

@Composable
private fun HourAdjuster(
    label: String,
    hour: Int,
    onDec: () -> Unit,
    onInc: () -> Unit,
    decTag: String,
    incTag: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            letterSpacing = 1.5.sp,
            modifier = Modifier.width(64.dp)
        )
        IconButton(onClick = onDec, modifier = Modifier.testTag(decTag)) {
            Icon(Icons.Filled.Remove, contentDescription = "$label minus one hour")
        }
        Text(
            text = fmt(hour),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .width(72.dp)
                .testTag("$label-value".lowercase())
        )
        IconButton(onClick = onInc, modifier = Modifier.testTag(incTag)) {
            Icon(Icons.Filled.Add, contentDescription = "$label plus one hour")
        }
    }
}

private fun fmt(hour: Int): String {
    val h = hour.mod(24)
    val period = if (h < 12) "AM" else "PM"
    val display = when {
        h == 0 -> 12
        h <= 12 -> if (h == 12) 12 else h
        else -> h - 12
    }
    return "$display $period"
}

@Composable
private fun remember24Hour(): Int =
    androidx.compose.runtime.remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }

