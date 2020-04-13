package com.openstartupsociety.socialtrace.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HealthProfile(
    val date: String,
    @field:Json(name = "health_profile") val questions: List<DailyCheckQuestion>
)