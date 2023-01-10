package com.example.habittracker.network_api.di.models

import com.google.gson.annotations.SerializedName

data class ErrorApiModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)