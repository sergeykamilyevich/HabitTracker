package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.NetworkHabitRepository
import javax.inject.Inject

class DeleteHabitFromCloudUseCase @Inject constructor( //TODO rename Api to Cloud
    private val networkHabitRepository: NetworkHabitRepository
    ) {

    suspend operator fun invoke(habit: Habit): String? {
        return networkHabitRepository.deleteHabit(habit)
    }
}