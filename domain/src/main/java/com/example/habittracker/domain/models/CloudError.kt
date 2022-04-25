package com.example.habittracker.domain.models


data class CloudError(
    val code: Int = UNKNOWN_CODE,
    val message: String
) {
    companion object {
        private const val UNKNOWN_CODE = 0
    }
}