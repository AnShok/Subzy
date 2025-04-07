package com.anshok.subzy.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.anshok.subzy.data.local.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert
    suspend fun insert(reminder: ReminderEntity): Long

    @Delete
    suspend fun delete(reminder: ReminderEntity)

    @Query("SELECT * FROM reminders WHERE subscriptionId = :subscriptionId")
    fun getRemindersForSubscription(subscriptionId: Long): Flow<List<ReminderEntity>>

    @Query("DELETE FROM reminders WHERE subscriptionId = :subscriptionId")
    suspend fun deleteRemindersForSubscription(subscriptionId: Long)
}