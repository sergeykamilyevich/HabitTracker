package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.DeleteAllHabitsUseCase
import javax.inject.Inject

class DeleteAllHabitsUseCaseImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
) : DeleteAllHabitsUseCase {

    override suspend operator fun invoke(): Either<IoError, Unit> = dbHabitRepository.deleteAllHabits()
}