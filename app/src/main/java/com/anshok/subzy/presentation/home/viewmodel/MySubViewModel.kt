package com.anshok.subzy.presentation.home.viewmodel

import android.util.Log
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
import com.anshok.subzy.shared.events.SubscriptionChangedNotifier
import com.anshok.subzy.util.CurrencyUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MySubViewModel(
    private val subscriptionInteractor: SubscriptionInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val userPreferences: UserPreferences,
    notifier: CurrencyChangedNotifier,
    private val subscriptionChangedNotifier: SubscriptionChangedNotifier
) : ViewModel() {

    // Выбранная опция сортировки (по дате, имени, цене)
    private val _sortOption = MutableStateFlow(SortOption.DATE)
    val sortOption: StateFlow<SortOption> = _sortOption

    // Направление сортировки (по возрастанию / убыванию)
    private val _sortDirection = MutableStateFlow(SortDirection.DESC)
    val sortDirection: StateFlow<SortDirection> = _sortDirection

    // Объединённый флоу, чтобы реагировать на любую смену сортировки
    val combinedSortState = combine(sortOption, sortDirection) { _, _ -> Unit }

    // Текущий выбранный период для отображения метрик (месяц, год и т.д.)
    private val _metricsPeriod = MutableStateFlow(MetricsDisplayPeriod.MONTHLY)

    // Триггер для повторной загрузки подписок
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    // Валюта, выбранная пользователем в настройках
    private val _defaultCurrencyCode = MutableStateFlow(userPreferences.getDefaultCurrency())

    /**
     * Список подписок, отсортированный согласно текущим настройкам.
     * Загружается при запуске или при изменениях (валюта, подписки и т.п.).
     */
    val subscriptions: StateFlow<List<Subscription>> = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            subscriptionInteractor.getAllSubscriptions()
                .map { sortSubscriptions(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Метрики (сумма, максимум, минимум) рассчитываются при изменении:
     * - списка подписок
     * - периода метрик
     * - валюты по умолчанию
     */
    val metrics: StateFlow<MetricsUiModel> = combine(
        subscriptions,
        _metricsPeriod,
        _defaultCurrencyCode
    ) { list, period, currencyCode ->
        Log.d("COMBINE", "Triggered: ${list.size} subs, period=$period, currency=$currencyCode")
        calculateMetrics(list, period, currencyCode)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MetricsUiModel(
            totalFormatted = null,
            highestFormatted = null,
            lowestFormatted = null,
            period = MetricsDisplayPeriod.MONTHLY,
            highestSub = null,
            lowestSub = null
        )
    )


    init {
        // Загружаем валюты при запуске (если ещё не загружены)
        viewModelScope.launch {
            if (currencyInteractor.getCurrencies().isEmpty()) {
                try {
                    currencyInteractor.loadCurrencies()
                } catch (_: Exception) {
                }
            }
        }

        // Слушаем изменение валюты по умолчанию
        viewModelScope.launch {
            notifier.flow.collect { newCode ->
                _defaultCurrencyCode.value = newCode
                refreshTrigger.emit(Unit)
            }
        }

        // Слушаем любые изменения подписок
        viewModelScope.launch {
            subscriptionChangedNotifier.flow.collect {
                refreshTrigger.emit(Unit)
            }
        }
    }

    fun switchMetricsPeriod() {
        val next = _metricsPeriod.value.next()
        _metricsPeriod.value = next
    }

    /**
     * Установка сортировки
     */
    fun setSort(option: SortOption, direction: SortDirection) {
        _sortOption.value = option
        _sortDirection.value = direction
    }

    /**
     * Применение сортировки вручную (например, при смене параметров)
     */
    fun reapplySorting(adapter: MySubAdapter, onDone: (() -> Unit)? = null) {
        val sorted = sortSubscriptions(subscriptions.value)
        adapter.submitList(sorted) { onDone?.invoke() }
    }

    /**
     * Сортировка списка подписок согласно текущим настройкам и валюте
     */
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

    /**
     * Расчёт метрик (сумма, самая дорогая/дешёвая) с учётом:
     * - курса валют
     * - периода (ежемесячно/ежегодно и т.д.)
     */
    private fun calculateMetrics(
        list: List<Subscription>,
        period: MetricsDisplayPeriod,
        currencyCode: String
    ): MetricsUiModel {
        val rawCurrencies = currencyInteractor.getCurrencies()
        val currencies = CurrencyMapper.mapList(rawCurrencies)
        val defaultCurrency = currencies.find { it.code == currencyCode }
            ?: return MetricsUiModel("--", null, null, period, null, null)

        val converted = list.mapNotNull { sub ->
            val fromCurrency = currencies.find { it.code == sub.currencyCode } ?: return@mapNotNull null
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
        val highestPair = converted.maxByOrNull { it.second }
        val lowestPair = converted.minByOrNull { it.second }

        return MetricsUiModel(
            totalFormatted = CurrencyUtils.formatPrice(total, currencyCode),
            highestFormatted = highestPair?.let { CurrencyUtils.formatPrice(it.second, currencyCode) },
            lowestFormatted = lowestPair?.let { CurrencyUtils.formatPrice(it.second, currencyCode) },
            period = period,
            highestSub = highestPair?.first,
            lowestSub = lowestPair?.first
        )
    }

    /**
     * UI-модель метрик подписок
     */
    data class MetricsUiModel(
        val totalFormatted: String?,
        val highestFormatted: String?,
        val lowestFormatted: String?,
        val period: MetricsDisplayPeriod,
        val highestSub: Subscription?,
        val lowestSub: Subscription?
    )
}
