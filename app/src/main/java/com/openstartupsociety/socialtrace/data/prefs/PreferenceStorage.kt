package com.openstartupsociety.socialtrace.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.openstartupsociety.socialtrace.R
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var token: String?
    var theme: String?
    var hasUpdatedHealthProfile: Boolean
    var dailyCheckTimestamp: Long
    var symptomStatus: Int
}

@Singleton
class SharedPreferenceStorage @Inject constructor(context: Context) : PreferenceStorage {

    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override var token by StringPreference(prefs, TOKEN, null)

    override var theme by StringPreference(
        prefs,
        THEME,
        context.getString(R.string.pref_theme_default_value)
    )

    override var hasUpdatedHealthProfile by BooleanPreference(
        prefs,
        HAS_UPDATED_HEALTH_PROFILE,
        false
    )

    override var dailyCheckTimestamp by LongPreference(prefs, DAILY_CHECK_TIMESTAMP, 0)

    override var symptomStatus by IntPreference(prefs, SYMPTOM_STATUS, 0)

    companion object {
        const val PREFS_NAME = "trackcovid"
        const val TOKEN = "token"
        const val THEME = "theme"
        const val HAS_UPDATED_HEALTH_PROFILE = "has_updated_health_profile"
        const val DAILY_CHECK_TIMESTAMP = "daily_check_timestamp"
        const val SYMPTOM_STATUS = "symptom_status"
    }
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.value.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}

class LongPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Long
) : ReadWriteProperty<Any, Long> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return preferences.value.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        preferences.value.edit { putLong(name, value) }
    }
}

class IntPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.value.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.value.edit { putInt(name, value) }
    }
}
