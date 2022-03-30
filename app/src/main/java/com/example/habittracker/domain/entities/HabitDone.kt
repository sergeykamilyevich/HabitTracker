package com.example.habittracker.domain.entities

data class HabitDone(
    val id: Int = UNDEFINED_ID,
    val habitId: Int,
    val date: Int
) {

    companion object {
        const val UNDEFINED_ID = 0
    }
}
