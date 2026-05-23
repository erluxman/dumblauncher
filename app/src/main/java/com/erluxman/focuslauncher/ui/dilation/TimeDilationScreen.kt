package com.erluxman.focuslauncher.ui.dilation

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
import com.erluxman.focuslauncher.service.insights.TimeDilation
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * Type the real minutes you scrolled; see what it felt like at 3×.
 * Behind FlagKey.TIME_DILATION.
 */
@Composable
fun TimeDilationScreen(
    onBack: () -> Unit,
) {
    var realTxt by remember { mutableStateOf("") }
    val real = realTxt.toIntOrNull() ?: 0
    val felt = TimeDilation.dilatedMinutes(real)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("dilation"),
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
                    .testTag("dilation-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("time dilation.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "scrolling stretches subjective time about 3×. enter real minutes.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("real minutes", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            BasicTextField(
                value = realTxt,
                onValueChange = { realTxt = it.filter { c -> c.isDigit() }.take(4) },
                singleLine = true,
                textStyle = bodyStyle.copy(color = MinimalTheme.fg, fontSize = 28.sp),
                cursorBrush = SolidColor(MinimalTheme.accent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().testTag("dilation-input"),
                decorationBox = { inner ->
                    if (realTxt.isEmpty()) {
                        Text("0", style = bodyStyle.copy(fontSize = 28.sp),
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
            Text("felt like", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(4.dp))
            Text(
                text = TimeDilation.formatHm(felt),
                style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Normal),
                color = if (real > 0) MinimalTheme.accent else MinimalTheme.fg,
                modifier = Modifier.testTag("dilation-felt"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
