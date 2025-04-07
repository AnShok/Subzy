package com.anshok.subzy.presentation.addSub.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.data.local.UserPreferences
import com.anshok.subzy.domain.api.CurrencyInteractor
import com.anshok.subzy.domain.api.SubscriptionInteractor
import com.anshok.subzy.domain.model.CurrencyRate
import com.anshok.subzy.domain.model.PaymentPeriodType
import com.anshok.subzy.domain.model.Subscription
import kotlinx.coroutines.launch
import java.util.*

class AddSubCreateViewModel(
    private val subscriptionInteractor: SubscriptionInteractor,
    private val currencyInteractor: CurrencyInteractor,
    userPreferences: UserPreferences
) : ViewModel() {

    private val _currencyCode = MutableLiveData<String>().apply {
        value = userPreferences.getDefaultCurrency()
    }
    val currencyCode: LiveData<String> = _currencyCode

    var selectedLogoUrl: String? = null // из API
    var selectedLogoResName: String? = null // из ресурсов
    var selectedImageFromGallery: String? = null // путь из галереи

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
        onResult: (success: Boolean, error: String?) -> Unit
    ) {
        if (name.isBlank() || price <= 0.0) {
            onResult(false, "Укажите название подписки и цену")
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
            comment = comment
        )

        viewModelScope.launch {
            val result = subscriptionInteractor.insertSubscription(subscription)
            onResult(result, if (!result) "A subscription with that name already exists" else null)
        }
    }

    private fun calculateNextPaymentDate(start: Long, period: Int, type: PaymentPeriodType): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date(start)
        when (type) {
            PaymentPeriodType.DAILY -> calendar.add(Calendar.DAY_OF_MONTH, period)
            PaymentPeriodType.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, period)
            PaymentPeriodType.MONTHLY -> calendar.add(Calendar.MONTH, period)
            PaymentPeriodType.YEARLY -> calendar.add(Calendar.YEAR, period)
        }
        return calendar.timeInMillis
    }
}
