package com.openstartupsociety.socialtrace.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openstartupsociety.socialtrace.data.local.repository.NotificationRepository
import com.openstartupsociety.socialtrace.data.network.repository.FirebaseRepository
import com.openstartupsociety.socialtrace.data.network.repository.NearbyUserRepository
import com.openstartupsociety.socialtrace.data.network.repository.QuestionsRepository
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import com.openstartupsociety.socialtrace.util.ThemeManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val userRepository: UserRepository,
    private val nearbyUserRepository: NearbyUserRepository,
    private val notificationRepository: NotificationRepository,
    private val questionsRepository: QuestionsRepository,
    private val themeManager: ThemeManager
) : ViewModel() {

    fun handleThemeChange(value: String) {
        userRepository.theme = value
        themeManager.setupTheme()
    }

    fun logout() {
        firebaseRepository.logout()
        clearPrefs()
        clearDb()
    }

    private fun clearDb() {
        viewModelScope.launch {
            nearbyUserRepository.deleteAll()
            notificationRepository.deleteAll()
            questionsRepository.deleteAllDailySymptoms()
        }
    }

    private fun clearPrefs() {
        userRepository.token = null
        userRepository.hasUpdatedHealthProfile = false
        userRepository.dailyCheckTimestamp = 0
        userRepository.symptomStatus = 0
    }
}