package com.erluxman.focuslauncher.ui.transparency
import com.erluxman.focuslauncher.data.prefs.caffeineDoses
import com.erluxman.focuslauncher.data.prefs.logCaffeine
import com.erluxman.focuslauncher.data.prefs.clearCaffeineLog
import com.erluxman.focuslauncher.data.prefs.drinkLog
import com.erluxman.focuslauncher.data.prefs.logDrink
import com.erluxman.focuslauncher.data.prefs.clearDrinkLog
import com.erluxman.focuslauncher.data.prefs.meditationLog
import com.erluxman.focuslauncher.data.prefs.logMeditation
import com.erluxman.focuslauncher.data.prefs.clearMeditationLog
import com.erluxman.focuslauncher.data.prefs.ideaParking
import com.erluxman.focuslauncher.data.prefs.addParkedIdea
import com.erluxman.focuslauncher.data.prefs.removeParkedIdea
import com.erluxman.focuslauncher.data.prefs.clearParkedIdeas
import com.erluxman.focuslauncher.data.prefs.readingLog
import com.erluxman.focuslauncher.data.prefs.logReading
import com.erluxman.focuslauncher.data.prefs.clearReadingLog
import com.erluxman.focuslauncher.data.prefs.workoutLog
import com.erluxman.focuslauncher.data.prefs.logWorkout
import com.erluxman.focuslauncher.data.prefs.clearWorkoutLog
import com.erluxman.focuslauncher.data.prefs.commitLog
import com.erluxman.focuslauncher.data.prefs.addCommits
import com.erluxman.focuslauncher.data.prefs.clearCommitLog
import com.erluxman.focuslauncher.data.prefs.prWall
import com.erluxman.focuslauncher.data.prefs.addPersonalRecord
import com.erluxman.focuslauncher.data.prefs.removePersonalRecord
import com.erluxman.focuslauncher.data.prefs.travelAtlas
import com.erluxman.focuslauncher.data.prefs.addTravel
import com.erluxman.focuslauncher.data.prefs.removeTravel
import com.erluxman.focuslauncher.data.prefs.subscriptions
import com.erluxman.focuslauncher.data.prefs.addSubscription
import com.erluxman.focuslauncher.data.prefs.removeSubscription
import com.erluxman.focuslauncher.data.prefs.moneyIncome
import com.erluxman.focuslauncher.data.prefs.moneyExpense
import com.erluxman.focuslauncher.data.prefs.moneyAssets
import com.erluxman.focuslauncher.data.prefs.moneyLiabilities
import com.erluxman.focuslauncher.data.prefs.setMoneyIncome
import com.erluxman.focuslauncher.data.prefs.setMoneyExpense
import com.erluxman.focuslauncher.data.prefs.setMoneyAssets
import com.erluxman.focuslauncher.data.prefs.setMoneyLiabilities
import com.erluxman.focuslauncher.data.prefs.mortalityWidgetsOptIn
import com.erluxman.focuslauncher.data.prefs.setMortalityWidgetsOptIn
import com.erluxman.focuslauncher.data.prefs.sleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.sleepWakeHour
import com.erluxman.focuslauncher.data.prefs.setSleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.setSleepWakeHour
import com.erluxman.focuslauncher.data.prefs.antiBio
import com.erluxman.focuslauncher.data.prefs.setAntiBio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import com.erluxman.focuslauncher.data.prefs.PrefKeys
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

private data class Technique(
    val name: String,
    val tagline: String,
    val explanation: String,
    val key: Preferences.Key<Boolean>
)

