package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.SyncHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadAllToCloudUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) {

    suspend operator fun invoke(habitList: List<Habit>): Either<CloudError, Unit> =
        syncHabitRepository.uploadAllToCloud(habitList)
}