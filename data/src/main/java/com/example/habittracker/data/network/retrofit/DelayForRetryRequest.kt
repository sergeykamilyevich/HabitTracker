package com.example.habittracker.data.network.retrofit

import android.util.Log

class DelayForRetryRequest(
    private var delay: Long = INITIAL_DELAY,
    private var retryCount: Int = INITIAL_COUNT
) {
    fun sleep() {
        delay = (delay * FACTOR).toLong().coerceAtMost(MAX_DELAY)
        retryCount++
        try {
            Thread.sleep(delay)
        } catch (interruptedException: InterruptedException) {
            Log.d(
                "Okhttp",
                "Interrupted exception: $interruptedException"
            )
        }
    }

    fun retryCount(): Int = retryCount

    companion object {
        private const val FACTOR = 1.5
        private const val MAX_DELAY = 60 * 60 * 1000L
        private const val INITIAL_DELAY = 1000L
        private const val INITIAL_COUNT = 0
    }
}
