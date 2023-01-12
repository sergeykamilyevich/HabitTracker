package com.example.habittracker.core_api.domain.usecases.db

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import javax.inject.Inject

class UpsertHabitUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
) {

    suspend operator fun invoke(habit: Habit): Either<IoError, Int> =
        dbHabitRepository.upsertHabit(habit)
}