package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.HabitRepository

class GetHabitItemUseCase(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItemId: Int): HabitItem {
        return habitRepository.getHabitById(habitItemId)
    }
}