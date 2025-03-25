package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.local.LocalDataSource
import com.anshok.subzy.data.local.db.entities.CategoryEntity
import com.anshok.subzy.data.local.db.entities.PaymentMethodEntity
import com.anshok.subzy.data.local.db.entities.SubscriptionCategoryEntity
import com.anshok.subzy.data.local.db.entities.SubscriptionPaymentMethodEntity
import com.anshok.subzy.domain.api.SubscriptionRepository
import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

class SubscriptionRepositoryImpl(
    private val localDataSource: LocalDataSource
) : SubscriptionRepository {

    override suspend fun insertSubscription(subscription: SubscriptionEntity): Boolean {
        val existingSubscription = localDataSource.getSubscriptionByName(subscription.name)
        return if (existingSubscription == null) {
            localDataSource.insertSubscription(subscription)
            true
        } else {
            false
        }
    }

    override suspend fun forceInsertSubscription(subscription: SubscriptionEntity) {
        localDataSource.insertSubscription(subscription)
    }

    override suspend fun updateSubscription(subscription: SubscriptionEntity) {
        localDataSource.updateSubscription(subscription)
    }

    override suspend fun deleteSubscription(subscription: SubscriptionEntity) {
        localDataSource.deleteSubscription(subscription)
    }

    override fun getAllSubscriptions(): Flow<List<SubscriptionEntity>> {
        return localDataSource.getAllSubscriptions()
    }

    override suspend fun getSubscriptionById(id: Long): SubscriptionEntity? {
        return localDataSource.getSubscriptionById(id)
    }

    override suspend fun getSubscriptionByName(name: String): SubscriptionEntity? {
        return localDataSource.getSubscriptionByName(name)
    }

    override suspend fun addCategoryToSubscription(subscriptionId: Long, categoryId: Long) {
        localDataSource.addCategoryToSubscription(
            SubscriptionCategoryEntity(id = 0, subscriptionId = subscriptionId, categoryId = categoryId)
        )
    }

    override suspend fun addPaymentMethodToSubscription(subscriptionId: Long, paymentMethodId: Long) {
        localDataSource.addPaymentMethodToSubscription(
            SubscriptionPaymentMethodEntity(id = 0, subscriptionId = subscriptionId, paymentMethodId = paymentMethodId)
        )
    }

    override suspend fun getCategoriesForSubscription(subscriptionId: Long): List<CategoryEntity> {
        return localDataSource.getCategoriesForSubscription(subscriptionId)
    }

    override suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<PaymentMethodEntity> {
        return localDataSource.getPaymentMethodsForSubscription(subscriptionId)
    }
}
