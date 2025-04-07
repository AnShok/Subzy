package com.anshok.subzy.domain.subscription

import com.anshok.subzy.data.converters.DomainToEntityMapper
import com.anshok.subzy.data.converters.EntityToDomainMapper
import com.anshok.subzy.domain.category.model.Category
import com.anshok.subzy.domain.paymentMethod.model.PaymentMethod
import com.anshok.subzy.domain.subscription.model.Subscription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubscriptionInteractorImpl(
    private val repository: SubscriptionRepository
) : SubscriptionInteractor {

    override suspend fun insertSubscription(subscription: Subscription): Boolean {
        val existingSubscription = repository.getSubscriptionByName(subscription.name)
        return if (existingSubscription == null) {
            repository.insertSubscription(DomainToEntityMapper.subscription(subscription))
            true
        } else {
            false
        }
    }

    override suspend fun forceInsertSubscription(subscription: Subscription) {
        repository.insertSubscription(DomainToEntityMapper.subscription(subscription))
    }

    override suspend fun updateSubscription(subscription: Subscription) {
        repository.updateSubscription(DomainToEntityMapper.subscription(subscription))
    }

    override suspend fun deleteSubscription(subscription: Subscription) {
        repository.deleteSubscription(DomainToEntityMapper.subscription(subscription))
    }

    override fun getAllSubscriptions(): Flow<List<Subscription>> {
        return repository.getAllSubscriptions().map { list ->
            list.map { EntityToDomainMapper.subscription(it) }
        }
    }

    override suspend fun getSubscriptionById(id: Long): Subscription? {
        return repository.getSubscriptionById(id)?.let { EntityToDomainMapper.subscription(it) }
    }

    override suspend fun getSubscriptionByName(name: String): Subscription? {
        return repository.getSubscriptionByName(name)?.let { EntityToDomainMapper.subscription(it) }
    }

    override suspend fun addCategoryToSubscription(subscriptionId: Long, categoryId: Long) {
        repository.addCategoryToSubscription(subscriptionId, categoryId)
    }

    override suspend fun addPaymentMethodToSubscription(
        subscriptionId: Long,
        paymentMethodId: Long
    ) {
        repository.addPaymentMethodToSubscription(subscriptionId, paymentMethodId)
    }

    override suspend fun getCategoriesForSubscription(subscriptionId: Long): List<Category> {
        return repository.getCategoriesForSubscription(subscriptionId)
            .map { EntityToDomainMapper.category(it) }
    }

    override suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<PaymentMethod> {
        return repository.getPaymentMethodsForSubscription(subscriptionId)
            .map { EntityToDomainMapper.paymentMethod(it) }
    }
}
