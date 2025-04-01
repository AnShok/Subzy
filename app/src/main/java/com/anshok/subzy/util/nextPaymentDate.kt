package com.anshok.subzy.util

import com.anshok.subzy.domain.model.PaymentPeriodType
import java.time.LocalDate

fun calculateNextPaymentDate(
    from: LocalDate,
    period: Int,
    type: PaymentPeriodType
): LocalDate {
    return when (type) {
        PaymentPeriodType.DAILY -> from.plusDays(period.toLong())
        PaymentPeriodType.WEEKLY -> from.plusWeeks(period.toLong())
        PaymentPeriodType.MONTHLY -> from.plusMonths(period.toLong())
        PaymentPeriodType.YEARLY -> from.plusYears(period.toLong())
    }
}

