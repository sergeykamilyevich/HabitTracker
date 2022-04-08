package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.repositories.HabitRepository

class DeleteHabitDoneUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitDoneId: Int) {
        habitRepository.deleteHabitDone(habitDoneId)
    }
}