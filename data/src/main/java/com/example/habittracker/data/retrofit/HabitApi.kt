package com.example.habittracker.data.retrofit

import com.example.habittracker.data.retrofit.models.HabitItemApiModel
import com.example.habittracker.data.retrofit.models.HabitUidApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT

interface HabitApi {

    @GET("/api/habit")
    @Headers("content-type: application/json")
    suspend fun getHabitList(
        @Header("Authorization") authorization: String = API_TOKEN
    ): Response<List<HabitItemApiModel>>

    @PUT("/api/habit")
    @Headers("content-type: application/json")
    suspend fun putHabit(
        @Header("Authorization") authorization: String = API_TOKEN
    ): Response<HabitUidApiModel>

    companion object {
        private const val API_TOKEN = "05b550ee-1713-43f1-a842-9815d354460d"
    }
}