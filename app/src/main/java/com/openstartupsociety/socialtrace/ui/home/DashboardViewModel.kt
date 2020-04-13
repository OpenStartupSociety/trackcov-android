package com.openstartupsociety.socialtrace.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUser
import com.openstartupsociety.socialtrace.data.network.repository.NearbyUserRepository
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import com.openstartupsociety.socialtrace.data.vo.Event
import com.openstartupsociety.socialtrace.data.vo.Resource
import com.openstartupsociety.socialtrace.util.JsonUtil
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val nearbyUserRepository: NearbyUserRepository,
    private val userRepository: UserRepository,
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    val nearbyUsers = nearbyUserRepository.nearbyUsers

    var hasUpdatedHealthProfile = userRepository.hasUpdatedHealthProfile

    var symptomStatus = userRepository.symptomStatus

    private val _navigateToScan = MutableLiveData<Event<Unit>>()
    val navigateToScan: LiveData<Event<Unit>> = _navigateToScan

    private val _scanResult = MutableLiveData<Resource<Boolean>>()
    val scanResult: LiveData<Resource<Boolean>> = _scanResult

    private val _navigateToDailyCheck = MutableLiveData<Event<Boolean>>()
    val navigateToDailyCheck: LiveData<Event<Boolean>> = _navigateToDailyCheck

    private val _navigateToHealthProfile = MutableLiveData<Event<Unit>>()
    val navigateToHealthProfile: LiveData<Event<Unit>> = _navigateToHealthProfile

    private val _shareAction = MutableLiveData<Event<String>>()
    val shareAction: LiveData<Event<String>> = _shareAction

    private val _nearbyAction = MutableLiveData<Event<Boolean>>()
    val nearbyAction: LiveData<Event<Boolean>> = _nearbyAction

    fun saveNearbyUser(text: String) {
        _scanResult.value = Resource.loading(false)
        viewModelScope.launch {
            try {
                val nearbyUser = JsonUtil.fromJson(text, NearbyUser::class.java)
                nearbyUser?.let {
                    nearbyUserRepository.saveNearbyUser(nearbyUser)
                    _scanResult.value = Resource.success(true)
                    _scanResult.value = Resource.loading(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _scanResult.value = Resource.error("Error reading code", false)
            }
        }
    }

    fun openScan() {
        _navigateToScan.value = Event(Unit)
    }

    fun openShare() {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) _shareAction.value = Event(remoteConfig["share_text"].asString())
        }
    }

    fun openDailyCheck() {
        _navigateToDailyCheck.value = if (hasUpdatedHealthProfile) Event(true) else Event(false)
    }

    fun openHealthProfile() {
        _navigateToHealthProfile.value = Event(Unit)
    }

    fun onNearbyChange(checked: Boolean) {
        _nearbyAction.value = Event(checked)
    }

    fun hasCompletedDailyCheckToday(): Boolean {
        val now = Calendar.getInstance()
        val checkTime = Calendar.getInstance().apply {
            timeInMillis = userRepository.dailyCheckTimestamp
        }

        return now.get(Calendar.DAY_OF_YEAR) == checkTime.get(Calendar.DAY_OF_YEAR)
    }
}
