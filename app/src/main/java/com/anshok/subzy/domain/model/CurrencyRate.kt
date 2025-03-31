package com.anshok.subzy.domain.model

data class CurrencyRate(
    val code: String,
    val name: String,
    val nominal: Int,
    val value: Double
)