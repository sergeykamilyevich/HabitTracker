package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.HabitListRepository

class DeleteHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    suspend operator fun invoke(habitItem: HabitItem) {
        habitListRepository.delete(habitItem)
    }
}