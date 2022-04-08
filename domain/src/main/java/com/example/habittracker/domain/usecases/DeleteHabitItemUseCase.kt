package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitItem

class DeleteHabitItemUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem) {
        habitRepository.deleteHabitItem(habitItem)
    }
}