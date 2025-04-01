package com.anshok.subzy.domain.model

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
)
