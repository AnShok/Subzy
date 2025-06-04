package com.anshok.subzy.util.extension

import com.anshok.subzy.domain.reminder.model.ReminderType

fun String.toReminderType(): ReminderType = when (this) {
    "за 1 день" -> ReminderType.ONE_DAY
    "за 3 дня" -> ReminderType.THREE_DAYS
    "за 1 неделю" -> ReminderType.SEVEN_DAYS
    else -> ReminderType.NONE
}

fun ReminderType.toLabel(): String = when (this) {
    ReminderType.ONE_DAY -> "за 1 день"
    ReminderType.THREE_DAYS -> "за 3 дня"
    ReminderType.SEVEN_DAYS -> "за 1 неделю"
    ReminderType.NONE -> "без уведомлений"
}
