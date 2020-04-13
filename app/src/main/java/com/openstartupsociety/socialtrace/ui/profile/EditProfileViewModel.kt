package com.openstartupsociety.socialtrace.ui.profile

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
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
import com.openstartupsociety.socialtrace.util.DateUtil.convertToDMMMYYYY
import com.openstartupsociety.socialtrace.util.DateUtil.convertToDDMMYYYY
import com.openstartupsociety.socialtrace.util.DeviceUtil
import com.openstartupsociety.socialtrace.util.JsonUtil
import com.openstartupsociety.socialtrace.util.NetworkUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val deviceUtil: DeviceUtil,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val firebaseUser = firebaseAuth.currentUser

    val name = ObservableField<String>(firebaseUser?.displayName)
    val phone = ObservableField<String>(firebaseUser?.phoneNumber?.substring(3))
    val gender = ObservableInt(0)
    val birthDate = ObservableField("Birth date")
    val height = ObservableField("Height")
    val weight = ObservableField("Weight")

    private val _datePickerEvent = MutableLiveData<Event<Unit>>()
    val datePickerEvent: LiveData<Event<Unit>> = _datePickerEvent

    private val _heightEvent = MutableLiveData<Event<Unit>>()
    val heightEvent: LiveData<Event<Unit>> = _heightEvent

    private val _weightEvent = MutableLiveData<Event<Unit>>()
    val weightEvent: LiveData<Event<Unit>> = _weightEvent

    private val _profile = MutableLiveData<Resource<Profile>>()
    val profile: LiveData<Resource<Profile>> = _profile

    private val _editProfile = MutableLiveData<Resource<String>>()
    val editProfile: LiveData<Resource<String>> = _editProfile

    private var profileResponse: Profile? = null

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            when (val response = userRepository.getProfile()) {
                is ApiSuccessResponse -> {
                    profileResponse = response.body.data
                    _profile.value = Resource.success(profileResponse)
                    name.set(profileResponse?.name)
                    phone.set(profileResponse?.phoneNumber?.substring(3))
                    profileResponse?.gender?.let { gender.set(it) }
                    profileResponse?.dateOfBirth?.let { birthDate.set(convertToDMMMYYYY(it)) }
                    profileResponse?.height?.let { height.set(it) }
                    profileResponse?.weight?.let { weight.set(it) }
                }
                is ApiErrorResponse -> _profile.value = Resource.error(response.errorMessage, null)
            }
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _editProfile.value = Resource.loading(null)
            val data = getProfileData()
            data?.let {
                when (val response = userRepository.putProfile(it)) {
                    is ApiSuccessResponse -> _editProfile.value =
                        Resource.success(response.body.data)
                    is ApiErrorResponse -> _editProfile.value =
                        if (!networkUtils.hasNetworkConnection())
                            Resource.error("No Internet connection", null)
                        else Resource.error(response.errorMessage, null)
                }
            }
        }
    }

    private fun getProfileData(): Profile? {
        profileResponse?.let {
            profileResponse!!.name = name.get()!!
            if (gender.get() != 0) profileResponse!!.gender = gender.get()
            if (birthDate.get() != "Birth date") {
                profileResponse!!.dateOfBirth = convertToDDMMYYYY(birthDate.get()!!)
            }
            if (height.get() != "Height") profileResponse!!.height = height.get()
            if (weight.get() != "Weight") profileResponse!!.weight = weight.get()
            return profileResponse!!
        }
        return null
    }

    fun getBarcodeData(): String {
        val user = firebaseAuth.currentUser!!
        val nearbyUser = NearbyUser(
            deviceId = deviceUtil.getDeviceId(),
            userId = user.uid
        )
        return JsonUtil.toJson(nearbyUser)
    }

    fun openDatePicker() {
        _datePickerEvent.value = Event(Unit)
    }

    fun openHeight() {
        _heightEvent.value = Event(Unit)
    }

    fun openWeight() {
        _weightEvent.value = Event(Unit)
    }
}
