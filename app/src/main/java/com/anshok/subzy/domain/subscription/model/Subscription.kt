package com.anshok.subzy.domain.subscription.model

import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class Subscription(
    val id: Long,
    val name: String,
    val logoUrl: String?,
    val price: Double,
    val currencyCode: String,
    val description: String?,
    val paymentPeriod: Int, // 1-100
    val paymentPeriodType: PaymentPeriodType, // тип
    val firstPaymentDate: Long,
    val nextPaymentDate: Long,
    val paymentMethodId: Long,
    val categoryId: Long,
    val comment: String?
) {
    fun getNextPaymentDatesUntil(endDate: LocalDate): List<Long> {
        val result = mutableListOf<LocalDate>()

        // Начинаем с текущей "следующей даты оплаты"
        var currentDate = Instant.ofEpochMilli(nextPaymentDate)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val periodValue = paymentPeriod.coerceAtLeast(1) // защита от 0
        val maxIterations = 1000 // защита от бесконечных циклов
        var counter = 0

        while (!currentDate.isAfter(endDate) && counter++ < maxIterations) {
            result.add(currentDate)

            currentDate = when (paymentPeriodType) {
                PaymentPeriodType.DAILY -> currentDate.plusDays(periodValue.toLong())

                PaymentPeriodType.WEEKLY -> currentDate.plusWeeks(periodValue.toLong())

                PaymentPeriodType.MONTHLY -> {
                    val next = currentDate.plusMonths(periodValue.toLong())
                    // Сохраняем "день" максимально возможный для месяца
                    next.withDayOfMonth(minOf(currentDate.dayOfMonth, next.lengthOfMonth()))
                }

                PaymentPeriodType.YEARLY -> {
                    val next = currentDate.plusYears(periodValue.toLong())
                    next.withDayOfMonth(minOf(currentDate.dayOfMonth, next.lengthOfMonth()))
                }
            }
        }

        return result.map { it.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() }
    }
}
