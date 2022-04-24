package com.example.habittracker.data.repositories

import android.util.Log
import com.example.habittracker.data.network.retrofit.HabitApi
import com.example.habittracker.data.network.models.HabitApiModel
import com.example.habittracker.data.network.models.HabitDoneApiModel
import com.example.habittracker.data.network.models.HabitUidApiModel
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.repositories.CloudHabitRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudHabitRepositoryImpl @Inject constructor(
    private val apiService: HabitApi
) : CloudHabitRepository {

    override suspend fun getHabitList(): Either<CloudResponseError, List<Habit>> {
        val response: Response<List<HabitApiModel>> = apiService.getHabitList()
        return if (response.isSuccessful) {
            val responseBody = response.body()
                ?: return CloudResponseError(message = "Unknown server error").failure()
            responseBody.map {
                it.toHabit()
            }.success()
        } else {
            val code = response.code()
            val message = response.message()
            CloudResponseError(code, message).failure()
        }
    }

    override suspend fun getHabitWithDoneList(): Either<CloudResponseError, List<HabitWithDone>> {
        val response: Response<List<HabitApiModel>> = apiService.getHabitList()
        return if (response.isSuccessful) {
            val responseBody = response.body()
                ?: return CloudResponseError(message = "Unknown server error").failure()
            responseBody.map {
                it.toHabitWithDone()
            }.success()
        } else {
            val code = response.code()
            val message = response.message()
            CloudResponseError(code, message).failure()
        }
    }

//        response?.let {
//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                Log.d("OkHttp", "${response.body()}")
//                responseBody?.let {
//                    return responseBody.map {
//                        it.toHabit()
//                    }
//                }
//            } else {
//                val rb = response.errorBody()
//                Log.d("OkHttp", "${rb}")
//            }
//
//        }
//        return null
//    }

    override suspend fun putHabit(habit: Habit): Either<CloudResponseError, String> {
        Log.d("OkHttp", "putHabit start")
        val habitItemApiModel = HabitApiModel.fromHabitItem(habit)
        val jsonRequestBody = Gson().toJson(habitItemApiModel)
        val requestBody = jsonRequestBody
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response: Response<HabitUidApiModel> = apiService.putHabit(habitItemApiModel = requestBody)
        return if (response.isSuccessful) {
            val responseBody = response.body()
                ?: return CloudResponseError(message = "Unknown server error").failure()
                responseBody.uid.success()
        } else {
            val code = response.code()
            val message = response.message()
            CloudResponseError(code, message).failure()
        }
    }

    override suspend fun deleteHabit(habit: Habit): Either<CloudResponseError, Unit> {
        val habitUidApiModel = HabitUidApiModel(uid = habit.uid)
        val jsonRequestBody = Gson().toJson(habitUidApiModel)
        val requestBody = jsonRequestBody
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response: Response<Unit> = apiService.deleteHabit(habitUidApiModel = requestBody)
        return if (response.isSuccessful) {
            Unit.success()
        } else {
            val code = response.code()
            val message = response.message()
            CloudResponseError(code, message).failure()
        }
    }

    override suspend fun postHabitDone(habitDone: HabitDone): Either<CloudResponseError, Unit> { //TODO change String to Either
        val habitDoneApiModel = HabitDoneApiModel.fromHabitDone(habitDone)
        val jsonRequestBody = Gson().toJson(habitDoneApiModel)
        val requestBody = jsonRequestBody
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response: Response<Unit> = apiService.postHabitDone(habitDoneApiModel = requestBody)
        return if (response.isSuccessful) {
             Unit.success()
        } else {
            val code = response.code()
            val message = response.message()
            CloudResponseError(code, message).failure()
        }
    }

    override suspend fun deleteAllHabits() {
        val habitList = getHabitList()
        if (habitList is Either.Success) {
            habitList.result.forEach {
                deleteHabit(it)
            }
        }
    }
}