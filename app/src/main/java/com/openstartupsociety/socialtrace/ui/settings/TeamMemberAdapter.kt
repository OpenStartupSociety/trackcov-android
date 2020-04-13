package com.openstartupsociety.socialtrace.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openstartupsociety.socialtrace.data.network.model.TeamMember
import com.openstartupsociety.socialtrace.data.network.model.TeamMemberDiffCallback
import com.openstartupsociety.socialtrace.databinding.ItemTeamMemberBinding

class TeamMemberAdapter : ListAdapter<TeamMember, TeamMemberViewHolder>(TeamMemberDiffCallback) {

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TeamMemberViewHolder(
            ItemTeamMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )

    override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class TeamMemberViewHolder(
    private val binding: ItemTeamMemberBinding,
    private val onItemClick: ((String) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TeamMember) {
        binding.run {
            member = item
            executePendingBindings()
        }
        itemView.setOnClickListener { onItemClick?.invoke(item.linkedIn) }
    }
}