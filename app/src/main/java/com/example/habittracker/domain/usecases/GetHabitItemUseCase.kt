package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.HabitListRepository

class GetHabitItemUseCase(private val habitListRepository: HabitListRepository) {

    suspend operator fun invoke(habitItemId: Int): HabitItem {
        return habitListRepository.getById(habitItemId)
    }
}