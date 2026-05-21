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
    private var lastIntercept: Pair<String, Long>? = null

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
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pkg = event.packageName?.toString() ?: return
        if (pkg == packageName) return
        if (!lobbyEnabledFlow.value) return
        if (pkg !in distractionsFlow.value) return

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
        scope.cancel()
    }
}
