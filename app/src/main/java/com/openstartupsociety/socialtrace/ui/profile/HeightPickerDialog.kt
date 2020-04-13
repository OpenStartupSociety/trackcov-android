package com.openstartupsociety.socialtrace.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.databinding.DialogHeightPickerBinding

class HeightPickerDialog : DialogFragment() {

    private lateinit var listener: OnHeightPickerListener
    private lateinit var selectedHeight: String

    fun setOnHeightPickerListener(listener: OnHeightPickerListener, selectedHeight: String) {
        this.listener = listener
        this.selectedHeight = selectedHeight
    }

    interface OnHeightPickerListener {
        fun onHeightPicked(height: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogHeightPickerBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_height_picker,
            null,
            false
        )

        binding.pickerOne.apply {
            minValue = 0
            maxValue = feetValues.size - 1
            displayedValues = feetValues
        }

        binding.pickerTwo.apply {
            minValue = 0
            maxValue = inchesValues.size - 1
            displayedValues = inchesValues
        }

        if (selectedHeight != requireContext().getString(R.string.height)) {
            val height: Array<String> = selectedHeight.split(" ").toTypedArray()
            val feetIndex = feetValues.indexOf(height[0])
            val inchesIndex = feetValues.indexOf(height[1])
            binding.pickerOne.value = feetIndex
            binding.pickerTwo.value = inchesIndex
        }

        return AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.height)
            setPositiveButton("OK") { _, _ ->
                listener.onHeightPicked(feetValues[binding.pickerOne.value] + " " + inchesValues[binding.pickerTwo.value])
            }
            setNegativeButton("Cancel") { _, _ -> }
            setView(binding.root)
        }.create()
    }

    companion object {
        val feetValues = (1..8).toList()
            .map { it.toString() }
            .map { "${it}ft" }
            .toTypedArray()

        val inchesValues = (0..11).toList()
            .map { it.toString() }
            .map { "${it}in" }
            .toTypedArray()
    }
}