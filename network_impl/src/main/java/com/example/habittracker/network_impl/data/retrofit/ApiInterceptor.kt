package com.example.habittracker.network_impl.data.retrofit

import android.util.Log
import com.example.habittracker.core_api.data.store.UserPreferences
import com.example.habittracker.core_api.domain.errors.IoError.CloudError
import com.example.habittracker.core_api.domain.errors.IoErrorFlow
import com.example.habittracker.core_api.domain.errors.failure
import com.example.habittracker.core_api.domain.errors.success
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiInterceptor @Inject constructor(
    private val ioErrorFlow: IoErrorFlow,
    private val userPreferences: UserPreferences,
) : Interceptor {

    private lateinit var response: Response

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { userPreferences.accessToken.first() } ?: EMPTY_TOKEN
        val request = chain.request().newBuilder()
            .addHeader("Authorization", token.toString())
            .build()
        val delayForRetryRequest = DelayForRetryRequest()
        var responseIsSuccess = false
        resetErrorFlow()
        do {
            try {
                response = chain.proceed(request)
                if (response.code >= 500) {
                    Log.e(
                        "Okhttp",
                        "Response error with code: ${response.code} for request: $request"
                    )

                    val cloudError = CloudError(
                        code = response.code,
                        message = response.message
                    )
                    ioErrorFlow.setError(cloudError.failure())
                    delayForRetryRequest.sleep()
                } else {
                    responseIsSuccess = true
                }
            } catch (e: Exception) {
                Log.e(
                    "Okhttp",
                    "Response exception: $e for request: $request"
                )
                val cloudError = CloudError(message = e.message ?: UNKNOWN_NETWORK_ERROR_MESSAGE)
                ioErrorFlow.setError(cloudError.failure())
                delayForRetryRequest.sleep()
            }
        } while (!responseIsSuccess && delayForRetryRequest.retryCount() < MAX_COUNT_RETRY)
        return response
    }

    private fun resetErrorFlow() {
        ioErrorFlow.setError(Unit.success())
    }

    companion object {
        private const val MAX_COUNT_RETRY = Long.MAX_VALUE
        private const val UNKNOWN_NETWORK_ERROR_MESSAGE = "Unknown network error"
        private const val EMPTY_TOKEN = ""
    }
}