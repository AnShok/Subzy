package com.anshok.subzy.data.local.impl

import com.anshok.subzy.data.local.LocalDataSource
import com.anshok.subzy.data.local.db.dao.*
import com.anshok.subzy.data.local.db.entities.*
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    private val subscriptionDao: SubscriptionDao,
    private val categoryDao: CategoryDao,
    private val paymentMethodDao: PaymentMethodDao,
    private val reminderDao: ReminderDao
) : LocalDataSource {

    // Подписки
    override suspend fun insertSubscription(subscription: SubscriptionEntity) {
        subscriptionDao.insert(subscription)
    }

    override suspend fun updateSubscription(subscription: SubscriptionEntity) {
        subscriptionDao.update(subscription)
    }

    override suspend fun deleteSubscription(subscription: SubscriptionEntity) {
        subscriptionDao.delete(subscription)
    }

    override fun getAllSubscriptions(): Flow<List<SubscriptionEntity>> {
        return subscriptionDao.getAll()
    }

    override suspend fun getSubscriptionById(id: Long): SubscriptionEntity? {
        return subscriptionDao.getById(id)
    }

    override suspend fun getSubscriptionByName(name: String): SubscriptionEntity? {
        return subscriptionDao.getByName(name)
    }

    // Категории
    override suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insert(category)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.update(category)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.delete(category)
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAll()
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity? {
        return categoryDao.getById(id)
    }

    // Методы оплаты
    override suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity) {
        paymentMethodDao.insert(paymentMethod)
    }

    override suspend fun updatePaymentMethod(paymentMethod: PaymentMethodEntity) {
        paymentMethodDao.update(paymentMethod)
    }

    override suspend fun deletePaymentMethod(paymentMethod: PaymentMethodEntity) {
        paymentMethodDao.delete(paymentMethod)
    }

    override fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>> {
        return paymentMethodDao.getAll()
    }

    override suspend fun getPaymentMethodById(id: Long): PaymentMethodEntity? {
        return paymentMethodDao.getById(id)
    }

    // Напоминания
    override suspend fun insertReminder(reminder: ReminderEntity) {
        reminderDao.insert(reminder)
    }

    override suspend fun deleteReminder(reminder: ReminderEntity) {
        reminderDao.delete(reminder)
    }

    override fun getRemindersForSubscription(subscriptionId: Long): Flow<List<ReminderEntity>> {
        return reminderDao.getRemindersForSubscription(subscriptionId)
    }

    override suspend fun deleteRemindersForSubscription(subscriptionId: Long) {
        reminderDao.deleteRemindersForSubscription(subscriptionId)
    }

    // Связи подписок с категориями и методами оплаты
    override suspend fun addCategoryToSubscription(subscriptionCategory: SubscriptionCategoryEntity) {
        subscriptionDao.addCategoryToSubscription(subscriptionCategory)
    }

    override suspend fun addPaymentMethodToSubscription(subscriptionPaymentMethod: SubscriptionPaymentMethodEntity) {
        subscriptionDao.addPaymentMethodToSubscription(subscriptionPaymentMethod)
    }

    override suspend fun getCategoriesForSubscription(subscriptionId: Long): List<CategoryEntity> {
        val categoryLinks = subscriptionDao.getCategoriesForSubscription(subscriptionId)
        return categoryLinks.mapNotNull { categoryDao.getById(it.categoryId) }
    }

    override suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<PaymentMethodEntity> {
        val paymentMethodLinks = subscriptionDao.getPaymentMethodsForSubscription(subscriptionId)
        return paymentMethodLinks.mapNotNull { paymentMethodDao.getById(it.paymentMethodId) }
    }
}
