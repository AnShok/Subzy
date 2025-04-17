package com.anshok.subzy.presentation.subDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.shared.events.SubscriptionChangedNotifier
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class DetailsSubViewModel(
    private val interactor: SubscriptionInteractor,
    private val subscriptionChangedNotifier: SubscriptionChangedNotifier
) : ViewModel() {

    private val _subscription = MutableLiveData<Subscription?>()
    val subscription: LiveData<Subscription?> = _subscription

    private var currentSubscription: Subscription? = null

    fun loadSubscriptionDetails(id: Long) {
        viewModelScope.launch {
            val subscription = interactor.getSubscriptionById(id)
            currentSubscription = subscription
            _subscription.postValue(subscription)
        }
    }

    fun deleteSubscription(onDeleted: () -> Unit) {
        viewModelScope.launch {
            currentSubscription?.let {
                interactor.deleteSubscription(it)
                subscriptionChangedNotifier.notify()
                onDeleted()
            }
        }
    }

    fun formatDate(millis: Long): String {
        val date = Date(millis)
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun getNextPaymentDateMillis(
        firstPaymentMillis: Long,
        period: Int,
        type: PaymentPeriodType
    ): Long {
        var current = Instant.ofEpochMilli(firstPaymentMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val today = LocalDate.now()
        val step = period.coerceAtLeast(1)

        while (!current.isAfter(today)) {
            current = when (type) {
                PaymentPeriodType.DAILY -> current.plusDays(step.toLong())
                PaymentPeriodType.WEEKLY -> current.plusWeeks(step.toLong())
                PaymentPeriodType.MONTHLY -> {
                    val next = current.plusMonths(step.toLong())
                    next.withDayOfMonth(minOf(current.dayOfMonth, next.lengthOfMonth()))
                }
                PaymentPeriodType.YEARLY -> {
                    val next = current.plusYears(step.toLong())
                    next.withDayOfMonth(minOf(current.dayOfMonth, next.lengthOfMonth()))
                }
            }
        }

        return current.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun calculateTotalPaidAmount(
        firstPaymentMillis: Long,
        period: Int,
        type: PaymentPeriodType,
        currentDate: LocalDate = LocalDate.now()
    ): Int {
        var current = Instant.ofEpochMilli(firstPaymentMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val step = period.coerceAtLeast(1)
        var payments = 0

        while (!current.isAfter(currentDate)) {
            payments++
            current = when (type) {
                PaymentPeriodType.DAILY -> current.plusDays(step.toLong())
                PaymentPeriodType.WEEKLY -> current.plusWeeks(step.toLong())
                PaymentPeriodType.MONTHLY -> {
                    val next = current.plusMonths(step.toLong())
                    next.withDayOfMonth(minOf(current.dayOfMonth, next.lengthOfMonth()))
                }
                PaymentPeriodType.YEARLY -> {
                    val next = current.plusYears(step.toLong())
                    next.withDayOfMonth(minOf(current.dayOfMonth, next.lengthOfMonth()))
                }
            }
        }

        return payments
    }

}
