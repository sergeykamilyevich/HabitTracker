package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.repositories.CloudHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostHabitDoneToCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(habitDone: HabitDone): Either<IoError, Unit> {
        return cloudHabitRepository.postHabitDone(habitDone)
    }
}