package com.erluxman.focuslauncher.ui.anchor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.service.insights.AnchorMath
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlin.math.abs

/**
 * Anchoring attack — show how your day's distraction compares to the
 * "most disciplined" anchor (12 min). Behind FlagKey.ANCHOR.
 */
@Composable
fun AnchorScreen(
    onBack: () -> Unit,
) {
    var minsTxt by remember { mutableStateOf("") }
    val mins = minsTxt.toIntOrNull() ?: 0
    val delta = AnchorMath.delta(mins)
    val ratio = if (AnchorMath.ANCHOR_MINUTES > 0) mins.toDouble() / AnchorMath.ANCHOR_MINUTES else 0.0

    Surface(
        modifier = Modifier.fillMaxSize().testTag("anchor"),
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
                    .testTag("anchor-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("anchor.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "the most disciplined user spends ${AnchorMath.ANCHOR_MINUTES} min/day on distractions.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("your distraction today", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            BasicTextField(
                value = minsTxt,
                onValueChange = { minsTxt = it.filter { c -> c.isDigit() }.take(4) },
                singleLine = true,
                textStyle = bodyStyle.copy(color = MinimalTheme.fg, fontSize = 28.sp),
                cursorBrush = SolidColor(MinimalTheme.accent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().testTag("anchor-input"),
                decorationBox = { inner ->
                    if (minsTxt.isEmpty()) {
                        Text("0 min", style = bodyStyle.copy(fontSize = 28.sp),
                            color = MinimalTheme.outline.copy(alpha = 0.5f))
                    }
                    inner()
                },
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MinimalTheme.outline.copy(alpha = 0.4f))
            )

            Spacer(Modifier.height(40.dp))
            Text("delta", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = when {
                    mins == 0 -> "—"
                    delta == 0 -> "exactly the anchor."
                    delta > 0 -> "+${delta} min over."
                    else -> "${delta} min under."
                },
                style = TextStyle(fontSize = 48.sp, fontWeight = FontWeight.Normal),
                color = if (delta <= 0 && mins > 0) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("anchor-delta"),
            )

            if (mins > 0) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "${"%.1f".format(ratio)}× the anchor.",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier.testTag("anchor-ratio"),
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = when {
                        ratio <= 1.0 -> "you're at or below the most disciplined. keep going."
                        ratio <= 2.0 -> "close enough — one push and you're under."
                        ratio <= 5.0 -> "a long climb. but climbable."
                        else -> "the gap is the work. don't argue with it."
                    },
                    style = bodyStyle.copy(fontSize = 14.sp),
                    color = MinimalTheme.outline,
                )
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
