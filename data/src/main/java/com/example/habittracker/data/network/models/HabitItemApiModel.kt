package com.example.habittracker.data.network.models

import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.Time
import com.example.habittracker.domain.models.HabitType
import com.google.gson.annotations.SerializedName

data class HabitItemApiModel(
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
    fun toHabitItem(): HabitItem {
        val currentDate = Time().getCurrentUtcDateInInt()
        val upToDateHabitDoneDates = doneDates.filter {
            recurrencePeriod > (currentDate - it)
        }
        return HabitItem(
            name = name,
            description = description,
            priority = HabitPriority.getPriorityById(priority),
            type = HabitType.getTypeById(type),
            color = color,
            recurrenceNumber = recurrenceNumber,
            recurrencePeriod = recurrencePeriod,
            done = upToDateHabitDoneDates.size,
            apiUid = apiUid,
            date = date
        )
    }

    companion object {
        fun fromHabitItem(habitItem: HabitItem) = HabitItemApiModel(
            name = habitItem.name,
            description = habitItem.description,
            priority = habitItem.priority.int,
            type = habitItem.type.int,
            color = habitItem.color,
            recurrenceNumber = habitItem.recurrenceNumber,
            recurrencePeriod = habitItem.recurrencePeriod,
            date = habitItem.date,
            apiUid = habitItem.apiUid
        )
    }
}