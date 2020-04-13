package com.openstartupsociety.socialtrace.ui.dailycheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.openstartupsociety.socialtrace.data.network.model.DailyCheck
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestion
import com.openstartupsociety.socialtrace.data.network.model.Question
import com.openstartupsociety.socialtrace.databinding.FragmentSymptomDisplayBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.ui.healthprofile.HealthProfileAdapter
import com.openstartupsociety.socialtrace.util.JsonUtil
import java.io.BufferedReader
import java.io.InputStreamReader

class SymptomDisplayFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentSymptomDisplayBinding

    private val dailyCheck by lazy { arguments?.getParcelable<DailyCheck>(ARG_DAILY_CHECK) }
    private val adapter by lazy { HealthProfileAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSymptomDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        mapQuestions(dailyCheck?.symptoms)
    }

    private fun mapQuestions(questions: List<DailyCheckQuestion>?) {
        val data = getQuestionsFromFile()
        questions?.map { serverQuestion ->
            val found = data.find { it.questionCode == serverQuestion.questionCode }
            found?.let { serverQuestion.questionCode = found.question }
        }
        adapter.submitList(questions)
    }

    private fun getQuestionsFromFile(): List<Question> {
        val inputStream = requireContext().resources.openRawResource(
            requireContext().resources.getIdentifier(
                "daily_check_questions",
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
        private const val ARG_DAILY_CHECK = "arg_daily_check"

        fun forSymptom(dailyCheck: DailyCheck) =
            SymptomDisplayFragment().apply {
                arguments = bundleOf(ARG_DAILY_CHECK to dailyCheck)
            }
    }
}