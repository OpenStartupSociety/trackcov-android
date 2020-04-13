package com.openstartupsociety.socialtrace.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.openstartupsociety.socialtrace.databinding.FragmentNotificationsBinding
import com.openstartupsociety.socialtrace.di.Injectable
import javax.inject.Inject

class NotificationFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentNotificationsBinding

    private val viewModel: NotificationViewModel by viewModels { viewModelFactory }
    private val adapter by lazy { NotificationAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.notifications.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            viewModel.isEmpty.set(it.isEmpty())
        }
    }

    companion object {
        fun newInstance() = NotificationFragment()
    }
}