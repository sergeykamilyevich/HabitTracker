package com.example.habittracker.network_api.di.models

import com.google.gson.annotations.SerializedName

data class HabitUidApiModel(
    @SerializedName("uid")
    val uid: String
)