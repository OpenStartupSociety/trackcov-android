package com.openstartupsociety.socialtrace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openstartupsociety.socialtrace.data.local.dao.DailySymptomDao
import com.openstartupsociety.socialtrace.data.local.dao.LocalNotificationDao
import com.openstartupsociety.socialtrace.data.local.dao.NearbyUserDao
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptom
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptomQuestion
import com.openstartupsociety.socialtrace.data.local.entities.LocalNotification
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUser

@Database(
    entities = [
        NearbyUser::class,
        LocalNotification::class,
        DailySymptom::class,
        DailySymptomQuestion::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nearbyUserDao(): NearbyUserDao
    abstract fun localNotificationDao(): LocalNotificationDao
    abstract fun dailySymptomDao(): DailySymptomDao
}
