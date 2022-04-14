package com.example.habittracker.domain.usecases

import com.example.habittracker.domain.repositories.HabitRepository
import com.example.habittracker.domain.models.HabitItem
import javax.inject.Inject

class GetHabitItemUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItemId: Int): HabitItem {
        return habitRepository.getHabitById(habitItemId)
    }
}