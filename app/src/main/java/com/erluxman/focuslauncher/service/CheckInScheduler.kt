package com.erluxman.focuslauncher.service

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

/**
 * RESTRICT-015 Reverse Notifications.
 *
 * Schedules two repeating daily check-ins via [AlarmManager]:
 *   - Morning routine reminder at 9:00.
 *   - Shutdown ritual reminder at 21:00.
 *
 * The receiver below posts a regular notification when fired.
 */
object CheckInScheduler {

    const val CHANNEL_ID = "focus_check_ins"
    private const val REQ_MORNING = 5101
    private const val REQ_SHUTDOWN = 5102

    fun scheduleAll(context: Context) {
        ensureChannel(context)
        scheduleAt(context, REQ_MORNING, hour = 9, minute = 0, action = ACTION_MORNING)
        scheduleAt(context, REQ_SHUTDOWN, hour = 21, minute = 0, action = ACTION_SHUTDOWN)
    }

    fun cancelAll(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        listOf(REQ_MORNING to ACTION_MORNING, REQ_SHUTDOWN to ACTION_SHUTDOWN).forEach { (req, action) ->
            am.cancel(pendingIntent(context, req, action))
        }
    }

    const val ACTION_MORNING = "com.erluxman.focuslauncher.CHECKIN_MORNING"
    const val ACTION_SHUTDOWN = "com.erluxman.focuslauncher.CHECKIN_SHUTDOWN"

    private fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                nm.createNotificationChannel(NotificationChannel(
                    CHANNEL_ID,
                    "Check-ins",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Morning and shutdown reminders"
                })
            }
        }
    }

    private fun scheduleAt(context: Context, requestCode: Int, hour: Int, minute: Int, action: String) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = pendingIntent(context, requestCode, action)
        val triggerAt = nextTrigger(hour, minute)
        am.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            AlarmManager.INTERVAL_DAY,
            pi
        )
    }

    fun nextTrigger(hour: Int, minute: Int, nowMs: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = nowMs
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= nowMs) add(Calendar.DAY_OF_YEAR, 1)
        }
        return cal.timeInMillis
    }

    private fun pendingIntent(context: Context, requestCode: Int, action: String): PendingIntent {
        val intent = Intent(context, CheckInReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}

class CheckInReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val (title, body) = when (intent.action) {
            CheckInScheduler.ACTION_MORNING -> "Morning check-in" to
                "Hydrate, stretch, and set today's One Thing."
            CheckInScheduler.ACTION_SHUTDOWN -> "Shutdown check-in" to
                "Tomorrow is built tonight — run the shutdown ritual."
            else -> "Check-in" to "It's time."
        }
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CheckInScheduler.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .build()
        }
        nm.notify(intent.action.hashCode(), notification)
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CheckInScheduler.scheduleAll(context)
        }
    }
}
