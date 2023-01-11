package com.example.habittracker.core_api.domain.models

enum class HabitPriority(val id: Int) {
    LOW(0),
    NORMAL(1),
    HIGH(2);

    companion object {

        private const val UNKNOWN_ID_EXCEPTION = "Unknown id of HabitPriority"

        fun findPriorityById(id: Int): HabitPriority {
            values().forEach {
                if (it.id == id) return it
            }
            throw RuntimeException(UNKNOWN_ID_EXCEPTION)
        }
    }
}
