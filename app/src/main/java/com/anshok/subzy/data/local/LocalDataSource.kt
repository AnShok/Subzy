package com.anshok.subzy.data.local

import com.anshok.subzy.data.local.db.entities.*
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    // Подписки
    suspend fun insertSubscription(subscription: SubscriptionEntity)
    suspend fun updateSubscription(subscription: SubscriptionEntity)
    suspend fun deleteSubscription(subscription: SubscriptionEntity)
    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>>
    suspend fun getSubscriptionById(id: Long): SubscriptionEntity?
    suspend fun getSubscriptionByName(name: String): SubscriptionEntity?

    // Категории
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(category: CategoryEntity)
    fun getAllCategories(): Flow<List<CategoryEntity>>
    suspend fun getCategoryById(id: Long): CategoryEntity?

    // Методы оплаты
    suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity)
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethodEntity)
    suspend fun deletePaymentMethod(paymentMethod: PaymentMethodEntity)
    fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>>
    suspend fun getPaymentMethodById(id: Long): PaymentMethodEntity?

    // Напоминания
    suspend fun insertReminder(reminder: ReminderEntity)
    suspend fun deleteReminder(reminder: ReminderEntity)
    fun getRemindersForSubscription(subscriptionId: Long): Flow<List<ReminderEntity>>
    suspend fun deleteRemindersForSubscription(subscriptionId: Long)

    // Связи подписок с категориями и методами оплаты
    suspend fun addCategoryToSubscription(subscriptionCategory: SubscriptionCategoryEntity)
    suspend fun addPaymentMethodToSubscription(subscriptionPaymentMethod: SubscriptionPaymentMethodEntity)
    suspend fun getCategoriesForSubscription(subscriptionId: Long): List<CategoryEntity>
    suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<PaymentMethodEntity>
}
