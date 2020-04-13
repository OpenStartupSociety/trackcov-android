package com.openstartupsociety.socialtrace.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openstartupsociety.socialtrace.data.network.repository.UserRepository
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _destination = MutableLiveData<SplashDestination>()
    val destination: LiveData<SplashDestination> = _destination

    init {
        redirect()
    }

    private fun redirect() {
        _destination.value = if (repository.token != null) SplashDestination.MAIN
        else SplashDestination.LOGIN
    }
}

enum class SplashDestination {
    LOGIN,
    MAIN
}
