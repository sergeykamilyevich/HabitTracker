package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.CloudHabitRepository
import javax.inject.Inject

//@Singleton
class GetHabitListFromCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, List<Habit>> {
        return cloudHabitRepository.getHabitList()
    }
}