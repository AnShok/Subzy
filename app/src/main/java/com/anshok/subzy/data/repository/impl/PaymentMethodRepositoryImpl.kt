package com.anshok.subzy.data.repository.impl

import com.anshok.subzy.data.local.LocalDataSource
import com.anshok.subzy.domain.api.PaymentMethodRepository
import com.anshok.subzy.data.local.db.entities.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

class PaymentMethodRepositoryImpl(
    private val localDataSource: LocalDataSource
) : PaymentMethodRepository {

    override suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity) {
        localDataSource.insertPaymentMethod(paymentMethod)
    }

    override suspend fun updatePaymentMethod(paymentMethod: PaymentMethodEntity) {
        localDataSource.updatePaymentMethod(paymentMethod)
    }

    override suspend fun deletePaymentMethod(paymentMethod: PaymentMethodEntity) {
        localDataSource.deletePaymentMethod(paymentMethod)
    }

    override fun getAllPaymentMethods(): Flow<List<PaymentMethodEntity>> {
        return localDataSource.getAllPaymentMethods()
    }

    override suspend fun getPaymentMethodById(id: Long): PaymentMethodEntity? {
        return localDataSource.getPaymentMethodById(id)
    }
}
