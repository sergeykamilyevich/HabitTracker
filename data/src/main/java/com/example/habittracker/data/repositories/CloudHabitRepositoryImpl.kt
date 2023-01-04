package com.example.habittracker.data.repositories

import com.example.habittracker.data.network.models.HabitApiModel
import com.example.habittracker.data.network.models.HabitDoneApiModel
import com.example.habittracker.data.network.models.HabitUidApiModel
import com.example.habittracker.data.network.retrofit.HabitApi
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.IoError.CloudError
import com.example.habittracker.domain.errors.IoError.DeletingAllHabitsError
import com.example.habittracker.domain.errors.failure
import com.example.habittracker.domain.errors.success
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.repositories.CloudHabitRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

//@Singleton
class CloudHabitRepositoryImpl @Inject constructor(
    private val apiService: HabitApi
) : CloudHabitRepository {

    override suspend fun getHabitList(): Either<IoError, List<Habit>> {
        val response: Response<List<HabitApiModel>> = apiService.getHabitList()
        return if (response.isSuccessful) {
            val responseBody = response.body()
                ?: return CloudError(message = "Unknown server error").failure()
            responseBody.map {
                it.toHabit()
            }.success()
        } else {
            val code = response.code()
            val message = response.message()
            CloudError(code, message).failure()
        }
    }

    override suspend fun putHabit(habit: Habit): Either<IoError, String> {
        val habitItemApiModel = HabitApiModel.fromHabitItem(habit)
        val jsonRequestBody = Gson().toJson(habitItemApiModel)
        val requestBody = jsonRequestBody
            .toRequestBody("application/json".toMediaTypeOrNull())
        val response: Response<HabitUidApiModel> =
            apiService.putHabit(habitItemApiModel = requestBody)
        return if (response.isSuccessful) {
            val responseBody = response.body()
                ?: return CloudError(message = UNKNOWN_SERVER_ERROR).failure()
            responseBody.uid.success()
        } else {
            val code = response.code()
            val message = response.message()
            CloudError(code, message).failure()
        }
    }

    override suspend fun deleteHabit(habit: Habit): Either<CloudError, Unit> {
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
            CloudError(code, message).failure()
        }
    }

    override suspend fun postHabitDone(habitDone: HabitDone): Either<CloudError, Unit> {
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
            CloudError(code, message).failure()
        }
    }

    override suspend fun deleteAllHabits(): Either<IoError, Unit> {
        when (val habitListBefore = getHabitList()) {
            is Success -> {
                habitListBefore.result.forEach {
                    deleteHabit(it)
                }
            }
            is Failure -> return habitListBefore.error.failure()
        }
        return when (val habitListAfter = getHabitList()) {
            is Success -> {
                if (habitListAfter.result.isEmpty()) Unit.success()
                else DeletingAllHabitsError().failure()
            }
            is Failure -> habitListAfter.error.failure()
        }

    }

    companion object {
        private const val UNKNOWN_SERVER_ERROR = "Unknown server error"
    }
}