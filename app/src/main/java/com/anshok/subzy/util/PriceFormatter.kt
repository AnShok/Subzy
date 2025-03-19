package com.anshok.subzy.util

import java.text.NumberFormat
import java.util.Locale

object PriceFormatter {

    private val currencySymbols = mapOf(
        "USD" to "$",
        "EUR" to "€",
        "RUB" to "₽",
        "MDL" to "L",
        "UAH" to "₴",
        "KZT" to "₸"
    )

    fun formatPrice(price: Double?, currencyCode: String?): String {
        if (price == null || currencyCode.isNullOrEmpty()) {
            return "Цена не указана"
        }

        val symbol = currencySymbols[currencyCode] ?: currencyCode
        val formatter = NumberFormat.getNumberInstance(Locale("ru"))
        formatter.maximumFractionDigits = if (price % 1 == 0.0) 0 else 2

        return "${formatter.format(price)} $symbol"
    }
}
