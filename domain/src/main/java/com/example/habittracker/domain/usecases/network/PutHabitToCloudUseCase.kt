package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutHabitToCloudUseCase @Inject constructor(
    private val networkHabitRepository: NetworkHabitRepository
    ) {

    suspend operator fun invoke(habit: Habit): String? {
        return networkHabitRepository.putHabit(habit)
    }
}