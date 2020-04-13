package com.openstartupsociety.socialtrace.ui.dailycheck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptom
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptomQuestion
import com.openstartupsociety.socialtrace.data.network.ApiErrorResponse
import com.openstartupsociety.socialtrace.data.network.ApiSuccessResponse
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestion
import com.openstartupsociety.socialtrace.data.network.repository.QuestionsRepository
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import com.openstartupsociety.socialtrace.data.vo.Resource
import com.openstartupsociety.socialtrace.util.NetworkUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class DailyCheckViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository,
    private val userRepository: UserRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _submit = MutableLiveData<Resource<Int>>()
    val submit: LiveData<Resource<Int>> = _submit

    fun submitData(data: Map<String, String>) {

        viewModelScope.launch {
            _submit.value = Resource.loading(null)
            val dailyCheckQuestions = mutableListOf<DailyCheckQuestion>()
            data.map { (code, ans) ->
                val dailyCheckQuestion = DailyCheckQuestion(code, ans)
                dailyCheckQuestions.add(dailyCheckQuestion)
            }
            dailyCheckQuestions.sortBy { it.questionCode }

            when (val response = questionsRepository.submitDailySymptoms(dailyCheckQuestions)) {
                is ApiSuccessResponse -> {
                    val result = response.body.data.result
                    _submit.value = Resource.success(result)

                    userRepository.dailyCheckTimestamp = System.currentTimeMillis()
                    userRepository.symptomStatus = result

                    val questions = mutableListOf<DailySymptomQuestion>()
                    for (question in dailyCheckQuestions) {
                        val symptomQuestion = DailySymptomQuestion(
                            0,
                            question.questionCode,
                            question.answer
                        )
                        questions.add(symptomQuestion)
                    }
                    val symptom = DailySymptom(
                        0,
                        result,
                        System.currentTimeMillis(),
                        questions
                    )
                    questionsRepository.insertDailySymptom(symptom)
                }
                is ApiErrorResponse -> _submit.value = if (!networkUtils.hasNetworkConnection())
                    Resource.error("No Internet connection", null)
                else Resource.error(response.errorMessage, null)
            }
        }
    }
}