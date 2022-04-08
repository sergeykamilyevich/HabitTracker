package com.example.habittracker.data.retrofit.api_models

import com.google.gson.annotations.SerializedName

data class HabitItemApiModel(
    val color: Int,
    val count: Int,
    val date: Int,
    val description: String,

    @SerializedName("done_dates")
    val doneDates: List<Int>,

    val frequency: Int,
    val priority: Int, //TODO Enum?
    val title: String,
    val type: Int,
    val uid: String
)