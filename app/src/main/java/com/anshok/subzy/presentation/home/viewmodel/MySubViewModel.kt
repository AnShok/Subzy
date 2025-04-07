package com.anshok.subzy.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.data.converters.CurrencyMapper
import com.anshok.subzy.data.local.UserPreferences
import com.anshok.subzy.domain.api.CurrencyInteractor
import com.anshok.subzy.domain.api.SubscriptionInteractor
import com.anshok.subzy.domain.model.CurrencyRate
import com.anshok.subzy.domain.model.Subscription
import com.anshok.subzy.shared.events.CurrencyChangedNotifier
import com.anshok.subzy.util.CurrencyUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class MySubViewModel(
    private val subscriptionInteractor: SubscriptionInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val userPreferences: UserPreferences,
    notifier: CurrencyChangedNotifier
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptions: StateFlow<List<Subscription>> = _subscriptions.asStateFlow()

    private val _upcomingBills = MutableStateFlow<List<Subscription>>(emptyList())
    val upcomingBills: StateFlow<List<Subscription>> = _upcomingBills.asStateFlow()

    private val _metrics =
        MutableStateFlow<Triple<String, Pair<Subscription, Double>?, Pair<Subscription, Double>?>>(
            Triple("--", null, null)
        )
    val metrics: StateFlow<Triple<String, Pair<Subscription, Double>?, Pair<Subscription, Double>?>> =
        _metrics.asStateFlow()

    init {
        viewModelScope.launch {
            val cached = currencyInteractor.getCurrencies()
            if (cached.isEmpty()) {
                try {
                    currencyInteractor.loadCurrencies()
                } catch (e: Exception) {
                    //  тост для ручного обновления?
                }
            }
            fetchSubscriptions()
        }

        viewModelScope.launch {
            notifier.flow.collect {
                calculateMetrics(_subscriptions.value)
            }
        }
    }


    fun getDefaultCurrencyCode(): String {
        return userPreferences.getDefaultCurrency()
    }

    private fun fetchSubscriptions() {
        viewModelScope.launch {
            subscriptionInteractor.getAllSubscriptions().collect { list ->
                _subscriptions.value = list.sortedByDescending { it.id }

                val now = LocalDate.now()
                _upcomingBills.value = list.filter { subscription ->
                    val next = Instant.ofEpochMilli(subscription.nextPaymentDate)
                        .atZone(ZoneId.systemDefault()).toLocalDate()
                    next.isAfter(now) || next.isEqual(now)
                }.sortedBy { it.nextPaymentDate }

                calculateMetrics(list)
            }
        }
    }

    private fun calculateMetrics(list: List<Subscription>) {
        val rawCurrencies = currencyInteractor.getCurrencies()
        val currencies: List<CurrencyRate> = CurrencyMapper.mapList(rawCurrencies)

        val defaultCode = userPreferences.getDefaultCurrency()
        val defaultCurrency = currencies.find { it.code == defaultCode } ?: return

        val converted = list.mapNotNull { sub ->
            val fromCurrency =
                currencies.find { it.code == sub.currencyCode } ?: return@mapNotNull null
            val convertedPrice = CurrencyUtils.convert(sub.price, fromCurrency, defaultCurrency)
            Pair(sub, convertedPrice)
        }

        val totalAmount = converted.sumOf { it.second }
        val mostExpensive = converted.maxByOrNull { it.second }
        val cheapest = converted.minByOrNull { it.second }

        val formattedTotal = CurrencyUtils.formatPrice(totalAmount, defaultCode)

        _metrics.value = Triple(
            formattedTotal,
            mostExpensive,
            cheapest
        )
    }
}
