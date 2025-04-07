package com.anshok.subzy.domain.currency.model

data class CurrencyRate(
    val code: String,
    val name: String,
    val nominal: Int,
    val value: Double
)