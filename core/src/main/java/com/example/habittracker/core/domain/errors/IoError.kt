package com.example.habittracker.core.domain.errors

sealed class IoError(
    open val code: Int = UNKNOWN_CODE,
    open val message: String,
) {

    data class DeletingAllHabitsError(
        override val message: String = EMPTY_MESSAGE
    ) : IoError(message = message)

    data class DeletingHabitError(
        override val message: String = EMPTY_MESSAGE
    ) : IoError(message = message)

    data class HabitAlreadyExistsError(
        override val message: String = EMPTY_MESSAGE
    ) : IoError(message = message)

    data class SqlError(
        override val message: String = EMPTY_MESSAGE
    ) : IoError(message = message)

    data class CloudError(
        override val code: Int = UNKNOWN_CODE,
        override val message: String = EMPTY_MESSAGE
    ) : IoError(code, message)

    companion object {
        private const val UNKNOWN_CODE = 0
        private const val EMPTY_MESSAGE = ""
    }
}

