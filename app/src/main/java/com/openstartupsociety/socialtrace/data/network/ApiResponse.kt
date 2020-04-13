package com.openstartupsociety.socialtrace.data.network

import com.openstartupsociety.socialtrace.data.network.model.ApiWrapper
import org.json.JSONObject
import retrofit2.Response

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            error.printStackTrace()
            return ApiErrorResponse("An error occurred")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            if (response.isSuccessful) {
                val body = response.body()
                return when {
                    body == null -> ApiEmptyResponse()
                    (body as ApiWrapper<*>).data == null -> ApiErrorResponse(
                        body.message
                    )
                    else -> ApiSuccessResponse(
                        body
                    )
                }
            } else {
                return try {
                    val jsonObject = JSONObject(response.errorBody()?.string()!!)
                    val msg = jsonObject.getString("message")
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    ApiErrorResponse(
                        errorMsg ?: "Unknown error"
                    )
                } catch (e: Exception) {
                    ApiErrorResponse("An error occurred")
                }
            }
        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()
