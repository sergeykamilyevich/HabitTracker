package com.example.habittracker.core_api.domain.usecases.common

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.repositories.SyncHabitRepository
import javax.inject.Inject

class UploadAllToCloudUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) {

    suspend operator fun invoke(habitList: List<Habit>): Either<IoError, Unit> =
        syncHabitRepository.uploadAllToCloud(habitList)
}