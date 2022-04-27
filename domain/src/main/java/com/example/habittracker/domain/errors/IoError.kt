package com.example.habittracker.domain.errors

sealed class IoError(
    open val code: Int = UNKNOWN_CODE,
    open val message: String = EMPTY_MESSAGE,
) {

    data class HabitAlreadyExistsException(override val message: String) : IoError(message = message)

    data class SqlException(override val message: String) : IoError(message = message)

    data class CloudError(
        override val code: Int = UNKNOWN_CODE,
        override val message: String
    ) : IoError(code, message)

    companion object {
        private const val UNKNOWN_CODE = 0
        private const val EMPTY_MESSAGE = ""
    }
}

