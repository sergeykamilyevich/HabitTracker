package com.example.habittracker.data.network.retrofit

import android.util.Log

class DelayForRetryRequest(
    var delay: Long = INITIAL_DELAY,
    var retryCount: Int = INITIAL_COUNT
) {
    fun sleep() {
        delay = (delay * FACTOR).toLong().coerceAtMost(MAX_DELAY)
        retryCount++
        try {
            Thread.sleep(delay) //TODO delay?
        } catch (interruptedException: InterruptedException) {
            Log.d(
                "Okhttp",
                "Interrupted exception: $interruptedException"
            )
        }
    }

    companion object {
        private const val FACTOR = 1.5
        private const val MAX_DELAY = 60 * 60 * 1000L
        private const val INITIAL_DELAY = 100L
        private const val INITIAL_COUNT = 0
    }
}
