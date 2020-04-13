package com.openstartupsociety.socialtrace.di.modules

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.openstartupsociety.socialtrace.App
import com.openstartupsociety.socialtrace.data.prefs.PreferenceStorage
import com.openstartupsociety.socialtrace.data.prefs.SharedPreferenceStorage
import com.openstartupsociety.socialtrace.util.DeviceUtil
import com.openstartupsociety.socialtrace.util.NetworkUtils
import com.openstartupsociety.socialtrace.util.ThemeManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Singleton
    @Provides
    fun providesThemeManager(preferenceStorage: PreferenceStorage, context: Context): ThemeManager =
        ThemeManager(preferenceStorage, context)

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun provideDeviceUtil(context: Context): DeviceUtil = DeviceUtil(context)

    @Singleton
    @Provides
    fun provideNetworkUtils(context: Context): NetworkUtils = NetworkUtils(context)
}