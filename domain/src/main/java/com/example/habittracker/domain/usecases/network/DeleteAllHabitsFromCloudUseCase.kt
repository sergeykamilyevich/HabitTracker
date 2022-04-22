package com.example.habittracker.domain.usecases.network

import com.example.habittracker.domain.repositories.NetworkHabitRepository
import javax.inject.Inject

class DeleteAllHabitsFromCloudUseCase @Inject constructor( //TODO rename Api to Cloud
    private val networkHabitRepository: NetworkHabitRepository
    ) {

    suspend operator fun invoke() {
        return networkHabitRepository.deleteAllHabits()
    }
}