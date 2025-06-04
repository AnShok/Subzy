package com.anshok.subzy.util.notification

import java.time.*
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun calculateDelayMillis(
        now: LocalDateTime = LocalDateTime.now(),
        paymentDate: LocalDate,
        reminderType: com.anshok.subzy.domain.reminder.model.ReminderType,
        notifyHour: Int,
        notifyMinute: Int
    ): Long? {
        val reminderDate = when (reminderType) {
            com.anshok.subzy.domain.reminder.model.ReminderType.ONE_DAY -> paymentDate.minusDays(1)
            com.anshok.subzy.domain.reminder.model.ReminderType.THREE_DAYS -> paymentDate.minusDays(3)
            com.anshok.subzy.domain.reminder.model.ReminderType.SEVEN_DAYS -> paymentDate.minusDays(7)
            else -> return null
        }

        val targetTime = reminderDate.atTime(notifyHour, notifyMinute)
        val delay = Duration.between(now, targetTime).toMillis()

        return if (delay > 0) delay else null
    }

    fun millisToTimeUnit(millis: Long): Pair<Long, TimeUnit> {
        return millis to TimeUnit.MILLISECONDS
    }
}
