package com.example.habittracker.data.network.models

import com.example.habittracker.data.db.models.HabitDoneDbModel
import com.example.habittracker.domain.models.*
import com.google.gson.annotations.SerializedName

data class HabitApiModel(
    @SerializedName("color")
    val color: Int,
    @SerializedName("count")
    val recurrenceNumber: Int,
    @SerializedName("date")
    val date: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("done_dates")
    val doneDates: List<Int> = listOf(),
    @SerializedName("frequency")
    val recurrencePeriod: Int,
    @SerializedName("priority")
    val priority: Int, //TODO Enum?
    @SerializedName("title")
    val name: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("uid")
    val apiUid: String
) {
    fun toHabit(): Habit {
//        val currentDate = Time().currentUtcDateInSeconds()
//        val upToDateHabitDoneDates = doneDates.filter {
//            recurrencePeriod > (currentDate - it)
//        }
        return Habit(
            name = name,
            description = description,
            priority = HabitPriority.getPriorityById(priority),
            type = HabitType.getTypeById(type),
            color = color,
            recurrenceNumber = recurrenceNumber,
            recurrencePeriod = recurrencePeriod,
            done = doneDates,
            uid = apiUid,
            date = date
        )
    }

    fun toHabitWithDone(): HabitWithDone {
        val habit = Habit(
            name = name,
            description = description,
            priority = HabitPriority.getPriorityById(priority),
            type = HabitType.getTypeById(type),
            color = color,
            recurrenceNumber = recurrenceNumber,
            recurrencePeriod = recurrencePeriod,
            uid = apiUid,
            date = date
        )
        val habitDone = doneDates.map {
            HabitDone(
                habitId = UNDEFINED_ID,
                date = it,
                habitUid = apiUid
            )
        }
        return HabitWithDone(habit = habit, habitDone = habitDone)
    }

    companion object {

        private const val UNDEFINED_ID = 0

        fun fromHabitItem(habit: Habit) = HabitApiModel(
            name = habit.name,
            description = habit.description,
            priority = habit.priority.int,
            type = habit.type.int,
            color = habit.color,
            recurrenceNumber = habit.recurrenceNumber,
            recurrencePeriod = habit.recurrencePeriod,
            date = habit.date,
            apiUid = habit.uid
        )
    }
}