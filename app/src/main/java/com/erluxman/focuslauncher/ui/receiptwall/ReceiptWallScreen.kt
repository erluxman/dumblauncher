package com.erluxman.focuslauncher.ui.receiptwall

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
import com.erluxman.focuslauncher.service.social.ReceiptWall
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme

/**
 * SOCIAL-012 — Receipt Wall. Enter (app, seconds) for a session you
 * quick-quit; the wall surfaces a "win" line when it's under 3 minutes.
 *
 * Behind FlagKey.RECEIPT_WALL.
 */
@Composable
fun ReceiptWallScreen(
    onBack: () -> Unit,
) {
    var app by remember { mutableStateOf("") }
    var secTxt by remember { mutableStateOf("") }
    val sec = secTxt.toIntOrNull() ?: 0
    val receipt = remember(app, sec) { ReceiptWall.receiptFor(app, sec) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("receipt-wall"),
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
                    .testTag("receipt-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("receipt wall.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "put a flagged app down inside ${ReceiptWall.THRESHOLD_SEC / 60} minutes — get a receipt for restraint.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Field("app", app, { app = it.take(20) }, "receipt-app", KeyboardType.Text)
            Spacer(Modifier.height(16.dp))
            Field("seconds in app", secTxt, { secTxt = it.filter { c -> c.isDigit() }.take(4) },
                "receipt-seconds", KeyboardType.Number)

            Spacer(Modifier.height(32.dp))
            Text(
                text = receipt ?: when {
                    app.isBlank() || sec == 0 -> "—"
                    sec > ReceiptWall.THRESHOLD_SEC -> "over ${ReceiptWall.THRESHOLD_SEC / 60} min. no receipt."
                    else -> "—"
                },
                style = bodyStyle,
                color = if (receipt != null) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier.testTag("receipt-output"),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Field(label: String, value: String, onValueChange: (String) -> Unit, tag: String, keyboardType: KeyboardType) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = captionStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(6.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg, fontSize = 24.sp),
            cursorBrush = SolidColor(MinimalTheme.accent),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth().testTag(tag),
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MinimalTheme.outline.copy(alpha = 0.4f))
        )
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
