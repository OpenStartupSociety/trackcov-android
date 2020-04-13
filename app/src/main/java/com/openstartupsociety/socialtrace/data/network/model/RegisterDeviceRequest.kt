package com.openstartupsociety.socialtrace.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterDeviceRequest(
    @field:Json(name = "registration_id") val registrationId: String,
    @field:Json(name = "device_id") val deviceId: String
)