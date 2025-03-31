package com.anshok.subzy.data.converters

import com.anshok.subzy.data.remote.models.Currency
import com.anshok.subzy.domain.model.CurrencyRate

object CurrencyMapper {

    fun mapToRate(currency: Currency): CurrencyRate {
        return CurrencyRate(
            code = currency.charCode,
            name = currency.name,
            nominal = currency.nominal,
            value = currency.value
        )
    }

    fun mapList(currencies: List<Currency>): List<CurrencyRate> {
        return currencies.map { mapToRate(it) }
    }
}
