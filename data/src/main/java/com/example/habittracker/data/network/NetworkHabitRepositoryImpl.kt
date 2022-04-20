package com.example.habittracker.data.network

import android.util.Log
import com.example.habittracker.data.network.models.HabitItemApiModel
import com.example.habittracker.data.network.models.HabitUidApiModel
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.models.HabitItem
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
    override suspend fun getHabitList(): List<HabitItem>? {
        val response: Response<List<HabitItemApiModel>>? = try {
            apiService.getHabitList()
        } catch (e: Exception) {
            Log.d("99999", "error $e")
            return null
        }
//        Log.d("OkHttp", "response code ${response?.code()}")
//        Log.d("OkHttp", "response errorBody ${response?.errorBody()}")
//        Log.d("OkHttp", "response body ${response?.body()}")
        response?.let {
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("99999", "${response.body()}")
                responseBody?.let {
                    return responseBody.map {
                        it.toHabitItem()
                    }
                }
            } else {
                val rb = response.errorBody()
                Log.d("OkHttp", "${rb}")
            }

        }
        return null
    }

    override suspend fun putHabit(habitItem: HabitItem): String? {
//        Log.d("OkHttp", "putHabit start")
        val habitItemApiModel = HabitItemApiModel.fromHabitItem(habitItem)
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
                Log.d("99999", "${response.body()}")
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

    override suspend fun deleteHabit(habitItem: HabitItem) {
        TODO("Not yet implemented")
    }

    override suspend fun postHabitDone(habitDone: HabitDone) {
        TODO("Not yet implemented")
    }
}