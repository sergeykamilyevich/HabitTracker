package com.example.habittracker.domain.models

data class HabitItem(
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var color: Int,
    var recurrenceNumber: Int,
    var recurrencePeriod: Int,
    val id: Int = UNDEFINED_ID,
    val date: Int = UNDEFINED_DATE,
    val doneDates: List<Int> = listOf()
) {

    companion object {
        const val UNDEFINED_ID = 0
        const val UNDEFINED_DATE = 0
    }
}