package com.openstartupsociety.socialtrace.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUser

@Dao
interface NearbyUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nearbyUser: NearbyUser)

    @Query("SELECT * FROM nearby_user")
    fun getNearbyUser(): LiveData<List<NearbyUser>>

    @Query("SELECT userId FROM nearby_user WHERE timestamp >= :oldTimestamp")
    suspend fun getContactsFromLast14Days(oldTimestamp: Long): List<String>

    @Query("DELETE FROM nearby_user")
    suspend fun deleteAll()
}