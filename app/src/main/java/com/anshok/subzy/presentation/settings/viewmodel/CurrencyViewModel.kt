package com.anshok.subzy.presentation.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.domain.currency.CurrencyInteractor
import com.anshok.subzy.domain.currency.model.CurrencyRate
import com.anshok.subzy.shared.events.CurrencyChangedNotifier
import kotlinx.coroutines.launch

class CurrencyViewModel(
    private val currencyInteractor: CurrencyInteractor,
    private val preferences: UserPreferences,
    private val notifier: CurrencyChangedNotifier
) : ViewModel() {

    private val _currencies = MutableLiveData<List<CurrencyRate>>()
    val currencies: LiveData<List<CurrencyRate>> = _currencies

    private val _selectedCurrencyCode = MutableLiveData<String>()
    val selectedCurrencyCode: LiveData<String> = _selectedCurrencyCode

    init {
        _selectedCurrencyCode.value = preferences.getDefaultCurrency()
        loadRates()
    }

    private fun loadRates() {
        viewModelScope.launch {
            _currencies.value = currencyInteractor.loadCurrencies().map {
                CurrencyRate(
                    code = it.charCode,
                    name = it.name,
                    nominal = it.nominal,
                    value = it.value
                )
            }
        }
    }

    fun setCurrencyCode(code: String) {
        preferences.saveDefaultCurrency(code)
        _selectedCurrencyCode.value = code

        viewModelScope.launch {
            notifier.notify(code)
        }
    }
}
