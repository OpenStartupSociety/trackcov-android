package com.openstartupsociety.socialtrace.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptom

@Dao
interface DailySymptomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailySymptom: DailySymptom)

    @Query("SELECT * FROM daily_symptom")
    fun getAllDailySymptoms(): LiveData<List<DailySymptom>>

    @Query("DELETE FROM daily_symptom")
    suspend fun deleteAll()
}