package com.openstartupsociety.socialtrace.data.network.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamMember(
    val name: String,
    val photo: String,
    val linkedIn: String
)

object TeamMemberDiffCallback : DiffUtil.ItemCallback<TeamMember>() {
    override fun areItemsTheSame(oldItem: TeamMember, newItem: TeamMember) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: TeamMember, newItem: TeamMember) = oldItem == newItem
}
