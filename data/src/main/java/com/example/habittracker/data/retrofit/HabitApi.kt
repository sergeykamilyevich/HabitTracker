package com.example.habittracker.data.retrofit

import com.example.habittracker.data.retrofit.api_models.HabitItemApiModel
import com.example.habittracker.data.retrofit.api_models.HabitUidApiModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT

interface HabitApi {

    @GET("/api/habit")
    @Headers("content-type: application/json")
    suspend fun getHabitList(@Header("Authorization") authorization: String = API_TOKEN): Response<List<HabitItemApiModel>>

    @PUT("/api/habit")
    @Headers("content-type: application/json")
    suspend fun putHabit(@Header("Authorization") authorization: String = API_TOKEN): Response<HabitUidApiModel>

    companion object {
        private const val API_TOKEN = "05b550ee-1713-43f1-a842-9815d354460d"
//        private const val BASE_URL = "https://droid-test-server.doubletapp.ru"
//        private var INSTANCE: HabitApi? = null
//        private val LOCK = Any()

//        fun getInstance(): HabitApi {
//            INSTANCE?.let {
//                return it
//            }
//            synchronized(LOCK) {
//                INSTANCE?.let {
//                    return it
//                }
//                val interceptor = HttpLoggingInterceptor()
//                interceptor.level = HttpLoggingInterceptor.Level.BODY
//                val client = OkHttpClient.Builder()
//                    .addInterceptor(interceptor)
//                    .build()
//                val retrofit = Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(client)
//                    .build()
//                    .create(HabitApi::class.java)
//                INSTANCE = retrofit
//                return retrofit
//            }
//        }
    }
}