package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.UpsertHabitUseCase
import javax.inject.Inject

class UpsertHabitUseCaseImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
) : UpsertHabitUseCase {

    override suspend operator fun invoke(habit: Habit): Either<IoError, Int> =
        dbHabitRepository.upsertHabit(habit)
}