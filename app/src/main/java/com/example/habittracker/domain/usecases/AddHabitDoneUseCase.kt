package com.example.habittracker.domain.usecases

import com.example.habittracker.data.HabitAlreadyExistsException
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitDone

class AddHabitDoneUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitDone: HabitDone) {
        return habitRepository.addHabitDone(habitDone)
    }
}