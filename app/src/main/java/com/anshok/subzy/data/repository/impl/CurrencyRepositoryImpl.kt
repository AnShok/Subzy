package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.remote.currency.search.CbrApiService
import com.anshok.subzy.data.remote.currency.search.CurrencyParser
import com.anshok.subzy.data.remote.models.Currency
import com.anshok.subzy.domain.api.CurrencyRepository

class CurrencyRepositoryImpl(
    private val api: CbrApiService
) : CurrencyRepository {

    private var cache: List<Currency> = emptyList()

    override suspend fun fetchCurrencies(): List<Currency> {
        val response = api.getRates()
        if (response.isSuccessful) {
            val xml = response.body()?.string() ?: ""
            val parsed = CurrencyParser.parse(xml)
            cache = parsed
            return parsed
        } else throw Exception("Currency API error: ${response.code()}")
    }

    override fun getCachedCurrencies(): List<Currency> = cache
}
