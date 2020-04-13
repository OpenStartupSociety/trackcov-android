package com.openstartupsociety.socialtrace.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.databinding.DialogWeightPickerBinding

class WeightPickerDialog : DialogFragment() {

    private lateinit var listener: OnWeightPickerListener
    private lateinit var selectedWeight: String

    fun setOnWeightPickerListener(listener: OnWeightPickerListener, selectedWeight: String) {
        this.listener = listener
        this.selectedWeight = selectedWeight
    }

    interface OnWeightPickerListener {
        fun onWeightPicked(weight: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogWeightPickerBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_weight_picker,
            null,
            false
        )

        binding.pickerOne.apply {
            minValue = 0
            maxValue = 200
            value = 60
        }

        binding.pickerTwo.apply {
            minValue = 0
            maxValue = 9
        }

        if (selectedWeight != requireContext().getString(R.string.weight)) {
            val text = selectedWeight.replace(" kg", "")
            val weight = text.split(".").toTypedArray()
            binding.pickerOne.value = weight[0].toInt()
            binding.pickerTwo.value = weight[1].toInt()
        }

        return AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.weight)
            setPositiveButton("OK") { _, _ ->
                listener.onWeightPicked(binding.pickerOne.value.toString() + "." + binding.pickerTwo.value.toString() + " kg")
            }
            setNegativeButton("Cancel") { _, _ -> }
            setView(binding.root)
        }.create()
    }
}