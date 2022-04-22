package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject

class DeleteHabitFromDbUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(habit: Habit) {
        dbHabitRepository.deleteHabit(habit)
    }
}