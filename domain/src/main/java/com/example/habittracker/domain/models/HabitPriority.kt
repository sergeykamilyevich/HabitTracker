package com.example.habittracker.domain.models

enum class HabitPriority(val id: Int) {
    LOW(0),
    NORMAL(1),
    HIGH(2);

    companion object {
        fun findPriorityById(id: Int): HabitPriority {
            values().forEach {
                if (it.id == id) return it
            }
            throw RuntimeException("Unknown id of HabitPriority")
        }
    }
}
