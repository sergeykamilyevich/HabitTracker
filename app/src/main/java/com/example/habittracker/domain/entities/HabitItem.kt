package com.example.habittracker.domain.entities

data class HabitItem(
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var color: Int,
    var recurrenceNumber: Int,
    var recurrencePeriod: Int,
    val id: Int = UNDEFINED_ID,
    val date: Int,
//    val doneDates: MutableList<Int> = mutableListOf()
) {

    companion object {
        const val UNDEFINED_ID = 0
        const val UNDEFINED_DATE = 0
    }
}
