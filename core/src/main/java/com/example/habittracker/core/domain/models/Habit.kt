package com.example.habittracker.core.domain.models

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Habit
        return date == other.date
                && name == other.name
                && description == other.description
                && priority == other.priority
                && color == other.color
                && recurrenceNumber == other.recurrenceNumber
                && recurrencePeriod == other.recurrencePeriod
                && uid == other.uid
                && done == other.done
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + if (name != EMPTY_STRING) name.hashCode() else UNDEFINED_HASH
        result =
            31 * result + if (description != EMPTY_STRING) description.hashCode() else UNDEFINED_HASH
        result = 31 * result + priority.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + color
        result = 31 * result + recurrenceNumber
        result = 31 * result + recurrencePeriod
        result = 31 * result + done.hashCode()
        result = 31 * result + if (uid != EMPTY_STRING) uid.hashCode() else UNDEFINED_HASH
        result = 31 * result + date
        return result
    }

    companion object {
        const val UNDEFINED_ID = 0
        private const val UNDEFINED_DATE = 0
        private const val SECONDS_IN_DAY = 24 * 60 * 60
        const val EMPTY_UID = ""
        private const val EMPTY_STRING = ""
        private const val UNDEFINED_HASH = 0
    }
}
