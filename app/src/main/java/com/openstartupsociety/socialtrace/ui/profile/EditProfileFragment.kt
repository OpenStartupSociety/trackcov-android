package com.openstartupsociety.socialtrace.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.vo.EventObserver
import com.openstartupsociety.socialtrace.data.vo.Status
import com.openstartupsociety.socialtrace.databinding.FragmentEditProfileBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.ui.home.DashboardFragment
import com.openstartupsociety.socialtrace.util.DateUtil
import com.openstartupsociety.socialtrace.util.DateUtil.getDateFromDayYear
import com.openstartupsociety.socialtrace.util.DateUtil.getDayYear
import com.openstartupsociety.socialtrace.util.extensions.snack
import javax.inject.Inject

class EditProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentEditProfileBinding

    private val viewModel: EditProfileViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.editProfile.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.root.snack("Profile updated")
                    val fragment = DashboardFragment.newInstance()
                    activity?.supportFragmentManager?.commit {
                        replace(R.id.container, fragment, fragment::class.java.simpleName)
                        addToBackStack(fragment::class.java.simpleName)
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> binding.root.snack(resource.message!!)
            }
        }

        viewModel.profile.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> binding.executePendingBindings()
                Status.LOADING -> {
                }
                Status.ERROR -> binding.root.snack(resource.message!!)
            }
        }

        viewModel.datePickerEvent.observe(viewLifecycleOwner, EventObserver { showDatePicker() })

        viewModel.heightEvent.observe(viewLifecycleOwner, EventObserver { showHeightPicker() })

        viewModel.weightEvent.observe(viewLifecycleOwner, EventObserver { showWeightPicker() })
    }

    private fun showWeightPicker() {
        val dialog = WeightPickerDialog()
        dialog.setOnWeightPickerListener(object : WeightPickerDialog.OnWeightPickerListener {
            override fun onWeightPicked(weight: String) {
                binding.weight.text = weight
            }
        }, binding.weight.text.toString())
        dialog.show(childFragmentManager, "weight picker")
    }

    private fun showHeightPicker() {
        val dialog = HeightPickerDialog()
        dialog.setOnHeightPickerListener(object : HeightPickerDialog.OnHeightPickerListener {
            override fun onHeightPicked(height: String) {
                binding.height.text = height
            }
        }, binding.height.text.toString())
        dialog.show(childFragmentManager, "height picker")
    }

    private fun showDatePicker() {
        val end = DateUtil.timestamp

        val text = binding.birthDate.text.toString()
        val open = if (text == getString(R.string.birth_date))
            DateUtil.timestamp
        else getDateFromDayYear(text).time

        val picker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(
                CalendarConstraints.Builder()
//                    .setEnd(end)  //Temp until DD-MM/MM-DD issue gets sorted
                    .setOpenAt(open)
                    .build()
            )
            .setTitleText("Select birth date")
            .setSelection(open)
            .build()

        picker.show(childFragmentManager, "Date picker")

        picker.addOnPositiveButtonClickListener { binding.birthDate.text = getDayYear(it) }
    }

    companion object {
        fun newInstance() = EditProfileFragment()
    }
}
