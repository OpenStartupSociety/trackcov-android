package com.openstartupsociety.socialtrace.data.network.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DailyCheckQuestion(
    @field:Json(name = "q_code") var questionCode: String,
    @field:Json(name = "ans") val answer: String
) : Parcelable

object DailyCheckQuestionDiffCallback : DiffUtil.ItemCallback<DailyCheckQuestion>() {
    override fun areItemsTheSame(oldItem: DailyCheckQuestion, newItem: DailyCheckQuestion) =
        oldItem.questionCode == newItem.questionCode

    override fun areContentsTheSame(oldItem: DailyCheckQuestion, newItem: DailyCheckQuestion) =
        oldItem == newItem
}
