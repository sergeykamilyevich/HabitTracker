package com.example.habittracker.db_api.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.HabitDone

interface AddHabitDoneUseCase {

    suspend operator fun invoke(habitDone: HabitDone): Either<IoError, Int>
}