package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.CloudHabitRepository
import javax.inject.Inject

class DeleteHabitFromCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
) {

    suspend operator fun invoke(habit: Habit): Either<CloudError, Unit> {
        return cloudHabitRepository.deleteHabit(habit)
    }
}