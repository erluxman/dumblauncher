package com.erluxman.focuslauncher.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

/**
 * Scaffold per project-plan §7 Week 2. No behavior wired up yet — exists so the
 * permission can be granted in onboarding and so future restrictions
 * (RESTRICT-015 batched delivery, dismissal-on-distraction, etc.) have a host.
 */
class FocusNotificationListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Listener connected; ${activeNotifications?.size ?: 0} active")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        // intentional no-op for MVP
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        // intentional no-op for MVP
    }

    companion object {
        private const val TAG = "FocusNotifListener"
    }
}
