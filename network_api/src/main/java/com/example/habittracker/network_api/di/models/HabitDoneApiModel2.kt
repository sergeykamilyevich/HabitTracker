package com.example.habittracker.network_api.di.models

import com.example.habittracker.domain.models.HabitDone
import com.google.gson.annotations.SerializedName

data class HabitDoneApiModel2(
    @SerializedName("date")
    val date: Int,
    @SerializedName("habit_uid")
    val habitUid: String
) {
    companion object {
        fun fromHabitDone(habitDone: HabitDone) = HabitDoneApiModel2(
            date = habitDone.date,
            habitUid = habitDone.habitUid
        )
    }
}