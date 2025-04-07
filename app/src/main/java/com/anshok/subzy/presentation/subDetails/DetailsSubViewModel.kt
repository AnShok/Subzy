package com.anshok.subzy.presentation.subDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshok.subzy.domain.subscription.SubscriptionInteractor
import com.anshok.subzy.domain.subscription.model.Subscription
import kotlinx.coroutines.launch

class DetailsSubViewModel(
    private val interactor: SubscriptionInteractor
) : ViewModel() {

    private val _subscription = MutableLiveData<Subscription?>()
    val subscription: LiveData<Subscription?> = _subscription

    private val _details = MutableLiveData<List<Pair<String, String>>>()
    val details: LiveData<List<Pair<String, String>>> = _details

    private var currentSubscription: Subscription? = null

    fun loadSubscriptionDetails(id: Long) {
        viewModelScope.launch {
            val subscription = interactor.getSubscriptionById(id)
            currentSubscription = subscription
            _subscription.postValue(subscription)

            subscription?.let {
                val details = listOf(
                    "Description" to (it.description ?: "Not specified"),
                    "Payment period" to "${it.paymentPeriod} ${it.paymentPeriodType.name.lowercase()}",
                    "First payment" to formatDate(it.firstPaymentDate),
                    "Reminder" to "Not specified", // TODO: load reminder from ReminderRepository
                    "Payment method" to it.paymentMethodId.toString(),
                    "Currency" to it.currencyCode,
                    "Category" to it.categoryId.toString(),
                    "Comment" to (it.comment ?: "Not specified")
                )
                _details.postValue(details)
            }
        }
    }

    fun deleteSubscription(onDeleted: () -> Unit) {
        viewModelScope.launch {
            currentSubscription?.let {
                interactor.deleteSubscription(it)
                onDeleted()
            }
        }
    }

    private fun formatDate(millis: Long): String {
        val date = java.util.Date(millis)
        val formatter = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
        return formatter.format(date)
    }
}
