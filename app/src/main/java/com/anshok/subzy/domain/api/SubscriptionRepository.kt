package com.anshok.subzy.domain.api

import com.anshok.subzy.data.local.db.entities.CategoryEntity
import com.anshok.subzy.data.local.db.entities.PaymentMethodEntity
import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    suspend fun insertSubscription(subscription: SubscriptionEntity): Boolean
    suspend fun getSubscriptionByName(name: String): SubscriptionEntity?
    suspend fun forceInsertSubscription(subscription: SubscriptionEntity)
    suspend fun updateSubscription(subscription: SubscriptionEntity)
    suspend fun deleteSubscription(subscription: SubscriptionEntity)
    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>>
    suspend fun getSubscriptionById(id: Long): SubscriptionEntity?

    suspend fun addCategoryToSubscription(subscriptionId: Long, categoryId: Long)
    suspend fun addPaymentMethodToSubscription(subscriptionId: Long, paymentMethodId: Long)
    suspend fun getCategoriesForSubscription(subscriptionId: Long): List<CategoryEntity>
    suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<PaymentMethodEntity>
}
