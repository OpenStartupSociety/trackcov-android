package com.openstartupsociety.socialtrace.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.openstartupsociety.socialtrace.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providePhoneAuthProvider(): PhoneAuthProvider {
        return PhoneAuthProvider.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfigSettings(): FirebaseRemoteConfigSettings {
        return FirebaseRemoteConfigSettings.Builder().build()
    }

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(configSettings: FirebaseRemoteConfigSettings): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }
}