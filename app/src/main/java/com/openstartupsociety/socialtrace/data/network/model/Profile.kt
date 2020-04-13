package com.openstartupsociety.socialtrace.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Profile(
    @field:Json(name = "user_id") val userId: String,
    var name: String?,
    @field:Json(name = "phone_number") val phoneNumber: String,
    @field:Json(name = "date_of_birth") var dateOfBirth: String?,
    var gender: Int?,
    var height: String?,
    var weight: String?
)