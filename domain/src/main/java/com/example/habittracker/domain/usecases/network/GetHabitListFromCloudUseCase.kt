package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.CloudResponseError
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.CloudHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHabitListFromCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(): Either<CloudResponseError, List<Habit>> {
        return cloudHabitRepository.getHabitList()
    }
}