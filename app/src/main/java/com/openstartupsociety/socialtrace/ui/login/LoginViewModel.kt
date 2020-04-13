package com.openstartupsociety.socialtrace.ui.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.openstartupsociety.socialtrace.data.network.ApiErrorResponse
import com.openstartupsociety.socialtrace.data.network.ApiSuccessResponse
import com.openstartupsociety.socialtrace.data.network.model.LoginRequest
import com.openstartupsociety.socialtrace.data.network.model.RegisterDeviceRequest
import com.openstartupsociety.socialtrace.data.network.repository.FirebaseRepository
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import com.openstartupsociety.socialtrace.data.vo.Event
import com.openstartupsociety.socialtrace.data.vo.Resource
import com.openstartupsociety.socialtrace.util.DeviceUtil
import com.openstartupsociety.socialtrace.util.ValidationUtil.isValidPhoneNumber
import com.openstartupsociety.socialtrace.util.extensions.onPropertyChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseRepository: FirebaseRepository,
    private val deviceUtil: DeviceUtil
) : ViewModel() {

    val phoneNumber = ObservableField<String>()
    val phoneNumberError = ObservableField<String>()
    val name = ObservableField<String>()
    val nameError = ObservableField<String>()
    val loading = ObservableBoolean(false)

    val otp = ObservableField<String>()
    val otpError = ObservableBoolean(false)

    private val _navigateToVerifyOtp = MutableLiveData<Event<String>>()
    val navigateToVerifyOtp: LiveData<Event<String>> = _navigateToVerifyOtp

    private val _navigateToMain = MutableLiveData<Event<Unit>>()
    val navigateToMain: LiveData<Event<Unit>> = _navigateToMain

    private val _resendAction = MutableLiveData<Event<Unit>>()
    val resendAction: LiveData<Event<Unit>> = _resendAction

    private val _otpResult = MutableLiveData<Resource<String>>()
    val otpResult: LiveData<Resource<String>> = _otpResult

    private var sentCode: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            sentCode = verificationId
            resendToken = token
        }

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            val code = credential.smsCode
            otp.set(code)
            verifyWithoutCode(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) = e.printStackTrace()
    }

    init {
        phoneNumber.onPropertyChanged { phoneNumberError.set(null) }
        name.onPropertyChanged { nameError.set(null) }
        otp.onPropertyChanged { otpError.set(false) }
    }

    fun openVerifyOtp() {
        if (isLoginDataValid()) {
            val phone = getFormattedPhone()
            _navigateToVerifyOtp.value = Event(phone)
            sendOtp(phone)
        }
    }

    fun openMain() {
        _navigateToMain.value = Event(Unit)
    }

    private fun sendOtp(phone: String) {
        _otpResult.value = Resource.loading(null)
        firebaseRepository.sendOtp(phone, callbacks)
    }

    fun resendOtp() {
        firebaseRepository.resendOtp(getFormattedPhone(), callbacks, resendToken)
        _resendAction.value = Event(Unit)
    }

    fun verifyOtp() {
        viewModelScope.launch {
            if (isOtpValid() && sentCode != null) {
                loading.set(true)
                _otpResult.value = Resource.loading(null)
                try {
                    val idToken = firebaseRepository.loginUser(sentCode, otp.get(), name.get())
                    login(idToken!!)
                } catch (e: Exception) {
                    loading.set(false)
                    if (e is FirebaseAuthInvalidCredentialsException)
                        otpError.set(true)
                    else _otpResult.value = Resource.error("An error occurred", null)
                }
            }
        }
    }

    fun verifyWithoutCode(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            loading.set(true)
            _otpResult.value = Resource.loading(null)
            try {
                val idToken = firebaseRepository.loginUserWithCredential(credential, name.get())
                login(idToken!!)
            } catch (e: Exception) {
                loading.set(false)
                if (e is FirebaseAuthInvalidCredentialsException)
                    otpError.set(true)
                else _otpResult.value = Resource.error("An error occurred", null)
            }
        }
    }

    private fun login(idToken: String) {
        viewModelScope.launch {
            val request = LoginRequest(idToken)
            when (val response = userRepository.login(request)) {
                is ApiSuccessResponse -> {
                    userRepository.token = response.body.data.token
                    userRepository.hasUpdatedHealthProfile =
                        response.body.data.hasCompletedHealthProfile
                    registerDevice()
                }
                is ApiErrorResponse -> {
                    loading.set(false)
                    _otpResult.value = Resource.error(response.errorMessage, null)
                }
            }
        }
    }

    private fun registerDevice() {
        viewModelScope.launch {
            val registrationId = firebaseRepository.getRegistrationId()
            val deviceId = deviceUtil.getDeviceId()
            val request = RegisterDeviceRequest(registrationId, deviceId)
            when (val response = userRepository.registerDevice(request)) {
                is ApiSuccessResponse -> {
                    loading.set(false)
                    _otpResult.value = Resource.success(response.body.data)
                }
                is ApiErrorResponse -> {
                    loading.set(false)
                    _otpResult.value = Resource.error(response.errorMessage, null)
                }
            }
        }
    }

    private fun isOtpValid(): Boolean {
        val otp = otp.get()
        if (otp.isNullOrEmpty() || otp.length < 6) {
            _otpResult.value = Resource.error("Enter OTP", null)
            return false
        }
        return true
    }

    private fun isLoginDataValid(): Boolean {
        if (name.get().isNullOrEmpty()) {
            nameError.set("Enter full name")
            return false
        } else if (!isValidPhoneNumber(phoneNumber.get())) {
            phoneNumberError.set("Enter valid phone number")
            return false
        }
        return true
    }

    private fun getFormattedPhone() = "+91${phoneNumber.get()!!}"

    fun getHiddenPhone(): String {
        val text = phoneNumber.get()
        return text?.substring(0, 2) + "xxxxxx" + text?.substring(text.length - 2, text.length)
    }
}
