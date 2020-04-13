package com.openstartupsociety.socialtrace.data.network

import com.openstartupsociety.socialtrace.data.network.model.ApiWrapper
import com.openstartupsociety.socialtrace.data.network.model.DailyCheck
import com.openstartupsociety.socialtrace.data.network.model.DailyCheckQuestion
import com.openstartupsociety.socialtrace.data.network.model.DailySymptomsResponse
import com.openstartupsociety.socialtrace.data.network.model.Feedback
import com.openstartupsociety.socialtrace.data.network.model.HealthProfile
import com.openstartupsociety.socialtrace.data.network.model.Login
import com.openstartupsociety.socialtrace.data.network.model.LoginRequest
import com.openstartupsociety.socialtrace.data.network.model.NotifyUsersRequest
import com.openstartupsociety.socialtrace.data.network.model.Profile
import com.openstartupsociety.socialtrace.data.network.model.RegisterDeviceRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("login/")
    suspend fun login(@Body request: LoginRequest): ApiResponse<ApiWrapper<Login>>

    @POST("notification/device/")
    suspend fun registerDevice(@Body request: RegisterDeviceRequest): ApiResponse<ApiWrapper<String>>

    @GET("user/profile/")
    suspend fun getProfile(): ApiResponse<ApiWrapper<Profile>>

    @PUT("user/profile/")
    suspend fun putProfile(@Body profile: Profile): ApiResponse<ApiWrapper<String>>

    @POST("user/daily-symptoms/")
    suspend fun postDailySymptoms(@Body questions: List<DailyCheckQuestion>): ApiResponse<ApiWrapper<DailySymptomsResponse>>

    @GET("user/daily-symptoms/")
    suspend fun getDailySymptoms(): ApiResponse<ApiWrapper<List<DailyCheck>>>

    @POST("user/health-profile/")
    suspend fun postHealthProfile(@Body questions: List<DailyCheckQuestion>): ApiResponse<ApiWrapper<String>>

    @GET("user/health-profile/")
    suspend fun getHealthProfile(): ApiResponse<ApiWrapper<HealthProfile>>

    @POST("notification/notify-users/")
    suspend fun notifyUsers(@Body request: NotifyUsersRequest): ApiResponse<ApiWrapper<String>>

    @POST("feedback/")
    suspend fun postFeedback(@Body feedback: Feedback): ApiResponse<ApiWrapper<String>>
}
