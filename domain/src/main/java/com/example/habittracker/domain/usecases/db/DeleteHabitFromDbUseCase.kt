package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.repositories.DbHabitRepository
import com.example.habittracker.domain.models.HabitItem
import javax.inject.Inject

class DeleteHabitFromDbUseCase @Inject constructor(private val dbHabitRepository: DbHabitRepository) {

    suspend operator fun invoke(habitItem: HabitItem) {
        dbHabitRepository.deleteHabit(habitItem)
    }
}