package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.repositories.HabitRepository
import com.example.habittracker.domain.models.HabitItem

class DeleteHabitItemUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem) {
        habitRepository.deleteHabitItem(habitItem)
    }
}