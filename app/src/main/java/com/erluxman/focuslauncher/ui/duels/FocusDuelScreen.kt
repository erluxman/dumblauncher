package com.erluxman.focuslauncher.ui.duels

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
import androidx.compose.runtime.collectAsState
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * SOCIAL-034 — Focus duel. Challenge a friend to a 2hr focus block.
 * Stub records the challenge; real wiring needs Firestore listeners.
 *
 * Behind FlagKey.FOCUS_DUEL.
 */
@Composable
fun FocusDuelScreen(
    backend: BackendRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val active by backend.activeDuel.collectAsState(initial = null)
    var uid by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("120") }
    val ready = uid.isNotBlank() && name.isNotBlank() && (duration.toIntOrNull() ?: 0) > 0
    val fmt = SimpleDateFormat("h:mm a", Locale.US)

    Surface(
        modifier = Modifier.fillMaxSize().testTag("focus-duel"),
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
                    .testTag("focus-duel-back")
                    .clickable { onBack() }
                    .padding(8.dp),
            )

            Spacer(Modifier.height(24.dp))
            Text("focus duel.", style = displayStyle, color = MinimalTheme.fg)
            Spacer(Modifier.height(8.dp))
            Text(
                "challenge a friend to a 2hr focus block. live scoreboard. whoever caves first loses.",
                style = bodyStyle.copy(fontSize = 14.sp),
                color = MinimalTheme.outline,
            )

            Spacer(Modifier.height(32.dp))
            active?.let { d ->
                Text("active", style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(4.dp))
                Text("vs ${d.otherName} · ${d.durationMin} min",
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
                    color = MinimalTheme.accent, modifier = Modifier.testTag("focus-duel-active"))
                Text("started ${fmt.format(Date(d.startedAtMs))}",
                    style = captionStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(16.dp))
                Text(
                    "forfeit",
                    style = bodyStyle,
                    color = MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("focus-duel-forfeit")
                        .clickable { scope.launch { backend.cancelFocusDuel() } }
                        .padding(vertical = 8.dp),
                )
                Spacer(Modifier.height(24.dp))
                return@Column
            }

            Text("challenge", style = captionStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(12.dp))
            Field(uid, { uid = it.take(40) }, "their uid", "focus-duel-uid", KeyboardType.Text)
            Spacer(Modifier.height(12.dp))
            Field(name, { name = it.take(40) }, "their name", "focus-duel-name", KeyboardType.Text)
            Spacer(Modifier.height(12.dp))
            Field(duration, { duration = it.filter { c -> c.isDigit() }.take(4) }, "minutes", "focus-duel-duration", KeyboardType.Number)
            Spacer(Modifier.height(16.dp))
            Text(
                "send challenge",
                style = bodyStyle,
                color = if (ready) MinimalTheme.accent else MinimalTheme.outline,
                modifier = Modifier
                    .testTag("focus-duel-send")
                    .clickable {
                        if (ready) {
                            scope.launch {
                                backend.challengeFocusDuel(uid.trim(), name.trim(), duration.toInt())
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun Field(value: String, onValueChange: (String) -> Unit, placeholder: String, tag: String, keyboardType: KeyboardType) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = bodyStyle.copy(color = MinimalTheme.fg),
            cursorBrush = SolidColor(MinimalTheme.accent),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
