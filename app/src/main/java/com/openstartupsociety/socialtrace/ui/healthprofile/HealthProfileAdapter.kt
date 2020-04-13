package com.openstartupsociety.socialtrace.ui.healthprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestion
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestionDiffCallback
import com.openstartupsociety.socialtrace.databinding.ItemQuestionBinding

class HealthProfileAdapter :
    ListAdapter<DailyCheckQuestion, HealthProfileViewHolder>(DailyCheckQuestionDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthProfileViewHolder =
        HealthProfileViewHolder(
            ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: HealthProfileViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class HealthProfileViewHolder(
    private val binding: ItemQuestionBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(question: DailyCheckQuestion) {
        binding.run {
            item = question
            executePendingBindings()
        }
    }
}