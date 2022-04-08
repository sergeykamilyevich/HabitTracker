package com.example.habittracker.domain.models

enum class HabitPriority(val id: Int) {
    LOW(0),
    NORMAL(1),
    HIGH(2);

    companion object {
        fun getPriorityByPosition(position: Int) = values()[position]
    }
}
