package com.anshok.subzy.domain.impl

import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import com.anshok.subzy.domain.api.SubscriptionRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.*

class SubscriptionInteractorImplTest {

    private val repository = mock(SubscriptionRepository::class.java)
    private val interactor = SubscriptionInteractorImpl(repository)

    @Test
    fun insertSubscription_shouldCallRepository() {
        runBlocking {
            val subscription = SubscriptionEntity(
                id = 0,
                name = "Netflix",
                logoUrl = "https://example.com/logo.png",
                price = 10.0,
                currencyCode = "USD",
                description = "Streaming service",
                paymentPeriodDays = 30,
                firstPaymentDate = System.currentTimeMillis(),
                nextPaymentDate = System.currentTimeMillis(),
                paymentMethodId = 1,
                categoryId = 1,
                comment = "Monthly subscription"
            )

            interactor.insertSubscription(subscription)
            verify(repository).insertSubscription(subscription)
        }
    }

    @Test
    fun getSubscriptionById_shouldCallRepository() {
        runBlocking {
            val subscriptionId = 1L
            interactor.getSubscriptionById(subscriptionId)
            verify(repository).getSubscriptionById(subscriptionId)
        }
    }
}