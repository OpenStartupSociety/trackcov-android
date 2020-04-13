package com.openstartupsociety.socialtrace.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openstartupsociety.socialtrace.data.network.model.TeamMember
import com.openstartupsociety.socialtrace.databinding.FragmentAboutTeamBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.util.CustomTabHelper
import com.openstartupsociety.socialtrace.util.JsonUtil
import java.io.BufferedReader
import java.io.InputStreamReader

class AboutTeamFragment : BottomSheetDialogFragment(), Injectable {

    private lateinit var binding: FragmentAboutTeamBinding

    private val adapter by lazy { TeamMemberAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutTeamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener { dismiss() }

        binding.recyclerView.adapter = adapter
        adapter.onItemClick = { CustomTabHelper.openCustomTab(requireContext(), it) }

        adapter.submitList(getTeamMembersFromFile())
    }

    private fun getTeamMembersFromFile(): List<TeamMember> {
        val inputStream = requireContext().resources.openRawResource(
            requireContext().resources.getIdentifier(
                "team_members",
                "raw",
                requireContext().packageName
            )
        )
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        var nextLine = bufferedReader.readLine()

        while (nextLine != null) {
            stringBuilder.append(nextLine)
            stringBuilder.append("\n")
            nextLine = bufferedReader.readLine()
        }

        val membersString = stringBuilder.toString()
        return JsonUtil.listFromJson(membersString, TeamMember::class.java)!!
    }

    companion object {
        fun newInstance() = AboutTeamFragment()
    }
}
