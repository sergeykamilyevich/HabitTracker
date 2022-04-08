package com.example.habittracker.domain.entities

data class HabitDone(
    val habitId: Int,
    val date: Int,
    val id: Int = UNDEFINED_ID
) {

    companion object {
        const val UNDEFINED_ID = 0
    }
}
