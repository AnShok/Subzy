package com.anshok.subzy.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_methods")
data class PaymentMethodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // Название метода оплаты (например, "Visa ***1234")
    val cardNumber: String, // Номер карты (или последние 4 цифры)
    val expirationDate: Long, // Срок действия карты
    val isDefault: Boolean = false // Является ли метод оплаты по умолчанию
)