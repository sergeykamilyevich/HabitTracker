package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject

class DeleteAllHabitsUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, Unit> = dbHabitRepository.deleteAllHabits()
}