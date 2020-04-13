package com.openstartupsociety.socialtrace.data.network.repository

import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.iid.FirebaseInstanceId
import com.openstartupsociety.socialtrace.util.extensions.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val phoneAuthProvider: PhoneAuthProvider
) {

    suspend fun loginUser(sentCode: String?, enteredCode: String?, name: String?): String? {
        val credential = PhoneAuthProvider.getCredential(sentCode!!, enteredCode!!)
        val user = firebaseAuth.signInWithCredential(credential).await().user
        val request = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        user?.updateProfile(request)?.await()
        return user?.getIdToken(true)?.await()?.token
    }

    suspend fun loginUserWithCredential(credential: PhoneAuthCredential, name: String?): String? {
        val user = firebaseAuth.signInWithCredential(credential).await().user
        val request = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        user?.updateProfile(request)?.await()
        return user?.getIdToken(true)?.await()?.token
    }

    fun changeName(name: String?) {
        val request = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        firebaseAuth.currentUser?.updateProfile(request)
    }

    suspend fun getIdToken() =
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token

    suspend fun getRegistrationId() = FirebaseInstanceId.getInstance().instanceId.await().token

    fun isUserLoggedIn() = firebaseAuth.currentUser != null

    fun sendOtp(phone: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        phoneAuthProvider.verifyPhoneNumber(
            phone,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callbacks
        )
    }

    fun resendOtp(
        phone: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        resendToken: PhoneAuthProvider.ForceResendingToken?
    ) {
        phoneAuthProvider.verifyPhoneNumber(
            phone,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callbacks,
            resendToken
        )
    }

    fun logout() = firebaseAuth.signOut()
}
