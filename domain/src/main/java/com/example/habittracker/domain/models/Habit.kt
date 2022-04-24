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
    var uid: String = EMPTY_UID, //TODO val or var?
    var date: Int = UNDEFINED_DATE,
    val id: Int = UNDEFINED_ID
) {

    fun clearUid(): Habit = copy(uid = EMPTY_UID)

    fun actualDoneListSize(): Int {
        val currentDate = Time().currentUtcDateInSeconds()
        println("99999  currentDate ${currentDate}")
        println("99999  recurrencePeriod ${recurrencePeriod}")
        println("99999  done ${done}")

        val actualDoneList = done.filter {
            recurrencePeriod > (currentDate - it) / SECONDS_IN_DAY
        }
        println("99999  actualDoneList ${actualDoneList}")
        println("99999  actualDoneList.size ${actualDoneList.size}")

        return actualDoneList.size

    }

    companion object {
        const val UNDEFINED_ID = 0
        const val UNDEFINED_DATE = 0
        const val SECONDS_IN_DAY = 24 * 60 * 60
        //        const val UNDONE = 0
        const val EMPTY_UID = ""
    }
}
