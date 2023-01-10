package com.example.habittracker.feature_habits.domain.usecases.network

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.models.HabitDone
import com.example.habittracker.core.domain.repositories.CloudHabitRepository
import javax.inject.Inject

//@Singleton
class PostHabitDoneToCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(habitDone: HabitDone): Either<IoError, Unit> {
        return cloudHabitRepository.postHabitDone(habitDone)
    }
}