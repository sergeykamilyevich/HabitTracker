package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.repositories.CloudHabitRepository
import javax.inject.Inject

class DeleteAllHabitsFromCloudUseCase @Inject constructor(
    private val cloudHabitRepository: CloudHabitRepository
    ) {

    suspend operator fun invoke() {
        return cloudHabitRepository.deleteAllHabits()
    }
}