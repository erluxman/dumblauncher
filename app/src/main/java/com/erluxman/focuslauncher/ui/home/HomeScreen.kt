package com.erluxman.focuslauncher.ui.home

import android.app.admin.DevicePolicyManager
import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.testTag
import com.erluxman.focuslauncher.data.local.AppDatabase
import com.erluxman.focuslauncher.data.local.entity.ProjectEntity
import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.repository.JournalRepository
import com.erluxman.focuslauncher.data.repository.ProjectRepository
import com.erluxman.focuslauncher.data.repository.TodoRepository
import com.erluxman.focuslauncher.service.ExportBuilder
import com.erluxman.focuslauncher.service.ExportSnapshot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.erluxman.focuslauncher.model.AppInfo
import com.erluxman.focuslauncher.receiver.FocusDeviceAdminReceiver
import com.erluxman.focuslauncher.ui.home.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    prefs: UserPrefs,
    onOpenTransparency: () -> Unit,
    onReplayOnboarding: () -> Unit,
    onOpenUninstall: () -> Unit = {},
    onOpenVip: () -> Unit = {},
    onOpenFocus: () -> Unit = {},
    onOpenMantra: () -> Unit = {},
    onOpenBoredom: () -> Unit = {}
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val todoRepository = remember { TodoRepository(database.todoDao()) }
    val projectRepository = remember { ProjectRepository(database.projectDao()) }
    val journalRepository = remember { JournalRepository(database.journalDao()) }

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(
            todoRepository,
            projectRepository,
            journalRepository,
            context.packageManager,
            prefs,
            context.applicationContext
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refreshUsage()
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var showSetupDialog by remember { mutableStateOf(false) }
    var showInterventionDialog by remember { mutableStateOf(false) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    if (uiState.isDreamMode) {
        DreamModeScreen(
            oneThingText = uiState.oneThingText,
            onExitDream = { /* leaves with next-day rollover */ }
        )
        return
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("home"),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(64.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.behaviorIndicatorEnabled) {
                        BehaviorIndicator(
                            state = uiState.behaviorState,
                            progress = uiState.behaviorProgress,
                            screenMinutes = uiState.screenMinutesToday,
                            targetMinutes = uiState.dailyTargetMin,
                            legacyMinutes = uiState.legacyBuilderMinutes,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                    IconButton(
                        onClick = { showSetupDialog = true },
                        modifier = Modifier.testTag("settings-button")
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Setup", tint = MaterialTheme.colorScheme.outline)
                    }
                }

                if (uiState.crisisActive) {
                    Spacer(Modifier.height(16.dp))
                    SoftenBanner()
                } else if (uiState.behaviorState in setOf("SINKING", "DROWNING") && uiState.behaviorIndicatorEnabled) {
                    Spacer(Modifier.height(16.dp))
                    val sadSelfMessage = remember(uiState.behaviorState, uiState.whyHere) {
                        com.erluxman.focuslauncher.service.SadSelfEngine.pick(
                            state = uiState.behaviorState,
                            why = uiState.whyHere,
                            seed = (System.currentTimeMillis() / (24L * 60 * 60 * 1000)).toInt()
                        )
                    }
                    InterventionBanner(
                        state = uiState.behaviorState,
                        whyHere = sadSelfMessage,
                        onPause = { showInterventionDialog = true }
                    )
                }

                if (uiState.whyHere.isNotBlank()) {
                    Spacer(Modifier.height(16.dp))
                    WhyHereCard(text = uiState.whyHere)
                }

                Spacer(modifier = Modifier.height(32.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f).testTag("home-list"),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    if (uiState.dueFutureLetter.isNotBlank()) {
                        item {
                            FutureLetterBanner(
                                entry = uiState.dueFutureLetter,
                                onDismiss = viewModel::dismissDueLetter
                            )
                        }
                    }
                    item {
                        OneThingCard(
                            text = uiState.oneThingText,
                            isForToday = uiState.oneThingIsForToday,
                            onSet = viewModel::setOneThing,
                            onClear = viewModel::clearOneThing
                        )
                    }
                    item {
                        AnchorChip(distractionMinutes = uiState.distractionMinutesToday)
                    }
                    item {
                        DomainStreaksRow(streaks = uiState.domainStreaks)
                    }
                    item {
                        FocusPointsChip(points = uiState.focusPoints)
                    }
                    item {
                        StreakRow(
                            days = uiState.streakDays,
                            best = uiState.streakBest,
                            focusSessionsToday = uiState.focusSessionsToday,
                            timeBankTotalMin = uiState.timeBankTotalMin
                        )
                    }
                    item {
                        GraceRow(
                            freezes = uiState.streakFreezes,
                            graceDays = uiState.graceDays,
                            onDeclareTomorrow = { iso -> viewModel.declareGraceDay(iso) },
                            onCancelTomorrow = { iso -> viewModel.cancelGraceDay(iso) }
                        )
                    }
                    item {
                        InsightsCard(
                            distractionMinutes = uiState.distractionMinutesToday,
                            hourlyRate = uiState.hourlyRateUsd,
                            userAge = uiState.userAge,
                            todosCompletedToday = uiState.todosCompletedToday,
                            focusSessionsToday = uiState.focusSessionsToday,
                            timeBankTotalMin = uiState.timeBankTotalMin,
                            onSetHourlyRate = { r -> scope.launch { prefs.setHourlyRate(r) } },
                            onSetAge = { a -> scope.launch { prefs.setUserAge(a) } }
                        )
                    }
                    if (uiState.afterFallDueDate.isNotBlank()) {
                        item {
                            AfterFallCard(
                                steps = HomeViewModel.AFTER_FALL_STEPS,
                                done = uiState.afterFallStepsDone,
                                onToggle = viewModel::toggleAfterFallStep,
                                onDismiss = viewModel::dismissAfterFall
                            )
                        }
                    }
                    if (uiState.timeDebtMin > 0) {
                        item {
                            TimeDebtCard(
                                debtMin = uiState.timeDebtMin,
                                effectiveTarget = uiState.effectiveTargetMin,
                                baseTarget = uiState.dailyTargetMin
                            )
                        }
                    }
                    item {
                        MoodPingCard(
                            recentPings = uiState.moodPings,
                            onLog = viewModel::logMoodPing
                        )
                    }
                    if (!uiState.isMorningDoneToday) {
                        item {
                            MorningRoutineCard(
                                steps = HomeViewModel.MORNING_STEPS,
                                done = uiState.morningStepsDone,
                                onToggle = viewModel::toggleMorningStep
                            )
                        }
                    }
                    item {
                        IdentityVoteCard(
                            builder = uiState.identityBuilderToday,
                            consumer = uiState.identityConsumerToday,
                            onVoteBuilder = { viewModel.voteIdentity(true) },
                            onVoteConsumer = { viewModel.voteIdentity(false) }
                        )
                    }
                    if (!uiState.isShutdownDoneToday) {
                        item {
                            ShutdownRitualCard(
                                steps = HomeViewModel.SHUTDOWN_STEPS,
                                done = uiState.shutdownStepsDone,
                                onToggle = viewModel::toggleShutdownStep
                            )
                        }
                    }
                    item {
                        HeatmapStrip(
                            counts = uiState.heatmapPerDay,
                            phantomBuzzToday = uiState.phantomBuzzToday,
                            onBuzz = viewModel::bumpPhantomBuzz
                        )
                    }
                    item { ProjectSection(uiState.projects) }
                    item { TodoSection(uiState.todos, onToggle = viewModel::toggleTodo, onAdd = viewModel::addTodo, onAddWithEstimate = viewModel::addTodoWithEstimate, onDelete = viewModel::deleteTodo) }
                    item {
                        TombstoneSection(
                            entries = uiState.appTombstones,
                            onAdd = viewModel::addTombstone,
                            onRemove = viewModel::removeTombstone
                        )
                    }
                    item {
                        FutureLetterComposer(onSave = viewModel::saveFutureLetter)
                    }
                    item { WidgetSection() }
                }
            }

            // App Dock at the bottom
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                AppDock(
                    dockApps = uiState.dockApps,
                    onAppClick = { viewModel.launchApp(context, it) },
                    onSearchClick = { isSearching = true },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            AnimatedVisibility(
                visible = isSearching,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                SearchOverlay(
                    query = searchQuery,
                    apps = uiState.apps,
                    onQueryChange = { searchQuery = it },
                    onAppClick = { 
                        viewModel.launchApp(context, it)
                        isSearching = false
                        searchQuery = ""
                    },
                    onDismiss = { 
                        isSearching = false
                        searchQuery = ""
                    }
                )
            }

            if (showInterventionDialog) {
                InterventionDialog(
                    state = uiState.behaviorState,
                    whyHere = uiState.whyHere,
                    onDismiss = { showInterventionDialog = false },
                    onSave = { note ->
                        viewModel.saveInterventionNote(note)
                        showInterventionDialog = false
                    }
                )
            }

            if (showSetupDialog) {
                SetupDialog(
                    onDismiss = { showSetupDialog = false },
                    onOpenTransparency = {
                        showSetupDialog = false
                        onOpenTransparency()
                    },
                    onReplayOnboarding = {
                        showSetupDialog = false
                        onReplayOnboarding()
                    },
                    onOpenUninstall = {
                        showSetupDialog = false
                        onOpenUninstall()
                    },
                    onOpenVip = {
                        showSetupDialog = false
                        onOpenVip()
                    },
                    onOpenFocus = {
                        showSetupDialog = false
                        onOpenFocus()
                    },
                    onOpenMantra = {
                        showSetupDialog = false
                        onOpenMantra()
                    },
                    onOpenBoredom = {
                        showSetupDialog = false
                        onOpenBoredom()
                    },
                    onExport = {
                        showSetupDialog = false
                        scope.launch {
                            val snapshot = ExportSnapshot(
                                whyHere = prefs.whyHere.first(),
                                mantra = prefs.mantraPhrase.first(),
                                dailyTargetMin = prefs.dailyTargetMin.first(),
                                streakDays = prefs.streakDays.first(),
                                streakBest = prefs.streakBest.first(),
                                vipContacts = prefs.vipContacts.first(),
                                distractionPackages = prefs.distractionPackages.first(),
                                todos = todoRepository.allTodos.first(),
                                projects = projectRepository.activeProjects.first(),
                                journal = journalRepository.recent.first()
                            )
                            val json = ExportBuilder.buildJson(snapshot, System.currentTimeMillis())
                            val send = Intent(Intent.ACTION_SEND).apply {
                                type = "application/json"
                                putExtra(Intent.EXTRA_TEXT, json)
                                putExtra(Intent.EXTRA_SUBJECT, "FocusLauncher export")
                            }
                            context.startActivity(Intent.createChooser(send, "Export data"))
                        }
                    }
                )
            }
        }
    }
}

@Composable
internal fun SoftenBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("soften-banner"),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "HEY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "You've been DROWNING for a few days. The discipline mechanics are paused. " +
                    "If you're not okay, please talk to someone.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "988 — US Suicide & Crisis Lifeline",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
internal fun InterventionBanner(state: String, whyHere: String, onPause: () -> Unit) {
    val color = if (state == "DROWNING") MaterialTheme.colorScheme.error else Color(0xFFF57C00)
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("intervention-banner"),
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "PAUSE",
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = whyHere.ifBlank { "You're in $state. Step away for a minute." },
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = onPause,
                modifier = Modifier.testTag("intervention-pause")
            ) {
                Text("What are you actually doing?")
            }
        }
    }
}

