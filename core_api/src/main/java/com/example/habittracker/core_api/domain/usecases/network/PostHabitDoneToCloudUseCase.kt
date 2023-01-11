package com.example.habittracker.core_api.domain.usecases.network

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.core_api.domain.repositories.CloudHabitRepository
import javax.inject.Inject

//@Singleton
class PostHabitDoneToCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(habitDone: HabitDone): Either<IoError, Unit> {
        return cloudHabitRepository.postHabitDone(habitDone)
    }
}