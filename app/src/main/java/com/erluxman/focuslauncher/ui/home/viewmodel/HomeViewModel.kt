package com.erluxman.focuslauncher.ui.home.viewmodel
import com.erluxman.focuslauncher.data.prefs.removeHighlight
import com.erluxman.focuslauncher.data.prefs.addHighlight
import com.erluxman.focuslauncher.data.prefs.highlights
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
import com.erluxman.focuslauncher.service.launcher.HealthSource
import com.erluxman.focuslauncher.service.lobby.RouletteShuffler
import com.erluxman.focuslauncher.service.lobby.TimeDebt
import com.erluxman.focuslauncher.service.launcher.UsageStatsHelper

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
import com.erluxman.focuslauncher.service.tracks.StreakLogic
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
    val unlocksToday: Int = 0,
    val morningStepsDone: Set<String> = emptySet(),
    val shutdownStepsDone: Set<String> = emptySet(),
    val heatmapPerDay: IntArray = IntArray(7),
    val isMorningDoneToday: Boolean = false,
    val isShutdownDoneToday: Boolean = false,
    val identityBuilderToday: Int = 0,
    val identityConsumerToday: Int = 0,
    val legacyBuilderMinutes: Int = 0,
    val crisisActive: Boolean = false,
    val timeDebtMin: Int = 0,
    val effectiveTargetMin: Int = 180,
    val moodPings: List<String> = emptyList(),
    val isDreamMode: Boolean = false,
    val sleepCutoffHour: Int = com.erluxman.focuslauncher.service.lobby.SleepWindow.DEFAULT_CUTOFF_HOUR,
    val sleepWakeHour: Int = com.erluxman.focuslauncher.service.lobby.SleepWindow.DEFAULT_WAKE_HOUR,
    val graceDays: Set<String> = emptySet(),
    val streakFreezes: Int = 0,
    val afterFallDueDate: String = "",
    val afterFallStepsDone: Set<String> = emptySet(),
    val hourlyRateUsd: Int = 0,
    val userAge: Int = 0,
    val mortalityWidgetsOptIn: Boolean = false,
    val anchoringEnabled: Boolean = true,
    val caffeineDoses: List<com.erluxman.focuslauncher.service.habits.CaffeineMath.Dose> = emptyList(),
    val drinks: List<com.erluxman.focuslauncher.service.habits.HangoverMath.Drink> = emptyList(),
    val meditationSessions: List<com.erluxman.focuslauncher.service.habits.MeditationLog.Session> = emptyList(),
    val parkedIdeas: List<com.erluxman.focuslauncher.service.insights.ParkedIdea.Item> = emptyList(),
    val readingSessions: List<com.erluxman.focuslauncher.service.habits.ReadingLog.Session> = emptyList(),
    val workoutSessions: List<com.erluxman.focuslauncher.service.fitness.WorkoutLog.Session> = emptyList(),
    val commitEntries: List<com.erluxman.focuslauncher.service.habits.CommitLog.Entry> = emptyList(),
    val personalRecords: List<String> = emptyList(),
    val travelVisits: List<com.erluxman.focuslauncher.service.places.TravelAtlas.Visit> = emptyList(),
    val subscriptions: List<com.erluxman.focuslauncher.service.money.SubscriptionMath.Item> = emptyList(),
    val moneyIncome: Int = 0,
    val moneyExpense: Int = 0,
    val moneyAssets: Int = 0,
    val moneyLiabilities: Int = 0,
    val highlights: Set<String> = emptySet(),
    val onboardingCompletedAt: Long = 0L,
    val distractionMinutesToday: Int = 0,
    val todosCompletedToday: Int = 0,
    val appTombstones: List<String> = emptyList(),
    val dueFutureLetter: String = "",
    val domainStreaks: Map<String, Pair<Int, Int>> = emptyMap(),
    val focusPoints: Int = 0,
    val energyZones: Set<String> = emptySet(),
    val currentHour: Int = 0,
    val trackLevel: Int = 1,
    val trackPoints: Int = 0,
    val trackMisses: Int = 0,
    val trackRecalibrated: Boolean = false,
    val builderMode: Boolean = false,
    val sadVoice: String = "STERN",
    val sadSuppressedUntilMs: Long = 0L,
    val celebrationMessage: String = "",
    val nourishingPackages: Set<String> = emptySet(),
    val baselineProposedTarget: Int? = null,
    val hourlyMinutes: IntArray = IntArray(24),
    val domainTracks: Map<String, Triple<Int, Int, Int>> = emptyMap(),  // domain → (level, points, miss)
    val emergencyPasses: Int = 5,
    val calendarEvents: List<com.erluxman.focuslauncher.service.launcher.CalendarReader.Event> = emptyList(),
    val activeEventTitle: String = "",
    val isFocusBlock: Boolean = false,
    val stepsToday: Int = 0,
    val sleepMinutesLastNight: Int = 0
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
        rollTimeDebtIfNewDay()
        rollDomainStreaks()
        ensureEmergencyPassesForWeek()
        recordBaselineSample()
        observeHeatmap()
        startDreamModeClock()
    }

    private fun recordBaselineSample() {
        viewModelScope.launch {
            if (prefs.baselineApplied.first()) return@launch
            val today = todayIso()
            val mins = runCatching { UsageStatsHelper.screenMinutesForDay(appContext, 1) }
                .getOrDefault(0)
            // Use yesterday's full-day minutes as a sample (today is incomplete).
            val yIso = yesterdayIso()
            prefs.addBaselineSample(yIso, mins)
            val samples = prefs.baselineSamples.first()
                .mapNotNull { it.split("|").getOrNull(1)?.toIntOrNull() }
            if (com.erluxman.focuslauncher.service.tracks.BaselineDetector.isComplete(samples)) {
                val target = com.erluxman.focuslauncher.service.tracks.BaselineDetector
                    .proposedTarget(samples)
                if (target != null) {
                    _uiState.update { it.copy(baselineProposedTarget = target) }
                }
            }
        }
    }

    fun acceptBaseline(target: Int) {
        viewModelScope.launch {
            prefs.setDailyTargetMin(target)
            prefs.markBaselineApplied()
            _uiState.update { it.copy(baselineProposedTarget = null) }
        }
    }

    fun rejectBaseline() {
        viewModelScope.launch {
            prefs.markBaselineApplied()
            _uiState.update { it.copy(baselineProposedTarget = null) }
        }
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
                val per = com.erluxman.focuslauncher.service.insights.HeatmapAggregator.perDayCounts(
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
                val legacy = com.erluxman.focuslauncher.service.insights.LegacyCounter.totalBuilderMinutes(
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
                _uiState.update {
                    val today = todayIso()
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                    }
                    val todosToday = todos.count { t ->
                        t.isCompleted && (t.completedAt ?: 0L) >= cal.timeInMillis
                    }
                    it.copy(todos = todos, projects = projects, todosCompletedToday = todosToday)
                }
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
                val debt = _uiState.value.timeDebtMin
                _uiState.update {
                    it.copy(
                        whyHere = why,
                        dailyTargetMin = target,
                        behaviorIndicatorEnabled = indicator,
                        effectiveTargetMin = com.erluxman.focuslauncher.service.lobby.TimeDebt
                            .effectiveTarget(target, debt)
                    )
                }
            }
        }

        viewModelScope.launch {
            combine(prefs.timeDebtMin, prefs.dailyTargetMin) { debt, base -> debt to base }
                .collect { (debt, base) ->
                    _uiState.update {
                        it.copy(
                            timeDebtMin = debt,
                            effectiveTargetMin = com.erluxman.focuslauncher.service.lobby.TimeDebt
                                .effectiveTarget(base, debt)
                        )
                    }
                }
        }

        viewModelScope.launch {
            prefs.moodPings.collect { set ->
                _uiState.update { it.copy(moodPings = set.sortedDescending()) }
            }
        }

        viewModelScope.launch {
            combine(prefs.hourlyRateUsd, prefs.userAge) { rate, age -> rate to age }
                .collect { (rate, age) ->
                    _uiState.update { it.copy(hourlyRateUsd = rate, userAge = age) }
                }
        }

        viewModelScope.launch {
            prefs.mortalityWidgetsOptIn.collect { v ->
                _uiState.update { it.copy(mortalityWidgetsOptIn = v) }
            }
        }

        viewModelScope.launch {
            prefs.technique(com.erluxman.focuslauncher.data.prefs.PrefKeys.TECH_ANCHORING).collect { v ->
                _uiState.update { it.copy(anchoringEnabled = v) }
            }
        }

        viewModelScope.launch {
            combine(prefs.sleepCutoffHour, prefs.sleepWakeHour) { c, w -> c to w }
                .collect { (c, w) ->
                    _uiState.update { it.copy(sleepCutoffHour = c, sleepWakeHour = w) }
                }
        }

        viewModelScope.launch {
            prefs.meditationLog.collect { set ->
                val sessions = com.erluxman.focuslauncher.service.habits.MeditationLog.parse(set)
                _uiState.update { it.copy(meditationSessions = sessions) }
            }
        }

        viewModelScope.launch {
            prefs.ideaParking.collect { set ->
                _uiState.update {
                    it.copy(parkedIdeas = com.erluxman.focuslauncher.service.insights.ParkedIdea.parse(set))
                }
            }
        }

        viewModelScope.launch {
            prefs.readingLog.collect { set ->
                _uiState.update {
                    it.copy(readingSessions = com.erluxman.focuslauncher.service.habits.ReadingLog.parse(set))
                }
            }
        }

        viewModelScope.launch {
            prefs.workoutLog.collect { set ->
                _uiState.update {
                    it.copy(workoutSessions = com.erluxman.focuslauncher.service.fitness.WorkoutLog.parse(set))
                }
            }
        }

        viewModelScope.launch {
            prefs.commitLog.collect { set ->
                _uiState.update {
                    it.copy(commitEntries = com.erluxman.focuslauncher.service.habits.CommitLog.parse(set))
                }
            }
        }

        viewModelScope.launch {
            prefs.unlockCounts.collect { set ->
                val today = todayIso()
                val count = set.filter { it.startsWith("$today|") }
                    .sumOf { e -> e.substringAfterLast("|").toIntOrNull() ?: 0 }
                _uiState.update { it.copy(unlocksToday = count) }
            }
        }

        viewModelScope.launch {
            prefs.prWall.collect { set ->
                _uiState.update { it.copy(personalRecords = set.sortedDescending()) }
            }
        }

        viewModelScope.launch {
            prefs.travelAtlas.collect { set ->
                _uiState.update {
                    it.copy(travelVisits = com.erluxman.focuslauncher.service.places.TravelAtlas.parse(set))
                }
            }
        }

        viewModelScope.launch {
            prefs.subscriptions.collect { set ->
                _uiState.update {
                    it.copy(subscriptions = com.erluxman.focuslauncher.service.money.SubscriptionMath.parse(set))
                }
            }
        }

        viewModelScope.launch {
            combine(
                prefs.moneyIncome, prefs.moneyExpense, prefs.moneyAssets, prefs.moneyLiabilities
            ) { i, e, a, l -> intArrayOf(i, e, a, l) }.collect { arr ->
                _uiState.update { it.copy(
                    moneyIncome = arr[0], moneyExpense = arr[1],
                    moneyAssets = arr[2], moneyLiabilities = arr[3]
                ) }
            }
        }

        viewModelScope.launch {
            prefs.highlights.collect { set ->
                _uiState.update { it.copy(highlights = set) }
            }
        }

        viewModelScope.launch {
            prefs.onboardingCompletedAt.collect { ms ->
                _uiState.update { it.copy(onboardingCompletedAt = ms) }
            }
        }

        viewModelScope.launch {
            prefs.drinkLog.collect { set ->
                val drinks = set.mapNotNull { entry ->
                    val parts = entry.split("|", limit = 2)
                    if (parts.size != 2) return@mapNotNull null
                    val ts = parts[0].toLongOrNull() ?: return@mapNotNull null
                    val units = parts[1].toDoubleOrNull() ?: return@mapNotNull null
                    com.erluxman.focuslauncher.service.habits.HangoverMath.Drink(units = units, takenAtMs = ts)
                }.sortedBy { it.takenAtMs }
                _uiState.update { it.copy(drinks = drinks) }
            }
        }

        viewModelScope.launch {
            prefs.caffeineDoses.collect { set ->
                val doses = set.mapNotNull { entry ->
                    val (tsStr, mgStr) = entry.split("|", limit = 2).let {
                        if (it.size != 2) return@mapNotNull null
                        it[0] to it[1]
                    }
                    val ts = tsStr.toLongOrNull() ?: return@mapNotNull null
                    val mg = mgStr.toIntOrNull() ?: return@mapNotNull null
                    com.erluxman.focuslauncher.service.habits.CaffeineMath.Dose(mg = mg, takenAtMs = ts)
                }.sortedBy { it.takenAtMs }
                _uiState.update { it.copy(caffeineDoses = doses) }
            }
        }

        viewModelScope.launch {
            prefs.appTombstones.collect { set ->
                _uiState.update { it.copy(appTombstones = set.sorted()) }
            }
        }

        viewModelScope.launch {
            prefs.futureLetters.collect { set ->
                val today = todayIso()
                val due = set.firstOrNull { e ->
                    val parts = e.split("|", limit = 4)
                    parts.size == 4 && parts[1] <= today && parts[2] == "0"
                }
                _uiState.update { it.copy(dueFutureLetter = due.orEmpty()) }
            }
        }

        viewModelScope.launch {
            prefs.domainStreaks.collect { set ->
                val map = set.mapNotNull { e ->
                    val parts = e.split("|", limit = 4)
                    if (parts.size != 4) return@mapNotNull null
                    val days = parts[1].toIntOrNull() ?: return@mapNotNull null
                    val best = parts[2].toIntOrNull() ?: return@mapNotNull null
                    parts[0] to (days to best)
                }.toMap()
                _uiState.update { it.copy(domainStreaks = map) }
            }
        }

        viewModelScope.launch {
            prefs.focusPoints.collect { p ->
                _uiState.update { it.copy(focusPoints = p) }
            }
        }

        viewModelScope.launch {
            prefs.energyZones.collect { set ->
                _uiState.update { it.copy(energyZones = set) }
            }
        }

        viewModelScope.launch {
            combine(
                prefs.trackLevel,
                prefs.trackPoints,
                prefs.trackMisses,
                prefs.trackRecalibrated,
                prefs.builderMode
            ) { lvl, pts, miss, recal, builder ->
                listOf(lvl, pts, miss, recal, builder)
            }.collect { vals ->
                _uiState.update {
                    it.copy(
                        trackLevel = vals[0] as Int,
                        trackPoints = vals[1] as Int,
                        trackMisses = vals[2] as Int,
                        trackRecalibrated = vals[3] as Boolean,
                        builderMode = vals[4] as Boolean
                    )
                }
                checkCelebration()
            }
        }

        viewModelScope.launch {
            combine(prefs.sadVoice, prefs.sadSuppressedUntil) { v, until -> v to until }
                .collect { (v, until) ->
                    _uiState.update { it.copy(sadVoice = v, sadSuppressedUntilMs = until) }
                }
        }

        viewModelScope.launch {
            prefs.nourishingPackages.collect { set ->
                _uiState.update { it.copy(nourishingPackages = set) }
            }
        }

        viewModelScope.launch {
            prefs.domainTracks.collect { set ->
                val map = set.mapNotNull { e ->
                    val parts = e.split("|", limit = 5)
                    if (parts.size != 5) return@mapNotNull null
                    val level = parts[1].toIntOrNull() ?: return@mapNotNull null
                    val points = parts[2].toIntOrNull() ?: return@mapNotNull null
                    val miss = parts[3].toIntOrNull() ?: return@mapNotNull null
                    parts[0] to Triple(level, points, miss)
                }.toMap()
                _uiState.update { it.copy(domainTracks = map) }
            }
        }

        viewModelScope.launch {
            prefs.emergencyPasses.collect { n ->
                _uiState.update { it.copy(emergencyPasses = n) }
            }
        }

        viewModelScope.launch {
            combine(
                prefs.graceDays,
                prefs.streakFreezes,
                prefs.afterFallDue,
                prefs.afterFallSteps
            ) { grace, freezes, dueDate, steps ->
                listOf(grace, freezes, dueDate, steps)
            }.collect { vals ->
                @Suppress("UNCHECKED_CAST")
                val grace = vals[0] as Set<String>
                val freezes = vals[1] as Int
                val due = vals[2] as String
                @Suppress("UNCHECKED_CAST")
                val steps = vals[3] as Set<String>
                _uiState.update {
                    it.copy(
                        graceDays = grace,
                        streakFreezes = freezes,
                        afterFallDueDate = due,
                        afterFallStepsDone = steps
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
            val rawHit = yesterdayMinutes <= target
            val currentDays = prefs.streakDays.first()
            val currentBest = prefs.streakBest.first()
            val graceSet = prefs.graceDays.first()
            val freezes = prefs.streakFreezes.first()

            // If the user would have broken: try grace day, then freeze.
            val hit = if (rawHit) true else when (
                com.erluxman.focuslauncher.service.tracks.GraceLogic.resolveBreak(yesterday, graceSet, freezes)
            ) {
                com.erluxman.focuslauncher.service.tracks.GraceOutcome.GracedByDay -> true
                com.erluxman.focuslauncher.service.tracks.GraceOutcome.GracedByFreeze -> {
                    prefs.setStreakFreezes((freezes - 1).coerceAtLeast(0))
                    true
                }
                com.erluxman.focuslauncher.service.tracks.GraceOutcome.Broken -> {
                    prefs.setAfterFallDue(today)
                    false
                }
            }
            val update = StreakLogic.update(
                todayIso = today,
                lastCheckIso = lastCheck,
                currentDays = currentDays,
                currentBest = currentBest,
                yesterdayIso = yesterday,
                yesterdayHitTarget = hit
            )
            if (update.persist) {
                prefs.applyStreak(update.days, update.best, today)
                // Auto-grant freezes as user accumulates perfect days.
                val earned = com.erluxman.focuslauncher.service.tracks.GraceLogic.earnedFreezes(update.days)
                if (earned > freezes) prefs.setStreakFreezes(earned)

                // Track System rollover: one day at a time.
                val snap = com.erluxman.focuslauncher.service.tracks.TrackSystem.Snapshot(
                    level = prefs.trackLevel.first(),
                    pointsToward = prefs.trackPoints.first(),
                    missStreak = prefs.trackMisses.first()
                )
                val next = com.erluxman.focuslauncher.service.tracks.TrackSystem.applyDay(snap, hit)
                prefs.applyTrackSnapshot(next.level, next.pointsToward, next.missStreak, next.recalibrated)
            }
        }
    }

    fun ackTrackRecalibration() {
        viewModelScope.launch { prefs.clearRecalibrated() }
    }

    fun setBuilderMode(value: Boolean) {
        viewModelScope.launch { prefs.setBuilderMode(value) }
    }

    fun setSadVoice(voice: String) {
        viewModelScope.launch { prefs.setSadVoice(voice) }
    }

    fun dismissIntervention() {
        viewModelScope.launch { prefs.bumpSadDismissAndSuppress(System.currentTimeMillis()) }
    }

    fun dismissCelebration() {
        _uiState.update { it.copy(celebrationMessage = "") }
    }

    private fun checkCelebration() {
        viewModelScope.launch {
            val streak = prefs.streakDays.first()
            val level = prefs.trackLevel.first()
            val lastStreak = prefs.celebrationLastStreak.first()
            val lastLevel = prefs.celebrationLastLevel.first()
            val levelUp = level > lastLevel
            val milestone = com.erluxman.focuslauncher.service.sad.CelebrationSelf
                .milestone(streak, levelUp)
                ?.takeIf { streak != lastStreak || levelUp }
            if (milestone != null) {
                val msg = com.erluxman.focuslauncher.service.sad.CelebrationSelf.pick(
                    milestone, (System.currentTimeMillis() / (24L * 60 * 60 * 1000)).toInt()
                )
                _uiState.update { it.copy(celebrationMessage = msg) }
                prefs.setCelebrationLast(streak, level)
            }
        }
    }

    fun declareGraceDay(dateIso: String) {
        viewModelScope.launch { prefs.addGraceDay(dateIso) }
    }

    fun cancelGraceDay(dateIso: String) {
        viewModelScope.launch { prefs.removeGraceDay(dateIso) }
    }

    fun toggleAfterFallStep(step: String) {
        viewModelScope.launch { prefs.toggleAfterFallStep(step) }
    }

    fun dismissAfterFall() {
        viewModelScope.launch { prefs.clearAfterFall() }
    }

    fun addTombstone(label: String) {
        viewModelScope.launch { prefs.addTombstone(label, todayIso()) }
    }

    fun removeTombstone(entry: String) {
        viewModelScope.launch { prefs.removeTombstone(entry) }
    }

    fun saveFutureLetter(deliverDateIso: String, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch { prefs.addFutureLetter(deliverDateIso, text) }
    }

    fun dismissDueLetter() {
        val due = _uiState.value.dueFutureLetter
        if (due.isBlank()) return
        viewModelScope.launch { prefs.markLetterDelivered(due) }
    }

    private fun rollDomainStreaks() {
        viewModelScope.launch {
            val today = todayIso()
            val current = prefs.domainStreaks.first()
            val currentTracks = prefs.domainTracks.first()
            fun forDomain(domain: String): Triple<Int, Int, String> {
                val entry = current.firstOrNull { it.startsWith("$domain|") }
                val parts = entry?.split("|", limit = 4) ?: return Triple(0, 0, "")
                if (parts.size != 4) return Triple(0, 0, "")
                return Triple(parts[1].toIntOrNull() ?: 0, parts[2].toIntOrNull() ?: 0, parts[3])
            }

            fun trackFor(domain: String): com.erluxman.focuslauncher.service.tracks.TrackSystem.Snapshot {
                val entry = currentTracks.firstOrNull { it.startsWith("$domain|") }
                val parts = entry?.split("|", limit = 5)
                return if (parts != null && parts.size == 5) {
                    com.erluxman.focuslauncher.service.tracks.TrackSystem.Snapshot(
                        level = parts[1].toIntOrNull() ?: 1,
                        pointsToward = parts[2].toIntOrNull() ?: 0,
                        missStreak = parts[3].toIntOrNull() ?: 0
                    )
                } else com.erluxman.focuslauncher.service.tracks.TrackSystem.Snapshot(1, 0, 0)
            }

            suspend fun rollTrack(domain: String, hit: Boolean, lastDate: String) {
                if (lastDate == today) return
                val next = com.erluxman.focuslauncher.service.tracks.TrackSystem.applyDay(trackFor(domain), hit)
                prefs.updateDomainTrack(domain, next.level, next.pointsToward, next.missStreak, today)
            }

            // FOCUS: at least one focus session yesterday → continues; today's date prevents re-roll.
            val focus = forDomain("focus")
            if (focus.third != today) {
                val sessionsYesterday = prefs.focusSessionsToday.first().takeIf {
                    prefs.focusSessionsDate.first() == yesterdayIso()
                } ?: 0
                val hit = sessionsYesterday > 0
                val (days, best) = streakNext(focus.first, focus.second, hit)
                prefs.updateDomainStreak("focus", days, best, today)
                rollTrack("focus", hit, focus.third)
            }

            // CREATION: at least one todo completed yesterday → continues.
            val creation = forDomain("creation")
            if (creation.third != today) {
                val cal = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -1)
                    set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                }
                val yStart = cal.timeInMillis
                val yEnd = yStart + 24L * 60 * 60 * 1000
                val recent = todoRepository.completedSince(yStart).first()
                val hit = recent.any { it in yStart until yEnd }
                val (days, best) = streakNext(creation.first, creation.second, hit)
                prefs.updateDomainStreak("creation", days, best, today)
                rollTrack("creation", hit, creation.third)
            }

            // SLEEP: presence of morning steps done yesterday = went to bed on time proxy.
            val sleep = forDomain("sleep")
            if (sleep.third != today) {
                val mDate = prefs.morningDoneDate.first()
                val mSteps = prefs.morningDoneSteps.first()
                val hit = mDate == yesterdayIso() && mSteps.size >= MORNING_STEPS.size
                val (days, best) = streakNext(sleep.first, sleep.second, hit)
                prefs.updateDomainStreak("sleep", days, best, today)
                rollTrack("sleep", hit, sleep.third)
            }
        }
    }

    private fun ensureEmergencyPassesForWeek() {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            val week = "%d-W%02d".format(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.WEEK_OF_YEAR)
            )
            prefs.resetEmergencyPassesIfNewWeek(week)
        }
    }

    fun spendEmergencyPass() {
        viewModelScope.launch { prefs.spendEmergencyPass() }
    }

    private fun streakNext(currentDays: Int, currentBest: Int, hit: Boolean): Pair<Int, Int> {
        val next = if (hit) currentDays + 1 else 0
        return next to maxOf(currentBest, next)
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
            val target = _uiState.value.effectiveTargetMin
                .takeIf { it > 0 } ?: (_uiState.value.dailyTargetMin.takeIf { it > 0 } ?: 180)
            val minutes = runCatching { UsageStatsHelper.todayScreenMinutes(appContext) }
                .getOrDefault(0)
            val distractions = prefs.distractionPackages.first()
            val distractionMin = runCatching {
                UsageStatsHelper.todayMinutesByPackage(appContext, distractions)
                    .values.sum()
            }.getOrDefault(0)
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
            val crisis = com.erluxman.focuslauncher.service.lobby.CrisisDetector.isCrisis(recent)
            val hourly = runCatching { UsageStatsHelper.todayHourlyMinutes(appContext) }
                .getOrDefault(IntArray(24))
            val steps = runCatching { com.erluxman.focuslauncher.service.launcher.HealthSource.todaySteps(appContext) }
                .getOrDefault(0)
            val sleepMin = runCatching { com.erluxman.focuslauncher.service.launcher.HealthSource.lastNightSleepMinutes(appContext) }
                .getOrDefault(0)
            _uiState.update {
                it.copy(
                    behaviorState = reading.state,
                    behaviorProgress = reading.progress,
                    screenMinutesToday = reading.screenMinutes,
                    distractionMinutesToday = distractionMin,
                    crisisActive = crisis,
                    hourlyMinutes = hourly,
                    stepsToday = steps,
                    sleepMinutesLastNight = sleepMin
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
        viewModelScope.launch {
            val today = todayIso()
            prefs.toggleShutdownStep(step, today)
            // After this toggle, if shutdown is fully complete and we haven't summarized today, write a day summary.
            val doneSteps = prefs.shutdownDoneSteps.first()
            val isDone = doneSteps.size >= SHUTDOWN_STEPS.size
            val alreadySummarized = today in prefs.daySummaryDates.first()
            if (isDone && !alreadySummarized) {
                val state = _uiState.value
                val text = com.erluxman.focuslauncher.service.insights.DaySummary.build(
                    dateIso = today,
                    screenMinutes = state.screenMinutesToday,
                    distractionMinutes = state.distractionMinutesToday,
                    targetMinutes = state.dailyTargetMin,
                    todosCompleted = state.todosCompletedToday,
                    focusSessions = state.focusSessionsToday,
                    oneThing = state.oneThingText
                )
                journalRepository.insert(
                    com.erluxman.focuslauncher.data.local.entity.JournalEntryEntity(
                        text = text,
                        behaviorState = state.behaviorState,
                        screenMinutes = state.screenMinutesToday
                    )
                )
                prefs.markDaySummary(today)
            }
        }
    }

    fun addNourishing(pkg: String) {
        if (pkg.isBlank()) return
        viewModelScope.launch {
            val current = prefs.nourishingPackages.first()
            prefs.setNourishingPackages(current + pkg.trim())
        }
    }

    fun removeNourishing(pkg: String) {
        viewModelScope.launch {
            val current = prefs.nourishingPackages.first()
            prefs.setNourishingPackages(current - pkg)
        }
    }

    fun voteIdentity(isBuilder: Boolean) {
        viewModelScope.launch { prefs.voteIdentity(isBuilder, todayIso()) }
    }

    fun logMoodPing(emoji: String, note: String) {
        viewModelScope.launch {
            val stamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())
            prefs.addMoodPing(stamp, emoji, note)
        }
    }

    private fun rollTimeDebtIfNewDay() {
        viewModelScope.launch {
            val today = todayIso()
            val lastDebtDate = prefs.timeDebtDate.first()
            if (lastDebtDate == today) return@launch
            val baseTarget = prefs.dailyTargetMin.first().coerceAtLeast(1)
            val currentDebt = prefs.timeDebtMin.first()
            val yesterdaysEffective = com.erluxman.focuslauncher.service.lobby.TimeDebt
                .effectiveTarget(baseTarget, currentDebt)
            val yesterdayMin = runCatching {
                UsageStatsHelper.screenMinutesForDay(appContext, daysAgo = 1)
            }.getOrDefault(yesterdaysEffective)
            val newDebt = com.erluxman.focuslauncher.service.lobby.TimeDebt
                .nextDebt(currentDebt, yesterdayMin, yesterdaysEffective)
            prefs.setTimeDebt(newDebt, today)
        }
    }

    private fun startDreamModeClock() {
        viewModelScope.launch {
            while (true) {
                val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val cutoff = prefs.sleepCutoffHour.first()
                val wake = prefs.sleepWakeHour.first()
                val baseDream = com.erluxman.focuslauncher.service.lobby.SleepWindow
                    .isInWindow(hour, cutoffHour = cutoff, wakeHour = wake)
                // Respect the user's transparency toggle for Dream Mode.
                // Default ON; can be turned off (even in debug builds) via the toggle.
                val enabled = prefs.technique(com.erluxman.focuslauncher.data.prefs.PrefKeys.TECH_DREAM).first()
                val dream = baseDream && enabled
                val events = runCatching {
                    com.erluxman.focuslauncher.service.launcher.CalendarReader.todayEvents(appContext)
                }.getOrDefault(emptyList())
                val active = com.erluxman.focuslauncher.service.launcher.CalendarReader.activeEvent(events)
                val isFocus = com.erluxman.focuslauncher.service.launcher.CalendarReader.isFocusBlock(active)
                _uiState.update {
                    it.copy(
                        isDreamMode = dream,
                        currentHour = hour,
                        calendarEvents = events,
                        activeEventTitle = active?.title.orEmpty(),
                        isFocusBlock = isFocus
                    )
                }
                delay(60_000)
            }
        }
    }

    fun setEnergyZone(window: String, energy: String) {
        viewModelScope.launch { prefs.setEnergyZone(window, energy) }
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
            val rotated = com.erluxman.focuslauncher.service.lobby.RouletteShuffler.shuffle(others, rouletteSeed)
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
            val actual = if (newCompleted) ((now - todo.createdAt) / (60 * 1000)).toInt() else null
            todoRepository.update(
                todo.copy(
                    isCompleted = newCompleted,
                    completedAt = if (newCompleted) now else null,
                    actualMinutes = actual
                )
            )
            // Reward / refund a focus point on completion / uncompletion.
            prefs.addFocusPoints(if (newCompleted) 1 else -1)
        }
    }

    fun addTodoWithEstimate(text: String, estimateMin: Int?) {
        viewModelScope.launch {
            todoRepository.insert(TodoEntity(text = text, estimatedMinutes = estimateMin))
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

        const val DREAM_MODE_START_HOUR = 22  // 10pm
        const val DREAM_MODE_END_HOUR = 5     // 5am

        val AFTER_FALL_STEPS = listOf(
            "Reflect: write one line about what happened",
            "Recommit: set tomorrow's one thing, smaller scope",
            "Tell someone (optional)"
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
