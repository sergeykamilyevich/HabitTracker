package com.example.habittracker.feature_habits.domain.usecases.network

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.core.domain.repositories.CloudHabitRepository
import javax.inject.Inject

//@Singleton
class GetHabitListFromCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, List<Habit>> {
        return cloudHabitRepository.getHabitList()
    }
}