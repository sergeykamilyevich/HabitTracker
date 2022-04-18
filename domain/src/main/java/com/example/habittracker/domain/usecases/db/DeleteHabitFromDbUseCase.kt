package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.repositories.HabitRepository
import com.example.habittracker.domain.models.HabitItem
import javax.inject.Inject
import javax.inject.Singleton

class DeleteHabitFromDbUseCase @Inject constructor(private val habitRepository: HabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem) {
        habitRepository.deleteHabitItem(habitItem)
    }
}