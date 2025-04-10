package com.anshok.subzy.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.data.converters.CurrencyMapper
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.domain.currency.CurrencyInteractor
import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.model.MetricsDisplayPeriod
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.presentation.home.adapter.MySubAdapter
import com.anshok.subzy.presentation.home.bottomsheet.SortDirection
import com.anshok.subzy.presentation.home.bottomsheet.SortOption
import com.anshok.subzy.shared.events.CurrencyChangedNotifier
import com.anshok.subzy.util.CurrencyUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MySubViewModel(
    private val subscriptionInteractor: SubscriptionInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val userPreferences: UserPreferences,
    notifier: CurrencyChangedNotifier
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptions: StateFlow<List<Subscription>> = _subscriptions.asStateFlow()

    private val _metrics = MutableStateFlow(
        MetricsUiModel("--", null, null, MetricsDisplayPeriod.MONTHLY)
    )
    val metrics: StateFlow<MetricsUiModel> = _metrics.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.DATE)
    val sortOption: StateFlow<SortOption> = _sortOption

    private val _sortDirection = MutableStateFlow(SortDirection.DESC)
    val sortDirection: StateFlow<SortDirection> = _sortDirection

    val combinedSortState = combine(sortOption, sortDirection) { _, _ -> Unit }

    var shouldAnimateNextMetrics: Boolean = true
        private set

    init {
        viewModelScope.launch {
            if (currencyInteractor.getCurrencies().isEmpty()) {
                try {
                    currencyInteractor.loadCurrencies()
                } catch (_: Exception) {
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

    fun switchMetricsPeriod() {
        val next = _metrics.value.period.next()
        shouldAnimateNextMetrics = true
        calculateMetrics(_subscriptions.value, next)
    }

    fun disableMetricsAnimation() {
        shouldAnimateNextMetrics = false
    }


    fun setSort(option: SortOption, direction: SortDirection) {
        _sortOption.value = option
        _sortDirection.value = direction
        applySorting()
    }

    fun reapplySorting(adapter: MySubAdapter, onDone: (() -> Unit)? = null) {
        val sorted = sortSubscriptions(_subscriptions.value)
        adapter.submitList(sorted) { onDone?.invoke() }
    }

    private fun fetchSubscriptions() {
        viewModelScope.launch {
            subscriptionInteractor.getAllSubscriptions().collect { list ->
                val sorted = sortSubscriptions(list)
                _subscriptions.value = sorted
                calculateMetrics(list)
            }
        }
    }

    private fun applySorting() {
        _subscriptions.value = sortSubscriptions(_subscriptions.value)
    }

    private fun sortSubscriptions(list: List<Subscription>): List<Subscription> {
        val currencies = CurrencyMapper.mapList(currencyInteractor.getCurrencies())
        val defaultCode = userPreferences.getDefaultCurrency()
        val defaultCurrency = currencies.find { it.code == defaultCode }

        val sorted = when (_sortOption.value) {
            SortOption.DATE -> list.sortedBy { it.id }
            SortOption.NAME -> list.sortedBy { it.name.lowercase() }
            SortOption.PRICE -> {
                if (defaultCurrency == null) {
                    list.sortedBy { it.price }
                } else {
                    list.sortedBy { sub ->
                        val from = currencies.find { rate -> rate.code == sub.currencyCode }
                        from?.let { CurrencyUtils.convert(sub.price, from, defaultCurrency) }
                            ?: sub.price
                    }
                }
            }
        }

        return if (_sortDirection.value == SortDirection.ASC) sorted else sorted.reversed()
    }


    private fun calculateMetrics(
        list: List<Subscription>,
        period: MetricsDisplayPeriod = _metrics.value.period
    ) {
        val rawCurrencies = currencyInteractor.getCurrencies()
        val currencies = CurrencyMapper.mapList(rawCurrencies)
        val defaultCode = userPreferences.getDefaultCurrency()
        val defaultCurrency = currencies.find { it.code == defaultCode } ?: return

        val converted = list.mapNotNull { sub ->
            val fromCurrency =
                currencies.find { it.code == sub.currencyCode } ?: return@mapNotNull null
            val base = CurrencyUtils.convert(sub.price, fromCurrency, defaultCurrency)
            val adjusted = when (period) {
                MetricsDisplayPeriod.MONTHLY -> when (sub.paymentPeriodType) {
                    PaymentPeriodType.YEARLY -> base / 12
                    PaymentPeriodType.WEEKLY -> base * 4.345
                    PaymentPeriodType.DAILY -> base * 30
                    else -> base
                }

                MetricsDisplayPeriod.YEARLY -> when (sub.paymentPeriodType) {
                    PaymentPeriodType.MONTHLY -> base * 12
                    PaymentPeriodType.WEEKLY -> base * 52
                    PaymentPeriodType.DAILY -> base * 365
                    else -> base
                }

                MetricsDisplayPeriod.WEEKLY -> when (sub.paymentPeriodType) {
                    PaymentPeriodType.MONTHLY -> base / 4.345
                    PaymentPeriodType.YEARLY -> base / 52
                    PaymentPeriodType.DAILY -> base * 7
                    else -> base
                }

                MetricsDisplayPeriod.DAILY -> when (sub.paymentPeriodType) {
                    PaymentPeriodType.MONTHLY -> base / 30
                    PaymentPeriodType.YEARLY -> base / 365
                    PaymentPeriodType.WEEKLY -> base / 7
                    else -> base
                }
            }
            sub to adjusted
        }

        val total = converted.sumOf { it.second }
        _metrics.value = MetricsUiModel(
            totalFormatted = CurrencyUtils.formatPrice(total, defaultCode),
            highest = converted.maxByOrNull { it.second },
            lowest = converted.minByOrNull { it.second },
            period = period
        )
    }

    data class MetricsUiModel(
        val totalFormatted: String,
        val highest: Pair<Subscription, Double>?,
        val lowest: Pair<Subscription, Double>?,
        val period: MetricsDisplayPeriod
    )
}
