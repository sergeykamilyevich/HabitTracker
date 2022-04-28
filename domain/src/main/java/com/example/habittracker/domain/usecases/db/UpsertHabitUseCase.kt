package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertHabitUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
) {

    suspend operator fun invoke(habit: Habit): Either<IoError, Int> =
        dbHabitRepository.upsertHabit(habit)
}