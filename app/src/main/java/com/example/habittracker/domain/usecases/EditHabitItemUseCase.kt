package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListRepository

class EditHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    suspend operator fun invoke(habitItem: HabitItem) {
        habitListRepository.edit(habitItem)
    }
}