package com.example.habittracker.network_impl.repositories

import com.example.habittracker.core.domain.errors.Either.Failure
import com.example.habittracker.core.domain.errors.Either.Success
import com.example.habittracker.core.domain.errors.IoError.CloudError
import com.example.habittracker.core.domain.errors.IoError.DeletingAllHabitsError
import com.example.habittracker.core.domain.errors.failure
import com.example.habittracker.core.domain.errors.success
import com.example.habittracker.network_api.di.models.HabitApiModel
import com.example.habittracker.network_api.di.models.HabitDoneApiModel
import com.example.habittracker.network_api.di.models.HabitUidApiModel
import com.example.habittracker.network_api.di.providers.HabitApi
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

//@Singleton
class CloudHabitRepositoryImpl @Inject constructor(
    private val apiService: HabitApi
) : com.example.habittracker.core.domain.repositories.CloudHabitRepository {

    override suspend fun getHabitList(): com.example.habittracker.core.domain.errors.Either<com.example.habittracker.core.domain.errors.IoError, List<com.example.habittracker.core.domain.models.Habit>> {
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

    override suspend fun putHabit(habit: com.example.habittracker.core.domain.models.Habit): com.example.habittracker.core.domain.errors.Either<com.example.habittracker.core.domain.errors.IoError, String> {
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

    override suspend fun deleteHabit(habit: com.example.habittracker.core.domain.models.Habit): com.example.habittracker.core.domain.errors.Either<CloudError, Unit> {
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

    override suspend fun postHabitDone(habitDone: com.example.habittracker.core.domain.models.HabitDone): com.example.habittracker.core.domain.errors.Either<CloudError, Unit> {
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

    override suspend fun deleteAllHabits(): com.example.habittracker.core.domain.errors.Either<com.example.habittracker.core.domain.errors.IoError, Unit> {
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