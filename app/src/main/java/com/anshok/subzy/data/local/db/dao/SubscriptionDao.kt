package com.anshok.subzy.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.anshok.subzy.data.local.entities.SubscriptionCategoryEntity
import com.anshok.subzy.data.local.entities.SubscriptionEntity
import com.anshok.subzy.data.local.entities.SubscriptionPaymentMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    @Insert
    suspend fun insert(subscription: SubscriptionEntity): Long

    @Update
    suspend fun update(subscription: SubscriptionEntity)

    @Delete
    suspend fun delete(subscription: SubscriptionEntity)

    @Query("SELECT * FROM subscriptions WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): SubscriptionEntity?

    @Query("SELECT * FROM subscriptions")
    fun getAll(): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE id = :id")
    suspend fun getById(id: Long): SubscriptionEntity?

    @Query("DELETE FROM subscriptions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert
    suspend fun addCategoryToSubscription(subscriptionCategory: SubscriptionCategoryEntity): Long

    @Insert
    suspend fun addPaymentMethodToSubscription(subscriptionPaymentMethod: SubscriptionPaymentMethodEntity): Long

    @Query("SELECT * FROM subscription_category WHERE subscriptionId = :subscriptionId")
    suspend fun getCategoriesForSubscription(subscriptionId: Long): List<SubscriptionCategoryEntity>

    @Query("SELECT * FROM subscription_payment_method WHERE subscriptionId = :subscriptionId")
    suspend fun getPaymentMethodsForSubscription(subscriptionId: Long): List<SubscriptionPaymentMethodEntity>
}