package com.openstartupsociety.socialtrace.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiWrapper<T>(
    val code: Int,
    val message: String,
    val data: T
)
