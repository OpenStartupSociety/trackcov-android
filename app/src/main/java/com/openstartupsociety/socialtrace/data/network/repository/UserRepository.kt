package com.openstartupsociety.socialtrace.data.network.repository

import com.openstartupsociety.socialtrace.data.network.ApiService
import com.openstartupsociety.socialtrace.data.network.model.Feedback
import com.openstartupsociety.socialtrace.data.network.model.LoginRequest
import com.openstartupsociety.socialtrace.data.network.model.Profile
import com.openstartupsociety.socialtrace.data.network.model.RegisterDeviceRequest
import com.openstartupsociety.socialtrace.data.prefs.PreferenceStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferenceStorage: PreferenceStorage
) {

    var token: String?
        get() = preferenceStorage.token
        set(value) {
            preferenceStorage.token = value
        }

    var theme: String?
        get() = preferenceStorage.theme
        set(value) {
            preferenceStorage.theme = value
        }

    var hasUpdatedHealthProfile: Boolean
        get() = preferenceStorage.hasUpdatedHealthProfile
        set(value) {
            preferenceStorage.hasUpdatedHealthProfile = value
        }

    var dailyCheckTimestamp: Long
        get() = preferenceStorage.dailyCheckTimestamp
        set(value) {
            preferenceStorage.dailyCheckTimestamp = value
        }

    var symptomStatus: Int
        get() = preferenceStorage.symptomStatus
        set(value) {
            preferenceStorage.symptomStatus = value
        }

    suspend fun registerDevice(request: RegisterDeviceRequest) = apiService.registerDevice(request)

    suspend fun login(request: LoginRequest) = apiService.login(request)

    suspend fun getProfile() = apiService.getProfile()

    suspend fun putProfile(profile: Profile) = apiService.putProfile(profile)

    suspend fun sendFeedback(feedback: Feedback) = apiService.postFeedback(feedback)
}
