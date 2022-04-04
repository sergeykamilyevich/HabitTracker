package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitDone

class AddHabitDoneUseCase(private val habitRepository: HabitRepository) { //TODO add dispatcher

    suspend operator fun invoke(habitDone: HabitDone): Int {
        return habitRepository.addHabitDone(habitDone)
    }
}