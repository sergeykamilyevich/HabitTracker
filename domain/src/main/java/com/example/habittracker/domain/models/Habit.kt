package com.example.habittracker.domain.models

data class Habit(
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var color: Int,
    var recurrenceNumber: Int,
    var recurrencePeriod: Int,
    var done: Int = UNDONE,
    var uid: String = EMPTY_UID, //TODO val or var?
    var date: Int = UNDEFINED_DATE,
    val id: Int = UNDEFINED_ID
) {

    fun clearUid(): Habit = copy(uid = EMPTY_UID)

    companion object {
        const val UNDEFINED_ID = 0
        const val UNDEFINED_DATE = 0
        const val UNDONE = 0
        const val EMPTY_UID = ""
    }
}
