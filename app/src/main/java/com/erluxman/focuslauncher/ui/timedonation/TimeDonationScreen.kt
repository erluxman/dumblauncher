package com.erluxman.focuslauncher.ui.timedonation

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.backend.BackendRepository
import com.erluxman.focuslauncher.ui.home.minimal.MinimalTheme
import kotlinx.coroutines.launch

/**
 * SOCIAL-035 — Time donation. Gift saved minutes to a friend's bank.
 * Behind FlagKey.TIME_DONATION.
 */
@Composable
fun TimeDonationScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var to by remember { mutableStateOf("") }
    var mins by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }
    val minsInt = mins.toIntOrNull() ?: 0
    val ready = to.isNotBlank() && minsInt > 0

    Surface(
        modifier = Modifier.fillMaxSize().testTag("time-donation"),
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
                    .testTag("time-donation-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("time donation.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "send minutes you saved to a friend's bank. venmo for hours.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            Field("to (uid)", to, { to = it.take(40) }, "time-donation-to", KeyboardType.Text)
            Spacer(Modifier.height(16.dp))
            Field("minutes", mins, { mins = it.filter { c -> c.isDigit() }.take(4) }, "time-donation-mins", KeyboardType.Number)
            Spacer(Modifier.height(16.dp))
            Field("note (optional)", note, { note = it.take(120) }, "time-donation-note", KeyboardType.Text)

            Spacer(Modifier.height(20.dp))
            Text(
                "send",
                style = bodyStyle,
                color = if (ready) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("time-donation-send")
                    .clickable {
                        if (ready) {
                            scope.launch {
                                val r = backend.donateTime(to.trim(), minsInt, note.trim())
                                status = if (r.isSuccess) "sent $minsInt min to ${to.trim()}."
                                else "failed: ${r.exceptionOrNull()?.message}"
                                if (r.isSuccess) { to = ""; mins = ""; note = "" }
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )

            status?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, style = captionStyle, color = MinimalTheme.outline,
                    modifier = Modifier.testTag("time-donation-status"))
            }
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
            textStyle = bodyStyle.copy(color = MinimalTheme.fg),
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
