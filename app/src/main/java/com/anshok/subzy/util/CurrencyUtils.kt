package com.anshok.subzy.util

import android.animation.ValueAnimator
import android.widget.TextView
import com.anshok.subzy.domain.currency.model.CurrencyRate
import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    private val currencySymbols = mapOf(

        "EUR" to "€",
        "USD" to "$",
        "RUB" to "₽",
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

    fun getSymbol(code: String): String {
        return currencySymbols[code.uppercase()] ?: code
    }

    fun formatPrice(price: Double?, currencyCode: String?): String {
        if (price == null || currencyCode.isNullOrBlank()) return "--"
        val formatter = NumberFormat.getNumberInstance(Locale("ru"))
        formatter.maximumFractionDigits = if (price % 1 == 0.0) 0 else 2
        return "${formatter.format(price)} ${getSymbol(currencyCode)}"
    }

    fun convert(amount: Double, from: CurrencyRate, to: CurrencyRate): Double {
        val rubAmount = amount * from.value / from.nominal
        return rubAmount * to.nominal / to.value
    }

    fun TextView.animateCurrencyChange(
        start: Double,
        end: Double,
        currencyCode: String,
        duration: Long = 500L
    ) {
        val animator = ValueAnimator.ofFloat(start.toFloat(), end.toFloat())
        animator.duration = duration
        animator.addUpdateListener {
            val value = (it.animatedValue as Float).toDouble()
            text = CurrencyUtils.formatPrice(value, currencyCode)
        }
        animator.start()
    }

}

