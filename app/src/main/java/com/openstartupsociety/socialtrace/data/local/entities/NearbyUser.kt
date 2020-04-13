package com.openstartupsociety.socialtrace.data.local.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "nearby_user")
data class NearbyUser(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @field:Json(name = "device_id") val deviceId : String,
    @field:Json(name = "user_id") val userId: String,
    val timestamp: Long = System.currentTimeMillis()
)

object NearbyUserDiffCallback : DiffUtil.ItemCallback<NearbyUser>() {
    override fun areItemsTheSame(oldItem: NearbyUser, newItem: NearbyUser) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: NearbyUser, newItem: NearbyUser) =
        oldItem == newItem
}