private val TECHNIQUES = listOf(
    Technique(
        "The Lobby",
        "Forces a pause before distraction apps open.",
        "When you tap a flagged app, an overlay counts down for a few seconds before letting it through. Inspired by friction-as-product research; you can switch it off.",
        PrefKeys.TECH_LOBBY
    ),
    Technique(
        "Dimming",
        "Reduces visual stimulation while flagged apps are open.",
        "We progressively dim the screen to dull the dopamine loop. It is a deliberate dark pattern; if you find it manipulative, disable it.",
        PrefKeys.TECH_DIMMING
    ),
    Technique(
        "Behavior indicator",
        "Surfaces a moral framing (THRIVING/DROWNING) on the home screen.",
        "Reframing your day as a state label is a behavioral nudge. Some users find it motivating, others find it shaming. Off-switch here.",
        PrefKeys.TECH_BEHAVIOR_INDICATOR
    ),
    Technique(
        "Hidden drawer",
        "App list is search-only by default.",
        "Removing visual app icons cuts down on impulse opens. If you'd rather see a full grid, disable this.",
        PrefKeys.TECH_HIDDEN_DRAWER
    ),
    Technique(
        "Variable ratio punishment",
        "Sometimes the Lobby is harder. Sometimes it isn't.",
        "About 1 in 5 Lobby visits gets a longer countdown and a harder cognitive tax. This is textbook slot-machine psychology turned against itself — disable any time.",
        PrefKeys.TECH_VARIABLE_RATIO
    ),
    Technique(
        "Escalating lockout",
        "Re-visiting the same app today makes the Lobby longer.",
        "Each subsequent Lobby visit for the same flagged app today adds a few seconds to the countdown. Toggle off if it ever feels punitive without being useful.",
        PrefKeys.TECH_ESCALATING
    ),
    Technique(
        "Dream mode",
        "After 10pm the home screen flips into wind-down mode.",
        "Replaces home with a breathing screen + tomorrow's One Thing between 10pm and 5am. Disable if you don't want the takeover (handy while inspecting the launcher).",
        PrefKeys.TECH_DREAM
    ),
    Technique(
        "Anchoring",
        "Shows a small \"most disciplined user\" stat next to yours.",
        "A textbook anchoring tactic — the comparison number is computed from your own stats, not real users. If that bothers you (or if comparison is unhelpful), disable.",
        PrefKeys.TECH_ANCHORING
    ),
)

@Composable
fun TransparencyScreen(prefs: UserPrefs, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.fillMaxSize().testTag("transparency"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("transparency-back")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "TRANSPARENCY",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Every behavioral technique we use.",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "We deliberately make leaving your phone harder. That's a dark pattern aimed in your favor. Each one is listed and switchable.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(TECHNIQUES, key = { it.key.name }) { tech ->
                    TechniqueCard(
                        tech = tech,
                        enabledFlow = prefs.technique(tech.key),
                        onToggle = { v -> scope.launch { prefs.setTechnique(tech.key, v) } }
                    )
                }
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "OPT-IN SURFACES",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline,
                        letterSpacing = 2.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Surfaces that lean on existential framing. Off by default. Turn on only if it helps you, not if it would harm you.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.height(12.dp))
                }
                item {
                    OptInCard(
                        name = "Mortality widgets",
                        tagline = "Beach Saturdays + days remaining (assumes 80yr life).",
                        explanation = "Two home-screen tiles built from your age and a fixed 80-year actuarial assumption. The math is local. If a death-clock would hurt you instead of helping you, leave this off.",
                        testTag = "tech-mortality",
                        enabledFlow = prefs.mortalityWidgetsOptIn,
                        onToggle = { v -> scope.launch { prefs.setMortalityWidgetsOptIn(v) } }
                    )
                }
                item {
                    AntiBioField(
                        flow = prefs.antiBio,
                        onSet = { v -> scope.launch { prefs.setAntiBio(v) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun AntiBioField(
    flow: kotlinx.coroutines.flow.Flow<String>,
    onSet: (String) -> Unit
) {
    val current by flow.collectAsState(initial = "")
    val draftState = androidx.compose.runtime.remember(current) { androidx.compose.runtime.mutableStateOf(current) }
    val draft = draftState.value
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("anti-bio-card"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("ANTI-BIO", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                "Describe what you've chosen NOT to do. Local-only for now; surfaces socially once profile lands.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Spacer(Modifier.height(8.dp))
            androidx.compose.material3.OutlinedTextField(
                value = draft,
                onValueChange = { draftState.value = it.take(280) },
                placeholder = { Text("I'm not the person who scrolls at 1am.") },
                modifier = Modifier.fillMaxWidth().testTag("anti-bio-input"),
                minLines = 2,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                androidx.compose.material3.Button(
                    onClick = { onSet(draft.trim()) },
                    modifier = Modifier.testTag("anti-bio-save"),
                ) { Text("Save") }
            }
        }
    }
}

@Composable
private fun OptInCard(
    name: String,
    tagline: String,
    explanation: String,
    testTag: String,
    enabledFlow: Flow<Boolean>,
    onToggle: (Boolean) -> Unit
) {
    val enabled by enabledFlow.collectAsState(initial = false)
    Surface(
        modifier = Modifier.fillMaxWidth().testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(tagline, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = onToggle,
                    modifier = Modifier.testTag("switch-$testTag")
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(explanation, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun TechniqueCard(
    tech: Technique,
    enabledFlow: Flow<Boolean>,
    onToggle: (Boolean) -> Unit
) {
    val enabled by enabledFlow.collectAsState(initial = true)
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("tech-${tech.key.name}"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(tech.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(tech.tagline, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                }
                Switch(checked = enabled, onCheckedChange = onToggle, modifier = Modifier.testTag("switch-${tech.key.name}"))
            }
            Spacer(Modifier.height(8.dp))
            Text(tech.explanation, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
