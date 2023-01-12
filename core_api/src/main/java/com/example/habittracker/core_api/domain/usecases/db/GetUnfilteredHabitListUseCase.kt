package com.example.habittracker.core_api.domain.usecases.db

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import javax.inject.Inject

class GetUnfilteredHabitListUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, List<Habit>> =
        dbHabitRepository.getUnfilteredList()
}