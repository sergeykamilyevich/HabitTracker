package com.example.habittracker.domain.usecases

import com.example.habittracker.data.room.HabitAlreadyExistsException
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.HabitRepository

class EditHabitItemUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem): HabitAlreadyExistsException? {
        return habitRepository.edit(habitItem)
    }
}