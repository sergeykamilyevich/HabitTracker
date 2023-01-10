package com.example.habittracker.feature_habits.domain.usecases.db

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.models.HabitDone
import com.example.habittracker.core.domain.repositories.DbHabitRepository
import javax.inject.Inject

//@Singleton
class AddHabitDoneUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(habitDone: HabitDone): Either<IoError, Int> =
        dbHabitRepository.addHabitDone(habitDone)
}