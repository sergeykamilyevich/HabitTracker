package com.example.habittracker.domain

data class HabitItem(
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var color: Int,
    var recurrenceNumber: Int,
    var recurrencePeriod: Int,
    var id: Int = UNDEFINED_ID,
) {

    companion object {
        const val UNDEFINED_ID = 0
    }
}
