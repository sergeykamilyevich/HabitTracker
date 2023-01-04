package com.example.habittracker.network_api.di.models

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitType
import com.google.gson.annotations.SerializedName

data class HabitApiModel2(
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
    val priority: Int,
    @SerializedName("title")
    val name: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("uid")
    val apiUid: String
) {
    fun toHabit(): Habit {
        return Habit(
            name = name,
            description = description,
            priority = HabitPriority.findPriorityById(priority),
            type = HabitType.findTypeById(type),
            color = color,
            recurrenceNumber = recurrenceNumber,
            recurrencePeriod = recurrencePeriod,
            done = doneDates,
            uid = apiUid,
            date = date
        )
    }

    companion object {

        fun fromHabitItem(habit: Habit) = HabitApiModel2(
            name = habit.name,
            description = habit.description,
            priority = habit.priority.id,
            type = habit.type.id,
            color = habit.color,
            recurrenceNumber = habit.recurrenceNumber,
            recurrencePeriod = habit.recurrencePeriod,
            date = habit.date,
            apiUid = habit.uid
        )
    }
}