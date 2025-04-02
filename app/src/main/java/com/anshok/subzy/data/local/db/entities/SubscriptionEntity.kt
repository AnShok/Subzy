package com.anshok.subzy.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val logoUrl: String?,
    val price: Double,
    val currencyCode: String,
    val description: String?,
    val paymentPeriod: Int,
    val paymentPeriodType: String,
    val firstPaymentDate: Long,
    val nextPaymentDate: Long,
    val paymentMethodId: Long,
    val categoryId: Long,
    val comment: String?
)