package com.anshok.subzy.presentation.addSub.create

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.domain.currency.CurrencyInteractor
import com.anshok.subzy.domain.currency.model.CurrencyRate
import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
import com.anshok.subzy.domain.reminder.model.ReminderType
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.presentation.addSub.create.state.SaveResult
import com.anshok.subzy.shared.events.SubscriptionChangedNotifier
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import com.anshok.subzy.util.notification.ReminderManager


class AddSubCreateViewModel(
    private val subscriptionInteractor: SubscriptionInteractor,
    private val currencyInteractor: CurrencyInteractor,
    userPreferences: UserPreferences,
    private val subscriptionChangedNotifier: SubscriptionChangedNotifier
) : ViewModel() {

    private val _currencyCode = MutableLiveData<String>().apply {
        value = userPreferences.getDefaultCurrency()
    }
    val currencyCode: LiveData<String> = _currencyCode

    var selectedLogoUrl: String? = null
    var selectedLogoResName: String? = null
    var selectedImageFromGallery: String? = null
    var cachedCurrencies: List<CurrencyRate> = emptyList()

    fun setCurrency(code: String) {
        _currencyCode.value = code
    }

    fun loadCurrencies(onLoaded: (List<CurrencyRate>) -> Unit) {
        viewModelScope.launch {
            cachedCurrencies = currencyInteractor.loadCurrencies().map {
                CurrencyRate(
                    code = it.charCode,
                    name = it.name,
                    nominal = it.nominal,
                    value = it.value
                )
            }
            onLoaded(cachedCurrencies)
        }
    }

    fun addSubscription(
        context: Context,
        name: String,
        price: Double,
        description: String?,
        paymentPeriod: Int,
        paymentPeriodType: PaymentPeriodType,
        firstPaymentDate: Long,
        categoryId: Long = 0L,
        paymentMethodId: Long = 0L,
        comment: String? = null,
        reminderType: ReminderType = ReminderType.NONE,
        onResult: (SaveResult) -> Unit
    ) {
        if (name.isBlank() || price <= 0.0) {
            onResult(SaveResult.InvalidInput)
            return
        }

        val finalLogo = when {
            !selectedImageFromGallery.isNullOrBlank() -> selectedImageFromGallery
            !selectedLogoUrl.isNullOrBlank() -> selectedLogoUrl
            !selectedLogoResName.isNullOrBlank() -> "res://$selectedLogoResName"
            else -> "res://ic_placeholder_30px"
        }

        val currency = _currencyCode.value ?: "USD"

        val subscription = Subscription(
            id = 0,
            name = name,
            logoUrl = finalLogo,
            price = price,
            currencyCode = currency,
            description = description,
            paymentPeriod = paymentPeriod,
            paymentPeriodType = paymentPeriodType,
            firstPaymentDate = firstPaymentDate,
            nextPaymentDate = calculateNextPaymentDate(firstPaymentDate, paymentPeriod, paymentPeriodType),
            paymentMethodId = paymentMethodId,
            categoryId = categoryId,
            comment = comment,
            reminderType = reminderType
        )

        viewModelScope.launch {
            val result = subscriptionInteractor.insertSubscription(subscription)
            if (result) {
                ReminderManager.scheduleReminderForSubscription(context, subscription)
            }
            onResult(if (result) SaveResult.Success else SaveResult.Duplicate)
            subscriptionChangedNotifier.notify()
        }
    }

    fun calculateNextPaymentDate(start: Long, period: Int, type: PaymentPeriodType): Long {
        val zone = ZoneId.systemDefault()
        val from = Instant.ofEpochMilli(start).atZone(zone).toLocalDate()

        val result = when (type) {
            PaymentPeriodType.DAILY -> from.plusDays(period.toLong())
            PaymentPeriodType.WEEKLY -> from.plusWeeks(period.toLong())
            PaymentPeriodType.MONTHLY -> {
                val added = from.plusMonths(period.toLong())
                val day = minOf(from.dayOfMonth, added.lengthOfMonth())
                added.withDayOfMonth(day)
            }

            PaymentPeriodType.YEARLY -> {
                val added = from.plusYears(period.toLong())
                val day = minOf(from.dayOfMonth, added.lengthOfMonth())
                added.withDayOfMonth(day)
            }
        }

        return result.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    fun loadSubscriptionForEdit(subscriptionId: Long, onLoaded: (Subscription) -> Unit) {
        viewModelScope.launch {
            val sub = subscriptionInteractor.getSubscriptionById(subscriptionId)
            sub?.let { onLoaded(it) }
        }
    }

    fun updateSubscription(
        context: Context,
        original: Subscription,
        updated: Subscription,
        reminderType: ReminderType = ReminderType.NONE,
        onResult: (SaveResult) -> Unit
    ) {
        viewModelScope.launch {
            subscriptionInteractor.updateSubscription(
                updated.copy(id = original.id, reminderType = reminderType)
            )

            ReminderManager.scheduleReminderForSubscription(context, updated)
            onResult(SaveResult.Success)
            subscriptionChangedNotifier.notify()

        }
    }

    fun formatDate(millis: Long): String {
        val date = Date(millis)
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}
