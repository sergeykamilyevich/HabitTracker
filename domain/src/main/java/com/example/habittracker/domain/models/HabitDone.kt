package com.example.habittracker.domain.models

data class HabitDone(
    val habitId: Int,
    val date: Int,
    val habitUid: String,
    val id: Int = UNDEFINED_ID
) {

    companion object {
        private const val UNDEFINED_ID = 0
    }
}
