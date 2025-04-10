package com.anshok.subzy.presentation.addSub.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.data.local.preferences.UserPreferences
import com.anshok.subzy.domain.currency.CurrencyInteractor
import com.anshok.subzy.domain.currency.model.CurrencyRate
import com.anshok.subzy.domain.paymentPeriod.model.PaymentPeriodType
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.model.Subscription
import com.anshok.subzy.presentation.addSub.create.state.SaveResult
import com.anshok.subzy.shared.events.SubscriptionChangedNotifier
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

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

    fun saveSubscription(
        name: String,
        price: Double,
        description: String?,
        paymentPeriod: Int,
        paymentPeriodType: PaymentPeriodType,
        firstPaymentDate: Long,
        categoryId: Long = 0L,
        paymentMethodId: Long = 0L,
        comment: String? = null,
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
            nextPaymentDate = calculateNextPaymentDate(
                firstPaymentDate,
                paymentPeriod,
                paymentPeriodType
            ),
            paymentMethodId = paymentMethodId,
            categoryId = categoryId,
            comment = comment
        )

        viewModelScope.launch {
            val result = subscriptionInteractor.insertSubscription(subscription)
            onResult(if (result) SaveResult.Success else SaveResult.Duplicate)
            subscriptionChangedNotifier.notify()



        }
    }

    private fun calculateNextPaymentDate(start: Long, period: Int, type: PaymentPeriodType): Long {
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

}
