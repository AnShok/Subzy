package com.anshok.subzy.domain.impl

import com.anshok.subzy.data.remote.models.Currency
import com.anshok.subzy.domain.api.CurrencyInteractor
import com.anshok.subzy.domain.api.CurrencyRepository

class CurrencyInteractorImpl(
    private val repository: CurrencyRepository
) : CurrencyInteractor {
    override suspend fun loadCurrencies(): List<Currency> {
        return repository.fetchCurrencies()
    }

    override fun getCurrencies(): List<Currency> {
        return repository.getCachedCurrencies()
    }
}
