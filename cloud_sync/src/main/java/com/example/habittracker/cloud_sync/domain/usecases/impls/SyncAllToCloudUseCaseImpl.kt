package com.example.habittracker.cloud_sync.domain.usecases.impls

import com.example.habittracker.cloud_sync.domain.usecases.interfaces.SyncAllToCloudUseCase
import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.errors.failure
import com.example.habittracker.core_api.domain.errors.success
import com.example.habittracker.network_api.domain.usecases.CloudUseCase
import com.example.habittracker.db_api.domain.usecases.DbUseCase
import javax.inject.Inject

class SyncAllToCloudUseCaseImpl @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
    private val uploadAllToCloudUseCase: UploadAllToCloudUseCaseImpl
) : SyncAllToCloudUseCase {

    override suspend operator fun invoke(): Either<IoError, Unit> {
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