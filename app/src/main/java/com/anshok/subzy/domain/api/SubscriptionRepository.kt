package com.anshok.subzy.domain.api

import com.anshok.subzy.data.local.db.entities.CategoryEntity
import com.anshok.subzy.data.local.db.entities.PaymentMethodEntity
import com.anshok.subzy.data.local.db.entities.ReminderEntity
import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import com.anshok.subzy.data.remote.search.dto.LogoResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface SubscriptionRepository {
    // Работа с подписками
    suspend fun insertSubscription(subscription: SubscriptionEntity): Boolean
    suspend fun getSubscriptionByName(name: String): SubscriptionEntity?
    suspend fun forceInsertSubscription(subscription: SubscriptionEntity)
    suspend fun updateSubscription(subscription: SubscriptionEntity)
    suspend fun deleteSubscription(subscription: SubscriptionEntity)
    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>>
    suspend fun getSubscriptionById(id: Long): SubscriptionEntity?

    // Работа с API
    suspend fun searchCompany(query: String, apiKey: String): Response<LogoResponse>

    // Работа с категориями и методами оплаты
    suspend fun addCategoryToSubscription(subscriptionId: Long, categoryId: Long)
    suspend fun addPaymentMethodToSubscription(subscriptionId: Long, paymentMethodId: Long)
    suspend fun getCategoriesForSubscription(subscriptionId: Long): List<CategoryEntity>
    suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<PaymentMethodEntity>

    // Напоминания
    suspend fun insertReminder(reminder: ReminderEntity)
    suspend fun deleteReminder(reminder: ReminderEntity)
    fun getRemindersForSubscription(subscriptionId: Long): Flow<List<ReminderEntity>>
    suspend fun deleteRemindersForSubscription(subscriptionId: Long)
}