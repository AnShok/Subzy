package com.anshok.subzy.domain.currency

import com.anshok.subzy.data.remote.models.Currency

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
