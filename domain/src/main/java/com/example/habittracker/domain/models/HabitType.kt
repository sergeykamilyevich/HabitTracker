package com.example.habittracker.domain.models

enum class HabitType(val id: Int) {
    BAD(0),
    GOOD(1);

    companion object {
        fun findTypeById(id: Int): HabitType {
            values().forEach {
                if (it.id == id) return it
            }
            throw RuntimeException("Unknown id of HabitType")
        }

    }
}


