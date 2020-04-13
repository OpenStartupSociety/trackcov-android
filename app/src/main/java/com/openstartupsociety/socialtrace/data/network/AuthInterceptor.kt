package com.openstartupsociety.socialtrace.data.network

import com.openstartupsociety.socialtrace.data.prefs.PreferenceStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        preferenceStorage.token?.let { requestBuilder.addHeader(AUTH_HEADER_KEY, it) }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        const val AUTH_HEADER_KEY = "Authorization"
    }
}
