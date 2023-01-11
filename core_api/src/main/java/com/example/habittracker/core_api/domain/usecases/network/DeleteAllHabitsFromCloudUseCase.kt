package com.example.habittracker.core_api.domain.usecases.network

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.repositories.CloudHabitRepository
import javax.inject.Inject

class DeleteAllHabitsFromCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, Unit> = cloudHabitRepository.deleteAllHabits()
}