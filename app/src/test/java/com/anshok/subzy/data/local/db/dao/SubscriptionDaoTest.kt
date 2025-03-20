package com.anshok.subzy.data.local.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.anshok.subzy.data.local.db.AppDatabase
import com.anshok.subzy.data.local.db.entities.SubscriptionEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SubscriptionDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var subscriptionDao: SubscriptionDao

    @Before
    fun setup() {
        // Используем in-memory базу данных для тестов
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        subscriptionDao = database.subscriptionDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertSubscription_shouldReturnId() = runBlocking {
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

        val id = subscriptionDao.insert(subscription)
        assert(id > 0)
    }

    @Test
    fun getSubscriptionById_shouldReturnCorrectSubscription() = runBlocking {
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

        val id = subscriptionDao.insert(subscription)
        val retrievedSubscription = subscriptionDao.getById(id)

        assert(retrievedSubscription != null)
        assert(retrievedSubscription?.name == "Netflix")
    }
}