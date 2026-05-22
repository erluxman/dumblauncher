package com.erluxman.focuslauncher.ui.breath

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.BreathCycle
import kotlinx.coroutines.delay
import kotlin.math.min

/**
 * HARDWARE-002 Breath to Unlock — visual 4-7-8 cycle.
 *
 * Caller passes [onCompleted] (e.g. flips lobby state to "passed"); we
 * call it once the user holds the breath for the full 19-second cycle.
 * No mic in this iteration; the timing wall does the work.
 */
@Composable
fun BreathScreen(
    onBack: () -> Unit,
    onCompleted: () -> Unit = {},
) {
    var startedMs by remember { mutableLongStateOf(0L) }
    var nowMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var done by remember { mutableStateOf(false) }
    val running = startedMs != 0L && !done
    val elapsed = if (running) (nowMs - startedMs) else 0L
    val phase = BreathCycle.phaseAt(elapsed)
    val scale = BreathCycle.circleScaleAt(elapsed)
    val animatedScale by animateFloatAsState(targetValue = scale, label = "scale")

    if (running) {
        LaunchedEffect(startedMs) {
            while (true) {
                delay(50)
                nowMs = System.currentTimeMillis()
                if (BreathCycle.phaseAt(nowMs - startedMs) == BreathCycle.Phase.DONE) {
                    done = true
                    onCompleted()
                    break
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("breath-screen"),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("breath-back")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    "BREATH UNLOCK",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            val cue = when {
                !running && !done -> "4 in. 7 hold. 8 out."
                done -> "Done."
                phase == BreathCycle.Phase.INHALE -> "Inhale"
                phase == BreathCycle.Phase.HOLD -> "Hold"
                phase == BreathCycle.Phase.EXHALE -> "Exhale"
                else -> ""
            }
            Text(
                text = cue,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("breath-cue")
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (running) "${(elapsed / 1000).toInt()}s / ${BreathCycle.CYCLE_MS / 1000}s"
                else "19-second cycle. No mic needed.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.testTag("breath-elapsed")
            )
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val r = min(size.width, size.height) * 0.45f * animatedScale
                    drawCircle(
                        color = androidx.compose.ui.graphics.Color(0xFF7AC6F4),
                        radius = r,
                        center = Offset(size.width / 2f, size.height / 2f),
                        style = Stroke(width = 6f),
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            if (done) {
                Text(
                    text = "You stayed with it for 19 seconds.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.testTag("breath-done")
                )
            } else {
                Button(
                    onClick = {
                        startedMs = System.currentTimeMillis()
                        nowMs = startedMs
                    },
                    enabled = !running,
                    modifier = Modifier.fillMaxWidth().testTag("breath-start")
                ) { Text(if (running) "Keep breathing…" else "Begin") }
            }
        }
    }
}
