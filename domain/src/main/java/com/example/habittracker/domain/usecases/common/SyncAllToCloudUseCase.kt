package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.success
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import javax.inject.Inject

class SyncAllToCloudUseCase @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
    private val uploadAllToCloudUseCase: UploadAllToCloudUseCase
) {

    suspend operator fun invoke(): Either<CloudError, Unit> {
        val habitList = dbUseCase.getUnfilteredHabitListFromDbUseCase.invoke()
        habitList?.let {
            cloudUseCase.deleteAllHabitsFromCloudUseCase()
            return uploadAllToCloudUseCase.invoke(habitList)
        }
        return Unit.success()
    }
}