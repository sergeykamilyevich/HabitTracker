package com.example.habittracker.feature_habits.domain.usecases.db

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.core.domain.repositories.DbHabitRepository
import javax.inject.Inject

//@Singleton
class UpsertHabitUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
) {

    suspend operator fun invoke(habit: Habit): Either<IoError, Int> =
        dbHabitRepository.upsertHabit(habit)
}