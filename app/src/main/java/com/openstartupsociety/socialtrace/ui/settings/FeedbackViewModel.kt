package com.openstartupsociety.socialtrace.ui.settings

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openstartupsociety.socialtrace.data.network.ApiErrorResponse
import com.openstartupsociety.socialtrace.data.network.ApiSuccessResponse
import com.openstartupsociety.socialtrace.data.network.model.Feedback
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import com.openstartupsociety.socialtrace.data.vo.Resource
import com.openstartupsociety.socialtrace.util.NetworkUtils
import com.openstartupsociety.socialtrace.util.extensions.onPropertyChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedbackViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _feedback = MutableLiveData<Resource<Boolean>>()
    val feedback: LiveData<Resource<Boolean>> = _feedback

    val feedbackText = ObservableField<String>()
    val feedbackTextError = ObservableField<String>()

    init {
        feedbackText.onPropertyChanged { feedbackTextError.set(null) }
    }

    fun sendFeedback() {
        if (!feedbackText.get().isNullOrEmpty()) {
            viewModelScope.launch {
                when (val response = userRepository.sendFeedback(Feedback(feedbackText.get()!!))) {
                    is ApiSuccessResponse -> _feedback.value = Resource.success(true)
                    is ApiErrorResponse -> _feedback.value =
                        if (!networkUtils.hasNetworkConnection())
                            Resource.error("No Internet connection", null)
                        else Resource.error(response.errorMessage, null)
                }
            }
        } else feedbackTextError.set("Enter feedback")
    }
}