package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.data.remote.currency.network.CbrApiService
import com.anshok.subzy.data.remote.currency.network.CurrencyParser
import com.anshok.subzy.data.remote.models.Currency
import com.anshok.subzy.domain.currency.CurrencyRepository
import com.google.gson.Gson

class CurrencyRepositoryImpl(
    private val api: CbrApiService,
    private val prefs: UserPreferences,
    private val gson: Gson
) : CurrencyRepository {

    private var cache: List<Currency> = emptyList()

    override suspend fun fetchCurrencies(): List<Currency> {
        if (prefs.isCurrencyCacheToday()) {
            prefs.getCurrencyCache()?.let { json ->
                cache = gson.fromJson(json, Array<Currency>::class.java).toList()
                return cache
            }
        }

        val response = api.getRates()
        if (response.isSuccessful) {
            val xml = response.body()?.string().orEmpty()
            val parsed = CurrencyParser.parse(xml)
            cache = parsed
            prefs.saveCurrencyCache(gson.toJson(parsed))
            return parsed
        } else throw Exception("Currency API error: ${response.code()}")
    }

    override fun getCachedCurrencies(): List<Currency> = cache
}
