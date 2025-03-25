package com.anshok.subzy.domain.impl

import com.anshok.subzy.domain.api.PaymentMethodInteractor
import com.anshok.subzy.domain.api.PaymentMethodRepository
import com.anshok.subzy.data.converters.DomainToEntityMapper
import com.anshok.subzy.data.converters.EntityToDomainMapper
import com.anshok.subzy.domain.model.PaymentMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PaymentMethodInteractorImpl(
    private val repository: PaymentMethodRepository
) : PaymentMethodInteractor {

    override suspend fun insertPaymentMethod(paymentMethod: PaymentMethod) {
        repository.insertPaymentMethod(DomainToEntityMapper.paymentMethod(paymentMethod))
    }

    override suspend fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        repository.updatePaymentMethod(DomainToEntityMapper.paymentMethod(paymentMethod))
    }

    override suspend fun deletePaymentMethod(paymentMethod: PaymentMethod) {
        repository.deletePaymentMethod(DomainToEntityMapper.paymentMethod(paymentMethod))
    }

    override fun getAllPaymentMethods(): Flow<List<PaymentMethod>> {
        return repository.getAllPaymentMethods().map { list ->
            list.map { EntityToDomainMapper.paymentMethod(it) }
        }
    }

    override suspend fun getPaymentMethodById(id: Long): PaymentMethod? {
        return repository.getPaymentMethodById(id)?.let { EntityToDomainMapper.paymentMethod(it) }
    }
}
