package com.example.habittracker.core_api.domain.usecases.common

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.errors.failure
import com.example.habittracker.core_api.domain.errors.success
import com.example.habittracker.core_api.domain.usecases.db.DbUseCase
import com.example.habittracker.core_api.domain.usecases.network.CloudUseCase
import javax.inject.Inject

class SyncAllToCloudUseCase @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
    private val uploadAllToCloudUseCase: UploadAllToCloudUseCase
) {

    suspend operator fun invoke(): Either<IoError, Unit> {
        val habitList = dbUseCase.getUnfilteredHabitListUseCase.invoke()
        return when (habitList) {
            is Success -> {
                cloudUseCase.deleteAllHabitsFromCloudUseCase.invoke()
                val uploadResult = uploadAllToCloudUseCase.invoke(habitList.result)
                when (uploadResult) {
                    is Success -> uploadResult.result.success()
                    is Failure -> uploadResult.error.failure()
                }
            }
            is Failure -> habitList.error.failure()
        }
    }
}