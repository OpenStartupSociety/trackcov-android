package com.openstartupsociety.socialtrace.ui.notifications

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.openstartupsociety.socialtrace.data.local.repository.NotificationRepository
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    notificationRepository: NotificationRepository
) : ViewModel() {

    val notifications = notificationRepository.notifications

    val isEmpty = ObservableBoolean(true)
}