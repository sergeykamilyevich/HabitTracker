package com.example.habittracker.data.network.retrofit

import com.example.habittracker.data.network.models.HabitApiModel
import com.example.habittracker.data.network.models.HabitUidApiModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface HabitApi {

    @GET("api/habit")
    @Headers("content-type: application/json")
    suspend fun getHabitList(
    ): Response<List<HabitApiModel>>

    @PUT("api/habit")
    @Headers("content-type: application/json")
    suspend fun putHabit(
        @Body habitItemApiModel: RequestBody
    ): Response<HabitUidApiModel>

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