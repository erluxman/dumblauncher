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
import com.erluxman.focuslauncher.service.UsageStatsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
    val whyHere: String = ""
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
            _uiState.update {
                it.copy(
                    behaviorState = reading.state,
                    behaviorProgress = reading.progress,
                    screenMinutesToday = reading.screenMinutes
                )
            }
        }
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
            dock.addAll(others.take(3))

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
            todoRepository.update(todo.copy(isCompleted = !todo.isCompleted))
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
