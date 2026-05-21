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
    private var lastIntercept: Pair<String, Long>? = null
    private var lastDistractionPackage: String? = null
    private var lastDistractionStartMs: Long = 0L

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
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pkg = event.packageName?.toString() ?: return
        if (pkg == packageName) return

        val distractions = distractionsFlow.value
        val isDistraction = pkg in distractions

        // Per-foreground transition handling: dimming + session receipts.
        if (lastDistractionPackage != null && lastDistractionPackage != pkg) {
            // Leaving (or switching out of) a distraction app.
            val elapsed = System.currentTimeMillis() - lastDistractionStartMs
            val toastText = SessionReceipt.format(elapsed, prettyLabel(lastDistractionPackage!!))
            if (toastText != null) {
                android.widget.Toast.makeText(this, toastText, android.widget.Toast.LENGTH_LONG).show()
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

        // Debounce: don't re-intercept the same package within 30s.
        val now = SystemClock.elapsedRealtime()
        val (lastPkg, lastTs) = lastIntercept ?: (null to 0L)
        if (lastPkg == pkg && now - lastTs < 30_000L) return
        lastIntercept = pkg to now

        val intent = Intent(this, LobbyActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(LobbyActivity.EXTRA_TARGET_PACKAGE, pkg)
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
