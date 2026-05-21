package com.erluxman.focuslauncher.ui.home.viewmodel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.erluxman.focuslauncher.data.local.entity.JournalEntryEntity
import com.erluxman.focuslauncher.data.local.entity.ProjectEntity
import com.erluxman.focuslauncher.data.local.entity.TodoEntity
import com.erluxman.focuslauncher.data.prefs.PrefKeys
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.data.repository.JournalRepository
import com.erluxman.focuslauncher.data.repository.ProjectRepository
import com.erluxman.focuslauncher.data.repository.TodoRepository
import com.erluxman.focuslauncher.model.AppInfo
import com.erluxman.focuslauncher.service.StreakLogic
import com.erluxman.focuslauncher.service.UsageStatsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class HomeUiState(
    val projects: List<ProjectEntity> = emptyList(),
    val todos: List<TodoEntity> = emptyList(),
    val apps: List<AppInfo> = emptyList(),
    val dockApps: List<AppInfo> = emptyList(),
    val behaviorState: String = "THRIVING",
    val behaviorProgress: Float = 1f,
    val behaviorIndicatorEnabled: Boolean = true,
    val screenMinutesToday: Int = 0,
    val dailyTargetMin: Int = 180,
    val whyHere: String = "",
    val oneThingText: String = "",
    val oneThingIsForToday: Boolean = false,
    val streakDays: Int = 0,
    val streakBest: Int = 0,
    val focusSessionsToday: Int = 0,
    val timeBankTotalMin: Int = 0,
    val phantomBuzzToday: Int = 0,
    val morningStepsDone: Set<String> = emptySet(),
    val shutdownStepsDone: Set<String> = emptySet(),
    val heatmapPerDay: IntArray = IntArray(7),
    val isMorningDoneToday: Boolean = false,
    val isShutdownDoneToday: Boolean = false,
    val identityBuilderToday: Int = 0,
    val identityConsumerToday: Int = 0,
    val legacyBuilderMinutes: Int = 0,
    val crisisActive: Boolean = false
)

