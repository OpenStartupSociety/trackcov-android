package com.openstartupsociety.socialtrace.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openstartupsociety.socialtrace.data.local.entities.LocalNotification
import com.openstartupsociety.socialtrace.data.local.entities.LocalNotificationDiffCallback
import com.openstartupsociety.socialtrace.databinding.ItemNotificationBinding

class NotificationAdapter :
    ListAdapter<LocalNotification, NotificationViewHolder>(LocalNotificationDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
        NotificationViewHolder(
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class NotificationViewHolder(
    private val binding: ItemNotificationBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: LocalNotification) {
        binding.run {
            notification = item
            executePendingBindings()
        }
    }
}