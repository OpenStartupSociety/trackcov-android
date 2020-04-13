package com.openstartupsociety.socialtrace.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Login(
    val token: String,
    @field:Json(name = "is_health_profile") val hasCompletedHealthProfile: Boolean
)