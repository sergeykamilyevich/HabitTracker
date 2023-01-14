package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.AddHabitDoneUseCase
import javax.inject.Inject

class AddHabitDoneUseCaseImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) : AddHabitDoneUseCase {

    override suspend operator fun invoke(habitDone: HabitDone): Either<IoError, Int> =
        dbHabitRepository.addHabitDone(habitDone)
}