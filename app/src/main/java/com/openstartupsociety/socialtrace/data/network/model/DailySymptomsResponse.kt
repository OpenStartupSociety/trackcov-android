package com.openstartupsociety.socialtrace.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailySymptomsResponse(
    val result: Int
)