package com.erluxman.focuslauncher.ui.home.minimal

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.prefs.PrefKeys
import com.erluxman.focuslauncher.data.prefs.sleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.sleepWakeHour
import com.erluxman.focuslauncher.service.lobby.SleepWindow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Minimal launcher home (see documentation/design-minimal.md).
 *
 * No cards, no surfaces. Type-only. The whole screen shows one
 * "section" determined by the hour-of-day:
 *
 *   05-09  Morning routine
 *   09-17  Work focus
 *   17-22  Shutdown ritual
 *   22-05  Dream (handled by the existing DreamModeScreen, not here)
 *
 * Quantitative data lives behind a swipe-down (StatsSheet); this surface
 * never shows it.
 */
@Composable
fun MinimalHomeScreen(
    prefs: UserPrefs,
    onOpenMenu: () -> Unit,
) {
    val context = LocalContext.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    val cutoffHour by prefs.sleepCutoffHour.collectAsState(initial = SleepWindow.DEFAULT_CUTOFF_HOUR)
    val wakeHour by prefs.sleepWakeHour.collectAsState(initial = SleepWindow.DEFAULT_WAKE_HOUR)
    val nowHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val isDream = SleepWindow.isInWindow(nowHour, cutoffHour, wakeHour)

    val oneThingText by prefs.oneThingText.collectAsState(initial = "")
    val whyHere by prefs.whyHere.collectAsState(initial = "")
    val streakDays by prefs.streakDays.collectAsState(initial = 0)
    val morningDate by prefs.morningDoneDate.collectAsState(initial = "")
    val morningSteps by prefs.morningDoneSteps.collectAsState(initial = emptySet())
    val shutdownDate by prefs.shutdownDoneDate.collectAsState(initial = "")
    val shutdownSteps by prefs.shutdownDoneSteps.collectAsState(initial = emptySet())

    val dreamEnabled by prefs.technique(PrefKeys.TECH_DREAM).collectAsState(initial = true)
    val showDream = isDream && dreamEnabled
    if (showDream) {
        DreamSurface(oneThingText = oneThingText)
        return
    }

    val now = remember { Date() }
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val timeStr = remember(now) { SimpleDateFormat("HH:mm", Locale.US).format(now) }
    val todayIso = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(now) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("minimal-home"),
        color = MinimalTheme.bg,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount > 40f) onOpenMenu()
                    }
                }
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp)) {
                // top: time + soft ↓ glyph (tappable to open menu)
                Spacer(Modifier.height(40.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("· $timeStr", style = captionStyle, color = MinimalTheme.outline)
                    Text(
                        text = "↓ menu",
                        style = captionStyle,
                        color = MinimalTheme.outline.copy(alpha = 0.6f),
                        modifier = Modifier
                            .testTag("dashboard-hint")
                            .clickable { onOpenMenu() }
                            .padding(8.dp),
                    )
                }

                Spacer(Modifier.weight(0.4f))

                when (hour) {
                    in 5..8 -> MorningSection(
                        morningStepsToday = if (morningDate == todayIso) morningSteps else emptySet(),
                        oneThingText = oneThingText,
                        onOpenMenu = onOpenMenu,
                        onToggleStep = { step ->
                            scope.launch { prefs.toggleMorningStep(step, todayIso) }
                        }
                    )
                    in 9..16 -> WorkSection(
                        streakDays = streakDays,
                        oneThingText = oneThingText,
                        whyHere = whyHere,
                        onOpenMenu = onOpenMenu,
                    )
                    in 17..21 -> ShutdownSection(
                        shutdownStepsToday = if (shutdownDate == todayIso) shutdownSteps else emptySet(),
                        oneThingText = oneThingText,
                        onSetOneThing = { text ->
                            scope.launch { prefs.setOneThing(text, todayIso) }
                        },
                        onToggleStep = { step ->
                            scope.launch { prefs.toggleShutdownStep(step, todayIso) }
                        }
                    )
                    else -> WorkSection(
                        streakDays = streakDays,
                        oneThingText = oneThingText,
                        whyHere = whyHere,
                        onOpenMenu = onOpenMenu,
                    )
                }

                Spacer(Modifier.weight(1f))

                MinimalDock(
                    onOpenMenu = onOpenMenu,
                    onDial = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        try { context.startActivity(intent) } catch (_: ActivityNotFoundException) {}
                    },
                    onMessages = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("sms:")
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        try { context.startActivity(intent) } catch (_: ActivityNotFoundException) {}
                    },
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MorningSection(
    morningStepsToday: Set<String>,
    oneThingText: String,
    onOpenMenu: () -> Unit,
    onToggleStep: (String) -> Unit,
) {
    Column(modifier = Modifier.testTag("section-morning")) {
        Text("good morning.", style = displayStyle, color = MinimalTheme.fg)
        Spacer(Modifier.height(40.dp))

        Text("today's one thing —", style = bodyStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(8.dp))
        Text(
            text = oneThingText.ifBlank { "(tap to set →)" },
            style = bodyStyle,
            color = if (oneThingText.isBlank()) MinimalTheme.outline else MinimalTheme.accent,
            modifier = Modifier
                .testTag("morning-one-thing")
                .clickable { if (oneThingText.isBlank()) onOpenMenu() },
        )

        Spacer(Modifier.height(40.dp))

        // Step checklist as inline glyphs: ● done, ○ undone.
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
            morningStepsShort().forEachIndexed { i, (full, short) ->
                val done = full in morningStepsToday
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .testTag("morning-step-$i")
                        .pointerInput(full) {
                            detectVerticalDragGestures { _, _ -> }
                        }
                ) {
                    Text(short, style = bodyStyle, color = if (done) MinimalTheme.fg else MinimalTheme.outline)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (done) "●" else "○",
                        style = bodyStyle,
                        color = if (done) MinimalTheme.accent else MinimalTheme.outline,
                        modifier = Modifier
                            .testTag("morning-step-toggle-$i")
                            .padding(8.dp)
                            .pointerInput(full) {
                                detectTap { onToggleStep(full) }
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkSection(
    streakDays: Int,
    oneThingText: String,
    whyHere: String,
    onOpenMenu: () -> Unit,
) {
    Column(modifier = Modifier.testTag("section-work")) {
        Text(
            text = if (streakDays > 0) "day $streakDays of work." else "let's begin.",
            style = displayStyle,
            color = MinimalTheme.fg,
            modifier = Modifier.testTag("work-streak"),
        )
        Spacer(Modifier.height(40.dp))
        Text("one thing —", style = bodyStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(8.dp))
        Text(
            text = oneThingText.ifBlank { "(tap to set →)" },
            style = bodyStyle,
            color = if (oneThingText.isBlank()) MinimalTheme.outline else MinimalTheme.accent,
            modifier = Modifier
                .testTag("work-one-thing")
                .clickable { if (oneThingText.isBlank()) onOpenMenu() },
        )
        if (whyHere.isNotBlank()) {
            Spacer(Modifier.height(40.dp))
            Text("you're here because —", style = bodyStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(8.dp))
            Text(
                text = whyHere,
                style = bodyStyle.copy(fontSize = 16.sp),
                color = MinimalTheme.outline,
                modifier = Modifier.testTag("work-why-here"),
            )
        }
    }
}

@Composable
private fun ShutdownSection(
    shutdownStepsToday: Set<String>,
    oneThingText: String,
    onSetOneThing: (String) -> Unit,
    onToggleStep: (String) -> Unit,
) {
    var nextOne by remember(oneThingText) { mutableStateOf("") }
    Column(modifier = Modifier.testTag("section-shutdown")) {
        Text("shutdown.", style = displayStyle, color = MinimalTheme.fg)
        Spacer(Modifier.height(40.dp))

        Text("tomorrow's one thing —", style = bodyStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value = nextOne,
            onValueChange = { nextOne = it.take(80) },
            textStyle = bodyStyle.copy(color = MinimalTheme.accent),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(MinimalTheme.accent),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("shutdown-next-one-input"),
            decorationBox = { inner ->
                Column {
                    inner()
                    Spacer(Modifier.height(4.dp))
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MinimalTheme.outline.copy(alpha = 0.4f))
                    )
                }
            },
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "save",
            style = bodyStyle,
            color = MinimalTheme.accent,
            modifier = Modifier
                .testTag("shutdown-save")
                .padding(4.dp)
                .pointerInput(nextOne) {
                    detectTap {
                        if (nextOne.isNotBlank()) onSetOneThing(nextOne.trim())
                    }
                },
        )
        Spacer(Modifier.height(40.dp))
        Text("close the day —", style = bodyStyle, color = MinimalTheme.outline)
        Spacer(Modifier.height(8.dp))
        shutdownStepsShort().forEachIndexed { i, (full, short) ->
            val done = full in shutdownStepsToday
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = if (done) "●" else "○",
                    style = bodyStyle,
                    color = if (done) MinimalTheme.accent else MinimalTheme.outline,
                    modifier = Modifier
                        .testTag("shutdown-step-$i")
                        .padding(end = 12.dp)
                        .pointerInput(full) {
                            detectTap { onToggleStep(full) }
                        },
                )
                Text(short, style = bodyStyle, color = if (done) MinimalTheme.fg else MinimalTheme.outline)
            }
        }
    }
}

@Composable
private fun DreamSurface(oneThingText: String) {
    Surface(
        modifier = Modifier.fillMaxSize().testTag("section-dream"),
        color = MinimalTheme.bg,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("●", style = TextStyle(fontSize = 80.sp), color = MinimalTheme.accent)
            Spacer(Modifier.height(40.dp))
            Text("inhale 4.  hold 7.  exhale 8.", style = bodyStyle, color = MinimalTheme.outline)
            Spacer(Modifier.height(64.dp))
            if (oneThingText.isNotBlank()) {
                Text("tomorrow —", style = bodyStyle, color = MinimalTheme.outline)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = oneThingText,
                    style = bodyStyle,
                    color = MinimalTheme.fg,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun MinimalDock(
    onOpenMenu: () -> Unit,
    onDial: () -> Unit,
    onMessages: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MinimalTheme.outline.copy(alpha = 0.25f))
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DockGlyph(label = "phone", onClick = onDial)
            DockGlyph(label = "msgs", onClick = onMessages)
            DockGlyph(label = "settings", onClick = onOpenMenu)
        }
    }
}

@Composable
private fun DockGlyph(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        style = bodyStyle.copy(fontSize = 14.sp),
        color = MinimalTheme.outline,
        modifier = Modifier
            .testTag("dock-$label")
            .clickable { onClick() }
            .padding(8.dp),
    )
}

private val displayStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val bodyStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
private val captionStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)

internal object MinimalTheme {
    val bg = Color(0xFF0B0B0E)
    val fg = Color(0xFFEEEEEE)
    val outline = Color(0xFF6E6E78)
    val accent = Color(0xFFE5A95C)
}

/** Match against the labels in HomeViewModel.MORNING_STEPS but with short visible names. */
private fun morningStepsShort(): List<Pair<String, String>> = listOf(
    "Drink a glass of water" to "water",
    "Stretch for two minutes" to "stretch",
    "No phone for 30 minutes after waking" to "no phone",
)

private fun shutdownStepsShort(): List<Pair<String, String>> = listOf(
    "Review tomorrow's calendar" to "review tomorrow",
    "Set tomorrow's one thing" to "tomorrow's one thing",
    "One-line journal entry" to "one-line journal",
)

private suspend fun androidx.compose.ui.input.pointer.PointerInputScope.detectTap(
    onTap: () -> Unit
) {
    detectTapGestures { onTap() }
}
