package com.anshok.subzy.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_methods")
data class PaymentMethodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cardNumber: String,
    val expirationDate: Long,
    val isDefault: Boolean = false
)