package com.example.habittracker.domain.models

enum class HabitType(val int: Int) {
    BAD(0),
    GOOD(1);

    companion object {
        fun getTypeById(position: Int) = values()[position] //TODO fix to id

    }
}