@Composable
internal fun InterventionDialog(
    state: String,
    whyHere: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var note by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("You're in $state") },
        text = {
            Column {
                if (whyHere.isNotBlank()) {
                    Text(
                        text = "Your declaration: \"$whyHere\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.height(12.dp))
                }
                Text("What are you actually doing right now?", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("intervention-note-input"),
                    minLines = 3,
                    placeholder = { Text("Be honest with yourself.") }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = note.trim().isNotEmpty(),
                onClick = { onSave(note) },
                modifier = Modifier.testTag("intervention-save")
            ) { Text("Save & continue") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Dismiss") }
        }
    )
}

@Composable
private fun OneThingCard(
    text: String,
    isForToday: Boolean,
    onSet: (String) -> Unit,
    onClear: () -> Unit
) {
    var draft by remember(text, isForToday) { mutableStateOf(if (isForToday) text else "") }
    val showInput = !isForToday || text.isBlank()

    Column {
        SectionHeader("TODAY'S ONE THING")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().testTag("one-thing"),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (showInput) {
                    Text(
                        text = "If you only do one thing today, what is it?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = draft,
                        onValueChange = { draft = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("one-thing-input"),
                        placeholder = { Text("e.g. ship the onboarding refactor") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { onSet(draft) },
                        enabled = draft.trim().length >= 3,
                        modifier = Modifier
                            .align(Alignment.End)
                            .testTag("one-thing-save")
                    ) { Text("Set as today's one thing") }
                } else {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("one-thing-text")
                    )
                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = onClear,
                        modifier = Modifier
                            .align(Alignment.End)
                            .testTag("one-thing-clear")
                    ) { Text("Change") }
                }
            }
        }
    }
}

