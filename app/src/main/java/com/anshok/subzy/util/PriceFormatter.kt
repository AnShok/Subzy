package com.anshok.subzy.util

import java.text.NumberFormat
import java.util.Locale

object PriceFormatter {

    private val currencySymbols = mapOf(

        "EUR" to "€",
        "USD" to "$",
        "KZT" to "₸",
        "UZS" to "сўм",
        "CNY" to "¥",
        "AUD" to "$",
        "AZN" to "₼",
        "AMD" to "֏",
        "BYN" to "Br",
        "BGN" to "лв",
        "BRL" to "R$",
        "HUF" to "Ft",
        "VND" to "₫",
        "HKD" to "HK$",
        "GEL" to "₾",
        "DKK" to "kr",
        "AED" to "د.إ",
        "EGP" to "£",
        "INR" to "₹",
        "IDR" to "Rp",
        "CAD" to "C$",
        "QAR" to "ر.ق",
        "KGS" to "с",
        "KRW" to "₩",
        "XDR" to "SDR",
        "MDL" to "L",
        "NZD" to "NZ$",
        "RON" to "lei",
        "TMT" to "m",
        "NOK" to "kr",
        "PLN" to "zł",
        "RSD" to "din.",
        "SGD" to "S$",
        "TJS" to "ЅМ",
        "THB" to "฿",
        "TRY" to "₺",
        "UAH" to "₴",
        "GBP" to "£",
        "CZK" to "Kč",
        "SEK" to "kr",
        "CHF" to "CHF",
        "ZAR" to "R",
        "JPY" to "¥",

        )

    fun formatPrice(price: Double?, currencyCode: String?): String {
        if (price == null || currencyCode.isNullOrEmpty()) {
            return "--"
        }

        val symbol = currencySymbols[currencyCode] ?: currencyCode
        val formatter = NumberFormat.getNumberInstance(Locale("ru"))
        formatter.maximumFractionDigits = if (price % 1 == 0.0) 0 else 2

        return "${formatter.format(price)} $symbol"
    }

    fun getSymbol(currencyCode: String): String {
        return currencySymbols[currencyCode] ?: currencyCode
    }
}
