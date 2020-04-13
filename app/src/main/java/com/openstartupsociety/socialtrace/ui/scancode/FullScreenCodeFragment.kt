package com.openstartupsociety.socialtrace.ui.scancode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import com.openstartupsociety.socialtrace.databinding.FragmentFullScreenCodeBinding
import com.openstartupsociety.socialtrace.di.Injectable
import javax.inject.Inject

class FullScreenCodeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentFullScreenCodeBinding

    private val viewModel: FullScreenCodeViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFullScreenCodeBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = ChangeBounds()
    }

    companion object {
        fun newInstance() = FullScreenCodeFragment()
    }
}
