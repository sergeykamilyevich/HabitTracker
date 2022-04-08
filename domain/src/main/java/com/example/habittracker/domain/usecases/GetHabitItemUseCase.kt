package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.HabitRepository
import com.example.habittracker.domain.entities.HabitItem

class GetHabitItemUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItemId: Int): HabitItem {
        return habitRepository.getHabitById(habitItemId)
    }
}