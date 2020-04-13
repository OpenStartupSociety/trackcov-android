package com.openstartupsociety.socialtrace.di.modules

import androidx.room.Room
import com.openstartupsociety.socialtrace.App
import com.openstartupsociety.socialtrace.data.local.AppDatabase
import com.openstartupsociety.socialtrace.data.local.dao.DailySymptomDao
import com.openstartupsociety.socialtrace.data.local.dao.LocalNotificationDao
import com.openstartupsociety.socialtrace.data.local.dao.NearbyUserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {
    @Singleton
    @Provides
    fun provideDb(app: App): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, "socialtrace.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNearbyUserDao(db: AppDatabase): NearbyUserDao {
        return db.nearbyUserDao()
    }

    @Singleton
    @Provides
    fun provideLocalNotificationDao(db: AppDatabase): LocalNotificationDao {
        return db.localNotificationDao()
    }

    @Singleton
    @Provides
    fun provideDailySymptomDao(db: AppDatabase): DailySymptomDao {
        return db.dailySymptomDao()
    }
}
