package com.anshok.subzy.domain.impl

import com.anshok.subzy.domain.api.SubscriptionInteractor
import com.anshok.subzy.domain.api.SubscriptionRepository
import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

class SubscriptionInteractorImpl(
    private val repository: SubscriptionRepository
) : SubscriptionInteractor {

    override suspend fun insertSubscription(subscription: SubscriptionEntity): Boolean {
        val existingSubscription = repository.getSubscriptionByName(subscription.name)
        return if (existingSubscription == null) {
            repository.insertSubscription(subscription)
            true // Подписка успешно добавлена
        } else {
            false // Подписка уже существует
        }
    }

    override suspend fun forceInsertSubscription(subscription: SubscriptionEntity) {
        repository.insertSubscription(subscription) // Принудительное добавление без проверки
    }

    override suspend fun updateSubscription(subscription: SubscriptionEntity) {
        repository.updateSubscription(subscription)
    }

    override suspend fun deleteSubscription(subscription: SubscriptionEntity) {
        repository.deleteSubscription(subscription)
    }

    override fun getAllSubscriptions(): Flow<List<SubscriptionEntity>> {
        return repository.getAllSubscriptions()
    }

    override suspend fun getSubscriptionById(id: Long): SubscriptionEntity? {
        return repository.getSubscriptionById(id)
    }

    override suspend fun getSubscriptionByName(name: String): SubscriptionEntity? {
        return repository.getSubscriptionByName(name)
    }
}