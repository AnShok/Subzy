package com.anshok.subzy.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.domain.subscription.model.SubscriptionGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

/**
 * ViewModel для [CalendarFragment], отвечающая за предоставление данных подписок,
 * отображаемых на календаре и в списке под ним.
 *
 * Основные задачи:
 * - Загрузка всех подписок и генерация будущих дат платежей.
 * - Фильтрация подписок по выбранной дате или текущему месяцу.
 * - Группировка подписок по дате для отображения через Groupie.
 *
 * Использует [SubscriptionInteractor] для получения данных.
 * Все будущие даты подписок кэшируются в [_upcomingBills] и доступны через [upcomingBills].
 */
class CalendarViewModel(
    private val subscriptionInteractor: SubscriptionInteractor,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _upcomingBills = MutableStateFlow<List<Subscription>>(emptyList())
    val upcomingBills: StateFlow<List<Subscription>> = _upcomingBills.asStateFlow()

//    init {
//        fetchUpcomingBills()
//    }

//    private fun fetchUpcomingBills() {
    fun fetchUpcomingBills() {
        viewModelScope.launch {
            subscriptionInteractor.getAllSubscriptions().collect { list ->
                val now = LocalDate.now()
                val endDate = now.plusYears(2)
                val upcoming = mutableListOf<Subscription>()

                for (sub in list) {
                    try {
                        // Добаление firstPaymentDate, если он в будущем
                        val firstDate = Instant.ofEpochMilli(sub.firstPaymentDate)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                        if (!firstDate.isBefore(now)) {
                            upcoming.add(sub.copy(nextPaymentDate = sub.firstPaymentDate))
                        }

                        // Добавление всех будущих платежей
                        val paymentDates = sub.getNextPaymentDatesUntil(endDate)
                        paymentDates.forEach { nextDate ->
                            val localDate = Instant.ofEpochMilli(nextDate)
                                .atZone(ZoneId.systemDefault()).toLocalDate()
                            if (!localDate.isBefore(now)) {
                                upcoming.add(sub.copy(nextPaymentDate = nextDate))
                            }
                        }
                    } catch (_: Exception) {
                    }
                }

                _upcomingBills.value = upcoming.sortedBy { it.nextPaymentDate }
            }
        }
    }

    fun getGroupedSubscriptionsForMonth(month: YearMonth): List<SubscriptionGroup> {
        val start = month.atDay(1)
        val end = month.atEndOfMonth()

        return _upcomingBills.value
            .flatMap { sub ->
                val first =
                    Instant.ofEpochMilli(sub.firstPaymentDate).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                val next = Instant.ofEpochMilli(sub.nextPaymentDate).atZone(ZoneId.systemDefault())
                    .toLocalDate()
                val dates = listOf(first, next).distinct()

                dates.filter { it in start..end }.map { date -> date to sub }
            }
            .groupBy({ it.first }, { it.second })
            .map { (date, subs) -> SubscriptionGroup(date, subs.distinctBy { it.id }) }
            .sortedBy { it.date }
    }

    fun hasSubscriptionsOn(date: LocalDate): Boolean {
        return _upcomingBills.value.any {
            val paymentDate = Instant.ofEpochMilli(it.nextPaymentDate)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            val firstDate = Instant.ofEpochMilli(it.firstPaymentDate)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            date == paymentDate || date == firstDate
        }
    }

    fun getSubscriptionsForDate(date: LocalDate): List<Subscription> {
        return _upcomingBills.value.filter {
            val first = Instant.ofEpochMilli(it.firstPaymentDate)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            val next = Instant.ofEpochMilli(it.nextPaymentDate)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            date == first || date == next
        }.distinctBy { it.id }
    }

}
