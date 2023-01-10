package com.example.habittracker.core.domain.models

enum class HabitType(val id: Int) {
    BAD(0),
    GOOD(1);

    companion object {

        private const val UNKNOWN_ID_EXCEPTION = "Unknown id of HabitType"

        fun findTypeById(id: Int): HabitType {
            values().forEach {
                if (it.id == id) return it
            }
            throw RuntimeException(UNKNOWN_ID_EXCEPTION)
        }

    }
}


