package com.example.habittracker.data.network.models

import com.google.gson.annotations.SerializedName

data class HabitUidApiModel(
    @SerializedName("uid")
    val uid: String
)