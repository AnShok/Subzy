package com.anshok.subzy.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,  // Название подписки (например, "Netflix")
    val logoUrl: String?,  // URL логотипа подписки (может быть null)
    val price: Double,  // Стоимость подписки
    val currencyCode: String,  // Код валюты (USD, EUR)
    val description: String?,  // Описание (необязательно)
    val paymentPeriodDays: Int,  // Период оплаты в днях (например, 30 для месячной подписки)
    val firstPaymentDate: Long,  // Timestamp даты первой оплаты
    val nextPaymentDate: Long,  // Timestamp даты следующего платежа
    val paymentMethodId: Long,  // ID метода оплаты
    val categoryId: Long,  // ID категории подписки
    val comment: String?  // Комментарий (опционально)
)