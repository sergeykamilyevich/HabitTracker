package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject

//@Singleton
class AddHabitDoneUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(habitDone: HabitDone): Either<IoError, Int> =
        dbHabitRepository.addHabitDone(habitDone)
}