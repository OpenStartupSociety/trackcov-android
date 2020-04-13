package com.openstartupsociety.socialtrace.ui.healthprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openstartupsociety.socialtrace.data.network.ApiErrorResponse
import com.openstartupsociety.socialtrace.data.network.ApiSuccessResponse
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestion
import com.openstartupsociety.socialtrace.data.network.repository.QuestionsRepository
import com.openstartupsociety.socialtrace.data.vo.Resource
import com.openstartupsociety.socialtrace.util.NetworkUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class HealthProfileDisplayViewModel @Inject constructor(
    private val repository: QuestionsRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _healthProfile = MutableLiveData<Resource<List<DailyCheckQuestion>>>()
    val healthProfile: LiveData<Resource<List<DailyCheckQuestion>>> = _healthProfile

    init {
        getHealthProfile()
    }

    private fun getHealthProfile() {
        _healthProfile.value = Resource.loading(null)
        viewModelScope.launch {
            when (val response = repository.getHealthProfile()) {
                is ApiSuccessResponse -> _healthProfile.value =
                    Resource.success(response.body.data.questions)
                is ApiErrorResponse -> _healthProfile.value =
                    if (!networkUtils.hasNetworkConnection())
                        Resource.error("No Internet connection", null)
                    else Resource.error(response.errorMessage, null)
            }
        }
    }
}