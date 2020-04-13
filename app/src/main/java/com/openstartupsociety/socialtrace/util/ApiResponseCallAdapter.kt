package com.openstartupsociety.socialtrace.util

import com.openstartupsociety.socialtrace.data.network.ApiResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class ApiResponseCall<T>(
    private val delegate: Call<T>
) : Call<ApiResponse<T>> {
    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@ApiResponseCall,
                    Response.success(ApiResponse.create(t))
                )
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@ApiResponseCall,
                    Response.success(ApiResponse.create(response))
                )
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = ApiResponseCall(delegate)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<ApiResponse<T>> =
        Response.success(ApiResponse.create(delegate.execute()))

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}

class ApiResponseCallAdapter<R>(
    private val responseType: Type,
    private val delegate: CallAdapter<R, Call<R>>
) : CallAdapter<R, Call<ApiResponse<R>>> {
    override fun responseType() = responseType

    override fun adapt(call: Call<R>): Call<ApiResponse<R>> {
        return ApiResponseCall(delegate.adapt(call))
    }
}

class ApiResponseCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (returnType is OneArgParameterizedType) {
            return null
        }
        val parameterizedReturn = returnType as? ParameterizedType ?: return null
        if (parameterizedReturn.rawType != Call::class.java) {
            return null
        }
        val parameterizedApiResponse =
            parameterizedReturn.actualTypeArguments.firstOrNull() as? ParameterizedType
                ?: return null
        val bodyType = parameterizedApiResponse.actualTypeArguments.firstOrNull() ?: return null
        val callBody = OneArgParameterizedType(Call::class.java, arrayOf(bodyType))
        val delegate = retrofit.callAdapter(callBody, annotations) ?: return null
        @Suppress("UNCHECKED_CAST")
        return ApiResponseCallAdapter(bodyType, delegate as CallAdapter<Any, Call<Any>>)
    }
}

open class OneArgParameterizedType(
    private val type: Type,
    private val typeArgs: Array<Type>
) : ParameterizedType {
    override fun getRawType() = type

    override fun getOwnerType() = null

    override fun getActualTypeArguments() = typeArgs
}
