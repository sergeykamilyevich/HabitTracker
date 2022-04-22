package com.example.habittracker.data.network.models

import com.example.habittracker.domain.models.HabitDone
import com.google.gson.annotations.SerializedName

data class HabitDoneApiModel(
    @SerializedName("date")
    val date: Int,
    @SerializedName("habit_uid")
    val habitUid: String
) {
    companion object {
        fun fromHabitDone(habitDone: HabitDone) = HabitDoneApiModel(
            date = habitDone.date,
            habitUid = habitDone.habitUid
        )
    }
}