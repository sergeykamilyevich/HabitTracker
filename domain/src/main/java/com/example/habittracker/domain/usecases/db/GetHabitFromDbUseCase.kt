package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHabitFromDbUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(habitItemId: Int): Habit =
        dbHabitRepository.getHabitById(habitItemId)
}