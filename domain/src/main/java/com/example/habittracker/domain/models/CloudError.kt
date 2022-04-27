package com.example.habittracker.domain.models


data class CloudError(
    val code: Int = UNKNOWN_CODE,
    val message: String = EMPTY_MESSAGE
) {
    companion object {
        private const val UNKNOWN_CODE = 0
        private const val EMPTY_MESSAGE = ""
    }
}