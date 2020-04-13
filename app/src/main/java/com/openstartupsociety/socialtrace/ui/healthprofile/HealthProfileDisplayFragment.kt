package com.openstartupsociety.socialtrace.ui.healthprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestion
import com.openstartupsociety.socialtrace.data.network.model.Question
import com.openstartupsociety.socialtrace.data.vo.Status
import com.openstartupsociety.socialtrace.databinding.FragmentHealthProfileDisplayBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.util.JsonUtil
import com.openstartupsociety.socialtrace.util.extensions.snack
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class HealthProfileDisplayFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentHealthProfileDisplayBinding

    private val viewModel: HealthProfileDisplayViewModel by viewModels { viewModelFactory }
    private val adapter by lazy { HealthProfileAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHealthProfileDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.healthProfile.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> mapQuestions(resource.data)
                Status.LOADING -> {
                }
                Status.ERROR -> binding.root.snack(resource.message!!)
            }
        }
    }

    private fun mapQuestions(questions: List<DailyCheckQuestion>?) {
        val data = getQuestionsFromFile()
        questions?.map { serverQuestion ->
            val found = data.find { it.questionCode == serverQuestion.questionCode }
            serverQuestion.questionCode = found?.question!!
        }
        adapter.submitList(questions)
    }

    private fun getQuestionsFromFile(): List<Question> {
        val inputStream = requireContext().resources.openRawResource(
            requireContext().resources.getIdentifier(
                "health_profile_questions",
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

        val questionsString = stringBuilder.toString()
        return JsonUtil.listFromJson(questionsString, Question::class.java)!!
    }


    companion object {
        fun newInstance() = HealthProfileDisplayFragment()
    }
}
