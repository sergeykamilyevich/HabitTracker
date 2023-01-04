package com.example.habittracker.data.network.retrofit

import com.example.habittracker.data.network.models.HabitApiModel
import com.example.habittracker.data.network.models.HabitUidApiModel
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class HabitApiFake : HabitApi {

    private val habitList = mutableListOf<HabitApiModel>()
    private val habitDoneList = mutableListOf<HabitUidApiModel>()
    private var indexHabits = 0

    private var errorReturn: Boolean = false

    fun setErrorReturn() {
        errorReturn = true
    }

    fun initFilling() = runBlocking {
        val habitsToInsert = mutableListOf<HabitApiModel>()
        ('a'..'z').forEachIndexed { index, c ->
            habitsToInsert.add(
                HabitApiModel(
                    name = c.toString(),
                    description = c.toString(),
                    priority = index % 3,
                    type = index % 2,
                    color = index,
                    recurrenceNumber = index,
                    recurrencePeriod = index * 2,
                    date = index,
                    apiUid = "uid $index"
                )
            )
        }
        habitsToInsert.shuffle()
        habitsToInsert.forEach {
            val jsonRequestBody = Gson().toJson(it)
            val requestBody = jsonRequestBody
                .toRequestBody("application/json".toMediaTypeOrNull())
            putHabit(requestBody)
        }
        val habitDoneListToInsert = mutableListOf<HabitUidApiModel>()
        (0..10).forEach {
            habitDoneListToInsert.add(HabitUidApiModel(uid = it.toString()))
        }
        habitDoneListToInsert.shuffle()
        habitDoneListToInsert.forEach {
            val jsonRequestBody = Gson().toJson(it)
            val requestBody = jsonRequestBody
                .toRequestBody("application/json".toMediaTypeOrNull())
            postHabitDone(requestBody)
        }
    }

    override suspend fun getHabitList(): Response<List<HabitApiModel>> =
        if (errorReturn) Response.error(0, "error".toResponseBody())
        else Response.success(habitList)

    override suspend fun putHabit(habitItemApiModel: RequestBody): Response<HabitUidApiModel> {
        if (errorReturn) return Response.error(0, "error".toResponseBody())
//        else {
//            val json = Gson().toJson(habitItemApiModel)
////            val client = OkHttpClient()
////            val gson = Gson()
////            val responseBody = client.newCall(habitItemApiModel).execute().body()
////            val entity: HabitApiModel =
////                gson.fromJson(responseBody.string(), HabitApiModel::class.java)
//            habitList.add(entity)
//            return Response.success(200)
        TODO("")
        // Response.success(habitList)
//        }
    }

    override suspend fun deleteHabit(habitUidApiModel: RequestBody): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postHabitDone(habitDoneApiModel: RequestBody): Response<Unit> {
        TODO("Not yet implemented")
    }
}

