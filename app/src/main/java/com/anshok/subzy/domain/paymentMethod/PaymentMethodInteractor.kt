package com.anshok.subzy.domain.paymentMethod

import com.anshok.subzy.domain.paymentMethod.model.PaymentMethod
import kotlinx.coroutines.flow.Flow

interface PaymentMethodInteractor {
    suspend fun insertPaymentMethod(paymentMethod: PaymentMethod)
    suspend fun updatePaymentMethod(paymentMethod: PaymentMethod)
    suspend fun deletePaymentMethod(paymentMethod: PaymentMethod)
    fun getAllPaymentMethods(): Flow<List<PaymentMethod>>
    suspend fun getPaymentMethodById(id: Long): PaymentMethod?
}
