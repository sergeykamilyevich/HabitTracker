package com.example.habittracker.domain.models

data class HabitDone(
    val habitId: Int,
    val date: Int,
    val habitUid: String = UNKNOWN_UID, //TODO maybe without default value?
    val id: Int = UNDEFINED_ID
) {

    companion object {
        const val UNDEFINED_ID = 0
        const val UNKNOWN_UID = ""
    }
}
