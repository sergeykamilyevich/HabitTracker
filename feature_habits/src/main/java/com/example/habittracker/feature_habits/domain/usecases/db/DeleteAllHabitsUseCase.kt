package com.example.habittracker.feature_habits.domain.usecases.db

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.repositories.DbHabitRepository
import javax.inject.Inject

class DeleteAllHabitsUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, Unit> = dbHabitRepository.deleteAllHabits()
}