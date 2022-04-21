package com.example.habittracker.data.network.models

import com.google.gson.annotations.SerializedName

data class ErrorApiModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)