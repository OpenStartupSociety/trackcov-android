package com.openstartupsociety.socialtrace.data.network.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Question(
    @field:Json(name = "q_code") val questionCode: String,
    val type: String,
    val question: String,
    val values: List<String>?
) : Parcelable
