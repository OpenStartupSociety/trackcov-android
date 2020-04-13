package com.openstartupsociety.socialtrace.ui.dailycheck

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptom
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptomDiffCallback
import com.openstartupsociety.socialtrace.databinding.ItemDailySymptomBinding
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_BAD
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_GOOD
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_OK

class SymptomAdapter : ListAdapter<DailySymptom, SymptomViewHolder>(DailySymptomDiffCallback) {

    var onItemClick: ((Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder =
        SymptomViewHolder(
            ItemDailySymptomBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )

    override fun onBindViewHolder(holder: SymptomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SymptomViewHolder(
    private val binding: ItemDailySymptomBinding,
    private val onItemClick: ((Long) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DailySymptom) {
        binding.symptom = item

        when (item.result) {
            SYMPTOM_STATUS_GOOD -> {
                binding.cardStatus.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.green)
                )
                binding.healthStatus.text = itemView.context.getString(R.string.health_status_good)
                binding.healthStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.black)
                )
            }
            SYMPTOM_STATUS_OK -> {
                binding.cardStatus.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_orange_light)
                )
                binding.healthStatus.text = itemView.context.getString(R.string.health_status_ok)
                binding.healthStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.black)
                )
            }
            SYMPTOM_STATUS_BAD -> {
                binding.cardStatus.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_red_light)
                )
                binding.healthStatus.text = itemView.context.getString(R.string.health_status_bad)
                binding.healthStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.white)
                )
            }
        }

        binding.executePendingBindings()

        binding.cardStatus.setOnClickListener { onItemClick?.invoke(item.timestamp) }
    }
}