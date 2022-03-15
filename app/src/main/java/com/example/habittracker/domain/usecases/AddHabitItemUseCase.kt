package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListRepository

class AddHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    suspend operator fun invoke(habitItem: HabitItem) {
        habitListRepository.add(habitItem)
    }
}