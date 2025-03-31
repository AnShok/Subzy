package com.anshok.subzy.util

import com.anshok.subzy.domain.model.CurrencyRate

object CurrencyConverter {

    fun convert(
        amount: Double,
        from: CurrencyRate,
        to: CurrencyRate
    ): Double {
        // Переводим в рубли, потом в целевую валюту
        val rubAmount = amount * from.nominal / from.value
        return rubAmount * to.value / to.nominal
    }
}
