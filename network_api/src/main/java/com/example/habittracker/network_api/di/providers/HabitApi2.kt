package com.example.habittracker.network_api.di.providers

import com.example.habittracker.network_api.di.models.HabitApiModel2
import com.example.habittracker.network_api.di.models.HabitUidApiModel2
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface HabitApi2 {

    @GET("api/habit")
    @Headers("content-type: application/json")
    suspend fun getHabitList(
    ): Response<List<HabitApiModel2>>

    @PUT("api/habit")
    @Headers("content-type: application/json")
    suspend fun putHabit(
        @Body habitItemApiModel: RequestBody
    ): Response<HabitUidApiModel2>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    @Headers("content-type: application/json")
    suspend fun deleteHabit(
        @Body habitUidApiModel: RequestBody
    ): Response<Unit>

    @POST("api/habit_done")
    @Headers("content-type: application/json")
    suspend fun postHabitDone(
        @Body habitDoneApiModel: RequestBody
    ): Response<Unit>

}