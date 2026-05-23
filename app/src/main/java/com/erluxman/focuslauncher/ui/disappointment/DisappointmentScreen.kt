package com.erluxman.focuslauncher.ui.disappointment

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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * SOCIAL-005 — The Disappointment API. One push/week to a chosen
 * respected person showing your worst stat for the week.
 * Behind FlagKey.DISAPPOINTMENT.
 */
@Composable
fun DisappointmentScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var to by remember { mutableStateOf("") }
    var stat by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("disappointment"),
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
                    .testTag("disappointment-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("disappointment.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "one push per week to a person you respect — with your worst stat. " +
                    "server-side rate-limited so you can't spam.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Text("to (uid / handle)", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            Field(to, { to = it.take(40) }, "their uid", "disappointment-to")

            Spacer(Modifier.height(20.dp))
            Text("your worst stat this week", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(6.dp))
            Field(stat, { stat = it.take(120) }, "5h 47m in tiktok", "disappointment-stat")

            Spacer(Modifier.height(20.dp))
            Text(
                "send",
                style = bodyStyle,
                color = if (to.isNotBlank() && stat.isNotBlank()) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("disappointment-send")
                    .clickable {
                        if (to.isNotBlank() && stat.isNotBlank()) {
                            scope.launch {
                                val r = backend.sendDisappointment(to.trim(), stat.trim())
                                status = if (r.isSuccess) "sent." else "failed: ${r.exceptionOrNull()?.message}"
                                if (r.isSuccess) { to = ""; stat = "" }
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            status?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = captionStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("disappointment-status"))
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Field(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    tag: String,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg),
            cursorBrush = SolidColor(MinimalTheme.accent),
            modifier = Modifier.fillMaxWidth().testTag(tag),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(placeholder, style = bodyStyle, color = MinimalTheme.outline.copy(alpha = 0.6f))
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
    }
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
