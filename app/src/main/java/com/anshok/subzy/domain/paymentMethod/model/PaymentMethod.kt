package com.anshok.subzy.domain.paymentMethod.model

data class PaymentMethod(
    val id: Long,
    val name: String,
    val cardNumber: String,
    val expirationDate: Long,
    val isDefault: Boolean
)
