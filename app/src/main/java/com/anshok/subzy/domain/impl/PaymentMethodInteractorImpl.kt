package com.anshok.subzy.domain.impl

import com.anshok.subzy.domain.api.PaymentMethodInteractor
import com.anshok.subzy.domain.api.PaymentMethodRepository
import com.anshok.subzy.data.local.db.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

class PaymentMethodInteractorImpl(
    private val repository: PaymentMethodRepository
) : PaymentMethodInteractor {

    override suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity) {
        repository.insertPaymentMethod(paymentMethod)
    }

    override suspend fun updatePaymentMethod(paymentMethod: PaymentMethodEntity) {
        repository.updatePaymentMethod(paymentMethod)
    }

    override suspend fun deletePaymentMethod(paymentMethod: PaymentMethodEntity) {
        repository.deletePaymentMethod(paymentMethod)
    }

    override fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>> {
        return repository.getAllPaymentMethods()
    }

    override suspend fun getPaymentMethodById(id: Long): PaymentMethodEntity? {
        return repository.getPaymentMethodById(id)
    }
}