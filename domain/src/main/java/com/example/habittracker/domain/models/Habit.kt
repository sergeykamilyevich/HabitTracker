package com.example.habittracker.domain.models

data class Habit(
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var color: Int,
    var recurrenceNumber: Int,
    var recurrencePeriod: Int,
    var done: List<Int> = listOf(),
    val uid: String = EMPTY_UID,
    var date: Int = UNDEFINED_DATE,
    val id: Int = UNDEFINED_ID
) {

    fun clearUid(): Habit = copy(uid = EMPTY_UID)

    fun actualDoneListSize(): Int {
        val currentDate = Time().currentUtcDateInSeconds()
        val actualDoneList = done.filter {
            recurrencePeriod > (currentDate - it) / SECONDS_IN_DAY
        }
        return actualDoneList.size

    }

    companion object {
        const val UNDEFINED_ID = 0
        const val UNDEFINED_DATE = 0
        const val SECONDS_IN_DAY = 24 * 60 * 60
        const val EMPTY_UID = ""
    }
}
