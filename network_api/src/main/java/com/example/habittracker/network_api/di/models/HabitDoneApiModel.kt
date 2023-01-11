package com.example.habittracker.network_api.di.models

import com.example.habittracker.core_api.domain.models.HabitDone
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