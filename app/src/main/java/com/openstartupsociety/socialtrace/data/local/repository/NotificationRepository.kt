package com.openstartupsociety.socialtrace.data.local.repository

import com.openstartupsociety.socialtrace.data.local.dao.LocalNotificationDao
import com.openstartupsociety.socialtrace.data.local.entities.LocalNotification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val localNotificationDao: LocalNotificationDao
) {

    val notifications = localNotificationDao.getAllNotifications()

    suspend fun insertNotification(notification: LocalNotification) =
        localNotificationDao.insert(notification)

    suspend fun deleteAll() = localNotificationDao.deleteAll()
}