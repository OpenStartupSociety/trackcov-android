package com.openstartupsociety.socialtrace.util

import android.content.Context
import android.provider.Settings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceUtil @Inject constructor(
    private val context: Context
) {

    fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}