package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject

class DeleteAllHabitsFromDbUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke() {
        dbHabitRepository.deleteAllHabits()
    }
}