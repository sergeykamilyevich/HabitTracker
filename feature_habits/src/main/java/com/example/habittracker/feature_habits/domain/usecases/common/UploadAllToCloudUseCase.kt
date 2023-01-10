package com.example.habittracker.feature_habits.domain.usecases.common

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.core.domain.repositories.SyncHabitRepository
import javax.inject.Inject

//@Singleton
class UploadAllToCloudUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) {

    suspend operator fun invoke(habitList: List<Habit>): Either<IoError, Unit> =
        syncHabitRepository.uploadAllToCloud(habitList)
}