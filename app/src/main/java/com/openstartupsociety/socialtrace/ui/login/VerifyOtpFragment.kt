package com.openstartupsociety.socialtrace.ui.login

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.vo.EventObserver
import com.openstartupsociety.socialtrace.data.vo.Status
import com.openstartupsociety.socialtrace.databinding.FragmentVerifyOtpBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.util.extensions.snack
import javax.inject.Inject

class VerifyOtpFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentVerifyOtpBinding

    private val viewModel: LoginViewModel by activityViewModels { viewModelFactory }
    private val phone by lazy { arguments?.getString(ARG_PHONE)!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startTimer()

        subscribeUi()
    }

    private fun startTimer() {
        val timer = object : CountDownTimer(60000, 1000) {
            override fun onFinish() {
                binding.btnResend.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text =
                    "The verification code will expire in 00:${millisUntilFinished / 1000}"
            }
        }
        timer.start()
    }

    private fun subscribeUi() {
        viewModel.otpResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    activity?.supportFragmentManager?.commit {
                        val fragment = WelcomeFragment.newInstance()
                        replace(R.id.container, fragment, fragment::class.java.simpleName)
                        addToBackStack(null)
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> binding.root.snack(resource.message!!)
            }
        }

        viewModel.resendAction.observe(viewLifecycleOwner, EventObserver {
            binding.btnResend.isEnabled = false
            startTimer()
        })
    }

    companion object {
        private const val ARG_PHONE = "arg_phone"

        fun forPhone(phone: String): VerifyOtpFragment =
            VerifyOtpFragment().apply {
                arguments = bundleOf(ARG_PHONE to phone)
            }
    }
}
