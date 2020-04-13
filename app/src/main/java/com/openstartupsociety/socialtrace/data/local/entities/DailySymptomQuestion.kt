package com.openstartupsociety.socialtrace.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class DailySymptomQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @field:Json(name = "q_code") val questionCode: String,
    @field:Json(name = "ans") val answer: String
)
