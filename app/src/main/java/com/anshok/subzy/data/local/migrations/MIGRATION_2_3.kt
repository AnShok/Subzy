package com.anshok.subzy.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // пример: если ты ничего не добавил — миграция может быть пустой
        // или добавь нужные ALTER TABLE и т.п.
    }
}
