package com.openstartupsociety.socialtrace.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUser
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUserDiffCallback
import com.openstartupsociety.socialtrace.databinding.ItemNearbyUserBinding

class NearbyUserAdapter : ListAdapter<NearbyUser, NearbyUserViewHolder>(NearbyUserDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyUserViewHolder =
        NearbyUserViewHolder(
            ItemNearbyUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: NearbyUserViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class NearbyUserViewHolder(
    private val binding: ItemNearbyUserBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        nearbyUser: NearbyUser
    ) {
        binding.run {
            user = nearbyUser
            executePendingBindings()
        }
    }
}