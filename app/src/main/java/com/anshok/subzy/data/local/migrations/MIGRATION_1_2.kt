package com.anshok.subzy.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // пример: добавление новой колонки
        database.execSQL("ALTER TABLE SubscriptionEntity ADD COLUMN reminderType TEXT DEFAULT 'NONE' NOT NULL")
    }
}
