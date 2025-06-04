package com.anshok.subzy.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.anshok.subzy.R
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.domain.reminder.model.ReminderType
import java.util.*

object NotificationUtils {

    private const val CHANNEL_ID = "subscription_reminders"
    private const val GROUP_KEY_SUBSCRIPTIONS = "group_subscriptions"
    private const val GROUP_NOTIFICATION_ID = 1000

    fun showReminderNotification(context: Context, subscription: Subscription) {
        if (!PermissionRequestHelper.hasAllRequiredPermissions(context)) return

        createNotificationChannel(context)

        val manager = NotificationManagerCompat.from(context)

        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.app_logo_transparent)

        val reminderText = when (subscription.reminderType) {
            ReminderType.ONE_DAY -> "Завтра оплата подписки"
            ReminderType.THREE_DAYS -> "Через 3 дня оплата подписки"
            ReminderType.SEVEN_DAYS -> "Через неделю оплата подписки"
            ReminderType.NONE -> return
        }

        val content = "$reminderText: ${subscription.name} — ${subscription.price} ${subscription.currencyCode}"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo_transparent)
            .setLargeIcon(largeIcon)
            .setContentTitle("Напоминание о подписке")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(GROUP_KEY_SUBSCRIPTIONS)
            .setAutoCancel(true)
            .build()

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            manager.notify(subscription.id.toInt(), notification)
            showGroupNotification(context)
        }
    }


    private fun showGroupNotification(context: Context) {
        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo_transparent)
            .setContentTitle("Скоро оплата по подпискам")
            .setContentText("Ознакомьтесь с предстоящими оплатами в приложении")
            .setGroup(GROUP_KEY_SUBSCRIPTIONS)
            .setGroupSummary(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(GROUP_NOTIFICATION_ID, summaryNotification)
        }

    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Напоминания по подпискам"
            val description = "Уведомления о предстоящих оплатах"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
