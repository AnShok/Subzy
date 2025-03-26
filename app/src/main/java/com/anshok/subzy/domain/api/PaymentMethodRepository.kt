package com.anshok.subzy.domain.api

import com.anshok.subzy.data.local.db.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

interface PaymentMethodRepository {
    suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity)
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethodEntity)
    suspend fun deletePaymentMethod(paymentMethod: PaymentMethodEntity)
    fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>>
    suspend fun getPaymentMethodById(id: Long): PaymentMethodEntity?
}