@Composable
private fun StreakRow(days: Int, best: Int, focusSessionsToday: Int, timeBankTotalMin: Int) {
    val bankLabel = when {
        timeBankTotalMin >= 60 -> "${timeBankTotalMin / 60}h ${timeBankTotalMin % 60}m"
        else -> "${timeBankTotalMin}m"
    }
    Row(
        modifier = Modifier.fillMaxWidth().testTag("streak-row"),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatChip(label = "STREAK", value = "${days}d", sub = "best ${best}d", modifier = Modifier.weight(1f))
        StatChip(label = "FOCUS", value = "${focusSessionsToday}", sub = "sessions", modifier = Modifier.weight(1f))
        StatChip(label = "BANK", value = bankLabel, sub = "saved", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MorningRoutineCard(
    steps: List<String>,
    done: Set<String>,
    onToggle: (String) -> Unit
) {
    Column(modifier = Modifier.testTag("morning")) {
        SectionHeader("MORNING ROUTINE")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                steps.forEach { step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggle(step) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = step in done, onCheckedChange = { onToggle(step) })
                        Text(
                            text = step,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (step in done) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IdentityVoteCard(
    builder: Int,
    consumer: Int,
    onVoteBuilder: () -> Unit,
    onVoteConsumer: () -> Unit
) {
    Column(modifier = Modifier.testTag("identity")) {
        SectionHeader("WHO ARE YOU RIGHT NOW?")
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IdentityChoice(
                label = "Builder",
                count = builder,
                onClick = onVoteBuilder,
                modifier = Modifier.weight(1f).testTag("identity-builder")
            )
            IdentityChoice(
                label = "Consumer",
                count = consumer,
                onClick = onVoteConsumer,
                modifier = Modifier.weight(1f).testTag("identity-consumer")
            )
        }
    }
}

@Composable
private fun IdentityChoice(
    label: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "$count votes today", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
private fun ShutdownRitualCard(
    steps: List<String>,
    done: Set<String>,
    onToggle: (String) -> Unit
) {
    Column(modifier = Modifier.testTag("shutdown")) {
        SectionHeader("SHUTDOWN RITUAL")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                steps.forEach { step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggle(step) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = step in done, onCheckedChange = { onToggle(step) })
                        Text(
                            text = step,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (step in done) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeatmapStrip(counts: IntArray, phantomBuzzToday: Int, onBuzz: () -> Unit) {
    Column(modifier = Modifier.testTag("heatmap")) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader("LAST 7 DAYS")
            TextButton(
                onClick = onBuzz,
                modifier = Modifier.testTag("phantom-buzz")
            ) {
                Text(if (phantomBuzzToday == 0) "Phantom buzz?" else "Buzz x$phantomBuzzToday")
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            counts.forEach { c ->
                val level = com.erluxman.focuslauncher.service.HeatmapAggregator.level(c)
                val alpha = when (level) {
                    0 -> 0.15f
                    1 -> 0.35f
                    2 -> 0.55f
                    3 -> 0.75f
                    else -> 1f
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha))
                )
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, sub: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 1.5.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = sub,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun WhyHereCard(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("why-here-card"),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "YOUR DECLARATION",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 1.5.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SetupDialog(
    onDismiss: () -> Unit,
    onOpenTransparency: () -> Unit = {},
    onReplayOnboarding: () -> Unit = {},
    onOpenUninstall: () -> Unit = {},
    onOpenVip: () -> Unit = {},
    onOpenFocus: () -> Unit = {},
    onOpenMantra: () -> Unit = {},
    onOpenBoredom: () -> Unit = {},
    onExport: () -> Unit = {}
) {
    val context = LocalContext.current
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(context, FocusDeviceAdminReceiver::class.java)
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var isAdminActive by remember { mutableStateOf(devicePolicyManager.isAdminActive(componentName)) }
    var isDefault by remember { mutableStateOf(isDefaultLauncher(context)) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isAdminActive = devicePolicyManager.isAdminActive(componentName)
                isDefault = isDefaultLauncher(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val roleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        isDefault = isDefaultLauncher(context)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("System Setup") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text("Complete these steps to enable anti-uninstall features and lock your experience.")
                
                Button(
                    onClick = {
                        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required for anti-uninstall protection.")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isAdminActive) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(if (isAdminActive) "Device Admin Active" else "Enable Device Admin")
                    }
                }

                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val roleManager = context.getSystemService(RoleManager::class.java)
                            if (roleManager != null) {
                                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                                roleLauncher.launch(intent)
                            }
                        } else {
                            val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isDefault) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(if (isDefault) "Default Launcher Active" else "Set as Default Launcher")
                    }
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Grant Usage Access") }

                Button(
                    onClick = {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            android.net.Uri.parse("package:${context.packageName}"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Grant Overlay (Dimming)") }

                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Enable Lobby (Accessibility)") }

                TextButton(
                    onClick = onOpenTransparency,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open-transparency")
                ) { Text("Transparency / Techniques") }

                TextButton(
                    onClick = onOpenVip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open-vip")
                ) { Text("VIP Contacts") }

                TextButton(
                    onClick = onOpenFocus,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open-focus")
                ) { Text("Focus Session (25/5)") }

                TextButton(
                    onClick = onOpenMantra,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open-mantra")
                ) { Text("Set Mantra") }

                TextButton(
                    onClick = onOpenBoredom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open-boredom")
                ) { Text("Boredom Preservatory (2 min)") }

                TextButton(
                    onClick = onExport,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open-export")
                ) { Text("Export data (JSON)") }

                TextButton(
                    onClick = onOpenUninstall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open-uninstall")
                ) { Text("Try to leave (72hr cooldown)") }

                TextButton(
                    onClick = onReplayOnboarding,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Replay Onboarding") }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}

private fun isDefaultLauncher(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = context.getSystemService(RoleManager::class.java)
        return roleManager?.isRoleHeld(RoleManager.ROLE_HOME) == true
    }

    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }
    val resolveInfo = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    val currentHomePackage = resolveInfo?.activityInfo?.packageName
    
    if (currentHomePackage == null || currentHomePackage == "android") return false
    
    return currentHomePackage == context.packageName
}

@Composable
fun BehaviorIndicator(
    state: String,
    progress: Float,
    screenMinutes: Int,
    targetMinutes: Int,
    legacyMinutes: Int = 0,
    modifier: Modifier = Modifier
) {
    val stateColor = when(state) {
        "THRIVING" -> MaterialTheme.colorScheme.primary
        "NEUTRAL" -> MaterialTheme.colorScheme.secondary
        "DRIFTING" -> Color(0xFFFBC02D)
        "SINKING" -> Color(0xFFF57C00)
        "DROWNING" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline
    }

    Column(modifier = modifier.testTag("behavior-indicator")) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "CURRENT STATE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 2.sp,
                modifier = Modifier.weight(1f)
            )
            if (legacyMinutes > 0) {
                Text(
                    text = "legacy ${com.erluxman.focuslauncher.service.LegacyCounter.format(legacyMinutes)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.testTag("legacy-label")
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = state,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp
                ),
                color = stateColor,
                modifier = Modifier.testTag("behavior-state-text")
            )
            Text(
                text = "${screenMinutes}m / ${targetMinutes}m",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            color = stateColor
        )
    }
}

@Composable
fun ProjectSection(projects: List<ProjectEntity>) {
    Column {
        SectionHeader("ACTIVE PROJECTS")
        Spacer(modifier = Modifier.height(16.dp))
        if (projects.isEmpty()) {
            ProjectCard("No Active Projects", "Focus on your baseline first.", 0f)
        } else {
            projects.forEach { project ->
                ProjectCard(project.title, project.description, project.progress)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ProjectCard(title: String, subtitle: String, progress: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun TodoSection(
    todos: List<TodoEntity>,
    onToggle: (TodoEntity) -> Unit,
    onAdd: (String) -> Unit,
    onDelete: (TodoEntity) -> Unit,
    onAddWithEstimate: (String, Int?) -> Unit = { t, _ -> onAdd(t) }
) {
    var newTodoText by remember { mutableStateOf("") }
    var estimateText by remember { mutableStateOf("") }

    fun submit() {
        if (newTodoText.isNotBlank()) {
            onAddWithEstimate(newTodoText.trim(), estimateText.toIntOrNull())
            newTodoText = ""
            estimateText = ""
        }
    }

    Column {
        SectionHeader("TODAY")
        Spacer(modifier = Modifier.height(12.dp))

        todos.forEach { todo ->
            TodoItem(todo, onToggle, onDelete)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                modifier = Modifier.weight(1f).testTag("add-task-input"),
                placeholder = { Text("Add task...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onDone = { submit() })
            )
            TextField(
                value = estimateText,
                onValueChange = { estimateText = it.filter { c -> c.isDigit() }.take(3) },
                modifier = Modifier.width(80.dp).testTag("add-task-estimate"),
                placeholder = { Text("min") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { submit() })
            )
            IconButton(onClick = ::submit) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
fun TodoItem(todo: TodoEntity, onToggle: (TodoEntity) -> Unit, onDelete: (TodoEntity) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onToggle(todo) },
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onToggle(todo) }
                .padding(start = 8.dp)
        ) {
            Text(
                text = todo.text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (todo.isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
            )
            if (todo.estimatedMinutes != null) {
                val actual = todo.actualMinutes
                val tag = if (actual != null) "est ${todo.estimatedMinutes}m → actual ${actual}m"
                else "est ${todo.estimatedMinutes}m"
                Text(
                    text = tag,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        IconButton(onClick = { onDelete(todo) }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Delete", tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun WidgetSection() {
    Column {
        SectionHeader("WIDGETS")
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ClockWidget() }
            item { QuoteWidget() }
            item { ProgressWidget() }
            item { EmptyWidgetPlaceholder() }
            item { EmptyWidgetPlaceholder() }
        }
    }
}

@Composable
fun ClockWidget() {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while(true) {
            currentTime = System.currentTimeMillis()
            kotlinx.coroutines.delay(1000)
        }
    }
    
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateSdf = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    WidgetContainer {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = sdf.format(Date(currentTime)), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = dateSdf.format(Date(currentTime)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun QuoteWidget() {
    WidgetContainer {
        Column {
            Text(
                text = "\"Discipline is the bridge between goals and accomplishment.\"",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "— Jim Rohn",
                style = androidx.compose.ui.text.TextStyle(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun ProgressWidget() {
    WidgetContainer {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.size(40.dp),
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Focus", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun EmptyWidgetPlaceholder() {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 100.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Add, contentDescription = "Empty widget slot", tint = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun WidgetContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 100.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.outline,
        letterSpacing = 1.5.sp
    )
}

@Composable
fun AppDock(dockApps: List<AppInfo>, onAppClick: (String) -> Unit, onSearchClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            dockApps.forEach { app ->
                DockIcon(app, onClick = { onAppClick(app.packageName) })
            }
            
            repeat(5 - dockApps.size) {
                DockPlaceholder()
            }

            VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 4.dp))

            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun DockIcon(app: AppInfo, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        app.icon?.let {
            Image(
                bitmap = it.toBitmap().asImageBitmap(),
                contentDescription = app.label,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun DockPlaceholder() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
    )
}

@Composable
fun SearchOverlay(
    query: String,
    apps: List<AppInfo>,
    onQueryChange: (String) -> Unit,
    onAppClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val filteredApps = remember(query, apps) {
        if (query.isBlank()) emptyList()
        else apps.filter { it.label.contains(query, ignoreCase = true) }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.98f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Type app name...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (filteredApps.isNotEmpty()) {
                        onAppClick(filteredApps.first().packageName)
                    }
                })
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredApps) { app ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAppClick(app.packageName) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        app.icon?.let {
                            Image(
                                bitmap = it.toBitmap().asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = app.label, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                Text("Close")
            }
        }
    }
}

@Composable
private fun FocusPointsChip(points: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("focus-points"),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "FOCUS POINTS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 1.5.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                "$points",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AnchorChip(distractionMinutes: Int) {
    val delta = com.erluxman.focuslauncher.service.AnchorMath.delta(distractionMinutes)
    val tone = if (delta <= 0) MaterialTheme.colorScheme.primary
               else MaterialTheme.colorScheme.error
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("anchor-chip"),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "MOST DISCIPLINED USER",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "${com.erluxman.focuslauncher.service.AnchorMath.ANCHOR_MINUTES} min today",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = if (delta > 0) "You: $distractionMinutes min  (+$delta over)"
                else if (delta < 0) "You: $distractionMinutes min  (${delta} under)"
                else "You: $distractionMinutes min  (matched)",
                style = MaterialTheme.typography.bodySmall,
                color = tone
            )
        }
    }
}

@Composable
private fun DomainStreaksRow(streaks: Map<String, Pair<Int, Int>>) {
    val domains = listOf("focus" to "FOCUS", "creation" to "CREATE", "sleep" to "SLEEP")
    Column(modifier = Modifier.testTag("domain-streaks")) {
        SectionHeader("DOMAIN STREAKS")
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            domains.forEach { (key, label) ->
                val (days, best) = streaks[key] ?: (0 to 0)
                StatChip(
                    label = label,
                    value = "${days}d",
                    sub = "best ${best}d",
                    modifier = Modifier.weight(1f).testTag("domain-$key")
                )
            }
        }
    }
}

@Composable
private fun FutureLetterBanner(entry: String, onDismiss: () -> Unit) {
    val parts = entry.split("|", limit = 4)
    val text = parts.getOrNull(3).orEmpty()
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("future-letter-banner"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "LETTER FROM A STRONGER YOU",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 1.5.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.testTag("future-letter-dismiss")
            ) { Text("Got it") }
        }
    }
}

@Composable
private fun TombstoneSection(
    entries: List<String>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    Column(modifier = Modifier.testTag("tombstones")) {
        SectionHeader("TOMBSTONES")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Apps you've sworn off.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(8.dp))
                entries.forEach { entry ->
                    val parts = entry.split("|", limit = 2)
                    val label = parts.getOrNull(0).orEmpty()
                    val date = parts.getOrNull(1).orEmpty()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "RIP $label",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            date,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        TextButton(
                            onClick = { onRemove(entry) },
                            modifier = Modifier.testTag("tombstone-remove-$label")
                        ) { Text("Resurrect") }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it.take(40) },
                        placeholder = { Text("App you've quit") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("tombstone-input")
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onAdd(newName.trim())
                            newName = ""
                        },
                        enabled = newName.isNotBlank(),
                        modifier = Modifier.testTag("tombstone-add")
                    ) { Text("Bury") }
                }
            }
        }
    }
}

@Composable
private fun FutureLetterComposer(onSave: (String, String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var daysOut by remember { mutableStateOf("14") }
    Column(modifier = Modifier.testTag("future-letter-composer")) {
        SectionHeader("WRITE TO FUTURE SELF")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Write a note now. It will surface on a future weak day.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it.take(600) },
                    placeholder = { Text("Dear future me...") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth().testTag("future-letter-text")
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = daysOut,
                        onValueChange = { daysOut = it.filter { c -> c.isDigit() }.take(3) },
                        placeholder = { Text("days") },
                        singleLine = true,
                        modifier = Modifier.width(80.dp).testTag("future-letter-days"),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("days out", style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            val n = daysOut.toIntOrNull() ?: 14
                            val cal = java.util.Calendar.getInstance().apply {
                                add(java.util.Calendar.DAY_OF_YEAR, n)
                            }
                            val iso = java.text.SimpleDateFormat(
                                "yyyy-MM-dd",
                                java.util.Locale.US
                            ).format(cal.time)
                            onSave(iso, text)
                            text = ""
                        },
                        enabled = text.isNotBlank(),
                        modifier = Modifier.testTag("future-letter-save")
                    ) { Text("Schedule") }
                }
            }
        }
    }
}

@Composable
private fun InsightsCard(
    distractionMinutes: Int,
    hourlyRate: Int,
    userAge: Int,
    todosCompletedToday: Int,
    focusSessionsToday: Int,
    timeBankTotalMin: Int,
    onSetHourlyRate: (Int) -> Unit = {},
    onSetAge: (Int) -> Unit = {}
) {
    val cost = com.erluxman.focuslauncher.service.InsightMath
        .opportunityCost(distractionMinutes, hourlyRate.toDouble())
    val lifeHours = com.erluxman.focuslauncher.service.InsightMath
        .lifetimeHoursOnScreen(distractionMinutes, userAge)
    val ratio = com.erluxman.focuslauncher.service.InsightMath
        .ioRatio(distractionMinutes, todosCompletedToday, focusSessionsToday)
    val projection = com.erluxman.focuslauncher.service.InsightMath
        .compoundedBalance(timeBankTotalMin, days = 365)

    Column(modifier = Modifier.testTag("insights-card")) {
        SectionHeader("INSIGHTS")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (hourlyRate > 0) {
                    InsightLine(
                        label = "Cost today",
                        value = "$${"%.2f".format(cost)}",
                        sub = "${distractionMinutes}m × \$$hourlyRate/hr"
                    )
                }
                if (userAge in 1..120) {
                    Spacer(Modifier.height(8.dp))
                    InsightLine(
                        label = "If you keep this rate",
                        value = "${lifeHours / 24 / 365} life-years",
                        sub = "on screen, before age 80"
                    )
                }
                Spacer(Modifier.height(8.dp))
                InsightLine(
                    label = "I/O ratio",
                    value = "%.2f".format(ratio),
                    sub = "creation ÷ consumption today"
                )
                Spacer(Modifier.height(8.dp))
                InsightLine(
                    label = "Bank in 1 year",
                    value = formatBankProjection(projection),
                    sub = "compounded at ${"%.1f".format(com.erluxman.focuslauncher.service.InsightMath.DAILY_RATE * 100)}%/day"
                )
                if (hourlyRate == 0 || userAge == 0) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Personalize",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        var rate by remember { mutableStateOf("") }
                        var age by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = rate,
                            onValueChange = { rate = it.filter { c -> c.isDigit() }.take(4) },
                            placeholder = { Text("$/hr") },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("insight-rate-input"),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword
                            )
                        )
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it.filter { c -> c.isDigit() }.take(3) },
                            placeholder = { Text("age") },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("insight-age-input"),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword
                            )
                        )
                        Button(
                            onClick = {
                                rate.toIntOrNull()?.let(onSetHourlyRate)
                                age.toIntOrNull()?.let(onSetAge)
                            },
                            modifier = Modifier.testTag("insight-save-button")
                        ) { Text("Save") }
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightLine(label: String, value: String, sub: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(sub, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

private fun formatBankProjection(min: Int): String = when {
    min >= 60 * 24 -> "${min / (60 * 24)}d ${(min / 60) % 24}h"
    min >= 60 -> "${min / 60}h ${min % 60}m"
    else -> "${min}m"
}

@Composable
private fun GraceRow(
    freezes: Int,
    graceDays: Set<String>,
    onDeclareTomorrow: (String) -> Unit,
    onCancelTomorrow: (String) -> Unit
) {
    val tomorrowIso = remember {
        val cal = java.util.Calendar.getInstance().apply {
            add(java.util.Calendar.DAY_OF_YEAR, 1)
        }
        java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(cal.time)
    }
    val isTomorrowGrace = tomorrowIso in graceDays
    val monthIso = remember { tomorrowIso.substring(0, 7) }
    val canDeclare = com.erluxman.focuslauncher.service.GraceLogic.canAddGrace(monthIso, graceDays)

    Column(modifier = Modifier.testTag("grace-row")) {
        SectionHeader("GRACE")
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatChip(
                label = "FREEZES",
                value = "$freezes",
                sub = "in reserve",
                modifier = Modifier.weight(1f).testTag("grace-freezes")
            )
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable(enabled = isTomorrowGrace || canDeclare) {
                        if (isTomorrowGrace) onCancelTomorrow(tomorrowIso)
                        else onDeclareTomorrow(tomorrowIso)
                    }
                    .testTag("grace-declare-tomorrow"),
                shape = RoundedCornerShape(12.dp),
                color = if (isTomorrowGrace)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "TOMORROW",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = if (isTomorrowGrace) "Grace ON" else "Declare grace",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (canDeclare || isTomorrowGrace) "tap to toggle"
                        else "month limit reached",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun AfterFallCard(
    steps: List<String>,
    done: Set<String>,
    onToggle: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.testTag("after-fall")) {
        SectionHeader("AFTER THE FALL")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Your streak broke. That's information, not a verdict.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                steps.forEach { step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggle(step) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = step in done, onCheckedChange = { onToggle(step) })
                        Text(
                            text = step,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onDismiss,
                    enabled = done.size >= steps.size,
                    modifier = Modifier.fillMaxWidth().testTag("after-fall-done")
                ) { Text("Recommit and continue") }
            }
        }
    }
}

@Composable
private fun TimeDebtCard(debtMin: Int, effectiveTarget: Int, baseTarget: Int) {
    Column(modifier = Modifier.testTag("time-debt-card")) {
        SectionHeader("TIME DEBT")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Yesterday you overspent. Today's budget is tighter.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Effective target: ${effectiveTarget}m  (base ${baseTarget}m − debt ${debtMin}m × 2)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun MoodPingCard(
    recentPings: List<String>,
    onLog: (String, String) -> Unit
) {
    var note by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("") }
    val emojis = listOf(":)", ":|", ":(", ":D", ":/", "<3")

    Column(modifier = Modifier.testTag("mood-ping-card")) {
        SectionHeader("MOOD PING")
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "How are you right now?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    emojis.forEach { e ->
                        val active = e == selectedEmoji
                        Surface(
                            modifier = Modifier
                                .clickable { selectedEmoji = e }
                                .testTag("mood-emoji-${emojis.indexOf(e)}"),
                            shape = RoundedCornerShape(8.dp),
                            color = if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = e,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it.take(40) },
                    placeholder = { Text("One word") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("mood-note-input")
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (selectedEmoji.isNotBlank()) {
                            onLog(selectedEmoji, note)
                            selectedEmoji = ""
                            note = ""
                        }
                    },
                    enabled = selectedEmoji.isNotBlank(),
                    modifier = Modifier.testTag("mood-log-button")
                ) { Text("Log it") }
                if (recentPings.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Recent:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    recentPings.take(3).forEach { line ->
                        val parts = line.split("|")
                        val stamp = parts.getOrNull(0).orEmpty()
                        val emoji = parts.getOrNull(1).orEmpty()
                        val n = parts.getOrNull(2).orEmpty()
                        Text(
                            text = "$stamp  $emoji  $n",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DreamModeScreen(oneThingText: String, onExitDream: () -> Unit) {
    val quotes = listOf(
        "Tomorrow is built tonight.",
        "Rest is not the opposite of progress.",
        "Sleep is the deal you made with reality.",
        "What you let go of tonight, you wake up free of."
    )
    val quote = remember {
        val day = (System.currentTimeMillis() / (24L * 60 * 60 * 1000)).toInt()
        quotes[((day % quotes.size) + quotes.size) % quotes.size]
    }
    var breathIn by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(4000)
            breathIn = !breathIn
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("dream-mode"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DREAM MODE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 4.sp
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = if (breathIn) "Breathe in…" else "…and out.",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("dream-breath")
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = quote,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            if (oneThingText.isNotBlank()) {
                Spacer(Modifier.height(40.dp))
                Text(
                    text = "Tomorrow's one thing:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = oneThingText,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.testTag("dream-one-thing")
                )
            }
        }
    }
}
