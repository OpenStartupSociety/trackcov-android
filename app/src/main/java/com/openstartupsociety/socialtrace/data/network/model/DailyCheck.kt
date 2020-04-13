package com.openstartupsociety.socialtrace.data.network.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DailyCheck(
    val date: String,
    val symptoms: List<DailyCheckQuestion>
) : Parcelable