package com.anshok.subzy.domain.api

import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

interface SubscriptionInteractor {
    suspend fun insertSubscription(subscription: SubscriptionEntity): Boolean
    suspend fun getSubscriptionByName(name: String): SubscriptionEntity?
    suspend fun forceInsertSubscription(subscription: SubscriptionEntity)
    suspend fun updateSubscription(subscription: SubscriptionEntity)
    suspend fun deleteSubscription(subscription: SubscriptionEntity)
    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>>
    suspend fun getSubscriptionById(id: Long): SubscriptionEntity?
}