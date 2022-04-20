package com.example.habittracker.data.network

import com.example.habittracker.data.network.models.HabitItemApiModel
import com.example.habittracker.data.network.models.HabitUidApiModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface HabitApi {

    @GET("api/habit")
    @Headers("content-type: application/json")
    suspend fun getHabitList(
        @Header("Authorization") authorization: String = API_TOKEN
    ): Response<List<HabitItemApiModel>>

    @PUT("api/habit")
    @Headers("content-type: application/json")
    suspend fun putHabit(
        @Header("Authorization") authorization: String = API_TOKEN, //TODO to interceptor
        @Body habitItemApiModel: RequestBody
    ): Response<HabitUidApiModel>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    @Headers("content-type: application/json")
    suspend fun deleteHabit(
        @Header("Authorization") authorization: String = API_TOKEN,
        @Body habitUidApiModel: RequestBody
    ): Response<String>
    companion object {
        private const val API_TOKEN = "05b550ee-1713-43f1-a842-9815d354460d"
    }
}