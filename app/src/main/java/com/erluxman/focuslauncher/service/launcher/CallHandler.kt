package com.erluxman.focuslauncher.service.launcher

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * CALLS-003 Hover Window + CALLS-004 Auto-Reply on Decline.
 *
 * On every PHONE_STATE broadcast we get the (just-changed) state and an
 * EXTRA_INCOMING_NUMBER. We:
 *   - On RINGING: post a heads-up notification ("Hover") if the number isn't
 *     in the VIP list, with a one-tap "Decline + Auto-Reply" action.
 *   - On IDLE: clear the notification.
 *
 * The companion [CallAutoReplyReceiver] handles the action button by
 * sending the configured SMS to the last ringing number.
 */
class PhoneStateReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE) ?: return
        val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        when (state) {
            TelephonyManager.EXTRA_STATE_RINGING -> {
                if (number.isNullOrBlank()) return
                scope.launch {
                    val vip = UserPrefs(context.applicationContext).vipContacts.first()
                    val normalized = number.filter { it.isDigit() || it == '+' }
                    val isVip = vip.any { v ->
                        v.filter { it.isDigit() || it == '+' } == normalized
                    }
                    if (!isVip) {
                        CallHover.show(context.applicationContext, number)
                    }
                }
            }
            TelephonyManager.EXTRA_STATE_IDLE -> CallHover.clear(context.applicationContext)
        }
    }
}

object CallHover {
    const val CHANNEL_ID = "call_hover"
    const val NOTIFICATION_ID = 7707
    const val EXTRA_NUMBER = "extra_number"
    const val ACTION_AUTO_REPLY = "com.erluxman.focuslauncher.CALL_AUTO_REPLY"

    fun show(context: Context, number: String) {
        ensureChannel(context)
        val replyIntent = Intent(context, CallAutoReplyReceiver::class.java).apply {
            action = ACTION_AUTO_REPLY
            putExtra(EXTRA_NUMBER, number)
        }
        val replyPi = PendingIntent.getBroadcast(
            context, 0, replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION") Notification.Builder(context)
        }
        val notification = builder
            .setSmallIcon(android.R.drawable.sym_call_incoming)
            .setContentTitle("Incoming call (non-VIP)")
            .setContentText("From $number")
            .addAction(0, "Decline + Auto-Reply", replyPi)
            .setOngoing(true)
            .build()
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, notification)
    }

    fun clear(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_ID)
    }

    private fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                nm.createNotificationChannel(NotificationChannel(
                    CHANNEL_ID,
                    "Call hover",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Non-VIP incoming calls" })
            }
        }
    }
}

class CallAutoReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != CallHover.ACTION_AUTO_REPLY) return
        val number = intent.getStringExtra(CallHover.EXTRA_NUMBER) ?: return
        val body = AutoReplyTemplates.pick()
        try {
            val sms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION") SmsManager.getDefault()
            }
            sms.sendTextMessage(number, null, body, null, null)
        } catch (_: SecurityException) {
            // No SEND_SMS permission granted; silently no-op.
        }
        CallHover.clear(context)
    }
}

object AutoReplyTemplates {
    private val lines = listOf(
        "I'm in focus mode and can't take calls right now. I'll get back to you tonight.",
        "Phone is locked down today. Text me what's up and I'll reply when I'm free.",
        "On a deep-work block. If it's urgent, text 'urgent' and I'll see it."
    )
    fun pick(seed: Long = System.currentTimeMillis()): String =
        lines[(((seed % lines.size) + lines.size) % lines.size).toInt()]
}
