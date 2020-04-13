package com.openstartupsociety.socialtrace.ui.healthprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openstartupsociety.socialtrace.data.network.ApiErrorResponse
import com.openstartupsociety.socialtrace.data.network.ApiSuccessResponse
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestion
import com.openstartupsociety.socialtrace.data.network.repository.QuestionsRepository
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import com.openstartupsociety.socialtrace.data.vo.Resource
import com.openstartupsociety.socialtrace.util.NetworkUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class HealthProfileViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository,
    private val userRepository: UserRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _submit = MutableLiveData<Resource<String>>()
    val submit: LiveData<Resource<String>> = _submit

    fun submitData(data: Map<String, String>) {
        viewModelScope.launch {
            _submit.value = Resource.loading(null)
            val dailyCheckQuestions = mutableListOf<DailyCheckQuestion>()
            data.map { (code, ans) ->
                val dailyCheckQuestion = DailyCheckQuestion(code, ans)
                dailyCheckQuestions.add(dailyCheckQuestion)
            }
            dailyCheckQuestions.sortBy { it.questionCode }

            when (val response = questionsRepository.submitHealthProfile(dailyCheckQuestions)) {
                is ApiSuccessResponse -> {
                    _submit.value = Resource.success(response.body.data)
                    userRepository.hasUpdatedHealthProfile = true
                }
                is ApiErrorResponse -> _submit.value = if (!networkUtils.hasNetworkConnection())
                    Resource.error("No Internet connection", null)
                else Resource.error(response.errorMessage, null)
            }
        }
    }
}