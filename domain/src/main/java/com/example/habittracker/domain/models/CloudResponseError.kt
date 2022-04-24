package com.example.habittracker.domain.models


data class CloudResponseError(
    val code: Int = UNKNOWN_CODE,
    val message: String
) {
    companion object {
        private const val UNKNOWN_CODE = 0
    }
}