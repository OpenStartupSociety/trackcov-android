package com.openstartupsociety.socialtrace.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.vo.EventObserver
import com.openstartupsociety.socialtrace.databinding.FragmentLoginBinding
import com.openstartupsociety.socialtrace.di.Injectable
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.navigateToVerifyOtp.observe(viewLifecycleOwner, EventObserver {
            activity?.supportFragmentManager?.commit {
                val fragment = VerifyOtpFragment.forPhone(it)
                replace(R.id.container, fragment, fragment::class.java.simpleName)
                addToBackStack(null)
            }
        })
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}