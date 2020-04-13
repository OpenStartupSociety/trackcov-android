package com.openstartupsociety.socialtrace.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commitNow
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.databinding.ActivitySettingsBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.ui.login.LoginActivity
import com.openstartupsociety.socialtrace.util.CustomTabHelper
import com.openstartupsociety.socialtrace.util.URL_PRIVACY
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commitNow { replace(R.id.container, SettingsFragment()) }
    }

    override fun androidInjector() = dispatchingAndroidInjector

    class SettingsFragment : PreferenceFragmentCompat(), Injectable {
        @Inject
        lateinit var viewModelFactory: ViewModelProvider.Factory

        private val viewModel: SettingsViewModel by activityViewModels { viewModelFactory }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings)

            findPreference<ListPreference>("pref_theme")?.setOnPreferenceChangeListener { _, newValue ->
                viewModel.handleThemeChange(newValue as String)
                return@setOnPreferenceChangeListener true
            }

            findPreference<Preference>("about_team")?.setOnPreferenceClickListener {
                val fragment = AboutTeamFragment.newInstance()
                fragment.show(activity?.supportFragmentManager!!, fragment::class.java.simpleName)
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("pref_privacy_policy")?.setOnPreferenceClickListener {
                CustomTabHelper.openCustomTab(requireContext(), URL_PRIVACY)
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("pref_feedback")?.setOnPreferenceClickListener {
                openFeedbackSheet()
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("pref_logout")?.setOnPreferenceClickListener {
                viewModel.logout()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finishAffinity()
                return@setOnPreferenceClickListener true
            }
        }

        private fun openFeedbackSheet() {
            val fragment = FeedbackFragment()
            fragment.show(activity?.supportFragmentManager!!, fragment.tag)
        }
    }
}