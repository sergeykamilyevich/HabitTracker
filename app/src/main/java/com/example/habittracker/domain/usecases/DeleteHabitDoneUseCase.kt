package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitDone

class DeleteHabitDoneUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitDone: HabitDone) {
        habitRepository.deleteHabitDone(habitDone)
    }
}