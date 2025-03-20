package com.anshok.subzy.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscription_category")
data class SubscriptionCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val subscriptionId: Long,
    val categoryId: Long
)