package com.anshok.subzy.domain.api

import com.anshok.subzy.data.remote.models.Currency

interface CurrencyRepository {
    suspend fun fetchCurrencies(): List<Currency>
    fun getCachedCurrencies(): List<Currency>
}
