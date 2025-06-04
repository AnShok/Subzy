package com.anshok.subzy.util.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.anshok.subzy.domain.reminder.model.ReminderType
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.model.Subscription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.time.*

object ReminderManager {

    fun scheduleReminderForSubscription(context: Context, subscription: Subscription) {
        if (!areNotificationsEnabled(context)) return
        if (subscription.reminderType == ReminderType.NONE) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // ✅ Проверка для Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return
        }

        val notifyTime = getNotificationTime(context)
        val paymentDate = Instant.ofEpochMilli(subscription.nextPaymentDate)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val reminderDate = when (subscription.reminderType) {
            ReminderType.ONE_DAY -> paymentDate.minusDays(1)
            ReminderType.THREE_DAYS -> paymentDate.minusDays(3)
            ReminderType.SEVEN_DAYS -> paymentDate.minusDays(7)
            else -> return
        }

        val notifyDateTime = reminderDate.atTime(notifyTime.first, notifyTime.second)
        val triggerMillis = notifyDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (triggerMillis <= System.currentTimeMillis()) return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("subscription_id", subscription.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            subscription.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            pendingIntent
        )
    }


    fun cancelReminder(context: Context, subscriptionId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            subscriptionId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        val notificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        val alarmPermissionGranted = Build.VERSION.SDK_INT < 31 ||
                context.checkSelfPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
        val postNotificationGranted = Build.VERSION.SDK_INT < 33 ||
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED

        return notificationsEnabled && alarmPermissionGranted && postNotificationGranted
    }

    private fun getNotificationTime(context: Context): Pair<Int, Int> {
        val prefs = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        val hour = prefs.getInt("notification_hour", 9)
        val minute = prefs.getInt("notification_minute", 0)
        return hour to minute
    }

    fun rescheduleAll(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val interactor: SubscriptionInteractor by inject(SubscriptionInteractor::class.java)
            val subscriptions = interactor.getAllSubscriptions().first()
            subscriptions.forEach { cancelReminder(context, it.id) }
            subscriptions.forEach { scheduleReminderForSubscription(context, it) }
        }
    }

    fun cancelAll(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val interactor: SubscriptionInteractor by inject(SubscriptionInteractor::class.java)
            val subscriptions = interactor.getAllSubscriptions().first()
            subscriptions.forEach { cancelReminder(context, it.id) }
        }
    }

}