class HomeViewModel(
    private val todoRepository: TodoRepository,
    private val projectRepository: ProjectRepository,
    private val journalRepository: JournalRepository,
    private val packageManager: PackageManager,
    private val prefs: UserPrefs,
    private val appContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
        loadApps()
        seedInitialData()
        observePrefs()
        startUsagePolling()
        rollStreakIfNewDay()
        observeHeatmap()
    }

    private fun observeHeatmap() {
        viewModelScope.launch {
            val sinceMs = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
            todoRepository.completedSince(sinceMs).collect { timestamps ->
                val now = System.currentTimeMillis()
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                }
                val per = com.erluxman.focuslauncher.service.HeatmapAggregator.perDayCounts(
                    completionsMs = timestamps,
                    nowMs = now,
                    dayStartLocalMs = cal.timeInMillis,
                    days = 7
                )
                // Legacy: cumulative builder minutes across all-time completions + focus sessions.
                // todos completed in the last 7 days is a decent proxy until we add a sums query;
                // for total we re-use the same flow size (cheap, scoped to recent activity).
                val totalTodos = timestamps.size
                val totalSessions = _uiState.value.focusSessionsToday  // today only; placeholder
                val legacy = com.erluxman.focuslauncher.service.LegacyCounter.totalBuilderMinutes(
                    completedTodos = totalTodos,
                    focusSessions = totalSessions
                )
                _uiState.update { it.copy(heatmapPerDay = per, legacyBuilderMinutes = legacy) }
            }
        }
    }

    private fun todayIso(): String = isoFmt.format(Date())
    private fun yesterdayIso(): String {
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        return isoFmt.format(cal.time)
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                todoRepository.allTodos,
                projectRepository.activeProjects
            ) { todos, projects ->
                _uiState.update { it.copy(todos = todos, projects = projects) }
            }.collect()
        }
    }

    private fun observePrefs() {
        viewModelScope.launch {
            combine(
                prefs.whyHere,
                prefs.dailyTargetMin,
                prefs.technique(PrefKeys.TECH_BEHAVIOR_INDICATOR)
            ) { why, target, indicator ->
                Triple(why, target, indicator)
            }.collect { (why, target, indicator) ->
                _uiState.update {
                    it.copy(
                        whyHere = why,
                        dailyTargetMin = target,
                        behaviorIndicatorEnabled = indicator
                    )
                }
            }
        }
        viewModelScope.launch {
            combine(
                prefs.oneThingText,
                prefs.oneThingDate,
                prefs.streakDays,
                prefs.streakBest,
                prefs.focusSessionsToday
            ) { text, date, days, best, sessions ->
                listOf(text, date, days, best, sessions)
            }.collect { (text, date, days, best, sessions) ->
                _uiState.update {
                    it.copy(
                        oneThingText = text as String,
                        oneThingIsForToday = (date as String) == todayIso(),
                        streakDays = days as Int,
                        streakBest = best as Int,
                        focusSessionsToday = sessions as Int
                    )
                }
            }
        }

        viewModelScope.launch {
            combine(
                prefs.timeBankTotalMin,
                prefs.phantomBuzzCount,
                prefs.phantomBuzzDate,
                prefs.morningDoneDate,
                prefs.morningDoneSteps
            ) { bank, phantom, phantomDate, mDate, mSteps ->
                listOf(bank, phantom, phantomDate, mDate, mSteps)
            }.collect { vals ->
                val bank = vals[0] as Int
                val phantom = vals[1] as Int
                val phantomDate = vals[2] as String
                val mDate = vals[3] as String
                @Suppress("UNCHECKED_CAST")
                val mSteps = vals[4] as Set<String>
                val today = todayIso()
                _uiState.update {
                    it.copy(
                        timeBankTotalMin = bank,
                        phantomBuzzToday = if (phantomDate == today) phantom else 0,
                        morningStepsDone = if (mDate == today) mSteps else emptySet(),
                        isMorningDoneToday = mDate == today && mSteps.size >= MORNING_STEPS.size
                    )
                }
            }
        }

        viewModelScope.launch {
            combine(
                prefs.shutdownDoneDate,
                prefs.shutdownDoneSteps,
                prefs.identityBuilderCount,
                prefs.identityConsumerCount,
                prefs.identityDate
            ) { sDate, sSteps, builder, consumer, iDate ->
                listOf(sDate, sSteps, builder, consumer, iDate)
            }.collect { vals ->
                val today = todayIso()
                val sDate = vals[0] as String
                @Suppress("UNCHECKED_CAST")
                val sSteps = vals[1] as Set<String>
                val builder = vals[2] as Int
                val consumer = vals[3] as Int
                val iDate = vals[4] as String
                _uiState.update {
                    it.copy(
                        shutdownStepsDone = if (sDate == today) sSteps else emptySet(),
                        isShutdownDoneToday = sDate == today && sSteps.size >= SHUTDOWN_STEPS.size,
                        identityBuilderToday = if (iDate == today) builder else 0,
                        identityConsumerToday = if (iDate == today) consumer else 0
                    )
                }
            }
        }
    }

    private fun rollStreakIfNewDay() {
        viewModelScope.launch {
            val lastCheck = prefs.streakLastCheckDate.first()
            val today = todayIso()
            if (lastCheck == today) return@launch
            val yesterday = yesterdayIso()
            val target = prefs.dailyTargetMin.first().coerceAtLeast(1)
            val yesterdayMinutes = runCatching {
                UsageStatsHelper.screenMinutesForDay(appContext, daysAgo = 1)
            }.getOrDefault(0)
            val hit = yesterdayMinutes <= target
            val update = StreakLogic.update(
                todayIso = today,
                lastCheckIso = lastCheck,
                currentDays = prefs.streakDays.first(),
                currentBest = prefs.streakBest.first(),
                yesterdayIso = yesterday,
                yesterdayHitTarget = hit
            )
            if (update.persist) prefs.applyStreak(update.days, update.best, today)
        }
    }

    fun setOneThing(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch { prefs.setOneThing(text.trim(), todayIso()) }
    }

    fun clearOneThing() {
        viewModelScope.launch { prefs.clearOneThing() }
    }


    /** Polls usage stats every 30s and recomputes behavior reading. */
    private fun startUsagePolling() {
        viewModelScope.launch {
            while (true) {
                refreshUsage()
                delay(30_000)
            }
        }
    }

    fun refreshUsage() {
        viewModelScope.launch {
            val target = _uiState.value.dailyTargetMin.takeIf { it > 0 } ?: 180
            val minutes = runCatching { UsageStatsHelper.todayScreenMinutes(appContext) }
                .getOrDefault(0)
            val reading = UsageStatsHelper.deriveBehaviorState(minutes, target)
            // Recent behavior states (today + 3 prior) → crisis detection.
            val recent = buildList {
                add(reading.state)
                for (d in 1..3) {
                    val m = runCatching {
                        UsageStatsHelper.screenMinutesForDay(appContext, daysAgo = d)
                    }.getOrDefault(0)
                    add(UsageStatsHelper.deriveBehaviorState(m, target).state)
                }
            }
            val crisis = com.erluxman.focuslauncher.service.CrisisDetector.isCrisis(recent)
            _uiState.update {
                it.copy(
                    behaviorState = reading.state,
                    behaviorProgress = reading.progress,
                    screenMinutesToday = reading.screenMinutes,
                    crisisActive = crisis
                )
            }
            val lastDeposit = prefs.timeBankLastDate.first()
            if (lastDeposit != todayIso()) {
                val yMin = runCatching {
                    UsageStatsHelper.screenMinutesForDay(appContext, daysAgo = 1)
                }.getOrDefault(target)
                val saved = (target - yMin).coerceAtLeast(0)
                prefs.depositTimeBank(saved, todayIso())
            }
        }
    }

    fun bumpPhantomBuzz() {
        viewModelScope.launch { prefs.bumpPhantomBuzz(todayIso()) }
    }

    fun toggleMorningStep(step: String) {
        viewModelScope.launch { prefs.toggleMorningStep(step, todayIso()) }
    }

    fun toggleShutdownStep(step: String) {
        viewModelScope.launch { prefs.toggleShutdownStep(step, todayIso()) }
    }

    fun voteIdentity(isBuilder: Boolean) {
        viewModelScope.launch { prefs.voteIdentity(isBuilder, todayIso()) }
    }

    private fun seedInitialData() {
        viewModelScope.launch {
            val currentProjects = projectRepository.activeProjects.first()
            if (currentProjects.isEmpty()) {
                projectRepository.insert(ProjectEntity(title = "Build FocusLauncher", description = "Complete CORE features", progress = 0.4f))
                projectRepository.insert(ProjectEntity(title = "Health Sprint", description = "Morning routine consistency", progress = 0.2f))
            }

            val currentTodos = todoRepository.allTodos.first()
            if (currentTodos.isEmpty()) {
                todoRepository.insert(TodoEntity(text = "Register for Device Admin"))
                todoRepository.insert(TodoEntity(text = "Set FocusLauncher as Default"))
            }
        }
    }

    private fun loadApps() {
        viewModelScope.launch {
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolvedApps = packageManager.queryIntentActivities(intent, 0)
            val allApps = resolvedApps.map {
                AppInfo(
                    label = it.loadLabel(packageManager).toString(),
                    packageName = it.activityInfo.packageName,
                    icon = it.loadIcon(packageManager)
                )
            }
                .filter { it.packageName != "com.erluxman.focuslauncher" }
                .distinctBy { it.packageName }
                .sortedBy { it.label.lowercase() }

            val dialIntent = Intent(Intent.ACTION_DIAL)
            val phoneApp = packageManager.resolveActivity(dialIntent, 0)?.let { info ->
                allApps.find { it.packageName == info.activityInfo.packageName }
            }

            val smsIntent = Intent(Intent.ACTION_SENDTO).apply { data = Uri.parse("smsto:") }
            val smsApp = packageManager.resolveActivity(smsIntent, 0)?.let { info ->
                allApps.find { it.packageName == info.activityInfo.packageName }
            }

            val dock = mutableListOf<AppInfo>()
            phoneApp?.let { dock.add(it) }
            smsApp?.let { dock.add(it) }

            val others = allApps.filter { it.packageName != phoneApp?.packageName && it.packageName != smsApp?.packageName }
            val rouletteSeed = (System.currentTimeMillis() / (24L * 60 * 60 * 1000))
            val rotated = com.erluxman.focuslauncher.service.RouletteShuffler.shuffle(others, rouletteSeed)
            dock.addAll(rotated.take(3))

            _uiState.update { it.copy(apps = allApps, dockApps = dock.take(5)) }
        }
    }

    fun addTodo(text: String) {
        viewModelScope.launch {
            todoRepository.insert(TodoEntity(text = text))
        }
    }

    fun toggleTodo(todo: TodoEntity) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val newCompleted = !todo.isCompleted
            todoRepository.update(
                todo.copy(
                    isCompleted = newCompleted,
                    completedAt = if (newCompleted) now else null
                )
            )
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            todoRepository.delete(todo)
        }
    }

    fun launchApp(context: Context, packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        }
    }

    fun saveInterventionNote(text: String) {
        if (text.isBlank()) return
        val current = _uiState.value
        viewModelScope.launch {
            journalRepository.insert(
                JournalEntryEntity(
                    text = text.trim(),
                    behaviorState = current.behaviorState,
                    screenMinutes = current.screenMinutesToday
                )
            )
        }
    }

    companion object {
        private val isoFmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val MORNING_STEPS = listOf(
            "Drink a glass of water",
            "Stretch for two minutes",
            "No phone for 30 minutes after waking"
        )

        val SHUTDOWN_STEPS = listOf(
            "Review tomorrow's calendar",
            "Set tomorrow's one thing",
            "One-line journal entry"
        )

        fun provideFactory(
            todoRepository: TodoRepository,
            projectRepository: ProjectRepository,
            journalRepository: JournalRepository,
            packageManager: PackageManager,
            prefs: UserPrefs,
            appContext: Context
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(todoRepository, projectRepository, journalRepository, packageManager, prefs, appContext) as T
            }
        }
    }
}
