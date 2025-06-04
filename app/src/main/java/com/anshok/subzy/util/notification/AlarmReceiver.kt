package com.anshok.subzy.util.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anshok.subzy.domain.reminder.model.ReminderType
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.util.extension.getNextPaymentLocalDatesUntil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val interactor: SubscriptionInteractor by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val subId = intent.getLongExtra("subscription_id", -1)
        if (subId == -1L) return

        CoroutineScope(Dispatchers.IO).launch {
            val subscription = interactor.getSubscriptionById(subId) ?: return@launch
            if (subscription.reminderType == ReminderType.NONE) return@launch

            // Показываем уведомление
            NotificationUtils.showReminderNotification(context, subscription)

            // Вычисляем дату следующего платежа
            val zone = ZoneId.systemDefault()
            val today = LocalDate.now()
            val currentNextDate = Instant.ofEpochMilli(subscription.nextPaymentDate)
                .atZone(zone).toLocalDate()

            val shouldUpdate = today.isAfter(currentNextDate) || today.isEqual(currentNextDate)
            if (shouldUpdate) {
                val nextDates = subscription.getNextPaymentLocalDatesUntil(currentNextDate.plusYears(1))

                val newNextDate = nextDates.firstOrNull { it.isAfter(currentNextDate) }
                    ?: currentNextDate // если не нашли — оставим текущую

                val nextInstant = LocalDateTime.of(newNextDate, java.time.LocalTime.MIDNIGHT)
                    .atZone(zone)
                    .toInstant()

                val updated = subscription.copy(
                    nextPaymentDate = nextInstant.toEpochMilli()
                )

                interactor.updateSubscription(updated)
                ReminderManager.scheduleReminderForSubscription(context, updated)
            }
        }
    }
}
