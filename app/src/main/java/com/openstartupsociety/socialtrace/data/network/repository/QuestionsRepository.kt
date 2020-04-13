package com.openstartupsociety.socialtrace.data.network.repository

import com.openstartupsociety.socialtrace.data.local.dao.DailySymptomDao
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptom
import com.openstartupsociety.socialtrace.data.network.ApiService
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionsRepository @Inject constructor(
    private val apiService: ApiService,
    private val dailySymptomDao: DailySymptomDao
) {

    suspend fun submitDailySymptoms(questions: List<DailyCheckQuestion>) =
        apiService.postDailySymptoms(questions)

    suspend fun getDailySymptoms() = apiService.getDailySymptoms()

    suspend fun submitHealthProfile(questions: List<DailyCheckQuestion>) =
        apiService.postHealthProfile(questions)

    suspend fun getHealthProfile() = apiService.getHealthProfile()

    suspend fun insertDailySymptom(symptom: DailySymptom) = dailySymptomDao.insert(symptom)

    fun getDailySymptomFromDb() = dailySymptomDao.getAllDailySymptoms()

    suspend fun deleteAllDailySymptoms() = dailySymptomDao.deleteAll()
}
