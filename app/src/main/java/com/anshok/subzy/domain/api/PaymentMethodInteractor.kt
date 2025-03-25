package com.anshok.subzy.domain.api

import com.anshok.subzy.domain.model.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface PaymentMethodInteractor {
    suspend fun insertPaymentMethod(paymentMethod: PaymentMethod)
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethod)
    suspend fun deletePaymentMethod(paymentMethod: PaymentMethod)
    fun getAllPaymentMethods(): Flow<List<PaymentMethod>>
    suspend fun getPaymentMethodById(id: Long): PaymentMethod?
}
