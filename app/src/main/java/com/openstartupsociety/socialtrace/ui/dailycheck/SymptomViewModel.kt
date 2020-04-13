package com.openstartupsociety.socialtrace.ui.dailycheck

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openstartupsociety.socialtrace.data.network.ApiErrorResponse
import com.openstartupsociety.socialtrace.data.network.ApiSuccessResponse
import com.openstartupsociety.socialtrace.data.network.model.DailyCheck
import com.openstartupsociety.socialtrace.data.network.repository.QuestionsRepository
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import com.openstartupsociety.socialtrace.data.vo.Event
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class SymptomViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val questionsRepository: QuestionsRepository
) : ViewModel() {

    var hasUpdatedHealthProfile = userRepository.hasUpdatedHealthProfile

    val dailySymptoms = questionsRepository.getDailySymptomFromDb()

    val symptoms = mutableListOf<DailyCheck>()

    private val _navigateToDailyCheck = MutableLiveData<Event<Boolean>>()
    val navigateToDailyCheck: LiveData<Event<Boolean>> = _navigateToDailyCheck

    private val _navigateToHealthProfile = MutableLiveData<Event<Unit>>()
    val navigateToHealthProfile: LiveData<Event<Unit>> = _navigateToHealthProfile

    init {
        getDailySymptoms()
    }

    fun openDailyCheck() {
        _navigateToDailyCheck.value = if (hasUpdatedHealthProfile) Event(true) else Event(false)
    }

    fun openHealthProfile() {
        _navigateToHealthProfile.value = Event(Unit)
    }

    fun hasCompletedDailyCheckToday(): Boolean {
        val now = Calendar.getInstance()
        val checkTime = Calendar.getInstance().apply {
            timeInMillis = userRepository.dailyCheckTimestamp
        }

        return now.get(Calendar.DAY_OF_YEAR) == checkTime.get(Calendar.DAY_OF_YEAR)
    }

    private fun getDailySymptoms() {
        viewModelScope.launch {
            when (val response = questionsRepository.getDailySymptoms()) {
                is ApiSuccessResponse -> symptoms.addAll(response.body.data)
                is ApiErrorResponse ->
                    Log.d(SymptomViewModel::class.java.simpleName, response.errorMessage)
            }
        }
    }
}