package com.openstartupsociety.socialtrace.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.prefs.PreferenceStorage
import javax.inject.Inject

class ThemeManager @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    val context: Context
) {

    fun setupTheme() {
        when (preferenceStorage.theme) {
            context.getString(R.string.pref_theme_system_value) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
            context.getString(R.string.pref_theme_light_value) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            context.getString(R.string.pref_theme_dark_value) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
            context.getString(R.string.pref_theme_battery_value) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            )
        }
    }
}
