package com.anshok.subzy.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anshok.subzy.data.local.db.dao.CategoryDao
import com.anshok.subzy.data.local.db.dao.PaymentMethodDao
import com.anshok.subzy.data.local.db.dao.ReminderDao
import com.anshok.subzy.data.local.db.dao.SubscriptionDao
import com.anshok.subzy.data.local.entities.CategoryEntity
import com.anshok.subzy.data.local.entities.PaymentMethodEntity
import com.anshok.subzy.data.local.entities.ReminderEntity
import com.anshok.subzy.data.local.entities.SubscriptionCategoryEntity
import com.anshok.subzy.data.local.entities.SubscriptionEntity
import com.anshok.subzy.data.local.entities.SubscriptionPaymentMethodEntity

@Database(
    entities = [
        SubscriptionEntity::class,
        CategoryEntity::class,
        PaymentMethodEntity::class,
        SubscriptionCategoryEntity::class,
        SubscriptionPaymentMethodEntity::class,
        ReminderEntity::class
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun paymentMethodDao(): PaymentMethodDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "subscription_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}