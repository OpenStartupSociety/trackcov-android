package com.openstartupsociety.socialtrace.ui.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUser
import com.openstartupsociety.socialtrace.data.network.ApiErrorResponse
import com.openstartupsociety.socialtrace.data.network.ApiSuccessResponse
import com.openstartupsociety.socialtrace.data.network.model.Profile
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import com.openstartupsociety.socialtrace.data.vo.Event
import com.openstartupsociety.socialtrace.data.vo.Resource
import com.openstartupsociety.socialtrace.util.DateUtil
import com.openstartupsociety.socialtrace.util.DeviceUtil
import com.openstartupsociety.socialtrace.util.GENDER_FEMALE
import com.openstartupsociety.socialtrace.util.GENDER_MALE
import com.openstartupsociety.socialtrace.util.GENDER_NO_DISCLOSE
import com.openstartupsociety.socialtrace.util.JsonUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val deviceUtil: DeviceUtil
) : ViewModel() {

    var hasUpdatedHealthProfile: Boolean
        get() = userRepository.hasUpdatedHealthProfile
        set(value) {
            userRepository.hasUpdatedHealthProfile = value
        }

    val name = ObservableField("Name")
    val phone = ObservableField("Phone")
    val birthDate = ObservableField("Birth date")
    val gender = ObservableField("Gender")

    private val _profile = MutableLiveData<Resource<Profile>>()
    val profile: LiveData<Resource<Profile>> = _profile

    private val _navigateToEdit = MutableLiveData<Event<Unit>>()
    val navigateToEdit: LiveData<Event<Unit>> = _navigateToEdit

    private val _navigateToHealthProfile = MutableLiveData<Event<Boolean>>()
    val navigateToHealthProfile: LiveData<Event<Boolean>> = _navigateToHealthProfile

    private val _openMenuAction = MutableLiveData<Event<Unit>>()
    val openMenuAction: LiveData<Event<Unit>> = _openMenuAction

    private val _navigateToFullScreenCode = MutableLiveData<Event<Unit>>()
    val navigateToFullScreenCode: LiveData<Event<Unit>> = _navigateToFullScreenCode

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            when (val response = userRepository.getProfile()) {
                is ApiSuccessResponse -> {
                    val profileResponse = response.body.data
                    _profile.value = Resource.success(profileResponse)
                    name.set(profileResponse.name)
                    phone.set(profileResponse.phoneNumber)
                    profileResponse.dateOfBirth?.let { birthDate.set(DateUtil.convertToDMMMYYYY(it)) }
                    profileResponse.gender?.let {
                        when (it) {
                            GENDER_MALE -> gender.set("Male")
                            GENDER_FEMALE -> gender.set("Female")
                            GENDER_NO_DISCLOSE -> gender.set("Don't want to disclose")
                        }
                    }
                }
                is ApiErrorResponse -> _profile.value = Resource.error(response.errorMessage, null)
            }
        }
    }

    fun getBarcodeData(): String {
        val user = firebaseAuth.currentUser!!
        val nearbyUser = NearbyUser(
            deviceId = deviceUtil.getDeviceId(),
            userId = user.uid
        )
        return JsonUtil.toJson(nearbyUser)
    }

    fun openMenu() {
        _openMenuAction.value = Event(Unit)
    }

    fun openEdit() {
        _navigateToEdit.value = Event(Unit)
    }

    fun openHealthProfile() {
        _navigateToHealthProfile.value = Event(hasUpdatedHealthProfile)
    }

    fun openFullScreenCode() {
        _navigateToFullScreenCode.value = Event(Unit)
    }
}
