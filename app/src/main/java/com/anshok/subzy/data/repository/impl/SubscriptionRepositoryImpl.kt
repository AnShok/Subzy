package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.local.LocalDataSource
import com.anshok.subzy.data.remote.RemoteDataSource
import com.anshok.subzy.data.remote.search.dto.LogoResponse
import com.anshok.subzy.domain.api.SubscriptionRepository
import com.anshok.subzy.data.local.db.entities.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class SubscriptionRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
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

    override suspend fun searchCompany(query: String, apiKey: String): Response<LogoResponse> {
        return remoteDataSource.searchCompany(query, apiKey)
    }

    override suspend fun addCategoryToSubscription(subscriptionId: Long, categoryId: Long) {
        localDataSource.addCategoryToSubscription(
            SubscriptionCategoryEntity(0, subscriptionId, categoryId)
        )
    }

    override suspend fun addPaymentMethodToSubscription(subscriptionId: Long, paymentMethodId: Long) {
        localDataSource.addPaymentMethodToSubscription(
            SubscriptionPaymentMethodEntity(0, subscriptionId, paymentMethodId)
        )
    }

    override suspend fun getCategoriesForSubscription(subscriptionId: Long): List<CategoryEntity> {
        return localDataSource.getCategoriesForSubscription(subscriptionId)
    }

    override suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<PaymentMethodEntity> {
        return localDataSource.getPaymentMethodsForSubscription(subscriptionId)
    }

    override suspend fun insertReminder(reminder: ReminderEntity) {
        localDataSource.insertReminder(reminder)
    }

    override suspend fun deleteReminder(reminder: ReminderEntity) {
        localDataSource.deleteReminder(reminder)
    }

    override fun getRemindersForSubscription(subscriptionId: Long): Flow<List<ReminderEntity>> {
        return localDataSource.getRemindersForSubscription(subscriptionId)
    }

    override suspend fun deleteRemindersForSubscription(subscriptionId: Long) {
        localDataSource.deleteRemindersForSubscription(subscriptionId)
    }
}
