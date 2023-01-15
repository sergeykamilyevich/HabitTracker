package com.example.habittracker.cloud_sync.domain.usecases.impls

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.cloud_sync.domain.repositories.SyncHabitRepository
import com.example.habittracker.cloud_sync.domain.usecases.interfaces.UploadAllToCloudUseCase
import javax.inject.Inject

class UploadAllToCloudUseCaseImpl @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) : UploadAllToCloudUseCase {

    override suspend operator fun invoke(habitList: List<Habit>): Either<IoError, Unit> =
        syncHabitRepository.uploadAllToCloud(habitList)
}