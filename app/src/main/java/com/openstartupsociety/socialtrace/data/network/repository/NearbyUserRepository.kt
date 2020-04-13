package com.openstartupsociety.socialtrace.data.network.repository

import androidx.lifecycle.LiveData
import com.openstartupsociety.socialtrace.data.local.dao.NearbyUserDao
import com.openstartupsociety.socialtrace.data.local.entities.NearbyUser
import com.openstartupsociety.socialtrace.data.network.ApiService
import com.openstartupsociety.socialtrace.data.network.model.NotifyUsersRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NearbyUserRepository @Inject constructor(
    private val nearbyUserDao: NearbyUserDao,
    private val apiService: ApiService
) {

    val nearbyUsers: LiveData<List<NearbyUser>>
        get() = nearbyUserDao.getNearbyUser()

    suspend fun saveNearbyUser(nearbyUser: NearbyUser) = nearbyUserDao.insert(nearbyUser)

    suspend fun getContactsFromLast14Days(timestamp: Long) =
        nearbyUserDao.getContactsFromLast14Days(timestamp)

    suspend fun deleteAll() = nearbyUserDao.deleteAll()

    suspend fun notifyUsers(request: NotifyUsersRequest) = apiService.notifyUsers(request)
}
