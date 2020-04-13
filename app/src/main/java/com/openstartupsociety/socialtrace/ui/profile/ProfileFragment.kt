package com.openstartupsociety.socialtrace.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.vo.EventObserver
import com.openstartupsociety.socialtrace.data.vo.Status
import com.openstartupsociety.socialtrace.databinding.FragmentProfileBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileDisplayFragment
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileFragment
import com.openstartupsociety.socialtrace.ui.scancode.FullScreenCodeFragment
import com.openstartupsociety.socialtrace.ui.settings.SettingsActivity
import com.openstartupsociety.socialtrace.util.extensions.snack
import javax.inject.Inject

class ProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.profile.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> binding.profile = resource.data
                Status.LOADING -> {
                }
                Status.ERROR -> binding.root.snack(resource.message!!)
            }
        }

        viewModel.openMenuAction.observe(viewLifecycleOwner, EventObserver { showMenuPopup() })

        viewModel.navigateToEdit.observe(viewLifecycleOwner, EventObserver {
            val fragment = EditProfileFragment.newInstance()
            activity?.supportFragmentManager?.commit {
                replace(R.id.container, fragment, fragment::class.java.simpleName)
                addToBackStack(fragment::class.java.simpleName)
            }
        })

        viewModel.navigateToHealthProfile.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                val fragment = HealthProfileDisplayFragment.newInstance()
                activity?.supportFragmentManager?.commit {
                    replace(R.id.container, fragment, fragment::class.java.simpleName)
                    addToBackStack(fragment::class.java.simpleName)
                }
            } else {
                val fragment = HealthProfileFragment.newInstance()
                activity?.supportFragmentManager?.commit {
                    replace(R.id.container, fragment, fragment::class.java.simpleName)
                    addToBackStack(fragment::class.java.simpleName)
                }
            }
        })

        viewModel.navigateToFullScreenCode.observe(viewLifecycleOwner, EventObserver {
            val fragment = FullScreenCodeFragment.newInstance()
            activity?.supportFragmentManager?.commit {
                addSharedElement(binding.barcode, "shared_element_code")
                replace(R.id.container, fragment, fragment::class.java.simpleName)
                addToBackStack(fragment::class.java.simpleName)
            }
        })
    }

    private fun showMenuPopup() {
        val popupMenu = PopupMenu(binding.root.context, binding.menu)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_profile, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.menu_profile_settings) {
                val intent = Intent(requireActivity(), SettingsActivity::class.java)
                startActivity(intent)
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
        popupMenu.show()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}