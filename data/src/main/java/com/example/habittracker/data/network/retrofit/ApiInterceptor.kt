package com.example.habittracker.data.network.retrofit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {

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
                    delayForRetryRequest.sleep()
                } else {
                    responseIsSuccess = true
                }
            } catch (e: Exception) {
                Log.d("Okhttp", "Response exception: $e for request: $request")
                delayForRetryRequest.sleep()
            }
        } while (!responseIsSuccess && delayForRetryRequest.retryCount < MAX_COUNT_RETRY)
        return response
    }

    companion object {
        private const val API_TOKEN = "05b550ee-1713-43f1-a842-9815d354460d"
        private const val MAX_COUNT_RETRY = Long.MAX_VALUE
    }
}