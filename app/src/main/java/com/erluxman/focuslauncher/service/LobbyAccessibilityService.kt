package com.erluxman.focuslauncher.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import com.erluxman.focuslauncher.data.prefs.PrefKeys
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.ui.lobby.LobbyActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LobbyAccessibilityService : AccessibilityService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val distractionsFlow = MutableStateFlow<Set<String>>(emptySet())
    private val lobbyEnabledFlow = MutableStateFlow(true)
    private val dimmingEnabledFlow = MutableStateFlow(true)
    private val escalatingEnabledFlow = MutableStateFlow(true)
    private val variableRatioEnabledFlow = MutableStateFlow(true)
    private val streakDaysFlow = MutableStateFlow(0)
    private val focusSessionsTodayFlow = MutableStateFlow(0)
    private val trackLevelFlow = MutableStateFlow(1)
    private val nourishingFlow = MutableStateFlow<Set<String>>(emptySet())
    private val lockedTodayFlow = MutableStateFlow<Set<String>>(emptySet())
    private val unlockCountsFlow = MutableStateFlow<Set<String>>(emptySet())
    private var lastIntercept: Pair<String, Long>? = null
    private var lastDistractionPackage: String? = null
    private var lastDistractionStartMs: Long = 0L
    private val visitCountToday = mutableMapOf<String, Int>()
    private var visitCountDate: String = ""

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            notificationTimeout = 100
        }

        val prefs = UserPrefs(applicationContext)
        scope.launch { prefs.distractionPackages.collect { distractionsFlow.value = it } }
        scope.launch { prefs.technique(PrefKeys.TECH_LOBBY).collect { lobbyEnabledFlow.value = it } }
        scope.launch { prefs.technique(PrefKeys.TECH_DIMMING).collect { dimmingEnabledFlow.value = it } }
        scope.launch { prefs.technique(PrefKeys.TECH_ESCALATING).collect { escalatingEnabledFlow.value = it } }
        scope.launch { prefs.technique(PrefKeys.TECH_VARIABLE_RATIO).collect { variableRatioEnabledFlow.value = it } }
        scope.launch { prefs.streakDays.collect { streakDaysFlow.value = it } }
        scope.launch { prefs.focusSessionsToday.collect { focusSessionsTodayFlow.value = it } }
        scope.launch { prefs.trackLevel.collect { trackLevelFlow.value = it } }
        scope.launch { prefs.nourishingPackages.collect { nourishingFlow.value = it } }
        scope.launch { prefs.lockedTodayPackages.collect { lockedTodayFlow.value = it } }
        scope.launch { prefs.unlockCounts.collect { unlockCountsFlow.value = it } }
    }

    private fun todayIsoLocal(): String =
        java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())

    private fun bumpAndGetVisitOrdinal(pkg: String): Int {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
        if (today != visitCountDate) {
            visitCountToday.clear()
            visitCountDate = today
        }
        val current = visitCountToday[pkg] ?: 0
        visitCountToday[pkg] = current + 1
        return current
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pkg = event.packageName?.toString() ?: return
        if (pkg == packageName) return

        val distractions = distractionsFlow.value
        val nourishing = nourishingFlow.value
        val isDistraction = pkg in distractions && pkg !in nourishing

        // Per-foreground transition handling: dimming + session receipts + overstay lockout.
        if (lastDistractionPackage != null && lastDistractionPackage != pkg) {
            // Leaving (or switching out of) a distraction app.
            val exitedPkg = lastDistractionPackage!!
            val elapsed = System.currentTimeMillis() - lastDistractionStartMs
            val toastText = SessionReceipt.format(elapsed, prettyLabel(exitedPkg))
            if (toastText != null) {
                android.widget.Toast.makeText(this, toastText, android.widget.Toast.LENGTH_LONG).show()
            }
            if (IntentPromise.didOverstay(elapsed)) {
                val today = todayIsoLocal()
                scope.launch { UserPrefs(applicationContext).lockPackageForToday(exitedPkg, today) }
                android.widget.Toast.makeText(
                    this,
                    "${prettyLabel(exitedPkg)} locked for the rest of today — you overstayed.",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
            if (!isDistraction) {
                DimmingOverlay.stop(this)
                lastDistractionPackage = null
            }
        }
        if (isDistraction) {
            if (lastDistractionPackage != pkg) {
                lastDistractionPackage = pkg
                lastDistractionStartMs = System.currentTimeMillis()
                if (dimmingEnabledFlow.value) DimmingOverlay.start(this)
            }
        }

        if (!lobbyEnabledFlow.value) return
        if (!isDistraction) return

        // If this package is locked-for-today, redirect home and toast.
        val today = todayIsoLocal()
        if (pkg in lockedTodayFlow.value) {
            android.widget.Toast.makeText(
                this,
                "${prettyLabel(pkg)} is locked until tomorrow.",
                android.widget.Toast.LENGTH_LONG
            ).show()
            val home = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(home)
            return
        }

        // Debounce: don't re-intercept the same package within 30s.
        val now = SystemClock.elapsedRealtime()
        val (lastPkg, lastTs) = lastIntercept ?: (null to 0L)
        if (lastPkg == pkg && now - lastTs < 30_000L) return
        lastIntercept = pkg to now

        scope.launch { UserPrefs(applicationContext).bumpUnlockCount(pkg, today) }
        val nextCount = UnlockIntervention.parseCount(unlockCountsFlow.value, today, pkg) + 1
        val visitOrdinal = bumpAndGetVisitOrdinal(pkg)
        // Track-level eases the Lobby base by one second per level above 1.
        val baseSeconds = TrackSystem.easedBaseSeconds(LobbyTuner.BASE_SECONDS, trackLevelFlow.value)
        val seconds = LobbyTuner.countdownSeconds(
            base = baseSeconds,
            visitOrdinal = visitOrdinal,
            escalating = escalatingEnabledFlow.value,
            variableRatio = variableRatioEnabledFlow.value,
            randomRoll = kotlin.random.Random.Default.nextDouble(),
            userLevel = 0  // legacy graduated-freedom ease already folded into baseSeconds
        )
        val harderMath = LobbyTuner.isHarderMath(
            variableRatio = variableRatioEnabledFlow.value,
            randomRoll = kotlin.random.Random.Default.nextDouble()
        )

        val intent = Intent(this, LobbyActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(LobbyActivity.EXTRA_TARGET_PACKAGE, pkg)
            putExtra(LobbyActivity.EXTRA_COUNTDOWN_SECONDS, seconds)
            putExtra(LobbyActivity.EXTRA_HARDER_MATH, harderMath)
            putExtra(LobbyActivity.EXTRA_INTERVENTION_COUNT,
                if (UnlockIntervention.shouldShow(nextCount)) nextCount else 0)
        }
        startActivity(intent)
    }

    override fun onInterrupt() = Unit

    override fun onDestroy() {
        super.onDestroy()
        DimmingOverlay.stop(this)
        scope.cancel()
    }

    private fun prettyLabel(pkg: String): String = try {
        val pm = packageManager
        pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString()
    } catch (_: Exception) {
        pkg
    }
}
