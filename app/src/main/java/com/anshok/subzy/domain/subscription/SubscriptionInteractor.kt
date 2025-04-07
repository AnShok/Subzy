package com.anshok.subzy.domain.subscription

import com.anshok.subzy.domain.category.model.Category
import com.anshok.subzy.domain.paymentMethod.model.PaymentMethod
import com.anshok.subzy.domain.subscription.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionInteractor {
    suspend fun insertSubscription(subscription: Subscription): Boolean
    suspend fun getSubscriptionByName(name: String): Subscription?
    suspend fun forceInsertSubscription(subscription: Subscription)
    suspend fun updateSubscription(subscription: Subscription)
    suspend fun deleteSubscription(subscription: Subscription)
    fun getAllSubscriptions(): Flow<List<Subscription>>
    suspend fun getSubscriptionById(id: Long): Subscription?

    suspend fun addCategoryToSubscription(subscriptionId: Long, categoryId: Long)
    suspend fun addPaymentMethodToSubscription(subscriptionId: Long, paymentMethodId: Long)
    suspend fun getCategoriesForSubscription(subscriptionId: Long): List<Category>
    suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<PaymentMethod>
}
