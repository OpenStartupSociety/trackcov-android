package com.openstartupsociety.socialtrace.ui

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.openstartupsociety.socialtrace.data.local.dao.NearbyUserDao
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val nearbyUserDao: NearbyUserDao
) : ViewModel() {

    val isServiceRunning = ObservableBoolean(false)
}