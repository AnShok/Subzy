package com.anshok.subzy.util.extension

import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
import com.anshok.subzy.domain.subscription.model.Subscription
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Subscription.getNextPaymentLocalDatesUntil(untilDate: LocalDate): List<LocalDate> {
    val result = mutableListOf<LocalDate>()
    var date = Instant.ofEpochMilli(firstPaymentDate)
        .atZone(ZoneId.systemDefault()).toLocalDate()

    val step = paymentPeriod.coerceAtLeast(1)

    while (date.isBefore(untilDate)) {
        if (date.isAfter(LocalDate.now())) {
            result.add(date)
        }

        date = when (paymentPeriodType) {
            PaymentPeriodType.DAILY -> date.plusDays(step.toLong())
            PaymentPeriodType.WEEKLY -> date.plusWeeks(step.toLong())
            PaymentPeriodType.MONTHLY -> {
                val next = date.plusMonths(step.toLong())
                next.withDayOfMonth(minOf(date.dayOfMonth, next.lengthOfMonth()))
            }
            PaymentPeriodType.YEARLY -> {
                val next = date.plusYears(step.toLong())
                next.withDayOfMonth(minOf(date.dayOfMonth, next.lengthOfMonth()))
            }
        }
    }

    return result
}
