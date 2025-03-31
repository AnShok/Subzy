package com.anshok.subzy.domain.api

import com.anshok.subzy.data.remote.models.Currency

interface CurrencyInteractor {
    suspend fun loadCurrencies(): List<Currency>
    fun getCurrencies(): List<Currency>
}
