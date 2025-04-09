package com.anshok.subzy.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.data.converters.CurrencyMapper
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.domain.currency.CurrencyInteractor
import com.anshok.subzy.domain.currency.model.CurrencyRate
import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.model.MetricsDisplayPeriod
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.shared.events.CurrencyChangedNotifier
import com.anshok.subzy.util.CurrencyUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MySubViewModel(
    private val subscriptionInteractor: SubscriptionInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val userPreferences: UserPreferences,
    notifier: CurrencyChangedNotifier
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptions: StateFlow<List<Subscription>> = _subscriptions.asStateFlow()

    data class MetricsUiModel(
        val totalFormatted: String,
        val highest: Pair<Subscription, Double>?,
        val lowest: Pair<Subscription, Double>?,
        val period: MetricsDisplayPeriod
    )

    private val _metrics = MutableStateFlow(
        MetricsUiModel("--", null, null, MetricsDisplayPeriod.MONTHLY)
    )
    val metrics: StateFlow<MetricsUiModel> = _metrics.asStateFlow()

    var shouldAnimateNextMetrics: Boolean = true
        private set

    fun triggerMetricsAnimationOnce() {
        shouldAnimateNextMetrics = true
    }

    fun disableMetricsAnimation() {
        shouldAnimateNextMetrics = false
    }

    val isManualRefreshPending = MutableStateFlow(false)

    fun switchMetricsPeriod() {
        val next = _metrics.value.period.next()
        shouldAnimateNextMetrics = true
        calculateMetrics(_subscriptions.value, next)
    }

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
                calculateMetrics(list)
            }
        }
    }

    private fun calculateMetrics(
        list: List<Subscription>,
        period: MetricsDisplayPeriod = _metrics.value.period
    ) {
        val rawCurrencies = currencyInteractor.getCurrencies()
        val currencies: List<CurrencyRate> = CurrencyMapper.mapList(rawCurrencies)
        val defaultCode = userPreferences.getDefaultCurrency()
        val defaultCurrency = currencies.find { it.code == defaultCode } ?: return

        val converted = list.mapNotNull { sub ->
            val fromCurrency =
                currencies.find { it.code == sub.currencyCode } ?: return@mapNotNull null
            val convertedPrice = CurrencyUtils.convert(sub.price, fromCurrency, defaultCurrency)
            val adjustedPrice = when (period) {
                MetricsDisplayPeriod.MONTHLY -> when (sub.paymentPeriodType) {
                    PaymentPeriodType.YEARLY -> convertedPrice / 12
                    PaymentPeriodType.WEEKLY -> convertedPrice * 4.345 // approx weeks/month
                    PaymentPeriodType.DAILY -> convertedPrice * 30
                    else -> convertedPrice
                }

                MetricsDisplayPeriod.YEARLY -> when (sub.paymentPeriodType) {
                    PaymentPeriodType.MONTHLY -> convertedPrice * 12
                    PaymentPeriodType.WEEKLY -> convertedPrice * 52
                    PaymentPeriodType.DAILY -> convertedPrice * 365
                    else -> convertedPrice
                }

                MetricsDisplayPeriod.WEEKLY -> when (sub.paymentPeriodType) {
                    PaymentPeriodType.MONTHLY -> convertedPrice / 4.345
                    PaymentPeriodType.YEARLY -> convertedPrice / 52
                    PaymentPeriodType.DAILY -> convertedPrice * 7
                    else -> convertedPrice
                }

                MetricsDisplayPeriod.DAILY -> when (sub.paymentPeriodType) {
                    PaymentPeriodType.MONTHLY -> convertedPrice / 30
                    PaymentPeriodType.YEARLY -> convertedPrice / 365
                    PaymentPeriodType.WEEKLY -> convertedPrice / 7
                    else -> convertedPrice
                }
            }
            sub to adjustedPrice
        }

        val total = converted.sumOf { it.second }
        val formatted = CurrencyUtils.formatPrice(total, defaultCode)

        val mostExpensive = converted.maxByOrNull { it.second }
        val cheapest = converted.minByOrNull { it.second }

        _metrics.value = MetricsUiModel(
            totalFormatted = formatted,
            highest = mostExpensive,
            lowest = cheapest,
            period = period
        )
    }


    fun refreshSubscriptionsManually(callback: (List<Subscription>) -> Unit) {
        viewModelScope.launch {
            isManualRefreshPending.value = true
            subscriptionInteractor.getAllSubscriptions().collect { list ->
                val sorted = list.sortedByDescending { it.id }
                triggerMetricsAnimationOnce()
                _subscriptions.value = sorted
                calculateMetrics(sorted)
                isManualRefreshPending.value = false
                callback(sorted)
            }
        }
    }
}
