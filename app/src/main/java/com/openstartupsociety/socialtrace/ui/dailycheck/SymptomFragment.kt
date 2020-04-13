package com.openstartupsociety.socialtrace.ui.dailycheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.vo.EventObserver
import com.openstartupsociety.socialtrace.databinding.FragmentSymptomBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileFragment
import com.openstartupsociety.socialtrace.util.DateUtil
import com.openstartupsociety.socialtrace.util.extensions.action
import com.openstartupsociety.socialtrace.util.extensions.snackWithAction
import javax.inject.Inject

class SymptomFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentSymptomBinding

    private val viewModel: SymptomViewModel by viewModels { viewModelFactory }
    private val adapter by lazy { SymptomAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSymptomBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        adapter.onItemClick = {
            val date = DateUtil.convertToDDMMYYYY(it)
            val item = viewModel.symptoms.find { dailyCheck -> dailyCheck.date == date }
            item?.let { dailyCheck ->
                val fragment = SymptomDisplayFragment.forSymptom(dailyCheck)
                activity?.supportFragmentManager?.commit {
                    replace(R.id.container, fragment, fragment::class.java.simpleName)
                    addToBackStack(fragment::class.java.simpleName)
                }
            }
        }

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.dailySymptoms.observe(viewLifecycleOwner) { adapter.submitList(it.reversed()) }

        viewModel.navigateToDailyCheck.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                val fragment = DailyCheckFragment.newInstance()
                activity?.supportFragmentManager?.commit {
                    replace(R.id.container, fragment, fragment::class.java.simpleName)
                    addToBackStack(fragment::class.java.simpleName)
                }
            } else binding.root.snackWithAction("Complete your health profile to do your daily check-in") {
                action("Complete") { viewModel.openHealthProfile() }
            }
        })

        viewModel.navigateToHealthProfile.observe(viewLifecycleOwner, EventObserver {
            val fragment = HealthProfileFragment.newInstance()
            activity?.supportFragmentManager?.commit {
                replace(R.id.container, fragment, fragment::class.java.simpleName)
                addToBackStack(fragment::class.java.simpleName)
            }
        })
    }

    companion object {
        fun newInstance() = SymptomFragment()
    }
}
