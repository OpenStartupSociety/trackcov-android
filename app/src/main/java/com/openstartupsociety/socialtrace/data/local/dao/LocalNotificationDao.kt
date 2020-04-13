package com.openstartupsociety.socialtrace.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openstartupsociety.socialtrace.data.local.entities.LocalNotification

@Dao
interface LocalNotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: LocalNotification)

    @Query("SELECT * FROM local_notification")
    fun getAllNotifications(): LiveData<List<LocalNotification>>

    @Query("DELETE FROM local_notification")
    suspend fun deleteAll()
}