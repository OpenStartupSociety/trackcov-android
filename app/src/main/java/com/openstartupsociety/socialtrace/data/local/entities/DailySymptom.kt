package com.openstartupsociety.socialtrace.data.local.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "daily_symptom")
@JsonClass(generateAdapter = true)
data class DailySymptom(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val result: Int,
    val timestamp: Long,
    val questions: List<DailySymptomQuestion>
)

object DailySymptomDiffCallback : DiffUtil.ItemCallback<DailySymptom>() {
    override fun areItemsTheSame(oldItem: DailySymptom, newItem: DailySymptom) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DailySymptom, newItem: DailySymptom) =
        oldItem == newItem
}
