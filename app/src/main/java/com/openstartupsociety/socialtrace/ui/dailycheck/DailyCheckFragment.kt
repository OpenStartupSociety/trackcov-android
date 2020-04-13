package com.openstartupsociety.socialtrace.ui.dailycheck

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.card.MaterialCardView
import com.openstartupsociety.socialtrace.R
import com.openstartupsociety.socialtrace.data.network.model.Question
import com.openstartupsociety.socialtrace.data.vo.Status
import com.openstartupsociety.socialtrace.databinding.FragmentDailyCheckBinding
import com.openstartupsociety.socialtrace.di.Injectable
import com.openstartupsociety.socialtrace.ui.home.DashboardFragment
import com.openstartupsociety.socialtrace.util.JsonUtil.listFromJson
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_BAD
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_GOOD
import com.openstartupsociety.socialtrace.util.SYMPTOM_STATUS_OK
import com.openstartupsociety.socialtrace.util.extensions.snack
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class DailyCheckFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentDailyCheckBinding

    private val viewModel: DailyCheckViewModel by viewModels { viewModelFactory }
    private val cardParams by lazy {
        LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            setMargins(16)
        }
    }
    private val radioGroups = mutableListOf<RadioGroup>()
    private val checkboxes = mutableListOf<LinearLayout>()
    private val editTexts = mutableListOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyCheckBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.done.setOnClickListener {
            if (isDataValid()) {
                viewModel.submitData(getData())
            } else binding.root.snack("Please fill out all the details")
        }

        for (question in getQuestionsFromFile()) {
            inflateCard(question)
        }

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.submit.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    when (resource.data) {
                        SYMPTOM_STATUS_GOOD -> binding.root.snack(getString(R.string.health_status_good))
                        SYMPTOM_STATUS_OK -> binding.root.snack(getString(R.string.health_status_ok))
                        SYMPTOM_STATUS_BAD -> binding.root.snack(getString(R.string.health_status_bad))
                    }
                    val fragment = DashboardFragment.newInstance()
                    activity?.supportFragmentManager?.commit {
                        replace(R.id.container, fragment, fragment::class.java.simpleName)
                        addToBackStack(fragment::class.java.simpleName)
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> binding.root.snack(resource.message!!)
            }
        }
    }

    private fun inflateCard(question: Question) {
        val cardView = MaterialCardView(context).apply {
            radius = 16f
            layoutParams = cardParams
        }
        binding.containerQuestions.addView(cardView)

        val linearLayout = LinearLayout(context).apply {
            setPadding(48)
            orientation = LinearLayout.VERTICAL
        }
        cardView.addView(linearLayout)

        val textView = TextView(context).apply {
            text = question.question
            setPadding(0, 0, 0, 24)
        }
        TextViewCompat.setTextAppearance(
            textView,
            androidx.appcompat.R.style.TextAppearance_AppCompat_Title
        )
        linearLayout.addView(textView)

        when (question.type) {
            "radio-button" -> inflateRadioButton(question, linearLayout)
            "checkbox" -> inflateCheckBox(question, linearLayout)
            "text" -> inflateEditText(question, linearLayout)
        }
    }

    private fun inflateEditText(question: Question, linearLayout: LinearLayout) {
        val editText = EditText(context).apply {
            tag = question.questionCode
        }
        editTexts.add(editText)
        linearLayout.addView(editText)
    }

    private fun inflateCheckBox(question: Question, linearLayout: LinearLayout) {
        val innerLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            tag = question.questionCode
        }

        for (value in question.values!!) {
            val checkBox = CheckBox(context)
            checkBox.text = value
            innerLayout.addView(checkBox)
        }

        checkboxes.add(innerLayout)
        linearLayout.addView(innerLayout)
    }

    private fun inflateRadioButton(question: Question, linearLayout: LinearLayout) {
        val radioGroup = RadioGroup(context).apply {
            orientation = RadioGroup.VERTICAL
            tag = question.questionCode
        }

        for (value in question.values!!) {
            val radioButton = RadioButton(context)
            radioButton.text = value
            radioGroup.addView(radioButton)
        }

        radioGroups.add(radioGroup)
        linearLayout.addView(radioGroup)
    }

    private fun isDataValid(): Boolean {
        for (radioGroup in radioGroups)
            if (radioGroup.checkedRadioButtonId == -1)
                return false

        for (layout in checkboxes) {
            var isCheckboxChecked = false
            for (i in 0 until layout.childCount) {
                val checkBox = layout.getChildAt(i) as CheckBox
                if (checkBox.isChecked) isCheckboxChecked = true
            }
            if (!isCheckboxChecked) return false
        }

        return true
    }

    private fun getData(): Map<String, String> {
        val data = mutableMapOf<String, String>()

        for (checkBoxLayout in checkboxes) {
            val checkBoxList = mutableListOf<CheckBox>()
            for (i in 0 until checkBoxLayout.childCount)
                checkBoxList.add(checkBoxLayout.getChildAt(i) as CheckBox)
            val values = mutableListOf<String>()
            for (checkBox in checkBoxList)
                if (checkBox.isChecked) values.add(checkBox.text.toString())
            data[checkBoxLayout.tag.toString()] = TextUtils.join(", ", values)
        }

        for (radioGroup in radioGroups) {
            val id = radioGroup.checkedRadioButtonId
            val radioButton = binding.containerQuestions.findViewById<RadioButton>(id)
            data[radioGroup.tag.toString()] = radioButton.text.toString()
        }

        for (editText in editTexts)
            if (editText.text.toString().isNotEmpty())
                data[editText.tag.toString()] = editText.text.toString()

        return data
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
        return listFromJson(questionsString, Question::class.java)!!
    }

    companion object {
        fun newInstance() = DailyCheckFragment()
    }
}
