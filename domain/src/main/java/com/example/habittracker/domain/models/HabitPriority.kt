package com.example.habittracker.domain.models

enum class HabitPriority(val int: Int) {
    LOW(0),
    NORMAL(1),
    HIGH(2);

    companion object {
        fun getPriorityByPosition(position: Int) = values()[position]

        fun getPriorityById(position: Int) = values()[position] //TODO fix to id
    }
}
