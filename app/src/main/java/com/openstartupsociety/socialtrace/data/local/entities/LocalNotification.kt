package com.openstartupsociety.socialtrace.data.local.entities

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_notification")
data class LocalNotification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val timestamp: Long
)

object LocalNotificationDiffCallback : DiffUtil.ItemCallback<LocalNotification>() {
    override fun areItemsTheSame(oldItem: LocalNotification, newItem: LocalNotification) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: LocalNotification, newItem: LocalNotification) =
        oldItem == newItem
}
