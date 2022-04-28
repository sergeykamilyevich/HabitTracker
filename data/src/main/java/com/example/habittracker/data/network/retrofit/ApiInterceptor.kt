package com.example.habittracker.data.network.retrofit

import android.util.Log
import com.example.habittracker.domain.errors.IoError.CloudError
import com.example.habittracker.domain.errors.IoErrorFlow
import com.example.habittracker.domain.errors.failure
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiInterceptor @Inject constructor(private val ioErrorFlow: IoErrorFlow) : Interceptor {

    private lateinit var response: Response

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", API_TOKEN)
            .build()
        val delayForRetryRequest = DelayForRetryRequest()
        var responseIsSuccess = false
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

    companion object {
        private const val API_TOKEN = "05b550ee-1713-43f1-a842-9815d354460d"
        private const val MAX_COUNT_RETRY = Long.MAX_VALUE
        private const val UNKNOWN_NETWORK_ERROR_MESSAGE = "Unknown network error"
    }
}