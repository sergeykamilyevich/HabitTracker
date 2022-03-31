package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.HabitRepository

class DeleteHabitDoneUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitDoneId: Int) {
        habitRepository.deleteHabitDone(habitDoneId)
    }
}