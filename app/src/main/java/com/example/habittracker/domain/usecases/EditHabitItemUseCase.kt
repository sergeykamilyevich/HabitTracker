package com.example.habittracker.domain.usecases

import com.example.habittracker.data.room.HabitAlreadyExistsException
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.HabitListRepository

class EditHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    suspend operator fun invoke(habitItem: HabitItem): HabitAlreadyExistsException? {
        return habitListRepository.edit(habitItem)
    }
}