package com.example.habittracker.data.repositories

import android.util.Log
import com.example.habittracker.data.network.HabitApi
import com.example.habittracker.data.network.models.HabitApiModel
import com.example.habittracker.data.network.models.HabitDoneApiModel
import com.example.habittracker.data.network.models.HabitUidApiModel
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHabitRepositoryImpl @Inject constructor(
    private val apiService: HabitApi
) : NetworkHabitRepository {
    override suspend fun getHabitList(): List<Habit>? {
        Log.d("OkHttp", "getHabitList start")
        val response: Response<List<HabitApiModel>>? = try {
            Log.d("OkHttp", "response start")
            apiService.getHabitList()
        } catch (e: Exception) {
            Log.d("OkHttp", "error $e")
            return null
        }
        Log.d("OkHttp", "response code ${response?.code()}")
        Log.d("OkHttp", "response errorBody ${response?.errorBody()}")
        Log.d("OkHttp", "response body ${response?.body()}")
        response?.let {
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("OkHttp", "${response.body()}")
                responseBody?.let {
                    return responseBody.map {
                        it.toHabit()
                    }
                }
            } else {
                val rb = response.errorBody()
                Log.d("OkHttp", "${rb}")
            }

        }
        return null
    }

    override suspend fun putHabit(habit: Habit): String? {
        Log.d("OkHttp", "putHabit start")
        val habitItemApiModel = HabitApiModel.fromHabitItem(habit)
        val jsonRequestBody = Gson().toJson(habitItemApiModel)
//        Log.d("OkHttp", "jsonObject = $jsonRequestBody")
        val requestBody = jsonRequestBody
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response: Response<HabitUidApiModel>? = try {
            apiService.putHabit(habitItemApiModel = requestBody)
        } catch (e: Exception) {
//            Log.d("OkHttp", "error $e")
            null
        }
        Log.d("OkHttp", "response putHabit $response")
        response?.let {
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("OkHttp", "${response.body()}")
                responseBody?.let {
                    return responseBody.uid
                }
            } else {
                val rb = response.errorBody()
                Log.d("OkHttp", "${rb}")
            }

        }
        return null
    }

    override suspend fun deleteHabit(habit: Habit): String? {
        Log.d("OkHttp", "deleteHabit start")
        val habitUidApiModel = HabitUidApiModel(uid = habit.uid)
        val jsonRequestBody = Gson().toJson(habitUidApiModel)
        val requestBody = jsonRequestBody
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response: Response<Unit>? = try {
            apiService.deleteHabit(habitUidApiModel = requestBody)
        } catch (e: Exception) {
            Log.d("OkHttp", "error $e")
            null
        }
        Log.d("OkHttp", "response deleteHabit $response")
        response?.let {
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("OkHttp", "responseBody ${response.body()}")
                responseBody?.let {
                    return ""
                }
            } else {
                val rb = response.errorBody()
                Log.d("OkHttp", "response.errorBody ${rb}")
            }
        }
        return null
    }

    override suspend fun postHabitDone(habitDone: HabitDone): String? { //TODO change String to Either
        Log.d("OkHttp", "postHabitDone start")
        val habitDoneApiModel = HabitDoneApiModel.fromHabitDone(habitDone)
        val jsonRequestBody = Gson().toJson(habitDoneApiModel)
        Log.d("OkHttp", "jsonObject = $jsonRequestBody")
        val requestBody = jsonRequestBody
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response: Response<Unit>? = try {
            apiService.postHabitDone(habitDoneApiModel = requestBody)
        } catch (e: Exception) {
            Log.d("OkHttp", "error $e")
            null
        }
        Log.d("OkHttp", "response postHabitDone $response")
        response?.let {
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("OkHttp", "${response.body()}")
                responseBody?.let {
                    return ""
                }
            } else {
                val rb = response.errorBody()
                Log.d("OkHttp", "${rb}")
            }

        }
        return null
    }

    override suspend fun deleteAllHabits() {
        val habitList = getHabitList()
        habitList?.forEach {
            deleteHabit(it)
        }
    }
}