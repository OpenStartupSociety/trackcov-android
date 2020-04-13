package com.openstartupsociety.socialtrace.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.zxing.integration.android.IntentIntegrator
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.vo.EventObserver
import com.openstartupsociety.socialtrace.data.vo.Status
import com.openstartupsociety.socialtrace.databinding.FragmentDashboardBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.services.NearbyService
import com.openstartupsociety.socialtrace.ui.dailycheck.DailyCheckFragment
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileFragment
import com.openstartupsociety.socialtrace.ui.scancode.ScannerActivity
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_BAD
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_GOOD
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_OK
import com.openstartupsociety.socialtrace.util.extensions.action
import com.openstartupsociety.socialtrace.util.extensions.gone
import com.openstartupsociety.socialtrace.util.extensions.isServiceRunning
import com.openstartupsociety.socialtrace.util.extensions.snack
import com.openstartupsociety.socialtrace.util.extensions.snackWithAction
import javax.inject.Inject

class DashboardFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentDashboardBinding

    private val viewModel: DashboardViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchNearby.isChecked =
            requireContext().isServiceRunning(NearbyService::class.java)

        handleHealthStatusCard()

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.nearbyUsers.observe(viewLifecycleOwner) {
            binding.nearbyPeople.text = it.size.toString()
        }

        viewModel.scanResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.root.snack("Saved nearby person in your log")
                }
                Status.LOADING -> {
                }
                Status.ERROR -> binding.root.snack(resource.message!!)
            }
        }

        viewModel.nearbyAction.observe(viewLifecycleOwner, EventObserver { handleService(it) })

        viewModel.navigateToScan.observe(viewLifecycleOwner, EventObserver {
            IntentIntegrator.forSupportFragment(this).apply {
                captureActivity = ScannerActivity::class.java
                setPrompt("Scan other's code")
                setOrientationLocked(false)
                initiateScan()
            }
        })

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

        viewModel.shareAction.observe(viewLifecycleOwner, EventObserver { shareLink(it) })
    }

    private fun handleHealthStatusCard() {
        when (viewModel.symptomStatus) {
            0 -> binding.cardStatus.gone()
            SYMPTOM_STATUS_GOOD -> {
                binding.cardStatus.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.green)
                )
                binding.healthStatus.text = getString(R.string.health_status_good)
                binding.healthStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), android.R.color.black)
                )
            }
            SYMPTOM_STATUS_OK -> {
                binding.cardStatus.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), android.R.color.holo_orange_light)
                )
                binding.healthStatus.text = getString(R.string.health_status_ok)
                binding.healthStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), android.R.color.black)
                )
            }
            SYMPTOM_STATUS_BAD -> {
                binding.cardStatus.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
                )
                binding.healthStatus.text = getString(R.string.health_status_bad)
                binding.healthStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), android.R.color.white)
                )
            }
        }
    }

    private fun handleService(checked: Boolean) {
        val intent = Intent(requireActivity(), NearbyService::class.java)
        if (checked) {
            requireActivity().startService(intent)
            binding.root.snack("Started looking for nearby users")
        } else {
            requireActivity().stopService(intent)
            binding.root.snack("Stopped looking for nearby users")
        }
    }

    private fun shareLink(text: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        requireContext().startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) result.contents?.let { viewModel.saveNearbyUser(it) }
        else super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        fun newInstance() = DashboardFragment()
    }
}