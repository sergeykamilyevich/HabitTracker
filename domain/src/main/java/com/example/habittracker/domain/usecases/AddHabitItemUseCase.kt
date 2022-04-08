package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.HabitAlreadyExistsException
import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitItem

class AddHabitItemUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem): HabitAlreadyExistsException? {
        return habitRepository.addHabitItem(habitItem)
    }
}