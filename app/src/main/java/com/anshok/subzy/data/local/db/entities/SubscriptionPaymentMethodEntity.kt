package com.anshok.subzy.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscription_payment_method")
data class SubscriptionPaymentMethodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val subscriptionId: Long,
    val paymentMethodId: Long
)