package com.openstartupsociety.socialtrace.ui.scancode

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUser
import com.openstartupsociety.socialtrace.util.DeviceUtil
import com.openstartupsociety.socialtrace.util.JsonUtil
import javax.inject.Inject

class ScannerViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val deviceUtil: DeviceUtil
) : ViewModel() {

    fun getBarcodeData(): String {
        val user = firebaseAuth.currentUser!!
        val nearbyUser = NearbyUser(
            deviceId = deviceUtil.getDeviceId(),
            userId = user.uid
        )
        return JsonUtil.toJson(nearbyUser)
    }
}
