package com.example.habittracker.feature_habits.domain.usecases.network

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.repositories.CloudHabitRepository
import javax.inject.Inject

class DeleteAllHabitsFromCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, Unit> = cloudHabitRepository.deleteAllHabits()
}