package com.example.habittracker.db_impl.domain.usecases

import android.util.Log
import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.GetUnfilteredHabitListUseCase
import javax.inject.Inject

class GetUnfilteredHabitListUseCaseImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) : GetUnfilteredHabitListUseCase {

    override suspend operator fun invoke(): Either<IoError, List<Habit>> {
        Log.d("99999", "GetUnfilteredHabitListUseCaseImpl invoke")
        return dbHabitRepository.getUnfilteredList()
    }

}